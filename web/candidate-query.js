import {regionalPreview} from './batch-plan.js';
import {originalRecipePlan} from './recipes.js';
import {expansionMatches} from './content-pack.js';
import {seasonalCandidates,plannedSeasonalRecipe} from './seasonal-pack.js';
import {recipeStateAt} from './holiday-calendar.js';
import {cookingIngredients} from './cooking-query.js';
import {speciesDiscovered} from './species-state.js';
import {RECIPE_CATALOG,recipePathInfo} from './recipe-book.js';
import {rank} from './progression.js';
import {speciesCode,ingredientName} from './knowledge.js';
export function cookingCandidates(s,toolId,now=Date.now()){
  if(s.expansion?.prepareMode){const {plan,candidates:raw}=regionalPreview(s,toolId,now);const candidates=raw.map(c=>({...c,egg:Number(c.key.split(':')[0]),id:Number(c.key.split(':')[1]),code:speciesCode(c.key),known:speciesDiscovered(s,...c.key.split(':').map(Number))}));return {ingredients:plan.materials,candidates,known:candidates.filter(c=>c.known).length,unknown:candidates.filter(c=>!c.known).length,nearby:[],blocked:[],changes:[plan.mode==='local-alternative'?'地方替代做法沿用原配方的候选与概率，不额外安排目标；地区新材料只替换原配料参与旧候选。':'地区做法成功时安排1只目标，其余23只来自原料理；请保持清洁并及时收取。'],mode:plan.mode,plan};}
  const ingredients=cookingIngredients(s),entries=new Map();
  const add=(egg,id,status,guaranteed=0)=>{const key=egg+':'+id;entries.set(key,{key,egg,id,code:speciesCode(key),status,guaranteed,known:speciesDiscovered(s,egg,id)});};
  if(toolId===8){
    if(s.egg!==0)throw Error('竹蒸笼目前只调理鸡蛋');
    for(const c of expansionMatches(s,s.egg,toolId,ingredients))add(0,c.id,c.ingredients.length?'guaranteed':'possible',c.ingredients.length?1:0);
  }else{
    const plan=originalRecipePlan(recipeStateAt(s,now),s.egg,toolId,ingredients,now);
    for(const id of new Set(plan.pool))add(s.egg,id,(s.egg===0&&[51,52].includes(id)||s.egg===1&&id===27)?'gate':'possible');
    for(const c of plan.gifts)add(s.egg,c.id,c.guaranteed?'guaranteed':'gate',c.guaranteed);
  }
  const planned=plannedSeasonalRecipe(s,toolId,ingredients);
  if(planned&&speciesDiscovered(s,planned.egg,planned.id))add(planned.egg,planned.id,'guaranteed',1);
  else for(const c of seasonalCandidates(s,toolId,ingredients))add(c.egg,c.id,'encounter');
  // Replacing index zero can remove the original gift or one steamer guarantee.
  if(planned)for(const e of entries.values())if(e.key!==planned.key&&e.guaranteed)e.guaranteed=0,e.status='possible';
  const nearby=[],blocked=[];
  for(const r of RECIPE_CATALOG){
    if(entries.has(r.key))continue;
    const info=recipePathInfo(s,r,now),known=speciesDiscovered(s,r.egg,r.id);
    if(s.progress?.knowledge.facts.some(f=>f.startsWith(r.key+':')||f.startsWith(r.key+'/'))||s.progress?.knowledge.recipes.some(f=>f.startsWith(r.key+'/')))blocked.push({key:r.key,code:speciesCode(r.key),reasons:[...(r.toolId!==toolId?['厨具']:[]),...(r.egg!==s.egg?['蛋种']:[]),...(r.ingredients.some(id=>!ingredients.includes(id))?['材料']:[]),...info.conditions.filter(c=>!c.met).map(c=>c.kind==='calendar'?'日期':'资格')]});
    if(!rank(s,'OBS-2')||known||r.toolId!==toolId||r.egg!==s.egg||!info.conditions.every(c=>c.met)||r.kind==='change')continue;
    const missing=r.ingredients.filter(id=>!ingredients.includes(id)).length,extra=ingredients.filter(id=>!r.ingredients.includes(id)).length;
    if(Math.max(missing,extra)===1&&(r.kind!=='seasonal'||!known))nearby.push({key:r.key,code:speciesCode(r.key),action:missing&&extra?'将'+ingredients.filter(id=>!r.ingredients.includes(id)).map(ingredientName).join('、')+'换为'+r.ingredients.filter(id=>!ingredients.includes(id)).map(ingredientName).join('、'):missing?'添一味：'+r.ingredients.filter(id=>!ingredients.includes(id)).map(ingredientName).join('、'):'少一味：'+ingredients.filter(id=>!r.ingredients.includes(id)).map(ingredientName).join('、')});
  }
  nearby.sort((a,b)=>Number(a.key.split(':')[1])-Number(b.key.split(':')[1]));
  const candidates=[...entries.values()];
  return {ingredients,candidates,known:candidates.filter(c=>c.known).length,unknown:candidates.filter(c=>!c.known).length,nearby:nearby.filter((c,i,a)=>a.findIndex(x=>x.key===c.key)===i).slice(0,3),blocked:blocked.filter((c,i,a)=>a.findIndex(x=>x.key===c.key)===i),changes:['破壳时脏污可能病变；防腐剂及原有免疫有效。','保温灯不焦化；其他厨具超过本批保鲜时刻可能变化。','温泉蛋鸡在破壳10秒后才处理会变为水煮蛋鸡，保鲜不延长此窗口。']};
}
