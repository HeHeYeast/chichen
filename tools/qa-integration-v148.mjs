import {createRequire} from 'node:module';
import {resolve} from 'node:path';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
const {chromium}=createRequire(import.meta.url)(resolve(process.argv[2]));
const base=process.argv[3],out=resolve(process.argv[5]??'artifacts/qa/integration-ui');await mkdir(out,{recursive:true});
const browser=await chromium.launch({headless:true,executablePath:process.argv[4]}),errors=[],checks=[];
const read=p=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
async function boot(width,height){
 const s=E.freshState();s.cp=100000;s.kitchenLevel=3;s.toolLevels.fill(2);s.duck=true;s.progress.tutorialSeen=true;
 for(let i=0;i<89;i++){s.farm['0:'+i]=30;s.total['0:'+i]=100;}
 delete s.total['0:51'];delete s.total['0:52'];delete s.farm['0:51'];delete s.farm['0:52'];
 const c=await browser.newContext({viewport:{width,height},isMobile:true,hasTouch:true,reducedMotion:'reduce'});
 await c.addInitScript(s=>{window.__qaOffset=0;const real=Date.now.bind(Date);Date.now=()=>real()+window.__qaOffset;if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(s));},s);
 const p=await c.newPage();p.on('pageerror',e=>errors.push(e.message));await p.goto(base+'/');await p.getByRole('button',{name:'开始游戏',exact:true}).tap();await p.locator('[data-control-id="nav:0"][tabindex="0"]').waitFor();return {c,p};
}
async function fits(p,selector){const b=await p.locator(selector).evaluate(el=>{const r=el.getBoundingClientRect(),g=document.querySelector('#game').getBoundingClientRect();return {inside:r.left>=g.left-1&&r.right<=g.right+1&&r.top>=g.top-1&&r.bottom<=g.bottom+1,overflow:el.scrollWidth-el.clientWidth};});assert.ok(b.inside,selector+' fits');assert.ok(b.overflow<=1,selector+' no horizontal overflow');}
let active;
try{
 for(const [w,h] of [[320,568],[375,850],[430,932],[844,390]]){
  const {c,p}=await boot(w,h);active=p;console.log("VIEWPORT",w);
  await p.locator('.workshop-launch').tap();await fits(p,'.workshop-screen');
  const before=await read(p);await p.locator('[data-plan-start]').tap();await p.locator('[data-learn="CUL-A"]').tap();assert.deepEqual((await read(p)).progress.skills,before.progress.skills);
  await p.locator('[data-plan-cancel]').tap();await p.locator('[data-learn="CUL-A"]').tap();assert.equal((await read(p)).progress.skills['CUL-A'],1);
  await p.screenshot({path:resolve(out,`skills-${w}.png`)});
  await p.locator('[data-workshop-tab="story"]').tap();await p.locator('[data-accept="first-sale"]').tap();await p.locator('[data-order-quantity="first-sale"]').fill('12');await p.locator('[data-deliver="first-sale"]').tap();await p.locator('[data-yes]').tap();assert.equal((await read(p)).progress.orders['first-sale'].completed,true);await p.locator('[data-story-back]').tap();
  await p.locator('[data-workshop-tab="trip"]').tap();await p.locator('[data-member="0"]').selectOption('0:0');await fits(p,'.workshop-screen');await p.screenshot({path:resolve(out,`trip-${w}.png`)});
  await p.locator('[data-depart]').tap();await p.locator('[data-yes]').tap();let save=await read(p);assert.equal(save.progress.trip.status,'running');const stock=save.farm['0:0'];
  await p.reload();await p.getByRole('button',{name:'开始游戏',exact:true}).tap();await p.locator('[data-control-id="nav:0"][tabindex="0"]').waitFor();await p.locator('.workshop-launch').tap();await p.locator('[data-workshop-tab="trip"]').tap();await p.locator('[data-recall]').waitFor();
  await p.evaluate(()=>window.__qaOffset+=2*3600000+1000);await p.locator('[data-claim-trip]').waitFor();save=await read(p);assert.equal(save.progress.trip.status,'returned');assert.equal(save.farm['0:0'],stock);
  await p.locator('[data-claim-trip]').tap();await p.locator('[data-yes]').tap();assert.equal((await read(p)).progress.trip.status,'settled');
  await p.locator('.workshop-screen .close').tap();await p.locator('[data-control-id="tool:0"]').click();await fits(p,'.cooking-dialog');assert.ok((await p.locator('[data-preview-species]').count())>0);await p.screenshot({path:resolve(out,`candidates-${w}.png`)});
  await p.locator('[data-preview-species="0:51"],[data-preview-species="0:52"]').first().click();assert.ok(!(await p.locator('.workshop-screen').innerText()).includes('火凤凰'));await fits(p,'.workshop-screen');await p.screenshot({path:resolve(out,`observation-${w}.png`)});
  await c.close();checks.push(w+'px: skills preview/learn, story settlement, trip reload/return/reward, candidate layout');
 }
 assert.deepEqual(errors,[]);console.log('Integration UI passed: '+checks.length+' viewport flows');
}catch(e){if(active){console.log('FAIL DOM',await active.locator('#dialog-layer').innerText());await active.screenshot({path:resolve(out,'failure.png')});}throw e;}finally{await writeFile(resolve(out,'report.json'),JSON.stringify({checks,errors,nativeDeviceTest:false},null,2));await browser.close();}
