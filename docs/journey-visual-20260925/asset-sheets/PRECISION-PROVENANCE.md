# 寻访精确复刻素材来源

本轮运行时素材全部独立于文字和游戏数据。没有把整张Mockup当页面背景，也没有把目标名称、数量或按钮文字画入新素材。

|Sheet|来源/制作方式|内容与用途|
|---|---|---|
|J06-precision-ui.png|内置 imagegen，以已确认画稿为风格参照|16个分离组件：Ribbon、左右植物、sparkle、brush、Reward Slot、nameplate、button、信封前后、庆祝、纸条、树、草石、钟表|
|J07-precision-landscape.png|内置 imagegen|四地标、两个地点、山丘、草地、草石；作为正式候选资产保留，最终关键地标替换为J09|
|J08-river.png|内置 imagegen|独立透明河流；最终主地图使用J11原稿河流|
|J09-reference-components.png|确认原稿语义多边形分割，去纸底、按颜色清理、组件归一化|24个无UI文字组件，包括地标、地点、树、草石、植物、角色、图标和星光|
|J10-ui-extracted.png|内置 imagegen，以J10-ui-extraction-input.png为精确参照|空白Ribbon、按钮、标签、笔刷；不含文字|
|J11-reference-environment.png|确认原稿的绿色/蓝色环境分色提取|map-hills、map-ground、map-river、place-environment、hero-ground；不含UI文字或交互控件|

制作要求摘要：暖奶油纸色的手绘游戏视觉、柔和绿色、棕色轮廓；透明背景，元素彼此分离；匹配参照的颜色、比例和线条；不生成文字、标签、数量、水印。地图环境与地标独立，信封与植物/发现物独立，确保真实数据可以在DOM中呈现。

原始内置 imagegen 输出（此次会话生成目录中的文件名）：

- J06：`exec-b837a6ff-2eb2-49d1-b728-d2242fde52b9.png`
- J07：`exec-e1cdad48-0529-4026-b57e-59e13ee43103.png`
- J08：`exec-d4622a46-3c18-4175-b289-e0a00ef3d326.png`
- J10：`exec-3f7d9536-bc9e-475f-817a-684a7b69bca7.png`

文件已复制到本目录，运行时不依赖用户目录。以上是来源索引与制作要求摘要，不冒充完整逐字的历史prompt日志。

## 可复现流水线

1. `python tools/extract-journey-reference.py`：生成J09/J11及原稿坐标、mask元数据。
2. `python tools/segment-journey-precision.py`：连通组件分割、alpha清理、8px padding归一化、PNG输出、manifest和光学metrics；生成4个内嵌PNG的SVG底板。
3. `python tools/build-journey-font.py --check`：验证四个真实字重的中文覆盖。
4. 浏览器从manifest对应的正式素材加载；文字、路线、队伍位置和奖励数量由现有真实数据驱动。

输出：`web/art/golden-journey/precision-*.png`，59个本轮PNG组件；`manifest.json`记录来源、sourceCell、sourceBounds、visualBounds、padding、anchor及SHA256，`wrappers`记录4个SVG的来源与SHA256。SVG只负责裁切/尺寸映射，没有用粗糙矢量重画插画。

## J12 计时质感修订

按用户反馈新增正式纸纹计时胶囊与独立时钟，两项组件；使用内置imagegen。完整prompt见J12-timer-prompt.json。经相同分割流程接入后，共61个本轮PNG组件、5个SVG底板。计时文字保持独立DOM。
