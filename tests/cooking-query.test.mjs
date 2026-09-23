import test from 'node:test';
import assert from 'node:assert/strict';
import {freshState,startBatch} from '../web/engine.js';
import {cookingIngredients} from '../web/cooking-query.js';
import {toolScrollFor} from '../web/tool-strip.js';

test('preparation uses the same available, deduplicated, capacity-limited ingredients as cooking',()=>{
  for(let level=0;level<4;level++){
    const state=freshState(1000);state.cp=100000;state.kitchenLevel=level;
    state.selected=[0,0,1,2,3];state.ingredients={0:2,1:0,2:1,3:1};
    const before=structuredClone(state),expected=[0,2,3].slice(0,Math.min(3,level+1));
    assert.deepEqual(cookingIngredients(state),expected);
    assert.deepEqual(state,before,'query does not modify any player state');
    startBatch(state,0,1000,()=>0.5);
    assert.deepEqual(state.batch.ingredients,expected);
  }
});
test('all prepared cookware IDs are visible within a clamped four-slot window',()=>{
  for(let id=0;id<9;id++){
    const start=toolScrollFor(id,5);assert.ok(start>=0&&start<=5&&id>=start&&id<start+4);
  }
});
