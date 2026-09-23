import { GAME_DATA as DATA, TOOL_COUNT, expansionRecipes, expansionUnlockInfo } from './content-pack.js';
import { availableIngredientIds } from './ingredient-unlocks.js';
import { originalRecipes } from './recipes.js';
import {characterIndex} from './catalog.js';
import {plannedSeasonalRecipe,seasonalSurprise} from './seasonal-pack.js';
import {recipeStateAt} from './holiday-calendar.js';
import {cookingIngredients} from './cooking-query.js';
import {speciesKey,speciesDiscovered} from './species-state.js';
import {freshProgress,validateProgress} from './progress-save.js';
import {effects,harvestCredit,syncProgress,basketQuote,checkedIncome} from './progression.js';
import {availableCount,validateConsumption} from './inventory.js';
import {advanceWorld,farmHP,checkFarmLoss} from './world-clock.js';
export {farmHP,checkFarmLoss,advanceWorld};

export const char = (egg,id) => characterIndex.get(speciesKey(egg,id));
export const tool = id => DATA.tools[1].find(t=>t.id===id);
export const ingredient = id => DATA.tools[2].find(t=>t.id===id);
export const label = o => o?.title_zh_CN ?? '???';
export const key = speciesKey;
export function freshState(now=Date.now()) {
  const s={version:3,cp:600,kitchenLevel:0,toolLevels:Array.from({length:TOOL_COUNT},(_,id)=>id===0?0:-1),ingredients:{0:1},selected:[],egg:0,duck:false,batch:null,farm:{},total:{},lastSeen:now,lastClean:now,farmFixed:now,farmChecked:now,dirty:false,alarm:false,music:true,sound:true,events:{}};
  s.progress=freshProgress(s);s.cleanCycle={hours:36,dirtyAt:now+36*3600000};return s;
}
export function cookInfo(s,id) {
  const t=tool(id),lv=s.toolLevels[id];
  if(!t||!Number.isInteger(lv)||lv<0||lv>2)throw Error('请先在商店购买调理用具。');
  const originalMinutes=t[`lv_${lv}_min`],e=effects(s);
  const minutes=Math.ceil(Math.max(6,originalMinutes*(1-e.reduction))*60000)/60000;
  const freshMinutes=s.progress?.protection.freshness===false?2*originalMinutes:Math.max(2*originalMinutes,120)+e.freshMinutes;
  return {cost:t[`lv_${lv}_cook_cp`],minutes,originalMinutes,freshMinutes,reduction:e.reduction,sickness:e.sickness};
}
export function startBatch(s,id,now=Date.now(),random=Math.random) {
  if(s.batch?.eggs.some(e=>!e.collected)) throw Error('请先收取这一批鸡宝。');
  if(!tool(id)||!Number.isInteger(s.toolLevels[id])||s.toolLevels[id]<0) throw Error('请先在商店购买调理用具。');
  const info=cookInfo(s,id),{cost,minutes}=info;
  if(s.cp<cost) throw Error('CP不足。');
  const selected=cookingIngredients(s);
  const result=expansionRecipes(s,s.egg,id,selected,random)??originalRecipes(recipeStateAt(s,now),s.egg,id,selected,now,random);
  if(!result || result.length!==24 || result.some(i=>!char(s.egg,i))) throw Error('配方数据异常，未扣除CP。');
  const seasonal=plannedSeasonalRecipe(s,id,selected);
  if(seasonal)result[0]=seasonal.id;
  else{const surprise=seasonalSurprise(s,id,selected,random);if(surprise)result[0]=surprise.id;}
  const duration=Math.ceil(minutes*60000);
  const positions=Array.from({length:24},(_,i)=>({x:Math.trunc(80.5+31*(i%6)+Math.trunc(3-random()*6)),y:184+15+26*Math.floor(i/6)+Math.trunc(3-random()*6)})).sort((a,b)=>a.y-b.y);
  const eggs=positions.map((p,i)=>{
    // Original chooses a time from the 24-entry list independently for each egg.
    const reduction=minutes*55000/23*Math.floor(random()*24);
    return {...p,id:result[i],egg:s.egg,openAt:Math.ceil(now+duration-reduction),blackAt:selected.includes(36)?null:Math.ceil(now+duration-reduction)+info.freshMinutes*60000,collected:false,status:'egg',flipped:random()<0.5,immune:selected.includes(18)};
  });
  updateKitchen(s,now);
  s.cp-=cost;
  selected.forEach(i=>s.ingredients[i]--);
  s.selected=[];
  s.batch={tool:id,level:s.toolLevels[id],egg:s.egg,ingredients:selected,started:now,ends:now+duration,eggs,rules:{version:1,originalMinutes:info.originalMinutes,reduction:info.reduction,freshMinutes:info.freshMinutes,sickness:info.sickness,kitchenLevel:s.kitchenLevel,freshness:s.progress.protection.freshness,protectSickness:s.progress.protection.sickness}};
  if(s.dirty)s.cleanCycle.dirtyAt=Math.min(s.cleanCycle.dirtyAt,now);
  if(seasonal)s.batch.seasonalRecipe=seasonal.key;
  delete s.events.seasonalRecipe;
  return s.batch;
}
// Include the short cracking/hatching animation, matching Android reminders.
export function batchReadyAt(batch){
  const eggs=batch?.eggs?.filter(e=>!e.collected)??[];
  return eggs.length?Math.max(...eggs.map(e=>Number.isFinite(e.openAt)&&e.openAt>0?e.openAt:batch.ends))+3000:null;
}
const sickExempt=[[1,2,20,30,34,35,51,52,53,54,68],[1,2,14,15,19,20,27,28,29,36]];
const burntExempt=[[0,2,5,20,25,26,27,30,31,33,34,35,53,54,68,70,110],[0,2,4,14,15,16,17,18,19,20,27,28,29,36,38,53]];
export function updateBatch(s,now=Date.now(),random=Math.random) {
  updateKitchen(s,now);
  const events=[],kitchenLevel=s.batch?.rules?.kitchenLevel??s.kitchenLevel;
  for(const e of s.batch?.eggs??[]) {
    if(e.collected) continue;
    if(e.status==='egg'&&now>e.openAt) {
      if(e.egg===0&&e.id===9&&now>e.openAt+10000)e.id=8;
      if((s.batch.rules?e.openAt>=s.cleanCycle.dirtyAt:s.dirty)&&!e.immune&&!sickExempt[e.egg].includes(e.id)&&random()<(s.batch.rules?.sickness??.4)) {
        e.id=1;
        if(kitchenLevel>=1&&random()<0.1){e.id=e.egg?19:34;if(kitchenLevel>=2&&random()<1/3){e.id=e.egg?29:53;if(kitchenLevel>=3&&random()<0.5)e.id=e.egg?36:68;}}
      }
      e.status='cracking';e.animationAt=now;events.push('break');
    }
    if(e.status==='cracking'&&now-e.animationAt>=2000){e.status='hatching';e.animationAt=now;events.push(e.egg?'duck':'chick');}
    if(e.status==='hatching'&&now-e.animationAt>=900)e.status='ready';
    if(e.status==='ready'&&e.blackAt&&now>e.blackAt&&s.batch.tool>0&&!burntExempt[e.egg].includes(e.id)) {
      if((e.egg===0&&[109,111,112,113].includes(e.id))||(e.egg===1&&[52,54,55,56].includes(e.id)))e.id=e.egg?53:110;
      else if((e.egg===0&&[69,71,72,73,74,75,76,77].includes(e.id))||(e.egg===1&&[37,39,40,41,42,43,44,45,46].includes(e.id)))e.id=e.egg?38:70;
      else {e.id=2;if(kitchenLevel>=1&&random()<1/50){e.id=e.egg?20:35;if(kitchenLevel>=2&&random()<(e.egg?1/20:1/3))e.id=e.egg?28:54;}}
    }
  }
  return events;
}
export function collect(s,index) {
  const e=s.batch?.eggs[index];
  if(!e||e.collected||!['ready','hatching'].includes(e.status))return false;
  const k=key(e.egg,e.id),wasKnown=speciesDiscovered(s,e.egg,e.id);checkedIncome(s,1);
  e.collected=true;
  s.farm[k]=Math.min(99999,(s.farm[k]??0)+1);s.total[k]=Math.min(99999,(s.total[k]??0)+1);s.cp+=1;
  if(s.progress){harvestCredit(s);if(!wasKnown&&e.egg===0&&e.id>=89&&e.id<=103)s.progress.fortune.drought=0;syncProgress(s);}
  return true;
}
export function sell(s,selection,options={},now=Date.now()) {
  advanceWorld(s,now);
  validateConsumption(s,selection,options);
  const entries=Object.entries(selection);
  for(const [k,n] of entries)if(!char(...k.split(':').map(Number))||!Number.isInteger(n)||n<0||n>availableCount(s,k))throw Error('卖出数量有误。');
  const baseIncome=entries.reduce((v,[k,n])=>v+char(...k.split(':').map(Number)).cp_1*n,0);
  const quote=basketQuote(s,selection),income=baseIncome+quote.bonus;checkedIncome(s,income);
  if(!income)throw Error('请先选择要卖出的数量。');
  entries.forEach(([k,n])=>s.farm[k]-=n);s.cp+=income;if(s.progress)s.progress.trade.credits-=quote.baskets;return income;
}
export function canBuyTool(s,id) {
  if(!Number.isInteger(id)||!tool(id))return false;
  if(id===8)return expansionUnlockInfo(s).available;
  const level=s.toolLevels[id],next=level+1;
  if(next>2)return false;
  if(level>=0)return next<=s.kitchenLevel;
  if(id===0)return true;
  if(id<6)return s.toolLevels[id-1]>=0;
  if(id===6)return s.kitchenLevel>=3;
  return s.kitchenLevel>=3&&s.toolLevels[6]>=2;
}
export function buyTool(s,id) {
  if(!canBuyTool(s,id))throw Error('尚未满足购买条件。');
  const next=(s.toolLevels[id]??-1)+1,cost=tool(id)[`lv_${next}_buy_cp`];
  if(s.cp<cost)throw Error('CP不足。');
  s.cp-=cost;s.toolLevels[id]=next;return cost;
}
export function duckUnlockInfo(s) {
  const price=2500,owned=s.duck===true,affordable=s.cp>=price;
  return {owned,price,affordable,available:!owned&&affordable,reason:owned?'已经拥有鸭蛋。':!affordable?'CP不足，还需要 '+(price-s.cp).toLocaleString('zh-CN')+' CP。':''};
}
export function buyDuck(s) {
  const info=duckUnlockInfo(s);
  if(!info.available)throw Error(info.reason);
  s.cp-=info.price;s.duck=true;return info.price;
}
export const availableIngredients=availableIngredientIds;
export function buyIngredient(s,id,count=1) {
  if(!availableIngredients(s).includes(id))throw Error('尚未解锁。');
  if(!Number.isInteger(count)||count<1)throw Error('数量有误。');
  if(Object.values(s.ingredients).reduce((a,b)=>a+b,0)+count>30)throw Error('调味料最多可持有30个。');
  const cost=ingredient(id).buy_cp*count;if(s.cp<cost)throw Error('CP不足。');
  const rebate=(s.progress?.trade.rebateRemainder??0)+cost*effects(s).rebate,refund=Math.floor(rebate/100);
  s.cp=s.cp-cost+refund;s.ingredients[id]=(s.ingredients[id]??0)+count;
  if(s.progress)s.progress.trade.rebateRemainder=rebate%100;
}
export function kitchenUpgradeInfo(s) {
  const currentLevel=s.kitchenLevel+1,maxed=currentLevel>=4;
  const targetLevel=maxed?null:currentLevel+1,cost=maxed?0:currentLevel*10000;
  // Display levels are one-based; an unowned tool is shown as level zero.
  // At the final kitchen, retain the completed Lv.3 cookware requirements.
  const requiredLevel=Math.min(currentLevel,3);
  const requirements=Array.from({length:6},(_,id)=>{
    const currentLevel=(s.toolLevels[id]??-1)+1;
    return {id,currentLevel,requiredLevel,met:currentLevel>=requiredLevel};
  });
  const toolsReady=requirements.every(r=>r.met),affordable=s.cp>=cost;
  return {maxed,currentLevel,targetLevel,cost,requirements,toolsReady,affordable,canUpgrade:!maxed&&toolsReady&&affordable,slots:Math.min(3,currentLevel)};
}
export function canUpgradeKitchen(s){const info=kitchenUpgradeInfo(s);return !info.maxed&&info.toolsReady;}
export function upgradeKitchen(s,now=Date.now()) {
  const info=kitchenUpgradeInfo(s);
  if(info.maxed)throw Error('厨房已达到最高等级。');
  if(!info.toolsReady)throw Error('请先将前六种调理用具升至当前厨房等级。');
  if(!info.affordable)throw Error('CP不足。');
  updateBatch(s,now);s.cp-=info.cost;s.kitchenLevel++;s.dirty=false;s.lastClean=now;s.lastSeen=now;
  s.cleanCycle={hours:effects(s).cleanHours,dirtyAt:now+effects(s).cleanHours*3600000};syncProgress(s);
}
// Saving or reopening the game must not restart the cleaning interval.
export const CLEAN_INTERVAL=36*3600000;
export function updateKitchen(s,now=Date.now()) {
  if(s.dirty||now<(s.cleanCycle?.dirtyAt??s.lastClean+CLEAN_INTERVAL))return false;
  s.dirty=true;return true;
}
export function resume(s,now=Date.now()) {advanceWorld(s,now);updateKitchen(s,now);s.lastSeen=now;}
export function repairCost(s,now=Date.now()){const hp=farmHP(s,now);return Math.trunc(Math.min(200,(100-hp)*(hp<=0?2:hp<30?1.5:hp<60?1.2:1)));}
// Use the original confirmation prices, including the intended Lv.4 fee.
// Its decompiled deduction function omitted Lv.4 and fell back to 100 CP.
export function kitchenCleanInfo(s,now=Date.now()){
  const interval=(s.cleanCycle?.hours??36)*3600000,elapsed=Math.max(0,now-s.lastClean),dirty=s.dirty||now>=(s.cleanCycle?.dirtyAt??s.lastClean+interval);
  const percent=dirty?100:Math.min(100,Math.floor(elapsed*100/interval));
  const fullCost=[100,150,200,250][s.kitchenLevel]??100;
  const cost=Math.ceil(fullCost*percent/100);
  return {hours:interval/3600000,nextHours:effects(s).cleanHours,percent,dirty,cost,fullCost,remaining:dirty?0:Math.max(0,interval-elapsed),canClean:percent>0,affordable:s.cp>=cost};
}
export function cleanCost(s,now=Date.now()){return kitchenCleanInfo(s,now).cost;}
export function clean(s,now=Date.now(),quote=null){
  const info=kitchenCleanInfo(s,now);
  if(!info.canClean)return false;
  // Delayed confirmations cannot charge a new price or repeat a completed clean.
  if(quote&&(quote.cost!==info.cost||quote.lastClean!==s.lastClean||quote.kitchenLevel!==s.kitchenLevel))throw Error('清洁费用或状态已变化，请重新打开打扫页面确认。');
  if(!info.affordable)throw Error('CP不足。');
  updateBatch(s,now);s.cp-=info.cost;s.dirty=false;s.lastClean=now;
  s.cleanCycle={hours:effects(s).cleanHours,dirtyAt:now+effects(s).cleanHours*3600000};return true;
}
export function repair(s,now=Date.now()){const cost=repairCost(s,now);if(s.cp<cost)throw Error('CP不足。');s.cp-=cost;s.farmFixed=now;s.farmChecked=now;}

export class SaveValidationError extends Error {
  constructor(message,code='INVALID_SAVE'){super(message);this.name='SaveValidationError';this.code=code;}
}

// Strict parsing never creates a new game on failure. The storage layer can keep
// the original payload and offer recovery without overwriting a player's save.
// Validation/migration does not advance the clock; call resume after loading.
export function normalizeSave(input,now=Date.now()) {
  const fail=path=>{throw new SaveValidationError(`存档中的 ${path} 数据无效，原存档未修改。`);};
  const record=(value,path)=>{
    if(!value||typeof value!=='object'||Array.isArray(value)||![Object.prototype,null].includes(Object.getPrototypeOf(value)))fail(path);
    return value;
  };
  const integer=(value,path,min=0,max=Number.MAX_SAFE_INTEGER)=>{if(!Number.isSafeInteger(value)||value<min||value>max)fail(path);return value;};
  const number=(value,path,min=0,max=Number.MAX_SAFE_INTEGER)=>{if(!Number.isFinite(value)||value<min||value>max)fail(path);return value;};
  const boolean=(value,path,fallback)=>{if(value===undefined&&fallback!==undefined)return fallback;if(typeof value!=='boolean')fail(path);return value;};
  record(input,'根节点');
  if(![1,2,3].includes(input.version)){
    if(Number.isInteger(input.version)&&input.version>3)throw new SaveValidationError('这份存档来自更新的游戏版本，请更新应用后再读取。','UNSUPPORTED_SAVE_VERSION');
    fail('版本');
  }
  const legacy=input.version===1,toolCount=legacy?8:TOOL_COUNT;
  const cp=integer(input.cp,'CP'),kitchenLevel=integer(input.kitchenLevel,'厨房等级',0,3);
  if(!Array.isArray(input.toolLevels)||input.toolLevels.length!==toolCount)fail('厨具');
  const toolLevels=input.toolLevels.map((level,id)=>integer(level,`厨具 ${id}`, -1,2));
  if(legacy)toolLevels.push(-1);
  const stockMap=(source,path,isIngredient=false)=>{
    record(source,path);const result={};
    for(const [key,value]of Object.entries(source)){
      if(isIngredient){if(!/^(0|[1-9]\d*)$/.test(key)||!ingredient(Number(key)))fail(`${path} ${key}`);}
      else{
        if(!/^[01]:(0|[1-9]\d*)$/.test(key))fail(`${path} ${key}`);
        const [egg,id]=key.split(':').map(Number);
        if(!char(egg,id)||(legacy&&id>=(egg?57:114)))fail(`${path} ${key}`);
      }
      result[key]=integer(value,`${path} ${key}`,0,isIngredient?30:99999);
    }
    if(isIngredient&&Object.values(result).reduce((sum,value)=>sum+value,0)>30)fail('调味料总数');
    return result;
  };
  const ingredients=stockMap(input.ingredients,'调味料',true);
  const ingredientList=(source,path,max=3)=>{
    if(!Array.isArray(source)||source.length>max)fail(path);
    source.forEach(id=>{if(!Number.isInteger(id)||!ingredient(id))fail(path);});
    return [...new Set(source)];
  };
  const selected=ingredientList(input.selected??[],'选中的调味料');
  const farm=stockMap(input.farm,'农场'),total=stockMap(input.total,'图鉴');
  const egg=integer(input.egg??0,'蛋种',0,1);
  const duck=boolean(input.duck,'鸭蛋',false);
  if(egg===1&&!duck)fail('鸭蛋蛋种');
  const lastSeen=number(input.lastSeen??now,'上次游玩时间');
  const lastClean=number(input.lastClean??lastSeen,'清洁时间');
  const farmFixed=number(input.farmFixed??lastSeen,'农场整修时间');
  const farmChecked=number(input.farmChecked??farmFixed,'农场检查时间');
  let batch=null;
  if(input.batch!==null&&input.batch!==undefined){
    const source=record(input.batch,'当前调理');
    const batchTool=integer(source.tool,'当前厨具',0,toolCount-1);
    const level=integer(source.level,'当前厨具等级',0,2),batchEgg=integer(source.egg,'当前蛋种',0,1);
    if(toolLevels[batchTool]<level||(batchEgg===1&&!duck)||(batchTool===8&&batchEgg!==0))fail('当前调理条件');
    const started=number(source.started,'开始时间'),ends=number(source.ends,'完成时间');
    if(ends<=started)fail('调理时长');
    if(!Array.isArray(source.eggs)||source.eggs.length!==24)fail('当前蛋数量');
    const eggs=source.eggs.map((sourceEgg,index)=>{
      record(sourceEgg,`第 ${index+1} 枚蛋`);
      const egg=integer(sourceEgg.egg,'蛋种',0,1),id=integer(sourceEgg.id,'品种编号');
      if(egg!==batchEgg||!char(egg,id)||(legacy&&id>=(egg?57:114)))fail('当前品种');
      if(!['egg','cracking','hatching','ready'].includes(sourceEgg.status))fail('孵化状态');
      const openAt=number(sourceEgg.openAt,'破壳时间');
      if(openAt<started||openAt>ends)fail('破壳时间');
      const blackAt=sourceEgg.blackAt===null?null:number(sourceEgg.blackAt,'过熟时间');
      if(blackAt!==null&&blackAt<=openAt)fail('过熟时间');
      const animationAt=sourceEgg.animationAt===undefined?undefined:number(sourceEgg.animationAt,'动画开始时间');
      if(sourceEgg.status!=='egg'&&animationAt===undefined)fail('孵化动画');
      return {...sourceEgg,egg,id,openAt,blackAt,x:number(sourceEgg.x,'蛋位置 X',0,320),y:number(sourceEgg.y,'蛋位置 Y',0,568),
        collected:boolean(sourceEgg.collected,'收取状态'),flipped:boolean(sourceEgg.flipped,'朝向',false),immune:boolean(sourceEgg.immune,'保护状态',false),
        ...(animationAt===undefined?{}:{animationAt})};
    });
    batch={...source,tool:batchTool,level,egg:batchEgg,started,ends,ingredients:ingredientList(source.ingredients??[],'本批调味料'),eggs};
    if(source.alarmed!==undefined)batch.alarmed=boolean(source.alarmed,'提醒状态');
  }
  const events=record(input.events??{},'活动记录');
  const jsonValue=(value,path)=>{
    if(value===null||['string','boolean'].includes(typeof value))return value;
    if(typeof value==='number'){if(!Number.isFinite(value))fail(path);return value;}
    if(Array.isArray(value))return value.map(item=>jsonValue(item,path));
    record(value,path);return Object.fromEntries(Object.entries(value).map(([key,item])=>[key,jsonValue(item,path)]));
  };
  const normalized={...input,version:3,cp,kitchenLevel,toolLevels,ingredients,selected,farm,total,egg,duck,batch,lastSeen,lastClean,farmFixed,farmChecked,
    dirty:boolean(input.dirty,'清洁状态',false),alarm:boolean(input.alarm,'孵化提醒',false),music:boolean(input.music,'背景音乐',true),sound:boolean(input.sound,'游戏音效',true),events:jsonValue(events,'活动记录')};
  if(input.version<3){
    for(const [k,n]of Object.entries(farm))if(n>0&&!(total[k]>0))total[k]=1;
    normalized.progress=freshProgress(normalized);
    normalized.cleanCycle={hours:36,dirtyAt:normalized.dirty?Math.min(lastSeen,lastClean+36*3600000):lastClean+36*3600000};
    if(batch)delete batch.rules;
  }else{normalized.progress=jsonValue(input.progress,'成长');normalized.cleanCycle=jsonValue(input.cleanCycle,'清洁周期');}
  validateProgress(normalized,fail);
  return normalized;
}

export function parseSave(text,now=Date.now()) {
  if(typeof text!=='string')throw new SaveValidationError('存档内容不是有效文本，原存档未修改。');
  let value;
  try{value=JSON.parse(text);}catch{throw new SaveValidationError('存档文件无法读取，原存档未修改。');}
  return normalizeSave(value,now);
}

// Compatibility helper for the old browser entry point. New persistence uses
// parseSave directly so errors remain recoverable instead of silently resetting.
export function readSave(storage,k,now=Date.now()) {
  try {
    const raw=storage.getItem(k);if(!raw)return freshState(now);
    const s=parseSave(raw,now);
    resume(s,now);return s;
  } catch {return freshState(now);}
}
