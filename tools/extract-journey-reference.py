"""Reference-derived component sheet. No labels, quantities or UI screenshots.

Explicit semantic polygons separate each illustration from surrounding text;
paper colour matting removes the original page while preserving painted edges.
The generated empty UI assets remain separate from dynamic typography.
"""
from pathlib import Path
from PIL import Image,ImageDraw
import numpy as np,json
from scipy import ndimage
ROOT=Path(__file__).resolve().parents[1]
src=Image.open(ROOT/'docs/journey-visual-20260925/mockup-main.png').convert('RGB')
# Polygon coordinates are native reference pixels, not runtime positions.
parts={
 'node-V':[(60,108),(155,108),(161,150),(174,162),(183,204),(180,216),(64,216),(58,194)],
 'node-T':[(272,120),(431,119),(464,151),(469,242),(445,255),(270,254),(267,224),(285,199)],
 'node-R':[(135,331),(194,328),(231,330),(284,327),(349,359),(349,420),(215,423),(141,413),(114,411),(114,385)],
 'node-B':[(330,513),(404,512),(453,536),(439,565),(453,583),(448,605),(389,626),(328,618),(254,592),(251,569),(306,548)],
 'place-V-0':[(529,251),(569,229),(590,205),(624,195),(655,211),(667,237),(671,228),(696,237),(721,238),(732,279),(718,308),(672,320),(560,307),(527,294)],
 'place-V-1':[(783,209),(820,170),(889,185),(935,222),(933,287),(949,311),(885,326),(778,317),(765,288),(789,274)],
 'tree-round':[(24,455),(55,431),(86,435),(108,458),(125,489),(122,521),(85,549),(58,549),(35,530),(23,507)],
 'tree-tall':[(425,309),(437,290),(452,286),(465,302),(475,337),(465,363),(431,370),(420,344)],
 'grass-rock':[(209,588),(239,590),(253,600),(277,588),(299,610),(317,623),(315,656),(279,670),(222,659),(210,636)],
 'botanical-left':[(1027,372),(1060,361),(1084,379),(1102,398),(1113,442),(1117,484),(1118,568),(1065,569),(1026,542),(1027,477)],
 'botanical-right':[(1358,404),(1372,366),(1390,365),(1405,398),(1421,399),(1422,437),(1399,463),(1394,484),(1420,478),(1427,510),(1411,536),(1426,558),(1382,575),(1335,568),(1336,508),(1343,468)],
 'hill':[(20,146),(43,109),(80,96),(111,106),(134,123),(196,169),(270,140),(298,117),(326,117),(350,96),(380,83),(416,89),(435,102),(462,86),(484,91),(484,149),(472,153),(447,116),(410,113),(359,128),(351,163),(326,169),(306,166),(274,180),(249,173),(239,177),(209,173),(195,185),(161,163),(139,147),(79,166),(20,204)],
 'celebrate-left':[(1033,142),(1088,125),(1115,154),(1097,183),(1040,185)],
 'celebrate-right':[(1357,123),(1412,117),(1416,166),(1380,184),(1357,163)],
 'rock':[(422,498),(433,489),(450,492),(460,504),(456,517),(421,518)],
 'tree-left':[(20,181),(26,160),(44,155),(54,165),(58,188),(63,211),(54,233),(24,235),(19,219)],
 'character-0-0':[(1058,158),(1161,158),(1161,244),(1058,244)],
 'character-0-3':[(1160,121),(1300,121),(1300,243),(1160,243)],
 'character-1-0':[(1285,148),(1398,148),(1398,246),(1285,246)],
 'sparkle':[(1035,314),(1071,314),(1071,365),(1035,365)],
 'hero-ground':[(1018,530),(1109,530),(1112,560),(1337,558),(1338,536),(1432,535),(1437,568),(1393,583),(1122,581),(1030,564)],
 'mat':[(520,742),(565,742),(565,769),(611,769),(611,742),(646,742),(657,780),(650,785),(522,785)],
}
for omitted in ['hill','hero-ground','mat']:parts.pop(omitted)
parts.update({
 'record-note':[(1125,716),(1165,716),(1165,756),(1125,756)],
 'action-note':[(840,452),(929,452),(929,539),(840,539)],
 'action-magnifier':[(680,451),(787,451),(787,537),(680,537)],
 'action-pouch':[(545,443),(640,443),(640,539),(545,539)],
 'map-icon':[(182,34),(221,34),(221,80),(182,80)],
})
# Keep lower labels outside the extraction polygons.
parts['place-V-0']=[(x,min(y,311)) for x,y in parts['place-V-0']]
parts['place-V-1']=[(x,min(y,309)) for x,y in parts['place-V-1']]
parts['node-V']=[(x,min(y,213)) for x,y in parts['node-V']]
parts['node-T']=[(x,min(y,243)) for x,y in parts['node-T']]
parts['celebrate-left']=[(1033,142),(1088,125),(1085,151),(1060,178),(1039,169)]
sheet=Image.new('RGBA',(2000,((len(parts)+4)//5)*400))
info=[]
for i,(name,poly) in enumerate(parts.items()):
 mask=Image.new('L',src.size);ImageDraw.Draw(mask).polygon(poly,fill=255)
 box=mask.getbbox();rgb=np.array(src.crop(box)).astype(float);m=np.array(mask.crop(box))/255
 # Reference paper is pale cream; retain warm contours and muted green paint.
 delta=np.max(np.abs(rgb-np.array([255,250,230])),axis=2)
 alpha=np.clip((delta-3)/10,0,1)*m
 if name.startswith(('node','place','tree','grass')):
  # Remove pale hill/ground behind the independently positioned component.
  pale=(rgb[:,:,0]>178)&(rgb[:,:,1]>186)&(rgb[:,:,2]>140)&(rgb[:,:,1]>rgb[:,:,0]+1)&(rgb[:,:,1]>rgb[:,:,2]+20)
  alpha[pale]=0
 if name=='node-R':
  water=(rgb[:,:,2]>rgb[:,:,0]+22)&(rgb[:,:,1]>rgb[:,:,0]+15)
  water[:,max(0,333-box[0]):]=False
  alpha[water]=0
 if name.startswith('character'):
  warm=(rgb[:,:,0]>rgb[:,:,1]+2)|(rgb[:,:,0]<135)
  alpha*=warm
 if name.startswith('botanical'):
  green=np.clip((rgb[:,:,1]-rgb[:,:,0]-3)/10,0,1)
  alpha*=green
 rgba=np.dstack([rgb,alpha*255]).astype('uint8');im=Image.fromarray(rgba)
 b=im.getchannel('A').getbbox();im=im.crop(b);im.thumbnail((360,340),Image.Resampling.LANCZOS)
 x=(i%5)*400+(400-im.width)//2;y=(i//5)*400+(400-im.height)//2
 sheet.alpha_composite(im,(x,y))
 info.append({'id':name,'referencePolygon':poly,'referenceBounds':box,'referenceVisualBounds':[box[0]+b[0],box[1]+b[1],b[2]-b[0],b[3]-b[1]],'sheetCell':[i%5,i//5],'sheetBounds':[x,y,im.width,im.height]})
folder=ROOT/'docs/journey-visual-20260925/asset-sheets'
sheet.save(folder/'J09-reference-components.png')
(folder/'J09-reference-components.json').write_text(json.dumps({'source':'../mockup-main.png','method':'semantic polygon segmentation; pale paper matte; no text components','components':info},ensure_ascii=False,indent=2),encoding='utf-8')
print('J09 reference sheet:',len(info),'independent components; all UI text excluded')

# Environment-only colour layers. No black/brown lettering, route, character,
# button or landmark silhouette is included. Semantic sheet cells remain separate.
regions=[('map-hills',(18,84,483,290),'green'),('map-ground',(18,327,483,725),'green'),
 ('map-river',(18,285,483,561),'blue'),('place-environment',(515,157,965,361),'green'),
 ('hero-ground',(1028,472,1428,581),'hero-green')]
env=Image.new('RGBA',(2000,800));records=[]
for i,(name,box,kind) in enumerate(regions):
 rgb=np.array(src.crop(box)).astype(float);r,g,b=rgb[:,:,0],rgb[:,:,1],rgb[:,:,2]
 if kind=='blue':
  core=(g-r>17)&(b-r>28)&(b-g>4)
  # Retain the pale water brush strokes immediately surrounded by water.
  near=ndimage.distance_transform_edt(~core)<4
  core|=near&(r>220)&(g>235)&(b>235)
  a=core.astype(float)*255
 else:
  a=((g-r>-8)&(g-b>30)&(r<247)&(r>145)&(g>165)).astype(float)*255
  if kind=='hero-green':a[:,1114-box[0]:1342-box[0]]=0
 im=Image.fromarray(np.dstack([rgb,a]).astype('uint8'))
 bb=im.getchannel('A').getbbox();im=im.crop(bb);im.thumbnail((370,370),Image.Resampling.LANCZOS)
 x=i%5*400+(400-im.width)//2;y=(400-im.height)//2
 env.alpha_composite(im,(x,y));records.append({'id':name,'referenceVisualBounds':[box[0]+bb[0],box[1]+bb[1],bb[2]-bb[0],bb[3]-bb[1]],'mask':kind})
env.crop((0,0,2000,400)).save(folder/'J11-reference-environment.png')
(folder/'J11-reference-environment.json').write_text(json.dumps({'source':'../mockup-main.png','components':records,'contains':'only static pale green environment and blue water; no text, controls, routes, characters'},indent=2),encoding='utf-8')
