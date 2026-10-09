// Work I playable acceptance: the first regular's story arrives through a manual
// purchase (no business) and through a real MN1 visitor; refresh never skips or repeats.
import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import {resolve,join} from 'node:path';
import {fileURLToPath} from 'node:url';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {freshState,char} from '../web/engine.js';
import {syncProgress} from '../web/progression.js';
import {setSlot,setStock,stockOf} from './qa-business-stock.mjs';
const root=fileURLToPath(new URL('../',import.meta.url)),require=createRequire(import.meta.url);
const candidates=[process.argv[2],process.env.CHICK_PLAYWRIGHT_PACKAGE,'playwright','playwright-core',process.env.APPDATA&&join(process.env.APPDATA,'npm/node_modules/gsd-pi/node_modules/playwright-core')].filter(Boolean);
let packagePath;for(const p of candidates){try{packagePath=require.resolve(p);break;}catch{}}
if(!packagePath)throw Error('An existing Playwright package is required.');
const {chromium}=require(packagePath),output=resolve(root,process.env.SHOP_QA_OUT??'artifacts/qa/work-i');await mkdir(output,{recursive:true});
const now=1800000000000,seed=freshState(now,4242);
seed.total=Object.fromEntries(['0:0','0:3','0:8','0:1','0:2','0:10','0:17','0:20','0:5'].map((k,i)=>[k,i?5:300]));
seed.farm={'0:0':40,'0:3':10,'0:8':10,'0:10':2,'0:17':2,'0:20':2};seed.toolLevels[1]=0;seed.toolLevels[2]=0;seed.cp=5000;seed.progress.tutorialSeen=true;seed.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};syncProgress(seed);
const checks=[],errors=[],screens=[];let browser,p,passed=false;
const server=spawn(process.execPath,['server.mjs','0'],{cwd:root,stdio:['ignore','pipe','pipe'],windowsHide:true});
const read=()=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
const screenshot=async name=>{await p.waitForTimeout(120);await p.screenshot({path:resolve(output,name+'.png')});screens.push(name);};
const start=async()=>{await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.locator('.workshop-launch').waitFor();await p.waitForTimeout(650);};
const jump=async at=>{await p.evaluate(({at,base})=>{window.__workEOffset=at-base;sessionStorage.setItem('work-e-clock',String(window.__workEOffset));},{at,base:now});};
const closePanels=async()=>{if(await p.locator('.bs-dialog[open]').count())await p.locator('[data-business-sheet-close]').click();if(await p.locator('.golden-business,.shop-subpage').count())await p.locator('#main-nav').getByRole('button',{name:'厨房',exact:true}).click();while(await p.locator('.screen-panel .close:visible').count())await p.locator('.screen-panel .close:visible').first().click();};
const ok=async()=>{await p.locator('[data-yes]').click();};
// 2026-10-07 (batch 3): orders sit on the 生意 page itself, one 「交付」 each (no 接取 / 预留 / 逐只交付 steps)
// the 生意 tab returns to the last 生意 page (常客 here): ‹ leads back to the home
async function orders(){await closePanels();await p.getByRole('button',{name:'生意',exact:true}).click();if(await p.locator('.shop-subpage [data-shop-back]').count())await p.locator('.shop-subpage [data-shop-back]').click();await p.locator('.business-screen .bh-orders').waitFor();}
async function fit(){await p.evaluate(()=>new Promise(done=>requestAnimationFrame(()=>requestAnimationFrame(done))));const r=await p.locator('.business-screen').evaluate(el=>{const a=el.getBoundingClientRect(),g=document.querySelector('#game').getBoundingClientRect(),b=el.querySelector('.bs-main-scroll');return {inside:a.left>=g.left-1&&a.right<=g.right+1&&a.top>=g.top-1&&a.bottom<=g.bottom+1,overflow:b.scrollWidth-b.clientWidth};});assert.ok(r.inside);assert.ok(r.overflow<2);}
const takeDone=async()=>{await p.locator('#dialog-layer .order-done').waitFor();await p.locator('#dialog-layer .gd-actions button',{hasText:'收下'}).click();};
async function cookAndHarvest(){await closePanels();await p.locator('[data-control-id="tool:0"]').click();await p.locator('.cooking-dialog').waitFor();await ok();const s=await read();await jump(s.batch.ends+1000);await p.waitForTimeout(3300);for(let i=0;i<24;i++)await p.locator(`[data-control-id="egg:${i}"]`).press('Enter');assert.equal((await read()).batch.eggs.every(e=>e.collected),true);}
const contextFor=async source=>{const c=await browser.newContext({viewport:{width:390,height:844},reducedMotion:'reduce'});
  await c.addInitScript(({seed,now})=>{if(location.protocol==='about:')return;window.__workEOffset=Number(sessionStorage.getItem('work-e-clock')??0);Date.now=()=>now+window.__workEOffset+Math.floor(performance.now());if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));},{seed:source,now});
  p=await c.newPage();p.on('pageerror',e=>errors.push(e.message));await p.goto(base+'/');await start();return c;};
const regulars=async()=>{await closePanels();await p.getByRole('button',{name:'生意',exact:true}).click();if(await p.locator('.shop-subpage [data-shop-back]').count())await p.locator('.shop-subpage [data-shop-back]').click();await p.locator('[data-trade-view="regulars"]').first().click();await p.locator('.regulars-screen').waitFor();};
async function fitRegulars(){await p.evaluate(()=>new Promise(done=>requestAnimationFrame(()=>requestAnimationFrame(done))));const r=await p.locator('.regulars-screen').evaluate(el=>{const a=el.getBoundingClientRect(),g=document.querySelector('#game').getBoundingClientRect(),b=el.querySelector('.regulars-scroll');return {inside:a.left>=g.left-1&&a.right<=g.right+1&&a.top>=g.top-1&&a.bottom<=g.bottom+1,overflow:b.scrollWidth-b.clientWidth};});assert.ok(r.inside);assert.ok(r.overflow<2);}
let base;
try{
  base=await new Promise((done,fail)=>{const timer=setTimeout(()=>fail(Error('server start timeout')),10000);server.once('error',fail);server.stdout.on('data',chunk=>{const m=String(chunk).match(/http:\/\/127\.0\.0\.1:\d+/);if(m){clearTimeout(timer);done(m[0]);}});});
  browser=await chromium.launch({headless:true,...(process.env.CHICK_QA_CHROME?{executablePath:process.env.CHICK_QA_CHROME}:{})});

  // A. No business at all: a manual O01 purchase queues RG1-1 on the regulars page.
  let context=await contextFor(seed);
  await regulars();let text=await p.locator('.regulars-screen').innerText();assert.match(text,/旧厨房老顾客/);assert.match(text,/还没来访/);await fitRegulars();await screenshot('regulars-list-390');
  await cookAndHarvest();await orders();let s=await read();const proposal=s.expansion.orders.proposals.find(x=>x.templateId==='O01');assert.ok(proposal);await screenshot('orders-proposal-390');
  for(const [width,height]of [[320,568],[1280,900]]){await p.setViewportSize({width,height});await fit();await screenshot(`orders-${width}`);}await p.setViewportSize({width:390,height:844});
  await p.locator(`[data-order-act="proposal:${proposal.id}"]`).click();await screenshot('orders-done');await takeDone();
  checks.push('生意页订单板：「街坊备早饭」一次「交付」完成（不再接取、预留、逐只交付），结果卡写明收入和情报');
  s=await read();assert.ok(s.expansion.facts.predicateWitnesses['O01:complete']);assert.equal(s.expansion.business.sequence,0,'no business was opened');assert.equal(s.expansion.orders.active.length,0);
  assert.equal(s.expansion.regulars.RG1.pendingStage.id,'RG1-1');assert.ok(s.expansion.collections.entitlements['NOTE-RG1-1']);
  await regulars();text=await p.locator('.regulars-screen').innerText();assert.match(text,/有新故事/);await screenshot('regulars-unread');
  await p.locator('[data-regular-open="RG1"]').click();await p.locator('[data-regular-read]').click();assert.match(await p.locator('.regulars-screen').innerText(),/今天这份火候正好/);await screenshot('regulars-reading');
  const cp=s.cp,farm=structuredClone(s.farm);await p.locator('[data-regular-read]').click();assert.equal((await read()).expansion.regulars.RG1.readStages.length,0);await p.locator('[data-regular-read]').click();
  s=await read();assert.deepEqual(s.expansion.regulars.RG1.readStages,['RG1-1']);assert.equal(s.cp,cp);assert.deepEqual(s.farm,farm);
  text=await p.locator('.regulars-screen').innerText();assert.match(text,/分开包/);assert.match(text,/「茶香便当」有效接待一次/);assert.ok(!/MN2|O07/.test(text),"content IDs are shown as names");await screenshot('regulars-next-stage');
  for(const [width,height]of [[320,568],[1280,900]]){await p.setViewportSize({width,height});await p.evaluate(()=>new Promise(resolve=>requestAnimationFrame(()=>requestAnimationFrame(resolve))));await fitRegulars();await screenshot(`regulars-${width}`);}
  await p.setViewportSize({width:390,height:844});
  checks.push('不开营业：交付完成“街坊备早饭”(O01)即在常客页出现第一段；读完才显示第二段的两条路（MN2营业或O07采购），读取不扣货不付款');
  await context.close();

  // B. Business path: a real MN1 session's visitor brings the story; refresh before reading.
  context=await contextFor(seed);
  await closePanels();await p.getByRole('button',{name:'生意',exact:true}).click();if(await p.locator('.shop-subpage [data-shop-back]').count())await p.locator('.shop-subpage [data-shop-back]').click();await p.locator('.business-screen').waitFor();
  if(await p.locator('.golden-business').count()){await p.locator('.bh-sign').first().click();if(await p.locator('[data-business-clear]').count())await p.locator('[data-business-clear]').click();}
  if(await p.locator('[data-business-filter="all"]').count())await p.locator('[data-business-filter="all"]').click();
  for(const key of ['0:0','0:3'])await setStock(p,key,6);
  await p.locator('[data-business-sheet-close]').click();
  await p.locator('[data-business-open]').click();await ok();s=await read();const began=s.expansion.business.active.startAt;assert.ok(s.expansion.business.active.visitorCandidates.includes('RG1'));
  await jump(began+2*3600000+1000);await p.waitForFunction(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')).expansion.business.active?.totalSold===6);
  s=await read();assert.equal(s.expansion.business.visitorSequence,0);assert.equal(s.expansion.regulars.RG1?.pendingStage??null,null);
  await p.reload();await start();await regulars();await p.locator('[data-regular-open="RG1"]').click();assert.equal(await p.locator('[data-regular-read]').count(),0);assert.ok(!(await p.locator('.regulars-screen').innerText()).includes('今天这份火候正好'));await screenshot('six-sales-waits-for-visitor');
  await closePanels();await p.getByRole('button',{name:'生意',exact:true}).click();if(await p.locator('.shop-subpage [data-shop-back]').count())await p.locator('.shop-subpage [data-shop-back]').click();
  checks.push('首窗只售6只时没有来客、没有首段阅读按钮；刷新与打开常客页也不提前排队');
  await jump(began+4*3600000+1000);await p.waitForFunction(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')).expansion.business.lastReport?.totalSold===12);
  await p.locator('.business-receipt-head').waitFor();text=await p.locator('.business-screen').innerText();assert.match(text,/来访的常客/);assert.match(text,/旧厨房老顾客带来「早饭趁热」/);await screenshot('receipt-visitor');
  s=await read();assert.equal(s.expansion.business.lastReport.validMenu,true);const ledger=JSON.stringify(s.expansion.collections.entitlements),incomeCP=s.expansion.business.lastReport.income;
  await p.locator('[data-business-regulars="RG1"]').click();await p.locator('.regulars-screen').waitFor();assert.match(await p.locator('.regulars-screen').innerText(),/可继续 · 「家常小铺」有效接待一次/);
  await p.reload();await start();s=await read();assert.equal(s.expansion.regulars.RG1.pendingStage.id,'RG1-1','refresh keeps the unread stage');assert.equal(JSON.stringify(s.expansion.collections.entitlements),ledger,'no second note');
  await regulars();await p.locator('[data-regular-open="RG1"]').click();await p.locator('[data-regular-read]').click();await p.locator('[data-regular-read]').click();await p.locator('[data-regular-read]').click();
  s=await read();assert.deepEqual(s.expansion.regulars.RG1.readStages,['RG1-1']);assert.equal(s.expansion.business.lastReport.income,incomeCP);
  await p.reload();await start();s=await read();assert.deepEqual(s.expansion.regulars.RG1.readStages,['RG1-1']);assert.equal(s.expansion.regulars.RG1.pendingStage,null);
  checks.push('开MN1营业12只：第2窗来客带来“早饭趁热”，账单列出并可“去读”；读前刷新不丢不重复纸条，读后刷新不跳段、账款不变');
  await context.close();
  assert.deepEqual(errors,[]);passed=true;
}catch(error){if(p&&!p.isClosed()){await writeFile(resolve(output,'failure.txt'),await p.locator('body').innerText());await screenshot('failure');}throw error;}
finally{await browser?.close();server.kill();const report={checkedAt:new Date().toISOString(),passed,checks,screens,errors,isolatedProfile:true,acceleratedClock:true,realDeviceTest:false};await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));}
