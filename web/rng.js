// Stable, domain-separated economic tickets. Rendering must use another source.
export const RNG_ALGORITHM='fnv1a-mulberry32-v1';
export function canonical(value){
  if(Array.isArray(value))return '['+value.map(canonical).join(',')+']';
  if(value&&typeof value==='object')return '{'+Object.keys(value).sort().map(k=>JSON.stringify(k)+':'+canonical(value[k])).join(',')+'}';
  return JSON.stringify(value);
}
export function hash32(value){let h=2166136261;for(const b of new TextEncoder().encode(String(value))){h^=b;h=Math.imul(h,16777619);}return h>>>0;}
export function payloadHash(value){return hash32(canonical(value)).toString(16).padStart(8,'0');}
export function unit(seed,ticketId,channel,index=0){
  if(!Number.isInteger(seed)||seed<0||seed>0xffffffff||!Number.isSafeInteger(index)||index<0)throw Error('随机票据无效');
  let t=(hash32(canonical([seed,ticketId,channel,index]))+0x6D2B79F5)|0;
  t=Math.imul(t^(t>>>15),t|1);t^=t+Math.imul(t^(t>>>7),t|61);
  return ((t^(t>>>14))>>>0)/4294967296;
}
export function channelRandom(state,ticketId,channel){let index=0;return ()=>unit(state.meta.rng.seed,ticketId,channel,index++);}

export function economicRandom(state,channel){return state.meta?channelRandom(state,`cmd-${state.meta.commandSeq+1}`,channel):Math.random;}
