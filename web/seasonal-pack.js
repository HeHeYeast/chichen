// Append-only content; original recipes and character IDs remain unchanged.
import {DATA} from './data.js';
import {speciesKey,speciesDiscovered} from './species-state.js';
export const SEASON_ART='/web/art/four-seasons-v12.png';
export const SEASONS=Object.freeze([
  {id:'spring',title:'春日野餐',months:[3,4,5],color:'#9bb96b'},
  {id:'summer',title:'夏日凉铺',months:[6,7,8],color:'#6caeb6'},
  {id:'autumn',title:'秋收烘焙',months:[9,10,11],color:'#cf9a57'},
  {id:'winter',title:'冬日暖锅',months:[12,1,2],color:'#ae9fbd'},
].map(Object.freeze));
const definitions=[
  [0,120,'樱桃大福鸡',2,1,[16,50],24,'软软的糯米外衣里藏着樱桃，蹦起来也舍不得让果子掉下来。'],
  [0,121,'蜂蜜松饼鸡',1,1,[9,33],20,'把自己叠成三层松饼，每次打招呼都会晃一晃头上的黄油。'],
  [1,57,'抹茶团子鸭',2,1,[16,64],25,'抱紧绿叶小被子，闻到茶香就眯起眼睛。'],
  [1,58,'花香茶冻鸭',6,0,[65,66],24,'把一朵菊花戴成小皇冠，走过的地方都像刚泡好一壶茶。'],
  [0,122,'柠檬冰沙鸡',6,0,[1,59],27,'头顶柠檬小太阳，却总想找个凉快的角落。'],
  [0,123,'焦糖布丁鸡',2,1,[57,58],26,'焦糖小帽有点滑，所以说话时只敢轻轻点头。'],
  [1,59,'橙橙汽水鸭',6,0,[44,59],27,'每笑一次，橙色肚皮里就冒出一串小泡泡。'],
  [1,60,'红豆冰棒鸭',6,1,[71,60],29,'数着身上的红豆睡午觉，常常还没数完就睡着了。'],
  [0,124,'芝麻月饼鸡',4,1,[9,72],25,'圆圆的花边像一轮小月亮，最爱大家坐在一起吃晚饭。'],
  [0,125,'红豆铜锣鸡',1,1,[9,71],23,'上下两片松饼夹着满满红豆，一开心就露出甜甜的夹心。'],
  [1,61,'焦糖吐司鸭',7,0,[57,58],30,'烤得方方正正的小鸭子，梦想是成为早餐盘里的主角。'],
  [1,62,'烤菇饭团鸭',4,1,[31,35],28,'戴着香菇帽到处找朋友，海苔围裙总是整理得很平整。'],
  [0,126,'热可可鸡',6,0,[17,57],26,'棉花糖发梢软蓬蓬的，冬天最喜欢挨着伙伴坐。'],
  [0,127,'雪帽曲奇鸡',4,1,[9,61],25,'糖霜小帽永远不会融化，彩糖纽扣一颗也不舍得吃。'],
  [1,63,'姜糖茶鸭',6,0,[29,66],24,'披着琥珀色茶衣，认真把每一颗冰糖都泡得甜甜的。'],
  [1,64,'围巾汤圆鸭',2,1,[16,72],25,'芝麻小卷毛下是一张圆脸，红围巾要和朋友一起围。'],
];
export const SEASONAL_CHARACTERS=Object.freeze(definitions.map(([egg,id,name,toolId,minLevel,ingredients,cp,description],index)=>Object.freeze({
  egg,id,title_zh_CN:name,title_zh_TW:name,title_ja:name,title_en:name,cp_0:1,cp_1:cp,rate:1,cnt_min:-1,cnt_max:-1,
  tool_1_0_id:toolId,tool_2_0_id:ingredients[0],tool_2_1_id:ingredients[1],toolId,minLevel,ingredients:Object.freeze(ingredients),description,comment:description,
  pack:'four-seasons',chapter:SEASONS[Math.floor(index/4)].id,artwork:SEASON_ART+'#'+index,key:speciesKey(egg,id),
})));
export const seasonalCharacter=(egg,id)=>SEASONAL_CHARACTERS.find(c=>c.egg===egg&&c.id===id);
export const seasonalFound=(s,c)=>speciesDiscovered(s,c.egg,c.id);
export function seasonalRecipeInfo(s,key){
  const c=SEASONAL_CHARACTERS.find(c=>c.key===key);if(!c)return null;
  const discovered=new Set([...Object.keys(s.total??{}).filter(k=>s.total[k]>0),...Object.keys(s.farm??{}).filter(k=>s.farm[k]>0)]).size;
  const conditions=[{met:s.kitchenLevel>=1,label:'厨房 Lv.2'}, {met:discovered>=12,label:`认识 12 种伙伴（${discovered} / 12）`},
    {met:(s.toolLevels[c.toolId]??-1)>=c.minLevel,label:`${DATA.tools[1][c.toolId].title_zh_CN} Lv.${c.minLevel+1}`},
    ...(c.egg?[{met:s.duck===true,label:'商店开放鸭蛋'}]:[])];
  const missing=c.ingredients.filter(id=>!(s.ingredients?.[id]>0)),unlocked=conditions.every(c=>c.met);
  return {...c,conditions,unlocked,missing,ready:unlocked&&!missing.length,found:seasonalFound(s,c),
    toolName:DATA.tools[1][c.toolId].title_zh_CN,ingredientNames:c.ingredients.map(id=>DATA.tools[2][id].title_zh_CN),
    reason:!unlocked?conditions.find(c=>!c.met).label:missing.length?'先补齐材料':'可以准备下一批'};
}
export function prepareSeasonalRecipe(s,key){
  const r=seasonalRecipeInfo(s,key);if(!r)throw Error('没有找到这份四时配方。');
  if(!r.found)throw Error('首次孵化并收取后，才会解锁这位伙伴的定向配方。');
  if(!r.ready)throw Error(r.reason);
  s.egg=r.egg;s.selected=[...r.ingredients];s.events={...s.events,seasonalRecipe:key};return r;
}
// Unrecorded handmade companions must remain discoverable after recipe secrecy.
// Only exact two-ingredient matches can add one surprise to an ordinary batch.
// Known recipes retain the previous explicit preparation/guarantee behavior.
export function seasonalCandidates(s,toolId,ingredients){
  return SEASONAL_CHARACTERS.filter(c=>c.egg===s.egg&&c.toolId===toolId&&!seasonalFound(s,c)&&
    c.ingredients.length===ingredients.length&&c.ingredients.every(id=>ingredients.includes(id))&&seasonalRecipeInfo(s,c.key).unlocked);
}
export function seasonalSurprise(s,toolId,ingredients,random=Math.random){
  const candidates=seasonalCandidates(s,toolId,ingredients);
  if(!candidates.length)return null;
  const chance=random();if(!Number.isFinite(chance)||chance<0||chance>=1)throw Error('随机数异常，未开始调理。');
  if(chance>=.25)return null;
  return candidates[Math.min(candidates.length-1,Math.floor(chance/.25*candidates.length))];
}
export function plannedSeasonalRecipe(s,toolId,ingredients=s.selected){
  const r=seasonalRecipeInfo(s,s.events?.seasonalRecipe);
  return r?.found&&r.unlocked&&s.egg===r.egg&&toolId===r.toolId&&ingredients.length===r.ingredients.length&&r.ingredients.every(id=>ingredients.includes(id))?r:null;
}
export function seasonalChapterInfo(s,id){
  const chapter=SEASONS.find(c=>c.id===id);if(!chapter)return null;
  const characters=SEASONAL_CHARACTERS.filter(c=>c.chapter===id),found=characters.filter(c=>seasonalFound(s,c)).length;
  const claimed=s.events?.seasonalCollections?.[id]===true,cp=600,room=Number.isSafeInteger(s.cp)&&s.cp<=Number.MAX_SAFE_INTEGER-cp;
  return {...chapter,characters,found,claimed,cp,available:found===4&&!claimed&&room};
}
export function claimSeasonalChapter(s,id){
  const chapter=seasonalChapterInfo(s,id);if(!chapter?.available)throw Error('这份章节回礼暂时不能领取。');
  s.cp+=chapter.cp;s.events={...s.events,seasonalCollections:{...s.events?.seasonalCollections,[id]:true}};return chapter;
}
