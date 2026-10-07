import {createRequire} from 'node:module';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import * as E from '../../engine.js';
const require=createRequire(import.meta.url);
const {chromium}=require(process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const out='docs/farm-product-reset-20260928/evidence';await mkdir(out,{recursive:true});
const browser=await chromium.launch({headless:true,executablePath:'C:/Program Files/Google/Chrome/Application/chrome.exe'});
const errors=[],checks=[];const context=await browser.newContext({viewport:{width:1280,height:1000},deviceScaleFactor:1});
const page=await context.newPage();page.on('pageerror',e=>errors.push(e.message));
await page.goto('http://127.0.0.1:4173/web/prototypes/farm-product-reset-20260928/index.html');await page.locator('[data-direction=a][data-zoom="1.45"]').waitFor();
for(const key of ['a','b']){
 const card=page.locator(`[data-direction=${key}]`);
 await card.locator('[data-mode=far]').click();assert.equal(await card.getAttribute('data-zoom'),'1.00');
 assert.equal(await card.locator('.building:visible').count(),5);checks.push(`${key}: all 5 destinations visible in overview`);
}
await page.locator('.comparison').screenshot({path:`${out}/blueprints-overview.png`});
for(const key of ['a','b']){
 const card=page.locator(`[data-direction=${key}]`),view=card.locator('.map-viewport');
 await card.locator('[data-mode=near]').click();assert.equal(await card.getAttribute('data-zoom'),'2.20');
 await card.screenshot({path:`${out}/direction-${key}-near.png`});
 for(const id of ['house','shop','shrine','display','repair']){
  await card.locator('select').selectOption(id);await card.locator(`[data-node=${id}]`).click();
  assert.equal(await card.locator(`[data-node=${id}]`).getAttribute('aria-pressed'),'true');
  assert.match(await card.locator('.selection').innerText(),/未接入 Runtime/);
 }
 checks.push(`${key}: all 5 building locations and function descriptions`);
 await card.locator('[data-mode=near]').click();const before=await card.getAttribute('data-center');
 await view.scrollIntoViewIfNeeded();const r=await view.boundingBox();
 await page.mouse.move(r.x+r.width/2,r.y+r.height/2);await page.mouse.down();await page.mouse.move(r.x+r.width/2-65,r.y+r.height/2-75,{steps:8});await page.mouse.up();
 assert.notEqual(await card.getAttribute('data-center'),before);checks.push(`${key}: two-axis pan`);
 await view.focus();await page.keyboard.press('ArrowLeft');
 await card.locator('[data-mode=home]').click();assert.equal(await card.getAttribute('data-zoom'),'1.45');
 await card.locator('[data-mode=far]').click();await page.mouse.move(r.x+r.width*.5,r.y+r.height*.5);await page.mouse.wheel(0,-10000);await page.waitForTimeout(80);assert.equal(await card.getAttribute('data-zoom'),'2.40');
 await card.locator('[data-mode=far]').click();checks.push(`${key}: wheel upper bound, keyboard pan and reset`);
}
for(const width of [390,320]){
 await page.reload();await page.locator('[data-direction=a][data-zoom="1.45"]').waitFor();
 await page.setViewportSize({width,height:844});await page.waitForTimeout(80);
 assert(await page.evaluate(()=>document.documentElement.scrollWidth<=innerWidth));
 for(const key of ['a','b']){
  const card=page.locator(`[data-direction=${key}]`);await card.locator('[data-mode=far]').click();
  const small=await card.locator('.building:visible, .camera-tools button, .zoom-bar button').evaluateAll(nodes=>nodes.filter(n=>{const r=n.getBoundingClientRect();return r.width<43.9||r.height<43.9}).length);assert.equal(small,0);
  await card.screenshot({path:`${out}/direction-${key}-far-${width}.png`});
 }
 checks.push(`${width}px: no horizontal overflow; minimum 44px control and destination targets`);
}
// Touch pinch uses real browser input; no application internals or user state.
const touchContext=await browser.newContext({viewport:{width:390,height:844},hasTouch:true,isMobile:true});
const touchPage=await touchContext.newPage();touchPage.on('pageerror',e=>errors.push(e.message));
await touchPage.goto('http://127.0.0.1:4173/web/prototypes/farm-product-reset-20260928/index.html');
const touchCard=touchPage.locator('[data-direction=a]');await touchCard.locator('[data-mode=far]').click();
const touchView=touchCard.locator('.map-viewport');await touchView.scrollIntoViewIfNeeded();const tr=await touchView.boundingBox();
const cdp=await touchContext.newCDPSession(touchPage);const px=tr.x+tr.width/2,py=tr.y+tr.height/2;
await cdp.send('Input.dispatchTouchEvent',{type:'touchStart',touchPoints:[{x:px-35,y:py,id:1},{x:px+35,y:py,id:2}]});
await cdp.send('Input.dispatchTouchEvent',{type:'touchMove',touchPoints:[{x:px-80,y:py,id:1},{x:px+80,y:py,id:2}]});
await cdp.send('Input.dispatchTouchEvent',{type:'touchEnd',touchPoints:[]});
assert(Number(await touchCard.getAttribute('data-zoom'))>1);checks.push('touch: two-finger pinch increases zoom');await touchContext.close();
// Read-only current Runtime evidence, isolated localStorage fixture.
const runtimeContext=await browser.newContext({viewport:{width:390,height:844}});const now=Date.now();const state=E.freshState(now,731);state.cp=1240;state.farm['0:0']=3;state.total['0:0']=3;
await runtimeContext.addInitScript(s=>localStorage.setItem('chick-kitchen-v1',JSON.stringify(s)),state);
const runtime=await runtimeContext.newPage();await runtime.goto('http://127.0.0.1:4173/');await runtime.getByRole('button',{name:'开始游戏',exact:true}).click();await runtime.waitForTimeout(750);await runtime.locator('[data-control-id="nav:1"]').click();await runtime.locator('[data-control-id="farm:house"]').waitFor();await runtime.waitForTimeout(200);
await runtime.screenshot({path:`${out}/current-runtime-farm.png`});
const entries=[];
for(const id of ['farm:house','farm:harvest','farm:fortune','farm:repair']){
 const entry=runtime.locator(`[data-control-id="${id}"]`);entries.push({id,label:await entry.getAttribute('aria-label')});
}
await runtime.locator('[data-control-id="farm:house"]').click();const destination=(await runtime.locator('#panels').innerText()).slice(0,280);
checks.push('current Runtime: isolated Farm screenshot, visible entries and house destination');
assert.equal(errors.length,0);
await writeFile(`${out}/verification.json`,JSON.stringify({capturedAt:new Date().toISOString(),checks,pageErrors:errors,runtime:{fixture:'isolated fresh state, 3 base chickens, 1240 CP',entries,houseDestination:destination},scope:'Blueprint prototype only. No production files or real player save changed.'},null,2));
console.log(`PASS: ${checks.length} checks; ${errors.length} prototype page errors. Screenshots and current Runtime evidence saved.`);await browser.close();
