"""Measured RGBA cuts from category sheets; no painting and no source character edits."""
from pathlib import Path
from PIL import Image
import hashlib, json

ROOT = Path(__file__).resolve().parents[1]
SHEETS = ROOT / 'docs/business-golden-20260924/asset-sheets'
OUT = ROOT / 'web/art/golden-business'
PARTS = {
    'B01-vessels.png': {
        'basket-base': (15,250,620,605), 'basket-front': (640,320,1235,595),
        'tray-base': (15,790,625,1060), 'tray-front': (640,845,1235,1045),
    },
    'B02-signs.png': {
        'sign-open': (15,325,450,620), 'sign-prepare': (480,325,915,620),
        'menu-easel': (935,285,1245,650), 'action-plaque': (20,805,485,1010),
        'counter-plank': (490,850,950,960), 'summary-label': (950,815,1235,1000),
    },
    'B03-props.png': {
        'orders': (120,90,610,605), 'regulars': (690,125,1170,610),
        'projects': (130,650,630,1140), 'coin': (730,700,1170,1120),
    },
}

def main():
    OUT.mkdir(parents=True, exist_ok=True)
    assets = []
    for sheet, parts in PARTS.items():
        source = Image.open(SHEETS / sheet).convert('RGBA')
        for name, crop in parts.items():
            piece = source.crop(crop)
            alpha = piece.getchannel('A').point(lambda a: 0 if a < 12 else a)
            piece.putalpha(alpha)
            bounds = alpha.getbbox()
            if not bounds: raise ValueError(name)
            piece = piece.crop(bounds)
            final = Image.new('RGBA', (piece.width+16, piece.height+16))
            final.paste(piece, (8,8))
            target = OUT / (name+'.png')
            final.save(target)
            assets.append({'id':'business:'+name,'path':'/web/art/golden-business/'+target.name,
                'sheet':'docs/business-golden-20260924/asset-sheets/'+sheet,'crop':list(crop),
                'alphaBoundsInCrop':list(bounds),'size':list(final.size),'safeInset':8,
                'anchor':[.5,1] if 'basket' in name or 'tray' in name else [.5,.5],
                'transparent':True,'status':'golden-sample',
                'sha256':hashlib.sha256(target.read_bytes()).hexdigest(),
                'source':'built-in-imagegen; measured sheet cut; no UI text or characters'})
    (OUT/'manifest.json').write_text(json.dumps({'version':1,'style':'jibao-business-golden','assets':assets},ensure_ascii=False,indent=2)+'\n',encoding='utf8')
    print(json.dumps({'assets':len(assets),'sheetsUnchanged':True}))

if __name__=='__main__': main()
