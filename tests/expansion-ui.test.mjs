import test from 'node:test';
import assert from 'node:assert/strict';
import {freshState} from '../web/engine.js';
import {recipePreparation,prepareExpansionRecipe} from '../web/expansion-ui.js';

test('recipe selection checks ownership, level and every ingredient without changing rejected states',()=>{
  const state=freshState(1800000000000);
  assert.equal(recipePreparation(state,999),null);
  let before=structuredClone(state);assert.throws(()=>prepareExpansionRecipe(state,114),/Lv.1/);assert.deepEqual(state,before);
  state.toolLevels[8]=1;state.ingredients={24:1};before=structuredClone(state);
  assert.deepEqual(recipePreparation(state,117).missing,[37]);
  assert.throws(()=>prepareExpansionRecipe(state,117),/调味料/);assert.deepEqual(state,before);
  assert.throws(()=>prepareExpansionRecipe(state,119),/Lv.3/);assert.deepEqual(state,before);
  state.ingredients[37]=1;const ready=recipePreparation(state,117);
  assert.equal(ready.ready,true);prepareExpansionRecipe(state,117);assert.deepEqual(state.selected,[24,37]);
  prepareExpansionRecipe(state,114);assert.deepEqual(state.selected,[]);
});
