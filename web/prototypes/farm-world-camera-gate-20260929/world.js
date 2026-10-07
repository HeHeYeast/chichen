export const UNIT=32;
export const VIEW={width:390,height:696};
export const DEFAULT_ZOOM=.625;
export const MAX_ZOOM=1;
export const WORLDS={a:{id:'a',name:'A · 紧凑大世界',width:40,height:72},b:{id:'b',name:'B · 舒展大世界',width:56,height:100}};
export const SIZE={house:10,shop:7.6,shrine:7.4,display:9,'tree-round':4.6,'tree-pine':4.4,'bush-low':1.65,'bush-tall':1.5,'rock-wide':1.1,'rock-tall':1.05,'fence-front':2.2,'fence-return':2.2,bench:1.7,basin:.95,campfire:1.1};
export const NAMES={house:'农舍',shop:'补给摊',shrine:'神社',display:'陈列亭'};
export const DETAILS={house:'完好',shop:'物资',shrine:'祈愿',display:'收藏'};
export function layout(world){
 const {width:w,height:h}=world;
 const buildings=[['house',.37*w,.35*h],['shop',.65*w,.43*h],['shrine',.73*w,.77*h],['display',.29*w,.76*h]].map(([id,x,y])=>({id,x,y,width:SIZE[id]}));
 const chickens=[[0,.45,.47,1],[3,.575,.54,1.02],[4,.425,.58,.94],[0,.7,.485,1],[3,.60,.653,1.04],[4,.30,.75,.94],[0,.725,.83,1]].map(([id,x,y,height])=>({id,x:x*w,y:y*h,height}));
 const environment=[];
 const add=(id,x,y,width=SIZE[id])=>environment.push({id,x,y,width});
 // Repeated assets form a world boundary. Interior space stays quiet.
 for(let x=-1,i=0;x<w+3;x+=3.3,i++){add(i%3?'tree-round':'tree-pine',x,3+(i%3)*.65,4.5+(i%2)*.5);add(i%2?'tree-pine':'tree-round',x+1,h+1-(i%3)*.6,4.8);}
 for(let y=6,i=0;y<h-1;y+=4.2,i++){add(i%2?'tree-round':'tree-pine',.6+(i%3)*.45,y,4.4);add(i%3?'tree-pine':'tree-round',w-.4-(i%2)*.6,y+1.5,4.8);if(i%2===0){add('bush-low',3.1,y+1.3);add('rock-wide',w-3,y+2.3);}}
 for(const b of buildings){add('tree-round',b.x-4.4,b.y-5.7);add('tree-pine',b.x+3.4,b.y-6.3);add('bush-low',b.x+4.3,b.y+1.3);add('fence-front',b.x-4.2,b.y-1.1);}
 add('bench',.29*w,.52*h);add('basin',.68*w,.55*h);add('campfire',.52*w,.68*h);
 add('fence-return',.79*w,.73*h);add('fence-front',.22*w,.81*h);add('rock-wide',.81*w,.64*h);add('bush-tall',.20*w,.60*h);
 const defaultCenter={x:buildings[0].x+4.4,y:buildings[0].y+5};
 return {buildings,chickens,environment,defaultCenter};
}
export function minZoom(world){return Math.min((VIEW.width-8)/(world.width*UNIT),(VIEW.height-8)/(world.height*UNIT));}
export function clampCamera(camera,world){
 camera.zoom=Math.max(minZoom(world),Math.min(MAX_ZOOM,camera.zoom));
 for(const [axis,extent,pixels] of [['x',world.width,VIEW.width],['y',world.height,VIEW.height]]){
  const half=pixels/(UNIT*camera.zoom*2);
  camera[axis]=half*2>=extent?extent/2:Math.max(half,Math.min(extent-half,camera[axis]));
 }
 return camera;
}
export function worldPoint(camera,x,y){const s=UNIT*camera.zoom;return{x:camera.x+(x-VIEW.width/2)/s,y:camera.y+(y-VIEW.height/2)/s};}
export function zoomAt(camera,world,zoom,x=VIEW.width/2,y=VIEW.height/2){
 const p=worldPoint(camera,x,y);camera.zoom=Math.max(minZoom(world),Math.min(MAX_ZOOM,zoom));
 camera.x=p.x-(x-VIEW.width/2)/(UNIT*camera.zoom);camera.y=p.y-(y-VIEW.height/2)/(UNIT*camera.zoom);
 return clampCamera(camera,world);
}
