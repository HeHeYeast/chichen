// Four long projects (PJ-1..4), three stages each. A stage completes by one
// explicit command once its check holds, its bounded delivery is full and its
// fixed CP cost is paid in the same transaction. Deliveries are separate
// confirmed commands (never pay CP back); the first delivery of a "choose N
// kinds" stage locks those kinds. Projects add no base payments, no multipliers
// and no 13th memento. PJ-1 unlocks three menu presets, PJ-2 teaches ALT-R, PJ-4
// may show any recorded species' permanent portrait (never counted as food).
import {REGIONAL,CONTENT_TEXT,resolveSpecies,SPECIES_TRADE} from './content-registry.js';
import {discoveryCount} from './progression.js';
import {freeCount,homeCount,lockedCount} from './inventory.js';
import {regionInfo} from './region-model.js';
import {reduceFacts} from './facts.js';
import {speciesDiscovered} from './species-state.js';
import {ACTIVE_BUSINESS_MENUS} from './business.js';

import {newOperationsEnabled,assertNewOperation} from './rollback-policy.js';
export const MAX_PRESETS=3,MAX_PORTRAITS=12;
function assertProjectSubmission(s,projectId,stageId){
  if(newOperationsEnabled('projects'))return;
  const p=s.expansion.projects?.[projectId];
  // Only this stage's already saved contribution permits continued settlement.
  // Completing a previous stage never silently starts the next while paused.
  const started=Object.values(p?.deliveries?.[stageId]??{}).some(n=>n>0)||(p?.payments?.[stageId]??0)>0||(p?.pinnedChoices?.[stageId]?.length??0)>0;
  if(!started)assertNewOperation('projects');
}
export const PROJECT_IDS=Object.freeze(REGIONAL.projects.map(p=>p.id));
export const freshProject=()=>({stages:{},pinnedChoices:{},deliveries:{},payments:{}});

const facts=s=>s.expansion?.facts??{};
const witness=(s,id)=>!!facts(s).predicateWitnesses?.[id];
const card=(s,id)=>Object.hasOwn(s.expansion?.discovery?.cards??{},id);
const identified=(s,id)=>Object.hasOwn(s.expansion?.discovery?.identified??{},String(id));
const known=(s,key)=>{const c=resolveSpecies(key);return !!c&&speciesDiscovered(s,c.egg,c.id);};
const check=(met,text)=>({met:!!met,text});
const sum=map=>Object.values(map??{}).reduce((a,b)=>a+b,0);
const validMenus=s=>REGIONAL.menus.filter(m=>witness(s,`${m.id}:validService`)).length;
const REGION_NAME={V:'谷地',R:'溪岸',T:'茶坡',B:'海湾'};
const definition=id=>{const p=REGIONAL.projects.find(x=>x.id===id);if(!p)throw Error('没有这个项目');return p;};
const project=(s,id)=>s.expansion?.projects?.[id]??null;
export const stageComplete=(s,projectId,stageId)=>project(s,projectId)?.stages?.[stageId]?.complete===true;
export const projectComplete=(s,id)=>stageComplete(s,id,definition(id).stages.at(-1).id);
const previousDone=(s,p,index)=>index===0||stageComplete(s,p.id,p.stages[index-1].id);
const previous=(s,p,index)=>index?[check(previousDone(s,p,index),'前一阶段已完成')]:[];
const categoryOf=key=>SPECIES_TRADE[key]?.category??null;
function topKinds(map,allowed,n){const counts=Object.entries(map).filter(([k,v])=>allowed.has(k)&&v>0).map(([,v])=>v).sort((a,b)=>b-a);return {kinds:counts.length,sum:counts.slice(0,n).reduce((a,b)=>a+b,0)};}
const regularAchieved=(s,id)=>{const r=s.expansion?.regulars?.[id];return !!r&&(r.readStages.length>0||!!r.pendingStage);};

// Gates and checks transcribed once per project/stage ID (content-pack projects).
export const PROJECT_RULES=Object.freeze({
  'PJ-1':{gate:s=>[check(s.kitchenLevel>=1,'厨房 Lv.2'),check(discoveryCount(s)>=12,`发现 ${Math.min(discoveryCount(s),12)}/12 种`)]},
  'PJ-1-A':{check:(s,p)=>{const n=[...p.oldAllowed,...p.newAllowed].filter(k=>known(s,k)&&resolveSpecies(k).edible).length;return [check(n>=6,`可营业品种已收录 ${Math.min(n,6)}/6 种`)];}},
  'PJ-1-B':{check:s=>[check(validMenus(s)>=2,`不同菜单有效接待 ${Math.min(validMenus(s),2)}/2 种`)]},
  'PJ-1-C':{check:()=>[]},
  'PJ-2':{gate:s=>[check(s.kitchenLevel>=1,'厨房 Lv.2'),check(identified(s,77)||identified(s,78),'辨认一种溪岸标本材料')]},
  'PJ-2-A':{check:s=>{const keys=REGIONAL.species.filter(c=>c.region==='R'&&known(s,c.key));
    return [check(card(s,'R-S1')&&card(s,'R-S2')&&identified(s,77)&&identified(s,78),'溪岸两张标本已登记并辨认'),check(keys.length>=4,`溪岸新品实收 ${Math.min(keys.length,4)}/4 种`),check(keys.some(c=>c.egg===0)&&keys.some(c=>c.egg===1),'鸡、鸭新品各至少1种')];}},
  'PJ-2-B':{check:()=>[]},
  'PJ-2-C':{check:()=>[]},
  'PJ-3':{gate:s=>[check(regionInfo(s,'T').met,'茶坡地区开放')]},
  'PJ-3-A':{check:s=>[check(card(s,'T-E1'),'记录一次焙叶事件')]},
  'PJ-3-B':{check:s=>{const allowed=new Set([...REGIONAL.selectors.tea,...REGIONAL.selectors.snack]),merged={};
    for(const map of [facts(s).businessCounts,facts(s).orderCounts])for(const [k,v]of Object.entries(map??{}))merged[k]=(merged[k]??0)+v;
    const t=topKinds(merged,allowed,3);return [check(t.kinds>=3&&t.sum>=36,`茶味或点心任选3种（已有 ${Math.min(t.kinds,3)}/3 种），营业或采购累计 ${Math.min(t.sum,36)}/36 只`),check(regularAchieved(s,'RG3'),'茶坡访客任一段完成')];}},
  'PJ-3-C':{check:()=>[]},
  'PJ-4':{gate:s=>[check(regionInfo(s,'B').met,'海湾开放')]},
  'PJ-4-A':{check:s=>['V','R','T','B'].flatMap(r=>{const n=REGIONAL.species.filter(c=>c.region===r&&known(s,c.key)).length,cards=REGIONAL.cards.filter(c=>c.region===r&&['specimen','lore'].includes(c.type)&&card(s,c.id)).length;
    return [check(n>=6&&cards>=2,`${REGION_NAME[r]}：新品 ${Math.min(n,6)}/6 种，标本或见闻 ${Math.min(cards,2)}/2 张`)];})},
  'PJ-4-B':{check:s=>[check(validMenus(s)>=4,`不同菜单有效接待 ${Math.min(validMenus(s),4)}/4 种`)]},
  'PJ-4-C':{check:()=>[]},
});

// Delivery rule of a stage (null when it has none) and its progress.
function deliveryInfo(s,p,stage){
  const c=stage.consume;if(!c)return null;
  const delivered=project(s,p.id)?.deliveries?.[stage.id]??{},locked=project(s,p.id)?.pinnedChoices?.[stage.id]??null,total=sum(delivered);
  if(c.distinct){const target=c.distinct*c.quantityEach;return {kind:'choose',distinct:c.distinct,quantityEach:c.quantityEach,allowed:c.allowed,locked,delivered:{...delivered},total,target,full:!!locked&&locked.every(k=>(delivered[k]??0)>=c.quantityEach)};}
  const categories=new Set(Object.keys(delivered).map(categoryOf).filter(Boolean));
  return {kind:'total',total,target:c.total,minimumCategories:c.minimumSignatureCategories,categories:[...categories],allowed:c.allowed,delivered:{...delivered},full:total>=c.total&&categories.size>=c.minimumSignatureCategories};
}

export function projectInfo(s,id){
  const p=definition(id),gate=PROJECT_RULES[id].gate(s),gateMet=gate.every(c=>c.met);
  const stages=p.stages.map((stage,index)=>{
    const complete=stageComplete(s,id,stage.id),checks=[...previous(s,p,index),...PROJECT_RULES[stage.id].check(s,p)],delivery=deliveryInfo(s,p,stage);
    const ready=gateMet&&!complete&&checks.every(c=>c.met)&&(!delivery||delivery.full);
    return {id:stage.id,index,complete,checks,checksMet:checks.every(c=>c.met),delivery,costCP:stage.costCP,paid:project(s,id)?.payments?.[stage.id]??0,ready,canPay:ready&&s.cp>=stage.costCP,
      current:!complete&&previousDone(s,p,index)};
  });
  return {id,name:CONTENT_TEXT[id]?.name??id,gate,gateMet,stages,complete:stages.every(x=>x.complete),current:stages.find(x=>x.current)??null};
}

const ensure=(s,id)=>{const all=s.expansion.projects;if(!all)throw Error('项目尚未完成存档迁移');return all[id]??=freshProject();};

// Confirmed delivery toward the current stage. Uses only free stock; the
// default keeps one at home. Delivered birds leave the farm; no CP is paid.
export function deliverProject(s,projectId,stageId,selection,{choice=null,overrideKeepOne=false}={}){
  assertProjectSubmission(s,projectId,stageId);
  const info=projectInfo(s,projectId),stage=info.stages.find(x=>x.id===stageId);
  if(!info.gateMet)throw Error(info.gate.filter(c=>!c.met).map(c=>c.text).join('；'));
  if(!stage||!stage.current)throw Error('这一阶段现在不能交付');if(!stage.delivery)throw Error('这一阶段不需要交付');
  if(!stage.checksMet)throw Error(stage.checks.filter(c=>!c.met).map(c=>c.text).join('；'));
  if(!selection||typeof selection!=='object'||Array.isArray(selection)||!Object.keys(selection).length)throw Error('请选择要交付的伙伴');
  const d=stage.delivery,allowed=new Set(d.allowed);
  let locked=d.locked;
  if(d.kind==='choose'&&!locked){
    if(!Array.isArray(choice)||new Set(choice).size!==d.distinct||choice.some(k=>!allowed.has(k)||!resolveSpecies(k)?.edible))throw Error(`先选定${d.distinct}种食用料理，首次交付后不再更换`);
    locked=[...choice];
  }
  const after={...d.delivered};
  for(const [key,n]of Object.entries(selection)){
    if(!allowed.has(key)||!resolveSpecies(key)?.edible)throw Error('这一阶段不收这种伙伴');
    if(!Number.isSafeInteger(n)||n<1)throw Error('交付数量无效');
    if(locked&&!locked.includes(key))throw Error('只能交付首次选定的品种');
    if(freeCount(s,key)<n)throw Error('可用伙伴不足，可能已用于营业、寻访或订单预留');
    if(!overrideKeepOne&&homeCount(s,key)-n<lockedCount(s,key))throw Error(`已锁定在家${lockedCount(s,key)}只（默认每种留1只）；到仓库调整锁定数量后再用`);
    after[key]=(after[key]??0)+n;
    if(d.kind==='choose'&&after[key]>d.quantityEach)throw Error(`每种交${d.quantityEach}只就够了`);
  }
  if(d.kind==='total'){
    const total=sum(after),categories=new Set(Object.keys(after).map(categoryOf).filter(Boolean)).size;
    if(total>d.target)throw Error(`这一阶段一共交${d.target}只`);
    // Leave enough room to still cover the required signature categories.
    if(d.target-total<d.minimumCategories-categories)throw Error(`至少要有${d.minimumCategories}类菜式，留些位置给其他菜式`);
  }
  const p=ensure(s,projectId);
  if(d.kind==='choose'&&!p.pinnedChoices[stageId])p.pinnedChoices[stageId]=[...locked];
  const delivered=p.deliveries[stageId]??={};
  for(const [key,n]of Object.entries(selection)){s.farm[key]-=n;delivered[key]=(delivered[key]??0)+n;}
  reduceFacts(s,Object.entries(selection).map(([key,quantity])=>({kind:'projectDelivery',stageId,key,quantity})));
  return {stageId,delivered:sum(delivered),target:d.target};
}

// One command pays the stage's fixed cost and records it complete. Retrying a
// completed stage is refused; insufficient CP changes nothing.
export function completeProjectStage(s,projectId,stageId){
  assertProjectSubmission(s,projectId,stageId);
  const info=projectInfo(s,projectId),stage=info.stages.find(x=>x.id===stageId);
  if(!stage)throw Error('没有这一阶段');if(stage.complete)throw Error('这一阶段已经完成');
  if(!info.gateMet)throw Error(info.gate.filter(c=>!c.met).map(c=>c.text).join('；'));
  if(!stage.current)throw Error('先完成前一阶段');
  if(!stage.checksMet)throw Error(stage.checks.filter(c=>!c.met).map(c=>c.text).join('；'));
  if(stage.delivery&&!stage.delivery.full)throw Error(`还需交付：${stage.delivery.total}/${stage.delivery.target}只`);
  if(s.cp<stage.costCP)throw Error(`CP不足：需要${stage.costCP} CP`);
  const p=ensure(s,projectId);s.cp-=stage.costCP;
  if(stage.costCP){p.payments[stageId]=stage.costCP;reduceFacts(s,[{kind:'projectPayment',stageId,amount:stage.costCP}]);}
  const seq=s.meta.factSeq+1;s.meta.factSeq=seq;p.stages[stageId]={complete:true,seq};
  // PJ-2 teaches the local alternative ALT-R (a method, not a new species).
  if(projectId==='PJ-2'&&projectComplete(s,'PJ-2'))for(const alt of REGIONAL.alternatives.filter(a=>a.unlock==='PJ-2'))for(const field of ['directions','full'])if(!s.expansion.methods[field].includes(alt.id))s.expansion.methods[field].push(alt.id);
  return {stageId,paid:stage.costCP,projectComplete:projectComplete(s,projectId)};
}

// 不挡进度 (2026-10-07): a stage that costs nothing completes by itself once it is met (its delivery, if any, full);
// stages with a CP cost still wait for the player's 登记. Runs after every command (regulars.js reconcileProgress).
export function autoCompleteProjects(s){
  if(!s.expansion?.projects||!newOperationsEnabled('projects'))return [];
  const done=[];
  for(const id of PROJECT_IDS)for(let step=0;step<3;step++){
    const stage=projectInfo(s,id).current;
    if(!stage||!stage.ready||stage.costCP!==0)break;
    completeProjectStage(s,id,stage.id);done.push(stage.id);
  }
  return done;
}

// PJ-4 exhibition: any recorded species' permanent portrait; nothing is consumed.
export function setProjectPortraits(s,keys){
  if(!projectInfo(s,'PJ-4').gateMet)throw Error('海湾开放后才能布置四地风味展');
  if(!Array.isArray(keys)||keys.length>MAX_PORTRAITS||new Set(keys).size!==keys.length)throw Error(`展册最多放${MAX_PORTRAITS}幅画像`);
  const allowed=new Set(REGIONAL.projects.find(p=>p.id==='PJ-4').optionalDisplay.allowed);
  for(const key of keys)if(!allowed.has(key)||!known(s,key))throw Error('只能展出已经收录的伙伴画像');
  const p=ensure(s,'PJ-4');if(keys.length)p.pinnedChoices.portraits=[...keys];else delete p.pinnedChoices.portraits;
  return [...keys];
}

// PJ-1 result: up to three saved business setups. Applying one only fills the
// preparation draft; it never reserves stock.
export function saveMenuPreset(s,slot,{menuId,stock}){
  if(!projectComplete(s,'PJ-1'))throw Error('完成「我的小店招牌册」后才能保存菜单预设');
  if(!Number.isSafeInteger(slot)||slot<0||slot>=MAX_PRESETS)throw Error('预设位置无效');
  if(!ACTIVE_BUSINESS_MENUS.includes(menuId))throw Error('这张菜单尚未开放');
  const keys=Object.keys(stock??{});if(!keys.length||keys.length>6)throw Error('预设需要1至6种出品');
  for(const key of keys)if(!resolveSpecies(key)?.edible||!Number.isSafeInteger(stock[key])||stock[key]<1||stock[key]>72)throw Error('预设出品无效');
  const presets=s.expansion.menus.presets,preset={menuId,stock:{...stock}};
  if(slot>presets.length)throw Error('请按顺序保存预设');presets[slot]=preset;return preset;
}
export function deleteMenuPreset(s,slot){const presets=s.expansion.menus.presets;if(!Number.isSafeInteger(slot)||slot<0||slot>=presets.length)throw Error('预设位置无效');presets.splice(slot,1);}

export function validateProjectsState(s,fail){
  const all=s.expansion.projects;if(!all||typeof all!=='object'||Array.isArray(all))fail('项目容器');
  for(const [id,p]of Object.entries(all)){
    const def=REGIONAL.projects.find(x=>x.id===id);if(!def)fail('项目身份');
    if(!p||typeof p!=='object'||Array.isArray(p)||Object.keys(p).sort().join()!=='deliveries,payments,pinnedChoices,stages')fail('项目记录');
    const ids=def.stages.map(x=>x.id);let seenIncomplete=false;
    for(const [i,stage]of def.stages.entries()){
      const done=p.stages[stage.id];
      if(done!==undefined){if(seenIncomplete||!done||Object.keys(done).sort().join()!=='complete,seq'||done.complete!==true||!Number.isSafeInteger(done.seq)||done.seq<1||done.seq>s.meta.factSeq)fail('项目阶段');}
      else seenIncomplete=true;
      const paid=p.payments[stage.id];
      if(paid!==undefined&&(paid!==stage.costCP||!done||!stage.costCP))fail('项目付款');
      if(done&&stage.costCP&&paid!==stage.costCP)fail('项目付款');
      const delivered=p.deliveries[stage.id];
      if(delivered!==undefined){
        if(!stage.consume||!delivered||typeof delivered!=='object'||Array.isArray(delivered))fail('项目交付');
        for(const [key,n]of Object.entries(delivered))if(!stage.consume.allowed.includes(key)||!Number.isSafeInteger(n)||n<1)fail('项目交付');
        const c=stage.consume,total=sum(delivered);
        if(c.distinct){const locked=p.pinnedChoices[stage.id];if(!Array.isArray(locked)||locked.length!==c.distinct||Object.keys(delivered).some(k=>!locked.includes(k)||delivered[k]>c.quantityEach))fail('项目交付');if(done&&locked.some(k=>delivered[k]!==c.quantityEach))fail('项目交付');}
        else{if(total>c.total)fail('项目交付');if(done&&(total!==c.total||new Set(Object.keys(delivered).map(categoryOf).filter(Boolean)).size<c.minimumSignatureCategories))fail('项目交付');}
      }else if(done&&stage.consume)fail('项目交付');
      if(i>0&&(delivered!==undefined)&&!p.stages[ids[i-1]])fail('项目交付顺序');
    }
    for(const k of Object.keys(p.stages))if(!ids.includes(k))fail('项目阶段');
    for(const k of Object.keys(p.payments))if(!ids.includes(k))fail('项目付款');
    for(const k of Object.keys(p.deliveries))if(!ids.includes(k))fail('项目交付');
    for(const [k,v]of Object.entries(p.pinnedChoices)){
      if(k==='portraits'){if(id!=='PJ-4'||!Array.isArray(v)||!v.length||v.length>MAX_PORTRAITS||new Set(v).size!==v.length||v.some(key=>!def.optionalDisplay.allowed.includes(key)))fail('展册画像');continue;}
      const stage=def.stages.find(x=>x.id===k);if(!stage?.consume?.distinct||!Array.isArray(v)||v.length!==stage.consume.distinct||new Set(v).size!==v.length||v.some(key=>!stage.consume.allowed.includes(key)))fail('项目选定品种');
    }
  }
  const presets=s.expansion.menus.presets;
  if(presets.length&&!projectComplete(s,'PJ-1'))fail('菜单预设尚未开放');
  for(const preset of presets){
    if(!preset||typeof preset!=='object'||Object.keys(preset).sort().join()!=='menuId,stock'||!REGIONAL.menus.some(m=>m.id===preset.menuId))fail('菜单预设');
    const keys=Object.keys(preset.stock??{});if(!keys.length||keys.length>6||keys.some(k=>!resolveSpecies(k)?.edible||!Number.isSafeInteger(preset.stock[k])||preset.stock[k]<1||preset.stock[k]>72))fail('菜单预设');
  }
}
