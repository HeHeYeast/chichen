import json,hashlib,zipfile
from pathlib import Path
root=Path.cwd();apk=root/'artifacts/round2-implementation/chick-kitchen-1.5.0-candidate.apk'
meta=json.loads((root/'android/.candidate-release.json').read_text(encoding='utf-8-sig'))
assert hashlib.sha256(apk.read_bytes()).hexdigest()==meta['sha256']
with zipfile.ZipFile(apk) as z:
 m=json.loads(z.read('assets/runtime-manifest.json'))
 for a in m['assets']:
  current=(root/a['path']).read_bytes();assert z.read('assets/'+a['path'])==current,a['path']
report={'apk':apk.name,'filesVerified':len(m['assets']),'sha256':meta['sha256'],'nativeDeviceTest':False}
(root/'artifacts/round2-implementation/apk-verification.json').write_text(json.dumps(report,indent=2)+'\n',encoding='utf-8')
print(json.dumps(report))
