# Work 1 美术交付状态

2026-09-27。无角色预览或内容设计剧透。

| 范围 | FINAL |
| --- | ---: |
| 新角色 | 48/48 |
| 地区材料 | 8/8 |
| 发现 | 24/24 |
| 纪念物 | 12/12 |
| 常客肖像 | 4/4 |

谷地、溪岸、茶坡、海湾均完成。96组源资产经过原生ImageGen生成／编辑、内部审查和迭代，输出220个Runtime变体及96份公开几何元数据。具体Concept、原始候选、提示词、参考与审查证据保存在content数据和内部生产目录；本文不列出。

项目级复用规范：[角色设计](character-design-spec.md)、[旧画风与原生生成约束](legacy-character-style-spec.md)、[生产Pipeline](character-art-pipeline.md)。核心工具为`tools/art_pipeline.py`，输入来自Art Brief/content，不固定地区或角色数量。增量发布保留其它批次的既有资源；源图、处理产物和Runtime结果以哈希关联。

Runtime已接入图鉴、厨房／孵蛋、农场、生意、寻访、收藏、常客。浏览器检查220个变体解码、120/48边界与脚底锚点、48角色实际图鉴结果、两种蛋各24个厨房槽位、四地区发现册、纪念物和常客，以及新存档未解锁隐藏规则。实际截图内部复核时发现的备货肖像尺寸问题已通过共用sprite-art槽位修复，并增加边界回归检查。

最终验收：7/7场景通过，592/592 Node测试、9/9 Pipeline测试通过；0页面异常、0失败资源请求；Runtime编译一致性检查通过。离线包包含220个变体和96份metadata，逐项核对哈希，未打包生产Concept、候选图或Review Sheet。

本批五类资产的Runtime有效placeholder剩余0。项目清单另有20组非本轮范围的美术仍为pending（地区、项目、菜单、特殊内容各自的配套资产），未虚报为FINAL；全项目`finalArtReady`因此仍为false。旧占位文件作为回退历史仍保留，不能以磁盘文件数代替有效占位数量。

未解决技术问题：无已知阻塞。本次完成浏览器验收与Android离线资源包，未重新构建发布APK、未安装真实设备、未修改真实玩家存档；不将浏览器验收表述为真机验收。

无剧透机器状态：`artifacts/internal-art/work1/engineering-status.json`。
完整技术证据：`artifacts/internal-art/work1/runtime-final-qa/report.json`、`node-tests-final.log`、`offline-package-qa.json`。这些目录中的PNG截图与review sheet属于内部内容，不应主动向玩家展示。
