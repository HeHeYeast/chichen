import {RECIPE_CATALOG,recipePaths,recipeId,recipePathInfo} from './recipe-book.js';
import {speciesDiscovered} from './species-state.js';
import {rank,checkedIncome} from './progression.js';
import {RULES} from './integration-data.js';
import {SPECIES_CLUES as AUTHORED_CLUES} from './content-registry.js';
import {GAME_DATA} from './content-pack.js';
import {ROUTE_REGION,regionPartners,clueRegionOf,REGION_SHORT} from './clue-regions.js';
import {isRegionalKey,regionalRows,regionalRowById,regionalPath,regionalHeld,regionalGate,regionalLevels,nextRegionalLayer,regionalStudy,regionalRuntimeId} from './regional-clues.js';
import {studyRegionalMethod,prepareRegionalRecipe} from './regional-methods.js';
export const speciesCode=key=>{const [egg,id]=key.split(':').map(Number);return (egg?'D':'C')+String(id+1).padStart(3,'0');};
export const factId=(r,level)=>level===1?r.key+':L1':recipeId(r)+':L'+level;
export const hasFact=(s,r,level)=>r.kind==='regional'?!!regionalLevels(s,r.key)?.[level-1]:s.progress.knowledge.facts.includes(factId(r,level))||s.progress.knowledge.recipes.includes(recipeId(r))||speciesDiscovered(s,r.egg,r.id);
// The regional partners (loop batch 4) answer every question below through regional-clues.js: one recipe each, the 方向
// and the complete method on the same five layers.
export function accessiblePath(s,key,now=Date.now()){
  if(isRegionalKey(key))return regionalPath(s,key,now);
  return recipePaths(key).map(r=>recipePathInfo(s,r,now)).sort((a,b)=>a.conditions.filter(c=>!c.met).length-b.conditions.filter(c=>!c.met).length||a.missing.length-b.missing.length||a.ingredients.length-b.ingredients.length)[0]??null;
}
export function observationInfo(s,key,now=Date.now()){
  if(isRegionalKey(key))return regionalObservation(s,key,now);
  const r=accessiblePath(s,key,now);if(!r)return null;
  const known=speciesDiscovered(s,r.egg,r.id),studied=s.progress.knowledge.recipes.includes(recipeId(r));
  const levels=[false,!!rank(s,'OBS-1'),!!rank(s,'OBS-1'),!!rank(s,'OBS-3'),!!rank(s,'OBS-3'),!!rank(s,'OBS-3')];
  const can=l=>known||studied||levels[l]||hasFact(s,r,l),details=[];
  if(can(2))details.push(r.special?'这是后续特殊变化或交换':`${r.egg?'鸭蛋':'鸡蛋'} · ${r.toolName} Lv.${r.minLevel+1} 起`,...r.conditions.filter(c=>c.kind==='calendar'||c.label.startsWith('开火时段')).map(c=>(c.met?'已满足：':'尚缺：')+c.label));
  if(can(3)&&!r.special)details.push(r.ingredients.length?'第一味：'+r.ingredientNames[0]:'无需材料');
  if(can(4)&&r.ingredients.length>1){const group=Object.entries(RULES.ingredientFlavorGroups).find(([,ids])=>ids.includes(r.ingredients[1]))?.[0];details.push('第二味类别：'+group);}
  if(can(5))details.push(...r.conditions.map(c=>(c.met?'已满足：':'尚缺：')+c.label),'缺少材料 '+r.missing.length+' 份');
  const full=known||studied;
  return {key,code:speciesCode(key),known,name:known?r.name:null,silhouette:can(1),clue:AUTHORED_CLUES[key],details,
    full,paths:full?recipePaths(key).map(p=>recipePathInfo(s,p,now)):[],studyCost:full?0:rank(s,'OBS-S')?50:100,canStudy:!full&&!!rank(s,'OBS-4'),path:r,levels};
}
function regionalObservation(s,key,now){
  const r=regionalPath(s,key,now),levels=[false,...regionalLevels(s,key)],known=speciesDiscovered(s,r.egg,r.id),full=levels[5],gate=regionalGate(s,key),study=regionalStudy(s,key),details=[];
  if(levels[2])details.push(`${r.egg?'鸭蛋':'鸡蛋'} · ${r.toolName} Lv.${r.minLevel+1} 起`);
  if(levels[3])details.push('第一味：'+r.ingredientNames[0]);
  if(levels[4]&&r.ingredients.length>1){const group=Object.entries(RULES.ingredientFlavorGroups).find(([,ids])=>ids.includes(r.ingredients[1]))?.[0];if(group)details.push('第二味类别：'+group);}
  if(!gate.met)details.push('下一步：'+gate.step.text);
  return {key,code:speciesCode(key),known,name:known?r.name:null,silhouette:levels[1],clue:gate.met||known?AUTHORED_CLUES[key]:gate.step.hint||gate.step.text,details,
    full,paths:full?[r]:[],studyCost:full?0:study.studyCost,canStudy:!full&&study.canStudy,path:r,levels,regional:true};
}
// What the player's clues (read from 寻访, or given by the current observation skills) already say about one recipe.
// The same levels observationInfo uses: 2 = cookware and level, 3 = first seasoning, 4 = flavour group of the second.
export function clueReach(s,key,now=Date.now()){
  if(isRegionalKey(key)){
    const r=regionalPath(s,key,now),levels=regionalLevels(s,key),full=speciesDiscovered(s,r.egg,r.id)||regionalHeld(s,key);
    const group=levels[3]&&r.ingredients.length>1?Object.entries(RULES.ingredientFlavorGroups).find(([,ids])=>ids.includes(r.ingredients[1]))?.[0]??null:null;
    return {path:r,full,levels,tool:levels[1],first:levels[2],group,count:r.ingredients.length,regional:true};
  }
  const r=accessiblePath(s,key,now);if(!r)return null;
  const full=speciesDiscovered(s,r.egg,r.id)||s.progress.knowledge.recipes.includes(recipeId(r));
  const levels=[false,!!rank(s,'OBS-1'),!!rank(s,'OBS-1'),!!rank(s,'OBS-3'),!!rank(s,'OBS-3'),!!rank(s,'OBS-3')];
  const can=l=>full||levels[l]||hasFact(s,r,l);
  const group=can(4)&&r.ingredients.length>1?Object.entries(RULES.ingredientFlavorGroups).find(([,ids])=>ids.includes(r.ingredients[1]))?.[0]??null:null;
  return {path:r,full,levels:[1,2,3,4,5].map(can),tool:can(2),first:can(3)&&!r.special,group,count:r.ingredients.length};
}
// The exact recipe the player holds for a partner not met yet (线索册「已解锁」): a studied recipe, or clues that leave
// nothing open — the cookware and the first seasoning of a recipe that uses at most one (or the note that it needs none).
// Null while something is still to be worked out. Special changes, shrine signs and gifts have no recipe to hold.
export function knownRecipe(s,key,now=Date.now()){
  const [egg,id]=key.split(':').map(Number);if(speciesDiscovered(s,egg,id))return null;
  if(isRegionalKey(key))return regionalHeld(s,key)?regionalPath(s,key,now):null;
  const rank=r=>r.conditions.filter(c=>!c.met).length;
  const studied=recipePaths(key).filter(r=>!['change','sign','gift'].includes(r.kind)&&s.progress.knowledge.recipes.includes(recipeId(r))).map(r=>recipePathInfo(s,r,now)).sort((a,b)=>rank(a)-rank(b));
  if(studied.length)return studied[0];
  const c=clueReach(s,key,now);if(!c||['change','sign','gift'].includes(c.path.kind))return null;
  return c.tool&&c.first&&c.path.ingredients.length<=1?c.path:null;
}
// 线索册「追踪」: the one partner not met yet that 下一锅「推荐」 works towards first. Saved as progress.knowledge.tracked, a species
// key such as "0:121"; absent when nothing is tracked, so old saves need no migration. A tracked partner that has since been met
// stays in the save until the next choice and simply counts as nothing tracked (trackedFound names it once for the UI).
// built on first use: knowledge.js loads inside import cycles, before the recipe catalogue is ready
let trackable=null;
const TRACKABLE={has:key=>(trackable??=new Set([...RECIPE_CATALOG.filter(r=>!['change','sign','gift'].includes(r.kind)).map(r=>r.key),...regionalRows().map(r=>r.key)])).has(key)};
export const trackableKey=key=>TRACKABLE.has(key);
export function trackedKey(s){
  const key=s.progress?.knowledge?.tracked;if(typeof key!=='string'||!TRACKABLE.has(key))return null;
  const [egg,id]=key.split(':').map(Number);return speciesDiscovered(s,egg,id)?null:key;
}
export function trackedFound(s){
  const key=s.progress?.knowledge?.tracked;if(typeof key!=='string'||!TRACKABLE.has(key))return null;
  const [egg,id]=key.split(':').map(Number);return speciesDiscovered(s,egg,id)?key:null;
}
// key null clears the choice; tracking another partner replaces it (one at a time).
export function trackPartner(s,key){
  const k=s.progress.knowledge;
  if(key===null){delete k.tracked;return null;}
  if(!TRACKABLE.has(key))throw Error('这只伙伴不能追踪');
  const [egg,id]=key.split(':').map(Number);if(speciesDiscovered(s,egg,id))throw Error('已经认识这只伙伴了');
  k.tracked=key;return key;
}
export function readObservation(s,key,now=Date.now()){
  const info=observationInfo(s,key,now);if(!info)throw Error('编号无效');
  for(let l=1;l<=5;l++)if(info.levels[l]){const f=factId(info.path,l);if(!s.progress.knowledge.facts.includes(f))s.progress.knowledge.facts.push(f);}
  return true;
}
export function studyRecipe(s,key,now=Date.now()){
  // a regional partner's 研读 is the region's own (it needs the 方向 first, and writes the complete method)
  if(isRegionalKey(key)){const r=studyRegionalMethod(s,regionalRuntimeId(key),now);return {cost:r.cost};}
  const info=observationInfo(s,key,now);if(!info)throw Error('编号无效');
  if(info.full)return {cost:0};
  if(!info.canStudy)throw Error('先学习观察分支的配方研读');
  if(s.cp<info.studyCost)throw Error('CP还差'+(info.studyCost-s.cp)+'。可以先出售伙伴，或继续寻访，免费找线索。');
  checkedIncome(s,0);s.cp-=info.studyCost;
  for(const r of recipePaths(key))if(!s.progress.knowledge.recipes.includes(recipeId(r)))s.progress.knowledge.recipes.push(recipeId(r));
  return {cost:info.studyCost};
}
export function prepareKnownPath(s,id,now=Date.now()){
  const regional=regionalRowById(id);if(regional){prepareRegionalRecipe(s,regional.runtimeId);return regionalPath(s,regional.key,now);}
  const path=RECIPE_CATALOG.find(r=>recipeId(r)===id);if(!path)throw Error('路径无效');
  const r=recipePathInfo(s,path,now);
  if(!speciesDiscovered(s,r.egg,r.id)&&!s.progress.knowledge.recipes.includes(id))throw Error('尚未学习这份配方');
  if(!r.ready)throw Error(r.special?'按特殊变化说明尝试':r.conditions.find(c=>!c.met)?.label??'请先补齐材料');
  s.egg=r.egg;s.selected=[...r.ingredients];delete s.events.seasonalRecipe;delete s.expansion?.prepareMode;
  if(r.kind==='seasonal'&&speciesDiscovered(s,r.egg,r.id))s.events.seasonalRecipe=r.key;
  return r;
}
// 寻访 reads up to the second seasoning's flavour group; the exact second seasoning is left to trying (or 研读), and the
// 「其余条件」 layer comes only from 辨味笔记 or 研读.
export const TRIP_LAYERS=4;
// 「其余条件」 worth telling: a holiday or season to cook in, or a time of day. Only these make layer 5 something an
// order's 情报 (or later a special find, an event, a regular's story) can reveal; ordinary trips never read layer 5.
export const SPECIAL_CONDITION=c=>c.kind==='calendar'||String(c.label??'').startsWith('开火时段');
export const specialConditionText=path=>path.conditions.filter(SPECIAL_CONDITION).map(c=>String(c.label).split(' · ')[0]).join('、');
const SPECIAL=new Set(['change','sign','gift']);
// The next layer a trip can tell about a partner not met yet: the shallowest one the player does not know by any means
// (clue facts, observation skills, 研读), skipping the second seasoning's group when the recipe has no second seasoning.
// Null once the recipe is held (studied, or clues that leave nothing open) or the trip layers are all read.
// deep: also layer 5 when the recipe has a special condition (order 情报); trips stop at TRIP_LAYERS.
// A regional partner (regional-clues.js): nothing before its 方向, then its own layers; its fifth layer is the complete
// method, which a trip in its region reads (method) but an order's 情报 (deep) does not.
export function nextClueLayer(s,key,now=Date.now(),{deep=false,method=true}={}){
  const [egg,id]=key.split(':').map(Number);if(speciesDiscovered(s,egg,id))return null;
  if(isRegionalKey(key))return nextRegionalLayer(s,key,now,{method:method&&!deep});
  const view=s.egg===egg?s:{...s,egg};
  if(knownRecipe(view,key,now))return null;
  const c=clueReach(view,key,now);if(!c||c.full||SPECIAL.has(c.path.kind))return null;
  for(let level=1;level<=(deep?5:TRIP_LAYERS);level++){
    if(c.levels[level-1]||(level===4&&c.path.ingredients.length<2)||(level===5&&!c.path.conditions.some(SPECIAL_CONDITION)))continue;
    return {level,path:c.path,known:c.levels.filter(Boolean).length};
  }
  return null;
}
// Clue tickets for one trip: every partner whose main clue region (clue-regions.js) lies on this route and still has a
// layer to read, one ticket each for its next layer. The tracked partner leads; then partners that could be cooked now,
// the ones read furthest first, so trips keep working one investigation through instead of scattering.
// method: false leaves out a regional partner's complete method (OBS-5's clue on a first meeting is not a trip).
export function clueCandidates(s,route,now=Date.now(),{method=true}={}){
  const region=ROUTE_REGION[route.id];if(!region)return [];
  const tracked=trackedKey(s),rows=[];
  for(const key of regionPartners(region)){
    if(key.startsWith('1:')&&!s.duck)continue;
    const next=nextClueLayer(s,key,now,{method});if(!next)continue;
    const r=next.path;
    rows.push({key,recipeId:recipeId(r),level:next.level,fact:factId(r,next.level),lead:key===tracked,ready:r.conditions.every(c=>c.met),known:next.known});
  }
  const order=key=>key.split(':').map(Number);
  rows.sort((a,b)=>b.lead-a.lead||b.ready-a.ready||b.known-a.known||order(a.key)[0]-order(b.key)[0]||order(a.key)[1]-order(b.key)[1]);
  return rows.map(({key,recipeId,level,fact})=>({key,recipeId,level,fact}));
}
// A ticket still tells something new: the partner is not met, and neither a clue fact, 研读 nor an observation skill has
// told this layer since the trip left.
export function clueStillNew(s,c){
  if(speciesDiscovered(s,...c.key.split(':').map(Number)))return false;
  if(isRegionalKey(c.key))return !regionalLevels(s,c.key)[c.level-1];
  if(s.progress.knowledge.facts.includes(c.fact)||s.progress.knowledge.recipes.includes(c.recipeId))return false;
  return !rank(s,c.level<=2?'OBS-1':'OBS-3');
}
// The one clue a returned trip gives: the first frozen ticket that is still new, or, when every ticket has gone stale (a
// trip that left before these rules, or layers learned while it was away), the route's first current candidate.
// Null when there is nothing left to read on this route.
export function tripClue(s,trip,now=Date.now()){
  const fresh=(trip.clueOrder??[]).find(c=>clueStillNew(s,c));
  return fresh??clueCandidates(s,{id:trip.routeId},now)[0]??null;
}
// What a clue ticket tells, in the words the 线索册 uses.
export function clueFactText(c){
  const path=RECIPE_CATALOG.find(r=>recipeId(r)===c.recipeId)??regionalRowById(c.recipeId);if(!path)return '';
  if(path.kind==='regional'&&c.level===5)return '记下了完整做法';
  if(c.level===1)return '看清了它的剪影';
  if(c.level===2)return `要用${GAME_DATA.tools[1][path.toolId]?.title_zh_CN??'厨具'} Lv.${path.minLevel+1}`;
  if(c.level===3)return path.ingredients.length?`第一味是「${ingredientName(path.ingredients[0])}」`:'不用放调味料';
  if(c.level===4){const group=Object.entries(RULES.ingredientFlavorGroups).find(([,ids])=>ids.includes(path.ingredients[1]))?.[0];return `第二味是${group}类`;}
  return c.detail?`其余条件：${c.detail}`:'其余条件';
}
// The region whose trips read this partner's next clue, with its short name.
export function clueRegion(key){const x=clueRegionOf(key);return x?{id:x.region,name:REGION_SHORT[x.region]}:null;}
export function ingredientName(id){return GAME_DATA.tools[2][id].title_zh_CN;}
