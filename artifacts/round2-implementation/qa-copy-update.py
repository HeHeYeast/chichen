from pathlib import Path
p=Path('tools/qa-recipe-book-v14.mjs');s=p.read_text(encoding='utf-8').replace("unknown.includes('还未认识的味道')", "unknown.includes('尚未收录')");p.write_text(s,encoding='utf-8')
