// Work L release acceptance in a real browser (accelerated clock, isolated profiles):
// the six golden legacy saves go through all five places after upgrading to the
// current schema, and a schema-5 save written mid-business with a regional trip in
// flight is upgraded across releases and settles exactly once.
import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import {resolve,join} from 'node:path';
import {fileURLToPath} from 'node:url';
import {mkdir,writeFile,readFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {syncProgress} from '../web/progression.js';
import {CURRENT_SAVE_VERSION} from '../web/save-migrations.js';
import {REGIONAL,resolveSpecies} from '../web/content-registry.js';
import {openBusiness} from '../web/business.js';
import {departRegional} from '../web/regional-exploration.js';
const root=fileURLToPath(new URL('../',import.meta.url)),require=createRequire(import.meta.url);
const candidates=[process.argv[2],process.env.CHICK_PLAYWRIGHT_PACKAGE,'playwright','playwright-core',process.env.APPDATA&&join(process.env.APPDATA,'npm/node_modules/gsd-pi/node_modules/playwright-core')].filter(Boolean);
let packagePath;for(const p of candidates){try{packagePath=require.resolve(p);break;}catch{}}
if(!packagePath)throw Error('An existing Playwright package is required.');
const {chromium}=require(packagePath),output=resolve(root,'artifacts/qa/work-l');await mkdir(output,{recursive:true});
const golden=JSON.parse(await readFile(resolve(root,'tests/fixtures/golden/expected.json'),'utf8'));
const checks=[],errors=[],screens=[];let browser,p,passed=false;
const server=spawn(process.execPath,['server.mjs','0'],{cwd:root,stdio:['ignore','pipe','pipe'],windowsHide:true});
const screenshot=async name=>{await p.waitForTimeout(120);await p.screenshot({path:resolve(output,name+'.png')});screens.push(name);};
const read=()=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
const start=async()=>{await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.locator('[data-control-id="nav:0"][tabindex="0"]').waitFor();await p.waitForTimeout(500);};
const nav=name=>p.getByRole('button',{name,exact:true}).click();
const hiddenNames=s=>REGIONAL.species.filter(c=>!(s.total?.[c.key]>0)).map(c=>resolveSpecies(c.key).title_zh_CN);
async function scan(names,where){const html=await p.locator('#game').evaluate(el=>el.innerHTML);for(const n of names)assert.ok(!html.includes(n),`${where} leaks ${n}`);}
async function contextAt(seed,at){
  const c=await browser.newContext({viewport:{width:390,height:844},reducedMotion:'reduce'});
  await c.addInitScript(({seed,at})=>{if(location.protocol==='about:')return;const offset=Number(sessionStorage.getItem('l-clock')??0);Date.now=()=>at+offset+Math.floor(performance.now());if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',seed);},{seed:JSON.stringify(seed),at});
  p=await c.newPage();p.on('pageerror',e=>errors.push(e.message));await p.goto(base+'/');await start();return c;
}
let base;
try{
  base=await new Promise((done,fail)=>{const timer=setTimeout(()=>fail(Error('server start timeout')),10000);server.once('error',fail);server.stdout.on('data',chunk=>{const m=String(chunk).match(/http:\/\/127\.0\.0\.1:\d+/);if(m){clearTimeout(timer);done(m[0]);}});});
  browser=await chromium.launch({headless:true,...(process.env.CHICK_QA_CHROME?{executablePath:process.env.CHICK_QA_CHROME}:{})});

  // 1. Six golden legacy saves (v1/v3) through all five places.
  for(const name of Object.keys(golden.cases)){
    const source=JSON.parse(await readFile(resolve(root,`tests/fixtures/golden/${name}.json`),'utf8'));
    const c=await contextAt(source,golden.now+3600000);let s=await read();
    assert.equal(s.version,CURRENT_SAVE_VERSION,`${name} upgraded`);const cp=s.cp,total=JSON.stringify(s.total);
    const names=hiddenNames(s);
    for(const [place,selector] of [['农场',null],['生意','.screen-panel'],['寻访','.regional-screen'],['图鉴','.screen-panel'],['厨房',null]]){await nav(place);if(selector)await p.locator(selector).first().waitFor();await scan(names,`${name}/${place}`);}
    await screenshot(`golden-${name}`);
    await p.reload();await start();s=await read();assert.equal(s.cp,cp,`${name} CP stable across reload`);assert.equal(JSON.stringify(s.total),total,`${name} collection kept`);
    await c.close();
  }
  checks.push('六个黄金旧档（v1新手/v1与v3前期/v3中期含进行中锅与寻访/193全收/满仓近上限）升级到schema 6后，五个地方逐一打开无报错、未知新品名不泄露；刷新后CP与收录不变');

  // 2. Cross-release upgrade with a business session and a regional trip in flight.
  const T0=1800000000000,H=3600000;
  let s=E.freshState(T0,4040);s.kitchenLevel=1;s.cp=3000;s.toolLevels[1]=0;
  s.total=Object.fromEntries(Array.from({length:30},(_,i)=>[`0:${i}`,i?3:400]));s.farm={'0:0':30,'0:3':12,'0:1':3};
  s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};s.progress.tutorialSeen=true;syncProgress(s);s=E.normalizeSave(s,T0);
  openBusiness(s,{menuId:'MN1',stock:{'0:0':6,'0:3':6}},T0);departRegional(s,{regionId:'V',placeId:'V:0',focus:'specimen',members:['0:1']},T0);
  s=E.normalizeSave(s,T0);const tripEnd=s.progress.trip.endAt;
  const v5=structuredClone(s);v5.version=5;v5.meta.migrationHistory.pop();for(const k of ['collections','regulars','projects','menus'])delete v5.expansion[k];
  const c=await contextAt(v5,T0+4*H+1000);s=await read();
  assert.equal(s.version,CURRENT_SAVE_VERSION);const report=s.expansion.business.lastReport;assert.ok(report&&report.totalSold===12,'both windows settled once while the app was closed');
  const cpAfter=s.cp;assert.ok(tripEnd<=T0+4*H,'trip already due');
  assert.ok(s.progress.trip?.returnedAt,'the in-flight trip returned at its end time');
  await nav('寻访');await p.locator('[data-regional-claim]').first().click();await p.waitForTimeout(300);if(await p.locator('#dialog-layer [data-yes]').count())await p.locator('#dialog-layer [data-yes]').click();
  s=await read();assert.ok(!s.progress.trip||s.progress.trip.returnedAt,'claimed (any overflow stays in the basket)');assert.ok(Object.keys(s.expansion.discovery.cards).includes('V-S1'),'the first valley trip still guarantees its specimen');
  await screenshot('upgrade-in-flight');await p.reload();await start();const again=await read();assert.equal(again.expansion.business.lastReport.totalSold,12);assert.ok(again.cp>=cpAfter,'no double settlement or loss');
  checks.push('schema 5旧版本在营业中（12只）且谷地寻访在途时保存；新版4小时后打开：两窗各6只只结算一次，寻访票据照常归队并保证首标本；刷新不重复');
  await c.close();
  assert.deepEqual(errors,[]);passed=true;
}catch(error){if(p&&!p.isClosed()){await writeFile(resolve(output,'failure.txt'),await p.locator('body').innerText());await screenshot('failure');}throw error;}
finally{await browser?.close();server.kill();const report={checkedAt:new Date().toISOString(),passed,checks,screens,errors,isolatedProfile:true,acceleratedClock:true,realDeviceTest:false};await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));}
