"""Generic native-art production pipeline. CLI output deliberately omits content spoilers."""
from pathlib import Path
import argparse, hashlib, json, re, shutil, sys
import numpy as np
from PIL import Image, ImageDraw, ImageFont
from scipy import ndimage

VERSION=1
ROOT=Path(__file__).resolve().parents[1]
KINDS={'species','materials','cards','mementos','regulars'}
PROFILES={
 'species':{'full':[512,512],'portrait':[256,256],'silhouette':[256,256]},
 'materials':{'ingredient-icon':[128,128],'specimen-cutout':[256,192],'shop-bundle':[256,256]},
 'cards':{'discovery-vignette':[480,270]},
 'mementos':{'display':[256,256],'small-icon':[48,48]},
 'regulars':{'portrait':[256,256]},
}
STYLE="""Use only the attached original small game PNGs as visual-language references, not as identities to copy. Draw one original asset for the old chicken-duck kitchen collection game. Simple slightly uneven near-black/brown outlines, a few opaque flat colors, at most one tiny flat shadow, tiny simple eyes when relevant. Low detail, compact silhouette, natural asymmetry, charming and surprising, NOT a polished modern mascot, not 3D, no soft light, no gradients, no glossy eyes, no uniform cheek blush, no detailed feathers or realistic food texture. No words, numbers, labels, symbols, watermark, border or ground shadow. Genuinely transparent background. The complete subject must fit with generous transparent margin. Square 1024 canvas unless the brief requests a landscape vignette. Preserve species and food identity, choose the specified non-default colors. References are style only."""
CHARACTER="""Stable IDLE POSE: standing, sitting, squatting or compact resting, supported weight, feet/abdomen grounded. No running, jumping, falling, wide waving or animation-dependent gesture. Not creepy, no body horror, no tied/twisted neck or strange anatomy. A compact chicken/duck world character, not a food object with a pasted face. Target visible width/height 0.8–1.25; no huge external props, long appendages or extreme width. One main memorable idea, 1–2 secondary details maximum, clear at 48px. Creative freedom is allowed; there is no food-area percentage requirement."""
def read(p): return json.loads(Path(p).read_text(encoding='utf-8'))
def write(p,data):
 p=Path(p);p.parent.mkdir(parents=True,exist_ok=True)
 tmp=p.with_suffix(p.suffix+'.tmp');tmp.write_text(json.dumps(data,ensure_ascii=False,indent=2)+'\n',encoding='utf-8');tmp.replace(p)
def digest(p):return hashlib.sha256(Path(p).read_bytes()).hexdigest()
def job_hash(j):return hashlib.sha256(json.dumps(j,sort_keys=True,ensure_ascii=False).encode()).hexdigest()
def safe_id(value):
 if not isinstance(value,str) or not re.fullmatch(r'[A-Za-z0-9][A-Za-z0-9_-]{0,95}',value):raise ValueError('UNSAFE_ID')
 return value
def within(base,relative):
 p=(Path(base)/relative).resolve()
 if not p.is_relative_to(Path(base).resolve()):raise ValueError('PATH_ESCAPE')
 return p
def validate_jobs(data):
 if data.get('version')!=VERSION or not isinstance(data.get('jobs'),list):raise ValueError('INVALID_JOB_SCHEMA')
 seen=set()
 for j in data['jobs']:
  safe_id(j['id'])
  if j['id'] in seen:raise ValueError('DUPLICATE_ID')
  seen.add(j['id'])
  if j['kind'] not in KINDS:raise ValueError('UNKNOWN_KIND')
  if not isinstance(j['prompt'],str) or not j['prompt'].strip():raise ValueError('EMPTY_PROMPT')
  if not j.get('references'):raise ValueError('MISSING_STYLE_REFERENCE')
  if any(not (ROOT/p).is_file() for p in j['references']):raise ValueError('MISSING_REFERENCE_FILE')
  if j.get('variants')!=PROFILES[j['kind']]:raise ValueError('UNSUPPORTED_VARIANT_PROFILE')
 return data
def prepare(content,briefs,concepts,out):
 data=read(content);art=read(briefs) if briefs else {}
 choices=read(concepts) if concepts else {}
 refs=choices.get('references',{})
 jobs=[]
 for kind in PROFILES:
  lookup={str(x['content']):x for x in art.get(kind,[])}
  for row in data.get(kind,[]):
   cid=row['id'];a=lookup.get(str(cid),{})
   aid=a.get('id',f'ART-{cid}' if kind!='materials' else f'ART-MAT-{cid}')
   concept=choices.get('concepts',{}).get(str(cid),{})
   name=row.get('name',row.get('title',str(cid)))
   if kind=='species' and not concept:raise ValueError('SPECIES_REQUIRES_AUTHORED_CONCEPT')
   inputs=refs.get('duck' if row.get('egg')==1 else 'chicken',[]) if kind=='species' else refs.get(kind,refs.get('objects',[]))
   brief=concept.get('design') or row.get('art') or a.get('brief') or name
   if kind=='regulars':brief=concept.get('design',name+'；'+row.get('stages',[{}])[0].get('text',''))
   if kind=='cards':brief+='。一个横向小情境插图，关键物件在中间60%，480:270横向构图，少量环境色块，透明外背景，禁止任何文字，不需要画框。'
   elif kind in ['materials','mementos']:brief+='。只画一份完整物品，不要人物、脸、眼睛或可爱吉祥物，不要sheet、不画多个版本；少量关键大形，透明背景。'
   elif kind=='regulars':brief+='。鸡鸭厨房世界的常客，简朴旧图鉴半身肖像；稳定坐姿，紧凑，表情小而温和，有限色块。'
   prompt=STYLE+'\n'+(CHARACTER if kind=='species' else '')+'\n内容身份：'+name+'。\nArt Brief: '+brief
   if concept.get('palette'):prompt+='\n配色：'+concept['palette']
   if kind=='species':prompt+='\n物种：'+('鸭宝，正常宽扁鸭嘴与蹼足' if row.get('egg') else '鸡宝，短尖喙与小鸡足')+'。料理材料：'+a.get('foodAndMaterial','')+'。这些是食材线索，不能覆盖上面的具体Concept或重新强制食品身体。'
   jobs.append(dict(id=aid,contentId=cid,identityKey=row.get('key'),region=row.get('region'),kind=kind,concept=concept,prompt=prompt,references=inputs,variants=PROFILES[kind]))
 result=validate_jobs({'version':VERSION,'jobs':jobs})
 write(Path(out)/'jobs.json',result)
 return {'planned':len(jobs),'kinds':{k:sum(j['kind']==k for j in jobs) for k in PROFILES}}
def jobdata(out):
 return validate_jobs(read(Path(out)/'jobs.json'))
def getjob(out,id):
 return next(j for j in jobdata(out)['jobs'] if j['id']==safe_id(id))
def select_candidate(out,id,attempt):
 out=Path(out);j=getjob(out,id)
 if attempt<1:raise ValueError('INVALID_ATTEMPT')
 record=read(out/'candidates'/id/f'r{attempt:03}.json')
 if record['jobHash']!=job_hash(j):raise ValueError('STALE_CONCEPT')
 if digest(within(out,record['source']))!=record['sourceHash']:raise ValueError('SOURCE_HASH_MISMATCH')
 write(out/'current'/f'{id}.json',record)
 return {'selected':1,'attempt':attempt}
def ingest(out,id,source,provider='native-imagegen',prompt=None):
 out=Path(out);j=getjob(out,id);source=Path(source)
 if provider!='native-imagegen':raise ValueError('UNSUPPORTED_PROVIDER')
 Image.open(source).verify()
 folder=out/'candidates'/id;folder.mkdir(parents=True,exist_ok=True)
 n=len(list(folder.glob('r*.png')))+1;target=folder/f'r{n:03}.png'
 shutil.copyfile(source,target)
 entry={'source':str(target.relative_to(out)),'sourceHash':digest(target),'provider':provider,'prompt':prompt or j['prompt'],'references':[{'path':p,'sha256':digest(ROOT/p)} for p in j['references']],'jobHash':hashlib.sha256(json.dumps(j,sort_keys=True,ensure_ascii=False).encode()).hexdigest(),'attempt':n}
 write(folder/f'r{n:03}.json',entry);write(out/'current'/f'{id}.json',entry)
 return {'registered':1,'attempt':n}
def analyze(source,kind):
 im=Image.open(source).convert('RGBA');arr=np.array(im);a=arr[:,:,3]
 if not np.any(a>16):raise ValueError('EMPTY_ALPHA')
 if np.mean(a<8)<.02:raise ValueError('OPAQUE_BACKGROUND')
 labels,count=ndimage.label(a>16)
 sizes=np.bincount(labels.ravel());slices=ndimage.find_objects(labels)
 components=[{'area':int(sizes[i+1]),'bbox':[s[1].start,s[0].start,s[1].stop,s[0].stop]} for i,s in enumerate(slices) if s is not None]
 # Only remove truly isolated one/two-pixel artifacts. Never remove a visible component.
 tiny=np.flatnonzero((sizes<=2)&(np.arange(len(sizes))>0))
 if len(tiny):arr[np.isin(labels,tiny),3]=0
 im=Image.fromarray(arr);box=im.getchannel('A').point(lambda p:255 if p>16 else 0).getbbox()
 if not box:raise ValueError('EMPTY_AFTER_CLEANUP')
 x0,y0,x1,y1=box;ratio=(x1-x0)/(y1-y0)
 errors=[]
 if kind=='species' and not .55<=ratio<=1.65:errors.append('EXTREME_ASPECT')
 if min(x0,y0,im.width-x1,im.height-y1)<2:errors.append('SOURCE_TOUCHES_EDGE')
 return im,box,components,errors
def normalize(im,box,size,kind,variant):
 im=im.crop(box);w,h=size
 scale=min(w*(.8 if variant=='full' else .76)/im.width,h*(.8 if variant=='full' else .76)/im.height)
 nw,nh=max(1,round(im.width*scale)),max(1,round(im.height*scale))
 pic=im.resize((nw,nh),Image.Resampling.LANCZOS)
 x=round((w-nw)/2);y=round(h*.92-nh) if variant=='full' and kind=='species' else round((h-nh)/2)
 out=Image.new('RGBA',(w,h));out.alpha_composite(pic,(x,y))
 if variant=='silhouette':
  alpha=out.getchannel('A');out=Image.new('RGBA',(w,h),(64,55,46,0));out.putalpha(alpha)
 return out,{'scale':scale,'translation':[x-box[0]*scale,y-box[1]*scale],'bounds':[x,y,nw,nh],'groundAnchor':[.5,(y+nh)/h]}
def process(out,id=None):
 out=Path(out);jobs=jobdata(out)['jobs'];count=0;failed=0
 for j in jobs:
  if id and j['id']!=id:continue
  p=out/'current'/f"{j['id']}.json"
  if not p.exists():continue
  src=read(p);source=within(out,src['source'])
  if src['jobHash']!=job_hash(j):raise ValueError('STALE_CONCEPT')
  if digest(source)!=src['sourceHash']:raise ValueError('SOURCE_HASH_MISMATCH')
  try:
   im,box,components,errors=analyze(source,j['kind'])
  except ValueError as e:
   write(out/'qa'/f"{j['id']}.json",{'passed':False,'errors':[str(e)],'sourceHash':src['sourceHash']});failed+=1;continue
  folder=out/'processed'/j['id'];folder.mkdir(parents=True,exist_ok=True)
  variants={}
  for variant,size in j['variants'].items():
   image,geometry=normalize(im,box,size,j['kind'],variant);path=folder/(variant+'.png');image.save(path)
   alpha=np.array(image.getchannel('A'),dtype=float);mass=alpha.sum();yy,xx=np.indices(alpha.shape)
   actual=image.getchannel('A').point(lambda p:255 if p>16 else 0).getbbox()
   if not actual or min(actual[0],actual[1],size[0]-actual[2],size[1]-actual[3])<2:errors.append('UNSAFE_OUTPUT_BOUNDS')
   variants[variant]={'path':str(path.relative_to(out)),'width':size[0],'height':size[1],'sha256':digest(path),'visibleBounds':list(actual) if actual else None,'centroid':[float((xx*alpha).sum()/mass/size[0]),float((yy*alpha).sum()/mass/size[1])],**geometry}
  metadata={'version':VERSION,'processingVersion':1,'source':src,'kind':j['kind'],'identityKey':j['identityKey'],'sourceSize':list(im.size),'sourceBounds':list(box),'components':components,'sourceLayersUnavailable':True,'variants':variants}
  write(folder/'metadata.json',metadata)
  write(out/'qa'/f"{j['id']}.json",{'passed':not errors,'errors':errors,'sourceHash':src['sourceHash'],'variantCount':len(variants),'componentCount':len(components)})
  # Review thumbnails on both backgrounds; no automatic approval.
  review=out/'review'/j['id'];review.mkdir(parents=True,exist_ok=True)
  master=Image.open(folder/(next(iter(j['variants']))+'.png'))
  for size in [120,48]:
   thumb=master.copy();thumb.thumbnail((size,size),Image.Resampling.LANCZOS)
   for label,bg in [('light','#eeeae1'),('dark','#303634')]:
    canvas=Image.new('RGB',(size,size),bg);canvas.paste(thumb,((size-thumb.width)//2,(size-thumb.height)//2),thumb);canvas.save(review/f'{size}-{label}.png')
  count+=1;failed+=bool(errors)
 return {'processed':count,'automaticFailures':failed}
REVIEW_AXES=['legacyStyle','identity','foodIdentity','memory','idle','friendlyAnatomy','smallSize','bounds','distinctness']
def approve(out,id,review_file):
 out=Path(out);j=getjob(out,id);r=read(review_file);src=read(out/'current'/f'{id}.json');qa=read(out/'qa'/f'{id}.json')
 if src['jobHash']!=job_hash(j):raise ValueError('STALE_CONCEPT')
 if r.get('sourceHash')!=src['sourceHash']:raise ValueError('STALE_REVIEW')
 if r.get('outputHash')!=digest(out/'processed'/id/'metadata.json'):raise ValueError('STALE_OUTPUT_REVIEW')
 if not qa['passed']:raise ValueError('AUTOMATIC_QA_FAILED')
 if not all(r.get('checks',{}).get(k) is True for k in REVIEW_AXES):raise ValueError('INCOMPLETE_INTERNAL_REVIEW')
 if not isinstance(r.get('notes'),str) or len(r['notes'])<12:raise ValueError('MISSING_REVIEW_EVIDENCE')
 write(out/'approved'/f'{id}.json',r)
 return {'internallyApproved':1}
def signature(p,frame=None):
 im=Image.open(p).convert('RGBA')
 if frame:
  x,y,w,h=frame;im=im.crop((x,y,x+w,y+h))
 box=im.getchannel('A').getbbox();im=im.crop(box)
 tiny=Image.new('RGBA',(48,48));im.thumbnail((44,44));tiny.alpha_composite(im,((48-im.width)//2,(48-im.height)//2))
 ar=np.array(tiny);mask=ar[:,:,3]>32;rgb=ar[:,:,:3][mask]
 hist=np.histogramdd(rgb,bins=4,range=[(0,256)]*3)[0].ravel();hist/=max(hist.sum(),1)
 return mask,hist
def similarity(out,legacy=None,legacy_index=None):
 out=Path(out);jobs=[j for j in jobdata(out)['jobs'] if j['kind']=='species'];sig={};rows=[]
 for j in jobs:
  p=out/'processed'/j['id']/'full.png'
  if p.exists():sig[j['id']]=signature(p)
 for n,k in enumerate(sig):
  for other in list(sig)[n+1:]:
   m,h=sig[k];m2,h2=sig[other];iou=float((m&m2).sum()/max(1,(m|m2).sum()));color=float(np.abs(h-h2).sum()/2)
   rows.append({'a':k,'b':other,'silhouetteIoU':round(iou,4),'colorDistance':round(color,4),'flagged':iou>.87 and color<.35})
 old=[];legacy_sig=[]
 if legacy_index:
  for entry in read(legacy_index):
   sprite=entry.get('sprite') or {};path=sprite.get('file',entry['path']).split('#')[0];frame=sprite.get('frame')
   legacy_sig.append((entry['key'],signature(ROOT/path.lstrip('/'),frame),{'path':path,'frame':frame}))
 elif legacy:
  paths=sorted(Path(legacy).glob('character_[01]/character_*_0_0.png'))
  legacy_sig=[(p.name,signature(p),{'path':str(p)}) for p in paths]
 if legacy_sig:
  for k,(m,h) in sig.items():
   near=[]
   for name,(m2,h2),source in legacy_sig:
    iou=float((m&m2).sum()/max(1,(m|m2).sum()));color=float(np.abs(h-h2).sum()/2)
    near.append({'old':name,'silhouetteIoU':round(iou,4),'colorDistance':round(color,4),**source})
   old.append({'id':k,'nearest':sorted(near,key=lambda r:r['silhouetteIoU']-r['colorDistance'],reverse=True)[:3]})
 result={'version':VERSION,'legacySourceCount':len(legacy_sig),'pairs':sorted(rows,key=lambda r:r['silhouetteIoU']-r['colorDistance'],reverse=True),'oldComparisons':old,'sourceHashes':{j['id']:read(out/'current'/f"{j['id']}.json")['sourceHash'] for j in jobs if j['id'] in sig}}
 write(out/'similarity.json',result)
 return {'compared':len(sig),'pairs':len(rows),'flagged':sum(r['flagged'] for r in rows),'legacyCompared':len(old),'legacySourceCount':len(legacy_sig)}
def publish(out,destination):
 out=Path(out);destination=Path(destination).resolve()
 if not destination.is_relative_to(ROOT):raise ValueError('PUBLISH_OUTSIDE_PROJECT')
 jobs=jobdata(out)['jobs'];previous=read(destination/'manifest.json') if (destination/'manifest.json').exists() else {'version':VERSION,'assets':{}}
 if previous.get('version')!=VERSION or not isinstance(previous.get('assets'),dict):raise ValueError('INVALID_PUBLISHED_MANIFEST')
 assets=dict(previous['assets'])
 for j in jobs:
  prior=assets.get(j['id'])
  if prior and any(prior.get(k)!=j.get(k) for k in ['kind','contentId','identityKey']):raise ValueError('PUBLISHED_IDENTITY_DRIFT')
  if any(k!=j['id'] and (a['kind'],a['contentId'])==(j['kind'],j['contentId']) for k,a in assets.items()):raise ValueError('DUPLICATE_PUBLISHED_CONTENT')
 dup=read(out/'batch-review.json') if (out/'batch-review.json').exists() else {}
 for j in jobs:
  id=j['id'];src=read(out/'current'/f'{id}.json');r=read(out/'approved'/f'{id}.json');qa=read(out/'qa'/f'{id}.json')
  if src['jobHash']!=job_hash(j):raise ValueError('STALE_CONCEPT')
  if not qa['passed'] or r['sourceHash']!=src['sourceHash'] or qa['sourceHash']!=src['sourceHash']:raise ValueError('UNAPPROVED_OR_STALE')
  if digest(within(out,src['source']))!=src['sourceHash']:raise ValueError('SOURCE_HASH_MISMATCH')
  if r.get('outputHash')!=digest(out/'processed'/id/'metadata.json'):raise ValueError('STALE_OUTPUT_REVIEW')
  if dup.get('sourceHashes',{}).get(id)!=src['sourceHash']:raise ValueError('BATCH_REVIEW_REQUIRED')
  m=read(out/'processed'/id/'metadata.json');target=destination/id/digest(out/'processed'/id/'metadata.json')[:12];target.mkdir(parents=True,exist_ok=True)
  variants={}
  for v,d in m['variants'].items():
   p=within(out,d['path'])
   if digest(p)!=d['sha256']:raise ValueError('OUTPUT_HASH_MISMATCH')
   shutil.copyfile(p,target/(v+'.png'));variants[v]={**d,'path':'/'+str((target/(v+'.png')).relative_to(ROOT)).replace('\\','/'),'available':True}
  public_metadata={k:v for k,v in m.items() if k!='source'}
  public_metadata['source']={'sourceHash':src['sourceHash'],'provider':src['provider']}
  public_metadata['variants']=variants
  write(target/'metadata.json',public_metadata)
  assets[id]={'id':id,'contentId':j['contentId'],'kind':j['kind'],'region':j['region'],'identityKey':j['identityKey'],'productionStatus':'art-approved','runtimeQA':'pending','sourceHash':src['sourceHash'],'safeArea':{'insetPercent':10,'topInsetPercent':12,'footClearancePercent':8} if j['kind']=='species' else None,'variants':variants,'metadata':'/'+str((target/'metadata.json').relative_to(ROOT)).replace('\\','/')}
 manifest={'version':VERSION,'assets':assets}
 write(destination/'manifest.json',manifest)
 js='// Generated by tools/art_pipeline.py. No content descriptions.\nexport const PRODUCTION_ART = '+json.dumps(assets,ensure_ascii=False,indent=2)+';\n'
 (destination/'manifest.js').write_text(js,encoding='utf-8')
 return {'published':len(jobs),'totalPublished':len(assets),'runtimeQA':'pending'}
def status(out):
 out=Path(out);jobs=jobdata(out)['jobs']
 valid=0;stale=0
 for j in jobs:
  id=j['id'];a=out/'approved'/f'{id}.json'
  if not a.exists():continue
  r=read(a);c=read(out/'current'/f'{id}.json');m=out/'processed'/id/'metadata.json'
  current=r['sourceHash']==c['sourceHash'] and c['jobHash']==job_hash(j) and m.exists() and r.get('outputHash')==digest(m)
  valid+=current;stale+=not current
 return {'planned':len(jobs),'generated':len(list((out/'current').glob('*.json'))),'approved':valid,'staleReviews':stale,'processed':len(list((out/'processed').glob('*/metadata.json')))}
def ingest_log(out,file):
 count=0
 for id,item in read(file).items():
  source=Path(re.search(r' as (.+?\.png) by default',item['hint']).group(1))
  current=Path(out)/'current'/f'{id}.json'
  if current.exists() and read(current)['sourceHash']==digest(source):continue
  ingest(out,id,source,prompt=item.get('editPrompt'));count+=1
 return {'registered':count}
def ingest_results(out,directory):
 latest={}
 for file in sorted(Path(directory).glob('*.json')):
  for id,item in read(file).items():
   if id not in latest or item.get('attempt',1)>latest[id].get('attempt',1):latest[id]=item
 p=Path(out)/'native-results-latest.json';write(p,latest)
 return ingest_log(out,p)
def review_sheets(out,kind='species',region=None):
 out=Path(out);jobs=[j for j in jobdata(out)['jobs'] if j['kind']==kind and (not region or j['region']==region)]
 jobs=[j for j in jobs if (out/'processed'/j['id']/'metadata.json').exists()]
 try:font=ImageFont.truetype('DejaVuSans.ttf',14)
 except OSError:font=ImageFont.load_default()
 for offset in range(0,len(jobs),12):
  page=jobs[offset:offset+12];canvas=Image.new('RGB',(1080,880),'#eeeae1');d=ImageDraw.Draw(canvas)
  for n,j in enumerate(page):
   x=n%3*360;y=n//3*220
   p=out/'processed'/j['id']/(next(iter(j['variants']))+'.png');im=Image.open(p).convert('RGBA');im.thumbnail((166,166),Image.Resampling.LANCZOS)
   canvas.paste(im,(x+(170-im.width)//2,y+18+(170-im.height)//2),im)
   for s,yy in [(48,y+28),(120,y+88)]:
    t=Image.open(p).convert('RGBA');t.thumbnail((s,s),Image.Resampling.LANCZOS);canvas.paste(t,(x+195,yy),t)
   d.text((x+9,y+197),j['id'],font=font,fill='#30251e')
  path=out/'review-sheets'/f'{kind}-{region or "all"}-{offset//12+1}.png';path.parent.mkdir(parents=True,exist_ok=True);canvas.save(path)
 return {'internalSheets':(len(jobs)+11)//12}
def finalize(destination,report):
 destination=Path(destination);p=destination/'manifest.json';m=read(p);r=read(report)
 if r.get('navigationOnly') or not r.get('passed') or r.get('manifestHash')!=digest(p):raise ValueError('STALE_OR_FAILED_RUNTIME_QA')
 if set(r.get('assetIds',[]))!=set(m['assets']):raise ValueError('INCOMPLETE_RUNTIME_QA')
 if not all(r.get('scenes',{}).get(k) for k in ['catalog','kitchen','farm','business','exploration','collection','regulars']):raise ValueError('INCOMPLETE_SCENE_QA')
 for a in m['assets'].values():
  for v in a['variants'].values():
   if digest(ROOT/v['path'].lstrip('/'))!=v['sha256']:raise ValueError('RUNTIME_ASSET_HASH_MISMATCH')
  a['productionStatus']='FINAL';a['runtimeQA']='passed'
 m['runtimeReportHash']=digest(report);write(p,m)
 (destination/'manifest.js').write_text('// Generated by tools/art_pipeline.py. No content descriptions.\nexport const PRODUCTION_ART = '+json.dumps(m['assets'],ensure_ascii=False,indent=2)+';\n',encoding='utf-8')
 return {'final':len(m['assets'])}
def main():
 p=argparse.ArgumentParser(description=__doc__);p.add_argument('--out',required=True)
 subs=p.add_subparsers(dest='command',required=True)
 q=subs.add_parser('prepare');q.add_argument('--content',required=True);q.add_argument('--briefs');q.add_argument('--concepts')
 q=subs.add_parser('ingest');q.add_argument('--id',required=True);q.add_argument('--source',required=True);q.add_argument('--prompt-file')
 q=subs.add_parser('ingest-log');q.add_argument('--file',required=True)
 q=subs.add_parser('ingest-results');q.add_argument('--directory',required=True)
 q=subs.add_parser('select');q.add_argument('--id',required=True);q.add_argument('--attempt',type=int,required=True)
 q=subs.add_parser('process');q.add_argument('--id')
 q=subs.add_parser('review');q.add_argument('--id',required=True);q.add_argument('--record',required=True)
 q=subs.add_parser('similarity');q.add_argument('--legacy');q.add_argument('--legacy-index')
 q=subs.add_parser('publish');q.add_argument('--destination',required=True)
 q=subs.add_parser('review-sheets');q.add_argument('--kind',default='species');q.add_argument('--region')
 q=subs.add_parser('finalize');q.add_argument('--destination',required=True);q.add_argument('--report',required=True)
 subs.add_parser('status')
 a=p.parse_args()
 if a.command=='prepare':result=prepare(a.content,a.briefs,a.concepts,a.out)
 elif a.command=='ingest':result=ingest(a.out,a.id,a.source,prompt=Path(a.prompt_file).read_text() if a.prompt_file else None)
 elif a.command=='process':result=process(a.out,a.id)
 elif a.command=='ingest-log':result=ingest_log(a.out,a.file)
 elif a.command=='ingest-results':result=ingest_results(a.out,a.directory)
 elif a.command=='select':result=select_candidate(a.out,a.id,a.attempt)
 elif a.command=='review-sheets':result=review_sheets(a.out,a.kind,a.region)
 elif a.command=='finalize':result=finalize(a.destination,a.report)
 elif a.command=='review':result=approve(a.out,a.id,a.record)
 elif a.command=='similarity':result=similarity(a.out,a.legacy,a.legacy_index)
 elif a.command=='publish':result=publish(a.out,a.destination)
 else:result=status(a.out)
 print(json.dumps(result))
if __name__=='__main__':
 try:main()
 except (ValueError,KeyError,StopIteration,FileNotFoundError) as e:print(json.dumps({'error':str(e)[:160]}));sys.exit(1)
