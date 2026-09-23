import re,json
from pathlib import Path
p=Path('docs/game-design.md').read_text(encoding='utf-8')
branches={'C':'CUL','H':'HOME','T':'TRADE','O':'OBS','R':'TRIP'}
rows=[]
for line in p.splitlines():
 m=re.match(r'\| ([CHTOR])([1-5S]) (.*?) \| (\d+)／.*? \| (.*?) \|',line)
 if not m:continue
 b,n,name,cost,body=m.groups();body=body.replace('**','')
 when={'C':'新开批次生效','H':'新开批次生效','T':'出售时结算','O':'查看时立即生效','R':'下次寻访生效'}[b]
 if b=='H' and n in ['3','S']:when='下次打扫／新批次生效'
 if b=='H' and n=='2':when='农场脱逃时生效'
 if b=='T' and n=='1':when='购买普通材料时生效'
 if b=='T' and n in ['3','4','5']:when='新收取及出售时生效'
 if b=='O' and n=='5':when='首次收取新品种时生效'
 rows.append({'id':branches[b]+'-'+n,'code':b+n,'branch':branches[b],'node':n,'name':name,'cost':int(cost),'description':body,'summary':body.split('。')[0]+'。','when':when})
assert len(rows)==30
Path('web/skill-data.js').write_text('// Confirmed round-two player copy. Single-level nodes; save IDs are stable.\nexport const SKILLS = '+json.dumps(rows,ensure_ascii=False,indent=2)+';\nexport const SKILL_BY_ID = Object.fromEntries(SKILLS.map(s=>[s.id,s]));\nexport const TRADE_CATEGORIES = ["家常","煎炸","炖煮","烘焙","茶饮","蒸点"];\n',encoding='utf-8')
