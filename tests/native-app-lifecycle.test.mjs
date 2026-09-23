import {execute} from '../web/game-commands.js';
import test from 'node:test';
import assert from 'node:assert/strict';
import {readFileSync} from 'node:fs';
import vm from 'node:vm';
import * as E from '../web/engine.js';

// Execute the real app handlers with lifecycle/audio/storage adapters. Geometry
// and Android WebView lifecycle delivery still need browser/device verification.
const app=readFileSync(new URL('../web/app.js',import.meta.url),'utf8');
const lifecycle=app.slice(app.indexOf("window.addEventListener('chick:native'"),app.indexOf('function gamePoint('));
const toggle=app.slice(app.indexOf('async function toggleHatchAlarm('),app.indexOf('async function exportProgress('));
const commit=app.slice(app.indexOf('function commitProgress('),app.indexOf('let toastTimer;'));
const boot=app.slice(app.indexOf('loaded=true;'),app.indexOf('function advanceClock('));
const NOW=1800000000000;
function harness(){
  const events=new Map(),calls={saved:0,music:0,paused:0,resumed:0,settings:0,close:0,permissions:0,alerts:[],announcements:[],changed:[]};
  const context={
    structuredClone,execute,review:false,committedState:null,saveStore:{write(candidate){calls.saved++;if(!context.saveWorks)throw Error('磁盘空间不足');}},window:{addEventListener:(type,fn)=>events.set(type,fn)},
    state:E.freshState(NOW),E,page:0,panel:'',loaded:true,recoveryError:'',lastSaveError:'磁盘空间不足',nativeKitchenPending:false,
    initialSave:{},time:NOW,saveWorks:true,permission:true,notifications:true,
    dialogs:{children:[]},panels:{querySelector:()=>({click(){context.panel='';}})},
    platform:{info:{android:true},bridge:{ready(){}},closeApp(){calls.close++;},async requestNotifications(){calls.permissions++;return {permissionGranted:context.permission,notificationsEnabled:context.notifications};}},
    now:()=>context.time,advanceClock(){calls.resumed++;},cancelInput(){},resetInput(){},dismissToast(){},renderControls(){},paint(){},sound(){},makeWalkers(){},collectionUI:{refresh(){}},
    save(){calls.saved++;if(context.saveWorks)context.state.lastSeen=context.time;return context.saveWorks;},
    music(){calls.music++;},bgm:{pause(){calls.paused++;}},
    openSettings(){calls.settings++;},openRecovery(){},
    closePanel(){context.panel='';},closeDialog(){context.dialogs.children=[];},
    changePage(index){context.page=index;calls.changed.push(index);},
    announce(message){calls.announcements.push(message);},alertBox(message){calls.alerts.push(message);},
    confirmBox(message,callback){context.confirm=callback;},
  };
  vm.createContext(context);vm.runInContext(commit+'\n'+lifecycle+'\n'+toggle,context);
  return {context,calls,emit:(type,result=null)=>events.get('chick:native')({detail:{type,result}}),boot:()=>vm.runInContext(boot,context)};
}

test('native pause/resume alone restores music and updates offline kitchen state',()=>{
  const h=harness();h.emit('pause');assert.equal(h.calls.paused,1);
  h.context.time+=2*86400000;h.emit('resume');
  assert.equal(h.calls.music,1);assert.equal(h.context.state.dirty,true);assert.equal(h.context.state.lastSeen,h.context.time);
});

test('duplicate resumes preserving a farm operation do not apply the same farm loss twice',()=>{
  const h=harness();h.context.page=1;h.context.state.farm={'0:0':10};
  h.context.state.farmFixed=NOW-10*86400000;h.context.state.farmChecked=NOW-10*86400000;
  h.emit('resume',{keepPage:true});assert.equal(h.context.state.farm['0:0'],0);assert.equal(h.calls.alerts.length,1);
  h.context.resumeGameplay();assert.equal(h.calls.alerts.length,1);assert.equal(h.context.state.farmChecked,NOW);
});

test('ordinary resume preserves every live screen and pending UI without a transaction',()=>{
  for(const page of [-1,0,1,2,3,4]){
    const h=harness();h.context.page=page;h.context.panel='open';h.context.dialogs.children=[{}];
    E.startBatch(h.context.state,0,NOW,()=>.5);
    const batch=structuredClone(h.context.state.batch),cp=h.context.state.cp;
    h.emit('resume');
    assert.equal(h.context.page,page);assert.equal(h.context.panel,'open');assert.equal(h.context.dialogs.children.length,1);
    assert.deepEqual(h.context.state.batch,batch);assert.equal(h.context.state.cp,cp);assert.equal(h.calls.saved,1);
  }
});

test('system picker resume preserves pending confirmation and refreshes settings only after the dialog closes',()=>{
  const h=harness();h.context.page=3;h.context.panel='settings';h.context.dialogs.children=[{}];
  h.emit('resume',{keepPage:true});
  assert.equal(h.context.page,3);assert.equal(h.context.panel,'settings');assert.equal(h.context.dialogs.children.length,1);assert.equal(h.calls.settings,0);
  h.context.closeDialog();h.emit('resume',{keepPage:true});assert.equal(h.calls.settings,1);assert.equal(h.context.page,3);
});

test('ordinary cold start and resume during loading wait at the title after assets become ready',()=>{
  const h=harness();h.context.loaded=false;h.context.page=-1;h.emit('resume');
  assert.equal(h.context.page,-1);assert.equal(h.context.nativeKitchenPending,false);assert.equal(h.calls.resumed,0);
  h.boot();assert.equal(h.context.page,-1);assert.equal(h.context.loaded,true);
});

test('explicit title navigation clears a stale notification target while waiting on assets',()=>{
  const h=harness();h.context.loaded=false;h.context.page=-1;
  h.emit('kitchen');h.emit('title');h.boot();assert.equal(h.context.page,-1);
});

test('notification navigation stays in the kitchen through all subsequent warm resumes',()=>{
  const h=harness();h.context.page=-1;h.emit('kitchen');h.emit('resume',{keepPage:true});
  assert.equal(h.context.page,0);h.emit('pause');h.emit('resume');assert.equal(h.context.page,0);
});

test('ordinary warm resumes refresh settings but do not replace an open confirmation',()=>{
  const h=harness();h.context.page=3;h.context.panel='settings';
  h.emit('resume');assert.equal(h.calls.settings,1);assert.equal(h.context.page,3);
  h.context.dialogs.children=[{}];h.emit('resume');assert.equal(h.calls.settings,1);assert.equal(h.context.dialogs.children.length,1);
});

test('warm resume retains the unconfirmed action and never confirms an exit or purchase',()=>{
  const h=harness();let actions=0;
  h.context.confirmBox('确认',()=>actions++);h.context.dialogs.children=[{}];
  const pending=h.context.confirm;
  h.emit('pause');h.emit('resume');h.emit('resume',{keepPage:false});
  assert.equal(h.context.confirm,pending);assert.equal(actions,0);assert.equal(h.calls.close,0);assert.equal(h.context.page,0);
});

test('turning on reminders after permission denial leaves state and storage unchanged',async()=>{
  const h=harness();h.context.permission=false;
  await h.context.toggleHatchAlarm();
  assert.equal(h.context.state.alarm,false);assert.equal(h.calls.saved,0);assert.match(h.calls.alerts[0],/尚未获准/);
});

test('a disabled notification channel also prevents misleading enabled reminder state',async()=>{
  const h=harness();h.context.notifications=false;
  await h.context.toggleHatchAlarm();
  assert.equal(h.context.state.alarm,false);assert.equal(h.calls.saved,0);assert.equal(h.calls.permissions,1);
});

test('failed reminder save restores either previous switch state and reports failure',async()=>{
  for(const initial of [false,true]){
    const h=harness();h.context.state.alarm=initial;h.context.saveWorks=false;h.context.page=3;
    await h.context.toggleHatchAlarm();
    assert.equal(h.context.state.alarm,initial);assert.match(h.calls.alerts[0],/没有保存成功/);
    assert.equal(h.calls.announcements.length,0);assert.equal(h.calls.settings,1);
  }
});

test('disabling enabled reminders needs no new notification permission prompt',async()=>{
  const h=harness();h.context.state.alarm=true;h.context.permission=false;
  await h.context.toggleHatchAlarm();
  assert.equal(h.context.state.alarm,false);assert.equal(h.calls.permissions,0);assert.equal(h.calls.saved,1);
});

test('a kitchen navigation event during asset loading is consumed after initialization',()=>{
  const h=harness();h.context.loaded=false;h.context.page=-1;h.emit('kitchen');
  assert.equal(h.context.nativeKitchenPending,true);assert.equal(h.calls.changed.length,0);
  h.boot();assert.equal(h.context.page,0);
});

test('native ready handshake happens after game initialization and can deliver a cold-start notification',()=>{
  const h=harness();h.context.loaded=false;h.context.page=-1;let acknowledged=0;
  h.context.platform.bridge.ready=()=>{acknowledged++;assert.equal(h.context.loaded,true);h.emit('kitchen');};
  h.boot();assert.equal(acknowledged,1);assert.equal(h.context.page,0);
});

test('save recovery lock is never bypassed by a notification click or app resume',()=>{
  const h=harness();h.context.recoveryError='存档损坏';h.context.page=-1;
  h.emit('kitchen');h.emit('resume');
  assert.equal(h.context.page,-1);assert.equal(h.calls.changed.length,0);assert.equal(h.calls.saved,0);
});

test('native back cancels current confirmation before offering application exit',()=>{
  const h=harness();h.context.dialogs.children=[{}];h.emit('back');
  assert.equal(h.context.dialogs.children.length,0);assert.equal(h.calls.close,0);
  h.emit('back');assert.equal(typeof h.context.confirm,'function');h.context.saveWorks=false;h.context.confirm();
  assert.equal(h.calls.close,0);assert.match(h.calls.alerts[0],/保存尚未成功/);
});
