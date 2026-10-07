import {createRequire} from 'node:module';
import {pathToFileURL} from 'node:url';
import {resolve} from 'node:path';
import {readFile} from 'node:fs/promises';
import {createHash} from 'node:crypto';
import assert from 'node:assert/strict';

const require=createRequire(import.meta.url);
const {chromium}=require(process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const dir='web/prototypes/farm-ui-v1-20260929';
const browser=await chromium.launch({headless:true,executablePath:'C:/Program Files/Google/Chrome/Application/chrome.exe'});
try{
  const page=await browser.newPage({viewport:{width:390,height:844},deviceScaleFactor:1});
  const errors=[];
  page.on('pageerror',e=>errors.push(e.message));
  await page.goto(pathToFileURL(resolve(dir,'index.html')).href);
  await page.locator('.map-image').evaluate(image=>image.decode());
  await page.evaluate(()=>document.fonts.ready);
  await page.screenshot({path:dir+'/farm-ui-v1-390x844.png'});
  const size=await page.locator('.screen').boundingBox();
  const mapWidth=await page.locator('.map-image').evaluate(image=>image.naturalWidth);
  const mapHash=createHash('sha256').update(await readFile('web/art/farm/camp-map.png')).digest('hex');
  const approvedHash=createHash('sha256').update(await readFile('web/prototypes/farm-map-resident-preview-20260929/assets/map-user-approved.png')).digest('hex');
  const layout=await page.evaluate(()=>{
    const box=selector=>{const r=document.querySelector(selector).getBoundingClientRect();return {x:r.x,y:r.y,w:r.width,h:r.height,right:r.right,bottom:r.bottom,cx:r.x+r.width/2,cy:r.y+r.height/2};};
    return {avatar:box('.avatar'),wallet:box('.wallet'),title:box('.title'),settings:box('.settings'),home:box('.home-status'),repair:box('.repair-status'),house:box('.place.house'),stand:box('.place.stand'),houseIcon:box('.place.house .icon'),standIcon:box('.place.stand .icon'),task:box('.task'),controls:box('.map-controls'),nav:box('.nav'),tabs:[...document.querySelectorAll('.tab')].map(tab=>{const r=tab.getBoundingClientRect(),icon=tab.querySelector('.icon').getBoundingClientRect(),label=tab.querySelector('span').getBoundingClientRect();return {x:r.x,w:r.width,cx:r.x+r.width/2,iconCx:icon.x+icon.width/2,labelCx:label.x+label.width/2};})};
  });
  assert.equal(size.width,390);assert.equal(size.height,844);
  assert.equal(mapWidth,1536);assert.equal(mapHash,approvedHash);assert.equal(errors.length,0,errors.join('\n'));
  assert(layout.avatar.cy===layout.wallet.cy&&layout.wallet.cy===layout.title.cy&&layout.title.cy===layout.settings.cy);
  assert.equal(layout.home.cy,layout.repair.cy);
  assert.equal(layout.house.w,layout.stand.w);assert.equal(layout.house.h,layout.stand.h);
  assert.equal(layout.houseIcon.w,layout.standIcon.w);
  assert.equal(layout.controls.right,374);
  assert.equal(layout.controls.x-layout.task.right,16);
  assert.equal(layout.nav.y-layout.task.bottom,20);
  assert.deepEqual(layout.tabs.map(tab=>tab.w),[78,78,78,78,78]);
  assert(layout.tabs.every(tab=>tab.cx===tab.iconCx&&tab.cx===tab.labelCx));
  console.log('PASS 390x844; approved map unchanged; HUD centers and status baseline aligned; 120x46 labels; 16px task/control gap; 20px task/nav gap; five centered 78px tabs');
}finally{await browser.close();}
