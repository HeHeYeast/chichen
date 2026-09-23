// Real Work B interface actions. Only boot fixture/time are controlled by QA.
import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import {resolve,join} from 'node:path';
import {fileURLToPath} from 'node:url';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {freshState} from '../web/engine.js';
import {earnedSources} from '../web/progression.js';
const root=fileURLToPath(new URL('../',import.meta.url)),require=createRequire(import.meta.url);
const candidates=[process.argv[2],process.env.CHICK_PLAYWRIGHT_PACKAGE,'playwright','playwright-core',process.env.APPDATA&&join(process.env.APPDATA,'npm/node_modules/gsd-pi/node_modules/playwright-core')].filter(Boolean);
let packagePath;for(const p of candidates){try{packagePath=require.resolve(p);break;}catch{}}
if(!packagePath)throw Error('An existing Playwright package is required.');
const {chromium}=require(packagePath),output=resolve(root,'artifacts/qa/work-b');await mkdir(output,{recursive:true});
const now=1800000000000,seed=freshState(now,123456);
seed.total={'0:0':116,'0:3':1,'0:4':1,'0:8':1,'0:18':1};seed.farm={'0:0':3,'0:3':2,'0:4':2,'0:8':1,'0:18':1};seed.toolLevels[1]=0;seed.ingredients={0:30};seed.progress.sources=earnedSources(seed);seed.progress.tutorialSeen=true;
const checks=[],errors=[],screens=[];let browser,p,passed=false;
const server=spawn(process.execPath,['server.mjs','0'],{cwd:root,stdio:['ignore','pipe','pipe'],windowsHide:true});
const read=()=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
const screenshot=async name=>{await p.locator('.regional-screen img').evaluateAll(async images=>{await Promise.all(images.map(image=>image.decode()));});await p.screenshot({path:resolve(output,name+'.png')});screens.push(name);};
const start=async()=>{await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.locator('.workshop-launch').waitFor();await p.waitForTimeout(650);};
const open=async()=>{await p.getByRole('button',{name:'寻访',exact:true}).click();await p.locator('.regional-screen').waitFor();};
const record=()=>p.locator('[data-regional-tab="record"]').click();
const jump=async at=>{await p.evaluate(({at,base})=>{window.__workBOffset=at-base;sessionStorage.setItem('work-b-clock',String(window.__workBOffset));},{at,base:now});};
async function fit(){await p.evaluate(()=>new Promise(done=>requestAnimationFrame(()=>requestAnimationFrame(done))));const box=await p.locator('.regional-screen').evaluate(el=>{const r=el.getBoundingClientRect(),g=document.querySelector('#game').getBoundingClientRect(),b=el.querySelector('.regional-body');return {inside:r.top>=g.top-1&&r.bottom<=g.bottom+1&&r.left>=g.left-1&&r.right<=g.right+1,overflow:b.scrollWidth-b.clientWidth};});assert.ok(box.inside,'regional sheet fits game');assert.ok(box.overflow<2,'regional paper has no horizontal overflow');}
async function depart(){await p.locator('[data-regional-depart]').click();await p.locator('[data-yes]').click();assert.equal((await read()).progress.trip.status,'running');}
async function finish(){const s=await read();await jump(s.progress.trip.endAt+1000);await p.locator('[data-regional-claim-notes]').waitFor();}
async function claim(){if(await p.locator('[data-regional-claim]').count()){await p.locator('[data-regional-claim]').click();await p.locator('[data-yes]').click();}if((await read()).progress.trip.status==='returned'){await p.locator('.regional-basket details summary').click();await p.locator('[data-regional-discard]').click();await p.locator('[data-yes]').click();}assert.equal((await read()).progress.trip.status,'settled');}
try{
  const base=await new Promise((done,fail)=>{const timer=setTimeout(()=>fail(Error('server start timeout')),10000);server.once('error',fail);server.stdout.on('data',chunk=>{const m=String(chunk).match(/http:\/\/127\.0\.0\.1:\d+/);if(m){clearTimeout(timer);done(m[0]);}});});
  browser=await chromium.launch({headless:true,...(process.env.CHICK_QA_CHROME?{executablePath:process.env.CHICK_QA_CHROME}:{})});
  const context=await browser.newContext({viewport:{width:390,height:844},reducedMotion:'reduce'});
  await context.addInitScript(({seed,now})=>{window.__workBOffset=Number(sessionStorage.getItem('work-b-clock')??0);Date.now=()=>now+window.__workBOffset+Math.floor(performance.now());if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));},{seed,now});
  p=await context.newPage();p.on('pageerror',error=>errors.push(error.message));await p.goto(base+'/');await start();await open();
  await fit();await screenshot('departure-390');await record();assert.ok(!(await p.locator('.regional-screen').innerHTML()).includes('荠菜煎饼鸡'));assert.match(await p.locator('.regional-screen').innerText(),/C129/);await screenshot('unknown-record');
  for(const [width,height] of [[320,568],[430,932],[1280,900]]){await p.setViewportSize({width,height});await fit();await screenshot(`record-${width}`);}
  await p.setViewportSize({width:390,height:844});await p.locator('[data-regional-tab="trip"]').click();
  await p.locator('[data-regional-slot="0"]').click();await p.locator('[data-regional-member="0:0"]').click();
  await p.locator('[data-regional-place="V:1"]').click();await p.locator('[data-regional-focus="materials"]').click();
  assert.match(await p.locator('.regional-screen').innerText(),/这趟带回荠菜标本/);await depart();
  const firstTrip=(await read()).progress.trip;assert.equal(firstTrip.regional.intro.materialId,75);assert.equal(firstTrip.remaining[0],75);assert.ok(firstTrip.remaining.length<=2);
  await finish();assert.ok(Object.hasOwn((await read()).expansion.discovery.cards,'V-S1'));assert.equal((await read()).ingredients[75],undefined);await screenshot('returned-full-bag');
  checks.push('正式谷地页面120/5资格、旧队员选择、切地点/关注仍首标本，满包自动记卡且试做料留篮');
  await record();const beforeIdentify=await read();await p.locator('[data-regional-identify]').click();const identified=await read();assert.equal(identified.cp,beforeIdentify.cp);assert.deepEqual(identified.ingredients,beforeIdentify.ingredients);assert.ok(Object.hasOwn(identified.expansion.discovery.identified,'75'));assert.equal(await p.locator('[data-regional-identify]').count(),0);await screenshot('identified-method-direction');
  await p.locator('[data-regional-tab="trip"]').click();await claim();assert.equal((await read()).ingredients[75],1);
  checks.push('免费辨认不增实体材料、容量30→36，首次篮中75领取一次');
  for(let i=0;i<3;i++){await depart();await finish();await claim();const s=await read();assert.equal(s.expansion.methods.full.includes('REC-V-C1'),i===2);if(i<2)assert.equal(s.expansion.methods.freeProgress.V.count,i+1);}
  await record();await screenshot('full-method-free');assert.match(await p.locator('.regional-screen').innerText(),/完整方法已记入册页/);assert.ok(!(await p.locator('.regional-screen').innerHTML()).includes('荠菜煎饼鸡'));
  checks.push('3趟真实完整出发/归队操作免费补全方法，未实收仍显示C129未知剪影');
  await p.locator('[data-regional-prepare]').click();assert.deepEqual((await read()).selected,[75]);assert.equal((await read()).expansion.prepareMode.recipeId,'REC-V-C1');
  await p.locator('[data-control-id="tool:1"]').click();await p.locator('[data-yes]').click();
  let s=await read();assert.equal(s.batch.rules.version,3);assert.equal(s.batch.plan.recipeId,'REC-V-C1');const frozen=structuredClone(s.batch.plan);await screenshot('regional-first-batch');
  await p.reload();await start();assert.deepEqual((await read()).batch.plan,frozen);
  s=await read();await jump(s.batch.ends+1000);await p.waitForTimeout(3300);
  for(let i=0;i<24;i++)await p.locator(`[data-control-id="egg:${i}"]`).press('Enter');
  s=await read();assert.equal(s.batch.eggs.filter(e=>e.collected).length,24);assert.equal(s.batch.plan.finished,true);assert.equal(s.expansion.regions.materialUse[75],true);
  checks.push('正式准备与平底锅开火产生Batch3，刷新冻结票据保留，24只实收结锅并记录地区材料用途');
  await open();await record();await fit();await screenshot('after-first-trial');
  async function buyTrialMaterial(){await p.locator('.regional-screen .close').click();await p.getByRole('button',{name:'厨房',exact:true}).click();await p.getByRole('button',{name:'补给 · 小卖部',exact:true}).click();await p.locator('[data-shop-tab="1"]').click();await p.locator('[data-shop-buy-ingredient="75"]').click();await p.locator('[data-yes]').click();assert.equal((await read()).ingredients[75],1);await p.getByRole('button',{name:'厨房',exact:true}).click();await open();await record();}
  let trials=1;
  while(!(await read()).total['0:128']&&trials<4){
    await buyTrialMaterial();await p.locator('[data-regional-prepare]').click();await p.locator('[data-control-id="tool:1"]').click();await p.locator('[data-yes]').click();s=await read();
    await jump(s.batch.ends+1000);await p.waitForTimeout(3300);for(let i=0;i<24;i++)await p.locator(`[data-control-id="egg:${i}"]`).press('Enter');
    trials++;await open();await record();
  }
  s=await read();assert.ok(s.total['0:128']>=1,'C129 is collected within four full clean batches');assert.match(await p.locator('.regional-screen').innerText(),/荠菜煎饼鸡/);await screenshot('C129-collected');
  await p.locator('.regional-screen .close').click();await p.getByRole('button',{name:'农场',exact:true}).click();await p.locator('[data-control-id="farm:harvest"]').click();await p.locator('[data-harvest-max="0:128"]').click();await p.locator('[data-harvest-sell]').click();await p.locator('[data-yes]').click();
  assert.equal((await read()).farm['0:128'],0);assert.ok((await read()).total['0:128']>=1);await p.locator('.harvest-screen .close').click();await p.getByRole('button',{name:'厨房',exact:true}).click();await open();await record();
  await buyTrialMaterial();await p.locator('[data-regional-prepare]').click();await p.locator('[data-control-id="tool:1"]').click();await p.locator('[data-yes]').click();s=await read();assert.equal(s.batch.plan.mode,'regional-repeat');assert.equal(s.batch.plan.targetScheduled,true);assert.equal(s.batch.plan.initialIds.filter(id=>id===128).length,1);
  checks.push(`完整合法购买/试做在${trials}批内实收C129，售空仍保留图鉴与方法，再开批安排1+23复刻`);
  assert.deepEqual(errors,[]);passed=true;await context.close();
}catch(error){if(p&&!p.isClosed()){await writeFile(resolve(output,'failure.txt'),await p.locator('body').innerText());await screenshot('failure');}throw error;}
finally{await browser?.close();server.kill();const report={checkedAt:new Date().toISOString(),passed,checks,screens,errors,isolatedProfile:true,acceleratedClock:true,realDeviceTest:false};await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));}
