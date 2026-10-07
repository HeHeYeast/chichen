import {layouts} from './layout-data.js';

for(const card of document.querySelectorAll('.direction')){
  const data=layouts[card.dataset.direction],view=card.querySelector('.map-viewport'),world=card.querySelector('.world'),hotspots=card.querySelector('.hotspots');
  const output=card.querySelector('output'),select=card.querySelector('select'),selection=card.querySelector('.selection');
  let zoom=1.45,cx=data.center[0],cy=data.center[1],scale=1,tx=0,ty=0,chosen='',dragged=false,pinch=null;
  const pointers=new Map();
  const buttons=new Map();
  for(const n of data.nodes){
    const b=document.createElement('button');b.className='building';b.dataset.node=n[0];b.setAttribute('aria-label',n[1]);
    const label=document.createElement('span');label.textContent=n[1];b.append(label);hotspots.append(b);buttons.set(n[0],b);
    b.addEventListener('click',e=>{if(dragged&&e.detail!==0)return;choose(n);});
  }
  function choose(n){
    chosen=n[0];selection.replaceChildren();const title=document.createElement('strong');title.textContent=n[1];const p=document.createElement('p');p.textContent=n[6]+'（功能归属示意，未接入 Runtime）';selection.append(title,p);paint();
  }
  function paint(){
    const w=view.clientWidth,h=view.clientHeight,fit=Math.min(w/1000,h/1100);scale=fit*zoom;
    cx=w>=1000*scale?500:Math.max(w/2/scale,Math.min(1000-w/2/scale,cx));
    cy=h>=1100*scale?550:Math.max(h/2/scale,Math.min(1100-h/2/scale,cy));
    tx=w/2-cx*scale;ty=h/2-cy*scale;
    world.style.transform=`translate(${tx}px,${ty}px) scale(${scale})`;
    for(const n of data.nodes){
      const b=buttons.get(n[0]),bw=Math.max(44,n[4]*scale),bh=Math.max(44,n[5]*scale),x=tx+(n[2]+n[4]/2)*scale-bw/2,y=ty+n[3]*scale;
      Object.assign(b.style,{left:x+'px',top:y+'px',width:bw+'px',height:bh+'px'});
      b.hidden=x+bw<0||x>w||y+bh<0||y>h;b.classList.toggle('chosen',chosen===n[0]);
      b.setAttribute('aria-pressed',String(chosen===n[0]));b.querySelector('span').hidden=n[0]==='repair'&&zoom<1.25;
    }
    output.value=zoom.toFixed(2)+'×';card.dataset.zoom=zoom.toFixed(2);card.dataset.center=`${cx.toFixed(1)},${cy.toFixed(1)}`;
    card.querySelector('[data-zoom=out]').disabled=zoom<=1;card.querySelector('[data-zoom=in]').disabled=zoom>=2.4;
  }
  function setZoom(next,px=view.clientWidth/2,py=view.clientHeight/2){
    const wx=(px-tx)/scale,wy=(py-ty)/scale;const old=zoom;zoom=Math.max(1,Math.min(2.4,next));
    const ns=scale*zoom/old;cx=wx-(px-view.clientWidth/2)/ns;cy=wy-(py-view.clientHeight/2)/ns;
    card.querySelectorAll('[data-mode]').forEach(b=>b.setAttribute('aria-pressed','false'));paint();
  }
  card.querySelectorAll('[data-mode]').forEach(b=>b.addEventListener('click',()=>{
    const mode=b.dataset.mode;zoom=mode==='far'?1:mode==='near'?2.2:1.45;[cx,cy]=mode==='near'?data.near:data.center;
    card.querySelectorAll('[data-mode]').forEach(o=>o.setAttribute('aria-pressed',String(o===b)));paint();
  }));
  card.querySelectorAll('[data-zoom]').forEach(b=>b.addEventListener('click',()=>setZoom(zoom+(b.dataset.zoom==='in'?.2:-.2))));
  select.addEventListener('change',()=>{const n=data.nodes.find(n=>n[0]===select.value);if(!n)return;zoom=2.2;cx=n[2]+n[4]/2;cy=n[3]+n[5]/2+45;choose(n);card.querySelectorAll('[data-mode]').forEach(b=>b.setAttribute('aria-pressed','false'));});
  const point=e=>{const r=view.getBoundingClientRect();return{x:e.clientX-r.left,y:e.clientY-r.top};};
  view.addEventListener('wheel',e=>{e.preventDefault();const p=point(e);setZoom(zoom*Math.exp(-e.deltaY*.001),p.x,p.y);},{passive:false});
  view.addEventListener('pointerdown',e=>{
    if(e.pointerType==='mouse'&&e.button!==0)return;
    const p=point(e);if(!pointers.size)dragged=false;
    pointers.set(e.pointerId,{...p,startX:p.x,startY:p.y,lastX:p.x,lastY:p.y});
    // Capture on the original target so a stationary press still produces its click.
    e.target.setPointerCapture(e.pointerId);view.classList.add('dragging');
    if(pointers.size===2){const [a,b]=[...pointers.values()];pinch={distance:Math.hypot(a.x-b.x,a.y-b.y),zoom};dragged=true;}
  });
  view.addEventListener('pointermove',e=>{
    const old=pointers.get(e.pointerId);if(!old)return;const p=point(e);pointers.set(e.pointerId,{...old,...p});
    if(pointers.size===2&&pinch){const [a,b]=[...pointers.values()];setZoom(pinch.zoom*Math.hypot(a.x-b.x,a.y-b.y)/Math.max(1,pinch.distance),(a.x+b.x)/2,(a.y+b.y)/2);}
    else if(pointers.size===1){
      if(Math.hypot(p.x-old.startX,p.y-old.startY)>8)dragged=true;
      if(dragged){cx-=(p.x-old.lastX)/scale;cy-=(p.y-old.lastY)/scale;paint();}
    }
    const current=pointers.get(e.pointerId);current.lastX=p.x;current.lastY=p.y;
  });
  function release(e){pointers.delete(e.pointerId);if(pointers.size<2)pinch=null;if(!pointers.size)view.classList.remove('dragging');}
  view.addEventListener('pointerup',release);view.addEventListener('pointercancel',release);
  view.addEventListener('keydown',e=>{if(e.target!==view)return;const delta=70/scale;
    if(e.key==='ArrowLeft')cx-=delta;else if(e.key==='ArrowRight')cx+=delta;else if(e.key==='ArrowUp')cy-=delta;else if(e.key==='ArrowDown')cy+=delta;
    else if(e.key==='+'||e.key==='='){setZoom(zoom+.2);return;}else if(e.key==='-'){setZoom(zoom-.2);return;}else return;e.preventDefault();paint();
  });
  new ResizeObserver(paint).observe(view);paint();
}
