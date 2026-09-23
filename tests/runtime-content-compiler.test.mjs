import test from 'node:test';
import {REQUIREMENT_KINDS} from '../web/requirements.js';
import assert from 'node:assert/strict';
import fs from 'node:fs';
import {compileRuntimeContent,validateAuthorSchema,sha256} from '../tools/build-runtime-content.mjs';
import {conditionInputs,CONDITION_SOURCE_HASHES} from '../tools/content-runtime-rules.mjs';
import {LEGACY193} from '../web/legacy-content.js';
import {REGIONAL_CONTENT,LEGACY_SEMANTICS} from '../web/regional-content.generated.js';
import {RUNTIME_REQUIREMENTS} from '../web/runtime-requirements.generated.js';
import {RUNTIME_ASSETS} from '../web/runtime-assets.generated.js';

const load=name=>JSON.parse(fs.readFileSync(new URL(`../docs/content-pack/${name}.json`,import.meta.url),'utf8'));
const source={content:load('content'),baseline:load('baseline'),art:load('art-manifest')};
const compile=(mutate=()=>{})=>{const data=structuredClone(source);mutate(data);return compileRuntimeContent(data.content,data.baseline,data.art);};
const result=compile();

test('runtime compiler freezes all 193 identities and appends exactly 48 / 152 chicken / 89 duck',()=>{
  assert.equal(LEGACY193.characters[0].length,128);assert.equal(LEGACY193.characters[1].length,65);
  const keys=[...LEGACY193.characters.flatMap((rows,egg)=>rows.map(s=>`${egg}:${s.id}`)),...result.runtime.species.map(s=>s.key)];
  assert.equal(keys.length,241);assert.equal(new Set(keys).size,241);
  assert.equal(keys.filter(k=>k.startsWith('0:')).length,152);assert.equal(keys.filter(k=>k.startsWith('1:')).length,89);
  for(const [region,offset] of [['V',0],['R',6],['T',12],['B',18]])for(const egg of [0,1])for(let n=1;n<=6;n++)assert.equal(result.runtime.species.find(s=>s.id===`${region}-${egg?'D':'C'}${n}`).key,`${egg}:${(egg?65:128)+offset+n-1}`);
  assert.throws(()=>compile(d=>d.content.species[0].key='0:127'),/241 identity uniqueness/);
});

test('frozen legacy gameplay properties and supply are checked against the actual legacy implementation',()=>{
  for(const field of ['name','description','basePrice','signature'])assert.throws(()=>compile(d=>d.baseline.species[0][field]=field==='basePrice'?999:'changed'),/Legacy attributes drift/);
  assert.throws(()=>compile(d=>d.baseline.species[0].exploration.G++),/Legacy abilities drift/);
  assert.throws(()=>compile(d=>d.baseline.species[0].recipes[0].minLevel++),/Legacy recipes drift/);
  assert.throws(()=>compile(d=>d.baseline.ingredients[3].unlockAlternatives[0][0].level++),/Legacy ingredient supply drift/);
});

test('all old species get explicit edible, tag and trait semantics, including excluded legacy food names',()=>{
  assert.equal(Object.keys(LEGACY_SEMANTICS).length,193);
  for(const old of source.baseline.species)assert.deepEqual(LEGACY_SEMANTICS[old.key].traits,old.exploration.traits);
  assert.equal(LEGACY_SEMANTICS['0:5'].edible,false);assert.equal(LEGACY_SEMANTICS['1:4'].edible,false);assert.equal(LEGACY_SEMANTICS['1:50'].edible,false);
  assert.equal(LEGACY_SEMANTICS['0:0'].edible,true);
});

test('83 material IDs preserve 0–74 and every new material has both eggs and at least three tools',()=>{
  assert.equal(LEGACY193.tools[2].length,75);assert.deepEqual(result.runtime.materials.map(m=>m.id),[75,76,77,78,79,80,81,82]);
  for(const m of result.runtime.materials){const recipes=result.runtime.recipes.filter(r=>r.ingredients.some(i=>i.id===m.id));assert.ok(new Set(recipes.map(r=>r.toolId)).size>=3);assert.equal(new Set(recipes.map(r=>r.egg)).size,2);}
  assert.throws(()=>compile(d=>d.content.materials[0].id=74),/83 material identity uniqueness/);
});

test('only display levels convert once; old supply zero-based conditions are unchanged',()=>{
  for(const sourceRow of source.content.species){const r=result.runtime.recipes.find(r=>r.id===sourceRow.recipe.id);assert.equal(r.toolLevel,sourceRow.recipe.toolLevel-1);assert.equal(r.kitchenLevel,sourceRow.recipe.kitchenLevel-1);assert.deepEqual(r.oldSupply,sourceRow.recipe.oldSupply.map(x=>({id:x.id,anyOf:x.anyOf})));}
  assert.equal(result.runtime.recipes.find(r=>r.id==='REC-V-C1').toolLevel,0);
  assert.throws(()=>compile(d=>d.content.species[0].recipe.toolLevel=0),/Invalid integer/);
});

test('all mixed allowed references convert to stable species keys while regional fixed groups retain card IDs',()=>{
  for(const allowed of Object.values(result.runtime.allowedSets))for(const key of allowed)assert.match(key,/^[01]:\d+$/);
  for(const c of result.runtime.collections.filter(c=>c.kind==='region')){assert.equal(c.fixed.referenceType,'card');assert.deepEqual(c.fixed.allowed,[`${c.region}-S1`,`${c.region}-S2`]);}
  assert.ok(result.runtime.menus[0].roles[0].allowed.includes('0:128'));
  assert.throws(()=>compile(d=>d.content.menus[0].roles[0].allowed.push('V-C99')),/Unknown species reference/);
});

test('schema rejects unknown fields at root, nested objects and array members and rejects missing required fields',()=>{
  const mutations=[d=>d.content.unknown=1,d=>d.content.species[0].recipe.hiddenRule=true,d=>d.content.cards[0].team.secret=false,d=>d.baseline.species[0].bonusCP=100,d=>d.art.species[0].surprise='x'];
  for(const mutate of mutations)assert.throws(()=>compile(mutate),/Unknown author field/);
  assert.throws(()=>compile(d=>delete d.content.species[0].key),/Missing author field/);
  assert.throws(()=>compile(d=>d.content.species[0].egg='0'),/Invalid type/);
  assert.throws(()=>validateAuthorSchema({},'absent'),/Unknown author schema/);
});

test('every authored condition is explicitly bound or unavailable with a reviewed source hash',()=>{
  const inputs=conditionInputs(source.content);assert.equal(inputs.length,438);assert.ok(Object.keys(RUNTIME_REQUIREMENTS).length>=438);
  for(const item of inputs){assert.equal(CONDITION_SOURCE_HASHES[item.id],sha256(JSON.stringify(item.source)));const req=RUNTIME_REQUIREMENTS[item.id];assert.ok(['compiled','unavailable'].includes(req.kind)||req.compiled===true&&REQUIREMENT_KINDS.includes(req.kind),item.id);if(RUNTIME_REQUIREMENTS[item.id].kind==='compiled'){assert.ok(RUNTIME_REQUIREMENTS[item.id].rule);assert.ok(RUNTIME_REQUIREMENTS[item.id].runtime);}assert.equal(RUNTIME_REQUIREMENTS[item.id].sourcePointer,item.sourcePointer);}
  assert.throws(()=>compile(d=>d.content.regulars[0].stages[0].gate+='修改'),/Unreviewed condition/);
  assert.throws(()=>compile(d=>d.content.cards[0].gate='任意'),/Unreviewed condition/);
});

test('compiled runtime contains no author work names, art briefs, raw natural-language condition expressions or executable callbacks',()=>{
  const serialized=JSON.stringify(result.runtime);
  for(const field of ['workName','technologyNote','namingDecision','oldSupplyRules','foodAndMaterial','businessExclusion'])assert.ok(!serialized.includes(`"${field}"`));
  assert.ok(!serialized.includes(source.content.menus[0].complete));
  assert.equal(result.text['V-C1'].description,source.content.species[0].description);
  assert.equal(result.text['RG1-1'].text,source.content.regulars[0].stages[0].text);
});

test('all 48 recipes preserve exact batch quantities, probability and independent guarantee contract',()=>{
  assert.equal(result.runtime.recipes.length,48);
  for(const r of result.runtime.recipes){assert.equal(r.firstChancePercent,25);assert.equal(r.hardAttempt,4);assert.equal(r.repeatGuaranteed,1);assert.equal(r.exact,true);assert.equal(r.extraIngredientsAllowed,false);for(const i of r.ingredients)assert.equal(i.quantity,1);if(r.toolId===8)assert.equal(r.egg,0);}
  assert.throws(()=>compile(d=>d.content.species[0].recipe.ingredients[0].quantity=2),/Invalid ingredient/);
  assert.throws(()=>compile(d=>d.content.species[0].recipe.firstChance=25),/Trial contract changed/);
  assert.throws(()=>compile(d=>d.content.species[6].recipe.toolId=8),/Steamer remains chicken-only/);
});

test('discovery chance uses integer percentages and bonus cannot stack; 24 cards exclude GUIDE-B',()=>{
  assert.equal(result.runtime.cards.length,24);assert.equal(result.runtime.guide.countAsDiscoveryCard,false);
  for(const c of result.runtime.cards){assert.equal(c.discoveryChance.basePercent,25);assert.equal(c.discoveryChance.perTeamFPercent,2);assert.equal(c.discoveryChance.capPercent,60);assert.equal(c.discoveryChance.bonusPercent,5);assert.equal(c.discoveryChance.bonusMaximumApplications,1);}
  assert.equal(result.runtime.cards.find(c=>c.id==='R-S1').team.environment,null);
  assert.equal(result.runtime.cards.find(c=>c.id==='R-S1').discoveryChance.bonusCondition.environment,'water');
  assert.equal(result.runtime.cards.find(c=>c.id==='T-N2').discoveryChance.bonusCondition.trait,'floral');
  assert.throws(()=>compile(d=>d.content.cards[0].discoveryChance.bonusMaximumApplications=2),/only once/);
});

test('business data preserves 8 menus / 12 orders / 16 stages / 4 project costs and distinguishes paper from mementos',()=>{
  assert.equal(result.runtime.menus.length,8);assert.equal(result.runtime.orders.length,12);assert.equal(result.runtime.regulars.flatMap(r=>r.stages).length,16);
  assert.deepEqual(result.runtime.projects.map(p=>p.stages.reduce((sum,s)=>sum+s.costCP,0)),[200,500,800,2000]);
  assert.equal(result.runtime.paperRecords.length,24);assert.equal(result.runtime.mementos.length,12);
  assert.deepEqual(result.runtime.menus[0].tiersPercent,{ordinary:0,suitable:5,complete:8});
  assert.equal(result.runtime.orders.find(o=>o.id==='O04').bonusCP,0);
  assert.throws(()=>compile(d=>d.content.projects[0].stages[2].costCP=201),/Project cost changed/);
});

test('art manifest records all 112 required sets and never borrows a legacy image for a new species',()=>{
  assert.equal(Object.keys(RUNTIME_ASSETS).length,112);
  for(const s of result.runtime.species){const art=RUNTIME_ASSETS[s.assetId];assert.equal(art.productionStatus,'final-art-pending');assert.equal(art.safeArea.insetPercent,12);assert.equal(art.variants.full.width,512);assert.equal(art.variants.portrait.width,256);for(const v of Object.values(art.variants)){assert.match(v.path,/^\/web\/art\/regional\//);assert.equal(v.available,false);}}
});

test('source compiler and checked-in generated runtime are equivalent and immutable',()=>{
  assert.deepEqual(REGIONAL_CONTENT,result.runtime);assert.deepEqual(LEGACY_SEMANTICS,result.legacySemantics);
  assert.ok(Object.isFrozen(REGIONAL_CONTENT.species));assert.ok(Object.isFrozen(REGIONAL_CONTENT.recipes[0].ingredients[0]));
  assert.throws(()=>REGIONAL_CONTENT.species.push({}));
});

test('local alternatives preserve their original target and ALT-T explicitly replaces ingredient 3, never ingredient 10',()=>{
  const alt=result.runtime.alternatives.find(a=>a.id==='ALT-T');assert.ok(alt.replaces.includes(3));assert.ok(!alt.replaces.includes(10));
  assert.equal(result.runtime.alternatives.find(a=>a.id==='ALT-R').unlock,'PJ-2');
  for(const a of result.runtime.alternatives){assert.match(a.target,/^[01]:\d+$/);assert.equal(a.mode,'local-alternative');assert.ok(!Object.hasOwn(a,'repeatGuaranteed'));}
});

test('references are validated by identity type and edible whitelists are enforced for menus and procurement',()=>{
  assert.throws(()=>compile(d=>d.content.cards[0].effects[2].target='COL-nope'),/Card effect reference/);
  assert.throws(()=>compile(d=>d.content.orders[0].menu='MN999'),/Unknown menus reference/);
  assert.throws(()=>compile(d=>d.content.regulars[0].stages[0].reward='M999'),/Unknown regular reward/);
  assert.throws(()=>compile(d=>d.content.menus[0].roles[0].allowed.push('V-C6')),/Inedible menu role/);
  assert.throws(()=>compile(d=>d.content.orders[0].groups[0].allowed.push('0:5')),/Inedible purchase group/);
});

test('optional bay cargo resolves its selector into stable home species without expanding discovery-card counts',()=>{
  const exchange=result.runtime.cards.find(c=>c.id==='B-E1').exchange;
  assert.equal(exchange.pool,'home');assert.equal(exchange.quantity,6);assert.equal(exchange.rewardMaterial,81);assert.equal(exchange.noBaseCP,true);assert.equal(exchange.replacesBaseSlot,true);
  assert.deepEqual(exchange.allowed,result.runtime.selectors.home);
});

test('every compiled condition binds to an implemented domain export; the rest are owned by a named later Work',async()=>{
  const {RUNTIME_REQUIREMENTS:R}=await import('../web/runtime-requirements.generated.js');
  const modules=new Map();
  for(const r of Object.values(R)){
    if(r.kind==='compiled'){const m=modules.get(r.runtime.module)??await import('../'+r.runtime.module);modules.set(r.runtime.module,m);assert.equal(typeof m[r.runtime.export],'function',`${r.id} → ${r.runtime.module}#${r.runtime.export}`);}
    else if(r.kind==='unavailable')assert.ok(['I','J','K','B'].includes(r.work)||/regularRelation|productionRules:ui|M(09|10|11|12):unlock/.test(r.id),`${r.id} pending for ${r.work}`);
  }
});
