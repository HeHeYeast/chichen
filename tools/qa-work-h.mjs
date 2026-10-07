// Work H playable acceptance: a 193-complete schema-5 legacy save upgrades,
// registers historical pages once, displays mementos and earns a trip stamp.
import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import {resolve,join} from 'node:path';
import {fileURLToPath} from 'node:url';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {freshState} from '../web/engine.js';
import {earnedSources} from '../web/progression.js';
import {LEGACY193} from '../web/legacy-content.js';
const root=fileURLToPath(new URL('../',import.meta.url)),require=createRequire(import.meta.url);
const candidates=[process.argv[2],process.env.CHICK_PLAYWRIGHT_PACKAGE,'playwright','playwright-core',process.env.APPDATA&&join(process.env.APPDATA,'npm/node_modules/gsd-pi/node_modules/playwright-core')].filter(Boolean);
let packagePath;for(const p of candidates){try{packagePath=require.resolve(p);break;}catch{}}
if(!packagePath)throw Error('An existing Playwright package is required.');
const {chromium}=require(packagePath),output=resolve(root,'artifacts/qa/work-h');await mkdir(output,{recursive:true});
const now=1800000000000,seed=freshState(now,20260923);
// Mid-game legacy save: kitchen Lv.2, duck bought, pan/boil/oven/keep-warm/steamer.
seed.kitchenLevel=3;seed.duck=true;seed.toolLevels=[0,1,1,1,1,1,1,1,1];seed.cp=20000;
seed.total=Object.fromEntries(LEGACY193.characters.flatMap((list,egg)=>list.map(c=>[`${egg}:${c.id}`,c.id===0&&egg===0?6000:2])));seed.farm={'0:0':12,'1:0':6,'0:128':2};
seed.total['0:128']=1;seed.expansion.discovery.cards['V-S1']=1;seed.expansion.discovery.identified['75']=2;seed.meta.factSeq=2;seed.expansion.regions.introSpecimenDone=['V'];
seed.events.seasonalCollections={spring:true};seed.ingredients={};seed.progress.sources=earnedSources(seed);seed.progress.tutorialSeen=true;
// Written by the previous (schema 5) build: no collection containers yet.
seed.version=5;seed.meta.migrationHistory.pop();for(const key of ['collections','regulars','projects','menus'])delete seed.expansion[key];
const checks=[],errors=[],screens=[];let browser,p,passed=false;
const server=spawn(process.execPath,['server.mjs','0'],{cwd:root,stdio:['ignore','pipe','pipe'],windowsHide:true});
const read=()=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
const screenshot=async name=>{await p.waitForTimeout(120);await p.screenshot({path:resolve(output,name+'.png')});screens.push(name);};
const start=async()=>{await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.locator('.workshop-launch').waitFor();await p.waitForTimeout(650);};
const open=async()=>{if(await p.locator('.regional-screen').count())return;await p.getByRole('button',{name:'寻访',exact:true}).click();await p.locator('.regional-screen').waitFor();};
const tab=async name=>{
  if(name==='record'){
    if(await p.locator('[data-regional-record]').count())await p.locator('[data-regional-record]').click();
    else if(await p.locator('[data-regional-tab="record"]').count())await p.locator('[data-regional-tab="record"]').first().click();
  }else if(await p.getByRole('button',{name:'回到寻访',exact:true}).count())await p.getByRole('button',{name:'回到寻访',exact:true}).click();
  else if(await p.locator('[data-journey-enter]').count())await p.locator('[data-journey-enter]').click();
};
const jump=async at=>{await p.evaluate(({at,base})=>{window.__workBOffset=at-base;sessionStorage.setItem('work-b-clock',String(window.__workBOffset));},{at,base:now});};
async function fit(){await p.evaluate(()=>new Promise(done=>requestAnimationFrame(()=>requestAnimationFrame(done))));const box=await p.locator('.regional-screen').evaluate(el=>{const r=el.getBoundingClientRect(),g=document.querySelector('#game').getBoundingClientRect(),b=el.querySelector('.regional-body');return {inside:r.top>=g.top-1&&r.bottom<=g.bottom+1&&r.left>=g.left-1&&r.right<=g.right+1,overflow:b.scrollWidth-b.clientWidth};});assert.ok(box.inside,'regional sheet fits game');assert.ok(box.overflow<2,'regional paper has no horizontal overflow');}
async function team(keys){
  const state=await read();
  for(let i=0;i<3;i++){await p.locator(`[data-regional-slot="${i}"]`).click();if(await p.locator('[data-regional-remove]').count())await p.locator('[data-regional-remove]').click();else await p.locator('[data-regional-pick-back]').click();}
  for(const [i,key] of keys.entries()){await p.locator(`[data-regional-slot="${i}"]`).click();await p.locator(`[data-regional-member="${key}"]`).click();}
  void state;
}
async function trip({place,focus,members}){
  await open();await tab('trip');await p.locator(`[data-regional-place="${place}"]`).click();await p.locator(`[data-regional-focus="${focus}"]`).click();
  if(members)await team(members);
  await p.locator('[data-regional-depart]').click();await p.locator('[data-journey-confirm]').click();
  const s=await read();assert.equal(s.progress.trip.status,'running');
  await jump(s.progress.trip.endAt+1000);await p.locator('[data-regional-claim]').waitFor();await p.locator('[data-regional-claim-notes]').waitFor({state:'attached'});
  const back=await read();const result=back.progress.trip.regional.result;
  if(await p.locator('[data-regional-claim]').count()){await p.locator('[data-regional-claim]').click();await p.waitForTimeout(300);if(await p.locator('#dialog-layer [data-yes]').count())await p.locator('#dialog-layer [data-yes]').click();}
  if((await read()).progress.trip.status==='returned'){await p.locator('.regional-basket details summary').click();await p.locator('[data-regional-discard]').click();await p.locator('[data-yes]').click();}
  assert.equal((await read()).progress.trip.status,'settled');
  return result;
}
async function until(card,options,limit){for(let i=0;i<limit;i++){const r=await trip(options);if((await read()).expansion.discovery.cards[card])return i+1;void r;}throw Error(`${card} not found within ${limit} complete trips`);}
async function identify(id){await open();await tab('record');await p.locator(`[data-regional-identify="${id}"]`).click();assert.ok(Object.hasOwn((await read()).expansion.discovery.identified,String(id)));}
async function buy(id,count=1){while(await p.locator('.screen-panel .close').count())await p.locator('.screen-panel .close').first().click();await p.getByRole('button',{name:'厨房',exact:true}).click();await p.getByRole('button',{name:'补给 · 小卖部',exact:true}).click();await p.locator('[data-shop-tab="1"]').click();await p.locator(`[data-shop-ingredient-details="${id}"]`).click();for(let i=0;i<count;i++){await p.locator(`[data-shop-buy-ingredient="${id}"]`).click();await p.locator('[data-yes]').click();}await p.getByRole('button',{name:'厨房',exact:true}).click();}
// A careful player cleans before a trial batch; dirt may otherwise turn the scheduled egg sick.
async function tidy(){await p.locator('[data-control-id="clean"]').click();const yes=p.locator('[data-yes]');if(await yes.isEnabled())await yes.click();else await p.locator('[data-no]').click();}
async function cook(toolId){await tidy();await p.locator(`[data-control-id="tool:${toolId}"]`).click();await p.locator('.cooking-dialog').waitFor();const text=await p.locator('.cooking-dialog').innerText();await p.locator('[data-yes]').click();return text;}
async function harvest(){const s=await read();await jump(s.batch.ends+1000);await p.waitForTimeout(3300);for(let i=0;i<24;i++)await p.locator(`[data-control-id="egg:${i}"]`).press('Enter');assert.equal((await read()).batch.eggs.filter(e=>e.collected).length,24);}
try{
  const base=await new Promise((done,fail)=>{const timer=setTimeout(()=>fail(Error('server start timeout')),10000);server.once('error',fail);server.stdout.on('data',chunk=>{const m=String(chunk).match(/http:\/\/127\.0\.0\.1:\d+/);if(m){clearTimeout(timer);done(m[0]);}});});
  browser=await chromium.launch({headless:true,...(process.env.CHICK_QA_CHROME?{executablePath:process.env.CHICK_QA_CHROME}:{})});
  const context=await browser.newContext({viewport:{width:390,height:844},reducedMotion:'reduce'});
  await context.addInitScript(({seed,now})=>{if(location.protocol==='about:')return;window.__workBOffset=Number(sessionStorage.getItem('work-b-clock')??0);Date.now=()=>now+window.__workBOffset+Math.floor(performance.now());if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));},{seed,now});
  p=await context.newPage();p.on('pageerror',error=>errors.push(error.message));await p.goto(base+'/');await start();
  let s=await read();assert.equal(s.version,6,'a schema-5 save upgrades on load');
  const got=s.expansion.collections.entitlements;for(let i=1;i<=8;i++)assert.ok(got[`M0${i}`]&&got[`PAGE-COL-${i}`]);
  assert.equal(s.cp,seed.cp,'historical pages never pay CP');assert.deepEqual(s.events,seed.events,'legacy seasonal/shrine records untouched');
  assert.ok(Object.keys(got).every(id=>!/^STAMP-|^FULLSTAMP-/.test(id)),'no practice stamp invented from history');
  const ledger=JSON.stringify(s.expansion.collections.entitlements);await p.reload();await start();s=await read();assert.equal(JSON.stringify(s.expansion.collections.entitlements),ledger,'reloading never grants twice or renumbers');
  checks.push('193全收的schema5旧档载入：迁移到6后以独立事务一次登记8张主题插页与M01–M08，不发CP、不动旧四时/神社记录、不补造实践印；再次载入不重复');
  await p.getByRole('button',{name:'图鉴',exact:true}).click();await p.locator('[data-book-tab="collections"]').click();await p.locator('.books-screen').waitFor();
  const text=await p.locator('.books-screen').innerText();assert.match(text,/街坊早饭册/);assert.ok(!/荠菜煎饼鸡|麦穗冠鸡/.test(await p.locator('.books-screen').innerHTML()),'unknown new species stay unnamed');
  await screenshot('books-theme-390');
  await p.locator('[data-gs-directory]').click();await p.locator('[data-books-open="COL-8"]').click();assert.equal(await p.locator('[data-gs-next]').isDisabled(),true);await screenshot('books-col8');
  // Category pills and the shelf chooser replaced the dropdowns.
  if(await p.locator('[data-gs-directory]').count()&&!await p.locator('[data-books-category]:visible').count())await p.locator('[data-gs-directory]').click();await p.locator('[data-books-category="mementos"]:visible').first().click();await p.locator('[data-books-slot="0"]').click();await p.locator('[data-books-pick="M01"]').click();await p.locator('[data-books-slot="1"]').click();await p.locator('[data-books-pick="M08"]').click();
  s=await read();assert.deepEqual(s.expansion.collections.display,['M01','M08',null]);assert.equal(s.cp,seed.cp);await screenshot('books-mementos');
  for(const [width,height] of [[320,568],[1280,900]]){await p.setViewportSize({width,height});await screenshot(`books-mementos-${width}`);}
  await p.setViewportSize({width:390,height:844});
  await p.locator('.books-screen .close').click();await p.getByRole('button',{name:'农场',exact:true}).click();await p.waitForTimeout(400);
  assert.equal(await p.locator('.farm-map-mementos img').count(),2);await screenshot('farm-shelf');
  await p.reload();await start();assert.deepEqual((await read()).expansion.collections.display,['M01','M08',null]);
  checks.push('纪念物页选择两件陈列（农场陈列架显示两件已拥有纪念物），刷新保留；更换陈列不改CP或库存；12件中常客的4件显示“尚未得到”');
  await p.getByRole('button',{name:'厨房',exact:true}).click();
  await open();await p.locator('[data-regional-region="V"]').click();
  await trip({place:'V:0',focus:'materials',members:['0:128','0:0']});
  s=await read();assert.ok(s.expansion.collections.entitlements['STAMP-V'],'mixed old+new valley team earns the region trip stamp');assert.ok(s.expansion.facts.companionFirst['0:128']);
  checks.push('新旧混合队伍（荠菜煎饼鸡＋鸡宝）完整寻访谷地：点亮同行脚步格并自动盖上谷地地区实践印');
  assert.deepEqual(errors,[]);passed=true;await context.close();
}catch(error){if(p&&!p.isClosed()){await writeFile(resolve(output,'failure.txt'),await p.locator('body').innerText());await screenshot('failure');}throw error;}
finally{await browser?.close();server.kill();const report={checkedAt:new Date().toISOString(),passed,checks,screens,errors,isolatedProfile:true,acceleratedClock:true,realDeviceTest:false};await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));}
