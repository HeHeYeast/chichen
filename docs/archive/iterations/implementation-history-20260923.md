# 历史实施记录（不可作为当前验收结论）

> 历史记录：保留当时的决策与验收证据；文中的“当前／待确认／未实现”仅指记录当时。现在的状态以[当前计划](../../plan.md)为准。

以下保存接管前后的阶段记录和旧测试数字。当前状态、修复与发布边界以 implementation-status.md / release-acceptance-report.md 为准；旧“已完成”、22/48低频结论、6项发布门禁和未构建APK描述均已被后续证据取代。

# 本轮工程实施状态

更新：2026-09-23（接管后）。唯一完成依据是 engineering-implementation-plan.md 的各阶段质量门。本目录无Git；另用项目外只读影子库（`%TEMP%/claude/.../shadow.git`，`--work-tree` 指向本目录）记录每个Work的差异，不改动项目文件。

| Work | 状态 | 验证与证据 |
|---|---|---|
| A | 已完成 | Node316/316；黄金17/17；正式UI8条；Java保存168/通知60；Android35 Java编译；源码快照20260923-074000-8e3954a0.zip |
| B | 已完成 | Node400/400；地区批次38；正式UI完整5类/11截图；Java保存198/通知60；待附阶段快照 |
| C | 已完成 | Node414/414；谷地12种/6卡/2材料/ALT-V；C浏览器验收8项；旧UI7套；Java保存213/通知60；Android Java编译；快照20260923-091740-24dd3fea.zip |
| D | 已完成 | Node420/420；D浏览器验收8项（含真离线26h、收摊后重配）；B/C/旧UI回归通过；Java保存226/通知60；MN1三条件编译 |
| E | 已完成 | Node437/437；E浏览器验收7项；B/C/D/旧UI回归；Java保存234/通知60；8菜单开放（MN5/MN7按地区/引路锁定）；O01/O04开放 |
| F | 已完成 | Node447/447；溪岸+茶坡24种/12卡/4材料/ALT-T；F浏览器验收4项；B–E与旧UI回归；Java保存240；O02/O03/O05/O06(V,R,T)/O07–O10/O12开放 |
| G | 已完成 | Node456/456；海湾12种/6卡/2材料/ALT-B、GUIDE-B、带货；G浏览器验收3项；B–F与旧UI回归；Java保存251；241身份与24卡合法可达测试 |
| H | 已完成 | Node465/465；schema6收藏册/成果账本/陈列；H浏览器验收3项；B–G与旧UI回归；Java保存256/通知60；快照20260923-104009-35a4c4fd |
| I | 已完成 | Node479/479；四常客16段、来客呈现与手动分支、M09–M12；I浏览器验收2项；B–H与旧UI回归；Java保存266/通知60；快照20260923-110343-a1bbb667 |
| J | 已完成 | Node489/489；PJ1–4阶段交付/付款、ALT-R、菜单预设、展册画像；J浏览器验收4项；B–I与旧UI回归；Java保存279/通知60；快照20260923-112201-f33dec0b |
| K | 已完成 | Node492/492；五入口底栏、页面归属与返回路径、桌面双栏、字体重建0缺字、438/438条件编译；K浏览器验收9项；B–J与旧UI回归；快照20260923-120136-7381dd01 |
| L | 功能门禁完成；发布未全部通过 | Node494/494；发布预检6/6；QA B–L 11/11；可达性8种子全过；14天经济模拟；真实2h窗口通过；打包修复；阻断：最终美术/价格签收/签名APK与真机（见release-acceptance-report） |

## 最新接管复核（2026-09-23 21:35，覆盖上表历史结论）

接管附件要求以实际磁盘和新证据为准。上表保留历史阶段交付记录，不再作为当前完成证明。

| Work | 当前状态 | 本轮证据 / 剩余门槛 |
|---|---|---|
| A | PARTIAL | 全量494/494、黄金与备份19/19；Native发现13种坏档误收，跨端校验修复中 |
| B | DONE | B浏览器重跑通过；保护/供货/知识门基线通过 |
| C | DONE | C浏览器重跑通过；地区/BatchPlan/投影/收藏94/94 |
| D | PARTIAL | D浏览器通过；Native营业快照校验待补齐 |
| E | PARTIAL | E浏览器通过；采购冻结地区及Native语义校验待补齐 |
| F | DONE | F浏览器重跑通过，24项区域定义接入保持 |
| G | PARTIAL | G旧UI通过；新反例发现带货缺队伍门槛、GUIDE首标本门槛不精确，已重开修复 |
| H | PARTIAL | H浏览器通过；Native事实/成果嵌套校验待补齐 |
| I | PARTIAL | I旧UI通过；6只成交无合格来客即可读故事、普通用料误作地区试做；补齐32替代分支验证 |
| J | PARTIAL | J浏览器通过；Native分数付款/非法锁定品种反例待修复 |
| K | PARTIAL | 旧QA通过但放宽横屏触控门槛；缺独立信息壳、四图鉴标签/搜索和统一收成分配。按冻结UI补齐 |
| L | PARTIAL | A–L浏览器全重跑通过，但旧断言不足；经济模拟策略/归因待修正。最终美术、真机与真人理解未验收 |

- 新鲜Node基线：494通过/0失败，artifacts/takeover/node-baseline.txt。
- runtime：241品种/83材料/438条件编译/0未实现；作者hash f5a0b0683380ee8b62afc09652f9d6f71d658f688c5324d41a3bc6923095f490。
- 新鲜浏览器A–L全部通过，日志 artifacts/takeover/browser-a.txt 至 browser-l.txt；不能覆盖上述新发现反例。
- 原生只读核验：Java279、通知60、8源码编译、Python29、657打包资源；41合法跨端通过，但13坏档误收，证据 artifacts/takeover/native-audit.md。
- 实际根目录无.git；外部影子库最新c510124（Work L）。本轮起始源码回滚快照 artifacts/source-backups/20260923-213545-b2f73201.zip，已逐文件验证。
- 当前单一集成：根负责app/BatchPlan/facts/Web校验与响应式外壳；native_save负责SaveRepository与原生测试；runtime_compiler负责regulars/orders及反例；design_reader负责regional-exploration及图鉴UI。共享热区不并写。
- 没有改变旧193/75材料身份、价格、旧奖励或真实存档。旧“12h仅22道安全”结论尚缺正确成熟时间及手艺对照，撤回其产品阻断地位，重做证据后判断。

## 接管核验（2026-09-23）

- 接管时代码状态：A/B按记录完成；C已部分进入代码（REGIONAL_RELEASE已放宽到整个谷地，但条件编译、卡门槛、UI、Java仍是B专用）；D领域模块与schema5已集成到根（freshState直接产出v5）。
- 基线：`node --test --experimental-test-isolation=none tests/*.test.mjs` 397/400。3项失败均为B专用发布断言（被C的放宽打破），非功能回归；B浏览器验收 `node tools/qa-work-b.mjs` 通过。
- 接管发现并修复的A/B缺陷（均不改变玩法或旧档经济）：
  1. **Android拒存所有新档**：Web根已产出schema5，SaveRepository只认≤4且迁移记录固定1条→Web每次提交在Java端被拒。修复：Java `CURRENT_VERSION=5`，迁移记录按版本逐步校验，新增 `validateBusiness`（营业容器/当前单/T-R-S占用/账单收入守恒/事实容器），未来版本改为6。影响：D需在此基础上扩展订单/营业细节。
  2. **地区采样出发永远无法保存**（B功能）：Web寻访篮校验只放行首标本材料，未放行冻结的采样材料。修复：progress-save同时认可 `regional.sampling.materialId`。
  3. **地区准备模式不随开火消费**：startBatch清除四时/复刻准备但未清 `expansion.prepareMode`，下一锅普通调理被判“与地区做法不一致”。修复：开火后删除准备模式。
  4. **保存失败提示退化**：A把操作统一到execute后，写入失败直接显示原始异常，旧UI门禁“暂未保存”失败。修复：未写入的存储失败以 `SAVE_FAILED` 抛出并带“进度暂未保存，本次操作未生效”。
  5. 旧门禁 qa-save-ui 断言版本4，改为读取 `CURRENT_SAVE_VERSION`。
- 以上均为明确缺陷或测试陈旧，不涉及架构替换。

## 已完成与当前分工

- 完整读取工程设计与实施计划。旧基线测试重新执行，256通过、0失败。
- 根集成人独占 app、engine、inventory、world-clock、progress-save、runtime registry 与Web schema。
- runtime_compiler：独立生成器、新生成文件、legacy193快照、编译测试。
- native_save：Android SaveRepository、MainActivity与原生保存测试。
- design_reader：正式玩法/UI/内容只读核对与实施索引。
- 本目录无Git元数据，采用 tools/source_snapshot.py 的源码快照，禁止覆盖真实玩家档或安装APK。

## 风险、偏差与阻塞

- 设计冲突：未发现不可调和的冻结设计矛盾。
- 待产品决定（L发现，未改数值）：间隔约12小时回访的玩家只能用保温灯、烤箱Lv.1、炖锅避免焦化（旧保鲜规则），48道地区做法中仅22道可用；这类玩家地区进度与高价出品受限（发布报告第3节）。
- 发布阻断：最终美术未交付、候选价格未签收、签名APK与真机未测（发布报告第5节）。
- 当前内容包是作者资产，不能将其静态审计计作可玩功能。
- 正式48角色美术未确认交付，功能接入可使用明确标记概念剪影；发行美术验收不可由剪影替代。
- A完成前不开新玩法。后续阶段不默认满足未编译条件。

## 验证命令

`node --test --experimental-test-isolation=none tests/*.test.mjs`（本环境标准Node测试隔离可能受子进程限制）。

完整门禁（Work L 起）：
- `node tools/build-runtime-content.mjs --check`、`node android/tests/build-native-content.mjs --check`（生成物无漂移；438/438条件已编译）
- `powershell -ExecutionPolicy Bypass -File tools/verify-release.ps1`（6项：Node、字体覆盖、原生存档、原生通知、Python备份/更新模拟、旧浏览器回归7套）
- `node tools/qa-work-b.mjs` … `node tools/qa-work-l.mjs`（各Work浏览器验收，加速时钟隔离档）；`node tools/qa-real-window.mjs`（真实时钟2小时，约2小时5分）
- `powershell -ExecutionPolicy Bypass -File tools/check-android-java.ps1`；`node android/package-runtime.mjs`（仅打包运行资源，不签名/不安装）
- `node tools/simulate-reachability.mjs <seed>`、`node tools/simulate-economy.mjs`（模拟报告写入 artifacts/sim）
- 发布报告：docs/archive/iterations/release-acceptance-report.md

## 回滚

阶段前创建只读源码快照，阶段通过后再留新快照与文件哈希。已升级存档只使用兼容schema且关闭新入口的构建；不执行存档降级或自动倒档。

## Work A 证据与回滚点

- 源码基线：artifacts/source-backups/20260923-071758-d3a16802.zip，2229文件哈希校验成功。
- A完成快照：artifacts/source-backups/20260923-074000-8e3954a0.zip。
- Node：artifacts/work-a-integration-tests.txt（316通过）；原生与正式UI：artifacts/qa/work-a/report.json、native-evidence.json；旧七套UI：artifacts/qa/release-ui/report.json。
- runtime sourceHash：d0771d2022bc42b22b4b391b3571cc6a5b8ec45d14c8705b5265a5112bbf93a5。条件438条未开放，按后续Work逐步编译，A未声称地区已可玩。
- schema4，旧batch1/2和trip1原票据保留；新身份可读/出售，所有新生产入口关闭。
- 修复同版缺skillVersion被错误退款迁移、Native首次命令revision1拒绝、保存成功通知失败误报。
- Web真实锁；未知ACK停写并回读；升级前字节备份独立于previous。
- 回退源码不可降级玩家状态；使用A的schema4兼容快照关闭新操作，保留新身份。无Git元数据，未伪造commit。

## Work B 证据与回滚点

- 仅开放V-S1、75、REC-V-C1。schema4，trip2/batch3，旧trip1/batch1/2仍原解释器结清。
- artifacts/work-b-tests.txt：400/400（包含D尚未开放的独立模块测试）；地区与BatchPlan38项覆盖25%边界、四批保护、变异owed、免费三趟、全锅材料使用与坏档。
- artifacts/qa/work-b/report.json：真实按钮闭环，满包首标本→辨认36→领料→三趟补全→前三批失败/第四批C129→出售售空→1+23复刻；11截图、320/390/430/1280检查、errors=[]。加速时钟，与真实等待区分。
- Native 198保存断言、60通知；12个真实JS地区状态跨端字节roundtrip，全Java编译；native-evidence.json记录哈希。
- 编译7条正式条件、431条未开放；当前sourceHash见runtime-content.manifest.json。
- 修复计时保存重置factSeq导致归队拒存；未知材料遮罩；普通新料整批收取记见闻；经济与视觉随机分离；目标之外新品、空随机票据、配方不符拒读。
- 兼容回滚：停止REGIONAL_RELEASE新操作，仍保留241/83注册表、trip2/batch3解释器、材料容量、已得知识/owed/真实同行快照。不会降级玩家档。

## Work C 证据与回滚点

- 状态：已完成（Web/Node/浏览器/Java保存与编译）。真机未安装。
- 开放：`RELEASED_REGIONS=['V']`，由地区派生 6卡、75/76、12方法、ALT-V；ALT-R等项目解锁的替代法由 `RELEASED_PROJECT_ALTERNATIVES` 控制（J再开）。
- 修改模块：tools/content-regional-rules.mjs（按身份表驱动、每Work只加本区行）、content-runtime-rules/build-runtime-content（条件编译接收runtime投影；覆盖统计把可求值树计为compiled）、region-model（发布由地区派生；删除V-N2/V-E2硬编码；卡门槛经requirements.evaluate）、regional-exploration（入门标本按可执行选择；候选顺序出发时冻结，入门在首，其余独立频道等权洗牌）、regional-methods（旧料供货复用ingredient-unlocks；ALT替换按位置映射，保留未替换旧料）、candidate-query（替代模式预览）、engine（开火消费准备模式）、progression/progress-save（发现来源至240、上限64）、save-migrations（准备模式认可local-alternative）、regional-ui（数据驱动：卡/材料/12方法/ALT/保护/未知遮罩）、app（开火确认模式说明）、SaveRepository（注册表驱动的trip2/batch3/准备模式校验）、build-native-content（加入regions/materials）。
- 新增模块：web/region-view.js（地区只读投影与未知规则）、web/batch-mode-view.js（批次模式说明）、tests/regional-valley.test.mjs、tests/region-view.test.mjs、tools/qa-work-c.mjs、tools/check-android-java.ps1。
- schema/rulesVersion：无根版本变化（仍schema5）；runtime rulesVersion 1；条件编译 7→44 条作者条件 + 14 条派生（12配方门槛+2供货）。sourceHash b3a1d60bd97197c5291637193ae96fe00d41610f2973e944a82560fae199afc9。
- 测试命令与结果：
  - `node --test --experimental-test-isolation=none tests/*.test.mjs` → 414/414（artifacts/work-c-tests.txt）。新增覆盖：V-S2入池门槛、V-N2/V-E2需辨认麦芽、V-E1叶形+菜园同一伙伴、特征加成仅一次、入门标本按可执行选择、4趟保护跨地点累计与空池冻结、V-C5/V-D5首标本方向、V-C6/V-D6观赏卡门槛、V-C4/V-D4厨房Lv.4与面包机/奶油供货、V-C3蒸笼地区模式无旧保证插入且普通蒸笼保证仍在、ALT-V与旧烤箱面粉抽取逐位相同且不插目标、礼物/四时不叠加、观赏品可同行不可营业、来源240/64点、12方法合法前置全部可达、准备模式随批次消费、确认页模式说明、未知投影不泄露。
  - `node tools/qa-work-b.mjs` 通过（新UI下B回归）。`node tools/qa-work-c.mjs` 通过8项（artifacts/qa/work-c/report.json，含320/390/1280截图）。
  - `node tools/verify-ui.mjs` 旧7套UI通过。
  - `android/tests/run-save-tests.ps1` 213断言（新增valley真实命令轨迹6态+9项反例）；`run-notification-tests.ps1` 60；`tools/check-android-java.ps1` 编译通过。
  - `node tools/build-runtime-content.mjs --check`、`node android/tests/build-native-content.mjs --check` 无漂移。
- 可玩验收（加速时钟、隔离档，非真机）：中期旧档→首标本保证→辨认荠菜→麦芽标本2趟→谷棚见闻与谷物事件→ALT-V烤箱开火（旧身份、无新增保底）→C129按方向3批内实收→寻访补全V-C3后蒸笼地区模式只见旧加权伴随、改回普通面粉蒸笼仍显示烧麦鸡“已安排1只”→V-C6实收后可同行、营业备货不出现。“后期回访做12种全收”以单元可达性测试（合法前置下12方法全部满足）证明，浏览器未逐一实收12种。
- 已知问题：48种正式美术未交付，仍用标注“概念剪影”的同一SVG；地区页较长，K统一布局；工坊入口文案仍写“谷地寻味”（仅V开放），K改为寻访一级入口。
- 回滚：关闭 `RELEASED_REGIONS` 中的V即停止新出发/新目标；已有票据由注册表驱动的校验与结算继续认可（测试断言校验不依赖发布表），12种库存/知识/保护/容量保留，不删注册表条目。源码快照 artifacts/source-backups/20260923-091740-24dd3fea.zip。
- 关键接口：`regionView(s,regionId)`、`batchModeView(s,plan)`、`regionalCardGateMet(s,card)`、`CARD_GATES`/`COMPILED_REGIONS`（F/G只加行）、`RELEASED_REGIONS`。

## Work D 证据与回滚点

- 状态：已完成（旧库存MN1营业闭环；Web/Node/浏览器/Java保存）。真机与真实2小时等待未做（L单独记证）。
- 接管时状态：business/business-model/business-ui/business-save/facts/requirements/timeline/menu-model 与 4→5 迁移已由前一Agent实现；本次按质量门审计、补齐与修复，未重写。
- 本次修改：
  - tools/content-business-rules.mjs：`COMPILED_MENUS=['MN1']`，MN1 unlock/complete/validService 编译为可求值树（E再开MN2–8）；build-runtime-content 把该文件纳入sourceHash来源（此前遗漏，改动它不会触发漂移检查）。
  - web/requirements.js：导出 `REQUIREMENT_KINDS`，编译器测试据此认可可求值条件。
  - web/workshop-ui.js：营业进行中时手艺页提供“结算并收摊后重配”（先按已过窗口结清再进入重配草稿；进行中的营业单不被改写）。
  - web/business-ui.js + 新增 web/ui-preferences.js：未读账单优先展示并在查看后标记已读；标记只存每设备UI偏好（`chick-kitchen-ui-v1`），不进经济存档，丢失只会再次显示账单。工坊入口显示“营业中/有新账单”。
  - Java：C期已加入 schema5 `validateBusiness`；本期加入真实Web命令生成的营业三态与8项反例。
- 新增测试：tests/business-gate.test.mjs（MN1编译条件与领域门槛逐边界一致；原生ACK丢失后回读为已提交、同commandId重放不重复、二次收摊只返回账单；未写入收摊整体不变且报“暂未保存”；即时出售只用自由库存与未预留额度；重配须先收摊、收摊先结清过去窗口再释放；数周离线只结一次不自动续开）。原有business/business-save/timeline-business测试继续覆盖T/R/S/Q、0/2h−1/2h/24h/数周、切片与一次推进一致、95CP、18只收摊、余数/额度预留、损耗边界、窗口不可重放。
- 测试结果：`node --test --experimental-test-isolation=none tests/*.test.mjs` 420/420（artifacts/work-d-tests.txt）；`node tools/qa-work-d.mjs` 8项通过（artifacts/qa/work-d/report.json）；qa-work-b、qa-work-c、verify-ui 7套均通过；run-save-tests 226、run-notification-tests 60；生成物无漂移。
- 可玩验收（加速时钟/隔离档）：旧档未完成第一笔采购不能开张；完成后备24留1、额度预留不减T/CP；即时出售只能卖自由1只；2h成交6只且刷新不重放；售罄95CP分项（72+8+3+12），反复查看不加钱；第二单18只提前收摊释放6只与额度；**关闭页面26小时后重开**由同一时间线结清一次并直接展示账单；手艺页收摊后重配。
- 已知问题：营业页信息架构仍挂在工坊入口下（K迁到“生意”一级）；来客计数已累积但常客内容在I接入；真实2小时窗口与真机后台未验证。
- 回滚：`ACTIVE_BUSINESS_MENUS` 置空即禁止新开店；进行中的单仍由 advanceTimeline/closeBusiness 按原rulesVersion结清，货款与facts保留，不还原初备库存。源码快照 artifacts/source-backups/20260923-093112-764c7f78.zip。
- 关键接口：`openBusiness/advanceBusiness/closeBusiness`、`advanceTimeline`、`closeBusinessTimeline`、`inventoryView/freeCount/homeCount/usableByOwner`、`reduceFacts`、`evaluate`、`businessModel`、`uiPreference/setUiPreference`。

## Work E 证据与回滚点

- 状态：已完成（O01、O04与情境采购基础；8菜单定义与条件；Web/Node/浏览器/Java保存）。
- 新增模块：web/orders.js（提案/接取冻结/按单预留Q/分组分批交付/O04展示/取消/严格校验）、web/order-model.js（只读视图，未收录品种只计数）、web/order-ui.js（生意簿“订单”页：旧三章卡、进行中、询问、交付/预留/展示页）、web/orders.css、tests/orders.test.mjs、tests/menus.test.mjs、tools/qa-work-e.mjs。
- 修改模块：business.js（8菜单均定义、各按解锁；来客步进为采购里程碑）、business-ui/business-model（菜单选择、生意簿营业/订单切换）、menu-model（`legacyRouteOpen`：MN4按原溪岸路线，MN5按茶坡地区开放+发布，MN7按GUIDE-B）、requirements（新增regionOpen叶；routeOpen改为原路线）、content-business-rules（MN1–MN8三条件全部编译，68条）、timeline（完整归队记寻访事实并作为采购里程碑）、regional-exploration（地区归队写tripComplete事实；升级后真实归队的旧trip也记一次，不回填）、engine（整批收完为采购里程碑）、save-migrations/business-save（orders容器定型并严格校验）、SaveRepository（订单实例与T≥R+S+Q）、app（订单页接入与刷新）。
- schema：仍为5（营业/订单在D/E开发期内定型，未发行，无需新增步骤）。orders字段：sequence、proposalSequence、proposals[≤2]、active[≤2]、templateProgress、refillCredits(≤2)、lastProposedTemplate。
- 设计取舍（工程机制，非内容创作）：采购在“累计收取240+发现8（原溪岸开放）”后开放，取自UI信息架构阶段表；提案仅在真实里程碑补位（营业每12只来客步进、完整寻访归队、整批收完），打开页面/重载/略过不补位，略过后空位等到下一个里程碑；模板按发布顺序稳定轮转，不重抽；一次只保留同模板一份意向/进行单；首份纸页以`Oxx:complete`/`O04:display`事实永久记录，H阶段据此入册（不重复）。
- 测试结果：`node --test --experimental-test-isolation=none tests/*.test.mjs` 437/437（artifacts/work-e-tests.txt）。覆盖：O01两批各6付基础价、完成才付12CP、首份纸页只一次；Q不被S占用、自有Q优先消耗、取消保留已付/释放Q、重接为新ID；农场损耗缩减Q并标记补货无罚款；默认留一与显式覆盖；组允许名单/超量/不同品种下限逐只校验且失败无副作用；O04只看自由在家各1只不扣货不付款、不重复、营业占用或外出不可展示；无不同品种可行解不生成；略过不无限刷新；最多2单；旧第一采购照常且只付一次；校验器拒绝冻结条款/货款/预留篡改；8菜单编译条件与领域门槛逐边界一致；MN5/MN7正确显示缺项；8组可开放作者例子真实营业：开张完整、首窗完整见证、余货减少后按剩余判档。
- 浏览器验收：`node tools/qa-work-e.mjs` 7项通过（artifacts/qa/work-e/report.json）；qa-work-b/c/d与verify-ui 7套均通过。
- Java：run-save-tests 234（新增订单真实命令4态与4项反例）；通知60；Android Java编译通过。
- 已知问题：订单页仍经工坊入口进入生意簿（K迁至“生意”一级）；O02/O03/O05–O12由F/G按计划开放；常客对采购的引用在I接入；纸页的册页展示在H。
- 回滚：`RELEASED_ORDER_TEMPLATES` 置空即停止新提案；已接实例仍可交付/取消（校验不依赖发布表），已付不退、已消费不回；菜单可从`ACTIVE_BUSINESS_MENUS`单独收回新开张。源码快照 artifacts/source-backups/20260923-095335-d2e2877c.zip。
- 关键接口：`orderMilestone`、`acceptProposal`、`reserveForOrder/releaseReservation`、`deliverOrderGroups`、`displayOrder`、`cancelOrderInstance`、`orderOptions/speciesProducible`、`ordersModel`、`legacyRouteOpen`。

## Work F 证据与回滚点

- 状态：已完成（Web/Node/浏览器/Java保存）。真机未测。
- 实现方式：沿用C的数据驱动链，**未新增领域分支**。改动：`COMPILED_REGIONS` 加 R/T 与12行卡门槛（R-N1为“本区任一材料”any-of，其余为单一材料或无）、`RELEASED_REGIONS=['V','R','T']`（ALT-T随T-E1开放，ALT-R仍等PJ-2）、`RELEASED_ORDER_TEMPLATES` 加 O02/O03/O05/O06/O07/O08/O09/O10/O12（O11留G）、O06只提供已发布地区的变体并在接取时冻结、工坊入口文案改为通用“寻味记录”。条件编译 68→156（R/T作者条件88+ALT两条保证）。
- 新增测试：tests/regional-river-tea.test.mjs（两区门槛独立、海湾仍关；蜂蜜/白米饭经旧供货规则见证入门；烧水壶/牛乳菜须厨房Lv.4与设备；旧料新品随各自首标本；R-N1任一材料、R-D6另需水芹、T-N1/T-N2各自材料；R-S1水边与T-N2花香加成只计一次；ALT-T映射79→3保留27且与旧茶叶蛋池逐位一致；辨认4种→42格且不开放其他供货；O06冻结地区、海湾变体未开放；O10两章各6且不能跨章交付；24个R/T方法合法前置全部可达）。原C/E测试中写死发布集合的断言改为按Work进度断言。
- 测试结果：`node --test --experimental-test-isolation=none tests/*.test.mjs` 447/447（artifacts/work-f-tests.txt）；`node tools/qa-work-f.mjs` 4项通过（artifacts/qa/work-f/report.json）；qa-work-b/c/d/e、verify-ui 7套通过；run-save-tests 240（新增溪岸真实轨迹与2项跨区反例）；生成物无漂移。
- 可玩验收（加速时钟/隔离档）：寻访页三地区切换；溪岸首趟换地点/补材料仍保证山柚标本→辨认；茶坡首标本焙香叶→辨认；未辨认桂花只以谜面出现；焙香奶茶鸭明确显示厨房Lv.4与烧水壶缺项；完整寻访作为采购里程碑，略过后空位不补，下一趟后按轮转接取F模板O02（需求冻结）。“Lv.4回访做全部新品”以单元可达性测试证明，浏览器未逐一实收24种。
- 已知问题：同C（概念剪影、页面较长、入口在工坊）；O06海湾变体与O11在G开放。
- 回滚：从 `RELEASED_REGIONS` 移除R或T即停止该区新出发/新目标，已有票据/库存/容量/保护保留；从 `RELEASED_ORDER_TEMPLATES` 移除模板只停新提案，已接实例可结清。源码快照 artifacts/source-backups/20260923-100623-42c17ead.zip。

## Work G 证据与回滚点

- 状态：已完成（Web/Node/浏览器/Java保存）。真机未测。
- 新增/修改：
  - exploration.js：`BAY_ROUTE`（12h/基础3/普通池盐27·海苔74·柠檬1，GUIDE-B后开放，环境按水边别名）；`ROUTES=[三条旧路线,海湾]`，旧工坊寻访页仍只列三条旧路线（海湾只走地区票据）；`routeFailures.bay`稀疏创建；召回释放带货。
  - regional-exploration.js：`guideEligibility`（厨房Lv.3、发现40、前三区任一区辨认标本且实收当地新品）与出发选项`guide`（仅溪岸岸边摊）；完整归队确定写入`guideFlags:['GUIDE-B']`，召回不给，不占发现卡；`cargo`选项（B-E1约定：正好6只、限家常名单、不能用同行队员、默认留一可显式覆盖、需已辨认盐花），出发时占用R、替换一格基础材料为盐花，完整归队才扣货并记`B-E1:cargoExchange`，无售款、不计营业/采购；票据与带货严格校验。
  - region-model：海湾地区门槛额外要求GUIDE-B；`RELEASED_REGIONS` 加 B（ALT-B随B-E1开放）。orders：O11开放；O06海湾变体随B发布出现。条件编译156→204（B作者条件+ALT-B+GUIDE-B四条）。
  - progress-save：海湾路线/稀疏保底计数/海湾只接受地区票据/带货奖励材料来源。SaveRepository：海湾12h、带货约定与状态、R含带货的T≥R+S+Q、路标票据限溪岸岸边摊。
  - regional-ui：溪岸岸边摊“追寻沿湾路标”勾选与原因、盐田小路“可选带货”折叠区（逐种步进、成本说明），确认页列出路标与带货。
- 新增测试：tests/regional-bay.test.mjs（引路门槛与地点、确定取得/召回不给/不占卡、海湾12h/3与轻装9h36/2、TRIP-1按实际基础份数18/12、带货占用/非队员/留一/替换基础格/存档中途不复制/归队扣一次/召回释放/B-E1后仍可换、带货票据篡改拒绝、ALT-B 81→27、O06海湾变体/O11/MN7开放、**48新品在合法前置下全部有可执行配方**、**24张发现卡在合法队伍与状态下全部成为候选**）。
- 测试结果：`node --test --experimental-test-isolation=none tests/*.test.mjs` 456/456（artifacts/work-g-tests.txt）；`node tools/qa-work-g.mjs` 3项通过；qa-work-b/c/d/e/f、verify-ui 7套通过（F脚本改为按进度断言地区列表）；run-save-tests 251（新增海湾真实轨迹与6项反例）；Android Java编译通过；生成物无漂移。
- 可玩验收（加速时钟/隔离档）：已走过谷地的Lv.3旧档先看到海湾“先追寻沿湾路标”；溪岸岸边摊勾选路标→完整归队确定取得GUIDE-B；海湾12h首趟保证盐花标本→辨认；盐田小路带家常6只，出发只占用、中途刷新不复制，归队才扣货并以盐花替换一格。“做B-C1”与“241/24全部可达”由单元可达性测试证明，浏览器未逐一实收。
- 已知问题：地区列表在390宽以横向滚动显示4个地区（K统一导航）；RG4引路分支在I接入（本期只有溪岸路标这一确定渠道，已满足“不形成循环锁”）。
- 回滚：从 `RELEASED_REGIONS` 移除B即停止新海湾出发/目标；已有海湾票据、带货约定按冻结条款到期结算或召回释放；GUIDE-B与海湾身份保留。源码快照 artifacts/source-backups/20260923-102100-05b15c0c.zip；影子提交 da4e98a。
- 关键接口：`guideEligibility`、`regionalTripInfo`的`guide/cargo`选项、`validateTripCargo`、`BAY_ROUTE/ROUTES/LEGACY_ROUTES`。

## Work H 证据与回滚点

- 状态：已完成（Web/Node/浏览器/Java保存）。真机未测；纪念物与插页正式美术未交付，陈列与纪念物页使用明确标注的“概念占位”。
- Schema：5→6（`CURRENT_SAVE_VERSION=6`，Java `CURRENT_VERSION=6`）。migrate5to6只新增空容器 `expansion.collections{version:1,entitlements:{},display:[null,null,null]}`、`regulars{}`、`projects{}`、`menus{presets:[]}`，迁移本身**不授予任何成果**。
- 新增/修改：
  - web/collection-progress.js：`collectionProgress`（主题收录只看永久发现、卖空仍算；COL-8只计四时章节、地区客座仅展示；地区册草稿/完成与实践；SP-*固定/可选/实践）、`ENTITLEMENTS`目录（主题插页、M01–M08、完整菜单印、地区插页/边饰/实践印、特殊插页/边饰/印、采购纸页NOTE-O*）、`grantEntitlementOnce`（每身份一次、seq取 `++meta.factSeq`、不发CP）、`reconcileEntitlements`、`setDisplay`、`validateCollectionsState`。
  - game-commands.js：每次成功提交后在同一事务内对账；app.js载入时若对账有新增则以独立提交写入（193全收旧档一次登记8主题插页+M01–M08）。
  - save-migrations.js：v6校验、`registerContainerValidator`（I/J注册前，regulars/projects/presets非空一律拒绝）。
  - tools/content-collection-rules.mjs：COL-1..8阶段/实践/完整菜单印、COL-X、SP-*、M01–M12效果、M01–M08解锁、O01–O12资格/付款/O10季节规则、productionRules（ui除外）编译。条件编译204→312（剩余126：常客关系/阶段(I)、项目阶段(J)、M09–M12解锁(I)、productionRules:ui(K)）。
  - collections-ui.js/collections.css：收藏册五分页（主题/地区/特殊/纪念物/纸页），未收录候选只计数不署名；图鉴“收藏册 ›”入口；农场新增“陈列架”热区与绘制（占位标签）。
  - SaveRepository：`validateCollections`（身份白名单、seq唯一且≤factSeq、恰好3个陈列位且只能陈列已得纪念物、menus预设≤3）；RuntimeContent增加entitlementIds/mementoIds。
- 新增测试：tests/collections.test.mjs 8项（迁移不授予/下一次提交对账且幂等；193全收得8插页+M01–M08但不补造实践印与地区页；COL-8章节与客座；普通摊售出一份地区菜即得地区实践印但不伪造MN1营业；SP-ALL脚步格按首次完整同行；陈列无经济影响；NOTE-O01一次；校验器反例）。Java新增4项v6反例。
- 测试结果：`node --test --experimental-test-isolation=none tests/*.test.mjs` 465/465（artifacts/work-h-tests.txt）；`node tools/qa-work-h.mjs` 3项通过（artifacts/qa/work-h/report.json，含320/390/1280截图）；verify-ui 7套、qa-work-b/c/d/e/f/g全部通过（D脚本版本断言改读 `CURRENT_SAVE_VERSION`）；run-save-tests 256、run-notification-tests 60、check-android-java 通过；生成物 `--check` 无漂移。
- 可玩验收（加速时钟/隔离档）：schema5的193全收旧档载入→迁移到6→一次登记8主题插页与M01–M08，CP、四时/神社旧记录不变、不补造实践印，刷新不重复；纪念物页选两件陈列，农场陈列架显示，刷新保留，CP/库存不变，常客4件显示“尚未得到”；荠菜煎饼鸡+鸡宝混合队伍完整寻访谷地→点亮脚步格并自动盖地区实践印。
- 已知问题：纪念物/插页为概念占位；收藏册暂从图鉴二级按钮进入（K统一导航）；M09–M12与NOTE-RG*在I由常客授予。
- 回滚：新入口可隐藏，已登记成果只读保留；v6档不降级。源码快照 artifacts/source-backups/20260923-104009-35a4c4fd.zip。
- 关键接口：`collectionProgress(s,id)`、`grantEntitlementOnce(s,id,source)`、`reconcileEntitlements(s)`、`setDisplay(s,slot,id)`、`registerContainerValidator(key,fn)`。

## Work I 证据与回滚点

- 状态：已完成（Web/Node/浏览器/Java保存）。真机未测；常客无立绘，页面用称呼首字的圆形小签作占位（设计本就不要求立绘）。
- Schema：仍为6（使用H预留的 `expansion.regulars` 稀疏容器，未升版本）。每位常客记录 `{readStages,pendingStage:{id,seq,branch}|null,activatedSeq,baselines,lastVisit:{stageId,sessionId}|null}`，首次有段落排队时才建立；旧三章只显示“旧三章已认识”，不记为已读。
- 新增/修改：
  - web/regulars.js：16段逐ID转写的 `STAGE_RULES`（门槛+两条分支，全部读永久事实/卡/辨认/订单完成见证），`regularInfo`、`reconcileRegulars`（每位最多1段未读；排队与纸条/纪念物登记同一事务；RG4-1达成即确定写入GUIDE-B，不占发现卡）、`readRegularStage`（只推进，不付款不扣货；激活下一段并记录RG2-4的O05基线）、`visitorCandidates`（开张时冻结：置顶优先，“熟客故事优先”按进度少者先、“地区线索优先”按R/T/B/V）、`presentVisitor`（每12只真实销量的来客步骤呈现一段未呈现的故事，否则仅普通文字无CP）、`reconcileProgress`、`validateRegularsState`。
  - business.js：开张冻结 `visitorCandidates`；来客步骤调用 `presentVisitor`。game-commands/app载入改用 `reconcileProgress`（常客→收藏）。save-migrations 直接校验常客容器，项目与菜单预设仍要求为空（J开放）。
  - regular-model.js / regular-ui.js / regulars.css：生意簿新增“常客”标签（营业/订单/常客）；四枚折页“已读/可继续/还需一件事”，当前段给门槛与两条路，读故事两步（先展开正文，再“收好这段”），置顶（UI偏好 `pinnedTarget`，K并入全局目标位）；账单“来访的常客”列出本单带来的故事并可“去读/回看”；营业准备新增“来客倾向”选择。分支原文中的MN/O/PJ编号显示为名称，未取得的卡与未辨认材料显示为“溪岸事件一”“茶坡新材料”等遮罩。
  - tools/content-regular-rules.mjs：RG1–4 delivery/completion、16段gate/alternatives/recordPolicy、M09–M12 unlock、48条物种regularRelation（仅导航，不发奖不解锁）编译。条件编译312→420（剩余18：PJ项目17条(J)、productionRules:ui(K)）。
  - SaveRepository：`validateRegulars`（顺序已读、唯一可读下一段、已读/未读段成果必须已登记、仅RG2-4带O05基线、来访记录）与营业 `visitorCandidates` 校验；项目容器仍须为空。
- 新增测试：tests/regulars.test.mjs 14项（16段结构与编译；无门槛不排队；手动O01不开店即排队并登记纸条；最多1段未读、读后追认下一段且不付款；真实MN1营业第2窗来客呈现且无额外收入；置顶/倾向只改顺序；RG2-3同单见证不拼接；RG2-4激活前O05不算、激活后算、O04展示可追认；RG3-1两种茶味；RG3-4营业18与采购18不混加且需3种；RG4-1写GUIDE-B且不增加卡、海湾门槛解除；RG1四段完整链只得M09一次、毕业无红点；事务内协调与重复阅读拒绝；旧三章只“已认识”；校验器反例；名称显示与遮罩）。Java新增1条真实Web轨迹（MN1营业→来客→阅读）与6项反例。
- 测试结果：`node --test --experimental-test-isolation=none tests/*.test.mjs` 479/479（artifacts/work-i-tests.txt）；`node tools/qa-work-i.mjs` 2项通过（artifacts/qa/work-i/report.json，含320/390/1280）；verify-ui 7套、qa-work-b/c/d/e/f/g/h 全部通过；run-save-tests 266、run-notification-tests 60、check-android-java 通过；生成物 `--check` 无漂移。
- 可玩验收（加速时钟/隔离档）：A 不开营业，收完一锅→接取“街坊备早饭”→分两批交付完成→常客页出现“熟悉的早饭”，读完后显示第二段两条路（「茶香便当」营业或「茶会添一盘」采购），CP与库存不变；B 开「家常小铺」备货12只，4小时后账单显示“旧厨房老顾客带来「熟悉的早饭」”，点“去读”进入常客页，读前刷新不丢段、不重复纸条，读后刷新不跳段、账款不变。其余15段与全部替代分支由单元测试逐条覆盖，浏览器未逐段实走。
- 已知问题：置顶暂存于本机UI偏好（K统一全局目标位时再决定是否入档；它只影响来客呈现顺序，不影响任何经济结果）；常客页入口仍经“工坊→生意簿”，五入口在K统一。
- 回滚：停止排队新段可移除 `reconcileRegulars` 调用；已登记纸条/纪念物、已读与未读段保留，允许继续阅读，不重发、不重置基线。源码快照 artifacts/source-backups/20260923-110343-a1bbb667.zip。
- 关键接口：`regularInfo(s,id)`、`reconcileRegulars(s)`、`readRegularStage(s,id)`、`visitorCandidates(s,{pinned,tendency})`、`presentVisitor(s,session)`、`reconcileProgress(s)`；J 的 PJ-3“RG3任一段完成”读取 `expansion.regulars.RG3.readStages/pendingStage`。

## Work J 证据与回滚点

- 状态：已完成（Web/Node/浏览器/Java保存）。真机未测；项目成果插画/布置组件无正式美术（页面用已收录角色头像与文字成果）。
- Schema：仍为6（使用H预留的 `expansion.projects` 与 `expansion.menus.presets`）。项目记录 `{stages:{stageId:{complete,seq}},pinnedChoices:{stageId:[选定品种],portraits?:[...]},deliveries:{stageId:{key:n}},payments:{stageId:cp}}`，首次操作时才建立；迁移不推算任何交付或付款。
- 新增/修改：
  - web/projects.js：`PROJECT_RULES` 逐ID转写PJ-1..4门槛与12个阶段检查；阶段按顺序推进；`deliverProject`（只用自由库存、默认留1；“任选N种”首交付锁定；PJ-4-B共48只且预留空间保证≥4类招牌风味；交付离开农场、不付货款，写 `projectDelivery` 事实）；`completeProjectStage`（检查+交付满+CP足够，同一事务扣固定费用并登记，写 `projectPayment`；重复确认拒绝；CP不足零改动；PJ-2完成即学会ALT-R）；`setProjectPortraits`（PJ-4展册最多12幅永久画像，任意已收录品种含旧观赏，不消耗、不算食用交付）；`saveMenuPreset/deleteMenuPreset`（PJ-1完成后3个预设，只存菜单与数量，载入仅填准备草稿不占用）；`validateProjectsState`。
  - region-model：`RELEASED_PROJECT_ALTERNATIVES=['ALT-R']`、`alternativeUnlocked`（卡或项目）；regional-methods 用它判定地方做法；寻味记录页对ALT-R显示“完成项目「溪岸风味篮」后…”。
  - project-model.js / project-ui.js / projects.css：生意簿新增“项目”标签（营业/订单/常客/项目）；列表只显示已开始/门槛已满足的项目加一个“下一个可能的项目”；详情逐阶段显示检查✓、交付进度、费用与“交付/登记完成/支付X CP完成”；交付页勾选/步进、留1开关；PJ-4展册画像选择；置顶共用 `pinnedTarget`。business-ui 营业准备新增“菜单预设”行；collections-ui 纸页标签显示“四地风味展 · 展册画像”。项目文本中的M01/ALT-R/R-S1等编号显示为名称或遮罩。
  - tools/content-project-rules.mjs：PJ gate、12个stage check（含费用与交付约束）、PJ-4 optionalDisplay.rule 编译。条件编译420→437（仅剩 productionRules:ui，属K）。
  - SaveRepository：`validateProjects`（顺序完成、付款等于固定费用、交付品种/上限/锁定/满额、类别数、画像只属PJ-4）与菜单预设校验（需PJ-1完成、≤3、菜单与食用品种合法）；RuntimeContent新增 `tradeCategories`。
- 新增测试：tests/projects.test.mjs 10项（编译覆盖与费用表；PJ-1 0/0/200与预设≤3不占库存；PJ-2 100/0/400、两种各6首交付锁定、不付货款、ALT-R只在完成后学会；只用自由库存/留1/不碰营业S；PJ-3 200/0/600、营业+采购合计36与RG3任一段；PJ-4 400/600/1000、事件不替代标本/见闻、48只≥4类的预留规则；画像含旧观赏且不消耗；CP不足零改动、ACK丢失重放不重复扣款、重复确认拒绝；迁移为空且检查可追认；校验器反例）。Java新增项目真实Web轨迹6步与6项反例。
- 测试结果：`node --test --experimental-test-isolation=none tests/*.test.mjs` 489/489（artifacts/work-j-tests.txt）；`node tools/qa-work-j.mjs` 4项通过（artifacts/qa/work-j/report.json）；verify-ui 7套、qa-work-b…i 全部通过；run-save-tests 279、run-notification-tests 60、check-android-java 通过；生成物 `--check` 无漂移。
- 可玩验收（加速时钟/隔离档）：招牌册两阶段登记不花CP、第三阶段支付200，刷新后阶段与余额不变；营业准备保存/载入菜单预设不占库存；溪岸风味篮付100→勾选两种各交6只→登记→付400并学会ALT-R，刷新后一致；四地风味展选择鸡宝与旧观赏的画像，库存不变，图鉴收藏册显示展册。PJ-3/PJ-4完整阶段由单元测试覆盖，浏览器未走完。
- 已知问题：PJ-4“在家实物陈列”模式未实现（只做永久画像模式，内容规则允许两者之一，画像模式已满足“不计食用交付”）；项目成果插画为文字；置顶仍为本机UI偏好（K统一）。
- 回滚：禁新阶段提交可移除项目页按钮/命令；已交付、已付款、已完成阶段与ALT-R保留，不退款；预设与画像只读保留。源码快照 artifacts/source-backups/20260923-112201-f33dec0b.zip。
- 关键接口：`projectInfo(s,id)`、`deliverProject(s,p,stage,selection,{choice,overrideKeepOne})`、`completeProjectStage(s,p,stage)`、`setProjectPortraits(s,keys)`、`saveMenuPreset/deleteMenuPreset`、`projectComplete`、`alternativeUnlocked`。

## Work K 证据与回滚点

- 状态：已完成（Web/Node/浏览器；Android Java编译与保存回归）。真机/WebView未测；正式48新品与项目/纪念物美术仍为概念占位（见L与已知问题）。
- Schema：无变化（仍为6）。置顶目标 `pinnedTarget`、已读账单、“补给搬家”提示均为本机UI偏好（`chick-kitchen-ui-v1`），不进经济存档。
- 导航与页面归属：
  - theme.js：`NAV` 固定为 厨房(0)/农场(1)/生意(5)/寻访(6)/图鉴(4) 五项，每项62逻辑像素（320宽不横滑）；`PAGES`、`navEntries()`（productionRules:ui 的绑定）。“商店”不再是底栏标签。
  - scene.js：五项底栏绘制（生意用原摊位图标；寻访为同风格路牌矢量图标）；生意/寻访页背景与页头；底栏结果小圆点只在真实待处理结果时出现（生意：未读账单或未读常客段；寻访：队伍已归来），锁定项不加红点。
  - app.js：`changePage(5)`→生意簿（记住上次子页：营业/订单/常客/项目；有未读账单时先显示账单）；`changePage(6)`→寻访页；`openTrade/openExplore` 让所有跨页入口经过所属地方；`panelReturn` 让从某地方打开的面板关闭时回到原地（寻访→原路线→关闭回寻访；订单→旧采购→回订单；订单→做法→回订单）；订单交付页对缺货品种提供“去看做法 ›”（旧品种进配方册并显示“返回订单”，地区品种进该地区记录），返回后交付页与草稿保留（`orderUI.resume`）。
  - 厨房：原“手艺 · 生意 · 寻访”条改为“手艺”“补给”两个按钮，补给一击打开小卖部；老玩家首次进入提示一次“补给搬到厨房了，缺料时也能直接打开。”
  - 手艺面板：移除重复的“小店营业/地区与发现”入口，旧页签改名“旧采购/旧路线”；面板高度改为不遮挡底栏。寻访页常驻“原路线”入口（原三条路线）。
  - 寻访：四个地区按钮改为2×2网格，不再横向滚动（G遗留问题关闭）。
  - 收藏册主题/地区/特殊页可“置顶”，与常客、项目共用一个全局目标位。
  - 桌面/平板宽屏（宽≥900且横向）：`#game.is-wide` 逻辑宽740，左侧保持320场景，完整页面在右侧404宽栏打开；无页面时右侧显示“今日小厨房”（锅/生意/寻访状态与置顶目标，按钮直达所属地方）。手机与横屏手机保持单列。
  - 字体：`tools/build-font.py` 重建 Noto Sans SC 可变子集，覆盖2,084码位、缺字0（此前新内容有88个字落到系统字体）；web/fonts/README 与 coverage.json 已更新。
  - 条件编译：productionRules:ui → `web/theme.js#navEntries`；**438/438 全部编译，0 unavailable**。
- 旧UI门禁脚本随导航迁移更新：点击“商店”改为“厨房→补给”；`nav:2` 改为 `supply`；B–J验收脚本的“工坊→生意/地区”改为点底栏“生意/寻访”；qa-tool-strip 坐标换算改用 #game 的逻辑宽度（桌面双栏）。
- 新增测试：tests/five-entry.test.mjs 3项（五项顺序与无商店、62宽不重叠；438/0编译且UI规则绑定；字体覆盖报告无缺字）；viewport-layout 断言改为五项。`tools/qa-work-k.mjs` 9项浏览器验收。
- 测试结果：`node --test --experimental-test-isolation=none tests/*.test.mjs` 492/492（artifacts/work-k-tests.txt）；`python tools/build-font.py --check` 通过；verify-ui 7套、qa-work-b…k 全部通过；run-save-tests 279、run-notification-tests 60、check-android-java 通过；生成物 `--check` 无漂移。
- 可玩验收（artifacts/qa/work-k/report.json，含各尺寸截图）：320×568、375、390、430、横屏844×390、桌面1280×900下底栏均为五项且无横溢，补给一击到小卖部；桌面生意页在右栏打开、左侧场景可见，厨房时右栏显示今日摘要；订单交付页选1只→切农场→回生意仍在同一交付页；缺货品种“去看做法”→“返回订单”→草稿仍在；寻访“原路线”关闭后回寻访；寻访两页、生意四页、收藏册五页扫描48个未收录新品名称0泄露；农场拖动后神社委托簿可进入；键盘聚焦“生意”回车进入；补给提示只出现一次。
- 已知问题：横屏手机（如844×390）整体按568逻辑高缩放，底栏触区约43×37px（与旧版同一缩放规则，未另做横屏布局）；系统“大字号”对固定像素字号的Canvas/面板不生效，未做大字号专项；深色模式未做；桌面右栏只承载完整页面与摘要，选择抽屉/确认框仍在左侧场景列内。
- 回滚：`NAV` 恢复四项并把生意/寻访挂回工坊即可；页面与数据无迁移。源码快照 artifacts/source-backups/20260923-120136-7381dd01.zip。
- 关键接口：`PAGES`、`navEntries()`、`openTrade(kind,id)`、`openExplore()`、`panelReturn`、`orderUI.resume()`、`renderDesk()`。

## Work L 证据与回滚点

- 状态：**功能门禁已完成；发布验收未全部通过**（最终美术、价格签收、真机/签名APK未完成，见 docs/archive/iterations/release-acceptance-report.md 第5节）。
- Schema：无变化（6）。1→6迁移链由6个黄金旧档（v1/v3）单元测试与浏览器验收覆盖；进行中跨发行升级（schema 5营业中+地区寻访在途）浏览器验收通过；无需6→7。
- 新增：
  - tools/simulate-reachability.mjs：合法操作机器人（每步经 `execute`），种子1–8全部取得48新品与24卡；每种≤4锅、每卡≤4趟（保底上限恰为4）；约160趟/130锅/60虚拟日/1.6万CP（artifacts/sim/reachability-*.json、reachability-summary.json）。
  - tools/simulate-economy.mjs：4画像×3回访节奏的14天模拟（营业、即时出售、采购、常客、项目、清洁维修、厨具与鸭蛋投入），逐日CP与收支分项；结果与发现见发布报告第3节（artifacts/sim/economy-14d.json）。
  - tools/qa-work-l.mjs：6个黄金旧档升级后五个地方逐一打开、未知名扫描、刷新稳定；schema 5营业中+寻访在途的跨发行升级只结算一次。
  - tools/qa-real-window.mjs：真实时钟2小时营业窗口+离开页面回访（已通过：1小时回访未成交，2小时3分回访第1窗6只一次入账）。
  - tests/release-backup.test.mjs：营业中、采购进行中、常客已读、项目完成、预设、画像、陈列、寻访在途的schema 6存档经备份/恢复与更新备份检查器无损。
  - tests/runtime-packaging.test.mjs：runtime-assets声明的可用性与磁盘文件一致，打包器只跳过声明为待交付的美术。
- 修复（记录）：**Android运行资源打包失败**——问题：`android/package-runtime.mjs` 静态遍历把 runtime-assets 中48新品“待交付美术”路径当成必需文件，自B/C加入地区资源清单后打包一直在lstat处失败；为何是缺陷：运行时从不加载这些`available:false`路径（用概念剪影），打包却因此无法产出；改动：只跳过清单明确声明为 `available:false` 的路径，其余缺文件仍报错；影响：打包恢复（657个运行文件，含全部新模块、样式与重建字体），美术交付后自动纳入。
- 测试结果：Node 494/494（artifacts/work-l-tests.txt）；`tools/verify-release.ps1` 6/6；qa-work-b…l 11/11；check-android-java通过；打包一致性通过；生成物无漂移；438/438条件编译。
- 可玩验收：六黄金档全流程（浏览器）；193老玩家、收集优先、低频、鸡蛋前期四类路线的14天模拟；241/48/24逐项合法可达（机器人）；真实2小时窗口与跨后台归来单独记证（非加速）。
- 未执行/未通过：签名APK构建与真机生命周期/强退/备份恢复（不安装APK、不动真实存档；需测试机）；最终美术；价格平衡签收；>14天长期经济与真人试玩；Safari/Firefox/真实手机浏览器。
- 回滚：L只新增工具/测试与打包过滤；打包过滤可回退（届时需美术文件齐全）。源码快照 artifacts/source-backups/20260923-140702-fdd0a589.zip。
