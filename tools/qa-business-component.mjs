// Independent Work D UI contract harness; integration entry points are checked by the parent gate.
import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import {join,resolve} from 'node:path';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
const require=createRequire(import.meta.url),{chromium}=require(join(process.env.APPDATA,'npm/node_modules/gsd-pi/node_modules/playwright-core'));
const output=resolve('artifacts/qa/work-d-component');await mkdir(output,{recursive:true});
const server=spawn(process.execPath,['server.mjs','0'],{stdio:['ignore','pipe','pipe'],windowsHide:true});let browser;const checks=[],errors=[];
try{
 const base=await new Promise((ok,bad)=>{server.once('error',bad);server.stdout.on('data',b=>{const m=String(b).match(/http:\/\/127\.0\.0\.1:\d+/);if(m)ok(m[0]);});});
 browser=await chromium.launch({headless:true,executablePath:'C:/Program Files/Google/Chrome/Application/chrome.exe'});const page=await browser.newPage({viewport:{width:390,height:844}});page.on('pageerror',e=>errors.push(e.message));
 await page.route('**/web/business-harness.html',r=>r.fulfill({contentType:'text/html',body:'<!doctype html><meta name="viewport" content="width=device-width,initial-scale=1"><link rel="stylesheet" href="/web/style.css"><link rel="stylesheet" href="/web/game-screens.css"><link rel="stylesheet" href="/web/business.css"><main id="viewport"><div id="game"><div id="panels"></div></div></main>'}));
 await page.goto(base+'/web/business-harness.html');
 await page.evaluate(async()=>{
  const [{freshState},{freshBusiness,advanceBusiness},{freshFacts},{syncProgress},{createBusinessUI}]=await Promise.all([import('/web/engine.js'),import('/web/business.js'),import('/web/facts.js'),import('/web/progression.js'),import('/web/business-ui.js')]);
  window.qaNow=1800000000000;window.qaState=freshState(qaNow);const s=qaState;s.farm={'0:0':25,'0:3':25,'0:8':25};s.total={'0:0':240,'0:3':1,'0:8':1};s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};s.expansion.business=freshBusiness();s.expansion.facts=freshFacts();s.expansion.orders={sequence:0,proposalSequence:0,proposals:[],active:[],templateProgress:{},refillCredits:0,lastProposedTemplate:null};s.expansion.inventoryPolicy={keepOne:true,collectionLocks:[],optionalOrderReservations:{}};syncProgress(s);
  const panels=document.querySelector('#panels');window.qaAdv=advanceBusiness;window.qaMessages=[];
  window.qaUI=createBusinessUI({getState:()=>qaState,getNow:()=>qaNow,panels,showPanel:(title,html,cls)=>{panels.innerHTML=`<section class="panel paper ${cls}"><button class="close">×</button><h2>${title}</h2>${html}</section>`;},commitProgress:fn=>{const draft=structuredClone(qaState);const out=fn(draft);qaState=draft;return out;},confirmBox:(text,fn)=>{qaMessages.push(text);window.qaConfirm=fn;},alertBox:text=>qaMessages.push(text),characterPortrait:()=>'<span aria-hidden="true">🐥</span>',goKitchen:()=>qaMessages.push('kitchen')});qaUI.open();
 });
 await page.locator('[data-business-quantity="0:0"]').fill('24');await page.locator('[data-business-quantity="0:0"]').press('Tab');assert.equal(await page.locator('[data-business-open]').isEnabled(),true);assert.equal(await page.locator('[data-business-quantity="0:0"]').inputValue(),'24');checks.push('quantity24/keep-one/preview');
 await page.screenshot({path:join(output,'prepare-390.png')});await page.locator('[data-business-open]').click();assert.match(await page.evaluate(()=>qaMessages.at(-1)),/备货24只/);assert.equal(await page.evaluate(()=>qaState.expansion.business.active),null);await page.evaluate(()=>qaConfirm());assert.equal(await page.evaluate(()=>qaState.farm['0:0']),25);await page.locator('[data-business-close]').waitFor();checks.push('confirmation-before-reserve/active');
 await page.evaluate(()=>{qaNow+=6*3600000;qaAdv(qaState,qaNow);qaUI.refresh();});assert.match(await page.locator('.business-totals').innerText(),/18/);await page.screenshot({path:join(output,'active-390.png')});
 await page.locator('[data-business-close]').click();await page.evaluate(()=>qaConfirm());assert.match(await page.locator('.business-receipt-head').innerText(),/卖出18只/);assert.equal(await page.evaluate(()=>qaState.expansion.business.lastReport.totalSold),18);const cp=await page.evaluate(()=>qaState.cp);await page.screenshot({path:join(output,'report-390.png')});
 await page.locator('[data-business-again]').click();await page.locator('[data-business-tab="report"]').click();assert.equal(await page.evaluate(()=>qaState.cp),cp);checks.push('18 earlyclose/report reopen no reward');
 for(const width of [320,390,768]){await page.setViewportSize({width,height:844});await page.evaluate(()=>qaUI.refresh());const overflow=await page.locator('.business-screen').evaluate(el=>el.scrollWidth-el.clientWidth);assert.ok(overflow<2,`${width}px horizontal overflow ${overflow}`);}checks.push('320/390/768 no horizontal overflow');assert.deepEqual(errors,[]);
 await writeFile(join(output,'report.json'),JSON.stringify({passed:true,checks,errors},null,2));console.log(JSON.stringify({passed:true,checks,errors}));
}finally{await browser?.close();server.kill();}
