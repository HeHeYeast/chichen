# 鸡宝厨房

离线经营与收集游戏：调理一批 24 枚蛋，收取鸡宝／鸭宝，经营农场、发现配方、培养手艺并派队寻访。原生 JavaScript + Canvas / HTML，Android 使用离线 WebView，无第三方前端依赖。

## 运行

安装 Node.js 24，在项目根目录运行：

```sh
npm start
```

打开 <http://127.0.0.1:4173/>。进度保存在当前浏览器；[评审入口](http://127.0.0.1:4173/review)使用隔离演示进度。基础 Web 版不需要安装 npm 依赖。

## 当前基线

Android 发行配置为 **1.5.20 / code 36**，241 种伙伴、83 种材料，存档 schema 6，兼容旧档。寻访、线索册、下一锅和生意已串联。版本以 [发行配置](android/release.json)为准，当前待办及验证边界只维护在[当前计划](docs/plan.md)。APK 和签名保留在本机，仓库不提供安装包下载。

Web 账号与手动云备份处于本机开发阶段，云地址默认为空，未部署线上服务。运行方法见[云服务说明](cloud/README.md)；[微信兼容原型](experiments/wechat/README.md)尚未通过微信开发者工具和真机验证。

## 项目目录

| 目录 | 用途 |
|---|---|
| `web/` | Web 游戏源码、正式美术与字体；[模块地图](web/MODULES.md) |
| `android/` | Android 包装、签名构建与原生测试 |
| `cloud/` | 本机账号／云备份 API 与关闭状态的 Worker 模板 |
| `assets/`、`res/` | 游戏直接读取的原作素材与数据，保持固定路径 |
| `tests/`、`tools/` | 自动测试、生成器与构建脚本；[工具索引](tools/README.md) |
| `docs/` | 核心规则、分类设计与历史档案；[文档导航](docs/README.md) |
| `experiments/` | 独立平台实验，不接入正式构建 |
| `artifacts/` | 构建输入、验证产物与本地归档；[保留规则](artifacts/README.md) |
| `prototype-python/`、`original-apk/`、`reference/` | 早期原型、原版来源与美术参考 |

## 开发与验证

```sh
npm test
npm run test:updates
node tools/build-runtime-content.mjs --check
```

浏览器检查使用 `npm run test:ui`；Android 构建前运行 `npm run verify:release`。这两项需要本机浏览器／Android 工具链，准备方法见[工程与维护](docs/engineering.md)。历史报告中的测试结果仅代表当时版本。

版本库：[HeHeYeast/chichen](https://github.com/HeHeYeast/chichen)，主分支 `main`，保持私有。源码、正式素材、测试、有效文档和必要生成输入入库；密钥、真实存档、数据库、APK、缓存与本地归档不入库。原作资源与改编的来源见[素材清单](docs/game-asset-inventory.md)及[授权准备](docs/permission-request/README.md)，现有材料不表示已取得公开分发授权。
