// Read-only aggregation of completed Work L experiments. Never tunes prices.
import {readFile,writeFile,mkdir} from 'node:fs/promises';
import {createHash} from 'node:crypto';
import {resolve} from 'node:path';
import {fileURLToPath} from 'node:url';
import {REGIONAL} from '../web/content-registry.js';
import * as E from '../web/engine.js';
const root=fileURLToPath(new URL('../',import.meta.url));
const load=async p=>JSON.parse(await readFile(resolve(root,p),'utf8'));
const simulation=await load('artifacts/sim/economy-14d.json'),pairs=await load('artifacts/sim/economy-pairs.json');
if(!simulation.passed||!pairs.passed||simulation.results.length!==64||!pairs.recipeWindows)throw Error('All 64 runs and the current paired evidence must pass before reporting');
const rs=simulation.results,range=values=>{const min=Math.min(...values),max=Math.max(...values);return min===max?String(min):`${min}～${max}`;};
const named={starter:'鸡蛋前期新档',collector:'60种中期收集', 'mature-low-stock':'193成熟低库存',hoarder:'193大量囤货'};
const cost=(r,types)=>types.reduce((n,k)=>n+(r.ledgerBeforeOffline[k]?.spend??0),0);
const invested=r=>cost(r,['tool-investment','duck-investment','kitchen-investment','project-payment']);
const adjusted14=r=>r.beforeOffline.cp-r.initial.cp+r.beforeOffline.assets.farm-r.initial.assets.farm+r.beforeOffline.assets.materials-r.initial.assets.materials;
const rows=[];for(const profile of Object.keys(named))for(const cadence of ['daily1','daily2','daily3','frequent']){const group=rs.filter(r=>r.profile===profile&&r.cadence===cadence);rows.push(`|${named[profile]}|${group[0].visitsPerDay}|${range(group.map(r=>r.beforeOffline.cp))}|${range(group.map(r=>r.beforeOffline.discoveries))}|${range(group.map(r=>r.beforeOffline.newSpecies))}|${range(group.map(r=>r.beforeOffline.cards))}|${range(group.map(r=>r.beforeOffline.projectStages))}|${range(group.map(r=>cost(r,['clean','repair'])))}|${range(group.map(invested))}|${range(group.map(r=>adjusted14(r)+invested(r)))}|`);}
const windows=[];for(const equipment of ['minimum','maximum'])for(const skill of ['none','HOME','HOME-calm']){const rr=pairs.recipeWindows.filter(r=>r.equipment===equipment&&r.skill===skill);windows.push(`|${equipment==='minimum'?'各配方最低厨具':'全部满级厨具'}|${skill}|${rr.filter(r=>r.foreground45).length}|${[120,480,600,720,1440].map(gap=>rr.filter(r=>r.gaps.find(g=>g.gap===gap).safe).length).join('|')}|`);}
const channels=pairs.channels.map(r=>`|${r.skillPolicy}|${r.instant.cp}|${r.business.cp}|${r.order.cp}|${r.initialCredits} / ${r.instant.creditsLeft} / ${r.business.creditsLeft} / ${r.order.creditsLeft}|`);
const thin=pairs.margins.filter(r=>r.regional&&r.skillPolicy==='none').sort((a,b)=>a.meanNet-b.meanNet).slice(0,8);
const branchEffects=['CUL','HOME','TRADE'].map(branch=>{const diffs=pairs.margins.filter(r=>r.regional&&r.skillPolicy===branch).map(r=>r.meanNet-pairs.margins.find(x=>x.recipeId===r.recipeId&&x.skillPolicy==='none').meanNet);return `|${branch}|${(diffs.reduce((a,b)=>a+b,0)/diffs.length).toFixed(2)}|${range(diffs)}|`;});
const losses=pairs.failures.filter(r=>r.firstFailureNet<0||r.fourBatchNet<0).map(r=>`|${r.recipeId}|${r.grossInputsFirst}|${r.firstFailureNet}|${r.fourBatchNet}|`);
const upkeep=rs.map(r=>cost(r,['clean','repair'])/Math.max(1,(r.ledgerBeforeOffline['legacy-cook']?.count??0)+(r.ledgerBeforeOffline['regional-cook']?.count??0)));
const sources=['tools/simulate-economy.mjs','tools/simulate-economy-pairs.mjs','tools/report-economy.mjs','tests/economy-simulation.test.mjs','web/content-registry.js','web/regional-content.generated.js','docs/content-pack/content.json','artifacts/sim/economy-14d.json','artifacts/sim/economy-pairs.json','artifacts/takeover/economy-tests.log'];
const hashes=[];for(const p of sources){try{hashes.push(`- ${p}: ${createHash('sha256').update(await readFile(resolve(root,p))).digest('hex')}`);}catch{}}
const report=`# Work L 经济证据复核

生成时间：${new Date().toISOString()}。64条轨迹：${simulation.checkedAt}；配对实验：${pairs.checkedAt}。

## 结论与边界

完成 4 初态 × 每日 1/2/3/8 次 × 不学手艺/料理/持家/经营 = 64 条 14 天轨迹，并各续走 72 小时离线。全部轨迹通过真实 execute 的存档校验、CP逐笔守恒、无负CP、零被吞掉的异常。另完成48配方四批保护成本、1664批正常边际收益、同货三渠道和逐配方首蛋保鲜窗口对照。本报告的“通过”指这些明确检查，不等于玩家节奏最优或无需人工游玩。

本次没有调整任何价格、旧资产或核心玩法。48款重复做法在8个配对种子下的无手艺**平均边际净额全部为正**，最薄 ${thin[0].meanNet} CP/批；但首次连败和维护摊销确有成本，不能据此声称每次尝试或每条14天路线都盈利。当前证据支持保留价格继续观察，未支持直接改价。

旧“22/48低频”结论撤回：它未按首枚最早破壳时间计算，也没有实际学习手艺。本次给出明确间隔与装备级别下的逐项窗口，不能把某个固定上线策略未完成的料理称为不可达。

## 实验策略与初态

- seed = 20260923；新档使用 freshState 的600CP。中期档有60种旧鸡、4000CP、厨房Lv2、常用厨具Lv1/2、尚无鸭蛋；193成熟低库存与大量囤货均有20000CP、全设备满级、鸭蛋开放，分别每种2只与食用种120只。累计实收不低于现存库存。
- 一次访问最多45分钟前台，最多追加1批前台可及时收取的料理，离开前可再开1批能承受计划间隔的料理。这是明确的操作时间假设，不代表所有低频玩家都停留45分钟。
- 选择已有完整地区方法的未收录目标；否则按作者给定旧材料组合寻找新发现。未向运行中的档案注入方法、材料、CP、发现卡或项目完成旗标。中期收集档同样优先地区方法、标本、见闻和未收录目标。
- 寻访检查全部地区、两地点与对应同行；轮换缺少的标本/见闻/事件，安排免费知识补全。未用付费观察分支缩短研究，所以不能把14天尚未学完全部做法解释成发行阻断。
- 用真实手艺指令学习所选分支；HOME在离开前启用已学会的安心等候，前台关闭。返料实际领取；采购按需求组分批；项目在条件成立时实际交货，再支付阶段费用。经营保留每种1只及最多6只食用备货缓冲，切换已开放菜单以取得不同接待记录。
- 每次访问先统一时间线再检查农场；维修完好度阈值65%，清扫在脏污后进行。每笔时间线收入、出售、订单、材料、开火、修缮、设备、鸭蛋、项目支付分别记账。72小时后也先检查农场再收锅，记录真实逃跑数量。
- 失败会带 action、message、code 和已执行轨迹报告并使运行失败；不把被拒绝操作当成功，也不忽略容量、供货或前置条件。

## 14天结果

以下范围来自该初态/频率的4个手艺组；以**第14天末、额外72h之前**为准。资产调整是现金变化 + 农场基础售价变化 + 材料未折扣基础购价变化；这是便于区分囤货变现的统一估值，并非可立即兑换的现金。不资本化新设备或在制批次。最后一列把可选设备/鸭蛋/厨房/项目CP支付加回，仍包含真实材料、开火、维护和交付物品的成本。

|初态|每日次数|期末CP|总发现|新品|卡片|项目阶段|清扫+修缮CP|可选投入CP|资产调整并加回可选投入CP|
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|
${rows.join('\n')}

14天维护按实际开火批次平均摊销的范围为 ${Math.min(...upkeep).toFixed(2)}～${Math.max(...upkeep).toFixed(2)} CP/批（一次开火未必在14天内收完；因此只是该访问策略的成本分摊，不是固定菜谱成本）。鸭蛋和各次厨具升级的实际支出都在 ledgerBeforeOffline 的独立条目，逐笔详情在 traces；不能只比较不同投入后的期末现金。本组策略14天内厨房升级发生 ${rs.reduce((n,r)=>n+(r.ledgerBeforeOffline['kitchen-investment']?.count??0),0)} 次：成熟档起点已满级，其余档未达到本策略的支付条件，所以这份14天结果没有证明跨全部厨房等级的晋级用时，也不能据此改写旧升级费用。

64次额外72h离线的现金变化为 ${range(rs.map(r=>r.offline.cpDelta))} CP，真实农场逃跑为 ${range(rs.map(r=>r.offline.farmLost))} 只。farmDelta包含营业扣货和收锅入库，单独的farmLost才是损耗，不能混用。

## 相同已生产24只的渠道比较

固定普通鸡24只，保持同一批实收库存/技能/额度；即时与营业均处理24只，O01处理12只后将余12只即时出售。营业结清24小时窗口。表内都是**销货收入**，不重复加收锅奖励。订单基础收入不叠招牌、主题或整筐奖励，部分交付不会提前发完成酬谢。

|手艺组|即时出售CP|MN1营业CP|O01 12只+余货12只CP|初始/即时后/营业后/订单后额度|
|---|---:|---:|---:|---|
${channels.join('\n')}

经营手艺下，订单组合不必高于即时整筐出售。营业也没有凭空增加第二批奖励；增加来自有上限的主题加价。

## 新材料与首次失败

8种地区材料基础购价：${REGIONAL.materials.map(m=>`${m.id}=${E.ingredient(m.id).buy_cp}CP`).join('，')}。首次无库存完整买料+开火投入范围 ${range(pairs.failures.map(r=>r.grossInputsFirst))} CP；四批累计完整投入 ${range(pairs.failures.map(r=>r.attempts.reduce((n,a)=>n+a.purchaseCP+a.fireCP,0)))} CP。每次只需先支付当批投入，收锅和伴随出售后可用回收款继续下一批；并非要求一次预付四批费用。

48款均以真实prepare/start/whole-harvest验证：只把地区25%判定票固定为0.99，普通伴随和孵化仍使用固定随机流，前三批无目标，第四批恰好实收1只目标，计数清零。首败净额范围 ${range(pairs.failures.map(r=>r.firstFailureNet))} CP，四批累计 ${range(pairs.failures.map(r=>r.fourBatchNet))} CP。正数表示伴随产物回收超过投入；负数为实际试做成本。该压力测试不是概率期望。

|曾出现首败或四批净亏的做法|首批完整投入CP|首败净CP|四批合计净CP|
|---|---:|---:|---:|
${losses.join('\n')}

这些数值已扣完整材料+开火、加实收CP和24只实际出售；没有另外补发试做奖励。四批净成本中最深 ${Math.min(...pairs.failures.map(r=>r.fourBatchNet))} CP，后续回收速度还取决于维护、订单/营业选择与投入。第一轮发现的低收益样本仍保留，没有用均值掩盖。

## 正常重复制作与手艺对照

48地区做法 + 4普通旧菜谱 × 4手艺 × 8种子，共1664批。相同 seed 和固定 runtime RNG ticket 避免学习产生额外commandSeq而改变伴随抽样；所有料理完整买料、干净开火、按实际最早过期时间安全收取并出售。记录实际折扣、拾金和返料。以下是无手艺组最薄的8款，**均未分摊设备、清扫、修缮、学习和寻访机会成本**：

|做法|8批平均边际净CP|最小|最大|
|---|---:|---:|---:|
${thin.map(r=>`|${r.recipeId}|${r.meanNet}|${r.minNet}|${r.maxNet}|`).join('\n')}

净额口径为实际净现金加退回材料的基础购价估值；退料已通过真实领取命令回到材料包。它不是把退料当作可直接出售的现金。

|分支相对不学手艺|48做法等权平均每批净额变化|各做法变化范围|
|---|---:|---:|
${branchEffects.join('\n')}

这里对每道菜使用同一批随机票据，HOME的正常干净及时收锅样本不凭空增加CP；其价值另由保鲜窗口与14天修缮/损耗记录体现。14天各分支会改变可开配方、供货和后续指令序号，行为与随机路径随后分歧，因此不能把它们的期末CP排序当作手艺强弱的因果结论。固定票据的单批实验才用于隔离边际影响；45分钟每次只追加1批的策略也没有榨取料理分支全部时间优势。

完整每批结果和各分支对照见 economy-pairs.json 的 margins。八种子是可重现实验样本，不是收益概率分布的精确期望或置信保证。尤其薄利菜可被一次清扫摊销抹去，因此不把“重复制作边际为正”写作“正式经营必定赚钱”。

## 首蛋保鲜与低频窗口（取代旧22/48结论）

最早破壳下界 = 调理时长 / 12。安全判定：整批在下次访问前结束，并且下次访问不晚于最早破壳 + 保鲜，另留3.6秒动画余量；保温灯不烧焦。以下是在明确装备/手艺条件下，48款中整批安全收取的数量；不表示届时玩家已有相应材料和完整方法。

|装备|手艺|45分钟前台|隔2h|隔8h|隔10h|隔12h|隔24h|
|---|---|---:|---:|---:|---:|---:|---:|
${windows.join('\n')}

安心等候的8小时窗口可覆盖全部48款；12/24小时单次静置不能覆盖所有非保温灯料理。满级无calm有25款能在45分钟前台收取，另8款保温灯可留到隔天，这与只看离线间隔得出的比例不同。旧厨具升级同时改变原时长和保鲜基准，所以“满级设备一定更适合长离线”也不成立。本次不修改旧计时合同，保留玩家追加收锅/延长前台时长的选择；没有据此提出删除玩法或修改旧资产。

## 复现与证据

- node tools/simulate-economy.mjs：64条全矩阵，artifacts/sim/economy-14d.json 与 traces/*.jsonl、最终存档。
- node tools/simulate-economy-pairs.mjs：48项保护成本、1664批正常对照、逐项保鲜窗口。
- node --test --test-isolation=none tests/economy-simulation.test.mjs：模拟器回归，包括首蛋反例、失败原子性、收入归因、项目实交、四批保护与额度。
- node tools/report-economy.mjs：仅汇总已经通过的证据，不改经济。

证据哈希：

${hashes.join('\n')}
`;
await mkdir(resolve(root,'artifacts/takeover'),{recursive:true});await writeFile(resolve(root,'artifacts/takeover/economy-audit.md'),report);console.log('Wrote artifacts/takeover/economy-audit.md: 64 trajectories, 48 trial recipes, 1664 margin batches; no prices changed.');
