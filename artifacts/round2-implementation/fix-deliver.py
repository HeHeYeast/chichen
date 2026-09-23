from pathlib import Path
p=Path('artifacts/round2-implementation/deliver.mjs');s=p.read_text(encoding='utf-8');s=s.replace(chr(96)+'artifacts/source-backups/20260921-214239-7027ae2d.zip'+chr(96),'artifacts/source-backups/20260921-214239-7027ae2d.zip');p.write_text(s,encoding='utf-8')
