"""Mechanical cuts of user-approved references; no generated/reinterpreted art.

All coordinates below are measured in the 390x844 reference frame. Hidden
interiors are reconstructed ONLY with matching straw/fabric samples. Runtime
gets empty components, never a page, egg array, numeric HUD or state label.
"""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFilter, ImageOps
import numpy as np
from scipy import ndimage
import json, hashlib

ROOT=Path(__file__).resolve().parents[1]
OUT=ROOT/'web/art/golden-kitchen'
QA=ROOT/'artifacts/kitchen-golden-runtime'
OUT.mkdir(parents=True,exist_ok=True); QA.mkdir(parents=True,exist_ok=True)
FILES=['e3f95393-29a3-4149-a3b7-35048cbea3cc.png','adeda638-1b40-4555-8cc4-9129c9eeff3d.png','73ca53da-de65-4525-8d76-5a24a7d41076.png','2649036c-79ba-4d26-b459-826f46a39386.png']
S=3
def box(b):return tuple(round(n*S) for n in b)
def cut(im,b):return im.crop(box(b))
entries=[]
def save(im,name,source,coords,method='measured crop'):
    p=OUT/(name+'.png');im.save(p)
    entries.append(dict(id=name,path='/web/art/golden-kitchen/'+p.name,source=source,referenceBounds=coords,size=im.size,method=method,sha256=hashlib.sha256(p.read_bytes()).hexdigest()))
def fill(im,b,color):ImageDraw.Draw(im).rectangle(box(b),fill=color)
def clone(im,b,sample):
    im.paste(cut(im,sample).resize((round((b[2]-b[0])*S),round((b[3]-b[1])*S))),box(b)[:2])
def object_cut(im,b):
    p=cut(im,b).convert('RGBA');a=np.array(p)
    # Closed ink contours protect white highlights and object interiors.
    ink=(a[:,:,0]<157)&(a[:,:,1]<138)&(a[:,:,2]<116)
    lab,n=ndimage.label(ndimage.binary_dilation(ink,iterations=1))
    counts=np.bincount(lab.ravel());counts[0]=0
    mask=ndimage.binary_fill_holes(lab==counts.argmax())
    mask=ndimage.binary_dilation(mask,iterations=1)
    a[:,:,3]=np.where(mask,255,0)
    return Image.fromarray(a)

def straw_surface(sample,size,seed):
    """Feathered source-patch quilting, deterministic; no synthesized drawing.

    Thin exposed straw strips cannot simply be stretched across a whole bed:
    that turns individual strands into repeated horizontal rails.
    """
    src=np.array(sample).astype(float);ph=min(src.shape[0],12*S);pw=min(src.shape[1],14*S)
    rng=np.random.default_rng(seed);w,h=size
    rgb=np.zeros((h+ph,w+pw,3));weight=np.zeros((h+ph,w+pw))
    wy=np.minimum(1,np.minimum(np.arange(ph)+1,ph-np.arange(ph))/(3*S))
    wx=np.minimum(1,np.minimum(np.arange(pw)+1,pw-np.arange(pw))/(3*S))
    mask=wy[:,None]*wx[None,:]
    for y in range(0,h,max(1,ph-5*S)):
        for x in range(0,w,max(1,pw-5*S)):
            sx=int(rng.integers(0,src.shape[1]-pw+1));sy=int(rng.integers(0,src.shape[0]-ph+1))
            patch=src[sy:sy+ph,sx:sx+pw]
            rgb[y:y+ph,x:x+pw]+=patch*mask[:,:,None];weight[y:y+ph,x:x+pw]+=mask
    return Image.fromarray(np.uint8(np.clip(rgb[:h,:w]/np.maximum(weight[:h,:w,None],.001),0,255)))

# The inner floor stops at the original front lip; no egg pixels survive here.
INTERIORS=[
 [(102,294),(280,294),(296,378),(92,379)],
 [(92,275),(294,275),(314,369),(73,369)],
 [(94,285),(300,285),(320,373),(70,373)],
 [(100,299),(297,299),(319,381),(76,381)],
]
SAMPLES=[(107,382,278,396),(102,268,285,275),(316,350,322,365),(133,382,267,389)]
FRONT=[380,368,373,381]
for i,file in enumerate(FILES):
    source='reference/kitchen/'+file
    im=Image.open(ROOT/source).convert('RGB').resize((390*S,844*S),Image.Resampling.LANCZOS)
    im.resize((390,844),Image.Resampling.LANCZOS).save(QA/f'lv{i+1}-reference.png')
    # Static HUD chrome. Remove all CP, numeric level and title wording.
    hud=im.copy()
    if i==0:
        fill(hud,(136,12,261,43),'#fcf8e8');fill(hud,(54,41,91,52),'#67421f')
        clone(hud,(126,65,263,85),(127,60,263,64));end=96
    else:
        fill(hud,(119,17,269,44),'#fcf8e8');fill(hud,(18,44,63,56),'#67421f')
        clone(hud,(124,70,267,90),(124,65,267,69));end=104
    save(cut(hud,(0,0,390,end)),f'lv{i+1}-header',source,[0,0,390,end],'reference chrome; dynamic text removed')
    start=148 if i==0 else 157
    surround=im.copy()
    ImageDraw.Draw(surround).rounded_rectangle(box((42,97,348,148) if i==0 else (10,104,380,157)),radius=10*S,fill='#fff9e8')
    save(cut(surround,(0,end,390,start)),f'lv{i+1}-status-surround',source,[0,end,390,start],'architecture around empty status frame')
    save(cut(im,(0,start,390,244)),f'lv{i+1}-wall',source,[0,start,390,244])
    # Build a deterministic liner from the reference's own exposed material.
    scene=im.copy();poly=INTERIORS[i]
    sample=cut(im,SAMPLES[i]);floor=Image.new('RGB',scene.size)
    if i==3:
        # Existing checker cloth, widened into the flat floor (no new art).
        sample=sample.resize((sample.width,16*S))
    elif i==2: sample=sample.rotate(90,expand=True)
    for row,y in enumerate(range(244*S,432*S,sample.height)):
        tile=sample if row%2==0 else ImageOps.flip(sample)
        for col,x in enumerate(range(0,390*S,sample.width)):
            floor.paste(tile if col%2==0 else ImageOps.mirror(tile),(x,y))
    if i in [0,2]:floor.paste(straw_surface(sample,(390*S,188*S),914+i),(0,244*S))
    mask=Image.new('L',scene.size);ImageDraw.Draw(mask).polygon([(round(x*S),round(y*S)) for x,y in poly],fill=255)
    scene.paste(floor,(0,0),mask)
    # Back/floor and front are separate layers; eggs draw between these files.
    save(cut(scene,(0,244,390,FRONT[i])),f'lv{i+1}-support',source,[0,244,390,FRONT[i]],'measured support and local decoration; hidden liner reconstructed from exposed material')
    save(cut(im,(0,FRONT[i],390,431)),f'lv{i+1}-front',source,[0,FRONT[i],390,431],'foreground lip and base; original source pixels')
    # Timer-zone background contains no timer text or clock.
    base=cut(im,(0,429,390,462));d=ImageDraw.Draw(base)
    # It is covered by the live timer but still remove baked text from the file.
    d.rectangle(box((103,8,304,25) if i==0 else (90,7,328,25)),fill='#fff9e8')
    save(base,f'lv{i+1}-counter',source,[0,429,390,462],'dynamic timer erased')

ref=Image.open(ROOT/'reference/kitchen'/FILES[2]).convert('RGB').resize((390*S,844*S),Image.Resampling.LANCZOS)
# Shared frame: remove all content; keep only outer paper and timber structure.
paper=cut(ref,(0,508,390,757));p=paper.copy()
ImageDraw.Draw(p).rounded_rectangle(box((16,36,374,230)),radius=8*S,fill='#fbf8e9')
# Heading will be its own blank wood plaque.
ImageDraw.Draw(p).rectangle(box((137,8,253,27)),fill='#edba7c')
save(p,'preparation-frame','reference/kitchen/'+FILES[2],[0,508,390,757],'empty shared panel; all dynamic contents removed')
plaque=cut(ref,(111,511,279,541));clone(plaque,(25,6,142,25),(25,3,142,6))
save(plaque,'plaque','reference/kitchen/'+FILES[2],[111,511,279,541],'blank reference wood nameplate')
save(cut(ref,(0,460,390,467)),'timber','reference/kitchen/'+FILES[2],[0,460,390,467])
for name,b in {'tool-0':(43,619,84,670),'tool-1':(123,619,181,666),'tool-2':(207,621,271,669),'tool-3':(303,618,356,670),'lemon':(57,550,104,586),'clock':(55,431,83,459)}.items():
    save(object_cut(ref,b),name,'reference/kitchen/'+FILES[2],list(b),'closed ink contour cutout')
# A complete front-row egg is traced at its actual contour, without its neighbors.
egg=cut(ref,(74,337,116,374)).convert('RGBA')
a=np.array(egg);ink=(a[:,:,0]<170)&(a[:,:,1]<140)&(a[:,:,2]<105)
lab,count=ndimage.label(~ink);target=lab[20*S,20*S]
mask=ndimage.binary_dilation(lab==target,iterations=5)
a[:,:,3]=np.where(mask,255,0);egg=Image.fromarray(a)
save(egg,'egg','reference/kitchen/'+FILES[2],[74,337,116,374],'interior flood fill bounded by original ink; dilated to retain contour')
manifest={'version':1,'goldenReferences':['reference/kitchen/'+f for f in FILES],'coordinateSystem':[390,844],'rasterScale':S,'extracted':entries,'reused':['web/art/icons-v4-alpha.png','web/art/cookware-a-v15.png','web/art/cookware-b-v15.png','web/catalog.js character and ingredient resolution','web/fonts/chick-ui.woff2'],'new':['empty liner restoration using reference texture','kitchen-golden renderer and per-level hit mapping'],'dynamic':['CP','level','current cookware','ingredients','cleanliness','countdown','24 eggs/characters','selection','locked state','pagination','navigation','egg species switch','harvest allocation'],'generation':'none'}
(OUT/'manifest.json').write_text(json.dumps(manifest,ensure_ascii=False,indent=2)+'\n',encoding='utf8')
print(f'Extracted {len(entries)} empty/static components; 4 references normalized; no page asset shipped.')
