# B 厨房布局修订｜2026-09-19

> 历史素材来源记录，非当前实施要求。B1/B2及过度简化的墙平台方案已否决；最终执行方向见[UI／美术规范](../../../docs/ui-ux.md)，本文件中的推荐仅代表生成当时。

## 用户反馈与本轮方向

用户认为 D 好一些，但更喜欢 B 的画法；此前 A/B/C 的布局像桌子，缺乏厨房感。本轮沿用 B 的暖木色、棕色轮廓与卡通画法，集中改厨房结构。D 只作为简洁造型的辅助参考。

本轮输出是两张可选背景小样，**未替换游戏素材，未修改四级房屋设定，未执行游戏布局接入**。

## 实际输出与观察

- **B1 正面厨房**：后方成排橱柜、左侧油烟机与双眼灶、锅、下方烤箱、中间窗口、右侧水槽与吊柜；前景为带下柜的宽备餐台。实际结果采用后方厨房加前景备餐台的构图，并没有完全服从提示词中的“同一连续台面、不要独立岛台”要求，因此交付名称用“正面厨房”而不宣称精确一字型连体结构。
- **B2 转角厨房**：左侧灶台沿转角连到后方水槽，吊柜和瓷砖明确厨房用途；前景是宽备餐台和下柜。浅透视的转角提供更清楚的厨房空间感。
- 两张保留 B 的暖色与轮廓，中央备餐台均未绘入鸡宝、窝或 UI。仍有柔和渐变，不能宣称已经严格纯平涂。
- 两张完整图均已查看。当前更推荐 B2 的厨房识别度；最终偏好由用户选择。
- 前景空台面起始高度约在图像 43%–45%，比当前游戏素材所用位置靠下。集成时需重新确认分区映射及 6×4 鸡宝、窝、HUD 的遮挡，不得直接把提示词目标当作实际像素坐标。

## 工具与交付

使用内置 image_gen，分别执行两次编辑生成；未使用 API/CLI fallback。生成原图直接复制到本目录，无裁剪、调色或脚本修图。

交付文件：
- [B1 正面厨房](kitchen-style-b1.png)
- [B2 转角厨房](kitchen-style-b2.png)

参考图按顺序：
1. 编辑目标：`artifacts/art-exploration/kitchen-style-20260919/kitchen-style-b.png`
2. 简洁造型参考：`artifacts/art-exploration/kitchen-style-20260919/kitchen-style-d.png`
3. 原版画法参考：`assets/png/Tool/Tool0/tool_0_0_2_0.jpg`

本轮核对范围：两张图片可读、尺寸、生成原文件与项目副本 SHA-256 一致、视觉内容。无游戏运行或交互验收结论。

## 完整提示词

### B1

源文件：`C:\Users\管啸野\.codex\generated_images\01a0b8de-fdb3-70b3-bf39-122b00673ec1\exec-83e50631-a61b-4395-9f99-65fe1250ace3.png`

```text
Use case: precise-object-edit.
Asset type: ONE full-bleed portrait 9:16 kitchen background for a 2D chicken-collecting game. One complete background, no panels.
Input image 1 is the B artwork EDIT TARGET: retain its friendly honey-beige colors, warm walnut contours, simple handmade cartoon drawing and economical large shapes. Image 2 is D, a secondary reference for restrained, straightforward original-game geometry. Image 3 is an ORIGINAL GAME STYLE reference only; never copy its nest, bowl or other gameplay objects.
User feedback to solve: B's color and drawing are preferred, but the scene currently reads as an empty wooden room with a freestanding table, not a kitchen. Change the architecture and furniture so that this is unmistakably a functional KITCHEN. This is a layout correction, not a new painting style.

Preserve: B's warm honey timber and cream palette, small plain blue window, dark warm brown contours, low visual density, slight hand-drawn shape irregularity, simple 2D cartoon rendering and large center free for the game's 24 chicken sprites. Frontal/slightly top-down game camera, not a wide-angle interior visualization. No fancy decor, no luxury fittings.
Rendering: opaque flat color blocks, use one restrained hard-edged shadow per plane, omit soft gradients as much as possible. Remove detailed woodgrain; at most a few short deliberate wood marks. No realistic material rendering, no glossy plastic, no bloom, no dramatic perspective, no painterly texture or tiny clutter. This should remain compatible with the old small 2D game, and not look like interior-design concept art.

Kitchen identity MUST be structural and visible: full lower kitchen cabinet bodies reach down to a low plinth, an inset sink visibly seated IN a counter with a connected faucet, a simple two-burner hob inset IN a counter, a small hood aligned over that hob, and one or two upper kitchen cupboards. No standalone table legs, no open space under a table, no decorative imitation appliances attached to nowhere. The sink/hob must have coherent support, counter cutouts and appropriate scale. A small cream tile backsplash protects the cooking/washing wall, surrounded by warm simple wooden construction. A single plain pot may sit on the rear hob; do not add loose items to the play area.
Composition: kitchen fittings and working back counter occupy roughly the upper 10–38% of the portrait, and are recognizable below the top 10% HUD band. A broad connected food-preparation counter occupies roughly 38–69%, with its central x12–88% ENTIRELY EMPTY for sprites to be added later. Below 69%, show solid cabinet doors/drawers and a small strip of floor. The foreground preparation surface must clearly belong to the fitted cabinetry, not an independent freestanding dining/work table.
No birds, people, eggs, nests, beds, baskets, containers in the play zone, text, UI, buttons, icons, logos or watermark. Do not render the game characters. Kitchen background only.
Specific layout B1: a compact straight fitted kitchen, with a single continuous wall-to-wall counter and matching golden-brown lower cabinets across its entire width. At upper-left sits a narrow plain matte grey hood; directly under it on the BACK LEFT EDGE of the counter is a modest two-burner hob and one small simple cooking pot. Center-back has a small wood-framed window. At back-right, the countertop has an inset cream sink basin and simple arched dull grey faucet; above/right is one small double-door wooden cupboard. A narrow cream tile backsplash joins the cooking zone to the sink. The broad clear prep area extends forward from this same counter, its front edge supported by wooden kitchen cupboards with a drawer row, and a modest small oven door on the left under the hob. No separate center island and no visible gap between the wall counter and front counter. Make all fitted elements easy to read at 320 px wide. This version is plain, compact, cozy and practical.
```

### B2

源文件：`C:\Users\管啸野\.codex\generated_images\01a0b8de-fdb3-70b3-bf39-122b00673ec1\exec-4e9d427e-4edd-4d9c-b079-f7b2156b6419.png`

```text
Use case: precise-object-edit.
Asset type: ONE full-bleed portrait 9:16 kitchen background for a 2D chicken-collecting game. One complete background, no panels.
Input image 1 is the B artwork EDIT TARGET: retain its friendly honey-beige colors, warm walnut contours, simple handmade cartoon drawing and economical large shapes. Image 2 is D, a secondary reference for restrained, straightforward original-game geometry. Image 3 is an ORIGINAL GAME STYLE reference only; never copy its nest, bowl or other gameplay objects.
User feedback to solve: B's color and drawing are preferred, but the scene currently reads as an empty wooden room with a freestanding table, not a kitchen. Change the architecture and furniture so that this is unmistakably a functional KITCHEN. This is a layout correction, not a new painting style.

Preserve: B's warm honey timber and cream palette, small plain blue window, dark warm brown contours, low visual density, slight hand-drawn shape irregularity, simple 2D cartoon rendering and large center free for the game's 24 chicken sprites. Frontal/slightly top-down game camera, not a wide-angle interior visualization. No fancy decor, no luxury fittings.
Rendering: opaque flat color blocks, use one restrained hard-edged shadow per plane, omit soft gradients as much as possible. Remove detailed woodgrain; at most a few short deliberate wood marks. No realistic material rendering, no glossy plastic, no bloom, no dramatic perspective, no painterly texture or tiny clutter. This should remain compatible with the old small 2D game, and not look like interior-design concept art.

Kitchen identity MUST be structural and visible: full lower kitchen cabinet bodies reach down to a low plinth, an inset sink visibly seated IN a counter with a connected faucet, a simple two-burner hob inset IN a counter, a small hood aligned over that hob, and one or two upper kitchen cupboards. No standalone table legs, no open space under a table, no decorative imitation appliances attached to nowhere. The sink/hob must have coherent support, counter cutouts and appropriate scale. A small cream tile backsplash protects the cooking/washing wall, surrounded by warm simple wooden construction. A single plain pot may sit on the rear hob; do not add loose items to the play area.
Composition: kitchen fittings and working back counter occupy roughly the upper 10–38% of the portrait, and are recognizable below the top 10% HUD band. A broad connected food-preparation counter occupies roughly 38–69%, with its central x12–88% ENTIRELY EMPTY for sprites to be added later. Below 69%, show solid cabinet doors/drawers and a small strip of floor. The foreground preparation surface must clearly belong to the fitted cabinetry, not an independent freestanding dining/work table.
No birds, people, eggs, nests, beds, baskets, containers in the play zone, text, UI, buttons, icons, logos or watermark. Do not render the game characters. Kitchen background only.
Specific layout B2: a compact L-shaped fitted kitchen that visibly wraps from the rear wall along the LEFT side. Show one coherent corner junction and connected lower cabinets, not a room containing a freestanding table. Back-right, place a small blue window over an inset cream sink and dull grey faucet. Back-left has a small wood wall cupboard; on the short LEFT RETURN of the countertop, up near the BACK of the scene and above the gameplay space, place a two-burner hob beneath a compact simple extractor hood. Use a small cream-tiled cooking/washing backsplash and warm honey wood elsewhere. The clear preparation counter comes forward as a broad connected L return with a continuous wood edge and full wooden cabinet fronts below; its huge central surface remains plain and empty. Use shallow controlled perspective with only one visible corner, no fisheye and no long tunnel-like perspective. Keep the kitchen fittings mostly in the upper 38% and extreme left upper margin, away from the central sprite area. The layout should feel like a small lived-in home kitchen with practical cooking/washing/preparation zones, drawn with the same simplicity and warm palette as B.
```

