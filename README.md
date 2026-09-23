# 鸡宝厨房

离线经营与收集游戏：调理一批24枚蛋，收取鸡宝／鸭宝，经营农场、发现配方，培养手艺并派队寻访。原生 JavaScript＋Canvas／HTML，Android 以 WebView 离线运行，无第三方前端依赖。

## 开始使用

需要 Node.js，在项目根目录运行 `npm start`，打开[正常游戏](http://127.0.0.1:4173/)。浏览器进度保存在当前浏览器；评审入口使用隔离演示进度。

- [全界面试玩](http://127.0.0.1:4173/web/ui-review.html)、[单屏检查](http://127.0.0.1:4173/review)、[四阶段厨房](http://127.0.0.1:4173/web/stage-review.html)。
- 手机安装、备份和通知：[工程与维护](docs/engineering.md)。
- 继续设计或开发：[文档索引](docs/README.md) → [当前计划](docs/plan.md)，再读对应权威文档。

## 当前版本

源码为 Android **1.5.0 / code 16 候选版**，存档 schema 3，兼容旧 v1/v2。包含193个品种、30项单级手艺、三章采购、候选观察和一队三路线寻访。版本标识见 [release.json](android/release.json)；功能规则见[游戏设计](docs/game-design.md)。

[1.5.0 候选包](artifacts/round2-implementation/chick-kitchen-1.5.0-candidate.apk)在2026-09-21追加手艺图标后已覆盖安装到荣耀 Magic8，核对存档并查看封面、厨房、手艺页；**未作为正式版发布**。这是一条历史安装记录，不代表当前设备连接或全量真机验收。当前验证边界与后续工作只在[当前计划](docs/plan.md)维护。

## 开发与来源

私有版本库：[HeHeYeast/chichen](https://github.com/HeHeYeast/chichen)，主分支为 `main`。源码、素材、测试和开发文档随提交保存；签名密钥、真实设备存档、APK、工具缓存和本地备份不入库。日常提交与恢复边界见[工程与维护](docs/engineering.md#版本管理)。

仓库原有的 `main.py`、`core/`、`entities/`、`scenes/`、`data/`、`settings.py` 和 `reference/` 保留为早期Python原型；当前Web／Android版本仍从 `npm start` 和上述构建命令进入。

`npm test` 检查规则；`npm run test:ui` 检查界面；`npm run verify:release` 运行发布门禁。构建前备份、签名与安全更新见[工程与维护](docs/engineering.md)。测试结果以本次运行输出为准。

原作资源、原始数据及反编译依据保留，新增内容与离线改编分别记录；不修改原角色身份。[素材来源](docs/game-asset-inventory.md)与[授权准备材料](docs/permission-request/README.md)可追溯，现有材料不表示已经取得公开分发授权。iOS 目前只有[个人移植方案](docs/ios-port-plan.md)，尚未实现。
