import test from 'node:test';
import assert from 'node:assert/strict';
import {recipeFixture} from '../tools/simulate-economy-pairs.mjs';
import {REGIONAL} from '../web/content-registry.js';
import {sampleBatchPlan} from '../web/batch-plan.js';
import {recipeChance,chanceInBatch} from '../web/hatch-probability.js';
import {EXTRA_REGIONS} from '../web/extra-regions.js';
import {depart,claimTrip,recall,explorationInfo} from '../web/exploration.js';
import {normalizeSave,advanceWorld} from '../web/engine.js';
import {regionCard} from '../web/journey-model.js';
import {regionPartners} from '../web/clue-regions.js';
import {loopGuide,discoveryCombinations} from '../web/loop-guide.js';
const NOW=1800000000000;
const rng=initial=>{let n=initial;return()=>((n=(Math.imul(n,1664525)+1013904223)>>>0)/4294967296);};
for(const chance of [.3,.2,.1])test(`real regional sampling: ${chance} per egg, multiple targets and first/repeat parity`,()=>{
 const s=recipeFixture(),r=REGIONAL.recipes.find(r=>recipeChance(r)===chance),id=Number(r.key.split(':')[1]);s.egg=r.egg;
 const plan={mode:'regional-trial',egg:r.egg,toolId:r.toolId,key:r.key,recipeId:r.id,materials:r.ingredients.map(x=>x.id),legacyMaterials:r.ingredients.filter(x=>x.id<75).map(x=>x.id),chance};
 let hits=0,misses=0,multiple=0;const random=rng(100+chance*100),pots=3000;
 for(let i=0;i<pots;i++){const a=sampleBatchPlan(s,plan,NOW,random);const n=a.result.filter(x=>x===id).length;hits+=n;misses+=n===0;multiple+=n>1;}
 assert.ok(Math.abs(hits/(pots*24)-chance)<.008);assert.ok(Math.abs(misses/pots-(1-chanceInBatch(chance)))<.02);assert.ok(multiple>pots*.5);
 assert.deepEqual(sampleBatchPlan(s,plan,NOW,rng(71)).result,sampleBatchPlan(s,{...plan,mode:'regional-repeat'},NOW,rng(71)).result);
});
for(const area of EXTRA_REGIONS)test(`${area.name}: two materials, several clues, saved trip and recall`,()=>{
 const s=recipeFixture();s.farm['0:0']=5;s.total['0:0']=4000;s.toolLevels.fill(2);s.kitchenLevel=3;
 assert.equal(regionCard(s,area.id,NOW).materials.total,2);assert.ok(regionPartners(area.id).length>=3);
 depart(s,{routeId:area.route,members:['0:0']},NOW,rng(5));assert.ok(s.progress.trip.remaining.every(id=>area.materials.includes(id)));
 let restored=normalizeSave(s,NOW);assert.deepEqual(restored.progress.trip,s.progress.trip);
 advanceWorld(restored,restored.progress.trip.endAt,()=>.99);claimTrip(restored,restored.progress.trip.id,{},restored.progress.trip.endAt,()=>.99);
 assert.equal(restored.progress.trip.status,'settled');assert.deepEqual(normalizeSave(restored,restored.progress.trip.endAt).ingredients,restored.ingredients);
 const again=recipeFixture();again.farm['0:0']=5;again.total['0:0']=4000;again.toolLevels.fill(2);again.kitchenLevel=3;
 depart(again,{routeId:area.route,members:['0:0']},NOW,rng(4));recall(again,again.progress.trip.id,NOW+1,()=>.99);assert.equal(again.progress.trip.status,'recalled');assert.deepEqual(again.ingredients,{});
});
test('first local partner suggests a real menu with an owned ordinary partner',()=>{
 const s=recipeFixture();s.farm={'0:128':10,'0:3':10};s.total['0:128']=10;s.total['0:3']=10;
 const menus=discoveryCombinations(s,['0:128']);assert.ok(menus.some(m=>m.complete&&m.bonus>0));
 s.expansion.regions.introSpecimenDone=[];assert.equal(loopGuide(s).target,'visitor');s.events.loopVisitorMet=true;assert.equal(loopGuide(s).target,'journey');
});

test('the first-loop guide advances after a complete menu and completed order',()=>{
 const s=recipeFixture();s.total['0:128']=7;s.expansion.facts.menuWitnesses={};s.expansion.facts.orderTemplateCounts={};
 assert.equal(loopGuide(s).id,'combination');
 s.expansion.facts.menuWitnesses.MN1={completeCount:1};assert.equal(loopGuide(s).id,'orders');
 s.expansion.facts.orderTemplateCounts.O01=1;assert.equal(loopGuide(s),null);
});

test('new destination cannot offer a departure before either ingredient is available',()=>{
 const s=recipeFixture();s.total=Object.fromEntries(Array.from({length:24},(_,i)=>['0:'+i,10]));s.farm={'0:0':3};s.toolLevels.fill(-1);s.toolLevels[0]=0;s.kitchenLevel=1;
 assert.equal(explorationInfo(s,'mushroom',[],NOW).unlocked,false);assert.equal(regionCard(s,'H',NOW).met,false);
 assert.throws(()=>depart(s,{routeId:'mushroom',members:['0:0']},NOW,()=>.99),/路线尚未开放/);
});
