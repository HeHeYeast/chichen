import {interfaceIcon} from '../../ui-icons.js';
import {resolveSprite,spriteSVG} from '../../art/manifest.js';
import {characterImage} from '../../catalog.js';
import {UNIT,VIEW,DEFAULT_ZOOM,MAX_ZOOM,WORLDS,NAMES,DETAILS,layout,minZoom,clampCamera,worldPoint,zoomAt} from './world.js';
const kitURL=new URL('../farm-scale-cohesion-gate-20260929/asset-manifest.json',import.meta.url);
const kit=await (await fetch(kitURL)).json();
for(const a of Object.values(kit))a.url=new URL(a.file,kitURL).href;
const viewport=document.querySelector('#viewport'),worldEl=document.querySelector('#world'),anchored=document.querySelector('#anchored');
let world,scene,camera,drag=null;
const art=id=>{const a=kit[id];return `<svg class="asset-svg" viewBox="${a.frame.join(' ')}" aria-hidden="true"><image href="${a.url}" width="${a.width}" height="${a.height}"/></svg>`;};
function object(o,kind='environment'){
 const a=kit[o.id],height=o.width*a.frame[3]/a.frame[2],w=o.width*UNIT,h=height*UNIT;
 return `<div class="object ${kind}" data-asset="${o.id}" style="left:${(o.x-o.width/2)*UNIT}px;top:${(o.y-height)*UNIT}px;width:${w}px;height:${h}px;z-index:${Math.round(o.y*100)}"><i class="contact" style="width:${w*.64}px;height:${Math.max(3,w*.045)}px"></i>${art(o.id)}</div>`;
}
function resident(o){
 const sprite=resolveSprite(characterImage(0,o.id)),height=o.height*UNIT,w=height*sprite.frame[2]/sprite.frame[3];
 return `<div class="object character" data-resident="${o.id}" style="left:${o.x*UNIT-w/2}px;top:${o.y*UNIT-height}px;width:${w}px;height:${height}px;z-index:${Math.round(o.y*100)}"><i class="contact" style="width:${w*.65}px;height:4px"></i>${spriteSVG(sprite)}</div>`;
}
function ground(){
 const w=world.width,h=world.height,b=Object.fromEntries(scene.buildings.map(n=>[n.id,n]));
 const main=`M${w*.5} ${h+2}Q${w*.49} ${h*.84} ${w*.52} ${h*.69}`;
 const links=`M${w*.52} ${h*.69}C${w*.29} ${h*.65} ${w*.22} ${h*.54} ${w*.27} ${h*.46}L${b.house.x} ${b.house.y}M${w*.52} ${h*.69}Q${w*.76} ${h*.58} ${b.shop.x} ${b.shop.y}`;
 const entries=`M${w*.5} ${h*.82}Q${w*.35} ${h*.82} ${b.display.x} ${b.display.y}M${w*.5} ${h*.82}Q${w*.64} ${h*.83} ${b.shrine.x} ${b.shrine.y}`;
 const paths=[[main,1.8],[links,1.8],[entries,1.3]];
 const roads=paths.map(([d,width])=>`<path d="${d}" stroke="#ac9459" stroke-width="${width+.15}"/>`).join('')+paths.map(([d,width])=>`<path d="${d}" stroke="#d9b779" stroke-width="${width}"/>`).join('');
 let grass='';for(let i=0;i<42;i++){const x=4+((i*17.31)%(w-8)),y=6+((i*23.13)%(h-12));grass+=`<path d="m${x} ${y} -.15-.25m.15.25 .08-.3m-.08.3 .27-.15"/>`;}
 return `<svg class="terrain" viewBox="0 0 ${w} ${h}" aria-label="独立世界地面"><rect width="${w}" height="${h}" fill="#77964c"/><path d="M3 5Q${w*.5} 2 ${w-3} 6L${w-2} ${h*.48} ${w-4} ${h-3}Q${w*.5} ${h} 3 ${h-5}L2 ${h*.55}Z" fill="#a8c262"/><path d="M${w*.27} ${h*.31}Q${w*.61} ${h*.28} ${w*.75} ${h*.45}L${w*.73} ${h*.68}Q${w*.53} ${h*.77} ${w*.28} ${h*.70}L${w*.19} ${h*.48}Z" fill="#b7cc72"/><g fill="none" stroke-linecap="round" stroke-linejoin="round">${roads}</g><path d="M${w*.38} ${h*.40}Q${w*.55} ${h*.35} ${w*.61} ${h*.46}L${w*.66} ${h*.57}Q${w*.59} ${h*.65} ${w*.46} ${h*.65}L${w*.34} ${h*.54}Z" fill="#bed27c"/><g fill="none" stroke="#8ca84e" stroke-width=".07" stroke-linecap="round" opacity=".7">${grass}</g></svg>`;
}
const icons={house:'farm',shop:'shop',shrine:'explore',display:'book'};
function render(){
 clampCamera(camera,world);
 const s=UNIT*camera.zoom,tx=VIEW.width/2-camera.x*s,ty=VIEW.height/2-camera.y*s;
 worldEl.style.transform=`translate(${tx}px,${ty}px) scale(${camera.zoom})`;
 const lod=camera.zoom<.45?'far':camera.zoom<.85?'default':'near';
 viewport.dataset.lod=lod;
 anchored.innerHTML=scene.buildings.map(b=>{
  const x=tx+b.x*s,y=ty+b.y*s+8;
  if(x<16||x>VIEW.width-16||y<8||y>VIEW.height-95)return '';
  const label=lod==='far'?interfaceIcon(icons[b.id]):`${NAMES[b.id]}${lod==='near'?`<small>· ${DETAILS[b.id]}</small>`:''}`;
  return `<span class="tag ${lod}" data-label="${b.id}" aria-label="${NAMES[b.id]} ${DETAILS[b.id]}" style="left:${x}px;top:${y}px">${label}</span>`;
 }).join('');
 const visibleW=VIEW.width/s,visibleH=VIEW.height/s;
 const left=Math.max(0,camera.x-visibleW/2),top=Math.max(0,camera.y-visibleH/2);
 const right=Math.min(world.width,camera.x+visibleW/2),bottom=Math.min(world.height,camera.y+visibleH/2);
 const mini=document.querySelector('#minimap');mini.setAttribute('viewBox',`0 0 ${world.width} ${world.height}`);
 mini.innerHTML=`<rect width="${world.width}" height="${world.height}" rx="1" fill="#98b46b"/>${scene.buildings.map(b=>`<rect x="${b.x-1.2}" y="${b.y-1.5}" width="2.4" height="2" rx=".3" fill="${b.id==='house'?'#bd7043':'#52734a'}"/>`).join('')}<rect x="${left}" y="${top}" width="${right-left}" height="${bottom-top}" fill="#fff2c72e" stroke="#fff9db" stroke-width=".8"/>`;
 for(const [id,visible] of [['north',top>.01],['south',bottom<world.height-.01],['west',left>.01],['east',right<world.width-.01]])document.getElementById(id).style.visibility=visible?'visible':'hidden';
 document.querySelectorAll('[data-preset]').forEach(b=>b.classList.toggle('active',b.dataset.preset===lod));
 document.querySelector('[data-action=minus]').disabled=camera.zoom<=minZoom(world)+1e-6;
 document.querySelector('[data-action=plus]').disabled=camera.zoom>=MAX_ZOOM-1e-6;
 document.querySelector('#readout').innerHTML=`<div><dt>当前世界</dt><dd>${world.width} × ${world.height} WU</dd></div><div><dt>Camera 中心</dt><dd>${camera.x.toFixed(1)}, ${camera.y.toFixed(1)} WU</dd></div><div><dt>Zoom / 居民高度</dt><dd>${camera.zoom.toFixed(3)} / ${(s).toFixed(1)} px</dd></div><div><dt>当前可见范围</dt><dd>${visibleW.toFixed(1)} × ${visibleH.toFixed(1)} WU</dd></div>`;
}
function reset(){camera={...scene.defaultCenter,zoom:DEFAULT_ZOOM};render();}
function selectWorld(id){world=WORLDS[id]??WORLDS.a;scene=layout(world);drag=null;viewport.classList.remove('dragging');worldEl.style.width=world.width*UNIT+'px';worldEl.style.height=world.height*UNIT+'px';worldEl.innerHTML=ground()+scene.environment.map(o=>object(o)).join('')+scene.buildings.map(o=>object(o,'building')).join('')+scene.chickens.map(resident).join('');document.querySelectorAll('[data-world]').forEach(b=>b.setAttribute('aria-pressed',String(b.dataset.world===world.id)));reset();}
function preset(name){if(name==='far'){camera={x:world.width/2,y:world.height/2,zoom:minZoom(world)};render();return;}zoomAt(camera,world,name==='near'?1:DEFAULT_ZOOM);render();}
function locate(id){const b=scene.buildings.find(b=>b.id===id);if(!b)return;camera={x:b.x,y:b.y-1.8,zoom:Math.max(camera.zoom,DEFAULT_ZOOM)};render();}
document.querySelectorAll('[data-world]').forEach(b=>b.addEventListener('click',()=>selectWorld(b.dataset.world)));
document.querySelectorAll('[data-preset]').forEach(b=>b.addEventListener('click',()=>preset(b.dataset.preset)));
document.querySelectorAll('[data-locate]').forEach(b=>b.addEventListener('click',()=>locate(b.dataset.locate)));
document.querySelectorAll('[data-action]').forEach(b=>b.addEventListener('click',()=>{const action=b.dataset.action;if(action==='reset')reset();else{zoomAt(camera,world,camera.zoom*(action==='plus'?1.2:1/1.2));render();}}));
viewport.addEventListener('pointerdown',e=>{if(e.target.closest('button')||e.button!==0||drag)return;drag={id:e.pointerId,x:e.clientX,y:e.clientY};viewport.setPointerCapture(e.pointerId);viewport.classList.add('dragging');viewport.focus({preventScroll:true});});
viewport.addEventListener('pointermove',e=>{if(!drag||e.pointerId!==drag.id)return;const s=UNIT*camera.zoom;camera.x-=(e.clientX-drag.x)/s;camera.y-=(e.clientY-drag.y)/s;drag.x=e.clientX;drag.y=e.clientY;render();});
function endDrag(e){if(drag?.id!==e.pointerId)return;drag=null;viewport.classList.remove('dragging');if(viewport.hasPointerCapture(e.pointerId))viewport.releasePointerCapture(e.pointerId);}
for(const event of ['pointerup','pointercancel','lostpointercapture'])viewport.addEventListener(event,endDrag);
viewport.addEventListener('wheel',e=>{if(e.target.closest('.camera-ui'))return;e.preventDefault();const rect=viewport.getBoundingClientRect();const delta=e.deltaY*(e.deltaMode===1?16:e.deltaMode===2?VIEW.height:1);zoomAt(camera,world,camera.zoom*Math.exp(-delta*.0015),e.clientX-rect.left,e.clientY-rect.top);render();},{passive:false});
viewport.addEventListener('keydown',e=>{if(e.target.closest('button'))return;const step=48/(UNIT*camera.zoom);if(e.key==='ArrowLeft')camera.x-=step;else if(e.key==='ArrowRight')camera.x+=step;else if(e.key==='ArrowUp')camera.y-=step;else if(e.key==='ArrowDown')camera.y+=step;else if(e.key==='Home'){reset();e.preventDefault();return;}else if(['+','=','-'].includes(e.key))zoomAt(camera,world,camera.zoom*(e.key==='-'?1/1.2:1.2));else return;e.preventDefault();render();});
document.querySelector('#nav').innerHTML=[['kitchen','厨房'],['farm','农场'],['shop','生意'],['explore','寻访'],['book','图鉴']].map(([id,name])=>`<span class="${id==='farm'?'active':''}">${interfaceIcon(id)}<b>${name}</b></span>`).join('');
const capture=new URLSearchParams(location.search).has('capture');
function shot(title,file,note){return `<figure><figcaption>${title}</figcaption>${capture?`<div class="pending" data-src="evidence/${file}.png"></div>`:`<img src="evidence/${file}.png" alt="${title}">`}<p>${note}</p></figure>`;}
document.querySelector('#comparison').innerHTML=shot('A · 40 × 72 WU','a-default-390x844','同尺度，默认约 2 × 2 镜头范围。')+shot('B · 56 × 100 WU','b-default-390x844','同尺度，默认约 3 × 3 镜头范围。');
document.querySelector('#zoom-shots').innerHTML=shot('Far · 全局关系','a-far-390x844','自适应全览；标签减为功能图标。')+shot('Default · 日常浏览','a-default-390x844','zoom 0.625；建筑短名保持屏幕字号。')+shot('Near · 对象细节','a-near-390x844','zoom 1.0；显示建筑用途 / 状态。');
selectWorld(new URLSearchParams(location.search).get('world')??'a');
window.farmCamera={ready:true,selectWorld,reset,preset,locate,getState:()=>({world:{...world},camera:{...camera},minZoom:minZoom(world),defaultCenter:{...scene.defaultCenter},buildings:scene.buildings.map(b=>({...b})),residents:scene.chickens.map(c=>({...c})),lod:viewport.dataset.lod,view:{...VIEW}}),worldPoint:(x,y)=>worldPoint(camera,x,y)};
