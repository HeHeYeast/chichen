#!/usr/bin/env python
"""Quantify the visual language of 120x120 legacy 鸡宝/鸭宝 sprites (and compare candidates).

Usage:
  python tools/analyze-legacy-style.py --out artifacts/art/legacy-style/analysis \
      --sprite 0:0 --sprite 1:38 ...            # legacy sprites by egg:id
      --file label=path.png ...                 # any 120x120-equivalent RGBA (candidates)
Outputs metrics.json and metrics.csv. Every metric is computed on the 120px canvas
(candidates larger than 120 are LANCZOS-downscaled to 120 first), alpha>64 = visible,
alpha>200 = opaque body, "ink" = opaque and luminance<70 (legacy outlines are near black).
"""
import argparse, csv, json, os
import numpy as np
from PIL import Image
from scipy import ndimage
from scipy.spatial import ConvexHull

def load120(path, fit_body=None):
    """Load as a 120x120 canvas. fit_body=N rescales so max(visual w,h) == N (legacy median ~78),
    which makes stroke/eye ratios comparable for crops that fill their own canvas."""
    im = Image.open(path).convert("RGBA")
    if fit_body:
        a = np.array(im)[..., 3]; ys, xs = np.where(a > 64)
        im = im.crop((xs.min(), ys.min(), xs.max() + 1, ys.max() + 1))
        s = fit_body / max(im.size)
        im = im.resize((max(1, round(im.width * s)), max(1, round(im.height * s))), Image.LANCZOS)
        c = Image.new("RGBA", (120, 120), (0, 0, 0, 0)); c.alpha_composite(im, ((120 - im.width) // 2, 120 - im.height)); return c
    if im.size != (120, 120):
        s = 120 / max(im.size)
        im = im.resize((max(1, round(im.width * s)), max(1, round(im.height * s))), Image.LANCZOS)
        c = Image.new("RGBA", (120, 120), (0, 0, 0, 0)); c.alpha_composite(im, ((120 - im.width) // 2, 120 - im.height)); im = c
    return im

def hsv(rgb):
    r, g, b = [rgb[..., i] / 255.0 for i in range(3)]
    mx = np.max(rgb, axis=2) / 255.0; mn = np.min(rgb, axis=2) / 255.0; d = mx - mn
    s = np.where(mx > 0, d / np.maximum(mx, 1e-6), 0)
    h = np.zeros_like(mx)
    m = d > 1e-6
    rc = np.where(m, (mx - r) / np.maximum(d, 1e-6), 0); gc = np.where(m, (mx - g) / np.maximum(d, 1e-6), 0); bc = np.where(m, (mx - b) / np.maximum(d, 1e-6), 0)
    h = np.where(mx == r, bc - gc, np.where(mx == g, 2 + rc - bc, 4 + gc - rc))
    h = (h / 6.0) % 1.0
    return h * 360, s, mx

def metrics(im):
    a = np.array(im).astype(int); al = a[..., 3]; rgb = a[..., :3]
    vis = al > 64; op = al > 200
    if not vis.any():
        return {"error": "empty"}
    ys, xs = np.where(vis); x0, y0, x1, y1 = int(xs.min()), int(ys.min()), int(xs.max()) + 1, int(ys.max()) + 1
    w, h = x1 - x0, y1 - y0
    lum = rgb.mean(axis=2)
    ink = op & (lum < 70)
    # stroke width: 2*distance-transform at ridge pixels of the ink mask
    dt = ndimage.distance_transform_edt(ink)
    ridge = ink & (dt >= ndimage.maximum_filter(dt, size=3) - 1e-6) & (dt > 0)
    stroke = float(np.median(dt[ridge] * 2)) if ridge.any() else 0.0
    body = op & ~ndimage.binary_dilation(ink, iterations=1)
    # colours
    q = (rgb[body] // 16).astype(int)
    keys = q[:, 0] * 256 + q[:, 1] * 16 + q[:, 2]
    _, counts = np.unique(keys, return_counts=True)
    share = counts / max(1, counts.sum())
    main_colours = int((share > 0.02).sum())
    H, S, V = hsv(rgb)
    # dominant hue family and shading layers inside it
    hb = H[body]; sb = S[body]; vb = V[body]
    hist, edges = np.histogram(hb[sb > 0.15], bins=24, range=(0, 360))
    dom = int(np.argmax(hist)) if hist.sum() else -1
    if dom >= 0:
        inh = body & (np.abs(((H - (edges[dom] + 7.5)) + 180) % 360 - 180) < 20) & (S > 0.15)
        vq = np.round(V[inh] * 255 / 12).astype(int)
        _, vc = np.unique(vq, return_counts=True)
        shade_layers = int((vc / max(1, vc.sum()) > 0.06).sum())
    else:
        shade_layers = 0
    # highlight specks: small bright low-sat blobs inside body
    bright = body & (V > 0.92) & (S < 0.18)
    lab, n = ndimage.label(bright)
    sizes = ndimage.sum(bright, lab, range(1, n + 1)) if n else []
    highlight_specks = int(sum(1 for s in sizes if 2 <= s <= 0.05 * body.sum()))
    # gradient-ness: neighbour differences among body pixels
    diff = np.zeros_like(lum)
    for dy, dx in ((0, 1), (1, 0)):
        sh = np.roll(rgb, (dy, dx), axis=(0, 1))
        d = np.abs(rgb - sh).sum(axis=2)
        both = body & np.roll(body, (dy, dx), axis=(0, 1))
        diff = np.where(both, np.maximum(diff, d), diff)
    bb = body & (diff > 0) | body
    flat = (diff <= 6) & body; soft = (diff > 6) & (diff <= 40) & body; hard = (diff > 40) & body
    gradient_ratio = float(soft.sum() / max(1, (soft.sum() + flat.sum())))
    # eyes: black blobs not part of the outline (do not touch the alpha boundary)
    black = op & (lum < 45)
    boundary = vis & ~ndimage.binary_erosion(vis, iterations=2)
    lab, n = ndimage.label(black)
    eye_h = []
    for i in range(1, n + 1):
        m = lab == i
        if m.sum() < 3 or m.sum() > 200 or (m & boundary).any():
            continue
        yy, xx = np.where(m); eh, ew = yy.max() - yy.min() + 1, xx.max() - xx.min() + 1
        if 0.4 <= ew / eh <= 2.5 and yy.min() < y0 + 0.6 * h:
            eye_h.append(eh)
    eye_h = sorted(eye_h, reverse=True)[:2]
    eye_ratio = float(np.mean(eye_h) / h) if eye_h else 0.0
    # beak: largest orange blob in the upper 70% of the body
    orange = op & (H > 12) & (H < 48) & (S > 0.45) & (V > 0.5)
    orange[y0 + int(0.72 * h):, :] = False
    lab, n = ndimage.label(orange)
    beak_ratio = 0.0
    if n:
        sizes = ndimage.sum(orange, lab, range(1, n + 1)); i = int(np.argmax(sizes)) + 1
        yy, xx = np.where(lab == i); beak_ratio = float((xx.max() - xx.min() + 1) / w)
    # blush
    blush = op & (H < 20) | op & (H > 330); blush &= (S > 0.15) & (S < 0.6) & (V > 0.75)
    blush = blush & ~orange
    has_blush = bool(blush.sum() >= 8)
    # texture density: Laplacian energy of body interior (ink dilated away)
    inner = body & ~ndimage.binary_dilation(ink, iterations=2)
    lap = np.abs(ndimage.laplace(lum.astype(float)))
    texture = float(lap[inner].mean()) if inner.any() else 0.0
    # asymmetry: alpha mask vs its mirror about the bbox centre
    m = vis[y0:y1, x0:x1]; mf = m[:, ::-1]
    asym = 1.0 - float((m & mf).sum() / max(1, (m | mf).sum()))
    # edge roughness: boundary length vs convex hull perimeter
    by, bx = np.where(boundary)
    try:
        hull = ConvexHull(np.stack([bx, by], 1)); hull_p = float(hull.area)  # 2D: area attr = perimeter
    except Exception:
        hull_p = 1.0
    rough = float(boundary.sum() / max(1.0, hull_p))
    return {
        "canvas": list(im.size), "visualBounds": [x0, y0, x1, y1], "visualW": w, "visualH": h, "aspectWH": round(w / h, 2), "bottomY": y1,
        "strokePx": round(stroke, 2), "strokePctW": round(100 * stroke / w, 2), "inkFraction": round(float(ink.sum() / max(1, op.sum())), 3),
        "mainColours": main_colours, "shadeLayersDomHue": shade_layers, "highlightSpecks": highlight_specks, "gradientRatio": round(gradient_ratio, 3),
        "eyeHPctBodyH": round(100 * eye_ratio, 1), "beakWPctBodyW": round(100 * beak_ratio, 1), "blush": has_blush,
        "textureLaplacian": round(texture, 2), "asymmetry": round(asym, 3), "edgeRoughness": round(rough, 3),
        "outlineIsBlack": bool(((rgb[ink].sum(axis=1) < 60).mean() > 0.5) if ink.any() else False),
    }

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--sprite", action="append", default=[], help="egg:id of a legacy sprite")
    ap.add_argument("--file", action="append", default=[], help="label=path")
    ap.add_argument("--out", required=True)
    ap.add_argument("--fit-body", type=int, default=0, help="rescale candidates so max(visual w,h)=N before measuring (e.g. 78)")
    args = ap.parse_args()
    os.makedirs(args.out, exist_ok=True)
    rows = []
    for s in args.sprite:
        e, i = s.split(":")
        p = f"assets/png/Character/character_{e}/character_{e}_{i}_0_0.png"
        rows.append({"label": s, "kind": "legacy", "file": p, **metrics(load120(p))})
    for f in args.file:
        label, p = f.split("=", 1)
        rows.append({"label": label, "kind": "candidate", "file": p, **metrics(load120(p, args.fit_body or None))})
    json.dump(rows, open(os.path.join(args.out, "metrics.json"), "w", encoding="utf-8"), ensure_ascii=False, indent=1)
    keys = [k for k in rows[0].keys() if k not in ("file",)]
    with open(os.path.join(args.out, "metrics.csv"), "w", newline="", encoding="utf-8") as fh:
        wr = csv.DictWriter(fh, fieldnames=keys, extrasaction="ignore"); wr.writeheader(); wr.writerows(rows)
    leg = [r for r in rows if r["kind"] == "legacy" and "error" not in r]
    summ = {}
    for k in ("visualW", "visualH", "aspectWH", "strokePx", "strokePctW", "inkFraction", "mainColours", "shadeLayersDomHue", "highlightSpecks", "gradientRatio", "eyeHPctBodyH", "beakWPctBodyW", "textureLaplacian", "asymmetry", "edgeRoughness"):
        v = np.array([r[k] for r in leg], dtype=float)
        summ[k] = {"min": round(float(v.min()), 2), "median": round(float(np.median(v)), 2), "max": round(float(v.max()), 2)}
    summ["blushShare"] = round(float(np.mean([r["blush"] for r in leg])), 2) if leg else None
    summ["outlineBlackShare"] = round(float(np.mean([r["outlineIsBlack"] for r in leg])), 2) if leg else None
    json.dump(summ, open(os.path.join(args.out, "legacy-summary.json"), "w"), indent=1)
    print(json.dumps(summ, indent=1))
    for r in rows:
        if r["kind"] == "candidate":
            print(r["label"], {k: r[k] for k in ("strokePx", "mainColours", "shadeLayersDomHue", "highlightSpecks", "gradientRatio", "eyeHPctBodyH", "textureLaplacian", "edgeRoughness", "outlineIsBlack")})

if __name__ == "__main__":
    main()
