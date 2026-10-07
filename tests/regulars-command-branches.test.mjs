// Authored 16×2 branch acceptance. Fixture supplies prerequisite discoveries and
// prior reads only; every tested sale, delivery, card or batch is a real command.
import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {REGIONAL,resolveSpecies} from '../web/content-registry.js';
import {GAME_DATA} from '../web/content-pack.js';
import {syncProgress} from '../web/progression.js';
import {execute} from '../web/game-commands.js';
import {openBusiness,advanceBusiness} from '../web/business.js';
import {orderMilestone,skipProposal,acceptProposal,deliverOrderGroups,displayOrder,orderOptions} from '../web/orders.js';
import {departRegional} from '../web/regional-exploration.js';
import {claimTrip} from '../web/exploration.js';
import {prepareRegionalRecipe} from '../web/regional-methods.js';
import {regularInfo,readRegularStage,STAGE_RULES} from '../web/regulars.js';

const NOW=1800000000000;
function fixture(stageId){
 const s=E.freshState(NOW,37);s.kitchenLevel=3;s.duck=true;s.toolLevels.fill(2);s.cp=90000;
 s.total=Object.fromEntries(GAME_DATA.characters.flatMap((cs,egg)=>cs.map(c=>[`${egg}:${c.id}`,egg===0&&c.id===0?10000:2])));
 s.farm=Object.fromEntries(Object.keys(s.total).map(k=>[k,80]));s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};
 s.expansion.regions.guideFlags=['GUIDE-B'];s.expansion.regions.introSpecimenDone=['V','R','T','B'];
 for(const m of REGIONAL.materials)s.expansion.discovery.identified[m.id]=++s.meta.factSeq;
 for(const c of REGIONAL.cards.filter(c=>c.type==='specimen'))s.expansion.discovery.cards[c.id]=++s.meta.factSeq;
 s.expansion.methods.directions=REGIONAL.recipes.map(r=>r.id);s.expansion.methods.full=REGIONAL.recipes.map(r=>r.id);
 const r=REGIONAL.regulars.find(r=>r.stages.some(st=>st.id===stageId)),i=r.stages.findIndex(st=>st.id===stageId);
 if(i){const read=r.stages.slice(0,i);for(const st of read)s.expansion.collections.entitlements[st.reward]={seq:++s.meta.factSeq,source:st.id};s.expansion.regulars[r.id]={readStages:read.map(st=>st.id),pendingStage:null,activatedSeq:s.meta.factSeq,baselines:stageId==='RG2-4'?{O05:0}:{},lastVisit:null};}
 if(stageId==='RG2-2')delete s.expansion.discovery.cards['R-S2'];
 syncProgress(s);return {s:E.normalizeSave(s,NOW),now:NOW,stageId,regularId:r.id};
}
function cmd(c,type,reduce,at=c.now){c.now=at;const out=execute({state:c.s,now:at,command:{type},reduce});c.s=out.state;return out.result;}
function service(c,id,stock){const def=REGIONAL.menus.find(m=>m.id===id);stock??=Object.fromEntries(def.examples[0].map(x=>[x.key,6]));cmd(c,'business:open',s=>openBusiness(s,{menuId:id,stock,pinnedRegular:c.regularId},c.now));const start=c.s.expansion.business.active.startAt;cmd(c,'business:advance',s=>advanceBusiness(s,start+24*3600000),start+24*3600000);}
function order(c,id,{region=null,groups=null,groupOnly=false,variantId=null}={}){
 for(let i=0;i<30&&!c.s.expansion.orders.proposals.some(p=>p.templateId===id);i++)cmd(c,'order:milestone',s=>{if(s.expansion.orders.proposals.length>=2)skipProposal(s,s.expansion.orders.proposals[0].id);return orderMilestone(s,c.now,'batch');});
 const proposal=c.s.expansion.orders.proposals.find(p=>p.templateId===id);assert.ok(proposal,`${id} has an executable proposal`);
 const option=orderOptions(c.s,id,c.now).find(x=>(!region||x.region===region)&&(!variantId||x.variantId===variantId));
 const instance=cmd(c,'order:accept',s=>acceptProposal(s,proposal.id,option,c.now));
 if(id==='O04'){const keys=instance.groups[0].allowed.filter(k=>c.s.farm[k]>0).slice(0,3);return cmd(c,'order:display',s=>displayOrder(s,instance.id,keys));}
 const allocations=[];const used=new Set();for(const [i,g]of instance.groups.entries()){if(groupOnly&&i>0)continue;const picks=groups?.[i];if(picks){for(const [key,quantity]of Object.entries(picks))allocations.push({groupId:g.id,key,quantity});}
 else{const keys=g.allowed.filter(k=>!used.has(k)&&c.s.farm[k]>g.quantity).slice(0,instance.groups.length===1?instance.minimumDistinct:1);assert.ok(keys.length);keys.forEach(k=>used.add(k));for(const [j,key]of keys.entries())allocations.push({groupId:g.id,key,quantity:j?1:g.quantity-keys.length+1});}}
 return cmd(c,'order:deliver',s=>deliverOrderGroups(s,instance.id,allocations,c.now));
}
function trip(c,cardId,{cargo=null}={}){const card=REGIONAL.cards.find(x=>x.id===cardId);
 // Real, bounded repeated trips use frozen RNG and fourth-trip protection;
 // competing discoveries are retained and never rewritten into the target.
 for(let attempt=0;attempt<12&&!c.s.expansion.discovery.cards[cardId];attempt++){
  cmd(c,'trip:depart',s=>departRegional(s,{regionId:card.region,placeId:card.placeId,focus:card.focus,members:card.team.oldExamples,cargo},c.now));const end=c.s.progress.trip.endAt;
  cmd(c,'trip:advance',s=>E.advanceWorld(s,end),end);cmd(c,'trip:claim',s=>claimTrip(s,s.progress.trip.id,{discard:true},end));
 }
 assert.ok(c.s.expansion.discovery.cards[cardId],`${cardId} discovered by a real trip`);
}
function batch(c,{regional=true}={}){
 const recipe=REGIONAL.recipes.find(r=>r.id==='REC-T-C1');
 cmd(c,'batch:prepare',s=>{s.ingredients={79:1};if(regional)prepareRegionalRecipe(s,recipe.id);else{s.selected=[79];delete s.expansion.prepareMode;}});
 cmd(c,'batch:start',s=>E.startBatch(s,regional?recipe.toolId:0,c.now));const ends=c.s.batch.ends;
 for(const offset of [1,2001,3001])cmd(c,'batch:advance',s=>E.updateBatch(s,ends+offset),ends+offset);
 for(let i=0;i<24;i++)cmd(c,'batch:collect',s=>E.collect(s,i,c.now));
 assert.equal(c.s.batch.eggs.every(e=>e.collected),true);return c.s.batch.plan.mode;
}
const branches={
 'RG1-1':[c=>service(c,'MN1'),c=>order(c,'O01')],
 'RG1-2':[c=>service(c,'MN2'),c=>order(c,'O07')],
 'RG1-3':[c=>service(c,'MN3'),c=>order(c,'O03')],
 'RG1-4':[c=>{service(c,'MN1');service(c,'MN2');},c=>{order(c,'O01');order(c,'O12');}],
 'RG2-1':[c=>service(c,'MN4'),c=>order(c,'O05')],
 'RG2-2':[c=>trip(c,'R-E1'),c=>trip(c,'R-S2')],
 'RG2-3':[c=>service(c,'MN4',{'0:43':6,'1:5':6,'0:135':6}),c=>order(c,'O06',{region:'R'})],
 'RG2-4':[c=>{trip(c,'R-E2');service(c,'MN4');},c=>order(c,'O05')],
 'RG3-1':[c=>service(c,'MN1',{'0:10':6,'1:39':6}),c=>order(c,'O07',{groups:[{'0:10':3,'1:39':3}],groupOnly:true})],
 'RG3-2':[c=>trip(c,'T-E1'),c=>{trip(c,'T-N1');batch(c);}],
 'RG3-3':[c=>service(c,'MN5'),c=>order(c,'O08')],
 'RG3-4':[c=>service(c,'MN1',{'0:10':6,'1:39':6,'0:114':6}),c=>{order(c,'O07',{groups:[{'0:10':3,'1:39':3},{'0:114':6}]});order(c,'O07',{groups:[{'0:10':3,'1:39':3},{'0:114':6}]});}],
 'RG4-1':[c=>service(c,'MN1'),c=>order(c,'O12')],
 'RG4-2':[c=>trip(c,'B-N1'),c=>trip(c,'B-E1')],
 'RG4-3':[c=>service(c,'MN7'),c=>order(c,'O11')],
 'RG4-4':[c=>trip(c,'B-E1',{cargo:{'0:0':6}}),c=>order(c,'O06',{region:'B'})],
};
for(const [stageId,paths]of Object.entries(branches))for(const [branch,path]of paths.entries())test(`${stageId} alternative ${branch+1}: real command producer, save, read and no repeated reward`,()=>{
 const c=fixture(stageId);assert.equal(regularInfo(c.s,c.regularId).pending,null);path(c);
 const r=c.s.expansion.regulars[c.regularId];
 // 不挡进度: when the next stage is already met as well, this one is recorded as read at once and the next one waits
 const recorded=r?.readStages.includes(stageId);assert.ok(recorded||r?.pendingStage?.id===stageId,stageId);if(!recorded)assert.equal(r.pendingStage.branch,branch);
 const reward=REGIONAL.regulars.find(r=>r.id===c.regularId).stages.find(s=>s.id===stageId).reward,entitlement=structuredClone(c.s.expansion.collections.entitlements[reward]);assert.ok(entitlement);
 const cp=c.s.cp,farm=structuredClone(c.s.farm);c.s=E.normalizeSave(c.s,c.now);if(!recorded){const read=cmd(c,'regular:read',s=>readRegularStage(s,c.regularId));assert.equal(read.stageId,stageId);}assert.equal(c.s.cp,cp);assert.deepEqual(c.s.farm,farm);
 cmd(c,'idle',()=>{});assert.deepEqual(c.s.expansion.collections.entitlements[reward],entitlement);
});
test('first business story waits for twelve actual sales across sessions, and cannot be read at six',()=>{
 const c=fixture('RG1-1');service(c,'MN1',{'0:0':6});assert.equal(c.s.expansion.business.visitorSequence,0);assert.equal(regularInfo(c.s,'RG1').pending,null);assert.throws(()=>cmd(c,'regular:read',s=>readRegularStage(s,'RG1')),/暂时没有/);service(c,'MN1',{'0:0':6});assert.equal(c.s.expansion.business.visitorSequence,1);assert.equal(regularInfo(c.s,'RG1').pending.id,'RG1-1');
});
test('ordinary material use does not replace RG3-2 regional trial; the fully collected regional batch does',()=>{
 const c=fixture('RG3-2');trip(c,'T-N1');assert.equal(batch(c,{regional:false}),'legacy');assert.equal(c.s.expansion.regions.materialUse[79],true);assert.equal(STAGE_RULES['RG3-2'].alts[1](c.s),false);assert.equal(regularInfo(c.s,'RG3').pending,null);batch(c);assert.equal(regularInfo(c.s,'RG3').pending.id,'RG3-2');
});
for(const [label,stageId,branch,path]of [
 ['second menu','RG1-3',0,c=>service(c,'MN6')],['second order','RG1-3',1,c=>order(c,'O09')],
 ['second river event','RG2-2',0,c=>trip(c,'R-E2')],['free lore instead of specimen','RG2-2',1,c=>trip(c,'R-N1')],
 ['MN2 river dish','RG2-3',0,c=>service(c,'MN2',{'1:71':6,'0:19':6,'0:121':6})],
 ['flower display','RG2-4',1,c=>order(c,'O04',{variantId:'O04-A'})],['silhouette display','RG2-4',1,c=>order(c,'O04',{variantId:'O04-B'})],
])test(`${stageId} inner OR: ${label} through actual commands`,()=>{const c=fixture(stageId);path(c);assert.equal(regularInfo(c.s,c.regularId).pending.id,stageId);assert.equal(regularInfo(c.s,c.regularId).pending.branch,branch);});
