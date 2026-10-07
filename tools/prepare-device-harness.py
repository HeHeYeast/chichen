"""Prepare an isolated, debuggable Android QA copy; never modifies the release app."""
from pathlib import Path
import os
import shutil

root = Path(__file__).resolve().parents[1]
source = root / 'android'
target = root / 'artifacts/device-qa/android-harness'
target.mkdir(parents=True, exist_ok=True)
for name in ['build.gradle', 'settings.gradle', 'gradle.properties', 'local.properties', 'release.json']:
    shutil.copy2(source / name, target / name)
shutil.copytree(source / 'app/src', target / 'app/src', dirs_exist_ok=True)
gradle = (source / 'app/build.gradle').read_text(encoding='utf-8')
gradle = gradle.replace("applicationId 'com.jibao.kitchen'", "applicationId 'com.jibao.kitchen.deviceqa'")
gradle = gradle.replace('signingConfigs {', "signingConfigs {\n        debug { storeFile rootProject.file('deviceqa.jks'); storePassword 'deviceqa-test'; keyAlias 'deviceqa'; keyPassword 'deviceqa-test' }")
asset_root = Path(os.environ.get('CHICK_DEVICE_QA_PAYLOAD', source / 'generated-assets')).resolve()
if not (asset_root / 'web/index.html').is_file():
    raise RuntimeError('QA payload entry is missing')
assets = asset_root.as_posix()
gradle = gradle.replace("rootProject.file('generated-assets')", f"file('{assets}')")
(target / 'app/build.gradle').write_text(gradle, encoding='utf-8')
manifest = target / 'app/src/main/AndroidManifest.xml'
manifest.write_text(manifest.read_text(encoding='utf-8').replace('com.jibao.kitchen.update-backup', 'com.jibao.kitchen.deviceqa.update-backup'), encoding='utf-8')
activity = target / 'app/src/main/java/com/jibao/kitchen/MainActivity.java'
code = activity.read_text(encoding='utf-8').replace('return !ENTRY.equals(request.getUrl().toString());', 'return !(ENTRY + "?review=1&fixture=ready").equals(request.getUrl().toString());')
code = code.replace('web.loadUrl(ENTRY);', '''WebView.setWebContentsDebuggingEnabled(true);
        web.loadUrl(ENTRY + "?review=1&fixture=ready");''')
activity.write_text(code, encoding='utf-8')
strings = target / 'app/src/main/res/values/strings.xml'
strings.write_text(strings.read_text(encoding='utf-8').replace('鸡宝厨房', '鸡宝触屏测试'), encoding='utf-8')
print(target)
