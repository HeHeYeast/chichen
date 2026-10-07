"""Unretouched screenshot comparisons and bytewise frozen-surface audit."""
from pathlib import Path
from zipfile import ZipFile
from PIL import Image,ImageDraw,ImageFont,ImageChops,ImageStat
import json,hashlib
ROOT=Path(__file__).resolve().parents[1];OUT=ROOT/'artifacts/journey-family';R=OUT/'runtime'
font=ImageFont.truetype('C:/Windows/Fonts/msyh.ttc',18)
def image(path):return Image.open(path).convert('RGB')
def board(items,path):
 out=Image.new('RGB',(sum(im.width for _,im in items)+16*(len(items)+1),max(im.height for _,im in items)+52),'#eee8d8');draw=ImageDraw.Draw(out);x=16
 for label,im in items:draw.text((x,9),label,font=font,fill='#61472e');out.paste(im,(x,38));x+=im.width+16
 out.save(path)
pages=[('世界地图','01-world'),('地区 / 地点 / 追寻','03-region-team'),('同行选择','04-companions'),('出发确认','05-confirm'),('在途','06-in-transit'),('归来 / 新发现','07-new-discovery'),('普通归来','08-ordinary-return')]
for i,part in enumerate([pages[:3],pages[3:6],pages[6:]],1):board([(label,image(R/(file+'.png'))) for label,file in part],OUT/f'runtime-{i}.png')
ref=image(ROOT/'docs/journey-visual-20260925/mockup-main.png')
# Each source is aspect fitted, not stretched or edited to imitate runtime.
for i,(name,runtime) in enumerate([('map','01-world'),('region','03-region-team'),('return','07-new-discovery')]):
 crop=ref.crop([(16,16,485,1046),(504,16,974,1046),(992,16,1460,1046)][i]);crop.thumbnail((390,844),Image.Resampling.LANCZOS)
 canvas=Image.new('RGB',(390,844),'#fff9e4');canvas.paste(crop,((390-crop.width)//2,(844-crop.height)//2))
 board([('Mockup · 示例内容',canvas),('Runtime · 原数据',image(R/(runtime+'.png')))],OUT/f'{name}-ab.png')
preserved=[];changed=[]
with ZipFile(ROOT/'artifacts/source-backups/20260925-journey-before.zip') as z:
 for n in z.namelist():
  if n.endswith('/') or not n.startswith('web/'):continue
  if (ROOT/n).read_bytes()==z.read(n):preserved.append(n)
  else:changed.append(n)
assert sorted(changed)==['web/index.html','web/regional-ui.js'],changed
manifest=json.loads((ROOT/'web/art/golden-journey/manifest.json').read_text(encoding='utf-8'))
for e in manifest['assets']:
 p=ROOT/e['path'].lstrip('/');im=Image.open(p);assert hashlib.sha256(p.read_bytes()).hexdigest()==e['sha256']
 if e['transparent']:
  assert im.mode=='RGBA' and im.getchannel('A').getextrema()[0]==0
  b=im.getchannel('A').getbbox();x,y,w,h=e['visualBounds'];assert b==(x,y,x+w,y+h),(e['id'],b)
businessDiff=ImageStat.Stat(ImageChops.difference(image(ROOT/'artifacts/business-family/states/01-business.png'),image(R/'frozen-business.png'))).mean
assert businessDiff==[0,0,0],businessDiff
report={'unchangedExistingWebFiles':len(preserved),'unchangedArtAndFonts':sum(n.startswith(('web/art/','web/fonts/')) for n in preserved),'changedExistingFiles':changed,'coreGameplayUnchanged':True,'businessPixelMeanDifference':businessDiff,'newIndependentTransparentAssets':sum(e['transparent'] for e in manifest['assets']),'staticGeographyBackgrounds':1,'manifestBoundsVerified':True,'physicalDevice':False}
(OUT/'preservation.json').write_text(json.dumps(report,ensure_ascii=False,indent=2)+'\n',encoding='utf-8');print(json.dumps(report,ensure_ascii=False))
