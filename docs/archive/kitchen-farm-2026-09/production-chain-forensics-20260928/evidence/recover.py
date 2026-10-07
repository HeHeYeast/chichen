from pathlib import Path
import sqlite3,json,hashlib,datetime,collections,subprocess
out=Path('docs/production-chain-forensics-20260928/evidence');root=Path.home()/'.codex'
c=sqlite3.connect((root/'state_5.sqlite').as_uri()+'?mode=ro',uri=True);c.row_factory=sqlite3.Row
h=sqlite3.connect((root/'thread_history_1.sqlite').as_uri()+'?mode=ro',uri=True);h.row_factory=sqlite3.Row
prefixes=['01a0b8de','01a0d0a6','01a0d334','01a0db3e','01a0e0d9','01a0e199','01a0e2fa','01a0e370','01a0e380','01a0e386','01a0e3b1']
threads=[dict(r) for r in c.execute('SELECT id,title,cwd,rollout_path,created_at,updated_at,thread_source FROM threads') if any(r['id'].startswith(x) for x in prefixes)]
(out/'threads.json').write_text(json.dumps(threads,ensure_ascii=False,indent=2),encoding='utf-8')
msgs=[];imgs=[];calls=[];events=[];schemas={}
for db in ['state_5.sqlite','thread_history_1.sqlite','logs_2.sqlite']:
 co=sqlite3.connect((root/db).as_uri()+'?mode=ro',uri=True)
 schemas[db]=co.execute("SELECT name,sql FROM sqlite_master WHERE type='table'").fetchall();co.close()
(out/'sqlite-schemas.json').write_text(json.dumps(schemas,ensure_ascii=False,indent=2),encoding='utf-8')
for t in threads:
 tid=t['id'];stem=tid[:8];mp=[]
 for row in h.execute('SELECT * FROM thread_items WHERE thread_id=? ORDER BY rollout_ordinal',(tid,)):
  d=json.loads(row['item_json']);meta={'thread_id':tid,'turn_id':row['turn_id'],'item_id':row['item_id'],'ordinal':row['rollout_ordinal'],'time':datetime.datetime.fromtimestamp(row['created_at_ms']/1000,datetime.timezone(datetime.timedelta(hours=8))).isoformat(),'type':row['item_type']}
  if d['type'] in ['userMessage','agentMessage']:
   if d['type']=='userMessage':txt='\n'.join(x.get('text','') for x in d.get('content',[]) if x.get('type')=='text'); attachments=[x for x in d.get('content',[]) if x.get('type')!='text'];meta['attachments']=[{k:v for k,v in a.items() if not (isinstance(v,str) and v.startswith('data:'))} for a in attachments]
   else:txt=d.get('text','');meta['phase']=d.get('phase')
   # Skip system/environment scaffolding masquerading as user content
   meta['text']=txt;msgs.append(meta);mp.append('## '+str(meta['ordinal'])+' · '+meta['time']+' · '+meta['type']+'\n\n'+txt+'\n')
  elif d['type']=='imageGeneration':
   meta.update({k:v for k,v in d.items() if k not in ['result','id','type']});imgs.append(meta)
  elif d['type'] in ['commandExecution','fileChange','mcpToolCall','imageView']:
   meta['detail']={k:v for k,v in d.items() if k not in ['aggregatedOutput','changes','result','content','id']};events.append(meta)
 (out/(stem+'-messages.md')).write_text('# '+t['title'].split('\n')[0]+'\n\n'+'\n'.join(mp),encoding='utf-8')
 rp=Path(t['rollout_path'])
 if rp.exists():
  for ln,line in enumerate(rp.open(encoding='utf-8'),1):
   d=json.loads(line);pay=d.get('payload',{})
   if d.get('type')=='response_item' and pay.get('type') in ['function_call','custom_tool_call']:
    calls.append({'thread_id':tid,'source':str(rp),'line':ln,'ordinal':d.get('ordinal'),'time':d.get('timestamp'),'type':pay.get('type'),'name':pay.get('name'),'call_id':pay.get('call_id'),'input':pay.get('input',pay.get('arguments',''))})
for name,data in [('messages',msgs),('image-events',imgs),('tool-calls',calls),('execution-events',events)]:
 with (out/(name+'.jsonl')).open('w',encoding='utf-8') as f:
  for row in data:f.write(json.dumps(row,ensure_ascii=False)+'\n')
print('threads',len(threads),'messages',len(msgs),'image events',len(imgs),'calls',len(calls),'execution events',len(events))
for t in threads:
 print(t['id'][:8],sum(x['thread_id']==t['id'] and x['type']=='userMessage' for x in msgs),'users',sum(x['thread_id']==t['id'] for x in imgs),'images')
# Save the observed dirty baseline rather than modifying any existing project file.
(out/'git-status-before.txt').write_text(subprocess.run(['git','status','--porcelain'],capture_output=True,text=True,encoding='utf-8').stdout,encoding='utf-8')
