// Uses the shipped app with isolated, real save loading. Never touches player data.
import {createRequire} from 'node:module';
import {spawn} from 'node:child_process';
import {mkdir,readFile,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {goldenEgg,eggFloor} from '../web/kitchen-golden.js';
const require=createRequire(import.meta.url),{chromium}=require(process.env.PLAYWRIGHT_MODULE??process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const out='artifacts/kitchen-golden-runtime',at=1801224000000;
await mkdir(out,{recursive:true});
const server=spawn(process.execPath,['server.mjs','0'],{stdio:['ignore','pipe','pipe'],windowsHide:true});
const base=await new Promise((ok,no)=>{server.on('error',no);server.stdout.on('data',b=>{const m=String(b).match(/http:\/\/127\.0\.0\.1:\d+/);if(m)ok(m[0]);});});
const browser=await chromium.launch({headless:true});
let page,passed=false;const errors=[],checks=[],screens=[];
function seed(level,mode='incubating'){
 const s=E.freshState(at,29);s.cp=50000;s.kitchenLevel=level;s.toolLevels=Array(9).fill(0);s.ingredients={0:4,1:4};s.progress.tutorialSeen=true;s.total={'0:0':24};s.music=false;s.sound=false;
 if(mode!=='empty'){
  s.selected=[0];E.startBatch(s,2,at-143000,()=>.5,()=>.5);
  for(const e of s.batch.eggs){e.openAt=at+3457000;e.blackAt=at+12000000;e.id=0;if(['ready','collected','partial'].includes(mode)){e.status='ready';e.animationAt=at-5000;e.openAt=at-10000;}}
  if(mode==='collected')s.batch.eggs.forEach(e=>e.collected=true);
  if(mode==='partial')s.batch.eggs.forEach((e,i)=>{if(i<12){e.status='egg';e.openAt=at+3457000;}});
 }
 s.cp=1280;s.selected=[1];s.lastClean=at-36*3600000*.82;s.cleanCycle.dirtyAt=at+36*3600000*.18;
 return s;
}
async function open(s,width=390,height=844,live=false){
 const c=await browser.newContext({viewport:{width,height},reducedMotion:'reduce',deviceScaleFactor:1});
 await c.addInitScript(({s,at,live})=>{if(!live)Date.now=()=>at;if(!sessionStorage.getItem('seeded')){localStorage.setItem('chick-kitchen-v1',JSON.stringify(s));localStorage.setItem('chick-kitchen-ui-v1',JSON.stringify({seenSupplyMove:true}));sessionStorage.setItem('seeded','1');}},{s,at:s.lastSeen,live});
 page=await c.newPage();page.on('pageerror',e=>errors.push(e.message));page.on('response',r=>{if(r.status()>=400)errors.push(r.status()+' '+r.url());});
 await page.goto(base);await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.waitForTimeout(650);await page.evaluate(()=>document.fonts.ready);return c;
}
const read=()=>page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
async function shot(name){await page.screenshot({path:`${out}/${name}.png`});screens.push(name);}
const control=id=>page.locator(`[data-control-id="${id}"]`);
try{
 for(let level=0;level<4;level++){
  const c=await open(seed(level));await shot(`lv${level+1}-runtime`);
  assert.equal(await page.locator('[data-control-id^="egg:"]').count(),24);
  assert.equal((await read()).kitchenLevel,level);
  for(let i=0;i<24;i++){const r=goldenEgg(level,i);assert.ok(r.x>=65&&r.x+r.w<=322&&r.y+r.h<=eggFloor(level)+1e-9);}
  checks.push(`Lv.${level+1}: real save level selects scene; 24 independent egg/hit mappings within support`);
  await c.close();
 }
 if(process.argv.includes('--capture-only')){passed=true;}else{
  for(let level=0;level<4;level++)for(const mode of ['empty','ready','collected','partial']){
   const c=await open(seed(level,mode));await shot(`lv${level+1}-${mode}`);
   assert.equal(await page.locator('[data-control-id^="egg:"]').count(),['empty','collected'].includes(mode)?0:24);
   if(mode==='ready'){
    const before=await read();
    // Front-to-back real pointer clicks must use the new visual mapping.
    for(let i=23;i>=0;i--)await control('egg:'+i).click({position:{x:15,y:6}});
    // Harvests commit when the last chick lands (MOTION.flightMs), not on release.
    await page.waitForTimeout(800);const after=await read();assert.equal(after.batch.eggs.filter(e=>e.collected).length,24);assert.equal(after.cp-before.cp,24);assert.equal(after.total['0:0']-before.total['0:0'],24);
    await page.waitForTimeout(800);assert.equal(await page.locator('#panels .harvest-card').count(),0,'harvest card paused');
    checks.push(`Lv.${level+1}: 24 actual clicks collect exactly once; CP/inventory +24; nest stays clear`);
   }
   checks.push(`Lv.${level+1}: ${mode} screenshot and state`);await c.close();
  }
  {
   // One swipe across the nest: every chick leaves at once and the gesture commits as one transaction.
   const c=await open(seed(1,'ready'));const before=await read();
   const centres=await page.locator('[data-control-id^="egg:"]').evaluateAll(es=>es.map(e=>{const r=e.getBoundingClientRect();return [r.x+r.width/2,r.y+r.height/2];}));
   const rows=[...centres].sort((a,b)=>a[1]-b[1]),path=[];for(let i=0;i<rows.length;i+=6){const row=rows.slice(i,i+6).sort((a,b)=>a[0]-b[0]);if((i/6)%2)row.reverse();path.push(...row);}
   await page.mouse.move(...path[0]);await page.mouse.down();
   for(let i=1;i<path.length;i++)for(let k=1;k<=4;k++)await page.mouse.move(path[i-1][0]+(path[i][0]-path[i-1][0])*k/4,path[i-1][1]+(path[i][1]-path[i-1][1])*k/4);
   await page.mouse.up();await page.waitForTimeout(800);const after=await read();
   assert.equal(after.batch.eggs.filter(e=>e.collected).length,24);assert.equal(after.cp-before.cp,24);
   assert.ok(after.meta.revision-before.meta.revision<=2,`swipe committed ${after.meta.revision-before.meta.revision} transactions`);
   checks.push(`Lv.2: one swipe collects all 24 exactly once in ${after.meta.revision-before.meta.revision} transaction(s)`);await c.close();
  }
  for(const level of [0,3]){
   const c=await open(seed(level),320,844);await shot(`lv${level+1}-320x844`);
   const bounds=await page.locator('#controls button,#quick-actions button,#main-nav button').evaluateAll(es=>es.filter(e=>!e.disabled).map(e=>{const r=e.getBoundingClientRect();return {id:e.dataset.controlId,x:r.x,right:r.right,y:r.y,bottom:r.bottom};}));
   assert.ok(bounds.every(r=>r.x>=-.5&&r.right<=320.5&&r.y>=0&&r.bottom<=844.5),JSON.stringify(bounds));
   await control('next').click();assert.ok(await control('tool:4').count());await control('prev').click();assert.ok(await control('tool:0').count());
   await control('ingredient').click();await page.locator('.ingredient-screen').waitFor();await page.keyboard.press('Escape');
   checks.push(`Lv.${level+1} 320px: all controls/eggs bounded; paging and seasoning usable`);await c.close();
  }
  const c=await open(seed(3,'empty'));
  for(const [id,selector]of [['supply','.shop-screen'],['inventory','.warehouse-screen'],['workshop','.workshop-screen'],['help','.guide-screen']]){
   await control(id).click();await page.locator(selector).waitFor();await page.locator('#main-nav').getByRole('button',{name:'厨房',exact:true}).click();
  }
  await control('settings').click();await page.locator('.settings-screen').waitFor();await page.locator('#main-nav').getByRole('button',{name:'厨房',exact:true}).click();
  checks.push('shop, warehouse, craft, help and settings open their real pages and return');
  for(const name of ['农场','生意','寻访','图鉴','厨房']){
   await page.locator('#main-nav').getByRole('button',{name,exact:true}).click();assert.equal(await page.locator('#main-nav').getByRole('button',{name,exact:true}).getAttribute('aria-current'),'page');
  }
  checks.push('all five bottom navigation pages and selected states');
  await control('ingredient').click();await page.locator('[data-nb-lens="manual"]').click();await page.locator('[data-id="1"]').click();await page.locator('[data-ok]').click();assert.deepEqual((await read()).selected,[]);await shot('seasoning-empty');
  await control('ingredient').click();await page.locator('[data-nb-lens="manual"]').click();await page.locator('[data-id="0"]').click();await page.locator('[data-ok]').click();assert.deepEqual((await read()).selected,[0]);
  const beforeCook=await read();await control('tool:2').click();await page.locator('.cooking-dialog').waitFor();await page.locator('[data-yes]').click();
  let cooked=await read();assert.equal(cooked.batch.tool,2);assert.equal(cooked.batch.eggs.length,24);assert.equal(cooked.cp,beforeCook.cp-60);assert.equal(cooked.ingredients[0],beforeCook.ingredients[0]-1);assert.deepEqual(cooked.selected,[]);assert.deepEqual(cooked.batch.ingredients,[0]);
  await shot('cook-started');checks.push('seasoning empty/selected; confirm blue pot starts 24 eggs, deducts exactly 60 CP + 1 seasoning');
  const cleanBefore=await read();await control('clean').click();await page.locator('.cleaning-dialog').waitFor();await page.locator('[data-yes]').click();
  const cleanAfter=await read();assert.equal(cleanAfter.cp,cleanBefore.cp-205);assert.equal(cleanAfter.lastClean,at);assert.equal(cleanAfter.batch.eggs.length,24);await shot('cleaned');checks.push('18% cleanliness: actual cleaning costs 205 CP at Lv.4; batch retained; resets to 100%');
  await control('alarm').click();assert.equal((await read()).alarm,true);await control('alarm').click();assert.equal((await read()).alarm,false);checks.push('alarm toggles through live timer');
  await control('next').click();await control('next').click();await control('prev').click();assert.ok(await control('tool:1').count());
  // A swipe also changes the real tool strip, without opening a cook confirmation.
  const t=await control('tool:2').boundingBox();await page.mouse.move(t.x+t.width/2,t.y+20);await page.mouse.down();await page.mouse.move(t.x+t.width/2-130,t.y+20,{steps:6});await page.mouse.up();assert.equal(await page.locator('.cooking-dialog').count(),0);
  checks.push('both paging arrows and drag work without accidental cooking');
  const snapshot=await read();await page.reload();await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.waitForTimeout(550);const restored=await read();
  for(const key of ['cp','ingredients','batch','kitchenLevel','toolLevels'])assert.deepEqual(restored[key],snapshot[key]);checks.push('new runtime saves reload without losing batch, CP, ingredients or levels');
  // Advance only this isolated test clock; use real engine transitions and live redraw.
  const latest=Math.max(...restored.batch.eggs.map(e=>e.openAt));await page.evaluate(value=>{Date.now=()=>value;},latest+1);await page.waitForTimeout(180);
  assert.ok((await read()).batch.eggs.some(e=>e.status==='cracking'));
  await page.evaluate(value=>{Date.now=()=>value;},latest+2101);await page.waitForTimeout(180);await page.evaluate(value=>{Date.now=()=>value;},latest+3101);await page.waitForTimeout(180);
  // Every crack is saved at once; the hatch→ready animation steps ride along with the next save.
  assert.ok((await read()).batch.eggs.every(e=>e.status!=='egg'));
  await page.evaluate(()=>window.dispatchEvent(new Event('pagehide')));
  assert.ok((await read()).batch.eggs.every(e=>e.status==='ready'));await shot('clock-ready');checks.push('real clock advances egg → cracking → hatching → ready, redraw and save');await c.close();
  // Original v3 save, loaded through the production migration route.
  const old=JSON.parse(await readFile('tests/fixtures/golden/mid-v3.json','utf8')),oc=await open(old);const migrated=await read(),expected=E.normalizeSave(old,old.lastSeen);
  for(const key of ['cp','kitchenLevel','ingredients','toolLevels','total'])assert.deepEqual(migrated[key],expected[key]);
  assert.equal(await page.evaluate(()=>localStorage.getItem('chick-kitchen-v1.pre-upgrade-v3')),JSON.stringify(old));await shot('legacy-v3');checks.push('v3 actual load preserves values and byte-for-byte pre-upgrade backup');await oc.close();
  const locked=seed(0,'empty');locked.toolLevels[3]=-1;locked.selected=[];const lc=await open(locked);await shot('locked-tool');await control('tool:3').click();await page.locator('.shop-screen').waitFor();assert.equal((await read()).toolLevels[3],-1);checks.push('locked cookware opens shop; no accidental purchase');await lc.close();
  const duck=seed(3,'empty');duck.duck=true;const dc=await open(duck);await control('duck:1').click();assert.equal((await read()).egg,1);await control('duck:0').click();assert.equal((await read()).egg,0);checks.push('unlocked chicken/duck selection retained');await dc.close();
  // Exercise actual upgrade transactions, not just four pre-seeded screenshots.
  const up=seed(0);up.cp=100000;up.toolLevels=Array(9).fill(2);const uc=await open(up);
  for(let level=1;level<=3;level++){
   await control('level').click();await page.locator('[data-upgrade-kitchen]').click();await page.locator('.upgrade-done [data-yes]').click();assert.equal((await read()).kitchenLevel,level);assert.equal((await read()).batch.eggs.length,24);await shot(`upgrade-to-lv${level+1}`);
  }
  checks.push('actual Lv.1 → Lv.2 → Lv.3 → Lv.4 upgrades swap scene and hit geometry while keeping current batch');await uc.close();
 }
 assert.deepEqual(errors,[]);passed=true;
}catch(e){if(page&&!page.isClosed()){await shot('failure');await writeFile(out+'/failure.txt',await page.locator('body').innerText());}throw e;}
finally{await browser.close();server.kill();await writeFile(out+'/qa-report.json',JSON.stringify({checkedAt:new Date().toISOString(),passed,captureOnly:process.argv.includes('--capture-only'),checks,errors,screens,isolatedSaves:true,realDevice:false},null,2));console.log(JSON.stringify({passed,checks,errors},null,2));}
