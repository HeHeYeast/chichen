// Settings → 设备检测: what this phone's WebView can do and whether taps land
// where they are drawn. Players can copy the result and send it back, because
// phone-specific failures cannot be reproduced without the phone itself.
const MIN_ENGINE=105;
const engine=()=>Number(/(?:Chrome|Chromium)\/(\d+)/.exec(navigator.userAgent)?.[1]??0);
const supports=test=>{try{return !!test();}catch{return false;}};
const CAPABILITIES=[
  ['画图',()=>typeof CanvasRenderingContext2D.prototype.roundRect==='function'],
  ['弹窗',()=>typeof HTMLDialogElement.prototype.showModal==='function'],
  ['排版',()=>CSS.supports('selector(:has(*))')&&CSS.supports('container-type:inline-size')],
  ['触摸',()=>typeof PointerEvent!=='undefined'&&typeof Element.prototype.setPointerCapture==='function'&&'inert' in HTMLElement.prototype],
  ['存档读写',()=>{localStorage.setItem('chick-kitchen-probe','1');localStorage.removeItem('chick-kitchen-probe');return true;}],
];
// The kitchen is drawn on a scaled canvas. Its on-screen box must match the
// scale the game applied, otherwise taps land beside what is drawn.
function sceneAlignment(){
  const stage=document.querySelector('#scene-stage'),canvas=document.querySelector('#scene');
  if(!stage||!canvas||!canvas.offsetWidth)return null;
  const matrix=/matrix\(([^,]+)/.exec(getComputedStyle(stage).transform);
  const scale=matrix?Number(matrix[1]):1,expected=canvas.offsetWidth*scale,actual=canvas.getBoundingClientRect().width;
  return expected>0?actual/expected:null;
}
const time=at=>{const d=new Date(at);return `${d.getMonth()+1}/${d.getDate()} ${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}`;};
export function deviceCheck(platform){
  const info=platform.info??{},diag=window.__chickDiag??{errors:[],touch:{}},rows=[];
  const add=(label,value,ok=true)=>rows.push({label,value,ok});
  add('游戏版本',`${info.android?'Android':'浏览器'} ${info.version??''}`);
  if(info.android&&info.release)add('手机',`${[info.maker,info.model].filter(Boolean).join(' ')} · Android ${info.release}`);
  const version=engine();
  add('网页组件',version?`${version} 版${info.webview?`（${info.webview}）`:''}`:'未能识别',version>=MIN_ENGINE);
  if(version&&version<MIN_ENGINE)add('建议','请在应用市场或系统更新里更新 WebView',false);
  add('屏幕',`${innerWidth}×${innerHeight}，像素比 ${Math.round((devicePixelRatio||1)*100)/100}`);
  if(info.fontScale)add('系统字号',`${Math.round(info.fontScale*100)}%${info.fontScale!==1?'（游戏内已固定为标准字号）':''}`);
  const missing=CAPABILITIES.filter(([,test])=>!supports(test)).map(([name])=>name);
  add('功能支持',missing.length?`缺少：${missing.join('、')}`:'全部可用',!missing.length);
  const ratio=sceneAlignment();
  if(ratio!==null)add('画面对齐',Math.abs(ratio-1)<.02?'正常':`偏差 ${Math.round((ratio-1)*100)}%`,Math.abs(ratio-1)<.02);
  const touch=diag.touch??{},taps=(touch.aligned??0)+(touch.offset??0);
  add('厨房点按',taps?`${taps} 次中 ${touch.offset??0} 次偏移${touch.rescued?`，已自动纠正 ${touch.rescued} 次`:''}`:'还没有点过厨房按钮',!(touch.offset>0));
  const errors=diag.errors??[];
  add('出错记录',errors.length?`${errors.length} 条`:'没有',!errors.length);
  return {rows,errors,ok:rows.every(r=>r.ok)};
}
export function deviceCheckText(result){
  return ['鸡宝厨房 设备检测',...result.rows.map(r=>`${r.ok?'✓':'×'} ${r.label}：${r.value}`),
    ...result.errors.map(e=>`! ${time(e.at)} ${e.message}${e.where?` @${e.where}`:''}${e.count>1?` ×${e.count}`:''}`),
    `UA: ${navigator.userAgent}`].join('\n');
}
export function clearDeviceErrors(){
  const diag=window.__chickDiag;if(diag)diag.errors=[];
  try{localStorage.removeItem('chick-kitchen-diag-v1');}catch{}
}
