# Kitchen Golden Reference — 2026-09-30

视觉真值仅为用户本轮指定的 `reference/kitchen` 四图。未采用旧 Concept、Background A–D、K1/K3、灰盒或 Remaster。

| Level | 最终参考路径 | 空间 / 容器身份 | 必须保留的差异 |
| --- | --- | --- | --- |
| Lv.1 | reference/kitchen/e3f95393-29a3-4149-a3b7-35048cbea3cc.png | 茅草屋、绳绑粗木、草窝 | 狭窄手作草窝，窗口、提灯、草帽、玉米；不得精装 |
| Lv.2 | reference/kitchen/adeda638-1b40-4555-8cc4-9129c9eeff3d.png | 木板房、架高木制编织孵化位 | 规则木框、木桶；不得加入高级厨房家具 |
| Lv.3 | reference/kitchen/73ca53da-de65-4525-8d76-5a24a7d41076.png | 抹灰 / 瓷砖、砖座搪瓷盆、草屑内衬 | 白盆深色边、侧把手、低砖座 |
| Lv.4 | reference/kitchen/2649036c-79ba-4d26-b459-826f46a39386.png | 精装温馨厨房、绿金孵化座、格纹软垫 | 鸡宝灯、皇冠、软包前沿和厨师摆件；保留认可方向 |

## 拆层合同

- 静态：墙面、梁柱、装饰、空容器、内衬、容器前沿、无字牌匾与木纹。
- 动态：24 个独立蛋 / 伙伴、当前锅具和调味、状态、真实倒计时、清洁度、CP、等级、解锁、下锅调味、厨具分页、当前页和入口响应。
- 共用 UI：同一 Canvas 组件与同源 DOM 点击坐标；沿用正式商店 / 仓库 / 手艺 / 帮助 / 设置 / 导航处理程序。
- 参考输出：逐张按全幅归一化为 390×844，不裁掉 UI。Lv.1 源图比例与另三级不同，采用独立 X/Y 归一化；这会保留原稿所有布局而产生原图到目标比例的拉伸，Runtime 使用相同规则。
- 不把完整 mockup 放进 Runtime。只发行经过拆分、去除动态信息的组件；参考全图只放验收目录。

## 既有成功流程核查

- 图鉴：`tools/slice-golden-assets.py` 切分纸张、未知槽、印章、标签；`web/golden-collection-ui.js` 负责真实角色及状态。
- 生意：`tools/slice-business-r2-assets.py` / `tools/slice-business-family.py` 记录坐标与来源，容器前后层、角色、计数分别表达。
- 寻访：`tools/segment-journey-precision.py` 提取已确认图形并记录视觉边界；`tools/qa-journey-precision.mjs` 使用同 viewport、同状态截图。地图压缩经验要求本次各级独立测量，不能以统一旧容器比例代替。
- 本轮继承上述来源清单、独立层、实机浏览器截图、同尺寸并排对照方式。

## 数据兼容

保持引擎、存档版本和存档中的旧蛋位不变。显示蛋位与收取命中位置通过独立的等级模板映射；不把美术坐标写回玩家存档。
