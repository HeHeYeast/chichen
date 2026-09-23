from pathlib import Path
p=Path('README.md');s=p.read_text(encoding='utf-8');intro='''# 鸡宝厨房 · 第二轮优化 / 1.5.0 候选版

[本轮交付报告与30项手艺](artifacts/round2-implementation/delivery.md) · [实际运行前后截图图册](artifacts/round2-implementation/report.html) · [1.5.0 候选安装包](artifacts/round2-implementation/chick-kitchen-1.5.0-candidate.apk)

已实现五方向30项单级手艺、默认草稿分配、招牌／拼盘、接锅／返料／复刻／安心等候和轻装寻访，并重做手艺、寻访、采购与观察界面。旧手艺一次性全额退点，原有收藏、订单、库存与活跃快照保留。256项规则／界面测试、五种窗口64张截图、六组发布门禁及签名候选构建通过。未安装手机；真实迁移、通知链路与长期平衡待进一步验证，详见报告。

---

''';p.write_text(intro+s,encoding='utf-8')
