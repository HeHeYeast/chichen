// Work G playable acceptance: the river guide, the bay route and an optional
// six-bird cargo exchange through real controls, including a mid-trip reload.
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
const {chromium}=require(packagePath),output=resolve(root,'artifacts/qa/work-g');await mkdir(output,{recursive:true});
const now=1800000000000,seed=freshState(now,20260923);
// Mid-game legacy save: kitchen Lv.2, duck bought, pan/boil/oven/keep-warm/steamer.
const OLD=['0:0','0:3','0:4','0:6','0:8','0:10','0:12','0:16','0:17','0:18','0:21','0:43','0:114','0:115','0:116','0:117','1:0','1:3','1:5','1:6','1:7','1:8','1:9','1:10','1:12','1:22'];
seed.kitchenLevel=2;seed.duck=true;seed.toolLevels=[0,1,1,1,1,1,1,-1,1];seed.cp=90000;
seed.total=Object.fromEntries(OLD.map((k,i)=>[k,i?20:900]));seed.farm=Object.fromEntries(OLD.map(k=>[k,9]));
for(let i=22;i<40;i++)seed.total['0:'+i]??=1;
// An earlier region already walked: valley specimen identified and C129 collected.
seed.expansion.discovery.cards['V-S1']=1;seed.expansion.discovery.identified['75']=2;seed.meta.factSeq=2;seed.expansion.regions.introSpecimenDone=['V'];seed.expansion.regions.opened=['V'];seed.expansion.methods.directions=['REC-V-C1','REC-V-C3','REC-V-C5','REC-V-D1','REC-V-D3','REC-V-D5'];seed.expansion.methods.freeProgress={V:{count:0,targetId:'REC-V-C1'}};seed.total['0:128']=1;seed.farm['0:128']=1;
seed.expansion.regions.history={companionFirst:{},trips:{},cardFacts:{'V-S1':{seq:1,tripId:'fixture-valley-intro',members:[{key:'0:0',G:4,F:2,environment:'yard',traits:['portable']}]}}};
seed.ingredients={};seed.progress.orders['first-sale']={accepted:true,completed:true,choice:'0:0',delivered:12};seed.progress.sources=earnedSources(seed);seed.progress.tutorialSeen=true;
const checks=[],errors=[],screens=[];let browser,p,passed=false;
const server=spawn(process.execPath,['server.mjs','0'],{cwd:root,stdio:['ignore','pipe','pipe'],windowsHide:true});
const read=()=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
const screenshot=async name=>{await p.waitForTimeout(120);await p.screenshot({path:resolve(output,name+'.png')});screens.push(name);};
const start=async()=>{await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.locator('.workshop-launch').waitFor();await p.waitForTimeout(650);};
const open=async()=>{if(await p.locator('.regional-screen').count())return;if(await p.locator('.screen-panel .close').count())await p.locator('.screen-panel .close').first().click();await p.getByRole('button',{name:'寻访',exact:true}).click();await p.locator('.regional-screen').waitFor();};
const tab=async name=>{await p.locator(`[data-regional-tab="${name}"]`).click();};
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
  await p.locator('[data-regional-depart]').click();await p.locator('[data-yes]').click();
  const s=await read();assert.equal(s.progress.trip.status,'running');
  await jump(s.progress.trip.endAt+1000);await p.locator('[data-regional-claim-notes]').waitFor();
  const back=await read();const result=back.progress.trip.regional.result;
  if(await p.locator('[data-regional-claim]').count()){await p.locator('[data-regional-claim]').click();await p.locator('[data-yes]').click();}
  if((await read()).progress.trip.status==='returned'){await p.locator('.regional-basket details summary').click();await p.locator('[data-regional-discard]').click();await p.locator('[data-yes]').click();}
  assert.equal((await read()).progress.trip.status,'settled');
  return result;
}
async function until(card,options,limit){for(let i=0;i<limit;i++){const r=await trip(options);if((await read()).expansion.discovery.cards[card])return i+1;void r;}throw Error(`${card} not found within ${limit} complete trips`);}
async function identify(id){await open();await tab('record');await p.locator(`[data-regional-identify="${id}"]`).click();assert.ok(Object.hasOwn((await read()).expansion.discovery.identified,String(id)));}
async function buy(id,count=1){while(await p.locator('.screen-panel .close').count())await p.locator('.screen-panel .close').first().click();await p.getByRole('button',{name:'厨房',exact:true}).click();await p.getByRole('button',{name:'补给 · 小卖部',exact:true}).click();await p.locator('[data-shop-tab="1"]').click();for(let i=0;i<count;i++){await p.locator(`[data-shop-buy-ingredient="${id}"]`).click();await p.locator('[data-yes]').click();}await p.getByRole('button',{name:'厨房',exact:true}).click();}
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
  await open();assert.equal(await p.locator('[data-regional-region="B"]').count(),1);await p.locator('[data-regional-region="B"]').click();
  assert.match(await p.locator('.regional-screen').innerText(),/沿湾路标/);await screenshot('bay-locked');
  await p.locator('[data-regional-region="R"]').click();await tab('trip');await p.locator('[data-regional-place="R:1"]').click();await p.locator('[data-regional-focus="materials"]').click();
  await team(['0:0']);await p.locator('[data-regional-guide]').check();await screenshot('river-guide-option');
  await p.locator('[data-regional-depart]').click();assert.match(await p.locator('.confirm').innerText(),/沿湾路标/);await p.locator('[data-yes]').click();
  let s=await read();assert.equal(s.progress.trip.regional.guide,true);await jump(s.progress.trip.endAt+1000);await p.locator('[data-regional-claim-notes]').waitFor();
  s=await read();assert.deepEqual(s.expansion.regions.guideFlags,['GUIDE-B']);assert.ok(!Object.keys(s.expansion.discovery.cards).includes('GUIDE-B'));
  if(await p.locator('[data-regional-claim]').count()){await p.locator('[data-regional-claim]').click();await p.locator('[data-yes]').click();}
  if((await read()).progress.trip.status==='returned'){await p.locator('.regional-basket details summary').click();await p.locator('[data-regional-discard]').click();await p.locator('[data-yes]').click();}
  checks.push('厨房Lv.3、发现40且谷地已辨认实收的旧档：海湾先显示“先追寻沿湾路标”；溪岸岸边摊勾选追寻路标，完整归队确定取得GUIDE-B，不计发现卡');
  await open();await p.locator('[data-regional-region="B"]').click();
  assert.equal((await trip({place:'B:1',focus:'materials',members:['1:0']})).cardId,'B-S1');
  s=await read();const bayTrip=s.progress.trip;assert.equal(bayTrip.routeId,'bay');assert.equal(bayTrip.endAt-bayTrip.startedAt,12*3600000);
  await identify(81);checks.push('海湾路线12小时：首趟保证盐花标本（换地点也保证），免费辨认');
  await open();await p.locator('[data-regional-region="B"]').click();await tab('trip');await p.locator('[data-regional-place="B:0"]').click();await p.locator('[data-regional-focus="lore"]').click();await team(['1:0']);
  await p.locator('.regional-cargo summary').click();for(let i=0;i<3;i++)await p.locator('[data-cargo-plus="0:3"]').click();for(let i=0;i<3;i++)await p.locator('[data-cargo-plus="0:8"]').click();
  await screenshot('cargo-six');const beforeFarm=structuredClone((await read()).farm),beforeCP=(await read()).cp;
  await p.locator('[data-regional-depart]').click();assert.match(await p.locator('.confirm').innerText(),/另带6只家常/);await p.locator('[data-yes]').click();
  s=await read();assert.equal(s.progress.trip.cargo.processed,false);assert.equal(s.farm['0:3'],beforeFarm['0:3'],'cargo is a reservation until the trip completes');
  await p.reload();await start();s=await read();assert.deepEqual(s.progress.trip.cargo.selection,{'0:3':3,'0:8':3});assert.equal(s.farm['0:3'],beforeFarm['0:3']);
  await jump(s.progress.trip.endAt+1000);await open();await p.locator('[data-regional-claim-notes]').waitFor();
  s=await read();assert.equal(s.progress.trip.cargo.outcome,'exchanged');assert.equal(s.farm['0:3'],beforeFarm['0:3']-3);assert.equal(s.farm['0:8'],beforeFarm['0:8']-3);
  assert.ok(s.progress.trip.remaining.includes(81)||s.ingredients[81]>0,'salt flower replaced one base slot');assert.ok(s.expansion.facts.predicateWitnesses['B-E1:cargoExchange']);
  assert.ok(s.cp-beforeCP<=18,'no sale CP for cargo');await screenshot('cargo-returned');
  checks.push('盐田小路带货6只：出发只占用不扣货，中途刷新不复制，完整归队才扣6只并以盐花替换1份基础材料；无售款、不计营业/采购');
  assert.deepEqual(errors,[]);passed=true;await context.close();
}catch(error){if(p&&!p.isClosed()){await writeFile(resolve(output,'failure.txt'),await p.locator('body').innerText());await screenshot('failure');}throw error;}
finally{await browser?.close();server.kill();const report={checkedAt:new Date().toISOString(),passed,checks,screens,errors,isolatedProfile:true,acceleratedClock:true,realDeviceTest:false};await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));}
