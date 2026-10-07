"""Package the original OFL display font as WOFF2 and check Chinese fallback."""
from pathlib import Path
from fontTools.ttLib import TTFont
import hashlib,json,re

ROOT=Path(__file__).resolve().parents[1]
source=ROOT/'artifacts/font-source/ZCOOLKuaiLe-Regular.ttf'
expected='812a6fc1fe54b6d73a419245c32dfeba8aa33104d5be90d1cf6af082007cb71d'
actual=hashlib.sha256(source.read_bytes()).hexdigest()
if actual!=expected:
    raise ValueError('Unexpected upstream font. Review the pinned source before updating.')
font=TTFont(source);original_names=[r.toBytes() for r in font['name'].names]
font.flavor='woff2';target=ROOT/'web/fonts/zcool-kuaile.woff2';font.save(target)
packed=TTFont(target)
assert [r.toBytes() for r in packed['name'].names]==original_names
requested={ord(c) for path in (ROOT/'web').glob('*.js') for c in path.read_text(encoding='utf-8') if '\u3400'<=c<='\u9fff'}
primary=set(packed.getBestCmap());fallback=set(TTFont(ROOT/'web/fonts/chick-ui.woff2').getBestCmap())
missing=requested-primary-fallback
report={'source':'https://github.com/googlefonts/zcool-kuaile','source_sha256':actual,'license':'OFL-1.1',
    'glyphs':len(primary),'requestedCJK':len(requested),'primaryMissing':[chr(n) for n in sorted(requested-primary)],
    'fallbackMissing':[chr(n) for n in sorted(missing)],'namesPreserved':True,'scope':'Golden collection display labels only'}
out=ROOT/'artifacts/golden-collection-v2/font-report.json';out.parent.mkdir(parents=True,exist_ok=True)
out.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
assert not missing,report
print(json.dumps(report,ensure_ascii=False))
