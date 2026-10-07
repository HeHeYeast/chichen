// Real app / isolated browser saves. Screenshots are runtime, not physical-device proof.
import {createRequire} from 'node:module';
import {spawn} from 'node:child_process';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {freshState,normalizeSave} from '../web/engine.js';
import {syncProgress} from '../web/progression.js';
import {openBusiness,advanceBusiness} from '../web/business.js';
const require=createRequire(import.meta.url);
const {chromium}=require(process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const out=process.env.BUSINESS_QA_OUT??'artifacts/golden-business';await mkdir(out,{recursive:true});
const now=Date.now(),seed=freshState(now,83);
seed.cp=3905;seed.duck=true;seed.progress.tutorialSeen=true;
seed.total={'0:0':240,'0:3':40,'0:4':40,'1:0':40,'0:8':40,'0:10':40};
seed.farm={'0:0':30,'0:3':30,'0:4':30,'1:0':30,'0:8':30,'0:10':30};
seed.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};
syncProgress(seed);const prepared=normalizeSave(seed,now),state=structuredClone(prepared);
openBusiness(state,{menuId:'MN1',stock:{'0:0':9,'0:3':4,'1:0':5}},now-2*3600000);
advanceBusiness(state,now);
await writeFile(out+'/demo-state.json',JSON.stringify(state,null,2));
const server=spawn(process.execPath,['server.mjs','0'],{stdio:['ignore','pipe','pipe'],windowsHide:true});
const base=await new Promise((yes,no)=>{server.on('error',no);server.stdout.on('data',b=>{const m=String(b).match(/http:\/\/127\.0\.0\.1:\d+/);if(m)yes(m[0]);});});
let browser,page,passed=false;const checks=[],errors=[],screens=[];
const save=()=>page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
const shot=async name=>{await page.evaluate(()=>document.fonts.ready);await page.waitForTimeout(180);await page.screenshot({path:out+'/'+name+'.png'});screens.push(name);};
async function open(s,width=390,height=780){
  const context=await browser.newContext({viewport:{width,height}});
  await context.addInitScript(s=>{if(location.protocol==='http:'&&!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(s));},s);
  page=await context.newPage();page.on('pageerror',e=>errors.push(e.message));await page.goto(base);
  await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.waitForTimeout(500);
  await page.locator('#main-nav').getByRole('button',{name:'生意',exact:true}).click();await page.locator('.golden-business').waitFor();
  return context;
}
try{
  browser=await chromium.launch({headless:true});
  const context=await open(state);await shot('active-390x780');
  assert.equal(await page.locator('[data-business-stock-key]').count(),3);
  const actual=await page.locator('[data-business-stock-key]').evaluateAll(items=>Object.fromEntries(items.map(el=>[el.dataset.businessStockKey,el.innerText])));
  for(const [key,quantity] of Object.entries(state.expansion.business.active.stock))if(quantity>0)assert.match(actual[key],new RegExp('×'+quantity));
  checks.push('real active stock supplies both character identities and remaining quantities');
  if(!process.argv.includes('--capture-only')){
    const before=await save();await page.locator('.bs-main-action').click();assert.match(await page.locator('.business-receipt-head').innerText(),/6/);await shot('current-bill');await page.locator('[data-business-sheet-close]').click();
    await page.locator('.game-help').click();assert.match(await page.locator('.bs-dialog').innerText(),/2小时/);await page.locator('[data-business-sheet-close]').click();
    await page.locator('.bs-menu').click();assert.equal(await page.locator('[data-business-menu]').count(),0);await page.locator('[data-business-sheet-close]').click();
    const after=await save();for(const key of ['farm','total','cp'])assert.deepEqual(after[key],before[key]);
    checks.push('bill/menu/help read-only; active menu cannot rewrite the current session');
    for(const [width,height]of [[320,568],[390,844],[430,932],[844,390],[1280,900]]){
      await page.setViewportSize({width,height});await shot(`active-${width}x${height}`);
      const metrics=await page.locator('.golden-business').evaluate(el=>({overflow:el.scrollWidth-el.clientWidth,missing:[...el.querySelectorAll('img')].filter(i=>!i.complete||!i.naturalWidth).map(i=>i.src)}));
      assert.ok(metrics.overflow<=1,JSON.stringify({width,metrics}));assert.deepEqual(metrics.missing,[]);
      await page.locator('.bs-main-action').scrollIntoViewIfNeeded();assert.ok(await page.locator('.bs-main-action').isVisible());
    }
    checks.push('320/390/430/landscape/desktop retain reachable actions and all raster assets');
    await page.setViewportSize({width:390,height:780});
    await page.locator('[data-business-close]').click();await page.locator('[data-no]').click();assert.ok((await save()).expansion.business.active);
    await page.locator('[data-business-close]').click();await page.locator('[data-yes]').click();await page.locator('.business-receipt-head').waitFor();
    const closed=await save();assert.equal(closed.expansion.business.active,null);assert.equal(closed.expansion.business.lastReport.totalSold,6);
    await page.locator('[data-business-again]').click();await page.locator('[data-business-tab="report"]').click();assert.equal((await save()).cp,closed.cp);
    checks.push('early-close cancellation leaves session active; confirmed close and reopened receipt settle only once');
    await context.close();

    const draftState=structuredClone(prepared);draftState.farm['1:0']=3;
    const draftContext=await open(draftState);await shot('prepare-empty');
    assert.equal(await page.locator('[data-business-stock-key]').count(),0);
    await page.locator('.bs-menu').click();await page.locator('[data-business-menu][aria-disabled=true]').last().click({force:true});
    await page.locator('#dialog-layer [data-yes]').click();assert.equal(await page.locator('.bs-dialog').evaluate(e=>e.open),false);
    await page.locator('.bs-vessel').click();await page.locator('[data-business-filter="all"]').click();
    for(const [key,n]of [['0:0',6],['0:3',4],['1:0',999]]){
      await page.locator(`[data-business-quantity="${key}"]`).fill(String(n));await page.locator(`[data-business-quantity="${key}"]`).press('Tab');
    }
    assert.equal(await page.locator('[data-business-quantity="1:0"]').inputValue(),'2','Keep-one still caps the draft');
    await page.locator('[data-business-quantity="0:0"]').fill('999');await page.locator('[data-business-quantity="0:0"]').press('Tab');
    assert.equal(await page.locator('[data-business-quantity="0:0"]').inputValue(),'18','Capacity caps the whole draft');
    await page.locator('[data-business-quantity="0:0"]').fill('6');await page.locator('[data-business-quantity="0:0"]').press('Tab');
    await shot('stock-details');await page.locator('[data-business-sheet-close]').click();await shot('prepared-three');
    assert.deepEqual((await save()).farm,draftState.farm);assert.equal((await save()).expansion.business.active,null);
    await page.locator('[data-business-open]').click();await page.locator('[data-no]').click();assert.equal((await save()).expansion.business.active,null);
    await page.locator('[data-business-open]').click();await page.locator('[data-yes]').click();
    assert.deepEqual((await save()).expansion.business.active.stock,{'0:0':6,'0:3':4,'1:0':2});
    assert.deepEqual((await save()).farm,draftState.farm);assert.equal(await page.locator('[data-business-stock-key]').count(),3);
    checks.push('draft basket editing, locked menu feedback, keep-one and capacity, open cancellation, exact confirmed reservation without removing farm stock');
    for(const [id,screen]of [['orders','.orders-screen'],['regulars','.regulars-screen'],['projects','.projects-screen']]){
      await page.locator(`.bs-props [data-trade-view="${id}"]`).click();await page.locator(screen).waitFor();
      await page.locator('[data-trade-view="business"]').click();await page.locator('.golden-business').waitFor();
    }
    checks.push('three shop props lead to the existing orders/regulars/projects screens and return');
    const current=page.locator('#main-nav [aria-current=page]');
    for(const phase of ['idle','hover','focus']){
      if(phase==='hover')await current.hover();if(phase==='focus')await current.focus();
      const css=await current.evaluate(el=>{const c=getComputedStyle(el);return [c.backgroundColor,c.boxShadow,c.borderBottomWidth];});
      assert.deepEqual(css,['rgba(0, 0, 0, 0)','none','0px']);
    }
    await draftContext.close();
    const six=structuredClone(prepared);openBusiness(six,{menuId:'MN1',stock:Object.fromEntries(Object.keys(six.farm).map(key=>[key,2]))},now);
    const sixContext=await open(six,320,568);assert.equal(await page.locator('[data-business-stock-key]').count(),6);await shot('six-stock-320');
    await page.locator('.bs-main-action').scrollIntoViewIfNeeded();await shot('six-stock-320-scrolled');
    assert.ok(await page.locator('.bs-main-action').evaluate(el=>el.getBoundingClientRect().bottom<=document.querySelector('#main-nav').getBoundingClientRect().top));await sixContext.close();
    const sold=structuredClone(state);advanceBusiness(sold,now+2*3600000);
    const soldContext=await open(sold);assert.equal(await page.locator('[data-business-stock-key="1:0"]').count(),0);await shot('sold-out-species-removed');await soldContext.close();
    const long=structuredClone(prepared);for(const key of ['0:18','0:75','1:55']){long.total[key]=10;long.farm[key]=10;}syncProgress(long);
    openBusiness(long,{menuId:'MN1',stock:{'0:18':2,'0:75':2,'1:55':2,'0:0':2}},now);
    const longContext=await open(long,320,568);await shot('long-names-320');
    assert.ok(await page.locator('.long-name').evaluateAll(plates=>plates.every(p=>[...p.children].every(c=>{const a=p.getBoundingClientRect(),b=c.getBoundingClientRect();return b.left>=a.left-1&&b.right<=a.right+1&&b.top>=a.top-1&&b.bottom<=a.bottom+1;}))));await longContext.close();
    const lockedState=freshState(now,84);lockedState.progress.tutorialSeen=true;
    const lockedContext=await open(lockedState,320,568);assert.equal(await page.locator('[data-business-stock-key]').count(),0);
    await page.locator('.game-help').click();assert.match(await page.locator('.bs-dialog').innerText(),/72只/);await page.locator('[data-business-sheet-close]').click();await shot('locked-320');await lockedContext.close();
    checks.push('six actual species scroll without shrinking; six/eight-character names fit at320; sold-out species removed; locked/empty never invent stock; nav idle/hover/focus never rectangular');
  }
  await context.close();assert.deepEqual(errors,[]);passed=true;
}finally{
  await browser?.close();server.kill();await writeFile(out+'/browser-report.json',JSON.stringify({passed,checkedAt:new Date().toISOString(),physicalDevice:false,isolatedDemoState:true,checks,errors,screens},null,2));console.log(JSON.stringify({passed,checks,errors,screens},null,2));
}
