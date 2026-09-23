// Four regulars × four stages. Each stage has one gate and two alternative
// branches (business record or manual purchase/discovery); whichever is met
// first queues the stage as the single unread story of that regular and
// registers its note/memento in the same transaction. Reading only advances the
// relationship (never pays, never consumes stock). Old three chapters only make
// the first regular "already acquainted"; they never count as a read story.
import {REGIONAL,CONTENT_TEXT,resolveSpecies} from './content-registry.js';
import {ordersUnlockInfo} from './orders.js';
import {legacyRouteOpen} from './menu-model.js';
import {regionInfo,nextRegionalFact} from './region-model.js';
import {guideEligibility} from './regional-exploration.js';
import {grantEntitlementOnce,reconcileEntitlements} from './collection-progress.js';
import {RULES} from './integration-data.js';

import {newOperationsEnabled} from './rollback-policy.js';
export const REGULARS_VERSION=1;
export const REGULAR_IDS=Object.freeze(REGIONAL.regulars.map(r=>r.id));
const STAGES=Object.fromEntries(REGIONAL.regulars.flatMap(r=>r.stages.map((st,i)=>[st.id,{...st,regularId:r.id,index:i}])));
export const freshRegular=()=>({readStages:[],pendingStage:null,activatedSeq:null,baselines:{},lastVisit:null});

const facts=s=>s.expansion?.facts??{};
const witness=(s,id)=>!!facts(s).predicateWitnesses?.[id];
const service=(s,...menus)=>menus.some(m=>witness(s,`${m}:validService`));
const done=(s,...orders)=>orders.some(o=>witness(s,`${o}:complete`));
const card=(s,id)=>Object.hasOwn(s.expansion?.discovery?.cards??{},id);
const identified=(s,id)=>Object.hasOwn(s.expansion?.discovery?.identified??{},String(id));
// Unfound materials stay masked: the name shows only after its specimen card or identification.
const REGION_NAME={V:'谷地',R:'溪岸',T:'茶坡',B:'海湾'};
const materialName=(s,id)=>{const m=REGIONAL.materials.find(x=>x.id===id);return identified(s,id)||card(s,m.specimen)?CONTENT_TEXT[m.stableId]?.name??`材料${id}`:`${REGION_NAME[m.region]}的一种新材料`;};
const selector=id=>new Set(REGIONAL.selectors[id]??[]);
const collected=(s,key)=>(s.total?.[key]??0)>0;
// "Any N kinds, each at least 1, total M": the best choice is the top N counts.
function topKinds(map,allowed,n){const counts=Object.entries(map??{}).filter(([k,v])=>allowed.has(k)&&v>0).map(([,v])=>v).sort((a,b)=>b-a);return {kinds:counts.length,sum:counts.slice(0,n).reduce((a,b)=>a+b,0)};}
const check=(met,text)=>({met:!!met,text});
const regionCards=(s,region,types)=>REGIONAL.cards.filter(c=>c.region===region&&types.includes(c.type)&&card(s,c.id)).length;
const firstSale=s=>!!s.progress?.orders?.[RULES.storyOrders[0].id]?.completed;
const activatedCount=(state,key)=>state?.baselines?.[key]??0;

// Every gate/branch is transcribed once per stage ID (content-pack regulars);
// nothing reads the Chinese source text at runtime.
export const STAGE_RULES=Object.freeze({
  'RG1-1':{gate:s=>{const o=ordersUnlockInfo(s);return [check(o.met,o.met?'收240只、发现8种':o.missing.join('；')),check(firstSale(s),'完成旧第一笔生意')];},
    alts:[s=>service(s,'MN1'),s=>done(s,'O01')]},
  'RG1-2':{alts:[s=>service(s,'MN2'),s=>done(s,'O07')]},
  'RG1-3':{alts:[s=>service(s,'MN3','MN6'),s=>done(s,'O03','O09')]},
  'RG1-4':{gate:s=>{const n=Object.keys(s.total??{}).filter(k=>collected(s,k)&&resolveSpecies(k)?.edible).length;return [check(n>=6,`不同食用品种实收 ${Math.min(n,6)}/6 种`)];},
    alts:[s=>Object.keys(facts(s).menuWitnesses??{}).filter(m=>witness(s,`${m}:validService`)).length>=2,s=>Object.values(facts(s).orderTemplateCounts??{}).filter(n=>n>0).length>=2]},
  'RG2-1':{gate:s=>{const o=ordersUnlockInfo(s);return [check(legacyRouteOpen(s,'R'),'原溪岸路线开放'),check(o.met,o.met?'情境采购开放':`情境采购开放（${o.missing.join('；')}）`)];},
    alts:[s=>service(s,'MN4'),s=>done(s,'O05')]},
  'RG2-2':{gate:s=>[check(identified(s,77)||identified(s,78),`辨认${materialName(s,77)}或${materialName(s,78)}`)],
    alts:[s=>card(s,'R-E1')||card(s,'R-E2'),s=>regionCards(s,'R',['specimen','lore'])>=2]},
  'RG2-3':{gate:s=>[check(REGIONAL.species.some(c=>c.region==='R'&&c.edible&&collected(s,c.key)),'实收一款溪岸食用新品')],
    alts:[s=>witness(s,'RG2-3:business'),s=>witness(s,'O06:R:complete')]},
  // "O05 again" counts completions after this stage was activated (baseline).
  'RG2-4':{baseline:s=>({O05:facts(s).orderTemplateCounts?.O05??0}),
    alts:[s=>card(s,'R-E2')&&service(s,'MN4'),(s,state)=>(facts(s).orderTemplateCounts?.O05??0)>activatedCount(state,'O05')||witness(s,'O04:display')]},
  'RG3-1':{gate:s=>[check(regionInfo(s,'T').met,'茶坡地区开放')],
    alts:[s=>{const t=topKinds(facts(s).businessCounts,selector('tea'),2);return t.kinds>=2&&t.sum>=6;},s=>witness(s,'RG3-1:order')]},
  'RG3-2':{gate:s=>[check(identified(s,79),`辨认${materialName(s,79)}`)],
    alts:[s=>card(s,'T-E1'),s=>card(s,'T-N1')&&witness(s,'RG3-2:regionalBatch')]},
  'RG3-3':{alts:[s=>witness(s,'RG3-3:business'),s=>done(s,'O08')]},
  'RG3-4':{alts:[s=>{const t=topKinds(facts(s).businessCounts,new Set([...selector('tea'),...selector('snack')]),3);return t.kinds>=3&&t.sum>=18;},
    s=>{const t=topKinds(facts(s).orderCounts,new Set([...selector('tea'),...selector('snack')]),3);return t.kinds>=3&&t.sum>=18;}]},
  'RG4-1':{gate:s=>{const g=guideEligibility(s);return [check(g.met,g.met?'厨房Lv.3、发现40种且前三区任一标本辨认并实收当地新品':g.missing.join('；'))];},
    alts:[s=>service(s,'MN1'),s=>done(s,'O12')],guide:true},
  'RG4-2':{gate:s=>[check(identified(s,81),`辨认${materialName(s,81)}`)],alts:[s=>card(s,'B-N1'),s=>card(s,'B-E1')]},
  'RG4-3':{alts:[s=>service(s,'MN7'),s=>done(s,'O11')]},
  'RG4-4':{alts:[s=>witness(s,'B-E1:cargoExchange'),s=>witness(s,'O06:B:complete')]},
});

const regulars=s=>{const r=s.expansion?.regulars;if(!r)throw Error('常客尚未完成存档迁移');return r;};
const definition=id=>{const r=REGIONAL.regulars.find(x=>x.id===id);if(!r)throw Error('没有这位常客');return r;};
export const acquainted=s=>RULES.storyOrders.every(o=>s.progress?.orders?.[o.id]?.completed);

// Read-only view of one regular's current stage and both branches.
export function regularInfo(s,id){
  const def=definition(id),state=s.expansion?.regulars?.[id]??null,read=state?.readStages??[];
  const complete=read.length===def.stages.length,stage=complete?null:def.stages[read.length],rules=stage?STAGE_RULES[stage.id]:null;
  const gate=rules?(rules.gate?.(s,state)??[]).concat(read.length?[check(true,'上一段已读')]:[]):[];
  const gateMet=!!stage&&gate.every(c=>c.met);
  const branches=rules?rules.alts.map((fn,i)=>({index:i,text:CONTENT_TEXT[stage.id]?.alternatives?.[i]??'',met:!!fn(s,state)})):[];
  return {id,name:CONTENT_TEXT[id]?.name??id,region:def.region,memento:def.memento,state,readStages:[...read],complete,
    stage:stage?{id:stage.id,index:read.length,title:CONTENT_TEXT[stage.id]?.title??stage.id,reward:stage.reward}:null,
    gate,gateMet,branches,pending:state?.pendingStage??null,met:gateMet&&branches.some(b=>b.met),
    // A relationship is visible once its first gate is met or any stage exists.
    opened:!!state||(read.length===0&&gateMet),acquainted:id==='RG1'&&acquainted(s)};
}

// Idempotent: queue the active stage of each regular when its gate and any
// branch hold; register the stage reward once. RG4-1 also opens the bay guide.
export function reconcileRegulars(s,{visitorRegularId=null}={}){
  if(!newOperationsEnabled('regulars'))return [];
  if(!s.expansion?.regulars||!s.expansion?.collections)return [];
  const queued=[];
  for(const id of REGULAR_IDS){
    const info=regularInfo(s,id);
    if(info.complete||info.pending||!info.met)continue;
    // First acquaintance by business is presented by an actual visitor step.
    // Manual order/group alternatives present immediately. Later stages retain
    // the authored retroactive rule, with no additional visit requirement.
    const branch=info.branches.find(b=>b.met&&(info.stage.index>0||b.index===1||visitorRegularId===id));
    if(!branch)continue;
    const r=regulars(s);r[id]??=freshRegular();
    const seq=s.meta.factSeq+1;if(!Number.isSafeInteger(seq))throw Error('事实序号超出范围');s.meta.factSeq=seq;
    r[id].pendingStage={id:info.stage.id,seq,branch:branch.index};
    grantEntitlementOnce(s,info.stage.reward,info.stage.id);
    if(STAGE_RULES[info.stage.id].guide&&!s.expansion.regions.guideFlags.includes('GUIDE-B')){s.expansion.regions.guideFlags.push('GUIDE-B');nextRegionalFact(s);}
    queued.push(info.stage.id);
  }
  return queued;
}
// Every committed command runs this after its reducer; so does load (once).
export function reconcileProgress(s){const stages=reconcileRegulars(s);return [...stages,...reconcileEntitlements(s)];}

// Reading moves the pending stage into readStages and activates the next one
// (recording its baseline); it never pays and never consumes stock.
export function readRegularStage(s,id){
  const r=regulars(s),state=r[id];definition(id);
  if(!state?.pendingStage)throw Error('这位常客暂时没有新的一段');
  const stageId=state.pendingStage.id;state.readStages.push(stageId);state.pendingStage=null;
  state.activatedSeq=s.meta.factSeq;
  const next=definition(id).stages[state.readStages.length];
  state.baselines=next&&STAGE_RULES[next.id].baseline?STAGE_RULES[next.id].baseline(s):{};
  reconcileRegulars(s);
  return {stageId,next:next?.id??null};
}

// Frozen at opening: relationships already met/open, pinned first, then the
// business tendency ('regulars' = least-advanced first, 'discovery' = region order R,T,B,V).
export function visitorCandidates(s,{pinned=null,tendency='regulars'}={}){
  const order=tendency==='discovery'?['RG2','RG3','RG4','RG1']:REGULAR_IDS;
  const open=order.map(id=>regularInfo(s,id)).filter(x=>(x.opened||x.met)&&!x.complete);
  if(tendency==='regulars')open.sort((a,b)=>a.readStages.length-b.readStages.length);
  const ids=open.map(x=>x.id);
  if(pinned&&ids.includes(pinned))ids.splice(ids.indexOf(pinned),1),ids.unshift(pinned);
  return ids.slice(0,4);
}
// A visitor step (every 12 real sales) presents one queued, not yet presented
// story among the frozen candidates; otherwise it is ordinary text with no CP.
export function presentVisitor(s,session){
  if(!s.expansion?.regulars)return null;
  reconcileRegulars(s);
  for(const id of session.visitorCandidates??[]){
    // Only the candidate actually chosen by this visitor may enter through
    // the first-stage business branch; do not queue every frozen candidate.
    const info=regularInfo(s,id);
    if(!info.pending&&info.stage?.index===0&&info.met)reconcileRegulars(s,{visitorRegularId:id});
    const state=s.expansion.regulars[id];
    if(state?.pendingStage&&state.lastVisit?.stageId!==state.pendingStage.id){state.lastVisit={stageId:state.pendingStage.id,sessionId:session.id};return {regularId:id,stageId:state.pendingStage.id};}
  }
  return null;
}

export function validateRegularsState(s,fail){
  const all=s.expansion.regulars;
  if(!all||typeof all!=='object'||Array.isArray(all))fail('常客容器');
  const got=s.expansion.collections?.entitlements??{};
  for(const [id,state]of Object.entries(all)){
    const def=REGIONAL.regulars.find(r=>r.id===id);if(!def)fail('常客身份');
    if(!state||typeof state!=='object'||Array.isArray(state)||Object.keys(state).sort().join()!=='activatedSeq,baselines,lastVisit,pendingStage,readStages')fail('常客记录');
    const ids=def.stages.map(x=>x.id),read=state.readStages;
    if(!Array.isArray(read)||read.length>4||read.some((x,i)=>x!==ids[i]))fail('常客已读顺序');
    for(const x of read)if(!Object.hasOwn(got,def.stages[ids.indexOf(x)].reward))fail('常客成果缺失');
    const p=state.pendingStage;
    if(p!==null){
      if(!p||typeof p!=='object'||Object.keys(p).sort().join()!=='branch,id,seq'||p.id!==ids[read.length]||![0,1].includes(p.branch)||!Number.isSafeInteger(p.seq)||p.seq<1||p.seq>s.meta.factSeq)fail('常客未读段');
      if(!Object.hasOwn(got,def.stages[read.length].reward))fail('常客成果缺失');
    }else if(!read.length)fail('空常客记录');
    if(read.length?(!Number.isSafeInteger(state.activatedSeq)||state.activatedSeq<1||state.activatedSeq>s.meta.factSeq):state.activatedSeq!==null)fail('常客激活序号');
    const b=state.baselines;if(!b||typeof b!=='object'||Array.isArray(b))fail('常客基线');
    const expected=ids[read.length]==='RG2-4'?['O05']:[];
    if(Object.keys(b).sort().join()!==expected.join()||Object.values(b).some(v=>!Number.isSafeInteger(v)||v<0))fail('常客基线');
    const v=state.lastVisit;
    if(v!==null&&(!v||typeof v!=='object'||Object.keys(v).sort().join()!=='sessionId,stageId'||!ids.includes(v.stageId)||!/^business-[1-9]\d*$/.test(v.sessionId)||ids.indexOf(v.stageId)>read.length))fail('常客来访记录');
  }
}
export {STAGES as REGULAR_STAGES};
