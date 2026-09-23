import test from 'node:test';
import assert from 'node:assert/strict';
import {mkdir,readdir,readFile,writeFile,copyFile,rm,realpath} from 'node:fs/promises';
import {resolve,join,sep} from 'node:path';
import {pathToFileURL} from 'node:url';
import {randomUUID} from 'node:crypto';
import * as E from '../web/engine.js';
import {LEGACY193} from '../web/legacy-content.js';
import {earnedSources} from '../web/progression.js';
import {ROLLBACK_POLICY,newOperationsEnabled} from '../web/rollback-policy.js';
import {departRegional} from '../web/regional-exploration.js';
import {prepareRegionalRecipe} from '../web/regional-methods.js';
import {openBusiness} from '../web/business.js';
import {orderMilestone,acceptProposal,orderOptions,reserveForOrder} from '../web/orders.js';
import {reduceFacts} from '../web/facts.js';
import {reconcileRegulars} from '../web/regulars.js';
import {reconcileEntitlements} from '../web/collection-progress.js';
import {completeProjectStage,deliverProject} from '../web/projects.js';
import {policyOverlay,PAUSED_POLICY} from '../tools/build-compatible-rollback.mjs';
const NOW=1800000000000,H=3600000;

// A separate real module graph gets a different immutable build configuration.
// Production has no mutable test override, save flag, or alternate interpreter.
async function isolatedPolicy(config,fn){
  const area=resolve('artifacts/takeover/rollback-tests'),dir=join(area,randomUUID());await mkdir(join(dir,'web'),{recursive:true});
  try{
    for(const entry of await readdir(resolve('web'),{withFileTypes:true}))if(entry.isFile()&&/\.(js|json)$/.test(entry.name))await copyFile(resolve('web',entry.name),join(dir,'web',entry.name));
    const policy=await readFile(join(dir,'web/rollback-policy.js'),'utf8');
    const replacement=`export const ROLLBACK_POLICY=Object.freeze({...${JSON.stringify(config)},regions:Object.freeze(${JSON.stringify(config.regions)})});`;
    await writeFile(join(dir,'web/rollback-policy.js'),policy.replace(/^export const ROLLBACK_POLICY=.*$/m,replacement));
    const load=name=>import(pathToFileURL(join(dir,'web',name+'.js')).href);
    await fn(load);
  }finally{
    const actual=await realpath(dir),base=await realpath(area);
    if(!actual.startsWith(base+sep))throw Error('Rollback fixture cleanup escaped its workspace');
    await rm(actual,{recursive:true,force:true});
  }
}
const allOff={region:false,business:false,orders:false,collections:false,regulars:false,projects:false,ui:false,regions:{V:false,R:false,T:false,B:false}};
function base(){
  const s=E.freshState(NOW,21);s.kitchenLevel=3;s.duck=true;s.toolLevels=s.toolLevels.map(()=>2);s.cp=1000000;
  s.total=Object.fromEntries(LEGACY193.characters.flatMap((list,egg)=>list.map(c=>[`${egg}:${c.id}`,3])));s.total['0:0']=9000;
  s.farm=Object.fromEntries(Object.keys(s.total).map(k=>[k,30]));s.progress.sources=earnedSources(s);
  s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};
  s.expansion.discovery.cards['V-S1']=++s.meta.factSeq;s.expansion.discovery.identified[75]=++s.meta.factSeq;
  s.expansion.regions.introSpecimenDone=['V'];s.expansion.methods.directions=['REC-V-C1'];s.expansion.methods.full=['REC-V-C1'];s.ingredients={75:1};
  return E.normalizeSave(s,NOW);
}
const paused=fn=>assert.throws(fn,e=>e.code==='FEATURE_PAUSED');

test('release policy defaults open and validates each feature/region independently',()=>{
  assert.ok(Object.isFrozen(ROLLBACK_POLICY)&&Object.isFrozen(ROLLBACK_POLICY.regions));
  for(const feature of ['region','business','orders','collections','regulars','projects','ui'])assert.equal(newOperationsEnabled(feature),true);
  for(const region of ['V','R','T','B'])assert.equal(newOperationsEnabled('region',region),true);
  assert.throws(()=>newOperationsEnabled('unknown'));assert.throws(()=>newOperationsEnabled('region','X'));
});

test('rollback overlay changes only the immutable build object and rejects an incomplete policy',async()=>{
  const source=await readFile(resolve('web/rollback-policy.js'),'utf8'),overlay=policyOverlay(source);
  const strip=s=>s.replace(/^export const ROLLBACK_POLICY=.*$/m,'');assert.equal(strip(source),strip(overlay));
  assert.ok(overlay.includes('"business":false'));assert.ok(overlay.includes('"V":false'));assert.equal(newOperationsEnabled('business'),true,'preparing an overlay does not install it');
  assert.throws(()=>policyOverlay(source,{...PAUSED_POLICY,regions:{V:false}}));assert.throws(()=>policyOverlay('changed source'));
});

test('all-off compatible build rejects new work but settles frozen trip, batch, business and orders then round-trips schema6',async()=>{
  const source=base();reconcileEntitlements(source);
  reduceFacts(source,[{kind:'orderComplete',templateId:'O01',instanceId:'order-900'},{kind:'businessWitness',menuId:'MN2',sessionId:'business-900',soldByKey:{'0:0':6},fullSoldByKey:{},roleSales:{},fullRoleSales:{},valid:true,complete:false}]);reconcileRegulars(source);
  assert.equal(source.expansion.regulars.RG1.pendingStage.id,'RG1-1');
  orderMilestone(source,NOW,'test');const proposal=source.expansion.orders.proposals.find(p=>p.templateId!=='O04')??source.expansion.orders.proposals[0];
  const option=orderOptions(source,proposal.templateId,NOW)[0],order=acceptProposal(source,proposal.id,option,NOW);assert.equal(order.kind,'purchase');
  const group=order.groups[0],key=group.allowed.find(k=>(source.farm[k]??0)>=group.quantity+1);reserveForOrder(source,order.id,key,1);
  prepareRegionalRecipe(source,'REC-V-C1');E.startBatch(source,1,NOW);departRegional(source,{regionId:'V',placeId:'V:0',focus:'materials',members:['0:0']},NOW);
  const businessKey=['0:3','0:8'].find(k=>k!==key);openBusiness(source,{menuId:'MN1',stock:{[businessKey]:12}},NOW);
  const original=E.normalizeSave(source,NOW),entitlements=structuredClone(original.expansion.collections.entitlements),events=structuredClone(original.events);
  await isolatedPolicy(allOff,async load=>{
    const [e,trip,methods,business,orders,regulars,collections,project,exploration]=await Promise.all(['engine','regional-exploration','regional-methods','business','orders','regulars','collection-progress','projects','exploration'].map(load));
    let s=e.normalizeSave(structuredClone(original),NOW);assert.deepEqual(s,original,'policy never changes migration/validation');
    const before=structuredClone(s);paused(()=>trip.departRegional(s,{regionId:'R'},NOW));paused(()=>methods.prepareRegionalRecipe(s,'REC-V-C1'));paused(()=>methods.identifyMaterial(s,75));paused(()=>business.openBusiness(s,{menuId:'MN1',stock:{'0:8':6}},NOW));paused(()=>orders.acceptProposal(s,'irrelevant',{},NOW));paused(()=>project.completeProjectStage(s,'PJ-1','PJ-1-A'));
    assert.deepEqual(orders.orderMilestone(s,NOW,'test'),[]);assert.deepEqual(regulars.reconcileRegulars(s),[]);assert.deepEqual(collections.reconcileEntitlements(s),[]);assert.deepEqual(s,before,'refused entries never mutate stock, CP or cursors');
    const pendingDraft=base();pendingDraft.expansion.prepareMode={kind:'regional',recipeId:'REC-V-C1'};pendingDraft.selected=[75];const draftBefore=structuredClone(pendingDraft);paused(()=>e.startBatch(pendingDraft,1,NOW));assert.deepEqual(pendingDraft,draftBefore,'saved preparation cannot bypass the new-batch gate');
    const earlyClose=structuredClone(s),closeCP=earlyClose.cp;business.closeBusiness(earlyClose,NOW);assert.equal(earlyClose.expansion.business.active,null);assert.equal(earlyClose.cp,closeCP);e.normalizeSave(earlyClose,NOW);
    regulars.readRegularStage(s,'RG1');assert.equal(s.expansion.regulars.RG1.pendingStage,null,'existing note is readable but next story is not queued');
    const baseline=structuredClone(s.expansion.regulars.RG1);regulars.reconcileRegulars(s);assert.deepEqual(s.expansion.regulars.RG1,baseline);
    const ready=e.batchReadyAt(s.batch);for(const dt of [0,2100,3100]){e.resume(s,ready+dt);e.updateBatch(s,ready+dt);}e.updateBatch(s,ready+3200);for(let i=0;i<24;i++)e.collect(s,i,ready+3200);
    assert.equal(s.batch.eggs.filter(x=>x.collected).length,24,'saved regional batch remains harvestable');
    e.advanceWorld(s,NOW+24*H);assert.equal(s.progress.trip.status,'returned');exploration.claimTrip(s,s.progress.trip.id,{discard:true},NOW+24*H);assert.equal(s.progress.trip.status,'settled');assert.equal(s.expansion.business.active,null);assert.equal(s.expansion.business.lastReport.totalSold,12);
    const active=s.expansion.orders.active.find(o=>o.id===order.id),allocations=active.groups.map(g=>({groupId:g.id,key:g.allowed.find(k=>(s.farm[k]??0)>=g.quantity+1),quantity:g.quantity}));
    const delivered=orders.deliverOrderGroups(s,order.id,allocations,NOW+24*H);assert.equal(delivered.complete,true);assert.equal(s.expansion.orders.active.length,0);
    assert.deepEqual(s.expansion.collections.entitlements,entitlements,'paused automatic grants preserve existing entitlements');assert.deepEqual(s.events,events,'old rewards are unchanged');
    s=e.normalizeSave(s,NOW+24*H);assert.deepEqual(E.normalizeSave(structuredClone(s),NOW+24*H),s,'default build reads settled hotfix save');
    const cp=s.cp,farm=structuredClone(s.farm);e.advanceWorld(s,NOW+24*H+1);assert.equal(s.cp,cp);assert.deepEqual(s.farm,farm,'settlement is not repeated');
    s.egg=0;s.selected=[];delete s.expansion.prepareMode;e.startBatch(s,0,NOW+24*H+1);assert.ok(s.batch,'old ordinary cooking remains open');e.normalizeSave(s,NOW+24*H+1);
  });
});

test('paused projects finish an already contributed stage but never start the next or refund earlier costs',async()=>{
  const s=base();for(const id of ['R-S1','R-S2'])s.expansion.discovery.cards[id]=++s.meta.factSeq;for(const id of [77,78])s.expansion.discovery.identified[id]=++s.meta.factSeq;for(const key of ['0:134','0:135','0:136','1:76'])s.total[key]=1;
  completeProjectStage(s,'PJ-2','PJ-2-A');deliverProject(s,'PJ-2','PJ-2-B',{'0:3':4},{choice:['0:3','0:4']});
  const cp=s.cp;await isolatedPolicy(allOff,async load=>{
    const project=await load('projects'),e=await load('engine');
    project.deliverProject(s,'PJ-2','PJ-2-B',{'0:3':2,'0:4':6});project.completeProjectStage(s,'PJ-2','PJ-2-B');
    assert.equal(s.expansion.projects['PJ-2'].stages['PJ-2-B'].complete,true);assert.equal(s.cp,cp);paused(()=>project.completeProjectStage(s,'PJ-2','PJ-2-C'));assert.equal(s.cp,cp);e.normalizeSave(s,NOW);
  });
});

test('per-region rollback stops only that region and preserves identity, capacity and materials',async()=>{
  await isolatedPolicy({...ROLLBACK_POLICY,regions:{...ROLLBACK_POLICY.regions,V:false}},async load=>{
    const trip=await load('regional-exploration'),e=await load('engine'),capacity=await load('material-capacity'),registry=await load('content-registry');const s=base();
    const before=structuredClone(s);paused(()=>trip.departRegional(s,{regionId:'V',members:['0:0']},NOW));assert.deepEqual(s,before);
    assert.equal(trip.regionalTripInfo(s,{regionId:'R',placeId:'R:0',focus:'materials',members:['1:0']},NOW).canDepart,true);
    assert.equal(Object.keys(registry.speciesByKey).length,241);assert.equal(Object.keys(registry.materialById).length,83);assert.equal(capacity.materialCapacity(s),36);assert.equal(s.ingredients[75],1);e.normalizeSave(s,NOW);
  });
});

test('a paused region retains an already learned local alternative and its original legacy outcome',async()=>{
  const s=base();s.expansion.discovery.cards['V-E2']=++s.meta.factSeq;s.expansion.discovery.identified[76]=++s.meta.factSeq;
  s.expansion.methods.directions.push('ALT-V');s.expansion.methods.full.push('ALT-V');s.ingredients={76:1};
  await isolatedPolicy(allOff,async load=>{const methods=await load('regional-methods'),e=await load('engine');methods.prepareLocalAlternative(s,'ALT-V');e.startBatch(s,4,NOW);assert.equal(s.batch.plan.mode,'local-alternative');assert.ok(s.batch.plan.initialIds.every(id=>id<128));e.normalizeSave(s,NOW);});
});
