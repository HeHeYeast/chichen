"""Build the kitchen-level badges web/art/golden-ui/avatar-lv1..4.png (厨房等级头像, kitchen header and farm HUD).

With the GPT work sheet artifacts/art-inbox/b5/b5-1.png (2 x 2, Lv.1 top-left ... Lv.4 bottom-right) each badge is cut
as an alpha blob, pixels kept as generated (alpha < 12 dropped), with a 6 px transparent margin, never resized.
Without it, all four are one placeholder: the chick that used to be painted into the kitchen header (saved as
artifacts/ui-design-20261002/art-batches/refs/kitchen-chick-avatar.png) with the cream strip-label plate over its old dark
band, so the farm and the kitchen at least show the same picture.

--clean-headers (run once, 2026-10-06): paints the old chick out of web/art/golden-kitchen/lv1..4-header.png by repeating
the wood just left of it (mirrored, row by row), since the kitchen now draws this badge there. It refuses to run twice.

The text plate (where 「Lv.N」 is written) is measured per picture and must be copied into web/level-badge.js.
Manifest entries go into web/art/golden-ui/manifest.json (replacing earlier avatar-lv* rows).
"""
from pathlib import Path
from PIL import Image, ImageDraw
import numpy as np, json, hashlib
from scipy import ndimage

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / 'web/art/golden-ui'
SHEET = ROOT / 'artifacts/art-inbox/b5/b5-1.png'
CHICK = ROOT / 'artifacts/ui-design-20261002/art-batches/refs/kitchen-chick-avatar.png'
# old painted chick in each header (1170-wide pixels, with margin) and the wood strip used to cover it
HEADER_AVATAR = {1: ((149, 15, 290, 169), (104, 148)), 2: ((38, 20, 216, 182), (2, 37)), 3: ((38, 20, 216, 182), (2, 37)), 4: ((38, 20, 216, 182), (2, 37))}


def from_sheet():
  src = Image.open(SHEET).convert('RGBA')
  alpha = np.array(src)[..., 3] > 24
  labels, n = ndimage.label(ndimage.binary_dilation(alpha, iterations=6))
  boxes = sorted((s for s in ndimage.find_objects(labels) if (s[0].stop - s[0].start) * (s[1].stop - s[1].start) > 20000),
                 key=lambda s: (round(s[0].start / 200), s[1].start))
  assert len(boxes) == 4, f'expected 4 badges on {SHEET}, found {len(boxes)}'
  out = []
  for s in boxes:
    piece = src.crop((s[1].start, s[0].start, s[1].stop, s[0].stop))
    piece.putalpha(piece.getchannel('A').point(lambda x: 0 if x < 12 else x))
    final = Image.new('RGBA', (piece.width + 12, piece.height + 12))
    final.paste(piece, (6, 6))
    out.append((final, f'artifacts/art-inbox/b5/b5-1.png', [s[1].start, s[0].start, s[1].stop, s[0].stop]))
  return out


def placeholder():
  frame = Image.open(CHICK).convert('RGBA')
  mask = Image.new('L', frame.size, 0)
  ImageDraw.Draw(mask).rounded_rectangle((4.5, 4.5, frame.width - 4.5, frame.height - 4.5), radius=26, fill=255)
  frame.putalpha(mask)
  plank = Image.open(OUT / 'strip-label.png').convert('RGBA')
  w = frame.width + 12
  plank = plank.resize((w, round(plank.height * w / plank.width)), Image.LANCZOS)
  # strip-label is a stretchable piece: make it taller by stretching only its plain middle band, so 13 px text fits
  top, bottom, tall = 16, plank.height - 16, 64
  middle = plank.crop((0, top, w, bottom)).resize((w, tall - 32), Image.LANCZOS)
  plate = Image.new('RGBA', (w, tall))
  plate.alpha_composite(plank.crop((0, 0, w, top)), (0, 0))
  plate.alpha_composite(middle, (0, top))
  plate.alpha_composite(plank.crop((0, bottom, w, plank.height)), (0, tall - 16))
  badge = Image.new('RGBA', (w, 100 + tall + 2))
  badge.alpha_composite(frame, (6, 0))
  badge.alpha_composite(plate, (0, 100))
  return [(badge, 'artifacts/ui-design-20261002/art-batches/refs/kitchen-chick-avatar.png + web/art/golden-ui/strip-label.png (placeholder)', [38, 22, 215, 180])] * 4


def clean_headers():
  for level, ((x0, y0, x1, y1), (s0, s1)) in HEADER_AVATAR.items():
    path = ROOT / f'web/art/golden-kitchen/lv{level}-header.png'
    img = Image.open(path).convert('RGBA')
    a = np.array(img)
    region = a[y0:y1, x0:x1, :3].astype(int)
    yellow = ((region[..., 0] > 235) & (region[..., 1] > 195) & (region[..., 1] < 235) & (region[..., 2] > 100) & (region[..., 2] < 170)).mean()
    assert yellow > .2, f'lv{level}-header: no painted chick left at {x0},{y0} (already cleaned?)'
    strip = a[y0:y1, s0:s1]
    tiles = [strip if i % 2 == 0 else strip[:, ::-1] for i in range((x1 - x0) // strip.shape[1] + 2)]
    a[y0:y1, x0:x1] = np.concatenate(tiles, axis=1)[:, :x1 - x0]
    Image.fromarray(a).save(path, optimize=True)
    print(f'lv{level}-header: painted chick covered at {(x0, y0, x1, y1)}')


def main():
  pieces = from_sheet() if SHEET.exists() else placeholder()
  manifest_path = OUT / 'manifest.json'
  manifest = json.loads(manifest_path.read_text(encoding='utf8'))
  manifest['assets'] = [a for a in manifest['assets'] if not a['id'].startswith('avatar-lv')]
  for level, (image, sheet, crop) in enumerate(pieces, 1):
    target = OUT / f'avatar-lv{level}.png'
    image.save(target, optimize=True)
    manifest['assets'].append({'id': f'avatar-lv{level}', 'path': f'/web/art/golden-ui/avatar-lv{level}.png', 'sheet': sheet, 'crop': crop,
                               'size': list(image.size), 'sha256': hashlib.sha256(target.read_bytes()).hexdigest()})
    print(f'avatar-lv{level} {image.size} from {sheet}')
  manifest_path.write_text(json.dumps(manifest, ensure_ascii=False, indent=1) + '\n', encoding='utf8')


if __name__ == '__main__':
  import sys
  if '--clean-headers' in sys.argv:
    clean_headers()
  main()
