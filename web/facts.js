import {REGIONAL,resolveSpecies} from './content-registry.js';

// Bounded maps keyed only by the frozen content IDs. Producers own their entity
// cursors (window, delivery, trip, batch slot); they emit each event once.
export function freshFacts(){return {version:1,businessCounts:{},businessMenuCounts:{},orderCounts:{},orderTemplateCounts:{},orderGroupCounts:{},menuWitnesses:{},tripWitnesses:{},companionFirst:{},eventWitnesses:{},predicateWitnesses:{},materialBatches:{},payments:{},projectDeliveries:{}};}
const add=(map,key,n)=>{const value=(map[key]??0)+n;if(!Number.isSafeInteger(value)||value<0)throw Error('事实计数超出范围');map[key]=value;};
const quantity=n=>{if(!Number.isSafeInteger(n)||n<1)throw Error('事实数量无效');return n;};
const species=key=>{const c=resolveSpecies(key);if(!c)throw Error('事实品种无效');return c;};
const total=map=>Object.values(map).reduce((a,b)=>a+b,0);
const isOld=key=>{const [egg,id]=key.split(':').map(Number);return id<(egg?65:128);};
function witness(f,id,seq,sourceId){const old=f.predicateWitnesses[id];f.predicateWitnesses[id]={firstSeq:old?.firstSeq??seq,lastSeq:seq,sourceId};}
function businessWitness(f,e,seq){
  const menu=REGIONAL.menus.find(m=>m.id===e.menuId);if(!menu)throw Error('事实菜单无效');
  const existing=f.menuWitnesses[e.menuId]??{count:0,completeCount:0,firstSeq:seq,lastSeq:seq,lastSessionId:null,completeLastSessionId:null};
  if(e.valid){if(existing.lastSessionId!==e.sessionId)existing.count++;existing.lastSessionId=e.sessionId;existing.lastSeq=seq;witness(f,`${e.menuId}:validService`,seq,e.sessionId);}
  if(e.complete){if(existing.completeLastSessionId!==e.sessionId)existing.completeCount++;existing.completeLastSessionId=e.sessionId;witness(f,`${e.menuId}:completeService`,seq,e.sessionId);}
  if(e.valid||e.complete)f.menuWitnesses[e.menuId]=existing;
  const keys=Object.keys(e.soldByKey).filter(k=>e.soldByKey[k]>0);
  if(e.valid&&['MN4','MN2'].includes(e.menuId)&&keys.some(k=>species(k).region==='R'&&species(k).edible))witness(f,'RG2-3:business',seq,e.sessionId);
  if(e.valid&&e.menuId==='MN5'&&keys.some(a=>keys.some(b=>a!==b&&species(a).tags.includes('savory')&&species(b).tags.includes('sweet'))))witness(f,'RG3-3:business',seq,e.sessionId);
  const fullKeys=Object.keys(e.fullSoldByKey).filter(k=>e.fullSoldByKey[k]>0);
  if(e.complete&&new Set(fullKeys.map(k=>species(k).egg)).size===2)witness(f,'SP-TABLE:practice',seq,e.sessionId);
}

function tripWitness(s,f,e,seq){
  if(!REGIONAL.regions.some(r=>r.id===e.region)||!Array.isArray(e.members)||e.members.length<1||e.members.length>3)throw Error('寻访事实无效');
  const members=e.members.map(m=>{species(m.key);if(!Array.isArray(m.traits)||!Number.isInteger(m.gather)||!Number.isInteger(m.discover)||m.gather<0||m.gather>20||m.discover<0||m.discover>20||!['yard','water','wood'].includes(m.environment))throw Error('同行事实缺少出发快照');return m;});
  const old=f.tripWitnesses[e.region]??{count:0,firstSeq:seq,lastSeq:seq};old.count++;old.lastSeq=seq;f.tripWitnesses[e.region]=old;
  for(const m of members)if(!f.companionFirst[m.key])f.companionFirst[m.key]={seq,tripId:e.tripId,region:e.region,gather:m.gather,discover:m.discover,environment:m.environment,traits:[...m.traits]};
  witness(f,'SP-ALL:practice',seq,e.tripId);
  if(members.some(m=>isOld(m.key))&&members.some(m=>species(m.key).region===e.region))witness(f,`COL-${e.region}:practice.trip`,seq,e.tripId);
  if(new Set(members.map(m=>species(m.key).season).filter(Boolean)).size>=2)witness(f,'COL-8:practice.trip',seq,e.tripId);
  if(e.cardId){
    const card=REGIONAL.cards.find(c=>c.id===e.cardId);if(!card||card.region!==e.region)throw Error('发现卡事实无效');
    const event=f.eventWitnesses[e.cardId];f.eventWitnesses[e.cardId]={firstSeq:event?.firstSeq??seq,lastSeq:seq,count:(event?.count??0)+1,tripId:e.tripId};
  }else if(e.placeId===null)return;
  // Cards are found once. Card-linked practices therefore also count on any later
  // regional trip to a region whose card is already recorded, so a first trip with
  // the "wrong" team never closes a page for good.
  const cards=REGIONAL.cards.filter(c=>c.region===e.region&&(c.id===e.cardId||Object.hasOwn(s.expansion?.discovery?.cards??{},c.id)));
  if(cards.some(c=>['V-E1','R-E1','T-E1','B-E2'].includes(c.id))&&members.some(m=>m.traits.includes('leaf')||m.traits.includes('tea')))witness(f,'SP-LEAF:practice',seq,e.tripId);
  if(cards.some(c=>c.type==='event'))for(const c of REGIONAL.collections.filter(c=>c.kind==='theme'&&c.region===e.region&&c.id!=='COL-8'))if(members.some(m=>c.optional.allowed.includes(m.key)))witness(f,`${c.id}:practice.trip`,seq,e.tripId);
  const shape=REGIONAL.specials.find(s=>s.id==='SP-SHAPE');if(cards.some(c=>c.type==='lore')&&members.some(m=>[...shape.oldKeys,...shape.newKeys].includes(m.key)))witness(f,'SP-SHAPE:practice',seq,e.tripId);
}

export function reduceFacts(s,events){
  const f=s.expansion.facts;if(!f||f.version!==1)throw Error('经营事实尚未完成迁移');
  for(const e of events){const seq=s.meta.factSeq+1;if(!Number.isSafeInteger(seq))throw Error('事实序号超出范围');s.meta.factSeq=seq;
    switch(e.kind){
      case 'businessSale':{
        const c=species(e.key),n=quantity(e.quantity);if(!REGIONAL.menus.some(m=>m.id===e.menuId)||!['ordinary','suitable','complete'].includes(e.tier))throw Error('营业事实无效');
        add(f.businessCounts,e.key,n);f.businessMenuCounts[e.menuId]??={};add(f.businessMenuCounts[e.menuId],e.key,n);
        if(c.region&&c.edible)witness(f,`COL-${c.region}:practice.business`,seq,e.sessionId);break;
      }
      case 'businessWitness':businessWitness(f,e,seq);break;
      case 'orderDelivery':{
        const order=REGIONAL.orders.find(o=>o.id===e.templateId),c=species(e.key),n=quantity(e.quantity);if(!order?.groups.some(g=>g.id===e.groupId&&g.allowed.includes(e.key)))throw Error('采购事实无效');
        add(f.orderCounts,e.key,n);f.orderGroupCounts[e.groupId]??={};add(f.orderGroupCounts[e.groupId],e.key,n);
        if(c.region&&c.edible)witness(f,`COL-${c.region}:practice.order`,seq,e.instanceId);break;
      }
      case 'orderGroupWitness':{
        if(e.templateId==='O07'&&e.groupId==='O07-G1'&&total(e.byKey)>=6&&Object.values(e.byKey).filter(n=>n>0).length>=2)witness(f,'RG3-1:order',seq,e.instanceId);break;
      }
      case 'orderComplete':{
        if(!REGIONAL.orders.some(o=>o.id===e.templateId))throw Error('采购模板事实无效');add(f.orderTemplateCounts,e.templateId,1);witness(f,`${e.templateId}:complete`,seq,e.instanceId);
        if(e.templateId==='O06'&&['V','R','T','B'].includes(e.region))witness(f,`O06:${e.region}:complete`,seq,e.instanceId);break;
      }
      case 'orderDisplay':if(e.templateId!=='O04')throw Error('展示事实无效');else{witness(f,'O04:display',seq,e.instanceId);witness(f,'SP-SHAPE:practice',seq,e.instanceId);break;}
      case 'tripComplete':tripWitness(s,f,e,seq);break;
      case 'cargoExchange':if(e.cardId!=='B-E1'||e.quantity!==6)throw Error('带货事实无效');else{witness(f,'B-E1:cargoExchange',seq,e.tripId);break;}
      case 'materialBatch':for(const id of e.materialIds){if(!Number.isInteger(id)||id<0||id>82)throw Error('材料事实无效');add(f.materialBatches,String(id),1);}break;
      case 'regionalMaterialBatch':if(e.materialId!==79)throw Error('地区试做材料事实无效');else{witness(f,'RG3-2:regionalBatch',seq,e.batchId);break;}
      case 'projectPayment':{if(!REGIONAL.projects.some(p=>p.stages.some(stage=>stage.id===e.stageId)))throw Error('项目事实无效');add(f.payments,e.stageId,quantity(e.amount));break;}
      case 'projectDelivery':{species(e.key);if(!REGIONAL.projects.some(p=>p.stages.some(stage=>stage.id===e.stageId)))throw Error('项目事实无效');f.projectDeliveries[e.stageId]??={};add(f.projectDeliveries[e.stageId],e.key,quantity(e.quantity));break;}
      case 'collected':species(e.key);break; // Root total owns collection history.
      case 'identified':if(!REGIONAL.materials.some(m=>m.id===e.materialId))throw Error('辨认事实无效');break;
      default:throw Error('未知事实事件 '+e.kind);
    }
  }
  return f;
}
