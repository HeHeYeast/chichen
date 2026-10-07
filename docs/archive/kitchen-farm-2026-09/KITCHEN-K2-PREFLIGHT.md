# 《鸡宝厨房》Kitchen Lv.2 · K2 生成前核验

**读者与用途：** 供下一轮完整 Concept 制作者决定是否各花一张 ImageGen。范围止于 K0 产品真值和 K1 两份方向 Brief；`READY FOR K2` 只表示方向可进入用户确认与生成准备，不代表视觉稿获批。流程顺序以本轮用户给定的 Production Pipeline Design 为准：K0 → K1 → 最多两张完整 Lv.2 Concept → Asset / Runtime。

依据：[Kitchen / Farm Audit](kitchen-farm-audit-20260928/AUDIT-BOARD.md)、[历史对照](production-chain-forensics-20260928/05-success-vs-failure-comparison.md)、[重复失败](production-chain-forensics-20260928/06-repeated-failure-patterns.md)、[成功模式](production-chain-forensics-20260928/07-successful-production-patterns.md)、[整图结论](production-chain-forensics-20260928/08-whole-image-conclusion.md)、[UI / Art Reference Study](ui-art-reference-study-20260928/REPORT.md)。本地未找到单独保存的 Production Pipeline Design 文件；不推测其余条款。

## 1. Product Truth · Hard Preserve

- **自然密集的大蛋窝是主角。** 蛋前后遮挡、紧密错落，缩小后首先读成“一窝蛋”。禁止 Grid、规则行列、等距排布，以及规则阵列加轻微随机偏移。保留的是**群形与遮挡关系**，不只是单枚蛋素材。
- 孵化、计时反馈、逐枚可收与收取，必须直接关联蛋窝；厨具是另一组核心操作对象，两者共同构成 Kitchen。
- 顶部 HUD 与固定五入口 Bottom Navigation 保持其真实语义；厨房、农场、生意、寻访、图鉴仍可识别。
- 现有玩法语义、经济、状态、存档和已经可用的交互由后续 Runtime 承接；视觉重制不得以重做玩法为前提。

核对图：[正式 Lv.2、自然蛋群](../../../artifacts/kitchen-remaster-lv2-20260927/before-lv2-390x844.png)；[K1 原型、规则排蛋反例](../../../web/prototypes/kitchen-farm-architecture-gate-20260928/evidence/k1-incubating-390.png)。[Lv.2 Remaster 后图](../../../artifacts/kitchen-remaster-lv2-20260927/after-lv2-390x844.png)保住蛋群，但旧室内主形变化不足，不因此成为新构图模板。

## 2. Open to Redesign

墙体的组织或是否出现、窗户有无与位置、搁架、台面、厨具的承托方式、调味入口、清洁状态、商店／仓库／手艺／帮助的视觉层级，以及背景与 UI 的空间关系，全部开放重做。环境只需让人认出“厨房”和看懂玩法对象，不需交代一套完整可信的室内。旧 Runtime 坐标、左架右窗与旧台面分界均不是约束。

**Not a design variable：** 蛋的数量规则、计时文本、CP 数值、实际库存、真实中文文案、存档逻辑、点击行为。K2 Concept 只需为这些真实状态预留可读的附着关系；具体内容和执行属于后续 Runtime。

## 3. Reference Role Map

| K2 候选参考图 | 唯一角色 | 可取 / 不可取 |
|---|---|---|
| [生意主屏](../../../artifacts/golden-business-r3/active-390x844.png) | **Style Truth** | 深棕主轮廓、浅底与局部对象色、少量承托影、对象与标签同组；不复制 2+1 篮子、店牌或入口布局。 |
| [寻访地图](../../../artifacts/journey-convergence/after/idle-map-390.png) | **Style Truth** | 场景弱边、功能物件强边、局部混合透视、环境退后；不复制河流、四地标、路线或地图占比。 |
| [图鉴收藏页](../../../artifacts/golden-collection-v2/empty-390.png) | **Style Truth** | 浅底、状态与内容容器的边缘层级、低对比材质；不复制纸页、二列格、印章或贴纸白边。 |
| [正式 Kitchen Lv.2](../../../artifacts/kitchen-remaster-lv2-20260927/before-lv2-390x844.png) | **Product Truth** | 只取自然蛋群的**整体群形、前后遮挡**，厨具身份，以及 HUD／五入口语义。K2 如需图像输入，先只提供相应局部，不把旧整屏作为构图参考；不沿用墙、窗、架、台面或坐标。 |

**Composition Truth：空。** 仅当用户明确批准 K2 的某张完整 Concept，它才可升级为构图真值。

**Negative Evidence（文字禁例，不作为 ImageGen reference 输入）：** [完整 Cozy Kitchen Concept](../../../artifacts/kitchen-concept-remaster-20260927/concepts/Kitchen-ABC-4Levels-Overview.png)的房间／家具主形；[Style A/B/C](../../../artifacts/kitchen-visual-language-lv2-20260927/Lv2-ABC-Style-Comparison.png)对旧室内结构的锁定；[保守 Lv.2 Remaster](../../../artifacts/kitchen-remaster-lv2-20260927/after-lv2-390x844.png)的旧坐标冻结；[Background A–D](../../../artifacts/kitchen-background-direction-gate-20260927/ABCD-overview.png)缺少玩法主体的空背景；[K1/K3 原型](kitchen-farm-architecture-gate-20260928/ARCHITECTURE-GATE-BOARD.md)的规则蛋阵列。失败图只转写关系禁例，避免让其画面反向牵引生成。

## 4. Direction A Brief

1. **一句话视觉概念：** 一窝蛋就是这间鸡宝厨房正在忙碌的中心。
2. **主形：** 单一、连续、自然拥挤的蛋窝占据最大块面；厨具群依蛋窝外缘形成近邻，整体是“中心与围合”，环境留出安静边界。
3. **阅读顺序：** 大蛋窝与前后错落的蛋 → 紧贴它的孵化／可收状态 → 邻近且可辨认的厨具与下一锅准备。
4. **蛋窝与厨具关系：** 厨具像围绕当前这窝蛋继续工作的器物，靠相邻、朝向和共用承托关系相接；不把厨具压成屏幕底部商品栏。调味与清洁可依对应对象出现，不另造抢眼主形。
5. **UI / Scene relationship：** 蛋窝、厨具及必要承托属于场景对象；等级、CP、设置和五入口属于全局 HUD／导航；计时／可收贴蛋窝，选材／开锅贴厨具，清洁贴其状态来源。低频入口按需收进次级层，保持可发现性。
6. **Art direction hypothesis：** 沿用生意主物件深棕轮廓、寻访环境弱边；浅底负责留白，对象色负责蛋与厨具，动作色和状态色各有职责。蛋与厨具只用少量色面和接触影；材质符号集中于对象身份，环境低密度。允许蛋与承托面的局部混合透视，不能为完整房间透视缩小蛋窝。
7. **Explicit exclusions：** 圆桌嵌窝；壁龛包窝；完整墙窗家具成为主形；厨具变底部商品格；低频入口四角常驻争主位；自然蛋群被均匀排点替换。

## 5. Direction B Brief

1. **一句话视觉概念：** 一件件厨具把下一锅送向大蛋窝的鸡宝厨房。
2. **主形：** 厨具操作块与更大的自然蛋窝形成**斜向咬合的双块关系**，中间由短的功能性交接区连接；不是上下叠两张互不相干的卡，也不描绘完整厨房纵深。
3. **阅读顺序：** 有明确操作身份的厨具 → 与它咬合、占更大面积的密集蛋窝 → 贴在交接处的当前批次状态／下一动作。
4. **蛋窝与厨具关系：** 厨具是开锅端，蛋窝是孵育和收取端；两端在同一画面内相接，调味随开锅端，计时与收取随蛋窝端，清洁反馈落在受影响的一端。交接由空间关系表达，不画生产流水线或新玩法。
5. **UI / Scene relationship：** 厨具、蛋窝与两者之间的短承托关系属于场景；顶部 HUD 与五入口仍为固定全局层；配料／可用状态贴厨具，计时／可收贴蛋窝，阶段性动作可贴交接处。低频入口保持一处次级层级，不永久铺满页面。
6. **Art direction hypothesis：** 仍用已认可页面的强对象轮廓、弱背景边和稳定中文标签区；浅底让双块分明，对象色区分厨具与蛋群，动作／状态色只强调当前关系。两块各有局部弱体积，材质限于辨认厨具和窝；环境密度低于交接区。用功能性混合透视保证两端正面可读，不强求统一家具消失点。
7. **Explicit exclusions：** 厨具独占画面令蛋窝缩成角落；斜向关系变成等距流程图；两端变成上下卡片／商品栏；画出完整柜体、桌腿和墙窗来解释连接；低频入口沿边常驻；蛋群排成斜向规则矩阵。

## 6. 极简 Composition Sketch

仅表达主次块与视线；方框不是按钮、家具或 Runtime 坐标。

```text
A · 中心围合
┌──────── HUD ────────┐
│    次级安静环境      │
│   ╭────────────╮    │
│   │ 大自然蛋窝  │ ← 第一眼
│   ╰────────────╯    │
│       ↓ 状态         │
│   近邻厨具群 ← 第三眼│
├──── Bottom Nav ────┤
```

```text
B · 双块交接
┌──────── HUD ────────┐
│ ┌───────┐            │
│ │厨具操作块│ ← 第一眼  │
│ └───────┘╲           │
│          ╲ 交接状态  │
│       ╭──────────╮   │
│       │ 大自然蛋窝 │ ← 第二眼
│       │          │   │
│       ╰──────────╯   │
├──── Bottom Nav ────┤
```

## 7. Direction Separation Check

| 去掉名称后比较 | A | B | 结论 |
|---|---|---|---|
| 主形 | 单一大蛋窝，厨具近邻围合 | 厨具块与更大蛋窝斜向咬合 | 不同 |
| 阅读顺序 | 蛋窝 → 蛋状态 → 厨具 | 厨具 → 蛋窝 → 交接状态 | 不同 |
| 蛋窝／厨具关系 | 围绕当前一窝共同工作 | 开锅端与孵育端交接 | 不同 |
| 环境职责 | 给中心主体留安静边界 | 标明两端相接，弱化其余空间 | 不同 |
| UI 与场景 | 状态沿中心与近邻对象附着 | 状态沿两端和交接处附着 | 不同 |

**去画风测试：** 只保留灰块与箭头仍能分辨“中心围合”和“斜向交接”；两方向差异不依赖颜色、窗型或风格名称。通过。

## 8. K2 Risk Checklist

| 生成前风险 | A | B |
|---|---|---|
| 变成完整室内设计；偷锁圆桌／壁龛／窗位／橱柜；要求可信家具透视 | Brief 只规定中心与近邻，禁圆桌和完整室内；通过 | 只规定双块交接，禁完整柜体与消失点；通过 |
| 大蛋窝弱化，或厨具降成底部商品栏 | 蛋窝第一眼且最大；厨具仍为近邻实体；通过 | 蛋窝虽第二眼但面积更大；厨具是操作端而非栏；通过，K2 必须重点核图 |
| 低频入口永久铺满页面 | 限定次级层；通过 | 限定一处次级层级；通过 |
| 只剩“暖木、可爱、故事书” | 中心围合与轮廓／色彩／密度职责明确；通过 | 双块交接与轮廓／色彩／密度职责明确；通过 |
| 重新生成规则蛋阵列 | 明写整体群形与前后遮挡；通过，K2 逐图核图 | 明写整体群形与前后遮挡，另禁斜向矩阵；通过，K2 逐图核图 |

**K2 实际 Gate：** 每张完整 Concept 先看缩略图主形与读序，再看蛋群遮挡、厨具身份、状态附着及同世界画风；任一硬保留项失真即退回，不进入 Asset / Runtime。K2 最多各生成一张完整 Lv.2 Concept，是否采纳由用户看整页决定。

## 9. 结论

**READY FOR K2。** 两份 Brief 已在主形、阅读顺序、对象关系、环境职责及 UI 附着方式上分离，参考图角色也已隔离。此结论不授权本轮生成；先让用户确认这两个方向，再按既定额度进入 K2。
