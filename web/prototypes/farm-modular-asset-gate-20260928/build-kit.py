"""Author independent transparent SVG modules and inspect PNG alpha, without editing PNG pixels."""
from pathlib import Path
from PIL import Image
import json
ROOT=Path(__file__).resolve().parent
INK='#553c2b'
def svg(name,body,w=120,h=120):
    source=f'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 {w} {h}" fill="none" stroke="{INK}" stroke-width="3.5" stroke-linejoin="round" stroke-linecap="round">{body}</svg>'
    (ROOT/'assets'/f'{name}.svg').write_text(source,encoding='utf-8')
    return {'file':f'assets/{name}.svg','width':w,'height':h,'frame':[0,0,w,h],'kind':'environment','source':'code-native SVG; transparent outside shapes'}
assets={}
# All fills are flat. Top/front/side planes share the same light direction.
assets['tree-pine']=svg('tree-pine','<path d="M54 80h16v31l-7 5-9-5z" fill="#b98347"/><path d="M64 82h6v29l-7 5z" fill="#885d36" stroke="none"/><path d="M60 5 42 33 46 33 27 56 34 56 14 86 28 91 24 96 47 98 56 94 69 101 83 96 97 96 91 89 107 85 87 57 92 57 74 34 79 34z" fill="#448659"/><path d="m60 5 5 32-12 10 11 5-8 25 18 11 21-2-8-29 5 0-18-23 5 0z" fill="#31684a" stroke="none"/><path d="M60 9 42 33 47 33 28 54 42 51 60 30z" fill="#68a765" stroke="none"/><path d="m42 70 9 6m21-22 8 7m-33 35 9-4" stroke="#2e6948" stroke-width="2.5"/>')
assets['tree-round']=svg('tree-round','<path d="m49 71 26 2-5 37-11 7-13-8z" fill="#b98347"/><path d="m63 76 12-3-5 37-11 7z" fill="#885d36" stroke="none"/><path d="M17 62C2 48 12 29 28 29 24 10 45 2 58 15 75 0 97 14 94 31 115 33 119 60 100 68 103 86 80 98 64 86 46 103 21 86 23 74 11 77 8 66 17 62Z" fill="#58975c"/><path d="M94 31C87 50 94 66 77 70 61 73 56 66 49 76 35 81 29 70 17 62L23 74C21 86 46 103 64 86 80 98 103 86 100 68 119 60 115 33 94 31Z" fill="#3d774e" stroke="none"/><path d="M28 29C24 10 45 2 58 15 51 23 36 27 36 39 26 34 21 44 17 44 11 33 21 29 28 29Z" fill="#85b56a" stroke="none"/><path d="m49 43 7-5m20 14 7 4" stroke="#3d774e" stroke-width="3"/>')
assets['bush-low']=svg('bush-low','<path d="M9 50C-2 30 15 20 29 27 28 7 50 2 61 18 77 6 95 17 91 30 114 24 125 50 108 61 88 68 68 61 55 67 37 62 18 68 9 50Z" fill="#73ad52"/><path d="M9 50C32 53 43 43 55 51 72 49 89 54 111 44L108 61C88 68 68 61 55 67 37 62 18 68 9 50Z" fill="#548d43" stroke="none"/><path d="M30 27C35 14 47 13 54 23" stroke="#9bcc67" stroke-width="8"/>',120,74)
assets['bush-tall']=svg('bush-tall','<path d="M18 90C1 76 6 56 22 53 8 35 23 18 41 23 50 0 76 8 77 26 102 22 111 45 96 60 120 82 95 105 76 99 60 115 35 102 33 93Z" fill="#559752"/><path d="M97 60C82 72 66 64 55 84 37 94 29 77 18 90L33 93C35 102 60 115 76 99 95 105 120 82 97 60Z" fill="#397a48" stroke="none"/><path d="M26 40C23 28 35 27 42 32M49 25C54 12 63 16 65 23" stroke="#87ba60" stroke-width="8"/>',120,120)
assets['rock-wide']=svg('rock-wide','<path d="m7 62 17-32 40-17 38 20 11 38-23 17-57-2z" fill="#8f9e83"/><path d="m24 30 40-17 16 32-37 9z" fill="#bcc6a2" stroke="none"/><path d="m80 45 22-12 11 38-23 17-47-2z" fill="#728470" stroke="none"/><path d="m24 30 19 24 37-9" stroke="#778772" stroke-width="2"/>',120,96)
assets['rock-tall']=svg('rock-tall','<path d="m22 91-4-39 24-37 37-8 24 32-1 55-36 19z" fill="#94a28a"/><path d="m18 52 24-37 37-8-17 40z" fill="#c5cba9" stroke="none"/><path d="m62 47 41-8-1 55-36 19z" fill="#738773" stroke="none"/><path d="m62 47 4 48" stroke="#84947d" stroke-width="2"/>')
assets['fence-front']=svg('fence-front','<path d="m9 30 97 13v14L9 44Zm0 29 97 13v14L9 73Z" fill="#d8ab60"/><path d="m7 13 9-5 9 8v81l-9 6-9-9zM95 26l9-5 9 8v81l-9 5-9-9z" fill="#b68143"/><path d="m16 8 9 8v81l-9 6zM104 21l9 8v81l-9 5z" fill="#956735" stroke="none"/><path d="m12 35 8 1m82 43 6 1" stroke="#80613a" stroke-width="2"/>',120,120)
assets['fence-return']=svg('fence-return','<path d="m15 57 69-37v14L15 73Zm0 28 69-37v14L15 99Z" fill="#c39651"/><path d="m9 44 8-5 9 6v65l-9 6-8-7zM78 7l8-5 9 7v65l-9 6-8-7z" fill="#b68143"/><path d="m17 39 9 6v65l-9 6zM86 2l9 7v65l-9 6z" fill="#906038" stroke="none"/>',104,120)
assets['bench']=svg('bench','<path d="m19 44 15 1v35l-12 5-3-7zm66 10 15 1v35l-12 5-3-7z" fill="#986539"/><path d="m8 34 31-16 80 18-23 19z" fill="#d7a258"/><path d="m8 34 88 21v15L8 48z" fill="#b78042"/><path d="m88 55 23-19v13L88 70z" fill="#8d5f36"/><path d="m34 30 23 5" stroke="#9c693d" stroke-width="2"/>',120,100)
assets['basin']=svg('basin','<path d="M12 44c0 37 91 43 98 2l-4 25c-17 26-72 25-90-1z" fill="#9ba792"/><ellipse cx="61" cy="44" rx="49" ry="24" fill="#d4d4b4"/><ellipse cx="61" cy="44" rx="37" ry="16" fill="#70b3b8"/><path d="M31 47c13 12 41 16 63-2" stroke="#a6d5cc" stroke-width="3"/>',120,100)
assets['campfire']=svg('campfire','<path d="m18 77 79 29 10-13-78-27z" fill="#9c673c"/><path d="m17 96 78-31 13 13-79 31z" fill="#bd8849"/><path d="M30 75c-7-17 8-27 9-40l13 10c16-20 16-25 15-36 28 23 9 36 24 43 23 39-4 47-28 43-19 4-39-4-33-20Z" fill="#ee9545"/><path d="M50 85c-8-14 7-20 12-35 3 19 17 16 19 32 0 12-25 19-31 3Z" fill="#ffe48a" stroke="none"/>')
assets['edge-path']=svg('edge-path','<path d="M2 17c24 7 41-4 61 2s33 6 55 1v16c-22 5-41 5-60 0S21 41 2 34Z" fill="#d8b677" stroke="none"/><path d="M2 34c20 7 36-3 56 2s36 5 60 0" stroke="#b59b59" stroke-width="3"/>',120,50)
assets['edge-grass']=svg('edge-grass','<path d="m2 25 13 4 3-13 6 13 21-2 4-12 6 12 22 2 5-16 7 15 29-5v23H2Z" fill="#92b454" stroke="none"/>',120,50)
assets['edge-stone']=svg('edge-stone','<path d="m3 25 8-12 25 4 2 18-28 3zm44-8 29-4 9 13-8 13-31-3zm44 4 19-6 8 12-3 14-23-2z" fill="#b0b698" stroke="#7f906a" stroke-width="2.5"/>',120,50)
# Metadata only: never change source PNG bytes. Cropping happens as a viewport at draw time.
for name in ['house','shop','shrine','display']:
    p=ROOT/'assets'/f'{name}.png';im=Image.open(p);assert im.mode=='RGBA',(name,im.mode)
    alpha=im.getchannel('A');hist=alpha.histogram();assert hist[0]>im.width*im.height*.08,(name,'no transparent margin')
    box=alpha.point(lambda a:255 if a>16 else 0).getbbox();x0,y0,x1,y1=box
    assets[name]={'file':f'assets/{name}.png','width':im.width,'height':im.height,'frame':[x0,y0,x1-x0,y1-y0],'kind':'building','source':'image_gen independent object','alphaZeroRatio':round(hist[0]/(im.width*im.height),4),'alphaExtrema':alpha.getextrema()}
(ROOT/'asset-manifest.json').write_text(json.dumps(assets,ensure_ascii=False,indent=2),encoding='utf-8')
print(f'PASS: {len(assets)} independent assets; four building PNGs have real alpha. No PNG pixels edited.')
