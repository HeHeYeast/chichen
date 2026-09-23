import {LAYOUT as L,RECT,FONT,MOTION} from './theme.js';

// The original cover is composited at its native proportions. Only the quiet
// wallpaper band repeats on tall phones; the two mascots are never stretched.
export const TITLE_ART={background:'/assets/png/MainMenu/main_bg.jpg',logo:'/assets/png/MainMenu/main_logo_cn.png'};
export function createTitleRenderer(ctx,image){
  const label=(value,x,y,size,color,weight=700)=>{
    ctx.font=`${weight} ${size}px ${FONT}`;ctx.textAlign='center';ctx.textBaseline='middle';ctx.fillStyle=color;ctx.fillText(value,x,y);
  };
  return function paintTitle(v){
    const extra=L.extra,t=v.reducedMotion?0:v.now/1000;
    ctx.fillStyle='#fff0cd';ctx.fillRect(0,0,320,L.height);
    image(TITLE_ART.background+'#top',0,0,320,167);
    ctx.save();ctx.beginPath();ctx.rect(0,167,320,56+extra);ctx.clip();
    for(let y=167;y<223+extra;y+=56)for(let x=0;x<320;x+=240)image(TITLE_ART.background+'#wall',x,y,240,56);
    ctx.restore();
    image(TITLE_ART.background+'#table',0,223+extra,320,345);

    // A few wisps rise from the pot; they stop with reduced-motion enabled.
    ctx.save();ctx.lineCap='round';ctx.strokeStyle='#fffdf2';ctx.lineWidth=2.2;
    for(let i=0;i<3;i++){
      const phase=v.reducedMotion ? .35 : ((t*.22+i/3)%1),x=277+i*12,y=235+extra-phase*44;
      ctx.globalAlpha=.5*Math.sin(phase*Math.PI);ctx.beginPath();
      ctx.moveTo(x,y);ctx.bezierCurveTo(x-7,y-6,x+7,y-12,x,y-19);ctx.stroke();
    }ctx.restore();

    const logoY=151+extra*.4+(v.reducedMotion?0:Math.sin(t*1.3)*1.3);
    image(TITLE_ART.logo+'#mark',15,logoY,290,290*184/599);
    label('鸡宝和鸭宝，等你开饭',160,logoY+106,12,'#785135');

    const r=RECT.start,down=v.pressedId==='start';
    ctx.save();ctx.translate(0,down?MOTION.pressDepth:0);
    ctx.fillStyle='#9f592f';ctx.strokeStyle='#603e27';ctx.lineWidth=2;
    ctx.beginPath();ctx.roundRect(r.x,r.y+4,r.w,r.h-4,23);ctx.fill();ctx.stroke();
    ctx.fillStyle=down?'#eeb450':'#ffdb7b';ctx.beginPath();ctx.roundRect(r.x,r.y,r.w,r.h-5,23);ctx.fill();ctx.stroke();
    ctx.strokeStyle='#fff3be';ctx.lineWidth=2;ctx.beginPath();ctx.roundRect(r.x+5,r.y+4,r.w-10,r.h-14,19);ctx.stroke();
    label(v.recovery?'恢复存档':v.loaded?'开始游戏':'正在载入…',r.x+r.w/2-8,r.y+21,19,'#684126',900);
    if(v.loaded){ctx.fillStyle='#8f5829';ctx.beginPath();ctx.moveTo(r.x+r.w-33,r.y+15);ctx.lineTo(r.x+r.w-25,r.y+21);ctx.lineTo(r.x+r.w-33,r.y+27);ctx.closePath();ctx.fill();}
    ctx.restore();
    label(v.recovery?'原进度已保留，请导入备份':v.loaded?'进度自动保存 · '+v.version:'厨房准备中，马上就好',160,L.height-13,10,'#694b34',500);
  };
}
