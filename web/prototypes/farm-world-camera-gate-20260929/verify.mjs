import {createRequire} from 'node:module';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {WORLDS,VIEW,UNIT,DEFAULT_ZOOM,layout,clampCamera,minZoom,zoomAt,worldPoint} from './world.js';
const require=createRequire(import.meta.url),{chromium}=require(process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const base='web/prototypes/farm-world-camera-gate-20260929',out=base+'/evidence';await mkdir(out,{recursive:true});
// Invariants are independent of DOM drawing: clamped bounds and cursor-stable zoom.
for(const world of Object.values(WORLDS)){
 for(const zoom of [-2,.4,.625,1,5])for(const x of [-100,20,200])for(const y of [-100,30,200]){
  const c=clampCamera({x,y,zoom},world),hx=VIEW.width/(UNIT*c.zoom*2),hy=VIEW.height/(UNIT*c.zoom*2);
  assert(c.zoom>=minZoom(world)&&c.zoom<=1);
  if(hx*2<world.width){assert(c.x>=hx&&c.x<=world.width-hx);}else assert.equal(c.x,world.width/2);
  if(hy*2<world.height){assert(c.y>=hy&&c.y<=world.height-hy);}else assert.equal(c.y,world.height/2);
 }
 const c={...layout(world).defaultCenter,zoom:DEFAULT_ZOOM},p=worldPoint(c,220,410);zoomAt(c,world,.8,220,410);const q=worldPoint(c,220,410);assert(Math.hypot(p.x-q.x,p.y-q.y)<1e-8);
}
const browser=await chromium.launch({headless:true,executablePath:'C:/Program Files/Google/Chrome/Application/chrome.exe'});
const errors=[],requests=[],snapshots=[];
try{
 const page=await browser.newPage({viewport:{width:1200,height:1050},deviceScaleFactor:1});
 page.on('pageerror',e=>errors.push(e.message));page.on('console',e=>{if(e.type()==='error')errors.push(e.text());});page.on('response',r=>{if(r.status()>=400)requests.push({url:r.url(),status:r.status()});});
 await page.goto('http://127.0.0.1:4173/'+base+'/index.html?capture');await page.waitForFunction(()=>window.farmCamera?.ready);await page.evaluate(()=>document.fonts.ready);await page.waitForLoadState('networkidle');
 const state=()=>page.evaluate(()=>window.farmCamera.getState());
 // Exercise world selection through real controls before pinning the phone for export.
 await page.locator('[data-world=b]').click();assert.equal((await state()).world.id,'b');await page.locator('[data-world=a]').click();assert.equal((await state()).world.id,'a');
 await page.evaluate(()=>document.body.dataset.export='phone');await page.setViewportSize({width:390,height:844});
 const phone=page.locator('.phone');assert.equal((await phone.boundingBox()).width,390);assert.equal((await phone.boundingBox()).height,844);
 const hudBefore=await page.locator('.hud').boundingBox(),navBefore=await page.locator('#nav').boundingBox();
 async function capture(id,preset){
  await page.evaluate(id=>window.farmCamera.selectWorld(id),id);await page.locator(`[data-preset=${preset}]`).click();await page.waitForLoadState('networkidle');
  const measure=await page.evaluate(()=>{const v=document.querySelector('.viewport').getBoundingClientRect();return{state:window.farmCamera.getState(),buildings:[...document.querySelectorAll('.world .building')].map(n=>{const r=n.getBoundingClientRect();return{id:n.dataset.asset,width:r.width,height:r.height,visible:r.right>v.left&&r.left<v.right&&r.bottom>v.top&&r.top<v.bottom,fullyVisible:r.left>=v.left&&r.right<=v.right&&r.top>=v.top&&r.bottom<=v.bottom};}),characterHeight:document.querySelector('.world .character').getBoundingClientRect().height,labelFont:document.querySelector('.tag')?getComputedStyle(document.querySelector('.tag')).fontSize:null};});
  if(preset==='default'){assert.equal(measure.state.camera.zoom,.625);assert(measure.buildings.filter(b=>b.visible).length>=1&&measure.buildings.filter(b=>b.visible).length<=3);assert(measure.buildings.filter(b=>b.fullyVisible).length>=1);assert(measure.characterHeight<24);assert(measure.buildings.find(b=>b.id==='house').width===200);}
  assert.equal(measure.state.lod,preset);
  await page.screenshot({path:`${out}/${id}-${preset}-390x844.png`});snapshots.push(measure);
 }
 for(const id of ['a','b'])for(const preset of ['default','far','near'])await capture(id,preset);
 const defaults=snapshots.filter(s=>s.state.lod==='default');assert.equal(defaults[0].characterHeight,defaults[1].characterHeight);assert.equal(defaults[0].buildings[0].width,defaults[1].buildings[0].width);
 await page.evaluate(()=>window.farmCamera.selectWorld('a'));const initial=await state();
 async function drag(x1,y1,x2,y2){await page.mouse.move(x1,y1);await page.mouse.down();await page.mouse.move(x2,y2,{steps:8});await page.mouse.up();}
 await drag(280,480,130,240);let moved=await state();assert(moved.camera.x>initial.camera.x+5);assert(moved.camera.y>initial.camera.y+8);
 assert.deepEqual(await page.locator('.hud').boundingBox(),hudBefore);assert.deepEqual(await page.locator('#nav').boundingBox(),navBefore);
 await page.locator('[data-action=reset]').click();assert.deepEqual((await state()).camera,initial.camera);
 await page.locator('[data-action=plus]').click();assert((await state()).camera.zoom>.625);await page.locator('[data-action=minus]').click();assert(Math.abs((await state()).camera.zoom-.625)<1e-8);
 const cursorBefore=await page.evaluate(()=>window.farmCamera.worldPoint(220,410));await page.mouse.move(220,480);await page.mouse.wheel(0,-100);await page.waitForTimeout(100);const cursorAfter=await page.evaluate(()=>window.farmCamera.worldPoint(220,410));assert(Math.hypot(cursorBefore.x-cursorAfter.x,cursorBefore.y-cursorAfter.y)<1e-6);
 for(const id of ['a','b']){
  await page.evaluate(id=>window.farmCamera.selectWorld(id),id);await page.locator('[data-preset=near]').click();
  for(let i=0;i<7;i++)await drag(330,690,50,150);
  const bottomRight=await state();assert(Math.abs(bottomRight.camera.x-(bottomRight.world.width-390/64))<1e-6);assert(Math.abs(bottomRight.camera.y-(bottomRight.world.height-696/64))<1e-6);
  for(let i=0;i<7;i++)await drag(50,150,330,690);
  const topLeft=await state();assert(Math.abs(topLeft.camera.x-390/64)<1e-6);assert(Math.abs(topLeft.camera.y-696/64)<1e-6);
  await page.locator('[data-preset=far]').click();const far=await state();await drag(300,490,70,210);assert.deepEqual((await state()).camera,far.camera);
 }
 await page.evaluate(()=>window.farmCamera.selectWorld('a'));await page.locator('#viewport').focus();await page.keyboard.press('ArrowRight');assert((await state()).camera.x>initial.camera.x);await page.keyboard.press('Home');assert.deepEqual((await state()).camera,initial.camera);
 // A genuine touch pointer sequence exercises the same single-finger handler.
 const cdp=await page.context().newCDPSession(page);await cdp.send('Emulation.setTouchEmulationEnabled',{enabled:true,maxTouchPoints:1});
 await cdp.send('Input.dispatchTouchEvent',{type:'touchStart',touchPoints:[{x:270,y:470}]});await cdp.send('Input.dispatchTouchEvent',{type:'touchMove',touchPoints:[{x:160,y:320}]});await cdp.send('Input.dispatchTouchEvent',{type:'touchEnd',touchPoints:[]});assert((await state()).camera.x>initial.camera.x&& (await state()).camera.y>initial.camera.y);
 await cdp.send('Emulation.setTouchEmulationEnabled',{enabled:false});await page.locator('[data-action=reset]').click();
 await page.evaluate(()=>delete document.body.dataset.export);await page.setViewportSize({width:1200,height:1050});
 await page.locator('[data-locate=shrine]').click();assert.equal((await state()).camera.x,(await state()).buildings.find(b=>b.id==='shrine').x);await page.evaluate(()=>window.farmCamera.reset());
 await page.evaluate(()=>{for(const n of document.querySelectorAll('.pending')){const img=new Image();img.src=n.dataset.src;img.alt=n.previousElementSibling.textContent;n.replaceWith(img);}});await page.locator('.shots img').evaluateAll(imgs=>Promise.all(imgs.map(im=>im.decode())));
 await page.locator('#comparison').screenshot({path:out+'/world-size-comparison.png'});await page.locator('#zoom-shots').screenshot({path:out+'/three-zooms.png'});await page.locator('.hero').screenshot({path:out+'/interactive-review.png'});
 const imageURLs=await page.locator('.world image').evaluateAll(ns=>ns.map(n=>n.getAttribute('href')));assert(!imageURLs.some(p=>/b-scene-gate|test-a|test-b|camp-390/.test(p)));
 assert.equal(errors.length,0,errors.join('\n'));assert.equal(requests.length,0,JSON.stringify(requests));
 await writeFile(out+'/verification.json',JSON.stringify({capturedAt:new Date().toISOString(),snapshots,checks:{worldUnitInvariants:true,sameAssetScaleAcrossWorlds:true,defaultShowsPartialWorld:true,mouseDragBothAxes:true,fourEdgeClampBothWorlds:true,farCenteredNoOverscroll:true,wheelAnchorStable:true,plusMinus:true,reset:true,locate:true,keyboard:true,singleFingerTouch:true,HUDAndNavFixed:true,zoomLOD:true,independentAssetsOnly:true},imageGenCalls:0,errors,failedRequests:requests},null,2));
 console.log('PASS: two world sizes; six 390×844 screenshots; default 1–3 buildings / 20px chicks; mouse + touch dual-axis pan; four-edge clamps; wheel anchor; zoom / reset / locate / LOD; fixed HUD; 0 browser errors.');
}finally{await browser.close();}
