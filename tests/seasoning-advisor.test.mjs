import test from 'node:test';
import assert from 'node:assert/strict';
import {freshState} from '../web/engine.js';
import {expectedOutcome,seasoningAdvice,rankAdvice,openDemands} from '../web/seasoning-advisor.js';

const NOW=1800000000000;
const total=counts=>[...counts.values()].reduce((a,b)=>a+b,0);

test('expected outcomes are whole batches: 24 birds for the original pools',()=>{
  const s=freshState(NOW,7);
  for(const ingredients of [[],[0]])assert.ok(Math.abs(total(expectedOutcome(s,0,ingredients,NOW).counts)-24)<1e-9);
});

test('advice only uses recipes the player already knows; nothing is spoiled',()=>{
  const s=freshState(NOW,7),before=structuredClone(s);
  // 皮蛋鸡 (0:5) is the heat lamp + 食盐土 recipe; it is not known in a fresh save.
  assert.deepEqual(seasoningAdvice(s,0,NOW).rows.map(r=>r.key),['']);
  s.total['0:5']=1;s.farm['0:5']=1;
  const rows=seasoningAdvice(s,0,NOW).rows;
  assert.deepEqual(rows.map(r=>r.key).sort(),['','0']);
  const salt=rows.find(r=>r.key==='0');
  assert.ok(salt.birds.some(b=>b.key==='0:5'&&b.n>0),'the known recipe shows its own partner');
  assert.ok(Math.abs(salt.birds.reduce((v,b)=>v+b.n,0)-24)<1e-9);
  assert.deepEqual(before.toolLevels,s.toolLevels,'advice is read-only');
});

test('a known recipe whose seasoning cannot be bought yet is left out',()=>{
  const s=freshState(NOW,7);
  s.total['0:25']=1;s.farm['0:25']=1; // 雪人鸡 needs 雪晶, locked in a fresh save
  assert.equal(seasoningAdvice(s,0,NOW).rows.some(r=>r.key==='17'||r.ingredients.length),false);
});

test('missing but purchasable seasonings are priced, and owned ones are not',()=>{
  const s=freshState(NOW,7);s.total['0:5']=1;s.farm['0:5']=1;s.ingredients[0]=0;
  const salt=seasoningAdvice(s,0,NOW).rows.find(r=>r.key==='0');
  assert.deepEqual(salt.missing,[0]);assert.ok(salt.buyCost>0);
  s.ingredients[0]=2;assert.deepEqual(seasoningAdvice(s,0,NOW).rows.find(r=>r.key==='0').missing,[]);
});

test('task lens scores sets by what open orders still need after the stock that can be spared',()=>{
  const s=freshState(NOW,7);s.total['0:5']=2;s.farm['0:5']=2;
  s.expansion.orders.active=[{id:'order-1',templateId:'O01',reserved:{},groups:[{id:'g',quantity:6,delivered:{},allowed:['0:5']}]}];
  assert.equal(openDemands(s)[0].short,5,'one spare 皮蛋鸡 counts; the one kept at home does not');
  s.farm['0:5']=1;assert.equal(openDemands(s)[0].short,6,'a single 皮蛋鸡 stays at home');s.farm['0:5']=2;
  const best=rankAdvice(seasoningAdvice(s,0,NOW),'tasks');
  assert.equal(best[0].key,'0');assert.ok(best[0].tasks[0].n>0&&best[0].tasks[0].n<=5);
  assert.equal(rankAdvice(seasoningAdvice(s,0,NOW),'tasks').some(r=>r.key===''),false,'plain batches do not hatch 皮蛋鸡');
});

test('tools the player does not own give no advice',()=>{
  const s=freshState(NOW,7);
  assert.equal(seasoningAdvice(s,1,NOW).owned,false);
});
