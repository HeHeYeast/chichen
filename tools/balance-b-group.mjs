// Design-only model. Does not change runtime rules or read/write player saves.
import fs from 'node:fs';
import crypto from 'node:crypto';
import assert from 'node:assert/strict';
import {GAME_DATA as D} from '../web/content-pack.js';
import {RECIPE_CATALOG} from '../web/recipe-book.js';
import {freshState,startBatch,updateBatch,char} from '../web/engine.js';
import {HOLIDAYS} from '../web/holiday-calendar.js';
const out='docs/b-group-balance';fs.mkdirSync(out,{recursive:true});
const seed=20260920;
function rng(s=seed){return ()=>{s|=0;s=s+0x6D2B79F5|0;let t=Math.imul(s^s>>>15,1|s);t=t+Math.imul(t^t>>>7,61|t)^t;return ((t^t>>>14)>>>0)/4294967296;};}
const round=x=>Math.round(x*10000)/10000;
const sources=['web/engine.js','web/data.js','web/recipes.js','web/content-pack.js','web/seasonal-pack.js','web/ingredient-unlocks.js','web/legacy-activities.js','web/shrine.js','web/recipe-book.js','web/recipe-catalog-data.js','web/holiday-calendar.js','web/farm.js','web/collection-ui.js','web/save-store.js','docs/worldbuilding-and-expansion-brief.md'];
const hashes=Object.fromEntries(sources.map(p=>[p,crypto.createHash('sha256').update(fs.readFileSync(p)).digest('hex')]));
const tools=D.tools[1].map(t=>({id:t.id,name:t.title_zh_CN,minutes:[0,1,2].map(l=>t[`lv_${l}_min`]),buy:[0,1,2].map(l=>t[`lv_${l}_buy_cp`]),cook:[0,1,2].map(l=>t[`lv_${l}_cook_cp`])}));
const ingredients=D.tools[2].map(t=>({id:t.id,name:t.title_zh_CN,price:t.buy_cp}));
const baseline={seed,hashes,counts:D.characters.map(a=>a.length),tools,ingredients,holidaySpecies:HOLIDAYS.flatMap(h=>h.keys).length,upgradeCosts:[10000,20000,30000],allCookwareBuy:tools.reduce((s,t)=>s+t.buy.reduce((a,b)=>a+b,0),0)};
fs.writeFileSync(`${out}/baseline.json`,JSON.stringify(baseline,null,2)+'\n');

// Three budgets, one preferred environment. Rarity and sale price never enter.
// Ordered authored classification; JSON output is the proposed authoritative table.
const forest=new Set([3,8,13,20,29,33,35,47,63,64,65]);
const wet=new Set([11,16,27,48,59,74]);
const overrides={
 '0:0':[4,2,'yard','普通鸡宝翻找地面小物'], '1:0':[4,2,'water','普通鸭宝善于浅水寻找'],
 '0:10':[2,4,'wood','已确认的茶香辨识方向'],
 '0:1':[3,3,'yard','病变不另加探险惩罚'], '1:1':[3,3,'water','病变不另加探险惩罚'],
 '0:20':[3,3,'water','河水配方'], '1:14':[3,3,'water','河水配方'],
 '0:47':[4,2,'wood','菌种包与林地采集'],
 '0:17':[4,2,'wood','香草风味，擅长林地采集'], '0:23':[4,2,'wood','人参根茎风味，擅长翻找'],
 '0:48':[2,4,'wood','樱花形态与花香'], '0:60':[2,4,'wood','康乃馨形态与花香'],
 '0:61':[4,2,'water','蜗牛形态偏湿润环境'], '0:63':[2,4,'wood','牵牛花形态与花香'],
 '0:79':[4,2,'wood','栗子形态与林地果实'], '0:83':[3,3,'wood','树木形态'],
 '0:85':[3,3,'wood','松树形态'], '0:86':[2,4,'wood','梅花形态与花香'],
 '1:48':[3,3,'wood','枫叶形态'],
 '0:51':[2,4,'yard','时段感知；总能力不高于普通品种'], '0:52':[2,4,'yard','时段感知；总能力不高于普通品种'],
 '0:104':[2,4,'yard','辨认路径；不复制时空交换能力'],
 '0:114':[4,2,'yard','基础点心搬运采集'], '0:116':[4,2,'water','荷叶与糯米风味'],
 '1:57':[2,4,'wood','绿茶风味'], '1:62':[4,2,'wood','蘑菇风味'], '1:64':[3,3,'water','水煮糯米风味'],
};
const abilities=D.characters.flatMap((list,egg)=>list.map(c=>{
 const key=`${egg}:${c.id}`,r=RECIPE_CATALOG.find(r=>r.key===key),ids=r?.ingredients??[];
 let gather=3,discover=3,environment=egg?'water':'yard',reason=egg?'鸭类基础浅水偏好':'鸡类基础地面偏好';
 if(ids.some(i=>forest.has(i))){environment='wood';gather=2;discover=4;reason='实际配方含茶、花、香草或根茎，偏辨味';}
 else if(ids.some(i=>wet.has(i))){environment='water';gather=4;discover=2;reason='实际配方含水边或湿润风味，偏采集';}
 else if([3,4,5,7,8].includes(r?.toolId)){gather=4;discover=2;reason+='；油炸、烘焙、慢炖或点心形态偏采集';}
 else if(r?.kind==='change'||r?.time){gather=2;discover=4;reason+='；变化或时段线索偏发现';}
 if(egg===0&&c.id>=89&&c.id<=103){gather=3;discover=3;environment='yard';reason='全15签同能力，吉凶不形成探索强弱';}
 if(overrides[key])[gather,discover,environment,reason]=overrides[key];
 return {key,name:c.title_zh_CN,gather,discover,environment,reason};
}));
assert.equal(abilities.length,193);assert.equal(new Set(abilities.map(a=>a.key)).size,193);
assert(abilities.every(a=>a.gather+a.discover===6&&[2,3,4].includes(a.gather)));
fs.writeFileSync(`${out}/exploration-species.json`,JSON.stringify(abilities,null,2)+'\n');

function signDraw(found,last,misses,mode,random){
 let ids=Array.from({length:15},(_,i)=>i),missing=ids.filter(i=>!found.has(i));
 if(mode==='pity'&&missing.length&&misses>=6)ids=missing;
 if(mode==='pity'&&ids.length>1)ids=ids.filter(i=>i!==last);
 const unknownWeight=mode==='uniform'?1:mode==='pity'&&misses>=3?4:2;
 const weights=ids.map(i=>found.has(i)?1:unknownWeight),sum=weights.reduce((a,b)=>a+b,0);
 let pick=random()*sum;for(let j=0;j<ids.length;j++){pick-=weights[j];if(pick<0)return ids[j];}return ids.at(-1);
}
function stats(a){a.sort((x,y)=>x-y);return {mean:round(a.reduce((s,x)=>s+x,0)/a.length),p50:a[Math.floor(a.length*.5)],p90:a[Math.floor(a.length*.9)],p95:a[Math.floor(a.length*.95)],p99:a[Math.floor(a.length*.99)],observedMax:a.at(-1)};}
const signs=[],trials=100000;
for(const mode of ['uniform','weighted','pity'])for(const k of [0,5,10,14]){
 const random=rng(seed+k+mode.length*100),next=[],complete=[];
 for(let t=0;t<trials;t++){
  const found=new Set(Array.from({length:k},(_,i)=>i));let last=k?k-1:null,misses=0,n=0,first=0;
  while(found.size<15){let id=signDraw(found,last,misses,mode,random);n++;misses++;last=id;if(!found.has(id)){if(!first)first=n;found.add(id);misses=0;}}
  next.push(first);complete.push(n);
 }
 signs.push({mode,collected:k,initialLast:k?k-1:null,next:stats(next),remainingCompletion:stats(complete)});
}
// Exact survival for final missing sign: previous sign known; post-draw safe collection.
const finalSurvival=[];let survival=1,expected=0;
for(let n=1;n<=7;n++){expected+=survival;const p=n===7?1:(n<=3?2/15:4/17);survival*=1-p;finalSurvival.push({n,newProbability:p,cumulative:1-survival});}

const now=new Date(2026,8,20,14).getTime();
function economics({name,toolId,level,egg=0,selected=[],kitchen=0,total=24,knownSeasonal=null}){
 const random=rng(seed+toolId*100+level*10+egg),N=20000;let sales=0,delaySales=0,ready=0,earliest=Infinity;
 for(let i=0;i<N;i++){
  const s=freshState(now);s.cp=1e7;s.duck=true;s.egg=egg;s.kitchenLevel=kitchen;s.toolLevels=Array(9).fill(level);s.total={'0:0':total};
  s.selected=[...selected];s.ingredients=Object.fromEntries(selected.map(id=>[id,1]));
  if(knownSeasonal){s.total[knownSeasonal]=1;s.events.seasonalRecipe=knownSeasonal;}
  const batch=startBatch(s,toolId,now,random),T=D.tools[1][toolId][`lv_${level}_min`]*60000;
  earliest=Math.min(earliest,...batch.eggs.map(e=>(e.openAt-now)/60000));ready+=Math.max(...batch.eggs.map(e=>(e.openAt-now)/60000));
  const late=structuredClone(s);
  for(const x of [T+1,T+2002,T+3003])updateBatch(s,now+x,random);
  for(const x of [12*3600000+1,12*3600000+2002,12*3600000+3003])updateBatch(late,now+x,random);
  sales+=s.batch.eggs.reduce((sum,e)=>sum+char(e.egg,e.id).cp_1,0);
  delaySales+=late.batch.eggs.reduce((sum,e)=>sum+char(e.egg,e.id).cp_1,0);
 }
 const material=selected.reduce((s,id)=>s+D.tools[2][id].buy_cp,0),cook=D.tools[1][toolId][`lv_${level}_cook_cp`],gross=sales/N;
 return {name,toolId,level:level+1,minutes:D.tools[1][toolId][`lv_${level}_min`],earliest:round(earliest),meanAllOpen:round(ready/N),sale:round(gross),collectCP:24,cook,material,net:round(gross+24-cook-material),net12HourReturn:round(delaySales/N+24-cook-material),purchaseRebateMax:round(material*.1)};
}
const econ=[
 {name:'起步保温灯无料',toolId:0,level:0},
 {name:'起步保温灯食盐土',toolId:0,level:0,selected:[0]},
 {name:'起步平底锅无料',toolId:1,level:0},
 {name:'起步平底锅照烧酱',toolId:1,level:0,selected:[2]},
 {name:'水煮锅乌龙茶',toolId:2,level:0,selected:[3]},
 {name:'中期烤箱面粉',toolId:4,level:1,kitchen:1,total:1200,selected:[9]},
 {name:'中期蒸笼面粉糯米',toolId:8,level:0,kitchen:1,total:1200,selected:[9,16]},
 {name:'后期炖锅白酒配方',toolId:5,level:2,kitchen:3,total:6000,selected:[22,24,43]},
 {name:'后期鸭奶茶',toolId:6,level:2,kitchen:3,total:6000,egg:1,selected:[57,63]},
 {name:'后期三层蒸笼豆沙白糖面粉',toolId:8,level:2,kitchen:3,total:6000,selected:[71,37,9]},
].map(economics);
const time=tools.flatMap(t=>t.minutes.map((T,i)=>({tool:t.name,level:i+1,base:T,rank1:round(T*.96),rank3:round(T*.9),rank5:round(T*.8),specialist:round(Math.max(6,T*.75)),baseFresh:Math.max(2*T,120),maxFresh:Math.max(2*T,120)+90})));
const points=(d,k,h)=>2+Math.floor(d/5)+2*(k-1)+2*[120,500,2000,5000].filter(x=>h>=x).length;
const stages=[{name:'第一批',d:1,k:1,h:24},{name:'早期探索',d:8,k:1,h:120},{name:'前期',d:12,k:1,h:500},{name:'中期',d:40,k:2,h:2000},{name:'第一专精',d:80,k:3,h:5000},{name:'高收藏',d:120,k:4,h:5000},{name:'近全收集',d:160,k:4,h:5000},{name:'全收集',d:193,k:4,h:5000}].map(s=>({...s,points:points(s.d,s.k,s.h)}));
assert.equal(points(193,4,5000),54);
const routes=[{id:'yard',hours:2,base:1,pool:[0,1,2]},{id:'water',hours:6,base:2,pool:[11,3,27]},{id:'wood',hours:10,base:3,pool:[8,13,33,35]}];
function expedition(route,G,F,A,rank=0,clueRank=0,master=false,target=null){const p=Math.min(.6,.05+.025*G+.04*A+.02*rank+(master?.06:0)),q=Math.min(.55,.10+.02*F+.03*A+.03*clueRank),price=route.pool.reduce((s,id)=>s+D.tools[2][id].buy_cp,0)/route.pool.length;const directed=Math.min(route.base,master?2:1);const guaranteed=target===null?route.base*price:directed*D.tools[2][target].buy_cp+(route.base-directed)*price;return {route:route.id,G,F,A,rank,clueRank,master,base:route.base,p:round(p),q:round(q),meanItems:round(route.base+p),replacementCP:round(guaranteed+p*price),per24h:round((guaranteed+p*price)*24/route.hours)};}
const expeditions=routes.flatMap(r=>[
 {build:'单只普通匹配',...expedition(r,4,2,1)},
 {build:'三只均衡不匹配',...expedition(r,9,9,0)},
 {build:'三只采集匹配',...expedition(r,12,6,3)},
 {build:'三只发现匹配',...expedition(r,6,12,3)},
 {build:'采集满手艺专精',...expedition(r,12,6,3,3,2,true)},
 {build:'发现满手艺专精',...expedition(r,6,12,3,3,2,true)},
 {build:'采集专精选最贵基础材料',...expedition(r,12,6,3,3,2,true,r.pool.reduce((a,b)=>D.tools[2][a].buy_cp>D.tools[2][b].buy_cp?a:b))},
]);
// Current allocations deliberately allow only 2/4,3/3,4/2: theoretical 3-member maxima G/F=12.
const assertions={species:193,totalPoints:54,allNodesCost:80,baseNodesCost:60,masteryActiveMax:1,minCookMinutes:Math.min(...time.map(x=>x.specialist)),maxExpeditionBonus:.6,maxClueChance:.55,maxCleanHours:72};
assert.equal(assertions.minCookMinutes,6);assert(signs.filter(x=>x.mode==='pity').every(x=>x.next.observedMax<=7));
assert(expeditions.every(x=>x.p<=.6&&x.q<=.55));
const grouped=[...new Set(abilities.map(a=>`${a.gather},${a.discover},${a.environment}`))].map(k=>abilities.filter(a=>`${a.gather},${a.discover},${a.environment}`===k));
const attainable=routes.map(r=>{let bestG=null,bestF=null;for(let i=0;i<grouped.length;i++)for(let j=i;j<grouped.length;j++)for(let k=j;k<grouped.length;k++){
 const use=[i,j,k],used={},team=[];let valid=true;for(const n of use){const a=grouped[n][used[n]??0];used[n]=(used[n]??0)+1;if(!a){valid=false;break;}team.push(a);}if(!valid)continue;
 const G=team.reduce((s,a)=>s+a.gather,0),F=team.reduce((s,a)=>s+a.discover,0),A=team.filter(a=>a.environment===r.id).length;
 const e=expedition(r,G,F,A,3,2,true),row={...e,team:team.map(a=>a.key)};
 if(!bestG||e.p>bestG.p)bestG=row;if(!bestF||e.q>bestF.q)bestF=row;
 }return {route:r.id,bestMaterial:bestG,bestClue:bestF};});
assert(attainable.every(a=>a.bestMaterial.p===.59&&a.bestClue.q===.49));
let minJ=0;for(let k=1;k<24;k++)minJ+=(k/24)**24;const batchTimeFactor=1-11/276*minJ;
const milk=econ.find(e=>e.name==='后期鸭奶茶');
const throughput=[0,.2,.25].map(reduction=>{const cycleMinutes=8*(1-reduction)*batchTimeFactor+.05;return {reduction,cycleMinutes,expectedCyclesPer24h:1440/cycleMinutes,netCPWithoutBusiness:1440/cycleMinutes*milk.net,netCPWithRebate:1440/cycleMinutes*(milk.net+milk.material*.1)};});
const inventoryCases=[[1,1],[3,1],[3,0],[1,0],[0,0]].map(([total,reserved])=>({total,reserved,home:total-reserved,sellAll:total-reserved,keepOne:Math.max(0,total-reserved-1)}));
assert(inventoryCases.every(c=>c.sellAll+c.reserved===c.total));
const report={seed,trials,hashes,assumptions:{signs:'每签均安全收录；已有收藏集合0..k-1，上次签为k-1；misses初始0；不将未收取等同收录',economics:'每情景20000批，真实引擎，正常收取为标称结束后完成动画；材料按商店原价；无委托和节日额外收入',exploration:'材料替代购买价值，非直接CP；所有列出材料已解锁；只计算一队无缝出发上界'},signs,finalSignExact:{expected,attempts:finalSurvival},econ,time,stages,expeditions,attainable,batchTimeFactor,throughput,inventoryCases,assertions};
fs.writeFileSync(`${out}/simulation.json`,JSON.stringify(report,null,2)+'\n');
const table=(head,rows)=>'| '+head.join(' | ')+' |\n| '+head.map(()=>'---').join(' | ')+' |\n'+rows.map(r=>'| '+r.join(' | ')+' |').join('\n')+'\n';
const reportMd=`# B组数值复算结果\n\n固定随机种子 ${seed}；每种签方案／收藏起点 ${trials.toLocaleString()} 次完整收集过程；每种经济情景20,000批。输入摘要见 baseline.json，正式解释与最终规则见上级设计文档。\n\n## 求签\n\n${table(['方案','已收录','下一新品均值','下一新品P95','集齐剩余均值','集齐P95','集齐P99'],signs.map(s=>[s.mode,s.collected,s.next.mean,s.next.p95,s.remainingCompletion.mean,s.remainingCompletion.p95,s.remainingCompletion.p99]))}\n14/15推荐方案精确期望：${round(expected)}次；第3/6/7次累计获得新目标概率：${[2,5,6].map(i=>round(finalSurvival[i].cumulative*100)+'%').join(' / ')}。\n\n## 真实厨房经济\n\n${table(['情景','原分钟','售价均值','收取CP','开火费','材料费','净收益','12h后返回净收益','满经营采购返利'],econ.map(s=>[s.name,s.minutes,s.sale,24,s.cook,s.material,s.net,s.net12HourReturn,s.purchaseRebateMax]))}\n\n## 时间（分钟）\n\n${table(['厨具','等级','原时长','4%','10%','20%','25%专精','基础保鲜','最高保鲜'],time.map(s=>[s.tool,s.level,s.base,s.rank1,s.rank3,s.rank5,s.specialist,s.baseFresh,s.maxFresh]))}\n\n## 点数\n\n${table(['阶段','发现D','厨房显示等级','累计H','可得点'],stages.map(s=>[s.name,s.d,s.k,s.h,s.points]))}\n\n## 探索（材料全部已开放）\n\n${table(['路线','队伍','额外材料概率','线索概率','期望材料数','替代购买CP','24h连续派队上界'],expeditions.map(s=>[s.route,s.build,s.p,s.q,s.meanItems,s.replacementCP,s.per24h]))}\n\n## 品种分配\n\n${table(['环境','4采集/2发现','3/3','2/4'],['yard','water','wood'].map(e=>[e,...[4,3,2].map(g=>abilities.filter(a=>a.environment===e&&a.gather===g).length)]))}\n\n数值断言：${JSON.stringify(assertions)}。\n`;
fs.writeFileSync(`${out}/results.md`,reportMd);
fs.appendFileSync(`${out}/results.md`,'\n## 合法队伍极限\n\n'+table(['路线','材料最高概率','实现队伍','线索最高概率','实现队伍'],attainable.map(a=>[a.route,a.bestMaterial.p,a.bestMaterial.team.join(' + '),a.bestClue.q,a.bestClue.team.join(' + ')]))+'\n## 全天即时收取压力测试：三级鸭奶茶\n\n包含每批3秒收尾；无操作耗时，无病变焦化，无清洁费用。是理想连续周转估算，不是正常日收益。返利与原材料成本抵扣同时计入净值，不额外重复加探索价值。\n\n'+table(['减时','整批平均分钟','24h期望批数','不含经营净CP','含10%采购返利净CP'],throughput.map(t=>[t.reduction,...[t.cycleMinutes,t.expectedCyclesPer24h,t.netCPWithoutBusiness,t.netCPWithRebate].map(round)])));
fs.writeFileSync(`${out}/exploration-species.md`,'# 193种品种的探索能力定表（设计建议）\n\n这些是本轮新增玩法参数，不声称是原游戏属性。英文环境代号 yard＝庭院、water＝浅水、wood＝林地。每种总能力6，15种签鸡完全同值。以同目录JSON为开发输入，发布前与正式文案逐项对照。\n\n'+table(['稳定身份','名称','采集','发现','环境','分配依据'],abilities.map(a=>[a.key,a.name,a.gather,a.discover,a.environment,a.reason])));
console.log(JSON.stringify({status:'PASS',...assertions,signs:signs.filter(s=>s.collected===14),baselineCookwareCP:baseline.allCookwareBuy,output:out},null,2));
