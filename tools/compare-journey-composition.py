"""User requested A/B overlays. Only reference framing and screenshot annotation;
runtime screenshots themselves are untouched. No artwork is manufactured here.
"""
from pathlib import Path
from PIL import Image,ImageDraw,ImageFont
import json
import sys
ROOT=Path(__file__).resolve().parents[1]
OUT=ROOT/(sys.argv[1] if len(sys.argv)>1 else 'artifacts/journey-convergence')
before_phase=sys.argv[2] if len(sys.argv)>2 else 'before'
font=ImageFont.truetype('C:/Windows/Fonts/msyh.ttc',15)
small=ImageFont.truetype('C:/Windows/Fonts/msyh.ttc',11)
ref=Image.open(ROOT/'docs/journey-visual-20260925/mockup-main.png').convert('RGB')
colors={'header':'#a84e6b','primaryArt':'#16846d','secondaryAction':'#b17905','dynamicCharacter':'#6745b5','CTA':'#cf5636','bottomNav':'#307fa8','target':'#825b37','ribbon':'#478334'}
boxes={
 'map':{'header':(16,16,485,92),'primaryArt':(18,86,483,925),'secondaryAction':(28,725,472,906),'dynamicCharacter':(42,510,221,664),'CTA':(103,810,397,887),'bottomNav':(18,925,483,1044)},
 'region':{'header':(504,16,974,90),'primaryArt':(520,155,958,358),'secondaryAction':(528,384,955,611),'dynamicCharacter':(518,682,958,835),'CTA':(765,846,958,913),'bottomNav':(506,925,972,1044),'target':(526,622,955,683)},
 'return':{'header':(992,16,1460,90),'primaryArt':(1030,362,1435,578),'secondaryAction':(1124,594,1331,677),'dynamicCharacter':(1040,112,1403,252),'CTA':(1080,773,1375,834),'bottomNav':(994,925,1458,1044),'ribbon':(1085,265,1368,338)}
}
def rgb(path):return Image.open(path).convert('RGB')
def board(items,path):
 w=sum(im.width for _,im in items)+16*(len(items)+1);h=max(im.height for _,im in items)+56
 result=Image.new('RGB',(w,h),'#eee8d8');draw=ImageDraw.Draw(result);x=16
 for label,im in items:draw.text((x,10),label,font=font,fill='#61472e');result.paste(im,(x,38));x+=im.width+16
 result.save(path)
def annotate(im,bounds,dashed=False):
 im=im.copy();d=ImageDraw.Draw(im)
 for key,b in bounds.items():
  if not b:continue
  x,y,w,h=(b[k] for k in ['x','y','width','height']);color=colors[key]
  if dashed:
   for yy in [y,y+h]:
    for xx in range(round(x),round(x+w),10):d.line((xx,yy,min(xx+5,x+w),yy),fill=color,width=2)
  else:d.rectangle((x,y,x+w,y+h),outline=color,width=2)
  d.text((x+3,y+3),key,font=small,fill=color,stroke_width=1,stroke_fill='#fff9e4')
 return im
layouts={phase:json.loads((OUT/phase/'layout.json').read_text(encoding='utf8')) for phase in [before_phase,'after']}
report={}
for i,name in enumerate(['map','region','return']):
 cropbox=[(16,16,485,1046),(504,16,974,1046),(992,16,1460,1046)][i]
 crop=ref.crop(cropbox);scale=min(390/crop.width,844/crop.height)
 resized=crop.resize((round(crop.width*scale),round(crop.height*scale)),Image.Resampling.LANCZOS)
 reference=Image.new('RGB',(390,844),'#fff9e4');dx=(390-resized.width)//2;dy=(844-resized.height)//2;reference.paste(resized,(dx,dy));reference.save(OUT/f'{name}-reference.png')
 refbounds={k:dict(zip(['x','y','width','height'],[(x-cropbox[0])*scale+dx,(y-cropbox[1])*scale+dy,(xx-x)*scale,(yy-y)*scale])) for k,(x,y,xx,yy) in boxes[name].items()}
 before=rgb(OUT/before_phase/f'{name}-390.png');after=rgb(OUT/'after'/f'{name}-390.png')
 board([('确认 Mockup',reference),('Runtime · 修改后',after)],OUT/f'{name}-ab.png')
 board([('Mockup',reference),('Runtime · 修改前',before),('Runtime · 修改后',after)],OUT/f'{name}-before-after.png')
 entries={p:next(x for x in layouts[p]['shots'] if x['name']==f'{name}-390') for p in layouts}
 overlays=[]
 for phase,im in [(before_phase,before),('after',after)]:
  blend=Image.blend(reference,im,.5);blend=annotate(blend,refbounds,True);blend=annotate(blend,entries[phase]['bounds'])
  blend.save(OUT/f'{name}-overlay-{phase}.png');overlays.append((phase+' · 50% overlay',blend))
 board(overlays,OUT/f'{name}-overlays.png')
 board([('Mockup · 区域',annotate(reference,refbounds)),('Runtime · 区域',annotate(after,entries['after']['bounds']))],OUT/f'{name}-regions.png')
 report[name]={'reference':refbounds,**{p:entries[p] for p in entries}}
 report[name]['primaryHeightPercent']={p:round(b['primaryArt']['height']/844*100,1) for p,b in [('reference',refbounds)]+[(p,entries[p]['bounds']) for p in entries]}
 report[name]['secondaryHeightPercent']={p:round(b['secondaryAction']['height']/844*100,1) for p,b in [('reference',refbounds)]+[(p,entries[p]['bounds']) for p in entries]}
(OUT/'composition-metrics.json').write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf8')
print(json.dumps({k:{t:v[t] for t in ['primaryHeightPercent','secondaryHeightPercent']} for k,v in report.items()},ensure_ascii=False))
