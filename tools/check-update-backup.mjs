import fs from 'node:fs';
import crypto from 'node:crypto';
import {pathToFileURL} from 'node:url';
import {parseBackup, MAX_SAVE_LENGTH} from '../web/save-store.js';
import {CURRENT_SAVE_VERSION} from '../web/save-migrations.js';

const canonical=value=>JSON.stringify(sort(value));
function sort(value){
  if(Array.isArray(value))return value.map(sort);
  if(value&&typeof value==='object')return Object.fromEntries(Object.keys(value).sort().map(key=>[key,sort(value[key])]));
  return value;
}
const hash=value=>crypto.createHash('sha256').update(value).digest('hex');
export function checkBackup(raw){
  if(Buffer.byteLength(raw)>MAX_SAVE_LENGTH)throw Error('备份超过允许大小');
  const data=JSON.parse(raw);
  if(data.format!=='chick-kitchen'||data.formatVersion!==1||!/^\d+\.\d+\.\d+$/.test(data.gameVersion??''))throw Error('不是游戏导出的备份');
  const exportedAt=Date.parse(data.exportedAt);
  if(!Number.isFinite(exportedAt))throw Error('备份没有有效导出时间');
  const state=parseBackup(raw,exportedAt);
  if(!Number.isInteger(data.save?.version)||data.save.version<2||data.save.version>CURRENT_SAVE_VERSION)throw Error(`更新备份必须是版本2至${CURRENT_SAVE_VERSION}存档`);
  const protocol=data.updateBackup;
  if(protocol&&(protocol.protocol!==1||protocol.applicationId!=='com.jibao.kitchen'||!Number.isSafeInteger(protocol.versionCode)||! /^[a-f0-9]{64}$/.test(protocol.sourceSha256)))throw Error('自动备份协议无效');
  // SAF export resumes the game once the document picker closes, updating only
  // this timestamp. Everything else must still match during the one-time bootstrap.
  const comparable=structuredClone(data.save);delete comparable.lastSeen;
  return {gameVersion:data.gameVersion,exportedAt,protocol:protocol?.protocol??0,versionCode:protocol?.versionCode??null,
    fileSha256:hash(raw),stateSha256:hash(canonical(data.save)),bootstrapSha256:hash(canonical(comparable)),sourceSha256:protocol?.sourceSha256??null,
    summary:{cp:state.cp,kitchenLevel:state.kitchenLevel+1,discovered:Object.values(state.total).filter(n=>n>0).length,
      farmCount:Object.values(state.farm).reduce((a,b)=>a+b,0),remaining:state.batch?.eggs.filter(egg=>!egg.collected).length??0}};
}
if(process.argv[1]&&import.meta.url===pathToFileURL(process.argv[1]).href){
  try{console.log(JSON.stringify(checkBackup(fs.readFileSync(process.argv[2],'utf8'))));}
  catch(error){console.error(error.message);process.exitCode=1;}
}
