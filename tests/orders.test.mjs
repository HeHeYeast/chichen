// Work E: situational purchases with per-order reservations and group delivery.
import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {orderOptions,orderMilestone,skipProposal,acceptProposal,reserveForOrder,releaseReservation,deliverOrderGroups,displayOrder,cancelOrderInstance,ordersUnlockInfo,validateOrdersState,RELEASED_ORDER_TEMPLATES} from '../web/orders.js';
import {openBusiness,prepareBusiness} from '../web/business.js';
import {inventoryView,freeCount} from '../web/inventory.js';
import {farmLossAt} from '../web/farm-clock.js';
import {acceptOrder as acceptStory,deliverOrder as deliverStory} from '../web/story-orders.js';
import {learnSkill,syncProgress} from '../web/progression.js';
import {REGIONAL,resolveSpecies} from '../web/content-registry.js';

const NOW=1800000000000;
function fixture({duck=false}={}){
  const s=E.freshState(NOW,5);
  s.total=Object.fromEntries(['0:0','0:3','0:8','0:1','0:2','0:10','0:17','0:20','0:5'].map((k,i)=>[k,i?5:300]));
  s.farm={'0:0':30,'0:3':10,'0:8':10,'0:10':2,'0:17':2,'0:20':2};s.toolLevels[1]=0;s.toolLevels[2]=0;
  if(duck){s.duck=true;s.total['1:0']=3;s.farm['1:0']=8;}
  syncProgress(s);return E.normalizeSave(s,NOW);
}
function withProposal(s,templateId){
  // Rotation is stable; skipping others and taking later milestones reaches any eligible template.
  for(let i=0;i<30&&!s.expansion.orders.proposals.some(x=>x.templateId===templateId);i++){
    if(s.expansion.orders.proposals.length>=2)skipProposal(s,s.expansion.orders.proposals[0].id);orderMilestone(s,NOW,'batch');
  }
  const p=s.expansion.orders.proposals.find(x=>x.templateId===templateId);assert.ok(p,`${templateId} proposed`);return p;
}
const check=s=>{validateOrdersState(s,m=>{throw Error(m);});E.normalizeSave(s,NOW);};

test('O06 cannot reload a missing or out-of-variant frozen region, even if its allowed rows were rebuilt to match',()=>{
  const s=fixture(),def=REGIONAL.orders.find(o=>o.id==='O06');s.expansion.orders.sequence=1;
  s.expansion.orders.templateProgress.O06={accepted:1,completed:0,cancelled:0,skipped:0};
  const make=region=>({id:'order-1',templateId:'O06',variantId:'O06-A',kind:'purchase',rulesVersion:1,acceptedAt:NOW,region,chapters:null,minimumDistinct:2,bonusCP:def.bonusCP,
    groups:def.groups.map(g=>({id:g.id,sourceGroupId:g.id,quantity:g.quantity,allowed:g.allowed.filter(k=>g.selector!=='regionFood'||region===null||resolveSpecies(k).region===region),delivered:{}})),reserved:{},paidCP:0,deliveries:0,needsRestock:false});
  s.expansion.orders.active=[make('R')];check(s);
  for(const region of [null,'B','unknown']){const bad=structuredClone(s);bad.expansion.orders.active=[make(region)];assert.throws(()=>check(bad),/地区/);}
});

test('orders open by Work (E: O01/O04; F: front-region and tea-slope templates; G: O11) at 240 collected and 8 discoveries',()=>{
  assert.deepEqual([...RELEASED_ORDER_TEMPLATES],['O01','O04','O02','O03','O05','O06','O07','O08','O09','O10','O12','O11']);
  const s=fixture();assert.equal(ordersUnlockInfo(s).met,true);
  const early=structuredClone(s);early.total['0:0']=100;assert.equal(ordersUnlockInfo(early).met,false);assert.deepEqual(orderMilestone(early,NOW,'batch'),[]);
  assert.deepEqual(s.progress.orders,{});
});

test('O01 delivered in two batches of 6: base price each time, the 12 CP bonus only on completion',()=>{
  const s=fixture(),p=withProposal(s,'O01');const o=acceptProposal(s,p.id,{},NOW);
  assert.equal(o.variantId,'O01-B','no duck: the chicken-kitchen variant is frozen');assert.ok(o.groups[0].allowed.every(k=>k.startsWith('0:')));
  const cp=s.cp,first=deliverOrderGroups(s,o.id,[{groupId:'O01-G1',key:'0:0',quantity:6}],NOW);
  assert.deepEqual([first.baseCP,first.bonusCP,first.complete],[6*E.char(0,0).cp_1,0,false]);assert.equal(s.cp,cp+first.income);check(s);
  const second=deliverOrderGroups(s,o.id,[{groupId:'O01-G1',key:'0:3',quantity:6}],NOW+1);
  assert.equal(second.complete,true);assert.equal(second.bonusCP,12);assert.equal(second.firstResult,'NOTE-O01');
  assert.equal(s.cp,cp+first.income+second.income);assert.equal(s.expansion.orders.active.length,0);
  assert.equal(s.expansion.facts.orderTemplateCounts.O01,1);assert.ok(s.expansion.facts.predicateWitnesses['O01:complete']);
  const again=acceptProposal(s,withProposal(s,'O01').id,{},NOW);assert.equal(again.id,'order-2');
  const repeat=deliverOrderGroups(s,again.id,[{groupId:'O01-G1',key:'0:0',quantity:6},{groupId:'O01-G1',key:'0:8',quantity:6}],NOW+2);
  assert.equal(repeat.firstResult,null,'the first-result note is never granted twice');check(s);
});

test('Q is a per-order hold: never a delivery, never taken by business S, released on cancel',()=>{
  const s=fixture(),o=acceptProposal(s,withProposal(s,'O01').id,{},NOW);
  reserveForOrder(s,o.id,'0:0',8);assert.deepEqual(inventoryView(s,'0:0'),{T:30,R:0,S:0,Q:8,free:22,home:30});
  assert.throws(()=>reserveForOrder(s,o.id,'0:0',5),/还需要/);assert.throws(()=>reserveForOrder(s,o.id,'0:5',1),/不在本单/);
  s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};
  assert.throws(()=>prepareBusiness(s,{stock:{'0:0':23}}),/可用伙伴不足/);
  openBusiness(s,{stock:{'0:0':21}},NOW);assert.deepEqual(inventoryView(s,'0:0'),{T:30,R:0,S:21,Q:8,free:1,home:9});
  const cp=s.cp,r=deliverOrderGroups(s,o.id,[{groupId:'O01-G1',key:'0:0',quantity:6}],NOW);
  assert.equal(s.expansion.orders.active[0].reserved['0:0'],2,'own Q is consumed first');assert.equal(s.farm['0:0'],24);assert.equal(s.cp,cp+r.income);
  releaseReservation(s,o.id,'0:0');
  reserveForOrder(s,o.id,'0:3',3);const cancel=cancelOrderInstance(s,o.id);
  assert.deepEqual(cancel.released,{'0:3':3});assert.equal(cancel.paidCP,r.baseCP);assert.equal(freeCount(s,'0:3'),10);assert.equal(s.cp,cp+r.income,'no bonus, nothing clawed back');
  const next=acceptProposal(s,withProposal(s,'O01').id,{},NOW);assert.equal(next.id,'order-2');assert.deepEqual(next.groups[0].delivered,{},'a new instance inherits no cancelled progress');
});

test('delivering other species shrinks a hold that now exceeds the remaining need',()=>{
  const s=fixture(),o=acceptProposal(s,withProposal(s,'O01').id,{},NOW);reserveForOrder(s,o.id,'0:0',12);
  deliverOrderGroups(s,o.id,[{groupId:'O01-G1',key:'0:3',quantity:6}],NOW);
  const left=s.expansion.orders.active[0];assert.equal(left.reserved['0:0'],6);assert.equal(s.farm['0:0'],30,'the released hold stays at home');check(s);
});

test('farm loss shrinks Q to what is still at home and marks restock without penalty',()=>{
  const s=fixture(),o=acceptProposal(s,withProposal(s,'O01').id,{},NOW);reserveForOrder(s,o.id,'0:3',6);
  s.farmFixed=NOW-6*24*3600000;s.farmChecked=NOW-5*24*3600000;const cp=s.cp;farmLossAt(s,NOW,()=>.99);
  const q=s.expansion.orders.active[0].reserved['0:3']??0;assert.ok(q<=s.farm['0:3']);
  if(s.farm['0:3']<6)assert.equal(s.expansion.orders.active[0].needsRestock,true);assert.equal(s.cp,cp);check(s);
});

test('keep-one is the default for deliveries and needs an explicit override',()=>{
  const s=fixture();s.farm['0:8']=6;const o=acceptProposal(s,withProposal(s,'O01').id,{},NOW);
  assert.throws(()=>deliverOrderGroups(s,o.id,[{groupId:'O01-G1',key:'0:8',quantity:6}],NOW),/留1只/);
  deliverOrderGroups(s,o.id,[{groupId:'O01-G1',key:'0:8',quantity:6}],NOW,{overrideKeepOne:true});assert.equal(s.farm['0:8'],0);
});

test('group allowance, over-delivery and the distinct minimum are enforced per bird',()=>{
  const s=fixture(),o=acceptProposal(s,withProposal(s,'O01').id,{},NOW);
  assert.throws(()=>deliverOrderGroups(s,o.id,[{groupId:'O01-G1',key:'0:10',quantity:1}],NOW),/允许名单/);
  assert.throws(()=>deliverOrderGroups(s,o.id,[{groupId:'O01-G1',key:'0:0',quantity:13}],NOW),/还需要/);
  assert.throws(()=>deliverOrderGroups(s,o.id,[{groupId:'O01-G9',key:'0:0',quantity:1}],NOW),/需求组/);
  const before=structuredClone(s);assert.throws(()=>deliverOrderGroups(s,o.id,[{groupId:'O01-G1',key:'1:0',quantity:1}],NOW));assert.deepEqual(s,before,'a rejected delivery changes nothing');
});

test('O04 display checks one free bird each, consumes nothing, pays nothing, and never repeats',()=>{
  const s=fixture(),p=withProposal(s,'O04'),o=acceptProposal(s,p.id,{variantId:'O04-A'},NOW);
  const before={farm:structuredClone(s.farm),cp:s.cp};
  assert.throws(()=>displayOrder(s,o.id,['0:10','0:17']),/3种/);
  s.expansion.business.active=null;
  const result=displayOrder(s,o.id,['0:10','0:17','0:20']);
  assert.deepEqual(s.farm,before.farm);assert.equal(s.cp,before.cp);assert.equal(result.firstResult,'NOTE-O04');
  assert.ok(s.expansion.facts.predicateWitnesses['O04:display']);check(s);
  for(let i=0;i<4;i++)orderMilestone(s,NOW+i,'trip');
  assert.ok(!s.expansion.orders.proposals.some(x=>x.templateId==='O04'),'a finished one-time display is not proposed again');
});

test('O04 cannot show a bird that is reserved for business or away',()=>{
  const s=fixture();s.farm['0:10']=1;const o=acceptProposal(s,withProposal(s,'O04').id,{variantId:'O04-B'},NOW);
  s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};
  s.expansion.inventoryPolicy.keepOne=false;
  const stocked=structuredClone(s);openBusiness(stocked,{stock:{'0:10':1}},NOW,);
  assert.throws(()=>displayOrder(stocked,o.id,['0:10','0:17','0:20']),/在家可用/,'the only bird is on the business stall (S)');
  const away=structuredClone(s);away.progress.tripSequence=1;away.progress.trip={id:'trip-1',status:'running',members:['0:10'],remaining:[],startedAt:NOW,endAt:NOW+1};
  assert.throws(()=>displayOrder(away,o.id,['0:10','0:17','0:20']),/在家可用/);
});

test('templates without a distinct executable solution are never generated',()=>{
  const s=fixture();for(const k of ['0:3','0:8','0:10'])delete s.total[k];s.farm={'0:0':30};
  assert.deepEqual(orderOptions(s,'O12',NOW),[],'one producible species cannot satisfy two distinct');
  assert.ok(orderOptions(s,'O01',NOW).length,'one species is enough for O01');
  const two=fixture();assert.ok(orderOptions(two,'O12',NOW).length);
});

test('skipping empties a slot until a real milestone; reloading or reopening draws nothing',()=>{
  const s=fixture();orderMilestone(s,NOW,'batch');orderMilestone(s,NOW,'batch');
  assert.equal(s.expansion.orders.proposals.length,2);
  const skipped=skipProposal(s,s.expansion.orders.proposals[0].id);assert.equal(s.expansion.orders.proposals.length,1);
  const reloaded=E.normalizeSave(structuredClone(s),NOW);assert.equal(reloaded.expansion.orders.proposals.length,1,'no refill on load');
  const last=s.expansion.orders.lastProposedTemplate,order=[...RELEASED_ORDER_TEMPLATES],start=order.indexOf(last)+1;
  const eligible=order.filter(id=>orderOptions(s,id,NOW).length&&!s.expansion.orders.proposals.some(p=>p.templateId===id));
  const expected=[...order.slice(start),...order.slice(0,start)].find(id=>eligible.includes(id));
  orderMilestone(s,NOW+1,'trip');assert.equal(s.expansion.orders.proposals.length,2);
  assert.equal(s.expansion.orders.proposals[1].templateId,expected,'rotation resumes after the last proposal; skipping never rerolls');void skipped;
});

test('at most two active orders; the legacy first chapter still completes and pays once',()=>{
  const s=fixture();orderMilestone(s,NOW,'batch');orderMilestone(s,NOW,'batch');
  for(const p of [...s.expansion.orders.proposals])acceptProposal(s,p.id,{variantId:p.templateId==='O04'?'O04-A':undefined},NOW);
  orderMilestone(s,NOW,'trip');assert.throws(()=>acceptProposal(s,s.expansion.orders.proposals[0]?.id??'none',{},NOW));
  acceptStory(s,'first-sale');const cp=s.cp,first=deliverStory(s,'first-sale',12,NOW);
  assert.equal(first.complete,true);assert.equal(s.cp,cp+first.income);assert.throws(()=>deliverStory(s,'first-sale',1,NOW));
});

test('validator rejects tampered frozen terms, paid totals, over-reservation and finished instances',()=>{
  const s=fixture(),o=acceptProposal(s,withProposal(s,'O01').id,{},NOW);deliverOrderGroups(s,o.id,[{groupId:'O01-G1',key:'0:0',quantity:2}],NOW);reserveForOrder(s,o.id,'0:3',4);check(s);
  for(const mutate of [x=>x.bonusCP=99,x=>x.groups[0].allowed.push('1:0'),x=>x.paidCP++,x=>x.reserved['0:3']=11,x=>x.groups[0].delivered['0:0']=12,x=>x.variantId='O04-A',x=>x.id='order-9']){
    const bad=structuredClone(s);mutate(bad.expansion.orders.active[0]);assert.throws(()=>check(bad));
  }
});
