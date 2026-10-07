// Kitchen egg-nest captures for before/after review. Isolated browser saves only;
// usage: node tools/capture-kitchen-eggs.mjs <output-directory>
import {createRequire} from 'node:module';
import {spawn} from 'node:child_process';
import {mkdir} from 'node:fs/promises';
import * as E from '../web/engine.js';
const require=createRequire(import.meta.url),{chromium}=require(process.env.PLAYWRIGHT_MODULE??process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const out=process.argv[2];if(!out)throw Error('usage: node tools/capture-kitchen-eggs.mjs <output-directory>');
const at=1801224000000;await mkdir(out,{recursive:true});
function seed(level,mode){
 const s=E.freshState(at,29);s.cp=1280;s.kitchenLevel=level;s.toolLevels=Array(9).fill(0);s.ingredients={0:4,1:4};s.progress.tutorialSeen=true;s.total={'0:0':24,'1:0':3};s.music=false;s.sound=false;s.duck=true;
 s.egg=mode==='duck'?1:0;s.selected=[0];E.startBatch(s,2,at-143000,()=>.5,()=>.5);
 const species=[0,8,9,10,11,29,37,0,8,10];
 s.batch.eggs.forEach((e,i)=>{e.openAt=at+3457000;e.blackAt=at+12000000;e.id=0;
  if(mode==='ready'){e.status='ready';e.animationAt=at-5000;e.openAt=at-10000;e.id=species[i%species.length];}
  if(mode==='partial'&&i%3===0){e.status='ready';e.animationAt=at-5000;e.openAt=at-10000;e.id=species[i%species.length];}});
 s.selected=[1];s.lastClean=at-36*3600000*.5;s.cleanCycle.dirtyAt=at+36*3600000*.5;return s;
}
const server=spawn(process.execPath,['server.mjs','0'],{stdio:['ignore','pipe','pipe'],windowsHide:true});
const base=await new Promise((ok,no)=>{server.on('error',no);server.stdout.on('data',b=>{const m=String(b).match(/http:\/\/127\.0\.0\.1:\d+/);if(m)ok(m[0]);});});
const browser=await chromium.launch({headless:true});const errors=[];
try{
 for(const [w,h,tag] of [[390,844,''],[320,568,'-320']])for(let level=0;level<4;level++)for(const mode of ['eggs','partial','duck']){
  if(tag&&mode!=='eggs')continue;
  const s=seed(level,mode),c=await browser.newContext({viewport:{width:w,height:h},reducedMotion:'reduce',deviceScaleFactor:2});
  await c.addInitScript(({s,at})=>{Date.now=()=>at;if(!sessionStorage.getItem('seeded')){localStorage.setItem('chick-kitchen-v1',JSON.stringify(s));localStorage.setItem('chick-kitchen-ui-v1',JSON.stringify({seenSupplyMove:true}));sessionStorage.setItem('seeded','1');}},{s,at:s.lastSeen});
  const page=await c.newPage();page.on('pageerror',e=>errors.push(e.message));page.on('response',r=>{if(r.status()>=400)errors.push(r.status()+' '+r.url());});
  await page.goto(base);await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.waitForTimeout(700);await page.evaluate(()=>document.fonts.ready);
  await page.screenshot({path:`${out}/lv${level+1}-${mode}${tag}.png`});await c.close();
 }
 if(errors.length)throw Error(errors.join('\n'));
 console.log('captured',out);
}finally{await browser.close();server.kill();}
