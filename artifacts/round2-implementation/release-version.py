import json
from pathlib import Path
p=Path('android/release.json');d=json.loads(p.read_text(encoding='utf-8'));d.update(versionName='1.5.0',versionCode=16,notes='第二轮厨房优化：30项单级手艺、跨方向试配、招牌与拼盘经营、安心等候、返料、普通已知出品复刻、轻装寻访。重做手艺、寻访、采购与观察界面，旧手艺一次性全额退点，原有批次与库存保留。');p.write_text(json.dumps(d,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
