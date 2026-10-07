# Farm · World Scale + Camera Prototype Gate

日期：2026-09-29。待用户与 Chat 判断世界规模与镜头，不自动冻结。

## 交付

- 交互页：`http://127.0.0.1:4173/web/prototypes/farm-world-camera-gate-20260929/index.html`
- [更新后的尺度规则](FARM-SCALE-BIBLE.md)
- [Camera 操作、边界、标签与 Pinch 规范](CAMERA-SPEC.md)
- [世界尺寸对照](../../../../web/prototypes/farm-world-camera-gate-20260929/evidence/world-size-comparison.png)
- [推荐 A 世界三档对照](../../../../web/prototypes/farm-world-camera-gate-20260929/evidence/three-zooms.png)
- [A 远景](../../../../web/prototypes/farm-world-camera-gate-20260929/evidence/a-far-390x844.png) / [A 默认 390×844](../../../../web/prototypes/farm-world-camera-gate-20260929/evidence/a-default-390x844.png) / [A 近景](../../../../web/prototypes/farm-world-camera-gate-20260929/evidence/a-near-390x844.png)
- B 同样保留远 / 默认 / 近三张原尺寸截图，位于同一 evidence 目录。

## 世界尺寸比较

| 维度 | A：40 × 72 WU | B：56 × 100 WU |
| --- | --- | --- |
| 默认世界相对镜头跨度 | 约 2.05 × 2.07 倍 | 约 2.87 × 2.87 倍 |
| 默认世界相对镜头面积 | 约 4.24 个镜头 | 约 8.25 个镜头 |
| 默认可见建筑 | 完整农舍 + 大部分补给 | 完整农舍 + 补给边缘 |
| 资产 / 路宽 / zoom | 与 B 相同 | 与 A 相同 |
| 观看结果 | 中央有空间，下方入口需浏览抵达 | 空间更舒展，同样四功能点显得更分散 |
| 建议 | **推荐作为第一版世界尺度** | 保留对照，暂不因“更大”直接采用 |

所有截图外框都是 390 × 844，不按世界尺寸重新缩小整个场景来塞满默认视口。世界 A 默认是 800 × 1440 屏幕像素的地图，通过 390 × 696 的 Camera 窗口浏览；远景全览是主动操作。

## 六项结论

1. **世界规模：推荐 A 的 40 × 72 WU。** 约四个默认镜头面积；已有四个功能点之间能产生浏览距离。B 约八个镜头面积，在当前内容量下主要增加安静空间和拖动行程。
2. **默认 zoom：0.625。** 标准居民约 20 px，农舍宽 200 px。默认只看一栋完整建筑及另一栋的大部分，神社与陈列留在下方；下方方向提示和小地图表达世界继续延伸。
3. **鸡宝比例：静态与浏览测试中合理，仍待用户确认。** 比上一版 24–27 px 更小，约占农舍高度的 12%；人物是居民而非主视觉块。远景居民会降到 7–10 px，只辨别位置，近景恢复到约 32 px 看角色。点击热区尚未实现，不因小尺寸直接扩大原画。
4. **仍需返工的资产：** 农舍 `ASSET NEEDS REWORK`：近景门洞是较大的黑色平面，局部渐变与 SVG 环境不一致；陈列亭 `ASSET NEEDS REWORK`：空台座远看仍容易读成普通棚子，展示语义需增强；树木 `ASSET NEEDS REWORK`：重复树冠与几何松树在近景和大面积边界中风格差异更明显。补给 / 神社暂可用于尺度判断，未宣布最终美术通过。水盆 / 凳 / 围栏 / 营火在这一世界尺度中退为小件；本轮全部沿用素材，不生成修图。
5. **Camera 可用性：** 双轴鼠标拖动与单指触摸、四边约束、三档缩放、指针中心滚轮、归位、键盘、建筑定位、标签 LOD 已通过自动浏览器检查；HUD / 导航保持固定。完整移动端 Pinch 尚未实现。
6. **是否进入 Layout Freeze Gate：值得进入评审。** 世界尺度与 Camera 已可独立判断，不再受“一屏四栋”的前提限制。但本轮没有批准冻结；先由用户确认 A / B 与默认镜头，下一轮再细定四建筑坐标、入口路线和安静空间。

## 验证证据

`evidence/verification.json` 保存本轮六张截图的 Camera 坐标、实际建筑宽度、角色高度、可见建筑数量，以及输入 / 边界检查结果。验证脚本还检查两种世界保持同一资产尺度，默认不是全图，HUD 与底部导航在拖动后位置不变。

复现：仓库根目录启动现有 `node server.mjs 4173`（已运行则复用），打开交互页；运行 `node web/prototypes/farm-world-camera-gate-20260929/verify.mjs` 可重新导出六张截图并复核行为。此脚本使用本机 Chrome / Playwright，不操作正式存档。

ImageGen = **0**；没有修改任何旧 Asset、正式 Runtime 或 Kitchen 文件。只增加独立原型和本轮文档。停止在 World Scale + Camera Prototype。
