# 验收覆盖索引

本表指向实际断言，不用测试总数替代需求验收。当前通过/失败以发布报告及最终门禁日志为准；这里不是另一次运行记录。行号在当前候选下核对。

|合同/需求|可复现断言所在文件|
|---|---|
|runtime严格schema、作者→runtime|[runtime-content-compiler.test.mjs:59](../../tests/runtime-content-compiler.test.mjs#L59)|
|193身份冻结、241总数/152鸡/89鸭|[runtime-content-compiler.test.mjs:17](../../tests/runtime-content-compiler.test.mjs#L17)|
|原75材料与83材料|[expansion-registry.test.mjs:19](../../tests/expansion-registry.test.mjs#L19)|
|稳定ID与allowed白名单|[runtime-content-compiler.test.mjs:125](../../tests/runtime-content-compiler.test.mjs#L125)|
|1→6完整链与资产/票据|[golden-saves.test.mjs:20](../../tests/golden-saves.test.mjs#L20)|
|迁移无时间/随机/发奖|[golden-saves.test.mjs:52](../../tests/golden-saves.test.mjs#L52)|
|schema6迁移不自动追认|[collections.test.mjs:23](../../tests/collections.test.mjs#L23)|
|future/corrupt保护|[save-store.test.mjs:30](../../tests/save-store.test.mjs#L30)|
|命令幂等、payload重复、revision冲突|[expansion-transactions.test.mjs:27](../../tests/expansion-transactions.test.mjs#L27)|
|ACK不明停写回读|[expansion-transactions.test.mjs:39](../../tests/expansion-transactions.test.mjs#L39)|
|双写者锁|[expansion-transactions.test.mjs:43](../../tests/expansion-transactions.test.mjs#L43)|
|经济/视觉RNG分离|[regional-batch.test.mjs:137](../../tests/regional-batch.test.mjs#L137)|
|TRSQ/free、reserve不扣T|[business.test.mjs:26](../../tests/business.test.mjs#L26)|
|Q不是交付、取消释放|[orders.test.mjs:60](../../tests/orders.test.mjs#L60)|
|库存损耗后的零Q|[order-loss-zero.test.mjs:8](../../tests/order-loss-zero.test.mjs#L8)|
|BatchPlan单模式/保底不叠|[regional-valley.test.mjs:145](../../tests/regional-valley.test.mjs#L145)|
|25%精确边界|[regional-batch.test.mjs:42](../../tests/regional-batch.test.mjs#L42)|
|第四批保护与owed|[regional-batch.test.mjs:55](../../tests/regional-batch.test.mjs#L55)|
|实际收录才解锁复刻|[regional-batch.test.mjs:103](../../tests/regional-batch.test.mjs#L103)|
|发现卡概率、上限|[regional-exploration.test.mjs:145](../../tests/regional-exploration.test.mjs#L145)|
|第四趟保护/跨地点|[regional-valley.test.mjs:71](../../tests/regional-valley.test.mjs#L71)|
|免费知识补全|[regional-exploration.test.mjs:108](../../tests/regional-exploration.test.mjs#L108)|
|带货与同队资格|[regional-bay.test.mjs:121](../../tests/regional-bay.test.mjs#L121)|
|首标本与引路|[regional-bay.test.mjs:42](../../tests/regional-bay.test.mjs#L42)|
|订单分批、尾款一次|[orders.test.mjs:46](../../tests/orders.test.mjs#L46)|
|项目按阶段支付|[projects.test.mjs:53](../../tests/projects.test.mjs#L53)|
|项目ACK/CP原子性|[projects.test.mjs:120](../../tests/projects.test.mjs#L120)|
|营业0/2h/24h、长期不重开|[business.test.mjs:32](../../tests/business.test.mjs#L32)|
|在线/离线完整状态一致|[timeline-business.test.mjs:17](../../tests/timeline-business.test.mjs#L17)|
|同时队伍/营业释放顺序|[timeline-business.test.mjs:30](../../tests/timeline-business.test.mjs#L30)|
|常客同一次营业事实|[regulars.test.mjs:82](../../tests/regulars.test.mjs#L82)|
|常客16×2真实命令分支|[regulars-command-branches.test.mjs:78](../../tests/regulars-command-branches.test.mjs#L78)|
|收藏追认及陈列|[collections.test.mjs:34](../../tests/collections.test.mjs#L34)|
|旧四时回礼不重复|[seasonal-pack.test.mjs:59](../../tests/seasonal-pack.test.mjs#L59)|
|旧神社/活动未领取保护|[golden-saves.test.mjs:85](../../tests/golden-saves.test.mjs#L85)|
|未知内容统一遮罩|[expansion-registry.test.mjs:57](../../tests/expansion-registry.test.mjs#L57)|
|搜索不得泄漏未知名字|[book-ui.test.mjs:25](../../tests/book-ui.test.mjs#L25)|
|48新品逐项静态合法路径|[regional-bay.test.mjs:182](../../tests/regional-bay.test.mjs#L182)|
|24卡逐项合法队伍|[regional-bay.test.mjs:194](../../tests/regional-bay.test.mjs#L194)|
|收成三去向原子提交|[harvest-allocation.test.mjs:11](../../tests/harvest-allocation.test.mjs#L11)|
|schema6完整备份与更新检查|[release-backup.test.mjs:1](../../tests/release-backup.test.mjs#L1)|
|兼容回滚保留结清|[rollback-policy.test.mjs:65](../../tests/rollback-policy.test.mjs#L65)|
|14天模拟器时序/归因|[economy-simulation.test.mjs:21](../../tests/economy-simulation.test.mjs#L21)|
|设备/鸭蛋投资|[economy-investments.test.mjs:9](../../tests/economy-investments.test.mjs#L9)|
|发布门不得少跑/漏扩展|[release-gates.test.mjs:7](../../tests/release-gates.test.mjs#L7)|

## 集成证据

|场景|证据|
|---|---|
|A–L每阶段可玩流程|`artifacts/qa/work-a` … `work-l`；`tools/verify-ui.mjs --release`|
|五入口/桌面/320宽/横屏/150%字级/未知DOM与aria|`artifacts/qa/work-k/report.json`|
|统一收成/输入跨秒稳定/补给回跳/材料见闻|`artifacts/qa/takeover-ui` 与 `release-ui/recipe-book.log`|
|图鉴四页/来源返回/精确库存|`artifacts/qa/book-navigation/report.json`|
|关闭新业务后正式UI仍可结清|`artifacts/qa/compatible-rollback/report.json`|
|48新品/24卡实际取得|`artifacts/sim/reachability-summary.json`，8个种子逐操作轨迹|
|CP守恒/14天/72h/手艺/采购/维护|`artifacts/takeover/economy-audit.md`，64条真实命令轨迹|
|第一次失败/第四锅/设备投入|`economy-pairs.json`、`economy-investments.json`及投资附录|
|Web/Native兼容/107坏档/103合法矩阵/ACK|`artifacts/takeover/native-final-evidence.json` 与 `native-repair.md`|
|Android真实打包/签名/662资源同源|`artifacts/takeover/native-final-evidence.json`；不是设备验收|
|真实2h关闭页面后回访|`artifacts/qa/real-window/report.json`；必须核对本轮时间，非加速|
|真机生命周期/强退/文件选择器恢复|**未执行**；JVM适配测试和浏览器Native mock不替代真机|
|最终美术/3–5新玩家理解度|**未完成外部验收**；概念剪影和自动操作不替代|

## 运行入口

`powershell -ExecutionPolicy Bypass -File tools/verify-release.ps1` 固定9门，浏览器固定22套。真实2h和长轨迹单独运行，避免快速预检误标为已包含真实等待。源码/报告/构建哈希在 `artifacts/takeover/final-provenance.json`。
