import * as E from '/web/engine.js';
import {resolveSprite,spriteSVG,uiIcon} from '/web/art/manifest.js';
import {interfaceIcon} from '/web/ui-icons.js';
import {toolImage} from '/web/catalog.js';

// Isolated layout laboratory: use existing gameplay rules, never import app.js,
// read a player save, write localStorage, or mount into the production kitchen.
const $=s=>document.querySelector(s);
const params=new URLSearchParams(location.search);
let layout=['A','B','C'].includes(params.get('layout'))?params.get('layout'):'A';
const single=params.get('view')==='phone';
document.body.classList.toggle('phone-only',single);
const layouts={
 A:{title:'A · 围绕蛋窝',description:'先看这一窝，再决定下一批。蛋窝占据中央，厨具始终在底部近手处。',points:['调味、打扫紧接 HUD，状态一眼可见。','蛋窝 → 孵化状态 → 小入口 → 厨具，形成纵向阅读顺序。','测试：中央蛋窝是否足够突出，底部厨具是否顺手。']},
 B:{title:'B · 上备下收',description:'上半区安排下一批，下半区照料这一窝。把蛋窝放到更容易触达的位置。',points:['厨具紧接调味区，准备操作聚在上方。','三个次级入口留在两区之间，孵化状态贴着蛋窝上沿。','测试：下方点蛋更顺手，是否值得让选厨具离拇指远一点。']},
 C:{title:'C · 侧边取用',description:'左侧取厨具，右侧看蛋窝。用并列区域代替从上到下的一长串。',points:['厨具纵向排列，选中态与蛋窝同时可见。','状态位于蛋窝上方，商店／仓库／手艺位于其下。','测试：横向分区是否更清楚，以及蛋窝变窄的代价。']}
};
// An organic clutch shared by all layouts, independent of the engine's old rows.
const eggPositions=[[62,91,-11],[102,83,5],[145,81,-4],[187,88,8],[227,82,-7],[270,99,12],[47,130,-14],[89,125,9],[132,118,-6],[174,128,10],[216,120,-9],[255,135,6],[302,140,-10],[63,169,8],[104,161,-12],[147,162,7],[194,169,-6],[234,161,10],[279,178,-8],[89,200,-8],[132,204,6],[174,200,-4],[219,203,9],[258,212,-5]];
let state,nextTool=0,toastTimer,lastFocus=null,dragging=false,collectedDuringDrag=new Set();
const esc=s=>String(s).replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const sprite=p=>{const s=resolveSprite(p);return s.frame?spriteSVG({...s,size:s.size??[1312,1199]}):`<img src="${p}" alt="">`;};
const bed=part=>{const s=resolveSprite('/web/art/stage-bed-1-v6.png#'+part);return `<svg class="bed-art" viewBox="${s.frame.join(' ')}" preserveAspectRatio="none" aria-hidden="true" overflow="hidden"><image href="${s.file}" width="${s.size[0]}" height="${s.size[1]}"/></svg>`;};
const toolArt=id=>sprite(toolImage(1,id,0));
const leftEggs=()=>state.batch?.eggs.filter(e=>!e.collected)??[];
const readyEgg=e=>['ready','hatching'].includes(e.status);
const duration=ms=>{const n=Math.max(0,Math.ceil(ms/1000));return `${Math.floor(n/3600).toString().padStart(2,'0')}:${Math.floor(n/60%60).toString().padStart(2,'0')}:${(n%60).toString().padStart(2,'0')}`;};

function reset(mode='running'){
 const now=Date.now();state=E.freshState(now,2718);state.progress.tutorialSeen=true;state.kitchenLevel=1;state.cp=1280;state.toolLevels=[0,0,0,0,-1,-1,-1,-1,-1];state.ingredients={0:5,1:5,2:5};nextTool=0;
 if(mode!=='empty'){
  E.startBatch(state,0,now,()=>.5,()=>.5);
  // Fixture contents deliberately use only the known basic chick. Rules remain imported.
  for(const e of state.batch.eggs){e.id=0;e.egg=0;e.status=mode==='ready'?'ready':'egg';if(mode==='ready'){e.openAt=now-1000;e.blackAt=now+86400000;}else e.openAt=now+115*60000+17000;}
  if(mode==='ready'){state.batch.started=now-7200000;state.batch.ends=now-1000;}
 }
 if(mode==='dirty'){state.lastClean=now-36*3600000;state.cleanCycle.dirtyAt=now-1000;state.dirty=true;}
 closeSheet();render();
}

function render(){
 const remaining=leftEggs(),clean=E.kitchenCleanInfo(state),ready=remaining.filter(readyEgg).length;
 const eggs=(state.batch?.eggs??[]).map((e,i)=>{if(e.collected)return '';const [x,y,tilt]=eggPositions[i];return `<button class="egg ${readyEgg(e)?'ready':''}" data-egg="${i}" aria-label="${readyEgg(e)?'收取鸡宝':'查看孵化中的蛋'} ${i+1}" style="left:${x/352*100}%;top:${y/276*100}%;--tilt:${tilt}deg;--z:${i+2}">${sprite(readyEgg(e)?'/web/art/chick-v4-0.png':'/web/art/egg-v4.png')}${readyEgg(e)?'<i class="ready-dot"></i>':''}</button>`;}).join('');
 const tools=[0,1,2,3].map(id=>{const info=E.cookInfo(state,id);return `<button class="tool ${nextTool===id?'is-selected':''}" data-tool="${id}" aria-label="选择下批厨具 ${esc(E.label(E.tool(id)))}" aria-pressed="${nextTool===id}"><span class="tool-art">${toolArt(id)}</span><strong>${esc(E.label(E.tool(id)))}</strong><small>${Math.round(info.minutes)}分 · ${info.cost} CP</small></button>`;}).join('');
 $('#game-content').innerHTML=`<div class="wood-identity" aria-hidden="true"></div>
 <header class="hud"><button class="profile" data-action="level" aria-label="玩家等级 8，厨房等级 2">${sprite('/web/art/chick-v4-0.png')}<strong>Lv.8</strong></button><div class="wallet" aria-label="CP ${state.cp}"><img src="/web/art/golden-business/coin.png" alt=""><b id="cp">${state.cp.toLocaleString('en-US')}</b><small>CP</small></div><button class="settings" data-action="settings" aria-label="设置">${interfaceIcon('settings')}</button></header>
 <div class="care"><button class="seasoning" data-action="seasoning" aria-label="选择调味料"><span><img src="/assets/png/Tool/Tool2/tool_2_0_0_0.png" alt=""></span><span><strong>调味</strong><small id="seasoning-label">${state.selected.length?`下批已选 ${state.selected.length} 种`:'下批 · 未选调味'}</small></span></button><button class="clean" data-action="clean" aria-label="打扫，脏污 ${clean.percent}%"><span>${interfaceIcon('clean')}</span><span><strong>打扫</strong><small>脏污 ${clean.percent}%</small></span></button></div>
 <div class="room-label">Lv.2 木房</div><button class="help" data-action="help" aria-label="帮助">?</button>
 <div class="nest" aria-label="自然堆叠的蛋窝"><div class="nest-back">${bed('back')}</div><div class="nest-cushion">${bed('cushion')}</div>${eggs}<div class="nest-front">${bed('front')}</div>${!remaining.length?`<div class="empty-nest"><strong>蛋窝空了</strong><p>准备好，就开始下一批吧</p><button class="start-batch" data-action="start">开始孵化 · 24 枚</button></div>`:''}</div>
 <section class="status" aria-label="孵化状态"><div class="status-main"><strong id="batch-title">${!remaining.length?'准备下一批':ready?`可收取 ${ready} / ${remaining.length}`:`孵化中 · ${remaining.length} 枚`}</strong><span id="current-tool">${remaining.length?'本批':'下批'} · ${esc(E.label(E.tool(remaining.length?state.batch.tool:nextTool)))}</span></div><div class="timer-row"><button class="alarm" data-action="alarm" aria-label="孵化提醒 ${state.alarm?'开启':'关闭'}" aria-pressed="${state.alarm}">◷</button><div class="progress" style="--progress:0%"><span id="timer"></span></div></div></section>
 <div class="egg-tip">${!remaining.length?'选好厨具和调味，再开始':ready?'轻划鸡宝收取 · 也可以逐只点按':'轻点蛋，查看这一批'}</div>
 <nav class="portals" aria-label="厨房次级入口"><button data-action="shop"><span class="portal-art">${interfaceIcon('shop')}</span><span>商店</span></button><button data-action="inventory"><span class="portal-art">${interfaceIcon('inventory')}</span><span>仓库</span></button><button data-action="skill"><img class="portal-art skill-book" src="/web/art/golden-business/regulars-original.png" alt=""><span>手艺</span></button></nav>
 <section class="tools" aria-label="厨具栏"><header class="tools-heading"><b>下批厨具</b><span>轻点换用</span><button class="tool-more" data-action="tools" aria-label="查看全部厨具">全部 ›</button></header><div class="tools-list">${tools}</div></section>
 <nav class="bottom-nav" aria-label="主导航">${[['kitchen','厨房'],['farm','农场'],['shop','生意'],['explore','寻访'],['book','图鉴']].map(([icon,name],i)=>`<button data-nav="${name}" ${i===0?'aria-current="page"':''}><span class="nav-art">${interfaceIcon(icon)}</span><span>${name}</span></button>`).join('')}</nav>`;
 updateClock();
}

function updateClock(){
 const now=Date.now(),remaining=leftEggs(),end=E.batchReadyAt(state.batch),ready=remaining.filter(readyEgg).length;
 const text=!remaining.length?'等待开始':ready===remaining.length?'全部孵化完成':`剩余 ${duration((end??now)-now)}`;
 if($('#timer'))$('#timer').textContent=text;
 const p=!remaining.length?0:Math.min(100,Math.max(0,(now-state.batch.started)/Math.max(1,(end??now)-state.batch.started)*100));
 $('.progress')?.style.setProperty('--progress',p+'%');
}

function showSheet(title,html){
 lastFocus=document.activeElement;$('#sheet-title').textContent=title;$('#sheet-body').innerHTML=html;$('#sheet-layer').hidden=false;$('#game-content').inert=true;$('#sheet-title').focus();
}
function closeSheet(){if(!$('#sheet-layer'))return;const wasOpen=!$('#sheet-layer').hidden;$('#sheet-layer').hidden=true;$('#game-content').inert=false;if(wasOpen&&lastFocus?.isConnected)lastFocus.focus();}
function toast(message){clearTimeout(toastTimer);$('#toast').textContent=message;$('#toast').hidden=false;toastTimer=setTimeout(()=>$('#toast').hidden=true,2300);}

function toolDetails(id){
 const tool=E.tool(id),owned=state.toolLevels[id]>=0;
 if(!owned){showSheet(E.label(tool),'<p>这件厨具尚未拥有。</p><p class="quiet">此处只验证厨具栏与商店入口，不购买正式道具。</p><button class="primary" data-action="shop">查看商店入口</button>');return;}
 const info=E.cookInfo(state,id);
 showSheet(E.label(tool),`<div class="sheet-hero">${toolArt(id)}</div><div class="row"><span>孵化时间</span><b>${info.minutes} 分钟</b></div><div class="row"><span>开批费用</span><b>${info.cost} CP</b></div><p>选作下一批厨具。${leftEggs().length?'当前这一窝继续使用'+esc(E.label(E.tool(state.batch.tool)))+'。':'随后可在蛋窝开始孵化。'}</p><button class="primary" data-select-tool="${id}">选作下批厨具</button>`);
}

function action(name){
 if(name==='tools'){showSheet('全部厨具',`<p class="quiet">已拥有前四件；本轮沿用现有九件厨具素材。</p><div class="tool-choice">${Array.from({length:9},(_,id)=>`<button data-tool="${id}" aria-pressed="${id===nextTool}"><span class="tool-art">${toolArt(id)}</span>${esc(E.label(E.tool(id)))}${state.toolLevels[id]<0?'<br>未拥有':''}</button>`).join('')}</div>`);}
 if(name==='seasoning'){showSheet('下一批调味',`<p>选用调味料，再开始下一批。当前孵化不受影响。</p>${[0,1,2].map(id=>`<div class="row"><img class="row-icon" src="/assets/png/Tool/Tool2/tool_2_${id}_0_0.png" alt=""><span>${esc(E.label(E.ingredient(id)))}<small class="quiet"> · ${state.ingredients[id]} 份</small></span><button data-seasoning="${id}" aria-pressed="${state.selected.includes(id)}">${state.selected.includes(id)?'已选':'选用'}</button></div>`).join('')}<p class="quiet">Lv.2 可选择 2 种；再次点按取消。</p><button class="primary" data-close>选好了</button>`);}
 if(name==='clean'){
  const info=E.kitchenCleanInfo(state);showSheet('打扫厨房',`<div class="sheet-hero">${interfaceIcon('clean')}</div><p>脏污 <b>${info.percent}%</b> · ${info.canClean?'打扫后恢复整洁。':'厨房很干净，暂时不用打扫。'}</p><button class="primary" data-clean ${info.canClean?'':'disabled'}>打扫 · ${info.cost} CP</button>`);
 }
 if(name==='start'){
  const info=E.cookInfo(state,nextTool);showSheet('开始下一批',`<div class="sheet-hero">${toolArt(nextTool)}</div><p>${esc(E.label(E.tool(nextTool)))} · 24 枚普通蛋</p><p>${info.minutes} 分钟 · ${info.cost} CP</p><p class="quiet">${state.selected.length?'调味：'+state.selected.map(id=>esc(E.label(E.ingredient(id)))).join('、'):'未使用调味料'}</p><button class="primary" data-start ${state.cp<info.cost?'disabled':''}>确认开始</button>`);
 }
 if(name==='alarm'){state.alarm=!state.alarm;render();toast(state.alarm?'本页孵化提醒已开启':'本页孵化提醒已关闭');}
 if(name==='settings'){showSheet('原型设置',`<label>音效开关<input data-setting="sound" type="checkbox" ${state.sound?'checked':''}></label><label>音乐开关<input data-setting="music" type="checkbox" ${state.music?'checked':''}></label><p class="quiet">仅演示控件状态，本页不播放声音、不请求通知权限、不写入正式存档。</p><button class="primary" data-close>返回厨房</button>`);}
 if(name==='help'){showSheet('怎么体验这套布局','<p>孵化中点蛋查看进度；切到“可收取”状态后，逐只点按或轻划鸡宝收取。</p><p>点厨具可选作下批；点调味可准备搭配。空窝时可开始新一批。</p><p class="quiet">A / B / C 只改变布局，共用同一份临时状态。外部演示按钮用于检查空窝、收取和脏污状态。</p><button class="primary" data-close>知道了</button>');}
 if(name==='level'){showSheet('Lv.2 木房','<p>玩家等级 8 · 厨房等级 2</p><p class="quiet">本轮只验证木房布局，不制作其他等级，不执行升级。</p><button class="primary" data-close>返回厨房</button>');}
 if(['shop','inventory','skill'].includes(name)){
  const labels={shop:'商店',inventory:'仓库',skill:'手艺'};
  showSheet(labels[name]+'入口',`<p>这里是「${labels[name]}」的入口位置。</p>${name==='inventory'?`<p>本页已收取 ${Object.values(state.farm).reduce((a,b)=>a+b,0)} 只鸡宝。</p>`:''}<p class="quiet">本轮只验证布局与入口辨识。正式${labels[name]}页面及其跳转方式没有修改。</p><button class="primary" data-close>返回厨房</button>`);
 }
}

function takeEgg(index,quiet=false){
 const e=state.batch?.eggs[index];if(!e||e.collected)return;
 if(readyEgg(e)){if(E.collect(state,index)){render();if(!quiet)toast('收取 1 只鸡宝 · +1 CP');}return;}
 if(!quiet)showSheet('这一窝正在孵化',`<div class="sheet-hero">${sprite('/web/art/egg-v4.png')}</div><p>${leftEggs().length} 枚 · ${esc(E.label(E.tool(state.batch.tool)))}</p><p>剩余 ${duration((E.batchReadyAt(state.batch)??Date.now())-Date.now())}</p><button class="primary" data-close>继续等待</button>`);
}

document.addEventListener('click',event=>{
 const target=event.target.closest('button');if(!target)return;
 try{
  if(target.dataset.layout){closeSheet();setLayout(target.dataset.layout);return;}
  if(target.dataset.demo){reset(target.dataset.demo);return;}
  if(target.hasAttribute('data-close')){closeSheet();return;}
  if(target.dataset.action){action(target.dataset.action);return;}
  if(target.hasAttribute('data-tool')){toolDetails(Number(target.dataset.tool));return;}
  if(target.hasAttribute('data-select-tool')){nextTool=Number(target.dataset.selectTool);closeSheet();render();toast('下批厨具：'+E.label(E.tool(nextTool)));return;}
  if(target.hasAttribute('data-egg')){takeEgg(Number(target.dataset.egg));return;}
  if(target.dataset.nav){if(target.dataset.nav==='厨房'){closeSheet();toast('已经在厨房');}else showSheet(target.dataset.nav+'入口',`<p>底部「${target.dataset.nav}」入口已保留。</p><p class="quiet">本轮只做厨房布局，不制作或迁移其他页面。</p><button class="primary" data-close>返回厨房</button>`);return;}
  if(target.hasAttribute('data-seasoning')){const id=Number(target.dataset.seasoning),index=state.selected.indexOf(id);if(index>=0)state.selected.splice(index,1);else if(state.selected.length<2&&state.ingredients[id]>0)state.selected.push(id);else{toast('Lv.2 最多选择 2 种调味料');return;}render();action('seasoning');$('#sheet-body').querySelector(`[data-seasoning="${id}"]`).focus();return;}
  if(target.hasAttribute('data-clean')){E.clean(state);closeSheet();render();toast('打扫完成');return;}
  if(target.hasAttribute('data-start')){E.startBatch(state,nextTool);closeSheet();render();toast('已开始孵化 · 24 枚蛋');return;}
 }catch(error){toast(error.message);}
});
$('.scrim').addEventListener('click',closeSheet);
document.addEventListener('change',event=>{if(event.target.dataset.setting)state[event.target.dataset.setting]=event.target.checked;});
document.addEventListener('keydown',event=>{
 if($('#sheet-layer').hidden)return;
 if(event.key==='Escape'){event.preventDefault();closeSheet();}
 if(event.key==='Tab'){const nodes=[...$('#sheet').querySelectorAll('button:not(:disabled),input,a[href]')],first=nodes[0],last=nodes.at(-1);if(!first)return;if(event.shiftKey&&(document.activeElement===first||document.activeElement===$('#sheet-title'))){last.focus();event.preventDefault();}else if(!event.shiftKey&&(document.activeElement===last||document.activeElement===$('#sheet-title'))){first.focus();event.preventDefault();}}
});
$('#phone').addEventListener('pointerdown',event=>{const egg=event.target.closest('[data-egg]');if(egg&&readyEgg(state.batch.eggs[Number(egg.dataset.egg)])){dragging=true;collectedDuringDrag.clear();}});
document.addEventListener('pointerup',()=>dragging=false);
document.addEventListener('pointercancel',()=>dragging=false);
$('#phone').addEventListener('pointermove',event=>{if(!dragging)return;const egg=document.elementFromPoint(event.clientX,event.clientY)?.closest('[data-egg]');if(egg){const id=Number(egg.dataset.egg);if(!collectedDuringDrag.has(id)){collectedDuringDrag.add(id);takeEgg(id,true);}}});

function setLayout(value){
 layout=value;$('#phone').dataset.layout=value;document.querySelectorAll('[data-layout]').forEach(el=>{if(el.tagName==='BUTTON')el.setAttribute('aria-current',String(el.dataset.layout===value));});
 const info=layouts[value];$('#layout-title').textContent=info.title;$('#layout-description').textContent=info.description;$('#layout-points').innerHTML=info.points.map(t=>`<li>${t}</li>`).join('');
 $('#phone-link').href=`?layout=${value}&view=phone`;const u=new URL(location.href);u.searchParams.set('layout',value);history.replaceState(null,'',u);
}
function fit(){const mobile=innerWidth<=650;const scale=single?Math.min(innerWidth/390,innerHeight/844):mobile?Math.min(1,(innerWidth-24)/390):Math.min(1,(innerHeight-146)/844);const k=Math.max(.25,scale);$('.phone-frame').style.width=390*k+'px';$('.phone-frame').style.height=844*k+'px';$('#phone').style.transform=`scale(${k})`;}
addEventListener('resize',fit);
reset();setLayout(layout);fit();
setInterval(()=>{const old=leftEggs().map(e=>e.status).join();E.updateBatch(state);if(old!==leftEggs().map(e=>e.status).join())render();else updateClock();},1000);
// Read-only diagnostics used by the prototype acceptance checks.
window.kitchenPrototype={snapshot:()=>({layout,level:state.kitchenLevel+1,cp:state.cp,nextTool,batchTool:state.batch?.tool??null,eggCount:leftEggs().length,ready:leftEggs().filter(readyEgg).length,selected:[...state.selected],clean:E.kitchenCleanInfo(state).percent,alarm:state.alarm}),assets:()=>[...new Set([...document.querySelectorAll('#game-content img')].map(e=>e.getAttribute('src')).concat([...document.querySelectorAll('#game-content image')].map(e=>e.getAttribute('href'))))].sort(),eggPositions:()=>eggPositions.map(p=>[...p])};

