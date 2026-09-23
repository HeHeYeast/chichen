// Farm buildings use world coordinates; the two lower actions use screen coordinates.
// Keep these targets shared by the Canvas painter and app input bindings.
export const FARM_WORLD={width:830,height:568,maxScroll:510};
export const FARM_ART={
  0:{file:'/web/art/farm-dawn-v8.png',label:'黎明',sky:'#f4ceb0',shadow:'#73865455'},
  10:{file:'/web/art/farm-day-v8.png',label:'白天',sky:'#94d9ef',shadow:'#57774355'},
  20:{file:'/web/art/farm-dusk-v8.png',label:'傍晚',sky:'#f5b18f',shadow:'#75634455'},
  30:{file:'/web/art/farm-night-v8.png',label:'夜晚',sky:'#4676b7',shadow:'#243d5b66'},
};
export const FARM_ART_FILES=Object.values(FARM_ART).map(art=>art.file);
export const FARM_ACTIONS=[
  {id:'farm:house',action:'harvest',label:'打开收成表',rect:{x:18,y:71,w:145,h:133},sign:{x:77,y:126,w:43,h:19},signText:'收成表'},
  {id:'farm:stall',action:'shop',label:'前往补给商店',rect:{x:214,y:116,w:107,h:82},sign:{x:250,y:172,w:43,h:20},signText:'商店'},
  {id:'farm:shrine',action:'activities',label:'打开神社委托簿',rect:{x:607,y:116,w:67,h:88},sign:{x:614,y:184,w:54,h:20},signText:'神社委托'},
  // Three memento slots; presentation only (no value, wear or sale).
  {id:'farm:display',action:'display',label:'打开纪念物陈列架',rect:{x:470,y:140,w:84,h:64},sign:{x:482,y:184,w:60,h:20},signText:'陈列架'},
];
export const FARM_RECT={
  repair:{x:10,y:458,w:100,h:36},
  harvest:{x:210,y:458,w:100,h:36},
  shrine:{x:119,y:456,w:82,h:36},
};
