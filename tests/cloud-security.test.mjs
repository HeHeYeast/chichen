import test from 'node:test';
import assert from 'node:assert/strict';
import {createLocalDatabase} from '../cloud/local-database.mjs';
import {createService,maintenance} from '../cloud/service.mjs';
import {freshState} from '../web/engine.js';
const start=1791388800000;
function setup(){
  const db=createLocalDatabase(':memory:');let now=start;
  const handler=createService({db,allowedUsernames:['alice','bob'],origin:'https://game.example',epoch:'test',now:()=>now});
  const call=async(path,{body,token,method=body?'POST':'GET',origin,peer='test'}={})=>{
    const r=await handler(new Request('https://api.example/v1/'+path,{method,headers:{'content-type':'application/json',...(origin?{origin}:{}),...(token?{authorization:'Bearer '+token}:{})},...(body?{body:JSON.stringify(body)}:{})}),{peer});
    return {status:r.status,headers:r.headers,data:r.status===204?null:await r.json()};
  };
  return {db,call,advance:ms=>now+=ms};
}
test('sessions are hashed, expire and revoke; account deletion isolates and cascades',async()=>{
  const t=setup();try{
    const credentials={username:'alice',password:'local-security-test-password'};
    const a=(await t.call('accounts',{body:credentials})).data;
    const stored=await t.db.prepare('SELECT password_hash FROM users WHERE uid=?').bind(a.uid).first();
    assert.match(stored.password_hash,/^scrypt:/);assert.ok(!stored.password_hash.includes(credentials.password));
    const token=await t.db.prepare('SELECT token_hash FROM sessions WHERE uid=?').bind(a.uid).first();assert.notEqual(token.token_hash,a.token);
    const pack={raw:JSON.stringify(freshState(start,42)),epoch:'test',uploadId:crypto.randomUUID(),expectedRevision:0};
    assert.equal((await t.call('saves',{body:pack,token:a.token})).status,201);
    const b=(await t.call('accounts',{body:{username:'bob',password:'another-test-password'}})).data;
    assert.equal((await t.call('saves/current?uid='+a.uid,{token:b.token})).data.save,null);
    assert.equal((await t.call('account',{method:'DELETE',body:{password:'incorrect-password'},token:a.token})).status,401);
    t.advance(24*3600000+1);assert.equal((await t.call('saves/current',{token:a.token})).status,401);
    const renewed=(await t.call('sessions',{body:credentials})).data;
    assert.equal((await t.call('account',{method:'DELETE',body:{password:credentials.password},token:renewed.token})).status,204);
    assert.equal((await t.db.prepare('SELECT count(*) AS n FROM saves WHERE uid=?').bind(a.uid).first()).n,0);
    assert.equal((await t.call('saves/current',{token:renewed.token})).status,401);
    assert.ok(await t.db.prepare('SELECT uid FROM users WHERE uid=?').bind(b.uid).first());
    const logged=(await t.call('sessions',{body:{username:'bob',password:'another-test-password'}})).data;
    assert.equal((await t.call('sessions/current',{method:'DELETE',token:logged.token})).status,204);
    assert.equal((await t.call('saves/current',{token:logged.token})).status,401);
  }finally{t.db.close();}
});
test('closed registration, origin restrictions, body limits, feedback retry and retention',async()=>{
  const t=setup();try{
    assert.equal((await t.call('accounts',{body:{username:'mallory',password:'valid-test-password'}})).status,403);
    assert.equal((await t.call('health',{origin:'https://evil.example'})).status,403);
    assert.equal((await t.call('health',{origin:'https://game.example'})).headers.get('access-control-allow-origin'),'https://game.example');
    const a=(await t.call('accounts',{body:{username:'alice',password:'valid-test-password'}})).data;
    assert.equal((await t.call('saves',{token:a.token,body:{raw:'x'.repeat(601*1024)}})).status,413);
    const feedback={id:crypto.randomUUID(),text:'测试反馈：第二台设备恢复后，所有原有伙伴和材料都应该保留。'};
    assert.equal((await t.call('feedback',{body:feedback})).status,201);
    assert.equal((await t.call('feedback',{body:feedback,peer:'different-network'})).status,200);
    assert.equal((await t.call('feedback',{body:{...feedback,text:feedback.text+'不同内容'}})).status,409);
    for(let i=0;i<2;i++)assert.equal((await t.call('feedback',{body:{...feedback,id:crypto.randomUUID()}})).status,201);
    const blocked=await t.call('feedback',{body:{...feedback,id:crypto.randomUUID()}});assert.equal(blocked.status,429);assert.ok(Number(blocked.headers.get('retry-after'))>0);
    t.advance(91*86400000);await maintenance(t.db,start+91*86400000);
    for(const table of ['feedback','sessions','rate_limits'])assert.equal((await t.db.prepare('SELECT count(*) AS n FROM '+table).first()).n,0);
  }finally{t.db.close();}
});
