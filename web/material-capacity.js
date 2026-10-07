export function materialCapacity(s){const n=Object.keys(s.expansion?.discovery?.identified??{}).length;return n>=4?42:n>=1?36:30;}
export const materialCount=s=>Object.values(s.ingredients??{}).reduce((a,b)=>a+b,0);
export const materialRoom=s=>Math.max(0,materialCapacity(s)-materialCount(s));
