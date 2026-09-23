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
const {chromium}=require(packagePath),output=resolve(root,'artifacts/qa/book-navigation');await mkdir(output,{recursive:true});
const now=1800000000000,seed=freshState(now,18);seed.total={'0:0':130,'0:3':1,'0:4':1,'0:8':1,'0:18':1};seed.farm={'0:0':3,'0:3':1};seed.toolLevels[1]=0;seed.progress.sources=earnedSources(seed);seed.progress.tutorialSeen=true;
const checks=[],errors=[],screens=[];let browser,p,passed=false;
const server=spawn(process.execPath,['server.mjs','0'],{cwd:root,stdio:['ignore','pipe','pipe'],windowsHide:true});
const shot=async name=>{await p.screenshot({path:resolve(output,name+'.png')});screens.push(name);};
const read=()=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
const nav=async label=>{await p.getByRole('button',{name:label,exact:true}).click();};
try{
  const base=await new Promise((done,fail)=>{const timer=setTimeout(()=>fail(Error('server start timeout')),10000);server.once('error',fail);server.stdout.on('data',chunk=>{const m=String(chunk).match(/http:\/\/127\.0\.0\.1:\d+/);if(m){clearTimeout(timer);done(m[0]);}});});
  browser=await chromium.launch({headless:true,...(process.env.CHICK_QA_CHROME?{executablePath:process.env.CHICK_QA_CHROME}:{})});
  const context=await browser.newContext({viewport:{width:390,height:844},reducedMotion:'reduce'});
  await context.addInitScript(({seed,now})=>{Date.now=()=>now+Math.floor(performance.now());if(location.protocol!=='about:'&&!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));},{seed,now});
  p=await context.newPage();p.on('pageerror',e=>errors.push(e.message));await p.goto(base+'/');await nav('开始游戏');await p.locator('.workshop-launch').waitFor();await p.waitForTimeout(650);
  await nav('图鉴');assert.deepEqual(await p.locator('[data-book-tab]').allTextContents(),['总览','品种','收藏','见闻']);assert.ok(await p.locator('[data-book-direction]').count()<=2);await shot('overview');
  await p.locator('[data-book-tab="species"]').click();
  const search=async value=>{await p.locator('[data-book-search]').fill(value);await p.locator('[data-book-search-form] button').click();};
  await search('荠菜煎饼鸡');assert.equal(await p.locator('[data-collection-card]').count(),0);assert.ok(!(await p.locator('.collection-screen').innerText()).includes('荠菜煎饼鸡'));
  await search('C129');assert.equal(await p.locator('[data-collection-card]').count(),1);assert.equal(await p.locator('[data-collection-card="128"] .collection-name').innerText(),'未发现');
  await p.locator('[data-collection-card="128"]').click();await p.locator('.regional-screen').waitFor();assert.ok(!(await p.locator('.regional-screen').innerText()).includes('荠菜煎饼鸡'));
  await p.locator('.regional-screen .close').click();await p.locator('.collection-screen').waitFor();assert.equal(await p.locator('[data-book-search]').inputValue(),'C129');
  checks.push('四分页与总览至多两方向；未知真名搜索无结果、稳定编号可查剪影，进入地区记录并原路返回保留搜索');
  await search('C001');await p.locator('[data-collection-card="0"]').click();assert.equal(await p.locator('[data-species-sell],[data-species-quantity]').count(),0);await p.locator('[data-species-inventory]').click();
  assert.equal(await p.locator('[data-harvest-row]').count(),1);assert.equal(await p.locator('[data-harvest-row="0:0"]').count(),1);await shot('inventory-deep-link');
  await nav('图鉴');await p.locator('[data-book-tab="species"]').click();await p.locator('[data-collection-card="0"]').click();const before=await read();await p.locator('[data-species-prepare]').click();await p.locator('[data-control-id="tool:0"]').waitFor();const after=await read();assert.equal(after.cp,before.cp);assert.deepEqual(after.farm,before.farm);assert.deepEqual(after.ingredients,before.ingredients);
  checks.push('品种档案无出售表，管理库存定位单品；准备下一锅只改厨房草稿，不扣CP/材料/伙伴');
  await nav('图鉴');await p.locator('[data-book-tab="collections"]').click();assert.equal(await p.locator('[data-books-tab]').count(),0);assert.equal(await p.locator('[data-books-category] option').count(),5);assert.equal(await p.locator('[data-books-seasonal],[data-books-shrine]').count(),2);
  await p.locator('[data-book-tab="lore"]').click();for(const text of ['尚未找到标本','未辨认','供货未开放','尚未用于料理'])assert.ok((await p.locator('.book-screen').innerText()).includes(text));
  for(const [width,height]of [[320,568],[390,844],[1280,900]]){
    await p.setViewportSize({width,height});for(const tab of ['overview','species','collections','lore']){
      await p.locator(`[data-book-tab="${tab}"]`).click();const b=await p.locator('.screen-panel').evaluate(el=>({overflow:el.scrollWidth-el.clientWidth,tabs:[...el.querySelectorAll('[data-book-tab]')].map(b=>({width:b.getBoundingClientRect().width,height:b.getBoundingClientRect().height}))}));
      assert.ok(b.overflow<=1,`${tab} at ${width} overflows`);assert.ok(b.tabs.every(x=>x.width>=44&&x.height>=44),`${tab} touch targets at ${width}`);
    }await shot('lore-'+width);
  }
  checks.push('收藏五视角使用分类选择，旧回礼入口仍在；见闻食材四态；320/390/1280四分页无横溢、分页触控均≥44px');
  assert.deepEqual(errors,[]);passed=true;await context.close();
}catch(e){if(p&&!p.isClosed()){await writeFile(resolve(output,'failure.txt'),await p.locator('body').innerText());await shot('failure');}throw e;}
finally{await browser?.close();server.kill();const report={checkedAt:new Date().toISOString(),passed,checks,errors,screens,isolatedProfile:true,realDeviceTest:false};await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));}
