from pathlib import Path
import sys,json,re,requests,hashlib
from PIL import Image,ImageDraw,ImageFont
ROOT=Path(__file__).parent
def probe(url):
    r=requests.get(url,timeout=35); print(r.status_code,len(r.content)); print(r.text[:300])
    links=list(dict.fromkeys(re.findall(r'https[^\s<>"\x27\\]+',r.text)))
    print('\n'.join(links)[:12000])
def imgs(url):
    r=requests.get(url,timeout=35);r.encoding='utf-8'
    print('\n'.join(re.findall(r'<img[^>]+>',r.text))[:16000])
def download(url,name):
    p=ROOT/'evidence'/name;p.parent.mkdir(parents=True,exist_ok=True)
    headers={'User-Agent':'Mozilla/5.0'}
    if '3dmgame.com' in url:headers['Referer']='https://shouyou.3dmgame.com/android/560350.html'
    r=requests.get(url,timeout=40,headers=headers);r.raise_for_status();p.write_bytes(r.content)
    im=Image.open(p);print(name,im.size)
    log=ROOT/'downloads.jsonl'
    with log.open('a',encoding='utf-8') as f:f.write(json.dumps(dict(file=name,url=url,size=im.size,sha256=hashlib.sha256(r.content).hexdigest(),retrieved='2026-09-28'),ensure_ascii=False)+'\n')
def sheet(paths,out,cols=4):
    w,h=330,640
    canvas=Image.new('RGB',(w*cols,h*((len(paths)+cols-1)//cols)), '#e9e9e5');d=ImageDraw.Draw(canvas)
    font=ImageFont.truetype('C:/Windows/Fonts/msyh.ttc',16)
    for i,p in enumerate(paths):
        im=Image.open(p).convert('RGB');im.thumbnail((w-16,h-64))
        x=(i%cols)*w;y=(i//cols)*h
        canvas.paste(im,(x+(w-im.width)//2,y+42))
        d.text((x+8,y+5),Path(p).stem[:24],fill='#222222',font=font)
    (ROOT/'evidence').mkdir(exist_ok=True)
    canvas.save(ROOT/'evidence'/out)
if __name__=='__main__':
    if sys.argv[1]=='probe':probe(sys.argv[2])
    elif sys.argv[1]=='imgs':imgs(sys.argv[2])
    elif sys.argv[1]=='download':download(sys.argv[2],sys.argv[3])
    elif sys.argv[1]=='mq':sheet(sorted((ROOT.parent/'archive/mqxq-research/screenshots').glob('*.jpg')), 'mq-contact.jpg')
    elif sys.argv[1]=='mq-extra':
        from concurrent.futures import ThreadPoolExecutor
        def run(n):
            try:download(f'http://img1.gamersky.com/image2021/01/20210122_yw_427_32/image{n:03d}_S.jpg',f'M{17+(n-1)//2:02d}-guide.jpg')
            except Exception as e:print(n,type(e).__name__)
        with ThreadPoolExecutor(max_workers=4) as pool:list(pool.map(run,range(1,18,2)))
    elif sys.argv[1]=='sheets':
        sheet(sorted((ROOT/'evidence').glob('T*.jpg')),'tata-contact.jpg')
        sheet(sorted((ROOT/'evidence').glob('M*.jpg')),'mq-extra-contact.jpg')
    elif sys.argv[1]=='batch':
        from concurrent.futures import ThreadPoolExecutor
        data=json.loads((ROOT/sys.argv[2]).read_text(encoding='utf-8'))
        def run(item):
            try:download(item['url'],item['name'])
            except Exception as e:print(item['name'],type(e).__name__,str(e)[:160])
        with ThreadPoolExecutor(max_workers=5) as pool:list(pool.map(run,data))
