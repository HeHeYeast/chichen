"""Read-only check of the latest APK against current sources and older releases."""
from pathlib import Path
from hashlib import sha256
from zipfile import ZipFile
from datetime import datetime, timezone
import json
import sys

root = Path(__file__).resolve().parents[1]
release = json.loads((root / 'android/release.json').read_text(encoding='utf-8-sig'))
releases = root / 'artifacts/releases'
metadata = json.loads((releases / f"chick-kitchen-{release['versionName']}.release.json").read_text(encoding='utf-8-sig'))
apk = releases / metadata['file']
assert sha256(apk.read_bytes()).hexdigest() == metadata['sha256']
assert metadata['versionCode'] == release['versionCode']
identity = json.loads((root / 'android/release-identity.json').read_text(encoding='utf-8-sig'))
assert metadata['certificateSha256'] == identity['certificateSha256']
with ZipFile(apk) as archive:
    manifest = json.loads(archive.read('assets/runtime-manifest.json'))
    assert manifest['contentHash'] == metadata['runtimeContentHash']
    assert manifest['files'] == len(manifest['assets']) == metadata['runtimeFiles']
    for asset in manifest['assets']:
        current = (root / asset['path']).read_bytes()
        assert sha256(current).hexdigest() == asset['sha256'], asset['path']
        assert archive.read('assets/' + asset['path']) == current, asset['path']
    previous = []
    for file in releases.glob('*.release.json'):
        old = json.loads(file.read_text(encoding='utf-8-sig'))
        if old['versionCode'] >= release['versionCode']:
            continue
        assert old['certificateSha256'] == metadata['certificateSha256'], file.name
        assert sha256((releases / old['file']).read_bytes()).hexdigest() == old['sha256'], file.name
        previous.append(old)
    unchanged = 0
    if previous:
        predecessor = max(previous, key=lambda old: old['versionCode'])
        with ZipFile(releases / predecessor['file']) as older:
            for asset in manifest['assets']:
                path = asset['path']
                if path.startswith(('assets/', 'res/')) or path in ('web/data.js', 'web/recipes.js'):
                    assert older.read('assets/' + path) == archive.read('assets/' + path), path
                    unchanged += 1
report = dict(checkedAt=datetime.now(timezone.utc).isoformat(), version=release['versionName'],
              sourceFilesMatched=manifest['files'], originalFilesUnchanged=unchanged,
              sha256=metadata['sha256'], runtimeContentHash=manifest['contentHash'],
              sameCertificateMetadata=True, oldApksUnchanged=len(previous), nativeDeviceTest=False)
text = json.dumps(report, ensure_ascii=False, indent=2)
if len(sys.argv) > 1:
    destination = root / sys.argv[1]
    destination.parent.mkdir(parents=True, exist_ok=True)
    destination.write_text(text + '\n', encoding='utf-8')
print(text)
