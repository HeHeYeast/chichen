// 订单一步交付 (2026-10-07): an order is finished by one 「交付」 once everything it asks for is at home — a new 询问 is
// accepted in the same step — and an 「只看不交」 order by one 「摆出来」. The steps the old flow showed (接取, 预留,
// 一键配齐, 逐只交付, 再确认) are gone from the screen; their commands in orders.js stay, so an order an older save has
// already accepted, partly delivered or reserved for keeps working: its reservations are used first.
// Every bird delivered is one the player may spare: free (not out on a trip, not on sale, not held by another order) and
// above the number the warehouse keeps at home (锁定在家). The base price of each bird and the order's bonus are paid
// exactly as before (orders.js deliverOrderGroups); the finished order then brings its 地区情报 (order-intel.js).
// 厨房往事 chapters (story-orders.js) get the same one step: the chapter is accepted with the dish the player can fill.
import {REGIONAL,CONTENT_TEXT,resolveSpecies} from './content-registry.js';
import {speciesDiscovered} from './species-state.js';
import {freeCount,homeCount,lockedCount,usableByOwner} from './inventory.js';
import {orderOptions,optionGroups,acceptProposal,deliverOrderGroups,displayOrder} from './orders.js';
import {storyOrders,acceptOrder,deliverOrder} from './story-orders.js';
import {grantOrderIntel,orderRegion,orderIntelDue} from './order-intel.js';
import {assertNewOperation} from './rollback-policy.js';

const sum=map=>Object.values(map).reduce((a,b)=>a+b,0);
const template=id=>REGIONAL.orders.find(o=>o.id===id)??null;
// How many of one partner an order may take: free (or held by this very order) and above the number kept at home.
export const spareFor=(s,key,ownerId=null)=>Math.max(0,Math.min(ownerId?usableByOwner(s,key,ownerId):freeCount(s,key),homeCount(s,key)-lockedCount(s,key)));

// Fills the groups from the spare birds: the group with the fewest choices first, inside it the partners no other group
// wants first, then the most plentiful. Then makes sure the delivery has the different kinds the order asks for.
function fill(s,{groups,minimumDistinct,delivered=new Set(),ownerId=null}){
  const left={};for(const g of groups)for(const k of g.allowed)left[k]??=spareFor(s,k,ownerId);
  const wanted=k=>groups.filter(g=>g.remaining>0&&g.allowed.includes(k)).length,alloc=[],rows=new Map();
  const choices=g=>g.allowed.filter(k=>left[k]>0).length;
  for(const g of [...groups].sort((a,b)=>choices(a)-choices(b))){
    let need=g.remaining;
    for(const k of g.allowed.filter(k=>left[k]>0).sort((a,b)=>wanted(a)-wanted(b)||left[b]-left[a]||(a<b?-1:1))){
      if(need<=0)break;const n=Math.min(need,left[k]);left[k]-=n;need-=n;alloc.push({groupId:g.id,key:k,quantity:n});
    }
    rows.set(g.id,{id:g.id,sourceGroupId:g.sourceGroupId??g.id,quantity:g.quantity,remaining:g.remaining,have:g.remaining-need,short:need,allowed:g.allowed});
  }
  const kinds=()=>new Set([...delivered,...alloc.filter(a=>a.quantity>0).map(a=>a.key)]).size;
  const fresh=g=>g.allowed.find(k=>left[k]>0&&!delivered.has(k)&&!alloc.some(a=>a.key===k&&a.quantity>0));
  while(kinds()<minimumDistinct){
    const from=alloc.filter(a=>a.quantity>1).sort((a,b)=>b.quantity-a.quantity).find(a=>fresh(groups.find(g=>g.id===a.groupId)));
    if(!from)break;const g=groups.find(x=>x.id===from.groupId),k=fresh(g);
    from.quantity--;left[from.key]++;left[k]--;alloc.push({groupId:g.id,key:k,quantity:1});
  }
  const out=groups.map(g=>rows.get(g.id)),short=out.reduce((n,r)=>n+r.short,0),kindsShort=Math.max(0,minimumDistinct-kinds());
  return {ready:short===0&&kindsShort===0,groups:out,short,kindsShort,allocation:alloc.filter(a=>a.quantity>0)};
}
// 只看不交: one free bird of each kind at home, nothing consumed (locks do not matter, no bird leaves).
function show(s,allowed,need){
  const keys=allowed.filter(k=>{const c=resolveSpecies(k);return c&&speciesDiscovered(s,c.egg,c.id)&&freeCount(s,k)>=1;}).sort((a,b)=>freeCount(s,b)-freeCount(s,a)||(a<b?-1:1));
  return {ready:keys.length>=need,have:Math.min(keys.length,need),need,kindsShort:Math.max(0,need-keys.length),short:0,keys:keys.slice(0,need),allowed};
}

// What one order needs right now and whether a single 「交付」 / 「摆出来」 finishes it. ref: {kind:'order'|'proposal', id}.
export function orderStatus(s,ref,now=Date.now()){
  const o=s.expansion?.orders;if(!o)return null;
  if(ref.kind==='order'){
    const a=o.active.find(x=>x.id===ref.id);if(!a)return null;
    const base={ref,templateId:a.templateId,kind:a.kind,region:orderRegion(a.templateId,a.region),bonusCP:a.bonusCP,accepted:true};
    if(a.kind==='display')return {...base,...show(s,a.groups[0].allowed,a.minimumDistinct),groups:[]};
    const groups=a.groups.map(g=>({id:g.id,sourceGroupId:g.sourceGroupId,quantity:g.quantity,remaining:g.quantity-sum(g.delivered),allowed:g.allowed}));
    return {...base,...fill(s,{groups,minimumDistinct:a.minimumDistinct,delivered:new Set(a.groups.flatMap(g=>Object.keys(g.delivered))),ownerId:a.id})};
  }
  const p=o.proposals.find(x=>x.id===ref.id),def=p&&template(p.templateId);if(!def)return null;
  const base={ref,templateId:def.id,kind:def.kind,bonusCP:def.bonusCP,accepted:false};
  const options=orderOptions(s,def.id,now);
  // Nothing the kitchen can make yet fits: the order waits (never pushed into 下一锅).
  if(!options.length)return {...base,region:orderRegion(def.id),blocked:true,ready:false,short:0,kindsShort:0,groups:def.groups.map(g=>({id:g.id,sourceGroupId:g.id,quantity:g.quantity,remaining:g.quantity,have:0,short:g.quantity,allowed:g.allowed}))};
  const need=def.kind==='display'?(def.displayDistinct??def.minimumDistinct):def.minimumDistinct;
  let best=null;
  for(const option of options){
    const groups=optionGroups(def.id,option).map(g=>({...g,remaining:g.quantity}));
    const st=def.kind==='display'?{...show(s,groups[0].allowed,need),groups:[]}:fill(s,{groups,minimumDistinct:need});
    const row={...base,...st,option,region:orderRegion(def.id,option.region)};
    if(!best||row.ready&&!best.ready||row.ready===best.ready&&row.short+row.kindsShort<best.short+best.kindsShort)best=row;
  }
  return best;
}

// The one-step command. Returns the delivery result, the order's name and its 情报.
export function completeOrderNow(s,ref,now=Date.now()){
  assertNewOperation('orders');
  const st=orderStatus(s,ref,now);if(!st)throw Error('这份订单已经变化');
  if(!st.ready)throw Error(st.blocked?'现在还做不出这份订单要的伙伴':st.short?`还差 ${st.short} 只`:`还差 ${st.kindsShort} 种不同的伙伴`);
  // whether this completion brings 情报 is read before it counts (order-intel.js ORDER_INTEL)
  const due=orderIntelDue(s,st.templateId);
  const orderId=st.accepted?ref.id:acceptProposal(s,ref.id,st.option,now,{deliverNow:true}).id;
  const result=st.kind==='display'?displayOrder(s,orderId,st.keys):deliverOrderGroups(s,orderId,st.allocation,now);
  const intel=grantOrderIntel(s,st.templateId,st.region,now,{due});
  return {...result,kind:st.kind,name:CONTENT_TEXT[st.templateId]?.name??st.templateId,intel};
}

// Birds the order board is counting on (what each order would deliver now, a 厨房往事 chapter's dish up to what it
// still needs): the shop's auto stock leaves them at home, so 「其余多余伙伴一起卖」 never sells what an order waits for.
// 「只看不交」 orders take nobody.
export function orderHolds(s,now=Date.now()){
  const held={},add=(k,n)=>{if(n>0)held[k]=(held[k]??0)+n;},o=s.expansion?.orders;
  for(const ref of [...(o?.active??[]).map(a=>({kind:'order',id:a.id})),...(o?.proposals??[]).map(p=>({kind:'proposal',id:p.id}))]){
    const st=orderStatus(s,ref,now);if(st&&st.kind!=='display')for(const a of st.allocation??[])add(a.key,a.quantity);
  }
  const story=storyStatus(s);if(story?.unlocked&&story.choice)add(story.choice.species,story.have);
  return held;
}
// 厨房往事: the chapter now open (or the next one still locked), the dish it can be filled with, and what is short.
export function storyStatus(s){
  const list=storyOrders(s),o=list.find(x=>!x.completed);if(!o)return null;
  const rows=o.choices.filter(c=>c.available).map(c=>({...c,spare:spareFor(s,c.species)}));
  const fixed=o.accepted&&o.delivered>0?rows.find(c=>c.species===o.choice):null;
  const choice=fixed??(o.accepted?rows.find(c=>c.species===o.choice):null)??[...rows].sort((a,b)=>b.spare-a.spare)[0]??null;
  const need=choice?choice.count-o.delivered:0,short=choice?Math.max(0,need-choice.spare):0;
  return {id:o.id,index:o.index,chapter:o.chapter,unlocked:o.unlocked,requirements:o.requirements,choice,need,have:choice?Math.min(need,choice.spare):0,short,
    ready:o.unlocked&&!!choice&&need>0&&short===0,extraCP:o.extraCP,delivered:o.delivered};
}
export function completeStoryNow(s,id,now=Date.now()){
  const st=storyStatus(s);if(!st||st.id!==id)throw Error('这一章已经变化');
  if(!st.ready)throw Error(st.unlocked?`还差 ${st.short} 只`:st.requirements.filter(r=>!r.met).map(r=>r.text).join('；'));
  acceptOrder(s,id,st.choice.species);
  return {...deliverOrder(s,id,st.need,now),species:st.choice.species,name:st.chapter.title.split('：').at(-1)};
}
