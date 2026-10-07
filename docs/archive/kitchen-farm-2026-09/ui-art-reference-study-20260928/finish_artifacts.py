from pathlib import Path
import json,csv,re,hashlib
from PIL import Image,ImageDraw,ImageFont
ROOT=Path(__file__).parent
items=json.loads((ROOT/'evidence-index.json').read_text(encoding='utf-8'))
byid={i['id']:i for i in items}
fontpath='C:/Windows/Fonts/msyh.ttc'
font=lambda s:ImageFont.truetype(fontpath,s)
cols=[
('T11','塔塔 · 营地','密背景围住功能空地',[(.02,.34,.98,.74),(.01,.06,.48,.13),(.00,.83,1,.999)]),
('M02','妙奇 · 角色','房间展架 + 选择册',[(.02,.18,.99,.42),(.01,.42,.99,.92),(.00,.93,1,.999)]),
('J01','鸡宝 · 生意','商品篮 + 管理区',[(.03,.17,.96,.54),(.07,.56,.93,.81),(.00,.906,1,.999)]),
('J04','鸡宝 · 寻访','地图节点 + 行程卡',[(.015,.075,.98,.68),(.025,.687,.975,.866),(.00,.89,1,.999)]),
('J07','鸡宝 · 图鉴','收藏格 + 印章/分页',[(.05,.18,.94,.76),(.09,.755,.95,.89),(.00,.908,1,.999)])]
cw=300; W=cw*5; H=920
board=Image.new('RGB',(W,H),'#eff0e9');d=ImageDraw.Draw(board)
d.text((20,12),'五组页面：空间承担不同任务，功能与状态形成稳定分组',font=font(23),fill='#173d36')
d.text((20,49),'人工视觉区域标注 · 原图见 Atlas · 框线不是点击热区 · 不统一缩放后的物件比例',font=font(15),fill='#566560')
colors=['#cb4535','#16788a','#7651ad']
for k,(id,title,caption,rects) in enumerate(cols):
 a=byid[id];im=Image.open(ROOT/a['file']).convert('RGB');im.thumbnail((cw-18,665))
 x=k*cw+(cw-im.width)//2;y=150
 d.text((k*cw+12,91),id+'  '+title,font=font(19),fill='#243c35')
 d.text((k*cw+12,121),caption,font=font(16),fill='#566560')
 board.paste(im,(x,y))
 for n,(r,col) in enumerate(zip(rects,colors),1):
  box=[int(x+r[0]*im.width),int(y+r[1]*im.height),int(x+r[2]*im.width),int(y+r[3]*im.height)]
  d.rectangle(box,outline=col,width=3)
  d.rectangle([box[0],box[1],box[0]+22,box[1]+25],fill=col)
  d.text((box[0]+5,box[1]),str(n),font=font(17),fill='white')
 d.text((k*cw+12,830),f'原图 {a["width"]}×{a["height"]}',font=font(14),fill='#566560')
d.text((20,868),'1 对象/场景区    2 信息与操作组织区    3 屏幕级边缘导航/返回区',font=font(19),fill='#243c35')
(ROOT/'evidence').mkdir(exist_ok=True);board.save(ROOT/'evidence/grammar-plate.png')

# Export all three seven-dimension matrices without silently losing missing rows.
rows=[];game=''
for line in (ROOT/'PAGE-TYPE-MATRIX.md').read_text(encoding='utf-8').splitlines():
 if line.startswith('## '):game=line[3:]
 if not line.startswith('|'):continue
 cells=[x.strip() for x in line.strip('|').split('|')]
 if len(cells)!=8 or cells[0]=='页面' or cells[0].startswith('---'):continue
 rows.append(dict(zip(['游戏','页面','主视觉对象','主CTA','次级入口','UI与场景关系','画风','关键设计点','证据与边界'],[game]+cells)))
with (ROOT/'page-type-matrix.csv').open('w',encoding='utf-8-sig',newline='') as f:
 w=csv.DictWriter(f,fieldnames=list(rows[0]));w.writeheader();w.writerows(rows)
print(f'Generated plate {W}x{H}; matrix rows: {len(rows)}')
