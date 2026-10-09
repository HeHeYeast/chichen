// Read-only projection of the four regulars for the 生意 → 常客 page and the
// business receipt. No affection bar, no ranking, no timers.
import {REGIONAL,CONTENT_TEXT} from './content-registry.js';
import {regularInfo,REGULAR_IDS} from './regulars.js';

const REGION_NAME={V:'谷地',R:'溪岸',T:'茶坡',B:'海湾'};
const stageText=id=>CONTENT_TEXT[id]??{};
const CARD_TYPE={specimen:'食材',lore:'线索',event:'事件'};
// Display only: authored text keeps content IDs; show names, keeping unfound
// cards and materials masked (the rule itself never reads this text).
export function readableRequirement(s,text){
  return String(text??'').replace(/带回并辨认/g,'带回').replace(/辨认/g,'找到').replace(/标本/g,'食材').replace(/(?<![A-Za-z0-9-])(MN\d|O\d\d|PJ-\d|M\d\d|ALT-[VRTB]|[VRTB]-[SNE]\d|RG\d(?!-)|GUIDE-B)(?![0-9])|(?<![\d.])(7[5-9]|8[0-2])(?![\d只种])/g,(all,id,material)=>{
    if(material){const m=REGIONAL.materials.find(x=>x.id===Number(material));const known=Object.hasOwn(s.expansion?.discovery?.identified??{},material)||Object.hasOwn(s.expansion?.discovery?.cards??{},m.specimen);return known?(CONTENT_TEXT[m.stableId]?.name??all):`${REGION_NAME[m.region]}新材料`;}
    if(/^[VRTB]-/.test(id)){const card=REGIONAL.cards.find(c=>c.id===id);const owned=Object.hasOwn(s.expansion?.discovery?.cards??{},id);return owned?`「${CONTENT_TEXT[id]?.title??id}」`:`${REGION_NAME[card.region]}${CARD_TYPE[card.type]}${"一二"[Number(id.at(-1))-1]}`;}
    if(id.startsWith('ALT-')){const alt=REGIONAL.alternatives.find(x=>x.id===id),seen=alt.ingredients.filter(n=>n>=75).every(n=>Object.hasOwn(s.expansion?.discovery?.identified??{},String(n)));return seen&&CONTENT_TEXT[id]?.name?`「${CONTENT_TEXT[id].name}」`:`${REGION_NAME[alt.region]}的地方做法`;}
    if(id==='GUIDE-B')return '沿湾路标';
    return CONTENT_TEXT[id]?.name?`「${CONTENT_TEXT[id].name}」`:all;
  });
}
export function regularsModel(s,pinned=null){
  const rows=REGULAR_IDS.map(id=>{
    const info=regularInfo(s,id),def=REGIONAL.regulars.find(r=>r.id===id),memento=CONTENT_TEXT[def.memento]?.name??def.memento;
    const pendingText=info.pending?stageText(info.pending.id):null;
    const stages=def.stages.map((st,i)=>{const read=info.readStages.includes(st.id),pending=info.pending?.id===st.id;
      return {id:st.id,index:i,title:stageText(st.id).title??st.id,status:read?'read':pending?'pending':i===info.readStages.length?'current':'later',
        text:read?stageText(st.id).text:null,next:read?readableRequirement(s,stageText(st.id).next):null,reward:st.reward,rewardName:CONTENT_TEXT[st.reward]?.name??st.reward};});
    const topic=info.complete?'已完成，可回看':info.pending?`新对话：${pendingText.title}`:info.stage?(info.gateMet?`想看：${info.stage.title}`:'还需一件事'):'';
    return {id,name:info.name,region:def.region,regionName:REGION_NAME[def.region],memento:def.memento,mementoName:memento,
      opened:info.opened,complete:info.complete,acquainted:info.acquainted,pinned:pinned===id,topic,
      current:info.stage&&!info.pending?{...info.stage,gate:info.gate,gateMet:info.gateMet,branches:info.branches.map(b=>({...b,text:readableRequirement(s,b.text)}))}:null,
      pending:info.pending?{id:info.pending.id,title:pendingText.title,text:pendingText.text,next:readableRequirement(s,pendingText.next),branch:info.pending.branch,
        branchText:readableRequirement(s,stageText(info.pending.id).alternatives?.[info.pending.branch]??'')}:null,
      stages,readCount:info.readStages.length,lastVisit:info.state?.lastVisit??null};
  });
  return {rows,unread:rows.filter(r=>r.pending).length,visible:rows.filter(r=>r.opened||r.readCount||r.pending)};
}
// Stories a finished business session actually presented (lastVisit is kept per regular).
export function reportVisitors(s,report){
  if(!report)return [];
  return REGULAR_IDS.map(id=>({id,state:s.expansion?.regulars?.[id]})).filter(x=>x.state?.lastVisit?.sessionId===report.id)
    .map(x=>({id:x.id,name:CONTENT_TEXT[x.id]?.name??x.id,stageId:x.state.lastVisit.stageId,title:stageText(x.state.lastVisit.stageId).title,unread:x.state.pendingStage?.id===x.state.lastVisit.stageId}));
}
