"""Compose labelled before/after sheets from capture-ui-review.mjs output.

usage: python tools/compose-ui-compare.py artifacts/ui-review-20260930
"""
import sys
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

root = Path(sys.argv[1])
font = ImageFont.truetype('C:/Windows/Fonts/msyh.ttc', 34)
small = ImageFont.truetype('C:/Windows/Fonts/msyh.ttc', 26)
INK, PAPER = (92, 60, 32), (250, 246, 236)


def pair(name, title, before, after, note=''):
    b, a = Image.open(before).convert('RGB'), Image.open(after).convert('RGB')
    gap, top = 40, 170 if note else 110
    sheet = Image.new('RGB', (b.width + a.width + gap * 3, max(b.height, a.height) + top + gap), PAPER)
    d = ImageDraw.Draw(sheet)
    d.text((gap, 20), title, fill=INK, font=font)
    if note: d.text((gap, 70), note, fill=(130, 100, 70), font=small)
    d.text((gap + b.width // 2 - 50, top - 42), '改动前', fill=INK, font=small)
    d.text((gap * 2 + b.width + a.width // 2 - 50, top - 42), '改动后', fill=INK, font=small)
    sheet.paste(b, (gap, top)); sheet.paste(a, (gap * 2 + b.width, top))
    sheet.save(root / f'compare-{name}.png'); return sheet.size


def strip(files, box):
    ims = [Image.open(f).convert('RGB') for f in files]
    crops = [im.crop((0, int(im.height * box[0]), im.width, int(im.height * box[1]))) for im in ims]
    out = Image.new('RGB', (sum(c.width for c in crops) + 12 * (len(crops) - 1), crops[0].height), PAPER)
    x = 0
    for c in crops: out.paste(c, (x, 0)); x += c.width + 12
    return out


B, A = root / 'before', root / 'after'
print(pair('kitchen', '① 厨房：提示条不再压住 CP/标题；鸡蛋·鸭蛋切换移到"下一锅准备"', B / '01-kitchen-390.png', A / '01-kitchen-390.png',
           '提示条改到底栏上方、可穿透点击；小黑板文字完整露出，切换钮每半边从 35×26 放大到 44×26'))
print(pair('cook-confirm', '② 开火确认：「放弃未收取」这条会造成损失的警告放到最前并醒目标出', B / '03-cook-confirm-390.png', A / '03-cook-confirm-390.png'))
pages = ['01-kitchen', '08-farm', '09-business', '10-journey', '11-book']
for tag in ('before', 'after'):
    strip([root / tag / f'{p}-390.png' for p in pages], (0.915, 1.0)).save(root / f'nav-{tag}.png')
nb, na = Image.open(root / 'nav-before.png'), Image.open(root / 'nav-after.png')
sheet = Image.new('RGB', (nb.width + 80, nb.height + na.height + 260), PAPER); d = ImageDraw.Draw(sheet)
d.text((40, 20), '③ 底栏：五个页面统一为农场页那套导航（原来只有农场页不同）', fill=INK, font=font)
d.text((40, 80), '改动前（厨房 · 农场 · 生意 · 寻访 · 图鉴）', fill=INK, font=small); sheet.paste(nb, (40, 120))
d.text((40, 140 + nb.height), '改动后', fill=INK, font=small); sheet.paste(na, (40, 180 + nb.height))
sheet.save(root / 'compare-nav.png'); print(sheet.size)
