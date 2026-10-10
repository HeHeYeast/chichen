// Materials stack by kind; discovery no longer gates inventory space.
export function materialCapacity(){return Infinity;}
export const materialCount=s=>Object.values(s.ingredients??{}).reduce((a,b)=>a+b,0);
export const materialRoom=s=>Math.max(0,materialCapacity(s)-materialCount(s));
