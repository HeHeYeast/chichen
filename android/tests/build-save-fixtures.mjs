import {contractMatrix} from './save-contract-matrix.mjs';
import {negativeCases} from './save-negative-cases.mjs';
// Generate native interoperability inputs through the real Web command path.
import {readFileSync,writeFileSync} from 'node:fs';
import {readRegularStage} from '../../web/regulars.js';
import {completeProjectStage,deliverProject,saveMenuPreset} from '../../web/projects.js';
import {reduceFacts} from '../../web/facts.js';
import {farmLossAt} from '../../web/farm-clock.js';
import {freshState,normalizeSave,buyIngredient,startBatch,advanceWorld} from '../../web/engine.js';
import {execute} from '../../web/game-commands.js';
import {migrate3to4,migrate4to5} from '../../web/save-migrations.js';
import {earnedSources,syncProgress,learnSkill} from '../../web/progression.js';
import {openBusiness} from '../../web/business.js';
import {orderMilestone,acceptProposal,reserveForOrder,deliverOrderGroups} from '../../web/orders.js';
import {departRegional,regionalTripInfo} from '../../web/regional-exploration.js';
import {identifyMaterial,prepareRegionalRecipe,prepareLocalAlternative,pinRegionalMethod,regionalRecipeInfo} from '../../web/regional-methods.js';
import {claimTrip} from '../../web/exploration.js';

const legacy=JSON.parse(readFileSync(new URL('../../tests/fixtures/save-contract.json',import.meta.url),'utf8'));
const now=legacy.now;
const version4=migrate3to4(legacy.cases[0].normalized),version5=migrate4to5(version4);
const cases=[{name:'empty-install',source:null,state:freshState(now,0x12345678)},
  {name:'upgrade-v3',source:legacy.cases[0].normalized,state:normalizeSave(legacy.cases[0].normalized,now)},
  ...[version4,version5].map(source=>({name:`upgrade-v${source.version}`,source,state:normalizeSave(source,now)}))].map(({name,source,state})=>{
  const writes=[];
  const store={write(candidate,{expectedRevision}){writes.push({expectedRevision,state:structuredClone(candidate)});}};
  const first=execute({state,store,now,command:{type:'buyIngredient',id:0,count:1},reduce:draft=>buyIngredient(draft,0,1)}).state;
  execute({state:first,store,now:now+1,command:{type:'buyIngredient',id:0,count:1},reduce:draft=>buyIngredient(draft,0,1)});
  return {name,source,writes};
});
const regional=[];
let state=freshState(now,123456),at=now;
state.total={'0:0':116,'0:3':1,'0:4':1,'0:8':1,'0:18':1};
state.farm={'0:0':3,'0:3':2,'0:4':2,'0:8':1,'0:18':1};state.toolLevels[1]=0;state.ingredients={0:30};state.progress.sources=earnedSources(state);
state=normalizeSave(state,now);
function run(name,reduce){state=execute({state,now:at,command:{type:name},advance:advanceWorld,reduce}).state;regional.push({name,state:structuredClone(state)});}
const options={regionId:'V',placeId:'V:1',focus:'materials',members:['0:0']};
run('first-regional-departure',s=>departRegional(s,options,at));
at=state.progress.trip.endAt;run('first-specimen-full-bag',()=>{});
run('identify-expands-to36',s=>identifyMaterial(s,75));
run('claim-regional-basket',s=>claimTrip(s,s.progress.trip.id,{},at));
for(let i=0;i<3;i++){
  run(`free-method-trip-${i+1}`,s=>departRegional(s,options,at));
  at=state.progress.trip.endAt;run(`free-method-return-${i+1}`,s=>claimTrip(s,s.progress.trip.id,{},at));
}
run('prepare-C129',s=>prepareRegionalRecipe(s,'REC-V-C1'));
run('batch3-C129',s=>startBatch(s,1,at));
// Work C: a legitimate mid-game legacy save walks the whole valley chain with
// real commands (malt specimen, sampling slot, ALT-V oven batch, V-C3 steamer).
const valley=[];
let v=freshState(now,20260923),vat=now;
v.kitchenLevel=1;v.duck=true;v.toolLevels=[0,1,1,-1,0,-1,-1,-1,0];v.cp=20000;
v.total=Object.fromEntries(['0:0','0:3','0:4','0:8','0:17','0:18','0:114','0:115','0:116','1:0','1:3','1:6'].map((k,i)=>[k,i?20:200]));
v.farm=Object.fromEntries(Object.keys(v.total).map(k=>[k,6]));for(let i=20;i<32;i++)v.total['0:'+i]=1;v.ingredients={};v.progress.sources=earnedSources(v);
v=normalizeSave(v,now);
function step(name,reduce,keep=false){v=execute({state:v,now:vat,command:{type:name},advance:advanceWorld,reduce}).state;if(keep)valley.push({name,state:structuredClone(v)});}
function voyage(name,options,keep=false){step(name+'-depart',s=>departRegional(s,{regionId:'V',members:['0:0'],...options},vat),keep);vat=v.progress.trip.endAt;step(name+'-return',s=>claimTrip(s,s.progress.trip.id,{discard:true},vat),keep);}
voyage('intro',{placeId:'V:0',focus:'specimen'});step('identify-75',s=>identifyMaterial(s,75));
voyage('sampling',{placeId:'V:0',focus:'materials',sampling:true},true);
for(let i=0;i<4&&!v.expansion.discovery.cards['V-S2'];i++)voyage('malt',{placeId:'V:1',focus:'specimen'});
step('identify-76',s=>identifyMaterial(s,76),true);
for(let i=0;i<8&&!v.expansion.discovery.cards['V-E2'];i++)voyage('grain-event',{placeId:'V:1',focus:'lore',members:['0:18']});
step('buy-malt',s=>{buyIngredient(s,76,2);buyIngredient(s,75,1);buyIngredient(s,9,1);});
step('prepare-ALT-V',s=>prepareLocalAlternative(s,'ALT-V'),true);
step('batch3-ALT-V',s=>startBatch(s,4,vat),true);
vat=v.batch.ends+60000;
step('pin-C3',s=>{if(!s.expansion.methods.full.includes('REC-V-C3'))pinRegionalMethod(s,'REC-V-C3');});
for(let i=0;i<3&&!v.expansion.methods.full.includes('REC-V-C3');i++)voyage('method-C3',{placeId:'V:0',focus:'materials'});
step('abandon-and-prepare-C3',s=>prepareRegionalRecipe(s,'REC-V-C3'));
step('abandon-ALT-batch-start-steamer-C3',s=>{s.batch=null;startBatch(s,8,vat);},true);
// Work F: the same chain on the river route (6h water), then free identification.
vat=v.batch.ends+60000;step('abandon-C3-for-river',s=>{s.batch=null;});
step('river-depart',s=>departRegional(s,{regionId:'R',placeId:'R:1',focus:'materials',members:['0:0']},vat),true);
vat=v.progress.trip.endAt;step('river-return',s=>claimTrip(s,s.progress.trip.id,{discard:true},vat),true);
const riverMaterial=v.progress.trip.regional.intro?.materialId;if(!riverMaterial)throw Error('river fixture must earn its entry specimen');
step('identify-river',s=>identifyMaterial(s,riverMaterial),true);
// Work D: old-stock business through real commands (open, two windows, sold out).
const business=[];
let b=freshState(now,777);b.farm={'0:0':25,'0:3':25};b.total=Object.fromEntries(Array.from({length:12},(_,id)=>[`0:${id}`,id===0?2000:1]));
b.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};syncProgress(b);learnSkill(b,'TRADE-2');learnSkill(b,'TRADE-3');b.progress.trade.category='家常';b=normalizeSave(b,now);
let bat=now;const bstep=(name,reduce)=>{b=execute({state:b,now:bat,command:{type:name},advance:advanceWorld,reduce}).state;business.push({name,state:structuredClone(b)});};
bstep('open-24-with-credit',s=>openBusiness(s,{stock:{'0:0':24},useRewards:true},bat));
bat+=4*3600000+1;bstep('two-windows-sold-12',()=>{});
bat+=20*3600000;bstep('sold-out-report',()=>{});
// Work E: an O01 instance with a reservation and one delivered batch, via real commands.
const ordersFx=[];
let q=freshState(now,31337);q.total=Object.fromEntries(['0:0','0:3','0:8','0:1','0:2','0:10','0:17','0:20','0:5'].map((k,i)=>[k,i?5:300]));
q.farm={'0:0':40,'0:3':10,'0:8':10,'0:10':2,'0:17':2,'0:20':2};q.toolLevels[1]=0;q.toolLevels[2]=0;syncProgress(q);q=normalizeSave(q,now);
const qstep=(name,reduce)=>{q=execute({state:q,now,command:{type:name},advance:advanceWorld,reduce}).state;ordersFx.push({name,state:structuredClone(q)});};
qstep('milestone',s=>orderMilestone(s,now,'batch'));
qstep('accept-O01',s=>acceptProposal(s,s.expansion.orders.proposals[0].id,{},now));
qstep('reserve-six',s=>reserveForOrder(s,s.expansion.orders.active[0].id,'0:3',6));
qstep('deliver-first-six',s=>deliverOrderGroups(s,s.expansion.orders.active[0].id,[{groupId:'O01-G1',key:'0:0',quantity:6}],now));
// Work G: guide from the river stall, then a bay trip carrying six birds.
const bay=[];
let g=freshState(now,2718);g.kitchenLevel=2;g.duck=true;g.cp=90000;g.toolLevels=[0,1,1,1,1,1,1,-1,1];
g.total=Object.fromEntries(['0:0','0:3','0:4','0:6','0:8','0:10','0:12','0:16','0:17','0:18','0:21','0:43','0:114','0:115','0:116','0:117','1:0','1:3','1:5','1:6','1:7','1:8','1:9','1:10','1:12','1:22'].map((k,i)=>[k,i?20:900]));
for(let i=22;i<40;i++)g.total['0:'+i]??=1;g.farm=Object.fromEntries(Object.keys(g.total).filter(k=>g.total[k]>=20).map(k=>[k,9]));g.progress.sources=earnedSources(g);g.ingredients={};g=normalizeSave(g,now);
let gat=now;const gstep=(name,reduce,keep)=>{g=execute({state:g,now:gat,command:{type:name},advance:advanceWorld,reduce}).state;if(keep)bay.push({name,state:structuredClone(g)});};
const gvoyage=(name,options,keep)=>{gstep(name+'-depart',s=>departRegional(s,{members:['0:0'],...options},gat),keep);gat=g.progress.trip.endAt;gstep(name+'-return',s=>claimTrip(s,s.progress.trip.id,{discard:true},gat),keep);};
gvoyage('valley-intro',{regionId:'V',placeId:'V:0',focus:'specimen'});gstep('identify-75',s=>{identifyMaterial(s,75);s.total['0:128']=1;s.farm['0:128']=1;});
gstep('guide-depart',s=>departRegional(s,{regionId:'R',placeId:'R:1',focus:'materials',members:['0:0'],guide:true},gat),true);gat=g.progress.trip.endAt;
gstep('guide-return',s=>claimTrip(s,s.progress.trip.id,{discard:true},gat),true);
gvoyage('bay-intro',{regionId:'B',placeId:'B:0',focus:'specimen'});gstep('identify-81',s=>identifyMaterial(s,81));
gstep('cargo-depart',s=>departRegional(s,{regionId:'B',placeId:'B:0',focus:'lore',members:['1:0'],cargo:{'0:3':3,'0:8':3}},gat),true);gat=g.progress.trip.endAt;
gstep('cargo-return',s=>claimTrip(s,s.progress.trip.id,{discard:true},gat),true);
// Work I: an MN1 session whose visitor brings RG1-1, then the player reads it.
const regularsFx=[];
let r=freshState(now,4242);r.farm={'0:0':25,'0:3':25};r.total=Object.fromEntries(Array.from({length:12},(_,id)=>[`0:${id}`,id===0?2000:1]));
r.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};syncProgress(r);r=normalizeSave(r,now);
let rat=now;const rstep=(name,reduce)=>{r=execute({state:r,now:rat,command:{type:name},advance:advanceWorld,reduce}).state;regularsFx.push({name,state:structuredClone(r)});};
rstep('mn1-open',s=>openBusiness(s,{menuId:'MN1',stock:{'0:0':6,'0:3':6}},rat));
rat+=4*3600000+1;rstep('mn1-visitor',()=>{});
rstep('read-RG1-1',s=>readRegularStage(s,'RG1'));
if(r.expansion.regulars.RG1?.readStages?.[0]!=='RG1-1')throw Error('regulars fixture must read RG1-1');
// Work J: PJ-1 three stages and a preset, then PJ-2 paid A and a locked partial delivery.
const projectsFx=[];
let j=freshState(now,5151);j.kitchenLevel=2;j.duck=true;j.cp=5000;j.toolLevels=[0,0,0,0,0,0,0,0,0];
j.total=Object.fromEntries(Array.from({length:40},(_,id)=>[`0:${id}`,id===0?3000:2]));j.total['0:134']=1;j.total['0:135']=1;j.total['0:136']=1;j.total['1:71']=1;j.total['1:0']=2;
j.farm={'0:0':30,'0:3':20,'0:4':20};j.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};syncProgress(j);j=normalizeSave(j,now);
reduceFacts(j,['MN1','MN3'].map((menuId,i)=>({kind:'businessWitness',sessionId:`business-${i+1}`,menuId,soldByKey:{'0:0':6},roleSales:{},fullSoldByKey:{},fullRoleSales:{},valid:true,complete:false})));
for(const id of ['R-S1','R-S2'])j.expansion.discovery.cards[id]=++j.meta.factSeq;for(const id of ['77','78'])j.expansion.discovery.identified[id]=++j.meta.factSeq;j=normalizeSave(j,now);
const jstep=(name,reduce)=>{j=execute({state:j,now,command:{type:name},advance:advanceWorld,reduce}).state;projectsFx.push({name,state:structuredClone(j)});};
jstep('pj1-a',s=>completeProjectStage(s,'PJ-1','PJ-1-A'));jstep('pj1-b',s=>completeProjectStage(s,'PJ-1','PJ-1-B'));jstep('pj1-c-pay-200',s=>completeProjectStage(s,'PJ-1','PJ-1-C'));
jstep('preset-0',s=>saveMenuPreset(s,0,{menuId:'MN1',stock:{'0:0':6,'0:3':6}}));
jstep('pj2-a-pay-100',s=>completeProjectStage(s,'PJ-2','PJ-2-A'));jstep('pj2-b-deliver-4',s=>deliverProject(s,'PJ-2','PJ-2-B',{'0:3':4},{choice:['0:3','0:4']}));
if(j.cp!==5000-300||j.farm['0:3']!==16)throw Error('projects fixture must pay 300 and deliver 4');
// A completely lost revocable Q reservation is deleted, not persisted as zero.
const lost=freshState(now,42);lost.total={'0:0':300,'0:3':6,'0:8':6,'0:1':1,'0:2':1,'0:10':1,'0:17':1,'0:20':1};lost.farm={'0:3':6};lost.toolLevels[1]=0;lost.toolLevels[2]=0;syncProgress(lost);
orderMilestone(lost,now,'batch');const lostOrder=acceptProposal(lost,lost.expansion.orders.proposals.find(p=>p.templateId==='O01').id,{},now);reserveForOrder(lost,lostOrder.id,'0:3',6);
lost.farmFixed=now-12*86400000;lost.farmChecked=now-5*86400000;
const afterLoss=execute({state:lost,now,command:{type:'farm-return-complete-Q-loss'},reduce:d=>farmLossAt(d,now,()=>.99)}).state;
if(afterLoss.farm['0:3']!==0||Object.keys(afterLoss.expansion.orders.active[0].reserved).length||!afterLoss.expansion.orders.active[0].needsRestock||afterLoss.cp!==lost.cp)throw Error('complete Q loss must release reservation without income');
ordersFx.push({name:'complete-Q-loss',state:afterLoss});
const fixtures={cases,regional,valley,business,orders:ordersFx,bay,regulars:regularsFx,projects:projectsFx};
fixtures.invalid=negativeCases(fixtures);
fixtures.matrix=contractMatrix(now);
const goldenExpected=JSON.parse(readFileSync(new URL('../../tests/fixtures/golden/expected.json',import.meta.url),'utf8'));
fixtures.golden=Object.keys(goldenExpected.cases).map(name=>{const source=JSON.parse(readFileSync(new URL(`../../tests/fixtures/golden/${name}.json`,import.meta.url),'utf8'));return {name,source,state:normalizeSave(source,goldenExpected.now)};});
// Historical schema-5 envelopes with live frozen tickets, constructed only as
// isolated interoperability fixtures (there is no player-save downgrade path).
fixtures.upgrades=[business[0],regional[0],regional.at(-1)].map(row=>{
  const source=structuredClone(row.state);source.version=5;source.meta.migrationHistory.pop();
  for(const field of ['collections','regulars','projects','menus'])delete source.expansion[field];
  const migrated=normalizeSave(source,now),state=execute({state:migrated,now:migrated.lastSeen,command:{type:'resume-after-upgrade'},reduce:()=>{}}).state;
  return {name:row.name,source,state,expectedRevision:source.meta.revision};
});
writeFileSync(process.argv[2],JSON.stringify(fixtures));
console.log(`Web validator: ${fixtures.invalid.length} shared malformed states rejected.`);
console.log(`Web execute: ${bay.length} bay states generated through real commands.`);
console.log(`Web execute: ${ordersFx.length} order states generated through real commands.`);
console.log(`Web execute: ${business.length} business states generated through real commands.`);
console.log(`Web execute: ${valley.length} valley states generated through real commands.`);
console.log(`Web execute: ${cases.length} native interoperability trajectories generated.`);
console.log(`Web execute: ${regional.length} regional states generated through real commands.`);
