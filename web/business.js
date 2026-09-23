import {REGIONAL,resolveSpecies,SPECIES_TRADE} from './content-registry.js';
import {effects,checkedIncome} from './progression.js';
import {RULES} from './integration-data.js';
import {freeCount,homeCount} from './inventory.js';
import {assignMenuRoles,menuSnapshot,menuFit,menuSalesWitness,businessUnlockInfo,menuUnlockInfo} from './menu-model.js';
import {reduceFacts} from './facts.js';
import {orderMilestone} from './orders.js';
import {visitorCandidates,presentVisitor} from './regulars.js';
import {assertNewOperation} from './rollback-policy.js';

export const BUSINESS_RULES_VERSION=1;
export const BUSINESS_WINDOW_MS=2*3600000;
export const BUSINESS_DURATION_MS=24*3600000;
// All eight menus are defined; each still needs its own unlock (menuUnlockInfo).
export const ACTIVE_BUSINESS_MENUS=Object.freeze(['MN1','MN2','MN3','MN4','MN5','MN6','MN7','MN8']);
export function freshBusiness(){return {sequence:0,active:null,lastReport:null,visitorProgress:0,visitorSequence:0,themeRemainder:0};}
export const businessCapacity=s=>s.kitchenLevel===0?24:s.kitchenLevel===1?48:72;
const sum=map=>Object.values(map).reduce((a,b)=>a+b,0);
const increment=(map,key,n)=>{map[key]=(map[key]??0)+n;};
const integer=(value,min,max,message)=>{if(!Number.isSafeInteger(value)||value<min||value>max)throw Error(message);};
const business=s=>{const b=s.expansion.business;if(!b)throw Error('营业尚未完成存档迁移');return b;};

export function businessBonusQuote(snapshot,selection,creditLimit){
  const remaining={...selection},basketItems=[],platterItems=[];let credits=creditLimit,baskets=0,platters=0;
  for(const [key,n]of Object.entries(selection))if(snapshot.basket&&snapshot.basketEligible.includes(key)){
    const count=Math.min(credits,Math.floor(n/24));if(count){credits-=count;baskets+=count;remaining[key]-=count*24;basketItems.push({key,count:count*24});}
  }
  if(snapshot.platter)while(credits>0){const keys=Object.keys(remaining).filter(k=>snapshot.platterEligible.includes(k)&&remaining[k]>=3).sort((a,b)=>remaining[b]-remaining[a]||a.localeCompare(b)).slice(0,4);if(keys.length<4)break;keys.forEach(k=>remaining[k]-=3);platterItems.push(keys);platters++;credits--;}
  return {baskets,platters,basketItems,platterItems,creditsUsed:baskets+platters,bonusCP:baskets*snapshot.basketBonus+platters*snapshot.platterBonus};
}

export function prepareBusiness(s,{menuId='MN1',roles,stock,useRewards=false,overrideKeepOne=false,overrideLocks=false,tendency='regulars',pinnedRegular=null}={}){
  assertNewOperation('business');
  if(business(s).active)throw Error('先收摊，再准备下一单营业');
  if(!ACTIVE_BUSINESS_MENUS.includes(menuId))throw Error('这张菜单尚未开放营业');
  const access=businessUnlockInfo(s),menuAccess=menuUnlockInfo(s,menuId);if(!access.met||!menuAccess.met)throw Error([...access.missing,...menuAccess.missing].join('；'));
  if(!stock||typeof stock!=='object'||Array.isArray(stock))throw Error('请选择营业备货');
  const keys=Object.keys(stock);if(!keys.length||keys.length>6)throw Error('每单选择1至6种食用出品');
  const capacity=businessCapacity(s),policy=s.expansion.inventoryPolicy??{keepOne:true,collectionLocks:[]};
  const lockKeys=Array.isArray(policy.collectionLocks)?policy.collectionLocks:Object.keys(policy.collectionLocks??{}).filter(k=>policy.collectionLocks[k]);
  for(const key of keys){const c=resolveSpecies(key);if(!c?.edible)throw Error('营业只接受可食用出品');integer(stock[key],1,capacity,'备货数量无效');
    if(freeCount(s,key)<stock[key])throw Error('自由库存不足，可能已用于寻访或采购');
    if(!overrideKeepOne&&policy.keepOne!==false&&homeCount(s,key)-stock[key]<1)throw Error('默认在家留1只，确认后可以全部备货');
    if(!overrideLocks&&lockKeys.includes(key))throw Error('这位伙伴已锁定收藏，请先取消锁定或明确确认使用');
  }
  if(sum(stock)>capacity)throw Error(`厨房本次最多备货${capacity}只`);
  if(!['regulars','discovery'].includes(tendency))throw Error('经营倾向无效');
  const assigned=assignMenuRoles(menuId,stock,roles),snapshot=menuSnapshot(s,menuId,stock,assigned),e=effects(s);
  const prices=Object.fromEntries(keys.map(key=>[key,{baseCP:resolveSpecies(key).cp_1,markupPercent:SPECIES_TRADE[key]?.category===s.progress.trade.category?e.markup:0}]));
  const rewards={basket:e.basket,platter:e.platter,basketBonus:e.basketBonus,platterBonus:e.platterBonus,basketEligible:[...RULES.trade.eligibleSpecies],platterEligible:keys.filter(k=>SPECIES_TRADE[k]?.platter)};
  const creditReserve=useRewards?businessBonusQuote(rewards,stock,s.progress.trade.credits).creditsUsed:0;
  // Regular candidates are frozen at opening (pinned first, then by tendency).
  const candidates=s.expansion.regulars?visitorCandidates(s,{pinned:pinnedRegular,tendency}):[];
  return {menuId,roles:assigned,stock:{...stock},initialStock:{...stock},snapshot,prices,rewards,creditReserve,useRewards:!!useRewards,tendency,capacity,candidates,fit:menuFit(menuId,stock,assigned,snapshot)};
}

export function openBusiness(s,options,now){
  integer(now,0,Number.MAX_SAFE_INTEGER-BUSINESS_DURATION_MS,'营业时间无效');
  const plan=prepareBusiness(s,options),b=business(s);integer(b.sequence+1,1,Number.MAX_SAFE_INTEGER,'营业序号超出范围');
  const session={...plan,id:`business-${b.sequence+1}`,rulesVersion:BUSINESS_RULES_VERSION,startAt:now,hardEndAt:now+BUSINESS_DURATION_MS,processedWindow:0,roleCursor:0,
    soldByKey:{},roleSales:{},fullSoldByKey:{},fullRoleSales:{},baseCP:0,markupCP:0,themeCP:0,bonusCP:0,totalSold:0,windowReports:[],
    recordedValid:false,recordedComplete:false,bonusSettled:false,pendingCloseAt:null,visitorCandidates:plan.candidates,visitorEvents:0};
  delete session.fit;delete session.candidates;b.sequence++;b.active=session;
  return {sessionId:session.id,stock:{...session.stock},creditReserve:session.creditReserve,fit:plan.fit};
}

function nextSale(session){
  for(let searched=0;searched<session.roles.length;searched++){
    const index=session.roleCursor%session.roles.length,role=session.roles[index];session.roleCursor=(index+1)%session.roles.length;
    const key=role.keys.find(k=>session.stock[k]>0);if(key)return {key,roleId:role.roleId};
  }
  return null;
}

export function advanceBusiness(s,targetAt,{deferClose=false}={}){
  integer(targetAt,0,Number.MAX_SAFE_INTEGER,'营业时间无效');const b=business(s),session=b.active;if(!session)return {processed:0,sold:0,closed:false};
  if(session.rulesVersion!==BUSINESS_RULES_VERSION)throw Error('不支持的营业规则版本');
  const maxWindow=Math.max(0,Math.min(12,Math.floor((Math.min(targetAt,session.hardEndAt)-session.startAt)/BUSINESS_WINDOW_MS)));
  let processed=0,sold=0;
  for(let window=session.processedWindow+1;window<=maxWindow;window++){
    if(session.pendingCloseAt!==null)break;
    const fit=menuFit(session.menuId,session.stock,session.roles,session.snapshot),entries=[],at=session.startAt+window*BUSINESS_WINDOW_MS;
    let baseCP=0,markupUnits=s.progress.trade.markupRemainder,themeUnits=b.themeRemainder;
    for(let i=0;i<6;i++){
      const sale=nextSale(session);if(!sale)break;
      const {key,roleId}=sale,price=session.prices[key];
      if(!Number.isSafeInteger(s.farm[key])||s.farm[key]<session.stock[key])throw Error('营业库存不一致');
      session.stock[key]--;s.farm[key]--;increment(session.soldByKey,key,1);increment(session.roleSales,roleId,1);
      if(fit.complete){increment(session.fullSoldByKey,key,1);increment(session.fullRoleSales,roleId,1);}
      const previousMarkup=Math.floor(markupUnits/100),previousTheme=Math.floor(themeUnits/100);
      baseCP+=price.baseCP;markupUnits+=price.baseCP*price.markupPercent;themeUnits+=Math.min(price.baseCP*fit.ratePercent,200);
      entries.push({key,roleId,quantity:1,baseCP:price.baseCP,markupCP:Math.floor(markupUnits/100)-previousMarkup,themeCP:Math.floor(themeUnits/100)-previousTheme});
    }
    const markupCP=Math.floor(markupUnits/100),themeCP=Math.floor(themeUnits/100),income=baseCP+markupCP+themeCP;
    s.cp=checkedIncome(s,income);s.progress.trade.markupRemainder=markupUnits%100;b.themeRemainder=themeUnits%100;
    session.baseCP+=baseCP;session.markupCP+=markupCP;session.themeCP+=themeCP;session.totalSold+=entries.length;session.processedWindow=window;
    session.windowReports.push({index:window,at,tier:fit.tier,entries,baseCP,markupCP,themeCP});
    const events=entries.map(e=>({kind:'businessSale',...e,sessionId:session.id,windowIndex:window,menuId:session.menuId,tier:fit.tier}));
    const witness=menuSalesWitness(session.menuId,session.soldByKey,session.roleSales,session.fullSoldByKey,session.fullRoleSales);
    events.push({kind:'businessWitness',sessionId:session.id,menuId:session.menuId,soldByKey:session.soldByKey,roleSales:session.roleSales,fullSoldByKey:session.fullSoldByKey,fullRoleSales:session.fullRoleSales,...witness});
    reduceFacts(s,events);session.recordedValid=witness.valid;session.recordedComplete=witness.complete;
    const visitors=Math.floor((b.visitorProgress+entries.length)/12);b.visitorProgress=(b.visitorProgress+entries.length)%12;
    integer(b.visitorSequence+visitors,0,Number.MAX_SAFE_INTEGER,'来客序号超出范围');b.visitorSequence+=visitors;session.visitorEvents+=visitors;
    // Each visitor step is a real business milestone for situational purchases.
    // A visitor may also bring the next queued regular story (no CP).
    for(let v=0;v<visitors;v++){orderMilestone(s,at,'visitor');presentVisitor(s,session);}
    processed++;sold+=entries.length;
    if(!sum(session.stock))session.pendingCloseAt=at;
  }
  if(targetAt>=session.hardEndAt&&session.pendingCloseAt===null)session.pendingCloseAt=session.hardEndAt;
  if(session.pendingCloseAt!==null&&!deferClose){settleBusinessClose(s,session.pendingCloseAt,session.totalSold===sum(session.initialStock)?'sold-out':'deadline');return {processed,sold,closed:true};}
  return {processed,sold,closed:false,pendingCloseAt:session.pendingCloseAt};
}

// Timeline uses this only after its at-the-release-boundary farm-loss check.
export function settleBusinessClose(s,closedAt,reason='manual'){
  const b=business(s),session=b.active;if(!session)return b.lastReport;
  integer(closedAt,session.startAt,Number.MAX_SAFE_INTEGER,'收摊时间无效');
  if(session.bonusSettled)throw Error('营业奖励已结清');
  const bonus=businessBonusQuote(session.rewards,session.soldByKey,session.creditReserve);
  if(s.progress.trade.credits<bonus.creditsUsed)throw Error('预留经营次数不足');
  s.cp=checkedIncome(s,bonus.bonusCP);s.progress.trade.credits-=bonus.creditsUsed;
  session.bonusSettled=true;session.bonusCP=bonus.bonusCP;
  const report={id:session.id,rulesVersion:session.rulesVersion,menuId:session.menuId,startAt:session.startAt,closedAt,reason,totalSold:session.totalSold,
    initialStock:{...session.initialStock},soldByKey:{...session.soldByKey},remainingStock:{...session.stock},baseCP:session.baseCP,markupCP:session.markupCP,themeCP:session.themeCP,bonusCP:session.bonusCP,
    income:session.baseCP+session.markupCP+session.themeCP+session.bonusCP,creditsUsed:bonus.creditsUsed,creditsReleased:session.creditReserve-bonus.creditsUsed,
    baskets:bonus.baskets,platters:bonus.platters,validMenu:session.recordedValid,completeMenu:session.recordedComplete,visitorEvents:session.visitorEvents,windowReports:structuredClone(session.windowReports),bonusSettled:true};
  b.lastReport=report;b.active=null;return report;
}
export function closeBusiness(s,now){
  const b=business(s);if(!b.active)return b.lastReport;
  advanceBusiness(s,now,{deferClose:true});const session=b.active;
  return settleBusinessClose(s,session.pendingCloseAt??Math.max(session.startAt,now),session.pendingCloseAt===null?'manual':session.totalSold===sum(session.initialStock)?'sold-out':'deadline');
}
export function nextBusinessBoundary(s){const a=s.expansion?.business?.active;return !a?null:a.pendingCloseAt??Math.min(a.hardEndAt,a.startAt+(a.processedWindow+1)*BUSINESS_WINDOW_MS);}
