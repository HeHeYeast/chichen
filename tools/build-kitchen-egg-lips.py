"""Cut the Lv.1/Lv.2 nest fronts into occluders that start at the visible rim.

The approved front strips are opaque rectangles whose top rows still show the
bed interior, so drawing them over eggs sliced every egg on one straight line.
Lv.2 keeps the wooden rail from its top outline down; Lv.1 keeps the draped
straw bundle with a wavy, feathered upper edge. Lv.3/Lv.4 strips already begin
at the dish rim / cushion roll and are used unchanged. Pixels are never repainted,
only given transparency. Usage: python tools/build-kitchen-egg-lips.py
"""
import math
from pathlib import Path
from PIL import Image

ART = Path(__file__).resolve().parents[1] / 'web/art/golden-kitchen'

# Lv.2 rail inner top outline, read from the 1170px strip (x, y); outside the
# rail the strip is posts/floor and stays opaque.
LV2_RIM = [(150, -4), (165, 0), (200, 10), (240, 19), (300, 29), (400, 33), (500, 34), (600, 34),
           (700, 33), (800, 31), (860, 27), (900, 21), (950, 11), (990, 1), (1005, -4)]


def interpolate(points, x):
    if x <= points[0][0] or x >= points[-1][0]:
        return None
    for (x0, y0), (x1, y1) in zip(points, points[1:]):
        if x0 <= x <= x1:
            t = (x - x0) / (x1 - x0)
            t = t * t * (3 - 2 * t)  # smooth between reading points
            return y0 + (y1 - y0) * t
    return None


def cut(level, edge, feather):
    image = Image.open(ART / f'lv{level}-front.png').convert('RGBA')
    pixels = image.load()
    for x in range(image.width):
        top = edge(x)
        if top is None:
            continue
        for y in range(image.height):
            alpha = max(0.0, min(1.0, (y - top) / feather + 0.5))
            if alpha >= 1:
                break
            r, g, b, _ = pixels[x, y]
            pixels[x, y] = (r, g, b, round(255 * alpha))
    image.save(ART / f'lv{level}-front-lip.png', optimize=True)
    return image.size


def lv1_edge(x):
    # The straw bundle spans the front between the two tied posts.
    if not 280 <= x <= 900:
        return None
    wave = 11 + 5 * math.sin(x / 23.0) + 3.5 * math.sin(x / 8.7 + 1.3) + 2 * math.sin(x / 3.9 + 0.4)
    taper = min(1, (x - 280) / 40, (900 - x) / 40)
    return wave * taper - 6 * (1 - taper)


if __name__ == '__main__':
    print('lv2', cut(2, lambda x: None if interpolate(LV2_RIM, x) is None else interpolate(LV2_RIM, x) - 0.5, 2.0))
    print('lv1', cut(1, lv1_edge, 7.0))
