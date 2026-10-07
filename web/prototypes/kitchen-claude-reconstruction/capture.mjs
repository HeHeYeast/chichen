// Captures the reconstruction candidates. Run from the repo root with the dev
// server up:  node server.mjs 4180  then  node web/prototypes/kitchen-claude-reconstruction/capture.mjs
import {createRequire} from 'node:module';
import {mkdir} from 'node:fs/promises';

const require=createRequire(import.meta.url);
const {chromium}=require(process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const base=process.argv[2]??'http://127.0.0.1:4180';
const out='artifacts/kitchen-claude-reconstruction-20260928';
await mkdir(out,{recursive:true});
const browser=await chromium.launch({headless:true,executablePath:'C:/Program Files/Google/Chrome/Application/chrome.exe'});
const jobs=[];
for(const v of ['A','B','C','D']){
  jobs.push({v,w:390,h:844,dsf:1,state:'incubating',file:`${v}-390x844.png`});
  jobs.push({v,w:390,h:844,dsf:2,state:'incubating',file:`${v}-390x844@2x.png`});
  jobs.push({v,w:390,h:844,dsf:1,state:'ready',file:`${v}-ready-390x844.png`});
  jobs.push({v,w:320,h:568,dsf:1,state:'incubating',file:`${v}-320x568.png`});
  jobs.push({v,w:320,h:568,dsf:2,state:'incubating',file:`${v}-320x568@2x.png`});
}
for(const job of jobs){
  const context=await browser.newContext({viewport:{width:job.w,height:job.h},deviceScaleFactor:job.dsf});
  const page=await context.newPage();
  const errors=[];page.on('pageerror',e=>errors.push(e.message));page.on('console',m=>{if(m.type()==='error')errors.push(m.text());});
  await page.goto(`${base}/web/prototypes/kitchen-claude-reconstruction/index.html?v=${job.v}&state=${job.state}`);
  await page.waitForFunction(()=>document.documentElement.dataset.ready,null,{timeout:15000});
  const ready=await page.evaluate(()=>document.documentElement.dataset.ready);
  await page.screenshot({path:`${out}/${job.file}`});
  console.log(job.file,ready,errors.length?errors.join(' | '):'');
  await context.close();
}
// Comparison board (all candidates) and one Runtime-vs-candidate pair board each.
for(const [query,file,width] of [['', 'COMPARISON-BOARD.png',1780],['?pair=A','PAIR-Runtime-vs-A.png',880],['?pair=B','PAIR-Runtime-vs-B.png',880],['?pair=C','PAIR-Runtime-vs-C.png',880]]){
  const context=await browser.newContext({viewport:{width,height:1200},deviceScaleFactor:1});
  const page=await context.newPage();
  await page.goto(`${base}/web/prototypes/kitchen-claude-reconstruction/board.html${query}`);
  await page.waitForFunction(()=>document.documentElement.dataset.ready==='1',null,{timeout:30000});
  await page.waitForTimeout(300);
  await page.screenshot({path:`${out}/${file}`,fullPage:true});
  if(!query)await page.locator('#verdict-block').screenshot({path:`${out}/PAIR-Runtime-vs-Cprime.png`});
  console.log(file);
  await context.close();
}
await browser.close();
