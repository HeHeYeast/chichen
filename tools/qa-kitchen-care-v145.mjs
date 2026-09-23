// Isolated browser saves and a test clock; never touches a connected phone.
import {createRequire} from 'node:module';
import {resolve} from 'node:path';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
const {chromium}=createRequire(import.meta.url)(resolve(process.argv[2]));
const base=process.argv[3]??'http://127.0.0.1:4173',out=resolve(process.argv[5]??'artifacts/qa/kitchen-care-v145');
await mkdir(out,{recursive:true});
const browser=await chromium.launch({headless:true,executablePath:process.argv[4]});
const errors=[],requests=[],checks=[],DAY=E.CLEAN_INTERVAL;
const read=p=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
const jump=(p,ms)=>p.evaluate(ms=>{window.__qaClockOffset+=ms;},ms);
async function seeded(seed,viewport={width:375,height:850}){
  const c=await browser.newContext({viewport,isMobile:true,hasTouch:true,reducedMotion:'reduce'});
  await c.addInitScript(seed=>{
    window.__qaClockOffset=0;const original=Date.now.bind(Date);Date.now=()=>original()+window.__qaClockOffset;
    if(!localStorage.getItem('chick-kitchen-v1'))localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));
  },seed);
  const p=await c.newPage();p.on('pageerror',e=>errors.push(e.message));
  p.on('response',r=>{if(r.status()>=400&&!r.url().endsWith('favicon.ico'))requests.push(r.url());});
  await p.goto(base+'/');await p.getByRole('button',{name:'开始游戏',exact:true}).tap();
  await p.locator('[data-control-id="nav:0"][tabindex="0"]').waitFor();
  return {c,p};
}
async function layout(p){
  const box=await p.locator('.cleaning-dialog').evaluate(el=>{
    const r=el.getBoundingClientRect(),game=document.querySelector('#game').getBoundingClientRect();
    const clipped=[...el.children].filter(x=>{const b=x.getBoundingClientRect();return b.left<r.left||b.right>r.right||b.top<r.top||b.bottom>r.bottom;}).map(x=>x.className);
    const text=el.querySelector('p');
    return {clipped,inside:r.left>=game.left&&r.right<=game.right&&r.top>=game.top&&r.bottom<=game.bottom,overflow:el.scrollHeight>el.clientHeight+1||text.scrollHeight>text.clientHeight+1};
  });assert.deepEqual(box.clipped,[]);assert.equal(box.inside,true);assert.equal(box.overflow,false);
}
try{
  for(const viewport of [{width:320,height:568},{width:375,height:850},{width:430,height:932},{width:844,height:390}]){
    const seed=E.freshState();seed.lastClean-=DAY/4;seed.cleanCycle.dirtyAt=seed.lastClean+DAY;const {c,p}=await seeded(seed,viewport);
    await p.screenshot({path:resolve(out,`kitchen-${viewport.width}.png`)});
    await p.locator('[data-control-id="clean"]').tap();await layout(p);
    assert.equal(await p.getByRole('progressbar').getAttribute('aria-valuenow'),'25');
    assert.equal(await p.locator('[data-yes]').innerText(),'提前打扫 25 CP');
    await p.screenshot({path:resolve(out,`cleaning-${viewport.width}.png`)});await c.close();
  }
  checks.push('320/375/430 宽及横屏：进度与付费按钮完整，弹窗内容无溢出');
  {
    const seed=E.freshState();seed.lastClean-=DAY/4;seed.cleanCycle.dirtyAt=seed.lastClean+DAY;E.startBatch(seed,0,Date.now(),()=>.5);
    const {c,p}=await seeded(seed);const before=await read(p),clean=p.locator('[data-control-id="clean"]');
    await clean.tap();await p.locator('[data-no]').tap();assert.deepEqual(await read(p),before);
    await clean.tap();await p.locator('[data-yes]').tap();
    let saved=await read(p);assert.equal(saved.cp,before.cp-25);assert.equal(saved.dirty,false);assert.deepEqual(saved.batch,before.batch);
    await clean.tap();assert.equal(await p.getByRole('progressbar').getAttribute('aria-valuenow'),'0');assert.equal(await p.locator('[data-yes]').isDisabled(),true);await p.locator('[data-no]').tap();
    await jump(p,DAY/2);await p.waitForTimeout(150);await clean.tap();assert.equal(await p.getByRole('progressbar').getAttribute('aria-valuenow'),'50');
    await p.locator('[data-no]').tap();await p.reload();await p.getByRole('button',{name:'开始游戏',exact:true}).tap();await p.locator('[data-control-id="nav:0"][tabindex="0"]').waitFor();
    // The injected clock restarts on reload; stored lastClean still defines the interval.
    await jump(p,DAY);await p.waitForTimeout(250);await clean.tap();assert.equal(await p.getByRole('progressbar').getAttribute('aria-valuenow'),'100');
    assert.equal(await p.locator('[data-yes]').innerText(),'打扫 100 CP');saved=await read(p);assert.equal(saved.dirty,true);
    await p.locator('[data-yes]').tap();assert.equal((await read(p)).cp,saved.cp-100);
    await p.screenshot({path:resolve(out,'cleaned.png')});await c.close();
    checks.push('取消不扣费，提前打扫扣 25 CP、保留批次，重复点击不扣费，在线进度更新，满一天仍按原价打扫');
  }
  {
    const seed=E.freshState();seed.cp=10;seed.lastClean-=DAY/4;seed.cleanCycle.dirtyAt=seed.lastClean+DAY;
    const {c,p}=await seeded(seed);await p.locator('[data-control-id="clean"]').tap();assert.equal(await p.locator('[data-yes]').isDisabled(),true);
    assert.match(await p.locator('[data-clean-price]').innerText(),/还差 15 CP/);await p.locator('[data-no]').tap();assert.equal((await read(p)).cp,10);await c.close();
    checks.push('余额不足显示差额并禁用支付');
  }
  {
    const seed=E.freshState();seed.lastClean-=DAY/2;seed.cleanCycle.dirtyAt=seed.lastClean+DAY;const {c,p}=await seeded(seed);
    await p.locator('[data-control-id="clean"]').tap();
    await p.evaluate(()=>{const original=Storage.prototype.setItem;Storage.prototype.setItem=function(key,value){if(key.startsWith('chick-kitchen'))throw new Error('QA save unavailable');return original.call(this,key,value);};window.__restoreStorage=()=>{Storage.prototype.setItem=original;};});
    await p.locator('[data-yes]').tap();assert.match(await p.locator('.confirm p').innerText(),/暂未保存/);
    assert.equal((await read(p)).cp,600);await p.evaluate(()=>window.__restoreStorage());await p.locator('[data-yes]').tap();
    await p.locator('[data-control-id="clean"]').tap();assert.equal(await p.getByRole('progressbar').getAttribute('aria-valuenow'),'50');
    await p.locator('[data-yes]').tap();assert.equal((await read(p)).cp,550);await c.close();
    checks.push('保存失败恢复原余额和脏污状态，恢复保存后只扣一次');
  }
  {
    const seed=E.freshState();seed.cp=100000;seed.kitchenLevel=3;seed.toolLevels.fill(1);
    const {c,p}=await seeded(seed);await p.getByRole('button',{name:'厨房',exact:true}).tap();await p.getByRole('button',{name:'补给 · 小卖部',exact:true}).tap();
    for(let id=0;id<9;id++){
      await p.locator(`[data-shop-tool-details="${id}"]`).tap();
      assert.equal(await p.locator('.shop-growth-stages .sprite-art').count(),3);
      assert.ok((await p.locator('.shop-growth-stages image').evaluateAll(els=>els.map(el=>el.getAttribute('href')))).every(path=>path.startsWith('/web/art/')));
      if(id===0||id===7)await p.screenshot({path:resolve(out,`growth-${id}.png`)});
      await p.locator('[data-shop-detail-back]').tap();
    }
    await c.close();checks.push('九类厨具成长册均展示独立三级图像');
  }
  {
    const seed=E.freshState();const {c,p}=await seeded(seed,{width:1100,height:1080});
    await p.evaluate(async()=>{
      const {toolImage}=await import('/web/catalog.js'),{resolveSprite,spriteSVG}=await import('/web/art/manifest.js'),{tool,label}=await import('/web/engine.js');
      document.body.innerHTML='<main><h1>厨具成长册</h1><p>九类厨具 · 三级外观 · 厨房与商店共用</p><div class="grid"></div></main>';
      const css=document.createElement('style');css.textContent='html,body{overflow:auto;background:#f7edcf}main{padding:20px 24px;font-family:Chicken UI,sans-serif;color:#553c2b}h1{margin:0;font-size:26px}main>p{margin:8px 0 20px}.grid{display:grid;grid-template-columns:repeat(3,1fr);gap:14px}.card{background:#fff9e6;border:1px solid #bfa373;border-radius:14px;padding:12px}.card h2{margin:0 0 8px;font-size:16px}.levels{display:flex;gap:6px}.level{flex:1;text-align:center}.level svg{display:block;width:100%;height:68px}.level span{font-size:12px}';document.head.append(css);
      document.querySelector('.grid').innerHTML=Array.from({length:9},(_,id)=>`<section class="card"><h2>${label(tool(id))}</h2><div class="levels">${[0,1,2].map(l=>`<div class="level">${spriteSVG(resolveSprite(toolImage(1,id,l)))}<span>Lv.${l+1}</span></div>`).join('')}</div></section>`).join('');
      await document.fonts.ready;
    });await p.waitForTimeout(500);await p.locator('main').screenshot({path:resolve(out,'all-cookware.png')});await c.close();
  }
  assert.deepEqual(errors,[]);assert.deepEqual(requests,[]);
  const report={checkedAt:new Date().toISOString(),checks,errors,requests,nativeDeviceTest:false};await writeFile(resolve(out,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));
}finally{await browser.close();}
