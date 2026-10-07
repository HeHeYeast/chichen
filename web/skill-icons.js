// Painted skill art (GPT work set b4, cut by tools/slice-gpt-work.py): one picture per skill,
// one emblem per branch, and the skill-point coin.
import {SKILLS} from './skill-data.js';
const ART='/web/art/golden-ui/';
export const SKILL_ART=Object.freeze(Object.fromEntries(SKILLS.map(s=>[s.id,`${ART}skill-${s.id}.png`])));
export const BRANCH_ART=Object.freeze({CUL:`${ART}branch-cul.png`,HOME:`${ART}branch-home.png`,TRADE:`${ART}branch-trade.png`,OBS:`${ART}branch-obs.png`,TRIP:`${ART}branch-trip.png`});
export const SKILL_POINT_ART=`${ART}skill-point.png`;
export function skillIcon(id){
 const src=SKILL_ART[id];if(!src)return '';
 return `<img class="skill-icon" data-skill-icon="${id}" src="${src}" alt="" aria-hidden="true" draggable="false">`;
}
export const branchEmblem=branch=>BRANCH_ART[branch]?`<img class="branch-emblem" src="${BRANCH_ART[branch]}" alt="" aria-hidden="true" draggable="false">`:'';
