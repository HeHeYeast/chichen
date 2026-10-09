import {extraRegion} from './extra-regions.js';
import {ingredientUnlockInfo} from './ingredient-unlocks.js';
// 寻访 region card and trip return, read-only: how much each region still has to tell (伙伴线索 / 新食材 / 特殊发现),
// where the tracked partner's next clue is, and what a returned trip's clue adds to the 线索册 (who, what, x/5 → y/5).
import {REGIONAL,CONTENT_TEXT} from './content-registry.js';
import {speciesDiscovered} from './species-state.js';
import {regionPartners,clueRegionOf,regionAccess,REGION_SHORT,REGION_ROUTE,ROUTE_REGION} from './clue-regions.js';
import {nextClueLayer,trackedKey,speciesCode,tripClue,clueFactText} from './knowledge.js';
import {clueRow} from './clue-book.js';
import {hasRegionalCard,materialIdentified,REGIONAL_RELEASE} from './region-model.js';
import {explorationInfo} from './exploration.js';
import {isRegionalKey,regionalGate,regionalRuntimeId} from './regional-clues.js';
// A regional partner (loop batch 4) still has something here before its 方向: its specimen or story card (a trip step).
const regionalStep=(s,key)=>{if(!isRegionalKey(key))return null;const g=regionalGate(s,key);return g.met?null:g.step;};

export const regionOfTrip=t=>t?.regional?.regionId??ROUTE_REGION[t?.routeId]??null;

// Where the tracked partner's next clue is: its region, whether a trip there can still read something, whether that
// region can be visited yet. Null when nothing is tracked.
export function trackedTrail(s,now=Date.now()){
  const key=trackedKey(s);if(!key)return null;
  const [egg,id]=key.split(':').map(Number),place=clueRegionOf(key),next=nextClueLayer(s,key,now),step=regionalStep(s,key);
  const access=place?regionAccess(s,place.region):null,find=step?.trip&&!step.blocked.length?step:null;
  // find: a regional partner's specimen or story card the trip looks for (sure to come home for the tracked partner)
  return {key,egg,id,code:speciesCode(key),region:place?.region??null,regionName:place?REGION_SHORT[place.region]:'',fullName:place?CONTENT_TEXT[place.region]?.name??'':'',
    scout:!!next||!!find,find,step,open:find?!!access?.met:!!access?.open,missing:access?.missing??[]};
}

export function regionCard(s,regionId,now=Date.now()){
  const partners=regionPartners(regionId).filter(k=>!k.startsWith('1:')||s.duck);
  const done=partners.filter(k=>{const [e,i]=k.split(':').map(Number);return speciesDiscovered(s,e,i)||!nextClueLayer(s,k,now)&&!regionalStep(s,k);}).length;
  const extra=extraRegion(regionId);
  const materials=REGIONAL.materials.filter(m=>m.region===regionId&&REGIONAL_RELEASE.materials.includes(m.id));
  const finds=REGIONAL.cards.filter(c=>c.region===regionId&&c.type!=='specimen'&&REGIONAL_RELEASE.cards.includes(c.id));
  const route=explorationInfo(s,REGION_ROUTE[regionId],[],now),access=regionAccess(s,regionId);
  // the latest thing found here: the last clue a settled trip brought, else the newest regional find
  const t=s.progress.trip;let recent='';
  if(t?.status==='settled'&&regionOfTrip(t)===regionId&&t.clueResult)recent=`${speciesCode(t.clueResult.key)} ${clueFactText(t.clueResult)}`;
  else{const facts=s.expansion?.regions?.history?.cardFacts??{},latest=REGIONAL.cards.filter(c=>c.region===regionId&&hasRegionalCard(s,c.id)).sort((a,b)=>(facts[b.id]?.seq??s.expansion.discovery.cards[b.id]??0)-(facts[a.id]?.seq??s.expansion.discovery.cards[a.id]??0))[0];
    if(latest)recent=CONTENT_TEXT[latest.id]?.title??'';}
  return {id:regionId,name:access.name,short:REGION_SHORT[regionId],met:access.met,routeOnly:access.routeOnly,missing:access.missing,
    hours:route.hours,minUnits:route.minUnits,pool:route.pool.filter(p=>p.unlocked).map(p=>p.id),
    clues:{done,total:partners.length},materials:extra?{done:extra.materials.filter(id=>ingredientUnlockInfo(s,id).available).length,total:2}:{done:materials.filter(m=>materialIdentified(s,m.id)).length,total:materials.length},
    finds:{done:finds.filter(c=>hasRegionalCard(s,c.id)).length,total:finds.length},recent};
}

// What a returned trip's clue adds: the partner, the new fact in 线索册 words, and 调查 x/5 before and after.
// Read before the basket is claimed; null when the trip brings no clue.
export function tripClueAdvance(s,t,now=Date.now()){
  if(!t||t.clueProcessed||!t.clueHit)return null;
  const c=tripClue(s,t,now);return c?clueAdvance(s,c,now):null;
}
export function clueAdvance(s,c,now=Date.now()){
  const before=clueRow(s,c.key,now,null);if(!before)return null;
  const k=s.progress.knowledge,view={...s,progress:{...s.progress,knowledge:{...k,facts:k.facts.includes(c.fact)?k.facts:[...k.facts,c.fact]}}};
  // a regional partner's fifth layer writes its complete method down (exploration.js claimTrip)
  const method=c.level===5?regionalRuntimeId(c.key):null;
  if(method&&!s.expansion.methods.full.includes(method))view.expansion={...s.expansion,methods:{...s.expansion.methods,full:[...s.expansion.methods.full,method]}};
  const after=clueRow(view,c.key,now,null)??before;
  const [egg,id]=c.key.split(':').map(Number);
  return {key:c.key,egg,id,code:speciesCode(c.key),text:clueFactText(c),before:before.progress,after:after.progress,complete:after.held,
    tracked:trackedKey(s)===c.key,silhouette:before.silhouette||c.level===1};
}
