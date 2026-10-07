import test from 'node:test';
import assert from 'node:assert/strict';
import {readFileSync} from 'node:fs';
import vm from 'node:vm';
import {DATA} from '../web/data.js';
import * as E from '../web/engine.js';
import {HOLIDAYS,holidayWindow,holidayForCharacter,holidayCalendar,recipeStateAt} from '../web/holiday-calendar.js';
import {calendarNotice} from '../web/discovery-calendar.js';
import {characterAccessInfo} from '../web/legacy-activities.js';
import {discoveredRecipe,prepareDiscoveredRecipe} from '../web/recipe-book.js';
import {makeBackup,parseBackup} from '../web/save-store.js';
const at=(m,d,h=12,year=2026)=>new Date(year,m-1,d,h).getTime();
const source=readFileSync(new URL('../web/recipes.js',import.meta.url),'utf8');
const scope={DATA,samplePool:pool=>[...pool]};vm.createContext(scope);
vm.runInContext(source.slice(source.indexOf('export function originalRecipes')).replaceAll('export function','function'),scope);
function qualified(){const s=E.freshState(at(9,14));s.cp=100000;s.kitchenLevel=3;s.toolLevels.fill(2);s.duck=true;for(const h of HOLIDAYS)for(const key of h.keys)s.events['campaign_char_'+key.replace(':','_')]=true;return s;}
const pool=(s,now,egg=0,tool=0,ingredients=[])=>scope.originalRecipes(recipeStateAt(s,now),egg,tool,ingredients,now,()=>0);

test('reported September heat-lamp regression excludes carnival and mothers-day even after permanent qualification and discovery',()=>{
  const s=qualified();s.total['0:49']=1;s.total['0:60']=1;
  assert.ok(!pool(s,at(9,14)).includes(49));assert.ok(!pool(s,at(9,14)).includes(60));
  for(let seed=1;seed<=100;seed++){
    const copy=structuredClone(s);let n=seed;const rng=()=>((n=(Math.imul(n,1664525)+1013904223)>>>0)/2**32);
    assert.ok(E.startBatch(copy,0,at(9,14),rng).eggs.every(e=>e.id!==49&&e.id!==60));
  }
});
test('spring group members have independent dates; fools day expires exactly at local midnight',()=>{
  const s=qualified();
  assert.ok(pool(s,at(4,1)).includes(49));assert.ok(pool(s,at(4,1)).includes(48));assert.ok(!pool(s,at(4,1)).includes(60));
  assert.equal(holidayForCharacter(0,49,at(4,1,0)-1).active,false);
  assert.equal(holidayForCharacter(0,49,at(4,2,0)-1).active,true);
  assert.equal(holidayForCharacter(0,49,at(4,2,0)).active,false);
  assert.ok(pool(s,at(5,10)).includes(60));assert.ok(!pool(s,at(5,10)).includes(49));
  assert.equal(holidayForCharacter(0,60,at(5,11)).active,false);
});
test('actual new-batch path can produce the eligible holiday bird during its window',()=>{
  for(const [id,when]of [[49,at(4,1)],[60,at(5,10)]]){
    const s=E.freshState(when);s.events[`campaign_char_0_${id}`]=true;
    const batch=E.startBatch(s,0,when,()=>.999);
    assert.ok(batch.eggs.some(e=>e.id===id),`eligible species ${id} must remain obtainable`);
  }
});
test('annual recurrence handles weekdays, lunar dates, leap month exclusion and Gregorian leap years',()=>{
  for(const [id,year,month,day]of [['mothers-day',2026,5,10],['mothers-day',2027,5,9],['thanksgiving',2026,11,26],['qixi',2026,8,19],['mid-autumn',2026,9,25],['spring-festival',2026,2,17],
    // New moons within minutes of Beijing midnight, where ICU's approximation is a day off.
    ['spring-festival',2027,2,6],['spring-festival',2030,2,3]]){
    const w=holidayWindow(id,at(1,1,0,year));assert.equal(w.start,at(month,day,0,year));
  }
  const cny=holidayWindow('spring-festival',at(2,23));assert.equal(cny.active,true);assert.equal(cny.end,at(2,24,0));
  assert.equal(holidayWindow('plum',at(2,29,12,2028)).active,true);assert.equal(holidayWindow('plum',at(3,1,0,2028)).active,false);
  assert.equal(holidayWindow('new-year',at(12,31)).start,at(1,1,0,2027));
  assert.equal(holidayForCharacter(0,0),null);
});
test('every limited partner is gated and original ingredients, cookware, progress and campaign qualification still apply',()=>{
  const s=qualified();
  for(const h of HOLIDAYS){const when=holidayWindow(h,at(1,1,0)).start;for(const key of h.keys){const [egg,id]=key.split(':').map(Number),flag=`campaign_char_${egg}_${id}`;
    assert.equal(recipeStateAt(s,when).events[flag],true);
    assert.equal(recipeStateAt(s,holidayWindow(h,when).end).events[flag],false);
    const unqualified=structuredClone(s);delete unqualified.events[flag];assert.equal(recipeStateAt(unqualified,when).events[flag],undefined);
  }}
  assert.ok(!pool(s,at(12,24)).includes(26));assert.ok(pool(s,at(12,24),0,0,[15]).includes(26));
  assert.ok(!pool(s,at(2,14),0,0,[18]).includes(32));
  const u=E.freshState(at(4,1));assert.ok(!pool(u,at(4,1)).includes(49));
  s.events.campaign_char_0_88=true;s.events.campaign_char_0_105=true;
  assert.equal(recipeStateAt(s,at(9,14)).events.campaign_char_0_88,true);assert.equal(recipeStateAt(s,at(9,14)).events.campaign_char_0_105,true);
});
test('previews begin seven calendar days before opening and switch off with the existing discovery setting',()=>{
  const s=E.freshState(at(3,24));s.events.campaign_char_0_49=true;
  assert.equal(calendarNotice(s,at(3,24)),null);assert.match(calendarNotice(s,at(3,25)).message,/7 天/);
  assert.match(calendarNotice(s,at(4,1)).message,/愚人节开始啦/);assert.equal(calendarNotice(s,at(4,2)),null);
  s.events.discoveryNotices=false;assert.equal(calendarNotice(s,at(3,25)),null);delete s.events.discoveryNotices;
  s.total['0:49']=1;assert.equal(calendarNotice(s,at(3,25)),null);
  const windows=holidayCalendar(at(9,18));assert.equal(windows[0].id,'chestnut');assert.ok(windows.find(h=>h.id==='mid-autumn').preview);
});
test('cookbook and character hints agree with actual date gating without erasing recorded recipes or changing a batch',()=>{
  const s=qualified();s.total['0:49']=1;s.farm['0:49']=1;s.total['0:60']=1;
  assert.ok(discoveredRecipe(s,'0:49',at(9,14)));assert.equal(discoveredRecipe(s,'0:49',at(9,14)).ready,false);
  const before=structuredClone(s);assert.throws(()=>prepareDiscoveredRecipe(s,'0:49',at(9,14)),/愚人节/);assert.deepEqual(s,before);
  assert.equal(discoveredRecipe(s,'0:49',at(4,1)).ready,true);
  assert.equal(characterAccessInfo(0,49,s,at(9,14)).qualified,true);assert.equal(characterAccessInfo(0,49,s,at(9,14)).unlocked,false);
  assert.equal(characterAccessInfo(0,49,s,at(4,1)).unlocked,true);
  assert.equal(discoveredRecipe(s,'0:48',at(4,1)),null);
});
test('updating old saves or crossing a holiday boundary never rerolls an existing batch or revokes creatures and qualifications',()=>{
  const s=E.freshState(at(4,1));s.events.campaign_char_0_49=true;s.events.legacyActivityClaims={spring:{day:20260401,at:at(4,1),collected:48}};s.total['0:49']=2;s.farm['0:49']=1;
  E.startBatch(s,0,at(4,1,23),()=>.999);const before=structuredClone(s);
  recipeStateAt(s,at(4,2));holidayCalendar(at(4,2));discoveredRecipe(s,'0:49',at(4,2));assert.deepEqual(s,before);
  const restored=parseBackup(makeBackup(s,at(4,2)),at(4,2));assert.deepEqual(restored.batch,before.batch);assert.deepEqual(restored.events,before.events);assert.deepEqual(restored.farm,before.farm);
  E.updateBatch(restored,at(4,2,3),()=>.9);E.updateBatch(restored,at(4,2,3)+2001,()=>.9);E.updateBatch(restored,at(4,2,3)+2902,()=>.9);
  const index=restored.batch.eggs.findIndex(e=>e.id===49);assert.ok(E.collect(restored,index));assert.equal(restored.total['0:49'],3);
});
test('hatch countdown and reminders use actual uncollected egg completion plus animation, with legacy fallback',()=>{
  const batch={started:1000,ends:121000,eggs:[{openAt:30000,collected:false},{openAt:60000,collected:false},{openAt:100000,collected:true}]};
  assert.equal(E.batchReadyAt(batch),63000);batch.eggs[1].collected=true;assert.equal(E.batchReadyAt(batch),33000);
  batch.eggs[0].collected=true;assert.equal(E.batchReadyAt(batch),null);assert.equal(E.batchReadyAt(null),null);
  batch.eggs=[{collected:false}];assert.equal(E.batchReadyAt(batch),124000);
});
