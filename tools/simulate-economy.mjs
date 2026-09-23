// Reproducible policy experiments, not a proof of player pacing or optimal play.
// Every mutation is a validated execute command. Timeline and voluntary actions
// have separate ledger entries, so idle business income cannot become sale income.
import {mkdir,writeFile} from 'node:fs/promises';
import {resolve} from 'node:path';
import {fileURLToPath} from 'node:url';
import * as E from '../web/engine.js';
import {execute} from '../web/game-commands.js';
import {discoveryCount,collectedTotal,syncProgress,SKILLS,skillPoints,skillGate,applySkillPlan,claimLeftovers} from '../web/progression.js';
import {LEGACY193} from '../web/legacy-content.js';
import {REGIONAL,resolveSpecies,SPECIES_TRADE} from '../web/content-registry.js';
import {freeCount} from '../web/inventory.js';
import {openBusiness,businessCapacity} from '../web/business.js';
import {businessUnlockInfo,menuUnlockInfo} from '../web/menu-model.js';
import {storyOrders,acceptOrder,deliverOrder} from '../web/story-orders.js';
import {depart,claimTrip,explorationInfo,LEGACY_ROUTES} from '../web/exploration.js';
import {departRegional,regionalTripInfo} from '../web/regional-exploration.js';
import {regionInfo} from '../web/region-model.js';
import {identifyMaterial,regionalRecipeInfo,regionalMethodInfo,pinRegionalMethod,prepareRegionalRecipe} from '../web/regional-methods.js';
import {acceptProposal,deliverOrderGroups,skipProposal,orderOptions,displayOrder} from '../web/orders.js';
import {projectInfo,completeProjectStage,deliverProject,PROJECT_IDS} from '../web/projects.js';
import {readRegularStage,REGULAR_IDS} from '../web/regulars.js';
import {RECIPE_CATALOG,recipePathInfo} from '../web/recipe-book.js';
import {ingredientUnlockInfo} from '../web/ingredient-unlocks.js';
import {materialRoom} from '../web/material-capacity.js';

export const H=3600000,DAY=24*H,START=Date.UTC(2027,0,4)-8*H;
export const CADENCES={daily1:[8],daily2:[8,20],daily3:[8,13,21],frequent:[8,10,12,14,16,18,20,22]};
export const PROFILES=['starter','collector','mature-low-stock','hoarder'];
export const SKILL_POLICIES=['none','CUL','HOME','TRADE'];
export const POLICY={foregroundMinutes:45,keepOne:1,pantryBufferPerEdible:6,operatingReserve:600,maxForegroundBatches:1,
  recipeKnowledge:'Author-guided choice of legacy ingredients; no discovery or method flags are injected after initialization.',
  objective:'Discover uncollected executable recipes, complete affordable manual orders/projects, vary menus, reserve one of each species.',
  limitations:'One fixed seed per paired policy, a fixed 45-minute visit budget, and a heuristic planner; observed failure to reach content is not a content reachability proof.'};
const root=fileURLToPath(new URL('../',import.meta.url));
const sum=o=>Object.values(o??{}).reduce((a,b)=>a+b,0);
const newCount=s=>REGIONAL.species.filter(c=>s.total[c.key]>0).length;
export function makeProfile(id,seed=20260923){
  const s=E.freshState(START,seed);s.progress.tutorialSeen=true;
  if(id!=='starter'){
    if(!PROFILES.includes(id))throw Error('Unknown simulation profile');
    s.kitchenLevel=id==='collector'?1:3;s.duck=id!=='collector';s.toolLevels=id==='collector'?[1,1,1,0,0,0,-1,-1,-1]:s.toolLevels.map(()=>2);s.cp=id==='collector'?4000:20000;
    const keys=LEGACY193.characters.flatMap((cs,egg)=>cs.map(c=>`${egg}:${c.id}`)).slice(0,id==='collector'?60:193);
    s.total=Object.fromEntries(keys.map((k,i)=>[k,i?(id==='hoarder'?120:20):3000]));
    s.farm=Object.fromEntries(keys.map(k=>[k,id==='hoarder'&&resolveSpecies(k).edible?120:id==='collector'?3:2]));
    s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};
  }
  syncProgress(s);return E.normalizeSave(s,START);
}
export function assetValue(s){return {farm:Object.entries(s.farm).reduce((n,[k,q])=>n+q*resolveSpecies(k).cp_1,0),materials:Object.entries(s.ingredients).reduce((n,[id,q])=>n+q*E.ingredient(+id).buy_cp,0)};}
const deltaMap=(a,b)=>Object.fromEntries([...new Set([...Object.keys(a??{}),...Object.keys(b??{})])].map(k=>[k,(b?.[k]??0)-(a?.[k]??0)]).filter(([,v])=>v));
export function createRunner(initial,{trace=true}={}){
 const r={s:initial,now:initial.clock.logicalAt,trace:[],ledger:{},failures:[],initialCP:initial.cp,commands:0};
 r.run=(type,fn,details={})=>{
   const before=r.s,cp=before.cp;
   try{
     const out=execute({state:before,now:r.now,command:{type,...details},reduce:(d,c)=>fn(d,c)});
     r.s=out.state;r.commands++;const delta=r.s.cp-cp,row=r.ledger[type]??={income:0,spend:0,count:0};row.count++;row.income+=Math.max(0,delta);row.spend+=Math.max(0,-delta);const world=['timeline','farm-visit'].includes(type)?out.result:null;if(world?.lost)row.farmLost=(row.farmLost??0)+world.lost;
     if(trace)r.trace.push({seq:r.s.meta.commandSeq,at:r.now,type,...details,cpBefore:cp,cpAfter:r.s.cp,delta,farm:deltaMap(before.farm,r.s.farm),materials:deltaMap(before.ingredients,r.s.ingredients),...(world?{world}:{}),rngSeed:r.s.meta.rng.seed});
     if(r.s.cp<0)throw Error('Negative CP committed');return out.result;
   }catch(error){r.failures.push({at:r.now,type,details,message:error.message,code:error.code??null});throw Object.assign(error,{simulationAction:type,simulationFailures:r.failures,simulationTrace:r.trace});}
 };
 r.advance=at=>{if(at<r.now)throw Error('Simulation clock moved backwards');r.now=at;return r.run('timeline',d=>E.advanceWorld(d,at));};
 return r;
}
// Earliest egg can open after duration/12, not after the last egg. A second
// safety margin covers the explicit cracking/hatching frames used below.
export function safeForGap(info,toolId,gapMinutes){return toolId===0||info.minutes+0.06<=gapMinutes&&gapMinutes<=info.minutes/12+info.freshMinutes-0.06;}
export function harvest(r){
 if(!r.s.batch||r.s.batch.eggs.every(e=>e.collected))return 0;
 const due=r.s.batch.eggs.some(e=>!e.collected&&e.openAt<r.now);if(!due)return 0;
 r.run('batch-frame',d=>E.updateBatch(d,r.now));r.advance(r.now+2001);r.run('batch-frame',d=>E.updateBatch(d,r.now));r.advance(r.now+901);r.run('batch-frame',d=>E.updateBatch(d,r.now));
 let n=0;r.run('harvest',d=>{for(let i=0;i<24;i++)if(!d.batch.eggs[i].collected&&['hatching','ready'].includes(d.batch.eggs[i].status)){E.collect(d,i,r.now);n++;}});return n;
}
export function learn(r,branch){
 if(branch==='none')return;
 for(const node of SKILLS.filter(n=>n.branch===branch))if(!r.s.progress.skills[node.id]&&!skillGate(r.s,node.id)&&skillPoints(r.s).available>=node.cost)
   r.run('skill',d=>applySkillPlan(d,{steps:[node.id],category:branch==='TRADE'?'家常':null},r.now),{id:node.id});
}
function summary(s){return {cp:s.cp,discoveries:discoveryCount(s),newSpecies:newCount(s),cards:Object.keys(s.expansion.discovery.cards).length,methods:s.expansion.methods.full.length,collected:collectedTotal(s),kitchenLevel:s.kitchenLevel+1,duck:s.duck,skills:Object.keys(s.progress.skills),businessSessions:s.expansion.business.sequence,ordersCompleted:sum(s.expansion.facts.orderTemplateCounts),regularsRead:REGULAR_IDS.reduce((n,id)=>n+(s.expansion.regulars[id]?.readStages.length??0),0),projectStages:PROJECT_IDS.reduce((n,id)=>n+Object.keys(s.expansion.projects[id]?.stages??{}).length,0),assets:assetValue(s)};}

export function simulate(profile,cadence,branch='none',{days=14,trace=true,foregroundMinutes=POLICY.foregroundMinutes,offlineHours=72,initialState=null}={}){
 const r=createRunner(initialState?E.normalizeSave(structuredClone(initialState),START):makeProfile(profile),{trace}),initial=summary(r.s),daily=[],decisions={recipeAttempts:{},tripTargets:{},deferredCooking:0},start=START;
 const avail=k=>Math.max(0,freeCount(r.s,k)-1);
 function upkeep(){const repair=E.repairCost(r.s,r.now);if(repair&&E.farmHP(r.s,r.now)<=65&&r.s.cp>=repair+200)r.run('repair',d=>E.repair(d,r.now));const c=E.kitchenCleanInfo(r.s,r.now);if(c.dirty&&r.s.cp>=c.cost+200)r.run('clean',d=>E.clean(d,r.now));}
 function stories(){for(const o of storyOrders(r.s)){if(!o.unlocked||o.completed)continue;let saved=r.s.progress.orders[o.id];if(!saved?.accepted){const pick=o.choices.toSorted((a,b)=>avail(b.species)-avail(a.species))[0];r.run('story-accept',d=>acceptOrder(d,o.id,pick.species));saved=r.s.progress.orders[o.id];}const item=storyOrders(r.s).find(x=>x.id===o.id).choices.find(c=>c.species===saved.choice);if(!item)continue;const n=Math.min(item.count-saved.delivered,avail(item.species));if(n>0)r.run('story-delivery',d=>deliverOrder(d,o.id,n,r.now),{id:o.id,key:item.species,quantity:n});}}
 function orders(){
   for(const p of [...r.s.expansion.orders.proposals]){if(r.s.expansion.orders.active.length>=2)break;const option=orderOptions(r.s,p.templateId,r.now)[0];if(option)r.run('order-accept',d=>acceptProposal(d,p.id,option,r.now),{templateId:p.templateId});else r.run('order-skip',d=>skipProposal(d,p.id),{templateId:p.templateId});}
   for(const o of [...r.s.expansion.orders.active]){
     if(o.kind==='display'){const keys=o.groups[0].allowed.filter(k=>freeCount(r.s,k)>0).slice(0,o.minimumDistinct);if(keys.length===o.minimumDistinct)r.run('order-display',d=>displayOrder(d,o.id,keys));continue;}
     const allocation=[],used={},distinct=new Set(o.groups.flatMap(g=>Object.keys(g.delivered)));
     // Seed each required kind before filling quantities; never strand the last
     // slot of a minimum-distinct order with just one species.
     for(const g of o.groups){let left=g.quantity-sum(g.delivered);const keys=g.allowed.filter(k=>avail(k)>(used[k]??0)).sort((a,b)=>avail(b)-avail(a));
       for(const k of keys){if(left<=0||distinct.size>=o.minimumDistinct)break;if(distinct.has(k))continue;allocation.push({groupId:g.id,key:k,quantity:1});used[k]=(used[k]??0)+1;distinct.add(k);left--;}
       for(const k of keys){const otherNeed=Math.max(0,o.minimumDistinct-distinct.size),n=Math.min(Math.max(0,left-otherNeed),avail(k)-(used[k]??0));if(n>0){allocation.push({groupId:g.id,key:k,quantity:n});used[k]=(used[k]??0)+n;distinct.add(k);left-=n;}}
     }
     if(allocation.length)r.run('order-delivery',d=>deliverOrderGroups(d,o.id,allocation,r.now),{id:o.id,templateId:o.templateId});
   }
 }
 function projects(){for(const id of PROJECT_IDS){let info=projectInfo(r.s,id),st=info.current;if(!info.gateMet||!st?.checksMet)continue;
   if(st.delivery&&!st.delivery.full){const d=st.delivery,selection={};let choice=null;
     if(d.kind==='choose'){choice=d.locked??d.allowed.filter(k=>avail(k)>0).sort((a,b)=>avail(b)-avail(a)).slice(0,d.distinct);if(choice.length===d.distinct)for(const k of choice){const n=Math.min(d.quantityEach-(d.delivered[k]??0),avail(k));if(n>0)selection[k]=n;}}
     else {let need=d.target-d.total;const categories=new Set(d.categories);const keys=d.allowed.filter(k=>avail(k)>0).sort((a,b)=>avail(b)-avail(a));for(const k of keys){const cat=SPECIES_TRADE[k]?.category;if(!cat||categories.has(cat)||categories.size>=d.minimumCategories||need<=0)continue;selection[k]=1;categories.add(cat);need--;}for(const k of keys){const n=Math.min(Math.max(0,need-Math.max(0,d.minimumCategories-categories.size)),avail(k)-(selection[k]??0));if(n>0){selection[k]=(selection[k]??0)+n;need-=n;}}}
     if(Object.keys(selection).length)r.run('project-delivery',s=>deliverProject(s,id,st.id,selection,{choice}),{projectId:id,stageId:st.id});
   }
   info=projectInfo(r.s,id);st=info.current;if(st?.ready&&r.s.cp>=st.costCP+POLICY.operatingReserve)r.run('project-payment',d=>completeProjectStage(d,id,st.id),{projectId:id,stageId:st.id});
 }for(const id of REGULAR_IDS)if(r.s.expansion.regulars[id]?.pendingStage)r.run('regular-read',d=>readRegularStage(d,id));}
 function trips(){
   if(r.s.progress.trip){if(r.now>=r.s.progress.trip.endAt)r.run('trip-claim',d=>claimTrip(d,d.progress.trip.id,{discard:true},r.now));else return;}
   for(const m of REGIONAL.materials)if(r.s.expansion.discovery.cards[m.specimen]&&!Object.hasOwn(r.s.expansion.discovery.identified,String(m.id)))r.run('identify',d=>identifyMaterial(d,m.id),{materialId:m.id});
   for(const region of ['V','R','T','B']){if(!regionInfo(r.s,region).met)continue;const pin=REGIONAL.recipes.find(x=>resolveSpecies(x.key).region===region&&r.s.expansion.methods.directions.includes(x.id)&&!regionalMethodInfo(r.s,x.id).full&&regionalRecipeInfo(r.s,x.id,{entry:true}).met);if(pin&&r.s.expansion.methods.freeProgress?.[region]?.targetId!==pin.id)r.run('method-pin',d=>pinRegionalMethod(d,pin.id),{id:pin.id});}
   const fallback=Object.keys(r.s.farm).filter(k=>avail(k)>0).sort((a,b)=>avail(b)-avail(a));if(!fallback.length)return;
   const candidates=[];
   for(const card of REGIONAL.cards){if(r.s.expansion.discovery.cards[card.id]||!regionInfo(r.s,card.region).met)continue;
     const team=card.team.oldExamples.filter(k=>freeCount(r.s,k)>0);const members=team.length?team.slice(0,3):fallback.slice(0,3),options={regionId:card.region,placeId:card.placeId,focus:card.focus,members};const info=regionalTripInfo(r.s,options,r.now);if(!info.canDepart)continue;
     const targetAvailable=info.candidates.some(c=>c.cardId===card.id)||info.firstSpecimen;if(!targetAvailable)continue;
     if(info.guide.available&&!r.s.expansion.regions.guideFlags.includes('GUIDE-B'))options.guide=true;
     const count=REGIONAL.cards.filter(c=>c.region===card.region&&r.s.expansion.discovery.cards[c.id]).length;
     candidates.push({options,id:card.id,score:(info.firstSpecimen?100:0)+(card.type==='specimen'?30:card.type==='lore'?20:10)-count*4-(decisions.tripTargets[card.id]??0)});
   }
   if(!candidates.length){for(const region of ['V','R','T','B']){if(!regionInfo(r.s,region).met)continue;const options={regionId:region,placeId:`${region}:0`,focus:'lore',members:fallback.slice(0,3)},info=regionalTripInfo(r.s,options,r.now);if(info.canDepart)candidates.push({options,id:`${region}:method`,score:-(decisions.tripTargets[`${region}:method`]??0)});}}
   const pick=candidates.sort((a,b)=>b.score-a.score)[0];if(pick){r.run('trip-depart',d=>departRegional(d,pick.options,r.now),{target:pick.id,...pick.options});decisions.tripTargets[pick.id]=(decisions.tripTargets[pick.id]??0)+1;return;}
   for(const route of LEGACY_ROUTES){const info=explorationInfo(r.s,route.id,fallback.slice(0,1),r.now);if(info.unlocked){r.run('legacy-trip',d=>depart(d,{routeId:route.id,members:fallback.slice(0,1)},r.now),{id:route.id});break;}}
 }
 function business(){if(r.s.expansion.business.active||!businessUnlockInfo(r.s).met)return;const cap=businessCapacity(r.s);let chosen=null;
   const menus=REGIONAL.menus.filter(m=>menuUnlockInfo(r.s,m.id).met).sort((a,b)=>Number(!!r.s.expansion.facts.predicateWitnesses[`${a.id}:validService`])-Number(!!r.s.expansion.facts.predicateWitnesses[`${b.id}:validService`])||sum(r.s.expansion.facts.businessMenuCounts[a.id])-sum(r.s.expansion.facts.businessMenuCounts[b.id]));
   for(const menu of menus){const stock={},used=new Set();let n=0,met=true;for(const role of menu.roles.filter(x=>x.required)){const k=role.allowed.filter(k=>!used.has(k)&&avail(k)>=3).sort((a,b)=>avail(b)-avail(a))[0];if(!k){met=false;break;}stock[k]=Math.min(12,avail(k),cap-n);n+=stock[k];used.add(k);}if(!met&&menu.id!=='MN1')continue;
     for(const k of Object.keys(r.s.farm).filter(k=>resolveSpecies(k).edible).sort((a,b)=>avail(b)-avail(a))){if(n>=cap)break;if(!stock[k]&&Object.keys(stock).length>=6)continue;const q=Math.min(cap-n,avail(k)-(stock[k]??0));if(q>0){stock[k]=(stock[k]??0)+q;n+=q;}}
     if(n>=6){chosen={menuId:menu.id,stock,useRewards:branch==='TRADE'};break;}}
   if(chosen)r.run('business-open',d=>openBusiness(d,chosen,r.now),{menuId:chosen.menuId,stock:chosen.stock});
 }
 function sell(){const selection={};for(const k of Object.keys(r.s.farm)){const q=Math.max(0,avail(k)-(resolveSpecies(k).edible&&businessUnlockInfo(r.s).met?6:0));if(q>0)selection[k]=q;}if(Object.keys(selection).length)r.run('instant-sale',d=>E.sell(d,selection,{useRewards:branch==='TRADE'},r.now),{quantity:sum(selection)});}
 function invest(){const ids=r.s.toolLevels.map((_,id)=>id).sort((a,b)=>(r.s.toolLevels[a]>=0)-(r.s.toolLevels[b]>=0)||a-b);for(const id of ids)if(E.canBuyTool(r.s,id)){const next=r.s.toolLevels[id]+1,cost=E.tool(id)[`lv_${next}_buy_cp`],reserve=id===1&&next===0?100:600;if(r.s.cp>=cost+reserve)r.run('tool-investment',d=>E.buyTool(d,id),{id,level:next});}if(!r.s.duck&&r.s.cp>=3100)r.run('duck-investment',d=>E.buyDuck(d));const k=E.kitchenUpgradeInfo(r.s);if(k.canUpgrade&&r.s.cp>=k.cost+600)r.run('kitchen-investment',d=>E.upgradeKitchen(d,r.now));}
 function cook({foreground,nextAt}){
   if(r.s.batch?.eggs.some(e=>!e.collected))return false;const gap=(nextAt-r.now)/60000;
   if(r.s.progress.leftovers.length&&materialRoom(r.s)>0)r.run('leftovers',d=>claimLeftovers(d));
   const calm=branch==='HOME'&&!!r.s.progress.skills['HOME-5']&&!foreground;
   if(r.s.progress.protection.calm!==calm)r.run('cooking-option',d=>{d.progress.protection.calm=calm;},{calm});
   const fits=toolId=>{const info=E.cookInfo(r.s,toolId,r.now);return foreground?info.minutes+0.06<=foregroundMinutes&&safeForGap(info,toolId,info.minutes+0.06):safeForGap(info,toolId,gap);};
   const candidates=[];
   for(const recipe of REGIONAL.recipes){if(r.s.total[recipe.key]>0||!r.s.expansion.methods.full.includes(recipe.id)||!regionalRecipeInfo(r.s,recipe.id).met||!fits(recipe.toolId))continue;candidates.push({regional:true,recipe,key:recipe.key,toolId:recipe.toolId,egg:recipe.egg,ingredients:recipe.ingredients.map(x=>x.id),score:100-(decisions.recipeAttempts[recipe.id]??0)});}
   for(const recipe of RECIPE_CATALOG){if(recipe.toolId<0||recipe.kind==='seasonal'||!recipePathInfo(r.s,recipe,r.now).conditions.every(x=>x.met)||!recipe.ingredients.every(id=>ingredientUnlockInfo(r.s,id).available)||!fits(recipe.toolId))continue;const cost=E.cookInfo(r.s,recipe.toolId,r.now).cost+recipe.ingredients.reduce((n,id)=>n+E.ingredient(id).buy_cp,0);if(r.s.cp<cost+100)continue;candidates.push({recipe,key:recipe.key,toolId:recipe.toolId,egg:recipe.egg,ingredients:recipe.ingredients,score:(r.s.total[recipe.key]>0?0:50)-(decisions.recipeAttempts[recipe.key]??0)-cost/1000});}
   const pick=candidates.filter(c=>c.ingredients.filter(id=>!(r.s.ingredients[id]>0)).length<=materialRoom(r.s)).sort((a,b)=>b.score-a.score)[0];if(!pick){decisions.deferredCooking++;return false;}
   const id=pick.regional?pick.recipe.id:pick.key,cost=E.cookInfo(r.s,pick.toolId,r.now).cost+pick.ingredients.reduce((n,id)=>n+(r.s.ingredients[id]>0?0:E.ingredient(id).buy_cp),0);if(r.s.cp<cost+100)return false;
   for(const id of pick.ingredients)if(!(r.s.ingredients[id]>0))r.run('ingredient-purchase',d=>E.buyIngredient(d,id,1),{id});
   r.run(pick.regional?'regional-cook':'legacy-cook',d=>{if(pick.regional)prepareRegionalRecipe(d,pick.recipe.id);else{d.egg=pick.egg;d.selected=[...pick.ingredients];delete d.events.seasonalRecipe;delete d.expansion.prepareMode;}E.startBatch(d,pick.toolId,r.now,undefined,()=>0.5);},{target:id,foreground});decisions.recipeAttempts[id]=(decisions.recipeAttempts[id]??0)+1;
   if(foreground){r.advance(r.s.batch.ends+1);harvest(r);}return true;
 }
 for(let day=0;day<days;day++){
   for(const [index,hour]of CADENCES[cadence].entries()){
     const visitAt=start+day*DAY+hour*H,nextHour=CADENCES[cadence][index+1]??CADENCES[cadence][0]+24,nextAt=start+day*DAY+nextHour*H;
     r.advance(visitAt);r.run('farm-visit',d=>({lost:E.checkFarmLoss(d,r.now)}));harvest(r);upkeep();learn(r,branch);stories();orders();projects();trips();business();sell();invest();
     cook({foreground:true,nextAt});stories();orders();projects();learn(r,branch);business();sell();
     // End the bounded foreground visit, then choose a recipe whose earliest
     // egg survives the entire planned absence (or use the harmless lamp).
     if(r.now<visitAt+foregroundMinutes*60000)r.advance(visitAt+foregroundMinutes*60000);
     cook({foreground:false,nextAt});
   }
   daily.push({day:day+1,...summary(r.s)});
 }
 // Close every cadence at the same 14×24h boundary. Otherwise a once-daily
 // player's report ends fourteen hours earlier than the frequent player's.
 r.advance(start+days*DAY);
 const beforeOffline=summary(r.s),ledgerBeforeOffline=structuredClone(r.ledger),offlineStart=r.now,offlineFarm={...r.s.farm},offlineCP=r.s.cp;const offlineWorld=r.advance(r.now+offlineHours*H),offlineVisit=r.run('farm-visit',d=>({lost:E.checkFarmLoss(d,r.now)}));harvest(r);const afterOffline=summary(r.s);
 E.normalizeSave(r.s,r.now);const end=summary(r.s),income=sum(Object.fromEntries(Object.entries(r.ledger).map(([k,v])=>[k,v.income]))),spend=sum(Object.fromEntries(Object.entries(r.ledger).map(([k,v])=>[k,v.spend])));
 const conserved=end.cp===initial.cp+income-spend,investment=['tool-investment','duck-investment','kitchen-investment','project-payment'].reduce((n,k)=>n+(r.ledger[k]?.spend??0),0);
 const inventoryAdjusted=end.cp-initial.cp+end.assets.farm-initial.assets.farm+end.assets.materials-initial.assets.materials;
 return {profile,cadence,skillPolicy:branch,visitsPerDay:CADENCES[cadence].length,days,policy:{...POLICY,foregroundMinutes},initial,end,beforeOffline,daily,ledgerBeforeOffline,ledger:r.ledger,failures:r.failures,decisions,commands:r.commands,trace:r.trace,
   checks:{cpConserved:conserved,noRejectedCommands:r.failures.length===0,noNegativeCP:daily.every(d=>d.cp>=0)&&end.cp>=0,validFinalSave:true},cash:{income,spend,delta:end.cp-initial.cp,investment,operatingCash:end.cp-initial.cp+investment,inventoryAdjusted,inventoryAdjustedBeforeInvestment:inventoryAdjusted+investment},
   offline:{hours:offlineHours,from:offlineStart,to:r.now,cpDelta:r.s.cp-offlineCP,farmLost:(offlineWorld.lost??0)+(offlineVisit.lost??0),farmDelta:deltaMap(offlineFarm,r.s.farm),before:beforeOffline,after:afterOffline},finalState:r.s};
}

export async function main(){
 const output=resolve(root,'artifacts/sim');await mkdir(resolve(output,'traces'),{recursive:true});const results=[],filter=process.env.CHICK_SIM_FILTER?.split(',');
 for(const profile of PROFILES)for(const cadence of Object.keys(CADENCES))for(const branch of SKILL_POLICIES){const id=`${profile}-${cadence}-${branch}`;if(filter&&!filter.some(x=>id.includes(x)))continue;
   try{const r=simulate(profile,cadence,branch);const {trace,finalState,...report}=r;await writeFile(resolve(output,'traces',id+'.jsonl'),trace.map(x=>JSON.stringify(x)).join('\n')+'\n');await writeFile(resolve(output,'traces',id+'-final.json'),JSON.stringify(finalState));results.push(report);console.log(`${id}: CP ${r.end.cp}; inventory-adjusted ${r.cash.inventoryAdjusted}; discoveries ${r.end.discoveries}; new ${r.end.newSpecies}; cards ${r.end.cards}; projects ${r.end.projectStages}; commands ${r.commands}`);}
   catch(error){await writeFile(resolve(output,'traces',id+'-failed.jsonl'),(error.simulationTrace??[]).map(x=>JSON.stringify(x)).join('\n'));results.push({profile,cadence,skillPolicy:branch,failed:true,message:error.message,action:error.simulationAction,failures:error.simulationFailures,stack:error.stack});console.error(`${id}: FAILED ${error.simulationAction}: ${error.message}`);}
 }
 const passed=results.length>0&&results.every(r=>!r.failed&&Object.values(r.checks).every(Boolean));await writeFile(resolve(output,'economy-14d.json'),JSON.stringify({checkedAt:new Date().toISOString(),passed,policy:POLICY,results},null,2));if(!passed)process.exitCode=1;
}
if(process.argv[1]&&resolve(process.argv[1])===fileURLToPath(import.meta.url))await main();
