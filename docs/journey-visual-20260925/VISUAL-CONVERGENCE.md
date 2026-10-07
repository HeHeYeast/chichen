# 寻访构图收敛 · 2026-09-25

本轮范围仅寻访。全游戏 Completion Pass 已停止。确认 Mockup 保持原样；只调整三个页面的构图、区域层级、素材显示比例和动态信息承载。没有新生成美术，没有修改其他视觉族。

## 同框对比方法

- 对照尺寸为 390×844。Mockup 按三个页面外框裁切，等比缩放并居中，保留细小侧边留白，不拉伸。
- 修改前与修改后的 Runtime 使用相同测试存档、固定时钟、同行和视口。修改前代码通过独立浏览器资源替换复现，没有倒退或覆盖项目文件。
- Runtime 图片是原始浏览器截图；并排合成不修改截图内容。额外提供 50% 透明叠图、区域边界图。
- 边界标记包括 header、primaryArt、secondaryAction、dynamicCharacter、CTA、bottomNav；地区页另标 target，归来页另标 ribbon。叠图中虚线是参考区域，实线是运行区域。
- 截图为浏览器手机视口，并非实体手机拍摄。

## 三组最终对照

### 世界地图

![世界地图](../../artifacts/journey-convergence/map-ab.png)

[修改前 / 后](../../artifacts/journey-convergence/map-before-after.png) · [50% 叠图](../../artifacts/journey-convergence/map-overlays.png) · [区域边界](../../artifacts/journey-convergence/map-regions.png) · [未出发状态](../../artifacts/journey-convergence/after/idle-map-390.png)

地图从标题下方连续延伸到导航上缘；目的地与主要操作浮在地图上。四地区由上左、上右、中部溪岸、下右盐田构成，河道随底图等比放大裁切、上移，与中部溪岸保持对应。只强调当前路线，避免四条放射虚线把地图变成连接图。同行使用真实 PNG，错落排列，位置仍随原行程时间推进，并限制视觉中心以免队伍出框或被浮层遮住。

这张 Runtime 是实际在途状态，因此按钮显示“看看同行伙伴”；空闲时仍是“当前目的地 / 进入地区”。未出发时不虚构已经在路上的队伍。

### 地区页

![地区页](../../artifacts/journey-convergence/region-ab.png)

[修改前 / 后](../../artifacts/journey-convergence/region-before-after.png) · [50% 叠图](../../artifacts/journey-convergence/region-overlays.png) · [区域边界](../../artifacts/journey-convergence/region-regions.png)

结构恢复为地点 → 可以做这些事 → 本次目标 → 同行 → 出发。三个行为入口使用 78px 的正式物件图，分别保留短名称和一行用途；目标改为物件图加短状态和名称，首次标本机制在帮助中说明。三个角色占据独立的主要视觉带，按真实透明轮廓放置；野餐垫保持自然宽高比，恢复承托角色的面积。G/F 原值继续可见。

### 归来页

![归来页](../../artifacts/journey-convergence/return-ab.png)

[修改前 / 后](../../artifacts/journey-convergence/return-before-after.png) · [50% 叠图](../../artifacts/journey-convergence/return-overlays.png) · [区域边界](../../artifacts/journey-convergence/return-regions.png)

主要顺序为归来的伙伴 → 新发现 → 名称与大型发现物 → 少量收益 → 收好发现。故事正文仍保留在“查看地区发现”中。普通材料缩成物件、数量、短名称；材料包容量与逐项整理藏在“整理”入口，满包时仍直接提示收获被保留。主按钮和查看详情改为上下主次操作，不再平分底部空间。印带使用分区伸缩，去掉原先整张图的非等比拉伸。

## 差异 checklist

| Mockup 特征 | 修改前 Runtime | 修改 | 修改后证据 |
|---|---|---|---|
| 地图是主体，操作覆盖其上 | 固定 460px 地图区，下方独立信息段 | 连续底图 + 目的地浮层 | map-ab / map-regions |
| 地标与河道分布支撑纵向构图 | 地标整体偏低，溪岸偏左，河道较窄 | 四节点重定位，底图等比覆盖并上移 | map-overlays |
| 队伍沿路线错落出现 | 小图横排，靠近底部或出框 | 真实角色扩大、错落、光学位置限制 | map-390 |
| 三个行为是重要视觉入口 | 47–49px 小图标，缺行为区标题 | 78px 物件图 + 独立行为区 | region-regions |
| 地点—行为—目标—同行—出发 | 主次偏平，目标堆三行说明 | 独立语义区域，短目标状态 | region-ab |
| 同行角色站在足够大的垫子上 | 垫子视觉宽度不足 | 根据原宽高比恢复承托面积 | region-ab |
| 大型发现物是视觉中心 | 180px 容器、正文夹在发现和收益之间 | 208px 容器、扩大真实发现物，正文移至详情 | return-ab |
| 收益是次级，主操作明确 | 材料包、管理和两个操作挤在一起 | 收益短行；管理入口；纵向主次 CTA | return-regions |
| 主页面少文字 | 归来页同时展示故事正文 | 正文留在地区发现，主页面仅结果 | return-ab |

区域占屏比例（按 844px 全屏高度；表示构图，不是像素相似度评分）：

| 区域 | Mockup | 修改前 | 修改后 |
|---|---:|---:|---:|
| 世界地图底图区域，包含浮层覆盖范围 | 81.5% | 54.5% | 83.2% |
| 地图操作区 | 17.6% | 20.2% | 17.8% |
| 地区两个地点区 | 19.7% | 22.6% | 19.9% |
| 地区行为区 | 22.0% | 10.4% | 20.2% |
| 归来发现物区域 | 21.0% | 21.3% | 24.6% |
| 归来材料收益区 | 8.1% | 11.4% | 8.3% |

旧归来页容器高度本来接近参考，但其中真实发现物偏小，且旁边正文抢占注意力。因此本轮同时调整内部素材比例、阅读顺序和文字承载，未把“区域高度相近”当作视觉已经通过。

## 验证与保留差异

- 390×844、430×932：三个主要构图完整可见；320×568：保持素材尺寸，内容区可滚动，主操作固定，未将全页压小。
- 专项构图脚本检查地图占比、四个地区名称不被浮层遮挡、地区五层顺序、主内容可见、CTA 位于底栏上方。
- 完整寻访验收覆盖四地区、空队伍、满队伍、锁定、条件不足、在途、普通/标本/见闻/事件归来、满包、带货、召回、保存重载，共 39 张状态截图；页面错误与缺图均为 0。
- 141 项寻访、存档和生意相关测试通过。
- 与本轮开始时逐文件比对，仅 `web/journey.css`、`web/journey-art.js`、`web/regional-ui.js` 三个 Runtime 文件变化；全部美术像素和 manifest 未变。经济、概率、保底、材料、路线规则、存档引擎及其他视觉族未改。生意基准截图像素差为 0。

Runtime 保留真实“藏在菜畦边的香气”、荠菜外观与真实数量；不伪造 Mockup 的谷穗和 ×3/×2。未辨认材料继续显示未知袋。原角色造型、正式地标/信封轮廓及已冻结的全局底栏与参考仍有细节差异；本轮没有通过重绘角色、增加外围叶片或替换底栏来追逐 AI 示例像素。

证据：[区域测量](../../artifacts/journey-convergence/composition-metrics.json)、[状态回归](../../artifacts/journey-family/runtime/report.json)、[业务测试](../../artifacts/journey-convergence/unit-tests.txt)、[范围校验](../../artifacts/journey-convergence/preservation.json)。

本轮到此停止，等待这三组对照的视觉验收，不扩展其他页面或全游戏 Completion Pass。
