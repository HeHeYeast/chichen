# 通用角色美术Pipeline

版本1，2026-09-27。输入Art Brief或content definition，输出Runtime资产、来源/几何元数据、内部审查和QA记录。数量、地区和ID全部来自输入。当前生产批次仅是实例，工具不能写死角色名称、地区数量或批次规模。

## 状态与责任

Concept → Generate → Internal Review → Asset Processing → Runtime QA → FINAL。

生成成功不等于批准；处理成功不等于FINAL。审批绑定源图SHA-256，Runtime结果绑定发布清单哈希。换源图、修改Concept或处理参数后需重新检查相关阶段。原始候选保留，不覆盖源文件。

- Concept：适配content/Art Brief，写入独立生产清单；身份与配方不可变化，构图自由遵循设计规范。
- Generate：导出原生请求；当前代理调用原生ImageGen，不输出预览，再把工具原始文件与提示词、参考、哈希登记。保留每次尝试。
- Internal Review：人工/代理实际看源图、120px、48px、浅深底和同批对照，逐项判断画风、物种、食品、记忆、Idle、可爱边界、重复风险。不能仅凭文件存在批准。
- Asset Processing：连通部件检测、透明清理、裁边、等比Normalization、full/portrait/silhouette等变体、metadata。自动几何失败回到Edit/Regenerate。
- Runtime QA：验证图鉴、孵蛋/厨房、农场、生意、寻访、收藏和常客。无裁切、漂浮、失衡或布局例外；隐藏状态不泄露未知图像。仅用隔离测试存档。
- FINAL：当前源图内部通过、自动QA通过、重复审查处置完毕、实际Runtime验证通过。报告数量，不展示内容。

## 工具与输入

tools/art_pipeline.py 提供prepare、ingest、ingest-log、process、review、review-sheets、similarity、publish、finalize、status；--help列出参数。prepare读取content定义与可选Art Brief/Concept数据，输出包含请求与参考的jobs.json。另可直接提供同schema的jobs.json，不依赖项目当前content编译器。

每个job需id、kind、contentId、region（可空）、identityKey（可空）、prompt、references、concept、variants。允许kind：species/materials/cards/mementos/regulars。新增资产类型通过profile扩展。旧角色重绘填写原identityKey并指定新的生产目录；publish不覆盖旧源PNG。首次环境准备需Python、Pillow、NumPy、SciPy；Runtime构建需要Node，浏览器验收使用现有Playwright。

命令顺序（`$batch`指本批内部工作目录）：

```powershell
python tools/art_pipeline.py --out $batch prepare --content $content --briefs $briefs --concepts $concepts
# 代理读取jobs.json，用原生ImageGen逐项生成，随后登记原始返回文件。
python tools/art_pipeline.py --out $batch ingest --id $assetId --source $nativeOutput
python tools/art_pipeline.py --out $batch process
python tools/art_pipeline.py --out $batch review-sheets --kind species
python tools/art_pipeline.py --out $batch review --id $assetId --record $internalDecision
python tools/art_pipeline.py --out $batch similarity --legacy assets/png/Character
# 全量内部比较后写batch-review.json，含各源图当前sourceHashes与处置记录。
python tools/art_pipeline.py --out $batch publish --destination web/art/production
node tools/build-runtime-content.mjs
node tools/qa-production-art.mjs --out $batch/runtime-qa
python tools/art_pipeline.py --out $batch finalize --destination web/art/production --report $runtimeReport
node tools/build-runtime-content.mjs
node tools/build-runtime-content.mjs --check
```

内部decision必须含sourceHash、outputHash（processed metadata文件SHA-256）、逐项checks、具体notes与证据路径。九个checks为legacyStyle、identity、foodIdentity、memory、idle、friendlyAnatomy、smallSize、bounds、distinctness；非角色物品的物种/Idle检查表示其无误生成人物且静态构图成立，应在notes说明。不能用批量全true脚本代替实际查看。派生资产变化也使outputHash失效。

`qa-production-art.mjs`是当前游戏的验收适配器，读取生产manifest和Runtime content，以隔离的合成存档覆盖现有场景。其它项目或未来新场景应增加对应适配器；不要把适配器的存档结构写进图像处理核心。

ingest登记原生输出文件，复制原始字节，记录source hash。原生后台批次每项返回后立即写独立receipt文件，避免并发覆盖共享结果表；ingest-results选择每项最高原生attempt登记，select可显式回选已有候选。回选并不跳过当前审核哈希验证。process检测alpha连通部件并输出组件bbox/面积；微小孤点清理仅有明确阈值，保留所有可见语义部件。自动处理不推断“帽子是多余的”。多主体误生成必须返工，不能任意选一个组件假装通过。

## 统一几何规则

species full为512方形透明画布，最长边适配宽80%/高80%，脚底锚点位于画布92%高度；不拉伸。portrait为256方形，安全边12%，主体居中完整保留。silhouette复用portrait alpha。其它变体尺寸由profile与job定义：材料128图标及256×192标本，发现480×270，纪念物256及48，常客256。

metadata包含来源哈希、原始尺寸、裁边矩形、连通组件、可见bbox、质心、ground anchor、标准化矩阵、变体尺寸与哈希、输入/处理版本。无法可靠推断双足位置时使用主体底部落点并要求内部确认。所有Runtime使用共同画布/元数据，不添加单角色CSS。

输出中的portrait是全身紧凑肖像，未经明确content配置不自动裁去食物结构；“分层”只报告实际可获得的alpha与组件，不凭空声称可分离遮挡部件。

## 自动QA与重复提示

检查路径与ID安全、重复ID、引用存在、RGBA真透明、空图/不透明背景、源图触边、可见宽高比、留白、安全边、anchor、尺寸、变体哈希、审批哈希时效及清单完整性。连通部件、色彩分布和归一化alpha相似度输出候选近邻；与原始旧PNG也比较。颜色统计只用于分析，绝不改写图像色板。legacy-index可提供既有身份及图集frame用于完整防重复；后续扩展可以参与比对，但不能因此成为原作Style Reference。相似度不是创意评分，最终通过或返工由内部审查记录决定。

有意义的工具测试覆盖：不透明源拒绝、空源拒绝、极端边界拒绝、缩放不裁切、anchor一致、透明负空间保留、不同数量/地区输入、缺失或过期审批拒绝、输出哈希校验与路径越界拒绝。

## 发布与Runtime

publish仅发布有当前内部审批与自动QA通过的资源，输出可重复构建的生产manifest与JavaScript映射。资源放在ID下的内容哈希目录，完整通过后才切换manifest；失败候选不会覆盖已在使用的旧版本。它先标记art-approved/runtime-pending；Runtime验证后才可将当前manifest对应条目标记FINAL。Runtime解析器用生产资源替换placeholder，旧身份/配方/存档不变。构建器纳入生产manifest哈希，避免内容重编译把新美术重置为pending。未生产的非本批资产保持真实pending状态。

发布按稳定ID增量合并，保留其它批次已发布的资源；不允许同一ID悄悄更换content或物种身份。未来旧角色重绘通过同一job schema和原identityKey处理，Runtime适配器的content定义也须包含该身份。finalize要求完整发布清单的当前Runtime QA，不能用单批局部结果冒充全量。

发布与验证分开，不能为了获得100%而把未使用的资源标为已接入。QA截屏与review sheet保存内部目录，交付报告只含分类型数量、占位剩余数、技术问题与测试路径。玩家存档只读，不覆盖真实玩家进度。

## 保密与重试

通用文档和工具帮助不含批次具体设计。请求/Concept/源图/review sheet属于内部内容；工具stdout默认仅汇总数量与错误码。原生输出通过后台调用登记，不向会话转发图像。不得为审核便利把全解锁测试存档交给用户。单只失败自行迭代；仅系统性方向变化才暂停询问。
