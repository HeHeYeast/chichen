// Isolated browser profiles only; never reads or writes the player's browser.
import {createRequire} from 'node:module';
import {spawn} from 'node:child_process';
import {mkdir,readFile,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {freshState,normalizeSave,startBatch} from '../web/engine.js';
import {REGIONAL} from '../web/content-registry.js';
const require=createRequire(import.meta.url);
const {chromium}=require(process.env.CHICK_PLAYWRIGHT_PACKAGE);
const phase=process.argv[2]??'after',out=`artifacts/visual-polish/${phase}`;
await mkdir(out,{recursive:true});
const server=spawn(process.execPath,['server.mjs','0'],{stdio:['ignore','pipe','pipe'],windowsHide:true});
const base=await new Promise((ok,no)=>{server.on('error',no);server.stdout.on('data',b=>{const m=String(b).match(/http:\/\/127\.0\.0\.1:\d+/);if(m)ok(m[0]);});});
const browser=await chromium.launch({headless:true,executablePath:process.env.CHICK_QA_CHROME});
const errors=[],checks=[],screens=[];let page,passed=false;
const seed=freshState(1800000000000,29);
seed.cp=25000;seed.kitchenLevel=3;seed.toolLevels=Array(9).fill(2);seed.duck=true;seed.progress.tutorialSeen=true;
for(let i=0;i<28;i++){seed.total['0:'+i]=30;seed.farm['0:'+i]=3;}
seed.ingredients={0:2,9:2,75:2,76:2,77:2,78:2,79:2,80:2,81:2,82:2};
for(const m of REGIONAL.materials){seed.expansion.discovery.cards[m.specimen]=++seed.meta.factSeq;seed.expansion.discovery.identified[m.id]=++seed.meta.factSeq;}
for(const card of REGIONAL.cards.filter(c=>c.type==='lore'))seed.expansion.discovery.cards[card.id]=++seed.meta.factSeq;
for(const r of REGIONAL.recipes){seed.total[r.key]=1;seed.farm[r.key]=1;}
for(const m of REGIONAL.mementos)seed.expansion.collections.entitlements[m.id]={seq:++seed.meta.factSeq,source:m.source};
seed.progress.orders['first-sale']={accepted:true,completed:true,choice:'0:0',delivered:12};
const nav=name=>page.locator('#main-nav').getByRole('button',{name,exact:true}).click();
async function open(state,width=390,height=844){
 const c=await browser.newContext({viewport:{width,height},reducedMotion:'reduce'});
 if(state)await c.addInitScript(s=>{Date.now=()=>s.lastSeen+Math.floor(performance.now());if(!sessionStorage.getItem('seeded')){localStorage.setItem('chick-kitchen-v1',JSON.stringify(s));sessionStorage.setItem('seeded','yes');}},state);
 page=await c.newPage();page.on('pageerror',e=>errors.push(e.message));page.on('response',r=>{if(r.status()>=400)errors.push(r.url()+': '+r.status());});
 await page.goto(base);await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.waitForTimeout(650);return c;
}
async function shot(name){
 await page.waitForTimeout(100);await page.screenshot({path:`${out}/${name}.png`});screens.push(name);
 const bad=await page.evaluate(()=>[...document.images].filter(i=>!i.complete||!i.naturalWidth).map(i=>i.src));assert.deepEqual(bad,[],'loaded images');
 const fit=await page.locator('#panels>.panel').evaluateAll(es=>es.map(e=>{const r=e.getBoundingClientRect();return {overflow:e.scrollWidth-e.clientWidth,inside:r.left>=-1&&r.right<=innerWidth+1&&r.bottom<=innerHeight+1};}));
 assert.ok(fit.every(x=>x.inside&&x.overflow<2),name+JSON.stringify(fit));checks.push(name);
}
try{
 const newGame=await open(null);await shot('new-kitchen');await nav('农场');await shot('new-farm');await newGame.close();
 const source=JSON.parse(await readFile('tests/fixtures/golden/mid-v3.json','utf8'));
 const old=await open(source);await shot('old-kitchen');const saved=await page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
 for(const key of ['cp','total','farm','ingredients','toolLevels'])assert.deepEqual(saved[key],normalizeSave(source,source.lastSeen)[key]);
 await page.reload();await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.waitForTimeout(650);await nav('图鉴');await page.locator('[data-book-tab=species]').click();await shot('old-collection');await old.close();
 const c=await open(seed);
 const sizes=phase==='before'?[[390,844]]:[[320,568],[390,844],[768,1024],[844,390],[1280,900],[1920,1080]];
 for(const [w,h]of sizes){
  await page.setViewportSize({width:w,height:h});
  await nav('厨房');await shot(`kitchen-${w}`);
  await nav('农场');await shot(`farm-${w}`);
  await nav('图鉴');await page.locator('[data-book-tab=overview]').click();await shot(`overview-${w}`);
  await page.locator('[data-book-tab=species]').click();await shot(`collection-${w}`);
  await page.locator('[data-book-tab=lore]').click();await shot(`materials-${w}`);
  await nav('寻访');await page.locator('[data-regional-tab=trip]').click();await shot(`dispatch-${w}`);await page.locator('[data-regional-tab=record]').click();await shot(`discoveries-${w}`);
  await nav('厨房');await page.locator('[data-control-id=workshop]').click();await shot(`skills-${w}`);
  await nav('厨房');await page.locator('[data-control-id=supply]').click();await shot(`shop-${w}`);await page.locator('[data-shop-tab="1"]').click();await shot(`shop-items-${w}`);
  await nav('生意');await shot(`business-${w}`);
  await nav('厨房');await page.getByRole('button',{name:'设置',exact:true}).click();await shot(`settings-${w}`);
 }
 await page.setViewportSize({width:390,height:844});
 await nav('图鉴');await page.locator('[data-book-tab=species]').click();
 await page.locator('[data-book-search]').fill('C129');await page.locator('[data-book-search-form] button').click();await shot('regional-species-card');
 await page.locator('[data-collection-card="128"]').click();await shot('regional-species-detail');
 await nav('图鉴');await page.locator('[data-book-tab=collections]').click();await shot('collections');
 if(await page.locator('[data-gs-directory]').count()&&!await page.locator('[data-books-category]').isVisible())await page.locator('[data-gs-directory]').click();await page.locator('[data-books-category]').selectOption('mementos');await shot('mementos');
 await page.locator('[data-books-slot="0"]').selectOption('M01');await nav('农场');await shot('farm-memento');
 for(const region of ['V','R','T','B']){
  await nav('寻访');await page.locator(`[data-regional-region="${region}"]`).click();await page.locator('[data-regional-tab=trip]').click();await shot(`route-${region}`);
  await page.locator('[data-regional-tab=record]').click();await page.locator('.regional-notes').scrollIntoViewIfNeeded();await shot(`notes-${region}`);
  await page.locator('.regional-note-card summary').first().click();await shot(`note-expanded-${region}`);
  await page.locator('.regional-methods').scrollIntoViewIfNeeded();await shot(`species-${region}`);
 }
 await nav('生意');await page.locator('[data-trade-view=projects]').click();await shot('projects');
 await page.locator('[data-trade-view=regulars]').click();await shot('regulars');
 await nav('厨房');await page.locator('[data-control-id=supply]').click();await page.locator('[data-shop-tab="1"]').click();
 await page.locator('[data-shop-ingredient-details="75"]').scrollIntoViewIfNeeded();await shot('shop-regional-materials');
 await page.locator('[data-shop-ingredient-details="75"]').click();await shot('material-detail');
 const heroFits=await page.locator('.shop-ingredient-hero img').evaluate(img=>{const a=img.getBoundingClientRect(),b=img.parentElement.getBoundingClientRect();return a.left>=b.left-1&&a.top>=b.top-1&&a.right<=b.right+1&&a.bottom<=b.bottom+1;});assert.ok(heroFits,'material image stays inside portrait frame');
 await page.setViewportSize({width:320,height:568});await page.evaluate(()=>{document.documentElement.style.fontSize='24px';dispatchEvent(new Event('resize'));});
 for(const name of ['厨房','农场','图鉴','寻访','生意']){await nav(name);await shot(`large-text-${name}`);}
 await nav('寻访');await page.locator('[data-regional-tab=record]').click();await page.locator('.regional-methods').scrollIntoViewIfNeeded();await shot('large-text-methods-reachable');
 await c.close();
 const ready=freshState(1800000000000,12);startBatch(ready,0,ready.lastSeen-3*3600000,()=>0);
 const harvest=await open(ready);await page.waitForTimeout(3300);await page.locator('[data-control-id="egg:0"]').press('Enter');await page.locator('.discovery-toast').waitFor();await shot('first-discovery');
 const savedFirst=await page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));assert.equal(savedFirst.total['0:0'],1);
 await page.locator('[data-control-id="egg:1"]').press('Enter');assert.equal((await page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')))).total['0:0'],2);
 await page.reload();await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.waitForTimeout(650);assert.equal(await page.locator('.discovery-toast:not([hidden])').count(),0);await harvest.close();
 assert.deepEqual(errors,[]);passed=true;
}catch(e){if(page&&!page.isClosed()){await page.screenshot({path:`${out}/failure.png`});await writeFile(`${out}/failure.txt`,await page.locator('body').innerText());}throw e;}finally{await browser.close();server.kill();await writeFile(`${out}/report.json`,JSON.stringify({phase,passed,checks,errors,screens,isolatedProfiles:true,realDevice:false},null,2));console.log(JSON.stringify({phase,passed,checks:checks.length,errors}));}
