// Popups close when thrown away: drag up or off to either side (touch and mouse); a slow or short drag, or a drag down,
// leaves them open; a list that can scroll keeps scrolling. Isolated browser contexts only.
import {createRequire} from 'node:module';
import {resolve} from 'node:path';
import assert from 'node:assert/strict';
import {freshState} from '../web/engine.js';
const {chromium}=createRequire(import.meta.url)(resolve(process.argv[2]));
const base=process.argv[3]??'http://127.0.0.1:4173';
const browser=await chromium.launch({headless:true,executablePath:process.argv[4]});
const errors=[],checks=[];
async function swipe(p,from,to,touch){
  if(touch){
    const c=await p.context().newCDPSession(p);
    await c.send('Input.dispatchTouchEvent',{type:'touchStart',touchPoints:[from]});
    for(let i=1;i<=10;i++)await c.send('Input.dispatchTouchEvent',{type:'touchMove',touchPoints:[{x:from.x+(to.x-from.x)*i/10,y:from.y+(to.y-from.y)*i/10}]});
    await c.send('Input.dispatchTouchEvent',{type:'touchEnd',touchPoints:[]});await c.detach();
  }else{await p.mouse.move(from.x,from.y);await p.mouse.down();await p.mouse.move(to.x,to.y,{steps:10});await p.mouse.up();}
  await p.waitForTimeout(450);
}
const spot=async(p,sel,dy=0)=>{const b=await p.locator(sel).first().boundingBox();return {x:b.x+b.width/2,y:b.y+Math.min(b.height-6,24)+dy};};
try{
  for(const [w,h,touch] of [[375,850,true],[320,568,true],[430,932,false]]){
    const context=await browser.newContext({viewport:{width:w,height:h},isMobile:touch,hasTouch:touch});
    const seed=freshState();seed.cp=900000;seed.kitchenLevel=3;seed.toolLevels.fill(2);seed.music=false;seed.sound=false;
    await context.addInitScript(s=>{if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(s));},seed);
    const p=await context.newPage();p.on('pageerror',e=>errors.push(e.message));
    await p.goto(base);await p.getByRole('button',{name:'开始游戏',exact:true}).click();
    await p.locator('[data-control-id="nav:0"][tabindex="0"]').waitFor();
    // 1. a confirm dialog (the cookware) thrown up, then off to the left, then right
    for(const dir of ['up','left','right']){
      await p.locator('[data-control-id="tool:0"]').click();await p.locator('#dialog-layer .gd').waitFor();
      // up: from the plank on top (a long body scrolls instead); sideways: from the body
      const a=await spot(p,dir==='up'?'#dialog-layer .gd .gd-title':'#dialog-layer .gd .gd-body');
      const to=dir==='up'?{x:a.x,y:Math.max(6,a.y-260)}:dir==='left'?{x:a.x-w*.7,y:a.y}:{x:a.x+w*.7,y:a.y};
      await swipe(p,a,to,touch);
      assert.equal(await p.locator('#dialog-layer .gd').count(),0,`confirm dialog should close when dragged ${dir} (${w}x${h} ${touch?"touch":"mouse"})`);
    }
    // 2. short drag, drag down: it stays
    await p.locator('[data-control-id="tool:0"]').click();await p.locator('#dialog-layer .gd').waitFor();
    let a=await spot(p,'#dialog-layer .gd .gd-body');
    await swipe(p,a,{x:a.x,y:a.y-30},touch);assert.equal(await p.locator('#dialog-layer .gd').count(),1,'a short drag must not close it');
    await swipe(p,a,{x:a.x,y:a.y+200},touch);assert.equal(await p.locator('#dialog-layer .gd').count(),1,'dragging down must not close it');
    assert.equal(await p.locator('#dialog-layer .gd').evaluate(el=>el.style.translate||''),'','it springs back');
    await p.locator('#dialog-layer .gd-close').click();
    // 3. a shop drawer (a sheet over a page) thrown away
    await p.locator('[data-control-id="supply"]').click();await p.locator('[data-shop-tab="1"]').click();
    await p.locator('[data-shop-ingredient-details]').first().click();await p.locator('.kp-drawer').waitFor();
    // up: from the backdrop (a long drawer body scrolls instead)
    a={x:w/2,y:150};
    await swipe(p,a,{x:a.x,y:Math.max(6,a.y-140)},touch);
    assert.equal(await p.locator('.kp-drawer').count(),0,'drawer should close when dragged up');
    await p.locator('[data-shop-ingredient-details]').first().click();await p.locator('.kp-drawer').waitFor();
    // sideways: from the drawer itself
    a=await spot(p,'.kp-drawer .kp-drawer-body');
    await swipe(p,a,{x:a.x+w*.7,y:a.y},touch);
    assert.equal(await p.locator('.kp-drawer').count(),0,'drawer should close when dragged sideways');
    checks.push({w,h,input:touch?'touch':'mouse',passed:true});await context.close();
  }
}finally{await browser.close();}
assert.deepEqual(errors,[]);
console.log('Popup swipe passed: '+checks.length+' viewports, no browser errors');
