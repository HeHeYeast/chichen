# 谷地 12 只 · Character Art Golden Batch Review

2026-09-26，Work 1 第一 Gate。**等待确认**；确认前未做剩余 36 只、未切图、未替换 Runtime、未改 FINAL manifest、未开始材料/发现/纪念物。玩法、经济、存档、RNG、内容身份、UI 结构均未改动。

评审板：[valley-review.png](../../../artifacts/art/valley-golden-batch/review/valley-review.png)（Style References ／ 12 只正常尺寸 ／ 48px 浅底 ／ 48px 深底 ／ silhouette）。设计依据：[character-design-spec.md](character-design-spec.md)。

## 1. 产出

| 文件 | 说明 |
|---|---|
| [valley-chickens-v2.png](../../../artifacts/art/valley-golden-batch/sheets/valley-chickens-v2.png) | 6 只鸡 3×2，1536×1024 RGBA，真透明（alpha 峰值 254，中转已知行为） |
| [valley-ducks-v2.png](../../../artifacts/art/valley-golden-batch/sheets/valley-ducks-v2.png) | 6 只鸭 3×2，同规格 |
| `valley-*-v1.png` + `.meta.json` | 首稿原始返回；sidecar 记录 provider/model/routing/参考图哈希 |
| `edits/V-C1-v2.png`、`V-D2-v2.png`、`V-C6-v2.png` + `.meta.json` | 三处单只 edit 的原始返回 |
| [v2-composition.json](../../../artifacts/art/valley-golden-batch/sheets/v2-composition.json) | v2 由 v1 + 三张 edit 合成的坐标/缩放记录 |
| [bounds.json](../../../artifacts/art/valley-golden-batch/review/bounds.json)、`review/crops/` | 连通 alpha 切分的 12 个 alpha bounds（仅评审用，不是正式 Normalization） |
| [tools/review-character-sheet.py](../../../tools/review-character-sheet.py) | 评审板与连通切分脚本，后续三地区复用 |

ImageGen：`--profile character-final`，全部 5 次调用实际选中 `codex789 / gpt-image-2.5-sunburst`，`fell_back=false`，无模型切换；`/v1/images/edits`，`quality high`，`background=transparent`。参考图顺序：四时图集 → 原作精灵 16 张拼板（2.4×）→ 鸡宝 v4。48 个 placeholder SVG 未作参考。`artifacts/imagegen/provenance.jsonl` 追加 5 行（tag `valley-chickens-v1`、`valley-ducks-v1`、`valley-edit-V-C1/V-D2/V-C6`）。

## 2. 迭代记录

| 轮 | 结论 | 处理 |
|---|---|---|
| v1 两张 sheet | 整体已属同一游戏；12 只单主体、不重叠、不触边、脚完整、无文字/场景。三处未达 brief 的身份特征 | 只做单只 edit，不重做 sheet |
| V-C1 | 缺“边缘两处小缺口”，silhouette 退化成普通蛋形 | edit 后左上、右下各一缺口，其余不变 |
| V-D2 | 缺“切面斜出一角”，与 V-D4 同为方块时靠这个分开 | edit 后右上角 45° 切面，其余不变 |
| V-C6 | 麦芒是细碎发丝状，48px 下成一撮乱毛 | edit 后为一束 6 根带麦粒的粗麦芒统一向右弯；因冠更高，合成时按 0.68 缩放并把脚底放到 y=1000，保持在自己的格内 |

## 3. 检查清单

| 项 | 结果 |
|---|---|
| 与旧角色是否同一游戏 | 是。描边色/线宽、豆眼＋高光、淡腮红、橙嘴橙脚、短弧翅与四时图集一致；柔和平涂＋一块暗部与原作精灵相近。比鸡宝 v4 略多一点纹理，与四时/点心坊两批持平。 |
| 鸡/鸭物种特征 | 6 鸡均为小尖喙＋三趾；6 鸭均为宽扁嘴＋低眼位＋蹼足；无鸭带冠、无鸡带宽嘴。 |
| AI 感 | 低。可见问题：C1/C2/C3/D2/D5 表面有轻微颗粒纹理（原作 0:113 等也有类似处理，48px 不可见）。 |
| 是否过度复杂 | 否。每只一个主形＋一种纹理＋≤2 处配料，无帽饰道具。 |
| 是否换皮 | 否。12 个 silhouette 两两不同：扁圆缺口 / 阶梯菱 / 圆锥 / 辫团＋尾 / 扇 / 水滴＋麦冠 / 横胶囊 / 削角长方 / 低堆探头 / 圆背砖 / 三角 / 凸背鸭。 |
| Silhouette 差异 | 最接近的一对是 V-D2 与 V-D4；v2 后靠削角、高度（282 vs 356）、纹理分开，48px 深浅底均可分。 |
| 视觉尺寸一致 | alpha 高 271–382（C6 含冠 471），宽 362–444。鸭因横向体型偏矮偏宽，视觉体量接近。正式 Normalization 时按视觉体量/脚点统一，不按 PNG 外框。 |
| 48px 辨识 | 12 只在浅底与深底均可辨认其食物/形状；C6 麦冠、C1 缺口、D2 削角在 48px 仍可见。 |
| 裁切/边缘 | 12 个连通体各 1 部件、无触边；深底下无明显 halo。 |

## 4. 尚存的小问题（已知，待你决定是否在冻结前处理）

1. V-D4 麦芽奶砖鸭在没有配色语境时会被读成豆腐；brief 定义就是乳白方砖，加深琥珀纹会更像奶砖但也更像布丁，本轮未改。
2. V-D3 荠叶粉结鸭是“鸭头从粉结中探出”，视觉上像“坐在窝里的鸭”，符合 brief 但食物感弱于其他 11 只。
3. V-D6 背部谷粒羽读作扇贝状鳞片，48px 下仍是“凸背”，可接受。
4. 表面细颗粒纹理略多于鸡宝 v4；若希望更平涂，需要在冻结 Style Spec 时统一决定，并对三地区一致执行，不建议只改个别角色。

## 5. 确认后的下一步（未开始）

冻结 Character Style Spec（本文 + design spec + 两份 prompt + 参考图顺序 + character-final），依次溪岸 12 → 茶坡 12 → 海湾 12；随后统一 Pipeline：Sheet → 连通切分 → Cleanup → Normalization（alpha/visual/body bounds、bottom-center anchor、portrait crop、safe area、recommended scale）→ full/portrait/silhouette → metadata → manifest → Runtime 验证。
