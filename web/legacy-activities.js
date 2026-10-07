import {materialCapacity,materialRoom} from './material-capacity.js';
import {economicRandom} from './rng.js';
// Permanent offline qualifications for original server / cross-app rewards.
// These commissions are a local adaptation; generated recipes remain intact.
import {availableCount} from './inventory.js';
import {advanceWorld} from './world-clock.js';
import {drawFortune} from './progression.js';
import { DATA } from './data.js';
import {holidayForCharacter} from './holiday-calendar.js';
import {collectedTotal,discoveryCount} from './species-state.js';

const campaignFlag = (egg,id) => `campaign_char_${egg}_${id}`;
const character = (egg,id) => ({egg,id,name:DATA.characters[egg].find(c=>c.id===id).title_zh_CN});
const group = (egg,ids) => ids.map(id=>character(egg,id));
const collect = target => ({kind:'total',target});
const species = target => ({kind:'species',target});
const cookware = (id,level=0) => ({kind:'tool',id,target:level+1});
const commission = (id,title,description,characters,requirements) => ({id,title,description,kind:'campaign',characters,requirements});
const gift = (id,title,ingredientId,activityId,tool) => ({
  id,title,kind:'gift',ingredientId,characters:ingredientId===68?group(0,Array.from({length:15},(_,i)=>89+i)):group(0,[ingredientId===69?106:107]),
  description:'每项每天可领一次；再次领取前需再收取 24 只伙伴。先用完同一种赠品，再领取下一份。',
  requirements:[{kind:'activity',id:activityId,target:1},cookware(tool)],
});

export const activityCatalog = Object.freeze([
  commission('spring','春日花笺','和伙伴们一起准备花束，让春日访客走进厨房。',group(0,[48,49,60]),[collect(48),species(3)]),
  commission('rain','雨后约定','为雨天和晴天的客人留出位置。',group(0,[61,62]),[collect(120),species(5)]),
  commission('summer','夏日游园','搭起夏日小摊，等待花朵、烟火和清凉的伙伴。',[...group(0,[63,64,65,66]),...group(1,[35])],[collect(300),species(8)]),
  commission('autumn','秋收茶会','把秋天的收获摆上餐桌，邀请新伙伴来坐坐。',[...group(0,[78,79,80]),...group(1,[47,48])],[collect(600),species(12)]),
  commission('winter','冬日庆典','为冬日客人准备暖灯与烘焙点心。',[...group(0,[26,27,83]),...group(1,[49,50])],[collect(100),cookware(4)]),
  commission('new-year','新年团圆','张灯结彩，和越来越多的伙伴迎接新年。',[...group(0,[84,85,86,87]),...group(1,[51])],[collect(1000),species(16)]),
  commission('sweet','甜蜜心意','学会制作巧克力点心，把心意送给新朋友。',group(0,[32]),[collect(300),cookware(2)]),
  commission('harvest-feast','丰收盛宴','为大家做一顿丰盛的烤物大餐。',group(0,[82]),[{kind:'chicks',target:2000},cookware(4)]),
  commission('shrine','神社来信','整理伙伴名册，迎来巫女和每天一份的神社礼物。',group(0,[88]),[collect(100),species(8)]),
  commission('yokai','妖怪夜话','备好热锅和热汤，欢迎有点古怪的新朋友。',group(0,[67,105]),[collect(300),cookware(1),cookware(2)]),
  {id:'time-travel',title:'时空旅行',kind:'travel',characters:group(0,[104]),
    description:'派出农场里的 1 只普通鸡宝，带回 1 只时空鸡。每天可旅行一次；再次出发前需再收取 24 只伙伴。',
    requirements:[collect(500),species(12),{kind:'stock',egg:0,id:0,target:1}]},
  gift('shrine-gift','神社签礼',68,'shrine',0),
  gift('flame-gift','灶火小礼',69,'yokai',1),
  gift('cotton-gift','木绵小礼',70,'yokai',2),
].map(entry=>Object.freeze({...entry,
  characters:Object.freeze(entry.characters.map(Object.freeze)),
  requirements:Object.freeze(entry.requirements.map(Object.freeze)),
  flags:Object.freeze(entry.kind==='campaign'?entry.characters.map(c=>campaignFlag(c.egg,c.id)):[]),
})));

const byId = new Map(activityCatalog.map(entry=>[entry.id,entry]));
const activityForCharacter = new Map(activityCatalog.filter(entry=>entry.kind==='campaign').flatMap(entry=>entry.characters.map(c=>[`${c.egg}:${c.id}`,entry])));
const positiveCount = value => Number.isSafeInteger(value)&&value>0?value:0;
const totalCollected = collectedTotal;
const travelCount = state => positiveCount(state.total?.['0:104']);
const campaignComplete = (state,entry) => entry.flags.length>0&&entry.flags.every(flag=>!!state.events?.[flag]);
const claims = state => state.events?.legacyActivityClaims??{};

function localDay(now) {
  const date=new Date(now);
  if(!Number.isFinite(now)||!Number.isFinite(date.getTime()))throw Error('当前时间无效，请稍后再试。');
  return date.getFullYear()*10000+(date.getMonth()+1)*100+date.getDate();
}

function conditionInfo(state,requirement) {
  let current=0,label='';
  const {target}=requirement;
  if(requirement.kind==='total'){current=totalCollected(state);label='累计收取伙伴';}
  if(requirement.kind==='chicks'){current=totalCollected(state,0);label='累计收取鸡宝';}
  if(requirement.kind==='species'){current=discoveryCount(state);label='发现不同品种';}
  if(requirement.kind==='tool'){current=(state.toolLevels?.[requirement.id]??-1)+1;label=`${DATA.tools[1][requirement.id].title_zh_CN}等级`;}
  if(requirement.kind==='activity'){current=campaignComplete(state,byId.get(requirement.id))?1:0;label=`完成「${byId.get(requirement.id).title}」`;}
  if(requirement.kind==='stock'){current=availableCount(state,`${requirement.egg}:${requirement.id}`);label='农场中的普通鸡宝（出发时消耗）';}
  return {kind:requirement.kind,label,current,target,met:current>=target};
}

function infoFor(state,entry,now) {
  const conditions=entry.requirements.map(requirement=>conditionInfo(state,requirement));
  const completed=entry.kind==='campaign'&&campaignComplete(state,entry);
  const claimed=claims(state)[entry.id],day=localDay(now);
  const repeatable=entry.kind==='gift'||entry.kind==='travel';
  const claimedToday=repeatable&&Number.isSafeInteger(claimed?.day)&&claimed.day>=day;
  const holidays=entry.characters.map(c=>({egg:c.egg,id:c.id,window:holidayForCharacter(c.egg,c.id,now)})).filter(c=>c.window);
  let rewardText=entry.kind==='campaign'?`获得 ${entry.characters.length} 种伙伴的${holidays.length?'活动资格（仍需对应日期）':'孵化配方'}`:entry.kind==='travel'?'普通鸡宝 × 1 → 时空鸡 × 1':`${DATA.tools[2][entry.ingredientId].title_zh_CN} × 1`;
  let reason=completed?'委托已完成':conditions.find(condition=>!condition.met)?'尚未满足委托条件':'';
  let targetCharacter=null;
  if(repeatable) {
    if(claimed) {
      // Time-travel visits are exchanges, not new collections (game-design). Claims
      // recorded before this rule have no snapshot and keep the previous count.
      const travelsSince=Number.isSafeInteger(claimed.travels)?Math.max(0,travelCount(state)-claimed.travels):0;
      const current=Math.max(0,totalCollected(state)-positiveCount(claimed.collected)-travelsSince);
      conditions.push({kind:'new-collections',label:'上次领取后收取伙伴',current,target:24,met:current>=24});
    }
    if(!reason&&claimedToday)reason='今天已领取，明天再来';
    if(!reason&&conditions.some(condition=>!condition.met))reason='再收取 24 只伙伴后，可领取下一份';
  }
  if(entry.kind==='gift') {
    const held=positiveCount(state.ingredients?.[entry.ingredientId]);
    const full=Object.values(state.ingredients??{}).reduce((sum,value)=>sum+positiveCount(value),0)>=materialCapacity(state);
    if(!reason&&held>0)reason='请先用完同一种赠品';
    if(!reason&&full)reason=`调味料已满 ${materialCapacity(state)} 个，请先腾出空间`;
    if(entry.ingredientId===68) {
      rewardText+=' · 随机签意，未收录的更容易抽到';
    }
  }
  if(entry.kind==='travel') {
    if(claimedToday&&!completed)reason='今天已旅行，明天再来';
    if(!reason&&(positiveCount(state.farm?.['0:104'])>=99999||positiveCount(state.total?.['0:104'])>=99999))reason='时空鸡数量已达上限';
  }
  return {...entry,conditions,holidays,completed,claimedToday,rewardText,targetCharacter,available:!reason,reason};
}

// Read-only: merely opening a board or cancelling a confirmation cannot claim.
export function getActivities(state,now=Date.now()) {
  return activityCatalog.map(entry=>infoFor(state,entry,now));
}

// Validate first; commit one coherent mutation only when the UI confirms.
// Call through the app's save transaction so persistence failure also rolls back.
export function claimActivity(state,id,now=Date.now(),random=economicRandom(state,'claimActivity')) {
  advanceWorld(state,now,random);
  const entry=byId.get(id);
  if(!entry)throw Error('没有找到这项委托。');
  const info=infoFor(state,entry,now);
  if(!info.available)throw Error(info.reason);
  const events={...state.events};
  if(entry.kind==='campaign')entry.flags.forEach(flag=>{events[flag]=true;});
  else {
    if(entry.ingredientId===68) {
      info.targetCharacter=character(0,drawFortune(state,random));
      events.gift_tool_2_68_character_id=info.targetCharacter.id;
      info.rewardText+=` · ${info.targetCharacter.name}`;
      events.legacyOmikujiCount=positiveCount(events.legacyOmikujiCount)+1;
    }
  }
  events.legacyActivityClaims={...claims(state),[id]:{day:localDay(now),at:now,collected:totalCollected(state)}};
  if(entry.kind==='gift')state.ingredients={...state.ingredients,[entry.ingredientId]:positiveCount(state.ingredients?.[entry.ingredientId])+1};
  if(entry.kind==='travel') {
    state.farm={...state.farm,'0:0':state.farm['0:0']-1,'0:104':positiveCount(state.farm?.['0:104'])+1};
    state.total={...state.total,'0:104':positiveCount(state.total?.['0:104'])+1};
    events.localTimeTravelUnlocked=true;
    // The visit itself is not one of the 24 further collections required.
    events.legacyActivityClaims[id].collected=totalCollected(state);
  }
  events.legacyActivityClaims[id].travels=travelCount(state);
  state.events=events;
  return {id,title:entry.title,rewardText:info.rewardText,kind:entry.kind,ingredientId:entry.ingredientId,targetCharacter:info.targetCharacter};
}

export function ingredientActivityId(id) {
  return activityCatalog.find(entry=>entry.kind==='gift'&&entry.ingredientId===id)?.id??null;
}

function campaignRecipe(egg,id) {
  if(egg===0&&(id===26||id===27))return '鸡蛋 · 保温灯 · 雪晶';
  if(egg===0&&id===32)return '鸡蛋 · 水煮锅 · 巧克力砖';
  if(egg===0&&id===82)return '鸡蛋 · 烤箱 · 累计收取 2000 只鸡宝';
  if(egg===1&&id===50)return '鸭蛋 · Lv.2 以上烤箱 · 面粉 ＋ 生姜';
  return `${egg?'鸭蛋':'鸡蛋'} · 保温灯`;
}

export function characterAccessInfo(egg,id,state,now=Date.now()) {
  const entry=activityForCharacter.get(`${egg}:${id}`);
  if(entry) {
    const qualified=!!state.events?.[campaignFlag(egg,id)],recipeText=campaignRecipe(egg,id),holiday=holidayForCharacter(egg,id,now);
    return {kind:'campaign',activityId:entry.id,title:entry.title,qualified,holiday,unlocked:qualified&&(!holiday||holiday.active),recipeText,
      text:`${qualified?'委托资格已获得':'先完成「'+entry.title+'」'}；${holiday?`${holiday.title}期间开火：${holiday.dateRange}（${holiday.status}）。`:''}${recipeText}。按配方概率孵化。${egg?'需先在商店购买鸭蛋。':''}`};
  }
  if(egg===0&&id>=89&&id<=103) {
    return {kind:'gift',activityId:'shrine-gift',title:'神社签礼',unlocked:campaignComplete(state,byId.get('shrine')),
      recipeText:'鸡蛋 · 保温灯 · 御神签',
      text:`完成「神社来信」后领取御神签。15种签随机，未收录的更容易抽到，也不会连抽同一签；连续6签没收到新签鸡，第7签必是未收录的，收到新签鸡后重新计数。鸡蛋、保温灯加对应御神签，每批会有 1 只${DATA.characters[0][id].title_zh_CN}，请保持厨房清洁并及时收取。`};
  }
  if(egg===0&&(id===106||id===107)) {
    const flame=id===106,recipeText=`鸡蛋 · ${flame?'平底锅 · 火苗':'水煮锅 · 木绵'}`;
    return {kind:'gift',activityId:flame?'flame-gift':'cotton-gift',title:flame?'灶火小礼':'木绵小礼',
      unlocked:campaignComplete(state,byId.get('yokai')),recipeText,
      text:`完成「妖怪夜话」后领取赠品；${recipeText}。每批会有 1 只，请保持厨房清洁并及时收取。`};
  }
  if(egg===0&&(id===51||id===52)) {
    const window=id===52?'当地时间 10:00–12:59 开始调理':'当地时间 00:00–09:59 或 13:00–23:59 开始调理';
    return {kind:'time',activityId:null,title:'凤凰的时刻',unlocked:(state.toolLevels?.[0]??-1)>=2,
      recipeText:`鸡蛋 · Lv.3 保温灯 · ${window}`,text:`${window}，使用鸡蛋和 Lv.3 保温灯。每批有 10% 机会让它加入候选，再随机孵化。`};
  }
  if(egg===0&&id===104)return {kind:'travel',activityId:'time-travel',title:'时空旅行',unlocked:!!state.events?.localTimeTravelUnlocked,
    recipeText:'农场 · 时空旅行 · 普通鸡宝 × 1',text:'累计收取 500 只伙伴、发现 12 种品种后，派出农场中 1 只普通鸡宝，带回 1 只时空鸡。每天一次，再次旅行前需再收取 24 只伙伴。'};
  return null;
}
