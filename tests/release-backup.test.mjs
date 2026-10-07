// Work L: a fully populated schema-6 save (business, orders, regulars, projects,
// presets, portraits, collections, a regional trip) survives backup/restore and
// the update-backup checker byte-for-value.
import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {syncProgress} from '../web/progression.js';
import {LEGACY193} from '../web/legacy-content.js';
import {makeBackup,parseBackup} from '../web/save-store.js';
import {execute} from '../web/game-commands.js';
import {reduceFacts} from '../web/facts.js';
import {openBusiness} from '../web/business.js';
import {orderMilestone,acceptProposal} from '../web/orders.js';
import {completeProjectStage,saveMenuPreset,setProjectPortraits,stageComplete} from '../web/projects.js';
import {readRegularStage} from '../web/regulars.js';
import {setDisplay} from '../web/collection-progress.js';
import {departRegional} from '../web/regional-exploration.js';
import {checkBackup} from '../tools/check-update-backup.mjs';

const NOW=1800000000000;
function populated(){
  let s=E.freshState(NOW,6060);s.kitchenLevel=3;s.duck=true;s.cp=90000;s.toolLevels=s.toolLevels.map(()=>2);
  s.total=Object.fromEntries(LEGACY193.characters.flatMap((list,egg)=>list.map(c=>[`${egg}:${c.id}`,c.id===0&&egg===0?9000:3])));
  s.farm={'0:0':40,'0:3':20,'0:4':20,'0:8':20,'1:0':20,'0:1':5};s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};
  s.expansion.regions.guideFlags=['GUIDE-B'];syncProgress(s);s=E.normalizeSave(s,NOW);
  let n=0;const step=reduce=>{s=execute({state:s,now:NOW,command:{type:'step',n:++n},reduce}).state;};
  step(d=>reduceFacts(d,['MN1','MN3'].map((menuId,i)=>({kind:'businessWitness',sessionId:`business-${90+i}`,menuId,soldByKey:{'0:0':6},roleSales:{},fullSoldByKey:{},fullRoleSales:{},valid:true,complete:false}))));
  step(d=>{reduceFacts(d,[{kind:'orderComplete',instanceId:'order-900',templateId:'O01',variantId:'O01-A',region:null,chapters:null,groupDeliveries:[]}]);});
  step(d=>readRegularStage(d,'RG1'));
  // stages that cost nothing complete by themselves once met (batch 3); the paid one still needs 登记
  for(const id of ['PJ-1-A','PJ-1-B','PJ-1-C'])step(d=>{if(!stageComplete(d,'PJ-1',id))completeProjectStage(d,'PJ-1',id);});
  step(d=>saveMenuPreset(d,0,{menuId:'MN1',stock:{'0:0':6,'0:3':6}}));step(d=>setProjectPortraits(d,['0:0','0:1']));
  step(d=>setDisplay(d,0,'M01'));step(d=>openBusiness(d,{menuId:'MN1',stock:{'0:0':6,'0:3':6}},NOW));
  step(d=>{orderMilestone(d,NOW,'batch');acceptProposal(d,d.expansion.orders.proposals[0].id,{},NOW);});
  step(d=>departRegional(d,{regionId:'V',placeId:'V:0',focus:'specimen',members:['0:1']},NOW));
  return s;
}

test('a fully populated schema-6 save round-trips through backup and the update checker',()=>{
  const s=populated();
  assert.ok(s.expansion.regulars.RG1.readStages.length===1&&s.expansion.projects['PJ-1'].stages['PJ-1-C']&&s.expansion.menus.presets.length===1);
  assert.ok(s.expansion.business.active&&s.expansion.orders.active.length===1&&s.progress.trip&&s.expansion.collections.display[0]==='M01');
  const raw=makeBackup(s,NOW),back=parseBackup(raw,NOW+1000);
  assert.deepEqual(back,s,'backup restore is lossless');
  const summary=checkBackup(raw);assert.equal(summary.summary.cp,s.cp);
});
