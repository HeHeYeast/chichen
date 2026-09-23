import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {GAME_DATA as DATA} from '../web/content-pack.js';
import {RECIPE_CATALOG,discoveredRecipe,prepareDiscoveredRecipe} from '../web/recipe-book.js';
import {DISCOVERY_CLUES,discoveryClue} from '../web/discovery-clues.js';
import {makeBackup,parseBackup} from '../web/save-store.js';

test('every current partner has a short authored clue, without its name, cookware name or recipe ingredients',()=>{
  const state=E.freshState();
  assert.equal(Object.keys(DISCOVERY_CLUES).length,193);
  assert.deepEqual(Object.keys(DISCOVERY_CLUES).sort(),RECIPE_CATALOG.map(r=>r.key).sort());
  for(const row of RECIPE_CATALOG){
    const clue=discoveryClue(state,row.egg,row.id);
    assert.deepEqual(Object.keys(clue),['key','text']);
    assert.ok(clue.text.length>=12&&clue.text.length<=48,row.key);
    const name=DATA.characters[row.egg].find(c=>c.id===row.id).title_zh_CN;
    const directAnswers=[name,...row.ingredients.map(id=>DATA.tools[2][id].title_zh_CN)];
    if(row.toolId>=0)directAnswers.push(DATA.tools[1][row.toolId].title_zh_CN);
    for(const answer of directAnswers)assert.ok(!clue.text.includes(answer),`${row.key} reveals ${answer}`);
    assert.doesNotMatch(clue.text,/Lv\.|\d+\s*(?:CP|份|分钟)|[＋+]/);
  }
});

test('reading every clue leaves the current batch, choices, resources and discovery records untouched',()=>{
  const state=E.freshState();E.startBatch(state,0,Date.now(),()=>.5);
  const before=structuredClone(state);
  for(const row of RECIPE_CATALOG){
    assert.ok(discoveryClue(state,row.egg,row.id));
    assert.equal(discoveredRecipe(state,row.key),null);
    assert.throws(()=>prepareDiscoveredRecipe(state,row.key),/收取/);
  }
  assert.deepEqual(state,before);
});

test('known partners keep their existing recipe instead of an unknown clue, including after sale and backup import',()=>{
  const state=E.freshState();state.total['0:120']=1;state.farm['0:120']=1;
  assert.equal(discoveryClue(state,0,120),null);
  E.sell(state,{'0:120':1});
  const restored=parseBackup(makeBackup(state));
  assert.equal(discoveryClue(restored,0,120),null);
  assert.ok(discoveredRecipe(restored,'0:120'));
  state.farm['1:64']=1;assert.equal(discoveryClue(state,1,64),null);
  assert.equal(discoveryClue(state,0,999),null);
  assert.equal(discoveryClue(state,2,0),null);
});

test('handmade and special clues suggest an impression or circumstance without copying the complete appearance rule',()=>{
  const state=E.freshState();
  assert.match(discoveryClue(state,0,120).text,/软糯.*酸甜/);
  assert.match(discoveryClue(state,0,9).text,/破壳.*瞬/);
  assert.match(discoveryClue(state,0,1).text,/打扫/);
  assert.match(discoveryClue(state,0,104).text,/远行/);
  assert.match(discoveryClue(state,0,52).text,/太阳/);
  assert.doesNotMatch(discoveryClue(state,0,52).text,/10:00|12:59|10%/);
});
