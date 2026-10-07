// Read-only UI evidence from the current production entry point, in an isolated save.
import {createRequire} from 'node:module';
import {mkdir,writeFile} from 'node:fs/promises';
import * as E from '../../web/engine.js';

const require=createRequire(import.meta.url);
const {chromium}=require(process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const out='docs/kitchen-farm-audit-20260928/evidence';
await mkdir(out,{recursive:true});
const browser=await chromium.launch({headless:true,executablePath:'C:/Program Files/Google/Chrome/Application/chrome.exe'});
const context=await browser.newContext({viewport:{width:390,height:844},deviceScaleFactor:1,reducedMotion:'reduce'});
const now=Date.now();
const state=E.freshState(now,12345);
state.cp=1240;
state.farm['0:0']=3;
state.total['0:0']=3;
E.startBatch(state,0,now,()=>0.5,()=>0.5);
await context.addInitScript(s=>localStorage.setItem('chick-kitchen-v1',JSON.stringify(s)),state);
const page=await context.newPage();
async function start(target='kitchen'){
  await page.goto('http://127.0.0.1:4173/');
  await page.getByRole('button',{name:'开始游戏',exact:true}).click();
  await page.waitForTimeout(650);
  if(target==='farm')await page.locator('[data-control-id="nav:1"]').click();
  await page.waitForTimeout(180);
}
async function controls(){return page.locator('#controls .hotspot,#quick-actions button,#main-nav button').evaluateAll(nodes=>nodes.filter(n=>!n.hidden).map(n=>({id:n.dataset.controlId,label:n.getAttribute('aria-label')||n.textContent.trim(),disabled:n.disabled,rect:(()=>{const r=n.getBoundingClientRect();return {x:Math.round(r.x),y:Math.round(r.y),w:Math.round(r.width),h:Math.round(r.height)};})()})));}
async function inspect(id,scene,scroll=false){
  await start(scene);
  if(scroll)for(let i=0;i<3;i++){await page.mouse.move(350,425);await page.mouse.down();await page.mouse.move(90,425,{steps:8});await page.mouse.up();}
  const target=page.locator(`[data-control-id="${id}"]`);
  const entry={id,scene,visible:await target.count()>0};
  if(!entry.visible)return entry;
  await target.click();await page.waitForTimeout(100);
  entry.heading=await page.locator('#panels .panel h2,#dialog-layer .confirm-title').allTextContents();
  entry.panelText=(await page.locator('#panels,#dialog-layer').allTextContents()).join(' ').replace(/\s+/g,' ').slice(0,360);
  entry.page=await page.locator('#main-nav [aria-current="page"]').allTextContents();
  entry.controls=await page.locator('#panels button,#dialog-layer button').allTextContents();
  if(['supply','workshop','farm:stall','farm:fortune','farm:shrine','farm:display','help'].includes(id)){
    entry.panelText='[Audit records the destination only; collection content omitted.]';
    entry.controls=[];
  }
  return entry;
}
await start();
await page.screenshot({path:`${out}/kitchen-390x844.png`});
const kitchenControls=await controls();
await start('farm');
await page.screenshot({path:`${out}/farm-left-390x844.png`});
const farmLeftControls=await controls();
await page.mouse.move(350,425);await page.mouse.down();await page.mouse.move(90,425,{steps:8});await page.mouse.up();
await page.screenshot({path:`${out}/farm-middle-390x844.png`});
const farmMiddleControls=await controls();
for(let i=0;i<2;i++){await page.mouse.move(350,425);await page.mouse.down();await page.mouse.move(90,425,{steps:8});await page.mouse.up();}
await page.waitForTimeout(250);
await page.screenshot({path:`${out}/farm-right-390x844.png`});
const farmRightControls=await controls();
const cases=[['ingredient','kitchen'],['level','kitchen'],['clean','kitchen'],['tool:0','kitchen'],['supply','kitchen'],['inventory','kitchen'],['workshop','kitchen'],['help','kitchen'],['alarm','kitchen'],['farm:house','farm'],['farm:stall','farm'],['farm:harvest','farm'],['farm:fortune','farm'],['farm:repair','farm'],['farm:shrine','farm',true],['farm:display','farm',true]];
const inspected=[];for(const [id,scene,scroll] of cases)inspected.push(await inspect(id,scene,scroll));
await writeFile(`${out}/runtime-inspection.json`,JSON.stringify({capturedAt:new Date().toISOString(),viewport:'390x844',source:'current /web/index.html; isolated localStorage',kitchenControls,farmLeftControls,farmMiddleControls,farmRightControls,inspected},null,2));
console.log(`Captured Kitchen and both Farm sides; inspected ${inspected.length} entries.`);
await browser.close();
