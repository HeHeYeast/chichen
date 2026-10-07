import {GAME_DATA as DATA,EXPANSION} from './content-pack.js';
import {SEASONAL_CHARACTERS} from './seasonal-pack.js';
import {speciesKey} from './species-state.js';
import {regionalSpeciesArt,materialArt} from './visual-assets.js';
import {productionCharacter} from './production-art.js';

// Stable species IDs are shared by recipes and saves. Art can be replaced without
// renumbering species or modifying their gameplay values.
export const characterIndex=new Map(DATA.characters.flatMap((list,egg)=>list.map(c=>[speciesKey(egg,c.id),c])));
export const artworkOverrides={characters:{},tools:{}};
for(const id of [0,3,4])artworkOverrides.characters[`0:${id}`]=`/web/art/chick-v4-${id}.png`;
for(const character of EXPANSION.characters)artworkOverrides.characters[`0:${character.id}`]=character.artwork;
for(const character of SEASONAL_CHARACTERS)artworkOverrides.characters[character.key]=character.artwork;
// Every original cookware tier shares the same redrawn style and keeps its ID.
for(let id=0;id<8;id++)for(let level=0;level<3;level++)artworkOverrides.tools[`1:${id}:${level}`]=`/web/art/cookware-${id<4?'a':'b'}-v15.png#${id%4*3+level}`;
for(const level of EXPANSION.levels)artworkOverrides.tools[`1:8:${level.level}`]=`${EXPANSION.art}#tool${level.level}`;
for(const rows of DATA.characters)for(const c of rows)if(c.pack==='regional')artworkOverrides.characters[`${c.egg}:${c.id}`]=c.artwork==='/web/art/regional-concept.svg'?regionalSpeciesArt(c.authorId):c.artwork;
for(let id=75;id<83;id++)artworkOverrides.tools[`2:${id}:0`]=materialArt(id);
export const characterImage=(egg,id)=>productionCharacter(egg,id)??artworkOverrides.characters[`${egg}:${id}`]??`/assets/png/Character/character_${egg}/character_${egg}_${id}_0_0.png`;
export const toolImage=(type,id,level=0)=>artworkOverrides.tools[`${type}:${id}:${level}`]??`/assets/png/Tool/Tool${type}/tool_${type}_${id}_${level}_0.png`;
export const speciesLabel=(egg,id)=>`${egg?'D':'C'}${String(id+1).padStart(3,'0')}`;
