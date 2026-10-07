# 《鸡宝厨房》工程健康与发布基线修复

2026-09-26；对象为本机**当前工作树**。本轮未修改玩法、经济、48 新品定义、存档结构或 UI 设计。结论：内容、规则、更新模拟、完整 9 门/22 套自动发布预检和当前 Android 调试构建已可复验；正式签名与真机发行验收仍未完成。

## 问题、根因与处理

| 原问题 | 定位/类别 | 处理 |
|---|---|---|
| 默认 `npm test` 的 56 文件 `spawn EPERM` | 当前 Work 沙箱限制 Node 子进程；正常进程权限下同一入口通过。不是测试断言失败，也不是项目脚本语义错误。 | 保留默认隔离语义。受限环境正式 fallback：`node --test --test-isolation=none tests/*.test.mjs`；使用 fallback 必须单独标明无隔离。 |
| 内容校验 1593 项中旧 193/现行 241 混用 | 测试断言过期。`GAME_DATA` 当前为 241，旧 193 是冻结前缀。 | 校验器分别断言旧 193 数量/值、当前 241、鸡 152、鸭 89、新 48、旧 75 与当前 83 材料；复用编译器的旧基线逐字段校验。现为 1599 项。 |
| 校验器刷新 `audit.json/md` 后运行时来源哈希漂移 | 生成流程问题。编译器扫描内容包全部 Markdown/JSON/CSV，把审计报告和说明文档当作运行时输入。 | 来源哈希只包含实际编译输入：`content.json`、`baseline.json`、`art-manifest.json`，编译/规则代码和旧内容代码。校验报告与解释性文档不参与运行时哈希，仍保留哈希检查。重新生成运行时文件与 manifest。 |
| `build-runtime-content --check` 漂移 | 上述误分类，加上工作树已有旧来源哈希。 | 按“作者源 → 校验 → 生成 → `--check`”重建；校验器再运行后 `--check` 仍通过。 |
| Python 更新测试的 58 个 WinError 5 | Work 沙箱对 Python `tempfile` 新建目录施加拒绝写入；系统临时目录和工作树下新建临时目录均复现，已有目录可写。正常进程权限下 29 项通过。 | 不修改测试清理或更新逻辑，不伪造受限环境 PASS；在允许临时目录写入的进程权限下执行原命令。 |
| 中文字库缺 4 字（况、叮、嘱、植） | 当前 UI 文案已更新，生成字库子集未同步。发布预检真实失败。 | 用仓库已有 Noto Sans SC 源字体重建 `chick-ui.woff2` 和覆盖报告；`--check` 通过。 |
| 扩展发布 UI 脚本引用旧版寻访/生意控件 | 测试过期。历史脚本仍寻找旧页签、隐藏的关闭按钮和旧生意二级入口。 | 按当前可见控件更新 Work B/C/D/E/F/G/H/L、图鉴导航、接管 UI 与兼容回滚脚本；原业务、库存、迁移和回滚断言保留。整套 22 套通过。 |

## 来源边界与重复性

- **作者源**：`docs/content-pack/content.json`、`baseline.json`、`art-manifest.json`；旧角色及材料事实由 `web/legacy-content.js` 等旧内容代码冻结，编译/规则文件决定转换语义。
- **生成文件**：`web/regional-*.generated.js`、`web/runtime-*.generated.*`、`web/runtime-content.manifest.json`、Android 打包的 `android/generated-assets/`、字体子集。
- **报告**：`docs/content-pack/audit.json` 和 `audit.md` 由 validator 重写。状态、摘要、叙述或未来时间戳不改变运行时内容，不进入来源哈希。说明文档和关系图用于审查，不是编译器读取的运行时输入。
- Runtime `sourceHash` 是排序后的**实际输入文件路径 → SHA-256 原始字节**映射再计算 SHA-256；输入文件变动仍会触发 `--check`。当前值：`6b37ff7287e0799ba7734bfdf19d46f97ca940b9c5864fd402356117024c4681`。校验器重复运行后报告字节与来源哈希均不变。

## 当前命令与结果

| 检查 | 最终结果 |
|---|---|
| `npm test`（正常进程权限） | **PASS**，591/591，0 失败/跳过；Work 沙箱内 **ENVIRONMENT BLOCKED**：56 个文件均 `spawn EPERM`，未进入测试体。 |
| `node --test --test-isolation=none tests/*.test.mjs` | **PASS**，591/591；受限环境 fallback，文件间不隔离。 |
| `node docs/content-pack/validate.mjs` | **PASS**，1599/1599；旧 193 值与当前 241 边界分别检查。 |
| `node tools/build-runtime-content.mjs`，再 `--check`，再 validator，再 `--check` | **PASS**；241 角色、83 材料、438/438 条件编译，0 unavailable；重复运行无漂移。 |
| `node android/tests/build-native-content.mjs --check` | **PASS**，241 身份与原生允许集一致。 |
| `npm run test:updates` | **PASS**，正常进程权限下 29/29；Work 沙箱内 **ENVIRONMENT BLOCKED**，29 测试产生 58 个临时目录权限错误。 |
| `python tools/build-font.py --check` | **PASS**，当前 UI 字符覆盖完整。 |
| `npm run test:ui` | **PASS**，默认 7/7 隔离浏览器套件；扩展 Work B/C/D/E/F/G/H/L、图鉴导航、接管 UI、兼容回滚分别单独 **PASS**。 |
| `npm run test:regression` | **PASS**，旧档迁移、12 组屏幕/字级、导航/面板与浏览器错误检查。 |
| `powershell -NoProfile -ExecutionPolicy Bypass -File tools/verify-release.ps1` | **PASS**，9/9 门，完整 22/22 隔离浏览器套件；未删除或跳过测试。 |

## Android 当前树证据

项目约定的 JDK 17、Android SDK 35、Gradle 8.9、adb 均存在；先前“本机没有工具链”的判断只覆盖了 `PATH`。从当前树运行 `node android/package-runtime.mjs` 后，离线 `:app:assembleDebug` **PASS**；`run-save-tests.ps1` **PASS**（649 断言），`run-notification-tests.ps1` **PASS**（60 断言，JVM 平台替身）。

当前调试包：[app-debug.apk](../android/app/build/outputs/apk/debug/app-debug.apk)，`versionName=1.5.1`、`versionCode=17`、APK SHA-256 `d7f4f597383f91e80f211bed7999c07e0e44dbb37a2ea0328baeb4d5c2887a31`。打包内容哈希 `e8257861b6d0c1cb701fa99c6523f9fd775b0485107ccd55549f8a404046afe3`；`python tools/check_apk_runtime.py android/app/build/outputs/apk/debug/app-debug.apk` **PASS**，942 个包内 Web 文件逐字节匹配当前工作树。**这不是正式签名发布 APK**；`adb devices -l` 无已连接设备，真机安装、保档、通知与离线回访 **ENVIRONMENT BLOCKED**。历史 2026-09-24 APK 不作为本轮证据。

## 当前可信基线与剩余阻塞

规则/内容与生成链、Python 更新模拟、完整 9 门/22 套 UI 发布预检、Android 调试构建可信；`docs/content-pack/audit.*` 是报告，不再污染运行时哈希。当前自动工程基线可用于后续美术/UI 制作。**正式发行验收仍未完成**：正式签名包和真机安装、保档、通知、离线回访未运行；本轮到此停止，不进入 UI 或美术制作。

主要修改：`docs/content-pack/validate.mjs`、`tools/build-runtime-content.mjs`、`tests/runtime-content-compiler.test.mjs`、`tools/qa-work-{b,c,d,e,f,g,h,l}.mjs`、`tools/qa-book-navigation.mjs`、`tools/qa-takeover-ui.mjs`、`tools/qa-compatible-rollback.mjs`、`web/fonts/chick-ui.woff2`、`web/fonts/coverage.json`、运行时生成文件及本报告。玩法完成状态未修改。
