// Private, spoiler-free stdout. Real browser, isolated synthetic saves only.
import assert from 'node:assert/strict';
import fs from 'node:fs/promises';
import crypto from 'node:crypto';
import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import * as E from '../web/engine.js';
import {REGIONAL} from '../web/content-registry.js';
import {LEGACY193} from '../web/legacy-content.js';
import {syncProgress} from '../web/progression.js';
import {ENTITLEMENTS} from '../web/collection-progress.js';
const require=createRequire(import.meta.url),{chromium}=require(process.env.CHICK_PLAYWRIGHT_PACKAGE??process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const outIndex=process.argv.indexOf('--out');
if(outIndex>=0&&!process.argv[outIndex+1])throw Error('Missing --out directory');
const out=outIndex>=0?process.argv[outIndex+1]:'artifacts/internal-art/production-runtime-qa',manifestPath='web/art/production/manifest.json';
await fs.mkdir(out,{recursive:true});
const navigationOnly=process.argv.includes('--navigation-only');
const manifestBytes=navigationOnly?Buffer.from('{"version":1,"assets":{}}'):await fs.readFile(manifestPath),manifest=JSON.parse(manifestBytes),assets=Object.values(manifest.assets);
const report={passed:false,navigationOnly,manifestHash:crypto.createHash('sha256').update(manifestBytes).digest('hex'),assetIds:assets.map(a=>a.id),scenes:{},checks:[],errors:[],requests:[],isolatedProfiles:true,realDevice:false};
const at=1800000000000;let s=E.freshState(at,2718);
s.cp=50000;s.kitchenLevel=3;s.duck=true;s.toolLevels=Array(9).fill(2);s.progress.tutorialSeen=true;
s.total=Object.fromEntries(LEGACY193.characters.flatMap((rows,egg)=>rows.map(c=>[`${egg}:${c.id}`,10])));
for(const c of REGIONAL.species)s.total[c.key]=10;
s.farm=Object.fromEntries(REGIONAL.species.map(c=>[c.key,2]));s.ingredients={0:2};
s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};syncProgress(s);
for(const c of REGIONAL.cards)s.expansion.discovery.cards[c.id]=++s.meta.factSeq;
for(const m of REGIONAL.materials){s.expansion.discovery.identified[m.id]=++s.meta.factSeq;s.ingredients[m.id]=1;}
s.expansion.regions.guideFlags=['GUIDE-B'];s.expansion.regions.introSpecimenDone=['V','R','T','B'];
for(const e of ENTITLEMENTS)s.expansion.collections.entitlements[e.id]={seq:++s.meta.factSeq,source:e.source};
for(const r of REGIONAL.regulars){for(const stage of r.stages)s.expansion.collections.entitlements[stage.reward]={seq:++s.meta.factSeq,source:stage.id};s.expansion.regulars[r.id]={readStages:r.stages.map(st=>st.id),pendingStage:null,activatedSeq:s.meta.factSeq,baselines:{},lastVisit:null};}
s.expansion.collections.display=REGIONAL.mementos.slice(0,3).map(m=>m.id);s.progress.lastTeam=REGIONAL.species.slice(0,3).map(c=>c.key);
s=E.normalizeSave(s,at);
const server=spawn(process.execPath,['server.mjs','0'],{stdio:['ignore','pipe','pipe'],windowsHide:true});
let browser,page,base;
const shot=async name=>{await page.screenshot({path:`${out}/${name}.png`});};
async function open(seed){
 const context=await browser.newContext({viewport:{width:390,height:844},reducedMotion:'reduce'});
 await context.addInitScript(({seed,at})=>{Date.now=()=>at+Math.floor(performance.now());if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));},{seed,at});
 await context.addInitScript(()=>{window.__artDraws=[];const draw=CanvasRenderingContext2D.prototype.drawImage;CanvasRenderingContext2D.prototype.drawImage=function(image,...args){if(image?.src?.includes('/art/production/'))window.__artDraws.push(new URL(image.src).pathname);return draw.call(this,image,...args);};});
 page=await context.newPage();page.on('pageerror',e=>report.errors.push(e.message));page.on('response',r=>{if(r.status()>=400)report.requests.push(r.url());});
 await page.goto(base);await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.waitForTimeout(700);return context;
}
const nav=name=>page.locator('#main-nav').getByRole('button',{name,exact:true}).click();
async function images(){
 await page.evaluate(async()=>{await Promise.all([...document.images].map(i=>i.decode().catch(()=>{})));});
 const broken=await page.locator('img[src*="/art/production/"]').evaluateAll(imgs=>imgs.filter(i=>!i.complete||!i.naturalWidth).map(i=>i.src));assert.deepEqual(broken,[]);
 const overflow=await page.locator('#panels>.panel').evaluateAll(els=>els.filter(e=>e.scrollWidth>e.clientWidth+2).length);assert.equal(overflow,0);
}
try{
 base=await new Promise((resolve,reject)=>{server.once('error',reject);const timer=setTimeout(()=>reject(Error('Server timeout')),10000);server.stdout.on('data',d=>{const m=String(d).match(/http:\/\/127\.0\.0\.1:\d+/);if(m){clearTimeout(timer);resolve(m[0]);}});});
 browser=await chromium.launch({headless:true,...(process.env.CHICK_QA_CHROME?{executablePath:process.env.CHICK_QA_CHROME}:{})});
 let context=await open(s);
 // Every published variant decodes and retains inset alpha at both display sizes.
 const geometry=await page.evaluate(async assets=>{
  const errors=[];let variants=0;
  for(const a of assets)for(const [name,v]of Object.entries(a.variants)){
   const im=new Image();im.src=v.path;await im.decode();variants++;
   if(im.naturalWidth!==v.width||im.naturalHeight!==v.height)errors.push('dimensions');
   for(const n of [48,120]){
    const c=document.createElement('canvas');c.width=n;c.height=n;const ctx=c.getContext('2d',{willReadFrequently:true});const scale=Math.min(n/im.width,n/im.height);ctx.drawImage(im,(n-im.width*scale)/2,(n-im.height*scale)/2,im.width*scale,im.height*scale);
    const px=ctx.getImageData(0,0,n,n).data;let left=n,right=0,top=n,bottom=0,count=0;
    for(let y=0;y<n;y++)for(let x=0;x<n;x++)if(px[(y*n+x)*4+3]>24){left=Math.min(left,x);right=Math.max(right,x);top=Math.min(top,y);bottom=Math.max(bottom,y);count++;}
    if(!count||left<1||top<1||right>=n-1||bottom>=n-1)errors.push('crop');
    if(a.kind==='species'&&name==='full'&&Math.abs((bottom+1)/n-.92)>.035)errors.push('anchor');
   }
  }return {variants,errors};
 },assets);assert.deepEqual(geometry.errors,[]);report.variantCount=geometry.variants;report.checks.push('all variants decoded; 48/120 alpha bounds and full foot anchor');
 // Verify the same published identity reaches every shared consumer.
 const resolvers=await page.evaluate(async assets=>{
  const {characterImage,toolImage}=await import('/web/catalog.js');const {productionCharacter,productionAsset}=await import('/web/production-art.js');const {familyCharacter,guestAvatar}=await import('/web/business-family-art.js');const {journeyCharacter,journeyMaterial,journeyDiscovery}=await import('/web/journey-art.js');const {mementoArt}=await import('/web/visual-assets.js');const errors=[];
  for(const a of assets){const v=a.variants;if(a.kind==='species'){const [egg,id]=a.identityKey.split(':').map(Number);if(characterImage(egg,id)!==v.full.path||productionCharacter(egg,id,'portrait')!==v.portrait.path||!familyCharacter(egg,id).includes(v.portrait.path)||!journeyCharacter(a.identityKey).includes(v.full.path))errors.push(a.id);}
   else if(a.kind==='materials'){if(toolImage(2,a.contentId)!==v['ingredient-icon'].path||journeyMaterial(a.contentId)!==v['ingredient-icon'].path)errors.push(a.id);}
   else if(a.kind==='cards'&&journeyDiscovery(a.contentId)!==v['discovery-vignette'].path)errors.push(a.id);
   else if(a.kind==='mementos'&&mementoArt(a.contentId)!==v.display.path)errors.push(a.id);
   else if(a.kind==='regulars'&&(!guestAvatar(a.contentId,true).includes(v.portrait.path)||guestAvatar(a.contentId,false).includes(v.portrait.path)))errors.push(a.id);
  }return errors;
 },assets);assert.deepEqual(resolvers,[]);report.checks.push('all shared consumer paths and unknown regular masking');
 for(const [scene,name]of [['catalog','图鉴'],['farm','农场'],['business','生意'],['exploration','寻访']]){
  await page.evaluate(()=>window.__artDraws=[]);await nav(name);
  if(scene==='catalog'){
   await page.locator('[data-book-tab="species"]').click();
   for(const c of REGIONAL.species){const [egg,id]=c.key.split(':').map(Number);await page.locator('[data-collection-egg="'+egg+'"]').click();await page.locator('[data-book-search]').fill(E.char(egg,id).title_zh_CN);await page.locator('[data-book-search-form] button').click();await images();assert.ok(await page.locator(navigationOnly?'.collection-picture img':'.collection-picture img[src*="/art/production/"]').count());}
   report.checks.push('each new character rendered in actual catalog search result');
  }
  if(scene==='business'){
   if(await page.locator('[data-trade-view="business"]').count())await page.locator('[data-trade-view="business"]').first().click();
   if(await page.locator('.bs-vessel').count())await page.locator('.bs-vessel').first().click();
   if(await page.locator('[data-business-filter="all"]').count())await page.locator('[data-business-filter="all"]').click();
   if(!navigationOnly)assert.ok(await page.locator('img[src*="/art/production/"]').count());
   const escaped=await page.locator('.business-stock-art img,.business-stock-art svg').evaluateAll(els=>els.filter(el=>{const r=el.getBoundingClientRect(),p=el.parentElement.getBoundingClientRect();return r.width>p.width+1||r.height>p.height+1;}).length);assert.equal(escaped,0,'business portraits must fit the common slot');
  }
  if(scene==='exploration'){await page.locator('[data-journey-enter]').click();await page.locator('[data-regional-last]').click();if(!navigationOnly)assert.equal(await page.locator('.journey-team img[src*="/art/production/"]').count(),3);}
  await page.waitForTimeout(400);await images();await shot(scene);report.scenes[scene]=true;
  if(scene==='farm'&&!navigationOnly)assert.ok(await page.evaluate(()=>window.__artDraws.length>0),'farm draws production characters');
  if(scene==='business'&&await page.locator('[data-business-sheet-close]').count())await page.locator('[data-business-sheet-close]').click();
 }
 // Discovery records exercise all four region pages with their material art.
 await page.locator('[data-journey-back]').click();
 for(const region of [...new Set(REGIONAL.species.map(c=>c.region))]){
  await page.locator(`[data-regional-region="${region}"]`).click();await page.locator('[data-journey-enter]').click();
  await page.locator('[data-regional-tab="record"]').first().click();await images();
  if(!navigationOnly){assert.equal(await page.locator('[data-regional-material] img[src*="/art/production/"]').count(),REGIONAL.materials.filter(m=>m.region===region).length);assert.equal(await page.locator('[data-regional-card] img[src*="/art/production/"]').count(),REGIONAL.cards.filter(c=>c.region===region&&c.type!=='specimen').length);}
  await shot('discovery-records-'+region);await page.locator('[data-journey-back]').click();
 }
 await nav('生意');if(await page.locator('[data-trade-view="business"]').count())await page.locator('[data-trade-view="business"]').first().click();
 await page.locator('[data-trade-view="regulars"]').first().click();await page.locator('.regulars-screen').waitFor();await images();if(!navigationOnly)assert.equal(await page.locator('.regulars-screen img[src*="/ART-RG"]').count(),4);await shot('regulars');report.scenes.regulars=true;
 await nav('图鉴');await page.locator('[data-book-tab="collections"]').click();
 if(await page.locator('[data-gs-directory]').count())await page.locator('[data-gs-directory]').click();
 await page.locator('[data-books-category]').selectOption('mementos');await images();if(!navigationOnly)assert.equal(await page.locator('.books-mementos img[src*="/art/production/"]').count(),REGIONAL.mementos.length);await shot('mementos');report.scenes.collection=true;
 for(const width of [320,768,1280]){await page.setViewportSize({width,height:900});await images();}await context.close();
 // Two real kitchen batches, each asset occurs in one supported stationary slot.
 for(const egg of [0,1])for(let offset=0;offset<REGIONAL.species.filter(c=>c.egg===egg).length;offset+=24){
  const list=REGIONAL.species.filter(c=>c.egg===egg),seed=structuredClone(s);seed.egg=egg;E.startBatch(seed,0,at-1000000,()=>.1,()=>.5);
  seed.batch.eggs.forEach((e,i)=>{const c=list[(offset+i)%list.length];Object.assign(e,{egg,id:Number(c.key.split(':')[1]),status:'ready',openAt:at-10000,blackAt:null,animationAt:at-5000});});seed.batch.ends=at-1;
  context=await open(E.normalizeSave(seed,at));await nav('厨房');if(!navigationOnly)await page.waitForFunction(()=>new Set(window.__artDraws).size>=24);else await page.waitForTimeout(700);await images();await shot('kitchen-'+egg+'-'+offset);await context.close();
 }report.scenes.kitchen=true;
 // New player: never render an unrevealed production portrait/food/design in DOM.
 const fresh=E.freshState(at,88);fresh.progress.tutorialSeen=true;context=await open(fresh);
 for(const name of ['图鉴','生意','寻访','农场','厨房']){await nav(name);if(name==='图鉴')await page.locator('[data-book-tab="species"]').click();const count=await page.locator('img[src*="/art/production/"],svg image[href*="/art/production/"]').count();assert.equal(count,0,'unknown production art shown');}
 await context.close();report.checks.push('actual seven scenes, compact/wide layouts, two ready batches, fresh-save spoiler gate');
 assert.deepEqual(report.errors,[]);assert.deepEqual(report.requests,[]);report.passed=true;
}catch(e){report.failure=String(e);if(page&&!page.isClosed()){await shot('failure');await fs.writeFile(out+'/failure.txt',await page.locator('body').innerText());}throw e;}
finally{await browser?.close();server.kill();await fs.writeFile(out+(navigationOnly?'/navigation-preflight.json':'/report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify({passed:report.passed,assetCount:assets.length,variantCount:report.variantCount,scenes:report.scenes,errorCount:report.errors.length,failedRequests:report.requests.length}));}
