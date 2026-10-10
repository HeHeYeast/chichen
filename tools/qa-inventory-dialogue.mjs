// Isolated browser storage: exercise the shipped app, never a player's save.
import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import {join} from 'node:path';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {freshState,normalizeSave} from '../web/engine.js';
const require=createRequire(import.meta.url);
const {chromium}=require(process.env.CHICK_PLAYWRIGHT_PACKAGE??join(process.env.APPDATA,'npm/node_modules/gsd-pi/node_modules/playwright-core'));
const now=Date.now(),s=freshState(now);
s.total={'0:0':116,'0:3':1,'0:4':1,'0:8':1,'0:18':1};
s.farm={'0:0':3,'0:3':2,'0:4':2,'0:8':1,'0:18':1};s.toolLevels[1]=0;
s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};
s.cp=100000;s.ingredients={0:100000,1:123};
const state=normalizeSave(s,now),out='artifacts/qa/inventory-dialogue';
await mkdir(out,{recursive:true});
const server=spawn(process.execPath,['server.mjs','0'],{stdio:['ignore','pipe','pipe'],windowsHide:true});
let browser,page;const errors=[],checks=[];
try{
 const base=await new Promise((resolve,reject)=>{const timer=setTimeout(()=>reject(Error('server timeout')),10000);server.once('error',reject);server.stdout.on('data',chunk=>{const m=String(chunk).match(/http:\/\/127\.0\.0\.1:\d+/);if(m){clearTimeout(timer);resolve(m[0]);}});});
 browser=await chromium.launch({headless:true,executablePath:process.env.CHICK_QA_CHROME??'C:/Program Files/Google/Chrome/Application/chrome.exe'});
 const context=await browser.newContext({viewport:{width:390,height:844},reducedMotion:'reduce'});
 await context.addInitScript(state=>{if(!localStorage.getItem('chick-kitchen-review-v1'))localStorage.setItem('chick-kitchen-review-v1',JSON.stringify(state));},state);
 page=await context.newPage();page.on('pageerror',e=>errors.push(e.message));
 await page.goto(base+'/web/index.html?review');
 await page.getByRole('button',{name:'开始游戏',exact:true}).click();
 await page.locator('[data-control-id="nav:0"][tabindex="0"]').waitFor();
 const nav=name=>page.locator('#main-nav').getByRole('button',{name,exact:true});
 await nav('生意').click();await page.locator('[data-loop-action="visitor"]').click();
 for(let i=0;i<3;i++)await page.locator('[data-intro-next]').click();
 await page.locator('.regional-screen').waitFor();
 assert.equal(await page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-review-v1')).events.loopVisitorMet),true);
 await nav('生意').click();assert.equal(await page.locator('[data-intro-next]').count(),0);
 await page.locator('[data-loop-action="journey"]').click();
 await page.locator('[data-regional-go]').click();await page.locator('[data-journey-confirm]').click();
 await page.locator('.jr-travel').waitFor();
 for(let i=0;i<3;i++){await nav('生意').click();assert.equal(await page.locator('[data-intro-next]').count(),0);await nav('寻访').click();await page.locator('.jr-travel').waitFor();}
 await nav('生意').click();await page.screenshot({path:join(out,'business-after-trip.png')});
 checks.push('Finished intro, returned before departure, started trip, then switched journey/business three times without replay');
 await page.reload();await page.getByRole('button',{name:'开始游戏',exact:true}).click();await nav('生意').click();assert.equal(await page.locator('[data-intro-next]').count(),0);
 checks.push('Reload preserves completed dialogue and active trip');
 await nav('厨房').click();
 // Use the app's visible warehouse/shop buttons from the kitchen.
 await page.getByRole('button',{name:'仓库',exact:true}).click();
 await page.locator('[data-wh-tab="mats"]').click();
 assert.equal(await page.locator('[data-wh-mat]').count(),2);
 assert.match(await page.locator('.wh-bag').innerText(),/100,123/);
 assert.equal(await page.locator('[data-wh-mat="0"] .wh-count').innerText(),'×10万');
 assert.doesNotMatch(await page.locator('.warehouse-screen').innerText(),/Infinity|\/30|\/36|\/42/);
 await page.screenshot({path:join(out,'warehouse-stacked.png')});
 await page.locator('[data-wh-shop]').click();await page.locator('[data-shop-tab="1"]').click();
 await page.locator('[data-shop-ingredient-details="0"]').click();
 await page.locator('[data-shop-set="10"]').click();await page.locator('[data-shop-buy-ingredient="0"]').click();await page.locator('[data-yes]').click();
 const loaded=await page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-review-v1')));
 assert.equal(loaded.ingredients[0],100010);assert.equal(loaded.cp,99950);
 checks.push('100,123 materials render as two stacks; shop buys 10 more above all former caps');
 assert.deepEqual(errors,[]);
 await writeFile(join(out,'report.json'),JSON.stringify({passed:true,checks,errors},null,2));console.log(JSON.stringify({passed:true,checks,errors},null,2));
}catch(error){if(page)await page.screenshot({path:join(out,'failure.png')}).catch(()=>{});throw error;}
finally{await browser?.close();server.kill();}
