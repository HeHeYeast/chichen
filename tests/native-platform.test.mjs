import test from 'node:test';
import assert from 'node:assert/strict';
import {createPlatform} from '../web/native-platform.js';

function setup({disabled=false}={}){
  const listeners=new Map(),calls=[];
  const bridge={
    platformInfo:()=>JSON.stringify({android:true,version:'1.0.0',versionCode:1}),
    notificationStatus:()=>JSON.stringify({supported:true,permissionGranted:true,notificationsEnabled:true,exactAllowed:false}),
    requestNotifications:(...args)=>calls.push(['permission',...args]),
    exportSave:(...args)=>calls.push(['export',...args]),
    importSave:(...args)=>calls.push(['import',...args]),
    openNotificationSettings:()=>calls.push(['notification-settings']),
    openExactAlarmSettings:()=>calls.push(['exact-settings']),
    testNotification:()=>JSON.stringify({ok:true,message:'测试提醒已发送'}),
    closeApp:()=>calls.push(['close']),
  };
  const host={ChickNative:bridge,addEventListener:(name,callback)=>listeners.set(name,callback)};
  const platform=createPlatform({window:host,disabled});
  const respond=(id,result,type='response')=>listeners.get('chick:native')({detail:{type,id,result}});
  return {platform,bridge,calls,respond};
}

test('native permission request preserves granted and denied status without inventing success',async()=>{
  const h=setup();
  const denied=h.platform.requestNotifications();
  h.respond(h.calls.at(-1)[1],{supported:true,permissionGranted:false,notificationsEnabled:false});
  assert.equal((await denied).permissionGranted,false);
  const granted=h.platform.requestNotifications();
  h.respond(h.calls.at(-1)[1],{supported:true,permissionGranted:true,notificationsEnabled:true});
  assert.equal((await granted).notificationsEnabled,true);
});

test('native file request IDs keep export/import responses separate and preserve backup bytes',async()=>{
  const h=setup(),raw='{"name":"鸡宝\\n厨房","count":12}';
  const exported=h.platform.exportBackup(raw),exportId=h.calls.at(-1)[1];
  assert.equal(h.calls.at(-1)[2],raw);
  const imported=h.platform.selectBackup(),importId=h.calls.at(-1)[1];
  assert.notEqual(exportId,importId);
  h.respond(importId,{ok:true,raw});h.respond(exportId,{ok:true,message:'已导出'});
  assert.deepEqual(await imported,{ok:true,raw});assert.deepEqual(await exported,{ok:true,message:'已导出'});
});

test('cancelled picker completes the native request without changing a save',async()=>{
  const h=setup(),request=h.platform.selectBackup();
  h.respond(h.calls.at(-1)[1],{ok:false,message:'已取消'});
  assert.deepEqual(await request,{ok:false,message:'已取消'});
  assert.deepEqual(h.calls.map(call=>call[0]),['import']);
});

test('unrelated lifecycle event and duplicate response cannot complete another pending request',async()=>{
  const h=setup(),request=h.platform.selectBackup(),id=h.calls.at(-1)[1];let completed=0;
  request.then(()=>completed++);
  h.respond(id,{ok:true},'resume');h.respond('unknown-id',{ok:true});
  await Promise.resolve();assert.equal(completed,0);
  h.respond(id,{ok:false,message:'已取消'});h.respond(id,{ok:true,raw:'not selected'});
  assert.deepEqual(await request,{ok:false,message:'已取消'});
});

test('synchronous native failure rejects only that request and later request still works',async()=>{
  const h=setup();h.bridge.importSave=()=>{throw Error('picker unavailable');};
  await assert.rejects(h.platform.selectBackup(),/picker unavailable/);
  h.bridge.importSave=(...args)=>h.calls.push(['import',...args]);
  const recovered=h.platform.selectBackup();h.respond(h.calls.at(-1)[1],{ok:true,raw:'valid selection'});
  assert.equal((await recovered).raw,'valid selection');
});

test('review mode isolates the native bridge and exposes browser notification limitation',async()=>{
  const h=setup({disabled:true});
  assert.equal(h.platform.bridge,null);assert.equal(h.platform.info.android,false);
  assert.equal((await h.platform.requestNotifications()).supported,false);
  assert.equal(h.platform.testNotification().ok,false);
  h.platform.openNotificationSettings();h.platform.openExactAlarmSettings();h.platform.closeApp();
  assert.deepEqual(h.calls,[]);
});

test('unreadable native notification status cannot enable a test or claim permission',()=>{
  const h=setup();h.bridge.notificationStatus=()=>'{broken';
  const status=h.platform.notificationStatus();
  assert.equal(status.supported,true);assert.equal(status.permissionGranted,false);
  assert.match(status.message,/无法读取/);
});

test('settings and immediate test route through explicit native operations',()=>{
  const h=setup();h.platform.openNotificationSettings();h.platform.openExactAlarmSettings();
  assert.equal(h.platform.testNotification().ok,true);h.platform.closeApp();
  assert.deepEqual(h.calls,[['notification-settings'],['exact-settings'],['close']]);
});
