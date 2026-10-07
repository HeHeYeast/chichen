import {resolveSprite,spriteSVG} from '../../art/manifest.js';
import {characterImage} from '../../catalog.js';
// Native map coordinates. Feet sit on open ground; no foreground-mask invention.
const residents=[
 {id:0,x:495,y:318,height:19,place:'农舍门前'},
 {id:3,x:1036,y:338,height:20,place:'补给门前'},
 {id:0,x:628,y:480,height:19,place:'中央草地西侧'},
 {id:3,x:738,y:538,height:20,place:'中央草地下侧'},
 {id:4,x:907,y:483,height:18,place:'中央草地东侧'},
 {id:0,x:872,y:558,height:19,place:'中央路边'},
 {id:4,x:462,y:710,height:18,place:'陈列附近'},
 {id:3,x:1170,y:680,height:20,place:'神社附近'},
];
document.querySelector('#residents-layer').innerHTML=residents.map((r,i)=>{
 const sprite=resolveSprite(characterImage(0,r.id)),w=r.height*sprite.frame[2]/sprite.frame[3];
 return `<div class="resident" data-index="${i}" data-place="${r.place}" style="left:${r.x-w/2}px;top:${r.y-r.height}px;width:${w}px;height:${r.height}px;z-index:${r.y}"><i class="shadow"></i>${spriteSVG(sprite)}</div>`;
}).join('');
const viewport=document.querySelector('#viewport'),world=document.querySelector('#map-world');
let camera={x:768,y:512,zoom:1},drag=null;
const minZoom=()=>Math.min(viewport.clientWidth/1536,viewport.clientHeight/1024);
function render(){
 camera.zoom=Math.min(2,Math.max(minZoom(),camera.zoom));
 const hw=viewport.clientWidth/(camera.zoom*2),hh=viewport.clientHeight/(camera.zoom*2);
 camera.x=hw>=768?768:Math.max(hw,Math.min(1536-hw,camera.x));camera.y=hh>=512?512:Math.max(hh,Math.min(1024-hh,camera.y));
 world.style.transform=`translate(${viewport.clientWidth/2-camera.x*camera.zoom}px,${viewport.clientHeight/2-camera.y*camera.zoom}px) scale(${camera.zoom})`;
 document.querySelector('#zoom-label').textContent=`原图 1536 × 1024 · 当前 ${Math.round(camera.zoom*100)}%`;
 document.querySelector('#minus').disabled=camera.zoom<=minZoom()+1e-6;document.querySelector('#plus').disabled=camera.zoom>=2-1e-6;
}
function fit(){camera={x:768,y:512,zoom:minZoom()};render();}
function zoom(z,x=viewport.clientWidth/2,y=viewport.clientHeight/2){const wx=camera.x+(x-viewport.clientWidth/2)/camera.zoom,wy=camera.y+(y-viewport.clientHeight/2)/camera.zoom;camera.zoom=Math.max(minZoom(),Math.min(2,z));camera.x=wx-(x-viewport.clientWidth/2)/camera.zoom;camera.y=wy-(y-viewport.clientHeight/2)/camera.zoom;render();}
document.querySelector('#residents').addEventListener('change',e=>document.body.classList.toggle('hide-residents',!e.target.checked));
document.querySelector('#fit').addEventListener('click',fit);document.querySelector('#native').addEventListener('click',()=>{camera={x:768,y:480,zoom:1};render();});
document.querySelector('#plus').addEventListener('click',()=>zoom(camera.zoom*1.25));document.querySelector('#minus').addEventListener('click',()=>zoom(camera.zoom/1.25));
viewport.addEventListener('pointerdown',e=>{if(e.button!==0||drag)return;drag={id:e.pointerId,x:e.clientX,y:e.clientY};viewport.setPointerCapture(e.pointerId);viewport.classList.add('dragging');});
viewport.addEventListener('pointermove',e=>{if(drag?.id!==e.pointerId)return;camera.x-=(e.clientX-drag.x)/camera.zoom;camera.y-=(e.clientY-drag.y)/camera.zoom;drag.x=e.clientX;drag.y=e.clientY;render();});
function end(e){if(drag?.id!==e.pointerId)return;drag=null;viewport.classList.remove('dragging');if(viewport.hasPointerCapture(e.pointerId))viewport.releasePointerCapture(e.pointerId);}
for(const type of ['pointerup','pointercancel','lostpointercapture'])viewport.addEventListener(type,end);
viewport.addEventListener('wheel',e=>{e.preventDefault();const r=viewport.getBoundingClientRect();zoom(camera.zoom*Math.exp(-e.deltaY*.0015),e.clientX-r.left,e.clientY-r.top);},{passive:false});
new ResizeObserver(()=>{if(!document.body.dataset.export&&!document.body.dataset.detail)render();}).observe(viewport);
fit();
window.mapPreview={ready:true,residents,fit,getCamera:()=>({...camera})};
