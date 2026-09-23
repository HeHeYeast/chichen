// Isolated mobile browser profiles only; the clock and random source below are
// test adapters. No player save or connected device is modified.
import {createRequire} from 'node:module';
import {resolve} from 'node:path';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
const {chromium}=createRequire(import.meta.url)(resolve(process.argv[2]));
const base=process.argv[3]??'http://127.0.0.1:4173',out=resolve('artifacts/qa/kitchen-v144');
await mkdir(out,{recursive:true});
const browser=await chromium.launch({headless:true,executablePath:process.argv[4]}),checks=[],errors=[],failedRequests=[];
const watch=p=>{p.on('pageerror',e=>errors.push(e.message));p.on('response',r=>{if(r.status()>=400)failedRequests.push(r.url());});};
const kitchen=async p=>{await p.locator('[data-control-id="tool:0"]').waitFor();assert.equal(await p.locator('#panels > *,#dialog-layer > *').count(),0);};
const settled=async p=>p.locator('[data-control-id="nav:0"][tabindex="0"]').waitFor();
const read=p=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
const jump=(p,ms)=>p.evaluate(ms=>{window.__qaClockOffset+=ms;},ms);
const start=async p=>{await p.getByRole('button',{name:'开始游戏',exact:true}).tap();await kitchen(p);await settled(p);};
const DAY=86400000;
async function seeded(seed){
  const c=await browser.newContext({viewport:{width:375,height:850},isMobile:true,hasTouch:true,reducedMotion:'reduce'});
  await c.addInitScript(seed=>{
    window.__qaClockOffset=0;const realNow=Date.now.bind(Date);Date.now=()=>realNow()+window.__qaClockOffset;
    if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));
  },seed);
  const p=await c.newPage();watch(p);await p.goto(base+'/');await start(p);return {c,p};
}
try{
  for(const [width,height]of [[320,568],[375,747],[430,956],[320,800]])for(const fraction of [.25,.5,.75]){
    const c=await browser.newContext({viewport:{width,height},isMobile:true,hasTouch:true,reducedMotion:'reduce'}),p=await c.newPage();watch(p);
    await p.goto(base+'/');const button=p.getByRole('button',{name:'开始游戏',exact:true});await button.waitFor();
    const before=await read(p),r=await button.boundingBox(),x=r.x+r.width*fraction,y=r.y+r.height/2;
    await p.touchscreen.tap(x,y);await p.touchscreen.tap(x,y);
    // Some WebViews emit an additional click without pointer detail.
    await p.locator('[data-control-id="nav:4"]').evaluate(el=>el.dispatchEvent(new MouseEvent('click',{bubbles:true,detail:0})));
    await kitchen(p);assert.deepEqual(await read(p),before);
    await settled(p);
    await p.getByRole('button',{name:'图鉴',exact:true}).tap();await p.getByRole('heading',{name:'鸡宝图鉴',exact:true}).waitFor();
    await p.getByRole('button',{name:'厨房',exact:true}).tap();await kitchen(p);
    await c.close();
  }
  checks.push('4种手机尺寸、开始按钮左中右共12组快速连点和额外click均留在厨房；保护结束后图鉴正常可用');

  // Keyboard/assistive activation must also continue to work after entry.
  {
    const c=await browser.newContext(),p=await c.newPage();watch(p);await p.goto(base+'/');
    const button=p.getByRole('button',{name:'开始游戏',exact:true});await button.waitFor();await button.focus();await p.keyboard.press('Enter');
    await kitchen(p);await settled(p);await p.getByRole('button',{name:'图鉴',exact:true}).evaluate(el=>el.click());
    await p.getByRole('heading',{name:'鸡宝图鉴',exact:true}).waitFor();await c.close();
    checks.push('键盘开始与辅助技术click仍可正常使用');
  }

  {
    const seed=E.freshState();seed.cp=1500;
    const {c,p}=await seeded(seed);
    assert.equal(await p.getByRole('button',{name:'打扫厨房',exact:true}).count(),0);
    await jump(p,DAY+1);const clean=p.getByRole('button',{name:'打扫厨房',exact:true});await clean.waitFor();
    const dirty=await read(p);assert.equal(dirty.dirty,true);assert.equal(dirty.lastClean,seed.lastClean);
    await p.screenshot({path:resolve(out,'naturally-dirty.png')});
    await clean.tap();await p.locator('[data-no]').tap();assert.deepEqual(await read(p),dirty);
    await clean.tap();await p.locator('[data-yes]').tap();await clean.waitFor({state:'detached'});
    const cleaned=await read(p);assert.equal(cleaned.dirty,false);assert.equal(cleaned.cp,dirty.cp-100);assert.ok(cleaned.lastClean>seed.lastClean+DAY);
    await p.screenshot({path:resolve(out,'cleaned.png')});
    await jump(p,DAY-60000);await p.waitForTimeout(180);assert.equal((await read(p)).dirty,false);
    await jump(p,60001);await clean.waitFor();assert.equal((await read(p)).dirty,true);
    await c.close();checks.push('空厨房在线自然变脏，出现污层和打扫入口；取消不扣款、确认扣100CP并清洁；新的一天后再次变脏');
  }

  {
    const seed=E.freshState();seed.cp=1500;seed.lastClean-=2*DAY;seed.lastSeen=Date.now();
    const {c,p}=await seeded(seed);await p.getByRole('button',{name:'打扫厨房',exact:true}).waitFor();
    assert.equal((await read(p)).dirty,true);assert.equal((await read(p)).lastClean,seed.lastClean);
    await c.close();checks.push('旧存档刚刚游玩过、两天未清洁，重新打开仍正确变脏');
  }

  {
    const seed=E.freshState();seed.lastClean-=2*DAY;seed.lastSeen=Date.now();
    const {c,p}=await seeded(seed);
    await p.locator('[data-control-id="tool:0"]').tap();await p.locator('[data-yes]').tap();
    const batch=(await read(p)).batch;assert.equal(batch.eggs.length,24);
    await p.evaluate(()=>{Math.random=()=>0;});await jump(p,batch.ends-Date.now()+50);
    await p.waitForFunction(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')).batch.eggs.some(e=>e.status==='cracking'&&e.id===1));
    await jump(p,2100);
    await p.waitForFunction(()=>['hatching','ready'].includes(JSON.parse(localStorage.getItem('chick-kitchen-v1')).batch.eggs[0].status));
    const sick=p.getByRole('button',{name:`收取${E.label(E.char(0,1))} 1`,exact:true});await sick.waitFor();await sick.tap();
    const saved=await read(p);assert.equal(saved.total['0:1'],1);assert.equal(saved.farm['0:1'],1);
    await p.getByRole('button',{name:'图鉴',exact:true}).tap();
    await p.locator('[data-book-tab="species"]').click();assert.ok((await p.locator('#panels').innerText()).includes(E.label(E.char(0,1))));
    await p.screenshot({path:resolve(out,'sick-chick-collected.png')});
    await c.close();checks.push('自然脏污厨房正常开火、破壳产生病鸡宝，触摸收取后进入农场和图鉴');
  }
  assert.deepEqual(errors,[]);assert.deepEqual(failedRequests,[]);
  const report={checkedAt:new Date().toISOString(),checks,errors,failedRequests,nativeDeviceTest:false};
  await writeFile(resolve(out,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));
}catch(error){console.error(JSON.stringify({checks,errors,failedRequests},null,2));throw error;}finally{await browser.close();}
