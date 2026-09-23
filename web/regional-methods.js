import {REGIONAL,CONTENT_TEXT,resolveRecipeId,resolveSpecies,materialById} from './content-registry.js';
import {LEGACY193} from './legacy-content.js';
import {rank} from './progression.js';
import {speciesDiscovered} from './species-state.js';
import {availableIngredientIds,ingredientUnlockInfo} from './ingredient-unlocks.js';
import {REGIONAL_RELEASE,regionInfo,regionalMaterial,hasRegionalCard,materialIdentified,nextRegionalFact,alternativeUnlocked} from './region-model.js';
import {assertNewOperation} from './rollback-policy.js';

const fullMethod=(s,r)=>s.expansion.methods.full.includes(r.id)||speciesDiscovered(s,r.egg,Number(r.key.split(':')[1]));
// Old ingredients keep the one shared supply definition (validated equal to the
// author's oldSupply at build time); a regional identity never bypasses it.
const oldSupplyMet=(s,id)=>ingredientUnlockInfo(s,id).available;

export function regionalRecipeInfo(s,recipeId,{entry=false}={}){
  const recipe=resolveRecipeId(recipeId),missing=[];
  if(!recipe||recipe.mode!=='regional-trial')throw Error('没有找到这份地方做法。');
  const species=resolveSpecies(recipe.key),area=regionInfo(s,species.region);
  if(!REGIONAL_RELEASE.methods.includes(recipeId))missing.push('这份地方做法尚未开放。');
  missing.push(...area.missing);
  if(s.kitchenLevel<recipe.kitchenLevel)missing.push(`厨房 Lv.${recipe.kitchenLevel+1}`);
  if((s.toolLevels[recipe.toolId]??-1)<recipe.toolLevel)missing.push(`${LEGACY193.tools[1].find(t=>t.id===recipe.toolId)?.title_zh_CN??'厨具'} Lv.${recipe.toolLevel+1}`);
  if(recipe.egg===1&&!s.duck)missing.push('先开放鸭蛋。');
  for(const supply of recipe.oldSupply)if(!oldSupplyMet(s,supply.id))missing.push(`${materialById[supply.id]?.title_zh_CN??'旧材料'}供货尚未开放。`);
  if(!entry){
    for(const id of species.unlock.identifiedMaterials)if(!materialIdentified(s,id))missing.push(`先辨认${materialById[id]?.title_zh_CN??id}。`);
    if(species.unlock.firstSpecimenForOldOnly&&!s.expansion.regions.introSpecimenDone.includes(species.region))missing.push('先找到本地区的首标本。');
    if(species.unlock.card&&!hasRegionalCard(s,species.unlock.card))missing.push(`先登记发现 ${species.unlock.card}。`);
    if(!fullMethod(s,recipe))missing.push('先补全这份地方做法。');
  }
  return {recipe,species,met:missing.length===0,missing:[...new Set(missing)],requirementId:`${recipeId}:gate`};
}

function executableUnknown(s,recipeId){
  const info=regionalRecipeInfo(s,recipeId,{entry:true}),{recipe,species}=info;
  return info.met&&!fullMethod(s,recipe)&&s.expansion.methods.directions.includes(recipeId)&&species.unlock.identifiedMaterials.every(id=>materialIdentified(s,id))&&(!species.unlock.card||hasRegionalCard(s,species.unlock.card));
}

export function regionalMethodInfo(s,recipeId){
  const info=regionalRecipeInfo(s,recipeId),full=fullMethod(s,info.recipe),direction=s.expansion.methods.directions.includes(recipeId);
  return {...info,direction,full,canStudy:!full&&direction&&executableUnknown(s,recipeId)&&!!rank(s,'OBS-4'),studyCost:full?0:rank(s,'OBS-S')?50:100,freeProgress:s.expansion.methods.freeProgress[info.species.region]??{count:0,targetId:null}};
}

export function identifyMaterial(s,id){
  const material=regionalMaterial(id);
  if(!material||!REGIONAL_RELEASE.materials.includes(id))throw Error('这份标本尚未开放。');
  assertNewOperation('region',material.region);
  if(!hasRegionalCard(s,material.specimen))throw Error('先在寻访中找到这份标本。');
  if(materialIdentified(s,id))return {id,identified:false,directions:[]};
  s.expansion.discovery.identified[id]=nextRegionalFact(s);
  const directions=refreshRegionalDirections(s,material.region);
  const progress=s.expansion.methods.freeProgress[material.region]??={count:0,targetId:null};
  if(progress.targetId===null)progress.targetId=directions.find(recipeId=>executableUnknown(s,recipeId))??null;
  return {id,identified:true,directions};
}

export function refreshRegionalDirections(s,regionId){
  const added=[];
  for(const r of REGIONAL.recipes){
    if(!REGIONAL_RELEASE.methods.includes(r.id))continue;
    const species=resolveSpecies(r.key),u=species.unlock;
    if(species.region!==regionId||!u.identifiedMaterials.every(id=>materialIdentified(s,id))||u.firstSpecimenForOldOnly&&!s.expansion.regions.introSpecimenDone.includes(regionId)||u.card&&!hasRegionalCard(s,u.card))continue;
    if(!s.expansion.methods.directions.includes(r.id)){s.expansion.methods.directions.push(r.id);added.push(r.id);}
  }
  for(const alternative of REGIONAL.alternatives){
    if(alternative.region!==regionId||!REGIONAL_RELEASE.alternatives.includes(alternative.id)||!hasRegionalCard(s,alternative.unlock))continue;
    for(const field of ['directions','full'])if(!s.expansion.methods[field].includes(alternative.id))s.expansion.methods[field].push(alternative.id);
  }
  return added;
}

export function pinRegionalMethod(s,recipeId){
  const {species}=regionalRecipeInfo(s,recipeId,{entry:true});
  assertNewOperation('region',species.region);
  if(!executableUnknown(s,recipeId))throw Error('请选择已有方向且当前可做的未知地方做法。');
  const progress=s.expansion.methods.freeProgress[species.region]??={count:0,targetId:null};
  progress.targetId=recipeId;
  return {recipeId,count:progress.count};
}

export function regionalMethodPlan(s,regionId){
  const progress=s.expansion.methods.freeProgress[regionId]??{count:0,targetId:null};
  const eligibleIds=REGIONAL.recipes.filter(r=>REGIONAL_RELEASE.methods.includes(r.id)&&resolveSpecies(r.key).region===regionId&&executableUnknown(s,r.id)).map(r=>r.id);
  const targetId=eligibleIds.includes(progress.targetId)?progress.targetId:null;
  if(targetId){eligibleIds.splice(eligibleIds.indexOf(targetId),1);eligibleIds.unshift(targetId);}
  // A missing pin freezes progress; this is not a stockpiled research coupon.
  return {eligibleIds:targetId?eligibleIds:[],targetId,countBefore:progress.count};
}

export function settleRegionalMethod(s,regionId,ticket){
  if(!ticket.targetId)return null;
  const recipeId=ticket.eligibleIds.find(id=>!fullMethod(s,resolveRecipeId(id)));
  if(!recipeId)return null;
  const progress=s.expansion.methods.freeProgress[regionId]??={count:0,targetId:ticket.targetId};
  if(ticket.countBefore<2){progress.count=ticket.countBefore+1;return null;}
  s.expansion.methods.full.push(recipeId);progress.count=0;
  nextRegionalFact(s);
  const next=REGIONAL.recipes.find(r=>REGIONAL_RELEASE.methods.includes(r.id)&&resolveSpecies(r.key).region===regionId&&executableUnknown(s,r.id));
  progress.targetId=next?.id??null;
  return recipeId;
}

export function studyRegionalMethod(s,recipeId,now){
  const info=regionalMethodInfo(s,recipeId);
  if(info.full)return {recipeId,cost:0};
  assertNewOperation('region',info.species.region);
  if(!info.canStudy)throw Error('先取得可做方向并学习观察分支的配方研读。');
  if(s.cp<info.studyCost)throw Error(`CP还差${info.studyCost-s.cp}；也可继续寻访免费补全。`);
  s.cp-=info.studyCost;s.expansion.methods.full.push(recipeId);nextRegionalFact(s);
  return {recipeId,cost:info.studyCost};
}

export function prepareRegionalRecipe(s,recipeId){
  const info=regionalRecipeInfo(s,recipeId);
  assertNewOperation('region',info.species.region);
  if(!info.met)throw Error(info.missing[0]);
  for(const ingredient of info.recipe.ingredients)if((s.ingredients[ingredient.id]??0)<ingredient.quantity)throw Error(`先补齐${materialById[ingredient.id]?.title_zh_CN??ingredient.id}。`);
  s.expansion.prepareMode={kind:'regional',recipeId};s.egg=info.recipe.egg;s.selected=info.recipe.ingredients.map(i=>i.id);
  delete s.events.seasonalRecipe;delete s.progress.replicate;
  return info;
}

export function regionalAlternativeInfo(s,recipeId){
  const recipe=resolveRecipeId(recipeId);
  if(!recipe||recipe.mode!=='local-alternative')throw Error('没有找到这份地方替代做法。');
  const missing=[...regionInfo(s,recipe.region).missing],egg=Number(recipe.target.split(':')[0]);
  if(!REGIONAL_RELEASE.alternatives.includes(recipeId))missing.push('这份地方替代做法尚未开放。');
  if(!s.expansion.methods.full.includes(recipeId)||!alternativeUnlocked(s,recipe))missing.push(recipe.unlock.startsWith('PJ-')?`先完成项目「${CONTENT_TEXT[recipe.unlock]?.name??recipe.unlock}」。`:`先登记发现 ${recipe.unlock}。`);
  if(s.kitchenLevel<recipe.kitchenLevel)missing.push(`厨房 Lv.${recipe.kitchenLevel+1}`);
  if((s.toolLevels[recipe.toolId]??-1)<recipe.toolLevel)missing.push(`${LEGACY193.tools[1].find(t=>t.id===recipe.toolId)?.title_zh_CN??'厨具'} Lv.${recipe.toolLevel+1}`);
  if(egg===1&&!s.duck)missing.push('先开放鸭蛋。');
  const available=availableIngredientIds(s);
  for(const id of recipe.ingredients)if(id>=75&&!materialIdentified(s,id)||id<75&&!available.includes(id))missing.push(`${materialById[id]?.title_zh_CN??id}供货尚未开放。`);
  for(const id of recipe.replaces)if(!available.includes(id))missing.push(`原配方的${materialById[id]?.title_zh_CN??id}供货尚未开放。`);
  // Regional ingredients map positionally onto `replaces`; kept old ingredients
  // stay in the legacy matching view (ALT-T: 79+27 matches the old 3+27).
  const regionalIds=recipe.ingredients.filter(id=>id>=75),legacyMaterials=recipe.ingredients.map(id=>id>=75?recipe.replaces[regionalIds.indexOf(id)]:id);
  return {recipe,alternative:recipe,met:missing.length===0,missing,egg,materials:[...recipe.ingredients],legacyMaterials,requirementId:`${recipeId}:guarantee`};
}

export function prepareLocalAlternative(s,recipeId){
  const info=regionalAlternativeInfo(s,recipeId);
  if(!info.met)throw Error(info.missing[0]);
  for(const id of info.materials)if(!(s.ingredients[id]>0))throw Error(`先补齐${materialById[id]?.title_zh_CN??id}。`);
  s.expansion.prepareMode={kind:'local-alternative',recipeId};s.egg=info.egg;s.selected=[...info.materials];delete s.events.seasonalRecipe;delete s.progress.replicate;
  return info;
}
