// Work J bindings: project gates, the twelve stage checks (with their bounded
// delivery and fixed cost) and the PJ-4 portrait display rule. Each names the
// domain function enforcing it; nothing interprets Chinese text at runtime.
const bind=(module,entry,args=[])=>({module,export:entry,args});
const rule=(kind,fields={})=>({kind,...fields});

function projectDefinition(id,field,raw){
  if(/^PJ-[1-4]$/.test(id)){
    if(field==='gate')return {rule:rule('project-gate',{project:id,retroactive:true}),runtime:bind('web/projects.js','projectInfo',[id])};
    if(field==='optionalDisplay.rule')return {rule:rule('portrait-display',{consume:false,countsAsFood:false,maximum:12,homeStockMode:false}),runtime:bind('web/projects.js','setProjectPortraits')};
  }
  const m=/^(PJ-[1-4])-([ABC])$/.exec(id);
  if(m&&field==='check'){
    const [,project]=m,stage=raw?.projects?.find(p=>p.id===project)?.stages?.find(s=>s.id===id);
    const consume=stage?.consume?(stage.consume.distinct?{kind:'choose',distinct:stage.consume.distinct,quantityEach:stage.consume.quantityEach,lockOnFirstDelivery:true}:{kind:'total',total:stage.consume.total,minimumSignatureCategories:stage.consume.minimumSignatureCategories}):null;
    return {rule:rule('project-stage',{stage:id,previousStage:!id.endsWith('A'),costCP:stage?.costCP??0,consume,basePayment:false,retroactiveChecks:true}),runtime:bind('web/projects.js','completeProjectStage',[project,id])};
  }
  return null;
}

export function compileProjectCondition(input,raw){
  const [id,...rest]=input.id.split(':'),field=rest.join(':');
  const d=projectDefinition(id,field,raw);
  return d?{id:input.id,kind:'compiled',work:'J',sourcePointer:input.sourcePointer,rule:d.rule,runtime:d.runtime}:null;
}
