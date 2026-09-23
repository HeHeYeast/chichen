// Read-only view of situational purchases. Unknown species never appear by
// name: groups list collected candidates and only count the rest.
import {REGIONAL,CONTENT_TEXT,resolveSpecies} from './content-registry.js';
import {speciesDiscovered} from './species-state.js';
import {freeCount,homeCount,usableByOwner} from './inventory.js';
import {ordersUnlockInfo,orderOptions,MAX_ACTIVE_ORDERS,MAX_PROPOSALS} from './orders.js';
import {storyOrders} from './story-orders.js';

const SELECTORS={snack:'小点',portableMeal:'便携主食',savoryMeal:'咸香主食',season:'四时手作',display:'有趣模样',food:'任选食用',regionFood:'地方新味'};
const code=key=>{const [egg,id]=key.split(':').map(Number);return `${egg?'D':'C'}${String(id+1).padStart(3,'0')}`;};
export const selectorLabel=selector=>CONTENT_TEXT.labels?.tags?.[selector]??SELECTORS[selector]??selector;
const template=id=>REGIONAL.orders.find(o=>o.id===id);
const sum=map=>Object.values(map).reduce((a,b)=>a+b,0);

function candidates(s,allowed,orderId=null){
  const known=allowed.filter(key=>{const c=resolveSpecies(key);return speciesDiscovered(s,c.egg,c.id);});
  return {rows:known.map(key=>({key,code:code(key),name:resolveSpecies(key).title_zh_CN,egg:Number(key[0]),id:Number(key.split(':')[1]),price:resolveSpecies(key).cp_1,
    free:freeCount(s,key),home:homeCount(s,key),usable:orderId?usableByOwner(s,key,orderId):freeCount(s,key)})),hidden:allowed.length-known.length};
}
function groupLabel(def,group){
  const source=def.groups.find(g=>g.id===group.sourceGroupId??group.id);
  const chapter=group.id.includes(':')?group.id.split(':')[1]:null;
  return chapter?`${selectorLabel(source.selector)} · ${chapter}`:selectorLabel(source.selector);
}

export function ordersModel(s,now){
  const o=s.expansion.orders,unlock=ordersUnlockInfo(s);
  const story=storyOrders(s).map(x=>({id:x.id,chapter:x.chapter,unlocked:x.unlocked,accepted:x.accepted,completed:x.completed,delivered:x.delivered}));
  const active=(o?.active??[]).map(a=>{
    const def=template(a.templateId),variant=CONTENT_TEXT[a.variantId]??{};
    return {id:a.id,templateId:a.templateId,kind:a.kind,name:CONTENT_TEXT[a.templateId]?.name??a.templateId,request:variant.text??CONTENT_TEXT[a.templateId]?.request,variantLabel:variant.label??'',
      region:a.region,regionName:a.region?CONTENT_TEXT[a.region]?.name:null,chapters:a.chapters,bonusCP:a.bonusCP,minimumDistinct:a.minimumDistinct,paidCP:a.paidCP,needsRestock:a.needsRestock,
      reserved:{...a.reserved},reservedTotal:sum(a.reserved),
      groups:a.groups.map(g=>({id:g.id,label:groupLabel(def,g),quantity:g.quantity,delivered:sum(g.delivered),remaining:g.quantity-sum(g.delivered),
        deliveredByKey:{...g.delivered},...candidates(s,g.allowed,a.id)})),
      distinctSoFar:new Set(a.groups.flatMap(g=>Object.keys(g.delivered))).size};
  });
  const proposals=(o?.proposals??[]).map(p=>{
    const def=template(p.templateId),options=orderOptions(s,p.templateId,now);
    return {id:p.id,templateId:p.templateId,kind:def.kind,name:CONTENT_TEXT[p.templateId]?.name,request:CONTENT_TEXT[p.templateId]?.request,bonusCP:def.bonusCP,minimumDistinct:def.kind==='display'?(def.displayDistinct??def.minimumDistinct):def.minimumDistinct,
      groups:def.groups.map(g=>({id:g.id,label:selectorLabel(g.selector),quantity:g.quantity})),
      options:options.map(x=>({...x,label:CONTENT_TEXT[x.variantId]?.label??x.variantId,text:CONTENT_TEXT[x.variantId]?.text??'',regionName:x.region?CONTENT_TEXT[x.region]?.name:null})),
      firstResult:!(o.templateProgress[p.templateId]?.completed>0)?CONTENT_TEXT[def.firstResult.id]?.name:null};
  });
  return {unlock,story,active,proposals,canAccept:(o?.active.length??0)<MAX_ACTIVE_ORDERS,limits:{active:MAX_ACTIVE_ORDERS,proposals:MAX_PROPOSALS}};
}
