// Explicit regional bindings. The author input's hash is checked by the parent
// compiler before these descriptors are selected. Never inspect Chinese text.
// Rows are keyed by stable identity; each Work adds its region's rows only.
// Two shapes exist and both are fail-closed:
//  - {kind:'compiled',rule,runtime}: a declarative contract enforced by the
//    named domain function (trip, recipe, identification, batch plan).
//  - requirement trees (all/any/identified/...) evaluated by web/requirements.js.
const bind=(module,entry,args)=>({module,export:entry,args});
const all=(...requirements)=>({kind:'all',requirements});
const condition=(kind,fields)=>({kind,...fields});

// Work progression of the regional line. A region is compiled only in its Work.
export const REGIONAL_WORKS=Object.freeze({V:'C',R:'F',T:'F',B:'G'});
export const COMPILED_REGIONS=Object.freeze(['V','R','T','B']);

// Card gates, transcribed once per card identity. `entry` means the card's
// material entry recipe must be executable (tool, kitchen, duck, old supply),
// which the specimen text states for every specimen card. Lore/event cards use
// explicit identified-material leaves. Team traits/environments stay in card.team.
export const CARD_GATES=Object.freeze({
  'V-S1':{entry:true},'V-S2':{entry:true},
  'V-N1':{identifiedAll:[]},'V-N2':{identifiedAll:[76]},
  'V-E1':{identifiedAll:[]},'V-E2':{identifiedAll:[76]},
  // Work F. R-N1 "本区任一材料" is an any-of; the rest name one material or none.
  'R-S1':{entry:true},'R-S2':{entry:true},
  'R-N1':{identifiedAny:[77,78]},'R-N2':{identifiedAll:[]},
  'R-E1':{identifiedAll:[77]},'R-E2':{identifiedAll:[]},
  'T-S1':{entry:true},'T-S2':{entry:true},
  'T-N1':{identifiedAll:[79]},'T-N2':{identifiedAll:[80]},
  'T-E1':{identifiedAll:[79]},'T-E2':{identifiedAll:[80]},
  // Work G. B-N2 accepts either bay material.
  'B-S1':{entry:true},'B-S2':{entry:true},
  'B-N1':{identifiedAll:[81]},'B-N2':{identifiedAny:[81,82]},
  'B-E1':{identifiedAll:[81]},'B-E2':{identifiedAll:[82]},
});

const identifiedTree=({identifiedAll=[],identifiedAny=null})=>({kind:'all',children:[
  ...identifiedAll.map(id=>({kind:'identified',id,label:`已辨认材料${id}`})),
  ...(identifiedAny?[{kind:'any',children:identifiedAny.map(id=>({kind:'identified',id,label:`已辨认材料${id}`}))}]:[]),
]});
const regionGate=({collected,discoveries,kitchenLevel})=>all(condition('collected-at-least',{count:collected}),condition('discovered-at-least',{count:discoveries}),condition('kitchen-at-least',{level:kitchenLevel}));
const entryGate=(regionId,recipe)=>all(condition('region-eligible',{regionId}),condition('duck-license',{required:recipe.egg===1}),condition('tool-at-least',{toolId:recipe.toolId,level:recipe.toolLevel}),condition('kitchen-at-least',{level:recipe.kitchenLevel}),condition('old-supply-all',{recipeId:recipe.id,materialIds:recipe.ingredients.filter(i=>i.id<75).map(i=>i.id)}));
const cardProtection=(regionId,card)=>condition('regional-card-ticket',{regionId,focus:card.focus,hardAttempt:4,basePercent:card.discoveryChance.basePercent,perTeamFPercent:card.discoveryChance.perTeamFPercent,capPercent:card.discoveryChance.capPercent,optionalBonusPercent:card.discoveryChance.bonusCondition?card.discoveryChance.bonusPercent:0,emptyPool:'freeze',recall:'freeze',legacyProtection:'independent'});

// Builds the per-identity definitions for the compiled regions from runtime
// projections (never from natural language). `runtime` is the projected table.
const memo=new WeakMap();
export function regionalDefinitions(runtime){
  if(memo.has(runtime))return memo.get(runtime);
  const definitions={},derived={};
  const recipeOf=key=>runtime.recipes.find(r=>r.key===key);
  for(const regionId of COMPILED_REGIONS){
    const work=REGIONAL_WORKS[regionId],region=runtime.regions.find(r=>r.id===regionId);
    definitions[`${regionId}:gate`]={work,rule:regionGate(region),runtime:bind('web/region-model.js','regionInfo',[regionId])};
    for(const species of runtime.species.filter(s=>s.region===regionId)){
      const recipe=recipeOf(species.key);
      definitions[`${species.id}:unlock.oldSupplyRules`]={work,rule:condition('old-supply-all',{recipeId:recipe.id,materialIds:recipe.ingredients.filter(i=>i.id<75).map(i=>i.id)}),runtime:bind('web/regional-methods.js','regionalRecipeInfo',[recipe.id,{entry:true}])};
      definitions[`${species.id}:unlock.technologyNote`]={work,rule:entryGate(regionId,recipe),runtime:bind('web/regional-methods.js','regionalRecipeInfo',[recipe.id,{entry:true}])};
      if(!species.edible)definitions[`${species.id}:businessExclusion`]={work,rule:condition('inedible-excluded',{key:species.key,menuRoles:false,purchaseGroups:false,displayMenusScore:false}),runtime:bind('web/menu-model.js','menuDefinition',[])};
      derived[`${recipe.id}:gate`]={work,sourcePointer:`/species/${runtime.species.indexOf(species)}/recipe`,rule:all(entryGate(regionId,recipe),...species.unlock.identifiedMaterials.map(materialId=>condition('material-identified',{materialId})),...(species.unlock.firstSpecimenForOldOnly?[condition('intro-specimen-done',{regionId})]:[]),...(species.unlock.card?[condition('card-owned',{cardId:species.unlock.card})]:[]),condition('full-regional-method',{recipeId:recipe.id})),runtime:bind('web/regional-methods.js','regionalRecipeInfo',[recipe.id])};
    }
    for(const material of runtime.materials.filter(m=>m.region===regionId)){
      const recipe=recipeOf(material.entrySpecies),directions=runtime.recipes.filter(r=>runtime.species.find(s=>s.key===r.key).unlock.identifiedMaterials.includes(material.id)).map(r=>r.id);
      definitions[`${material.id}:identification`]={work,rule:condition('identify-specimen',{materialId:material.id,cardId:material.specimen,costCP:0,grantedMaterialQuantity:0,supplyPermanent:true,directionIds:directions,introUsesBaseSlot:true}),runtime:bind('web/regional-methods.js','identifyMaterial',[material.id])};
      definitions[`${material.id}:entryGate`]={work,rule:entryGate(regionId,recipe),runtime:bind('web/regional-methods.js','regionalRecipeInfo',[recipe.id,{entry:true}])};
      derived[`${material.id}:supply`]={work,sourcePointer:`/materials/${runtime.materials.indexOf(material)}/specimen`,rule:condition('material-identified',{materialId:material.id}),runtime:bind('web/region-model.js','materialIdentified',[material.id])};
    }
    for(const card of runtime.cards.filter(c=>c.region===regionId)){
      const gate=CARD_GATES[card.id];
      if(!gate)throw Error(`Missing card gate ${card.id}`);
      if(!!gate.entry!==(card.type==='specimen'))throw Error(`Specimen gate shape ${card.id}`);
      if(gate.entry){const material=runtime.materials.find(m=>m.id===card.material);definitions[`${card.id}:gate`]={work,rule:entryGate(regionId,recipeOf(material.entrySpecies)),runtime:bind('web/regional-exploration.js','regionalTripInfo',[{regionId,placeId:card.placeId,focus:card.focus}])};}
      else definitions[`${card.id}:gate`]={work,requirement:identifiedTree(gate)};
      definitions[`${card.id}:protection`]={work,rule:cardProtection(regionId,card),runtime:bind('web/regional-exploration.js','regionalCardOutcome',[])};
    }
    if(regionId===runtime.guide.sourceRegion||regionId==='B'){
      // GUIDE-B is a route state earned by a complete river trip; it is
      // compiled with the bay (its only consumer), never counted as a card.
      if(regionId==='B'){
        definitions['GUIDE-B:gate']={work,rule:all(condition('kitchen-at-least',{level:2}),condition('discovered-at-least',{count:40}),condition('earlier-region-identified-and-collected',{regions:['V','R','T']})),runtime:bind('web/regional-exploration.js','guideEligibility',[])};
        definitions['GUIDE-B:team']={work,rule:condition('any-team',{min:1,max:3,traitsRequired:false}),runtime:bind('web/regional-exploration.js','regionalTripInfo',[{regionId:runtime.guide.sourceRegion,placeId:runtime.guide.sourcePlaceId,guide:true}])};
        definitions['GUIDE-B:trigger']={work,rule:condition('complete-trip-flag',{regionId:runtime.guide.sourceRegion,placeId:runtime.guide.sourcePlaceId,deterministic:true,recall:'nothing'}),runtime:bind('web/regional-exploration.js','settleRegionalTrip',[])};
        definitions['GUIDE-B:effect']={work,rule:condition('route-state',{flag:'GUIDE-B',route:'bay',countAsDiscoveryCard:false,usesCardSlot:false}),runtime:bind('web/exploration.js','explorationInfo',['bay'])};
      }
    }
    for(const alternative of runtime.alternatives.filter(a=>a.region===regionId)){
      const regionalIngredients=alternative.ingredients.filter(id=>id>=75);
      if(regionalIngredients.length!==alternative.replaces.length)throw Error(`Alternative replacement arity ${alternative.id}`);
      definitions[`${alternative.id}:guarantee`]={work,rule:condition('local-alternative-original-probability',{target:alternative.target,replacements:Object.fromEntries(regionalIngredients.map((id,i)=>[id,alternative.replaces[i]])),insertTarget:false,forbid:['regional-target','seasonal','dim-sum-guarantee','gift']}),runtime:bind('web/batch-plan.js','buildBatchPlan',[])};
    }
  }
  const result={definitions,derived};memo.set(runtime,result);return result;
}

export function compileRegionalCondition(input,runtime){
  const definition=regionalDefinitions(runtime).definitions[input.id];
  if(!definition)return null;
  const {work,requirement,...rest}=structuredClone(definition);
  // Evaluable trees keep the requirement shape consumed by requirements.js.
  return requirement?{id:input.id,...requirement,compiled:true,work,sourcePointer:input.sourcePointer}:{id:input.id,kind:'compiled',work,sourcePointer:input.sourcePointer,...rest};
}

// These rules have structured author inputs rather than a natural-language
// CONDITION_FIELDS entry. Add to generated requirements with separate coverage.
export function regionalDerivedRules(runtime){
  return Object.fromEntries(Object.entries(regionalDefinitions(runtime).derived).map(([id,{work,sourcePointer,rule,runtime:binding}])=>[id,{id,kind:'compiled',work,sourcePointer,rule,runtime:binding}]));
}
