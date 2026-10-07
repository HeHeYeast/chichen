// Save identity is egg kind + original internal ID; display numbers are separate.
export const speciesKey=(egg,id)=>`${egg}:${id}`;
export const speciesDiscovered=(state,egg,id)=>
  (state.total?.[speciesKey(egg,id)]??0)>0||(state.farm?.[speciesKey(egg,id)]??0)>0;
// The single definitions of "累计收取" and "发现品种" used by every unlock gate.
// A species counts as discovered once it was ever collected or is held on the farm.
export const collectedTotal=(state,egg)=>Object.entries(state.total??{}).reduce((n,[k,v])=>n+(egg===undefined||k.startsWith(egg+':')?v:0),0);
export const discoveryCount=state=>new Set([...Object.keys(state.total??{}),...Object.keys(state.farm??{})].filter(k=>(state.total?.[k]??0)>0||(state.farm?.[k]??0)>0)).size;
