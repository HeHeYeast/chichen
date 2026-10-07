from pathlib import Path
import json,hashlib,re,datetime,sys
B=Path(__file__).resolve().parents[1];ev=B/'evidence';workspace=B.parents[1]
errors=[]
reports=['01-history-recovery-report.md','02-forensics-master-table.md','03-timeline.md','04-imagegen-prompt-archive.md','05-success-vs-failure-comparison.md','06-repeated-failure-patterns.md','07-successful-production-patterns.md','08-whole-image-conclusion.md','09-evidence-gaps.md']
for name in ['README.md']+reports:
 if not (B/name).is_file():errors.append('Missing report '+name)
cases=json.loads((ev/'cases.json').read_text(encoding='utf-8'))
keys=['user','work','plan','actual','tools','prompts','refs','edits','middle','final','feedback','gate']
for c in cases:
 for k in keys:
  if not c.get(k):errors.append(c['id']+' missing '+k)
imgs=json.loads((ev/'image-archive-index.json').read_text(encoding='utf-8'));matched=0
for r in imgs:
 p=Path(r['savedPath'])
 if not p.is_file() or hashlib.sha256(p.read_bytes()).hexdigest()!=r.get('sha256'):errors.append('Image missing/changed '+r['archive_id'])
 arc=(B/'prompts'/(r['archive_id']+'.md')).read_text(encoding='utf-8')
 if r['revisedPrompt'] not in arc:errors.append('Prompt transcription mismatch '+r['archive_id'])
 for ref in r['exact_project_copies']:
  p=workspace/ref
  if not p.is_file() or hashlib.sha256(p.read_bytes()).hexdigest()!=r['sha256']:errors.append('Copy changed '+ref)
 matched+=bool(r['exact_project_copies'])
# Check report and generated prompt navigation, excluding quotations in recovered historical message files.
links=[]
for p in list(B.glob('*.md'))+list((B/'prompts').glob('*.md')):
 for dest in re.findall(r'!?\[[^\]]*\]\(([^)]+)\)',p.read_text(encoding='utf-8')):
  if dest.startswith(('https:','http:','#')):continue
  q=p.parent/dest.split('#')[0]
  if q.name=='verification.json' and q.parent.resolve()==ev.resolve():continue
  if not q.exists():errors.append('Broken link '+p.name+' => '+dest)
  else:links.append(q.resolve())
before=json.loads((ev/'runtime-hashes-before.json').read_text(encoding='utf-8'));changed=[]
for rel,sha in before.items():
 p=workspace/rel
 if not p.is_file() or hashlib.sha256(p.read_bytes()).hexdigest()!=sha:changed.append(rel)
if changed:errors.append('Runtime changed: '+str(changed))
manifest={str(p):hashlib.sha256(p.read_bytes()).hexdigest() for p in set(links) if p.is_file() and not p.is_relative_to(B)}
(ev/'cited-source-hashes.json').write_text(json.dumps(manifest,ensure_ascii=False,indent=2),encoding='utf-8')
result={'verified_at':datetime.datetime.now(datetime.timezone(datetime.timedelta(hours=8))).isoformat(),'reports':len(reports),'cases':len(cases),'fields_per_case':len(keys),'completed_image_events':len(imgs),'original_prompts_verified':sum(r['level']=='ORIGINAL' for r in imgs),'source_pngs_verified':len(imgs),'events_with_exact_project_png_copies':matched,'local_links_checked':len(links),'cited_external_files_hashed':len(manifest),'runtime_files_checked':len(before),'runtime_changed':changed,'errors':errors,'scope':'Static forensic document and source verification only. Historical UI QA not re-run. No ImageGen or Computer Use called in this task.'}
(ev/'verification.json').write_text(json.dumps(result,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(result,ensure_ascii=False,indent=2));sys.exit(bool(errors))
