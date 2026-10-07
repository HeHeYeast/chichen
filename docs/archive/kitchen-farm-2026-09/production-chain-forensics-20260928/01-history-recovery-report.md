# History Recovery Report

取证日期：2026-09-28，Asia/Shanghai。范围是本机可读的《鸡宝厨房》生产历史。本报告先恢复记录，再作对照；不制作新图、不设计新版、不提出最终 Production Pipeline。

## 恢复结果

|对象|可恢复程度|证据与限制|
|---|---|---|
|Work 用户 Prompt|高|11 个相关历史会话的用户消息、后续纠正与阶段授权已导出；Work 2.5 的原始粘贴附件仍在。用户粘贴 Chat 建议时，标为“用户传入的 Chat 文本”，不冒充用户独立表述。|
|Work 中间回复、公开计划|高|agentMessage 中的 commentary/final 可逐条恢复；使用公开回复解释 Work 的理解，不把未公开推理当作证据交付。|
|实际工具调用|高|JSONL 保留 exec 原文、工具名称、调用顺序、reference 数组和 load/store 变量定义；SQLite 保留命令、文件变更、图片查看等事件。|
|ImageGen Prompt|高|恢复 80 个已完成图像事件，均有真实 revisedPrompt 字段及相邻原始生成调用。两种字段分别归档，不能把工具记录的 revisedPrompt 偷换成逐字提交参数。62 个生成编排调用可包含并发、失败或重试，不能视为 62 张图或计费次数。|
|ImageGen 输入图与输出|高但非绝对完整|80 个生成源 PNG 本机均存在；59 个事件找到项目内 SHA256 完全相同的 PNG 副本。其余有裁切/缩放/分割的可能，需按命令、文档确认，不能用相似外观伪造哈希对应。reference 原始路径/角色保留在调用和变量定义。|
|Chat 总指挥的原始讨论|局部|历史 Work 会话 01a0e386 的 read_thread 工具结果缓存了 5 轮 Chat 内容，包含 Lv.2、Background A–D 的真实不满。它不是完整 Chat 导出；本轮没有联网读取或打开旧 GUI。|
|用户 Gate|可恢复关键节点|图鉴、营业主屏、生意系统的明确通过；寻访“先通过、后撤回 Runtime 通过”；厨房多次停止与 Lv.2 明确不通过；Visual Forensics 明确通过。没有找到的单候选通过不补写。|
|K1/K3、F1/F3|生产过程可恢复|用户指令、制作命令、源码、27 张候选截图与旧验证记录均在。后续历史会话内未找到逐候选验收原话；本轮用户陈述提供“移动按钮/蛋排列反向”的反馈，图片与源码可独立核证其中蛋位问题。|

## 本机检查路径与方法

1. 当前项目的 docs、artifacts、web/art、web/prototypes、生成来源、manifest、截图、代码、Git 工作区状态。
2. `%USERPROFILE%\.codex`：`state_5.sqlite`、`thread_history_1.sqlite`、`logs_2.sqlite` 的表结构；按项目 cwd/相关标题筛选，再读 11 个相关会话。sessions 有 230 个文件，不只依赖状态库中的最新 rollout 路径猜测历史。
3. `%APPDATA%\Codex`：目录及文件类型盘点。所见主要是浏览器状态、缓存与浏览器数据库；没有把浏览器 History/登录数据当 Work 消息库，也没有读取凭据。
4. `%LOCALAPPDATA%\Codex\Logs`：82 个日志文件；按相关会话 ID 找到桌面日志，作为定位辅助，正文恢复以 rollout 和历史库为主。
5. `%LOCALAPPDATA%\Codex-Lumon`：发现 6 个 SQLite 文件；尝试只读打开其历史库返回 `unable to open database file`，记录为未读缺口，不申请升级权限、不复制数据库规避。
6. `%LOCALAPPDATA%\OpenAI\Codex`：主要为工具运行时。未发现比主历史库更直接的项目正文来源。

这是有范围的检查，不声称穷举所有 AppData 软件目录、浏览器缓存、其他账户或云端。目录盘点排除了插件依赖、node_modules、凭据目录及通用缓存。精确范围与文件数见 [discovery-inventory.json](evidence/discovery-inventory.json)。

SQLite 连接使用 `file:...?...mode=ro`；只执行 `SELECT`，表和 schema 从 `sqlite_master` 查询，相当于只读 `.tables` / `.schema`。未执行 UPDATE/INSERT/DELETE、迁移、checkpoint、VACUUM 或 journal 修改。输出报告写入项目新目录，不写回历史库。数据库是活跃文件，不能以本轮期间文件时间变化证明本轮修改了数据库。

## 已保存的证据

|文件|内容|
|---|---|
|[threads.json](evidence/threads.json)|11 个会话的完整 ID、原始标题、rollout 路径、时间与来源|
|[messages.jsonl](evidence/messages.jsonl)|302 条用户/公开助手消息；有 thread、turn、item、ordinal、时间|
|[各会话可读文本](evidence/01a0d334-messages.md)|文件名使用会话前 8 位；原文按 ordinal 排列，便于引用定位|
|[tool-calls.jsonl](evidence/tool-calls.jsonl)|1,095 条原始调用输入；取证数据，不是待执行脚本|
|[execution-events.jsonl](evidence/execution-events.jsonl)|1,787 条命令/文件变更/工具/图片查看事件索引；不等于成功操作次数|
|[可读执行顺序示例](evidence/01a0e199-execution.md)|每个会话另有 `会话前缀-execution.md`，按ordinal列已记录命令、工具、状态；与消息文件一起对照计划/实际|
|[image-events.jsonl](evidence/image-events.jsonl)|80 个真实 imageGeneration 事件，移除庞大 base64，保留文字与路径|
|[image-archive-index.json](evidence/image-archive-index.json)|生成结果、提示词来源、原图哈希、项目内完全一致副本、调用定位|
|[cached-chat-messages.md](evidence/cached-chat-messages.md)|从过去工具返回值提取的 5 轮 Chat 缓存；明确区分 Chat user / assistant|
|[work-2.5-user-original.txt](evidence/work-2.5-user-original.txt)|原始粘贴附件，补齐消息里只有附件路径的上层指令|
|[sqlite-schemas.json](evidence/sqlite-schemas.json)|三个主库的表结构原文|
|[生成前置失败](evidence/generation-preflight-failures.json)|Concept和Background最初编排因超过5张reference被拒；后收敛参考数再生成。这些不是已完成图片，也没有成本结论|
|[本轮用户要求](evidence/current-user-request.md)|单独保存本轮输入，避免把当前回顾伪造成历史消息|

## 证据分层

- **U**：真实用户消息。当前任务的用户回顾标 U-now；历史用户传入 Chat 文本标 U-Chat，不混淆作者。
- **W**：Work 当时的公开说明、计划、自评。W 的 PASS 只表示内部判定。
- **T**：原始工具调用/结果、代码与可定位执行记录。计划与实际冲突时优先 T。
- **D**：项目文档和 manifest。文档摘要可作旁证，但不能覆盖原始消息。
- **V**：本轮直接查看已有 PNG 得到的可见现象。
- **I**：基于以上证据的分析推断。尤其“reference 导致趋同”的因果强弱必须限定，不能假定看到了模型内部。

## 已确认的关键恢复结论

**可以继续本轮完整取证，不需要因“历史完全不可恢复”停止。** 但不能恢复完整 Chat 决策讨论、所有附件图片内容、服务端隐藏 Prompt/模型版本/种子，也不能精确核算 ImageGen 成本。项目的 `artifacts/imagegen/provenance.jsonl` 主要记录另一路 CLI/API 资产测试，不能拿其中模型名和费用字段填给这些内置 ImageGen 页面。

成功页面首先是完整 Mockup 获认可，最终成功则经过拆分资产、真实动态对象组合和视觉回归；Kitchen 的失败不能用“成功整图直出、失败也整图直出，所以只差 Prompt 词汇”概括。这一判断已有原始用户指令和实际制作记录支持，详见后续对照。
