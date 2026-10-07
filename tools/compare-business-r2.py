"""Layout/crop screenshots for A/B review; no retouching of runtime or source art."""
from pathlib import Path
from PIL import Image,ImageDraw,ImageFont,ImageChops,ImageStat
from zipfile import ZipFile
import hashlib,json
ROOT=Path(__file__).resolve().parents[1];OUT=ROOT/'artifacts/golden-business-r2'
font=ImageFont.truetype('C:/Windows/Fonts/msyh.ttc',18);small=ImageFont.truetype('C:/Windows/Fonts/msyh.ttc',13)
def board(images,labels,path):
    width=sum(im.width for im in images)+18*(len(images)+1);height=max(im.height for im in images)+75
    result=Image.new('RGB',(width,height),'#eee8d8');draw=ImageDraw.Draw(result);x=18
    for im,label in zip(images,labels):
        draw.text((x,10),label,font=font,fill='#61462e');result.paste(im,(x,42));x+=im.width+18
    draw.text((18,height-22),'真实浏览器截图；仅并排排版，未重绘界面。',font=small,fill='#8b795f');result.save(OUT/path)
def main():
    ref=Image.open(ROOT/'docs/art-direction-20260924-v2/mockups/01-business.png').convert('RGB').resize((390,780),Image.Resampling.LANCZOS)
    old=Image.open(OUT/'before-390x780.png').convert('RGB');live=Image.open(OUT/'active-390x780.png').convert('RGB')
    board([ref,live],['确认 Mockup','本轮 Runtime · 390×780'],'mockup-vs-runtime.png')
    board([old,live],['修改前','本轮修正后'],'before-vs-after.png')
    board([Image.open(OUT/'subpages/orders-active-390.png').convert('RGB'),Image.open(OUT/'subpages/regulars-unread.png').convert('RGB')],['订单 · 独立子页面','常客 · 独立子页面'],'subpages-runtime.png')
    crops=[('货篮名牌',(48,260,170,309),(48,258,170,310),(48,258,170,310)),('销量汇总',(95,472,298,514),(89,476,300,514),(92,476,301,522)),('托盘与柜台',(0,420,390,476),(0,419,390,475),(0,411,390,478)),('账单与收摊',(93,619,347,681),(89,625,351,693),(90,631,358,698)),('常客簿',(160,516,238,597),(152,513,240,595),(152,518,240,600)),('CP栏',(10,10,117,52),(8,7,119,53),(8,7,119,53)),('帮助',(340,10,385,53),(331,2,386,55),(331,2,386,55))]
    contact=Image.new('RGB',(1010,60+len(crops)*145),'#eee8d8');draw=ImageDraw.Draw(contact)
    for x,label in [(115,'确认 Mockup'),(410,'修改前'),(705,'修正后')]:draw.text((x,13),label,font=font,fill='#60442c')
    for i,(label,a,b,c) in enumerate(crops):
        y=58+i*145;draw.text((10,y+43),label,font=small,fill='#60442c')
        for x,source,crop in zip([115,410,705],[ref,old,live],[a,b,c]):
            part=source.crop(crop);scale=min(280/part.width,110/part.height,2);part=part.resize((round(part.width*scale),round(part.height*scale)),Image.Resampling.LANCZOS);contact.paste(part,(x,y+max(0,(110-part.height)//2)))
    contact.save(OUT/'seven-details.png')
    backup=ROOT/'artifacts/source-backups/20260925-business-r2-before.zip'
    with ZipFile(backup) as z:
        originals=[n for n in z.namelist() if n.startswith(('web/art/','web/fonts/')) and (ROOT/n).is_file() and n!='web/art/golden-business/manifest.json']
        changed=[n for n in originals if z.read(n)!=(ROOT/n).read_bytes()]
        assert not changed,changed
        preserved=['web/business.js','web/orders.js','web/regulars.js','web/menu-model.js','web/inventory.js','web/project-ui.js','web/scene.js','web/farm-scene.js','web/golden-collection-ui.js','web/golden-collection.css','web/game-visual-system.css']
        assert all(z.read(n)==(ROOT/n).read_bytes() for n in preserved)
    manifest=json.loads((ROOT/'web/art/golden-business/manifest.json').read_text(encoding='utf8'))
    new=[a for a in manifest['assets'] if 'revision-2/' in a['sheet']]
    for a in new:
        path=ROOT/a['path'].lstrip('/');assert hashlib.sha256(path.read_bytes()).hexdigest()==a['sha256'];assert Image.open(path).mode=='RGBA'
    oldbook=Image.open(ROOT/'artifacts/golden-business/collection-regression/browser-390x780.png').convert('RGB')
    newbook=Image.open(OUT/'collection-regression/browser-390x780.png').convert('RGB');diff=ImageChops.difference(oldbook,newbook)
    evidence={'sourceBackup':str(backup.relative_to(ROOT)),'existingArtAndFontsUnchanged':len(originals),'preservedSource':preserved,'newSheetCuts':len(new),'transparent':True,'collectionScreenshotMeanDifference':ImageStat.Stat(diff).mean,'physicalDevice':False}
    (OUT/'preservation.json').write_text(json.dumps(evidence,ensure_ascii=False,indent=2)+'\n',encoding='utf8');print(json.dumps(evidence,ensure_ascii=False))
if __name__=='__main__':main()
