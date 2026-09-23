const {chromium}=await import('file:///'+process.env.APPDATA.replaceAll('\\','/')+'/npm/node_modules/gsd-pi/node_modules/playwright-core/index.mjs');
const b=await chromium.connectOverCDP('http://127.0.0.1:9223');
const p=b.contexts()[0].pages()[0];
console.log(await p.locator('body').innerText());
console.log(await p.evaluate(()=>({info:JSON.parse(ChickNative.platformInfo()),save:JSON.parse(ChickNative.loadSave()).status,width:innerWidth,height:innerHeight,buttons:[...document.querySelectorAll('button')].map(x=>({text:x.textContent,aria:x.getAttribute('aria-label'),control:x.dataset.controlId}))})));
await p.screenshot({path:'artifacts/device-qa/20260923/first-launch.png'});
await b.close();
