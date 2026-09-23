import {createRequire} from 'node:module';
import {resolve} from 'node:path';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {RECIPE_CATALOG} from '../web/recipe-book.js';
import {DISCOVERY_CLUES} from '../web/discovery-clues.js';
import {GAME_DATA as DATA} from '../web/content-pack.js';

const {chromium}=createRequire(import.meta.url)(resolve(process.argv[2]));
const base=process.argv[3]??'http://127.0.0.1:4173';
const out=resolve('artifacts/qa/discovery-clues-v142');await mkdir(out,{recursive:true});
const browser=await chromium.launch({headless:true,...(process.argv[4]?{executablePath:process.argv[4]}:{})}),checks=[],errors=[],failedRequests=[];
const watch=p=>{p.on('pageerror',e=>errors.push(e.message));p.on('response',r=>{if(r.status()>=400)failedRequests.push(r.url());});};
const read=p=>p.evaluate(()=>{const s=JSON.parse(localStorage.getItem('chick-kitchen-review-v1'));return {cp:s.cp,batch:s.batch,ingredients:s.ingredients,selected:s.selected,total:s.total,farm:s.farm,events:s.events,egg:s.egg};});
async function fixture(p,name){await p.evaluate(name=>postMessage({type:'chick-review',command:'scene',value:name},location.origin),name);}
async function fits(p){
  const bounds=await p.locator('.screen-panel').evaluate(panel=>{const r=panel.getBoundingClientRect(),f=panel.querySelector('footer').getBoundingClientRect(),c=panel.querySelector('.species-discovery-clue').getBoundingClientRect();return {overflow:panel.scrollWidth-panel.clientWidth,footerBottom:f.bottom,panelBottom:r.bottom,viewport:innerHeight,clueLeft:c.left,panelLeft:r.left,clueRight:c.right,panelRight:r.right};});
  assert.ok(bounds.overflow<=1,JSON.stringify(bounds));assert.ok(bounds.footerBottom<=bounds.panelBottom+1&&bounds.footerBottom<=bounds.viewport+1,JSON.stringify(bounds));
  assert.ok(bounds.clueLeft>=bounds.panelLeft&&bounds.clueRight<=bounds.panelRight,JSON.stringify(bounds));return bounds;
}
try{
  const context=await browser.newContext({viewport:{width:375,height:747},reducedMotion:'reduce'}),p=await context.newPage();watch(p);
  await p.goto(base+'/web/index.html?review=1');await p.getByRole('button',{name:'开始游戏',exact:true}).waitFor();
  await fixture(p,'v14-new');await p.locator('[data-cookbook-back]').click();
  await p.locator('.collection-book').waitFor();const before=await read(p);
  let inspected=0;
  for(const egg of [0,1]){
    await p.locator(`[data-collection-egg="${egg}"]`).click();
    for(let page=0;page<Math.ceil(DATA.characters[egg].length/9);page++){
      const ids=await p.locator('[data-collection-card]').evaluateAll(cards=>cards.map(c=>Number(c.dataset.collectionCard)));
      for(const id of ids){
        const key=`${egg}:${id}`,row=RECIPE_CATALOG.find(r=>r.key===key);
        await p.locator(`[data-collection-card="${id}"]`).click();
        assert.equal(await p.locator('.species-discovery-clue p').innerText(),DISCOVERY_CLUES[key]);
        assert.equal(await p.locator('.species-portrait img,.species-portrait svg,[data-species-recipe],[data-species-seasonal],details').count(),0);
        const text=await p.locator('.species-sheet').innerText();
        assert.ok(!text.includes(DATA.characters[egg].find(c=>c.id===id).title_zh_CN),key);
        for(const ingredient of row.ingredients)assert.ok(!text.includes(DATA.tools[2][ingredient].title_zh_CN),`${key} ingredient`);
        if(egg===0&&id===120)await p.screenshot({path:resolve(out,'handmade-clue.png')});
        if(egg===0&&id===60){assert.match(await p.locator('.species-clue-calendar').innerText(),/母亲节/);await p.screenshot({path:resolve(out,'holiday-clue.png')});}
        if(egg===1&&id===64)await p.screenshot({path:resolve(out,'duck-clue.png')});
        await p.locator('[data-unknown-back]').click();
        assert.match(await p.locator('.collection-pager').innerText(),new RegExp(`第\\s*${page+1}\\s*/`));
        inspected++;
      }
      if(page+1<Math.ceil(DATA.characters[egg].length/9))await p.locator('[data-collection-next]').click();
    }
    console.log(`Inspected ${inspected} unknown partner screens`);
  }
  assert.equal(inspected,193);assert.deepEqual(await read(p),before);checks.push('193 位未知伙伴逐项点开：线索正确，名称、画像、配料与完整配方隐藏；返回保留页码，存档不变');
  await fixture(p,'v14-unknown');await p.locator('.species-discovery-clue').waitFor();await p.locator('[data-unknown-kitchen]').click();await p.locator('[data-control-id="tool:0"]').waitFor();assert.deepEqual(await read(p),before);checks.push('从线索回厨房不调整材料、蛋种、CP 或当前批次');
  await fixture(p,'v14-species');await p.locator('[data-species-recipe="1:30"]').waitFor();assert.equal(await p.locator('.species-discovery-clue').count(),0);
  await p.locator('[data-species-recipe="1:30"]').click();assert.equal(await p.locator('.cookbook-mixture>span').count(),3);await p.locator('[data-cookbook-back]').click();await p.locator('[data-species-recipe="1:30"]').waitFor();checks.push('已收录伙伴继续显示档案和完整配方，往返正常');
  const layouts=[];
  for(const [width,height] of [[320,568],[375,747],[430,932],[1256,2503],[932,430]]){
    await p.setViewportSize({width,height});await fixture(p,'v14-unknown');await p.locator('.species-discovery-clue').waitFor();
    await p.locator('.species-sheet').evaluate(el=>el.scrollTop=0);
    layouts.push({width,height,...await fits(p)});
    await p.screenshot({path:resolve(out,`clue-${width}x${height}.png`)});
    await p.locator('.species-sheet').evaluate(el=>el.scrollTop=el.scrollHeight);await fits(p);
  }
  checks.push('320/375/430 宽、Magic8 长屏及横屏：线索可读，无横向溢出，滚动后页脚可操作');
  assert.deepEqual(errors,[]);assert.deepEqual(failedRequests,[]);
  await writeFile(resolve(out,'browser-report.json'),JSON.stringify({passed:true,inspected,checks,layouts,errors,failedRequests},null,2)+'\n');console.log(JSON.stringify({passed:true,inspected,checks},null,2));
}finally{await browser.close();}
