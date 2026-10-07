// 2026-10-07 batch 3: one-step order delivery, 订单情报, the 生意 home model and its links into 下一锅.
import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {REGIONAL} from '../web/content-registry.js';
import {GAME_DATA} from '../web/content-pack.js';
import {syncProgress,learnSkill} from '../web/progression.js';
import {execute} from '../web/game-commands.js';
import {acceptProposal,reserveForOrder,orderOptions} from '../web/orders.js';
import {orderStatus,completeOrderNow,orderHolds,spareFor,storyStatus,completeStoryNow} from '../web/order-delivery.js';
import {orderIntelPlan,grantOrderIntel,regionHints,orderRegion} from '../web/order-intel.js';
import {nextClueLayer,trackPartner,factId} from '../web/knowledge.js';
import {clueRegionOf,regionPartners} from '../web/clue-regions.js';
import {suggestBusinessStock} from '../web/business-advisor.js';
import {menuCore,menuDemands,orderBoard,displayDemands,todayMenuId} from '../web/business-home.js';
import {nextBatchGoals,wants} from '../web/next-batch-goals.js';
import {projectInfo,stageComplete} from '../web/projects.js';
import {lockedCount} from '../web/inventory.js';

const NOW=1800000000000;
// Every species met (two of each collected), a farm with `stock` of each, orders open, the first chapter done.
function fixture({stock=8,skills=['OBS-1'],missing=[]}={}){
  const s=E.freshState(NOW,37);s.kitchenLevel=3;s.duck=true;s.toolLevels.fill(2);s.cp=90000;
  s.total=Object.fromEntries(GAME_DATA.characters.flatMap((cs,egg)=>cs.map(c=>[`${egg}:${c.id}`,egg===0&&c.id===0?10000:2])).filter(([k])=>!missing.includes(k)));
  s.farm=Object.fromEntries(Object.keys(s.total).map(k=>[k,stock]));s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};
  s.expansion.regions.introSpecimenDone=['V','R','T','B'];syncProgress(s);for(const id of skills)learnSkill(s,id);
  return E.normalizeSave(s,NOW);
}
const propose=(s,templateId)=>{const o=s.expansion.orders;o.proposalSequence++;const p={id:`proposal-${o.proposalSequence}`,templateId,reason:'batch'};o.proposals.push(p);return p;};
// a partner of `region` the kitchen has not met (for the 情报 tests): forget what was collected of one, until a layer is open
function forget(s,keys){for(const k of keys){delete s.total[k];s.farm[k]=0;}}
function unmet(s,region){for(const k of regionPartners(region).filter(k=>k.startsWith('0:'))){const t=s.total[k],f=s.farm[k];forget(s,[k]);if(nextClueLayer(s,k,NOW))return k;s.total[k]=t;s.farm[k]=f;}return null;}
const valid=s=>{E.validateSaveSchema(structuredClone(E.normalizeSave(s,NOW)),NOW);return true;};

test('a proposal with enough spare partners is accepted and delivered by one 交付: base price per bird plus the bonus',()=>{
  const s=fixture(),p=propose(s,'O02'),cp=s.cp;
  const st=orderStatus(s,{kind:'proposal',id:p.id},NOW);assert.equal(st.ready,true);assert.equal(st.short,0);
  const r=completeOrderNow(s,{kind:'proposal',id:p.id},NOW);
  assert.equal(r.complete,true);assert.equal(r.bonusCP,REGIONAL.orders.find(o=>o.id==='O02').bonusCP);
  assert.equal(s.cp,cp+r.income);assert.equal(s.expansion.orders.proposals.length,0);assert.equal(s.expansion.orders.active.length,0);
  assert.deepEqual(s.expansion.orders.templateProgress.O02,{accepted:1,completed:1,cancelled:0,skipped:0});
  assert.ok(valid(s));
});

test('one-step delivery never takes the partners kept at home, and says how many are still short',()=>{
  const s=fixture({stock:1}),p=propose(s,'O02');
  const st=orderStatus(s,{kind:'proposal',id:p.id},NOW);
  assert.equal(st.ready,false);assert.equal(st.short,12,'one of each kind is kept at home, so nothing can go');
  assert.throws(()=>completeOrderNow(s,{kind:'proposal',id:p.id},NOW),/还差 12 只/);
  for(const k of Object.keys(s.farm))assert.equal(spareFor(s,k),0);
});

test('an order an older save accepted and reserved for is finished by the same one step, its reservation used first',()=>{
  const s=fixture(),p=propose(s,'O02'),option=orderOptions(s,'O02',NOW)[0];
  const order=acceptProposal(s,p.id,option,NOW),key=order.groups[0].allowed.find(k=>s.farm[k]>=3);reserveForOrder(s,order.id,key,2);
  assert.ok(valid(s));
  const r=completeOrderNow(s,{kind:'order',id:order.id},NOW);assert.equal(r.complete,true);assert.equal(s.expansion.orders.active.length,0);assert.ok(valid(s));
});

test('「只看不交」: 摆出来 completes with three kinds at home and takes nobody',()=>{
  const s=fixture(),p=propose(s,'O04'),farm=structuredClone(s.farm);
  const st=orderStatus(s,{kind:'proposal',id:p.id},NOW);assert.equal(st.kind,'display');assert.equal(st.ready,true);assert.equal(st.keys.length,3);
  const r=completeOrderNow(s,{kind:'proposal',id:p.id},NOW);assert.equal(r.kind,'display');assert.equal(r.income,0);assert.deepEqual(s.farm,farm);
  assert.deepEqual(displayDemands(s,NOW),[],'nothing left to make');assert.ok(valid(s));
});

test('订单情报: the tracked partner first when its main clue region is the order\'s, never a layer already known',()=>{
  const key=unmet(fixture(),'V');assert.ok(key);const s=fixture({missing:[key]});trackPartner(s,key);
  const before=nextClueLayer(s,key,NOW);assert.ok(before);
  const plan=orderIntelPlan(s,'O02','V',NOW);assert.equal(plan.kind,'clue');assert.equal(plan.clue.key,key);assert.equal(plan.clue.level,before.level);
  assert.equal(s.progress.knowledge.facts.includes(plan.clue.fact),false,'the layer was not known');
  const got=grantOrderIntel(s,'O02','V',NOW);assert.ok(s.progress.knowledge.facts.includes(got.clue.fact));
  // one layer more, or the whole recipe when that layer completes it (a recipe with one seasoning)
  assert.ok(got.advance.complete?got.advance.after===5:got.advance.after===got.advance.before+1);assert.equal(got.advance.tracked,true);assert.ok(valid(s));
});

test('订单情报: the order\'s own special find the first time (a hint on the region card), a partner clue after that',()=>{
  const s=fixture();assert.equal(orderRegion('O04'),'V');
  const first=grantOrderIntel(s,'O04','V',NOW);assert.equal(first.kind,'place');assert.equal(first.hint.cardId,'V-E1');assert.ok(first.hint.text);
  assert.deepEqual(regionHints(s,'V').map(h=>h.cardId),['V-E1']);assert.ok(valid(s));
  const again=orderIntelPlan(s,'O04','V',NOW);assert.equal(again.kind==='place'&&again.cardId==='V-E1',false,'the same find is not hinted twice');
  s.expansion.discovery.cards['V-E1']=++s.meta.factSeq;assert.deepEqual(regionHints(s,'V'),[],'found: the hint is gone');
});

test('订单情报 may tell 「其余条件」 only for a holiday, season or time-of-day recipe; trips never read layer 5',()=>{
  const s=fixture({skills:['OBS-1','OBS-3']});
  for(const key of regionPartners('V').concat(regionPartners('T'),regionPartners('R'),regionPartners('B'))){
    const trip=nextClueLayer(s,key,NOW),deep=nextClueLayer(s,key,NOW,{deep:true});
    if(trip)assert.ok(trip.level<=4);
    if(deep?.level===5)assert.ok(deep.path.conditions.some(c=>c.kind==='calendar'||String(c.label).startsWith('开火时段')),key);
  }
});

test('the hint list is checked on load',()=>{
  const s=fixture();s.progress.knowledge.hints=['V-E1'];assert.ok(valid(s));
  s.progress.knowledge.hints=['V-E1','V-E1'];assert.throws(()=>E.validateSaveSchema(structuredClone(s),NOW));
  s.progress.knowledge.hints=['nope'];assert.throws(()=>E.validateSaveSchema(structuredClone(s),NOW));
});

test('the shop\'s auto stock leaves at home what the order board is counting on',()=>{
  const s=fixture({stock:4}),p=propose(s,'O02');
  const held=orderHolds(s,NOW),plan=suggestBusinessStock(s,'MN1',{now:NOW});
  assert.ok(Object.keys(held).length);
  for(const [k,n] of Object.entries(plan.stock))assert.ok(n<=spareFor(s,k)-(held[k]??0),k);
  void p;
});

test('today\'s menu: core slots, its gaps become 下一锅 wants, and a 生意 goal card leads 「推荐」',()=>{
  const s=fixture({stock:1});
  const core=menuCore(s,'MN2',null,NOW);assert.equal(core.slots.length,3);assert.equal(core.complete,false);
  const gaps=menuDemands(s,'MN2',NOW);assert.ok(gaps.length>0);for(const d of gaps)assert.equal(d.kind,'menu');
  assert.ok(wants(s,NOW,'MN2').some(d=>d.kind==='menu'));
  const slot=core.slots.find(x=>x.makeable);if(!slot)return;
  const g=nextBatchGoals(s,NOW,{menuId:'MN2',goal:{kind:'menu',id:'MN2',key:slot.key,name:core.name}});
  if(g.goal.carded){assert.equal(g.cards[0].goal,true);assert.ok(g.cards[0].reason.startsWith('今日菜单'));}
  assert.equal(menuDemands({...s,expansion:{...s.expansion,business:{...s.expansion.business,active:{menuId:'MN1'}}}},'MN2',NOW).length,0,'not while the shop is open');
});

test('one card per pot: a set that fills more than one want says 「也能补…」 instead of another card',()=>{
  const s=fixture({stock:1});propose(s,'O02');
  const g=nextBatchGoals(s,NOW,{menuId:'MN2'});
  const ids=g.cards.map(c=>c.id);assert.equal(new Set(ids).size,ids.length);
  for(const c of g.cards)for(const a of c.also)assert.match(a,/^也/);
});

test('the order board puts what can be handed over first; the 厨房往事 chapter is one of its cards',()=>{
  const s=fixture({stock:1});propose(s,'O02');propose(s,'O04');
  const b=orderBoard(s,NOW);assert.ok(b.total>=2);
  const rank=c=>c.state==='ready'?0:c.state==='short'?1:2;for(let i=1;i<b.all.length;i++)assert.ok(rank(b.all[i-1])<=rank(b.all[i])||b.all[i-1].makeable!==b.all[i].makeable);
  assert.equal(b.all.find(c=>c.templateId==='O04').state,'ready');
  const st=storyStatus(s);if(st?.unlocked)assert.ok(b.all.some(c=>c.type==='story'));
});

test('不挡进度: a project stage that costs nothing completes by itself once met; a paid stage waits for 登记',()=>{
  const s=fixture();
  const out=execute({state:s,now:NOW,command:{type:'test'},reduce:()=>{}}).state;
  assert.equal(stageComplete(out,'PJ-1','PJ-1-A'),true,'PJ-1-A is free and met');
  const c=projectInfo(out,'PJ-1').stages.find(x=>x.id==='PJ-1-C');assert.equal(c.complete,false,'PJ-1-C costs CP');
});
