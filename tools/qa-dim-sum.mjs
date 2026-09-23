// Usage: node tools/qa-dim-sum.mjs <existing-playwright-package-directory>
// Runs in an isolated browser context and only uses the review fixture store.
import {createRequire} from 'node:module';
import {resolve} from 'node:path';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';

if(!process.argv[2])throw Error('Pass an existing Playwright package directory; this script does not install dependencies.');
const {chromium}=createRequire(import.meta.url)(resolve(process.argv[2]));
const base=process.argv[3]??'http://127.0.0.1:4173';
const output=resolve('artifacts/qa/dim-sum-v9');await mkdir(output,{recursive:true});
const browser=await chromium.launch({headless:true});
const context=await browser.newContext({viewport:{width:1200,height:1100},deviceScaleFactor:1});
const page=await context.newPage(),errors=[],failedRequests=[];
page.on('pageerror',error=>errors.push(error.message));
page.on('response',response=>{if(response.status()>=400)failedRequests.push({status:response.status(),url:response.url()});});
const frame=page.frameLocator('#preview');
const state=()=>page.locator('#preview').evaluate(iframe=>JSON.parse(iframe.contentWindow.localStorage.getItem('chick-kitchen-review-v1')));
const screenshot=name=>page.locator('#preview').screenshot({path:resolve(output,name+'.png')});
const load=async(scene,dialog)=>{
  await page.goto(base+'/review?scene='+scene);
  if(dialog)await frame.getByRole('dialog',{name:dialog,exact:true}).waitFor({state:'visible'});
  else await frame.getByRole('button',{name:'使用竹蒸笼',exact:true}).waitFor({state:'visible'});
  await page.locator('#status').filter({hasText:'25000 CP'}).waitFor({state:'visible'});
};
try{
  await load('v9-dim-sum-recipes','竹笼点心坊');
  const layouts=[];
  for(const width of [320,375,430]){
    await page.locator(`[data-width="${width}"]`).click();
    await page.waitForFunction(expected=>Math.abs(document.querySelector('#preview').contentDocument.querySelector('#game').getBoundingClientRect().width-expected)<.6,width);
    const layout=await frame.locator('.expansion-screen').evaluate(panel=>{
      const rect=panel.getBoundingClientRect(),page=panel.querySelector('.recipe-page'),footer=panel.querySelector('footer').getBoundingClientRect();
      const clipped=[...panel.querySelectorAll('button')].filter(button=>{const r=button.getBoundingClientRect();return r.left<rect.left-.5||r.right>rect.right+.5||r.top<rect.top-.5||r.bottom>rect.bottom+.5;}).map(button=>button.textContent.trim());
      return {width:Math.round(rect.width),height:Math.round(rect.height),pageClient:page.clientHeight,pageScroll:page.scrollHeight,footerInside:footer.bottom<=rect.bottom,clipped};
    });
    assert.deepEqual(layout.clipped,[]);assert.equal(layout.footerInside,true);layouts.push({viewport:width,...layout});
    await screenshot('recipe-'+width);
  }
  const before=await state();
  await frame.getByRole('button',{name:'选好配方',exact:true}).click();
  await frame.getByRole('button',{name:'使用竹蒸笼',exact:true}).waitFor({state:'visible'});
  const prepared=await state();
  assert.deepEqual(prepared.selected,[24,37]);assert.equal(prepared.cp,before.cp);
  assert.deepEqual(prepared.ingredients,before.ingredients);assert.deepEqual(prepared.batch,before.batch);
  assert.equal(await frame.getByRole('button',{name:'向右滚动厨具',exact:true}).isDisabled(),true);
  await screenshot('prepared-kitchen');
  await frame.getByRole('button',{name:'使用竹蒸笼',exact:true}).click();
  await frame.locator('[data-no]').click();assert.equal((await state()).cp,25000);

  await load('v9-dim-sum');
  await frame.getByRole('button',{name:'使用竹蒸笼',exact:true}).waitFor({state:'visible'});
  const firstBefore=await state();
  await frame.getByRole('button',{name:'收取小笼包鸡 1',exact:true}).click();
  assert.equal((await state()).farm['0:114'],firstBefore.farm['0:114']+1);
  assert.equal((await state()).cp,firstBefore.cp+1);
  await screenshot('harvested-kitchen');

  await load('v9-dim-sum-detail','伙伴档案');
  assert.ok((await frame.locator('.species-story').textContent()).includes('奶黄'));
  assert.ok((await frame.locator('.species-recipe').textContent()).includes('奶油 ＋ 白砂糖'));
  const quantityFits=await frame.locator('.species-screen').evaluate(panel=>{
    const quantity=panel.querySelector('.species-quantity').getBoundingClientRect(),footer=panel.querySelector('footer').getBoundingClientRect();
    return quantity.top>=panel.getBoundingClientRect().top&&quantity.bottom<=footer.top;
  });
  assert.equal(quantityFits,true);
  await screenshot('species-detail');

  await load('v9-dim-sum-shop','鸡宝小卖部');
  await frame.locator('[data-shop-buy-tool="8"]').scrollIntoViewIfNeeded();
  assert.equal(await frame.locator('[data-shop-buy-tool="8"]').isDisabled(),true);
  await screenshot('steamer-shelf');
  await frame.locator('[data-shop-recipes]').click();
  await frame.getByRole('button',{name:'第6味 寿桃豆沙鸡',exact:true}).click();
  assert.ok((await frame.locator('.recipe-page h3').textContent()).includes('寿桃豆沙鸡'));
  assert.equal(await frame.getByRole('button',{name:'下一份配方',exact:true}).isDisabled(),true);
  await screenshot('final-recipe');
  await frame.getByRole('button',{name:'合上点心配方册',exact:true}).click();
  await frame.getByRole('dialog',{name:'鸡宝小卖部',exact:true}).waitFor({state:'visible'});

  await page.goto(base+'/review?scene=v8-shop');
  await frame.getByRole('dialog',{name:'鸡宝小卖部',exact:true}).waitFor({state:'visible'});
  await frame.locator('[data-shop-recipes]').click();
  await frame.getByRole('button',{name:'第6味 寿桃豆沙鸡',exact:true}).click();
  assert.ok((await frame.locator('.recipe-status').textContent()).includes('Lv.3'));
  await screenshot('locked-recipe');
  await frame.locator('[data-recipe-prepare]').click();
  await frame.getByRole('dialog',{name:'鸡宝小卖部',exact:true}).waitFor({state:'visible'});

  // Exercise the real purchase -> ingredient -> recipe -> cooking path, rather
  // than relying only on fixtures with all content already owned.
  const shoppingBefore=await state();assert.equal(shoppingBefore.toolLevels[8],-1);
  await frame.locator('[data-shop-buy-tool="8"]').click();await frame.locator('[data-yes]').click();
  assert.equal((await state()).cp,shoppingBefore.cp-3000);assert.equal((await state()).toolLevels[8],0);
  await frame.locator('[data-shop-recipes]').click();await frame.locator('[data-recipe-id="116"]').click();
  assert.equal(await frame.locator('[data-recipe-prepare]').textContent(),'去补齐原料');
  await frame.locator('[data-recipe-prepare]').click();
  await frame.locator('[data-shop-buy-ingredient="16"]').click();await frame.locator('[data-yes]').click();
  assert.equal((await state()).ingredients[16],1);assert.equal((await state()).cp,shoppingBefore.cp-3025);
  await frame.locator('[data-shop-recipes]').click();await frame.locator('[data-recipe-prepare]').click();
  await frame.getByRole('button',{name:'使用竹蒸笼',exact:true}).click();await frame.locator('[data-yes]').click();
  const cooking=await state();
  assert.equal(cooking.cp,shoppingBefore.cp-3145);assert.equal(cooking.ingredients[16],0);
  assert.equal(cooking.batch.tool,8);assert.equal(cooking.batch.level,0);assert.equal(cooking.batch.eggs.length,24);
  assert.equal(cooking.batch.ends-cooking.batch.started,45*60000);assert.ok(cooking.batch.eggs.some(egg=>egg.id===116));
  await screenshot('real-first-recipe');

  assert.deepEqual(errors,[]);assert.deepEqual(failedRequests,[]);
  const report={checkedAt:new Date().toISOString(),layouts,errors,failedRequests,checks:['320/375/430配方册边界','选择配方保留库存/CP/已有批次','第九厨具聚焦与下一页禁用','取消重新调理不扣费','收取新品种入农场并加CP','新品档案配方与描述及固定数量栏','蒸笼满级货架','六味书签与返回','未解锁配方跳厨具货架','真实购买蒸笼3000CP→补糯米25CP→选配方→调理120CP，24蛋包含目标品种']};
  await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2));
  console.log(JSON.stringify(report,null,2));
}finally{await browser.close();}
