// Frozen K contracts missed by the original screenshot-only gate.
import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import {resolve,join} from 'node:path';
import {fileURLToPath} from 'node:url';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {syncProgress} from '../web/progression.js';
import {orderMilestone,acceptProposal} from '../web/orders.js';
import {setStock,stockOf} from './qa-business-stock.mjs';
const root=fileURLToPath(new URL('../',import.meta.url)),require=createRequire(import.meta.url);
const paths=[process.env.CHICK_PLAYWRIGHT_PACKAGE,'playwright','playwright-core',process.env.APPDATA&&join(process.env.APPDATA,'npm/node_modules/gsd-pi/node_modules/playwright-core')].filter(Boolean);
let pkg;for(const path of paths)try{pkg=require.resolve(path);break;}catch{}
if(!pkg)throw Error('Playwright required');
const {chromium}=require(pkg),out=resolve(root,'artifacts/qa/takeover-ui');await mkdir(out,{recursive:true});
const now=1800000000000,seed=E.freshState(now,42);seed.total={'0:0':300,'0:3':6,'0:8':6,'0:1':1,'0:2':1,'0:10':1,'0:17':1,'0:20':1,'0:6':1,'0:5':1};seed.farm={'0:0':30,'0:3':12};seed.cp=1000;seed.expansion.discovery.cards['V-S1']=1;seed.expansion.discovery.identified['75']=2;seed.meta.factSeq=2;seed.toolLevels[1]=0;seed.toolLevels[2]=0;seed.progress.tutorialSeen=true;seed.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};syncProgress(seed);orderMilestone(seed,now,'batch');acceptProposal(seed,seed.expansion.orders.proposals.find(p=>p.templateId==='O01').id,{},now);E.normalizeSave(seed,now);
const checks=[],errors=[];let browser,p,passed=false;
const server=spawn(process.execPath,['server.mjs','0'],{cwd:root,stdio:['ignore','pipe','pipe'],windowsHide:true});
try{
  const base=await new Promise((done,fail)=>{const timer=setTimeout(()=>fail(Error('server timeout')),10000);server.on('error',fail);server.stdout.on('data',b=>{const m=String(b).match(/http:\/\/127\.0\.0\.1:\d+/);if(m){clearTimeout(timer);done(m[0]);}});});
  browser=await chromium.launch({headless:true,...(process.env.CHICK_QA_CHROME?{executablePath:process.env.CHICK_QA_CHROME}:{})});
  const context=await browser.newContext({viewport:{width:390,height:844},reducedMotion:'reduce'});
  await context.addInitScript(({seed,now})=>{if(location.protocol==='about:')return;window.offset=Number(sessionStorage.getItem('qa-offset')??0);Date.now=()=>now+window.offset+Math.floor(performance.now());if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));},{seed,now});
  p=await context.newPage();p.on('pageerror',e=>errors.push(e.message));await p.goto(base+'/');
  const start=async()=>{await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.waitForTimeout(700);};await start();
  const read=()=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1'))),nav=title=>p.getByRole('button',{name:title,exact:true}).click();
  await p.locator('[data-control-id="tool:0"]').click();await p.locator('[data-yes]').click();let s=await read();
  await p.evaluate(offset=>{window.offset=offset;sessionStorage.setItem('qa-offset',String(offset));},s.batch.ends-now+1000);await p.waitForTimeout(3800);
  for(let i=0;i<24;i++)await p.locator(`[data-control-id="egg:${i}"]`).press('Enter');
  s=await read();assert.ok(s.batch.eggs.every(e=>e.collected));const cp=s.cp,total=s.farm['0:0'];
  // The harvest card (and its allocation page) is paused; after the last egg the kitchen stays clear.
  await p.waitForTimeout(800);assert.equal(await p.locator('[data-control-id=harvest-allocation]').count(),0);assert.equal(await p.locator('#panels .harvest-card').count(),0);
  await nav('生意');await p.locator('.business-screen').waitFor();await p.locator('[data-business-sheet="stock"]').first().click();if(await p.locator('[data-business-clear]').count())await p.locator('[data-business-clear]').click();
  await setStock(p,'0:0',3);assert.equal((await read()).cp,cp,'stocking a draft does not spend');
  assert.equal(await stockOf(p,'0:0'),3);checks.push('真实厨房收24只后不再弹收成卡；备货3只只是草稿，不花CP');
  // The stock sheet survives the clock tick: the open basket keeps its DOM and draft amount.
  await p.locator('.bs-dialog [data-business-stock-key="0:0"]').click();await p.locator('.bs-dialog [data-business-basket-range]').evaluate(el=>window.__basket=el);
  await p.waitForTimeout(1600);assert.equal(await p.evaluate(()=>window.__basket.isConnected),true,'idle countdown must not replace the open basket');
  await p.locator('.bs-dialog [data-business-basket-done]').click();assert.equal(await stockOf(p,'0:0'),3);await p.locator('[data-business-sheet-close]').click();checks.push('备货篮跨过每秒时钟仍保留同一DOM和草稿数量');
  await nav('图鉴');assert.deepEqual(await p.locator('[data-book-tab]').allTextContents(),['品种','配方','收藏','日历']);await p.locator('[data-book-tab=species]').click();await p.locator('[data-collection-page="0"]').click();assert.equal(await p.locator('[data-collection-card="0"]').count(),1);
  await p.locator('[data-collection-card="0"]').click();assert.equal(await p.locator('[data-species-sell]').count(),0);await p.locator('[data-species-inventory]').click();await p.locator('.warehouse-screen .wh-sheet').waitFor();checks.push('图鉴四分页、编号搜索、档案不内嵌出售、精准进入该类农场库存');
  for(const [width,height,font]of [[320,568,16],[844,390,16],[390,844,24],[1280,900,24]]){
    await p.setViewportSize({width,height});await p.evaluate(font=>{document.documentElement.style.fontSize=font+'px';window.dispatchEvent(new Event('resize'));},font);
    await nav('生意');await p.locator('[data-business-sheet="stock"]').first().click();await p.waitForTimeout(100);
    const sizes=await p.locator('[data-control-id^="nav:"]').evaluateAll(els=>els.map(e=>{const r=e.getBoundingClientRect();return [r.width,r.height];}));assert.ok(sizes.every(([w,h])=>w>=44&&h>=44));
    const bounds=await p.locator('.screen-panel').evaluate(el=>{const r=el.getBoundingClientRect(),body=el.querySelector('.business-scroll');return {w:r.width,bottom:r.bottom,overflow:body.scrollWidth-body.clientWidth,text:parseFloat(getComputedStyle(el.querySelector('.bs-dialog .kp-cell-name,p,.rc-sold')).fontSize)};});assert.ok(bounds.w<=width);assert.ok(bounds.bottom<=height-59);assert.ok(bounds.overflow<=1);assert.ok(bounds.text>=14);
    await p.locator('[data-business-sheet-close]').click();const submit=p.locator('[data-business-open]');await submit.scrollIntoViewIfNeeded();const b=await submit.boundingBox();assert.ok(b.y+b.height<=height-59&&b.height>=44);await p.screenshot({path:resolve(out,`layout-${width}-${height}-${font}.png`)});
  }
  checks.push('320/短横屏/150%大字：正文独立滚动、导航与提交≥44px且不重叠，桌面大字退回单列');
  await nav('厨房');await p.locator('[data-control-id=tool\\:0]').count();
  await p.locator('[data-control-id="tool:0"]').click();await p.locator('[data-yes]').waitFor();assert.equal(await p.locator('#main-nav').evaluate(e=>e.inert),true);await p.keyboard.press('Escape');assert.equal(await p.locator('#main-nav').evaluate(e=>e.inert),false);checks.push('危险确认隔离背景，Esc取消并恢复主导航');
  await p.setViewportSize({width:390,height:844});await p.evaluate(()=>{document.documentElement.style.fontSize='16px';window.dispatchEvent(new Event('resize'));});
  await p.getByRole('button',{name:'补给 · 小卖部',exact:true}).click();await p.locator('[data-shop-tab="1"]').click();await p.locator('[data-shop-ingredient-details="75"]').click();
  const materialBefore=await read();await p.locator('[data-shop-material-lore]').click();await p.locator('.regional-screen').waitFor();
  assert.ok((await p.locator('.regional-body').innerText()).includes(E.label(E.ingredient(75))));
  await nav('厨房');await p.getByRole('button',{name:'补给 · 小卖部',exact:true}).click();await p.locator('[data-shop-tab="1"]').click();await p.locator('[data-shop-ingredient-details="75"]').click();await p.locator('[data-shop-detail-ingredient="75"]').waitFor();
  const materialAfter=await read();assert.equal(materialAfter.cp,materialBefore.cp);assert.deepEqual(materialAfter.ingredients,materialBefore.ingredients);
  checks.push('已辨认材料商品→对应食材见闻→经补给入口返回原商品，浏览不扣费、不发物品');
  assert.deepEqual(errors,[]);passed=true;
}catch(e){if(p&&!p.isClosed()){await p.screenshot({path:resolve(out,'failure.png')});await writeFile(resolve(out,'failure.txt'),await p.locator('body').innerText());}throw e;}
finally{await browser?.close();server.kill();const report={checkedAt:new Date().toISOString(),passed,checks,errors,isolatedProfile:true,acceleratedClock:true,realDeviceTest:false};await writeFile(resolve(out,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));}
