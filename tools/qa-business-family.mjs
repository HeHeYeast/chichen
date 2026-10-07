// Isolated runtime states, produced with existing game commands. Never user saves.
import {createRequire} from 'node:module';
import {spawn} from 'node:child_process';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {freshState,normalizeSave} from '../web/engine.js';
import {LEGACY193} from '../web/legacy-content.js';
import {syncProgress} from '../web/progression.js';
import {reduceFacts} from '../web/facts.js';
import {openBusiness,advanceBusiness} from '../web/business.js';
import {orderMilestone,acceptProposal,deliverOrderGroups} from '../web/orders.js';
import {completeProjectStage,deliverProject} from '../web/projects.js';
import {reconcileProgress,readRegularStage} from '../web/regulars.js';
const require=createRequire(import.meta.url),{chromium}=require(process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const out='artifacts/business-family/states',now=1800000000000;await mkdir(out,{recursive:true});
function base(){const s=freshState(now,4242);s.kitchenLevel=2;s.duck=true;s.toolLevels=[0,0,0,0,0,0,0,0,0];s.cp=5000;s.progress.tutorialSeen=true;
  s.total=Object.fromEntries(LEGACY193.characters.flatMap((list,egg)=>list.map(c=>[`${egg}:${c.id}`,c.id===0&&egg===0?9000:2])));
  for(const k of ['0:134','0:135','0:136','1:71'])s.total[k]=1;
  s.farm={'0:0':40,'0:3':30,'0:4':20,'0:8':10,'1:0':20};s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};syncProgress(s);
  const n=normalizeSave(s,now);reduceFacts(n,['MN1','MN3'].map((menuId,i)=>({kind:'businessWitness',sessionId:`business-${i+500}`,menuId,soldByKey:{'0:0':6},roleSales:{},fullSoldByKey:{},fullRoleSales:{},valid:true,complete:false})));
  for(const id of ['R-S1','R-S2'])n.expansion.discovery.cards[id]=++n.meta.factSeq;for(const id of ['77','78'])n.expansion.discovery.identified[id]=++n.meta.factSeq;
  n.expansion.regions.guideFlags=['GUIDE-B'];return n;}
const states={};states.locked=normalizeSave(freshState(now,6),now);states.locked.progress.tutorialSeen=true;states.empty=base();
const trading=base();openBusiness(trading,{menuId:'MN1',stock:{'0:0':9,'0:3':4,'1:0':5}},now-7200000);advanceBusiness(trading,now);states.trading=trading;
const zero=base();openBusiness(zero,{menuId:'MN1',stock:{'0:0':6,'0:3':6}},now);states.zero=zero;
const settled=base();openBusiness(settled,{menuId:'MN1',stock:{'0:0':6,'0:3':6}},now-14400000);advanceBusiness(settled,now);reconcileProgress(settled);states.settled=settled;
const orders=base();orderMilestone(orders,now,'batch');const a=acceptProposal(orders,orders.expansion.orders.proposals[0].id,{},now);deliverOrderGroups(orders,a.id,[{groupId:a.groups[0].id,key:'0:0',quantity:6}],now);orderMilestone(orders,now,'batch');states.orders=structuredClone(orders);
deliverOrderGroups(orders,a.id,[{groupId:a.groups[0].id,key:'0:3',quantity:6}],now);reconcileProgress(orders);states.orderDone=structuredClone(orders);
const full=base();for(let i=0;i<3;i++){orderMilestone(full,now,'batch');if(i<2)acceptProposal(full,full.expansion.orders.proposals[0].id,{},now);}states.ordersFull=full;
const project=base();completeProjectStage(project,'PJ-2','PJ-2-A');states.project=structuredClone(project);
deliverProject(project,'PJ-2','PJ-2-B',{'0:3':6,'0:4':6},{choice:['0:3','0:4']});states.projectReady=structuredClone(project);completeProjectStage(project,'PJ-2','PJ-2-B');states.projectPoor=structuredClone(project);states.projectPoor.cp=0;completeProjectStage(project,'PJ-2','PJ-2-C');states.projectDone=project;
const done=base();reduceFacts(done,[{kind:'orderComplete',instanceId:'order-510',templateId:'O01',variantId:'O01-A',region:null,chapters:null,groupDeliveries:[]},...['MN2','MN6'].map((menuId,i)=>({kind:'businessWitness',sessionId:`business-${i+600}`,menuId,soldByKey:{'0:0':6},roleSales:{},fullSoldByKey:{},fullRoleSales:{},valid:true,complete:false}))]);reconcileProgress(done);for(let i=0;i<4;i++){readRegularStage(done,'RG1');reconcileProgress(done);}states.regularDone=done;
for(const [key,s]of Object.entries(states)){states[key]=normalizeSave(s,now);await writeFile(`${out}/${key}.json`,JSON.stringify(states[key],null,2));}
const server=spawn(process.execPath,['server.mjs','0'],{stdio:['ignore','pipe','pipe'],windowsHide:true});
const url=await new Promise((yes,no)=>{server.on('error',no);server.stdout.on('data',b=>{const m=String(b).match(/http:\/\/127\.0\.0\.1:\d+/);if(m)yes(m[0]);});});
let browser,page,context,passed=false;const errors=[],checks=[],screens=[];
async function load(key,width=390,height=844){await context?.close();context=await browser.newContext({viewport:{width,height},reducedMotion:'reduce'});await context.addInitScript(({state,now})=>{Date.now=()=>now+Math.floor(performance.now());if(location.protocol==='http:'&&!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(state));},{state:states[key],now});page=await context.newPage();page.on('pageerror',e=>errors.push(e.message));await page.goto(url);await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.waitForTimeout(750);await page.evaluate(()=>document.fonts.ready);await page.locator('#main-nav').getByRole('button',{name:'生意',exact:true}).click();}
async function nav(kind){if(await page.locator('.bs-dialog[open]').count())await page.locator('[data-business-sheet-close]').click();if(kind==='business'&&await page.locator('.golden-business').count())return;await page.locator(`[data-trade-view="${kind}"]`).first().click();}
async function shot(name){await page.evaluate(()=>document.fonts.ready);await page.waitForTimeout(100);const metrics=await page.locator('.screen-panel').evaluate(el=>({overflow:el.scrollWidth-el.clientWidth,missing:[...el.querySelectorAll('img')].filter(i=>!i.complete||!i.naturalWidth).map(i=>i.src)}));assert.ok(metrics.overflow<2,JSON.stringify(metrics));assert.deepEqual(metrics.missing,[]);await page.screenshot({path:`${out}/${name}.png`});screens.push(name);}
const save=()=>page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
async function projectDetail(id='PJ-2'){await nav('projects');await page.locator(`[data-project-open="${id}"]`).click();}
try{browser=await chromium.launch({headless:true});
  await load('trading');await shot('01-business');await page.locator('.bs-main-action').click();await shot('ledger-active');const before=await save();await page.locator('[data-shop-help]').click();await shot('ledger-help');await page.locator('[data-shop-help-close]').click();await page.locator('[data-business-ledger-back]').click();await page.locator('.bs-main-action').click();const after=await save();for(const k of ['cp','farm'])assert.deepEqual(after[k],before[k]);checks.push('active receipt is read-only; opening help/reopening never changes CP or farm');
  await load('orders');await nav('orders');assert.equal(await page.locator('.orders-card[data-order]').count(),1);assert.ok(await page.locator('.order-examples image').count()>0);await shot('02-orders');
  await page.locator('[data-order-deliver]').click();assert.equal(await page.locator('[data-orders-confirm]').isDisabled(),true);await shot('orders-delivery-disabled');await page.locator('[data-orders-back]').click();
  await load('ordersFull');await nav('orders');assert.equal(await page.locator('[data-proposal-accept]').first().isDisabled(),true);await shot('orders-at-capacity');
  await load('orderDone');await nav('orders');assert.match(await page.locator('.orders-screen').innerText(),/已办好 1 单/);await shot('orders-completed');await nav('regulars');await shot('03-regulars');await page.locator('[data-regular-open="RG1"]').click();await page.locator('[data-regular-read]').click();await shot('regulars-story');
  await load('empty');await nav('orders');await shot('orders-empty');await nav('regulars');await page.locator('[data-regular-open="RG1"]').click();assert.equal(await page.locator('[data-regular-read]').count(),0);await shot('regulars-no-story');
  await load('regularDone');await nav('regulars');await shot('regulars-completed-list');await page.locator('[data-regular-open="RG1"]').click();assert.equal(await page.locator('[data-regular-read]').count(),0);assert.match(await page.locator('.regulars-screen').innerText(),/四段都读完/);await shot('regulars-completed');
  await load('project');await projectDetail();await shot('04-projects');assert.equal(await page.locator('[data-project-complete="PJ-2-B"]').isDisabled(),true);assert.equal(await page.locator('[data-project-deliver="PJ-2-B"]').isEnabled(),true);
  await load('projectReady');await projectDetail();assert.equal(await page.locator('[data-project-complete="PJ-2-B"]').isEnabled(),true);await shot('projects-ready');
  await load('projectPoor');await projectDetail();assert.equal(await page.locator('[data-project-complete="PJ-2-C"]').isDisabled(),true);assert.match(await page.locator('.projects-screen').innerText(),/CP不足/);await shot('projects-insufficient-cp');
  await load('projectDone');await projectDetail();assert.equal(await page.locator('[data-project-complete]').count(),0);await shot('projects-completed');
  await load('zero');await page.locator('.bs-main-action').click();assert.match(await page.locator('.shop-ledger').innerText(),/还没有成交/);await shot('ledger-zero-sales');
  await load('settled');await shot('05-ledger');const bill=states.settled.expansion.business.lastReport;assert.match(await page.locator('.business-receipt-total').innerText(),new RegExp(String(bill.income)));assert.equal(bill.income,bill.baseCP+bill.markupCP+bill.themeCP+bill.bonusCP);await page.locator('[data-business-regulars="RG1"]').click();await page.locator('.regulars-screen').waitFor();checks.push('settled receipt matches engine sums and links to the actual visitor story');
  await load('locked');await shot('business-locked');await nav('orders');await shot('orders-locked');await nav('regulars');assert.equal(await page.locator('[data-regular-open]').count(),0);await shot('regulars-unknown');await nav('projects');await shot('projects-locked');await page.locator('[data-project-open]').first().click();assert.equal(await page.locator('[data-project-complete]').count(),0);await shot('projects-locked-detail');
  for(const width of [320,430,1280]){await load('orders',width,width===320?568:900);await nav('orders');await shot(`orders-${width}`);await nav('regulars');await shot(`regulars-${width}`);await projectDetail();await shot(`projects-${width}`);await load('settled',width,width===320?568:900);await shot(`ledger-${width}`);}
  checks.push('all five family pages: empty/unlocked/locked/actionable/disabled/completed states captured; portraits from live model; 320/390/430/1280 without overflow or missing assets');
  assert.deepEqual(errors,[]);passed=true;
}catch(e){if(page&&!page.isClosed()){await page.screenshot({path:`${out}/failure.png`});await writeFile(`${out}/failure.txt`,await page.locator('body').innerText());}throw e;}
finally{await browser?.close();server.kill();await writeFile(`${out}/report.json`,JSON.stringify({passed,physicalDevice:false,isolatedSave:true,checks,errors,screens},null,2));console.log(JSON.stringify({passed,checks,errors,screens},null,2));}
