import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {REGIONAL} from '../web/content-registry.js';
import {syncProgress} from '../web/progression.js';
import {reduceFacts} from '../web/facts.js';
import {reconcileRegulars,readRegularStage} from '../web/regulars.js';
import {openBusiness,BUSINESS_WINDOW_MS} from '../web/business.js';
import {execute} from '../web/game-commands.js';
const NOW=1800000000000;
function readingDuringBusiness(){
 const s=E.freshState(NOW,63);s.kitchenLevel=3;s.duck=true;s.toolLevels.fill(2);s.cp=5000;
 const keys=REGIONAL.menus.find(m=>m.id==='MN2').examples[0].map(x=>x.key);
 s.total=Object.fromEntries(['0:0','0:3','0:8','0:12','0:16','0:21','1:0','1:3',...keys].map(k=>[k,10]));s.total['0:0']=1000;
 s.farm=Object.fromEntries(Object.keys(s.total).map(k=>[k,20]));
 s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};syncProgress(s);
 reduceFacts(s,[{kind:'orderComplete',instanceId:'order-1',templateId:'O01',variantId:'O01-A',region:null,chapters:null,groupDeliveries:[]}]);
 reconcileRegulars(s);assert.equal(s.expansion.regulars.RG1.pendingStage.id,'RG1-1');
 openBusiness(s,{menuId:'MN2',stock:Object.fromEntries(keys.map(k=>[k,6])),pinnedRegular:'RG1'},NOW);
 return E.normalizeSave(s,NOW);
}
test('acknowledging the displayed story never reads the next story queued by the same clock advance',()=>{
 const s=readingDuringBusiness();
 const out=execute({state:s,now:NOW+2*BUSINESS_WINDOW_MS+1,command:{type:'regular:read'},advance:E.advanceWorld,reduce:d=>readRegularStage(d,'RG1','RG1-1')});
 assert.equal(out.result.stageId,'RG1-1','only acknowledge the story actually displayed');
 assert.deepEqual(out.state.expansion.regulars.RG1.readStages,['RG1-1']);
 assert.equal(out.state.expansion.regulars.RG1.pendingStage.id,'RG1-2','newly queued story stays unread');
});
test('a stale or foreign story identity is rejected without changing the relationship',()=>{
 const s=readingDuringBusiness(),before=structuredClone(s);
 assert.throws(()=>readRegularStage(s,'RG1','RG1-2'),/进度已变化/);
 assert.throws(()=>readRegularStage(s,'RG1','RG2-1'),/没有这段/);
 assert.deepEqual(s,before);
});
test('repeated acknowledgement of the same displayed story leaves the next story and rewards unchanged',()=>{
 const s=readingDuringBusiness();E.advanceWorld(s,NOW+2*BUSINESS_WINDOW_MS+1);
 assert.equal(s.expansion.regulars.RG1.pendingStage.id,'RG1-2');const before=structuredClone(s);
 for(let i=0;i<3;i++)assert.equal(readRegularStage(s,'RG1','RG1-1').alreadyRead,true);
 assert.deepEqual(s,before);
});
