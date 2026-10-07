# 鸡宝厨房

离线经营与收集游戏：调理一批24枚蛋，收取鸡宝／鸭宝，经营农场、发现配方，培养手艺并派队寻访。原生 JavaScript＋Canvas／HTML，Android 以 WebView 离线运行，无第三方前端依赖。

## 开始使用

需要 Node.js，在项目根目录运行 `npm start`，打开[正常游戏](http://127.0.0.1:4173/)。浏览器进度保存在当前浏览器；评审入口使用隔离演示进度。

- [全界面试玩](http://127.0.0.1:4173/web/ui-review.html)、[单屏检查](http://127.0.0.1:4173/review)、[四阶段厨房](http://127.0.0.1:4173/web/stage-review.html)。
- 手机安装、备份和通知：[工程与维护](docs/engineering.md)。
- 继续设计或开发：[文档索引](docs/README.md) → [当前计划](docs/plan.md)，再读对应权威文档。代码结构见 [web 模块地图](web/MODULES.md)，脚本见 [tools 索引](tools/README.md)。

## 当前版本

当前正式签名安装包为 **1.5.11 / code 27**，241 品种，存档 schema 6，兼容旧 v1–v5；保留原193品种、手艺、旧采购与三条旧路线。本版修复旧 WebView 下缩放后触摸坐标偏移导致的鸡宝点击、滑动收取和厨房按钮失效，并保留上一版的离线贴图补齐。发布检查新增旧、新两种内核行为的触屏对照，见 [换机安装审查](docs/regression-and-ui-audit.md)。[下载安装包](artifacts/releases/chick-kitchen-1.5.11.apk)，版本标识见 [release.json](android/release.json)。

[1.5.0 候选包](artifacts/round2-implementation/chick-kitchen-1.5.0-candidate.apk)在2026-09-21追加手艺图标后已覆盖安装到荣耀 Magic8，核对存档并查看封面、厨房、手艺页；**未作为正式版发布**。这是一条历史安装记录，不代表当前设备连接或全量真机验收。当前验证边界与后续工作只在[当前计划](docs/plan.md)维护。

## 开发与来源

私有版本库：[HeHeYeast/chichen](https://github.com/HeHeYeast/chichen)，主分支为 `main`。源码、素材、测试和开发文档随提交保存；签名密钥、真实设备存档、APK、工具缓存和本地备份不入库。日常提交与恢复边界见[工程与维护](docs/engineering.md#版本管理)。

早期 pygame 原型已移入 [`prototype-python/`](prototype-python/README.md)，原版安装包中运行时不用的解包残留移入 [`original-apk/`](original-apk/README.md)；运行时仍直接读取根目录的 `assets/` 与 `res/`。`reference/` 是美术参考图。当前Web／Android版本从 `npm start` 和上述构建命令进入。

`npm test` 检查规则；`npm run test:ui` 检查界面；`npm run verify:release` 运行发布门禁。构建前备份、签名与安全更新见[工程与维护](docs/engineering.md)。测试结果以本次运行输出为准。

原作资源、原始数据及反编译依据保留，新增内容与离线改编分别记录；不修改原角色身份。[素材来源](docs/game-asset-inventory.md)与[授权准备材料](docs/permission-request/README.md)可追溯，现有材料不表示已经取得公开分发授权。iOS 目前只有[个人移植方案](docs/ios-port-plan.md)，尚未实现。
