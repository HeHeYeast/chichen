# 原生存档恢复检查

运行 `android/tests/run-save-tests.ps1`，可用 `-JavaHome` 指定 JDK 17。脚本编译实际的生产 `SaveRepository.java` 和 `SaveBridge.java`，使用 Android 官方 AOSP JSON 解析/序列化代码，并在 `android/build/save-test-data` 中读写真实文件。

脚本先运行 `android/tests/build-save-fixtures.mjs`，直接调用 Web 的 `freshState`、`normalizeSave`、`execute` 和购买命令，生成四条跨端轨迹：空安装首次命令，以及 v3/v4/v5 升级首次命令。Java 逐项读取真实候选、执行 `expectedRevision` 提交、验证 CP/库存原文回读和重试，再执行第二条普通命令。首次原子命令允许从内存 revision 0 直接持久化 revision 1；不是先另写一个空 revision 0 档。

`Context` 与 `AtomicFile` 是 JVM 适配器。测试证明了存档校验、错误处理和恢复顺序，不等同于 Android 文件系统断电测试或手机覆盖安装验收。

当前测试覆盖 schema 1–6、事务和跨端行为，断言总数以脚本输出为准：

- 空存档、首次保存、后续保存、相同内容不挤掉上一份备份。
- 主存档截断或 checksum 不匹配时回退到已验证的上一份备份；恢复后的自动保存继续保留正确备份。
- 较新游戏版本或较新原生文件格式禁止自动回退和自动覆盖；明确导入前保存旧原文件。
- 无可用备份的坏档禁止自动保存；明确导入前按字节保留坏 UTF-8 文件。
- 原始 JSON 小于 2MB，但字符串转义后原生 envelope 超过 2MB，仍可正确保存与读回。
- 外部文件仍严格限 2MB UTF-8 字节，不把中文字数误当字节数。
- 错误版本类型、非整数版本、无效 CP、负 CP 和未来版本输入拒绝保存。
- 旧版 8 件厨具存档可交给网页逻辑迁移；新版为 9 件厨具。
- 注入原子写入失败时保留原存档；导入前另存正常存档。
- 241 品种、83 材料及旧 171/193/75 身份边界；schema 4 随机根、地区、发现、方法、保护与时钟字段。
- revision 冲突、相同命令不同候选拒绝、ACK 丢失后幂等重试；通知失败仍返回持久化成功回执。
- 升级前字节级封套副本不随滚动保存覆盖；副本写失败和 current 写失败均保留原资产。
- 非零 revision 备份可导入空安装，首次命令不得跳过修订号。
- `BusinessSaveValidator.java` 镜像 Web 营业窗口、菜单快照、采购实例与事实校验。`RuntimeContent.java` 从正式注册表生成 allowed、冻结语义与基础价；运行 `node android/tests/build-native-content.mjs --check` 检查无漂移。
- `save-negative-cases.mjs` 的 107 个独立反例先由 Web 拒绝，再验证 Native 拒绝且不改变主档；覆盖缺失/额外字段、字符串与小数类型、营业逐窗收支/角色轮转、采购变体/交付/预留、事实ID与见证、项目付款和锁定品种。
- `save-contract-matrix.mjs` 使用明确标记的合成成熟前置，通过正式领域生产器冻结 8 菜单（开张/2h/提前收摊/24h）、所有采购变体及分批交付；另验证超过 JVM 32 位整数范围的合法常客基线。
- 六黄金原档与 Web 迁移到 schema 6 的候选均无损原文往返，升级前封套按字节保留；营业/地区寻访/批次进行中的 schema 5→6 在真实 revision 提交和 ACK 重放后保持票据。

完整机型验收还需：第一次安装、覆盖更新、导出/导入文件选择器、关闭进程后重开、低存储空间、设备重启。卸载应用会清除其私有数据；外部导出的备份必须由用户保留，后续版本使用固定包名和同一签名覆盖安装。
