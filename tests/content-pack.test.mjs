import test from 'node:test';
import assert from 'node:assert/strict';
import {DATA} from '../web/data.js';
import {GAME_DATA,EXPANSION,TOOL_COUNT,expansionRecipes,expansionRecipeHints,expansionUnlockInfo} from '../web/content-pack.js';
import {characterImage,toolImage} from '../web/catalog.js';
import * as E from '../web/engine.js';
import {farmDisplay} from '../web/farm.js';

const NOW=1800000000000;
const rng=(seed=7)=>()=>{seed=(Math.imul(1664525,seed)+1013904223)>>>0;return seed/4294967296;};
function prepared(level=0){
  const state=E.freshState(NOW);state.cp=100000;state.kitchenLevel=level+1;
  for(let id=0;id<12;id++)state.total[`0:${id}`]=1;
  state.toolLevels[8]=level;return state;
}

test('dim sum appends permanent IDs without altering original recipes or character values',()=>{
  assert.deepEqual(DATA.characters.map(list=>list.length),[114,57]);
  assert.deepEqual(GAME_DATA.characters.map(list=>list.length),[152,89]);
  assert.deepEqual(GAME_DATA.characters[0].slice(0,114),DATA.characters[0]);
  assert.deepEqual(GAME_DATA.characters[1].slice(0,57),DATA.characters[1]);
  assert.deepEqual(GAME_DATA.tools[1].slice(0,8),DATA.tools[1]);
  assert.equal(TOOL_COUNT,9);
  assert.deepEqual(EXPANSION.characters.map(c=>c.id),[114,115,116,117,118,119]);
  EXPANSION.characters.forEach((c,index)=>assert.equal(characterImage(0,c.id),`/web/art/expansion-dim-sum.png#chick${index}`));
  [0,1,2].forEach(level=>assert.equal(toolImage(1,8,level),`/web/art/expansion-dim-sum.png#tool${level}`));
});

test('steamer needs the second kitchen and twelve distinct discoveries, then follows kitchen growth',()=>{
  const state=E.freshState(NOW);state.cp=100000;state.total['0:0']=99999;
  assert.equal(E.canBuyTool(state,8),false);
  state.kitchenLevel=1;
  assert.equal(expansionUnlockInfo(state).discovered,1);
  assert.equal(E.canBuyTool(state,8),false);
  for(let id=0;id<12;id++)state.total[`0:${id}`]=1;
  assert.equal(E.canBuyTool(state,8),true);assert.equal(E.buyTool(state,8),3000);
  assert.equal(E.canBuyTool(state,8),false);
  for(const [kitchen,price,minutes]of [[2,6000,38],[3,12000,30]]){
    state.kitchenLevel=kitchen;assert.equal(E.buyTool(state,8),price);
    assert.equal(E.cookInfo(state,8).minutes,minutes);
  }
  const before=structuredClone(state);assert.equal(E.canBuyTool(state,8),false);
  assert.throws(()=>E.buyTool(state,8));assert.deepEqual(state,before);
  assert.equal(E.canBuyTool(state,9),false);assert.equal(E.canBuyTool(state,-1),false);
});

test('steamer cost rejection is atomic and it does not become a kitchen upgrade prerequisite',()=>{
  const state=prepared();state.toolLevels[8]=-1;state.cp=2999;
  const before=structuredClone(state);assert.throws(()=>E.buyTool(state,8),/CP不足/);assert.deepEqual(state,before);
  state.toolLevels.fill(1,0,6);state.cp=20000;
  assert.equal(E.kitchenUpgradeInfo(state).canUpgrade,true);
  E.upgradeKitchen(state,NOW);assert.equal(state.kitchenLevel,2);assert.equal(state.toolLevels[8],-1);
});

test('new ingredient routes unlock per steamer tier, leaving an unowned steamer inert',()=>{
  const state=E.freshState(NOW);state.kitchenLevel=1;
  assert.deepEqual(E.availableIngredients(state),[0]);
  state.toolLevels[8]=0;
  assert.ok([9,16].every(id=>E.availableIngredients(state).includes(id)));
  assert.ok([24,25,37,71].every(id=>!E.availableIngredients(state).includes(id)));
  state.toolLevels[8]=1;
  assert.ok([9,16,24,25,37].every(id=>E.availableIngredients(state).includes(id)));
  assert.equal(E.availableIngredients(state).includes(71),false);
  state.toolLevels[8]=2;assert.ok(E.availableIngredients(state).includes(71));
});

test('each unlocked recipe is learnable and guarantees its target within a compact 24 egg batch',()=>{
  for(const recipe of EXPANSION.characters){
    const state=prepared(recipe.minLevel);
    state.ingredients=Object.fromEntries(recipe.ingredients.map(id=>[id,2]));state.selected=[...recipe.ingredients];
    for(const seed of [1,7,42,99]){
      const copy=structuredClone(state),batch=E.startBatch(copy,8,NOW,rng(seed));
      assert.equal(batch.eggs.length,24);assert.ok(batch.eggs.some(egg=>egg.id===recipe.id),recipe.title_zh_CN);
      assert.equal(copy.cp,state.cp-120);assert.equal(batch.ends,NOW+EXPANSION.levels[recipe.minLevel].minutes*60000);
      for(const id of recipe.ingredients)assert.equal(copy.ingredients[id],1);
      for(let row=0;row<4;row++)assert.equal(batch.eggs.filter(egg=>Math.abs(egg.y-(199+26*row))<=3).length,6);
    }
  }
});

test('recipe hints and production agree about locked levels and multi-ingredient combinations',()=>{
  const state=prepared(0),hints=expansionRecipeHints(state);
  assert.equal(hints.length,6);assert.deepEqual(hints.filter(h=>h.unlocked).map(h=>h.id),[114,115,116]);
  assert.equal(hints.find(h=>h.id===117).hint,'奶油 ＋ 白砂糖');
  assert.ok(expansionRecipes(state,0,8,[24,37],rng()).every(id=>id===114));
  state.toolLevels[8]=1;
  assert.ok(expansionRecipes(state,0,8,[24],rng()).every(id=>id===114));
  assert.ok(expansionRecipes(state,0,8,[24,37],rng()).includes(117));
  const combo=expansionRecipes(state,0,8,[9,16,25],rng());
  for(const id of [115,116,118])assert.ok(combo.includes(id));
  assert.equal(expansionRecipes(state,0,0,[],rng()),null);
});

test('unsupported egg type and invalid random data fail before spending or consuming',()=>{
  const state=prepared();state.egg=1;state.duck=true;state.ingredients={9:1};state.selected=[9];
  const before=structuredClone(state);assert.throws(()=>E.startBatch(state,8,NOW,rng()),/只调理鸡蛋/);assert.deepEqual(state,before);
  state.egg=0;const validEgg=structuredClone(state);
  assert.throws(()=>E.startBatch(state,8,NOW,()=>NaN),/随机数/);assert.deepEqual(state,validEgg);
});

test('new species hatch, sell at their own prices, retain discoveries and appear in all farm periods',()=>{
  const state=prepared(2);state.ingredients={71:1,37:1};state.selected=[71,37];
  const batch=E.startBatch(state,8,NOW,rng()),index=batch.eggs.findIndex(egg=>egg.id===119),egg=batch.eggs[index];
  E.updateBatch(state,egg.openAt+1,()=>.9);E.updateBatch(state,egg.openAt+2001,()=>.9);E.updateBatch(state,egg.openAt+2901,()=>.9);
  assert.equal(egg.status,'ready');assert.equal(E.collect(state,index),true);
  assert.equal(E.sell(state,{'0:119':1}),28);assert.equal(state.farm['0:119'],0);assert.equal(state.total['0:119'],1);
  for(const species of EXPANSION.characters)state.farm[`0:${species.id}`]=100;
  for(const hour of [6,12,18,22]){
    const date=new Date(NOW);date.setHours(hour);
    const ids=farmDisplay(state,date.getTime(),rng()).filter(entry=>entry.id>=114).map(entry=>entry.id).sort();
    assert.deepEqual(ids,[114,115,116,117,118,119]);
  }
});

test('new recipes keep existing cleanliness, late collection and protection rules',()=>{
  const state=prepared(),batch=E.startBatch(state,8,NOW,rng()),egg=batch.eggs[0];
  state.dirty=true;state.cleanCycle.dirtyAt=NOW;E.updateBatch(state,egg.openAt+1,()=>.3);assert.equal(egg.id,1);
  egg.id=114;egg.status='ready';E.updateBatch(state,egg.blackAt+1,()=>.9);assert.equal(egg.id,2);
  const protectedState=prepared();protectedState.ingredients={36:1};protectedState.selected=[36];
  E.startBatch(protectedState,8,NOW,rng());assert.ok(protectedState.batch.eggs.every(egg=>egg.blackAt===null));
});
