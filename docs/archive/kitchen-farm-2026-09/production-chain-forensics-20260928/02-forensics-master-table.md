# Forensics Master Table

所有成功与未通过案例使用同一12项模板。日期均+08。IG编号可跳转完整真实Prompt；U/W引用含会话前缀和ordinal，完整ID见evidence/threads.json。视觉结论是本轮判断，不替用户重新选稿。

|ID|案例|阶段|Gate|
|---|---|---|---|
|[S01](#s01)|图鉴 / 收藏 Golden Sample|09-24|用户明确通过；不等于所有首次生成都通过。|
|[S02](#s02)|生意营业主页面 Golden Sample|09-24—25|用户明确通过。|
|[S03](#s03)|寻访：地图、地区、归来|09-24—26|最终认可有后续用户回顾支持；不能把 09-25 10:36 的通过当成未经撤回的最终 Runtime Gate，也未找到09-26单独终验“通过”句。|
|[S04](#s04)|生意视觉族：订单/常客/项目/账单|09-25|用户明确通过；作为额外成功样本。|
|[K00](#k00)|09-19 厨房背景 A/B/C/D、B1/B2、墙平台、原图轻修|09-19|D 只有相对偏好；B1/B2及过度简化方向被否，未找到最终整页通过。|
|[K01](#k01)|Work 2.5 Kitchen 场景入口与四级 Mockup|09-27 13时|未通过/未获明确确认；不得把 internal review 当用户 Gate。|
|[F00](#f00)|Work 2.5 Farm 紧凑庭院|09-27 13时|无明确用户通过；只能列未过 Gate，不能伪造一次具体驳回。|
|[K02](#k02)|完整 Kitchen AI Concept：A/B/C × 四级|09-27 14:42—15:24|整组被用户否定；没有A/B/C任选一通过。|
|[K03](#k03)|Style Test A — Environment Illustration|09-27 15:24—16:08|全组未通过；A本来就是负向对照，不能说它未实现测试任务。|
|[K04](#k04)|Style Test B — Game Asset Composition|09-27 15:24—16:08|全组未通过；A本来就是负向对照，不能说它未实现测试任务。|
|[K05](#k05)|Style Test C — UI-first Game Scene|09-27 15:24—16:08|全组未通过；A本来就是负向对照，不能说它未实现测试任务。|
|[K06](#k06)|A/B/C 灰盒 Layout Prototype|09-27 16:08—19:38|U-Chat传入否定，不采用三选一；不是因灰盒“没画完”就判失败。|
|[K07](#k07)|V1/V2 Visual Prototype 提议后中止|09-27 19:38—19:48|中止/改题，不能列为两套已生成失败方案。|
|[K08](#k08)|Visual Forensics（诊断通过）|09-27 19:48—20:03|用户明确通过的是分析，不是厨房成图。|
|[K09](#k09)|Asset Language Gate — 蛋窝 A/B|09-27 20:03—20:35|内部PASS；用户Gate未恢复到明确通过；不可作为完成厨房。|
|[K10](#k10)|Asset Language Gate — 调味 A/B|09-27 20:03—20:35|内部PASS；用户Gate未恢复到明确通过；不可作为完成厨房。|
|[K11](#k11)|Asset Language Gate — 清洁 A/B|09-27 20:03—20:35|内部PASS；用户Gate未恢复到明确通过；不可作为完成厨房。|
|[K12](#k12)|Asset Language Gate — 仓库 A/B|09-27 20:03—20:35|内部PASS；用户Gate未恢复到明确通过；不可作为完成厨房。|
|[K13](#k13)|Lv.2 Before / After Remaster|09-27 21:07—22:45|用户明确不通过；内部交互/视觉PASS不能覆盖它。|
|[K14](#k14)|Background Direction A — Original Remaster|09-27 22:45—23:10|整组未获认可；没有逐张排名/逐张用户拒绝记录。|
|[K15](#k15)|Background Direction B — Game Scene|09-27 22:45—23:10|整组未获认可；没有逐张排名/逐张用户拒绝记录。|
|[K16](#k16)|Background Direction C — Storybook|09-27 22:45—23:10|整组未获认可；没有逐张排名/逐张用户拒绝记录。|
|[K17](#k17)|Background Direction D — Stylized|09-27 22:45—23:10|整组未获认可；没有逐张排名/逐张用户拒绝记录。|
|[K18](#k18)|Background 统一 Overlay|09-27 23时|完成“占位验证”不等于完整页面Gate。|
|[K19](#k19)|K1 Architecture Prototype|09-28 00:47后|结构原型未获最终用户通过；无权把机器验证通过当体验改善。|
|[K20](#k20)|K3 Architecture Prototype|09-28 00:47后|结构原型未获最终用户通过；无权把机器验证通过当体验改善。|
|[F01](#f01)|F1 Farm Architecture Prototype|09-28 00:47后|未通过最终Gate/待定；不能具体编造“用户因草地空而否F1”等原话。|
|[F02](#f02)|F3 Farm Architecture Prototype|09-28 00:47后|未通过最终Gate/待定；不能具体编造“用户因草地空而否F1”等原话。|
|[X01](#x01)|Work 2 Remaining UI Remaster（补充边界样本）|09-27 11—12时|工程阶段完成有用户记录；不足以当每页审美通过，不加入核心成功对照结论。|

<a id="s01"></a>

## S01 · 图鉴 / 收藏 Golden Sample
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0d334 · ordinal 136](evidence/01a0d334-messages.md)：已有角色为主体，以纸张、胶带、印章组成收藏册；[U/W 01a0d334 · ordinal 215](evidence/01a0d334-messages.md)：先做图鉴 Golden Sample。|
|2. Work 的公开理解|从完整绘本改为角色贴纸＋纸页组件；随后把 Mockup 当视觉真值。|
|3. 计划步骤|简化 Mockup → 规范 → 素材表 → 切分 → 动态 Runtime → 并排修正。|
|4. 实际顺序|三页初稿后生成简化收藏图；局部纠正鸭宝；G01/G02 首轮组件接入；用户指出白边/纸张/字体/未知槽差距，再生成 G03–G05 并修角色视觉边界、底栏，最后修两处细节。|
|5. 工具|内置 ImageGen；Python 切分/图像边界处理；HTML/CSS/JS；浏览器截图与尺寸验证。|
|6. ImageGen Prompt / 分级 / 产物类型|[IG015](prompts/IG015.md)、[IG020](prompts/IG020.md)、[IG022](prompts/IG022.md)、[IG023](prompts/IG023.md)、[IG024](prompts/IG024.md)、[IG025](prompts/IG025.md)、[IG026](prompts/IG026.md)、[IG027](prompts/IG027.md)；ORIGINAL。整页 Mockup 与 asset sheet 分开。|
|7. 输入参考|原页面截图、原厨房视觉、现有鸡宝/鸭宝；简化稿成为后续纸张/印章 style truth。准确路径在调用内。|
|8. 多轮 edit|有：简化稿鸭宝局部 edit；组件重新生成与程序调参。不是一张图一次完成。|
|9. 中间产物|[mockup-before-after.png](../../../../artifacts/golden-collection-v2/mockup-before-after.png)|
|10. 最终产物|[browser-390x844.png](../../../../artifacts/golden-collection-v2/browser-390x844.png)；[manifest.json](../../../../web/art/golden-collection/manifest.json)|
|11. 用户实际反馈|[U/W 01a0d334 · ordinal 504](evidence/01a0d334-messages.md)：方向正确但视觉差异明显；[U/W 01a0d334 · ordinal 861](evidence/01a0d334-messages.md)：“当前图鉴 Golden Sample 已通过”。|
|12. 用户 Gate|用户明确通过；不等于所有首次生成都通过。|

**结果层（同10维）：** 结构=角色/未知槽/印章/翻页；构图=正面纸页；颜色=奶油纸＋灰绿；画风=手绘贴纸；造型=保留角色轮廓；描边=轮廓白边与棕线；光照=短边影；材质=纸纹局部；空间=叠层；功能/装饰=纸、章直接承担状态。


<a id="s02"></a>

## S02 · 生意营业主页面 Golden Sample
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0d334 · ordinal 136](evidence/01a0d334-messages.md)：角色坐货篮/托盘；[U/W 01a0d334 · ordinal 861](evidence/01a0d334-messages.md)：制作第二个 Golden Sample，不机械复用图鉴白边。|
|2. Work 的公开理解|把实际售卖角色作为主画面；容器为后层/前沿、数量独立。|
|3. 计划步骤|确认简化稿 → B01–B03 → 分层动态角色 → 真实状态 → 对照。|
|4. 实际顺序|初稿小店场景被要求简化；生成三角色货篮稿并局部纠正鸡宝；生成承托、招牌、小物件 sheet；实现后用户继续点出细节，账单位置/框体/原图内容持续收敛。|
|5. 工具|内置 ImageGen；Python 切分；manifest；DOM/CSS；浏览器。|
|6. ImageGen Prompt / 分级 / 产物类型|[IG013](prompts/IG013.md)、[IG018](prompts/IG018.md)、[IG021](prompts/IG021.md)、[IG028](prompts/IG028.md)、[IG029](prompts/IG029.md)、[IG030](prompts/IG030.md)；ORIGINAL。整页用于概念，生产用空篮/空牌。|
|7. 输入参考|当前生意截图＋鸡宝、香煎鸡、鸭宝原 PNG；后续已确认 business mockup。|
|8. 多轮 edit|有：IG021 定点角色纠正；组件与 Runtime 修订，最后 r3。|
|9. 中间产物|[mockup-vs-runtime.png](../../../../artifacts/golden-business/mockup-vs-runtime.png) → [mockup-vs-runtime.png](../../../../artifacts/golden-business-r2/mockup-vs-runtime.png)|
|10. 最终产物|[active-390x844.png](../../../../artifacts/golden-business-r3/active-390x844.png)；[manifest.json](../../../../web/art/golden-business/manifest.json)|
|11. 用户实际反馈|[U/W 01a0d334 · ordinal 1602](evidence/01a0d334-messages.md)：“当前营业主页面 Golden Sample 已通过”；ordinal 1506 曾要求复用原图、缩小/上移查看账单。|
|12. 用户 Gate|用户明确通过。|

**结果层（同10维）：** 结构=营业/货品/已售/账单；构图=两上一下三大角色＋薄柜台；颜色=奶油底/蜜黄；画风=原角色＋手绘器皿；造型=角色大于容器；描边=角色原轮廓；光照=篮下短影；材质=编织集中；空间=篮后—角色—前沿；功能/装饰=货篮就是货品对象。


<a id="s03"></a>

## S03 · 寻访：地图、地区、归来
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0d334 · ordinal 136](evidence/01a0d334-messages.md)：地图可完整但只突出四区、路线、队伍；[U/W 01a0d334 · ordinal 2100](evidence/01a0d334-messages.md)：地图→地区→地点→伙伴→出发→归来。|
|2. Work 的公开理解|地图是主体；地标、路线、队伍、状态分离；后续用户把构图/字重/位置作为明确复刻目标。|
|3. 计划步骤|三屏 Mockup → 同类组件/地图底 → Runtime → 视觉差异收敛 → 高精度提取/排版。|
|4. 实际顺序|先简化世界图；后生成三屏主稿、J01–J04及地形；首轮 Runtime 地图压缩、层级偏平；多轮修正仍有差距；09-26另会话从确认原稿提取 J09/J11 地标/河流，生成补充 J06/J07/J08/J10/J12，规范边界与字体。|
|5. 工具|ImageGen；Python 语义分割/去底/归一化；原图组件提取；HTML/CSS/SVG包装；浏览器截图、overlay、几何比较。|
|6. ImageGen Prompt / 分级 / 产物类型|[IG014](prompts/IG014.md)、[IG016](prompts/IG016.md)、[IG019](prompts/IG019.md)、[IG040](prompts/IG040.md)、[IG041](prompts/IG041.md)、[IG042](prompts/IG042.md)、[IG043](prompts/IG043.md)、[IG044](prompts/IG044.md)、[IG045](prompts/IG045.md)、[IG046](prompts/IG046.md)、[IG047](prompts/IG047.md)、[IG048](prompts/IG048.md)、[IG049](prompts/IG049.md)、[IG050](prompts/IG050.md)、[IG051](prompts/IG051.md)；ORIGINAL。不能以旧来源文档“摘要”断言完整 Prompt 缺失。|
|7. 输入参考|原寻访页面＋已有角色；生意视觉；已确认 journey mockup。精确阶段直接使用用户指定的 journey-round2 三组左右对照。|
|8. 多轮 edit|有；新 sheet、局部 edit、原稿提取与 CSS 重排并存。最终河流/主要地标并非全部来自最后一次生成。|
|9. 中间产物|[map-ab.png](../../../../artifacts/journey-round2/map-ab.png)；[PRECISION-PROVENANCE.md](../../../journey-visual-20260925/asset-sheets/PRECISION-PROVENANCE.md)|
|10. 最终产物|[map-comparison.png](../../../../artifacts/journey-precision/final/map-comparison.png)；[manifest.json](../../../../web/art/golden-journey/manifest.json)|
|11. 用户实际反馈|[U/W 01a0d334 · ordinal 2653](evidence/01a0d334-messages.md)曾通过，[U/W 01a0d334 · ordinal 2710](evidence/01a0d334-messages.md)随即明确“Runtime 暂不通过”；[U/W 01a0db3e · ordinal 823](evidence/01a0db3e-messages.md)继续改字重/计时质感。09-27/本轮用户把最终寻访列为认可页面。|
|12. 用户 Gate|最终认可有后续用户回顾支持；不能把 09-25 10:36 的通过当成未经撤回的最终 Runtime Gate，也未找到09-26单独终验“通过”句。|

**结果层（同10维）：** 结构=地点—路线—队伍—行动；构图=大地图到近底栏；颜色=奶油/灰绿/浅蓝；画风=节点式手绘；造型=少数特征识别四区；描边=节点重于地形；光照=局部；材质=茅草木纹限定节点；空间=各节点局部透视；功能/装饰=河流/地标支持导航，不追求可信农场。


<a id="s04"></a>

## S04 · 生意视觉族：订单/常客/项目/账单
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0d334 · ordinal 1602](evidence/01a0d334-messages.md)：主屏已过，完整收口视觉族，复杂规则进帮助。|
|2. Work 的公开理解|共用语言，按各子系统语义分别用剪贴板、册子、筹备纸和账单。|
|3. 计划步骤|两屏验证与 asset sheet；切分；真实数据；整族检查。|
|4. 实际顺序|订单/常客生成双屏稿和空白纸本；边框太厚后 edit；去光晕；用户要求直接复用原图内容；项目/账单双屏，生产 sheet 修订，真实状态截图。|
|5. 工具|ImageGen、分割/归一化、HTML/CSS、浏览器。|
|6. ImageGen Prompt / 分级 / 产物类型|[IG031](prompts/IG031.md)、[IG032](prompts/IG032.md)、[IG033](prompts/IG033.md)、[IG034](prompts/IG034.md)、[IG035](prompts/IG035.md)、[IG036](prompts/IG036.md)、[IG037](prompts/IG037.md)、[IG038](prompts/IG038.md)、[IG039](prompts/IG039.md)；ORIGINAL。|
|7. 输入参考|已通过营业页面与此前纸张/小物件，具体参考见原始调用。|
|8. 多轮 edit|有：去大面积阴影、缩窄边框、sheet 修订。|
|9. 中间产物|[subpages-runtime.png](../../../../artifacts/golden-business-r2/subpages-runtime.png)|
|10. 最终产物|[REVIEW.md](../../../business-family-20260925/REVIEW.md)|
|11. 用户实际反馈|[U/W 01a0d334 · ordinal 2100](evidence/01a0d334-messages.md)：“当前「生意」模块视觉重构通过，先冻结”。|
|12. 用户 Gate|用户明确通过；作为额外成功样本。|

**结果层（同10维）：** 结构=任务对象/交付/进度；构图=单一物件隐喻；颜色=延续小店；画风=手绘纸本；造型=剪贴板/账簿有不同语义；描边=缩窄框；光照=去光晕；材质=纸木有限；空间=正面信息层；功能/装饰=纸本承载真实订单与结算。


<a id="k00"></a>

## K00 · 09-19 厨房背景 A/B/C/D、B1/B2、墙平台、原图轻修
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0b8de · ordinal 9](evidence/01a0b8de-messages.md)：厨房 AI 味重，生成候选；随后多次纠正身份与结构。|
|2. Work 的公开理解|先把问题理解为木纹、光影、画法；再试平台/墙面身份；最终回到原图轻修。|
|3. 计划步骤|四画法候选 → 布局修订 → 阶段平台 → 原图 touchup。|
|4. 实际顺序|A/B/C 同参考生成，D 原图改绘；反馈后 B1/B2；用户纠正木房太豪华与墙丢失；再按残破→破旧→正常→豪华生成平台套图，最后原图轻修。|
|5. 工具|内置 ImageGen、文件复制、图片查看。|
|6. ImageGen Prompt / 分级 / 产物类型|[IG001](prompts/IG001.md)、[IG002](prompts/IG002.md)、[IG003](prompts/IG003.md)、[IG004](prompts/IG004.md)、[IG005](prompts/IG005.md)、[IG006](prompts/IG006.md)、[IG007](prompts/IG007.md)、[IG008](prompts/IG008.md)、[IG009](prompts/IG009.md)、[IG010](prompts/IG010.md)、[IG011](prompts/IG011.md)；ORIGINAL；不含 IG012 应用图标。|
|7. 输入参考|v7 木房＋原作 Tool0 背景；后续以生成平台为基础扩阶段；最终原作单底图。|
|8. 多轮 edit|有多轮；一部分是不同参考重新生成，一部分以前图派生，不可统称全为连续 edit。|
|9. 中间产物|[layout-revision-notes.md](../../../../artifacts/art-exploration/kitchen-style-20260919/layout-revision-notes.md)|
|10. 最终产物|[original-touchup-notes.md](../../../../artifacts/art-exploration/kitchen-style-20260919/original-touchup-notes.md)|
|11. 用户实际反馈|[U/W 01a0b8de · ordinal 107](evidence/01a0b8de-messages.md)：“D更好一点”；151：“木板房不需要这么豪华”；227：“连墙都没有了”。|
|12. 用户 Gate|D 只有相对偏好；B1/B2及过度简化方向被否，未找到最终整页通过。|

**结果层（同10维）：** 结构=背景板；构图=墙/平台注册；颜色=暖木到淡彩；画风=原作/现代卡通对照；造型=墙被削弱；描边=尝试简化；光照=仍有渐变；材质=木纹减少；空间=厨房身份与桌面身份反复；功能/装饰=缺整页检验。视觉细项主要来自当时文档，未把未逐张查看的早期图当本轮独立视觉判定。


<a id="k01"></a>

## K01 · Work 2.5 Kitchen 场景入口与四级 Mockup
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e0d9 · ordinal 642](evidence/01a0e0d9-messages.md)及[work-2.5-user-original.txt](evidence/work-2.5-user-original.txt)：修调味/清洁对齐，重新审视场景与页面。|
|2. Work 的公开理解|将入口做成独立物件；保留四级背景和厨具，以独立 Mockup Gate 提交。|
|3. 计划步骤|场景审计→局部 asset sheet→透明分割→HTML Mockup→四级检查。|
|4. 实际顺序|生成五件厨房物件表；切分后放入旧背景；制作调味/清洁/仓库 overlay；四级和小屏截图。|
|5. 工具|内置 ImageGen、组件切分、独立 HTML、浏览器。|
|6. ImageGen Prompt / 分级 / 产物类型|[IG053](prompts/IG053.md)；ORIGINAL，五件 asset sheet，非整页。|
|7. 输入参考|现有厨房及正式图形；具体数组见 IG053 调用。|
|8. 多轮 edit|物件表一次可见生成；后续为组合与尺寸调整。|
|9. 中间产物|[four-kitchen-levels.png](../../../../artifacts/scene-first-review/four-kitchen-levels.png)|
|10. 最终产物|[main-scenes.png](../../../../artifacts/scene-first-review/main-scenes.png)|
|11. 用户实际反馈|随后用户另启全厨房 Concept；没有找到本候选被明确批准。|
|12. 用户 Gate|未通过/未获明确确认；不得把 internal review 当用户 Gate。|

**结果层（同10维）：** 结构=上部功能对象/下部厨具；构图=原背景＋入口带；颜色=蜜黄木房；画风=新道具更细；造型=箱册篮具象；描边=与旧蛋不同密度；光照=局部新物件体积；材质=编织/木箱；空间=道具叠旧窗；功能/装饰=标签仍突出。图中蛋已呈规则三行，是另一个可见回退样本。


<a id="f00"></a>

## F00 · Work 2.5 Farm 紧凑庭院
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e0d9 · ordinal 642](evidence/01a0e0d9-messages.md)及原粘贴附件：从现有玩法出发做场景入口。|
|2. Work 的公开理解|把横向农场重组为竖屏庭院，建筑对应收成、商店、神社、展示和整修。|
|3. 计划步骤|现有 Farm 参考→新无字背景→叠旧角色与入口→Mockup Gate。|
|4. 实际顺序|IG052 生成完整庭院环境参考；HTML 叠基本角色、建筑标签、功能 overlay；未接正式横向/昼夜 Runtime。|
|5. 工具|内置 ImageGen；HTML/CSS；浏览器截图。|
|6. ImageGen Prompt / 分级 / 产物类型|[IG052](prompts/IG052.md)；ORIGINAL。完整背景，不含 UI/角色；后由 HTML 组合。|
|7. 输入参考|旧 Farm painting；Prompt 明确给收成屋、摊位、神社、空架、修理箱位置，还要求小雏菊与石水盆。|
|8. 多轮 edit|没有找到本背景的多轮 edit；存在组合检查。|
|9. 中间产物|[farm-environment-reference.png](../../../../artifacts/scene-first-review/farm-environment-reference.png)|
|10. 最终产物|[farm.png](../../../../artifacts/scene-first-review/farm.png)|
|11. 用户实际反馈|[review-notes.md](../../../../artifacts/scene-first-review/review-notes.md)自称 awaiting user confirmation；本轮用户仍把 Farm 列未解决。|
|12. 用户 Gate|无明确用户通过；只能列未过 Gate，不能伪造一次具体驳回。|

**结果层（同10维）：** 结构=建筑热点；构图=中部草地＋四周建筑；颜色=黄绿/暖白/蓝山；画风=饱满庭院插画；造型=具象房屋；描边=建筑显著；光照=连续晴天环境；材质=草花铺满；空间=庭院透视；功能/装饰=背景先承担大量环境信息，UI依赖后叠。


<a id="k02"></a>

## K02 · 完整 Kitchen AI Concept：A/B/C × 四级
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e199 · ordinal 9](evidence/01a0e199-messages.md)：三套真正不同方向×四级，当前Runtime主要参考，等待选稿，不实现。|
|2. Work 的公开理解|A=圆形照料桌；B=L形台面；C=壁龛＋推车；把功能映射为家具/站点。|
|3. 计划步骤|查看真实四级与成功页→统一参考→生成三张四联图→裁单屏→用户Gate。|
|4. 实际顺序|真实截图与厨具参考准备；A/B/C生成，C标签/当前厨具语义修订后重生成；12张单屏从板图裁取。|
|5. 工具|内置 ImageGen；浏览器捕获；脚本裁切/尺寸规范化。|
|6. ImageGen Prompt / 分级 / 产物类型|[IG054](prompts/IG054.md)、[IG055](prompts/IG055.md)、[IG056](prompts/IG056.md)、[IG057](prompts/IG057.md)；ORIGINAL。要求完整四屏，非可运行UI。|
|7. 输入参考|四级 Runtime contact sheet 为主；生意、寻访、图鉴仅 style；厨具为身份。三套同5图。|
|8. 多轮 edit|C再次生成使用同参考；不是证据支持的A→B→C连续edit。A还有未产图的编排重试，不计成图。|
|9. 中间产物|[Kitchen-Concept-A-Lv1-Lv4.png](../../../../artifacts/kitchen-concept-remaster-20260927/concepts/Kitchen-Concept-A-Lv1-Lv4.png)|
|10. 最终产物|[Kitchen-ABC-4Levels-Overview.png](../../../../artifacts/kitchen-concept-remaster-20260927/concepts/Kitchen-ABC-4Levels-Overview.png)|
|11. 用户实际反馈|[U/W 01a0e199 · ordinal 195](evidence/01a0e199-messages.md)：暂停四级；核心不是元素多，而是 Environment / Cozy Kitchen Illustration。|
|12. 用户 Gate|整组被用户否定；没有A/B/C任选一通过。|

**结果层（同10维）：** 结构=功能变家具标签；构图=A圆桌/B转角/C壁龛；颜色=木褐＋奶油＋鼠尾草；画风=室内插画；造型=桌腿橱柜窗座；描边=内部结构普遍强；光照=灯/窗/物体体积；材质=墙木编织地毯连续；空间=房间透视；功能/装饰=操作被真实家具关系组织。


<a id="k03"></a>

## K03 · Style Test A — Environment Illustration
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e199 · ordinal 195](evidence/01a0e199-messages.md)：同功能/布局/蛋数，只变视觉表达，成功页升为首要风格参考。|
|2. Work 的公开理解|保留环境绘制逻辑作为对照。|
|3. 计划步骤|先三路同布局测试；发现蛋数漂移后，采用锁定基底连续编辑保一致。|
|4. 实际顺序|先独立A草稿；蛋数不符后两次局部修蛋，最后A成为24蛋基底。|
|5. 工具|内置 ImageGen；图片查看；整屏规范化和比较。|
|6. ImageGen Prompt / 分级 / 产物类型|[IG058](prompts/IG058.md)、[IG061](prompts/IG061.md)、[IG062](prompts/IG062.md)；ORIGINAL。完整页面；最终A/B/C edit链可追。|
|7. 输入参考|初稿为成功三页、Runtime、上一轮A木房；最终B额外锁定A，最终C锁定B。所有参考路径见原始调用/定义。|
|8. 多轮 edit|共3张初稿＋A两次局部修正＋A→B→C，共7张完成事件；全组次数，不是本行各7次。|
|9. 中间产物|[draft-A.png](../../../../artifacts/kitchen-visual-language-lv2-20260927/draft-A.png)|
|10. 最终产物|[Lv2-Style-A-full.png](../../../../artifacts/kitchen-visual-language-lv2-20260927/Lv2-Style-A-full.png)|
|11. 用户实际反馈|[U/W 01a0e199 · ordinal 317](evidence/01a0e199-messages.md)：停止整页ImageGen，仍稳定产生cozy illustration。|
|12. 用户 Gate|全组未通过；A本来就是负向对照，不能说它未实现测试任务。|

**结果层（同10维）：** 结构=同一全屏固定入口；构图=中央圆桌/大窗；颜色=暖褐奶油绿色；画风=A — Environment Illustration；造型=锁定家具与蛋；描边=A细节多/B清晰/C均匀粗；光照=逐渐局部化；材质=逐渐减纹；空间=同房间关系；功能/装饰=连续木墙地板、圆桌腿、编织地毯，材质与阴影最密。


<a id="k04"></a>

## K04 · Style Test B — Game Asset Composition
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e199 · ordinal 195](evidence/01a0e199-messages.md)：同功能/布局/蛋数，只变视觉表达，成功页升为首要风格参考。|
|2. Work 的公开理解|物件像可拆Asset，保留局部浅体积。|
|3. 计划步骤|先三路同布局测试；发现蛋数漂移后，采用锁定基底连续编辑保一致。|
|4. 实际顺序|先独立B草稿；最终以修好蛋数的A作为锁定底稿转绘B。|
|5. 工具|内置 ImageGen；图片查看；整屏规范化和比较。|
|6. ImageGen Prompt / 分级 / 产物类型|[IG059](prompts/IG059.md)、[IG063](prompts/IG063.md)；ORIGINAL。完整页面；最终A/B/C edit链可追。|
|7. 输入参考|初稿为成功三页、Runtime、上一轮A木房；最终B额外锁定A，最终C锁定B。所有参考路径见原始调用/定义。|
|8. 多轮 edit|共3张初稿＋A两次局部修正＋A→B→C，共7张完成事件；全组次数，不是本行各7次。|
|9. 中间产物|[draft-B.png](../../../../artifacts/kitchen-visual-language-lv2-20260927/draft-B.png)|
|10. 最终产物|[Lv2-Style-B-full.png](../../../../artifacts/kitchen-visual-language-lv2-20260927/Lv2-Style-B-full.png)|
|11. 用户实际反馈|[U/W 01a0e199 · ordinal 317](evidence/01a0e199-messages.md)：停止整页ImageGen，仍稳定产生cozy illustration。|
|12. 用户 Gate|全组未通过；A本来就是负向对照，不能说它未实现测试任务。|

**结果层（同10维）：** 结构=同一全屏固定入口；构图=中央圆桌/大窗；颜色=暖褐奶油绿色；画风=B — Game Asset Composition；造型=锁定家具与蛋；描边=A细节多/B清晰/C均匀粗；光照=逐渐局部化；材质=逐渐减纹；空间=同房间关系；功能/装饰=色块与轮廓更独立，纹理减少，但窗帘、圆桌、地毯仍支配空间。


<a id="k05"></a>

## K05 · Style Test C — UI-first Game Scene
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e199 · ordinal 195](evidence/01a0e199-messages.md)：同功能/布局/蛋数，只变视觉表达，成功页升为首要风格参考。|
|2. Work 的公开理解|进一步符号化，让物件和UI承担主要信息。|
|3. 计划步骤|先三路同布局测试；发现蛋数漂移后，采用锁定基底连续编辑保一致。|
|4. 实际顺序|先独立C草稿；最终以B作为锁定目标转绘C。|
|5. 工具|内置 ImageGen；图片查看；整屏规范化和比较。|
|6. ImageGen Prompt / 分级 / 产物类型|[IG060](prompts/IG060.md)、[IG064](prompts/IG064.md)；ORIGINAL。完整页面；最终A/B/C edit链可追。|
|7. 输入参考|初稿为成功三页、Runtime、上一轮A木房；最终B额外锁定A，最终C锁定B。所有参考路径见原始调用/定义。|
|8. 多轮 edit|共3张初稿＋A两次局部修正＋A→B→C，共7张完成事件；全组次数，不是本行各7次。|
|9. 中间产物|[draft-C.png](../../../../artifacts/kitchen-visual-language-lv2-20260927/draft-C.png)|
|10. 最终产物|[Lv2-Style-C-full.png](../../../../artifacts/kitchen-visual-language-lv2-20260927/Lv2-Style-C-full.png)|
|11. 用户实际反馈|[U/W 01a0e199 · ordinal 317](evidence/01a0e199-messages.md)：停止整页ImageGen，仍稳定产生cozy illustration。|
|12. 用户 Gate|全组未通过；A本来就是负向对照，不能说它未实现测试任务。|

**结果层（同10维）：** 结构=同一全屏固定入口；构图=中央圆桌/大窗；颜色=暖褐奶油绿色；画风=C — UI-first Game Scene；造型=锁定家具与蛋；描边=A细节多/B清晰/C均匀粗；光照=逐渐局部化；材质=逐渐减纹；空间=同房间关系；功能/装饰=更平、更粗线、更少纹理；家具轮廓与地毯环纹依旧同权，并未成为另一套游戏构图。


<a id="k06"></a>

## K06 · A/B/C 灰盒 Layout Prototype
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e199 · ordinal 317](evidence/01a0e199-messages.md)：禁止ImageGen，旧素材/占位制作可运行布局。|
|2. Work 的公开理解|把布局作为单独变量：A围窝、B上备下收、C侧边厨具。|
|3. 计划步骤|旧素材+共用状态→3个布局→点击验证→390/320截图→选择Gate。|
|4. 实际顺序|搭建独立原型；换布局共用24蛋和同8素材；收取/调味/开批/打扫演示；次级入口占位。|
|5. 工具|HTML/CSS/JS、现有PNG、浏览器；没有ImageGen。|
|6. ImageGen Prompt / 分级 / 产物类型|N/A：无图像生成，不制造RECONSTRUCTED Prompt。|
|7. 输入参考|原蛋/窝/厨具/HUD；背景简化为占位色面。|
|8. 多轮 edit|无图像edit；程序布局调整。|
|9. 中间产物|[verification.json](../../../../artifacts/kitchen-layout-prototype-20260927/verification.json)|
|10. 最终产物|[Layout-ABC-Comparison.png](../../../../artifacts/kitchen-layout-prototype-20260927/Layout-ABC-Comparison.png)|
|11. 用户实际反馈|[U/W 01a0e199 · ordinal 444](evidence/01a0e199-messages.md)用户传入Chat判断：三套都不过度选择；A场景弱，B管理面板，C缩小蛋窝。|
|12. 用户 Gate|U-Chat传入否定，不采用三选一；不是因灰盒“没画完”就判失败。|

**结果层（同10维）：** 结构=A蛋窝/B准备/C侧栏；构图=功能块分配；颜色=纯米黄；画风=旧sprite＋UI；造型=主体原素材；描边=控件边框；光照=旧素材局部影；材质=环境缺席；空间=页面分区；功能/装饰=只有信息架构，整页游戏场景感不足。


<a id="k07"></a>

## K07 · V1/V2 Visual Prototype 提议后中止
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e199 · ordinal 444](evidence/01a0e199-messages.md)：回原厨房，做V1保守与V2适度重构。|
|2. Work 的公开理解|公开回复承诺 Runtime Layout＋独立Asset，不再整页生成。|
|3. 计划步骤|审查当前Runtime→组件表→两个组合原型。|
|4. 实际顺序|只恢复到准备与读文件；后续用户ordinal469立即改为Visual Forensics。未找到V1/V2完成图或生成事件。|
|5. 工具|只读检查；未发现本阶段完成ImageGen。|
|6. ImageGen Prompt / 分级 / 产物类型|N/A；未执行完成，不能补写提示词。|
|7. 输入参考|计划使用正式Runtime/旧素材；不是证明实际送入ImageGen。|
|8. 多轮 edit|无可证实多轮edit。|
|9. 中间产物|准备记录见同会话444—469。|
|10. 最终产物|未找到完成产物。|
|11. 用户实际反馈|[U/W 01a0e199 · ordinal 469](evidence/01a0e199-messages.md)：暂停Concept/Layout/Asset，先取证。|
|12. 用户 Gate|中止/改题，不能列为两套已生成失败方案。|

**结果层（同10维）：** 未产出可审完整图片；结构/构图/颜色/画风/造型/描边/光照/材质/空间/功能装饰均无完成证据，不作视觉评价。


<a id="k08"></a>

## K08 · Visual Forensics（诊断通过）
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e199 · ordinal 469](evidence/01a0e199-messages.md)：用成功页正例与厨房负例，不调用ImageGen。|
|2. Work 的公开理解|区分“画法细节”与“室内组织目标”，并回查当时提示词。|
|3. 计划步骤|逐项看图→读运行组件/Prompt→对照表→可执行检查项。|
|4. 实际顺序|看成功3页、独立素材、Concept与Style；公开指出圆桌/木墙是主动要求，地毯/桶被后续锁定。|
|5. 工具|读取图片、文件、源码与历史Prompt；无新图。|
|6. ImageGen Prompt / 分级 / 产物类型|N/A：分析任务；查阅既存ORIGINAL而非新生成。|
|7. 输入参考|正例生意/寻访/图鉴；负例四级Concept与Style A/B/C。|
|8. 多轮 edit|无。|
|9. 中间产物|[U/W 01a0e199 · ordinal 507](evidence/01a0e199-messages.md)与545公开中间发现。|
|10. 最终产物|[visual-forensics-source.md](../../../../artifacts/kitchen-remaster-lv2-20260927/visual-forensics-source.md)|
|11. 用户实际反馈|[U/W 01a0e199 · ordinal 558](evidence/01a0e199-messages.md)：“上一轮 Visual Forensics 通过”。|
|12. 用户 Gate|用户明确通过的是分析，不是厨房成图。|

**结果层（同10维）：** 覆盖结构、构图、色块、画风、造型、描边、光照、材质、空间、功能/装饰；关键反例是C已经平涂仍保留完整室内构图。


<a id="k09"></a>

## K09 · Asset Language Gate — 蛋窝 A/B
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e199 · ordinal 558](evidence/01a0e199-messages.md)：只测4类独立Asset、每类A/B，不能拼厨房。|
|2. Work 的公开理解|宽带藤篮 / 浅木托；空容器与24枚旧蛋程序分层|
|3. 计划步骤|正例参考→独立透明资产→内部筛选→浅/木底与Runtime尺寸→用户素材Gate。|
|4. 实际顺序|并发生成8件；检查手刷光晕、试edit、重生成；程序叠旧蛋；建立兼容性板和逐项Checklist。|
|5. 工具|内置 ImageGen；现有蛋叠放/alpha处理；HTML评审板；浏览器尺寸检查。|
|6. ImageGen Prompt / 分级 / 产物类型|[IG065](prompts/IG065.md)、[IG066](prompts/IG066.md)；ORIGINAL，单Asset，非完整背景/页面。|
|7. 输入参考|同5图：生意r3、journey-convergence地图、图鉴v2、basket-base、precision-ref-node-T。|
|8. 多轮 edit|全组10张完成图=8初稿+手刷2次修订；本候选实际见对应IG，不能把所有都记为10轮。|
|9. 中间产物|[final-generation-record.json](../../../../artifacts/kitchen-asset-language-gate-20260927/final-generation-record.json)|
|10. 最终产物|[nest-AB-Comparison.png](../../../../artifacts/kitchen-asset-language-gate-20260927/nest-AB-Comparison.png)|
|11. 用户实际反馈|蛋窝B后来放到真实Runtime会遮前排蛋，被Work淘汰；A也未采用。 [Checklist.md](../../../../artifacts/kitchen-asset-language-gate-20260927/Checklist.md)声明PASS不代表用户批准。|
|12. 用户 Gate|内部PASS；用户Gate未恢复到明确通过；不可作为完成厨房。|

**结果层（同10维）：** 结构=单物件；构图=孤立对象；颜色=暖木奶油绿；画风=既有游戏sprite；造型=宽带藤篮 / 浅木托；空容器与24枚旧蛋程序分层；描边=棕轮廓；光照=局部，手刷光晕被拒；材质=宽带/少量；空间=局部浅体积；功能/装饰=可识别入口，未验证整屏主次。


<a id="k10"></a>

## K10 · Asset Language Gate — 调味 A/B
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e199 · ordinal 558](evidence/01a0e199-messages.md)：只测4类独立Asset、每类A/B，不能拼厨房。|
|2. Work 的公开理解|三罐架 / 双罐提篮；图案识别配料|
|3. 计划步骤|正例参考→独立透明资产→内部筛选→浅/木底与Runtime尺寸→用户素材Gate。|
|4. 实际顺序|并发生成8件；检查手刷光晕、试edit、重生成；程序叠旧蛋；建立兼容性板和逐项Checklist。|
|5. 工具|内置 ImageGen；现有蛋叠放/alpha处理；HTML评审板；浏览器尺寸检查。|
|6. ImageGen Prompt / 分级 / 产物类型|[IG067](prompts/IG067.md)、[IG069](prompts/IG069.md)；ORIGINAL，单Asset，非完整背景/页面。|
|7. 输入参考|同5图：生意r3、journey-convergence地图、图鉴v2、basket-base、precision-ref-node-T。|
|8. 多轮 edit|全组10张完成图=8初稿+手刷2次修订；本候选实际见对应IG，不能把所有都记为10轮。|
|9. 中间产物|[final-generation-record.json](../../../../artifacts/kitchen-asset-language-gate-20260927/final-generation-record.json)|
|10. 最终产物|[seasoning-AB-Comparison.png](../../../../artifacts/kitchen-asset-language-gate-20260927/seasoning-AB-Comparison.png)|
|11. 用户实际反馈|后来Lv.2内部选A，但整页被用户否；没有用户单独通过A的证据。 [Checklist.md](../../../../artifacts/kitchen-asset-language-gate-20260927/Checklist.md)声明PASS不代表用户批准。|
|12. 用户 Gate|内部PASS；用户Gate未恢复到明确通过；不可作为完成厨房。|

**结果层（同10维）：** 结构=单物件；构图=孤立对象；颜色=暖木奶油绿；画风=既有游戏sprite；造型=三罐架 / 双罐提篮；图案识别配料；描边=棕轮廓；光照=局部，手刷光晕被拒；材质=宽带/少量；空间=局部浅体积；功能/装饰=可识别入口，未验证整屏主次。


<a id="k11"></a>

## K11 · Asset Language Gate — 清洁 A/B
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e199 · ordinal 558](evidence/01a0e199-messages.md)：只测4类独立Asset、每类A/B，不能拼厨房。|
|2. Work 的公开理解|扫把 / 手刷；手刷光晕初稿及局部edit均未被保留，重新生成|
|3. 计划步骤|正例参考→独立透明资产→内部筛选→浅/木底与Runtime尺寸→用户素材Gate。|
|4. 实际顺序|并发生成8件；检查手刷光晕、试edit、重生成；程序叠旧蛋；建立兼容性板和逐项Checklist。|
|5. 工具|内置 ImageGen；现有蛋叠放/alpha处理；HTML评审板；浏览器尺寸检查。|
|6. ImageGen Prompt / 分级 / 产物类型|[IG068](prompts/IG068.md)、[IG070](prompts/IG070.md)、[IG073](prompts/IG073.md)、[IG074](prompts/IG074.md)；ORIGINAL，单Asset，非完整背景/页面。|
|7. 输入参考|同5图：生意r3、journey-convergence地图、图鉴v2、basket-base、precision-ref-node-T。|
|8. 多轮 edit|全组10张完成图=8初稿+手刷2次修订；本候选实际见对应IG，不能把所有都记为10轮。|
|9. 中间产物|[final-generation-record.json](../../../../artifacts/kitchen-asset-language-gate-20260927/final-generation-record.json)|
|10. 最终产物|[cleaning-AB-Comparison.png](../../../../artifacts/kitchen-asset-language-gate-20260927/cleaning-AB-Comparison.png)|
|11. 用户实际反馈|最后手刷B在Lv.2内部采用；用户没有单独确认。 [Checklist.md](../../../../artifacts/kitchen-asset-language-gate-20260927/Checklist.md)声明PASS不代表用户批准。|
|12. 用户 Gate|内部PASS；用户Gate未恢复到明确通过；不可作为完成厨房。|

**结果层（同10维）：** 结构=单物件；构图=孤立对象；颜色=暖木奶油绿；画风=既有游戏sprite；造型=扫把 / 手刷；手刷光晕初稿及局部edit均未被保留，重新生成；描边=棕轮廓；光照=局部，手刷光晕被拒；材质=宽带/少量；空间=局部浅体积；功能/装饰=可识别入口，未验证整屏主次。


<a id="k12"></a>

## K12 · Asset Language Gate — 仓库 A/B
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e199 · ordinal 558](evidence/01a0e199-messages.md)：只测4类独立Asset、每类A/B，不能拼厨房。|
|2. Work 的公开理解|开口箱 / 带盖编织箱，测试场景入口语义|
|3. 计划步骤|正例参考→独立透明资产→内部筛选→浅/木底与Runtime尺寸→用户素材Gate。|
|4. 实际顺序|并发生成8件；检查手刷光晕、试edit、重生成；程序叠旧蛋；建立兼容性板和逐项Checklist。|
|5. 工具|内置 ImageGen；现有蛋叠放/alpha处理；HTML评审板；浏览器尺寸检查。|
|6. ImageGen Prompt / 分级 / 产物类型|[IG071](prompts/IG071.md)、[IG072](prompts/IG072.md)；ORIGINAL，单Asset，非完整背景/页面。|
|7. 输入参考|同5图：生意r3、journey-convergence地图、图鉴v2、basket-base、precision-ref-node-T。|
|8. 多轮 edit|全组10张完成图=8初稿+手刷2次修订；本候选实际见对应IG，不能把所有都记为10轮。|
|9. 中间产物|[final-generation-record.json](../../../../artifacts/kitchen-asset-language-gate-20260927/final-generation-record.json)|
|10. 最终产物|[storage-AB-Comparison.png](../../../../artifacts/kitchen-asset-language-gate-20260927/storage-AB-Comparison.png)|
|11. 用户实际反馈|最终Lv.2保留旧仓库图标，两候选均未采用。 [Checklist.md](../../../../artifacts/kitchen-asset-language-gate-20260927/Checklist.md)声明PASS不代表用户批准。|
|12. 用户 Gate|内部PASS；用户Gate未恢复到明确通过；不可作为完成厨房。|

**结果层（同10维）：** 结构=单物件；构图=孤立对象；颜色=暖木奶油绿；画风=既有游戏sprite；造型=开口箱 / 带盖编织箱，测试场景入口语义；描边=棕轮廓；光照=局部，手刷光晕被拒；材质=宽带/少量；空间=局部浅体积；功能/装饰=可识别入口，未验证整屏主次。


<a id="k13"></a>

## K13 · Lv.2 Before / After Remaster
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e2fa · ordinal 9](evidence/01a0e2fa-messages.md)：以当前正式厨房为产品Base，停止开放探索，只做Lv.2最终Gate。|
|2. Work 的公开理解|把任务收敛为原木房重绘＋入口重新归位；进一步把产品Base解释成精确背景注册模板。|
|3. 计划步骤|计划→当前四级截图→重绘背景→独立Runtime→状态/交互验证→唯一对照。|
|4. 实际顺序|IG075严格锁定旧构图；商店/仓库/手艺上搁架，调味/清洁靠厨具；试蛋窝B遮蛋后保留旧窝；只选调味A/清洁B；拍390、320、430与多状态。|
|5. 工具|内置 ImageGen image edit；复制隔离Runtime；HTML/CSS/JS；浏览器；哈希验证。|
|6. ImageGen Prompt / 分级 / 产物类型|[IG075](prompts/IG075.md)；ORIGINAL。只编辑背景，完整UI由Runtime组合。|
|7. 输入参考|唯一编辑目标：kitchen-stage-1-v7.png；没有把成功三页一起送本次背景调用，画法约束用文字传达。|
|8. 多轮 edit|背景可见1次完成；后续程序调整、旧/新蛋窝适配实验。|
|9. 中间产物|[internal-nest-B-fit.png](../../../../artifacts/kitchen-remaster-lv2-20260927/internal-nest-B-fit.png)|
|10. 最终产物|[lv2-before-after-gate.png](../../../../artifacts/kitchen-remaster-lv2-20260927/lv2-before-after-gate.png)|
|11. 用户实际反馈|[U/W 01a0e2fa · ordinal 291](evidence/01a0e2fa-messages.md)：“Gate不通过…目标过于保守…原背景轻度重绘、入口重新排列、少量Asset替换”。缓存Chat亦说没耳目一新。|
|12. 用户 Gate|用户明确不通过；内部交互/视觉PASS不能覆盖它。|

**结果层（同10维）：** 结构=入口重排；构图=精确保留左架右窗/中央蛋窝；颜色=几乎同暖木；画风=稍减纹；造型=原容器；描边=接近旧版；光照=短影；材质=降低木纹；空间=原背景；功能/装饰=更干净但核心视觉表达未重制。


<a id="k14"></a>

## K14 · Background Direction A — Original Remaster
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e2fa · ordinal 291](evidence/01a0e2fa-messages.md)：允许重做窗/架/台面构图；只测Lv.2背景，不放蛋/UI，四方向同参考。|
|2. Work 的公开理解|横窗＋弧形工作面，木墙连续|
|3. 计划步骤|同5参考＋同留白坐标→4背景→D内部修订→相同Overlay→用户方向Gate。|
|4. 实际顺序|一批四方向生成（早期编排有重试）；D初稿经内部否定后用修订Prompt与原5参考重生成；保留4终稿，不接Runtime。|
|5. 工具|内置ImageGen；HTML/CSS Overlay；浏览器导图与哈希核对。|
|6. ImageGen Prompt / 分级 / 产物类型|[IG076](prompts/IG076.md)；ORIGINAL。完整Background，不是完整页面。|
|7. 输入参考|同5图：生意r3、寻访convergence、图鉴v2、旧Lv.2身份、被否的Concept-B作negative参考。|
|8. 多轮 edit|A/B/C各1个完成事件；D有2。D最后仍用原5参考，未把D初稿当编辑底图，不能称连续edit。|
|9. 中间产物|[final-prompts.json](../../../../artifacts/kitchen-background-direction-gate-20260927/final-prompts.json)|
|10. 最终产物|[A-background.png](../../../../artifacts/kitchen-background-direction-gate-20260927/A-background.png)|
|11. 用户实际反馈|缓存Chat用户：“感觉还是很相像…没有整体融入进去我也不好判断…有点普通”。见 [原话](evidence/cached-chat-messages.md)；不是Work自评。|
|12. 用户 Gate|整组未获认可；没有逐张排名/逐张用户拒绝记录。|

**结果层（同10维）：** 结构=无UI；构图=横窗＋弧形工作面，木墙连续；颜色=共用暖木/奶油/少量绿；画风=同一参考族内变体；造型=建筑结构；描边=结构边；光照=局部阴影但仍有底面渐变；材质=A/B木、C水粉、D少纹；空间=预留中部与下部；功能/装饰=玩法主体完全缺席，只能审背景。


<a id="k15"></a>

## K15 · Background Direction B — Game Scene
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e2fa · ordinal 291](evidence/01a0e2fa-messages.md)：允许重做窗/架/台面构图；只测Lv.2背景，不放蛋/UI，四方向同参考。|
|2. Work 的公开理解|上部/侧边木构片段＋连通空地，实际仍有台阶/木廊纵深|
|3. 计划步骤|同5参考＋同留白坐标→4背景→D内部修订→相同Overlay→用户方向Gate。|
|4. 实际顺序|一批四方向生成（早期编排有重试）；D初稿经内部否定后用修订Prompt与原5参考重生成；保留4终稿，不接Runtime。|
|5. 工具|内置ImageGen；HTML/CSS Overlay；浏览器导图与哈希核对。|
|6. ImageGen Prompt / 分级 / 产物类型|[IG077](prompts/IG077.md)；ORIGINAL。完整Background，不是完整页面。|
|7. 输入参考|同5图：生意r3、寻访convergence、图鉴v2、旧Lv.2身份、被否的Concept-B作negative参考。|
|8. 多轮 edit|A/B/C各1个完成事件；D有2。D最后仍用原5参考，未把D初稿当编辑底图，不能称连续edit。|
|9. 中间产物|[final-prompts.json](../../../../artifacts/kitchen-background-direction-gate-20260927/final-prompts.json)|
|10. 最终产物|[B-background.png](../../../../artifacts/kitchen-background-direction-gate-20260927/B-background.png)|
|11. 用户实际反馈|缓存Chat用户：“感觉还是很相像…没有整体融入进去我也不好判断…有点普通”。见 [原话](evidence/cached-chat-messages.md)；不是Work自评。|
|12. 用户 Gate|整组未获认可；没有逐张排名/逐张用户拒绝记录。|

**结果层（同10维）：** 结构=无UI；构图=上部/侧边木构片段＋连通空地，实际仍有台阶/木廊纵深；颜色=共用暖木/奶油/少量绿；画风=同一参考族内变体；造型=建筑结构；描边=结构边；光照=局部阴影但仍有底面渐变；材质=A/B木、C水粉、D少纹；空间=预留中部与下部；功能/装饰=玩法主体完全缺席，只能审背景。


<a id="k16"></a>

## K16 · Background Direction C — Storybook
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e2fa · ordinal 291](evidence/01a0e2fa-messages.md)：允许重做窗/架/台面构图；只测Lv.2背景，不放蛋/UI，四方向同参考。|
|2. Work 的公开理解|斜梁、圆窗、修补木接头、水粉墙面|
|3. 计划步骤|同5参考＋同留白坐标→4背景→D内部修订→相同Overlay→用户方向Gate。|
|4. 实际顺序|一批四方向生成（早期编排有重试）；D初稿经内部否定后用修订Prompt与原5参考重生成；保留4终稿，不接Runtime。|
|5. 工具|内置ImageGen；HTML/CSS Overlay；浏览器导图与哈希核对。|
|6. ImageGen Prompt / 分级 / 产物类型|[IG078](prompts/IG078.md)；ORIGINAL。完整Background，不是完整页面。|
|7. 输入参考|同5图：生意r3、寻访convergence、图鉴v2、旧Lv.2身份、被否的Concept-B作negative参考。|
|8. 多轮 edit|A/B/C各1个完成事件；D有2。D最后仍用原5参考，未把D初稿当编辑底图，不能称连续edit。|
|9. 中间产物|[final-prompts.json](../../../../artifacts/kitchen-background-direction-gate-20260927/final-prompts.json)|
|10. 最终产物|[C-background.png](../../../../artifacts/kitchen-background-direction-gate-20260927/C-background.png)|
|11. 用户实际反馈|缓存Chat用户：“感觉还是很相像…没有整体融入进去我也不好判断…有点普通”。见 [原话](evidence/cached-chat-messages.md)；不是Work自评。|
|12. 用户 Gate|整组未获认可；没有逐张排名/逐张用户拒绝记录。|

**结果层（同10维）：** 结构=无UI；构图=斜梁、圆窗、修补木接头、水粉墙面；颜色=共用暖木/奶油/少量绿；画风=同一参考族内变体；造型=建筑结构；描边=结构边；光照=局部阴影但仍有底面渐变；材质=A/B木、C水粉、D少纹；空间=预留中部与下部；功能/装饰=玩法主体完全缺席，只能审背景。


<a id="k17"></a>

## K17 · Background Direction D — Stylized
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e2fa · ordinal 291](evidence/01a0e2fa-messages.md)：允许重做窗/架/台面构图；只测Lv.2背景，不放蛋/UI，四方向同参考。|
|2. Work 的公开理解|初稿仍像房间，后改符号横梁/榫接大块|
|3. 计划步骤|同5参考＋同留白坐标→4背景→D内部修订→相同Overlay→用户方向Gate。|
|4. 实际顺序|一批四方向生成（早期编排有重试）；D初稿经内部否定后用修订Prompt与原5参考重生成；保留4终稿，不接Runtime。|
|5. 工具|内置ImageGen；HTML/CSS Overlay；浏览器导图与哈希核对。|
|6. ImageGen Prompt / 分级 / 产物类型|[IG079](prompts/IG079.md)、[IG080](prompts/IG080.md)；ORIGINAL。完整Background，不是完整页面。|
|7. 输入参考|同5图：生意r3、寻访convergence、图鉴v2、旧Lv.2身份、被否的Concept-B作negative参考。|
|8. 多轮 edit|A/B/C各1个完成事件；D有2。D最后仍用原5参考，未把D初稿当编辑底图，不能称连续edit。|
|9. 中间产物|[final-prompts.json](../../../../artifacts/kitchen-background-direction-gate-20260927/final-prompts.json)|
|10. 最终产物|[D-background.png](../../../../artifacts/kitchen-background-direction-gate-20260927/D-background.png)|
|11. 用户实际反馈|缓存Chat用户：“感觉还是很相像…没有整体融入进去我也不好判断…有点普通”。见 [原话](evidence/cached-chat-messages.md)；不是Work自评。|
|12. 用户 Gate|整组未获认可；没有逐张排名/逐张用户拒绝记录。|

**结果层（同10维）：** 结构=无UI；构图=初稿仍像房间，后改符号横梁/榫接大块；颜色=共用暖木/奶油/少量绿；画风=同一参考族内变体；造型=建筑结构；描边=结构边；光照=局部阴影但仍有底面渐变；材质=A/B木、C水粉、D少纹；空间=预留中部与下部；功能/装饰=玩法主体完全缺席，只能审背景。


<a id="k18"></a>

## K18 · Background 统一 Overlay
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e2fa · ordinal 291](evidence/01a0e2fa-messages.md)：半透明矩形标未来位置，只检查构图，不真正设计UI。|
|2. Work 的公开理解|把背景与未来占位区对齐，不放真实内容。|
|3. 计划步骤|统一矩形→4背景同坐标→截图。|
|4. 实际顺序|HUD、蛋窝、厨具、底栏四矩形叠层；同名区域完全一致；不更改背景像素。|
|5. 工具|HTML/CSS＋浏览器截图；没有额外ImageGen。|
|6. ImageGen Prompt / 分级 / 产物类型|N/A；底图沿用IG076–080。|
|7. 输入参考|A–D背景；HUD 3/1.5/94/7.5%、窝9/37/82/32%、厨具3/76/94/13%、底栏0/91.5/100/8.5%。|
|8. 多轮 edit|无ImageGen edit。|
|9. 中间产物|[verification.json](../../../../artifacts/kitchen-background-direction-gate-20260927/verification.json)|
|10. 最终产物|[B-overlay.png](../../../../artifacts/kitchen-background-direction-gate-20260927/B-overlay.png)|
|11. 用户实际反馈|同Background组反馈：用户无法从未完整融入的背景判断整体效果。|
|12. 用户 Gate|完成“占位验证”不等于完整页面Gate。|

**结果层（同10维）：** 结构=四块占位；构图=锁定共用大区；颜色=遮罩影响观感；画风/造型/描边/光照/材质=只继承背景，UI未提供；空间=预留而非实际叠放；功能/装饰=没有真实蛋窝状态/厨具密度/角色尺度，无法判完整主次。


<a id="k19"></a>

## K19 · K1 Architecture Prototype
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e3b1 · ordinal 319](evidence/01a0e3b1-messages.md)：完整结构Gate、复用正式素材、保留自然大蛋窝；四阶段与两尺寸。|
|2. Work 的公开理解|固定下方下一锅工作台，持续显示当前厨具/材料与主操作|
|3. 计划步骤|已有素材→独立结构原型→多状态截图→可达/截断/点击尺寸验证。|
|4. 实际顺序|HTML重建界面；共用eggButtons以row=floor(i/6)、col=i%6排24蛋、加1–2px偏移；状态切换与按钮只给原型反馈；27截图为全组K/F，不是本候选27张。|
|5. 工具|HTML/CSS/JS；旧背景/蛋/厨具；浏览器检查；没有ImageGen。|
|6. ImageGen Prompt / 分级 / 产物类型|N/A；没有生成新美术。|
|7. 输入参考|旧Kitchen Lv.1背景、正式蛋/窝/厨具、HUD/底栏；候选不是Lv.2 Remaster扩版。|
|8. 多轮 edit|无图像edit；CSS布局迭代。|
|9. 中间产物|[prototype.js](../../../../web/prototypes/kitchen-farm-architecture-gate-20260928/prototype.js)|
|10. 最终产物|[k1-incubating-390.png](../../../../web/prototypes/kitchen-farm-architecture-gate-20260928/evidence/k1-incubating-390.png)|
|11. 用户实际反馈|本轮用户明确指出规则分散排蛋是反向修改；历史会话未恢复到其后逐候选Gate原话。截图与源码独立支持回退事实。|
|12. 用户 Gate|结构原型未获最终用户通过；无权把机器验证通过当体验改善。|

**结果层（同10维）：** 结构=固定下方下一锅工作台，持续显示当前厨具/材料与主操作；构图=上部原场景/下部大面板；颜色=旧蜜黄；画风=旧sprite+圆角UI；造型=24蛋独立重复；描边=原蛋轮廓/新框；光照=原素材；材质=旧背景；空间=6列4行、弱遮挡；功能/装饰=重排操作但破坏原窝团块，原型反馈未证实真实流程体验。


<a id="k20"></a>

## K20 · K3 Architecture Prototype
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e3b1 · ordinal 319](evidence/01a0e3b1-messages.md)：完整结构Gate、复用正式素材、保留自然大蛋窝；四阶段与两尺寸。|
|2. Work 的公开理解|底部按准备/等待/可收/全收切换焦点，细轨保留厨具调味|
|3. 计划步骤|已有素材→独立结构原型→多状态截图→可达/截断/点击尺寸验证。|
|4. 实际顺序|HTML重建界面；共用eggButtons以row=floor(i/6)、col=i%6排24蛋、加1–2px偏移；状态切换与按钮只给原型反馈；27截图为全组K/F，不是本候选27张。|
|5. 工具|HTML/CSS/JS；旧背景/蛋/厨具；浏览器检查；没有ImageGen。|
|6. ImageGen Prompt / 分级 / 产物类型|N/A；没有生成新美术。|
|7. 输入参考|旧Kitchen Lv.1背景、正式蛋/窝/厨具、HUD/底栏；候选不是Lv.2 Remaster扩版。|
|8. 多轮 edit|无图像edit；CSS布局迭代。|
|9. 中间产物|[prototype.js](../../../../web/prototypes/kitchen-farm-architecture-gate-20260928/prototype.js)|
|10. 最终产物|[k3-incubating-390.png](../../../../web/prototypes/kitchen-farm-architecture-gate-20260928/evidence/k3-incubating-390.png)|
|11. 用户实际反馈|本轮用户明确指出规则分散排蛋是反向修改；历史会话未恢复到其后逐候选Gate原话。截图与源码独立支持回退事实。|
|12. 用户 Gate|结构原型未获最终用户通过；无权把机器验证通过当体验改善。|

**结果层（同10维）：** 结构=底部按准备/等待/可收/全收切换焦点，细轨保留厨具调味；构图=上部原场景/下部大面板；颜色=旧蜜黄；画风=旧sprite+圆角UI；造型=24蛋独立重复；描边=原蛋轮廓/新框；光照=原素材；材质=旧背景；空间=6列4行、弱遮挡；功能/装饰=重排操作但破坏原窝团块，原型反馈未证实真实流程体验。


<a id="f01"></a>

## F01 · F1 Farm Architecture Prototype
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e3b1 · ordinal 319](evidence/01a0e3b1-messages.md)：两方案三段、夜间解释、完好/整修、两尺寸，不新增玩法。|
|2. Work 的公开理解|建筑即入口；满完好度不强化整修，方向箭头提示右侧内容|
|3. 计划步骤|复用正式横向场景→入口/状态层→3段/2尺寸/整修→结构Gate。|
|4. 实际顺序|同一正式夜景连续裁切；F1标签附建筑、方向箭头；F3新增当前段行动区；“夜间休息”补充库存解释；只展示入口反馈，没有真实经济动作。|
|5. 工具|HTML/CSS/JS、正式PNG、浏览器；没有ImageGen。|
|6. ImageGen Prompt / 分级 / 产物类型|N/A；Farm F1/F3不是ImageGen失败样本。|
|7. 输入参考|当前正式Farm夜景、旧UI。不是先前IG052庭院继续edit。|
|8. 多轮 edit|无图像edit。|
|9. 中间产物|[ARCHITECTURE-GATE-BOARD.md](../kitchen-farm-architecture-gate-20260928/ARCHITECTURE-GATE-BOARD.md)|
|10. 最终产物|[f1-left-390.png](../../../../web/prototypes/kitchen-farm-architecture-gate-20260928/evidence/f1-left-390.png)|
|11. 用户实际反馈|本轮用户将Farm结构尝试列未解决；未恢复独立F1/F3审美通过/拒绝逐字记录。|
|12. 用户 Gate|未通过最终Gate/待定；不能具体编造“用户因草地空而否F1”等原话。|

**结果层（同10维）：** 结构=建筑即入口；满完好度不强化整修，方向箭头提示右侧内容；构图=原大草地不变；颜色=原蓝绿夜景；画风=既有环境插画；造型=原房屋神社；描边=旧物件；光照=原连续夜景；材质=密集草花；空间=830逻辑宽横向三段；功能/装饰=入口更清楚但主要可见面积仍是环境，F3又出现行动面板；只支持有限结构评价。


<a id="f02"></a>

## F02 · F3 Farm Architecture Prototype
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e3b1 · ordinal 319](evidence/01a0e3b1-messages.md)：两方案三段、夜间解释、完好/整修、两尺寸，不新增玩法。|
|2. Work 的公开理解|横向农舍/集市/神社三段，底部操作随视口变化|
|3. 计划步骤|复用正式横向场景→入口/状态层→3段/2尺寸/整修→结构Gate。|
|4. 实际顺序|同一正式夜景连续裁切；F1标签附建筑、方向箭头；F3新增当前段行动区；“夜间休息”补充库存解释；只展示入口反馈，没有真实经济动作。|
|5. 工具|HTML/CSS/JS、正式PNG、浏览器；没有ImageGen。|
|6. ImageGen Prompt / 分级 / 产物类型|N/A；Farm F1/F3不是ImageGen失败样本。|
|7. 输入参考|当前正式Farm夜景、旧UI。不是先前IG052庭院继续edit。|
|8. 多轮 edit|无图像edit。|
|9. 中间产物|[ARCHITECTURE-GATE-BOARD.md](../kitchen-farm-architecture-gate-20260928/ARCHITECTURE-GATE-BOARD.md)|
|10. 最终产物|[f3-left-390.png](../../../../web/prototypes/kitchen-farm-architecture-gate-20260928/evidence/f3-left-390.png)|
|11. 用户实际反馈|本轮用户将Farm结构尝试列未解决；未恢复独立F1/F3审美通过/拒绝逐字记录。|
|12. 用户 Gate|未通过最终Gate/待定；不能具体编造“用户因草地空而否F1”等原话。|

**结果层（同10维）：** 结构=横向农舍/集市/神社三段，底部操作随视口变化；构图=原大草地不变；颜色=原蓝绿夜景；画风=既有环境插画；造型=原房屋神社；描边=旧物件；光照=原连续夜景；材质=密集草花；空间=830逻辑宽横向三段；功能/装饰=入口更清楚但主要可见面积仍是环境，F3又出现行动面板；只支持有限结构评价。


<a id="x01"></a>

## X01 · Work 2 Remaining UI Remaster（补充边界样本）
|字段|恢复结果|
|---|---|
|1. 上层 Prompt|[U/W 01a0e0d9 · ordinal 9](evidence/01a0e0d9-messages.md)：遍历KEEP/POLISH/REDESIGN，复用正式资产、不剧透。|
|2. Work 的公开理解|批量页面收口与视觉QA。|
|3. 计划步骤|真实页面审计→6REDESIGN/21POLISH→回归→展示。|
|4. 实际顺序|页面级实现与截图；其后用户要求展示并转入Work2.5，指出Kitchen/Farm仍未完成整体融合。|
|5. 工具|读取资产、HTML/CSS、运行截图与测试；该阶段未见完成ImageGen事件。|
|6. ImageGen Prompt / 分级 / 产物类型|N/A（本阶段）；13时两次生成归K01/F00，不移算到已完成的Work2。|
|7. 输入参考|Work1正式资产与已通过三系统；内容不可据此全部公开。|
|8. 多轮 edit|代码组合迭代，不是新整页ImageGen。|
|9. 中间产物|[current-ui-remaster-audit.md](../../../ui/current-ui-remaster-audit.md)|
|10. 最终产物|[ui-remaster-final-report.md](../../../ui/ui-remaster-final-report.md)|
|11. 用户实际反馈|[U/W 01a0e0d9 · ordinal 518](evidence/01a0e0d9-messages.md)用户说“Work2已完成”并要展示；Work2.5又明确“不把KEEP/POLISH/REDESIGN结论视为不可修改”。|
|12. 用户 Gate|工程阶段完成有用户记录；不足以当每页审美通过，不加入核心成功对照结论。|

**结果层（同10维）：** 这是批量状态边界；不逐张披露新增收集内容。UI结构及视觉是否每页通过不能由6/21统计推出。
