import {REGIONAL,REQUIREMENTS,resolveSpecies} from './content-registry.js';
import {collectedTotal,discoveryCount} from './progression.js';
import {freeCount,homeCount,usableByOwner} from './inventory.js';
import {availableIngredientIds} from './ingredient-unlocks.js';
import {menuFit,menuDefinition,legacyRouteOpen} from './menu-model.js';
import {regionInfo} from './region-model.js';
import {recipePaths} from './recipe-book.js';

// The only evaluable leaves; anything else (including 'unavailable') fails closed.
export const REQUIREMENT_KINDS=Object.freeze(['all','any','discoveredCount','identified','cardOwned','toolAtLeast','kitchenAtLeast','supplyOpen','methodKnown','routeOpen','regionOpen','stockAtLeast','counterAtLeast','witnessExists','stageDone','stageRead','menuRoleDiscovered','menuComplete']);
const discovered=(s,key)=>(s.total?.[key]??0)>0||(s.farm?.[key]??0)>0;
export function evaluate(requirement,s,context={}){
  const r=typeof requirement==='string'?REQUIREMENTS[requirement]:requirement;
  if(!r)return {met:false,current:0,target:1,missing:['尚未定义的条件'],actionRef:null};
  if(!REQUIREMENT_KINDS.includes(r.kind))return {met:false,current:0,target:r.target??1,missing:[r.reason??'尚未开放的条件'],actionRef:null};
  if(r.kind==='all'||r.kind==='any'){
    const children=(r.children??[]).map(child=>evaluate(child,s,context)),met=r.kind==='all'?children.every(x=>x.met):children.some(x=>x.met);
    return {met,current:children.filter(x=>x.met).length,target:r.kind==='all'?children.length:1,missing:met?[]:children.filter(x=>!x.met).flatMap(x=>x.missing),actionRef:children.find(x=>!x.met)?.actionRef??null,children};
  }
  let current=0,target=r.target??1;const f=s.expansion?.facts;
  switch(r.kind){
    case 'unavailable':break;
    case 'discoveredCount':{const allowed=r.allowed??(r.selector?REGIONAL.selectors[r.selector]:null);current=allowed?allowed.filter(k=>discovered(s,k)).length:discoveryCount(s);break;}
    case 'identified':current=Object.hasOwn(s.expansion.discovery.identified,String(r.id))?1:0;break;
    case 'cardOwned':current=Object.hasOwn(s.expansion.discovery.cards,r.id)?1:0;break;
    case 'toolAtLeast':current=s.toolLevels[r.id]??-1;target=r.level;break;
    case 'kitchenAtLeast':current=s.kitchenLevel;target=r.level;break;
    case 'supplyOpen':current=availableIngredientIds(s).includes(r.id)?1:0;break;
    case 'methodKnown':current=s.expansion.methods.full.includes(r.id)||s.progress.knowledge.recipes.includes(r.id)?1:0;break;
    // routeOpen: the legacy route (bay: GUIDE-B); regionOpen: the regional gate of a released region.
    case 'routeOpen':current=legacyRouteOpen(s,r.id)?1:0;break;
    case 'regionOpen':current=regionInfo(s,r.id).met?1:0;break;
    case 'stockAtLeast':if(!resolveSpecies(r.key))break;else{current=r.view==='home'?homeCount(s,r.key):r.view==='owner'?usableByOwner(s,r.key,r.owner):r.view==='free'?freeCount(s,r.key):0;break;}
    case 'counterAtLeast':{
      if(r.source==='collected')current=collectedTotal(s);else{
        const maps={business:f?.businessCounts,orders:f?.orderCounts,orderTemplates:f?.orderTemplateCounts,materialBatches:f?.materialBatches};
        const sources=r.sources??[r.source];if(!Array.isArray(sources)||!sources.length||sources.some(source=>!Object.hasOwn(maps,source)))return {met:false,current:0,target,missing:['未知事实来源'],actionRef:null};
        const map={};for(const source of new Set(sources))for(const [key,n]of Object.entries(maps[source]??{}))map[key]=(map[key]??0)+n;
        const allowed=r.allowed??(r.selector?REGIONAL.selectors[r.selector]:Object.keys(map));current=allowed.reduce((n,k)=>n+Math.max(0,(map[k]??0)-(r.since?.[k]??0)),0);
        if(r.minimumDistinct&&allowed.filter(k=>(map[k]??0)-(r.since?.[k]??0)>0).length<r.minimumDistinct)current=0;
      }break;
    }
    case 'witnessExists':{const w=f?.predicateWitnesses[r.id];current=w&&(r.sinceSeq===undefined||w.lastSeq>r.sinceSeq)?1:0;break;}
    case 'stageDone':current=(r.scope==='legacyOrder'?s.progress.orders[r.id]?.completed:s.expansion.projects?.[r.projectId]?.stages?.[r.id]?.complete)?1:0;break;
    case 'stageRead':current=s.expansion.regulars?.[r.regularId]?.readStages?.includes(r.id)?1:0;break;
    case 'menuRoleDiscovered':{const role=menuDefinition(r.menuId)?.roles.find(x=>x.id===r.roleId);current=role?.allowed.some(k=>discovered(s,k)&&(r.toolId===undefined||recipePaths(k).some(path=>path.toolId===r.toolId)))?1:0;break;}
    case 'menuComplete':current=context.stock&&context.roles&&context.snapshot&&menuFit(r.menuId,context.stock,context.roles,context.snapshot).complete?1:0;break;
    default:break; // Unknown predicate always fails closed.
  }
  const met=current>=target;return {met,current,target,missing:met?[]:[r.label??r.id??r.kind],actionRef:r.actionRef??null};
}

export function projectProgress(def,instance,s){return {id:def.id,stages:(def.stages??[]).map(stage=>({id:stage.id,done:instance?.stages?.[stage.id]?.complete===true,requirement:evaluate(stage.checkRequirement??stage.requirement,s),costCP:stage.costCP??0,paid:instance?.payments?.[stage.id]??0}))};}
