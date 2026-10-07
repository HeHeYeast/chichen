from pathlib import Path
import json,csv,shutil,hashlib,html
from PIL import Image
ROOT=Path(__file__).parent; REPO=ROOT.parent.parent; E=ROOT/'evidence'
items=[]
sources={
'TS1':('官方活动攻略','https://www.taptap.cn/moment/846445543147177753','2026-09-08'),
'TS2':('玩家挖宝攻略','https://www.taptap.cn/moment/846733139903513042','2026-09-09'),
'TS3':('玩家角色列表','https://www.taptap.cn/moment/852581458705908228','2026-09-25（图片地址日期）'),
'TS4':('官方猫冰宣传卡','https://www.taptap.cn/moment/846353291242635810','2026-09-08'),
'TS5':('3DM 图文实机教程','https://shouyou.3dmgame.com/android/560350.html','2026-08-14；设置图内可见 2026-07-29 / global build'),
'TS6':('3DM 成长说明','https://shouyou.3dmgame.com/gl/635361.html','2026-09-07'),
'MS1':('旧研究登记的玩家帖（本轮未成功重开）','https://www.taptap.cn/moment/101063887486452480','旧研究采集 2026-09-15；截图游戏版本未明'),
'MS2':('旧研究登记的官方商店组','https://www.taptap.cn/app/162127','旧研究采集 2026-09-15；逐图 CDN 未留存'),
'MS3':('游民星空角色指南','https://wap.gamersky.com/news/Content-1357180.html','2021-01-22'),
'JS':('项目浏览器截图留档','','2026-09-24—25 系列；不是本轮真机采集')}
def add(id,game,title,page,kind,source,file,observation,limit,origin=''):
 p=E/file;im=Image.open(p)
 s=sources[source]
 items.append(dict(id=id,game=game,title=title,page=page,kind=kind,source_id=source,source_title=s[0],source_url=s[1],source_date=s[2],file='evidence/'+file,origin=origin,width=im.width,height=im.height,sha256=hashlib.sha256(p.read_bytes()).hexdigest(),observation=observation,limit=limit))
t=[
('01','活动道具图标横条','资产','官方说明图','TS1','event-entry','五枚图标使用粗深边、白外圈与青色偏移底。','不是主界面入口截图；文件名 entry 沿用采集名。'),
('02','长跑活动角色局部','活动','官方局部','TS1','event-running','角色组合保留夸张外形。','244×298 局部，不能证明完整活动 UI。'),
('03','挖宝棋盘与组队卡','活动','玩家拼图','TS2','treasure-guide-1','方格棋盘、四个组队卡与钥匙进度把玩法变成离散槽位。','紫底标题、白色段落及红箭头属于攻略排版。'),
('04','挖宝进度与标旗状态','活动','玩家拼图','TS2','treasure-guide-2','同一棋盘从完整到缺块；标旗气泡依附目标格。','没有完整屏幕和弹窗切换过程。'),
('05','塔塔持有列表','角色','玩家实机','TS3','player-roster','四列卡片固定承载元素、类型、星数、等级和进度；底部筛选悬浮。','这是持有角色列表，不是全图鉴；点击行为未实测。'),
('06','猫冰官方宣传卡','资产','官方海报','TS4','character-poster','冰块身体、猫耳与巨大眼睛组合；粗深蓝边、硬色块。','“塔塔图鉴”是宣传标题，不是游戏图鉴页。'),
('07','营地英文版','主场景','媒体实机','TS5','home-en','深森林包住浅色空地；底角按钮有独立高对比底座。','旧全球版本；右侧红圈为教程标注，不能作 UI。'),
('08','个人与好友页','二级页面','媒体实机','TS5','profile-en','头像区、编队槽、标签页、名单卡、邀请按钮分层。','低分辨率旧全球服样本，不外推现版本所有菜单。'),
('09','设置弹窗','设置','媒体实机','TS5','settings-en','背景暗化与模糊，奶白面板、紫色行按钮、底部独立关闭。','缺少开关切换前后；局部模糊不能等同全场景柔光。'),
('10','语言选择弹窗','弹窗','媒体实机','TS5','language-popup','重复行按钮与固定关闭沿用设置弹窗语法。','底部列表裁切说明有延伸内容，滚动方式未实测。'),
('11','营地中文状态','主界面','媒体实机','TS5','home-cn','功能集中在林间亮区；底部战斗、塔塔、弹珠台明确。','同组全球服切换中文截图，并非独立国服证据。'),
('12','章节路线页','二级页面','媒体实机','TS5','battle-select','蛇形路径上的节点与编号；底部宽黄按钮标明开始战斗。','不能据此认定营地必须同样使用路线布局。'),
('13','战斗进行中','主场景','媒体实机','TS5','battle','森林被压成两侧边界，中间长条留给战斗单位与进度。','动态速度、点击热区与遮挡时长未实测。'),
('14','胜利覆盖层','弹窗','媒体实机','TS5','result','暗化场景、黄色胜利横幅、底部返回/继续构成三层。','不是居中卡片弹窗，证明同系统允许不同覆盖层。'),
('15','共享等级成长面板','成长页','媒体局部','TS6','candy','巨大等级数、单进度条、三个强化块；橙黄与白分上下语义区。','是成长局部，不是完整糖果机操作页。'),
('16','增益徽章网格局部','成长页','媒体局部','TS6','growth','三列徽章每格包含角色轮廓与固定黑色增益牌。','旧采集名 growth；不能证明角色详情或升星确认界面。')]
for n,title,page,kind,s,f,o,l in t:add('T'+n,'塔塔冒险队',title,page,kind,s,'T'+n+'-'+f+'.jpg',o,l)
m=[
('打工总览与生产卡','主界面','建筑上方主题装饰巨大；下方店铺卡重复组织角色、计时、产物与加班。','上方两个建筑变暗的具体原因未知，不能直接判定未解锁。'),
('角色房间、技能与角色册','角色','角色在左，装备陈列在上，相框在中，下部是角色网格与技能夹板。','旧文件名“打工排班”不准确；画面支持角色页，不支持排班交互结论。'),
('角色房间另一选中状态','角色','同一布局容纳不同角色，卡片的位置与数值字段复用。','旧文件名“情缘激活”不能证明状态变化的触发原因。'),
('角色列表筛选面板','二级页面','筛选浮层用两排元素/类型图标；角色房间与列表仍可见。','旧文件名“战斗力与等级面板”不完整，应识别为筛选状态。'),
('委托纸板弹窗','任务','三列委托便签、下方当前详情与提交；任务/委托用垂下标签切换。','这里只看到委托标签选中；任务页内容缺证。'),
('观光团布展选择','二级页面','五个加号槽悬在展示区；下方书册格子选宠物；返回与出发分两端。','橙色手绘圈属于攻略标注；不是限时活动主页证据。'),
('周常商店','商店','不同体积道具共享货架槽与价格牌，刷新规则写在横向招牌上。','购买确认、售罄、货币不足的完整状态缺证。'),
('挑战教学图','二级页面','图标和关系箭头承担规则说明。','属于说明画面；不是战斗实机，完整帮助关闭结构缺证。'),
('神话星全景宣传','主场景','小星球承载密集地标，不遵守真实建筑比例。','官方宣传包装遮住部分 HUD，不能量测完整主界面。'),
('神话星奖池宣传','活动','舞台与角色展示占中央。','常驻奖池宣传不是限时活动完整截图。'),
('探索区域选择宣传','二级页面','单色地图轮廓、节点与页签压缩地理信息。','营销框与前景角色不属于可证实的运行 UI。'),
('家园装饰宣传','主场景','斜俯视房间保留成套家具与重复装饰。','是完整空间反例；装修用途不能直接替代经营主界面。'),
('幽灵主题群像宣传（纠错）','资产','群像与外框讲主题，未见可确认收藏格。','旧名“图鉴收集网格”错误，不能计入图鉴覆盖。'),
('章节挑战宣传','二级页面','战斗信息被放进挂页状面板。','面板被营销前景遮挡，CTA 不完整。'),
('星际探险全景宣传','主场景','小星球地标向观者展开，建筑不按远近缩到不可读。','仅支持画风和大构图，不能证明当前首页入口集合。'),
('幽灵主题主视觉','资产','群像、强光与氛围表现服务宣传。','不提取主页面光照规范，不用于证明 UI 系统。')]
old=sorted((REPO/'docs/archive/mqxq-research/screenshots').glob('*.jpg'))
for i,(title,page,o,l) in enumerate(m,1):
 fn=f'M{i:02d}-archive.jpg';shutil.copyfile(old[i-1],E/fn)
 add(f'M{i:02d}','妙奇星球',title,page,'旧档实机' if i<=8 else '旧档官方宣传','MS1' if i<=8 else 'MS2',fn,o,l,str(old[i-1].relative_to(REPO)))
extra=[
('稀有度色框说明','资产','五种色框共用头像符号。','说明图不代表整个收藏页。'),
('五类角色头像例','资产','同尺寸头像槽容纳不同头型与帽子。','水平截取，仅看卡框/轮廓。'),
('不夜城抽取舞台','二级页面','上部舞台呈现角色，下部双购买槽明确单次/多次成本。','局部图，HUD 未完整；文案数值为旧版。'),
('奖池内容收集网格','图鉴 / 收藏','四列卡片保留未拥有剪影；“已有”角标和日常/活动切换可见。','是奖池内容列表，不等于总图鉴；当前全图鉴仍缺。'),
('兑换不足弹窗','弹窗','兑换所需数值突出，底部灰色按钮明确不足状态。','旧版静态图，触发路径来自文章说明。'),
('角色成长预览面板','成长页','左头像右属性，底部技能纸条，左右箭头与两档预览。','抠出的面板，升级按钮与背景关系不可见。'),
('角色装备与升级弹窗','成长页','房间装备入口对应编号 Tab、材料槽与蓝色升级按钮。','媒体左右拼图；不能测量其全屏占比。'),
('角色皮肤相框入口','角色','三个相框是固定槽；未获得用剪影，顶上感叹号提示。','红色矩形是文章标注，不是游戏组件。'),
('皮肤挑战二级弹窗','弹窗','左剪影与加成，右故事纸页，下方材料数量与挑战按钮。','旧版截图；不确认当前挑战机制与按钮点击结果。')]
for i,(title,page,o,l) in enumerate(extra,17):add(f'M{i:02d}','妙奇星球',title,page,'媒体局部' if i in [17,18,22,23] else '媒体实机','MS3',f'M{i:02d}-guide.jpg',o,l)
j=[
('生意：营业中','主界面','artifacts/golden-business-r3/active-390x844.png','三个篮子形成 2+1 排列，柜台线把商品与管理区分开。','本轮用户认可页面族；具体截图不是本轮重新确认的版本。'),
('生意：待开张空态','主界面','artifacts/golden-business-r3/prepare-empty.png','空篮仍占视觉中心，空位与加号显示下一步。','静态留档；按钮可用性不在本轮验证范围。'),
('生意：当前账单','弹窗','artifacts/golden-business-r3/current-bill.png','数字分栏、进度提示和逐项明细放入浅纸色面板。','比主页面更偏通用信息表单；不能把主页面成功等同所有弹窗成熟。'),
('寻访：世界地图','主场景','artifacts/journey-precision/final/map-390.png','地标与文字牌绑定；单条虚线贯穿地图；底部行程卡单独承载状态。','有连续环境；不支持“所有场景必须切断空间”的结论。'),
('寻访：地区页','二级页面','artifacts/journey-precision/final/region-390.png','地点二列、行为三列、同行三列与准备出发按区分层。','UI 行为命名可读，真实触控与动态变化未本轮实测。'),
('寻访：归来','二级页面','artifacts/journey-precision/final/return-390.png','角色一排、大信封发现物、少量收益、上下主次操作。','不是普通奖励列表，每次发现物变化范围仍需历史资料。'),
('图鉴：收藏册','图鉴 / 收藏','artifacts/golden-collection-detail-fix/browser-390x844.png','二列三行贴纸网格；问号空位、进度、印章和分页同时存在。','此页是收藏标签；品种标签完整结构不由此推断。'),
('图鉴：全未知','图鉴 / 收藏','artifacts/golden-collection-detail-fix/empty-390.png','六个未知格保持册页布局，印章降低强调。','无法从静态图判断点击未知格的反馈。'),
('图鉴：双印章状态','图鉴 / 收藏','artifacts/golden-collection-detail-fix/stamps-completed.png','收录与实践使用同族但不同图标的绿色印章。','4/6 与双印章共同出现的业务语义需另查；这里仅比视觉状态。'),
('生意：订单','任务','artifacts/business-family/orders-regulars/orders-active-390.png','夹板形成内容边界，交付按钮靠近当前订单。','页面族二级页为辅助证据，不扩大用户审美认可范围。'),
('生意：项目','成长页','artifacts/business-family/projects/projects-list-390.png','招牌册对象居中，阶段数和继续筹备沿竖轴排列。','长页在视口下方延伸，不是所有内容一屏显示。')]
for i,(title,page,origin,o,l) in enumerate(j,1):
 fn=f'J{i:02d}.png';shutil.copyfile(REPO/origin,E/fn);add(f'J{i:02d}','鸡宝厨房',title,page,'项目浏览器留档','JS',fn,o,l,origin)
(ROOT/'evidence-index.json').write_text(json.dumps(items,ensure_ascii=False,indent=2),encoding='utf-8')
with (ROOT/'evidence-index.csv').open('w',encoding='utf-8-sig',newline='') as f:
 w=csv.DictWriter(f,fieldnames=list(items[0]));w.writeheader();w.writerows(items)
def esc(t):return html.escape(str(t),quote=True)
cards=[]
for a in items:
 source=f'<a href="{esc(a["source_url"])}" target="_blank" rel="noreferrer">{esc(a["source_title"])}</a>' if a['source_url'] else esc(a['source_title'])
 cards.append(f'''<article id="{a['id']}" data-game="{esc(a['game'])}" data-page="{esc(a['page'])}" data-kind="{esc(a['kind'])}"><header><small>{a['id']} · {esc(a['kind'])}</small><h2>{esc(a['title'])}</h2></header><a href="{a['file']}" target="_blank"><img loading="lazy" src="{a['file']}" alt="{esc(a['title'])}"></a><div class="note"><p><b>可见：</b>{esc(a['observation'])}</p><p class="limit"><b>边界：</b>{esc(a['limit'])}</p><p class="source">{source}<br>{esc(a['source_date'])} · {a['width']}×{a['height']}</p></div></article>''')
options=lambda key:''.join(f'<option>{esc(s)}</option>' for s in sorted(set(a[key] for a in items)))
doc='''<!doctype html><html lang="zh-CN"><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>鸡宝厨房 · UI / Art Reference Atlas</title><style>
*{box-sizing:border-box}body{margin:0;background:#eeeee8;color:#222d30;font:16px/1.65 "Microsoft YaHei",sans-serif}main{max-width:1560px;margin:auto;padding:30px}h1{font-size:32px;margin:8px 0}h2{font-size:18px;margin:4px 0}a{color:#176961}small,.source{font-size:12px;color:#59686b}nav{display:flex;flex-wrap:wrap;gap:12px;align-items:center;background:#fff;padding:16px;margin:24px 0;position:sticky;top:0;z-index:2;border-bottom:2px solid #176961}select,input{font:inherit;padding:7px;border:1px solid #889992;border-radius:4px;max-width:100%}.grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(285px,1fr));gap:20px}article{background:white;border:1px solid #c8d0cb;min-width:0;scroll-margin-top:100px}article header{padding:15px 18px;border-bottom:1px solid #ddd}article img{display:block;width:100%;height:450px;object-fit:contain;background:#e3e5e2;padding:8px}.note{padding:0 18px 14px}.note p{font-size:14px}.limit{color:#8a542e}article[hidden]{display:none}.intro{max-width:1050px}.notice{border-left:4px solid #b37532;padding:10px 18px;background:#fff5e4}.links{display:flex;gap:20px;flex-wrap:wrap}button{padding:8px 14px;font:inherit;border:1px solid #889992;background:#fff;cursor:pointer}footer{padding-top:28px;color:#66736e}@media(max-width:600px){main{padding:16px}h1{font-size:25px}nav{position:static}article img{height:480px}}
</style><main><div class="intro"><small>RESEARCH / 2026-09-28 / EVIDENCE FIRST</small><h1>《鸡宝厨房》UI + Art Reference Atlas</h1><p>对照塔塔冒险队、妙奇星球与本项目生意／寻访／图鉴。点击图片查看原尺寸。页面不是新的 Kitchen/Farm 方案，也不代表已经确认生产规范。</p><div class="notice">来源分级保留在每张卡上。宣传海报、攻略拼图和局部不能代替完整实机。旧档 M02–04 页面类型已纠正，M13 与 T06 都不计入真实图鉴页覆盖。跨年代、跨地区样本不拼成同一版本。</div><p class="links"><a href="REPORT.md">完整研究报告</a><a href="MISSING-EVIDENCE.md">手机补图清单</a><a href="evidence-index.csv">证据索引 CSV</a><a href="page-type-matrix.csv">页面矩阵 CSV</a><a href="evidence/grammar-plate.png">五组构图标注</a></p></div>
<nav><label>游戏 <select id="game"><option value="">全部</option>'''+options('game')+'''</select></label><label>页面 <select id="page"><option value="">全部</option>'''+options('page')+'''</select></label><label>来源 <select id="kind"><option value="">全部</option>'''+options('kind')+'''</select></label><label>检索 <input id="search" placeholder="如：剪影、物件、弹窗"></label><button id="reset">重置</button><output id="count" aria-live="polite"></output></nav><div class="grid">'''+''.join(cards)+'''</div><footer>公开图片版权属于对应权利人；本资料用于内部分析，不作为可直接投产的资产。观察是本轮视觉分析，设计意图均需与报告中的推断标记结合阅读。</footer></main><script>
const cards=[...document.querySelectorAll('article')];const controls=['game','page','kind','search'].map(x=>document.getElementById(x));function filter(){let n=0;cards.forEach(c=>{let ok=controls.slice(0,3).every(e=>!e.value||c.dataset[e.id]===e.value)&&c.textContent.toLowerCase().includes(controls[3].value.toLowerCase());c.hidden=!ok;if(ok)n++});document.getElementById('count').textContent=n+' / '+cards.length+' 张'}controls.forEach(e=>e.addEventListener('input',filter));document.getElementById('reset').addEventListener('click',()=>{controls.forEach(e=>e.value='');filter()});filter();</script></html>'''
(ROOT/'atlas.html').write_text(doc,encoding='utf-8')
print(f'Atlas built: {len(items)} evidence cards')
