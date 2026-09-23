import {execute} from '../web/game-commands.js';
import test from 'node:test';
import assert from 'node:assert/strict';
import {readFileSync} from 'node:fs';
import * as E from '../web/engine.js';
import * as P from '../web/progression.js';
import * as K from '../web/knowledge.js';
import * as T from '../web/exploration.js';
import {availableCount,reservedCount} from '../web/inventory.js';
import {cookingCandidates} from '../web/candidate-query.js';
import {acceptOrder,deliverOrder} from '../web/story-orders.js';
import {RULES,ABILITIES,DESCRIPTIONS,AUTHORED_CLUES,STORY_CHAPTERS} from '../web/integration-data.js';
import {recipeId,recipePaths} from '../web/recipe-book.js';
import {claimActivity} from '../web/legacy-activities.js';
import {makeBackup,parseBackup,createSaveStore} from '../web/save-store.js';
const NOW=new Date(2026,8,21,11).getTime(),HOUR=3600000;
function full(){const s=E.freshState(NOW);s.cp=100000;s.kitchenLevel=3;s.toolLevels.fill(2);s.duck=true;for(let i=0;i<89;i++){s.total['0:'+i]=100;s.farm['0:'+i]=3;}P.syncProgress(s);return s;}
function branch(s,b,special=false){for(const n of ['1','2','3','4','5',...(special?['S']:[])])P.learnSkill(s,b+'-'+n);}
function same(s,fn){const before=structuredClone(s);assert.throws(fn);assert.deepEqual(s,before);}
test('approved machine configuration and authored content cover all 193 identities',()=>{
 const cfg=JSON.parse(readFileSync(new URL('../docs/b-group-balance/design-config.json',import.meta.url)));delete cfg.status;assert.deepEqual(RULES,cfg);
 assert.equal(Object.keys(ABILITIES).length,193);assert.equal(Object.keys(DESCRIPTIONS).length,193);assert.equal(Object.keys(AUTHORED_CLUES).length,193);assert.equal(STORY_CHAPTERS.length,3);
 for(const a of Object.values(ABILITIES))assert.equal(a.gather+a.discover,6);
});
test('point sources, branching prerequisites, exclusive specialization and respec persist without duplication',()=>{
 const s=full();for(const key of Object.keys(ABILITIES))s.total[key]=100;P.syncProgress(s);assert.equal(P.skillPoints(s).earned,54);
 same(s,()=>P.learnSkill(s,'CUL-3'));branch(s,'CUL',true);assert.equal(P.skillPoints(s).spent,18);branch(s,'HOME');same(s,()=>P.learnSkill(s,'HOME-S'));
 P.respecSkills(s,NOW);assert.equal(P.skillPoints(s).available,54);same(s,()=>P.respecSkills(s,NOW+72*HOUR-1));P.respecSkills(s,NOW+72*HOUR);
 const restored=parseBackup(makeBackup(s,NOW),NOW);assert.deepEqual(restored,s);P.syncProgress(restored);assert.equal(P.skillPoints(restored).earned,54);
});
test('new cook durations and independent freshness snapshot all 27 cookware levels',()=>{
 const s=full();branch(s,'CUL',true);P.learnSkill(s,'HOME-1');P.learnSkill(s,'HOME-4');
 for(let tool=0;tool<9;tool++)for(let lv=0;lv<3;lv++){s.toolLevels[tool]=lv;const info=E.cookInfo(s,tool),base=E.tool(tool)['lv_'+lv+'_min'];assert.equal(info.minutes,Math.ceil(Math.max(6,base*.8)*60)/60);assert.equal(info.freshMinutes,Math.max(2*base,120)+90);}
 s.progress.protection.freshness=false;assert.equal(E.cookInfo(s,1).freshMinutes,Math.max(120,2*E.cookInfo(s,1).originalMinutes));
 const b=E.startBatch(s,1,NOW,()=>.5),deadlines=b.eggs.map(e=>[e.openAt,e.blackAt]);s.progress.skills={};s.progress.protection.freshness=true;assert.deepEqual(b.eggs.map(e=>[e.openAt,e.blackAt]),deadlines);
 assert.deepEqual(E.normalizeSave(s,NOW).batch,b);
});
test('new eggs use dirt at opening, cleanup processes pending eggs before starting next cycle, legacy batches remain legacy',()=>{
 const s=E.freshState(NOW);const e=E.startBatch(s,0,NOW,()=>.5).eggs[0];e.id=3;e.openAt=NOW+1000;E.updateBatch(s,NOW+40*HOUR,()=>0);assert.equal(e.id,3,'later dirt must not retroactively sicken a clean opening');
 const dirty=E.freshState(NOW);dirty.dirty=true;E.startBatch(dirty,0,NOW,()=>.5);const d=dirty.batch.eggs[0];d.id=3;E.clean(dirty,d.openAt+1);assert.notEqual(d.status,'egg');
 const old=structuredClone(s);old.version=2;old.batch.eggs[0].status='egg';old.batch.eggs[0].id=3;delete old.batch.rules;const migrated=E.normalizeSave(old,NOW);E.updateBatch(migrated,NOW+40*HOUR,()=>0);assert.equal(migrated.batch.eggs[0].id,1);
 const cycle=full();branch(cycle,'HOME',true);assert.equal(cycle.cleanCycle.hours,36);E.clean(cycle,NOW+HOUR);assert.equal(cycle.cleanCycle.hours,72);
});
test('trade fractional rebates, whitelist baskets, overflow progress and one-time initial credit',()=>{
 const s=full();branch(s,'TRADE');assert.equal(s.progress.trade.credits,1);s.ingredients={};const price=E.ingredient(0).buy_cp,cp=s.cp;
 for(let i=0;i<10;i++)E.buyIngredient(s,0);assert.equal(s.cp,cp-price*10+Math.floor(price*10*.06));
 s.farm['0:0']=50;s.progress.trade.credits=6;assert.equal(E.sell(s,{'0:0':48},{keepOne:true},NOW),48*3+24);assert.equal(s.progress.trade.credits,4);
 s.progress.trade.credits=6;for(let i=0;i<100;i++)P.harvestCredit(s);assert.equal(s.progress.trade.harvestProgress,23);E.sell(s,{'0:0':1},{},NOW);assert.equal(s.progress.trade.credits,6);
 P.respecSkills(s,NOW);P.learnSkill(s,'TRADE-1');P.learnSkill(s,'TRADE-3');assert.equal(s.progress.trade.credits,6);
});
test('fortune excludes previous, soft pity reweights and sole hard-pity target is still obtainable',()=>{
 const s=full();s.progress.fortune={lastTarget:89,drought:3};let pool=P.fortunePool(s).pool;assert.ok(!pool.some(p=>p.id===89));assert.ok(pool.every(p=>p.weight===4));
 for(let id=89;id<103;id++)s.total['0:'+id]=1;s.progress.fortune={lastTarget:103,drought:6};assert.deepEqual(P.fortunePool(s).pool,[{id:103,weight:1}]);assert.equal(P.drawFortune(s,()=>.99),103);assert.equal(s.progress.fortune.drought,7);
 E.startBatch(s,0,NOW,()=>.5);s.batch.eggs[0].id=103;s.batch.eggs[0].status='ready';E.collect(s,0);assert.equal(s.progress.fortune.drought,0);
 P.drawFortune(s,()=>0);assert.equal(s.progress.fortune.drought,0);
 let calls=0;const fresh=E.freshState(NOW);same(fresh,()=>claimActivity(fresh,'shrine-gift',NOW,()=>{calls++;return 0;}));assert.equal(calls,0);
});
test('candidate query is pure, does not consume RNG, and enumerates gated phoenix and guaranteed steamer targets',()=>{
 const s=full(),before=structuredClone(s),original=Math.random;Math.random=()=>{throw Error('preview consumed RNG');};
 try{const q=cookingCandidates(s,0,NOW);assert.ok(q.candidates.some(c=>c.id===52&&c.status==='gate'));assert.deepEqual(s,before);
 s.egg=0;s.ingredients={9:1,16:1,25:1};s.selected=[9,16,25];const steam=cookingCandidates(s,8,NOW);for(const id of [115,116,118])assert.equal(steam.candidates.find(c=>c.id===id).guaranteed,1);
 }finally{Math.random=original;}
});
test('observation learns facts separately from discovery and unknown seasonal preparation has no guarantee marker',()=>{
 const s=full();branch(s,'OBS',true);const key='0:120',r=recipePaths(key)[0];s.ingredients=Object.fromEntries(r.ingredients.map(id=>[id,1]));
 K.readObservation(s,key,NOW);const cp=s.cp;assert.equal(K.studyRecipe(s,key,NOW).cost,50);assert.equal(s.cp,cp-50);assert.equal(K.studyRecipe(s,key,NOW).cost,0);
 assert.equal(K.observationInfo(s,key,NOW).name,null);assert.equal(s.total[key],undefined);const prepared=K.prepareKnownPath(s,recipeId(r),NOW);assert.equal(prepared.key,key);assert.equal(s.events.seasonalRecipe,undefined);
 const q=cookingCandidates(s,r.toolId,NOW);assert.equal(q.candidates.find(c=>c.key===key).status,'encounter');P.respecSkills(s,NOW);assert.equal(K.observationInfo(s,key,NOW).full,true);assert.ok(s.progress.knowledge.facts.length);
});
test('exploration reservations block sales, travel and story consumption, recall does not reward',()=>{
 const s=full();s.farm['0:0']=1;acceptOrder(s,'first-sale');const total=structuredClone(s.total);T.depart(s,{routeId:'yard',members:['0:0']},NOW,()=>.99);assert.equal(availableCount(s,'0:0'),0);assert.equal(reservedCount(s,'0:0'),1);
 same(s,()=>E.sell(s,{'0:0':1},{},NOW));same(s,()=>deliverOrder(s,'first-sale',1,NOW));same(s,()=>claimActivity(s,'time-travel',NOW));
 same(s,()=>T.depart(s,{routeId:'yard',members:['0:1']},NOW));same(s,()=>P.respecSkills(s,NOW));const ingredients=structuredClone(s.ingredients);T.recall(s,s.progress.trip.id,NOW+1);assert.equal(availableCount(s,'0:0'),1);assert.deepEqual(s.ingredients,ingredients);assert.deepEqual(s.total,total);assert.equal(s.progress.routeFailures.yard,0);
});
test('exploration auto-return, full bag, partial claim, once-only clues, import and saved tickets',()=>{
 const s=full();s.ingredients={0:30};T.depart(s,{routeId:'wood',members:['0:0','0:1','0:2']},NOW,()=>0);const id=s.progress.trip.id,end=s.progress.trip.endAt,ticket=structuredClone(s.progress.trip);
 const loaded=parseBackup(makeBackup(s,NOW),NOW);assert.deepEqual(loaded.progress.trip,ticket);E.advanceWorld(loaded,end);assert.equal(reservedCount(loaded,'0:0'),0);
 T.claimTrip(loaded,id,{},end);assert.equal(loaded.progress.trip.remaining.length,4);const facts=loaded.progress.knowledge.facts.length;T.claimTrip(loaded,id,{},end);assert.equal(loaded.progress.knowledge.facts.length,facts);same(loaded,()=>T.depart(loaded,{routeId:'yard',members:['0:0']},end));
 loaded.ingredients[0]=28;assert.equal(T.claimTrip(loaded,id,{},end).materials.length,2);assert.equal(loaded.progress.trip.remaining.length,2);const cp=loaded.cp;T.claimTrip(loaded,id,{materials:false,discard:true},end);assert.equal(loaded.progress.trip.status,'settled');assert.equal(loaded.cp,cp);assert.deepEqual(T.claimTrip(loaded,id,{},end).materials,[]);
 assert.deepEqual(E.normalizeSave(loaded,end).progress.trip,loaded.progress.trip);
});
test('trip return boundary and rollback never re-reserve, far-offline farm losses preserve away stock until return',()=>{
 const s=full();s.farm={'0:0':1};s.farmFixed=NOW-200*HOUR;s.farmChecked=NOW-120*HOUR;T.depart(s,{routeId:'yard',members:['0:0']},NOW,()=>.99);const end=s.progress.trip.endAt;E.advanceWorld(s,end,()=>.99);assert.equal(s.farm['0:0'],1);E.advanceWorld(s,NOW-1,()=>.99);assert.equal(s.progress.trip.status,'returned');assert.equal(availableCount(s,'0:0'),1);
 const t=full();T.depart(t,{routeId:'yard',members:['0:0']},NOW,()=>.5);assert.equal(T.recall(t,t.progress.trip.id,t.progress.trip.endAt).returned,true);
});
test('exploration route hard pity and no-eligible freeze use settlement once',()=>{
 const s=full();delete s.total['0:4'];delete s.farm['0:4'];s.progress.routeFailures.yard=7;T.depart(s,{routeId:'yard',members:['0:0']},NOW,()=>.99);
 assert.ok(s.progress.trip.clueOrder.length);assert.equal(s.progress.trip.clueHit,true);
 const all=full();for(const k of Object.keys(ABILITIES))all.total[k]=1;all.total['0:0']=5000;all.progress.routeFailures.yard=3;T.depart(all,{routeId:'yard',members:['0:0']},NOW,()=>.99);assert.equal(all.progress.trip.clueOrder.length,0);T.claimTrip(all,all.progress.trip.id,{discard:true},all.progress.trip.endAt);assert.equal(all.progress.routeFailures.yard,3);
});
test('three story orders pay only base prices plus once-only bonuses; partial choice locks and CP overflow is atomic',()=>{
 const s=full();s.farm['0:0']=24;acceptOrder(s,'first-sale');const cp=s.cp;deliverOrder(s,'first-sale',6,NOW);assert.equal(s.cp,cp+18);deliverOrder(s,'first-sale',6,NOW);assert.equal(s.cp,cp+36+120);same(s,()=>deliverOrder(s,'first-sale',1,NOW));
 acceptOrder(s,'tea-party');assert.ok(s.progress.knowledge.recipes.includes(recipeId(recipePaths('0:10')[0])));s.farm['0:10']=6;deliverOrder(s,'tea-party',6,NOW);
 acceptOrder(s,'signature-table','0:16');s.farm['0:16']=24;deliverOrder(s,'signature-table',1,NOW);same(s,()=>acceptOrder(s,'signature-table','0:21'));s.cp=Number.MAX_SAFE_INTEGER;same(s,()=>deliverOrder(s,'signature-table',23,NOW));s.cp=0;assert.equal(deliverOrder(s,'signature-table',23,NOW).extra,1500);assert.equal(s.progress.trade.credits,0);
});
test('invalid schema-3 reservations, ledgers, duplicate rewards and unknown knowledge are rejected without mutating input',()=>{
 const s=full();T.depart(s,{routeId:'yard',members:['0:0']},NOW,()=>.5);
 for(const mutate of [x=>x.farm['0:0']=0,x=>x.progress.trip.members.push('0:0'),x=>x.progress.trip.endAt++,x=>x.progress.trip.id='trip-999',x=>x.progress.trip.clueProcessed=true,x=>x.progress.trade.credits=7,x=>x.progress.knowledge.recipes.push('made-up'),x=>x.progress.skills['OBS-S']=1,x=>x.cleanCycle.hours=37]){const bad=structuredClone(s);mutate(bad);same(bad,()=>E.normalizeSave(bad,NOW));}
});
import vm from 'node:vm';
test('real UI commit restores the complete state after disk failure at every new transaction boundary',()=>{
 const source=readFileSync(new URL('../web/app.js',import.meta.url),'utf8');const code=source.slice(source.indexOf('function commitProgress('),source.indexOf('let toastTimer;'));
 const start=full();start.farm['0:0']=40;
 const running=structuredClone(start);T.depart(running,{routeId:'yard',members:['0:0']},NOW,()=>0);
 const returned=structuredClone(running);E.advanceWorld(returned,returned.progress.trip.endAt);
 const order=structuredClone(start);acceptOrder(order,'first-sale');
 const skill=structuredClone(start);branch(skill,'OBS');
 const gift=structuredClone(start);claimActivity(gift,'shrine',NOW);
 const batch=structuredClone(start);E.startBatch(batch,0,NOW,()=>.5);batch.batch.eggs[0].status='ready';
 for(const [state,at,fn]of [[start,NOW,s=>P.learnSkill(s,'CUL-1')],[start,NOW,s=>acceptOrder(s,'first-sale')],[order,NOW,s=>deliverOrder(s,'first-sale',12,NOW)],[start,NOW,s=>T.depart(s,{routeId:'yard',members:['0:0']},NOW,()=>0)],[running,NOW+1,s=>T.recall(s,s.progress.trip.id,NOW+1)],[returned,returned.progress.trip.endAt,s=>T.claimTrip(s,s.progress.trip.id,{},returned.progress.trip.endAt)],[skill,NOW,s=>K.studyRecipe(s,'0:120',NOW)],[gift,NOW,s=>claimActivity(s,'shrine-gift',NOW,()=>0)],[batch,NOW,s=>E.collect(s,0)]]){
  const before=structuredClone(state);const context={state:structuredClone(state),committedState:structuredClone(state),review:false,recoveryError:'',lastSaveError:'',execute,saveStore:{write(){throw Error('disk failure');}},structuredClone,E,now:()=>at,save:()=>false,renderControls(){},makeWalkers(){},sound(){},alertBox(){}};
  vm.createContext(context);vm.runInContext(code,context);assert.equal(context.commitProgress(fn),null);assert.deepEqual(context.state,before);
 }
});
test('every supported speed rank across all cookware levels follows original T0 and every clean cycle boundary charges proportionally',()=>{
 for(const [skills,reduction]of [[{},0],[{'CUL-2':1},.10],[{'CUL-S':1},.20]]){
  const s=full();s.progress.skills=skills;
  for(let t=0;t<9;t++)for(let l=0;l<3;l++){s.toolLevels[t]=l;const b=E.tool(t)['lv_'+l+'_min'],i=E.cookInfo(s,t);assert.equal(i.minutes,Math.ceil(Math.max(6,b*(1-reduction))*60)/60);assert.equal(i.freshMinutes,Math.max(120,2*b));}
 }
 for(const hours of RULES.housekeeping.cleanHours){const s=E.freshState(NOW);s.cleanCycle={hours,dirtyAt:NOW+hours*HOUR};assert.equal(E.kitchenCleanInfo(s,NOW+hours*HOUR-1).dirty,false);assert.equal(E.kitchenCleanInfo(s,NOW+hours*HOUR).dirty,true);assert.equal(E.cleanCost(s,NOW+hours*HOUR/2),50);}
});
test('point threshold grants and permanent old-stock discovery survive sale and repeated migrations',()=>{
 const s=E.freshState(NOW);for(let i=0;i<10;i++){s.total['0:'+i]=1;assert.equal(P.skillPoints(s).earned,Math.floor((i+1)/5));}
 s.farm['1:0']=1;const old=structuredClone(s);old.version=2;const migrated=E.normalizeSave(old,NOW);E.sell(migrated,{'1:0':1},{},NOW);assert.equal(migrated.total['1:0'],1);assert.equal(P.discoveryCount(migrated),11);assert.deepEqual(E.normalizeSave(migrated,NOW),migrated);
 for(const h of [24,120,500,2000,5000]){const a=E.freshState(NOW);a.total['0:0']=h-1;const before=P.skillPoints(a).earned;a.total['0:0']=h;assert.equal(P.skillPoints(a).earned-before,2);}
});
