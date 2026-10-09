import {createHash,randomBytes,randomUUID} from 'node:crypto';
import {hashPassword,verifyPassword} from './password.mjs';
import {normalizeSave} from '../web/engine.js';
import {CURRENT_SAVE_VERSION} from '../web/save-migrations.js';
const digest=value=>createHash('sha256').update(value).digest('hex');
const uuid=value=>typeof value==='string'&&/^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i.test(value);
const fail=(status,code,message)=>{throw Object.assign(Error(message),{status,code});};
const MAX_BODY=600*1024,MAX_SAVE=512*1024;
export async function maintenance(db,now=Date.now()){
  await db.prepare('DELETE FROM rate_limits WHERE bucket<?').bind(now-86400000).run();
  await db.prepare('DELETE FROM sessions WHERE expires_at<=?').bind(now).run();
  await db.prepare('DELETE FROM feedback WHERE created_at<?').bind(now-90*86400000).run();
}
export function createService({db,allowedUsernames=[],origin,epoch,now=()=>Date.now()}){
  if(!epoch||!origin)throw Error('Explicit origin and cloud epoch required');
  const stmt=(sql,...args)=>db.prepare(sql).bind(...args);
  const head=uid=>stmt('SELECT * FROM saves WHERE uid=? ORDER BY revision DESC LIMIT 1',uid).first();
  const publicSave=s=>s?{revision:s.revision,raw:s.raw,hash:s.hash,schemaVersion:s.schema_version,createdAt:s.created_at,uploadId:s.upload_id,epoch}:null;
  async function limit(key,max,window){
    const bucket=(Math.floor(now()/window)+1)*window,row=await stmt('INSERT INTO rate_limits(key,bucket,count) VALUES(?,?,1) ON CONFLICT(key) DO UPDATE SET count=CASE WHEN bucket=excluded.bucket THEN count+1 ELSE 1 END,bucket=excluded.bucket RETURNING count',digest(key),bucket).first();
    if(row.count>max)throw Object.assign(Error('操作较频繁，请稍后再试。'),{status:429,code:'RATE_LIMITED',retryAfter:Math.max(1,Math.ceil((bucket-now())/1000))});
  }
  async function body(request){
    if(!request.headers.get('content-type')?.startsWith('application/json'))fail(415,'JSON_REQUIRED','请使用 JSON 请求。');
    const reader=request.body?.getReader();if(!reader)fail(400,'INVALID_JSON','缺少请求内容。');
    let bytes=0,chunks=[];for(;;){const {value,done}=await reader.read();if(done)break;bytes+=value.byteLength;if(bytes>MAX_BODY){await reader.cancel();fail(413,'TOO_LARGE','数据过大，本机存档未改动。');}chunks.push(value);}
    try{const b=JSON.parse(Buffer.concat(chunks).toString('utf8'));if(!b||typeof b!=='object'||Array.isArray(b))throw Error();return b;}catch{fail(400,'INVALID_JSON','无法读取请求。');}
  }
  async function authenticate(request){
    const token=request.headers.get('authorization')?.match(/^Bearer ([A-Za-z0-9_-]{43})$/)?.[1];if(!token)fail(401,'AUTH_REQUIRED','请登录后操作。');
    const row=await stmt('SELECT users.uid,users.username,sessions.token_hash FROM sessions JOIN users ON users.uid=sessions.uid WHERE token_hash=? AND expires_at>?',digest(token),now()).first();
    if(!row)fail(401,'AUTH_REQUIRED','登录已失效，请重新登录。');return row;
  }
  async function session(user){
    const token=randomBytes(32).toString('base64url'),expiresAt=now()+24*3600000;
    await stmt('DELETE FROM sessions WHERE expires_at<=?',now()).run();
    await stmt('INSERT INTO sessions(token_hash,uid,expires_at) VALUES(?,?,?)',digest(token),user.uid,expiresAt).run();
    await stmt('DELETE FROM sessions WHERE uid=? AND token_hash NOT IN (SELECT token_hash FROM sessions WHERE uid=? ORDER BY expires_at DESC LIMIT 5)',user.uid,user.uid).run();
    return {uid:user.uid,username:user.username,token,expiresAt,epoch};
  }
  return async function handle(request,{peer='local'}={}){
    const requestId=randomUUID(),headers={'content-type':'application/json; charset=utf-8','cache-control':'no-store','x-content-type-options':'nosniff'};
    const requestOrigin=request.headers.get('origin');
    if(requestOrigin===origin){headers['access-control-allow-origin']=origin;headers.vary='Origin';}
    const reply=(value,status=200)=>new Response(JSON.stringify(value),{status,headers});
    try{
      if(requestOrigin&&requestOrigin!==origin)fail(403,'ORIGIN_DENIED','此来源不允许访问。');
      if(request.method==='OPTIONS')return new Response(null,{status:204,headers:{...headers,'access-control-allow-methods':'GET,POST,DELETE,OPTIONS','access-control-allow-headers':'Content-Type,Authorization'}});
      const url=new URL(request.url),path=url.pathname,method=request.method;
      if(path==='/v1/health'&&method==='GET')return reply({ok:true,epoch,schemaVersion:CURRENT_SAVE_VERSION});
      // Global cap is checked before attacker-selected keys reach storage or password hashing.
      await limit('global-day',3000,86400000);await limit('global',600,60000);await limit('peer:'+peer,100,60000);
      if((path==='/v1/accounts'||path==='/v1/sessions')&&method==='POST'){
        await limit('auth:'+peer,8,600000);const b=await body(request);
        if(typeof b.username!=='string'||!/^[a-z0-9_]{3,24}$/i.test(b.username)||typeof b.password!=='string'||b.password.length<10||b.password.length>128)fail(422,'INVALID_CREDENTIALS','用户名需 3–24 位字母数字或下划线，密码需 10–128 字符。');
        const username=b.username.toLowerCase();await limit('login:'+username,8,600000);
        let user=await stmt('SELECT * FROM users WHERE username=?',username).first();
        if(path==='/v1/accounts'){
          if(!allowedUsernames.includes(username))fail(403,'REGISTRATION_CLOSED','目前仅向已登记的测试玩家开放注册。');
          if(user)fail(409,'USERNAME_TAKEN','此用户名已注册。');
          const passwordHash=await hashPassword(b.password),uid=randomUUID();
          const result=await stmt('INSERT OR IGNORE INTO users(uid,username,password_hash,created_at) VALUES(?,?,?,?)',uid,username,passwordHash,now()).run();
          if(!result.meta.changes)fail(409,'USERNAME_TAKEN','此用户名已注册。');
          user={uid,username};return reply(await session(user),201);
        }
        if(!user||!await verifyPassword(b.password,user.password_hash))fail(401,'INVALID_LOGIN','用户名或密码不正确。');
        return reply(await session(user));
      }
      if(path==='/v1/feedback'&&method==='POST'){
        const b=await body(request);
        if(!uuid(b.id)||typeof b.text!=='string'||b.text.trim().length<20||b.text.length>2000)fail(422,'INVALID_FEEDBACK','请填写 20–2000 字反馈。');
        const owner=digest(b.id),old=await stmt('SELECT owner,body FROM feedback WHERE id=?',b.id).first();
        if(old){if(old.owner!==owner||old.body!==b.text)fail(409,'ID_REUSED','提交编号已被使用。');return reply({id:b.id,received:true});}
        await limit('feedback-day',50,86400000);await limit('feedback:'+peer,3,86400000);
        await stmt('DELETE FROM feedback WHERE created_at<?',now()-90*86400000).run();
        const inserted=await stmt('INSERT OR IGNORE INTO feedback(id,owner,body,created_at) VALUES(?,?,?,?)',b.id,owner,b.text,now()).run();
        if(!inserted.meta.changes){const received=await stmt('SELECT body FROM feedback WHERE id=?',b.id).first();if(received?.body!==b.text)fail(409,'ID_REUSED','提交编号已被使用。');}
        return reply({id:b.id,received:true},inserted.meta.changes?201:200);
      }
      const user=await authenticate(request);await limit('user:'+user.uid,60,60000);
      if(path==='/v1/sessions/current'&&method==='DELETE'){await stmt('DELETE FROM sessions WHERE token_hash=?',user.token_hash).run();return new Response(null,{status:204,headers});}
      if(path==='/v1/account'&&method==='DELETE'){
        const b=await body(request),stored=await stmt('SELECT password_hash FROM users WHERE uid=?',user.uid).first();
        if(typeof b.password!=='string'||b.password.length>128||!await verifyPassword(b.password,stored.password_hash))fail(401,'INVALID_LOGIN','密码不正确。');
        await stmt('DELETE FROM users WHERE uid=?',user.uid).run();return new Response(null,{status:204,headers});
      }
      if(path==='/v1/saves/current'&&method==='GET')return reply({save:publicSave(await head(user.uid)),epoch});
      if(path==='/v1/saves/history'&&method==='GET'){
        const cursor=Number(url.searchParams.get('before')??Number.MAX_SAFE_INTEGER),size=Number(url.searchParams.get('limit')??10);
        if(!Number.isSafeInteger(cursor)||cursor<1||!Number.isInteger(size)||size<1||size>20)fail(422,'INVALID_PAGE','分页参数无效。');
        const {results}=await stmt('SELECT revision,hash,schema_version,created_at FROM saves WHERE uid=? AND revision<? ORDER BY revision DESC LIMIT ?',user.uid,cursor,size+1).all();
        return reply({items:results.slice(0,size),nextCursor:results.length>size?String(results[size-1].revision):null,epoch});
      }
      const historical=path.match(/^\/v1\/saves\/history\/(\d+)$/);
      if(historical&&method==='GET'){const s=await stmt('SELECT * FROM saves WHERE uid=? AND revision=?',user.uid,Number(historical[1])).first();if(!s)fail(404,'SAVE_NOT_FOUND','该历史记录不存在。');return reply({save:publicSave(s),epoch});}
      if(path==='/v1/saves'&&method==='POST'){
        const b=await body(request);
        if(b.epoch!==epoch)fail(409,'EPOCH_CHANGED','备份服务已变更，请重新检查云端。');
        if(!uuid(b.uploadId)||!Number.isSafeInteger(b.expectedRevision)||b.expectedRevision<0||typeof b.raw!=='string')fail(422,'INVALID_SAVE','存档请求格式无效。');
        if(Buffer.byteLength(b.raw,'utf8')>MAX_SAVE)fail(413,'TOO_LARGE','云备份暂支持 512KiB，本机进度仍保留。');
        let state;try{state=JSON.parse(b.raw);normalizeSave(state,now());}catch(e){fail(e.code==='UNSUPPORTED_SAVE_VERSION'?426:422,e.code==='UNSUPPORTED_SAVE_VERSION'?'UPGRADE_REQUIRED':'INVALID_SAVE','存档不兼容或校验失败，本机进度仍保留。');}
        const hash=digest(b.raw),old=await stmt('SELECT * FROM saves WHERE uid=? AND upload_id=?',user.uid,b.uploadId).first();
        if(old){if(old.hash!==hash)fail(409,'ID_REUSED','同一提交编号不能对应两份进度。');return reply({revision:old.revision,hash,headRevision:(await head(user.uid)).revision,epoch});}
        const current=await head(user.uid);
        if(current?.schema_version>CURRENT_SAVE_VERSION)fail(426,'UPGRADE_REQUIRED','云端进度来自更新版本。');
        // The MAX comparison and unique key are evaluated by SQLite in one write statement.
        const result=await stmt('INSERT INTO saves(uid,revision,upload_id,raw,hash,schema_version,created_at) SELECT ?,?,?,?,?,?,? WHERE COALESCE((SELECT MAX(revision) FROM saves WHERE uid=?),0)=?',user.uid,b.expectedRevision+1,b.uploadId,b.raw,hash,state.version,now(),user.uid,b.expectedRevision).run();
        if(!result.meta.changes){const retried=await stmt('SELECT revision,hash FROM saves WHERE uid=? AND upload_id=?',user.uid,b.uploadId).first();if(retried?.hash===hash)return reply({revision:retried.revision,hash,headRevision:(await head(user.uid)).revision,epoch});fail(409,'REVISION_CONFLICT','云端已有其他进度，请先查看两份进度。');}
        return reply({revision:b.expectedRevision+1,headRevision:b.expectedRevision+1,hash,epoch},201);
      }
      fail(404,'NOT_FOUND','接口不存在。');
    }catch(e){if(e.status===429)headers['retry-after']=String(e.retryAfter);return reply({error:{code:e.code??'INTERNAL_ERROR',message:e.status?e.message:'服务暂时不可用，本机进度不受影响。',requestId}},e.status??500);}
  };
}
