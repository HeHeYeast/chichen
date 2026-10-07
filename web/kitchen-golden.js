// Presentation coordinates only. Never migrate or overwrite saved egg positions.
import * as E from './engine.js';
import {LAYOUT as L} from './theme.js';
import {characterImage,toolImage} from './catalog.js';
import {TOOL_COUNT} from './content-pack.js';
import {NESTS,EGG_ASPECT} from './kitchen-egg-nests.js';
import {levelBadge,levelBadgeSrc} from './level-badge.js';
const A='/web/art/golden-kitchen/';
export const KITCHEN_ART=[...Array.from({length:4},(_,i)=>['header','status-surround','wall','support','front','counter'].map(n=>`${A}lv${i+1}-${n}.png`)).flat(),...['preparation-frame','plaque','timber','clock','lemon','tool-0','tool-1','tool-2','tool-3','lv1-front-lip','lv2-front-lip'].map(n=>A+n+'.png')];
// Lv.1/Lv.2 fronts carry bed interior above the rim; their cut lips occlude eggs instead.
const LIPS=['lv1-front-lip','lv2-front-lip','lv3-front','lv4-front'];
// Whole original eggs. In the 120px sprites the egg occupies (20,34)-(100,120).
const EGG_SPRITE=(egg,frame=0)=>`/assets/png/Egg/egg_${egg?1:0}_0_${frame}.png`;
export const GOLDEN_LAYOUTS=[
 {start:148,front:380,rows:[[124,152,180,208,236,264,299,29,32],[119,149,179,209,239,269,314,31,33],[113,145,177,209,241,273,330,33,34],[109,143,176,210,244,277,347,36,34]]},
 {start:157,front:368,rows:[[113,145,177,209,241,274,277,32,37],[109,142,176,209,242,278,292,34,38],[104,140,175,210,245,281,311,36,39],[99,137,175,213,251,288,330,39,39]]},
 {start:157,front:373,rows:[[112,145,178,211,245,278,287,33,36],[107,141,176,211,246,282,302,35,37],[102,139,175,211,248,285,320,37,38],[94,134,174,214,254,294,338,42,35]]},
 {start:157,front:381,rows:[[120,150,181,212,243,274,299,31,36],[114,148,181,214,247,280,314,33,37],[107,143,180,217,253,289,331,36,38],[101,139,176,214,252,291,348,38,33]]},
];
const levelIndex=level=>Math.max(0,Math.min(3,level|0));
const GOLDEN_HEIGHT=720;
// A natural nest: 24 overlapping eggs sorted back to front (see kitchen-egg-nests.js).
export function goldenEgg(level,index){const nest=NESTS[levelIndex(level)],[x,bottom,scale,tilt]=nest.eggs[index],h=nest.eggH*scale,w=h*EGG_ASPECT;return {x:x-w/2,y:bottom-h,w,h,tilt};}
export const eggFloor=level=>NESTS[levelIndex(level)].floor;
// The room fills the available height; eggs must retain their original shape.
// Cancel the room's vertical stretch for both egg size and row spacing, anchored
// to the front of the nest. Rendering and hit targets share this correction.
// Golden-space height of one golden-space width unit, so a box drawn w x (w*sceneRatio()) is square on screen.
const sceneRatio=()=>(320/390)/((L.navY-4)/GOLDEN_HEIGHT);
export function goldenEggDraw(level,index){const r=goldenEgg(level,index),front=GOLDEN_LAYOUTS[levelIndex(level)].front,ratio=sceneRatio();return {...r,y:front+(r.y-front)*ratio,h:r.h*ratio};}
export function goldenToScene(r){return {x:r.x*320/390,y:r.y*(L.navY-4)/GOLDEN_HEIGHT,w:r.w*320/390,h:r.h*(L.navY-4)/GOLDEN_HEIGHT};}
export function goldenRect(id,level=0){
 const one=levelIndex(level)===0;
 const rects={level:one?[51,4,45,50]:[13,5,56,53],settings:one?[285,2,44,53]:[319,3,57,55],ingredient:[24,538,306,40],clean:one?[260,102,83,39]:[282,110,89,40],batch:one?[42,97,216,50]:[10,104,270,53],'nest-prompt':[100,316,191,48],alarm:[55,430,282,30],prev:[20,680,53,35],next:[318,680,53,35],eggArea:[65,270,257,113],'harvest-allocation':[105,329,185,38],supply:[16,467,80,39],inventory:[104,467,80,39],workshop:[215,467,77,39],help:[299,467,75,39]};
 let a=rects[id];
 if(id==='eggArea'){const eggs=Array.from({length:24},(_,i)=>goldenEggDraw(level,i)),top=Math.min(...eggs.map(e=>e.y))-.1,bottom=Math.max(...eggs.map(e=>e.y+e.h))+.1;a=[65,top,257,bottom-top];}
 if(id.startsWith('slot:'))a=[22+Number(id.slice(5))*88,600,82,74];
 if(id.startsWith('duck:'))a=[16+Number(id.slice(5))*45,510,44,26];
 return a?goldenToScene({x:a[0],y:a[1],w:a[2],h:a[3]}):null;
}
export const goldenEggHit=(level,index)=>goldenToScene(goldenEggDraw(level,index));

export function createGoldenKitchen(ctx,image){
 const ink='#3e2514',muted='#725c42',paper='#fff9e8';
 function box(x,y,w,h,r,fill=paper,stroke='#69472b',width=1.5){ctx.beginPath();ctx.roundRect(x,y,w,h,r);ctx.fillStyle=fill;ctx.fill();if(stroke){ctx.strokeStyle=stroke;ctx.lineWidth=width;ctx.stroke();}}
 function text(s,x,y,size=14,color=ink,weight=700,align='center',max=Infinity){
  ctx.font=`${weight} ${size}px "Microsoft YaHei", "Chicken UI", sans-serif`;
  const width=ctx.measureText(String(s)).width;if(width>max){size*=max/width;ctx.font=`${weight} ${size}px "Microsoft YaHei", "Chicken UI", sans-serif`;}
  ctx.textAlign=align;ctx.textBaseline='middle';ctx.fillStyle=color;ctx.fillText(String(s),x,y);
 }
 const art=(name,x,y,w,h)=>image(A+name+'.png',x,y,w,h);
 // Draw an original egg sprite so its visible egg fills r, tilted about its contact point.
 function eggSprite(src,r,tilt=0,alpha=1){const sx=r.w/80,sy=r.h/86;ctx.save();ctx.translate(r.x+r.w/2,r.y+r.h);if(tilt)ctx.rotate(tilt*Math.PI/180);image(src,-r.w/2-20*sx,-r.h-34*sy,120*sx,120*sy,false,alpha);ctx.restore();}
 function green(x,y,w,h,label){
  const g=ctx.createLinearGradient(0,y,0,y+h);g.addColorStop(0,'#83c558');g.addColorStop(1,'#4e9b42');box(x,y,w,h,h/2,g,'#285e28',1.6);box(x+2.7,y+2.5,w-5.4,h-5,Math.max(1,h/2-3),'#ffffff08','#a6da80',1);text(label,x+w/2,y+h/2+1,h*.56,'#fffef0',800);
 }
 function tool(id,lv,x,y,w,h){if(id<4&&lv===0){const ratio=[41/51,58/47,64/48,53/52][id],dw=Math.min(w,h*ratio),dh=dw/ratio;art('tool-'+id,x+(w-dw)/2,y+(h-dh)/2,dw,dh);}else image(toolImage(1,id,Math.max(0,lv)),x,y,w,h);}
 return function(s,v){
  ctx.save();ctx.scale(320/390,(L.navY-4)/GOLDEN_HEIGHT);
  const level=levelIndex(s.kitchenLevel),n=level+1,g=GOLDEN_LAYOUTS[level],one=level===0;
  const pending=v.pending??new Set(),remaining=s.batch?.eggs.filter((e,i)=>!e.collected&&!pending.has(i))??[],ready=remaining.filter(e=>['ready','hatching'].includes(e.status)).length;
  const status=remaining.length?(ready===remaining.length?'可收取':ready?'部分可收':'孵化中'):s.batch?'已收完':'待开锅';
  box(0,0,390,GOLDEN_HEIGHT,0,'#b4824e',null);
  art('lv'+n+'-header',0,0,390,one?96:104);
  art('lv'+n+'-status-surround',0,one?96:104,390,g.start-(one?96:104));
  art('lv'+n+'-wall',0,g.start,390,244-g.start);
  art('lv'+n+'-support',0,244,390,g.front-244);
  art('lv'+n+'-front',0,g.front,390,431-g.front);
  art('lv'+n+'-counter',0,429,390,33);
  text(s.cp.toLocaleString('en-US'),one?198:209,one?30:34,one?23:27,ink,800,'center',one?126:159);
  // 厨房等级头像 (level-badge.js), the same picture the farm shows; square on screen, centred where the header's own
  // chick used to be painted (tools/build-level-badges.py --clean-headers took that one out).
  {const b=levelBadge(level),w=one?48:60,h=w*b.h/b.w*sceneRatio(),x=(one?73:42)-w/2,y=(one?30.5:33.5)-h/2,p=b.plate;
   image(levelBadgeSrc(level),x,y,w,h);
   text('Lv.'+n,x+(p.x+p.w/2)/b.w*w,y+(p.y+p.h/2)/b.h*h,Math.min(13,p.h/b.h*h*.95),'#5a3a1c',900,'center',p.w/b.w*w*.9);}
  text('鸡宝厨房 Lv.'+n,one?193:195,one?76:82,one?18:20,'#20150b',800,'center',one?146:177);
  const sx=one?42:10,sy=one?97:104,sw=one?306:370,sh=one?50:53;
  box(sx,sy+2,sw,sh,11,'#835c3580',null);box(sx,sy,sw,sh,11,paper,'#705033',1.3);
  const current=s.batch?.tool??null;
  if(current!==null)tool(current,s.batch.level,sx+7,sy+6,47,39);else tool(0,0,sx+10,sy+6,39,39);
  const currentIngredients=(s.batch?.ingredients??[]).map(i=>E.label(E.ingredient(i))).join('、');
  text(current!==null?'本锅：'+E.label(E.tool(current)):'本锅：待开始',sx+60,sy+16,one?12:14,ink,800,'left',one?79:108);
  text(currentIngredients?'已用'+currentIngredients:'未放调味',sx+60,sy+34,one?10:12,muted,600,'left',one?80:110);
  const badgeX=one?178:183,badgeW=one?69:85;green(badgeX,sy+10,badgeW,29,status);
  // Engine percent is dirt accumulation. Present actual cleanliness, not its inverse.
  const clean=100-E.kitchenCleanInfo(s,v.now).percent,cx=one?260:284;
  text('清洁度',cx,sy+14,10,ink,600,'left');box(cx,sy+27,one?43:45,12,6,'#ebdbc0','#664425',1);
  if(clean>0){ctx.save();ctx.beginPath();ctx.roundRect(cx+1,sy+28,(one?41:43)*clean/100,10,5);ctx.clip();box(cx+1,sy+28,one?41:43,10,5,'#70b85b',null);ctx.restore();}
  text(clean+'%',one?320:349,sy+34,one?12:13,ink,700);
  for(const [i,e]of(s.batch?.eggs??[]).entries()){
   if(e.collected||pending.has(i))continue;const r=goldenEggDraw(level,i),cx=r.x+r.w/2,bottom=r.y+r.h;
   // Contact shadow first, so each egg settles into the bed and onto the one behind it.
   ctx.beginPath();ctx.ellipse(cx,bottom-r.h*.04,r.w*.47,r.w*.15,0,0,Math.PI*2);ctx.fillStyle='rgba(84,48,16,.24)';ctx.fill();
   if(['ready','hatching'].includes(e.status)){
    // Character art is authored in a square 120px cell; keep that cell square, not egg-shaped.
    const cw=r.w*1.68,ch=cw*sceneRatio();image(characterImage(e.egg,e.id),cx-cw/2,bottom-ch*.94,cw,ch,e.flipped);
    if(e.status==='hatching')eggSprite(EGG_SPRITE(e.egg,2),r,0,.55);
   }else eggSprite(EGG_SPRITE(e.egg,e.status==='cracking'?1:0),r,r.tilt);
  }
  // The rim sits in front of the nest and hides the lowest part of the front eggs.
  art(LIPS[level],0,g.front,390,431-g.front);
  if(!remaining.length&&!s.batch){box(100,316,191,48,10,'#fff9e4ee','#b28a56',1);text('点下面的厨具开火',195,333,15);text('每锅 24 枚蛋',195,353,12,muted,600);}
  const tx=one?74:55,tw=one?238:281;
  box(tx,431,tw,28,14,paper,'#5c3c24',1.5);art('clock',tx,431,28,28);
  const seconds=Math.max(0,Math.ceil(((E.batchReadyAt(s.batch)??v.now)-v.now)/1000));
  const time=Math.floor(seconds/60)+':'+String(seconds%60).padStart(2,'0');
  const timer=remaining.length?`${remaining.length}枚 · ${status} · ${ready===remaining.length?'轻划收取':'剩余 '+time}`:s.batch?'这锅都收好了':'锅是空的';
  text(timer,tx+tw/2+13,446,one?12:14,ink,800,'center',tw-38);
  if(s.alarm){box(tx+17,430,10,10,5,'#ffd86a','#74532d',.7);text('✓',tx+22,435,8);}
  art('timber',0,461,390,48);ctx.fillStyle='#bd874c';ctx.fillRect(197,471,2,25);
  art('preparation-frame',0,508,390,212);art('plaque',111,509,168,28);text('下一锅',195,523,18,ink,800);
  // The egg kind belongs to the next pot, beside its plaque rather than over the wall art.
  if(s.duck){box(16,510,89,26,13,'#fff8e3','#5c3c24',1.4);box(17.5+s.egg*44,511.5,42,23,11.5,'#ffe39c',null);ctx.strokeStyle='#d8c29b';ctx.lineWidth=1;ctx.beginPath();ctx.moveTo(60.5,515);ctx.lineTo(60.5,531);ctx.stroke();[0,1].forEach(i=>text(i?'鸭蛋':'鸡蛋',38.5+i*44,523.5,13,s.egg===i?ink:muted,s.egg===i?800:600));}
  const ids=s.selected??[],names=ids.map(i=>E.label(E.ingredient(i))).join('、'),capacity=Math.min(3,n);
  // The whole seasoning row opens 下一锅 (see goldenRect.ingredient); the dish sits close to the frame edge.
  for(let i=0;i<capacity;i++){const x=26+i*38,id=ids[i];box(x,541,34,34,17,id===undefined?'#eee1c5':'#fff6dc',id===undefined?'#d6c19c':'#c9a46a',1);
   if(id!==undefined){if(E.label(E.ingredient(id))==='柠檬')art('lemon',x-2,542,38,30);else image(toolImage(2,id),x+1,543,32,29);}}
  const tx2=26+capacity*38+4;
  text('调味料',tx2,547,13,muted,700,'left');text(names||'还没放',tx2,566,15,ink,800,'left',244-tx2);green(250,543,80,30,'调整');
  ctx.strokeStyle='#e8d3b1';ctx.lineWidth=1;ctx.beginPath();ctx.moveTo(24,580);ctx.lineTo(366,580);ctx.stroke();
  text('点厨具开火',195,591,13,ink,700);ctx.strokeStyle='#caa470';ctx.beginPath();ctx.moveTo(79,591);ctx.lineTo(148,591);ctx.moveTo(242,591);ctx.lineTo(312,591);ctx.stroke();
  for(let slot=0;slot<4;slot++){
   const id=v.toolScroll+slot,lv=s.toolLevels[id],x=22+88*slot,active=s.batch?.tool===id&&remaining.length>0,locked=lv<0;
   box(x,600,82,74,8,active?'#ffe5a0':'#faf4e4',active?'#efac24':'#efdfc2',active?1.7:1);
   ctx.save();if(locked)ctx.globalAlpha=.46;tool(id,lv,x+16,602,50,42);ctx.restore();
   text(E.label(E.tool(id)),x+41,652,13,ink,800,'center',77);
   const info=locked?null:E.cookInfo(s,id,v.now);text(info?`${Number(info.minutes.toFixed(1))}分 · ${info.cost}CP`:'去购买',x+41,667,11,muted,600,'center',78);
   if(lv>0){box(x+56,602,24,15,6,'#fff3c9','#d0ac6e',.6);text('Lv.'+(lv+1),x+68,609.5,10);}
  }
  for(const [x,dir] of [[21,-1],[319,1]]){box(x,683,51,31,9,'#a77840',null);box(x,681,51,31,9,'#f5e4c5',ink,1.5);text(dir===-1?'‹':'›',x+25,695,30,ink,800);}
  // Where the four visible cookware sit among all of them: one dot per cookware, the visible ones filled.
  const step=25,left=195-(TOOL_COUNT*step-3)/2;
  box(left-5,683,TOOL_COUNT*step+7,30,10,'#f7f0dd','#efdfc2',1);box(left-3+v.toolScroll*step,685,4*step+3,26,8,'#ffe5a0','#efac24',1.2);
  for(let i=0;i<TOOL_COUNT;i++){const on=i>=v.toolScroll&&i<v.toolScroll+4;ctx.save();ctx.globalAlpha=on?1:(s.toolLevels[i]<0?.3:.55);tool(i,Math.max(0,s.toolLevels[i]),left+i*step,688,22,20);ctx.restore();}
  // Existing reward flight remains visual feedback only; commit is in app.js.
  for(const f of v.flights){const p=Math.min(1,(performance.now()-f.born)/720),r=goldenEgg(level,f.index??0);if(!v.reducedMotion)image(characterImage(f.e.egg,f.e.id),r.x+(31-r.x)*p,r.y+(24-r.y)*p-Math.sin(p*Math.PI)*32,40-20*p,(40-20*p)*sceneRatio(),false,1-p);}
  ctx.restore();
 };
}
