# 文档导航

维护日期：2026-10-09。继续开发先读[当前计划](plan.md)，再读对应规则。本文只负责导航；历史对话、已完成操作和旧版测试数量不作为当前待办。

## 日常必读

| 文档 | 唯一职责 |
|---|---|
| [当前计划](plan.md) | 当前基线、下一步、未决与验证边界 |
| [游戏设计](game-design.md) | 核心玩法、数值与规则不变量 |
| [世界与内容](worldbuilding-kitchen-story.md) | 世界设定、故事正文和作者规范 |
| [UI 与美术](ui-ux.md) | 交互规则、画风与制作验收标准 |
| [工程与维护](engineering.md) | 架构、存档、测试、签名、构建与备份 |

## 按任务查阅

| 分类 | 内容 |
|---|---|
| 设计 `design/` | [玩法扩展](design/gameplay-expansion-design.md)、[内容资产](design/content-expansion-assets.md)、[工程迁移合同](design/engineering-integration-design.md)、[UI 信息架构](design/ui-information-architecture.md)、[视觉语言](design/ui-visual-language.md)、[界面参考](design/ui-game-references.md)、[1.5.19 循环设计](design/loop-design-20261007.md) |
| 平台 `platform/` | [云备份实施合同](platform/cloud-wechat-implementation.md)、[早期云服务调研](platform/cloud-save-design.md)、[iOS 未实施方案](platform/ios-port-plan.md) |
| 质量 `quality/` | [设备发行清单](quality/device-release-checklist.md)、[断言覆盖](quality/verification-coverage.md)、[兼容回滚](quality/compatible-rollback.md)、[回归审计](quality/regression-and-ui-audit.md)、[美术资产盘点](quality/visual-asset-inventory.md) |
| 内容生成 | [内容包审计](content-pack/audit.md)、[193 品种描述](species-descriptions.md)、[发现线索](species-discovery-audit.md)、[B 组数据](b-group-balance/) |
| 素材与许可 | [原素材来源](game-asset-inventory.md)、[角色制作规范](art/character-design-spec.md)、[授权准备](permission-request/README.md) |
| 历史 `archive/` | [历史档案导航](archive/README.md)：旧实施计划、逐轮操作记录、验收报告与决策依据 |

设计附件中有保留的原始阶段口径；实现状态始终以当前计划和本次代码验证为准。视觉轮次目录保留 mockup、来源、切图与验收引用，不能仅因名字带日期就删除。

## 维护方式

1. 新结论写入负责该主题的规则文档；只在当前计划保留尚未完成的任务。不要为每次对话、截图、修复另建交接文档。
2. 已完成工作的操作流水进入历史档案，当前计划只保留结论和链接；候选、确认、实现、验证四种状态分开写。
3. 截图、浏览器输出和生成调用日志放 `artifacts/`，不堆进当前文档。原始对话不重复粘贴；保留最终决定、否决理由、来源和无法重建的证据。
4. `content-pack/`、品种表、故事 CH 章节、30 项手艺表、B 组机器输入以及原作来源属于生成接口。修改格式或移动前检查工具引用。
5. 移动文档时同步相对链接和脚本路径；历史测试结果保留日期，不能替代本次验证。新增生成物默认不入库，确需保留时显式选择。

2026-10-09 整理：将根目录 26 份专项／历史文档归入 `design/`、`platform/`、`quality/` 和 `archive/iterations/`。旧索引的迁移账本及计划全文保留在[历史档案](archive/README.md)，避免丢失决策依据；不再从首页加载这些流水。
