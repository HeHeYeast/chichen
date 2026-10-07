"""Cut the approved category sheets; no illustration or character regeneration.

Coordinates are measured per object (the generated gutters are not a perfect grid).
Only nearly transparent exterior dust is removed. Original sheets stay untouched.
"""
from pathlib import Path
from PIL import Image
import hashlib
import json

ROOT = Path(__file__).resolve().parents[1]
SHEETS = ROOT / 'docs/art-direction-20260924-v2/asset-sheets'
OUT = ROOT / 'web/art/golden-collection'
PARTS = {
    'revision-2/G03-paper.png': {
        'v2-paper': (24, 42, 783, 1210), 'v2-unknown': (818, 451, 1238, 870),
    },
    'revision-2/G04-stamps.png': {
        'v2-collection-active': (10, 186, 620, 603), 'v2-collection-inactive': (620, 185, 1247, 610),
        'v2-practice-active': (10, 741, 622, 1115), 'v2-practice-inactive': (625, 740, 1247, 1120),
    },
    'revision-2/G05-stationery.png': {
        'v2-tape': (19, 149, 407, 341), 'v2-tab-idle': (420, 130, 830, 353),
        'v2-tab-current': (836, 136, 1246, 354), 'v2-pin': (130, 407, 308, 858),
        'v2-memento': (429, 409, 804, 851), 'v2-swash': (814, 553, 1244, 730),
        'v2-round-button': (72, 895, 371, 1187), 'v2-page-button': (473, 895, 768, 1186),
        'v2-backdrop': (806, 900, 1233, 1202),
    },
    'G01-paper.png': {
        'paper': (20, 90, 418, 766), 'unknown': (426, 236, 845, 681),
        'tape': (859, 383, 1237, 550), 'tab-idle': (10, 900, 427, 1187),
        'tab-current': (430, 900, 846, 1187), 'memento-clip': (865, 786, 1226, 1224),
    },
    'G02-state.png': {
        'stamp-done': (10, 230, 505, 585), 'stamp-pending': (505, 230, 987, 585),
        'pin-on': (1008, 125, 1210, 613), 'pin-off': (40, 671, 259, 1143),
        'nav-wash': (289, 791, 901, 1081), 'paper-button': (919, 775, 1237, 1091),
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
            if bounds is None:
                raise ValueError(f'Empty component: {name}')
            piece = piece.crop(bounds)
            # A transparent 8px guard protects filter/shadow edges during atlas sampling.
            final = Image.new('RGBA', (piece.width + 16, piece.height + 16))
            final.paste(piece, (8, 8))
            target = OUT / f'{name}.png'
            final.save(target)
            assets.append({'id': f'golden:{name}', 'path': '/web/art/golden-collection/' + target.name,
                'sheet': 'docs/art-direction-20260924-v2/asset-sheets/' + sheet,
                'crop': list(crop), 'alphaBoundsInCrop': list(bounds), 'size': list(final.size),
                'anchor': [0.5, 0.5], 'safeInset': 8, 'transparent': True,
                'sha256': hashlib.sha256(target.read_bytes()).hexdigest(),
                'status': 'golden-sample' if sheet.startswith('revision-2/') else 'superseded',
                'source': 'built-in-imagegen; measured crop; no text'})
    manifest = {'version': 1, 'style': 'jibao-confirmed-mockups-v2', 'assets': assets}
    (OUT / 'manifest.json').write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + '\n', encoding='utf8')
    print(json.dumps({'cutAssets': len(assets), 'rgba': True, 'sheetsUnchanged': True}))

if __name__ == '__main__':
    main()
