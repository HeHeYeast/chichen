import {createRequire} from 'node:module';
import {resolve} from 'node:path';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
const {chromium}=createRequire(import.meta.url)(resolve(process.argv[2]));
const out=resolve('artifacts/qa/holiday-reminders-v141');await mkdir(out,{recursive:true});
const browser=await chromium.launch({headless:true}),errors=[],checks=[];
const day=new Date(2026,8,14,12).getTime();
function seed(){const s=E.freshState(day);s.cp=9000;s.alarm=true;s.total={'0:49':1,'0:60':1};s.farm={'0:49':1,'0:60':1};for(const id of [48,49,60])s.events[`campaign_char_0_${id}`]=true;return s;}
async function fits(p){const result=await p.locator('.screen-panel').evaluate(e=>({overflow:e.scrollWidth-e.clientWidth,bottom:e.querySelector('footer').getBoundingClientRect().bottom,height:innerHeight}));assert.ok(result.overflow<=1&&result.bottom<=result.height,result);}
try{
  for(const width of [320,375,430]){
    const c=await browser.newContext({viewport:{width,height:width===320?568:width===375?747:932},timezoneId:'Asia/Shanghai'});
    await c.addInitScript(({s,now})=>{const NativeDate=Date;window.Date=class extends NativeDate{constructor(...args){super(...(args.length?args:[now]));}static now(){return now;}};localStorage.setItem('chick-kitchen-review-v1',JSON.stringify(s));},{s:seed(),now:day});
    const p=await c.newPage();p.on('pageerror',e=>errors.push(e.message));await p.goto('http://127.0.0.1:4173/web/index.html?review=1');await p.getByRole('button',{name:'开始游戏',exact:true}).click();
    await p.getByRole('button',{name:'图鉴',exact:true}).click();await p.locator('[data-book-tab="species"]').click();await p.locator('[data-collection-recipes]').click();await p.locator('[data-cookbook-recipe="0:49"]').click();
    assert.match(await p.locator('.cookbook-conditions').innerText(),/愚人节期间开火.*2027年4月1日/);assert.equal(await p.locator('[data-cookbook-prepare]').innerText(),'查看活动日期');await fits(p);await p.screenshot({path:resolve(out,`fools-date-${width}.png`)});
    await p.locator('[data-cookbook-prepare]').click();assert.equal(await p.locator('[data-holiday]').count(),17);assert.ok(await p.locator('[data-holiday="chestnut"]').evaluate(e=>e.classList.contains('is-open')));
    await p.locator('[data-holiday="mothers-day"]').scrollIntoViewIfNeeded();assert.match(await p.locator('[data-holiday="mothers-day"]').innerText(),/2027年5月9日/);assert.doesNotMatch(await p.locator('.journal-scroll').innerText(),/不必等现实节日|旧节日伙伴 · 全年可得/);await fits(p);await p.screenshot({path:resolve(out,`calendar-${width}.png`)});
    await p.locator('[data-holiday="mothers-day"] [data-journal-activity]').click();assert.match(await p.locator('.activity-detail').innerText(),/仍需对应日期/);assert.match(await p.locator('.activity-detail').innerText(),/C50.*愚人节/);await fits(p);await p.screenshot({path:resolve(out,`spring-letter-${width}.png`)});
    const saved=await p.evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-review-v1')));assert.deepEqual(saved.farm,seed().farm);assert.deepEqual(saved.total,seed().total);assert.equal(saved.cp,9000);
    await c.close();
  }
  checks.push('320/375/430 宽：旧收藏可查看配方但非节日只可查看日期；日历、来信统一显示日期，页脚可见，无横向溢出');
  const c=await browser.newContext({viewport:{width:375,height:747}});
  await c.addInitScript(s=>{window.__reminderQA={raw:JSON.stringify(s),probe:0,help:0};window.ChickNative={platformInfo:()=>JSON.stringify({android:true,version:'1.4.1-test'}),loadSave:()=>JSON.stringify({status:'ok',raw:window.__reminderQA.raw}),saveGame:raw=>{window.__reminderQA.raw=raw;return '{"ok":true}';},ready(){},notificationStatus:()=>JSON.stringify({notificationsEnabled:true,exactAllowed:true,message:'预计 09月14日 12:15:00 提醒（按本批实际孵化时间）。',testMessage:window.__reminderQA.probe?'后台测试已安排：12:01:00，可以返回桌面等待。':''}),testDelayedNotification:()=>{window.__reminderQA.probe++;return JSON.stringify({ok:true,message:'已安排 1 分钟后的后台测试。'});},openAppSettings(){window.__reminderQA.help++;}};},seed());
  const p=await c.newPage();p.on('pageerror',e=>errors.push(e.message));await p.goto('http://127.0.0.1:4173/');await p.getByRole('button',{name:'设置',exact:true}).click();
  await p.locator('[data-test-delayed]').click();assert.equal(await p.evaluate(()=>window.__reminderQA.probe),1);await p.locator('[data-yes]').click();await fits(p);await p.screenshot({path:resolve(out,'settings.png')});
  await p.locator('[data-background-help]').click();assert.match(await p.locator('.settings-content').innerText(),/应用启动管理/);await p.locator('[data-app-settings]').click();assert.equal(await p.evaluate(()=>window.__reminderQA.help),1);await fits(p);await p.screenshot({path:resolve(out,'background-help.png')});await p.locator('[data-help-back]').click();assert.match(await p.locator('[data-notification-status]').innerText(),/后台测试已安排/);await c.close();
  checks.push('模拟 Android：预计时间、后台测试与帮助入口可操作；系统设置返回入口保留，未修改游戏库存');
  assert.deepEqual(errors,[]);await writeFile(resolve(out,'browser-report.json'),JSON.stringify({at:new Date().toISOString(),checks,errors,actualDevice:false},null,2));console.log(JSON.stringify({checks,errors}));
}finally{await browser.close();}
