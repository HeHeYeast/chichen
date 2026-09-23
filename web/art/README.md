# 鸡宝厨房素材索引

当前接入厨房 v7 背景、v6 孵化床，以及农场 v8 四时段背景。工具为内置 image_gen；原版 `assets/` 未改写，生成原图复制到项目后通过独立图像和源矩形接入。角色和 UI 仍处于新旧素材混用、逐组优化阶段；当前实现待用户确认整体画风，不代表全量重绘或最终验收。

最新厨房画面方向见[UI／美术规范](../../docs/ui-ux.md)，当前v7背景与最新要求的差异尚未落地；版本号较新不自动表示已获审美认可。

## 当前厨具：统一三级外观（v15）

八类原版厨具共 24 个等级外观，使用 [图集 A](cookware-a-v15.png) 和 [图集 B](cookware-b-v15.png)；竹蒸笼沿用已有三级重绘，合计 27 个等级外观。厨房、商店、成长册、配方共用目录，按可见边界等比显示，并裁切图集避免相邻图案串入。旧原图保留。

[制作记录与完整提示词](provenance.md#cookware-v15-notes)。以下涉及首四种一级小样和高阶旧图的说明是历史记录，当前厨具以本节为准。

## 当前厨房：四级房屋与孵化设施

| 游戏等级（内部等级） | 房屋背景 | 独立孵化床／设施 |
|---|---|---|
| Lv.1（0）破旧茅草房屋 | [kitchen-stage-0-v7.png](kitchen-stage-0-v7.png) | [stage-bed-0-v6.png](stage-bed-0-v6.png)：散草窝 |
| Lv.2（1）略微整齐木板房屋 | [kitchen-stage-1-v7.png](kitchen-stage-1-v7.png) | [stage-bed-1-v6.png](stage-bed-1-v6.png)：木框颗粒草垫 |
| Lv.3（2）小康公寓厨房 | [kitchen-stage-2-v7.png](kitchen-stage-2-v7.png) | [stage-bed-2-v6.png](stage-bed-2-v6.png)：深木框、厚实白棉软垫 |
| Lv.4（3）豪华精装厨房 | [kitchen-stage-3-v7.png](kitchen-stage-3-v7.png) | [stage-facility-3-v6.png](stage-facility-3-v6.png)：粉色拱背软包、黄色软垫与奶油色柜身 |

初级收取容器使用 [basket-v4.png](basket-v4.png)；其余三级使用 [stage-vessels-v5.png](stage-vessels-v5.png) 的 `#0` 灰蓝金属收取槽、`#1` 银色金属碗、`#2` 奶油陶碗。版本号不同不表示停用，以此处及运行时代码引用为准。

[kitchen-stages.js](../kitchen-stages.js) 定义四级背景、设施、容器和布局，[manifest.js](manifest.js) 定义源矩形。背景原图按墙面、台面、下柜三段映射到游戏画面；孵化设施分层绘制，保留紧凑的 6×4 蛋阵及前排可见区。鸡宝、蛋、文字、按钮和动画独立于背景绘制。

制作与注册记录：

- [茅草屋与木板屋 v7](provenance.md#kitchen-stage-0-1-v7-notes)、[公寓与精装厨房 v7](provenance.md#kitchen-high-stages-v7-notes)
- [四级设施成长 v6](provenance.md#stage-growth-v6-notes)、[木框颗粒床 v6](provenance.md#stage-bed-1-v6-notes)、[白棉软垫 v6](provenance.md#stage-bed-2-v6-notes)、[最高级设施 v6 及备选稿](provenance.md#stage-facility-3-v6-notes)
- [v5 床垫与容器制作记录](provenance.md#stage-parts-v5-notes)：容器仍在使用，床垫已由上表 v6 替换

## 当前农场：v8 四时段

| 时段代码 | 当前背景 |
|---|---|
| 0 · 黎明 | [farm-dawn-v8.png](farm-dawn-v8.png) |
| 10 · 白天 | [farm-day-v8.png](farm-day-v8.png) |
| 20 · 傍晚 | [farm-dusk-v8.png](farm-dusk-v8.png) |
| 30 · 夜晚 | [farm-night-v8.png](farm-night-v8.png) |

完整提示词、原图来源、建筑点击区域、固定操作区域和检查记录见 [农场 v8 制作记录](provenance.md#farm-v8-notes)。[farm-theme.js](../farm-theme.js) 提供四时段资源和共用坐标，[farm-scene.js](../farm-scene.js) 绘制背景、动态鸡宝、建筑标识及修缮／收成入口。农场保留 830×568 世界、0～510 横向滚动范围，以及原有时段、角色位置和维护规则。

屋舍入口打开收成表，补给摊入口打开商店；农场神社已有固定入口、委托、签册与收藏回礼；远景建筑图本身不承担按钮。人物、文字和 UI 未烘焙进背景。农场中的角色继续使用下述新旧角色混合目录。

## 当前角色、图标与 UI 的覆盖范围

| 文件 | 用途与范围 |
|---|---|
| [chick-v4-0.png](chick-v4-0.png) | 鸡宝（C01），保留蛋形身体与短翅、短脚。 |
| [chick-v4-3.png](chick-v4-3.png) | 香煎鸡（C04），保留焦边、煎纹与半睁眼。 |
| [chick-v4-4.png](chick-v4-4.png) | 荷包蛋鸡（C05），保留蛋白轮廓、蛋黄与表情。 |
| [egg-v4.png](egg-v4.png) | 基础鸡蛋；裂纹和破壳效果由游戏绘制。 |
| [icons-v4-alpha.png](icons-v4-alpha.png) | 真实 alpha 八枚图标。首行：保温灯、平底锅、水煮锅、油炸锅；次行：厨房、农场、图鉴、商店。 |
| [manifest.js](manifest.js) | 图集源矩形、角色可见边界和基线定位；同时规范原版厨具的可见尺寸。 |

三只鸡宝与基础蛋的输入、提示词和透明度检查见 [角色素材记录](provenance.md#characters-v4-notes)。[catalog.js](../catalog.js) 只替换 C01、C04、C05；其余原171品种使用原版角色图；新增6点心鸡与16四时品种使用主题图集。厨具已统一27个等级外观，早期四个一级小样不再代表当前覆盖。食材、鸭蛋及商店中的原版蛋图标也继续使用原素材。

图鉴、收成账本、商店、设置和照顾手册使用运行时 HTML／CSS／SVG 界面，厨房和农场主场景使用 Canvas。按钮、文字、状态与版式在代码中统一；插图仍混用三只重绘鸡宝、八枚图标和原版角色／食材／厨具，尚不是一套全部重绘的 UI 素材。照顾手册内容为程序排版的中文说明，并配角色与厨具插图。

## 查看与验收

运行项目根目录的 `npm start` 后，可查看：

- [当前可玩版本与测试场景](http://127.0.0.1:4173/review)
- [历史 v4 素材与字体比较板](http://127.0.0.1:4173/web/art-board.html)：仅用于早期素材对照，不是当前完整索引
- [2026-09-08 返工前基线](http://127.0.0.1:4173/web/baseline-20260908/index.html?review=1)

生成原图、比较板及检查记录用于追溯；实际尺寸、动画和操作以当前试玩为评价依据。新增界面尚未获得用户整体画风验收。

## 停用试稿与历史资产

- **`icons-v4.png` 是废弃试稿，不得接入运行时或新页面。**当前使用 `icons-v4-alpha.png`。`stage-bed-1-v6-candidate-rgb.png` 也只是未采用候选稿，不是当前木框床。
- `kitchen-v2.png`、`kitchen-v3.png`、`kitchen-v4.png`、四张 `kitchen-stage-0-v5.png` 至 `kitchen-stage-3-v5.png`，以及 `kitchen-stage-3-v6.png` 是历史背景。当前四级房屋使用上表 v7。
- `nest-v4.png`、`nest-v4b.png`、`stage-beds-v5.png` 和 `stage-beds-v5b.png` 是历史巢／床试稿，已被当前厨房的 v6 设施替换；部分旧元数据保留用于追溯。
- `stage-facility-3-v6b.png`、`stage-facility-3-v6c.png` 是最高级设施备选稿；运行时使用无后缀的 `stage-facility-3-v6.png`。
- `ui-atlas.png` 是历史图集，保留作过程与基线对照，不用于当前游戏。`reference-gameplay.jpg` 是用户实机参考副本，只用于对照，不作为场景资产。
- [环境 v4 制作记录](provenance.md#environment-v4-notes)、[厨房背景 v5 制作记录](provenance.md#kitchen-backgrounds-v5-notes) 保留历史方案及制作过程；其中的“当前”仅指记录当时状态。

下方历史提示词入口只用于追溯停用试稿，不代表当前绑定。

历史背景与图集提示词见[制作记录](provenance.md#early-prompts)。

## 后续素材与内容扩展约定

- 品种稳定 ID 不因重绘改变。新素材通过目录层替换，避免破坏配方及现有存档。
- 原有鸡宝的轮廓、表情和品种识别特征保留，不能批量替换成同质化吉祥物。
- 当前覆盖范围以上方索引和运行时目录为准。后续角色、厨具和其他 UI 插图逐组统一，保留品种及等级差异，不把批量生成或代码接入当作画风验收。
- 后续新增玩法先确定独立规则、资源产出及存档版本迁移，再添加内容。当前没有引入战斗、抽卡或付费货币。
- 实际可玩版本优先作为视觉检查依据，生成效果稿不视为完成验收。


## 后续新增资产

6点心与三级蒸笼、16四时品种的来源见制作记录对应节；30手艺图标为SVG，见[图标模块](../skill-icons.js)和[已执行反馈检查](../../artifacts/skill-polish/delivery.md)。Android应用图标v2见[来源](../../artifacts/art-exploration/app-icon-20260919/notes.md)。
