import * as E from './engine.js';
import {COLOR as C,LAYOUT as L,NAV,FONT} from './theme.js';
import {timeZone} from './farm.js';
import {characterImage,toolImage} from './catalog.js';
const root='/assets/png/';
const character=characterImage;
const utensil=(id,lv=0)=>toolImage(1,id,lv);
const seasoning=id=>toolImage(2,id);
export function createRenderer(ctx,image){
  function box(x,y,w,h,r,fill,stroke=C.ink,line=2){ctx.beginPath();ctx.roundRect(x,y,w,h,r);ctx.fillStyle=fill;ctx.fill();if(stroke){ctx.strokeStyle=stroke;ctx.lineWidth=line;ctx.stroke();}}
  function text(value,x,y,size=14,fill=C.paper,outline=0,align='center'){ctx.font=`800 ${size}px ${FONT}`;ctx.textAlign=align;ctx.textBaseline='middle';ctx.lineJoin='round';if(outline){ctx.strokeStyle=C.ink;ctx.lineWidth=outline;ctx.strokeText(String(value),x,y);}ctx.fillStyle=fill;ctx.fillText(String(value),x,y);}
  function tile(x,y,w,h,active=false){box(x,y+3,w,h,10,'#9a744c',C.ink,1.5);box(x,y,w,h,10,active?C.yellow:C.purple,C.ink,1.8);box(x+4,y+4,w-8,3,2,active?'#fff0a8':C.purpleLight,null);}
  function icon(kind,x,y,size=28){
    const atlas={kitchen:4,farm:5,book:6,shop:7};if(atlas[kind]!==undefined){image('/web/art/ui-atlas.png#'+atlas[kind],x-2,y-2,size+4,size+4);return;}
    ctx.save();ctx.translate(x,y);ctx.scale(size/32,size/32);ctx.lineWidth=2.5;ctx.strokeStyle=C.ink;ctx.lineJoin='round';ctx.lineCap='round';
    const path=(points,fill,close=true)=>{ctx.beginPath();points.forEach(([a,b],i)=>i?ctx.lineTo(a,b):ctx.moveTo(a,b));if(close)ctx.closePath();ctx.fillStyle=fill;if(close)ctx.fill();ctx.stroke();};
    if(kind==='kitchen'){image(character(0,0),-5,-8,42,42);}
    if(kind==='farm'){path([[3,14],[16,3],[29,14]],'#ee805f');box(6,14,20,16,2,'#f8c68a');box(12,20,8,10,1,'#704f59');}
    if(kind==='book'){box(4,3,24,28,3,'#f9c955');box(7,3,21,24,3,'#9781cb');image(character(0,0),6,3,23,23);}
    if(kind==='shop'){box(5,12,23,17,2,'#f4d997');box(11,18,8,11,1,'#96745c');path([[2,13],[7,3],[26,3],[31,13]],'#f17e66');path([[11,4],[9,13]],'#fff');path([[22,4],[24,13]],'#fff');}
    if(kind==='gear'){ctx.translate(16,16);for(let i=0;i<8;i++){ctx.rotate(Math.PI/4);box(-3,-15,6,10,1,'#fff4da');}ctx.beginPath();ctx.arc(0,0,10,0,Math.PI*2);ctx.fillStyle='#fff4da';ctx.fill();ctx.stroke();ctx.beginPath();ctx.arc(0,0,4,0,Math.PI*2);ctx.fillStyle=C.purple;ctx.fill();ctx.stroke();}
    ctx.restore();
  }
  function header(s,title){
    box(9,10,43,43,21,C.yellow,C.ink,2.5);image(character(0,0),5,2,49,49);text('Lv.'+(s.kitchenLevel+1),30,47,11,'#fff',2.5);
    box(65,13,195,34,15,'#fff4cf',C.ink,2);box(69,17,25,25,13,'#eeb443','#6c442c');text('C',81.5,30,15,'#fff2a3');text(s.cp+' CP',168,30,19,C.ink);
    tile(275,13,34,34);icon('gear',281,19,22);
    if(title){box(101,67,118,24,12,'#fff9df','#a07f53',1.5);text(title,160,79,12,C.ink);}
  }
  function nav(page){box(-4,497,328,76,0,'#b3dfce',null);NAV.forEach((n,i)=>{const x=8+i*78;tile(x,503,70,57,n.id===page);icon(n.icon,x+21,508,28);text(n.title,x+35,550,12,C.ink);});}
  function kitchen(s,v){
    image('/web/art/kitchen-v3.png',0,0,320,568);
    if(s.dirty){ctx.fillStyle='#47384235';ctx.fillRect(0,110,320,236);for(let i=0;i<13;i++){ctx.beginPath();ctx.ellipse(25+i*22,310+(i%3)*6,7,2,0,0,7);ctx.fill();}}
    const batch=s.batch,remaining=batch?.eggs.filter(e=>!e.collected)??[],ready=remaining.filter(e=>['ready','hatching'].includes(e.status)).length;
    header(s,remaining.length?`可收取 ${ready} / ${remaining.length}`:'选择厨具开始孵化');
    tile(8,106,42,47);image(seasoning(s.selected[0]??0),11,105,37,37);text('调味料',29,146,9,C.ink);
    if(s.selected.length){box(36,101,16,16,8,C.yellow);text(s.selected.length,44,109,9,C.ink);}
    if(s.dirty){tile(270,106,42,47);image(root+'MainGame/kitchen_fix_0_0.png',274,107,34,34);text('清洁',291,146,9,C.ink);}
    if(s.duck){[0,1].forEach(id=>{box(9,159+id*29,25,27,8,s.egg===id?C.yellow:C.purple);image(root+`Egg/egg_${id}_0_0.png`,8,155+id*29,28,28);});}
    const t=performance.now()/100;
    for(const [i,e]of (batch?.eggs??[]).entries()){
      if(e.collected)continue;
      const pulse=(1-Math.cos((t+i*.32)*.55))*1.5,x=e.x-30,y=e.y-30;
      if(e.status==='egg'||e.status==='cracking'){
        const wobble=e.status==='cracking'?Math.sin(t*3+i)*1.1:0;
        image(root+`Egg/egg_${e.egg}_0_${e.status==='egg'?0:1}.png`,x+wobble,y-pulse,60,60+pulse,e.flipped);
      }else{
        image(character(e.egg,e.id),x,y-pulse,60,60+pulse,e.flipped);
        if(e.status==='hatching')image(root+`Egg/egg_${e.egg}_0_2.png`,x,y,60,60,e.flipped,Math.max(0,1-(v.now-e.animationAt)/900));
      }
    }
    for(const f of v.flights){const p=Math.min(1,(performance.now()-f.born)/650),ease=1-(1-p)**3;const x=f.x+(262-f.x)*ease,y=f.y+(313-f.y)*ease-Math.sin(p*Math.PI)*65,size=60-25*p;image(character(f.e.egg,f.e.id),x,y,size,size,f.e.flipped,1-p*.65);text('+1',283,312-p*19,13,C.yellow,2);}
    if(!remaining.length){text('轻点下方厨具',160,240,14,'#68503c');text('开始新一轮孵化',160,262,11,'#806549');}
    const progress=batch?Math.max(0,Math.min(1,(batch.ends-v.now)/(batch.ends-batch.started))):0;
    box(48,L.timerY,250,26,13,remaining.length?C.ink:C.paper);
    if(remaining.length){box(80,L.timerY+4,Math.max(1,213*progress),18,9,C.mint,null);image(utensil(batch.tool,batch.level),48,L.timerY-5,37,37);const sec=Math.max(0,Math.ceil((batch.ends-v.now)/1000));text(sec?`${Math.floor(sec/60)}:${String(sec%60).padStart(2,'0')}`:'可以收取',190,L.timerY+13,12,C.paper,2);image(root+`MainGame/alarm_${s.alarm?'on':'off'}.png`,11,L.timerY-2,29,29);}
    else text('准备就绪',173,L.timerY+13,12,C.muted);
    for(let slot=0;slot<4;slot++){
      const id=v.toolScroll+slot,lv=s.toolLevels[id],x=L.toolX+slot*(L.toolWidth+L.toolGap),active=remaining.length>0&&batch.tool===id;
      tile(x,L.toolY,L.toolWidth,L.toolHeight,active);image(utensil(id,Math.max(0,lv)),x+8,L.toolY+2,53,53,false,lv>=0?1:.45);
      text(E.label(E.tool(id)),x+L.toolWidth/2,L.toolY+55,10,C.ink);
      box(x+8,L.toolY+66,L.toolWidth-16,16,8,'#fff9e8',null);text(lv>=0?E.cookInfo(s,id).cost+' CP':'未购买',x+L.toolWidth/2,L.toolY+74,9,lv>=0?C.ink:C.muted);
      if(active){text('▼',x+L.toolWidth/2,L.toolY-5,14,C.yellow,2);}
    }
    text('‹',12,488,19,C.ink);text(`${v.toolScroll+1}—${v.toolScroll+4} / 8`,160,489,8,C.ink);text('›',307,488,19,C.ink);
  }
  function farm(s,v){
    const zone=timeZone(v.now);image(root+`Tool/Tool0/tool_0_1_${zone}_0.jpg`,-v.farmScroll,0,830,568);
    image(root+`Farm/farm_house_${zone}_0.png`,2-v.farmScroll,136,90,90);
    image(root+`Farm/monster_village_${zone}_0.png`,235-v.farmScroll,146,65,65);image(root+`Farm/jinja_house_${zone}_0.png`,355-v.farmScroll,148,65,65);
    for(const w of v.walkers){const p=(1-Math.cos(performance.now()/500+w.id)) * 2;if(w.shadow!==null)image(root+'Character/character_shadow.png',w.x-v.farmScroll,w.y+w.shadow,48,48);image(character(w.egg,w.id),w.x-v.farmScroll,w.y-p,48,48+p,w.dir<0);}
    image(root+`Tool/Tool0/tool_0_1_0_front_${zone===30?1:0}.png`,-v.farmScroll,465,830,55);
    header(s,'农场');box(9,465,98,25,11,C.ink);image(root+'MainGame/farm_fix_0_0.png',10,460,32,32);text(E.farmHP(s,v.now)+'%',70,478,12,C.yellow);text('拖动画面探索农场',215,479,10,'#fff',2);
  }
  return {paint(s,v){ctx.setTransform(2,0,0,2,0,0);ctx.clearRect(0,0,320,568);ctx.imageSmoothingEnabled=true;ctx.imageSmoothingQuality='high';
    if(v.page<0){image('/web/art/kitchen-v3.png',0,0,320,568);box(26,51,268,83,18,C.ink);text('鸡宝厨房',160,88,32,C.yellow);text('今天也有新的鸡宝诞生',160,119,11,C.paper);for(let i=0;i<3;i++)image(character(0,[0,3,4][i]),52+i*69,193,82,82);tile(72,405,176,49,true);text(v.loaded?'开始游戏':'正在载入…',160,431,19,C.ink);return;}
    if(v.page===0)kitchen(s,v);else if(v.page===1)farm(s,v);else{box(0,0,320,568,0,'#c4e9da',null);for(let y=55;y<500;y+=26)for(let x=0;x<320;x+=26)if((x+y)%52===3)box(x,y,26,26,0,'#d3f0e6',null);header(s,v.page===2?'商店':v.page===4?'鸡宝图鉴':'设置');}
    nav(v.page);
  }};
}
