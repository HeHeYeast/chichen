import test from 'node:test';
import assert from 'node:assert/strict';
import {INVESTMENT_CASES,investmentPair,prerequisites,gateEvidence} from '../tools/simulate-investment-pairs.mjs';

test('kitchen entry equipment is actually purchased before either paired arm starts',()=>{
 const first=prerequisites('kitchen-1-2');assert.deepEqual(first.s.toolLevels.slice(0,6),[0,0,0,0,0,0]);assert.equal(first.s.kitchenLevel,0);assert.equal(first.s.cp,92000);assert.equal(first.trace.length,5);
 const second=prerequisites('kitchen-2-3');assert.deepEqual(second.s.toolLevels.slice(0,6),[1,1,1,1,1,1]);assert.equal(second.s.kitchenLevel,1);assert.equal(second.s.cp,57000);assert.equal(second.trace.length,12);
});
test('failed equipment / capital prerequisites are atomic and duck price is exactly 2500',()=>{
 const rows=gateEvidence();assert.equal(rows.length,6);assert.ok(rows.filter(x=>x.blocked).every(x=>x.unchanged));const exact=rows.find(x=>x.id==='duck-exact-2500');assert.equal(exact.cost,2500);assert.equal(exact.remainingCP,0);assert.equal(exact.owned,true);
});
const costs={'kitchen-1-2':10000,'kitchen-2-3':20000,duck:2500,'frying-upgrade-1-2':1500,'frying-purchase':500};
for(const def of INVESTMENT_CASES)test(`${def.id}: same starting assets, one explicit investment, conserved benchmark cash`,()=>{
 const row=investmentPair(def.id,3);assert.equal(row.control.investmentCost,0);assert.equal(row.treatment.investmentCost,costs[def.id]);assert.deepEqual(row.control.pre,row.treatment.pre);
 for(const branch of [row.control,row.treatment]){assert.ok(branch.batches<=30);assert.ok(branch.productionMinutes<=360);assert.equal(branch.units,branch.batches*24);assert.equal(branch.failures.length,0);assert.equal(branch.operatingCP,branch.rows.reduce((n,b)=>n+b.netCP,0));assert.equal(branch.netIncludingInvestment,branch.operatingCP-branch.investmentCost);assert.ok(branch.rows.every(b=>b.quantity===24));assert.ok(branch.trace.every((r,i)=>!i||r.at>=branch.trace[i-1].at&&r.seq===branch.trace[i-1].seq+1));}
 assert.equal(row.incrementalNet,row.incrementalOperatingCP-row.treatment.investmentCost);assert.ok(row.control.post.executableRecipeKeys.every(k=>row.treatment.post.executableRecipeKeys.includes(k)),'investment must preserve the old executable recipes');
});
test('kitchen buys slots/capacity without magically speeding the same old cookware',()=>{
 for(const [id,slots,capacity]of [['kitchen-1-2',2,48],['kitchen-2-3',3,72]]){const row=investmentPair(id,2);assert.equal(row.treatment.post.slots,slots);assert.equal(row.treatment.post.businessStockCapacity,capacity);assert.equal(row.treatment.post.earnedSkillPoints-row.control.post.earnedSkillPoints,2);assert.equal(row.control.batchMinutes[0],row.treatment.batchMinutes[0]);assert.equal(row.control.batches,row.treatment.batches);assert.equal(row.incrementalOperatingCP,0);}
});
test('representative cookware upgrade reduces minutes and fits more batches in the same budget',()=>{
 const row=investmentPair('frying-upgrade-1-2',2);assert.equal(row.control.batchMinutes[0],15);assert.equal(row.treatment.batchMinutes[0],13);assert.ok(row.treatment.batches>row.control.batches);
});
test('duck purchase unlocks duck recipes; its 2500 CP cost is not charged for every batch',()=>{
 const row=investmentPair('duck',2);assert.equal(row.control.pre.duck.owned,false);assert.equal(row.control.post.duck.owned,false);assert.equal(row.treatment.post.duck.owned,true);assert.ok(row.newExecutableRecipeKeys.some(k=>k.startsWith('1:')));assert.equal(row.treatment.ledger['investment-duck'].count,1);assert.equal(row.treatment.ledger['investment-duck'].spend,2500);
});
