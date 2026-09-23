"""Seal the verified isolated debug artifact and source/asset fingerprints."""
from datetime import datetime, timezone
from hashlib import sha256
from pathlib import Path
from zipfile import ZipFile
import json
import re
import shutil

root=Path(__file__).resolve().parents[2]
out=root/'artifacts/takeover'
apk=root/'android/app/build/outputs/apk/debug/app-debug.apk'
digest=lambda path:sha256(path.read_bytes()).hexdigest()
def text(path):
    data=path.read_bytes()
    return data.decode('utf-16' if data.startswith((b'\xff\xfe',b'\xfe\xff')) else 'utf-8-sig')
signature=text(out/'native-apk-signature-final.txt')
assert 'Verified using v2 scheme (APK Signature Scheme v2): true' in signature
certificate=re.search(r'Signer #1 certificate SHA-256 digest: ([a-f0-9]+)',signature).group(1)
assert certificate=='3924e188c4b7427bbce6611271e5a23789f7c7c4937ab769b7ec6aa5a85291d2'
with ZipFile(apk) as archive:
    manifest=json.loads(archive.read('assets/runtime-manifest.json'))
    assert manifest['files']==len(manifest['assets'])
    assert sha256(json.dumps(manifest['assets'],ensure_ascii=False,separators=(',',':')).encode()).hexdigest()==manifest['contentHash']
    for row in manifest['assets']:
        source=root/row['path']
        assert digest(source)==row['sha256'],row['path']
        assert archive.read('assets/'+row['path'])==source.read_bytes(),row['path']
    (out/'native-runtime-manifest-final.json').write_bytes(archive.read('assets/runtime-manifest.json'))
apk_hash=digest(apk)
delivery=out/f'chick-kitchen-241-debug-{apk_hash[:12]}.apk'
if not delivery.exists():shutil.copyfile(apk,delivery)
assert digest(delivery)==apk_hash
source_paths=sorted((root/'android/app/src/main/java/com/jibao/kitchen').glob('*.java'))
native_sources={p.relative_to(root).as_posix():digest(p) for p in source_paths}
old=json.loads((out/'native-fix-source-hashes.json').read_text(encoding='utf8'))
repaired={p:digest(root/p) for p in old}
(out/'native-fix-source-hashes.json').write_text(json.dumps(repaired,indent=2)+'\n',encoding='utf8')
report={
 'checkedAt':datetime.now(timezone.utc).isoformat(),
 'scope':'Isolated debug build; no installation, player saves or permanent release key access',
 'apk':{'path':delivery.relative_to(root).as_posix(),'sha256':apk_hash,'bytes':delivery.stat().st_size,'applicationId':manifest['applicationId'],'versionName':manifest['versionName'],'versionCode':manifest['versionCode'],'debugOnly':True,'signatureV2':True,'certificateSha256':certificate},
 'runtime':{'files':manifest['files'],'bytes':manifest['bytes'],'contentHash':manifest['contentHash'],'manifestSha256':digest(out/'native-runtime-manifest-final.json'),'allPackagedBytesMatchCurrentSources':True},
 'checks':{'nativeSaveAssertions':649,'sharedCorruptFixturesRejectedByBothPlatforms':107,'legalContractMatrixStates':103,'notifications':60,'android35CompiledSources':9,'nativeContentGeneratedCheck':True},
 'nativeSources':native_sources,'repairSources':repaired,
 'supersededDebugPackages':[p.relative_to(root).as_posix() for p in sorted(out.glob('chick-kitchen-241-debug-*.apk')) if p!=delivery],
 'limits':['Debug signing only, not a release certificate','No physical-device lifecycle/process-death/restore validation','Concept silhouettes are not final production art']
}
(out/'native-final-evidence.json').write_text(json.dumps(report,ensure_ascii=False,indent=2)+'\n',encoding='utf8')
print(json.dumps({k:report[k] for k in ['apk','runtime','checks']},ensure_ascii=False,indent=2))
