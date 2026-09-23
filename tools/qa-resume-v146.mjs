// All saves belong to isolated Playwright profiles. Android events are mocked;
// this verifies game routing, not device lifecycle or OS notification delivery.
import {createRequire} from 'node:module';
import {resolve} from 'node:path';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {makeBackup} from '../web/save-store.js';
const {chromium}=createRequire(import.meta.url)(resolve(process.argv[2]));
const base=process.argv[3]??'http://127.0.0.1:4173',out=resolve(process.argv[5]??'artifacts/qa/resume-v146');await mkdir(out,{recursive:true});
const browser=await chromium.launch({headless:true,executablePath:process.argv[4]}),checks=[],errors=[],failedRequests=[];
const watch=p=>{p.on('pageerror',e=>errors.push(e.message));p.on('response',r=>{if(r.status()>=400)failedRequests.push(r.url());});};
const durable=s=>({cp:s.cp,kitchenLevel:s.kitchenLevel,toolLevels:s.toolLevels,ingredients:s.ingredients,selected:s.selected,farm:s.farm,total:s.total,batch:s.batch,events:s.events});
const kitchen=async p=>{await p.locator('[data-control-id="tool:0"]').waitFor({state:'visible'});assert.equal(await p.locator('#panels > *,#dialog-layer > *').count(),0);assert.equal(await p.locator('[data-control-id="start"]').count(),0);};
const title=async p=>{await p.getByRole('button',{name:'开始游戏',exact:true}).waitFor();assert.equal(await p.locator('[data-control-id=\"tool:0\"],#panels > *,#dialog-layer > *').count(),0);};
const start=async p=>{await p.getByRole('button',{name:'开始游戏',exact:true}).click();await kitchen(p);await p.locator('[data-control-id="nav:0"][tabindex="0"]').waitFor();};
const event=(p,type,result=null)=>p.evaluate(({type,result})=>window.dispatchEvent(new CustomEvent('chick:native',{detail:{type,result}})),{type,result});
try{
  for(const [width,height]of [[320,568],[375,747],[320,800]]){
    const c=await browser.newContext({viewport:{width,height},reducedMotion:'reduce'}),p=await c.newPage();watch(p);
    await p.goto(base+'/web/index.html?review=1');const start=p.getByRole('button',{name:'开始游戏',exact:true});await start.waitFor();
    const r=await start.boundingBox();assert.ok(r.y+r.height<height-8&&r.height>=48,'cover button fits clear of the bottom edge');
    assert.equal(await p.evaluate(()=>document.fonts.check('900 19px "Chicken UI"','开始游戏')),true);
    await p.screenshot({path:resolve(out,`cover-${width}x${height}.png`)});
    const first=await p.locator('canvas').evaluate(el=>el.toDataURL());await p.waitForTimeout(350);
    assert.equal(await p.locator('canvas').evaluate(el=>el.toDataURL()),first,'reduced motion makes the cover stationary');
    const before=durable(await p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-review-v1'))));
    await start.click();await kitchen(p);assert.deepEqual(durable(await p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-review-v1')))),before);
    await c.close();
  }
  checks.push('三种竖屏封面按钮不裁切，开始只进入厨房，不花CP；减少动态效果关闭封面动画');

  const seed=E.freshState();seed.cp=25000;seed.total={'0:0':12};seed.farm={'0:0':12};E.startBatch(seed,0,Date.now(),()=>.5);
  const c=await browser.newContext({viewport:{width:375,height:747}});
  await c.addInitScript(raw=>{
    const m=window.__launchMock={raw:localStorage.getItem('qa-launch-save')??raw,pending:null,permission:false,writes:0,imports:0,closes:0,error:localStorage.getItem('qa-launch-error')};
    const status=()=>({supported:true,permissionGranted:m.permission,notificationsEnabled:m.permission,exactAllowed:false,message:'模拟通知状态'});
    const response=(id,result)=>window.dispatchEvent(new CustomEvent('chick:native',{detail:{type:'response',id,result}}));
    m.respond=result=>{const pending=m.pending;m.pending=null;response(pending.id,result);};
    const save=raw=>{m.raw=raw;m.writes++;localStorage.setItem('qa-launch-save',raw);return JSON.stringify({ok:true});};
    window.ChickNative={platformInfo:()=>JSON.stringify({android:true,version:'1.4.6-test'}),loadSave:()=>JSON.stringify(m.error?{status:'error',message:m.error}:{status:'ok',raw:m.raw}),saveGame:save,importGame:raw=>{m.imports++;return save(raw);},notificationStatus:()=>JSON.stringify(status()),requestNotifications:id=>{m.pending={id,kind:'permission'};},importSave:id=>{m.pending={id,kind:'import'};},exportSave:(id,raw)=>{m.pending={id,raw,kind:'export'};},openNotificationSettings(){},openExactAlarmSettings(){},ready(){},closeApp(){m.closes++;}};
  },JSON.stringify(seed));
  const p=await c.newPage();watch(p);const state=()=>p.evaluate(()=>JSON.parse(window.__launchMock.raw));
  await p.goto(base+'/');await title(p);await p.waitForTimeout(500);await title(p);await start(p);assert.deepEqual(durable(await state()),durable(seed));
  checks.push('模拟APP冷启动等待开始按钮，等待后不自动跳转；点击进入厨房，CP、24枚孵化批次与图鉴保持');
  for(const name of ['厨房','生意','寻访','图鉴','农场','设置']){
    await p.getByRole('button',{name,exact:true}).click();const before=durable(await state());
    const screen=await p.locator('#panels > section').evaluateAll(els=>els[0]?.getAttribute('aria-label')??null);
    await event(p,'pause');await event(p,'resume');
    assert.equal(await p.locator('[data-control-id="start"]').count(),0);
    assert.equal(await p.locator('#panels > section').evaluateAll(els=>els[0]?.getAttribute('aria-label')??null),screen);
    assert.deepEqual(durable(await state()),before);
  }
  checks.push('厨房、生意、寻访、图鉴、农场、设置退到后台再打开直接保留原页，CP、批次和图鉴不变');
  await p.getByRole('button',{name:'图鉴',exact:true}).click();const beforeReload=durable(await state());await p.reload();await title(p);await start(p);assert.deepEqual(durable(await state()),beforeReload);
  checks.push('模拟进程重建后停留开始界面，点击后回厨房');
  await p.getByRole('button',{name:'厨房',exact:true}).click();await p.getByRole('button',{name:'补给 · 小卖部',exact:true}).click();await event(p,'kitchen');await event(p,'resume',{keepPage:true});await kitchen(p);
  await event(p,'back');await p.locator('[data-yes]').waitFor();await event(p,'pause');await event(p,'resume');
  assert.match(await p.locator('.confirm > p').innerText(),/离开小厨房/);assert.equal(await p.evaluate(()=>window.__launchMock.closes),0);await p.locator('[data-no]').click();await kitchen(p);
  const tool=await p.locator('[data-control-id="tool:0"]').boundingBox();await p.mouse.move(tool.x+tool.width/2,tool.y+tool.height/2);await p.mouse.down();await event(p,'pause');await event(p,'resume');await p.mouse.up();await kitchen(p);
  checks.push('通知入口仍直达厨房；恢复保留退出确认但不会自动退出，中断触摸不误触厨具');

  await p.getByRole('button',{name:'设置',exact:true}).click();await p.locator('[data-title]').click();await event(p,'pause');await event(p,'resume');await p.getByRole('button',{name:'开始游戏',exact:true}).waitFor();
  const movingFrame=await p.locator('canvas').evaluate(el=>el.toDataURL());await p.waitForTimeout(400);
  assert.notEqual(await p.locator('canvas').evaluate(el=>el.toDataURL()),movingFrame,'normal motion animates the title and steam');
  const beforeTitle=durable(await state());await p.getByRole('button',{name:'开始游戏',exact:true}).focus();await p.keyboard.press('Enter');await kitchen(p);await p.locator('[data-control-id="nav:0"][tabindex="0"]').waitFor();assert.deepEqual(durable(await state()),beforeTitle);
  checks.push('设置可回到新版动态封面，键盘或触摸开始再次进入厨房并保留当前批次');

  await p.getByRole('button',{name:'设置',exact:true}).click();await p.getByRole('switch',{name:'孵化完成提醒',exact:true}).click();
  await event(p,'pause');await event(p,'resume',{keepPage:true});await p.locator('.settings-screen').waitFor();
  await p.evaluate(()=>{const m=window.__launchMock;m.permission=true;m.respond({permissionGranted:true,notificationsEnabled:true});});
  await p.getByRole('switch',{name:'孵化完成提醒',exact:true}).filter({hasText:'开'}).waitFor();assert.equal((await state()).alarm,true);
  await p.locator('[data-permission]').click();await event(p,'pause');await event(p,'resume',{keepPage:true});await p.locator('.settings-screen').waitFor();
  await p.locator('[data-exact]').click();await event(p,'pause');await event(p,'resume',{keepPage:true});await p.locator('.settings-screen').waitFor();
  checks.push('通知授权和两种系统设置返回设置页，授权结果被正常接收');

  const beforeImport=durable(await state());await p.locator('[data-import]').click();await event(p,'pause');
  await p.evaluate(raw=>window.__launchMock.respond({ok:true,raw}),makeBackup(seed));
  await p.locator('[data-no]').waitFor();await event(p,'resume',{keepPage:true});await p.locator('[data-no]').click();assert.deepEqual(durable(await state()),beforeImport);assert.equal(await p.evaluate(()=>window.__launchMock.imports),0);
  await p.locator('[data-import]').click();await event(p,'pause');await p.evaluate(()=>window.__launchMock.respond({ok:false,message:'已取消'}));await event(p,'resume',{keepPage:true});await p.locator('.settings-screen').waitFor();
  await p.locator('[data-export]').click();await event(p,'pause');await p.evaluate(()=>window.__launchMock.respond({ok:true,message:'备份已导出。'}));await p.locator('[data-yes]').waitFor();await event(p,'resume',{keepPage:true});await p.locator('[data-yes]').click();await p.locator('.settings-screen').waitFor();assert.deepEqual(durable(await state()),beforeImport);
  checks.push('文件选择返回保留导入确认；取消、导出完成不会被导航吞掉，也不改进度');
  await p.getByRole('button',{name:'厨房',exact:true}).click();
  await p.locator('[data-control-id="clean"]').click();const cleaningCP=(await state()).cp;
  await event(p,'pause');await event(p,'resume');await p.getByRole('dialog',{name:'打扫厨房',exact:true}).waitFor();
  assert.equal((await state()).cp,cleaningCP);await p.locator('[data-no]').click();
  await p.locator('[data-control-id="tool:0"]').click();const cookText=await p.locator('.confirm > p').innerText();
  await event(p,'pause');await event(p,'resume');assert.equal(await p.locator('.confirm > p').innerText(),cookText);assert.equal((await state()).cp,cleaningCP);await p.locator('[data-no]').click();
  checks.push('打扫和开火确认在后台恢复后保留，未确认不扣款或重开批次');
  await p.getByRole('button',{name:'厨房',exact:true}).click();await p.getByRole('button',{name:'补给 · 小卖部',exact:true}).click();await p.locator('[data-shop-tool-details="1"]').click();
  await event(p,'pause');await event(p,'resume');await p.getByRole('dialog',{name:'平底锅成长册',exact:true}).waitFor();
  await p.getByRole('button',{name:'厨房',exact:true}).click();await event(p,'pause');await event(p,'resume');await kitchen(p);
  await p.screenshot({path:resolve(out,'warm-resume-kitchen.png')});
  checks.push('成长册等二级页面保留；厨房从后台返回无需开始按钮');
  await event(p,'back');await p.locator('[data-yes]').click();assert.equal(await p.evaluate(()=>window.__launchMock.closes),1);
  await p.reload();await title(p);await start(p);checks.push('明确确认退出会关闭应用，模拟新实例重新打开仍显示封面');
  await p.evaluate(()=>localStorage.setItem('qa-launch-error','模拟损坏存档，请导入备份'));
  await p.reload();await p.getByRole('dialog',{name:'存档需要恢复',exact:true}).waitFor();await event(p,'resume');await event(p,'kitchen');await p.getByRole('dialog',{name:'存档需要恢复',exact:true}).waitFor();assert.equal(await p.evaluate(()=>window.__launchMock.writes),0);
  checks.push('无法读取存档时仍停留恢复入口，启动和通知均不覆盖原存档');
  await c.close();assert.deepEqual(errors,[]);assert.deepEqual(failedRequests,[]);
  const report={checkedAt:new Date().toISOString(),checks,errors,failedRequests,nativeDeviceTest:false};await writeFile(resolve(out,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));
}catch(error){console.error(JSON.stringify({checks,errors,failedRequests},null,2));throw error;}finally{await browser.close();}
