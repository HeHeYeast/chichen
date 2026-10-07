import {FARM_MAP,WORLD,HOME_CAMERA,PLACES,residentsFor,farmStock,fitZoom,clampCamera,zoomCamera,hitPlace} from './farm-world.js';
import {characterImage} from './catalog.js';
import {resolveSprite,spriteSVG} from './art/manifest.js';
import {originalPortraitFrame} from './portrait-frames.js';
import {mementoArt} from './visual-assets.js';
import {farmIcon} from './farm-ui-icons.js';
import {levelBadgeMarkup} from './level-badge.js';

export function createFarmMapUI({stage,onPlace,onRepair,onSettings,onKitchen,onLevel,avatar}){
  const root=document.createElement('section');root.className='farm-map';root.hidden=true;root.setAttribute('aria-label','农场营地');stage.append(root);
  root.innerHTML=`
    <div class="farm-map-ground" tabindex="0" aria-label="营地地图，可拖动或用方向键浏览，加减键缩放">
      <div class="farm-map-world"><img class="farm-map-image" src="${FARM_MAP}" alt="河流环绕的营地，中央草地四周分布农舍、集市、神社和陈列亭" draggable="false"><div class="farm-map-residents"></div><div class="farm-map-mementos"></div></div>
      <div class="farm-map-labels">${PLACES.map(p=>`<button data-place="${p.id}" aria-label="打开${p.name}：${p.description}">${farmIcon(p.icon)}<span><strong>${p.name}</strong><small>${p.description}</small></span><i hidden></i></button>`).join('')}</div>
    </div>
    <header class="farm-hud">
      <button class="farm-avatar" data-level aria-label="查看厨房等级"></button>
      <div class="farm-wallet">${farmIcon('coin')}<strong data-coins></strong></div>
      <button class="farm-title" data-camera="places" aria-expanded="false" aria-controls="farm-destinations" aria-label="农场地点与全景" title="农场地点与全景">农场${farmIcon('sprout')}</button>
      <button class="farm-settings" data-settings aria-label="设置">${farmIcon('gear')}</button>
      <button class="farm-home-status" data-home-stock aria-label="查看在家伙伴">${avatar?avatar():farmIcon('chick')}<span data-stock></span></button>
      <button class="farm-repair-status" data-repair title="修复围栏">${farmIcon('sprout')}<span></span></button>
    </header>
    <button class="farm-task" data-task><span class="farm-task-icon">${farmIcon('quest')}</span><span class="farm-task-copy"><b></b><strong></strong></span><span class="farm-task-arrow" aria-hidden="true">›</span></button>
    <div class="farm-map-toolbar" aria-label="地图控制"><div class="farm-map-zoom"><button data-camera="in" aria-label="放大地图">+</button><button data-camera="out" aria-label="缩小地图">−</button></div><button data-camera="home" aria-label="地图归位" title="归位">${farmIcon('target')}</button></div>
    <section class="farm-map-destinations" id="farm-destinations" aria-label="农场地点" hidden>
      <div class="farm-destinations-heading"><strong>逛逛农场</strong><button data-close-places aria-label="关闭地点面板">×</button></div>
      <div class="farm-destinations-grid">${PLACES.map(p=>`<button data-locate="${p.id}">${farmIcon(p.icon)}<span>${p.name}<small>${p.description}</small></span></button>`).join('')}<button data-fence>${farmIcon('fence')}<span>修复围栏<small data-fence-state></small></span></button></div>
      <button data-camera="fit">${farmIcon('panorama')}查看全景</button>
    </section>`;
  const ground=root.querySelector('.farm-map-ground'),world=root.querySelector('.farm-map-world'),labels=[...root.querySelectorAll('[data-place]')];
  let camera={...HOME_CAMERA},active=false,blocked=false,residentKey='',displayKey='',gesture=null,homeView=true,taskAction=onKitchen;
  const points=new Map();
  const dimensions=()=>({w:ground.clientWidth,h:ground.clientHeight});
  const homeCamera=()=>{const {w,h}=dimensions(),ratio=w/390,zoom=Math.max(.7*ratio,h/WORLD.height);return clampCamera({x:HOME_CAMERA.x,y:(h/2+4*ratio)/zoom,zoom},w,h);};
  const local=e=>{const r=ground.getBoundingClientRect();return {x:(e.clientX-r.left)*ground.clientWidth/r.width,y:(e.clientY-r.top)*ground.clientHeight/r.height};};
  const open=id=>{if(active&&!blocked)onPlace(id);};
  function render(){
    if(!active)return;
    const {w,h}=dimensions();if(!w||!h)return;
    camera=homeView?homeCamera():clampCamera(camera,w,h);
    const tx=w/2-camera.x*camera.zoom,ty=h/2-camera.y*camera.zoom;
    world.style.transform=`translate(${tx}px,${ty}px) scale(${camera.zoom})`;
    root.dataset.lod=camera.zoom<.4?'far':camera.zoom>1?'near':'default';
    root.dataset.camera=JSON.stringify(camera);
    labels.forEach((b,i)=>{const p=PLACES[i],x=tx+p.x*camera.zoom,y=ty+p.y*camera.zoom,far=camera.zoom<.4;
      b.hidden=x<20||x>w-(far?20:88)||y<60||y>h-85;b.style.left=x+'px';b.style.top=y+'px';});
    root.querySelector('[data-camera="out"]').disabled=camera.zoom<=fitZoom(w,h)+.001;
    root.querySelector('[data-camera="in"]').disabled=camera.zoom>=1.599;
  }
  function cancel(){points.clear();gesture=null;ground.classList.remove('is-dragging');}
  function zoom(value,point){homeView=false;const {w,h}=dimensions();camera=zoomCamera(camera,value,point??{x:w/2,y:h/2},w,h);render();}
  function resetGesture(){
    const list=[...points.values()],{w,h}=dimensions();
    if(list.length===2){const a=list[0],b=list[1],mid={x:(a.x+b.x)/2,y:(a.y+b.y)/2};gesture={pinch:true,moved:true,distance:Math.max(1,Math.hypot(a.x-b.x,a.y-b.y)),zoom:camera.zoom,wx:camera.x+(mid.x-w/2)/camera.zoom,wy:camera.y+(mid.y-h/2)/camera.zoom};}
  }
  ground.addEventListener('pointerdown',e=>{
    if(blocked||e.button!==0||points.size>=2)return;
    if(!root.querySelector('.farm-map-destinations').hidden){togglePlaces(false);return;}
    const p=local(e);points.set(e.pointerId,p);ground.setPointerCapture(e.pointerId);
    if(points.size===1)gesture={start:p,last:p,moved:false,target:e.target.closest('[data-place]')?.dataset.place};else resetGesture();
    e.preventDefault();ground.focus({preventScroll:true});
  });
  ground.addEventListener('pointermove',e=>{
    if(!points.has(e.pointerId)||!gesture)return;
    const p=local(e);points.set(e.pointerId,p);
    if(points.size===2){
      homeView=false;
      const [a,b]=[...points.values()],{w,h}=dimensions(),mid={x:(a.x+b.x)/2,y:(a.y+b.y)/2};
      const z=Math.max(fitZoom(w,h),Math.min(1.6,gesture.zoom*Math.hypot(a.x-b.x,a.y-b.y)/gesture.distance));
      camera=clampCamera({zoom:z,x:gesture.wx-(mid.x-w/2)/z,y:gesture.wy-(mid.y-h/2)/z},w,h);
    }else{
      if(Math.hypot(p.x-gesture.start.x,p.y-gesture.start.y)>5)gesture.moved=true;
      if(gesture.moved){homeView=false;camera.x-=(p.x-gesture.last.x)/camera.zoom;camera.y-=(p.y-gesture.last.y)/camera.zoom;}
      gesture.last=p;
    }
    if(gesture.moved)ground.classList.add('is-dragging');render();
  });
  function end(e){
    if(!points.has(e.pointerId))return;
    const p=local(e),tap=e.type==='pointerup'&&points.size===1&&!gesture?.moved,target=gesture?.target;
    points.delete(e.pointerId);if(ground.hasPointerCapture(e.pointerId))ground.releasePointerCapture(e.pointerId);
    if(points.size){const remaining=[...points.values()][0];gesture={start:remaining,last:remaining,moved:true};}
    else{gesture=null;ground.classList.remove('is-dragging');}
    if(tap){const {w,h}=dimensions(),place=target??hitPlace(camera.x+(p.x-w/2)/camera.zoom,camera.y+(p.y-h/2)/camera.zoom)?.id;if(place)open(place);}
  }
  ground.addEventListener('pointerup',end);ground.addEventListener('pointercancel',end);
  ground.addEventListener('lostpointercapture',e=>{if(points.has(e.pointerId))cancel();});
  window.addEventListener('blur',cancel);
  ground.addEventListener('click',e=>{e.preventDefault();if(e.detail===0){const id=e.target.closest('[data-place]')?.dataset.place;if(id)open(id);}});
  ground.addEventListener('wheel',e=>{if(blocked)return;e.preventDefault();zoom(camera.zoom*Math.exp(-e.deltaY*.0015),local(e));},{passive:false});
  ground.addEventListener('keydown',e=>{
    if(blocked||e.target!==ground)return;
    const delta={ArrowLeft:[-60,0],ArrowRight:[60,0],ArrowUp:[0,-60],ArrowDown:[0,60]}[e.key];
    if(delta){e.preventDefault();homeView=false;camera.x+=delta[0]/camera.zoom;camera.y+=delta[1]/camera.zoom;render();}
    if(['+','=','-','Home'].includes(e.key)){e.preventDefault();if(e.key==='Home'){homeView=true;render();}else zoom(camera.zoom*(e.key==='-'?.8:1.25));}
  });
  root.querySelector('[data-repair]').onclick=()=>{if(!blocked)onRepair();};
  root.querySelector('[data-fence]').onclick=()=>{if(!blocked){togglePlaces(false);onRepair();}};
  root.querySelector('[data-settings]').onclick=()=>{if(!blocked)onSettings();};
  root.querySelector('[data-level]').onclick=()=>{if(!blocked)onLevel();};
  root.querySelector('[data-home-stock]').onclick=()=>open('house');
  root.querySelector('[data-task]').onclick=()=>{if(!blocked)taskAction();};
  root.querySelector('[data-close-places]').onclick=()=>{togglePlaces(false);root.querySelector('[data-camera="places"]').focus();};
  root.addEventListener('keydown',e=>{if(e.key==='Escape'&&!root.querySelector('.farm-map-destinations').hidden){togglePlaces(false);root.querySelector('[data-camera="places"]').focus();e.stopPropagation();}});
  root.querySelectorAll('[data-locate]').forEach(b=>b.onclick=()=>{
    if(blocked)return;homeView=false;
    const p=PLACES.find(p=>p.id===b.dataset.locate),{h}=dimensions();
    camera={x:p.x,y:p.y+(h/2-Math.max(70,h*.38))/.95,zoom:.95};render();togglePlaces(false);labels.find(b=>b.dataset.place===p.id)?.focus({preventScroll:true});
  });
  function togglePlaces(show){root.querySelector('.farm-map-destinations').hidden=!show;root.querySelector('[data-camera="places"]').setAttribute('aria-expanded',String(show));}
  root.querySelectorAll('[data-camera]').forEach(b=>b.onclick=()=>{
    if(blocked)return;
    const action=b.dataset.camera,{w,h}=dimensions();
    if(action==='places'){togglePlaces(root.querySelector('.farm-map-destinations').hidden);return;}
    cancel();if(action==='home')homeView=true;else if(action==='fit'){homeView=false;camera={x:WORLD.width/2,y:WORLD.height/2,zoom:fitZoom(w,h)};togglePlaces(false);ground.focus({preventScroll:true});}else zoom(camera.zoom*(action==='in'?1.25:.8));render();
  });
  new ResizeObserver(()=>{cancel();render();}).observe(ground);
  function update(state,{visible,hp,shrineReady}){
    active=visible;root.hidden=!visible;
    if(!active){cancel();togglePlaces(false);return;}
    const stock=farmStock(state),home=stock.reduce((n,r)=>n+r.home,0),total=stock.reduce((n,r)=>n+r.T,0);
    root.querySelector('[data-stock]').textContent=`在家 ${home} 位`;
    // 厨房等级头像: the same picture as the kitchen header (level-badge.js)
    const badge=root.querySelector('.farm-avatar');if(badge.dataset.kitchenLevel!==String(state.kitchenLevel)){badge.dataset.kitchenLevel=state.kitchenLevel;badge.innerHTML=levelBadgeMarkup(state.kitchenLevel);badge.setAttribute('aria-label',`厨房 Lv.${state.kitchenLevel+1}，查看厨房等级`);}
    const coins=root.querySelector('[data-coins]');coins.textContent=state.cp.toLocaleString('en-US');coins.classList.toggle('is-long',String(state.cp).length>5);root.querySelector('.farm-wallet').setAttribute('aria-label',`${state.cp} CP`);
    const repair=root.querySelector('[data-repair]');repair.querySelector('span').innerHTML=`${hp<100?'整修':'完好'} <b>${hp}%</b>`;repair.setAttribute('aria-label',`${hp<100?'修复围栏，整修':'围栏完好'} ${hp}%`);repair.disabled=hp>=100;repair.classList.toggle('needs-repair',hp<60);
    root.querySelector('[data-fence]').disabled=hp>=100;root.querySelector('[data-fence-state]').textContent=hp>=100?'围栏完好':`完好度 ${hp}%`;
    // The existing empty-state guidance becomes the task strip, without a second overlay.
    let kicker='当前任务 · 前往厨房',copy='迎接第一位鸡宝';taskAction=onKitchen;
    if(total&&hp<60){kicker='农场照料 · 修复围栏';copy='整修围栏，让鸡宝安心住下';taskAction=onRepair;}
    else if(total&&home===0){kicker='农场动态 · 伙伴外出中';copy='去仓库看看伙伴的近况';taskAction=()=>open('house');}
    else if(total||Object.values(state.total??{}).some(n=>n>0)){kicker='当前任务 · 前往厨房';copy='收取下一锅，迎接更多鸡宝';}
    root.querySelector('.farm-task-copy b').textContent=kicker;root.querySelector('.farm-task-copy strong').textContent=copy;root.querySelector('[data-task]').setAttribute('aria-label',`${kicker}：${copy}`);
    const residents=residentsFor(state),key=JSON.stringify(residents);
    if(key!==residentKey){residentKey=key;root.querySelector('.farm-map-residents').innerHTML=residents.map(r=>{
      const [egg,id]=r.key.split(':').map(Number),resolved=resolveSprite(characterImage(egg,id)),sprite=resolved.frame?resolved:originalPortraitFrame(egg,id)??resolved;
      const frame=sprite.frame??[0,0,120,120],width=r.height*frame[2]/frame[3];
      return `<div class="farm-map-resident" data-species="${r.key}" style="left:${r.x-width/2}px;top:${r.y-r.height}px;width:${width}px;height:${r.height}px"><i></i>${sprite.frame?spriteSVG(sprite):`<img src="${sprite.file}" alt="" style="width:100%;height:100%;object-fit:contain">`}</div>`;
    }).join('');}
    const display=state.expansion?.collections?.display??[],dk=JSON.stringify(display);
    if(dk!==displayKey){displayKey=dk;root.querySelector('.farm-map-mementos').innerHTML=display.map((id,i)=>id?`<img src="${mementoArt(id)}" alt="陈列纪念物" style="left:${351+i*35}px;top:${634+i*6}px">`:'').join('');}
    labels.find(b=>b.dataset.place==='shrine').querySelector('i').hidden=!shrineReady;
    render();
  }
  return {update,setBlocked(value){blocked=value;root.inert=value||!active;if(value)cancel();},getCamera:()=>({...camera})};
}
