from PIL import Image,ImageDraw
from pathlib import Path
files=list(Path('assets/png/Tool/Tool0').glob('*'))+list(Path('assets/png/Tool/Tool1').glob('*_0_0.png'))+list(Path('assets/png/MainGame').glob('*'))+list(Path('assets/png/Farm').glob('*0_0.png'))+list(Path('assets/png/Character').glob('**/character_0_0_*.png'))
w=1000; out=Image.new('RGB',(w,((len(files)+5)//6)*155),'#dfd9ce');d=ImageDraw.Draw(out)
for i,p in enumerate(files):
 im=Image.open(p).convert('RGBA');im.thumbnail((158,120));x=(i%6)*166;y=(i//6)*155;out.paste(im,(x+(166-im.width)//2,y),im);d.text((x+3,y+121),p.name,fill='black');d.text((x+3,y+135),str(Image.open(p).size),fill='black')
out.save('artifacts/asset-sheet.jpg')
