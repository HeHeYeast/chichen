"""Unretouched runtime contact sheets and preservation evidence."""
from pathlib import Path
from zipfile import ZipFile
from PIL import Image,ImageDraw,ImageFont,ImageChops,ImageStat
import json,hashlib
ROOT=Path(__file__).resolve().parents[1];OUT=ROOT/'artifacts/business-family';S=OUT/'states'
font=ImageFont.truetype('C:/Windows/Fonts/msyh.ttc',18)
def board(items,path):
 width=sum(im.width for _,im in items)+16*(len(items)+1);height=max(im.height for _,im in items)+50
 out=Image.new('RGB',(width,height),'#eee8d8');draw=ImageDraw.Draw(out);x=16
 for label,im in items: draw.text((x,9),label,font=font,fill='#61472e');out.paste(im,(x,38));x+=im.width+16
 out.save(path)
def openim(path):return Image.open(path).convert('RGB')
pages=[('营业','01-business'),('订单','02-orders'),('常客','03-regulars'),('项目','04-projects'),('账单','05-ledger')]
board([(label,openim(S/(file+'.png'))) for label,file in pages[:3]],OUT/'runtime-01-business-orders-regulars.png')
board([(label,openim(S/(file+'.png'))) for label,file in pages[3:]],OUT/'runtime-02-projects-ledger.png')
mockups=[(ROOT/'docs/business-golden-20260924/revision-2/subpages-mockup.png',[('orders','02-orders'),('regulars','03-regulars')]),(ROOT/'docs/business-family-20260925/projects-ledger-mockup.png',[('projects','projects-ready'),('ledger','05-ledger')])]
for path,pairs in mockups:
 ref=openim(path)
 for i,(name,file) in enumerate(pairs):
  crop=ref.crop((i*ref.width//2,0,(i+1)*ref.width//2,ref.height));crop.thumbnail((390,844),Image.Resampling.LANCZOS)
  canvas=Image.new('RGB',(390,844),'#eee8d8');canvas.paste(crop,((390-crop.width)//2,(844-crop.height)//2))
  board([('Mockup · 示例内容',canvas),('Runtime · 真实数据',openim(S/(file+'.png')))],OUT/(name+'-ab.png'))
backup=ROOT/'artifacts/source-backups/20260925-business-family-before.zip'
preserved=['web/business.js','web/orders.js','web/regulars.js','web/projects.js','web/menu-model.js','web/inventory.js','web/business-model.js','web/timeline.js','web/engine.js','web/game-commands.js','web/scene.js','web/farm-scene.js','web/regional-ui.js','web/golden-collection-ui.js','web/golden-collection.css','web/game-visual-system.css','web/business-golden-ui.js','web/business-golden.css','web/business-refinement.css']
with ZipFile(backup) as z:
 oldart=[n for n in z.namelist() if n.startswith(('web/art/','web/fonts/')) and n!='web/art/golden-business/manifest.json']
 assert all(z.read(n)==(ROOT/n).read_bytes() for n in oldart),'existing art changed'
 assert all(z.read(n)==(ROOT/n).read_bytes() for n in preserved),'frozen code changed'
manifest=json.loads((ROOT/'web/art/golden-business/manifest.json').read_text(encoding='utf8'))
new=[a for a in manifest['assets'] if 'business-family-20260925' in a['sheet']]
for a in new:
 p=ROOT/a['path'].lstrip('/');im=Image.open(p);assert im.mode=='RGBA';assert im.getchannel('A').getextrema()[0]==0;assert hashlib.sha256(p.read_bytes()).hexdigest()==a['sha256']
business_diff=ImageStat.Stat(ImageChops.difference(openim(ROOT/'artifacts/golden-business-r3/active-390x780.png'),openim(OUT/'business/active-390x780.png'))).mean
collection_diff=ImageStat.Stat(ImageChops.difference(openim(ROOT/'artifacts/golden-business-r2/collection-regression/browser-390x780.png'),openim(OUT/'collection-regression/browser-390x780.png'))).mean
assert business_diff==[0,0,0],business_diff
assert collection_diff==[0,0,0],collection_diff
report={'oldArtAndFontsUnchanged':len(oldart),'preservedSource':preserved,'newIndependentRgbaAssets':len(new),'businessPixelMeanDifference':business_diff,'collectionPixelMeanDifference':collection_diff,'physicalDevice':False}
(OUT/'preservation.json').write_text(json.dumps(report,ensure_ascii=False,indent=2)+'\n',encoding='utf8');print(json.dumps(report,ensure_ascii=False))
