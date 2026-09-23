// Independent bounded final-kitchen experiment; no runtime or existing test edits.
import assert from 'node:assert/strict';
import {readFile,writeFile,mkdir} from 'node:fs/promises';
import {createHash} from 'node:crypto';
import {resolve} from 'node:path';
import {fileURLToPath} from 'node:url';
import * as E from '../web/engine.js';
import {REGIONAL} from '../web/content-registry.js';
import {regionalRecipeInfo} from '../web/regional-methods.js';
import {regionInfo} from '../web/region-model.js';
import {businessCapacity} from '../web/business.js';
import {skillPoints} from '../web/progression.js';
import {START,createRunner} from './simulate-economy.mjs';
import {executableRecipes} from './simulate-investment-pairs.mjs';

const root=fileURLToPath(new URL('../',import.meta.url));
const output=resolve(root,'artifacts/sim/kitchen-lv4');
const FUNDING=250000;
const late=REGIONAL.recipes.filter(x=>x.kitchenLevel===3);
const clone=x=>structuredClone(x);
const hash=x=>createHash('sha256').update(x).digest('hex');
const spend=r=>Object.values(r.ledger).reduce((n,x)=>n+x.spend,0);
function toolsTo(r,level){for(let id=0;id<6;id++)while(r.s.toolLevels[id]<level)r.run('prerequisite-tool',d=>E.buyTool(d,id),{id,from:r.s.toolLevels[id]+1,to:r.s.toolLevels[id]+2});}
function kitchen(r){r.run('upgrade-kitchen',d=>E.upgradeKitchen(d,r.now),{from:r.s.kitchenLevel+1,to:r.s.kitchenLevel+2});}
function snapshot(r){const s=r.s;return {cp:s.cp,kitchenLevel:s.kitchenLevel+1,tools:s.toolLevels.map(x=>x+1),slots:E.kitchenUpgradeInfo(s).slots,businessCapacity:businessCapacity(s),earnedSkillPoints:skillPoints(s).earned,canBuyKettle:E.canBuyTool(s,6),canBuyBread:E.canBuyTool(s,7),oldExecutable:executableRecipes(s),firstSixMinutes:Array.from({length:6},(_,id)=>E.cookInfo(s,id,r.now).minutes),regions:Object.fromEntries(['V','R','T','B'].map(id=>[id,regionInfo(s,id)])),regional:late.map(recipe=>{const entry=regionalRecipeInfo(s,recipe.id,{entry:true}),full=regionalRecipeInfo(s,recipe.id);return {id:recipe.id,key:recipe.key,kitchenMet:s.kitchenLevel>=recipe.kitchenLevel,entryMet:entry.met,entryMissing:entry.missing,fullMet:full.met,fullMissing:full.missing};})};}
const rejected=[];
function reject(id,state,fn,pattern){const r=createRunner(clone(state)),before=JSON.stringify(r.s);assert.throws(()=>r.run(id,fn),pattern);assert.equal(JSON.stringify(r.s),before);rejected.push({id,reason:r.failures[0].message,atomic:true,stateSHA256:hash(before)});}
function verifyPair(before,after){assert.equal(before.kitchenLevel,3);assert.equal(after.kitchenLevel,4);assert.equal(before.cp-after.cp,30000);assert.equal(before.slots,3);assert.equal(after.slots,3);assert.equal(before.businessCapacity,72);assert.equal(after.businessCapacity,72);assert.equal(after.earnedSkillPoints-before.earnedSkillPoints,2);assert.deepEqual(after.firstSixMinutes,before.firstSixMinutes);assert.ok(before.oldExecutable.every(k=>after.oldExecutable.includes(k)));assert.equal(before.canBuyKettle,false);assert.equal(after.canBuyKettle,true);assert.equal(after.canBuyBread,false);assert.equal(before.regional.filter(x=>x.kitchenMet).length,0);assert.equal(after.regional.filter(x=>x.kitchenMet).length,9);}

await mkdir(output,{recursive:true});
// Only CP is declared laboratory funding. All other fresh-state progress is real.
const fresh=E.freshState(START,20260923);fresh.cp=FUNDING;
const r=createRunner(fresh),costStages=[];
for(let target=0;target<2;target++){const cp=r.s.cp;toolsTo(r,target);const toolCost=cp-r.s.cp;kitchen(r);costStages.push({targetKitchen:target+2,tools:toolCost,kitchen:(target+1)*10000,total:cp-r.s.cp});}
const missingTools=clone(r.s);
reject('lv4-insufficient-cookware',missingTools,d=>E.upgradeKitchen(d,START),/前六种/);
const toolsCP=r.s.cp;toolsTo(r,2);const finalToolCost=toolsCP-r.s.cp;assert.equal(finalToolCost,42000);
const ready=clone(r.s),short=clone(ready);short.cp=29999;
reject('lv4-insufficient-cp',short,d=>E.upgradeKitchen(d,START),/CP不足/);
reject('kettle-before-lv4',ready,d=>E.buyTool(d,6),/购买条件/);
const exact=clone(ready);exact.cp=30000;const exactRunner=createRunner(exact);kitchen(exactRunner);assert.equal(exactRunner.s.cp,0);assert.equal(exactRunner.s.kitchenLevel,3);
const before=snapshot(r);kitchen(r);const after=snapshot(r);verifyPair(before,after);
costStages.push({targetKitchen:4,tools:finalToolCost,kitchen:30000,total:finalToolCost+30000});
assert.equal(spend(r),135000);assert.equal(r.s.cp,FUNDING-135000);
reject('lv4-is-maximum',r.s,d=>E.upgradeKitchen(d,START),/最高等级/);
reject('bread-needs-kettle-lv3',r.s,d=>E.buyTool(d,7),/购买条件/);

// Progress is reused unchanged from a recorded real-command simulation. Funding
// is the only injected field; no cards, discoveries, methods or guides are set.
const source='artifacts/sim/traces/collector-frequent-none-final.json',sourceBytes=await readFile(resolve(root,source)),progress=JSON.parse(sourceBytes);
const sourceCP=progress.cp;progress.cp=FUNDING;const p=createRunner(progress);
assert.equal(p.s.kitchenLevel,1);
toolsTo(p,1);kitchen(p);toolsTo(p,2);
const progressionBefore=snapshot(p),progressReady=clone(p.s);kitchen(p);const progressionAfter=snapshot(p);verifyPair(progressionBefore,progressionAfter);
assert.equal(progressionBefore.regions.B.met,true);assert.equal(progressionAfter.regions.B.met,true);
assert.deepEqual(progressionBefore.regions.B.missing,progressionAfter.regions.B.missing);
// Late cookware remains a separate paid investment after the kitchen gate.
const followupCP=p.s.cp;while(p.s.toolLevels[6]<2)p.run('late-kettle',d=>E.buyTool(d,6),{to:p.s.toolLevels[6]+2});
while(p.s.toolLevels[7]<1)p.run('late-bread',d=>E.buyTool(d,7),{to:p.s.toolLevels[7]+2});
const followupCost=followupCP-p.s.cp,withLateTools=snapshot(p);assert.equal(followupCost,42500);
assert.equal(withLateTools.regional.filter(x=>x.entryMet).length,9);assert.equal(withLateTools.regions.B.met,true);
assert.ok(progressReady.expansion.regions.guideFlags.includes('GUIDE-B'));
assert.equal(progressReady.expansion.regulars.RG4.pendingStage.id,'RG4-1');
assert.equal(progressReady.expansion.regulars.RG4.pendingStage.branch,1);
for(const [name,runner] of [['fresh-prerequisites',r],['progression-prerequisites',p],['exact-cp',exactRunner]]){
 assert.equal(runner.failures.length,0);const net=Object.values(runner.ledger).reduce((n,x)=>n+x.income-x.spend,0);assert.equal(runner.s.cp-runner.initialCP,net);
 await writeFile(resolve(output,name+'.jsonl'),runner.trace.map(x=>JSON.stringify(x)).join('\n')+'\n');
}
// One-command paired branch records its common input for independent replay.
await writeFile(resolve(output,'pair-input.json'),JSON.stringify(progressReady,null,2));
const result={checkedAt:new Date().toISOString(),passed:true,scope:'Bounded funded eligibility/cost experiment, not natural progression timing or 14-day graduation evidence.',funding:FUNDING,costStages,cumulativeCost:spend(r),rejected,exactCP:{cost:30000,remaining:exactRunner.s.cp},fresh:{before,after},progression:{source,sourceSHA256:hash(sourceBytes),sourceCP,laboratoryCP:FUNDING,before:progressionBefore,after:progressionAfter,followupCost,withLateTools},commands:r.commands+p.commands+exactRunner.commands};
await writeFile(resolve(output,'result.json'),JSON.stringify(result,null,2));
const rows=progressionBefore.regional.map((b,i)=>{const a=progressionAfter.regional[i],l=withLateTools.regional[i];return `|${b.id}|${b.kitchenMet?'通过':'未达'}→${a.kitchenMet?'通过':'未达'}|${b.entryMet?'通过':b.entryMissing.join('；')}|${a.entryMet?'通过':a.entryMissing.join('；')}|${l.entryMet?'通过':l.entryMissing.join('；')}|${l.fullMet?'已齐备':l.fullMissing.join('；')}|`;});
const report=`# Work L 厨房 Lv3→Lv4 有界投资补验

${result.checkedAt}；全部断言通过；${result.commands} 条成功真实事务命令，${rejected.length} 项预期拒绝均整份状态回滚。正式 Web、现有测试与经济数值均未修改。未重跑 64 条长期模拟。

每个起点明确给定 250000 CP 实验资金。第一条从 freshState 起步，除资金外不注入实收、发现、厨具或知识；所有前置购买与厨房升级都经正式 execute 事务逐笔扣款。后期资格对照复用已有 collector-frequent-none 真实轨迹的期末存档，原 CP ${sourceCP}，仅资金设为 250000；该起点已有的发现、鸭蛋、方法和标本保持原值，不补造路标或知识。

## 法定前置及实际成本

|厨房升级|该阶段厨具投入 CP|厨房按钮 CP|阶段合计 CP|累计 CP|
|---|---:|---:|---:|---:|
|Lv1→Lv2|8000|10000|18000|18000|
|Lv2→Lv3|25000|20000|45000|63000|
|Lv3→Lv4|42000|30000|72000|135000|

最后一级要求前六件厨具全部 Lv3。由 Lv2 升到 Lv3 的六笔真实费用依次为 2000、2500、5000、7500、10000、15000 CP。若玩家已经拥有这六件 Lv3 厨具，升级厨房的一次直接费用是 30000 CP；若刚达到 Lv3 厨房最低配置，则还需 42000 CP 厨具投入，共 72000 CP。满额资金 30000 CP 的分支升级成功并恰好剩 0 CP。

## 同起点升级前后

材料槽 3→3，营业备货上限 72→72，新增可分配手艺 2 点（没有学习或折算为收益）。前六件同厨具的所有批次分钟数不变。新鲜实验起点旧配方可执行 ${before.oldExecutable.length}→${after.oldExecutable.length} 种；既有进度起点 ${progressionBefore.oldExecutable.length}→${progressionAfter.oldExecutable.length} 种；所有原可执行配方保留。这里“可执行”只查询厨具、厨房和供货条件，不是把未知名称显示给玩家，也不声称已完成实收。

Lv4 开放电热水壶购买；面包机仍需先把水壶升到 Lv3。后期供货追加实验实际购买水壶 Lv1→Lv3 共 22500 CP，以及面包机 Lv1→Lv2 共 20000 CP，合计 42500 CP。该费用没有隐含在厨房 30000 CP 标价中。

## 后期九份地区做法

同一真实进度起点，升级清除全部九份的“厨房 Lv4”门槛，但不会代替厨具、鸭蛋、旧材料供货、地区路线或地方知识。下表“入口”只检查设备与地区资格；最终一栏另列知识/卡片条件。

|做法|厨房条件|Lv3 入口|仅升 Lv4 入口|另购后期厨具后入口|另购后期厨具后完整条件|
|---|---|---|---|---|---|
${rows.join('\n')}

原期末存档已有采购与前三区事实，但厨房仅 Lv2；本实验真实完成 Lv2→Lv3 前置时，事务按已有事实追认 RG4-1 的手动采购替代分支，合法登记沿湾路标。因此最终 Lv3→Lv4 的同起点两分支海湾都已经开放，并非本次 Lv4 升级新发路线，也没有直接改写路标字段。后期厨具购齐后九份入口条件成立；仍缺知识的做法没有算作“立即可做”。

## 原子拒绝与复现

${rejected.map(x=>'- '+x.id+'：'+x.reason+'；序列化整份状态与执行前完全一致。').join('\n')}

运行 node tools/verify-kitchen-lv4-investment.mjs。原始结果及逐笔命令位于 artifacts/sim/kitchen-lv4/，pair-input.json 是已有进度对照的共同升级前输入。此报告不测赚到前置资金所需天数、不提供投资回本结论，也不是自然推进到 Lv4 或 14 天全晋级的证明。

- 工具 SHA256：${hash(await readFile(fileURLToPath(import.meta.url)))}
- 源期末存档 SHA256：${result.progression.sourceSHA256}
- 结果 SHA256：${hash(await readFile(resolve(output,'result.json')))}
`;
await writeFile(resolve(root,'artifacts/takeover/kitchen-lv4-investment.md'),report);
console.log(JSON.stringify({passed:true,commands:result.commands,atomicRejections:rejected.length,kitchenCost:30000,prerequisiteTools:finalToolCost,incrementalTotal:72000,cumulativeFreshCost:135000,lateToolsCost:followupCost,slots:[before.slots,after.slots],businessCapacity:[before.businessCapacity,after.businessCapacity],oldExecutable:[progressionBefore.oldExecutable.length,progressionAfter.oldExecutable.length],lateRegionalEntry:withLateTools.regional.filter(x=>x.entryMet).length,report:'artifacts/takeover/kitchen-lv4-investment.md'},null,2));
