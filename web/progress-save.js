import {validateRegionalTrip} from './regional-exploration.js';
import {ROUTES} from './exploration.js';
import {validateBatchPlan} from './batch-plan.js';
import {RULES} from './integration-data.js';
import {SPECIES_ABILITIES as ABILITIES} from './content-registry.js';
import {earnedSources,skillPoints,skillGate,SKILL_BY_ID,SKILLS,TRADE_CATEGORIES,MAX_SKILL_POINTS} from './progression.js';
import {RECIPE_CATALOG,recipeId} from './recipe-book.js';
export function freshProgress(s){
  return {skillVersion:2,migrationRespec:false,migrationNotice:false,leftovers:[],hotStove:null,lastHarvest:null,sources:earnedSources(s),skills:{},respecAt:null,protection:{freshness:true,sickness:true,calm:false},trade:{initialGranted:false,credits:0,harvestProgress:0,rebateRemainder:0,markupRemainder:0,category:null},
    fortune:{lastTarget:Number.isInteger(s.events?.gift_tool_2_68_character_id)&&s.events.gift_tool_2_68_character_id>=89&&s.events.gift_tool_2_68_character_id<=103?s.events.gift_tool_2_68_character_id:null,drought:0},
    knowledge:{facts:[],recipes:[]},orders:{},trip:null,tripSequence:0,lastTeam:[],routeFailures:{yard:0,water:0,wood:0},logicalAt:0,tutorialSeen:false};
}
// Reject malformed new state; migration alone may create defaults for old saves.
export function validateProgress(s,fail){
  const p=s.progress;
  const object=(v,k)=>{if(!v||typeof v!=='object'||Array.isArray(v))fail(k);};
  const int=(v,k,min=0,max=Number.MAX_SAFE_INTEGER)=>{if(!Number.isSafeInteger(v)||v<min||v>max)fail(k);};
  const num=(v,k,min=0,max=Number.MAX_SAFE_INTEGER)=>{if(!Number.isFinite(v)||v<min||v>max)fail(k);};
  const bool=(v,k)=>{if(typeof v!=='boolean')fail(k);};
  const list=(v,k,max=2000)=>{if(!Array.isArray(v)||v.length>max||new Set(v).size!==v.length)fail(k);};
  object(p,'成长');object(p.sources,'点数来源');object(p.skills,'手艺');
  const earned=earnedSources(s);
  for(const [key,value]of Object.entries(p.sources))if(earned[key]!==value)fail('点数来源 '+key);
  Object.assign(p.sources,earned);
  for(const [id,lv]of Object.entries(p.skills)){int(lv,'技能等级',1,1);if(skillGate(s,id,lv))fail('技能前置 '+id);}
  const replay={...s,progress:{...p,skills:{}}};for(const def of SKILLS)if(p.skills[def.id]){if(skillGate(replay,def.id))fail('手艺学习顺序 '+def.id);replay.progress.skills[def.id]=1;}
  int(p.skillVersion,'手艺版本',2,2);bool(p.migrationRespec,'迁移重配');bool(p.migrationNotice,'迁移提示');
  if(!Array.isArray(p.leftovers)||p.leftovers.length>5||p.leftovers.some(id=>!Number.isInteger(id)||id<0||id>(s.version>=4?82:74)||[68,69,70].includes(id)))fail('待收余料');
  if(p.replicate!==undefined&&p.replicate!==null&&(typeof p.replicate!=='string'||!ABILITIES[p.replicate]))fail('复刻目标');
  if(p.hotStove!==null){object(p.hotStove,'接锅记录');num(p.hotStove.at,'接锅时间');if(typeof p.hotStove.signature!=='string')fail('接锅配方');}
  if(p.lastHarvest!==null){object(p.lastHarvest,'收成摘要');int(p.lastHarvest.base,'基础收成',24,24);int(p.lastHarvest.bonus,'收成奖励',0,24);}
  if(skillPoints(s).available<0||skillPoints(s).earned>MAX_SKILL_POINTS)fail('手艺点');
  if(p.respecAt!==null)num(p.respecAt,'重新分配时间');
  object(p.protection,'保护选项');bool(p.protection.freshness,'保鲜开关');bool(p.protection.sickness,'病变开关');bool(p.protection.calm,'安心模式');
  object(p.trade,'经营');bool(p.trade.initialGranted,'初始额度');int(p.trade.credits,'额度',0,6);int(p.trade.harvestProgress,'收取进度',0,23);int(p.trade.rebateRemainder,'返利余数',0,99);
  int(p.trade.markupRemainder,'招牌小数',0,99);if(p.trade.category!==null&&!TRADE_CATEGORIES.includes(p.trade.category))fail('招牌分类');
  if((p.skills['TRADE-3']||p.skills['TRADE-4'])&&!p.trade.initialGranted)fail('经营初次学习标记');
  object(p.fortune,'签礼');if(p.fortune.lastTarget!==null)int(p.fortune.lastTarget,'上次签',89,103);int(p.fortune.drought,'签礼计数');
  object(p.knowledge,'知识');list(p.knowledge.facts,'线索事实');list(p.knowledge.recipes,'配方知识');
  const recipes=new Map(RECIPE_CATALOG.map(r=>[recipeId(r),r]));
  const facts=new Set(RECIPE_CATALOG.flatMap(r=>[r.key+':L1',...[2,3,4,5].map(l=>recipeId(r)+':L'+l)]));
  for(const f of p.knowledge.facts)if(!facts.has(f))fail('线索事实身份');
  for(const r of p.knowledge.recipes)if(!recipes.has(r))fail('配方身份');
  object(p.orders,'采购');
  for(const [id,o]of Object.entries(p.orders)){
    const cfg=RULES.storyOrders.find(r=>r.id===id);if(!cfg)fail('采购编号');object(o,'采购状态');bool(o.accepted,'接单状态');bool(o.completed,'采购完成');
    const item=(cfg.items??cfg.chooseOne).find(c=>c.species===o.choice);if(!item||!o.accepted)fail('采购选项');
    int(o.delivered,'采购交付数',0,item.count);if(o.completed!==(o.delivered===item.count))fail('采购结算');
    if(cfg.previous&&!p.orders[cfg.previous]?.completed)fail('采购先后');
  }
  num(p.logicalAt,'逻辑时钟');int(p.tripSequence,'探索序号');list(p.lastTeam,'上次队伍',3);for(const k of p.lastTeam)if(!ABILITIES[k])fail('上次队伍身份');
  bool(p.tutorialSeen,'成长提示');object(p.routeFailures,'路线保底');for(const r of RULES.exploration.routes)int(p.routeFailures[r.id],'路线保底',0,7);
  // The bay counter is sparse: created by its first counted trip, never back-filled.
  if(p.routeFailures.bay!==undefined)int(p.routeFailures.bay,'路线保底',0,7);if(Object.keys(p.routeFailures).some(k=>!ROUTES.some(r=>r.id===k)))fail('路线保底身份');
  if(p.trip!==null){
    const t=p.trip;object(t,'探索');int(t.version,'探索规则版本',1,s.version>=4?2:1);if(t.version===2)validateRegionalTrip(t,fail);
    if(t.id!=='trip-'+p.tripSequence||p.tripSequence<1)fail('探索事务身份');
    const r=ROUTES.find(r=>r.id===t.routeId);if(!r)fail('探索路线');if(r.id==='bay'&&t.version!==2)fail('海湾只接受地区寻访票据');
    if(!['running','returned','settled','recalled'].includes(t.status))fail('探索状态');
    list(t.members,'探索队员',3);if(!t.members.length)fail('空队伍');
    for(const key of t.members)if(!ABILITIES[key]||(t.status==='running'&&(s.farm[key]??0)<1))fail('探索占用 '+key);
    num(t.startedAt,'出发时间');num(t.endAt,'归队时间');if(t.endAt-t.startedAt!==r.hours*3600000*(t.snapshot?.light?.8:1))fail('路线时长');
    if(!Array.isArray(t.remaining)||t.remaining.length>r.baseUnits+1||t.remaining.some(id=>!Number.isInteger(id)||(id!==0&&!r.pool.includes(id)&&!(t.version===2&&(t.regional.intro?.materialId===id||t.regional.sampling?.materialId===id||t.cargo?.rewardMaterial===id)))))fail('探索材料');
    bool(t.clueHit,'线索票据');bool(t.clueProcessed,'线索结算');bool(t.special,'样本叙事');
    if(!Array.isArray(t.clueOrder)||t.clueOrder.length>386)fail('线索顺序');
    const checkClue=c=>{object(c,'线索');const path=recipes.get(c.recipeId);if(!path||path.key!==c.key||![1,2].includes(c.level)||c.fact!==(c.level===1?c.key+':L1':c.recipeId+':L2'))fail('线索票据身份');};
    t.clueOrder.forEach(checkClue);if(new Set(t.clueOrder.map(c=>c.fact)).size!==t.clueOrder.length)fail('重复线索票据');
    if(t.clueResult!==null){checkClue(t.clueResult);if(!t.clueProcessed||!t.clueHit||!p.knowledge.facts.includes(t.clueResult.fact))fail('线索发放状态');}
    if(t.status==='running'&&(t.clueProcessed||t.clueResult!==null))fail('在途奖励提前结算');
    if(['settled','recalled'].includes(t.status)&&(t.remaining.length||!t.clueProcessed))fail('未清空奖励篮');
    if(['returned','settled'].includes(t.status)&&t.returnedAt!==t.endAt)fail('归队结算时间');
    if(t.status==='recalled'){num(t.recalledAt,'召回时间');if(t.recalledAt>=t.endAt||t.recalledAt<t.startedAt)fail('召回边界');}
    object(t.snapshot,'探索快照');const x=t.snapshot;
    int(x.G,'采集快照',2,12);int(x.F,'发现快照',2,12);int(x.A,'环境快照',0,3);
    if(x.G+x.F!==6*t.members.length||x.A>t.members.length)fail('能力总额');
    num(x.materialChance,'材料概率',0,.6);num(x.clueChance,'线索概率',0,.55);
    if(![6,8].includes(x.hardAttempt))fail('保底快照');bool(x.bonus,'额外材料');
    if(!Array.isArray(x.directed)||x.directed.length>2||x.directed.some(id=>!r.pool.includes(id)))fail('定向材料快照');
    if(x.priority!==null&&!t.clueOrder.some(c=>c.key===x.priority))fail('定向线索快照');
    object(x.skills,'手艺快照');for(const [id,lv]of Object.entries(x.skills))if(!(SKILL_BY_ID[id]&&lv===1)&&(!Array.isArray(RULES.skillCosts[id.split('-')[1]])||!['CUL','HOME','TRADE','OBS','TRIP'].includes(id.split('-')[0])||!Number.isInteger(lv)||lv<1||lv>RULES.skillCosts[id.split('-')[1]].length))fail('快照节点');
    if(x.light!==undefined){bool(x.light,'轻装快照');if(x.light&&r.id==='yard')fail('菜园轻装');}
    if(t.cpReward!==undefined){int(t.cpReward,'寻访CP',0,r.baseUnits*6);bool(t.cpProcessed,'寻访CP结算');if(t.status==='running'&&t.cpProcessed)fail('提前发放寻访CP');}
    if(t.status==='running'&&t.remaining.length!==r.baseUnits-(x.light?1:0)+(x.bonus?1:0))fail('在途材料票据份数');
  }
  if(s.batch?.rules?.version===3)validateBatchPlan(s,fail);
  const c=s.cleanCycle;object(c,'清洁周期');if(!RULES.housekeeping.cleanHours.includes(c.hours))fail('清洁周期时长');num(c.dirtyAt,'脏污起点');if(c.dirtyAt>s.lastClean+c.hours*3600000)fail('清洁到期');
  if(s.batch?.rules){const b=s.batch.rules;object(b,'调理快照');int(b.version,'调理规则版本',1,s.version>=4?3:2);num(b.originalMinutes,'原调理时长',6,360);num(b.reduction,'减时',0,.25);num(b.freshMinutes,'保鲜',12,810);if(![.2,.4].includes(b.sickness))fail('病变率');int(b.kitchenLevel,'调理厨房',0,3);bool(b.freshness,'保鲜选项');bool(b.protectSickness,'防病选项');if(b.version>=2){bool(b.calm,'安心批次');bool(b.pickGold,'拾金快照');bool(b.returnEligible,'返料资格');bool(b.finished,'批次结算');int(b.harvestBonus,'拾金总计',0,24);if(b.returnTicket!==null){int(b.returnTicket,'返料票据',0,s.version>=4?82:74);if(!b.returnEligible||[68,69,70].includes(b.returnTicket)||!s.batch.ingredients.includes(b.returnTicket))fail('返料票据来源');}for(const egg of s.batch.eggs)bool(egg.gold,'拾金票据');}}
}

// Old skill allocations are refunded once; clocks, knowledge, rewards and active snapshots survive unchanged.
export function migrateSkills(s,fail){
 const p=s.progress;if(p.skillVersion===2)return;
 if(p.skillVersion!==undefined)fail('未知手艺版本');
 let spent=0;
 for(const [id,lv]of Object.entries(p.skills??{})){
  const [b,n]=id.split('-'),costs=RULES.skillCosts[n];
  if(!['CUL','HOME','TRADE','OBS','TRIP'].includes(b)||!Array.isArray(costs)||!Number.isInteger(lv)||lv<1||lv>costs.length)fail('旧手艺节点');
  spent+=costs.slice(0,lv).reduce((a,b)=>a+b,0);
 }
 if(spent>Object.values(earnedSources(s)).reduce((a,b)=>a+b,0))fail('旧手艺透支');
 p.skillVersion=2;p.skills={};p.migrationRespec=true;p.migrationNotice=true;
 p.leftovers=[];p.hotStove=null;p.lastHarvest=null;p.protection.calm=false;p.trade.markupRemainder=0;p.trade.category=null;
}
