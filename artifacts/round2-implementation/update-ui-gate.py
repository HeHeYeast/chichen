from pathlib import Path
p=Path('tools/verify-ui.mjs');s=p.read_text(encoding='utf-8').replace("['integration',['tools/qa-integration-v148.mjs'", "['integration',['tools/qa-round2.mjs'");p.write_text(s,encoding='utf-8')
