import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {freshState,normalizeSave,validateSaveSchema} from '../web/engine.js';
import {learnSkill,syncProgress} from '../web/progression.js';
import {nextClueLayer,clueCandidates,clueStillNew,tripClue,trackPartner,clueReach,factId,TRIP_LAYERS} from '../web/knowledge.js';
import {explorationInfo,depart,claimTrip,ROUTES} from '../web/exploration.js';
import {clueRegionOf,clueRegionTable,regionPartners,CLUE_REGION_OVERRIDES,ROUTE_REGION} from '../web/clue-regions.js';
import {clueBookModel,clueRow,GUESS_MAX} from '../web/clue-book.js';
import {nextBatchGoals} from '../web/next-batch-goals.js';
import {tripClueAdvance} from '../web/journey-model.js';
import {RECIPE_CATALOG,REGIONAL_RECIPE_ROWS,recipeId} from '../web/recipe-book.js';
import {REGIONAL} from '../web/content-registry.js';
const NOW=1800000000000;
function player(skills=[]){
  const s=freshState(NOW);s.kitchenLevel=2;s.toolLevels.fill(1);s.toolLevels[8]=-1;s.cp=100000;s.egg=0;
  for(let i=0;i<89;i++){s.total['0:'+i]=600;s.farm['0:'+i]=3;}
  syncProgress(s);for(const id of skills)learnSkill(s,id);
  for(let i=12;i<89;i++){delete s.total['0:'+i];delete s.farm['0:'+i];}
  for(let i=0;i<12;i++)s.total['0:'+i]=600;
  return s;
}
const wood=ROUTES.find(r=>r.id==='wood');
// run one wood trip with a 1-member team and claim it at its end; returns the clue the claim gave
function trip(s,at){depart(s,{routeId:'wood',members:['0:0']},at,()=>0.99);const t=s.progress.trip;return claimTrip(s,t.id,{},t.endAt).clue;}

test('every 线索册 partner has exactly one main clue region; overrides win; C122 follows its honey to 茶坡',()=>{
  const keys=new Set([...RECIPE_CATALOG.filter(r=>!['change','sign','gift'].includes(r.kind)).map(r=>r.key),...REGIONAL_RECIPE_ROWS.map(r=>r.key)]);
  const table=clueRegionTable();
  assert.equal(table.length,keys.size);
  // the 48 regional partners (batch 4) belong to their own region
  for(const r of REGIONAL_RECIPE_ROWS){const x=clueRegionOf(r.key);assert.equal(x.by,'regional');assert.equal(x.region,REGIONAL.species.find(c=>c.key===r.key).region);}
  for(const x of table)assert.ok(['V','R','T','B'].includes(x.region),x.key);
  for(const [key,o] of Object.entries(CLUE_REGION_OVERRIDES)){assert.equal(clueRegionOf(key).region,o.region);assert.equal(clueRegionOf(key).by,'override');}
  assert.ok(Object.keys(CLUE_REGION_OVERRIDES).length<=table.length*.1,'overrides stay a short list');
  assert.deepEqual({region:clueRegionOf('0:121').region,by:clueRegionOf('0:121').by},{region:'T',by:'second'});
  // tea stays on 茶坡 even when its leaves are bought, bread goes with the wheat
  assert.equal(clueRegionOf('1:43').region,'T');assert.equal(clueRegionOf('0:109').region,'V');
});

test('a trip reads the next layer nobody shows yet: skills and studied recipes are skipped, the second-seasoning group only when there is one',()=>{
  const plain=player([]),obs=player(['OBS-1']);
  for(const key of regionPartners('T').slice(0,30)){
    const a=nextClueLayer(plain,key,NOW),b=nextClueLayer(obs,key,NOW);
    if(a)assert.equal(a.level,1,'without skills the silhouette comes first');
    if(b){assert.ok(b.level>=3,`${key}: 风味辨识 already shows the silhouette and cookware`);assert.ok(b.level<=TRIP_LAYERS);
      const c=clueReach(obs,key,NOW);if(b.level===4)assert.ok(c.path.ingredients.length>=2);}
  }
  // the next layer moves on once a fact is read
  const key='0:121',first=nextClueLayer(obs,key,NOW);assert.equal(first.level,3);
  obs.progress.knowledge.facts.push(factId(first.path,3));
  assert.equal(nextClueLayer(obs,key,NOW).level,4);
  obs.progress.knowledge.facts.push(factId(first.path,4));
  assert.equal(nextClueLayer(obs,key,NOW),null,'the exact second seasoning is never a trip clue');
});

test('the tracked partner\'s clue lies on its route: every trip there reads exactly one more layer of it, 2/5 → 3/5 → 4/5',()=>{
  const s=player(['OBS-1']);trackPartner(s,'0:121');
  const info=explorationInfo(s,'wood',['0:0'],NOW);assert.equal(info.trackedClue.key,'0:121');assert.equal(info.clueSure,true);assert.ok(info.clueChance<=.55,'the snapshot keeps the team odds');
  assert.equal(clueRow(s,'0:121',NOW,null).progress,2);
  let at=NOW;const before=s.progress.knowledge.facts.length;
  const c1=trip(s,at);assert.equal(c1.key,'0:121');assert.equal(c1.level,3);
  assert.equal(s.progress.knowledge.facts.length,before+1,'one fact per trip');
  assert.equal(clueRow(s,'0:121',NOW,null).progress,3);
  at=s.progress.trip.endAt+1;const c2=trip(s,at);assert.equal(c2.key,'0:121');assert.equal(c2.level,4);
  assert.equal(clueRow(s,'0:121',NOW,null).progress,4);
  // nothing left for C122: the next trip goes on to another 茶坡 partner, still one fact
  at=s.progress.trip.endAt+1;const n=s.progress.knowledge.facts.length,c3=trip(s,at);
  assert.ok(s.progress.knowledge.facts.length<=n+1);if(c3)assert.notEqual(c3.key,'0:121');
  // a claimed trip never pays again
  assert.equal(claimTrip(s,s.progress.trip.id,{},s.progress.trip.endAt).clue,null);
});

test('without tracking a trip keeps the old odds and pity, and still gives at most one clue',()=>{
  const s=player(['OBS-1']);const info=explorationInfo(s,'wood',['0:0'],NOW);
  assert.equal(info.trackedClue,null);assert.ok(info.clueChance<1);
  for(let i=0,at=NOW;i<6;i++){const n=s.progress.knowledge.facts.length;depart(s,{routeId:'wood',members:['0:0']},at,()=>0.99);const t=s.progress.trip;claimTrip(s,t.id,{},t.endAt);assert.ok(s.progress.knowledge.facts.length-n<=1);at=t.endAt+1;}
});

test('tickets from before (silhouette and cookware already shown by 风味辨识) give the current next layer instead of nothing',()=>{
  const s=player(['OBS-1']);trackPartner(s,'0:121');
  depart(s,{routeId:'wood',members:['0:0']},NOW,()=>0);const t=s.progress.trip;
  const r=RECIPE_CATALOG.find(x=>x.key==='0:121');
  t.clueOrder=[{key:'0:121',recipeId:recipeId(r),level:2,fact:factId(r,2)}];t.clueHit=true;
  assert.equal(clueStillNew(s,t.clueOrder[0]),false,'风味辨识 already shows the cookware');
  assert.equal(tripClue(s,t,NOW).level,3);
  const s2=structuredClone(s);s2.progress.trip.status='returned';s2.progress.trip.returnedAt=s2.progress.trip.endAt;
  const adv=tripClueAdvance(s2,s2.progress.trip,NOW);assert.deepEqual({code:adv.code,before:adv.before,after:adv.after,text:adv.text},{code:'C122',before:2,after:3,text:'第一味是「面粉」'});
  assert.equal(claimTrip(s,t.id,{},t.endAt).clue.level,3);
});

test('a trip that leaves while a partner is tracked, and its return, pass save validation',()=>{
  const s=normalizeSave(freshState(NOW),NOW);s.kitchenLevel=2;s.toolLevels.fill(1);s.toolLevels[8]=-1;
  for(let i=0;i<89;i++){s.total['0:'+i]=600;s.farm['0:'+i]=3;}for(let i=12;i<89;i++){delete s.total['0:'+i];delete s.farm['0:'+i];}
  syncProgress(s);learnSkill(s,'OBS-1');trackPartner(s,'0:121');
  depart(s,{routeId:'wood',members:['0:0']},NOW,()=>.99);assert.equal(s.progress.trip.clueHit,true);
  validateSaveSchema(structuredClone(s),NOW);
  claimTrip(s,s.progress.trip.id,{},s.progress.trip.endAt);
  validateSaveSchema(structuredClone(s),s.progress.trip.endAt);
});

test('deeper clue tickets pass save validation and the trip region map covers every route',()=>{
  const s=normalizeSave(freshState(NOW),NOW),r=RECIPE_CATALOG.find(x=>x.key==='0:121');
  s.progress.knowledge.facts.push(factId(r,3),factId(r,4));
  validateSaveSchema(structuredClone(s),NOW);
  for(const route of ROUTES)assert.ok(ROUTE_REGION[route.id],route.id);
});

test('推测 cards only once the clues leave a few seasonings; before that the track bar says which trip reads the next layer',()=>{
  const s=player(['OBS-1']);trackPartner(s,'0:121');
  let g=nextBatchGoals(s,NOW);
  assert.ok(g.cards.every(c=>c.action!=='guess'||(c.guess.group&&c.guess.candidates.length<=GUESS_MAX)),'no guess with many seasonings left');
  assert.equal(g.tracked.carded,false);assert.equal(g.tracked.note,'去茶坡寻访，读第一味');
  // first seasoning read: still 30-odd seasonings for the second slot, so the next step is a trip, not a batch
  let at=NOW;trip(s,at);g=nextBatchGoals(s,NOW);
  assert.equal(g.tracked.carded,false);assert.equal(g.tracked.note,'去茶坡寻访，读第二味的类别');
  let book=clueBookModel(s,0,NOW).tracked;assert.equal(book.status,'scout');assert.equal(book.canTry,true,'trying stays allowed from the 线索册');
  // the second one's group read: a few seasonings left, the guess is the first card
  at=s.progress.trip.endAt+1;trip(s,at);g=nextBatchGoals(s,NOW);
  const card=g.cards[0];assert.equal(card.kind,'track');assert.equal(card.action,'guess');assert.match(card.reason,/^追踪 C122 · 第二味：.+类$/);
  assert.ok(card.guess.candidates.length>=1&&card.guess.candidates.length<=GUESS_MAX);
  book=clueBookModel(s,0,NOW).tracked;assert.equal(book.status,'guess');assert.equal(book.region.id,'T');assert.equal(book.progress,4);
});

// Batch 5 playtest: while the party that brings the tracked partner's next clue is out, 下一锅 says it is on the way instead
// of sending the player to the same region again.
test('the tracked partner\'s clue already on the road: 下一锅 says so and offers no second 「去X」',()=>{
  const s=player(['OBS-1']);trackPartner(s,'0:121');
  const before=nextBatchGoals(s,NOW).tracked;assert.ok(before.scout,'not out yet: 去茶坡');assert.match(before.note,/^去.+寻访/);
  depart(s,{routeId:'wood',members:['0:0']},NOW,()=>0.99);assert.equal(s.progress.trip.clueHit,true);
  const out=nextBatchGoals(s,NOW+60000).tracked;assert.equal(out.scout,null);assert.match(out.note,/寻访中，回来就读到/);
  claimTrip(s,s.progress.trip.id,{},s.progress.trip.endAt);
  assert.ok(nextBatchGoals(s,s.progress.trip.endAt).tracked.scout,'home again: the next layer is a new trip');
});
