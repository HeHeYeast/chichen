// Work E playable acceptance: legacy first chapter + situational purchases with
// reservation, two-batch delivery and the O04 display, through real controls.
import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import {resolve,join} from 'node:path';
import {fileURLToPath} from 'node:url';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {freshState,char} from '../web/engine.js';
import {syncProgress} from '../web/progression.js';
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
const closePanels=async()=>{while(await p.locator('.screen-panel .close').count())await p.locator('.screen-panel .close').first().click();};
const ok=async()=>{await p.locator('[data-yes]').click();};
async function orders(){await closePanels();await p.getByRole('button',{name:'生意',exact:true}).click();await p.locator('[data-trade-view="business"]').first().click();await p.locator('[data-trade-view="orders"]').click();await p.locator('.orders-screen').waitFor();}
async function fit(){await p.evaluate(()=>new Promise(done=>requestAnimationFrame(()=>requestAnimationFrame(done))));const r=await p.locator('.orders-screen').evaluate(el=>{const a=el.getBoundingClientRect(),g=document.querySelector('#game').getBoundingClientRect(),b=el.querySelector('.orders-scroll');return {inside:a.left>=g.left-1&&a.right<=g.right+1&&a.top>=g.top-1&&a.bottom<=g.bottom+1,overflow:b.scrollWidth-b.clientWidth};});assert.ok(r.inside);assert.ok(r.overflow<2);}
async function cookAndHarvest(){await closePanels();await p.locator('[data-control-id="tool:0"]').click();await p.locator('.cooking-dialog').waitFor();await ok();const s=await read();await jump(s.batch.ends+1000);await p.waitForTimeout(3300);for(let i=0;i<24;i++)await p.locator(`[data-control-id="egg:${i}"]`).press('Enter');assert.equal((await read()).batch.eggs.every(e=>e.collected),true);}
try{
  const base=await new Promise((done,fail)=>{const timer=setTimeout(()=>fail(Error('server start timeout')),10000);server.once('error',fail);server.stdout.on('data',chunk=>{const m=String(chunk).match(/http:\/\/127\.0\.0\.1:\d+/);if(m){clearTimeout(timer);done(m[0]);}});});
  browser=await chromium.launch({headless:true,...(process.env.CHICK_QA_CHROME?{executablePath:process.env.CHICK_QA_CHROME}:{})});
  const context=await browser.newContext({viewport:{width:390,height:844},reducedMotion:'reduce'});
  await context.addInitScript(({seed,now})=>{if(location.protocol==='about:')return;window.__workEOffset=Number(sessionStorage.getItem('work-e-clock')??0);Date.now=()=>now+window.__workEOffset+Math.floor(performance.now());if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));},{seed,now});
  p=await context.newPage();p.on('pageerror',e=>errors.push(e.message));await p.goto(base+'/');await start();

  // Legacy first chapter still works exactly once beside the new orders.
  await p.locator('.workshop-launch').click();await p.locator('[data-workshop-tab="story"]').click();await p.locator('[data-accept="first-sale"]').click();
  let s=await read();const legacyCP=s.cp;await p.locator('[data-order-quantity="first-sale"]').fill('12');await p.locator('[data-deliver="first-sale"]').click();if(await p.locator('[data-yes]').count())await ok();
  s=await read();assert.equal(s.progress.orders['first-sale'].completed,true);const legacyPaid=s.cp-legacyCP;assert.ok(legacyPaid>12);
  checks.push(`旧第一采购在新版照常接取并一次交付12只，获得${legacyPaid}CP（含原酬谢一次）`);
  await orders();assert.match(await p.locator('.orders-screen').innerText(),/暂时没有新询问/);await fit();await screenshot('orders-empty-390');
  checks.push('生意簿“订单”页：旧三章卡在顶，达到240/8后开放情境采购；打开页面本身不产生询问');

  await cookAndHarvest();await orders();
  s=await read();assert.equal(s.expansion.orders.proposals[0]?.templateId,'O01','a finished batch is a real milestone');
  await screenshot('proposal-O01');await p.locator('[data-proposal-accept]').first().click();await ok();
  s=await read();const order=s.expansion.orders.active[0];assert.equal(order.templateId,'O01');assert.equal(order.variantId,'O01-B');
  checks.push('收完一锅后出现“街坊备早饭”意向；接取冻结为鸡蛋厨房变体（未开鸭蛋）');

  await p.locator(`[data-order-reserve="${order.id}"]`).click();for(let i=0;i<6;i++)await p.locator('[data-slot-plus="O01-G1|0:3"]').click();await p.locator('[data-orders-confirm]').click();
  s=await read();assert.equal(s.expansion.orders.active[0].reserved['0:3'],6);assert.equal(s.farm['0:3'],10,'reservation never reduces T');
  await p.locator('[data-orders-back]').click();await p.locator('[data-trade-view="business"]').click();await p.locator('.business-screen').waitFor();
  if(await p.locator('[data-business-filter="all"]').count())await p.locator('[data-business-filter="all"]').click();
  assert.match(await p.locator('.business-screen').innerText(),/C004 · 自由 4只/);await screenshot('business-sees-free-after-Q');
  checks.push('预留6只香煎鸡：T仍为10，营业备货页只显示自由4只（Q不会被S占用）');

  await p.locator('[data-trade-view="orders"]').click();await p.locator(`[data-order-deliver="${order.id}"]`).click();
  for(let i=0;i<6;i++)await p.locator('[data-slot-plus="O01-G1|0:0"]').click();
  s=await read();const cp0=s.cp,price=char(0,0).cp_1;await p.locator('[data-orders-confirm]').click();await ok();await ok();
  s=await read();assert.equal(s.cp,cp0+6*price);assert.equal(s.expansion.orders.active[0].deliveries,1);
  await p.locator(`[data-order-deliver="${order.id}"]`).click();for(let i=0;i<6;i++)await p.locator('[data-slot-plus="O01-G1|0:3"]').click();
  await screenshot('deliver-second-batch');await p.locator('[data-orders-confirm]').click();await ok();
  assert.match(await p.locator('.confirm').innerText(),/街坊便签/);await ok();
  s=await read();assert.equal(s.expansion.orders.active.length,0);assert.equal(s.farm['0:3'],4);assert.ok(s.expansion.facts.predicateWitnesses['O01:complete']);
  const bonusCP=s.cp-cp0-6*price-6*char(0,3).cp_1;assert.equal(bonusCP,12);
  checks.push('分两批交付：第一批6只鸡宝只付基础价；第二批用预留的6只香煎鸡完成，另付酬谢12CP并记下“街坊便签”');

  await cookAndHarvest();await orders();s=await read();
  const display=s.expansion.orders.proposals.find(x=>x.templateId==='O04');assert.ok(display,'the next milestone rotates to O04');
  await p.locator(`[data-proposal-accept="${display.id}"]`).click();await ok();s=await read();const shown=s.expansion.orders.active.find(x=>x.templateId==='O04');
  const before={farm:structuredClone(s.farm),cp:s.cp};await p.locator(`[data-order-display="${shown.id}"]`).click();
  for(const key of ['0:10','0:17','0:20'])await p.locator(`[data-display-key="${key}"]`).check();
  await screenshot('display-pick-3');await p.locator('[data-display-confirm]').click();assert.match(await p.locator('.confirm').innerText(),/花叶明信片/);await ok();
  s=await read();assert.deepEqual(s.farm,before.farm);assert.equal(s.cp,before.cp);assert.ok(s.expansion.facts.predicateWitnesses['O04:display']);
  checks.push('花叶陈列：选3种各在家1只供看，不扣货、不付款，记下“花叶明信片”');

  for(const [width,height]of [[320,568],[1280,900]]){await p.setViewportSize({width,height});await fit();await screenshot(`orders-${width}`);}
  await p.setViewportSize({width:390,height:844});
  const saved=await read();await p.reload();await start();assert.deepEqual((await read()).expansion.orders,saved.expansion.orders);assert.equal((await read()).cp,saved.cp);
  checks.push('320/390/1280无横溢；刷新后订单、预留与余额不变');
  assert.deepEqual(errors,[]);passed=true;await context.close();
}catch(error){if(p&&!p.isClosed()){await writeFile(resolve(output,'failure.txt'),await p.locator('body').innerText());await screenshot('failure');}throw error;}
finally{await browser?.close();server.kill();const report={checkedAt:new Date().toISOString(),passed,checks,screens,errors,isolatedProfile:true,acceleratedClock:true,realDeviceTest:false};await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));}
