# Legacy Style Reverse Engineering · 生成控制方法对比（V-C5 芝麻折扇鸡）

2026-09-27。目标只有一个：**哪种 ImageGen 工作流最能保留旧鸡宝真实画风？** 本轮未重做谷地 12 只、未改 Runtime、未切资产、未做剩余 36 只、未冻结 Style Spec。Concept Board v3 未改。

所有文件在 `artifacts/art/legacy-style/`。量化依据见 [legacy-character-style-analysis.md](legacy-character-style-analysis.md)。

## 1. 测试角色与 Base

V-C5：鸡本体简单、料理只在尾羽、容易看出模型有没有重画主体。Base = **0:0 原版鸡宝**（`assets/png/Character/character_0/character_0_0_0_0.png`，120×120，可视 74×81，脚底 y=119）。注意：游戏内 0:0 现在显示的是 chick-v4-0 重绘，本实验用的是原作精灵。

## 2. 方法与执行

| 方法 | 输入 | Prompt | Provider / model（均一次生成） |
|---|---|---|---|
| Previous v3 sheet | 三张参考图，text-to-image sheet | 长 prompt（本体+料理+风格禁令） | codex789 sunburst |
| Previous reference-edit | base LANCZOS 放大到 1024 | 长 prompt（逐项保留、逐项禁止） | codex789 sunburst |
| **Test A** minimal prompt | base LANCZOS 1024 | 3 句极短 prompt（[prompt-minimal.md](../../artifacts/art/legacy-style/inputs/prompt-minimal.md)），无 cute/soft/detailed 等词 | codex789 sunburst |
| **Test B** low-information | base **最近邻** 放大到 1024（无平滑、无超分） | 同 A | codex789 sunburst（第一次调用返回失败、无详细错误记录，重试一次成功） |
| **Test B2** | base 原 120×120 直接上传 | 同 A | codex789 sunburst |
| **Test C** masked edit | base LANCZOS 1024 + mask（alpha 0 = 可编辑，仅尾部框 x0–46、y48–118 的 120 空间区域，[mask-preview.png](../../artifacts/art/legacy-style/inputs/mask-preview.png)） | 同 A | vectorengine gpt-image-2（mask 有文档）；codex789 sunburst（mask 无文档，probe） |
| **Test D** 局部生成＋合成 | base 作风格参考，只生成折扇尾 part（[prompt-fan.md](../../artifacts/art/legacy-style/D/prompt-fan.md)） | 只描述扇尾 | codex789 sunburst；随后脚本把 part 缩到体高 62%、alpha 合成在**原图之后**，原图像素零改动，无程序绘制（[composite.json](../../artifacts/art/legacy-style/D/composite.json)） |

所有 1254 输出仅按画布比例缩回 120（`*-120.png`）用于评审；1254 原图只做技术检查。

## 3. 对照板

- [legacy-style-method-comparison.png](../../artifacts/art/legacy-style/legacy-style-method-comparison.png)：Base → v3 → 前一轮 edit → A → B → B2 → C-ve → C-codex → D，各在 120 画布、图鉴 56px 浅/深底、48px，以及对齐后的锁定区差异图。
- [legacy-style-blind-test.png](../../artifacts/art/legacy-style/legacy-style-blind-test.png) 与 [48px 版](../../artifacts/art/legacy-style/legacy-style-blind-test-48px.png)：15 只随机旧角色 + 7 个新结果（A、B、B2、C-ve、C-codex、D、前一轮 edit）打乱、无标签、整张 120 画布缩到显示尺寸。答案 [legacy-style-blind-test-key.json](../../artifacts/art/legacy-style/legacy-style-blind-test-key.json)，也在本文末尾。

## 4. 像素保持程度（Base 未要求修改区域）

锁定区 = base 可见像素减去尾部编辑框，共 2919 px。模型输出普遍把角色重新居中/缩放，所以先在 scale 0.85–1.15、位移 ±18 px 内搜索最佳对齐，再统计变化（>48 RGB 差或 >64 alpha 差）。差异图 `analysis/diff-*.png`（红=锁定区被改，蓝=编辑框内容）。

| 方法 | 对齐（scale, dx, dy） | 锁定区被改像素 | 观察 |
|---|---|---|---|
| Previous reference-edit（长 prompt） | 0.95, −2, +12 | **31.0%** | 整只重画：眼、腮红、脚、阴影全部重绘，线改棕 |
| Test A minimal prompt | 0.95, −8, +10 | 23.1% | 仍整只重画，但线保持黑、阴影一块；眼更大、加了腮红和高光 |
| Test B nearest ref | 0.95, −4, +8 | 23.2% | 与 A 同级；纹理最低（5.0），无高光，最接近旧图度量 |
| Test B2 raw 120 ref | 0.90, −2, +12 | **37.1%** | 信息太少，模型自由发挥最多，线偏棕 |
| Test C mask (VectorEngine) | 0.95, −8, +8 | 20.5% | mask 被接受（HTTP 200），但锁定区仍被重画，还加了头顶冠羽、腮红和尾部闪点 |
| Test C mask (Codex789 probe) | 0.90, −12, +10 | 23.5% | 同上：接受 mask 字段，无锁定效果，也加了冠羽 |
| **Test D part + composite** | 1.0, 0, 0 | **0.0%** | 原图像素完全不变；新增内容只有扇尾 part |

结论：两家 relay 的 `mask` 都不是硬锁——gpt-image 系列会重画整幅，锁定区变化与不带 mask 的 A 处于同一水平（20–23%）。位移/缩放（dx −2…−12，scale 0.9–0.95）说明模型总会重新构图，任何 edit 路线都需要后续按 base 对齐。

## 5. 画风保真观察（在 56px/48px 判断，不看 1254）

| 维度 | v3 sheet | 长 prompt edit | A / B / C（极短 prompt 整体 edit） | D（局部＋合成） |
|---|---|---|---|---|
| 线条 | 棕、均匀、矢量感 | 棕 | **黑**，粗细与旧图同级，略不匀 | 主体=原图；扇 part 黑线、平涂 |
| 五官 | 统一 AI 萌脸 | 沿用但重绘 | 重绘：眼略大（A 15.9%）、多数加了腮红 | 原图五官 |
| 色彩/阴影 | 多层、饱和 | 单块阴影 | 单块阴影，主色 5–6 | 原图 |
| 细节密度 | 12.6 | 6.0 | 4.8–6.8（旧图素体范围） | 7.5 |
| AI 感 | 明显 | 中 | 低；混排中不靠“重复出现的同一只鸡”很难挑出 | 最低；扇尾像贴在身后的贝壳，融合最弱 |
| 料理表达 | 强 | 中 | 中：扇尾都偏小、偏像贝壳 | 弱：位置/大小由脚本决定，衔接无过渡 |
| 稳定性 | — | 出现过伪影（D3 灰块） | C-ve 出现闪点；C 两家都擅自加冠羽 | 只依赖 part 生成，风险最小 |

盲测板的诚实说明：7 个新结果都是同一只 0:0 的变体，所以在混排中能被认出的原因是“同一只鸡出现了 7 次”，不是画风；单独看任何一只，A/B/C/D 在 56px 下与旧鸡处于同一层次，v3 与长 prompt edit 则能被挑出（棕线、腮红、整体更“干净”）。

## 6. 回答：哪种工作流最能保留旧画风

1. **最保真：Test D（AI 只生成新增局部，原始 sprite 原样合成）**。原图 100% 保留，新增 part 在极短 prompt＋旧图参考下自然得到黑线、平涂、低密度。代价：part 的大小、位置、衔接由脚本决定，对“尾羽、冠、背饰”这类外挂结构可行，对“背羽换色、羽纹、缺口”这类要改原图像素的特征不适用。
2. **次之：极短 prompt 整体 edit（A/B）**。线色、层数、密度都回到旧图范围，但主体必被重画 20–25%，眼睛/腮红/脚会漂；需要按 base 对齐并用度量门禁（黑线、眼高、主色数、纹理）验收，允许同模型重抽。B（最近邻参考）比 A 少一点高光/腮红，可作为默认输入方式；B2（原 120 直传）反而更差。
3. **mask 不可用作锁定**：两家 relay 都接受 mask 但不生效，且都擅自加冠羽；不建议依赖。
4. **长 prompt 是画风漂移的主要来源**：同一 base、同一模型，长 prompt 把线改棕、重画 31%，短 prompt 23%。
5. 现役 chick-v4-0/3/4 本身也是棕线，与旧图不一致；若以“旧 193 画风”为准，这三张也不应作为风格锚点。

建议的正式路线（未执行、待确认）：**D 为主、B 为辅的混合流程**——外挂结构（尾扇、麦穗冠、辫子、卷结领、蛋卷翅）用 D；需要改原图像素的特征（煎饼背羽色、绿碎、缺口、米粒纹、糖丝纹）用 B 路线整体 edit，再把 edit 结果中“未要求修改的区域”替换回 base 像素（对齐后按 mask 合成，脚本只做合成不绘制），最后用 [analyze-legacy-style.py](../../tools/analyze-legacy-style.py) 的度量与 56px 混排盲测验收。每只以体型/姿态最接近的旧 sprite 为 base，而不是从 sheet 出发。

## 7. 盲测答案

新结果所在槽位：1 = C-codex，7 = B，10 = C-ve，13 = A，16 = D，17 = 前一轮 reference-edit，19 = B2。其余 15 个为旧角色：0:68、0:83、0:22、1:8、0:106、0:82、0:77、0:30、0:33、1:13、0:95、1:15、0:50、0:21、0:64。
