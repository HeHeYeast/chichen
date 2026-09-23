// Runs in new browser contexts and uses only the explicit review fixture store.
import {createRequire} from 'node:module';
import {resolve} from 'node:path';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
if(!process.argv[2])throw Error('Pass an existing Playwright package directory.');
const {chromium}=createRequire(import.meta.url)(resolve(process.argv[2]));
const output=resolve('artifacts/qa/long-screen');await mkdir(output,{recursive:true});
const base=process.argv[3]??'http://127.0.0.1:4173';
const browser=await chromium.launch({headless:true}),reports=[],errors=[],failedRequests=[];
const configurations=[
  {name:'magic8-native',width:1256,height:2503,density:1},
  {name:'magic8-375',width:375,height:747,density:3},
  {name:'tall-430',width:430,height:932,density:2},
  {name:'original-320',width:320,height:568,density:1},
  {name:'landscape',width:932,height:430,density:2},
];
const almost=(value,expected,label,tolerance=.7)=>assert.ok(Math.abs(value-expected)<=tolerance,`${label}: ${value} / ${expected}`);
try{
  for(const device of configurations){
    const context=await browser.newContext({viewport:{width:device.width,height:device.height},deviceScaleFactor:device.density});
    const page=await context.newPage();
    page.on('pageerror',error=>errors.push({device:device.name,message:error.message}));
    page.on('response',response=>{if(response.status()>=400)failedRequests.push({device:device.name,status:response.status(),url:response.url()});});
    const saved=()=>page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-review-v1')));
    const fixture=async(name,expectedPage)=>{
      await page.evaluate(({name,expectedPage})=>new Promise(resolve=>{
        const listener=event=>{if(event.source===window&&event.data?.type==='chick-status'&&event.data.loaded&&event.data.page===expectedPage){removeEventListener('message',listener);resolve();}};
        addEventListener('message',listener);postMessage({type:'chick-review',command:'scene',value:name},location.origin);
      }),{name,expectedPage});
      await page.evaluate(()=>new Promise(resolve=>requestAnimationFrame(()=>requestAnimationFrame(resolve))));
    };
    const geometry=()=>page.locator('#game').evaluate(game=>{
      const box=element=>{const r=element.getBoundingClientRect();return{x:r.x,y:r.y,width:r.width,height:r.height,bottom:r.bottom,right:r.right};};
      const canvas=game.querySelector('canvas');
      return {game:box(game),canvas:box(canvas),buffer:[canvas.width,canvas.height],extra:parseFloat(game.style.getPropertyValue('--extra-height')),logicalHeight:parseFloat(game.style.getPropertyValue('--game-height')),viewport:[innerWidth,innerHeight],navigation:[...game.querySelectorAll('[data-control-id^="nav:"]')].map(box)};
    });
    const screenshot=name=>page.screenshot({path:resolve(output,device.name+'-'+name+'.png'),animations:'disabled'});
    const checkCanvasBottom=async(screen)=>{
      // Geometry alone missed a real-device paint failure. Inspect actual bitmap
      // pixels as well, including the final row, before taking each screenshot.
      await page.evaluate(()=>new Promise(resolve=>requestAnimationFrame(()=>requestAnimationFrame(resolve))));
      const result=await page.locator('#scene').evaluate(canvas=>{
        const rows=Math.min(8,canvas.height),pixels=canvas.getContext('2d').getImageData(0,canvas.height-rows,canvas.width,rows).data;
        let minimumAlpha=255,transparentPixels=0,nonOpaquePixels=0;
        const rowMinimumAlpha=Array(rows).fill(255);
        for(let index=3;index<pixels.length;index+=4){
          const alpha=pixels[index],row=Math.floor((index-3)/4/canvas.width);
          minimumAlpha=Math.min(minimumAlpha,alpha);rowMinimumAlpha[row]=Math.min(rowMinimumAlpha[row],alpha);
          if(alpha===0)transparentPixels++;if(alpha!==255)nonOpaquePixels++;
        }
        return {width:canvas.width,height:canvas.height,rows,minimumAlpha,transparentPixels,nonOpaquePixels,pixelCount:canvas.width*rows,rowMinimumAlpha};
      });
      assert.equal(result.minimumAlpha,255,`${screen} bottom bitmap must be fully opaque: ${JSON.stringify(result)}`);
      return {screen,...result};
    };
    const navigationFits=async(screen)=>{
      const current=await geometry(),scale=current.game.width/320;
      assert.equal(current.navigation.length,4,screen+' navigation count');
      for(const nav of current.navigation){
        almost(nav.bottom,current.game.bottom-8*scale,screen+' nav bottom inset');
        assert.ok(nav.y>=current.game.y&&nav.bottom<=current.game.bottom&&nav.x>=current.game.x&&nav.right<=current.game.right,screen+' navigation stays inside game');
      }
    };
    const panelFits=async(name)=>{
      await page.locator('#panels .panel').evaluate(panel=>Promise.all(panel.getAnimations({subtree:true}).map(animation=>animation.finished.catch(()=>{}))));
      const result=await page.locator('#panels .panel').evaluate(panel=>{
        const p=panel.getBoundingClientRect(),game=document.querySelector('#game').getBoundingClientRect(),nav=document.querySelector('[data-control-id="nav:0"]').getBoundingClientRect();
        const footer=panel.querySelector('footer')?.getBoundingClientRect();
        const behind=[...document.querySelectorAll('#controls button')].filter(button=>{const r=button.getBoundingClientRect();return r.top>=p.top&&r.top<p.bottom&&button.tabIndex>=0;}).map(button=>button.getAttribute('aria-label'));
        return {top:p.top,bottom:p.bottom,navTop:nav.top,withinGame:p.left>=game.left-.5&&p.right<=game.right+.5&&p.top>=game.top-.5&&p.bottom<=game.bottom+.5,footerInside:!footer||footer.top>=p.top&&footer.bottom<=p.bottom+.5,behind};
      });
      assert.equal(result.withinGame,true,name+' panel');assert.equal(result.footerInside,true,name+' footer');assert.ok(result.bottom<result.navTop,name+' does not cover navigation');assert.deepEqual(result.behind,[],name+' blocks controls behind panel');
      return {name,...result};
    };
    try{
      await page.goto(base+'/web/index.html?review=1');await page.getByRole('button',{name:'开始游戏',exact:true}).waitFor({state:'visible'});
      const titleLayout=await geometry(),beforeStart=await saved(),startBox=await page.getByRole('button',{name:'开始游戏',exact:true}).boundingBox();
      assert.ok(startBox.x>=titleLayout.game.x&&startBox.y>=titleLayout.game.y&&startBox.x+startBox.width<=titleLayout.game.right+.7&&startBox.y+startBox.height<=titleLayout.game.bottom+.7,'title start button is inside viewport');
      const bitmapChecks=[await checkCanvasBottom('title')];await screenshot('title');
      await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.locator('[data-control-id="tool:0"]').waitFor({state:'visible'});
      const afterStart=await saved();
      assert.equal(afterStart.cp,beforeStart.cp,'starting from the title does not spend CP');assert.deepEqual(afterStart.batch,beforeStart.batch,'title entry keeps the active batch');assert.deepEqual(afterStart.farm,beforeStart.farm,'title entry keeps farm progress');
      await fixture('busy',0);
      const layout=await geometry(),scale=layout.game.width/320,before=await saved();
      almost(layout.game.y,0,'no top gap');almost(layout.game.bottom,device.height,'no bottom gap');
      if(device.height>=device.width){almost(layout.game.x,0,'portrait left');almost(layout.game.right,device.width,'portrait right');}
      else{assert.ok(layout.game.x>0);almost(layout.game.x,device.width-layout.game.right,'landscape horizontal centering');}
      assert.deepEqual(layout.canvas,layout.game,'canvas fills live game rectangle');
      almost(layout.buffer[0],layout.game.width*device.density,'canvas buffer width',1);almost(layout.buffer[1],layout.game.height*device.density,'canvas buffer height',1);
      await navigationFits('kitchen');
      const hitPositions=await page.locator('[data-control-id^="egg:"]').evaluateAll(buttons=>buttons.map(button=>({id:Number(button.dataset.controlId.slice(4)),x:parseFloat(button.style.left),y:parseFloat(button.style.top),w:parseFloat(button.style.width),h:parseFloat(button.style.height)})));
      assert.equal(hitPositions.length,24);
      for(const hit of hitPositions){const egg=before.batch.eggs[hit.id];almost(hit.x,egg.x-19,'egg x unchanged',.01);almost(hit.y,egg.y-13+layout.extra*.58,'egg y translated',.01);assert.equal(hit.w,38);assert.equal(hit.h,42);}
      bitmapChecks.push(await checkCanvasBottom('kitchen'));await screenshot('kitchen');
      await page.locator('[data-control-id="egg:23"]').click();const collected=await saved();
      assert.equal(collected.batch.eggs[23].collected,true,'last-row chick hit');assert.equal(collected.cp,before.cp+1);assert.deepEqual(collected.batch.eggs.map(e=>[e.x,e.y]),before.batch.eggs.map(e=>[e.x,e.y]));
      await page.getByRole('button',{name:'向右滚动厨具',exact:true}).click();assert.equal(await page.locator('[data-control-id="tool:0"]').count(),0);assert.equal(await page.locator('[data-control-id="tool:4"]').count(),1);
      await page.getByRole('button',{name:'向左滚动厨具',exact:true}).click();assert.equal(await page.locator('[data-control-id="tool:0"]').count(),1);
      await page.getByRole('button',{name:'选择调味料',exact:true}).click();const panels=[await panelFits('ingredients')];
      await page.getByRole('button',{name:'厨房',exact:true}).click();await page.getByRole('button',{name:'补给 · 小卖部',exact:true}).click();await page.locator('.shop-screen').waitFor({state:'visible'});panels.push(await panelFits('shop'));
      await page.getByRole('button',{name:'设置',exact:true}).click();await page.locator('.settings-screen').waitFor({state:'visible'});panels.push(await panelFits('settings'));await screenshot('settings');
      await fixture('v8-farm',1);const farmAtStart=await saved(),farmLayout=await geometry();
      const dragY=farmLayout.game.y+(farmLayout.logicalHeight-115)*scale;
      for(let drag=0;drag<3;drag++){
        await page.mouse.move(farmLayout.game.x+280*scale,dragY);await page.mouse.down();await page.mouse.move(farmLayout.game.x+35*scale,dragY,{steps:14});await page.mouse.up();
      }
      const shrine=page.getByRole('button',{name:'打开神社委托簿',exact:true});await shrine.waitFor({state:'visible'});
      const shrineBox=await shrine.boundingBox();almost(shrineBox.x,farmLayout.game.x+97*scale,'farm drag reaches right edge',1);
      assert.deepEqual((await saved()).total,farmAtStart.total,'farm scroll does not change progress');await navigationFits('farm');bitmapChecks.push(await checkCanvasBottom('farm'));await screenshot('farm');
      await shrine.click();await page.locator('.activities-screen').waitFor({state:'visible'});panels.push(await panelFits('shrine'));
      await page.getByRole('button',{name:'厨房',exact:true}).click();await page.locator('[data-control-id="tool:0"]').waitFor({state:'visible'});
      reports.push({device,titleLayout,titleStartButtonInside:true,titleStartPreservesProgress:true,layout,panels,bitmapChecks,lastRowCollected:true,toolPaging:true,lowerFarmDrag:true,shrineHit:true});
      if(device.name==='magic8-375'){
        await fixture('busy',0);const stable=await saved();
        await page.setViewportSize({width:747,height:375});await page.waitForFunction(()=>Math.abs(document.querySelector('#game').getBoundingClientRect().bottom-innerHeight)<.7);
        await page.setViewportSize({width:375,height:747});await page.waitForFunction(()=>Math.abs(document.querySelector('#game').getBoundingClientRect().width-innerWidth)<.7);
        assert.deepEqual((await saved()).batch,stable.batch,'rotation preserves active batch');
        await page.locator('[data-control-id="egg:23"]').click();assert.equal((await saved()).batch.eggs[23].collected,true,'hit remains aligned after rotation');
      }
    }catch(error){await screenshot('failure');throw error;}finally{await context.close();}
  }
  assert.deepEqual(errors,[]);assert.deepEqual(failedRequests,[]);
  const report={checkedAt:new Date().toISOString(),reports,errors,failedRequests,checks:['Title screenshot before fixtures; start button inside viewport and enters kitchen without changing progress','Title/kitchen/farm final8canvaspixelrows fully opaque, including final row','Portrait full viewport without top/bottom bands','DPR canvas buffer matches visual dimensions','Saved 6 by 4 egg spacing unchanged','Last-row chick collect works before/after rotation','Lower navigation and cookware pagination hit at rendered position','Expanded panel footers fit and underlying controls are blocked','Farm drag works in extended lower region and shrine sign opens its board']};
  await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));
}catch(error){console.error(JSON.stringify({errors,failedRequests},null,2));throw error;}finally{await browser.close();}
