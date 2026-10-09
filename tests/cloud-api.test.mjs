import test from 'node:test';
import assert from 'node:assert/strict';
import {createLocalDatabase} from '../cloud/local-database.mjs';
import {createService} from '../cloud/service.mjs';
import {freshState} from '../web/engine.js';
import {readFileSync,readdirSync} from 'node:fs';

test('old player registers and backs up the entire existing save without changing rewards',async()=>{
  const db=createLocalDatabase(':memory:');
  try{
    const service=createService({db,allowedUsernames:['alice'],origin:'http://localhost:4173',epoch:'test'});
    const call=async(path,body,token)=>{const r=await service(new Request('http://localhost/v1/'+path,{method:'POST',headers:{'content-type':'application/json',...(token?{authorization:'Bearer '+token}:{})},body:JSON.stringify(body)}));return {status:r.status,data:await r.json()};};
    const auth=await call('accounts',{username:'alice',password:'test-passphrase-123'});assert.equal(auth.status,201);
    const state=freshState(1791388800000,42);state.cp=12345;
    const saved=await call('saves',{raw:JSON.stringify(state),expectedRevision:0,epoch:'test',uploadId:crypto.randomUUID()},auth.data.token);
    assert.equal(saved.status,201);assert.equal(saved.data.revision,1);
    const response=await service(new Request('http://localhost/v1/saves/current',{headers:{authorization:'Bearer '+auth.data.token}}));
    assert.deepEqual(JSON.parse((await response.json()).save.raw),state);
    let revision=1;
    for(const file of readdirSync(new URL('./fixtures/golden/',import.meta.url)).filter(n=>n.endsWith('.json')&&n!=='expected.json')){
      const raw=readFileSync(new URL('./fixtures/golden/'+file,import.meta.url),'utf8');
      const legacy=await call('saves',{raw,expectedRevision:revision,epoch:'test',uploadId:crypto.randomUUID()},auth.data.token);assert.equal(legacy.status,201,file);revision++;
      const restored=await service(new Request('http://localhost/v1/saves/current',{headers:{authorization:'Bearer '+auth.data.token}}));assert.equal((await restored.json()).save.raw,raw,file+' original bytes must survive cloud storage');
    }
  }finally{db.close();}
});
