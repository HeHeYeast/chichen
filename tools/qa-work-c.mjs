// Work C playable acceptance through real interface actions. Only the boot
// fixture (a legitimate mid-game legacy save) and the clock are controlled.
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
const {chromium}=require(packagePath),output=resolve(root,'artifacts/qa/work-c');await mkdir(output,{recursive:true});
const now=1800000000000,seed=freshState(now,20260923);
// Mid-game legacy save: kitchen Lv.2, duck bought, pan/boil/oven/keep-warm/steamer.
const OLD=['0:0','0:3','0:4','0:8','0:17','0:18','0:114','0:115','0:116','1:0','1:3','1:6'];
seed.kitchenLevel=1;seed.duck=true;seed.toolLevels=[0,0,1,-1,0,-1,-1,-1,0];seed.cp=20000;
seed.total=Object.fromEntries(OLD.map((k,i)=>[k,i?20:200]));seed.farm=Object.fromEntries(OLD.map(k=>[k,6]));
for(let i=20;i<32;i++)seed.total['0:'+i]=1;
seed.ingredients={};seed.progress.orders['first-sale']={accepted:true,completed:true,choice:'0:0',delivered:12};seed.progress.sources=earnedSources(seed);seed.progress.tutorialSeen=true;
const checks=[],errors=[],screens=[];let browser,p,passed=false;
const server=spawn(process.execPath,['server.mjs','0'],{cwd:root,stdio:['ignore','pipe','pipe'],windowsHide:true});
const read=()=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
const screenshot=async name=>{await p.waitForTimeout(120);await p.screenshot({path:resolve(output,name+'.png')});screens.push(name);};
const start=async()=>{await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.locator('.workshop-launch').waitFor();await p.waitForTimeout(650);};
const open=async()=>{if(await p.locator('.regional-screen').count())return;if(await p.locator('.screen-panel .close').count())await p.locator('.screen-panel .close').first().click();await p.getByRole('button',{name:'寻访',exact:true}).click();await p.locator('.regional-screen').waitFor();};
const tab=async name=>{await p.locator(`[data-regional-tab="${name}"]`).click();};
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
  await p.locator('[data-regional-depart]').click();await p.locator('[data-yes]').click();
  const s=await read();assert.equal(s.progress.trip.status,'running');
  await jump(s.progress.trip.endAt+1000);await p.locator('[data-regional-claim-notes]').waitFor();
  const back=await read();const result=back.progress.trip.regional.result;
  if(await p.locator('[data-regional-claim]').count()){await p.locator('[data-regional-claim]').click();await p.locator('[data-yes]').click();}
  if((await read()).progress.trip.status==='returned'){await p.locator('.regional-basket details summary').click();await p.locator('[data-regional-discard]').click();await p.locator('[data-yes]').click();}
  assert.equal((await read()).progress.trip.status,'settled');
  return result;
}
async function until(card,options,limit){for(let i=0;i<limit;i++){const r=await trip(options);if((await read()).expansion.discovery.cards[card])return i+1;void r;}throw Error(`${card} not found within ${limit} complete trips`);}
async function identify(id){await open();await tab('record');await p.locator(`[data-regional-identify="${id}"]`).click();assert.ok(Object.hasOwn((await read()).expansion.discovery.identified,String(id)));}
async function buy(id,count=1){while(await p.locator('.screen-panel .close').count())await p.locator('.screen-panel .close').first().click();await p.getByRole('button',{name:'厨房',exact:true}).click();await p.getByRole('button',{name:'补给 · 小卖部',exact:true}).click();await p.locator('[data-shop-tab="1"]').click();for(let i=0;i<count;i++){await p.locator(`[data-shop-buy-ingredient="${id}"]`).click();await p.locator('[data-yes]').click();}await p.getByRole('button',{name:'厨房',exact:true}).click();}
// A careful player cleans before a trial batch; dirt may otherwise turn the scheduled egg sick.
async function tidy(){await p.locator('[data-control-id="clean"]').click();const yes=p.locator('[data-yes]');if(await yes.isEnabled())await yes.click();else await p.locator('[data-no]').click();}
async function cook(toolId){await tidy();await p.locator(`[data-control-id="tool:${toolId}"]`).click();await p.locator('.cooking-dialog').waitFor();const text=await p.locator('.cooking-dialog').innerText();await p.locator('[data-yes]').click();return text;}
async function harvest(){const s=await read();await jump(s.batch.ends+1000);await p.waitForTimeout(3300);for(let i=0;i<24;i++)await p.locator(`[data-control-id="egg:${i}"]`).press('Enter');assert.equal((await read()).batch.eggs.filter(e=>e.collected).length,24);}
try{
  const base=await new Promise((done,fail)=>{const timer=setTimeout(()=>fail(Error('server start timeout')),10000);server.once('error',fail);server.stdout.on('data',chunk=>{const m=String(chunk).match(/http:\/\/127\.0\.0\.1:\d+/);if(m){clearTimeout(timer);done(m[0]);}});});
  browser=await chromium.launch({headless:true,...(process.env.CHICK_QA_CHROME?{executablePath:process.env.CHICK_QA_CHROME}:{})});
  const context=await browser.newContext({viewport:{width:390,height:844},reducedMotion:'reduce'});
  await context.addInitScript(({seed,now})=>{window.__workBOffset=Number(sessionStorage.getItem('work-b-clock')??0);Date.now=()=>now+window.__workBOffset+Math.floor(performance.now());if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));},{seed,now});
  p=await context.newPage();p.on('pageerror',error=>errors.push(error.message));await p.goto(base+'/');await start();
  await open();await tab('record');await fit();
  let html=await p.locator('.regional-screen').innerHTML();
  for(const name of ['荠菜煎饼鸡','麦芽脆片鸡','麦穗冠鸡','谷粒斑鸭'])assert.ok(!html.includes(name),`unknown ${name} is masked`);
  for(const code of ['C129','C134','D066','D071'])assert.ok(html.includes(code));
  await screenshot('record-unknown-390');
  for(const [width,height] of [[320,568],[1280,900]]){await p.setViewportSize({width,height});await fit();await screenshot(`record-unknown-${width}`);}
  await p.setViewportSize({width:390,height:844});
  checks.push('中期旧档打开谷地册：6卡谜面、12编号剪影，名称/配方不泄露；320/390/1280无横溢');

  // First specimen is guaranteed, even on the grain-shed place and material focus.
  assert.equal((await trip({place:'V:1',focus:'materials',members:['0:0']})).cardId,'V-S1');
  await identify(75);await screenshot('identified-75');
  const maltTrips=await until('V-S2',{place:'V:1',focus:'specimen'},4);await identify(76);
  checks.push(`首趟保证荠菜标本；麦芽标本在${maltTrips}趟内取得（4趟保护，Lv.2+烤箱+面粉资格），两份材料免费辨认`);
  // Two eligible cards share this place/focus: each is guaranteed within 4 trips of the previous one.
  const loreTrips=await until('V-N2',{place:'V:1',focus:'lore',members:['0:18']},8);
  const eventTrips=(await read()).expansion.discovery.cards['V-E2']?0:await until('V-E2',{place:'V:1',focus:'lore',members:['0:18']},4);
  let s=await read();assert.ok(s.expansion.methods.full.includes('ALT-V'),'grain event teaches ALT-V');
  assert.ok(s.expansion.methods.directions.includes('REC-V-C6')&&s.expansion.methods.directions.includes('REC-V-D6'),'grain-shed lore opens ornamental directions');
  checks.push(`谷棚两张卡共${loreTrips+eventTrips}趟取得（谷物+菜园由同一伙伴满足）；ALT-V与两种观赏方向登记`);
  await open();await tab('record');await screenshot('record-directions');
  const full=s.expansion.methods.full.filter(id=>id.startsWith('REC-V-'));
  assert.ok(full.length>=1,'free method trips completed at least one pinned method');
  checks.push(`免费方法随真实完整寻访补全：${full.join('、')}`);

  // ALT-V: prepare from the region page, cook in the oven, old target identity only.
  s=await read();if(!(s.ingredients[76]>0))await buy(76);
  await open();await tab('record');await p.locator('[data-regional-alt-prepare="ALT-V"]').click();
  s=await read();assert.deepEqual(s.expansion.prepareMode,{kind:'local-alternative',recipeId:'ALT-V'});
  let text=await cook(4);assert.match(text,/地方替代做法/);assert.match(text,/不额外安排目标/);
  s=await read();assert.equal(s.batch.plan.mode,'local-alternative');assert.ok(s.batch.eggs.every(e=>e.id<128),'no new species inserted');assert.equal(s.expansion.prepareMode,undefined);
  await screenshot('alt-v-batch');await harvest();
  checks.push('ALT-V真实准备→烤箱开火：确认页说明替代模式，24只全为旧身份、无新增保底，准备模式随批次消费');


  // Regional trial for the first full method, until the species is actually collected.
  const target=full[0];
  const recipe=(await import('../web/content-registry.js')).REGIONAL.recipes.find(r=>r.id===target);
  let batches=0;
  while(!(await read()).total[recipe.key]&&batches<4){
    for(const i of recipe.ingredients){const st=await read();if(!(st.ingredients[i.id]>0))await buy(i.id);}
    await open();await tab('record');await p.locator(`[data-regional-method="${target}"]`).click();await p.locator('[data-regional-prepare]').click();
    s=await read();assert.equal(s.expansion.prepareMode.recipeId,target);
    text=await cook(recipe.toolId);assert.match(text,/地区试做/);
    s=await read();if(recipe.toolId===8)assert.ok(s.batch.plan.initialIds.filter(id=>id!==Number(recipe.key.split(':')[1])).every(id=>[114,115].includes(id)||id<128));
    await harvest();batches++;
  }
  s=await read();assert.ok(s.total[recipe.key]>=1,`${target} collected within four clean batches`);
  await open();await tab('record');await p.locator(`[data-regional-method="${target}"]`).click();
  html=await p.locator('.regional-screen').innerHTML();assert.match(await p.locator('.regional-screen').innerText(),/已收录/);
  await screenshot('first-valley-collected');
  checks.push(`${target}按已知方向准备并在${batches}批内实收；收录后名称出现、再次准备为复刻`);

  // Pin another method, earn it by complete trips, then compare regional and ordinary steamer modes.
  async function earn(recipeId){await open();await tab('record');await p.locator(`[data-regional-method="${recipeId}"]`).click();if(await p.locator('[data-regional-pin]').count())await p.locator('[data-regional-pin]').click();
    for(let i=0;i<3&&!(await read()).expansion.methods.full.includes(recipeId);i++)await trip({place:'V:0',focus:'materials',members:['0:0']});
    assert.ok((await read()).expansion.methods.full.includes(recipeId),recipeId+' earned by three complete trips');}
  async function stock(ids){for(const id of ids){if(!((await read()).ingredients[id]>0))await buy(id);}}
  await earn('REC-V-C3');await stock([75,9]);
  await open();await tab('record');await p.locator('[data-regional-method="REC-V-C3"]').click();await p.locator('[data-regional-prepare]').click();
  await tidy();await p.locator('[data-control-id="tool:8"]').click();await p.locator('.cooking-dialog').waitFor();
  assert.match(await p.locator('[data-cook-mode]').innerText(),/地区试做/);
  const regionalBadge=await p.locator('.cooking-dialog [data-preview-species="0:115"] small').first().evaluate(e=>e.textContent);
  assert.equal(regionalBadge,'可能出现','regional steamer lists shaomai only as a weighted companion');
  await screenshot('steamer-regional-confirm');await p.locator('[data-yes]').click();
  s=await read();assert.equal(s.batch.plan.mode.startsWith('regional'),true);assert.ok(s.batch.plan.initialIds.every(id=>[114,115,130].includes(id)));await harvest();
  await stock([9]);await p.locator('[data-control-id="ingredient"]').click();await p.locator('.ingredients-grid [data-id="9"]').click();await p.locator('[data-ok]').click();
  await p.locator('[data-control-id="tool:8"]').click();await p.locator('.cooking-dialog').waitFor();
  assert.equal(await p.locator('[data-cook-mode]').count(),0,'ordinary mode shows no regional banner');
  assert.equal(await p.locator('.cooking-dialog [data-preview-species="0:115"] small').first().evaluate(e=>e.textContent),'已安排1只','ordinary steamer keeps its per-recipe guarantee');
  await screenshot('steamer-ordinary-confirm');await p.locator('[data-no]').click();
  checks.push('V-C3以真实寻访补全后蒸笼开火：地区模式只用旧加权伴随（114/115）、无旧保证插入；改回普通面粉蒸笼，确认页仍显示烧麦鸡“已安排1只”');

  await earn('REC-V-C6');let tries=0;
  while(!(await read()).total['0:133']&&tries<4){await stock([76,0]);await open();await tab('record');await p.locator('[data-regional-method="REC-V-C6"]').click();await p.locator('[data-regional-prepare]').click();await cook(0);await harvest();tries++;}
  assert.ok((await read()).total['0:133']>=1,'ornamental collected within four clean batches');
  await open();await tab('trip');await p.locator('[data-regional-slot="0"]').click();
  if(await p.locator('[data-regional-keep]').isChecked())await p.locator('[data-regional-keep]').click();
  assert.equal(await p.locator('[data-regional-member="0:133"]').count(),1,'ornamental can be chosen as a companion');await screenshot('ornamental-companion');
  await p.locator('[data-regional-member="0:133"]').click();
  while(await p.locator('.screen-panel .close').count())await p.locator('.screen-panel .close').first().click();
  await p.getByRole('button',{name:'生意',exact:true}).click();await p.locator('[data-trade-view="business"]').first().click();await p.locator('.business-screen, [data-business-filter]').first().waitFor();
  if(await p.locator('[data-business-filter="all"]').count())await p.locator('[data-business-filter="all"]').click();
  assert.equal(await p.locator('[data-business-quantity="0:133"]').count(),0,'ornamental never appears as business stock');await screenshot('business-edible-only');
  checks.push(`麦穗冠鸡C134在${tries}批内实收：可在寻访配队中选择，营业备货列表（含全部食用出品）不出现`);
  await p.reload();await start();
  assert.deepEqual(errors,[]);passed=true;await context.close();
}catch(error){if(p&&!p.isClosed()){await writeFile(resolve(output,'failure.txt'),await p.locator('body').innerText());await screenshot('failure');}throw error;}
finally{await browser?.close();server.kill();const report={checkedAt:new Date().toISOString(),passed,checks,screens,errors,isolatedProfile:true,acceleratedClock:true,realDeviceTest:false};await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));}
