import {createRequire} from 'node:module';
import {mkdir,writeFile,readFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
const require=createRequire(import.meta.url),{chromium}=require(process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const base='web/prototypes/farm-modular-asset-gate-20260928',out=base+'/evidence';await mkdir(out,{recursive:true});
const browser=await chromium.launch({headless:true,executablePath:'C:/Program Files/Google/Chrome/Application/chrome.exe'});
try{
 const page=await browser.newPage({viewport:{width:1320,height:1000},deviceScaleFactor:1}),errors=[],requests=[];
 page.on('pageerror',e=>errors.push(e.message));page.on('console',e=>{if(e.type()==='error')errors.push(e.text());});page.on('response',r=>{if(r.status()>=400)requests.push({url:r.url(),status:r.status()});});
 await page.goto('http://127.0.0.1:4173/'+base+'/index.html?capture');await page.waitForFunction(()=>window.assetGate?.ready);await page.evaluate(()=>document.fonts.ready);await page.waitForLoadState('networkidle');
 const checks=[];
 assert.equal(await page.locator('#buildings .asset-card').count(),4);assert.equal(await page.locator('#environment .asset-card').count(),14);
 for(const [key,count] of [['a',3],['b',2]]){
  const s=page.locator(`#test-${key} .screen`),r=await s.boundingBox();assert.equal(r.width,390);assert.equal(r.height,844);
  assert.equal(await s.locator('.character').count(),count);assert.equal(await s.locator('.building').count(),2);
  const images=await s.locator('image').evaluateAll(nodes=>nodes.map(n=>n.getAttribute('href')));assert(!images.some(p=>/b-scene-gate|tata|scene-art/.test(p)));
  const depth=await s.locator('.object').evaluateAll(nodes=>nodes.every(n=>Number(getComputedStyle(n).zIndex)===Math.round(Number(n.dataset.footY))));assert(depth);
  checks.push({test:key,characters:count,buildings:2,individualSprites:images.length,footDepthSorted:depth,noFullSceneImage:true});
 }
 await page.locator('#asset-board').screenshot({path:out+'/asset-language-board.png'});
 await page.locator('#tests').screenshot({path:out+'/tests-side-by-side.png'});
 await page.getByRole('button',{name:'隐藏建筑层'}).click();assert.equal(await page.locator('.world .building:visible').count(),0);assert.equal(await page.locator('.character:visible').count(),5);assert.equal(await page.locator('.terrain:visible').count(),2);
 await page.locator('#tests').screenshot({path:out+'/layers-without-buildings.png'});await page.getByRole('button',{name:'显示建筑层'}).click();
 await page.getByRole('button',{name:'显示脚点'}).click();assert(await page.locator('.anchor:visible').count()>35);await page.locator('#tests').screenshot({path:out+'/anchor-debug.png'});await page.getByRole('button',{name:'隐藏脚点'}).click();
 await page.addStyleTag({content:'body[data-export]{width:390px;height:844px;overflow:hidden}body[data-export] .test{display:none}body[data-export=a] #test-a,body[data-export=b] #test-b{display:block}body[data-export] .screen{position:fixed;left:0;top:0;z-index:9999;box-shadow:none}'});
 await page.setViewportSize({width:390,height:844});
 for(const key of ['a','b']){await page.evaluate(key=>document.body.dataset.export=key,key);await page.screenshot({path:out+`/test-${key}.png`});}
 await page.evaluate(()=>delete document.body.dataset.export);await page.setViewportSize({width:1320,height:1000});
 await page.evaluate(()=>{for(const n of document.querySelectorAll('.pending')){const img=new Image();img.src=n.dataset.src;img.alt=n.previousElementSibling.textContent;n.replaceWith(img);}});
 await page.locator('#comparison img').evaluateAll(imgs=>Promise.all(imgs.map(im=>im.decode())));await page.locator('#comparison').screenshot({path:out+'/comparison.png'});
 assert.equal(errors.length,0,errors.join('\n'));assert.equal(requests.length,0,JSON.stringify(requests));
 const kit=JSON.parse(await readFile(base+'/asset-manifest.json','utf8'));for(const id of ['house','shop','shrine','display'])assert(kit[id].alphaZeroRatio>.08);
 await writeFile(out+'/verification.json',JSON.stringify({capturedAt:new Date().toISOString(),assets:18,buildingAssets:4,vectorEnvironmentAssets:14,checks,alphaVerified:true,buildingLayerToggle:true,anchorToggle:true,errors,failedRequests:requests,scope:'Local static modular tests only; no Runtime or zoom/pan integration.'},null,2));
 console.log('PASS: 18 independent assets, 2 assembled tests, 3+2 existing characters, no full-scene images in test worlds, independent layers, alpha and foot-depth checks; 0 browser errors.');
}finally{await browser.close();}
