import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {syncProgress} from '../web/progression.js';
import {orderMilestone,acceptProposal,reserveForOrder} from '../web/orders.js';
import {farmLossAt} from '../web/farm-clock.js';
import {execute} from '../web/game-commands.js';
test('complete home stock loss removes zero Q entries so the transaction and reload remain valid',()=>{
  const now=1800000000000,s=E.freshState(now,42);s.total={'0:0':300,'0:3':6,'0:8':6,'0:1':1,'0:2':1,'0:10':1,'0:17':1,'0:20':1};s.farm={'0:3':6};s.toolLevels[1]=0;s.toolLevels[2]=0;syncProgress(s);
  orderMilestone(s,now,'batch');const o=acceptProposal(s,s.expansion.orders.proposals.find(p=>p.templateId==='O01').id,{},now);reserveForOrder(s,o.id,'0:3',6);
  s.farmFixed=now-12*86400000;s.farmChecked=now-5*86400000;
  const cp=s.cp,result=execute({state:s,command:{type:'farm-return'},now,reduce:d=>farmLossAt(d,now,()=>.99)}),r=result.state.expansion.orders.active[0];
  assert.equal(result.state.farm['0:3'],0);assert.deepEqual(r.reserved,{});assert.equal(r.needsRestock,true);assert.equal(result.state.cp,cp);assert.equal(r.paidCP,0);assert.doesNotThrow(()=>E.normalizeSave(result.state,now));
});
