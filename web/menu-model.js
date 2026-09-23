import {REGIONAL,resolveSpecies,CONTENT_TEXT} from './content-registry.js';
import {collectedTotal,discoveryCount} from './progression.js';
import {RULES} from './integration-data.js';
import {recipePaths} from './recipe-book.js';
import {regionInfo} from './region-model.js';

export const menuDefinition=id=>REGIONAL.menus.find(m=>m.id===id)??null;
const discovered=(s,key)=>(s.total?.[key]??0)>0||(s.farm?.[key]??0)>0;
// The original yard/water/wood routes open by collection and discoveries only;
// the bay route exists only after GUIDE-B. Regional gates are separate.
export function legacyRouteOpen(s,id){
  if(id==='B')return s.expansion?.regions?.guideFlags?.includes('GUIDE-B')===true;
  const route=RULES.exploration.routes.find(r=>r.id===REGIONAL.regions.find(x=>x.id===id)?.route);
  return !!route&&collectedTotal(s)>=route.requiredCollected&&discoveryCount(s)>=route.requiredDiscoveries;
}
export function businessUnlockInfo(s){
  const first=RULES.storyOrders[0].id;
  const missing=[];
  if(collectedTotal(s)<72)missing.push('累计收取72只');if(discoveryCount(s)<3)missing.push('发现3种伙伴');if(!s.progress.orders[first]?.completed)missing.push('完成第一笔旧采购');
  return {met:!missing.length,missing,actionRef:missing.some(x=>x.includes('采购'))?{page:'business',tab:'orders'}:{page:'kitchen'}};
}
export function menuUnlockInfo(s,menuId){
  const menu=menuDefinition(menuId);if(!menu)return {met:false,missing:['菜单不存在']};
  const roleKnown=roleId=>menu.roles.find(r=>r.id===roleId).allowed.some(key=>discovered(s,key));
  let met=false;
  switch(menuId){
    case 'MN1':return businessUnlockInfo(s);
    case 'MN2':met=roleKnown('MN2-R1')&&roleKnown('MN2-R2');break;
    case 'MN3':met=(s.toolLevels[8]??-1)>=0||menu.roles[0].allowed.some(key=>discovered(s,key)&&recipePaths(key).some(r=>r.toolId===2));break;
    case 'MN4':met=legacyRouteOpen(s,'R')&&roleKnown('MN4-R1')&&roleKnown('MN4-R2');break;
    case 'MN5':met=regionInfo(s,'T').met;break;
    case 'MN6':met=roleKnown('MN6-R1')&&roleKnown('MN6-R2');break;
    case 'MN7':met=legacyRouteOpen(s,'B');break;
    case 'MN8':met=REGIONAL.selectors.season.filter(key=>discovered(s,key)).length>=2;break;
  }
  return {met,missing:met?[]:[CONTENT_TEXT[menuId].unlock],actionRef:{page:menuId==='MN5'||menuId==='MN7'?'exploration':'catalog'}};
}

export function assignMenuRoles(menuId,stock,requested){
  const menu=menuDefinition(menuId);if(!menu)throw Error('菜单不存在');
  const keys=Object.keys(stock).filter(k=>stock[k]>0),used=new Set(),roles=[];
  if(requested!==undefined){
    if(!Array.isArray(requested))throw Error('菜单角色无效');
    const roleIds=new Set();
    for(const row of requested){const def=menu.roles.find(r=>r.id===row.roleId);if(!def||roleIds.has(row.roleId)||!Array.isArray(row.keys)||row.keys.length>def.maxSpecies)throw Error('菜单角色无效');roleIds.add(row.roleId);
      for(const key of row.keys){if(!keys.includes(key)||used.has(key)||!def.allowed.includes(key))throw Error('同一品种只能分配一个合适角色');used.add(key);}
      roles.push({roleId:row.roleId,keys:[...row.keys]});
    }
  }else {
    const required=menu.roles.filter(r=>r.required);
    function match(index,chosen){if(index===required.length)return chosen;for(const key of keys)if(!chosen.some(x=>x.key===key)&&required[index].allowed.includes(key)){const result=match(index+1,[...chosen,{roleId:required[index].id,key}]);if(result)return result;}return null;}
    const matched=match(0,[])??[];
    for(const def of menu.roles){const chosen=matched.filter(x=>x.roleId===def.id).map(x=>x.key);chosen.forEach(k=>used.add(k));roles.push({roleId:def.id,keys:chosen});}
    for(const def of menu.roles){const role=roles.find(r=>r.roleId===def.id);if(!role.keys.length){const key=keys.find(k=>!used.has(k)&&def.allowed.includes(k));if(key){role.keys.push(key);used.add(key);}}}
    for(const def of menu.roles){const role=roles.find(r=>r.roleId===def.id);const chosen=keys.filter(key=>!used.has(key)&&def.allowed.includes(key)).slice(0,def.maxSpecies-role.keys.length);chosen.forEach(k=>used.add(k));role.keys.push(...chosen);}
  }
  for(const def of menu.roles)if(!roles.some(r=>r.roleId===def.id))roles.push({roleId:def.id,keys:[]});
  const ordinary=keys.filter(key=>!used.has(key));if(ordinary.length)roles.push({roleId:'ordinary',keys:ordinary});
  return roles;
}

export function menuSnapshot(s,menuId,stock,roles){
  return {menuId,roles:structuredClone(roles),identified:Object.keys(s.expansion.discovery.identified),species:Object.fromEntries(Object.keys(stock).map(key=>{const c=resolveSpecies(key);return [key,{egg:c.egg,region:c.region??null,tags:[...(c.tags??[])],season:c.season??null,regionalMaterials:[...(c.unlock?.identifiedMaterials??[])]}];}))};
}

export function menuFit(menuId,stock,roles,snapshot){
  const def=menuDefinition(menuId);if(!def)throw Error('菜单不存在');
  const activeKeys=Object.keys(stock).filter(key=>stock[key]>0),required=def.roles.filter(r=>r.required);
  const availableRole=id=>roles.find(r=>r.roleId===id)?.keys.filter(k=>stock[k]>0)??[];
  const missing=required.filter(r=>!availableRole(r.id).length).map(r=>r.id);
  if(missing.length)return {tier:'ordinary',ratePercent:0,missing,complete:false,suitable:false};
  const assigned=roles.filter(r=>r.roleId!=='ordinary').flatMap(r=>r.keys).filter(k=>stock[k]>0);
  const qualifying=assigned.filter(k=>stock[k]>=3),traits=k=>snapshot.species[k],has=(keys,tag)=>keys.some(k=>traits(k)?.tags.includes(tag));
  let complete=false;
  switch(menuId){
    case 'MN1':complete=availableRole('MN1-R1').filter(k=>stock[k]>=6).length>=2;break;
    case 'MN2':complete=def.roles.every(r=>availableRole(r.id).some(k=>stock[k]>=3))&&qualifying.length>=3;break;
    case 'MN3':complete=qualifying.some(a=>qualifying.some(b=>a!==b&&traits(a)?.tags.includes('savory')&&traits(b)?.tags.includes('sweet')));break;
    case 'MN4':complete=qualifying.length>=3&&new Set(qualifying.map(k=>traits(k).egg)).size===2;break;
    case 'MN5':complete=qualifying.length>=3&&(has(qualifying,'floral')||has(qualifying,'roast'));break;
    case 'MN6':complete=qualifying.length>=3&&(has(qualifying,'ginger')||has(qualifying,'mushroom'));break;
    case 'MN7':complete=assigned.every(k=>stock[k]>=3)&&new Set(assigned.map(k=>traits(k).egg)).size===2&&assigned.some(k=>traits(k).tags.includes('bay')&&traits(k).regionalMaterials.some(id=>snapshot.identified.includes(String(id))));break;
    case 'MN8':complete=new Set(qualifying.map(k=>traits(k).season).filter(Boolean)).size>=3;break;
  }
  return {tier:complete?'complete':'suitable',ratePercent:complete?8:5,missing:[],complete,suitable:true,distinct:activeKeys.length};
}

export function menuSalesWitness(menuId,soldByKey,roleSales,fullSoldByKey,fullRoleSales){
  const menu=menuDefinition(menuId),sum=map=>Object.values(map).reduce((a,b)=>a+b,0),covered=map=>menu.roles.filter(r=>r.required).every(r=>(map[r.id]??0)>=1);
  return {valid:sum(soldByKey)>=6&&covered(roleSales),complete:sum(fullSoldByKey)>=6&&covered(fullRoleSales)};
}
