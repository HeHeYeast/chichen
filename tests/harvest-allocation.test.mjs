import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {syncProgress} from '../web/progression.js';
import {orderMilestone,acceptProposal} from '../web/orders.js';
import {execute} from '../web/game-commands.js';
import {inventoryView} from '../web/inventory.js';
import {harvestStock,planHarvestAllocation,allocateHarvest} from '../web/harvest-allocation.js';
const NOW=1800000000000;
function fixture(){const s=E.freshState(NOW,42);s.total={'0:0':300,'0:3':2,'0:8':2,'0:1':2,'0:2':2,'0:10':2,'0:17':2,'0:20':2};s.farm={'0:0':1};s.toolLevels[1]=0;s.toolLevels[2]=0;syncProgress(s);E.startBatch(s,0,NOW);const at=s.batch.ends+3100;E.updateBatch(s,at-3000);E.updateBatch(s,at);for(let i=0;i<24;i++)E.collect(s,i,at);orderMilestone(s,at,'batch');const p=s.expansion.orders.proposals.find(p=>p.templateId==='O01');acceptProposal(s,p.id,{},at);return {s:E.normalizeSave(s,at),at};}
test('one harvest allocation sells, reserves and retains a non-owning business draft atomically',()=>{
  const {s,at}=fixture(),key=Object.keys(harvestStock(s)).find(k=>harvestStock(s)[k]>=18),orderId=s.expansion.orders.active[0].id;
  assert.ok(key);const before=inventoryView(s,key),cp=s.cp,options={orderId,rows:{[key]:{sale:6,business:6,order:6}}},p=planHarvestAllocation(s,options);
  const result=execute({state:s,now:at,command:{type:'allocate',options},reduce:d=>allocateHarvest(d,options,at)});
  const after=inventoryView(result.state,key);assert.equal(after.T,before.T-6);assert.equal(after.Q,6);assert.equal(after.S,0);assert.equal(result.state.cp,cp+p.quote.income);assert.equal(result.state.expansion.facts.orderCounts[key]??0,0);assert.equal(result.state.expansion.business.active,null);assert.deepEqual(result.result.business,{[key]:6});E.normalizeSave(result.state,at);
});
test('combined destinations cannot overlap free stock or consume the last home bird',()=>{const {s}=fixture(),key=Object.keys(harvestStock(s))[0],n=harvestStock(s)[key];s.farm[key]=n;assert.throws(()=>planHarvestAllocation(s,{rows:{[key]:{sale:n-1,business:1}}}),/留种/);assert.throws(()=>planHarvestAllocation(s,{keepOne:false,rows:{[key]:{sale:n,order:1}}}),/超过/);assert.equal(planHarvestAllocation(s,{keepOne:false,rows:{[key]:{sale:n}}}).sale[key],n);});
test('invalid reservation or failed persistence rolls back sale, CP and Q together',()=>{const {s,at}=fixture(),key=Object.keys(harvestStock(s))[0],before=structuredClone(s);assert.throws(()=>execute({state:s,now:at,command:{type:'allocate'},reduce:d=>allocateHarvest(d,{orderId:'missing',rows:{[key]:{sale:2,order:1}}},at)}));assert.deepEqual(s,before);assert.throws(()=>execute({state:s,now:at,store:{write(){throw Error('disk');}},command:{type:'allocate'},reduce:d=>allocateHarvest(d,{rows:{[key]:{sale:2}}},at)}));assert.deepEqual(s,before);});
test('collection lock and non-integer amounts are rejected; default plan leaves everything home',()=>{const {s}=fixture(),key=Object.keys(harvestStock(s))[0];assert.deepEqual(planHarvestAllocation(s).sale,{});assert.throws(()=>planHarvestAllocation(s,{rows:{[key]:{sale:.5}}}),/整只/);s.expansion.inventoryPolicy.collectionLocks=[key];assert.throws(()=>planHarvestAllocation(s,{rows:{[key]:{sale:1}}}),/保护/);});
