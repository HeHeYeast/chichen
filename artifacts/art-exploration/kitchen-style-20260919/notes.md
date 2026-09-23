# 厨房背景画风候选｜2026-09-19

> 历史素材来源记录，非当前实施要求。B1/B2及过度简化的墙平台方案已否决；最终执行方向见[UI／美术规范](../../../docs/ui-ux.md)，本文件中的推荐仅代表生成当时。

本轮为用户挑选画风的四张独立小样，统一使用木板房题材以便比较。**未接入游戏，未替换现有素材；不是四级厨房成套交付。**

## 对照判断

原版参考依靠较少色块、简练轮廓和低密度材质线表达房间；当前 v7 背景的木纹、细部高光、柔和渐变及体积阴影明显更多。此次尝试降低细节密度，并比较原版改绘与现有稿简化的差异。

- A：原版朴素平涂方向。较低饱和度、减少木纹；输出仍有柔和渐变，并非严格纯平涂。
- B：暖色手绘方向。更温暖、轮廓更明显；仍有一定现代卡通感和渐变。
- C：清爽淡彩方向。灰绿木墙与浅木台面，整体更清淡；属于风格探索，未作为房屋阶段新设定。
- D：直接以原版为底改绘。房屋结构和造型最贴近原版，仍有柔和渐变。
- 四张都保留中央空台面；未把鸡宝、窝、收取容器、文字或 UI 烘焙进背景。

建议先确定画法再扩展四级厨房；下一轮需在真实鸡宝、孵化窝和厨具同屏时判断一致性，不能只靠空背景定稿。若要求严格平涂与精确线宽，选稿后还需对轮廓和色块做受控重绘，单靠反复随机生成不能保证一致。

## 生成方式与验证范围

使用内置 image_gen 工具，四个独立调用，没有使用 CLI/API fallback。完整生成原图原样复制到本目录，没有脚本修图。
四张均已查看；本轮检查仅覆盖图像内容、文件存在、图片尺寸以及复制文件与生成源的一致性。没有执行游戏集成、布局适配或交互验收。提示词指定的 30%/66% 分界仅是生成目标，不能当作像素级注册值。

## 参考文件

A/B/C：
1. 当前背景编辑目标：`web/art/kitchen-stage-1-v7.png`。
2. 原版画法参考：`assets/png/Tool/Tool0/tool_0_0_0_0.jpg`。
3. 原版画法参考：`assets/png/Tool/Tool0/tool_0_0_2_0.jpg`。

D：
1. 原版编辑目标：`assets/png/Tool/Tool0/tool_0_0_0_0.jpg`。
2. 原版窗口画法参考：`assets/png/Tool/Tool0/tool_0_0_2_0.jpg`。

## 交付文件与完整提示词

### A 原版朴素平涂

候选文件：[kitchen-style-a.png](kitchen-style-a.png)

源文件：`C:\Users\管啸野\.codex\generated_images\01a0b8de-fdb3-70b3-bf39-122b00673ec1\exec-c8ed7dc8-f06f-4615-891b-974fba548b22.png`

```text
Use case: style-transfer.
Asset type: ONE standalone portrait 9:16 background plate for an existing small-screen 2D chicken-collecting game, no contact sheet.
Input images: Image 1 is the EDIT TARGET, the current wooden kitchen background. Images 2 and 3 are ORIGINAL GAME STYLE REFERENCES ONLY. They demonstrate economical flat colors, simple handmade shapes and very sparse surface detail. Do NOT copy the nest, cushion, basket, bowl, or any foreground gameplay prop from them.
Primary request: REDRAW image 1 in a much simpler 2D sprite-era game illustration style that can coexist with the original game. This must look deliberately drawn as a game background, with significantly less rendering and visual detail.
Preserve the wooden-room subject and overall layout: frontal back wall made of planks, one small empty shelf on left, one simple four-pane window on right, one enormous totally empty wooden table, simple open lower shelf and two table legs. Same straight-on camera with table seen mildly from above. The room is modest, neatly repaired, not luxury.
Composition: portrait full bleed. Back wall from top to 30% of height, entire empty tabletop from 30% to 66% of height, table front and legs below 66%. The top 10% is hidden by HUD, so shelf and window should be visible between 10% and 30%. Do not add objects on the tabletop. Keep the center x15–85%, y30–66% visually calm for 24 existing character sprites and a nest added later. Table edges are straight horizontal boundaries. No tilted room, no fisheye, no isometric view.
Strict exclusions: no animals, people, eggs, nests, beds, cushions, baskets, dishes, pots, plants, food, UI, writing, logo or watermark. No beams of light, photographic grain, paper texture, painterly texture, dense wood grain, glossy highlights, bevel highlighting, ambient occlusion, blurred shading, vignette, realistic 3D materials or intricate scenery outside the window. The final must be a complete clean background image, not a framed illustration.
Style direction A — closest to the original game's humble sprite-era backgrounds. Use muted taupe wood wall, dusty biscuit-tan tabletop, dark cocoa-brown contour lines, a very pale quiet blue window. Narrow palette of roughly 8–12 flat opaque colors. Slightly angular, workmanlike hand-drawn geometry. Outlines are modest medium weight, neither thick black stickers nor sketchy scratch marks. Each object uses one flat base color and at most ONE hard-edged shadow color. Absolutely NO soft shading or color gradients. Remove every wood-grain streak except at most three short purposeful marks across the whole table; show plank seams with single simple lines. Window exterior is a single pale blue fill and one white flat cloud shape. Do not beautify into polished cozy concept art. Aim for the deliberate simplicity and modest visual density of original reference 2. The wall and table must read as broad uninterrupted flat areas at 320 pixels wide.
```

### B 暖色手绘线稿

候选文件：[kitchen-style-b.png](kitchen-style-b.png)

源文件：`C:\Users\管啸野\.codex\generated_images\01a0b8de-fdb3-70b3-bf39-122b00673ec1\exec-c1061b36-e63f-42eb-8e00-cab0de2eb4f6.png`

```text
Use case: style-transfer.
Asset type: ONE standalone portrait 9:16 background plate for an existing small-screen 2D chicken-collecting game, no contact sheet.
Input images: Image 1 is the EDIT TARGET, the current wooden kitchen background. Images 2 and 3 are ORIGINAL GAME STYLE REFERENCES ONLY. They demonstrate economical flat colors, simple handmade shapes and very sparse surface detail. Do NOT copy the nest, cushion, basket, bowl, or any foreground gameplay prop from them.
Primary request: REDRAW image 1 in a much simpler 2D sprite-era game illustration style that can coexist with the original game. This must look deliberately drawn as a game background, with significantly less rendering and visual detail.
Preserve the wooden-room subject and overall layout: frontal back wall made of planks, one small empty shelf on left, one simple four-pane window on right, one enormous totally empty wooden table, simple open lower shelf and two table legs. Same straight-on camera with table seen mildly from above. The room is modest, neatly repaired, not luxury.
Composition: portrait full bleed. Back wall from top to 30% of height, entire empty tabletop from 30% to 66% of height, table front and legs below 66%. The top 10% is hidden by HUD, so shelf and window should be visible between 10% and 30%. Do not add objects on the tabletop. Keep the center x15–85%, y30–66% visually calm for 24 existing character sprites and a nest added later. Table edges are straight horizontal boundaries. No tilted room, no fisheye, no isometric view.
Strict exclusions: no animals, people, eggs, nests, beds, cushions, baskets, dishes, pots, plants, food, UI, writing, logo or watermark. No beams of light, photographic grain, paper texture, painterly texture, dense wood grain, glossy highlights, bevel highlighting, ambient occlusion, blurred shading, vignette, realistic 3D materials or intricate scenery outside the window. The final must be a complete clean background image, not a framed illustration.
Style direction B — warm hand-inked cartoon game background. Use honey-beige flat timber, cream light areas, medium walnut outlines, quiet sky blue and one sage-green flat outdoor silhouette. Confident visibly hand-drawn, slightly irregular contours with tiny natural line-width variation; no scribbling, no pencil shading. Shapes are a little chunky and friendly. At most 12–16 distinct opaque colors; each plane has one solid base and one small hard-edged cel shadow. Remove all smooth gradients, shiny edge strips and elaborate perspective rendering. Timber is communicated primarily by large plank shapes and only a few short graphic grain marks, never full-surface texture. Window outside has one white cloud and one flat green hill, no scenic landscape. Keep broad tabletop especially plain. Deliberate characterful drawing, not generic polished AI game key art. Simplicity comparable to a lovingly hand-drawn old handheld game.
```

### C 清爽淡彩卡通

候选文件：[kitchen-style-c.png](kitchen-style-c.png)

源文件：`C:\Users\管啸野\.codex\generated_images\01a0b8de-fdb3-70b3-bf39-122b00673ec1\exec-488e3daf-ae8d-4f64-acb3-0d5f2b01a36f.png`

```text
Use case: style-transfer.
Asset type: ONE standalone portrait 9:16 background plate for an existing small-screen 2D chicken-collecting game, no contact sheet.
Input images: Image 1 is the EDIT TARGET, the current wooden kitchen background. Images 2 and 3 are ORIGINAL GAME STYLE REFERENCES ONLY. They demonstrate economical flat colors, simple handmade shapes and very sparse surface detail. Do NOT copy the nest, cushion, basket, bowl, or any foreground gameplay prop from them.
Primary request: REDRAW image 1 in a much simpler 2D sprite-era game illustration style that can coexist with the original game. This must look deliberately drawn as a game background, with significantly less rendering and visual detail.
Preserve the wooden-room subject and overall layout: frontal back wall made of planks, one small empty shelf on left, one simple four-pane window on right, one enormous totally empty wooden table, simple open lower shelf and two table legs. Same straight-on camera with table seen mildly from above. The room is modest, neatly repaired, not luxury.
Composition: portrait full bleed. Back wall from top to 30% of height, entire empty tabletop from 30% to 66% of height, table front and legs below 66%. The top 10% is hidden by HUD, so shelf and window should be visible between 10% and 30%. Do not add objects on the tabletop. Keep the center x15–85%, y30–66% visually calm for 24 existing character sprites and a nest added later. Table edges are straight horizontal boundaries. No tilted room, no fisheye, no isometric view.
Strict exclusions: no animals, people, eggs, nests, beds, cushions, baskets, dishes, pots, plants, food, UI, writing, logo or watermark. No beams of light, photographic grain, paper texture, painterly texture, dense wood grain, glossy highlights, bevel highlighting, ambient occlusion, blurred shading, vignette, realistic 3D materials or intricate scenery outside the window. The final must be a complete clean background image, not a framed illustration.
Style direction C — crisp light flat cartoon game illustration with clean restrained lines. Keep this a modest wooden room, but paint its planks pale muted grey-sage; the broad table remains light natural biscuit wood. Warm ivory shelf edges, dark warm grey-brown uniform outlines, a flat powder-blue window. Rounded corners used sparingly in the shelf and window; the overall carpentry remains structurally simple. Use only broad solid fills and one unobtrusive hard-edged shadow per object. No gradients anywhere. No wood grain on painted wall; the table has just plank seams and at most two tiny graphic wood knots. Outside the window is plain sky and one simple white cloud. This should feel airy, plain, unpretentious and compatible with small flat character sprites, not glossy 3D, not decorative illustration. Fewer lines and more quiet empty areas than image 1.
```

### D 原素材直接改绘

候选文件：[kitchen-style-d.png](kitchen-style-d.png)

源文件：`C:\Users\管啸野\.codex\generated_images\01a0b8de-fdb3-70b3-bf39-122b00673ec1\exec-b8614cd3-8ebd-461c-84c4-11c90045a6f2.png`

```text
Use case: precise-object-edit.
Asset type: clean portrait background plate for the SAME existing 2D chicken game.
Image 1 is the ORIGINAL GAME background to edit. Image 2 is another ORIGINAL GAME background, ONLY a reference for drawing a very simple window. These original drawings are the authoritative style. Keep that plain flat sprite-era rendering, muted warm palette, simple uniform outlines and intentionally economical artwork. Do not modernize or polish the art.

Edit image 1 as follows:
1. Completely remove the big central straw nest and the partial wicker basket at the right. Reconstruct the table and wooden floor seamlessly behind them. The entire tabletop must now be absolutely empty. No replacement props.
2. The back wall occupies the top 30% of the whole portrait. Move the broad wooden tabletop rear edge to 30% of image height, and its front edge to 66%. Show the broad empty table from a slightly overhead angle. Below it are plain table legs and simple planked floor. Preserve the original wide frontal game camera, no narrow central vanishing point, no 3D view.
3. In the back-wall band between 10% and 28% of image height, replace the old small wall box with a small plain empty wooden shelf at left, and add a small rectangular four-pane window on the right. Window has only solid pale-blue panes, no landscape and no rendered lighting.
4. Make the wooden room modest and neatly repaired, slightly brighter than the original: wall a solid muted sand-brown, table a solid biscuit tan, dark muted brown joins and outline, lower floor a single muted brown. Keep the low visual density of the original, not dense modern cozy-game decoration.

The drawing medium is hard opaque FLAT COLOR SHAPES, each plane filled uniformly, with at most one simple hard-edged shadow patch where structurally necessary. Flat 2D game background, no spatial lighting simulation at all. All broad surfaces must be truly flat: no gradient, no texture, no smooth shading, no grain/noise, no ambient shadows, no sunlight, no specular shine, no edge glints, no vignette. Wood is identified by board seams and a maximum of four short simple grain lines in the entire image. Straightforward, slightly naive original game drawing, not premium concept art.
No nests, bowls, baskets, beds, cushions, birds, animals, eggs, people, UI, lettering, labels, logos or watermark. Single full-bleed portrait illustration, approximately 9:16. The whole central table is deliberately plain empty game space.
```

