# Work 2.5 — Scene-first 信息架构审计

日期：2026-09-27。状态：审计与设计提案，等待厨房／农场 Mockup Gate；未修改 Runtime，未开始 Work 3。本文修正 Work 2 对厨房、农场默认 KEEP 的判断。

## 结论

厨房与农场都需要重新组织主场景，不是只移动两个按钮。厨房的调味料和清洁入口应在 HUD 下方同一顶线，操作台成为中部主视觉，仓库／手艺／商店用场景物件承载，厨具架固定在操作台下方。农场需要将主要功能纳入首屏院落、提高旧伙伴的可读尺寸与接地阴影；不是新增作物或模拟玩法。

生意和寻访继续作为成熟参考。完整图鉴、配方、账簿、常客故事、项目、手艺与发现册保留自己的空间；短操作优先局部展开。旧采购与旧路线从手艺页中移回经营、寻访，保留旧规则和显式旧系统名称。

## 范围与证据

重新启动本机当前游戏，独立 Chromium contexts 与合成 fresh/mid/complete 状态，390×844 主视口。证据在 `artifacts/internal-scene-first/`：`runtime/`、`business-family/`、`journey-precision-states/`、`extra-runtime/`。这些目录全部内部使用，不公开新增收藏图像。当前源码与 Work 2 交付未修改；`source-before.json` 记录工作开始时的文件哈希。

主遍历 60 张截图，生意遍历 36 张，寻访遍历 40 张，另补四级厨房、清洁、农场两端、整修、神社、委托等。本轮是信息架构取证，不是 Work 3 全游戏 Visual QA。普通／空／锁定／完成不是单独页面。平台专属入口在表中明确标为 `source:`（当前处理函数核查）；不得将这些标记伪称为本轮新截图。这些入口的对应迁移仍纳入审计，不掩盖覆盖差异。

## 统计口径

- **Full Page**：当前主任务内容被替换，原场景不再可操作，需要显式返回。不是以 HTML 的 dialog 标签或有没有新 URL 判断。
- 一个任务面内部换页签／记录／地区／数据状态不重复计数：例如商店三个货架、地区与特殊目录、同一行程的准备／运行／归来。不同任务有自己的返回链路，才单列。
- 当前厨房升级是保留背景的大 Overlay，清洁和整修也是 Overlay；它们均不计整页减少。成熟生意菜单和主题目录已有 Sheet，也不冒领收益。
- 下表共 66 个任务面／局部操作条目，其中 Full Page **50**，提案保留 **31**。这不是将 Work 2 的 56 个状态组直接减成 31，两者统计口径不同。
- Full → Overlay **13**；Full → Inline **2**；并入既有任务面 **3**；取消独立观察导航 **1**。新增快捷库存／求签／陈列路径不会假装删掉完整收藏或仓库。
- 另有 **1 个 Android 专属后台帮助页**由当前源码核查，浏览器没有该入口，不计入 50；其 Inline 建议单列。未提前开展 Android 装机。
- 所有建议数字是**提案预估**，不是已实现效果；Gate 后按实际迁移重新计数。

|指标|当前|建议|
|---|---:|---:|
|独立 Full Page 任务面|50|31|
|24 个固定典型任务平均层级（含 Overlay）|1.63|1.42|
|同组任务平均 Full Page 深度|1.46|0.67|

包含弹层的层级下降有限：多数任务仍需一次打开。这轮主要收益是保持场景上下文、少走返回链和减少中间整页，不宣称点击数下降等于整页减少。

## 逐项路由判断

“减少跳转”是该任务从入口打开再返回的一次往返 Full Page 切换预估，局部开合不算整页跳转；不汇总成玩家实际日均收益。完整浏览入口保留时填 0，新增快捷路径单独说明。

|ID／当前入口|当前页面链路|建议链路|当前 → 建议|原因|少几次整页跳转／往返|证据索引|
|---|---|---|---|---|---:|---|
|title · 标题|启动→标题→游戏|保持|FULL → **FULL**|进入游戏的独立起点|0|extra-runtime/title|
|kitchen · 厨房|底栏→厨房|保持主场景，入口回到场景物件|FULL → **FULL**|核心经营空间，四级背景各自保留|0|runtime/01-kitchen|
|farm · 农场|底栏→农场→横向拖动找物件|院落首屏可见主要物件；保留可扩展横向空间|FULL → **FULL**|降低寻找入口成本，不新增产出系统|0|extra-runtime/farm-west|
|business · 生意|底栏→生意|保持成熟小铺|FULL → **FULL**|完整经营空间，不重套厨房布局|0|business-family/01-business|
|world · 寻访地图|底栏→世界地图|保持地图；旧路线成为地图内入口|FULL → **FULL**|独立地点选择空间|0|journey-precision-states/01-world|
|book-index · 图鉴总览|底栏→图鉴总览|保持索引册|FULL → **FULL**|长期浏览入口|0|runtime/11-book-overview|
|seasoning · 调味料|厨房→整页调味料→返回|顶部调味架→选择 Sheet→收起|FULL → **OVERLAY**|少量选择必须保留厨房上下文；多材料时内部滚动|2|runtime/02-seasoning|
|clean · 打扫|厨房→清洁确认→关闭|顶部扫具→状态与费用面板|OVERLAY → **OVERLAY**|已是弹窗，不虚报导航减少；优先纠正顶部位置|0|extra-runtime/cleaning|
|upgrade · 厨房升级|等级→升级预览面板→确认|房屋铭牌→升级预览 Sheet→确认|OVERLAY → **OVERLAY**|现有大面板仍露出场景；保留四级预览与原条件|0|extra-runtime/kitchen-upgrade|
|tools-inline · 厨具选择|厨房下方厨具架→开火核对|台下厨具架→核对；成长直接入口|INLINE → **INLINE**|滚动与选中属于场景内操作|0|extra-runtime/cook-confirm|
|cook · 开火与放弃当前批次确认|厨具→确认弹窗|保持核对；候选说明折叠|OVERLAY → **OVERLAY**|损失与费用仍需明确确认|0|extra-runtime/cook-confirm|
|allocation · 安排本锅收成|厨房→本锅收成整页→核对|收成篮→分配 Sheet→核对|FULL → **OVERLAY**|短期批次操作；分配去向与占用规则不变|2|extra-runtime/harvest-allocation|
|inventory · 完整仓库／收成表|厨房或农场→完整收成表|收成屋快捷 Sheet；需要批量时进入完整收成表|FULL → **FULL**|大库存、多行筛选与批量结算仍值得整页|0|runtime/34-inventory|
|inventory-item · 单品库存操作|收成表→个体操作档案→返回|点旧伙伴或仓库物件→单品 Sheet，数量 Inline|FULL → **OVERLAY**|库存操作不应借道完整角色档案；深度档案仍可展开|2|runtime/inventory-detail|
|repair · 农场整修|底部整修→确认|围栏工具箱→耐久与费用 Overlay|OVERLAY → **OVERLAY**|已经无需整页；调整到场景物件并保留读数|0|extra-runtime/farm-repair|
|shop · 商店货架（厨具／材料／其他）|厨房或农场→商店→货架页签|完整商店保留；常用补货在调味 Sheet 内切换模式|FULL → **FULL**|多品类搜索与比较有独立空间价值|0|runtime/27-shop-tools|
|shop-filter · 商店搜索／筛选／无结果|货架内搜索筛选|保持 Inline；未知名称继续隐藏|INLINE → **INLINE**|无新增路由|0|runtime/31-shop-empty|
|tool-growth · 厨具成长册|商店→成长册整页→返回|厨具物件长按或成长入口→成长 Sheet|FULL → **OVERLAY**|一件厨具的三个等级可在局部比较|2|runtime/28-tool-growth|
|material · 材料已知／未知详情|商店→材料详情整页→返回|货架物件→详情 Sheet→关闭回原滚动位置|FULL → **OVERLAY**|单物件属性与购买条件无需整页|2|runtime/30-material-detail|
|purchase-confirm · 购买／蛋种购买确认|商品→确认弹窗|保持原确认与原命令|OVERLAY → **OVERLAY**|不得把视觉收敛变为免确认购买|0|extra-runtime/shop-other|
|skills · 手艺主册|厨房→手艺|挂在厨房的手艺册→完整成长空间|FULL → **FULL**|五分支、试配、应用属于深度系统|0|runtime/06-workshop|
|skill-detail · 技能详情|手艺→技能整页→返回|手艺贴纸→详情 Sheet→加入同一草稿|FULL → **OVERLAY**|保留分支位置和未应用草稿|2|runtime/07-skill-detail|
|skill-help · 手艺点数帮助／重配确认|手艺→弹窗|保持 Overlay|OVERLAY → **OVERLAY**|解释与确认不增加路由|0|runtime/73-skill-applied|
|old-orders · 旧采购|厨房→手艺→旧采购；生意亦可进入|生意订单页内保留“旧采购”分区|FULL → **MERGE**|从成长系统移出采购；旧章节和任务命令不变|2|runtime/08-legacy-order|
|old-chapter · 旧采购已完成章节|旧采购→章节整页→返回|订单历史→阅读 Sheet|FULL → **OVERLAY**|可滚动阅读，保留所在章节定位|2|runtime/70-story-complete|
|old-trip · 旧路线准备／运行／归来|手艺→旧路线；地图→旧路线|寻访地区工作区内切换“旧路线”模式|FULL → **MERGE**|入口回归寻访；保留旧路线独立规则与状态|2|runtime/09-legacy-route|
|old-team · 旧路线同行选择|旧路线→选人整页→返回|当前行程→同行 Sheet|FULL → **OVERLAY**|选择后原位返回；不改候选条件|2|runtime/10-legacy-team|
|observation · 旧观察／已知未知方法|开火候选或品种→观察整页|伙伴档案内“观察记录”折叠区，开火内可读 Sheet|FULL → **REMOVE NAVIGATION**|保留观察内容与知识权限，取消第三套详情路由|2|runtime/71-observation|
|species · 品种图鉴|图鉴→品种|保持完整浏览|FULL → **FULL**|多记录搜索、分页与收藏浏览|0|runtime/12-species|
|species-filter · 品种筛选／搜索|品种内筛选|保持 Inline|INLINE → **INLINE**|筛选不是独立页面|0|runtime/14-species-empty|
|species-detail · 已知／未知伙伴档案|品种或收藏→档案|保持深度档案；单次库存从场景直接管理|FULL → **FULL**|不移除角色阅读空间；未知身份继续保护|0|runtime/13-known-detail|
|collection-theme · 成熟主题收藏|图鉴收藏→主题册|保持|FULL → **FULL**|已成熟的收藏浏览隐喻|0|extra-runtime/theme-collection|
|collection-directory · 主题搜索目录|主题页→目录弹层|保持 Overlay|OVERLAY → **OVERLAY**|当前已经是局部面板|0|extra-runtime/theme-directory|
|collection-index · 地区／特殊收藏目录|收藏分类→地区或特殊目录|保持目录，各类别原位切换|FULL → **FULL**|同一浏览结构的分类只计一个任务面|0|runtime/17-region-collection|
|collection-detail · 地区／特殊收藏详情|目录→详情|保持深度收藏页，阶段条件 Inline|FULL → **FULL**|属于目标与长期记录阅读|0|runtime/18-region-detail|
|lore · 见闻（材料／地区／特殊／日历入口）|图鉴→见闻→筛选内容|保留见闻页；短状态解释 Inline|FULL → **FULL**|记录跨系统聚合，不塞入厨房|0|runtime/23-lore|
|mementos · 纪念物收藏与完整管理|农场陈列架→整页目录；图鉴亦可进入|图鉴保留完整纪念物页；农场仅打开陈列 Sheet|FULL → **FULL**|浏览收藏与摆放是不同任务，不删收藏空间|0|runtime/79-mementos-empty|
|memento-slot · 陈列位选择|完整纪念物页中的下拉项|场景空架→陈列 Sheet→点空位 Inline 选择|INLINE → **OVERLAY**|新增短路径；不是删除一个已有整页|2|runtime/79-mementos-empty|
|papers · 收藏纸页|收藏类别→独立纸页内容|收藏目录内“已得纸页”折叠区|FULL → **MERGE**|记录与目录同属一个阅读上下文|2|runtime/22-papers|
|recipes · 配方册列表|品种→配方册|保留|FULL → **FULL**|多记录比较与浏览需要空间|0|runtime/55-recipes|
|recipe-detail · 配方详情与准备|配方册→配方详情|保留；回到发起入口|FULL → **FULL**|必要条件和准备动作需要完整空间|0|runtime/56-recipe-detail|
|business-menu · 菜单／备货／开张帮助|生意物件→局部面板|保持现有 Sheet／Inline|OVERLAY → **OVERLAY**|成熟例子，不能计作新收敛收益|0|business-family/ledger-help|
|ledger · 账本／营业结果|生意→账本|保持独立账本|FULL → **FULL**|对账与结果阅读，不重复结算|0|business-family/ledger-active|
|orders · 采购订单列表|生意→订单|保持订单簿，吸收旧采购入口|FULL → **FULL**|完整经营任务空间|0|business-family/02-orders|
|order-fulfil · 交付／预留／供查看选择|订单→整页选择→确认→返回|订单卡→数量或对象 Sheet→确认|FULL → **OVERLAY**|单个订单执行面；保留所有扣货确认|2|business-family/orders-delivery-disabled|
|regulars · 常客列表|生意→常客|保持|FULL → **FULL**|收藏阅读入口|0|business-family/regulars-unknown|
|regular-detail · 常客详情／故事阅读|常客列表→详情；故事在详情内展开|保持|FULL → **FULL**|故事阅读不压成小弹窗；展开不另算页面|0|business-family/regulars-story|
|projects · 项目列表|生意→项目|保持|FULL → **FULL**|长期经营筹备空间|0|business-family/04-projects|
|project-detail · 项目阶段详情|项目→详情|保持|FULL → **FULL**|跨阶段进度与条件适合整页|0|business-family/projects-ready|
|project-delivery · 项目交付／展册画像选择|项目详情→整页选择→确认→返回|项目详情→任务 Sheet→确认→回原阶段|FULL → **OVERLAY**|保留库存与永久画像的语义区别，长内容面板内滚动|2|business-selection/project-delivery|
|region · 寻访地区／关注／运行／归来|地图→地区；运行与归来复用行程空间|保持地区与行程工作区，旧路线以明确模式接入|FULL → **FULL**|同一目的地的状态变化不另算页面|0|journey-precision-states/03-region-team|
|journey-team · 寻访同行选择|地区→同行整页→返回|座位→同行 Sheet→原位返回|FULL → **OVERLAY**|选择任务短，保留地点与关注选择|2|journey-precision-states/04-companions|
|journey-confirm · 出发核对|地区→整页核对→出发|地区底部展开核对；关键扣留提示必须确认|FULL → **INLINE**|核对保留、独立导航层取消|2|journey-precision-states/05-confirm|
|journey-record · 地区发现册／标本／方法|地区→发现册；方法原位切换|保持完整发现册与内部 Inline|FULL → **FULL**|深度记录浏览，未知信息不提前公开|0|journey-precision-states/discovery-record|
|journey-help · 行程详情／帮助／带货小面板|地图或地区→Overlay|保持现有 Overlay|OVERLAY → **OVERLAY**|已有上下文保留，不重复设计|0|journey-precision-states/travel-detail|
|shrine · 神社求签／签册／回礼主容器|农场→神社整页（含页签）|神社物件→求签 Sheet；签册与回礼进入完整收藏册|FULL → **FULL**|主容器仍因深度收藏保留；日常求签从快捷路径进入|0|extra-runtime/shrine-book|
|sign-detail · 签册详情|神社签册→签详情|保持深度收藏详情，来源返回|FULL → **FULL**|收藏内容有完整阅读价值|0|extra-runtime/shrine-sign|
|activities · 常驻委托簿|农场神社委托→委托簿|神社→委托簿；保留完整列表|FULL → **FULL**|多条长期条件与奖励需要浏览|0|extra-runtime/activities|
|activity-detail · 单条常驻委托|委托簿→详情→返回|委托卡 Inline 展开条件；复杂阅读可延展 Sheet|FULL → **INLINE**|避免每封来信都换整页；权利与领取规则不变|2|extra-runtime/activity-detail|
|journal · 寻宝日历／四时手记|设置、见闻或神社→日历手记|保留一个手记空间和内部页签|FULL → **FULL**|按时间浏览的深度内容|0|runtime/52-calendar|
|settings · 设置／音量／存档工具|HUD→设置整页|齿轮→工具 Sheet，存档危险动作仍单独确认|FULL → **OVERLAY**|设置保持工具可读性；长内容内部滚动|2|runtime/41-settings|
|notification-help · 后台提醒帮助|设置→整页帮助|设置内部展开专门说明|ANDROID ONLY → **INLINE**|仅 Android 入口，网页当前数量不计入；建议原位展开|2|source:settings-ui.js:50|
|manual · 九章帮助|厨房或设置→照顾手册|场景 ? 打开当前章节 Sheet，仍可切换全部章节|FULL → **OVERLAY**|信息保留，按当前上下文定位|2|runtime/help-0|
|save-dialog · 存档导出／导入／通知反馈|设置内动作→原生文件操作或确认|保持原有确认、错误与原生权限流程|OVERLAY → **OVERLAY**|系统文件选择器不算游戏 Full Page|0|extra-runtime/save-feedback|
|recovery · 异常存档恢复|启动→恢复界面|保持专用阻断恢复页|FULL → **FULL**|异常恢复必须清晰，不能叠在可操作场景上|0|extra-runtime/recovery|
|nav · 固定底部导航|厨房／农场／生意／寻访／图鉴|五入口、顺序与选中样式保留|INLINE → **INLINE**|入口稳定，按任务收敛子导航|0|runtime/01-kitchen|

## 典型任务深度明细

以表述中的起点为 depth 0。D 为一次完成任务途中最深的嵌套上下文，含弹层；F 只数离开当前完整任务面的层级。同级分类切换、状态刷新、关闭返回不增加 D；单独明确的确认不作为页面任务面另计（两侧采用同口径）。本组等权抽样，不是玩家遥测，不推断使用频率。运行／归来等状态采用相同路由，不另加权。

|典型任务|D 当前→建议|F 当前→建议|
|---|---:|---:|
|厨房选调味料|1 → 1|1 → 0|
|厨房打扫|1 → 1|0 → 0|
|厨房开火确认|1 → 1|0 → 0|
|厨房查看厨具成长|2 → 1|2 → 0|
|商店查看并购买材料|2 → 2|2 → 1|
|调味面板内补常用材料|3 → 1|3 → 0|
|厨房查看房屋升级|1 → 1|0 → 0|
|厨房管理单品库存|2 → 1|2 → 0|
|批量出售库存|1 → 1|1 → 1|
|安排本锅收成|1 → 1|1 → 0|
|农场整修|1 → 1|0 → 0|
|农场查看今日求签|1 → 1|1 → 0|
|农场更换陈列|1 → 1|1 → 0|
|手艺查看技能详情|2 → 2|2 → 1|
|从厨房进入旧采购|2 → 2|2 → 2|
|旧路线挑选同行（从寻访根）|2 → 2|2 → 1|
|地区寻访准备并核对（从地图根）|2 → 2|2 → 1|
|查看图鉴伙伴档案|2 → 2|2 → 2|
|查看配方详情（从品种）|2 → 2|2 → 2|
|查看地区收藏详情（从收藏目录）|1 → 1|1 → 1|
|查看营业账本|1 → 1|1 → 1|
|采购订单选择交付（从生意根）|2 → 2|2 → 1|
|项目选择交付（从生意根）|3 → 3|3 → 2|
|查看设置内帮助章节|2 → 1|2 → 0|

## 场景与交互约束

### 厨房

1. HUD 下方同一 y 基线安放调味架和扫具，图像、简短状态、点击区域整体对齐；不得只把透明命中框移上去。
2. 四级背景维持独立身份：茅草房、木房、砖房、别墅。复用现有四张画稿和各级独立操作台；材质、窗户、墙面、台面保持明显递进。当前源码第三级标题偏“公寓”，本提案只按用户指定的砖房阶段表达，不更改升级级别或数值。
3. 控件与背景锚点分开：厨房顶部工作区、墙上工具物件、操作台、厨具架四个层级。长屏增加台面工作空间，不能让 HUD 或入口随着空白飘远。
4. 调味和补充已辨认材料在同一 Sheet 切换模式，完成后仍见当前批次。完整商店继续可达，未知材料不在快捷补货泄漏。
5. 收取以角色回篮／数量反馈表达；安排本锅收成是局部任务，原分配、扣货和开火规则不变。

### 农场

1. 由大片空地与右侧隐藏入口，改为收成屋、商店、神社、空陈列架围绕草地的院落构图。原横向漫游可作为景深延展，不取消原功能。
2. 伙伴显示数量是视觉抽样，不等于库存总量；不增加新角色，不改变移动和库存规则。原型只用已知旧角色。
3. 耐久读数附在围栏工具箱；低耐久反馈绑定该物件。收成屋可先开单品 Sheet，批量管理另开完整收成表。
4. 神社有两个意图：日常求签留在场景；签册、回礼、委托保留完整浏览。关闭求签返回农场，不先回委托再回农场。
5. 空陈列位直接在架上操作。图鉴内完整纪念物目录继续保留；空状态不画虚构奖励。

### 四个主入口的一致性

共同使用现有暖纸色、棕色轮廓、低饱和绿与蜂蜜黄、短标签和五个固定底栏入口。厨房强调台面、农场强调院落、生意强调货篮与纸菜单、寻访强调地标与路径。不给生意／寻访补一个相同大顶栏；厨房／农场 HUD 只共享必要余额、等级、设置语言。底栏顺序和结果提示维持现有契约。

## Overlay 的实际约束

- 普通 Sheet 保留至少约 30% 的主场景或父任务视觉；短屏可长到更高并内部滚动，但仍是同一局部事务，不伪装为新整页。
- 普通 Overlay 只保留一层。补货／详情在同一面板内切换模式；确有扣货风险时允许现有确认浮在面板上，取消必须回到未丢失的草稿。
- 关闭／返回／Escape 回到调用入口，恢复焦点、滚动、地点、页签、草稿，不默认跳厨房；遮罩阻止背景误操作，结果反馈后才解除。
- 单品快捷面板与完整仓库复用原库存、留一、预留、售价、经营奖励和确认接口，不生成第二套库存算法。
- 旧系统合并的是入口和承载页，不是状态机；旧路线与地区寻访继续显示不同模式，禁止借 UI 收敛合并 RNG、行程或奖励。
- 锁定／空／已完成均在原任务面表达；原生系统权限／文件选择继续交给系统，不另造游戏整页。

## 素材与 Mockup Gate

厨房用现有四级背景、操作台、旧厨具、蛋以及五件独立场景物件制作 HTML/CSS 原型；五件新物件由内置 ImageGen 生成 Asset Sheet，按 Alpha 边界分出五个 SVG 视窗，仅用于评审（`props-segmentation.json`），没有接入 Runtime；农场院落画是内置 ImageGen 生成的无角色场景参考，叠加现有旧伙伴、HUD 和独立面板。提示词与来源保存在 `artifacts/scene-first-review/imagegen-prompt.txt`。不是全页 Runtime 贴图。

Gate 后如采纳农场方向：需要独立远景／草地、收成屋、摊位、神社、三槽空架、围栏工具箱的 Asset Sheet → segmentation → 原尺寸与透明边界检查 → 层级接入。当前参考图的陈列架仅表达位置，正式组件必须是三个可独立命中的槽位，不能照搬两层木架造型冒充三槽。昼夜四套光照继续保留；阴影、走动区域、可达入口需随新布局校准。

厨房可先复用现有层；若物件表现仍像图标，需要生成少量独立调味托盘、壁挂扫具、手艺册、收成篮素材，不重做角色，不把整幅厨房重新生成为唯一背景。四级厨房采用同一语义锚点但允许各级物件材质变化。

Mockup 的厨具价格／时长从现有 cookInfo 只读计算；余额与批次进度为隔离展示状态，交互只示意开合，不执行购买／出售／解锁，也不写任何存档。需确认：厨房主场景感、顶部对齐、四级递进、农场完成度、页面收敛合理性。

## 确认后的执行顺序（尚未执行）

1. 固化批准的厨房／农场构图与素材清单；先完成四级厨房的顶部布局和操作台锚点。
2. 实现有来源返回的局部面板容器，再迁移调味、材料、技能详情和单品库存，逐项验证草稿与确认。
3. 拆分并接入农场场景层、昼夜、角色接地与命中区域；迁移修缮、日常求签、陈列快捷操作。
4. 合并旧采购、旧路线、纸页与观察入口；保留深度整页，逐条检查关闭返回目的地。
5. 按本表复算实际 Full Page 与深度，执行本 Work 2.5 的 Runtime Screenshot QA，再写 `docs/ui/scene-first-remaster-report.md`。未实现前不产出“最终完成”报告。

保护经济、RNG、配方、解锁、存档 Schema、身份、收藏、营业与寻访规则。逻辑缺陷仅记录，不能借此重写。人工 Gate 前暂停 Runtime 实施；本 Work 不启动 Work 3。

## Gate 附件

- `artifacts/scene-first-review/main-scenes.png`：厨房与农场 Mockup。
- `artifacts/scene-first-review/four-kitchen-levels.png`：四级厨房，四张原有独立背景。
- `artifacts/scene-first-review/interaction-examples.png`：调味、库存、空陈列的局部交互。
- `artifacts/scene-first-review/four-scenes-overview.png`：两张新 Mockup 与两张成熟 Runtime 同框。
- `prototype-review.json`：原型开合、模式切换、数量、Escape、小屏与顶部对齐检查。原型检查不等于 Runtime 已实施。
