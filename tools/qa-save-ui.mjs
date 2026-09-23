// Isolated browser verification of actual export/import controls. The optional
// native bridge is explicitly a mock; this does not test Android OS delivery.
import {CURRENT_SAVE_VERSION} from '../web/save-migrations.js';
import {createRequire} from 'node:module';
import {resolve} from 'node:path';
import {mkdir,readFile,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import {parseBackup} from '../web/save-store.js';

if(!process.argv[2])throw Error('Pass an existing Playwright package directory.');
const {chromium}=createRequire(import.meta.url)(resolve(process.argv[2]));
const base=process.argv[3]??'http://127.0.0.1:4173';
const output=resolve(process.argv[4]??'artifacts/qa/save-ui-v9');await mkdir(output,{recursive:true});
const browser=await chromium.launch({headless:true}),errors=[];
const durable=state=>({cp:state.cp,kitchenLevel:state.kitchenLevel,toolLevels:state.toolLevels,ingredients:state.ingredients,selected:state.selected,farm:state.farm,total:state.total,
  batch:state.batch?{tool:state.batch.tool,level:state.batch.level,egg:state.batch.egg,ingredients:state.batch.ingredients,started:state.batch.started,ends:state.batch.ends,
    eggs:state.batch.eggs.map(({id,egg,collected,openAt,blackAt,status})=>({id,egg,collected,openAt,blackAt,status}))}:null});
const watch=page=>page.on('pageerror',error=>errors.push(error.message));
async function exportFile(page,scope,name){
  const downloadEvent=page.waitForEvent('download');await scope.locator('[data-export]').click();const download=await downloadEvent;
  const file=resolve(output,name);await download.saveAs(file);const raw=await readFile(file,'utf8');
  const parsed=parseBackup(raw);await scope.locator('[data-yes]').click();return {file,raw,parsed};
}
async function selectFile(page,scope,file){
  const event=page.waitForEvent('filechooser');await scope.locator('[data-import]').click();await (await event).setFiles(file);
  await scope.locator('[data-yes]').waitFor({state:'visible'});
}
try{
  const reviewContext=await browser.newContext({viewport:{width:1200,height:1000},acceptDownloads:true});
  const review=await reviewContext.newPage();watch(review);const preview=review.frameLocator('#preview');
  await review.goto(base+'/review?scene=v8-settings');await preview.getByRole('dialog',{name:'小厨房设置',exact:true}).waitFor();
  const settingsBackup=await exportFile(review,preview,'settings-backup.json');assert.equal(settingsBackup.parsed.cp,12480);assert.equal(settingsBackup.parsed.version,CURRENT_SAVE_VERSION);
  await review.goto(base+'/review?scene=v9-dim-sum');await preview.getByRole('button',{name:'使用竹蒸笼',exact:true}).waitFor();
  await preview.getByRole('button',{name:'设置',exact:true}).click();
  const batchBackup=await exportFile(review,preview,'active-batch-backup.json');assert.equal(batchBackup.parsed.cp,25000);assert.equal(batchBackup.parsed.batch.eggs.length,24);
  await reviewContext.close();

  // A fresh context has no connection to the user's normal browser storage.
  const normalContext=await browser.newContext({viewport:{width:430,height:932},acceptDownloads:true});
  const normal=await normalContext.newPage();watch(normal);await normal.goto(base+'/');
  await normal.getByRole('button',{name:'开始游戏',exact:true}).click();await normal.locator('[data-control-id="nav:0"][tabindex="0"]').waitFor();await normal.getByRole('button',{name:'设置',exact:true}).click();
  const read=()=>normal.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
  const initial=durable(await read());assert.equal(initial.cp,600);
  await selectFile(normal,normal,batchBackup.file);
  assert.ok((await normal.locator('.confirm p').textContent()).includes('25,000 CP'));
  await normal.locator('[data-no]').click();assert.deepEqual(durable(await read()),initial);
  await selectFile(normal,normal,batchBackup.file);await normal.locator('[data-yes]').click();
  await normal.locator('.confirm p').filter({hasText:'进度已恢复'}).waitFor();await normal.locator('[data-yes]').click();
  assert.deepEqual(durable(await read()),durable(batchBackup.parsed));
  assert.equal(await normal.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1.pre-import')).cp),600);
  await normal.reload();await normal.getByRole('button',{name:'开始游戏',exact:true}).waitFor();
  assert.deepEqual(durable(await read()),durable(batchBackup.parsed));
  await normal.getByRole('button',{name:'开始游戏',exact:true}).click();await normal.locator('[data-control-id="nav:0"][tabindex="0"]').waitFor();await normal.getByRole('button',{name:'设置',exact:true}).click();
  const invalid=resolve(output,'invalid-backup.json');await writeFile(invalid,'{broken');
  const beforeInvalid=durable(await read());await selectFile(normal,normal,invalid);
  assert.ok((await normal.locator('.confirm p').textContent()).includes('当前进度已保留'));await normal.locator('[data-yes]').click();
  assert.deepEqual(durable(await read()),beforeInvalid);await normal.screenshot({path:resolve(output,'restored-settings.png')});
  await normalContext.close();

  const nativeContext=await browser.newContext({viewport:{width:430,height:932}});
  await nativeContext.addInitScript(()=>{
    const mock=window.__nativeMock={raw:null,allow:false,permissionRequests:0,importCandidate:null,importCommits:0};
    const status=()=>({supported:true,permissionGranted:mock.allow,notificationsEnabled:mock.allow,exactAllowed:true,message:mock.allow?'模拟权限：已允许通知':'模拟权限：未允许通知'});
    const response=(id,result)=>queueMicrotask(()=>window.dispatchEvent(new CustomEvent('chick:native',{detail:{type:'response',id,result}})));
    window.ChickNative={
      platformInfo:()=>JSON.stringify({android:true,version:'1.0.0-test'}),
      loadSave:()=>JSON.stringify(mock.raw?{status:'ok',raw:mock.raw}:{status:'empty'}),
      saveGame:raw=>{mock.raw=raw;return JSON.stringify({ok:true});},
      importGame:raw=>{mock.raw=raw;mock.importCommits++;return JSON.stringify({ok:true});},
      notificationStatus:()=>JSON.stringify(status()),
      requestNotifications:id=>{mock.permissionRequests++;response(id,status());},
      importSave:id=>response(id,mock.importCandidate?{ok:true,raw:mock.importCandidate}:{ok:false,message:'已取消'}),
      ready(){},openNotificationSettings(){},openExactAlarmSettings(){},testNotification:()=>JSON.stringify({ok:true,message:'模拟通知'}),closeApp(){},
    };
  });
  const native=await nativeContext.newPage();watch(native);await native.goto(base+'/');
  await native.getByRole('button',{name:'开始游戏',exact:true}).click();await native.locator('[data-control-id="nav:0"][tabindex="0"]').waitFor();
  await native.locator('[data-control-id="tool:0" ]').waitFor({state:'visible'});await native.getByRole('button',{name:'设置',exact:true}).click();
  const nativeState=()=>native.evaluate(()=>JSON.parse(window.__nativeMock.raw));
  await native.getByRole('switch',{name:'孵化完成提醒',exact:true}).click();
  await native.locator('.confirm p').filter({hasText:'通知尚未获准'}).waitFor();await native.locator('[data-yes]').click();assert.equal((await nativeState()).alarm,false);
  await native.evaluate(()=>{window.__nativeMock.allow=true;});await native.getByRole('switch',{name:'孵化完成提醒',exact:true}).click();
  await native.getByRole('switch',{name:'孵化完成提醒',exact:true}).filter({hasText:'开'}).waitFor();assert.equal((await nativeState()).alarm,true);
  await native.getByRole('switch',{name:'孵化完成提醒',exact:true}).click();assert.equal((await nativeState()).alarm,false);
  const beforeCancel=durable(await nativeState());await native.locator('[data-import]').click();assert.deepEqual(durable(await nativeState()),beforeCancel);
  await native.evaluate(raw=>{window.__nativeMock.importCandidate=raw;},batchBackup.raw);await native.locator('[data-import]').click();
  await native.locator('[data-no]').click();assert.deepEqual(durable(await nativeState()),beforeCancel);
  assert.equal(await native.evaluate(()=>window.__nativeMock.importCommits),0);
  await native.locator('[data-import]').click();await native.locator('[data-yes]').click();await native.locator('.confirm p').filter({hasText:'进度已恢复'}).waitFor();
  assert.deepEqual(durable(await nativeState()),durable(batchBackup.parsed));assert.equal(await native.evaluate(()=>window.__nativeMock.importCommits),1);
  assert.equal(await native.evaluate(()=>window.__nativeMock.permissionRequests),2);
  await nativeContext.close();
  assert.deepEqual(errors,[]);
  const report={checkedAt:new Date().toISOString(),errors,checks:['v8-settings真实导出按钮生成可验证JSON','24枚当前批次导出','正式网页隔离context导入取消不改进度','确认导入保留CP/图鉴/库存/批次截止时间','刷新后正式存档仍存在','保存导入前恢复点','损坏JSON拒绝且不改进度','模拟native权限拒绝/允许/关闭提醒','模拟native文件选择取消、导入取消及确认仅提交一次'],nativeDeviceTest:false};
  await writeFile(resolve(output,'report.json'),JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));
}finally{await browser.close();}
