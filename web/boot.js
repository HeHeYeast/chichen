// Keep this entry readable by older Android WebViews so unsupported runtimes
// show recovery guidance before importing code that can touch player saves.
// Script errors are kept (outside the save) for the settings device check, so a
// button that fails on one phone can be reported without a cable.
var DIAG_KEY='chick-kitchen-diag-v1';
var diag=window.__chickDiag={errors:[],touch:{aligned:0,offset:0,rescued:0}};
try{var kept=JSON.parse(localStorage.getItem(DIAG_KEY)||'[]');if(kept&&kept.length)diag.errors=kept.slice(-10);}catch(ignored){}
function recordError(message,where){
  message=String(message||'未知错误').slice(0,240);
  var last=diag.errors[diag.errors.length-1];
  if(last&&last.message===message){last.count=(last.count||1)+1;last.at=Date.now();}
  else diag.errors.push({at:Date.now(),message:message,where:String(where||'').slice(0,160)});
  diag.errors=diag.errors.slice(-10);
  try{localStorage.setItem(DIAG_KEY,JSON.stringify(diag.errors));}catch(ignored){}
}
diag.record=recordError;
window.addEventListener('error',function(event){
  if(!event.error&&event.target&&event.target!==window)return;
  recordError(event.message||(event.error&&event.error.message),(event.filename||'').replace(/^.*\/web\//,'')+':'+(event.lineno||0));
});
window.addEventListener('unhandledrejection',function(event){var r=event.reason;recordError(r&&r.message||r,'promise');});
function engineVersion(){var m=/(?:Chrome|Chromium)\/(\d+)/.exec(navigator.userAgent||'');return m?+m[1]:0;}
function showStartupError(unsupported) {
  var panel=document.createElement('section');
  panel.setAttribute('role','alert');
  panel.style.cssText='position:fixed;inset:0;z-index:9999;background:#fff4d9;color:#523c26;padding:12vh 8vw;box-sizing:border-box;font:18px/1.7 sans-serif;overflow:auto';
  var heading=document.createElement('h1');heading.textContent=unsupported?'请更新系统网页组件':'游戏暂时无法打开';
  var message=document.createElement('p');message.textContent=unsupported?'请在手机的应用市场或系统更新里更新 WebView（系统网页组件）或 Chrome，再重新打开游戏。'+(engineVersion()?'这台手机目前是 '+engineVersion()+' 版，需要 105 版或更新。':''):'请关闭后重新打开游戏；如果仍无法进入，请重新安装同版本或更新版本的安装包。';
  var note=document.createElement('p');note.textContent='请勿卸载游戏或清除应用数据，以免丢失存档。';
  var retry=document.createElement('button');retry.textContent='重新打开';retry.style.cssText='font:inherit;padding:12px 24px;min-height:48px';retry.onclick=function(){location.reload();};
  panel.appendChild(heading);panel.appendChild(message);panel.appendChild(note);panel.appendChild(retry);document.body.appendChild(panel);
}
// Test features used after startup too. A partially supported WebView otherwise
// opens the game and only fails on its first Canvas frame, modal or CSS layout.
var canvasSupport=typeof CanvasRenderingContext2D!=='undefined'&&typeof CanvasRenderingContext2D.prototype.roundRect==='function';
var dialogSupport=typeof HTMLDialogElement!=='undefined'&&typeof HTMLDialogElement.prototype.showModal==='function';
var layoutSupport=typeof CSS!=='undefined'&&typeof CSS.supports==='function'&&CSS.supports('selector(:has(*))');
var inputSupport=typeof PointerEvent!=='undefined'&&'inert' in HTMLElement.prototype&&typeof Element.prototype.setPointerCapture==='function';
if(typeof structuredClone!=='function'||typeof Object.hasOwn!=='function'||typeof Array.prototype.at!=='function'||!canvasSupport||!dialogSupport||!layoutSupport||!inputSupport) {
  showStartupError(true);
} else {
  import('./app.js').catch(function(error){console.error('Game startup failed',error);recordError(error&&error.message||error,'startup');showStartupError(false);});
}
