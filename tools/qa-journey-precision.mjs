// Same saves, viewport, original artwork and clock for before/after composition.
import {createRequire} from 'node:module';
import {spawn} from 'node:child_process';
import {readFile,writeFile,mkdir} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {departRegional} from '../web/regional-exploration.js';
const require=createRequire(import.meta.url),{chromium}=require(process.env.PLAYWRIGHT_MODULE??process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const phase=process.argv[2]??'final',root=process.argv[3]??'artifacts/journey-convergence',out=`${root}/${phase}`,now=1800000000000;
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
 assert.ok(await page.evaluate(async()=>{await Promise.all([400,500,700,900].map(w=>document.fonts.load(`${w} 16px "Journey Rounded"`)));return [400,500,700,900].every(w=>document.fonts.check(`${w} 16px "Journey Rounded"`));}),'all four real font faces available');
 if(name.startsWith('map-')){
  const curve=await page.locator('.journey-routes path.is-route-selected').getAttribute('d');assert.ok(curve.includes(' C '),'cubic route');
  const party=await page.locator('.journey-traveller').evaluateAll(es=>es.map(e=>{const r=e.getBoundingClientRect();return [r.x+r.width/2,r.y+r.height/2];}));
  assert.equal(party.length,3);for(let i=1;i<party.length;i++)assert.ok(Math.hypot(party[i][0]-party[i-1][0],party[i][1]-party[i-1][1])>24,'party follows route with spacing');
 }
 if(name.startsWith('return-'))assert.equal(await page.locator('.journey-reward-slot').count(),1,'one actual material means one reward slot');
 if(name.startsWith('region-')){const stats=await page.locator('.journey-team').innerText();assert.match(stats,/采集\s*4\s*·\s*发现\s*2/);assert.doesNotMatch(stats,/\b[GF]\s*\d/,'player stats use Chinese labels');}
 const elements=await page.locator('.journey-screen h1,.journey-screen h2,.journey-screen h3,.journey-screen button,.journey-screen strong,.journey-screen small,.journey-screen .journey-art,.journey-screen .journey-character,.regional-footer-caption,.journey-environment,.journey-traveller,.journey-section-title,.journey-ribbon,.journey-reward-slot,.journey-return-party,.journey-found-object,.journey-target,.journey-return-notes,#main-nav,#main-nav button,#main-nav svg').evaluateAll(els=>els.map(e=>{const b=e.getBoundingClientRect(),c=getComputedStyle(e);let visual={x:b.x,y:b.y,width:b.width,height:b.height};if(e.tagName==='svg'&&e.viewBox.baseVal.width){const v=e.viewBox.baseVal,ar=e.getAttribute('preserveAspectRatio')||'xMidYMid meet';if(ar!=='none'){const k=Math.min(b.width/v.width,b.height/v.height),w=v.width*k,h=v.height*k;visual={x:b.x+(b.width-w)/2,y:b.y+(ar.includes('YMax')?b.height-h:(b.height-h)/2),width:w,height:h};}}let textBounds=null;if(['H1','H2','STRONG','SMALL'].includes(e.tagName)){const nodes=[...e.childNodes].filter(n=>n.nodeType===3&&n.textContent.trim());if(nodes.length){const range=document.createRange();range.setStartBefore(nodes[0]);range.setEndAfter(nodes.at(-1));const t=range.getBoundingClientRect();textBounds={x:t.x,y:t.y,width:t.width,height:t.height};}}return {visual,textBounds,tag:e.tagName,class:e.getAttribute('class'),asset:e.getAttribute('data-asset'),text:e.textContent.trim().slice(0,65),x:b.x,y:b.y,width:b.width,height:b.height,font:c.fontFamily,size:c.fontSize,weight:c.fontWeight,lineHeight:c.lineHeight,color:c.color};})); const bounds={};for(const [key,selector]of Object.entries(selectors)){const el=page.locator(selector).first();if(await el.count())bounds[key]=await el.boundingBox();}
 const scroll=await page.locator('.journey-body').evaluate(e=>({width:e.clientWidth,height:e.clientHeight,scrollHeight:e.scrollHeight,scrollWidth:e.scrollWidth,top:e.scrollTop}));
 if(scroll.scrollWidth>scroll.width+1){await page.screenshot({path:`${out}/${name}-overflow.png`});console.log(JSON.stringify({name,scroll,overflow:await page.locator('.journey-body *').evaluateAll(es=>es.filter(e=>e.getBoundingClientRect().right>innerWidth).map(e=>({class:e.getAttribute('class'),text:e.textContent.slice(0,30),right:e.getBoundingClientRect().right,width:e.getBoundingClientRect().width})))}));} assert.ok(scroll.scrollWidth<=scroll.width+1,name+' horizontal overflow');
 const flow=await page.locator('.journey-body').evaluate(e=>[...e.querySelectorAll(':scope > *, .journey-loot > *')].map(n=>{const r=n.getBoundingClientRect(),s=getComputedStyle(n);return {class:n.className,y:r.y,height:r.height,margin:s.margin,padding:s.padding,minHeight:s.minHeight};}));
 await page.screenshot({path:`${out}/${name}.png`});shots.push({name,viewport:page.viewportSize(),bounds,scroll,flow,elements});
}
let url;
try{
 url=await new Promise((yes,no)=>{server.on('error',no);server.stdout.on('data',b=>{const m=String(b).match(/http:\/\/127\.0\.0\.1:\d+/);if(m)yes(m[0]);});});browser=await chromium.launch({headless:true});
 for(const [w,h]of [[390,844],[320,568],[430,932]]){
  await load('running',w,h);await shot(`map-${w}`);
  if(phase==='legacy-after'&&w===320){await page.locator('.journey-body').evaluate(e=>e.scrollTop=e.scrollHeight);await shot('map-lower-320');const landmark=await page.locator('[data-regional-region="B"]').boundingBox(),overlay=await page.locator('.journey-map-destination').boundingBox();assert.ok(landmark.y+landmark.height<=overlay.y,'small-phone salt node is reachable above overlay after scrolling');}
  if(phase==='legacy-after'&&w>=390){const m=shots.at(-1);assert.ok(m.bounds.primaryArt.height/h>.8);assert.ok(m.bounds.secondaryAction.y>m.bounds.primaryArt.y);assert.ok(m.bounds.secondaryAction.y+m.bounds.secondaryAction.height<=m.bounds.bottomNav.y);for(const node of await page.locator('.journey-node').all()){const b=await node.boundingBox();assert.ok(b.y+b.height<m.bounds.secondaryAction.y,'all four labels remain above the floating action');}}
  await load('open',w,h);await shot(`idle-map-${w}`);await page.locator('[data-journey-enter]').click();
  for(const [i,key]of ['0:0','0:3','1:0'].entries()){await page.locator(`[data-regional-slot="${i}"]`).click();await page.locator(`[data-regional-member="${key}"]`).click();}
  await page.locator('.journey-body').evaluate(e=>e.scrollTop=0);await shot(`region-${w}`);
  if(phase==='legacy-after'&&w>=390){const r=shots.at(-1);assert.ok(r.scroll.scrollHeight<=r.scroll.height+1,'standard phone fits region composition');assert.ok(r.bounds.primaryArt.y+r.bounds.primaryArt.height<=r.bounds.secondaryAction.y);assert.ok(r.bounds.secondaryAction.y+r.bounds.secondaryAction.height<=r.bounds.target.y);assert.ok(r.bounds.target.y+r.bounds.target.height<=r.bounds.dynamicCharacter.y);assert.ok(r.bounds.dynamicCharacter.y+r.bounds.dynamicCharacter.height<=r.bounds.CTA.y+1);}
  if(w===320){await page.locator('[data-regional-slot="2"]').scrollIntoViewIfNeeded();await shot(`region-team-${w}`);}
  await load('return',w,h);await page.locator('[data-journey-enter]').click();await shot(`return-${w}`);
  if(phase==='legacy-after'){assert.equal(await page.locator('.journey-discovery p').count(),0,'story belongs in discovery details');const r=shots.at(-1);assert.ok(r.bounds.CTA.y+r.bounds.CTA.height<=r.bounds.bottomNav.y);if(w>=390)assert.ok(r.scroll.scrollHeight<=r.scroll.height+1,'standard phone fits discovery composition');}
  if(w===320){await page.locator('.journey-loot').scrollIntoViewIfNeeded();await shot(`return-loot-${w}`);}
 }
 assert.deepEqual(errors,[]);
}finally{await browser?.close();server.kill();await writeFile(`${out}/layout.json`,JSON.stringify({phase,errors,shots},null,2));console.log(JSON.stringify({phase,screens:shots.length,errors}));}
