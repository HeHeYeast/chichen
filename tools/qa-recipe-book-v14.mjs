import {createRequire} from 'node:module';
import {resolve} from 'node:path';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
const {chromium}=createRequire(import.meta.url)(resolve(process.argv[2]));
const base=process.argv[3]??'http://127.0.0.1:4173',out=resolve(process.argv[4]??'artifacts/qa/recipe-book-v14');await mkdir(out,{recursive:true});
const browser=await chromium.launch({headless:true}),checks=[],errors=[],failedRequests=[];
const watch=p=>{p.on('pageerror',e=>errors.push(e.message));p.on('response',r=>{if(r.status()>=400)failedRequests.push(r.url());});};
const read=p=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-review-v1')));
const durable=s=>({cp:s.cp,total:s.total,farm:s.farm,ingredients:s.ingredients,batch:s.batch,selected:s.selected,egg:s.egg,events:s.events});
async function fixture(p,name){await p.evaluate(name=>postMessage({type:'chick-review',command:'scene',value:name},location.origin),name);await p.waitForTimeout(150);}
async function panelFits(p){return p.locator('.screen-panel').evaluate(panel=>{const r=panel.getBoundingClientRect(),f=panel.querySelector('footer').getBoundingClientRect();return{overflow:panel.scrollWidth-panel.clientWidth,bottom:f.bottom,limit:r.bottom,viewport:innerHeight};});}
try{
  const c=await browser.newContext({viewport:{width:375,height:747},reducedMotion:'reduce'}),p=await c.newPage();watch(p);
  await p.goto(base+'/web/index.html?review=1');await p.getByRole('button',{name:'开始游戏',exact:true}).waitFor();
  await fixture(p,'v14-new');await p.locator('.cookbook-empty').waitFor();assert.equal(await p.locator('[data-cookbook-recipe]').count(),0);
  for(const tool of [0,1,8,-1]){await p.locator(`[data-cookbook-tool="${tool}"]`).click();assert.equal(await p.locator('[data-cookbook-recipe]').count(),0);}
  await fixture(p,'v14-unknown');const unknown=await p.locator('.workshop-screen').innerText();assert.ok(unknown.includes('尚未收录'));assert.ok(!unknown.includes('奶黄')&&!unknown.includes('鲜奶油'));assert.equal(await p.locator('[data-species-recipe],[data-species-seasonal]').count(),0);
  await p.screenshot({path:resolve(out,'unknown-hidden.png')});checks.push('新档所有厨具配方为空，未知档案不显示名称、材料或配方链接');
  await fixture(p,'v14-recipes');await p.locator('[data-cookbook-recipe="0:6"]').waitFor();assert.equal(await p.locator('[data-cookbook-tool]').count(),10);
  await p.locator('[data-cookbook-egg="1"]').click();const ducks=await p.locator('[data-cookbook-recipe]').evaluateAll(nodes=>nodes.map(n=>n.dataset.cookbookRecipe));assert.ok(ducks.length&&ducks.every(k=>k.startsWith('1:')));
  await p.locator('[data-cookbook-egg="-1"]').click();await p.locator('[data-cookbook-tool="8"]').click();assert.equal(await p.locator('[data-cookbook-recipe]').count(),6);
  await p.locator('[data-cookbook-recipe="0:117"]').click();assert.match(await p.locator('.cookbook-mixture').innerText(),/奶油/);await p.locator('[data-cookbook-back]').click();assert.equal(await p.locator('[data-cookbook-tool="8"]').getAttribute('aria-pressed'),'true');
  await p.getByRole('button',{name:'厨房',exact:true}).click();await p.getByRole('button',{name:'补给 · 小卖部',exact:true}).click();assert.equal(await p.locator('[data-shop-recipes],.expansion-screen').count(),0);await p.getByRole('tab',{name:'调味料',exact:true}).click();assert.equal(await p.locator('[data-shop-recipes]').count(),0);
  checks.push('九类厨具与特殊变化可切换、鸡鸭可筛选、蒸笼六份已合并；商店旧入口移除');
  await fixture(p,'v14-species');await p.locator('[data-species-recipe="1:30"]').click();assert.match(await p.locator('.cookbook-mixture').innerText(),/红酒/);assert.equal(await p.locator('.cookbook-mixture>span').count(),3);
  await p.locator('[data-cookbook-back]').click();await p.locator('[data-species-recipe="1:30"]').waitFor();
  await p.locator('[data-species-inventory]').click();await p.locator('[data-harvest-character="30"]').click();await p.locator('[data-species-sell]').click();await p.locator('[data-yes]').click();assert.equal((await read(p)).farm['1:30'],0);await p.locator('[data-species-recipe="1:30"]').click();await p.locator('.cookbook-detail').waitFor();
  checks.push('鸭宝档案直达三材料配方并可返回；售罄后配方仍在');
  await fixture(p,'v14-busy');const before=durable(await read(p));await p.locator('[data-cookbook-prepare]').click();await p.locator('[data-control-id="tool:8"]').waitFor();const prepared=await read(p);
  assert.deepEqual(prepared.batch,before.batch);assert.deepEqual(prepared.ingredients,before.ingredients);assert.equal(prepared.cp,before.cp);assert.deepEqual(prepared.selected,[24,37]);assert.equal(prepared.egg,0);
  await p.locator('[data-control-id="tool:8"]').click();await p.locator('[data-no]').click();assert.deepEqual((await read(p)).batch,before.batch);checks.push('准备点心配方只改下一批选择，已有鸭蛋批次和CP、库存保留；放弃重开确认可取消');
  await fixture(p,'v14-detail');const beforeShop=durable(await read(p));await p.locator('[data-cookbook-prepare]').click();await p.locator('.shop-detail-screen').waitFor();assert.deepEqual(durable(await read(p)),beforeShop);
  const materialId=Number(await p.locator('[data-shop-detail-ingredient]').getAttribute('data-shop-detail-ingredient'));
  await p.locator('[data-shop-detail-ingredient]').click();await p.locator('[data-yes]').click();
  const afterSupply=await read(p);assert.equal(afterSupply.ingredients[materialId],(beforeShop.ingredients[materialId]??0)+1);assert.ok(afterSupply.cp<beforeShop.cp);
  await p.getByRole('button',{name:'离开商店',exact:true}).click();await p.locator('.cookbook-detail').waitFor();
  assert.deepEqual((await read(p)).batch,beforeShop.batch);assert.deepEqual((await read(p)).selected,beforeShop.selected);
  checks.push('配方缺料直达商品，不自动扣费；确认购买一份、关闭货架后回原配方，当前锅与下一锅草稿保留');
  await fixture(p,'v14-calendar');await p.locator('[data-journal-chapter="spring"]').click();assert.equal(await p.locator('[data-seasonal-recipe]').count(),1);const chapterText=await p.locator('.journal-scroll').innerText();assert.ok(!chapterText.includes('蜂蜜松饼鸡'));await p.locator('[data-seasonal-recipe="0:120"]').click();await p.locator('.cookbook-detail').waitFor();
  const beforeSeason=durable(await read(p));await p.locator('[data-cookbook-prepare]').click();await p.locator('[data-control-id="tool:2"]').waitFor();const season=await read(p);assert.equal(season.events.seasonalRecipe,'0:120');assert.deepEqual(season.ingredients,beforeSeason.ingredients);
  await p.reload();await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.locator('[data-control-id="nav:0"][tabindex="0"]').waitFor();assert.equal((await read(p)).events.seasonalRecipe,'0:120');await p.getByRole('button',{name:'图鉴',exact:true}).click();await p.locator('[data-book-tab="species"]').click();await p.locator('[data-collection-recipes]').click();await p.locator('[data-cookbook-tool="2"]').click();await p.locator('[data-cookbook-recipe="0:120"]').waitFor();
  checks.push('四时手记隐藏未知配方，已收录配方转到统一册子；定向选择与解锁跨重开保留');
  await fixture(p,'v14-recipes');await p.locator('[data-cookbook-tool="-1"]').click();await p.locator('[data-cookbook-recipe="0:34"]').click();assert.equal(await p.locator('[data-cookbook-prepare]').isDisabled(),true);assert.match(await p.locator('.cookbook-note').innerText(),/病变/);
  checks.push('病变等特殊伙伴有真实出现方式，不提供误导的直接制作按钮');
  await fixture(p,'v14-harvest');const farmBefore=durable(await read(p)),stocks=Object.values(farmBefore.farm),all=stocks.reduce((a,b)=>a+b,0),extras=stocks.reduce((a,b)=>a+Math.max(0,b-1),0);
  await p.locator('[data-harvest-all]').click();assert.equal(Number(await p.locator('[data-harvest-total]').innerText()),all);assert.deepEqual(durable(await read(p)),farmBefore);
  await p.locator('[data-harvest-keep-one]').click();assert.equal(Number(await p.locator('[data-harvest-total]').innerText()),extras);
  await p.locator('[data-harvest-egg="1"]').click();assert.equal(await p.locator('[data-harvest-row="1:0"] [data-harvest-quantity]').innerText(),'3');
  await p.locator('[data-harvest-sell]').click();await p.locator('[data-no]').click();assert.deepEqual(durable(await read(p)),farmBefore);
  await p.screenshot({path:resolve(out,'bulk-keep-one.png')});await p.locator('[data-harvest-sell]').click();await p.locator('[data-yes]').click();
  const sold=await read(p);assert.ok(Object.values(sold.farm).every(q=>q===1));assert.deepEqual(sold.total,farmBefore.total);assert.ok(sold.cp>farmBefore.cp);assert.equal(await p.locator('[data-harvest-keep-one]').isDisabled(),true);
  await p.locator('[data-harvest-all]').click();await p.locator('[data-harvest-sell]').click();await p.locator('[data-yes]').click();assert.ok(Object.values((await read(p)).farm).every(q=>q===0));assert.equal(await p.locator('[data-harvest-all]').isDisabled(),true);assert.equal(await p.locator('[data-harvest-sell]').isDisabled(),true);
  checks.push('批量全选覆盖鸡鸭库存，每种留一只可调整或取消；确认后各品种保留一只，图鉴不变，售罄禁用');
  await c.close();
  for(const [width,height]of [[320,568],[375,747],[430,932]]){
    const device=await browser.newContext({viewport:{width,height},reducedMotion:'reduce'}),page=await device.newPage();watch(page);await page.goto(base+'/web/index.html?review=1');await page.getByRole('button',{name:'开始游戏',exact:true}).waitFor();
    for(const name of ['v14-recipes','v14-detail','v14-steamer','v14-species','v14-new','v14-harvest']){await fixture(page,name);const layout=await panelFits(page);assert.ok(layout.overflow<=1&&layout.bottom<=layout.limit+1&&layout.bottom<layout.viewport);await page.screenshot({path:resolve(out,`${name}-${width}.png`)});}
    await device.close();
  }
  checks.push('320／375／430宽的配方列表、详情、档案、蒸笼和空状态无横向溢出，页脚保持可见');
  const s=E.freshState();s.kitchenLevel=3;s.toolLevels.fill(2);s.cp=20000;s.total['0:6']=1;s.ingredients={1:2};
  const nc=await browser.newContext({viewport:{width:375,height:747}});await nc.addInitScript(raw=>{const m=window.__bookNative={raw,fail:false};window.ChickNative={platformInfo:()=>JSON.stringify({android:true,version:'1.4.0-test'}),loadSave:()=>JSON.stringify({status:'ok',raw:m.raw}),saveGame:next=>{if(m.fail)return JSON.stringify({ok:false,message:'模拟保存失败'});m.raw=next;return JSON.stringify({ok:true});},notificationStatus:()=>JSON.stringify({supported:true,permissionGranted:true,exactAllowed:true}),ready(){},closeApp(){}};},JSON.stringify(s));
  const n=await nc.newPage();watch(n);await n.goto(base+'/');await n.getByRole('button',{name:'开始游戏',exact:true}).click();await n.locator('[data-control-id="nav:0"][tabindex="0"]').waitFor();await n.getByRole('button',{name:'图鉴',exact:true}).click();await n.locator('[data-book-tab="species"]').click();await n.locator('[data-collection-recipes]').click();await n.locator('[data-cookbook-tool="1"]').click();await n.locator('[data-cookbook-recipe="0:6"]').click();await n.evaluate(()=>window.__bookNative.fail=true);await n.locator('[data-cookbook-prepare]').click();await n.locator('[data-yes]').click();assert.deepEqual(await n.evaluate(()=>JSON.parse(window.__bookNative.raw).selected),[]);
  await n.evaluate(()=>window.__bookNative.fail=false);await n.locator('[data-cookbook-prepare]').click();await n.locator('[data-control-id="tool:1"]').waitFor();assert.deepEqual(await n.evaluate(()=>JSON.parse(window.__bookNative.raw).selected),[1]);await nc.close();checks.push('模拟Android保存失败不提交配方，恢复后可再准备，未操作手机');
  assert.deepEqual(errors,[]);assert.deepEqual(failedRequests,[]);const report={checkedAt:new Date().toISOString(),checks,errors,failedRequests,nativeDeviceTest:false};await writeFile(resolve(out,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));
}catch(error){console.error(JSON.stringify({checks,errors,failedRequests},null,2));throw error;}finally{await browser.close();}
