import test from 'node:test';
import assert from 'node:assert/strict';
import fs from 'node:fs';
import {LEGACY193} from '../web/legacy-content.js';
import {GAME_DATA} from '../web/content-pack.js';
import {REGIONAL,resolveSpecies,resolveMaterial,resolveRecipeId,SPECIES_ABILITIES,SPECIES_DESCRIPTIONS,SPECIES_TRADE} from '../web/content-registry.js';
import {RECIPE_CATALOG,recipeId} from '../web/recipe-book.js';
import {speciesView,collectionPageModel,saleSelectionSummary} from '../web/collection-ui.js';
import {characterImage} from '../web/catalog.js';
import {freshState,normalizeSave,sell} from '../web/engine.js';
import {syncProgress} from '../web/progression.js';
import {createSaveStore,makeBackup,parseBackup} from '../web/save-store.js';
import {execute} from '../web/game-commands.js';

const NOW=1800000000000;
const author=JSON.parse(fs.readFileSync(new URL('../docs/content-pack/content.json',import.meta.url),'utf8'));
const baseline=JSON.parse(fs.readFileSync(new URL('../docs/content-pack/baseline.json',import.meta.url),'utf8'));

test('runtime GAME_DATA preserves exact original 193 character values and all 75 original materials',()=>{
  assert.deepEqual(GAME_DATA.characters.map(rows=>rows.length),[152,89]);
  for(const egg of [0,1])assert.deepEqual(GAME_DATA.characters[egg].slice(0,LEGACY193.characters[egg].length),LEGACY193.characters[egg]);
  assert.deepEqual(GAME_DATA.tools[2].slice(0,75),LEGACY193.tools[2]);
  assert.deepEqual(GAME_DATA.tools.slice(0,2),LEGACY193.tools.slice(0,2));
  assert.equal(GAME_DATA.tools[2].length,83);
  assert.ok(Object.isFrozen(GAME_DATA));assert.ok(Object.isFrozen(GAME_DATA.characters));assert.ok(Object.isFrozen(GAME_DATA.tools));
  assert.throws(()=>GAME_DATA.characters[0]=[]);
});

test('species resolver covers old193 and new48 with stable numeric, author and display identities',()=>{
  for(const old of baseline.species){const s=resolveSpecies(old.key);assert.equal(s.key,old.key);assert.equal(s.id,Number(old.key.split(':')[1]));assert.equal(s.title_zh_CN,old.name);assert.equal(s.cp_1,old.basePrice);assert.equal(s.edible,old.edible);assert.deepEqual(s.traits,old.exploration.traits);}
  for(const row of author.species){const s=resolveSpecies(row.key);assert.equal(s.id,Number(row.key.split(':')[1]));assert.equal(s.authorId,row.id);assert.equal(s.title_zh_CN,row.name);assert.equal(s.cp_1,row.priceProposal.baseSaleCP);assert.equal(s.cp_0,row.priceProposal.collectCP);assert.equal(s.catalogLabel,row.catalogLabel);assert.ok(Object.isFrozen(s));}
  for(const invalid of ['0:152','1:89','2:0','0:0128','V-C1','',null])assert.equal(resolveSpecies(invalid),null);
});

test('material resolver preserves all old values and maps 8 new material names/prices without implicit supply unlock',()=>{
  for(let id=0;id<75;id++)assert.deepEqual(resolveMaterial(id),LEGACY193.tools[2][id]);
  const state=freshState(NOW);
  for(const row of author.materials){const m=resolveMaterial(row.id);assert.equal(m.id,row.id);assert.equal(m.stableId,row.stableId);assert.equal(m.title_zh_CN,row.name);assert.equal(m.buy_cp,row.priceCP);assert.ok(Object.isFrozen(m));assert.equal(state.ingredients[row.id],undefined);assert.equal(state.expansion.discovery.identified[row.id],undefined);}
  for(const id of [-1,83,999,null])assert.equal(resolveMaterial(id),null);
});

test('recipe resolver preserves every legacy recipe string ID and its gameplay qualifiers',()=>{
  for(const old of RECIPE_CATALOG){const r=resolveRecipeId(recipeId(old));assert.ok(r,recipeId(old));for(const field of ['egg','id','key','kind','toolId','minLevel','minKitchen','campaign','collectionTotal','time'])assert.deepEqual(r[field],old[field],`${recipeId(old)} ${field}`);assert.deepEqual(r.ingredients,old.ingredients);}
});

test('new recipe and alternative resolvers keep stable IDs, old ingredient gates and no ordinary-pool alias',()=>{
  for(const def of REGIONAL.species){const recipe=resolveRecipeId(def.recipeId);assert.equal(recipe.id,def.recipeId);assert.equal(recipe.key,def.key);assert.equal(recipe.egg,def.egg);assert.ok(Object.isFrozen(recipe.ingredients));assert.equal(resolveRecipeId(def.key),null);}
  for(const alternative of REGIONAL.alternatives)assert.equal(resolveRecipeId(alternative.id).target,alternative.target);
  for(const id of ['REC-V-C7','REC-X-C1','ALT-X','',null])assert.equal(resolveRecipeId(id),null);
});

test('abilities, descriptions and trading cover every new identity explicitly, including eight ornaments',()=>{
  for(const row of author.species){assert.deepEqual(SPECIES_ABILITIES[row.key],{gather:row.exploration.G,discover:row.exploration.F,environment:row.exploration.environment});assert.equal(SPECIES_DESCRIPTIONS[row.key],row.description);assert.equal(SPECIES_TRADE[row.key].category,row.signature);assert.equal(SPECIES_TRADE[row.key].platter,row.edible);assert.equal(SPECIES_TRADE[row.key].replicable,false);}
  assert.equal(REGIONAL.species.filter(s=>!s.edible).length,8);
});

test('unknown species projection masks all 48 new names, descriptions, price and art even with a complete method',()=>{
  const state=freshState(NOW);state.expansion.methods.full=REGIONAL.recipes.map(r=>r.id);
  for(const row of author.species){const view=speciesView(state,row.egg,Number(row.key.split(':')[1]));assert.equal(view.known,false);assert.equal(view.name,'未发现');assert.equal(view.code,row.catalogLabel);assert.equal(view.price,null);assert.equal(view.stock,0);assert.equal(view.total,0);assert.ok(!('description'in view));assert.ok(!('artwork'in view));assert.ok(!JSON.stringify(view).includes(row.name));}
  for(const egg of [0,1]){const page=collectionPageModel(state,{egg,page:100});assert.equal(page.total,egg?89:152);for(const row of page.entries)assert.equal(row.known,false);}
});

test('new identities stay known after selling out; catalog artwork uses the marked concept resource instead of an old character',()=>{
  const state=freshState(NOW);
  for(const row of author.species){state.total[row.key]=1;const id=Number(row.key.split(':')[1]),view=speciesView(state,row.egg,id);assert.equal(view.name,row.name);assert.equal(view.stock,0);assert.equal(view.known,true);assert.equal(characterImage(row.egg,id),'/web/art/regional-concept.svg');}
  const svg=fs.readFileSync(new URL('../web/art/regional-concept.svg',import.meta.url),'utf8');assert.match(svg,/概念|concept/i);
});

test('compatibility fixture can save, sell every new species, restart and backup/restore through the transaction entry',()=>{
  const memory=new Map(),storage={getItem:key=>memory.get(key)??null,setItem:(key,value)=>memory.set(key,value)};
  const store=createSaveStore({storage,key:'registry-fixture',now:()=>NOW});store.load();
  let state=freshState(NOW);state.duck=true;
  for(const row of author.species){state.farm[row.key]=2;state.total[row.key]=2;}
  state.ingredients={75:1,76:1,77:1,78:1,79:1,80:1,81:1,82:1};syncProgress(state);state=normalizeSave(state,NOW);store.write(state);
  const selection=Object.fromEntries(author.species.map(s=>[s.key,2])),quoted=saleSelectionSummary(state,selection),beforeCP=state.cp;
  assert.equal(quoted.quantity,96);assert.equal(quoted.income,author.species.reduce((sum,s)=>sum+s.priceProposal.baseSaleCP*2,0));
  const committed=execute({state,store,command:{type:'compat-sale',selection},now:NOW,reduce:draft=>sell(draft,selection,{},NOW)});
  assert.equal(committed.state.cp,beforeCP+quoted.income);assert.equal(Object.values(committed.state.farm).reduce((a,b)=>a+b,0),0);
  assert.deepEqual(store.load().state,committed.state);assert.deepEqual(parseBackup(makeBackup(committed.state,NOW),NOW),committed.state);
  for(const row of author.species)assert.equal(speciesView(committed.state,row.egg,Number(row.key.split(':')[1])).known,true);
});

test('legacy saves reject new identity/material injection instead of silently broadening their source schema',()=>{
  const old=()=>{const s=freshState(NOW);s.version=3;for(const field of ['meta','clock','expansion','contentRevision'])delete s[field];return s;};
  for(const field of ['farm','total']){const s=old();s[field]['0:128']=1;assert.throws(()=>normalizeSave(s,NOW),/无效/);}
  const ingredient=old();ingredient.ingredients[75]=1;assert.throws(()=>normalizeSave(ingredient,NOW),/无效/);
  const selected=old();selected.selected=[75];assert.throws(()=>normalizeSave(selected,NOW),/无效/);
});

test('schema4 cannot re-enter the legacy skill refund migration by omitting the skill version',()=>{
  const state=freshState(NOW);delete state.progress.skillVersion;const before=structuredClone(state);
  assert.throws(()=>normalizeSave(state,NOW),/手艺版本/);assert.deepEqual(state,before);
});

test('authoring and integration generation read the fixed legacy boundary, not the expanding runtime aggregate',()=>{
  const content=fs.readFileSync(new URL('../docs/content-pack/authoring.mjs',import.meta.url),'utf8');
  assert.match(content,/import\s*\{LEGACY193 as D,EXPANSION\}\s*from\s*'\.\.\/\.\.\/web\/legacy-content\.js'/);
  assert.doesNotMatch(content,/from\s*'\.\.\/\.\.\/web\/content-pack\.js'/);
  const integration=fs.readFileSync(new URL('../tools/build-integration-content.mjs',import.meta.url),'utf8');assert.match(integration,/legacy-content\.js/);assert.doesNotMatch(integration,/from\s*'\.\.\/web\/content-pack\.js'/);
});
