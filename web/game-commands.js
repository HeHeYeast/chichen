import {normalizeSave} from './engine.js';
import {canonical,payloadHash,channelRandom} from './rng.js';
import {reconcileProgress} from './regulars.js';

export class CommandError extends Error{constructor(message,code){super(message);this.code=code;}}
// The application holds the only state; reducers receive an isolated draft.
export function execute({state,store,commandId=`cmd-${state.meta.commandSeq+1}`,expectedRevision=state.meta.revision,command,now,reduce,advance=()=>{},summary=''}){
  const hash=payloadHash(command),last=state.meta.lastCommit;
  if(last?.commandId===commandId){if(last.payloadHash!==hash)throw new CommandError('操作编号已用于另一项操作。','COMMAND_REUSE');return {state,result:last.summary,replayed:true};}
  const seq=Number(/^cmd-(\d+)$/.exec(commandId)?.[1]);
  if(!Number.isSafeInteger(seq)||seq<=state.meta.commandSeq)throw new CommandError('这项操作已经处理，请刷新进度。','COMMAND_PROCESSED');
  if(expectedRevision!==state.meta.revision||seq!==state.meta.commandSeq+1)throw new CommandError('进度已变化，请重新确认。','REVISION_CONFLICT');
  if(!Number.isSafeInteger(now)||now<0)throw Error('时间无效');
  store?.assertWritable?.(expectedRevision);
  const draft=structuredClone(state),at=Math.max(now,state.clock.logicalAt);
  advance(draft,at);
  const result=reduce(draft,{now:at,random:channelRandom(draft,commandId,'command'),commandId});
  // Collection results follow committed facts inside the same transaction.
  if(draft.expansion?.collections)reconcileProgress(draft);
  draft.clock={logicalAt:at,lastWallAt:now};draft.lastSeen=now;
  draft.meta.revision++;draft.meta.commandSeq++;
  draft.meta.lastCommit={commandId,payloadHash:hash,summary:String(summary||command.type||'进度已保存').slice(0,512)};
  const candidate=normalizeSave(draft,now);
  // Only a confirmed-unwritten failure lands here without a code; the caller keeps
  // the previous state. Ambiguous results already carry ACK_UNKNOWN from the store.
  try{store?.write(candidate,{expectedRevision});}
  catch(error){if(error.code)throw error;throw Object.assign(new CommandError(`进度暂未保存，本次操作未生效：${error.message}`,'SAVE_FAILED'),{cause:error});}
  return {state:candidate,result,replayed:false};
}

// Hold a real browser lock for the lifetime of the writable document.
// If unavailable, this build is read-only; localStorage check-then-set is no lock.
export async function acquireWriter({locks=globalThis.navigator?.locks,key,native=false,isolated=false}={}){
  if(native||isolated)return {writable:true,release(){}};
  if(!locks?.request)return {writable:false,reason:'此浏览器不支持安全的单窗口保存，请使用支持 Web Locks 的浏览器。',release(){}};
  let release;const held=new Promise(resolve=>release=resolve);
  return new Promise(resolve=>{locks.request('chick-kitchen/writer/'+key,{mode:'exclusive',ifAvailable:true},async lock=>{
    if(!lock){resolve({writable:false,reason:'另一个窗口正在游玩。请关闭该窗口后重新打开本页。',release(){}});return;}
    resolve({writable:true,release});await held;
  }).catch(error=>resolve({writable:false,reason:error.message,release(){}}));});
}
