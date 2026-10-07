// 生意主页 (2026-10-07, loop design batch 3), read-only: what the shop sells today and what its menu is short of, the few
// orders worth looking at (deliverable first), and one line each for 常客 and 项目. Commands stay where they were:
// business.js (开张), order-delivery.js (交付 / 摆出来), regulars.js and projects.js.
// 今日菜单: the menu being prepared (the player's last pick, else the one that sells best now); while the shop is open, the
// menu on sale. Its 3 core partners are those of the menu's authored example the farm is closest to (two for 家常小铺):
// each slot is ✓ once enough can be spared, else have/need. A shop that opens with its menu complete adds 25% to its menu
// partners all day (business.js rules 2); the other spare partners sell beside them at their usual price, and partners
// kept at home are never stocked.
import {REGIONAL,CONTENT_TEXT,resolveSpecies} from './content-registry.js';
import {speciesDiscovered} from './species-state.js';
import {inventoryView,lockedCount,freeCount} from './inventory.js';
import {menuUnlockInfo,businessUnlockInfo,assignMenuRoles,menuSnapshot,menuFit} from './menu-model.js';
import {suggestBusinessStock,menuOverview,menuBonusIncome,estimateIncome,holdsOf} from './business-advisor.js';
import {orderStatus,storyStatus,spareFor} from './order-delivery.js';
import {speciesProducible} from './orders.js';
import {selectorLabel,SEASON_NAME} from './order-model.js';
import {regularsModel,readableRequirement} from './regular-model.js';
import {projectsModel} from './project-model.js';
import {REGION_SHORT} from './clue-regions.js';
import {orderIntelDue} from './order-intel.js';
import {MENU_BONUS_PERCENT} from './business.js';

const known=(s,key)=>{const c=resolveSpecies(key);return !!c&&speciesDiscovered(s,c.egg,c.id);};
const nameOf=key=>resolveSpecies(key)?.title_zh_CN??'';
// What can go on sale: spare (free, above the number kept at home), less what the order board is counting on.
const usable=(s,key,now=Date.now())=>{const v=inventoryView(s,key);return Math.max(0,Math.min(v.free,v.home-lockedCount(s,key))-(holdsOf(s,now)[key]??0));};
const RANK={ordinary:0,suitable:1,complete:2};
export {MENU_BONUS_PERCENT};

export function todayMenuId(s,preferred=null){
  const active=s.expansion?.business?.active;if(active)return active.menuId;
  if(preferred&&REGIONAL.menus.some(m=>m.id===preferred)&&menuUnlockInfo(s,preferred).met)return preferred;
  return menuOverview(s).filter(o=>o.plan).sort((a,b)=>RANK[b.plan.tier]-RANK[a.plan.tier]||b.plan.income-a.plan.income)[0]?.id??'MN1';
}

// The menu card: core slots, whether the stock completes the menu, the forecast. stock: the stock being prepared (the
// auto plan when the player has not changed it).
export function menuCore(s,menuId,stock=null,now=Date.now()){
  const menu=REGIONAL.menus.find(m=>m.id===menuId);if(!menu)return null;
  const unlock=menuUnlockInfo(s,menuId),plan=unlock.met?suggestBusinessStock(s,menuId,{now}):null;
  const used=stock&&Object.keys(stock).length?stock:plan?.stock??{};
  const roles=Object.keys(used).length?assignMenuRoles(menuId,used):[];
  const fit=Object.keys(used).length?menuFit(menuId,used,roles,menuSnapshot(s,menuId,used,roles)):{tier:'ordinary',complete:false};
  const need=menuId==='MN1'?6:3,roleKeys=roles.filter(r=>r.roleId!=='ordinary').flatMap(r=>r.keys);
  let slots;
  if(fit.complete)slots=roleKeys.slice(0,3).map(key=>({key,need,have:Math.max(need,used[key]??0),ok:true,known:true}));
  else{
    let best=null;
    for(const ex of menu.examples??[]){const rows=ex.map(({key,quantity})=>({key,need:quantity,have:Math.min(quantity,usable(s,key,now))}));const lack=rows.reduce((n,r)=>n+r.need-r.have,0);if(!best||lack<best.lack)best={rows,lack};}
    slots=(best?.rows??[]).map(r=>({...r,ok:r.have>=r.need,known:known(s,r.key)}));
  }
  slots=slots.map(x=>({...x,name:x.known?nameOf(x.key):'',makeable:!x.ok&&x.known&&speciesProducible(s,x.key,now)}));
  const missing=fit.complete?0:slots.reduce((n,x)=>n+Math.max(0,x.need-x.have),0);
  const count=Object.values(used).reduce((a,b)=>a+b,0),extra=Object.entries(used).filter(([k])=>!roleKeys.includes(k)).reduce((n,[,q])=>n+q,0);
  return {id:menuId,name:CONTENT_TEXT[menuId]?.name??menuId,unlocked:unlock.met,unlock,tier:fit.tier,complete:fit.complete,slots,missing,
    makeable:slots.some(x=>x.makeable),stock:used,count,extra,income:count?estimateIncome(s,menuId,used,roles,fit):0,bonus:fit.complete?menuBonusIncome(s,used,roles):0};
}

// 下一锅 demands for today's menu: each core partner the kitchen still has to make (known, short at home). Only while the
// shop is not open and the menu is not complete yet.
export function menuDemands(s,menuId,now=Date.now()){
  if(!menuId||s.expansion?.business?.active||!businessUnlockInfo(s).met)return [];
  const core=menuCore(s,menuId,null,now);if(!core||!core.unlocked||core.complete)return [];
  return core.slots.filter(x=>x.known&&!x.ok).map(x=>({kind:'menu',id:menuId,key:x.key,name:core.name,label:`今日菜单「${core.name}」`,what:x.name,quantity:x.need,allowed:new Set([x.key]),short:x.need-x.have}));
}

// ---- 订单板 ----
function groupLabel(def,g){
  const source=def.groups.find(x=>x.id===(g.sourceGroupId??g.id)),chapter=g.id.includes(':')?g.id.split(':')[1]:null;
  return chapter?`${selectorLabel(source.selector)} · ${SEASON_NAME[chapter]??chapter}`:selectorLabel(source.selector);
}
// A face for a need line: the partner the delivery would use, else a known partner that fits, else none (a plain icon).
function faceFor(s,allowed,alloc){
  const own=alloc?.sort((a,b)=>b.quantity-a.quantity)[0]?.key;if(own)return own;
  return allowed.filter(k=>known(s,k)).sort((a,b)=>spareFor(s,b)-spareFor(s,a)||freeCount(s,b)-freeCount(s,a))[0]??null;
}
function orderCard(s,ref,now){
  const st=orderStatus(s,ref,now);if(!st)return null;
  const def=REGIONAL.orders.find(x=>x.id===st.templateId);
  let needs,quantity,have,makeable=false;
  if(st.kind==='display'){
    needs=[{label:'不同模样',have:st.have,quantity:st.need,kinds:true,keys:st.keys??[]}];quantity=st.need;have=st.have;
    makeable=!st.ready&&!!st.allowed?.some(k=>freeCount(s,k)<1&&speciesProducible(s,k,now));
  }else{
    needs=st.groups.map(g=>({label:groupLabel(def,g),have:g.quantity-g.remaining+g.have,quantity:g.quantity,key:faceFor(s,g.allowed,st.allocation?.filter(a=>a.groupId===g.id)),short:g.short}));
    quantity=needs.reduce((n,x)=>n+x.quantity,0);have=needs.reduce((n,x)=>n+x.have,0);
    makeable=!st.ready&&!st.blocked&&st.groups.some(g=>g.short>0&&g.allowed.some(k=>speciesProducible(s,k,now)));
  }
  const state=st.ready?'ready':st.blocked?'blocked':'short',gap=st.short?`还差 ${st.short} 只`:st.kindsShort?`还差 ${st.kindsShort} 种`:'';
  return {ref,type:st.kind==='display'?'display':'deliver',templateId:st.templateId,name:CONTENT_TEXT[st.templateId]?.name??st.templateId,
    region:st.region,regionName:REGION_SHORT[st.region]??'',needs,cp:st.bonusCP,intel:!!st.region&&orderIntelDue(s,st.templateId),state,gap,short:st.short,kindsShort:st.kindsShort,makeable,
    progress:quantity?have/quantity:0,accepted:st.accepted};
}
function storyCard(s,st,now){
  const c=st.choice,quantity=c?c.count:0,have=c?st.delivered+st.have:0;
  return {ref:{kind:'story',id:st.id},type:'story',name:st.chapter.title.split('：').at(-1),chapter:st.index+1,region:null,regionName:'',
    needs:c?[{label:c.name,have,quantity,key:c.species}]:[],cp:st.extraCP,intel:false,state:st.ready?'ready':!st.unlocked?'blocked':'short',
    gap:st.unlocked?`还差 ${st.short} 只`:st.requirements.find(r=>!r.met)?.text??'',short:st.short,kindsShort:0,makeable:!!c&&st.unlocked&&!st.ready&&speciesProducible(s,c.species,now),
    progress:quantity?have/quantity:0,accepted:true};
}
// Deliverable first, then the ones furthest along; orders that cannot be worked on yet last. limit: cards on the home page.
export function orderBoard(s,now=Date.now(),{limit=3}={}){
  const o=s.expansion?.orders,cards=[];
  for(const a of o?.active??[])cards.push(orderCard(s,{kind:'order',id:a.id},now));
  for(const p of o?.proposals??[])cards.push(orderCard(s,{kind:'proposal',id:p.id},now));
  const story=storyStatus(s);if(story?.unlocked)cards.push(storyCard(s,story,now));
  const rank=c=>c.state==='ready'?0:c.state==='short'?(c.makeable?1:2):3;
  const list=cards.filter(Boolean).sort((a,b)=>rank(a)-rank(b)||b.progress-a.progress);
  return {cards:list.slice(0,limit),all:list,total:list.length,ready:list.filter(c=>c.state==='ready').length};
}
// 下一锅 demands for 「只看不交」 orders still short of kinds: any partner of the order's list not at home yet adds one.
// Never when enough kinds are already home (then 生意 shows 「摆出来」, not another batch).
export function displayDemands(s,now=Date.now()){
  const o=s.expansion?.orders,out=[];if(!o)return out;
  const refs=[...o.active.filter(a=>a.kind==='display').map(a=>({kind:'order',id:a.id,templateId:a.templateId})),
    ...o.proposals.filter(p=>REGIONAL.orders.find(x=>x.id===p.templateId)?.kind==='display').map(p=>({kind:'proposal',id:p.id,templateId:p.templateId}))];
  for(const ref of refs){
    const st=orderStatus(s,ref,now);if(!st||st.ready||st.blocked||!st.kindsShort)continue;
    const allowed=st.allowed.filter(k=>freeCount(s,k)<1);if(!allowed.length)continue;
    const name=CONTENT_TEXT[ref.templateId]?.name??ref.templateId;
    out.push({kind:ref.kind,id:ref.id,display:true,name,label:`${ref.kind==='proposal'?'新订单':'订单'}「${name}」`,what:'',quantity:st.need,allowed:new Set(allowed),short:st.kindsShort});
  }
  return out;
}
// 下一锅 demand for the 厨房往事 chapter now open (orders and proposals are seasoning-advisor's openDemands).
export function storyDemands(s){
  const st=storyStatus(s);if(!st?.unlocked||!st.choice||st.short<=0)return [];
  return [{kind:'story',id:st.id,name:st.chapter.title.split('：').at(-1),label:`厨房往事「${st.chapter.title.split('：').at(-1)}」`,what:st.choice.name,quantity:st.choice.count,allowed:new Set([st.choice.species]),short:st.short}];
}

// ---- 常客 / 项目 small cards ----
const REGULAR_SHORT={RG1:'老顾客',RG2:'采购人',RG3:'茶坡访客',RG4:'货客'};
const PROJECT_SHORT={'PJ-1':'招牌册','PJ-2':'风味篮','PJ-3':'茶坡一桌','PJ-4':'风味展'};
export function regularsSummary(s){
  if(!s.expansion?.regulars)return null;
  const m=regularsModel(s),done=m.rows.reduce((n,r)=>n+r.readCount+(r.pending?1:0),0),total=REGIONAL.regulars.reduce((n,r)=>n+r.stages.length,0);
  const pending=m.rows.find(r=>r.pending),next=m.rows.find(r=>r.opened&&!r.complete&&r.current?.gateMet);
  const line=pending?`${REGULAR_SHORT[pending.id]??pending.name}有新故事`:next?`${REGULAR_SHORT[next.id]??next.name}：${next.current.branches[0]?.text??''}`:m.rows.some(r=>r.opened)?'常客还在路上':'营业或交订单后会有人来';
  return {done,total,line,dot:!!pending,focus:pending?.id??next?.id??null};
}
export function projectsSummary(s){
  if(!s.expansion?.projects)return null;
  const m=projectsModel(s),done=m.rows.reduce((n,r)=>n+r.progress,0),total=REGIONAL.projects.reduce((n,p)=>n+p.stages.length,0);
  const ready=m.rows.find(r=>r.gateMet&&r.current?.ready),open=m.rows.find(r=>r.gateMet&&r.current);
  const line=ready?`${PROJECT_SHORT[ready.id]}可以登记${ready.current.costCP?` · ${ready.current.costCP} CP`:''}`:open?`${PROJECT_SHORT[open.id]}：${readableRequirement(s,CONTENT_TEXT[open.current.id]?.check??'')}`:'厨房再大一些就能筹备';
  return {done,total,line,dot:!!ready&&s.cp>=(ready.current.costCP??0),focus:ready?.id??open?.id??null};
}
