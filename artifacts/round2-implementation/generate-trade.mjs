import {RECIPE_CATALOG as R,recipeId} from '../../web/recipe-book.js';
import {GAME_DATA as D} from '../../web/content-pack.js';
import {RULES} from '../../web/integration-data.js';
import {writeFileSync} from 'node:fs';
const seasonal={'0:120':'蒸点','0:121':'烘焙','1:57':'蒸点','1:58':'茶饮','0:122':'茶饮','0:123':'蒸点','1:59':'茶饮','1:60':'茶饮','0:124':'烘焙','0:125':'烘焙','1:61':'烘焙','1:62':'烘焙','0:126':'茶饮','0:127':'烘焙','1:63':'茶饮','1:64':'蒸点'};
const byTool={1:'煎炸',2:'炖煮',3:'煎炸',4:'烘焙',5:'炖煮',6:'茶饮',7:'烘焙',8:'蒸点'};
const rows=D.characters.flatMap((cs,egg)=>cs.map(c=>{
 const key=egg+':'+c.id,paths=R.filter(r=>r.key===key),r=paths.find(r=>['pool','dim-sum'].includes(r.kind)&&r.toolId>0&&!r.campaign&&!r.time),home=RULES.trade.eligibleSpecies.includes(key);
 const category=home?'家常':seasonal[key]??(r?byTool[r.toolId]:null);
 return {key,name:c.title_zh_CN,category,platter:!!category&&c.id>2,replicable:!!category&&!seasonal[key]&&paths.some(r=>r.kind==='pool'&&!r.campaign&&!r.time),basis:home?'家常白名单':seasonal[key]?'四时人工标签':r?recipeId(r):'特殊／观赏，不加价'};
}));
writeFileSync('web/trade-data.js','// Explicit audited classification for all 193 species; no runtime name inference.\nexport const TRADE_SPECIES = '+JSON.stringify(Object.fromEntries(rows.map(({key,category,platter,replicable})=>[key,{category,platter,replicable}])),null,2)+';\n');
writeFileSync('artifacts/round2-implementation/trade-audit.json',JSON.stringify(rows,null,2));
console.log(rows.length,'species classified');
