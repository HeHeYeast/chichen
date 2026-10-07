"""Unretouched A/B layout plus preservation evidence; never production artwork."""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont, ImageChops, ImageStat
from zipfile import ZipFile
from fontTools.ttLib import TTFont
import json, hashlib, re

ROOT=Path(__file__).resolve().parents[1]
OUT=ROOT/'artifacts/golden-business'

def main():
    ref=Image.open(ROOT/'docs/art-direction-20260924-v2/mockups/01-business.png').convert('RGB')
    live=Image.open(OUT/'active-390x780.png').convert('RGB')
    assert live.size==(390,780)
    board=Image.new('RGB',(834,858),'#ede7d6')
    draw=ImageDraw.Draw(board)
    font=ImageFont.truetype('C:/Windows/Fonts/msyh.ttc',18)
    small=ImageFont.truetype('C:/Windows/Fonts/msyh.ttc',13)
    draw.text((18,12),'确认 Mockup',font=font,fill='#563d28')
    draw.text((426,12),'实际 Runtime · 浏览器 390×780',font=font,fill='#563d28')
    board.paste(ref.resize((390,780),Image.Resampling.LANCZOS),(18,43))
    board.paste(live,(426,43))
    draw.text((18,833),'左图仅等比缩放；右图原始运行截图。底栏沿用已通过的全局选中态。',font=small,fill='#685c49')
    board.save(OUT/'mockup-vs-runtime.png')
    backup=ROOT/'artifacts/source-backups/20260924-223934-8ffb34a1.zip'
    with ZipFile(backup) as archive:
        retained=[n for n in archive.namelist() if n.startswith(('assets/png/','web/art/','web/fonts/')) and not n.endswith('/')]
        changed=[n for n in retained if not (ROOT/n).exists() or hashlib.sha256(archive.read(n)).digest()!=hashlib.sha256((ROOT/n).read_bytes()).digest()]
        behavior=['web/business.js','web/business-model.js','web/timeline.js','web/menu-model.js','web/inventory.js','web/order-ui.js','web/regular-ui.js','web/project-ui.js','web/scene.js','web/farm-scene.js','android/release.json']
        changed_behavior=[n for n in behavior if archive.read(n)!=(ROOT/n).read_bytes()]
    assert not changed,(changed)
    assert not changed_behavior,changed_behavior
    old=Image.open(ROOT/'artifacts/golden-collection-detail-fix/browser-390x780.png').convert('RGB')
    new=Image.open(OUT/'collection-regression/browser-390x780.png').convert('RGB')
    diff=ImageChops.difference(old,new)
    changed_pixels=sum(max(pixel)>0 for pixel in diff.get_flattened_data())
    max_difference=max(max(pixel) for pixel in diff.get_flattened_data())
    requested={ord(c) for name in ['web/business-golden-ui.js','web/business-ui.js'] for c in (ROOT/name).read_text(encoding='utf8') if '\u3400'<=c<='\u9fff'}
    primary=set(TTFont(ROOT/'web/fonts/zcool-kuaile.woff2').getBestCmap())
    fallback=set(TTFont(ROOT/'web/fonts/chick-ui.woff2').getBestCmap())
    missing=requested-primary-fallback
    assert not missing,missing
    runtime=json.loads((ROOT/'android/generated-assets/runtime-manifest.json').read_text(encoding='utf8'))
    paths=[a['path'] for a in runtime['assets']]
    assert not any('mockups/' in p or 'asset-sheets/' in p for p in paths)
    manifest=json.loads((ROOT/'web/art/golden-business/manifest.json').read_text(encoding='utf8'))
    for asset in manifest['assets']:
        p=asset['path'].lstrip('/');assert p in paths
        assert hashlib.sha256((ROOT/p).read_bytes()).hexdigest()==asset['sha256']
        assert Image.open(ROOT/p).mode=='RGBA'
    evidence={'originalArtAndFontsCompared':len(retained),'changedOriginalAssets':changed,'unchangedBehaviorFiles':behavior,
        'collection390Regression':{'changedPixels':changed_pixels,'totalPixels':old.width*old.height,'maximumChannelDifference':max_difference,'meanChannelDifference':ImageStat.Stat(diff).mean},
        'businessCJKChecked':len(requested),'missingWithFallback':[], 'independentRGBAAssets':len(manifest['assets']),
        'offlineFiles':runtime['files'],'offlineBytes':runtime['bytes'],'offlineContentHash':runtime['contentHash'],
        'fullMockupOrSourceSheetInRuntime':False,'physicalDevice':False}
    (OUT/'preservation-and-assets.json').write_text(json.dumps(evidence,ensure_ascii=False,indent=2)+'\n',encoding='utf8')
    print(json.dumps(evidence,ensure_ascii=False))

if __name__=='__main__':main()
