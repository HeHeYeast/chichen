// Work C: the whole valley (12 species, 2 materials, 6 cards, ALT-V) on the
// same data-driven chain as Work B, and its exclusivity with the old pools.
import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {earnedSources,skillPoints,SPECIES_SOURCE_LIMIT,MAX_SKILL_POINTS} from '../web/progression.js';
import {departRegional,regionalTripInfo,settleRegionalTrip} from '../web/regional-exploration.js';
import {claimTrip} from '../web/exploration.js';
import {identifyMaterial,regionalRecipeInfo,prepareRegionalRecipe,regionalAlternativeInfo,prepareLocalAlternative} from '../web/regional-methods.js';
import {buildBatchPlan,sampleBatchPlan} from '../web/batch-plan.js';
import {originalRecipes} from '../web/recipes.js';
import {recipeStateAt} from '../web/holiday-calendar.js';
import {prepareBusiness} from '../web/business.js';
import {cookingCandidates} from '../web/candidate-query.js';
import {REGIONAL} from '../web/content-registry.js';
import {prepareGiftRecipe} from '../web/shrine.js';

const NOW=1800000000000,H=3600000;
const OLD=['0:0','0:3','0:4','0:8','0:17','0:18','0:114','0:115','0:116','1:0','1:3','1:6'];
// Mid-game boundary fixture: kitchen Lv.2, duck, pan/boil/oven/keep-warm/steamer
// bought; only legacy facts. Valley facts are then produced by real commands.
function mid(){
  const s=E.freshState(NOW,20260923);
  s.kitchenLevel=1;s.duck=true;s.toolLevels=[0,0,1,-1,0,-1,-1,-1,0];
  s.total=Object.fromEntries(OLD.map((k,i)=>[k,i?20:200]));s.farm=Object.fromEntries(OLD.map(k=>[k,5]));
  for(let i=20;i<32;i++){s.total['0:'+i]=1;}
  s.progress.sources=earnedSources(s);s.ingredients={};
  return s;
}
function trip(s,options,at=s.progress.trip?.endAt??NOW){
  departRegional(s,{regionId:'V',members:['0:0'],...options},at);
  const t=s.progress.trip;E.advanceWorld(s,t.endAt);claimTrip(s,t.id,{discard:true},t.endAt,()=>.99);
  return t.regional.result;
}
function forceMiss(s){for(const c of s.progress.trip.regional.candidates)c.roll=.999;}
function valley(){
  const s=mid();
  assert.equal(trip(s,{placeId:'V:0',focus:'specimen'}).cardId,'V-S1','first complete trip guarantees the executable entry specimen');
  identifyMaterial(s,75);
  return s;
}

test('second specimen, lore and event cards follow compiled gates and teams, not a hard-coded list',()=>{
  const s=valley();
  const lore=regionalTripInfo(s,{regionId:'V',placeId:'V:1',focus:'lore',members:['0:18']},NOW).candidates.map(c=>c.cardId);
  assert.deepEqual(lore,[],'V-N2 and V-E2 require identified malt');
  const specimen=regionalTripInfo(s,{regionId:'V',placeId:'V:1',focus:'specimen',members:['0:0']},NOW).candidates.map(c=>c.cardId);
  assert.deepEqual(specimen,['V-S2'],'kitchen Lv.2, oven Lv.1 and flour supply admit the malt specimen');
  s.toolLevels[4]=-1;assert.deepEqual(regionalTripInfo(s,{regionId:'V',placeId:'V:1',focus:'specimen',members:['0:0']},NOW).candidates,[],'no oven, no malt specimen');s.toolLevels[4]=0;
  s.expansion.discovery.cards['V-S2']=++s.meta.factSeq;identifyMaterial(s,76);
  const at=regionalTripInfo(s,{regionId:'V',placeId:'V:1',focus:'lore',members:['0:0']},NOW).candidates.map(c=>c.cardId);
  assert.deepEqual(at,['V-N2'],'grain event needs a grain companion');
  const team=regionalTripInfo(s,{regionId:'V',placeId:'V:1',focus:'lore',members:['0:18']},NOW);
  assert.deepEqual(team.candidates.map(c=>c.cardId).sort(),['V-E2','V-N2']);
  assert.equal(team.candidates.find(c=>c.cardId==='V-E2').chance,(25+2*team.companions[0].F+5)/100,'trait bonus applies once');
  const leaf=regionalTripInfo(s,{regionId:'V',placeId:'V:0',focus:'lore',members:['0:17']},NOW);
  assert.deepEqual(leaf.candidates.map(c=>c.cardId),['V-N1'],'leaf without a yard companion does not qualify for V-E1');
  const pair=regionalTripInfo(s,{regionId:'V',placeId:'V:0',focus:'lore',members:['0:17','0:0']},NOW);
  assert.deepEqual(pair.candidates.map(c=>c.cardId).sort(),['V-E1','V-N1']);
});

test('the intro guarantee picks the executable specimen and never a card the kitchen cannot try',()=>{
  const s=mid();s.toolLevels[1]=-1;
  const info=regionalTripInfo(s,{regionId:'V',placeId:'V:0',focus:'materials',members:['0:0']},NOW);
  assert.equal(info.introCardId,'V-S2','without a pan the malt specimen is the only executable entry');
  assert.equal(info.introMaterial,76);
  s.toolLevels[4]=-1;
  assert.equal(regionalTripInfo(s,{regionId:'V',placeId:'V:0',focus:'materials',members:['0:0']},NOW).firstSpecimen,false);
});

test('4-trip protection counts region+focus across both places and freezes on an empty pool',()=>{
  const s=valley();s.expansion.discovery.cards['V-S2']=++s.meta.factSeq;identifyMaterial(s,76);
  const places=['V:0','V:1','V:0'];let at=s.progress.trip.endAt;
  for(const [i,placeId] of places.entries()){
    departRegional(s,{regionId:'V',placeId,focus:'lore',members:['0:0']},at);forceMiss(s);
    const t=s.progress.trip;E.advanceWorld(s,t.endAt);claimTrip(s,t.id,{discard:true},t.endAt,()=>.99);at=t.endAt;
    assert.equal(s.expansion.cardProtection.V.lore,i+1,'switching place does not reset the lore counter');
  }
  departRegional(s,{regionId:'V',placeId:'V:1',focus:'lore',members:['0:0']},at);forceMiss(s);
  const t=s.progress.trip;assert.equal(t.regional.failedBefore,3);E.advanceWorld(s,t.endAt);
  assert.equal(t.regional.result.cardId,'V-N2','fourth eligible trip is guaranteed');assert.equal(s.expansion.cardProtection.V.lore,0);
  claimTrip(s,t.id,{discard:true},t.endAt,()=>.99);
  // Only V-N1 remains at V:0 (no leaf+yard team): after it the place pool empties and freezes.
  s.expansion.discovery.cards['V-N1']=++s.meta.factSeq;s.expansion.cardProtection.V.lore=2;
  departRegional(s,{regionId:'V',placeId:'V:0',focus:'lore',members:['0:0']},t.endAt);
  assert.deepEqual(s.progress.trip.regional.candidates,[]);E.advanceWorld(s,s.progress.trip.endAt);
  assert.equal(s.expansion.cardProtection.V.lore,2,'empty pool freezes');
});

test('old-material valley species open with the first specimen, ornamentals with V-N2, late ones at kitchen Lv.4',()=>{
  const s=mid();
  assert.ok(!s.expansion.methods.directions.includes('REC-V-D5'));
  trip(s,{placeId:'V:0',focus:'specimen'});
  assert.ok(s.expansion.methods.directions.includes('REC-V-C5')&&s.expansion.methods.directions.includes('REC-V-D5'),'first specimen registers old-material directions without identification');
  assert.ok(regionalRecipeInfo(s,'REC-V-D5').missing.some(x=>/补全/.test(x)),'direction is not the full method');
  identifyMaterial(s,75);s.expansion.discovery.cards['V-S2']=++s.meta.factSeq;identifyMaterial(s,76);
  for(const id of ['REC-V-C6','REC-V-D6']){assert.ok(!s.expansion.methods.directions.includes(id));assert.ok(regionalRecipeInfo(s,id).missing.some(x=>x.includes('V-N2')));}
  s.expansion.discovery.cards['V-N2']=++s.meta.factSeq;
  departRegional(s,{regionId:'V',placeId:'V:0',focus:'materials',members:['0:0']},s.progress.trip.endAt);E.advanceWorld(s,s.progress.trip.endAt);
  for(const id of ['REC-V-C6','REC-V-D6'])assert.ok(s.expansion.methods.directions.includes(id),'lore card opens ornamental directions');
  for(const id of ['REC-V-C4','REC-V-D4']){
    s.expansion.methods.full.push(id);s.kitchenLevel=2;
    assert.ok(regionalRecipeInfo(s,id).missing.includes('厨房 Lv.4'),`${id} needs kitchen Lv.4`);
  }
  s.kitchenLevel=3;s.toolLevels=[0,0,1,1,0,1,1,1,1];
  assert.ok(!regionalRecipeInfo(s,'REC-V-C4').missing.includes('厨房 Lv.4'));
  assert.ok(regionalRecipeInfo(s,'REC-V-C4').missing.every(x=>!/Lv/.test(x)),'bread machine and cream supply ready at Lv.4');
});

test('V-C3 steamer trial keeps old weighted dim-sum companions without inserting the old shaomai guarantee',()=>{
  const s=valley();s.expansion.methods.full.push('REC-V-C3');s.ingredients={75:1,9:1};
  prepareRegionalRecipe(s,'REC-V-C3');
  const plan=buildBatchPlan(s,8,NOW);assert.equal(plan.mode,'regional-trial');assert.deepEqual(plan.legacyMaterials,[9]);
  const miss=sampleBatchPlan(s,plan,NOW,()=>0);
  assert.equal(miss.ticket.targetScheduled,true,'roll 0 schedules the target');
  assert.equal(miss.result.filter(id=>id===130).length,1);
  assert.deepEqual([...new Set(miss.result.filter(id=>id!==130))],[114],'weighted companions only: no guaranteed 115 insert');
  const legacy=structuredClone(s);delete legacy.expansion.prepareMode;legacy.selected=[9];
  const old=sampleBatchPlan(legacy,buildBatchPlan(legacy,8,NOW),NOW,()=>0);
  assert.ok(old.result.includes(115),'ordinary steamer mode keeps its original per-recipe guarantee');
  const preview=cookingCandidates(s,8,NOW);
  assert.ok(preview.candidates.every(c=>c.key==='0:130'||!c.guaranteed),'preview shows no old guarantee in regional mode');
});

test('ALT-V replaces malt by flour in the old matcher, keeps original probability and inserts nothing',()=>{
  const s=valley();s.expansion.discovery.cards['V-S2']=++s.meta.factSeq;identifyMaterial(s,76);
  assert.equal(regionalAlternativeInfo(s,'ALT-V').met,false);
  s.expansion.discovery.cards['V-E2']=++s.meta.factSeq;
  departRegional(s,{regionId:'V',placeId:'V:0',focus:'materials',members:['0:0']},s.progress.trip.endAt);E.advanceWorld(s,s.progress.trip.endAt);
  const info=regionalAlternativeInfo(s,'ALT-V');assert.equal(info.met,true);assert.deepEqual(info.legacyMaterials,[9]);
  s.ingredients={76:1};prepareLocalAlternative(s,'ALT-V');
  assert.deepEqual(s.expansion.prepareMode,{kind:'local-alternative',recipeId:'ALT-V'});
  const plan=buildBatchPlan(s,4,NOW);assert.equal(plan.mode,'local-alternative');assert.equal(plan.guaranteed,false);
  let i=0;const seq=()=>[.1,.7,.3,.9,.5][i++%5];
  const sampled=sampleBatchPlan(s,plan,NOW,seq);i=0;
  assert.deepEqual(sampled.result,originalRecipes(recipeStateAt(s,NOW),0,4,[9],NOW,seq),'identical to the old oven+flour draw');
  assert.equal(sampled.ticket.targetScheduled,false);
  s.selected=[76,9];assert.throws(()=>buildBatchPlan(s,4,NOW),/精确/,'extra ingredients are rejected');
  s.selected=[76];s.events.seasonalRecipe='0:120';assert.throws(()=>buildBatchPlan(s,4,NOW),/一种调理模式/);
  delete s.events.seasonalRecipe;const b=E.startBatch(s,4,NOW,()=>.5,()=>.5);
  assert.equal(b.plan.mode,'local-alternative');assert.deepEqual(E.normalizeSave(s,NOW).batch.plan,b.plan,'validator accepts the saved alternative');
  const alt=structuredClone(E.normalizeSave(s,NOW));alt.batch.plan.targetScheduled=true;assert.throws(()=>E.normalizeSave(alt,NOW));
});

test('gift and seasonal preparations clear a valley mode instead of stacking guarantees',()=>{
  const s=valley();s.expansion.methods.full.push('REC-V-C1');s.ingredients={75:1};
  prepareRegionalRecipe(s,'REC-V-C1');
  s.events.gift_tool_2_68_character_id=90;s.ingredients[68]=1;
  try{prepareGiftRecipe(s,68);}catch{}
  assert.ok(!s.expansion.prepareMode||s.expansion.prepareMode.kind!=='regional'||!s.selected.includes(68),'gift medium never joins a regional plan');
  s.selected=[75,68];s.expansion.prepareMode={kind:'regional',recipeId:'REC-V-C1'};
  assert.throws(()=>buildBatchPlan(s,1,NOW));
});

test('ornamental valley species may be companions but never business stock',()=>{
  const s=valley();s.farm['0:133']=3;s.total['0:133']=3;s.farm['0:0']=30;
  const info=regionalTripInfo(s,{regionId:'V',placeId:'V:0',focus:'lore',members:['0:133']},NOW);
  assert.equal(info.companions[0].key,'0:133');
  s.progress.orders['first-sale']={accepted:true,completed:true,choice:'0:0',delivered:1};
  assert.throws(()=>prepareBusiness(s,{menuId:'MN1',stock:{'0:133':1}}),/可食用/);
});

test('skill point sources extend to 240 discoveries and 64 points without recomputing old sources',()=>{
  assert.equal(SPECIES_SOURCE_LIMIT,240);assert.equal(MAX_SKILL_POINTS,64);
  const s=E.freshState(NOW,1);s.kitchenLevel=3;
  for(let i=0;i<152;i++)s.total['0:'+i]=i?1:5000;for(let i=0;i<89;i++)s.total['1:'+i]=1;
  const sources=earnedSources(s);assert.equal(Object.keys(sources).filter(k=>k.startsWith('species:')).length,48);
  s.progress.sources=sources;assert.equal(skillPoints(s).earned,64);
  E.normalizeSave(s,NOW);
});

test('all twelve valley recipes are reachable from legal prerequisites on one save',()=>{
  const s=valley();s.expansion.discovery.cards['V-S2']=++s.meta.factSeq;identifyMaterial(s,76);s.expansion.discovery.cards['V-N2']=++s.meta.factSeq;
  departRegional(s,{regionId:'V',placeId:'V:0',focus:'materials',members:['0:0']},s.progress.trip.endAt);E.advanceWorld(s,s.progress.trip.endAt);
  s.kitchenLevel=3;s.toolLevels=[0,2,2,2,2,2,2,2,2];s.progress.sources=earnedSources(s);
  const ids=REGIONAL.recipes.filter(r=>r.id.startsWith('REC-V-')).map(r=>r.id);
  for(const id of ids){assert.ok(s.expansion.methods.directions.includes(id),id+' direction');s.expansion.methods.full.push(id);assert.equal(regionalRecipeInfo(s,id).met,true,id+': '+regionalRecipeInfo(s,id).missing.join(','));}
  assert.equal(ids.length,12);
});

test('a preparation mode is consumed by its batch; the next ordinary batch is not blocked',()=>{
  const s=valley();s.expansion.methods.full.push('REC-V-C1');s.ingredients={75:1,0:1};
  prepareRegionalRecipe(s,'REC-V-C1');const b=E.startBatch(s,1,NOW,()=>.9,()=>.5);assert.equal(b.plan.mode,'regional-trial');
  assert.equal(s.expansion.prepareMode,undefined);
  for(let i=0;i<24;i++){b.eggs[i].status='ready';E.collect(s,i,b.ends+5000);}
  s.selected=[0];const next=E.startBatch(s,1,b.ends+6000,()=>.5,()=>.5);assert.equal(next.plan.mode,'legacy');
});

test('the kitchen confirmation explains the owning guarantee mode from the same plan',async()=>{
  const {batchModeView}=await import('../web/batch-mode-view.js');
  const s=valley();s.expansion.methods.full.push('REC-V-C1');s.ingredients={75:1};prepareRegionalRecipe(s,'REC-V-C1');
  const trial=batchModeView(s,cookingCandidates(s,1,NOW).plan);assert.equal(trial.mode,'regional-trial');assert.match(trial.lines[0],/25%/);assert.match(trial.title,/C129/);
  s.expansion.trial['REC-V-C1']={failedFullBatches:3,owed:false,attemptSeq:3};assert.match(batchModeView(s,cookingCandidates(s,1,NOW).plan).lines[0],/连续3批/);
  s.total['0:128']=1;const repeat=batchModeView(s,cookingCandidates(s,1,NOW).plan);assert.equal(repeat.mode,'regional-repeat');assert.match(repeat.title,/荠菜煎饼鸡/);
  delete s.expansion.prepareMode;s.selected=[];assert.equal(batchModeView(s,cookingCandidates(s,1,NOW).plan),null);
});
