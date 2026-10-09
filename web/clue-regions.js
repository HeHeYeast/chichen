import {EXTRA_REGIONS} from './extra-regions.js';
// 主线索地区: the one region whose 寻访 reads the next clue about a partner not met yet, so the 线索册 and the map can say
// where to go instead of asking the player to remember it.
// The region follows the recipe's own materials first: where its characteristic seasoning comes home from (the second one,
// or a later one when the second comes from nowhere in particular), then its first seasoning; a recipe whose seasonings
// belong nowhere goes by its cookware. A short override list keeps partners whose riddle and story plainly belong somewhere
// else (the old 近郊 routes already named a few by hand).
// Static content: one answer per partner, the same for every save.
import {RECIPE_CATALOG} from './recipe-book.js';
import {RULES} from './integration-data.js';
import {REGIONAL} from './content-registry.js';
import {regionInfo,REGIONAL_RELEASE} from './region-model.js';
import {legacyRouteOpen} from './menu-model.js';

export const CLUE_REGIONS=Object.freeze(['V','R','T','B','O','H']);
export const ROUTE_REGION=Object.freeze({yard:'V',water:'R',wood:'T',bay:'B',orchard:'O',mushroom:'H'});
export const REGION_ROUTE=Object.freeze({V:'yard',R:'water',T:'wood',B:'bay',O:'orchard',H:'mushroom'});
export const REGION_SHORT=Object.freeze({V:'谷地',R:'溪岸',T:'茶坡',B:'风湾',O:'果园',H:'菌圃'});
// Cookware with no material to follow: 谷地 the stall, the griddle and the bread machine (its riddles all smell of wheat),
// 溪岸 the pots for water, 茶坡 the slow ovens and the steamer, 风湾 the fryer.
export const TOOL_REGION=Object.freeze({0:'V',1:'V',7:'V',2:'R',6:'R',4:'T',5:'T',8:'T',3:'B'});
// Shop seasonings no trip brings home but whose world is plainly one region: tea leaves and the tea flower grow on 茶坡.
const THEME_MATERIAL=Object.freeze({63:'T',64:'T',65:'T',67:'T'});
// Partners placed by their content, not their seasonings. Each says why; keep the list short.
export const CLUE_REGION_OVERRIDES=Object.freeze({
  // carried over from the old route extras (溪岸小径 / 林间茶坡 named these partners by hand)
  '1:0':{region:'R',why:'鸭宝：溪岸小径原本就把它列为水边线索'},
  '0:10':{region:'T',why:'茶叶蛋鸡：林间茶坡原本就把它列为茶坡线索（乌龙茶叶本身在溪岸带回）'},
  '1:41':{region:'T',why:'乌龙茶鸭：林间茶坡原本就把它列为茶坡线索（乌龙茶叶本身在溪岸带回）'},
  // holiday partners cooked with nothing but warmth: their riddle names the place
  '0:48':{region:'T',why:'樱花鸡：春风吹来的花瓣'},
  '0:60':{region:'T',why:'康乃馨鸡：送给妈妈的花'},
  '0:63':{region:'T',why:'牵牛花鸡：夏日清晨的小花'},
  '0:86':{region:'T',why:'梅花鸡：早早开放的小花'},
  '1:48':{region:'T',why:'枫叶鸭：秋风染红的叶子'},
  '0:61':{region:'R',why:'蜗牛鸡：滴滴答答的雨声'},
  '0:62':{region:'R',why:'晴天娃娃鸡：下了好久的雨'},
  '0:66':{region:'R',why:'喜鹊：搭一座桥'},
  '1:31':{region:'V',why:'南蛮鸭：荞麦面朴素的谷物气息'},
});
const BAY_POOL=[27,74,1];
const NO_RECIPE=new Set(['change','sign','gift']);
const KINDS=new Set(['pool','dim-sum','seasonal']);

// Where each seasoning comes home from: the old route pools (an earlier region wins when two share one), then the
// regional materials.
// (built on first use: this module can load inside import cycles before the content data is ready)
let materials=null;
function materialTable(){
  if(materials)return materials;
  const out={};
  for(const r of RULES.exploration.routes)for(const id of r.pool)out[id]??=ROUTE_REGION[r.id];
  for(const id of BAY_POOL)out[id]??='B';
  for(const m of REGIONAL.materials)out[m.id]??=m.region;
  for(const [id,region] of Object.entries(THEME_MATERIAL))out[id]??=region;
  return materials=Object.freeze(out);
}
export const materialRegion=id=>materialTable()[id]??null;

// The recipe a partner's clues are about when nothing else decides: its first ordinary path in the catalogue.
function canonicalPath(key){
  const paths=RECIPE_CATALOG.filter(r=>r.key===key&&!NO_RECIPE.has(r.kind));
  return paths.find(r=>KINDS.has(r.kind))??paths[0]??null;
}

let table=null;
function build(){
  const out=new Map(),MATERIAL_REGION=materialTable();
  for(const r of RECIPE_CATALOG){
    if(out.has(r.key)||NO_RECIPE.has(r.kind))continue;
    const path=canonicalPath(r.key);if(!path)continue;
    const [first,...rest]=path.ingredients,trait=rest.find(id=>MATERIAL_REGION[id]);
    const o=CLUE_REGION_OVERRIDES[r.key];
    const place=o?{region:o.region,by:'override',why:o.why}
      :trait!=null?{region:MATERIAL_REGION[trait],by:'second',material:trait}
      :first!=null&&MATERIAL_REGION[first]?{region:MATERIAL_REGION[first],by:'first',material:first}
      :{region:TOOL_REGION[path.toolId]??'V',by:'tool',toolId:path.toolId};
    out.set(r.key,Object.freeze({key:r.key,...place}));
  }
  // the regional partners (loop batch 4): their clues are where they come from
  for(const r of REGIONAL.recipes)if(r.mode==='regional-trial'&&!out.has(r.key)){const region=REGIONAL.species.find(c=>c.key===r.key)?.region;if(CLUE_REGIONS.includes(region))out.set(r.key,Object.freeze({key:r.key,region,by:'regional'}));}
  for(const area of EXTRA_REGIONS)for(const key of area.keys)out.set(key,Object.freeze({key,region:area.id,by:'override',why:area.hint}));
  return out;
}
// {key, region, by: 'override' | 'second' | 'first' | 'tool' | 'regional', material?, toolId?, why?} or null for a partner
// with no recipe.
export function clueRegionOf(key){table??=build();return table.get(key)??null;}
export function clueRegionTable(){table??=build();return [...table.values()];}
export const regionPartners=regionId=>clueRegionTable().filter(x=>x.region===regionId).map(x=>x.key);

// Whether a region can be visited: its own gate is met, or (before that) the old route behind it is open, which still
// brings materials and partner clues but no regional finds.
export function regionAccess(s,regionId){
  const area=regionInfo(s,regionId),routeOnly=!area.met&&REGIONAL_RELEASE.regions.includes(regionId)&&legacyRouteOpen(s,regionId);
  return {met:area.met,routeOnly,open:area.met||routeOnly,missing:area.missing,name:area.name};
}
