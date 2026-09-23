import fs from 'node:fs';
import assert from 'node:assert/strict';
import {DATA} from '../../web/data.js';
import {GAME_DATA as D,EXPANSION} from '../../web/content-pack.js';
import {RECIPE_CATALOG as R} from '../../web/recipe-book.js';
import {SEASONAL_CHARACTERS as S} from '../../web/seasonal-pack.js';
import {recipeStateAt,holidayForCharacter} from '../../web/holiday-calendar.js';
import {activityCatalog} from '../../web/legacy-activities.js';
import {ingredientUnlockInfo} from '../../web/ingredient-unlocks.js';
import {freshState,startBatch} from '../../web/engine.js';

const out=new URL('./',import.meta.url),now=new Date('2026-09-19T11:00:00+08:00').getTime();
const source=fs.readFileSync(new URL('../../web/recipes.js',import.meta.url),'utf8');
// Evaluate the real condition tree with a read-only pool capture instead of drawing 24 tickets.
const poolFn=new Function('DATA','samplePool',source.slice(source.indexOf('export function originalRecipes')).replace('export function','function')+';return originalRecipes;')(DATA,p=>p);
const chars=new Map(D.characters.flatMap((a,e)=>a.map(c=>[`${e}:${c.id}`,c])));
const name=(e,id)=>chars.get(`${e}:${id}`).title_zh_CN,price=(e,id)=>chars.get(`${e}:${id}`).cp_1;
const ing=ids=>ids.length?ids.map(i=>D.tools[2][i].title_zh_CN).join('＋'):'不放材料';
const matCost=ids=>ids.reduce((n,i)=>n+D.tools[2][i].buy_cp,0);
const state=freshState(now);state.kitchenLevel=3;state.duck=true;state.toolLevels.fill(2);state.cp=1e8;
for(const [k] of chars)state.total[k]=1;
state.total['0:0']=2000;state.total['1:0']=2000;
for(const a of activityCatalog)for(const f of a.flags)state.events[f]=true;
const current=recipeStateAt(state,now);
function average(e,t,ids,lv){
  const st={...current,toolLevels:current.toolLevels.map((v,i)=>i===t?lv:v)};
  const mean=r=>{const p=poolFn(st,e,t,ids.filter(i=>![68,69,70].includes(i)),now,()=>r);assert(p.length>=24);return p.reduce((n,id)=>n+price(e,id),0)/p.length;};
  if(t===8){const matches=EXPANSION.characters.filter(c=>c.minLevel<=lv&&c.ingredients.every(i=>ids.includes(i))),fixed=matches.filter(c=>c.ingredients.length);return {gross:fixed.reduce((n,c)=>n+c.cp_1,0)+(24-fixed.length)*matches.reduce((n,c)=>n+c.rate*c.cp_1,0)/matches.reduce((n,c)=>n+c.rate,0)};}
  const p=t===0&&lv===2?(e?1/8:1/10):0,m=p*mean(.001)+(1-p)*mean(.9);
  return {gross:24*m,mean:m};
}
function calc(e,t,ids,lv,forced){
  let {gross,mean}=average(e,t,ids,lv);
  if(forced!==undefined)gross-=mean-price(e,forced);
  const tool=D.tools[1][t],cost=tool[`lv_${lv}_cook_cp`],minutes=tool[`lv_${lv}_min`],materials=matCost(ids),net=gross+24-cost-materials;
  return {e,t,ids,lv,forced,gross,cost,materials,minutes,net,hour:net*60/minutes};
}
const ranked=[];let evaluated=0;
for(let e=0;e<2;e++)for(let t=0;t<9;t++){
  if(e&&t===8)continue;
  const relevant=[...new Set(R.filter(r=>r.egg===e&&r.toolId===t).flatMap(r=>r.ingredients))].filter(i=>![68,69,70].includes(i)).sort((a,b)=>a-b);
  const combos=[[]];function enumerate(start,arr){if(arr.length===3)return;for(let i=start;i<relevant.length;i++){const next=[...arr,relevant[i]];combos.push(next);enumerate(i+1,next);}}enumerate(0,[]);
  for(let lv=0;lv<3;lv++)for(const ids of combos){const x=calc(e,t,ids,lv);ranked.push(x);evaluated++;
    for(const c of S.filter(c=>c.egg===e&&c.toolId===t&&c.minLevel<=lv&&c.ingredients.length===ids.length&&c.ingredients.every(i=>ids.includes(i))))ranked.push(calc(e,t,ids,lv,c.id));
  }
}
ranked.sort((a,b)=>b.hour-a.hour);
const f=n=>n.toFixed(2),type=e=>e?'鸭宝':'鸡宝',recipe=x=>`${type(x.e)} · ${D.tools[1][x.t].title_zh_CN} Lv.${x.lv+1} · ${ing(x.ids)}${x.forced!==undefined?'（定向'+name(x.e,x.forced)+'）':''}`;
const table=(heads,rows)=>['|'+heads.join('|')+'|','|'+heads.map(()=>'---').join('|')+'|',...rows.map(row=>'|'+row.map(v=>String(v??'—').replaceAll('|','／').replaceAll('\n',' ')).join('|')+'|')].join('\n');
const rankTable=xs=>table(['排名','配方','耗时/分','售出收入/批','收取奖励','开火费','材料费','净收益/批','净CP/小时'],xs.map((x,i)=>[i+1,recipe(x),x.minutes,f(x.gross),24,x.cost,x.materials,f(x.net),f(x.hour)]));
let md=`# 鸡宝鸭宝：全配方与孵化收益\n\n整理日期：2026-09-19。依据本地当前 web 版本实际规则；共 ${chars.size} 种（鸡宝 128、鸭宝 65）。这是一份游戏之外的独立资料。\n\n## 计算口径\n\n- 每批 24 只；每收取一只另得 1 CP，卖出收入按各品种实际售价计算。农场留存本身不持续产币。\n- **每批净收益＝24 只的期望卖价总和＋24－开火费－本批材料购买价之和。每小时净收益＝每批净收益÷标称分钟数×60。** 每种所选材料每批消耗 1 份。\n- 排名按厨房 Lv.4、材料均已解锁、各品种已发现、累计鸡宝/鸭宝各达到 2000、神社委托均已完成计算；厨具等级逐行标注。不代表你的个人存档进度。\n- 节日过滤采用 2026年9月19日 11:00（北京时间）；只有当天开放的节日品种进入池。凤凰使用白天时段规则；夜间结果可能略有不同。\n- 原版按权重无放回抽取 24 个；期望值仍为 24×池内加权均价。凤凰和稀有鸭宝先随机入池，已分别按 10% / 12.5% 对两种池加权，未误当最终出货率。\n- 竹蒸笼先为每个命中材料配方安排 1 只，其余按权重抽取；四时定向配方只替换 1 只，其余 23 只沿用原结果。四时配方当前代码不限制季节，但需先发现才能定向。\n- 假定厨房清洁、持续及时收取、没有烧焦/病变/农场损失。尤其半熟鸡须在自身破壳时间附近处理，晚于 10 秒会变水煮鸡；挂机收益可能低于本表。\n- 统一用厨具标称整批耗时比较；随机提前收完可缩短实际周期，动画、操作和闲置则延长周期。未计一次性设备购买/升级、鸭蛋解锁 2500 CP、清扫维修以及一次性图鉴/章节/委托奖励。\n- 这是长期平均净收益，不保证每一批得到相同金额。不同材料组合可能命中多个配方，不能把目标品种售价直接乘 24。\n\n## 单位时间收益总排名（前 20）\n\n穷举每种蛋、每种厨具所有会影响产出的可购买材料的 0–3 种组合，并比较 Lv.1–3；共计算 ${evaluated} 个普通组合档位，另计四时定向。无效材料只增加成本，故不纳入。免费但每天限领的神社赠品不参加可持续排名。\n\n${rankTable(ranked.slice(0,20))}\n\n## 每种厨具的最优方案\n\n${rankTable(D.tools[1].map(t=>ranked.find(x=>x.t===t.id)).sort((a,b)=>b.hour-a.hour))}\n\n## 各厨具等级的最优方案\n\n这些是“该厨具维持此等级，其他条件已解锁”的比较，不是新手进度推荐。\n\n${rankTable([0,1,2].map(lv=>ranked.find(x=>x.lv===lv)))}\n\n## 全部厨具耗时与费用\n\n${table(['厨具','Lv.1 分钟','Lv.2 分钟','Lv.3 分钟','每批开火费','购买/两次升级CP'],D.tools[1].map(t=>[t.title_zh_CN,...[0,1,2].map(l=>t['lv_'+l+'_min']),t.lv_0_cook_cp,[0,1,2].map(l=>t['lv_'+l+'_buy_cp']).join(' / ')]))}\n\n烧水壶需厨房 Lv.4；面包机还需烧水壶 Lv.3。竹蒸笼 Lv.1/2/3 分别需厨房 Lv.2/3/4，首次还需发现 12 种。普通厨具升级需要相应厨房等级，前六种厨具按购买顺序开放。\n\n## 全品种配方与售价\n\n等级均按游戏显示从 Lv.1 起。下表的厨房等级包含厨具购买门槛与材料槽数；材料本身可能还有其他解锁条件，详见末尾材料表。“普通池”表示加入随机池，不保证出货。单只收益为售价＋1 CP收取奖励，尚未扣除批次成本。\n\n`;
const named=[];
for(let e=0;e<2;e++){
 const rows=R.filter(r=>r.egg===e).sort((a,b)=>a.id-b.id).map(r=>{
  const c=chars.get(r.key),t=D.tools[1][r.toolId],h=holidayForCharacter(e,r.id,now),a=activityCatalog.find(a=>a.flags.includes(r.campaign));
  const conditions=[r.campaign?`完成「${a?.title??r.campaign}」`:null,h?`${h.rule}；今日${h.active?'开放':'未开放'}`:null,r.collectionTotal?`累计${type(r.collectionTotal[0])}${r.collectionTotal[1]}只`:null,r.time?(r.time==='day-phoenix'?'10:00–12:59':'其余时段'):null,r.kind==='seasonal'?'发现12种；首次随机惊喜25%，发现后配方定向1只':null,r.kind==='dim-sum'?(r.ingredients.length?'每个匹配配方保底1只':'基础点心池'):null,r.note].filter(Boolean);
  const k=Math.max(r.minKitchen,r.minLevel,r.ingredients.length-1,r.toolId>=6&&r.toolId<=7?3:0,r.toolId===8?r.minLevel+1:0);
  if(t&& !['sign','gift'].includes(r.kind)&&(!h||h.active)&&(!r.time||r.time==='day-phoenix')){
   const x=calc(e,r.toolId,[...r.ingredients],2,r.kind==='seasonal'?r.id:undefined);named.push({...x,target:c.title_zh_CN});
  }
  return [r.id,c.title_zh_CN,c.cp_1,c.cp_1+1,t?`${t.title_zh_CN} Lv.${r.minLevel+1}；厨房Lv.${k+1}`:`特殊变化；厨房Lv.${r.minKitchen+1}`,ing(r.ingredients),matCost(r.ingredients),conditions.join('；')||'普通池'];
 });
 md+=`### ${type(e)}（${rows.length}种）\n\n`+table(['编号','品种','卖价CP/只','含收取CP/只','厨具/厨房最低条件','材料','材料CP/批','说明'],rows)+'\n\n';
}
named.sort((a,b)=>b.hour-a.hour);
md+='## 图鉴标准配方收益排名（满级，今日可用）\n\n每行只放该品种列明的材料；不同品种共用同一材料组合时会重复。数字是整批收益，并非仅目标品种贡献。特殊变化、限领赠品、今日未开放节日及夜凤凰不列入。\n\n'+table(['排名','目标品种','配方','分钟','期望售出CP/批','材料CP','开火CP','净CP/批','净CP/小时'],named.map((x,i)=>[i+1,x.target,recipe(x),x.minutes,f(x.gross),x.materials,x.cost,f(x.net),f(x.hour)]))+'\n\n';
md+='## 神社赠品：单批价值，不作可持续排名\n\n御神签、火苗、木绵各每天限领一次，且下次领取前还需再收取24只；同类未用完不可再次领取。赠品购买价为0，但不能无限刷。时空旅行每天一次，消耗一只普通鸡宝。\n\n';
const gifts=R.filter(r=>['sign','gift'].includes(r.kind)).map(r=>({...calc(r.egg,r.toolId,[...r.ingredients],2,r.id),target:name(r.egg,r.id)})).sort((a,b)=>b.hour-a.hour);
md+=table(['目标','材料','分钟','期望净CP/批','折算CP/小时（限领）'],gifts.map(x=>[x.target,ing(x.ids),x.minutes,f(x.net),f(x.hour)]))+'\n\n';
md+='## 材料价格与解锁\n\n'+table(['材料','CP/份','购买条件'],D.tools[2].map(i=>[i.title_zh_CN,i.buy_cp,ingredientUnlockInfo(state,i.id).description]))+'\n\n';
md+=`## 结论\n\n- 满足本表条件后，最高效率为 **${recipe(ranked[0])}**：每批净赚 **${f(ranked[0].net)} CP**，${ranked[0].minutes} 分钟一批，折算 **${f(ranked[0].hour)} CP/小时**。\n- 需要频繁上线的短周期厨具适合刷CP；长周期厨具适合减少操作次数，单批收入高不等于每小时收益高。\n- 稀有配方和四时定向主要有收集价值；材料成本会影响净收益，建议按本表整批净收益比较。\n\n## 数据依据\n\n读取 web/data.js、content-pack.js、recipes.js、engine.js、recipe-book.js、seasonal-pack.js、holiday-calendar.js、legacy-activities.js、ingredient-unlocks.js。未修改游戏规则或存档。分析脚本与数据快照保存在本资料同目录，便于后续重算。\n`;
assert.equal(R.length,chars.size);assert.equal(new Set(R.map(r=>r.key)).size,chars.size);
for(const x of ranked)assert(Number.isFinite(x.hour)&&Math.abs(x.net-(x.gross+24-x.cost-x.materials))<1e-9);
// Compare analytical expectations against the actual batch generator with deterministic randomness.
let seed=192026;const random=()=>{seed=(Math.imul(seed,1664525)+1013904223)>>>0;return seed/2**32;};
const probes=[...ranked.slice(0,3),ranked.find(x=>x.t===0&&x.e===0),ranked.find(x=>x.t===0&&x.e===1),ranked.find(x=>x.t===8),ranked.find(x=>x.forced!==undefined)];
const verification=[];
for(const x of probes){let sum=0,sq=0;const N=12000;
 for(let j=0;j<N;j++){const s={...state,egg:x.e,cp:1e8,toolLevels:state.toolLevels.map((v,i)=>i===x.t?x.lv:v),selected:x.ids,ingredients:Object.fromEntries(x.ids.map(i=>[i,1])),events:{...state.events,...(x.forced!==undefined?{seasonalRecipe:`${x.e}:${x.forced}`}:{})},batch:null};const b=startBatch(s,x.t,now,random);const v=b.eggs.reduce((n,c)=>n+price(c.egg,c.id),0);sum+=v;sq+=v*v;}
 const observed=sum/N,se=Math.sqrt(Math.max(0,sq/N-observed**2)/N),diff=Math.abs(observed-x.gross);assert(diff<=6*se+.03,recipe(x)+' expectation mismatch');verification.push({recipe:recipe(x),expected:x.gross,observed,standardError:se,batches:N});
}
fs.writeFileSync(new URL('鸡宝鸭宝-配方与收益.md',out),md);
fs.writeFileSync(new URL('calculation-data.json',out),JSON.stringify({date:'2026-09-19',count:chars.size,evaluated,top:ranked.slice(0,100),named,verification},null,2));
console.log(JSON.stringify({catalog:chars.size,evaluated,verifiedBatches:probes.length*12000,top:ranked.slice(0,10).map(x=>({recipe:recipe(x),net:x.net,hour:x.hour})),verification},null,2));
