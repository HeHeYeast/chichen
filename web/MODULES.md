# web/ 模块地图

游戏是无构建步骤的原生 ES 模块，文件平铺在 `web/`：测试、工具、Android 打包和 HTML 都按 `web/<文件名>` 引用，所以**按用途分组记录在这里，而不是挪进子目录**。入口链：`index.html` → `boot.js`（旧 WebView 检测）→ `app.js`。Android 打包从 `index.html` 沿 import 图收集文件，不在图里的页面/模块不会进 APK。

## 1. 原版规则与数据（改动前先核对原版 Java）

| 模块 | 内容 |
|---|---|
| `data.js` | 原版 XML 数据（品种、厨具、调味料），由 `tools/build-data.py` 生成 |
| `recipes.js` | 原版 `getRateArrayWithID` 的逐行翻译：按蛋种/厨具/等级/调味料把品种按权重放进池子，再无放回抽 24 枚。**生成文件，不要手改** |
| `recipe-catalog-data.js` | 从 `recipes.js` 抽出的配方册条件，`tools/build-recipe-catalog.py` 生成 |
| `engine.js` | 开火 `startBatch`、破壳病变/焦化 `updateBatch`、收取、出售、购买、厨房升级、清洁、存档校验 |
| `legacy-content.js`、`seasonal-pack.js`、`content-pack.js` | 竹蒸笼点心 6 种与四时配方 16 种（193 种冻结边界）；`content-pack.js` 叠加 241 种运行时数据 |
| `legacy-activities.js`、`holiday-calendar.js`、`shrine.js` | 原版服务器活动的离线替代：委托资格、节日窗口、神社签礼 |
| `ingredient-unlocks.js`、`material-capacity.js`、`farm.js`、`farm-clock.js`、`world-clock.js` | 调味料解锁、材料容量、农场展示与逃跑/修缮时钟 |

## 2. 开火决策链

`seasoning-advisor.js`（调味页顶部的推荐：只用已收录/已研读/线索已公开的配方，按配方池精确算出 24 枚的期望产出，按收益、订单与项目缺口、未收录伙伴排序；只读）。`cooking-query.js`（实际参与的调味料，按厨房等级截取 1–3 槽）→ `batch-plan.js`（普通 / 地区试做 / 地方替代 三种计划）→ `legacy-recipe-adapter.js`（原版池或竹蒸笼池抽样）→ 季节配方、复刻只替换第 0 枚。预览用 `candidate-query.js`，与实际开火走同一套条件函数。

## 3. 成长与扩展系统

| 领域 | 模块 |
|---|---|
| 手艺/点数 | `progression.js`、`skill-data.js`、`progress-save.js` |
| 发现与知识 | `species-state.js`（**唯一的"累计收取 / 发现品种"定义**）、`knowledge.js`、`discovery-clues.js`、`discovery-calendar.js`、`facts.js`、`visibility-model.js` |
| 营业 | `business-advisor.js`（摆货页「帮我摆」与预计结果：优先凑完整菜单、再按价值补满，尊重留一只与订单/寻访占用；只读）、`business.js`、`business-model.js`、`business-save.js`、`menu-model.js`、`timeline.js` |
| 订单/常客/项目/收藏 | `orders.js`、`story-orders.js`、`regulars.js`、`projects.js`、`collection-progress.js`、`requirements.js`，各自的 `*-model.js` 为只读投影 |
| 寻访/地区 | `exploration.js`、`regional-exploration.js`、`regional-methods.js`、`region-model.js`、`harvest-allocation.js`、`inventory.js` |
| 内容注册 | `content-registry.js` 汇总 `*.generated.*`（由 `tools/build-runtime-content.mjs` 从 `docs/content-pack/` 编译，**不要手改**）、`integration-data.js`、`trade-data.js` |
| 存档与事务 | `game-commands.js`（每次操作一个事务、推进随机序号）、`save-store.js`、`save-migrations.js`、`rng.js`、`rollback-policy.js` |

## 4. 界面与绘制

- Web 账号／手动云备份：`cloud-profiles.js` 隔离游客和 UID 本机分支，`cloud-client.js` 管理条件上传和恢复日志，`cloud-ui.js` 复用设置皮肤，`cloud-config.js` 只含公开地址。服务端位于独立 `cloud/`，不进入客户端 import 图；默认未配置线上服务，Android／review 不启用账号入口。

- 画布场景：`scene.js`（厨房）、`kitchen-golden.js`（正式四级厨房；蛋堆布局来自生成的 `kitchen-egg-nests.js`）、`kitchen-stages.js`、`farm-scene.js`、`farm-world.js`、`farm-theme.js`、`title-scene.js`、`theme.js`、`tool-strip.js`。
- 寻访地图：`regional-ui.js` 的 `mapMarkup` 把地形（`journey-art.js` 的 `journeyEnvironment`）、路线、地点和同行伙伴放进同一块按 390×684 参考坐标等比缩放的画布，只显示有地形的上方 520；尺寸规则在 `mobile-fit.css` 末尾，地点图标用 `cqw` 随画布缩放。
- 改版共用部分（2026-10）：`game-frame.js`＋`game-frame.css`（星级、伙伴格子、按钮呼吸与停顿箭头、第一次指引、「?」图卡；不放常驻提示条）；`next-batch-ui.js`（下一锅：「推荐」书签 + 新伙伴/订单/多赚、缺调料开火时买）；`next-batch-goals.js`（「推荐」的卡片：追踪的伙伴第一，其余按推进价值排，只读）；`clue-book.js`＋`clue-book-ui.js`（线索册独立页：调查卡、☆ 追踪 `progress.knowledge.tracked`、全部伙伴筛选）；`clue-regions.js`（每只伙伴的主线索地区）；`regional-clues.js`（48 只地区伙伴在线索册里的五层：方向＝厨具和第一味、完整方法＝第 5 层，方向之前的地区步骤，逐枚试做概率与旧锅兼容）；`journey-model.js`（寻访地图卡片的数据：地区进度、追踪去向、归来线索 x/5 → y/5）；`business-home.js`＋`business-home-ui.js`（生意主页：今日营业、订单板、常客/项目小卡；只读模型 + 画面）；`order-delivery.js`（订单一步交付、展示型摆出来、厨房往事一步交付、订单等着的伙伴）；`order-intel.js`（订单情报：调查线索或地点提示 `progress.knowledge.hints`）；`warehouse-ui.js`（仓库：伙伴/材料、卖掉多余预览）。
- 面板 UI：`*-ui.js`（`collection-ui`、`shop-ui`、`business-ui`、`order-ui`、`regular-ui`、`project-ui`、`regional-ui`、`workshop-ui`、`recipe-book-ui`、`book-ui`、`farm-map-ui` 等），以及 `*-view.js`、`*-art.js`、`*-icons.js` 等纯展示辅助。
- 样式：`index.html` 按顺序加载 31 个样式表；后加载的 `visual-polish.css`、`ui-remaster.css`、`journey.css`、`mobile-fit.css` 等覆盖前面的基础样式。
- 平台：`native-platform.js`（Android 桥）、`device-check.js`（设置 → 设备检测：网页组件版本、功能、画面/点按对齐和 `boot.js` 记下的脚本错误，可复制发回）、`ui-preferences.js`（仅本机界面偏好）、`sfx.js`（音效解码一次后用 Web Audio 播放；每次克隆 `<audio>` 会让 Android WebView 新建播放器，收取时卡顿）。

## 5. 非运行时页面

`classic.html`/`classic-app.js`（旧版对照）、`baseline-20260908/`（返工前基线，供 `compare.html`、`art-board.html` 对照）、`review.html`、`ui-review.html`、`stage-review.html`、`icon-review.html`、`asset-plan.html`、`prototypes/`（未采纳的厨房原型）。这些都不进 APK。

## 6. 循环修订（2026-10-10）

- `hatch-probability.js`：常见／少见／稀有的逐枚概率；`batch-plan.js` 版本2地区票据，版本1只负责旧锅结算。
- `new-partner-advice.js`：线索册顺序的最多五个新伙伴目标；`ingredient-flavors.js`：调味类别共享词表。
- `extra-regions.js`：果园与菌圃的两档材料、路线和关联伙伴；沿用已有身份与供货条件。
- `loop-guide.js`：由已完成事实生成的首次循环提示、由实际库存生成的菜单建议。
- `visitor-dialogue.js`：已确认的初访／回访对白；`regular-ui.js` 使用人类场景和多轮阅读，故事阶段仍按对应营业／订单文案呈现。
- `tools/qa-loop-review.mjs`、`tests/loop-rebuild.test.mjs`：多宽度对话、真实新伙伴与新地区交互、概率分布和组合衔接验证。
