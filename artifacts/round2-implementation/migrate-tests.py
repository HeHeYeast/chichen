from pathlib import Path
p=Path('tests/integration.test.mjs');s=p.read_text(encoding='utf-8').replace("['A','A','A','B','C','C','D',...(special?['S']:[])]", "['1','2','3','4','5',...(special?['S']:[])]")
s=s.replace("P.learnSkill(s,'CUL-C')","P.learnSkill(s,'CUL-3')").replace('P.skillPoints(s).spent,16','P.skillPoints(s).spent,18')
s=s.replace("const s=full();branch(s,'CUL',true);", "const s=full();branch(s,'CUL',true);P.learnSkill(s,'HOME-1');P.learnSkill(s,'HOME-4');")
s=s.replace('Math.ceil(Math.max(6,base*.75)*60000)/60000','Math.ceil(Math.max(6,base*.8)*60)/60')
s=s.replace('2*E.cookInfo(s,1).originalMinutes','Math.max(120,2*E.cookInfo(s,1).originalMinutes)')
s=s.replace("branch(cycle,'HOME');", "branch(cycle,'HOME',true);")
s=s.replace('Math.floor(price*10*.1)','Math.floor(price*10*.06)')
s=s.replace("P.learnSkill(s,'TRADE-A');P.learnSkill(s,'TRADE-A');P.learnSkill(s,'TRADE-B');", "P.learnSkill(s,'TRADE-1');P.learnSkill(s,'TRADE-3');")
s=s.replace("P.learnSkill(s,'CUL-A')", "P.learnSkill(s,'CUL-1')")
s=s.replace("[[{},0],[{'CUL-A':1},.04],[{'CUL-A':2},.07],[{'CUL-A':3},.10],[{'CUL-C':1},.15],[{'CUL-C':2},.20],[{'CUL-S':1},.25]]", "[[{},0],[{'CUL-2':1},.10],[{'CUL-S':1},.20]]")
s=s.replace('Math.ceil(Math.max(6,b*(1-reduction))*60000)/60000','Math.ceil(Math.max(6,b*(1-reduction))*60)/60')
p.write_text(s,encoding='utf-8')
# Keep historical inputs intact and explicitly extend the expected migrated contract.
import json
p=Path('tests/fixtures/save-contract.json');data=json.loads(p.read_text(encoding='utf-8'))
for c in data['cases']:
 n=c['normalized'];pgr=n['progress'];old=c['state']['version']==3
 pgr.update(skillVersion=2,migrationRespec=old,migrationNotice=old,leftovers=[],hotStove=None,lastHarvest=None)
 pgr['skills']={};pgr['trade'].update(markupRemainder=0,category=None);pgr['protection']['calm']=False
p.write_text(json.dumps(data,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
