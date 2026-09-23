import {createRequire} from 'node:module';
import {mkdir,writeFile} from 'node:fs/promises';
import * as E from '../../web/engine.js';
const {chromium}=createRequire(import.meta.url)(process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const out='artifacts/round2-implementation';
const browser=await chromium.launch({headless:true});
const s=E.freshState();s.cp=12580;s.kitchenLevel=2;s.toolLevels=[2,2,2,2,2,2,-1,-1,1];s.duck=true;s.progress.tutorialSeen=true;
for(let i=0;i<65;i++){s.farm['0:'+i]=30;s.total['0:'+i]=35;}delete s.total['0:51'];delete s.farm['0:51'];s.ingredients={0:4,1:3,8:2,13:2,16:2};
const c=await browser.newContext({viewport:{width:390,height:844},isMobile:true,hasTouch:true,reducedMotion:'reduce'});
await c.addInitScript(s=>{if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(s));},s);
const p=await c.newPage();p.on('pageerror',e=>console.log('ERROR',e.message));
await p.goto('http://127.0.0.1:4197/');await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.waitForTimeout(650);await p.locator('.workshop-launch').click();
await p.screenshot({path:out+'/iteration1-skills.png'});
await p.locator('[data-workshop-tab="trip"]').click();
for(const [i,k]of [[0,'0:0'],[1,'0:3'],[2,'0:8']]){await p.locator('[data-member-slot="'+i+'"]').click();await p.locator('[data-pick-member="'+k+'"]').click();}
await p.screenshot({path:out+'/iteration1-trip.png'});
await p.locator('[data-workshop-tab="story"]').click();await p.screenshot({path:out+'/iteration1-story.png'});
await p.setViewportSize({width:320,height:568});await p.locator('[data-workshop-tab="skills"]').click();await p.screenshot({path:out+'/iteration1-skills-320.png'});
console.log('Screenshots captured');await browser.close();


