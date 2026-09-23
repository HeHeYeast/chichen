// Checks only the independent design artifact; never opens the normal game.
import {createRequire} from 'node:module';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
const require=createRequire(import.meta.url);
const {chromium}=require(process.env.CHICK_PLAYWRIGHT_PACKAGE||process.env.APPDATA+'/npm/node_modules/gsd-pi/node_modules/playwright-core');
const base=process.env.UI_DESIGN_URL||'http://127.0.0.1:4175/docs/ui-architecture/';
const browser=await chromium.launch({headless:true,executablePath:process.env.CHICK_QA_CHROME||'C:/Program Files/Google/Chrome/Application/chrome.exe'});
const out=new URL('./evidence/',import.meta.url);await mkdir(out,{recursive:true});
const errors=[],checks=[],screenshots=[],failures=[];let completed=false;
const c=await browser.newContext({reducedMotion:'reduce'}),p=await c.newPage();p.on('pageerror',e=>errors.push(e.message));
p.on('response',r=>{if(r.status()>=400)errors.push(`${r.status()} ${r.url()}`);});
async function shot(name){await p.screenshot({path:new URL(name+'.png',out).pathname.replace(/^\/([A-Z]:)/,'$1')});screenshots.push(name+'.png');}
async function click(sel){await p.locator(sel).filter({visible:true}).first().click();}
async function open(page){await p.goto(base+'prototype.html?page='+page);await p.locator('h1').waitFor();}
async function scenario(id){await click('[data-act="demo"]');await click('[data-act="scenario-'+id+'"]');}
async function time(id){await click('[data-act="demo"]');await click('[data-act="time-'+id+'"]');}
try{
 const routes=['kitchen','harvest','sale','business','business-check','running','bill','orders','order','customers','customer','projects','project','journey','team','trip-check','away','returned','discovery','region','collection','species-list','species','themes','theme','reward','seen','recipe','cook','cooking','farm','inventory','shop','craft','display','shrine','calendar','settings'];
 for(const [w,h]of [[320,568],[375,667],[390,844],[430,932],[844,390],[1280,900]]){
  await p.setViewportSize({width:w,height:h});
  for(const route of routes){await open(route);const fit=await p.evaluate(()=>{
   const shell=document.querySelector('.shell'),content=document.querySelector('.content'),commit=document.querySelector('.commit'),nav=document.querySelector('.nav');
   const r=e=>{const b=e.getBoundingClientRect();return {x:b.x,y:b.y,right:b.right,bottom:b.bottom};};
   return {bodyWidth:document.documentElement.scrollWidth,innerWidth,shell:r(shell),content:r(content),commit:r(commit),nav:r(nav),contentOverflow:content.scrollWidth-content.clientWidth,buttons:[...document.querySelectorAll('.shell button')].filter(e=>e.getBoundingClientRect().width>0).map(e=>({name:e.textContent.trim(),h:e.getBoundingClientRect().height,w:e.getBoundingClientRect().width}))};
  });
  if(fit.contentOverflow>1||fit.bodyWidth>w||fit.shell.bottom>h+1||fit.commit.bottom>fit.nav.y+1&&w<1024||fit.buttons.some(b=>b.h<43.9))failures.push({viewport:[w,h],route,fit});
  }
  checks.push({viewport:`${w}×${h}`,routes:routes.length,checks:'horizontal fit, shell bounds, footer/nav separation, 44px button height'});
 }
 await p.setViewportSize({width:320,height:568});
 for(const route of ['kitchen','business','bill','orders','journey','returned','region','theme','project']){await open(route);await shot('proposed-'+route+'-320');}
 await p.setViewportSize({width:1280,height:900});await open('business');await shot('proposed-business-desktop');await open('journey');await shot('proposed-journey-desktop');
 await p.setViewportSize({width:390,height:844});
 await open('harvest');await click('[data-act="keep"]');assert.match(await p.locator('.notice').innerText(),/留在家里/);
 await scenario('a');await click('[data-go="sale"]');await click('[data-act="sell"]');assert.equal(await p.locator('h1').innerText(),'已经售出');assert.match(await p.locator('.balance').innerText(),/1,340/);checks.push('A: leave stock and immediate sale branch');
 await scenario('a');await click('[data-go="business"]');await click('.commit [data-go="business-check"]');await click('[data-act="open-business"]');assert.equal(await p.locator('h1').innerText(),'正在营业');checks.push('A: batch to business draft and submit');
 await scenario('b');await click('summary');await p.locator('#bonus').check();await click('.commit [data-go="business-check"]');await click('[data-act="open-business"]');await time('business');assert.match(await p.locator('.balance').innerText(),/1,355/);assert.match(await p.locator('.receipt').innerText(),/已到账/);await click('[data-nav="kitchen"]');await click('[data-nav="business"]');assert.match(await p.locator('.balance').innerText(),/1,355/);checks.push('B: 95 CP business receipt, repeated view gives no second payout');
 await scenario('c');await click('[data-act="last-team"]');await click('.commit [data-go="trip-check"]');await click('[data-act="depart"]');await time('trip');await click('[data-act="identify"]');await click('[data-act="prepare"]');await click('.commit [data-go="cook"]');await click('[data-act="start-cook"]');await time('new');assert.match(await p.locator('h1').innerText(),/荠菜煎饼鸡/);checks.push('C: depart, return, identify, prepare recipe, cook and simulated real collection');
 await click('[data-go="theme"]');await click('[data-go="reward"]');assert.match(await p.locator('h1').innerText(),/这页有了自己的味道/);await shot('proposed-reward-390');checks.push('D: new species → theme → recorded milestone result');
 await scenario('full');await click('[data-act="identify"]');assert.match(await p.locator('.notice').innerText(),/没有额外发放/);checks.push('Full bag still allows specimen identification');
 await scenario('empty');assert.equal(await p.locator('.commit button').innerText(),'回厨房准备');checks.push('Empty inventory routes to preparation');
 await scenario('fail');await click('[data-act="open-business"]');assert.match(await p.locator('.notice').innerText(),/保存失败/);assert.match(await p.locator('.balance').innerText(),/1,260/);checks.push('Simulated save failure preserves funds and draft');
 await scenario('orders');await click('[data-go="order"]');await click('[data-act="deliver"]');assert.match(await p.locator('.notice').innerText(),/10\/12/);await click('[data-act="deliver"]');assert.match(await p.locator('.notice').innerText(),/没有重复到账/);checks.push('Partial order delivery displays progress and does not pay twice');
 await open('project');await click('[data-act="print"]');assert.match(await p.locator('.balance').innerText(),/1,060/);await click('[data-go="display"]');await click('[data-act="display-1"]');assert.match(await p.locator('.notice').innerText(),/已陈列/);checks.push('Project payment → permanent result → display');
 await open('business');await p.locator('[data-act="demo"]').focus();await p.keyboard.press('Enter');assert.ok(await p.locator('dialog').isVisible());await p.keyboard.press('Escape');assert.ok(!await p.locator('dialog').isVisible());assert.equal(await p.evaluate(()=>document.activeElement?.dataset.act),'demo');checks.push('Keyboard dialog Enter/Escape and focus restoration');
 assert.equal(await p.evaluate(()=>localStorage.length+sessionStorage.length),0);checks.push('Prototype leaves localStorage and sessionStorage empty');
 await p.setViewportSize({width:320,height:568});await open('business');await p.addStyleTag({content:'.content{font-size:18px}.content h1{font-size:28px}.content h2{font-size:22px}'});assert.ok(await p.locator('.content').evaluate(e=>e.scrollWidth<=e.clientWidth));await shot('proposed-business-large-text-320');checks.push('320px larger body text sample: no horizontal overflow; not OS font or screen-reader certification');
 for(const [w,h]of [[320,568],[1280,900]]){await p.setViewportSize({width:w,height:h});await p.goto(base+'index.html');if(await p.evaluate(()=>document.documentElement.scrollWidth>innerWidth))console.log(await p.evaluate(()=>[...document.querySelectorAll('body *')].filter(e=>e.getBoundingClientRect().right>innerWidth+1).map(e=>({tag:e.tagName,cls:e.className,right:e.getBoundingClientRect().right,width:e.getBoundingClientRect().width})).slice(0,25)));assert.ok(await p.evaluate(()=>document.documentElement.scrollWidth<=innerWidth));assert.deepEqual(await p.locator('img').evaluateAll(els=>els.filter(e=>!e.complete||e.naturalWidth===0).map(e=>e.src)),[]);}
 await shot('design-board-desktop');checks.push('Design board at 320px and desktop, all embedded screenshots loaded');
 assert.deepEqual(errors,[]);assert.deepEqual(failures,[]);completed=true;
 console.log(`Design prototype verified: ${routes.length*6} page/viewport combinations, ${checks.length-6} interaction/edge checks, ${screenshots.length} screenshots, no browser errors.`);
}catch(error){errors.push(error.message);throw error;}finally{await writeFile(new URL('./verification.json',import.meta.url),JSON.stringify({checkedAt:new Date().toISOString(),scope:'Independent docs design prototype only; no game regression, native device test or user acceptance',checks,screenshots,errors,failures,passed:completed&&errors.length===0&&failures.length===0},null,2));await browser.close();}
