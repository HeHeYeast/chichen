"""Measure source alpha only; never resample, recolor or replace character art."""
from pathlib import Path
from PIL import Image
import json, hashlib, math

ROOT = Path(__file__).resolve().parents[1]
paths = sorted((ROOT/'assets/png/Character').glob('character_*/*_0_0.png'))
paths += sorted((ROOT/'web/art').glob('chick-v4-*.png'))
result = {}
for path in paths:
    source = Image.open(path).convert('RGBA')
    alpha = source.getchannel('A')
    bounds = alpha.point(lambda v: 255 if v >= 32 else 0).getbbox()
    if not bounds:
        continue
    x,y,r,b = bounds
    ink = sum(alpha.histogram()[32:])
    # Equal occupied area, constrained to a 112px maximum dimension.
    # Measured reference silhouette envelopes: fluffy egg slightly broader;
    # solid yellow duck lighter/smaller. Both stand on the same foot baseline.
    optical = 1.07 if path.name == 'chick-v4-4.png' else .90 if path.name == 'character_1_0_0_0.png' else 1
    scale = min(84/math.sqrt(ink)*optical, 112/max(r-x,b-y))
    result['/'+path.relative_to(ROOT).as_posix()] = {
        'size': list(source.size), 'bounds': [x,y,r-x,b-y], 'inkPixels': ink,
        'display': [round((r-x)*scale,2), round((b-y)*scale,2)], 'opticalScale':optical,
        'sha256': hashlib.sha256(path.read_bytes()).hexdigest(),
    }
(ROOT/'web/golden-portrait-metrics.js').write_text(
    '// Generated alpha measurements. Existing image files stay untouched.\n'
    'export const GOLDEN_PORTRAITS = '+json.dumps(result,ensure_ascii=False,separators=(',',':'))+';\n',encoding='utf-8')
print(json.dumps({'measured':len(result),'sourceImagesEdited':False}))
