// User-defined room progression; the recovered gameplay still uses levels 0..3.
export const KITCHEN_STAGES = [
  {level:0,title:'破旧茅草房屋',description:'破损茅草檐、歪木柱、修补墙与粗木桌。',background:'/web/art/kitchen-stage-0-v7.png',bed:'/web/art/stage-bed-0-v6.png',bedRect:[10,158,300,177],frontY:314,vessel:'/web/art/basket-v4.png',dirty:{wall:'#a58964',table:'#906f40',webs:[[61,98],[255,101]]}},
  {level:1,title:'略微整齐木板房屋',description:'完整木板墙、方木窗、简易搁架与平整木桌。',background:'/web/art/kitchen-stage-1-v7.png',bed:'/web/art/stage-bed-1-v6.png',bedParts:[['#back',30,161,258,26],['#cushion',30,187,258,127],['#front',30,314,258,25]],vessel:'/web/art/stage-vessels-v5.png#0',dirty:{wall:'#b18462',table:'#876846',webs:[[63,98],[253,102]]}},
  {level:2,title:'小康公寓厨房',description:'瓷砖、家用吊柜、抽油烟机与浅色台面。',background:'/web/art/kitchen-stage-2-v7.png',bed:'/web/art/stage-bed-2-v6.png',bedParts:[['#back',29,160,259,22],['#cushion',29,182,259,132],['#front',29,314,259,26]],vessel:'/web/art/stage-vessels-v5.png#1',dirty:{wall:'#8b9c80',table:'#927d58',webs:[[61,101],[254,99]]}},
  {level:3,title:'豪华精装厨房',description:'成套定制柜、石材台面与精致金属五金。',background:'/web/art/kitchen-stage-3-v7.png',bed:'/web/art/stage-facility-3-v6.png',bedParts:[['#back',30,148,258,39],['#cushion',30,187,258,127],['#front',30,314,258,29]],vessel:'/web/art/stage-vessels-v5.png#2',dirty:{wall:'#aa937b',table:'#a48164',webs:[[73,104],[253,103]]}},
];
export const kitchenStage = level => KITCHEN_STAGES[Math.max(0,Math.min(3,Math.trunc(Number(level)||0)))];

// Register the original plates without editing their pixels. Wall/table boundaries
// vary in the art, while the egg positions and working surface stay fixed in game.
Object.assign(KITCHEN_STAGES[0],{backgroundSize:[941,1672],backgroundRows:[598,1150]});
Object.assign(KITCHEN_STAGES[1],{backgroundSize:[941,1672],backgroundRows:[545,1085]});
Object.assign(KITCHEN_STAGES[2],{backgroundSize:[941,1672],backgroundRows:[539,1168]});
Object.assign(KITCHEN_STAGES[3],{backgroundSize:[941,1672],backgroundRows:[645,1156]});
export const kitchenBackgroundParts = stage => stage.backgroundRows
  ? [['#wall',0,0,320,170],['#table',0,170,320,204],['#base',0,374,320,194]]
  : [['',0,0,320,568]];
