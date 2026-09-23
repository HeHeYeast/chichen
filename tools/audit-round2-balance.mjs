// Fixed-seed paired scenario audit using the actual game start/update/collect/sell functions.
import * as E from '../web/engine.js';
import * as P from '../web/progression.js';
import {RECIPE_CATALOG} from '../web/recipe-book.js';
import {GAME_DATA} from '../web/content-pack.js';
import {TRADE_SPECIES} from '../web/trade-data.js';
import {writeFileSync,mkdirSync} from 'node:fs';
const NOW=new Date(2026,8,21,11).getTime(),N=64,rows=[];
function rng(seed){return()=>{seed=(Math.imul(seed,1664525)+1013904223)>>>0;return seed/4294967296;};}
const stages=[{name:'新档',d:0,h:0,k:0,lv:0},{name:'12种',d:12,h:500,k:0,lv:0},{name:'40种',d:40,h:2000,k:1,lv:1},{name:'80种',d:80,h:5000,k:2,lv:2},{name:'全收集',d:193,h:5000,k:3,lv:2}];
const builds={baseline:[],cook:['CUL-1','CUL-2','CUL-4'],trade:['TRADE-1','TRADE-2','TRADE-3'],care:['HOME-1','HOME-3','HOME-5'],cook_master:['CUL-1','CUL-2','CUL-3','CUL-4','CUL-S'],trade_master:['TRADE-1','TRADE-2','TRADE-3','TRADE-4','TRADE-S'],care_master:['HOME-2','HOME-3','HOME-4','HOME-5','HOME-S']};
for(const stage of stages)for(const egg of [0,1])for(let tool=0;tool<9;tool++){
 if((stage.d===0&&(egg||tool>0))||(tool>=6&&tool<8&&stage.k<3)||(tool===8&&(egg||stage.d<12)))continue;
 const proto=E.freshState(NOW);proto.cp=100000;proto.kitchenLevel=stage.k;proto.toolLevels.fill(stage.lv);proto.duck=egg===1;proto.egg=egg;proto.ingredients={};
 const all=GAME_DATA.characters.flatMap((cs,e)=>cs.map(c=>e+':'+c.id));for(const k of all.slice(0,stage.d))proto.total[k]=1;if(stage.h)proto.total['0:0']=Math.max(1,stage.h-stage.d+1);
 const available=E.availableIngredients(proto),paths=RECIPE_CATALOG.filter(r=>r.egg===egg&&r.toolId===tool&&r.minLevel<=stage.lv&&r.minKitchen<=stage.k&&r.kind==='pool'&&!r.time&&!r.campaign&&r.ingredients.length<=Math.min(3,stage.k+1)&&r.ingredients.every(id=>available.includes(id)));
 const costly=paths.sort((a,b)=>b.ingredients.reduce((n,id)=>n+E.ingredient(id).buy_cp,0)-a.ingredients.reduce((n,id)=>n+E.ingredient(id).buy_cp,0))[0]?.ingredients??[];
 for(const [recipe,ingredients]of [['无料',[]],['高成本',costly]])for(const visits of [1,3,24])for(const [build,ids]of Object.entries(builds)){
  if(build.endsWith('master')&&stage.d<80)continue;
  const base=structuredClone(proto);for(const id of ids){if(!P.skillGate(base,id)&&P.skillPoints(base).available>=P.SKILL_BY_ID[id].cost)P.learnSkill(base,id);}
  if(P.rank(base,'TRADE-2')){const counts={};for(const r of paths){const c=TRADE_SPECIES[r.key]?.category;if(c)counts[c]=(counts[c]??0)+1;}base.progress.trade.category=Object.keys(counts).sort((a,b)=>counts[b]-counts[a])[0]??'家常';}
  base.progress.protection.calm=build.startsWith('care');if(P.rank(base,'CUL-4'))base.progress.hotStove={signature:P.batchSignature(egg,tool,ingredients),at:NOW};
  const values=[],lost=[],novel=[],inv=[],duration=[];
  for(let i=0;i<N;i++){
   const s=structuredClone(base),random=rng(91731+i*313+tool*173+egg*13);s.ingredients=Object.fromEntries(ingredients.map(id=>[id,1]));s.selected=[...ingredients];const start=s.cp;E.startBatch(s,tool,NOW,random);const minutes=(s.batch.ends-NOW)/60000,returnAt=Math.max(s.batch.ends,NOW+24/visits*3600000)+1;E.updateBatch(s,returnAt,random);E.updateBatch(s,returnAt+2100,random);E.updateBatch(s,returnAt+3100,random);
   const old=new Set(Object.keys(s.total)),selection={};let changed=0;for(let j=0;j<24;j++){const e=s.batch.eggs[j],key=e.egg+':'+e.id;selection[key]=(selection[key]??0)+1;if([1,2,34,35,53,54,68,70,110].includes(e.id))changed++;E.collect(s,j,returnAt+3100);}
   E.sell(s,selection,{},returnAt+3100);
   const materialCost=ingredients.reduce((n,id)=>n+E.ingredient(id).buy_cp,0)*(1-P.effects(s).rebate/100),returnedValue=Object.entries(s.ingredients).reduce((n,[id,count])=>n+count*E.ingredient(+id).buy_cp,0);
   const batches=Math.min(visits,1440/minutes),cleanPerDay=(100+stage.k*50)*24/P.effects(s).cleanHours;
   values.push((s.cp-start-materialCost+returnedValue)*batches-cleanPerDay);lost.push(changed);novel.push(Object.keys(selection).filter(k=>!old.has(k)).length);inv.push(Object.values(s.ingredients).reduce((a,b)=>a+b,0));duration.push(minutes);
  }
  const mean=a=>a.reduce((a,b)=>a+b,0)/a.length,m=mean(values),sd=Math.sqrt(values.reduce((n,v)=>n+(v-m)**2,0)/(N-1));
  rows.push({stage:stage.name,egg,tool,recipe,ingredients,visits,build,skills:Object.keys(base.progress.skills),points:P.skillPoints(base).spent,cpPerDay:+m.toFixed(2),ci95:+(1.96*sd/Math.sqrt(N)).toFixed(2),upgrade10000Days:m>0?+(10000/m).toFixed(2):null,changedPerBatch:+mean(lost).toFixed(2),newKindsPerBatch:+mean(novel).toFixed(2),returnedInventory:+mean(inv).toFixed(3),minutes:mean(duration)});
 }
}
const comparisons=rows.filter(r=>r.build!=='baseline').map(r=>{const b=rows.find(b=>b.stage===r.stage&&b.egg===r.egg&&b.tool===r.tool&&b.recipe===r.recipe&&b.visits===r.visits&&b.build==='baseline');return {...r,baselineCP:b.cpPerDay,uplift:b.cpPerDay>0?(r.cpPerDay/b.cpPerDay-1)*100:null};});
const warnings=comparisons.filter(r=>r.uplift>25&&r.stage==='12种'&&r.points>=4).sort((a,b)=>b.uplift-a.uplift);
mkdirSync('artifacts/round2-implementation',{recursive:true});writeFileSync('artifacts/round2-implementation/balance.json',JSON.stringify({seed:91731,samplesPerScenario:N,scenarios:rows.length,batches:rows.length*N,method:'Paired fixed-seed independent batches using runtime rules. Daily rates assume stable recipes, maintenance amortization and full sales. Not a longitudinal progression simulation. Material returns valued at shop price; no expedition income.',rows,earlyWarnings:warnings},null,2));
console.log(JSON.stringify({scenarios:rows.length,batches:rows.length*N,earlyWarnings:warnings.length,largestEarly:warnings.slice(0,3)},null,2));
