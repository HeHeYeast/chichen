# 美术来源与制作记录

这些记录用于复现提示词、透明边界与图集裁切，不是当前设计或TODO。各节中的“当前／后续”均限定于制作当时；现用／停用绑定见[资源索引](README.md)，美术方向见[UI规范](../../docs/ui-ux.md)。完整提示词与来源不删，避免丢失独有信息。

<a id="characters-v4-notes"></a>

## 厨房样板：鸡宝素材 v4

生成日期：2026-09-08。使用内置 imagegen，未使用 API/CLI，未用脚本处理、裁剪、抠图或重绘图片。脚本仅复制文件、读取像素统计以验证真透明背景。原始素材保留。

### 输入与范围

- 原鸡宝：`assets/png/Character/character_0/character_0_0_0_0.png`
- 原香煎鸡：`assets/png/Character/character_0/character_0_3_0_0.png`
- 原荷包蛋鸡：`assets/png/Character/character_0/character_0_4_0_0.png`
- 用户风格参考：`C:/Users/管啸野/AppData/Local/Temp/codex-clipboard-42c718dd-27bd-40e7-81cb-d0de25c384c2.png`。仅参考平面色块、清楚轮廓和 Q 版比例，没有复制参考角色。
- 先制作普通鸡宝，再将它作为后两只角色的统一风格锚点。
- 保留原来的蛋形轮廓、喙、脚、食物特征与差异表情。只交付三种角色的静态姿态，不包含逐帧动画或其他物种。

### 输出与 alpha 验证

坐标为 `[left, top, rightExclusive, bottomExclusive]`，主体边界按 alpha > 64 测量。生成图带少量极低透明度边缘杂点，因此不建议直接使用 alpha > 0 自动裁边。

| 文件 | 尺寸 | 主体边界 | alpha = 0 像素数 | 原 120×120 素材主体边界 |
|---|---|---|---:|---|
| `chick-v4-0.png` | 1312×1199 | `[234,186,1067,1002]` | 1,064,728 | `[21,39,95,120]` |
| `chick-v4-3.png` | 1350×1165 | `[141,25,1247,1043]` | 734,216 | `[21,48,98,119]` |
| `chick-v4-4.png` | 1312×1199 | `[94,13,1207,1145]` | 623,129 | `[24,53,94,120]` |
| `egg-v4.png` | 1312×1199 | `[324,188,988,990]` | 1,155,785 | `[23,37,98,120]` |

三张最终 PNG 均有真实 alpha。香煎鸡与荷包蛋鸡首轮生成了画进图片的棋盘格背景，经像素格式检查发现后，没有导入项目；再次使用内置 imagegen 执行背景提取后才保存最终文件。

追加基础蛋素材 `egg-v4.png` 同样通过真实 alpha 检查。输入为 `assets/png/Egg/egg_0_0_0.png` 与本轮鸡宝风格锚点。只包含完整蛋的基础姿态；裂纹和破壳由消费端分别表现。

渲染建议：使用上述主体边界作为显式 sourceRect，等比缩放到原角色可见区域，底部对齐原脚部基线。避免将整张大图直接视作原 120×120 透明画布，否则角色尺寸和空白比例会改变。原鸡宝边界 top=39 含少量较高处像素，实际主要头部更低；可在实机评审中按可见轮廓微调目标高度，保持 6×4 密集队列中的识别性。

美术检查：当前轮廓、暖黄/蛋白/橙色配色与主要明暗关系已经统一；生成器仍引入轻微色面渐变，未达到完全纯色的严格平涂。使用小尺寸实机表现验收，勿将大图精度本身视作通过。

### 最终提示词

#### 普通鸡宝：生成

Use case: style-transfer. Asset type: transparent PNG game sprite, one character only. Image 1 is the edit target, the original 鸡宝 chick; Image 2 is a style reference only, do not copy any of its creatures. Redraw the chick from Image 1 with the same egg-shaped slightly asymmetrical squat body silhouette, same little beak pointing subtly right, simple black bead eyes and calm vaguely clueless expression, small curved wings, tiny orange feet. Keep the recognizable identity and body proportions of Image 1, no added comb, costume, props or big anime eyes. Production flat 2D chibi game sprite: clean confident dark warm-brown outline, approximately #553c2b, pale buttery cream body, pale orange beak and feet, just one crisp warm light-ochre shadow shape on lower right, one small clean cream light shape. No gradients, paper grain, glow, glossy surfaces, realistic feathers, thick sticker border, drop shadow, text or environment. Large centered whole character with every foot visible and space around, transparent background with real alpha. The silhouette, facial features and body color must remain readable when drawn only 38 pixels high in a tight game grid. Match the bold simplified cut-out color shapes in the reference but preserve the original chicken design.

#### 香煎鸡：生成

Use case: style-transfer. Asset type: single transparent PNG game sprite. Image 1 is the original fried chicken creature to redraw. Image 2 is the approved rendering-style anchor for this exact game sprite set. Redraw Image 1 faithfully in Image 2's visual language. Preserve Image 1's smug sleepy half-lidded eyes, small orange beak, egg-shaped plump body, little wings, tiny orange feet, golden fried crust around the lower sides and top edge, three diagonal orange grill stripes across the lower cream belly. Keep original silhouette and expression distinct from the plain chick. Use the same warm brown outline weight, same face-feature scale and tiny feet of Image 2, but with toasted golden ochre edges and orange sear marks. Main body pale buttery cream, outline dark warm brown approximately #553c2b, one flat warm shadow shape only. Simple confident flat 2D chibi game art with clean color areas. Avoid noisy crust textures, grain, gradients, glossy highlights, fuzzy feathers, photorealism, 3D, sticker outlines, glow, shadows beneath, text, accessories and environment. The whole character must be fully visible, centered at large size with generous transparent margins, actual alpha transparent background. Will be rendered at only 38 px high, therefore use clear large simple sear marks and readable smug expression.

#### 香煎鸡：最终背景提取

Use case: background-extraction. Remove the entire pale gray and white checkerboard background from this image and output a genuinely transparent RGBA PNG cutout of the chicken. Preserve the chicken exactly, including its brown outline, sleepy eyes, golden toasted edge, three orange belly stripes, its little feet, color, pose, proportions and framing. Outside the chicken silhouette alpha must be zero. Do not draw a checkerboard or any background color. Do not add any shadow, glow, new artwork or text. This is a production game sprite background removal, not a preview on a transparency grid.

#### 荷包蛋鸡：生成

Use case: style-transfer. Asset type: single transparent PNG game character. Image 1 is the edit target: original 荷包蛋鸡 fried-egg chick. Image 2 is the style anchor for this same game's new sprite family. Redraw only Image 1 in Image 2's clean bold flat 2D chibi style. Preserve its identity: irregular egg-white shaped plump body with slightly scalloped sides; small orange beak; two tiny slightly worried eyes with diagonal expressive brows, mild grumpy worried expression; large round orange-yellow yolk belly; two little white wings folded in front and orange feet. Its white body must remain a fried egg, not a generic yellow chicken. Match Image 2's warm dark-brown outline weight (approximately #553c2b), simple face size, rounded silhouette and tiny feet. Use warm white egg-white, a single flat light-cream shadow shape, bright orange yolk and feet; very subtle narrow golden toasted rim. No gradients, grain, paper texture, 3D, glossy highlights, shadows beneath, soft fuzzy edges, objects, costume, environment or text. Clear forms designed to read at 38 pixels high in a mobile game. Whole body and both feet fully visible, large centered single sprite with generous transparent margins. Output true transparent RGBA PNG with alpha=0 everywhere outside character; absolutely do not paint checkerboard pattern or solid background.

#### 荷包蛋鸡：最终背景提取

Use case: background-extraction. Remove every part of the pale gray/white checkerboard background. Output this one fried-egg chicken as a truly transparent RGBA PNG game sprite with alpha=0 outside its silhouette, including any space between its feet. Preserve the character drawing exactly: warm-brown outline, scalloped white body, worried brows, black small eyes, orange beak, golden yolk belly held by folded wings, orange feet. Preserve its pose, proportions and colors. Do not draw checkerboard squares or any background at all. No colored background, no new shadow, no glow, no text. This is actual alpha background removal for production use.

### 原始生成文件

- 鸡宝：`C:/Users/管啸野/.codex/generated_images/01a07e5c-7438-7871-8b19-5118355feb69/exec-b1677a87-022d-401a-956e-6d6f8019be25.png`
- 香煎鸡最终：`C:/Users/管啸野/.codex/generated_images/01a07e5c-7438-7871-8b19-5118355feb69/exec-7c265960-2890-482e-9e2c-cffc0c8253dc.png`
- 荷包蛋鸡最终：`C:/Users/管啸野/.codex/generated_images/01a07e5c-7438-7871-8b19-5118355feb69/exec-0f0d8c45-df12-4373-9642-1797f7d01143.png`
- 基础蛋：`C:/Users/管啸野/.codex/generated_images/01a07e5c-7438-7871-8b19-5118355feb69/exec-2eb09618-7a90-43e8-9d37-4ffb06fd06eb.png`

#### 基础蛋：最终提示词

Use case: style-transfer. Asset type: one transparent PNG egg game sprite. Image 1 is the original egg silhouette to preserve. Image 2 is this game family's finished chick rendering style; match only its warm brown outline and flat clean color language. Redraw the egg from image 1: a plain upright slightly asymmetrical rounded egg, narrow rounded tip, wider rounded bottom, warm cream-white fill, confident dark warm-brown outline approximately #553c2b, one simple crisp pale butter-yellow shadow crescent at lower right, one small clean ivory highlight near upper left. Keep it simple enough to read at 35 pixels high in a tight 6 by 4 egg tray. No face, feet, cracks, patterns, decorations, texture, grain, gradient, realistic shading, 3D, glow, shadows beneath, text or environment. Entire egg visible centered at large size with generous transparent margins. Output a real transparent RGBA PNG, alpha zero outside egg. Do not draw a checkerboard pattern, transparency-preview grid, white background, or any other background pixels; actual empty transparent alpha only.


<a id="cookware-v15-notes"></a>

## 厨具统一外观 · v15

2026-09-19，使用内置 imagegen，以 `icons-v4-alpha.png` 为画风参考、八类原版 Lv.3 图片为轮廓参考，生成两个透明图集。原图保留，图集使用实测可见边界裁切显示，不拉伸；厨房、商店、成长册和配方共用映射。

- `cookware-a-v15.png`：按行保温灯、平底锅、水煮锅、油炸锅；按列 Lv.1、Lv.2、Lv.3。
- `cookware-b-v15.png`：按行烤箱、炖锅、烧水壶、面包机；按列 Lv.1、Lv.2、Lv.3。
- 竹蒸笼沿用已有三级重绘。品种 ID、升级条件、费用和耗时不变。

### 最终生成提示词

#### 图集 A

Create a production-ready TRANSPARENT RGBA sprite atlas for the existing cheerful chibi kitchen game. Use the first reference ONLY as style reference: warm dark brown smooth bold outlines, rich honey/coral/blue palette, simple cel shading and cream highlights, friendly rounded appliance silhouettes. Do not include its navigation icons. Exactly 12 isolated cookware icons arranged in a strict equal 3 COLUMN by 4 ROW grid. No text, no numbers, no labels, no grid lines, no tiles, no background, no exterior ground shadows. All empty space genuinely transparent. Every icon centered within its cell with generous 12% padding, never crosses cells. Consistent optical scale, slight front/right three-quarter view, readable at 45 px. Columns are increasingly upgraded levels 1,2,3, distinct silhouettes/functional additions not merely recoloring. Avoid faces, characters or unrelated items. Preserve cookware identity. Atlas A. Row 1 heat lamps: basic yellow curved desk warming lamp over one egg, upgraded red dome warmer with stronger support, advanced rectangular silver heat reflector lamp on short bracket (last-column reference image 2 defines the silhouette). Row 2 frying pans: basic orange open frying pan, deeper mint saute pan with long wooden handle, dark blue deep saute pan with GLASS DOMED LID and side helper handle (reference 3 silhouette). Row 3 boiling pots: basic blue round pot with two handles and lid, taller teal stockpot with lid, tall shiny silver stockpot with black side handles and domed glass lid (reference 4 silhouette). Row 4 deep fryers: basic coral countertop electric fryer with open raised mesh basket and long handle, larger cream/red fryer with raised hinged lid and basket, squared silver professional countertop fryer with open hinged lid and dark basket (reference 5 silhouette). Row layout exact; transparent background.

#### 图集 B

Create a production-ready TRANSPARENT RGBA sprite atlas for the existing cheerful chibi kitchen game. Use the first reference ONLY as style reference: warm dark brown smooth bold outlines, rich honey/coral/blue palette, simple cel shading and cream highlights, friendly rounded appliance silhouettes. Do not include its navigation icons. Exactly 12 isolated cookware icons arranged in a strict equal 3 COLUMN by 4 ROW grid. No text, no numbers, no labels, no grid lines, no tiles, no background, no exterior ground shadows. All empty space genuinely transparent. Every icon centered within its cell with generous 12% padding, never crosses cells. Consistent optical scale, slight front/right three-quarter view, readable at 45 px. Columns are increasingly upgraded levels 1,2,3, distinct silhouettes/functional additions not merely recoloring. Avoid faces, characters or unrelated items. Preserve cookware identity. Atlas B. Row 1 ovens: small coral countertop toaster oven with glass door and two right-side knobs, larger mint oven with broad window and side controls, substantial dark charcoal/silver professional oven with upper control panel and visible two inner racks (reference 2 silhouette). Row 2 stewing pots: rounded brown clay stew pot with lid and two handles, cream ceramic electric slow cooker with domed lid and handles, tall silver electric pressure/slow cooker with black handles and top knob (reference 3 silhouette). Row 3 kettles: copper traditional round stovetop kettle with arched handle, tall teal electric kettle with spout and top handle, compact white electric kettle with black overhead handle and dark inset front indicator (reference 4 silhouette). Row 4 bread makers: small cream/coral two-slot toaster with a bread slice, mint countertop bread machine with hinged lid and a small control panel, warm yellow upright glass-front bread baking cabinet with two visible horizontal shelves and rounded side control dial (reference 5 silhouette). Row layout exact; transparent background.


<a id="environment-v4-notes"></a>

## 厨房场景素材制作记录 · 2026-09-08

使用内置 imagegen 生成独立部件，在游戏画布中排列、裁切和分层。原版资源没有改写。以下记录制作约束和生成来源，不把生成成功等同于美术验收通过。

| 接入文件 | 生成来源文件 | 制作约束 |
|---|---|---|
| kitchen-v4.png | exec-87f10a68-0851-4174-8f2e-637b47a0609a.png | 9:16 明亮厨房背景；浅绿橱柜、奶油墙面、蜂蜜黄台面；中央留空；不包含鸡宝、巢、篮或界面文字。 |
| nest-v4b.png | exec-4a22daf1-4e5c-4e18-9b61-6884dc77e631.png | 俯视圆角矩形巢，薄边、宽阔内部，适配四行六列；真实透明背景。 |
| basket-v4.png | exec-41277f1a-5560-41ab-83ce-65e3ce48a61d.png | 无提手的矮收取篮；明确开口与前壁；减少编织细节，透明背景。 |
| icons-v4-alpha.png | exec-fecad7c1-7f0d-490d-adc1-6c6b872005c5.png | 4×2 八枚透明图标；上排四厨具，下排厨房、农场、图鉴、商店；暖棕轮廓、简化明暗。 |

上述源图位于本机生成目录 `C:/Users/管啸野/.codex/generated_images/01a07b82-6e9d-7d83-bd7c-2b233c81beb4/`；运行时使用项目内副本，不依赖该目录。

### 甄别与返工

- 首轮椭圆巢在实际满鸡状态下容不下末行，改为薄边圆角矩形。旧稿 `nest-v4.png` 停用。
- 首轮图标将棋盘格画进 RGB 图片，没有真实透明通道；提取尝试也未成功。重新生成真实 RGBA 图集，旧稿 `icons-v4.png` 停用。
- 当前巢外缘仍有浅色雾边，运行时用圆角路径裁掉外侧；角色的可见主体通过 `manifest.js` 的源矩形和原版基线定位。
- 图标使用独立源矩形，巢与篮口使用前后绘制层。画布文字、按钮状态和动画由代码绘制，不烘焙到背景。
- 新图标仍有少量渐变和高光；背景与角色线条也存在细微差异。这些属于本轮用户美术评审项，不能用“分辨率更高”替代判断。

角色与鸡蛋的输入参考和提示词见 [角色记录](provenance.md#characters-v4-notes)。全品种与全部厨具等级尚未重绘。


<a id="expansion-dim-sum-notes"></a>

## 竹笼点心坊图集

`expansion-dim-sum.png` 使用内置 `image_gen` 生成，最终原图尺寸 **1254×1254**，模式 **RGBA**。三个既有 `chick-v4-0/3/4.png` 已检查，用于确定暖色、圆润造型、深棕描线与鸡嘴脚的视觉规范；生成提示将这些特征明确写出。

前两行依次为小笼包鸡、烧麦鸡、荷叶糯米鸡、奶黄流沙鸡、水晶饺鸡、寿桃豆沙鸡；第三行为单层、双层、三层竹蒸笼。图集无文字，厨具不带角色面孔。

最终选择首次真正带透明通道的生成稿。两次试做的加粗／背景提取稿输出为 RGB 并带可见棋盘底，未接入、未复制进本项目。未用程序修改任何图片像素。

源文件：`C:\Users\管啸野\.codex\generated_images\01a09b36-952f-7791-87ab-ce23dab5a584\exec-db020023-af5f-46a0-abd5-90c8b6fbb5bf.png`。

只读 Alpha 检查确认透明像素 815126 个，Alpha 范围 0–255，主体内部采样为 252–253。依据每个对象的可见边界加 4px 余量登记九个独立源矩形。最高蒸笼跨过严格均分格线，所以没有直接用等分九宫格截取。六只鸡宝在原 120×120 角色坐标中保持底部基线 118，最大宽 82、高 80；在 60px 场景框中得到约 37–40px 的实际可见高度。

最终生成提示：

> Use case: stylized-concept. Production game sprite atlas, PNG with REAL TRANSPARENT ALPHA background. Create one SQUARE image containing exactly NINE separate sprites arranged in a strict evenly spaced 3 by 3 grid. Each sprite is centered within its own equal square cell with generous transparent gutters and outer padding. No grid lines, no cell backgrounds, no text, no numbers, no watermark, no scenery, no cast shadows or ground ellipse, no particles outside any sprite. Sprites must not overlap.
>
> Style: cute warm Chinese chibi food-chicken mobile game. Thick smooth DARK WARM BROWN outlines, rounded simple silhouettes, large head/body in one plump shape, tiny orange chick beaks and small orange feet. Cream egg yellow, honey gold, spring green and soft coral pink; BRIGHT and clean. Flat painted shapes with just one soft cel-shadow region and a small cream highlight. Uniform visual scale and outline weight. Friendly expressive dark brown eyes, a tiny white glint, peach cheeks. Not glossy 3D, not painterly textured, not realistic food photography.
>
> Reading order is STRICT: Row 1 left: xiaolongbao chick, a plump pale cream soup dumpling body with a pinched spiral bun top, tiny chick wings, two feet, sweet shy face. Row 1 center: shumai chick, an open yellow pleated dumpling cup body with a scalloped ruffled top, orange shrimp-filling tuft visible on top, tiny chick face and feet, cheerful eyes. Row 1 right: lotus-leaf sticky-rice chick, rounded chick bundled in a fresh green folded lotus-leaf jacket, its cream little face peeking out above the leaf fold, orange beak and two tiny feet, sleepy friendly eyes.
>
> Row 2 left: golden custard lava-bun chick, round bright golden bun with a small crack revealing a simple warm yellow custard swoosh on its belly, smiling face and little raised wings. Row 2 center: crystal dumpling chick, translucent-looking pale ivory and very light mint blue crescent dumpling BODY with big simple pleats on top; visible body remains opaque drawn color, not a ghost. Round chick face, tiny orange beak and feet, delighted eyes. Row 2 right: longevity peach red-bean-bun chick, a plump cream-and-pink peach-shaped bun, soft pink pointed top, two simple green leaf wings at the base, tiny chick face/beak/feet, rosy cheeks.
>
> Row 3 left: one-tier BAMBOO STEAMER with lid. A single shallow honey bamboo basket, lid with one round knob and minimal weave marks, front three-quarter game item view. Row 3 center: two-tier BAMBOO STEAMER with lid. EXACTLY two visibly stacked bamboo basket tiers, slightly brighter bamboo bands, same angle and drawing vocabulary. Row 3 right: three-tier BAMBOO STEAMER with lid. EXACTLY three clearly separated stacked bamboo basket tiers, small warm gold fitting on lid and a simple green tie accent, same angle.
>
> The three steamers are tools, NO faces, NO chicks inside, NO food spilling out, NO steam clouds. Their increasing height must be obvious while their widths stay similar. The six chicks have complete, legible silhouettes and all use the same drawing proportions. Keep all artwork inside its own 3x3 cell. Actual transparent background; never draw a checkerboard.


<a id="farm-v8-notes"></a>

## 农场 v8：四时段美术与场景模块

### 本任务改动范围

新增 `web/farm-scene.js`、`web/farm-theme.js`、四张 `web/art/farm-*-v8.png` 和本说明。没有修改 app.js、scene.js、theme.js、manifest.js、farm.js 或 engine.js；父任务负责预载、界面入口、输入绑定与浏览器验收。

### 美术文件

| 时段 | 原规则小时 | 文件 | 实际PNG尺寸 |
|---|---|---|---|
| 0 黎明 | 05:00–07:59 | farm-dawn-v8.png | 1517 × 1037 RGB |
| 10 白天 | 08:00–16:59 | farm-day-v8.png | 1516 × 1037 RGB |
| 20 傍晚 | 17:00–18:59 | farm-dusk-v8.png | 1517 × 1037 RGB |
| 30 夜晚 | 19:00–04:59 | farm-night-v8.png | 1517 × 1037 RGB |

日间图由内置 imagegen 生成，其他三张以同一日间图为参考，仅编辑光照与色彩，保持建筑、路、围栏和花丛位置。输出尺寸存在1px宽度差异，统一映射至世界830 × 568；按钮矩形保留边缘余量。四张实际文件均已 view_image 检查。夜晚采用蓝色天空与可读的青绿草地、暖窗光；没有把图片整体压成暗黑。

背景没有鸡、鸭、蛋、人物或 UI 文字。农舍和补给摊是两个可交互建筑；远处小神社只是装饰，没有添加新活动玩法。

整体比厨房素材更偏柔和手绘、细碎草叶多于严格三色平涂；最终风格一致性与实尺寸对比由完整页面验收，不能将图像生成结果直接称为用户美术通过。

### 接口与坐标

`createFarmRenderer(ctx,image)` 返回 `{draw(s,v)}`；其中 image 使用主渲染器已有的图集裁切接口。draw 读取 v.now、farmScroll、walkers、pressedId、reducedMotion，且不修改存档或角色位置。header 与 navigation 由父级绘制。

`farm-theme.js` 导出：

- FARM_WORLD：width=830、height=568、maxScroll=510。
- FARM_ART：按0/10/20/30映射背景路径、时段文字及阴影颜色。
- FARM_ART_FILES：用于父任务图片预载的4个完整路径。
- FARM_ACTIONS：世界坐标的建筑入口；输入侧应减去 farmScroll。
- FARM_RECT：固定在屏幕下部的两个操作按钮，和画面使用相同几何。

| id | action | rect [x,y,w,h] | 标牌 sign [x,y,w,h] |
|---|---|---|---|
| farm:house | harvest | [18,71,145,133] | [77,126,43,19]，收成表 |
| farm:stall | shop | [214,116,107,82] | [250,172,43,20]，商店 |
| farm:repair | 固定修缮入口 | [10,458,100,36] | 无 |
| farm:harvest | 固定收成入口 | [210,458,100,36] | 无 |

建筑第一次点击就打开对应已有页面。所有建筑热点位于 y220 上方，原 y220..450 的横拖区域继续可用。不要保留原妖怪村/神社占位弹窗热点到新的装饰位置。

农场父 header 建议只画 y0..58 顶栏，不再添加原 y66..92 中部“农场”标题条，因为那一条会遮盖部分农舍屋顶。模块会在下部中央显示时段和横拖提示。

### 场景行为

- 背景按原 farm.js 的 timeZone(v.now) 切换，世界坐标和滚动范围不变。
- 沿用父任务传入的原 farmDisplay 结果及排列顺序。地面角色与特殊天空角色均保留 x/y；null 阴影角色不绘地面投影。
- 以原阴影图片可见范围 y98..120 为依据绘制小椭圆。正常呼吸底脚位置稳定；天空角色有极小漂浮。
- reducedMotion 时停用呼吸、漂浮、叶子/萤光点运动，星光固定不闪烁。
- 少量太阳、月亮和星光由 Canvas 绘制并随世界滚动，底图不烘焙这些动效。
- 农场空时显示一个草地木牌式引导；已有鸡宝但当前时段没有可见物种时，使用相应提示。
- 底部修缮百分比直接读取原 farmHP；实际价格与修缮动作仍由父任务调用原规则。
- 只有修缮和收成两个固定按钮，中间的时段与滚动指示不是额外可点击按钮。

### 验证记录

本任务已完成代码语法检查，并运行临时只读验证：

- 四张PNG存在且宽高比与830:568接近。
- 两建筑及其标牌都在世界边界内，建筑不相交；两个固定按钮互不重叠、按压后低于导航顶边502。
- 四时段 × 三个滚动位置（0、255、510）共12个场景调用，背景路径/位置正确。
- reducedMotion 在不同动画时钟下输出相同绘制记录。
- 普通与特殊天空角色的原始位置保持；边缘角色只作视口剔除。
- Canvas save/restore 平衡，没有非有限坐标；绘制前后存档相同。
- 空农场提示、修缮按钮按压分支可执行。

这些是静态与模拟Canvas接口验证，不是浏览器绘制、点击或用户审美验收。父任务还需检查真实手机尺寸下的标牌字、动作热点及四时段截图。

### 生成原图

原图目录：
`C:/Users/管啸野/.codex/generated_images/01a0808c-0146-7d03-a0c2-f11988f884a6/`

| 文件 | 原图名 |
|---|---|
| farm-day-v8.png | exec-cfb47cc1-7107-465c-81d5-f3d1c1359017.png |
| farm-dawn-v8.png | exec-7d3a8a76-f4ac-4974-b9eb-d8e1d9ebdc81.png |
| farm-dusk-v8.png | exec-d02ed181-cd63-4a9f-96e8-02f2e9074778.png |
| farm-night-v8.png | exec-3d66af12-4dcb-4666-8458-f601cfcc239f.png |

全部使用 imagegen skill 的内置工具，没有使用CLI或自建API脚本。项目文件是完整原图复制，没有程序化裁切、重采样、抠图或调色。

### 日间完整提示词

```text
Use case: stylized-concept. Asset type: ONE complete wide farm background panorama for a cute 2D chicken-collecting mobile game. Landscape width:height about 830:568 (roughly 3:2). Full bleed, no frame.
Art direction must match a warm chibi kitchen game: bright soft green grass, pale aqua sky, warm ocher paths, cream walls and coral red roofs, confident warm dark-brown outlines, chunky rounded shapes, simple hand-painted cel-shadows. Cheerful daylight, gentle countryside. No deep photoreal shadows, no realistic texture noise, no 3D glossy rendering. Small readable scenery with a LARGE calm open pasture for characters.
Exact world composition, using a logical 830-wide by 568-high canvas:
The top y0..58 is sky hidden by the game's HUD. From y58..110 show pale sky, small soft clouds and gentle distant green hills. Do NOT bake a sun or moon; those will animate separately.
Place the ONLY main farmhouse near the LEFT edge, occupying approximately x18..145, y100..210. A compact cream cottage with a chunky coral-red gabled roof, short chimney, warm wooden door and two windows. Whole silhouette visible. Include one small BLANK wooden sign board immediately above its door, no writing.
Place one small SUPPLY STALL at x215..312, y125..210: simple cream-and-coral striped canopy over a tidy small wooden counter. No bottles or tiny clutter. A single BLANK sign board on its front. This is the only other obvious clickable building.
At about x525..590,y135..198, a very small peaceful red-roof countryside shrine can be distant decoration; no stairs stretching into the play field. All other distant elements are grouped bushes, small trees and a low wooden fence BEHIND the open pasture, generally near y175..210.
MOST IMPORTANT: y210..440 across the ENTIRE x0..830 width is a broad continuous, uncluttered walkable pasture. No buildings, ponds, large rocks, walls, fences, tall grass or objects in this character area. A soft narrow warm dirt footpath may curve horizontally through the grass without interrupting it. Leave space even at x0..60 and x750..830 for the game's existing character positions. Sparse tiny ground flowers only, never dense pattern.
Small flower clumps and low fence fragments may frame the very bottom y470..568 and far corners, because game UI covers the bottom. Keep the center open and readable at 320 px viewport width.
High overhead three-quarter view, a coherent gently sloping farm, no exaggerated fisheye perspective. The background must scroll smoothly horizontally: do not compose everything in the middle like a poster, distribute calm scenery across the whole width. Buildings are small in the upper third; pasture dominates.
ABSOLUTELY NO characters, people, chickens, ducks, eggs, creatures, animal silhouettes, baskets, UI, text, letters, numbers, logos or watermarks. No floating cards, no inventory panels. Produce background art only, daytime lighting.
```

### 黎明完整提示词

```text
Use case: lighting-weather. This attached image is a finished 2D game background plate. Produce a DAWN lighting variant of exactly this scene.
Change ONLY colors, lighting and sky mood. PRESERVE THE EXACT geometry, camera, image dimensions, crop, building positions, farmhouse roof and doors, stall, blank signs, shrine, fences, flowers, every major hill and the footpath. No moved, enlarged, added or removed objects.
Dawn: pale peach and buttery yellow near the horizon, light cool lavender-blue at the top, soft fresh mint-green pasture, warm cream buildings. Sunrise gently warms one side of the scene, but the whole pasture remains bright and easy to read on a phone. Keep the cartoon outline and original drawing style. No fog hiding the field, no dramatic dark shadows.
Do NOT add a sun, moon, stars, characters, chickens, animals, eggs, text, labels, UI or new props. Both sign boards stay blank. Full background image, same composition and scale.
```

### 傍晚完整提示词

```text
Use case: lighting-weather. Edit the attached finished 2D farm background into an EARLY EVENING / SUNSET color variant.
Change ONLY the time-of-day lighting and colors. Preserve the exact camera, proportions, image size, landscape, field, paths, fences, flower clumps, farmhouse, supply stall, shrine and both blank signs. Every building must stay at the same coordinates and scale because the game uses fixed click targets.
Evening palette: warm coral-pink and peach sunset sky, lavender distant hills, soft golden rim-light, fresh light olive-green pasture with gentle warm amber illumination. Cozy late afternoon, still bright enough to see all details clearly on a small phone. Farmhouse windows can have a small warm cream light, no large glow. Keep original cartoon illustration and dark-brown outlines.
No black shadows or brown muddy field. No added sun or moon, no stars, no characters, animals, chickens, eggs, text, labels, UI, extra objects or new scenery. Do not move or redraw architecture.
```

### 夜晚完整提示词

```text
Use case: lighting-weather. Convert the attached finished 2D farm game background to CLEAR, BRIGHT MOONLIT NIGHT.
This is strictly a lighting/color edit: preserve exact composition, image size, camera and ALL geometry. The farmhouse, stall, shrine, two blank signs, fence, paths and flower beds must keep identical pixel positions and scale for fixed game click targets. Do not add or remove any scenery.
Night palette: visible medium periwinkle-blue sky, soft blue-violet distant hills, clearly readable desaturated MINT-TEAL GREEN pasture, pale blue cream building walls and subdued coral roofs. The whole field should be luminous and legible under soft moonlight, like a cozy chibi game. Farmhouse windows and shrine niche emit small warm golden light. Keep the same clean illustration style. NO near-black field, NO dark green mud, NO crushing details in shadow, NO heavy vignette.
Do not add a visible moon, sun or stars; the renderer will add them. Do not add any characters, animals, chickens, eggs, labels, text, UI, fireflies, technology, or extra lantern props. Both signboards remain blank and unchanged. Full background only.
```



<a id="four-seasons-v12-notes"></a>

## 四时食谱 · 16 位新伙伴素材

2026-09-14，使用内置 imagegen 生成（未使用 CLI/API 回退）。

- 项目文件：[four-seasons-v12.png](four-seasons-v12.png)，1254×1254 RGBA，背景具有真实 alpha。
- 原始输出：C:/Users/管啸野/.codex/generated_images/01a07b82-6e9d-7d83-bd7c-2b233c81beb4/exec-eb5779a7-73d3-4841-bd19-4717244c4e10.png。
- 保留原始输出，复制到项目；没有用脚本重绘、改色或抠图。代码只读取 alpha，测量可见边界，并通过 [manifest.js](manifest.js) 的源矩形显示各个角色。
- 4×4 图集，每行一个章节；各行前两格为鸡宝，后两格为鸭宝。鸭宝采用宽嘴和蹼脚，与鸡宝尖喙区分。
- 新品种内部 ID：鸡 120–127，鸭 57–64；图鉴显示 C121–C128、D58–D65。
- 确认了 16 个独立轮廓和实际页面中的透明边缘。农场行走、呼吸、方向变化与厨房破壳／收取复用现有程序动画，并非每只另有手绘逐帧动画。
- 原版所有素材和角色编号保留。本轮新增图集，不替换现有角色文件。

完整配方与获取方法见 [四时食谱说明](../../docs/game-design.md)。

### 实际生成提示词

```text
Use case: stylized-concept. Asset type: production transparent sprite ATLAS for a bright 2D chibi chicken-and-duck cooking collection game. Create one square 2048x2048 RGBA atlas: EXACTLY 16 isolated full-body characters, strict 4 columns x 4 rows evenly spaced invisible grid. Each character fits in central 75% of its 512px square; generous real transparent gaps, no overlaps, no text, no grid, no backgrounds, no ground shadows. Each cell bottom of feet aligned at 88% height. Consistent optical scale. Crisp warm dark brown outlines, flat rich pastel colors, simple clean cel shading, no realism, no grain, no glow, no card frames. Squat egg-shaped bodies, distinct expressive faces, stubby wings and two small orange feet. CHICKS have pointed tiny beaks; DUCKS visibly wider flat orange bills and webbed feet. Show clearly different food silhouettes rather than the same animal with recolored hat. All front three-quarter view, readable as 60px game sprites. Cheerful expressive game art.

Exact cell order left to right:
Row1 spring:
1 CHICK cherry mochi: plump pale pink rice cake body, single deep red cherry and green leaf on head, subtle flour patches, cheerful pointed beak.
2 CHICK honey pancake: body three squat golden pancake layers, honey dripping on upper edge, butter cube on head, happy face on front.
3 DUCK matcha dango: round green dumpling body in one folded green leaf, white sesame specks, broad duck bill, sleepy eyes.
4 DUCK chrysanthemum tea jelly: translucent golden jelly pudding body with white-yellow flower crown, wide duck bill, curious face.
Row2 summer:
1 CHICK lemon shaved ice: pale yellow icy mound body, lemon wedge crest, blue short bowl-like lower belly, tiny pointed beak, excited eyes.
2 CHICK caramel custard: rounded flan shape with caramel brown cap dripping edge, cream body, small pointed beak, shy expression.
3 DUCK orange soda: round orange fizzy jelly body with cream belly, small orange slice on head, broad bill, laughing eyes.
4 DUCK redbean ice pop: short rounded rectangle creamy pale rose popsicle body with chunky dark red beans, wooden stick tail just behind, broad bill, sleepy eyes.
Row3 autumn:
1 CHICK sesame mooncake: scalloped golden-brown mooncake body, embossed simple flower on forehead, tiny black sesame accents, proud pointed beak.
2 CHICK redbean dorayaki: two pancake halves sandwiching thick dark redbean body middle, little face in top pancake, pointed beak, tiny wings.
3 DUCK caramel toast: square thick golden toast with rounded upper corners, caramel stripe and cream patch, broad bill, perky expression.
4 DUCK grilled mushroom riceball: white triangular riceball body with dark seaweed sash and chestnut-brown mushroom cap, broad bill.
Row4 winter:
1 CHICK hot cocoa: squat cocoa-brown body with creamy marshmallow swirl crest, cream belly, small pointed beak, warm smile.
2 CHICK snowcap cookie: round scalloped biscuit body with white icing upper half and colorful tiny sugar dots, small pointed beak, lively eyes.
3 DUCK ginger rock-sugar tea: amber tea-drop body with tiny ginger bow and two translucent sugar cube crest shapes, broad bill, gentle eyes.
4 DUCK scarf sesame tangyuan: round snowy white dumpling body, dark sesame hair curl, short coral knitted scarf, broad bill, rosy cheeks.

EXACTLY one independent character in each cell, 16 total. Real alpha transparent background throughout, never draw a checkerboard. No extra decorative objects outside each character. Preserve clean gaps for cropping and warm bright game color identity.
```



<a id="kitchen-backgrounds-v5-notes"></a>

## Kitchen stage backgrounds v5

Generated 2026-09-08 with the built-in image generation tool. Reference inputs: the project’s kitchen-v4.png and chick-v4-0.png. No external media downloaded or modified. All four stages were visually inspected, then copied intact into this folder. No raster recoloring, resizing or background removal was performed by script.

| Runtime file | Generator original |
| --- | --- |
| kitchen-stage-0-v5.png | exec-c6d00017-c1a7-45ba-858c-b312aaac0b5b.png |
| kitchen-stage-1-v5.png | exec-e3eee552-db43-4a2c-93cf-44c2c2e7e17e.png |
| kitchen-stage-2-v5.png | exec-d945b91b-4e28-443f-b2c8-6795dc4c6b9c.png |
| kitchen-stage-3-v5.png | exec-ba7ca07d-e5a0-4356-86d3-323a56ce436b.png |

Original output directory: `C:/Users/管啸野/.codex/generated_images/01a07b82-6e9d-7d83-bd7c-2b233c81beb4/`.

### Shared production brief

Portrait 9:16 bright chibi kitchen background plate. Warm brown contours, large simple shapes, quiet light colors and clean shadows. Top 0–10% quiet behind HUD. Wall 10–30%. Table back edge exactly at 30%; an empty tabletop extends to 66%. Simple cabinet fronts below 66%. No characters, eggs, beds, baskets, vessels, text, or controls baked into background. Keep camera and boundaries registered across all four stages.

Level 1: pale honey wood planks, modest wood shelf and herb jars, right hanging spoon. The stage-0 output became the reference for the other three edits.

Level 2 edit: warm apricot brick wall, pale cream mortar and painted cream shelf, same table and camera.

Level 3 edit: cream tiles with light mint grout, centered wood-framed window with sky/trees, small left shelf and herb jar, right spoon; pale cream worktop and sparse mint cabinet accents.

Level 4 edit: cream wallpaper with small peach dots, centered bright window and peach scalloped curtains, small left wood shelf and jar, right spoon; pale honey table with peach trim at the front edge.

### Runtime composition and limitations

- Runtime stretches the complete plate to the fixed 320×568 scene (near-identical aspect ratio).
- Beds and vessels are independently bound by `kitchen-stages.js`; their source frames live in `manifest.js`. Bed prototype v5b replaces the thicker v5 prototype.
- The fixed 6×4 egg positions and collection hit targets are unchanged. Bed design was made to fit them.
- Webs and table dust are code-drawn maintenance layers keyed per stage. They are not baked into the background or represented as four newly generated dirty PNGs.
- These are a playable art pilot. The generated backgrounds retain mild gradients despite the flat-color brief. Full-game stylistic consistency and final acceptance remain a user review step; farm, most characters, utensils and other pages still contain original art.


<a id="kitchen-high-stages-v7-notes"></a>

## 公寓与精装厨房背景 v7

制作日期：2026-09-09。工具：内置 image_gen；直接复制生成原图，没有使用 CLI、SVG 或脚本图像编辑。未修改现有代码。

### 交付与定位

| 内部阶段 / 画面等级 | 文件 | 实际格式与尺寸 | 源图分段建议（像素 y） |
|---|---|---|---|
| 2 / Lv.3 小康公寓厨房 | kitchen-stage-2-v7.png | RGB，941×1672 | 墙 0–539；台面含前沿 539–1168；下部 1168–1672 |
| 3 / Lv.4 豪华精装厨房 | kitchen-stage-3-v7.png | RGB，941×1672 | 墙 0–645；台面含石材前沿 645–1156；下部 1156–1672 |

分界来自原图目视检查与中央行色值只读统计；不是已执行的裁切。交付原图保持不变。root 可将三段分别绘制至游戏 y=0–170、170–374、374–568，以维持固定蛋阵与 UI 的注册。

若不作分段绘制，原图墙/台面线落到逻辑 y≈183（公寓）和 y≈219（精装），不符合计划的 y≈170；因此当前交付需要上述运行时注册，不能直接声称原图已精确对齐30%分界。

公寓：普通灰色抽油烟机、简单浅绿平门吊柜、明亮瓷砖与小窗、两件家用挂具。保留了小盆栽在最左边缘；中心台面为空，不含蛋、角色、床、窝、篮、文字或UI。

精装：定制框门吊柜、宽大造型烟机、黄铜五金和龙头、大窗、连续厚石台面。使用一次针对渲染风格的修正，将细碎写实大理石纹理替换为少数宽浅色纹理，并加强暖棕轮廓；房屋结构没有改为木屋。仍保留少量柔和渐层，不是严格每面只有两色的硬边美术。

背景已经原图检查；实际 HUD 覆盖下的能见度、与角色床组合及同屏审美验收由 root 进行。

### 生成原路径及文件校验

- 公寓：C:/Users/管啸野/.codex/generated_images/01a08088-93fc-7280-a7e4-c221f3e541a5/exec-14aab74d-5725-4999-8dbc-a9fb900c5c46.png
- 公寓最终 SHA-256：ec3bb8a2eae90a511098a8a520dc3263d921b03305b67d47c7c1bab392b3e2a2
- 精装初稿（未作为最终文件复制）：C:/Users/管啸野/.codex/generated_images/01a08088-93fc-7280-a7e4-c221f3e541a5/exec-ff58ec97-2e44-4449-b8c1-49654dcd8fce.png
- 精装最终：C:/Users/管啸野/.codex/generated_images/01a08088-93fc-7280-a7e4-c221f3e541a5/exec-5e8b07bd-e3fa-425c-94b1-c057fe8114e7.png
- 精装最终 SHA-256：3bc7281a2a94766d0625c699488e50c84ee561a87018aeb2fac7a16154e676fc

### 公寓完整提示词

参考图：web/art/kitchen-stage-2-v5.png。仅取风格与构图注册。

Use case: style-transfer. Asset: a full-screen portrait BACKGROUND PLATE for a 2D chibi chicken-kitchen mobile game, internal stage 2 / displayed level 3. Redesign the supplied background into an ORDINARY CLEAN MODERN FAMILY APARTMENT KITCHEN, practical middle-income home quality. Reference image is only the rendering style and exact wall/worktop composition registration; substantially replace its simple window-and-spoon wall with real modern kitchen facilities.
Composition registration is crucial: portrait approximately 9:16. At the game size 320 by 568, the top 0–58 pixels (top 10%) are behind a HUD, so put no essential identity only there. The entire visible kitchen wall and important fittings must occupy y=58–170 (10–30% of image height). Wall/worktop boundary is a straight horizontal line exactly around 30% down. From 30% to 66% is a broad completely EMPTY unbroken pale laminate worktop; keep this entire central area free for characters to be added later. From 66% to bottom are plain modern base cupboards. Do not change these height zones.
Upper visible wall between 10% and 30%: a recognizable inexpensive brushed-grey household extraction hood on the left with a short duct, a modest rectangular daylight window near the middle, a small pair of simple flat-front light mint wall cupboards on the right with ordinary silver handles, light warm-white rectangular ceramic tile backsplash, and a short rail with only two plain hanging cooking tools. All major identifying parts must visibly fit into that 10–30% strip, below the HUD. Keep a coherent frontal kitchen composition with the worktop viewed mildly from above. Ordinary neat home kitchen, not luxurious, not rustic and not a child's bedroom.
Palette and art: bright cream, restrained pale mint, clean light grey, warm chocolate outlines. Friendly polished 2D cartoon game art, large flat shapes, clean warm dark-brown edges, simplified cel-shaded surfaces, one clear shadow tone, simple materials. Daylight and cheerful warm brightness. No photorealism, no 3D rendering, no tiny noise, no brushed painterly grain, no ornate carvings or gold luxury fixtures.
Absolute empty-play-area constraints: no eggs, birds, animals, nests, beds, cradles, baskets, trays, foreground containers, stovetop burners or objects on the central worktop. No characters, no text, no numbers, no badges, no buttons, no UI, no watermark. Background alone, edge to edge. Lower cupboard details cannot be the only feature that distinguishes this level. Output one complete portrait image.

### 精装初稿完整提示词

参考图：本轮 web/art/kitchen-stage-2-v7.png。保留同一游戏风格与构图注册，改变房间结构及档次。

Use case: style-transfer. Asset: portrait full-screen BACKGROUND PLATE for the HIGHEST LEVEL luxury fitted kitchen in the same 2D chibi mobile game. Reference is an ordinary apartment kitchen: retain its warm clean cartoon rendering, portrait camera registration and empty worktop, but make a major STRUCTURAL AND MATERIAL UPGRADE to an unmistakably luxurious professionally fitted kitchen. Do not merely recolor its same cabinets. No bedroom, no dollhouse curtains.
Layout registration: approximately 9:16 portrait. Top 0–10% is covered by game HUD; therefore essential luxury features must visibly appear BELOW 10%, across the 10–30% upper wall strip. The wall ends and broad countertop begins at 30% of image height. The entire countertop from 30% to 66%, especially x=15%–85%, is entirely EMPTY for a 6-by-4 character group and bed added later. The lower third below 66% can contain base cabinets, but luxury must already be obvious from the upper visible wall, not only lower cabinets. Mildly top-down view of empty worktop, frontal back wall, no floor-plan or steep isometric view.
Replace the whole upper room structure. A substantial fitted cream-white cabinet system spans the upper wall, with thick framed inset doors, fine warm-brass trim and deliberate architectural cornice. A generously sized clear daylight window occupies the left section. A beautifully shaped broad ivory-and-brass extraction hood occupies the central wall, with a distinctive tapered bell profile and thick gold-edged canopy. On the right, full custom sage-green and ivory wall cabinets with refined brass handles, plus a curved polished warm-brass kitchen faucet visible against the backsplash just above the worktop at far right. Use large clean ivory marble backsplash slabs with only two or three broad restrained pale veining strokes, not a tiny repeated tile grid. Make the window, hood canopy, cabinetry frames and faucet all recognizable within the upper 10–30% strip. No curtains, no scalloped nursery ornament, no jars-and-vines substitution for actual kitchen facilities.
Countertop: an expansive continuous light ivory STONE slab with substantial bevelled thickness along its front at about 66% image height, straight broad edge, extremely sparse pale marble veining, entirely empty in the central playable area. Lower base cupboards can continue the cream-white custom joinery with sage accents and small brass hardware. Keep the palette bright and airy: warm ivory, soft sage, gentle warm gold. Visibly finer architecture and materials than the simple reference kitchen, but not cluttered with tiny decor.
Art: polished bright 2D chibi mobile-game background; warm dark-brown confident outlines, broad simple flat color blocks, restrained one-step cel shadows, simplified cartoon materials. Clear silhouette at 320 by 568 pixels. No photorealism, no 3D render, no complex reflections, no gritty texture, no tiny noise, no dramatic dark lighting.
Strict exclusions: no characters, no eggs, no birds, no nests, no beds, no cradle, no basket, no tray or loose container, no furniture or cookware in the center of the worktop. No UI, text, digits, buttons, icons, watermark. Do not render a game screenshot: render the clean room background only, edge to edge, with the center completely empty.

### 精装最终风格修正完整提示词

参考图1：精装初稿生成文件；参考图2：web/art/kitchen-stage-1-v7.png（只取新前两级的线条与卡通材质）。

Use case: style-transfer. Edit reference image 1, the luxury cream-and-sage kitchen. Reference image 2 is ONLY the painterly cartoon line quality and simplified game materials reference; do not copy its wooden room or replace the luxury fittings with wood.
Keep image 1's complete architecture, composition and geometry exactly: large left window, central sculpted ivory-and-brass hood, fitted framed cream and sage wall cabinets, warm-brass faucet on the far right, entirely empty broad pale worktop, luxury lower drawers and cupboards. Do not move the wall/worktop boundary or change the camera. Preserve the upgrade to high-quality fitted luxury kitchen.
Make ONE targeted rendering-style correction: turn the semi-realistic fine marble grain, gleaming metallic strips and diffuse shading into a clean illustrated 2D chibi mobile-game background, stylistically matching reference 2. Use clearer slightly thicker warm dark-brown contours around the cabinets, hood, window, faucet and countertop edge. Replace realistic marble mottling and fine branching veins with flat ivory surfaces and only TWO or THREE broad soft pale-beige stylized vein shapes total. Give each brass and cabinet surface a simple base color plus one cel-shadow shape and one restrained broad highlight, without photographic reflections or material noise. Maintain bright daylight, cream white, sage green and warm gold. Rich architecture, economical details, legible at 320 pixels wide.
The big central countertop must remain COMPLETELY EMPTY. No eggs, birds, nests, beds, basket, tray, loose containers, characters, UI, text or watermark. One complete portrait background only. Do not add new decorations or clutter.



<a id="kitchen-stage-0-1-v7-notes"></a>

## 前两级房屋背景 v7

本次只新增两张完整背景板及本说明，不修改代码。新方向由用户明确指定：初始破旧茅草房屋 → 略微整齐木板房屋 → 小康公寓厨房 → 豪华精装厨房；本任务只负责前两级。

### 交付文件

| 文件 | 图片尺寸 / 模式 | 建筑识别 |
|---|---|---|
| kitchen-stage-0-v7.png | 941 × 1672 / RGB | 毛边下垂茅草檐、歪斜原木柱、绳绑连接、土墙开裂、露出竹篾补层、晴天透光裂缝、钉补粗木桌 |
| kitchen-stage-1-v7.png | 941 × 1672 / RGB | 完整横木板墙、直木梁与方柱、简易方木窗、整齐空木架、平整木台面与开放下层木架 |

两张图均没有烘焙鸡、鸭、蛋、床、窝、篮、容器、文字或 UI。stage1 没有砖、瓷砖或现代橱柜。stage0 以晴天明亮黄褐色表现破旧，没有压成暗色老照片。

### 画面注册与可见特征

图像生成未精确服从桌后沿30%高度，应由场景分段注册，不直接假定整张等比缩放就满足蛋阵坐标。

| 阶段 | 原图墙面 | 原图台面 | 原图桌前及下部 | 建议逻辑映射 |
|---|---|---|---|---|
| stage0 | y0..598 | y598..1150 | y1150..1672 | 分别映射y0..170、170..374、374..568 |
| stage1 | y0..545 | y545..1085 | y1085..1672 | 同上 |

均使用整幅图片宽度0..941 → 逻辑宽度0..320，不改变项目PNG像素。建议分段由 renderer 实现，保证中心 x47..271、y168..335 放入蛋阵与独立巢。

按上述映射，320 × 568 实尺寸的主要特征位置估算如下：

- stage0 HUD 下还能看到约 y58..91 的下垂毛边稻草；竹篾修补块约 x190..241、y95..148；两侧粗柱持续到桌后沿y170。它们不是全部藏在顶部HUD后。
- stage1 方窗约 x200..287、y60..145，空木架约 x24..174、y90..127，均处于可见墙带内；完整板墙与直梁构成有序房屋轮廓。
- 桌面中央没有建筑或小道具伸入，stage0钉补条集中两侧；真实叠加后的遮挡仍需在完整游戏画面中核对。

已经使用 view_image 查看项目中的两张完整文件。此任务没有操控 root 的浏览器，所列实尺寸位置是依据原图边界与既定逻辑映射计算，不能当作已完成集成后截图验收。

### 生成过程与限制

采用 imagegen skill 的内置图像工具，每阶段一张输出，本轮两次请求均成功，没有使用 CLI/API fallback。所有工具原图保留在默认生成目录，项目只复制完整图片，未裁切、调色或脚本修图。Pillow 仅用于只读核对尺寸与文件模式。

画面相对旧v5包含更多草、木、土材质纹理；它们不是严格只用两三色的纯平涂。最终一致性与游戏画面质感由完整四级对照验收。

### 原输出路径

- stage0：`C:/Users/管啸野/.codex/generated_images/01a0808c-0146-7d03-a0c2-f11988f884a6/exec-11ece3c2-1872-44ac-a93c-a85feccff659.png`
- stage1：`C:/Users/管啸野/.codex/generated_images/01a0808c-0146-7d03-a0c2-f11988f884a6/exec-c1e6f399-7c68-42c9-a115-c6b3a7df5d3e.png`

### stage0 完整提示词

```text
Use case: stylized-concept. Asset type: a portrait 9:16 production background plate for a bright cute 2D chicken-hatching mobile game.
Create ONE complete background of the INSIDE OF A VERY POOR, SHABBY THATCHED HUT with a large empty rough wooden worktable. This is the cheapest starting home, visibly ramshackle and repaired by hand. Warm sunny daytime and cheerful readable colors. It must not look like a tidy wooden cabin or a designer rustic kitchen.
Critical screen registration for the 320x568 game canvas:
- Top 0–10% height will be hidden by game HUD; do not put the only identifying details there.
- The important hut structure MUST BE VISIBLE between 10% and 30% height (about y58–170): a drooping FRAYED STRAW EAVE with distinct irregular clumps and loose straw tips hanging into this band; a slightly crooked raw-log upright at each side; patched tan earthen wall with a small exposed crosswoven bamboo repair panel; two narrow irregular daylight gaps in the wall. No nice window, no finished boards, no straight polished beams. Show bright sky through thin cracks, not darkness. Straw fringe should reach about 17–20% height so it remains visible below the HUD.
- At EXACTLY 30% height, the rear edge of a broad wooden tabletop crosses the scene horizontally.
- From 30% to 66% height is a WIDE EMPTY tabletop viewed from a shallow overhead angle, extending from left edge to right edge. This is where gameplay will go. The central x15%–85%, y30%–60% must have no props and no bulky structure. Use a few broad light honey-tan rough boards, slightly uneven joined seams, and one or two nailed repair strips only at extreme side edges. The center stays calm and light, with no deep gaps under future characters.
- At 66% height is the front edge of the table. Below this, simple battered board apron, two rough uneven supports and pale earthen floor can be shown; this area is mostly covered by game controls. Absolutely no modern cabinet doors or finished cabinetry.
Art direction: high-quality hand-drawn chibi mobile game environment, clean bold warm DARK CHOCOLATE outlines, solid large color shapes, only a small number of whole cel-shadows. Pale warm beige mud wall, yellow dry straw, warm ocher timber, subtle sky-cyan slits. Bright enough to clearly read at 320 pixels wide. Shape language slightly irregular and chunky. No photographic texture, no photorealism, no realistic weathering noise, no heavy vignette, no dramatic gloom, no sepia old photograph, no glossy 3D finish.
Single coherent room construction rather than a pile of decorations. Preserve the huge empty work area. No chickens, ducks, eggs, beds, nests, baskets, bowls, tools, UI, labels, numbers, text, signs, plants, jars, appliances, tiles, bricks, lamps or curtains. Background only. Full bleed, no outer border or phone frame.
```

### stage1 完整提示词

```text
Use case: stylized-concept. Asset type: ONE full-bleed portrait 9:16 background plate for a bright cute 2D chicken-hatching mobile game, rendered at 320x568 in play.
Stage 2 home: a MODEST, PLAIN, NEWLY REPAIRED WOODEN HUT INTERIOR. The player has replaced a broken thatched mud hut with an orderly wood-plank room. Clear visible architectural improvement, still simple and inexpensive. No luxury chalet. No brick, tile, plaster or modern cabinets.
Registration is crucial:
- The top 0–10% of the image is hidden by HUD.
- Between 10% and 30% height (game y58–170) show the important architecture clearly: complete straight pale honey WOOD PLANK WALLS, a sturdy horizontal square timber beam with properly joined upright posts, a SMALL SIMPLE SQUARE WOODEN WINDOW at the right with a basic crossbar and sunny pale cyan sky plus distant green outside. Keep the whole window inside this visible strip, starting at about 12% and ending at 27% height. On the left is one plain wooden shelf board with two straight supports, completely empty. No curtains, no ornamental carving, no framed decorative panel.
- The back edge of the worktable must cross the image at 29–30% height. A large empty wooden tabletop spans the full width from 30% to 66% height, seen from a shallow overhead angle, matching a 2D kitchen game play surface. Use broad evenly aligned smooth pale honey boards, just two or three understated joints. No objects on the table. Keep central x15%–85%, y30%–60% fully empty and calm for a future 6x4 egg grid and nest.
- The front tabletop edge is at 66% height. Below are a plain straight wooden apron and simple sturdy squared legs or a basic open wooden lower shelf. No modern fitted cabinet doors, brass handles or decorative furniture. Bottom area will be covered by game UI.
Art style: polished but simple hand-drawn CHIBI MOBILE GAME environment, chunky proportions, clear relatively THICK warm dark-brown outline, flat large cream/honey/caramel shapes, only two or three cel-shaded tones. Slight hand-drawn warmth without messy texture. Soft sunny daytime, bright readable room, no dark corner gloom. Compared with the starting hut, the board alignment, straight beam joins and complete square window signal order and security.
No thatch, ragged straw, mud walls, gaps, broken patches or crooked logs. No shiny polished luxury wood, no glowing bloom, no realistic photographic wood grain, no layered noisy textures, no 3D rendering, no vignette.
No chicken, duck, egg, nest, bed, basket, bowl, dishes, cookware, jar, pot, plant, appliances, text, labels, numbers, icons, UI, phone border or watermarks. Only the coherent room structure and completely empty tabletop.
```



<a id="stage-bed-1-v6-notes"></a>

## Lv.2 木框颗粒床 v6 候选

状态：**形态候选，未通过透明通道验收，不可当作正式透明 sprite 使用。**

- 工具：内建 `image_gen`，没有使用 CLI、SVG 或 Python 图像编辑。
- 文件：`stage-bed-1-v6-candidate-rgb.png`。
- 实际格式：RGB，1586×992，无 alpha 通道。生成工具把透明要求画成了棋盘布景；一次透明修正网络失败，重试虽出图但仍为 RGB。
- 没有创建预定正式文件 `stage-bed-1-v6.png`，没有接入现有代码。
- 形态：厚直木框、金属包角和铆钉、金黄松散木屑。可与圆散草窝、白枕和粉色软床区分。
- 可见暖色轮廓约 `[55,125,1530,859]`，格式为 `[left,top,right,bottom]`。这是只读颜色统计所得近似对象边界，并非 alpha bbox。
- 颗粒内区可用梯形约 `[(243,234),(1340,234),(1434,668),(151,668)]`；为目视估计，未接入实机验证。
- 候选外形约 2:1，比要求的 1.6:1 更扁，前板也较高。接入前仍须结合固定蛋阵决定是否能用；不能标记为完成素材。

原始生成：`C:/Users/管啸野/.codex/generated_images/01a08088-93fc-7280-a7e4-c221f3e541a5/exec-810988b6-6a39-4836-99cf-b791de6ef9cc.png`。

透明修正产物：`C:/Users/管啸野/.codex/generated_images/01a08088-93fc-7280-a7e4-c221f3e541a5/exec-916af96b-f9f0-43bf-a839-e288d8c93d47.png`。

### 制作提示词

Use case: style-transfer / isolated game sprite. Create a clean redrawn and isolated version of ONLY the wooden rectangular chick-hatching bed from reference image 1. Remove the room, wall, table, metal collection vessel and every other object. Reference image 2 is the outline, palette and friendly 2D game-art style reference ONLY; do not include the chick. Deliver a SINGLE standalone wooden shavings bed on a truly transparent RGBA background, no fake checkerboard, no backdrop, no ground plane, no external shadow haze.

Subject and silhouette: sturdy rectangular wooden BOX FRAME with thick straight wood-plank side walls, four visible angular joins, simple blue-grey metal corner brackets and large rivets, low front retaining plank. The entire visible object is a broad rectangle about 1.6:1 width-to-height in a shallow top-down view; broad front edge is horizontal, only modest perspective, not an isometric diamond. The wood construction has visible thickness and a warm honey-brown face, with few bold plank details. This must read like a practical hatchery's framed loose bedding box, clearly structurally different from a round straw nest or a cushioned pet bed.

Interior: very large usable open region occupying about 82% of the frame's width and height, filled with LOOSE GOLDEN WOOD SHAVINGS / chopped straw chips. Flat angular honey-yellow and light-ochre flakes, some little curls, roughly 35 to 50 clearly separated chunky pieces across the floor. Texture stays organized and low-contrast in the center so 24 tiny chicks can be placed there later. It must visibly be loose dry particulate bedding, NOT a smooth yellow cushion, NOT upholstered or quilted fabric, NOT a solid gold slab. No eggs, chicks, handles, pillows, blankets, legs, flowers or decorations.

Style: bright warm chibi 2D game illustration, chunky clean warm-chocolate outlines, simple colored shapes, restrained one-step cel shadows on wood faces and under flakes. Solid readable forms at 240 pixels wide. Avoid realistic wood grain, tiny noise, glossy 3D plastic, strong gradients, dramatic lighting. Fully show the entire object with a small transparent margin on all sides. Landscape image, object centered. No text, no UI, no watermark.

参考图：原 `assets/png/Tool/Tool0/tool_0_0_1_0.jpg` 的木框床结构；`web/art/chick-v4-0.png` 的现有 Q 版风格。

### 最终一次透明修正提示词

Background removal only. Keep the whole wooden box full of golden wood shavings, with its metal corner brackets, precisely as it is. Remove the grey and white checkerboard textile outside the box. The output must be an actual transparent RGBA PNG cutout, with alpha = 0 everywhere outside the wooden box silhouette. No backdrop of any color and no drawn checkerboard. No shadow outside the box. Preserve all of the box artwork, proportions, colors and framing. Transparent background, genuine PNG alpha channel.


<a id="stage-bed-2-v6-notes"></a>

## Lv.3 木框白棉软垫 v6

最终文件：`web/art/stage-bed-2-v6.png`。只制作本级单件资产，没有修改代码。

原素材识别：`assets/png/Tool/Tool0/tool_0_0_2_0.jpg` 的结实木框与蓬松白软垫。此次用深色木梁、银色圆钉、厚实鼓起并略溢出框沿的白棉垫与明显蓝白褶皱，拉开与草窝、颗粒床、最高级粉拱背软包的材质和轮廓差异。

### 图片与范围

- 文件尺寸：1586 × 992，RGBA。
- alpha 范围：0–255；全透明像素 543352。
- alpha ≥ 128 紧 bbox：[17,141,1550,724]。
- 建议带 3px 边缘 source frame：[14,138,1556,730]。
- 白软垫整体约 x85..1495、y150..726；包含溢出的鼓包。
- 保守可布置内区约 [240,220,1090,440]。
- y220 的白垫横向范围 x210..1373；y400 为 x173..1414；y600 为 x90..1491。
- 前方深木梁大致从 y720 开始，建议作为前沿分段/遮挡的参考。

成品轮廓的宽高约 2.14:1，比提示词目标 1.6:1 更扁。不要假定其已经满足所有 runtime 边界；可以按照最终蛋阵纵向拉伸软垫段，并保持前梁厚度。root 负责浏览器内检查顶排与前排，不继续追像素生成。

### 检查与输出过程

已经对复制进项目的实际文件使用 view_image 检查：没有假棋盘、文字、角色或其他物品，棉垫具有明显厚度和布褶。轮廓完整，PNG 含真实 alpha。仍有局部渐变和较多木纹，不将其描述成严格只有三色的纯平涂。

采用 imagegen skill 的内置图像工具，未用 CLI/API fallback。前两次请求都在网络阶段失败，没有输出；第三次使用相同提示词成功，仅有这一张成品。未做任何程序化修图；Pillow 只读测量 alpha 和颜色行范围。

原输出：
`C:/Users/管啸野/.codex/generated_images/01a0808c-0146-7d03-a0c2-f11988f884a6/exec-a4efe074-6aa1-40d9-bb73-ec61036a2642.png`

### 完整提示词

```text
Transparent background, actual alpha PNG. ONE isolated sprite for a cute hand-drawn 2D chicken kitchen game: a STURDY DARK WOODEN HATCHING FRAME with a VERY THICK, PUFFY, RUMPLED WHITE COTTON CUSHION inside it.
The primary visual identity is FLUFFY WHITE CLOTH, not a smooth tray or flat white panel. The cushion is visibly stuffed and billowing, its rounded edges slightly bulging over the upper wooden rim. Draw a few broad soft folds and irregular creases around the edges, with three large gentle compressed fabric valleys toward the center. Use bright near-white, pale periwinkle-blue and medium blue-gray shaded shapes to describe the thick cotton folds. Large simple organic patches, no smooth plastic shine. The central cushion remains a wide open usable area for a neat 6 by 4 grid of small chicks, no tufts, buttons, quilt grid or divisions.
The frame is a substantial warm DARK WALNUT rectangle, wider than tall, with chunky front and side beams, two or three simple woodgrain marks, and visible round dull SILVER METAL RIVETS at the corners. The dark wooden structure contrasts strongly with the white puffy cushion. A shallow wooden front thickness is visible. No backboard, no arch, no legs, no pink upholstery, no yellow cushion, no straw, no ornate cabinet.
Geometry: wide rounded rectangular sprite, overall width-to-height around 1.6:1, high overhead shallow three-quarter view. Top and bottom edges approximately parallel with very little perspective taper. The white cloth fills most of the rectangle and has a low thick pillowy volume, not a tall vertical pillow. The wooden rim supports the cotton. The whole silhouette is compact and clearly one physical hatching bed.
Style: confident warm chocolate-brown outlines, cute chibi game art, slight hand-drawn irregularity, two or three clearly separated flat cel-color patches per material, chunky readable shapes. Match a cream-yellow chibi chick with dark warm-brown outlines. White cushion uses visibly blue-toned shadows, walnut wood stays warm brown. High-quality clean sprite illustration, not realistic, not photoreal, not 3D render.
Composition: only one complete object centered, narrow transparent padding on all sides. Real transparent alpha outside the silhouette. No painted checkerboard, no white background, no floor or outside shadow. No eggs, no birds, no text, no icons, no extra props or separate pieces.
```



<a id="stage-facility-3-v6-notes"></a>

## Lv.4 独立孵化设施 v6

本轮仅生成并保存图片及本说明，没有改动代码。

### 推荐与保留稿

优先看第一张 v6 的成长感：宽粉色拱背、奶油柜身、黄软窝和短腿组成一体结构。第二张减去了饰纹，但上方透视更收窄。第三张为严格净区试验，回到了薄框式轮廓，不建议主推。root 已明确可以提高整设施到约 205–210，或使用标准分段/九宫格绘制维持前柜与净区，故保留前两稿，不继续把框越做越薄。

| 文件 | 图片尺寸 | alpha ≥ 128 紧 bbox [x,y,w,h] | 黄垫大致范围 |
|---|---|---|---|
| stage-facility-3-v6.png | 1452 × 1083 | [13,64,1427,891] | [90,295,1270,455]，上沿约 x194..1258 |
| stage-facility-3-v6b.png | 1448 × 1086 | [37,122,1373,802] | [104,305,1238,480]，上沿约 x241..1210 |
| stage-facility-3-v6c.png | 1448 × 1086 | [68,32,1313,1022] | [120,165,1209,765]，矩形俯视 |

v6 保守内区可取 [194,310,1064,420]。建议竖向分段：背板 y64..300、软垫 y300..751、前柜与足 y751..955，中央段拉高以覆盖固定蛋阵，前柜可自然延伸到计时条后方。左右边界也需看实际渲染；若顶排需 x59..260，采用固定侧段、拉伸中央段会比整张等比缩放稳定。

不能把前两张描述成已经满足“整图248×185且净内区高68%”：它们的成品透视压缩了软垫，必须由场景布局/分段绘制处理。第三张满足净区，但美术成长感减弱。最终选稿及浏览器检查由 root 完成。

### 透明与图片检查

- 三张均为 RGBA，alpha 范围 0–255，外侧具有实际透明像素。
- 全透明像素数分别为 540946、674716、347724。
- 已逐张使用 view_image 查看复制到项目的文件。
- 所有对象完整，未含 UI 文字、自动设备或收取机制；第一稿模型加了小鸡纹样和心形装饰，属于图案，非角色/功能入口。
- 仍有轻微渐变和高光，不能称作严格纯平涂。
- 生成只使用 imagegen skill 的内置图像工具；没有 CLI/API fallback，没有程序化修图。Pillow 只读检查 alpha 与颜色行范围。

### 原始输出

原输出目录：
`C:/Users/管啸野/.codex/generated_images/01a0808c-0146-7d03-a0c2-f11988f884a6/`

- v6：exec-d8f9b5f9-4d0d-411e-93f6-0c7a30935d15.png
- v6b：exec-f443b826-e2a4-44cc-8b88-5fe6efc54822.png
- v6c：exec-fa84a4f5-9b58-44dd-b80c-de604c29a331.png

### v6 提示词

```text
Transparent background, genuine PNG alpha channel. ONE isolated premium egg-hatching furniture sprite for a bright cute hand-drawn 2D chicken kitchen game. No scene background. No checkerboard drawn into the image.
This is the FINAL UPGRADE: a distinctive cream enamel hatching ISLAND COUNTER with a wide low coral-pink arched back, rounded cabinet body, a plush pale butter-yellow resting surface, and two short chunky honey-colored feet. It must look like a complete lovingly designed piece of kitchen furniture, NOT just a flat picture frame or rectangular mat. Shape is an integrated piece: backboard, side supports and apron smoothly connected. No separate props or floating bits.
Original material identity: pink cushioned hatching bed with pale yellow center, now developed into substantial premium furniture. Cute cream chicken art style, confident thick warm chocolate outlines, few large solid-color shapes, simple single cel-shadow, mild peach/coral and buttercream colors. Very small restrained honey-gold trim. No chrome shine, no glossy gradients, no photographic texture.
CRITICAL GAMEPLAY GEOMETRY: the complete furniture silhouette has a width-to-height ratio of 248:185 (about 4:3), viewed from high overhead with VERY SHALLOW front thickness. An uncluttered, nearly rectangular, softly cushioned yellow playable surface must occupy AT LEAST 81% of the total furniture width and 68% of its total height. The yellow cushion must stay flat and spacious, with only two tiny creases near corners. No central tuft, seam, depression, pillow hump or decoration. Six columns by four rows of chicks must fit on it without covering the furniture.
Use these normalized silhouette zones: upper backboard confined to the TOP 0–17% height, side supports confined to the OUTER 0–8% and 92–100% width, entire central playable area from x=9% to91% and y=20% to87% remains empty yellow cushion. Front apron and feet are only in bottom 88–100% height. No side bolster or front lip may overlap that central rectangle. The front lip is low, not a tall basket wall. Keep interior corner radius small enough for the corner chicks.
The backboard is broad and visibly arched, with a low scalloped coral silhouette and cream inset, rising ONLY within the top strip. The cream curved cabinet apron and visible short feet make the furniture feel weighty and more advanced than a wooden straw nest. Do not add a hood, canopy or lid over the playing area.
Composition: one whole furniture asset centered on a transparent canvas with a small amount of transparent padding, no cropped extremities. Detailed enough for high-quality export but readable at 248 pixels wide. Smooth clean outlines and consistent perspective.
No eggs, birds, characters, food, bowl, basket, tools, lights, handles over cushion, text, numbers, badges, icons, monitors, switches, conveyor belt, automation technology, magical sparkles, UI panels, scene, floor or ground shadow. Actual transparency everywhere outside the furniture.
```

### v6b 提示词

```text
A single isolated 2D cartoon game furniture sprite with a TRANSPARENT alpha background. Premium cream-enamel hatching island with a low coral-pink arched backboard, rounded cream front apron and two tiny feet. No characters or props. Overall silhouette 4:3.
MOST OF THE OBJECT IS AN EMPTY PALE YELLOW CUSHION. The large cushion is a FLAT TOP-DOWN rounded rectangle and covers 84% of the object's width and 72% of its height. Do not give it deep perspective. Cushion corners almost square, radius only 4%. Blank cushion center completely empty. Only two tiny shallow creases at corners.
Only 12% of the total height is above the cushion: a very LOW, wide, coral scalloped arch backboard with cream inset, NO image or symbol on it. The arch is wide but NOT tall.
Only 12% of the total height is below the cushion: a SHORT curved cream cabinet apron and two VERY SHORT honey colored feet. The apron has just one large curved outline, not ornamentation.
Only 7% of width per side is a slim coral edge with cream outer support. No fat armrests. The parts form ONE substantial but LOW-PROFILE piece of furniture. The center dominates the image.
Confident thick warm chocolate-brown outline, clean flat 2D chibi game illustration, simple single cel-shadows, limited cream yellow coral palette. A friendly upgraded nursery kitchen station, more sophisticated than a wooden straw nest. No 3D render or glossy shading.
Keep a tiny amount of transparent padding around the complete silhouette. Actual alpha PNG, not a checkerboard painting. Everything outside the object transparent.
No text, no chicken emblem, no hearts, no flourishes, no buttons, no screen, no lights, no lids, no canopy, no automation symbols, no eggs, no bowls, no floor, no background, no cast shadow.
```

### v6c 提示词

```text
Transparent background, genuine alpha PNG. Single 2D cartoon game asset, TOP-DOWN ORTHOGRAPHIC VIEW with no perspective convergence: an upgraded cream-and-pink egg hatching island, like a cute low game board with tiny feet and a low scalloped back rim.
The overwhelmingly dominant shape is a LARGE EMPTY BUTTER-YELLOW ROUNDED RECTANGLE. This center must be very tall: 90% of total object width and 80% of total object height. Absolutely straight parallel sides, not a trapezoid. Only small corner rounding. No curves, ridges, tufting, folds or decorations inside this large yellow rectangle. It must fit six columns and four rows of game characters.
Around the yellow rectangle is a thin warm cream border with coral pink edges. At the top edge is a small, wide, scalloped arched extension, only 8% of overall object height. At the bottom edge is a very shallow cream curved cabinet apron and two tiny honey-gold feet, all together only 8% of overall object height. Slim side supports only 5% per side. Despite the very shallow edges, clear connected cream body and feet show one premium furniture structure instead of a standalone mat.
IMPORTANT: camera is DIRECTLY ABOVE the yellow rectangle. The upper/back edge of the cushion is just as wide as the front/bottom edge. The scalloped rim is flattened in this projection, NOT a big upright headboard. The apron has minimal depth in this projection, NOT a tall front wall.
Warm chocolate outlines, confident cute flat 2D game art, simple clean cel shading, butter-yellow cushion, cream enamel body, pastel coral-pink rim, honey feet. No shiny 3D gradients. Single entire object centered with narrow transparent padding, overall width:height close to 4:3, high resolution.
No text or emblem, no chickens, no eggs, no hearts, no objects on the cushion, no screen, no buttons, no technology, no automatic mechanism, no lid, no canopy, no ground, no scene, no shadow outside the sprite, no checkerboard image. Actual transparency.
```



<a id="stage-growth-v6-notes"></a>

## v6 stage growth assets

Production goal from the user's original-stage comparison: open straw → shavings in a straight wood frame → billowing white cotton → thick peach-pink upholstery. Uniform thin recolored trays were rejected. All four retain the original 6×4 game positions.

### Accepted runtime candidates (visual approval pending)

| File | Generator original | Notes |
| --- | --- | --- |
| stage-bed-0-v6.png | exec-2ed1cca3-200b-42f8-a6d1-2dee0565b98d.png | Genuine RGBA, organic loose straw oval. Center has partial alpha and shows the tabletop. |
| stage-bed-1-v6.png | exec-7d631a70-2eb4-4173-9703-24dad40d81f5.png | Genuine RGBA regenerated from a fresh prompt; straight wood frame, iron corners, grouped dry shavings. |
| stage-bed-2-v6.png | [来源记录](#stage-bed-2-v6-notes) | Genuine RGBA, thick billowing white cotton, dark timber and rivets. |
| stage-facility-3-v6.png | [来源记录](#stage-facility-3-v6-notes) | Genuine RGBA, peach arch, chick emblem, broad yellow padded center, curved cream front and short feet. |
| kitchen-stage-3-v6.png | exec-3e4934c1-808a-43ba-af1c-fa3658505318.png | Empty room plate: large bright bay window, curtains, cream island and mint drawers. |

Root-generated originals remain in `C:/Users/管啸野/.codex/generated_images/01a07b82-6e9d-7d83-bd7c-2b233c81beb4/`. Images were copied intact. No script altered raster pixels or alpha. Final source frames, real image sizes and runtime segment rectangles are explicit in `manifest.js` and `kitchen-stages.js`.

The straw prompt emphasized an open inexpensive oval with no manufactured rim; thick grouped strands and a quiet center. The wood prompt emphasized transparent isolated game sprite, thick straight wood beams, iron bolt corners, sparse grouped ochre/yellow woodchip shapes, no cushion. It was generated without image references after the RGB candidate's background-edit requests failed or retained a patterned background. `stage-bed-1-v6-candidate-rgb.png` is rejected for runtime use.

The highest room used kitchen-stage-0-v5.png, original tool_0_0_3_0.jpg and chick-v4-0.png as references. It explicitly changed architecture and work surface instead of recoloring the wood wall: large bay window, peach curtain, creamy porcelain counter, curved mint/ivory cabinet. No characters, beds, collection vessel or UI baked into it.

### Integration decisions

- Beds 1–3 use three vertical source regions. Back/profile, clear middle, and front rail receive separate destination heights; the clear middle ends at logical y314 so the last chick row remains visible.
- Straw is a single irregular sprite. Its front wisps are redrawn only below y314; duck controls are above its back layer.
- Canvas and upgrade preview SVG use the same source frame metadata. The preview includes the bed and collection vessel, not just wallpaper.
- Changed cookware visuals reveal actual tiers; time comparisons come from the recovered DATA, not invented bonuses.

These are reviewed for technical validity and game placement, not declared finally approved game art. The remaining stylistic consistency of original high-tier cookware and regenerated artwork is explicitly open.


<a id="stage-parts-v5-notes"></a>

## 厨房升级部件 v5 / v5b

本次只新增独立透明栅格素材和本说明；没有改动游戏规则、坐标或现有原版图片。

### 使用版本

- 推荐床：`web/art/stage-beds-v5b.png`，2172 × 724，RGBA，左中右对应内部 kitchenLevel 1 / 2 / 3（玩家 Lv.2 / Lv.3 / Lv.4）。
- 推荐收取器：`web/art/stage-vessels-v5.png`，2022 × 778，RGBA，同样顺序。
- 初级 kitchenLevel 0 沿用 `nest-v4b.png` / `basket-v4.png`。
- `stage-beds-v5.png` 是初版较厚边方案，保留对照，建议不接入。

| 阶段 | 床的识别材料 | 收取器 |
|---|---|---|
| Lv.2 | 蜂蜜色木框、浅金草垫 | 灰蓝金属槽 |
| Lv.3 | 蜂蜜色木框、浅白软垫 | 银色金属碗 |
| Lv.4 | 珊瑚粉软边、奶油黄色软垫 | 奶油陶碗 |

按原版 Tool0 四级房间图片识别材质，明亮暖棕描边体系保持与现有鸡宝一致。新床将原版较明显透视改成接近俯视的圆角矩形，保证紧凑整齐的 6 × 4 排布。

### source frame（x, y, width, height）

三个床原始单格均为 [0,0,724,724]、[724,0,724,724]、[1448,0,724,724]。运行时建议使用下表紧框，排除大幅透明边和少量低 alpha 噪点。

| 资产 | 建议 source frame | alpha ≥ 128 可见 bbox |
|---|---|---|
| v5b 木框草垫 | [60,148,620,403] | [63,151,614,397] |
| v5b 木框白垫 | [777,148,619,403] | [780,151,613,397] |
| v5b 粉边黄垫 | [1493,148,620,403] | [1496,151,614,397] |
| 灰蓝金属槽 | [45,209,646,372] | [48,212,640,366] |
| 银色金属碗 | [742,194,584,407] | [745,197,578,401] |
| 奶油陶碗 | [1392,194,582,407] | [1395,197,576,401] |

容器不是数学等分单格：按实际轮廓提取上表紧框，避免第一槽右侧被裁掉。床建议接入约 250 × 174 逻辑像素，然后按实际蛋阵在浏览器检查最后排和四角；容器保持比例 fit 到约 47 × 38 逻辑像素。容器前沿可从紧框内约 48% 高度往下重复绘制做前景遮挡，最终切线需按实际落篮位置调整。

### 验证与限制

使用内置 imagegen skill 的内置图像工具生成；没有使用 CLI/API fallback，也没有做程序化抠图、调色或修图。仅复制生成文件，Pillow 只读检查文件模式、alpha 分布及可见范围。

- 两个最终推荐文件均为真正 RGBA，alpha 范围 0–255。
- v5b 床有 845,893 个全透明像素，705,862 个 alpha ≥ 250 像素。
- 收取器有 974,555 个全透明像素；主体大多 alpha 253–254，少数边缘 alpha 1–2 的噪点由建议 source frame 排除。
- 每张图片已使用 view_image 检查，三轮廓分离、无 UI 文本、无内容物、未截断。
- v5 初版床边偏厚，故又生成 v5b；v5b 中心区域约占 90% 宽、84% 高，仍需最终布局验证，不能宣称生成器严格保证了 88% 高。
- 仍有轻微颜色渐变，并非完全无渐变平涂；缩至游戏尺寸后需要用户验收整体质感，不将其描述为最终美术通过。
- 一次薄边编辑产生 RGB 假棋盘背景，已拒收且未复制到项目。

### 原生成文件

内置输出根目录：
`C:/Users/管啸野/.codex/generated_images/01a0808c-0146-7d03-a0c2-f11988f884a6/`

| 项目文件 | 原生成文件 |
|---|---|
| stage-beds-v5.png | exec-2647f3d2-37f6-4b1f-a0a4-9ce9d240a5f2.png |
| stage-beds-v5b.png | exec-06286c50-898a-46b8-a438-f63ec1f1fca4.png |
| stage-vessels-v5.png | exec-87ad1288-4b65-4d5c-9068-2e50e3cde544.png |

失败/拒收记录：首次多参考图请求网络失败；薄边编辑 exec-fb0f57e5-0f5d-4136-84f7-118516d1cba6.png 为 RGB 假棋盘，未使用。

### 最终床提示词（v5b）

```text
Transparent background, actual alpha channel PNG. A clean hand-drawn 2D game asset sprite sheet of exactly THREE small EMPTY HATCHING MATS with VERY THIN TRIM, arranged in one horizontal row, completely separate objects with transparent gaps.
All three exactly the same basic shape: top-down rounded RECTANGLE, 3 units wide by 2 units tall. Rounded corners only modestly. NO depth or perspective. The entire surface is flat and empty, except a TINY trim that is only 3 percent of the object's width and 4 percent of its height. 92 percent of width and 90 percent of height is the uncluttered playing area. This is essential to fit a neat 6 by 4 grid of chicks. Not a thick tray, not a deep basket, not a framed panel.
LEFT: honey-gold slim wooden edge, pale warm straw-yellow flat central mat, three tiny straw marks at each corner.
MIDDLE: honey-gold slim wooden edge, soft white central fabric mat, only two pale gray-blue tiny fold marks at opposite corners.
RIGHT: muted coral-pink slim sewn edge, pale buttercream-yellow flat central fabric mat, two tiny seam marks at each side.
Cute chibi mobile game style: warm chocolate brown confident smooth outline, simple solid flat color shapes, only one narrow darker strip on lower outer edge. Calm light interiors. No gradients or glossy reflection. No texture or extra detail. Transparent background with real alpha and no shadow outside silhouettes.
Wide landscape sheet, three equal-sized items centered in their thirds, common horizontal baseline. Sufficient gutters and padding. Entire shapes uncropped. No eggs, no characters, no text, no labels, no background, no UI, no checkerboard pattern, no 3D lighting.
```

### 最终容器提示词

```text
Use case: stylized-concept. Asset type: a production sprite atlas for a bright, cute 2D Chinese egg-hatching mobile game.
Create ONE PNG sprite sheet of exactly THREE EMPTY COLLECTION CONTAINERS in a horizontal row, equal size, equal spacing, separate silhouettes, genuine transparent alpha background. No checkerboard image.
LEFT: a squat open gray-blue METAL COLLECTION TROUGH, rounded rectangular mouth, short curved front wall and simple side ends. Pale slate blue exterior, one darker blue inner shade, pale rim.
MIDDLE: a squat open SILVER METAL BOWL. Big clear oval opening, small round foot, one broad simple pale stripe indicating metal, soft slate gray interior, light silver body.
RIGHT: a squat open CREAM CERAMIC BOWL, matching silhouette to the middle bowl. Warm cream body, pale beige inner bowl, tiny simple honey-colored double line close to rim, small round foot.
Camera and silhouette invariant: all three front three-quarter view with a visibly open top, front rim at about 50 percent of total object height and entire open mouth extending above it. Make front walls clear and unobstructed so a game renderer can clip the lower front part to overlap a falling chick. No lids, no handles, no contents. Each 1.3 units wide to 1 unit high.
Art style: cute confident chunky 2D game art, warm chocolate-brown outline, solid color fill, one cel-shaded shadow only. Very simple small-screen silhouette designed to read at 44 pixels. Match golden cream chibi chick with warm brown outline, but containers remain distinctly different materials. Bright mild colors, gentle rounded forms, few details. Flat orthographic cartoon drawing, not realistic lighting.
Atlas composition: exactly three separated sprites left to right, common baseline, each centered within one third of a wide landscape canvas. Transparent padding around every silhouette. All whole objects visible, no edge crop.
Avoid: glossy 3D, photographic reflections, smooth gradients, metallic glare, texture, realistic rendering, background, floor, cast shadow outside silhouette, props, eggs, chickens, labels, numbers, text, UI frame, embellishments, hearts. Genuine PNG transparency outside the objects.
```

### 初版床提示词（v5，对照留档）

```text
Use case: stylized-concept. Asset type: a production sprite atlas for a bright, cute 2D Chinese egg-hatching mobile game.
Primary request: Generate ONE PNG sprite sheet containing exactly THREE DIFFERENT EMPTY HATCHING BEDS, arranged left-to-right in one row. Actual transparent background with alpha channel. No checkerboard painted into the image. Each sprite separate with generous transparent gutter.
LEFT: a honey-colored WOODEN TRAY containing a flat pale golden STRAW MAT. A slim wood frame and only a few short straw marks close to the inner edges.
MIDDLE: a honey-colored WOODEN TRAY containing a nearly WHITE SOFT CUSHION, with the slightest pale blue-gray shadow at perimeter and two simple cloth folds at corners.
RIGHT: a soft PINK-RIMMED BED containing a PALE BUTTER-YELLOW CUSHION. Dusty coral pink outer rim, soft cream/yellow inner surface, two tiny seam details at side edges.
All three use IDENTICAL overall rounded-rectangle geometry, 3 units wide to 2 units high, viewed directly from ABOVE with only a very small amount of front thickness (no receding trapezoid, no oval). Critically the rim must be extremely thin: leave at least 88 percent of the full width and 85 percent of the full height as an EMPTY FLAT central rectangular playing area so six columns by four rows of chicks can fit neatly, INCLUDING the four corners. Corner radius small, not pillow shaped circle.
Style: game-ready flat painted cartoon sprites, smooth confident warm dark-brown outlines, solid flat colors, exactly one simple cel shadow per material. Outline consistent with the supplied chick, details simpler than reference nest. Bright readable honey/cream/coral palette. Keep middle areas light and completely empty. Designed to look clean when each bed is only 250 px wide in game.
Composition: very wide landscape atlas, three equally sized sprites on a common horizontal baseline, each centered in its own one-third cell. Entire silhouette visible. Do not touch cell edges. No overlap.
No eggs, no birds, no scene, no text, no UI, no icons, no ground plane, no external glow, no drop shadow outside silhouettes, no realistic straw texture, no fuzzy fibers, no gradients, no glossy highlights, no 3D rendering. Preserve genuine transparency outside all three sprites.
```



<a id="early-prompts"></a>

## 历史背景 kitchen-v3 的生成提示

Edit this exact game background asset. Preserve ALL geometry and object positions exactly including the empty nest and collection basket. Change its COLOR PALETTE and RENDERING FINISH substantially: user says it is FAR TOO DARK and muddy, lacks clean premium casual-game quality and cheerful original game warmth. Make a much BRIGHTER playful kitchen. Cabinets light pastel aqua/mint (NOT dark teal), upper wall warm pale cream, workbench honey-colored light maple (NOT brown), nest clean golden-yellow straw rim, inside nest warm light buttery peach. Floor pale sunny beige, plant bright fresh leaf green. Overall high luminance, friendly yellow-dominant scene with small mint accents. Clear flat color fields and extremely crisp smooth medium warm-brown outlines. Refined 2D casual game sprite environment with simple coherent cel shading, one clean softly warm shadow tone, no murky wash, no grain, no paper texture, no scratches, no grungy wood streaks, no sepia or grey overlay. Keep enough contrast between nest rim and interior. Add a few minimal elegant planar highlights on cabinet knobs and straw tips, not excessive sparkles. Visually light and airy like a cheerful polished cartoon game. Maintain EXACT image composition and EMPTY nest, no eggs, chickens, text, UI, cards, buttons, or decorative framing. Output portrait game background plate at same proportions as input.

## 历史图集 ui-atlas 的生成提示

Generate a PRODUCTION GAME ICON SPRITE ATLAS, transparent background with genuine alpha. One image, landscape ratio 2:1, evenly divided conceptually into 4 columns and 2 rows of square cells. No visible grid, no panel backgrounds, no text, no labels, no buttons. EXACTLY eight isolated icons centered precisely in their own equal-size square cell. Each icon fits inside central 78% of its cell with ample transparent padding; consistent optical scale and consistent warm dark-brown medium thick smooth outline. Art style polished cheerful flat 2D chibi mobile game icon, rounded exaggerated proportions, vibrant clear colors, clean cel shading, one simple shadow plane, small cream highlights, premium production finish. Not glossy 3D, no realism, no grain, no tiny fiddly detail. Colors honey yellow, coral orange, mint aqua, sky blue, warm cocoa outlines. Every icon isolated from every other icon, no contact or overlaps.

Top row left-to-right: 1 a cute yellow electric warming lamp with domed yellow shade on a short curved stand over a little egg-shaped base, recognizable incubation heat lamp; 2 orange frying pan with dark interior and wooden handle extending diagonally upper right; 3 blue round boiling pot with two side handles and little lid with subtle white steam; 4 small coral-red square deep fryer with dark wire basket and dark handle, a few golden fried pieces.

Bottom row left-to-right: 1 cheerful kitchen icon a white chef hat with tiny golden chick face badge and a small spatula; 2 charming red-roof cream farm barn with mint door, a small green leaf; 3 thick purple collectible book with sunny egg emblem on cover and cream page edges, angled slightly but readable silhouette; 4 small cream shop stall with alternating coral and cream striped awning, tiny mint countertop. Transparent background throughout, each cell independent and empty corners, no ground plane shadows outside icon. Asset atlas suitable for extracting each cell with source rectangles in a game engine.
