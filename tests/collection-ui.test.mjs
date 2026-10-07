import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {collectionPageModel,speciesView,saleSelectionSummary,harvestEntries,bulkSaleSelection} from '../web/collection-ui.js';

const NOW=1800000000000;

test('bulk sale includes both species ledgers, skips zero stock and retains one of every owned variety',()=>{
  const s=E.freshState(NOW);s.farm={'0:0':6,'0:4':1,'0:117':3,'1:0':4,'1:64':1,'0:3':0};s.total={...s.farm,'0:3':2};
  E.startBatch(s,0,NOW,()=>.5);const before=structuredClone(s);
  // 「全选」 sells down to the locked number, one per kind unless the player changed it
  const kept=bulkSaleSelection(s);assert.deepEqual(kept,{'0:0':5,'0:117':2,'1:0':3});assert.deepEqual(s,before);
  const custom=structuredClone(s);custom.expansion.inventoryPolicy.locks={'0:4':0,'0:0':3};
  assert.deepEqual(bulkSaleSelection(custom),{'0:0':3,'0:4':1,'0:117':2,'1:0':3});
  const amount=saleSelectionSummary(s,kept);E.sell(s,amount.selection);
  assert.equal(s.cp,before.cp+amount.income);assert.deepEqual(s.total,before.total);assert.deepEqual(s.batch,before.batch);
  assert.deepEqual(s.farm,{'0:0':1,'0:4':1,'0:117':1,'1:0':1,'1:64':1,'0:3':0});assert.deepEqual(bulkSaleSelection(s),{});
  s.expansion.inventoryPolicy.keepOne=false;E.sell(s,bulkSaleSelection(s));assert.ok(Object.values(s.farm).every(n=>n===0));assert.deepEqual(s.total,before.total);
  assert.deepEqual(bulkSaleSelection(s),{});
});

test('unfound collection entries expose only an unknown label and no real sale price',()=>{
  const state=E.freshState(NOW);
  for(const egg of [0,1]){
    const entry=speciesView(state,egg,4),page=collectionPageModel(state,{egg});
    assert.equal(entry.known,false);
    assert.equal(entry.name,'未发现');
    assert.equal(entry.price,null);
    assert.equal(entry.stock,0);
    assert.equal(entry.total,0);
    assert.equal(JSON.stringify(page).includes(E.label(E.char(egg,4))),false);
    assert.ok(page.entries.every(item=>!item.known&&item.price===null));
  }
});

test('a lifetime discovery stays visible without stock and possessed species are visible',()=>{
  const state=E.freshState(NOW);
  state.total['0:3']=12;
  state.farm['0:4']=2;
  const discovered=speciesView(state,0,3),possessed=speciesView(state,0,4);
  assert.equal(discovered.known,true);
  assert.equal(discovered.name,'香煎鸡');
  assert.equal(discovered.price,4);
  assert.equal(discovered.stock,0);
  assert.equal(discovered.total,12);
  assert.equal(possessed.known,true);
  assert.equal(possessed.name,'荷包蛋鸡');
  assert.deepEqual(harvestEntries(state,0,true).map(item=>item.id),[4]);
  assert.deepEqual(harvestEntries(state,0,false).map(item=>item.id),[3,4]);
});

test('nine-slot collection pages clamp to the final chicken and duck pages without losing IDs',()=>{
  const state=E.freshState(NOW);
  assert.equal(collectionPageModel(state).entries.length,9);
  const chicken=collectionPageModel(state,{egg:0,page:999});
  assert.equal(chicken.pages,17);
  assert.equal(chicken.page,16);
  assert.deepEqual(chicken.entries.map(item=>item.id),[144,145,146,147,148,149,150,151]);
  const duck=collectionPageModel(state,{egg:1,page:999});
  assert.equal(duck.pages,10);
  assert.equal(duck.page,9);
  assert.deepEqual(duck.entries.map(item=>item.id),[81,82,83,84,85,86,87,88]);
  assert.equal(collectionPageModel(state,{page:-5}).page,0);
});

test('known-only collection has a valid empty page and never inserts unknown species',()=>{
  const state=E.freshState(NOW),empty=collectionPageModel(state,{page:12,knownOnly:true});
  assert.deepEqual(empty.entries,[]);
  assert.equal(empty.page,0);
  assert.equal(empty.pages,1);
  assert.equal(empty.discovered,0);
  assert.deepEqual(harvestEntries(state),[]);
  state.total['0:0']=8;
  state.total['0:4']=1;
  const found=collectionPageModel(state,{knownOnly:true});
  assert.equal(found.discovered,2);
  assert.deepEqual(found.entries.map(item=>item.id),[0,4]);
  assert.ok(found.entries.every(item=>item.known));
  assert.deepEqual(collectionPageModel(state,{egg:1,knownOnly:true}).entries,[]);
});

test('mixed chicken and duck sale selection clamps to stock and does not mutate the draft',()=>{
  const state=E.freshState(NOW);
  state.farm={'0:0':3,'0:4':8,'1:0':2};
  state.total={'0:0':10,'0:3':1,'0:4':8,'1:0':4};
  const draft={'0:0':99,'0:3':5,'0:4':2,'1:0':1,'0:999':8,'bad':12};
  const original=structuredClone(draft),before=structuredClone(state);
  const sale=saleSelectionSummary(state,draft);
  assert.deepEqual(sale.selection,{'0:0':3,'0:4':2,'1:0':1});
  assert.equal(sale.quantity,6);
  assert.equal(sale.income,22);
  assert.deepEqual(draft,original);
  assert.deepEqual(state,before);
  assert.deepEqual(saleSelectionSummary(state,{'0:0':-1,'0:4':NaN,'1:0':Infinity}).selection,{});
});

test('selling a captured selection uses its original quantities and preserves the encyclopedia',()=>{
  const state=E.freshState(NOW);
  state.farm={'0:0':3,'0:4':8,'1:0':2};
  state.total={'0:0':10,'0:4':8,'1:0':4};
  const draft={'0:0':3,'0:4':2,'1:0':1};
  const summary=saleSelectionSummary(state,draft),snapshot=Object.freeze({...summary.selection});
  draft['0:4']=8;
  draft['1:0']=2;
  const lifetime=structuredClone(state.total),beforeCP=state.cp;
  assert.equal(E.sell(state,snapshot),22);
  assert.equal(state.cp,beforeCP+22);
  assert.deepEqual(state.farm,{'0:0':0,'0:4':6,'1:0':1});
  assert.deepEqual(state.total,lifetime);
  assert.equal(speciesView(state,0,0).known,true);
  assert.equal(speciesView(state,0,0).stock,0);
  assert.equal(collectionPageModel(state,{knownOnly:true}).discovered,2);
  const refreshed=saleSelectionSummary(state,draft);
  assert.deepEqual(refreshed.selection,{'0:4':6,'1:0':1});
  assert.equal(refreshed.income,33);
});

test('new dim sum discoveries enter the existing book and harvest ledger without renumbering old species',()=>{
  const state=E.freshState(NOW);state.total['0:117']=3;state.farm['0:117']=2;state.total['0:113']=1;
  const last=collectionPageModel(state,{egg:0,page:13});
  assert.equal(last.total,152);assert.equal(last.discovered,2);
  assert.equal(last.entries[0].name,'奶黄流沙鸡');assert.equal(last.entries[0].price,20);
  assert.equal(last.entries[1].name,'未发现');
  assert.deepEqual(harvestEntries(state).map(entry=>entry.id),[117]);
  const sale=saleSelectionSummary(state,{'0:117':2});assert.equal(sale.income,40);
  assert.equal(E.sell(state,sale.selection),40);assert.equal(state.total['0:117'],3);assert.equal(speciesView(state,0,113).name,'咖喱面包鸡');
});
