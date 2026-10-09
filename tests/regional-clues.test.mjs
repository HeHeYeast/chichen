// 2026-10-07 loop batch 4: the regional partners in the same 线索册 — one investigation for everyone (regional-clues.js).
import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {earnedSources,learnSkill,syncProgress} from '../web/progression.js';
import {claimTrip} from '../web/exploration.js';
import {departRegional,regionalTripInfo,settleRegionalTrip} from '../web/regional-exploration.js';
import {identifyMaterial,regionalRecipeInfo,prepareRegionalRecipe} from '../web/regional-methods.js';
import {trackPartner,nextClueLayer,clueCandidates,observationInfo,studyRecipe,factId,trackedFound} from '../web/knowledge.js';
import {clueRow,clueBookModel,CLUE_STEPS,CLUE_FILTERS} from '../web/clue-book.js';
import {regionalGate,regionalLevels,regionalTrial,regionalRow,regionalRows,isRegionalKey} from '../web/regional-clues.js';
import {nextBatchGoals} from '../web/next-batch-goals.js';
import {trackedTrail,regionCard} from '../web/journey-model.js';
import {orderIntelPlan,grantOrderIntel,orderIntelDue,ORDER_INTEL,orderRegion} from '../web/order-intel.js';
import {regionPartners} from '../web/clue-regions.js';
import {materialCapacity} from '../web/material-capacity.js';
import {recipeId} from '../web/recipe-book.js';
import {REGIONAL} from '../web/content-registry.js';
import {ROUTES} from '../web/exploration.js';

const NOW=1800000000000;
const OLD=['0:0','0:3','0:4','0:6','0:8','0:10','0:12','0:16','0:17','0:18','0:21','0:43','0:114','0:115','0:116','0:117','1:0','1:3','1:5','1:6','1:7','1:8','1:9','1:10','1:12','1:22'];
// Kitchen Lv.3, duck eggs, most cookware; the valley open and its first trip made (no intro specimen in the way).
function valley(skills=[]){
  const s=E.freshState(NOW,99);s.kitchenLevel=2;s.duck=true;s.cp=90000;s.toolLevels=[0,1,1,1,1,1,1,-1,1];
  s.total=Object.fromEntries(OLD.map((k,i)=>[k,i?20:900]));for(let i=22;i<40;i++)s.total['0:'+i]??=1;
  s.farm=Object.fromEntries(OLD.map(k=>[k,9]));s.progress.sources=earnedSources(s);s.ingredients={};
  s.expansion.regions.introSpecimenDone=['V'];s.expansion.regions.opened=['V'];syncProgress(s);for(const id of skills)learnSkill(s,id);
  return E.normalizeSave(s,NOW);
}
const valid=s=>{E.validateSaveSchema(structuredClone(E.normalizeSave(s,s.progress.logicalAt||NOW)),s.progress.logicalAt||NOW);return true;};
// a whole trip: leave, come home at its end (rolls as frozen, or forced to miss), take the basket
function trip(s,options,{miss=false}={}){
  const at=Math.max(NOW,s.progress.trip?.endAt??0)+1;departRegional(s,{regionId:'V',members:['0:0'],...options},at);const t=s.progress.trip;
  if(miss)for(const c of t.regional.candidates)c.roll=.999;
  t.status='returned';t.returnedAt=t.endAt;settleRegionalTrip(s,t);claimTrip(s,t.id,{discard:true},t.endAt,()=>.99);return t;
}

test('every regional partner joins the 线索册 with its region as its main region; nothing about it shows before its region does',()=>{
  const s=valley(),m=clueBookModel(s,0,NOW),regional=m.rows.filter(r=>r.regional);
  assert.equal(regional.length,regionalRows(0).length,'all 24 chicken partners of the regions are listed');
  assert.ok(regional.every(r=>r.region&&r.region.id===REGIONAL.species.find(c=>c.key===r.key).region));
  for(const r of regional)assert.ok(regionPartners(r.region.id).includes(r.key),`${r.code} counts on its region card`);
  // before its 方向 the riddle (which names the material) stays hidden and the next step is its region's
  const c2=clueRow(s,'0:129',NOW);assert.equal(c2.riddle,'');assert.equal(c2.gate.kind,'specimen');assert.equal(c2.status,'scout');assert.equal(c2.scoutFind.trip.cardId,'V-S2');
  assert.equal(c2.progress,0);assert.equal(c2.toolId,null);
  // the bay is not open: its partners wait on it
  const bay=clueRow(s,'0:146',NOW);assert.equal(bay.status,'wait');assert.match(bay.need[0],/风湾开放还需/);
  assert.equal(regionCard(s,'V',NOW).clues.total,regionPartners('V').filter(k=>k.startsWith('0:')||s.duck).length);
});

test('a regional partner from unknown to met: track → its specimen is sure → 辨认 → trips read it up to the complete method → 下一锅 tries it',()=>{
  const s=valley();trackPartner(s,'0:129');assert.ok(valid(s),'a tracked regional partner is a valid save');
  // the map and the trip plan go where its specimen is, and the tracked partner's find is sure to come home
  const trail=trackedTrail(s,NOW);assert.equal(trail.region,'V');assert.equal(trail.find.trip.cardId,'V-S2');assert.equal(trail.scout,true);
  const info=regionalTripInfo(s,{regionId:'V',placeId:'V:1',focus:'specimen',members:['0:0']},NOW);assert.equal(info.sureCardId,'V-S2');
  const t1=trip(s,{placeId:'V:1',focus:'specimen'},{miss:true});
  assert.equal(t1.regional.sure,'V-S2');assert.equal(t1.regional.candidates[0].cardId,'V-S2');assert.equal(t1.regional.result.cardId,'V-S2','found even though every roll missed');
  assert.equal(clueRow(s,'0:129',NOW).identify,null,'recognition is automatic on return');assert.ok(valid(s));
  identifyMaterial(s,76);
  let row=clueRow(s,'0:129',NOW);
  assert.deepEqual(regionalLevels(s,'0:129'),[false,true,true,false,false],'the 方向 = cookware and first seasoning');
  assert.equal(row.progress,2);assert.equal(row.toolId,4);assert.equal(row.first,76);assert.ok(row.riddle.length>0,'the riddle shows from the 方向 on');
  assert.equal(nextClueLayer(s,'0:129',NOW).level,1);
  // three tracked trips: silhouette, the second seasoning's group, the complete method
  const seen=[];
  for(let i=0;i<3;i++){const before=s.expansion.methods.full.length;const t=trip(s,{placeId:'V:0',focus:'materials'});seen.push(t.clueResult.level);assert.equal(t.clueResult.key,'0:129');
    if(i<2)assert.equal(s.expansion.methods.full.length,before);}
  assert.deepEqual(seen,[1,4,5]);
  assert.equal(s.expansion.methods.full.includes('REC-V-C2'),true,'the trip wrote the method down, as 研读 would');
  row=clueRow(s,'0:129',NOW);assert.equal(row.held,true);assert.equal(row.progress,CLUE_STEPS);assert.equal(row.status,'ready');
  assert.equal(row.chance,1-.8**24);assert.deepEqual([row.trial.left,row.trial.sure],[null,false]);assert.ok(valid(s));
  // 下一锅: the tracked card leads, in the region's trial mode
  const g=nextBatchGoals(s,NOW),card=g.cards[0];
  assert.equal(card.kind,'track');assert.equal(card.regional,'REC-V-C2');assert.equal(card.outcome.chance,1-.8**24);assert.deepEqual(card.ingredients,[76,9]);
  // Old miss counters never force a new batch to contain the target.
  s.expansion.trial['REC-V-C2']={failedFullBatches:3,owed:false,attemptSeq:3};
  assert.deepEqual([regionalTrial(s,'0:129').sure,regionalTrial(s,'0:129').chance],[false,1-.8**24]);
  assert.equal(nextBatchGoals(s,NOW).cards[0].outcome.chance,1-.8**24);
  for(const id of card.ingredients)E.buyIngredient(s,id,1,{forBatch:true});prepareRegionalRecipe(s,'REC-V-C2');E.startBatch(s,4,NOW);
  assert.equal(s.batch.plan.mode,'regional-trial');assert.equal(s.batch.plan.targetScheduled,true);assert.ok(s.batch.plan.initialIds.includes(129));
  // batch 5: it is in the pot now — collecting is all that is left, so 下一锅 offers no second trial (that would throw the
  // pot away) and the 线索册 says so
  assert.equal(regionalTrial(s,'0:129').inPot,true);assert.equal(clueRow(s,'0:129',NOW).trial.inPot,true);
  const after=nextBatchGoals(s,NOW);assert.ok(!after.cards.some(c=>c.regional==='REC-V-C2'));assert.equal(after.tracked.note,'这锅正在试做它，收取后揭晓');
  // device walkthrough: the same while a trial whose 25% did not come up is cooking — no second trial, no hint of the roll
  s.batch.plan.targetScheduled=false;s.batch.eggs=s.batch.eggs.filter(e=>`${e.egg}:${e.id}`!=='0:129');
  assert.equal(regionalTrial(s,'0:129').inPot,true);
  const miss=nextBatchGoals(s,NOW);assert.ok(!miss.cards.some(c=>c.regional==='REC-V-C2'));assert.equal(miss.tracked.note,'这锅正在试做它，收取后揭晓');
  // collected: the trial is open again
  for(const e of s.batch.eggs)e.collected=true;assert.equal(regionalTrial(s,'0:129').inPot,false);
});

test('the same card for every partner: fields, steps and statuses match; 5/5 only when the recipe is held',()=>{
  const s=valley(['OBS-1']),m=clueBookModel(s,0,NOW),plain=m.rows.find(r=>!r.regional),regional=m.rows.find(r=>r.regional);
  for(const field of Object.keys(plain))if(!['when','guess','narrow','canTry'].includes(field))assert.ok(field in regional,`regional rows carry ${field}`);
  const statuses=new Set(CLUE_FILTERS.map(([id])=>id));
  for(const r of m.rows){assert.ok(statuses.has(r.status),r.code);assert.equal(r.progress===CLUE_STEPS,r.held,r.code);}
  // OBS-1 tells every partner's silhouette and cookware, the regional ones too
  assert.deepEqual(regionalLevels(s,'0:129').slice(0,2),[true,true]);assert.equal(clueRow(s,'0:129',NOW).progress,2);
  // the observation page reads a regional partner like any other
  const o=observationInfo(s,'0:129',NOW);assert.equal(o.regional,true);assert.equal(o.silhouette,true);assert.match(o.details.join(),/下一步/);
});

test('a regional partner\'s complete method comes from its region\'s trips or 研读, never from an order or OBS-5',()=>{
  const s=valley(['OBS-1','OBS-3']);s.expansion.discovery.cards['V-S2']=++s.meta.factSeq;identifyMaterial(s,76);
  assert.deepEqual(regionalLevels(s,'0:129'),[true,true,true,true,false],'OBS-3 tells the second seasoning\'s group once the 方向 is known');
  assert.equal(nextClueLayer(s,'0:129',NOW).level,5,'a trip reads the method');
  assert.equal(nextClueLayer(s,'0:129',NOW,{deep:true}),null,'an order\'s 情报 does not');
  assert.equal(clueCandidates(s,ROUTES.find(r=>r.id==='yard'),NOW,{method:false}).some(c=>c.key==='0:129'),false,'nor OBS-5');
  // 研读 is the region's own (needs the skill and the 方向)
  learnSkill(s,'OBS-4');const before=s.cp,r=clueRow(s,'0:129',NOW);assert.equal(r.canStudy,true);
  studyRecipe(s,'0:129',NOW);assert.equal(s.cp,before-r.studyCost);assert.equal(clueRow(s,'0:129',NOW).held,true);assert.ok(valid(s));
});

test('saves: regional facts, a tracked regional partner and the tracked find ticket validate; wrong ones do not',()=>{
  const s=valley();trackPartner(s,'0:129');
  const row=regionalRow('0:129');s.progress.knowledge.facts.push(factId(row,1),factId(row,4));assert.ok(valid(s));
  const bad=structuredClone(s);bad.progress.knowledge.facts.push('0:129/regional/4/0/76-27//:L4');assert.throws(()=>valid(bad),/线索事实身份/);
  departRegional(s,{regionId:'V',placeId:'V:1',focus:'specimen',members:['0:0']},NOW+1);assert.equal(s.progress.trip.regional.sure,'V-S2');assert.ok(valid(s));
  const moved=structuredClone(s);moved.progress.trip.regional.candidates.reverse();moved.progress.trip.regional.candidates.push(...[]);
  if(moved.progress.trip.regional.candidates[0].cardId!=='V-S2')assert.throws(()=>valid(moved),/追踪发现票据/);
  const foreign=structuredClone(s);foreign.progress.trip.regional.sure='R-S1';assert.throws(()=>valid(foreign),/追踪发现票据/);
  // an old save (no tracked, no regional facts) is untouched
  const old=valley();assert.equal(old.progress.knowledge.tracked,undefined);assert.ok(valid(old));
});

test('订单情报 is configured per order and stays in its region; with nothing new there it sends the region\'s materials',()=>{
  const s=valley();
  // first only / every third / always
  assert.equal(ORDER_INTEL.O01,'first');assert.equal(orderIntelDue(s,'O01'),true);
  s.expansion.orders.templateProgress.O01={completed:1};assert.equal(orderIntelDue(s,'O01'),false,'an everyday order tells its news once');
  s.expansion.orders.templateProgress.O02={completed:1};assert.equal(orderIntelDue(s,'O02'),false);
  s.expansion.orders.templateProgress.O02={completed:3};assert.equal(orderIntelDue(s,'O02'),true,'every third time');
  assert.equal(orderIntelDue(s,'O04'),true);assert.equal(orderIntelPlan(s,'O01','V',NOW,{due:false}).kind,'off');
  // nothing left to tell in the valley: never another region's partner, a valley find or valley materials instead
  for(const key of regionPartners('V'))s.total[key]=1;
  const plan=orderIntelPlan(s,'O02','V',NOW);
  assert.notEqual(plan.kind,'clue','no valley partner has a layer left');
  assert.ok(['place','material','none'].includes(plan.kind));if(plan.kind==='place')assert.equal(plan.cardId[0],'V');
  for(const c of REGIONAL.cards.filter(c=>c.region==='V'))s.expansion.discovery.cards[c.id]??=++s.meta.factSeq;
  const material=orderIntelPlan(s,'O02','V',NOW);assert.equal(material.kind,'material');
  const got=grantOrderIntel(s,'O02','V',NOW);assert.equal(got.kind,'material');assert.equal(s.ingredients[got.materialId],got.quantity);
  assert.equal(orderRegion('O02'),'V');
});

test('开火 buys what the batch is missing even when the bag is full: it goes straight into the pot',()=>{
  const s=valley();const cap=materialCapacity(s);s.ingredients={1:cap};
  assert.throws(()=>E.buyIngredient(s,9,1),/最多可持有/);
  E.buyIngredient(s,9,1,{forBatch:true});s.selected=[9];s.egg=0;E.startBatch(s,4,NOW);
  assert.equal(Object.values(s.ingredients).reduce((a,b)=>a+b,0),cap,'the bag is as full as before');
});
