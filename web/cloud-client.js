import {parseSave} from './engine.js';
export function createCloudAPI({endpoint,token=()=>null,fetch:send=globalThis.fetch}){
  const url=new URL(endpoint);if(url.protocol!=='https:'&&!(url.protocol==='http:'&&['localhost','127.0.0.1'].includes(url.hostname)))throw Error('云服务必须使用 HTTPS。');
  return async(path,{method='GET',body}={})=>{
    const response=await send(url.origin+'/v1/'+path,{method,headers:{'content-type':'application/json',...(token()?{authorization:'Bearer '+token()}:{})},...(body===undefined?{}:{body:JSON.stringify(body)}),signal:AbortSignal.timeout(15000),credentials:'omit',redirect:'error'});
    if(response.status===204)return null;
    let data;try{data=await response.json();}catch{throw Error('云服务未返回有效结果，本机进度仍保留。');}
    if(!response.ok)throw Object.assign(Error(data.error?.message??'云端请求失败。'),{code:data.error?.code,status:response.status});return data;
  };
}
async function hash(raw){return [...new Uint8Array(await crypto.subtle.digest('SHA-256',new TextEncoder().encode(raw)))].map(v=>v.toString(16).padStart(2,'0')).join('');}
export function createManualCloud({api,storage,key,getRaw,replaceRaw,epoch,now=()=>Date.now()}){
  const metaKey=key+'.cloud-v1';let busy=false,phase='ready';
  const read=()=>{const raw=storage.getItem(metaKey);if(raw===null)return {baseRevision:0,epoch};const m=JSON.parse(raw);if(!Number.isSafeInteger(m.baseRevision)||m.baseRevision<0||typeof m.epoch!=='string')throw Error('云备份记录损坏，请先导出本机进度。');return m;};
  const write=m=>storage.setItem(metaKey,JSON.stringify(m));
  async function run(work){if(busy)throw Error('正在处理备份，请稍候。');busy=true;try{return await work();}catch(e){phase=e.code==='REVISION_CONFLICT'?'conflict':e.status===401?'auth-required':e.status===426?'upgrade-required':'error';throw e;}finally{busy=false;}}
  return {
    get busy(){return busy;},get phase(){return phase;},get metadata(){return read();},
    backup:()=>run(async()=>{
      phase='uploading';const m=read();if(m.restorePending)throw Error('恢复记录尚未确认，请先重新查看并恢复云端，或导出本机备份。');if(m.epoch!==epoch)throw Error('备份服务已变更，请先查看云端进度。');
      if(!m.pending){const raw=getRaw();parseSave(raw);m.pending={raw,expectedRevision:m.baseRevision,epoch,uploadId:crypto.randomUUID()};write(m);}
      const result=await api('saves',{method:'POST',body:m.pending});
      if(result.epoch!==epoch||result.hash!==await hash(m.pending.raw)||result.revision!==m.pending.expectedRevision+1)throw Error('备份回执不匹配，已保留待确认记录。');
      const savedRaw=m.pending.raw;m.baseRevision=result.revision;m.lastHash=result.hash;m.backedAt=now();delete m.pending;write(m);
      phase=result.headRevision!==result.revision?'conflict':getRaw()===savedRaw?'backed-up':'dirty';return {phase,revision:result.revision};
    }),
    preview:revision=>run(async()=>{
      phase='checking';const localRaw=getRaw();const result=await api(revision?'saves/history/'+revision:'saves/current');
      if(result.epoch!==epoch)throw Error('云端环境已变化，请重新登录。');
      if(result.save){if(new TextEncoder().encode(result.save.raw).length>512*1024||await hash(result.save.raw)!==result.save.hash)throw Error('云端存档校验失败，本机进度未改动。');parseSave(result.save.raw);}
      phase='preview';return {localRaw,remote:result.save,epoch};
    }),
    restore:preview=>run(async()=>{
      phase='restoring';if(preview.epoch!==epoch||!preview.remote)throw Error('没有可恢复的云端备份。');
      if(getRaw()!==preview.localRaw)throw Error('预览后本机进度发生变化，请重新查看。');
      // Historical restoration is a local branch based on the current head, captured before apply.
      const current=await api('saves/current');
      if(current.epoch!==epoch||getRaw()!==preview.localRaw)throw Error('进度发生变化，请重新查看。');
      if(!current.save)throw Error('云端记录已变化，请重新查看。');
      if(preview.remote.revision===current.save.revision&&preview.remote.hash!==current.save.hash)throw Error('云端校验失败。');
      // Selecting a historical revision must not silently accept an unseen newer head.
      const baseline=preview.headRevision??preview.remote.revision;
      if(current.save.revision!==baseline)throw Error('云端又有新进度，请重新查看后选择。');
      storage.setItem(key+'.cloud-before-restore',preview.localRaw);
      write({...read(),pending:null,restorePending:true});
      replaceRaw(preview.remote.raw);
      write({baseRevision:current.save.revision,epoch,backedAt:preview.remote.createdAt,lastHash:preview.remote.hash});phase='restored';
    }),
    chooseLocal:preview=>run(async()=>{
      if(preview.epoch!==epoch||getRaw()!==preview.localRaw)throw Error('预览后本机进度发生变化，请重新查看。');
      const m=read();m.baseRevision=preview.remote?.revision??0;m.epoch=epoch;delete m.pending;write(m);phase='ready';
      // Caller then invokes backup; server CAS still rejects another device's concurrent write.
    }),
    history:options=>api('saves/history'+(options?.before?'?before='+encodeURIComponent(options.before):'')),
  };
}
