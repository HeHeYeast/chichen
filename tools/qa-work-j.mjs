// Work J playable acceptance: pay/deliver project stages, reopen with the same
// balance, save/load a menu preset, learn ALT-R and add portraits to PJ-4.
import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import {resolve,join} from 'node:path';
import {fileURLToPath} from 'node:url';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {freshState,char} from '../web/engine.js';
import {syncProgress} from '../web/progression.js';
import {LEGACY193} from '../web/legacy-content.js';
import {reduceFacts} from '../web/facts.js';
import {normalizeSave} from '../web/engine.js';
const root=fileURLToPath(new URL('../',import.meta.url)),require=createRequire(import.meta.url);
const candidates=[process.argv[2],process.env.CHICK_PLAYWRIGHT_PACKAGE,'playwright','playwright-core',process.env.APPDATA&&join(process.env.APPDATA,'npm/node_modules/gsd-pi/node_modules/playwright-core')].filter(Boolean);
let packagePath;for(const p of candidates){try{packagePath=require.resolve(p);break;}catch{}}
if(!packagePath)throw Error('An existing Playwright package is required.');
const {chromium}=require(packagePath),output=resolve(root,'artifacts/qa/work-j');await mkdir(output,{recursive:true});
const now=1800000000000,seed=freshState(now,4242);
// A late legacy player: 193 recorded, two menus served, river specimens identified, bay guide.
seed.kitchenLevel=2;seed.duck=true;seed.toolLevels=[0,0,0,0,0,0,0,0,0];seed.cp=5000;seed.progress.tutorialSeen=true;
seed.total=Object.fromEntries(LEGACY193.characters.flatMap((list,egg)=>list.map(c=>[`${egg}:${c.id}`,c.id===0&&egg===0?9000:2])));
for(const k of ['0:134','0:135','0:136','1:71'])seed.total[k]=1;
seed.farm={'0:0':40,'0:3':20,'0:4':20,'0:8':10};seed.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};syncProgress(seed);
Object.assign(seed,normalizeSave(seed,now));
reduceFacts(seed,['MN1','MN3'].map((menuId,i)=>({kind:'businessWitness',sessionId:`business-${i+1}`,menuId,soldByKey:{'0:0':6},roleSales:{},fullSoldByKey:{},fullRoleSales:{},valid:true,complete:false})));
for(const id of ['R-S1','R-S2'])seed.expansion.discovery.cards[id]=++seed.meta.factSeq;for(const id of ['77','78'])seed.expansion.discovery.identified[id]=++seed.meta.factSeq;
seed.expansion.regions.guideFlags=['GUIDE-B'];
const checks=[],errors=[],screens=[];let browser,p,passed=false;
const server=spawn(process.execPath,['server.mjs','0'],{cwd:root,stdio:['ignore','pipe','pipe'],windowsHide:true});
const read=()=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
const screenshot=async name=>{await p.waitForTimeout(120);await p.screenshot({path:resolve(output,name+'.png')});screens.push(name);};
const start=async()=>{await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.locator('.workshop-launch').waitFor();await p.waitForTimeout(650);};
const jump=async at=>{await p.evaluate(({at,base})=>{window.__workEOffset=at-base;sessionStorage.setItem('work-e-clock',String(window.__workEOffset));},{at,base:now});};
const closePanels=async()=>{while(await p.locator('.screen-panel .close').count())await p.locator('.screen-panel .close').first().click();};
const ok=async()=>{await p.locator('[data-yes]').click();};
async function orders(){await closePanels();await p.getByRole('button',{name:'生意',exact:true}).click();await p.locator('[data-trade-view="business"]').first().click();await p.locator('[data-trade-view="orders"]').click();await p.locator('.orders-screen').waitFor();}
async function fit(){await p.evaluate(()=>new Promise(done=>requestAnimationFrame(()=>requestAnimationFrame(done))));const r=await p.locator('.orders-screen').evaluate(el=>{const a=el.getBoundingClientRect(),g=document.querySelector('#game').getBoundingClientRect(),b=el.querySelector('.orders-scroll');return {inside:a.left>=g.left-1&&a.right<=g.right+1&&a.top>=g.top-1&&a.bottom<=g.bottom+1,overflow:b.scrollWidth-b.clientWidth};});assert.ok(r.inside);assert.ok(r.overflow<2);}
async function cookAndHarvest(){await closePanels();await p.locator('[data-control-id="tool:0"]').click();await p.locator('.cooking-dialog').waitFor();await ok();const s=await read();await jump(s.batch.ends+1000);await p.waitForTimeout(3300);for(let i=0;i<24;i++)await p.locator(`[data-control-id="egg:${i}"]`).press('Enter');assert.equal((await read()).batch.eggs.every(e=>e.collected),true);}
const projects=async()=>{await closePanels();await p.getByRole('button',{name:'生意',exact:true}).click();await p.locator('[data-trade-view="business"]').first().click();await p.locator('[data-trade-view="projects"]').first().click();await p.locator('.projects-screen').waitFor();};
async function fitProjects(){await p.evaluate(()=>new Promise(done=>requestAnimationFrame(()=>requestAnimationFrame(done))));const r=await p.locator('.projects-screen').evaluate(el=>{const a=el.getBoundingClientRect(),g=document.querySelector('#game').getBoundingClientRect(),b=el.querySelector('.projects-scroll');return {inside:a.left>=g.left-1&&a.right<=g.right+1&&a.top>=g.top-1&&a.bottom<=g.bottom+1,overflow:b.scrollWidth-b.clientWidth};});assert.ok(r.inside);assert.ok(r.overflow<2);}
const complete=async stageId=>{await p.locator(`[data-project-complete="${stageId}"]`).click();await ok();};
try{
  const base=await new Promise((done,fail)=>{const timer=setTimeout(()=>fail(Error('server start timeout')),10000);server.once('error',fail);server.stdout.on('data',chunk=>{const m=String(chunk).match(/http:\/\/127\.0\.0\.1:\d+/);if(m){clearTimeout(timer);done(m[0]);}});});
  browser=await chromium.launch({headless:true,...(process.env.CHICK_QA_CHROME?{executablePath:process.env.CHICK_QA_CHROME}:{})});
  const context=await browser.newContext({viewport:{width:390,height:844},reducedMotion:'reduce'});
  await context.addInitScript(({seed,now})=>{if(location.protocol==='about:')return;window.__workEOffset=Number(sessionStorage.getItem('work-e-clock')??0);Date.now=()=>now+window.__workEOffset+Math.floor(performance.now());if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));},{seed,now});
  p=await context.newPage();p.on('pageerror',e=>errors.push(e.message));await p.goto(base+'/');await start();

  await projects();let text=await p.locator('.projects-screen').innerText();assert.match(text,/我的小店招牌册/);assert.match(text,/溪岸风味篮/);await fitProjects();await screenshot('projects-list-390');
  await p.locator('[data-project-open="PJ-1"]').click();await screenshot('pj1-detail');
  let s=await read();const cp0=s.cp;
  await complete('PJ-1-A');await complete('PJ-1-B');s=await read();assert.equal(s.cp,cp0,'check stages cost nothing');
  await complete('PJ-1-C');s=await read();assert.equal(s.cp,cp0-200);assert.equal(s.expansion.projects['PJ-1'].stages['PJ-1-C'].complete,true);
  assert.match(await p.locator('.projects-screen').innerText(),/菜单预设/);await screenshot('pj1-done');
  await p.reload();await start();s=await read();assert.equal(s.cp,cp0-200);assert.equal(s.expansion.projects['PJ-1'].payments['PJ-1-C'],200);
  checks.push('招牌册：两个检查阶段直接登记不花CP，第三阶段确认支付200CP；刷新后阶段与余额不变');

  await closePanels();await p.getByRole('button',{name:'生意',exact:true}).click();await p.locator('[data-trade-view="business"]').first().click();await p.locator('.business-screen').waitFor();
  if(await p.locator('[data-business-filter="all"]').count())await p.locator('[data-business-filter="all"]').click();
  for(const key of ['0:0','0:3']){await p.locator(`[data-business-quantity="${key}"]`).fill('6');await p.locator(`[data-business-quantity="${key}"]`).press('Tab');}
  await p.locator('[data-business-preset-save="0"]').click();s=await read();assert.deepEqual(s.expansion.menus.presets,[{menuId:'MN1',stock:{'0:0':6,'0:3':6}}]);assert.equal(s.farm['0:3'],20,'saving a preset reserves nothing');
  await p.locator('[data-business-quantity="0:0"]').fill('0');await p.locator('[data-business-quantity="0:0"]').press('Tab');
  await p.locator('[data-business-preset-load="0"]').click();assert.equal(await p.locator('[data-business-quantity="0:0"]').inputValue(),'6');await screenshot('business-preset');
  checks.push('完成招牌册后营业准备出现“菜单预设”：保存当前备货为预设1、清空后一键载入，均不占用库存');

  await projects();await p.locator('[data-project-open="PJ-2"]').click();const cp1=(await read()).cp;
  await complete('PJ-2-A');assert.equal((await read()).cp,cp1-100);
  await p.locator('[data-project-deliver="PJ-2-B"]').click();await p.locator('.projects-picks').waitFor();
  for(const key of ['0:3','0:4'])await p.locator(`[data-project-choice="${key}"]`).check();
  for(let i=0;i<6;i++){await p.locator('[data-project-plus="0:3"]').click();await p.locator('[data-project-plus="0:4"]').click();}
  await screenshot('pj2-deliver');await p.locator('[data-project-confirm]').click();await ok();
  s=await read();assert.deepEqual(s.expansion.projects['PJ-2'].deliveries['PJ-2-B'],{'0:3':6,'0:4':6});assert.equal(s.farm['0:3'],14);assert.equal(s.cp,cp1-100,'deliveries pay nothing');
  await complete('PJ-2-B');await complete('PJ-2-C');s=await read();assert.equal(s.cp,cp1-500);assert.ok(s.expansion.methods.full.includes('ALT-R'));
  await screenshot('pj2-done');await p.reload();await start();s=await read();assert.equal(s.cp,cp1-500);assert.ok(s.expansion.methods.full.includes('ALT-R'));
  checks.push('溪岸风味篮：付100→勾选2种各交6只（首次交付锁定、不付货款）→登记→付400；学会ALT-R；刷新后余额与交付记录正确');

  await projects();await p.locator('[data-project-open="PJ-4"]').click();await p.locator('[data-project-portraits]').click();
  const farm=structuredClone((await read()).farm);for(const key of ['0:0','0:1'])await p.locator(`[data-project-portrait="${key}"]`).check({force:true});
  await screenshot('pj4-portraits');await p.locator('[data-project-portraits-save]').click();
  s=await read();assert.deepEqual(s.expansion.projects['PJ-4'].pinnedChoices.portraits,['0:0','0:1']);assert.deepEqual(s.farm,farm);
  for(const [width,height]of [[320,568],[1280,900]]){await p.setViewportSize({width,height});await fitProjects();await screenshot(`projects-${width}`);}
  await p.setViewportSize({width:390,height:844});await closePanels();await p.getByRole('button',{name:'图鉴',exact:true}).click();await p.locator('[data-book-tab="collections"]').click();await p.locator('[data-books-category]').selectOption('papers');
  assert.match(await p.locator('.books-screen').innerText(),/四地风味展 · 展册画像（2）/);await screenshot('books-exhibit');
  checks.push('四地风味展：旧伙伴（含观赏）以永久画像入展册并在图鉴收藏册展示，不消耗库存；320/390/1280无横溢');
  assert.deepEqual(errors,[]);passed=true;await context.close();
}catch(error){if(p&&!p.isClosed()){await writeFile(resolve(output,'failure.txt'),await p.locator('body').innerText());await screenshot('failure');}throw error;}
finally{await browser?.close();server.kill();const report={checkedAt:new Date().toISOString(),passed,checks,screens,errors,isolatedProfile:true,acceleratedClock:true,realDeviceTest:false};await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));}
