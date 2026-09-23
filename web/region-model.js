import {REGIONAL,CONTENT_TEXT,resolveSpecies,SPECIES_ABILITIES} from './content-registry.js';
import {collectedTotal,discoveryCount} from './progression.js';

// Release gates are per region, never an alternative content definition.
// Later regional Works append a region ID; identities are never reassigned.
// Project-unlocked alternatives (ALT-R) open only with their project Work.
export const RELEASED_REGIONS=Object.freeze(['V','R','T','B']);
export const RELEASED_PROJECT_ALTERNATIVES=Object.freeze(['ALT-R']);
// A project-unlocked alternative is learned when that project's last stage is complete.
export const projectFinished=(s,id)=>{const p=REGIONAL.projects.find(x=>x.id===id);return !!p&&s.expansion?.projects?.[id]?.stages?.[p.stages.at(-1).id]?.complete===true;};
export const alternativeUnlocked=(s,alt)=>alt.unlock.startsWith('PJ-')?projectFinished(s,alt.unlock):hasRegionalCard(s,alt.unlock);
const releasedRegion=id=>RELEASED_REGIONS.includes(id);
export const REGIONAL_RELEASE=Object.freeze({
  regions:RELEASED_REGIONS,
  cards:Object.freeze(REGIONAL.cards.filter(c=>releasedRegion(c.region)).map(c=>c.id)),
  materials:Object.freeze(REGIONAL.materials.filter(m=>releasedRegion(m.region)).map(m=>m.id)),
  methods:Object.freeze(REGIONAL.recipes.filter(r=>releasedRegion(resolveSpecies(r.key).region)).map(r=>r.id)),
  alternatives:Object.freeze(REGIONAL.alternatives.filter(a=>releasedRegion(a.region)&&(REGIONAL.cards.some(c=>c.id===a.unlock)||RELEASED_PROJECT_ALTERNATIVES.includes(a.id))).map(a=>a.id)),
});
export const regionalCard=id=>REGIONAL.cards.find(c=>c.id===id)??null;
export const regionalMaterial=id=>REGIONAL.materials.find(m=>m.id===id)??null;
export const hasRegionalCard=(s,id)=>Object.hasOwn(s.expansion?.discovery?.cards??{},id);
export const materialIdentified=(s,id)=>Object.hasOwn(s.expansion?.discovery?.identified??{},id);
export const nextRegionalFact=s=>++s.meta.factSeq;

export function regionInfo(s,id){
  const region=REGIONAL.regions.find(r=>r.id===id);
  if(!region)throw Error('没有找到这处地区。');
  const missing=[];
  if(!REGIONAL_RELEASE.regions.includes(id))missing.push('这处地区尚未开放。');
  if(collectedTotal(s)<region.collected)missing.push(`累计收取 ${collectedTotal(s)}/${region.collected} 只`);
  if(discoveryCount(s)<region.discoveries)missing.push(`发现 ${discoveryCount(s)}/${region.discoveries} 种`);
  if(s.kitchenLevel<region.kitchenLevel)missing.push(`厨房 Lv.${region.kitchenLevel+1}`);
  // The bay route itself exists only after the deterministic guide (GUIDE-B).
  if(region.route==='bay'&&!s.expansion?.regions?.guideFlags?.includes('GUIDE-B'))missing.push('先在溪岸岸边摊追寻沿湾路标');
  return {region,name:CONTENT_TEXT[id]?.name??id,met:missing.length===0,canEnter:missing.length===0,missing,places:region.places,opened:s.expansion.regions.opened.includes(id),requirementId:region.gateRequirement};
}

export function regionalCompanion(key){
  const species=resolveSpecies(key),ability=SPECIES_ABILITIES[key];
  if(!species||!ability)throw Error('同行伙伴身份无效。');
  return {key,G:ability.gather,F:ability.discover,environment:ability.environment,traits:[...(species.exploration?.traits??species.traits??[])]};
}

// Querying never creates state or consumes a random roll. Specimen gates are
// the material entry recipe (domain), other gates are compiled requirement
// trees evaluated by the caller; unknown/uncompiled gates fail closed.
export function regionCardCandidates(s,{regionId,placeId,focus,specimenEligibility={},companions=[],gateMet=()=>false}){
  return REGIONAL.cards.filter(c=>{
    if(!REGIONAL_RELEASE.cards.includes(c.id)||c.region!==regionId||c.placeId!==placeId||c.focus!==focus||hasRegionalCard(s,c.id))return false;
    if(c.type==='specimen'?!specimenEligibility[c.material]:!gateMet(c))return false;
    return teamMeets(c,companions);
  });
}
// One member may satisfy both the trait and environment predicates.
export function teamMeets(card,companions){
  if(card.team.trait&&!companions.some(member=>member.traits.includes(card.team.trait)))return false;
  if(card.team.environment&&!companions.some(member=>member.environment===card.team.environment))return false;
  return true;
}

export function regionalCardChance(card,companions){
  const cfg=card.discoveryChance;
  const condition=cfg.bonusCondition;
  if(condition&&Object.keys(condition).some(k=>!['trait','environment'].includes(k)))throw Error('该发现卡的特征条件尚未接入。');
  const bonus=!!condition&&(!condition.trait||companions.some(c=>c.traits?.includes(condition.trait)))&&(!condition.environment||companions.some(c=>c.environment===condition.environment));
  return Math.min(cfg.capPercent,cfg.basePercent+cfg.perTeamFPercent*companions.reduce((n,c)=>n+c.F,0)+(bonus?cfg.bonusPercent:0))/100;
}

export function recordRegionalTrip(s,trip,cardId=null){
  const r=trip.regional,region=REGIONAL.regions.find(x=>x.id===r.regionId);
  const seq=nextRegionalFact(s);
  const history=s.expansion.regions.history??={companionFirst:{},trips:{}};
  const companionFacts=history.companionFacts??={};
  for(const member of r.companions)if(!Object.hasOwn(history.companionFirst,member.key)){
    history.companionFirst[member.key]=seq;
    companionFacts[member.key]={seq,tripId:trip.id,region:r.regionId,gather:member.G,discover:member.F,environment:member.environment,traits:[...member.traits]};
  }
  if(cardId){
    const cardFacts=history.cardFacts??={};
    if(!Object.hasOwn(cardFacts,cardId))cardFacts[cardId]={seq:s.expansion.discovery.cards[cardId],tripId:trip.id,members:structuredClone(r.companions)};
  }
  const previous=history.trips[r.regionId]??{count:0,lastMembers:[],regionalWithLegacy:false,twoSeasonChapters:false};
  const species=r.companions.map(c=>resolveSpecies(c.key));
  const chapters=new Set(species.map(c=>c.chapter).filter(Boolean));
  history.trips[r.regionId]={count:previous.count+1,lastMembers:r.companions.map(c=>c.key),regionalWithLegacy:previous.regionalWithLegacy||(species.some(c=>c.region===region.id)&&species.some(c=>c.pack!=='regional')),twoSeasonChapters:previous.twoSeasonChapters||chapters.size>=2};
}
