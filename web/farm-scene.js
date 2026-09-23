import {farmHP} from './engine.js';
import {timeZone} from './farm.js';
import {characterImage} from './catalog.js';
import {FONT,MOTION,LAYOUT as L,farmY} from './theme.js';
import {uiIcon} from './art/manifest.js';
import {FARM_ART,FARM_WORLD,FARM_ACTIONS,FARM_RECT} from './farm-theme.js';

const INK='#553c2b';

// The parent scene owns the HUD and navigation. This painter does not mutate state.
export function createFarmRenderer(ctx,image){
  function box(x,y,w,h,r,fill,stroke=INK,line=1.5){
    ctx.beginPath();ctx.roundRect(x,y,w,h,r);if(fill){ctx.fillStyle=fill;ctx.fill();}
    if(stroke){ctx.strokeStyle=stroke;ctx.lineWidth=line;ctx.stroke();}
  }
  function text(value,x,y,size=12,fill=INK,weight=700,align='center'){
    ctx.font=`${weight} ${size}px ${FONT}`;ctx.textAlign=align;ctx.textBaseline='middle';ctx.fillStyle=fill;ctx.fillText(String(value),x,y);
  }
  function ellipse(x,y,rx,ry,fill){ctx.beginPath();ctx.ellipse(x,y,rx,ry,0,0,Math.PI*2);ctx.fillStyle=fill;ctx.fill();}
  function line(points,color=INK,width=1.5){
    ctx.beginPath();points.forEach(([x,y],i)=>i?ctx.lineTo(x,y):ctx.moveTo(x,y));ctx.strokeStyle=color;ctx.lineWidth=width;ctx.lineJoin='round';ctx.lineCap='round';ctx.stroke();
  }
  function skyLight(zone,scroll,t,reducedMotion){
    const x=365-scroll,y=82;
    ctx.save();
    ctx.translate(0,farmY(y)-y);
    if(zone===30){
      // A single moon belongs to the world, so it scrolls with the countryside.
      ctx.beginPath();ctx.arc(x,y,13,.3,Math.PI*1.75);ctx.quadraticCurveTo(x-5,y+3,x+12,y+4);ctx.fillStyle='#fff2ba';ctx.fill();
      for(const [i,[sx,sy]]of [[190,68],[441,66],[590,86],[739,69]].entries()){
        ctx.globalAlpha=reducedMotion?.8:.65+Math.sin(t/1300+i)*.2;
        line([[sx-scroll-2,sy],[sx-scroll+2,sy]],'#fff1c9',1.5);line([[sx-scroll,sy-2],[sx-scroll,sy+2]],'#fff1c9',1.5);
      }
    }else{
      const color=zone===10?'#ffe992':'#ffcf87';
      ellipse(x,y,10,10,color);
      for(let i=0;i<8;i++){const a=i*Math.PI/4;line([[x+Math.cos(a)*14,y+Math.sin(a)*14],[x+Math.cos(a)*17,y+Math.sin(a)*17]],color,2);}
    }
    ctx.restore();
  }
  function buildingSigns(scroll,pressedId){
    for(const action of FARM_ACTIONS){
      const r=action.sign,x=r.x-scroll,down=pressedId===action.id;
      if(x+r.w<0||x>320)continue;
      ctx.save();ctx.translate(0,farmY(r.y)-r.y);if(down)ctx.translate(0,MOTION.pressDepth);
      box(x,r.y,r.w,r.h,3,down?'#efbd76':'#ffe2a4','#986c3a',1);
      line([[x+4,r.y+3],[x+r.w-4,r.y+3]],'#fff2c4',1);
      text(action.signText,x+r.w/2,r.y+r.h/2+.5,10.5,INK,800);
      ctx.restore();
    }
  }
  // Placeholder tags for displayed mementos until final memento art is delivered.
  const MEMENTO_TINT=['#e8c98f','#c9d9a5','#e6b7a0','#b8cfd8','#e8d2a8','#d8b8a0','#c4d0e0','#e0c8d8','#d9c79b','#a9c3a0','#d7b89a','#c9d4de'];
  function displayShelf(s,scroll){
    const shown=s.expansion?.collections?.display??[],base=475-scroll;if(base+80<0||base>320)return;
    ctx.save();ctx.translate(0,farmY(150)-150);
    line([[base,176],[base+74,176]],'#8a6337',2);
    shown.forEach((id,i)=>{const x=base+4+i*24;if(id){const n=Number(id.slice(1))-1;box(x,154,18,20,3,MEMENTO_TINT[n]??'#e8d6aa','#8a6337',1);line([[x+4,158],[x+14,158]],'#fff4d0',1);}else{ctx.setLineDash([2,2]);box(x,154,18,20,3,null,'#a88b5c',1);ctx.setLineDash([]);}});
    ctx.restore();
  }
  function walkers(list,scroll,t,reducedMotion,shadow){
    for(const walker of list){
      const x=walker.x-scroll;
      if(x>320||x+48<0)continue;
      const breathe=reducedMotion?0:(1-Math.cos(t/650+walker.id))*.9;
      const float=reducedMotion||walker.shadow!==null?0:Math.sin(t/1100+walker.id)*1.5;
      ctx.save();ctx.translate(0,farmY(walker.y)-walker.y);
      // The original shadow sprite occupies y98..120 on its 120px source canvas.
      if(walker.shadow!==null)ellipse(x+24,walker.y+43.6+walker.shadow,13.6,4.4,shadow);
      image(characterImage(walker.egg,walker.id),x,walker.y-breathe+float,48,48+breathe,walker.dir<0);
      ctx.restore();
    }
  }
  function meadowMotion(scroll,t,zone,reducedMotion){
    if(reducedMotion)return;
    ctx.save();
    for(const [i,base]of [176,399,714].entries()){
      const x=base-scroll+Math.sin(t/2700+i)*9,y=386+Math.sin(t/1900+i)*7+i*9;
      if(x<0||x>320)continue;
      ctx.globalAlpha=zone===30?.48:.32;
      ctx.save();ctx.translate(x,farmY(y));ctx.rotate(Math.sin(t/2200+i)*.7);
      if(zone===30){ellipse(0,0,1.6,1.6,'#fff1ad');}
      else{ctx.beginPath();ctx.moveTo(-3,0);ctx.quadraticCurveTo(0,-4,4,0);ctx.quadraticCurveTo(0,3,-3,0);ctx.fillStyle='#668d48';ctx.fill();}
      ctx.restore();
    }
    ctx.restore();
  }
  function emptyField(s,list){
    if(list.length)return;
    const total=Object.values(s.farm??{}).reduce((sum,count)=>sum+Math.max(0,Number(count)||0),0);
    // One small field sign gives an empty pasture a purpose without becoming a panel.
    line([[81,296],[81,320],[86,320],[86,296]],'#947448',3);
    line([[234,296],[234,320],[239,320],[239,296]],'#947448',3);
    box(49,245,222,65,8,'#fff2cb','#88663e',1.6);
    text(total?'鸡宝们暂时躲起来啦':'这里还没有鸡宝',160,266,14);
    text(total?'换个时段再来，或打开收成表':'到厨房孵化，让它们来这里散步',160,289,11,'#897049',500);
  }
  function hammer(x,y){
    line([[x-4,y+7],[x+5,y-5]],'#a4703f',4);
    ctx.save();ctx.translate(x+4,y-5);ctx.rotate(-.5);box(-6,-3,13,7,2,'#bdd0cd',INK,1.2);ctx.restore();
  }
  function actionButton(r,id,label,v,icon){
    box(r.x,r.y+3,r.w,r.h-3,8,'#a47c49',INK,1.4);
    ctx.save();if(v.pressedId===id)ctx.translate(0,MOTION.pressDepth);
    box(r.x,r.y,r.w,r.h-3,8,v.pressedId===id?'#ebca8e':'#fff2c8',INK,1.4);
    line([[r.x+7,r.y+3],[r.x+r.w-7,r.y+3]],'#fffbe2',1.5);
    if(icon==='repair')hammer(r.x+17,r.y+15);
    else image(uiIcon(6),r.x+6,r.y+3,24,26);
    text(label,r.x+62,r.y+16,11.5,INK,800);
    ctx.restore();
  }
  function bottomActions(s,v,zone,scroll){
    const hp=Math.max(0,Math.min(100,farmHP(s,v.now)));
    actionButton(FARM_RECT.repair,'farm:repair','整修 '+hp+'%',v,'repair');
    actionButton(FARM_RECT.harvest,'farm:harvest','收成表',v,'harvest');
    text(FARM_ART[zone].label+' · 左右拖动',160,446,9,zone===30?'#fff5d4':'#55432e',700);
    const r=FARM_RECT.shrine;
    box(r.x,r.y+3,r.w,r.h-3,8,'#a47c49',INK,1.4);
    const press=v.pressedId==='farm:fortune'?MOTION.pressDepth:0;
    box(r.x,r.y+press,r.w,r.h-3,8,v.pressedId==='farm:fortune'?'#e7c592':'#f8e5b2',INK,1.4);
    text('神社 ›',160,r.y+16+press,12,INK,800);
    if(v.shrineReady){ellipse(194,r.y+4,5,5,'#d46143');text('!',194,r.y+4,8,'#fff5d4',800);}
    box(119,498,82,3,1.5,'#65543488',null);
    box(119+scroll/FARM_WORLD.maxScroll*57,498,25,3,1.5,'#fff1bc',null);
    if(hp<30){ellipse(102,457,6.5,6.5,'#e8845b');text('!',102,457.5,9.5,'#fff7dc',900);}
  }
  return {draw(s,v){
    const zone=timeZone(v.now),art=FARM_ART[zone];
    const scroll=Math.max(0,Math.min(FARM_WORLD.maxScroll,Number(v.farmScroll)||0));
    const t=performance.now(),list=v.walkers??[];
    ctx.save();ctx.beginPath();ctx.rect(0,58,320,444+L.extra);ctx.clip();
    image(art.file,-scroll,-58*L.extra/444,FARM_WORLD.width,FARM_WORLD.height*(1+L.extra/444));
    skyLight(zone,scroll,t,v.reducedMotion);
    buildingSigns(scroll,v.pressedId);
    displayShelf(s,scroll);
    walkers(list,scroll,t,v.reducedMotion,art.shadow);
    meadowMotion(scroll,t,zone,v.reducedMotion);
    ctx.save();ctx.translate(0,farmY(245)-245);emptyField(s,list);ctx.restore();
    ctx.save();ctx.translate(0,L.extra);bottomActions(s,v,zone,scroll);ctx.restore();
    ctx.restore();
  }};
}
