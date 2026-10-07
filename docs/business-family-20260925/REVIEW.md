# 生意视觉族 · 最终验收

本轮收口营业、订单、常客、项目、账单；未进入寻访。截图来自隔离存档的真实浏览器 Runtime（390×844），没有把 Mockup 贴进游戏，也未重绘运行截图。未连接手机或安装APK。

## 五页截图

| 页面 | 最终Runtime |
| --- | --- |
| 营业 | [营业](../../artifacts/business-family/states/01-business.png) |
| 订单 | [进行中和新询问](../../artifacts/business-family/states/02-orders.png) |
| 常客 | [头像、身份和新故事](../../artifacts/business-family/states/03-regulars.png) |
| 项目 | [溪岸风味篮筹备](../../artifacts/business-family/states/04-projects.png) |
| 账单 | [本次营业结算](../../artifacts/business-family/states/05-ledger.png) |

[前三页总览](../../artifacts/business-family/runtime-01-business-orders-regulars.png) · [项目与账单总览](../../artifacts/business-family/runtime-02-projects-ledger.png)

## Mockup / Runtime

[订单对比](../../artifacts/business-family/orders-ab.png) · [常客对比](../../artifacts/business-family/regulars-ab.png) · [项目对比](../../artifacts/business-family/projects-ab.png) · [账单对比](../../artifacts/business-family/ledger-ab.png)

项目与账单已自行检查并修正：装饰不再随边框压扁，主按钮不拉成扁长条，项目说明去除内部配置措辞，账单去掉重复导航以突出收入与来客。Mockup上的示例昵称、费用、数量和角色组合未硬编码进Runtime，差异来自真实状态。保留现有全局底栏，并未采用Mockup中生成的导航变体。

## 真实状态验收

| 页面 | 空状态 | 未解锁 | 可操作 | 不可操作 | 完成 |
| --- | --- | --- | --- | --- | --- |
| 营业 | 未备货；0成交 | 早期小店门槛 | 备货/开张/查看账单/收摊 | 不满足开张条件、数量上限/留一只 | 售罄或提前结算，不重复入账 |
| 订单 | 无已接采购/无询问 | 240收取与发现门槛 | 接取、交付、预留、释放、略过、旧采购 | 两单满额不能再接；未选数量不能交付 | 完成订单移出进行中，累计已办好单数 |
| 常客 | 无新故事 | 未认识剪影与帮助中的门槛 | 打开详情、读新故事、回读、置顶 | 无新故事不出现阅读按钮 | 四段全读完、纪念物保留、无重复奖励 |
| 项目 | 交付列表无候选时提示；早期展示下一份筹备计划 | 明确开始条件 | 当前阶段可交付、可登记或支付 | CP不足、条件未达成、交付未齐禁用完成 | 已完成印章，成果/后续阶段可回看 |
| 账单 | 零成交小票 | 无独立解锁：未开张时不伪造账单 | 看帮助、实际来客故事跳转、回到营业 | 阅读无付款操作，预计奖励不算已入账 | 实售、加成、总收入对应原结算记录 |

项目原模型早期始终保留一个待开启项目，因此没有新增“项目完全消失”的伪状态。账单不新设业务门槛；零成交作为可达空状态验收。

状态截图：[目录与报告](../../artifacts/business-family/states/report.json)。包括320/390/430/1280宽度，共36张状态/尺寸截图，未发现横向溢出、缺图或页面异常。

## 业务与保留验证

- 166项相关测试通过，0失败：[测试输出](../../artifacts/business-family/unit-tests.txt)。包括新增的订单示例角色只读检查：每张示例图必须已收录且属于原命令实际接受的变体允许集合。
- [营业回归](../../artifacts/business-family/business/browser-report.json)：实际在售、只读账单、菜单、备货、留一只、容量、收摊取消/确认、重复回看、长名称与多品种。
- [订单/常客回归](../../artifacts/business-family/orders-regulars/report.json)：真实交付、预留/释放、故事产生与阅读、6只不提前来客、12只实际来访、刷新不丢失或重复。
- [项目回归](../../artifacts/business-family/projects/report.json)：阶段支付/交付不重复、品种锁定、成果与菜单预设、画像不消耗库存。
- [收藏回归](../../artifacts/business-family/collection-regression/browser-report.json)通过。
- [保留证据](../../artifacts/business-family/preservation.json)：205个已有美术/字体文件未变；营业与图鉴相同状态截图逐像素相同。营业/订单/常客/项目/库存/时间线/存档/命令核心文件未修改，厨房、农场、寻访、全局底栏文件未修改。
- 离线资源打包通过：830个文件，59.4 MiB；新切片已进入离线包。未构建或安装发布APK。

## 交付材料

[Visual Spec与拆解](VISUAL-SPEC.md) · [项目/账单Mockup](projects-ledger-mockup.png) · [最终表面Sheet](asset-sheets/F03-surfaces-separated.png) · [状态Sheet](asset-sheets/F02-states.png) · [生成提示集](PROMPTS.json)

素材通过内置imagegen生成，再按用户要求切分独立RGBA并登记manifest；角色均复用已有资产。到此停止，等待下一阶段。
