from pathlib import Path
p=Path('tools/audit-round2-balance.mjs');s=p.read_text(encoding='utf-8').replace('returnAt=Math.max(s.batch.ends,NOW+24/visits*3600000)', 'returnAt=Math.max(s.batch.ends,NOW+24/visits*3600000)+1');p.write_text(s,encoding='utf-8')
