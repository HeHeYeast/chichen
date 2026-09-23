import {recipeDiscovered} from './recipe-book.js';

import {AUTHORED_CLUES} from './integration-data.js';
export const DISCOVERY_CLUES=Object.freeze(AUTHORED_CLUES);

// A read-only, deliberately small view: no hidden recipe or character payload.
export function discoveryClue(state,egg,id) {
  const key=`${egg}:${id}`;
  if(recipeDiscovered(state,egg,id)||!Object.hasOwn(DISCOVERY_CLUES,key))return null;
  return {key,text:DISCOVERY_CLUES[key]};
}
