# 应用图标：参考原版的调整｜2026-09-19

用户要求：当前应用图标不美观，希望参考原本游戏。沿用此前美术反馈，优先原版角色与风格一致，不重新创造泛用小鸡形象。

## 本次实现

- 原版参考：`assets/png/icon.png`（256×256）。原包 `res/drawable-xhdpi-v4/icon.png` 等也已查看。
- 新彩色素材：[`web/art/app-icon-v2.png`](../../../web/art/app-icon-v2.png)。
- Android 正式资源：`android/app/src/main/res/drawable-nodpi/ic_launcher_art_v2.png`，生成原图直接复制，无脚本修图。
- `ic_launcher_foreground.xml` 改为引用该位图；正常及圆形自适应图标共用这一前景。
- 新增 `ic_launcher_monochrome.xml`，为系统单色主题提供带原版头顶小毛与张嘴特征的简化轮廓。正常/圆形入口均绑定；通知图标未改。
- 调整图标背景色为奶油色。
- 图标原有矢量与配置备份于本目录 `previous/`。
- [预览页](http://127.0.0.1:4173/web/icon-review.html) 同时显示原版、修改前、修改后与不同尺寸/遮罩。

新图保留原版鸡宝探头的构图、三笔小毛、深棕圆眼、张开的橙色嘴、腮红与短翅，以及锅具、浅蓝盘子、奶油墙与赤陶色横带。删除了原图左上蓝色徽标和横带上的小标记，墙面花纹减少。结果存在少量柔和渐变，不将其描述为严格纯平涂或像素级原样。

## 验证与范围

- Android SDK 35 的 aapt2 对全体应用资源执行 compile 和 link，均通过；链接使用 min API 26 / target API 35。
- 资源检查产物位于 `artifacts/qa/app-icon-20260919/`。其中 `linked-resources.apk` 仅为资源链接检查包，不是可安装的完整应用。
- 内置浏览器查看了原版/修改前/修改后的对照，及 48px、72px 圆形与圆角方形、72px 单色图标；六张位图均成功加载。眼睛和嘴在已查看的遮罩中可见，轮廓/下半身体按原版探头构图有所裁切。
- 预览用 108 单位画布中央 72 单位区域模拟自适应遮罩，属于浏览器外观检查，不能替代所有手机桌面或动态效果的真机验收。
- 没有改版本号、签名、安装应用或操作手机存档；未构建完整发布 APK。
- 本地 agent-browser 命令不可用，实际外观检查使用内置浏览器工具完成。

## 生成来源

使用内置 image_gen，1 次编辑生成；没有使用 API/CLI fallback。

参考图：`D:/gxy_code/game/chicken_duck_test/assets/png/icon.png`

生成源：`C:\Users\管啸野\.codex\generated_images\01a0b8de-fdb3-70b3-bf39-122b00673ec1\exec-a47f3840-64f6-4215-b142-8558d56d3393.png`

## 完整提示词

```text
Use case: precise-object-edit.
Asset type: Android adaptive launcher icon artwork, ONE square raster icon, full bleed.
Edit the supplied ORIGINAL GAME ICON conservatively, not a generic new mascot. Preserve its visual identity and old 2D hand-drawn game style: a pale yellow egg-shaped chick peeking from the lower/right foreground, a tiny three-stroke dark hair sprout (NOT a red chicken comb), round dark-brown eyes with a tiny single white highlight, pink cheek dashes, an OPEN orange beak with the original funny cheerful expression, short wings. Behind/left, a simple pale-blue oval dish and a small hanging pan/ladle communicate the kitchen. Cream wall and muted terracotta upper band. Keep the specific original chick facial proportions and personality as closely as possible.
Remove the little blue publisher badge at upper left and the small emblem on the terracotta band. Reduce the repeated pink wall decoration to just a few subtle large shapes, or omit if busy. Do not add text, initials, a title, logo, sparkle, chef hat, red comb, extra bird, limbs, food or new props.
Style: crisp but slightly handmade warm-brown line art, clean flat pale-yellow/cream/terracotta/blue color blocks, almost no gradient. A single restrained cel shadow is enough. Faithful early mobile-game cartoon illustration, NOT 3D, not glossy, not plush, not shiny vector clipart, not pastel kawaii redesign. Do not over-polish the original drawing.

ADAPTIVE ICON COMPOSITION is important: the phone will crop the image around its CENTER to a circle or rounded square. Create a square full-bleed cream kitchen-wall field continuing all the way to the four square corners, with no pre-rounded corners, white outer margins, visible frame, circular badge or baked drop shadow.
Recompose only enough to keep the main facial features and hair tuft within the CENTRAL 60% of the entire square (x20–80%, y20–80%). The chick's face is near x58%, y53%; its head/body silhouette occupies roughly x38–82%, y28–83%, and its lower body can continue a little further down. Move the chick slightly inward compared with the reference so neither eye nor the beak is cropped by a circular launcher mask. The little hanging pan/ladle sits behind on the left near x32%, y42%, the blue dish is behind near x34%, y64%. The terracotta beam is a quiet strip near y20–30%, not a roof or an extra frame. Keep everything small-detail-free and legible when the CENTRAL 66% crop is displayed as a 48px icon.
Leave enough plain cream surroundings for the system's adaptive masking. Prioritize the original chick's identifiable happy face over background detail. The wall and utensil shapes remain secondary. Output just one clean square icon master, not an icon sheet, not a phone mockup, not multiple options.
```

