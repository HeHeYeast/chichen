import test from 'node:test';
import assert from 'node:assert/strict';
import {freshState,normalizeSave} from '../web/engine.js';
import {LEGACY193} from '../web/legacy-content.js';
import {syncProgress} from '../web/progression.js';
import {orderMilestone,skipProposal,acceptProposal} from '../web/orders.js';
import {ordersModel} from '../web/order-model.js';
import {speciesDiscovered} from '../web/species-state.js';

test('proposal artwork is read-only, known, and belongs to the real accepted variant',()=>{
  const now=1800000000000,s=freshState(now,35);s.kitchenLevel=2;s.duck=true;s.toolLevels=Array(9).fill(0);
  s.total=Object.fromEntries(LEGACY193.characters.flatMap((rows,egg)=>rows.map(c=>[`${egg}:${c.id}`,c.id===0?300:1])));s.farm={'0:0':30,'1:0':30};syncProgress(s);
  const state=normalizeSave(s,now),seen=new Set();
  for(let i=0;i<25;i++){
    orderMilestone(state,now,'batch');const before=structuredClone(state),model=ordersModel(state,now);assert.deepEqual(state,before);
    for(const p of model.proposals)for(const option of p.options){
      const accepted=acceptProposal(structuredClone(state),p.id,option,now),allowed=new Set(accepted.groups.flatMap(g=>g.allowed));
      for(const row of option.artRows){assert.ok(allowed.has(row.key),`${p.templateId}/${option.variantId}: ${row.key}`);assert.ok(speciesDiscovered(state,row.egg,row.id));}
      seen.add(p.templateId);
    }
    for(const p of [...state.expansion.orders.proposals])skipProposal(state,p.id);
  }
  assert.ok(seen.has('O01'));assert.ok(seen.has('O04'));assert.ok(seen.size>=4);
});
