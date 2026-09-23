import {CURRENT_SAVE_VERSION} from '../web/save-migrations.js';
// Work A integration acceptance: real entry/UI, isolated browser storage only.
import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import {resolve,join} from 'node:path';
import {fileURLToPath} from 'node:url';
import {mkdir,readFile,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
const root=fileURLToPath(new URL('../',import.meta.url)),require=createRequire(import.meta.url);
const candidates=[process.argv[2],process.env.CHICK_PLAYWRIGHT_PACKAGE,'playwright','playwright-core',process.env.APPDATA&&join(process.env.APPDATA,'npm/node_modules/gsd-pi/node_modules/playwright-core')].filter(Boolean);
let packagePath;for(const p of candidates){try{packagePath=require.resolve(p);break;}catch{}}
if(!packagePath)throw Error('An existing Playwright installation is required.');
const {chromium}=require(packagePath),output=resolve(root,'artifacts/qa/work-a');await mkdir(output,{recursive:true});
const fixtures=JSON.parse(await readFile(resolve(root,'tests/fixtures/save-contract.json'),'utf8'));
const source=fixtures.cases.find(c=>c.name==='v2-active-batch-and-events').normalized,raw=JSON.stringify(source);
const checks=[],errors=[];let browser,active,passed=false;
const server=spawn(process.execPath,['server.mjs','0'],{cwd:root,stdio:['ignore','pipe','pipe'],windowsHide:true});
const read=p=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
const assets=s=>({cp:s.cp,farm:s.farm,total:s.total,ingredients:s.ingredients,toolLevels:s.toolLevels,events:s.events,orders:s.progress.orders,trip:s.progress.trip});
const start=async p=>{await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.locator('[data-control-id="nav:0"][tabindex="0"]').waitFor();};
async function settings(p){await p.getByRole('button',{name:'设置',exact:true}).click();await p.locator('[data-export]').waitFor();}
async function importFile(p,path){const chooser=p.waitForEvent('filechooser');await p.locator('[data-import]').click();await(await chooser).setFiles(path);await p.locator('[data-yes]').click();await p.locator('.confirm p').filter({hasText:'进度已恢复'}).waitFor();await p.locator('[data-yes]').click();}
try {
  const base=await new Promise((done,fail)=>{const timer=setTimeout(()=>fail(Error('isolated server timeout')),10000);server.once('error',fail);server.stdout.on('data',chunk=>{const m=String(chunk).match(/http:\/\/127\.0\.0\.1:\d+/);if(m){clearTimeout(timer);done(m[0]);}});});
  browser=await chromium.launch({headless:true,...(process.env.CHICK_QA_CHROME?{executablePath:process.env.CHICK_QA_CHROME}:{})});
  const context=await browser.newContext({viewport:{width:430,height:932},acceptDownloads:true,reducedMotion:'reduce'});
  await context.addInitScript(({raw,now})=>{window.__workAOffset=0;Date.now=()=>now+window.__workAOffset+Math.floor(performance.now());if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',raw);},{raw,now:fixtures.now});
  const p=active=await context.newPage();p.on('pageerror',e=>errors.push(e.message));await p.goto(base+'/');await start(p);
  const migrated=await read(p);assert.equal(migrated.version,CURRENT_SAVE_VERSION);assert.deepEqual(assets(migrated),assets(source));
  assert.equal(await p.evaluate(()=>localStorage.getItem('chick-kitchen-v1.pre-upgrade-v3')),raw);
  checks.push(`正式入口读取共享v3批次存档，迁移为${CURRENT_SAVE_VERSION}且保留升级前逐字节JSON与资产`);
  const second=await context.newPage();second.on('pageerror',e=>errors.push(e.message));await second.goto(base+'/');
  await second.locator('[data-recovery-message]').waitFor();assert.match(await second.locator('[data-recovery-message]').innerText(),/另一个窗口/);
  const beforeReadonly=await read(p);await second.waitForTimeout(200);assert.deepEqual(assets(await read(p)),assets(beforeReadonly));await second.close();
  checks.push('第二正式标签页只读，不能覆盖持有Web Lock的首标签进度');
  await p.evaluate(offset=>window.__workAOffset=offset,Math.ceil(source.batch.ends-fixtures.now)+1000);
  await p.waitForFunction(()=>{const s=JSON.parse(localStorage.getItem('chick-kitchen-v1'));return s.batch.eggs.every(e=>e.status!=='egg');},{timeout:15000});
  await p.waitForTimeout(2600);
  for(let i=0;i<24;i++){await p.locator(`[data-control-id="egg:${i}"]`).press('Enter');}
  const harvested=await read(p);assert.equal(harvested.batch.eggs.filter(e=>e.collected).length,24);
  assert.equal(harvested.cp,migrated.cp+24);assert.equal(Object.values(harvested.farm).reduce((a,b)=>a+b,0),29);
  checks.push('旧批次按原票据完成并经正式厨房逐枚收取24只，CP与库存同步增加');
  await p.locator('.workshop-launch').click();await p.locator('[data-workshop-tab="story"]').click();
  await p.locator('[data-accept="first-sale"]').click();await p.locator('[data-order-quantity="first-sale"]').fill('12');await p.locator('[data-deliver="first-sale"]').click();await p.locator('[data-yes]').click();
  assert.equal((await read(p)).progress.orders['first-sale'].completed,true);
  await p.locator('.workshop-screen .close').click();
  checks.push('旧首章采购从正式手艺入口接受并交付12只，完成记录保留');
  await p.getByRole('button',{name:'农场',exact:true}).click();await p.locator('[data-control-id="farm:harvest"]').click();
  await p.locator('[data-harvest-plus="0:0"]').click();const beforeSale=await read(p);await p.locator('[data-harvest-sell]').click();await p.locator('[data-yes]').click();const sold=await read(p);
  assert.equal(sold.farm['0:0'],beforeSale.farm['0:0']-1);assert.ok(sold.cp>beforeSale.cp);assert.deepEqual(sold.total,beforeSale.total);
  checks.push('正式农场出售减少1只库存并增加货款，永久图鉴记录不减少');
  await p.locator('.harvest-screen .close').click();await settings(p);
  const downloading=p.waitForEvent('download');await p.locator('[data-export]').click();const download=await downloading,backupPath=resolve(output,'work-a-ui-backup.json');await download.saveAs(backupPath);await p.locator('[data-yes]').click();
  const backup=JSON.parse(await readFile(backupPath,'utf8'));assert.deepEqual(assets(backup.save),assets(await read(p)));
  await importFile(p,backupPath);assert.deepEqual(assets(await read(p)),assets(backup.save));
  checks.push('正式设置导出JSON与导入确认路径保持CP、库存、图鉴、旧奖励及采购记录');
  await settings(p);const trip=fixtures.cases.find(c=>c.name==='v3-partially-claimed-basket').normalized;
  const tripPath=resolve(output,'old-trip-input.json');await writeFile(tripPath,JSON.stringify(trip));await importFile(p,tripPath);
  await p.locator('.workshop-launch').click();await p.locator('[data-workshop-tab="trip"]').click();
  // This shared legacy basket is deliberately full. Make room through the
  // actual ingredient picker and cooking action, never by editing saved stock.
  await p.locator('[data-trip-home]').click();await p.locator('[data-control-id="ingredient"]').click();await p.locator('[data-id="0"]').click();await p.locator('[data-ok]').click();
  await p.locator('[data-control-id="tool:0"]').click();await p.locator('[data-yes]').click();
  await p.locator('.workshop-launch').click();await p.locator('[data-workshop-tab="trip"]').click();
  const beforeClaim=await read(p);await p.locator('[data-claim-trip]').click();await p.locator('[data-yes]').click();const claimed=await read(p);
  assert.equal(claimed.progress.trip.status,'settled');assert.equal(Object.values(claimed.ingredients).reduce((a,b)=>a+b,0),Object.values(beforeClaim.ingredients).reduce((a,b)=>a+b,0)+1);
  checks.push('旧已部分领取寻访从正式寻访页领取剩余1格，结清且不重抽票据');
  await p.screenshot({path:resolve(output,'old-trip-settled.png')});
  await p.reload();await start(p);assert.equal((await read(p)).progress.trip.status,'settled');assert.deepEqual(assets(await read(p)),assets(claimed));
  checks.push('刷新重新进入后保留已结清寻访与所有资产');
  assert.deepEqual(errors,[]);await context.close();passed=true;
} catch(error) {
  if(active&&!active.isClosed()){await writeFile(resolve(output,'failure.txt'),await active.locator('body').innerText());await active.screenshot({path:resolve(output,'failure.png')});}
  throw error;
} finally {
  await browser?.close();server.kill();
  const report={checkedAt:new Date().toISOString(),passed,checks,errors,isolatedProfile:true,acceleratedClock:true,nativeDeviceTest:false};
  await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));
}
