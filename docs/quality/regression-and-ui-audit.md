# 鸡宝厨房 · Regression 与 UI 审计

## 多机型兼容加固（2026-10-05，1.5.12 / code 28）

用户要求在不能连手机的前提下，确认 1.5.11 是否完善、能否在各类安卓手机上运行。本轮不改玩法、存档格式和签名。

**用真实旧内核复核 1.5.11**：经用户同意，从 Playwright 官方 CDN 下载 Chromium 105（`boot.js` 接受的最低版本）、108、114（反馈华为手机 WebView 版本），三者都确认是旧的 zoom 几何（`zoom:2` 的 100px 元素报告宽 100）。现有离线触屏回归 `qa-packaged-mobile` 在三者上都通过；用同一套回归跑 1.5.10 APK 解出的资源，在 114 上第一步就失败：厨房触摸层 `#controls` 盖住「开始游戏」。这与「按钮、界面都点不动」的反馈一致，说明 1.5.11 的根因判断正确。

**逐按钮对比**：新增 `tools/qa-button-sweep.mjs`，以华为手机近乎新档、主力手机已玩存档两份真实导出存档，在五个主页逐个真实触摸每个可见按钮（每次重新载入），记录有无反应、是否被遮挡、是否报错；`compare-button-sweeps.mjs` 只列出在新版 Chromium 有效、在旧内核失效的按钮。1.5.10 在 114 上五页全部打不开；1.5.11 和 1.5.12 在 105/114 上 169 个按钮与新版一致（105 上一次「谷地早市」无反应，单独重跑 3/3 正常，属并行负载下的偶发）。

**静态兼容扫描**：用 MDN 兼容数据扫描打包后的 122 个 JS、36 个 CSS。JS 全部可按 ES2022 解析，没有用到 105 之后的 API（迭代器方法、toSorted、groupBy、Set 新方法等均未使用）。CSS 只有 `dvh`（108）缺兜底，已在 6 处前加 `vh`；`text-wrap`、`paint-order`、无前缀 `mask` 只影响外观且已有前缀写法。

**修复与加固**：
- 系统字号：Android WebView 默认按系统字体大小放大网页文字，130% 起还强制可缩放；旧内核上 `text-size-adjust:100%` 挡不住。原生设 `setTextZoom(100)`。桌面浏览器无法复现此放大，属预防性修复。
- 窄屏：系统「显示大小」调大后可小于 320 CSS 像素；280 宽时底栏「图鉴」、右上角设置、地图缩放、寻访帮助都在屏外，无法点击。`index.html` 在屏宽小于 320 时把视口设为 320，由浏览器整体缩放（原生开启 `setUseWideViewPort`，其他屏宽仍是 device-width）。105/114 在 280×653 上 167 个按钮与 320×746 一致，厨房单点 1 只、滑动 24/24。
- 厨房按钮：松手时若坐标换算判定不在按钮内、但浏览器命中仍是同一按钮，照常触发；对齐/偏移/纠正次数进入设备检测。
- 渲染进程被回收或崩溃时（低内存手机）自动重建页面，存档在原生侧不受影响；错误级控制台消息写入 logcat。
- 不支持模块脚本的极旧网页组件显示更新提示（`nomodule`）而不是空白；能力不足的提示写出当前与所需版本，措辞不限定 Google Play。
- 设置 →「设备检测」：游戏版本、手机型号与 Android 版本、网页组件版本和包名、屏幕、系统字号、功能支持、画面对齐、厨房点按对齐、最近 10 条脚本错误（`boot.js` 记录在 `chick-kitchen-diag-v1`，不进存档），可复制（原生剪贴板，旧包退回网页剪贴板）。

**回归**：636/636 Node 测试；Android Java 编译检查通过；`qa-legacy-zoom` 增加 280×653，现 5 个尺寸 × 新旧两种几何在新版 Chromium、以及真实 105/114 上全部单点 1 只、滑动 24/24。可重复入口 `tools/qa-old-webviews.mjs`（内核目录见文件头）。

**发布**：9/9 发布检查（含 28 套浏览器回归、649 项原生存档断言、66 项提醒断言、备份/更新模拟、中文字体覆盖；字体子集补入「纠」）通过。正式包 `artifacts/releases/chick-kitchen-1.5.12.apk`，SHA-256 `9bcd1211fda5b084e2223dc7fa504e49f1a9916be7b5b4401945de1c61883afd`，永久签名不变，可覆盖 1.5.11 并保留进度。从最终 APK 解出的 1471 个文件在真实 Chromium 105/114 上跑离线触屏回归：43 个画面，无脚本错误、无缺图。

**边界**：仍未装到反馈手机，未连接任何真机；系统字号修复未能在桌面复现原问题；厂商后台提醒、文件选择仍需真机。以后换机反馈先请玩家在「设备检测」复制结果。

## 厨房触屏兼容修复（2026-10-04，1.5.11 / code 27）

用户进一步反馈厨房直接点击/滑动收取鸡宝无效。连接华为 NOH-AN00 后确认：Android 12、华为 WebView 114.0.5.302、已安装 1.5.10。先通过只读原生快照保存进度到 `artifacts/device-backups/20261004-223129-baf54645`，再按用户要求执行普通收取；实际收得 1 只、顶部入口收得 9 只，CP 600→610。这只证明这些位置可用，不足以排除其他位置/手势偏移。USB、无线连接随后不稳定，按用户要求停止手机调试，未将修复版回装到该手机，也未卸载、清数据或导入测试进度。

**确认根因**：厨房用 CSS `zoom` 缩放，输入却用 `canvas.getBoundingClientRect()` 换算坐标。旧内核返回的矩形未包含此缩放，指针仍来自实际画面，导致命中位置错开。Chromium 128 [官方变更记录](https://developer.chrome.com/release-notes/128#standardized_css_zoom_property) 说明 zoom 实现及相关 JavaScript API 行为发生变化。本地切换旧缩放行为时，384px 的画面返回 320px 矩形，同一路径只收 3/24；改为 `transform: scale(...)`、以中心缩放后，两种内核返回相同画面矩形，单点与滑动判定一致。不改玩家保存的蛋坐标、配方、奖励或存档格式。

**原生模拟器对照**：已启动独立 Android 15 / Google WebView 124.0.6367.219。测试包使用单独 `com.jibao.kitchen.deviceqa` 身份、CDP 初始化的真实引擎测试存档，以及 Android 原生 `input tap/swipe`；正式包不启用调试入口。相同位置/手势：从 1.5.10 APK 解出的旧资源单点收 0 只、滑动收 3/24、CP 600→603；修复资源单点收 1 只、滑动收 24/24、CP 600→624，无脚本错误。证据分别在 `artifacts/device-qa/emulator124-before/report.json` 和 `emulator124-fixed/report.json`，附 ready/single/swipe 截图。Android 11 / WebView 83 的独立模拟器验证了能力不足时显示更新提示，不能计作游戏通过。

**发布验证**：636/636 Node 测试、649 项原生存档断言、66 项提醒断言、38 项备份/更新模拟、9/9 发布检查、28/28 浏览器套件通过。新增 `qa-legacy-zoom` 检查旧/新模式确实激活，覆盖 320×568、384×712、430×932、560×900 和 Lv.1/Lv.2/Lv.4，共八组；真实触摸单点仅收 1 只、连续滑动收齐 24、奖励恰好增加 24 CP，开始/调料/关闭按钮可用。沿用离线资源门禁，1468 个正式 APK 运行文件与源码逐字节一致，保留 1.5.10 补齐的贴图与永久签名。

正式包 `artifacts/releases/chick-kitchen-1.5.11.apk`，SHA-256 `e3759382f4e1bd00acef39a815f2ab250f9b0f21e336ec40de7ad8cb5defa1e6`。发布证据 `android/build/release-verification.json/.log`、`artifacts/qa/release-ui/report.json`、`artifacts/qa/legacy-zoom/report.json`、`artifacts/releases/chick-kitchen-1.5.11.release.json`。同签名覆盖更新保留进度。

**最终 APK 原生检查**：在上述独立模拟器安装正式签名的 1.5.11，正常从封面进入、确认开火并切换五个主页面；原生导出快照确认 CP 600、24 枚孵化中的蛋。关闭后重新启动，界面仍显示同一锅的 24 枚蛋。截图、界面树及断言报告在 `artifacts/device-qa/emulator124-release-{cover,cook-confirm,kitchen,reloaded}.png` 和 `emulator124-release-smoke.json`；模拟器快照在 `artifacts/device-backups/20261004-232434-fea661c1`。全部操作仅针对模拟器。

**范围**：当前 minSdk 26，即最低 Android 8，网页组件需通过启动能力检测。覆盖品牌和尺寸需依靠回归与实际设备验证，不能承诺全部历史安卓系统。此修复已在本地实际旧 WebView 验证；尚不能声明反馈华为手机全功能、文件选择和厂商后台提醒已逐项通过。下方 1.5.10 为先前首轮记录，其“当时未连接”描述仅适用于该轮。

## 换机安装审查（2026-10-04，1.5.10 / code 26）

用户反馈另一台手机安装后按钮/功能失效、贴图缺失。本轮检查实际 1.5.9 APK、离线打包器、启动能力检测、原生资源响应，以及现有规则、存档、通知、更新和界面回归。修改前源码/美术快照：`artifacts/source-backups/20261004-214944-be5ec69b.zip`。

- **已确认的资源缺陷**：1.5.9 APK 缺少 golden-ui 清单中的 100 张图，包括 30 项手艺图标、五个分支徽章、指引箭头、清洁/设置等图标。界面使用 `kitArt(name)` 和前缀拼接路径，原打包器的字面路径扫描无法发现；源码服务器上却存在这些图片，所以旧界面测试会通过。现从已批准的 golden-ui 清单显式收录全部 125 张切图及清单，缺少源文件时构建直接失败。
- **兼容性缺陷**：旧启动检查只检查 structuredClone、Object.hasOwn、Array.at。Canvas roundRect、dialog.showModal、CSS :has、inert 和指针捕获在进入游戏后才使用，部分 WebView 可能通过启动检查后在绘图、弹窗或输入阶段失败。现把必要能力一起检查；不支持时先提示更新系统网页组件，尚未载入或写入存档。模拟缺失绘图/布局接口的浏览器验证了原始存档字节不变。这是代码中的潜在失效路径，尚不能确认反馈手机使用了哪一个 WebView。
- **资源响应**：原生资源处理补齐 WebP、OGG、WAV 的 MIME，和打包器允许的文件类型一致。
- **新增回归**：`tools/qa-packaged-mobile.mjs` 只读取离线资源目录；源码中有图也不能替打包目录兜底。检查 74 条动态图片路径；320×568、390×844、430×932 下真实触屏点击/手指滑动、新档开火、重新进入保留批次、保存弹窗、商店/仓库/手艺/帮助、五个主页面；还检查五项手艺分支、生意子页、寻访帮助弹窗、图鉴四书签及不支持的网页组件。发布门禁自动重打包再运行此项。

本次完整发布结果：**636/636 Node 测试、649 项原生存档断言、66 项提醒断言、38 项备份/更新模拟、9/9 发布检查、27/27 浏览器套件**均通过，中文字体无缺字。离线触屏回归生成 43 张截图，图片缺失与 JavaScript 错误均为 0。正式 APK 的 1468 个运行文件已与源码逐字节核对，保留永久签名和应用标识。

证据：`android/build/release-verification.json`、`artifacts/qa/release-ui/report.json`、`artifacts/mobile-package-audit/report.json`、`artifacts/mobile-package-before/report.json`、`artifacts/releases/chick-kitchen-1.5.10.release.json`。最终 APK 单独解包的复测结果写入 `artifacts/mobile-final-apk-audit/report.json`。

**验证边界**：没有连接到反馈手机，也没有安装到玩家设备或替换玩家进度；本机 Android 15 模拟器尝试启动后未能进入可连接状态，因此不计作原生设备验证。源码/JVM与浏览器模拟不证明某款手机的系统文件选择、通知权限和厂商后台策略。用新 APK 同签名覆盖安装，保留已有进度；反馈手机型号、系统/WebView 版本和具体失效操作仍待现场核对。

以下为 2026-09-24 的历史审计记录，其版本/数量/环境描述不代表本轮。

日期：2026-09-24。范围：当前 241 候选源码的存档、入口、核心场景、信息页面和扩展系统整合。本轮不增加玩法，不改变配方、价格、解锁条件或品种身份，不向真实手机安装，也不导入或替换真实玩家进度。

## 1. 基线与考古

- 当前工作目录没有 `.git` 元数据，`git status` / `git log` 无法执行。历史文档中有外部影子库及私有仓库接入的说明，但不能据此声称已检查当前目录的提交历史。
- 对照 `web/baseline-20260908`、保留的 classic 实现，以及 `artifacts/source-backups/20260921-214239-7027ae2d.zip` 内的 193 版源码。七个关键文件的原文与 unified diff 保存在 `artifacts/regression-audit/history/`。
- 另对照 20260923-234227 封存快照，确认本轮之前已有一处工程计划文档变化，导致生成来源哈希过期。
- 阅读工程迁移设计、UI 信息架构、实施状态、兼容回滚说明及设备恢复记录。旧文档中“尚未实现”、193 种/schema 3 与实际 241 种/schema 6 有时间差；本报告以实际运行代码及本轮验证为准。
- 修改前规则基线：587/587 通过。这没有覆盖画布实际占用面积和卡片相交，故不能替代视觉回归。

## 2. 发现、根因与修复

| 严重度 | 问题与根因 | 处理与文件 |
|---|---|---|
| 高 | 看不到旧进度：正式浏览器、不同网址/端口、review 演示及独立 Android QA 包使用不同存储空间。当前正式 key 始终为 `chick-kitchen-v1`，没有证据表明本次扩展改名清空了它。 | 保留已有 v1→v6 迁移链，实测旧档；设置说明补充存储位置差异。没有凭空合并档案或自动套用演示档。`save-store.js`、`settings-options.js`；具体边界见第 3 节。 |
| 高 | classic 旧入口仍直接写正式 key，绕开单写锁、事务与备份；旧 `readSave` 失败后返回新档，存在静默覆盖风险。 | 普通 `/classic` 和 `classic.html` 转入正式安全入口；classic 评审保留源码和玩法展示，但只写独立 classic-review key。未来版本档经旧 URL 仍锁定，原始字节不变。`classic.html`、`classic-app.js`。 |
| 高 | 主档损坏后虽可回退 previous，下一次写入会覆盖损坏原文，丢失后续恢复证据。 | 恢复提交前保存 `.unreadable` 原文；该备份失败则不覆盖主档。保留 `.previous`、`.pre-import` 和 `.pre-upgrade-vN` 的既有用途。补充损坏、写入失败、主档缺失三类断言。`save-store.js`、`tests/save-store.test.mjs`。 |
| 高 | 厨房/农场上下空带：外置 HTML 导航后，缩放仍按包含旧底栏的完整画布高度求值，场景又裁掉 66 个逻辑像素。 | 用实际导航高度计算可用空间，并按可见场景高度求缩放；仅裁掉旧底栏，不改蛋坐标、场景资源或命中规则。390×844 原约 78px 上下空带消除。`app.js`、`responsive-shell.css`。 |
| 中 | 底栏图标消失：`externalNavigation` 停止画布导航，但 HTML 渲染仅写入标题。 | 恢复已有图集图标，并补同风格寻访路标；保留名称、选中态、结果圆点和键盘操作。`ui-icons.js`、`app.js`、`responsive-shell.css`。 |
| 高 | 商店仅叫“补给”并挤在厨具翻页处；仓库没有厨房直达入口，设置仅无文字齿轮。 | 厨房右侧固定商店、仓库、手艺、帮助；顶部设置增加明确文字和 44×44 逻辑触区。商店和设置作为辅助页时仍高亮厨房归属。`app.js`、`scene.js`、`theme.js`、`responsive-shell.css`。 |
| 中 | 清洁常驻卡片包含进度条、费用与说明，占 107×50 逻辑像素。 | 缩为 62×36 的扫把与当前脏污百分比；点击原有确认面板查看进度、费用和不足提示。收费、取消、保存失败回滚及防重复扣款保持原规则。`theme.js`、`scene.js`。 |
| 高 | 图鉴行重叠：自动行最小值 128px，而最终样式强制卡片至少 144px，子内容亦有固定高度。 | 行高由内容决定，卡片图片固定容纳区域，标题与库存自动换行；窄屏两列，其他三列。去掉没有内容的填充占位格。`responsive-shell.css`。 |
| 高 | 大字号时图鉴筛选区占据大半页面，卡片被挤进极小滚动条带；短横屏同样难用。 | 图鉴整张纸页滚动，筛选与卡片共用一个滚动容器；其他页保留既有正文和底部操作布局。长标题、0/1/9 卡、已知/未知、hover、150% 字号及 DPR 3 均加入真实浏览器断言。`responsive-shell.css`、`tools/qa-regression-audit.mjs`。 |
| 中 | 扩展页面视觉提示薄弱；营业未开放时只有条件文字，缺少去完成旧采购的直接入口。 | 复用商店与角色素材补营业空状态；增加采购直达按钮、营业/订单/常客/项目页签图形标识及寻访路线插图，保留既有进度、状态与奖励反馈。`business-ui.js`、`regional-ui.js`、`ui-icons.js`、`app.js`、`responsive-shell.css`。 |
| 中 | 快捷入口移出画布后，横屏/桌面可能与顶部设置重合，状态刷新会丢失快捷按钮键盘焦点。 | 按场景真实缩放锚定顶部；横屏有空余时放入场景侧边空位；恢复对应快捷按钮焦点，确认框期间设为 inert。`app.js`、`responsive-shell.css`。 |
| 低 | 浏览器平台仍显示 1.4.8，与当前工程候选不一致。 | 显示 1.5.0 / 241 候选；Android 仍采用桥接返回的真实包版本，没有伪造已发布新版。`native-platform.js`。 |
| 中 | 字体子集缺少“仓”“址”，会回退为不同字体。 | 从项目保留的原字体重生成子集并检查覆盖。`web/fonts/chick-ui.woff2`、`web/fonts/coverage.json`。 |
| 中 | 生成来源哈希过期，完整预检失败。根因是本轮之前工程计划的一行 Git 接入说明变化。 | 重新生成来源指纹；逐项对照证明五个生成输出仅 `sourceHash` 改变，玩法数据与条件未改。`regional-*.generated.js`、`runtime-*.generated.*`、`runtime-content.manifest.json`。 |

以上路径未标目录者均位于 `web/`。本轮沿用已有画风与素材，不删除旧内容解决适配问题。

## 3. 存档兼容与“进度消失”的结论

正式 Web 使用 localStorage；原生 Android 通过 `ChickNative` 的文件持久化仓库加载/提交。当前生产保存链路不使用 IndexedDB。review 与普通入口隔离；Android QA 包和正式包也隔离。浏览器无法跨 origin 自动读取另一端口、另一浏览器或手机原生文件。

现有迁移按版本逐步校验：v1 补第九厨具槽，v2/v3 补齐旧进度结构，v3→v6 增加事务元数据、地区、营业、收藏、常客与项目容器。进行中的旧批次和旧旅程仍使用其原解释器；迁移本身不结算时间、不发奖、不采样随机数。收藏追认继续由独立幂等事务处理。坏档/未来档锁定保存，绝不因版本不一致新建覆盖。

留存的真实玩家备份 `artifacts/device-backups/20260923-234754-dbe52521/before-update.json` 已只读核验：

- 原 schema 3 可迁移为 schema 6；仍为 **Lv.1、2835 CP、56 种**。
- CP、厨房等级、材料、农场、终身收录、厨具、批次和原进度八域逐项相等。
- 原始备份 SHA-256 为 `3d5afd6734c51ab9f613cd52994ca00639aae502de3aeeddf4382e5d4545c507`，读取前后字节不变。
- 没有将该备份或黄金测试档导入真实游戏，也没有覆盖连接设备。证据：`artifacts/regression-audit/player-backup-verification.json`。

**不能据此声称用户当前某个浏览器/手机上的“消失记录”已经现场找回。** 本轮证实兼容链可用并堵住旧入口风险；实际原入口之外的数据仍须从原存储空间或原备份读取。找不到来源时不得把新档、样例档或高等级 QA 档冒充玩家进度。

## 4. 最终导航结构

- 一级：厨房 / 农场 / 生意 / 寻访 / 图鉴。保留已经整合的五入口，避免老玩家再次寻找营业和队伍；桌面变为左侧纵栏，手机为底栏。
- 厨房侧栏：商店、仓库（原收成账本）、手艺、帮助。
- 顶部：设置；其中保留导入、导出、通知与声音。
- 生意二级：营业 / 订单 / 常客 / 项目；图鉴二级：总览 / 品种 / 收藏 / 见闻。
- 清洁：默认扫把 + 脏污百分比，点击展开。仓库不新增一套库存，沿用现有预留、留种和出售逻辑。
- 原神社、活动、三条旧寻访路线、旧采购与手艺仍保留入口。商店补货、订单查看方法等跨页返回继续由既有返回关系处理。

## 5. 验证与复现

全部浏览器测试使用临时独立配置，不读取日常浏览器进度。加速时钟测试不等于真机离线验收。

| 验证 | 本轮结果 / 证据 |
|---|---|
| 规则与保存 | 589/589 通过，含六份黄金旧档、迁移/幂等/事务/库存与新增恢复保护。`artifacts/regression-audit/node-final.txt`。 |
| 原有浏览器回归 | 最终完整预检中 22/22 再次通过，覆盖旧七套及 A–L、图鉴导航、收成分配和兼容回滚。见 `artifacts/qa/release-ui/report.json`。 |
| 本次专项浏览器审计 | 15 组检查通过，0 console error/warning、0 失败资源请求。320/360/390/430/768/844/1280/1920 宽度，短横屏、150% 字号与 DPR 3。`artifacts/regression-audit/browser-report.json`。 |
| 原生存档 | 649 条行为断言通过；JVM 文件与 AOSP JSON 适配，非本轮真机安装证据。 |
| 原生通知 | 60 条行为断言通过；平台模拟，非真实后台通知验收。 |
| 备份与安全更新模拟 | 29 项通过；未连接手机执行更新。 |
| 字体 | 当前所需 2088 个字符全部覆盖，missing 为空。`artifacts/regression-audit/font-check.txt`。 |
| 资源与离线打包 | 663 个运行文件、50.7 MiB；新图标模块被真实入口依赖图纳入。`artifacts/regression-audit/package-final.log`。 |
| 生成物一致性 | 241 身份、83 材料、438 条已编译条件；无玩法语义差异。`artifacts/regression-audit/generated-diff.json`。 |
| 完整预检 | **9/9 全部通过**，结束于 2026-09-24 00:48:34（Asia/Shanghai）。`android/build/release-verification.json` 与 `artifacts/regression-audit/release-final.log`。这是本地工程预检通过，不是正式发布或真机验收完成。 |

可重复运行：

```text
npm start
npm test
npm run test:regression
node tools/verify-ui.mjs --release
powershell -NoProfile -ExecutionPolicy Bypass -File tools/verify-release.ps1
```

浏览器工具需要已有 Playwright；可通过 `CHICK_PLAYWRIGHT_PACKAGE` 指定。沙箱阻止浏览器/测试子进程时，不能把 `spawn EPERM` 算成产品失败或测试通过。本轮在独立浏览器环境实际执行后才记录结果。

截图在 `artifacts/regression-audit/`：`before-*.png` 为修复前；`kitchen-宽x高.png`、`farm-宽x高.png`、`book-宽x高-字号.png` 为最终尺寸验证。早期抓拍的图鉴动画中间帧有半透明效果，不应误认为稳定态渲染；最终截图采用 reduced motion。

## 6. 修改范围

- 应用与场景：`web/app.js`、`web/theme.js`、`web/scene.js`、`web/responsive-shell.css`、新增 `web/ui-icons.js`。
- 辅助及扩展界面：`web/business-ui.js`、`web/regional-ui.js`、`web/settings-options.js`、`web/native-platform.js`。
- 保存安全：`web/save-store.js`、`web/classic.html`、`web/classic-app.js`、`tests/save-store.test.mjs`。
- 验证入口：`package.json`、`tools/qa-regression-audit.mjs`、`tools/audit-capture.mjs`。
- 生成物：字体文件与覆盖报告；五个 runtime 生成输出和内容 manifest；`android/generated-assets/` 为重新打包的派生目录。
- 文档：本报告、根 README 和文档索引。

## 7. 尚未解决与下一阶段建议

1. **112 组扩展最终美术仍未交付。** 本轮补齐界面层的图标、场景提示和空状态，不把概念剪影当成最终角色/发现卡/纪念物美术，也不凭空扩写内容。后续按现有资产清单逐批替换并截图验收。
2. 本轮未在当前真实 Android 设备上安装覆盖包，没有新 APK 发布；系统返回、文件选择器、强退与后台通知仍应在保留真实备份后做一次正式真机验证。
3. 用户所指原记录究竟位于哪个当前入口，不能仅从本地源码确定。已有真实备份可兼容读取；现场恢复须先识别来源，不能自动选“进度最多”的档案覆盖。
4. 旧设计文档的部分“尚未实现”措辞属于历史提案，与当前实现不完全一致；本报告和根 README 明确当前事实。下一轮文档整理应区分历史提案与现行规范，避免顺带改动内容编译合同。
5. 将 `npm run test:regression` 与原有 22 套浏览器检查一并用于 UI 改动验收；重点保留面积、矩形相交、可达性和旧档字节保护断言，不能只截到一张图就判定通过。
