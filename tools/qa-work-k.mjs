// Work K playable acceptance: five fixed places at phone/landscape/desktop sizes,
// desktop side column, cross-page returns with drafts, unknown-name scan.
import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import {resolve,join} from 'node:path';
import {fileURLToPath} from 'node:url';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {freshState,char} from '../web/engine.js';
import {syncProgress} from '../web/progression.js';
import {REGIONAL,resolveSpecies} from '../web/content-registry.js';
import {orderMilestone,acceptProposal} from '../web/orders.js';
import {normalizeSave} from '../web/engine.js';
const root=fileURLToPath(new URL('../',import.meta.url)),require=createRequire(import.meta.url);
const candidates=[process.argv[2],process.env.CHICK_PLAYWRIGHT_PACKAGE,'playwright','playwright-core',process.env.APPDATA&&join(process.env.APPDATA,'npm/node_modules/gsd-pi/node_modules/playwright-core')].filter(Boolean);
let packagePath;for(const p of candidates){try{packagePath=require.resolve(p);break;}catch{}}
if(!packagePath)throw Error('An existing Playwright package is required.');
const {chromium}=require(packagePath),output=resolve(root,'artifacts/qa/work-k');await mkdir(output,{recursive:true});
const now=1800000000000,seed=freshState(now,4242);
seed.total=Object.fromEntries(['0:0','0:3','0:8','0:1','0:2','0:10','0:17','0:20','0:5'].map((k,i)=>[k,i?5:300]));
seed.farm={'0:0':40,'0:3':10,'0:8':10,'0:10':2,'0:17':2,'0:20':2};seed.toolLevels[1]=0;seed.toolLevels[2]=0;seed.cp=5000;seed.progress.tutorialSeen=true;seed.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};delete seed.farm['0:8'];syncProgress(seed);
// An accepted O01 (orders open at 240/8) so the order sheet and its detours can be exercised.
Object.assign(seed,normalizeSave(seed,now));orderMilestone(seed,now,'batch');acceptProposal(seed,seed.expansion.orders.proposals.find(x=>x.templateId==='O01').id,{},now);
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
const NAV_ORDER=['厨房','农场','生意','寻访','图鉴'];
const navLabels=()=>p.locator('[data-control-id^="nav:"]').evaluateAll(els=>els.map(e=>e.getAttribute('aria-label')));
async function noOverflow(selector){const r=await p.locator(selector).first().evaluate(el=>{const scroll=[...el.querySelectorAll('*')].filter(x=>x.scrollWidth>x.clientWidth+1&&getComputedStyle(x).overflowX==='visible'&&x.clientWidth>0).length;const a=el.getBoundingClientRect(),g=document.querySelector('#game').getBoundingClientRect();return {inside:a.left>=g.left-1&&a.right<=g.right+1,overflow:el.scrollWidth-el.clientWidth,scroll};});assert.ok(r.inside,`${selector} inside the game`);assert.ok(r.overflow<2,`${selector} has no horizontal overflow`);}
const nav=name=>p.getByRole('button',{name,exact:true}).click();
const hidden=[];
async function scan(where){const html=await p.locator('#game').evaluate(el=>el.innerHTML+[...el.querySelectorAll('[aria-label]')].map(x=>x.getAttribute('aria-label')).join(' '));for(const name of hidden)assert.ok(!html.includes(name),`${where} leaks the unknown species name ${name}`);}
try{
  const base=await new Promise((done,fail)=>{const timer=setTimeout(()=>fail(Error('server start timeout')),10000);server.once('error',fail);server.stdout.on('data',chunk=>{const m=String(chunk).match(/http:\/\/127\.0\.0\.1:\d+/);if(m){clearTimeout(timer);done(m[0]);}});});
  browser=await chromium.launch({headless:true,...(process.env.CHICK_QA_CHROME?{executablePath:process.env.CHICK_QA_CHROME}:{})});
  for(const c of REGIONAL.species)if(!(seed.total[c.key]>0))hidden.push(resolveSpecies(c.key).title_zh_CN);assert.equal(hidden.length,48);

  // 1. The five fixed places at every size; 补给 lives in the kitchen.
  for(const [width,height] of [[320,568],[375,812],[390,844],[430,932],[844,390],[1280,900]]){
    const context=await browser.newContext({viewport:{width,height},reducedMotion:'reduce'});
    await context.addInitScript(({seed,now})=>{if(location.protocol==='about:')return;window.__workEOffset=Number(sessionStorage.getItem('work-e-clock')??0);Date.now=()=>now+window.__workEOffset+Math.floor(performance.now());if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));},{seed,now});
    p=await context.newPage();p.on('pageerror',e=>errors.push(e.message));await p.goto(base+'/');await start();
    assert.deepEqual(await navLabels(),NAV_ORDER);assert.equal(await p.getByRole('button',{name:'商店',exact:true}).count(),0,'no shop tab');
    const sizes=await p.locator('[data-control-id^="nav:"]').evaluateAll(els=>els.map(e=>{const r=e.getBoundingClientRect();return [r.width,r.height];}));
    assert.ok(sizes.every(([w,h])=>w>=44&&h>=44),`nav targets ${JSON.stringify(sizes)}`);
    await screenshot(`kitchen-${width}x${height}`);
    for(const [name,selector] of [['生意','.business-screen'],['寻访','.regional-screen'],['图鉴','.collection-screen, .album-screen, .screen-panel']]){await nav(name);await p.locator(selector).first().waitFor();await noOverflow('.screen-panel');}
    await screenshot(`trade-explore-book-${width}x${height}`);
    if(width===1280){
      await nav('生意');const layout=await p.evaluate(()=>{const n=document.querySelector('#main-nav').getBoundingClientRect(),s=document.querySelector('.screen-panel').getBoundingClientRect(),d=document.querySelector('#desk').getBoundingClientRect();return {navRight:n.right,panelLeft:s.left,panelRight:s.right,panelWidth:s.width,summaryLeft:d.left,summaryWidth:d.width};});
      assert.ok(layout.navRight<=layout.panelLeft&&layout.panelRight<=layout.summaryLeft,'desktop navigation / task / summary are separate columns');assert.ok(layout.panelWidth>=480&&layout.panelWidth<=560);assert.ok(layout.summaryWidth>=240&&layout.summaryWidth<=280);await screenshot('desktop-trade');
      await p.evaluate(()=>{document.documentElement.style.fontSize='24px';window.dispatchEvent(new Event('resize'));});await noOverflow('.screen-panel');assert.equal(await p.locator('#game.is-wide').count(),0,'large text falls back to single column');await screenshot('desktop-large-text');await p.evaluate(()=>{document.documentElement.style.fontSize='';window.dispatchEvent(new Event('resize'));});
      await nav('厨房');assert.match(await p.locator('#desk').innerText(),/今日小厨房/);await screenshot('desktop-desk');
    }
    await nav('厨房');await p.getByRole('button',{name:'补给 · 小卖部',exact:true}).click();await p.locator('.shop-screen').waitFor();await p.locator('.shop-screen .close').click();
    assert.equal(await p.locator('.shop-screen').count(),0);await context.close();
  }
  checks.push('320×568、375、390、430、横屏844×390与桌面1280：底栏固定为厨房/农场/生意/寻访/图鉴五项、无“商店”标签；每页无横向溢出；厨房“补给”一击打开小卖部');
  checks.push('桌面1280：左五导航、中间480–560正文、右240–280摘要；150%字级退回单列；短横屏导航宽高均≥44px');

  // 2. Cross-page returns keep drafts.
  const context=await browser.newContext({viewport:{width:390,height:844},reducedMotion:'reduce'});
  await context.addInitScript(({seed,now})=>{if(location.protocol==='about:')return;window.__workEOffset=Number(sessionStorage.getItem('work-e-clock')??0);Date.now=()=>now+window.__workEOffset+Math.floor(performance.now());if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));},{seed,now});
  p=await context.newPage();p.on('pageerror',e=>errors.push(e.message));await p.goto(base+'/');await start();
  await p.locator('.game-toast').waitFor({timeout:4000});assert.match(await p.locator('.game-toast').innerText(),/补给搬到厨房了/);
  await nav('生意');await p.locator('[data-trade-view="orders"]').first().click();const order=(await read()).expansion.orders.active[0];
  await p.locator(`[data-order-deliver="${order.id}"]`).click();await p.locator('[data-slot-plus="O01-G1|0:0"]').click();await screenshot('order-draft');
  await nav('农场');await nav('生意');await p.locator('[data-orders-confirm]').waitFor();assert.match(await p.locator('[data-orders-confirm]').innerText(),/交付1只/);
  checks.push('生意→订单交付页选1只后切到农场再回生意：直接回到同一交付页，草稿数量保留');
  const recipe=p.locator('[data-order-recipe]').first();await recipe.click();await p.locator('[data-cookbook-back]').waitFor();assert.match(await p.locator('[data-cookbook-back]').innerText(),/返回订单/);await screenshot('order-to-recipe');
  await p.locator('[data-cookbook-back]').click();await p.locator('[data-orders-confirm]').waitFor();assert.match(await p.locator('[data-orders-confirm]').innerText(),/交付1只/);
  checks.push('交付页对缺货品种“去看做法”→配方详情显示“返回订单”→返回原交付页，选择仍在');
  await nav('寻访');await p.locator('.regional-screen').waitFor();await scan('寻访·出发');
  await p.locator('[data-regional-legacy-routes]').click();await p.locator('.workshop-screen').waitFor();assert.match(await p.locator('.workshop-screen').innerText(),/庭院|水边|林间/);await screenshot('explore-legacy-routes');
  await p.locator('.workshop-screen .close').click();await p.locator('.regional-screen').waitFor();
  checks.push('寻访页常驻“原路线”入口：打开原来的三条路线，关闭后回到寻访页而不是厨房');
  await p.locator('[data-regional-tab="record"]').click();await scan('寻访·地区与发现');
  for(const tab of ['business','orders','regulars','projects']){await nav('生意');await p.locator(`[data-trade-view="${tab}"]`).first().click();await p.waitForTimeout(150);await scan(`生意·${tab}`);}
  await nav('图鉴');await p.locator('[data-book-tab=collections]').click();for(const tab of ['theme','region','special','mementos','papers']){await p.locator('[data-books-category]').selectOption(tab);await scan(`图鉴·收藏册·${tab}`);}
  checks.push(`未知投影扫描：寻访两页、生意四页、图鉴收藏册五页的DOM与aria中，48个未收录新品名称均未出现`);
  await nav('农场');const shrine=p.locator('[data-control-id="farm:shrine"]');
  for(let i=0;i<4&&!(await shrine.count());i++){const g=await p.locator('#game').boundingBox();await p.mouse.move(g.x+g.width*.85,g.y+g.height*.55);await p.mouse.down();await p.mouse.move(g.x+g.width*.15,g.y+g.height*.55,{steps:10});await p.mouse.up();await p.waitForTimeout(200);}
  await shrine.click();await p.locator('.screen-panel').first().waitFor();await screenshot('farm-shrine');checks.push('旧神社委托簿仍从农场原位置（横向拖动农场后）进入');
  await nav('厨房');await p.locator('[data-control-id="nav:5"]').focus();await p.keyboard.press('Enter');await p.locator('.screen-panel').first().waitFor();checks.push('键盘聚焦“生意”并回车即进入生意页');
  await p.reload();await start();assert.equal(await p.locator('.game-toast').count(),0,'the supply notice shows once');
  checks.push('“补给搬到厨房了”只提示一次，刷新后不再出现');
  assert.deepEqual(errors,[]);passed=true;await context.close();
}catch(error){if(p&&!p.isClosed()){await writeFile(resolve(output,'failure.txt'),await p.locator('body').innerText());await screenshot('failure');}throw error;}
finally{await browser?.close();server.kill();const report={checkedAt:new Date().toISOString(),passed,checks,screens,errors,isolatedProfile:true,acceleratedClock:true,realDeviceTest:false};await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));}
