from pathlib import Path
p=Path('tools/qa-round2.mjs');s=p.read_text(encoding='utf-8').replace("await p.locator('.future-skills>summary').click();await p.locator('[data-skill-detail", "if(await p.locator('.future-skills').getAttribute('open')===null)await p.locator('.future-skills>summary').click();await p.locator('[data-skill-detail")
s=s.replace("await p.locator('[data-control-id^=\"egg:\"]').first().click({force:true});", "await p.locator('[data-control-id^=\"egg:\"]').first().press('Enter');assert.ok((await read(p)).batch.eggs.some(e=>e.collected));")
p.write_text(s,encoding='utf-8')
