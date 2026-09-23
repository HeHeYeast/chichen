import {CURRENT_SAVE_VERSION} from '../web/save-migrations.js';
import {test} from 'node:test';
import assert from 'node:assert/strict';
import {createSaveStore,parseBackup,makeBackup} from '../web/save-store.js';
import {freshState,startBatch,updateBatch,collect} from '../web/engine.js';
const NOW=1789290000000,KEY='player';
function setup(entries={}){
  const data=new Map(Object.entries(entries));let fail='';
  const storage={getItem:k=>data.get(k)??null,setItem(k,v){if(fail===k)throw Error('disk full');data.set(k,v);}};
  return {data,storage,failOn:k=>fail=k,store:createSaveStore({storage,key:KEY,now:()=>NOW})};
}
const raw=(cp=600)=>JSON.stringify({...freshState(NOW),cp});
test('first installation starts once; later loads keep progress',()=>{
  const env=setup(),state=env.store.load().state;state.cp=3456;env.store.write(state);
  assert.equal(env.store.load().state.cp,3456);
});
test('legacy app progress migrates without losing current hatch deadlines or inventory',()=>{
  const state=freshState(NOW);state.toolLevels[1]=0;state.cp=4000;startBatch(state,1,NOW,()=>.4);
  delete state.batch.rules;delete state.progress;delete state.cleanCycle;state.version=1;state.toolLevels.pop();state.total={'0:18':20};state.farm={'0:18':7};
  const env=setup({[KEY]:JSON.stringify(state)}),loaded=env.store.load().state;
  assert.equal(loaded.version,CURRENT_SAVE_VERSION);assert.equal(loaded.toolLevels[8],-1);
  assert.deepEqual(loaded.batch,state.batch);assert.deepEqual(loaded.farm,state.farm);assert.equal(loaded.cp,state.cp);
});
test('corrupt primary falls back to known good; next save does not replace backup with corruption',()=>{
  const env=setup({[KEY]:'{broken',[KEY+'.previous']:raw(4444)});
  const loaded=env.store.load();assert.match(loaded.notice,/恢复/);assert.equal(loaded.state.cp,4444);
  loaded.state.cp++;env.store.write(loaded.state);
  assert.equal(JSON.parse(env.data.get(KEY+'.previous')).cp,4444);
  assert.equal(env.data.get(KEY+'.unreadable'),'{broken');
});

test('recovery must preserve unreadable primary before overwriting it',()=>{
  const env=setup({[KEY]:'{broken',[KEY+'.previous']:raw(4444)});
  const loaded=env.store.load();env.failOn(KEY+'.unreadable');
  assert.throws(()=>env.store.write(loaded.state),/disk full/);
  assert.equal(env.data.get(KEY),'{broken');
  assert.equal(JSON.parse(env.data.get(KEY+'.previous')).cp,4444);
});

test('missing primary recovers previous generation instead of starting over',()=>{
  const env=setup({[KEY+'.previous']:raw(4567)});
  const loaded=env.store.load();assert.equal(loaded.state.cp,4567);
  assert.match(loaded.notice,/恢复/);env.store.write(loaded.state);
  assert.equal(JSON.parse(env.data.get(KEY)).cp,4567);
});
test('future save never falls back to stale backup or allows automatic overwrite',()=>{
  const future=JSON.stringify({...freshState(NOW),version:8});
  const env=setup({[KEY]:future,[KEY+'.previous']:raw()});
  assert.equal(env.store.load().state,null);assert.equal(env.store.locked,true);
  assert.throws(()=>env.store.write(freshState(NOW)),/暂停/);assert.equal(env.data.get(KEY),future);
});
test('both broken saves lock; an invalid import leaves all bytes unchanged',()=>{
  const env=setup({[KEY]:'old damaged',[KEY+'.previous']:'damaged too'});
  env.store.load();assert.throws(()=>parseBackup('{bad'));assert.throws(()=>env.store.write({cp:12},{importing:true}));
  assert.equal(env.data.get(KEY),'old damaged');assert.equal(env.data.get(KEY+'.previous'),'damaged too');
});
test('confirmed valid import saves previous payload before replacing and unlocks recovery',()=>{
  const env=setup({[KEY]:'recoverable raw'});env.store.load();
  const state=parseBackup(makeBackup({...freshState(NOW),cp:9000},NOW));env.store.write(state,{importing:true});
  assert.equal(env.data.get(KEY+'.pre-import'),'recoverable raw');assert.equal(env.store.locked,false);
  assert.equal(env.store.load().state.cp,9000);
});
test('failed pre-import backup cannot overwrite current progress',()=>{
  const env=setup({[KEY]:raw(700)});env.store.load();env.failOn(KEY+'.pre-import');
  assert.throws(()=>env.store.write({...freshState(NOW),cp:9999},{importing:true}),/disk full/);
  assert.equal(JSON.parse(env.data.get(KEY)).cp,700);
});
test('failed main write is reported and original primary remains readable',()=>{
  const env=setup({[KEY]:raw(700)});const state=env.store.load().state;state.cp=800;env.failOn(KEY);
  assert.throws(()=>env.store.write(state),/disk full/);assert.equal(JSON.parse(env.data.get(KEY)).cp,700);
});
test('portable backup can resume and collect existing eggs exactly once',()=>{
  const state=freshState(NOW);startBatch(state,0,NOW,()=>.5);
  updateBatch(state,state.batch.ends+1,()=>.5);updateBatch(state,state.batch.ends+3001,()=>.5);
  const imported=parseBackup(makeBackup(state,NOW));const before=imported.cp;
  assert.equal(collect(imported,0),true);assert.equal(collect(imported,0),false);assert.equal(imported.cp,before+1);
  assert.deepEqual(imported.batch.eggs.slice(1),state.batch.eggs.slice(1));
});
test('backup accepts legacy raw files and rejects unrelated or newer envelopes',()=>{
  const old={...freshState(NOW),version:1};old.toolLevels.pop();
  assert.equal(parseBackup(JSON.stringify(old)).version,CURRENT_SAVE_VERSION);
  assert.throws(()=>parseBackup(JSON.stringify({format:'other',save:old})),/不是/);
  assert.throws(()=>parseBackup(JSON.stringify({format:'chick-kitchen',formatVersion:2,save:old})),/更新/);
  assert.throws(()=>parseBackup('x'.repeat(2*1024*1024+1)),/太大/);
});
test('native errors stay locked and native write failure is not treated as successful save',()=>{
  let reply={status:'error',message:'storage damaged'},failWrite=false;
  const native={loadSave:()=>JSON.stringify(reply),saveGame:()=>JSON.stringify({ok:!failWrite,message:'write failed'})};
  const store=createSaveStore({native,now:()=>NOW});assert.equal(store.load().state,null);
  reply={status:'ok',raw:raw(800)};const state=store.load().state;assert.equal(state.cp,800);
  state.cp++;failWrite=true;assert.throws(()=>store.write(state),/write failed/);
});
