import vm from 'node:vm';
import assert from 'node:assert/strict';
import {readFileSync,writeFileSync} from 'node:fs';
import {createRequire} from 'node:module';
import {fileURLToPath} from 'node:url';
const bundle=readFileSync(new URL('dist/game.js',import.meta.url),'utf8'),stored=new Map(),draws=[];
const ctx=new Proxy({},{get:(_,name)=>(...args)=>draws.push([name,...args]),set:()=>true});
const wx={getStorageSync:k=>stored.get(k)??'',setStorageSync:(k,v)=>stored.set(k,v),getSystemInfoSync:()=>({windowWidth:390,windowHeight:844,pixelRatio:1}),createCanvas:()=>({getContext:()=>ctx}),onTouchStart(){},onHide(){}};
// No DOM/window/fetch/localStorage, and deliberately no structuredClone/TextEncoder.
const sandbox={wx,console};vm.createContext(sandbox);vm.runInContext(bundle,sandbox,{timeout:10000});const probe=sandbox.JibaoProbe;
const initial=probe.state();probe.start();assert.equal(probe.state().batch.eggs.length,24);probe.harvest();const saved=probe.state();probe.harvest();assert.equal(probe.state().cp,saved.cp);assert.deepEqual(probe.state().farm,saved.farm);probe.reload();assert.equal(probe.state().cp,saved.cp);
const second={wx,console};vm.createContext(second);vm.runInContext(bundle,second,{timeout:10000});assert.equal(second.JibaoProbe.state().cp,saved.cp);
const report={passed:true,execution:'actual bundled game rules running in Node VM with explicitly simulated wx APIs',wechatDevtools:false,wechatPhone:false,checks:['no DOM globals','polyfills exercised','24-egg real startBatch','real harvest without duplicate rewards','wx adapter storage roundtrip','second runtime restoration'],initialCP:initial.cp,harvestCP:saved.cp,drawCalls:draws.length};
writeFileSync(new URL('../../artifacts/wechat-probe/runtime.json',import.meta.url),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));
if(process.argv.includes('--render')){
  const require=createRequire(import.meta.url),{chromium}=require(process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');const browser=await chromium.launch({executablePath:'C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe',headless:true});
  try{const page=await browser.newPage({viewport:{width:390,height:844}});await page.setContent('<html><body style="margin:0"><canvas id="probe"></canvas></body></html>');
    await page.evaluate(()=>{const m=new Map();globalThis.wx={createCanvas:()=>document.querySelector('canvas'),getSystemInfoSync:()=>({windowWidth:390,windowHeight:844,pixelRatio:1}),getStorageSync:k=>m.get(k)??'',setStorageSync:(k,v)=>m.set(k,v),onHide(){},onTouchStart(fn){document.querySelector('canvas').onclick=e=>fn({touches:[{clientY:e.clientY}]});}};});
    await page.addScriptTag({content:bundle});await page.mouse.click(120,380);await page.mouse.click(120,440);const actual=await page.evaluate(()=>JibaoProbe.state());assert.equal(actual.batch.eggs.length,24);await page.screenshot({path:fileURLToPath(new URL('../../artifacts/wechat-probe/canvas-browser.png',import.meta.url))});
    report.browserCanvasRendered=true;report.browserCP=actual.cp;writeFileSync(new URL('../../artifacts/wechat-probe/runtime.json',import.meta.url),JSON.stringify(report,null,2));
  }finally{await browser.close();}
}
