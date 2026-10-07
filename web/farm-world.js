import {inventoryView} from './inventory.js';
import {resolveSpecies} from './content-registry.js';

// Coordinates refer to the accepted, unmodified 1536 × 1024 map.
export const FARM_MAP='/web/art/farm/camp-map.png';
export const WORLD={width:1536,height:1024};
// Approved 390 × 844 composition: map origin (-192, 93), map viewport y=97.
export const HOME_CAMERA={x:387/.7,y:338/.7,zoom:.7};
export const PLACES=[
  {id:'house',name:'仓库',description:'收取与存放',icon:'home',x:537,y:214,rect:[410,175,150,130]},
  {id:'market',name:'集市',description:'经营与补给',icon:'shop',x:1050,y:242,rect:[971,218,131,109]},
  {id:'shrine',name:'神社',description:'祈愿与回礼',icon:'shrine',x:1220,y:565,rect:[1144,537,122,126]},
  {id:'display',name:'陈列亭',description:'展示收藏',icon:'basket',x:463,y:609,rect:[328,592,153,110]},
  {id:'dock',name:'码头',description:'出发去寻访',icon:'explore',x:1100,y:818,rect:[989,797,128,109]},
];
export const RESIDENT_SPOTS=[[495,318],[1036,338],[628,480],[738,538],[907,483],[872,558],[462,710],[1170,680]];
export function farmStock(state){
  return Object.keys(state.farm??{}).filter(key=>resolveSpecies(key)).sort((a,b)=>a.localeCompare(b,undefined,{numeric:true})).map(key=>({key,...inventoryView(state,key)}));
}
export function residentsFor(state){
  const stock=farmStock(state).filter(r=>r.home>0),remaining=stock.map(r=>r.home),result=[];
  while(result.length<RESIDENT_SPOTS.length){
    let added=false;
    for(let i=0;i<stock.length&&result.length<RESIDENT_SPOTS.length;i++)if(remaining[i]>0){
      const [x,y]=RESIDENT_SPOTS[result.length];result.push({key:stock[i].key,x,y,height:19});remaining[i]--;added=true;
    }
    if(!added)break;
  }
  return result;
}
export const fitZoom=(w,h)=>Math.min(w/WORLD.width,h/WORLD.height);
export function clampCamera(camera,w,h){
  const zoom=Math.max(fitZoom(w,h),Math.min(1.6,camera.zoom)),hw=w/(2*zoom),hh=h/(2*zoom);
  return {zoom,x:hw>=WORLD.width/2?WORLD.width/2:Math.max(hw,Math.min(WORLD.width-hw,camera.x)),y:hh>=WORLD.height/2?WORLD.height/2:Math.max(hh,Math.min(WORLD.height-hh,camera.y))};
}
export function zoomCamera(camera,zoom,point,w,h){
  zoom=Math.max(fitZoom(w,h),Math.min(1.6,zoom));
  return clampCamera({zoom,x:camera.x+(point.x-w/2)/camera.zoom-(point.x-w/2)/zoom,y:camera.y+(point.y-h/2)/camera.zoom-(point.y-h/2)/zoom},w,h);
}
export function hitPlace(x,y){return PLACES.find(p=>x>=p.rect[0]&&x<=p.rect[0]+p.rect[2]&&y>=p.rect[1]&&y<=p.rect[1]+p.rect[3]);}
