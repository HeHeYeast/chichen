# 资产清单、规范与提示词

统一规范：[生成前参考笔记](TATA-REFERENCE-NOTES.md)。产物根目录：`web/prototypes/farm-modular-asset-gate-20260928/assets/`。

## 建筑 PNG

| 资产 | 使用稿 | 提示词 | 状态 |
|---|---|---|---|
| 农舍 | [house.png](../../../../web/prototypes/farm-modular-asset-gate-20260928/assets/house.png) | [首稿](PROMPT-HOUSE.txt)＋[定点修正](PROMPT-HOUSE-EDIT.txt) | 采用修正版；[首稿](../../../../web/prototypes/farm-modular-asset-gate-20260928/assets/house-study.png)仅追溯 |
| 补给摊 | [shop.png](../../../../web/prototypes/farm-modular-asset-gate-20260928/assets/shop.png) | [提示词](PROMPT-SHOP.txt) | 1 个候选 |
| 神社 | [shrine.png](../../../../web/prototypes/farm-modular-asset-gate-20260928/assets/shrine.png) | [提示词](PROMPT-SHRINE.txt) | 1 个候选 |
| 陈列亭 | [display.png](../../../../web/prototypes/farm-modular-asset-gate-20260928/assets/display.png) | [提示词](PROMPT-DISPLAY.txt) | 1 个候选 |

[共享提示词](PROMPT-SHARED.txt)。所有建筑使用内置 ImageGen，透明背景设为 true；共 5 次调用，不使用 CLI 后备，不生成整图环境，也没有对 PNG 进行程序调色、抠图或形状重绘。缩放和裁显示边界属于浏览器排版，不改变源像素。

## 独立环境 SVG

两树：`tree-pine.svg`、`tree-round.svg`；两灌木：`bush-low.svg`、`bush-tall.svg`；两石头：`rock-wide.svg`、`rock-tall.svg`；两围栏：`fence-front.svg`、`fence-return.svg`；生活：`bench.svg`、`campfire.svg`、`basin.svg`；边缘：`edge-path.svg`、`edge-grass.svg`、`edge-stone.svg`。

共 14 个，都是代码原生、无背景矩形的独立文件。平面色块、统一墨色、上左亮／右侧暗，无空气渐变。代码原生环境模块不消耗 ImageGen 额度；不是从整图抠出的装饰。路径与草地边缘接口尚待修，不在测试场强行使用。

## 使用数据

[asset-manifest.json](../../../../web/prototypes/farm-modular-asset-gate-20260928/asset-manifest.json)记录原始尺寸、alpha 显示框、真实透明像素比例和来源。所有 SVG 使用自身 viewBox；PNG 使用 alpha 大于 16 的包围框作为显示窗口，四周原始透明留白仍保留在文件中。

实例坐标存于原型 `board.js`，以屏幕脚点 `(x,y)` 和显示宽度定位；高度按原比例推导，`z-index` 使用脚点 y。接地影独立于图像，统一向右下偏少量。树可多次引用同一文件；不通过水平翻转改变光向。

Test A：`characterImage(0,0)`、`characterImage(0,3)`、`characterImage(0,4)`。Test B：前两者。角色使用项目现有 sprite 定义，未改角色图像或身份。样例 HUD 数字不接玩家存档。
