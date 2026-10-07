// Exercise both Chromium geometry modes with physical touch coordinates.
import {createRequire} from 'node:module';
import {spawn} from 'node:child_process';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
const {chromium}=createRequire(import.meta.url)(process.argv[2]??process.env.CHICK_PLAYWRIGHT_PACKAGE??process.env.PLAYWRIGHT_MODULE??process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const out=process.env.CHICK_ZOOM_OUTPUT??'artifacts/qa/legacy-zoom';await mkdir(out,{recursive:true});
const server=spawn(process.execPath,['server.mjs','0'],{stdio:['ignore','pipe','pipe'],windowsHide:true});
const base=await new Promise((ok,no)=>{server.on('error',no);server.stdout.on('data',b=>{const m=String(b).match(/http:\/\/127\.0\.0\.1:\d+/);if(m)ok(m[0]);});});
const reports=[];let failed=false;
try{
 // A real pre-128 engine (CHICK_QA_CHROME) only has the legacy geometry: CHICK_ZOOM_MODES=legacy.
 const modes=(process.env.CHICK_ZOOM_MODES??'legacy,current').split(',').map(m=>m.trim()==='legacy');
 for(const legacy of modes){
  const browser=await chromium.launch({headless:true,executablePath:process.env.CHICK_QA_CHROME,args:legacy?['--disable-blink-features=StandardizedBrowserZoom']:[]});
  try{
   for(const [width,height,level] of [[280,653,0],[320,568,0],[384,712,0],[430,932,3],[560,900,1]]){
   const at=Date.now(),state=E.freshState(at,31);state.kitchenLevel=level;state.sound=false;state.music=false;E.startBatch(state,0,at-120000,()=>.5);state.batch.eggs.forEach(e=>{e.status='ready';e.openAt=at-10000;e.animationAt=at-3000;e.blackAt=null;});
   const context=await browser.newContext({viewport:{width,height},isMobile:true,hasTouch:true,deviceScaleFactor:3});
   await context.addInitScript(({state,at})=>{Date.now=()=>at;localStorage.setItem('chick-kitchen-v1',JSON.stringify(state));localStorage.setItem('chick-kitchen-ui-v1',JSON.stringify({seenSupplyMove:true}));},{state,at});
   const page=await context.newPage();const errors=[];page.on('pageerror',e=>errors.push(e.message));
   await page.goto(base);
   await page.getByRole('button',{name:'开始游戏',exact:true}).tap();await page.waitForTimeout(700);
   const metrics=await page.evaluate(()=>{
    const probe=document.createElement('div');probe.style.cssText='position:fixed;top:-10000px;width:100px;height:10px;zoom:2';document.body.append(probe);const probeWidth=probe.getBoundingClientRect().width;probe.remove();
    const root=document.querySelector('#game').getBoundingClientRect(),canvas=document.querySelector('#scene').getBoundingClientRect();
    const stage=document.querySelector('#scene-stage'),style=getComputedStyle(stage),scale=parseFloat(style.zoom)||1;
    // The intended visual transform can be derived independently of zoom-sensitive APIs.
    const height=parseFloat(stage.style.height),shown=height*scale,top=root.y+(document.querySelector('#panels').clientHeight-shown)/2;
    return {probeWidth,root:{x:root.x,y:root.y,width:root.width},canvas:{x:canvas.x,y:canvas.y,width:canvas.width,height:canvas.height},scale,height,top,transform:style.transform};
   });
   assert.equal(metrics.probeWidth,legacy?100:200,'the requested Chromium geometry mode must actually be active');
   const centres=await page.locator('[data-control-id^="egg:"]').evaluateAll(es=>es.map(e=>({x:parseFloat(e.style.left)+parseFloat(e.style.width)/2,y:parseFloat(e.style.top)+parseFloat(e.style.height)/2})));
   // Use physical visual coordinates, rather than deriving a pass from the same broken rect.
   const visibleScale=metrics.transform==='none'?metrics.scale:Number(metrics.transform.match(/^matrix\(([^,]+)/)?.[1])||1;
   const physicalTop=metrics.root.y+(await page.locator('#panels').evaluate(e=>e.clientHeight)-metrics.height*visibleScale)/2;
   const points=centres.map(p=>({x:metrics.root.x+(metrics.root.width-320*visibleScale)/2+p.x*visibleScale,y:physicalTop+p.y*visibleScale}));
   const cdp=await context.newCDPSession(page);
   const tap=async p=>{await cdp.send('Input.dispatchTouchEvent',{type:'touchStart',touchPoints:[p]});await cdp.send('Input.dispatchTouchEvent',{type:'touchEnd',touchPoints:[]});await page.waitForTimeout(850);};
   await tap(points.at(-1));
   const one=await page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
   assert.equal(one.batch.eggs.filter(e=>e.collected).length,1,'a single visible front chick touch collects exactly once');
   assert.equal(one.cp-state.cp,1);
   const rows=points.sort((a,b)=>a.y-b.y),path=[];for(let i=0;i<rows.length;i+=6){const row=rows.slice(i,i+6).sort((a,b)=>a.x-b.x);if((i/6)%2)row.reverse();path.push(...row);}
   await cdp.send('Input.dispatchTouchEvent',{type:'touchStart',touchPoints:[path[0]]});
   for(let i=1;i<path.length;i++)for(let k=1;k<=4;k++){const x=path[i-1].x+(path[i].x-path[i-1].x)*k/4,y=path[i-1].y+(path[i].y-path[i-1].y)*k/4;await cdp.send('Input.dispatchTouchEvent',{type:'touchMove',touchPoints:[{x,y}]});}
   await cdp.send('Input.dispatchTouchEvent',{type:'touchEnd',touchPoints:[]});await page.waitForTimeout(900);
   const save=await page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1'))),collected=save.batch.eggs.filter(e=>e.collected).length;
   await page.locator('[data-control-id="ingredient"]').tap();await page.locator('.next-batch-screen').waitFor();
   await page.locator('#panels .close').tap();
   try{await page.screenshot({path:out+'/'+(legacy?'legacy':'current')+`-${width}-${height}-lv${level+1}.png`});}catch(error){console.warn('Screenshot unavailable:',error.message);}
   const report={legacy,browser:browser.version(),viewport:[width,height],level:level+1,metrics,single:1,collected,cp:save.cp,errors,passed:collected===24&&save.cp-state.cp===24&&!errors.length};reports.push(report);failed||=!report.passed;
   await context.close();
   }
  }finally{await browser.close();}
 }
}finally{server.kill();await writeFile(out+'/report.json',JSON.stringify(reports,null,2));console.log(JSON.stringify(reports,null,2));if(failed)process.exitCode=1;}
