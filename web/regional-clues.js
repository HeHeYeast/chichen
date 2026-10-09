import {recipeChance,chanceInBatch} from './hatch-probability.js';
// 地区伙伴并入线索册 (loop batch 4, 2026-10-07): the 48 regional partners are investigated like every other partner — the
// same 调查 x/5, main region (their own), 追踪, 去寻访 / 去制作 and 研读 — instead of a second system of 方向, 完整方法,
// 免费 3 趟 and a separate 研读 on the region page. What their old states mean on the five layers:
//   1 剪影      OBS-1, or a trip.
//   2 厨具      OBS-1, or the 方向.
//   3 第一味    the 方向 (it names the cookware and the first seasoning).
//   4 第二味类别 a trip, or OBS-3 once the 方向 is known.
//   5 完整做法  the old 完整方法 — the recipe held (5/5): a trip in its own region reads it (the regional speciality that
//              replaces the 「免费 3 趟」: an ordinary partner's trips stop at the second seasoning's group), or 研读; a
//              one-seasoning recipe is complete with its 方向, as before.
// Before its 方向 a regional partner waits on its region's own steps (gate): the region open, duck eggs, the first trip
// there (入门标本), its specimen found and 辨认, or a story card. A trip step is what 寻访 does — for the tracked partner it
// is sure to come home (regional-exploration.js sureCard) —; 辨认 is one free tap. Its riddle names the place and the
// material, so it shows from the 方向 on, as on the old region page.
// The trial is unchanged (batch-plan.js): every batch 25%, sure by the fourth after three without it; once met, one a batch.
// Read-only, except convertFreeProgress through regional-methods.js.
import {REGIONAL,CONTENT_TEXT,REQUIREMENTS,resolveSpecies} from './content-registry.js';
import {REGIONAL_RECIPE_ROWS,recipePathInfo,recipeId} from './recipe-book.js';
import {speciesDiscovered} from './species-state.js';
import {rank} from './progression.js';
import {evaluate} from './requirements.js';
import {REGIONAL_RELEASE,regionInfo,hasRegionalCard,materialIdentified,regionalCard,regionalMaterial} from './region-model.js';
import {regionalRecipeInfo,regionalFullMethod,regionalMethodInfo} from './regional-methods.js';

const SHORT={V:'谷地',R:'溪岸',T:'茶坡',B:'风湾'};
// built on first use: this module loads inside import cycles, before the content data is ready
let rows=null;
const table=()=>rows??=new Map(REGIONAL_RECIPE_ROWS.filter(r=>REGIONAL_RELEASE.methods.includes(r.runtimeId)).map(r=>[r.key,r]));
export const regionalRow=key=>table().get(key)??null;
export const isRegionalKey=key=>table().has(key);
export const regionalRows=egg=>[...table().values()].filter(r=>egg==null||r.egg===egg);
export const regionalRowById=id=>[...table().values()].find(r=>recipeId(r)===id||r.runtimeId===id)??null;
const factOf=(r,level)=>level===1?r.key+':L1':recipeId(r)+':L'+level;
const discovered=(s,r)=>speciesDiscovered(s,r.egg,r.id);
export const regionOfKey=key=>resolveSpecies(key)?.region??null;
const cardTitle=id=>CONTENT_TEXT[id]?.title??id;

// The recipe as a catalogue path, with the ordinary conditions only (kitchen, duck eggs, cookware level).
export function regionalPath(s,key,now=Date.now()){const r=regionalRow(key);return r?recipePathInfo(s,r,now,{gates:false}):null;}
export const regionalHeld=(s,key)=>{const r=regionalRow(key);return !!r&&regionalFullMethod(s,r.runtimeId);};

// The first step still between the partner and its 方向, or met. step: {kind, text, regionId, trip?, blocked[]}; a trip
// step names where 寻访 finds it ({placeId, focus, cardId} — the place and direction of that card; intro: any trip there).
// blocked: what keeps that step from happening now, in the words the region page uses.
export function regionalGate(s,key){
  const r=regionalRow(key);if(!r)return null;
  const species=resolveSpecies(key),u=species.unlock,regionId=species.region,area=regionInfo(s,regionId),name=SHORT[regionId];
  const step=(kind,text,extra={})=>({met:false,step:{kind,text,regionId,blocked:[],...extra}});
  if(!area.met)return step('region',`${name}还没开放`,{blocked:area.missing.map(m=>`${name}开放还需${m}`)});
  if(r.egg===1&&!s.duck)return step('duck','先开放鸭蛋',{blocked:['先开放鸭蛋']});
  const entryBlocked=m=>{const material=regionalMaterial(m);const info=material?regionalRecipeInfo(s,resolveSpecies(material.entrySpecies).recipeId,{entry:true}):null;return info?.met?[]:info?.missing??[];};
  if(u.firstSpecimenForOldOnly&&!s.expansion.regions.introSpecimenDone.includes(regionId)){
    // the first trip there brings an entry specimen the kitchen can try now; with none ready it brings none
    const ready=REGIONAL.materials.some(m=>m.region===regionId&&!hasRegionalCard(s,m.specimen)&&!entryBlocked(m.id).length);
    return step('intro',`第一次去${name}寻访`,{trip:{placeId:null,focus:'specimen',cardId:null},blocked:ready?[]:entryBlocked(REGIONAL.materials.find(m=>m.region===regionId)?.id)});
  }
  for(const id of u.identifiedMaterials){
    if(materialIdentified(s,id))continue;
    const m=regionalMaterial(id),card=regionalCard(m.specimen);
    if(hasRegionalCard(s,m.specimen))return step('identify',`辨认带回的「${CONTENT_TEXT[m.stableId]?.name??'标本'}」`,{materialId:id});
    return step('specimen',`在${name}找到「${cardTitle(card.id)}」`,{materialId:id,trip:{placeId:card.placeId,focus:'specimen',cardId:card.id},hint:CONTENT_TEXT[card.id]?.hint??'',blocked:entryBlocked(id)});
  }
  if(u.card&&!hasRegionalCard(s,u.card)){
    const card=regionalCard(u.card),gate=REQUIREMENTS[card.gateRequirement],e=gate&&gate.kind!=='unavailable'&&gate.kind!=='compiled'?evaluate(gate,s):{met:false,missing:['还没开放']};
    return step('card',`在${name}记下「${cardTitle(card.id)}」`,{trip:{placeId:card.placeId,focus:card.focus,cardId:card.id},hint:CONTENT_TEXT[card.id]?.hint??'',team:card.team,blocked:e.met?[]:e.missing});
  }
  return {met:true,step:null};
}

// What the player knows of each layer (1–5). The 方向 is the save's methods.directions; the complete method methods.full.
export function regionalLevels(s,key){
  const r=regionalRow(key);if(!r)return null;
  if(discovered(s,r)||regionalFullMethod(s,r.runtimeId))return [true,true,true,true,true];
  const facts=s.progress?.knowledge?.facts??[],fact=l=>facts.includes(factOf(r,l));
  const direction=s.expansion?.methods?.directions?.includes(r.runtimeId)??false,obs1=!!rank(s,'OBS-1'),obs3=!!rank(s,'OBS-3');
  return [obs1||fact(1),obs1||direction||fact(2),direction||fact(3),(obs3&&direction)||fact(4),false];
}

// The next layer a trip (or an order's 情报) can read: none before the 方向 (the gate comes first), then the shallowest
// unknown one; the complete method only when method is allowed (trips in its region yes, order 情报 no).
export function nextRegionalLayer(s,key,now=Date.now(),{method=true}={}){
  const r=regionalRow(key);if(!r||discovered(s,r))return null;
  if(!regionalGate(s,key).met)return null;
  const levels=regionalLevels(s,key);
  for(let level=1;level<=5;level++){
    if(levels[level-1]||(level===4&&r.ingredients.length<2)||(level===5&&!method))continue;
    return {level,path:regionalPath(s,key,now),known:levels.filter(Boolean).length};
  }
  return null;
}
// Every layer still to read, in order (for turning old free-method trips into layers).
export function regionalTripLayers(s,key){
  const r=regionalRow(key),levels=regionalLevels(s,key);if(!r||!levels)return [];
  return [1,2,3,4,5].filter(l=>!levels[l-1]&&!(l===4&&r.ingredients.length<2)).map(level=>({level,fact:factOf(r,level)}));
}

// The trial's odds as the kitchen will roll them: 25% a batch, sure once owed or after three full batches without it, and
// one a batch once met. left: batches at most until it is sure (the sure one included).
// inPot: the batch in the pot is this recipe's trial and is not collected yet, whether the 25% came up or not: collecting
// it is all that is left — starting another batch would throw it away (batch 5 playtest). The screens never tell which
// (device walkthrough: a card for a second trial right after firing one gave the roll away and looked like a failed start).
export function regionalTrial(s,key){
  const r=regionalRow(key);if(!r)return null;
  const t=s.expansion?.trial?.[r.runtimeId]??{failedFullBatches:0,owed:false},sure=discovered(s,r)||t.owed||t.failedFullBatches>=3;
  const plan=s.batch?.plan,inPot=!discovered(s,r)&&plan?.mode==='regional-trial'&&plan.recipeId===r.runtimeId&&!plan.finished&&!!s.batch.eggs?.some(e=>!e.collected);
  const perEgg=recipeChance({minLevel:r.minLevel,ingredients:r.ingredients});
  return {recipeId:r.runtimeId,chance:chanceInBatch(perEgg),perEgg,expected:24*perEgg,sure:false,inPot,failed:0,left:null};
}
// What stops a held regional recipe from being tried now: the region's own conditions (all but the complete method) and
// any seasoning that cannot be had.
export function regionalBlockers(s,key){
  const r=regionalRow(key);if(!r)return [];
  return regionalRecipeInfo(s,r.runtimeId).missing;
}
export const regionalStudy=(s,key)=>{const r=regionalRow(key);if(!r)return {canStudy:false,studyCost:100};const i=regionalMethodInfo(s,r.runtimeId);return {canStudy:i.canStudy,studyCost:i.studyCost};};
export const regionalRuntimeId=key=>regionalRow(key)?.runtimeId??null;
export const regionShort=id=>SHORT[id]??'';
