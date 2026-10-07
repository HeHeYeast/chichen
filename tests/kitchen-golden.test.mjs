import test from 'node:test';
import assert from 'node:assert/strict';
import {existsSync,readFileSync} from 'node:fs';
import {GOLDEN_LAYOUTS,KITCHEN_ART,goldenEgg,goldenEggHit,goldenRect,eggFloor,createGoldenKitchen} from '../web/kitchen-golden.js';
import {NESTS,EGG_ASPECT} from '../web/kitchen-egg-nests.js';
import {setViewportHeight} from '../web/theme.js';
import {characterImage} from '../web/catalog.js';
import {freshState,startBatch} from '../web/engine.js';

test('four stable 24-egg nests stay inside their bed, sorted back to front and never gridded',()=>{
 const layouts=[];
 for(let level=0;level<4;level++){
  const eggs=Array.from({length:24},(_,i)=>goldenEgg(level,i));layouts.push(JSON.stringify(eggs));
  assert.equal(new Set(eggs.map(e=>`${e.x},${e.y}`)).size,24);
  for(const [i,e] of eggs.entries()){
   assert.ok(e.x>=65&&e.x+e.w<=323);assert.ok(e.y+e.h<=eggFloor(level)+1e-9,'the rim hides at most the contact of the front eggs');
   assert.ok(Math.abs(e.w/e.h-EGG_ASPECT)<1e-9,'whole eggs keep the original sprite proportion');assert.ok(Math.abs(e.tilt)<=12);
   if(i)assert.ok(e.y+e.h>=eggs[i-1].y+eggs[i-1].h,'draw order and hit order run back to front');
  }
  // A natural nest: no six-egg rows sharing one baseline, and neighbours overlap.
  assert.ok(new Set(eggs.map(e=>Math.round(e.y+e.h))).size>=18);
  const overlaps=eggs.filter((a,i)=>eggs.some((b,j)=>i!==j&&a.x<b.x+b.w&&b.x<a.x+a.w&&a.y<b.y+b.h&&b.y<a.y+a.h)).length;assert.equal(overlaps,24);
  assert.deepEqual(eggs,Array.from({length:24},(_,i)=>goldenEgg(level,i)));
 }
 assert.equal(new Set(layouts).size,4);assert.equal(NESTS.length,4);
});
test('visual mapping leaves old saved egg coordinates and RNG untouched',()=>{
 const s=freshState(1800000000000,41);startBatch(s,0,s.lastSeen,()=>.5,()=>.3);const before=structuredClone(s);
 for(let level=0;level<4;level++)for(let i=0;i<24;i++)goldenEggHit(level,i);
 assert.deepEqual(s,before);
});
test('resized hit targets agree with rendering and preserve front-to-back rows',()=>{
 for(const height of [568,687,823,866]){
  setViewportHeight(height);
  for(let level=0;level<4;level++){
   const area=goldenRect('eggArea',level);
   for(let i=0;i<24;i++){const r=goldenEggHit(level,i);assert.ok(r.x>=area.x&&r.x+r.w<=area.x+area.w);assert.ok(r.y>=area.y&&r.y+r.h<=area.y+area.h);if(i)assert.ok(r.y+r.h>=goldenEggHit(level,i-1).y+goldenEggHit(level,i-1).h-1e-9);}
  }
 }
 setViewportHeight(568);
});
test('every preloaded cut is present and declared for offline packaging; no full mockup ships',()=>{
 const manifest=JSON.parse(readFileSync(new URL('../web/art/golden-kitchen/manifest.json',import.meta.url),'utf8'));
 assert.equal(KITCHEN_ART.length,35);
 for(const asset of KITCHEN_ART){assert.ok(existsSync(new URL('..'+asset,import.meta.url)));assert.ok(manifest.extracted.some(a=>a.path===asset));assert.doesNotMatch(asset,/reference|mockup|full/);}
 assert.match(readFileSync(new URL('../android/package-runtime.mjs',import.meta.url),'utf8'),/kitchenGolden\.extracted/);
});
test('eggs keep their shape on short and tall screens',()=>{
 for(const height of [568,687,823,866]){
  setViewportHeight(height);
  for(let level=0;level<4;level++)for(let i=0;i<24;i++){
   const raw=goldenEgg(level,i),hit=goldenEggHit(level,i);
   assert.ok(Math.abs(hit.w/hit.h-raw.w/raw.h)<1e-9,'room height must never flatten or stretch an egg');
  }
 }
 setViewportHeight(568);
});
test('hatched chicks keep their square art cell on short and tall screens',()=>{
 for(const height of [568,687,823,866]){
  setViewportHeight(height);
  let sx=1,sy=1;const chicks=[];
  const noop=()=>({width:10,addColorStop(){}});
  const ctx=new Proxy({},{get:(o,k)=>k in o?o[k]:k==='scale'?(x,y)=>{sx*=x;sy*=y;}:noop,set:(o,k,v)=>(o[k]=v,true)});
  const draw=createGoldenKitchen(ctx,(path,x,y,w,h)=>{if(path===characterImage(0,0))chicks.push([w*sx,h*sy]);});
  const s=freshState(1800000000000,41);startBatch(s,0,s.lastSeen,()=>.5,()=>.3);
  s.batch.eggs.forEach((e,i)=>{e.status=i%2?'ready':'hatching';});
  for(let level=0;level<4;level++){s.kitchenLevel=level;sx=sy=1;chicks.length=0;
   draw(s,{now:s.lastSeen,toolScroll:0,flights:[],reducedMotion:true});
   assert.equal(chicks.length,24);
   for(const [w,h] of chicks)assert.ok(Math.abs(w/h-1)<1e-9,`chick drawn ${w.toFixed(2)}x${h.toFixed(2)} at height ${height}`);
  }
 }
 setViewportHeight(568);
});
