import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {earnedSources} from '../web/progression.js';
import {departRegional} from '../web/regional-exploration.js';
import {claimTrip} from '../web/exploration.js';
import {identifyMaterial,prepareRegionalRecipe,regionalRecipeInfo} from '../web/regional-methods.js';
import {makeBackup,parseBackup} from '../web/save-store.js';

const NOW=1800000000000,RECIPE='REC-V-C1',KEY='0:128';
function readyState(){
  // A legitimate early-game boundary fixture, followed by the real discovery,
  // identification and three complete free-method trips (no all-unlock state).
  const s=E.freshState(NOW,20260923);
  s.total={'0:0':116,'0:3':1,'0:4':1,'0:8':1,'0:18':1};
  s.farm={'0:0':3,'0:3':1,'0:4':1,'0:8':1,'0:18':1};s.toolLevels[1]=0;s.progress.sources=earnedSources(s);
  let at=NOW;
  for(let i=0;i<4;i++){
    departRegional(s,{regionId:'V',placeId:'V:0',focus:'specimen',members:['0:0']},at);
    at=s.progress.trip.endAt;
    E.advanceWorld(s,at,()=>.99);claimTrip(s,s.progress.trip.id,{},at,()=>.99);
    if(i===0)identifyMaterial(s,75);
  }
  // A one-ingredient entry dish is fully known once identification reveals its direction.
  assert.ok(regionalRecipeInfo(s,RECIPE).met);
  return E.normalizeSave(s,at);
}
function start(s,random=()=>.9,at=s.batch?.ends+4000||s.progress.trip.endAt){
  if(!(s.ingredients[75]>0))E.buyIngredient(s,75);
  prepareRegionalRecipe(s,RECIPE);return E.startBatch(s,1,at,random,()=>.5);
}
function mature(s,at=s.batch.ends+1,random=()=>{throw Error('new batch rerolled after departure');}){
  E.updateBatch(s,at,random);E.updateBatch(s,at+2000,random);E.updateBatch(s,at+2900,random);
  assert.ok(s.batch.eggs.every(e=>e.collected||e.status==='ready'));
  return at+2900;
}
function finish(s,at=s.batch.ends+1){
  const readyAt=mature(s,at);
  for(let i=0;i<24;i++)assert.equal(E.collect(s,i,readyAt),true);
  return readyAt;
}

test('real startBatch handles 25% boundaries with one target plus23 legacy companions',()=>{
  for(const [roll,hit]of [[0,true],[.25-Number.EPSILON,true],[.25,false],[1-Number.EPSILON,false]]){
    const s=readyState(),beforeCP=s.cp,beforeMaterial=s.ingredients[75];
    const b=start(s,()=>roll);
    assert.equal(b.plan.roll,roll);assert.equal(b.plan.targetScheduled,hit);
    assert.equal(b.eggs.filter(e=>e.id===128).length,hit?1:0);
    assert.equal(b.eggs.filter(e=>e.id<128).length,hit?23:24);
    assert.equal(s.ingredients[75],beforeMaterial-1);assert.equal(s.cp,beforeCP-E.tool(1).lv_0_cook_cp);
    assert.deepEqual(b.plan.initialIds,b.eggs.map(e=>e.id));assert.equal(b.rules.version,3);
    assert.deepEqual(E.normalizeSave(s,b.started),s);
  }
});

test('three complete failures schedule fourth batch and collecting actual target clears protection',()=>{
  const s=readyState();
  for(let i=0;i<3;i++){
    const b=start(s,()=>.9);assert.equal(b.plan.targetScheduled,false);finish(s);
    assert.deepEqual(s.expansion.trial[RECIPE],{failedFullBatches:i+1,owed:false,attemptSeq:i+1});
    assert.equal(E.collect(s,23,b.ends+3001),false,'last-slot double click cannot count another failed batch');
    assert.equal(s.expansion.trial[RECIPE].failedFullBatches,i+1);
    assert.deepEqual(E.normalizeSave(s,b.ends+3001),s);
  }
  const fourth=start(s,()=>.999);assert.equal(fourth.plan.targetScheduled,true);assert.equal(fourth.plan.roll,null);assert.equal(s.expansion.trial[RECIPE].owed,true);
  finish(s);assert.equal(s.total[KEY],1);assert.equal(s.farm[KEY],1);
  assert.deepEqual(s.expansion.trial[RECIPE],{failedFullBatches:0,owed:false,attemptSeq:4});
  assert.equal(s.expansion.regions.materialUse[75],true);
});

test('a partial batch never increments failure or marks material used',()=>{
  const s=readyState();start(s,()=>.9);const at=mature(s);
  for(let i=0;i<23;i++)E.collect(s,i,at);
  assert.equal(s.expansion.trial[RECIPE].failedFullBatches,0);assert.equal(s.batch.plan.finished,false);assert.equal(s.expansion.regions.materialUse?.[75],undefined);
  E.collect(s,23,at);assert.equal(s.expansion.trial[RECIPE].failedFullBatches,1);assert.equal(s.batch.plan.finished,true);assert.equal(s.expansion.regions.materialUse[75],true);
});

test('overcooked target is not discovery and owed protection survives full collection and reload',()=>{
  const s=readyState();start(s,()=>0);assert.equal(s.batch.eggs[0].id,128);
  finish(s,Math.max(...s.batch.eggs.map(e=>e.blackAt))+1);
  assert.equal(s.total[KEY],undefined);assert.equal(s.batch.eggs[0].id,2);
  assert.equal(s.expansion.trial[RECIPE].owed,true);assert.equal(s.expansion.trial[RECIPE].failedFullBatches,1);
  const restored=parseBackup(makeBackup(s,s.batch.ends),s.batch.ends);
  const next=start(restored,()=>.999);assert.equal(next.plan.targetScheduled,true);assert.equal(next.plan.roll,null);
  finish(restored);assert.equal(restored.total[KEY],1);assert.equal(restored.expansion.trial[RECIPE].owed,false);
});

test('dirty-kitchen disease uses frozen tickets and retains the unpaid target',()=>{
  const s=readyState(),at=NOW+37*3600000;
  E.resume(s,at);assert.equal(s.dirty,true);start(s,()=>0,at);finish(s);
  assert.equal(s.batch.eggs[0].id,1);assert.equal(s.total[KEY],undefined);assert.equal(s.expansion.trial[RECIPE].owed,true);
  assert.equal(s.expansion.trial[RECIPE].failedFullBatches,1);
});

test('abandoning a paid scheduled batch retains owed without increasing full-batch failure',()=>{
  const s=readyState();start(s,()=>0);const before=structuredClone(s.expansion.trial[RECIPE]),paid=s.cp;
  // The real overwrite UI explicitly clears the old batch on its command draft
  // before startBatch. No settlement is synthesized for this abandoned batch.
  const at=s.batch.started+1;s.batch=null;start(s,()=>.999,at);
  assert.equal(before.owed,true);assert.equal(s.expansion.trial[RECIPE].failedFullBatches,0);assert.equal(s.expansion.trial[RECIPE].owed,true);assert.equal(s.batch.plan.targetScheduled,true);
  assert.ok(s.cp<paid,'replacement is a new paid batch, never a free reroll');
});

test('permanent actual discovery guarantees repeat after selling the final copy',()=>{
  const s=readyState();start(s,()=>0);finish(s);const at=s.batch.ends+3001;
  assert.equal(s.farm[KEY],1);E.sell(s,{[KEY]:1},{},at);assert.equal(s.farm[KEY],0);
  const b=start(s,()=>.999,at+1);assert.equal(b.plan.mode,'regional-repeat');assert.equal(b.plan.roll,null);assert.equal(b.plan.targetScheduled,true);assert.equal(b.eggs.filter(e=>e.id===128).length,1);
  finish(s);assert.equal(s.total[KEY],2);
});

test('extra/duplicate/wrong egg/wrong tool/missing material conflict rejects before any debit',()=>{
  for(const mutate of [
    s=>{s.ingredients[0]=1;s.selected=[75,0];},s=>{s.selected=[75,75];},s=>{s.duck=true;s.egg=1;},s=>{s.ingredients[75]=0;},
    s=>{s.events.seasonalRecipe='0:120';},s=>{s.ingredients[68]=1;s.selected=[75,68];},
  ]){
    const s=readyState();prepareRegionalRecipe(s,RECIPE);mutate(s);const before=structuredClone(s);
    assert.throws(()=>E.startBatch(s,1,s.progress.trip.endAt,()=>.1,()=>.5));assert.deepEqual(s,before);
  }
  const s=readyState();prepareRegionalRecipe(s,RECIPE);const before=structuredClone(s);assert.throws(()=>E.startBatch(s,0,s.progress.trip.endAt,()=>.1,()=>.5));assert.deepEqual(s,before);
});

test('ordinary cooking with new material stays entirely in the original identity pool',()=>{
  const s=readyState();s.selected=[75];delete s.expansion.prepareMode;
  const b=E.startBatch(s,1,s.progress.trip.endAt,()=>0,()=>.5);
  assert.equal(b.plan.mode,'legacy');assert.equal(b.plan.recipeId,null);assert.equal(b.plan.targetScheduled,false);assert.ok(b.eggs.every(e=>e.id<128));assert.deepEqual(s.expansion.trial,{});
  assert.equal(s.expansion.regions.materialUse?.[75],undefined);
  finish(s);assert.equal(s.total[KEY],undefined);assert.deepEqual(s.expansion.trial,{});
  assert.equal(s.expansion.regions.materialUse?.[75],true,'a fully collected ordinary batch still records actual new-material use');
});

test('reload replays frozen illness/overcook/harvest tickets without requesting randomness',()=>{
  const s=readyState();start(s,()=>.14);
  const restored=parseBackup(makeBackup(s,s.batch.started),s.batch.started),at=Math.max(...s.batch.eggs.map(e=>e.blackAt))+1;
  assert.deepEqual(restored.batch,s.batch);finish(s,at);finish(restored,at);
  assert.deepEqual(restored,s);
});

test('visual random variation cannot change production, deadlines, mutation or reward tickets',()=>{
  const a=readyState(),b=structuredClone(a);prepareRegionalRecipe(a,RECIPE);prepareRegionalRecipe(b,RECIPE);
  E.startBatch(a,1,a.progress.trip.endAt,undefined,()=>.1);E.startBatch(b,1,b.progress.trip.endAt,undefined,()=>.9);
  const economic=batch=>({plan:batch.plan,rules:batch.rules,eggs:batch.eggs.map(({x,y,flipped,...economic})=>economic)});
  assert.deepEqual(economic(a.batch),economic(b.batch));assert.notDeepEqual(a.batch.eggs.map(e=>[e.x,e.y]),b.batch.eggs.map(e=>[e.x,e.y]));
});

test('invalid economic RNG values cannot debit a regional batch',()=>{
  for(const value of [NaN,1,-.1,Infinity]){
    const s=readyState();prepareRegionalRecipe(s,RECIPE);const before=structuredClone(s);
    assert.throws(()=>E.startBatch(s,1,s.progress.trip.endAt,()=>value,()=>.5));assert.deepEqual(s,before);
  }
});

test('current save rejects impossible legacy/new companion tickets and inconsistent scheduled rolls',()=>{
  const s=readyState();start(s,()=>.9);
  for(const mutate of [
    state=>{state.batch.plan.initialIds[1]=129;},
    state=>{state.batch.plan.roll=null;},
    state=>{state.batch.ingredients=[0];},
    state=>{state.batch.level=-1;},
  ]){const invalid=structuredClone(s);mutate(invalid);assert.throws(()=>E.normalizeSave(invalid,s.batch.started));}
  const ordinary=readyState();ordinary.selected=[];E.startBatch(ordinary,1,ordinary.progress.trip.endAt,()=>.5,()=>.5);ordinary.batch.plan.initialIds[0]=128;
  assert.throws(()=>E.normalizeSave(ordinary,ordinary.batch.started));
});
