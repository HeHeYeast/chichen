// Uses disposable browser contexts and the loopback development API only.
import {createRequire} from 'node:module';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {freshState} from '../web/engine.js';
import {spawn} from 'node:child_process';
import {fileURLToPath} from 'node:url';
const require=createRequire(import.meta.url),{chromium}=require(process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const output=new URL('../artifacts/cloud-implementation/',import.meta.url);await mkdir(output,{recursive:true});
const root=fileURLToPath(new URL('../',import.meta.url)),base='http://127.0.0.1:4188';
const servers=[spawn(process.execPath,['server.mjs','4188'],{cwd:root,windowsHide:true,stdio:'pipe'}),spawn(process.execPath,['cloud/dev-server.mjs'],{cwd:root,windowsHide:true,stdio:'pipe',env:{...process.env,CHICK_API_PORT:'4189',CHICK_WEB_ORIGIN:base,CHICK_DB_FILE:fileURLToPath(new URL('../cloud/.local/qa-'+crypto.randomUUID()+'.sqlite',import.meta.url))}})];
for(const server of servers)await new Promise((resolve,reject)=>{server.stdout.once('data',resolve);server.once('error',reject);server.once('exit',code=>reject(Error('Test server exited '+code)));});
const browser=await chromium.launch({executablePath:'C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe',headless:true});
const report={runtime:'isolated Edge contexts + local HTTP + real SQLite',checks:[],errors:[],realDevice:false,production:false};
async function enter(page){if(await page.locator('[data-cloud-upload]').isVisible())return;const start=page.getByRole('button',{name:'开始游戏',exact:true}),settings=page.getByRole('button',{name:'设置',exact:true});await Promise.race([start.waitFor(),settings.waitFor()]);if(await start.isVisible()){await start.click();await page.locator('[data-control-id="nav:0"][tabindex="0"]').waitFor();}await settings.click();await page.locator('.settings-screen [data-save]').waitFor();await page.locator('[data-cloud-open]').click();}
async function auth(page,register=false){await page.locator('[data-cloud-name]').fill('alice');await page.locator('[data-cloud-password]').fill('browser-test-passphrase');await page.locator(register?'[data-cloud-register]':'[data-cloud-login]').click();await page.getByRole('button',{name:'开始游戏',exact:true}).waitFor();}
const read=page=>page.evaluate(()=>{const p=JSON.parse(localStorage.getItem('chick-kitchen-v1.active-profile'));return {profile:p,state:JSON.parse(localStorage.getItem(p?'chick-kitchen-v1.account.'+p.uid:'chick-kitchen-v1')),guest:JSON.parse(localStorage.getItem('chick-kitchen-v1'))};});
try{
  const a=await browser.newContext({viewport:{width:430,height:932}}),page=await a.newPage();page.on('pageerror',e=>report.errors.push(e.message));
  const configure=ctx=>ctx.route('**/web/cloud-config.js',route=>route.fulfill({contentType:'application/javascript',body:'export const CLOUD_ENDPOINT="http://127.0.0.1:4189"; export const cloudEndpoint=()=>CLOUD_ENDPOINT;'}));await configure(a);
  const seeded=freshState(Date.now(),42);seeded.cp=12345;
  await page.addInitScript(s=>{if(localStorage.getItem('qa-seeded')===null){localStorage.setItem('chick-kitchen-v1',JSON.stringify(s));localStorage.setItem('qa-seeded','1');}},seeded);
  await page.goto(base);await enter(page);await auth(page,true);let saved=await read(page);assert.equal(saved.state.cp,12345);assert.equal(saved.guest.cp,12345);report.checks.push('old guest registration preserves guest and copies account progress');
  await enter(page);await page.locator('[data-cloud-upload]').click();await page.locator('.confirm p').filter({hasText:'云端已收到'}).waitFor();await page.locator('[data-yes]').click();report.checks.push('UI backup acknowledged by local server');
  const b=await browser.newContext({viewport:{width:390,height:844}});await configure(b);const other=await b.newPage();other.on('pageerror',e=>report.errors.push(e.message));await other.goto(base);await enter(other);await auth(other);assert.equal((await read(other)).state.cp,600);
  await enter(other);await other.locator('[data-cloud-preview]').click();await other.locator('[data-cloud-restore]').click();await other.locator('[data-yes]').click();await other.getByRole('button',{name:'开始游戏',exact:true}).waitFor();assert.equal((await read(other)).state.cp,12345);report.checks.push('second browser login and explicit restore');
  await enter(page);await page.locator('[data-cloud-upload]').click();await page.locator('.confirm p').filter({hasText:'云端已收到'}).waitFor();await page.locator('[data-yes]').click();
  await enter(other);await other.locator('[data-cloud-upload]').click();await other.locator('.confirm p').filter({hasText:'其他进度'}).waitFor();assert.equal((await read(other)).state.cp,12345);await other.locator('[data-yes]').click();report.checks.push('second device cannot silently overwrite');
  await b.setOffline(true);await other.locator('[data-cloud-upload]').click();await other.locator('.confirm').waitFor();assert.equal((await read(other)).state.cp,12345);await other.locator('[data-yes]').click();await b.setOffline(false);report.checks.push('network failure retains durable progress');
  await page.screenshot({path:fileURLToPath(new URL('web-cloud.png',output))});
  await page.setViewportSize({width:320,height:640});
  const bounds=await page.locator('[data-cloud-upload],[data-cloud-preview],[data-cloud-history],[data-cloud-logout]').evaluateAll(items=>items.map(el=>{const r=el.getBoundingClientRect();return {left:r.left,right:r.right,width:innerWidth};}));assert.ok(bounds.every(r=>r.left>=0&&r.right<=r.width));
  await page.screenshot({path:fileURLToPath(new URL('web-cloud-320.png',output))});report.checks.push('cloud actions remain within a 320px viewport');
  await page.locator('[data-cloud-logout]').click();await page.getByRole('button',{name:'开始游戏',exact:true}).waitFor();saved=await read(page);assert.equal(saved.profile,null);assert.equal(saved.guest.cp,12345);report.checks.push('logout returns to preserved guest');
  await enter(page);await page.locator('[data-cloud-back]').click();await page.locator('[data-feedback-open]').click();
  const feedback='测试反馈：断网时先保留草稿，联网后手动提交，并且返回可查询的收件编号。';
  await page.locator('[data-feedback-text]').fill(feedback);await a.setOffline(true);await page.locator('[data-feedback-send]').click();await page.locator('.confirm').waitFor();await page.locator('[data-yes]').click();
  assert.equal(await page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1.feedback-draft')).text),feedback);await a.setOffline(false);
  await page.locator('[data-feedback-send]').click();await page.locator('.confirm p').filter({hasText:'反馈已收到'}).waitFor();assert.equal(await page.evaluate(()=>localStorage.getItem('chick-kitchen-v1.feedback-draft')),null);report.checks.push('feedback keeps offline draft and acknowledges actual server receipt');
  // A real browser process restart uses an isolated on-disk profile, never the user's browser.
  const localEntries=await page.evaluate(()=>Object.entries(localStorage)),accountKey=localEntries.find(([k])=>/^chick-kitchen-v1\.account\.[a-f0-9-]{36}$/.test(k))[0];
  localEntries.push(['chick-kitchen-v1.active-profile',JSON.stringify({uid:accountKey.split('.account.')[1],username:'alice'})]);
  const persistentDir=fileURLToPath(new URL('../cloud/.local/browser-'+crypto.randomUUID(),import.meta.url));
  let persistent=await chromium.launchPersistentContext(persistentDir,{executablePath:'C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe',headless:true});
  try{await persistent.addInitScript(entries=>{if(!localStorage.getItem('restart-seeded')){for(const [k,v]of entries)localStorage.setItem(k,v);localStorage.setItem('restart-seeded','1');}},localEntries);const p=await persistent.newPage();await p.goto(base);await p.getByRole('button',{name:'开始游戏',exact:true}).waitFor();assert.equal((await read(p)).state.cp,12345);}finally{await persistent.close();}
  persistent=await chromium.launchPersistentContext(persistentDir,{executablePath:'C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe',headless:true});
  try{const p=await persistent.newPage();await p.goto(base);await p.getByRole('button',{name:'开始游戏',exact:true}).waitFor();assert.equal((await read(p)).state.cp,12345);assert.equal((await read(p)).guest.cp,12345);assert.equal(await p.evaluate(()=>sessionStorage.getItem('chick-cloud-session-v1')),null);await p.reload();await p.getByRole('button',{name:'开始游戏',exact:true}).waitFor();assert.equal((await read(p)).state.cp,12345);}finally{await persistent.close();}
  report.checks.push('real isolated browser process close, restart and refresh preserve guest and account progress without a login session');
  assert.deepEqual(report.errors,[]);report.passed=true;
}catch(e){report.passed=false;report.error=e.stack;const p=browser.contexts()[0]?.pages()[0];if(p){report.pageText=(await p.locator('body').innerText()).slice(-5000);await p.screenshot({path:fileURLToPath(new URL('failure.png',output))});}throw e;}
finally{await writeFile(new URL('browser.json',output),JSON.stringify(report,null,2));await browser.close();for(const server of servers)server.kill();console.log(JSON.stringify(report,null,2));}
