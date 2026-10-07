// Caller-selected, isolated .deviceqa app only. Never accesses player saves.
// Raw page CDP supports older Android WebViews without browser contexts.
import {execFileSync} from 'node:child_process';
import {mkdir,writeFile} from 'node:fs/promises';
import assert from 'node:assert/strict';
const serial=process.argv[2];assert.ok(serial,'Explicit ADB serial required');
const adb=process.env.CHICK_ADB??'D:/gxy_code/_toolchain/android-sdk/platform-tools/adb.exe';
const run=(...args)=>execFileSync(adb,['-s',serial,...args],{encoding:'utf8',windowsHide:true}).trim();
const pid=run('shell','pidof','com.jibao.kitchen.deviceqa');assert.match(pid,/^\d+$/);
const port=run('forward','tcp:0',`localabstract:webview_devtools_remote_${pid}`);
const out=process.argv[3]??'artifacts/device-qa/webview';await mkdir(out,{recursive:true});
const report={serial,checks:[],errors:[],passed:false};let socket;
try{
 const targets=await (await fetch(`http://127.0.0.1:${port}/json/list`)).json();
 const target=targets.find(t=>t.url.includes('appassets.androidplatform.net')&&t.url.includes('review=1'));
 assert.ok(target,'Only isolated review mode is permitted');
 socket=new WebSocket(target.webSocketDebuggerUrl);
 await new Promise((ok,no)=>{socket.addEventListener('open',ok,{once:true});socket.addEventListener('error',no,{once:true});});
 let seq=0;const pending=new Map();
 socket.addEventListener('message',event=>{
  const data=JSON.parse(event.data);
  if(data.id){const call=pending.get(data.id);if(call){pending.delete(data.id);clearTimeout(call.timer);data.error?call.no(Error(call.method+': '+data.error.message)):call.ok(data.result);}}
  else if(data.method==='Runtime.exceptionThrown')report.errors.push(data.params.exceptionDetails.exception?.description??data.params.exceptionDetails.text);
 });
 const send=(method,params={})=>new Promise((ok,no)=>{const id=++seq,timer=setTimeout(()=>{pending.delete(id);no(Error('CDP timeout: '+method));},15000);pending.set(id,{ok,no,timer,method});socket.send(JSON.stringify({id,method,params}));});
 const evaluate=async fn=>{const result=await send('Runtime.evaluate',{expression:`(${fn})()`,returnByValue:true,awaitPromise:true});if(result.exceptionDetails)throw Error(result.exceptionDetails.exception?.description??result.exceptionDetails.text);return result.result.value;};
 const pause=ms=>new Promise(ok=>setTimeout(ok,ms));
 const read=()=>evaluate(()=>JSON.parse(localStorage.getItem('chick-kitchen-review-v1')));
 const shot=async name=>{const pixels=execFileSync(adb,['-s',serial,'exec-out','screencap','-p'],{windowsHide:true});await writeFile(out+'/'+name+'.png',pixels);};
 const reset=async()=>{
  // Build a valid save through the real engine rather than a visual review
  // fixture; native QA must also exercise transactional save validation.
  const seed=await evaluate(async()=>{const E=await import('/web/engine.js'),at=Date.now(),s=E.freshState(at,31);s.sound=false;s.music=false;E.startBatch(s,0,at-120000,()=>.5);s.batch.eggs.forEach(e=>{e.status='ready';e.openAt=at-10000;e.animationAt=at-3000;e.blackAt=null;});E.parseSave(JSON.stringify(s),at);return s;});
  // Seed after the old document's pagehide save and before the new app loads.
  const hook=await send('Page.addScriptToEvaluateOnNewDocument',{source:`localStorage.setItem('chick-kitchen-review-v1',${JSON.stringify(JSON.stringify(seed))});`});
  await send('Page.reload',{ignoreCache:true});
  await pause(1500);
  for(let i=0;i<100;i++){if(await evaluate(()=>!!document.querySelector('[data-control-id="start"]')))break;if(i===99)throw Error('Seeded QA cover unavailable');await pause(200);}
  await evaluate(()=>document.querySelector('[data-control-id="start"]').click());await pause(700);
  await send('Page.removeScriptToEvaluateOnNewDocument',{identifier:hook.identifier});
 };
 const points=()=>evaluate(()=>{
  const root=document.querySelector('#game'),stage=document.querySelector('#scene-stage'),r=root.getBoundingClientRect(),style=getComputedStyle(stage);
  const scale=style.transform==='none'?parseFloat(style.zoom)||1:Number(style.transform.match(/^matrix\(([^,]+)/)?.[1])||1;
  const top=r.y+(document.querySelector('#panels').clientHeight-parseFloat(stage.style.height)*scale)/2,left=r.x+(root.clientWidth-320*scale)/2;
  return [...document.querySelectorAll('[data-control-id^="egg:"]')].map(e=>({x:left+(parseFloat(e.style.left)+parseFloat(e.style.width)/2)*scale,y:top+(parseFloat(e.style.top)+parseFloat(e.style.height)/2)*scale}));
 });
 const nativePoint=async point=>{
  run('shell','uiautomator','dump','/data/local/tmp/jibao-deviceqa.xml');
  const xml=run('shell','cat','/data/local/tmp/jibao-deviceqa.xml'),node=xml.match(/<node[^>]*class="android.webkit.WebView"[^>]*>/)?.[0];
  assert.ok(node&&node.includes('com.jibao.kitchen.deviceqa'),'QA WebView must be foreground');
  const bounds=node.match(/bounds="\[(\d+),(\d+)\]\[(\d+),(\d+)\]"/);assert.ok(bounds,'WebView screen bounds required');
  const dpr=await evaluate(()=>devicePixelRatio);
  return {x:Math.round(Number(bounds[1])+point.x*dpr),y:Math.round(Number(bounds[2])+point.y*dpr),dpr,offset:[Number(bounds[1]),Number(bounds[2])]};
 };
 await send('Runtime.enable');await send('Page.enable');
 await reset();
 report.geometry=await evaluate(()=>({ua:navigator.userAgent,inner:[innerWidth,innerHeight],dpr:devicePixelRatio,zoom:getComputedStyle(document.querySelector('#scene-stage')).zoom,transform:getComputedStyle(document.querySelector('#scene-stage')).transform,boxes:['#game','#scene-stage','#scene','#controls'].map(id=>{const e=document.querySelector(id),r=e.getBoundingClientRect();return {id,x:r.x,y:r.y,width:r.width,height:r.height,client:[e.clientWidth,e.clientHeight]};})}));
 await shot('ready');
 const before=await read(),point=(await points()).at(-1);
 const physical=await nativePoint(point);report.input='Android input tap/swipe';
 run('shell','input','tap',String(physical.x),String(physical.y));await pause(1000);
 const after=await read();report.single={before:before.cp,after:after.cp,collected:after.batch.eggs.filter(e=>e.collected).length,point};await shot('single');
 await reset();
 const rows=(await points()).sort((a,b)=>a.y-b.y),strokes=[];
 for(let i=0;i<rows.length;i+=6){const row=rows.slice(i,i+6).sort((a,b)=>a.x-b.x),y=row.reduce((sum,p)=>sum+p.y,0)/row.length;strokes.push([row[0].x,y,row.at(-1).x,y]);}
 // Separate physical finger sweeps also exercise release/capture boundaries.
 for(let pass=0;pass<2;pass++)for(const stroke of strokes){const p=stroke.map((n,i)=>String(Math.round(n*physical.dpr+physical.offset[i%2])));run('shell','input','swipe',...p,'250');}
 await pause(1000);
 const swiped=await read();report.swipe={collected:swiped.batch.eggs.filter(e=>e.collected).length,cp:swiped.cp};await shot('swipe');
 assert.equal(report.single.collected,1,'visible front chick touch');assert.equal(report.swipe.collected,24,'all 24 chicks on a touch sweep');assert.equal(report.swipe.cp-before.cp,24);assert.deepEqual(report.errors,[]);report.passed=true;
}catch(error){report.errors.push(error.message);process.exitCode=1;}
finally{socket?.close();run('forward','--remove',`tcp:${port}`);await writeFile(out+'/report.json',JSON.stringify(report,null,2));console.log(JSON.stringify(report,null,2));}
