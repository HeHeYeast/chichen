# 微信小游戏隔离兼容性原型

只验证现有规则和存档接口能否脱离浏览器 UI 执行；不替代正式厨房、农场、图鉴，也不自动启动微信移植。当前实际证据是 Node VM 中的 wx 测试替身以及浏览器 Canvas，**不是微信开发者工具或微信真机通过**。

## 运行和产物

项目根目录执行：

```powershell
npm --prefix cloud ci --ignore-scripts
node experiments/wechat/build.mjs
node experiments/wechat/verify.mjs
node experiments/wechat/verify.mjs --render
```

最后一个命令另需本机 Edge 和 GSD 附带 playwright-core，路径与浏览器 QA 脚本相同。`dist/` 是可重建、已忽略的独立输出，不加入现有 Web／Android 构建入口。

证据写入 `artifacts/wechat-probe/build.json`、`runtime.json` 和 `canvas-browser.png`。构建报告里的 `browserCoupling` 是词法扫描候选，可能包含业务数据中的 window 等单词，不能直接当 DOM 依赖结论；实际无 DOM 运行才是本轮核心证据。

原型调用正式 `startBatch/updateBatch/collect`，经过正式 `execute` 和 `createSaveStore`。初始 CP 600，开 24 枚蛋，测试按钮推进到可收取时点，收取后 CP 624，再次收取不重复奖励；重启隔离运行实例读取同一测试存储。时间推进只存在于原型按钮，不加入正式游戏。

存储键 `jibao-wechat-compatibility-probe-v1`，与玩家真实主档隔离。损坏档展示错误并停止写入；JSON clone 适配只适用于当前可序列化状态，不能当作支持任意对象的 structuredClone polyfill。

## 真实微信验证：待执行

1. 用户安装官方微信开发者工具，提供可用小游戏 AppID（公开 ID，不是 AppSecret）。
2. 在 `project.config.json` 填入本人 AppID，导入本目录；输出根是 `dist/`。如工具版本对小游戏根字段有不同要求，以导入界面明确指向 `dist`，记录实际配置，不修改正式工程入口。
3. 编译检查 `wx.createCanvas`、触摸、存储、生命周期；真实基础库版本、工具版本和错误日志写入实验记录。
4. 在有体验权限的 Android／iOS 手机上测试开锅→收取→退出→重进→读回，并核对 CP 与存档摘要。不要上传真实玩家存档作实验。
5. 通过后再加一页真实图鉴长列表、字体、图片、音效及 HTTPS 请求验证。记录包体、内存、首屏耗时、低端机帧率及断网表现。

不申请正式审核，不开支付／广告，不把调试关闭域名校验当作正式网络配置。当前主包／分包准确限制仍需官方后台／工具核实。

路线比较、发布资格和时间估算见 [实施报告](../../docs/platform/cloud-wechat-implementation.md)。推荐先薄平台适配＋Canvas 增量迁移；当前 Web UI 继续维护，共享业务与内容模块保持单一来源。
