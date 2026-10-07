// All ownership is derived from unique activity records; farm remains T.
// A team may bring several of one kind, so away partners are counted per place in the team.
const awayCount=(t,key)=>t?.status==='running'?t.members.filter(m=>m===key).length+(t.cargo&&!t.cargo.processed?(t.cargo.selection?.[key]??0):0):0;
export function inventoryView(s,key){
  const T=s.farm?.[key]??0,t=s.progress?.trip;
  const R=awayCount(t,key);
  const S=s.expansion?.business?.active?.stock?.[key]??0;
  const Q=(s.expansion?.orders?.active??[]).reduce((n,o)=>n+(o.reserved?.[key]??0),0);
  if(![T,R,S,Q].every(n=>Number.isSafeInteger(n)&&n>=0)||T>99999||T<R+S+Q)throw Error('伙伴数量记录不一致，请重新读取进度。');
  return {T,R,S,Q,free:T-R-S-Q,home:T-R-S};
}
export const reservedCount=(s,key)=>inventoryView(s,key).R;
export const freeCount=(s,key)=>inventoryView(s,key).free;
export const homeCount=(s,key)=>inventoryView(s,key).home;
export const availableCount=freeCount;
// Locks: how many of a kind must stay at home when selling, stocking the shop or delivering. Every kind keeps one by
// default (the old 「每种留 1 只」); the player can lock more, or none. Teams going out may use locked partners.
export const lockedCount=(s,key)=>{const p=s.expansion?.inventoryPolicy,n=p?.locks?.[key];return Number.isSafeInteger(n)&&n>=0?n:(p?.keepOne===false?0:1);};
export const spareCount=(s,key)=>{const v=inventoryView(s,key);return Math.max(0,Math.min(v.free,v.home-lockedCount(s,key)));};
export function usableByOwner(s,key,owner){
  const id=String(owner).replace(/^order:/,'');
  const order=s.expansion?.orders?.active?.find(o=>o.id===id);
  return freeCount(s,key)+(order?.reserved?.[key]??0);
}
export function validateConsumption(s,selection,{keepOne=false}={}){
  for(const [key,n]of Object.entries(selection))if(!/^[01]:(0|[1-9]\d*)$/.test(key)||!Number.isSafeInteger(n)||n<0||n>freeCount(s,key)||(n>0&&keepOne&&homeCount(s,key)-n<lockedCount(s,key)))throw Error('在家数量已变化，或要留在家里的数量已锁定；请重新选择。外出、营业或已预留的伙伴不能重复交付或出售');
}
export function shrinkOrderReservations(s){
  const budget={};
  for(const o of s.expansion?.orders?.active??[])for(const [key,n]of Object.entries(o.reserved??{})){
    const t=s.progress?.trip,R=awayCount(t,key);
    budget[key]??=Math.max(0,(s.farm[key]??0)-R-(s.expansion?.business?.active?.stock?.[key]??0));
    const kept=Math.min(n,budget[key]);if(kept)o.reserved[key]=kept;else delete o.reserved[key];budget[key]-=kept;
    if(kept<n)o.needsRestock=true;
  }
}
