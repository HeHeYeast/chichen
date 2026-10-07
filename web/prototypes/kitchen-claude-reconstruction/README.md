# Kitchen Lv.2 主界面重构候选（独立原型）

不被游戏加载，不改正式 Runtime / 资源。启动 `node server.mjs 4180` 后打开：

- `index.html?v=A|B|C|D` — A 木台正视 · B 本锅/下一锅 · C 木屋剖面 · D = C′（C + 左上调味，推荐混合）
- 追加 `&state=ready` 看可收取状态；任意视口尺寸均自适应（320×568 已验证）
- `board.html` — 对照板（Runtime + A/B/C + 功能区叠图 + 说明 + 压力测试 + C′ 判断）；`board.html?pair=A|B|C` 为双列对照

`node web/prototypes/kitchen-claude-reconstruction/capture.mjs http://localhost:4180` 把全部截图输出到 `artifacts/kitchen-claude-reconstruction-20260928/`。

## 口径

- 蛋群：`engine.js startBatch()` 同一排布公式 + `egg-v4` / `stage-bed-1-v6` 原素材与分层；只整体缩放，不重排。
- HUD 为 `scene.js header()` 的画布移植，五入口为已认可导航样式；厨具、调味、扫帚、角色均为现有资产。
- 木墙、屋架、柜台、槽位、吊牌、木牌为 SVG / CSS 程序化绘制。
- `ref/runtime-lv2-390x844.png` 复制自 `artifacts/kitchen-zone-balance-20260928/current-kitchen-lv2.png`，仅供对照板使用。
