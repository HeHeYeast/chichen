# 工程、存档与安全迭代

核对：2026-09-22。当前版本与设备验证状态只在[当前计划](plan.md)维护。本文用于让开发者在保留真实进度的前提下修改、检查、构建和更新；不是一次发布的通过证明。

## 架构与职责

| 边界 | 职责与入口 |
|---|---|
| 游戏会话 | 应用入口协调页面、Canvas场景、时钟和保存；规则与动画分离 |
| 规则 | 引擎管理批次／购买／清洁／迁移；成长管理手艺与效果；探索、知识、采购分别处理事务 |
| 共享查询 | 品种稳定身份、在家库存、真实候选查询复用；配方册展示数据不能另造随机规则 |
| 内容 | 原始XML与反编译依据生成原版数据／配方；主题包追加ID；Markdown正文与机器表生成集成内容 |
| 显示 | 厨房／农场Canvas，详情和列表HTML/CSS/SVG；共用主题和命中区域，动画不发奖励 |
| 平台 | 浏览器本地存档；Android Java WebView离线资源、私有文件、系统文件选择、通知与生命周期 |
| 预览与发布 | 本地服务、隔离评审页、规则／浏览器／JVM／更新测试、离线APK打包和安全覆盖 |

源码定位可从[Web目录](../web)、[Android源码](../android/app/src/main/java/com/jibao/kitchen)、[测试](../tests)、[工具](../tools)进入。无第三方前端框架。2026-09-24接入私有Git版本库，接入之前的开发过程仍以历史文档与本地快照为依据。原始assets／res／dex、SDK解包内容与反编译结果用于来源核对，不作为全部运行文件打包。

## 版本管理

版本库为 [HeHeYeast/chichen](https://github.com/HeHeYeast/chichen)，保持 **Private**；远端名为 `origin`，主分支为 `main`。首次导入保留远端已有提交，并记录当前开发基线，不表示此版本已通过正式发布验收。

日常修改前先检查 `git status`；有本地修改时先提交或妥善保存，再执行 `git pull --ff-only`。完成一项改动后运行相关验证，检查 `git diff` 和暂存文件清单，再提交并推送。较大的功能可使用独立分支，主分支保留可追溯的阶段成果。

`.gitignore` 排除 `.signing/`、真实设备备份、APK、源码备份ZIP、数据库、下载工具、构建缓存及新增产物。已有设备测试脚本、原素材、必要字体源、反编译依据与历史证据继续跟踪；新证据需显式选择入库。2026-10-09 将 67 个未跟踪的旧轮次目录压缩为本机 `artifacts/local-archive/historical-rounds-20261009.zip`，逐文件核对 SHA-256 后清理散落原件；文档中对应的历史路径需从归档按原路径恢复，或重新生成。详见[产物保留规则](../artifacts/README.md)。

Git保存源码版本，不能替代签名密钥和玩家存档的独立备份。换电脑时需另行安全迁移 `.signing/` 并准备工具链；回退代码不等于降级玩家存档。源代码快照与安全覆盖安装流程继续保留。

## 内容生成与固定路径契约

| 输入／生成器 | 输出及约束 |
|---|---|
| 原始XML、反编译配方；build-data.py | data与recipes生成文件，不手改生成结果 |
| build-recipe-catalog.py | 展示目录和来源摘要；候选查询仍与真实执行规则共源 |
| species-descriptions.md、species-discovery-audit.md、worldbuilding-kitchen-story.md；build-integration-content.mjs | 193描述／193线索／三章正文及能力、基础配置；保留Markdown表格列与CH-01～03章节格式 |
| b-group-balance中的design-config.json、exploration-species.json | 仍是生成器输入；基础路线、订单、点数与能力保留，旧skillCosts等字段不代表当前30手艺算法 |
| 当前skill-data、trade-data、progression、exploration | 1.5手艺／分类／计算的执行来源；新增知识不能只修改旧B模型JSON |
| game-design.md的30项手艺表；artifacts/round2-implementation/generate-skill-data.py | 表格仍可生成相同skill-data；ID／成本／文案列是生成接口，改格式前同步工具；本次仅修工具输入路径，未执行生成 |
| build-asset-inventory.py | 原素材CSV／JSON，保持机器输出位置 |

旧B组模型有来源哈希及固定路径校验：`worldbuilding-and-expansion-brief.md`保留为兼容入口，`b-group-gameplay-design.md`保留为历史模型说明；不得据其旧“尚未实现”恢复待办。`node tools/verify-b-group-design.mjs --integrated`核查历史模型并记录源码差异，不证明当前数值已经平衡。历史模拟不要直接重跑覆盖已留存证据。

修改内容后才按需重生成，核对稳定身份、正文、线索与章节；本次整理仅修状态、规范与引用，不重生成、不修改游戏代码。内容源新增规范不能混入章节正文解析区域。artifacts下两份历史评审辅助脚本的文档路径已同步；它们不是日常发布命令，不要为了阅读文档重跑生成历史图册。

## 存档与事务不变量

Web 已增加可关闭的账号与手动云备份层，普通游客原键保留，账号按服务端 UID 分开存储；注册只复制游客档，登录不自动覆盖，云恢复先保存本机副本。经济命令仍使用原 `createSaveStore`。云写入以 epoch＋云 revision 条件提交，拒绝静默覆盖，不按设备时间合并奖励。代码与本机联调完成，线上尚未配置；接口、迁移、失败恢复和验证边界见 [实施报告](platform/cloud-wechat-implementation.md) 与 [服务说明](../cloud/README.md)。Android 暂未接入账号层；小游戏仅有隔离兼容实验。

schema 3兼容v1/v2，旧批次结果和截止时刻保留；旧技能只退点一次，知识／采购／占用／票据／奖励保留。JS严格游戏校验、Java原生结构校验、备份工具和共享fixture同步维护，不能假定Java与JS全字段等价。

原生保存使用原子文件、校验摘要、上一份有效档和导入前恢复点。坏档或未来版本进入恢复流程并暂停自动写入，不用新档覆盖。导入是整档替换，不合并两台进度；确认前展示摘要，取消不变更。

保存失败整笔回滚内存，成功后才展示动画；派队／归队／领奖和库存事务不可拆。批次、清洁与寻访按启动快照，不追溯重抽；绝对时间、逻辑时间和日历分别处理。系统时钟前跳只完成当前旅程，不自动产生多日收益。离线备份可整体回滚世界，不承诺服务端防作弊。

## 玩家安装、迁移与提醒

手机可打开已验证的APK安装；更新直接覆盖，不卸载或清数据。浏览器与手机无云同步，需在来源设备设置导出最新JSON，传到目标设备后核对厨房、CP、发现与批次再确认导入，保留目标旧档。评审进度不得覆盖真实存档。

设置可启用孵化提醒、查看权限及预计时间、发送即时测试或独立1分钟后台测试，并查看荣耀后台帮助。提醒按剩余蛋最晚破壳＋3秒，整批一次；换批／收完／关闭取消，开机／更新／时间或授权变化后恢复。通知点击通过就绪握手进厨房，不结算奖励。网页关闭不能替代原生提醒；测试通知送达不证明重启或长期省电路径。

## Android构建

离线游戏的固定应用标识为 `com.jibao.kitchen`。Android 8.0（API 26）及以上可以安装；当前编译与目标版本为 API 35。原生入口为 `MainActivity`，运行资源从 APK 内加载，无须联网权限。

## 制作一个版本

在项目根目录运行：

```powershell
# 只检查并整理实际运行资源，不编译、不创建签名。
./tools/build-android.ps1 -PrepareOnly

# 编译、签名和校验候选包，供验证用；不产生最终发布记录。
./tools/build-android.ps1 -CandidateOnly

# 使用已经安装的工具链和依赖缓存离线构建正式签名包。
./tools/build-android.ps1
```

默认工具链在 `D:\gxy_code\_toolchain`，包含 JDK 17、Gradle 8.9、Android SDK 35 和 AGP 8.7.3 缓存。可以用脚本参数指定其他路径。已有缓存目录位于项目外时，受限工作环境需要允许 Gradle 写入自身缓存；不要把“锁文件被拒绝”误判为工具链损坏。只有明确需要下载缺失依赖时才传入 `-AllowNetwork`。

打包器从 `web/index.html` 追踪实际脚本与样式依赖，收录当前注册的美术素材、字体、原版 PNG 和音乐。评审页、旧基线、素材备选稿、原 APK 的 dex/二进制资源和开发工具均不打入 APK。`android/generated-assets/runtime-manifest.json` 记录每个源文件的校验值，APK 发布前核对实际资源清单。

脚本执行全新 release 构建，再核对签名、应用标识、版本和资源清单。仅成功时才产生：

- `artifacts/releases/chick-kitchen-<版本>.apk`
- 同名 `.sha256` 校验文件
- 同名 `.release.json` 发布记录，包含 APK 校验值、签名证书指纹、时间和说明

失败不会把上次 APK 当成新版本复制。已发布版本不可覆盖；下一次发布应同时增加 `android/release.json` 中的 `versionCode` 和 `versionName`，并填写实际更新说明。

正式构建与 `-CandidateOnly` 都强制先运行 `tools/verify-release.ps1`：runtime生成物、Native生成物、资源声明、Node规则测试、中文字体、原生存档、原生通知、Python更新/备份模拟、浏览器UI九项门禁（当前浏览器 28 套，包括旧七套、Work A–L、图鉴/收成与兼容回滚、厨房/农场/发布布局、离线触屏及旧/新内核缩放行为回归）。任何失败都停止签名编译和发布。UI检查自行启动并关闭独立本地服务；需已有Playwright和浏览器，可用环境变量 `CHICK_PLAYWRIGHT_PACKAGE` 指定包路径、`CHICK_QA_CHROME` 指定浏览器。当前机器自动发现已有GSD附带的Playwright。离线触屏回归前自动重打包，直接服务 `android/generated-assets`，不从源码补找缺失图片。`-PrepareOnly` 仅整理资源；结果记录于 `android/build/release-verification.json` 和 `.log`。打包后逐文件校验APK、清单及源码字节一致。

## 保持存档与更新身份

首次正式构建生成项目专用签名，私钥和随机密码存放在 `.signing/`，不会写入 APK 或输出日志。请将整个 `.signing/` 目录另行保存在安全位置。`android/release-identity.json` 仅记录公钥指纹，应与源码一起保留。

后续所有版本必须复用同一私钥和应用标识。已有身份但私钥丢失时，构建会停止，不会悄悄生成新钥匙。若仅换一把钥匙，同一个应用将无法覆盖更新。

应用的原生存档保存在自己的私有目录中，同签名覆盖安装保留该目录。应用内的存档导出与导入可用于额外备份或换机。本项目不依赖系统云备份；卸载应用或清除应用数据会删除本机私有存档，更新时应直接安装新 APK。

## 正常开发

1. 修改前 `npm run backup:source`，生成唯一文件名的源码/素材ZIP并逐文件SHA256验证。包含Web、Android源文件、测试、文档、原素材、字体源和原版生成依据；排除构建缓存、设备存档和`.signing`。
2. 修改代码；更新 `android/release.json` 的版本和说明。已发布版本不可覆盖，下一次正式版本按实际发布记录递增。
3. `npm run build:android`：再保存源码快照 → Node/字体/JVM/更新模拟/真实浏览器门禁 → 离线编译签名 → 校验包名/版本/签名及每个资源字节 → 写不可覆盖的正式APK和发布记录。
4. 手机连接后运行 `./tools/install-android.ps1 -Launch`，或双击根目录“一键安全更新.cmd”。只使用现成APK时无需重复构建。多设备用 `-Serial` 明确指定。
5. 也可使用 `./tools/iterate-android.ps1 -Install` 一次完成新版本的源码快照、门禁、构建和安全更新；没设备会在设备阶段停止，已经构建的包保留。

门禁也可单独运行 `npm run verify:release`。浏览器回归自动开启随机本地端口并清理服务，不依赖AI手动打开网页；只用隔离浏览器存档。可通过 `CHICK_PLAYWRIGHT_PACKAGE` 和 `CHICK_QA_CHROME` 指定已有工具。本机具备JDK17、离线Gradle/SDK缓存、Python字体工具和Playwright；换电脑需先准备这些依赖，不能跳过门禁。

## 首次接入与后续更新

1.4.6及更旧：在游戏设置导出一次最新JSON，将文件保存到电脑。十分钟内执行 `./tools/install-android.ps1 -LegacyBackup <路径> -Launch`；导出后暂停操作游戏，勿继续孵化收取/买卖。旧版无自动接口，脚本不能证明手工文件来自当前这台手机或导出后没有新进度；这是首次接入的明确限制。脚本验证文件结构、游戏版本和时间，复制留存后才覆盖安装；更新后逐项比较，允许导出返回导致的lastSeen变化。

1.4.7及后续：授权USB调试 → 检查设备/用户空间/版本 → 暂停应用 → 只读Provider快照 → 本地落盘/校验 → 再取快照检查进度未变 → 同包名同签名覆盖 → 比较更新前后原始存档摘要和状态 → `-Launch`启动及进程检查。任何异常都保留日志和已经保存的快照，不尝试自动恢复。

每次报告与JSON快照在 `artifacts/device-backups/<时间-随机标识>/`。其中`installed`、`saveVerified`、`nativeLaunchVerified`、`webUiVerified`含义独立；真实画面无法由进程存在证明。Provider、覆盖与存档比较已有历史设备证据；最新安装和画面范围见当前计划，不把历史记录当实时设备状态。

独立备份：`./tools/install-android.ps1 -BackupOnly`，不需要本地APK。旧版本仍需上面的手工导出。只读预检：`./tools/install-android.ps1 -CheckOnly`，不关闭游戏、不安装。

## 回滚与三类备份

| 类别 | 位置与作用 | 恢复规则 |
|---|---|---|
| 源码/素材 | `artifacts/source-backups/*.zip`，清单内含每个文件摘要 | 用 `python tools/source_snapshot.py --verify <zip>` 验证后解压到新目录，再人工核对差异；不覆盖当前工作区 |
| 玩家存档 | 游戏内导出或 `artifacts/device-backups/` | 游戏设置中由玩家明确确认导入；有新进度时绝不自动恢复旧档 |
| 签名密钥 | `.signing/`，不进入源码ZIP和发布日志 | 单独复制到用户控制的安全离线位置；丢失后不能靠生成新密钥保持覆盖身份 |

已有正式APK和SHA256保留作为历史证据，不能把“装回低版本”当成安全回滚。发现问题优先用原签名构建更高versionCode的修复版。源码和存档同盘备份有助于回退，但不能防硬盘损坏；本轮没有创建外部/异地备份。

## 验证边界与环境

`npm test`为规则与界面契约；`npm run test:ui`自动启动隔离服务；`npm run test:updates`为备份／更新模拟；`npm run verify:release`包含runtime/Native生成物、资源声明、规则、字体、原生存档、原生通知、更新模拟、真实浏览器九类门禁。实际浏览器组数以脚本为准，不能从旧文档复制数量。JVM适配器不是设备系统测试；安装成功、nativeLaunchVerified、webUiVerified、saveVerified分别判断。

Windows已识别ADB Interface却无设备时，可核查platform-tools后端；本机历史使用ADB_LIBUSB=1后恢复识别。不能以清数据解决驱动问题。发布脚本按UTF-8读中文JSON，原生命令stderr可能只是正常进度，应按退出码判失败，不降低门禁。

iOS尚未实现，异步持久化／跨平台事务、签名与设备条件见[iOS专项方案](platform/ios-port-plan.md)。不把方案内新增文件名当已存在接口。


## 变更验收契约

内容变更应核对193身份、名称、已有22项首句、日期和特殊获取，不把文学造型变成新配件或效果。料理／采购／寻访须检查取消、连点、坏输入、保存失败、旧报价与批次切换；覆盖在家和在途守恒、归队与分次领奖幂等。内容生成后的文案长度、中文字体及实际条件需检查。

真实旧档至少含普通进度、活跃批次、在途、归队待领、部分采购和重配冷却；迁移前后逐项比较CP、库存、知识、批次结果和时间，退点仅一次。完整发布不使用单组UI调试筛选；图片／mockup不能代替实现。需要操作手机时使用当时新导出的真实档，不能把历史备份当最新状态。

原始内容来源包括496图片、21音频及反编译生成依据；不全部进入APK。源码／素材快照、真实存档和签名私钥分开保全；未建立异地备份时不能声称已有灾备。历史文档的全部源码恢复位置应为新目录，不覆盖正在工作的目录或手机。
