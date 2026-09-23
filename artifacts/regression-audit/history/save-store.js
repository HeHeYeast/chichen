import {freshState,parseSave,normalizeSave,SaveValidationError} from './engine.js';

export const MAX_SAVE_LENGTH=2*1024*1024;
export function parseBackup(text,now=Date.now()){
  if(typeof text!=='string'||text.length>MAX_SAVE_LENGTH)throw Error('文件太大或不是有效的存档文本。');
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

/** Strict reads + one known-good generation. A failed load locks automatic writes. */
export function createSaveStore({storage,key,native=null,now=()=>Date.now()}){
  let locked=false,lastGood=null;
  function load(){
    try{
      if(native){
        const result=JSON.parse(native.loadSave());
        if(result.status==='error')throw Error(result.message);
        if(result.status==='empty'){locked=false;return {state:freshState(now())};}
        if(!['ok','recovered'].includes(result.status))throw Error('应用未返回有效存档，已停止自动保存。');
        const state=parseSave(result.raw,now());lastGood=JSON.stringify(state);locked=false;
        return {state,notice:result.message??''};
      }
      const raw=storage.getItem(key),previous=storage.getItem(key+'.previous');
      if(raw===null&&previous===null){locked=false;return {state:freshState(now())};}
      let state,notice='';
      try{state=parseSave(raw,now());}
      catch(error){
        if(error.code==='UNSUPPORTED_SAVE_VERSION'||previous===null)throw error;
        state=parseSave(previous,now());notice='主存档无法读取，已恢复上一份自动备份。请导出备份留存。';
      }
      lastGood=JSON.stringify(state);locked=false;return {state,notice};
    }catch(error){locked=true;return {state:null,error:error.message};}
  }
  function write(state,{importing=false}={}){
    if(locked&&!importing)throw Error('存档尚未恢复，自动保存已暂停。');
    const raw=JSON.stringify(normalizeSave(state,now()));
    if(native){
      const result=JSON.parse(importing?native.importGame(raw):native.saveGame(raw));
      if(!result.ok)throw Error(result.message||'应用无法保存，请保留游戏并导出备份。');
    }else{
      if(importing){
        const existing=storage.getItem(key);
        if(existing!==null)storage.setItem(key+'.pre-import',existing);
      }
      if(lastGood&&lastGood!==raw)storage.setItem(key+'.previous',lastGood);
      storage.setItem(key,raw);
    }
    lastGood=raw;locked=false;return true;
  }
  return {load,write,get locked(){return locked;}};
}
