import {freshState,parseSave,normalizeSave,SaveValidationError} from './engine.js';

export const MAX_SAVE_LENGTH=2*1024*1024;
export const utf8Length=text=>new TextEncoder().encode(text).length;
export function parseBackup(text,now=Date.now()){
  if(typeof text!=='string'||utf8Length(text)>MAX_SAVE_LENGTH)throw Error('文件太大或不是有效的存档文本。');
  let value;try{value=JSON.parse(text);}catch{throw Error('无法读取这个文件，请选择鸡宝厨房导出的 JSON 备份。');}
  if(value?.format){
    if(value.format!=='chick-kitchen')throw Error('这不是鸡宝厨房的备份。');
    if(value.formatVersion!==1)throw new SaveValidationError('备份版本较新，请更新游戏后导入。','UNSUPPORTED_SAVE_VERSION');
    value=value.save;
  }
  return normalizeSave(value,now);
}
export function makeBackup(state,now=Date.now(),version='1.4.8'){
  return JSON.stringify({format:'chick-kitchen',formatVersion:1,exportedAt:new Date(now).toISOString(),gameVersion:version,save:normalizeSave(state,now)},null,2);
}

/** The current write is the commit point; ambiguous acknowledgements are read back. */
export function createSaveStore({storage,key,native=null,now=()=>Date.now(),writer={writable:true}}){
  let locked=false,lastGood=null,observedRaw=null,upgradeRaw=null,rejectedRaw=null;
  const error=(message,code)=>Object.assign(Error(message),{code});
  function readRaw(){
    if(!native)return storage.getItem(key);
    const result=JSON.parse(native.loadSave());
    if(result.status==='empty')return null;
    if(!['ok','recovered'].includes(result.status))throw Error(result.message||'无法确认持久存档。');
    return result.raw;
  }
  function load(){
    upgradeRaw=null;rejectedRaw=null;
    try{
      let raw,previous=null,notice='';
      if(native){const result=JSON.parse(native.loadSave());if(result.status==='error')throw Error(result.message);if(result.status==='empty')raw=null;else if(['ok','recovered'].includes(result.status)){raw=result.raw;notice=result.message??'';}else throw Error('应用未返回有效存档，已停止自动保存。');}
      else{raw=storage.getItem(key);previous=storage.getItem(key+'.previous');}
      observedRaw=raw;
      if(raw===null&&previous===null){locked=false;lastGood=null;return {state:freshState(now())};}
      let state,source=raw;
      try{state=parseSave(raw,now());}catch(cause){if(cause.code==='UNSUPPORTED_SAVE_VERSION'||previous===null)throw cause;source=previous;state=parseSave(source,now());rejectedRaw=raw;notice='主存档无法读取，已恢复上一份自动备份。原始内容会保留，请导出备份留存。';}
      upgradeRaw=JSON.parse(source).version<state.version?source:null;
      lastGood=JSON.stringify(state);locked=false;return {state,notice};
    }catch(cause){locked=true;return {state:null,error:cause.message};}
  }
  function assertWritable(expectedRevision){
    if(!writer.writable)throw error(writer.reason||'当前窗口为只读。','READ_ONLY');
    if(locked)throw error('存档尚未恢复，自动保存已暂停。','SAVE_LOCKED');
    const current=readRaw();
    if(current!==observedRaw){locked=true;throw error('另一处已保存新进度，请重新读取后操作。','REVISION_CONFLICT');}
    if(expectedRevision!==undefined&&lastGood&&JSON.parse(lastGood).meta.revision!==expectedRevision)throw error('进度已变化，请重新确认。','REVISION_CONFLICT');
  }
  function write(state,{importing=false,expectedRevision}={}){
    if(!writer.writable)throw error(writer.reason||'当前窗口为只读。','READ_ONLY');
    if(!importing)assertWritable(expectedRevision);
    const normalized=normalizeSave(state,now()),raw=JSON.stringify(normalized);
    if(utf8Length(raw)>MAX_SAVE_LENGTH)throw Error('存档超过2MiB，未写入。');
    const before=readRaw();
    if(native){
      try{
        const result=JSON.parse(importing?native.importGame(raw):expectedRevision!==undefined&&native.commitGame?native.commitGame(raw,expectedRevision):native.saveGame(raw));
        if(!result.ok)throw Error(result.message||'应用无法保存，请保留游戏并导出备份。');
      }catch(cause){
        let persisted;try{persisted=readRaw();}catch{locked=true;throw error('保存结果尚不能确认，已暂停后续操作。请重新读取存档。','ACK_UNKNOWN');}
        if(persisted!==raw){if(persisted!==before){locked=true;throw error('保存结果发生冲突，已暂停后续操作。','ACK_UNKNOWN');}throw cause;}
      }
    }else{
      if(importing&&before!==null)storage.setItem(key+'.pre-import',before);
      // Preserve the rejected bytes before committing a recovered generation.
      // If this safety copy fails, keep the original primary untouched.
      if(rejectedRaw!==null)storage.setItem(key+'.unreadable',rejectedRaw);
      if(upgradeRaw!==null){const backupKey=key+'.pre-upgrade-v'+JSON.parse(upgradeRaw).version;if(storage.getItem(backupKey)===null)storage.setItem(backupKey,upgradeRaw);}
      if(lastGood&&lastGood!==raw)storage.setItem(key+'.previous',lastGood);
      try{storage.setItem(key,raw);}catch(cause){
        let persisted;try{persisted=storage.getItem(key);}catch{locked=true;throw error('保存结果尚不能确认，已暂停后续操作。','ACK_UNKNOWN');}
        if(persisted!==raw)throw cause;
      }
    }
    lastGood=raw;observedRaw=raw;upgradeRaw=null;rejectedRaw=null;locked=false;return true;
  }
  return {load,write,assertWritable,get locked(){return locked;}};
}
