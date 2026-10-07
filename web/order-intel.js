// 订单情报 (2026-10-07): a finished order may bring word from its region (地区情报), one of two kinds.
//  调查情报 — the next layer still unknown of one 线索册 partner: the same facts 寻访 reads, never a layer the player already
//            has (skills, 研读, earlier clues); unlike a trip it may also tell 「其余条件」 when that is a holiday, a season
//            or a time of day (knowledge.js SPECIAL_CONDITION). Never a regional partner's complete method: that stays
//            the trip's (and 研读's).
//  地点情报 — where to look for a special find of the region (the order's own `card` first): the find's own hint, kept in
//            progress.knowledge.hints and shown on that region's card until the find is made.
// Not every order (user, after batch 3): ORDER_INTEL says which completions bring 情报, so 生意 never outpaces 寻访. And it
// stays in the order's region: the tracked partner when its main clue region is this one; the order's own find the first
// time it can be looked for (region open, its gate met); this region's partners, the one closest to being worked out first
// (it could be cooked now, then the most layers known); another find of this region; with nothing new to tell, the region
// sends two of its materials instead (when the bag has room). An order whose story points to another region would name it
// in ORDER_INTEL_REGION; none does yet (外地尝鲜箱 already speaks for the region it was frozen to).
import {REGIONAL,CONTENT_TEXT,REQUIREMENTS} from './content-registry.js';
import {evaluate} from './requirements.js';
import {hasRegionalCard,regionInfo,materialIdentified,REGIONAL_RELEASE} from './region-model.js';
import {regionPartners,clueRegionOf,REGION_SHORT,REGION_ROUTE} from './clue-regions.js';
import {nextClueLayer,trackedKey,factId,specialConditionText} from './knowledge.js';
import {recipeId} from './recipe-book.js';
import {clueAdvance} from './journey-model.js';
import {materialRoom} from './material-capacity.js';
import {availableIngredientIds} from './ingredient-unlocks.js';
import {ROUTES} from './exploration.js';

// Which completions of an order bring 情报: 'always'; 'first' (its first completion only); n (the first, then every n-th).
// The one-off display and the 外地尝鲜箱 (all about regional flavours) always do; the orders written around a place of
// their region every third time; everyday orders only the first time. Batch 5 tunes the numbers.
export const ORDER_INTEL=Object.freeze({O01:'first',O02:3,O03:'first',O04:'always',O05:3,O06:'always',O07:3,O08:'first',O09:'first',O10:3,O11:3,O12:'first'});
export const ORDER_INTEL_REGION=Object.freeze({});
export const INTEL_MATERIALS=2;
// Due on this completion: counts the times the order was finished before (orders.js templateProgress).
export function orderIntelDue(s,templateId){
  const rule=ORDER_INTEL[templateId],done=s.expansion?.orders?.templateProgress?.[templateId]?.completed??0;
  if(rule==='always')return true;if(rule==='first')return done===0;
  return Number.isInteger(rule)&&rule>0?done%rule===0:false;
}
// The region an order speaks for: the region an 外地尝鲜箱 was frozen to, else the region of its special find.
export function orderRegion(templateId,frozen=null){
  if(frozen)return frozen;if(ORDER_INTEL_REGION[templateId])return ORDER_INTEL_REGION[templateId];
  const card=REGIONAL.orders.find(o=>o.id===templateId)?.card;return card?card[0]:null;
}
export const intelHints=s=>Array.isArray(s.progress?.knowledge?.hints)?s.progress.knowledge.hints:[];
const gateMet=(s,card)=>{const r=REQUIREMENTS[card.gateRequirement];return !!r&&r.kind!=='unavailable'&&evaluate(r,s).met;};
const partnersIn=(s,region)=>regionPartners(region).filter(k=>!k.startsWith('1:')||s.duck);
const keyOrder=k=>k.split(':').map(Number);

function clueTicket(s,key,now){
  const next=nextClueLayer(s,key,now,{deep:true});if(!next)return null;
  const r=next.path,detail=next.level===5?specialConditionText(r):undefined;
  return {key,recipeId:recipeId(r),level:next.level,fact:factId(r,next.level),...(detail?{detail}:{}),ready:r.conditions.every(c=>c.met),known:next.known};
}
function bestClue(s,keys,now){
  const rows=keys.map(k=>clueTicket(s,k,now)).filter(Boolean);
  rows.sort((a,b)=>b.ready-a.ready||b.known-a.known||keyOrder(a.key)[0]-keyOrder(b.key)[0]||keyOrder(a.key)[1]-keyOrder(b.key)[1]);
  return rows[0]??null;
}
// A find worth a hint now: released, not made, not hinted yet, its region open and its own gate met.
const hintable=(s,card)=>!!card&&REGIONAL_RELEASE.cards.includes(card.id)&&!hasRegionalCard(s,card.id)&&!intelHints(s).includes(card.id)&&regionInfo(s,card.region).met&&gateMet(s,card);
// The special find an order points to, when a hint about it would help now.
function placeCard(s,templateId){
  const id=REGIONAL.orders.find(o=>o.id===templateId)?.card,card=id?REGIONAL.cards.find(c=>c.id===id):null;
  return hintable(s,card)?card:null;
}
// Another find of the same region (stories and events before specimens, which the first trips bring anyway).
const otherCard=(s,region)=>REGIONAL.cards.filter(c=>c.region===region).sort((a,b)=>(a.type==='specimen')-(b.type==='specimen')).find(c=>hintable(s,c))??null;
// The region's materials for the fallback: its own identified ones first, then what its route brings home now.
function regionMaterial(s,region){
  const own=REGIONAL.materials.filter(m=>m.region===region&&materialIdentified(s,m.id)).map(m=>m.id);
  const route=ROUTES.find(r=>r.id===REGION_ROUTE[region]),open=availableIngredientIds(s);
  return [...own,...(route?.pool??[]).filter(id=>open.includes(id))][0]??null;
}

// Read-only: what finishing this order now would tell. due: false when this completion brings no 情报 (ORDER_INTEL).
export function orderIntelPlan(s,templateId,region,now=Date.now(),{due=true}={}){
  if(!due||!region)return {kind:'off',region};
  const tracked=trackedKey(s);
  if(tracked&&clueRegionOf(tracked)?.region===region){const t=clueTicket(s,tracked,now);if(t)return {kind:'clue',region,clue:t};}
  const card=placeCard(s,templateId);if(card)return {kind:'place',region:card.region,cardId:card.id};
  const local=bestClue(s,partnersIn(s,region),now);if(local)return {kind:'clue',region,clue:local};
  const other=otherCard(s,region);if(other)return {kind:'place',region,cardId:other.id};
  const material=regionMaterial(s,region),n=Math.min(INTEL_MATERIALS,materialRoom(s));
  return material!=null&&n>0?{kind:'material',region,materialId:material,quantity:n}:{kind:'none',region};
}

// Applies the 情报 inside the order's own transaction. Returns what the result card shows: the region, and the partner's
// advance (who, the new fact, 调查 x/5 → y/5), the find's hint, or the materials; null when this completion brings none.
export function grantOrderIntel(s,templateId,region,now=Date.now(),{due=true}={}){
  const plan=orderIntelPlan(s,templateId,region,now,{due}),regionName=REGION_SHORT[plan.region]??'';
  if(plan.kind==='off')return null;
  if(plan.kind==='clue'){
    const {ready,known,...ticket}=plan.clue,advance=clueAdvance(s,ticket,now);
    if(!s.progress.knowledge.facts.includes(ticket.fact))s.progress.knowledge.facts.push(ticket.fact);
    return {kind:'clue',region:plan.region,regionName,clue:ticket,advance};
  }
  if(plan.kind==='place'){
    const k=s.progress.knowledge;k.hints=[...intelHints(s),plan.cardId];
    return {kind:'place',region:plan.region,regionName,hint:placeHint(s,plan.cardId)};
  }
  if(plan.kind==='material'){
    s.ingredients[plan.materialId]=(s.ingredients[plan.materialId]??0)+plan.quantity;
    return {kind:'material',region:plan.region,regionName,materialId:plan.materialId,quantity:plan.quantity};
  }
  return {kind:'none',region:plan.region,regionName};
}

// One hinted find, in the words the region card uses: the region's name, the spot, and the find's own hint.
export function placeHint(s,cardId){
  const card=REGIONAL.cards.find(c=>c.id===cardId);if(!card)return null;
  const region=REGIONAL.regions.find(r=>r.id===card.region),place=region?.places.find(p=>p.id===card.placeId);
  return {cardId,region:card.region,regionName:REGION_SHORT[card.region],fullName:CONTENT_TEXT[card.region]?.name??'',placeId:card.placeId,placeName:place?.name??'',
    focus:card.focus,team:{trait:card.team.trait,environment:card.team.environment},text:CONTENT_TEXT[cardId]?.hint??''};
}
// The hints still open in one region (the find not made yet), newest first.
export function regionHints(s,regionId){
  return intelHints(s).filter(id=>id.startsWith(regionId+'-')&&!hasRegionalCard(s,id)).reverse().map(id=>placeHint(s,id)).filter(Boolean);
}
