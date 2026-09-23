from pathlib import Path
p=Path('web/shop-ui.js');s=p.read_text(encoding='utf-8').replace('const price=item.buy_cp*quantity,rebate=', 'const state=getState(),price=item.buy_cp*quantity,rebate=');p.write_text(s,encoding='utf-8')
p=Path('web/workshop-ui.js');s=p.read_text(encoding='utf-8').replace('skillDetail=null;', 'skillDetail=null,tripStatus=null;',1)
s=s.replace('detailKey=null;skillDetail=null;\n    const s=getState(),t=s.progress.trip', 'detailKey=null;skillDetail=null;tripStatus=getState().progress.trip?.status;\n    const s=getState(),t=s.progress.trip')
s=s.replace("if(!detailKey&&!skillDetail&&tab==='trip'&&getState().progress.trip?.status==='returned')trip();", "if(!detailKey&&!skillDetail&&tab==='trip'&&tripStatus!==getState().progress.trip?.status)trip();")
p.write_text(s,encoding='utf-8')
