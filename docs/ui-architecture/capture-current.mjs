import {createRequire} from 'node:module';
import {mkdir,writeFile} from 'node:fs/promises';
import * as E from '../../web/engine.js';
const require=createRequire(import.meta.url);const {chromium}=require(process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const browser=await chromium.launch({headless:true,executablePath:'C:/Program Files/Google/Chrome/Application/chrome.exe'});
const context=await browser.newContext({viewport:{width:320,height:568},reducedMotion:'reduce'});
const s=E.freshState();s.cp=12580;s.kitchenLevel=1;s.toolLevels.fill(1);s.duck=true;for(let i=0;i<20;i++){s.farm['0:'+i]=30;s.total['0:'+i]=100;}s.ingredients={0:4,1:3,8:2};
await context.addInitScript(s=>localStorage.setItem('chick-kitchen-v1',JSON.stringify(s)),s);
const p=await context.newPage();await p.goto('http://127.0.0.1:4173/');await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.waitForTimeout(700);
await mkdir('docs/ui-architecture/evidence',{recursive:true});
for(const [id,action] of [['kitchen',null],['skills',()=>p.locator('.workshop-launch').click()],['trip',()=>p.locator('[data-workshop-tab="trip"]').click()],['collection',()=>p.locator('[data-control-id="nav:4"]').press('Enter')],['farm',()=>p.locator('[data-control-id="nav:1"]').press('Enter')]]){if(action)await action();await p.screenshot({path:'docs/ui-architecture/evidence/current-'+id+'-320.png'});}
console.log('Captured 5 current UI pages in isolated browser context.');await browser.close();
