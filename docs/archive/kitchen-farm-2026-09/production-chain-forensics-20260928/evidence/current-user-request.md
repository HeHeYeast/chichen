# 《鸡宝厨房》Kitchen / Farm 历史生产链取证与成功/失败对照

你现在不是在继续设计 Kitchen / Farm，也不是在做普通项目总结。

## 一、项目背景

《鸡宝厨房》已经有正式可玩的 Web Runtime。

目前已有一些用户明确认可的页面：

- 生意：鸡宝坐在篮子里经营小铺
- 寻访：童话旅行地图
- 图鉴：贴纸收藏册 / 手账

这些页面的整体视觉、UI 组织和游戏感基本符合用户预期。

但 Kitchen 和 Farm 的 Remaster 已经进行了大量尝试，用户仍然不满意。

Kitchen 尤其经历过：

- 完整 AI Concept
- 四级厨房探索
- 灰盒
- Style Test
- Environment Illustration
- Game Asset Composition
- UI-first Game Scene
- Asset Gate
- 独立 Lv.2 Remaster
- Background A/B/C/D
- Overlay
- K1/K3 Architecture Prototype

Farm 也做过 F1/F3 等结构尝试。

这些尝试的问题包括：

- 新背景构图、配色、风格大同小异，本身就没有达到用户审美要求；
- 单独看背景也无法判断完整 UI；
- 完整界面生成容易出现明显的 AI 插画感 / Cozy Interior 感；
- 结构原型虽然调整了入口，但很多只是“移动按钮”，没有带来实际体验提升；
- Kitchen 某些原本成功的设计甚至被改坏，例如用户最喜欢的鸡蛋排列是**紧密、自然错落、互相遮挡的一大窝蛋**，而后续原型曾把它改成更规则、更分散的排列，这是明确的反向修改。

用户现在已经不愿意继续用昂贵额度做盲目探索。

---

# 二、本轮真正目的

本轮的目的不是总结“哪些图不好看”。

真正要回答的是：

> **为什么同样由 Work + ImageGen 参与生产，生意、寻访、图鉴能够得到用户满意的完整页面，而 Kitchen / Farm 的大量尝试却持续失败？**

我们需要恢复并比较：

**Prompt → Work 如何理解 → Work 实际执行路径 → ImageGen Prompt → 输入参考 → 中间结果 → 最终结果 → 用户反馈**

最终这份报告将用于下一阶段设计一套新的 **Kitchen / Farm Production Pipeline**。

后续是否继续整图生成、是否改为局部 asset、怎样使用 reference、ImageGen 在哪一步使用、用户在哪一步 Gate，都要基于本轮证据决定。

所以本轮必须尽量恢复“过程”，而不是只评价最终 PNG。

---

# 三、取证原则

尽可能避免 Computer Use。

固定优先级：

1. CLI / filesystem
2. SQLite / JSON / JSONL / logs
3. 项目内 docs / artifacts / screenshots / markdown / manifests
4. 如果以上无法恢复，再明确报告缺口

不要自动打开旧 Work GUI 会话。

如果历史数据无法恢复，就基于现有文档、图片、artifacts 做有限证据分析。

**绝不伪造不存在的 Prompt 或执行记录。**

---

# 四、先做 History Recovery Report

检查本机可访问范围：

- `%USERPROFILE%\.codex`
- `%APPDATA%`
- `%LOCALAPPDATA%`
- 当前鸡宝厨房项目目录
- docs
- artifacts
- logs
- `*.sqlite`
- `*.sqlite3`
- `*.db`
- `*.json`
- `*.jsonl`
- `*.log`
- `*.md`

SQLite 只读：

- `.tables`
- `.schema`
- `SELECT`

禁止任何修改数据库的操作。

先说明：

- 能否恢复 Work 用户 Prompt
- 能否恢复 Work 中间回复
- 能否恢复 tool calls
- 能否恢复 ImageGen Prompt
- 哪些只能从项目文件推断
- 哪些完全缺失

---

# 五、复盘对象

## A. Kitchen / Farm 失败或未通过用户 Gate 的尝试

至少覆盖：

- 完整 Kitchen AI Concept
- 四级厨房布局探索
- A/B/C 灰盒
- Environment Illustration
- Game Asset Composition
- UI-first Game Scene
- Visual Forensics
- Asset Language Gate
- 蛋窝 / 调味 / 清洁 / 仓库候选
- Lv.2 Before / After Remaster
- Background A/B/C/D
- Overlay
- K1/K3 Architecture Prototype
- Farm F1/F3
- 项目中找到的其他中间实验

## B. 已获用户认可的成功页面

至少覆盖：

- 生意
- 寻访
- 图鉴
- 如果能找到其他明确被用户认可的 UI Remaster，也纳入

**成功和失败必须使用同一分析模板。**

---

# 六、每个案例恢复完整生产链

对每个案例尽可能恢复：

1. 用户 / Chat 给 Work 的上层 Prompt
2. Work 如何解释任务
3. Work 规划了什么步骤
4. 实际执行顺序
5. 使用的工具
6. ImageGen / Image Edit Prompt
7. 使用了哪些输入参考
8. 是否进行了多轮 edit
9. 中间产物
10. 最终产物
11. 用户实际反馈
12. 是否通过用户 Gate

---

# 七、ImageGen Prompt 来源必须分级

- `ORIGINAL`：找到真实原始 Prompt / tool call
- `PARTIAL`：只能恢复部分真实指令
- `RECONSTRUCTED`：只能根据现有证据重建生成意图

RECONSTRUCTED 不能伪装成 ORIGINAL。

同时记录：

- text-to-image / image edit
- 输入参考图
- 是否基于前一张继续 edit
- 是否要求完整背景
- 是否要求完整页面
- 是否只生成 asset
- 后续是否由 HTML/CSS/Runtime 组合

---

# 八、重点分析四类差异

## 1. Prompt 层

- 成功页面是否有更明确的视觉隐喻
- Kitchen / Farm 的 Prompt 是否过多使用“厨房、木房、农场、温馨、cozy”等空间描述
- 不同方向的 Prompt 是否实际上差异很小

## 2. Work 执行层

- 是否先生成完整场景再贴 UI
- 是否先做 UI / 物件 / asset 再组合
- 是否有真正的用户 Gate
- 是否一次改变太多变量

## 3. ImageGen 层

- 是否整图直出
- 是否 reference 过强导致收敛
- 是否连续 edit 导致风格越来越相似
- 是否模型自动补齐完整室内 / 农场环境

## 4. 结果层

同时分析：

- UI 结构
- 构图
- 颜色
- 画风
- 造型
- 描边
- 光照
- 材质
- 空间
- 功能对象与装饰对象关系

---

# 九、特别回答一个核心问题

必须单独回答：

> **为什么生意、寻访、图鉴能够较好地“整图直出”，而 Kitchen / Farm 更容易生成出 AI 味重、构图同质、缺乏游戏 UI 感的结果？**

这个结论必须基于实际生产链和图片证据，而不是泛泛解释。

---

# 十、最终交付

输出：

### 1. History Recovery Report

### 2. Forensics Master Table

### 3. Timeline

### 4. ImageGen Prompt Archive

### 5. Success vs Failure Comparison

### 6. Repeated Failure Patterns

### 7. Successful Production Patterns

### 8. “为什么成功页面可以整图直出，而 Kitchen/Farm 不行”的专项结论

### 9. Evidence Gaps

---

# 停止点

本轮只做取证与对照。

不生成新图。\
不设计新 Kitchen/Farm。\
不提出最终 Production Pipeline。\
不修改 Runtime。\
不主动使用 Computer Use。

如果做不到及时说明情况并停止任务
