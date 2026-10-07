// Same saves, viewport, original artwork and clock for before/after composition.
import {createRequire} from 'node:module';
import {spawn} from 'node:child_process';
import {readFile,writeFile,mkdir} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {departRegional} from '../web/regional-exploration.js';
const require=createRequire(import.meta.url),{chromium}=require(process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const phase=process.argv[2]??'after',root=process.argv[3]??'artifacts/journey-convergence',out=`${root}/${phase}`,now=1800000000000;
await mkdir(out,{recursive:true});
const fixture=async key=>JSON.parse(await readFile(`artifacts/journey-family/runtime/${key}.json`,'utf8'));
const states={open:await fixture('open'),return:await fixture('newReturn'),locked:await fixture('locked')};
states.running=structuredClone(states.open);departRegional(states.running,{regionId:'V',placeId:'V:0',focus:'specimen',members:['0:0','0:3','1:0']},now-1440000);
const server=spawn(process.execPath,['server.mjs','0'],{stdio:['ignore','pipe','pipe'],windowsHide:true});
let browser,context,page;const errors=[],shots=[];
const selectors={header:'.journey-header',primaryArt:'.journey-map-art,.journey-places,.journey-found-object',secondaryAction:'.journey-map-destination,.journey-actions,.journey-focus,.journey-loot-items',dynamicCharacter:'.journey-travellers,.journey-team,.journey-return-party',CTA:'[data-journey-enter],[data-regional-depart],[data-regional-claim]',bottomNav:'#main-nav',target:'.journey-target',ribbon:'.journey-ribbon'};
async function load(key,width=390,height=844){
 await context?.close();context=await browser.newContext({viewport:{width,height},reducedMotion:'reduce'});
 if(process.env.JOURNEY_REPLAY_OLD==='1')for(const file of ['regional-ui.js','journey.css','journey-art.js'])await context.route(`**/web/${file}`,route=>route.fulfill({path:`artifacts/journey-convergence/before/${file}`,contentType:file.endsWith('.js')?'text/javascript':'text/css'}));
 await context.addInitScript(({s,now})=>{Date.now=()=>now;if(location.protocol==='http:')localStorage.setItem('chick-kitchen-v1',JSON.stringify(s));},{s:states[key],now});
 page=await context.newPage();page.on('pageerror',e=>errors.push(e.message));page.on('response',r=>{if(r.status()>=400)errors.push(r.url());});
 await page.goto(url);await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.waitForTimeout(750);await page.locator('#main-nav').getByRole('button',{name:'寻访',exact:true}).click();
}
async function shot(name){
 await page.evaluate(()=>document.fonts.ready);await page.waitForTimeout(120);
 const bounds={};for(const [key,selector]of Object.entries(selectors)){const el=page.locator(selector).first();if(await el.count())bounds[key]=await el.boundingBox();}
 const scroll=await page.locator('.journey-body').evaluate(e=>({width:e.clientWidth,height:e.clientHeight,scrollHeight:e.scrollHeight,scrollWidth:e.scrollWidth,top:e.scrollTop}));
 assert.ok(scroll.scrollWidth<=scroll.width+1,name+' horizontal overflow');
 const flow=await page.locator('.journey-body').evaluate(e=>[...e.querySelectorAll(':scope > *, .journey-loot > *')].map(n=>{const r=n.getBoundingClientRect(),s=getComputedStyle(n);return {class:n.className,y:r.y,height:r.height,margin:s.margin,padding:s.padding,minHeight:s.minHeight};}));
 await page.screenshot({path:`${out}/${name}.png`});shots.push({name,viewport:page.viewportSize(),bounds,scroll,flow});
}
let url;
try{
 url=await new Promise((yes,no)=>{server.on('error',no);server.stdout.on('data',b=>{const m=String(b).match(/http:\/\/127\.0\.0\.1:\d+/);if(m)yes(m[0]);});});browser=await chromium.launch({headless:true});
 for(const [w,h]of [[390,844],[320,568],[430,932]]){
  await load('running',w,h);await shot(`map-${w}`);
  if(phase==='after'&&w===320){await page.locator('.journey-body').evaluate(e=>e.scrollTop=e.scrollHeight);await shot('map-lower-320');const landmark=await page.locator('[data-regional-region="B"]').boundingBox(),overlay=await page.locator('.journey-map-destination').boundingBox();assert.ok(landmark.y+landmark.height<=overlay.y,'small-phone salt node is reachable above overlay after scrolling');}
  if(phase==='after'&&w>=390){const m=shots.at(-1);assert.ok(m.bounds.primaryArt.height/h>.8);assert.ok(m.bounds.secondaryAction.y>m.bounds.primaryArt.y);assert.ok(m.bounds.secondaryAction.y+m.bounds.secondaryAction.height<=m.bounds.bottomNav.y);for(const node of await page.locator('.journey-node').all()){const b=await node.boundingBox();assert.ok(b.y+b.height<m.bounds.secondaryAction.y,'all four labels remain above the floating action');}}
  await load('open',w,h);await shot(`idle-map-${w}`);await page.locator('[data-journey-enter]').click();
  for(const [i,key]of ['0:0','0:3','1:0'].entries()){await page.locator(`[data-regional-slot="${i}"]`).click();await page.locator(`[data-regional-member="${key}"]`).click();}
  await page.locator('.journey-body').evaluate(e=>e.scrollTop=0);await shot(`region-${w}`);
  if(phase==='after'&&w>=390){const r=shots.at(-1);assert.ok(r.scroll.scrollHeight<=r.scroll.height+1,'standard phone fits region composition');assert.ok(r.bounds.primaryArt.y+r.bounds.primaryArt.height<=r.bounds.secondaryAction.y);assert.ok(r.bounds.secondaryAction.y+r.bounds.secondaryAction.height<=r.bounds.target.y);assert.ok(r.bounds.target.y+r.bounds.target.height<=r.bounds.dynamicCharacter.y);assert.ok(r.bounds.dynamicCharacter.y+r.bounds.dynamicCharacter.height<=r.bounds.CTA.y+1);}
  if(w===320){await page.locator('[data-regional-slot="2"]').scrollIntoViewIfNeeded();await shot(`region-team-${w}`);}
  await load('return',w,h);await page.locator('[data-journey-enter]').click();await shot(`return-${w}`);
  if(phase==='after'){assert.equal(await page.locator('.journey-discovery p').count(),0,'story belongs in discovery details');const r=shots.at(-1);assert.ok(r.bounds.CTA.y+r.bounds.CTA.height<=r.bounds.bottomNav.y);if(w>=390)assert.ok(r.scroll.scrollHeight<=r.scroll.height+1,'standard phone fits discovery composition');}
  if(w===320){await page.locator('.journey-loot').scrollIntoViewIfNeeded();await shot(`return-loot-${w}`);}
 }
 assert.deepEqual(errors,[]);
}finally{await browser?.close();server.kill();await writeFile(`${out}/layout.json`,JSON.stringify({phase,errors,shots},null,2));console.log(JSON.stringify({phase,screens:shots.length,errors}));}
