# 文档索引与维护规则

整理日期：2026-09-22。面向后续GPT Work／开发者：先读项目README与当前计划，确定实际基线，再按任务读下表。文档表达设计意图，代码证明实现；二者冲突须显式登记，不静默以代码覆盖设计。

## 核心文档的唯一职责

| 文档 | 负责什么／新内容写在哪里 |
|---|---|
| [项目README](../README.md) | 项目定位、运行入口、文档入口；不堆版本日志 |
| [游戏设计](game-design.md) | 核心循环、现行玩法与数值、规则边界、平衡目标 |
| [世界与内容](worldbuilding-kitchen-story.md) | 正式世界、三章正文、作者规范、留白／否决项 |
| [UI／美术](ui-ux.md) | 页面与交互、最新美术方向、制作和验收标准 |
| [工程与维护](engineering.md) | 架构、内容生成、存档、测试、构建、签名、备份更新 |
| [当前计划](plan.md) | 唯一当前状态、未完成任务、证据边界和待确认决定 |

## 按需附件

| 文件 | 用途与维护边界 |
|---|---|
| [工程接入与迁移设计](engineering-integration-design.md)、[后续实施计划](engineering-implementation-plan.md) | 193→241的代码考古、runtime内容、逐版本存档、事实/事务/RNG/离线/UI与测试合同；12个可玩Work及回滚边界。工程设计已完成，功能尚未实现；负责人文档为工程与维护，落地合同迁回该文档，执行状态仍在当前计划 |
| [整体UI信息架构](ui-information-architecture.md)、[视觉展板与原型](ui-architecture/index.html) | 下一阶段五主导航、全功能归属、核心流程、尺寸与状态契约；负责人文档为UI／美术。尚未接入，实施后将已落地规则迁回UI规范，原型保留为设计依据，不作为运行或真机证据 |
| [下一阶段玩法与内容扩展](gameplay-expansion-design.md) | 经营／挂机／收集的下一阶段推荐设计，尚未实现；游戏设计负责后续承接。包含库存营业、四地48种鸡鸭及分期验收；落地规则迁回对应核心文档后收束，不成为第二套现行规则 |
| [四地区完整内容资产](content-expansion-assets.md) | 内容阶段交付：48鸡鸭、8材料、24发现卡、8菜单/主题、12采购、16常客段、4项目、4地区册与美术brief；附JSON/CSV及关系审计。承接玩法与UI输入，尚未接入游戏；后续按此实现，现行193项源表不提前重生成 |
| [品种描述](species-descriptions.md)、[线索与获取依据](species-discovery-audit.md) | 193项内容源表，由生成器读取；不在其他文档再复制正文 |
| [素材来源清单](game-asset-inventory.md) | 原始资源统计、历史依据；现用绑定看[资源索引](../web/art/README.md)，提示词／裁切看[来源记录](../web/art/provenance.md) |
| [iOS专项方案](ios-port-plan.md) | 尚未实施的个人移植设计；不是当前工程事实，平台要求实施前复核 |
| [授权准备](permission-request/README.md) | 联系来源、未发信件草稿和附件；不是已获许可或本次联系指令 |
| [B组历史模型](b-group-gameplay-design.md)、[结果](b-group-balance/results.md)、[能力可读表](b-group-balance/exploration-species.md) | 历史推导与机器校验配套；能力／基础配置JSON仍为输入，旧手艺数值不代表1.5。详见工程文档 |
| [旧主交接入口](worldbuilding-and-expansion-brief.md) | 仅为历史工具固定路径保留的导航，不维护需求／TODO副本 |
| [版本证据索引](archive/release-history.md) | 已完成版本的继承关系与原有产物位置，替代十余份QA叙述 |
| [妙奇星球研究](archive/mqxq-research/妙奇星球玩法调研.md) | 有来源分级、截图、独有评分矩阵的冻结研究；不是本项目需求 |

JSON／CSV、截图、mockup是配套数据与证据，不按文档数量机械删除。二轮mockup保留原路径，UI文档引用它们；原始资源、旧APK、真实存档、源码备份和第三方许可不在本次清理范围。

## 长期维护规则

1. 先改负责该主题的核心文档。事实、已确认设计、候选、历史证据、待验结果显式区分；文件名默认不带轮次、日期或“最终版”。
2. 只有独立受众／生命周期或必须作为工具输入的完整资料才新建文档，并在此登记负责人文档、状态和结束条件。不要为每次修复、评审、QA或阶段创建一个Markdown。
3. 临时研究先放当前计划的小节；需要实验数据时放对应产物目录。结论定后迁入权威文档，任务勾完后删除过程文字。只留下无法重建且有明确复核用途的来源证据。
4. 交接在当前计划更新“基线、下一步、未决与验证边界”，不另写handoff。发布事实与截图留产物目录，计划只链接；长期规则不放交付报告。
5. archive仅留有具体保留理由的资料；本次只收版本证据索引和一项外部研究。B模型因固定路径校验留原处并明确冻结；不要以“可能有用”归档所有被淘汰稿。
6. 删除独有信息前核对迁移位置；移动后检查Markdown链接、锚点、裸路径及工具引用。描述／线索表、CH章节与game-design的30手艺表属于生成接口，不随意改列、编号或解析边界；只改文档状态不重生成游戏数据。
7. 新版本更新当前计划与必要规则，不在README顶端追加整份发布记录。历史测试只证明当时构建；真机、浏览器、模拟、用户审美不能互相替代。

## 本次盘点与去向

原docs有45份Markdown，整理后17份（含索引、内容源、专项与历史附件），减少28份；web/art制作记录另由14份并为1份。主要问题是阶段记录成为入口、重复版本声明、世界观比较稿嵌入整段旧交接、已实现B/C仍列未开始、旧UI／清洁／求签状态覆盖不清。下表覆盖原有每份docs文档，兼作迁移账本；不再另建整理报告。

| 原文档（相对docs） | 原用途 | 处理与有效内容去向 |
|---|---|---|
| `android-player-guide.md` | 玩家安装／备份／通知与玩法指南 | 合并后删除旧文件 → [engineering.md](engineering.md) |
| `android-v9-qa.md` | 阶段QA／发布记录 | 规则迁入game-design／ui-ux／engineering，历史结论压缩到[证据索引](archive/release-history.md) |
| `b-group-balance/exploration-species.md` | 生成能力表 | 保留／校正状态 → [b-group-balance/exploration-species.md](b-group-balance/exploration-species.md) |
| `b-group-balance/results.md` | 生成数值研究结果 | 保留／校正状态 → [b-group-balance/results.md](b-group-balance/results.md) |
| `b-group-gameplay-design.md` | 已被新版部分覆盖的设计模型 | 固定路径原位冻结，保留可复算模型；当前规则移交game-design |
| `b04-engineering-delivery.md` | 工程交付／验收 | 合并后删除旧文件 → [engineering.md](engineering.md) |
| `b04-engineering-plan.md` | 工程批次计划（已完成） | 合并后删除旧文件 → [engineering.md](engineering.md) |
| `content-access-v10-qa.md` | 阶段QA／发布记录 | 规则迁入game-design／ui-ux／engineering，历史结论压缩到[证据索引](archive/release-history.md) |
| `discovery-clues-v142.md` | 阶段QA／发布记录 | 规则迁入game-design／ui-ux／engineering，历史结论压缩到[证据索引](archive/release-history.md) |
| `four-seasons-v12.md` | 主题内容／配方与数值／发布 | 合并后删除旧文件 → [game-design.md](game-design.md) |
| `game-asset-inventory.md` | 原资产与升级来源 | 保留／校正状态 → [game-asset-inventory.md](game-asset-inventory.md) |
| `game-ui-production-workflow.md` | UI调研／制作与验收规范 | 合并后删除旧文件 → [ui-ux.md](ui-ux.md) |
| `global-ui-v8-qa.md` | 阶段QA／发布记录 | 规则迁入game-design／ui-ux／engineering，历史结论压缩到[证据索引](archive/release-history.md) |
| `holiday-reminders-v141.md` | 阶段QA／发布记录 | 规则迁入game-design／ui-ux／engineering，历史结论压缩到[证据索引](archive/release-history.md) |
| `integration-checklist.md` | 阶段集成清单（已完成） | 合并后删除旧文件 → [plan.md](plan.md) |
| `integration-delivery-v148.md` | 阶段QA／发布记录 | 规则迁入game-design／ui-ux／engineering，历史结论压缩到[证据索引](archive/release-history.md) |
| `ios-port-plan.md` | 未实施工程专项 | 保留／校正状态 → [ios-port-plan.md](ios-port-plan.md) |
| `kitchen-art-direction-20260919.md` | 最新用户美术纠正（仍有效） | 合并后删除旧文件 → [ui-ux.md](ui-ux.md) |
| `kitchen-care-v145.md` | 阶段QA／发布记录 | 规则迁入game-design／ui-ux／engineering，历史结论压缩到[证据索引](archive/release-history.md) |
| `kitchen-entry-cleaning-v144.md` | 阶段QA／发布记录 | 规则迁入game-design／ui-ux／engineering，历史结论压缩到[证据索引](archive/release-history.md) |
| `kitchen-stages-v5-qa.md` | 阶段QA／发布记录 | 规则迁入game-design／ui-ux／engineering，历史结论压缩到[证据索引](archive/release-history.md) |
| `kitchen-stages-v7-qa.md` | 阶段QA／发布记录 | 规则迁入game-design／ui-ux／engineering，历史结论压缩到[证据索引](archive/release-history.md) |
| `kitchen-ui-todo.md` | 累积TODO／历史制作日志 | 合并后删除旧文件 → [plan.md](plan.md) |
| `kitchen-v4-qa.md` | 阶段QA／发布记录 | 规则迁入game-design／ui-ux／engineering，历史结论压缩到[证据索引](archive/release-history.md) |
| `kitchen-v4-style.md` | 旧UI／美术规范 | 合并后删除旧文件 → [ui-ux.md](ui-ux.md) |
| `launch-cover-v13.md` | 阶段QA／发布记录 | 规则迁入game-design／ui-ux／engineering，历史结论压缩到[证据索引](archive/release-history.md) |
| `mqxq-research/妙奇星球玩法调研.md` | 外部研究与原始证据 | 归档，保留独有研究／评分矩阵 → [研究](archive/mqxq-research/妙奇星球玩法调研.md) |
| `original-content-access.md` | 原版出处／离线改编与获取规则 | 合并后删除旧文件 → [game-design.md](game-design.md) |
| `permission-request/README.md` | 授权准备与证据 | 保留／校正状态 → [permission-request/README.md](permission-request/README.md) |
| `permission-request/request-letter-zh-TW.md` | 未发送信件草稿 | 合并后删除旧文件 → [授权询问信草稿](permission-request/README.md#授权询问信草稿) |
| `recipe-book-v14.md` | 配方与售卖设计／发布 | 合并后删除旧文件 → [game-design.md](game-design.md) |
| `round2-optimization/鸡宝厨房第二轮优化设计方案.md` | 已确认二轮设计／UI／验收 | 玩法迁入[game-design](game-design.md)，UI／文案迁入ui-ux，迁移／验收纳入engineering／plan；删除旧方案 |
| `safe-development-update.md` | 工程备份／构建／更新操作 | 合并后删除旧文件 → [engineering.md](engineering.md) |
| `shrine-ui-v11-qa.md` | 阶段QA／发布记录 | 规则迁入game-design／ui-ux／engineering，历史结论压缩到[证据索引](archive/release-history.md) |
| `species-content-guide.md` | 内容创作与维护规范 | 合并后删除旧文件 → [worldbuilding-kitchen-story.md](worldbuilding-kitchen-story.md) |
| `species-descriptions.md` | 全量内容生成源 | 保留／校正状态 → [species-descriptions.md](species-descriptions.md) |
| `species-discovery-audit.md` | 内容生成源／获取复核 | 保留／校正状态 → [species-discovery-audit.md](species-discovery-audit.md) |
| `start-screen-v143.md` | 阶段QA／发布记录 | 规则迁入game-design／ui-ux／engineering，历史结论压缩到[证据索引](archive/release-history.md) |
| `warm-resume-v146.md` | 阶段QA／发布记录 | 规则迁入game-design／ui-ux／engineering，历史结论压缩到[证据索引](archive/release-history.md) |
| `world-journey-design.md` | 旧旅行／探索候选方案 | 合并后删除旧文件 → [plan.md](plan.md) |
| `worldbuilding-a-handoff.md` | A组阶段性交接（已完成） | 合并后删除旧文件 → [worldbuilding-kitchen-story.md](worldbuilding-kitchen-story.md) |
| `worldbuilding-and-expansion-brief.md` | 阶段主交接／旧TODO | 旧需求并入核心文档，仅保留工具兼容索引 |
| `worldbuilding-foundation-discussion.md` | 已覆盖的基础讨论／否决候选 | 删除重复及已否决候选；确认前提／否决边界迁入[世界与内容](worldbuilding-kitchen-story.md) |
| `worldbuilding-framework-options.md` | 已否决方案比较／重复交接 | 删除重复及已否决候选；确认前提／否决边界迁入[世界与内容](worldbuilding-kitchen-story.md) |
| `worldbuilding-kitchen-story.md` | 正式世界／三章正文 | 保留／校正状态 → [worldbuilding-kitchen-story.md](worldbuilding-kitchen-story.md) |

其他目录：根README重写为入口；Android README缩为工程导航；14份web/art制作记录合并为1份provenance，保留完整提示词和裁切来源；二轮交付移出重复手艺表，保留验收证据；字体／测试适配器README、第三方工具文档／许可、原版反编译资料及真实存档不改。资产探索笔记与图像相邻保留来源身份，不作为当前要求。二轮mockup中的设计链接、3个历史辅助脚本的文档输入／链接基准同步更新，未改游戏实现。历史JSON盘点中的旧文件名作为当时证据保留，迁移去向查本表，不改写历史报告。
