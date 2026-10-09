import {GAME_DATA as DATA,EXPANSION} from './content-pack.js';
import {ORIGINAL_RECIPE_CATALOG} from './recipe-catalog-data.js';
import {SEASONAL_CHARACTERS,seasonalRecipeInfo,prepareSeasonalRecipe} from './seasonal-pack.js';
import {characterAccessInfo} from './legacy-activities.js';
import {holidayForCharacter} from './holiday-calendar.js';
import {cookingTiming,batchSignature} from './progression.js';
import {speciesKey,speciesDiscovered,collectedTotal} from './species-state.js';
import {REGIONAL} from './content-registry.js';
import {regionalRecipeInfo,prepareRegionalRecipe} from './regional-methods.js';

export const recipeDiscovered=speciesDiscovered;
const rows=[...ORIGINAL_RECIPE_CATALOG,
  ...EXPANSION.characters.map(c=>({egg:0,id:c.id,toolId:8,minLevel:c.minLevel,minKitchen:Math.max(0,c.ingredients.length-1),ingredients:[...c.ingredients],kind:'dim-sum',description:c.description})),
  ...SEASONAL_CHARACTERS.map(c=>({egg:c.egg,id:c.id,toolId:c.toolId,minLevel:c.minLevel,minKitchen:1,ingredients:[...c.ingredients],kind:'seasonal',description:c.description})),
].map(r=>Object.freeze({...r,key:speciesKey(r.egg,r.id),ingredients:Object.freeze(r.ingredients)}));
const byKey=new Map(rows.map(r=>[r.key,r]));
const regionalRows=REGIONAL.recipes.filter(r=>r.mode==='regional-trial').map(r=>Object.freeze({runtimeId:r.id,kind:'regional',key:r.key,egg:r.egg,id:Number(r.key.split(':')[1]),toolId:r.toolId,minLevel:r.toolLevel,minKitchen:r.kitchenLevel,ingredients:Object.freeze(r.ingredients.map(m=>m.id))}));
// The 48 regional partners' recipes as catalogue rows (one each). They stay out of RECIPE_CATALOG, whose rows the ordinary
// cooking pool and the recommenders read; the 线索册 adds them through regional-clues.js (loop batch 4).
export const REGIONAL_RECIPE_ROWS=Object.freeze(regionalRows);
export const recipePaths=key=>rows.filter(r=>r.key===key);
export const recipeId=r=>[r.key,r.kind,r.toolId,r.minLevel,r.ingredients.join('-'),r.campaign??'',r.time??''].join('/');
export const RECIPE_CATALOG=Object.freeze(rows);
export const RECIPE_TOOLS=Object.freeze([...DATA.tools[1].map(t=>({id:t.id,name:t.title_zh_CN})),{id:-1,name:'特殊变化'}]);

// Return nothing for an undiscovered species, including direct detail requests.
// The UI never receives its name, artwork, ingredient list, or preparation data.
export function discoveredRecipe(s,key,now=Date.now()){
  const r=byKey.get(key)??regionalRows.find(r=>r.key===key);if(!r||!recipeDiscovered(s,r.egg,r.id))return null;
  return recipePathInfo(s,r,now);
}
// gates: false leaves a regional recipe's own steps (its region, specimen, 辨认, complete method) out of the conditions,
// for the 线索册, which shows them as the partner's next step instead.
export function recipePathInfo(s,r,now=Date.now(),{gates=true}={}){
  const key=r.key;
  const c=DATA.characters[r.egg].find(c=>c.id===r.id),access=characterAccessInfo(r.egg,r.id,s,now);
  const conditions=[];
  const condition=(met,label)=>conditions.push({met:Boolean(met),label});
  condition(s.kitchenLevel>=r.minKitchen,`厨房 Lv.${r.minKitchen+1}`);
  if(r.egg)condition(s.duck,'已开放鸭蛋');
  if(r.toolId>=0)condition((s.toolLevels[r.toolId]??-1)>=r.minLevel,`${DATA.tools[1][r.toolId].title_zh_CN} Lv.${r.minLevel+1} 起`);
  if(r.campaign)condition(s.events?.[r.campaign]===true,`完成「${access?.title??'对应神社来信'}」`);
  const holiday=holidayForCharacter(r.egg,r.id,now);
  if(holiday)conditions.push({met:holiday.active,kind:'calendar',label:`${holiday.title}期间开火 · ${holiday.dateRange}（${holiday.status}）`});
  if(r.collectionTotal){const [egg,n]=r.collectionTotal,total=collectedTotal(s,egg);condition(total>=n,`累计收取${egg?'鸭宝':'鸡宝'} ${n} 只（${total} / ${n}）`);}
  if(r.time){const hour=new Date(now).getHours(),day=hour>=10&&hour<=12;condition(r.time==='day-phoenix'?day:!day,r.time==='day-phoenix'?'开火时段 10:00–12:59':'开火时段 00:00–09:59 或 13:00–23:59');}
  if(r.kind==='sign')condition(s.events?.gift_tool_2_68_character_id===r.id,'神社抽到对应的签礼');
  if(r.kind==='seasonal')for(const check of seasonalRecipeInfo(s,key).conditions)if(!conditions.some(c=>c.label===check.label))conditions.push(check);
  if(r.kind==='regional'&&gates)for(const label of regionalRecipeInfo(s,r.runtimeId).missing)condition(false,label);
  const missing=r.ingredients.filter(id=>!(s.ingredients?.[id]>0));
  const special=r.kind==='change',tool=r.toolId>=0?DATA.tools[1][r.toolId]:null;
  const level=tool?Math.max(r.minLevel,s.toolLevels[r.toolId]??0):0;
  const note=r.note??(r.kind==='regional'?'地区做法按稀有度逐枚抽取，同锅可以出多只；请保持清洁并及时收取。':r.kind==='seasonal'?'配方匹配时逐枚抽取，首见和再次制作概率相同。':r.kind==='dim-sum'?(r.ingredients.length?'搭配成功后按候选权重逐枚抽取，数量不固定。':'不放调味料时只会孵出小笼包鸡；请保持清洁并及时收取。'):'随机出现，数量不固定；符合条件也不保证每批都有。');
  return {...r,name:c.title_zh_CN,toolName:tool?.title_zh_CN??'特殊变化',ingredientNames:r.ingredients.map(id=>DATA.tools[2][id].title_zh_CN),
    conditions,missing,special,note,activityId:access?.activityId,originalMinutes:tool?.[`lv_${level}_min`],minutes:tool?cookingTiming(s,tool[`lv_${level}_min`],{signature:batchSignature(r.egg,r.toolId,r.ingredients),now}).minutes:undefined,cost:tool?.[`lv_${level}_cook_cp`],
    ready:!special&&!missing.length&&conditions.every(c=>c.met)};
}

export function recipeBookModel(s,{toolId=0,egg=-1}={},now=Date.now()){
  if(!RECIPE_TOOLS.some(t=>t.id===toolId))toolId=0;
  egg=egg===0||egg===1?egg:-1;
  const known=[...rows,...regionalRows].filter(r=>recipeDiscovered(s,r.egg,r.id));
  return {toolId,egg,total:known.length,tools:RECIPE_TOOLS.map(t=>({...t,count:known.filter(r=>r.toolId===t.id&&(egg<0||r.egg===egg)).length})),
    entries:known.filter(r=>r.toolId===toolId&&(egg<0||r.egg===egg)).map(r=>discoveredRecipe(s,r.key,now))};
}

// Preparation changes only next-batch choices. Starting/spending still happens
// through the kitchen's normal confirmation and revalidation.
export function prepareDiscoveredRecipe(s,key,now=Date.now()){
  const r=discoveredRecipe(s,key,now);
  if(!r)throw Error('先孵化并收取这位伙伴，才能解锁它的配方。');
  if(r.special)throw Error('这是特殊变化，请按出现方式收集。');
  const blocked=r.conditions.find(c=>!c.met);if(blocked)throw Error(blocked.label);
  if(r.missing.length)throw Error('调味料还没备齐，请先补齐材料。');
  if(r.kind==='regional')prepareRegionalRecipe(s,r.runtimeId);
  else if(r.kind==='seasonal'){delete s.expansion?.prepareMode;prepareSeasonalRecipe(s,key);}
  else{s.egg=r.egg;s.selected=[...r.ingredients];delete s.events.seasonalRecipe;delete s.expansion?.prepareMode;}
  return r;
}
