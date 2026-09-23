import {advanceTimeline} from './timeline.js';
export {farmHP} from './farm-clock.js';
// Both the online tick and resume enter this same chronological interpreter.
export const advanceWorld=advanceTimeline;
export function checkFarmLoss(s,now=Date.now(),random=null){return advanceTimeline(s,now,random,{checkFarm:true}).lost;}
