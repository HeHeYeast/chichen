// Independent browser profiles and review saves only; never the player's save.
import {createRequire} from 'node:module';
import {resolve} from 'node:path';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {SEASONAL_CHARACTERS} from '../web/seasonal-pack.js';
const {chromium}=createRequire(import.meta.url)(resolve(process.argv[2]));
const base=process.argv[3]??'http://127.0.0.1:4173',out=resolve('artifacts/qa/seasonal-v12');await mkdir(out,{recursive:true});
const browser=await chromium.launch({headless:true}),checks=[],errors=[],failedRequests=[],screens=[];
const watch=page=>{page.on('pageerror',e=>errors.push(e.message));page.on('response',r=>{if(r.status()>=400)failedRequests.push(r.url());});};
const read=p=>p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-review-v1')));
const durable=s=>({cp:s.cp,events:s.events,ingredients:s.ingredients,selected:s.selected,egg:s.egg,duck:s.duck,batch:s.batch,farm:s.farm,total:s.total});
async function fixture(p,id){await p.evaluate(id=>new Promise(done=>{const cb=e=>{if(e.source===window&&e.data?.type==='chick-status'&&e.data.loaded){removeEventListener('message',cb);done();}};addEventListener('message',cb);postMessage({type:'chick-review',command:'scene',value:id},location.origin);}),id);await p.evaluate(()=>new Promise(done=>requestAnimationFrame(()=>requestAnimationFrame(done))));}
async function shot(p,name){const file=resolve(out,name+'.png');await p.screenshot({path:file,animations:'disabled'});screens.push(file);}
async function fits(p){const r=await p.locator('.screen-panel').evaluate(el=>{const panel=el.getBoundingClientRect(),footer=el.querySelector('footer').getBoundingClientRect(),scroll=el.querySelector('.scroll');return {width:el.clientWidth,scrollWidth:el.scrollWidth,innerWidth:scroll?.clientWidth,innerScroll:scroll?.scrollWidth,footerBottom:footer.bottom,panelBottom:panel.bottom};});assert.ok(r.scrollWidth<=r.width+1,JSON.stringify(r));assert.ok(r.innerScroll<=r.innerWidth+1,JSON.stringify(r));assert.ok(r.footerBottom<=r.panelBottom+1,JSON.stringify(r));}
try{
  const context=await browser.newContext({viewport:{width:375,height:747},deviceScaleFactor:2}),p=await context.newPage();watch(p);
  await p.goto(base+'/web/index.html?review=1');await p.getByRole('button',{name:'开始游戏',exact:true}).waitFor();
  await fixture(p,'v12-preview');await p.locator('.journal-screen').waitFor();assert.match(await p.locator('.journal-window').first().textContent(),/30 分钟后开放/);assert.match(await p.locator('.journal-scroll').textContent(),/永久委托/);
  const before=durable(await read(p));await p.locator('[data-discovery-notices]').click();assert.equal((await read(p)).events.discoveryNotices,false);
  await p.reload();await p.getByRole('button',{name:'开始游戏',exact:true}).click();await p.getByRole('button',{name:'设置',exact:true}).click();await p.locator('[data-journal]').click();assert.equal(await p.locator('[data-discovery-notices]').getAttribute('aria-checked'),'false');
  checks.push('日历显示提前30分钟、真实开火窗口和节日常驻说明；提醒开关重开保留');
  await fixture(p,'v12-notice');await p.locator('.game-toast').waitFor();assert.match(await p.locator('.game-toast').textContent(),/火凤凰.*30 分钟/);await p.getByRole('button',{name:'厨房',exact:true}).click();await p.getByRole('button',{name:'补给 · 小卖部',exact:true}).click();assert.equal(await p.locator('.game-toast').isVisible(),false);await p.locator('[data-shop-journal]').click();await p.locator('.journal-screen').waitFor();
  checks.push('厨房确实显示提前预告，离开页面即收起；商店可进入食谱');
  await fixture(p,'v12-busy');const busy=durable(await read(p));await p.locator('[data-seasonal-prepare]').click();const prep=await read(p);assert.equal(prep.egg,0);assert.deepEqual(prep.selected,[16,50]);assert.deepEqual(prep.ingredients,busy.ingredients);assert.deepEqual(prep.batch,busy.batch);assert.equal(prep.cp,busy.cp);
  await p.getByRole('button',{name:'使用水煮锅',exact:true}).click();assert.match(await p.locator('.confirm p').textContent(),/樱桃大福鸡/);await p.locator('[data-no]').click();assert.deepEqual(durable(await read(p)),durable(prep));
  checks.push('新配方准备保留正在孵化的鸭蛋、库存和CP；开火说明目标，取消不扣费');
  await fixture(p,'v12-detail');await p.locator('[data-seasonal-prepare]').click();await p.getByRole('button',{name:'使用水煮锅',exact:true}).click();await p.locator('[data-yes]').click();const cooked=await read(p);assert.equal(cooked.batch.eggs.filter(e=>e.id===120).length,1);assert.equal(cooked.batch.eggs.length,24);assert.equal(cooked.ingredients[16],1);assert.equal(cooked.ingredients[50],1);assert.equal(cooked.cp,25000-E.tool(2).lv_2_cook_cp);
  await p.reload();await p.getByRole('button',{name:'开始游戏',exact:true}).click();assert.equal((await read(p)).batch.seasonalRecipe,'0:120');
  checks.push('实际开火出1只指定新品，24蛋与扣费正确，重开读取新批次');
  await fixture(p,'v12-new');await p.locator('[data-journal-tab="recipes"]').click();await p.locator('[data-journal-chapter="spring"]').click();await p.locator('[data-seasonal-recipe="0:120"]').click();assert.match(await p.locator('.journal-scroll').textContent(),/厨房 Lv.2.*认识 12 种/);assert.equal((await read(p)).events.seasonalRecipe,undefined);
  await p.locator('[data-journal-back]').click();await p.locator('[data-seasonal-recipe="1:57"]').click();await p.locator('[data-seasonal-prepare]').click();await p.locator('[data-shop-duck]').waitFor();
  checks.push('新玩家看到具体解锁要求；鸭宝配方能跳转鸭蛋购买');
  await fixture(p,'v12-recipes');await p.locator('[data-journal-chapter="summer"]').click();await p.locator('[data-seasonal-recipe="0:122"]').click();assert.equal(await p.locator('[data-seasonal-prepare]').textContent(),'去补齐材料');await p.locator('[data-seasonal-prepare]').click();await p.locator('.shop-detail-screen').waitFor();
  checks.push('缺材料的新配方可跳到对应商品详情');
  await fixture(p,'v12-rewards');await p.locator('[data-journal-chapter="autumn"]').click();const rewardBefore=await read(p);await p.locator('[data-seasonal-reward="autumn"]').click();assert.equal((await read(p)).cp,rewardBefore.cp+600);assert.equal(await p.locator('[data-seasonal-reward="autumn"]').count(),0);
  checks.push('旧收藏计入四时章节，一次性600CP回礼保存并更新按钮');
  await fixture(p,'v12-collection');assert.match(await p.locator('.species-name').textContent(),/围巾汤圆鸭/);assert.match(await p.locator('.species-story').textContent(),/围巾/);await p.locator('[data-species-seasonal]').click();assert.match(await p.locator('.journal-name').textContent(),/围巾汤圆鸭/);await p.getByRole('button',{name:'图鉴',exact:true}).click();await p.locator('[data-collection-journal]').click();await p.locator('.journal-screen').waitFor();
  checks.push('新鸭宝档案、配方线索、图鉴新配方入口全部连通');
  for(const [width,height] of [[320,568],[375,747],[430,932]]){await p.setViewportSize({width,height});for(const scene of ['v12-calendar','v12-preview','v12-recipes','v12-detail','v12-collection','v12-new']){await fixture(p,scene);await fits(p);await shot(p,scene+'-'+width);}}
  await p.setViewportSize({width:375,height:747});await fixture(p,'v12-recipes');for(const season of ['spring','summer','autumn','winter']){await p.locator(`[data-journal-chapter="${season}"]`).click();await shot(p,'chapter-'+season);}
  await fixture(p,'v12-farm');await shot(p,'sixteen-in-farm');
  checks.push('320/375/430宽的日历、配方、档案和未解锁状态无横向溢出，全部16只新素材可显示');
  await context.close();
  const s=E.freshState();s.cp=9000;s.kitchenLevel=3;s.toolLevels.fill(2);s.duck=true;s.ingredients={16:2,50:2};for(let id=0;id<12;id++)s.total['0:'+id]=1;for(const c of SEASONAL_CHARACTERS)s.total[c.key]=1;
  const nativeContext=await browser.newContext({viewport:{width:375,height:747}});await nativeContext.addInitScript(raw=>{const m=window.__seasonNative={raw,fail:false};window.ChickNative={platformInfo:()=>JSON.stringify({android:true,version:'1.3.0-test'}),loadSave:()=>JSON.stringify({status:'ok',raw:m.raw}),saveGame:next=>{if(m.fail)return JSON.stringify({ok:false,message:'模拟保存失败'});m.raw=next;return JSON.stringify({ok:true});},notificationStatus:()=>JSON.stringify({supported:true,permissionGranted:true,notificationsEnabled:true,exactAllowed:true,message:'测试状态'}),ready(){},closeApp(){}};},JSON.stringify(s));
  const n=await nativeContext.newPage();watch(n);await n.goto(base+'/');await n.locator('[data-control-id="tool:0" ]').waitFor({state:'visible'});await n.getByRole('button',{name:'图鉴',exact:true}).click();await n.locator('[data-collection-journal]').click();await n.locator('[data-journal-chapter="spring"]').click();await n.locator('[data-seasonal-recipe="0:120"]').click();await n.evaluate(()=>{window.__seasonNative.fail=true;});await n.locator('[data-seasonal-prepare]').click();await n.locator('[data-yes]').click();assert.equal(await n.evaluate(()=>JSON.parse(window.__seasonNative.raw).events.seasonalRecipe),undefined);await n.evaluate(()=>{window.__seasonNative.fail=false;});await n.locator('[data-seasonal-prepare]').click();assert.equal(await n.evaluate(()=>JSON.parse(window.__seasonNative.raw).events.seasonalRecipe),'0:120');
  checks.push('模拟Android保存失败时配方选择回退，恢复后只准备一次；没有真机安装');
  await nativeContext.close();assert.deepEqual(errors,[]);assert.deepEqual(failedRequests,[]);const report={checkedAt:new Date().toISOString(),checks,errors,failedRequests,screens,nativeDeviceTest:false};await writeFile(resolve(out,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));
}catch(e){console.error(JSON.stringify({checks,errors,failedRequests},null,2));throw e;}finally{await browser.close();}
