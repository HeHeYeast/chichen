import {REGIONAL,CONTENT_TEXT} from './content-registry.js';
import {businessUnlockInfo} from './menu-model.js';
import {menuOverview} from './business-advisor.js';
import {menuCore} from './business-home.js';

// A factual next step, recomputed from this save. No global tutorial flag can
// lock an old save, and reading a visitor never awards or consumes anything.
export function loopGuide(s){
  if(!businessUnlockInfo(s).met)return {id:'first-sale',text:'先交第一笔生意，之后就能自己摆摊。',action:'看需求',target:'story'};
  if(!s.expansion.regions.introSpecimenDone.includes('V')&&s.events.loopVisitorMet)return {id:'valley-trip',text:'去谷地走走，第一趟就能带回一份地方食材。',action:'去寻访',target:'journey'};
  if(!s.expansion.regions.introSpecimenDone.includes('V'))return {id:'valley',text:'老街坊带了条消息来，听听她说什么。',action:'聊两句',target:'visitor'};
  if(!(s.total['0:128']>0))return {id:'first-partner',text:'地方食材带回来了，去线索册看看荠菜煎饼的做法。',action:'看线索',target:'clue'};
  if(!Object.values(s.expansion.facts.menuWitnesses??{}).some(w=>w.completeCount>0))return {id:'combination',text:'把新伙伴和已有出品搭成菜单，凑齐后售价增加 25%。',action:'去摆货',target:'stock'};
  if(!Object.values(s.expansion.facts.orderTemplateCounts??{}).some(n=>n>0))return {id:'orders',text:'常客也会留采购需求，库存够了就能交付。',action:'看订单',target:'orders'};
  return null;
}
export function discoveryCombinations(s,keys){
  if(!businessUnlockInfo(s).met)return [];
  return menuOverview(s).filter(m=>m.roleKeys.some(k=>keys.includes(k))).map(m=>{
    const core=menuCore(s,m.id,m.plan?.stock??{});
    return core?.unlocked?{...core,description:CONTENT_TEXT[m.id]?.description??'',matched:REGIONAL.menus.find(x=>x.id===m.id).roles.flatMap(x=>x.allowed).filter(k=>keys.includes(k))}:null;
  }).filter(Boolean).sort((a,b)=>Number(b.complete)-Number(a.complete)||a.missing-b.missing).slice(0,3);
}
