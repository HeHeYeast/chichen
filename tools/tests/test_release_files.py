import hashlib
import importlib.util
import json
from pathlib import Path
import tempfile
import unittest
import zipfile

ROOT = Path(__file__).resolve().parents[2]
def module(name):
    spec = importlib.util.spec_from_file_location(name, ROOT / f'tools/{name}.py')
    result = importlib.util.module_from_spec(spec); spec.loader.exec_module(result)
    return result
snapshot = module('source_snapshot')
runtime = module('check_apk_runtime')


class ReleaseFileTests(unittest.TestCase):
    def setUp(self):
        temp = tempfile.TemporaryDirectory(); self.addCleanup(temp.cleanup)
        self.root = Path(temp.name)
        for name in ('web','android','android/build','.signing','artifacts/device-backups'):
            (self.root / name).mkdir(parents=True, exist_ok=True)
        (self.root/'web/app.js').write_text('source', encoding='utf-8')

    def test_source_snapshot_round_trip_excludes_keys_saves_and_build(self):
        for name in ('.signing/key.jks','artifacts/device-backups/save.json','android/build/stale.apk'):
            (self.root/name).write_bytes(b'private-or-generated')
        archive = snapshot.create(self.root)
        manifest = snapshot.verify(archive)
        self.assertEqual([r['path'] for r in manifest['files']], ['web/app.js'])
        (self.root/'web/app.js').write_text('edited', encoding='utf-8')
        with zipfile.ZipFile(archive) as z:
            self.assertEqual(z.read('web/app.js'), b'source')
        self.assertNotEqual(snapshot.create(self.root), archive)

    def test_damaged_snapshot_fails_verification(self):
        path = self.root/'broken.zip'
        with zipfile.ZipFile(path,'w') as z:
            z.writestr('web/app.js','changed')
            z.writestr('snapshot-manifest.json',json.dumps({'files':[{'path':'web/app.js','bytes':6,'sha256':'0'*64}]}))
        with self.assertRaisesRegex(ValueError,'checksum'):
            snapshot.verify(path)

    def apk(self, content=b'source'):
        release={'applicationId':'com.jibao.kitchen','versionName':'1.4.7','versionCode':14}
        (self.root/'android/release.json').write_text(json.dumps(release),encoding='utf-8')
        path=self.root/'test.apk'
        manifest={**release,'assets':[{'path':'web/app.js','sha256':hashlib.sha256(b'source').hexdigest()}]}
        with zipfile.ZipFile(path,'w') as z:
            z.writestr('assets/runtime-manifest.json',json.dumps(manifest))
            z.writestr('assets/web/app-version.json',json.dumps(release))
            z.writestr('assets/web/app.js',content)
        return path

    def test_apk_bytes_match_source_and_manifest(self):
        self.assertEqual(runtime.verify(self.apk(),self.root),1)

    def test_apk_same_file_names_but_changed_bytes_are_rejected(self):
        with self.assertRaisesRegex(ValueError,'bytes differ'):
            runtime.verify(self.apk(b'stale!'),self.root)

    def test_source_changed_after_packaging_is_rejected(self):
        path=self.apk();(self.root/'web/app.js').write_text('new source',encoding='utf-8')
        with self.assertRaisesRegex(ValueError,'bytes differ'):
            runtime.verify(path,self.root)


if __name__ == '__main__':
    unittest.main()
