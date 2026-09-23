import {REGIONAL,CONTENT_TEXT} from './content-registry.js';
import {speciesView} from './collection-ui.js';
import {freeCount,homeCount} from './inventory.js';
import {businessCapacity,prepareBusiness,BUSINESS_WINDOW_MS,ACTIVE_BUSINESS_MENUS,businessBonusQuote} from './business.js';
import {businessUnlockInfo,menuFit,menuUnlockInfo} from './menu-model.js';

export function businessModel(s,now){
  const b=s.expansion.business,active=b?.active,report=b?.lastReport;
  const visibleRow=(key,quantity)=>{const [egg,id]=key.split(':').map(Number);return {...speciesView(s,egg,id),quantity,free:freeCount(s,key),home:homeCount(s,key)};};
  const menus=REGIONAL.menus.filter(m=>ACTIVE_BUSINESS_MENUS.includes(m.id)).map(m=>({id:m.id,name:CONTENT_TEXT[m.id].name,completeText:CONTENT_TEXT[m.id].complete,unlock:menuUnlockInfo(s,m.id),roles:m.roles.map(role=>({id:role.id,required:role.required,candidates:role.allowed.map(key=>visibleRow(key,0)).filter(row=>row.known&&row.free>0)}))}));
  return {access:businessUnlockInfo(s),capacity:businessCapacity(s),menus,
    active:active?{id:active.id,menuId:active.menuId,name:CONTENT_TEXT[active.menuId].name,startAt:active.startAt,hardEndAt:active.hardEndAt,nextAt:Math.min(active.hardEndAt,active.startAt+(active.processedWindow+1)*BUSINESS_WINDOW_MS),remainingMs:Math.max(0,active.hardEndAt-now),
      stock:Object.entries(active.stock).map(([key,n])=>visibleRow(key,n)),sold:active.totalSold,income:active.baseCP+active.markupCP+active.themeCP,creditReserve:active.creditReserve,
      projectedBonus:businessBonusQuote(active.rewards,active.soldByKey,active.creditReserve).bonusCP,fit:menuFit(active.menuId,active.stock,active.roles,active.snapshot),visitorEvents:active.visitorEvents}:null,
    report:report?{...structuredClone(report),name:CONTENT_TEXT[report.menuId].name,entries:Object.entries(report.soldByKey).map(([key,n])=>visibleRow(key,n))}:null};
}
export function businessPreparationModel(s,options){try{const plan=prepareBusiness(s,options);return {ready:true,missing:[],...plan};}catch(error){return {ready:false,missing:[error.message]};}}
