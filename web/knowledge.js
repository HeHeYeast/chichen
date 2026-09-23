import {RECIPE_CATALOG,recipePaths,recipeId,recipePathInfo} from './recipe-book.js';
import {speciesDiscovered} from './species-state.js';
import {rank,checkedIncome} from './progression.js';
import {RULES} from './integration-data.js';
import {SPECIES_CLUES as AUTHORED_CLUES} from './content-registry.js';
import {GAME_DATA} from './content-pack.js';
import {availableIngredientIds} from './ingredient-unlocks.js';
export const speciesCode=key=>{const [egg,id]=key.split(':').map(Number);return (egg?'D':'C')+String(id+1).padStart(3,'0');};
export const factId=(r,level)=>level===1?r.key+':L1':recipeId(r)+':L'+level;
export const hasFact=(s,r,level)=>s.progress.knowledge.facts.includes(factId(r,level))||s.progress.knowledge.recipes.includes(recipeId(r))||speciesDiscovered(s,r.egg,r.id);
export function accessiblePath(s,key,now=Date.now()){
  return recipePaths(key).map(r=>recipePathInfo(s,r,now)).sort((a,b)=>a.conditions.filter(c=>!c.met).length-b.conditions.filter(c=>!c.met).length||a.missing.length-b.missing.length||a.ingredients.length-b.ingredients.length)[0]??null;
}
export function observationInfo(s,key,now=Date.now()){
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
export function readObservation(s,key,now=Date.now()){
  const info=observationInfo(s,key,now);if(!info)throw Error('编号无效');
  for(let l=1;l<=5;l++)if(info.levels[l]){const f=factId(info.path,l);if(!s.progress.knowledge.facts.includes(f))s.progress.knowledge.facts.push(f);}
  return true;
}
export function studyRecipe(s,key,now=Date.now()){
  const info=observationInfo(s,key,now);if(!info)throw Error('编号无效');
  if(info.full)return {cost:0};
  if(!info.canStudy)throw Error('先学习观察分支的配方研读');
  if(s.cp<info.studyCost)throw Error('CP还差'+(info.studyCost-s.cp)+'。可以先出售伙伴，或继续寻访免费线索。');
  checkedIncome(s,0);s.cp-=info.studyCost;
  for(const r of recipePaths(key))if(!s.progress.knowledge.recipes.includes(recipeId(r)))s.progress.knowledge.recipes.push(recipeId(r));
  return {cost:info.studyCost};
}
export function prepareKnownPath(s,id,now=Date.now()){
  const path=RECIPE_CATALOG.find(r=>recipeId(r)===id);if(!path)throw Error('路径无效');
  const r=recipePathInfo(s,path,now);
  if(!speciesDiscovered(s,r.egg,r.id)&&!s.progress.knowledge.recipes.includes(id))throw Error('尚未学习这份配方');
  if(!r.ready)throw Error(r.special?'按特殊变化说明尝试':r.conditions.find(c=>!c.met)?.label??'请先补齐材料');
  s.egg=r.egg;s.selected=[...r.ingredients];delete s.events.seasonalRecipe;delete s.expansion?.prepareMode;
  if(r.kind==='seasonal'&&speciesDiscovered(s,r.egg,r.id))s.events.seasonalRecipe=r.key;
  return r;
}
export function clueCandidates(s,route,now=Date.now()){
  const unlocked=availableIngredientIds(s),extra=route.id==='water'?['1:0']:route.id==='wood'?['1:39','1:40','1:41','0:10']:[];
  if(!route.pool.some(id=>unlocked.includes(id)))return [];
  const candidates=[];
  for(const r of RECIPE_CATALOG){
    if(r.kind==='change'||r.kind==='sign'||r.kind==='gift'||speciesDiscovered(s,r.egg,r.id)||(!extra.includes(r.key)&&!r.ingredients.some(id=>route.pool.includes(id)&&unlocked.includes(id))))continue;
    let eligible=false;
    for(let d=0;d<=7;d++){
      const date=new Date(now);date.setDate(date.getDate()+d);
      if(recipePathInfo(s,r,date.getTime()).conditions.every(c=>c.met)){eligible=true;break;}
    }
    if(!eligible)continue;
    const level=!hasFact(s,r,1)?1:!hasFact(s,r,2)?2:0;
    if(level)candidates.push({key:r.key,recipeId:recipeId(r),level,fact:factId(r,level)});
  }
  const sort=(a,b)=>a.level-b.level||a.key.split(':')[0]-b.key.split(':')[0]||Number(a.key.split(':')[1])-Number(b.key.split(':')[1])||a.recipeId.localeCompare(b.recipeId);
  return candidates.sort(sort).filter((c,i,a)=>a.findIndex(x=>x.fact===c.fact)===i);
}
export function clueStillNew(s,c){return !speciesDiscovered(s,...c.key.split(':').map(Number))&&!s.progress.knowledge.facts.includes(c.fact)&&!s.progress.knowledge.recipes.includes(c.recipeId);}
export function ingredientName(id){return GAME_DATA.tools[2][id].title_zh_CN;}
