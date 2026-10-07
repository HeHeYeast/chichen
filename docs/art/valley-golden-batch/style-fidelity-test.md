# Style Fidelity Experiment · 3 只（V-C1 / V-C5 / V-D3）

2026-09-26。v3 sheet 因与旧 193 画风断层未通过；本轮不重抽 12 只、不改 Concept Board，只用 3 只做 **reference-preserving image edit** 实验：以单张旧正式角色 PNG 为 base，只改 Concept 要求的区域。**等待确认**；未切资产、未接 Runtime、未做其他 9 只或剩余 36 只。

所有文件在 `artifacts/art/style-fidelity/`。ImageGen：`--profile character-final`，三次均选中 `codex789 / gpt-image-2.5-sunburst`，`fell_back=false`，`/v1/images/edits`，每只 **一次生成、未重抽**；`.meta.json` 与 `artifacts/imagegen/provenance.jsonl`（tag `fidelity-V-C1/C5/D3`）保留。

## 1. Base Character 选择

按种类、body shape、pose、头身比、翅膀与脚的位置匹配，不按料理匹配。候选板：[base-candidates.png](../../../artifacts/art/style-fidelity/base-candidates.png)。

| 新角色 | Base | 选择原因 | 未选的近似候选 |
|---|---|---|---|
| V-C1 荠菜煎饼鸡（趴平、憨厚） | **0:43** | 全部 114 只鸡里可视高度最矮（54px）、宽高比 1.28，趴平、脚在身前、头身一体、翅膀贴身，正是 Concept 的“贴地趴鸡”；纯黄底色，只需改背羽色 | 0:57/0:24 是双体；0:42 是圆球不是趴；0:65/0:67 有大道具 |
| V-C5 芝麻折扇鸡（小蛋身、昂首、尾扇） | **0:0** | 原版鸡宝标准站姿：蛋形、头身比、翅膀低置、脚位都是最基本的鸡宝语法，尾部空白可直接加扇尾，颜色就是奶黄，改动最少 | 0:1 偏瘦且要整体换色；0:40/0:26 需去掉叶/鹿角；0:28 是白色异形 |
| V-D3 荠叶粉结鸭（缩脖、矮圆、迷糊） | **1:38** | 鸭、矮圆、脖子缩进身体、翅膀小而贴身、蹼足在下缘；白/象牙底色接近粉条色，只需在颈部加卷结 | 1:9 太小且是炸物橙色；1:13/1:31 是绒毛球；1:22 是冬粉鸭本尊（对照品，不能用） |

Base 度量（[base-metrics.json](../../../artifacts/art/style-fidelity/base/base-metrics.json)，120×120 画布，alpha>64）：

| Base | 可视尺寸 | 线宽≈ | 线宽/高 | 描线占比 | 量化色数 |
|---|---|---|---|---|---|
| 0:43 | 69×54 | 2.8px | 5.2% | 0.207 | 91 |
| 0:0 | 74×81 | 2.0px | 2.5% | 0.159 | 75 |
| 1:38 | 72×70 | 2.0px | 2.9% | 0.237 | 56 |

## 2. Edit 方法与结果

- 输入：base 120px PNG 直接 LANCZOS 放大到 1024（`edits/*-input-1024.png`），不做任何重绘。
- Prompt（`edits/prompt-*.md`）：明确“这是放大的低分辨率 sprite，故意低细节、扁平、略不规则”，逐项要求保留姿态、比例、眼/嘴/脚样式、线宽、平涂阴影、细节密度、软 sprite 质感；只列 Concept 改动；禁止高光、体积光、3D、羽毛细节、真实食物质感、大眼、多腮红、光滑矢量描边、渐变。
- 输出 1254×1254 → 按画布比例缩回 120×120（`edits/*-edit-120.png`）。缩放只是技术处理；设计本身在 base 的细节密度上完成。

| 新角色 | 结果 | 保留了什么 | 改了什么 | 问题 |
|---|---|---|---|---|
| V-C1 | [V-C1-edit.png](../../../artifacts/art/style-fidelity/edits/V-C1-edit.png) | 0:43 的趴姿、橙冠、闭眼、嘴、翅膀、脚、线宽、平涂 | 背羽浅金煎饼色、6 处绿碎、背缘右上与左翅缘缺口 | 无明显问题；缺口偏小但 56px 可见 |
| V-C5 | [V-C5-edit.png](../../../artifacts/art/style-fidelity/edits/V-C5-edit.png) | 0:0 的蛋身、头身比、奶黄、眼嘴脚、翅膀、线宽、单块阴影 | 身后左下加半开五折扇尾、折谷芝麻、脸颊芝麻点、单眼眨眼 | 扇尾偏小偏低，56px 读成“贝壳”；原版头顶小呆毛被去掉 |
| V-D3 | [V-D3-edit.png](../../../artifacts/art/style-fidelity/edits/V-D3-edit.png) | 1:38 的矮圆缩脖体、绒毛感、蹼足、线宽 | 颈部一圈粉条卷结、头顶卷结呆毛、两片小叶、半睁眼 | **腹部出现一块灰色晕染伪影**（模型把“一块阴影”画成了脏灰块）；卷结读成围巾/绳 |

Edit 缩回 120px 后的度量（[edit-120-metrics.json](../../../artifacts/art/style-fidelity/edits/edit-120-metrics.json)）：

| 新角色 | 可视尺寸 | 线宽≈ | 线宽/高 | 描线占比 | 量化色数 | 对比 base |
|---|---|---|---|---|---|---|
| V-C1 | 76×60 | 2.0px | 3.3% | 0.173 | 69 | 尺寸略大 10%，线略细，色数更少（更平） |
| V-C5 | 84×74 | 2.0px | 2.7% | 0.143 | 61 | 加扇后变宽，其余与 0:0 几乎一致 |
| V-D3 | 66×80 | 2.0px | 2.5% | 0.167 | 93 | 因加了呆毛变高；色数增加来自灰色伪影 |

## 3. 对照与盲测板

- [base-v3-edit-comparison.png](../../../artifacts/art/style-fidelity/base-v3-edit-comparison.png)：每行 Base → v3 sheet 版 → Edit 1254 原图 → Edit 缩回 120 后放大。
- [style-fidelity-test.png](../../../artifacts/art/style-fidelity/style-fidelity-test.png)：12 只随机旧角色 + 3 只新角色，打乱，无名字，按图鉴网格 56px（整张 120 画布等比缩放，与游戏一致），左浅底右深底。
- [style-fidelity-test-48px.png](../../../artifacts/art/style-fidelity/style-fidelity-test-48px.png)：同一顺序 48px。
- 旧角色池：原作 0:1–0:113（排除游戏里已用 v4 重绘显示的 0:3、0:4 与 base 0:0）与 1:0–1:56，排除三只 base；随机种子 20260926。答案在 [style-fidelity-test-key.json](../../../artifacts/art/style-fidelity/style-fidelity-test-key.json)，也在本文末尾。

## 4. 观察到的 Style Fidelity 差异

| 维度 | v3 sheet（text-to-image 带参考） | Reference-preserving Edit |
|---|---|---|
| 线条 | 粗细完全均匀、圆滑、闭合，像矢量贴纸 | 线宽 2px 与 base 相同，转角和收笔保留原作的轻微不匀 |
| 五官 | 统一的“AI 萌宠脸”：大而亮的豆眼、固定腮红、固定嘴形 | 沿用各自 base 的眼形（0:43 闭眼、0:0 小豆眼、1:38 小点眼）；腮红只在 base 有的地方 |
| 色彩 | 饱和、干净、同一色温；每只多层色阶 | 沿用 base 的略灰、略暖的旧色；色阶数与 base 同级 |
| 阴影 | 柔和体积阴影＋高光，偶有渐变 | 一块平阴影，无高光（D3 的“阴影”被画坏成灰块，是失败案例而不是风格） |
| 细节密度 | 羽毛纹、米粒纹、糖丝纹等高清材质 | 与 base 同级：C1 只有 6 个绿点，C5 只有点状芝麻 |
| AI 感 | 明显；新旧混排一眼能挑出 | 低；在 56px/48px 混排中三只与旧角色处于同一层次 |
| 原游戏家族感 | 同一“品类”但不是同一批 | C1 几乎可当原作补画；C5 是原版鸡宝加尾饰；D3 因伪影和围巾感略弱 |
| 设计表达 | 强：料理落点清楚 | 弱一档：为了保真，改动被模型压得很保守（C5 扇小、D3 卷结像围巾） |

结论：reference-preserving edit 明显解决了画风断层，代价是料理表达变保守，而且单次生成里出现了一例明显伪影（D3）。若采用这条路线，每只需要允许 1–2 次同模型 edit 迭代（例如“把扇尾放大到与身体等高”“去掉腹部灰块”），并接受每只单独生成、以 base 为锚而非以 sheet 为锚。

## 5. 若确认走这条路线，下一步建议（未执行）

1. 为 12 只各选一个 base（已用的 0:43、0:0、1:38 之外再选 9 只，同样按体型/姿态，不按料理）。
2. Prompt 模板固定为本轮结构；每只允许最多 2 次同模型 edit（第一次改 Concept 区域，第二次只修局部问题），不换模型、不重画。
3. 每只输出 1254 原图 + 120px sprite 版；评审板改为“Base → Edit → 120px → 混排盲测”。
4. Normalization 以 base 的可视边界与脚点为参照，避免新角色比旧角色大一圈。

## 6. 盲测答案

| 槽位 | 身份 |
|---|---|
| 8 | **V-D3（新）** |
| 13 | **V-C5（新）** |
| 15 | **V-C1（新）** |
| 其余 | 旧角色：1:19、1:16、0:32、0:14、1:51、0:99、0:12、0:62、0:21、0:51、0:7、1:41（按槽位 1–7、9–12、14） |
