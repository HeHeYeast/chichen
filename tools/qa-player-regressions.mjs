// Player-reported failures: unread guests hidden by remembered detail pages,
// and character atlases rendered as entire sheets. Use isolated saves only.
import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import {join} from 'node:path';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {freshState,normalizeSave,startBatch} from '../web/engine.js';
import {syncProgress} from '../web/progression.js';
import {reduceFacts} from '../web/facts.js';
import {reconcileRegulars,reconcileProgress} from '../web/regulars.js';
import {characterIndex,characterImage} from '../web/catalog.js';
import {REGIONAL} from '../web/content-registry.js';
import {openBusiness,BUSINESS_WINDOW_MS} from '../web/business.js';
const require=createRequire(import.meta.url);
const {chromium}=require(process.argv[2]??process.env.CHICK_PLAYWRIGHT_PACKAGE??join(process.env.APPDATA,'npm/node_modules/gsd-pi/node_modules/playwright-core'));
const out='artifacts/qa/player-regressions';await mkdir(out,{recursive:true});
const now=1800000000000,seed=freshState(now,41);
seed.kitchenLevel=3;seed.duck=true;seed.cp=5000;seed.toolLevels.fill(2);
seed.total=Object.fromEntries([...characterIndex.keys()].map(k=>[k,10]));seed.total['0:0']=1000;
seed.farm=Object.fromEntries([...characterIndex.keys()].map(k=>[k,5]));
seed.progress.tutorialSeen=true;seed.events.loopVisitorMet=true;
seed.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};syncProgress(seed);
for(const [i,templateId]of ['O01','O05'].entries())reduceFacts(seed,[{kind:'orderComplete',instanceId:`order-${i+1}`,templateId,variantId:`${templateId}-A`,region:null,chapters:null,groupDeliveries:[]}]);
reconcileRegulars(seed);assert.ok(seed.expansion.regulars.RG1.pendingStage);assert.ok(seed.expansion.regulars.RG2.pendingStage);
const source=normalizeSave(seed,now),checks=[],errors=[];let browser,page,passed=false;
const server=spawn(process.execPath,['server.mjs','0'],{stdio:['ignore','pipe','pipe'],windowsHide:true});
try{
 const base=await new Promise((ok,bad)=>{server.once('error',bad);server.stdout.on('data',b=>{const m=String(b).match(/http:\/\/127\.0\.0\.1:\d+/);if(m)ok(m[0]);});});
 browser=await chromium.launch({headless:true,executablePath:process.env.CHICK_QA_CHROME??'C:/Program Files/Google/Chrome/Application/chrome.exe'});
 const context=await browser.newContext({viewport:{width:390,height:844},hasTouch:true,isMobile:true});
 await context.addInitScript(({source,now})=>{if(location.protocol==='about:')return;Date.now=()=>now+Math.floor(performance.now());if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(source));},{source,now});
 page=await context.newPage();page.on('pageerror',e=>errors.push(e.message));
 await page.goto(base+'/');await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.locator('.workshop-launch').waitFor();await page.waitForTimeout(650);
 await page.getByRole('button',{name:'生意',exact:true}).click();
 await page.locator('[data-trade-view="regulars"]').first().click();
 // Explicit list access also covers the card route used in old saves.
 if(await page.locator('[data-regular-open="RG1"]').count())await page.locator('[data-regular-open="RG1"]').tap();
 await page.locator('[data-regular-read]').tap();await page.locator('[data-regular-read]').tap();await page.locator('[data-regular-read]').tap();
 assert.deepEqual(await page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')).expansion.regulars.RG1.readStages),['RG1-1']);
 await page.locator('[data-shop-back]').tap();
 assert.match(await page.locator('.bh-small[data-trade-view="regulars"]').innerText(),/采购人有新故事/);
 await page.locator('.bh-small[data-trade-view="regulars"]').tap();
 assert.equal(await page.locator('[data-regular-read]').count(),1,'new-story notification must lead to a readable story, not remembered RG1 detail');
 assert.match(await page.locator('.regulars-head h3').innerText(),/采购人/);
 await page.screenshot({path:join(out,'unread-story-390.png')});checks.push('read RG1, return home, then RG2 notification opens RG2 story on touch');
 const before=await page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
 await page.locator('[data-regular-read]').tap();
 await page.waitForTimeout(2200);assert.match(await page.locator('.rg-dialogue-progress').innerText(),/^1 \//,'timer preserves dialogue');
 await page.getByRole('button',{name:'寻访',exact:true}).click();await page.getByRole('button',{name:'生意',exact:true}).click();
 assert.match(await page.locator('.rg-dialogue-progress').innerText(),/^1 \//,'navigation preserves dialogue');
 for(const [width,height]of [[320,568],[390,844],[430,932]]){
  await page.setViewportSize({width,height});
  const fit=await page.locator('[data-regular-read]').evaluate(el=>{const r=el.getBoundingClientRect();return r.top>=0&&r.bottom<=innerHeight&&r.left>=0&&r.right<=innerWidth&&el.contains(document.elementFromPoint(r.left+r.width/2,r.top+r.height/2));});
  assert.ok(fit,`story footer visible and clickable at ${width}`);
 }
 while(await page.locator('[data-regular-read]').count())await page.locator('[data-regular-read]').tap();
 const after=await page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
 assert.deepEqual(after.expansion.regulars.RG2.readStages,['RG2-1']);assert.equal(after.cp,before.cp);assert.deepEqual(after.farm,before.farm);
 await page.reload();await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.locator('.workshop-launch').waitFor();await page.waitForTimeout(650);
 assert.deepEqual(await page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')).expansion.regulars.RG2.readStages),['RG2-1']);
 checks.push('dialogue survives clock/navigation, footer usable at 320/390/430px, read persists without stock or CP changes');
 await page.setViewportSize({width:390,height:844});
 await page.getByRole('button',{name:'寻访',exact:true}).click();await page.locator('.journey-map').waitFor();
 const art=await page.evaluate(async()=>{const {journeyCharacter}=await import('/web/journey-art.js');const {characterIndex}=await import('/web/catalog.js');return [...characterIndex.keys()].map(key=>({key,html:journeyCharacter(key)}));});
 for(const {key,html}of art)assert.ok(!/<(?:img|image)[^>]*(?:src|href)="[^"]+#/.test(html),`${key}: raw atlas fragment`);
 checks.push(`${art.length} journey portraits never load raw atlas fragment URLs`);
 const atlasKeys=[...characterIndex.keys()].filter(key=>characterImage(...key.split(':').map(Number)).includes('#'));
 await page.evaluate(async keys=>{const {journeyCharacter}=await import('/web/journey-art.js');
  const gallery=document.createElement('div');gallery.id='portrait-qa';gallery.style='position:fixed;inset:0;z-index:10000;background:#fff9e4;overflow:auto;display:grid;grid-template-columns:repeat(4,1fr);gap:8px;padding:12px';
  gallery.innerHTML=keys.map(k=>`<div style="height:110px;text-align:center"><div style="width:72px;height:80px;margin:auto">${journeyCharacter(k)}</div><small>${k}</small></div>`).join('');document.body.append(gallery);
  const paths=[...new Set([...gallery.querySelectorAll('image')].map(el=>el.getAttribute('href')))];
  await Promise.all(paths.map(path=>{const im=new Image();im.src=path;return im.decode();}));
 },atlasKeys);
 assert.equal(await page.locator('#portrait-qa svg svg[overflow="hidden"]').count(),atlasKeys.length);
 await page.locator('#portrait-qa').screenshot({path:join(out,'atlas-portraits-390.png')});
 // Narrow/wide letterboxes must clip the source frame independently of outer CSS.
 for(const [w,h]of [[60,100],[100,60]]){
  await page.locator('#portrait-qa>div>div').evaluateAll((els,{w,h})=>els.forEach(el=>{el.style.width=w+'px';el.style.height=h+'px';}),{w,h});
  assert.ok(await page.locator('#portrait-qa svg svg').evaluateAll(els=>els.every(el=>getComputedStyle(el).overflow==='hidden')));
 }
 checks.push(`${atlasKeys.length} atlas identities visually rendered with inner clipping in square, narrow and wide slots`);
 // While a story is on screen, a real business window may queue its successor.
 // Keep the old conversation stable and never acknowledge the unseen one.
 const raceSeed=structuredClone(source),raceKeys=REGIONAL.menus.find(m=>m.id==='MN2').examples[0].map(x=>x.key);
 raceSeed.sound=false;raceSeed.music=false;for(const key of raceKeys)raceSeed.farm[key]=20;
 openBusiness(raceSeed,{menuId:'MN2',stock:Object.fromEntries(raceKeys.map(k=>[k,6])),pinnedRegular:'RG1'},now);
 const raceContext=await browser.newContext({viewport:{width:390,height:844},hasTouch:true,isMobile:true});
 await raceContext.addInitScript(({seed,now})=>{if(location.protocol==='about:')return;window.__storyOffset=0;Date.now=()=>now+window.__storyOffset+Math.floor(performance.now());localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));},{seed:normalizeSave(raceSeed,now),now});
 page=await raceContext.newPage();page.on('pageerror',e=>errors.push(e.message));await page.goto(base+'/');
 await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.locator('.workshop-launch').waitFor();await page.waitForTimeout(650);
 await page.getByRole('button',{name:'生意',exact:true}).click();if(await page.locator('.shop-ledger').count())await page.locator('[data-shop-back]').tap();
 await page.locator('[data-trade-view="regulars"]').first().tap();await page.locator('[data-regular-read]').tap();await page.locator('[data-regular-read]').tap();
 const oldText=await page.locator('.regulars-story').innerText();assert.match(await page.locator('.rg-dialogue-progress').innerText(),/^2 \//);
 await page.evaluate(offset=>{window.__storyOffset=offset;},2*BUSINESS_WINDOW_MS+1000);
 await page.waitForFunction(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')).expansion.regulars.RG1.pendingStage?.id==='RG1-2');
 await page.waitForTimeout(1400);assert.equal(await page.locator('.regulars-story').innerText(),oldText,'background progress must not replace a story being read');
 await page.getByRole('button',{name:'寻访',exact:true}).tap();await page.getByRole('button',{name:'生意',exact:true}).tap();
 assert.equal(await page.locator('.regulars-story').innerText(),oldText,'navigation retains the displayed story even after its successor arrives');
 const raceBefore=await page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
 await page.locator('[data-regular-read]').tap();
 const raceAfter=await page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
 assert.deepEqual(raceAfter.expansion.regulars.RG1.readStages,['RG1-1']);assert.equal(raceAfter.expansion.regulars.RG1.pendingStage.id,'RG1-2');
 assert.equal(raceAfter.cp,raceBefore.cp);assert.deepEqual(raceAfter.farm,raceBefore.farm);
 assert.equal(await page.locator('[data-regular-read]').innerText(),'听听看');await page.screenshot({path:join(out,'background-story-kept-unread.png')});
 checks.push('real MN2 window during RG1-1 dialogue keeps the displayed text and leaves RG1-2 unread on acknowledgement');
 for(const input of ['touch','mouse','keyboard'])for(const [selector,result]of [['#main-nav [data-control-id="nav:1"]','.farm-map:not([hidden])'],['#quick-actions [data-control-id="inventory"]','.warehouse-screen'],['.bh-small[data-trade-view="regulars"]','.regulars-screen']]){
  const pressSeed=structuredClone(source);pressSeed.sound=false;pressSeed.music=false;startBatch(pressSeed,0,now);pressSeed.batch.eggs[0].openAt=now+1000;
  const pressContext=await browser.newContext({viewport:{width:390,height:844},hasTouch:true,isMobile:true});
  await pressContext.addInitScript(({seed,now})=>{if(location.protocol==='about:')return;window.__touchOffset=0;Date.now=()=>now+window.__touchOffset;localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));},{seed:normalizeSave(pressSeed,now),now});
  page=await pressContext.newPage();page.on('pageerror',e=>errors.push(e.message));await page.goto(base+'/');await page.getByRole('button',{name:'开始游戏',exact:true}).tap();await page.waitForTimeout(700);
  if(selector.startsWith('.bh-small'))await page.getByRole('button',{name:'生意',exact:true}).tap();
  const button=page.locator(selector),box=await button.boundingBox();assert.ok(box);const cdp=await pressContext.newCDPSession(page);
  const point={x:box.x+box.width/2,y:box.y+box.height/2};
  if(input==='touch')await cdp.send('Input.dispatchTouchEvent',{type:'touchStart',touchPoints:[point]});else if(input==='keyboard'){await button.focus();await page.keyboard.down(' ');}else{await page.mouse.move(point.x,point.y);await page.mouse.down();}
  await page.evaluate(selector=>{window.__heldControl=document.querySelector(selector);window.__touchOffset=1001;},selector);
  await page.waitForFunction(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')).batch.eggs[0].status!=='egg');
  await page.waitForTimeout(1200);
  if(input==='touch')await cdp.send('Input.dispatchTouchEvent',{type:'touchEnd',touchPoints:[]});else if(input==='keyboard')await page.keyboard.up(' ');else await page.mouse.up();await page.waitForTimeout(300);
  assert.equal(await page.locator(result).count(),1,`${input}/${selector}: a hatch between press and release must not swallow the click`);
  await pressContext.close();
 }
 checks.push('physical held touch, mouse and keyboard input on navigation, warehouse and business cards survives a simultaneous egg hatch');
 const projectSeed=structuredClone(source);projectSeed.sound=false;projectSeed.music=false;projectSeed.expansion.regions.guideFlags=['GUIDE-B'];
 for(const m of REGIONAL.materials)projectSeed.expansion.discovery.identified[m.id]=++projectSeed.meta.factSeq;
 for(const c of REGIONAL.cards.filter(c=>c.type==='specimen'))projectSeed.expansion.discovery.cards[c.id]=++projectSeed.meta.factSeq;
 reduceFacts(projectSeed,['MN1','MN2'].map((menuId,i)=>({kind:'businessWitness',sessionId:`business-${i+100}`,menuId,soldByKey:{'0:0':6},roleSales:{},fullSoldByKey:{},fullRoleSales:{},valid:true,complete:false})));
 reconcileProgress(projectSeed);
 const projectContext=await browser.newContext({viewport:{width:390,height:844},hasTouch:true,isMobile:true});
 await projectContext.addInitScript(({seed,now})=>{if(location.protocol==='about:')return;Date.now=()=>now;localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));},{seed:normalizeSave(projectSeed,now),now});
 page=await projectContext.newPage();page.on('pageerror',e=>errors.push(e.message));await page.goto(base+'/');await page.getByRole('button',{name:'开始游戏',exact:true}).tap();await page.waitForTimeout(700);
 await page.getByRole('button',{name:'生意',exact:true}).tap();assert.equal(await page.locator('.bh-small[data-trade-view="projects"]').getAttribute('data-trade-focus'),'PJ-1');
 await page.locator('.bh-small[data-trade-view="projects"]').tap();await page.locator('[data-project-list]').tap();await page.locator('[data-project-open="PJ-4"]').tap();
 await page.locator('[data-project-portraits]').tap();const portraitButton=page.locator('[data-project-portrait]').first(),portraitKey=await portraitButton.getAttribute('data-project-portrait');await portraitButton.tap();
 const projectBefore=await page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
 await page.getByRole('button',{name:'寻访',exact:true}).tap();await page.getByRole('button',{name:'生意',exact:true}).tap();
 assert.equal(await page.locator(`[data-project-portrait="${portraitKey}"]`).count(),1,'returning retains the actual project editor');
 assert.equal(await page.locator(`[data-project-portrait="${portraitKey}"]`).getAttribute('aria-checked'),'true','returning retains the unsaved portrait selection');
 const projectAfter=await page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));assert.equal(projectAfter.cp,projectBefore.cp);assert.deepEqual(projectAfter.farm,projectBefore.farm);
 checks.push('project notification target does not override a later project page or erase its portrait draft on return');
 assert.deepEqual(errors,[]);passed=true;
}catch(e){if(page&&!page.isClosed()){await page.screenshot({path:join(out,'failure.png')});await writeFile(join(out,'failure.txt'),await page.locator('body').innerText());}throw e;}
finally{await browser?.close();server.kill();const report={passed,checks,errors,realDeviceTest:false};await writeFile(join(out,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));}
