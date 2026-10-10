import {ABILITIES} from '../web/integration-data.js';
import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import * as P from '../web/progression.js';
import * as T from '../web/exploration.js';
import * as K from '../web/knowledge.js';
import {TRADE_SPECIES} from '../web/trade-data.js';
import {RULES} from '../web/integration-data.js';
import {GAME_DATA} from '../web/content-pack.js';
import {acceptOrder,deliverOrder} from '../web/story-orders.js';
const NOW=new Date(2026,8,21,11).getTime();
function state(){const s=E.freshState(NOW);s.cp=100000;s.kitchenLevel=3;s.toolLevels.fill(2);s.duck=true;for(let i=0;i<89;i++){s.total['0:'+i]=100;s.farm['0:'+i]=30;}P.syncProgress(s);return s;}
function learn(s,...ids){for(const id of ids)P.learnSkill(s,id);}
function ready(s){for(const e of s.batch.eggs){e.status='ready';e.animationAt=NOW;}}
function finish(s,at=NOW){ready(s);for(let i=0;i<24;i++)E.collect(s,i,at);}
function seeded(seed=4521){return ()=>{seed=(Math.imul(seed,1664525)+1013904223)>>>0;return seed/4294967296;};}
test('round2: 30 single-level nodes, 74 points total, independent tier-II and meaningful gate progress',()=>{
 assert.equal(P.SKILLS.length,30);assert.equal(P.SKILLS.filter(n=>n.node!=='S').reduce((n,s)=>n+s.cost,0)+4,74);
 const s=state();learn(s,'CUL-2','CUL-4');assert.equal(P.rank(s,'CUL-1'),0);assert.equal(P.rank(s,'CUL-3'),0);assert.throws(()=>P.learnSkill(s,'CUL-2'));
 const n=E.freshState(NOW);n.total={'0:0':24};assert.equal(P.skillGate(n,'CUL-1'),'');assert.match(P.skillGate(n,'CUL-2'),/发现 1\/8/);assert.throws(()=>P.learnSkill(n,'TRIP-1'));
 learn(s,'TRADE-1','TRADE-2');assert.throws(()=>P.learnSkill(s,'TRADE-5'));assert.throws(()=>P.learnSkill(s,'TRADE-S'));
});
test('round2: applying drafts is atomic, category cannot switch outside respec, respec never grants credits twice',()=>{
 const s=state(),base=JSON.stringify(s.progress.skills),before=structuredClone(s);
 assert.throws(()=>P.applySkillPlan(s,{steps:['CUL-1','CUL-S'],base},NOW));assert.deepEqual(s,before);
 P.applySkillPlan(s,{steps:['TRADE-2','TRADE-3','TRADE-4'],category:'家常',base},NOW);assert.equal(s.progress.trade.credits,1);
 assert.throws(()=>P.applySkillPlan(s,{steps:[],category:'煎炸'},NOW));
 P.applySkillPlan(s,{steps:['TRADE-2','TRADE-4'],reset:true,category:'煎炸'},NOW);assert.equal(s.progress.trade.credits,1);
 assert.equal(s.progress.trade.category,'煎炸');assert.throws(()=>P.applySkillPlan(s,{steps:[],reset:true},NOW+1));
});
test('round2: old skill migration refunds once and preserves all in-flight clocks, baskets, knowledge and orders',()=>{
 const s=state();E.startBatch(s,0,NOW,()=>.5);s.batch.rules.version=1;T.depart(s,{routeId:'yard',members:['0:0']},NOW,()=>.9);acceptOrder(s,'first-sale');deliverOrder(s,'first-sale',4,NOW);
 const old=structuredClone(s);old.version=3;delete old.progress.skillVersion;old.progress.skills={'CUL-A':3,'CUL-B':1};old.progress.trade.credits=6;old.progress.respecAt=NOW;
 const b=structuredClone(old.batch),t=structuredClone(old.progress.trip),next=E.normalizeSave(old,NOW);
 assert.deepEqual(next.progress.skills,{});assert.equal(next.progress.migrationRespec,true);assert.deepEqual(next.batch,b);assert.deepEqual(next.progress.trip,t);assert.equal(next.cp,old.cp);assert.deepEqual(next.ingredients,old.ingredients);assert.deepEqual(next.progress.orders,old.progress.orders);assert.deepEqual(E.normalizeSave(next,NOW),next);
 finish(next,NOW+120*60000);E.advanceWorld(next,NOW+3*3600000);P.respecSkills(next,NOW+3*3600000);assert.equal(next.progress.migrationRespec,false);assert.throws(()=>P.respecSkills(next,NOW+3*3600000+1));
});
test('round2: kitchen snapshot gold is once per egg, whole-batch returns once and a full pending basket disables eligibility before starting',()=>{
 const s=state();learn(s,'CUL-1','CUL-3');s.ingredients={0:30};s.selected=[0];E.startBatch(s,1,NOW,()=>0);s.ingredients[0]=30;const cp=s.cp;
 finish(s,NOW+600000);assert.equal(s.cp,cp+48);assert.equal(s.progress.leftovers.length,0);assert.equal(s.ingredients[0],31);assert.equal(s.progress.lastHarvest.bonus,24);
 assert.equal(E.collect(s,0),false);assert.equal(s.cp,cp+48);assert.equal(P.claimLeftovers(s).length,0);s.progress.leftovers=[0,0];assert.equal(P.claimLeftovers(s).length,2);assert.equal(s.ingredients[0],33);assert.equal(P.claimLeftovers(s).length,0);
 s.progress.leftovers=[0,0,0,0,0];s.selected=[0];E.startBatch(s,1,NOW+600001,()=>0);assert.equal(s.batch.rules.returnEligible,false);assert.equal(s.batch.rules.returnTicket,null);finish(s);assert.equal(s.progress.leftovers.length,5);
});
test('round2: pickup and return probabilities match confirmed rates with seeded samples',()=>{
 const rng=seeded(),s=state();learn(s,'CUL-1','CUL-3');let gold=0,returns=0;const N=3000;
 for(let i=0;i<N;i++){s.batch=null;s.cp=100000;s.ingredients={0:1};s.selected=[0];E.startBatch(s,1,NOW,rng);gold+=s.batch.eggs.filter(e=>e.gold).length;returns+=s.batch.rules.returnTicket!==null?1:0;}
 assert.ok(Math.abs(gold/(N*24)-.15)<.005);assert.ok(Math.abs(returns/N-.2)<.025);
});
test('round2: 10/15/20/25% timing, second ceiling, 30-minute reheat boundary, calm >=8h and snapshot independence',()=>{
 const s=state();learn(s,'CUL-2','CUL-4','CUL-3','CUL-S','HOME-2','HOME-3','HOME-5');s.selected=[];
 const signature=P.batchSignature(0,1,[]);for(const [special,hot,rate]of [[false,false,.1],[false,true,.15],[true,false,.2],[true,true,.25]]){
  if(special)s.progress.skills['CUL-S']=1;else delete s.progress.skills['CUL-S'];s.progress.hotStove=hot?{signature,at:NOW}:null;
  const i=E.cookInfo(s,1,NOW);assert.ok(Math.abs(i.reduction-rate)<1e-8);assert.equal(i.minutes,Math.ceil(Math.max(6,i.originalMinutes*(1-rate))*60)/60);
 }
 assert.equal(E.cookInfo(s,1,NOW+30*60000).hot,true);assert.equal(E.cookInfo(s,1,NOW+30*60000+1).hot,false);
 s.progress.protection.calm=true;s.progress.protection.freshness=false;const i=E.cookInfo(s,1,NOW);assert.equal(i.minutes,Math.ceil(Math.max(6,i.originalMinutes*.75)*1.25*60)/60);assert.ok(i.freshMinutes>=480);
 E.startBatch(s,1,NOW,()=>.5);const deadlines=s.batch.eggs.map(e=>[e.openAt,e.blackAt]);s.progress.skills={};assert.deepEqual(s.batch.eggs.map(e=>[e.openAt,e.blackAt]),deadlines);assert.deepEqual(E.normalizeSave(s,NOW).batch,s.batch);
});
test('round2: hot stove requires full collection, cancels on other recipe or discard, no extra return for special media',()=>{
 const s=state();learn(s,'CUL-2','CUL-4','CUL-3');E.startBatch(s,0,NOW,()=>0);ready(s);for(let i=0;i<23;i++)E.collect(s,i,NOW);assert.equal(s.progress.hotStove,null);E.collect(s,23,NOW);assert.equal(E.cookInfo(s,0,NOW).hot,true);
 E.startBatch(s,1,NOW+1,()=>0);assert.equal(s.progress.hotStove,null);assert.equal(s.batch.rules.returnTicket,null);
});
test('round2: explicit category covers all species, excludes special species, split sales preserve fractional markup',()=>{
 assert.equal(Object.keys(TRADE_SPECIES).length,193);for(const k of ['0:1','0:2','0:51','0:52','0:89','1:27'])assert.equal(TRADE_SPECIES[k].category,null);
 const s=state();learn(s,'TRADE-2');s.progress.trade.category='家常';const a=structuredClone(s),b=structuredClone(s);E.sell(a,{'0:0':24},{},NOW);for(let i=0;i<24;i++)E.sell(b,{'0:0':1},{},NOW);assert.equal(a.cp,b.cp);assert.equal(a.progress.trade.markupRemainder,b.progress.trade.markupRemainder);assert.equal(a.progress.trade.markupRemainder,64);
 const cp=s.cp;acceptOrder(s,'first-sale');deliverOrder(s,'first-sale',12,NOW);assert.equal(s.cp,cp+36+120);assert.equal(s.progress.trade.markupRemainder,0);
});
test('round2: basket-first then maximum platters consumes disjoint stock, opt out preserves credits, combined skills never double credits',()=>{
 const s=state();learn(s,'TRADE-1','TRADE-3','TRADE-4','TRADE-5');s.progress.trade.credits=6;
 const q=P.basketQuote(s,{'0:3':27,'0:4':3,'0:8':3,'0:10':3});assert.equal(q.baskets,1);assert.equal(q.platters,1);assert.equal(q.bonus,20);assert.equal(q.basketItems[0].count,24);assert.equal(q.platterItems[0].length,4);
 assert.equal(P.basketQuote(s,{'0:0':24},{useRewards:false}).bonus,0);s.progress.trade.credits=0;for(let i=0;i<24;i++)P.harvestCredit(s);assert.equal(s.progress.trade.credits,1);s.progress.trade.credits=6;for(let i=0;i<100;i++)P.harvestCredit(s);assert.equal(s.progress.trade.harvestProgress,23);
});
test('round2: light expedition uses 4h48/8h and actual base units, directed materials and CP are settled once even full',()=>{
 const s=state();learn(s,'TRIP-1','TRIP-2','TRIP-3','TRIP-5','TRIP-S');s.ingredients={0:30};const i=T.explorationInfo(s,'water',['0:0'],NOW,{light:true});assert.equal(i.hours,4.8);assert.equal(i.minUnits,1);assert.equal(i.directedUnits,1);assert.equal(i.cpReward,6);
 const w=T.explorationInfo(s,'wood',['0:0'],NOW,{light:true});assert.equal(w.hours,8);assert.equal(w.minUnits,2);
 T.depart(s,{routeId:'water',members:['0:0'],light:true},NOW,()=>0);const t=s.progress.trip;assert.equal(t.endAt-NOW,17280000);assert.equal(t.cpReward,6);const cp=s.cp;T.claimTrip(s,t.id,{materials:false},t.endAt);assert.equal(s.cp,cp+6);T.claimTrip(s,t.id,{},t.endAt);assert.equal(s.cp,cp+6);assert.equal(t.remaining.length,0);assert.equal(t.status,'settled');assert.deepEqual(E.normalizeSave(s,t.endAt).progress.trip,t);
});
test('round2: matching team improves odds exactly and empty clue pools show zero without advancing pity',()=>{
 const s=state();delete s.total['0:4'];delete s.farm['0:4'];const team=['0:0','0:3','0:8'];const a=T.explorationInfo(s,'yard',team,NOW);const G=team.reduce((n,k)=>n+ABILITIES[k].gather,0),F=team.reduce((n,k)=>n+ABILITIES[k].discover,0),A=team.filter(k=>ABILITIES[k].environment==='yard').length;assert.ok(Math.abs(a.materialChance-(.05+.025*G/3+.04*A))<1e-8);assert.ok(Math.abs(a.clueChance-(.10+.02*F/3+.03*A))<1e-8);learn(s,'TRIP-2','TRIP-3');const b=T.explorationInfo(s,'yard',team,NOW);assert.ok(Math.abs(b.materialChance-(.05+.025*G/3+.04*A+.03*A))<1e-8);assert.ok(Math.abs(b.clueChance-(.10+.02*F/3+.03*A+.02*A))<1e-8);
 for(const [egg,cs]of GAME_DATA.characters.entries())for(const c of cs)s.total[egg+':'+c.id]=100;assert.equal(T.explorationInfo(s,'yard',team,NOW).clueChance,0);
});
test('round2: observer records facts permanently, study stays secret, first discovery clue only once',()=>{
 const s=state();learn(s,'OBS-1','OBS-3','OBS-4','OBS-5');const info=K.observationInfo(s,'0:120',NOW);assert.equal(info.name,null);assert.equal(info.silhouette,true);assert.ok(info.details.some(d=>d.startsWith('第一味')));K.readObservation(s,'0:120',NOW);P.respecSkills(s,NOW);assert.ok(K.observationInfo(s,'0:120',NOW).details.some(d=>d.startsWith('第一味')));
 // 举一反三 records the next layer nobody shows yet: with 风味辨识 that is a first seasoning or a flavour group, never
 // the silhouette or cookware the skill already shows (2026-10-07); with 辨味笔记 as well there is nothing left to record.
 learn(s,'OBS-1','OBS-2','OBS-4','OBS-5');delete s.farm['0:3'];delete s.total['0:3'];delete s.farm['0:4'];delete s.total['0:4'];E.startBatch(s,1,NOW,()=>.5);s.batch.eggs[0].id=3;s.batch.eggs[0].status='ready';const before=s.progress.knowledge.facts.length;E.collect(s,0,NOW);assert.ok(s.progress.knowledge.facts.length>before);assert.match(s.progress.knowledge.facts.at(-1),/:L[34]$/);const after=s.progress.knowledge.facts.length;E.collect(s,0,NOW);assert.equal(s.progress.knowledge.facts.length,after);
});
test('round2: paid replication never overwrites a first seasonal surprise in the same batch',()=>{
 const s=state();learn(s,'CUL-2','CUL-3','CUL-5');s.ingredients={9:1,33:1};s.selected=[9,33];
 const choices=E.replicateOptions(s,1,NOW);assert.ok(choices.length);s.progress.replicate=choices[0].key;
 E.startBatch(s,1,NOW,()=>.1);
 assert.ok(s.batch.eggs.every(e=>e.id===121),'targeting never overwrites a seasonal result');
});
test('round2: paid replication only accepts already collected ordinary candidates and costs exactly 10CP',()=>{
 const s=state();learn(s,'CUL-2','CUL-3','CUL-5');const choices=E.replicateOptions(s,1,NOW);assert.ok(choices.length);s.progress.replicate=choices[0].key;const cost=E.tool(1).lv_2_cook_cp,cp=s.cp;E.startBatch(s,1,NOW,()=>.5);assert.equal(s.cp,cp-cost-10);assert.equal(s.batch.eggs[0].id,+choices[0].key.split(':')[1]);
 s.batch=null;s.progress.replicate='0:52';assert.throws(()=>E.startBatch(s,0,NOW));s.progress.replicate='0:120';assert.throws(()=>E.startBatch(s,2,NOW));
});
