// Isolated browser contexts only; never reads a real player's save or device.
import {createRequire} from 'node:module';
import {resolve} from 'node:path';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {freshState} from '../web/engine.js';
const {chromium}=createRequire(import.meta.url)(resolve(process.argv[2]));
const base=process.argv[3]??'http://127.0.0.1:4173',out=resolve('artifacts/qa/b04-tool-strip');
await mkdir(out,{recursive:true});
const browser=await chromium.launch({headless:true,executablePath:process.argv[4]});
const errors=[],checks=[];
async function coordinates(p,x,y){return p.locator('#scene').evaluate((el,{x,y})=>{
  // Scene coordinates stay 320 wide; information pages have an independent shell.
  const r=el.getBoundingClientRect(),h=parseFloat(getComputedStyle(el).height),w=parseFloat(getComputedStyle(el).width);
  return {x:r.left+x*r.width/w,y:r.top+(384+h-568+y)*r.height/h};
},{x,y});}
async function drag(p,from,to,touch=false){
  const a=await coordinates(p,from,35),b=await coordinates(p,to,35);
  if(touch){
    const client=await p.context().newCDPSession(p);
    await client.send('Input.dispatchTouchEvent',{type:'touchStart',touchPoints:[a]});
    for(let i=1;i<=8;i++)await client.send('Input.dispatchTouchEvent',{type:'touchMove',touchPoints:[{x:a.x+(b.x-a.x)*i/8,y:a.y}]});
    await client.send('Input.dispatchTouchEvent',{type:'touchEnd',touchPoints:[]});await client.detach();
  }else{await p.mouse.move(a.x,a.y);await p.mouse.down();await p.mouse.move(b.x,b.y,{steps:8});await p.mouse.up();}
}
async function visibleIds(p){return p.locator('[data-control-id^="tool:"]').evaluateAll(els=>els.map(e=>Number(e.dataset.controlId.split(':')[1])));}
async function unchanged(p){
  assert.equal(await p.locator('#dialog-layer > *').count(),0,'drag must never open cooking or shopping');
  const state=await p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
  assert.equal(state.cp,900000);assert.equal(state.batch,null);assert.deepEqual(state.ingredients,{0:1});
}
try{
  for(const [width,height,touch] of [[320,568,true],[375,850,true],[430,932,true],[1000,800,false],[844,390,false]]){
    const context=await browser.newContext({viewport:{width,height},isMobile:touch,hasTouch:touch,reducedMotion:'reduce'});
    const seed=freshState();seed.cp=900000;seed.kitchenLevel=3;seed.toolLevels.fill(2);seed.total={'0:114':1};seed.music=false;seed.sound=false;
    await context.addInitScript(s=>{if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(s));},seed);
    const p=await context.newPage();p.on('pageerror',e=>errors.push(e.message));
    await p.goto(base);await p.getByRole('button',{name:'开始游戏',exact:true}).click();
    await p.locator('[data-control-id="nav:0"][tabindex="0"]').waitFor();
    assert.deepEqual(await visibleIds(p),[0,1,2,3]);
    await drag(p,270,70,touch);assert.deepEqual(await visibleIds(p),[3,4,5,6]);await unchanged(p);
    await drag(p,270,70,touch);assert.deepEqual(await visibleIds(p),[5,6,7,8]);await unchanged(p);
    assert.equal(await p.locator('[data-control-id="next"]').isDisabled(),true);
    await drag(p,270,70,touch);assert.deepEqual(await visibleIds(p),[5,6,7,8]);await unchanged(p);
    await p.locator('[data-control-id="prev"]').click();assert.deepEqual(await visibleIds(p),[4,5,6,7]);
    await p.locator('[data-control-id="next"]').click();
    await p.screenshot({path:resolve(out,`tools-${width}.png`)});
    await drag(p,60,275,touch);await drag(p,60,275,touch);assert.deepEqual(await visibleIds(p),[0,1,2,3]);
    // A tap still requests confirmation; cancelling preserves every resource.
    await p.locator('[data-control-id="tool:0"]').click();await p.locator('[data-no]').click();await unchanged(p);
    // Keyboard remains usable after drag and focus reconstruction.
    await p.locator('[data-control-id="next"]').focus();await p.keyboard.press('Enter');assert.deepEqual(await visibleIds(p),[1,2,3,4]);
    await p.getByRole('button',{name:'图鉴',exact:true}).click();
    await p.locator('[data-book-tab="species"]').click();await p.locator('[data-collection-recipes]').click();
    await p.locator('[data-cookbook-tool="8"]').click();
    await p.locator('[data-cookbook-recipe="0:114"]').click();
    await p.locator('[data-cookbook-prepare]').click();
    await p.locator('[data-control-id="tool:8"]').waitFor();
    assert.deepEqual(await visibleIds(p),[5,6,7,8]);await unchanged(p);
    await p.setViewportSize({width:height,height:width});await p.setViewportSize({width,height});await unchanged(p);
    checks.push({width,height,input:touch?'touch':'mouse',passed:true});await context.close();
  }
  assert.deepEqual(errors,[]);
  await writeFile(resolve(out,'results.json'),JSON.stringify({checks,errors,nativeDeviceTest:false},null,2));
  console.log(`C04 browser regression passed: ${checks.length} viewport/input combinations`);
}finally{await browser.close();}
