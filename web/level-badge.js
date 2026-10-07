// 厨房等级头像: one picture per kitchen level, shared by the kitchen header (drawn on the canvas by kitchen-golden.js)
// and the farm HUD (DOM), with a blank plate at the bottom for 「Lv.N」. Built by tools/build-level-badges.py; until the
// per-level art arrives (GPT work batch b5, 小鸡换装) all four files are the same chick.
const ART='/web/art/golden-ui/';
// Measured on the picture, in its own pixels: the whole badge and the inner face of the plate where the text sits.
const PLACEHOLDER=Object.freeze({w:189,h:166,plate:{x:16,y:106,w:157,h:44}});
export const LEVEL_BADGES=Object.freeze([PLACEHOLDER,PLACEHOLDER,PLACEHOLDER,PLACEHOLDER]);
const index=level=>Math.min(3,Math.max(0,Number(level)|0));
export const levelBadge=level=>LEVEL_BADGES[index(level)];
export const levelBadgeSrc=level=>`${ART}avatar-lv${index(level)+1}.png`;
// DOM version: the picture keeps its own aspect; the text box is the measured plate, in percent.
export function levelBadgeMarkup(level){
  const b=levelBadge(level),p=b.plate,pc=(v,of)=>`${(v/of*100).toFixed(2)}%`;
  return `<span class="level-badge" style="aspect-ratio:${b.w}/${b.h}"><img src="${levelBadgeSrc(level)}" alt="" draggable="false"><b class="level-badge-text" data-safe style="left:${pc(p.x,b.w)};top:${pc(p.y,b.h)};width:${pc(p.w,b.w)};height:${pc(p.h,b.h)}">Lv.${index(level)+1}</b></span>`;
}
