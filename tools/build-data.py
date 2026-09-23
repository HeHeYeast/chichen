"""Extract original XML data and translate the original recipe eligibility branches."""
from pathlib import Path
import json
import re
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
def records(path, tag):
    return [{c.tag: int(c.text) if re.fullmatch(r'-?\d+', c.text or '') else c.text for c in e}
            for e in ET.parse(path).findall('.//' + tag)]

data = {
    'characters': [records(ROOT / f'assets/characters_file_{i}.xml', 'character') for i in range(2)],
    'tools': [records(ROOT / f'assets/tools_file_{i}.xml', 'tool') for i in range(4)],
    'images': [str(p.relative_to(ROOT)).replace('\\', '/') for p in (ROOT / 'assets/png').rglob('*') if p.is_file()],
}
(ROOT / 'web/data.js').write_text('export const DATA = ' + json.dumps(data, ensure_ascii=False, separators=(',', ':')) + ';\n', encoding='utf-8')

src = (ROOT / 'artifacts/original-source/sources/com/idtinc/maingame/sublayout0/MainGameUnit.java').read_text()
body = src[src.index('        if (_eggID == 0) {', src.index('public ArrayList<Short> getRateArrayWithID')):src.index('    public void addCharacterToAllRateArrayList')]
body = body[:body.rfind('    }')]
body = re.sub(r'^.*Log\.[di].*\n', '', body, flags=re.M)
body = re.sub(r'\(short\) \(Math.random\(\) \* ([\w.]+)\)', r'Math.trunc(random() * \1)', body)
body = re.sub(r'\((?:short|int|float|double)\)\s*', '', body)
body = re.sub(r'(\d+\.\d+)[fd]\b', r'\1', body)
body = re.sub(r'\b(?:short|int) (\w+)', r'let \1', body)
body = re.sub(r'this\.appDelegate\.getTool1LevelWithIndex\(_tool1_ID\)', 'state.toolLevels[_tool1_ID]', body)
body = re.sub(r'this\.tool_2_SelectView\.tool_2_(\d)', r'(ingredients[\1] ?? -1)', body)
body = body.replace('this.appDelegate.defaultSharedPreferences != null && ', '')
body = body.replace('this.appDelegate != null && this.appDelegate.defaultSharedPreferences != null', 'true')
body = re.sub(r'this\.appDelegate\.defaultSharedPreferences.getBoolean\("([^"]+)", false\)', r'!!state.events?.["\1"]', body)
body = re.sub(r'this\.appDelegate\.defaultSharedPreferences.getInt\("([^"]+)", -1\)', r'(state.events?.["\1"] ?? -1)', body)
body = re.sub(r'\s*SharedPreferences.Editor editor[^;]+;\s*editor.putInt[^;]+;\s*editor.putInt[^;]+;\s*editor.commit\(\);', '', body)
body = re.sub(r'SimpleDateFormat sdf = new SimpleDateFormat\("HH"\);\s*Date nowDate = new Date\(\);\s*let hourInt = Integer.parseInt\(sdf.format\(nowDate\)\);', 'let hourInt = new Date(now).getHours();', body)
body = body.replace('Math.random()', 'random()').replace('returnRateArrayList.size()', 'returnRateArrayList.length')
body = re.sub(r'returnRateArrayList.remove\((\d+)\)', r'returnRateArrayList.splice(\1, 1)', body)
body = re.sub(r'Short.valueOf\((\w+)\)', r'\1', body)
body = re.sub(r'returnRateArrayList.add\((\w+), ([\w]+)\)', r'returnRateArrayList.splice(\1, 0, \2)', body)
body = body.replace('getFinalRateArrayWithAllRateArray(allRateArrayList)', 'samplePool(allRateArrayList, random)')
body = re.sub(r'addCharacterToAllRateArrayList\(_eggID, (\d+), allRateArrayList\)', r'add(\1)', body)
# The same eligibility branches serve production and a symbolic, RNG-free query.
# Preview collects every branch of the two stochastic pool gates; it never samples.
body = body.replace('Math.trunc(random() * 10.0)', '(preview ? 0 : Math.trunc(random() * 10.0))')
body = body.replace('Math.trunc(random() * 8.0)', '(preview ? 0 : Math.trunc(random() * 8.0))')
body = body.replace('samplePool(allRateArrayList, random)', '(preview ? [] : samplePool(allRateArrayList, random))')
body = body.replace('Math.trunc(random() * 100.0)', '(preview ? -1 : Math.trunc(random() * 100.0))')
body = re.sub(r'\(random\(\) \* returnRateArrayList.length\)', '(preview ? 0 : random() * returnRateArrayList.length)', body)
body = body.replace('returnRateArrayList.splice(randInsertIndex, 0, idShort);', '''if(preview){
                      const target=state.events?.gift_tool_2_68_character_id;
                      if(Number.isInteger(target)&&target>=89&&target<=103)gifts.push({id:target,guaranteed:1});
                      else for(let id=89;id<=102;id++)gifts.push({id,guaranteed:0});
                    }else returnRateArrayList.splice(randInsertIndex, 0, idShort);''')
body = body.replace('returnRateArrayList.splice(randInsertIndex2, 0, 106);', 'if(preview)gifts.push({id:106,guaranteed:1});else returnRateArrayList.splice(randInsertIndex2, 0, 106);')
body = body.replace('returnRateArrayList.splice(randInsertIndex3, 0, 107);', 'if(preview)gifts.push({id:107,guaranteed:1});else returnRateArrayList.splice(randInsertIndex3, 0, 107);')
body = body.replace('return returnRateArrayList;', 'return preview ? {pool:allRateArrayList,gifts} : returnRateArrayList;')
prelude = '''// Generated from MainGameUnit.getRateArrayWithID; do not hand-edit.
import { DATA } from './data.js';
export function samplePool(pool, random = Math.random) {
  const remaining = [...pool], result = [];
  for (let i = 0; i < 24; i++) {
    const index = Math.floor(random() * remaining.length);
    result.push(remaining.splice(index, 1)[0]);
  }
  return result;
}
export function originalRecipes(state, egg, tool, ingredients, now = Date.now(), random = Math.random) {
  return originalRules(state,egg,tool,ingredients,now,random,false);
}
export function originalRecipePlan(state,egg,tool,ingredients,now=Date.now()) {
  return originalRules(state,egg,tool,ingredients,now,()=>{throw Error('Preview attempted random sampling');},true);
}
function originalRules(state, _eggID, _tool1_ID, ingredients, now, random, preview) {
  const tool_0_0_level = state.kitchenLevel;
  const totalChars0Cnt = Object.entries(state.total).filter(([k]) => k.startsWith('0:')).reduce((n, [,v]) => n+v, 0);
  const totalChars1Cnt = Object.entries(state.total).filter(([k]) => k.startsWith('1:')).reduce((n, [,v]) => n+v, 0);
  const totalCharsAllCnt = totalChars0Cnt + totalChars1Cnt;
  const allRateArrayList = [];
  const gifts = [];
  let returnRateArrayList = null;
  const add = id => { const c = DATA.characters[_eggID].find(c => c.id === id); if(c) for(let i=0; i<c.rate; i++) allRateArrayList.push(id); };
'''
(ROOT / 'web/recipes.js').write_text(prelude + body + '\n}\n', encoding='utf-8')
print('Extracted', [len(x) for x in data['characters']], 'characters;', [len(x) for x in data['tools']], 'tools;', len(data['images']), 'original images')
