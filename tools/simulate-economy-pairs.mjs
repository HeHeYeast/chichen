// Work L counterfactuals: identical stock / authored recipes / equal seeds.
// No source prices or player saves are changed. Forced .99 trial tickets are a named
// worst-case protection experiment, never an estimate of expected yield.
import assert from 'node:assert/strict';
import {mkdir,writeFile} from 'node:fs/promises';
import {resolve} from 'node:path';
import {fileURLToPath} from 'node:url';
import * as E from '../web/engine.js';
import {REGIONAL,resolveSpecies} from '../web/content-registry.js';
import {prepareRegionalRecipe} from '../web/regional-methods.js';
import {openBusiness} from '../web/business.js';
import {acceptProposal,deliverOrderGroups,orderOptions} from '../web/orders.js';
import {buildBatchPlan} from '../web/batch-plan.js';
import {sampleLegacyCompanions} from '../web/legacy-recipe-adapter.js';
import {channelRandom} from '../web/rng.js';
import {claimLeftovers} from '../web/progression.js';
import {START,H,makeProfile,createRunner,learn,harvest,SKILL_POLICIES,assetValue,safeForGap} from './simulate-economy.mjs';
const root=fileURLToPath(new URL('../',import.meta.url));
const sum=o=>Object.values(o??{}).reduce((a,b)=>a+b,0);
const seeded=seed=>()=>{seed=(Math.imul(seed,1664525)+1013904223)>>>0;return seed/4294967296;};

export function recipeFixture(seed=37){
 const s=makeProfile('mature-low-stock',seed);s.cp=90000;s.farm={};s.ingredients={};
 s.expansion.regions.guideFlags=['GUIDE-B'];s.expansion.regions.introSpecimenDone=['V','R','T','B'];
 for(const m of REGIONAL.materials)s.expansion.discovery.identified[m.id]=++s.meta.factSeq;
 for(const c of REGIONAL.cards)s.expansion.discovery.cards[c.id]=++s.meta.factSeq;
 s.expansion.methods.directions=REGIONAL.recipes.map(r=>r.id);s.expansion.methods.full=REGIONAL.recipes.map(r=>r.id);
 return E.normalizeSave(s,START);
}
export function finishTimely(r){
 // Actual per-egg timers, including the earliest egg, define foreground
 // checkpoints. No batch ends shortcut can silently scorch early birds.
 const allAt=r.s.batch.ends+1,earliestExpiry=Math.min(...r.s.batch.eggs.filter(e=>e.blackAt!==null).map(e=>e.blackAt));
 if(r.s.batch.tool===0||allAt+3003<=earliestExpiry){r.advance(Math.max(r.now,allAt));harvest(r);assert.ok(r.s.batch.eggs.every(e=>e.collected));return;}
 while(r.s.batch.eggs.some(e=>!e.collected)){
   const next=Math.min(...r.s.batch.eggs.filter(e=>!e.collected).map(e=>e.openAt));
   r.advance(Math.max(r.now,next+1));harvest(r);
 }
 assert.equal(r.s.batch.plan.finished,true);
}
export function produce(r,recipe,{forcedFailure=false,regional=true,pairedSeed=null}={}){
 const beforeCP=r.s.cp,beforeFarm={...r.s.farm},beforeAssets=assetValue(r.s),ingredients=regional?recipe.ingredients.map(x=>x.id):recipe.ingredients;
 for(const id of ingredients)if(!(r.s.ingredients[id]>0))r.run('ingredient-purchase',d=>E.buyIngredient(d,id,1),{id});
 const purchaseCP=beforeCP-r.s.cp;
 const fireBefore=r.s.cp;
 r.run(regional?'regional-cook':'legacy-cook',d=>{if(regional)prepareRegionalRecipe(d,recipe.id);else{d.egg=recipe.egg;d.selected=[...ingredients];delete d.expansion.prepareMode;}
   let random=pairedSeed===null?undefined:channelRandom(d,`paired-${pairedSeed}`,'batch-comparison');if(forcedFailure){const plan=buildBatchPlan(d,recipe.toolId,r.now),seed=37+(d.expansion.trial[recipe.id]?.attemptSeq??0)*71;let calls=0;const previewRandom=seeded(seed);sampleLegacyCompanions(structuredClone(d),plan,r.now,()=>{calls++;return previewRandom();});let index=0;const actualRandom=seeded(seed);random=()=>{index++;const value=actualRandom();return !plan.guaranteed&&index>calls&&index<=calls+24?.99:value;};}
   E.startBatch(d,recipe.toolId,r.now,random,()=>.5);
 },{recipeId:recipe.id??recipe.key,forcedFailure,pairedSeed});
 const fireCP=fireBefore-r.s.cp,plan=structuredClone(r.s.batch.plan),minutes=(r.s.batch.ends-r.s.batch.started)/60000;
 finishTimely(r);if(r.s.progress.leftovers.length)r.run('leftovers',d=>claimLeftovers(d));const yieldByKey=Object.fromEntries(Object.entries(r.s.farm).map(([k,n])=>[k,n-(beforeFarm[k]??0)]).filter(([,n])=>n));
 const harvestCP=r.s.cp-(beforeCP-purchaseCP-fireCP),saleBefore=r.s.cp;
 r.run('instant-sale',d=>E.sell(d,yieldByKey,{overrideKeepOne:true,useRewards:true},r.now),{recipeId:recipe.id??recipe.key});
 const saleCP=r.s.cp-saleBefore,baseSale=Object.entries(yieldByKey).reduce((n,[k,q])=>n+q*resolveSpecies(k).cp_1,0),afterAssets=assetValue(r.s);
 return {recipeId:recipe.id??recipe.key,key:recipe.key,minutes,pairedSeed,ingredientNominal:ingredients.reduce((n,id)=>n+E.ingredient(id).buy_cp,0),purchaseCP,fireCP,harvestCP,saleCP,baseSale,netCP:r.s.cp-beforeCP,
   materialsValueChange:afterAssets.materials-beforeAssets.materials,netAfterMaterialValue:r.s.cp-beforeCP+afterAssets.materials-beforeAssets.materials,yieldByKey,target:yieldByKey[recipe.key]??0,roll:plan.roll,targetScheduled:plan.targetScheduled,mode:plan.mode,failedFullBatches:r.s.expansion.trial[recipe.id]?.failedFullBatches??null};
}
export function failureCosts(){return REGIONAL.recipes.map(recipe=>{
 const r=createRunner(recipeFixture(),{trace:false}),attempts=[];
 for(let i=0;i<4;i++){const c=E.kitchenCleanInfo(r.s,r.now);if(c.dirty)r.run('clean',d=>E.clean(d,r.now));attempts.push(produce(r,recipe,{forcedFailure:true}));}
 assert.deepEqual(attempts.map(x=>x.target),[0,0,0,0],recipe.id+' independent misses');
 assert.deepEqual(attempts.map(x=>x.failedFullBatches),[0,0,0,0]);
 return {recipeId:recipe.id,key:recipe.key,price:resolveSpecies(recipe.key).cp_1,attempts,firstFailureNet:attempts[0].netCP,fourBatchNet:attempts.reduce((n,x)=>n+x.netCP,0),grossInputsFirst:attempts[0].ingredientNominal+attempts[0].fireCP,commands:r.commands};
});}
export function sameStockChannels(){
 // A real lamp batch gives exactly 24 raw chicken. All channels receive the
 // same collected batch. O01 requests 12; its remainder is sold instantly, so
 // every comparison disposes of 24 birds and differs only in channel rules.
 return SKILL_POLICIES.map(branch=>{
   const seed=recipeFixture(91),r=createRunner(seed,{trace:false});learn(r,branch);
   r.run('cook',d=>{d.egg=0;d.selected=[];E.startBatch(d,0,r.now,()=>.5,()=>.5);});finishTimely(r);
   const stock={'0:0':24};assert.equal(r.s.farm['0:0'],24);const shared=r.s,at=r.now;
   const instant=createRunner(structuredClone(shared),{trace:false});instant.run('sale',d=>E.sell(d,stock,{overrideKeepOne:true,useRewards:true},at));
   const business=createRunner(structuredClone(shared),{trace:false});business.run('open',d=>openBusiness(d,{menuId:'MN1',stock,useRewards:true,overrideKeepOne:true},at));business.advance(at+24*H);
   const order=createRunner(structuredClone(shared),{trace:false}),proposal=order.s.expansion.orders.proposals.find(p=>p.templateId==='O01');assert.ok(proposal);
   order.run('accept',d=>acceptProposal(d,proposal.id,orderOptions(d,'O01',at)[0],at));const instance=order.s.expansion.orders.active[0];
   order.run('deliver',d=>deliverOrderGroups(d,instance.id,[{groupId:instance.groups[0].id,key:'0:0',quantity:12}],at,{overrideKeepOne:true}));order.run('sell-remainder',d=>E.sell(d,{'0:0':12},{overrideKeepOne:true,useRewards:true},at));
   for(const x of [instant,business,order])assert.equal(x.s.farm['0:0'],0);
   return {skillPolicy:branch,produced:stock,initialCredits:shared.progress.trade.credits,instant:{cp:instant.s.cp-shared.cp,creditsLeft:instant.s.progress.trade.credits},business:{cp:business.s.cp-shared.cp,creditsLeft:business.s.progress.trade.credits,report:business.s.expansion.business.lastReport},order:{cp:order.s.cp-shared.cp,creditsLeft:order.s.progress.trade.credits,completed:order.s.expansion.orders.templateProgress.O01.completed},note:'Order comparison = 12 delivered to O01 + 12 instant sold. Order base has no business markup.'};
 });
}
export function normalMargins({samples=8}={}){
 const legacy=[{key:'0:0',egg:0,toolId:0,ingredients:[]},{key:'0:3',egg:0,toolId:1,ingredients:[]},{key:'0:10',egg:0,toolId:2,ingredients:[3]},{key:'0:16',egg:0,toolId:3,ingredients:[]}];
 const results=[];
 for(const regional of [false,true])for(const recipe of regional?REGIONAL.recipes:legacy)for(const branch of SKILL_POLICIES){const rows=[];
   for(let seed=1;seed<=samples;seed++){const initial=recipeFixture(seed);if(regional)initial.total[recipe.key]=1;const r=createRunner(E.normalizeSave(initial,START),{trace:false});learn(r,branch);rows.push(produce(r,recipe,{regional,pairedSeed:seed}));}
   const nets=rows.map(x=>x.netAfterMaterialValue);results.push({recipeId:recipe.id??recipe.key,regional,skillPolicy:branch,samples,meanNet:nets.reduce((a,b)=>a+b,0)/samples,minNet:Math.min(...nets),maxNet:Math.max(...nets),negativeSamples:nets.filter(n=>n<0).length,rows});
 }
 return results;
}
export function timingPairs(){return [1,2,3,4,5,6,7,8].map(toolId=>{const none=createRunner(recipeFixture(),{trace:false}),home=createRunner(recipeFixture(),{trace:false});learn(home,'HOME');home.run('calm',d=>{d.progress.protection.calm=true;});const a=E.cookInfo(none.s,toolId,START),b=E.cookInfo(home.s,toolId,START);return {toolId,none:a,HOME:b,gaps:[120,480,720,1440].map(gap=>({gap,none:safeForGap(a,toolId,gap),HOME:safeForGap(b,toolId,gap)}))};});}
export function recipeTiming(){const rows=[];for(const equipment of ['minimum','maximum'])for(const skill of ['none','HOME','HOME-calm'])for(const recipe of REGIONAL.recipes){const initial=recipeFixture();initial.toolLevels[recipe.toolId]=equipment==='minimum'?recipe.toolLevel:2;const r=createRunner(initial,{trace:false});if(skill!=='none')learn(r,'HOME');if(skill==='HOME-calm')r.run('calm',d=>{d.progress.protection.calm=true;});r.s.egg=recipe.egg;r.s.selected=recipe.ingredients.map(x=>x.id);const info=E.cookInfo(r.s,recipe.toolId,START);rows.push({recipeId:recipe.id,equipment,skill,toolId:recipe.toolId,toolLevel:r.s.toolLevels[recipe.toolId],minutes:info.minutes,freshMinutes:info.freshMinutes,earliestOpenMinutes:info.minutes/12,foreground45:info.minutes+.06<=45&&safeForGap(info,recipe.toolId,info.minutes+.06),gaps:[120,480,600,720,1440].map(gap=>({gap,safe:info.minutes+.06<=gap&&safeForGap(info,recipe.toolId,gap)}))});}return rows;}
export async function main(){const output=resolve(root,'artifacts/sim');await mkdir(output,{recursive:true});const failures=failureCosts(),channels=sameStockChannels(),margins=normalMargins(),timing=timingPairs(),recipeWindows=recipeTiming();const passed=failures.length===48&&failures.every(x=>x.attempts[3].target===1);
 await writeFile(resolve(output,'economy-pairs.json'),JSON.stringify({checkedAt:new Date().toISOString(),passed,method:{failure:'Only the regional trial roll is forced to .99; ordinary companions and hatching use a seeded random stream. Four fully collected batches per recipe, clean and timely: stress cost, not mean expectation.',normal:'Eight seeds per 48 repeat recipes + four ordinary legacy recipes, per none/CUL/HOME/TRADE; fixed paired runtime-RNG ticket, same full equipment and full supply prerequisites, no initial stock. Materials valued at nominal replacement cost.',scope:'Marginal batch economics excludes equipment, upkeep, research and trip opportunity costs. These are separately recorded in the 14-day policy experiments.'},failures,channels,margins,timing,recipeWindows},null,2));
 console.log(JSON.stringify({passed,failureRecipes:failures.length,marginExperiments:margins.length,normalSamples:sum(Object.fromEntries(margins.map((x,i)=>[i,x.samples]))),negativeRepeatMeans:margins.filter(x=>x.regional&&x.skillPolicy==='none'&&x.meanNet<0).length,channels:channels.map(x=>({skill:x.skillPolicy,instant:x.instant.cp,business:x.business.cp,order:x.order.cp}))},null,2));
}
if(process.argv[1]&&resolve(process.argv[1])===fileURLToPath(import.meta.url))await main();
