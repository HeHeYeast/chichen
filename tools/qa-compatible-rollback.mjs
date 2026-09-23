// A compatible rollback is a different immutable build policy, never an older
// schema. Intercept only this module in isolated browser contexts; keep the real
// UI, repository, commands, tickets and timeline interpreters unchanged.
import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import {resolve,join} from 'node:path';
import {fileURLToPath} from 'node:url';
import {mkdir,readFile,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {LEGACY193} from '../web/legacy-content.js';
import {earnedSources} from '../web/progression.js';
import {departRegional} from '../web/regional-exploration.js';
import {prepareRegionalRecipe} from '../web/regional-methods.js';
import {openBusiness} from '../web/business.js';
import {policyOverlay,PAUSED_POLICY} from './build-compatible-rollback.mjs';

const root=fileURLToPath(new URL('../',import.meta.url)),require=createRequire(import.meta.url);
const candidates=[process.argv[2],process.env.CHICK_PLAYWRIGHT_PACKAGE,'playwright','playwright-core',process.env.APPDATA&&join(process.env.APPDATA,'npm/node_modules/gsd-pi/node_modules/playwright-core')].filter(Boolean);
let packagePath;for(const candidate of candidates)try{packagePath=require.resolve(candidate);break;}catch{}
if(!packagePath)throw Error('An existing Playwright package is required.');
const {chromium}=require(packagePath),output=resolve(root,'artifacts/qa/compatible-rollback');await mkdir(output,{recursive:true});
const policyPath=resolve(root,'web/rollback-policy.js'),policySource=await readFile(policyPath,'utf8'),overlay=policyOverlay(policySource),now=1800000000000;
function seed(){
  const s=E.freshState(now,21);s.kitchenLevel=3;s.duck=true;s.toolLevels=s.toolLevels.map(()=>2);s.cp=1000000;s.progress.tutorialSeen=true;
  s.total=Object.fromEntries(LEGACY193.characters.flatMap((list,egg)=>list.map(c=>[`${egg}:${c.id}`,3])));s.total['0:0']=9000;
  s.farm=Object.fromEntries(Object.keys(s.total).map(k=>[k,30]));s.progress.sources=earnedSources(s);
  s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};
  s.expansion.discovery.cards['V-S1']=++s.meta.factSeq;s.expansion.discovery.identified[75]=++s.meta.factSeq;
  s.expansion.regions.introSpecimenDone=['V'];s.expansion.methods.directions=['REC-V-C1'];s.expansion.methods.full=['REC-V-C1'];s.ingredients={75:1};
  return E.normalizeSave(s,now);
}
const source=seed();prepareRegionalRecipe(source,'REC-V-C1');E.startBatch(source,1,now);
departRegional(source,{regionId:'V',placeId:'V:0',focus:'materials',members:['0:0']},now);
openBusiness(source,{menuId:'MN1',stock:{'0:3':12}},now);E.normalizeSave(source,now);
const checks=[],errors=[],screens=[];let browser,p,passed=false,servedOverlays=0;
const server=spawn(process.execPath,['server.mjs','0'],{cwd:root,stdio:['ignore','pipe','pipe'],windowsHide:true});
const read=()=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
const nav=label=>p.getByRole('button',{name:label,exact:true}).click();
const shot=async name=>{await p.screenshot({path:resolve(output,name+'.png')});screens.push(name);};
const jump=at=>p.evaluate(({at,now})=>{window.rollbackOffset=at-now;sessionStorage.setItem('rollback-clock',String(window.rollbackOffset));},{at,now});
const start=async()=>{await nav('开始游戏');await p.locator('.workshop-launch').waitFor();await p.waitForTimeout(700);};
const business=async()=>{await nav('生意');await p.locator('[data-trade-view="business"]').first().click();await p.locator('.business-screen').waitFor();};
try{
  const base=await new Promise((done,fail)=>{const timer=setTimeout(()=>fail(Error('Server start timeout')),10000);server.once('error',fail);server.stdout.on('data',chunk=>{const m=String(chunk).match(/http:\/\/127\.0\.0\.1:\d+/);if(m){clearTimeout(timer);done(m[0]);}});});
  browser=await chromium.launch({headless:true,...(process.env.CHICK_QA_CHROME?{executablePath:process.env.CHICK_QA_CHROME}:{})});
  async function contextFor(saved){
    const context=await browser.newContext({viewport:{width:1280,height:900},reducedMotion:'reduce'});
    await context.route('**/web/rollback-policy.js',route=>{servedOverlays++;return route.fulfill({status:200,contentType:'text/javascript',body:overlay});});
    await context.addInitScript(({saved,now})=>{if(location.protocol==='about:')return;window.rollbackOffset=Number(sessionStorage.getItem('rollback-clock')??0);Date.now=()=>now+window.rollbackOffset+Math.floor(performance.now());if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(saved));},{saved,now});
    p=await context.newPage();p.on('pageerror',e=>errors.push(e.message));await p.goto(base+'/');await start();
    assert.deepEqual(await p.evaluate(async()=> (await import('/web/rollback-policy.js')).ROLLBACK_POLICY),PAUSED_POLICY);
    assert.equal(await p.locator('#game').evaluate(el=>el.classList.contains('is-wide')),false,'paused UI uses the compatible single-column shell');
    return context;
  }
  const manual=await contextFor(source);
  for(const label of ['厨房','农场','生意','寻访','图鉴']){
    await nav(label);const button=p.getByRole('button',{name:label,exact:true});assert.ok(await button.isVisible());
    if(label==='生意')await p.locator('[data-trade-view]').first().waitFor();
    if(label==='寻访')await p.locator('.regional-screen').waitFor();
    if(label==='图鉴')await p.locator('[data-book-tab]').first().waitFor();
  }
  await shot('five-entrances');checks.push('关闭七域与四地区后，桌面退回单列，厨房/农场/生意/寻访/图鉴仍可达');

  await business();const beforeClose=await read();await p.locator('[data-business-close]').click();await p.locator('[data-yes]').click();await p.locator('.business-receipt-head').waitFor();
  let saved=await read();assert.equal(saved.expansion.business.active,null);assert.equal(saved.expansion.business.lastReport.reason,'manual');assert.equal(saved.expansion.business.lastReport.totalSold,0);assert.equal(saved.cp,beforeClose.cp);assert.equal(saved.farm['0:3'],beforeClose.farm['0:3']);await shot('manual-close');
  await p.locator('[data-business-again]').click();await p.locator('[data-business-quantity="0:3"]').fill('6');await p.locator('[data-business-quantity="0:3"]').press('Tab');
  assert.ok(await p.locator('[data-business-open]').isDisabled());assert.match(await p.locator('.business-screen').innerText(),/营业暂时暂停/);
  assert.equal((await read()).cp,beforeClose.cp);checks.push('已有营业可由正式入口立即收摊，未售库存保留；新营业备货后仍被拒绝且不扣CP');

  await nav('厨房');await jump(E.batchReadyAt(source.batch)+1000);await p.waitForTimeout(3800);
  for(let i=0;i<24;i++)await p.locator(`[data-control-id="egg:${i}"]`).press('Enter');
  saved=await read();assert.equal(saved.batch.eggs.filter(e=>e.collected).length,24);assert.ok(Object.values(saved.farm).reduce((a,b)=>a+b,0)>=Object.values(source.farm).reduce((a,b)=>a+b,0)+24);await shot('frozen-batch-collected');checks.push('已保存地区锅保留原票据，正式厨房逐只收齐24枚，关闭地区新操作不阻止收获');

  await jump(source.progress.trip.endAt+1000);await nav('寻访');await p.locator('[data-regional-claim]').waitFor();
  const returned=await read(),basket=returned.progress.trip.remaining.length,materialsBefore=Object.values(returned.ingredients).reduce((a,b)=>a+b,0);
  await p.locator('[data-regional-claim]').click();await p.locator('[data-yes]').click();saved=await read();assert.equal(saved.progress.trip.status,'settled');assert.equal(Object.values(saved.ingredients).reduce((a,b)=>a+b,0),materialsBefore+basket);
  await p.locator('[data-regional-last]').click();assert.ok(await p.locator('[data-regional-depart]').isDisabled());assert.match(await p.locator('.regional-screen').innerText(),/暂时暂停/);await shot('returned-trip-new-trip-paused');
  const settledCP=saved.cp,settledFarm=structuredClone(saved.farm),settledIngredients=structuredClone(saved.ingredients);await p.reload();await start();await nav('寻访');saved=await read();assert.equal(saved.cp,settledCP);assert.deepEqual(saved.farm,settledFarm);assert.deepEqual(saved.ingredients,settledIngredients);assert.equal(saved.progress.trip.status,'settled');
  E.normalizeSave(saved,source.progress.trip.endAt+5000);checks.push('已有寻访自动归队并从正式篮子入口领取；新行程禁用，刷新不重复CP/材料/伙伴，正式默认构建可读取结清存档');

  // Also exercise the exact served build's domain guards on isolated clones.
  // This catches a merely disabled UI that forgot to protect command entry.
  const guards=await p.evaluate(async()=>{
    const original=JSON.parse(localStorage.getItem('chick-kitchen-v1'));
    const [trip,methods,biz,orders,projects,regulars,collections]=await Promise.all(['regional-exploration','regional-methods','business','orders','projects','regulars','collection-progress'].map(name=>import(`/web/${name}.js`)));
    const actions=[s=>trip.departRegional(s,{regionId:'V',members:['0:0']},Date.now()),s=>methods.prepareRegionalRecipe(s,'REC-V-C1'),s=>biz.openBusiness(s,{menuId:'MN1',stock:{'0:3':6}},Date.now()),s=>orders.acceptProposal(s,'unused',{},Date.now()),s=>projects.completeProjectStage(s,'PJ-1','PJ-1-A')];
    return actions.map(action=>{const draft=structuredClone(original);let code=null;try{action(draft);}catch(e){code=e.code;}return {code,unchanged:JSON.stringify(draft)===JSON.stringify(original)};}).concat([s=>orders.orderMilestone(s,Date.now(),'test'),s=>regulars.reconcileRegulars(s),s=>collections.reconcileEntitlements(s)].map(action=>{const draft=structuredClone(original);return {empty:action(draft).length===0,unchanged:JSON.stringify(draft)===JSON.stringify(original)};}));
  });
  assert.ok(guards.every(x=>x.unchanged&&(x.code==='FEATURE_PAUSED'||x.empty)));checks.push('浏览器真实关闭构建：地区/试做/营业/订单/项目命令拒绝且资产不变，常客/收藏/订单新追认停止');await manual.close();

  const auto=await contextFor(source);await jump(now+24*3600000+1000);await p.goto('about:blank');await p.goto(base+'/');await start();await business();await p.locator('.business-receipt-head').waitFor();
  saved=await read();const report=structuredClone(saved.expansion.business.lastReport);assert.equal(saved.expansion.business.active,null);assert.equal(report.totalSold,12);assert.equal(saved.cp,source.cp+report.income);assert.equal(saved.farm['0:3'],source.farm['0:3']-12);const autoCP=saved.cp,autoFarm=structuredClone(saved.farm);
  await shot('offline-auto-close');await p.locator('[data-business-tab="report"]').click();await p.reload();await start();await business();saved=await read();assert.deepEqual(saved.expansion.business.lastReport,report);assert.equal(saved.cp,autoCP);assert.deepEqual(saved.farm,autoFarm);E.normalizeSave(saved,now+24*3600000+10000);
  checks.push('已有营业离线跨24h仍自动售罄12只并结清一次；反复看账单与重启不重复收入，结清后schema6仍可读取');await auto.close();
  assert.ok(servedOverlays>=2);assert.equal(await readFile(policyPath,'utf8'),policySource,'production policy was never modified');assert.deepEqual(errors,[]);passed=true;
}catch(error){if(p&&!p.isClosed()){await writeFile(resolve(output,'failure.txt'),await p.locator('body').innerText());await shot('failure');}throw error;}
finally{await browser?.close();server.kill();const report={checkedAt:new Date().toISOString(),passed,checks,errors,screens,servedOverlays,policy:PAUSED_POLICY,productionFilesChanged:false,isolatedProfile:true,acceleratedClock:true,realDeviceTest:false};await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2)+'\n');console.log(JSON.stringify(report,null,2));}
