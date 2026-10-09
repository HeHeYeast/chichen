# 历史版本与验证证据索引

归档范围：2026-09-08～21。这里只解释被合并版本说明的继承关系，并定位原有证据；没有待执行安装命令，也不构成当前版本验收。当前状态只读[计划](../plan.md)。

| 阶段 | 保留的关键变化与边界 | 原有证据位置（相对项目根） |
|---|---|---|
| v4 | 厨房样板、分层、按压与取消；25项测试历史记录，三只原角色小样，审美未确认 | artifacts/qa/kitchen-v4-sample-375.png |
| v5／v6 | v5只换墙被否决；v6四种床、清洁最高级250修正；28项规则历史记录 | artifacts/qa/kitchen-stages-v6.png |
| v7 | 四房屋接入、6×4与三次升级；28规则／64矩形，未代表用户审美通过 | artifacts/qa/kitchen-stages-v7.png |
| v8 | 农场／图鉴／商店／设置与头像边界；58项历史规则检查，不是171重绘 | web/art/README.md；web/portrait-frames.js |
| 1.0.0 / v9 | Android离线壳、schema2、备份／通知、蒸笼6种；107规则／40存档／47通知 | artifacts/qa/dim-sum-v9/；artifacts/qa/save-ui-v9/ |
| 1.1.0～1.1.1 / v10 | 75材料、鸭蛋2500、10委托；143规则；Magic8 transform底部裁切，zoom诊断农场改善，未证明全部WebView内部成因 | artifacts/qa/magic8-v10/；artifacts/qa/shop-v10/；artifacts/qa/activities-v10/ |
| 1.2.0 / v11 | 签册、8回礼9100、下一批准备与滚动／事务改进；151规则，旧顺序签后来被随机替代 | artifacts/qa/shrine-v11/ |
| 1.3.0 / v12 | 16四时、章回礼2400；164规则；当时全年活动和显式首次制作后来分别被日期过滤、25%偶遇修正 | artifacts/qa/seasonal-v12/ |
| 1.3.1 / v13 | 原作封面，启动直接厨房的历史要求；167规则，已被1.4.3／1.4.6覆盖 | artifacts/qa/launch-v13/ |
| 1.4.0 / v14 | 统一配方册、批售、四时未知25%偶遇；176规则，真机实际交易解释CP变化 | artifacts/qa/recipe-book-v14/；artifacts/qa/magic8-v14/ |
| 1.4.1 | 17节日窗口、最后破壳＋3秒通知、独立后台测试；185规则，长期省电／重启不由即时测试证明 | artifacts/qa/holiday-reminders-v141/ |
| 1.4.2 | 193基础谜面；189规则，旧“无展开答案”后由观察知识分层扩展 | artifacts/qa/discovery-clues-v142/ |
| 1.4.3 | 冷启动封面等待；191规则，Magic8更新前后1050CP／15种／31库存及批次核对 | artifacts/qa/start-screen-v143/；artifacts/qa/magic8-v143/ |
| 1.4.4 | 开始500毫秒防穿透；lastClean累计自然变脏；197规则；24小时基线后来调整 | artifacts/qa/kitchen-v144/ |
| 1.4.5 | 27厨具外观、按比例提前打扫；204规则；当前周期改36–72小时 | artifacts/qa/kitchen-care-v145/ |
| 1.4.6 | 温恢复保留界面；206规则，真机安装与部分可见状态核对不等于完整通知验证 | artifacts/qa/resume-v146/；artifacts/qa/magic8-v146/ |
| 1.4.7 / B04 | 厨具拖动、源码快照、共享校验与发布门禁；220规则／56存档／60通知／29更新 | artifacts/qa/b04-build.log；artifacts/qa/b04-packaged-runtime.json |
| 1.4.8 / ABC | 193描述、10线索修订、3章、B系统、schema3；241规则／70存档／60通知／29更新；安全安装但未逐页真机 | artifacts/device-backups/20260921-090417-acbc8a55/；artifacts/qa/integration-final-node.log |
| 1.5.0候选 | 30单级手艺替换旧树；256规则／76存档／60通知／29更新；追加图标后Magic8覆盖及三页查看 | artifacts/round2-implementation/delivery.md；artifacts/skill-polish/delivery.md；artifacts/device-backups/20260921-230634-cf3752b4/ |

正式APK、校验文件与release.json保留在[发布产物](../../artifacts/releases)；候选与诊断包可能同版本号不同内容，须核对文件摘要。旧QA中的“手机断开”“下次安装某旧版”均为当时状态，不继承为当前行动。

浏览器模拟、JVM替身、静态检查、签名构建、真实安装、存档比较、系统通知和用户审美分别证明不同范围。android/build及release-ui目录可能被后续运行更新，不能凭该通用路径重新声称它仍是某次历史构建的证据；优先用版本目录和对应发布摘要。
