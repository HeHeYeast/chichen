// Additional destinations reuse existing ingredients and partner identities.
// Their two gathering tiers follow the same shop-supply conditions as cooking.
export const EXTRA_REGIONS=Object.freeze([
  {id:'O',name:'果园小径',short:'果园',route:'orchard',hours:6,materials:[1,44],collected:120,discoveries:12,kitchenLevel:1,environment:'yard',keys:['0:6','1:5','0:122','1:59','1:42'],hint:'树下找柠檬，果园深处还有柳橙。'},
  {id:'H',name:'山间菌圃',short:'菌圃',route:'mushroom',hours:10,materials:[29,35],collected:240,discoveries:20,kitchenLevel:1,environment:'wood',keys:['0:42','0:47','1:62','1:63'],hint:'坡边种着生姜，林下可以寻找菌种。'}
]);
export const extraRegion=id=>EXTRA_REGIONS.find(r=>r.id===id);
export const EXTRA_ROUTES=Object.freeze(EXTRA_REGIONS.map(r=>Object.freeze({id:r.route,name:r.name,hours:r.hours,baseUnits:2,pool:r.materials,requiredCollected:r.collected,requiredDiscoveries:r.discoveries,environment:r.environment,kitchenLevel:r.kitchenLevel})));
