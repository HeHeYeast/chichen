import test from 'node:test';
import {freshOrders} from '../web/orders.js';
import assert from 'node:assert/strict';
import {freshState,normalizeSave} from '../web/engine.js';
import {freshBusiness,openBusiness,advanceBusiness,closeBusiness,settleBusinessClose,prepareBusiness,businessCapacity,businessBonusQuote,BUSINESS_WINDOW_MS as WINDOW,nextBusinessBoundary} from '../web/business.js';
import {freshFacts,reduceFacts} from '../web/facts.js';
import {REGIONAL,resolveSpecies} from '../web/content-registry.js';
import {assignMenuRoles,menuSnapshot,menuFit,menuSalesWitness} from '../web/menu-model.js';
import {businessModel} from '../web/business-model.js';
import {freeCount,homeCount,inventoryView} from '../web/inventory.js';
import {validateBusinessState} from '../web/business-save.js';
import {evaluate} from '../web/requirements.js';
import {syncProgress} from '../web/progression.js';
import {execute} from '../web/game-commands.js';
import {createSaveStore} from '../web/save-store.js';

const NOW=1800000000000;
function fixture({kitchen=0,rewards=false}={}){
  const s=freshState(NOW);s.kitchenLevel=kitchen;s.farm={'0:0':25,'0:3':25,'0:8':25};s.total={'0:0':240,'0:3':1,'0:8':1};
  s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};
  s.expansion.business=freshBusiness();s.expansion.facts=freshFacts();s.expansion.orders=freshOrders();
  s.expansion.inventoryPolicy={keepOne:true,collectionLocks:[],optionalOrderReservations:{}};
  if(rewards){s.progress.skills={'TRADE-2':1,'TRADE-3':1};s.progress.trade.category='家常';s.progress.trade.credits=1;}
  syncProgress(s);return s;
}
const stock={'0:0':24};
test('open reserves S without reducing farm, paying CP, granting harvest, or prematurely recording a menu',()=>{
  const s=fixture(),farm=structuredClone(s.farm),total=structuredClone(s.total),cp=s.cp;
  openBusiness(s,{stock},NOW);assert.deepEqual(s.farm,farm);assert.deepEqual(s.total,total);assert.equal(s.cp,cp);assert.equal(freeCount(s,'0:0'),1);assert.equal(homeCount(s,'0:0'),1);assert.equal(s.expansion.facts.menuWitnesses.MN1,undefined);
  assert.equal(nextBusinessBoundary(s),NOW+WINDOW);assert.throws(()=>openBusiness(s,{stock},NOW),/收摊/);
});

test('0 / 2h minus 1 / exact 2h / 24h and weeks stop at sold-out without repeated money',()=>{
  const s=fixture();openBusiness(s,{stock},NOW);
  for(const at of [NOW,NOW+WINDOW-1]){assert.equal(advanceBusiness(s,at).sold,0);assert.equal(s.cp,600);}
  assert.equal(advanceBusiness(s,NOW+WINDOW).sold,6);assert.equal(s.farm['0:0'],19);assert.equal(s.expansion.business.active.processedWindow,1);
  assert.equal(advanceBusiness(s,NOW+24*3600000).sold,18);assert.equal(s.expansion.business.active,null);assert.equal(s.expansion.business.lastReport.totalSold,24);
  const after=structuredClone(s);advanceBusiness(s,NOW+30*24*3600000);closeBusiness(s,NOW+30*24*3600000);assert.deepEqual(s,after);
});

test('95CP audited example (rules 1, a session opened before 2026-10-07) uses one shared markup remainder and a separate theme remainder',()=>{
  const s=fixture({rewards:true}),start=s.cp;openBusiness(s,{stock,useRewards:true},NOW);s.expansion.business.active.rulesVersion=1;
  assert.equal(s.progress.trade.credits,1);assert.equal(s.expansion.business.active.creditReserve,1);
  advanceBusiness(s,NOW+24*3600000);const r=s.expansion.business.lastReport;
  assert.deepEqual([r.baseCP,r.markupCP,r.themeCP,r.bonusCP,r.income],[72,8,3,12,95]);assert.equal(s.cp,start+95);
  assert.equal(s.progress.trade.markupRemainder,64);assert.equal(s.expansion.business.themeRemainder,60);assert.equal(s.progress.trade.credits,0);assert.equal(r.creditsUsed,1);
  assert.equal(s.progress.trade.harvestProgress,0);assert.equal(s.expansion.business.visitorSequence,2);
});

test('18 sales early-close returns six unsold reservations and refunds unused credit reservation only',()=>{
  const s=fixture({rewards:true});openBusiness(s,{stock,useRewards:true},NOW);const report=closeBusiness(s,NOW+3*WINDOW+1000);
  assert.equal(report.totalSold,18);assert.equal(report.bonusCP,0);assert.equal(report.creditsUsed,0);assert.equal(report.creditsReleased,1);assert.equal(report.remainingStock['0:0'],6);
  assert.equal(s.farm['0:0'],7);assert.equal(freeCount(s,'0:0'),7);assert.equal(s.progress.trade.credits,1);
});

test('split online windows and one offline advance produce identical complete state',()=>{
  const a=fixture({kitchen:2,rewards:true}),b=structuredClone(a);a.farm['0:0']=73;b.farm['0:0']=73;
  openBusiness(a,{stock:{'0:0':72},useRewards:true},NOW);openBusiness(b,{stock:{'0:0':72},useRewards:true},NOW);
  for(let i=1;i<=12;i++)advanceBusiness(a,NOW+i*WINDOW);advanceBusiness(b,NOW+24*3600000);assert.deepEqual(a,b);assert.equal(a.expansion.business.lastReport.windowReports.length,12);
});

test('backward clocks cannot repeat windows; later collected stock is never added to the current session',()=>{
  const s=fixture();openBusiness(s,{stock},NOW);advanceBusiness(s,NOW+2*WINDOW);const money=s.cp;advanceBusiness(s,NOW+1);assert.equal(s.cp,money);
  s.farm['0:0']+=48;advanceBusiness(s,NOW+10*WINDOW);assert.equal(s.expansion.business.lastReport.totalSold,24);assert.equal(s.farm['0:0'],49);
});

test('keep-one is at-home, Q and R cannot be stocked twice, locks require explicit override',()=>{
  const s=fixture();s.farm['0:0']=25;s.progress.trip={status:'running',members:['0:0']};
  assert.throws(()=>prepareBusiness(s,{stock}),/留1只/);s.expansion.orders.active=[{id:'order-1',reserved:{'0:0':2}}];assert.throws(()=>prepareBusiness(s,{stock,overrideKeepOne:true}),/可用伙伴不足/);
  s.progress.trip=null;s.expansion.orders.active=[];s.expansion.inventoryPolicy.collectionLocks=['0:0'];assert.throws(()=>prepareBusiness(s,{stock}),/锁定/);
  openBusiness(s,{stock,overrideLocks:true},NOW);assert.deepEqual(inventoryView(s,'0:0'),{T:25,R:0,S:24,Q:0,free:1,home:1});
});

test('capacities are 24/48/72/72; inedible/new locked menus/empty/bad quantities reject before mutating',()=>{
  assert.deepEqual([0,1,2,3].map(k=>businessCapacity({kitchenLevel:k})),[24,48,72,72]);
  const s=fixture();s.farm['0:1']=20;const before=structuredClone(s);
  for(const options of [{stock:{}},{stock:{'0:0':25}},{stock:{'0:1':2}},{stock:{'0:0':1.5}},{stock,menuId:'MN2'},{stock:{'0:0':-1}}])assert.throws(()=>openBusiness(s,options,NOW));
  assert.deepEqual(s,before);
});

test('window tier is recomputed from remaining stock; complete sales are counted only from complete windows',()=>{
  const s=fixture();openBusiness(s,{stock:{'0:0':12,'0:3':12}},NOW);advanceBusiness(s,NOW+4*WINDOW);
  const r=s.expansion.business.lastReport;assert.deepEqual(r.windowReports.map(w=>w.tier),['complete','complete','suitable','suitable']);assert.equal(r.completeMenu,true);
  assert.equal(s.expansion.facts.menuWitnesses.MN1.count,1);assert.equal(s.expansion.facts.menuWitnesses.MN1.completeCount,1);
});

test('prices and skill percentages freeze at start; shared markup remains shared with outside sales',()=>{
  const s=fixture({rewards:true});openBusiness(s,{stock},NOW);s.progress.skills={};s.progress.trade.category='茶饮';s.progress.trade.markupRemainder=99;
  advanceBusiness(s,NOW+WINDOW);assert.equal(s.expansion.business.active.markupCP,3);assert.equal(s.progress.trade.markupRemainder,15);
});

test('rules 1: theme cap is per bird, exactly 200 integer percent units before rounding',()=>{
  const s=fixture();openBusiness(s,{stock},NOW);s.expansion.business.active.rulesVersion=1;s.expansion.business.active.prices['0:0'].baseCP=100;
  advanceBusiness(s,NOW+WINDOW);assert.equal(s.expansion.business.active.baseCP,600);assert.equal(s.expansion.business.active.themeCP,12);assert.equal(s.expansion.business.themeRemainder,0);
});

test('rules 2: a suitable menu adds nothing; a complete one adds 25% to every menu bird, with no cap',()=>{
  const plain=fixture();openBusiness(plain,{stock},NOW);assert.equal(plain.expansion.business.active.rulesVersion,2);
  plain.expansion.business.active.prices['0:0'].baseCP=100;advanceBusiness(plain,NOW+WINDOW);assert.equal(plain.expansion.business.active.themeCP,0);
  const full=fixture();openBusiness(full,{stock:{'0:0':12,'0:3':12}},NOW);const a=full.expansion.business.active;
  for(const key of Object.keys(a.prices))a.prices[key].baseCP=100;
  advanceBusiness(full,NOW+WINDOW);assert.equal(a.windowReports[0].tier,'complete');assert.equal(a.themeCP,150);
  for(const e of a.windowReports[0].entries)assert.equal(e.themeCP,25);
});

test('theme markup applies only to birds filling a menu role, never to ordinary placements',()=>{
  const s=fixture();openBusiness(s,{stock:{'0:0':6,'0:3':6,'0:8':6}},NOW);
  const a=s.expansion.business.active;for(const key of Object.keys(a.prices))a.prices[key].baseCP=100;
  assert.ok(a.roles.some(r=>r.roleId==='ordinary'&&r.keys.length));
  advanceBusiness(s,NOW+WINDOW);const entries=a.windowReports[0].entries;
  assert.ok(entries.some(e=>e.roleId==='ordinary'));for(const e of entries)assert.equal(e.themeCP,e.roleId==='ordinary'?0:25);
});
// Batch 5 playtest: judged window by window, the 25% stopped once the menu birds left in stock no longer made the menu.
test('a shop that opened complete pays its menu birds 25% in every window, also once they run low; one that opened suitable never',()=>{
  const s=fixture();openBusiness(s,{stock:{'0:0':6,'0:3':6,'0:8':6}},NOW);
  const a=s.expansion.business.active;for(const key of Object.keys(a.prices))a.prices[key].baseCP=100;
  advanceBusiness(s,NOW+3*WINDOW,{deferClose:true});
  assert.equal(a.windowReports[0].tier,'complete');assert.ok(a.windowReports.slice(1).some(w=>w.tier!=='complete'));
  const later=a.windowReports.slice(1).flatMap(w=>w.entries);assert.ok(later.some(e=>e.roleId!=='ordinary'));
  for(const w of a.windowReports)for(const e of w.entries)assert.equal(e.themeCP,e.roleId==='ordinary'?0:25);
  assert.doesNotThrow(()=>validateBusinessState(s));
  const t=fixture();openBusiness(t,{stock:{'0:0':6,'0:3':2,'0:8':6}},NOW);const b=t.expansion.business.active;
  advanceBusiness(t,NOW+2*WINDOW,{deferClose:true});assert.notEqual(b.windowReports[0].tier,'complete');for(const w of b.windowReports)for(const e of w.entries)assert.equal(e.themeCP,0);
});
test('rules 1 sessions still pay their menu markup to menu-role birds only',()=>{
  const s=fixture();openBusiness(s,{stock:{'0:0':6,'0:3':6,'0:8':6}},NOW);
  const a=s.expansion.business.active;a.rulesVersion=1;for(const key of Object.keys(a.prices))a.prices[key].baseCP=100;
  advanceBusiness(s,NOW+WINDOW);for(const e of a.windowReports[0].entries)assert.equal(e.themeCP===0,e.roleId==='ordinary');
});

test('ordinary food sells normally without acquiring a false menu witness',()=>{
  const s=fixture();s.farm['0:4']=25;openBusiness(s,{stock:{'0:4':24}},NOW);advanceBusiness(s,NOW+4*WINDOW);
  assert.equal(s.expansion.business.lastReport.totalSold,24);assert.equal(s.expansion.business.lastReport.themeCP,0);assert.equal(s.expansion.business.lastReport.validMenu,false);
});

test('timeline may defer close to check farm loss before reservation release, and reports stay read-only',()=>{
  const s=fixture();openBusiness(s,{stock},NOW);advanceBusiness(s,NOW+4*WINDOW,{deferClose:true});assert.ok(s.expansion.business.active);assert.equal(s.expansion.business.active.pendingCloseAt,NOW+4*WINDOW);
  settleBusinessClose(s,NOW+4*WINDOW,'sold-out');const before=structuredClone(s);businessModel(s,NOW+4*WINDOW);businessModel(s,NOW+4*WINDOW);assert.deepEqual(s,before);
});

test('basket reward consumes 24 of one eligible species first and platters use only remaining four species times three',()=>{
  const snapshot={basket:true,platter:true,basketBonus:12,platterBonus:8,basketEligible:['0:0'],platterEligible:['0:0','0:3','0:4','0:8']};
  const q=businessBonusQuote(snapshot,{'0:0':27,'0:3':3,'0:4':3,'0:8':3},2);assert.deepEqual([q.baskets,q.platters,q.bonusCP,q.creditsUsed],[1,1,20,2]);
  const insufficient=businessBonusQuote(snapshot,{'0:0':24,'0:3':3,'0:4':3,'0:8':3},2);assert.equal(insufficient.platters,0);
});

test('all 16 authored menu examples have complete compatible assignments without opening E gameplay',()=>{
  const s=fixture({kitchen:3});s.expansion.discovery.identified={'75':1,'76':2,'77':3,'78':4,'79':5,'80':6,'81':7,'82':8};
  for(const m of REGIONAL.menus)for(const example of m.examples){const goods=Object.fromEntries(example.map(x=>[x.key,x.quantity])),roles=assignMenuRoles(m.id,goods),snapshot=menuSnapshot(s,m.id,goods,roles);assert.equal(menuFit(m.id,goods,roles,snapshot).complete,true,m.id+' '+JSON.stringify(example));}
});

test('menu roles reject reuse of a single species across two groups and witness cannot combine sessions',()=>{
  assert.throws(()=>assignMenuRoles('MN2',{'0:10':6},[{roleId:'MN2-R1',keys:['0:10']},{roleId:'MN2-R2',keys:['0:10']}]),/只能/);
  assert.equal(menuSalesWitness('MN2',{'0:10':6},{'MN2-R1':6},{},{}).valid,false);
  assert.equal(menuSalesWitness('MN2',{'0:0':6},{'MN2-R2':6},{},{}).valid,false);
});

test('RG2-3 and RG3-3 require matching species and roles in one valid session, never stitched historical counters',()=>{
  const s=fixture();reduceFacts(s,[{kind:'businessWitness',sessionId:'a',menuId:'MN2',soldByKey:{'0:10':3,'0:0':3},roleSales:{'MN2-R1':3,'MN2-R2':3},fullSoldByKey:{},fullRoleSales:{},valid:true,complete:false}]);
  reduceFacts(s,[{kind:'businessSale',sessionId:'b',menuId:'MN1',windowIndex:1,roleId:'ordinary',key:'0:134',quantity:1,tier:'ordinary'}]);assert.equal(s.expansion.facts.predicateWitnesses['RG2-3:business'],undefined);
  reduceFacts(s,[{kind:'businessWitness',sessionId:'c',menuId:'MN2',soldByKey:{'0:10':5,'0:134':1},roleSales:{'MN2-R1':5,'MN2-R2':1},fullSoldByKey:{},fullRoleSales:{},valid:true,complete:false}]);assert.equal(s.expansion.facts.predicateWitnesses['RG2-3:business'].sourceId,'c');
  reduceFacts(s,[{kind:'businessWitness',sessionId:'d',menuId:'MN5',soldByKey:{'0:10':3,'0:18':3},roleSales:{'MN5-R1':3,'MN5-R2':3},fullSoldByKey:{},fullRoleSales:{},valid:true,complete:false}]);
  // A savory pair is insufficient even if older sweet sales exist.
  assert.equal(s.expansion.facts.predicateWitnesses['RG3-3:business'],undefined);
});

test('trip facts retain member snapshots and SP-LEAF requires an additional leaf/tea witness even on fruit event R-E1',()=>{
  const s=fixture(),member=(key,traits)=>({key,gather:4,discover:2,environment:'water',traits});
  reduceFacts(s,[{kind:'tripComplete',tripId:'trip-1',region:'R',placeId:'R:0',focus:'lore',cardId:'R-E1',members:[member('0:6',['fruit'])]}]);assert.equal(s.expansion.facts.predicateWitnesses['SP-LEAF:practice'],undefined);
  reduceFacts(s,[{kind:'tripComplete',tripId:'trip-2',region:'R',placeId:'R:0',focus:'lore',cardId:'R-E1',members:[member('0:6',['fruit']),member('0:10',['tea'])]}]);assert.ok(s.expansion.facts.predicateWitnesses['SP-LEAF:practice']);assert.equal(s.expansion.facts.companionFirst['0:6'].tripId,'trip-1');
});

test('requirements fail closed, honor counter source and distinct witnesses, and preserve since baselines',()=>{
  const s=fixture();s.expansion.facts.businessCounts={'0:10':12,'1:39':5,'1:40':1};s.expansion.facts.orderCounts={'0:10':1};
  assert.equal(evaluate({kind:'unavailable',target:0},s).met,false);assert.equal(evaluate({kind:'unrecognized',target:0},s).met,false);
  assert.equal(evaluate({kind:'counterAtLeast',source:'business',selector:'tea',minimumDistinct:3,target:18},s).met,true);
  assert.equal(evaluate({kind:'counterAtLeast',source:'orders',selector:'tea',minimumDistinct:3,target:18},s).met,false);
  assert.equal(evaluate({kind:'counterAtLeast',source:'business',selector:'tea',since:{'0:10':12},target:18},s).met,false);
  assert.equal(evaluate({kind:'counterAtLeast',sources:['business','orders'],selector:'tea',minimumDistinct:3,target:19},s).met,true);
  assert.equal(evaluate({kind:'counterAtLeast',source:'invalid',target:0},s).met,false);
});

test('business save failure after calculation leaves both CP and farm at their persisted values',()=>{
  const s=fixture();let raw=JSON.stringify(normalizeSave(s,NOW)),fail=false;
  const storage={getItem:k=>k==='business-test'?raw:null,setItem(k,v){if(k==='business-test'){if(fail)throw Error('disk full');raw=v;}}};
  const store=createSaveStore({storage,key:'business-test',now:()=>NOW});let state=store.load().state;
  state=execute({state,store,command:{type:'openBusiness',stock},now:NOW,reduce:draft=>openBusiness(draft,{stock},NOW)}).state;
  const before=structuredClone(state);fail=true;
  assert.throws(()=>execute({state,store,command:{type:'advanceBusiness'},now:NOW+WINDOW,reduce:draft=>advanceBusiness(draft,NOW+WINDOW)}),/disk full/);
  assert.deepEqual(state,before);assert.deepEqual(JSON.parse(raw),before);
});
