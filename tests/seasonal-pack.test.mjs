import test from 'node:test';
import assert from 'node:assert/strict';
import {readFileSync} from 'node:fs';
import * as E from '../web/engine.js';
import {DATA} from '../web/data.js';
import {GAME_DATA} from '../web/content-pack.js';
import {SEASONAL_CHARACTERS as CH,SEASONS,prepareSeasonalRecipe,seasonalRecipeInfo,plannedSeasonalRecipe,claimSeasonalChapter,seasonalChapterInfo} from '../web/seasonal-pack.js';
import {phoenixWindows,calendarNotice,seasonCalendar} from '../web/discovery-calendar.js';
import {originalRecipes} from '../web/recipes.js';
import {getActivities} from '../web/legacy-activities.js';
import {farmDisplay} from '../web/farm.js';
import {characterImage} from '../web/catalog.js';
import {resolveSprite} from '../web/art/manifest.js';
import {makeBackup,parseBackup} from '../web/save-store.js';
const NOW=new Date(2026,8,14,9,30).getTime();
const rng=(seed=7)=>()=>{seed=(Math.imul(1664525,seed)+1013904223)>>>0;return seed/4294967296;};
function ready(c=CH[0]){const s=E.freshState(NOW);s.kitchenLevel=3;s.toolLevels.fill(2);s.cp=25000;s.duck=true;for(let i=0;i<12;i++)s.total['0:'+i]=1;s.ingredients=Object.fromEntries(c.ingredients.map(i=>[i,2]));s.total[c.key]=1;return s;}
test('sixteen distinct sprites append eight chickens and eight ducks; all originals retain values',()=>{
  assert.deepEqual(GAME_DATA.characters.map(a=>a.length),[152,89]);assert.equal(CH.length,16);
  assert.equal(new Set(CH.map(c=>c.key)).size,16);assert.equal(new Set(CH.map(c=>c.title_zh_CN)).size,16);
  for(let egg=0;egg<2;egg++)assert.deepEqual(GAME_DATA.characters[egg].slice(0,DATA.characters[egg].length),DATA.characters[egg]);
  const frames=[];for(const c of CH){assert.equal(characterImage(c.egg,c.id),c.artwork);const a=resolveSprite(c.artwork);assert.ok(readFileSync('.'+a.file).length>10000);assert.equal(a.frame.length,4);assert.ok(a.frame[0]+a.frame[2]<=a.size[0]);assert.ok(a.frame[1]+a.frame[3]<=a.size[1]);frames.push(a.frame.join(','));}
  assert.equal(new Set(frames).size,16);
});
test('read-only recipe previews expose concrete gates; missing materials and duck access cannot spend or prepare',()=>{
  const s=E.freshState(NOW),before=structuredClone(s);assert.equal(seasonalRecipeInfo(s,CH[0].key).ready,false);assert.throws(()=>prepareSeasonalRecipe(s,CH[0].key));assert.deepEqual(s,before);
  for(const modify of [s=>s.duck=false,s=>s.kitchenLevel=0,s=>s.total={},s=>s.toolLevels[2]=-1,s=>s.ingredients={}]){
    const c=CH[2],s=ready(c);modify(s);const copy=structuredClone(s);assert.throws(()=>prepareSeasonalRecipe(s,c.key));assert.deepEqual(s,copy);
  }
});
test('preparing new chicken or duck recipes retains active opposite egg batch, stock and CP',()=>{
  for(const c of [CH[0],CH[2]]){const s=ready(c);s.egg=1-c.egg;s.selected=[];E.startBatch(s,1,NOW,rng());const before=structuredClone(s);
    prepareSeasonalRecipe(s,c.key);assert.equal(s.egg,c.egg);assert.deepEqual(s.selected,c.ingredients);assert.deepEqual(s.batch,before.batch);assert.deepEqual(s.ingredients,before.ingredients);assert.equal(s.cp,before.cp);assert.equal(s.events.seasonalRecipe,c.key);
  }
});
test('all sixteen actual recipes make independent targets, consume once, hatch, sell and retain collection',()=>{
  for(const c of CH){const s=ready(c);prepareSeasonalRecipe(s,c.key);const cp=s.cp,cost=E.cookInfo(s,c.toolId).cost;const batch=E.startBatch(s,c.toolId,NOW,rng());
    assert.equal(batch.eggs.length,24);assert.equal(batch.eggs.filter(e=>e.id===c.id).length>1,true);assert.equal(batch.egg,c.egg);assert.equal(batch.seasonalRecipe,c.key);assert.equal(s.cp,cp-cost);assert.equal(s.events.seasonalRecipe,undefined);c.ingredients.forEach(i=>assert.equal(s.ingredients[i],1));
    E.updateBatch(s,batch.ends+1,()=>.9);E.updateBatch(s,batch.ends+2002,()=>.9);E.updateBatch(s,batch.ends+2903,()=>.9);
    const i=batch.eggs.findIndex(e=>e.id===c.id);assert.ok(E.collect(s,i),c.key);assert.equal(E.sell(s,{[c.key]:1}),c.cp_1);assert.equal(s.total[c.key],2);assert.equal(s.farm[c.key],0);
  }
});
test('known and unknown handmade recipes share the same distribution without an explicit intent',()=>{
 for(const c of CH){const s=ready(c);s.egg=c.egg;s.selected=[...c.ingredients];const unknown=structuredClone(s);delete unknown.total[c.key];
 assert.deepEqual(E.startBatch(s,c.toolId,NOW,rng()).eggs.map(e=>e.id),E.startBatch(unknown,c.toolId,NOW,rng()).eggs.map(e=>e.id));}
});

test('edited ingredients or egg do not accidentally manufacture stale target and successful other cooking clears intent',()=>{
  const s=ready();prepareSeasonalRecipe(s,CH[0].key);s.selected=[];assert.equal(plannedSeasonalRecipe(s,2),null);assert.equal(E.startBatch(s,2,NOW,rng()).eggs.some(e=>e.id===120),false);assert.equal(s.events.seasonalRecipe,undefined);
  const b=ready();prepareSeasonalRecipe(b,CH[0].key);b.egg=1;assert.equal(plannedSeasonalRecipe(b,2),null);
});
test('failed start preserves new recipe intent and existing stock',()=>{
  const s=ready();prepareSeasonalRecipe(s,CH[0].key);s.cp=0;const before=structuredClone(s);assert.throws(()=>E.startBatch(s,2,NOW,rng()),/CP/);assert.deepEqual(s,before);
});
test('sixteen new partners remain visible in all farm time zones',()=>{
  const s=ready();for(const c of CH)s.farm[c.key]=2;
  for(const h of [6,12,18,22]){const d=new Date(NOW);d.setHours(h);const display=farmDisplay(s,d.getTime(),rng());for(const c of CH)assert.ok(display.some(w=>w.egg===c.egg&&w.id===c.id),c.key+' at '+h);}
});
test('chapter rewards count lifetime discoveries and cannot be replayed or mutate creatures',()=>{
  const s=ready();for(const c of CH)s.total[c.key]=1;const before=structuredClone(s);for(const season of SEASONS){assert.equal(seasonalChapterInfo(s,season.id).found,4);claimSeasonalChapter(s,season.id);assert.throws(()=>claimSeasonalChapter(s,season.id));}
  assert.equal(s.cp,before.cp+2400);assert.deepEqual(s.total,before.total);assert.deepEqual(s.farm,before.farm);
  const incomplete=ready(),copy=structuredClone(incomplete);assert.throws(()=>claimSeasonalChapter(incomplete,'winter'));assert.deepEqual(incomplete,copy);
});
test('new IDs, current batch, recipe choice, notices and one-time rewards roundtrip in v2 backups',()=>{
  const s=ready(CH[15]);for(const c of CH)s.total[c.key]=1;claimSeasonalChapter(s,'winter');prepareSeasonalRecipe(s,CH[15].key);s.events.discoveryNotices=false;E.startBatch(s,2,NOW,rng());
  prepareSeasonalRecipe(s,CH[15].key);const read=E.parseSave(JSON.stringify(s),NOW);assert.deepEqual(read,E.normalizeSave(s,NOW));const backup=parseBackup(makeBackup(s,NOW),NOW);assert.deepEqual(backup,E.normalizeSave(s,NOW));
  const old=E.freshState(NOW);assert.deepEqual(E.normalizeSave(old,NOW),old);
});
test('phoenix windows have exact local boundaries, pre-notice requires eligible tool and unknown species',()=>{
  const s=ready(),at=(h,m=0)=>new Date(2026,8,14,h,m).getTime();
  for(const [h,m,expected]of [[9,59,51],[10,0,52],[12,59,52],[13,0,51],[23,59,51],[0,0,51]])assert.equal(phoenixWindows(s,at(h,m)).find(w=>w.active).id,expected);
  assert.equal(calendarNotice(s,at(8,59)),null);assert.match(calendarNotice(s,at(9)).message,/60/);assert.match(calendarNotice(s,at(9,30)).message,/30/);assert.match(calendarNotice(s,at(12)).message,/凤凰/);
  s.total['0:52']=1;assert.equal(calendarNotice(s,at(9)),null);delete s.total['0:52'];s.toolLevels[0]=1;assert.equal(calendarNotice(s,at(9)),null);s.toolLevels[0]=2;s.events.discoveryNotices=false;assert.equal(calendarNotice(s,at(9)),null);
});
test('recommendation previews begin seven days before calendar season without locking recipes',()=>{
  assert.equal(seasonCalendar(new Date(2026,1,21).getTime()).preview,false);
  const spring=seasonCalendar(new Date(2026,1,22).getTime());assert.equal(spring.days,7);assert.equal(spring.next.id,'spring');assert.equal(spring.preview,true);
  assert.equal(seasonCalendar(new Date(2026,11,25).getTime()).current.id,'winter');assert.equal(seasonCalendar(new Date(2027,1,28).getTime()).days,1);
  const s=ready();assert.equal(seasonalRecipeInfo(s,CH[0].key).ready,true);
});
test('old seasonal and holiday commissions stay permanent and available independent of calendar date',()=>{
  const s=ready();s.total['0:0']=5000;
  const summer=getActivities(s,new Date(2026,6,1).getTime()).filter(a=>a.kind==='campaign').map(a=>[a.id,a.available]);
  const winter=getActivities(s,new Date(2026,11,25).getTime()).filter(a=>a.kind==='campaign').map(a=>[a.id,a.available]);assert.deepEqual(summer,winter);
});
