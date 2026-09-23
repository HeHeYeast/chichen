"""Create/verify an immutable source and art snapshot; excludes saves and keys.

Restore manually into a NEW directory after verification. Never overlays a live
project or a phone. Archives on this disk are rollback aids, not offsite backups.
"""
import argparse
from datetime import datetime, timezone
import hashlib
import json
from pathlib import Path
import uuid
import zipfile

ROOT = Path(__file__).resolve().parents[1]
TREES = ('web', 'tools', 'tests', 'docs', 'android', 'assets', 'res',
         'artifacts/original-source/sources', 'artifacts/font-source')
EXCLUDE = {'build', '.gradle', 'generated-assets', '__pycache__', 'jadx', 'node_modules'}


def verify(path):
    with zipfile.ZipFile(path) as archive:
        manifest = json.loads(archive.read('snapshot-manifest.json'))
        expected = {item['path'] for item in manifest['files']} | {'snapshot-manifest.json'}
        if len(archive.namelist()) != len(expected) or set(archive.namelist()) != expected:
            raise ValueError('Snapshot file list differs')
        for item in manifest['files']:
            name = item['path']
            if name.startswith(('/', '\\')) or '..' in Path(name).parts or ':' in name:
                raise ValueError('Unsafe snapshot path')
            data = archive.read(name)
            if len(data) != item['bytes'] or hashlib.sha256(data).hexdigest() != item['sha256']:
                raise ValueError('Snapshot checksum differs: ' + name)
    return manifest


def create(root=ROOT):
    destination = root / 'artifacts/source-backups'
    destination.mkdir(parents=True, exist_ok=True)
    path = destination / (datetime.now().strftime('%Y%m%d-%H%M%S-') + uuid.uuid4().hex[:8] + '.zip')
    files = [p for p in root.iterdir() if p.is_file() and (p.suffix in {'.md', '.json', '.mjs', '.cmd'} or p.name == '.gitignore')]
    for tree in TREES:
        files.extend(p for p in (root / tree).rglob('*') if p.is_file()
                     and not EXCLUDE.intersection(p.relative_to(root).parts)
                     and p.name not in {'local.properties', '.build.lock', '.candidate-release.json'}
                     and p.suffix not in {'.zip', '.pyc'})
    records = []
    with zipfile.ZipFile(path, 'x', zipfile.ZIP_DEFLATED) as archive:
        for file in sorted(files):
            if file.is_symlink() or not file.resolve().is_relative_to(root.resolve()):
                raise ValueError('Snapshot refuses links outside source tree')
            data = file.read_bytes()
            name = file.relative_to(root).as_posix()
            archive.writestr(name, data)
            records.append({'path': name, 'bytes': len(data), 'sha256': hashlib.sha256(data).hexdigest()})
        archive.writestr('snapshot-manifest.json', json.dumps({'schema': 1, 'kind': 'source-and-art',
            'createdAt': datetime.now(timezone.utc).isoformat(), 'files': records}, ensure_ascii=False))
    verify(path)
    return path


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--verify', type=Path)
    args = parser.parse_args()
    if args.verify:
        print(f"Verified {len(verify(args.verify)['files'])} source/art files")
    else:
        print(create())
