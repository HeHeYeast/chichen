import {explorationInfo,depart} from './exploration.js';
import {REGIONAL,REQUIREMENTS,resolveSpecies} from './content-registry.js';
import {evaluate} from './requirements.js';
import {reduceFacts} from './facts.js';
import {availableCount,homeCount,lockedCount} from './inventory.js';
import {discoveryCount} from './progression.js';
import {economicRandom,RNG_ALGORITHM} from './rng.js';
import {randomUnit} from './progression.js';
import {REGIONAL_RELEASE,regionInfo,regionalCard,hasRegionalCard,materialIdentified,regionalCompanion,teamMeets,regionCardCandidates,regionalCardChance,nextRegionalFact,recordRegionalTrip} from './region-model.js';
import {regionalRecipeInfo,regionalMethodPlan,settleRegionalMethod,refreshRegionalDirections,identifyMaterial} from './regional-methods.js';
import {newOperationsEnabled,assertNewOperation} from './rollback-policy.js';
import {trackedKey} from './knowledge.js';
import {isRegionalKey,regionOfKey,regionalGate} from './regional-clues.js';

const FOCUSES=Object.freeze(['materials','specimen','lore']);
const missCount=(s,regionId,focus)=>s.expansion.cardProtection[regionId]?.[focus]??0;
// The first complete trip guarantees an entry specimen the kitchen can try now;
// the region's designated entry material is preferred when both are executable.
function introCardFor(s,region,specimenEligibility){
  const entryMaterial=REGIONAL.materials.find(m=>m.region===region.id&&m.entrySpecies===region.entrySpecies)?.id;
  const cards=REGIONAL.cards.filter(c=>c.region===region.id&&c.type==='specimen'&&REGIONAL_RELEASE.cards.includes(c.id)&&!hasRegionalCard(s,c.id)&&specimenEligibility[c.material]);
  return cards.find(c=>c.material===entryMaterial)??cards[0]??null;
}
export const regionalCardGateMet=(s,card)=>{const r=REQUIREMENTS[card.gateRequirement];return !!r&&r.kind!=='unavailable'&&r.kind!=='compiled'&&evaluate(r,s).met;};
// The tracked partner's next step when it is a find in this region (its specimen, or the story card it waits on): like a
// tracked partner's next clue layer, a trip that can find it brings it home (loop batch 4). Null otherwise.
export function trackedFind(s,regionId){
  const key=trackedKey(s);if(!key||!isRegionalKey(key)||regionOfKey(key)!==regionId)return null;
  const gate=regionalGate(s,key);return !gate.met&&gate.step.trip?.cardId?{key,cardId:gate.step.trip.cardId,placeId:gate.step.trip.placeId,focus:gate.step.trip.focus}:null;
}

export function regionalTripInfo(s,{regionId='V',placeId='V:0',focus='specimen',members=[],directed=[],priority=null,light=false,sampling=false,guide=false,cargo=null,cargoOverrideKeepOne=false}={},now=Date.now()){
  const area=regionInfo(s,regionId),missing=[...area.missing];
  if(!newOperationsEnabled('region',regionId))missing.push('这处地区暂时暂停新出发，已有队伍仍可归来和领取。');
  if(!area.places.some(p=>p.id===placeId))throw Error('这处地点不属于所选地区。');
  if(!FOCUSES.includes(focus))throw Error('请选择补材料、找标本或寻见闻。');
  if(typeof sampling!=='boolean')throw Error('请选择普通材料池或地区采样池。');
  if(!Array.isArray(members)||members.length>3)throw Error('请选择1至3位同行伙伴。');
  const companions=members.map(regionalCompanion);
  const old=explorationInfo(s,area.region.route,members,now,{light});
  if(!members.length)missing.push('请选择至少一位在家伙伴。');
  if(members.some(key=>availableCount(s,key)<members.filter(m=>m===key).length))missing.push('同行伙伴在家的可用数量不足。');
  if(['running','returned'].includes(s.progress.trip?.status))missing.push('先等当前队伍归来，并领完归来篮。');
  const entrySpecies=resolveSpecies(area.region.entrySpecies);
  const entry=regionalRecipeInfo(s,entrySpecies.recipeId,{entry:true});
  const specimenEligibility=Object.fromEntries(REGIONAL.materials.filter(m=>m.region===regionId).map(m=>[m.id,regionalRecipeInfo(s,resolveSpecies(m.entrySpecies).recipeId,{entry:true}).met]));
  const introCard=introCardFor(s,area.region,specimenEligibility);
  const firstSpecimen=area.met&&!s.expansion.regions.introSpecimenDone.includes(regionId)&&!!introCard;
  const ordinaryCandidates=regionCardCandidates(s,{regionId,placeId,focus,specimenEligibility,companions,gateMet:card=>regionalCardGateMet(s,card)});
  const rawCandidates=firstSpecimen?[introCard,...ordinaryCandidates.filter(c=>c.id!==introCard.id)]:ordinaryCandidates;
  const tracked=trackedFind(s,regionId),sureCardId=!firstSpecimen&&tracked&&rawCandidates.some(c=>c.id===tracked.cardId)?tracked.cardId:null;
  const candidates=rawCandidates.map(card=>({cardId:card.id,chance:regionalCardChance(card,companions)}));
  const introMaterial=firstSpecimen?introCard.material:null;
  const samplingIds=sampling?area.region.materials.filter(id=>REGIONAL_RELEASE.materials.includes(id)&&materialIdentified(s,id)):[];
  // Slots: intro trial first, then the cargo exchange, then one sampling slot;
  // a contract that cannot fit is refused instead of adding a material.
  const guideInfo=guideOption(s,regionId,placeId);
  if(typeof guide!=='boolean')throw Error('请选择是否追寻沿湾路标。');
  if(guide&&!guideInfo.available)missing.push(guideInfo.reason);
  const cargoInfo=cargoOption(s,{regionId,placeId,members,selection:cargo,overrideKeepOne:cargoOverrideKeepOne});
  if(cargo!==null&&cargoInfo.missing.length)missing.push(...cargoInfo.missing);
  const cargoUsed=cargo!==null&&!cargoInfo.missing.length;
  if(cargoUsed&&old.baseUnits-(firstSpecimen?1:0)<1)missing.push('本趟基础材料格不够安排带货交换。');
  if(sampling&&(!samplingIds.length||old.baseUnits-(firstSpecimen?1:0)-(cargoUsed?1:0)<1))missing.push('本趟没有可用的已辨认地区采样位置。');
  const regionalDirected=Array.isArray(directed)?directed.filter(id=>id>=75):[];
  const legacyDirected=Array.isArray(directed)?directed.filter(id=>id<75):[];
  if(!Array.isArray(directed)||new Set(directed).size!==directed.length||directed.length>old.directedUnits||legacyDirected.length>old.baseUnits-(firstSpecimen?1:0)-(sampling?1:0)-(cargoUsed?1:0)||regionalDirected.length>1||regionalDirected.some(id=>!samplingIds.includes(id))||legacyDirected.some(id=>!old.pool.some(p=>p.id===id&&p.unlocked)))missing.push('定向材料与本趟基础材料格不相容。');
  if(priority!==null&&(!s.progress.skills['OBS-S']||!old.clues.slice(0,3).some(c=>c.key===priority)))missing.push('旧配方定向线索尚不符合资格。');
  return {...old,regionId,placeId,focus,met:missing.length===0,canDepart:missing.length===0,missing,companions,firstSpecimen,introMaterial,introCardId:firstSpecimen?introCard.id:null,introOverridesPlace:firstSpecimen&&introCard.placeId!==placeId,candidates,sureCardId,trackedFind:tracked,method:regionalMethodPlan(s,regionId),samplingIds,regionalDirected,legacyDirected,guide:guideInfo,guideRequested:guide&&guideInfo.available,cargo:cargoInfo,cargoUsed,slotSources:Array.from({length:old.baseUnits},(_,i)=>{const cargoSlot=cargoUsed?(firstSpecimen?1:0):-1,sampleSlot=sampling?(firstSpecimen?1:0)+(cargoUsed?1:0):-1;return firstSpecimen&&i===0?'intro-specimen':i===cargoSlot?'cargo-exchange':i===sampleSlot?'regional-sampling':'legacy';}),entryMissing:firstSpecimen||s.expansion.regions.introSpecimenDone.includes(regionId)?[]:entry.missing};
}

export function departRegional(s,options={},now=Date.now()){
  assertNewOperation('region',options.regionId??'V');
  const info=regionalTripInfo(s,options,now);
  if(!info.canDepart)throw Error(info.missing[0]);
  // Separate channels keep the old material/CP/clue interpretation intact when
  // new card candidates are added. Preview never allocates either channel.
  // Candidate order is frozen here: the intro promise first, the remaining
  // eligible cards in equal-weight random order (own channel), then one roll each.
  // The tracked partner's find (sureCardId) goes first, ahead of the shuffled rest, and is sure to be the one.
  const order=economicRandom(s,'regional-card-order'),sure=info.candidates.find(c=>c.cardId===info.sureCardId)??null,rest=info.candidates.slice(info.firstSpecimen?1:0).filter(c=>c!==sure);
  for(let i=rest.length-1;i>0;i--){const j=Math.floor(randomUnit(order)*(i+1));[rest[i],rest[j]]=[rest[j],rest[i]];}
  const random=economicRandom(s,'regional-cards');
  const candidates=[...(info.firstSpecimen?[info.candidates[0]]:[]),...(sure?[sure]:[]),...rest].map(c=>({...c,roll:randomUnit(random)}));
  const cargoSlot=info.cargoUsed?(info.firstSpecimen?1:0):null;
  const sampling=info.samplingIds.length?{eligibleIds:[...info.samplingIds],materialId:info.regionalDirected[0]??info.samplingIds[Math.floor(randomUnit(economicRandom(s,'regional-materials'))*info.samplingIds.length)],baseSlot:(info.firstSpecimen?1:0)+(info.cargoUsed?1:0),directed:info.regionalDirected.length>0}:null;
  const result=depart(s,{routeId:info.route.id,members:options.members??[],directed:info.legacyDirected,priority:options.priority??null,light:!!options.light},now,economicRandom(s,'regional-legacy-trip'));
  const trip=s.progress.trip;
  if(info.firstSpecimen||sampling||info.cargoUsed){
    // A base slot is reassigned, never an extra reward. Shift old directed
    // results to remaining base slots and keep the independently rolled bonus.
    const base=trip.remaining.slice(0,info.baseUnits),extra=trip.remaining.slice(info.baseUnits);
    const allocated=[...(info.firstSpecimen?[info.introMaterial]:[]),...(info.cargoUsed?[info.cargo.rewardMaterial]:[]),...(sampling?[sampling.materialId]:[])];
    trip.remaining=[...allocated,...base.slice(0,info.baseUnits-allocated.length),...extra];
  }
  trip.version=2;
  if(info.cargoUsed)trip.cargo={cardId:info.cargo.cardId,selection:{...options.cargo},quantity:info.cargo.quantity,rewardMaterial:info.cargo.rewardMaterial,baseSlot:cargoSlot,processed:false,outcome:null};
  trip.regional={rulesVersion:1,rngAlgorithm:RNG_ALGORITHM,regionId:info.regionId,placeId:info.placeId,focus:info.focus,intro:info.firstSpecimen?{cardId:info.introCardId,materialId:info.introMaterial,baseSlot:0}:null,sampling,candidates,failedBefore:missCount(s,info.regionId,info.focus),method:structuredClone(info.method),companions:structuredClone(info.companions),processed:false,result:null,...(info.guideRequested?{guide:true}:{}),...(sure?{sure:sure.cardId}:{})};
  if(!s.expansion.regions.opened.includes(info.regionId))s.expansion.regions.opened.push(info.regionId);
  return {...result,regional:trip.regional};
}

// Reads only frozen per-candidate probabilities. An already registered target
// falls through to its saved successor; settlement never samples another roll.
export function regionalCardOutcome(s,ticket){
  const candidate=ticket.candidates.find(c=>!hasRegionalCard(s,c.cardId));
  if(!candidate)return {cardId:null,eligible:false};
  const guaranteed=ticket.intro?.cardId===candidate.cardId||ticket.sure===candidate.cardId||ticket.failedBefore>=3;
  return {cardId:guaranteed||candidate.roll<candidate.chance?candidate.cardId:null,eligible:true,focus:regionalCard(candidate.cardId).focus};
}

export function settleRegionalTrip(s,trip){
  if(trip?.version!==2||!trip.regional)return null;
  const ticket=trip.regional;
  if(ticket.processed)return ticket.result;
  if(!['returned','settled'].includes(trip.status))return null;
  const outcome=regionalCardOutcome(s,ticket);
  if(outcome.cardId)s.expansion.discovery.cards[outcome.cardId]=nextRegionalFact(s);
  if(outcome.eligible){
    const focus=outcome.focus;
    if(['specimen','lore'].includes(focus)){
      const counter=s.expansion.cardProtection[ticket.regionId]??={specimen:0,lore:0};
      counter[focus]=outcome.cardId?0:Math.min(3,ticket.failedBefore+1);
    }
  }
  if(ticket.intro&&!s.expansion.regions.introSpecimenDone.includes(ticket.regionId))s.expansion.regions.introSpecimenDone.push(ticket.regionId);
  refreshRegionalDirections(s,ticket.regionId);
  const methodId=settleRegionalMethod(s,ticket.regionId,ticket.method);
  recordRegionalTrip(s,trip,outcome.cardId);
  const foundMaterial=regionalCard(outcome.cardId)?.material;
  if(foundMaterial!=null&&!materialIdentified(s,foundMaterial))identifyMaterial(s,foundMaterial,{settlement:true});
  if(s.expansion.facts)reduceFacts(s,[{kind:'tripComplete',tripId:trip.id,region:ticket.regionId,placeId:ticket.placeId,focus:ticket.focus,cardId:outcome.cardId,members:ticket.companions.map(c=>({key:c.key,gather:c.G,discover:c.F,environment:c.environment,traits:[...c.traits]}))}]);
  // Cargo is consumed only by a complete return: no sale CP, no order/business counts.
  if(trip.cargo&&!trip.cargo.processed){
    for(const [key,n]of Object.entries(trip.cargo.selection)){if((s.farm[key]??0)<n)throw Error('带货库存不一致');s.farm[key]-=n;}
    trip.cargo.processed=true;trip.cargo.outcome='exchanged';
    if(s.expansion.facts)reduceFacts(s,[{kind:'cargoExchange',cardId:trip.cargo.cardId,quantity:trip.cargo.quantity,tripId:trip.id}]);
  }
  let guide=false;
  if(ticket.guide&&!s.expansion.regions.guideFlags.includes('GUIDE-B')){s.expansion.regions.guideFlags.push('GUIDE-B');nextRegionalFact(s);guide=true;}
  ticket.result={cardId:outcome.cardId,methodId,...(ticket.guide?{guide}:{})};ticket.processed=true;
  return ticket.result;
}

// Root progress-save calls this after validating the unchanged legacy trip
// fields. It deliberately accepts settled/claimed baskets shorter than the
// original slot count; the intro allocation remains an immutable ticket.
export function validateRegionalTrip(trip,fail){
  const object=(v,label)=>{if(!v||typeof v!=='object'||Array.isArray(v))fail(label);};
  const integer=(v,label,max=Number.MAX_SAFE_INTEGER)=>{if(!Number.isSafeInteger(v)||v<0||v>max)fail(label);};
  const list=(v,label,max,allowed)=>{if(!Array.isArray(v)||v.length>max||new Set(v).size!==v.length||v.some(x=>!allowed(x)))fail(label);};
  const ticket=trip.regional;object(ticket,'地区寻访票据');
  if(ticket.rulesVersion!==1||ticket.rngAlgorithm!==RNG_ALGORITHM||!REGIONAL.regions.some(r=>r.id===ticket.regionId))fail('地区票据版本');
  const area=REGIONAL.regions.find(r=>r.id===ticket.regionId);
  if(trip.routeId!==area.route||!area.places.some(p=>p.id===ticket.placeId)||!FOCUSES.includes(ticket.focus))fail('地区路线地点');
  integer(ticket.failedBefore,'地区失败前值',3);
  if(ticket.intro!==null){object(ticket.intro,'首标本票据');const card=regionalCard(ticket.intro.cardId);if(!card||card.region!==ticket.regionId||card.type!=='specimen'||card.material!==ticket.intro.materialId||ticket.intro.baseSlot!==0)fail('首标本槽位');}
  if(!Array.isArray(ticket.candidates)||ticket.candidates.length>24)fail('地区候选');
  const cardIds=[];
  for(const c of ticket.candidates){object(c,'地区机会票据');const card=regionalCard(c.cardId);if(!card||card.region!==ticket.regionId||cardIds.includes(c.cardId))fail('地区候选身份');cardIds.push(c.cardId);if(!Number.isFinite(c.chance)||c.chance<0||c.chance>.6||!Number.isFinite(c.roll)||c.roll<0||c.roll>=1)fail('地区概率票据');}
  if(ticket.intro&&!cardIds.includes(ticket.intro.cardId))fail('首标本未列候选');
  // the tracked partner's find (optional, loop batch 4): one of the candidates, first after the intro
  if(ticket.sure!==undefined&&(!cardIds.includes(ticket.sure)||ticket.intro||ticket.candidates[0]?.cardId!==ticket.sure))fail('追踪发现票据');
  object(ticket.method,'地方方法票据');
  list(ticket.method.eligibleIds,'地方方法候补',48,id=>REGIONAL.recipes.some(r=>r.id===id&&resolveSpecies(r.key).region===ticket.regionId));
  if(ticket.method.targetId!==null&&!ticket.method.eligibleIds.includes(ticket.method.targetId))fail('地方方法目标');
  if((ticket.method.targetId===null)!==(ticket.method.eligibleIds.length===0)||ticket.method.targetId!==null&&ticket.method.eligibleIds[0]!==ticket.method.targetId)fail('地方方法顺序');
  integer(ticket.method.countBefore,'地方方法前值',2);
  if(!Array.isArray(ticket.companions)||ticket.companions.length!==trip.members.length)fail('地区队员快照');
  for(let i=0;i<ticket.companions.length;i++){const c=ticket.companions[i];object(c,'地区队员');if(c.key!==trip.members[i])fail('地区队员身份');integer(c.G,'采集能力',20);integer(c.F,'发现能力',20);if(!['yard','water','wood'].includes(c.environment))fail('地区能力');list(c.traits,'地区队员特征',20,t=>typeof t==='string'&&t.length>0&&t.length<40);}
  if(ticket.companions.reduce((n,c)=>n+c.G,0)!==trip.snapshot.G||ticket.companions.reduce((n,c)=>n+c.F,0)!==trip.snapshot.F)fail('地区能力总额');
  if(typeof ticket.processed!=='boolean')fail('地区处理标志');
  if(ticket.guide!==undefined&&(ticket.guide!==true||ticket.regionId!==REGIONAL.guide.sourceRegion||ticket.placeId!==REGIONAL.guide.sourcePlaceId))fail('沿湾路标票据');
  if(ticket.processed&&ticket.guide&&typeof ticket.result.guide!=='boolean')fail('沿湾路标结果');
  validateTripCargo(trip,fail);
  if(['returned','settled'].includes(trip.status)&&!ticket.processed)fail('归队地区未处理');
  if(!ticket.processed&&ticket.result!==null)fail('地区未处理结果');
  if(ticket.processed){object(ticket.result,'地区结果');if(ticket.result.cardId!==null&&!cardIds.includes(ticket.result.cardId))fail('地区结果卡');if(ticket.result.methodId!==null&&!ticket.method.eligibleIds.includes(ticket.result.methodId))fail('地区结果方法');if(trip.status==='running'||trip.status==='recalled')fail('地区提前结算');}
  if(trip.status==='running'&&ticket.intro&&trip.remaining[0]!==ticket.intro.materialId)fail('首标本材料槽位');
  if(ticket.sampling!==undefined&&ticket.sampling!==null){
    const sampling=ticket.sampling;object(sampling,'地区采样票据');
    list(sampling.eligibleIds,'地区采样候选',2,id=>area.materials.includes(id));
    if(!sampling.eligibleIds.length||!sampling.eligibleIds.includes(sampling.materialId)||typeof sampling.directed!=='boolean')fail('地区采样材料');
    integer(sampling.baseSlot,'地区采样槽位',2);
    if(sampling.baseSlot>=(trip.snapshot.baseUnits??trip.remaining.length)||ticket.intro&&sampling.baseSlot===ticket.intro.baseSlot||trip.cargo&&sampling.baseSlot===trip.cargo.baseSlot)fail('地区材料槽位冲突');
    if(trip.status==='running'&&trip.remaining[sampling.baseSlot]!==sampling.materialId)fail('地区采样材料槽位');
    if(sampling.directed&&!(trip.snapshot.skills['TRIP-2']||trip.snapshot.skills['TRIP-S']))fail('地区采样定向手艺');
  }
}

// A legacy (version 1) trip that really completes after the upgrade records the
// same bounded completion fact from its saved members; nothing is back-filled.
const ROUTE_REGION={yard:'V',water:'R',wood:'T'};
export function recordLegacyTripFact(s,trip){
  if(trip?.version!==1||!s.expansion?.facts||trip.status!=='returned')return;
  const region=ROUTE_REGION[trip.routeId];if(!region)return;
  reduceFacts(s,[{kind:'tripComplete',tripId:trip.id,region,placeId:null,focus:null,cardId:null,members:trip.members.map(key=>{const c=regionalCompanion(key);return {key,gather:c.G,discover:c.F,environment:c.environment,traits:c.traits};})}]);
}

// GUIDE-B: kitchen Lv.3, 40 discoveries and one earlier region whose specimen
// first specimen was identified and whose new dish was actually collected; then a complete
// river trip from the stall place. A route state, never a 25th card.
export function guideEligibility(s){
  const missing=[];
  if(s.kitchenLevel<2)missing.push('厨房 Lv.3');
  if(discoveryCount(s)<40)missing.push(`发现 ${discoveryCount(s)}/40 种`);
  const earlier=['V','R','T'].some(regionId=>{
    const materials=REGIONAL.materials.filter(m=>m.region===regionId),recorded=materials.filter(m=>hasRegionalCard(s,m.specimen));
    const cardFacts=s.expansion.regions.history?.cardFacts??{};
    // The intro can choose the second material when only that recipe is ready.
    // Missing old snapshots cannot establish which specimen was the first;
    // identifying both satisfies the original condition without inventing history.
    const completeHistory=recorded.length>0&&recorded.every(m=>cardFacts[m.specimen]);
    const first=completeHistory?[...recorded].sort((a,b)=>cardFacts[a.specimen].seq-cardFacts[b.specimen].seq)[0]:null;
    const firstIdentified=first?materialIdentified(s,first.id):materials.every(m=>materialIdentified(s,m.id));
    return firstIdentified&&REGIONAL.species.some(c=>c.region===regionId&&((s.total?.[c.key]??0)>0));
  });
  if(!earlier)missing.push('在谷地、溪岸或茶坡任一处，辨认首趟带回的标本并收取过一款当地新品');
  return {met:!missing.length,missing};
}
function guideOption(s,regionId,placeId){
  if(s.expansion.regions.guideFlags.includes('GUIDE-B'))return {available:false,reason:'海湾路线已经开放。',done:true};
  if(regionId!==REGIONAL.guide.sourceRegion||placeId!==REGIONAL.guide.sourcePlaceId)return {available:false,reason:'沿湾路标只在溪岸的岸边摊出现。'};
  const gate=guideEligibility(s);return gate.met?{available:true,reason:''}:{available:false,reason:gate.missing.join('；'),missing:gate.missing};
}
// Optional cargo (B-E1): six extra free home birds, never the companions
// themselves; the exchange replaces one base material slot with salt flower.
function cargoOption(s,{regionId,placeId,members,selection,overrideKeepOne}){
  const card=REGIONAL.cards.find(c=>c.exchange&&c.region===regionId&&c.placeId===placeId);
  if(!card)return {available:false,missing:['这处地点没有沿途交换约定。']};
  const x=card.exchange,missing=[];
  if(!regionalCardGateMet(s,card))missing.push('先辨认盐花，才能约定带货交换。');
  // Owning the event removes the need to discover it again, never its team
  // requirements. Reuse the card predicate so one member can fulfill both.
  if(!teamMeets(card,members.map(regionalCompanion)))missing.push('带货交换需要便携伙伴与水边伙伴同行，可由同一位满足。');
  const info={available:!missing.length,cardId:card.id,quantity:x.quantity,rewardMaterial:x.rewardMaterial,allowed:[...x.allowed],missing};
  if(selection===null||selection===undefined)return info;
  if(!selection||typeof selection!=='object'||Array.isArray(selection))throw Error('带货选择无效');
  let total=0;
  for(const [key,n]of Object.entries(selection)){
    if(!x.allowed.includes(key))missing.push('带货只接受约定的家常出品。');
    if(!Number.isSafeInteger(n)||n<1)throw Error('带货数量无效');total+=n;
    const going=members.filter(m=>m===key).length,extra=availableCount(s,key)-going;
    if(n>extra)missing.push('带货数量超过了家里可用的只数（同行的伙伴不能兼带货）。');
    else if(!overrideKeepOne&&homeCount(s,key)-n-going<lockedCount(s,key))missing.push(`已锁定在家${lockedCount(s,key)}只，到仓库调整锁定数量后才能带出。`);
  }
  if(total!==x.quantity)missing.push(`带货要正好凑满${x.quantity}只；不带货就全部减到0。`);
  return {...info,missing:[...new Set(missing)]};
}
export function validateTripCargo(trip,fail){
  if(trip.cargo===undefined)return;
  const c=trip.cargo;if(!c||typeof c!=='object'||Array.isArray(c))fail('带货票据');
  const card=REGIONAL.cards.find(x=>x.id===c.cardId&&x.exchange);
  if(trip.version!==2||!card||trip.regional.regionId!==card.region||trip.regional.placeId!==card.placeId)fail('带货约定身份');
  let total=0;for(const [key,n]of Object.entries(c.selection??{})){if(!card.exchange.allowed.includes(key)||!Number.isSafeInteger(n)||n<1)fail('带货选择');total+=n;}
  if(total!==card.exchange.quantity||c.quantity!==card.exchange.quantity||c.rewardMaterial!==card.exchange.rewardMaterial||typeof c.processed!=='boolean')fail('带货数量');
  if(!Number.isSafeInteger(c.baseSlot)||c.baseSlot!==(trip.regional.intro?1:0))fail('带货槽位');
  if(trip.regional.sampling&&trip.regional.sampling.baseSlot!==c.baseSlot+1)fail('带货与采样槽位');
  if(trip.status==='running'&&(c.processed||c.outcome!==null||trip.remaining[c.baseSlot]!==c.rewardMaterial))fail('在途带货');
  if(trip.status==='recalled'&&(!c.processed||c.outcome!=='released'))fail('召回带货释放');
  if(['returned','settled'].includes(trip.status)&&(!c.processed||c.outcome!=='exchanged'))fail('归队带货交换');
}
