// Save identity is egg kind + original internal ID; display numbers are separate.
export const speciesKey=(egg,id)=>`${egg}:${id}`;
export const speciesDiscovered=(state,egg,id)=>
  (state.total?.[speciesKey(egg,id)]??0)>0||(state.farm?.[speciesKey(egg,id)]??0)>0;
