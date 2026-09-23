import fs from 'node:fs';
import assert from 'node:assert/strict';
import {DATA} from '../../web/data.js';
import {GAME_DATA as D,EXPANSION} from '../../web/content-pack.js';
import {RECIPE_CATALOG as R} from '../../web/recipe-book.js';
import {SEASONAL_CHARACTERS as S} from '../../web/seasonal-pack.js';
import {ingredientUnlockInfo} from '../../web/ingredient-unlocks.js';
import {freshState,startBatch} from '../../web/engine.js';
const dir=new URL('./',import.meta.url),now=new Date('2026-09-19T11:00:00+08:00').getTime();
const src=fs.readFileSync(new URL('../../web/recipes.js',import.meta.url),'utf8');
const pool=new Function('DATA','samplePool',src.slice(src.indexOf('export function originalRecipes')).replace('export function','function')+';return originalRecipes;')(DATA,p=>p);
const price=(e,id)=>D.characters[e].find(c=>c.id===id).cp_1;
const charName=(e,id)=>D.characters[e].find(c=>c.id===id).title_zh_CN;
const ingredients=ids=>ids.length?ids.map(i=>D.tools[2][i].title_zh_CN).join('＋'):'不放材料';
const maxTool=(k,t)=>t<6?Math.min(k,2):t<8?(k===3?2:-1):k-1;
function stage(k){
 const s=freshState(now);s.kitchenLevel=k;s.toolLevels=D.tools[1].map(t=>maxTool(k,t.id));s.duck=true;
 // Discovery is an explicit prerequisite; these flags avoid mixing first-discovery surprises into repeatable recipes.
 for(const r of R)if(r.minKitchen<=k&&(r.toolId<0||r.minLevel<=s.toolLevels[r.toolId])&&r.ingredients.length<=Math.min(k+1,3))s.total[r.key]=1;
 return s;
}
function compute(k,e,t,lv,ids,forced){
 const s=stage(k);s.toolLevels[t]=lv;s.egg=e;
 let gross,mean;
 if(t===8){const m=EXPANSION.characters.filter(c=>c.minLevel<=lv&&c.ingredients.every(i=>ids.includes(i))),fixed=m.filter(c=>c.ingredients.length);gross=fixed.reduce((n,c)=>n+c.cp_1,0)+(24-fixed.length)*m.reduce((n,c)=>n+c.rate*c.cp_1,0)/m.reduce((n,c)=>n+c.rate,0);}
 else{const avg=r=>{const p=pool(s,e,t,ids,now,()=>r);assert(p.length>=24);return p.reduce((n,id)=>n+price(e,id),0)/p.length;};const chance=t===0&&lv===2?(e?1/8:.1):0;mean=(1-chance)*avg(.9)+chance*avg(.001);gross=24*mean;}
 if(forced!==undefined)gross+=price(e,forced)-mean;
 const tool=D.tools[1][t],minutes=tool[`lv_${lv}_min`],cost=tool[`lv_${lv}_cook_cp`],material=ids.reduce((n,i)=>n+D.tools[2][i].buy_cp,0),net=gross+24-cost-material;
 const gates=[e?'先购买鸭蛋（2500 CP）':null,t===8?'已发现12种；竹蒸笼达到所列等级':null,t===7?'厨房Lv.4且烧水壶Lv.3':null,...ids.map(i=>D.tools[2][i].title_zh_CN+'：'+ingredientUnlockInfo(stage(k),i).description),forced!==undefined?'已发现12种且已收取过目标品种，再从配方册定向':null].filter(Boolean).join('；')||'拥有所列厨具即可';
 return {k,e,t,lv,ids,forced,minutes,cost,material,gross,net,hour:net*60/minutes,gates};
}
const f=n=>n.toFixed(2),fmt=x=>`${x.e?'鸭蛋':'鸡蛋'} / ${D.tools[1][x.t].title_zh_CN} Lv.${x.lv+1} / ${ingredients(x.ids)}${x.forced!==undefined?' / 定向'+charName(x.e,x.forced):''}`;
const table=(heads,rows)=>['|'+heads.join('|')+'|','|'+heads.map(()=>'---').join('|')+'|',...rows.map(r=>'|'+r.map(v=>String(v).replaceAll('|','／')).join('|')+'|')].join('\n');
const tableRows=xs=>table(['排名','蛋种 / 厨具 / 调味料','分钟','卖出CP/批','开火CP','材料CP','净CP/批','净CP/小时','额外解锁条件'],xs.map((x,i)=>[i+1,fmt(x),x.minutes,f(x.gross),x.cost,x.material,f(x.net),f(x.hour),x.gates]));
const all=[],counts=[];
for(let k=0;k<4;k++){
 const rows=[],s=stage(k),potential=structuredClone(s);potential.total['0:0']=2000;potential.total['1:0']=2000;
 for(let e=0;e<2;e++)for(let t=0;t<9;t++){
  if(e&&t===8||s.toolLevels[t]<0)continue;
  const relevant=[...new Set(R.filter(r=>r.egg===e&&r.toolId===t).flatMap(r=>r.ingredients))].filter(i=>ingredientUnlockInfo(potential,i).available).sort((a,b)=>a-b);
  const combos=[[]];function more(start,a){if(a.length===Math.min(3,k+1))return;for(let i=start;i<relevant.length;i++){const b=[...a,relevant[i]];combos.push(b);more(i+1,b);}}more(0,[]);
  // A lower cookware level can coexist with upgraded peers. Equipment prerequisites are explicit in each row.
  for(let lv=0;lv<=s.toolLevels[t];lv++)for(const ids of combos){
   const atLevel={...potential,toolLevels:potential.toolLevels.map((v,i)=>i===t?lv:v)};
   if(!ids.every(i=>ingredientUnlockInfo(atLevel,i).available))continue;
   rows.push(compute(k,e,t,lv,ids));
   for(const c of S.filter(c=>k>=1&&c.egg===e&&c.toolId===t&&c.minLevel<=lv&&c.ingredients.length===ids.length&&c.ingredients.every(i=>ids.includes(i))))rows.push(compute(k,e,t,lv,ids,c.id));
  }
 }
 rows.sort((a,b)=>b.hour-a.hour);all.push(rows);counts.push(rows.length);
 for(const x of rows){assert(x.ids.length<=Math.min(3,k+1));assert(x.lv<=maxTool(k,x.t));assert(x.t<6||x.t===8||k===3);const atLevel={...potential,toolLevels:potential.toolLevels.map((v,i)=>i===x.t?x.lv:v)};assert(x.ids.every(i=>ingredientUnlockInfo(atLevel,i).available));assert(Number.isFinite(x.hour));assert(Math.abs(x.net-(x.gross+24-x.cost-x.material))<1e-8);}
 const base=rows.filter(x=>x.ids.length===0);
 const title=`厨房Lv.${k+1}-完整收益列表.md`;
 const text=`# 厨房 Lv.${k+1}：完整收益列表\n\n${k===0?'你当前所在阶段。每批最多放1种调味料，前六种厨具最高Lv.1；烧水壶、面包机、竹蒸笼均未开放。':'每批最多放'+Math.min(3,k+1)+'种调味料；只列这一厨房等级允许的厨具。'}\n\n本表共 **${rows.length} 条**，不截断为前十名。包含鸡蛋和鸭蛋、无材料方案、所有相关且在此阶段有解锁途径的可购买调味料组合，以及各可用厨具等级。不是每一条都已在你的存档解锁，请看最后一列。\n\n## 计算口径\n\n- 每批24只：净CP＝期望卖出CP＋24收取奖励－开火费－材料费；净CP/小时＝净CP×60÷标称分钟。\n- 为避免把满级进度套给新手，按当前厨房重新计算池子：**不加入神社活动品种、不加入累计1000/2000只才出现的品种**。材料的累计次数/发现条件单列，部分行仅是解锁后的预览。达到累计门槛后的保温灯收益会变化。\n- 假设已拥有行内厨具，所需材料已解锁并按商店价格补货；鸡鸭分开，鸭蛋仍需先花2500CP购买。若材料需要另一件厨具/更高厨具等级，须额外满足最后一列。\n- 只计稳定重复料理，首次发现四时品种的25%惊喜不并入基础收益；四时定向行明确要求已发现目标，并按每批替换1只计算。免费但限领的神社赠品、病变/烧焦及节日限定见原193种配方总表。\n- 厨房保持清洁并及时收取、全部卖出。半熟鸡需在自身破壳附近处理，晚于10秒会变水煮鸡；未计升级费、清扫维修、动画操作时间和挂机空置。\n- 不是“目标品种卖价×24”，而是按同批所有可能产出的权重求期望；负值方案也完整保留。某些单独无效的材料会增加成本，表中也保留以便比较。\n\n## 无材料方案（完整）\n\n${tableRows(base)}\n\n## 所有可购买材料组合与厨具等级：完整排名\n\n${tableRows(rows)}\n`;
 fs.writeFileSync(new URL(title,dir),text);
}
// Fresh low-level validation against the actual batch generator, checking material slots and cash deduction too.
let seed=197;const rng=()=>{seed=(Math.imul(seed,1664525)+1013904223)>>>0;return seed/2**32;};
const probes=[all[0][0],all[0].find(x=>x.e===1),all[0].find(x=>x.t===0&&x.ids.length===0),...all.slice(1).map(a=>a[0])],verified=[];
for(const x of probes){let sum=0,sq=0;const n=600;for(let i=0;i<n;i++){const s=stage(x.k);s.cp=1e6;s.toolLevels[x.t]=x.lv;s.egg=x.e;s.selected=[...x.ids];s.ingredients=Object.fromEntries(x.ids.map(id=>[id,1]));if(x.forced!==undefined)s.events.seasonalRecipe=`${x.e}:${x.forced}`;const b=startBatch(s,x.t,now,rng);assert.deepEqual(b.ingredients,x.ids);assert.equal(s.cp,1e6-x.cost);assert(x.ids.every(id=>s.ingredients[id]===0));const value=b.eggs.reduce((n,e)=>n+price(e.egg,e.id),0);sum+=value;sq+=value*value;}
const mean=sum/n,se=Math.sqrt(Math.max(0,sq/n-mean*mean)/n);assert(Math.abs(mean-x.gross)<6*se+.05,fmt(x));verified.push({recipe:fmt(x),kitchen:x.k+1,expected:x.gross,observed:mean,n});}
let index=`# 鸡宝鸭宝：按厨房等级查看完整收益\n\n你当前为 **厨房 Lv.1**，优先看第一份列表。旧表的满级排名不适合作为你现阶段的刷钱建议。\n\n`;
index+=table(['厨房等级','材料槽位','完整列表','方案数'],all.map((a,k)=>[k+1,Math.min(k+1,3),`[厨房Lv.${k+1}-完整收益列表](厨房Lv.${k+1}-完整收益列表.md)`,a.length]));
index+='\n\n## 你当前阶段：厨房 Lv.1 前十名\n\n'+tableRows(all[0].slice(0,10));
index+='\n\n## 厨房 Lv.1：不放调味料的全部方案\n\n'+tableRows(all[0].filter(x=>!x.ids.length));
index+='\n\n完整表保留负收益、鸡鸭两种蛋及所有阶段内的相关材料组合，标明额外解锁条件。193种品种的特殊获取、节日和神社赠品见[原始全配方资料](鸡宝鸭宝-配方与收益.md)。\n\n校验：四个等级全部通过材料槽位、厨具上限和材料解锁途径检查；6个代表方案共3600批实际孵化抽样与期望值一致（统计误差范围内）。\n';
fs.writeFileSync(new URL('按厨房等级-统计总览.md',dir),index);
fs.writeFileSync(new URL('stage-calculations.json',dir),JSON.stringify({counts,all,verified}));
const oldURL=new URL('鸡宝鸭宝-配方与收益.md',dir);let old=fs.readFileSync(oldURL,'utf8');if(!old.includes('按厨房等级-统计总览.md'))old=old.replace('\n\n','\n\n> **当前为厨房Lv.1，请优先查看[按厨房等级完整统计](按厨房等级-统计总览.md)。下文原有收益排名采用后期解锁条件。**\n\n');fs.writeFileSync(oldURL,old);
console.log(JSON.stringify({counts,total:counts.reduce((a,b)=>a+b,0),topLv1:all[0].slice(0,10).map(x=>({recipe:fmt(x),net:x.net,hour:x.hour})),verified},null,2));
