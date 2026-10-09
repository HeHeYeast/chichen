import {parseSave} from './engine.js';
/** The old guest key is never renamed or erased. Prepare a profile before publishing its pointer. */
export function createProfileVault({storage,key}){
  const pointer=key+'.active-profile';
  const read=()=>{const raw=storage.getItem(pointer);if(raw===null)return null;const value=JSON.parse(raw);if(value===null)return null;if(!value||typeof value.uid!=='string'||!/^[a-zA-Z0-9_-]{1,80}$/.test(value.uid)||typeof value.username!=='string')throw Error('账号索引损坏，已停止自动切换。');return value;};
  const profileKey=uid=>key+'.account.'+uid;
  return {
    get active(){return read();},get saveKey(){const active=read();return active?profileKey(active.uid):key;},
    activate(account,{copyGuest=false}={}){
      if(!account||!/^[a-zA-Z0-9_-]{1,80}$/.test(account.uid)||typeof account.username!=='string')throw Error('账号标识无效。');
      const target=profileKey(account.uid);
      if(copyGuest&&storage.getItem(target)===null){
        const raw=storage.getItem(key);if(raw!==null){parseSave(raw);storage.setItem(target,raw);if(storage.getItem(target)!==raw)throw Error('账号进度复制尚未确认。');}
      }
      storage.setItem(pointer,JSON.stringify({uid:account.uid,username:account.username}));
    },
    logout(){storage.removeItem?storage.removeItem(pointer):storage.setItem(pointer,'null');},
  };
}
