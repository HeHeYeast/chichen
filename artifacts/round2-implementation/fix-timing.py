from pathlib import Path
p=Path('web/exploration.js');s=p.read_text(encoding='utf-8').replace('hours=route.hours*(light?.8:1)', 'hours=Number((route.hours*(light?.8:1)).toFixed(2))');p.write_text(s,encoding='utf-8')
p=Path('tests/round2.test.mjs');s=p.read_text(encoding='utf-8').replace('s.batch=null;s.ingredients={0:1};','s.batch=null;s.cp=100000;s.ingredients={0:1};');p.write_text(s,encoding='utf-8')
p=Path('web/journal-ui.js');s=p.read_text(encoding='utf-8').replace('原版规则：每批先有 10% 机会将对应凤凰放入抽取池，再抽取 24 枚蛋；不代表必出，也不是每只 10%。', '每批开火有10%机会开启本时段凤凰的出现机会；开启后仍需逐枚抽取，不保证这一批能收到。');p.write_text(s,encoding='utf-8')
