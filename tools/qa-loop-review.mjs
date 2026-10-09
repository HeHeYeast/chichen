import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import {join} from 'node:path';
import {mkdir,writeFile,readFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
const require=createRequire(import.meta.url);
const {chromium}=require(process.env.CHICK_PLAYWRIGHT_PACKAGE??join(process.env.APPDATA,'npm/node_modules/gsd-pi/node_modules/playwright-core'));
const out='artifacts/qa/loop-review';await mkdir(out,{recursive:true});
const server=spawn(process.execPath,['server.mjs','0'],{stdio:['ignore','pipe','pipe'],windowsHide:true});
let browser;const errors=[],checks=[];
try{
 const base=await new Promise((ok,bad)=>{server.once('error',bad);server.stdout.on('data',b=>{const m=String(b).match(/http:\/\/127\.0\.0\.1:\d+/);if(m)ok(m[0]);});});
 browser=await chromium.launch({headless:true,executablePath:process.env.CHICK_QA_CHROME??'C:/Program Files/Google/Chrome/Application/chrome.exe'});
 const page=await browser.newPage({viewport:{width:1080,height:1130}});page.on('pageerror',e=>errors.push(e.message));
 await page.goto(base+'/web/prototypes/loop-review-20261009/index.html');
 await page.locator('#layout-a .scene-full').waitFor();await page.evaluate(()=>document.fonts.ready);
 await page.locator('[data-visitor="RG4"] img').waitFor();
 await page.locator('img').evaluateAll(els=>Promise.all(els.map(e=>e.decode())));
 assert.equal(await page.locator('[data-visitor]').count(),4);
 assert.equal(new Set(await page.locator('.background-art').evaluateAll(els=>els.map(e=>e.src))).size,4);
 assert.ok((await page.locator('.background-art').evaluateAll(els=>els.map(e=>e.naturalWidth))).every(n=>n>=1000));
 await page.locator('.layouts').screenshot({path:join(out,'dialogue-comparison.png')});
 await page.locator('#backgrounds').screenshot({path:join(out,'visitor-backgrounds.png')});
 for(const id of ['RG1','RG2','RG3','RG4']){
  const card=page.locator(`[data-visitor="${id}"]`),first=await card.locator('.speech').innerText();
  await card.locator('[data-scene-phase="repeat"]').click();assert.notEqual(await card.locator('.speech').innerText(),first);
  let turns=1;while(await card.locator('[data-scene-next]').count()){await card.locator('[data-scene-next]').click();turns++;assert.ok(turns<=4);}
  assert.ok(turns>=2);assert.ok(await card.locator('.dialogue-cue').count());
  await card.locator('[data-scene-close]').last().click();assert.equal(await card.locator('.scene-dialogue').count(),0);
  await card.locator('[data-scene-reopen]').click();assert.equal(await card.locator('.scene-dialogue').count(),1);
  await card.locator('[data-scene-help]').click();assert.match(await card.locator('.scene-help-panel').innerText(),/不扣伙伴/);
  await page.keyboard.press('Escape');assert.equal(await card.locator('.scene-help-panel').count(),0);
  await card.locator('[data-scene-phase="first"]').click();
 }
 checks.push('four composed NPC scenes: arrival/return switch, dialogue close/reopen and contextual help');
 for(const [step,title] of [[0,'常客来访'],[1,'怎么寻访？'],[3,'下一锅怎么选？'],[5,'组合怎么卖？']]){
  await page.locator('#step').selectOption(String(step));await page.locator('#layout-a [data-help]').click();
  assert.equal(await page.locator('#layout-a .help-card h3').innerText(),title);
  await page.locator('#layout-a [data-help-close]').click();
 }
 checks.push('visitor, journey, next-batch and combination pages show their own help');
 for(const width of [320,390,430]){
  await page.setViewportSize({width,height:900});
  assert.deepEqual(await page.locator('.scene-preview').evaluateAll(els=>els.filter(e=>{const d=e.querySelector('.scene-dialogue'),r=e.getBoundingClientRect(),b=d.getBoundingClientRect();return b.left<r.left||b.right>r.right||b.top<r.top||b.bottom>r.bottom||d.scrollWidth>d.clientWidth+1;}).map(e=>e.parentElement.dataset.visitor)),[],`dialogue outside scene ${width}`);
  for(const id of ['RG1','RG2','RG3','RG4']){
   const card=page.locator(`[data-visitor="${id}"]`);
   for(const phase of ['first','repeat']){
    await card.locator(`[data-scene-phase="${phase}"]`).click();let rounds=0;
    while(true){
     assert.ok(await card.locator('.scene-dialogue').evaluate(d=>{const r=d.parentElement.getBoundingClientRect(),b=d.getBoundingClientRect();return b.top>=r.top&&b.bottom<=r.bottom&&d.scrollWidth<=d.clientWidth+1;}),`round overflows ${width}/${id}/${phase}/${rounds}`);
     if(!await card.locator('[data-scene-next]').count())break;
     await card.locator('[data-scene-next]').click();assert.ok(++rounds<4);
    }
    if(width===390&&id==='RG1'&&phase==='first')await card.screenshot({path:join(out,'old-customer-final-turn-390.png')});
   }
   await card.locator('[data-scene-phase="first"]').click();
  }
  if(width===390)await page.locator('[data-visitor="RG2"]').screenshot({path:join(out,'river-dialogue-390.png')});
  for(let step=0;step<8;step++){
   await page.locator('#step').selectOption(String(step));
   assert.equal(await page.evaluate(()=>document.documentElement.scrollWidth>innerWidth),false,`page overflow ${width}/${step}`);
   assert.deepEqual(await page.locator('.game-body').evaluateAll(els=>els.filter(e=>e.scrollWidth>e.clientWidth+1).map(e=>e.scrollWidth)),[],`game overflow ${width}/${step}`);
   assert.deepEqual(await page.locator('img').evaluateAll(els=>els.filter(e=>e.complete&&!e.naturalWidth).map(e=>e.src)),[],`broken image ${width}/${step}`);
  }
 }
 checks.push('8 preview steps fit at 320/390/430px, both layouts; no missing images');
 await page.setViewportSize({width:1080,height:1130});await page.locator('#step').selectOption('4');
 await page.locator('.layouts').screenshot({path:join(out,'combination-comparison.png')});
 await page.locator('#layout-a [data-next]').click();await page.locator('#layout-a [data-stock="0"]').fill('2');await page.locator('#layout-a [data-stock="0"]').blur();
 assert.match(await page.locator('#layout-a .gain').innerText(),/尚未凑齐/);await page.locator('#layout-a [data-stock="0"]').fill('6');await page.locator('#layout-a [data-stock="0"]').blur();
 assert.match(await page.locator('#layout-a .gain').innerText(),/25%/);await page.locator('#layout-a [data-help]').click();await page.locator('#layout-a [data-help-close]').click();
 await page.locator('#restart').click();let cycleClicks=0;while(await page.locator('#step').inputValue()!=='7'){await page.locator('#layout-a [data-talk],#layout-a [data-next]').click();assert.ok(++cycleClicks<20);}
 assert.equal(await page.locator('#layout-a [data-tab]').count(),5);await page.locator('#layout-a .tabs [data-tab="新伙伴"]').click();assert.match(await page.locator('#layout-a .target').innerText(),/竹蒸笼/);
 checks.push('clickable cycle, stock-dependent bonus, help and four next-batch tabs');
 // Exercise the actual next-batch UI in an isolated page; no player saves touched.
 const styles=(await readFile('web/index.html','utf8')).match(/<link rel="stylesheet"[^>]+>/g).join('');
 await page.route('**/web/next-partner-harness.html',r=>r.fulfill({contentType:'text/html',body:'<!doctype html><meta name="viewport" content="width=device-width,initial-scale=1">'+styles+'<main id="viewport"><div id="game" style="zoom:1.21875;--game-height:692.5px"><div id="panels"></div></div></main>'}));
 await page.setViewportSize({width:390,height:844});await page.goto(base+'/web/next-partner-harness.html');
 await page.evaluate(async()=>{
  const [E,P,K,C,U,A,Kit,Art]=await Promise.all([import('/web/engine.js'),import('/web/progression.js'),import('/web/knowledge.js'),import('/web/clue-book.js'),import('/web/next-batch-ui.js'),import('/web/catalog.js'),import('/web/ui-kit.js'),import('/web/art/manifest.js')]);
  window.qaState=E.freshState(1800000000000);qaState.kitchenLevel=3;qaState.cp=100000;qaState.toolLevels.fill(2);
  for(let id=0;id<89;id++){qaState.total[`0:${id}`]=100;qaState.farm[`0:${id}`]=3;}P.syncProgress(qaState);
  for(const id of ['OBS-1','OBS-3','OBS-4'])P.learnSkill(qaState,id);
  for(let id=12;id<89;id++){delete qaState.total[`0:${id}`];delete qaState.farm[`0:${id}`];}
  for(const r of C.clueBookModel(qaState,0,1800000000000).clues.filter(r=>!r.regional).slice(0,8))K.studyRecipe(qaState,r.key,1800000000000);
  window.qaActions=[];const panels=document.querySelector('#panels');
  const portrait=path=>{const sprite=Art.resolveSprite(path);return sprite.frame?Art.spriteSVG(sprite):`<img src="${path}" alt="">`;};
  window.qaUI=U.createNextBatchUI({getState:()=>qaState,getNow:()=>1800000000000,panels,
   showPanel:(title,html,cls,kit)=>panels.innerHTML=`<section class="panel paper ${cls} kp gd kp-kitchen">${Kit.kitPageHead({title,icon:kit?.icon,help:kit?.help})}${html}</section>`,closePanel:()=>qaActions.push('close'),
   confirmBox:(t,fn)=>fn(),alertBox:t=>qaActions.push(t),act:fn=>{fn();return true;},sound:()=>{},
   characterPortrait:(e,id)=>portrait(A.characterImage(e,id)),ingredientPortrait:id=>portrait(A.toolImage(2,id)),toolPortrait:(id,l)=>portrait(A.toolImage(1,id,l)),
   startCook:(tool,prep)=>{const copy=structuredClone(qaState);prep(copy);qaActions.push({tool,selected:copy.selected});},openRestock:()=>{},openClueBook:()=>qaActions.push('clues'),openJourney:id=>qaActions.push(id)});
  qaUI.open({lens:'new'});
 });
 assert.equal(await page.locator('[data-nb-lens]').filter({hasText:/^(推荐|新伙伴|订单|多赚)$/}).count(),4);
 assert.equal(await page.locator('.nb-partner').count(),5);assert.equal(await page.locator('[data-nb-fire]').count(),0);
 await page.screenshot({path:join(out,'new-partners-390.png')});
 await page.locator('[data-nb-partner]').filter({hasText:'准备这一锅'}).first().click();assert.equal(await page.locator('[data-nb-fire]').count(),1);
 await page.locator('[data-nb-fire]').click();assert.ok((await page.evaluate(()=>qaActions)).some(x=>typeof x==='object'&&Number.isInteger(x.tool)));
 checks.push('actual new-partner page retains four tabs, shows five targets, prepares then starts chosen recipe');
 await page.evaluate(async()=>{
  const [Shop,Kit,A,Art]=await Promise.all([import('/web/shop-ui.js'),import('/web/ui-kit.js'),import('/web/catalog.js'),import('/web/art/manifest.js')]);
  const portrait=path=>{const s=Art.resolveSprite(path);return s.frame?Art.spriteSVG(s):`<img src="${path}" alt="">`;};
  qaState.ingredients[33]=2;const panels=document.querySelector('#panels');
  window.qaShop=Shop.createShopUI({getState:()=>qaState,panels,showPanel:(title,html,cls,kit)=>panels.innerHTML=`<section class="panel paper ${cls} kp gd kp-shop">${Kit.kitPageHead({title,icon:kit?.icon,help:kit?.help})}${html}</section>`,confirmBox:()=>{},alertBox:()=>{},act:()=>false,sound:()=>{},toolPortrait:(id,l)=>portrait(A.toolImage(1,id,l)),changePage:()=>{}});
  qaShop.setTab(1);qaShop.openShop();
 });
 await page.locator('[data-shop-flavor]').selectOption('甜味');
 assert.ok(await page.locator('[data-shop-ingredient-details="33"]').count());
 assert.ok((await page.locator('.sh-item-flavor').allTextContents()).every(x=>x==='甜味'));
 await page.locator('[data-shop-ingredient-details="33"]').click();assert.match(await page.locator('.sh-drawer').innerText(),/甜味/);
 checks.push('shop flavor filter and ingredient details use the same clue category');
 // Actual regular dialogue: intermediate lines must not commit the story or consume stock.
 await page.evaluate(async()=>{
  const [E,P,F,R,U]=await Promise.all([import('/web/engine.js'),import('/web/progression.js'),import('/web/facts.js'),import('/web/regulars.js'),import('/web/regular-ui.js')]);
  window.qaRegular=E.freshState(1800000000000,11);qaRegular.kitchenLevel=2;qaRegular.duck=true;qaRegular.toolLevels.fill(0);
  qaRegular.total={'0:0':400,'0:3':6,'0:8':6,'0:12':1,'0:16':1,'0:21':1,'1:0':6,'1:3':1};qaRegular.farm={'0:0':20,'0:3':12,'0:8':12,'1:0':12};
  qaRegular.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};P.syncProgress(qaRegular);qaRegular=E.normalizeSave(qaRegular,1800000000000);
  F.reduceFacts(qaRegular,[{kind:'orderComplete',instanceId:'order-1',templateId:'O01',variantId:'O01-A',region:null,chapters:null,groupDeliveries:[]}]);R.reconcileRegulars(qaRegular);
  window.qaReadCommits=0;window.qaReadBefore=JSON.stringify([qaRegular.cp,qaRegular.farm,qaRegular.expansion.collections.entitlements]);
  const panels=document.querySelector('#panels');window.qaRegularUI=U.createRegularUI({getState:()=>qaRegular,commitProgress:fn=>{qaReadCommits++;return fn(qaRegular);},panels,showPanel:(title,html,cls)=>panels.innerHTML=`<section class="panel paper ${cls}">${html}</section>`,openBusiness:()=>{},openOrders:()=>{},openProjects:()=>{}});qaRegularUI.open('RG1');
 });
 await page.locator('[data-regular-read]').click();const line1=await page.locator('.regulars-story').innerText();
 assert.equal(await page.evaluate(()=>qaReadCommits),0);assert.equal(await page.locator('.regulars-next').count(),0);
 await page.locator('[data-regular-read]').click();assert.notEqual(await page.locator('.regulars-story').innerText(),line1);
 assert.equal(await page.evaluate(()=>qaReadCommits),0);assert.equal(await page.locator('.regulars-next').count(),1);
 for(const width of [320,390]){await page.setViewportSize({width,height:844});await page.evaluate(width=>{const g=document.querySelector('#game');g.style.zoom=width/320;g.style.setProperty('--game-height',844/(width/320)+'px');},width);await page.screenshot({path:join(out,`regular-dialogue-${width}.png`)});assert.deepEqual(await page.locator('.regulars-scroll').evaluateAll(els=>els.filter(e=>e.scrollWidth>e.clientWidth+1).map(e=>({scroll:e.scrollWidth,client:e.clientWidth}))),[]);}
 await page.screenshot({path:join(out,'regular-dialogue-390.png')});
 await page.locator('[data-regular-read]').click();assert.equal(await page.evaluate(()=>qaReadCommits),1);
 assert.deepEqual(await page.evaluate(()=>qaRegular.expansion.regulars.RG1.readStages),['RG1-1']);
 assert.equal(await page.evaluate(()=>JSON.stringify([qaRegular.cp,qaRegular.farm,qaRegular.expansion.collections.entitlements])===qaReadBefore),true);
 checks.push('actual regular dialogue advances one line at a time; only final acknowledgement marks read, with no stock, CP or reward changes');
 // New destinations use the real region UI, including the companion picker and departure.
 await page.evaluate(async()=>{
  const [U,E,A,Art,Kit]=await Promise.all([import('/web/regional-ui.js'),import('/web/engine.js'),import('/web/catalog.js'),import('/web/art/manifest.js'),import('/web/ui-kit.js')]);
  qaRegular.toolLevels.fill(2);qaRegular.kitchenLevel=3;qaRegular.total['0:0']=4000;for(let i=0;i<25;i++)qaRegular.total['0:'+i]??=1;
  window.qaTripNow=1800000000000;window.qaDialog=null;
  const panels=document.querySelector('#panels');
  window.qaRegion=U.createRegionalUI({getState:()=>qaRegular,getNow:()=>qaTripNow,commitProgress:fn=>fn(qaRegular),panels,
   showPanel:(title,html,cls)=>panels.innerHTML=`<section class="panel paper ${cls} kp gd kp-journey">${html}</section>`,
   alertBox:t=>{throw Error(t);},confirmBox:(t,fn)=>fn(),kitDialog:opts=>{qaDialog=opts;},
   characterPortrait:(e,id)=>{const path=A.characterImage(e,id),sprite=Art.resolveSprite(path);return sprite.frame?Art.spriteSVG(sprite):`<img src="${path}" alt="">`;},
   goKitchen:()=>{},openRecipe:()=>{},openClueBook:k=>qaActions.push(k)});
  qaRegion.open({region:'O'});
 });
 for(const id of ['O','H']){
  await page.evaluate(id=>qaRegion.open({region:id}),id);
  await page.locator(`[data-regional-region="${id}"]`).click();
  assert.equal(await page.locator('.jr-material-preview span').count(),2);
  await page.locator('[data-regional-slot="0"]').click();await page.locator('[data-regional-member="0:0"]').click();
  await page.locator('[data-journey-enter]').click();assert.ok(await page.locator('[data-extra-clue]').count()>=3);
  await page.screenshot({path:join(out,`new-region-${id}-390.png`)});
  await page.locator('[data-extra-go]').click();await page.evaluate(()=>qaDialog.onYes());
  assert.equal(await page.evaluate(()=>qaRegular.progress.trip.status),'running');
  await page.evaluate(async()=>{const [E,X]=await Promise.all([import('/web/engine.js'),import('/web/exploration.js')]);qaTripNow=qaRegular.progress.trip.endAt;E.advanceWorld(qaRegular,qaTripNow,()=>.99);X.claimTrip(qaRegular,qaRegular.progress.trip.id,{},qaTripNow,()=>.99);qaRegion.refresh();});
  assert.equal(await page.evaluate(()=>qaRegular.progress.trip.status),'settled');
 }
 await page.evaluate(()=>qaRegularUI.open('intro:RG1'));
 await page.locator('[data-intro-next]').click();const intro=await page.locator('.regulars-story').innerText();
 await page.evaluate(()=>qaRegularUI.refresh());assert.equal(await page.locator('.regulars-story').innerText(),intro);
 await page.locator('[data-intro-next]').click();await page.locator('[data-intro-next]').click();assert.equal(await page.evaluate(()=>qaRegular.events.loopVisitorMet),true);
 checks.push('two new regions: previews, picker, related clues, departure and return; first visitor preserves dialogue during refresh');
 assert.deepEqual(errors,[]);await writeFile(join(out,'report.json'),JSON.stringify({passed:true,checks,errors},null,2));console.log(JSON.stringify({passed:true,checks,errors},null,2));
}finally{await browser?.close();server.kill();}
