// Work I: four regulars × four stages, one unread stage each, business and
// manual branches, activation baselines, visitor presentation and M09–M12.
import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {REGIONAL} from '../web/content-registry.js';
import {syncProgress} from '../web/progression.js';
import {execute} from '../web/game-commands.js';
import {reduceFacts} from '../web/facts.js';
import {openBusiness,advanceBusiness,BUSINESS_WINDOW_MS as WINDOW} from '../web/business.js';
import {STAGE_RULES,REGULAR_IDS,regularInfo,reconcileRegulars,readRegularStage,visitorCandidates,validateRegularsState,reconcileProgress} from '../web/regulars.js';
import {regularsModel,reportVisitors} from '../web/regular-model.js';
import {RUNTIME_REQUIREMENTS} from '../web/runtime-requirements.generated.js';
import {ownedMementos} from '../web/collection-progress.js';
import {regionInfo} from '../web/region-model.js';

const NOW=1800000000000;
function base(){
  const s=E.freshState(NOW,11);s.kitchenLevel=2;s.duck=true;s.toolLevels=[0,0,0,0,0,0,0,0,0];
  s.total={'0:0':400,'0:3':6,'0:8':6,'0:12':1,'0:16':1,'0:21':1,'1:0':6,'1:3':1};s.farm={'0:0':20,'0:3':12,'0:8':12,'1:0':12};
  s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};syncProgress(s);return E.normalizeSave(s,NOW);
}
let order=0;
const complete=(s,templateId,extra={})=>reduceFacts(s,[{kind:'orderComplete',instanceId:`order-${++order}`,templateId,variantId:`${templateId}-A`,region:extra.region??null,chapters:null,groupDeliveries:[]}]);
const service=(s,menuId,soldByKey={'0:0':6})=>reduceFacts(s,[{kind:'businessWitness',sessionId:`business-${++order}`,menuId,soldByKey,roleSales:{},fullSoldByKey:{},fullRoleSales:{},valid:true,complete:false}]);
const check=s=>validateRegularsState(s,m=>{throw Error(m);});
const pending=(s,id)=>s.expansion.regulars[id]?.pendingStage?.id??null;

test('four regulars have sixteen stages, each with a gate and two branches; every condition is compiled',()=>{
  assert.equal(REGULAR_IDS.length,4);assert.equal(Object.keys(STAGE_RULES).length,16);
  for(const r of REGIONAL.regulars)for(const st of r.stages){
    assert.equal(STAGE_RULES[st.id].alts.length,2,st.id);
    for(const field of ['gate','alternatives','recordPolicy'])assert.equal(RUNTIME_REQUIREMENTS[`${st.id}:${field}`].kind,'compiled',`${st.id}:${field}`);
  }
  for(const id of ['M09','M10','M11','M12'])assert.equal(RUNTIME_REQUIREMENTS[`${id}:unlock`].kind,'compiled');
  assert.deepEqual(REGIONAL.regulars.map(r=>r.stages.at(-1).reward),['M09','M10','M11','M12'],'stage 4 gives the memento');
});

test('RG1-1: nothing without the gate; a manual O01 completion queues the story and its note without any business',()=>{
  const early=E.normalizeSave(E.freshState(NOW,3),NOW);complete(early,'O01');assert.deepEqual(reconcileRegulars(early),[]);assert.deepEqual(early.expansion.regulars,{});
  const s=base(),cp=s.cp,farm=structuredClone(s.farm);
  assert.equal(regularInfo(s,'RG1').gateMet,true);assert.equal(regularInfo(s,'RG1').met,false);
  complete(s,'O01');assert.deepEqual(reconcileRegulars(s),['RG1-1']);
  assert.equal(pending(s,'RG1'),'RG1-1');assert.equal(s.expansion.regulars.RG1.pendingStage.branch,1);
  assert.ok(s.expansion.collections.entitlements['NOTE-RG1-1'],'note registered in the achieving transaction');
  assert.equal(s.cp,cp);assert.deepEqual(s.farm,farm);check(s);E.normalizeSave(s,NOW);
  assert.deepEqual(reconcileRegulars(s),[],'idempotent');
});

test('at most one unread stage; reading never pays and activates the next stage',()=>{
  const s=base();complete(s,'O01');reconcileRegulars(s);
  assert.equal(pending(s,'RG1'),'RG1-1');
  const cp=s.cp,farm=structuredClone(s.farm);
  const result=readRegularStage(s,'RG1');assert.deepEqual(result,{stageId:'RG1-1',next:'RG1-2'});
  assert.equal(pending(s,'RG1'),null,'RG1-2 waits for its own record');
  assert.deepEqual(s.expansion.regulars.RG1.readStages,['RG1-1']);assert.equal(s.cp,cp);assert.deepEqual(s.farm,farm);
  assert.throws(()=>readRegularStage(s,'RG1'),/暂时没有/);check(s);
});

test('不挡进度 (2026-10-07): an unread story never holds the next one back; it is recorded as read and stays readable',()=>{
  const s=base();complete(s,'O01');service(s,'MN2');reconcileRegulars(s);
  assert.equal(pending(s,'RG1'),'RG1-2','MN2 already recorded, so RG1-1 is recorded and RG1-2 waits instead');
  assert.deepEqual(s.expansion.regulars.RG1.readStages,['RG1-1']);
  assert.ok(s.expansion.collections.entitlements['NOTE-RG1-1']&&s.expansion.collections.entitlements['NOTE-RG1-2'],'both notes are registered once');
  const m=regularsModel(s).rows.find(r=>r.id==='RG1');assert.ok(m.stages[0].text,'the recorded story keeps its text for 回读');
  assert.deepEqual(reconcileRegulars(s),[]);check(s);
});

test('a real MN1 session queues RG1-1 and the next visitor step presents it; no CP beyond sales',()=>{
  const s=base();
  const frozen=visitorCandidates(s);assert.ok(frozen.includes('RG1')&&!frozen.includes('RG3')&&!frozen.includes('RG4'),'only relationships whose gate is met are frozen');
  openBusiness(s,{menuId:'MN1',stock:{'0:0':6,'0:3':6}},NOW);assert.deepEqual(s.expansion.business.active.visitorCandidates,frozen);
  advanceBusiness(s,NOW+12*WINDOW);const report=s.expansion.business.lastReport;
  assert.equal(report.validMenu,true);assert.equal(report.visitorEvents,1);
  assert.equal(pending(s,'RG1'),'RG1-1');assert.equal(s.expansion.regulars.RG1.pendingStage.branch,0);
  assert.deepEqual(s.expansion.regulars.RG1.lastVisit,{stageId:'RG1-1',sessionId:report.id});
  assert.equal(report.income,report.baseCP+report.markupCP+report.themeCP+report.bonusCP,'stories add no income line');
  assert.deepEqual(reportVisitors(s,report).map(v=>[v.id,v.stageId,v.unread]),[['RG1','RG1-1',true]]);
  E.normalizeSave(s,NOW);
});

test('pinned regular is presented first; tendency only reorders; frozen candidates ignore mid-session openings',()=>{
  const s=base();s.progress.orders['story-2']={accepted:true,choice:'0:0',delivered:1,completed:true};
  // RG2 needs the legacy river route and purchases: both hold for this rich save.
  s.total['0:0']=3000;for(let i=20;i<60;i++)s.total[`0:${i}`]=1;syncProgress(s);
  const all=visitorCandidates(s);assert.ok(all.includes('RG1')&&all.includes('RG2'));
  assert.equal(visitorCandidates(s,{pinned:'RG2'})[0],'RG2');assert.equal(visitorCandidates(s,{tendency:'discovery'})[0],'RG2');
  assert.deepEqual([...visitorCandidates(s)].sort(),[...visitorCandidates(s,{tendency:'discovery'})].sort());
});

test('RG2-3 needs MN4/MN2 and a river dish in the same session; separate records are not stitched',()=>{
  const s=base();
  reduceFacts(s,[{kind:'businessWitness',sessionId:'business-a',menuId:'MN2',soldByKey:{'0:10':3},roleSales:{},fullSoldByKey:{},fullRoleSales:{},valid:true,complete:false},
    {kind:'businessWitness',sessionId:'business-b',menuId:'MN1',soldByKey:{'0:135':1},roleSales:{},fullSoldByKey:{},fullRoleSales:{},valid:false,complete:false}]);
  assert.equal(STAGE_RULES['RG2-3'].alts[0](s),false);
  reduceFacts(s,[{kind:'businessWitness',sessionId:'business-c',menuId:'MN4',soldByKey:{'0:135':1,'0:19':3},roleSales:{},fullSoldByKey:{},fullRoleSales:{},valid:true,complete:false}]);
  assert.equal(STAGE_RULES['RG2-3'].alts[0](s),true);
});

test('RG2-4: an O05 finished before activation does not count; a new one after reading RG2-3 does; O04 display is retroactive',()=>{
  const s=base();complete(s,'O05');complete(s,'O05');
  s.expansion.regulars.RG2={readStages:['RG2-1','RG2-2'],pendingStage:{id:'RG2-3',seq:s.meta.factSeq,branch:1},activatedSeq:s.meta.factSeq,baselines:{},lastVisit:null};
  for(const id of ['NOTE-RG2-1','NOTE-RG2-2','NOTE-RG2-3'])s.expansion.collections.entitlements[id]={seq:++s.meta.factSeq,source:'test'};
  readRegularStage(s,'RG2');assert.deepEqual(s.expansion.regulars.RG2.baselines,{O05:2});
  assert.equal(pending(s,'RG2'),null,'two earlier O05 completions are before activation');check(s);
  const display=structuredClone(s);reduceFacts(display,[{kind:'orderDisplay',templateId:'O04',instanceId:'order-x'}]);reconcileRegulars(display);assert.equal(pending(display,'RG2'),'RG2-4');
  complete(s,'O05');reconcileRegulars(s);assert.equal(pending(s,'RG2'),'RG2-4');assert.ok(s.expansion.collections.entitlements.M10);check(s);
});

test('RG3-1 and RG3-4: business and purchase totals are separate branches and never add together',()=>{
  const s=base(),tea=REGIONAL.selectors.tea,snack=REGIONAL.selectors.snack.filter(k=>!tea.includes(k));
  s.expansion.facts.businessCounts={[tea[0]]:6};assert.equal(STAGE_RULES['RG3-1'].alts[0](s),false,'two tea kinds are required');
  s.expansion.facts.businessCounts={[tea[0]]:5,[tea[1]]:1};assert.equal(STAGE_RULES['RG3-1'].alts[0](s),true);
  s.expansion.facts.businessCounts={[tea[0]]:5,[tea[1]]:3,[snack[0]]:2};s.expansion.facts.orderCounts={[tea[0]]:4,[tea[1]]:2,[snack[0]]:2};
  assert.equal(STAGE_RULES['RG3-4'].alts[0](s),false);assert.equal(STAGE_RULES['RG3-4'].alts[1](s),false,'10 sold + 8 delivered are not 18 of either');
  s.expansion.facts.businessCounts[snack[0]]=10;assert.equal(STAGE_RULES['RG3-4'].alts[0](s),true);
  s.expansion.facts.orderCounts={[tea[0]]:16,[tea[1]]:2};assert.equal(STAGE_RULES['RG3-4'].alts[1](s),false,'three kinds each at least one');
});

test('RG4-1 opens the bay route (GUIDE-B) without a discovery card; the shore-sign route remains an alternative',()=>{
  const s=base();s.total['0:0']=6000;for(let i=20;i<60;i++)s.total[`0:${i}`]=1;s.total['0:128']=1;s.expansion.discovery.identified['75']=++s.meta.factSeq;s.expansion.discovery.identified['76']=++s.meta.factSeq;syncProgress(s);
  assert.equal(regularInfo(s,'RG4').gateMet,true);
  const cards=structuredClone(s.expansion.discovery.cards);complete(s,'O12');reconcileRegulars(s);
  assert.equal(pending(s,'RG4'),'RG4-1');assert.deepEqual(s.expansion.regions.guideFlags,['GUIDE-B']);assert.deepEqual(s.expansion.discovery.cards,cards,'GUIDE-B is not a 25th card');
  assert.equal(regionInfo(s,'B').missing.includes('在溪岸小集的岸边摊找到沿湾路标'),false);E.normalizeSave(s,NOW);
});

test('a full RG1 chain gives three notes and M09 exactly once; the graduated regular has no pending red dot',()=>{
  const s=base();for(let i=20;i<30;i++)s.total[`0:${i}`]=1;
  complete(s,'O01');complete(s,'O07');complete(s,'O03');reconcileRegulars(s);
  // every stage after an unread one is already met: the chain is recorded up to the last story, which waits
  assert.equal(pending(s,'RG1'),'RG1-4');assert.deepEqual(s.expansion.regulars.RG1.readStages,['RG1-1','RG1-2','RG1-3']);
  assert.ok(s.expansion.collections.entitlements.M09,'two different templates (O01, O07, O03) satisfy RG1-4');
  readRegularStage(s,'RG1');assert.equal(regularInfo(s,'RG1').complete,true);assert.deepEqual(reconcileRegulars(s),[]);
  assert.deepEqual(ownedMementos(s).filter(id=>/M09|M10|M11|M12/.test(id)),['M09']);
  const m=regularsModel(s);assert.equal(m.rows[0].complete,true);assert.equal(m.unread,0);check(s);E.normalizeSave(s,NOW);
});

test('commands reconcile regulars in the same transaction; replaying a read is refused, never paid twice',()=>{
  let s=base();complete(s,'O01');
  const once=execute({state:s,now:NOW,command:{type:'test'},reduce:()=>{}}).state;assert.equal(pending(once,'RG1'),'RG1-1');
  const read=execute({state:once,now:NOW,command:{type:'regular:read'},reduce:d=>readRegularStage(d,'RG1')}).state;
  assert.deepEqual(read.expansion.regulars.RG1.readStages,['RG1-1']);assert.equal(read.cp,once.cp);
  assert.throws(()=>execute({state:read,now:NOW,command:{type:'regular:read'},reduce:d=>readRegularStage(d,'RG1')}));
  assert.deepEqual(reconcileProgress(structuredClone(read)),[]);
});

test('old three chapters only make RG1 acquainted; migration leaves every regular unread',()=>{
  const s=base();for(const o of ['first-sale','story-2','story-3'])s.progress.orders[o]={accepted:true,choice:'0:0',delivered:12,completed:true};
  const info=regularInfo(s,'RG1');assert.equal(info.readStages.length,0);assert.deepEqual(s.expansion.regulars,{});
});

test('the regulars validator rejects out-of-order reads, missing results, stray baselines and bad visits',()=>{
  const s=base();complete(s,'O01');reconcileRegulars(s);readRegularStage(s,'RG1');check(s);
  const cases=[r=>r.RG1.readStages=['RG1-2'],r=>r.RG1.pendingStage={id:'RG1-3',seq:1,branch:0},r=>r.RG9=r.RG1,r=>r.RG1.baselines={O05:1},
    r=>r.RG1.lastVisit={stageId:'RG1-4',sessionId:'business-1'},r=>r.RG1.activatedSeq=null,r=>r.RG1.extra=1,r=>r.RG2={readStages:[],pendingStage:null,activatedSeq:null,baselines:{},lastVisit:null}];
  for(const mutate of cases){const bad=structuredClone(s);mutate(bad.expansion.regulars);assert.throws(()=>check(bad));assert.throws(()=>E.normalizeSave(bad,NOW));}
  const noNote=structuredClone(s);delete noNote.expansion.collections.entitlements['NOTE-RG1-1'];assert.throws(()=>check(noNote),/成果缺失/);
});

test('branch text shows names for menus and orders but keeps unfound cards and materials masked',async()=>{
  const {readableRequirement}=await import('../web/regular-model.js');const s=base();
  assert.equal(readableRequirement(s,'MN2有效接待一次'),'「茶香便当」有效接待一次');
  assert.equal(readableRequirement(s,'完成T-N1并用79完整收取一批'),'完成茶坡线索一并用茶坡新材料完整收取一批');
  s.expansion.discovery.identified['79']=++s.meta.factSeq;assert.match(readableRequirement(s,'用79'),/用焙香叶/);
  assert.equal(readableRequirement(s,'营业累计18只、发现40种'),'营业累计18只、发现40种','quantities are not materials');
});
