import test from 'node:test';
import assert from 'node:assert/strict';
import {readFileSync} from 'node:fs';
import {createHash} from 'node:crypto';
import vm from 'node:vm';
import {DATA} from '../web/data.js';
import {LEGACY193 as GAME_DATA} from '../web/legacy-content.js';
import * as E from '../web/engine.js';
import {ORIGINAL_RECIPE_CATALOG,RECIPE_SOURCE_SHA256} from '../web/recipe-catalog-data.js';
import {RECIPE_CATALOG,RECIPE_TOOLS,discoveredRecipe,recipeBookModel,prepareDiscoveredRecipe} from '../web/recipe-book.js';
import {SEASONAL_CHARACTERS,seasonalSurprise,prepareSeasonalRecipe} from '../web/seasonal-pack.js';
import {makeBackup,parseBackup} from '../web/save-store.js';
const NOW=new Date(2026,8,14,11).getTime();
function known(r){const s=E.freshState(NOW);s.kitchenLevel=3;s.toolLevels.fill(2);s.cp=30000;s.duck=true;s.total[r.key]=1;for(let i=0;i<12;i++)s.total[`0:${i}`]=1;s.ingredients=Object.fromEntries(r.ingredients.map(id=>[id,2]));if(r.campaign)s.events[r.campaign]=true;if(r.collectionTotal)s.total[`${r.collectionTotal[0]}:0`]=r.collectionTotal[1];if(r.kind==='sign')s.events.gift_tool_2_68_character_id=r.id;return s;}
test('all 193 partners have one cookbook record, with nine cookware groups and a special-change group',()=>{
  assert.equal(RECIPE_CATALOG.length,193);assert.equal(new Set(RECIPE_CATALOG.map(r=>r.key)).size,193);assert.equal(RECIPE_TOOLS.length,10);
  for(const [egg,list]of GAME_DATA.characters.entries())for(const c of list)assert.ok(RECIPE_CATALOG.some(r=>r.egg===egg&&r.id===c.id));
});
test('the display catalog tracks every actual original cooking branch including minimum levels and ingredients',()=>{
  const source=readFileSync(new URL('../web/recipes.js',import.meta.url),'utf8').replace(/\r\n/g,'\n');
  assert.equal(createHash('sha256').update(source).digest('hex'),RECIPE_SOURCE_SHA256,'rebuild the display catalog after recipe source changes');
  // Observe the real source's full weighted pool without random sampling. This
  // proves reachability even for a 1-weight rare result, without flaky retries.
  const scope={DATA,samplePool:pool=>[...pool]};vm.createContext(scope);vm.runInContext(source.slice(source.indexOf('export function originalRecipes')).replaceAll('export function','function'),scope);
  for(const row of ORIGINAL_RECIPE_CATALOG.filter(r=>r.kind==='pool')){
    const s=known({...row,key:`${row.egg}:${row.id}`});s.kitchenLevel=row.minKitchen;s.toolLevels[row.toolId]=row.minLevel;
    const when=new Date(2026,8,14,row.time==='other-phoenix'?15:11).getTime();
    const pool=scope.originalRecipes(s,row.egg,row.toolId,row.ingredients,when,()=>0);
    assert.ok(pool.includes(row.id),`unreachable recipe ${row.egg}:${row.id} source line ${row.sourceLine}`);
    for(const ingredient of row.ingredients){const fewer=row.ingredients.filter(id=>id!==ingredient);assert.ok(!scope.originalRecipes(s,row.egg,row.toolId,fewer,when,()=>0).includes(row.id),`unnecessary ingredient ${ingredient} for ${row.egg}:${row.id}`);}
    if(row.minLevel>0){s.toolLevels[row.toolId]--;assert.ok(!scope.originalRecipes(s,row.egg,row.toolId,row.ingredients,when,()=>0).includes(row.id),`wrong minimum level for ${row.egg}:${row.id}`);}
  }
});
test('unknown species have no recipe payload; equipment and campaign unlocks alone never expose them',()=>{
  const s=E.freshState(NOW);s.toolLevels.fill(2);s.kitchenLevel=3;s.duck=true;for(const r of RECIPE_CATALOG)if(r.campaign)s.events[r.campaign]=true;
  assert.equal(recipeBookModel(s).total,0);
  for(const r of RECIPE_CATALOG){assert.equal(discoveredRecipe(s,r.key),null);const before=structuredClone(s);assert.throws(()=>prepareDiscoveredRecipe(s,r.key),/收取/);assert.deepEqual(s,before);}
  s.batch={eggs:[{egg:0,id:120,status:'ready',collected:false}]};assert.equal(discoveredRecipe(s,'0:120'),null);
});
test('existing total or farm discovery unlocks the recipe and selling the last one cannot remove it',()=>{
  const s=E.freshState(NOW);s.total['0:3']=4;s.farm['1:5']=1;
  assert.equal(discoveredRecipe(s,'0:3').name,'香煎鸡');assert.equal(discoveredRecipe(s,'1:5').name,'橙汁香煎鸭');
  s.total['1:5']=1;E.sell(s,{'1:5':1});assert.equal(discoveredRecipe(s,'1:5').name,'橙汁香煎鸭');
  assert.deepEqual(recipeBookModel(s,{toolId:1,egg:1}).entries.map(r=>r.key),['1:5']);assert.equal(recipeBookModel(s,{toolId:0}).entries.length,0);
  assert.equal(recipeBookModel(parseBackup(makeBackup(s,NOW),NOW),{toolId:1}).entries.length,2);
});
test('preparing a recorded recipe is atomic, keeps the current batch and never spends materials or CP',()=>{
  for(const key of ['0:6','1:30','0:117','0:120','1:64']){
    const r=RECIPE_CATALOG.find(r=>r.key===key),s=known(r);E.startBatch(s,0,NOW,()=>.5);s.egg=1-r.egg;s.selected=[];const before=structuredClone(s);
    prepareDiscoveredRecipe(s,key,NOW);assert.equal(s.egg,r.egg);assert.deepEqual(s.selected,[...r.ingredients]);assert.deepEqual(s.batch,before.batch);assert.deepEqual(s.ingredients,before.ingredients);assert.equal(s.cp,before.cp);
    if(r.kind==='seasonal')assert.equal(s.events.seasonalRecipe,key);else assert.equal(s.events.seasonalRecipe,undefined);
  }
});
test('missing ingredients, levels, kitchen slots, campaign and sign identity cannot silently prepare',()=>{
  const cases=[['0:6',s=>s.ingredients={}],['1:30',s=>s.kitchenLevel=0],['0:117',s=>s.toolLevels[8]=0],['1:30',s=>s.duck=false],['0:48',s=>delete s.events.campaign_char_0_48],['0:89',s=>s.events.gift_tool_2_68_character_id=90]];
  for(const [key,mutate]of cases){const s=known(RECIPE_CATALOG.find(r=>r.key===key));mutate(s);const before=structuredClone(s);assert.equal(discoveredRecipe(s,key,NOW).ready,false);assert.throws(()=>prepareDiscoveredRecipe(s,key,NOW));assert.deepEqual(s,before);}
});
test('phoenix time windows and special changes are explained without claiming a guaranteed batch',()=>{
  const s=known(RECIPE_CATALOG.find(r=>r.key==='0:52'));assert.equal(discoveredRecipe(s,'0:52',NOW).ready,true);assert.equal(discoveredRecipe(s,'0:52',new Date(2026,8,14,13).getTime()).ready,false);
  s.total['0:34']=1;const change=discoveredRecipe(s,'0:34');assert.equal(change.special,true);assert.equal(change.ready,false);assert.match(change.note,/病变/);assert.throws(()=>prepareDiscoveredRecipe(s,'0:34'),/特殊变化/);
  assert.match(discoveredRecipe(s,'0:52',NOW).note,/最终获得率/);
});
test('all sixteen unknown handmades remain discoverable before their recipe is visible, then unlock only on collection',()=>{
  for(const c of SEASONAL_CHARACTERS){const r=RECIPE_CATALOG.find(r=>r.key===c.key),s=known(r);delete s.total[c.key];s.egg=c.egg;s.selected=[...c.ingredients];
    assert.equal(discoveredRecipe(s,c.key),null);assert.throws(()=>prepareSeasonalRecipe(s,c.key),/收取/);
    assert.equal(seasonalSurprise(s,c.toolId,c.ingredients,()=>.249).key,c.key);assert.equal(seasonalSurprise(s,c.toolId,c.ingredients,()=>.25),null);
    const cp=s.cp,batch=E.startBatch(s,c.toolId,NOW,()=>0);assert.equal(batch.eggs.length,24);assert.equal(batch.eggs.filter(e=>e.id===c.id).length,1);assert.equal(s.cp,cp-E.cookInfo(s,c.toolId).cost);assert.equal(discoveredRecipe(s,c.key),null);
    E.updateBatch(s,batch.ends+1,()=>.9);E.updateBatch(s,batch.ends+2002,()=>.9);E.updateBatch(s,batch.ends+2903,()=>.9);
    const index=batch.eggs.findIndex(e=>e.id===c.id);E.collect(s,index);assert.ok(discoveredRecipe(s,c.key));E.sell(s,{[c.key]:1});assert.ok(discoveredRecipe(s,c.key));
    assert.equal(seasonalSurprise(s,c.toolId,c.ingredients,()=>0),null);
  }
});
test('surprise discovery obeys exact pairing and progress gates and rejects invalid random data before spending',()=>{
  const c=SEASONAL_CHARACTERS[0],s=known(RECIPE_CATALOG.find(r=>r.key===c.key));delete s.total[c.key];s.egg=c.egg;
  assert.equal(seasonalSurprise(s,c.toolId,[],()=>0),null);assert.equal(seasonalSurprise(s,c.toolId,[...c.ingredients,0],()=>0),null);
  s.kitchenLevel=0;assert.equal(seasonalSurprise(s,c.toolId,c.ingredients,()=>0),null);s.kitchenLevel=3;
  assert.throws(()=>seasonalSurprise(s,c.toolId,c.ingredients,()=>NaN),/随机数/);
});
