// Kitchen Lv.2 reconstruction candidates A / B / C.
// Prototype only: reads shipped art, reuses the runtime egg-pile formula and HUD
// drawing verbatim, and never imports or writes game state.
const Q=new URLSearchParams(location.search);
const V=(Q.get('v')||'A').toUpperCase();
const STATE=Q.get('state')==='ready'?'ready':'incubating';
const SEED=Number(Q.get('seed')||7);
const W=innerWidth,H=innerHeight,K=W/390,HD=H/K,DPR=Math.max(2,Math.ceil(devicePixelRatio||1));
const INK='#553c2b',HUD_H=71,NAV_H=80;
const jobs=[],pending=[];

/* ---------- small helpers ---------- */
function rng(seed){let a=seed>>>0;return()=>{a=a+0x6D2B79F5|0;let t=Math.imul(a^a>>>15,1|a);t=t+Math.imul(t^t>>>7,61|t)^t;return((t^t>>>14)>>>0)/4294967296;};}
function div(cls='',style={},html=''){const e=document.createElement('div');if(cls)e.className=cls;Object.assign(e.style,style);if(html)e.innerHTML=html;return e;}
function put(parent,e,x,y,w,h){Object.assign(e.style,{position:'absolute',left:x+'px',top:y+'px'});if(w!=null)e.style.width=w+'px';if(h!=null)e.style.height=h+'px';parent.append(e);return e;}
function track(img){pending.push(img.decode?img.decode().catch(()=>{}):new Promise(r=>{img.onload=img.onerror=r;}));return img;}
function pic(src,w,h,style={}){const i=new Image();i.src=src;Object.assign(i.style,{width:w+'px',height:h+'px'},style);return track(i);}
const clamp=(v,a,b)=>Math.max(a,Math.min(b,v));
// Crop one frame of an atlas into a w×h box (contain), without bleeding neighbours.
function sprite(file,frame,size,w,h,{valign='center'}={}){
  const [fx,fy,fw,fh]=frame,s=Math.min(w/fw,h/fh),cw=fw*s,ch=fh*s;
  const box=div('spr',{width:w+'px',height:h+'px'});
  const clip=div('',{left:(w-cw)/2+'px',top:(valign==='bottom'?h-ch:(h-ch)/2)+'px',width:cw+'px',height:ch+'px'});
  const img=new Image();img.src=file;Object.assign(img.style,{left:-fx*s+'px',top:-fy*s+'px',width:size[0]*s+'px',height:size[1]*s+'px'});
  clip.append(track(img));box.append(clip);return box;
}

/* ---------- shipped art (paths and frames copied from web/art/manifest.js) ---------- */
const ICONS='/web/art/icons-v4-alpha.png',ICON_SIZE=[1774,887];
const ICON={hat:[44,467,359,362],barn:[463,467,391,359],book:[916,462,366,375],stall:[1366,450,368,401]};
const icon=(name,s)=>sprite(ICONS,ICON[name],ICON_SIZE,s,s);
const CA='/web/art/cookware-a-v15.png',CB='/web/art/cookware-b-v15.png';
const COOK=[[CA,[47,30,282,338]],[CA,[29,412,335,262]],[CA,[26,744,314,253]],[CA,[31,1080,328,313]],[CB,[41,115,302,241]],[CB,[32,438,312,243]],[CB,[47,740,276,299]],[CB,[40,1094,295,292]]];
const TOOLS=[{name:'保温灯',meta:'120分·0CP'},{name:'平底锅',meta:'15分·90CP'},{name:'水煮锅',meta:'60分·60CP'},{name:'油炸锅',meta:'30分·120CP'}];
const ACTIVE=2; // current batch uses 水煮锅 (matches the runtime baseline capture)
const tool=(id,w,h,valign='bottom')=>{const [file,f]=COOK[id];return sprite(file,[f[0]-2,f[1]-2,f[2]+4,f[3]+4],[1086,1448],w,h,{valign});};
const ING=id=>`/assets/png/Tool/Tool2/tool_2_${id}_0_0.png`; // 0 食盐土, 1 柠檬
const BROOM='/assets/png/MainGame/kitchen_fix_0_0.png';
const SIGN='/web/art/golden-business/sign-open.png';
const SVG={
  crate:s=>`<svg viewBox="0 0 48 48" width="${s}" height="${s}" fill="none" stroke="${INK}" stroke-width="2.5" stroke-linejoin="round"><ellipse cx="24" cy="44" rx="19" ry="2.6" fill="rgba(120,80,40,.18)" stroke="none"/><ellipse cx="17" cy="17" rx="6.4" ry="7.9" fill="#fff8e4"/><ellipse cx="30" cy="15.5" rx="6.4" ry="7.9" fill="#fff1cc"/><path d="M26 18.5a4 5 0 0 1 3-6" stroke="#fff" stroke-width="1.6"/><path d="M5 21h38v21H5z" fill="#e9ad67"/><path d="M5 21h38v5H5z" fill="#f6c98c"/><path d="M5 31.5h38" stroke="#b9793b" stroke-width="2.2"/><path d="M11 21v21M37 21v21" stroke-width="2.3"/><path d="M13.5 26.5l21 12M34.5 26.5l-21 12" stroke="#c98a4a" stroke-width="1.8"/></svg>`,
  hat:s=>`<svg viewBox="0 0 48 48" width="${s}" height="${s}" fill="none" stroke="${INK}" stroke-width="2.5" stroke-linejoin="round"><ellipse cx="24" cy="44.5" rx="15" ry="2.4" fill="rgba(120,80,40,.18)" stroke="none"/><path d="M12.5 30c-8-1.8-7.6-13.8.8-13.8C14.4 7.8 23 6.4 25.2 11.6 29.2 6.6 38.6 9.6 37.4 16.6c8.4.2 8.6 12 .4 13.4z" fill="#fffdf6"/><path d="M17 17.5c.6-3 2.6-4.6 5-5" stroke="#e6dccb" stroke-width="2"/><path d="M12.5 29.5h25V42h-25z" fill="#ffe7a8"/><path d="m25 32 1.6 3.2 3.5.5-2.5 2.4.6 3.4-3.2-1.7-3.2 1.7.6-3.4-2.5-2.4 3.5-.5z" fill="#f29b4a" stroke-width="1.4"/></svg>`,
  explore:s=>`<svg viewBox="0 0 32 32" width="${s}" height="${s}" fill="none" stroke="#68472e" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"><path fill="#e2bf76" d="M5 28q9-10 22-2"/><path d="M16 4v24"/><path fill="#88b77c" d="M5 5h18l5 5-5 5H5z"/><path fill="#f7d679" d="M27 16H10l-5 4 5 4h17z"/></svg>`,
  clock:s=>`<svg viewBox="0 0 30 30" width="${s}" height="${s}"><path d="M6.5 6.5 3 10M23.5 6.5 27 10" stroke="${INK}" stroke-width="3" stroke-linecap="round"/><circle cx="15" cy="16.5" r="10.5" fill="#fff5d3" stroke="${INK}" stroke-width="2"/><path d="M15 10.5v6l4.2 2.4" stroke="${INK}" stroke-width="2.2" fill="none" stroke-linecap="round" stroke-linejoin="round"/></svg>`,
  left:s=>`<svg viewBox="0 0 20 20" width="${s}" height="${s}"><path d="M12.5 4.5 7 10l5.5 5.5" fill="none" stroke="${INK}" stroke-width="2.6" stroke-linecap="round" stroke-linejoin="round"/></svg>`,
  right:s=>`<svg viewBox="0 0 20 20" width="${s}" height="${s}"><path d="M7.5 4.5 13 10l-5.5 5.5" fill="none" stroke="${INK}" stroke-width="2.6" stroke-linecap="round" stroke-linejoin="round"/></svg>`,
};
const svg=(name,s)=>div('ico',{width:s+'px',height:s+'px',position:'relative'},SVG[name](s));

/* ---------- canvas primitives, ported from web/scene.js ---------- */
function G(ctx){
  const face=(w,s)=>`${w} ${s}px "Chicken UI","Microsoft YaHei",sans-serif`;
  const box=(x,y,w,h,r,fill,stroke=INK,line=1.6)=>{ctx.beginPath();ctx.roundRect(x,y,w,h,r);ctx.fillStyle=fill;ctx.fill();if(stroke){ctx.strokeStyle=stroke;ctx.lineWidth=line;ctx.stroke();}};
  const text=(v,x,y,size=13,fill=INK,weight=700,align='center',maxW)=>{ctx.font=face(weight,size);if(maxW&&ctx.measureText(String(v)).width>maxW){size=Math.max(10,size*maxW/ctx.measureText(String(v)).width);ctx.font=face(weight,size);}ctx.textAlign=align;ctx.textBaseline='middle';ctx.fillStyle=fill;ctx.fillText(String(v),x,y);};
  const line=(pts,color=INK,width=1.6)=>{ctx.beginPath();pts.forEach((p,i)=>i?ctx.lineTo(...p):ctx.moveTo(...p));ctx.strokeStyle=color;ctx.lineWidth=width;ctx.lineCap='round';ctx.lineJoin='round';ctx.stroke();};
  const oval=(x,y,rx,ry,fill,stroke=null,width=1.5)=>{ctx.beginPath();ctx.ellipse(x,y,rx,ry,0,0,Math.PI*2);ctx.fillStyle=fill;ctx.fill();if(stroke){ctx.strokeStyle=stroke;ctx.lineWidth=width;ctx.stroke();}};
  const button=(r,fill,draw)=>{box(r.x,r.y+3,r.w,r.h-3,10,'#ae7e48',INK,1.5);box(r.x,r.y,r.w,r.h-3,10,fill,INK,1.6);line([[r.x+8,r.y+4],[r.x+r.w-8,r.y+4]],'#fff9dc',1.5);draw?.();};
  const gear=(x,y)=>{ctx.save();ctx.translate(x,y);for(let i=0;i<8;i++){ctx.rotate(Math.PI/4);box(-2.5,-11,5,7,1,'#fff8d9',INK,1.4);}oval(0,0,7.6,7.6,'#fff8d9',INK);oval(0,0,2.4,2.4,'#bd9162');ctx.restore();};
  return {box,text,line,oval,button,gear};
}
// Same placement/frame semantics as image() in web/app.js.
function drawSprite(ctx,img,spr,x,y,w,h,flip=false){
  ctx.save();ctx.translate(flip?x+w:x,y);if(flip)ctx.scale(-1,1);
  let dx=0,dy=0,dw=w,dh=h;
  if(spr.placement){const [px,py,pw,ph]=spr.placement;dx=w*px/120;dy=h*py/120;dw=w*pw/120;dh=h*ph/120;}
  const [sx,sy,sw,sh]=spr.frame;
  if(spr.fit){const s=Math.min(w/sw,h/sh);dw=sw*s;dh=sh*s;dx=(w-dw)/2;dy=(h-dh)/2;}
  ctx.drawImage(img,sx,sy,sw,sh,dx,dy,dw,dh);ctx.restore();
}
const cache={};
const load=src=>cache[src]??=new Promise((res,rej)=>{const i=new Image();i.onload=()=>res(i);i.onerror=()=>rej(Error('missing '+src));i.src=src;});

/* ---------- the nest: runtime tray parts + engine.js egg formula, unchanged ---------- */
const NEST={x:22,y:152,w:300,h:192};
const EGG={frame:[324,188,664,802],placement:[23,37,75,83]};
const CHICK=[{file:'/web/art/chick-v4-0.png',frame:[234,186,833,816],placement:[21,39,74,81]},{file:'/web/art/chick-v4-3.png',frame:[141,25,1106,1018],placement:[21,48,77,71]},{file:'/web/art/chick-v4-4.png',frame:[94,13,1113,1132],placement:[24,53,70,67]}];
const BED_PARTS=[[[10,196,1566,141],[30,161,258,26]],[[10,337,1566,282],[30,187,258,127]],[[10,619,1566,178],[30,314,258,25]]];
const VESSEL={frame:[45,209,646,372],fit:true};
const READY=new Set([2,7,9,14,19,22]);
function eggPile(seed){
  // engine.js startBatch(): 6×4 base with ±3 jitter, 60px sprites at 31/26 spacing, back-to-front.
  const r=rng(seed);
  const p=Array.from({length:24},(_,i)=>({x:Math.trunc(80.5+31*(i%6)+Math.trunc(3-r()*6)),y:184+15+26*Math.floor(i/6)+Math.trunc(3-r()*6)})).sort((a,b)=>a.y-b.y);
  return p.map((q,i)=>({...q,flipped:r()<.5,ready:STATE==='ready'&&READY.has(i),kind:i%3}));
}
function nestCanvas(scale,{vessel=true}={}){
  const c=document.createElement('canvas'),cw=NEST.w*scale,ch=NEST.h*scale;
  Object.assign(c.style,{width:cw+'px',height:ch+'px'});c.width=Math.round(cw*K*DPR);c.height=Math.round(ch*K*DPR);
  jobs.push(async()=>{
    const [bed,egg,ves,...chicks]=await Promise.all([load('/web/art/stage-bed-1-v6.png'),load('/web/art/egg-v4.png'),load('/web/art/stage-vessels-v5.png'),...CHICK.map(x=>load(x.file))]);
    const ctx=c.getContext('2d'),s=scale*K*DPR;ctx.setTransform(s,0,0,s,-NEST.x*s,-NEST.y*s);ctx.imageSmoothingQuality='high';
    const g=G(ctx);
    for(const [src,dst] of BED_PARTS)ctx.drawImage(bed,...src,...dst);
    for(const e of eggPile(SEED)){
      if(e.ready){g.oval(e.x,e.y+25,19,6,'#ebce7670');g.line([[e.x+19,e.y-6],[e.x+19,e.y+2]],'#fff2b4',1.8);g.line([[e.x+15,e.y-2],[e.x+23,e.y-2]],'#fff2b4',1.8);}
      g.oval(e.x+1,e.y+27,14,3,'#bd8b4529');
      if(e.ready)drawSprite(ctx,chicks[e.kind],CHICK[e.kind],e.x-30,e.y-30,60,60,e.flipped);
      else drawSprite(ctx,egg,EGG,e.x-30,e.y-30,60,60,e.flipped);
    }
    ctx.drawImage(bed,...BED_PARTS[2][0],...BED_PARTS[2][1]);
    if(vessel)drawSprite(ctx,ves,VESSEL,268,294,47,47);
  });
  return c;
}
// Place the nest so its tray centre sits at cx and its tray bottom at `bottom` (design px).
function placeNest(D,{scale,cx,bottom,vessel=true}){
  const left=cx-(159-NEST.x)*scale,top=bottom-(339-NEST.y)*scale;
  put(D,nestCanvas(scale,{vessel}),left,top);
  const X=lx=>left+(lx-NEST.x)*scale,Y=ly=>top+(ly-NEST.y)*scale;
  return {X,Y,scale,top:Y(161),bottom:Y(339),left:X(30),right:X(288)};
}

/* ---------- global frame: runtime HUD (canvas port) and approved nav ---------- */
function hud(D,{help=false}={}){
  const c=document.createElement('canvas');c.width=Math.round(390*K*DPR);c.height=Math.round(72*K*DPR);Object.assign(c.style,{width:'390px',height:'72px'});
  jobs.push(async()=>{
    const chick=await load(CHICK[0].file);
    const ctx=c.getContext('2d'),s=390/320*K*DPR;ctx.setTransform(s,0,0,s,0,0);const g=G(ctx);
    g.box(0,0,320,58,0,'#f7dc89',null);g.line([[0,56],[320,56]],'#b78949',2);
    g.button({x:9,y:9,w:43,h:43},'#ffd05a',()=>{drawSprite(ctx,chick,CHICK[0],8,-3,45,45);g.box(12,38,37,13,6,INK,null);g.text('Lv.2',30.5,44,10,'#fff6d5');});
    const pw=help?162:204;
    g.box(62,13,pw,34,17,'#fff9df',INK,1.8);g.oval(78,30,14,14,'#ffc943',INK,1.6);g.oval(78,29,9,9,'#ffe7a0',null);g.text('C',78,29,15,'#9b6331',900);
    g.text('1,280',62+pw/2+9,30,22,INK,800,'center',pw-50);
    if(help)g.button({x:230,y:9,w:37,h:42},'#fff4d2',()=>{g.text('?',248.5,23,19,INK,900);g.text('帮助',248.5,41,9);});
    g.button({x:273,y:7,w:44,h:44},'#f5e6bd',()=>{g.gear(295,23);g.text('设置',295,42,9);});
  });
  put(D,c,0,0);
}
function nav(D){
  const n=div('nav');
  for(const [label,make,on] of [['厨房',()=>icon('hat',34),1],['农场',()=>icon('barn',34)],['生意',()=>icon('stall',34)],['寻访',()=>svg('explore',34)],['图鉴',()=>icon('book',34)]]){
    const b=div(on?'on':'');b.append(make());const t=document.createElement('span');t.textContent=label;b.append(t);n.append(b);
  }
  put(D,n,0,HD-NAV_H);
}
function plaque(D,cx,y,w){
  const ready=STATE==='ready';
  return put(D,div('plaque'+(ready?' ready':''),{width:w+'px'},`<span class="clock">${SVG.clock(28)}</span><span class="txt">${ready?'可收取 6 / 24 · 剩余 12:05':'孵化中 · 剩余 57:40'}</span><span class="bar"><i style="width:${ready?82:38}%"></i></span>`),cx-w/2,y);
}
function scaled(D,e,x,y,w,h,s,origin='0 0'){put(D,e,x,y,w,h);e.style.transform=`scale(${s})`;e.style.transformOrigin=origin;
  const fx=origin.startsWith('100%')?1:origin.startsWith('50%')?.5:0;e.bounds=[x+w*(1-s)*fx,y,w*s,h*s];return e;}
// Functional zones (design px at the current viewport) for the comparison board overlay.
window.ZONES=[];
const zone=(kind,label,[x,y,w,h])=>window.ZONES.push({kind,label,x:+x.toFixed(1),y:+y.toFixed(1),w:+w.toFixed(1),h:+h.toFixed(1)});
const HELP_RECT=[230*390/320,9*390/320,37*390/320,42*390/320];

/* ---------- procedural Lv.2 log-cabin structure ---------- */
// Each log: chinked gap, lit upper band, body, shaded underside — a rounded profile in 4 flat tones.
const LOG={chink:'#b07d47',a:'#e7bd84',b:'#e4b87e',hi:'rgba(255,238,200,.55)',lo:'rgba(188,128,66,.2)',lo2:'rgba(150,98,46,.26)',grain:'rgba(158,104,52,.34)'};
const LOG_LIGHT={chink:'#caa06a',a:'#f1d7aa',b:'#eed2a2',hi:'rgba(255,250,232,.62)',lo:'rgba(200,150,95,.16)',lo2:'rgba(172,122,68,.2)',grain:'rgba(170,120,65,.24)'};
function logs(x0,y0,x1,y1,{lh=32,seed=1,pal=LOG}={}){
  const r=rng(seed),w=x1-x0;let s=`<rect x="${x0}" y="${y0}" width="${w}" height="${y1-y0}" fill="${pal.chink}"/>`;
  for(let yb=y1,i=0;yb>y0;yb-=lh,i++){
    const yt=Math.max(y0,yb-lh),top=yt+1.3,bot=yb-1.3,h=bot-top;if(h<=1)continue;
    s+=`<rect x="${x0}" y="${top}" width="${w}" height="${h}" fill="${i%2?pal.a:pal.b}"/>`;
    s+=`<rect x="${x0}" y="${top+1.5}" width="${w}" height="${Math.min(5,h*.17)}" fill="${pal.hi}"/>`;
    s+=`<rect x="${x0}" y="${bot-h*.32}" width="${w}" height="${h*.32}" fill="${pal.lo}"/><rect x="${x0}" y="${bot-3}" width="${w}" height="3" fill="${pal.lo2}"/>`;
    for(let g=0;g<2;g++){const gx=x0+r()*(w-70),gy=top+h*(.34+r()*.26),gl=22+r()*46;if(h>12)s+=`<path d="M${gx.toFixed(1)} ${gy.toFixed(1)}h${gl.toFixed(1)}" stroke="${pal.grain}" stroke-width="1.3" stroke-linecap="round"/>`;}
    if(r()<.32&&h>18){const kx=x0+24+r()*(w-48);s+=`<ellipse cx="${kx.toFixed(1)}" cy="${(top+h*.46).toFixed(1)}" rx="4.8" ry="2.6" fill="none" stroke="${pal.grain}" stroke-width="1.4"/>`;}
  }
  return s;
}
// Cabin corner: the stacked log ends that identify a log house at a glance.
function logEnds(cx,y0,y1,{lh=32,r=13.5}={}){
  let s='';for(let yb=y1;yb-lh/2>y0-4;yb-=lh){const cy=yb-lh/2;
    s+=`<circle cx="${cx}" cy="${cy}" r="${r}" fill="#ebc48e" stroke="#9c6d40" stroke-width="1.7"/><circle cx="${cx}" cy="${cy}" r="${r*.62}" fill="none" stroke="#d2a169" stroke-width="1.3"/><circle cx="${cx}" cy="${cy}" r="${r*.24}" fill="#d2a169"/>`;}
  return s;
}
// Lower wall behind the stage: vertical boards, darker, so the cream eggs read first.
function wainscot(x0,y0,x1,y1,{bw=39}={}){
  let s=`<rect x="${x0}" y="${y0}" width="${x1-x0}" height="${y1-y0}" fill="#c78d55"/>`;
  for(let x=x0,i=0;x<x1;x+=bw,i++){s+=`<rect x="${x}" y="${y0}" width="${bw}" height="${y1-y0}" fill="${i%2?'#c98f57':'#c4894f'}"/><rect x="${x+2}" y="${y0}" width="3" height="${y1-y0}" fill="rgba(236,185,125,.45)"/><path d="M${x} ${y0}V${y1}" stroke="#a4703d" stroke-width="1.8"/>`;}
  return s+`<rect x="${x0}" y="${y0}" width="${x1-x0}" height="8" fill="rgba(110,66,30,.2)"/>`;
}
function beam(x,y,w,h,{fill='#c98f55',hi='#e0ab70',lo='#ad7542',stroke='#7d5431'}={}){
  return `<rect x="${x}" y="${y}" width="${w}" height="${h}" fill="${fill}" stroke="${stroke}" stroke-width="1.8"/><rect x="${x+1}" y="${y+1.5}" width="${w-2}" height="3" fill="${hi}"/><rect x="${x+1}" y="${y+h-4.5}" width="${w-2}" height="3" fill="${lo}"/>`;
}
function background(D,inner,defs=''){
  put(D,div('bg',{height:HD+'px'},`<svg width="390" height="${HD}" viewBox="0 0 390 ${HD}" xmlns="http://www.w3.org/2000/svg" style="display:block"><defs>${defs}</defs>${inner}</svg>`),0,0);
}
function inlineSVG(w,h,inner){return `<svg width="${w}" height="${h}" viewBox="0 0 ${w} ${h}" style="position:absolute;left:0;top:0;overflow:visible">${inner}</svg>`;}

/* ======================================================================
   Candidate A · 木台正视: original reading order, every zone rebuilt as an object.
   ====================================================================== */
function candidateA(D){
  const navTop=HD-NAV_H,cT=navTop-204,colW=366/4,col=i=>12+colW*(i+.5);
  const nTop=cT+8-178*390/320,railY=Math.round(nTop+46);
  // Wall = log upper wall (function wall) + chair rail + board wainscot (stage backdrop).
  let s=logs(0,HUD_H,390,railY,{seed:SEED+11});
  s+=wainscot(0,railY+14,390,cT);
  s+=beam(-3,railY,396,14);
  for(const x of [-4,378])s+=`<rect x="${x}" y="${HUD_H}" width="16" height="${cT-HUD_H}" fill="#c98f55" stroke="#7d5431" stroke-width="1.8"/><rect x="${x+3}" y="${HUD_H}" width="3" height="${cT-HUD_H}" fill="#dfa96d"/>`;
  s+=beam(-2,HUD_H-3,394,21);
  for(let x=40;x<370;x+=62)s+=`<rect x="${x}" y="${HUD_H+17}" width="20" height="11" rx="1.5" fill="#b87e47" stroke="#7d5431" stroke-width="1.6"/><rect x="${x+2}" y="${HUD_H+19}" width="16" height="2.5" fill="#d39a5e"/>`;
  // Counter: a front-view surface, four vertical boards, and a cubby recess for cookware.
  s+=`<rect x="0" y="${cT-7}" width="390" height="7" fill="rgba(140,90,40,.14)"/>`;
  s+=`<rect x="-3" y="${cT+12}" width="396" height="${navTop-cT-10}" fill="#d99d5f" stroke="${INK}" stroke-width="2"/><rect x="0" y="${cT+13}" width="390" height="6" fill="#c3874b"/>`;
  for(let i=1;i<4;i++)s+=`<path d="M${12+colW*i} ${cT+118}V${navTop}" stroke="#c4874b" stroke-width="1.6"/>`;
  s+=`<rect x="12" y="${cT+22}" width="366" height="96" rx="10" fill="#b07540"/><path d="M13 ${cT+34}q0-11 11-11h342q11 0 11 11v3H13z" fill="#93602f"/><rect x="13" y="${cT+104}" width="364" height="13" fill="#d09a60"/>`;
  s+=`<ellipse cx="${col(ACTIVE)}" cy="${cT+78}" rx="40" ry="30" fill="#ffe7a3" opacity=".42"/><ellipse cx="${col(ACTIVE)}" cy="${cT+82}" rx="28" ry="20" fill="#fff0bf" opacity=".5"/>`;
  for(let i=1;i<4;i++)s+=`<rect x="${12+colW*i-2.5}" y="${cT+23}" width="5" height="81" fill="#93602f"/>`;
  s+=`<rect x="12" y="${cT+22}" width="366" height="96" rx="10" fill="none" stroke="${INK}" stroke-width="2.2"/>`;
  s+=`<rect x="-3" y="${cT}" width="396" height="14" rx="2" fill="#f6d8a3" stroke="${INK}" stroke-width="2.2"/><rect x="0" y="${cT+2.5}" width="390" height="3" fill="#fff1d2"/>`;
  background(D,s);
  const n=placeNest(D,{scale:390/320,cx:194,bottom:cT+8});
  plaque(D,194,cT-26,236);

  // Wall zone above the nest: seasoning shelf (left) + one hanging rail (right).
  const wT=HUD_H+32,wB=n.top-4,zh=wB-wT,gs=clamp(zh/124,.7,1),gy=wT+Math.max(2,(zh-112*gs)*.5);
  const shelf=div('group');
  shelf.innerHTML=inlineSVG(198,112,`<rect x="2" y="74" width="194" height="8" fill="rgba(140,90,40,.14)"/>
    <path d="M22 74h17l-13 23h-4z M158 74h17l-13 23h-4z" fill="#c68848" stroke="${INK}" stroke-width="1.8" stroke-linejoin="round"/>
    <rect x="0" y="62" width="198" height="13" rx="3" fill="#dc9e5d" stroke="${INK}" stroke-width="2.2"/><rect x="2" y="64.5" width="194" height="3" fill="#f2c48b"/>
    <ellipse cx="38" cy="62" rx="22" ry="3.5" fill="rgba(110,70,35,.22)"/>`);
  put(shelf,pic(ING(1),64,64),6,0);
  put(shelf,div('slot-empty',{},'+'),74,14,46,46);
  put(shelf,div('',{background:'#fff8e2',border:`2px solid ${INK}`,borderRadius:'9px',boxShadow:'0 2px 0 #b98a52',display:'flex',flexDirection:'column',alignItems:'center',justifyContent:'center',lineHeight:'1.15'},`<b style="font-size:14px;font-weight:900">调味料</b><span style="font-size:11.5px;font-weight:750;color:#7a5a3c">已选 1/2 <span class="chev">›</span></span>`),128,12,64,50);
  zone('seasoning','调味搁板',scaled(D,shelf,20,gy,198,112,gs).bounds);

  const rail=div('group'),pegs=[24,76,128];
  rail.innerHTML=inlineSVG(152,120,`<rect x="0" y="0" width="152" height="11" rx="2.5" fill="#c98f55" stroke="#7d5431" stroke-width="1.8"/><rect x="1" y="1.5" width="150" height="3" fill="#e0ab70"/>
    ${pegs.map(x=>`<path d="M${x} 7V22" stroke="#7a5234" stroke-width="1.8"/><circle cx="${x}" cy="6" r="3.6" fill="#7c5232"/>`).join('')}`);
  [['商店',()=>icon('stall',32)],['仓库',()=>svg('crate',32)],['手艺',()=>svg('hat',32)]].forEach(([label,make],i)=>{
    const t=div('tag');t.append(make());t.append(document.createTextNode(label));put(rail,t,pegs[i]-24,18,48,60);
  });
  const clean=div('chip',{gap:'5px',padding:'0 10px 0 6px'});
  clean.append(pic(BROOM,24,24));clean.insertAdjacentHTML('beforeend',`<span>清洁</span><span style="width:34px;height:6px;border-radius:3px;background:#ecdfbc;overflow:hidden;display:inline-block"><i style="display:block;width:18%;height:100%;background:#c9905c"></i></span><span class="dim">18%</span>`);
  put(rail,clean,4,90,144,28);
  const [rx,ry,rw,rh]=scaled(D,rail,230,gy-2,152,120,gs,'100% 0').bounds;
  zone('secondary','商店/仓库/手艺',[rx,ry,rw,rh*80/120]);zone('clean','清洁',[rx,ry+rh*88/120,rw,rh*32/120]);
  zone('nest','蛋窝',[n.left,n.top,n.right-n.left,n.bottom-n.top]);zone('status','状态',[76,cT-26,236,38]);
  zone('cookware','厨具',[12,cT+22,366,navTop-8-cT-22]);zone('current','本锅',[col(ACTIVE)-colW/2,cT+22,colW,140]);zone('help','帮助',HELP_RECT);

  // Cookware stands inside the counter cubbies; nameplates hang on the boards.
  TOOLS.forEach((t,i)=>{
    const cx=col(i);put(D,tool(i,62,54),cx-31,cT+50);
    if(i===ACTIVE)put(D,div('ribbon',{},'本锅'),cx-22,cT+26);
    put(D,div('plate'+(i===ACTIVE?' on':''),{},`<b>${t.name}</b><small>${t.meta}</small>`),cx-42,cT+125,84,36);
  });
  put(D,div('knob off',{},SVG.left(18)),16,navTop-39);put(D,div('knob',{},SVG.right(18)),344,navTop-39);
  put(D,div('pager',{},'下一锅 · 厨具 1–4 / 9'),95,navTop-32,200,20);
  hud(D,{help:true});nav(D);
}

/* ======================================================================
   Candidate B · 本锅 / 下一锅: a counter plank splits the stage from a prep bench.
   ====================================================================== */
function candidateB(D){
  const navTop=HD-NAV_H,tall=HD>=780,benchH=tall?262:190,plankTop=navTop-benchH-24,bT=plankTop+24,trayBottom=plankTop+8;
  const scale=clamp((trayBottom-HUD_H-22-104)/178,1.02,1.25);
  let s=logs(0,HUD_H,390,plankTop+4,{seed:SEED+23,pal:LOG_LIGHT});
  s+=beam(-2,HUD_H-3,394,21,{fill:'#d39b60',hi:'#ebbd84',lo:'#b88049',stroke:'#8a5f38'});
  s+=`<rect x="0" y="${bT}" width="390" height="${navTop-bT}" fill="#fff6de"/><rect x="0" y="${bT}" width="390" height="7" fill="rgba(170,120,60,.13)"/>`;
  s+=`<rect x="-4" y="${plankTop}" width="398" height="24" rx="4" fill="#e0a563" stroke="${INK}" stroke-width="2.4"/><rect x="0" y="${plankTop+3}" width="390" height="4" fill="#f4c88e"/><rect x="0" y="${plankTop+16}" width="390" height="4.5" fill="#c1824a"/><circle cx="18" cy="${plankTop+12}" r="2.6" fill="#7b5233"/><circle cx="372" cy="${plankTop+12}" r="2.6" fill="#7b5233"/>`;
  const col=i=>34+80.5*(i+.5);
  if(tall)s+=`<path d="M44 ${bT+184}H346" stroke="#dcc49c" stroke-width="2" stroke-dasharray="6 6" stroke-linecap="round"/>`;
  TOOLS.forEach((t,i)=>{const cy=tall?bT+99:bT+66,on=i===ACTIVE;
    if(on)s+=`<ellipse cx="${col(i)}" cy="${cy-18}" rx="38" ry="34" fill="#ffe9a6" opacity=".55"/>`;
    s+=`<ellipse cx="${col(i)}" cy="${cy}" rx="32" ry="8" fill="${on?'#ffd675':'#ecc28a'}" stroke="${INK}" stroke-width="1.8"/><ellipse cx="${col(i)}" cy="${cy-1.5}" rx="25" ry="4.6" fill="${on?'#ffe49c':'#f5d6a4'}"/>`;});
  background(D,s);
  const n=placeNest(D,{scale,cx:190,bottom:trayBottom});
  plaque(D,190,trayBottom-34,226);

  // Stage top: next-pot seasoning cubby (left) and a hanging 本锅 board (right).
  const wT=HUD_H+24,wB=n.top-4,zh=wB-wT,gs=Math.min(1,zh/118),gy=wT+Math.max(2,(zh-118*gs)/2);
  const cub=div('group');
  cub.innerHTML=inlineSVG(164,112,`<rect x="0" y="0" width="164" height="80" rx="8" fill="#d99a5a" stroke="${INK}" stroke-width="2.2"/><rect x="3" y="3" width="158" height="3.5" rx="1.5" fill="#f0c088"/>
    ${[10,86].map(x=>`<rect x="${x}" y="10" width="68" height="61" rx="6" fill="#a8703d"/><path d="M${x} 18q0-8 8-8h52q8 0 8 8v2H${x}z" fill="#8f5b30"/><rect x="${x}" y="62" width="68" height="9" fill="#c89156"/><rect x="${x}" y="10" width="68" height="61" rx="6" fill="none" stroke="${INK}" stroke-width="1.8"/>`).join('')}`);
  put(cub,pic(ING(1),60,60),14,10);
  put(cub,div('slot-empty',{borderColor:'rgba(255,236,200,.7)',color:'#f4dcae',background:'rgba(255,240,210,.08)'},'+'),97,17,46,44);
  const cc=div('chip',{},`下一锅调味 <span class="dim">1/2</span><span class="chev">›</span>`);put(cub,cc,8,86,148,26);
  zone('seasoning','下一锅调味',scaled(D,cub,12,gy,164,112,gs).bounds);

  // 本锅 board: tool + seasoning actually used + the cleanliness that affects this batch.
  const board=div('group',{background:`url(${SIGN}) center/100% 100% no-repeat`});
  put(board,div('ribbon',{height:'19px',fontSize:'11px'},'本锅'),12,30);
  put(board,tool(ACTIVE,50,42,'center'),14,48);
  put(board,div('',{color:'#fff6da',fontWeight:900,fontSize:'17px',letterSpacing:'.5px',whiteSpace:'nowrap',lineHeight:'20px'},'水煮锅'),68,46);
  const used=div('',{display:'flex',alignItems:'center',gap:'2px',color:'#e3eecb',fontSize:'11.5px',fontWeight:800,whiteSpace:'nowrap'});used.append(pic(ING(0),20,20));used.append('食盐土 · 24枚');put(board,used,66,68);
  const cl=div('',{display:'flex',alignItems:'center',gap:'5px',color:'#e3eecb',fontSize:'11px',fontWeight:800,whiteSpace:'nowrap'});
  cl.append(pic(BROOM,16,16));cl.insertAdjacentHTML('beforeend',`清洁 <span style="width:52px;height:6px;border-radius:3px;background:rgba(255,255,255,.26);overflow:hidden;display:inline-block"><i style="display:block;width:18%;height:100%;background:#f3d27a"></i></span> 18%`);
  put(board,cl,18,92);
  const [bx,by,bw,bh]=scaled(D,board,190,gy-4,190,121,gs,'100% 0').bounds;
  zone('current','本锅牌',[bx,by,bw,bh]);zone('clean','清洁',[bx+14*gs,by+88*gs,150*gs,20*gs]);
  zone('nest','蛋窝',[n.left,n.top,n.right-n.left,n.bottom-n.top]);zone('status','状态',[77,trayBottom-34,226,38]);zone('help','帮助',HELP_RECT);

  // Prep bench = 下一锅: cookware on coasters, then secondary entries as objects.
  if(tall)put(D,div('note',{textAlign:'center',fontSize:'12.5px',color:'#9b7b56'},'— 下一锅 · 点厨具开锅 —'),95,bT+10,200,18);
  const tY=tall?bT+44:bT+12;
  TOOLS.forEach((t,i)=>{const cx=col(i),on=i===ACTIVE;
    put(D,tool(i,62,56),cx-31,tY);
    if(on&&tall)put(D,div('ribbon',{},'本锅'),cx-22,bT+26);
    put(D,div('plate'+(on?' on':''),{padding:'0'},`<b>${t.name}</b>`),cx-37,tY+66,74,26);
    put(D,div('note',{textAlign:'center',color:'#86664a',fontSize:'11px'},t.meta),cx-40,tY+96,80,14);
  });
  put(D,div('knob off',{width:'28px',height:'28px'},SVG.left(16)),3,tY+18);put(D,div('knob',{width:'28px',height:'28px'},SVG.right(16)),359,tY+18);
  put(D,div('pager',{fontSize:'11px',color:'#a3845f'},'厨具 1–4 / 9'),145,tY+114,100,14);
  const items=[['商店',s=>icon('stall',s)],['仓库',s=>svg('crate',s)],['手艺',s=>svg('hat',s)]];
  if(tall)items.forEach(([label,make],i)=>{const cx=85+110*i,e=div('',{display:'flex',flexDirection:'column',alignItems:'center',gap:'3px',fontWeight:900,fontSize:'15px'});e.append(make(44));e.append(label);put(D,e,cx-40,bT+192,80,70);});
  else items.forEach(([label,make],i)=>{const cx=80+115*i,e=div('chip',{height:'34px',gap:'6px',padding:'0 12px 0 6px',fontSize:'14px'});e.append(make(26));e.append(label);put(D,e,cx-48,bT+144,96,34);});
  const cTop=tall?bT+26:tY;zone('cookware','厨具 · 下一锅',[3,cTop,384,tY+128-cTop]);zone('current','本锅',[col(ACTIVE)-40,cTop,80,tY+110-cTop]);
  zone('secondary','商店/仓库/手艺',tall?[45,bT+192,300,70]:[32,bT+144,326,34]);
  hud(D,{help:true});nav(D);
}

/* ======================================================================
   Candidate C · 木屋剖面: the Lv.2 cabin is the stage; next-pot inputs share one dock.
   ====================================================================== */
// mix=true is C′ (served as ?v=D): the same cabin, with seasoning back at the top-left.
function candidateC(D,mix=false){
  const navTop=HD-NAV_H,tall=HD>=780,dockH=mix?(tall?196:180):(tall?224:204),dockTop=navTop-dockH,sill=dockTop-14,trayBottom=sill+6;
  const scale=clamp((trayBottom-HUD_H-24-110)/178,1.0,1.2),nestTop=trayBottom-178*scale;
  const peakY=HUD_H+9,eaveY=clamp(nestTop-150,peakY+30,peakY+92),beamW=20;
  const slope=(eaveY-peakY)/207,inner=x=>peakY+beamW*.95+Math.abs(195-x)*slope; // underside of the roof beam
  const clip=`<clipPath id="cabin"><path d="M10 ${sill}L10 ${inner(10)}L195 ${inner(195)}L380 ${inner(380)}L380 ${sill}Z"/></clipPath>`;
  let s=`<rect width="390" height="${HD}" fill="#e7eed2"/>`;
  s+=`<path d="M0 ${HUD_H}H390V${eaveY+40}L195 ${peakY}L0 ${eaveY+40}Z" fill="#edf2da"/>`;
  s+=`<g clip-path="url(#cabin)">${logs(0,peakY,390,sill+2,{seed:SEED+37,lh:30})}</g>`;
  for(const x of [11,379])s+=logEnds(x,inner(x)+4,sill+2);
  // Two roof beams crossing at the ridge: the gable outline that says "cabin".
  const over=15,ends=[[-24,peakY+beamW/2+219*slope,195+over,peakY+beamW/2-over*slope],[414,peakY+beamW/2+219*slope,195-over,peakY+beamW/2-over*slope]];
  s+=ends.map(([x1,y1,x2,y2])=>`<path d="M${x1} ${y1}L${x2} ${y2}" stroke="${INK}" stroke-width="${beamW+4}"/>`).join('');
  s+=ends.map(([x1,y1,x2,y2])=>`<path d="M${x1} ${y1}L${x2} ${y2}" stroke="#bb7d45" stroke-width="${beamW}"/><path d="M${x1} ${y1-6}L${x2} ${y2-6}" stroke="#d69c60" stroke-width="3.5"/>`).join('');
  s+=`<circle cx="195" cy="${peakY+beamW/2}" r="5" fill="#7c5232" stroke="${INK}" stroke-width="1.6"/>`;
  // Sill + dock (the dock is the single next-pot station).
  s+=`<rect x="-3" y="${sill}" width="396" height="15" fill="#a8703d" stroke="${INK}" stroke-width="2.2"/><rect x="0" y="${sill+2.5}" width="390" height="3" fill="#c88c52"/>`;
  s+=`<rect x="-3" y="${dockTop}" width="396" height="${navTop-dockTop+3}" fill="#d59a5b" stroke="${INK}" stroke-width="2"/><rect x="-3" y="${dockTop}" width="396" height="12" fill="#f0ca92" stroke="${INK}" stroke-width="2.2"/><rect x="0" y="${dockTop+2.5}" width="390" height="3" fill="#fbe2b6"/>`;
  const r1=dockTop+(tall?20:16),r1h=mix?38:tall?56:50,r2=r1+r1h+(tall?10:8),colW=314/4,col=i=>38+colW*(i+.5),cubH=tall?74:68;
  TOOLS.forEach((t,i)=>{const x=col(i)-35,on=i===ACTIVE;
    s+=`<rect x="${x}" y="${r2+4}" width="70" height="${cubH}" rx="10" fill="${on?'#e7b262':'#a06839'}"/><path d="M${x} ${r2+14}q0-10 10-10h50q10 0 10 10v2H${x}z" fill="${on?'#d9a04f':'#86552c'}"/><rect x="${x}" y="${r2+4}" width="70" height="${cubH}" rx="10" fill="none" stroke="${INK}" stroke-width="2"/>`;
    if(on)s+=`<ellipse cx="${col(i)}" cy="${r2+cubH*.55}" rx="26" ry="20" fill="#fff0bf" opacity=".55"/>`;});
  background(D,s,clip);

  const n=placeNest(D,{scale,cx:195,bottom:trayBottom}),pw=mix?200:218;
  plaque(D,195,trayBottom-34,pw);
  put(D,div('chip',{height:'24px',fontSize:'12.5px',padding:'0 8px'},`仓库<span class="chev">›</span>`),n.X(291.5)-31,n.Y(331)-3,62,24);
  if(mix){const c=div('chip',{height:'24px',fontSize:'12.5px',padding:'0 8px 0 4px',gap:'2px'});c.append(pic(BROOM,20,20));c.append('18%');put(D,c,8,n.Y(331)-3,66,24);zone('clean','清洁',[8,n.Y(331)-3,66,24]);}

  // Ridge: the current pot hangs as a medallion — 本锅 is read before the eggs.
  const room=n.top-(peakY+26),d=clamp(room*.48,58,100),medTop=Math.max(peakY+beamW+8,n.top-22-30-d);
  const med=div('',{},inlineSVG(d,d,`<circle cx="${d/2}" cy="${d/2}" r="${d/2-1.2}" fill="#d89b5c" stroke="${INK}" stroke-width="2.4"/><circle cx="${d/2}" cy="${d/2}" r="${d/2-8}" fill="#fff6d8" stroke="#b27b45" stroke-width="1.8"/><circle cx="${d/2}" cy="3" r="4" fill="#7c5232" stroke="${INK}" stroke-width="1.4"/>`));
  put(med,tool(ACTIVE,d*.6,d*.52,'center'),d*.2,d*.23);
  const badge=div('',{width:d*.34+'px',height:d*.34+'px',borderRadius:'50%',background:'#fff8e2',border:`2px solid ${INK}`,display:'grid',placeItems:'center',boxShadow:'0 2px 0 #b98a52'});badge.append(pic(ING(0),d*.3,d*.3));
  put(med,badge,d*.7,d*.64);
  put(D,div('',{borderLeft:'2px solid #7a5234'}),194,peakY+beamW*.6,0,medTop-peakY-beamW*.6+2);
  put(D,med,195-d/2,medTop,d,d);
  const mw=mix?116:124;
  put(D,div('chip',{height:'26px',fontSize:mix?'13px':'13.5px'},`<span style="color:#b25b35">本锅</span> · 水煮锅`),195-mw/2,medTop+d+4,mw,26);

  // Under the roof slopes: cleanliness (left) and craft (right) hang as objects.
  const side=(make,dim)=>{const g=div('group');put(g,div('',{},inlineSVG(96,12,`<circle cx="48" cy="6" r="4" fill="#7c5232" stroke="${INK}" stroke-width="1.4"/>`)),0,0,96,12);put(g,make(),26,4);put(g,div('chip',{height:'26px',padding:'0 8px'},dim),6,56,84,26);return g;};
  // Side objects share the medallion's baseline, so the three read as one row above the nest.
  const rowBase=medTop+d+30,sideMax=n.top-4;
  const broom=side(()=>pic(BROOM,44,44,{transform:'rotate(-10deg)'}),`清洁 <span class="dim">18%</span>`);
  const craft=side(()=>svg('hat',44),`手艺<span class="chev">›</span>`);
  const fit=x=>{const top=Math.max(inner(x)+8,rowBase-84),s=Math.min(1,(Math.min(sideMax,rowBase)-top)/84);return [top,s];};
  const [ly,ls]=fit(76),[ry,rs]=fit(314);
  if(mix){
    // Hanging spice shelf under the left slope: two real slots + the entry, roped to the roof beam.
    const g=div('group'),H=92,top=Math.max(inner(70)+14,rowBase-H),gsc=Math.min(1,(Math.min(sideMax,rowBase)-top)/H);
    g.innerHTML=inlineSVG(112,H,`<rect x="0" y="46" width="112" height="11" rx="3" fill="#dc9e5d" stroke="${INK}" stroke-width="2.2"/><rect x="2" y="48.5" width="108" height="2.8" fill="#f2c48b"/><ellipse cx="32" cy="46" rx="19" ry="3" fill="rgba(110,70,35,.22)"/>`);
    put(g,pic(ING(1),50,50),7,0);put(g,div('slot-empty',{fontSize:'20px'},'+'),62,7,38,38);
    put(g,div('chip',{height:'26px',fontSize:'12.5px',padding:'0 6px'},`下一锅调味<span class="chev">›</span>`),0,64,112,26);
    const b=scaled(D,g,14,top,112,H,gsc,'50% 0').bounds;
    for(const x of [b[0]+7*gsc,b[0]+105*gsc])put(D,div('',{borderLeft:'2px solid #7a5234'}),x,inner(x)-2,0,top+46*gsc-inner(x)+2);
    zone('seasoning','调味 · 下一锅',b);
  }else zone('clean','清洁',scaled(D,broom,24,ly,96,84,ls,'50% 0').bounds);
  zone('secondary','手艺',scaled(D,craft,270,ry,96,84,rs,'50% 0').bounds);
  zone('current','本锅吊牌',[195-mw/2,medTop,mw,d+30]);zone('nest','蛋窝',[n.left,n.top,n.right-n.left,n.bottom-n.top]);
  zone('status','状态',[195-pw/2,trayBottom-34,pw,38]);zone('secondary','仓库',[n.X(291.5)-31,n.Y(331)-3,62,24]);zone('help','帮助',HELP_RECT);

  // Dock row 1: next-pot label, seasoning slots, shop. Row 2: cookware cubbies.
  put(D,div('',{background:'#8d5a30',border:`2px solid ${INK}`,borderRadius:'8px',boxShadow:'inset 0 2px 0 #a8713f,0 2px 0 #6a4224',color:'#fff1d0',fontWeight:900,fontSize:'14px',display:'grid',placeItems:'center',letterSpacing:'1px'},'下一锅'),12,r1+(r1h-30)/2,68,30);
  const cs=r1h-4,shopH=mix?36:40;
  if(mix)put(D,div('note',{color:'#5d3f27',fontSize:'12.5px',fontWeight:800},'点厨具开锅 · 换一件试试新搭配'),90,r1+(r1h-18)/2,200,18);
  else{
    const s1=div('',{background:'#fff3d2',border:`2px solid ${INK}`,borderRadius:'12px',boxShadow:'0 2px 0 #a8713f',display:'grid',placeItems:'center'});s1.append(pic(ING(1),cs-6,cs-6));put(D,s1,90,r1+2,cs,cs);
    put(D,div('slot-empty',{background:'rgba(255,243,210,.55)',borderColor:'rgba(85,60,43,.5)'},'+'),96+cs,r1+2,cs,cs);
    put(D,div('chip',{height:'28px',padding:'0 8px'},`调味 <span class="dim">1/2</span><span class="chev">›</span>`),104+cs*2,r1+(r1h-28)/2,78,28);
  }
  const shop=div('tag flat',{flexDirection:'row',justifyContent:'center',gap:'3px',padding:'0 8px 0 4px'});shop.append(icon('stall',mix?26:30));shop.append('商店');
  put(D,shop,300,r1+(r1h-shopH)/2+(mix?0:2),80,shopH);
  TOOLS.forEach((t,i)=>{const cx=col(i),on=i===ACTIVE;
    put(D,tool(i,56,50),cx-28,r2+cubH-26-50+18);
    if(on)put(D,div('ribbon',{},'本锅'),cx-22,r2-8);
    put(D,div('plate'+(on?' on':''),{padding:0},`<b style="font-size:13px">${t.name}</b>`),cx-37,r2+cubH-10,74,24);
    put(D,div('note',{textAlign:'center',color:'#5d3f27',fontSize:'10.5px'},t.meta),cx-40,r2+cubH+17,80,14);
  });
  put(D,div('knob off',{width:'28px',height:'28px'},SVG.left(16)),6,r2+22);put(D,div('knob',{width:'28px',height:'28px'},SVG.right(16)),356,r2+22);
  const pagerY=Math.min(navTop-18,r2+cubH+36);
  put(D,div('pager',{color:'#5d3f27',fontSize:'11px'},'厨具 1–4 / 9'),145,pagerY,100,14);
  if(!mix)zone('seasoning','调味 · 下一锅',[88,r1,104+cs*2+80-88,cs+4]);zone('secondary','商店',[300,r1+(r1h-shopH)/2,80,shopH]);
  zone('cookware','厨具 · 下一锅',[6,r2-8,378,pagerY+14-(r2-8)]);
  hud(D,{help:true});nav(D);
}

/* ---------- boot ---------- */
async function boot(){
  const phone=document.getElementById('phone');
  const D=div('design',{height:HD+'px',transform:`scale(${K})`});phone.append(D);
  await Promise.all([document.fonts.load('800 22px "Chicken UI"'),document.fonts.load('900 14px "Chicken UI"'),document.fonts.load('18px "Golden Display"')]).catch(()=>{});
  ({A:candidateA,B:candidateB,C:candidateC,D:d=>candidateC(d,true)}[V]??candidateA)(D);
  await Promise.all(jobs.map(j=>j()));
  await Promise.all(pending);
  await document.fonts.ready;
  document.documentElement.dataset.ready='1';
}
boot().catch(e=>{document.documentElement.dataset.ready='error';document.body.insertAdjacentHTML('beforeend',`<pre style="position:fixed;left:0;top:0;color:red;background:#fff;z-index:9;white-space:pre-wrap">${e.stack}</pre>`);});
