"""Build the game's variable Chinese font subset from its current text.

Input: the unmodified Google Fonts Noto Sans SC variable TTF.
Direct web/*.js, web/*.html and Android bridge messages are scanned; baseline
snapshots are deliberately excluded. This does not rewrite images,
game text, names, or gameplay data.
"""

from __future__ import annotations

import argparse
import hashlib
import html
import json
from pathlib import Path
import re
import unicodedata

import fontTools
from fontTools import subset
from fontTools.ttLib import TTFont


ROOT = Path(__file__).resolve().parents[1]
WEB = ROOT / "web"
OUTPUT = WEB / "fonts"
SOURCE = ROOT / "artifacts" / "font-source" / "NotoSansSC-wght.ttf"
SOURCE_URL = "https://raw.githubusercontent.com/google/fonts/main/ofl/notosanssc/NotoSansSC%5Bwght%5D.ttf"
COMMON_PUNCTUATION = "\u00a0　，。！？：；、（）《》〈〉【】「」『』〔〕［］｛｝—–…·•‘’“”％＋－×÷＝￥¥€£¢°℃‰←→↑↓‹›▲▼△▽★☆"


def source_text() -> tuple[list[Path], set[int], set[int]]:
    files = sorted(path for path in WEB.iterdir() if path.is_file() and path.suffix in {".js", ".html"})
    files += sorted((ROOT / "android" / "app" / "src" / "main" / "java").rglob("*.java"))
    text = "\n".join(path.read_text(encoding="utf-8-sig") for path in files)
    text += html.unescape(text)
    for braced, plain in re.findall(r"\\u(?:\{([0-9a-fA-F]{1,6})\}|([0-9a-fA-F]{4}))", text):
        text += chr(int(braced or plain, 16))
    current = {ord(char) for char in text if ord(char) >= 32 and not unicodedata.category(char).startswith("C")}
    requested = current | set(range(32, 127)) | {ord(char) for char in COMMON_PUNCTUATION}
    return files, current, requested


def names(font: TTFont) -> set[tuple[int, int, int, int, str]]:
    return {(record.nameID, record.platformID, record.platEncID, record.langID, record.toUnicode()) for record in font["name"].names}


def missing_description(codepoints: set[int]) -> list[str]:
    return [f"U+{point:04X} {chr(point)} {unicodedata.name(chr(point), 'UNKNOWN')}" for point in sorted(codepoints)]


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--source", type=Path, default=SOURCE, help="Unmodified upstream variable TTF")
    parser.add_argument("--format", choices=["auto", "woff2", "ttf"], default="auto")
    parser.add_argument("--check", action="store_true", help="Check current text against an existing generated font without rebuilding")
    args = parser.parse_args()
    if not args.source.is_file():
        raise SystemExit(f"Missing source font: {args.source}\nDownload the unmodified font from {SOURCE_URL}")

    files, current, requested = source_text()
    original = TTFont(args.source, recalcTimestamp=False)
    missing_from_source = requested - set(original.getBestCmap())
    if missing_from_source:
        raise SystemExit("Upstream font does not cover requested characters:\n" + "\n".join(missing_description(missing_from_source)))
    source_names = names(original)
    source_axes = [(axis.axisTag, axis.minValue, axis.defaultValue, axis.maxValue) for axis in original["fvar"].axes]
    if not any(tag == "wght" and minimum == 100 and maximum == 900 for tag, minimum, _, maximum in source_axes):
        raise SystemExit("Expected the original variable wght axis from 100 to 900; no static font or instancing is accepted.")

    font_format = args.format
    if font_format == "auto":
        try:
            import brotli  # noqa: F401
        except ImportError:
            font_format = "ttf"
        else:
            font_format = "woff2"
    target = OUTPUT / f"chick-ui.{font_format}"
    if args.check and not target.is_file():
        raise SystemExit(f"Generated font not found: {target}")

    if not args.check:
        OUTPUT.mkdir(parents=True, exist_ok=True)
        options = subset.Options()
        options.name_IDs = ["*"]
        options.name_languages = ["*"]
        options.name_legacy = True
        options.layout_features = ["*"]
        options.glyph_names = True
        options.notdef_glyph = True
        options.notdef_outline = True
        options.recommended_glyphs = True
        options.recalc_timestamp = False
        # Keep fvar / gvar / HVAR and all named instances. Do not instantiate.
        builder = subset.Subsetter(options=options)
        builder.populate(unicodes=requested)
        builder.subset(original)
        original.flavor = "woff2" if font_format == "woff2" else None
        original.save(target)

    generated = TTFont(target, recalcTimestamp=False)
    missing = requested - set(generated.getBestCmap())
    generated_axes = [(axis.axisTag, axis.minValue, axis.defaultValue, axis.maxValue) for axis in generated["fvar"].axes]
    if missing:
        raise SystemExit("Subset is missing current UI characters:\n" + "\n".join(missing_description(missing)))
    if source_axes != generated_axes:
        raise SystemExit("Variable axes changed while subsetting")
    if source_names != names(generated):
        raise SystemExit("Original copyright / naming records changed while subsetting")

    report = {
        "source_url": SOURCE_URL,
        "source_sha256": hashlib.sha256(args.source.read_bytes()).hexdigest(),
        "fonttools_version": fontTools.__version__,
        "files": [path.relative_to(ROOT).as_posix() for path in files],
        "current_text_codepoints": len(current),
        "requested_codepoints": len(requested),
        "covered_codepoints": len(requested - missing),
        "missing_codepoints": missing_description(missing),
        "variable_axes": generated_axes,
        "original_name_records_preserved": True,
        "output": target.relative_to(ROOT).as_posix(),
        "output_bytes": target.stat().st_size,
    }
    if not args.check:
        format_hint = "woff2" if font_format == "woff2" else "truetype"
        (OUTPUT / "font.css").write_text(
            "/* Generated by tools/build-font.py. Noto Sans SC is licensed under SIL OFL 1.1. */\n"
            "@font-face {\n"
            "  font-family: 'Chicken UI';\n"
            f"  src: url('./{target.name}') format('{format_hint}');\n"
            "  font-style: normal;\n"
            "  font-weight: 100 900;\n"
            "  font-display: swap;\n"
            "}\n",
            encoding="utf-8",
        )
        (OUTPUT / "coverage.json").write_text(json.dumps(report, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    original.close()
    generated.close()
    print(json.dumps(report, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()
