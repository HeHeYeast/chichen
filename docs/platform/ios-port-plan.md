# 鸡宝厨房 iOS 移植评估与实施方案

评估日期：2026-09-22。依据：当前工作区 1.5.0 候选版及其后追加的手艺图标／反馈实现，游戏存档 schema 3。目标设备已确认：**iPhone 13 mini**；已安装的 iOS 版本和可用 Mac 尚未确认，不影响本次方案完成。

**目标已按用户补充调整：优先装到用户自己的另一台 iPhone 自用，不上架、不对外分发。** App Store 的准备工作仅保留在附录，既不属于近期实施范围，也不是个人真机验证的前置条件。本次只评估、记录方案和运行既有检查，没有实现 iOS 工程、改动游戏源码或操作玩家手机存档。

本文供用户决定实施范围，也供后续开发者按文件落点完成移植；阅读后应能从第11节阶段1开始建立可验证的个人安装样机，而不是直接重写游戏。

## 1. 结论与推荐范围

推荐 **现有 JavaScript 游戏 + Canvas／HTML／CSS 界面 + Capacitor 8 的 WKWebView 容器 + 少量 Swift 平台服务**。Android 继续使用现有自定义 WebView 外壳，不在本次顺便迁移 Android 容器，不引入 React、Unity 或另一套游戏引擎。

这不是把桌面原生程序改写成手机程序：当前正式游戏已经是按手机竖屏设计的离线 Web 游戏，Android 用 Java 提供存档、通知、文件选择和生命周期接口。真正需要移植的是这层平台能力，以及 Chrome／Android WebView 和 iPhone WKWebView 之间的行为差异。

近期建议范围：

- 用户的 iPhone 13 mini、竖屏、离线运行；初步按 iOS 17 及以上评估，最终以手机系统版本确定最低版本，不要求用户现在升级系统。
- 保留全部 193 品种、配方、30 项手艺、采购、寻访、图鉴和既有艺术风格，不重新平衡数值。
- 使用原生可靠存档；支持 Android 导出的 JSON 导入 iPhone，支持从 iPhone 再导出。
- 保留孵化完成本地通知；不增加账号、服务器、云同步、广告、付费、联网校时或后台常驻。
- 先用免费 Apple Personal Team 安装验证；长期使用的签名维护方式另选。免费签名不是永久安装。

**最大工程风险是同步存档改异步后破坏交易一致性，其次才是布局。** 简单加壳、让页面显示出来，只能算技术样机。

## 2. 仓库事实、阅读依据与验证边界

### 2.1 当前版本的权威顺序

按“当前运行代码与生成数据 → 最新交付及真机报告 → 设计稿 → 历史版本说明”判断已实现行为：

1. [README](../../README.md)、[Android 版本配置](../../android/release.json)：1.5.0，versionCode 16，Android 应用 ID `com.jibao.kitchen`。
2. [第二轮交付](../../artifacts/round2-implementation/delivery.md)、[手艺表现交付](../../artifacts/skill-polish/delivery.md)：30 项单级手艺及新界面已实现。README 前部新增安装记录优先于下面残留的“未安装手机”旧文字。
3. [第二轮设计方案](../game-design.md)是理解改动意图的依据，不能把其中仍写“尚未实现”的状态照搬。该稿建议 schema 4，当前代码实际上采用 **schema 3 + skillVersion 2**；iOS 不应根据设计稿自行升级为 schema 4。
4. [合并交付](../archive/release-history.md)、[工程计划](../engineering.md)、[工程交付](../engineering.md)、[温恢复](../archive/release-history.md)、[安全更新](../engineering.md)提供迁移和保护进度的契约。
5. [正式世界基础及故事](../worldbuilding-kitchen-story.md)、[主交接](../worldbuilding-and-expansion-brief.md)、[A 组交接](../worldbuilding-kitchen-story.md)、[品种内容规范](../worldbuilding-kitchen-story.md)、[193 项描述](../species-descriptions.md)、[发现线索审计](../species-discovery-audit.md)定义内容边界；其中旧的“本轮仅文档”描述不否认随后已经接入的三章故事。
6. [B 组玩法设计](../b-group-gameplay-design.md)、[数值结果](../b-group-balance/results.md)、[探索能力表](../b-group-balance/exploration-species.json)解释设计来源，但旧手艺模型不能覆盖 `skill-data.js` 的新模型。
7. [原版获取核对](../game-design.md)、[节日修正](../archive/release-history.md)、[配方册](../game-design.md)、[四时内容](../game-design.md)、清洁／厨房阶段及历次 UI QA 记录用于追踪兼容性。例：v12 手作最初主动指定，后续已经增加首次偶遇和知识边界，不能回退成旧入口规则。
8. 美术依据包括 [资源盘点](../game-asset-inventory.md)、[制作流程](../ui-ux.md)、[厨房风格](../ui-ux.md)、[近期美术方向](../ui-ux.md)、`web/art/*notes.md`、图集 manifest 和当前运行截图。妙奇星球调研、mockup 是参考，不是必须新建的玩法。

本次盘点了 `docs` 中既有 44 份 Markdown 文档及其结构，并沿正式入口核对渲染、交互、玩法、数据生成、存档、原生桥接和构建链。反编译原作中与数据／配方有关的路径作为来源核对；旧广告 SDK、DEX、原生二进制及历史截图不属于当前要翻译的应用源码。

### 2.2 当前环境已取得的证据

| 检查 | 本次结果 | 能证明什么／不能证明什么 |
| --- | --- | --- |
| `npm test` | 256 项通过，0 失败 | 当前 Node 规则及接口回归；不能证明 iOS 的 WebKit 和 Swift 实现 |
| `npm run test:ui` | skill-art、integration、tool-strip、recipe-book、save-ui、resume、kitchen-care 七组通过 | 当前 Chromium 隔离存档界面回归；不是 iPhone 触摸、通知或真机后台验收 |
| 正式运行清单与源文件逐项校验 | 609 项，51,850,675 字节，SHA256 不匹配 0 项 | 现有打包资产基线一致；不是未来 IPA 大小或 iOS 包校验 |
| 当前界面截图复核 | 查看 320 宽手艺页、375 宽配方册等已有回归输出 | 新卡片和固定操作栏已经存在；不能把 mockup 当真实页面 |
| iOS 工程、模拟器、真机 | 仓库没有现成 iOS 工程；本次未运行 | 所有 WKWebView、签名、通知可靠性结论仍须阶段 1–5 实测 |

证据：[Node 日志](../../artifacts/qa/ios-port-baseline-node.log)、[UI 日志](../../artifacts/qa/ios-port-baseline-ui.log)、[UI 总报告](../../artifacts/qa/release-ui/report.json)、[仓库与资源审计](../../artifacts/qa/ios-port-repository-audit.json)。UI 总报告明确 `nativeDeviceTest: false`。本次未重新签名构建 Android，历史 JVM／安全更新验证不伪装成本次重新执行结果。

当前目录不是 Git 工作树。后续改实现前应使用现有源码快照工具留基线；不能假设存在可回退的 Git 提交。

## 3. 技术栈与目录结构

| 目录／入口 | 当前用途 | iOS 处理 |
| --- | --- | --- |
| `package.json`、`server.mjs` | 原生 ES Modules；无前端框架、无现有运行时 npm 依赖、无必需打包器；Node 提供本地静态预览 | 保留测试和预览；Node 服务不随 App 运行 |
| `web/index.html`、`app.js` | Canvas 舞台、DOM 控件／面板／对话层；启动、输入、状态提交、声音和调度 | 保留入口和界面，抽出平台及事务协调 |
| `web/theme.js`、`scene.js`、`farm-scene.js`、`title-scene.js` | 320 逻辑宽度，Canvas 2D 绘图、布局与动画 | 主体复用，适配视口、分辨率和帧调度 |
| `web/*-ui.js`、相关 CSS | 商店、图鉴、配方册、手艺、寻访、采购、神社、设置、手册等 DOM 界面 | 局部调整尺寸、滚动、输入和异步回调；不整体 SwiftUI 重建 |
| `web/engine.js`、`progression.js` 等 | 玩法状态和纯规则 | 直接复用，保持平台无关 |
| `web/data.js`、`recipes.js`、`integration-data.js` 等 | 生成后的游戏规则、文案和内容 | 原样打包；改内容应修改生成源 |
| `assets/*.xml`、`tools/build-*` | 原始数据、配方转换、内容生成和字体生成 | 开发资料及工具保留；运行时不再解析原始 Java |
| `assets/png`、`assets/music`、`res/raw`、`web/art`、`web/fonts` | 图像、音乐、音效、中文字体 | 按正式运行清单打包，避免整库复制 |
| `web/native-platform.js`、`save-store.js` | 浏览器和 Android 桥接、备份与存档适配 | 必须抽象；详见第 6 节 |
| `android/app/src/main/java/com/jibao/kitchen` | Activity、可靠存档、通知、恢复接收器、更新备份 Provider | 行为契约复用，Java／Android API 用 Swift 实现 |
| `android/package-runtime.mjs`、`tools/*android*.ps1` | 资源收集、哈希、Android 构建签名及安全更新 | 提取公共资源收集；iOS 增加独立构建／安装步骤 |
| `tests`、`tools/qa-*`、`android/tests` | 规则、浏览器、原生契约及更新测试 | Node／大部分浏览器场景复用；Java 系统替身不能验证 iOS 原生服务 |
| 根目录 `AndroidManifest.xml`、`classes.dex`、`lib`、`META-INF` 等 | 历史 APK 解包资料 | 不进入 iOS，不尝试将 `.so` 或 DEX 转换为 iOS 库 |
| `artifacts/original-source`、旧 `web/baseline-*`、classic／review 页面 | 来源研究、历史实现和开发预览 | 保留作为研究记录，排除 iOS 正式运行资源 |

当前正式模块没有账号、后端业务 API、广告请求或联网同步依赖。解包目录出现广告资源，不代表现有运行入口还在运行广告 SDK。

## 4. 游戏逻辑与数据：复用时必须保留什么

### 4.1 内容与标识

`data.js` 提供原版 114 鸡／57 鸭，`content-pack.js` 增加 6 鸡及竹蒸笼，`seasonal-pack.js` 增加 8 鸡／8 鸭，合计 **128 鸡、65 鸭、193 品种**。现有 9 类厨具各 3 级，共 27 外观，75 种调味料，4 个厨房阶段。

`species-state.js`／`catalog.js` 以 `egg:id` 标识品种；C001、D001 是展示编号。iOS 禁止重新编号、把数组索引当全局 ID、把展示编号写入存档，或为迁移另建一套 Swift 配方表。

生成关系：

- `tools/build-data.py` 从 `assets/characters_file_*.xml`、`tools_file_*.xml` 及原作 `MainGameUnit.java` 对应配方逻辑生成 `data.js`／`recipes.js`。实际抽取与符号候选应继续共享来源。
- `tools/build-recipe-catalog.py` 生成 `recipe-catalog-data.js`；`recipe-book.js` 管理实际可用配方路径。
- `tools/build-integration-content.mjs` 将 193 描述／发现线索、探索配置和三章故事写入 `integration-data.js`。当前手艺以 `skill-data.js`、经营分类以 `trade-data.js` 为直接依据，不能仅重新生成旧 B 组模型。
- `tools/build-font.py` 生成中文子集。新增界面文字要重新检查覆盖，而不是人工修改二进制字体。

### 4.2 规则与时间

| 规则组 | 当前代码／事实 | 移植不可破坏的契约 |
| --- | --- | --- |
| 初始经营 | `engine.freshState`：600 CP、厨房等级 0、保温灯可用、初始材料、schema 3 | 读档错误不能被当作新玩家，不能用初始状态覆盖旧档 |
| 开火与收取 | `cookInfo`／`startBatch`／`updateBatch`／`collect`：每批 24 枚，先确定结果和各枚时间，原有 6×4 蛋位保存在批次中 | 改屏幕尺寸不能改保存的蛋坐标；重启、迁移、重配不重抽结果；费用和材料仅提交一次 |
| 当前批次快照 | `batch.rules` 的规则版本与开火时参数 | 新手艺仅按已有生效边界作用，新系统不能追溯缩短旧批次或重置保鲜 |
| 手艺 | `skill-data.js`／`progression.js`／`progress-save.js`：5 方向×6项、点数来源54、全局一个专精、skillVersion 2 | 旧树一次性退点；新版本不每次启动补偿；知识、订单、余数及快照保留 |
| 经营与采购 | `trade-data.js`、`story-orders.js`、`inventory.js`：3 笔常驻采购，分批货款与一次性酬谢；合计额外2120 CP | 采购不误套普通售卖加价；展示报价与提交时可用库存一致；重复点击不重复结算 |
| 寻访 | `exploration.js`：一队1–3不同品种，2／6／10小时路线，轻装改变对应时长／基础收益；出发锁定奖励 | 成员占用来自当前队伍；在家可用数统一经 `availableCount`；到期释放占用、满包分批领取、CP／线索／物品不重复 |
| 离线世界 | `world-clock.js`／`engine.resume`：毫秒时间戳，到期结算；`logicalAt` 对部分世界推进做非倒退保护 | 不要求后台 JS 持续执行；多次 resume 幂等。该保护不是全局联网防作弊，不应夸大 |
| 农场 | `farm.js`／引擎：按品种聚合库存、照料完好度及脱逃；每2小时损耗，脱逃还受时间／完好度条件控制 | 外出成员不被当作在家可出售／脱逃对象；画面随机走动不变成个体存档模型 |
| 清洁 | 引擎／成长模块：基础36小时，可达54／72小时；提前清洁按当前脏污比例报价 | 不按早期24小时文档重写；切后台和日期变化后重新检查报价 |
| 知识与图鉴 | `knowledge.js`、`candidate-query.js`、`recipe-book.js` | 研读不等于收录，未知名称／完整形象不泄漏；出售后不丢发现；多配方路径不合并成单个固定配方 |
| 季节与节日 | `holiday-calendar.js`、`discovery-calendar.js`、`legacy-activities.js`、`shrine.js` | 16手作全年可发现；旧28节日品种受开火日期窗口约束；永久资格、已持有个体和已确定批次不被节后清除 |

`holiday-calendar.js` 使用本地时区 `Date` 和 `Intl.DateTimeFormat('en-u-ca-chinese').formatToParts()` 寻找农历日期。需要在目标 WebKit 核对农历 month 的表示、闰月排除、跨年／时区和夏令时，而不是假设 Node 的 ICU 输出必然相同。若目标设备缺少相同日历能力，再用可测试的日期适配或有限年份表替代日历实现，不改节日规则。

凤凰使用本地开火小时（10:00–12:59 为对应形态窗口）。批次、寻访、每日礼物、重配冷却涉及不同时间边界；仅把所有 `Date.now()` 换成后台定时器不会解决问题。保持当前离线时钟政策，将时钟前跳／回退作为回归用例。

## 5. iOS 技术路线比较

| 路线 | 当前仓库的复用程度与代价 | 判断 |
| --- | --- | --- |
| **Capacitor 8 + WKWebView + Swift 插件** | JS规则、Canvas、DOM、内容、绝大部分资源和测试可保留；获得标准本地资源加载和插件／生命周期基础；仍需可靠存档与文件交互实现 | **推荐**，最符合已经存在的 Android WebView 分层 |
| 自建 Swift WKWebView 外壳 | 同样高度复用 Web 内容，减少第三方容器依赖；需自行维护资源 scheme、导航限制、桥接、生命周期、文件选择等 | 可行备选；只有 Capacitor 样机出现具体阻碍或确定长期只维护极小自用壳时切换 |
| Safari／主屏幕 Web App | 界面和规则可复用；没有应用签名续期问题，但现有仓库没有完整离线缓存方案，浏览器存档和系统提醒不能直接等价 | 无 Mac 时的可选试用路线，需单独实现 HTTPS、缓存和备份；不能把当前本地预览地址当安装包 |
| SwiftUI + SpriteKit／原生 UIKit | 数据内容可导出，几乎全部画面、输入和状态协调要重写；两种语言维护配方／迁移容易分叉 | 当前没有证据证明值得采用 |
| Unity／Godot／Flutter／React Native | 必须重新实现 Canvas画面、DOM面板和多数交互；引入新的资源及构建体系 | 不适合本次以复用为目的的移植 |

Capacitor 与具体前端框架无绑定，当前原生 ES Modules 不需要先迁移 React。官方 iOS 容器使用 WKWebView，Capacitor 8 的底线为 iOS 15+，开发环境要求 Node 22+、Xcode 26+；这些是容器条件，**不是本游戏已通过 iOS 15 兼容性验证**。[Capacitor 概述](https://capacitorjs.com/docs)、[iOS 支持](https://capacitorjs.com/docs/ios)、[环境要求](https://capacitorjs.com/docs/getting-started/environment-setup)。

本项目采用 iOS 17+ 作为未取得实际系统版本前的规划假设，依据是现有 `structuredClone`、`Array.at`、Canvas、`inert` 和现代 CSS 的实际使用，以及降低小范围自用验证成本；不是说更旧系统绝对不能运行。最终只为用户实际需要的系统增加兼容层。`structuredClone`／`Array.at` 在 Safari 15.4 才加入，足以说明直接取容器最低版本存在风险。[WebKit 15.4 发布说明](https://webkit.org/blog/12445/new-webkit-features-in-safari-15-4/)。

安装时锁定 `@capacitor/core`、`cli`、`ios` 和选用插件的兼容确切版本，提交 lockfile；不要每次构建追随 `latest`。当前官方 8.5 文档已经采用 UIScene 生命周期，新工程应使用相应模板并转发 Scene 事件，不能只照旧教程在 AppDelegate 接后台回调。[Capacitor 8.5 生命周期说明](https://capacitorjs.com/docs/updating/8-5)。

## 6. 平台抽象与可靠存档：改动最大的一部分

### 6.1 当前耦合在哪里

`web/native-platform.js` 只识别 `window.ChickNative`。`platformInfo`、`notificationStatus`、`loadSave`、`saveGame`、`importGame` 都按同步返回 JSON 使用；导入文件、导出和授权另用 requestId 异步响应。部分页面仍按 `info.android` 判定能力，文案直接写“Android版／荣耀／精确闹钟”。

`save-store.js` 的 `load()`、`write()` 是同步接口。`app.js` 的 `act()` 和 `commitProgress()` 依赖“先尝试保存，失败恢复原状态，成功才提交后续表现”。其他直接 `save()` 的入口还包括提醒、设置、页面离开、恢复和定时推进。不能只改一个导出函数就认为桥接完成。

Swift／Capacitor 的常规桥接是异步的。把 `saveGame` 改成 Promise 而保留 `if (!save())` 会把 Promise 当真值：可能先发奖励和动画，后收到保存失败，下一次开局状态又回退。输入、100ms tick 和 resume 还可能同时覆盖彼此。

### 6.2 推荐接口与职责

新增 `web/platform/`，保留 `native-platform.js` 作为过渡入口，提供三个实现：浏览器、既有 Android、iOS。上层统一异步签名；Android 同步桥可被异步适配器包装，原 Java 无须立刻重写。

| 能力 | 建议接口语义 | 分层 |
| --- | --- | --- |
| 启动信息 | `ready()` 返回 platform、appVersion、capabilities | 不再以 `android` 布尔值推断所有能力 |
| 读档 | `loadSave()` → empty／ok／recovered／error + raw + revision | Swift可靠读取；JS严格解释游戏 schema |
| 保存／导入 | `commitSave({requestId, expectedRevision, raw, importing})` | 单写者、原子提交、明确确认结果；游戏 schema 不承载平台 revision |
| 文件 | `chooseBackup()`／`exportBackup(text)` | 取消、无权限、成功分别返回；不把打开分享面板当已备份 |
| 提醒 | `getReminderStatus()`／`requestReminderPermission()`／`openSettings()`／`testReminder()` | iOS 不暴露 Android 精确闹钟／电池白名单选项 |
| 生命周期 | 标准 pause／resume／notificationOpen 事件 | 合并重复来源，冷启动事件可排队 |

桥接应有异常、超时、取消和销毁清理，不沿用当前 `pending` Map 没有超时的假设。超时表示“结果不确定”时，必须读回 revision／requestId 核实，不能自动重试一笔可能已经成功的扣费或领取。

### 6.3 把经济提交串行化，而不是全面重写引擎

建议新增 `web/game-session.js`：

1. 启动等待平台就绪和读档完成，严格校验／必要迁移；确认 empty 才创建新档。错误锁档时不启动自动写入。
2. 所有持久状态变更进入一个串行队列，包括 `act`、`commitProgress`、tick、设置、导入和 resume。排到时从**最新已提交状态**克隆，重新检查库存、报价、批次及资格。
3. 对候选执行当前纯规则，产生一个确定状态。随机结果只在该次候选里产生；原生侧不再重算。
4. 等待原生确认可靠写入，才替换权威内存状态并发出奖励／声音／通知 UI。失败保留之前状态和草稿，反馈具体原因。
5. 收取队列携带批次身份及蛋索引；同一蛋仅提交一次，旧批次排队事件不能命中新批次同索引。保留连续划收，可合并成功提示，不能每只弹确认框。
6. 仅非经济性的时间检查可合并；不随意丢弃领取、出售、采购等操作。事务进行时避免定时刷新重建正在操作的 DOM；旧报价失效则重新确认。
7. 对“文件已写入、确认消息丢失”注入故障：根据持久 requestId／revision 读回协调，既不盲目回滚已经提交的档，也不重新抽一次奖励。

可将小批量纯 UI 调整放在并行开发顺序中，但**接入真实玩家档前必须完成所有写入口的队列化**。不要使用同步 `prompt` 等桥接技巧模拟 Android 返回值。

### 6.4 存档落盘与恢复

当前浏览器键为 `chick-kitchen-v1`、`.previous`、`.pre-import`；键名 v1 不代表内容仍为 schema 1。Android 的 `SaveRepository.java` 在私有 `filesDir/saves` 保存 current、previous、before-import，使用 SHA256 包装和 AtomicFile；损坏恢复与未来版本锁档已经是产品能力。

iOS 建议在 `Library/Application Support/ChickKitchen/saves` 保存对应多代文件，使用 Swift actor 或串行执行器、原子替换和完整性校验：

- 保留 current／previous／before-import；无法读取的原文件在恢复或导入前另留证据，不直接覆盖。
- SHA256 按确切 UTF-8 raw 字节计算；JS和Swift统一 2 MiB **字节**上限。当前 JS 部分按字符串长度，Android 按字节，这是要统一的边界。
- 原生信封版本、游戏 schema、备份格式版本分开。导出仍为 `format: chick-kitchen`、`formatVersion: 1`、内层 save schema 3，包含正确 appVersion。
- JS沿用 `parseSave`／`normalizeSave`／`progress-save.js` 完整业务验证，Swift验证容器、尺寸和基础结构；增加共享 fixture 防两端接受范围分叉。
- current 损坏可恢复已验证 previous；高版本档不偷偷退回较旧档继续玩；全部不可读时锁定自动保存，提供导入／导出保全入口。
- 写入成功与提醒调度成功分别报告。存档已提交后通知调度失败，不能返回“保存失败”让 JS 回滚造成两端分歧。
- 主档不放 `Caches`／临时目录；沙盒绝对目录可能变化，不能保存在 JSON 中。缓存和分享临时文件可清理，玩家档不可按缓存处理。
- 对 iOS 文件保护等级做明确选择并实测：在首次解锁后允许需要的后台原生读操作，同时保留系统文件保护。重启首次解锁前读不到档不能被判断为 empty。

不建议用 localStorage 或把整份高频游戏状态放入 Preferences 作为正式 iOS 存档。Preferences 面向轻量键值设置，并不是高频完整存档数据库；若使用 UserDefaults 保存少量元数据，再按实际 API 配置隐私清单。[Capacitor Preferences 的定位与清单要求](https://capacitorjs.com/docs/apis/preferences)。

### 6.5 两台手机的进度迁移

推荐一期采用**手动整档迁移**：Android 设置导出最新 JSON → 通过用户选择的文件传输方式放到 iPhone Files → iOS 预览备份版本／导出时间／余额／发现数／当前批次 → 用户确认导入 → 先保全 iPhone 原档再写入 → 再按当前时间恢复。

Android 包签名和 iOS Team／Bundle ID 不共享，不能自动读取另一台手机沙盒。两台设备继续各自游玩会形成两个分支；一期不合并 CP、材料、奖励或发现记录。再次导入会替换目标进度，需明确展示备份时间，不自动用旧档覆盖新进度。

导入前后比较稳定字段：余额、厨具、发现、库存、知识、订单、技能、旧批次结果及时间、寻访快照／待领物。正常离线时间推进造成的变化单独记录，不要求整个 JSON 字节永远相同。双向迁移仅在两端都支持相同 schema／内容版本时承诺；Android 老包无法解释未来内容时应拒绝。

`makeBackup` 和浏览器 platform fallback 仍有硬编码 1.4.8，应从统一生成的 `app-version.json` 取展示／导出版本，不能让 iOS 1.5.0 备份自报旧版。

## 7. iPhone 屏幕、安全区域和 UI／UX

### 7.1 当前缩放是真实手机布局，但还不等于 iOS 适配

`theme.fitViewport(width,height,density)` 将逻辑宽度固定320，逻辑高按 `320×height/width` 限制在568–800；缩放取宽高约束中的较小值。`setViewportHeight()` 同时移动 Canvas 和 DOM 的蛋区、厨具、翻页和底部导航。`app.resize()` 读取 `innerWidth/innerHeight`，给游戏根元素设置 CSS `zoom`，Canvas 按 DPR 配置 backing store。

这套布局已解决不少 Android 长屏问题，应保留。当前使用 `zoom` 与 Android 真机曾出现的 transform 裁切有关，不能为了“兼容 Safari”未经验证统一改回 transform。若目标 WKWebView 有坐标问题，在视口适配层实现同一逻辑坐标契约，并同时回归 Android。

### 7.2 安全区域：推荐由原生层拥有

`index.html` 已有 `viewport-fit=cover`，但正式 CSS 没有实际使用 `env(safe-area-inset-*)`。Android Activity 在 WebView 外通过 system bars／display cutout padding 处理可用区域；不能假设同一页面进入 iPhone 后也自动获得安全范围。

一期推荐：自定义 Capacitor ViewController，让游戏内容 WebView 的布局范围受 `safeAreaLayoutGuide` 约束，外层用厨房背景色填充，状态栏选适合浅色背景的样式。Web 内容继续按自己真正获得的可用视口缩放，WebView 不再叠加自动 contentInset，CSS也不重复减 safe-area。必须检查容器自动约束，使用受支持的自定义控制器方式，不通过页面常量猜刘海高度。

若样机证明原生约束不适合容器，再统一改成 Web 层 `env()` + 一个可测量的 usable viewport；两种方案只能有一个安全区域拥有者。严禁 native padding、contentInset 和 CSS env 三重相加。Capacitor配置可控制 `contentInset`，本地 scheme 和页面入口也有明确规则。[Capacitor 配置](https://capacitorjs.com/docs/config)。

验收用实际 safeAreaInsets 和 `getBoundingClientRect()`，而不是只修改浏览器截图高度：顶部金额／设置不撞刘海或灵动岛，底部标签与确认按钮不落入 Home 指示条，导入文件／授权返回后不发生二次缩小。

### 7.3 尺寸与方向

目标机已经明确为 **iPhone 13 mini**，采用5.4英寸屏幕，因此优先保证小屏的文字和操作空间，不因其他大屏截图漂亮就判定适配完成。[Apple设备规格](https://support.apple.com/zh-tw/111873)。原生启动时记录实际屏幕bounds、safeAreaInsets、Web viewport和DPR；不能把2340×1080面板像素直接当CSS布局尺寸。

针对这台手机，第一轮重点检查：厨房顶部金额／设置、底部Home区域、手艺页固定应用栏、开火长弹窗、寻访选人与收益摘要、备份及数量键盘。先用375宽的小屏样例预检，最终以这台手机报告的可用视口和实际手指操作为准。iOS版本等实施前在“设置 → 通用 → 关于本机”确认即可；未知版本不是现在停下评估或要求升级的理由。

先针对用户实际设备，再覆盖下列 CSS viewport 样例；它们是测试几何尺寸，不是把每组数值固定绑定某个最新机型。

| 样例尺寸 | 重点 |
| --- | --- |
| 320×568 | 已有逻辑最小基线、手艺卡片、长弹窗和底栏可达 |
| 375×667 | 短屏安全区扣除后可能由高度限制缩放，不能只测375宽长屏 |
| 375×812、390×844、393×852 | 刘海／灵动岛、中型长屏、三位寻访队员和收益摘要 |
| 430×932及目标大屏实测 | 高DPR清晰度、内存和字体大小，避免任意放大图集 |
| 横屏尺寸及方向切换 | 一期原生锁竖屏；网页预览须有可读提示与可恢复状态 |

当前844×390横屏会把568逻辑高缩到390，游戏宽只约220 CSS像素，12逻辑字号只约8.2像素；不能以“没有溢出”视为可用横屏。用户只需另一台 iPhone 时，锁竖屏是成本最低且与现有设计一致的选择。iPad专用、多窗口、横屏双栏均不作为一期目标；若系统允许 iPhone兼容模式在iPad运行，仍检查启动与基本操作。

### 7.4 控件与字体的实际问题

`theme.js` 的设置按钮34×36、蛋种29×29、提醒34×31、翻页42×25逻辑单位，`app.js` 手艺入口高25，均可能小于舒适触控范围；蛋格约38×42，中心间距约31×26，本身有重叠。部分关闭按钮已经用伪元素扩大命中区域，不能只看外观27×27就判定完全没有适配。

一般操作以最终呈现接近至少44×44pt的触控区域为验收目标，按实际缩放换算而非写44逻辑像素了事。[Apple 按钮指引](https://developer.apple.com/design/human-interface-guidelines/buttons)。

- 设置、蛋种、提醒、翻页和主要入口优先增加不重叠命中范围及必要留白；关闭、返回、确认的位置统一。
- 蛋区不能把24个命中框一律扩大到44而造成更严重歧义。保留前后层级判定和划收，以边缘／中间蛋分别实测；若用户需要简化收取，再评估独立辅助收取入口，不默认改变操作规则。
- 现有不少设置／说明为9–12逻辑字号；第二轮手艺卡已较大，不应重复重做。正文推荐最终约16–17 CSS像素，辅助信息尽量13以上，重要规则不靠低对比小字传达。这是本项目可读性目标，不是宣称Apple规定统一字号。
- 逐步将长文本 DOM 面板与游戏世界缩放解耦：世界保留320逻辑坐标，面板按照安全视口和字号自适应。第一步先调整实际不足页面，不重建整套布局引擎。
- 目标 iPhone 系统“大字体”不能假定自动放大自定义Canvas／Web字体。评估游戏内字号档位；字号增加时允许卡片增高和内容滚动，固定底部按钮保持可见。

### 7.5 按当前页面提出的改造

| 页面 | 已有可复用设计 | iOS需要做的局部调整 |
| --- | --- | --- |
| 厨房 | 上方资产栏、24枚蛋、横滑厨具、四底部导航，清洁和开火弹层 | 安全区域、上方小按钮、低矮入口；小屏开火摘要／确认区分开滚动；触控命中跟随同一坐标变换 |
| 手艺 | 当前已为单级卡片、草稿方案、分支切换及固定应用栏，有专属图标 | 保留；减少小屏页头占高，顶部标签必要时换行，分支切换记住位置；不回退到旧技能长表 |
| 寻访 | 当前已有三队员位、选人界面、收益概率、在途／归队篮子 | 主操作始终可达；短屏保留紧凑收益条；伙伴列表用原生惯性滚动；领取中清楚反馈且保留未领物 |
| 采购／出售 | 当前有分批交付、数量控制、收成表和价格解释 | 数量输入补 `inputmode`、键盘收起和遮挡处理；提交前重新检查可用数；失败保留数量 |
| 图鉴／配方册 | 已知／未知分层、2列配方卡、多级返回及目标配料 | 小屏和大字号允许降低列数；长名称可展开读全；不泄漏未知完整图像，返回恢复筛选与滚动 |
| 商店 | 分类、厨具三级预览、材料购买 | 大图延迟加载；说明字加大；选数量时键盘不遮价格和购买按钮 |
| 设置／备份 | 自动备份、导入导出、音效、通知及帮助 | 将Android／荣耀专属帮助换成能力驱动的iOS文案；突出“导出进度”和签名更新前备份；导入系统面板返回保留当前界面 |
| 手册／故事 | 已有多标签手册及三章正文 | 横向标签可滑且可发现，长文内容滚动；交易成功不强迫播放长故事；字号档位适配 |
| 封面 | 当前有独立启动画面与进入按钮 | 保留；原生启动画面只做短暂静态承接，避免两次长动画；加载失败显示可重试原因 |

### 7.6 触控、滚动、键盘与辅助功能

`app.js` 已有主pointer捕获、松手命中、厨具横向滑动阈值、竖向取消、划收去重和插值、blur／resize／visibility／lost capture取消，以及弹层防点击穿透。这些是有价值的代码，必须复用并做 WebKit 回归。

潜在关键问题：`#game { touch-action:none }` 位于滚动面板祖先，后代 `.scroll { touch-action:pan-y }` 不一定恢复默认触摸滚动。应将禁手势收窄到Canvas操作区／拖动控件，滚动面板祖先允许相应浏览器手势。现有Chromium滚轮／脚本滚动通过，不能证明iPhone手指惯性滚动正常。

键盘开启时使用 `visualViewport` 或原生键盘事件管理**面板可见高度／滚动位置**，不要把整张厨房缩成小图。输入显示字号和系统缩放、焦点滚动、键盘“完成”、选择文本均在真机检查。返回游戏应取消未完成的按压，但保留材料／手艺草稿。

已有 aria标签、status播报、键盘Enter／Space和部分焦点管理；现有弹窗焦点循环偏向button，需补输入、select、checkbox及关闭后焦点恢复。VoiceOver至少能完成导航、购买、查看数量和备份；Canvas图像和等价DOM按钮避免重复朗读。减少动态效果已有实现，保留静态结算提示，不把动画作为获得奖励的唯一证据。

## 8. 资源、文件路径、打包与性能

### 8.1 正式运行资源不是整个仓库

`android/package-runtime.mjs` 已从HTML／CSS／相对JS依赖及资源manifest收集运行文件，并包含指定图片／音乐目录；它排除了开发预览和旧游戏入口。可提取成 `tools/package-runtime.mjs` 公共收集器，Android脚本保留原入口和输出语义，iOS生成 `dist/ios-runtime`。

当前609项约49.45 MiB，包括51JS、13CSS、494PNG、27JPG、21MP3、1WOFF2、1TXT、1HTML。`web/art` 约30.21 MiB，`assets/png`约13.93 MiB，音频约4.01 MiB。此值不包括将来原生框架、签名和压缩，不可直接称作IPA体积。

Capacitor的 `webDir` 需要根入口 `index.html`。推荐构建时复制当前入口到 `dist/ios-runtime/index.html`，同时保持 `/web`、`/assets`、`/res` 结构和根绝对URL有效；不在仓库根移动现有入口。使用本地 `capacitor://localhost` 资源来源，不用`file://`或依赖运行中的`127.0.0.1:4173`，不把Android的假HTTPS域名写进iOS。

现有收集器只理解相对 `.js` 引用；直接在源码里加 `import ... from '@capacitor/core'`，浏览器和现有打包器不会自动解析。仅对平台适配入口增加轻量构建输出，或采用明确可运行的模块分发方式，再纳入依赖清单；没有理由为此重写全游戏。新根入口、适配bundle和版本文件也要进入最终产物清单／哈希验证。

检查大小写、中文文件名、URL编码、CSS `url()`、字体／音频MIME、图集引用及禁止目录穿越。Windows上路径不区分大小写，不能据此推断iOS包内URL都正确。`server.mjs` 字体MIME现有兜底和Android专门MIME实现不同，iOS以自己的实际加载结果验收。

Release包不含docs、反编译源码、APK、玩家备份、签名材料、QA页面和远端调试地址；关闭远程导航和release Web Inspector。个人版本也应限制原生桥只服务包内可信页面，文件picker返回的数据不能拼入可执行脚本。

### 8.2 图片和字体复用

所有27厨具外观已有资源：24原厨具重绘外观加竹蒸笼3级，不需要重新补齐所谓“缺失的三级图”。角色有原始图与新增／重绘图混用；保持 `manifest.js`、`portrait-frames.js` 的帧和裁剪映射，不把整张atlas压进头像。

`web/fonts/chick-ui.woff2` 本次实际626,936字节，约612 KiB，使用Noto Sans SC子集，许可文本随包；字体README旧体积数字不能代替文件检查。原17MB级TTF属于字体生成输入，不随运行包复制。当前启动等待 `document.fonts`，需验证WK字体就绪、中文标点、数字对齐及失败回退。若未来将本字体用在Swift原生文字，补字体注册及Swift文案扫描；普通系统picker使用系统字体，无须搬整份TTF。

### 8.3 优先测量的性能点

- `app.js` 启动 `primary` 包含全部 `ASSET_FILES` 和农场资源，不仅是首屏；改为封面／当前厨房必要项先加载，再按页面加载其余资源。
- 图片缓存Map没有明确淘汰策略。全部 `web/art` 图片按宽×高×4估计解码总量约150 MiB，**这是全量解码估算，不是已测峰值常驻内存**；还有Canvas、DOM贴图及系统开销。
- 430×932、DPR3的一张RGBA画布就约14.3 MiB，多个合成层和缓冲还会增加。保留清晰度，但低性能设备可评估DPR上限或质量档位，以实测决定。
- 原atlas若降采样，必须同步缩放frame／size／clip映射，保留原始资产和派生构建；只缩PNG文件不改坐标会串图。
- 当前RAF持续全场重绘，打开静态面板或封面也在跑；pause时明确停调度，恢复仅启动一套循环。静态场景减少重绘，动画可优先目标60fps，设备不足时稳定30fps优于频繁掉帧。
- 当前100ms tick克隆／比较状态，**不是每100ms必然写盘**；时间检查有变更和较低频逻辑检查点。异步化后仍需要合并非关键检查，避免重复JSON与原生写入排队；不能以节电为由延迟关键消费保存到退出时。

在目标真机用Safari Web Inspector与Instruments测启动、交互、峰值内存和Energy Log；个人版目标先定为30分钟连续游玩无崩溃／持续内存增长、常用操作无可感阻塞，再基于第一次实测记录数值预算。

## 9. 生命周期、声音和本地通知

### 9.1 前后台恢复

当前 `app.js` 通过visibility、pagehide及Android原生pause／resume处理保存和暂停音乐，`resumeGameplay()`推进离线时间。温恢复保留当前页、滚动、未确认弹窗和草稿；真正重建进程回封面；点击孵化通知则在初始化后直达厨房。这是当前产品行为，不是iOS应重新设计的启动规则。

推荐统一生命周期协调器：

- 进入后台：取消pointer状态、停RAF／tick／音效，提交已经接受的操作；后台保存只做补充，不承担唯一的持久化机会。
- 返回前台：恢复读取必要系统状态，串行执行一次世界推进，再刷新现有页面；重复native／document事件合并。
- 系统中止Web内容进程：重建WebView并从最近原生已提交档恢复；不假装JS内存草稿仍存在，也不新建档覆盖。
- 文件选择、通知授权、控制中心或设置返回：保留温恢复界面，不重复开封面、不自动提交之前未确认操作。
- 进程被强制结束、系统内存回收、重启：不依赖最后一次 `pagehide`／`applicationWillTerminate` 一定执行。通过此前已提交的批次／寻访截止时间继续计算。

使用Capacitor App事件或自有插件事件之一做主通路，按实际选用版本转发UIScene；避免注册两套平行逻辑。[Capacitor App生命周期接口](https://capacitorjs.com/docs/apis/app)。

### 9.2 音频

复用 `assets/music` 和 `res/raw` 的MP3及现有音量／开关数据。首次播放仍由用户点击“进入厨房”等手势触发，处理 `play()` 拒绝，不能因加载完成自动播放失败而阻塞启动。真机检查静音开关、控制中心、锁屏、耳机插拔、电话／系统中断及返回后重复播放。

一期建议遵从设备静音，不申请后台音频保活；不要为维持倒计时在后台播放无声音乐。如HTMLAudio无法满足中断恢复，再局部增加AVAudioSession协调，不先重写所有音效系统。

### 9.3 孵化提醒

Android `HatchScheduler.java` 是需保留语义、重新实现机制的典型：它以未收取蛋的最大 `openAt + 3000ms` 作为完成时间，批次token去重，保存后更新安排，已发送token有持久记录，并有独立一分钟测试。不是简单拿 `batch.ends` 或每只蛋注册一条通知。

iOS 使用 `UNUserNotificationCenter` 安排一条当前批次本地通知，无需APNs服务器和后台持续计时。[Apple 本地通知机制](https://developer.apple.com/library/archive/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/SchedulingandHandlingLocalNotifications.html)。

- 建议由小型Swift `ReminderService` 与已提交档协调，复用JS给出的标准完成时刻并以共享fixture核验；若用官方Local Notifications插件，仍需补批次身份、去重、导入协调，插件本身不自动具备本项目语义。
- 权限仅在玩家打开提醒时申请；拒绝后游戏照常使用，设置显示实际权限并允许前往系统设置。
- 成功开新批次、关闭提醒、收完、导入档后，取消过时pending请求并安排正确请求。文件保存成功但通知失败单独反馈。
- 稳定请求ID只保留一个当前批次；测试通知使用独立ID，不干扰真批次。导入旧档时与原生已交付／已安排状态协调，避免每次启动再发同一批提醒。
- iOS没有Android那种能在本地通知真正送达前运行自有广播接收器再检查档的等价保证；应在每次提交和恢复时主动取消／核对，不能照搬AlarmReceiver的执行模型。
- 前台是否呈现系统横幅与游戏内提示去重；点击通知仅导航，不发CP、不替代收取。冷启动先完成读档再消费导航事件。
- 专注模式、通知摘要和系统设置会影响呈现，不能承诺毫秒级准时响铃。游戏倒计时及进度以保存的时间和恢复逻辑为准。

设置中的“精确闹钟”“荣耀后台保护”等仅在Android显示。寻访通知、节日日历后台推送不属于当前原生提醒契约，若新增需另行确认，不在移植时顺便添加。

## 10. 个人安装：环境、开发、调试与续签

### 10.1 需要什么

当前Windows可继续开发共享JS／CSS、运行Node与浏览器检查、整理资源；完整构建、签名和iOS模拟器流程需要Mac上的Xcode。准备与选定Xcode兼容的macOS，具体组合查看[Apple Xcode系统要求](https://developer.apple.com/xcode/system-requirements)，不要只看“有一台Mac”就假设系统足够新。Capacitor 8的Node22+／Xcode26+要求见第5节；Swift Package Manager按当前默认模板使用，只有选中依赖确实要求时才加CocoaPods。

至少需要用户那台iPhone做触摸、静音、通知和后台验证；模拟器不能替代这些。云Mac可以承担构建，但不能自动获得用户家中手机的USB连接，仍需设计包安装和真机日志回收路径。

### 10.2 免费和付费签名怎样选

| 安装方式 | 是否公开上架／审核 | 适合本项目的情况 | 限制 |
| --- | --- | --- | --- |
| Xcode + 免费Personal Team | 不上架，不经App Store审核 | **第一轮推荐**：自己的手机、验证离线运行与存档 | 免费profile发出7天后到期，要重新构建／签名安装；当前每设备最多3个此类App |
| Apple Developer Program + 开发／Ad Hoc安装到已注册设备 | 不上架；注册设备分发不需要Beta App Review | 经常长期自用，愿意支付会员并维护签名 | 仍有证书和profile有效期，不是永久签名；需登记设备，确切到期看生成profile |
| TestFlight | 通过App Store Connect管理；外部测试可能需要Beta审核 | 后续邀请其他测试者时再考虑 | 不等于正式上架，但也是分发服务；仅自己使用没有必要优先引入 |

Apple官方明确Personal Team可用于个人设备测试，profile有效7天，到期需重新安装；免费账号并非不能装。[Personal Team规则](https://developer.apple.com/help/account/basics/about-your-developer-account)。付费计划目前为99美元／年或当地价格，**付费不要求公开发布App**。[会员说明](https://developer.apple.com/programs/enroll/)。注册设备安装、签名和Developer Mode流程按[Apple设备分发说明](https://developer.apple.com/documentation/xcode/distributing-your-app-to-registered-devices)执行。

没有Mac且不想维护签名时，讨论主屏幕Web App作为替代；不推荐为这一个自用游戏采购不明企业签名或把Apple账号交给第三方代签。是否购买Mac／付费会员由用户确认，本次不采购、不注册、不上传账号材料。

### 10.3 后续开发时的可执行顺序

以下为计划，命令在本次没有执行；`build:ios:web`等是后续新增脚本。

1. 快照当前源码与素材，明确用户目标iPhone／iOS及Mac版本；已有Android真实进度导出留存，样机先使用复制后的测试档。
2. 加入锁定版本的Capacitor依赖和lockfile，新增 `capacitor.config.json`，`appName`沿用鸡宝厨房，Bundle ID由实际Apple Team确认可用后固定。Android的ID不是必然能在Apple账号注册的证明。
3. 建立 `npm run build:ios:web`：公共运行依赖收集 → 必要平台适配bundle → 生成版本／资源manifest → 写 `dist/ios-runtime`。输出目录删除操作必须限定在工作区生成目录。
4. Mac中首次 `npx cap add ios`，以后 `npx cap sync ios`，再 `npx cap open ios`。**不执行 `cap add android`**，避免与现有Android目录冲突。
5. 在Xcode选定Team、固定Bundle ID、deployment target和竖屏；配置自定义安全区ViewController、Scene事件、App图标及静态Launch Screen。
6. 连接iPhone，信任Mac，按系统提示开启Developer Mode；选择真实设备作为运行目标，自动签名Build & Run。首次构建与签名需网络，已打包游戏内容应能飞行模式运行。
7. 用Safari Web Inspector检查DOM／Canvas／资源，Xcode看Swift日志和文件错误，用Instruments看内存／能耗。日志记录operationId、revision、耗时和错误码，避免打印整份玩家备份；release关闭可远程检查配置。
8. 完成第12节用例后导出用户最新进度，在iPhone进行一次明确的整档导入。成功后由用户选择哪台手机继续作为主要进度来源。
9. 后续升级／续签：先导出最新备份 → 保持相同Team／Bundle ID覆盖安装 → 检查原进度与当前批次 → 继续游玩。不要为了续签先卸载；必须更换身份时把它视为新应用容器，先保全备份再迁移。开发阶段实测一次到期后的续签和保存恢复。

本地通知不需要启用Push Notifications或后台音频能力。沙盒档可参与系统设备备份，但它不是跨设备实时云同步；不向用户承诺系统备份一定及时或一定存在。

## 11. 分阶段实施计划

路径带“新增”的是建议落点，尚不存在；iOS具体Xcode目录由锁定版本生成，下面采用常见 `ios/App/App` 结构。

### 阶段 0：评估与基线（本次）

- **目标**：确定复用路线、真实风险、自用范围及可验证的实施契约。
- **文件**：新增本文；评估日志和 `artifacts/qa/ios-port-repository-audit.json`。游戏源码不变。
- **风险**：历史文档和当前实现混用，误以为已有iOS验证；已在第2节区分。
- **验收**：用户最初要求的技术／目录／UI／数据／资源／存档／平台／环境／阶段均有仓库依据；实际检查有日志；不能把此阶段标成“iOS版已完成”。

### 阶段 1：目标iPhone最小样机（首个实际开发阶段）

- **目标**：用现有游戏资源在自己的iPhone离线启动，验证容器、资源URL、安全区、字体、触摸和最小异步提交。
- **涉及文件**：新增 `capacitor.config.json`、`ios/`、`tools/package-ios-runtime.mjs`、`web/platform/ios.js`；调整 `package.json`、公共资源收集入口；新建独立测试存档命名空间和原生SaveRepository最小实现。
- **具体步骤**：先跑封面／厨房／手艺页；选择一笔代表操作验证“读取测试档→克隆→规则→异步可靠写入→确认后刷新→杀进程重读”；再验证后台返回和中文字体。其余未接入的经济操作在样机中明确禁用或隔离，不让半完成事务链接触真实玩家档。
- **风险**：无Mac／目标系统不满足、CSS zoom／pointer坐标差异、裸npm模块无法打包、安全区双重扣除。
- **验收**：飞行模式冷启动无缺资源；目标机顶部／底部无遮挡；一笔操作成功后重启保留，失败不扣费；系统返回无双重缩放；记录真机截图和运行日志。若有明确WK性能阻碍，再决定局部原生化或自建WK壳，不能仅凭偏好换引擎。

### 阶段 2：完整事务与跨平台存档

- **目标**：所有写入口进入异步串行提交；建立可用于真实玩家进度的存档和双向JSON备份。
- **涉及文件**：`web/app.js`、`save-store.js`、`native-platform.js`、`settings-ui.js`、调用commit的各 `*-ui.js`；新增 `game-session.js`、`platform/browser.js`、`platform/android.js`；新增Swift `SaveRepository.swift`、`ChickPlatformPlugin.swift`、`BackupDocumentService.swift`；扩展相关Node／共享fixture／XCTest。
- **风险**：Promise被当布尔值、旧报价提交、tick／resume覆盖新状态、ACK丢失重复奖励、导入后旧请求写回、未来版本被新档覆盖。
- **验收**：第6节故障注入全通过；活跃24蛋、部分订单、在途／待领寻访和旧技能迁移完整；重复提交幂等；导入前副本存在；UTF-8边界一致；Android和浏览器原回归不下降。此阶段通过前不正式迁移用户主进度。

### 阶段 3：目标手机UI与交互适配

- **目标**：安全区域、真实触控、键盘和短屏可读性合格，保留当前视觉及页面组织。
- **涉及文件**：`theme.js`、`app.js`、`style.css`、`game-screens.css`、`workshop-ui.*`、`settings-ui.*`、`collection-ui.*`、`shop-ui.*`、`recipe-book*`、`journal-ui.*`；新增视口适配模块；iOS ViewController与Info.plist。
- **风险**：布局改变导致Canvas／DOM命中错位、祖先touch-action禁滚动、放大文字遮住确认、小按钮目标互相覆盖、系统边缘手势误触开火。
- **验收**：第7节尺寸／字号／VoiceOver核心流程及真实手指滚动通过；没有隐藏的不可达按钮；厨房划收／厨具翻页准确；文件picker回来保留草稿。截图必须来自运行版本，不能只交静态mockup。

### 阶段 4：生命周期、声音和本地通知

- **目标**：后台暂停省电、恢复正确；孵化通知与存档协调，系统文件／授权流程可靠。
- **涉及文件**：`app.js`、平台适配／会话模块、`settings-ui.js`、`settings-options.js`；新增Swift `ReminderService.swift`、Scene／AppDelegate及相关XCTest。
- **风险**：重复resume、Web进程被杀、音频中断、通知delegate被覆盖、导入旧档重发、通知失败被误判为保存失败。
- **验收**：前台／锁屏／手动杀进程后提醒按系统允许送达，点击冷／温启动正确到厨房；收完／关提醒后pending取消；专注或拒绝权限不影响游玩；一次真实到期批次与一次真实寻访恢复通过，不能全部用加速时钟替代。

### 阶段 5：性能与个人使用交付

- **目标**：目标机稳定运行，安装／升级／续签不丢进度，形成可重复维护流程。
- **涉及文件**：按测量结果修改 `app.js`、`scene.js`、图集manifest／加载器；新增 `tools/verify-ios.*`、`ios/README.md`、iOS测试清单与构建manifest；扩展源码快照的包含目录（当前工具不能假定自动包含新增ios）。
- **风险**：预载内存峰值、循环重复导致耗电、生成资源漏包、签名身份变化、续签时误卸载。
- **验收**：目标机30分钟游玩、后台来回、断网冷启通过；资源清单与bundle一致；至少一次覆盖升级验证前后档；免费方案完成一次续签流程或在说明中清楚记录尚未到期实测；给用户可安装版本及一页安装／备份／续签说明。

### 可选阶段 6：未来公开发行

仅用户重新要求发布时启动。文件包括隐私政策、权利／素材来源清单、App图标与商店截图、App Store Connect说明、发布构建及审核材料。风险和验收见附录A。本阶段与当前自用交付无依赖关系。

阶段1是应先做的技术验证，不必先完成所有UI整理。阶段2的可靠存档完成后，阶段3／4按依赖可交错推进；最终必须合并回归。目标机已确定为iPhone 13 mini，在确认其系统和可用Mac、完成样机前不承诺工期，尤其不把“加壳几小时”当完整移植估时。

## 12. 验收矩阵与通过条件

| 类别 | 必测场景 | 通过标准 |
| --- | --- | --- |
| 玩法一致性 | 原27厨具、手作／节日边界、30手艺、采购、寻访、未知信息 | 相同输入和固定随机源产生一致结果；不新增消费／奖励／解锁差异 |
| 旧档 | v1／v2／v3、旧技能、旧批次、快照新旧版本 | 合法档迁移成功且仅一次；结果和原截止时间不重置 |
| 存储异常 | current损坏、previous损坏、未来版、磁盘写失败、中文字节超限、ACK丢失 | 不生成新档覆盖；不重复经济操作；可解释恢复和导出保全 |
| 并发输入 | 连点同蛋、划过24蛋、购买时tick、导入同时resume、队列中开新批次 | 每笔最多一次；批次身份校验；不回写旧状态 |
| 离线恢复 | 5分钟／数小时／跨日／时区变化／时钟回退 | 按当前规则结算，归队占用解除、待领篮子和订单不丢 |
| UI | 目标机及小／中／大测试视口，安全区、大字体、键盘、长文 | 无遮挡、误触和不可达主操作；触控与图形一致 |
| 生命周期 | 文件picker、权限弹框、系统设置、锁屏、杀Web进程、重启 | 温恢复保留页面／草稿；冷恢复从持久档开始；无重复循环／音轨 |
| 通知 | 正常批次、取消、导入、重复启动、拒绝授权、冷／温点击 | 单批不重复安排，旧通知能取消；系统点击不发奖励 |
| 文件迁移 | Android→iOS→同版本Android；取消／错格式／高版本导入 | 格式兼容、目标旧档保全、取消无改动；不合并两台进度 |
| 性能 | 首屏、大图鉴、农场、长时间游玩 | 记录真机启动／内存／帧率；无持续增长或系统回收导致丢档 |
| 安装 | 相同身份覆盖、断网使用、免费续签 | 进度保留；不卸载清档；文档说明实际签名有效期 |

共享层继续运行 `npm test`、`npm run test:ui`，对公共构建／保存接口变动加跑现有Android原生存档／通知及安全更新门禁。新增iOS原生XCTest验证文件提交与恢复，XCUITest或真机手工步骤验证系统界面。Playwright WebKit可作补充预检，不能作为iOS WKWebView／原生插件的替代验收。

每次报告分别记录：规则测试、浏览器测试、iOS原生测试、模拟器、真机、安装、存档验证。任一只做了替身／模拟的项目明确注明，不把“进程启动”当“界面和进度正常”。

## 13. 仍需用户确认的设计决定

当前已经确认：**只装自己的另一台iPhone，不公开发布**，所以无需现在准备App Store材料或联系权利人来批准本次评估。

| 待确认 | 推荐默认 | 影响 |
| --- | --- | --- |
| iPhone 13 mini已确认；其iOS版本、可用Mac及macOS版本待实施前确认 | 先针对这台手机，未知系统时暂按iOS17+规划，不要求现在升级 | 决定最低系统、构建环境和需要的兼容层 |
| 签名维护 | 先免费Personal Team验证，接受后再决定是否付费 | 免费7天续签；付费也需维护身份和profile，不能保证永久安装 |
| 两台设备的进度关系 | 手动JSON迁移，明确一台主要游玩设备 | 无自动同步；两台并行玩会分叉，整档导入会替换目标 |
| 方向／设备范围 | iPhone竖屏；暂不做iPad专用和横屏 | 避免为个人使用重做双栏和多窗口布局 |
| UI改造程度 | 保留暖色手绘、320逻辑世界和划收，先修安全区／可读性／滚动 | 如果希望大字号模式或更简化的收取，另定交互验收；不默认增加一键收取 |

这些信息影响下一轮实施，不妨碍本次评估文档完成。技术路线和可靠存档原则无需用户逐项决定实现细节。

## 附录 A：只有未来需要 App Store 时才启用

### A.1 与个人安装不同的准备

需要Apple Developer Program、固定且可注册的Bundle ID、分发签名、App Store Connect应用记录、上传归档包和审核。当前Apple要求自2026-04-28起上传的iOS应用使用iOS26 SDK或更高构建；**构建SDK版本与最低可运行iOS版本不同**。实际提交前再次核对，不把今天的要求视为永久规则。[Apple提交要求](https://developer.apple.com/news/upcoming-requirements/)。

准备App图标、真实设备截图、说明／关键词、支持网址、隐私政策网址、年龄分级、数据隐私申报、出口合规回答、审核联系人及玩法说明。代码／插件使用Required Reason APIs时按实际使用生成 `PrivacyInfo.xcprivacy`；不能因为游戏离线就省略依赖审计。当前看不到运行时数据上传，是否能声明“不收集数据”仍以最终包和所有新增插件为准。[Apple隐私申报定义](https://developer.apple.com/app-store/app-privacy-details/)。

用Xcode Archive验证后上传App Store Connect，先完成需要的TestFlight验证，再选择build提交审核；审核备注解释离线经营、图鉴、手艺及提醒测试方法。现有玩法具有实质内容，不能仅因使用WKWebView认定一定违反最低功能要求，也不能承诺必过。未来功能更新采用签名包，不引入未经评估的远程下载执行代码。[App Review Guidelines，尤其2.5.2／4.2](https://developer.apple.com/app-store/review/guidelines/)。

### A.2 仓库中已有的内容权利记录

[授权准备说明](../permission-request/README.md)明确表示目前不代表已取得授权。正式运行仍使用原作的部分形象、标题、音频、素材和由原始资料转换的内容／逻辑；新增绘画并未替换全部原作内容。因此未来公开分发前需要按实际权利情况确认许可范围或替换相关内容，并整理可追溯来源。不能把“免费”当成已获公开分发许可。审核知识产权要求参见[Apple指南5.2](https://developer.apple.com/app-store/review/guidelines/)。

**这项是未来发行准备，不是本次个人移植评估的阻断条件。** 本文不判定私人复制的具体法律结论，也没有代用户联系原作者、发送邮件或寻求授权。

若未来选择中国大陆商店，Apple明确对游戏要求提供审批编号及相关证明，并按适用情形填写其他备案资料；应先确定发行地区、主体和可用材料，不能认为离线／免费就自动豁免。[App Store Connect地区资料要求](https://developer.apple.com/help/app-store-connect/reference/app-information/app-information/)。当前自用不走这个流程。

### A.3 未来发行验收

Release无测试入口和远端调试地址，资源来源可追溯，最终插件／隐私清单核对完成，系统权限只在需要时申请；新装／覆盖／离线／恢复／导入／导出均完成真机回归；商店截图来自实际运行；提交构建与已验证构建资源摘要一致。审核等待、权利确认及地区材料办理单独计时，不混入技术移植工期。

## 附录 B：代码复核导航

| 要回答的问题 | 优先定位 |
| --- | --- |
| 屏幕坐标为何固定、长屏怎么增加空间 | `web/theme.js`：`fitViewport`、`setViewportHeight`；`web/app.js`：`resize` |
| Canvas和DOM如何保持一致 | `web/index.html`、`app.js`：hotspot／paint；`scene.js`、`style.css` |
| 为什么不能直接改成异步save | `app.js`：`save`、`act`、`commitProgress`及所有直接save调用；`save-store.js` |
| 桥接为何只适配Android | `native-platform.js`：`createPlatform`、`info`及request Map；`app.js`原生事件分支 |
| 什么才是当前真实游戏档 | `engine.js`：`freshState`、`normalizeSave`、`parseSave`；`progress-save.js` |
| 迁移如何不重抽、不重复领取 | `engine.js`批次快照；`exploration.js`出发／归队／领取；`progression.js`技能迁移；`inventory.js` |
| 日历如何影响候选、而不清空历史资格 | `holiday-calendar.js`：`recipeStateAt`、`holidayWindow`；`candidate-query.js` |
| Android已有怎样的恢复能力 | `android/app/src/main/java/com/jibao/kitchen/SaveRepository.java` |
| 当前提醒什么时候响 | 同目录`HatchScheduler.java`、`HatchAlarmReceiver.java`；`engine.js`：`batchReadyAt` |
| 为什么不能打包整个工作区 | `android/package-runtime.mjs`、`server.mjs`路由、`web/art/manifest.js` |
| 新内容到底来自哪里 | `tools/build-data.py`、`build-recipe-catalog.py`、`build-integration-content.mjs`；`skill-data.js`、`trade-data.js` |
| 后续更新怎样保护正在游玩的进度 | `docs/engineering.md`、`tools/source_snapshot.py`、`tools/install-android.ps1`；iOS需另实现对应身份／备份流程 |

本文中的已有文件为代码依据；新增文件仅是实施落点。官方资料检索日期同评估日期，依赖、签名和上架规则在实际实施／提交时复核。
