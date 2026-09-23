// Design coordinates are shared by rendering and input. Gameplay keeps its original grid.
export const COLOR={ink:'#553c2b',purple:'#d9eacf',purpleLight:'#f5f9e6',yellow:'#ffd05a',mint:'#71bd99',paper:'#fff7d9',muted:'#937b59',coral:'#e97d55',shadow:'#ac793e'};
export const FONT='"Chicken UI", "Microsoft YaHei", "PingFang SC", "Noto Sans CJK SC", sans-serif';
export const LAYOUT={width:320,height:568,extra:0,eggOffset:0,navY:506,toolY:384,toolWidth:69,toolGap:8,toolX:10,toolHeight:82,timerY:343,pagerY:473,pagerHeight:25};
export const NAV=[{id:0,title:'厨房',icon:'kitchen'},{id:1,title:'农场',icon:'farm'},{id:4,title:'图鉴',icon:'book'},{id:2,title:'商店',icon:'shop'}];
export const RECT={level:{x:9,y:9,w:43,h:43},settings:{x:276,y:12,w:34,h:36},ingredient:{x:8,y:109,w:38,h:46},clean:{x:205,y:105,w:107,h:50},alarm:{x:10,y:341,w:34,h:31},prev:{x:8,y:473,w:42,h:25},next:{x:270,y:473,w:42,h:25},start:{x:70,y:493,w:180,h:48},eggArea:{x:47,y:168,w:224,h:145}};
export const navRect=i=>({x:6+i*79,y:LAYOUT.navY,w:71,h:54});
export const toolRect=slot=>({x:LAYOUT.toolX+slot*(LAYOUT.toolWidth+LAYOUT.toolGap),y:LAYOUT.toolY,w:LAYOUT.toolWidth,h:LAYOUT.toolHeight});
export const duckRect=i=>({x:8,y:167+i*31,w:29,h:29});
export const contains=(r,x,y)=>x>=r.x&&x<=r.x+r.w&&y>=r.y&&y<=r.y+r.h;
export const MOTION={flightMs:720,feedbackMs:1100,pressDepth:2.5,basketX:291,basketY:319};
export function canvasSizing(width,height,density=1){const scale=Math.min(width/LAYOUT.width,height/LAYOUT.height);return {scale,width:Math.max(1,Math.round(LAYOUT.width*scale*density)),height:Math.max(1,Math.round(LAYOUT.height*scale*density))};}
// Portrait phones gain room between the fixed-size HUD, nest and lower controls.
// Saved egg coordinates and the proportions of sprites are never changed.
export function fitViewport(width,height,density=1){
  const logicalHeight=Math.max(568,Math.min(800,320*height/width));
  const scale=Math.min(width/320,height/logicalHeight);
  return {logicalHeight,scale,width:Math.max(1,Math.round(320*scale*density)),height:Math.max(1,Math.round(logicalHeight*scale*density))};
}
export function setViewportHeight(height){
  LAYOUT.height=height;LAYOUT.extra=height-568;LAYOUT.eggOffset=LAYOUT.extra*.58;
  LAYOUT.navY=506+LAYOUT.extra;LAYOUT.toolY=384+LAYOUT.extra;
  LAYOUT.timerY=343+LAYOUT.extra;LAYOUT.pagerY=473+LAYOUT.extra;
  RECT.alarm.y=341+LAYOUT.extra;RECT.prev.y=RECT.next.y=473+LAYOUT.extra;
  RECT.start.y=493+LAYOUT.extra;
  RECT.eggArea.y=168+LAYOUT.eggOffset;
}
export const farmY=y=>y+(LAYOUT.extra*Math.max(0,y-58)/444);
export const farmRect=r=>({...r,y:farmY(r.y),h:r.h*(1+LAYOUT.extra/444)});
