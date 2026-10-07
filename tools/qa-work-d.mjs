import {openFarmSale,closeWarehouse} from './qa-farm-navigation.mjs';
// Work D: formal entry, real controls, isolated old-save seed and accelerated clock.
import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import {resolve,join} from 'node:path';
import {fileURLToPath} from 'node:url';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {freshState,normalizeSave} from '../web/engine.js';
import {syncProgress,learnSkill} from '../web/progression.js';
import {CURRENT_SAVE_VERSION} from '../web/save-migrations.js';
import {setStock,stockOf} from './qa-business-stock.mjs';
const root=fileURLToPath(new URL('../',import.meta.url)),require=createRequire(import.meta.url),{chromium}=require(process.argv[2]??join(process.env.APPDATA,'npm/node_modules/gsd-pi/node_modules/playwright-core'));
const output=resolve(root,'artifacts/qa/work-d');await mkdir(output,{recursive:true});
const now=1800000000000,seed=freshState(now,123456);
seed.total=Object.fromEntries(Array.from({length:12},(_,id)=>[`0:${id}`,id===0?2000:1]));seed.farm={'0:0':25};seed.progress.tutorialSeen=true;
seed.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};syncProgress(seed);learnSkill(seed,'TRADE-2');learnSkill(seed,'TRADE-3');seed.progress.trade.category='家常';
seed.version=4;seed.meta.migrationHistory=[{from:3,to:4}];for(const key of ['business','facts','orders','inventoryPolicy'])delete seed.expansion[key];normalizeSave(seed,now);
const checks=[],errors=[],screens=[];let browser,p,passed=false;
const server=spawn(process.execPath,['server.mjs','0'],{cwd:root,stdio:['ignore','pipe','pipe'],windowsHide:true});
const read=()=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
const start=async()=>{await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.locator('.workshop-launch').waitFor();await p.waitForTimeout(650);};
const open=async()=>{await p.getByRole('button',{name:'生意',exact:true}).click();await p.locator('.business-screen').waitFor();};
const stockSheet=async()=>{await p.locator('[data-business-sheet="stock"]').first().click();if(await p.locator('[data-business-clear]').count())await p.locator('[data-business-clear]').click();await p.locator('.bs-dialog[open]').waitFor();};
const screenshot=async name=>{await p.screenshot({path:resolve(output,name+'.png')});screens.push(name);};
const jump=async at=>{await p.evaluate(({at,base})=>{window.__workDOffset=at-base;sessionStorage.setItem('work-d-clock',String(window.__workDOffset));},{at,base:now});};
async function fit(){await p.evaluate(()=>new Promise(done=>requestAnimationFrame(()=>requestAnimationFrame(done))));const result=await p.locator('.business-screen').evaluate(el=>{const r=el.getBoundingClientRect(),g=document.querySelector('#game').getBoundingClientRect();return {fits:r.left>=g.left-1&&r.right<=g.right+1&&r.top>=g.top-1&&r.bottom<=g.bottom+1,overflow:el.scrollWidth-el.clientWidth};});assert.ok(result.fits);assert.ok(result.overflow<2);}
async function stock24(){await stockSheet();await setStock(p,'0:0',24);if(await p.locator('.business-more:not([open])').count())await p.locator('.business-more summary').click();await p.locator('[data-business-rewards]').click();await p.locator('[data-business-sheet-close]').click();await p.locator('[data-business-open]').click();await p.locator('[data-yes]').click();assert.equal((await read()).expansion.business.active.stock['0:0'],24);}
try{
 const base=await new Promise((done,fail)=>{server.once('error',fail);server.stdout.on('data',chunk=>{const m=String(chunk).match(/http:\/\/127\.0\.0\.1:\d+/);if(m)done(m[0]);});});
 browser=await chromium.launch({headless:true,executablePath:process.env.CHICK_QA_CHROME??'C:/Program Files/Google/Chrome/Application/chrome.exe'});
 async function contextFor(source){const c=await browser.newContext({viewport:{width:390,height:844},reducedMotion:'reduce'});await c.addInitScript(({seed,now})=>{if(location.protocol==='about:')return;window.__workDOffset=Number(sessionStorage.getItem('work-d-clock')??0);Date.now=()=>now+window.__workDOffset+Math.floor(performance.now());if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));},{seed:source,now});p=await c.newPage();p.on('pageerror',e=>errors.push(e.message));await p.goto(base+'/');await start();return c;}
 const locked=structuredClone(seed);locked.progress.orders={};const lockedContext=await contextFor(locked);await open();await stockSheet();assert.match(await p.locator('.business-screen').innerText(),/第一笔|首笔|首单/);assert.equal(await p.locator('[data-business-stock-key]').count(),0);await screenshot('legacy-gate');await lockedContext.close();checks.push('旧档门槛未完成采购时不允许开张');
 const context=await contextFor(seed);assert.equal((await read()).version,CURRENT_SAVE_VERSION);await open();await fit();await screenshot('prepare-390');await stock24();let s=await read();assert.equal(s.farm['0:0'],25);assert.equal(s.progress.trade.credits,1);assert.equal(s.expansion.business.active.creditReserve,1);const began=s.expansion.business.active.startAt,startCP=s.cp;checks.push('旧4→5与正式备货24留1/额度reserve不减T或CP');
 await p.getByRole('button',{name:'农场',exact:true}).click();await openFarmSale(p,'0:0');assert.equal(await p.locator('.wh-sheet [data-wh-range]').getAttribute('max'),'1');assert.equal(await p.locator('.wh-sheet [data-wh-set]').last().getAttribute('data-wh-set'),'1');await closeWarehouse(p);await p.getByRole('button',{name:'厨房',exact:true}).click();await open();checks.push('即时出售只能选择自由1只，营业S仍为24');
 await jump(began+2*3600000+1000);await p.waitForFunction(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')).expansion.business.active?.totalSold===6);await screenshot('first-window');const window=structuredClone((await read()).expansion.business.active.windowReports);await p.reload();await start();await open();assert.deepEqual((await read()).expansion.business.active.windowReports,window);checks.push('2h实售6只，保存刷新不重复窗口');
 await jump(began+8*3600000+1000);await p.waitForFunction(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')).expansion.business.lastReport?.totalSold===24);await p.locator('.business-receipt-head').waitFor();s=await read();// business rules 2 (2026-10-07): 鸡宝 alone is no complete 家常小铺, so no menu bonus (the old 95 CP had +3)
assert.deepEqual([s.expansion.business.lastReport.baseCP,s.expansion.business.lastReport.markupCP,s.expansion.business.lastReport.themeCP,s.expansion.business.lastReport.bonusCP,s.expansion.business.lastReport.income],[72,8,0,12,92]);assert.equal(s.cp,startCP+92);assert.equal(s.progress.trade.credits,0);await screenshot('receipt-92cp');
 for(const [width,height]of [[320,568],[430,932],[1280,900]]){await p.setViewportSize({width,height});await fit();await screenshot(`receipt-${width}`);}await p.locator('[data-business-again]').click();await p.locator('[data-business-tab="report"]').click();assert.equal((await read()).cp,startCP+92);checks.push('售罄92CP精确账目（规则2：只有鸡宝不算凑齐菜单，没有菜单加成），320/430/1280适配，生意页「上单」反复查看不发奖');await context.close();
 const second=await contextFor(seed);await open();await stock24();s=await read();await jump(s.expansion.business.active.startAt+6*3600000+1000);await p.waitForFunction(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')).expansion.business.active?.totalSold===18);await p.locator('#panels .bh-today .gd-btn[data-business-sheet="ledger"]').click();await p.locator('[data-business-close]').click();await p.locator('[data-yes]').click();s=await read();assert.equal(s.expansion.business.lastReport.totalSold,18);assert.equal(s.expansion.business.lastReport.remainingStock['0:0'],6);assert.equal(s.expansion.business.lastReport.bonusCP,0);assert.equal(s.expansion.business.lastReport.creditsReleased,1);assert.equal(s.progress.trade.credits,1);assert.equal(s.expansion.business.active,null);await screenshot('early18-release6');checks.push('独立第二单18只提前收摊，余6解除S，未用额度释放');await second.close();
 const offline=await contextFor(seed);await open();await stock24();s=await read();const offlineStart=s.expansion.business.active.startAt,offlineCP=s.cp;
 // Leave the page entirely while time passes: no app code runs until the player returns.
 await jump(offlineStart+26*3600000);await p.goto('about:blank');await p.waitForTimeout(300);await p.goto(base+'/');await start();
 s=await read();assert.equal(s.expansion.business.active,null);assert.equal(s.expansion.business.lastReport.totalSold,24);assert.equal(s.expansion.business.lastReport.income,92);assert.equal(s.cp,offlineCP+92);
 await open();await p.locator('.business-receipt-head').waitFor();assert.equal((await read()).cp,offlineCP+92);await screenshot('offline-return-receipt');
 checks.push('离开页面26小时后重开：resume按同一时间线结清一次（24只/92CP），账单只读、重复查看不加钱');await offline.close();
 const respec=await contextFor(seed);await open();await stock24();s=await read();await jump(s.expansion.business.active.startAt+4*3600000+1000);await p.waitForFunction(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')).expansion.business.active?.totalSold===12);
 await p.getByRole('button',{name:'厨房',exact:true}).click();await p.locator('.workshop-launch').click();await p.locator('.workshop-screen').waitFor();
 const summary=p.locator('details:has([data-respec-close-business]) summary');if(await summary.count())await summary.first().click();
 await p.locator('[data-respec-close-business]').click();await p.locator('[data-yes]').click();s=await read();
 assert.equal(s.expansion.business.active,null);assert.equal(s.expansion.business.lastReport.reason,'manual');assert.equal(s.expansion.business.lastReport.totalSold,12);assert.equal(s.expansion.business.lastReport.remainingStock['0:0'],12);
 await screenshot('respec-after-close');checks.push('手艺页“结算并收摊后重配”：先结清已过的2个窗口（12只），释放余货后进入重配草稿');await respec.close();
 assert.deepEqual(errors,[]);passed=true;
}catch(error){if(p&&!p.isClosed()){await writeFile(resolve(output,'failure.txt'),await p.locator('body').innerText());await screenshot('failure');}throw error;}
finally{await browser?.close();server.kill();const report={checkedAt:new Date().toISOString(),passed,checks,screens,errors,isolatedProfile:true,acceleratedClock:true,realDeviceTest:false};await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));}

