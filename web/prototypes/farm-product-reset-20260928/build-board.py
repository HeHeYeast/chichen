from pathlib import Path
import json

ROOT = Path(__file__).resolve().parent
DATA = {
 'a': {'title':'农舍营地型','tag':'先回家，再办事','center':[490,485],'near':[490,420],
 'nodes':[
  ['house','农舍 · 收成',330,185,310,235,'主屋承接收成表：库存、预留、出售及详情。'],
  ['shop','补给摊',120,490,170,145,'沿用小卖部，默认厨具页；不新增集市交易。'],
  ['shrine','神社',730,330,155,150,'同一建筑进入选择层，再分委托簿与御神签。'],
  ['display','陈列亭',690,735,180,140,'沿用纪念物与三个陈列位；这里仅显示空槽。'],
  ['repair','整修工具台',310,660,115,85,'附属于农舍：查看完好度、费用，再按原规则整修。']],
 'path':'M 490 1030 Q 475 850 490 660 L 490 425 M 490 555 Q 390 570 205 650 M 490 565 Q 670 565 805 495 M 490 705 Q 620 875 780 895 M 485 765 L 368 765',
 'yard':[510,565,150,105],'walkers':[[450,520],[550,580],[475,620]],
 'summary':'主屋最大，前院聚拢伙伴；补给与工具台形成服务侧翼，神社和陈列退到支路。',
 'risk':'防止主屋过大遮住伙伴，也防止退回“上沿房屋＋下方草地”。'},
 'b': {'title':'中央活动空地型','tag':'先看伙伴，再沿路逛','center':[500,555],'near':[500,555],
 'nodes':[
  ['house','农舍 · 收成',125,180,225,180,'西北农舍保留收成主功能，定位菜单可直接聚焦。'],
  ['shop','补给摊',700,220,165,145,'东北补给摊：环路短支路连接摊前。'],
  ['shrine','神社',760,690,150,155,'东南静区：委托簿与御神签仍分开命名。'],
  ['display','陈列亭',140,730,175,130,'西南散步区：三个空槽，纯展示。'],
  ['repair','整修工具台',105,465,110,85,'西侧服务支路上的小设施，不挤占中央活动区。']],
 'path':'M 500 1030 L 500 900 C 250 900 270 730 270 555 C 270 345 335 395 500 395 C 660 395 700 420 700 555 C 700 765 700 900 500 900 M 305 428 L 240 380 M 685 440 L 780 385 M 680 865 L 835 865 M 300 840 L 230 880 M 270 570 L 160 570',
 'yard':[500,575,165,165],'walkers':[[450,495],[550,555],[480,650]],
 'summary':'中央草地留给伙伴，四周建筑用环路串联；先读生活场景，再选择办事目的地。',
 'risk':'低库存或夜间中央更显空，近景找外围建筑需要清楚的定位入口。'}
}

def building(n):
    id,label,x,y,w,h,_=n
    color={'house':'#c88e70','shop':'#b5be9b','shrine':'#b99187','display':'#bfae87','repair':'#b4afa1'}[id]
    s=f'<g class="structure" data-kind="{id}" transform="translate({x} {y})">'
    s+=f'<ellipse cx="{w*.54}" cy="{h*.98}" rx="{w*.52}" ry="{h*.10}" fill="#788578" opacity=".2"/>'
    if id=='repair':
        s+=f'<path d="M 6 32 L {w-8} 32 L {w} 48 L 18 48 Z" fill="{color}" stroke="#625e51" stroke-width="4"/><path d="M 18 48 V {h} M {w-10} 48 V {h}" stroke="#625e51" stroke-width="7"/><path d="M 45 15 L 65 34 M 56 7 L 39 22" stroke="#625e51" stroke-width="7"/>'
    else:
        s+=f'<path d="M 18 {h*.42} L {w-24} {h*.42} L {w-24} {h*.92} L 18 {h*.92} Z" fill="#f1e8d1" stroke="#625e51" stroke-width="4"/>'
        s+=f'<path d="M {w-24} {h*.42} L {w} {h*.29} L {w} {h*.77} L {w-24} {h*.92} Z" fill="#cfc6af" stroke="#625e51" stroke-width="4"/>'
        s+=f'<path d="M 0 {h*.4} L {w*.18} 0 L {w*.84} 0 L {w} {h*.29} L {w-24} {h*.45} Z" fill="{color}" stroke="#625e51" stroke-width="4" stroke-linejoin="round"/>'
        s+=f'<path d="M {w*.18} 0 L {w*.30} {h*.43}" stroke="#625e51" stroke-width="3" opacity=".5"/>'
        if id in ['house','shrine']:
            s+=f'<rect x="{w*.39}" y="{h*.61}" width="{w*.22}" height="{h*.31}" rx="8" fill="#8e8976"/><path d="M {w*.30} {h} H {w*.70}" stroke="#a49b83" stroke-width="9"/>'
        else:
            for i in range(3):
                s+=f'<rect x="{w*(.17+i*.23)}" y="{h*.62}" width="{w*.16}" height="{h*.19}" rx="3" fill="none" stroke="#968b72" stroke-width="3" stroke-dasharray="5 4"/>'
    return s+'</g>'

def svg(key):
    d=DATA[key]
    s='<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1000 1100" role="img" aria-label="'+d['title']+'低保真布局蓝图"><style>text{font-family:Microsoft YaHei,sans-serif;fill:#484d42}.label{font-size:23px;font-weight:700}.note{font-size:20px;fill:#7a806e}</style>'
    s+='<defs><pattern id="grid" width="50" height="50" patternUnits="userSpaceOnUse"><path d="M 50 0 H 0 V 50" fill="none" stroke="#819078" opacity=".09"/></pattern></defs>'
    s+='<rect x="22" y="22" width="956" height="1056" rx="110" fill="#d9dfc9" stroke="#9ca78c" stroke-width="3" stroke-dasharray="10 9"/><rect x="22" y="22" width="956" height="1056" rx="110" fill="url(#grid)"/>'
    # Flat boundary symbols: these are plan placeholders, not final trees.
    for x,y,r in [(110,110,53),(185,85,39),(830,110,43),(907,180,40),(70,350,34),(945,590,31),(80,850,40),(110,965,45),(240,1010,33),(835,995,44),(915,905,40)]:
        s+=f'<circle cx="{x}" cy="{y}" r="{r}" fill="#a6b699" stroke="#899c7a" stroke-width="3"/><path d="M {x-10} {y} H {x+10} M {x} {y-10} V {y+10}" stroke="#899c7a" stroke-width="2"/>'
    s+='<text x="75" y="55" class="note">林缘 / 浏览边界</text><text x="880" y="70" class="note">北 ↑</text>'
    s+=f'<path d="{d["path"]}" fill="none" stroke="#b8a888" stroke-width="60" stroke-linejoin="round" stroke-linecap="round"/><path d="{d["path"]}" fill="none" stroke="#e7d6b5" stroke-width="51" stroke-linejoin="round" stroke-linecap="round"/>'
    cx,cy,rx,ry=d['yard']
    s+=f'<ellipse cx="{cx}" cy="{cy}" rx="{rx}" ry="{ry}" fill="#ecedd8" stroke="#a6b091" stroke-width="3" stroke-dasharray="9 8"/>'
    s+=f'<text x="{cx}" y="{cy+ry-20}" text-anchor="middle" class="note">'+('伙伴前院' if key=='a' else '中央伙伴活动地')+'</text>'
    for n in sorted(d['nodes'],key=lambda n:n[3]+n[5]):s+=building(n)
    for x,y in d['walkers']:
        s+=f'<g class="partner" transform="translate({x} {y})"><ellipse cx="0" cy="12" rx="21" ry="8" fill="#8e9d77" opacity=".3"/><ellipse cx="0" cy="-6" rx="18" ry="21" fill="#ead9a3" stroke="#79735e" stroke-width="3"/><circle cx="-6" cy="-9" r="2" fill="#625e51"/><circle cx="6" cy="-9" r="2" fill="#625e51"/><path d="M -4 -2 H 5 L 0 3 Z" fill="#b39166"/></g>'
    for id,label,x,y,w,h,_ in d['nodes']:
        s+=f'<g class="static-label"><rect x="{x+w/2-82}" y="{y+h+15}" width="164" height="38" rx="6" fill="#fffbee" stroke="#a29b85" stroke-width="2"/><text x="{x+w/2}" y="{y+h+41}" text-anchor="middle" class="label">{label}</text></g>'
    s+='<text x="500" y="1060" text-anchor="middle" class="note">南入口 · 不是新页面入口</text></svg>'
    return s

for key in DATA:(ROOT/f'direction-{key}.svg').write_text(svg(key),encoding='utf-8')
(ROOT/'layout-data.js').write_text('export const layouts='+json.dumps(DATA,ensure_ascii=False)+';\n',encoding='utf-8')

cards=''
for k,d in DATA.items():
    cards+=f'''<article class="direction" data-direction="{k}">
      <header class="direction-head"><span class="letter">{k.upper()}</span><div><h2>{d['title']}</h2><p>{d['tag']}</p></div></header>
      <p class="summary">{d['summary']}</p>
      <div class="camera-tools" aria-label="{k.upper()} 镜头控制"><button data-mode="far">远景</button><button data-mode="default" aria-pressed="true">默认</button><button data-mode="near">近景</button><button data-mode="home">归位</button><label>定位 <select aria-label="{k.upper()} 定位建筑"><option value="">选择建筑</option>{''.join(f'<option value="{n[0]}">{n[1]}</option>' for n in d['nodes'])}</select></label></div>
      <div class="phone"><div class="hud"><strong>农场</strong><span>完好 100% <small>示例</small></span><span>设置</span></div>
      <div class="map-viewport" tabindex="0" aria-label="{k.upper()} 营地地图，可拖动、滚轮或双指缩放"><div class="world">{svg(k)}</div><div class="hotspots"></div><span class="map-hint">低保真 · 固定布局</span></div>
      <div class="zoom-bar"><button data-zoom="out" aria-label="{k.upper()} 缩小">−</button><output>1.45×</output><button data-zoom="in" aria-label="{k.upper()} 放大">＋</button><span>拖动浏览 · 双指缩放</span></div>
      <nav class="mock-nav" aria-label="五栏位置示意，不跳转"><span>厨房</span><b>农场</b><span>生意</span><span>寻访</span><span>图鉴</span></nav></div>
      <div class="selection" aria-live="polite"><strong>点建筑，查看功能归属</strong><p>示意入口不执行游戏操作。伙伴圆点只表达站位，不代表真实库存数量。</p></div>
      <p class="risk"><b>风险</b> {d['risk']}</p>
      <a class="download" href="direction-{k}.svg" target="_blank">查看完整布局图 ↗</a>
    </article>'''

html='''<!doctype html><html lang="zh-CN"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>鸡宝厨房 · Farm Product Reset</title><link rel="stylesheet" href="board.css"></head><body>
<main><header class="intro"><div class="eyebrow">鸡宝厨房 / FARM / 2026.09.28</div><div class="gate">REFERENCE + PRODUCT RESET GATE</div><h1>同一个营地，两种生活重心。</h1><p class="lead">固定建筑布局、可平移与缩放的轻俯视伙伴营地。农舍承接收成管理，次要事务拥有自己的场所。</p><p class="boundary">布局提案 · 等待评审　／　建筑、色块与角色均为占位示意，不是最终美术。</p></header>
<section class="reference"><h2>从参考中带走的，是组织与画法。</h2><div class="principles"><div><b>01　地面连续</b><p>无地平线；路径接到建筑门前。功能群组之间用空地分隔。</p></div><div><b>02　轮廓分层</b><p>建筑和角色强，树石次之，草地最弱；不让纹理和功能争位置。</p></div><div><b>03　块面有体积</b><p>屋顶、立面、侧面分色，脚底短影。材质用少量符号说明。</p></div><div><b>04　保持鸡宝语言</b><p>暖浅底、深棕边、角色表情与绿色状态；不复制塔塔活动横幅或数字圈。</p></div></div></section>
<section class="decision"><b>本轮建议先评审 A</b><span>A 更直接承接收成管理；B 更强调看伙伴、逛营地。两者功能完全相同。</span></section>
<section class="comparison">'''+cards+'''</section>
<section class="spec"><h2>镜头改变观察尺度，不改变玩法。</h2><div class="spec-grid"><div><h3>远景 · 1.0×</h3><p>看完整营地、四处功能地标和路径。短名保留，收起细节与重复状态。</p></div><div><h3>默认 · 1.45×</h3><p>A 聚焦主屋与前院；B 聚焦中央伙伴活动地。定位可发现视野外建筑。</p></div><div><h3>近景 · 2.2×</h3><p>看建筑与伙伴的关系，双轴平移观察局部。HUD 与入口文字保持可读尺寸。</p></div></div><p>统一保留：完好度、全局导航、缩放、归位与定位。未来场景同屏至多一枚主动待办提示。建筑不能拖动，也没有自由装修。</p></section>
<section class="reset"><h2>保留功能，重置空间。</h2><div class="reset-row"><b>4 个目的地</b><p>农舍 → 收成表　／　补给摊 → 小卖部　／　神社 → 委托簿、御神签　／　陈列亭 → 三个纪念物位</p></div><div class="reset-row"><b>1 处附属设施</b><p>整修工具台与全局完好度状态。沿用 CP 与完好度规则，不新增升级工坊。</p></div><div class="reset-row"><b>1 项口径修正</b><p>在家数量应排除外出和营业占用；可安排数量再扣订单预留。草地是代表性展示，不能数精灵当库存。</p></div></section>
<footer>STOP HERE / 本轮止于产品定义与布局蓝图。选方向后再做局部画风样张，不进入最终 Farm 大图。</footer></main><script type="module" src="board.js"></script></body></html>'''
(ROOT/'index.html').write_text(html,encoding='utf-8')
print('Built two SVG maps, layout data and comparison board.')
