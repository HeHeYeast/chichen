import {createRequire} from 'node:module';
import {pathToFileURL} from 'node:url';
import {resolve} from 'node:path';
import assert from 'node:assert/strict';

const require=createRequire(import.meta.url);
const {chromium}=require(process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const dir='web/prototypes/bottom-nav-compact-v1';
const browser=await chromium.launch({headless:true,executablePath:'C:/Program Files/Google/Chrome/Application/chrome.exe'});
try{
  const variant=await browser.newPage({viewport:{width:390,height:79},deviceScaleFactor:1});
  const errors=[];variant.on('pageerror',e=>errors.push(e.message));
  await variant.goto(pathToFileURL(resolve(dir,'index.html')).href);
  await variant.evaluate(()=>document.fonts.ready);
  assert.equal(await variant.locator('.tab').count(),5);
  assert.equal(await variant.locator('.tab.selected').count(),1);
  assert.equal(await variant.locator('.tab.selected').getAttribute('data-tab'),'farm');
  await variant.screenshot({path:dir+'/bottom-nav-compact-v1-390x79.png'});

  const states=await browser.newPage({viewport:{width:390,height:395},deviceScaleFactor:1});
  states.on('pageerror',e=>errors.push(e.message));
  await states.goto(pathToFileURL(resolve(dir,'states.html')).href);
  await Promise.all(states.frames().slice(1).map(frame=>frame.evaluate(()=>document.fonts.ready)));
  assert.equal(states.frames().length,6);
  for(const [i,frame] of states.frames().slice(1).entries()){
    assert.equal(await frame.locator('.tab.selected').count(),1);
    assert.equal(await frame.locator('.tab.selected').getAttribute('data-tab'),['kitchen','farm','business','explore','book'][i]);
  }
  await states.screenshot({path:dir+'/bottom-nav-compact-v1-states-390x395.png'});

  const farm=await browser.newPage({viewport:{width:390,height:844},deviceScaleFactor:1});
  await farm.goto(pathToFileURL(resolve('web/prototypes/farm-ui-v1-20260929/index.html')).href);
  await farm.evaluate(()=>document.fonts.ready);
  const farmNav=await farm.locator('.nav').screenshot();
  const variantNav=await variant.locator('.nav').screenshot();
  const maxChannelDifference=await variant.evaluate(async ([left,right])=>{
    const load=src=>new Promise((resolve,reject)=>{const image=new Image();image.onload=()=>resolve(image);image.onerror=reject;image.src='data:image/png;base64,'+src;});
    const [a,b]=await Promise.all([load(left),load(right)]);
    const pixels=image=>{const canvas=document.createElement('canvas');canvas.width=image.width;canvas.height=image.height;const context=canvas.getContext('2d');context.drawImage(image,0,0);return context.getImageData(0,0,canvas.width,canvas.height).data;};
    const ap=pixels(a),bp=pixels(b);let max=0;for(let i=0;i<ap.length;i++)max=Math.max(max,Math.abs(ap[i]-bp[i]));return max;
  },[farmNav.toString('base64'),variantNav.toString('base64')]);
  assert(maxChannelDifference<=2,'The archived navigation differs visibly from the Farm screen');
  assert.equal(errors.length,0,errors.join('\n'));
  console.log(`PASS 390x79 bottom nav; 5 active states; Farm screen max pixel difference ${maxChannelDifference}/255; no page errors`);
}finally{await browser.close();}
