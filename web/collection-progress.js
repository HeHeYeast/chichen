// Collections (8 theme, 4 region, 4 special), their paper/stamp/memento
// entitlements and the three display slots. Progress reads permanent facts
// only; granting is a separate idempotent step that never pays CP and never
// touches the legacy seasonal/shrine claims.
import {REGIONAL,resolveSpecies} from './content-registry.js';
import {speciesDiscovered} from './species-state.js';
import {newOperationsEnabled} from './rollback-policy.js';

export const COLLECTIONS_VERSION=1,DISPLAY_SLOTS=3;
export function freshCollections(){return {version:COLLECTIONS_VERSION,entitlements:{},display:[null,null,null]};}

const known=(s,key)=>{const c=resolveSpecies(key);return !!c&&speciesDiscovered(s,c.egg,c.id);};
const count=(s,keys)=>keys.filter(k=>known(s,k)).length;
const witness=(s,id)=>!!s.expansion?.facts?.predicateWitnesses?.[id];
const card=(s,id)=>Object.hasOwn(s.expansion?.discovery?.cards??{},id);
const identified=(s,id)=>Object.hasOwn(s.expansion?.discovery?.identified??{},String(id));
const species=region=>REGIONAL.species.filter(x=>x.region===region).map(x=>x.key);

// Special pages, transcribed once per identity (see content-pack collections.md).
export const SPECIAL_RULES=Object.freeze({
  'SP-ALL':{open:3,complete:6,fixedCards:null},
  'SP-LEAF':{open:2,complete:4,fixedCards:['V-S1','R-S2','T-S1','B-S2']},
  'SP-TABLE':{perEgg:{open:1,complete:3}},
  'SP-SHAPE':{open:3,complete:8},
});
const THEME_CHAPTERS={'COL-8':{stage1:2,stage2:3}};

function themeProgress(s,c){
  const pool=[...new Set(c.optional.allowed)],have=count(s,pool),rep=c.fixed.allowed.some(k=>known(s,k));
  const chapters=THEME_CHAPTERS[c.id]?new Set(pool.filter(k=>known(s,k)).map(k=>resolveSpecies(k).season).filter(Boolean)).size:null;
  const stage=(n,chapterMin)=>({met:rep&&have>=n&&(chapterMin===undefined||chapters>=chapterMin),current:have,target:n});
  const need=THEME_CHAPTERS[c.id];
  const [first,second,third]=c.rewards;
  return {id:c.id,kind:'theme',menu:c.menu,region:c.region,representative:rep,chapters,
    stages:[{id:`${c.id}:1`,reward:first.id,...stage(c.optional.countStage1,need?.stage1)},{id:`${c.id}:2`,reward:second.id,...stage(c.optional.countStage2,need?.stage2)}],
    practice:{reward:third.id,met:witness(s,`${c.menu}:validService`)||witness(s,`${c.id}:practice.trip`),sources:{business:witness(s,`${c.menu}:validService`),trip:witness(s,`${c.id}:practice.trip`)}},
    fullMenu:{reward:`FULLSTAMP-${c.id}`,met:witness(s,`${c.menu}:completeService`)},
    guests:(c.displayGuests??[]).filter(k=>known(s,k)).length};
}
function regionProgress(s,c){
  const region=c.region,keys=species(region),have=count(s,keys),materials=REGIONAL.materials.filter(m=>m.region===region).map(m=>m.id);
  const lore=REGIONAL.cards.filter(x=>x.region===region&&x.type==='lore').map(x=>x.id),cards=REGIONAL.cards.filter(x=>x.region===region).map(x=>x.id);
  const oldPartner=c.oldKeys.some(k=>known(s,k));
  const [a,b,full]=c.stages;
  return {id:c.id,kind:'region',region,collected:have,total:keys.length,
    stages:[
      {id:a.id,reward:a.reward,met:materials.some(id=>identified(s,id))&&have>=c.optional.countStage1&&oldPartner,current:have,target:c.optional.countStage1},
      {id:b.id,reward:b.reward,met:materials.every(id=>identified(s,id))&&have>=c.optional.countStage2&&lore.some(id=>card(s,id)),current:have,target:c.optional.countStage2},
      {id:full.id,reward:full.reward,met:have===keys.length&&cards.every(id=>card(s,id))&&materials.every(id=>identified(s,id)),current:have,target:keys.length},
    ],
    practice:{reward:c.practiceReward,met:['business','order','trip'].some(k=>witness(s,`COL-${region}:practice.${k}`)),sources:Object.fromEntries(['business','order','trip'].map(k=>[k,witness(s,`COL-${region}:practice.${k}`)]))}};
}
function specialProgress(s,sp){
  const rule=SPECIAL_RULES[sp.id],pool=[...sp.oldKeys,...sp.newKeys];
  let open,complete,current,target;
  if(rule.perEgg){
    const edible=pool.filter(k=>resolveSpecies(k).edible&&known(s,k)),chicks=edible.filter(k=>k.startsWith('0:')).length,ducks=edible.filter(k=>k.startsWith('1:')).length;
    open=chicks>=rule.perEgg.open&&ducks>=rule.perEgg.open;complete=chicks>=rule.perEgg.complete&&ducks>=rule.perEgg.complete;current=Math.min(chicks,ducks);target=rule.perEgg.complete;
  }else{
    const fixed=!rule.fixedCards||rule.fixedCards.some(id=>card(s,id));current=count(s,pool);target=rule.complete;
    open=fixed&&current>=rule.open;complete=fixed&&current>=rule.complete;
  }
  const footprints=sp.id==='SP-ALL'?Object.keys(s.expansion?.facts?.companionFirst??{}).length:null;
  return {id:sp.id,kind:'special',current,target,footprints,
    stages:[{id:`${sp.id}:open`,reward:`PAGE-${sp.id}`,met:open},{id:`${sp.id}:complete`,reward:`BORDER-${sp.id}`,met:complete}],
    practice:{reward:`STAMP-${sp.id}`,met:witness(s,`${sp.id}:practice`)}};
}

export function collectionProgress(s,id){
  const c=REGIONAL.collections.find(x=>x.id===id);if(c)return c.kind==='theme'?themeProgress(s,c):regionProgress(s,c);
  const sp=REGIONAL.specials.find(x=>x.id===id);if(sp)return specialProgress(s,sp);
  throw Error('没有这本收藏册');
}
export const allCollections=()=>[...REGIONAL.collections.map(c=>c.id),...REGIONAL.specials.map(s=>s.id)];

// Every grantable identity with its kind and the fact it follows. Paper notes
// from orders follow the permanent completion witnesses written by E.
export const ENTITLEMENTS=Object.freeze((()=>{
  const list=[];
  for(const c of REGIONAL.collections){
    if(c.kind==='theme'){for(const r of c.rewards)list.push({id:r.id,kind:r.kind==='memento'?'memento':r.kind==='stamp'?'stamp':'paper',source:c.id});list.push({id:`FULLSTAMP-${c.id}`,kind:'stamp',source:c.id});}
    else{for(const st of c.stages)list.push({id:st.reward,kind:'paper',source:c.id});list.push({id:c.practiceReward,kind:'stamp',source:c.id});}
  }
  for(const sp of REGIONAL.specials)for(const [id,kind] of [[`PAGE-${sp.id}`,'paper'],[`BORDER-${sp.id}`,'paper'],[`STAMP-${sp.id}`,'stamp']])list.push({id,kind,source:sp.id});
  for(const p of REGIONAL.paperRecords)if(p.source.startsWith('O'))list.push({id:p.id,kind:'paper',source:p.source});
  return list.map(Object.freeze);
})());
const ENTITLEMENT_BY_ID=new Map(ENTITLEMENTS.map(e=>[e.id,e]));
// Regular-stage notes and M09–M12 are granted by Work I through the same ledger.
export const isEntitlement=id=>ENTITLEMENT_BY_ID.has(id)||REGIONAL.paperRecords.some(p=>p.id===id)||REGIONAL.mementos.some(m=>m.id===id);

export function grantEntitlementOnce(s,id,source){
  const c=s.expansion.collections;if(!c)throw Error('收藏尚未完成存档迁移');
  if(!isEntitlement(id))throw Error('未知的收藏成果');
  if(Object.hasOwn(c.entitlements,id))return false;
  const seq=s.meta.factSeq+1;if(!Number.isSafeInteger(seq))throw Error('事实序号超出范围');s.meta.factSeq=seq;
  c.entitlements[id]={seq,source};return true;
}

// Pure-collection and practice results are registered by fact, never by CP;
// repeating the reconciliation, reading pages or moving displays grants nothing.
export function reconcileEntitlements(s){
  if(!newOperationsEnabled('collections'))return [];
  if(!s.expansion?.collections)return [];
  const granted=[];
  const give=(met,id,source)=>{if(met&&grantEntitlementOnce(s,id,source))granted.push(id);};
  for(const id of allCollections()){
    const p=collectionProgress(s,id);
    for(const st of p.stages)give(st.met,st.reward,id);
    give(p.practice.met,p.practice.reward,id);
    if(p.fullMenu)give(p.fullMenu.met,p.fullMenu.reward,id);
  }
  for(const p of REGIONAL.paperRecords)if(p.source.startsWith('O'))give(witness(s,`${p.source}:complete`)||(p.source==='O04'&&witness(s,'O04:display')),p.id,p.source);
  return granted;
}

export const ownedMementos=s=>REGIONAL.mementos.map(m=>m.id).filter(id=>Object.hasOwn(s.expansion?.collections?.entitlements??{},id));
// Display is presentation only: no value, wear, sale or reward on change.
export function setDisplay(s,slot,mementoId){
  const c=s.expansion.collections;if(!Number.isSafeInteger(slot)||slot<0||slot>=DISPLAY_SLOTS)throw Error('陈列位无效');
  if(mementoId!==null){if(!ownedMementos(s).includes(mementoId))throw Error('这件纪念物还没有收入册中');const other=c.display.indexOf(mementoId);if(other>=0&&other!==slot)c.display[other]=null;}
  c.display[slot]=mementoId;return [...c.display];
}

export function validateCollectionsState(s,fail){
  const c=s.expansion.collections;
  if(!c||typeof c!=='object'||Array.isArray(c)||Object.keys(c).sort().join()!=='display,entitlements,version')fail('收藏容器');
  if(c.version!==COLLECTIONS_VERSION)fail('收藏版本');
  if(!c.entitlements||typeof c.entitlements!=='object'||Array.isArray(c.entitlements))fail('收藏成果');
  const seqs=new Set();
  for(const [id,v]of Object.entries(c.entitlements)){
    if(!isEntitlement(id)||!v||typeof v!=='object'||Object.keys(v).sort().join()!=='seq,source'||!Number.isSafeInteger(v.seq)||v.seq<1||v.seq>s.meta.factSeq||seqs.has(v.seq)||typeof v.source!=='string'||!v.source.length||v.source.length>40)fail('收藏成果身份');
    seqs.add(v.seq);
  }
  if(!Array.isArray(c.display)||c.display.length!==DISPLAY_SLOTS)fail('陈列位');
  const shown=c.display.filter(x=>x!==null);
  if(new Set(shown).size!==shown.length||shown.some(id=>!REGIONAL.mementos.some(m=>m.id===id)||!Object.hasOwn(c.entitlements,id)))fail('陈列纪念物');
  return true;
}
