# 鸡宝厨房 · Kitchen Lv.1–Lv.4 Runtime Recreation

四级已接入正式 Runtime：各级参考分别拆分为静态组件，24 枚蛋、文字、状态、计时及交互继续由原玩法控制。旧存档蛋坐标保留，显示和点击共用新的等级映射。

本轮未编辑 Farm 源码；没有重新设计 Lv.4，也没有将整张 mockup 作为背景。

## 交付入口

- [四级对照与全部状态图库](../../artifacts/kitchen-golden-runtime/index.html)
- [Golden Reference Manifest](GOLDEN-REFERENCE-MANIFEST.md)
- [Asset Manifest](ASSET-MANIFEST.md) · [34 件资产的来源、坐标和哈希](../../web/art/golden-kitchen/manifest.json)
- [Egg Layout Notes](EGG-LAYOUT-NOTES.md)
- [Functional QA](FUNCTIONAL-QA.md) · [浏览器原始记录](../../artifacts/kitchen-golden-runtime/qa-report.json)
- [POLISH TODO](POLISH-TODO.md)

| Level | Golden Reference 390×844 | Runtime 390×844 | 同尺寸对照 |
| --- | --- | --- | --- |
| Lv.1 | [参考](../../artifacts/kitchen-golden-runtime/lv1-reference.png) | [运行截图](../../artifacts/kitchen-golden-runtime/lv1-runtime.png) | [Reference / Runtime](../../artifacts/kitchen-golden-runtime/lv1-comparison.png) |
| Lv.2 | [参考](../../artifacts/kitchen-golden-runtime/lv2-reference.png) | [运行截图](../../artifacts/kitchen-golden-runtime/lv2-runtime.png) | [Reference / Runtime](../../artifacts/kitchen-golden-runtime/lv2-comparison.png) |
| Lv.3 | [参考](../../artifacts/kitchen-golden-runtime/lv3-reference.png) | [运行截图](../../artifacts/kitchen-golden-runtime/lv3-runtime.png) | [Reference / Runtime](../../artifacts/kitchen-golden-runtime/lv3-comparison.png) |
| Lv.4 | [参考](../../artifacts/kitchen-golden-runtime/lv4-reference.png) | [运行截图](../../artifacts/kitchen-golden-runtime/lv4-runtime.png) | [Reference / Runtime](../../artifacts/kitchen-golden-runtime/lv4-comparison.png) |

[四级空锅](../../artifacts/kitchen-golden-runtime/empty-state-board.png) · [Lv.1 窄屏](../../artifacts/kitchen-golden-runtime/lv1-320x844.png) · [Lv.4 窄屏](../../artifacts/kitchen-golden-runtime/lv4-320x844.png)

## 验收结论

功能回归 **38 组通过**，自动测试 **601/601 通过**；离线资源打包成功。四级真实升级、96 次实际收取、开锅、调味、计时、打扫、分页、入口、导航及存档回归均有证据。

主场景与容器身份保留，四级都有独立蛋位。仍存在空内衬补片、蛋描边与高光、部分 UI 字体 / 图标差异，已单列 `POLISH TODO`。本轮停止，不自行继续下一轮大规模美术修改。

两个有意保留的玩法语义：Lv.1 调味槽显示真实 **1/1**（参考的 1/3 不符合现有规则）；“清洁度”显示 **100 − 原引擎脏污百分比**，清洁费用和计时规则不变。
