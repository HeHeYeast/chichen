// Work E playable acceptance: legacy first chapter + situational purchases and the O04 display, through real controls.
// Since 2026-10-07 (batch 3) orders sit on the 生意 page and finish with one 「交付」 / 「摆出来」 (no 接取 / 预留 /
// 逐只交付 steps); reservations an older save holds are covered by the unit tests (tests/order-delivery.test.mjs).
import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import {resolve,join} from 'node:path';
import {fileURLToPath} from 'node:url';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {freshState,char} from '../web/engine.js';
import {syncProgress} from '../web/progression.js';
import {setSlot,setStoryAmount} from './qa-business-stock.mjs';
const root=fileURLToPath(new URL('../',import.meta.url)),require=createRequire(import.meta.url);
const candidates=[process.argv[2],process.env.CHICK_PLAYWRIGHT_PACKAGE,'playwright','playwright-core',process.env.APPDATA&&join(process.env.APPDATA,'npm/node_modules/gsd-pi/node_modules/playwright-core')].filter(Boolean);
let packagePath;for(const p of candidates){try{packagePath=require.resolve(p);break;}catch{}}
if(!packagePath)throw Error('An existing Playwright package is required.');
const {chromium}=require(packagePath),output=resolve(root,'artifacts/qa/work-e');await mkdir(output,{recursive:true});
const now=1800000000000,seed=freshState(now,4242);
seed.total=Object.fromEntries(['0:0','0:3','0:8','0:1','0:2','0:10','0:17','0:20','0:5'].map((k,i)=>[k,i?5:300]));
seed.farm={'0:0':40,'0:3':10,'0:8':10,'0:10':2,'0:17':2,'0:20':2};seed.toolLevels[1]=0;seed.toolLevels[2]=0;seed.cp=5000;seed.progress.tutorialSeen=true;syncProgress(seed);
const checks=[],errors=[],screens=[];let browser,p,passed=false;
const server=spawn(process.execPath,['server.mjs','0'],{cwd:root,stdio:['ignore','pipe','pipe'],windowsHide:true});
const read=()=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
const screenshot=async name=>{await p.waitForTimeout(120);await p.screenshot({path:resolve(output,name+'.png')});screens.push(name);};
const start=async()=>{await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.locator('.workshop-launch').waitFor();await p.waitForTimeout(650);};
const jump=async at=>{await p.evaluate(({at,base})=>{window.__workEOffset=at-base;sessionStorage.setItem('work-e-clock',String(window.__workEOffset));},{at,base:now});};
const closePanels=async()=>{while(await p.locator('.screen-panel:visible .close:visible').count())await p.locator('.screen-panel:visible .close:visible').first().click();};
const ok=async()=>{await p.locator('[data-yes]').click();};
async function orders(){await closePanels();await p.getByRole('button',{name:'生意',exact:true}).click();await p.locator('.business-screen .bh-orders').waitFor();}
async function fit(){await p.evaluate(()=>new Promise(done=>requestAnimationFrame(()=>requestAnimationFrame(done))));const r=await p.locator('.business-screen').evaluate(el=>{const a=el.getBoundingClientRect(),g=document.querySelector('#game').getBoundingClientRect(),b=el.querySelector('.bs-main-scroll');return {inside:a.left>=g.left-1&&a.right<=g.right+1&&a.top>=g.top-1&&a.bottom<=g.bottom+1,overflow:b.scrollWidth-b.clientWidth};});assert.ok(r.inside);assert.ok(r.overflow<2);}
const takeDone=async()=>{await p.locator('#dialog-layer .gd-actions button',{hasText:'收下'}).click();};
async function cookAndHarvest(){await closePanels();await p.getByRole('button',{name:'厨房',exact:true}).click();await p.locator('[data-control-id="tool:0"]').click();await p.locator('.cooking-dialog').waitFor();await ok();const s=await read();await jump(s.batch.ends+1000);await p.waitForTimeout(3300);for(let i=0;i<24;i++)await p.locator(`[data-control-id="egg:${i}"]`).press('Enter');assert.equal((await read()).batch.eggs.every(e=>e.collected),true);}
try{
  const base=await new Promise((done,fail)=>{const timer=setTimeout(()=>fail(Error('server start timeout')),10000);server.once('error',fail);server.stdout.on('data',chunk=>{const m=String(chunk).match(/http:\/\/127\.0\.0\.1:\d+/);if(m){clearTimeout(timer);done(m[0]);}});});
  browser=await chromium.launch({headless:true,...(process.env.CHICK_QA_CHROME?{executablePath:process.env.CHICK_QA_CHROME}:{})});
  const context=await browser.newContext({viewport:{width:390,height:844},reducedMotion:'reduce'});
  await context.addInitScript(({seed,now})=>{if(location.protocol==='about:')return;window.__workEOffset=Number(sessionStorage.getItem('work-e-clock')??0);Date.now=()=>now+window.__workEOffset+Math.floor(performance.now());if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));},{seed,now});
  p=await context.newPage();p.on('pageerror',e=>errors.push(e.message));await p.goto(base+'/');await start();

  // Legacy first chapter still works exactly once beside the new orders.
  await p.locator('#main-nav').getByRole('button',{name:'生意',exact:true}).click();await p.locator('#panels .bh-order:has(.bh-tag.is-story) [data-order-detail]').click();await p.locator('#panels [data-order-story]').click();await p.locator('[data-accept="first-sale"]').click();
  let s=await read();const legacyCP=s.cp;await setStoryAmount(p,'first-sale',12);await p.locator('[data-deliver="first-sale"]').click();if(await p.locator('[data-yes]').count())await ok();
  s=await read();assert.equal(s.progress.orders['first-sale'].completed,true);const legacyPaid=s.cp-legacyCP;assert.ok(legacyPaid>12);
  checks.push(`旧第一采购在新版照常接取并一次交付12只，获得${legacyPaid}CP（含原酬谢一次）`);
  await orders();assert.equal((await read()).expansion.orders.proposals.length,0);assert.equal(await p.locator('#panels .bh-main [data-order-detail^="proposal:"]').count(),0,'no 询问 before a milestone');await fit();await screenshot('orders-empty-390');
  checks.push('生意页订单板：旧三章是一张卡，达到240/8后开放情境采购；打开页面本身不产生询问');

  await cookAndHarvest();await orders();
  s=await read();const proposal=s.expansion.orders.proposals[0];assert.equal(proposal?.templateId,'O01','a finished batch is a real milestone');
  await screenshot('proposal-O01');const cp0=s.cp,farm0=structuredClone(s.farm);
  await p.locator(`[data-order-act="proposal:${proposal.id}"]`).click();await p.locator('#dialog-layer .order-done').waitFor();
  assert.match(await p.locator('#dialog-layer .order-done').innerText(),/街坊便签/);await screenshot('order-done-O01');await takeDone();
  s=await read();assert.equal(s.expansion.orders.active.length,0);assert.equal(s.expansion.orders.proposals.length,0);assert.ok(s.expansion.facts.predicateWitnesses['O01:complete']);
  assert.deepEqual(s.expansion.orders.templateProgress.O01,{accepted:1,completed:1,cancelled:0,skipped:0});
  const taken=Object.fromEntries(Object.keys(farm0).map(k=>[k,farm0[k]-(s.farm[k]??0)]).filter(([,n])=>n>0));
  assert.equal(Object.values(taken).reduce((a,b)=>a+b,0),12);for(const k of Object.keys(taken))assert.ok(s.farm[k]>=1,`${k}: one stays at home`);
  const bonusCP=s.cp-cp0-Object.entries(taken).reduce((n,[k,q])=>n+q*char(...k.split(':').map(Number)).cp_1,0);assert.equal(bonusCP,12);
  checks.push('收完一锅后出现“街坊备早饭”；一次「交付」就接取并交齐12只，付基础价另付酬谢12CP，记下“街坊便签”，每种至少留1只在家');

  await cookAndHarvest();await orders();s=await read();
  const display=s.expansion.orders.proposals.find(x=>x.templateId==='O04');assert.ok(display,'the next milestone rotates to O04');
  const before={farm:structuredClone(s.farm),cp:s.cp};await p.locator(`[data-order-act="proposal:${display.id}"]`).click();await p.locator('#dialog-layer .order-done').waitFor();
  assert.match(await p.locator('#dialog-layer .order-done').innerText(),/花叶明信片/);await screenshot('display-done');await takeDone();
  s=await read();assert.deepEqual(s.farm,before.farm);assert.equal(s.cp,before.cp);assert.ok(s.expansion.facts.predicateWitnesses['O04:display']);
  checks.push('花叶陈列：「摆出来」一次完成，3种各在家1只供看，不扣货、不付款，记下“花叶明信片”');

  for(const [width,height]of [[320,568],[1280,900]]){await p.setViewportSize({width,height});await fit();await screenshot(`orders-${width}`);}
  await p.setViewportSize({width:390,height:844});
  const saved=await read();await p.reload();await start();assert.deepEqual((await read()).expansion.orders,saved.expansion.orders);assert.equal((await read()).cp,saved.cp);
  checks.push('320/390/1280无横溢；刷新后订单与余额不变');
  assert.deepEqual(errors,[]);passed=true;await context.close();
}catch(error){if(p&&!p.isClosed()){await writeFile(resolve(output,'failure.txt'),await p.locator('body').innerText());await screenshot('failure');}throw error;}
finally{await browser?.close();server.kill();const report={checkedAt:new Date().toISOString(),passed,checks,screens,errors,isolatedProfile:true,acceleratedClock:true,realDeviceTest:false};await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));}
