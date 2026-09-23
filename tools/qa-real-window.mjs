// Work L: one real, unaccelerated 2-hour business window with the page closed in
// between (the "background" return). No clock override: Date.now is the device
// clock. Evidence is recorded separately from the accelerated QA fixtures.
import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import {resolve,join} from 'node:path';
import {fileURLToPath} from 'node:url';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {freshState} from '../web/engine.js';
import {syncProgress} from '../web/progression.js';
const root=fileURLToPath(new URL('../',import.meta.url)),require=createRequire(import.meta.url);
const candidates=[process.env.CHICK_PLAYWRIGHT_PACKAGE,'playwright','playwright-core',process.env.APPDATA&&join(process.env.APPDATA,'npm/node_modules/gsd-pi/node_modules/playwright-core')].filter(Boolean);
let packagePath;for(const p of candidates){try{packagePath=require.resolve(p);break;}catch{}}
if(!packagePath)throw Error('An existing Playwright package is required.');
const {chromium}=require(packagePath),output=resolve(root,'artifacts/qa/real-window');await mkdir(output,{recursive:true});
const WINDOW=2*3600000,sleep=ms=>new Promise(r=>setTimeout(r,ms));
const seed=freshState(Date.now(),8080);seed.total={'0:0':300,'0:3':6,'0:8':6,'0:1':1,'0:2':1,'0:10':1,'0:17':1,'0:20':1,'0:5':1};seed.farm={'0:0':30,'0:3':12};seed.cp=1000;
seed.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};seed.progress.tutorialSeen=true;syncProgress(seed);
const checks=[],errors=[],timeline=[];let browser,passed=false;
const server=spawn(process.execPath,['server.mjs','0'],{cwd:root,stdio:['ignore','pipe','pipe'],windowsHide:true});
const mark=text=>{timeline.push({at:new Date().toISOString(),text});console.log(new Date().toISOString(),text);};
try{
  const base=await new Promise((done,fail)=>{const timer=setTimeout(()=>fail(Error('server start timeout')),10000);server.once('error',fail);server.stdout.on('data',chunk=>{const m=String(chunk).match(/http:\/\/127\.0\.0\.1:\d+/);if(m){clearTimeout(timer);done(m[0]);}});});
  browser=await chromium.launch({headless:true,...(process.env.CHICK_QA_CHROME?{executablePath:process.env.CHICK_QA_CHROME}:{})});
  const context=await browser.newContext({viewport:{width:390,height:844},reducedMotion:'reduce'});
  await context.addInitScript(({seed})=>{if(location.protocol==='about:')return;if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));},{seed});
  const visit=async()=>{const p=await context.newPage();p.on('pageerror',e=>errors.push(e.message));await p.goto(base+'/');await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.waitForTimeout(800);return p;};
  const read=p=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
  let p=await visit();
  await p.getByRole('button',{name:'生意',exact:true}).click();await p.locator('[data-trade-view="business"]').first().click();
  if(await p.locator('[data-business-filter="all"]').count())await p.locator('[data-business-filter="all"]').click();
  for(const key of ['0:0','0:3']){await p.locator(`[data-business-quantity="${key}"]`).fill('6');await p.locator(`[data-business-quantity="${key}"]`).press('Tab');}
  await p.locator('[data-business-open]').click();await p.locator('[data-yes]').click();
  let s=await read(p);const startAt=s.expansion.business.active.startAt,cp0=s.cp;mark(`opened MN1 with 12 at ${new Date(startAt).toISOString()}`);
  await p.screenshot({path:resolve(output,'opened.png')});await p.close();mark('page closed (app left in background)');
  await sleep(Math.max(0,startAt+WINDOW/2-Date.now()));
  p=await visit();s=await read(p);assert.equal(s.expansion.business.active.totalSold,0,'nothing sells before the first real 2h window');assert.equal(s.cp,cp0);
  mark('1h return: no sale yet, CP unchanged');await p.screenshot({path:resolve(output,'one-hour.png')});await p.close();
  await sleep(Math.max(0,startAt+WINDOW+3*60000-Date.now()));
  p=await visit();s=await read(p);const a=s.expansion.business.active??null,report=s.expansion.business.lastReport;
  const sold=a?a.totalSold:report.totalSold,windows=a?a.windowReports.length:report.windowReports.length;
  assert.equal(windows,1,'exactly one real window settled');assert.equal(sold,6,'the first window sold six');
  const income=(a?a.windowReports[0]:report.windowReports[0]);assert.equal(s.cp,cp0+income.baseCP+income.markupCP+income.themeCP,'CP rose by exactly that window');
  await p.getByRole('button',{name:'生意',exact:true}).click();await p.locator('[data-trade-view="business"]').first().click();await p.waitForTimeout(500);await p.screenshot({path:resolve(output,'after-first-window.png')});
  mark(`2h03m return: window 1 settled once (${sold} sold, +${income.baseCP+income.markupCP+income.themeCP} CP)`);
  await p.reload();await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.waitForTimeout(800);const again=await read(p);assert.equal(again.cp,s.cp,'reopening does not settle twice');
  checks.push(`真实时钟（未加速）：开张后离开页面；1小时回访未成交、CP不变；2小时3分回访第1窗恰好卖出6只并一次入账（+${income.baseCP+income.markupCP+income.themeCP}CP），刷新不重复结算`);
  assert.deepEqual(errors,[]);passed=true;await context.close();
}finally{await browser?.close();server.kill();const report={checkedAt:new Date().toISOString(),passed,checks,timeline,errors,acceleratedClock:false,realDeviceTest:false,note:'Desktop Chromium headless with the real device clock; not an Android device.'};await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));}
