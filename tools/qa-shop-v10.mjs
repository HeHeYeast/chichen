// Isolated development browser: only the review fixture store is read or changed.
import {createRequire} from 'node:module';
import {resolve} from 'node:path';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
if(!process.argv[2])throw Error('Pass an existing Playwright package directory.');
const {chromium}=createRequire(import.meta.url)(resolve(process.argv[2]));
const base=process.argv[3]??'http://127.0.0.1:4173';
const output=resolve('artifacts/qa/shop-v10');await mkdir(output,{recursive:true});
const browser=await chromium.launch({headless:true});
const context=await browser.newContext({viewport:{width:1200,height:1100},deviceScaleFactor:1});
const page=await context.newPage(),errors=[],failedRequests=[],layouts=[];
page.on('pageerror',error=>errors.push(error.message));
page.on('response',response=>{if(response.status()>=400)failedRequests.push({status:response.status(),url:response.url()});});
const frame=page.frameLocator('#preview');
const state=()=>page.locator('#preview').evaluate(iframe=>JSON.parse(iframe.contentWindow.localStorage.getItem('chick-kitchen-review-v1')));
const screenshot=name=>page.locator('#preview').screenshot({path:resolve(output,name+'.png')});
const load=async(scene,cp)=>{
  await page.goto(base+'/review?scene='+scene);
  await page.locator('#status').filter({hasText:cp+' CP'}).waitFor({state:'visible'});
  await frame.getByRole('button',{name:'厨房',exact:true}).click();await frame.getByRole('button',{name:'补给 · 小卖部',exact:true}).click();
  await frame.getByRole('dialog',{name:'鸡宝小卖部',exact:true}).waitFor({state:'visible'});
};
const checkLayout=async(name,width)=>{
  const result=await frame.locator('.shop-screen').evaluate(panel=>{
    const r=panel.getBoundingClientRect(),scroll=panel.querySelector('.shop-shelf-scroll,.shop-detail-scroll'),s=scroll.getBoundingClientRect();
    const fixed=[...panel.children].filter(el=>el!==scroll),clipped=fixed.filter(el=>{const x=el.getBoundingClientRect();return x.left<r.left-.6||x.right>r.right+.6||x.top<r.top-.6||x.bottom>r.bottom+.6;}).map(el=>el.className);
    const firstBuy=panel.querySelector('.shop-ingredient .shop-buy')?.getBoundingClientRect();
    return {panelWidth:r.width,panelHeight:r.height,scrollHeight:s.height,scrollHorizontalOverflow:scroll.scrollWidth>scroll.clientWidth+1,firstIngredientBuyVisible:firstBuy?firstBuy.top>=s.top&&firstBuy.bottom<=s.bottom+.6:null,clipped};
  });
  assert.deepEqual(result.clipped,[]);assert.equal(result.scrollHorizontalOverflow,false);assert.ok(result.scrollHeight>90);
  if(result.firstIngredientBuyVisible!==null)assert.equal(result.firstIngredientBuyVisible,true);
  layouts.push({name,width,...result});await screenshot(name+'-'+width);
};
try{
  await load('new',600);
  await frame.getByRole('tab',{name:'调味料',exact:true}).click();
  assert.equal(await frame.locator('.shop-ingredient').count(),75);
  assert.equal(await frame.locator('[data-shop-buy-ingredient]').count(),1);
  for(const width of [320,375,430]){
    await page.locator(`[data-width="${width}"]`).click();
    await page.waitForFunction(expected=>Math.abs(document.querySelector('#preview').contentDocument.querySelector('#game').getBoundingClientRect().width-expected)<.6,width);
    await checkLayout('catalogue',width);
    await frame.getByRole('searchbox',{name:'搜索调味料名称'}).fill('面粉');
    assert.equal(await frame.locator('.shop-ingredient').count(),1);
    await frame.locator('[data-shop-ingredient-details="9"]').first().click();
    assert.match(await frame.locator('.shop-condition-list').textContent(),/水煮锅 Lv\.2/);
    await checkLayout('ingredient-conditions',width);
    await frame.locator('[data-shop-detail-back]').click();
    assert.equal(await frame.getByRole('searchbox',{name:'搜索调味料名称'}).inputValue(),'面粉');
    await frame.getByRole('button',{name:'清除调味料搜索',exact:true}).click();
  }
  await frame.locator('[data-shop-filter="available"]').click();assert.equal(await frame.locator('.shop-ingredient').count(),1);
  await frame.getByRole('searchbox',{name:'搜索调味料名称'}).fill('面粉');assert.equal(await frame.locator('.shop-ingredient').count(),0);
  await frame.locator('.shop-empty').waitFor({state:'visible'});
  await frame.locator('[data-shop-filter="locked"]').click();assert.equal(await frame.locator('.shop-ingredient').count(),1);
  await frame.getByRole('button',{name:'清除调味料搜索',exact:true}).click();assert.equal(await frame.locator('.shop-ingredient').count(),74);
  await frame.locator('[data-shop-filter="all"]').click();
  const search=frame.getByRole('searchbox',{name:'搜索调味料名称'});
  await search.focus();await search.dispatchEvent('compositionstart');
  await search.evaluate(el=>{el.value='红';el.dispatchEvent(new InputEvent('input',{bubbles:true,isComposing:true}));});
  assert.equal(await frame.locator('.shop-ingredient').count(),75,'Do not replace shelves until IME commits');
  await search.dispatchEvent('compositionend');assert.ok(await frame.locator('.shop-ingredient').count()<75);
  assert.equal(await search.evaluate(el=>document.activeElement===el),true);
  await search.fill('食盐');const beforeIngredient=await state();
  await frame.locator('[data-shop-buy-ingredient="0"]').click();await frame.locator('[data-no]').click();assert.equal((await state()).cp,beforeIngredient.cp);
  await frame.locator('[data-shop-buy-ingredient="0"]').click();await frame.locator('[data-yes]').click();
  assert.equal((await state()).cp,beforeIngredient.cp-5);assert.equal((await state()).ingredients[0],beforeIngredient.ingredients[0]+1);
  assert.equal(await search.inputValue(),'食盐');
  await frame.getByRole('tab',{name:'厨具',exact:true}).click();
  for(let id=0;id<8;id++){
    await frame.locator(`[data-shop-tool-details="${id}"]`).click();
    assert.equal(await frame.locator('.shop-growth-stage').count(),3);
    if(id===1)for(const width of [320,375,430]){
      await page.locator(`[data-width="${width}"]`).click();
      await page.waitForFunction(expected=>Math.abs(document.querySelector('#preview').contentDocument.querySelector('#game').getBoundingClientRect().width-expected)<.6,width);
      await checkLayout('cookware-growth',width);
    }
    await frame.locator('[data-shop-detail-back]').click();
  }
  await frame.getByRole('tab',{name:'其他',exact:true}).click();await frame.locator('[data-shop-duck]').click();
  assert.match(await frame.locator('.confirm p').textContent(),/CP不足/);await frame.locator('[data-yes]').click();
  await load('stage1',20000);await frame.getByRole('tab',{name:'其他',exact:true}).click();
  const beforeDuck=await state();assert.equal(beforeDuck.duck,false);assert.ok(beforeDuck.batch);
  await frame.locator('[data-shop-duck]').click();await frame.locator('[data-no]').click();
  assert.equal((await state()).cp,beforeDuck.cp);assert.equal((await state()).duck,false);
  await frame.locator('[data-shop-duck]').click();await frame.locator('[data-yes]').click();
  const bought=await state();assert.equal(bought.cp,beforeDuck.cp-2500);assert.equal(bought.duck,true);assert.equal(bought.egg,beforeDuck.egg);assert.deepEqual(bought.selected,beforeDuck.selected);assert.deepEqual(bought.batch,beforeDuck.batch);
  await screenshot('duck-unlocked');
  assert.equal(await frame.locator('[data-shop-duck]').count(),0);
  await frame.getByRole('tab',{name:'调味料',exact:true}).click();await search.fill('御神签');
  assert.equal(await frame.locator('[data-shop-buy-ingredient="68"]').count(),0);
  assert.equal(await frame.locator('[data-shop-activity="68"]').count(),1);
  await screenshot('special-gift-route');
  await frame.locator('[data-shop-activity="68"]').click();
  await frame.locator('.shrine-screen').waitFor({state:'visible'});
  assert.deepEqual(errors,[]);assert.deepEqual(failedRequests,[]);
  const report={checkedAt:new Date().toISOString(),layouts,errors,failedRequests,checks:['75 catalogue entries visible from new game','filters combine with search, clear,empty result','Chinese IME preserves input until composition commit','locked ingredient actual alternatives','24 original cookware appearance files loaded','320/375/430 catalogue, details and growth panels','ingredient cancel and real purchase from searched results','duck insufficient funds, cancel,2500CP purchase preserves selected egg/materials/live batch','special gifts route exists with no purchase control']};
  await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));
}catch(error){console.error(JSON.stringify({errors,failedRequests},null,2));await screenshot('failure');throw error;}finally{await browser.close();}
