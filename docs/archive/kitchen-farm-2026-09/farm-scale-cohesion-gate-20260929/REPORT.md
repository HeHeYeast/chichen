# Farm · Scale + Asset Cohesion Gate

日期：2026-09-29。交付状态：单个正式测试视口，等待用户与 Chat 判断。

## 交付入口

- 评审页：`http://127.0.0.1:4173/web/prototypes/farm-scale-cohesion-gate-20260929/index.html`
- [FARM-SCALE-BIBLE.md](FARM-SCALE-BIBLE.md)：角色、门洞、摆件和单屏容量。
- [ASSET-COHESION-NOTES.md](ASSET-COHESION-NOTES.md)：本轮各类资产的具体修正与遗留差异。
- [正式视口](../../../../web/prototypes/farm-scale-cohesion-gate-20260929/evidence/camp-390x844.png)：390 × 844。
- [等比对照](../../../../web/prototypes/farm-scale-cohesion-gate-20260929/evidence/comparison.png)：塔塔营地 / Test A / Test B / 本轮。
- [同尺度资产核对](../../../../web/prototypes/farm-scale-cohesion-gate-20260929/evidence/same-scale-assets.png)。
- [辅助范围图](../../../../web/prototypes/farm-scale-cohesion-gate-20260929/evidence/scale-overlay.png)。
- [去建筑地面证据](../../../../web/prototypes/farm-scale-cohesion-gate-20260929/evidence/ground-without-buildings.png)。

## 本轮结果

B 中央活动空地型，轻斜俯视。上左农舍、上右补给、下左陈列、下右神社。五只现有鸡宝缩至 24–27 px，四个独立建筑在一个视口中呈现。左右支路从下方入口分流，树簇划出边界；两件生活小物退到路外，中央不靠堆装饰填满。

四个建筑透明原图复用、十二个透明 SVG 模块重绘，程序绘制所有地面。没有使用全场景生成，没有新增 ImageGen 调用。HUD 与五导航只用于判断页面成立，数值为示意，建筑标签不接功能。

## 结论

**足以进入 Farm Layout Freeze Gate 的评审。** 此页已经能够讨论四个入口的相对占地、中央活动区大小、角色尺度和边界关系；这是进入下一轮的建议，不代表已获用户批准或自动冻结。

没有发现必须再发散一套布局才能解决的尺度障碍。后续最重要的资产问题是四建筑原画与原生矢量环境之间的局部渲染差异；布局冻结时也需明确可点击入口和实际走路宽度，不能把本轮静态图当成已经实现的可玩地图。

本轮停止，不扩多视口，不做昼夜，不实装平移 / 缩放，不修改 Kitchen 或正式 Runtime。
