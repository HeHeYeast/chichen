import {LEGACY193,EXPANSION} from './legacy-content.js';
import {SEASONAL_CHARACTERS} from './seasonal-pack.js';
import {REGIONAL_CONTENT,LEGACY_SEMANTICS} from './regional-content.generated.js';
import {REGIONAL_TEXT} from './regional-text.generated.js';
import {RUNTIME_REQUIREMENTS} from './runtime-requirements.generated.js';
import {RUNTIME_ASSETS} from './runtime-assets.generated.js';
import {ABILITIES,DESCRIPTIONS,AUTHORED_CLUES} from './integration-data.js';
import {TRADE_SPECIES} from './trade-data.js';
import {ORIGINAL_RECIPE_CATALOG} from './recipe-catalog-data.js';

const deepFreeze=v=>{if(v&&typeof v==='object'&&!Object.isFrozen(v)){Object.values(v).forEach(deepFreeze);Object.freeze(v);}return v;};
export const REGIONAL=deepFreeze(REGIONAL_CONTENT);
export const CONTENT_TEXT=deepFreeze(REGIONAL_TEXT);
export const REQUIREMENTS=deepFreeze(RUNTIME_REQUIREMENTS);
const newSpecies=REGIONAL.species.map(def=>({...CONTENT_TEXT[def.id],...def,id:def.numericId,authorId:def.id,title_zh_CN:CONTENT_TEXT[def.id].name,cp_0:def.collectCP,cp_1:def.baseSaleCP,pack:'regional',artwork:RUNTIME_ASSETS[def.assetId]?.variants.full?.available?RUNTIME_ASSETS[def.assetId].variants.full.path:'/web/art/regional-concept.svg'}));
const oldSpecies=LEGACY193.characters.flatMap((list,egg)=>list.map(c=>({...c,egg,key:`${egg}:${c.id}`,...LEGACY_SEMANTICS[`${egg}:${c.id}`]})));
export const speciesByKey=deepFreeze(Object.fromEntries([...oldSpecies,...newSpecies].map(s=>[s.key,s])));
export const materialById=deepFreeze(Object.fromEntries([...LEGACY193.tools[2],...REGIONAL.materials.map(m=>({...m,...CONTENT_TEXT[m.id],title_zh_CN:CONTENT_TEXT[m.id]?.name??CONTENT_TEXT[m.stableId]?.name??String(m.id),buy_cp:m.priceCP,pack:'regional'}))].map(m=>[m.id,m])));
const legacyRecipes=[...ORIGINAL_RECIPE_CATALOG,...EXPANSION.characters.map(c=>({egg:0,id:c.id,toolId:8,minLevel:c.minLevel,minKitchen:Math.max(0,c.ingredients.length-1),ingredients:[...c.ingredients],kind:'dim-sum',description:c.description})),...SEASONAL_CHARACTERS.map(c=>({egg:c.egg,id:c.id,toolId:c.toolId,minLevel:c.minLevel,minKitchen:1,ingredients:[...c.ingredients],kind:'seasonal',description:c.description}))].map(r=>({...r,key:`${r.egg}:${r.id}`}));
const legacyRecipeId=r=>[r.key,r.kind,r.toolId,r.minLevel,r.ingredients.join('-'),r.campaign??'',r.time??''].join('/');
export const recipeById=deepFreeze(Object.fromEntries([...legacyRecipes.map(r=>[legacyRecipeId(r),r]),...REGIONAL.recipes.map(r=>[r.id,r]),...REGIONAL.alternatives.map(r=>[r.id,r])]));
export const resolveSpecies=key=>speciesByKey[key]??null;
export const resolveMaterial=id=>materialById[id]??null;
export const resolveRecipeId=id=>recipeById[id]??null;
export const allowedSets=deepFreeze(REGIONAL.allowedSets);
// 采集/发现 run 1–20 for each partner (a typical one about 9). The chance formulas were tuned for a typical 3, so every
// point of G or F counts for one third of what it did on the old 2–4 scale.
export const ABILITY_SCALE=3;
export const SPECIES_ABILITIES=deepFreeze({...ABILITIES,...Object.fromEntries(REGIONAL.species.map(s=>[s.key,{gather:s.exploration.G,discover:s.exploration.F,environment:s.exploration.environment}]))});
export const SPECIES_DESCRIPTIONS=deepFreeze({...DESCRIPTIONS,...Object.fromEntries(REGIONAL.species.map(s=>[s.key,CONTENT_TEXT[s.id].description]))});
export const SPECIES_CLUES=deepFreeze({...AUTHORED_CLUES,...Object.fromEntries(REGIONAL.species.map(s=>[s.key,CONTENT_TEXT[s.id].clue]))});
const categoryNames={home:'家常',fry:'煎炸',stew:'炖煮',steam:'蒸点',bake:'烘焙',tea:'茶饮'};
export const SPECIES_TRADE=deepFreeze({...TRADE_SPECIES,...Object.fromEntries(REGIONAL.species.map(s=>[s.key,{category:categoryNames[s.signature]??null,platter:s.edible,replicable:false}]))});
export const RUNTIME_GAME_DATA=deepFreeze({...LEGACY193,characters:[0,1].map(egg=>Object.freeze([...LEGACY193.characters[egg],...newSpecies.filter(c=>c.egg===egg)])),tools:LEGACY193.tools.map((rows,i)=>i===2?Object.freeze(Object.values(materialById)):rows)});
