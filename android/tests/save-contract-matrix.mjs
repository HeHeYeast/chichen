import assert from 'node:assert/strict';
import {freshState,normalizeSave} from '../../web/engine.js';
import {REGIONAL,speciesByKey} from '../../web/content-registry.js';
import {syncProgress} from '../../web/progression.js';
import {openBusiness,advanceBusiness,closeBusiness} from '../../web/business.js';
import {orderOptions,acceptProposal,deliverOrderGroups} from '../../web/orders.js';
// Broad legal boundary fixtures are deliberately separate from real progression
// trajectories: their mature seed is synthetic, their frozen tickets use producers.
export function contractMatrix(now){
  const rows=[],base=freshState(now,55555);
  base.cp=900000;base.kitchenLevel=3;base.duck=true;base.toolLevels.fill(2);
  base.farm=Object.fromEntries(Object.keys(speciesByKey).map(k=>[k,100]));base.total={...base.farm,'0:0':10000};
  base.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};
  base.expansion.regions.guideFlags=['GUIDE-B'];
  for(const c of REGIONAL.cards)base.expansion.discovery.cards[c.id]=++base.meta.factSeq;
  for(const m of REGIONAL.materials)base.expansion.discovery.identified[m.id]=++base.meta.factSeq;
  base.expansion.methods.directions=REGIONAL.recipes.map(r=>r.id);base.expansion.methods.full=[...base.expansion.methods.directions];
  syncProgress(base);normalizeSave(base,now);
  const keep=(name,state)=>rows.push({name,state:normalizeSave(state,state.lastSeen)});
  for(const menu of REGIONAL.menus){
    const s=structuredClone(base),keys=[];for(const role of menu.roles)for(const key of role.allowed)if(!keys.includes(key)&&keys.length<6){keys.push(key);if(keys.filter(k=>role.allowed.includes(k)).length>=2)break;}
    // Place at least one distinct key in every required role before spare slots.
    const required=[];for(const role of menu.roles){const key=role.allowed.find(k=>!required.includes(k));if(key)required.push(key);}
    const stock=Object.fromEntries([...new Set([...required,...keys])].slice(0,6).map(k=>[k,12]));
    openBusiness(s,{menuId:menu.id,stock},now);keep(menu.id+'-open',s);
    advanceBusiness(s,now+7200000);keep(menu.id+'-2h',s);
    const manual=structuredClone(s);closeBusiness(manual,now+7200001);keep(menu.id+'-manual',manual);
    advanceBusiness(s,now+86400000);keep(menu.id+'-24h',s);
  }
  const variants=new Set();
  for(const def of REGIONAL.orders)for(const duck of [false,true]){
    const seed=structuredClone(base);seed.duck=duck;seed.egg=0;
    for(const option of orderOptions(seed,def.id,now)){
      const name=option.variantId+(option.region?':'+option.region:'')+(option.chapters?':'+option.chapters.join('-'):'');if(variants.has(name))continue;variants.add(name);
      const s=structuredClone(seed);s.expansion.orders.proposalSequence=1;s.expansion.orders.proposals=[{id:'proposal-1',templateId:def.id,reason:'batch'}];
      acceptProposal(s,'proposal-1',option,now);keep(name+'-accepted',s);
      if(def.kind==='purchase'){const a=s.expansion.orders.active[0],g=a.groups[0];deliverOrderGroups(s,a.id,[{groupId:g.id,key:g.allowed[0],quantity:1}],now);keep(name+'-partial',s);}
    }
  }
  for(const def of REGIONAL.orders)for(const variant of def.variants)assert([...variants].some(id=>id===variant.id||id.startsWith(variant.id+':')),`${variant.id} lacks a legal matrix option`);
  // A safe-integer baseline remains valid after 32-bit JVM integer range.
  const high=structuredClone(base),stages=REGIONAL.regulars.find(r=>r.id==='RG2').stages;
  for(const stage of stages.slice(0,3))high.expansion.collections.entitlements[stage.reward]={seq:++high.meta.factSeq,source:stage.id};
  high.expansion.regulars.RG2={readStages:stages.slice(0,3).map(s=>s.id),pendingStage:null,activatedSeq:high.meta.factSeq,baselines:{O05:2147483648},lastVisit:null};keep('regular-safe-integer-baseline',high);
  return rows;
}
