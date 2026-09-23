import {createRequire} from 'node:module';
import {mkdir,writeFile} from 'node:fs/promises';
import * as E from '../../web/engine.js';
const {chromium}=createRequire(import.meta.url)(process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const out=new URL('./screens/',import.meta.url);await mkdir(out,{recursive:true});
const browser=await chromium.launch({headless:true}),report={at:new Date().toISOString(),screens:[],errors:[],actions:[]};
let c=await browser.newContext({viewport:{width:390,height:844},isMobile:true,hasTouch:true,reducedMotion:'reduce'});
let p=await c.newPage();p.on('pageerror',e=>report.errors.push(e.message));
async function shot(name){await p.screenshot({path:new URL(name+'.png',out).pathname.replace(/^\/([A-Z]:)/,'$1')});report.screens.push({name,text:await p.locator('body').innerText(),controls:await p.locator('button,input,select').evaluateAll(es=>es.map(e=>({text:e.textContent,aria:e.getAttribute('aria-label'),data:{...e.dataset},disabled:e.disabled}))) });}
try{
 await p.goto('http://127.0.0.1:4185/');await p.getByRole('button',{name:'开始游戏',exact:true}).waitFor();await shot('01-title');await p.getByRole('button',{name:'开始游戏',exact:true}).tap();await p.locator('.workshop-launch').waitFor();await p.waitForTimeout(650);await shot('02-new-kitchen');
 await p.locator('.workshop-launch').tap();await shot('03-new-skills');
 const s=E.freshState();s.cp=12580;s.kitchenLevel=2;s.toolLevels=[2,2,2,2,2,2,-1,-1,1];s.duck=true;s.progress.tutorialSeen=true;
 for(let i=0;i<65;i++){s.farm['0:'+i]=30;s.total['0:'+i]=35;}delete s.total['0:51'];delete s.farm['0:51'];
 s.ingredients={0:4,1:3,8:2,13:2,16:2};
 await c.close();c=await browser.newContext({viewport:{width:390,height:844},isMobile:true,hasTouch:true,reducedMotion:'reduce'});await c.addInitScript(s=>{if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(s));},s);p=await c.newPage();p.on('pageerror',e=>report.errors.push(e.message));await p.goto('http://127.0.0.1:4185/');await p.getByRole('button',{name:'开始游戏',exact:true}).tap();await p.locator('.workshop-launch').waitFor();await p.waitForTimeout(650);
 await p.locator('.workshop-launch').tap();await shot('04-skills-cooking');
 for(const b of ['HOME','TRADE','OBS','TRIP']){await p.locator('[data-branch="'+b+'"]').tap();await shot('05-skills-'+b.toLowerCase());}
 await p.locator('[data-plan-start]').tap();await p.locator('[data-branch="CUL"]').tap();await p.locator('[data-learn="CUL-A"]').tap();await shot('06-skill-trial');await p.locator('[data-plan-cancel]').tap();
 await p.locator('[data-workshop-tab="story"]').tap();await shot('07-story');await p.locator('[data-accept="first-sale"]').tap();await shot('08-order-delivery');await p.locator('[data-order-quantity="first-sale"]').fill('12');await p.locator('[data-deliver="first-sale"]').tap();await shot('09-order-confirm');await p.locator('[data-yes]').tap();await shot('10-story-reading');await p.locator('[data-story-back]').tap();
 await p.locator('[data-workshop-tab="trip"]').tap();await shot('11-trip-empty');await p.locator('[data-member="0"]').selectOption('0:0');await p.locator('[data-member="1"]').selectOption('0:3');await p.locator('[data-member="2"]').selectOption('0:8');await shot('12-trip-team');await p.locator('.workshop-body').evaluate(e=>e.scrollTop=e.scrollHeight);await shot('13-trip-outlook');await p.locator('[data-depart]').tap();await shot('14-trip-confirm');await p.locator('[data-yes]').tap();await shot('15-trip-running');await p.locator('[data-recall]').tap();await p.locator('[data-yes]').tap();
 await p.locator('.workshop-screen .close').tap();await p.locator('[data-control-id="tool:0"]').tap();await shot('16-cook-preview');await p.locator('[data-preview-species="0:51"]').tap();await shot('17-observation');
 await p.setViewportSize({width:320,height:568});await shot('18-observation-320');await p.locator('[data-observe-back]').tap();await shot('19-preview-320');
 await p.setViewportSize({width:390,height:844});
 for(const [scene,name] of [['stage1','20-kitchen-stage1'],['stage4','21-kitchen-stage4'],['v8-farm','22-farm'],['v8-collection','23-collection'],['v8-detail','24-species-detail'],['v14-harvest','25-sell'],['v8-shop','26-shop-tools'],['v8-shop-ingredients','27-shop-ingredients'],['v10-duck','28-shop-service'],['v14-recipes','29-recipes'],['v14-detail','30-recipe-detail'],['v14-unknown','31-unknown'],['v11-draw','32-shrine'],['v11-book','33-sign-book'],['v10-activities','34-activities'],['v12-calendar','35-calendar'],['v12-rewards','36-seasonal'],['v8-settings','37-settings'],['v8-manual','38-help'],['v11-ingredients','39-ingredients'],['upgrade-ready','40-upgrade'],['v8-farm-damaged','41-farm-damaged']]){
  await p.goto('http://127.0.0.1:4185/?review=1');await p.getByRole('button',{name:'开始游戏',exact:true}).waitFor();await p.evaluate(scene=>window.postMessage({type:'chick-review',command:'scene',value:scene},location.origin),scene);await p.waitForTimeout(250);await shot(name);
 }
 report.actions.push('Started fresh game; inspected all five branches; trial allocation cancelled; first order accepted, delivered and chapter read; three-member team selected, departed and recalled; candidate opened observation; existing pages loaded through built-in isolated review fixtures.');
}finally{await writeFile(new URL('./capture-report.json',import.meta.url),JSON.stringify(report,null,2));await browser.close();}


