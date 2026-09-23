import {writeFile} from 'node:fs/promises';
const {chromium}=await import('file:///'+process.env.APPDATA.replaceAll('\\','/')+'/npm/node_modules/gsd-pi/node_modules/playwright-core/index.mjs');const b=await chromium.connectOverCDP('http://127.0.0.1:9223');const p=b.contexts()[0].pages()[0];
const mode=process.argv[2]??'export';
if(mode==='export'){
 await p.getByRole('button',{name:'厨房',exact:true}).click();await p.getByRole('button',{name:'设置',exact:true}).click();
 await writeFile('artifacts/device-qa/20260923/before-export.json',await p.evaluate(()=>JSON.parse(ChickNative.loadSave()).raw));await p.locator('[data-export]').click();
}else if(mode==='import'){
 const yes=p.locator('[data-yes]');if(await yes.count())await yes.click();
 if(!(await p.locator('[data-import]').count())){await p.getByRole('button',{name:'厨房',exact:true}).click();await p.getByRole('button',{name:'设置',exact:true}).click();}
 await p.locator('[data-import]').click();
}else if(mode==='confirm'){
 console.log(await p.locator('#dialog-layer').innerText());await p.locator('[data-yes]').click();await p.waitForTimeout(500);console.log(await p.locator('#dialog-layer').innerText());await writeFile('artifacts/device-qa/20260923/after-picker-import.json',await p.evaluate(()=>JSON.parse(ChickNative.loadSave()).raw));await p.screenshot({path:'artifacts/device-qa/20260923/import-restored.png'});
}else console.log(await p.locator('body').innerText());
await b.close();
