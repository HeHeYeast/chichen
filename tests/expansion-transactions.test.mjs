import test from 'node:test';
import assert from 'node:assert/strict';
import {freshState,normalizeSave,startBatch,collect,updateBatch,sell} from '../web/engine.js';
import {execute,acquireWriter} from '../web/game-commands.js';
import {createSaveStore,parseBackup,MAX_SAVE_LENGTH,utf8Length} from '../web/save-store.js';
import {unit} from '../web/rng.js';
import {syncProgress} from '../web/progression.js';
const NOW=1800000000000;
function env({native=false}={}){
  let raw=null,mode='ok';const storage={getItem:k=>k==='test'?raw:null,setItem(k,v){if(k==='test'){if(mode==='before')throw Error('disk');raw=v;if(mode==='after')throw Error('ack');}}};
  const bridge={loadSave(){if(mode==='unreadable')throw Error('offline');return JSON.stringify(raw===null?{status:'empty'}:{status:'ok',raw});},saveGame(v){if(mode==='before')throw Error('disk');raw=v;if(mode==='after')throw Error('ack');if(mode==='unknown'){mode='unreadable';throw Error('ack');}return '{"ok":true}';}};
  const store=createSaveStore({storage,key:'test',native:native?bridge:null,now:()=>NOW});let state=store.load().state;store.write(state);
  return {store,get state(){return state;},get raw(){return raw;},mode:v=>mode=v,run(command,reduce){const r=execute({state,store,command,now:NOW,reduce});state=r.state;return r;}};
}
test('v3→4 is pure deterministic and preserves assets, tickets, old claimed/unclaimed gifts',()=>{
  const old=freshState(NOW);old.version=3;for(const k of ['meta','clock','contentRevision','expansion'])delete old[k];
  old.farm={'0:0':27};old.total={'0:0':100};old.events={seasonalCollections:{spring:true,summer:false},shrineCollections:{first:false}};
  startBatch(old,0,NOW,()=>.4);syncProgress(old);const before=structuredClone(old),a=normalizeSave(old,NOW),b=normalizeSave(old,NOW+99999);
  assert.deepEqual(a,b);assert.deepEqual(old,before);for(const k of Object.keys(old))if(k!=='version')assert.deepEqual(a[k],old[k],k);
  assert.deepEqual(normalizeSave(a),a);assert.equal(a.meta.revision,0);assert.deepEqual(a.expansion.discovery,{cards:{},identified:{}});
});
test('current schema missing extension fields is corrupt, never defaulted during load',()=>{
  for(const path of [['meta'],['clock'],['expansion'],['expansion','methods'],['meta','rng'],['expansion','discovery','cards']]){
    const s=freshState(NOW);let o=s;for(const key of path.slice(0,-1))o=o[key];delete o[path.at(-1)];assert.throws(()=>normalizeSave(s),/无效/);
  }
});
test('command replay, payload reuse, stale revision and old sequence cannot repeat CP',()=>{
  const e=env(),r=e.run({type:'reward',cp:5},s=>s.cp+=5);assert.equal(r.state.cp,605);
  const same=execute({state:e.state,store:e.store,commandId:'cmd-1',expectedRevision:0,command:{type:'reward',cp:5},now:NOW,reduce:()=>assert.fail('replay')});assert.equal(same.replayed,true);
  assert.throws(()=>execute({state:e.state,store:e.store,commandId:'cmd-1',command:{type:'other'},now:NOW,reduce(){}}),/编号/);
  assert.throws(()=>execute({state:e.state,store:e.store,expectedRevision:0,command:{type:'next'},now:NOW,reduce(){}}),/变化/);
  assert.equal(JSON.parse(e.raw).cp,605);
});
for(const native of [false,true])for(const mode of ['before','after'])test(`${native?'Native':'Web'} commit fault ${mode} yields exactly old or new state`,()=>{
  const e=env({native});e.mode(mode);
  if(mode==='before'){assert.throws(()=>e.run({type:'sale'},s=>{s.farm={'0:0':10};s.cp+=30;}),/disk/);assert.equal(JSON.parse(e.raw).cp,600);}
  else{e.run({type:'sale'},s=>{s.farm={'0:0':10};s.cp+=30;});assert.equal(e.state.cp,630);assert.equal(JSON.parse(e.raw).meta.revision,1);}
});
test('ACK unknown locks writes until reload instead of restoring over the committed result',()=>{
  const e=env({native:true});e.mode('unknown');assert.throws(()=>e.run({type:'sale'},s=>s.cp+=9),x=>x.code==='ACK_UNKNOWN');
  assert.equal(e.store.locked,true);assert.equal(JSON.parse(e.raw).cp,609);e.mode('ok');const restored=e.store.load().state;assert.equal(restored.meta.commandSeq,1);assert.equal(restored.cp,609);
});
test('writer lock makes second browser document read-only and unsupported browsers fail closed',async()=>{
  let held=false;const locks={async request(name,options,cb){if(held)return cb(null);held=true;try{return await cb({name});}finally{held=false;}}};
  const first=await acquireWriter({key:'x',locks}),second=await acquireWriter({key:'x',locks});assert.equal(first.writable,true);assert.equal(second.writable,false);first.release();
  assert.equal((await acquireWriter({key:'x',locks:null})).writable,false);
});
test('stale store refuses second writer even before new command is reduced',()=>{
  const data=new Map(),storage={getItem:k=>data.get(k)??null,setItem:(k,v)=>data.set(k,v)};
  const a=createSaveStore({storage,key:'x',now:()=>NOW}),b=createSaveStore({storage,key:'x',now:()=>NOW});const sa=a.load().state;a.write(sa);const sb=b.load().state;
  execute({state:sa,store:a,command:{type:'first'},now:NOW,reduce:s=>s.cp++});assert.throws(()=>execute({state:sb,store:b,command:{type:'second'},now:NOW,reduce:()=>assert.fail('stale reducer ran')}),x=>x.code==='REVISION_CONFLICT');
});
test('first upgrade preserves exact old bytes, and backup failure prevents the commit',()=>{
  const old=freshState(NOW);old.version=3;for(const k of ['meta','clock','contentRevision','expansion'])delete old[k];
  const raw=JSON.stringify(old,null,2).replaceAll('\n','\r\n');
  for(const fail of [false,true]){
    const data=new Map([['x',raw]]),storage={getItem:k=>data.get(k)??null,setItem(k,v){if(fail&&k==='x.pre-upgrade-v3')throw Error('backup disk');data.set(k,v);}};
    const store=createSaveStore({storage,key:'x',now:()=>NOW});const s=store.load().state;
    const run=()=>execute({state:s,store,command:{type:'upgrade'},now:NOW,reduce:s=>s.cp++});
    if(fail){assert.throws(run,/backup disk/);assert.equal(data.get('x'),raw);}
    else{run();assert.equal(data.get('x.pre-upgrade-v3'),raw);assert.equal(JSON.parse(data.get('x')).cp,601);}
  }
});
test('UTF8 two-MiB backup limit counts Chinese bytes and accepts exact boundary',()=>{
  const raw=JSON.stringify(freshState(NOW));const exact=raw+' '.repeat(MAX_SAVE_LENGTH-utf8Length(raw));assert.equal(parseBackup(exact).cp,600);
  assert.throws(()=>parseBackup(exact+' '),/太大/);assert.throws(()=>parseBackup('汉'.repeat(Math.ceil(MAX_SAVE_LENGTH/3))),/太大/);
});
test('economic channels deterministic, separated, bounded, migration never calls RNG',()=>{
  const a=Array.from({length:1000},(_,i)=>unit(123,'cmd-1','cook',i));assert.deepEqual(a,Array.from({length:1000},(_,i)=>unit(123,'cmd-1','cook',i)));assert.ok(a.every(n=>n>=0&&n<1));assert.notEqual(a[0],unit(123,'cmd-1','trip',0));
  const random=Math.random;try{Math.random=()=>assert.fail('migration sampled');normalizeSave(freshState(NOW));}finally{Math.random=random;}
});
test('upgraded old loop cooks, collects, sells and reloads without changing money twice',()=>{
  const e=env();e.run({type:'cook'},s=>startBatch(s,0,NOW,()=>.5));const end=e.state.batch.ends;
  e.run({type:'hatch'},s=>{updateBatch(s,end+1,()=>.9);updateBatch(s,end+2001,()=>.9);updateBatch(s,end+3001,()=>.9);for(let i=0;i<24;i++)collect(s,i,end+3001);});
  assert.equal(Object.values(e.state.farm).reduce((a,b)=>a+b,0),24);const before=e.state.cp,stock=structuredClone(e.state.farm);
  const sale=e.run({type:'sell'},s=>sell(s,stock,{},end+3001));assert.equal(sale.state.cp,before+sale.result);assert.equal(Object.values(sale.state.farm).reduce((a,b)=>a+b,0),0);assert.deepEqual(e.store.load().state,sale.state);
});
