// Work F playable acceptance: river and tea slope on the shared regional page,
// lock explanations, and an order F opens. Boot fixture and clock are controlled.
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
const {chromium}=require(packagePath),output=resolve(root,'artifacts/qa/work-f');await mkdir(output,{recursive:true});
const now=1800000000000,seed=freshState(now,20260923);
// Mid-game legacy save: kitchen Lv.2, duck bought, pan/boil/oven/keep-warm/steamer.
const OLD=['0:0','0:3','0:4','0:6','0:8','0:10','0:17','0:18','0:43','0:114','0:115','0:116','0:117','1:0','1:3','1:5','1:6','1:7','1:9','1:22'];
seed.kitchenLevel=1;seed.duck=true;seed.toolLevels=[0,1,1,0,0,0,-1,-1,0];seed.cp=20000;
seed.total=Object.fromEntries(OLD.map((k,i)=>[k,i?20:600]));seed.farm=Object.fromEntries(OLD.map(k=>[k,6]));
for(let i=20;i<32;i++)seed.total['0:'+i]=1;
seed.ingredients={};seed.progress.orders['first-sale']={accepted:true,completed:true,choice:'0:0',delivered:12};seed.progress.sources=earnedSources(seed);seed.progress.tutorialSeen=true;
const checks=[],errors=[],screens=[];let browser,p,passed=false;
const server=spawn(process.execPath,['server.mjs','0'],{cwd:root,stdio:['ignore','pipe','pipe'],windowsHide:true});
const read=()=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
const screenshot=async name=>{await p.waitForTimeout(120);await p.screenshot({path:resolve(output,name+'.png')});screens.push(name);};
const start=async()=>{await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.locator('.workshop-launch').waitFor();await p.waitForTimeout(650);};
const open=async()=>{if(await p.locator('.regional-screen').count())return;await p.getByRole('button',{name:'寻访',exact:true}).click();await p.locator('.regional-screen').waitFor();};
const tab=async name=>{
  if(name==='record'){
    if(await p.locator('[data-regional-record]').count())await p.locator('[data-regional-record]').click();
    else if(await p.locator('[data-regional-tab="record"]').count())await p.locator('[data-regional-tab="record"]').first().click();
  }else if(await p.getByRole('button',{name:'回到寻访',exact:true}).count())await p.getByRole('button',{name:'回到寻访',exact:true}).click();
  else if(await p.locator('[data-journey-enter]').count())await p.locator('[data-journey-enter]').click();
};
const leaveRegional=async()=>{while(await p.locator('.regional-screen').count())await p.locator('[data-journey-back]').click();};
const jump=async at=>{await p.evaluate(({at,base})=>{window.__workBOffset=at-base;sessionStorage.setItem('work-b-clock',String(window.__workBOffset));},{at,base:now});};
async function fit(){await p.evaluate(()=>new Promise(done=>requestAnimationFrame(()=>requestAnimationFrame(done))));const box=await p.locator('.regional-screen').evaluate(el=>{const r=el.getBoundingClientRect(),g=document.querySelector('#game').getBoundingClientRect(),b=el.querySelector('.regional-body');return {inside:r.top>=g.top-1&&r.bottom<=g.bottom+1&&r.left>=g.left-1&&r.right<=g.right+1,overflow:b.scrollWidth-b.clientWidth};});assert.ok(box.inside,'regional sheet fits game');assert.ok(box.overflow<2,'regional paper has no horizontal overflow');}
async function team(keys){
  const state=await read();
  for(let i=0;i<3;i++){await p.locator(`[data-regional-slot="${i}"]`).click();if(await p.locator('[data-regional-remove]').count())await p.locator('[data-regional-remove]').click();else await p.locator('[data-regional-pick-back]').click();}
  for(const [i,key] of keys.entries()){await p.locator(`[data-regional-slot="${i}"]`).click();await p.locator(`[data-regional-member="${key}"]`).click();}
  void state;
}
async function trip({place,focus,members}){
  await open();await tab('trip');await p.locator(`[data-regional-place="${place}"]`).click();await p.locator(`[data-regional-focus="${focus}"]`).click();
  if(members)await team(members);
  await p.locator('[data-regional-depart]').click();await p.locator('[data-journey-confirm]').click();
  const s=await read();assert.equal(s.progress.trip.status,'running');
  await jump(s.progress.trip.endAt+1000);await p.locator('[data-regional-claim]').waitFor();await p.locator('[data-regional-claim-notes]').waitFor({state:'attached'});
  const back=await read();const result=back.progress.trip.regional.result;
  if(await p.locator('[data-regional-claim]').count()){await p.locator('[data-regional-claim]').click();await p.waitForTimeout(300);if(await p.locator('#dialog-layer [data-yes]').count())await p.locator('#dialog-layer [data-yes]').click();}
  if((await read()).progress.trip.status==='returned'){await p.locator('.regional-basket details summary').click();await p.locator('[data-regional-discard]').click();await p.locator('[data-yes]').click();}
  assert.equal((await read()).progress.trip.status,'settled');
  return result;
}
async function until(card,options,limit){for(let i=0;i<limit;i++){const r=await trip(options);if((await read()).expansion.discovery.cards[card])return i+1;void r;}throw Error(`${card} not found within ${limit} complete trips`);}
async function identify(id){await open();await tab('record');assert.equal(await p.locator(`[data-regional-identify="${id}"]`).count(),0);assert.ok(Object.hasOwn((await read()).expansion.discovery.identified,String(id)));}
async function buy(id,count=1){while(await p.locator('.screen-panel .close').count())await p.locator('.screen-panel .close').first().click();await p.getByRole('button',{name:'厨房',exact:true}).click();await p.getByRole('button',{name:'补给 · 小卖部',exact:true}).click();await p.locator('[data-shop-tab="1"]').click();await p.locator(`[data-shop-ingredient-details="${id}"]`).click();for(let i=0;i<count;i++){await p.locator(`[data-shop-buy-ingredient="${id}"]`).click();await p.locator('[data-yes]').click();}await p.getByRole('button',{name:'厨房',exact:true}).click();}
// A careful player cleans before a trial batch; dirt may otherwise turn the scheduled egg sick.
async function tidy(){await p.locator('[data-control-id="clean"]').click();const yes=p.locator('[data-yes]');if(await yes.isEnabled())await yes.click();else await p.locator('[data-no]').click();}
async function cook(toolId){await tidy();await p.locator(`[data-control-id="tool:${toolId}"]`).click();await p.locator('.cooking-dialog').waitFor();const text=await p.locator('.cooking-dialog').innerText();await p.locator('[data-yes]').click();return text;}
async function harvest(){const s=await read();await jump(s.batch.ends+1000);await p.waitForTimeout(3300);for(let i=0;i<24;i++)await p.locator(`[data-control-id="egg:${i}"]`).press('Enter');assert.equal((await read()).batch.eggs.filter(e=>e.collected).length,24);}
try{
  const base=await new Promise((done,fail)=>{const timer=setTimeout(()=>fail(Error('server start timeout')),10000);server.once('error',fail);server.stdout.on('data',chunk=>{const m=String(chunk).match(/http:\/\/127\.0\.0\.1:\d+/);if(m){clearTimeout(timer);done(m[0]);}});});
  browser=await chromium.launch({headless:true,...(process.env.CHICK_QA_CHROME?{executablePath:process.env.CHICK_QA_CHROME}:{})});
  const context=await browser.newContext({viewport:{width:390,height:844},reducedMotion:'reduce'});
  await context.addInitScript(({seed,now})=>{if(location.protocol==='about:')return;window.__workBOffset=Number(sessionStorage.getItem('work-b-clock')??0);Date.now=()=>now+window.__workBOffset+Math.floor(performance.now());if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));},{seed,now});
  p=await context.newPage();p.on('pageerror',error=>errors.push(error.message));await p.goto(base+'/');await start();
  await open();const regions=await p.locator('[data-regional-region]').allInnerTexts();assert.ok(regions.length>=3,'valley, river and tea slope are listed (the bay joins in Work G, still route-locked here)');
  await p.locator('[data-regional-region="R"]').click();await fit();await screenshot('river-trip-390');
  checks.push(`寻访页地区列表：${regions.join('、')}（海湾在G开放，但本档未取得引路时仍锁定）`);
  assert.equal((await trip({place:'R:1',focus:'materials',members:['1:0']})).cardId,'R-S1','first river trip guarantees the executable entry specimen');
  await identify(77);await screenshot('river-identified');
  let s=await read();assert.ok(s.expansion.methods.directions.includes('REC-R-C1'));assert.ok(s.expansion.methods.directions.includes('REC-R-C5'));
  checks.push('溪岸首趟保证山柚标本（换地点/补材料也保证），免费辨认后出现山柚方向与旧料新品方向');
  await open();await p.locator('[data-journey-back]').click();await p.locator('[data-regional-region="T"]').click();
  assert.equal((await trip({place:'T:1',focus:'lore',members:['0:10']})).cardId,'T-S1');await identify(79);
  await tab('record');await p.locator('[data-regional-card="T-N2"] summary').click();const text=await p.locator('.regional-screen').innerText();
  assert.match(text,/先找到「[^」]+」对应的食材/,'T-N2 names the unfound osmanthus only by its riddle');
  await p.locator('[data-regional-method="REC-T-D1"]').click();const detail=await p.locator('[data-regional-method-detail]').innerText();
  assert.match(detail,/厨房 Lv\.4/);assert.match(detail,/烧水壶/);await screenshot('tea-kettle-lock');
  for(const [width,height] of [[320,568],[1280,900]]){await p.setViewportSize({width,height});await fit();await screenshot(`tea-record-${width}`);}
  await p.setViewportSize({width:390,height:844});
  checks.push('茶坡首标本焙香叶→辨认；未辨认的桂花只以卡片谜面提及；焙香奶茶鸭方向明确显示厨房Lv.4与烧水壶缺项，不凭空解锁');
  s=await read();assert.ok(!s.expansion.discovery.cards['T-N2']);
  // 2026-10-07 (batch 3): the 询问 are slips on the 生意 page; 略过 sits in each order's own sheet (its name opens it)
  await leaveRegional();await p.getByRole('button',{name:'生意',exact:true}).click();
  await p.locator('.business-screen .bh-orders').waitFor();s=await read();
  const fOrders=['O02','O03','O05','O06','O07','O08','O09','O10','O12'];const proposed=s.expansion.orders.proposals.map(x=>x.templateId);
  assert.ok(proposed.length>=1,'complete trips were real milestones');await screenshot('orders-after-trips');
  while(await p.locator('#panels .bh-main [data-order-detail^="proposal:"]').count()){await p.locator('#panels .bh-main [data-order-detail^="proposal:"]').first().click();await p.locator('#panels [data-order-skip]').click();await p.waitForTimeout(200);}
  assert.equal((await read()).expansion.orders.proposals.length,0,'skipping leaves the slots empty until the next milestone');
  await open();await p.locator('[data-regional-region="V"]').click();await trip({place:'V:0',focus:'materials',members:['0:0']});
  await leaveRegional();await p.getByRole('button',{name:'生意',exact:true}).click();await p.locator('.business-screen .bh-orders').waitFor();
  s=await read();const next=s.expansion.orders.proposals.find(x=>fOrders.includes(x.templateId));assert.ok(next,`an F template follows the rotation (${s.expansion.orders.proposals.map(x=>x.templateId)})`);
  const slip=p.locator(`#panels .bh-main .bh-order:has([data-order-detail="proposal:${next.id}"])`);await slip.waitFor();
  const def=(await import('../web/content-registry.js')).REGIONAL.orders.find(o=>o.id===next.templateId);
  assert.equal(await slip.locator('.bh-need').count(),def.kind==='display'?1:def.groups.length,'one need line per group');
  assert.equal(await slip.locator('.bh-act').count(),1,'one next step on the slip');await screenshot('f-order-slip');
  await slip.locator('[data-order-detail]').click();await p.locator('#panels .bh-sheet').waitFor();assert.ok((await p.locator('#panels .bh-request').innerText()).length>4,'the request text lives in the order sheet');await screenshot('f-order-sheet');
  checks.push(`完整寻访为采购里程碑（首轮 ${proposed.join('、')}）；略过后空位不补，下一趟归来按轮转出现F模板 ${next.templateId}：生意页订单卡只写需求进度、奖励和一个下一步，原文在订单详情`);
  assert.deepEqual(errors,[]);passed=true;await context.close();
}catch(error){if(p&&!p.isClosed()){await writeFile(resolve(output,'failure.txt'),await p.locator('body').innerText());await screenshot('failure');}throw error;}
finally{await browser?.close();server.kill();const report={checkedAt:new Date().toISOString(),passed,checks,screens,errors,isolatedProfile:true,acceleratedClock:true,realDeviceTest:false};await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));}
