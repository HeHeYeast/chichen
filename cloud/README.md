# 鸡宝厨房账号与手动云备份服务

状态：开发／本机联调通过，未部署到 Cloudflare。产品方向与验证证据见 [实施报告](../docs/platform/cloud-wechat-implementation.md)。接口合同见 [openapi.json](openapi.json)。

## 本机运行

需要 Node 24（本轮使用 24.14.0）；SQLite 来自 Node 内置模块，当前 Node 会提示实验功能。游戏与 API 分别运行在两个终端：

```powershell
node server.mjs 4173
```

```powershell
node cloud/dev-server.mjs
```

打开 `http://127.0.0.1:4173/?cloud=local`，开始游戏 → 设置 → 账号与云备份。默认仅允许测试用户名 `alice`、`bob` 注册，密码由测试者自行设置，不使用其他网站真实密码。原游客档保留，注册会复制其完整进度；云备份需要手动点击。

SQLite 位于 `cloud/.local/development.sqlite`，该目录已忽略，不提交。测试不同设备可用两个独立浏览器 profile／隐私窗口，第二设备登录后选择查看云端并恢复。普通页面没有 `?cloud=local` 时，空 `CLOUD_ENDPOINT` 会显示未配置。

开发服务只监听 loopback，不能直接当公网服务。可设置 `CHICK_API_PORT`、`CHICK_WEB_ORIGIN`、`CHICK_TEST_USERS`、`CHICK_DB_FILE` 改本机测试端口／允许来源／用户名／隔离数据库；不要把包含真实用户数据的数据库交给测试脚本。

## 复核

```powershell
node --test --test-isolation=none tests/cloud-*.test.mjs
node --test --test-isolation=none tests/*.test.mjs
npm --prefix cloud ci --ignore-scripts
node cloud/verify-worker.mjs
node tools/qa-cloud-save.mjs
```

后两个脚本分别运行真实本机 workerd/D1 与隔离 Edge HTTP 联调，不需要 Cloudflare 登录，不创建云资源。Windows 沙箱可能阻止 workerd 路径解析或浏览器进程，需要批准仅本地测试的执行权限。浏览器脚本当前使用本机 GSD 附带的 `playwright-core` 和 Edge 固定安装路径；其他机器应配置相应工具路径再运行，不能把找不到测试工具当产品失败。

`verify-worker.mjs` 使用 lockfile 中 Wrangler 的 Miniflare 依赖；当前锁定版本包含 Miniflare 5 预发布构建，需要在更新工具依赖时复核 `convertV4MiniflareOptions` 接口。这只影响本机开发工具；正式业务不依赖 Miniflare。

## Cloudflare 测试环境准备（尚未执行）

用户需要自行完成免费账号注册和官方登录授权。不要把 Cloudflare 密码、API token、数据库导出、微信 AppSecret 发到聊天或写进客户端。`wrangler.jsonc` 是关闭状态模板：假 D1 ID、空注册名单、`workers_dev:false`、`ENABLE_TEST_API:false`。

获得测试部署授权后才执行：确认账号仍是 Free → 创建独立测试 D1 → 只在新空测试库执行 `schema.sql` → 将返回的公开 D1 ID 写入配置 → 设置唯一 `CLOUD_EPOCH`、实际 `WEB_ORIGIN`、最多十个允许的用户名 → 启用测试 API／测试路由 → 部署 → 将公开 HTTPS endpoint 写入 `web/cloud-config.js`。不同环境必须使用不同数据库和 epoch，不共用真实玩家库。模板的每日 Cron 清理应一并验证。

不要通过修改已有生产数据库来“试一下”。有旧测试库时先备份并核对表结构；`CREATE TABLE IF NOT EXISTS` 不负责未来表结构迁移。每次改库使用单独编号迁移，游戏 schema 的升级另算。

注册／登录最先测：安全 scrypt 参数不降级，记录线上 CPU 指标与超限错误，不能用本机墙钟代替。如果免费 CPU 不够，停止测试开放、保留本地功能，重新讨论服务或预算；不要自动升级每月 5 美元套餐。

再测：手机三个运营商访问、同账号跨设备、旧存档注册、两端冲突、断网与 ACK 丢失、历史恢复、恶意请求、Cron、账号删除与灾备。测试成功不等于获得正式部署授权。本轮未开启 R2、Paid、自动扣费或域名购买。

## 运维边界

- 当前仅一个精确 Web origin；无 Origin 请求供未来原生适配，但仍依赖 token 认证。微信 HTTPS 合法域名与平台资质需单独确认。
- 服务只支持手动快照，不合并货币／材料／奖励。数据库保留当前 + 最近 20 份；重复上传同 UUID／原文返回同回执。
- 会话 24 小时，到期重新登录，关闭标签页一般失去该 sessionStorage 会话；本机账号进度不因此删除。
- 反馈管理员通过受保护数据库控制台查看 `feedback`；不要将查询结果截图公开。当前没有消息通知或工单后台。
- 已实现账号删除 API，但删除 UI、密码找回／重置、独立加密备份及恢复演练尚未完成。打开真实玩家注册前处理这些缺口。
- Worker 每日维护会删除到期会话、过期限速记录、90 天以前反馈。`dev-server` 没有后台调度；本机留存是测试数据，不宣称已执行线上清理。
- 灾备恢复后必须换 epoch、撤销会话、处理删除清单，避免把旧云库当新基线。详细恢复流程在实施报告第 4 节。

本地凭据和数据库位于忽略目录；源码只有公开配置。用户导出的个人存档、数据库快照、生产日志必须另行受限保存，不加入 Git。
