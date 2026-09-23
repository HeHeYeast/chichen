from pathlib import Path
p=Path('web/workshop-ui.js');s=p.read_text(encoding='utf-8').replace("alertBox('首次收取24只：2点。", "alertBox((discoveryCount(getState())<190?'再发现'+(5-discoveryCount(getState())%5)+'种伙伴，获得1点。\\n':'')+'首次收取24只：2点。")
p.write_text(s,encoding='utf-8')
p=Path('web/engine.js');s=p.read_text(encoding='utf-8').replace('if(s.progress){harvestCredit(s);', 'if(s.progress){s.progress.discoveryClue=null;harvestCredit(s);');p.write_text(s,encoding='utf-8')
p=Path('web/app.js');s=p.read_text(encoding='utf-8').replace("if(bonus)toast('顺手拾金 · 额外 +1 CP',1400);", "if(bonus)toast('顺手拾金 · 额外 +1 CP',1400);if(state.progress.discoveryClue)toast('举一反三 · 已记下一条新线索，可在图鉴中查看'+(bonus?'\\n顺手拾金 · 额外 +1 CP':''),3500);")
p.write_text(s,encoding='utf-8')
p=Path('tools/qa-round2.mjs');s=p.read_text(encoding='utf-8');idx=s.index(' assert.deepEqual(errors,[])')
s=s[:idx]+''' {const s=mature();P.learnSkill(s,'CUL-1');E.startBatch(s,0,Date.now()-3*3600000,()=>0);const {c,p}=await boot(390,844,s);active=p;await p.waitForTimeout(3500);await p.locator('[data-control-id^="egg:"]').first().press('Enter');await shot(p,'effect-trigger');assert.match(await p.locator('.game-toast').innerText(),/额外/);await c.close();}
 {const {c,p}=await boot(390,844);active=p;await p.locator('.workshop-launch').click();await p.locator('[data-learn="CUL-1"]').click();const saved=await read(p);await p.evaluate(()=>{const original=Storage.prototype.setItem;window.__qaRestore=()=>Storage.prototype.setItem=original;Storage.prototype.setItem=function(){throw new DOMException('QA full disk','QuotaExceededError');};});await p.locator('[data-plan-apply]').click();await shot(p,'save-failure');assert.deepEqual((await read(p)).progress.skills,saved.progress.skills);await p.evaluate(()=>window.__qaRestore());await c.close();}
'''+s[idx:];p.write_text(s,encoding='utf-8')
