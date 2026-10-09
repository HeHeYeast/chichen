# 鸡宝厨房 · Current State / Decision History / Handoff v2

审计日期：2026-09-27。**以当前工作树和正式入口为准**；本文只记录状态与既有决策，不是实施授权。新增收集内容在本文仅列数量，不列角色、发现或具体解锁内容。

## 1. 一眼看懂阶段与边界

|标签|当前事实|
|---|---|
|**FINAL / RUNTIME**|可玩产品 Base 是 `web/index.html` → `web/app.js`，由 `server.mjs` 的 `/` 提供；五个主入口为厨房、农场、生意、寻访、图鉴。已有四级厨房、孵化/厨具等玩法、241 个身份（旧 193 + 新 48）、83 种材料及扩展经营/寻访/收藏内容。Work 1 正式美术和 Work 2 UI Remaster 已接入当前工作树。厨房目前仍是**原正式四级设计**，没有接入本轮 Kitchen Remaster。|
|**PROTOTYPE**|`web/prototypes/kitchen-layout/` 是 Lv.2 A/B/C 灰盒布局；`web/prototypes/kitchen-remaster/` 是独立 Lv.2 可操作 Remaster Gate，使用单独存档键。两者都不是 `/` 的页面，也不是已批准设计。|
|**EXPERIMENT**|`artifacts/kitchen-concept-remaster-20260927/`、`kitchen-visual-language-lv2-20260927/`、`kitchen-asset-language-gate-20260927/`、`kitchen-background-direction-gate-20260927/` 中的完整图、候选 Asset、背景和 Overlay 只用于视觉判断；未作为正式厨房素材接入。|
|**ABANDONED / 未采纳**|整张 AI Kitchen Concept 当 Runtime 背景、管理后台式灰盒厨房、只靠减纹理的 Style C、仅凭单独 Asset 或背景 Gate 判定整页、保守轻换皮，均已被后续判断否定。旧文件保留作证据，不代表方案有效。|
|**TODO**|当前工作是 **Work 3A Kitchen Remaster 的 Creative Direction 选择前**。下一轮约六个 Lv.2 核心视觉概念尚未制作，更未得到用户选择；Work 3B 农场、Work 4 全游戏 QA、Work 5 APK/真机均未开始本轮实施。|

**技术状态：**Work 1/2 的既有记录没有已知技术 blocker；目前卡在厨房视觉概念与整屏效果的用户 Gate，不是引擎障碍。此交接轮只读代码与证据、未重新运行测试、未构建 APK。工作树有大量既存已修改和未跟踪文件；不要把历史 APK 或历史报告当作当前树的发行验收。较旧的 `docs/current-state-handoff.md` / `docs/current-state-audit.md` 关于“48 个新角色仍全是占位、图鉴旧分支未改”的判断已被 Work 1/2 覆盖。`docs/ui/kitchen-remaster-plan.md` 中“只轻改旧木房并固定搁架/窗户”的限制也已被后续用户判断放宽。

## 2. 已完成工作

### Work 1 · 正式美术 [FINAL / RUNTIME]

- 48/48 新角色、8/8 地区材料、24/24 发现、12/12 纪念物、4/4 常客肖像完成；四地区共 96 组源资产，输出 220 个 Runtime 变体与 96 份几何元数据。内容身份和正式图已进入图鉴、厨房/孵蛋、农场、生意、寻访、收藏、常客。Runtime 资源位于 `web/art/production/`，生成索引为 `web/runtime-assets.generated.js`，接入逻辑见 `web/production-art.js` 及各页面模块。
- 本批五类**有效 Runtime placeholder 为 0**；磁盘上的旧占位图是回退历史。项目清单还有 20 组**本批范围外**配套美术 pending，所以全项目 `finalArtReady` 仍为 false；不要写成“全部美术完成”。
- 已记录的验收：7/7 场景、592/592 Node、9/9 Pipeline、220 变体解码与边界/锚点、离线包哈希与资源检查通过，浏览器 0 页面异常/0 失败请求；**未重建正式发布 APK、未做当前版真机验收**。见 `docs/art/work1-delivery-status.md`、`artifacts/internal-art/work1/engineering-status.json`、`artifacts/internal-art/work1/runtime-final-qa/report.json`。复用规范在 `docs/art/character-design-spec.md`、`docs/art/legacy-character-style-spec.md`、`docs/art/character-art-pipeline.md`；Pipeline 为 `tools/art_pipeline.py`。内部图稿与截图不面向玩家展示。

### Work 2 · Remaining UI Remaster [FINAL / RUNTIME]

- 以可达页面/状态组计 **56/56**：KEEP 29、POLISH 21、REDESIGN 6。六个重做面是手艺主页面、图鉴总览、地区收藏目录/详情、特殊收藏目录/详情；其余覆盖厨房附属操作、仓库、商店、旧采购/旧路线、配方册裁切、见闻、纪念物、纸页、帮助、日历、设置等精修或回归。正式样式从 `web/index.html` 加载 `web/ui-remaster.css`；相关页面源码和 `web/remaster-view.js` 已在当前树。
- **明确认可的视觉基准**：生意＝鸡宝篮子小铺（含营业、订单、常客、项目及账本视觉族）；寻访＝旅行地图与地点/行程视觉族；图鉴＝收藏册/主题详情视觉族。Work 2 保留并回归这三组，不再重做核心方向；手艺册与图鉴六个改造面已按本轮审计完成 Runtime 与截图 QA。旧文档中图鉴总览/地区/特殊仍待 REDESIGN 的说法属于 Work 2 前状态。
- 记录的 Work 2 验收：592 自动测试通过、114 张 Runtime 截图、0 页面错误/失败资源；覆盖 320×568、390×844、430×932、844×390、1280×900 及窄屏放大字号，另有手艺操作、未知信息保护、隔离档备份/恢复反馈等检查。见 `docs/ui/current-ui-remaster-audit.md`、`docs/ui/ui-remaster-final-report.md`、`artifacts/internal-ui-remaster/completion-evidence.json`。这不是 Work 4 的全游戏最终 QA 或 Android 验收。

## 3. Kitchen Remaster 决策历史：尝试 → 问题 → 留下的结论

|尝试与目的|实际问题 / 为什么未成为最终方案|保留下来的有效结论与证据|
|---|---|---|
|**完整 AI Kitchen Concept**：在四级厨房中比较围窝照料、转角台、小工坊 A/B/C。|整屏概念把家具、窗景、光线组织成完整可信的 Cozy Interior；可爱但像室内插画，且未拆成游戏资产。三套均未获选。|蛋窝、厨具、入口和四级成长都要有明确关系；Concept 可用于方向选择，不能直接当 Runtime 背景。`artifacts/kitchen-concept-remaster-20260927/Concept-Notes.md`。|
|**A/B/C 布局探索与灰盒 Layout Prototype**：在 Lv.2 控制素材和状态，比较居中蛋窝、上备下收、侧边厨具。|可操作，但弱场景/功能区/卡片式组织接近管理面板；仅布局正确不足以产生吸引人的厨房。没有选定 A/B/C。|自然错落蛋位、功能链、状态/入口可被独立验证；灰盒只适合验证空间与交互。`artifacts/kitchen-layout-prototype-20260927/README.md`、`web/prototypes/kitchen-layout/`。|
|**Environment Illustration vs Game Scene**：固定 Lv.2 物件比较 A 环境插画、B Asset 组合、C UI 优先。|B/C 降低材质后，仍继承窗、桌腿、地毯和完整室内立面的骨架；C 变平却仍未解决“AI 味”。它们是风格试验，不是定稿。|游戏对象和关系先于室内空间；允许局部透视和丰富场景，但视觉权重服务玩法。`artifacts/kitchen-visual-language-lv2-20260927/Game-Scene-Visual-Language.md`。|
|**Visual Forensics**：与最终生意、寻访、图鉴并排取证。|发现问题并非单项纹理、渐变或木纹；此前生成指令自己锁定了完整房间骨架，模型又补齐陈设。灰盒又走向另一端。|形成 Game Asset Visual Language / PASS–FAIL Checklist；逐项检查对象、实际手机尺寸、轮廓、局部光影、可拆换、背景主次、动态 UI 与整屏中心。原文 `artifacts/kitchen-remaster-lv2-20260927/visual-forensics-source.md`。|
|**Kitchen Asset Language Gate**：分别验证蛋窝、调味、清洁、仓库候选 A/B 的独立性和手机尺寸。|8 个候选在独立 Asset Gate 内 PASS，但没有完整 UI、背景、厨具关系；PASS 不能等于采纳。|独立边界、真实大小、动态信息不烘焙、蛋/窝分层等标准可复用；必须进行整屏判断。`artifacts/kitchen-asset-language-gate-20260927/README.md`、`Checklist.md`。|
|**Lv.2 Remaster Gate**：用旧木房重绘、入口重排和少量候选做独立可操作 Before/After。|QA 技术通过，但视觉方向未获用户确认；过度沿用原窗/搁架和小幅换皮会限制 Remaster。木托候选前沿遮蛋，未采用。|保留正式自然蛋群/原窝、功能链和同档同状态 1:1 Runtime 截图法；原型结果不得反向称作正式版。`docs/ui/kitchen-remaster-plan.md`、`artifacts/kitchen-remaster-lv2-20260927/visual-qa.md`、`web/prototypes/kitchen-remaster/`。|
|**Background Direction Gate**：A Original Remaster、B Game Scene、C Storybook、D Stylized 只比较 Lv.2 背景。|四张虽语言不同，整体仍普通、彼此差异有限；脱离真实 UI/Asset 很难判断最终体验。没有选定背景。|背景可真正重制，旧窗和搁架位置不是约束；背景仍需与完整界面共同评估。`artifacts/kitchen-background-direction-gate-20260927/README.md`、`ABCD-overview.png`。|

### “AI 味”与 Game Asset Visual Language [已确认判断]

这里的“AI 味”是**画面组织偏差**，不是通过外观判定生成来源。问题主要是画面先证明“一间完整、可信的室内环境”：统一透视/光照、连续家具支撑、窗外景深、墙地纹理共同占据注意力。已成功的生意、寻访、图鉴则先告诉玩家**有什么游戏对象、它们是什么状态、可以做什么**。它们也有渐变、材质、环境和细节，不能把“删光场景”当成修复。

最重要的 PASS/FAIL：每件 Asset 有游戏作用；在实际手机尺寸先读出形状和关键结构，再读材质；光影为本物件与接触服务；边界独立、状态/文字可更新；背景从属核心对象；整屏首先看见自然蛋窝、孵化状态与厨具操作，辅助陈设不得抢主次。**单 Asset PASS 与单背景 PASS 都不能代替整屏 PASS。**完整规则和失败示例见 `artifacts/kitchen-remaster-lv2-20260927/visual-forensics-source.md`；候选量尺见 `artifacts/kitchen-asset-language-gate-20260927/Checklist.md`。

## 4. 已确认的厨房产品原则

1. 当前正式四级厨房是产品 **Base**，不是废稿；任务是 Remaster，而非另做一款厨房。可以重新安排 UI/布局，也可以真正重绘背景，不必保留旧窗户或搁架坐标；但不能退化成完整 Cozy AI Interior Illustration。
2. 自然错落、互相遮挡的**一窝鸡蛋**必须保留；蛋窝/孵化是核心视觉和玩法主体，厨具仍是重要操作。不能改成规则格子、管理卡片或静态烘焙蛋图。
3. 调味、打扫、商店、仓库、手艺、帮助的位置与表现可以重设。状态、费用、数量和交互文字应与美术分层；保护现有玩法、经济、存档、解锁和五入口语义。
4. 四级身份必须清楚：Lv.1 茅草房；Lv.2 木房；Lv.3 砖房/小康厨房；Lv.4 别墅/精装厨房。先用 Lv.2 做视觉 Gate，获批后再扩展，不把概念四级图当已实施。
5. **UI、背景、Asset 作为一屏整体判断**。可爱、轻量、经营/挂机/收集感与明确操作关系要同时成立；完成度不由单张图或单个候选决定。

## 5. DO NOT REPEAT

- 不把整张 AI Kitchen Concept 当正式 Runtime 背景；也不把厨房做成管理后台、灰盒卡片列表。
- 不为“游戏化”机械删除所有场景，也不只靠减少纹理/渐变修“AI 味”。
- 不只生成独立 Asset 而忽略整屏；不只生成背景而脱离 UI/Asset 判断。
- 不为保守 Remaster 将任务缩成旧图轻微换皮；旧窗/搁架不是必须保留的空间约束。
- 不一次同时改背景、布局、Asset、信息架构后再倒猜原因；每轮 Gate 明确变量与判断对象。
- 不经用户 Gate 直接扩展 Lv.1/Lv.3/Lv.4；不把 `web/prototypes/` 或 `artifacts/` 误接到正式入口。
- 不把旧审计中的 placeholder、旧 APK、单次浏览器 QA、Concept PASS 写成当前正式发行状态；不向用户剧透新增收集内容。

## 6. 最新 Kitchen 判断与尚未执行的下一步 [PLAN / TODO]

Background A/B/C/D 仍显普通，差异有限，脱离完整 UI 难以选择。下一步准备回到 **Kitchen Creative Direction Exploration**：寻找厨房自身的核心视觉概念，达到“图鉴＝收藏册、寻访＝旅行地图、生意＝鸡宝篮子小铺”那样鲜明的记忆点，而不是继续问“木房背景怎么画”。

计划仅用 **Lv.2** 做约 **6 个真正不同**的 Creative Direction；可用完整 Mockup 作为 Concept Art，但**不是 Runtime Asset**。比较视觉中心、蛋窝/孵化、厨具与蛋的关系、可爱/幽默/经营感、世界观，以及四级升级如何围绕概念成长。用户先凭第一眼选择喜欢的方向。预定顺序：**Creative Direction → Final Lv.2 Mockup → Asset 拆分 → Runtime 1:1 复刻 → Lv.1/3/4 扩展**。这些步骤目前**均未执行**，不预选方向。

## 7. 剩余项目路线与偏好

|Work|状态与范围|
|---|---|
|**3A Kitchen Remaster**|当前所在阶段；先完成上节 Creative Direction Gate，再按选定方向落实 Lv.2 与四级。|
|**3B Farm Remaster + 必要页面/交互收敛**|厨房确认后再开始；此前 `docs/ui/scene-first-ia-audit.md` 和 `artifacts/scene-first-review/` 属早期提案/原型，尚未进入正式 Farm Remaster。|
|**4 全游戏 Visual / UX QA**|页面遍历、视觉一致性、交互、空/锁定/极端状态、手机尺寸、新旧功能回归。Work 2 的局部 QA 不等于此项完成。|
|**5 Android Build / APK / 真机验收**|构建并安装当前版 APK，核对存档、触摸、性能及用户实际游玩。历史 1.5.1 包不能代替当前树。开源相关素材重绘与版权清理暂缓，不阻塞个人使用版 APK。|

**用户审美与协作偏好：**喜欢可爱、轻量、有经营/挂机/收集感且有记忆点的页面；认可当前生意、寻访、图鉴最终方向和自然蛋位。不喜欢完整 AI 插画直接成为游戏页面，也不接受纯工具面板。场景可以丰富，但应服务游戏对象。希望先看 Concept 并确认，再拆 Asset 与 1:1 复刻；避免“优化”损害已有好设计；新增收集内容尽量不剧透。

## 8. 新会话优先读取路径

1. **本文件**；正式入口 `web/index.html`、`web/app.js`、`web/scene.js`、`web/kitchen-stages.js`，需要核对接入时再看 `web/production-art.js` / `web/runtime-assets.generated.js`。
2. 已完成交付：`docs/art/work1-delivery-status.md`、`docs/ui/ui-remaster-final-report.md`、`docs/ui/current-ui-remaster-audit.md`。
3. 厨房判断：`artifacts/kitchen-remaster-lv2-20260927/visual-forensics-source.md`、`docs/ui/kitchen-remaster-plan.md`、`artifacts/kitchen-background-direction-gate-20260927/README.md`、`artifacts/kitchen-asset-language-gate-20260927/Checklist.md`；再按需查看 Concept/Layout/Style 原文和图。
4. 发行/历史边界：`docs/archive/iterations/implementation-status.md`、`docs/quality/regression-and-ui-audit.md`、`docs/current-state-audit.md` 仅作阶段证据；遇到冲突，以当前代码、较新交付与本文件标出的时间顺序复核。

**交接时工作树：**本轮开始 `git status --short` 已有大量已修改的 Runtime/文档与未跟踪 Asset/Artifact。本文是本轮唯一预期新增文件；没有清理、提交、构建或修改正式 Runtime。当前浏览器/设备是否正在运行、当前手机安装何包及真实玩家档状态，本轮未重新现场确认。
