from pathlib import Path
p=Path('web/progression.js');s=p.read_text(encoding='utf-8').replace('s.progress.skills={};s.progress.trade.category=null;', 's.progress.skills={};s.progress.trade.category=null;delete s.progress.replicate;');p.write_text(s,encoding='utf-8')
