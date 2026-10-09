import test from 'node:test';
import assert from 'node:assert/strict';
import {freshState} from '../web/engine.js';
import {REGIONAL} from '../web/content-registry.js';
import {RULES} from '../web/integration-data.js';
import {menuCore} from '../web/business-home.js';
import {suggestBusinessStock} from '../web/business-advisor.js';
import {prepareBusiness,openBusiness} from '../web/business.js';
import {inventoryView} from '../web/inventory.js';

const NOW=1800000000000;
function fixture(stock){
  const s=freshState(NOW,3);s.kitchenLevel=3;s.toolLevels.fill(2);s.duck=true;
  for(const o of RULES.storyOrders)s.progress.orders[o.id]={...(s.progress.orders[o.id]??{}),completed:true};
  s.total={'0:0':3000,'0:3':1,'0:8':1};s.farm={...stock};
  for(const c of REGIONAL.species)s.total[c.key]=1;
  for(const [key,n]of Object.entries(stock))s.total[key]=(s.total[key]??0)+n;
  s.expansion.regions.guideFlags=['GUIDE-B'];
  s.expansion.discovery.identified=Object.fromEntries(REGIONAL.materials.map(m=>[m.id,1]));
  return s;
}
for(const menu of REGIONAL.menus)for(const [i,example]of menu.examples.entries())test(`${menu.id} example ${i+1}: owned menu birds can be stocked and reserved exactly`,()=>{
  const s=fixture(Object.fromEntries(example.map(x=>[x.key,x.quantity+1])));
  const suggested=suggestBusinessStock(s,menu.id,{now:NOW});
  assert.equal(prepareBusiness(s,{menuId:menu.id,stock:suggested.stock}).fit.complete,true);
  const before={...s.farm};openBusiness(s,{menuId:menu.id,stock:suggested.stock},NOW);
  assert.deepEqual(s.farm,before,'opening reserves birds; it does not sell them yet');
  for(const [key,n]of Object.entries(suggested.stock)){assert.equal(inventoryView(s,key).S,n);assert.equal(inventoryView(s,key).home,1);}
});
test('empty manual stock stays empty even when a complete automatic plan exists',()=>{
  const s=fixture({'0:0':30,'0:21':20}),core=menuCore(s,'MN1',{},NOW);
  assert.equal(core.count,0);assert.equal(core.income,0);assert.equal(core.complete,false);
  assert.ok(core.slots.every(x=>x.have===0&&!x.ok));
  assert.ok(core.slots.some(x=>x.key==='0:21'&&x.available>0),'available alternative is shown instead of missing example bird');
});
test('partial shelf counts do not become warehouse counts',()=>{
  const s=fixture({'0:0':30,'0:21':20}),core=menuCore(s,'MN1',{'0:0':1},NOW);
  const chick=core.slots.find(x=>x.key==='0:0');
  assert.equal(chick.have,1);assert.equal(chick.ok,false);assert.equal(chick.makeable,false);
  assert.equal(core.slots.find(x=>x.key==='0:21').have,0);
});
test('only copy is protected until the player changes the home lock',()=>{
  const s=fixture({'0:0':1});assert.equal(suggestBusinessStock(s,'MN1',{now:NOW}),null);
  assert.throws(()=>prepareBusiness(s,{stock:{'0:0':1}}),/锁定/);
  s.expansion.inventoryPolicy.locks={'0:0':0};
  assert.doesNotThrow(()=>prepareBusiness(s,{stock:{'0:0':1}}));
});
test('collection lock forbids stocking while still owning the species',()=>{
  const s=fixture({'0:0':30,'0:21':20});s.expansion.inventoryPolicy.collectionLocks=['0:0'];
  assert.throws(()=>prepareBusiness(s,{stock:{'0:0':6}}),/收藏/);
  assert.equal(suggestBusinessStock(s,'MN1',{now:NOW}).stock['0:0'],undefined);
  const slot=menuCore(s,'MN1',{},NOW).slots.find(x=>x.key==='0:0');
  assert.equal(slot.available,0);
});
