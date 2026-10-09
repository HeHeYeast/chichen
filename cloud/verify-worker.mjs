import {build} from 'esbuild';
import {Miniflare,convertV4MiniflareOptions} from 'miniflare';
import {readFileSync,mkdirSync,writeFileSync} from 'node:fs';
import {fileURLToPath} from 'node:url';
import assert from 'node:assert/strict';
import {freshState} from '../web/engine.js';
mkdirSync(new URL('.local/',import.meta.url),{recursive:true});
const output=fileURLToPath(new URL('.local/worker.mjs',import.meta.url));
await build({entryPoints:[fileURLToPath(new URL('worker.mjs',import.meta.url))],outfile:output,bundle:true,format:'esm',platform:'neutral',external:['node:*'],target:'es2022'});
const mf=new Miniflare(convertV4MiniflareOptions({modules:true,scriptPath:output,compatibilityDate:'2026-10-08',compatibilityFlags:['nodejs_compat'],bindings:{ENABLE_TEST_API:'true',WEB_ORIGIN:'http://localhost:4173',CLOUD_EPOCH:'workerd-local',ALLOWED_USERNAMES:'alice,bob'},d1Databases:['DB']}));
const evidence={runtime:'local workerd + Miniflare D1 (not Cloudflare deployed)',checks:[],paidResources:false};
try{
  const db=await mf.getD1Database('DB');const sql=readFileSync(new URL('schema.sql',import.meta.url),'utf8').replace(/--[^\n]*/g,'').replace(/CREATE TRIGGER[\s\S]*?END;/,s=>s.replace(/\s+/g,' '));await db.exec(sql);
  const call=async(path,body,token)=>{const r=await mf.dispatchFetch('http://localhost/v1/'+path,{method:body?'POST':'GET',headers:{'content-type':'application/json',...(token?{authorization:'Bearer '+token}:{})},...(body?{body:JSON.stringify(body)}:{})});return {status:r.status,data:await r.json()};};
  const began=performance.now(),a=await call('accounts',{username:'alice',password:'local-workerd-test-only'});evidence.registrationWallMs=Math.round(performance.now()-began);assert.equal(a.status,201,JSON.stringify(a.data));evidence.checks.push('scrypt registration in real workerd');
  const state=freshState(1791388800000,42),pack={raw:JSON.stringify(state),expectedRevision:0,uploadId:crypto.randomUUID(),epoch:'workerd-local'};
  assert.equal((await call('saves',pack,a.data.token)).status,201);
  assert.equal((await call('saves',pack,a.data.token)).status,200);evidence.checks.push('D1 immutable insert and lost-ACK retry');
  const race=await Promise.all([1,2].map(n=>call('saves',{...pack,raw:JSON.stringify({...state,cp:600+n}),expectedRevision:1,uploadId:crypto.randomUUID()},a.data.token)));assert.deepEqual(race.map(r=>r.status).sort(),[201,409]);evidence.checks.push('concurrent CAS only one winner');
  const b=await call('accounts',{username:'bob',password:'another-local-test-only'});assert.equal((await call('saves/current',null,b.data.token)).data.save,null);evidence.checks.push('user isolation');
  assert.equal((await call('saves/history',null,a.data.token)).data.items.length,2);evidence.checks.push('D1 history read');
  evidence.passed=true;console.log(JSON.stringify(evidence,null,2));
}catch(e){evidence.passed=false;evidence.error=e.message;throw e;}
finally{mkdirSync(new URL('../artifacts/cloud-implementation/',import.meta.url),{recursive:true});writeFileSync(new URL('../artifacts/cloud-implementation/workerd.json',import.meta.url),JSON.stringify(evidence,null,2));await mf.dispose();}
