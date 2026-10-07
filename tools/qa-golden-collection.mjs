// Isolated browser proof for the collection golden sample, never player saves.
import {createRequire} from 'node:module';
import {spawn} from 'node:child_process';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {freshState,normalizeSave} from '../web/engine.js';
import {syncProgress} from '../web/progression.js';
import {reconcileEntitlements,grantEntitlementOnce} from '../web/collection-progress.js';
import {resolveSpecies,REGIONAL} from '../web/content-registry.js';
const require=createRequire(import.meta.url);
const {chromium}=require(process.env.CHICK_PLAYWRIGHT_PACKAGE??process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const out=process.env.GOLDEN_QA_OUT??'artifacts/golden-collection';await mkdir(out,{recursive:true});
const now=Date.now(),seed=freshState(now,73);seed.cp=3923;seed.duck=true;seed.progress.tutorialSeen=true;
seed.total={'0:0':24,'0:3':2,'0:4':2,'1:0':2};seed.farm={'0:0':8,'0:3':2,'0:4':2,'1:0':2};
syncProgress(seed);reconcileEntitlements(seed);const state=normalizeSave(seed,now);
await writeFile(out+'/demo-state.json',JSON.stringify(state,null,2));
const server=spawn(process.execPath,['server.mjs','0'],{stdio:['ignore','pipe','pipe'],windowsHide:true});
const base=await new Promise((yes,no)=>{server.on('error',no);server.stdout.on('data',b=>{const m=String(b).match(/http:\/\/127\.0\.0\.1:\d+/);if(m)yes(m[0]);});});
let browser,page,passed=false;const errors=[],checks=[],screens=[];
const save=()=>page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
const shot=async name=>{await page.evaluate(()=>document.fonts.ready);await page.waitForTimeout(150);await page.screenshot({path:out+'/'+name+'.png'});screens.push(name);};
async function open(state,width=390,height=780){
  const context=await browser.newContext({viewport:{width,height}});
  await context.addInitScript(s=>{if(location.protocol==='http:'&&!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(s));},state);
  page=await context.newPage();page.on('pageerror',e=>errors.push(e.message));await page.goto(base);
  await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.waitForTimeout(650);
  await page.locator('#main-nav').getByRole('button',{name:'图鉴',exact:true}).click();await page.locator('[data-book-tab="collections"]').click();await page.locator('.golden-book').waitFor();
  return context;
}
try{
  browser=await chromium.launch({headless:true,...(process.env.CHICK_QA_CHROME?{executablePath:process.env.CHICK_QA_CHROME}:{})});
  const context=await open(state);await shot('browser-390x780');
  const assertPinVisible=async()=>{
    const visible=await page.locator('.gs-pin img').evaluate(el=>{
      const pin=el.getBoundingClientRect(),clip=document.querySelector('.gs-paper-scroll').getBoundingClientRect();
      return pin.top>=clip.top&&pin.bottom<=clip.bottom&&pin.left>=clip.left&&pin.right<=clip.right;
    });
    assert.ok(visible,'The complete pin and ribbon must fit inside the paper scroll viewport');
  };
  await assertPinVisible();
  const landmarks=await page.evaluate(()=>{
    const rect=el=>{const r=el.getBoundingClientRect();return [r.x,r.y,r.width,r.height].map(n=>+n.toFixed(2));};
    const q=s=>rect(document.querySelector(s));
    return {viewport:[innerWidth,innerHeight],title:q('.gs-header h1'),tabs:q('.gs-bookmarks'),paper:q('.gs-paper'),theme:q('.gs-page-heading h3'),
      portraits:[...document.querySelectorAll('.gs-grid .gs-sticker-ink')].map(rect),names:[...document.querySelectorAll('.gs-grid .gs-character-name')].map(rect),
      unknown:q('.gs-unknown'),stamps:q('.gs-stamps'),pager:q('.gs-pager'),nav:q('#main-nav'),
      displayFontLoaded:document.fonts.check('30px "Golden Display"'),borderImage:getComputedStyle(document.querySelector('.gs-paper')).borderImageSource};
  });
  assert.equal(landmarks.displayFontLoaded,true);await writeFile(out+'/landmarks.json',JSON.stringify(landmarks,null,2));
  const current=page.locator('#main-nav [aria-current=page]');
  for(const mode of ['idle','hover','focus']){
    if(mode==='hover')await current.hover();if(mode==='focus')await current.focus();
    const skin=await current.evaluate(b=>{const s=getComputedStyle(b);return {background:s.backgroundColor,shadow:s.boxShadow,border:s.borderBottomWidth};});
    assert.equal(skin.background,'rgba(0, 0, 0, 0)');assert.equal(skin.shadow,'none');assert.equal(skin.border,'0px');
  }
  await current.hover();await page.mouse.down();
  assert.equal(await current.evaluate(b=>getComputedStyle(b).backgroundColor),'rgba(0, 0, 0, 0)');
  await page.mouse.move(0,0);
  await page.mouse.up();
  assert.equal(await page.locator('.gs-character').count(),4);assert.equal(await page.locator('.gs-unknown').count(),2);
  const before=await save();
  await page.locator('[data-gs-stamp=collection]').click();assert.match(await page.locator('.gs-dialog').innerText(),/已入册/);await page.locator('[data-gs-close]').click();
  await page.locator('[data-gs-stamp=practice]').click();assert.match(await page.locator('.gs-dialog').innerText(),/尚未入册/);await shot('practice-pending');await page.locator('[data-gs-close]').click();
  await page.locator('[data-gs-unknown]').first().click();assert.ok(!(await page.locator('.gs-dialog').innerText()).includes(resolveSpecies('0:128').title_zh_CN));await page.locator('[data-gs-close]').click();
  await page.locator('[data-books-pin]').click();assert.equal(await page.locator('[data-books-pin]').getAttribute('aria-pressed'),'true');await shot('pinned');
  await assertPinVisible();
  await page.locator('[data-gs-species="0:0"]').click();await page.locator('[data-species-back]').click();await page.locator('.golden-book').waitFor();
  await page.locator('[data-gs-next]').click();assert.match(await page.locator('.gs-pager').innerText(),/2 \/ 8/);await page.locator('[data-gs-prev]').click();
  await page.locator('[data-gs-directory]').click();assert.equal(await page.locator('[data-books-category] option').count(),5);await page.locator('[data-books-category]').selectOption('mementos');await page.locator('[data-book-tab=collections]').click();
  const after=await save();for(const k of ['cp','total','farm','ingredients'])assert.deepEqual(after[k],before[k]);assert.deepEqual(after.expansion.collections,before.expansion.collections);
  checks.push('four existing characters, two non-leaking unknowns, separate stamp facts, pin, profile return, theme pagination, category reachability, no rewards from reading');
  for(const [w,h]of [[320,568],[390,844],[430,932],[844,390],[1280,900]]){
    await page.setViewportSize({width:w,height:h});await shot(`browser-${w}x${h}`);
    const metrics=await page.locator('.golden-book').evaluate(el=>({overflow:el.scrollWidth-el.clientWidth,images:[...el.querySelectorAll('img')].filter(i=>!i.complete||!i.naturalWidth).map(i=>i.src),nav:[...document.querySelectorAll('#main-nav button')].map(b=>({width:b.getBoundingClientRect().width,height:b.getBoundingClientRect().height})),selected:getComputedStyle(document.querySelector('#main-nav [aria-current=page]')).boxShadow}));
    assert.ok(metrics.overflow<=1,JSON.stringify({w,metrics}));assert.deepEqual(metrics.images,[]);assert.ok(metrics.nav.every(r=>r.width>=44&&r.height>=44));assert.equal(metrics.selected,'none');
    if(w===320){
      await page.locator('[data-gs-next]').scrollIntoViewIfNeeded();
      const visible=await page.locator('[data-gs-next]').evaluate(el=>{const r=el.getBoundingClientRect(),nav=document.querySelector('#main-nav').getBoundingClientRect();return r.bottom<=nav.top&&r.top>=0;});
      assert.ok(visible,'Short-screen pager must be reachable above the bottom nav');await shot('browser-320-scrolled');
      await page.locator('.gs-paper-scroll').evaluate(el=>el.scrollTop=0);
    }
  }
  checks.push('320/390/430/landscape/desktop: no horizontal overflow, assets loaded, nav targets >=44, no tab underline');
  await page.setViewportSize({width:390,height:780});await page.emulateMedia({reducedMotion:'reduce'});await page.locator('[data-gs-help]').click();await page.locator('[data-gs-close]').click();
  assert.deepEqual(errors,[]);await context.close();
  const empty=await open(freshState(now,74),320,568);assert.equal(await page.locator('.gs-character').count(),0);assert.equal(await page.locator('.gs-unknown').count(),6);await shot('empty-320');
  await page.setViewportSize({width:390,height:780});assert.equal(await page.locator('.gs-stamp.is-pending').count(),2);
  assert.match(await page.locator('[data-gs-stamp=collection] img').getAttribute('src'),/v2-collection-inactive/);await shot('empty-390');await empty.close();
  const completedState=structuredClone(state);grantEntitlementOnce(completedState,'STAMP-COL-1','COL-1');
  const completed=await open(completedState);assert.equal(await page.locator('.gs-stamp.is-done').count(),2);
  assert.match(await page.locator('[data-gs-stamp=practice] img').getAttribute('src'),/v2-practice-active/);await shot('stamps-completed');await completed.close();
  const fullState=structuredClone(state);for(const key of REGIONAL.collections.find(c=>c.id==='COL-1').optional.allowed.slice(0,6))fullState.total[key]=Math.max(fullState.total[key]??0,3);
  syncProgress(fullState);reconcileEntitlements(fullState);const full=await open(fullState);assert.equal(await page.locator('.gs-grid .gs-character').count(),6);
  const separated=await page.evaluate(()=>{const names=[...document.querySelectorAll('.gs-grid .gs-character-name')];return Math.max(...names.map(n=>n.getBoundingClientRect().bottom))<document.querySelector('.gs-stamps').getBoundingClientRect().top;});
  assert.ok(separated,'Six known characters must retain name-to-stamp spacing');await shot('six-known');await full.close();
  checks.push('all four stamp assets render from state; display font loaded; idle/hover/focus nav has no tile or underline; empty collection exposes no species identity; reduced-motion help flow');passed=true;
}finally{await browser?.close();server.kill();await writeFile(out+'/browser-report.json',JSON.stringify({passed,checkedAt:new Date().toISOString(),physicalDevice:false,isolatedDemoState:true,checks,errors,screens},null,2));console.log(JSON.stringify({passed,checks,errors,screens},null,2));}
