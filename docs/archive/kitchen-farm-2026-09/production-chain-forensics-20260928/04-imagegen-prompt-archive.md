# ImageGen Prompt Archive

恢复了 80 个已完成 imageGeneration 事件，逐条保存真实工具字段、原始调用、输入变量定义及输出哈希。80 不是计费调用次数；失败、重试、未投影事件不以此计数。IG012 是同一早期会话的应用图标，保留作边界证据，不纳入 Kitchen 因果统计。

**分级：** ORIGINAL＝真实工具记录/调用；PARTIAL＝只有部分真实指令；RECONSTRUCTED＝分析者重建意图。本档案不编造缺失 Prompt。无图像生成的原型标 N/A，不凑 RECONSTRUCTED Prompt。

**模式：** 带 reference 的生成不能自动当作“编辑上一张”。本档案区分参考引导生成与明确 image edit/style transfer 意图；内置工具未暴露 images API endpoint，不推断实际服务端模式或模型版本。

|ID|时间（+08）|会话 / ordinal|分级|意图|真实提示词开头|
|---|---|---|---|---|---|
|[IG001](prompts/IG001.md)|2026-09-19T16:58:23|01a0b8de / 54|ORIGINAL|image edit / style transfer|Use case: style-transfer. Asset type: ONE standalone portrait 9:16 background plate for an|
|[IG002](prompts/IG002.md)|2026-09-19T16:59:05|01a0b8de / 55|ORIGINAL|image edit / style transfer|Use case: style-transfer. Asset type: ONE standalone portrait 9:16 background plate for an|
|[IG003](prompts/IG003.md)|2026-09-19T16:59:49|01a0b8de / 58|ORIGINAL|image edit / style transfer|Use case: style-transfer. Asset type: ONE standalone portrait 9:16 background plate for an|
|[IG004](prompts/IG004.md)|2026-09-19T17:01:50|01a0b8de / 79|ORIGINAL|image edit / style transfer|Use case: precise-object-edit. Asset type: clean portrait background plate for the SAME ex|
|[IG005](prompts/IG005.md)|2026-09-19T17:06:46|01a0b8de / 127|ORIGINAL|image edit / style transfer|Use case: precise-object-edit. Asset type: ONE full-bleed portrait 9:16 kitchen background|
|[IG006](prompts/IG006.md)|2026-09-19T17:07:24|01a0b8de / 128|ORIGINAL|image edit / style transfer|Use case: precise-object-edit. Asset type: ONE full-bleed portrait 9:16 kitchen background|
|[IG007](prompts/IG007.md)|2026-09-19T17:20:26|01a0b8de / 206|ORIGINAL|reference-guided generation|Use case: style-transfer. Asset type: portrait 9:16 background plate for a small 2D chicke|
|[IG008](prompts/IG008.md)|2026-09-19T17:23:22|01a0b8de / 222|ORIGINAL|reference-guided generation|Use case: style-transfer. Asset type: portrait 9:16 background plate for a small 2D chicke|
|[IG009](prompts/IG009.md)|2026-09-19T17:23:54|01a0b8de / 223|ORIGINAL|reference-guided generation|Use case: style-transfer. Asset type: portrait 9:16 background plate for a small 2D chicke|
|[IG010](prompts/IG010.md)|2026-09-19T17:24:41|01a0b8de / 228|ORIGINAL|reference-guided generation|Use case: style-transfer. Asset type: portrait 9:16 background plate for a small 2D chicke|
|[IG011](prompts/IG011.md)|2026-09-19T17:27:17|01a0b8de / 246|ORIGINAL|image edit / style transfer|Use case: precise-object-edit. 对这张原版《鸡宝厨房》的游戏场景做一张非常轻微的清稿优化版。它是唯一且严格的内容、画风、构图参考。用户明确要求“内容和|
|[IG012](prompts/IG012.md)|2026-09-19T17:36:16|01a0b8de / 308|ORIGINAL|image edit / style transfer|Use case: precise-object-edit. Asset type: Android adaptive launcher icon artwork, ONE squ|
|[IG013](prompts/IG013.md)|2026-09-24T19:41:50|01a0d334 / 80|ORIGINAL|reference-guided generation|Use case: ui-mockup. Redesign reference 1 into a complete portrait mobile GAME UI concept |
|[IG014](prompts/IG014.md)|2026-09-24T19:42:35|01a0d334 / 81|ORIGINAL|reference-guided generation|Use case: ui-mockup. Transform reference 1 boring dispatch form into complete portrait mob|
|[IG015](prompts/IG015.md)|2026-09-24T19:43:23|01a0d334 / 92|ORIGINAL|reference-guided generation|Use case: ui-mockup. Transform first reference screenshot into premium cute hand-painted m|
|[IG016](prompts/IG016.md)|2026-09-24T19:45:01|01a0d334 / 104|ORIGINAL|reference-guided generation|Use case: ui-mockup. Paint a complete usable portrait phone GAME UI for 鸡宝厨房 regional expl|
|[IG017](prompts/IG017.md)|2026-09-24T19:45:52|01a0d334 / 105|ORIGINAL|image edit / style transfer|Use case: precise-object-edit / ui-mockup. Edit the attached existing mobile game kitchen |
|[IG018](prompts/IG018.md)|2026-09-24T19:51:39|01a0d334 / 160|ORIGINAL|reference-guided generation|VISUAL MOCKUP ONLY, not a production background or asset sheet. Produce one complete tall |
|[IG019](prompts/IG019.md)|2026-09-24T19:52:16|01a0d334 / 161|ORIGINAL|reference-guided generation|VISUAL MOCKUP ONLY, not a production background or asset sheet. Produce one complete tall |
|[IG020](prompts/IG020.md)|2026-09-24T19:53:06|01a0d334 / 162|ORIGINAL|reference-guided generation|VISUAL MOCKUP ONLY, not a production background or asset sheet. Produce one complete tall |
|[IG021](prompts/IG021.md)|2026-09-24T19:54:26|01a0d334 / 171|ORIGINAL|reference-guided generation|Precise localized image edit. Image1 is the complete approved-for-layout UI MOCKUP. Image2|
|[IG022](prompts/IG022.md)|2026-09-24T19:55:05|01a0d334 / 172|ORIGINAL|reference-guided generation|Precise localized image edit. Image1 is the complete simple collection UI mockup. Image2 i|
|[IG023](prompts/IG023.md)|2026-09-24T20:09:30|01a0d334 / 281|ORIGINAL|reference-guided generation|Production ASSET SHEET for a lightweight cute 2D game, NOT a mockup screenshot. Use refere|
|[IG024](prompts/IG024.md)|2026-09-24T20:10:16|01a0d334 / 282|ORIGINAL|reference-guided generation|Production ASSET SHEET for a lightweight cute 2D game, NOT a mockup screenshot. Use refere|
|[IG025](prompts/IG025.md)|2026-09-24T20:34:55|01a0d334 / 535|ORIGINAL|reference-guided generation|Production game asset sheet, NOT a mockup. Reference image is the approved UI; use ONLY it|
|[IG026](prompts/IG026.md)|2026-09-24T20:36:17|01a0d334 / 542|ORIGINAL|reference-guided generation|Production raster ASSET SHEET for a warm hand-painted cute chicken collecting game, using |
|[IG027](prompts/IG027.md)|2026-09-24T20:37:54|01a0d334 / 549|ORIGINAL|reference-guided generation|Make a GAME ASSET SHEET, never a screen. Attached reference is approved style truth: warm |
|[IG028](prompts/IG028.md)|2026-09-24T22:41:56|01a0d334 / 900|ORIGINAL|reference-guided generation|Game production ASSET SHEET, transparent RGBA, 2 columns x 2 rows, four isolated objects. |
|[IG029](prompts/IG029.md)|2026-09-24T22:46:46|01a0d334 / 909|ORIGINAL|reference-guided generation|Production game signage ASSET SHEET, transparent RGBA, 3 columns x2 rows, six isolated bla|
|[IG030](prompts/IG030.md)|2026-09-24T22:53:19|01a0d334 / 918|ORIGINAL|reference-guided generation|Production tiny shop-prop ASSET SHEET for cute warm hand-painted game. Attached business m|
|[IG031](prompts/IG031.md)|2026-09-25T00:03:05|01a0d334 / 1253|ORIGINAL|reference-guided generation|Create a production transparent RGBA Asset Sheet with SIX isolated BLANK game UI materials|
|[IG032](prompts/IG032.md)|2026-09-25T00:04:21|01a0d334 / 1258|ORIGINAL|reference-guided generation|Visual Mockup validation ONLY, not production art. Create one landscape image with TWO COM|
|[IG033](prompts/IG033.md)|2026-09-25T00:09:47|01a0d334 / 1279|ORIGINAL|image edit / style transfer|EDIT THIS ASSET SHEET. Preserve all six object shapes, positions, linework, and colors exa|
|[IG034](prompts/IG034.md)|2026-09-25T00:14:16|01a0d334 / 1299|ORIGINAL|reference-guided generation|Production transparent RGBA Asset Sheet, FOUR isolated game paper/book objects, 2x2 grid w|
|[IG035](prompts/IG035.md)|2026-09-25T00:32:57|01a0d334 / 1336|ORIGINAL|image edit / style transfer|Edit this production asset sheet following UI usability feedback. Preserve positions, over|
|[IG036](prompts/IG036.md)|2026-09-25T09:07:03|01a0d334 / 1647|ORIGINAL|reference-guided generation|Create a simplified two-panel visual MOCKUP, not production art, for this exact warm hand-|
|[IG037](prompts/IG037.md)|2026-09-25T09:08:53|01a0d334 / 1679|ORIGINAL|reference-guided generation|PRODUCTION GAME ASSET SHEET, transparent RGBA background, no page UI. Match supplied warm |
|[IG038](prompts/IG038.md)|2026-09-25T09:11:58|01a0d334 / 1711|ORIGINAL|reference-guided generation|Production small STATE ICON Asset Sheet for warm hand-painted cute chicken kitchen UI, sam|
|[IG039](prompts/IG039.md)|2026-09-25T09:19:52|01a0d334 / 1790|ORIGINAL|image edit / style transfer|Revise this exact production asset sheet, preserve same art style and colors. Six SEPARATE|
|[IG040](prompts/IG040.md)|2026-09-25T09:50:13|01a0d334 / 2146|ORIGINAL|reference-guided generation|Use case: ui-mockup. Create ONE landscape presentation image with THREE complete portrait |
|[IG041](prompts/IG041.md)|2026-09-25T09:51:30|01a0d334 / 2161|ORIGINAL|reference-guided generation|Use case: stylized-concept. PRODUCTION ASSET SHEET, not a UI screenshot. Match exactly the|
|[IG042](prompts/IG042.md)|2026-09-25T09:53:11|01a0d334 / 2190|ORIGINAL|reference-guided generation|PRODUCTION ASSET SHEET J02, six isolated clickable place objects for same game. Match firs|
|[IG043](prompts/IG043.md)|2026-09-25T09:57:45|01a0d334 / 2221|ORIGINAL|reference-guided generation|PRODUCTION ASSET SHEET J03. Exactly SIXTEEN isolated small travel UI objects in a FOUR by |
|[IG044](prompts/IG044.md)|2026-09-25T10:01:15|01a0d334 / 2251|ORIGINAL|reference-guided generation|PRODUCTION material/specimen ASSET SHEET J04. Eight isolated botanical food icons, FOUR co|
|[IG045](prompts/IG045.md)|2026-09-25T10:03:58|01a0d334 / 2277|ORIGINAL|reference-guided generation|PRODUCTION STATIC GEOGRAPHY BACKGROUND, portrait 832x1024. Extract only the very sparse ge|
|[IG046](prompts/IG046.md)|2026-09-25T10:06:48|01a0d334 / 2322|ORIGINAL|image edit / style transfer|Precise-object-edit of this transparent production asset sheet. Preserve all eight positio|
|[IG047](prompts/IG047.md)|2026-09-26T09:15:49|01a0db3e / 149|ORIGINAL|reference-guided generation|Create one production ASSET SHEET extracted/recreated as faithfully as possible from the a|
|[IG048](prompts/IG048.md)|2026-09-26T09:17:16|01a0db3e / 156|ORIGINAL|reference-guided generation|Produce a single transparent production game Asset Sheet, 3 columns x 3 rows, nine isolate|
|[IG049](prompts/IG049.md)|2026-09-26T09:21:48|01a0db3e / 209|ORIGINAL|reference-guided generation|Extract/recreate ONLY the continuous blue river shape from the left map of this approved U|
|[IG050](prompts/IG050.md)|2026-09-26T09:48:58|01a0db3e / 424|ORIGINAL|image edit / style transfer|Precise background-extraction edit of the supplied 2x2 production UI asset sheet. Preserve|
|[IG051](prompts/IG051.md)|2026-09-26T11:44:55|01a0db3e / 845|ORIGINAL|reference-guided generation|Create a production asset sheet from the attached approved game UI reference, focusing ONL|
|[IG052](prompts/IG052.md)|2026-09-27T13:16:26|01a0e0d9 / 781|ORIGINAL|reference-guided generation|Use case: ui-mockup environment reference only. Create a new portrait mobile game FARM COU|
|[IG053](prompts/IG053.md)|2026-09-27T13:42:43|01a0e0d9 / 922|ORIGINAL|reference-guided generation|Use case: ui-mockup. Asset type: FIVE independent painted kitchen interaction props arrang|
|[IG054](prompts/IG054.md)|2026-09-27T14:50:29|01a0e199 / 114|ORIGINAL|reference-guided generation|Use case: ui-mockup. Create a FINISHED MOBILE GAME CONCEPT PRESENTATION, not a runtime cha|
|[IG055](prompts/IG055.md)|2026-09-27T14:52:37|01a0e199 / 123|ORIGINAL|reference-guided generation|Use case: ui-mockup. Create a FINISHED MOBILE GAME CONCEPT PRESENTATION, not a runtime cha|
|[IG056](prompts/IG056.md)|2026-09-27T14:54:26|01a0e199 / 132|ORIGINAL|reference-guided generation|Use case: ui-mockup. Create a FINISHED MOBILE GAME CONCEPT PRESENTATION, not a runtime cha|
|[IG057](prompts/IG057.md)|2026-09-27T14:57:48|01a0e199 / 147|ORIGINAL|reference-guided generation|Use case: ui-mockup. Create a FINISHED MOBILE GAME CONCEPT PRESENTATION, not a runtime cha|
|[IG058](prompts/IG058.md)|2026-09-27T15:37:37|01a0e199 / 230|ORIGINAL|reference-guided generation|Use case: style-transfer / mobile game ui-mockup. This is a CONTROLLED STYLE TEST for 鸡宝厨房|
|[IG059](prompts/IG059.md)|2026-09-27T15:39:31|01a0e199 / 237|ORIGINAL|reference-guided generation|Use case: style-transfer / mobile game ui-mockup. This is a CONTROLLED STYLE TEST for 鸡宝厨房|
|[IG060](prompts/IG060.md)|2026-09-27T15:42:39|01a0e199 / 248|ORIGINAL|reference-guided generation|Use case: style-transfer / mobile game ui-mockup. This is a CONTROLLED STYLE TEST for 鸡宝厨房|
|[IG061](prompts/IG061.md)|2026-09-27T15:49:32|01a0e199 / 262|ORIGINAL|reference-guided generation|Precise local image edit. Use attached image as immutable full-screen Lv.2 kitchen layout |
|[IG062](prompts/IG062.md)|2026-09-27T15:52:09|01a0e199 / 269|ORIGINAL|image edit / style transfer|LOCAL EDIT ONLY. Preserve the entire supplied image, every existing object, text, and EVER|
|[IG063](prompts/IG063.md)|2026-09-27T15:54:08|01a0e199 / 278|ORIGINAL|image edit / style transfer|STYLE TRANSFER EDIT, not regeneration. Image 1 is the EXACT LOCKED TARGET screen, now cont|
|[IG064](prompts/IG064.md)|2026-09-27T15:57:11|01a0e199 / 285|ORIGINAL|image edit / style transfer|STYLE TRANSFER EDIT for VERSION C — UI-FIRST GAME SCENE. Image 1 is the LOCKED B layout an|
|[IG065](prompts/IG065.md)|2026-09-27T20:10:49|01a0e199 / 592|ORIGINAL|reference-guided generation|Use case: stylized-concept. Production sprite for the EXISTING mobile idle game 鸡宝厨房. Gene|
|[IG066](prompts/IG066.md)|2026-09-27T20:11:43|01a0e199 / 593|ORIGINAL|reference-guided generation|Use case: stylized-concept. Production sprite for the EXISTING mobile idle game 鸡宝厨房. Gene|
|[IG067](prompts/IG067.md)|2026-09-27T20:15:17|01a0e199 / 606|ORIGINAL|reference-guided generation|Use case: stylized-concept. Production sprite for the EXISTING mobile idle game 鸡宝厨房. Gene|
|[IG068](prompts/IG068.md)|2026-09-27T20:16:15|01a0e199 / 607|ORIGINAL|reference-guided generation|Use case: stylized-concept. Production sprite for the EXISTING mobile idle game 鸡宝厨房. Gene|
|[IG069](prompts/IG069.md)|2026-09-27T20:17:08|01a0e199 / 610|ORIGINAL|reference-guided generation|Use case: stylized-concept. Production sprite for the EXISTING mobile idle game 鸡宝厨房. Gene|
|[IG070](prompts/IG070.md)|2026-09-27T20:18:11|01a0e199 / 617|ORIGINAL|reference-guided generation|Use case: stylized-concept. Production sprite for the EXISTING mobile idle game 鸡宝厨房. Gene|
|[IG071](prompts/IG071.md)|2026-09-27T20:18:53|01a0e199 / 618|ORIGINAL|reference-guided generation|Use case: stylized-concept. Production sprite for the EXISTING mobile idle game 鸡宝厨房. Gene|
|[IG072](prompts/IG072.md)|2026-09-27T20:19:39|01a0e199 / 619|ORIGINAL|reference-guided generation|Use case: stylized-concept. Production sprite for the EXISTING mobile idle game 鸡宝厨房. Gene|
|[IG073](prompts/IG073.md)|2026-09-27T20:23:28|01a0e199 / 631|ORIGINAL|image edit / style transfer|Precise object edit. Image1 is an edit target, an existing isolated game cleaning brush; i|
|[IG074](prompts/IG074.md)|2026-09-27T20:26:45|01a0e199 / 648|ORIGINAL|reference-guided generation|Use case: stylized-concept. Production sprite for the EXISTING mobile idle game 鸡宝厨房. Gene|
|[IG075](prompts/IG075.md)|2026-09-27T21:15:56|01a0e2fa / 117|ORIGINAL|image edit / style transfer|Use case: style-transfer, precise-object-edit. Asset type: production mobile game LEVEL 2 |
|[IG076](prompts/IG076.md)|2026-09-27T22:52:15|01a0e2fa / 347|ORIGINAL|reference-guided generation|Create ONE new portrait BACKGROUND / SCENE BASE for level 2 of the Chinese mobile game Chi|
|[IG077](prompts/IG077.md)|2026-09-27T22:52:55|01a0e2fa / 350|ORIGINAL|reference-guided generation|Create ONE new portrait BACKGROUND / SCENE BASE for level 2 of the Chinese mobile game Chi|
|[IG078](prompts/IG078.md)|2026-09-27T22:53:39|01a0e2fa / 351|ORIGINAL|reference-guided generation|Create ONE new portrait BACKGROUND / SCENE BASE for level 2 of the Chinese mobile game Chi|
|[IG079](prompts/IG079.md)|2026-09-27T22:54:21|01a0e2fa / 360|ORIGINAL|reference-guided generation|Create ONE new portrait BACKGROUND / SCENE BASE for level 2 of the Chinese mobile game Chi|
|[IG080](prompts/IG080.md)|2026-09-27T22:57:51|01a0e2fa / 384|ORIGINAL|reference-guided generation|Create ONE new portrait BACKGROUND / SCENE BASE for level 2 of the Chinese mobile game Chi|
