export function materialCapacity(s){const n=Object.keys(s.expansion?.discovery?.identified??{}).length;return n>=4?42:n>=1?36:30;}
export const materialCount=s=>Object.values(s.ingredients??{}).reduce((a,b)=>a+b,0);
export const materialRoom=s=>Math.max(0,materialCapacity(s)-materialCount(s));
export function requireMaterialRoom(s,count){if(!Number.isSafeInteger(count)||count<0||count>materialRoom(s))throw Error(`材料包容量为${materialCapacity(s)}份，请先使用材料或保留在待领篮中。`);}
