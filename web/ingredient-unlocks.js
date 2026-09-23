import { GAME_DATA as DATA, EXPANSION } from './content-pack.js';

// Each group is an alternative; every condition within a group must be met.
// This is the shared source for shop explanations and purchase validation.
// Cookware levels below are the original zero-based ownership levels.
const cookware=(id,level=0)=>Object.freeze({kind:'tool',id,level});
const discovered=(egg,id)=>Object.freeze({kind:'discovery',egg,id});
const collected=count=>Object.freeze({kind:'collected',count});
const kitchen=level=>Object.freeze({kind:'kitchen',level});
const duck=discovered(1,0);
const rules=Array.from({length:DATA.tools[2].length},()=>[]);
const add=(ids,...alternatives)=>ids.forEach(id=>rules[id].push(...alternatives));

add([0],[]);
add([1,2],[cookware(1)]);
add([3],[cookware(2)],[duck,cookware(6)]);
add([4],[cookware(2)]);
add([5,6,7],[cookware(3)]);
add([8,9,10],[cookware(4)]);
add([9],[cookware(2,1)]);
add([11],[discovered(0,1)],[discovered(1,1)]);
add([12,13],[cookware(5)]);
add([14],[cookware(5)],[cookware(3,1)]);
add([15],[collected(100)]);
add([16],[discovered(0,18)]);
add([17],[collected(300)]);
add([18],[collected(500)]);
add([19,30,31],[cookware(1,1)]);
add([20],[cookware(1,1)],[cookware(2,1)]);
add([21,22,24],[cookware(2,1)]);
add([23],[cookware(2,1)],[cookware(4,1)],[cookware(5,1)],[duck,cookware(1,1)]);
add([25,32],[cookware(3,1)]);
add([26],[discovered(0,35)]);
add([27],[cookware(4,1)],[duck,cookware(0)],[duck,cookware(2)],[duck,cookware(5)]);
add([28],[cookware(5,1)]);
add([29],[cookware(5,1)],[duck,cookware(3,2)],[duck,cookware(4,1)],[duck,cookware(5,2)],[duck,cookware(6,2)]);
add([33],[cookware(4,1)],[duck,cookware(4)]);
add([34],[cookware(5,1)],[duck,cookware(1,2)]);
add([35],[collected(2000)]);
add([36],[kitchen(2)]);
add([37,38],[cookware(1,2)]);
add([39,40],[cookware(2,2)]);
add([41],[cookware(3,2)],[cookware(4,2)],[duck,cookware(1,2)]);
add([42],[cookware(4,2)]);
add([43],[cookware(5,2)]);
add([44],[duck,cookware(1)]);
add([45],[duck,cookware(3)],[duck,cookware(5,1)]);
add([46],[duck,cookware(1,1)],[duck,cookware(2,2)],[duck,cookware(3,2)],[duck,cookware(4,2)]);
add([47,48],[duck,cookware(2,1)]);
add([49],[duck,cookware(5,1)]);
add([50],[duck,cookware(1,2)]);
add([51,52],[duck,cookware(2,2)]);
add([53],[duck,cookware(3,2)]);
add([54,55],[duck,cookware(4,2)]);
add([56],[cookware(6)]);
add([57],[cookware(6,1)]);
add([58,59,60,61,62],[cookware(6,2)]);
add([63,64],[duck,cookware(6)]);
add([65,66],[duck,cookware(6,1)]);
add([67],[duck,cookware(6,2)]);
add([71,72],[cookware(7,1)]);
add([73,74],[duck,cookware(7,1)]);
for(const level of EXPANSION.levels)add(level.ingredients,[cookware(EXPANSION.toolId,level.level)]);

for(let id=75;id<=82;id++)add([id],[{kind:'regional',id}]);
const SPECIAL_IDS=new Set([68,69,70]);
export const INGREDIENT_UNLOCK_RULES=Object.freeze(rules.map((alternatives,id)=>Object.freeze({
  id,special:SPECIAL_IDS.has(id),alternatives:Object.freeze(alternatives.map(group=>Object.freeze(group))),
})));

function requirementInfo(state,requirement,total) {
  switch(requirement.kind) {
    case 'regional':return {description:'在地区寻访取得标本，并免费辨认后开放供货',met:Object.hasOwn(state.expansion?.discovery?.identified??{},String(requirement.id))};
    case 'tool':return {description:`${DATA.tools[1].find(t=>t.id===requirement.id).title_zh_CN} Lv.${requirement.level+1}`,met:(state.toolLevels?.[requirement.id]??-1)>=requirement.level};
    case 'discovery':return {description:`收取过${requirement.egg===1&&requirement.id===0?'基础品种':''}「${DATA.characters[requirement.egg].find(c=>c.id===requirement.id).title_zh_CN}」`,met:(state.total?.[`${requirement.egg}:${requirement.id}`]??0)>0};
    case 'collected':return {description:`累计收取 ${requirement.count.toLocaleString('zh-CN')} 只鸡宝或鸭宝`,met:total>=requirement.count};
    case 'kitchen':return {description:`厨房 Lv.${requirement.level+1}`,met:state.kitchenLevel>=requirement.level};
    default:throw Error('未知调料解锁条件。');
  }
}

export function ingredientUnlockInfo(state,id) {
  const rule=INGREDIENT_UNLOCK_RULES[id];
  if(!Number.isInteger(id)||!rule)throw Error('调味料不存在。');
  if(rule.special) {
    const description='活动礼物获得，不使用 CP 购买。';
    return {id,available:false,special:true,requirements:[description],description,reason:description,requirementGroups:[]};
  }
  const total=Object.values(state.total??{}).reduce((sum,count)=>sum+count,0);
  const requirementGroups=rule.alternatives.map(group=>{
    const conditions=group.map(requirement=>requirementInfo(state,requirement,total));
    return {description:conditions.length?conditions.map(item=>item.description).join('，并且'):'开始游戏即可购买',met:conditions.every(item=>item.met),conditions};
  });
  const requirements=requirementGroups.map(group=>group.description);
  const available=requirementGroups.some(group=>group.met);
  const description=requirements.length>1?`满足任一条件：${requirements.join('；或')}`:requirements[0];
  return {id,available,special:false,requirements,description,reason:available?'':description,requirementGroups};
}

export function availableIngredientIds(state) {
  return INGREDIENT_UNLOCK_RULES.filter(rule=>ingredientUnlockInfo(state,rule.id).available).map(rule=>rule.id);
}
