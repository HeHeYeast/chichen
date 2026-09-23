import {createRequire} from 'node:module';
import {resolve} from 'node:path';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import * as E from '../../web/engine.js';
import * as P from '../../web/progression.js';
const {chromium}=createRequire(import.meta.url)(process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const out=resolve('artifacts/skill-polish');await mkdir(out+'/video',{recursive:true});
const s=E.freshState();s.cp=12580;s.kitchenLevel=3;s.toolLevels.fill(2);s.duck=true;s.progress.tutorialSeen=true;s.sound=false;s.music=false;
for(let i=0;i<89;i++){s.farm['0:'+i]=3;s.total['0:'+i]=100;}
P.learnSkill(s,'CUL-1');E.startBatch(s,0,Date.now()-3*3600000,()=>0);
E.updateBatch(s,Date.now()-3500);E.updateBatch(s,Date.now()-1000);E.updateBatch(s,Date.now());
const browser=await chromium.launch({headless:true});
try{
 const c=await browser.newContext({viewport:{width:390,height:844},recordVideo:{dir:out+'/video',size:{width:390,height:844}}});
 await c.addInitScript(s=>localStorage.setItem('chick-kitchen-v1',JSON.stringify(s)),s);
 const p=await c.newPage();await p.goto('http://127.0.0.1:4197');await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.waitForTimeout(900);
 await p.locator('[data-control-id^="egg:"]').first().press('Enter');
 const animation=await p.locator('.skill-feedback-emblem svg').evaluate(el=>({name:getComputedStyle(el).animationName,duration:getComputedStyle(el).animationDuration,pointerEvents:getComputedStyle(el.closest('.skill-toast')).pointerEvents}));
 assert.equal(animation.name,'skill-stamp');assert.equal(animation.duration,'0.38s');assert.equal(animation.pointerEvents,'none');
 await p.waitForTimeout(1200);await p.locator('[data-control-id^="egg:"]').first().press('Enter');await p.waitForTimeout(3000);
 const cp=await p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')).cp);assert.equal(cp,s.cp+4);
 await c.close();await p.video().saveAs(out+'/trigger-demo.webm');await writeFile(out+'/animation-report.json',JSON.stringify({...animation,twoPickupsCP:4,passed:true},null,2));
 console.log('Recorded real animation: 380ms stamp, pointer-events none, exactly +4 CP for two golden pickups.');
}finally{await browser.close();}
