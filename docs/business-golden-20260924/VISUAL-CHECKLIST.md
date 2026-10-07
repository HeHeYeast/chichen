# 营业主页面 · Mockup → Runtime 对照

范围：第二个 Golden Sample，仅营业主页面。图鉴已经通过，此处不重新定义收藏组件；订单、常客、项目和寻访不进入新的页面实现。

## A/B 参考

![确认 Mockup 与实际浏览器运行](../../artifacts/golden-business/mockup-vs-runtime.png)

左图为已确认的简化 Mockup，等比缩放到 390×780；右图为实际浏览器截图，未重绘、未修图。A/B 中的 CP、三种出品、已售和剩余来自隔离演示存档：通过真实营业引擎开张并完成一轮成交，随后在真实应用中显示。

## 逐项检查

| Mockup 特征 | 修改前 Runtime | 修改与检查结果 | 修改后截图 |
|---|---|---|---|
| 角色是主体，卖什么就摆什么 | 表单／小头像列表占主页面 | `active.stock > 0` 对应 catalog 原图；演示剩余鸡宝 6、香煎鸡 4、鸭宝 2，售罄品种移除 | [营业中](../../artifacts/golden-business/active-390x780.png)、[售空移除](../../artifacts/golden-business/sold-out-species-removed.png) |
| 角色坐在篮子里，无白贴纸边 | 没有器皿层次 | B01 篮底→原角色→前沿遮脚；alpha 取景处理透明边界，保持原图比例；暖色轻接触影；未引用收藏白边滤镜 | [营业中](../../artifacts/golden-business/active-390x780.png) |
| 两篮一盘、店名与吊牌，上右菜单 | 矩形功能栏＋配置区 | 同一简化构图，独立 B02 营业牌／菜单架／柜台；主页面仅保留当前状态与操作 | [营业中](../../artifacts/golden-business/active-390x780.png) |
| 木牌和衬布有手绘轮廓与材质 | 普通 UI 卡片 | 三张分类 Asset Sheet 切出 14 张透明 PNG；不使用 CSS 篮子、代码木纹或整页背景图 | [B01](asset-sheets/B01-vessels.png)、[B02](asset-sheets/B02-signs.png)、[B03](asset-sheets/B03-props.png) |
| 短名和数量读起来清楚 | 输入框式数量 | 空白器皿名牌叠实时文字；数字使用已验证的易读字体，避免展示字体中的斜体数字歧义；长名用独立宽标签，两行显示 | [备好三种](../../artifacts/golden-business/prepared-three.png)、[六／八字名称](../../artifacts/golden-business/long-names-320.png) |
| 松散而清楚，少说明文字 | 规则、菜单、偏好、筛选同时暴露 | 库存设置进货篮详情；菜单设置进菜单牌；复杂规则进 `？`。保留小店的角色视觉空间 | [主页面](../../artifacts/golden-business/active-390x780.png)、[备货详情](../../artifacts/golden-business/stock-details.png) |
| 订单纸条／常客簿／项目牌 | 矩形 Tab | B03 三个独立物件；常客簿封面叠已有鸡宝；只改变主页面入口，保留原页面 | [主页面](../../artifacts/golden-business/active-390x780.png) |
| 木牌账单按钮、轻量收摊 | 表单底部操作栏 | 查看真实当前营业账目，数字由原营业模型提供；收摊仍有二次确认，取消无结算 | [本单账单](../../artifacts/golden-business/current-bill.png) |
| 统一的游戏展示字体和问号入口 | 常规 Web 字体为主 | 使用收藏样例已确认的展示字体／正文层级；复用同一圆钮素材和圆润问号，44px 触区 | [主页面](../../artifacts/golden-business/active-390x780.png) |
| **旧 Mockup 底栏需服从最新全局标准** | 整格黄底＋粗下线 | 已通过的导航样式提为全局：手绘柔和底、图标轻抬与放大、文字强调；hover／focus 仍无整格底色 | [主页面](../../artifacts/golden-business/active-390x780.png) |
| 示例图只有三种，真实系统可到六种 | 货物始终表格化 | 两列器皿延长正文，保留角色尺寸；320×568 可滚动到主操作，不压缩所有内容 | [六种](../../artifacts/golden-business/six-stock-320.png)、[滚动到底](../../artifacts/golden-business/six-stock-320-scrolled.png) |
| 未开张不能伪造正在卖的角色 | 有配置但缺场景状态 | 空篮引导备货；筹备／待开张／营业中使用各自文字与牌态；门槛解释在帮助／备货详情 | [空草稿](../../artifacts/golden-business/prepare-empty.png)、[未解锁](../../artifacts/golden-business/locked-320.png) |

## 明确保留的差异

1. 原鸭宝 PNG 的分辨率低于新鸡宝原画，因此运行版放大后的边缘更软；遵循复用已有角色，不 AI 重画来追赶示意图。
2. 正式切分的器皿、木牌与 Mockup 的微观笔触不会像素相等；以独立材质、前后层、比例、暖棕线条与轻阴影收敛视觉语言。
3. 底栏采用用户刚刚通过的全局样式，有意去掉旧营业参考里的矩形选中块。示例数值并非写死，真实存档内容会改变出品数量和摆放。

## 验证证据

- [真实浏览器报告](../../artifacts/golden-business/browser-report.json)：备货留一只／容量、开张前不占库、取消与确认、只读账单、不重复结算、原有三个页面往返、长名称、空状态、六种库存、多个视口。
- [收藏回归](../../artifacts/golden-business/collection-regression/browser-report.json)：收藏功能和所有素材状态通过；帮助／底栏抽取后布局不变。
- [资产保留与离线打包检查](../../artifacts/golden-business/preservation-and-assets.json)：676 个已有美术及字体不变；业务核心、订单／常客／项目代码、厨房／农场场景和发布版本未变；14 个独立 RGBA 资产收进 manifest；完整页面和源素材表不进入运行包。
- 本轮营业／菜单／存档／时间线／项目及收藏相关 **96 项单元测试通过**；原营业组件验收和菜单预设／项目既有回归通过。
- 全部截图由隔离浏览器存档生成，**不声称完成真机验收**，不安装或覆盖玩家游戏。

本轮停止在营业主页面 Golden Sample，等待用户视觉确认。
