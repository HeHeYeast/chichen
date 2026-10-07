# 图像生成记录

本轮使用内置 image_gen，每个候选单独生成。场景美术为生成图，最终 390×844 页面由场景图与统一 HTML/CSS HUD、导航、标签排版合成并通过浏览器截图输出。无正式 Runtime 接入。

下文保留实际使用的完整提示词；用户的塔塔截图和鸡宝成功页面先经直接看图分析，再转为画风约束。未将参考游戏原图裁为资产。

| 候选 | 完整提示词 | 项目内场景源图 |
|---|---|---|
| B1 温和营地型 | [B1-PROMPT.txt](B1-PROMPT.txt) | [b1.png](../../../../web/prototypes/farm-b-scene-gate-20260928/art/b1.png) |
| B2 功能聚落型 | [B2-PROMPT.txt](B2-PROMPT.txt) | [b2.png](../../../../web/prototypes/farm-b-scene-gate-20260928/art/b2.png) |
| B3 活力活动型 | [B3-PROMPT.txt](B3-PROMPT.txt)＋[局部修正](B3-EDIT-PROMPT.txt) | [b3.png](../../../../web/prototypes/farm-b-scene-gate-20260928/art/b3.png) |

B3 初稿 `art/b3-study.png` 仅供追溯；最终界面使用已修正陈列位的 `art/b3.png`。所有图片均从内置工具输出位置复制到项目，保留原始输出。没有调用 CLI / API 后备模式，也没有用程序绘制占位建筑替代生成场景。

最终图的可读中文、货币状态、建筑标签、五栏导航由页面排版提供；原图没有烘焙文字。角色、建筑、路径和景观已经在生成场景内。本轮没有产出可直接供 Runtime 分层使用的独立建筑精灵，后续需根据所选方向另行规划资产拆分。
