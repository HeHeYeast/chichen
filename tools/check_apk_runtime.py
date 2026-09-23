"""Verify every bundled byte against the manifest AND current source files."""
import hashlib
import json
from pathlib import Path
import sys
import zipfile


def verify(apk, root):
    with zipfile.ZipFile(apk) as archive:
        manifest = json.loads(archive.read('assets/runtime-manifest.json'))
        expected = {'assets/' + row['path'] for row in manifest['assets']}
        expected.update({'assets/runtime-manifest.json', 'assets/web/app-version.json'})
        actual = [n for n in archive.namelist() if n.startswith('assets/') and not n.endswith('/')]
        if len(actual) != len(expected) or set(actual) != expected:
            raise ValueError('APK runtime file list differs')
        release = json.loads((root / 'android/release.json').read_text(encoding='utf-8-sig'))
        version = json.loads(archive.read('assets/web/app-version.json'))
        for key in ('applicationId', 'versionName', 'versionCode'):
            if version[key] != release[key] or manifest[key] != release[key]:
                raise ValueError('APK runtime version differs')
        for row in manifest['assets']:
            path = (root / row['path']).resolve()
            if not path.is_relative_to(root.resolve()):
                raise ValueError('Runtime path escapes project')
            content = archive.read('assets/' + row['path'])
            if content != path.read_bytes() or hashlib.sha256(content).hexdigest() != row['sha256']:
                raise ValueError('APK/source bytes differ: ' + row['path'])
        return len(manifest['assets'])


if __name__ == '__main__':
    print(f"Verified {verify(Path(sys.argv[1]), Path(__file__).resolve().parents[1])} APK runtime files against sources")
