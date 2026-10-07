// Actual-browser regression assertions. All storage lives in temporary profiles.
import assert from 'node:assert/strict';
import {createRequire} from 'node:module';
import {spawn} from 'node:child_process';
import {mkdir,readFile,writeFile} from 'node:fs/promises';
import {parseBackup} from '../web/save-store.js';
import {freshState,normalizeSave} from '../web/engine.js';
const require=createRequire(import.meta.url);
const {chromium}=require(process.env.CHICK_PLAYWRIGHT_PACKAGE??process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const out='artifacts/regression-audit',checks=[],errors=[],requests=[];await mkdir(out,{recursive:true});
const server=spawn(process.execPath,['server.mjs','0'],{stdio:['ignore','pipe','pipe'],windowsHide:true});
const base=await new Promise((done,fail)=>{server.on('error',fail);server.stdout.on('data',b=>{const m=String(b).match(/http:\/\/127\.0\.0\.1:\d+/);if(m)done(m[0]);});});
const browser=await chromium.launch({headless:true});let page,passed=false;
function watch(p){p.on('pageerror',e=>errors.push(e.message));p.on('console',m=>{if(['error','warning'].includes(m.type()))errors.push(m.text());});p.on('response',r=>{if(r.status()>=400)requests.push(r.url());});}
async function open(seed,viewport={width:390,height:844},dpr=1){
 const c=await browser.newContext({viewport,reducedMotion:'reduce',deviceScaleFactor:dpr});
 if(seed)await c.addInitScript(seed=>{Date.now=()=>seed.lastSeen;if(!sessionStorage.getItem('seeded')){localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));sessionStorage.setItem('seeded','yes');}},seed);
 const p=await c.newPage();watch(p);await p.goto(base);await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.waitForTimeout(600);return {c,p};
}
const nav=(p,name)=>p.locator('#main-nav').getByRole('button',{name,exact:true}).click();
const read=p=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
async function shot(p,name){await p.screenshot({path:`${out}/${name}.png`});}
async function cards(p){
 const result=await p.locator('.collection-stamp').evaluateAll(els=>{
  const overlap=(a,b)=>Math.min(a.right,b.right)-Math.max(a.left,b.left)>1&&Math.min(a.bottom,b.bottom)-Math.max(a.top,b.top)>1;
  const rects=els.map(e=>e.getBoundingClientRect()),problems=[];
  els.forEach((el,i)=>{for(let j=i+1;j<els.length;j++)if(overlap(rects[i],rects[j]))problems.push('cards '+i+'/'+j);
   const children=[...el.children].map(e=>e.getBoundingClientRect());
   for(let k=0;k<children.length-1;k++)if(overlap(children[k],children[k+1]))problems.push('card children '+i+'/'+k);
   if(el.scrollHeight>el.clientHeight+1||el.scrollWidth>el.clientWidth+1)problems.push('card overflow '+i);
  });return problems;
 });assert.deepEqual(result,[]);
}
async function surface(p){
 const r=await p.locator('#panels>.panel').evaluate(el=>{const r=el.getBoundingClientRect();return {left:r.left,right:r.right,bottom:r.bottom,overflow:el.scrollWidth-el.clientWidth};});
 const n=await p.locator('#main-nav').boundingBox();const v=p.viewportSize();
 assert.ok(r.left>=-1&&r.right<=v.width+1);assert.ok(r.bottom<=v.height+1);assert.ok(r.overflow<=1,JSON.stringify(r));
 if(v.width<1024)assert.ok(r.bottom<=n.y+1);
}
try{
 // Independently saved old player data; loading never writes to the source file.
 const source=JSON.parse(await readFile('tests/fixtures/golden/mid-v3.json','utf8'));
 const old=await open(source);page=old.p;const migrated=await read(page);
 for(const key of ['cp','kitchenLevel','farm','total','ingredients','toolLevels'])assert.deepEqual(migrated[key],normalizeSave(source,source.lastSeen)[key],key);
 assert.equal(migrated.version,6);assert.equal(await page.evaluate(()=>localStorage.getItem('chick-kitchen-v1.pre-upgrade-v3')),JSON.stringify(source));
 await page.reload();await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.waitForTimeout(600);
 assert.deepEqual((await read(page)).total,migrated.total);assert.equal((await read(page)).cp,migrated.cp);
 await page.goto(base+'/classic');await page.waitForURL('**/web/index.html');assert.equal((await read(page)).cp,migrated.cp);
 checks.push('v3 real loading/migration/reload/archived-entry redirect preserves CP, inventories, discovery and raw upgrade backup');await old.c.close();
 const fixture=freshState(1800000000000,29);fixture.total={'0:0':300,'0:3':12,'0:4':3,'0:18':2,'0:20':2};fixture.farm={'0:0':20,'0:3':5};fixture.cp=6000;fixture.progress.tutorialSeen=true;
 const ui=await open(fixture);page=ui.p;
 for(const [width,height,font] of [[320,568,16],[360,640,16],[390,844,16],[430,932,16],[768,1024,16],[844,390,16],[1280,900,16],[1920,1080,16],[320,568,24],[390,844,24],[1280,900,24]]){
  await page.setViewportSize({width,height});await page.evaluate(font=>{document.documentElement.style.fontSize=font+'px';dispatchEvent(new Event('resize'));},font);await nav(page,'厨房');await page.waitForTimeout(100);
  assert.equal(await page.locator('#main-nav button>svg').count(),5);
  for(const id of ['supply','inventory','workshop','help'])assert.equal(await page.locator(`#quick-actions [data-control-id=${id}]`).isVisible(),true);
  const sizes=await page.locator('#main-nav button,#quick-actions button').evaluateAll(es=>es.map(e=>{const r=e.getBoundingClientRect();return [r.width,r.height];}));assert.ok(sizes.every(([w,h])=>w>=44&&h>=44));
  const scene=await page.locator('#scene-stage').boundingBox(),area=await page.locator('#panels').boundingBox();
  if(height>width&&font===16)assert.ok(Math.abs(scene.height-area.height)<3,'scene must fill portrait play area');
  assert.ok(scene.y>=-1&&scene.y+scene.height<=height+1);
  const colors=await page.locator('canvas').evaluate(c=>{const copy=document.createElement('canvas');copy.width=c.width;copy.height=c.height;const ctx=copy.getContext('2d',{willReadFrequently:true});ctx.drawImage(c,0,0);const d=ctx.getImageData(0,0,c.width,c.height).data,s=new Set();for(let i=0;i<d.length;i+=200)s.add([d[i],d[i+1],d[i+2]].join());return s.size;});assert.ok(colors>200);
  if(font===16)await shot(page,`kitchen-${width}x${height}`);
  await page.locator('[data-control-id=inventory]').click();await page.locator('.harvest-screen').waitFor();await surface(page);await page.keyboard.press('Escape');
  await nav(page,'图鉴');await page.locator('[data-book-tab=species]').click();await surface(page);await cards(page);
  await page.locator('.collection-name').first().evaluate(e=>e.textContent='很长很长的厨房伙伴名称与特别收藏纪念品');await cards(page);
  await page.locator('.collection-stamp').first().hover();await cards(page);
  await shot(page,`book-${width}x${height}-${font}`);
  await page.locator('[data-book-search]').fill('C001');await page.locator('[data-book-search-form] button').click();assert.equal(await page.locator('.collection-stamp').count(),1);await cards(page);
  await page.locator('[data-book-search]').fill('不存在的名字');await page.locator('[data-book-search-form] button').click();assert.equal(await page.locator('.collection-stamp').count(),0);await page.locator('[data-collection-clear]').click();
  for(const name of ['生意','寻访']){await nav(page,name);await surface(page);}
  await nav(page,'农场');if(font===16)await shot(page,`farm-${width}x${height}`);
  checks.push(`${width}x${height}, ${font}px: scene, icons, shortcuts, core surfaces and 0/1/9 cards, long titles, hover, locked/unlocked`);
 }
 await page.setViewportSize({width:390,height:844});await page.evaluate(()=>{document.documentElement.style.fontSize='16px';dispatchEvent(new Event('resize'));});await nav(page,'厨房');
 for(const [id,selector] of [['supply','.shop-screen'],['workshop','.workshop-screen'],['help','.guide-screen']]){await page.locator(`#quick-actions [data-control-id=${id}]`).click();await page.locator(selector).waitFor();await nav(page,'厨房');}
 await page.getByRole('button',{name:'设置',exact:true}).click();await page.locator('.settings-screen').waitFor();assert.ok(await page.locator('[data-export]').count());assert.ok(await page.locator('[data-import]').count());await page.locator('[data-return]').click();
 await page.locator('[data-control-id=clean]').click();await page.locator('.cleaning-dialog').waitFor();assert.equal(await page.locator('#quick-actions').evaluate(e=>e.inert),true);await page.keyboard.press('Escape');
 await shot(page,'kitchen-final');checks.push('shop, inventory, skills, help, settings/backups and compact cleaning open and return; modal isolates shortcuts');
 await ui.c.close();
 const dense=await open(fixture,{width:390,height:844},3);page=dense.p;await nav(page,'图鉴');await page.locator('[data-book-tab=species]').click();await cards(page);await shot(page,'book-dpr3');await dense.c.close();checks.push('DPR 3 portrait uses the same non-overlapping layout');
 // Current/future invalid bytes must never become a new 600 CP game.
 const bad=await browser.newContext();await bad.addInitScript(()=>localStorage.setItem('chick-kitchen-v1','{"version":999}'));page=await bad.newPage();watch(page);await page.goto(base+'/classic');await page.locator('[data-recover]').waitFor();assert.equal(await page.evaluate(()=>localStorage.getItem('chick-kitchen-v1')),'{"version":999}');await bad.close();checks.push('future save via legacy URL stays locked and byte-for-byte intact');
 assert.deepEqual(errors,[]);assert.deepEqual(requests,[]);passed=true;
}catch(e){if(page&&!page.isClosed()){await shot(page,'failure');await writeFile(`${out}/failure.txt`,await page.locator('body').innerText());}throw e;}
finally{await browser.close();server.kill();await writeFile(`${out}/browser-report.json`,JSON.stringify({checkedAt:new Date().toISOString(),passed,checks,errors,requests,isolatedProfiles:true,realDevice:false},null,2));console.log(JSON.stringify({passed,checks,errors,requests},null,2));}
