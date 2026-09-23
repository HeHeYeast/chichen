// Append-only runtime projection. Author tools must import legacy-content.js.
export * from './legacy-content.js';
export {RUNTIME_GAME_DATA as GAME_DATA} from './content-registry.js';
import {RUNTIME_GAME_DATA} from './content-registry.js';
import {speciesDiscovered} from './species-state.js';
export function discoveredSpeciesCount(state){return RUNTIME_GAME_DATA.characters.reduce((n,rows,egg)=>n+rows.filter(c=>speciesDiscovered(state,egg,c.id)).length,0);}
