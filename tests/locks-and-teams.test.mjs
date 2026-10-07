import test from 'node:test';
import assert from 'node:assert/strict';
import {freshState,sell} from '../web/engine.js';
import {depart} from '../web/exploration.js';
import {advanceWorld} from '../web/world-clock.js';
import {inventoryView,lockedCount,spareCount,validateConsumption} from '../web/inventory.js';
import {validateBusinessState} from '../web/business-save.js';
import {bulkSaleSelection} from '../web/collection-ui.js';
import {syncProgress} from '../web/progression.js';
const NOW=1800000000000;
const check=s=>validateBusinessState(s,m=>{throw Error(m);});
function farm(counts){const s=freshState(NOW);s.farm={...counts};s.total={...counts};return s;}

test('every kind is locked at one by default, the old switch sets the default, explicit locks win',()=>{
  const s=farm({'0:0':5,'0:3':2});
  assert.equal(lockedCount(s,'0:0'),1);
  s.expansion.inventoryPolicy.keepOne=false;assert.equal(lockedCount(s,'0:0'),0);
  s.expansion.inventoryPolicy.locks={'0:0':3,'0:3':0};
  assert.equal(lockedCount(s,'0:0'),3);assert.equal(lockedCount(s,'0:3'),0);
  s.expansion.inventoryPolicy.keepOne=true;assert.equal(lockedCount(s,'0:8'),1,'a kind without an entry uses the default');
});

test('selling, 全选 and consumption stop at the lock; unlocking frees the rest',()=>{
  const s=farm({'0:0':6,'0:3':1});
  assert.equal(spareCount(s,'0:0'),5);assert.equal(spareCount(s,'0:3'),0);
  assert.deepEqual(bulkSaleSelection(s),{'0:0':5});
  assert.throws(()=>validateConsumption(s,{'0:0':6},{keepOne:true}),/锁定/);
  s.expansion.inventoryPolicy.locks={'0:0':4,'0:3':0};
  assert.deepEqual(bulkSaleSelection(s),{'0:0':2,'0:3':1});
  assert.throws(()=>sell(s,{'0:0':3},{keepOne:true},NOW));
  sell(s,{'0:0':2,'0:3':1},{keepOne:true},NOW);assert.equal(s.farm['0:0'],4);assert.equal(s.farm['0:3'],0);
});

test('a save with lock counts validates and a bad lock is rejected',()=>{
  const s=farm({'0:0':3});
  s.expansion.inventoryPolicy.locks={'0:0':2};assert.equal(check(s),true);
  s.expansion.inventoryPolicy.locks={'0:0':-1};assert.throws(()=>check(s));
  s.expansion.inventoryPolicy.locks={'9:9':1};assert.throws(()=>check(s));
  s.expansion.inventoryPolicy.locks={'0:0':'2'};assert.throws(()=>check(s));
});

test('a 日常寻访 team may bring one kind twice; the last copies leave the farm and come back',()=>{
  const s=farm({'0:0':2,'0:3':1});s.cp=100000;s.kitchenLevel=3;for(let i=0;i<89;i++)s.total['0:'+i]=100;syncProgress(s);
  assert.throws(()=>depart(s,{routeId:'yard',members:['0:3','0:3']},NOW),/在家/);
  depart(s,{routeId:'yard',members:['0:0','0:0','0:3']},NOW);
  assert.equal(inventoryView(s,'0:0').R,2);assert.equal(inventoryView(s,'0:0').free,0);
  assert.equal(inventoryView(s,'0:3').R,1);assert.equal(inventoryView(s,'0:3').free,0,'a locked last copy may go out');
  const t=s.progress.trip;advanceWorld(s,t.endAt+1);
  assert.equal(inventoryView(s,'0:0').free,2);assert.equal(inventoryView(s,'0:3').free,1,'back on the farm after the trip');
});
