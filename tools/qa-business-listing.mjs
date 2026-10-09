// Isolated browser contract: owned birds can be stocked through the real UI.
import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import {join} from 'node:path';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
const require=createRequire(import.meta.url);
const {chromium}=require(process.env.CHICK_PLAYWRIGHT_PACKAGE??join(process.env.APPDATA,'npm/node_modules/gsd-pi/node_modules/playwright-core'));
const out='artifacts/qa/business-listing';await mkdir(out,{recursive:true});
const server=spawn(process.execPath,['server.mjs','0'],{stdio:['ignore','pipe','pipe'],windowsHide:true});
let browser;const checks=[],errors=[];
try{
  const base=await new Promise((ok,bad)=>{server.once('error',bad);server.stdout.on('data',b=>{const m=String(b).match(/http:\/\/127\.0\.0\.1:\d+/);if(m)ok(m[0]);});});
  browser=await chromium.launch({headless:true,executablePath:process.env.CHICK_QA_CHROME??'C:/Program Files/Google/Chrome/Application/chrome.exe'});
  const page=await browser.newPage({viewport:{width:390,height:844}});page.on('pageerror',e=>errors.push(e.message));
  await page.route('**/web/listing-harness.html',r=>r.fulfill({contentType:'text/html',body:'<!doctype html><meta name="viewport" content="width=device-width,initial-scale=1">'+['style','game-screens','business','game-visual-system','business-golden','business-refinement'].map(x=>`<link rel="stylesheet" href="/web/${x}.css">`).join('')+'<main id="viewport"><div id="game"><div id="panels"></div></div></main>'}));
  await page.goto(base+'/web/listing-harness.html');
  await page.evaluate(async()=>{
    const [{freshState},{createBusinessUI},{RULES}]=await Promise.all([import('/web/engine.js'),import('/web/business-ui.js'),import('/web/integration-data.js')]);
    const panels=document.querySelector('#panels');window.qaMessages=[];
    window.resetListing=(farm)=>{
      window.qaState=freshState(1800000000000,3);qaState.farm=farm;qaState.total={'0:0':3000,'0:3':2,'0:21':2,...Object.fromEntries(Object.keys(farm).map(k=>[k,3000]))};
      for(const o of RULES.storyOrders)qaState.progress.orders[o.id]={...(qaState.progress.orders[o.id]??{}),completed:true};
      localStorage.setItem('chick-kitchen-ui-v1',JSON.stringify({businessMenu:'MN1'}));
      window.qaUI=createBusinessUI({getState:()=>qaState,getNow:()=>1800000000000,panels,
        showPanel:(title,html,cls)=>{panels.innerHTML=`<section class="panel paper ${cls}">${html}</section>`;},
        commitProgress:fn=>{const draft=structuredClone(qaState);const result=fn(draft);qaState=draft;return result;},
        confirmBox:(text,fn)=>{qaMessages.push(text);window.qaConfirm=fn;},alertBox:text=>qaMessages.push(text),
        characterPortrait:(egg,id)=>`<span>${egg}:${id}</span>`,openGoal:g=>qaMessages.push(g),goKitchen:()=>qaMessages.push('kitchen')});
      qaUI.open();
    };resetListing({'0:0':30,'0:21':20});
  });
  const stock=async()=>{await page.locator('.bh-sign[data-business-sheet="stock"]').click();};
  const close=()=>page.locator('[data-business-sheet-close]').click();
  await stock();await page.locator('[data-business-clear]').click();await close();
  assert.equal(await page.locator('.bh-actions .gd-btn').innerText(),'摆货');
  assert.equal(await page.locator('.bh-bonus').innerText(),'★★★凑齐 +25%');
  checks.push('cleared stock stays empty on home');
  await page.locator('[data-business-slot="0:21"]').click();
  assert.equal(await page.locator('[data-business-basket-range]').count(),1);
  const set=async n=>page.locator('[data-business-basket-range]').evaluate((el,n)=>{el.value=String(n);el.dispatchEvent(new Event('change',{bubbles:true}));},n);
  await set(6);await page.locator('[data-business-basket-done]').click();
  await page.locator('[data-business-slot="0:0"]').click();await set(1);await page.locator('[data-business-basket-done]').click();
  assert.equal(await page.locator('[data-business-slot="0:0"] .bh-basket-tag').innerText(),'1/6');
  await page.locator('[data-business-slot="0:0"]').click();await set(6);await page.locator('[data-business-basket-done]').click();
  assert.match(await page.locator('.bh-bonus').innerText(),/已凑齐/);
  checks.push('owned alternative opens stock editor; partial quantity matches shelf; complete combination detected');
  await page.locator('[data-business-open]').click();await page.evaluate(()=>qaConfirm());
  assert.deepEqual(await page.evaluate(()=>qaState.expansion.business.active.stock),{'0:21':6,'0:0':6});
  assert.deepEqual(await page.evaluate(()=>qaState.farm),{'0:0':30,'0:21':20});
  checks.push('opening reserves exactly the selected quantities');
  await page.evaluate(()=>resetListing({'0:0':1}));
  await page.locator('[data-business-sheet="menu"]').click();
  const menu=page.locator('[data-business-pick-menu="MN1"]');assert.match(await menu.innerText(),/暂无可摆伙伴/);assert.equal(await menu.getAttribute('aria-disabled'),null);
  await menu.click();await stock();const only=page.locator('[data-business-stock-key="0:0"]');
  assert.equal(await only.isDisabled(),true);assert.match(await only.innerText(),/在家留 1 只/);
  checks.push('unlocked menu is selectable without spare stock; last copy explains its home lock');
  await close();await page.screenshot({path:join(out,'only-copy.png')});
  assert.deepEqual(errors,[]);await writeFile(join(out,'report.json'),JSON.stringify({passed:true,checks,errors},null,2));
  console.log(JSON.stringify({passed:true,checks,errors},null,2));
}finally{await browser?.close();server.kill();}
