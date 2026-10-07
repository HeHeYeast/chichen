// Page captures for UI before/after review, from a copy of an exported save in an
// isolated browser profile. usage: node tools/capture-ui-review.mjs <save.json> <output-directory>
import {createRequire} from 'node:module';
import {spawn} from 'node:child_process';
import {mkdir,readFile} from 'node:fs/promises';
const require=createRequire(import.meta.url),{chromium}=require(process.env.PLAYWRIGHT_MODULE??process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const [savePath,out]=process.argv.slice(2);if(!savePath||!out)throw Error('usage: node tools/capture-ui-review.mjs <save.json> <output-directory>');
const raw=JSON.parse(await readFile(savePath,'utf8')),save=raw.save??raw,at=Math.max(save.lastSeen??0,save.clock?.logicalAt??0)+60000;
await mkdir(out,{recursive:true});
const server=spawn(process.execPath,['server.mjs','0'],{stdio:['ignore','pipe','pipe'],windowsHide:true});
const base=await new Promise((ok,no)=>{server.on('error',no);server.stdout.on('data',b=>{const m=String(b).match(/http:\/\/127\.0\.0\.1:\d+/);if(m)ok(m[0]);});});
const browser=await chromium.launch({headless:true});const errors=[];
const nav=(page,name)=>page.locator('#main-nav').getByRole('button',{name,exact:true}).click();
const control=(page,id)=>page.locator(`[data-control-id="${id}"]`).click();
const steps=[
 ['01-kitchen',async p=>{}],
 ['02-seasoning',async p=>{await control(p,'ingredient');await p.locator('.ingredient-screen').waitFor();}],
 ['03-cook-confirm',async p=>{await control(p,'tool:1');await p.locator('.cooking-dialog').waitFor();}],
 ['04-warehouse',async p=>{await control(p,'inventory');await p.waitForTimeout(300);}],
 ['05-workshop',async p=>{await control(p,'workshop');await p.locator('.workshop-screen').waitFor();}],
 ['06-shop',async p=>{await control(p,'supply');await p.locator('.shop-screen').waitFor();}],
 ['07-settings',async p=>{await control(p,'settings');await p.locator('.settings-screen').waitFor();}],
 ['08-farm',async p=>{await nav(p,'农场');await p.waitForTimeout(500);}],
 ['09-business',async p=>{await nav(p,'生意');await p.waitForTimeout(500);}],
 ['10-journey',async p=>{await nav(p,'寻访');await p.waitForTimeout(500);}],
 ['11-book',async p=>{await nav(p,'图鉴');await p.waitForTimeout(500);}],
];
try{
 for(const [w,h] of [[390,844],[360,780]])for(const [name,go] of steps){
  const c=await browser.newContext({viewport:{width:w,height:h},reducedMotion:'reduce',deviceScaleFactor:2});
  await c.addInitScript(({s,at})=>{Date.now=()=>at;if(!sessionStorage.getItem('seeded')){localStorage.setItem('chick-kitchen-v1',JSON.stringify(s));localStorage.setItem('chick-kitchen-ui-v1',JSON.stringify({seenSupplyMove:true}));sessionStorage.setItem('seeded','1');}},{s:save,at});
  const page=await c.newPage();page.on('pageerror',e=>errors.push(name+': '+e.message));page.on('response',r=>{if(r.status()>=400)errors.push(name+': '+r.status()+' '+r.url());});
  await page.goto(base);await page.getByRole('button',{name:'开始游戏',exact:true}).click();await page.waitForTimeout(700);await page.evaluate(()=>document.fonts.ready);
  try{await go(page);await page.waitForTimeout(250);await page.screenshot({path:`${out}/${name}-${w}.png`});}catch(e){errors.push(name+': '+e.message.split('\n')[0]);}
  await c.close();
 }
 console.log('captured',out,errors.length?'\n'+errors.join('\n'):'');
}finally{await browser.close();server.kill();}
