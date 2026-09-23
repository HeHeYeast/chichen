// Read-only projection of one region for UI use. Raw author text never reaches
// the page directly: names, portraits and full recipes pass the unknown rules.
import {REGIONAL,REQUIREMENTS,CONTENT_TEXT,resolveSpecies,materialById} from './content-registry.js';
import {LEGACY193} from './legacy-content.js';
import {speciesDiscovered} from './species-state.js';
import {REGIONAL_RELEASE,regionInfo,hasRegionalCard,materialIdentified,teamMeets,regionalCompanion} from './region-model.js';
import {regionalRecipeInfo,regionalMethodInfo,regionalAlternativeInfo} from './regional-methods.js';
import {regionalCardGateMet} from './regional-exploration.js';
import {evaluate} from './requirements.js';

const TRAITS={portable:'便携',fruit:'果香',tea:'茶香',leaf:'叶形',grain:'谷物',salt:'盐晶',floral:'花香'};
const ENVIRONMENTS={yard:'菜园',water:'水边',wood:'林间'};
const FOCUS={specimen:'找标本',lore:'寻见闻',materials:'补材料'};
export const traitLabel=id=>TRAITS[id]??id;
export const environmentLabel=id=>ENVIRONMENTS[id]??id;
export const focusLabel=id=>FOCUS[id]??id;
const toolName=id=>LEGACY193.tools[1].find(t=>t.id===id)?.title_zh_CN??'厨具';
export const speciesCode=key=>{const [egg,id]=key.split(':').map(Number);return `${egg?'D':'C'}${String(id+1).padStart(3,'0')}`;};

// A regional material's name is known once its specimen was recorded; before
// that the page shows only "未辨认材料". Old materials are always public.
export function materialLabel(s,id){
  if(id<75)return materialById[id]?.title_zh_CN??String(id);
  const m=REGIONAL.materials.find(x=>x.id===id);
  return m&&(hasRegionalCard(s,m.specimen)||materialIdentified(s,id))?materialById[id].title_zh_CN:'未辨认材料';
}

// An unfound material is referred to by its specimen card title, never its name.
function identifyHint(s,id){
  const label=materialLabel(s,id);if(label!=='未辨认材料')return `先辨认${label}`;
  const m=REGIONAL.materials.find(x=>x.id===id);return `先找到并辨认「${CONTENT_TEXT[m.specimen]?.title??m.specimen}」的标本`;
}
export function releasedRegions(){return REGIONAL.regions.filter(r=>REGIONAL_RELEASE.regions.includes(r.id)).map(r=>r.id);}

export function cardView(s,card,companions=null){
  const text=CONTENT_TEXT[card.id]??{},found=hasRegionalCard(s,card.id);
  let gateMet,gateMissing=[];
  if(card.type==='specimen'){const m=REGIONAL.materials.find(x=>x.id===card.material),entry=regionalRecipeInfo(s,resolveSpecies(m.entrySpecies).recipeId,{entry:true});gateMet=entry.met;gateMissing=entry.missing;}
  else{gateMet=regionalCardGateMet(s,card);const gate=evaluate(REQUIREMENTS[card.gateRequirement],s);if(!gate.met)gateMissing=gate.missing.map(x=>x.replace(/已辨认材料(\d+)/,(_,id)=>identifyHint(s,Number(id))));}
  const team=[card.team.trait&&`${traitLabel(card.team.trait)}特征`,card.team.environment&&`适应${environmentLabel(card.team.environment)}`].filter(Boolean);
  return {id:card.id,type:card.type,placeId:card.placeId,focus:card.focus,found,title:text.title??card.id,hint:text.hint??'',
    result:found?text.result??'':null,next:found?text.next??'':null,gateMet,gateText:text.gate??'',
    gateMissing,
    team,teamMet:companions?teamMeets(card,companions):null,material:card.material};
}

export function materialView(s,material){
  const text=CONTENT_TEXT[material.stableId]??{},found=hasRegionalCard(s,material.specimen),identified=materialIdentified(s,material.id);
  return {id:material.id,specimenCard:material.specimen,found,identified,supplyOpen:identified,used:!!s.expansion.regions.materialUse?.[material.id],
    name:found||identified?text.name:'未辨认材料',specimenName:found?text.specimenName:null,recognition:found?text.recognition:null,
    lore:identified?text.lore:null,shop:identified?text.shop:null,price:identified?material.priceCP:null};
}

// Direction reveals code, egg, tool and first ingredient; the full method reveals
// the complete recipe; only an actual collection reveals the real name/portrait.
export function methodView(s,recipe){
  const species=resolveSpecies(recipe.key),info=regionalMethodInfo(s,recipe.id),text=CONTENT_TEXT[species.authorId]??{};
  const collected=speciesDiscovered(s,recipe.egg,species.id),direction=s.expansion.methods.directions.includes(recipe.id),full=info.full;
  const stage=collected?'collected':full?'full':direction?'direction':'unknown';
  const trial=s.expansion.trial[recipe.id]??{failedFullBatches:0,owed:false,attemptSeq:0};
  const ingredients=recipe.ingredients.map(i=>i.id);
  return {recipeId:recipe.id,key:recipe.key,code:speciesCode(recipe.key),egg:recipe.egg,stage,collected,direction,full,
    name:collected?species.title_zh_CN:null,description:collected?text.description:null,clue:direction||full||collected?text.clue:null,
    ornamental:!species.edible,tool:direction||full||collected?`${toolName(recipe.toolId)} Lv.${recipe.toolLevel+1}`:null,
    kitchen:full||collected?`厨房 Lv.${recipe.kitchenLevel+1}`:null,
    firstIngredient:direction||full||collected?materialLabel(s,ingredients[0]):null,
    ingredients:full||collected?ingredients.map(id=>materialLabel(s,id)):null,ingredientIds:full||collected?ingredients:null,
    met:info.met,missing:stage==='unknown'?[]:info.missing,canStudy:info.canStudy,studyCost:info.studyCost,pinned:info.freeProgress.targetId===recipe.id,
    trial:{failed:trial.failedFullBatches,owed:trial.owed,attempts:trial.attemptSeq},
    prepared:s.expansion.prepareMode?.recipeId===recipe.id};
}

export function alternativeView(s,alternative){
  const info=regionalAlternativeInfo(s,alternative.id),known=s.expansion.methods.full.includes(alternative.id);
  const [egg,id]=alternative.target.split(':').map(Number),target=resolveSpecies(alternative.target);
  return {id:alternative.id,known,name:known?CONTENT_TEXT[alternative.id]?.name??alternative.id:null,unlockCard:alternative.unlock,unlockName:CONTENT_TEXT[alternative.unlock]?.name??CONTENT_TEXT[alternative.unlock]?.title??alternative.unlock,
    target:alternative.target,targetCode:speciesCode(alternative.target),targetName:speciesDiscovered(s,egg,id)?target.title_zh_CN:null,
    tool:`${toolName(alternative.toolId)} Lv.${alternative.toolLevel+1}`,kitchen:`厨房 Lv.${alternative.kitchenLevel+1}`,
    ingredients:known?alternative.ingredients.map(x=>materialLabel(s,x)):null,met:info.met,missing:info.missing,
    prepared:s.expansion.prepareMode?.recipeId===alternative.id};
}

export function regionView(s,regionId,{members=[]}={}){
  const info=regionInfo(s,regionId),companions=members.map(regionalCompanion);
  const cards=REGIONAL.cards.filter(c=>c.region===regionId&&REGIONAL_RELEASE.cards.includes(c.id)).map(c=>cardView(s,c,companions));
  const materials=REGIONAL.materials.filter(m=>m.region===regionId&&REGIONAL_RELEASE.materials.includes(m.id)).map(m=>materialView(s,m));
  const methods=REGIONAL.recipes.filter(r=>REGIONAL_RELEASE.methods.includes(r.id)&&resolveSpecies(r.key).region===regionId).map(r=>methodView(s,r));
  const alternatives=REGIONAL.alternatives.filter(a=>a.region===regionId&&REGIONAL_RELEASE.alternatives.includes(a.id)).map(a=>alternativeView(s,a));
  const free=s.expansion.methods.freeProgress[regionId]??{count:0,targetId:null};
  const protection=s.expansion.cardProtection[regionId]??{specimen:0,lore:0};
  return {id:regionId,name:CONTENT_TEXT[regionId]?.name??regionId,met:info.met,missing:info.missing,places:info.places,opened:info.opened,
    introDone:s.expansion.regions.introSpecimenDone.includes(regionId),cards,materials,methods,alternatives,free,protection,
    counts:{cards:cards.filter(c=>c.found).length,collected:methods.filter(m=>m.collected).length,total:methods.length}};
}
