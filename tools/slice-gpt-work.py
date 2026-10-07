"""Cut the GPT work sheets (artifacts/art-inbox/b1..b4) into single transparent pieces in web/art/golden-ui.

Each sheet's pieces are found as alpha blobs, put in reading order and named by the job's part list; a few parts
are drawn as several blobs (dotted divider, sparkles, confetti, steam) and are merged by index. Pixels are kept
as generated (only alpha < 12 dropped), cropped to the visible bounds with a 6px transparent margin, never resized.
"""
from pathlib import Path
from PIL import Image
import numpy as np, json, hashlib
from scipy import ndimage

ROOT = Path(__file__).resolve().parents[1]
INBOX = ROOT / 'artifacts/art-inbox'
OUT = ROOT / 'web/art/golden-ui'
SKILLS = ['CUL-1', 'CUL-2', 'CUL-3', 'CUL-4', 'CUL-5', 'CUL-S', 'HOME-1', 'HOME-2', 'HOME-3', 'HOME-4', 'HOME-5', 'HOME-S']
SKILLS2 = ['TRADE-1', 'TRADE-2', 'TRADE-3', 'TRADE-4', 'TRADE-5', 'TRADE-S', 'OBS-1', 'OBS-2', 'OBS-3', 'OBS-4', 'OBS-5', 'OBS-S',
           'TRIP-1', 'TRIP-2', 'TRIP-3', 'TRIP-4', 'TRIP-5', 'TRIP-S']
# sheet -> (file, names in reading order; a tuple of blob indexes merges them into one piece)
SHEETS = {
  'b1-1': ('b1/b1-1.png', ['paper-main', 'paper-torn', 'inset-board', 'sticky-note', 'strip-label', 'drawer-lip']),
  'b1-2': ('b1/b1-2.png', ['hanging-sign', 'ribbon-green', 'ribbon-yellow', 'ribbon-red', 'section-tag', 'bunting']),
  'b1-3': ('b1/b1-3.png', ['btn-close', 'btn-back', 'btn-minus', 'btn-plus', 'coin-quick', 'coin-quick-on', 'coin-quick-off', 'chip-plain', 'chip-hot']),
  'b1-4': ('b1/b1-4-v2.png', {0: 'bar-track', 4: 'bar-green', 5: 'bar-yellow', 9: 'bar-red', 1: 'bead', 2: 'cell', 3: 'cell-on', 6: 'cell-missing', 7: 'cell-locked', 8: 'cell-new'}),
  'b1-5': ('b1/b1-5.png', {0: 'divider-leaf', (1, 2, 3): 'divider-dots', 4: 'arrow', 5: 'leaf-corner-l', 6: 'leaf-corner-r', (7, 8, 9): 'sparkles', 10: 'stamp-ring'}),
  'b2-1': ('b2/b2-1.png', ['ic-hourglass', 'ic-flame', 'ic-pot', 'ic-egg', 'ic-duck-egg', 'ic-jar', 'ic-broom', 'ic-germ', 'ic-fresh', 'ic-upgrade', 'ic-hammer', 'ic-fence', 'ic-house', 'ic-star', 'ic-heart', 'ic-check']),
  'b2-2': ('b2/b2-2-v2.png', ['ic-chick', 'ic-basket', 'ic-bill', 'ic-bell', 'ic-gift', 'ic-calendar', 'ic-book', 'ic-map', 'ic-leaf', 'ic-chest', 'ic-filter', 'ic-sort', 'ic-tag', 'ic-refresh', 'ic-alarm', 'ic-cross']),
  'b3-1': ('b3/b3-1.png', ['clean-set', 'counter-clean', 'counter-dusty', 'counter-dirty', 'fence-broken', 'fence-fixed']),
  'b3-2': ('b3/b3-2-v2.png', {(0, 1, 2, 3, 4, 6, 8): 'confetti', 7: 'basket-empty', (5, 9): 'steam', 10: 'crate-empty', 11: 'blueprint', 12: 'coin-pile'}),
  'b4-1': ('b4/b4-1.png', [f'skill-{s}' for s in SKILLS] + ['branch-cul', 'branch-home', 'branch-trade', 'branch-obs']),
  'b4-2': ('b4/b4-2.png', [f'skill-{s}' for s in SKILLS2] + ['branch-trip', 'skill-point']),
  'b4-3': ('b4/b4-3-v2.png', ['set-music', 'set-sound', 'set-hatch', 'set-keep', 'set-notify', 'set-background']),
}


def blobs(alpha):
  solid = alpha > 24
  lab, _ = ndimage.label(ndimage.binary_dilation(solid, iterations=14))
  boxes = []
  for sl in ndimage.find_objects(lab):
    sub = solid[sl]
    if sub.sum() < 300:
      continue
    ys, xs = np.where(sub)
    boxes.append((sl[1].start + xs.min(), sl[0].start + ys.min(), sl[1].start + xs.max() + 1, sl[0].start + ys.max() + 1))
  boxes.sort(key=lambda b: (b[1] + b[3]) / 2)
  rows = []
  for b in boxes:
    cy = (b[1] + b[3]) / 2
    if rows and abs(cy - rows[-1][0]) < 60:
      rows[-1][1].append(b)
    else:
      rows.append([cy, [b]])
  return [b for r in rows for b in sorted(r[1], key=lambda b: b[0])]


def main():
  OUT.mkdir(parents=True, exist_ok=True)
  assets = []
  for sheet, (file, names) in SHEETS.items():
    src = Image.open(INBOX / file).convert('RGBA')
    found = blobs(np.array(src)[..., 3])
    plan = dict(enumerate(names)) if isinstance(names, list) else names
    used = sorted(i for k in plan for i in (k if isinstance(k, tuple) else (k,)))
    assert used == list(range(len(found))), (sheet, len(found), used)
    for key, name in plan.items():
      idx = key if isinstance(key, tuple) else (key,)
      box = (min(found[i][0] for i in idx), min(found[i][1] for i in idx), max(found[i][2] for i in idx), max(found[i][3] for i in idx))
      # Only this piece's blobs: other pieces' pixels inside the merged box are cleared.
      mask = Image.new('L', src.size, 0)
      for i in idx:
        mask.paste(255, tuple(int(v) for v in (found[i][0] - 10, found[i][1] - 10, found[i][2] + 10, found[i][3] + 10)))
      piece = src.copy()
      alpha = Image.fromarray(np.minimum(np.array(piece.getchannel('A')), np.array(mask)))
      piece.putalpha(alpha.point(lambda x: 0 if x < 12 else x))
      piece = piece.crop(tuple(int(v) for v in box))
      final = Image.new('RGBA', (piece.width + 12, piece.height + 12))
      final.paste(piece, (6, 6))
      target = OUT / f'{name}.png'
      final.save(target, optimize=True)
      assets.append({'id': name, 'path': f'/web/art/golden-ui/{name}.png', 'sheet': f'artifacts/art-inbox/{file}', 'crop': [int(v) for v in box],
                     'size': list(final.size), 'sha256': hashlib.sha256(target.read_bytes()).hexdigest()})
      print(f'{sheet} {name:16} {final.size}')
  # Derived: one period of the dotted divider (a single dot, 48px apart on the sheet) as a repeat-x tile for leader lines.
  dots = Image.open(OUT / 'divider-dots.png')
  tile = dots.crop((39, 22, 87, 50))
  tile.save(OUT / 'dots-tile.png', optimize=True)
  assets.append({'id': 'dots-tile', 'path': '/web/art/golden-ui/dots-tile.png', 'sheet': 'web/art/golden-ui/divider-dots.png', 'crop': [39, 22, 87, 50],
                 'size': list(tile.size), 'sha256': hashlib.sha256((OUT / 'dots-tile.png').read_bytes()).hexdigest()})
  (OUT / 'manifest.json').write_text(json.dumps({'source': 'GPT work, artifacts/art-inbox/gpt-work jobs b1-b4; alpha-preserving cut, no resize', 'assets': assets}, ensure_ascii=False, indent=1) + '\n', encoding='utf8')


if __name__ == '__main__':
  main()
