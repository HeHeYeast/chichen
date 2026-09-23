// Work L: operation-driven reachability. A bot plays a late legacy save using only
// the domain commands the UI calls (regional trips, claim, identify, pin a method
// and earn it with free trips, buy supplies, prepare, cook, collect) until all 48
// new species are collected and all 24 discovery cards are recorded. Each step is
// validated by normalizeSave. The report lists attempts per card/species, the
// method that obtained it and the CP spent. Usage: node tools/simulate-reachability.mjs [seed]
import {mkdir,writeFile} from 'node:fs/promises';
import {resolve} from 'node:path';
import {fileURLToPath} from 'node:url';
import * as E from '../web/engine.js';
import {execute} from '../web/game-commands.js';
import {earnedSources} from '../web/progression.js';
import {LEGACY193} from '../web/legacy-content.js';
import {REGIONAL,resolveSpecies} from '../web/content-registry.js';
import {departRegional,regionalTripInfo,guideEligibility} from '../web/regional-exploration.js';
import {claimTrip} from '../web/exploration.js';
import {identifyMaterial,regionalRecipeInfo,regionalMethodInfo,pinRegionalMethod,prepareRegionalRecipe} from '../web/regional-methods.js';
import {regionInfo} from '../web/region-model.js';
import * as MATERIAL from '../web/material-capacity.js';

const root=fileURLToPath(new URL('../',import.meta.url));
const seedArg=Number(process.argv[2]??7),H=3600000;
let now=1800000000000;
const s0=E.freshState(now,seedArg);
s0.kitchenLevel=3;s0.duck=true;s0.cp=2000000;s0.toolLevels=s0.toolLevels.map(()=>2);
s0.total=Object.fromEntries(LEGACY193.characters.flatMap((list,egg)=>list.map(c=>[`${egg}:${c.id}`,3])));s0.total['0:0']=9000;
s0.farm=Object.fromEntries(Object.keys(s0.total).map(k=>[k,6]));s0.progress.sources=earnedSources(s0);s0.progress.tutorialSeen=true;
let s=E.normalizeSave(s0,now);
const startCP=s.cp,log=[],cards={},species={},steps={trips:0,batches:0,identify:0,purchases:0};
// Every action is one real transaction (commandSeq advances the RNG stream, the
// save is validated, collection/regular reconciliation runs), exactly as the UI does.
function run(type,fn){const previous=s;let result;try{const outcome=execute({state:previous,now,command:{type,at:now},advance:E.advanceWorld,reduce:draft=>{s=draft;result=fn();}});s=outcome.state;return result;}catch(error){s=previous;throw error;}}
const has=(id)=>Object.hasOwn(s.expansion.discovery.cards,id);
const identified=id=>Object.hasOwn(s.expansion.discovery.identified,String(id));
const collected=key=>(s.total[key]??0)>0;
const keys=()=>Object.keys(s.farm).filter(k=>s.farm[k]>=2);

// A careful player keeps the farm repaired (escapes only happen to a neglected farm).
function upkeep(){if(E.repairCost(s,now)>0){E.repair(s,now);steps.repairs=(steps.repairs??0)+1;}}
function trip(options,why){
  if(s.progress.trip){const open=s.progress.trip;now=Math.max(now,open.endAt);run('claim',()=>{E.advanceWorld(s,now);claimTrip(s,open.id,{discard:true},now);});}
  run('upkeep',upkeep);
  const info=regionalTripInfo(s,options,now);if(!info.canDepart)throw Error(`cannot depart ${JSON.stringify(options)}: ${info.missing?.join('；')??''}`);
  run('depart',()=>departRegional(s,options,now));const t=s.progress.trip;now=t.endAt;
  run('claim',()=>{E.advanceWorld(s,now);claimTrip(s,t.id,{discard:true},now);});steps.trips++;
  const result=s.progress.trip?.regional?.result??null;
  for(const m of REGIONAL.materials)if(has(m.specimen)&&!identified(m.id)){run('identify',()=>identifyMaterial(s,m.id));steps.identify++;log.push(`identify ${m.id}`);}
  log.push(`trip ${options.regionId}/${options.placeId}/${options.focus} [${options.members.join(',')}] ${why} → ${result?.cardId??'-'}`);
  return result;
}
// A team whose snapshot makes `card` an eligible candidate at its place/focus.
function teamFor(card){
  const pool=keys().filter(k=>resolveSpecies(k));
  const attempt=members=>{const info=regionalTripInfo(s,{regionId:card.region,placeId:card.placeId,focus:card.focus,members},now);return info.canDepart&&info.candidates.some(c=>c.cardId===card.id)?members:null;};
  for(const a of pool){const t=attempt([a]);if(t)return t;}
  // Pairs over the whole pool: a trait and an environment may come from different members.
  for(let i=0;i<pool.length;i++)for(let j=i+1;j<pool.length;j++){const t=attempt([pool[i],pool[j]]);if(t)return t;}
  const short=pool.slice(0,70);
  for(let i=0;i<short.length;i++)for(let j=i+1;j<short.length;j++)for(let k=j+1;k<Math.min(short.length,i+25);k++){const t=attempt([short[i],short[j],short[k]]);if(t)return t;}
  return null;
}
function earnCards(region){
  for(let guard=0;guard<400;guard++){
    const missing=REGIONAL.cards.filter(c=>c.region===region&&!has(c.id));if(!missing.length)return;
    const card=missing.find(c=>teamFor(c))??null;if(!card){const c=missing[0],info=regionalTripInfo(s,{regionId:c.region,placeId:c.placeId,focus:c.focus,members:['0:6','1:0']},now);console.error(JSON.stringify({card:c.id,place:c.placeId,focus:c.focus,canDepart:info.canDepart,missing:info.missing,candidates:info.candidates,identified:Object.keys(s.expansion.discovery.identified),cards:Object.keys(s.expansion.discovery.cards),farm:[s.farm['0:6'],s.farm['1:0']],trip:!!s.progress.trip}));throw Error(`no legal team yet for ${missing.map(c=>c.id)} — collect more species first`);}
    const members=teamFor(card);cards[card.id]??={attempts:0,team:members};cards[card.id].attempts++;
    const r=trip({regionId:region,placeId:card.placeId,focus:card.focus,members},`for ${card.id}`);if(r?.cardId&&cards[r.cardId]===undefined)cards[r.cardId]={attempts:0,team:members,incidental:true};
  }
  throw Error(`cards of ${region} not complete after 400 trips`);
}
// Harvest a batch on the live timeline: open, crack (2s), hatch (0.9s), collect promptly.
function harvest(){const readyAt=E.batchReadyAt(s.batch);for(const dt of [0,2100,3100]){now=readyAt+dt;run('tick',()=>{E.resume(s,now);E.updateBatch(s,now);});}now=readyAt+3200;run('collect',()=>{E.updateBatch(s,now);for(let i=0;i<24;i++)E.collect(s,i,now);});}
// The bag is capped (30/36/42). Like a player, spend surplus seasonings in plain
// lamp batches (up to kitchen-level many kinds each) until `need` slots are free.
function freeRoom(need,keep){
  const {materialRoom}=MATERIAL;
  for(let guard=0;materialRoom(s)<need;guard++){
    if(guard>40)throw Error('cannot free material room');
    const surplus=Object.keys(s.ingredients).map(Number).filter(id=>s.ingredients[id]>0&&!keep.includes(id)&&![68,69,70].includes(id)).slice(0,3);
    if(!surplus.length)throw Error('bag full of needed materials');
    run('plain',()=>{s.egg=0;s.selected=surplus;if(E.kitchenCleanInfo(s,now).cost>0)E.clean(s,now);E.startBatch(s,0,now);});steps.plainBatches=(steps.plainBatches??0)+1;harvest();
  }
}
function cook(recipeId){
  const r=REGIONAL.recipes.find(x=>x.id===recipeId),key=r.key;species[key]={recipeId,batches:0,trips:0};
  // Earn the full method with pinned free trips (no paid study).
  for(let guard=0;!regionalMethodInfo(s,recipeId).full;guard++){
    if(guard>12)throw Error(`method ${recipeId} not full after 12 trips`);
    run('pin',()=>pinRegionalMethod(s,recipeId));const region=resolveSpecies(key).region;
    trip({regionId:region,placeId:`${region}:0`,focus:'materials',members:['0:0']},`method ${recipeId}`);species[key].trips++;
  }
  for(let guard=0;!collected(key);guard++){
    if(guard>8)throw Error(`${key} not collected after 8 full batches`);
    const info=regionalRecipeInfo(s,recipeId);if(!info.met)throw Error(`${recipeId}: ${info.missing.join('；')}`);
    const wanted=info.recipe.ingredients.map(i=>i.id),missingCount=info.recipe.ingredients.reduce((n,i)=>n+Math.max(0,i.quantity-(s.ingredients[i.id]??0)),0);freeRoom(missingCount,wanted);
    run('buy',()=>{for(const i of info.recipe.ingredients){const need=i.quantity-(s.ingredients[i.id]??0);if(need>0){E.buyIngredient(s,i.id,need);steps.purchases++;}}});
    run('cook',()=>{prepareRegionalRecipe(s,recipeId);upkeep();if(E.kitchenCleanInfo(s,now).cost>0)E.clean(s,now);E.startBatch(s,r.toolId,now);});steps.batches++;species[key].batches++;harvest();
  }
  log.push(`collected ${key} via ${recipeId} in ${species[key].batches} batch(es)`);
}
const order=['V','R','T','B'];
for(const region of order){
  if(region==='B'&&!s.expansion.regions.guideFlags.includes('GUIDE-B')){
    if(!guideEligibility(s).met)throw Error('guide not eligible: '+guideEligibility(s).missing.join('；'));
    trip({regionId:REGIONAL.guide.sourceRegion,placeId:REGIONAL.guide.sourcePlaceId,focus:'materials',members:['0:0'],guide:true},'follow the bay signs');
  }
  if(!regionInfo(s,region).met)throw Error(`${region} closed: ${regionInfo(s,region).missing.join('；')}`);
  // Intro specimen first (guaranteed), then cook what the identified materials allow,
  // then every remaining card (some need the new species as companions), then the rest.
  const intro=REGIONAL.cards.find(c=>c.region===region&&c.type==='specimen'&&!has(c.id));
  if(intro&&!s.expansion.regions.introSpecimenDone.includes(region))trip({regionId:region,placeId:intro.placeId,focus:'specimen',members:['0:0']},'intro specimen');
  for(let pass=0;pass<3;pass++){
    for(const r of REGIONAL.recipes.filter(x=>resolveSpecies(x.key).region===region))if(!collected(r.key)&&s.expansion.methods.directions.includes(r.id)&&regionalRecipeInfo(s,r.id,{entry:true}))try{cook(r.id);}catch(error){if(pass===2)throw error;}
    try{earnCards(region);}catch(error){if(pass===2)throw error;}
  }
  for(const r of REGIONAL.recipes.filter(x=>resolveSpecies(x.key).region===region))if(!collected(r.key))cook(r.id);
}
const missingSpecies=REGIONAL.species.filter(c=>!collected(c.key)).map(c=>c.key),missingCards=REGIONAL.cards.filter(c=>!has(c.id)).map(c=>c.id);
const report={checkedAt:new Date().toISOString(),seed:seedArg,passed:!missingSpecies.length&&!missingCards.length,missingSpecies,missingCards,
  totals:{...steps,virtualDays:+((now-1800000000000)/86400000).toFixed(1),cpSpent:startCP-s.cp},cards,species,finalValid:true,log};
await mkdir(resolve(root,'artifacts/sim'),{recursive:true});await writeFile(resolve(root,`artifacts/sim/reachability-${seedArg}.json`),JSON.stringify(report,null,2));
console.log(JSON.stringify({passed:report.passed,seed:seedArg,missingSpecies,missingCards,totals:report.totals},null,2));
if(!report.passed)process.exitCode=1;
