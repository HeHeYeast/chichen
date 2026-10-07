import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath,pathToFileURL} from 'node:url';
import {LEGACY193,EXPANSION} from '../web/legacy-content.js';
import {SEASONAL_CHARACTERS} from '../web/seasonal-pack.js';
import {ORIGINAL_RECIPE_CATALOG} from '../web/recipe-catalog-data.js';
import {ABILITIES,DESCRIPTIONS} from '../web/integration-data.js';
import {TRADE_SPECIES} from '../web/trade-data.js';
import {INGREDIENT_UNLOCK_RULES} from '../web/ingredient-unlocks.js';
import {AUTHOR_SCHEMAS,compileConditions,CONTENT_REVISION,RUNTIME_FORMAT,RULES_VERSION,SIGNATURES} from './content-runtime-rules.mjs';
import {mergeProductionArt} from './production-art-manifest.mjs';

const ROOT=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
export const sha256=value=>crypto.createHash('sha256').update(value).digest('hex');
const clone=value=>structuredClone(value);
const assert=(test,message)=>{if(!test)throw Error(message);};
const same=(a,b,message)=>assert(JSON.stringify(a)===JSON.stringify(b),message);
const select=(object,keys)=>Object.fromEntries(keys.filter(k=>object[k]!==undefined).map(k=>[k,clone(object[k])]));
const safeInteger=(value,min,max,label)=>assert(Number.isSafeInteger(value)&&value>=min&&value<=max,`Invalid integer ${label}: ${value}`);
const percent=(value,label)=>{const n=value*100;safeInteger(n,0,100,label);return n;};
const unique=(rows,label,key='id')=>{const values=rows.map(x=>x[key]);assert(new Set(values).size===values.length,`Duplicate ${label}`);};

export function validateAuthorSchema(value,name) {
  const schema=AUTHOR_SCHEMAS[name];assert(schema,`Unknown author schema ${name}`);
  function visit(node,pointer,normalized) {
    const type=node===null?'null':Array.isArray(node)?'array':typeof node;
    const rule=schema[normalized];
    assert(rule,`Unknown author field ${name}${pointer}`);
    assert(rule.types.includes(type),`Invalid type at ${name}${pointer}: ${type}`);
    if(type==='number')assert(Number.isFinite(node),`Non-finite number at ${pointer}`);
    if(type==='array')node.forEach((n,i)=>visit(n,`${pointer}/${i}`,`${normalized}/*`));
    if(type==='object') {
      for(const key of Object.keys(node))assert(rule.allowed.includes(key),`Unknown author field ${name}${pointer}/${key}`);
      for(const key of rule.required??[])assert(Object.hasOwn(node,key),`Missing author field ${name}${pointer}/${key}`);
      for(const [key,v] of Object.entries(node))visit(v,`${pointer}/${key}`,`${normalized}/${key}`);
    }
  }
  visit(value,'','');return true;
}

export function validateLegacy(baseline) {
  const legacy=LEGACY193.characters.flatMap((list,egg)=>list.map(c=>({...c,key:`${egg}:${c.id}`,egg})));
  assert(legacy.length===193&&LEGACY193.characters[0].length===128&&LEGACY193.characters[1].length===65,'Frozen legacy identity boundary changed');
  assert(LEGACY193.tools[2].length===75,'Frozen legacy material boundary changed');
  same(baseline.species.map(s=>s.key),legacy.map(s=>s.key),'193 baseline identity/order drift');
  const recipes=[...ORIGINAL_RECIPE_CATALOG,
    ...EXPANSION.characters.map(c=>({egg:0,id:c.id,toolId:8,minLevel:c.minLevel,ingredients:[...c.ingredients],kind:'steamer'})),
    ...SEASONAL_CHARACTERS.map(c=>({egg:c.egg,id:c.id,toolId:c.toolId,minLevel:c.minLevel,ingredients:[...c.ingredients],kind:'seasonal',chapter:c.chapter})),
  ];
  for(const [i,s] of baseline.species.entries()) {
    const c=legacy[i],ability=ABILITIES[s.key];
    same([s.egg,s.name,s.basePrice,s.description,s.signature],[c.egg,c.title_zh_CN,c.cp_1,DESCRIPTIONS[s.key],TRADE_SPECIES[s.key].category],`Legacy attributes drift: ${s.key}`);
    same([s.exploration.G,s.exploration.F,s.exploration.environment],[ability.gather,ability.discover,ability.environment],`Legacy abilities drift: ${s.key}`);
    same(s.recipes,recipes.filter(r=>r.egg===s.egg&&r.id===c.id),`Legacy recipes drift: ${s.key}`);
    same(s.season,SEASONAL_CHARACTERS.find(r=>r.key===s.key)?.chapter??null,`Legacy season drift: ${s.key}`);
  }
  for(const [i,m] of baseline.ingredients.entries()) {
    const old=LEGACY193.tools[2][i];
    same([m.id,m.name,m.price],[old.id,old.title_zh_CN,old.buy_cp],`Legacy ingredient drift: ${i}`);
    same(m.unlockAlternatives,INGREDIENT_UNLOCK_RULES[i].alternatives,`Legacy ingredient supply drift: ${i}`);
    for(const group of m.unlockAlternatives)for(const rule of group) {
      assert(['tool','discovery','collected','kitchen'].includes(rule.kind),`Unknown legacy supply predicate ${rule.kind}`);
      if(rule.kind==='tool') {safeInteger(rule.id,0,8,'legacy tool');safeInteger(rule.level,0,2,'legacy internal level');}
    }
  }
  for(const [i,t] of baseline.tools.entries())same([t.id,t.name,t.cookCP],[LEGACY193.tools[1][i].id,LEGACY193.tools[1][i].title_zh_CN,LEGACY193.tools[1][i].lv_0_cook_cp],`Legacy tool drift: ${i}`);
  return legacy;
}

export function compileRuntimeContent(content,baseline,art) {
  validateAuthorSchema(content,'content');validateAuthorSchema(baseline,'baseline');validateAuthorSchema(art,'art-manifest');
  assert(content.version===1,'Unsupported author format');
  const legacy=validateLegacy(baseline);
  const expected={oldSpecies:193,newSpecies:48,chicken:24,duck:24,total:241,materials:8,cards:24,themeCollections:8,regionCollections:4,menus:8,orders:12,regulars:4,regularStages:16,projects:4,mementos:12};
  same(content.counts,expected,'Authored count declaration changed');
  for(const [name,count] of Object.entries({species:48,materials:8,regions:4,cards:24,alternatives:4,menus:8,orders:12,regulars:4,projects:4,collections:12,specials:4,mementos:12,paperRecords:24})) {
    assert(content[name].length===count,`Expected ${count} ${name}`);unique(content[name],name);
  }
  assert(content.regulars.reduce((n,r)=>n+r.stages.length,0)===16,'Expected 16 regular stages');
  assert(content.species.filter(s=>s.edible).length===40,'Expected 40 edible and 8 ornamental new species');
  assert(content.collections.filter(s=>s.kind==='theme').length===8&&content.collections.filter(s=>s.kind==='region').length===4,'Collection kind counts');
  unique(content.species,'species keys','key');
  const newById=new Map(content.species.map(s=>[s.id,s]));
  const oldKeys=new Set(legacy.map(s=>s.key));
  const keys=new Set([...oldKeys,...content.species.map(s=>s.key)]);
  assert(keys.size===241,'241 identity uniqueness');
  const speciesKey=ref=>{const key=newById.get(ref)?.key??ref;assert(keys.has(key),`Unknown species reference ${ref}`);return key;};
  const speciesList=refs=>refs.map(speciesKey);
  const tableSets=Object.fromEntries(['regions','cards','menus','orders','regulars','projects','collections','specials','mementos','alternatives','paperRecords'].map(t=>[t,new Set(content[t].map(x=>x.id))]));
  const reference=(table,id)=>assert(tableSets[table].has(id),`Unknown ${table} reference ${id}`);
  const materialIds=new Set([...baseline.ingredients,...content.materials].map(m=>m.id));
  assert(materialIds.size===83,'83 material identity uniqueness');
  same(content.materials.map(m=>m.id),[75,76,77,78,79,80,81,82],'Regional material identity order');
  const edibleKeys=new Set([...baseline.species,...content.species].filter(s=>s.edible).map(s=>s.key));
  for(const r of content.regions) {reference('regions',r.id);for(const m of r.materials)assert(materialIds.has(m),`Unknown region material ${m}`);speciesKey(r.entrySpecies);}
  for(const menu of content.menus) {
    for(const table of ['orders','regulars','projects'])for(const id of menu[table])reference(table,id);
    unique(menu.roles,`${menu.id} roles`);
    for(const role of menu.roles) {
      assert(role.allowed.length>0,`Empty menu role ${role.id}`);
      for(const key of speciesList(role.allowed))assert(edibleKeys.has(key),`Inedible menu role ${role.id}/${key}`);
    }
  }
  for(const order of content.orders) {
    for(const [field,table]of Object.entries({menu:'menus',card:'cards',regular:'regulars',project:'projects'}))if(order[field])reference(table,order[field]);
    unique(order.groups,`${order.id} groups`);unique(order.variants,`${order.id} variants`);
    reference('paperRecords',order.firstResult.id);
    safeInteger(order.bonusCP,0,10000,`${order.id} bonus`);
    for(const group of order.groups) {
      safeInteger(group.quantity,1,1000,`${group.id} quantity`);
      assert(group.allowed.length>0,`Empty order group ${group.id}`);
      assert(Object.hasOwn(content.selectors,group.selector)||(order.id==='O06'&&group.selector==='regionFood'),`Unknown selector ${group.selector}`);
      for(const key of speciesList(group.allowed))if(order.kind==='purchase')assert(edibleKeys.has(key),`Inedible purchase group ${group.id}/${key}`);
    }
    for(const variant of order.variants)if(variant.regions)for(const region of variant.regions)reference('regions',region);
  }
  for(const regular of content.regulars) {
    reference('regions',regular.region);reference('mementos',regular.memento);assert(regular.stages.length===4,`Four stages ${regular.id}`);unique(regular.stages,`${regular.id} stages`);
    for(const stage of regular.stages)assert(tableSets.paperRecords.has(stage.reward)||tableSets.mementos.has(stage.reward),`Unknown regular reward ${stage.reward}`);
  }
  for(const project of content.projects) {
    assert(project.stages.length===3,`Three project stages ${project.id}`);unique(project.stages,`${project.id} stages`);
    for(const stage of project.stages)safeInteger(stage.costCP,0,100000,`${stage.id} cost`);
  }
  for(const material of content.materials)assert(newById.get(material.entrySpecies)?.recipe.ingredients.some(i=>i.id===material.id),`Material entry recipe ${material.id}`);
  for(const [i,region] of ['V','R','T','B'].entries())for(const egg of [0,1])for(let n=1;n<=6;n++) {
    const s=newById.get(`${region}-${egg?'D':'C'}${n}`);
    assert(s&&s.key===`${egg}:${(egg?65:128)+i*6+n-1}`&&s.egg===egg&&s.region===region,`Frozen identity mapping changed: ${region}/${egg}/${n}`);
    assert(s.catalogLabel===`${egg?'D':'C'}${String(Number(s.key.split(':')[1])+1).padStart(3,'0')}`,`Catalog number ${s.id}`);
  }
  const text={};
  for(const table of ['regions','species','materials','alternatives','cards','menus','orders','regulars','projects','collections','specials','mementos','paperRecords'])for(const row of content[table]) {
    // This table is private presentation input. A visibility projection must redact it before UI use.
    text[row.stableId??row.id]=select(row,['name','title','description','clue','hint','text','next','result','request','finish','specimenName','recognition','lore','shop','gate','complete','unlock','validService','stageRules','theme','optional','practice','reward','rewards','businessExclusion','regularRelation']);
    if(row.stages)for(const stage of row.stages)text[stage.id]=select(stage,['title','text','next','gate','alternatives','check','requires','reward','recordPolicy']);
    if(row.variants)for(const variant of row.variants)text[variant.id]=select(variant,['label','text']);
  }
  text[content.guide.id]=select(content.guide,['hint','gate','team','trigger','result','effect']);
  text.labels={tags:clone(content.tags),traits:clone(content.traits)};

  const recipes=content.species.map(s=>{
    const r=s.recipe;
    assert(r.id===`REC-${s.id}`&&r.mode==='regional-trial','Regional recipe identity/mode');
    safeInteger(r.toolId,0,8,`${s.id} tool`);safeInteger(r.toolLevel,1,3,`${s.id} display tool level`);safeInteger(r.kitchenLevel,1,4,`${s.id} display kitchen level`);
    assert(!(r.toolId===8&&s.egg===1),`Steamer remains chicken-only: ${s.id}`);
    assert(r.exact&&!r.extraIngredientsAllowed&&r.firstChance===0.25&&r.hardAttempt===4&&r.repeatGuaranteed===1,`Trial contract changed: ${s.id}`);
    assert(r.ingredients.length>=1&&r.ingredients.length<=3,`Ingredient arity ${s.id}`);unique(r.ingredients,`${s.id} ingredients`);
    for(const ingredient of r.ingredients)assert(materialIds.has(ingredient.id)&&![68,69,70].includes(ingredient.id)&&ingredient.quantity===1,`Invalid ingredient ${s.id}/${ingredient.id}`);
    same(r.materialSupplyRequired,r.ingredients.map(m=>m.id),`Supply coverage ${s.id}`);
    same(r.oldSupply.map(m=>m.id).sort((a,b)=>a-b),r.ingredients.filter(m=>m.id<75).map(m=>m.id).sort((a,b)=>a-b),`Old supply coverage ${s.id}`);
    for(const old of r.oldSupply)same(old.anyOf,baseline.ingredients[old.id].unlockAlternatives,`Old supply semantics ${s.id}/${old.id}`);
    return {...select(r,['id','mode','toolId','exact','extraIngredientsAllowed','candidateKind','hardAttempt','repeatGuaranteed','companionPolicy','materialSupplyRequired']),
      key:s.key,egg:s.egg,toolLevel:r.toolLevel-1,kitchenLevel:r.kitchenLevel-1,ingredients:clone(r.ingredients),firstChancePercent:percent(r.firstChance,'trial chance'),
      oldSupply:r.oldSupply.map(m=>({id:m.id,anyOf:clone(m.anyOf)})),rulesVersion:RULES_VERSION};
  });
  unique(recipes,'recipe ids');
  unique(recipes.map(r=>({id:[r.egg,r.toolId,...r.ingredients.map(x=>x.id).sort((a,b)=>a-b)].join('/')})),'precise recipe tuples');
  const species=content.species.map(s=>{
    for(const t of ['menus','collections','orders','regulars','projects','specials'])for(const id of s[t])reference(t,id);
    for(const id of s.relatedCards)reference('cards',id);
    for(const tag of s.tags)assert(Object.hasOwn(content.tags,tag),`Unknown species tag ${tag}`);
    for(const trait of s.exploration.traits)assert(Object.hasOwn(content.traits,trait),`Unknown trait ${trait}`);
    assert(s.signature===null||Object.hasOwn(SIGNATURES,s.signature),`Unknown signature ${s.signature}`);
    assert(s.edible===(s.signature!==null),`Edibility/signature mismatch ${s.id}`);
    safeInteger(s.priceProposal.baseSaleCP,0,100000,`${s.id} base CP`);safeInteger(s.priceProposal.collectCP,0,100000,`${s.id} collect CP`);
    const unlock=select(s.unlock,['region','identifiedMaterials','firstSpecimenForOldOnly','card','duckLicense','fullMethodRequired']);
    unlock.minimumKitchen=s.unlock.minimumKitchen-1;
    if(unlock.card)reference('cards',unlock.card);
    return {...select(s,['id','key','egg','region','catalogLabel','edible','viewable','tags','exploration','menus','collections','orders','regulars','projects','relatedCards','specials','displayMenus']),
      numericId:Number(s.key.split(':')[1]),recipeId:s.recipe.id,unlock,signature:s.signature===null?null:SIGNATURES[s.signature],
      baseSaleCP:s.priceProposal.baseSaleCP,collectCP:s.priceProposal.collectCP,priceStatus:'proposal-unbalanced',assetId:`ART-${s.id}`,
      links:s.uses.map(u=>({system:u.system,target:u.target}))};
  });
  const materials=content.materials.map(m=>{
    reference('regions',m.region);reference('cards',m.specimen);reference('cards',m.event);safeInteger(m.priceCP,0,10000,`${m.id} price`);
    const users=content.species.filter(s=>s.recipe.ingredients.some(i=>i.id===m.id));
    assert(new Set(users.map(s=>s.recipe.toolId)).size>=3&&new Set(users.map(s=>s.egg)).size===2,`Material usage coverage ${m.id}`);
    same(m.uses,users.map(s=>s.id),`Material users ${m.id}`);
    return {...select(m,['id','stableId','region','priceCP','specimen','event','states']),entrySpecies:speciesKey(m.entrySpecies),uses:speciesList(m.uses),tools:clone(m.tools),oldKeys:speciesList(m.oldKeys),assetId:`ART-MAT-${m.id}`};
  });
  const regions=content.regions.map(r=>({...select(r,['id','route','hours','environment','materials','discoveries','collected']),kitchenLevel:r.kitchen-1,entrySpecies:speciesKey(r.entrySpecies),oldKeys:speciesList(r.oldKeys),places:r.places.map((name,i)=>({id:`${r.id}:${i}`,index:i,name})),gateRequirement:`${r.id}:gate`}));
  const alternatives=content.alternatives.map(a=>{
    speciesKey(a.target);safeInteger(a.toolLevel,1,3,`${a.id} level`);safeInteger(a.kitchenLevel,1,4,`${a.id} kitchen`);
    assert(tableSets.cards.has(a.unlock)||tableSets.projects.has(a.unlock),`Alternative unlock ${a.id}`);
    return {...select(a,['id','region','target','toolId','ingredients','replaces','unlock','mode','quantityPerIngredient','fullMethod']),toolLevel:a.toolLevel-1,kitchenLevel:a.kitchenLevel-1,guaranteeRequirement:`${a.id}:guarantee`,rulesVersion:RULES_VERSION};
  });
  const cards=content.cards.map(c=>{
    reference('regions',c.region);assert(c.placeIndex===0||c.placeIndex===1,`Card place ${c.id}`);
    same(c.place,content.regions.find(r=>r.id===c.region).places[c.placeIndex],`Card place label ${c.id}`);
    assert(['specimen','lore','event'].includes(c.type)&&['找标本','寻见闻'].includes(c.focus),`Card type/focus ${c.id}`);
    if(c.material!==null)assert(materialIds.has(c.material),`Unknown card material ${c.id}`);
    if(c.team.trait!==null)assert(Object.hasOwn(content.traits,c.team.trait),`Unknown card trait ${c.id}`);
    if(c.team.environment!==null)assert(['yard','water','wood'].includes(c.team.environment),`Unknown card environment ${c.id}`);
    const chance=c.discoveryChance;
    assert(chance.bonusMaximumApplications===1,'Discovery bonus may apply only once');
    const effects=c.effects.map(effect=>{
      const e=clone(effect);
      assert(['specimen','supply-after-identification','regional-collection','species-direction','practice','alternative-method'].includes(e.kind),`Unknown card effect ${c.id}/${e.kind}`);
      if(e.material!==undefined)assert(materialIds.has(e.material),`Unknown effect material ${c.id}`);
      if(e.target&&['species-direction'].includes(e.kind))e.target=speciesKey(e.target);
      else if(e.target&&e.kind==='alternative-method')reference('alternatives',e.target);
      else if(e.target)assert(tableSets.collections.has(e.target)||tableSets.specials.has(e.target),`Card effect reference ${e.target}`);
      return e;
    });
    return {...select(c,['id','region','type','material']),placeId:`${c.region}:${c.placeIndex}`,focus:c.focus==='找标本'?'specimen':'lore',
      team:{...select(c.team,['trait','environment','sameMemberMaySatisfyBoth','maxPredicates','bonusEnvironment','bonusTrait']),oldExamples:speciesList(c.team.oldExamples)},
      gateRequirement:`${c.id}:gate`,effects,discoveryChance:{basePercent:percent(chance.base,'card base'),perTeamFPercent:percent(chance.perTeamF,'card F'),capPercent:percent(chance.cap,'card cap'),bonusPercent:percent(chance.bonus,'card bonus'),bonusCondition:clone(chance.bonusCondition),bonusMaximumApplications:1},
      ...(c.exchange?{exchange:{...clone(c.exchange),allowed:speciesList(content.selectors[c.exchange.pool])}}:{}),assetId:`ART-${c.id}`};
  });
  const menus=content.menus.map(m=>({...select(m,['id','orders','regulars','projects','oneSpeciesOneRole','maxSpecies','perBirdBonusCapCP']),
    roles:m.roles.map(r=>({...select(r,['id','required','maxSpecies']),allowed:speciesList(r.allowed)})),tiersPercent:Object.fromEntries(Object.entries(m.tiers).map(([k,v])=>[k,percent(v,`${m.id} tier`)])),
    completeRequirement:`${m.id}:complete`,unlockRequirement:`${m.id}:unlock`,validServiceRequirement:`${m.id}:validService`,
    examples:m.examples.map(example=>example.map(x=>({key:speciesKey(x.id),quantity:x.quantity})))}));
  const selectors=Object.fromEntries(Object.entries(content.selectors).map(([k,list])=>[k,speciesList(list)]));
  const orders=content.orders.map(o=>({...select(o,['id','kind','repeatable','minimumDistinct','bonusCP','menu','card','regular','project','displayDistinct']),
    groups:o.groups.map(g=>({...select(g,['id','selector','quantity']),allowed:speciesList(g.allowed)})),firstResult:select(o.firstResult,['id','repeatReward']),
    variants:o.variants.map(v=>({...select(v,['id','egg','regions']),...(v.allowed?{allowed:speciesList(v.allowed)}:{})})),
    qualificationRequirement:`${o.id}:qualification`,paymentRequirement:`${o.id}:payment`,...(o.seasonRule?{seasonRequirement:`${o.id}:seasonRule`}:{})}));
  const regulars=content.regulars.map(r=>({...select(r,['id','region','memento']),stages:r.stages.map(s=>({id:s.id,reward:s.reward,gateRequirement:`${s.id}:gate`,alternativesRequirement:`${s.id}:alternatives`,recordPolicyRequirement:`${s.id}:recordPolicy`}))}));
  const projects=content.projects.map(p=>({id:p.id,gateRequirement:`${p.id}:gate`,oldAllowed:speciesList(p.oldAllowed),newAllowed:speciesList(p.newAllowed),
    stages:p.stages.map(s=>({id:s.id,checkRequirement:`${s.id}:check`,costCP:s.costCP,consume:s.consume?{...clone(s.consume),allowed:speciesList(s.consume.allowed)}:null})),
    ...(p.optionalDisplay?{optionalDisplay:{...select(p.optionalDisplay,['minimum','consume']),allowed:speciesList(p.optionalDisplay.allowed),requirement:`${p.id}:optionalDisplay.rule`}}:{})}));
  for(const [i,p] of projects.entries())assert(p.stages.reduce((n,s)=>n+s.costCP,0)===[200,500,800,2000][i],`Project cost changed ${p.id}`);
  const collections=content.collections.map(c=>({...select(c,['id','kind','menu','region']),oldKeys:speciesList(c.oldKeys),newKeys:speciesList(c.newIds),
    // Regional fixed slots are specimen-card references, not species references.
    fixed:{kind:c.fixed.kind,referenceType:c.kind==='region'?'card':'species',allowed:c.kind==='region'?c.fixed.allowed.map(id=>(reference('cards',id),id)):speciesList(c.fixed.allowed)},
    optional:{...select(c.optional,['countStage1','countStage2','countComplete','representativeIncludedInCount']),allowed:speciesList(c.optional.allowed)},
    ...(c.displayGuests?{displayGuests:speciesList(c.displayGuests)}:{}),
    ...(c.stages?{stages:c.stages.map(s=>({id:s.id,requirement:`${s.id}:requires`,reward:s.reward}))}:{stageRequirement:`${c.id}:stageRules`}),
    practiceRequirement:`${c.id}:practice.any`,...(c.practice.fullMenuStamp?{fullMenuRequirement:`${c.id}:practice.fullMenuStamp`}:{}),
    ...(Array.isArray(c.rewards)?{rewards:c.rewards.map(r=>select(r,['stage','id','kind']))}:{practiceReward:c.practice.reward})}));
  const specials=content.specials.map(s=>({id:s.id,oldKeys:speciesList(s.oldKeys),newKeys:speciesList(s.newIds),fixedRequirement:`${s.id}:fixed`,optionalRequirement:`${s.id}:optional`,practiceRequirement:`${s.id}:practice`,rewardRequirement:`${s.id}:reward`}));
  const mementos=content.mementos.map(m=>({...select(m,['id','source','once','displaySlots']),unlockRequirement:`${m.id}:unlock`,assetId:`ART-${m.id}`}));
  const papers=content.paperRecords.map(p=>select(p,['id','source','once']));
  const legacySemantics=Object.fromEntries(baseline.species.map(s=>[s.key,{edible:s.edible,tags:clone(s.tags),traits:clone(s.exploration.traits),signature:s.signature===null?null:SIGNATURES[s.signature],season:s.season,participation:clone(s.participation),menus:clone(s.menus),orders:clone(s.orders),collections:clone(s.collections),specials:clone(s.specials)}]));
  for(const key of oldKeys)assert(legacySemantics[key].signature!==undefined,`Unknown legacy signature ${key}`);
  const allowedSets={};
  for(const menu of menus)for(const role of menu.roles)allowedSets[role.id]=role.allowed;
  for(const order of orders)for(const group of order.groups)allowedSets[group.id]=group.allowed;
  for(const [selector,list]of Object.entries(selectors))allowedSets[`selector:${selector}`]=list;
  for(const c of collections){allowedSets[`${c.id}:optional`]=c.optional.allowed;if(c.fixed.referenceType==='species')allowedSets[`${c.id}:fixed`]=c.fixed.allowed;}
  const runtime={runtimeFormat:RUNTIME_FORMAT,rulesVersion:RULES_VERSION,contentRevision:CONTENT_REVISION,species,recipes,materials,regions,alternatives,cards,guide:{id:'GUIDE-B',countAsDiscoveryCard:false,sourceRegion:'R',sourcePlaceId:'R:1',gateRequirement:'GUIDE-B:gate',triggerRequirement:'GUIDE-B:trigger'},menus,selectors,orders,regulars,projects,collections,specials,mementos,paperRecords:papers,allowedSets};
  const {requirements,coverage}=compileConditions(content,sha256,runtime);
  const assets={};
  for(const table of ['species','materials','cards','mementos','regions','projects','menus','specials'])for(const entry of art[table]) {
    assert(!assets[entry.id],`Duplicate art id ${entry.id}`);
    assert(content[table].some(row=>row.id===entry.content),`Unknown art content ${entry.id}`);
    assets[entry.id]={id:entry.id,contentId:entry.content,kind:table,productionStatus:'final-art-pending',variants:{},crop:null,safeArea:table==='species'?{insetPercent:12,footClearancePercent:8}:table==='cards'?{centerPercent:60}:null};
    for(const variant of entry.deliverables)assets[entry.id].variants[variant]={path:`/web/art/regional/${entry.id}/${variant}.png`,available:false,width:table==='species'?(variant==='full'?512:256):table==='cards'?480:null,height:table==='species'?(variant==='full'?512:256):table==='cards'?270:null};
  }
  assert(Object.keys(assets).length===112,'Expected 112 artwork sets');
  return {runtime,text,requirements,coverage,legacySemantics,assets,counts:{species:241,chicken:152,duck:89,legacy:193,regional:48,materials:83,cards:24,menus:8,orders:12,regularStages:16,projects:4,mementos:12,paperRecords:24}};
}

const emitModule=(exports,sourceHash)=>`// Generated by tools/build-runtime-content.mjs. Do not edit.\n// sourceHash: ${sourceHash}\nconst deepFreeze = value => { if(value && typeof value === 'object' && !Object.isFrozen(value)){for(const child of Object.values(value))deepFreeze(child);Object.freeze(value);}return value;};\n`+Object.entries(exports).map(([name,value])=>`export const ${name} = deepFreeze(${JSON.stringify(value,null,2)});\n`).join('');
export function buildRuntimeContent({root=ROOT,check=false}={}) {
  const pack=path.join(root,'docs/content-pack');
  const read=name=>JSON.parse(fs.readFileSync(path.join(pack,`${name}.json`),'utf8'));
  const content=read('content');
  const result=compileRuntimeContent(content,read('baseline'),read('art-manifest'));
  const productionFile='web/art/production/manifest.json';
  const hasProduction=fs.existsSync(path.join(root,productionFile));
  if(hasProduction)result.assets=mergeProductionArt(root,content,result.assets,JSON.parse(fs.readFileSync(path.join(root,productionFile),'utf8')));
  const sources={};
  // Hash only inputs that can change compiled runtime bytes. Audit reports and
  // explanatory documents are outputs/context, not runtime authoring inputs.
  const files=['docs/content-pack/content.json','docs/content-pack/baseline.json','docs/content-pack/art-manifest.json',
    'tools/build-runtime-content.mjs','tools/content-runtime-rules.mjs','tools/content-regional-rules.mjs','tools/content-business-rules.mjs','tools/content-collection-rules.mjs','tools/content-regular-rules.mjs','tools/content-project-rules.mjs','web/legacy-content.js','web/data.js','web/seasonal-pack.js','web/recipe-catalog-data.js','web/integration-data.js','web/trade-data.js','web/ingredient-unlocks.js'];
  files.push('tools/production-art-manifest.mjs');
  if(hasProduction)files.push(productionFile);
  for(const file of files.sort())sources[file]=sha256(fs.readFileSync(path.join(root,file)));
  const sourceHash=sha256(JSON.stringify(sources));
  const compiled=result.coverage.filter(c=>c.status==='compiled').length,unavailable=result.coverage.length-compiled;
  const outputs={
    'web/regional-content.generated.js':emitModule({REGIONAL_CONTENT:result.runtime,LEGACY_SEMANTICS:result.legacySemantics},sourceHash),
    'web/regional-text.generated.js':emitModule({REGIONAL_TEXT:result.text},sourceHash),
    'web/runtime-requirements.generated.js':emitModule({RUNTIME_REQUIREMENTS:result.requirements},sourceHash),
    'web/runtime-assets.generated.js':emitModule({RUNTIME_ASSETS:result.assets},sourceHash),
    'web/runtime-condition-coverage.generated.json':JSON.stringify({runtimeFormat:RUNTIME_FORMAT,sourceHash,total:result.coverage.length,compiled,unavailable,conditions:result.coverage},null,2)+'\n',
  };
  const manifest={runtimeFormat:RUNTIME_FORMAT,rulesVersion:RULES_VERSION,contentRevision:CONTENT_REVISION,sourceHash,sources,
    counts:result.counts,idMapping:Object.fromEntries(result.runtime.species.map(s=>[s.id,s.key])),
    legacyIdentityHash:sha256(JSON.stringify(LEGACY193.characters)),legacyMaterialHash:sha256(JSON.stringify(LEGACY193.tools[2])),
    finalArtReady:Object.values(result.assets).every(a=>a.productionStatus==='FINAL'),
    productionArt:{final:Object.values(result.assets).filter(a=>a.productionStatus==='FINAL').length,approved:Object.values(result.assets).filter(a=>a.productionStatus==='art-approved').length,pending:Object.values(result.assets).filter(a=>a.productionStatus==='final-art-pending').length},
    priceStatus:'proposal-unbalanced',conditionCoverage:{total:result.coverage.length,compiled,unavailable},
    outputs:Object.fromEntries(Object.entries(outputs).map(([name,value])=>[name,sha256(value)]))};
  outputs['web/runtime-content.manifest.json']=JSON.stringify(manifest,null,2)+'\n';
  for(const [file,expected] of Object.entries(outputs)) {
    const target=path.join(root,file);
    if(check)assert(fs.existsSync(target)&&fs.readFileSync(target,'utf8')===expected,`Generated output drift: ${file}; run node tools/build-runtime-content.mjs`);
    else fs.writeFileSync(target,expected,'utf8');
  }
  return manifest;
}
if(process.argv[1]&&pathToFileURL(path.resolve(process.argv[1])).href===import.meta.url) {
  try {const m=buildRuntimeContent({check:process.argv.includes('--check')});console.log(`runtime ${m.counts.species} species / ${m.counts.materials} materials; ${m.conditionCoverage.compiled} compiled / ${m.conditionCoverage.unavailable} unavailable conditions; source ${m.sourceHash}`);}
  catch(error){console.error(error.message);process.exitCode=1;}
}
