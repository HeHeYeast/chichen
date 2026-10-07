// Local shrine progression layered over the original gifts and recipe IDs.
// Reading the book never claims rewards or advances the daily sign sequence.
import {GAME_DATA as DATA} from './content-pack.js';
import {getActivities} from './legacy-activities.js';

export const FORTUNE_IDS=Object.freeze(Array.from({length:15},(_,i)=>89+i));
const notes=[
  '今天适合尝试一份新配方，惊喜正在路上。','把期待放进厨房，慢慢等它发芽。','再多一点耐心，就会遇见新朋友。',
  '小小的收获，也值得好好庆祝。','照顾好眼前的一批，就是很棒的一天。','好运走得慢一点，记得为它留个位置。',
  '不顺心的时候，先把小厨房打扫干净。','坏运气也会过去，鸡宝会陪着你。','一半交给耐心，一半交给热腾腾的锅。',
  '今天的小幸运，藏在不起眼的角落。','平平稳稳地开火，也是一种幸福。','换个配方再试一次，也许就不同了。',
  '别急着下结论，再给新朋友一点时间。','把烦恼留在门外，明天还有新的相遇。','转一圈，歇一歇，惊喜有时会绕个弯。',
];
const count=value=>Number.isSafeInteger(value)&&value>0?value:0;
const found=(state,egg,id)=>count(state.total?.[`${egg}:${id}`])>0||count(state.farm?.[`${egg}:${id}`])>0;
const goals=[
  {id:'signs-3',title:'初识签缘',kind:'signs',target:3,cp:300,note:'收录 3 种签鸡'},
  {id:'signs-6',title:'签香常伴',kind:'signs',target:6,cp:800,note:'收录 6 种签鸡'},
  {id:'signs-10',title:'十签之约',kind:'signs',target:10,cp:1600,note:'收录 10 种签鸡'},
  {id:'signs-15',title:'满签团圆',kind:'signs',target:15,cp:3000,note:'收齐 15 种签鸡'},
  {id:'duck-5',title:'池塘新朋友',kind:'ducks',target:5,cp:600,note:'收录 5 种鸭宝'},
  {id:'yokai-4',title:'妖怪茶话会',kind:'yokai',target:4,cp:1000,note:'收录妖怪来信和礼物中的 4 种伙伴'},
  {id:'dim-sum-6',title:'六味点心席',kind:'dim-sum',target:6,cp:1200,note:'收齐竹笼点心坊的 6 种鸡宝'},
  {id:'time-1',title:'旅途的纪念',kind:'time',target:1,cp:600,note:'收录时空鸡'},
];
export const SHRINE_GOALS=Object.freeze(goals.map(Object.freeze));

export function shrineGoals(state){
  const counts={signs:FORTUNE_IDS.filter(id=>found(state,0,id)).length,
    ducks:DATA.characters[1].filter(c=>found(state,1,c.id)).length,
    yokai:[67,105,106,107].filter(id=>found(state,0,id)).length,
    'dim-sum':[114,115,116,117,118,119].filter(id=>found(state,0,id)).length,
    time:found(state,0,104)?1:0};
  return SHRINE_GOALS.map(goal=>{
    const current=counts[goal.kind],claimed=state.events?.shrineCollections?.[goal.id]===true;
    const room=Number.isSafeInteger(state.cp)&&state.cp<=Number.MAX_SAFE_INTEGER-goal.cp;
    return {...goal,current,claimed,available:current>=goal.target&&!claimed&&room,
      reason:claimed?'回礼已收下':current<goal.target?`再收录 ${goal.target-current} 种即可领取`:!room?'CP 已达上限':'可领取回礼'};
  });
}
// The partners that count toward a goal, in book order (for its stamp card).
export function goalMembers(state,kind){
  const ids=kind==='signs'?FORTUNE_IDS.map(id=>[0,id]):kind==='ducks'?DATA.characters[1].map(c=>[1,c.id]):kind==='yokai'?[67,105,106,107].map(id=>[0,id]):kind==='dim-sum'?[114,115,116,117,118,119].map(id=>[0,id]):[[0,104]];
  return ids.filter(([egg,id])=>found(state,egg,id));
}
export function claimShrineGoal(state,id){
  const goal=shrineGoals(state).find(goal=>goal.id===id);
  if(!goal)throw Error('没有找到这份收藏回礼。');
  if(!goal.available)throw Error(goal.reason);
  state.cp+=goal.cp;
  state.events={...state.events,shrineCollections:{...state.events?.shrineCollections,[id]:true}};
  return {id,title:goal.title,cp:goal.cp};
}
export function shrineBook(state,now=Date.now()){
  const gift=getActivities(state,now).find(entry=>entry.id==='shrine-gift');
  const held=count(state.ingredients?.[68])>0;
  const target=state.events?.gift_tool_2_68_character_id;
  const heldId=held&&FORTUNE_IDS.includes(target)?target:null;
  const latest=count(state.events?.legacyOmikujiCount)>0&&FORTUNE_IDS.includes(target)?target:null;
  const signs=FORTUNE_IDS.map((id,index)=>({id,number:index+1,name:DATA.characters[0].find(c=>c.id===id).title_zh_CN,
    fortune:id===103?'转运签':DATA.characters[0].find(c=>c.id===id).title_zh_CN.replace('签鸡',''),
    note:notes[index],found:found(state,0,id),held:heldId===id,
    cooking:!!state.batch?.eggs.some(e=>!e.collected&&e.egg===0&&e.id===id)}));
  return {gift,signs,held,heldId,latest,discovered:signs.filter(s=>s.found).length,goals:shrineGoals(state),
    activeBatch:!!state.batch?.eggs.some(e=>!e.collected)};
}
export function giftRecipe(state,ingredientId){
  const toolId=({68:0,69:1,70:2})[ingredientId];
  if(toolId===undefined)throw Error('这份材料还没有特别配方。');
  if(!count(state.ingredients?.[ingredientId]))throw Error('先领取这份礼物，再来准备配方。');
  if((state.toolLevels?.[toolId]??-1)<0)throw Error('请先在商店买下对应厨具。');
  return {ingredientId,toolId,name:DATA.tools[2][ingredientId].title_zh_CN,
    toolName:DATA.tools[1][toolId].title_zh_CN,activeBatch:!!state.batch?.eggs.some(e=>!e.collected)};
}
export function prepareGiftRecipe(state,ingredientId){
  const recipe=giftRecipe(state,ingredientId);
  state.egg=0;state.selected=[ingredientId];delete state.events.seasonalRecipe;delete state.expansion?.prepareMode;
  return recipe;
}
