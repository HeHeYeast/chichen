// Isolated UI fitting checks; never opens a player's browser profile or phone save.
import {createRequire} from 'node:module';
import {spawn} from 'node:child_process';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {LEGACY193} from '../web/legacy-content.js';
import {syncProgress} from '../web/progression.js';
import {reduceFacts} from '../web/facts.js';
import {openBusiness} from '../web/business.js';
import {orderMilestone,acceptProposal} from '../web/orders.js';
const require=createRequire(import.meta.url),{chromium}=require(process.env.CHICK_PLAYWRIGHT_PACKAGE??process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const baseline=process.argv.includes('--baseline'),out=`artifacts/ui-fit-20260930/${baseline?'before':'after'}`;
await mkdir(out,{recursive:true});
const at=1800000000000;
const source=E.freshState(at,4242);source.kitchenLevel=2;source.duck=true;source.cp=5000;
source.toolLevels=Array(9).fill(0);source.progress.tutorialSeen=true;
source.total=Object.fromEntries(LEGACY193.characters.flatMap((list,egg)=>list.map(c=>[`${egg}:${c.id}`,c.id===0&&egg===0?9000:2])));
source.farm={'0:0':40,'0:3':30,'0:4':20,'0:8':10,'0:18':10,'0:75':10,'1:0':20};
source.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};syncProgress(source);
reduceFacts(source,['MN1','MN3'].map((menuId,i)=>({kind:'businessWitness',sessionId:`business-${i+500}`,menuId,soldByKey:{'0:0':6},roleSales:{},fullSoldByKey:{},fullRoleSales:{},valid:true,complete:false})));
for(const id of ['R-S1','R-S2'])source.expansion.discovery.cards[id]=++source.meta.factSeq;
for(const id of ['77','78'])source.expansion.discovery.identified[id]=++source.meta.factSeq;
source.expansion.regions.guideFlags=['GUIDE-B'];
const server=spawn(process.execPath,['server.mjs','0'],{stdio:['ignore','pipe','pipe'],windowsHide:true});
const base=await new Promise((resolve,reject)=>{server.on('error',reject);server.stdout.on('data',b=>{const m=String(b).match(/http:\/\/127\.0\.0\.1:\d+/);if(m)resolve(m[0]);});});
const browser=await chromium.launch({headless:true,executablePath:process.env.CHICK_QA_CHROME||'C:/Program Files/Google/Chrome/Application/chrome.exe'});
let page,context;const errors=[],screens=[];
async function load(width=390,height=780,mode='empty'){
 await context?.close();context=await browser.newContext({viewport:{width,height},reducedMotion:'reduce'});
 const s=structuredClone(source);s.music=false;s.sound=false;s.selected=[0];s.ingredients={0:10,1:10};E.startBatch(s,0,at,()=>.5,()=>.5);
 if(mode==='trading')openBusiness(s,{menuId:'MN1',stock:{'0:18':9,'0:75':4,'1:0':5}},at);
 if(mode==='orders'){orderMilestone(s,at,'batch');acceptProposal(s,s.expansion.orders.proposals[0].id,{},at);orderMilestone(s,at,'batch');}
 await context.addInitScript(({s,at})=>{Date.now=()=>at;localStorage.setItem('chick-kitchen-v1',JSON.stringify(s));localStorage.setItem('chick-kitchen-ui-v1',JSON.stringify({bookTab:'lore',seenSupplyMove:true}));},{s,at});
 page=await context.newPage();page.on('pageerror',e=>errors.push(e.message));page.on('response',r=>{if(r.status()>=400)errors.push(`${r.status()} ${r.url()}`);});
 await page.goto(base);await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.waitForTimeout(700);await page.evaluate(()=>document.fonts.ready);
}
async function nav(name){await page.locator('#main-nav').getByRole('button',{name,exact:true}).click();await page.waitForTimeout(120);}
async function shot(name){
 await page.screenshot({path:`${out}/${name}.png`});
 const metrics=await page.evaluate(()=>{
  const rect=e=>{const r=e.getBoundingClientRect();return {x:r.x,y:r.y,w:r.width,h:r.height};};
  const scrolls=[...document.querySelectorAll('#panels .scroll,.bs-main-scroll,.journey-body')].map(e=>({class:e.className,...rect(e),extra:e.scrollHeight-e.clientHeight,xExtra:e.scrollWidth-e.clientWidth}));
  const labels=[...document.querySelectorAll('.bs-nameplate,.bs-summary,.family-stamp,.journey-destination-label,.regulars-card-text,.book-path')].map(e=>({class:e.className,text:e.textContent.trim(),xExtra:e.scrollWidth-e.clientWidth,yExtra:e.scrollHeight-e.clientHeight}));
  const today=document.querySelector('.bh-today');
  return {nav:rect(document.querySelector('#main-nav')),today:today?{...rect(today),bottom:today.getBoundingClientRect().bottom}:null,scrolls,labels,bodyExtra:document.body.scrollWidth-innerWidth,broken:[...document.querySelectorAll('#panels img')].filter(i=>!i.complete||!i.naturalWidth).map(i=>i.src)};
 });screens.push({name,...metrics});assert.equal(metrics.broken.length,0);assert.ok(metrics.bodyExtra<2);
 if(!baseline){
  assert.ok(metrics.nav.h<=68,`${name} nav too tall`);assert.ok(metrics.scrolls.every(s=>s.xExtra<=2),`${name} horizontal overflow`);
  assert.ok(metrics.labels.every(s=>s.xExtra<=2&&s.yExtra<=2),`${name} text overflow: ${JSON.stringify(metrics.labels)}`);
  if(/^(regulars|projects|journey-map|journey-region)-\d+$/.test(name))assert.ok(metrics.scrolls.every(s=>s.extra<=2),`${name} should fit without scrolling: ${JSON.stringify(metrics.scrolls)}`);
  // an order's sheet sits over the (scrolling) 生意 page: the sheet itself shows everything
  if(/^orders(-active)?-\d+$/.test(name))assert.ok(metrics.scrolls.filter(s=>!/bs-main-scroll/.test(s.class)).every(s=>s.extra<=2),`${name}: the order sheet should fit without scrolling: ${JSON.stringify(metrics.scrolls)}`);
  // 2026-10-07 (batch 3): the 生意 page scrolls on to its order board, but 今日营业 is whole on the first screen
  if(/^business(-trading)?-\d+$/.test(name))assert.ok(metrics.today&&metrics.today.bottom<=metrics.nav.y+1,`${name}: 今日营业 must be whole above the navigation: ${JSON.stringify(metrics.today)}`);
 }
}
try{
 for(const [w,h] of [[390,780],[320,568],[430,900]]){
  await load(w,h);await shot(`kitchen-${w}`);
  await nav('生意');await shot(`business-${w}`);
  // 订单: an order's own sheet (its name opens it); 常客 / 项目: the small cards
  for(const kind of ['orders','regulars','projects']){if(kind==='orders'){if(!await page.locator('#panels .bh-main [data-order-detail]').count())continue;await page.locator('#panels .bh-main [data-order-detail]').first().click();await shot(`${kind}-${w}`);await page.locator('#panels [data-business-sheet-close]').click();continue;}await page.locator(`[data-trade-view="${kind}"]`).first().click();await shot(`${kind}-${w}`);await page.locator('.shop-subpage [data-shop-back]').click();}
  await nav('寻访');await shot(`journey-map-${w}`);await page.locator('[data-journey-enter]').click();await shot(`journey-region-${w}`);
  await nav('图鉴');if(!baseline)await page.locator('#panels [data-book-tab="species"][aria-selected="true"]').waitFor();await shot(`book-entry-${w}`);
  await page.locator('#panels [data-book-tab="calendar"]').first().click();await shot(`book-calendar-${w}`);
  await nav('厨房');await nav('图鉴');
  if(!baseline)assert.equal(await page.locator('#panels [data-book-tab="species"][aria-selected="true"]').count(),1,'main navigation opens the species bookmark');
  await load(w,h,'trading');await nav('生意');await shot(`business-trading-${w}`);
  await page.locator('#panels .bh-today .gd-btn[data-business-sheet="ledger"]').click();await shot(`business-ledger-${w}`);await nav('生意');
  await page.locator('[data-trade-view="projects"]').first().click();await page.locator('[data-project-open]').first().click();await shot(`project-detail-${w}`);
  await load(w,h,'orders');await nav('生意');await shot(`business-orders-${w}`);await page.locator('#panels .bh-main [data-order-detail^="order:"]').first().click();await shot(`orders-active-${w}`);await page.locator('#panels [data-business-sheet-close]').click();
 }
 assert.deepEqual(errors,[]);
}finally{await browser.close();server.kill();await writeFile(`${out}/report.json`,JSON.stringify({baseline,errors,screens},null,2));}
console.log(`UI fit: ${screens.length} screens, ${errors.length} errors`);
