import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {RECIPE_CATALOG} from '../web/recipe-book.js';
import {cookingCandidates} from '../web/candidate-query.js';
import {originalRecipePlan, originalRecipes} from '../web/recipes.js';
import {recipeStateAt,holidayForCharacter} from '../web/holiday-calendar.js';
import {GAME_DATA} from '../web/content-pack.js';
import {execute} from '../web/game-commands.js';
import {unit} from '../web/rng.js';
import {REGIONAL,resolveSpecies} from '../web/content-registry.js';
import {prepareRegionalRecipe} from '../web/regional-methods.js';
import {SEASONAL_CHARACTERS,prepareSeasonalRecipe} from '../web/seasonal-pack.js';
const NOW=new Date(2026,8,30,11).getTime();
const rng=seed=>{let i=0;return()=>unit(seed,'release','hatch',i++);};
function prepared(egg=0){const s=E.freshState(NOW,37);s.cp=1000000;s.kitchenLevel=3;s.toolLevels.fill(2);s.duck=true;s.egg=egg;s.total={'0:0':3000,'1:0':3000};return s;}
function forRecipe(r){
  const s=prepared(r.egg);s.selected=[...r.ingredients];s.ingredients=Object.fromEntries(r.ingredients.map(id=>[id,10]));
  if(r.campaign)s.events[r.campaign]=true;
  if(r.kind==='sign')s.events.gift_tool_2_68_character_id=r.id;
  let at=r.time==='other-phoenix'?new Date(2026,8,30,15).getTime():NOW;
  const holiday=holidayForCharacter(r.egg,r.id,at);if(holiday)at=holiday.start+11*3600000;
  // Use the same start-time clock for positive and negative cases.
  s.lastSeen=s.lastClean=s.farmFixed=s.farmChecked=at;s.cleanCycle.dirtyAt=at+36*3600000;
  return {s,at};
}
const ids=(s,tool,at)=>cookingCandidates(s,tool,at).candidates.map(c=>c.id);

test('every pair of single-condiment recipes preserves both candidates in either selection order',()=>{
  const rows=RECIPE_CATALOG.filter(r=>r.kind==='pool'&&r.ingredients.length===1&&!r.campaign);let cases=0;
  for(const a of rows)for(const b of rows){
    if(a.id>=b.id||a.egg!==b.egg||a.toolId!==b.toolId||a.ingredients[0]===b.ingredients[0])continue;
    const {s,at}=forRecipe(a);s.selected=[...a.ingredients,...b.ingredients];s.ingredients=Object.fromEntries(s.selected.map(id=>[id,10]));
    const first=ids(s,a.toolId,at);s.selected.reverse();const second=ids(s,a.toolId,at);
    assert.ok(first.includes(a.id)&&first.includes(b.id),`${a.key} + ${b.key}`);assert.deepEqual(second,first);cases++;
  }
  assert.ok(cases>=20,`tested ${cases} condiment pairs`);
});

test('lemon and teriyaki both hatch, survive save/reload and enter inventory in 256 seeded batches',()=>{
  const counts={};
  for(let seed=1;seed<=256;seed++){
    let s=prepared();s.selected=seed%2?[1,2]:[2,1];s.ingredients={1:1,2:1};
    const before=structuredClone(s);E.startBatch(s,1,NOW,rng(seed),()=>.5);
    assert.deepEqual(s.batch.ingredients,before.selected);assert.deepEqual(s.ingredients,{1:0,2:0});
    s=E.normalizeSave(JSON.parse(JSON.stringify(s)),NOW);const b=s.batch;
    E.updateBatch(s,b.ends+1);E.updateBatch(s,b.ends+2001);E.updateBatch(s,b.ends+2901);
    for(let i=0;i<24;i++){const id=b.eggs[i].id;assert.ok([3,4,6,7].includes(id));assert.equal(E.collect(s,i,b.ends+2901),true);counts[id]=(counts[id]??0)+1;}
    assert.equal(Object.values(s.farm).reduce((a,b)=>a+b,0),24);
    assert.deepEqual(E.normalizeSave(JSON.parse(JSON.stringify(s)),b.ends+2901).farm,s.farm);
  }
  for(const id of [3,4,6,7])assert.ok(counts[id]>0,`candidate ${id} unreachable`);
  console.log('256 batches / 6144 collected eggs:',JSON.stringify(counts));
});

test('every original pool recipe is reachable with seeded real sampling and fails each missing requirement',()=>{
  let cases=0;
  for(const r of RECIPE_CATALOG.filter(r=>r.kind==='pool')){
    const {s,at}=forRecipe(r),has=x=>ids(x,r.toolId,at).includes(r.id);
    assert.ok(has(s),`positive ${r.key}`);
    const seen=new Set();for(let seed=1;seed<=512&&!seen.has(r.id);seed++)for(const id of originalRecipes(recipeStateAt(s,at),r.egg,r.toolId,r.ingredients,at,rng(seed)))seen.add(id);
    assert.ok(seen.has(r.id),`seeded sampler cannot reach ${r.key}`);
    const deny=(mutate,label)=>{const x=structuredClone(s);mutate(x);assert.equal(has(x),false,`${r.key} missing ${label}`);cases++;};
    for(const id of r.ingredients)deny(x=>x.selected=x.selected.filter(i=>i!==id),`ingredient ${id}`);
    if(r.minLevel>0)deny(x=>x.toolLevels[r.toolId]=r.minLevel-1,'tool level');
    if(r.collectionTotal)deny(x=>x.total={[r.egg+':0']:r.collectionTotal[1]-1},'lifetime collection');
    if(r.campaign)deny(x=>delete x.events[r.campaign],'campaign');
    if(r.minKitchen>0)deny(x=>x.kitchenLevel=r.minKitchen-1,'kitchen level');
    if(r.time){const other=new Date(at);other.setHours(r.time==='day-phoenix'?15:11);assert.equal(ids(s,r.toolId,+other).includes(r.id),false,`${r.key} time`);cases++;}
    if(holidayForCharacter(r.egg,r.id,at)){const after=holidayForCharacter(r.egg,r.id,at).end;assert.equal(ids(s,r.toolId,after).includes(r.id),false,`${r.key} holiday`);cases++;}
  }
  console.log('Original recipe missing-condition checks:',cases);
});

test('all runtime identities, recipe ingredients and positive weights are valid and unique',()=>{
  for(const [egg,rows] of GAME_DATA.characters.entries()){
    assert.equal(new Set(rows.map(r=>r.id)).size,rows.length);
    for(const c of rows){assert.equal(E.char(egg,c.id),c);if(c.pack!=='regional')assert.ok(c.rate>0&&Number.isFinite(c.rate),`${egg}:${c.id} weight`);}
  }
  for(const rows of GAME_DATA.tools)assert.equal(new Set(rows.map(r=>r.id)).size,rows.length);
  for(const r of RECIPE_CATALOG){assert.ok(E.char(r.egg,r.id));if(r.toolId>=0)assert.ok(E.tool(r.toolId));for(const id of r.ingredients)assert.ok(E.ingredient(id),`${r.key} ingredient ${id}`);}
});

test('locked duck egg cannot start production or spend resources',()=>{
  const s=E.freshState(NOW,1);s.egg=1;const before=structuredClone(s);
  assert.throws(()=>E.startBatch(s,0,NOW,rng(1)),/鸭蛋/);assert.deepEqual(s,before);
});

test('failed persistence of a multi-condiment batch leaves resources and random ticket untouched',()=>{
  const s=prepared();s.selected=[1,2];s.ingredients={1:1,2:1};const before=structuredClone(s);
  assert.throws(()=>execute({state:s,store:{write(){throw Error('disk full');}},command:{type:'cook'},now:NOW,reduce:x=>E.startBatch(x,1,NOW)}),/disk full/);
  assert.deepEqual(s,before);
  const run=()=>execute({state:s,command:{type:'cook'},now:NOW,reduce:x=>E.startBatch(x,1,NOW)}).state;
  const a=run(),b=run();assert.deepEqual(a.batch.plan.initialIds,b.batch.plan.initialIds);assert.deepEqual(a.batch.eggs.map(e=>e.tickets),b.batch.eggs.map(e=>e.tickets));
});

test('all 48 regional recipes produce their target only when every required gate is met',()=>{
  const base=prepared();base.total=Object.fromEntries(GAME_DATA.characters.flatMap((rows,egg)=>rows.filter(c=>c.pack!=='regional').map(c=>[`${egg}:${c.id}`,100])));
  for(const c of REGIONAL.cards)base.expansion.discovery.cards[c.id]=++base.meta.factSeq;
  for(const m of REGIONAL.materials)base.expansion.discovery.identified[m.id]=++base.meta.factSeq;
  base.expansion.methods.full=REGIONAL.recipes.map(r=>r.id);base.expansion.methods.directions=[...base.expansion.methods.full];
  base.expansion.regions.guideFlags=['GUIDE-B'];base.expansion.regions.introSpecimenDone=['V','R','T','B'];
  let negatives=0;
  for(const r of REGIONAL.recipes){
    const s=structuredClone(base);s.ingredients=Object.fromEntries(r.ingredients.map(m=>[m.id,m.count??1]));prepareRegionalRecipe(s,r.id);
    const b=E.startBatch(s,r.toolId,NOW,()=>0,()=>.5);assert.ok(b.eggs.some(e=>`${e.egg}:${e.id}`===r.key),r.id);
    const restored=E.normalizeSave(JSON.parse(JSON.stringify(s)),NOW);assert.deepEqual(restored.batch.plan,b.plan);
    E.updateBatch(restored,b.ends+1);E.updateBatch(restored,b.ends+2001);E.updateBatch(restored,b.ends+2901);
    for(let i=0;i<24;i++)E.collect(restored,i,b.ends+2901);assert.equal(restored.farm[r.key],1,r.id+' inventory');
    const deny=(mutate,label)=>{
      const x=structuredClone(base);x.ingredients=Object.fromEntries(r.ingredients.map(m=>[m.id,m.count??1]));prepareRegionalRecipe(x,r.id);mutate(x);const before=structuredClone(x);
      assert.throws(()=>E.startBatch(x,r.toolId,NOW,()=>0),undefined,`${r.id} missing ${label}`);assert.deepEqual(x,before);negatives++;
    };
    // A one-ingredient direction is its whole method, so withdrawing the method withdraws both.
    deny(x=>{x.expansion.methods.full=x.expansion.methods.full.filter(id=>id!==r.id);if(r.ingredients.length===1)x.expansion.methods.directions=x.expansion.methods.directions.filter(id=>id!==r.id);},'method');
    deny(x=>x.toolLevels[r.toolId]=r.toolLevel-1,'cookware');
    if(r.kitchenLevel>0)deny(x=>x.kitchenLevel=r.kitchenLevel-1,'kitchen');
    if(r.egg===1)deny(x=>x.duck=false,'duck access');
    for(const m of r.ingredients)deny(x=>x.ingredients[m.id]=0,'ingredient '+m.id);
    const u=resolveSpecies(r.key).unlock;
    for(const id of u.identifiedMaterials)deny(x=>delete x.expansion.discovery.identified[id],'identified '+id);
    if(u.card)deny(x=>delete x.expansion.discovery.cards[u.card],'discovery card');
    if(u.firstSpecimenForOldOnly)deny(x=>x.expansion.regions.introSpecimenDone=[],'intro specimen');
    deny(x=>{x.total={};x.farm={};},'region progress');
  }
  console.log('48 regional recipes; missing-condition rejections:',negatives);
});

test('all 16 seasonal recipes enforce ingredients, cookware, kitchen, discoveries and duck access at actual start',()=>{
  for(const c of SEASONAL_CHARACTERS){
    const s=prepared(c.egg);s.total=Object.fromEntries(Array.from({length:12},(_,i)=>['0:'+i,1]));s.total[c.key]=1;s.ingredients=Object.fromEntries(c.ingredients.map(id=>[id,1]));
    prepareSeasonalRecipe(s,c.key);const valid=structuredClone(s);assert.ok(E.startBatch(valid,c.toolId,NOW,rng(7)).eggs.some(e=>e.id===c.id));
    for(const change of [x=>x.kitchenLevel=0,x=>x.total={[c.key]:1},x=>x.toolLevels[c.toolId]=c.minLevel-1,...c.ingredients.map(id=>x=>x.ingredients[id]=0),...(c.egg?[x=>x.duck=false]:[])]){
      const x=structuredClone(s);change(x);
      try{const b=E.startBatch(x,c.toolId,NOW,()=>0);assert.equal(b.eggs.some(e=>e.id===c.id),false,c.key+' blocked target');}
      catch(error){if(error.code==='ERR_ASSERTION')throw error;assert.match(error.message,/鸭蛋|购买/);}
    }
  }
});

test('expired eggs cannot be collected as fresh during the hatching animation after offline resume',()=>{
  let s=prepared();E.startBatch(s,1,NOW,rng(19));const expired=Math.max(...s.batch.eggs.map(e=>e.blackAt))+1;
  s=E.normalizeSave(JSON.parse(JSON.stringify(s)),expired);E.resume(s,expired);E.updateBatch(s,expired);E.updateBatch(s,expired+2001);
  assert.ok(s.batch.eggs.every(e=>e.status==='hatching'));
  for(let i=0;i<24;i++)E.collect(s,i,expired+2001);
  assert.ok(Object.keys(s.farm).every(k=>['0:2','0:35','0:54'].includes(k)),JSON.stringify(s.farm));
});
