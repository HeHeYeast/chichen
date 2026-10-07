"""User-authorized sheet cuts; preserve source colors and alpha, never draw art."""
from pathlib import Path
from PIL import Image
import json,hashlib
ROOT=Path(__file__).resolve().parents[1]
DIR=ROOT/'docs/business-golden-20260924/revision-2/asset-sheets'
OUT=ROOT/'web/art/golden-business'
PARTS={'B04-frames.png':{
 'v2-nameplate':(18,224,503,414),'v2-summary':(540,250,1012,395),
 'v2-wallet':(50,681,481,880),'v2-help':(645,644,902,905),
 'v2-action':(18,1118,503,1305),'v2-counter':(527,1153,1010,1260),
},'B05-books.png':{
 'v2-regulars':(170,70,680,530),'v2-order-pad':(910,70,1300,590),
 'v2-guest-page':(235,565,615,950),'v2-divider':(760,758,1430,825),
}}
def main():
 manifest=json.loads((OUT/'manifest.json').read_text(encoding='utf8'))
 for sheet,parts in PARTS.items():
  if not (DIR/sheet).exists():continue
  im=Image.open(DIR/sheet).convert('RGBA')
  for name,crop in parts.items():
   cut=im.crop(crop);a=cut.getchannel('A').point(lambda p:0 if p<12 else p);cut.putalpha(a);bounds=a.getbbox();assert bounds,name
   cut=cut.crop(bounds);final=Image.new('RGBA',(cut.width+16,cut.height+16));final.paste(cut,(8,8));path=OUT/(name+'.png');final.save(path)
   entry={'id':'business:'+name,'path':'/web/art/golden-business/'+path.name,'sheet':str((DIR/sheet).relative_to(ROOT)).replace('\\','/'),'crop':list(crop),'alphaBoundsInCrop':list(bounds),'size':list(final.size),'safeInset':8,'anchor':[.5,.5],'transparent':True,'status':'golden-sample','sha256':hashlib.sha256(path.read_bytes()).hexdigest(),'source':'built-in-imagegen; alpha-preserving sheet cut; no UI text or character'}
   manifest['assets']=[a for a in manifest['assets'] if a['id']!=entry['id']]+[entry]
 (OUT/'manifest.json').write_text(json.dumps(manifest,ensure_ascii=False,indent=2)+'\n',encoding='utf8')
 print('cut',sum(len(parts) for sheet,parts in PARTS.items() if (DIR/sheet).exists()),'new independent assets')
if __name__=='__main__':main()
