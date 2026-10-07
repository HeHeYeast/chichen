#!/usr/bin/env python
"""Segment AI character sheets by connected alpha and build a review board.

Usage:
  python tools/review-character-sheet.py --out artifacts/art/valley-golden-batch/review \
      --sheet artifacts/art/valley-golden-batch/sheets/valley-chickens-v1.png:3x2:V-C1,V-C2,V-C3,V-C4,V-C5,V-C6 \
      --sheet artifacts/art/valley-golden-batch/sheets/valley-ducks-v1.png:3x2:V-D1,... \
      --names docs/content-pack/content.json --title valley-review

Segmentation is connected-component based (alpha > ALPHA_T), components are
assigned to grid cells by centroid and merged per cell, so crests or tails
that split off the main body are kept with their character. No averaged
rectangle slicing. Writes crops/<id>.png, bounds.json and <title>.png.
"""
import argparse, json, os, sys
import numpy as np
from PIL import Image, ImageDraw, ImageFont
from scipy import ndimage

ALPHA_T = 64
LIGHT = (245, 238, 222, 255)
DARK = (58, 46, 38, 255)
INK = (70, 48, 34)

def font(size):
    for p in ["C:/Windows/Fonts/msyh.ttc", "C:/Windows/Fonts/simhei.ttf", "/usr/share/fonts/truetype/noto/NotoSansCJK-Regular.ttc"]:
        if os.path.exists(p):
            try:
                return ImageFont.truetype(p, size)
            except Exception:
                pass
    return ImageFont.load_default()

def segment(sheet_path, cols, rows, ids, margin=6):
    im = Image.open(sheet_path).convert("RGBA")
    a = np.array(im)[..., 3]
    mask = a > ALPHA_T
    lab, n = ndimage.label(mask)
    objs = ndimage.find_objects(lab)
    W, H = im.size
    cw, ch = W / cols, H / rows
    cells = {}
    for i, sl in enumerate(objs, start=1):
        ys, xs = sl
        area = int((lab[sl] == i).sum())
        if area < 40:
            continue
        cy, cx = ndimage.center_of_mass(lab[sl] == i)
        cy += ys.start; cx += xs.start
        cell = (min(rows - 1, int(cy // ch)), min(cols - 1, int(cx // cw)))
        box = [xs.start, ys.start, xs.stop, ys.stop]
        if cell in cells:
            b = cells[cell]["box"]
            cells[cell]["box"] = [min(b[0], box[0]), min(b[1], box[1]), max(b[2], box[2]), max(b[3], box[3])]
            cells[cell]["parts"] += 1
            cells[cell]["area"] += area
        else:
            cells[cell] = {"box": box, "parts": 1, "area": area}
    out = []
    k = 0
    for r in range(rows):
        for c in range(cols):
            if k >= len(ids):
                break
            cid = ids[k]; k += 1
            info = cells.get((r, c))
            if not info:
                out.append({"id": cid, "missing": True})
                continue
            x0, y0, x1, y1 = info["box"]
            crop_box = [max(0, x0 - margin), max(0, y0 - margin), min(W, x1 + margin), min(H, y1 + margin)]
            crop = im.crop(crop_box)
            out.append({
                "id": cid, "sheet": sheet_path, "cell": [r, c],
                "alphaBounds": [x0, y0, x1, y1], "crop": crop_box,
                "width": x1 - x0, "height": y1 - y0, "parts": info["parts"], "area": info["area"],
                "touchesEdge": x0 <= 1 or y0 <= 1 or x1 >= W - 1 or y1 >= H - 1,
                "_img": crop,
            })
    return out, im

def fit(img, box_w, box_h, resample=Image.LANCZOS):
    s = min(box_w / img.width, box_h / img.height)
    return img.resize((max(1, round(img.width * s)), max(1, round(img.height * s))), resample)

def silhouette(img, color=(70, 48, 34, 255)):
    a = np.array(img)[..., 3]
    out = np.zeros(a.shape + (4,), dtype=np.uint8)
    out[a > ALPHA_T] = color
    return Image.fromarray(out, "RGBA")

def paste_center(canvas, img, x, y, w, h, bottom=False):
    px = x + (w - img.width) // 2
    py = y + (h - img.height) if bottom else y + (h - img.height) // 2
    canvas.alpha_composite(img, (px, py))

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--sheet", action="append", required=True, help="path:COLSxROWS:id1,id2,...")
    ap.add_argument("--out", required=True)
    ap.add_argument("--title", default="review")
    ap.add_argument("--names", help="content.json to look up display names by authorId")
    ap.add_argument("--ref", action="append", default=[], help="label=path style reference images")
    ap.add_argument("--small", type=int, default=48)
    ap.add_argument("--blind", action="store_true", help="add a hidden-name test strip (numbered silhouettes + 48px only)")
    ap.add_argument("--compare", help="directory of previous-round crops (<id>.png) for a side-by-side row")
    ap.add_argument("--compare-label", default="上一版")
    args = ap.parse_args()

    names = {}
    if args.names and os.path.exists(args.names):
        data = json.load(open(args.names, encoding="utf-8"))
        def walk(o):
            if isinstance(o, dict):
                if "id" in o and "name" in o and isinstance(o["id"], str):
                    names[o["id"]] = o["name"]
                for v in o.values(): walk(v)
            elif isinstance(o, list):
                for v in o: walk(v)
        walk(data)

    os.makedirs(os.path.join(args.out, "crops"), exist_ok=True)
    chars = []
    for spec in args.sheet:
        path, grid, ids = spec.split(":", 2)
        cols, rows = [int(v) for v in grid.lower().split("x")]
        segs, _ = segment(path, cols, rows, ids.split(","))
        chars.extend(segs)

    bounds = []
    for ch in chars:
        if ch.get("missing"):
            bounds.append({"id": ch["id"], "missing": True}); continue
        ch["_img"].save(os.path.join(args.out, "crops", f'{ch["id"]}.png'))
        bounds.append({k: v for k, v in ch.items() if not k.startswith("_")})
    json.dump(bounds, open(os.path.join(args.out, "bounds.json"), "w", encoding="utf-8"), ensure_ascii=False, indent=2)

    present = [c for c in chars if not c.get("missing")]
    n = len(present)
    F, Fs = font(22), font(15)
    cell_w, big_h, small_h, sil_h = 236, 250, 96, 150
    label_h = 44
    refs = []
    for r in args.ref:
        label, p = r.split("=", 1)
        refs.append((label, Image.open(p).convert("RGBA")))
    ref_h = 230 if refs else 0
    prev = {}
    if args.compare:
        for c in present:
            p = os.path.join(args.compare, c["id"] + ".png")
            if os.path.exists(p):
                prev[c["id"]] = Image.open(p).convert("RGBA")
    blind_h = (label_h + 150) if args.blind else 0
    cmp_h = (label_h + big_h + 30) if prev else 0
    W = max(cell_w * n, cell_w * 6) + 40
    H = 60 + ref_h + blind_h + (label_h + big_h) + (label_h + small_h) * 2 + (label_h + sil_h) + cmp_h + 40
    board = Image.new("RGBA", (W, H), LIGHT)
    d = ImageDraw.Draw(board)
    d.text((20, 14), args.title, font=F, fill=INK)
    y = 60
    if refs:
        d.text((20, y), "Style References（旧正式美术）", font=Fs, fill=INK)
        x = 20
        for label, img in refs:
            im = fit(img, 300, ref_h - 40)
            board.alpha_composite(im, (x, y + 24))
            d.text((x, y + 24 + im.height + 2), label, font=Fs, fill=INK)
            x += im.width + 24
        y += ref_h
    if args.blind:
        d.text((20, y), "隐藏名称测试（只有编号：先看 silhouette 和 48px，记录第一眼读成什么）", font=Fs, fill=INK)
        y += label_h - 14
        for i, c in enumerate(present):
            x = 20 + i * cell_w
            sil = fit(silhouette(c["_img"]), 110, 110, Image.NEAREST)
            paste_center(board, sil, x, y, 120, 120, bottom=True)
            sm = fit(c["_img"], args.small, args.small)
            paste_center(board, sm, x + 130, y + 30, args.small + 8, args.small + 8)
            d.text((x + 4, y + 124), f"#{i + 1}", font=Fs, fill=INK)
        y += 150
    # normal size
    d.text((20, y), f"12 只正常尺寸（同一缩放：最高者 {big_h}px；下方数字＝原 alpha 高度 px）", font=Fs, fill=INK)
    y += label_h
    max_h = max(c["height"] for c in present)
    scale = (big_h - 10) / max_h
    for i, c in enumerate(present):
        x = 20 + i * cell_w
        im = c["_img"].resize((max(1, round(c["_img"].width * scale)), max(1, round(c["_img"].height * scale))), Image.LANCZOS)
        paste_center(board, im, x, y, cell_w - 8, big_h, bottom=True)
        nm = names.get(c["id"], "")
        d.text((x + 4, y + big_h + 2), f'{c["id"]} {nm}  h{c["height"]} w{c["width"]}', font=Fs, fill=INK)
    y += big_h + 28
    # small light / dark
    for bg, title in ((LIGHT, f"{args.small}px 浅底"), (DARK, f"{args.small}px 深底")):
        d.text((20, y), title + "（每只独立缩放到 48px 框内）", font=Fs, fill=INK if bg == LIGHT else INK)
        y += label_h - 14
        strip = Image.new("RGBA", (W - 40, small_h), bg)
        for i, c in enumerate(present):
            x = i * cell_w
            im = fit(c["_img"], args.small, args.small)
            paste_center(strip, im, x + 8, 0, args.small + 8, small_h, bottom=False)
            im2 = fit(c["_img"], args.small * 2, args.small * 2)
            paste_center(strip, im2, x + 70, 0, args.small * 2 + 8, small_h, bottom=False)
        board.alpha_composite(strip, (20, y))
        y += small_h + 14
    # silhouettes
    d.text((20, y), "Silhouette 预览（alpha>64 实心）", font=Fs, fill=INK)
    y += label_h - 14
    for i, c in enumerate(present):
        x = 20 + i * cell_w
        im = fit(silhouette(c["_img"]), cell_w - 24, sil_h, Image.NEAREST)
        paste_center(board, im, x, y, cell_w - 8, sil_h, bottom=True)
    if prev:
        y += sil_h + 20
        d.text((20, y), f"{args.compare_label} → 本版 并排（每格左：{args.compare_label}，右：本版；各自缩放到同高）", font=Fs, fill=INK)
        y += label_h - 14
        half = (cell_w - 16) // 2
        for i, c in enumerate(present):
            x = 20 + i * cell_w
            pair = [prev.get(c["id"]), c["_img"]]
            for j, im in enumerate(pair):
                if im is None:
                    continue
                im = fit(im, half - 4, big_h - 20)
                paste_center(board, im, x + j * half, y, half, big_h - 20, bottom=True)
            d.text((x + 4, y + big_h - 16), c["id"], font=Fs, fill=INK)
    out_path = os.path.join(args.out, args.title + ".png")
    board.convert("RGB").save(out_path)
    print(json.dumps({"board": out_path, "characters": [{k: v for k, v in b.items()} for b in bounds]}, ensure_ascii=False, indent=1))

if __name__ == "__main__":
    main()
