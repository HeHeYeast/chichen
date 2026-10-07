import {remasterStage} from './remaster.js';
import * as E from '/web/engine.js';
import {COLOR as C,LAYOUT as L,NAV,FONT,RECT,navRect,toolRect,duckRect,MOTION} from '/web/theme.js';
import {characterImage,toolImage} from '/web/catalog.js';
import {uiIcon} from '/web/art/manifest.js';
import {kitchenStage,kitchenBackgroundParts} from '/web/kitchen-stages.js';
import {createFarmRenderer} from '/web/farm-scene.js';
import {TOOL_COUNT,TOOL_SCROLL_MAX} from '/web/content-pack.js';
import {createTitleRenderer} from '/web/title-scene.js';
const root='/assets/png/';
const utensil=(id,lv=0)=>toolImage(1,id,lv);
const eggArt=egg=>egg===0?'/web/art/egg-v4.png':root+'Egg/egg_1_0_0.png';

export function createRenderer(ctx,image){
  const farmRenderer=createFarmRenderer(ctx,image);
  const titleRenderer=createTitleRenderer(ctx,image);
  function room(stage){for(const [part,x,y,w,h]of kitchenBackgroundParts(stage)){
    const rect=part==='#wall'?[x,y,w,h+L.eggOffset]:part==='#table'?[x,y+L.eggOffset,w,h+L.extra-L.eggOffset]:part==='#base'?[x,y+L.extra,w,h]:[x,y,w,h+L.extra];
    image(stage.background+part,...rect);
  }}
  function box(x,y,w,h,r,fill,stroke=C.ink,line=1.6){
    ctx.beginPath();ctx.roundRect(x,y,w,h,r);ctx.fillStyle=fill;ctx.fill();
    if(stroke){ctx.strokeStyle=stroke;ctx.lineWidth=line;ctx.stroke();}
  }
  function text(value,x,y,size=13,fill=C.ink,weight=700,align='center',maxWidth){
    ctx.font=`${weight} ${size}px ${FONT}`;
    if(maxWidth&&ctx.measureText(String(value)).width>maxWidth){size=Math.max(10,size*maxWidth/ctx.measureText(String(value)).width);ctx.font=`${weight} ${size}px ${FONT}`;}
    ctx.textAlign=align;ctx.textBaseline='middle';ctx.fillStyle=fill;ctx.fillText(String(value),x,y);
  }
  function line(points,color=C.ink,width=1.6){ctx.beginPath();for(const [i,p]of points.entries())i?ctx.lineTo(...p):ctx.moveTo(...p);ctx.strokeStyle=color;ctx.lineWidth=width;ctx.lineCap='round';ctx.lineJoin='round';ctx.stroke();}
  function oval(x,y,rx,ry,fill,stroke=null,width=1.5){ctx.beginPath();ctx.ellipse(x,y,rx,ry,0,0,Math.PI*2);ctx.fillStyle=fill;ctx.fill();if(stroke){ctx.strokeStyle=stroke;ctx.lineWidth=width;ctx.stroke();}}
  function star(x,y,r=5,fill=C.yellow){ctx.beginPath();for(let i=0;i<10;i++){const a=-Math.PI/2+i*Math.PI/5,rr=i%2?r*.45:r;i?ctx.lineTo(x+Math.cos(a)*rr,y+Math.sin(a)*rr):ctx.moveTo(x+Math.cos(a)*rr,y+Math.sin(a)*rr);}ctx.closePath();ctx.fillStyle=fill;ctx.fill();}
  function pressed(v,id,fn){ctx.save();if(v.pressedId===id)ctx.translate(0,MOTION.pressDepth);fn(v.pressedId===id);ctx.restore();}
  function button(r,v,id,fill=C.paper,draw){
    box(r.x,r.y+3,r.w,r.h-3,10,'#ae7e48',C.ink,1.5);
    pressed(v,id,down=>{box(r.x,r.y,r.w,r.h-3,10,down?'#edc779':fill,C.ink,1.6);line([[r.x+8,r.y+4],[r.x+r.w-8,r.y+4]],'#fff9dc',1.5);draw?.();});
  }
  function gear(x,y){
    ctx.save();ctx.translate(x,y);for(let i=0;i<8;i++){ctx.rotate(Math.PI/4);box(-2.5,-11,5,7,1,'#fff8d9',C.ink,1.4);}oval(0,0,7.6,7.6,'#fff8d9',C.ink);oval(0,0,2.4,2.4,'#bd9162');ctx.restore();
  }
  function clock(x,y,on){oval(x,y,11,11,on?'#ffcf5d':'#fff5d3',C.ink,1.6);line([[x,y-6],[x,y],[x+4,y+2]],C.ink,1.7);line([[x-7,y-11],[x-11,y-8]],C.ink,2.4);line([[x+7,y-11],[x+11,y-8]],C.ink,2.4);}
  function header(s,v,title){
    box(0,0,320,58,0,'#f7dc89',null);line([[0,56],[320,56]],'#b78949',2);
    button(RECT.level,v,'level',C.yellow,()=>{image(characterImage(0,0),8,-3,45,45);box(12,38,37,13,6,C.ink,null);text('Lv.'+(s.kitchenLevel+1),30.5,44,10,'#fff6d5');});
    box(62,13,204,34,17,'#fff9df',C.ink,1.8);oval(78,30,14,14,'#ffc943',C.ink,1.6);oval(78,29,9,9,'#ffe7a0',null);text('C',78,29,15,'#9b6331',900);text(s.cp.toLocaleString('en-US'),173,30,22,C.ink,800,'center',155);
    button(RECT.settings,v,'settings','#f5e6bd',()=>{gear(295,23);text('设置',295,42,9);});
    if(title){box(87,66,146,26,13,'#fff9e1','#ac854e',1.5);text(title,160,79,12,C.ink,700,'center',133);}
  }
  function navigation(page,v){
    box(0,L.navY-4,320,66,0,'#e8efd6',null);line([[0,L.navY-4],[320,L.navY-4]],'#7da17a',2);line([[0,L.navY-1],[320,L.navY-1]],'#fffde2',2);
    NAV.forEach((n,i)=>{const r=navRect(i),active=n.id===page;pressed(v,'nav:'+n.id,()=>{
      if(active){box(r.x,r.y+2,r.w,r.h-4,12,'#ffd16a',C.ink,1.6);line([[r.x+10,r.y+5],[r.x+r.w-10,r.y+5]],'#fff5ce',2);}
      if(n.icon==='explore')exploreIcon(r.x+r.w/2,r.y+18);else image(uiIcon(n.icon),r.x+r.w/2-14,r.y+4,28,28);text(n.title,r.x+r.w/2,r.y+43,12,C.ink,active?800:700);
      // One quiet dot when a place has a real result waiting; never for locks.
      if(v.navBadges?.[n.id]){oval(r.x+r.w-12,r.y+9,5,5,'#e2593b',C.ink,1.2);}
    });});
  }
  // Nav glyph for 寻访: a signpost over a path, same ink outline as the atlas icons.
  function exploreIcon(x,y){
    ctx.save();ctx.lineJoin='round';ctx.lineCap='round';
    ctx.beginPath();ctx.moveTo(x-12,y+13);ctx.quadraticCurveTo(x-2,y+6,x+2,y+9);ctx.quadraticCurveTo(x+8,y+12,x+13,y+6);ctx.lineWidth=5;ctx.strokeStyle='#caa05f';ctx.stroke();ctx.lineWidth=1.4;ctx.strokeStyle=C.ink;ctx.stroke();
    box(x-2,y-10,4,21,1.5,'#9b6a3c',C.ink,1.4);
    ctx.beginPath();ctx.moveTo(x-11,y-10);ctx.lineTo(x+7,y-10);ctx.lineTo(x+12,y-6);ctx.lineTo(x+7,y-2);ctx.lineTo(x-11,y-2);ctx.closePath();ctx.fillStyle='#78b36b';ctx.fill();ctx.lineWidth=1.6;ctx.strokeStyle=C.ink;ctx.stroke();
    ctx.beginPath();ctx.moveTo(x+11,y-1);ctx.lineTo(x-6,y-1);ctx.lineTo(x-10,y+3);ctx.lineTo(x-6,y+7);ctx.lineTo(x+11,y+7);ctx.closePath();ctx.fillStyle='#f3c25a';ctx.fill();ctx.stroke();
    ctx.restore();
  }
  function tray(stage,front=false){
    if(stage.bedRect){
      ctx.save();if(front){ctx.beginPath();ctx.rect(0,stage.frontY,320,50);ctx.clip();}
      image(stage.bed,...stage.bedRect);ctx.restore();return;
    }
    if(stage.bedParts){
      for(const [part,x,y,w,h]of stage.bedParts)if(!front||part==='#front')image(stage.bed+part,x,y,w,h);
      return;
    }
    if(stage.level>0){
      ctx.save();if(front){ctx.beginPath();ctx.rect(35,316,253,14);ctx.clip();}
      image(stage.bed,39,169,243,159);ctx.restore();return;
    }
    ctx.save();ctx.beginPath();ctx.roundRect(39,174,243,144,19);ctx.clip();
    if(front){ctx.beginPath();ctx.rect(30,310,260,19);ctx.clip();}
    image(stage.bed,33,169,254,158);
    ctx.restore();
  }
  function basket(stage,v,front=false){
    const time=performance.now(),age=Math.min(...(v.feedbacks??[]).map(f=>time-f.born-MOTION.flightMs).filter(a=>a>=0),9999);
    const squash=!v.reducedMotion&&age<250?Math.sin(age/250*Math.PI)*2:0;
    if(front){ctx.save();ctx.beginPath();ctx.rect(269,319+squash,48,20);ctx.clip();}
    image(stage.vessel,268,294+squash,47,47-squash);
    if(front)ctx.restore();
  }
  // Small code-drawn maintenance marks register with each room, never with the HUD.
  function grime(stage){
    const d=stage.dirty;ctx.save();ctx.globalAlpha=.65;
    d.webs.forEach(([x,y],index)=>{
      ctx.save();ctx.translate(x,y);if(index)ctx.scale(-1,1);
      for(let j=0;j<5;j++){const a=j*Math.PI/8;line([[0,0],[Math.cos(a)*28,Math.sin(a)*28]],d.wall,.8);}
      for(const radius of [10,19,28]){const points=Array.from({length:9},(_,j)=>{const a=j*Math.PI/16,r=radius*(j%2?.88:1);return [Math.cos(a)*r,Math.sin(a)*r];});line(points,d.wall,.7);}
      ctx.restore();
    });
    for(const [x,y,w,h]of [[54,324,9,2.5],[90,329,13,2],[212,326,10,2],[259,333,5,1.7],[45,283,4,2]]){
      oval(x,y,w,h,d.table);oval(x+w+4,y-2,1.4,.9,d.table);
    }
    ctx.restore();
  }
  function drawEgg(e,i,v){
    const t=performance.now(),pulse=v.reducedMotion?0:Math.sin(t/620+i*.8)*.65;
    const ready=['ready','hatching'].includes(e.status),x=e.x-30,y=e.y-30;
    if(ready){
      oval(e.x,e.y+25,19,6,'#ebce7670');
      if(e.status==='ready'){const d=v.reducedMotion?0:Math.sin(t/800+i)*1.5;line([[e.x+19,e.y-6+d],[e.x+19,e.y+2+d]],'#fff2b4',1.8);line([[e.x+15,e.y-2+d],[e.x+23,e.y-2+d]],'#fff2b4',1.8);}
    }
    oval(e.x+1,e.y+27,14,3,'#bd8b4529');
    if(!ready){
      const wobble=e.status==='cracking'&&!v.reducedMotion?Math.sin(t/62+i)*1.3:0;
      image(eggArt(e.egg),x+wobble,y-pulse,60,60+pulse,e.flipped);
      if(e.status==='cracking')line([[e.x-8,e.y+3],[e.x-2,e.y],[e.x+2,e.y+5],[e.x+8,e.y+2]],'#886038',1.3);
    }else{
      const p=e.status==='hatching'?Math.min(1,(v.now-e.animationAt)/900):1;
      const hop=e.status==='hatching'&&!v.reducedMotion?Math.sin(p*Math.PI)*7:0;
      image(characterImage(e.egg,e.id),x,y-pulse-hop,60,60+pulse,e.flipped);
      if(e.status==='hatching'){
        ctx.save();ctx.globalAlpha=1-p;
        for(const direction of [-1,1]){
          ctx.save();ctx.translate(e.x+direction*(11+p*12),e.y+24-p*7);ctx.rotate(direction*p*.9);
          ctx.beginPath();ctx.moveTo(-9,-5);ctx.lineTo(-5,-1);ctx.lineTo(-1,-6);ctx.lineTo(3,-1);ctx.lineTo(8,-5);ctx.quadraticCurveTo(9,7,0,7);ctx.quadraticCurveTo(-10,6,-9,-5);ctx.fillStyle='#fff8dc';ctx.fill();ctx.strokeStyle=C.ink;ctx.lineWidth=1.2;ctx.stroke();ctx.restore();
        }
        if(!v.reducedMotion){star(e.x-20,e.y+2-p*12,3,'#fff5b0');star(e.x+21,e.y-p*7,2,'#fff5b0');}ctx.restore();
      }
    }
  }
  function flights(v){
    const t=performance.now();
    for(const f of v.flights){
      const p=Math.min(1,(t-f.born)/MOTION.flightMs),ease=1-(1-p)**2;
      if(v.reducedMotion)continue;
      const size=60-30*p,x=f.x+(MOTION.basketX-15-f.x)*ease,y=f.y+(MOTION.basketY-30-f.y)*ease-Math.sin(p*Math.PI)*52;
      image(characterImage(f.e.egg,f.e.id),x,y,size,size,f.e.flipped,p>.93?(1-p)/.07:1);
    }
  }
  function feedback(v){
    const t=performance.now(),events=(v.feedbacks??[]).filter(f=>t-f.born>=MOTION.flightMs);
    if(events.length){const newest=events.at(-1),p=(t-newest.born-MOTION.flightMs)/(MOTION.feedbackMs-MOTION.flightMs);if(p<1){ctx.save();ctx.globalAlpha=1-p;box(266,287-p*12,48,20,9,'#fff6d0',C.ink,1.2);text('+'+events.reduce((sum,event)=>sum+(event.amount??1),0)+' CP',290,297-p*12,10,C.ink);ctx.restore();}}
  }
  function kitchen(s,v){
    const stage=s.kitchenLevel===1?remasterStage:kitchenStage(s.kitchenLevel);
    room(stage);
    const batch=s.batch,remaining=batch?.eggs.filter(e=>!e.collected)??[],ready=remaining.filter(e=>['ready','hatching'].includes(e.status)).length;
    // Keep the architecture visible; batch counts share the timer beneath the nest.
    header(s,v);
    ctx.save();ctx.translate(0,L.eggOffset);
    tray(stage);
    if(s.dirty)grime(stage);
    if(s.duck)[0,1].forEach(i=>button(duckRect(i),v,'duck:'+i,s.egg===i?C.yellow:C.paper,()=>image(eggArt(i),7,duckRect(i).y-5,30,30)));
    for(const [i,e] of (batch?.eggs??[]).entries())if(!e.collected)drawEgg(e,i,v);
    tray(stage,true);basket(stage,v);flights(v);basket(stage,v,true);feedback(v);
    if(!remaining.length){const returning=Object.values(s.total).some(n=>n>0);ctx.fillStyle='#fff7de';ctx.strokeStyle='#b18a54';ctx.lineWidth=1.5;ctx.beginPath();ctx.roundRect(65,217,200,55,10);ctx.fill();ctx.stroke();text(returning?'选一件厨具，准备下一锅':'点下方保温灯，开始第一批',165,237,12,'#68482d',700,'center',184);text(returning?'换个搭配，遇见新伙伴':'免费调理24枚蛋',165,258,12,'#795b3c',500);}
    ctx.restore();
    const readyAt=E.batchReadyAt(batch);
    const progress=readyAt!==null?Math.max(0,Math.min(1,(v.now-batch.started)/(readyAt-batch.started))):0;
    if(remaining.length){pressed(v,'alarm',()=>clock(27,L.timerY+13,s.alarm));
      box(53,L.timerY,253,26,13,'#fff7d7','#997044',1.5);
      if(progress>0){ctx.save();ctx.beginPath();ctx.roundRect(56,L.timerY+3,247,20,10);ctx.clip();box(56,L.timerY+3,247*progress,20,0,ready===remaining.length?'#afd578':'#a8d6b3',null);ctx.restore();}
      const seconds=Math.max(0,Math.ceil(((readyAt??v.now)-v.now)/1000));
      const timer='剩余 '+Math.floor(seconds/60)+':'+String(seconds%60).padStart(2,'0');
      const status=ready?'可收取 '+ready+' / '+remaining.length+' · '+(seconds?timer:'轻划收取'):'孵化中 · '+timer;
      text(status,179,L.timerY+13,12,C.ink,700,'center',237);
    }else{text('选择下方厨具开始',160,L.timerY+13,12,'#8f693c',700);}
    cookware(s,v,remaining);
  }
  function cookware(s,v,remaining){
    box(0,469+L.extra,320,33,0,'#f6e3b6',null);
    box(4,380+L.extra,312,89,14,'#fff3ce','#b58a4d',1.7);line([[16,383+L.extra],[303,383+L.extra]],'#fffde6',2);
    ctx.save();ctx.beginPath();ctx.rect(8,L.toolY-2,304,L.toolHeight+3);ctx.clip();
    const position=v.toolPosition??v.toolScroll;
    for(let id=0;id<TOOL_COUNT;id++){
      const slot=id-position,lv=s.toolLevels[id],r=toolRect(slot),active=remaining.length>0&&s.batch.tool===id;
      if(r.x+r.w<8||r.x>312)continue;
      if(slot)line([[r.x-4,395+L.extra],[r.x-4,455+L.extra]],'#e7cca0',1);
      pressed(v,'tool:'+id,down=>{
        if(active||down){box(r.x,r.y,r.w,r.h-1,10,down?'#efbe65':'#ffe19a','#c18a3d',1.2);if(active)box(r.x+17,r.y+2,r.w-34,3,2,'#cf8240',null);}
        oval(r.x+34.5,r.y+48,21,3,'#c79b5333');
        image(utensil(id,Math.max(0,lv)),r.x+12,r.y+6,45,43,false,lv>=0?1:.45);
        if(lv>0){oval(r.x+54,r.y+12,8,8,C.yellow,C.ink,1);text(lv+1,r.x+54,r.y+12,10,C.ink,800);}
        text(E.label(E.tool(id)),r.x+34.5,r.y+57,11,C.ink,700,'center',65);
        const info=lv>=0?E.cookInfo(s,id,v.now):null;
        // The confirmation shows seconds; tiny cookware labels need bounded precision.
        text(info?Number(info.minutes.toFixed(1))+'分·'+info.cost+'CP':'去购买',r.x+34.5,r.y+74,10,lv>=0?'#795b39':'#aa8e68',600,'center',66);
      });
    }
    ctx.restore();
    for(const [id,r,disabled]of [['prev',RECT.prev,v.toolScroll===0],['next',RECT.next,v.toolScroll>=TOOL_SCROLL_MAX]]){
      ctx.save();ctx.globalAlpha=disabled?.4:1;button(r,v,id,'#ffedc3',()=>{const x=r.x+r.w/2,y=r.y+11;line(id==='prev'?[[x+3,y-4],[x-2,y],[x+3,y+4]]:[[x-3,y-4],[x+2,y],[x-3,y+4]],C.ink,2);});ctx.restore();
    }
    text('厨具 '+(v.toolScroll+1)+'–'+(v.toolScroll+4)+' / '+TOOL_COUNT,160,485+L.extra,11,'#8c693e',600);
  }
  function farm(s,v){
    farmRenderer.draw(s,v);header(s,v);
  }
  function pageBackdrop(page){
    box(0,0,320,L.height,0,page===2?'#e0b77d':page===4?'#bcccaa':page===5?'#ecc88f':page===6?'#c6d8ae':'#e0c598',null);
    if(page===5){for(let x=0;x<320;x+=40){box(x,58,20,16,0,'#e98a63',null);box(x+20,58,20,16,0,'#fff3d6',null);}line([[0,74],[320,74]],'#b9824a',2);}
    else if(page===6){for(let y=90;y<L.navY-12;y+=46){ctx.save();ctx.setLineDash([6,7]);line([[10,y],[110,y+14],[210,y-6],[310,y+10]],'#7f9a6c88',2);ctx.restore();}}
    else if(page===2){for(let y=58;y<L.navY-4;y+=44){line([[0,y],[320,y]],'#c3986355',2);line([[0,y+2],[320,y+2]],'#fae0ad66',1);}}
    else if(page===4){for(let y=74;y<L.navY-6;y+=29){oval(4,y,8,3,'#77976955');oval(316,y+12,8,3,'#77976955');}}
    else{for(let x=0;x<320;x+=53)line([[x,58],[x,L.navY-4]],'#a9845155',2);}
  }
  return {paint(s,v){
    const scale=ctx.canvas.width/L.width;ctx.setTransform(scale,0,0,scale,0,0);ctx.clearRect(0,0,L.width,L.height);ctx.imageSmoothingEnabled=true;ctx.imageSmoothingQuality='high';
    if(v.page<0){titleRenderer(v);return;}
    if(v.page===0)kitchen(s,v);else if(v.page===1)farm(s,v);else{pageBackdrop(v.page);header(s,v,v.page===5?'小店生意簿':v.page===6?'寻访与地区':'');}
    if(!v.externalNavigation)navigation(v.page,v);
  }};
}
