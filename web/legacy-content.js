// Original, generated data stays unchanged. New releases append stable IDs here.
import { DATA } from './data.js';
import {SEASONAL_CHARACTERS} from './seasonal-pack.js';
import {speciesDiscovered,discoveryCount} from './species-state.js';

const ART = '/web/art/expansion-dim-sum.png';
const characters = [
  {id:114,title_zh_CN:'小笼包鸡',cp_1:6,rate:100,minLevel:0,ingredients:[],description:'把热乎乎的汤汁藏在肚子里，走路时总怕晃出来。'},
  {id:115,title_zh_CN:'烧麦鸡',cp_1:11,rate:40,minLevel:0,ingredients:[9],description:'开花一样的烧麦裙摆里，藏着一颗认真做早饭的心。'},
  {id:116,title_zh_CN:'荷叶糯米鸡',cp_1:14,rate:30,minLevel:0,ingredients:[16],description:'裹着清香的荷叶小被子，喜欢在草地上慢慢散步。'},
  {id:117,title_zh_CN:'奶黄流沙鸡',cp_1:20,rate:20,minLevel:1,ingredients:[24,37],description:'开心时会露出金黄的奶黄馅，连笑容都是暖乎乎的。'},
  {id:118,title_zh_CN:'水晶饺鸡',cp_1:17,rate:25,minLevel:1,ingredients:[25],description:'透亮的月牙外衣一抖一抖，在阳光下会闪出细碎的光。'},
  {id:119,title_zh_CN:'寿桃豆沙鸡',cp_1:28,rate:10,minLevel:2,ingredients:[71,37],description:'顶着粉红桃尖来送祝福，最喜欢陪伙伴一起过生日。'},
].map((entry,index)=>Object.freeze({
  ...entry,ingredients:Object.freeze(entry.ingredients),egg:0,cp_0:1,
  title_en:entry.title_zh_CN,title_zh_TW:entry.title_zh_CN,title_ja:entry.title_zh_CN,
  cnt_min:-1,cnt_max:-1,tool_1_0_id:8,tool_2_0_id:entry.ingredients[0]??-1,
  tool_2_1_id:entry.ingredients[1]??-1,comment:entry.description,
  artwork:`${ART}#chick${index}`,pack:'dim-sum',
}));

const levels = Object.freeze([
  Object.freeze({level:0,title:'竹蒸笼',buyCP:3000,minutes:45,cookCP:120,kitchenLevel:1,ingredients:Object.freeze([9,16])}),
  Object.freeze({level:1,title:'双层竹蒸笼',buyCP:6000,minutes:38,cookCP:120,kitchenLevel:2,ingredients:Object.freeze([24,25,37])}),
  Object.freeze({level:2,title:'三层竹蒸笼',buyCP:12000,minutes:30,cookCP:120,kitchenLevel:3,ingredients:Object.freeze([71])}),
]);
const steamer = {id:8,title_zh_CN:'竹蒸笼',title_en:'Bamboo Steamer',title_zh_TW:'竹蒸籠',title_ja:'竹せいろ',pack:'dim-sum'};
for (const level of levels) {
  steamer[`lv_${level.level}_buy_cp`]=level.buyCP;
  steamer[`lv_${level.level}_min`]=level.minutes;
  steamer[`lv_${level.level}_cook_cp`]=level.cookCP;
}

export const EXPANSION = Object.freeze({
  id:'dim-sum',title:'竹笼点心坊',toolId:8,egg:0,requiredDiscoveries:12,
  characters:Object.freeze(characters),levels,art:ART,
  recipeNote:'搭配符合配方时，每批至少孵出 1 只对应点心鸡宝。请保持厨房清洁并及时收取。',
});
export const GAME_DATA = {
  ...DATA,
  characters:[Object.freeze([...DATA.characters[0],...characters,...SEASONAL_CHARACTERS.filter(c=>c.egg===0)]),Object.freeze([...DATA.characters[1],...SEASONAL_CHARACTERS.filter(c=>c.egg===1)])],
  tools:DATA.tools.map((list,type)=>type===1?Object.freeze([...list,Object.freeze(steamer)]):list),
};
export const TOOL_COUNT = GAME_DATA.tools[1].length;
export const TOOL_SCROLL_MAX = Math.max(0,TOOL_COUNT-4);

export function expansionUnlockInfo(state) {
  const ownedLevel=state.toolLevels?.[8]??-1,nextLevel=ownedLevel+1,maxed=nextLevel>=levels.length;
  const target=maxed?null:levels[nextLevel],discovered=discoveryCount(state);
  const kitchenMet=!!target&&state.kitchenLevel>=target.kitchenLevel;
  const discoveriesMet=ownedLevel>=0||discovered>=EXPANSION.requiredDiscoveries;
  const available=!maxed&&kitchenMet&&discoveriesMet;
  const reason=maxed?'这一件已升至最高等级':!kitchenMet?`厨房 Lv.${target.kitchenLevel+1} 后开放`:!discoveriesMet?`已发现 ${discovered} / ${EXPANSION.requiredDiscoveries} 种伙伴` : '';
  return {ownedLevel,nextLevel,maxed,target,discovered,kitchenMet,discoveriesMet,available,reason};
}

export function expansionRecipeHints(state) {
  const level=state.toolLevels?.[8]??-1;
  return characters.map(character=>({
    id:character.id,egg:0,name:character.title_zh_CN,description:character.description,
    ingredients:[...character.ingredients],
    ingredientNames:character.ingredients.map(id=>DATA.tools[2].find(item=>item.id===id).title_zh_CN),
    requiredLevel:character.minLevel+1,unlocked:level>=character.minLevel,
    discovered:speciesDiscovered(state,0,character.id),
    price:character.cp_1,artwork:character.artwork,
    hint:character.ingredients.length?character.ingredients.map(id=>DATA.tools[2].find(item=>item.id===id).title_zh_CN).join(' ＋ '):'不放调味料',
  }));
}

export function expansionMatches(state,egg,toolId,ingredients){
  if(toolId!==8)return null;
  if(egg!==0)throw Error('竹蒸笼目前只调理鸡蛋，请先切换鸡蛋。');
  const level=state.toolLevels?.[8]??-1;
  if(level<0||level>2)throw Error('请先在商店购买竹蒸笼。');
  return characters.filter(c=>c.minLevel<=level&&c.ingredients.every(id=>ingredients.includes(id)));
}
export function expansionRecipes(state,egg,toolId,ingredients,random=Math.random) {
  const matches=expansionMatches(state,egg,toolId,ingredients);if(matches===null)return null;
  const pool=matches.flatMap(c=>Array(c.rate).fill(c.id));
  // Every egg draws from the matched weighted pool; no result occupies a fixed slot.
  const result=[];
  const index=length=>{const value=random();if(!Number.isFinite(value)||value<0||value>=1)throw Error('随机数异常，未开始调理。');return Math.floor(value*length);};
  while(result.length<24)result.push(pool[index(pool.length)]);
  for(let i=result.length-1;i>0;i--){const j=index(i+1);[result[i],result[j]]=[result[j],result[i]];}
  return result;
}

// Frozen pre-regional boundary. Authoring tools must import this identity set.
export const LEGACY193 = GAME_DATA;
