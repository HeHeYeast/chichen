import {materialCapacity,materialRoom} from './material-capacity.js';
import {economicRandom} from './rng.js';
import {RULES} from './integration-data.js';

import {speciesDiscovered,collectedTotal,discoveryCount} from './species-state.js';
import {SKILLS,SKILL_BY_ID,TRADE_CATEGORIES} from './skill-data.js';
import {SPECIES_TRADE as TRADE_SPECIES} from './content-registry.js';
import {GAME_DATA} from './content-pack.js';
export {SKILLS,SKILL_BY_ID,TRADE_CATEGORIES};
export const SPECIES_SOURCE_LIMIT=240,MAX_SKILL_POINTS=64;
export const BRANCHES={CUL:'料理',HOME:'持家',TRADE:'经营',OBS:'观察',TRIP:'寻访'};
export const rank=(s,id)=>s.progress?.skills?.[id]??0;
export {collectedTotal,discoveryCount};
export function earnedSources(s){
 const H=collectedTotal(s),D=discoveryCount(s),sources={};
 if(H>=24)sources['harvest:24']=2;
 // One point per five discoveries through the full 241-species registry (48 sources, 64 total).
 for(let i=5;i<=Math.min(SPECIES_SOURCE_LIMIT,D);i+=5)sources['species:'+i]=1;
 for(let i=1;i<=s.kitchenLevel;i++)sources['kitchen:'+i]=2;
 for(const h of RULES.skillPoints.collectionMilestones)if(H>=h)sources['harvest:'+h]=2;
 return sources;
}
export function syncProgress(s){Object.assign(s.progress.sources,earnedSources(s));}
export function skillPoints(s){
 const earned=Object.values({...s.progress?.sources,...earnedSources(s)}).reduce((a,b)=>a+b,0);
 const spent=Object.entries(s.progress?.skills??{}).reduce((n,[id,lv])=>n+(SKILL_BY_ID[id]?.cost??0)*lv,0);
 return {earned,spent,available:earned-spent};
}
export function skillRequirements(s,id){
 const def=SKILL_BY_ID[id];if(!def)return [{met:false,text:'无效手艺'}];
 const {branch,node}=def,H=collectedTotal(s),D=discoveryCount(s);
 const spent=SKILLS.filter(n=>n.branch===branch&&n.node!=='S'&&n.id!==id&&rank(s,n.id)).reduce((a,n)=>a+n.cost,0);
 const rows=[],add=(met,text)=>rows.push({met:!!met,text});
 const h=node==='S'?2000:node==='5'?1000:['3','4'].includes(node)?240:24;
 const d=node==='S'?60:node==='5'?24:['3','4'].includes(node)?12:node==='2'?8:0;
 add(H>=h,`累计收取 ${Math.min(H,h)}/${h} 只`);if(d)add(D>=d,`发现 ${Math.min(D,d)}/${d} 种`);
 if(['3','4','5','S'].includes(node)){const n=node==='S'?8:node==='5'?5:2;add(spent>=n,`${BRANCHES[branch]}普通手艺已投入 ${spent}/${n} 点`);}
 if(['5','S'].includes(node)){const k=node==='S'?2:1;add(s.kitchenLevel>=k,`厨房 Lv.${s.kitchenLevel+1} / Lv.${k+1}`);}
 if(branch==='TRIP')add(H>=120&&D>=5,`寻访开放：收取 ${Math.min(H,120)}/120 只、发现 ${Math.min(D,5)}/5 种`);
 if(id==='TRADE-5')add(rank(s,'TRADE-3')||rank(s,'TRADE-4'),'学会成筐交售或多味拼盘');
 if(id==='TRADE-S'){add(rank(s,'TRADE-2'),'学会小店招牌');add(rank(s,'TRADE-3')||rank(s,'TRADE-4'),'学会成筐交售或多味拼盘');}
 if(node==='S')add(!Object.entries(s.progress.skills).some(([k,v])=>k!==id&&k.endsWith('-S')&&v),'全局只选一个专精');
 return rows;
}
export function skillGate(s,id,level=1){
 if(!SKILL_BY_ID[id]||level!==1)return '已经学满或节点无效';
 return skillRequirements(s,id).filter(r=>!r.met).map(r=>r.text).join('；');
}
export function learnSkill(s,id){
 if(rank(s,id))throw Error('已经学会这项手艺');
 const reason=skillGate(s,id);if(reason)throw Error(reason);
 const cost=SKILL_BY_ID[id].cost;if(skillPoints(s).available<cost)throw Error('还差'+(cost-skillPoints(s).available)+'点');
 syncProgress(s);s.progress.skills[id]=1;
 if(['TRADE-3','TRADE-4'].includes(id)&&!s.progress.trade.initialGranted){s.progress.trade.initialGranted=true;s.progress.trade.credits++;}
 return {id,level:1,cost};
}
export function respecReason(s,now=Date.now()){
 if(s.expansion?.business?.active)return '先结清当前营业，再重新分配';
 if(s.batch?.eggs.some(e=>!e.collected))return '先收完厨房这一批，再重新分配';
 if(s.progress.trip?.status==='running'&&s.progress.trip.endAt>now)return '先等同行伙伴归队，再重新分配';
 if(!s.progress.migrationRespec&&s.progress.respecAt!==null&&now-s.progress.respecAt<72*3600000)return '下次可用：'+new Date(s.progress.respecAt+72*3600000).toLocaleString('zh-CN',{month:'numeric',day:'numeric',hour:'2-digit',minute:'2-digit',hour12:false});
 return '';
}
export function respecSkills(s,now=Date.now()){
 const reason=respecReason(s,now);if(reason)throw Error(reason);
 s.progress.skills={};s.progress.trade.category=null;delete s.progress.replicate;s.progress.respecAt=now;s.progress.migrationRespec=false;return true;
}
export function applySkillPlan(s,{steps,reset=false,category=null,base},now=Date.now()){
 if(base!==undefined&&JSON.stringify(s.progress.skills)!==base)throw Error('实际手艺已变化，请重新试配');
 const next=structuredClone(s);if(reset)respecSkills(next,now);
 for(const id of steps)learnSkill(next,id);
 if(rank(next,'TRADE-2')){
  if(!TRADE_CATEGORIES.includes(category))throw Error('请为小店选择一个招牌类别');
  if(rank(s,'TRADE-2')&&!reset&&s.progress.trade.category!==category)throw Error('重新分配手艺时才能更换招牌类别');
  next.progress.trade.category=category;
 }
 s.progress=next.progress;return true;
}
export function effects(s){
 const r=id=>rank(s,id);
 return {reduction:r('CUL-S')?.20:r('CUL-2')?.10:0,freshMinutes:r('HOME-4')?90:r('HOME-1')?30:0,
 cleanHours:r('HOME-S')?72:r('HOME-3')?54:36,reserve:!!r('HOME-2'),sickness:r('HOME-S')&&s.progress?.protection.sickness!==false?.2:.4,
 rebate:r('TRADE-1')?6:0,basket:!!r('TRADE-3'),platter:!!r('TRADE-4'),capacity:r('TRADE-5')?6:3,basketBonus:r('TRADE-S')?18:12,platterBonus:r('TRADE-S')?12:8,markup:r('TRADE-2')?(r('TRADE-S')?18:12):0};
}
export function harvestCredit(s){
 const e=effects(s),t=s.progress.trade;if(!e.basket&&!e.platter)return;
 t.harvestProgress++;
 if(t.harvestProgress>=24){if(t.credits<e.capacity){t.credits++;t.harvestProgress=0;}else t.harvestProgress=23;}
}
export function basketQuote(s,selection,{useRewards=true}={}){
 const e=effects(s),remaining={...selection},basketItems=[],platterItems=[];let credits=useRewards?s.progress.trade.credits-(s.expansion?.business?.active?.creditReserve??0):0,baskets=0,platters=0,baseIncome=0,markupUnits=s.progress.trade.markupRemainder??0;
 for(const [key,n]of Object.entries(selection)){
  const [egg,id]=key.split(':').map(Number),price=GAME_DATA.characters[egg]?.find(c=>c.id===id)?.cp_1??0;
  baseIncome+=price*n;
  if(e.markup&&TRADE_SPECIES[key]?.category===s.progress.trade.category)markupUnits+=price*n*e.markup;
  if(e.basket&&RULES.trade.eligibleSpecies.includes(key)){const count=Math.min(credits,Math.floor(n/24));if(count){credits-=count;baskets+=count;remaining[key]-=24*count;basketItems.push({key,count:count*24});}}
 }
 if(e.platter)while(credits>0){
  const keys=Object.keys(remaining).filter(k=>TRADE_SPECIES[k]?.platter&&remaining[k]>=3).sort((a,b)=>remaining[b]-remaining[a]||a.localeCompare(b)).slice(0,4);
  if(keys.length<4)break;keys.forEach(k=>remaining[k]-=3);platterItems.push(keys);platters++;credits--;
 }
 const markup=Math.floor(markupUnits/100),bonus=baskets*e.basketBonus+platters*e.platterBonus;
 return {baskets,platters,bonus,baseIncome,markup,markupRemainder:markupUnits%100,income:baseIncome+markup+bonus,creditsUsed:baskets+platters,basketItems,platterItems};
}
export const batchSignature=(egg,tool,ingredients)=>JSON.stringify([egg,tool,[...ingredients].sort((a,b)=>a-b)]);
export function cookingTiming(s,originalMinutes,{signature=null,now=Date.now()}={}){
 const e=effects(s),hot=!!(rank(s,'CUL-4')&&signature&&s.progress.hotStove?.signature===signature&&now>=s.progress.hotStove.at&&now-s.progress.hotStove.at<=30*60000);
 const reduction=Math.min(.25,e.reduction+(hot?.05:0)),calm=!!(rank(s,'HOME-5')&&s.progress.protection.calm),freshness=calm||s.progress?.protection.freshness!==false;
 const minutes=Math.ceil(Math.max(6,originalMinutes*(1-reduction))*(calm?1.25:1)*60)/60;
 const normal=Math.max(2*originalMinutes,120)+(freshness?e.freshMinutes:0),freshMinutes=calm?Math.max(normal,480):normal;
 return {minutes,originalMinutes,freshMinutes,reduction,sickness:e.sickness,hot,calm,freshness};
}
export function claimLeftovers(s){
 let room=materialRoom(s),received=[];
 while(room>0&&s.progress.leftovers.length){const id=s.progress.leftovers.shift();s.ingredients[id]=(s.ingredients[id]??0)+1;received.push(id);room--;}
 return received;
}
export function fortunePool(s){
  const f=s.progress.fortune,unknown=RULES.fortune.speciesIDs.filter(id=>!speciesDiscovered(s,0,id));
  let pool=(unknown.length&&f.drought>=6?unknown:RULES.fortune.speciesIDs).map(id=>({id,weight:unknown.includes(id)?f.drought>=6?1:f.drought>=3?4:2:1}));
  if(pool.length>1)pool=pool.filter(p=>p.id!==f.lastTarget);
  return {pool,unknown};
}
export function randomUnit(random){const r=random();if(!Number.isFinite(r)||r<0||r>=1)throw Error('随机数无效，操作未提交');return r;}
export function drawFortune(s,random=economicRandom(s,'drawFortune')){
  const {pool,unknown}=fortunePool(s);let value=randomUnit(random)*pool.reduce((n,p)=>n+p.weight,0),id=pool.at(-1).id;
  for(const p of pool){value-=p.weight;if(value<0){id=p.id;break;}}
  s.progress.fortune={lastTarget:id,drought:unknown.length?Math.min(Number.MAX_SAFE_INTEGER,s.progress.fortune.drought+1):0};return id;
}
export function checkedIncome(s,amount){if(!Number.isSafeInteger(amount)||amount<0||!Number.isSafeInteger(s.cp+amount))throw Error('CP余额超出可保存范围');return s.cp+amount;}
