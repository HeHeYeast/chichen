// Situational purchases (O01–O12): proposals, frozen instances, per-order
// reservations (Q), grouped batch deliveries and the O04 display. The legacy
// three chapters stay in progress.orders and story-orders.js, untouched.
import {REGIONAL,resolveSpecies} from './content-registry.js';
import {speciesDiscovered} from './species-state.js';
import {recipePaths,recipePathInfo} from './recipe-book.js';
import {ingredientUnlockInfo} from './ingredient-unlocks.js';
import {regionalRecipeInfo} from './regional-methods.js';
import {RELEASED_REGIONS} from './region-model.js';
import {collectedTotal,discoveryCount,checkedIncome} from './progression.js';
import {freeCount,homeCount,usableByOwner,lockedCount} from './inventory.js';
import {reduceFacts} from './facts.js';

import {newOperationsEnabled,assertNewOperation} from './rollback-policy.js';
export const ORDER_RULES_VERSION=1;
export const MAX_PROPOSALS=2,MAX_ACTIVE_ORDERS=2;
// Each Work opens its templates; saved instances of any template stay valid.
export const RELEASED_ORDER_TEMPLATES=Object.freeze(['O01','O04','O02','O03','O05','O06','O07','O08','O09','O10','O12','O11']);
export const ORDER_UNLOCK={collected:240,discoveries:8};
export function freshOrders(){return {sequence:0,proposalSequence:0,proposals:[],active:[],templateProgress:{},refillCredits:0,lastProposedTemplate:null};}

const orders=s=>{const o=s.expansion.orders;if(!o)throw Error('采购尚未完成存档迁移');return o;};
const template=id=>REGIONAL.orders.find(o=>o.id===id)??null;
const int=(v,min,max,message)=>{if(!Number.isSafeInteger(v)||v<min||v>max)throw Error(message);};
const sum=map=>Object.values(map).reduce((a,b)=>a+b,0);
const progress=(s,id)=>orders(s).templateProgress[id]??={accepted:0,completed:0,cancelled:0,skipped:0};

export function ordersUnlockInfo(s){
  const missing=[];
  if(collectedTotal(s)<ORDER_UNLOCK.collected)missing.push(`累计收取 ${collectedTotal(s)}/${ORDER_UNLOCK.collected} 只`);
  if(discoveryCount(s)<ORDER_UNLOCK.discoveries)missing.push(`发现 ${discoveryCount(s)}/${ORDER_UNLOCK.discoveries} 种`);
  return {met:!missing.length,missing};
}

// "Known executable": actually collected, and some owned path (egg, cookware,
// kitchen, dated/sign conditions, every ingredient's supply) can make it now.
export function speciesProducible(s,key,now){
  const c=resolveSpecies(key);if(!c||!speciesDiscovered(s,c.egg,c.id))return false;
  if(c.egg===1&&!s.duck)return false;
  if(c.pack==='regional')return regionalRecipeInfo(s,c.recipeId).met;
  return recipePaths(key).some(r=>r.kind!=='change'&&r.toolId>=0&&recipePathInfo(s,r,now).conditions.every(x=>x.met)&&r.ingredients.every(id=>ingredientUnlockInfo(s,id).available));
}

function variantsFor(s,def){
  if(def.kind==='display')return def.variants;
  if(def.id==='O06')return def.variants;
  // Missing duck eggs use the chicken-kitchen variant; otherwise the standing one.
  return def.variants.filter(v=>s.duck?v.egg===undefined:v.egg===0);
}
function groupAllowed(def,variant,group,{region=null,chapter=null}={}){
  let keys=[...(variant?.allowed&&def.kind==='display'?variant.allowed:group.allowed)];
  if(variant?.egg===0)keys=keys.filter(k=>k.startsWith('0:'));
  if(region)keys=keys.filter(k=>resolveSpecies(k).region===region);
  if(chapter)keys=keys.filter(k=>resolveSpecies(k).season===chapter);
  return keys;
}
// Small exact check (≤2 groups): each group gets a distinct species and the
// union reaches the template's distinct minimum. No general solver needed.
function distinctSolution(groupKeys,minimumDistinct){
  if(groupKeys.some(keys=>!keys.length))return false;
  const union=new Set(groupKeys.flat());if(union.size<minimumDistinct)return false;
  if(groupKeys.length===1)return true;
  return groupKeys[0].some(a=>groupKeys[1].some(b=>a!==b));
}
function expandGroups(def,variant,{region=null,chapters=null}={}){
  if(def.id==='O10'&&chapters)return chapters.map((chapter,i)=>({id:`${def.groups[0].id}:${chapter}`,sourceGroupId:def.groups[0].id,quantity:def.groups[0].quantity/chapters.length,allowed:groupAllowed(def,variant,def.groups[0],{chapter})}));
  return def.groups.map(group=>({id:group.id,sourceGroupId:group.id,quantity:group.quantity,allowed:groupAllowed(def,variant,group,{region:def.id==='O06'&&group.selector==='regionFood'?region:null})}));
}

// Options a player can pick when accepting; empty means the template cannot be offered.
export function orderOptions(s,templateId,now){
  const def=template(templateId);if(!def)return [];
  const options=[];
  for(const variant of variantsFor(s,def)){
    if(def.kind==='display'){const known=groupAllowed(def,variant,def.groups[0]).filter(k=>{const c=resolveSpecies(k);return speciesDiscovered(s,c.egg,c.id);});if(known.length>=(def.displayDistinct??def.minimumDistinct))options.push({variantId:variant.id,region:null,chapters:null});continue;}
    // O06 freezes one released region the kitchen can already make; later regions never rewrite it.
    const regions=def.id==='O06'?(variant.regions??[]).filter(r=>RELEASED_REGIONS.includes(r)):[null];
    for(const region of regions){
      if(def.id==='O10'){
        const chapters=[...new Set(def.groups[0].allowed.map(k=>resolveSpecies(k).season))].filter(ch=>groupAllowed(def,variant,def.groups[0],{chapter:ch}).some(k=>speciesProducible(s,k,now)));
        for(let i=0;i<chapters.length;i++)for(let j=i+1;j<chapters.length;j++)options.push({variantId:variant.id,region:null,chapters:[chapters[i],chapters[j]]});
        continue;
      }
      const groups=expandGroups(def,variant,{region}).map(g=>g.allowed.filter(k=>speciesProducible(s,k,now)));
      if(distinctSolution(groups,def.minimumDistinct))options.push({variantId:variant.id,region,chapters:null});
    }
  }
  return options;
}
export const templateQualified=(s,templateId,now)=>orderOptions(s,templateId,now).length>0;
// The groups one accepting option would freeze (read-only; order-delivery.js checks them before delivering in one go).
export function optionGroups(templateId,option){
  const def=template(templateId),variant=def?.variants.find(v=>v.id===option.variantId);if(!def||!variant)return [];
  return expandGroups(def,variant,{region:option.region??null,chapters:option.chapters??null});
}
// What a proposal would ask for if it were accepted now with its first option (read-only: 下一锅 can cook for an order that
// is still only a proposal). Null when it cannot be accepted today.
export function proposalGroups(s,proposalId,now){
  const p=orders(s).proposals.find(x=>x.id===proposalId);if(!p)return null;
  const def=template(p.templateId),option=orderOptions(s,p.templateId,now)[0];if(!def||!option)return null;
  const variant=def.variants.find(v=>v.id===option.variantId);
  return expandGroups(def,variant,{region:option.region,chapters:option.chapters}).map(g=>({...g,selector:def.groups.find(x=>x.id===g.sourceGroupId)?.selector??null}));
}

function eligibleTemplates(s,now){
  const o=orders(s),busy=new Set([...o.proposals.map(p=>p.templateId),...o.active.map(a=>a.templateId)]);
  return RELEASED_ORDER_TEMPLATES.filter(id=>{const def=template(id);return !busy.has(id)&&(def.repeatable||!(o.templateProgress[id]?.completed>0))&&templateQualified(s,id,now);});
}

// Milestones (complete trip, business visitor step, finished batch) earn one
// refill; opening pages, reloading or skipping never draws a new proposal.
export function orderMilestone(s,now,reason){
  if(!newOperationsEnabled('orders'))return [];
  if(!s.expansion?.orders||!ordersUnlockInfo(s).met)return [];
  const o=orders(s);if(o.proposals.length>=MAX_PROPOSALS)return [];
  o.refillCredits=Math.min(MAX_PROPOSALS,o.refillCredits+1);
  const added=[];
  while(o.refillCredits>0&&o.proposals.length<MAX_PROPOSALS){
    const ids=eligibleTemplates(s,now);if(!ids.length)break;
    // Stable rotation: resume after the last template ever proposed.
    const last=o.proposals.at(-1)?.templateId??o.lastProposedTemplate??null,start=last?(RELEASED_ORDER_TEMPLATES.indexOf(last)+1):0;
    const ordered=[...RELEASED_ORDER_TEMPLATES.slice(start),...RELEASED_ORDER_TEMPLATES.slice(0,start)].filter(id=>ids.includes(id));
    const id=ordered[0];int(o.proposalSequence+1,1,Number.MAX_SAFE_INTEGER,'采购提案序号超出范围');
    o.proposalSequence++;o.lastProposedTemplate=id;o.refillCredits--;
    const proposal={id:`proposal-${o.proposalSequence}`,templateId:id,reason};o.proposals.push(proposal);added.push(proposal);
  }
  return added;
}

export function skipProposal(s,proposalId){
  const o=orders(s),index=o.proposals.findIndex(p=>p.id===proposalId);if(index<0)throw Error('这份采购意向已经变化');
  const [p]=o.proposals.splice(index,1);progress(s,p.templateId).skipped++;return p;
}

// deliverNow: accepted only to be delivered in full by the same command (order-delivery.js), so it never takes an
// active slot for longer than that transaction and the slot limit does not apply.
export function acceptProposal(s,proposalId,{variantId,region=null,chapters=null}={},now,{deliverNow=false}={}){
  assertNewOperation('orders');
  const o=orders(s),index=o.proposals.findIndex(p=>p.id===proposalId);if(index<0)throw Error('这份采购意向已经变化');
  if(!deliverNow&&o.active.length>=MAX_ACTIVE_ORDERS)throw Error(`最多同时进行${MAX_ACTIVE_ORDERS}单采购，先完成或取消一单`);
  const p=o.proposals[index],def=template(p.templateId),options=orderOptions(s,def.id,now);
  const option=options.find(x=>x.variantId===(variantId??options[0]?.variantId)&&(x.region??null)===(region??x.region??null)&&JSON.stringify(x.chapters)===JSON.stringify(chapters??x.chapters));
  if(!option)throw Error('这份采购的条件已经变化，暂时不能接取');
  const variant=def.variants.find(v=>v.id===option.variantId);
  int(o.sequence+1,1,Number.MAX_SAFE_INTEGER,'采购序号超出范围');o.sequence++;
  const instance={id:`order-${o.sequence}`,templateId:def.id,variantId:variant.id,kind:def.kind,rulesVersion:ORDER_RULES_VERSION,acceptedAt:now,region:option.region,chapters:option.chapters,
    minimumDistinct:def.kind==='display'?(def.displayDistinct??def.minimumDistinct):def.minimumDistinct,bonusCP:def.bonusCP,
    groups:expandGroups(def,variant,{region:option.region,chapters:option.chapters}).map(g=>({...g,delivered:{}})),reserved:{},paidCP:0,deliveries:0,needsRestock:false};
  o.proposals.splice(index,1);o.active.push(instance);progress(s,def.id).accepted++;
  return instance;
}

const instanceOf=(s,id)=>{const o=orders(s).active.find(x=>x.id===id);if(!o)throw Error('这笔采购已经结束或不存在');return o;};
const remainingOf=g=>g.quantity-sum(g.delivered);
const deliveredKeys=instance=>new Set(instance.groups.flatMap(g=>Object.keys(g.delivered).filter(k=>g.delivered[k]>0)));

// Q is a revocable hold: it never reduces T and is not a delivery.
export function reserveForOrder(s,orderId,key,quantity){
  const o=instanceOf(s,orderId);if(o.kind!=='purchase')throw Error('展示委托只看在家伙伴，不需要预留');
  int(quantity,1,99999,'预留数量无效');
  const need=o.groups.filter(g=>g.allowed.includes(key)).reduce((n,g)=>n+remainingOf(g),0);
  if(!need)throw Error('这种伙伴不在本单需求内');
  if((o.reserved[key]??0)+quantity>need)throw Error('预留不能超过本单还需要的数量');
  if(freeCount(s,key)<quantity)throw Error('可用伙伴不足，可能已用于营业、寻访或其他订单');
  o.reserved[key]=(o.reserved[key]??0)+quantity;o.needsRestock=false;return {...o.reserved};
}
export function releaseReservation(s,orderId,key,quantity=null){
  const o=instanceOf(s,orderId),held=o.reserved[key]??0,n=quantity??held;int(n,1,held,'释放数量无效');
  o.reserved[key]=held-n;if(!o.reserved[key])delete o.reserved[key];return {...o.reserved};
}

// One explicit allocation per bird: group, species, quantity. Own Q is consumed
// first, then free stock. Base price per delivery; the bonus once at completion.
export function deliverOrderGroups(s,orderId,allocations,now,{overrideKeepOne=false}={}){
  const o=instanceOf(s,orderId);if(o.kind!=='purchase')throw Error('展示委托不交付伙伴');
  if(!Array.isArray(allocations)||!allocations.length)throw Error('请选择本次交付的伙伴');
  const perKey={},perGroup={};
  for(const a of allocations){
    const g=o.groups.find(x=>x.id===a?.groupId);if(!g)throw Error('需求组无效');
    if(!g.allowed.includes(a.key))throw Error('这只伙伴不在该需求组的允许名单中');
    int(a.quantity,1,99999,'交付数量无效');perKey[a.key]=(perKey[a.key]??0)+a.quantity;perGroup[g.id]=(perGroup[g.id]??0)+a.quantity;
  }
  for(const g of o.groups)if((perGroup[g.id]??0)>remainingOf(g))throw Error('交付超过该需求组还需要的数量');
  for(const [key,n]of Object.entries(perKey))if(usableByOwner(s,key,o.id)<n)throw Error('可用数量已变化，请重新选择；营业、寻访或其他订单占用的伙伴不能交付');
  const policy=s.expansion.inventoryPolicy;
  if(!overrideKeepOne)for(const [key,n]of Object.entries(perKey))if(homeCount(s,key)-n<lockedCount(s,key))throw Error(`已锁定在家${lockedCount(s,key)}只（默认每种留1只）；到仓库调整锁定数量后再用`);
  const remainingAfter=o.groups.reduce((n,g)=>n+remainingOf(g)-(perGroup[g.id]??0),0),distinct=new Set([...deliveredKeys(o),...Object.keys(perKey)]);
  if(distinct.size+remainingAfter<o.minimumDistinct)throw Error(`本单至少需要${o.minimumDistinct}种不同出品，请换一种交付`);
  let base=0;
  for(const [key,n]of Object.entries(perKey)){base+=resolveSpecies(key).cp_1*n;}
  const complete=remainingAfter===0,income=base+(complete?o.bonusCP:0);
  s.cp=checkedIncome(s,income);
  const events=[];
  for(const a of allocations){
    const g=o.groups.find(x=>x.id===a.groupId);g.delivered[a.key]=(g.delivered[a.key]??0)+a.quantity;
    events.push({kind:'orderDelivery',instanceId:o.id,templateId:o.templateId,variantId:o.variantId,region:o.region,groupId:g.sourceGroupId,key:a.key,quantity:a.quantity});
  }
  for(const [key,n]of Object.entries(perKey)){const own=Math.min(o.reserved[key]??0,n);if(own){o.reserved[key]-=own;if(!o.reserved[key])delete o.reserved[key];}s.farm[key]-=n;}
  // Delivering other species can shrink what a hold is still for; release the excess.
  for(const key of Object.keys(o.reserved)){const need=o.groups.filter(g=>g.allowed.includes(key)).reduce((sum,g)=>sum+remainingOf(g),0);if(o.reserved[key]>need)o.reserved[key]=need;if(!o.reserved[key])delete o.reserved[key];}
  o.paidCP+=base;o.deliveries++;
  for(const g of o.groups)if(perGroup[g.id])events.push({kind:'orderGroupWitness',instanceId:o.id,templateId:o.templateId,groupId:g.sourceGroupId,byKey:{...g.delivered}});
  const first=!(orders(s).templateProgress[o.templateId]?.completed>0);
  if(complete){
    events.push({kind:'orderComplete',instanceId:o.id,templateId:o.templateId,variantId:o.variantId,region:o.region,chapters:o.chapters,groupDeliveries:o.groups.map(g=>({groupId:g.sourceGroupId,delivered:{...g.delivered}}))});
    orders(s).active=orders(s).active.filter(x=>x.id!==o.id);progress(s,o.templateId).completed++;
  }
  reduceFacts(s,events);
  return {orderId:o.id,templateId:o.templateId,baseCP:base,bonusCP:complete?o.bonusCP:0,income,complete,firstResult:complete&&first?template(o.templateId).firstResult.id:null};
}

// O04: each chosen species must have one free bird at home right now; nothing
// is consumed, reserved or paid. Both variants share one first-result note.
export function displayOrder(s,orderId,keys){
  const o=instanceOf(s,orderId);if(o.kind!=='display')throw Error('这笔采购需要交付，不是展示');
  if(!Array.isArray(keys)||new Set(keys).size!==keys.length||keys.length!==o.minimumDistinct)throw Error(`请选择${o.minimumDistinct}种不同伙伴`);
  for(const key of keys){if(!o.groups[0].allowed.includes(key))throw Error('这只伙伴不在本次展示的允许名单中');if(freeCount(s,key)<1)throw Error('展示需要在家可用1只；营业、寻访或采购占用的伙伴不能同时展示');}
  const first=!(orders(s).templateProgress[o.templateId]?.completed>0);
  orders(s).active=orders(s).active.filter(x=>x.id!==o.id);progress(s,o.templateId).completed++;
  reduceFacts(s,[{kind:'orderDisplay',instanceId:o.id,templateId:o.templateId,keys:[...keys]},{kind:'orderComplete',instanceId:o.id,templateId:o.templateId,variantId:o.variantId,region:null,chapters:null,groupDeliveries:[]}]);
  return {orderId:o.id,templateId:o.templateId,keys:[...keys],income:0,firstResult:first?template(o.templateId).firstResult.id:null};
}

// Cancelling keeps delivered birds and paid base CP; only unused Q is released.
export function cancelOrderInstance(s,orderId){
  const o=instanceOf(s,orderId);orders(s).active=orders(s).active.filter(x=>x.id!==o.id);progress(s,o.templateId).cancelled++;
  return {orderId:o.id,released:{...o.reserved},paidCP:o.paidCP,delivered:o.groups.reduce((n,g)=>n+sum(g.delivered),0)};
}

// Strict schema check (no repair): identities, frozen allowed subsets, delivery
// sums, paid base CP and reservations. Q conservation is checked by the caller.
export function validateOrdersState(s,error){
  const obj=(v,label,keys)=>{if(!v||typeof v!=='object'||Array.isArray(v))error(label);if(keys){for(const k of keys)if(!Object.hasOwn(v,k))error(`${label}.${k}`);for(const k of Object.keys(v))if(!keys.includes(k))error(`${label}.${k}`);}return v;};
  const n=(v,label,min=0,max=Number.MAX_SAFE_INTEGER)=>{if(!Number.isSafeInteger(v)||v<min||v>max)error(label);};
  const o=obj(s.expansion.orders,'采购',['sequence','proposalSequence','proposals','active','templateProgress','refillCredits','lastProposedTemplate']);
  n(o.sequence,'采购序号');n(o.proposalSequence,'提案序号');n(o.refillCredits,'提案补位',0,MAX_PROPOSALS);
  if(o.lastProposedTemplate!==null&&!template(o.lastProposedTemplate))error('最近提案模板');
  if(!Array.isArray(o.proposals)||o.proposals.length>MAX_PROPOSALS)error('采购提案数量');
  const proposalIds=new Set();
  for(const p of o.proposals){obj(p,'采购提案',['id','templateId','reason']);const m=/^proposal-([1-9]\d*)$/.exec(p.id);if(!m||Number(m[1])>o.proposalSequence||proposalIds.has(p.id)||!template(p.templateId)||!['visitor','trip','batch'].includes(p.reason))error('采购提案身份');proposalIds.add(p.id);}
  for(const [id,v]of Object.entries(obj(o.templateProgress,'模板进度'))){if(!template(id))error('模板进度身份');obj(v,'模板进度',['accepted','completed','cancelled','skipped']);for(const k of ['accepted','completed','cancelled','skipped'])n(v[k],'模板进度计数');if(v.completed+v.cancelled>v.accepted)error('模板进度守恒');}
  if(!Array.isArray(o.active)||o.active.length>MAX_ACTIVE_ORDERS)error('进行中采购数量');
  const ids=new Set();
  for(const a of o.active){
    obj(a,'进行中采购',['id','templateId','variantId','kind','rulesVersion','acceptedAt','region','chapters','minimumDistinct','bonusCP','groups','reserved','paidCP','deliveries','needsRestock']);
    const m=/^order-([1-9]\d*)$/.exec(a.id);if(!m||Number(m[1])>o.sequence||ids.has(a.id))error('采购身份');ids.add(a.id);
    const def=template(a.templateId),variant=def?.variants.find(v=>v.id===a.variantId);
    if(!def||!variant||a.kind!==def.kind||a.rulesVersion!==ORDER_RULES_VERSION||a.bonusCP!==def.bonusCP||a.minimumDistinct!==(def.kind==='display'?(def.displayDistinct??def.minimumDistinct):def.minimumDistinct))error('采购冻结条款');
    n(a.acceptedAt,'接取时间');n(a.paidCP,'已付货款');n(a.deliveries,'交付次数');if(typeof a.needsRestock!=='boolean')error('补货标记');
    if(a.region!==null&&!(def.id==='O06'&&variant.regions?.includes(a.region)))error('冻结地区');
    if(def.id==='O06'&&a.region===null)error('地区采购未冻结地区');
    const seasons=new Set(def.groups[0].allowed.map(k=>resolveSpecies(k).season).filter(Boolean));
    if(a.chapters!==null&&!(def.id==='O10'&&Array.isArray(a.chapters)&&a.chapters.length===2&&a.chapters[0]!==a.chapters[1]&&a.chapters.every(c=>seasons.has(c))))error('冻结章节');
    if(def.id==='O10'&&a.chapters===null)error('四时章节未冻结');
    const expected=expandGroups(def,variant,{region:a.region,chapters:a.chapters});
    if(!Array.isArray(a.groups)||a.groups.length!==expected.length)error('需求组');
    let paid=0,remaining=0;
    for(const [i,g]of a.groups.entries()){
      obj(g,'需求组',['id','sourceGroupId','quantity','allowed','delivered']);const e=expected[i];
      if(g.id!==e.id||g.sourceGroupId!==e.sourceGroupId||g.quantity!==e.quantity||JSON.stringify(g.allowed)!==JSON.stringify(e.allowed))error('需求组冻结');
      let got=0;for(const [key,q]of Object.entries(obj(g.delivered,'已交付'))){if(!g.allowed.includes(key))error('已交付身份');n(q,'已交付数量',1);got+=q;paid+=resolveSpecies(key).cp_1*q;}
      if(got>g.quantity)error('交付超量');remaining+=g.quantity-got;
    }
    if(def.kind==='display'&&(a.deliveries||a.paidCP||Object.keys(a.reserved).length))error('展示委托不交付');
    if(paid!==a.paidCP)error('采购货款守恒');if(remaining===0)error('已完成采购仍在进行');
    for(const [key,q]of Object.entries(obj(a.reserved,'采购预留'))){n(q,'采购预留数量',1);const need=a.groups.filter(g=>g.allowed.includes(key)).reduce((sum,g)=>sum+g.quantity-Object.values(g.delivered).reduce((x,y)=>x+y,0),0);if(q>need)error('采购预留超过需求');}
  }
  return true;
}
