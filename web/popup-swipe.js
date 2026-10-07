// Any popup can be thrown away: drag it up, or off to either side, and it closes the way its own close button would
// (a confirm counts as 「取消」, a drawer as a tap on its backdrop). Dragging down is left alone, and so is anything that
// would scroll, a slider or a field, so lists and quantity sliders keep working.
const POPUP='#dialog-layer .gd, #panels .kp-drawer';
const START=10,DISTANCE=72,FLICK=.55; // px before it counts as a drag, px to let go of, px/ms
const NOT_THESE='input,select,textarea,[data-no-swipe]';

// the popup under the finger, or the one this backdrop belongs to
function popupFor(target){
  const direct=target.closest(POPUP);if(direct)return direct;
  const back=target.closest('.kp-scrim,.modal-shade');
  return back?[...(back.parentElement?.children??[])].find(c=>c!==back&&c.matches(POPUP))??null:null;
}
function closerFor(popup){
  if(popup.matches('#dialog-layer .gd'))return popup.querySelector('.gd-close');
  return [...(popup.parentElement?.children??[])].find(c=>c!==popup&&c.classList?.contains('kp-scrim'))??null;
}
// Is there something under the finger that would scroll along this axis?
function scrolls(from,popup,axis){
  for(let n=from;n&&n.nodeType===1;n=n.parentElement){
    const cs=getComputedStyle(n);
    if(axis==='y'&&/(auto|scroll)/.test(cs.overflowY)&&n.scrollHeight>n.clientHeight+1)return true;
    if(axis==='x'&&/(auto|scroll)/.test(cs.overflowX)&&n.scrollWidth>n.clientWidth+1)return true;
    if(n===popup)break;
  }
  return false;
}

export function installPopupSwipe(doc=document){
  let g=null,lastTouch=0;
  const begin=(x,y,target)=>{
    if(g||!target?.closest)return;
    const popup=popupFor(target);if(!popup||target.closest(NOT_THESE)||!closerFor(popup))return;
    g={popup,target,x,y,t:performance.now(),drag:false,axis:null,dx:0,dy:0};
  };
  const move=(x,y,e)=>{
    if(!g)return;
    const dx=x-g.x,dy=y-g.y;
    if(!g.drag){
      if(Math.hypot(dx,dy)<START)return;
      const up=dy<0&&Math.abs(dy)>Math.abs(dx),side=Math.abs(dx)>Math.abs(dy);
      if(!up&&!side){g=null;return;}
      g.axis=up?'y':'x';
      if(scrolls(g.target,g.popup,g.axis)){g=null;return;}
      g.drag=true;g.popup.style.transition='none';g.popup.dataset.swiping='1';
    }
    g.dx=g.axis==='x'?dx:0;g.dy=g.axis==='y'?Math.min(0,dy):0;
    g.popup.style.translate=`${g.dx}px ${g.dy}px`;
    g.popup.style.opacity=String(Math.max(.35,1-Math.hypot(g.dx,g.dy)/360));
    if(e?.cancelable)e.preventDefault();
  };
  const finish=()=>{
    const s=g;g=null;if(!s?.drag)return;
    const dist=s.axis==='x'?Math.abs(s.dx):-s.dy,speed=dist/Math.max(1,performance.now()-s.t);
    // the tap that ends a drag must not press a button under the finger
    const block=e=>{if(!e.isTrusted)return;e.stopPropagation();e.preventDefault();}; // real taps only: our own close click must pass
    doc.addEventListener('click',block,{capture:true,once:true});setTimeout(()=>doc.removeEventListener('click',block,true),350);
    const popup=s.popup,close=closerFor(popup);
    popup.style.transition='translate .18s ease-out, opacity .18s ease-out';
    if(close&&(dist>=DISTANCE||speed>=FLICK&&dist>=24)){
      const w=doc.defaultView?.innerWidth??400,h=doc.defaultView?.innerHeight??800;
      popup.style.translate=s.axis==='x'?`${s.dx<0?-w:w}px 0px`:`0px ${-h}px`;popup.style.opacity='0';
      setTimeout(()=>{if(popup.isConnected){popup.style.removeProperty('translate');popup.style.removeProperty('opacity');popup.style.removeProperty('transition');delete popup.dataset.swiping;}close.click();},170);
    }else{
      popup.style.translate='0px 0px';popup.style.opacity='1';
      setTimeout(()=>{popup.style.removeProperty('translate');popup.style.removeProperty('opacity');popup.style.removeProperty('transition');delete popup.dataset.swiping;},190);
    }
  };
  // touch (the phone)
  doc.addEventListener('touchstart',e=>{lastTouch=Date.now();if(e.touches.length===1)begin(e.touches[0].clientX,e.touches[0].clientY,e.target);else g=null;},{passive:true});
  doc.addEventListener('touchmove',e=>{if(e.touches.length===1)move(e.touches[0].clientX,e.touches[0].clientY,e);},{passive:false});
  doc.addEventListener('touchend',finish,{passive:true});
  doc.addEventListener('touchcancel',()=>{if(g?.drag){g.popup.style.removeProperty('translate');g.popup.style.removeProperty('opacity');g.popup.style.removeProperty('transition');delete g.popup.dataset.swiping;}g=null;},{passive:true});
  // mouse (desktop, tests); the synthetic mouse events that follow a touch are ignored
  doc.addEventListener('mousedown',e=>{if(Date.now()-lastTouch>600&&e.button===0)begin(e.clientX,e.clientY,e.target);},true);
  doc.addEventListener('mousemove',e=>{if(g&&Date.now()-lastTouch>600)move(e.clientX,e.clientY,e);},true);
  doc.addEventListener('mouseup',()=>{if(Date.now()-lastTouch>600)finish();},true);
}
