# 谷地 12 只 · Golden Batch Review v3

2026-09-26。按确认后的 [Concept Board](character-concept-board.md)（含 D2/D4/D5 微调与“料理≤约 1/3 视觉面积”规则）重新生成。**等待确认**；本轮未切图、未接 Runtime、未做剩余 36 只、未改 content-pack。

评审板：[valley-review-v3.png](../../../artifacts/art/valley-golden-batch/review/valley-review-v3.png)，含 Style Reference 对照、隐藏名称测试、正常尺寸、48px 浅/深底、silhouette、v2 → v3 并排。v2 的图片与 provenance 全部保留在原位，作为“不通过的设计方向”记录。

## 1. 产出与 ImageGen

| 文件 | 说明 |
|---|---|
| [valley-chickens-v3.png](../../../artifacts/art/valley-golden-batch/sheets/valley-chickens-v3.png) | 6 鸡 3×2，1536×1024 RGBA 真透明，`.meta.json` 同名 |
| [valley-ducks-v3.png](../../../artifacts/art/valley-golden-batch/sheets/valley-ducks-v3.png) | 6 鸭，同规格 |
| `prompt-valley-chickens-v3.md` / `prompt-valley-ducks-v3.md` | 新 prompt 结构：每格先写“一只什么体型、什么姿态、什么表情的鸡宝/鸭宝”，再写料理落在哪种羽毛/冠/尾/花纹上；顶部加入 `fundamentally a chicken/duck, not an anthropomorphic food item` 与 1/3 面积约束；鸭 sheet 明确禁止 block/slab/brick/cylinder/triangle 身体 |
| `review/crops/*.png`、`review/bounds.json` | v3 连通 alpha 切分（评审用）；v2 切分备份在 `review-v2-crops/` |

`--profile character-final`，两次调用均选中 `codex789 / gpt-image-2.5-sunburst`，`fell_back=false`；参考图与 v1 完全相同（四时图集 → 原作 16 张拼板 → 鸡宝 v4）；`quality high`、`background=transparent`。v3 一次生成即用，**未做任何单只 edit**。`artifacts/imagegen/provenance.jsonl` 新增 `valley-chickens-v3`、`valley-ducks-v3` 两行。

## 2. 隐藏名称测试（只看编号 silhouette + 48px 后再看正常尺寸）

| # | 第一眼 | 第二眼 | 结论 |
|---|---|---|---|
| 1 | 一只趴平的扁鸡，闭眼 | 背是煎饼色、有绿碎、羽缘两个咬口 | V-C1 ✓ |
| 2 | 一只挺胸抬翅、很神气的瘦鸡 | 一侧翅膀是三片菱形脆片，翅尖掉麦屑，头顶麦屑冠 | V-C2 ✓ |
| 3 | 一只蓬起来蹲坐、笑眯眯的胖鸡 | 玉米黄粗羽、绿点，头顶凹窝里一片叶 | V-C3 ✓ |
| 4 | 一只圆鸡回头看你，脸红 | 一条粗辫子从后颈垂到身侧，辫尾翘起 | V-C4 ✓（辫子位置偏侧，见 §4） |
| 5 | 一只挺胸眨眼的小鸡，身后有扇形尾 | 尾是半开折扇、折谷有芝麻，脸上芝麻雀斑 | V-C5 ✓ |
| 6 | 一只端正站立、头顶一穗的鸡 | 鸡冠就是麦穗，叶形翅，麦秆尾，羽上麦壳纹 | V-C6 ✓ |
| 7 | 一只趴着的长鸭，抬头 | 两只翅膀卷成蛋卷，翅尖是带绿圈的螺旋，一紧一松 | V-D1 ✓ |
| 8 | 一只端坐发呆的胖鸭 | 背羽压平有米粒纹，背后一小口露琥珀层，肚底一道琥珀带 | V-D2 ✓ |
| 9 | 一只缩着脖子、头顶一根卷毛的鸭 | 颈羽是粉条卷成的结领，夹两片荠叶 | V-D3 ✓ |
| 10 | 一只趴扁了的圆鸭，闭眼笑 | 暖乳白软 Q，侧腹琥珀糖丝纹，翅尾淡琥珀 | V-D4 ✓（奶砖身份最弱，见 §4） |
| 11 | 一只鼓着腮、皱眉蹲着的鸭 | 米粒羽纹、肚底和脚边胡椒点，隐约三角趋势 | V-D5 ✓ |
| 12 | 一只低头翘尾啄地的花鸭 | 背上成排凸起的麦粒羽，不对称麦棕斑 | V-D6 ✓ |

12/12 第一眼是鸡/鸭，没有一只再被读成“食物长了脸”。v2 → v3 并排行里每一格左边都是食物形、右边都是鸟形。

## 3. 检查清单

| 项 | 结果 |
|---|---|
| 与旧角色同一游戏 | 是；描边、豆眼高光、腮红、橙嘴橙脚、短翅与四时图集/原作一致。v3 比 v2 更接近原作：体型全部回到蛋形/梨形/趴伏/蹲坐词汇。 |
| 物种特征 | 6 鸡尖喙三趾；6 鸭宽扁嘴、低眼位、蹼足、翘尾；无串。 |
| 料理面积 | 除 C5 扇尾、C6 麦穗冠（羽毛结构）外，其余料理都在 1/3 以下；D2/D4/D5 主体已是普通鸭。 |
| AI 感 / 复杂度 | 低；无道具无服装。鸭 sheet 的羽毛质感（D2、D5、D6）比鸡 sheet 略蓬松、细节略多，仍在同一家族内。 |
| Silhouette | 不再要求两两极端；靠姿态分开：趴（C1、D1、D4 三只趴姿分别是扁鸡、长鸭、压扁圆鸭）、蹲（C3、D5）、坐（D2）、缩脖（D3）、翘尾（D6）、站（C2、C4、C5、C6）。 |
| 48px | 浅/深底 12 只都能认出姿态与关键落点；C1 缺口、C2 阶梯翅、C5 扇、C6 穗、D1 螺旋、D3 卷领、D6 凸背在 48px 可见；D2 小切口和 D4 糖丝纹在 48px 基本消失，靠姿态与色彩分辨。 |
| 裁切/边缘 | 12 个连通体（C2 麦屑分成 4 个碎块，已按格合并），无触边，无 halo。 |
| 视觉尺寸 | 鸭 sheet 整体画得比鸡 sheet 大一圈（D4 宽 492、D3 高 444 vs C4 宽 318）。这是 sheet 内相对尺度问题，正式 Normalization 时按视觉体量与脚点统一，不改 UI。 |

## 4. 尚存问题（可接受，标出供决定）

1. **V-C4** 辫子生成在颈侧垂下，更像“扎了一条辫子的鸡”，不是“背羽编成辫”。角色感成立、面积合规，但与 concept 的落点略偏。可保留，或后续按单只 edit 改成沿背脊。
2. **V-D4** 隐藏名称时只读成“趴扁的软白鸭”，奶砖身份靠色彩和糖丝纹，48px 下几乎只剩姿态。这是确认规则下的预期结果；若要更强识别，只能加大琥珀纹，不建议回到块状。
3. **鸭 sheet 略大、略蓬松**，后续三地区若沿用同 prompt，需在 Normalization 阶段统一体量，并在下一地区 prompt 里保留“same optical size as the chick sheet”一类约束。

## 5. 若确认，冻结为 Character Style Spec 的内容

- Style Anchors 与参考图顺序、`character-final`、3×2 1536×1024 sheet、`quality high`。
- Prompt 结构：先鸡/鸭本体（体型、姿态、表情）→ 再料理落点 → 顶部“fundamentally a chicken/duck” + 1/3 面积规则 + 鸭 sheet 禁止块状身体。
- Concept 方法：每只 2–3 个维度（Body / Food Integration / Pose / Expression / Signature / Gag），silhouette 允许共享。
- 评审板固定六项：Style Reference、隐藏名称测试、正常尺寸、48px 浅/深底、silhouette、上一版并排。
- 溪岸 → 茶坡 → 海湾各 12 只先做 Concept 表再生成，不再重新探索画风。
