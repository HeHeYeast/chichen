import { DATA } from './data.js';
import * as E from './engine.js';
import {farmDisplay,timeZone} from './farm.js';
import {createRenderer} from './scene.js';
import {NAV,LAYOUT as L} from './theme.js';
import {characterImage,toolImage,speciesLabel} from './catalog.js';
const game=document.querySelector('#game'),canvas=document.querySelector('#scene'),ctx=canvas.getContext('2d');
const controls=document.querySelector('#controls'),panels=document.querySelector('#panels'),dialogs=document.querySelector('#dialog-layer');
const review=new URLSearchParams(location.search).has('review');
const storageKey=review?'chick-kitchen-baseline-review-v1':'chick-kitchen-baseline-v1';
let state=E.readSave(localStorage,storageKey),page=-1,loaded=false,panel='',shopTab=0,albumEgg=0,selection={},toolScroll=0,farmScroll=0,clockOffset=0,speed=1,virtualNow=Date.now(),previousReal=Date.now(),tick=0,dialogAction=null,lastFocused=null;
const images=new Map(),flights=[],walkers=[];
const now=()=>virtualNow+clockOffset;
const imgPath=toolImage;
const charPath=characterImage;
const sourcePath=p=>'/assets/png/'+p;
const soundNames=['start','yes','no','click','open','alert0','alert1','buy','sell','clean','fix','break','chick0','error','duck0'];
const sounds=soundNames.map((s,i)=>new Audio(`/res/raw/se${String(i).padStart(3,'0')}_${s}.mp3`));
const bgm=new Audio('/assets/music/bgm/bgm_000.mp3');bgm.loop=true;bgm.volume=.35;
function sound(id){if(!state.sound)return;const a=sounds[id].cloneNode();a.volume=.65;a.play().catch(()=>{});}
function music(){if(!state.music||page<0){bgm.pause();return;}const id=page===1?1:page===2?2:0;const src=`/assets/music/bgm/bgm_00${id}.mp3`;if(!bgm.src.endsWith(src))bgm.src=src;bgm.play().catch(()=>{});}
function save(){state.lastSeen=now();try{localStorage.setItem(storageKey,JSON.stringify(state));}catch{announce('浏览器存储不可用，进度无法保存。');}}
function announce(message){document.querySelector('#announcement').textContent=message;}
function act(fn){try{fn();save();renderControls();}catch(e){sound(13);alertBox(e.message);}}
function loadImage(path){if(images.has(path))return images.get(path);const image=new Image();image.src=path;images.set(path,image);return image;}
function image(path,x,y,w,h,flip=false,alpha=1){
  const toolMatch=path.match(/Tool1\/tool_1_([0-3])_0_0\.png$/);if(toolMatch)path='/web/art/ui-atlas.png#'+toolMatch[1];
  const [file,cell]=path.split('#'),im=loadImage(file);if(!im.complete||!im.naturalWidth)return;
  ctx.save();ctx.globalAlpha=alpha;ctx.translate(flip?x+w:x,y);if(flip)ctx.scale(-1,1);
  if(cell!==undefined){const id=Number(cell),cw=im.naturalWidth/4,ch=im.naturalHeight/2;ctx.drawImage(im,id%4*cw,Math.floor(id/4)*ch,cw,ch,0,0,w,h);}else ctx.drawImage(im,0,0,w,h);
  ctx.restore();
}
function toolPortrait(id,lv){return id<4&&lv===0?`<span class="tool-art" style="--column:${id}" aria-hidden="true"></span>`:`<img src="${imgPath(1,id,lv)}" alt="">`;}
function round(x,y,w,h,r,fill,stroke=null,line=1){ctx.beginPath();ctx.roundRect(x,y,w,h,r);if(fill){ctx.fillStyle=fill;ctx.fill();}if(stroke){ctx.strokeStyle=stroke;ctx.lineWidth=line;ctx.stroke();}}
const renderer=createRenderer(ctx,image);
let farmZone=-1;
function makeWalkers(){walkers.splice(0,walkers.length,...farmDisplay(state,now()));farmZone=timeZone(now());}
function paint(){if(page===1&&farmZone!==timeZone(now()))makeWalkers();renderer.paint(state,{page,loaded,now:now(),tick,toolScroll,farmScroll,flights,walkers});}
function hotspot(name,x,y,w,h,handler){const b=document.createElement('button');b.className='hotspot';b.setAttribute('aria-label',name);Object.assign(b.style,{left:x+'px',top:y+'px',width:w+'px',height:h+'px'});b.onclick=handler;controls.append(b);return b;}
function renderControls(){
  controls.replaceChildren();
  if(page<0){if(loaded)hotspot('开始游戏',72,405,176,49,()=>{sound(0);changePage(0);});return;}
  NAV.forEach((n,i)=>hotspot(n.title,8+i*78,503,70,57,()=>changePage(n.id)));
  hotspot('设置',275,13,34,34,()=>changePage(3));
  if(page===0){
    hotspot('选择调味料',8,106,42,47,openIngredients);
    hotspot('厨房等级 '+(state.kitchenLevel+1),9,10,43,43,()=>{if(E.canUpgradeKitchen(state))confirmBox(`升级至厨房Lv.${state.kitchenLevel+2}需要花费${(state.kitchenLevel+1)*10000}cp。\n你确定要升级吗？`,()=>act(()=>{E.upgradeKitchen(state);sound(7);}));else alertBox('请先将前六种调理用具升至当前厨房等级。');});
    if(state.dirty)hotspot('打扫厨房',270,106,42,47,()=>confirmBox('打扫厨房需要花费40cp。\n你确定要打扫吗？',()=>act(()=>{E.clean(state,now());sound(9);})));
    if(state.duck)[0,1].forEach(i=>hotspot(i?'选择鸭蛋':'选择鸡蛋',9,159+i*29,25,27,()=>act(()=>{state.egg=i;sound(3);}))); 
    if(state.batch?.eggs.some(e=>!e.collected))hotspot('孵化闹钟 '+(state.alarm?'开启':'关闭'),8,L.timerY-2,35,31,()=>act(()=>{state.alarm=!state.alarm;sound(3);}));
    for(let slot=0;slot<4;slot++){const id=toolScroll+slot;hotspot((state.toolLevels[id]>=0?'使用':'购买')+E.label(E.tool(id)),L.toolX+slot*(L.toolWidth+L.toolGap),L.toolY,L.toolWidth,L.toolHeight,()=>{if(state.toolLevels[id]>=0)requestCook(id);else{shopTab=0;changePage(2);}});}
    hotspot('向右滚动厨具',288,482,32,18,()=>{toolScroll=Math.min(4,toolScroll+1);renderControls();});hotspot('向左滚动厨具',0,482,32,18,()=>{toolScroll=Math.max(0,toolScroll-1);renderControls();});
    state.batch?.eggs.forEach((e,i)=>{if(!e.collected)hotspot(e.status==='egg'?'孵化中的蛋 '+(i+1):'收取'+E.label(E.char(e.egg,e.id))+' '+(i+1),e.x-19,e.y-13,38,42,()=>collectEgg(i));});
  }else if(page===1){
    hotspot('打开收成表',2-farmScroll,136,90,90,openAlbum);
    hotspot('整修农场',9,460,98,34,()=>confirmBox(`整修农场需要花费${E.repairCost(state,now())}cp。\n你确定要整修吗？`,()=>act(()=>{E.repair(state,now());sound(10);}))); 
    hotspot('妖怪村',235-farmScroll,146,65,65,()=>alertBox('妖怪村的活动玩法正在按原版规则还原。'));
    hotspot('神社',355-farmScroll,148,65,65,()=>alertBox('神社的奉纳玩法正在按原版规则还原。'));
    if(farmScroll>0)hotspot('回到农舍',0,420,40,40,()=>{farmScroll=0;renderControls();});
  }
  reportStatus();
}
function reportStatus(){
  if(review)parent.postMessage({type:'chick-status',cp:state.cp,ready:state.batch?.eggs.filter(e=>!e.collected&&e.status==='ready').length??0,eggs:state.batch?.eggs.filter(e=>!e.collected).length??0,clock:new Date(now()).toLocaleTimeString(),page},location.origin);
}
function changePage(index){if(index===page)return;closePanel();closeDialog();page=index;sound(3);if(page===1){const lost=E.checkFarmLoss(state,now());makeWalkers();if(lost){save();alertBox(`脱逃事件：逃走了${lost}只鸡！`);}}if(page===2)openShop();if(page===3)openSettings();if(page===4)openAlbum();music();renderControls();paint();}
function requestCook(id){const info=E.cookInfo(state,id);const replace=state.batch?.eggs.some(e=>!e.collected);const message=replace?`你确定要中断目前的调理，\n然后使用${E.label(E.tool(id))}开始新的调理吗？\n之前花费的CP不会退还。\n本次需要${info.cost}cp。`:`使用${E.label(E.tool(id))}调理，需要花费${info.cost}cp。\n\n你确定要使用吗？`;
  confirmBox(message,()=>act(()=>{const candidate=structuredClone(state);candidate.batch=null;E.startBatch(candidate,id,now());state=candidate;sound(1);}));
}
function collectEgg(index){if(dialogs.children.length||panel)return;const e=state.batch?.eggs[index];if(!e)return;if(E.collect(state,index)){flights.push({born:performance.now(),e:{...e},x:e.x-30,y:e.y-30,dx:0,dy:0,h:60,alpha:1,phase:0,wait:0});sound(1);save();announce('收取'+E.label(E.char(e.egg,e.id))+'，获得1cp');renderControls();}}
function animateFlights(){for(let i=flights.length-1;i>=0;i--)if(performance.now()-flights[i].born>=650)flights.splice(i,1);}
function closePanel(){panel='';panels.replaceChildren();renderControls();}
function closeDialog(){dialogs.replaceChildren();dialogAction=null;lastFocused?.focus();}
function confirmBox(message,fn,onlyOK=false){lastFocused=document.activeElement;dialogs.innerHTML=`<div class="modal-shade"></div><section class="confirm paper" role="dialog" aria-modal="true" aria-label="确认"><p></p><footer>${onlyOK?'':'<button class="cream" data-no>不要</button>'}<button class="cream" data-yes>${onlyOK?'好':'要'}</button></footer></section>`;dialogs.querySelector('p').textContent=message;dialogAction=fn;dialogs.querySelector('[data-no]')?.addEventListener('click',()=>{sound(2);closeDialog();});dialogs.querySelector('[data-yes]').onclick=()=>{const action=dialogAction;closeDialog();sound(1);action?.();};dialogs.querySelector('button').focus();}
function alertBox(message){confirmBox(message,null,true);}
function showPanel(title,body,classes=''){panel=title;sound(4);panels.innerHTML=`<section class="panel paper ${classes}" role="dialog" aria-label="${title}"><button class="close" aria-label="关闭">×</button><h2>${title}</h2>${body}</section>`;panels.querySelector('.close').onclick=()=>page===4?changePage(0):closePanel();panels.querySelector('.close').focus();}
function openIngredients(){let draft=[...state.selected];const max=Math.min(3,state.kitchenLevel+1);const draw=()=>{showPanel('调味料',`<p class="counter">${draft.length} / ${max}</p><div class="scroll ingredients-grid">${Object.entries(state.ingredients).filter(([,n])=>n>0).map(([id,n])=>`<button class="ingredient ${draft.includes(+id)?'selected':''}" data-id="${id}" aria-pressed="${draft.includes(+id)}" aria-label="${E.label(E.ingredient(+id))}"><img src="${imgPath(2,id)}" alt=""><small>${n}</small><div class="ingredient-title">${E.label(E.ingredient(+id))}</div></button>`).join('')||'<p class="empty">没有调味料，可以去商店购买。</p>'}</div><footer><span></span><button class="orange" data-ok>OK</button><span></span></footer>`);panels.querySelectorAll('[data-id]').forEach(b=>b.onclick=()=>{const id=+b.dataset.id;if(draft.includes(id))draft=draft.filter(i=>i!==id);else if(draft.length<max)draft.push(id);else{sound(13);return;}sound(3);draw();});panels.querySelector('[data-ok]').onclick=()=>{state.selected=draft;save();closePanel();};};draw();}
function openShop(){panel='商店';const rows=shopTab===0?DATA.tools[1].map(t=>{const lv=state.toolLevels[t.id],allowed=E.canBuyTool(state,t.id),cost=t[`lv_${lv+1}_buy_cp`];return `<div class="item">${toolPortrait(t.id,Math.min(2,Math.max(0,lv+1)))}<div class="info"><strong>${E.label(t)} Lv.${Math.min(3,lv+2)}</strong><br>${lv===2?'已达到最高等级':allowed?`${cost}cp`:'尚未解锁'}<br><small>${lv>=0?'持有 Lv.'+(lv+1):'未购买'}</small></div><button class="orange" data-buy-tool="${t.id}" ${!allowed?'disabled':''}>${lv<0?'购买':'升级'}</button></div>`;}).join(''):shopTab===1?E.availableIngredients(state).map(id=>{const t=E.ingredient(id);return `<div class="item"><img src="${imgPath(2,id)}" alt=""><div class="info"><strong>${E.label(t)}</strong><br>${t.buy_cp}cp<small>　持有：${state.ingredients[id]??0}</small></div><button class="orange" data-buy-ingredient="${id}">购买</button></div>`;}).join(''):`<div class="item"><img src="/assets/png/Egg/egg_0_0_0.png" alt=""><div class="info">鸡蛋<br>已拥有</div></div><div class="item"><img src="/assets/png/Egg/egg_1_0_0.png" alt=""><div class="info">鸭蛋<br>${state.duck?'已拥有':'2500cp · 原版需登录解锁'}</div><button class="orange" data-duck ${state.duck?'disabled':''}>${state.duck?'已拥有':'详情'}</button></div>`;
  showPanel('商店',`<div class="subtabs">${['调理用具','调味料','其他'].map((n,i)=>`<button data-shop-tab="${i}" class="${i===shopTab?'active':''}">${n}</button>`).join('')}</div><div class="scroll rows">${rows}</div>`,'store-panel');panels.querySelector('.close').style.display='none';
  panels.querySelectorAll('[data-shop-tab]').forEach(b=>b.onclick=()=>{shopTab=+b.dataset.shopTab;openShop();});
  panels.querySelectorAll('[data-buy-tool]').forEach(b=>b.onclick=()=>{const id=+b.dataset.buyTool,cost=E.tool(id)[`lv_${state.toolLevels[id]+1}_buy_cp`];confirmBox(`${E.label(E.tool(id))}\n需要花费${cost}cp。\n你确定要购买吗？`,()=>act(()=>{E.buyTool(state,id);sound(7);openShop();}));});
  panels.querySelectorAll('[data-buy-ingredient]').forEach(b=>b.onclick=()=>{const id=+b.dataset.buyIngredient;confirmBox(`${E.label(E.ingredient(id))} ×1\n需要花费${E.ingredient(id).buy_cp}cp。\n你确定要购买吗？`,()=>act(()=>{E.buyIngredient(state,id);sound(7);openShop();}));});
  panels.querySelector('[data-duck]')?.addEventListener('click',()=>alertBox('原版鸭蛋需要登录 iDT 并上传纪录后解锁。\n本地复刻尚未接入该服务。'));
}
let collectionScroll=0;
function openAlbum(){selection={};if(page===4)renderCollection();else renderAlbum();}
function renderCollection(){
  const list=DATA.characters[albumEgg],discovered=list.filter(c=>state.total[E.key(albumEgg,c.id)]>0).length;
  showPanel(`已发现 ${discovered} / ${list.length}`,`<div class="subtabs"><button data-species="0" class="${albumEgg===0?'active':''}">鸡宝</button><button data-species="1" class="${albumEgg===1?'active':''}">鸭宝</button></div><div class="scroll collection-grid">${list.map(c=>{const k=E.key(albumEgg,c.id),known=state.total[k]>0;return `<button class="collection-card ${known?'discovered':'locked'}" data-card="${c.id}" aria-label="${known?E.label(c):'未发现品种 '+(c.id+1)}"><small>${speciesLabel(albumEgg,c.id)}</small><img src="${known?charPath(albumEgg,c.id):'/assets/png/Egg/egg_'+albumEgg+'_0_0.png'}" alt="">${known?'':'<span class="lock-mark">?</span>'}<strong>${known?E.label(c):'未发现'}</strong>${known?`<span class="stock">${state.farm[k]??0} 只</span>`:''}</button>`;}).join('')}</div>`,'collection');
  const grid=panels.querySelector('.collection-grid');grid.scrollTop=collectionScroll;grid.onscroll=()=>{collectionScroll=grid.scrollTop;};
  panels.querySelectorAll('[data-species]').forEach(b=>b.onclick=()=>{albumEgg=+b.dataset.species;collectionScroll=0;renderCollection();});
  panels.querySelectorAll('[data-card]').forEach(b=>b.onclick=()=>{const id=+b.dataset.card;if(state.total[E.key(albumEgg,id)])showCharacter(albumEgg,id);else sound(13);});
}

function renderAlbum(){const list=DATA.characters[albumEgg],sum=Object.entries(selection).reduce((v,[k,n])=>v+E.char(...k.split(':').map(Number)).cp_1*n,0);const scrollTop=panels.querySelector('.rows')?.scrollTop??0;
  showPanel(page===4?'鸡宝图鉴':'收成表',`<div class="subtabs"><button data-egg="0" class="${albumEgg===0?'active':''}">鸡</button><button data-egg="1" class="${albumEgg===1?'active':''}">鸭</button></div><div class="scroll rows">${list.map(c=>{const k=E.key(albumEgg,c.id),known=state.total[k]>0;return `<div class="item ${known?'':'unknown'}"><button class="portrait" data-char="${c.id}" aria-label="${known?E.label(c):'未发现品种 '+(c.id+1)}"><img src="${charPath(albumEgg,c.id)}" alt="">${known?'':'<span class="question">?</span>'}</button><div class="info"><strong>${albumEgg?'D':'C'}${String(c.id+1).padStart(2,'0')} ${known?E.label(c):'???'}</strong><br>价格：${known?c.cp_1:'-'}<br>数量：${known?state.farm[k]??0:'-'}</div>${known?`<div class="count"><button class="orange" data-minus="${k}" aria-label="减少${E.label(c)}卖出数量">−</button><output>${selection[k]??0}</output><button class="orange" data-plus="${k}" aria-label="增加${E.label(c)}卖出数量">+</button></div>`:''}</div>`;}).join('')}</div><footer><button class="orange" data-sell>卖出</button><span>合计：${sum} cp</span></footer>`,'album');
  panels.querySelector('.rows').scrollTop=scrollTop;panels.querySelectorAll('[data-egg]').forEach(b=>b.onclick=()=>{albumEgg=+b.dataset.egg;selection={};panels.querySelector('.rows').scrollTop=0;renderAlbum();});
  panels.querySelectorAll('[data-plus]').forEach(b=>b.onclick=()=>{const k=b.dataset.plus;selection[k]=Math.min(state.farm[k]??0,(selection[k]??0)+1);renderAlbum();});
  panels.querySelectorAll('[data-minus]').forEach(b=>b.onclick=()=>{const k=b.dataset.minus;selection[k]=Math.max(0,(selection[k]??0)-1);renderAlbum();});
  panels.querySelectorAll('[data-char]').forEach(b=>b.onclick=()=>{const id=+b.dataset.char;if(state.total[E.key(albumEgg,id)])showCharacter(albumEgg,id);else sound(13);});
  panels.querySelector('[data-sell]').onclick=()=>{if(sum<=0){alertBox('请先选择要卖出的数量。');return;}confirmBox(`合计：${sum}cp\n你确定要卖出吗？`,()=>act(()=>{E.sell(state,selection);selection={};sound(8);makeWalkers();renderAlbum();}));};
}
function showCharacter(egg,id){
  const c=E.char(egg,id),k=E.key(egg,id);let quantity=Math.min(1,state.farm[k]??0);
  function draw(){
    showPanel(`${speciesLabel(egg,id)} ${E.label(c)}`,`<div class="portrait-stage"><img src="${charPath(egg,id)}" alt="${E.label(c)}"></div><div class="character-stats"><p><small>售价</small><strong>${c.cp_1} CP</strong></p><p><small>拥有</small><strong>${state.farm[k]??0} 只</strong></p></div><p class="lifetime">累计孵化 ${state.total[k]??0} 只</p><div class="detail-sale"><div class="count"><button class="orange" data-decrease aria-label="减少卖出数量">−</button><output>${quantity}</output><button class="orange" data-increase aria-label="增加卖出数量">+</button></div><button class="orange" data-sell-one ${quantity?'':'disabled'}>卖出 · ${quantity*c.cp_1} CP</button></div><footer><button class="back-button" data-back>‹ 返回${page===4?'图鉴':'收成表'}</button></footer>`,'detail');
    panels.querySelector('[data-decrease]').onclick=()=>{quantity=Math.max(0,quantity-1);draw();};
    panels.querySelector('[data-increase]').onclick=()=>{quantity=Math.min(state.farm[k]??0,quantity+1);draw();};
    panels.querySelector('[data-sell-one]').onclick=()=>confirmBox(`卖出 ${quantity} 只${E.label(c)}，获得 ${quantity*c.cp_1} CP。`,()=>act(()=>{E.sell(state,{[k]:quantity});quantity=Math.min(1,state.farm[k]);sound(8);makeWalkers();draw();}));
    panels.querySelector('[data-back]').onclick=()=>page===4?renderCollection():renderAlbum();
  }
  draw();
}
function openSettings(){panel='其他';panels.innerHTML=`<div class="settings"><button class="paper" data-manual>游戏说明</button><div class="row"><span>背景音乐</span><button class="orange" data-music>${state.music?'ON':'OFF'}</button></div><div class="row"><span>音效</span><button class="orange" data-sound>${state.sound?'ON':'OFF'}</button></div><button class="paper" data-save>保存游戏</button><button class="paper" data-title>回到标题画面</button></div>`;panels.querySelector('[data-manual]').onclick=()=>openManual(0);panels.querySelector('[data-music]').onclick=()=>{state.music=!state.music;save();music();openSettings();};panels.querySelector('[data-sound]').onclick=()=>{state.sound=!state.sound;save();sound(3);openSettings();};panels.querySelector('[data-save]').onclick=()=>{save();alertBox('游戏进度已保存在此浏览器。');};panels.querySelector('[data-title]').onclick=()=>{save();page=-1;closePanel();music();renderControls();};}
function openManual(i){const files=i===0?['manual000_cn0.jpg','manual000_cn1.jpg']:i===1?['manual001_cn.jpg']:['manual002_cn.jpg'];showPanel('游戏说明',`<div class="subtabs">${['厨房','农场','商店'].map((n,j)=>`<button data-manual-tab="${j}" class="${j===i?'active':''}">${n}</button>`).join('')}</div><div class="scroll">${files.map(f=>`<img src="/assets/png/Manual/${f}" alt="原版${['厨房','农场','商店'][i]}操作说明">`).join('')}</div>`,'manual');panels.querySelector('.close').onclick=openSettings;panels.querySelectorAll('[data-manual-tab]').forEach(b=>b.onclick=()=>openManual(+b.dataset.manualTab));}
let pointer=null;
controls.addEventListener('pointerdown',ev=>{const r=game.getBoundingClientRect(),x=(ev.clientX-r.left)/r.width*320,y=(ev.clientY-r.top)/r.height*568;pointer={x,y,last:x,drag:false};if(page===0&&y>168&&y<309)hitEggs(x,y);});
controls.addEventListener('pointermove',ev=>{if(!pointer||panel||dialogs.children.length)return;const r=game.getBoundingClientRect(),x=(ev.clientX-r.left)/r.width*320,y=(ev.clientY-r.top)/r.height*568;if(Math.abs(x-pointer.x)>4)pointer.drag=true;if(page===0&&y>168&&y<309)hitEggs(x,y);if(page===1&&pointer.y>220&&pointer.y<470){farmScroll=Math.max(0,Math.min(510,farmScroll-(x-pointer.last)));}pointer.last=x;});
window.addEventListener('pointerup',()=>{if(page===1&&pointer?.drag)renderControls();pointer=null;});
function hitEggs(x,y){const list=state.batch?.eggs??[];for(let i=list.length-1;i>=0;i--){const e=list[i];if(!e.collected&&Math.abs(e.x-x)<19&&Math.abs(e.y+6-y)<21){collectEgg(i);break;}}}
document.addEventListener('keydown',e=>{if(e.key==='Escape'){if(dialogs.children.length)closeDialog();else if(panel&&page<2)closePanel();else if(page===3)openSettings();else if(page===4)changePage(0);}if(e.key==='Tab'&&dialogs.children.length){const b=[...dialogs.querySelectorAll('button')],first=b[0],last=b.at(-1);if(e.shiftKey&&document.activeElement===first){e.preventDefault();last.focus();}else if(!e.shiftKey&&document.activeElement===last){e.preventDefault();first.focus();}}});
function resize(){const scale=Math.min(innerWidth/320,innerHeight/568);game.style.transform=`scale(${scale})`;}
window.addEventListener('resize',resize);resize();
window.addEventListener('pagehide',save);document.addEventListener('visibilitychange',()=>{if(document.hidden){save();bgm.pause();}else{advanceClock();E.resume(state,now());if(page===1){const lost=E.checkFarmLoss(state,now());makeWalkers();if(lost)alertBox(`脱逃事件：逃走了${lost}只鸡！`);}music();}});
// Only the separate review iframe accepts these controls. Real saves never do.
window.addEventListener('message',event=>{if(!review||event.origin!==location.origin||event.source!==parent||event.data?.type!=='chick-review')return;const command=event.data.command;
  if(command==='speed'){virtualNow=now();clockOffset=0;speed=event.data.value;}
  if(command==='skip')clockOffset+=event.data.value;
  if(command==='scene'){
    speed=1;virtualNow=Date.now();clockOffset=0;state=E.freshState(now());state.toolLevels=[0,0,0,0,0,0,-1,-1];state.cp=600;flights.length=0;
    if(event.data.value==='title'){page=-1;closePanel();}
    else if(event.data.value==='new'){state=E.freshState(now());page=-1;changePage(0);}
    else if(event.data.value==='duck'){state.duck=true;state.egg=1;E.startBatch(state,1,now());state.batch.eggs.forEach(e=>{e.status='ready';e.openAt=now()-10000;});page=-1;changePage(0);}
    else if(event.data.value==='farm'){for(let i=0;i<12;i++){state.farm['0:'+i]=3;state.total['0:'+i]=3;}page=-1;changePage(1);}
    else {const id=event.data.value==='dirty'?0:1;E.startBatch(state,id,now());if(event.data.value==='ready'||event.data.value==='dirty')state.batch.eggs.forEach(e=>{e.status='ready';e.openAt=now()-10000;});if(event.data.value==='hatching')state.batch.eggs.forEach((e,i)=>{e.openAt=now()+i*600-1000;});if(event.data.value==='dirty')state.dirty=true;page=-1;changePage(0);}
    if(['ready','dirty','duck'].includes(event.data.value)&&state.batch){state.batch.started=now()-900000;state.batch.ends=now()-1;}
    if(event.data.value==='hatching'&&state.batch)state.batch.ends=now()+13800;
    save();renderControls();
  }
});
const primary=['web/art/kitchen-v3.png','web/art/ui-atlas.png',
  ...DATA.images.filter(p=>/\/Egg\//.test(p)||/\/Tool1\/tool_1_\d+_0_0\.png$/.test(p)||/alarm_|kitchen_fix_0_0|farm_fix_0_0/.test(p)),
  ...Array.from({length:20},(_,i)=>charPath(0,i).slice(1))];
const loading=document.createElement('p');loading.className='loading';loading.textContent='正在载入…';controls.append(loading);
await Promise.allSettled([document.fonts.ready,...primary.map(p=>new Promise(resolve=>{const im=loadImage('/'+p);if(im.complete)resolve();else{im.onload=resolve;im.onerror=resolve;}}))]);
loaded=true;renderControls();paint();
function advanceClock(){const real=Date.now();virtualNow+=(real-previousReal)*speed;previousReal=real;}
setInterval(()=>{advanceClock();tick++;const events=E.updateBatch(state,now());if(state.alarm&&state.batch&&!state.batch.alarmed&&now()>=state.batch.ends){state.batch.alarmed=true;sound(5);announce('这一批鸡宝已经孵化，请及时收取。');save();}if(events.length){if(page===0)[...new Set(events)].forEach(s=>sound(s==='break'?11:s==='duck'?14:12));save();renderControls();}if(tick%60===0){for(const e of state.batch?.eggs??[])if(e.status==='ready'&&Math.random()<.5)e.flipped=!e.flipped;}
  animateFlights();for(const w of walkers){w.turn+=Math.floor(Math.random()*2);if(w.turn>=30){w.turn=0;w.dir=Math.random()<.5?-1:1;}}if(tick%10===0)reportStatus();
},100);

function renderFrame(){paint();requestAnimationFrame(renderFrame);}requestAnimationFrame(renderFrame);

