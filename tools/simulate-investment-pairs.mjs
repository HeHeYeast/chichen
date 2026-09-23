// Work L bounded investment comparisons. Laboratory funding is declared; this
// is not a simulation of earning the entry capital or of 14-day graduation.
import assert from 'node:assert/strict';
import {mkdir,writeFile,readFile} from 'node:fs/promises';
import {createHash} from 'node:crypto';
import {resolve} from 'node:path';
import {fileURLToPath} from 'node:url';
import * as E from '../web/engine.js';
import {RECIPE_CATALOG,recipePathInfo} from '../web/recipe-book.js';
import {ingredientUnlockInfo} from '../web/ingredient-unlocks.js';
import {businessCapacity} from '../web/business.js';
import {channelRandom} from '../web/rng.js';
import {syncProgress,skillPoints} from '../web/progression.js';
import {START,createRunner} from './simulate-economy.mjs';
import {finishTimely} from './simulate-economy-pairs.mjs';

export const INVESTMENT_BUDGET={minutes:360,maxBatches:30,seedCount:8};
const root=fileURLToPath(new URL('../',import.meta.url));
const costOf=ledger=>Object.values(ledger).reduce((n,x)=>n+x.spend,0);
const sum=o=>Object.values(o??{}).reduce((a,b)=>a+b,0);
export function fundedFixture(seed=1){
 const s=E.freshState(START,seed);s.cp=100000;s.total={'0:0':240};s.farm={};s.ingredients={};s.progress.tutorialSeen=true;syncProgress(s);
 return E.normalizeSave(s,START);
}
function buyFirstSix(r){for(let id=1;id<6;id++)r.run('prerequisite-tool',d=>E.buyTool(d,id),{id,level:0});}
export function prerequisites(kind,seed=1){
 const r=createRunner(fundedFixture(seed));
 if(kind!=='frying-purchase')buyFirstSix(r);
 if(['kitchen-2-3','frying-upgrade-1-2'].includes(kind))r.run('prerequisite-kitchen',d=>E.upgradeKitchen(d,r.now),{from:1,to:2});
 if(kind==='kitchen-2-3')for(let id=0;id<6;id++)r.run('prerequisite-tool',d=>E.buyTool(d,id),{id,level:1});
 return r;
}
export const INVESTMENT_CASES=[
 {id:'kitchen-1-2',label:'厨房 Lv1→Lv2',invest:(r)=>r.run('investment-kitchen',d=>E.upgradeKitchen(d,r.now)),control:{egg:0,toolId:1},treatment:{egg:0,toolId:1}},
 {id:'kitchen-2-3',label:'厨房 Lv2→Lv3',invest:(r)=>r.run('investment-kitchen',d=>E.upgradeKitchen(d,r.now)),control:{egg:0,toolId:1},treatment:{egg:0,toolId:1}},
 {id:'duck',label:'购买鸭蛋',invest:(r)=>r.run('investment-duck',d=>E.buyDuck(d)),control:{egg:0,toolId:1},treatment:{egg:1,toolId:1}},
 {id:'frying-upgrade-1-2',label:'平底锅 Lv1→Lv2',invest:(r)=>r.run('investment-tool',d=>E.buyTool(d,1),{id:1,level:1}),control:{egg:0,toolId:1},treatment:{egg:0,toolId:1}},
 {id:'frying-purchase',label:'首次购买平底锅',invest:(r)=>r.run('investment-tool',d=>E.buyTool(d,1),{id:1,level:0}),control:{egg:0,toolId:0},treatment:{egg:0,toolId:1}},
];
export function executableRecipes(s){
 // These are cookbook-defined combinations the controls/supply can execute,
 // not a claim that an unknown recipe is revealed in the player's UI.
 return [...new Set(RECIPE_CATALOG.filter(r=>r.toolId>=0&&r.kind!=='seasonal'&&recipePathInfo(s,r,START).conditions.every(c=>c.met)&&r.ingredients.every(id=>ingredientUnlockInfo(s,id).available)).map(r=>r.key))].sort();
}
function gates(s){return {kitchen:E.kitchenUpgradeInfo(s),duck:E.duckUnlockInfo(s),fryingCanBuy:E.canBuyTool(s,1),slots:Math.min(3,s.kitchenLevel+1),businessStockCapacity:businessCapacity(s),earnedSkillPoints:skillPoints(s).earned,executableRecipeKeys:executableRecipes(s),materials:E.availableIngredients(s)};}
export function benchmark(initial,recipe,{investment=null,seed=1,budget=INVESTMENT_BUDGET}={}){
 const r=createRunner(structuredClone(initial)),startCP=r.s.cp;const pre=gates(r.s);investment?.(r);const post=gates(r.s),investmentCost=startCP-r.s.cp;
 assert.ok(recipe.egg===0||r.s.duck,'the duck comparison must use a legally purchased duck egg');
 assert.ok(r.s.toolLevels[recipe.toolId]>=0,'the selected cookware must actually be owned');
 const productionStartCP=r.s.cp,endAt=START+budget.minutes*60000,batches=[];
 for(let index=0;index<budget.maxBatches;index++){
   // Setting the next egg/ingredients is the same reversible selection command
   // in both arms; paired tickets are independent of the extra investment cmd.
   r.run('prepare',d=>{d.egg=recipe.egg;d.selected=[];delete d.expansion.prepareMode;});
   const timing=E.cookInfo(r.s,recipe.toolId,r.now);if(r.now+timing.minutes*60000+3003>endAt)break;
   const before=r.s.cp;
   r.run('cook',d=>E.startBatch(d,recipe.toolId,r.now,channelRandom(d,`investment-${seed}-${index}`,'paired-production'),()=>.5),{index,egg:recipe.egg,toolId:recipe.toolId});
   const fireCP=before-r.s.cp,minutes=(r.s.batch.ends-r.s.batch.started)/60000;finishTimely(r);const harvestCP=r.s.cp-(before-fireCP),selection=Object.fromEntries(Object.entries(r.s.farm).filter(([,n])=>n>0)),beforeSale=r.s.cp;
   r.run('sell',d=>E.sell(d,selection,{overrideKeepOne:true,useRewards:false},r.now));
   batches.push({index,minutes,fireCP,harvestCP,saleCP:r.s.cp-beforeSale,netCP:r.s.cp-before,quantity:sum(selection),yieldByKey:selection});
 }
 assert.ok(r.now<=endAt);const finishedAt=r.now;r.advance(endAt);assert.ok(Object.values(r.s.farm).every(n=>n===0));
 const ledgerNet=Object.values(r.ledger).reduce((n,x)=>n+x.income-x.spend,0);assert.equal(r.s.cp-startCP,ledgerNet);
 return {investmentCost,pre,post,batches: batches.length,units:batches.length*24,productionMinutes:(finishedAt-START)/60000,batchMinutes:batches.map(x=>x.minutes),operatingCP:r.s.cp-productionStartCP,netIncludingInvestment:r.s.cp-startCP,finalCP:r.s.cp,ledger:r.ledger,rows:batches,trace:r.trace,failures:r.failures,commands:r.commands};
}
export function investmentPair(caseId,seed=1,budget=INVESTMENT_BUDGET){
 const def=INVESTMENT_CASES.find(x=>x.id===caseId);if(!def)throw Error('Unknown investment case');
 const base=prerequisites(caseId,seed),control=benchmark(base.s,def.control,{seed,budget}),treatment=benchmark(base.s,def.treatment,{investment:def.invest,seed,budget});
 assert.equal(control.investmentCost,0);assert.ok(treatment.investmentCost>0);assert.equal(treatment.pre.executableRecipeKeys.join(),control.pre.executableRecipeKeys.join());
 return {caseId,label:def.label,seed,budget,initial:{cp:base.s.cp,kitchenLevel:base.s.kitchenLevel+1,toolLevels:base.s.toolLevels.map(x=>x+1),duck:base.s.duck,prerequisiteCost:costOf(base.ledger),fundedCP:100000},prerequisiteTrace:base.trace,control,treatment,incrementalOperatingCP:treatment.operatingCP-control.operatingCP,incrementalNet:treatment.netIncludingInvestment-control.netIncludingInvestment,newExecutableRecipeKeys:treatment.post.executableRecipeKeys.filter(k=>!control.post.executableRecipeKeys.includes(k))};
}
export function gateEvidence(){
 const rows=[];
 function reject(id,initial,fn,pattern){const r=createRunner(initial),before=JSON.stringify(r.s);assert.throws(()=>r.run(id,fn),pattern);assert.equal(JSON.stringify(r.s),before);rows.push({id,blocked:true,reason:r.failures[0].message,unchanged:true});}
 reject('kitchen-1-2-missing-tools',fundedFixture(),d=>E.upgradeKitchen(d,START),/前六种/);
 const shortCP=prerequisites('kitchen-1-2').s;shortCP.cp=9999;reject('kitchen-1-2-insufficient-cp',shortCP,d=>E.upgradeKitchen(d,START),/CP不足/);
 const onlyFirstLevel=prerequisites('frying-upgrade-1-2').s;reject('kitchen-2-3-tools-still-lv1',onlyFirstLevel,d=>E.upgradeKitchen(d,START),/前六种/);
 const frying=prerequisites('kitchen-1-2').s;reject('frying-upgrade-needs-kitchen-lv2',frying,d=>E.buyTool(d,1),/购买条件/);
 const noDuck=fundedFixture();noDuck.cp=2499;reject('duck-requires-2500',noDuck,d=>E.buyDuck(d),/CP不足/);
 const exact=fundedFixture();exact.cp=2500;const exactDuck=createRunner(exact);exactDuck.run('buy-duck-exact',d=>E.buyDuck(d));assert.equal(exactDuck.s.cp,0);assert.equal(exactDuck.s.duck,true);rows.push({id:'duck-exact-2500',cost:2500,remainingCP:0,owned:true});
 return rows;
}
export async function main(){
 const output=resolve(root,'artifacts/sim/investments');await mkdir(output,{recursive:true});const gateChecks=gateEvidence(),results=[];
 for(const def of INVESTMENT_CASES)for(let seed=1;seed<=INVESTMENT_BUDGET.seedCount;seed++){
   const pair=investmentPair(def.id,seed),{prerequisiteTrace,...rest}=pair;
   await writeFile(resolve(output,`${def.id}-${seed}-prerequisites.jsonl`),prerequisiteTrace.map(x=>JSON.stringify(x)).join('\n')+'\n');
   for(const branch of ['control','treatment']){await writeFile(resolve(output,`${def.id}-${seed}-${branch}.jsonl`),pair[branch].trace.map(x=>JSON.stringify(x)).join('\n')+'\n');delete rest[branch].trace;}
   results.push(rest);
 }
 const report={checkedAt:new Date().toISOString(),passed:results.length===40&&results.every(r=>r.control.failures.length===0&&r.treatment.failures.length===0),method:{initialFunding:100000,recordedHarvest:{'0:0':240},startingFarm:{},skills:'none',budget:INVESTMENT_BUDGET,scope:'Laboratory-funded equal-start counterfactuals; each prerequisite is a real purchase, no runtime asset or price edits. Neither branch learns skills, opens business or receives orders. Both have 6h and at most 30 starts; actual batch count can differ due to time. This is not a 14-day progression or payback guarantee.'},gateChecks,results};
 await writeFile(resolve(root,'artifacts/sim/economy-investments.json'),JSON.stringify(report,null,2));await writeAppendix(report);console.log(JSON.stringify({passed:report.passed,pairs:results.length,branches:results.length*2,gateChecks:gateChecks.length,batches:results.reduce((n,r)=>n+r.control.batches+r.treatment.batches,0)},null,2));
}
async function writeAppendix(report){
 const range=xs=>`${Math.min(...xs)}～${Math.max(...xs)}`,mean=xs=>(xs.reduce((a,b)=>a+b,0)/xs.length).toFixed(2),table=[];
 for(const def of INVESTMENT_CASES){const rs=report.results.filter(r=>r.caseId===def.id),r=rs[0];table.push(`|${def.label}|${r.initial.prerequisiteCost}|${r.treatment.investmentCost}|${r.control.post.slots}→${r.treatment.post.slots}|${r.control.post.businessStockCapacity}→${r.treatment.post.businessStockCapacity}|${r.control.post.executableRecipeKeys.length}→${r.treatment.post.executableRecipeKeys.length}|${r.control.batches}→${r.treatment.batches}|${r.control.batchMinutes[0]}→${r.treatment.batchMinutes[0]}|${mean(rs.map(x=>x.control.operatingCP))} / ${mean(rs.map(x=>x.treatment.operatingCP))}|${mean(rs.map(x=>x.incrementalNet))}|`);}
 const hashes=[];for(const path of ['tools/simulate-investment-pairs.mjs','artifacts/sim/economy-investments.json'])hashes.push(`- ${path}: ${createHash('sha256').update(await readFile(resolve(root,path))).digest('hex')}`);
 const text=`# Work L 投资对照附录

时间：${report.checkedAt}。5类投资 × 8固定种子 × 两条分支 = 40对、80条真实命令路径，6项门槛核对。没有修改正式Web源码、价格或玩家存档。

这是有界的资金充足实验：每个独立起点由freshState建立，设置100000CP实验资金、已实收普通鸡240只、无现存伙伴/材料、无手艺。实验资金不表示玩家自然积累；它用于隔离一次投资的成本和能力差。之后所有前置厨具、厨房和鸭蛋均通过execute调用正式购买/升级函数，资金逐笔扣除。每对共享完全相同的购买前起点。

两分支均使用6小时前台时间和最多30批操作上限。厨具升级/厨房升级均做同种无调味平底锅鸡；鸭蛋分支改用新开放的无调味平底锅鸭；首次买平底锅对照原有保温灯鸡。全批干净及时收取、实际出售，同种子固定票据。不同厨具可能改变可完成批数，实际操作次数因此不同，但允许的时间和最多开火次数一致。未收录组合的“可执行”计数只表示明确厨具、厨房、供货条件成立，不把未知名称提前展示给玩家。

|投资|共享前置累计CP|本次一次成本CP|材料槽|营业备货上限|当时旧配方可执行种数|6h整批数|每批分钟|平均经营净CP：不投/投资|平均增量净CP（含一次成本）|
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|
${table.join('\n')}

## 厨房前置与合法成本

1. 厨房Lv1→Lv2：原有保温灯Lv1，依次真实购买平底锅500、水煮锅1000、油炸锅1500、烤箱2000、炖锅3000，前六件达到Lv1，合计8000CP；再付10000CP升级厨房。由最初实验状态累计18000CP。
2. 厨房Lv2→Lv3：前六件需全部Lv2，依次升级费用1000、1500、3000、4500、6000、9000，共25000CP；再付20000CP升级厨房。相对已有厨房Lv2追加45000CP，若从上面的最初状态起累计63000CP。这里不是把前置厨具费用藏进厨房标价。
3. 鸭蛋购买检查2500CP；2499CP拒绝且草稿回滚，恰好2500CP真实购买后剩0CP。此实验没有把2500CP视作每批成本。
4. 平底锅Lv1→Lv2要先拥有厨房Lv2，才可支付1500CP；厨房Lv1直接请求被拒绝，资产保持原值。

以下预期拒绝均逐字记录原因、确认整份状态未变：${report.gateChecks.filter(x=>x.blocked).map(x=>x.id+'（'+x.reason+'）').join('；')}。

## 如何理解收益

“经营净CP”=本时段实际收锅与出售收入－开火费用；本组不用材料、不营业、不做订单，不混入初始资金。含投资的增量另扣一次购买/升级费用。共享前置费用不再次计入两条分支的差额，已单独列出。

厨房升级增加材料槽、营业容量和2点可分配手艺（本组没有学习，未把点数折成CP）；可执行配方还同时受厨具和供货限制。本组Lv1→Lv2后首六厨具仍Lv1，所以当前可执行旧种数保持23，并不凭新增一槽发明配方；Lv2→Lv3在已有Lv2厨具下从34变35。对于保持原厨具、同一无材料料理的对照，调理时间不变，不能期待单靠升级按钮在同一时段直接返还10000/20000CP。后续组合、手艺与营业容量的价值不是本表同菜谱现金差能穷尽的。

代表厨具升级确实缩短一批所需时间。鸭蛋购买打开6种当时可执行的鸭配方，但这一个无调味平底锅鸭工作负载的平均经营净额比鸡锅低162.5CP，因此购买蛋种不是自动加收益的倍率；需要结合后续配方、菜单、采购和收集用途判断，不能把本例推广成“鸭蛋总是不值得”。

每项8个种子的“含成本增量”范围：${INVESTMENT_CASES.map(d=>d.label+' '+range(report.results.filter(r=>r.caseId===d.id).map(r=>r.incrementalNet))+'CP').join('；')}。这些是6小时、固定做法/操作上限的有限对照，不是投资回本保证，也不是14天完成全部厨房晋级的证明。14天主报告仍保留“该策略未升级厨房”的真实结果，不用本实验替换。

## 复现

- node tools/simulate-investment-pairs.mjs
- node --test --test-isolation=none tests/economy-investments.test.mjs
- 原始结果：artifacts/sim/economy-investments.json；前置及两条分支逐笔轨迹：artifacts/sim/investments/*.jsonl。

${hashes.join('\n')}
`;
 await mkdir(resolve(root,'artifacts/takeover'),{recursive:true});await writeFile(resolve(root,'artifacts/takeover/economy-investment-appendix.md'),text);
}
if(process.argv[1]&&resolve(process.argv[1])===fileURLToPath(import.meta.url))await main();
