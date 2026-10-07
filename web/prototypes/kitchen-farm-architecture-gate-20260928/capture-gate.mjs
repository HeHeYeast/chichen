// Captures only isolated architecture prototype pages. Never opens player save.
import {createRequire} from 'node:module';
import {writeFile} from 'node:fs/promises';
const require=createRequire(import.meta.url);
const {chromium}=require(process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const root='web/prototypes/kitchen-farm-architecture-gate-20260928';
const browser=await chromium.launch({headless:true,executablePath:'C:/Program Files/Google/Chrome/Application/chrome.exe'});
const report=[];
async function capture(variant,{state='empty',region='left',health='full',size=390},name){
  const height=size===320?568:844;
  const context=await browser.newContext({viewport:{width:size,height},deviceScaleFactor:1,reducedMotion:'reduce'});
  const page=await context.newPage();
  const errors=[];page.on('pageerror',error=>errors.push(error.stack||String(error)));
  const params=new URLSearchParams({shot:variant,state,region,health,size:String(size)});
  await page.goto(`http://127.0.0.1:4173/${root}/index.html?${params}`);
  try{await page.locator('.phone').waitFor({timeout:7000});}catch(error){console.error('Prototype render failed',name,errors,await page.locator('body').innerText());throw error;}
  await page.evaluate(()=>document.fonts.ready);
  await page.waitForTimeout(160);
  const metrics=await page.locator('.phone').evaluate(el=>{
    const bounds=el.getBoundingClientRect();
    const all=[...el.querySelectorAll('button')].filter(b=>getComputedStyle(b).display!=='none');
    const ready=[...el.querySelectorAll('.nest-egg.is-ready')];
    const readyOverlaps=ready.flatMap((a,i)=>ready.slice(i+1).filter(b=>{const x=a.getBoundingClientRect(),y=b.getBoundingClientRect();return x.left<y.right&&x.right>y.left&&x.top<y.bottom&&x.bottom>y.top;}).map(b=>[a.getAttribute('aria-label'),b.getAttribute('aria-label')]));
    const readyBlocked=ready.filter(b=>{const r=b.getBoundingClientRect(),top=document.elementFromPoint(r.left+r.width/2,r.top+r.height/2);return top?.closest('button')!==b;}).map(b=>b.getAttribute('aria-label'));
    return {width:bounds.width,height:bounds.height,overflowX:el.scrollWidth-el.clientWidth,overflowY:el.scrollHeight-el.clientHeight,
      smallHits:all.map(b=>{const r=b.getBoundingClientRect();return {label:b.getAttribute('aria-label')||b.textContent.trim().slice(0,24),w:Math.round(r.width),h:Math.round(r.height)};}).filter(b=>b.w<40||b.h<40),
      cutText:all.filter(b=>b.scrollWidth>b.clientWidth+2).map(b=>b.textContent.trim().slice(0,24)),readyOverlaps,readyBlocked,
      nestButtons:el.querySelectorAll('.nest-egg').length,navButtons:el.querySelectorAll('.bottom-nav button').length};
  });
  await page.screenshot({path:`${root}/evidence/${name}.png`});
  report.push({name,variant,state,region,health,size,errors,metrics});
  await context.close();
}
for(const variant of ['k1','k3']){
  for(const state of ['empty','incubating','ready','done'])await capture(variant,{state,size:390},`${variant}-${state}-390`);
  for(const state of ['empty','ready'])await capture(variant,{state,size:320},`${variant}-${state}-320`);
}
for(const variant of ['f1','f3']){
  for(const region of ['left','middle','right'])await capture(variant,{region,size:390},`${variant}-${region}-390`);
  for(const region of ['left','middle','right'])await capture(variant,{region,size:320},`${variant}-${region}-320`);
  await capture(variant,{region:'left',health:'low',size:390},`${variant}-repair-390`);
  if(variant==='f3')await capture(variant,{region:'right',health:'low',size:390},'f3-right-repair-390');
}
await writeFile(`${root}/evidence/verification.json`,JSON.stringify(report,null,2));
console.log(JSON.stringify({screens:report.length,pageErrors:report.filter(r=>r.errors.length).map(r=>r.name),overflow:report.filter(r=>r.metrics.overflowX>0||r.metrics.overflowY>0).map(r=>r.name),cutText:report.filter(r=>r.metrics.cutText.length).map(r=>r.name),smallHits:report.flatMap(r=>r.metrics.smallHits.map(h=>({screen:r.name,...h}))).slice(0,30),readyOverlaps:report.filter(r=>r.metrics.readyOverlaps.length).map(r=>r.name),readyBlocked:report.filter(r=>r.metrics.readyBlocked.length).map(r=>r.name)},null,2));
await browser.close();
