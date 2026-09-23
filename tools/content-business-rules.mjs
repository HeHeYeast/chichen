// Explicit structured supplements to content.json. No natural-language parsing.
// A menu's three conditions are compiled only in the Work that opens it: D opens
// MN1 (old stock), E opens MN2-MN8 with orders.
export const COMPILED_MENUS=Object.freeze(['MN1','MN2','MN3','MN4','MN5','MN6','MN7','MN8']);
const all=(...children)=>({kind:'all',children});
export const BUSINESS_REQUIREMENTS={
  'MN1:unlock':all({kind:'counterAtLeast',source:'collected',target:72},{kind:'discoveredCount',target:3},{kind:'stageDone',scope:'legacyOrder',id:'first-sale'}),
  'MN2:unlock':all({kind:'menuRoleDiscovered',menuId:'MN2',roleId:'MN2-R1'},{kind:'menuRoleDiscovered',menuId:'MN2',roleId:'MN2-R2'}),
  'MN3:unlock':{kind:'any',children:[{kind:'toolAtLeast',id:8,level:0},{kind:'menuRoleDiscovered',menuId:'MN3',roleId:'MN3-R1',toolId:2}]},
  'MN4:unlock':all({kind:'routeOpen',id:'R'},{kind:'menuRoleDiscovered',menuId:'MN4',roleId:'MN4-R1'},{kind:'menuRoleDiscovered',menuId:'MN4',roleId:'MN4-R2'}),
  'MN5:unlock':{kind:'regionOpen',id:'T'},
  'MN6:unlock':all({kind:'menuRoleDiscovered',menuId:'MN6',roleId:'MN6-R1'},{kind:'menuRoleDiscovered',menuId:'MN6',roleId:'MN6-R2'}),
  'MN7:unlock':{kind:'routeOpen',id:'B'},
  'MN8:unlock':{kind:'discoveredCount',selector:'season',target:2},
};
for(let i=1;i<=8;i++){
  BUSINESS_REQUIREMENTS[`MN${i}:complete`]={kind:'menuComplete',menuId:`MN${i}`,scope:'remainingStockAtWindowStart'};
  BUSINESS_REQUIREMENTS[`MN${i}:validService`]={kind:'witnessExists',id:`MN${i}:validService`,scope:'sameSession',minSold:6,minEachRequiredRole:1};
}
export const BUSINESS_FACT_DIMENSIONS=Object.freeze({
  businessSale:['sessionId','windowIndex','menuId','roleId','key','quantity','tier','baseCP','markupCP','themeCP'],
  businessWitness:['sessionId','menuId','soldByKey','roleSales','fullSoldByKey','fullRoleSales','valid','complete'],
  orderDelivery:['instanceId','templateId','variantId','region','groupId','key','quantity'],
  orderComplete:['instanceId','templateId','variantId','region','chapters','groupDeliveries'],
  tripComplete:['tripId','region','placeId','focus','cardId','members'],
});

export function compileBusinessCondition(input){
  const [id,field]=input.id.split(':');
  if(!COMPILED_MENUS.includes(id)||!['unlock','complete','validService'].includes(field))return null;
  const rule=BUSINESS_REQUIREMENTS[input.id];if(!rule)throw Error(`Missing business rule ${input.id}`);
  return {id:input.id,...structuredClone(rule),compiled:true,work:id==='MN1'?'D':'E',sourcePointer:input.sourcePointer};
}
