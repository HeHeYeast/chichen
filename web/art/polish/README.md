# Visual Polish 资源接口

本目录是 2026-09-24 Game UI Pass 的原生矢量资源。与现有暖棕描边、奶油纸张、草绿／麦黄／溪蓝配色对应；没有修改、重采样或覆盖原作位图。生成源为 `tools/build-polish-art.mjs`，执行 `node tools/build-polish-art.mjs` 可重建。

机器清单：[inventory.json](inventory.json)。显示接口：[visual-assets.js](../../visual-assets.js)。资源原始制作方式是代码绘制 SVG，不使用外部下载图片。

| 目录 | 数量 | 尺寸 / 安全区 | 文件名 | 当前状态 |
|---|---:|---|---|---|
| species | 48 | 128×128；轮廓位于约 12–116；足底 y=108 | species-V-C1.svg 等 | 可区别的概念立绘，非正式角色定稿 |
| materials | 8 | 128×128；内容约 18–110 | material-75.svg … material-82.svg | 可交付的小尺寸矢量图标 |
| discoveries | 24 | 128×128；圆章边界 10–118 | discovery-V-S1.svg 等 | 按地区与标本/见闻/事件分类的概念章；独立故事插画待补 |
| regions | 4 | 400×160；允许横向居中裁切 | region-V.svg / R / T / B | 地区路线小景概念稿 |
| mementos | 12 | 128×128；物件约 18–112 | memento-M01.svg … M12.svg | 图形占位，正式物件定稿待补 |
| ui | 1 | 128×128 | unknown.svg | 未知蛋形与问号标记 |

## 替换方式

1. 稳定 ID 与存档 ID 不变。SVG 可以直接按同名文件替换；需保持 viewBox、留白与底部锚点。
2. 换成 PNG/WebP 时，在 `web/visual-assets.js` 添加对应 ID 的显式映射，并更新 inventory 中的 path、尺寸、制作来源与 status。新增物件仍通过统一入口渲染。
3. 正式地区角色资源的权威入口仍为 `runtime-assets.generated.js` 背后的内容源。现有 full=512×512、portrait=256×256、silhouette=256×256 规划不变；完成后从内容源生成，不直接手改 generated 文件。`catalog.js` 只在权威资源仍指向 `regional-concept.svg` 时回退到这里的概念立绘。
4. 原始 120×120 角色画布在 Canvas 中缩放；UI 通过 `characterPortrait()` 渲染。建议最终 full 图保留透明背景、足底锚点及角色可见边界元数据，避免农场中大小跳变。
5. 未发现的名称、配方和彩图继续受 visibility projection 控制。标本找到后可显示材料图，商店只有辨认后显示正式商品图；不能仅靠 CSS 模糊隐藏信息。
6. 重新生成 `node android/package-runtime.mjs` 并验证 `android/generated-assets/runtime-manifest.json`。打包器显式读取本 inventory，覆盖动态拼接的路线、发现章和纪念物路径。

本目录的 character/discovery/memento 概念稿不构成最终美术验收。重建会覆盖本目录生成文件；正式资源应登记后移至正式路径，或同步更新生成源，避免下一次重建覆盖。
