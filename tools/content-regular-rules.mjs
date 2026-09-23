// Work I bindings: regular stages (gate, two branches, record policy), the
// delivery/completion contract, M09–M12 and the species "regular relation"
// edges, which are navigation only and never grant or unlock anything.
const bind=(module,entry,args=[])=>({module,export:entry,args});
const rule=(kind,fields={})=>({kind,...fields});
// RG2-4's O05 branch alone requires a new completion after activation.
const AFTER_ACTIVATION={'RG2-4':['O05']};

function regularDefinition(id,field){
  if(/^RG[1-4]$/.test(id)){
    if(field==='delivery')return {rule:rule('one-unread-read-advances',{maxPending:1,visitorEverySales:12,manualBranchesQueue:true,forcedBusiness:false}),runtime:bind('web/regulars.js','reconcileRegulars')};
    if(field==='completion')return {rule:rule('once-text-memento',{affection:false,deadline:false,dailyGreeting:false,cp:0}),runtime:bind('web/regulars.js','readRegularStage')};
  }
  const m=/^(RG[1-4])-([1-4])$/.exec(id);
  if(m){
    const [,regular,n]=m;
    if(field==='gate')return {rule:rule('stage-gate',{stage:id,previousRead:n!=='1'}),runtime:bind('web/regulars.js','regularInfo',[regular])};
    if(field==='alternatives')return {rule:rule('any-of-branches',{stage:id,branches:2,sameSessionWitness:['RG2-3','RG3-3'].includes(id)}),runtime:bind('web/regulars.js','regularInfo',[regular])};
    if(field==='recordPolicy')return {rule:rule('record-policy',{stage:id,retroactive:true,afterActivation:AFTER_ACTIVATION[id]??[],readConsumes:false}),runtime:bind('web/regulars.js','readRegularStage',[regular])};
  }
  if(/^M(09|1[0-2])$/.test(id)&&field==='unlock')return {rule:rule('regular-stage-4',{memento:id,once:true}),runtime:bind('web/regulars.js','reconcileRegulars')};
  if(field==='regularRelation')return {rule:rule('relation-navigation-only',{grants:false,unlocks:false}),runtime:bind('web/regulars.js','regularInfo')};
  return null;
}

export function compileRegularCondition(input){
  const [id,...rest]=input.id.split(':'),field=rest.join(':');
  const d=regularDefinition(id,field);
  return d?{id:input.id,kind:'compiled',work:'I',sourcePointer:input.sourcePointer,rule:d.rule,runtime:d.runtime}:null;
}
