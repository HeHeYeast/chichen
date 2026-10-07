"""Build an auditable measurement report from untouched browser captures."""
from pathlib import Path
import json,hashlib
from PIL import Image,ImageChops
R=Path(__file__).resolve().parents[1];O=R/'artifacts/journey-precision'
read=lambda p:json.loads(p.read_text(encoding='utf-8-sig'))
ref=read(O/'reference-geometry.json');layout=read(O/'final/layout.json')
base=read(O/'baseline-geometry.json');result={}
components=read(R/'docs/journey-visual-20260925/asset-sheets/J09-reference-components.json')['components']
environments=read(R/'docs/journey-visual-20260925/asset-sheets/J11-reference-environment.json')['components']
def source_rect(e,page):
 x,y,w,h=e['referenceVisualBounds'];origin,dx={'map':(16,3),'region':(504,2),'return':(992,3)}[page];f=844/1030
 return [(x-origin)*f+dx,(y-16)*f,w*f,h*f]
for e in components:
 key=e['id']
 if key.startswith('node-'):ref['map'][key]=source_rect(e,'map')
 if key.startswith('place-'):ref['region'][key]=source_rect(e,'region')
 if key.startswith(('tree','grass','rock','map-icon')):ref['map'][key]=source_rect(e,'map')
 if key.startswith('botanical-') or key=='record-note':ref['return'][key]=source_rect(e,'return')
for e in environments:
 p='region' if e['id']=='place-environment' else 'return' if e['id']=='hero-ground' else 'map'
 ref[p][e['id']]=source_rect(e,p)
def rect(e,visual=False):
 if not e:return None
 e=e.get('visual',e) if visual else e
 return [e[k] for k in ['x','y','width','height']]
def union(es):
 rs=[rect(e,True) for e in es]
 if not rs:return None
 x=min(r[0] for r in rs);y=min(r[1] for r in rs)
 return [x,y,max(r[0]+r[2] for r in rs)-x,max(r[1]+r[3] for r in rs)-y]
def pct(r):return [v/d*100 for v,d in zip(r,[390,844,390,844])] if r else None
def fmt(r):return ' / '.join(f'{v:.2f}' for v in r) if r else '—'
for page in ['map','region','return']:
 s=next(s for s in layout['shots'] if s['name']==page+'-390');es=s['elements'];b=s['bounds']
 first=lambda f:next((e for e in es if f(e)),None)
 cl=lambda c:first(lambda e:c in (e['class'] or '').split())
 asset=lambda a:rect(first(lambda e:e['asset']==a),True)
 rt={'header':rect(b['header']),'title':rect(first(lambda e:e['tag']=='H1')['textBounds']),'CTA':rect(b['CTA']),'bottomNav':rect(b['bottomNav'])}
 if page=='map':
  for k in 'VTRB':rt['node-'+k]=asset('precision-ref-node-'+k)
  labels=[e for e in es if e['tag']=='STRONG' and e['text'] in ['谷地早市','林间茶坡','溪岸小集','风湾盐田']]
  for k,t in zip('VTRB',['谷地早市','林间茶坡','溪岸小集','风湾盐田']):rt['label-'+k]=rect(next(e for e in labels if e['text']==t))
  rt['party']=union([e for e in es if 'journey-traveller' in (e['class'] or '').split()]);rt['destination']=rect(b['secondaryAction'])
  for k in ['map-icon','map-hills','map-ground','map-river','tree-left','tree-round','tree-tall','grass-rock','rock']:rt[k]=asset('precision-ref-'+k)
 elif page=='region':
  for k in ['place-V-0','place-V-1','place-environment']:rt[k]=asset('precision-ref-'+k)
  headings=[e for e in es if 'journey-section-title' in (e['class'] or '')]
  for i,e in enumerate(headings):rt['heading'+str(i+1)]=rect(e)
  for k,a in [('pouch','pouch'),('magnifier','magnifier'),('note','note')]:rt[k]=asset('precision-ref-action-'+a)
  for k,t in [('label-place0','菜畦'),('label-place1','谷物棚边')]:rt[k]=rect(first(lambda e:e['tag']=='STRONG' and e['text']==t))
  for i,t in enumerate(['补材料','找标本','寻见闻']):rt['action-name'+str(i)]=rect(first(lambda e:e['tag']=='STRONG' and e['text']==t))
  for i,t in enumerate(['收集地区材料','发现动植物','听听当地故事']):rt['action-description'+str(i)]=rect(first(lambda e:e['tag']=='SMALL' and e['text']==t))
  rt['target']=rect(b['target']);rt['team']=rect(b['dynamicCharacter']);rt['time']=rect(cl('regional-footer-caption'))
 else:
  rt.update(party=rect(cl('journey-return-party')),ribbon=rect(cl('journey-ribbon')),hero=rect(cl('journey-found-object')),record=rect(cl('journey-return-notes')))
  rt['discovery-title']=rect(first(lambda e:e['tag']=='H2' and e['width']>0))
  rt['envelope']=union([e for e in es if e['asset'] in ['precision-envelope-back','precision-envelope-front']])
  rt['reward']=union([e for e in es if 'journey-reward-slot' in (e['class'] or '').split()])
  rt['botanical']=union([e for e in es if e['asset'] in ['precision-ref-botanical-left','precision-ref-botanical-right']])
  for k in ['botanical-left','botanical-right','record-note','hero-ground']:rt[k]=asset('precision-ref-'+k)
  rt['secondaryCTA']=rect(first(lambda e:e['tag']=='BUTTON' and e['text']=='查看地区发现'))
 result[page]={}
 for key,m in ref[page].items():
  r=rt.get(key);delta=[v-u for u,v in zip(m,r)] if r else None
  result[page][key]={'mockup':m,'runtime':r,'delta':delta,'mockupPercent':pct(m),'runtimePercent':pct(r),'deltaPercentagePoints':pct(delta),'baselineRuntime':base.get(page,{}).get(key,{}).get('runtime')}
  if key.startswith('node-'):
   result[page][key]['centerMockupPercent']=[(m[0]+m[2]/2)/390*100,(m[1]+m[3]/2)/844*100]
   result[page][key]['centerRuntimePercent']=[(r[0]+r[2]/2)/390*100,(r[1]+r[3]/2)/844*100]
(O/'final-geometry.json').write_text(json.dumps(result,ensure_ascii=False,indent=2),encoding='utf-8')
md=['# 最终几何与字体实测','', '统一 viewport：390 × 844。坐标顺序均为 x / y / width / height；Delta = Runtime − Mockup。百分比分母为整页，不是局部容器。','',
'Mockup 的地标、环境和地点插画由原稿语义分割的 alpha 边界换算；其他区域沿用修改前记录的边界（光学读数约 ±2px）。Runtime 的 SVG 按 viewBox 与 preserveAspectRatio 换算可见绘制边界；文字记录浏览器排版框，不能把字形墨迹边界与排版框视为完全等价。','',
'地图队伍位置随真实进度移动；归来奖励宽度按真实单槽计算。标题实际文字长度不同会改变字宽，此处仍原样披露差值。','']
for page,items in result.items():
 md+=['## '+page,'','|元素|Mockup px|Runtime px|Delta px|Mockup %|Runtime %|Delta 百分点|','|---|---|---|---|---|---|---|']
 for key,d in items.items():md.append('|'+key+'|'+'|'.join(fmt(d[k]) for k in ['mockup','runtime','delta','mockupPercent','runtimePercent','deltaPercentagePoints'])+'|')
 md+=['']
md+=['## 四个地标中心与可见尺寸','','|地标|Mockup center x/y %|Runtime center x/y %|Mockup visual w/h %|Runtime visual w/h %|','|---|---|---|---|---|']
for k in 'VTRB':
 d=result['map']['node-'+k];md.append('|'+k+'|'+'|'.join(fmt(v) for v in [d['centerMockupPercent'],d['centerRuntimePercent'],d['mockupPercent'][2:],d['runtimePercent'][2:]])+'|')
md+=['','## Runtime 字体、颜色与行高逐项记录','','字体 family/size/weight/line-height/color 的全部原始记录见 final/layout.json；此表摘录所有可见语义文本。所有字重均来自真实字体文件，font-synthesis:none。','', '|页面|文字|字号|字重|行高|颜色|family|','|---|---|---|---|---|---|---|']
for page in ['map','region','return']:
 s=next(s for s in layout['shots'] if s['name']==page+'-390')
 for e in s['elements']:
  if e['tag'] in ['H1','H2','H3','STRONG','SMALL'] and e['width'] and e['text']:
   md.append('|'+page+'|'+e['text'].replace('|','/')+'|'+'|'.join(e[k] for k in ['size','weight','lineHeight','color','font'])+'|')
(O/'GEOMETRY.md').write_text('\n'.join(md)+'\n',encoding='utf-8')
before=read(O/'scope-before.json');changed=[];missing=[]
for name,sha in before.items():
 p=R/name
 if not p.exists():missing.append(name)
 elif hashlib.sha256(p.read_bytes()).hexdigest()!=sha:changed.append(name)
frozenOld=R/'artifacts/journey-family/runtime/frozen-business.png';frozenNew=O/'state-regression/frozen-business.png'
pixel=None
if frozenOld.exists() and frozenNew.exists():
 a=Image.open(frozenOld).convert('RGB');b=Image.open(frozenNew).convert('RGB')
 pixel={'sameSize':a.size==b.size,'differentPixels':sum(1 for p in ImageChops.difference(a,b).getdata() if p!=(0,0,0)) if a.size==b.size else None}
(O/'scope-verification.json').write_text(json.dumps({'changedExistingFiles':changed,'missingExistingFiles':missing,'frozenBusinessPixelComparison':pixel},ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps({'measuredElements':sum(map(len,result.values())),'changedExistingFiles':changed,'frozenBusiness':pixel},ensure_ascii=False))
