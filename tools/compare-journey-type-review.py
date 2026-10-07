"""Focused, equally scaled crops from untouched browser screenshots."""
from pathlib import Path
from PIL import Image,ImageDraw,ImageFont
R=Path(__file__).resolve().parents[1];O=R/'artifacts/journey-precision'
font=ImageFont.truetype('C:/Windows/Fonts/msyh.ttc',20)
rows=[('同行属性与计时','region',(12,620,378,735)),('寻访进度','map',(22,589,369,646)),('本次寻访目标','region',(12,489,378,544)),('新发现标题','return',(72,200,319,270))]
scale=1.5;col=575;board=Image.new('RGB',(1180,900),'#eee8d8');d=ImageDraw.Draw(board)
d.text((20,12),'修改前',font=font,fill='#61472e');d.text((20+col,12),'修改后',font=font,fill='#61472e')
y=48
for label,page,box in rows:
 d.text((20,y),label,font=font,fill='#61472e');y+=29
 for i,phase in enumerate(['type-review-before','final']):
  im=Image.open(O/phase/(page+'-390.png')).convert('RGB').crop(box)
  im=im.resize((round(im.width*scale),round(im.height*scale)),Image.Resampling.LANCZOS)
  board.paste(im,(20+col*i,y))
 y+=im.height+12
board.crop((0,0,1180,y)).save(O/'type-review-before-after.png')
print('Saved focused before/after comparison; equal crops and scale, no pixel retouching.')
