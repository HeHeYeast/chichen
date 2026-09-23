import {DATA} from './data.js';

// Stable species IDs are shared by recipes and saves. Art can be replaced without
// renumbering species or modifying their gameplay values.
export const characterIndex=new Map(DATA.characters.flatMap((list,egg)=>list.map(c=>[`${egg}:${c.id}`,c])));
export const artworkOverrides={characters:{},tools:{}};
export const characterImage=(egg,id)=>artworkOverrides.characters[`${egg}:${id}`]??`/assets/png/Character/character_${egg}/character_${egg}_${id}_0_0.png`;
export const toolImage=(type,id,level=0)=>artworkOverrides.tools[`${type}:${id}:${level}`]??`/assets/png/Tool/Tool${type}/tool_${type}_${id}_${level}_0.png`;
export const speciesLabel=(egg,id)=>`${egg?'D':'C'}${String(id+1).padStart(2,'0')}`;
