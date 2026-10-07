# Kitchen Asset Manifest

机器可读清单：`web/art/golden-kitchen/manifest.json`。每个提取件包含来源、390×844 坐标、方法、实际尺寸及 SHA-256。可通过 `python tools/extract-kitchen-golden.py` 再现。

| 类型 | 内容 | 来源 / 接入 |
| --- | --- | --- |
| 复用 | 导航、商店、仓库、手艺、帮助图标；已孵化角色；高级厨具、真实调味料；游戏字体 | 现有 `ui-icons.js`、`catalog.js`、正式 PNG / atlas、`web/fonts`，不替换其他页面 |
| 提取 | 各级 header、status-surround、wall、support、front、counter | 用户四张最终参考，共 24 件；HUD 的 CP、等级、标题及状态已清除，由 Runtime 重写 |
| 提取 | preparation-frame、plaque、timber、egg、clock、lemon、tool-0～3 | Lv.3 参考中相同的共用组件，共 10 件；面板清空，鸡蛋与图标做透明轮廓切分 |
| 新增 | 空孵化位被遮住的中央内衬 | 只使用各自参考中外露的草、编织物或格纹布料补片；Lv.1 / Lv.3 羽化纹理拼接，Lv.2 / Lv.4 连续布纹拼接。隐藏部分无原始像素，属于修复，不宣称精确提取 |
| 新增 | Canvas 共用 UI、四级独立蛋位和命中映射、厨房限定 CSS | `web/kitchen-golden.js`、`web/kitchen-golden.css` |
| 动态 | 24 枚独立蛋 / 伙伴、剩余数量、状态、倒计时、闹钟、清洁度、CP、等级 | 原引擎数据，逐帧绘制；输入沿用正式控制器 |
| 动态 | 本锅锅具 / 调味，下锅调味槽，厨具费用 / 时长 / 等级 / 锁定 / 页码 | `batch`、`selected`、`toolLevels` 与 `cookInfo`，未烘焙参考数值 |
| 动态 | 鸡鸭切换、收成分配、商店 / 仓库 / 手艺 / 帮助 / 设置 / 升级 / 导航 | 原处理程序保留；升级缩略图也使用新的空场景组件 |

共有 **34 个静态组件**。游戏不请求原参考、390×844 对照页或整页 mockup。`android/package-runtime.mjs` 读取组件清单，以便离线包包含动态拼接路径中的全部图片。

图像编辑服务曾尝试局部修复 Lv.4 空内衬，但连接失败，没有获得或使用生成图。最终使用的所有像素来自项目现有资产或用户确认稿；无新增生成背景。

未改 Farm 文件、玩法引擎、存档版本及迁移规则。
