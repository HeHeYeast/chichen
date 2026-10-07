"""Extract the user-approved guestbook icon verbatim; no generated or painted pixels."""
from pathlib import Path
from collections import deque
from PIL import Image
import hashlib, json

root = Path(__file__).resolve().parents[1]
source = 'docs/art-direction-20260924-v2/mockups/01-business.png'
crop = (369, 1180, 534, 1340)
piece = Image.open(root/source).convert('RGBA').crop(crop)
# Remove only the cream background connected to the crop perimeter.
# Enclosed cream book-page pixels and the complete cover illustration stay intact.
pixels = piece.load()
queue = deque([(x,y) for x in range(piece.width) for y in (0,piece.height-1)] +
              [(x,y) for y in range(piece.height) for x in (0,piece.width-1)])
seen = set()
while queue:
    x,y = queue.popleft()
    if (x,y) in seen or not (0 <= x < piece.width and 0 <= y < piece.height): continue
    seen.add((x,y))
    r,g,b,a = pixels[x,y]
    if r < 195 or g < 180 or b < 135: continue
    pixels[x,y] = (r,g,b,0)
    queue.extend(((x-1,y),(x+1,y),(x,y-1),(x,y+1)))
target = root/'web/art/golden-business/regulars-original.png'
piece.save(target)
manifest_path = root/'web/art/golden-business/manifest.json'
manifest = json.loads(manifest_path.read_text(encoding='utf8'))
asset = {'id':'business:regulars-original','path':'/web/art/golden-business/regulars-original.png',
         'sheet':source,'crop':list(crop),'size':list(piece.size),'transparent':True,
         'status':'golden-sample','source':'User-requested approved mockup icon extraction; original cover and chick retained; perimeter cream removed only',
         'sha256':hashlib.sha256(target.read_bytes()).hexdigest()}
manifest['assets'] = [a for a in manifest['assets'] if a['id'] != asset['id']] + [asset]
manifest_path.write_text(json.dumps(manifest,ensure_ascii=False,indent=2)+'\n',encoding='utf8')
print(target)
