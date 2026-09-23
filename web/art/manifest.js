// Source frames use pixels; placement uses the original 120x120 sprite canvas.
// Keeping this metadata explicit prevents transparent padding changing gameplay scale.
import {KITCHEN_STAGES} from '../kitchen-stages.js';
import {SEASON_ART} from '../seasonal-pack.js';
import {TITLE_ART} from '../title-scene.js';
const atlas='/web/art/icons-v4-alpha.png';
const frames=[[56,16,335,396],[472,69,387,308],[916,64,381,318],[1353,44,383,358],[44,467,359,362],[463,467,391,359],[916,462,366,375],[1366,450,368,401]];
export const SPRITES=Object.fromEntries(frames.map((frame,i)=>[atlas+'#'+i,{file:atlas,frame,size:[1774,887],fit:true}]));
// Measured connected alpha bounds, not equal cells: handles and tall lids vary.
const cookwareAtlases=['/web/art/cookware-a-v15.png','/web/art/cookware-b-v15.png'];
const cookwareFrames=[
  [[47,30,282,338],[385,28,298,344],[734,72,323,294],[29,412,335,262],[374,407,352,269],[690,406,381,271],[26,744,314,253],[381,709,319,289],[738,701,326,301],[31,1080,328,313],[380,1019,314,378],[733,1027,324,374]],
  [[41,115,302,241],[380,91,323,272],[744,70,304,303],[32,438,312,243],[373,412,340,280],[743,393,311,304],[47,740,276,299],[427,735,262,304],[767,745,270,300],[40,1094,295,292],[386,1068,306,327],[745,1086,305,299]],
];
cookwareAtlases.forEach((file,atlasIndex)=>cookwareFrames[atlasIndex].forEach(([x,y,w,h],index)=>{
  SPRITES[file+'#'+index]={file,frame:[x-2,y-2,w+4,h+4],size:[1086,1448],fit:true};
}));
// The diagonal pan handles share rectangular bounds with the adjacent icon.
// Clip their empty corners without changing any pixels in the source atlas.
SPRITES[cookwareAtlases[0]+'#4'].clip=[[372,405],[729,405],[729,455],[640,640],[585,678],[372,678]];
SPRITES[cookwareAtlases[0]+'#5'].clip=[[1040,402],[1074,402],[1074,680],[688,680],[688,548],[780,459],[850,426]];
for(const stage of KITCHEN_STAGES){
  if(!stage.backgroundRows)continue;
  const [w,h]=stage.backgroundSize,[tableY,frontY]=stage.backgroundRows;
  const rows={wall:[0,0,w,tableY],table:[0,tableY,w,frontY-tableY],base:[0,frontY,w,h-frontY]};
  for(const [part,frame]of Object.entries(rows))SPRITES[stage.background+'#'+part]={file:stage.background,frame,size:[w,h]};
}
// Original 120px cookware has large transparent top margins. Normalize the visible
// frames at presentation time so advanced tiers don't look smaller than the pilot art.
const cookwareBounds=[[[9,45,116,117],[11,34,108,117],[11,39,108,117]],[[11,51,115,117],[12,52,118,117],[10,43,117,117]],[[6,55,113,117],[11,38,109,117],[10,19,110,117]],[[11,52,108,117],[12,26,109,117],[11,20,109,118]],[[10,46,111,117],[11,38,109,117],[13,31,111,118]],[[6,40,113,117],[11,26,109,117],[12,27,108,117]],[[14,29,101,117],[17,20,101,117],[18,26,101,117]],[[10,58,111,117],[11,50,108,117],[11,33,113,118]]];
cookwareBounds.forEach((levels,id)=>levels.forEach(([x,y,right,bottom],level)=>{const file=`/assets/png/Tool/Tool1/tool_1_${id}_${level}_0.png`;SPRITES[file]={file,frame:[x,y,right-x,bottom-y],size:[120,120],fit:true};}));
const beds='/web/art/stage-beds-v5b.png',vessels='/web/art/stage-vessels-v5.png';
[[60,148,620,403],[777,148,619,403],[1493,148,620,403]].forEach((frame,i)=>SPRITES[beds+'#'+i]={file:beds,frame,size:[2172,724]});
[[45,209,646,372],[742,194,584,407],[1392,194,582,407]].forEach((frame,i)=>SPRITES[vessels+'#'+i]={file:vessels,frame,size:[2022,778],fit:true});
const facility='/web/art/stage-facility-3-v6.png';
for(const [part,frame]of Object.entries({back:[13,64,1427,236],cushion:[13,300,1427,451],front:[13,751,1427,204]}))SPRITES[facility+'#'+part]={file:facility,frame,size:[1452,1083]};
const straw='/web/art/stage-bed-0-v6.png',cotton='/web/art/stage-bed-2-v6.png';
const shavings='/web/art/stage-bed-1-v6.png';
for(const [part,frame]of Object.entries({back:[10,196,1566,141],cushion:[10,337,1566,282],front:[10,619,1566,178]}))SPRITES[shavings+'#'+part]={file:shavings,frame,size:[1586,992]};
SPRITES[straw]={file:straw,frame:[65,110,1483,797],size:[1586,992]};
for(const [part,frame]of Object.entries({back:[14,138,1556,82],cushion:[14,220,1556,502],front:[14,722,1556,146]}))SPRITES[cotton+'#'+part]={file:cotton,frame,size:[1586,992]};
const characterFrames={0:{size:[1312,1199],frame:[234,186,833,816],placement:[21,39,74,81]},3:{size:[1350,1165],frame:[141,25,1106,1018],placement:[21,48,77,71]},4:{size:[1312,1199],frame:[94,13,1113,1132],placement:[24,53,70,67]}};
for(const [id,value] of Object.entries(characterFrames))SPRITES[`/web/art/chick-v4-${id}.png`]={file:`/web/art/chick-v4-${id}.png`,...value};
SPRITES['/web/art/egg-v4.png']={file:'/web/art/egg-v4.png',frame:[324,188,664,802],placement:[23,37,75,83]};
// Dim-sum expansion: measured from the original RGBA atlas, without editing pixels.
// Individual source frames keep the tall third steamer outside the row above.
const dimSum='/web/art/expansion-dim-sum.png';
const dimSumFrames=[[38,57,349,346],[459,60,344,349],[880,57,343,351],[32,477,358,321],[448,457,359,340],[864,463,368,337],[57,921,330,230],[472,874,315,286],[897,831,308,343]];
dimSumFrames.forEach((frame,index)=>{
  const sprite={file:dimSum,frame,size:[1254,1254]};
  if(index<6){
    const scale=Math.min(82/frame[2],80/frame[3]);
    const width=frame[2]*scale,height=frame[3]*scale;
    sprite.placement=[(120-width)/2,118-height,width,height];
  }else sprite.fit=true;
  SPRITES[dimSum+'#'+(index<6?'chick'+index:'tool'+(index-6))]=sprite;
});
export const resolveSprite=path=>SPRITES[path]??{file:path};
// The inner viewport clips the atlas to the source frame, including when the
// outer SVG has letterboxing in a wide or tall UI slot.
let spriteClipSerial=0;
export function spriteSVG(sprite){
  const [, ,w,h]=sprite.frame,id='sprite-clip-'+(++spriteClipSerial);
  const clip=sprite.clip?`<defs><clipPath id="${id}" clipPathUnits="userSpaceOnUse"><polygon points="${sprite.clip.map(p=>p.join(',')).join(' ')}"/></clipPath></defs>`:'';
  return `<svg class="sprite-art" viewBox="0 0 ${w} ${h}" aria-hidden="true"><svg width="${w}" height="${h}" style="width:${w}px;height:${h}px" viewBox="${sprite.frame.join(' ')}" overflow="hidden">${clip}<image ${clip?`clip-path="url(#${id})"`:''} href="${sprite.file}" width="${sprite.size[0]}" height="${sprite.size[1]}"/></svg></svg>`;
}
for(const [part,frame]of Object.entries({top:[0,0,640,334],wall:[0,334,480,112],table:[0,446,640,690]}))SPRITES[TITLE_ART.background+'#'+part]={file:TITLE_ART.background,frame,size:[640,1136]};
SPRITES[TITLE_ART.logo+'#mark']={file:TITLE_ART.logo,frame:[24,269,599,184],size:[640,1136]};
// Measured RGBA source bounds; a few transparent pixels preserve antialiasing.
const seasonalFrames=[[23,11,271,298],[335,30,274,279],[645,33,278,277],[959,25,278,286],
  [22,322,272,293],[336,349,275,266],[648,325,275,292],[963,326,275,291],
  [22,639,274,270],[331,655,279,253],[648,630,284,288],[959,636,275,280],
  [15,918,287,305],[329,943,284,278],[652,927,274,297],[953,933,286,292]];
seasonalFrames.forEach((frame,index)=>{
  const scale=Math.min(82/frame[2],80/frame[3]),w=frame[2]*scale,h=frame[3]*scale;
  SPRITES[SEASON_ART+'#'+index]={file:SEASON_ART,frame,size:[1254,1254],placement:[(120-w)/2,118-h,w,h]};
});
export const uiIcon=(id)=>atlas+'#'+id;
export const ASSET_FILES=[...cookwareAtlases,...Object.values(TITLE_ART),...KITCHEN_STAGES.map(stage=>stage.background),facility,straw,shavings,cotton,vessels,'/web/art/basket-v4.png',atlas,'/web/art/chick-v4-0.png','/web/art/chick-v4-3.png','/web/art/chick-v4-4.png','/web/art/egg-v4.png',dimSum];
