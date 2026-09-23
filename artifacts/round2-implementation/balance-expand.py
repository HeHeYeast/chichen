from pathlib import Path
p=Path('tools/audit-round2-balance.mjs');s=p.read_text(encoding='utf-8').replace("care:['HOME-1','HOME-3','HOME-5']", "care:['HOME-1','HOME-3','HOME-5'],cook_master:['CUL-1','CUL-2','CUL-3','CUL-4','CUL-S'],trade_master:['TRADE-1','TRADE-2','TRADE-3','TRADE-4','TRADE-S'],care_master:['HOME-2','HOME-3','HOME-4','HOME-5','HOME-S']")
s=s.replace('const base=structuredClone(proto);', "if(build.endsWith('master')&&stage.d<80)continue;\n  const base=structuredClone(proto);")
s=s.replace("base.progress.protection.calm=build==='care';", "base.progress.protection.calm=build.startsWith('care');")
s=s.replace('(stage.k+1)*50*24', '(100+stage.k*50)*24')
s=s.replace("if(P.rank(base,'TRADE-2')){const counts={};for(const r of paths)", "if(P.rank(base,'TRADE-2')){const counts={};for(const r of paths)")
p.write_text(s,encoding='utf-8')
