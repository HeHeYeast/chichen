import assert from 'node:assert/strict';
import {createRequire} from 'node:module';
import {spawn} from 'node:child_process';
import {mkdir,writeFile} from 'node:fs/promises';
import {freshState} from '../web/engine.js';
const {chromium}=createRequire(import.meta.url)(process.env.CHICK_PLAYWRIGHT_PACKAGE??process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const out='artifacts/rc-20260930';await mkdir(out,{recursive:true});
const server=spawn(process.execPath,['server.mjs','0'],{stdio:['ignore','pipe','pipe'],windowsHide:true});
const base=await new Promise((resolve,reject)=>{server.on('error',reject);server.stdout.on('data',b=>{const m=String(b).match(/http:\/\/127\.0\.0\.1:\d+/);if(m)resolve(m[0]);});});
const browser=await chromium.launch({headless:true});const report={passed:false,checks:[],errors:[]};let p;
const read=()=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
const nav=name=>p.locator('#main-nav').getByRole('button',{name,exact:true}).click();
const ctl=id=>p.locator(`[data-control-id="${id}"]`).press('Enter');
async function open(seed=null){
  const c=await browser.newContext({viewport:{width:320,height:568},reducedMotion:'reduce'});
  await c.addInitScript(s=>{window.releaseTime=Date.now();Date.now=()=>window.releaseTime;if(s&&!sessionStorage.seeded){localStorage.setItem('chick-kitchen-v1',JSON.stringify(s));sessionStorage.seeded='1';}},seed);
  p=await c.newPage();p.on('pageerror',e=>report.errors.push(e.message));p.on('response',r=>{if(r.status()>=400)report.errors.push(r.status()+' '+r.url());});
  await p.goto(base);await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.waitForTimeout(650);return c;
}
async function harvest(){
  const s=await read();await p.evaluate(at=>window.releaseTime=at,s.batch.ends+1);await p.waitForTimeout(250);
  await p.evaluate(()=>window.releaseTime+=2100);await p.waitForTimeout(250);await p.evaluate(()=>window.releaseTime+=1000);await p.waitForTimeout(250);
  for(let i=0;i<24;i++)await ctl('egg:'+i);
  const after=await read();assert.equal(after.batch.eggs.filter(e=>e.collected).length,24);assert.equal(after.cp,s.cp+24);return after;
}
try{
  let c=await open();assert.equal((await read()).cp,600);await ctl('tool:0');await p.locator('[data-yes]').click();let s=await harvest();assert.equal(s.total['0:0'],24);await p.waitForFunction(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')).progress.tutorialSeen===true);
  await p.screenshot({path:out+'/fresh-first-harvest.png'});
  for(const name of ['农场','生意','寻访','图鉴','厨房']){await nav(name);assert.equal(await p.locator('#main-nav button').evaluateAll(bs=>bs.filter(b=>b.querySelector('svg,img')).length),5);}
  await ctl('supply');await p.locator('.shop-screen').waitFor();await p.keyboard.press('Escape');await ctl('workshop');await p.locator('.workshop-screen').waitFor();await p.keyboard.press('Escape');
  await p.getByRole('button',{name:'设置',exact:true}).click();await p.locator('.settings-screen').waitFor();await p.locator('[data-return]').click();
  s=await read();await p.reload();await p.getByRole('button',{name:'开始游戏',exact:true}).click();assert.equal((await read()).cp,s.cp);assert.deepEqual((await read()).farm,s.farm);await c.close();report.checks.push('true fresh save: first 24 eggs, collection, tutorial, all five pages, shop, skills, settings, close/back and reload at 320px');
  const seed=freshState(Date.now(),37);seed.cp=50000;seed.kitchenLevel=1;seed.toolLevels[1]=1;seed.ingredients={1:2,2:2};seed.progress.tutorialSeen=true;
  c=await open(seed);await ctl('ingredient');await p.locator('[data-nb-lens="manual"]').click();for(const id of [1,2])await p.locator(`[data-id="${id}"]`).click();await p.locator('[data-ok]').click();assert.deepEqual((await read()).selected,[1,2]);
  await ctl('tool:1');assert.equal(await p.locator('[data-preview-species="0:6"]').count(),1);assert.equal(await p.locator('[data-preview-species="0:7"]').count(),1);await p.screenshot({path:out+'/two-condiments-candidates.png'});await p.locator('[data-yes]').click();s=await harvest();assert.ok(s.farm['0:6']>0&&s.farm['0:7']>0);assert.deepEqual(s.ingredients,{1:1,2:1});await c.close();report.checks.push('real UI selects two condiments, previews both targets, hatches and collects both into inventory');
  c=await browser.newContext();const raw=JSON.stringify(seed);await c.addInitScript(raw=>{localStorage.setItem('chick-kitchen-v1',raw);window.structuredClone=undefined;},raw);p=await c.newPage();await p.goto(base);await p.getByRole('heading',{name:'请更新系统网页组件'}).waitFor();assert.equal(await p.evaluate(()=>localStorage.getItem('chick-kitchen-v1')),raw);assert.equal(await p.locator('#main-nav button').count(),0);await p.screenshot({path:out+'/old-webview-guidance.png'});await c.close();report.checks.push('unsupported runtime shows update guidance without loading game or changing existing save');
  c=await browser.newContext();await c.addInitScript(raw=>localStorage.setItem('chick-kitchen-v1',raw),raw);p=await c.newPage();await p.route('**/web/app.js',route=>route.fulfill({contentType:'text/javascript',body:'throw new Error("QA module unavailable");'}));await p.goto(base);await p.getByRole('heading',{name:'游戏暂时无法打开'}).waitFor();assert.equal(await p.evaluate(()=>localStorage.getItem('chick-kitchen-v1')),raw);await c.close();report.checks.push('failed module loading shows recovery guidance and preserves raw save');
  assert.deepEqual(report.errors,[]);report.passed=true;
}catch(error){if(p&&!p.isClosed()){await p.screenshot({path:out+'/release-flow-failure.png'});await writeFile(out+'/release-flow-failure.txt',await p.locator('body').innerText());}throw error;}
finally{await browser.close();server.kill();await writeFile(out+'/release-flow.json',JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));}
