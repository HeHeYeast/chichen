import test from 'node:test';
import assert from 'node:assert/strict';
import {createLocalDatabase} from '../cloud/local-database.mjs';
import {createService} from '../cloud/service.mjs';
import {freshState} from '../web/engine.js';
import {createManualCloud} from '../web/cloud-client.js';
const state=()=>freshState(1791388800000,42);
const memory=()=>{const m=new Map();return {getItem:k=>m.get(k)??null,setItem:(k,v)=>m.set(k,v)};};
async function setup(){
  const db=createLocalDatabase(':memory:'),service=createService({db,allowedUsernames:['alice','bob'],origin:'http://localhost:4173',epoch:'test'});
  const call=async(path,body,token,method=body?'POST':'GET')=>{const r=await service(new Request('http://localhost/v1/'+path,{method,headers:{'content-type':'application/json',...(token?{authorization:'Bearer '+token}:{})},...(body?{body:JSON.stringify(body)}:{})}));const data=r.status===204?null:await r.json();return {status:r.status,data};};
  const a=(await call('accounts',{username:'alice',password:'test-passphrase-123'})).data;
  return {db,call,a,upload:(s,rev,id=crypto.randomUUID(),token=a.token)=>call('saves',{raw:JSON.stringify(s),expectedRevision:rev,uploadId:id,epoch:'test'},token)};
}
test('concurrent devices cannot silently overwrite and retries do not create another revision',async()=>{
  const t=await setup();try{
    const s=state(),id=crypto.randomUUID();await t.upload(s,0,id);
    assert.equal((await t.upload(s,0,id)).data.revision,1);
    s.cp++;assert.equal((await t.upload(s,0,id)).status,409);
    const other=state();other.cp+=30;
    const results=await Promise.all([t.upload(s,1),t.upload(other,1)]);
    assert.deepEqual(results.map(r=>r.status).sort(),[201,409]);
    assert.equal((await t.call('saves/current',null,t.a.token)).data.save.revision,2);
  }finally{t.db.close();}
});
test('accounts are isolated, future and corrupt saves are refused, history is bounded',async()=>{
  const t=await setup();try{
    const b=(await t.call('accounts',{username:'bob',password:'different-test-password'})).data;
    await t.upload(state(),0);assert.equal((await t.call('saves/current',null,b.token)).data.save,null);
    assert.equal((await t.call('saves/history/1',null,b.token)).status,404);
    const bad=state();bad.cp=-1;assert.equal((await t.upload(bad,1)).status,422);
    bad.version=999;assert.equal((await t.upload(bad,1)).status,426);
    assert.equal((await t.call('saves/current',null,'invalid')).status,401);
    for(let i=1;i<24;i++){const s=state();s.cp+=i;assert.equal((await t.upload(s,i)).status,201);}
    const rows=await t.db.prepare('SELECT count(*) AS n FROM saves WHERE uid=?').bind(t.a.uid).first();assert.equal(rows.n,21);
    assert.equal((await t.call('saves/history?limit=10000',null,t.a.token)).status,422);
    assert.equal((await t.call('saves/current',null,t.a.token)).data.save.revision,24);
  }finally{t.db.close();}
});
test('manual client retains local changes after offline failure, retries lost acknowledgements, and refuses stale previews',async()=>{
  const t=await setup();try{
    let raw=JSON.stringify(state()),offline=false,drop=false;
    const api=async(path,options={})=>{if(offline)throw Error('offline');const r=await t.call(path,options.body,t.a.token,options.method);if(r.status>=400)throw Object.assign(Error(r.data.error.message),{code:r.data.error.code,status:r.status});if(drop){drop=false;throw Error('lost ACK');}return r.data;};
    const storage=memory(),make=()=>createManualCloud({api,storage,key:'profile-a',getRaw:()=>raw,replaceRaw:r=>{raw=r;},epoch:'test'});
    offline=true;await assert.rejects(make().backup(),/offline/);assert.equal(JSON.parse(raw).cp,600);
    offline=false;drop=true;await assert.rejects(make().backup(),/lost ACK/);
    assert.equal((await make().backup()).revision,1);
    const preview=await make().preview();const changed=state();changed.cp=777;raw=JSON.stringify(changed);
    await assert.rejects(make().restore(preview),/本机进度发生变化/);assert.equal(JSON.parse(raw).cp,777);
    const fresh=await make().preview();await make().restore(fresh);assert.equal(JSON.parse(raw).cp,600);assert.equal(JSON.parse(storage.getItem('profile-a.cloud-before-restore')).cp,777);
  }finally{t.db.close();}
});
test('failed restore metadata commit blocks later uploads instead of replaying a pre-restore pending snapshot',async()=>{
  const t=await setup();try{
    await t.upload(state(),0);let raw=JSON.stringify({...state(),cp:888});const storage=memory();let replaced=false;
    const safeStorage={getItem:storage.getItem,setItem:(k,v)=>{if(replaced&&k.endsWith('.cloud-v1'))throw Error('quota');storage.setItem(k,v);}};
    const api=async(path,o={})=>{const r=await t.call(path,o.body,t.a.token,o.method);if(r.status>=400)throw Object.assign(Error(r.data.error.message),{code:r.data.error.code});return r.data;};
    const sync=createManualCloud({api,storage:safeStorage,key:'a',getRaw:()=>raw,replaceRaw:r=>{raw=r;replaced=true;},epoch:'test'});
    await assert.rejects(sync.restore(await sync.preview()),/quota/);
    replaced=false;await assert.rejects(sync.backup(),/恢复记录尚未确认/);
  }finally{t.db.close();}
});
