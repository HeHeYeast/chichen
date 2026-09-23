// Explicit bindings for collections, specials, mementos, order terms and the
// package-wide production rules. Each descriptor names the domain function that
// enforces it; nothing here interprets Chinese text at runtime.
const bind=(module,entry,args=[])=>({module,export:entry,args});
const rule=(kind,fields={})=>({kind,...fields});

// Regular-stage mementos (M09–M12) unlock with Work I; their pure-display
// effect is the same contract as M01–M08 and is compiled now.
const REGULAR_MEMENTOS=new Set(['M09','M10','M11','M12']);

function collectionDefinition(id,field){
  if(/^COL-[1-8]$/.test(id)){
    if(field==='stageRules')return {work:'H',rule:rule('theme-stages',{representativeAnyOf:true,countsFromOptional:true,representativeIncludedInCount:true,chapterMinimums:id==='COL-8'?[2,3]:null,guestsCounted:false}),runtime:bind('web/collection-progress.js','collectionProgress',[id])};
    if(field==='practice.any')return {work:'H',rule:rule('any-witness',{witnesses:['menu:validService',`${id}:practice.trip`],business:'same-session',trip:'event-card-with-page-member'}),runtime:bind('web/collection-progress.js','collectionProgress',[id])};
    if(field==='practice.fullMenuStamp')return {work:'H',rule:rule('witness',{witness:'menu:completeService',grants:`FULLSTAMP-${id}`,secondItem:false}),runtime:bind('web/collection-progress.js','reconcileEntitlements',[])};
  }
  if(/^COL-[VRTB]$/.test(id)&&field==='practice.any')return {work:'H',rule:rule('any-witness',{witnesses:['practice.business','practice.order','practice.trip'],minimumSold:1,newAndOldDistinctMembers:true}),runtime:bind('web/collection-progress.js','collectionProgress',[id])};
  if(/^COL-[VRTB]-[ABC]$/.test(id)&&field==='requires'){
    const stage=id.at(-1);
    return {work:'H',rule:rule('region-stage',{stage,identified:stage==='A'?'any':'all',collected:stage==='A'?3:stage==='B'?6:12,oldPartner:stage==='A',loreCard:stage==='B',allCards:stage==='C'}),runtime:bind('web/collection-progress.js','collectionProgress',[id.slice(0,5)])};
  }
  if(/^SP-(ALL|LEAF|TABLE|SHAPE)$/.test(id)){
    const binding=bind('web/collection-progress.js',field==='reward'?'reconcileEntitlements':'collectionProgress',field==='reward'?[]:[id]);
    return {work:'H',rule:rule(`special-${field}`,{special:id}),runtime:binding};
  }
  if(/^M(0[1-9]|1[0-2])$/.test(id)){
    if(field==='effect')return {work:'H',rule:rule('display-only',{value:0,durability:false,sale:false,exchange:false,slots:3}),runtime:bind('web/collection-progress.js','setDisplay',[])};
    if(field==='unlock'&&!REGULAR_MEMENTOS.has(id))return {work:'H',rule:rule('collection-stage-2',{memento:id,practiceRequired:false}),runtime:bind('web/collection-progress.js','reconcileEntitlements',[])};
  }
  return null;
}

// Orders opened in E/F/G: qualification, payment and the O10 chapter rule.
function orderDefinition(id,field){
  if(!/^O(0[1-9]|1[0-2])$/.test(id))return null;
  const work=id==='O11'?'G':['O01','O04'].includes(id)?'E':'F';
  if(field==='qualification')return {work,rule:rule('producible-known-solution',{perGroup:true,distinctMinimum:true,displayUsesDiscovery:id==='O04',stockNotRequired:true}),runtime:bind('web/orders.js','orderOptions',[id])};
  if(field==='payment')return {work,rule:id==='O04'?rule('display-no-consume',{homeFreeEach:1,cp:0,note:'NOTE-O04'}):rule('base-per-delivery-bonus-once',{signature:false,theme:false,basket:false}),runtime:bind('web/orders.js',id==='O04'?'displayOrder':'deliverOrderGroups',[])};
  if(field==='seasonRule'&&id==='O10')return {work,rule:rule('two-chapters-six-each',{frozenAtAccept:true}),runtime:bind('web/orders.js','acceptProposal',[])};
  return null;
}

// Package-wide production rules: one binding per rule to the module enforcing it.
const PRODUCTION={
  knowledge:['C','web/region-view.js','methodView'],method:['C','web/regional-methods.js','regionalRecipeInfo'],trial:['C','web/batch-plan.js','buildBatchPlan'],
  companions:['C','web/legacy-recipe-adapter.js','sampleLegacyCompanions'],prices:['D','web/business.js','prepareBusiness'],timing:['D','web/timeline.js','advanceTimeline'],
  unlock:['G','web/regional-exploration.js','regionalTripInfo'],
  // Five fixed places 厨房/农场/生意/寻访/图鉴 (Work K).
  ui:['K','web/theme.js','navEntries'],
};

export function compileCollectionCondition(input){
  const [id,...rest]=input.id.split(':'),field=rest.join(':');
  let d=null;
  if(id==='productionRules'&&PRODUCTION[field]){const [work,module,entry]=PRODUCTION[field];d={work,rule:rule(`production-${field}`),runtime:bind(module,entry)};}
  else d=collectionDefinition(id,field)??orderDefinition(id,field);
  return d?{id:input.id,kind:'compiled',work:d.work,sourcePointer:input.sourcePointer,rule:d.rule,runtime:d.runtime}:null;
}
