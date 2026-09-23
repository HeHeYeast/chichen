"""Inventory original game media without changing source assets."""
from pathlib import Path
from collections import Counter
import csv
import json
import re
from PIL import Image

ROOT = Path(__file__).resolve().parents[1]


def classify(path):
    rel = path.relative_to(ROOT).as_posix()
    name = path.name
    if rel.startswith('assets/png/Character/'):
        match = re.match(r'character_(\d+)_(\d+)_', name)
        if match and 'character_rev' not in rel:
            return ('鸡宝' if match[1] == '0' else '鸭宝', f'character:{match[1]}:{match[2]}')
        return '角色特殊方向与辅助', name
    if rel.startswith('assets/png/Tool/Tool0/'):
        match = re.match(r'tool_0_(\d+)_(\d+)_', name)
        kind = '厨房阶段' if match[1] == '0' else '农场时段与前景'
        return kind, f'facility:{match[1]}:{match[2]}'
    if rel.startswith('assets/png/Tool/Tool1/'):
        match = re.match(r'tool_1_(\d+)_(\d+)_', name)
        return '厨具等级与状态', f'cookware:{match[1]}:{match[2]}'
    if rel.startswith('assets/png/Tool/Tool2/'):
        match = re.match(r'tool_2_(\d+)_', name)
        return '调味料', f'ingredient:{match[1]}'
    if rel.startswith('assets/png/Egg/'):
        return '蛋与蛋壳', name.rsplit('_', 2)[0]
    if rel.startswith('assets/png/'):
        parent = path.parent.name if path.parent.name != 'png' else '根目录标识'
        return parent, name
    if rel.startswith('assets/music/'):
        return '背景音乐', path.stem
    return '音效与提示音', path.stem


def build():
    files = sorted(p for p in (ROOT / 'assets/png').rglob('*') if p.suffix.lower() in {'.png', '.jpg', '.jpeg'})
    files += sorted((ROOT / 'assets/music').rglob('*.mp3'))
    files += sorted((ROOT / 'res/raw').glob('*.mp3'))
    rows = []
    for path in files:
        category, group = classify(path)
        row = dict(category=category, logical_group=group, source=path.relative_to(ROOT).as_posix(), width='', height='', format=path.suffix[1:].upper(), mode='', bytes=path.stat().st_size)
        if path.suffix.lower() != '.mp3':
            with Image.open(path) as im:
                row.update(width=im.width, height=im.height, format=im.format, mode=im.mode)
        rows.append(row)
    counts = Counter(row['category'] for row in rows)
    summary = dict(scope='assets/png images + assets/music MP3 + res/raw MP3; excludes SDK resources, fonts, XML and new sample art', total_files=len(rows), images=sum(r['format'] != 'MP3' for r in rows), audio=sum(r['format'] == 'MP3' for r in rows), by_category=dict(sorted(counts.items())), groups={category: len({r['logical_group'] for r in rows if r['category'] == category}) for category in counts})
    with (ROOT / 'docs/original-asset-files.csv').open('w', encoding='utf-8-sig', newline='') as handle:
        writer = csv.DictWriter(handle, fieldnames=list(rows[0]))
        writer.writeheader()
        writer.writerows(rows)
    (ROOT / 'docs/original-asset-summary.json').write_text(json.dumps(summary, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')
    print(json.dumps(summary, ensure_ascii=False, indent=2))


if __name__ == '__main__':
    build()
