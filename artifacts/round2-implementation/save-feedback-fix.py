from pathlib import Path
p=Path('web/app.js');s=p.read_text(encoding='utf-8').replace('if(!save()){state=previous;renderControls();return null;}', "if(!save()){state=previous;renderControls();alertBox('这次操作未保存，CP和物品已恢复原状。请重试；若反复出现，可先导出备份。');return null;}")
p.write_text(s,encoding='utf-8')
