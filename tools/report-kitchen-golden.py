"""Same-size screenshot evidence; never used by the game renderer."""
from pathlib import Path
from PIL import Image,ImageDraw,ImageFont
import json,hashlib
ROOT=Path(__file__).resolve().parents[1];OUT=ROOT/'artifacts/kitchen-golden-runtime'
font=ImageFont.truetype('C:/Windows/Fonts/arial.ttf',18)
records=[]
for n in range(1,5):
    ref=Image.open(OUT/f'lv{n}-reference.png').convert('RGB');run=Image.open(OUT/f'lv{n}-runtime.png').convert('RGB')
    assert ref.size==run.size==(390,844)
    board=Image.new('RGB',(780,880),'#f5ecd9');board.paste(ref,(0,36));board.paste(run,(390,36))
    d=ImageDraw.Draw(board);d.text((16,9),f'Lv.{n}  Golden Reference',font=font,fill='#483323');d.text((406,9),f'Lv.{n}  Runtime',font=font,fill='#483323');board.save(OUT/f'lv{n}-comparison.png')
    records.append({'level':n,'reference':f'lv{n}-reference.png','runtime':f'lv{n}-runtime.png','comparison':f'lv{n}-comparison.png','imageSize':[390,844],'comparisonSize':[780,880],'comparisonHeaderHeight':36,'runtimeSha256':hashlib.sha256((OUT/f'lv{n}-runtime.png').read_bytes()).hexdigest()})
qa=json.loads((OUT/'qa-report.json').read_text(encoding='utf8'));assert qa['passed'] and not qa['captureOnly']
(OUT/'comparison-manifest.json').write_text(json.dumps({'pairs':records,'qaCheckedAt':qa['checkedAt'],'noScreenshotRetouching':True},ensure_ascii=False,indent=2)+'\n',encoding='utf8')
html='''<!doctype html><html lang="zh-CN"><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>鸡宝厨房 · 四级还原验收</title><style>
*{box-sizing:border-box}body{margin:0;background:#f4efdf;color:#422c1b;font-family:"Microsoft YaHei",sans-serif}main{max-width:1000px;margin:auto;padding:28px 20px}h1{font-size:25px;margin:0 0 12px}p{line-height:1.8}nav{display:flex;gap:16px;flex-wrap:wrap;padding:16px 0;position:sticky;top:0;background:#f4efdfed}a{color:#496c37}section{margin:26px 0 40px;scroll-margin-top:60px}h2{font-size:20px}img.compare{display:block;width:min(100%,780px);height:auto;border:1px solid #c6b393;border-radius:6px}.states{display:flex;gap:10px;overflow-x:auto;padding-bottom:10px}.states figure{margin:0;flex:0 0 180px}.states img{width:180px;border:1px solid #d5c5aa;border-radius:5px}figcaption{padding:9px 0;font-size:13px}.note{padding:15px 18px;background:#fff9e9;border-left:4px solid #b59047}.facts{display:flex;gap:20px;flex-wrap:wrap;font-size:14px}footer{padding:24px 0;border-top:1px solid #caba9d}
</style><main><h1>鸡宝厨房 · Lv.1–Lv.4</h1><p>正式 Runtime 与指定 Golden Reference 的同尺寸对照。每一侧均为 390 × 844，浏览器原始截图未修图。</p><nav><a href="#lv1">Lv.1 茅草</a><a href="#lv2">Lv.2 木屋</a><a href="#lv3">Lv.3 搪瓷盆</a><a href="#lv4">Lv.4 软垫座</a><a href="#narrow">320 宽</a></nav><div class="note">四级已接入真实状态与交互。空内衬纹理、蛋描边和部分字体 / 图标仍有细节差异；不将功能通过等同于视觉完全一致。Lv.1 的 1/1 调味槽是现有玩法规则，参考中的 1/3 未写入游戏。</div>'''
for n in range(1,5):
    html+=f'<section id="lv{n}"><h2>Lv.{n} · Reference / Runtime</h2><a href="lv{n}-comparison.png"><img class="compare" src="lv{n}-comparison.png" alt="Lv.{n} 同尺寸对照"></a><p><a href="lv{n}-reference.png">参考原尺寸</a> · <a href="lv{n}-runtime.png">Runtime 原尺寸</a></p><div class="states">'
    for key,label in [('empty','空锅'),('runtime','孵化中'),('ready','可收取'),('collected','全收完'),('partial','部分可收')]:html+=f'<figure><a href="lv{n}-{key}.png"><img src="lv{n}-{key}.png" alt="Lv.{n} {label}" loading="lazy"></a><figcaption>{label}</figcaption></figure>'
    html+='</div></section>'
html+='<section id="narrow"><h2>320 × 844 窄屏</h2><div class="states">'
for n in [1,4]:html+=f'<figure><a href="lv{n}-320x844.png"><img src="lv{n}-320x844.png" alt="Lv.{n} 320宽"></a><figcaption>Lv.{n} · 320 × 844</figcaption></figure>'
html+='</div></section><footer><a href="qa-report.json">功能回归记录</a> · <a href="comparison-manifest.json">截图尺寸与哈希</a> · <a href="../../docs/kitchen-golden-runtime/DELIVERY.md">完整交付说明</a></footer></main></html>'
(OUT/'index.html').write_text(html,encoding='utf8')
print('4 Reference/Runtime pairs verified at 390x844; 4 comparison boards and review gallery created.')
