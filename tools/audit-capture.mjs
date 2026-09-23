import {createRequire} from 'node:module';
import {mkdir,writeFile} from 'node:fs/promises';
const require=createRequire(import.meta.url);
const {chromium}=require(process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const out='artifacts/regression-audit';await mkdir(out,{recursive:true});
const browser=await chromium.launch({headless:true});
const page=await browser.newPage({viewport:{width:390,height:844},reducedMotion:'reduce'}),errors=[];
page.on('pageerror',e=>errors.push(e.message));page.on('console',m=>{if(m.type()==='error')errors.push(m.text());});
try{
 await page.goto('http://127.0.0.1:4173');await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.waitForTimeout(900);
 const measurements=[];
 for(const name of ['厨房','农场','图鉴','生意','寻访']){
  await page.getByRole('button',{name,exact:true}).click();await page.waitForTimeout(250);
  if(name==='图鉴')await page.locator('[data-book-tab=species]').click();
  await page.waitForTimeout(300);
  await page.screenshot({path:`${out}/${process.argv[2]??'before'}-${name}.png`});
  measurements.push({name,...await page.evaluate(()=>({rects:Object.fromEntries(['#game','#scene-stage','#scene','#controls','#main-nav','#panels'].map(s=>{const el=document.querySelector(s),r=el.getBoundingClientRect();return [s,{x:r.x,y:r.y,w:r.width,h:r.height,zoom:getComputedStyle(el).zoom}]})),cards:[...document.querySelectorAll('.collection-stamp')].map(e=>({h:e.getBoundingClientRect().height,scroll:e.scrollHeight,client:e.clientHeight})),text:document.body.innerText}))});
 }
 await writeFile(`${out}/${process.argv[2]??'before'}-measurements.json`,JSON.stringify({errors,measurements},null,2));console.log(JSON.stringify({errors,measurements},null,2));
}finally{await browser.close();}
