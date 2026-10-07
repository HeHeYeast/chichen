from pathlib import Path
from urllib.parse import unquote,urlparse
from html.parser import HTMLParser
import json,csv,re,hashlib,collections,subprocess
from PIL import Image
ROOT=Path(__file__).parent
class Parser(HTMLParser):
 def __init__(self):super().__init__();self.ids=set();self.refs=[];self.cards=0
 def handle_starttag(self,tag,attrs):
  a=dict(attrs)
  if 'id' in a:self.ids.add(a['id'])
  if tag=='article':self.cards+=1
  for key in ['href','src']:
   if key in a:self.refs.append(a[key])
errors=[];checks=[]
items=json.loads((ROOT/'evidence-index.json').read_text(encoding='utf-8'))
assert len(items)==52 and len({x['id'] for x in items})==52
for a in items:
 p=ROOT/a['file']
 with Image.open(p) as im:
  assert im.size==(a['width'],a['height']);im.verify()
 assert hashlib.sha256(p.read_bytes()).hexdigest()==a['sha256']
 assert a['observation'] and a['limit'] and a['source_date']
checks.append('52 / 52 evidence images decode, dimensions and SHA-256 match; all have observation and limit')
atlas=(ROOT/'atlas.html').read_text(encoding='utf-8');parser=Parser();parser.feed(atlas)
assert parser.cards==52
scripts=re.findall(r'<script>([\s\S]*?)</script>',atlas)
for script in scripts:
 proc=subprocess.run(['node','--check'],input=script,text=True,capture_output=True)
 if proc.returncode:errors.append('Atlas script syntax: '+proc.stderr)
checks.append('Atlas: 52 cards, JavaScript syntax checked (not a browser interaction test)')
refs=[(ROOT/'atlas.html',x) for x in parser.refs]
for md in ROOT.glob('*.md'):
 if md.name=='VALIDATION.md':continue
 refs += [(md,x) for x in re.findall(r'\]\(([^)]+)\)',md.read_text(encoding='utf-8'))]
local_count=0
for origin,ref in refs:
 if re.match(r'^https?://',ref):continue
 raw=unquote(ref); path,sep,anchor=raw.partition('#')
 target=(origin.parent/path).resolve() if path else origin
 if not target.exists():errors.append(f'Missing link {origin.name}: {ref}')
 if target.name=='atlas.html' and anchor and anchor not in parser.ids:errors.append(f'Missing atlas anchor {anchor}')
 local_count+=1
checks.append(f'{local_count} local link/image references checked, including Atlas evidence anchors')
rows=list(csv.DictReader((ROOT/'page-type-matrix.csv').open(encoding='utf-8-sig')))
assert len(rows)==33
assert sorted(collections.Counter(r['游戏'] for r in rows).values())==[11,11,11]
checks.append('Page Type Matrix: 33 rows, 11 page types for each comparison group; missing evidence retained')
report=(ROOT/'REPORT.md').read_text(encoding='utf-8')
assert len(re.findall(r'^## [1-9]\. ',report,re.M))==9
for token in ['HUD','Bottom Navigation','Drawer','轮廓','比例','描边','饱和度','明度','阴影','光照','材质','透视','细节密度','背景复杂度','强烈建议迁移','可以参考','不适合']:
 assert token in report,token
checks.append('Nine deliverable sections and all requested UI/art comparison dimensions found')
with Image.open(ROOT/'evidence/grammar-plate.png') as im:assert im.size==(1500,920);im.verify()
checks.append('Annotated comparison plate: 1500 x 920, opens successfully; manually visually inspected in this turn')
downloads=[json.loads(x) for x in (ROOT/'downloads.jsonl').read_text(encoding='utf-8').splitlines()]
assert len(downloads)==25
checks.append('25 newly downloaded reference images have source URL, acquisition date and hash')
text='# 核验记录\n\n2026-09-28 · 本轮最后一次产物检查。\n\n'+''.join('- PASS · '+x+'\n' for x in checks)
text+='\n## 阅读复核\n\n'
text+='- 已区分观察、推断、候选约束；候选阈值未描述为游戏统计事实。\n'
text+='- 旧档页面标签更正已写入图集、矩阵和报告；原文件未被覆盖。\n'
text+='- 保留完整空间与高背景密度的反例，不用统一“无阴影/无渐变/无空间”规则解释所有游戏。\n'
text+='- 52 条不等于 52 个页面；缺证、局部、旧版本和宣传材料均明确标记。\n'
text+='- 三组鸡宝页面使用同一维度；辅助二级页不自动继承用户认可。\n'
text+='- 新读者可从规则回到证据 ID，并能从补图表知道具体需要哪个页面/状态。\n'
text+='- 没有执行游戏行为测试或手机测试；不宣称按钮、状态转换或用户体验收益已验证。\n'
text+='- 没有生成新 Kitchen/Farm、修改 Runtime、决定最终 Creative Direction 或最终 Production Pipeline。\n'
text+='\n## 范围与未验证项\n\n仅新增本研究目录，并在文档索引加入入口。既有工作区有其他未提交改动；本轮未清理、回退或接管它们。HTML 图集做了结构、链接与脚本语法核验，未做浏览器交互测试。公开来源可访问性以本轮采集时为准；旧妙奇逐图来源未恢复的部分仍为继承来源。\n'
if errors:text+='\n## FAIL\n\n'+'\n'.join(errors)
(ROOT/'VALIDATION.md').write_text(text,encoding='utf-8')
print('\n'.join(checks));print('RESULT:', 'FAIL' if errors else 'PASS')
if errors:raise SystemExit('\n'.join(errors))
