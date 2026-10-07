"""Explicit ADB UI checks on a caller-selected device; never installs or clears data.
Taps are ordinary gameplay actions and may save their results.
"""
import argparse
import json
import os
from pathlib import Path
import re
import subprocess
import time
import xml.etree.ElementTree as ET

p = argparse.ArgumentParser()
p.add_argument('--serial', required=True)
p.add_argument('--adb', default=r'D:\gxy_code\_toolchain\android-sdk\platform-tools\adb.exe')
p.add_argument('action', choices=['launch', 'capture', 'text', 'tap', 'back'])
p.add_argument('value', nargs='?')
args = p.parse_args()
adb = [args.adb, '-s', args.serial]


def call(*parts):
    return subprocess.check_output(adb + list(parts), timeout=40,
                                   env={**os.environ, 'ADB_LIBUSB': '1'})


def nodes():
    dumped = call('shell', 'uiautomator', 'dump', '/data/local/tmp/jibao-audit-ui.xml').decode('utf-8', errors='replace')
    if 'dumped to:' not in dumped:
        raise RuntimeError('Fresh UI hierarchy unavailable: ' + dumped)
    raw = call('shell', 'cat', '/data/local/tmp/jibao-audit-ui.xml')
    root = ET.fromstring(raw)
    return raw, list(root.iter('node'))


def controls(items):
    return [{k: n.get(k) for k in ['text', 'content-desc', 'clickable', 'enabled', 'bounds']}
            for n in items if n.get('package') == 'com.jibao.kitchen'
            and (n.get('text') or n.get('content-desc'))]


if args.action == 'launch':
    print(call('shell', 'am', 'start', '-n', 'com.jibao.kitchen/.MainActivity').decode('utf-8'))
elif args.action == 'back':
    call('shell', 'input', 'keyevent', 'KEYCODE_BACK')
elif args.action == 'capture':
    if not args.value:
        p.error('capture requires a local PNG output path')
    target = Path(args.value)
    target.parent.mkdir(parents=True, exist_ok=True)
    target.write_bytes(call('exec-out', 'screencap', '-p'))
    raw, items = nodes()
    target.with_suffix('.xml').write_bytes(raw)
    target.with_suffix('.json').write_text(json.dumps(controls(items), ensure_ascii=False, indent=2), encoding='utf-8')
    print(json.dumps(controls(items), ensure_ascii=False, indent=2))
elif args.action == 'text':
    print(json.dumps(controls(nodes()[1]), ensure_ascii=False, indent=2))
elif args.action == 'tap':
    items = nodes()[1]
    matches = [n for n in items if n.get('package') == 'com.jibao.kitchen'
               and args.value in [n.get('text'), n.get('content-desc')]
               and n.get('clickable') == 'true' and n.get('enabled') == 'true']
    if len(matches) != 1:
        raise RuntimeError(f'Expected one visible enabled control for {args.value!r}, found {len(matches)}')
    bounds = list(map(int, re.findall(r'\d+', matches[0].get('bounds', ''))))
    if len(bounds) != 4 or bounds[2] <= bounds[0] or bounds[3] <= bounds[1]:
        raise RuntimeError('Control is clipped; scroll it into view before tapping')
    x1, y1, x2, y2 = bounds
    call('shell', 'input', 'tap', str((x1+x2)//2), str((y1+y2)//2))
    time.sleep(.5)
