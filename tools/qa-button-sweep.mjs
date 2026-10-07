// Tap every visible button on each main page with a real touch, from a fresh
// load each time, and record whether anything observable happened. Run it on
// several real Chromium engines (CHICK_QA_CHROME) and compare the reports with
// tools/compare-button-sweeps.mjs: a button that responds on one engine and is
// dead or covered on another is an engine-specific defect.
//   node tools/qa-button-sweep.mjs [playwright-core path]
//   env: CHICK_QA_CHROME, CHICK_QA_PAYLOAD (default android/generated-assets),
//        CHICK_SWEEP_OUTPUT, CHICK_SWEEP_VIEWPORT (e.g. 384x780), CHICK_SWEEP_SEEDS, CHICK_SWEEP_TAP=0
import http from 'node:http';
import path from 'node:path';
import {createRequire} from 'node:module';
import {readFile,mkdir,writeFile} from 'node:fs/promises';

const require=createRequire(import.meta.url);
const {chromium}=require(process.argv.slice(2).find(a=>!a.startsWith('--'))??process.env.CHICK_PLAYWRIGHT_PACKAGE??process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const payload=path.resolve(process.env.CHICK_QA_PAYLOAD??'android/generated-assets');
const out=path.resolve(process.env.CHICK_SWEEP_OUTPUT??'artifacts/qa/button-sweep');
const [width,height]=(process.env.CHICK_SWEEP_VIEWPORT??'384x780').split('x').map(Number);
// Real exported saves: a new phone's near-empty game and a played-in game.
const SEEDS={
  fresh:'artifacts/device-backups/20261004-223129-baf54645/before-update.json',
  played:'artifacts/device-backups/20260930-235029-bce0392e/after-update.json',
};
const seedNames=(process.env.CHICK_SWEEP_SEEDS??Object.keys(SEEDS).join(',')).split(',');
const PAGES=(process.env.CHICK_SWEEP_PAGES??'厨房,农场,生意,寻访,图鉴').split(',');
await mkdir(out,{recursive:true});

const mime={'.html':'text/html','.js':'text/javascript','.css':'text/css','.json':'application/json','.png':'image/png','.svg':'image/svg+xml','.woff2':'font/woff2','.mp3':'audio/mpeg','.webp':'image/webp','.ogg':'audio/ogg','.wav':'audio/wav','.jpg':'image/jpeg'};
const server=http.createServer(async(req,res)=>{
  try{
    const url=new URL(req.url,'http://localhost'),name=url.pathname==='/'?'/web/index.html':decodeURIComponent(url.pathname);
    const target=path.resolve(payload,'.'+name);
    if(!target.startsWith(payload+path.sep))throw Error('outside payload');
    res.writeHead(200,{'Content-Type':mime[path.extname(target)]??'application/octet-stream','Cache-Control':'no-store'}).end(await readFile(target));
  }catch{res.writeHead(404).end('Missing packaged asset');}
});
await new Promise(resolve=>server.listen(0,'127.0.0.1',resolve));
const base=`http://127.0.0.1:${server.address().port}`;
const browser=await chromium.launch({headless:true,executablePath:process.env.CHICK_QA_CHROME||undefined});
const report={browser:browser.version(),viewport:[width,height],payload,results:[],failures:[]};

// Everything a player could notice after a tap, minus clocks the game rewrites on its own.
const snapshot=page=>page.evaluate(()=>{
  const save=JSON.parse(localStorage.getItem('chick-kitchen-v1')||'{}');
  for(const k of ['lastSeen','clock','meta','farmChecked'])delete save[k];
  const html=s=>document.querySelector(s)?.innerHTML??'';
  return JSON.stringify([html('#panels'),html('#dialog-layer'),html('#main-nav'),html('#controls').replace(/style="[^"]*"/g,''),
    document.querySelector('#announcement')?.textContent??'',document.querySelector('.toast,[role=alert]')?.textContent??'',save,
    [...document.querySelectorAll('#game *')].filter(e=>e.scrollTop>0).length]);
});
// Visible, enabled tap targets in a stable document order.
// Bounds are the page's own layout viewport: under 320 px wide the page lays out at 320.
const targets=page=>page.evaluate(()=>{const width=innerWidth,height=innerHeight;
  const list=[...document.querySelectorAll('#game button, #game [role=button], #game a[href], #game summary, #game .hotspot')];
  return list.map((el,index)=>{
    const r=el.getBoundingClientRect(),cs=getComputedStyle(el);
    const label=(el.getAttribute('aria-label')||el.textContent||el.dataset.controlId||el.className||'').replace(/\s+/g,' ').trim().slice(0,40);
    const x=r.left+r.width/2,y=r.top+r.height/2;
    const visible=r.width>4&&r.height>4&&x>0&&y>0&&x<width&&y<height&&cs.visibility!=='hidden'&&cs.pointerEvents!=='none'&&!el.closest('[inert],[hidden]');
    const top=visible?document.elementFromPoint(x,y):null;
    const covered=visible&&!(top&&(el===top||el.contains(top)||(el.classList.contains('hotspot')&&top.id==='controls')));
    return {index,label,control:el.dataset.controlId??'',inNav:!!el.closest('#main-nav'),disabled:el.disabled||el.getAttribute('aria-disabled')==='true',visible,covered,x,y,
      cover:covered&&top?(top.id?'#'+top.id:top.tagName.toLowerCase()+(top.className&&typeof top.className==='string'?'.'+top.className.split(' ')[0]:'')):''};
  }).filter(t=>t.visible&&!t.disabled);
});

async function openPage(seed,pageName){
  await context?.close();
  context=await browser.newContext({viewport:{width,height},hasTouch:true,isMobile:true,deviceScaleFactor:3,reducedMotion:'reduce'});
  const at=Date.parse('2026-10-05T10:00:00+08:00');
  await context.addInitScript(({seed,at})=>{
    const real=Date.now.bind(Date),start=real();Date.now=()=>at+(real()-start);
    localStorage.setItem('chick-kitchen-v1',JSON.stringify(seed));
    localStorage.setItem('chick-kitchen-ui-v1',JSON.stringify({seenSupplyMove:true}));
  },{seed,at});
  page=await context.newPage();errors=[];page.on('pageerror',e=>errors.push(e.message));
  await page.goto(base);
  await page.getByRole('button',{name:'开始游戏',exact:true}).tap({timeout:15000});
  await page.waitForTimeout(900);
  // Dismiss any arrival dialog so the page itself is swept.
  for(let i=0;i<3&&await page.locator('#dialog-layer button').count();i++){await page.locator('#dialog-layer button').last().tap({timeout:3000}).catch(()=>{});await page.waitForTimeout(400);}
  if(pageName!=='厨房'){await page.locator('#main-nav').getByRole('button',{name:pageName,exact:true}).tap({timeout:5000});await page.waitForTimeout(700);}
}
let context,page,errors=[];
for(const seedName of seedNames){
  const seed=JSON.parse(await readFile(SEEDS[seedName],'utf8')).save;
  for(const pageName of PAGES){
    try{await openPage(seed,pageName);}catch(error){report.failures.push({seed:seedName,page:pageName,step:'open',error:error.message.split('\n')[0]});continue;}
    const list=(await targets(page)).filter(t=>!t.inNav);
    for(const [n,t] of list.entries()){
      const entry={seed:seedName,page:pageName,n,label:t.label,control:t.control,covered:t.covered,cover:t.cover};
      try{
        // CHICK_SWEEP_TAP=0 only checks that each button is on screen and not covered.
        if(process.env.CHICK_SWEEP_TAP==='0'){report.results.push(entry);continue;}
        if(n>0)await openPage(seed,pageName);
        const now=(await targets(page)).filter(x=>!x.inNav)[n];
        if(!now||now.label!==t.label){entry.skipped='layout differs after reload';report.results.push(entry);continue;}
        const before=await snapshot(page);
        await page.touchscreen.tap(now.x,now.y);
        await page.waitForTimeout(800);
        entry.changed=(await snapshot(page))!==before;
        entry.errors=[...errors];
      }catch(error){entry.error=error.message.split('\n')[0];}
      report.results.push(entry);
    }
  }
}
await context?.close();await browser.close();server.close();
const dead=report.results.filter(r=>r.changed===false||r.covered||r.errors?.length||r.error);
report.summary={buttons:report.results.length,responded:report.results.filter(r=>r.changed).length,covered:report.results.filter(r=>r.covered).length,withErrors:report.results.filter(r=>r.errors?.length).length,openFailures:report.failures.length};
await writeFile(path.join(out,'report.json'),JSON.stringify(report,null,2));
console.log(report.browser,report.viewport.join('x'),JSON.stringify(report.summary));
for(const r of dead)console.log(' ',r.seed,r.page,`#${r.n}`,JSON.stringify(r.label),r.covered?'COVERED by '+r.cover:'',r.changed===false?'no-response':'',r.errors?.length?'ERR '+r.errors[0]:'',r.error??'');
for(const f of report.failures)console.log(' OPEN FAIL',f.seed,f.page,f.error);
