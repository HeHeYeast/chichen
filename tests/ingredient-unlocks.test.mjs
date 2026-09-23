import {CURRENT_SAVE_VERSION} from '../web/save-migrations.js';
import test from 'node:test';
import assert from 'node:assert/strict';
import { GAME_DATA as DATA } from '../web/content-pack.js';
import * as E from '../web/engine.js';
import { INGREDIENT_UNLOCK_RULES, ingredientUnlockInfo, availableIngredientIds } from '../web/ingredient-unlocks.js';
import { releasedIngredientIds } from './fixtures/ingredient-unlocks-v1.mjs';

const NOW=1800000000000;

test('all 83 ingredients have usable explanations, with three event-only gifts',()=>{
  const state=E.freshState(NOW),before=structuredClone(state);
  assert.equal(INGREDIENT_UNLOCK_RULES.length,83);
  assert.deepEqual(INGREDIENT_UNLOCK_RULES.filter(rule=>rule.special).map(rule=>rule.id),[68,69,70]);
  for(const item of DATA.tools[2]) {
    const info=ingredientUnlockInfo(state,item.id);
    assert.equal(info.id,item.id);
    assert.ok(info.description.length>0,`ingredient ${item.id}`);
    assert.ok(info.requirements.length>0);
    assert.ok(info.requirements.every(value=>typeof value==='string'&&value.length>0));
    assert.equal(info.available,info.requirementGroups.some(group=>group.met));
    if(!info.special)assert.ok(info.requirementGroups.length>0);
  }
  assert.equal(ingredientUnlockInfo(state,0).available,true);
  assert.deepEqual(state,before,'viewing catalog must not change a save');
  for(const id of [-1,83,'0',null,1.5])assert.throws(()=>ingredientUnlockInfo(state,id),/不存在/);
});

test('shared rules preserve released unlock behavior across mixed cookware and discovery states',()=>{
  let seed=3512749;
  const random=limit=>{seed=(Math.imul(seed,1664525)+1013904223)>>>0;return Math.floor(seed/4294967296*limit);};
  for(let sample=0;sample<4096;sample++) {
    const state=E.freshState(NOW);
    state.toolLevels=Array.from({length:9},()=>random(4)-1);
    state.kitchenLevel=random(4);state.duck=!!random(2);
    state.total={'0:0':random(2100),'0:1':random(2),'1:1':random(2),'1:0':random(2),'0:18':random(2),'0:35':random(2)};
    assert.deepEqual(availableIngredientIds(state),releasedIngredientIds(state),`sample ${sample}`);
    assert.deepEqual(E.availableIngredients(state),availableIngredientIds(state));
  }
});

test('count, kitchen and every cookware level retain their exact unlock boundaries',()=>{
  for(const total of [0,99,100,299,300,499,500,1999,2000])for(let kitchen=0;kitchen<4;kitchen++) {
    const state=E.freshState(NOW);state.total={'0:0':total};state.kitchenLevel=kitchen;
    assert.deepEqual(availableIngredientIds(state),releasedIngredientIds(state));
  }
  for(let tool=0;tool<9;tool++)for(let level=-1;level<3;level++)for(const hasDuck of [false,true]) {
    const state=E.freshState(NOW);state.toolLevels.fill(-1);state.toolLevels[tool]=level;state.total={'1:0':hasDuck?1:0};
    assert.deepEqual(availableIngredientIds(state),releasedIngredientIds(state),`tool ${tool}, level ${level}, duck ${hasDuck}`);
  }
});

test('explanations expose alternatives and distinguish owning duck eggs from collecting the basic duck',()=>{
  const state=E.freshState(NOW);state.cp=2500;state.toolLevels[1]=0;
  E.buyDuck(state);
  const orange=ingredientUnlockInfo(state,44);
  assert.equal(orange.available,false);
  assert.match(orange.description,/基础品种「鸭宝」/);
  assert.match(orange.description,/平底锅 Lv\.1/);
  assert.equal(orange.requirementGroups[0].conditions[0].met,false);
  state.total['1:0']=1;
  assert.equal(ingredientUnlockInfo(state,44).available,true);
  const flour=ingredientUnlockInfo(state,9);
  assert.match(flour.description,/烤箱 Lv\.1/);
  assert.match(flour.description,/水煮锅 Lv\.2/);
  assert.match(flour.description,/竹蒸笼 Lv\.1/);
  assert.equal(flour.available,false);
  state.toolLevels[8]=0;
  assert.equal(ingredientUnlockInfo(state,9).available,true);
});

test('event gifts never become zero-price purchases, including already-owned gifts',()=>{
  const state=E.freshState(NOW);state.cp=99999;state.toolLevels.fill(2);state.kitchenLevel=3;
  state.total={'1:0':1,'0:0':9999,'0:1':1,'0:18':1,'0:35':1};
  assert.equal(availableIngredientIds(state).length,72);
  for(const id of [68,69,70]) {
    state.ingredients[id]=1;
    const before=structuredClone(state),info=ingredientUnlockInfo(state,id);
    assert.equal(info.available,false);assert.equal(info.special,true);
    assert.throws(()=>E.buyIngredient(state,id),/尚未解锁/);
    assert.deepEqual(state,before);
  }
});

test('duck eggs cost 2500 CP locally and purchase preserves a live chicken batch',()=>{
  const state=E.freshState(NOW);state.cp=3000;
  E.startBatch(state,0,NOW,()=>0.5);state.selected=[0];
  const before=structuredClone(state),batch=state.batch;
  assert.deepEqual(E.duckUnlockInfo(state),{owned:false,price:2500,affordable:true,available:true,reason:''});
  assert.equal(E.buyDuck(state),2500);
  assert.equal(state.batch,batch);
  assert.deepEqual(state,{...before,cp:before.cp-2500,duck:true});
  const saved=E.parseSave(JSON.stringify(state),NOW+1000);
  assert.equal(saved.version,CURRENT_SAVE_VERSION);assert.equal(saved.duck,true);
  assert.deepEqual(saved.batch,before.batch);
});

test('duck purchase rejects insufficient funds and duplicate purchase without partial changes',()=>{
  const state=E.freshState(NOW);state.cp=2499;
  const poor=structuredClone(state);
  assert.equal(E.duckUnlockInfo(state).available,false);
  assert.throws(()=>E.buyDuck(state),/CP不足/);assert.deepEqual(state,poor);
  state.cp=2500;E.buyDuck(state);assert.equal(state.cp,0);
  state.cp=2500;const owned=structuredClone(state);
  assert.equal(E.duckUnlockInfo(state).owned,true);assert.equal(E.duckUnlockInfo(state).available,false);
  assert.throws(()=>E.buyDuck(state),/已经拥有/);assert.deepEqual(state,owned);
});

test('reading or dismissing duck purchase details never mutates progress',()=>{
  const state=E.freshState(NOW);state.cp=3000;
  const before=structuredClone(state);
  E.duckUnlockInfo(state);E.duckUnlockInfo(state);
  assert.deepEqual(state,before);
});

test('purchased duck eggs can start and collect an original duck batch',()=>{
  const state=E.freshState(NOW);state.cp=3000;E.buyDuck(state);state.egg=1;
  const batch=E.startBatch(state,0,NOW,()=>0.5);
  assert.equal(batch.egg,1);assert.ok(batch.eggs.every(egg=>egg.egg===1));
  const first=batch.eggs[0],open=first.openAt;
  E.updateBatch(state,open+1,()=>0.5);E.updateBatch(state,open+2001,()=>0.5);E.updateBatch(state,open+2901,()=>0.5);
  assert.equal(E.collect(state,0),true);
  assert.ok(state.total[`1:${first.id}`]>0);assert.ok(state.farm[`1:${first.id}`]>0);
  assert.equal(E.parseSave(JSON.stringify(state),NOW).duck,true);
});
