"""Untouched browser screenshots beside approved reference and 50% overlay."""
from pathlib import Path
import json,sys
from PIL import Image,ImageDraw,ImageFont
ROOT=Path(__file__).resolve().parents[1]
OUT=ROOT/'artifacts/journey-precision'
phase=sys.argv[1]
f=ImageFont.truetype('C:/Windows/Fonts/msyh.ttc',15)
for name in ['map','region','return']:
 ref=Image.open(OUT/(name+'-reference.png')).convert('RGB')
 run=Image.open(OUT/phase/(name+'-390.png')).convert('RGB')
 assert ref.size==run.size==(390,844)
 overlay=Image.blend(ref,run,.5);overlay.save(OUT/phase/(name+'-overlay.png'))
 board=Image.new('RGB',(1234,896),'#eee8d8');d=ImageDraw.Draw(board)
 for i,(label,im) in enumerate([('确认 Mockup',ref),('Runtime · '+phase,run),('50% Overlay',overlay)]):
  x=16+i*406;d.text((x,10),label,font=f,fill='#61472e');board.paste(im,(x,38))
 board.save(OUT/phase/(name+'-comparison.png'))
print(phase+': 3 reference | runtime | unannotated 50% overlay boards saved')
