import {CURRENT_SAVE_VERSION} from '../web/save-migrations.js';
import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';

const NOW=1800000000000;
function legacySave(){
  const state=E.freshState(NOW);E.startBatch(state,0,NOW,()=>.25);
  delete state.batch.rules;delete state.progress;delete state.cleanCycle;state.version=1;state.toolLevels.pop();state.cp=4321;state.farm={'0:18':7,'1:3':2};state.total={'0:18':10,'1:3':6};state.duck=true;
  state.events={campaign_char_0_48:true,gift_tool_2_68_character_id:89};return state;
}

test('version one migration retains progress, selection, clocks and active batch while appending an unowned steamer',()=>{
  const old=legacySave(),before=structuredClone(old),migrated=E.parseSave(JSON.stringify(old),NOW+86400001);
  assert.equal(migrated.version,CURRENT_SAVE_VERSION);assert.equal(migrated.toolLevels.length,9);assert.equal(migrated.toolLevels[8],-1);
  assert.deepEqual(migrated.toolLevels.slice(0,8),old.toolLevels);
  for(const property of ['cp','farm','total','ingredients','selected','batch','events','lastSeen','lastClean','farmFixed','farmChecked','duck','egg'])assert.deepEqual(migrated[property],old[property],property);
  assert.deepEqual(old,before);assert.equal(migrated.dirty,false);
  E.resume(migrated,NOW+E.CLEAN_INTERVAL+1);assert.equal(migrated.dirty,true);
});

test('version two round trips new species and a partially collected steamer batch without changing deadlines',()=>{
  const state=E.freshState(NOW);state.kitchenLevel=3;state.toolLevels[8]=2;state.ingredients={71:2,37:1};state.selected=[71,37];
  E.startBatch(state,8,NOW,()=>.25);
  const egg=state.batch.eggs[0];E.updateBatch(state,egg.openAt+1,()=>.9);E.updateBatch(state,egg.openAt+2001,()=>.9);E.updateBatch(state,egg.openAt+2901,()=>.9);E.collect(state,0);
  state.batch.alarmed=true;state.alarm=true;
  const restored=E.parseSave(JSON.stringify(state),NOW+100000);
  assert.deepEqual(restored,state);assert.notEqual(restored,state);assert.notEqual(restored.batch,state.batch);
});

test('future saves and malformed current saves throw instead of returning a new game',()=>{
  const current=E.freshState(NOW);
  assert.throws(()=>E.parseSave(JSON.stringify({...current,version:99})),error=>error.code==='UNSUPPORTED_SAVE_VERSION');
  assert.throws(()=>E.parseSave('{broken'),error=>error.code==='INVALID_SAVE');
  for(const mutate of [
    state=>state.toolLevels.pop(),state=>state.cp=-1,state=>state.cp=Infinity,state=>state.cp=1.5,
    state=>state.kitchenLevel=4,state=>state.toolLevels[1]=3,state=>state.ingredients={0:-1},
    state=>state.ingredients={0:20,1:1.5},state=>state.ingredients={83:1},state=>state.total={'0:152':1},
    state=>state.farm={'2:0':1},state=>state.farm={'0:0':-1},state=>state.lastSeen='yesterday',state=>state.egg=1,
  ]){
    const candidate=structuredClone(current);mutate(candidate);const before=structuredClone(candidate);
    assert.throws(()=>E.normalizeSave(candidate),error=>error.code==='INVALID_SAVE');assert.deepEqual(candidate,before);
  }
});

test('invalid batch data is rejected atomically, including a wrong egg type or deadline',()=>{
  const valid=legacySave();
  for(const mutate of [
    state=>state.batch.eggs.pop(),state=>state.batch.eggs[0].id=999,
    state=>state.batch.eggs[0].egg=1,state=>state.batch.eggs[0].status='sold',
    state=>state.batch.ends=state.batch.started,state=>state.batch.eggs[0].openAt=state.batch.ends+1,
    state=>state.batch.eggs[0].collected=1,state=>state.batch.tool=8,
  ]){
    const candidate=structuredClone(valid);mutate(candidate);const before=structuredClone(candidate);
    assert.throws(()=>E.normalizeSave(candidate),error=>error.code==='INVALID_SAVE');assert.deepEqual(candidate,before);
  }
});

test('optional old preferences gain defaults while required progress remains strictly validated',()=>{
  const old=legacySave();for(const key of ['alarm','music','sound','events','farmChecked'])delete old[key];
  const migrated=E.normalizeSave(old,NOW);
  assert.equal(migrated.music,true);assert.equal(migrated.sound,true);assert.equal(migrated.alarm,false);assert.deepEqual(migrated.events,{});
  assert.equal(migrated.farmChecked,old.farmFixed);assert.equal(migrated.cp,old.cp);
  const incomplete=structuredClone(old);delete incomplete.total;
  assert.throws(()=>E.normalizeSave(incomplete),error=>error.code==='INVALID_SAVE');
});
