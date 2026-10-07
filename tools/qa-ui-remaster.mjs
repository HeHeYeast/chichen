// Work 2 browser acceptance. All saves, screenshots and content stay private.
import assert from 'node:assert/strict';
import fs from 'node:fs/promises';
import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import * as E from '../web/engine.js';
import {LEGACY193} from '../web/legacy-content.js';
import {REGIONAL} from '../web/content-registry.js';
import {syncProgress} from '../web/progression.js';
import {ENTITLEMENTS} from '../web/collection-progress.js';
const {chromium}=createRequire(import.meta.url)(process.env.CHICK_PLAYWRIGHT_PACKAGE??process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const out='artifacts/internal-ui-remaster/final-runtime',at=1800000000000;
await fs.mkdir(out,{recursive:true});
const report={passed:false,isolatedSaves:true,physicalDevice:false,checks:[],screens:[],errors:[],badResponses:[]};
const server=spawn(process.execPath,['server.mjs','0'],{stdio:['ignore','pipe','pipe'],windowsHide:true});
let browser,page,context;
const seed=(kind='mid')=>{
 let s=E.freshState(at,2718);s.progress.tutorialSeen=true;
 if(kind!=='fresh'){
  s.cp=50000;s.kitchenLevel=3;s.duck=true;s.toolLevels=Array(9).fill(2);
  s.total=Object.fromEntries(LEGACY193.characters.flatMap((rows,egg)=>rows.map(c=>[`${egg}:${c.id}`,10])));
  s.farm={'0:0':30,'0:3':10,'1:0':5};s.ingredients={0:5,1:3};
  s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};syncProgress(s);
  if(kind==='complete'){
   for(const c of REGIONAL.species){s.total[c.key]=10;s.farm[c.key]=2;}
   for(const c of REGIONAL.cards)s.expansion.discovery.cards[c.id]=++s.meta.factSeq;
   for(const m of REGIONAL.materials){s.expansion.discovery.identified[m.id]=++s.meta.factSeq;s.ingredients[m.id]=1;}
   for(const e of ENTITLEMENTS)s.expansion.collections.entitlements[e.id]={seq:++s.meta.factSeq,source:e.source};
   s.expansion.collections.display=REGIONAL.mementos.slice(0,3).map(m=>m.id);
  }
 }
 return E.normalizeSave(s,at);
};
const click=async q=>{await page.locator(q).first().click();};
const ctl=async id=>page.locator(`[data-control-id="${id}"]`).press('Enter');
const nav=async name=>page.locator('#main-nav').getByRole('button',{name,exact:true}).click();
const select=async(q,v)=>page.locator(q).selectOption(v);
const read=()=>page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
async function shot(id){
 await page.evaluate(async()=>{await document.fonts.ready;await Promise.all([...document.images].map(i=>i.decode().catch(()=>{})));});
 const issues=await page.locator('#panels>.panel').evaluateAll(es=>es.filter(e=>{const r=e.getBoundingClientRect();return e.scrollWidth>e.clientWidth+2||r.left< -1||r.right>innerWidth+1;} ).map(e=>e.className));
 assert.deepEqual(issues,[],id+' panel overflow');
 assert.equal(await page.locator('img').evaluateAll(es=>es.filter(i=>!i.complete||!i.naturalWidth).length),0,id+' broken image');
 await page.screenshot({path:`${out}/${id}.png`});report.screens.push(id);
}
async function boot(base,kind='mid',size={width:390,height:844}){
 await context?.close();context=await browser.newContext({viewport:size,reducedMotion:'reduce'});
 await context.addInitScript(({s,at})=>{Date.now=()=>at;if(!sessionStorage.getItem('seeded')){localStorage.setItem('chick-kitchen-v1',JSON.stringify(s));sessionStorage.setItem('seeded','1');}},{s:seed(kind),at});
 page=await context.newPage();page.setDefaultTimeout(7000);page.on('pageerror',e=>report.errors.push(e.message));page.on('response',r=>{if(r.status()>=400)report.badResponses.push(r.url());});
 await page.goto(base);await click('[data-control-id="start"]');await page.waitForTimeout(600);
}
async function directory(category){await nav('图鉴');await click('[data-book-tab="collections"]');if(await page.locator('[data-gs-directory]').count())await click('[data-gs-directory]');await select('[data-books-category]',category);}
try{
 const base=await new Promise((r,j)=>{server.on('error',j);server.stdout.on('data',b=>{const m=String(b).match(/http:\/\/127\.0\.0\.1:\d+/);if(m)r(m[0]);});});
 browser=await chromium.launch({headless:true,...(process.env.CHICK_QA_CHROME?{executablePath:process.env.CHICK_QA_CHROME}:{})});
 await boot(base);
 await shot('01-kitchen');await ctl('ingredient');await shot('02-seasoning');await click('[data-id="0"]');assert.equal(await page.locator('[data-id="0"]').getAttribute('aria-pressed'),'true');await shot('02-seasoning-selected');
 await nav('厨房');await ctl('workshop');const before=await read();await shot('06-workshop');
 for(const branch of ['CUL','HOME','TRADE','OBS','TRIP']){await click(`[data-branch="${branch}"]`);assert.equal(await page.locator('.remaster-skill-board .skill-node').count(),6);}
 await click('[data-branch="CUL"]');await click('[data-learn="CUL-1"]');assert.deepEqual((await read()).progress.skills,before.progress.skills);await shot('72-skill-draft');await click('[data-plan-cancel]');assert.deepEqual((await read()).progress.skills,before.progress.skills);
 await click('[data-skill-detail="CUL-1"]');await shot('07-skill-detail');await click('[data-detail-learn]');await click('[data-plan-apply]');await shot('73-skill-applied');await page.keyboard.press('Escape');assert.equal((await read()).progress.skills['CUL-1'],1);
 for(const key of ['cp','farm','total','ingredients','toolLevels','seed'])assert.deepEqual((await read())[key],before[key],key);
 await page.reload();await click('[data-control-id="start"]');await page.waitForTimeout(600);assert.equal((await read()).progress.skills['CUL-1'],1);
 report.checks.push('30 skills; draft/cancel do not write; apply persists after reload; CP/stock/RNG unchanged');
 await ctl('workshop');await click('[data-workshop-tab="story"]');await shot('08-legacy-order');await click('.order-history summary');await click('[data-story-read]');await shot('70-story-complete');
 await click('[data-workshop-tab="trip"]');await shot('09-legacy-route');await click('[data-member-slot]');await shot('10-legacy-team');await click('[data-pick-member]');await click('[data-depart]');await shot('74-trip-confirm');await page.keyboard.press('Escape');
 await nav('厨房');await ctl('tool:0');await click('.candidate-list summary');await click('[data-preview-species]');await shot('71-observation');
 await nav('厨房');await ctl('inventory');await shot('34-inventory');await click('[data-harvest-plus]');assert.ok(await page.locator('.harvest-row.is-selected').count());await click('[data-harvest-character]');await shot('inventory-detail');
 await nav('厨房');await ctl('supply');await shot('27-shop-tools');await click('[data-shop-tool-details]');await shot('28-tool-growth');await click('[data-shop-detail-back]');await click('[data-shop-tab="1"]');await shot('29-shop-materials');await click('[data-shop-ingredient-details]');await shot('30-material-detail');await click('[data-shop-detail-back]');await page.locator('[data-shop-search]').fill('no-result');await page.waitForTimeout(350);await shot('31-shop-empty');await click('[data-shop-search-clear]');await click('[data-shop-filter="locked"]');await shot('32-shop-locked');
 await nav('图鉴');await click('[data-book-tab="overview"]');await shot('11-book-overview');await click('[data-index-tab="species"]');await shot('12-species');await click('[data-collection-card]');await shot('13-known-detail');await nav('图鉴');await click('[data-book-tab="species"]');await page.locator('[data-book-search]').fill('no-result');await click('[data-book-search-form] button');await shot('14-species-empty');await click('[data-collection-clear]');await click('[data-collection-recipes]');await shot('55-recipes');
 const cards=await page.locator('.cookbook-card').evaluateAll(es=>({count:es.length,invalid:es.filter(e=>e.getBoundingClientRect().height<175||e.scrollHeight>e.clientHeight+2).length}));assert.ok(cards.count>20);assert.equal(cards.invalid,0);report.checks.push('long recipe list: '+cards.count+' cards, no collapsed or cropped cards');
 await click('[data-cookbook-recipe]');await shot('56-recipe-detail');
 for(const [cat,ids]of [['region',['17-region-collection','18-region-detail']],['special',['19-special-collection','20-special-detail']]]){await directory(cat);await shot(ids[0]);await click('[data-books-open]');await shot(ids[1]);await click('.remaster-conditions summary');await shot(ids[1]+'-conditions');await click('[data-books-pin]');await nav('图鉴');await click('[data-book-tab="overview"]');await click('[data-book-pin]');assert.equal(await page.locator('.remaster-collection-page').count(),1);}
 await directory('mementos');await shot('21-mementos');await select('[data-books-category]','papers');await shot('22-papers');await click('[data-book-tab="lore"]');await shot('23-lore');for(const [v,id]of [['region','24-lore-region'],['special','25-lore-special'],['calendar','26-lore-calendar']]){await select('[data-book-lore-type]',v);await shot(id);}
 await nav('厨房');await ctl('settings');await shot('41-settings');await click('[data-manual]');for(let i=0;i<9;i++){await click(`[data-chapter="${i}"]`);await shot('help-'+i);}await nav('厨房');await ctl('settings');await click('[data-journal]');await shot('52-calendar');await click('[data-journal-tab="recipes"]');await shot('53-seasonal');
 await boot(base,'fresh');await ctl('ingredient');await shot('58-seasoning-empty');await nav('厨房');await ctl('inventory');await shot('59-inventory-empty');await nav('厨房');await ctl('workshop');await shot('60-skills-locked');await click('[data-skill-detail]');await shot('61-skill-locked-detail');
 for(const name of ['图鉴','生意','寻访','农场','厨房']){await nav(name);if(name==='图鉴'){await click('[data-book-tab="species"]');await click('[data-collection-card]');await shot('62-unknown-detail');}assert.equal(await page.locator('img[src*="/art/production/"],svg image[href*="/art/production/"]').count(),0);}
 await directory('mementos');await shot('79-mementos-empty');await select('[data-books-category]','papers');await shot('78-papers-empty');report.checks.push('fresh-save unknown identities remain masked across all five places');
 await boot(base,'complete');await directory('mementos');await shot('65-mementos-owned');const saved=await read();await select('[data-books-slot="0"]','');assert.equal((await read()).expansion.collections.display[0],null);assert.equal((await read()).cp,saved.cp);assert.deepEqual((await read()).expansion.collections.entitlements,saved.expansion.collections.entitlements);await click('[data-book-tab="lore"]');await shot('66-material-known');report.checks.push('memento display changes presentation only; ownership and CP retained');
 for(const [width,height,font]of [[320,568,16],[390,844,16],[430,932,16],[844,390,16],[1280,900,16],[320,568,24]]){
  await boot(base,'mid',{width,height});await page.evaluate(f=>document.documentElement.style.fontSize=f+'px',font);const suffix=`${width}-${font}`;
  await ctl('workshop');await shot('skills-'+suffix);await click('[data-skill-detail]');await shot('skill-detail-'+suffix);await click('[data-skill-back]');await click('[data-learn="CUL-1"]');await click('[data-plan-cancel]');
  await nav('图鉴');await click('[data-book-tab="overview"]');await shot('overview-'+suffix);await directory('region');await shot('regions-'+suffix);await click('[data-books-open]');await shot('region-detail-'+suffix);await click('.remaster-conditions summary');await shot('conditions-'+suffix);await click('[data-books-list]');await select('[data-books-category]','special');await shot('special-'+suffix);await click('[data-books-open]');await shot('special-detail-'+suffix);await nav('厨房');await ctl('inventory');await shot('inventory-'+suffix);
 }
 report.checks.push('six responsive configurations including 320px/24px text; six redesigned pages render and actions remain reachable');
 assert.deepEqual(report.errors,[]);assert.deepEqual(report.badResponses,[]);report.passed=true;
}catch(e){report.failure=String(e);if(page&&!page.isClosed())await page.screenshot({path:out+'/failure.png'});throw e;}
finally{await browser?.close();server.kill();await fs.writeFile(out+'/report.json',JSON.stringify(report,null,2));console.log(JSON.stringify({passed:report.passed,checks:report.checks.length,screens:report.screens.length,errors:report.errors.length,badResponses:report.badResponses.length}));}
