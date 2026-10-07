import {createRequire} from 'node:module';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
const require=createRequire(import.meta.url);
const {chromium}=require(process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const out='web/prototypes/farm-b-scene-gate-20260928/deliverables';await mkdir(out,{recursive:true});
const browser=await chromium.launch({headless:true,executablePath:'C:/Program Files/Google/Chrome/Application/chrome.exe'});
try{
 const page=await browser.newPage({viewport:{width:1320,height:1120},deviceScaleFactor:1});const errors=[],failed=[];
 page.on('pageerror',e=>errors.push(e.message));page.on('response',r=>{if(r.status()>=400)failed.push({url:r.url(),status:r.status()});});
 await page.goto('http://127.0.0.1:4173/web/prototypes/farm-b-scene-gate-20260928/index.html');
 await page.waitForFunction(()=>window.sceneGate?.ready);await page.evaluate(()=>document.fonts.ready);
 await page.locator('.scene-art').evaluateAll(imgs=>Promise.all(imgs.map(im=>im.decode())));
 const checks=[];
 for(const id of ['b1','b2','b3']){
  const screen=page.locator(`#${id} .game-screen`),r=await screen.boundingBox();assert.equal(r.width,390);assert.equal(r.height,844);
  assert.equal(await screen.locator('.building-label').count(),4);assert.equal(await screen.locator('.game-nav>span').count(),5);
  const dims=await screen.locator('.scene-art').evaluate(img=>({w:img.naturalWidth,h:img.naturalHeight,loaded:img.complete}));assert(dims.loaded&&dims.w>900&&dims.h>1600);
  checks.push({id,size:[r.width,r.height],art:dims,labels:4,nav:5});
 }
 // Screenshot the three complete screens together; notes remain in the review board.
 await page.addStyleTag({content:'.candidate-notes,.annotation-key{display:none!important}'});
 await page.locator('#candidates').screenshot({path:`${out}/b-scene-comparison.png`});
 await page.getByRole('button',{name:'显示建筑位置说明'}).click();assert.equal(await page.locator('.pin:visible').count(),18);
 // Export from an exact viewport at integer origin, avoiding locator clipping
 // rounding a fractional document offset into an extra row of pixels.
 await page.addStyleTag({content:`body[data-export]{width:390px;height:844px;overflow:hidden;margin:0}body[data-export] .candidate{display:none}body[data-export=b1] #b1,body[data-export=b2] #b2,body[data-export=b3] #b3{display:block}body[data-export] .game-screen{position:fixed;top:0;left:0;box-shadow:none}body[data-export] .board-head,body[data-export] .candidate-head,body[data-export] .bottom-note{display:none}`});
 await page.setViewportSize({width:390,height:844});
 for(const annotated of [false,true])for(const id of ['b1','b2','b3']){
  await page.evaluate(({id,annotated})=>{document.body.dataset.export=id;document.body.classList.toggle('annotated',annotated);},{id,annotated});
  await page.screenshot({path:`${out}/${id}${annotated?'-annotated':''}-390x844.png`});
 }
 assert.equal(errors.length,0);assert.equal(failed.length,0);
 await writeFile(`${out}/verification.json`,JSON.stringify({capturedAt:new Date().toISOString(),checks,annotationPins:18,pageErrors:errors,failedRequests:failed,scope:'Static scene gate; no Runtime state or camera behavior integrated.'},null,2));
 console.log('PASS: 3 complete 390x844 screens + 3 annotated screens + comparison; 18 annotation pins; 0 page errors; 0 failed requests.');
}finally{await browser.close();}
