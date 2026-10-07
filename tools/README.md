# 工具脚本索引

所有脚本都从项目根目录运行（`node tools/…`、`python tools/…`）。脚本内部按自身位置推算项目根，所以**不要把它们挪进子目录**；这里只按用途分类。日常入口见 `package.json` 的 `npm` 命令。

## 发布与安装（会被门禁或安装流程调用）

| 脚本 | 用途 |
|---|---|
| `verify-release.ps1` | `npm run verify:release`：9 项发布门禁（测试、内容校验、生成物、字体、UI 套件等） |
| `verify-ui.mjs` | `npm run test:ui`：浏览器 UI 套件调度；`CHICK_QA_ONLY=<名称>` 只跑一套，发布时跑全部 |
| `iterate-android.ps1` / `build-android.ps1` | `npm run build:android`：先快照源码，再构建签名包 |
| `install-android.ps1`、`safe_android_update.py`、`check-update-backup.mjs` | 带备份门的安装与覆盖更新 |
| `check_apk_runtime.py`、`verify-packaged-runtime.py` | APK 内文件与当前源码逐字节核对 |
| `qa-packaged-mobile.mjs` | 直接读取离线打包目录（可用 `CHICK_QA_PAYLOAD` 指定 APK 解出的 assets），检查动态图标、触屏点击/滑动、新档开火与续档、五页入口和不支持的 WebView；不从源码补找缺失文件 |
| `check-android-java.ps1` | 只编译检查 Android Java 源码 |
| `qa-old-webviews.mjs` | 用真实旧版 Chromium（105/108/114，代表旧手机 WebView）跑离线触屏回归和厨房单点/滑动收取；`CHICK_LEGACY_CHROMIUM_ROOT` 指向内核目录，`CHICK_LEGACY_PLAYWRIGHT` 指向 playwright-core 1.34.x，`CHICK_OLD_WEBVIEW_SWEEP=1` 另跑逐按钮对比 |
| `qa-button-sweep.mjs`、`compare-button-sweeps.mjs` | 用两份真实导出存档，在五个主页逐个真实触摸每个可见按钮并记录是否有反应、是否被遮挡、是否报错；不同内核的报告对比后，只列出在参考内核有效、在旧内核失效的按钮。`CHICK_SWEEP_VIEWPORT=280x653` 查窄屏，`CHICK_SWEEP_TAP=0` 只查遮挡 |
| `source_snapshot.py` | `npm run backup:source`：不可变源码/美术快照，写入 `artifacts/source-backups/` |
| `build-compatible-rollback.mjs` | 生成与 schema 兼容的回滚覆盖包（不安装、不部署） |
| `inspect-jibao-reminders.py`、`magic8-ui.py` | 已授权真机检查的辅助工具 |

## 内容与数据生成

| 脚本 | 用途 |
|---|---|
| `build-data.py` | `npm run data`：从原版 XML 生成 `web/data.js`，并把原版 Java 配方分支翻译成 `web/recipes.js` |
| `build-recipe-catalog.py` | 从 `web/recipes.js` 抽取配方册展示数据 `web/recipe-catalog-data.js` |
| `build-runtime-content.mjs`（`--check` 校验漂移）+ `content-*-rules.mjs` | 由 `docs/content-pack/` 作者源编译地区内容、条件与美术索引等 `web/*.generated.*` |
| `build-integration-content.mjs`、`build-save-fixtures.mjs` | 集成数据与历史存档夹具 |
| `build-font.py`（`--check`）、`build-journey-font.py`、`build-golden-display-font.py` | 中文字体子集 |
| `build-portrait-frames.py`、`build-asset-inventory.py`、`production-art-manifest.mjs`、`build-polish-art.mjs` | 美术元数据、原始素材清单与矢量补图 |

## 浏览器 QA 套件

`verify-ui.mjs` 的浏览器套件都在隔离档案里运行，不接触玩家存档。现行门禁：默认 7 套（`qa-skill-art`、`qa-round2`、`qa-tool-strip`、`qa-recipe-book-v14`、`qa-save-ui`、`qa-resume-v146`、`qa-kitchen-care-v145`），发布时再加 `qa-work-a`…`qa-work-l`、`qa-book-navigation`、`qa-takeover-ui`、`qa-compatible-rollback`、`qa-kitchen-golden`、`qa-farm-ui-v1`、`qa-release-final`、`qa-ui-fit`、`qa-packaged-mobile`、`qa-legacy-zoom`，共 28 套。`qa-legacy-zoom` 验证旧、新缩放行为确实启用，并按独立计算的画面位置测试单点和连续触摸。`qa-regression-audit.mjs` 对应 `npm run test:regression`。其余 `qa-*` 是对应版本或视觉轮次的验收脚本，保留以便复现证据。

原生设备审查使用显式序列号：`device-ui.py --serial <序列号>` 读取画面、定位按钮并执行普通点击（会保存正常玩法结果，不清数据）；`prepare-device-harness.py` 在 artifacts 下生成独立 `com.jibao.kitchen.deviceqa` 测试项目，发布源码与正式签名不变，可用 `CHICK_DEVICE_QA_PAYLOAD` 对照旧 APK 解包资源；`qa-device-webview.mjs <序列号> <输出目录>` 只连接此测试副本的 review 页面，通过真实引擎生成隔离测试存档、原生触摸输入和 CDP 读取结果。其测试副本允许重载固定 review URL，正式 Activity 不开放该入口。

## 经济与平衡实验

`simulate-economy*.mjs`、`simulate-investment-pairs.mjs`、`simulate-reachability.mjs`、`report-economy.mjs`、`audit-round2-balance.mjs`、`verify-kitchen-lv4-investment.mjs`、`balance-b-group.mjs`、`verify-b-group-design.mjs`：使用真实规则函数的固定种子实验，只读，不改数值。

## 美术管线与视觉比对（一次性，按轮次保留）

`art_pipeline.py`（通用美术生成管线）、`analyze-legacy-style.py`、`review-character-sheet.py`、`measure-golden-portraits.py`，以及 `slice-*`、`segment-*`、`extract-*`、`compare-*`、`report-journey-precision.py`、`report-kitchen-golden.py`：切图、分割、测量和截图对照，产物写入 `docs/` 或 `artifacts/` 下对应的视觉轮次目录。

## 厨房蛋窝与界面对照（2026-09-30）

| 脚本 | 用途 |
|---|---|
| `build-kitchen-egg-layout.mjs` | 生成 `web/kitchen-egg-nests.js`：四级厨房各 24 枚蛋的自然蛋堆布局（按从后到前排序） |
| `build-kitchen-egg-lips.py` | 从 Lv.1/Lv.2 前沿图切出只从草捆/木条上缘开始遮挡的透明图，只改透明度不改像素 |
| `capture-kitchen-eggs.mjs <目录>` | 隔离存档下截取四级厨房的孵化中、部分孵出、鸭蛋画面，用于前后对照 |
| `capture-ui-review.mjs <存档> <目录>` | 用导出存档的副本在隔离浏览器里截取主要页面 |
| `compose-ui-compare.py <目录>` | 把 before/after 截图拼成带标注的对照图 |

## 其他

`read_source.py`、`inspect_assets.py`：查看反编译源码与原始素材的小工具。`jadx/`、`jadx.zip` 是本机反编译工具（不入库）。`tests/` 是 `npm run test:updates` 的 Python 测试。
