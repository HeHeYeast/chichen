"""User-authorized Asset Sheet cutting, preserving generated RGBA pixels."""
from pathlib import Path
from PIL import Image
import json,hashlib,shutil
ROOT=Path(__file__).resolve().parents[1]
GEN=Path('C:/Users/管啸野/.codex/generated_images/01a0d334-4010-7f53-8fc4-36daac48b533')
DOC=ROOT/'docs/business-family-20260925'; DOC.mkdir(exist_ok=True)
SHEETS=DOC/'asset-sheets'; SHEETS.mkdir(exist_ok=True)
sources={'F03-surfaces-separated.png':'exec-cecec476-4efb-4fc6-a3dd-68322761b16f.png','F02-states.png':'exec-a1e4faf7-b080-4535-9775-0b8b27011bdf.png'}
parts={'F03-surfaces-separated.png':{'family-project-board':(75,40,615,580),'family-receipt':(655,40,1170,585),'family-label':(75,690,640,915),'family-avatar-base':(700,580,1140,995),'family-pin':(265,995,445,1230),'family-clip':(765,995,1040,1230)},
       'F02-states.png':{'family-stamp-done':(10,125,505,425),'family-stamp-active':(535,125,1020,425),'family-stamp-idle':(1050,125,1535,425),'family-unknown':(45,505,475,940),'family-check':(600,555,930,900),'family-envelope':(1040,550,1510,910)}}
shutil.copy2(GEN/'exec-c12c0477-6112-4534-880d-efa996dd75c6.png',DOC/'projects-ledger-mockup.png')
manifest_path=ROOT/'web/art/golden-business/manifest.json';manifest=json.loads(manifest_path.read_text(encoding='utf8'))
for name,file in sources.items():
 shutil.copy2(GEN/file,SHEETS/name)
 source=Image.open(SHEETS/name).convert('RGBA')
 for key,crop in parts[name].items():
  piece=source.crop(crop);alpha=piece.getchannel('A').point(lambda x:0 if x<12 else x);piece.putalpha(alpha);bounds=alpha.getbbox();piece=piece.crop(bounds)
  final=Image.new('RGBA',(piece.width+12,piece.height+12));final.paste(piece,(6,6))
  target=ROOT/'web/art/golden-business'/f'{key}.png';final.save(target)
  asset={'id':'business:'+key,'path':'/web/art/golden-business/'+target.name,'sheet':str((SHEETS/name).relative_to(ROOT)).replace('\\','/'),'crop':list(crop),'alphaBoundsInCrop':list(bounds),'size':list(final.size),'safeInset':6,'transparent':True,'status':'golden-sample','sha256':hashlib.sha256(target.read_bytes()).hexdigest(),'source':'built-in imagegen, category sheet alpha-preserving cut; no characters or UI text baked in'}
  manifest['assets']=[a for a in manifest['assets'] if a['id']!=asset['id']]+[asset]
  print(key,final.size)
manifest_path.write_text(json.dumps(manifest,ensure_ascii=False,indent=2)+'\n',encoding='utf8')
