// Isolated review/native-mock profiles only. Never touches the user's save.
import {createRequire} from 'node:module';
import {resolve} from 'node:path';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {claimActivity} from '../web/legacy-activities.js';
const {chromium}=createRequire(import.meta.url)(resolve(process.argv[2]));
const base=process.argv[3]??'http://127.0.0.1:4173',output=resolve('artifacts/qa/shrine-v11');
await mkdir(output,{recursive:true});
const browser=await chromium.launch({headless:true}),checks=[],errors=[],screens=[];
const read=page=>page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-review-v1')));
const durable=s=>({cp:s.cp,events:s.events,ingredients:s.ingredients,selected:s.selected,egg:s.egg,batch:s.batch,farm:s.farm,total:s.total});
const watch=page=>page.on('pageerror',e=>errors.push(e.message));
async function fixture(page,name){
  await page.evaluate(name=>new Promise(done=>{
    const listener=e=>{if(e.source===window&&e.data?.type==='chick-status'&&e.data.loaded){removeEventListener('message',listener);done();}};
    addEventListener('message',listener);postMessage({type:'chick-review',command:'scene',value:name},location.origin);
  }),name);
  await page.evaluate(()=>new Promise(done=>requestAnimationFrame(()=>requestAnimationFrame(done))));
}
async function shot(page,name){const file=resolve(output,name+'.png');await page.screenshot({path:file,animations:'disabled'});screens.push(file);}
async function fits(page){
  const box=await page.locator('.screen-panel').evaluate(el=>{
    const p=el.getBoundingClientRect(),footer=el.querySelector('footer').getBoundingClientRect(),game=document.querySelector('#game').getBoundingClientRect();
    return {width:el.clientWidth,scrollWidth:el.scrollWidth,pBottom:p.bottom,footerBottom:footer.bottom,gameBottom:game.bottom};
  });
  assert.ok(box.scrollWidth<=box.width+1,JSON.stringify(box));assert.ok(box.footerBottom<=box.pBottom+1,JSON.stringify(box));assert.ok(box.pBottom<box.gameBottom,JSON.stringify(box));
}
try{
  const context=await browser.newContext({viewport:{width:375,height:747},deviceScaleFactor:2}),page=await context.newPage();watch(page);
  await page.goto(base+'/web/index.html?review=1');await page.getByRole('button',{name:'开始游戏',exact:true}).waitFor();
  await fixture(page,'v11-new');assert.equal(await page.locator('[data-shrine-draw]').isDisabled(),true);
  await page.locator('[data-shrine-letters]').click();await page.getByRole('dialog',{name:'神社来信',exact:true}).waitFor();
  await page.locator('.close').click();await page.getByRole('dialog',{name:'神社委托簿',exact:true}).waitFor();
  await page.getByRole('button',{name:'农场',exact:true}).click();assert.equal(await page.locator('.panel').count(),0);
  await page.getByRole('button',{name:'前往神社求签',exact:true}).click();await page.locator('.shrine-screen').waitFor();
  checks.push('新玩家显示具体前置；来信详情返回上一级；当前农场导航关闭弹层；快捷入口可用');

  await fixture(page,'v11-draw');const before=durable(await read(page));
  await page.locator('[data-shrine-draw]').click();await page.locator('.shrine-slip').waitFor();
  const drawn=await read(page);assert.equal(drawn.ingredients[68],1);assert.equal(drawn.events.legacyOmikujiCount,1);assert.equal(drawn.cp,before.cp);
  assert.equal(await page.locator('[data-shrine-draw]').count(),0);assert.equal(await page.locator('[data-shrine-prepare]').count(),1);await shot(page,'drawn-375');
  await page.reload();await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.getByRole('button',{name:'农场',exact:true}).click();await page.locator('[data-control-id="farm:fortune"]').click();
  assert.equal((await read(page)).events.legacyOmikujiCount,1);assert.equal(await page.locator('[data-shrine-prepare]').count(),1);
  checks.push('一次点击领取免费签，结果可见，重载后签和领取记录保留且不能重复领取');

  await fixture(page,'v11-busy');const busy=durable(await read(page));
  await page.locator('[data-shrine-prepare]').click();await page.locator('.game-toast').waitFor();const prepared=await read(page);
  assert.deepEqual(prepared.batch,busy.batch);assert.equal(prepared.cp,busy.cp);assert.deepEqual(prepared.ingredients,busy.ingredients);assert.deepEqual(prepared.selected,[68]);
  await page.getByRole('button',{name:'使用保温灯',exact:true}).click();assert.equal(await page.locator('[data-yes]').textContent(),'放弃并重开');assert.equal(await page.locator('[data-no]').textContent(),'继续照顾');
  await page.locator('[data-no]').click();assert.deepEqual(durable(await read(page)),durable(prepared));
  checks.push('配好下一批只改变选择，活动批次/库存/CP保留；重开明确提示放弃数量，取消无消耗');

  await fixture(page,'v11-ingredients');const ingredientsBefore=durable(await read(page));
  await page.locator('.ingredients-grid').evaluate(el=>{el.scrollTop=el.scrollHeight;});
  const scroll=await page.locator('.ingredients-grid').evaluate(el=>el.scrollTop);
  for(const id of [29,28,27])await page.locator(`[data-id="${id}"]`).click();
  assert.ok(Math.abs(await page.locator('.ingredients-grid').evaluate(el=>el.scrollTop)-scroll)<1);
  await page.locator('[data-id="26"]').click();assert.ok((await page.locator('.counter').textContent()).includes('已选满'));
  assert.equal(await page.locator('.ingredient.selected').count(),3);assert.deepEqual(durable(await read(page)),ingredientsBefore);
  await page.getByRole('button',{name:'厨房',exact:true}).click();assert.equal(await page.locator('.panel').count(),0);assert.deepEqual(durable(await read(page)),ingredientsBefore);
  checks.push('材料列表选择不跳顶；槽满有文字反馈；返回厨房放弃草稿且不扣库存');

  await fixture(page,'v11-settings');const toggle=page.locator('[data-toggle="alarm"]');await toggle.scrollIntoViewIfNeeded();
  const settingsScroll=await page.locator('.settings-content').evaluate(el=>el.scrollTop);await toggle.click();
  assert.ok(Math.abs(await page.locator('.settings-content').evaluate(el=>el.scrollTop)-settingsScroll)<1);
  assert.equal(await page.locator('[data-toggle="alarm"]').getAttribute('aria-checked'),'true');
  checks.push('设置开关重绘保留滚动位置和状态');

  await fixture(page,'v11-goals');const rewardsBefore=await read(page);await page.locator('[data-shrine-reward="signs-3"]').click();
  const rewarded=await read(page);assert.equal(rewarded.cp,rewardsBefore.cp+300);assert.equal(rewarded.events.shrineCollections['signs-3'],true);assert.equal(await page.locator('[data-shrine-reward="signs-3"]').count(),0);
  await page.reload();await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.getByRole('button',{name:'农场',exact:true}).click();await page.locator('[data-control-id="farm:fortune"]').click();await page.locator('[data-shrine-tab="goals"]').click();
  assert.equal(await page.locator('[data-shrine-reward="signs-3"]').count(),0);assert.equal((await read(page)).cp,rewarded.cp);
  checks.push('收藏回礼按真实旧收藏解锁，一次领取保存CP和印记，重开不能重复领取');

  await page.locator('[data-shrine-reward="duck-5"]').scrollIntoViewIfNeeded();
  const goalScroll=await page.locator('.shrine-body').evaluate(el=>el.scrollTop);
  await page.locator('[data-shrine-reward="duck-5"]').click();
  assert.ok(Math.abs(await page.locator('.shrine-body').evaluate(el=>el.scrollTop)-goalScroll)<1);
  assert.equal(await page.locator('[data-shrine-claimed="duck-5"]').textContent(),'已收下 +600 CP');
  checks.push('领取下方回礼后保留滚动位置，奖励在原位置显示');

  await fixture(page,'v11-draw');await page.locator('[data-shrine-back]').click();await page.locator('[data-activity-tab="gifts"]').click();await page.locator('[data-activity-id="flame-gift"]').click();
  await page.locator('[data-activity-claim]').click();await page.locator('[data-yes]').click();await page.locator('[data-yes]').click();
  await page.locator('[data-activity-prepare="69"]').click();assert.deepEqual((await read(page)).selected,[69]);
  checks.push('火苗赠品领取后也能直接准备平底锅配方');

  assert.equal(await page.locator('.game-toast').isVisible(),true);
  await page.getByRole('button',{name:'厨房',exact:true}).click();await page.getByRole('button',{name:'补给 · 小卖部',exact:true}).click();
  assert.equal(await page.locator('.game-toast').isVisible(),false);
  checks.push('离开厨房后关闭配方提示，避免覆盖下一界面的标题');

  await page.getByRole('button',{name:'厨房',exact:true}).click();await page.getByRole('button',{name:'补给 · 小卖部',exact:true}).click();await page.locator('[data-shop-tool-details="0"]').click();await page.getByRole('button',{name:'厨房',exact:true}).click();await page.getByRole('button',{name:'补给 · 小卖部',exact:true}).click();
  assert.equal(await page.locator('.shop-detail-screen').count(),0);assert.equal(await page.locator('.shop-shelf-scroll').count(),1);
  checks.push('当前商店导航从详情回到货架');

  for(const [width,height] of [[320,568],[375,747],[430,932]]){
    await page.setViewportSize({width,height});
    for(const scene of ['v11-new','v11-draw','v11-held','v11-book','v11-goals']){
      await fixture(page,scene);await fits(page);await shot(page,scene+'-'+width);
    }
  }
  checks.push('320/375/430宽的求签、签纸、签册、回礼和锁定状态无横溢出，页脚在面板内');
  await context.close();

  const s=E.freshState();s.cp=9000;s.kitchenLevel=3;s.toolLevels.fill(2);for(let id=0;id<18;id++)s.total['0:'+id]=120;
  for(const id of [89,90,91])s.total['0:'+id]=1;claimActivity(s,'shrine');
  const nativeContext=await browser.newContext({viewport:{width:375,height:747}});
  await nativeContext.addInitScript(raw=>{
    const mock=window.__qaNative={raw,fail:false};
    window.ChickNative={platformInfo:()=>JSON.stringify({android:true,version:'1.2.0-test'}),loadSave:()=>JSON.stringify({status:'ok',raw:mock.raw}),saveGame:next=>{if(mock.fail)return JSON.stringify({ok:false,message:'模拟保存失败'});mock.raw=next;return JSON.stringify({ok:true});},notificationStatus:()=>JSON.stringify({supported:true,permissionGranted:true,notificationsEnabled:true,exactAllowed:true,message:'测试通知状态'}),ready(){},closeApp(){}};
  },JSON.stringify(s));
  const native=await nativeContext.newPage();watch(native);await native.goto(base+'/web/index.html');await native.locator('[data-control-id="tool:0" ]').waitFor({state:'visible'});await native.getByRole('button',{name:'农场',exact:true}).click();await native.locator('[data-control-id="farm:fortune"]').click();
  await native.evaluate(()=>{window.__qaNative.fail=true;});await native.locator('[data-shrine-draw]').click();await native.locator('.confirm p').filter({hasText:'进度暂未保存'}).waitFor();await native.locator('[data-yes]').click();
  assert.equal(await native.locator('[data-shrine-draw]').isEnabled(),true);assert.equal(await native.evaluate(()=>JSON.parse(window.__qaNative.raw).ingredients[68]),undefined);
  await native.evaluate(()=>{window.__qaNative.fail=false;});await native.locator('[data-shrine-draw]').click();assert.equal(await native.locator('[data-shrine-prepare]').count(),1);
  await native.locator('[data-shrine-tab="goals"]').click();await native.evaluate(()=>{window.__qaNative.fail=true;});await native.locator('[data-shrine-reward="signs-3"]').click();await native.locator('[data-yes]').click();
  assert.equal(await native.locator('[data-shrine-reward="signs-3"]').isEnabled(),true);assert.equal(await native.evaluate(()=>JSON.parse(window.__qaNative.raw).cp),9000);
  await native.evaluate(()=>{window.__qaNative.fail=false;});await native.locator('[data-shrine-reward="signs-3"]').click();assert.equal(await native.evaluate(()=>JSON.parse(window.__qaNative.raw).cp),9300);
  checks.push('模拟Android：求签和收藏回礼保存失败时回退，恢复后可正常各领一次');

  await native.getByRole('button',{name:'厨房',exact:true}).click();await native.getByRole('button',{name:'补给 · 小卖部',exact:true}).click();await native.getByRole('tab',{name:'调味料',exact:true}).click();
  const original=await native.evaluate(()=>JSON.parse(window.__qaNative.raw));await native.evaluate(()=>{window.__qaNative.fail=true;});
  await native.locator('[data-shop-buy-ingredient="0"]').click();await native.locator('[data-yes]').click();await native.locator('.confirm p').filter({hasText:'进度暂未保存'}).waitFor();await native.locator('[data-yes]').click();
  assert.equal(await native.evaluate(()=>JSON.parse(window.__qaNative.raw).cp),original.cp);
  assert.ok((await native.locator('[data-shop-ingredient-details="0"]').textContent()).includes('持有 '+original.ingredients[0]));
  await native.evaluate(()=>{window.__qaNative.fail=false;});await native.locator('[data-shop-buy-ingredient="0"]').click();await native.locator('[data-yes]').click();
  assert.equal(await native.evaluate(()=>JSON.parse(window.__qaNative.raw).ingredients[0]),original.ingredients[0]+1);
  checks.push('模拟Android：普通商店购买保存失败也回退内存库存，恢复保存后购买一次');
  await nativeContext.close();
  assert.deepEqual(errors,[]);
  const report={checkedAt:new Date().toISOString(),checks,errors,screens,nativeDeviceTest:false};await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));
}catch(error){console.error(JSON.stringify({checks,errors},null,2));throw error;}finally{await browser.close();}
