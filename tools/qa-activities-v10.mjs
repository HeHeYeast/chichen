import {createRequire} from 'node:module';
import {resolve} from 'node:path';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
const {chromium}=createRequire(import.meta.url)(resolve(process.argv[2]));
const base=process.argv[3]??'http://127.0.0.1:4173',out=resolve('artifacts/qa/activities-v10');
await mkdir(out,{recursive:true});
const browser=await chromium.launch({headless:true}),errors=[],checks=[];
const watch=page=>page.on('pageerror',error=>errors.push(error.message));
const durable=s=>({cp:s.cp,events:s.events,ingredients:s.ingredients,farm:s.farm,total:s.total,selected:s.selected,batch:s.batch});
try{
  const context=await browser.newContext({viewport:{width:1200,height:1000}}),page=await context.newPage();watch(page);
  const scope=page.frameLocator('#preview');
  const read=()=>scope.locator('body').evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-review-v1')));
  async function scene(id,title){await page.goto(base+'/review?scene='+id);await scope.getByRole('dialog',{name:title,exact:true}).waitFor();}
  await scene('v10-activities','神社委托簿');
  const before=durable(await read());
  await scope.locator('[data-activity-id="spring"]').click();await scope.locator('[data-activity-claim]').click();
  await scope.locator('[data-no]').click();assert.deepEqual(durable(await read()),before);checks.push('打开与取消委托不改变存档');
  await scope.locator('[data-activity-claim]').click();await scope.locator('[data-yes]').click();
  await scope.locator('.confirm p').filter({hasText:'来信已收下'}).waitFor();await scope.locator('[data-yes]').click();
  const claimed=await read();assert.equal(claimed.events.campaign_char_0_48,true);assert.equal(claimed.cp,before.cp);assert.deepEqual(claimed.farm,before.farm);assert.equal(await scope.locator('[data-activity-claim]').count(),0);checks.push('确认来信保存配方开放记录，未直接增加角色或扣费');
  await scene('v10-gifts','神社御神签');
  const giftBefore=durable(await read());await scope.locator('[data-shrine-draw]').click();
  const gift=await read();assert.equal(gift.ingredients[68],1);assert.equal(gift.events.legacyOmikujiCount,1);assert.equal(gift.cp,giftBefore.cp);assert.equal(await scope.locator('[data-shrine-draw]').count(),0);checks.push('免费求签一次入包且同日不能重复领取');
  await scope.locator('[data-shrine-prepare]').click();await scope.getByRole('button',{name:'使用保温灯',exact:true}).click();await scope.locator('[data-yes]').click();
  const cooked=await read();assert.ok(cooked.batch.ingredients.includes(68));assert.equal(cooked.ingredients[68]??0,0);assert.equal(cooked.batch.eggs.filter(egg=>egg.id===89).length,1);checks.push('领取御神签后可回厨房选择并调理，原配方生成一只对应签鸡');
  await scene('v10-gifts-full','神社御神签');assert.equal(await scope.locator('[data-shrine-draw]').isDisabled(),true);assert.ok((await scope.locator('.shrine-status').textContent()).includes('30'));checks.push('调味料包满时禁止领取且明确原因');
  await scene('v10-travel','时空旅行');const travelBefore=durable(await read());await scope.locator('[data-activity-claim]').click();await scope.locator('[data-no]').click();assert.deepEqual(durable(await read()),travelBefore);
  await scope.locator('[data-activity-claim]').click();await scope.locator('[data-yes]').click();await scope.locator('.confirm p').filter({hasText:'旅途结束'}).waitFor();await scope.locator('[data-yes]').click();
  const travel=await read();assert.equal(travel.farm['0:0'],travelBefore.farm['0:0']-1);assert.equal(travel.farm['0:104'],1);assert.equal(travel.total['0:104'],1);assert.equal(travel.cp,travelBefore.cp);checks.push('时空旅行确认才兑换一只，农场与图鉴同步记录');
  await scene('v10-collection','未发现的伙伴');assert.equal((await scope.locator('.species-unknown-screen').textContent()).includes(E.label(E.char(0,89))),false);
  await scope.locator('[data-species-activity]').click();await scope.getByRole('dialog',{name:'神社御神签',exact:true}).waitFor();await scope.locator('[data-shrine-letters]').click();await scope.getByRole('dialog',{name:'神社来信',exact:true}).waitFor();checks.push('未知图鉴不泄露角色名称与价格；线索可跳到赠品及前置委托');
  await page.goto(base+'/review?scene=v10-farm-shrine');await scope.getByRole('button',{name:'打开神社委托簿',exact:true}).waitFor();await scope.getByRole('button',{name:'打开神社委托簿',exact:true}).click();await scope.getByRole('dialog',{name:'神社委托簿',exact:true}).waitFor();checks.push('农场现有神社可点击打开委托簿');
  for(const width of [320,375,430]){
    for(const [id,title] of [['v10-activities','神社委托簿'],['v10-gifts','神社御神签'],['v10-collection','未发现的伙伴']]){
      await scene(id,title);await page.locator(`button[data-width="${width}"]`).click();
      const dimensions=await scope.locator('.screen-panel').evaluate(el=>({width:el.clientWidth,scrollWidth:el.scrollWidth,footerBottom:el.querySelector('footer').getBoundingClientRect().bottom,panelBottom:el.getBoundingClientRect().bottom}));
      assert.ok(dimensions.scrollWidth<=dimensions.width+1,JSON.stringify(dimensions));assert.ok(dimensions.footerBottom<=dimensions.panelBottom+1,JSON.stringify(dimensions));
      await page.locator('#preview').screenshot({path:resolve(out,id+'-'+width+'.png'),animations:'disabled'});
    }
  }
  checks.push('320/375/430 宽下无横向溢出，页脚保持在面板内');
  await context.close();
  const fixture=E.freshState();fixture.cp=9000;fixture.kitchenLevel=3;fixture.toolLevels=Array(9).fill(2);for(let id=0;id<18;id++){fixture.total['0:'+id]=120;fixture.farm['0:'+id]=3;}
  const nativeContext=await browser.newContext({viewport:{width:430,height:932}});
  await nativeContext.addInitScript(raw=>{
    const mock=window.__activityNative={raw,fail:false};
    window.ChickNative={platformInfo:()=>JSON.stringify({android:true,version:'1.1.0-test'}),loadSave:()=>JSON.stringify({status:'ok',raw:mock.raw}),saveGame:next=>{if(mock.fail)return JSON.stringify({ok:false,message:'模拟保存失败'});mock.raw=next;return JSON.stringify({ok:true});},notificationStatus:()=>JSON.stringify({supported:true,permissionGranted:true,notificationsEnabled:true,exactAllowed:true,message:'模拟通知状态'}),ready(){},closeApp(){}};
  },JSON.stringify(fixture));
  const native=await nativeContext.newPage();watch(native);await native.goto(base+'/');await native.locator('[data-control-id="tool:0" ]').waitFor({state:'visible'});
  await native.getByRole('button',{name:'厨房',exact:true}).click();await native.getByRole('button',{name:'补给 · 小卖部',exact:true}).click();await native.getByRole('tab',{name:'其他',exact:true}).click();await native.locator('[data-shop-activity=""]').click();await native.locator('[data-activity-id="spring"]').click();
  await native.evaluate(()=>{window.__activityNative.fail=true;});await native.locator('[data-activity-claim]').click();await native.locator('[data-yes]').click();await native.locator('.confirm p').filter({hasText:'进度暂未保存'}).waitFor();await native.locator('[data-yes]').click();
  assert.equal(await native.locator('[data-activity-claim]').isEnabled(),true);assert.equal(await native.evaluate(()=>JSON.parse(window.__activityNative.raw).events.campaign_char_0_48),undefined);
  await native.evaluate(()=>{window.__activityNative.fail=false;});await native.locator('[data-activity-claim]').click();await native.locator('[data-yes]').click();await native.locator('.confirm p').filter({hasText:'来信已收下'}).waitFor();await native.locator('[data-yes]').click();assert.equal(await native.evaluate(()=>JSON.parse(window.__activityNative.raw).events.campaign_char_0_48),true);checks.push('模拟 Android 保存失败时回退内存领取，恢复保存后可再确认一次成功');
  await nativeContext.close();
  assert.deepEqual(errors,[]);const report={checkedAt:new Date().toISOString(),checks,errors,nativeDeviceTest:false};await writeFile(resolve(out,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));
}finally{await browser.close();}
