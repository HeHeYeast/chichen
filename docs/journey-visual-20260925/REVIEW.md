# 寻访视觉族 · Runtime 验收

> 此文保留首次实现记录。用户指出其构图仍有差距，最新修正与三组验收图见 [寻访构图收敛](VISUAL-CONVERGENCE.md)。

2026-09-25。已完成浏览器实现与自动验收，等待用户视觉确认。到此停止；不扩展其他一级入口，不构建或安装APK。生意模块沿用用户已确认版本。

## 七步完整流程

![地图、地区地点追寻、同行选择](../../artifacts/journey-family/runtime-1.png)

![出发确认、在途、归来发现](../../artifacts/journey-family/runtime-2.png)

![普通归来](../../artifacts/journey-family/runtime-3.png)

四地区均有独立节点及两个可点击地点；谷物棚按原内容名称显示“谷物棚边”。地图状态、名称、旗帜、路线、同行全部由Runtime叠加。伙伴是原资产，使用可见轮廓尺寸，无图鉴白色贴纸边。队伍沿路径的位置按原行程时间计算，只影响显示。

## Mockup / Runtime

![世界地图对照](../../artifacts/journey-family/map-ab.png)

![地区准备对照](../../artifacts/journey-family/region-ab.png)

![归来发现对照](../../artifacts/journey-family/return-ab.png)

左侧为制作前的Mockup，右侧为未经修饰的浏览器截图。并排图仅对Mockup等比缩放；没有把截图修成设计图，也没有把完整Mockup接入游戏。

Mockup里的“金黄的谷穗”、示例数量和G数值并非游戏内容。Runtime展示真实的“藏在菜畦边的香气”、荠菜形态与原G/F；未辨认实物继续显示未知袋。空队伍地图不会凭空画三只伙伴；出发后显示实际同行。底栏与帮助沿用已确认全局版本。

## 差异收敛清单

| Mockup特征 | 首轮Runtime | 修改 | 修改后证据 |
|---|---|---|---|
| 四个清楚地标，简单背景 | 图形可见，但按压反馈改变节点定位 | 定位与按压缩放拆开；路径不接收点击 | [地图](../../artifacts/journey-family/runtime/01-world.png) |
| 地点有接地感，小旗选中 | 8地点原图留白不同 | alpha连通区域分组、紧边裁切、统一padding与底中anchor | [切分资产](../../artifacts/journey-family/segmented-assets.png) |
| 小伙伴在垫子上，尺寸一致 | 原PNG透明边界与组图比例不同 | 使用原角色可见bounds作栅格图片视口，保留画风与自然宽高比 | [同行](../../artifacts/journey-family/runtime/04-companions.png) |
| 伙伴与G/F完整可读 | 390高布局G/F一行略被页脚遮住 | 去除标题和追寻区多余留白；小屏改滚动，不继续压缩角色 | [地区](../../artifacts/journey-family/runtime/03-region-team.png) |
| 轻柔布料主按钮 | 整图拉伸造成厚边扁圆 | 正式布底九宫切片，保持薄边和圆角 | [出发确认](../../artifacts/journey-family/runtime/05-confirm.png) |
| 展开的发现绶带 | 等比缩放使绶带过窄 | 对文字承载底单独定义横向伸展，不影响物件的等比规则 | [新发现](../../artifacts/journey-family/runtime/07-new-discovery.png) |
| 新发现突出，普通归来简洁 | 原流程是说明和日志 | 信封+真实标本/事件物件，普通结果使用收集袋与实物数量 | [事件](../../artifacts/journey-family/runtime/return-event.png) / [普通](../../artifacts/journey-family/runtime/08-ordinary-return.png) |
| 概率、保底不占主操作页 | 原有说明卡密集 | 当前目标与缺项留主页面，完整预览/保底/F/G/采样/方法补全移入帮助 | [帮助](../../artifacts/journey-family/runtime/help.png) |
| 原色、原字体、原全局导航 | 不允许新生成素材覆盖全局样式 | 寻访样式独立限定作用域，生意截图逐像素核对 | [保留审计](../../artifacts/journey-family/preservation.json) |

## 正式美术与生产记录

- [视觉规范与资产拆解](VISUAL-SPEC.md)
- [来源与最终提示词](PROMPTS.md)（内置imagegen，未使用API/CLI生成）
- [独立素材总览](../../artifacts/journey-family/segmented-assets.png)
- [Manifest](../../web/art/golden-journey/manifest.json)：36个独立透明物件 + 1张纯地形背景。包含sourceCell、sourceBounds、visualBounds、anchor、padding、透明信息、校验哈希。

切分不是矩形粗裁：先在整张表上识别alpha连通区域，按语义位置分组；同一物件的独立麦粒、脚印、水滴一起保留；小噪点去除；实际轮廓越过虚拟格线仍完整提取。留白统一8px，地标/地点按底中锚点，普通图标按中心锚点。Runtime的SVG仅作为原PNG裁切视口和动态路线，不用于画正式美术。

## 数据与交互验收

[39张状态截图及浏览器报告](../../artifacts/journey-family/runtime/report.json)。覆盖：

- 未开放／已开放四地区、开放条件、真实两地点、首标本覆盖所选方向的原规则；
- 空队伍、满队伍、重复品种排除、无可用库存、默认留一、最后一只提示；
- 条件不足仍允许普通材料行程，不人为提高出发门槛；
- 出发、在途、存档重载、实际计时归来、归来待领；
- 新标本、新见闻、新事件、普通归来、免费辨认；
- 沿湾路标完整归来后开放海湾；
- 可选带货的六只选择、冻结票据、重载、完成后扣货并占原材料槽；
- 满包保留材料、单独领取CP/线索、提前召回；
- 320／390／430／1280宽无横向溢出、无页面报错、无缺失素材。320×568使用滚动，实际操作可达；不以挤压间距容纳全部内容。

[141项测试通过](../../artifacts/journey-family/unit-tests.txt)：四地区探索、保底、首次标本、材料、方法、批次、地区视图、存档合同，以及营业／订单／常客／项目回归。

[冻结核对](../../artifacts/journey-family/preservation.json)：370个原有web文件保持字节一致，其中218个为美术／字体。原有web文件只修改regional-ui.js和index.html；新增寻访专属表现文件。概率、材料池、路线、RNG、时间轴、存档、交易与业务源文件全部保留。营业截图平均像素差为0。

Android离线资源包验证通过：871文件，66.4 MiB；37个寻访绘画资产全部收录，源Asset Sheet与Mockup不进入运行包。仅检查资源打包，没有构建、签名、安装或真机验收。
