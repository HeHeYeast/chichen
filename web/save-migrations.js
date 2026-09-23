import {canonical,hash32,RNG_ALGORITHM} from './rng.js';
import {freshBusiness} from './business.js';
import {freshFacts} from './facts.js';
import {freshOrders} from './orders.js';
import {validateBusinessState} from './business-save.js';
import {freshCollections,validateCollectionsState} from './collection-progress.js';
import {validateRegularsState} from './regulars.js';
import {validateProjectsState} from './projects.js';
// Regular records (Work I), project stages/deliveries/payments and menu presets (Work J).
function validateLaterContainers(s,fail){
  validateRegularsState(s,fail);
  validateProjectsState(s,fail);
}
export const CURRENT_SAVE_VERSION=6;
export const CONTENT_REVISION='regional-1';
export function migrate3to4(source,{seed}={}){
  const s=structuredClone(source);
  s.version=4;s.contentRevision=CONTENT_REVISION;
  s.meta={revision:0,commandSeq:0,factSeq:0,lastCommit:null,rng:{algorithm:RNG_ALGORITHM,seed:seed??hash32('chick-kitchen/migration/3-4/'+canonical(source))},migrationHistory:[{from:3,to:4}]};
  s.clock={logicalAt:Math.max(s.progress.logicalAt,s.lastSeen),lastWallAt:s.lastSeen};
  s.expansion={regions:{opened:[],introSpecimenDone:[],guideFlags:[]},discovery:{cards:{},identified:{}},methods:{directions:[],full:[],freeProgress:{}},trial:{},cardProtection:{}};
  return s;
}
export function migrate4to5(source){
  const s=structuredClone(source);s.version=5;s.meta.migrationHistory.push({from:4,to:5});
  Object.assign(s.expansion,{business:freshBusiness(),facts:freshFacts(),orders:freshOrders(),inventoryPolicy:{keepOne:true,collectionLocks:[],optionalOrderReservations:{}}});
  // B's actual companion/card records remain in regions.history. Migration does
  // not replay events or fabricate business/practice history from collection.
  return s;
}
// 5→6 adds empty containers only. Historical collection pages are registered by
// the separate idempotent reconciliation after migration, never inside it.
export function migrate5to6(source){
  const s=structuredClone(source);s.version=6;s.meta.migrationHistory.push({from:5,to:6});
  Object.assign(s.expansion,{collections:freshCollections(),regulars:{},projects:{},menus:{presets:[]}});
  return s;
}
// A validator is injected to avoid a dependency cycle with the legacy engine.
// Each transition validates both sides; no time, randomness or rewards here.
export function migrateSave(source,validate,now){
  let s=structuredClone(source);
  validate(s,now);
  if(s.version===1){s={...s,version:2,toolLevels:[...s.toolLevels,-1]};validate(s,now);}
  if(s.version===2){s=validate(s,now);s.version=3;validate(s,now);}
  if(s.version===3){s=validate(s,now);s=migrate3to4(s);validate(s,now);}
  if(s.version===4){s=validate(s,now);s=migrate4to5(s);validate(s,now);}
  if(s.version===5){s=validate(s,now);s=migrate5to6(s);validate(s,now);}
  return validate(s,now);
}
export function validateExpansionSave(s,fail){
  const obj=(v,p)=>{if(!v||typeof v!=='object'||Array.isArray(v))fail(p);return v;};
  const int=(v,p,max=Number.MAX_SAFE_INTEGER)=>{if(!Number.isSafeInteger(v)||v<0||v>max)fail(p);};
  const list=(v,p,allowed)=>{if(!Array.isArray(v)||new Set(v).size!==v.length||v.some(x=>!allowed(x)))fail(p);};
  if(s.contentRevision!==CONTENT_REVISION)fail('内容版本');
  const m=obj(s.meta,'事务元数据');for(const k of ['revision','commandSeq','factSeq'])int(m[k],k);
  if(m.commandSeq!==m.revision)fail('事务修订序号');
  const r=obj(m.rng,'随机根');if(r.algorithm!==RNG_ALGORITHM)fail('随机算法');int(r.seed,'随机种子',0xffffffff);
  if(!Array.isArray(m.migrationHistory)||m.migrationHistory.length!==s.version-3||m.migrationHistory.some((entry,i)=>entry?.from!==i+3||entry?.to!==i+4))fail('迁移记录');
  if(m.lastCommit!==null){const c=obj(m.lastCommit,'最后提交');if(typeof c.commandId!=='string'||c.commandId!==`cmd-${m.commandSeq}`||typeof c.payloadHash!=='string'||!/^[a-f0-9]{8}$/.test(c.payloadHash)||typeof c.summary!=='string'||c.summary.length>512)fail('最后提交');}
  else if(m.commandSeq!==0)fail('缺失提交回执');
  const clock=obj(s.clock,'时钟');int(clock.logicalAt,'逻辑时间');int(clock.lastWallAt,'墙钟时间');if(clock.logicalAt<clock.lastWallAt)fail('时钟顺序');
  const e=obj(s.expansion,'扩展');const regions=obj(e.regions,'地区');
  const region=x=>['V','R','T','B'].includes(x),recipe=x=>/^(REC-[VRTB]-[CD][1-6]|ALT-[VRTB])$/.test(x);
  list(regions.opened,'开放地区',region);list(regions.introSpecimenDone,'首标本地区',region);list(regions.guideFlags,'引路',x=>x==='GUIDE-B');
  if(regions.materialUse!==undefined)for(const [k,v]of Object.entries(obj(regions.materialUse,'材料使用'))){if(!/^(7[5-9]|8[0-2])$/.test(k)||v!==true)fail('材料使用事实');}
  const species=k=>{const m=/^([01]):(0|[1-9]\d*)$/.exec(k);return !!m&&Number(m[2])<(m[1]==='0'?152:89);};
  if(regions.history!==undefined){
    const history=obj(regions.history,'同行历史');for(const [k,v]of Object.entries(obj(history.companionFirst,'首次同行'))){if(!species(k))fail('同行身份');int(v,'同行序号');}
    for(const [k,v]of Object.entries(obj(history.trips,'完整寻访'))){if(!region(k))fail('寻访地区');obj(v,'寻访见证');int(v.count,'寻访次数');list(v.lastMembers,'同行成员',species);if(v.lastMembers.length>3||typeof v.regionalWithLegacy!=='boolean'||typeof v.twoSeasonChapters!=='boolean')fail('同行见证');}
    const companion=(v,snapshot=false)=>{obj(v,'同行快照');const g=snapshot?v.G:v.gather,f=snapshot?v.F:v.discover;int(g,'采集',6);int(f,'发现',6);if(g+f!==6||!['yard','water','wood'].includes(v.environment))fail('同行能力');list(v.traits,'同行特征',x=>['portable','fruit','tea','leaf','grain','salt','floral'].includes(x));};
    if(history.companionFacts!==undefined)for(const [k,v]of Object.entries(obj(history.companionFacts,'首次同行快照'))){if(!species(k)||!region(v.region)||typeof v.tripId!=='string')fail('同行来源');int(v.seq,'同行事实序号');if(v.seq>m.factSeq)fail('同行未来序号');companion(v);}
    if(history.cardFacts!==undefined)for(const [k,v]of Object.entries(obj(history.cardFacts,'卡片同行快照'))){if(!/^[VRTB]-[SNE][12]$/.test(k)||typeof v.tripId!=='string'||!Array.isArray(v.members)||v.members.length<1||v.members.length>3)fail('卡片来源');int(v.seq,'卡片事实序号');if(v.seq>m.factSeq)fail('卡片未来序号');list(v.members.map(x=>x.key),'卡片队员',species);for(const member of v.members)companion(member,true);}
  }
  if(e.prepareMode!==undefined){obj(e.prepareMode,'准备模式');if(Object.keys(e.prepareMode).length!==2||!(e.prepareMode.kind==='regional'&&/^REC-[VRTB]-[CD][1-6]$/.test(e.prepareMode.recipeId)||e.prepareMode.kind==='local-alternative'&&/^ALT-[VRTB]$/.test(e.prepareMode.recipeId)))fail('准备模式身份');}
  const d=obj(e.discovery,'发现');for(const [k,v]of Object.entries(obj(d.cards,'发现卡'))){if(!/^[VRTB]-[SNE][12]$/.test(k))fail('卡身份');int(v,'卡序号');}
  for(const [k,v]of Object.entries(obj(d.identified,'辨认'))){if(!/^(7[5-9]|8[0-2])$/.test(k))fail('辨认材料');int(v,'辨认序号');}
  const methods=obj(e.methods,'方法');list(methods.directions,'方法方向',recipe);list(methods.full,'完整方法',recipe);
  for(const [k,v]of Object.entries(obj(methods.freeProgress,'免费方法'))){if(!region(k))fail('方法地区');obj(v,'方法进度');int(v.count,'方法趟次',2);if(v.targetId!==null&&!recipe(v.targetId))fail('方法目标');}
  for(const [k,v]of Object.entries(obj(e.trial,'试做'))){if(!recipe(k))fail('试做方法');obj(v,'试做记录');int(v.failedFullBatches,'失败批',3);int(v.attemptSeq,'试做序号');if(typeof v.owed!=='boolean')fail('试做保护');}
  for(const [k,v]of Object.entries(obj(e.cardProtection,'卡保护'))){if(!region(k))fail('卡保护地区');obj(v,'卡保护进度');int(v.specimen,'标本保护',3);int(v.lore,'见闻保护',3);}
  if(s.version>=5)validateBusinessState(s,fail);
  if(s.version>=6){
    validateCollectionsState(s,fail);
    // Regulars and projects are sparse per-identity records (Works I/J); menus hold at most three presets.
    for(const key of ['regulars','projects'])if(!e[key]||typeof e[key]!=='object'||Array.isArray(e[key]))fail(key);
    if(!e.menus||typeof e.menus!=='object'||Object.keys(e.menus).join()!=='presets'||!Array.isArray(e.menus.presets)||e.menus.presets.length>3)fail('菜单预设');
    validateLaterContainers(s,fail);
  }
}
