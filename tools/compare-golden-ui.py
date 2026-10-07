"""Assemble unretouched reference/runtime evidence, not game artwork."""
from pathlib import Path
from PIL import Image,ImageDraw,ImageFont,ImageStat
import json,hashlib,zipfile

ROOT=Path(__file__).resolve().parents[1]
OUT=ROOT/'artifacts/golden-collection-v2';OUT.mkdir(exist_ok=True)
ref=ROOT/'docs/art-direction-20260924-v2/mockups/03-collection.png'
before=OUT/'before/browser-390x780.png';after=OUT/'browser-390x780.png'
images=[Image.open(p).convert('RGB').resize((390,780),Image.Resampling.LANCZOS) for p in [ref,before,after]]
labels=['确认 Mockup','上一版 Runtime','本轮 Runtime · 浏览器']
font=ImageFont.truetype('C:/Windows/Fonts/msyh.ttc',18)
def board(indices,name,box=(0,0,390,780),scale=1):
    w,h=int((box[2]-box[0])*scale),int((box[3]-box[1])*scale)
    canvas=Image.new('RGB',((w+20)*len(indices)+10,h+66),'#f3efdf');draw=ImageDraw.Draw(canvas)
    for n,i in enumerate(indices):
        x=10+n*(w+20);draw.text((x,12),labels[i],font=font,fill='#563d28')
        canvas.paste(images[i].crop(box).resize((w,h),Image.Resampling.LANCZOS),(x,48))
    canvas.save(OUT/name)
board([0,2],'mockup-vs-runtime.png')
board([0,1,2],'mockup-before-after.png')
for name,box in [('header',(20,0,382,157)),('stickers',(38,153,351,445)),('unknown',(38,441,352,580)),('stamps',(25,574,380,693)),('nav',(0,696,390,780))]:
    board([0,1,2],f'detail-{name}.png',box,1.5)

# All four stamp states captured from real isolated runtime state fixtures.
states=Image.new('RGB',(620,255),'#f9f2dc');draw=ImageDraw.Draw(states)
for i,(file,label) in enumerate([('stamps-completed.png','收录完成 / 实践完成'),('empty-390.png','收录未完成 / 实践未完成')]):
    im=Image.open(OUT/file).convert('RGB').crop((31,579,234,650)).resize((406,142),Image.Resampling.LANCZOS)
    im.thumbnail((290,160));x=10+i*310;draw.text((x,12),label,font=font,fill='#563d28');states.paste(im,(x,66))
states.save(OUT/'four-stamp-states.png')

with zipfile.ZipFile(ROOT/'artifacts/source-backups/20260924-200417-51aac400.zip') as z:
    originals=[n for n in z.namelist() if n.lower().endswith(('.png','.webp','.jpg')) and (ROOT/n).is_file()]
    changed=[n for n in originals if hashlib.sha256(z.read(n)).digest()!=hashlib.sha256((ROOT/n).read_bytes()).digest()]
report={'reference':str(ref.relative_to(ROOT)),'reference_sha256':hashlib.sha256(ref.read_bytes()).hexdigest(),
        'runtime_sha256':hashlib.sha256(after.read_bytes()).hexdigest(),'runtimeViewport':[390,780],
        'referenceDisplayScale':390/887,'screenshotsRetouched':False,'physicalDevice':False,
        'existingArtCompared':len(originals),'existingArtChanged':changed,
        'blankPaperMedianRGB':dict(zip(['mockup','before','after'],[ImageStat.Stat(i.crop((183,345,205,385))).median for i in images]))}
(OUT/'comparison-evidence.json').write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(report,ensure_ascii=False))
