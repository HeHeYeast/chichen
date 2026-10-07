# 收藏 Golden Sample · Mockup → Runtime 差异收敛

本轮只修改收藏主题样板。A固定为确认的 `mockups/03-collection.png`；B为游戏在浏览器中真实运行的截图。A等比缩至390×780，B按相同视口采集；同为四个已知、两个未知、收录完成／实践未完成。截图没有描画、修图或美化。原图不可直接作为游戏背景。

**[新一轮并排对照](../../artifacts/golden-collection-v2/mockup-vs-runtime.png)** · **[Mockup／上一版／本轮三列对照](../../artifacts/golden-collection-v2/mockup-before-after.png)**

## 视觉差异 checklist

以下“上一版”指本轮开始时保留的Runtime截图。标记表示改动已经接入并在浏览器中检查，不代替用户最终视觉确认。

| 检查 | Mockup特征 | 上一版Runtime | 本轮修改 | 修改后截图 |
|---|---|---|---|---|
| ☑ CharacterSticker尺寸 | 两列角色以真实轮廓呈现；蛋鸡略蓬松、鸭宝略小，脚底接近同一基线 | 用统一图片框容纳不同原图；鸭宝仍受透明边界影响，第一排偏大 | 测量174个原PNG的alpha边界和着色面积，不修改原像素；先按着色面积归一，再按A对蛋鸡×1.07、鸭宝×0.90作有限光学校正；脚底对齐。样板四只真实轮廓约99×97、102×94、103×105、89×94 CSS px | [角色三列放大](../../artifacts/golden-collection-v2/detail-stickers.png) |
| ☑ 白边／阴影 | 连续、明显的奶白贴纸切边；阴影只轻轻托起角色 | 有白边，但图片尺度不同导致相对厚度和位置不统一 | 保持原图深棕线，独立从SourceAlpha外扩4 CSS px；不沿图片矩形画框。白边外暖棕20%软影，模糊2.1px、下移2.5px；原图裁切与白边使用同一真实轮廓 | [角色三列放大](../../artifacts/golden-collection-v2/detail-stickers.png) |
| ☑ 标题／Tab／留白 | 标题组居中偏上，主题标题居中且有浅黄色手绘划线；角色和名字之间留白明确 | 主题标题偏左，Tab后纸页起点低；大角色挤掉上方留白 | 以390×780为基准：主标题36px，主题标题33px，Tab22px；重排标题＋计数，接入独立笔刷划线。两排名字上缘y=272/421，未知槽y=450，印章区y=587，翻页y=645。短屏滚动，不继续压缩间距 | [页头三列放大](../../artifacts/golden-collection-v2/detail-header.png) · [短屏滚动](../../artifacts/golden-collection-v2/browser-320-scrolled.png) |
| ☑ PaperSheet | 暖奶油、细纸纹、柔软不规则边、轻微叠层和卷角 | 上一版素材通过border-image延展后显得薄、平、轮廓规整 | 新G03纸材表切出独立主纸PNG，保留自然边缘、细纤维和短接触影；主纸用独立图片铺放，不再使用CSS border或border-image来表现纸材。背景仅低强度纸感笔触，避免抢角色 | [完整A/B](../../artifacts/golden-collection-v2/mockup-vs-runtime.png) |
| ☑ 胶带 | 半透明米色和撕口，两个压角不遮脸 | 窄小且视觉存在感不足 | G05独立胶带切片，40×16、倾斜约46°，分别压第一、第四角色槽外侧；程序只摆放图片 | [角色细节](../../artifacts/golden-collection-v2/detail-stickers.png) |
| ☑ 收录／实践四态印章 | 收录为双椭圆，实践为不规则圆角章；绿实印／浅砂未完成印，有断墨 | 两种都用椭圆外框；实践图标沿用封面小图，缺少开页书印记 | G04统一制作收录active/inactive、实践active/inactive四个独立切片。实践开页书图符在素材内；收录图符从现有鸡宝提取暗线再着色，未重新画角色。动态文字与完成事实独立绑定；印章轻倾斜 | [四态实际运行截取](../../artifacts/golden-collection-v2/four-stamp-states.png) · [完整完成态](../../artifacts/golden-collection-v2/stamps-completed.png) |
| ☑ 纪念物 | 双层纸签、明显木夹、原鸡宝缩略图和短标题 | 木夹小、层次弱，入口尺度不足 | G05无字双纸＋木夹切片；原角色另叠，80×108入口、角色30×33，单独排字；无AI角色烘焙在纸签里 | [印章／纪念物／翻页对照](../../artifacts/golden-collection-v2/detail-stamps.png) |
| ☑ 字体层级 | 展示字有手写感和粗细变化，名字与标题不像后台正文 | 所有文字主要依赖普通中文无衬线 | 展示文字接入ZCOOL KuaiLe完整字库，统一字重400并按层级轻量加粗；角色名19px、Tab22px、主标题36px。数量、问号采用原字体的清晰粗字形；帮助正文继续原字体。缺字自动回退，未修改其他页面字体 | [页头](../../artifacts/golden-collection-v2/detail-header.png) · [名字](../../artifacts/golden-collection-v2/detail-stickers.png) |
| ☑ UnknownSlot | 浅燕麦色纸片、松软毛边，大圆润问号，文字在下方留出边距 | 边线偏硬，问号小；名称靠近底边 | G03独立未知纸片124×124，取消硬轮廓；问号50px粗字形，标签18px。调整上下位置，保留纸纹和留白；不使用未发现角色轮廓或名称 | [未知槽三列放大](../../artifacts/golden-collection-v2/detail-unknown.png) · [全未知状态](../../artifacts/golden-collection-v2/empty-390.png) |
| ☑ BottomNav | 按最新要求，保留手绘图标，禁止整格黄色Tab与粗棕底线 | 上轮默认选中底已减轻，但全状态及字体仍需核对 | 收藏样板内固定透明格底；默认／hover／focus／按下均检查。当前图标1.09倍、上浮3px，背后49×24手绘淡黄刷痕，文字加深；reduce-motion保留底和字重。格子80px高，仍复用旧导航图 | [底栏三列对照](../../artifacts/golden-collection-v2/detail-nav.png) |

## 实测锚点

运行截图坐标来自DOM真实布局，不是手工画的参考框。参考图为视觉比对，未宣称逐像素误差率。

| 项目 | 当前390×780运行值 |
|---|---|
| 原鸡宝alpha轮廓 | x68.17 / y169.34 / 98.66×96.66 |
| 香煎鸡alpha轮廓 | x233.52 / y172.14 / 101.95×93.86 |
| 荷包蛋鸡alpha轮廓 | x65.97 / y310.09 / 103.05×104.91 |
| 鸭宝alpha轮廓 | x239.92 / y321.14 / 89.16×93.86 |
| 两排脚底 | y266 / y415 |
| 两排名字上缘 | y272 / y421 |
| 未知槽 | 124×124，上缘y450 |
| 印章区／翻页／底栏 | y587 / y645 / y700 |

白边、软影不计入角色alpha轮廓尺寸。遇到六个已知角色时纸页加高14px，保留名字到印章之间的空隙；在较矮屏幕内滚动，而非压缩三个角色行。

## 素材与字体交付

- **G03**：主纸、未知纸，2片。
- **G04**：收录完成／未完成、实践完成／未完成，4片。
- **G05**：胶带、两种Tab、置顶书签、木夹纪念纸、手绘刷痕、圆钮、翻页纸钮、安静背景笔触，9片。
- [源素材表与生成提示](asset-sheets/revision-2/prompts.json)：使用内置imagegen，参考确认Mockup；均为无界面文字的类别素材表。
- [运行切片manifest](../../web/art/golden-collection/manifest.json)：15个当前样板素材，记录源表、裁切、尺寸、透明边、sha256；上一轮12片标记superseded，不再打入运行包。
- [展示字体官方来源](https://github.com/googlefonts/zcool-kuaile)，随包保留OFL许可。完整7053字形；对当前中文内容与原字体回退联合检查，无缺字。未将字形转为图片。
- 原角色、厨房、农场等734个既有位图与本轮之前备份核对，变化0。没有生成新角色，也没有替换旧场景。

## 验证与停止位置

29项图鉴／收藏相关测试通过；浏览器检查通过：多尺寸、短屏滚动、全未知、六角色、四态章、置顶、档案返回、分类入口、帮助与减少动效。浏览不更改库存、CP或收藏奖励。离线资源打包成功，未构建或安装新手机包。

证据：[浏览器报告](../../artifacts/golden-collection-v2/browser-report.json)、[锚点测量](../../artifacts/golden-collection-v2/landmarks.json)、[对比与旧资产hash](../../artifacts/golden-collection-v2/comparison-evidence.json)、[字库报告](../../artifacts/golden-collection-v2/font-report.json)。

本轮结果是基于A/B的视觉语言收敛，不宣称像素级复刻。保留的差异包括：原角色表情和细节、原鸭宝源图清晰度，以及真实可显示字体与AI手写字形的差别；底栏则按最新要求主动取消参考图中的整格选中底。当前对照为浏览器实际运行，非真机。

**停在收藏Golden Sample，等待本轮视觉确认。生意、寻访、厨房、农场未扩展实现。**

### 两处局部修正

根据后续圈选，帮助问号改用原中文字体的圆润粗字形，在原纸钮中居中；置顶书签下移10px，使图钉完整落入滚动容器可视区域，避免顶端被截断。未改主页面间距和美术素材。浏览器回归通过，并增加置顶前后书签完整可见的检查。

[两处修正前后截图](../../artifacts/golden-collection-detail-fix/two-details-before-after.png) · [完整运行截图](../../artifacts/golden-collection-detail-fix/browser-390x780.png)
