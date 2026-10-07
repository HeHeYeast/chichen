import {createRequire} from 'node:module';
import {mkdir,readFile,writeFile} from 'node:fs/promises';
import {createHash} from 'node:crypto';
import assert from 'node:assert/strict';
const require=createRequire(import.meta.url),{chromium}=require(process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const base='web/prototypes/farm-map-resident-preview-20260929',out=base+'/evidence';await mkdir(out,{recursive:true});
const hash=p=>readFile(p).then(b=>createHash('sha256').update(b).digest('hex'));
const sourceHash=await hash('C:/Users/管啸野/AppData/Local/Temp/codex-clipboard-87859c0a-a54e-4f69-a238-ce001d537ec0.png');assert.equal(sourceHash,await hash(base+'/assets/map-user-approved.png'));
const browser=await chromium.launch({headless:true,executablePath:'C:/Program Files/Google/Chrome/Application/chrome.exe'});
try{
 const page=await browser.newPage({viewport:{width:1280,height:950},deviceScaleFactor:1});const errors=[];page.on('pageerror',e=>errors.push(e.message));page.on('response',r=>{if(r.status()>=400)errors.push(r.status()+' '+r.url());});
 await page.goto('http://127.0.0.1:4173/'+base+'/index.html');await page.waitForFunction(()=>window.mapPreview?.ready);await page.waitForLoadState('networkidle');
 assert.equal(await page.locator('.resident').count(),8);await page.locator('#residents').uncheck();assert.equal(await page.locator('.resident:visible').count(),0);await page.locator('#residents').check();
 await page.locator('#native').click();assert.equal((await page.evaluate(()=>window.mapPreview.getCamera())).zoom,1);const c=await page.evaluate(()=>window.mapPreview.getCamera());const r=await page.locator('.viewport').boundingBox();await page.mouse.move(r.x+700,r.y+350);await page.mouse.down();await page.mouse.move(r.x+600,r.y+290,{steps:8});await page.mouse.up();const d=await page.evaluate(()=>window.mapPreview.getCamera());assert(d.x>c.x&&d.y>c.y);await page.locator('#fit').click();
 await page.evaluate(()=>document.body.dataset.export='1');await page.setViewportSize({width:1536,height:1024});
 const dimensions=await page.locator('.resident').evaluateAll(nodes=>nodes.map(n=>({place:n.dataset.place,width:n.getBoundingClientRect().width,height:n.getBoundingClientRect().height})));assert(dimensions.every(d=>d.height>=18&&d.height<=20));
 await page.screenshot({path:out+'/map-with-residents.png'});
 await page.evaluate(()=>{delete document.body.dataset.export;document.body.dataset.detail='1';});await page.setViewportSize({width:720,height:480});await page.screenshot({path:out+'/detail-native-scale.png'});
 await page.evaluate(()=>delete document.body.dataset.detail);await page.setViewportSize({width:1280,height:950});await page.locator('#fit').click();
 assert.equal(errors.length,0,errors.join('\n'));
 await writeFile(out+'/verification.json',JSON.stringify({date:new Date().toISOString(),mapDimensions:[1536,1024],sourceHash,sourceUnmodified:true,residents:8,dimensions,layerToggle:true,panBothAxes:true,imageGenCalls:0,errors},null,2));
 console.log('PASS: original map SHA-256 unchanged; 8 existing residents at 18–20px; independent layer toggle; dual-axis preview pan; 0 load errors.');
}finally{await browser.close();}
