// Exercise the offline payload, never the source server: a missing packaged file
// must remain missing even when it exists in the developer's working tree.
import http from 'node:http';
import path from 'node:path';
import {createRequire} from 'node:module';
import {readFile,mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {kitIcon,kitArt,kitArrow} from '../web/ui-kit.js';
import {SKILL_ART,BRANCH_ART,SKILL_POINT_ART} from '../web/skill-icons.js';

const require=createRequire(import.meta.url);
const {chromium}=require(process.argv.slice(2).find(a=>!a.startsWith('--'))??process.env.CHICK_PLAYWRIGHT_PACKAGE??process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const payload=path.resolve(process.env.CHICK_QA_PAYLOAD??'android/generated-assets');
const out=path.resolve(process.env.CHICK_QA_MOBILE_OUTPUT??'artifacts/mobile-package-audit');
await mkdir(out,{recursive:true});
const mime={'.html':'text/html','.js':'text/javascript','.css':'text/css','.json':'application/json','.png':'image/png','.svg':'image/svg+xml','.woff2':'font/woff2','.mp3':'audio/mpeg'};
const server=http.createServer(async(req,res)=>{
  try{
    const url=new URL(req.url,'http://localhost'),name=url.pathname==='/'?'/web/index.html':decodeURIComponent(url.pathname);
    const target=path.resolve(payload,'.'+name);
    if(!target.startsWith(payload+path.sep))throw Error('outside payload');
    const body=await readFile(target);res.writeHead(200,{'Content-Type':mime[path.extname(target)]??'application/octet-stream','Cache-Control':'no-store'}).end(body);
  }catch{res.writeHead(404).end('Missing packaged asset');}
});
await new Promise(resolve=>server.listen(0,'127.0.0.1',resolve));
const base=`http://127.0.0.1:${server.address().port}`;
const browser=await chromium.launch({headless:true,executablePath:process.env.CHICK_QA_CHROME});
const report={passed:false,payload,checks:[],errors:[],missing:[],screens:[],nativeDeviceTest:false};
let page,context;
const read=()=>page.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1')));
async function checkArt(){
  await page.evaluate(()=>document.fonts.ready);
  const bad=await page.locator('img').evaluateAll(es=>es.filter(e=>e.getBoundingClientRect().width>0&&e.complete&&!e.naturalWidth).map(e=>e.src));
  report.missing.push(...bad);
}
async function shot(name){await checkArt();await page.screenshot({path:path.join(out,name+'.png')});report.screens.push(name);}
async function open(seed,width=390,height=844,features={}){
  await context?.close();context=await browser.newContext({viewport:{width,height},hasTouch:true,isMobile:true,deviceScaleFactor:3,reducedMotion:'reduce'});
  await context.addInitScript(({seed,features})=>{
    if(seed&&!sessionStorage.seeded){localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));sessionStorage.seeded='1';}
    localStorage.setItem('chick-kitchen-ui-v1',JSON.stringify({seenSupplyMove:true}));
    // First-visit guidance is part of the player experience, including its hit targets.
    Object.defineProperty(navigator,'webdriver',{get:()=>false});
    if(features.roundRect===false)CanvasRenderingContext2D.prototype.roundRect=undefined;
    if(features.has===false){const supports=CSS.supports.bind(CSS);CSS.supports=(...args)=>args[0]==='selector(:has(*))'?false:supports(...args);}
  },{seed,features});
  page=await context.newPage();page.on('pageerror',e=>report.errors.push(e.message));
  page.on('response',r=>{if(r.status()>=400)report.missing.push(r.status()+' '+r.url());});
  await page.goto(base);return page;
}
const tap=async selector=>{await page.locator(selector).first().tap();await page.waitForTimeout(120);};
const nav=async name=>{await page.locator('#main-nav').getByRole('button',{name,exact:true}).tap();await page.waitForTimeout(180);};
const control=id=>tap(`[data-control-id="${id}"]`);
async function start(){await page.getByRole('button',{name:'开始游戏',exact:true}).tap();await page.waitForTimeout(650);}
async function closePanel(){await tap('#panels .close');}
async function touchScroll(selector){
  const target=page.locator(selector).first();
  const metrics=await target.evaluate(el=>({top:el.scrollTop,max:el.scrollHeight-el.clientHeight}));
  if(metrics.max<=10)return;
  const r=await target.boundingBox(),cdp=await context.newCDPSession(page);
  const x=r.x+r.width*.5,start=Math.min(r.y+r.height-25,heightOfViewport()-90),end=Math.max(r.y+25,start-160);
  await cdp.send('Input.dispatchTouchEvent',{type:'touchStart',touchPoints:[{x,y:start}]});
  for(let step=1;step<=10;step++){
    await cdp.send('Input.dispatchTouchEvent',{type:'touchMove',touchPoints:[{x,y:start+(end-start)*step/10}]});
    await page.waitForTimeout(25);
  }
  await cdp.send('Input.dispatchTouchEvent',{type:'touchEnd',touchPoints:[]});await page.waitForTimeout(180);await cdp.detach();
  const after=await target.evaluate(el=>el.scrollTop);
  assert.ok(after>metrics.top+10,`${selector}: a real finger swipe must scroll, ${metrics.top} → ${after}`);
}
const heightOfViewport=()=>page.viewportSize().height;
try{
  // Composed paths cannot be discovered by literal scanning. Check the rendered helpers.
  const markup=Object.values(kitIcon).join('')+kitArrow+kitArt('crate-empty');
  const paths=new Set([...markup.matchAll(/src="([^"]+)"/g)].map(m=>m[1]).concat(Object.values(SKILL_ART),Object.values(BRANCH_ART),SKILL_POINT_ART));
  for(const asset of paths){const response=await fetch(base+asset);if(!response.ok)report.missing.push(response.status+' '+asset);}
  report.checks.push(`offline icon and skill paths: ${paths.size}`);
  for(const [width,height] of [[320,568],[390,844],[430,932]]){
    await open(null,width,height);await start();await control('batch');await page.locator('[data-yes]').waitFor();
    await tap('[data-yes]');
    await page.waitForFunction(()=>JSON.parse(localStorage.getItem('chick-kitchen-v1'))?.batch?.eggs?.length===24);
    const state=await read();assert.equal(state.batch.tool,0);assert.equal(state.cp,600);
    await shot('fresh-cooking-'+width);await page.reload();await start();assert.equal((await read()).batch.started,state.batch.started);
    await control('ingredient');await page.locator('.next-batch-screen').waitFor();
    await tap('[data-nb-lens="manual"]');await tap('[data-nb-tool="0"]');await tap('[data-ok]');
    await control('settings');await tap('[data-save]');await tap('#dialog-layer .gd-close');await tap('[data-return]');
    for(const [id,selector] of [['supply','.shop-screen'],['inventory','.warehouse-screen'],['workshop','.workshop-screen'],['help','.guide-screen']]){
      await control(id);await page.locator(selector).waitFor();
      if(id==='supply')await touchScroll('.shop-shelf-scroll');
      await shot(id+'-'+width);await closePanel();
    }
    for(const name of ['农场','生意','寻访','图鉴','厨房']){await nav(name);await shot(name+'-'+width);}
    report.checks.push(`${width}×${height}: actual touch, fresh cooking, reload, save dialog, four shortcuts, five pages`);
  }
  const at=Date.now(),seed=E.freshState(at,31);seed.cp=50000;seed.kitchenLevel=3;seed.toolLevels=Array(9).fill(0);seed.duck=true;seed.ingredients={0:5,1:5,2:5};seed.progress.tutorialSeen=true;seed.total={'0:0':24};seed.farm={'0:0':24};seed.sound=false;seed.music=false;
  await open(seed);await start();await control('workshop');
  for(const branch of ['CUL','HOME','TRADE','OBS','TRIP']){
    const button=page.locator(`[data-branch="${branch}"]`);if(await button.count())await button.tap();await shot('skills-'+branch);
  }
  await nav('生意');
  // 2026-10-07: orders are slips on the 生意 page (an order's name opens its sheet); 常客 / 项目 are the small cards
  for(const kind of ['orders','regulars','projects']){if(kind==='orders'){if(!await page.locator('#panels .bh-main [data-order-detail]').count())continue;await tap('#panels .bh-main [data-order-detail]');await shot(kind);await tap('#panels [data-business-sheet-close]');continue;}await tap(`[data-trade-view="${kind}"]`);await shot(kind);await tap('.shop-subpage [data-shop-back]');}
  await nav('寻访');await tap('[data-journey-enter]');await shot('journey-region');
  await tap('[data-journey-help]');await page.locator('.journey-help[open]').waitFor();await tap('[data-journey-help-close]');
  await nav('图鉴');
  for(const tab of ['species','recipes','collections','calendar']){
    const button=page.locator(`[data-book-tab="${tab}"]`).first();if(await button.count()){await button.tap();await page.waitForTimeout(180);await shot('book-'+tab);}
  }
  report.checks.push('returning save: all skills, business subpages, journey help modal, book tabs');
  for(const features of process.argv.includes('--baseline')?[]:[{roundRect:false},{has:false}]){
    const raw=JSON.stringify(seed);await open(seed,390,844,features);
    await page.getByRole('heading',{name:'请更新系统网页组件'}).waitFor({timeout:3000});
    assert.equal(await page.evaluate(()=>localStorage.getItem('chick-kitchen-v1')),raw);
    assert.equal(await page.locator('#main-nav button').count(),0);
    report.checks.push('unsupported '+Object.keys(features)[0]+': guidance before save load/write');
  }
  assert.deepEqual(report.errors,[],'JavaScript errors');
  assert.deepEqual([...new Set(report.missing)],[],'missing packaged art');report.passed=true;
}catch(error){report.failure=error.stack;if(page&&!page.isClosed())await page.screenshot({path:path.join(out,'failure.png')});throw error;}
finally{await browser.close();await new Promise(resolve=>server.close(resolve));report.missing=[...new Set(report.missing)];await writeFile(path.join(out,'report.json'),JSON.stringify(report,null,2)+'\n');console.log(JSON.stringify(report,null,2));}
