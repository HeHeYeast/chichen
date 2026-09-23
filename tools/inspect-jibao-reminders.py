"""Read only this app's alarm/notification records; never persist other apps' notifications."""
import json,re,subprocess,sys
from pathlib import Path
from datetime import datetime
ADB=['D:/gxy_code/_toolchain/android-sdk/platform-tools/adb.exe','-s','AYXGVB6126000685','shell']
def read(*args):
    return subprocess.check_output(ADB+list(args),text=True,encoding='utf-8',errors='replace')
alarms=read('dumpsys','alarm').splitlines()
pending=[]
for i,line in enumerate(alarms):
    if 'tag=*walarm*:com.jibao.kitchen.' in line and i>0 and 'origWhen' in alarms[i-1]:
        pending.append('\n'.join(alarms[i-1:i+4]))
notifications=[]
active=False
for line in read('dumpsys','notification').splitlines():
    if 'NotificationRecord(' in line:
        active='pkg=com.jibao.kitchen ' in line or 'pkg=com.jibao.kitchen)' in line
        if active: notifications.append([line.strip()])
    elif active:
        if not line.strip(): active=False
        elif re.search(r'postTime|mCreationTimeMs|mRankingTimeMs|android.title|android.text|importance=|mImportance=',line): notifications[-1].append(line.strip())
package=[l.strip() for l in read('dumpsys','package','com.jibao.kitchen').splitlines() if re.search(r'versionCode=|versionName=|firstInstallTime=|lastUpdateTime=',l)]
activity=[l.strip() for l in read('dumpsys','activity','activities').splitlines() if 'topResumedActivity' in l]
result={'checkedAt':datetime.now().astimezone().isoformat(),'package':package,'gameInForeground':any('com.jibao.kitchen' in l for l in activity),'pendingAlarms':pending,'activeNotifications':notifications}
if len(sys.argv)>1:Path(sys.argv[1]).write_text(json.dumps(result,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(result,ensure_ascii=False,indent=2))
