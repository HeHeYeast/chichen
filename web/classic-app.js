import { DATA } from './data.js';
import * as E from './engine.js';
import {farmDisplay,timeZone} from './farm.js';
const game=document.querySelector('#game'),canvas=document.querySelector('#scene'),ctx=canvas.getContext('2d');
const controls=document.querySelector('#controls'),panels=document.querySelector('#panels'),dialogs=document.querySelector('#dialog-layer');
const review=new URLSearchParams(location.search).has('review');
const storageKey='chick-kitchen-classic-review-v1';
let state=E.readSave(localStorage,storageKey),page=-1,loaded=false,panel='',shopTab=0,albumEgg=0,selection={},toolScroll=0,farmScroll=0,clockOffset=0,speed=1,virtualNow=Date.now(),previousReal=Date.now(),tick=0,dialogAction=null,lastFocused=null;
const images=new Map(),flights=[],walkers=[];
const now=()=>virtualNow+clockOffset;
const imgPath=(type,id,lv=0)=>`/assets/png/Tool/Tool${type}/tool_${type}_${id}_${lv}_0.png`;
const charPath=(egg,id)=>`/assets/png/Character/character_${egg}/character_${egg}_${id}_0_0.png`;
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
function image(path,x,y,w,h,flip=false,alpha=1){const im=loadImage(path);if(!im.complete||!im.naturalWidth)return;ctx.save();ctx.globalAlpha=alpha;if(flip){ctx.translate(x+w,y);ctx.scale(-1,1);ctx.drawImage(im,0,0,w,h);}else ctx.drawImage(im,x,y,w,h);ctx.restore();}
function round(x,y,w,h,r,fill,stroke=null,line=1){ctx.beginPath();ctx.roundRect(x,y,w,h,r);if(fill){ctx.fillStyle=fill;ctx.fill();}if(stroke){ctx.strokeStyle=stroke;ctx.lineWidth=line;ctx.stroke();}}
const rgb=n=>'#'+(n>>>0).toString(16).padStart(8,'0').slice(2);
function frame(x,y,w,h,r=10,active=false){round(x,y,w,h,r,active?rgb(-200082):'#fffff0','#181818',10);round(x,y,w,h,r,null,active?rgb(-12988):rgb(-2248316),8);round(x,y,w,h,r,null,active?rgb(-227838):rgb(-3109815),4);}
function text(str,x,y,size=16,fill='#fffff0',stroke='#24221e',width=2,align='center'){ctx.font=`${size}px Chick`;ctx.textAlign=align;ctx.textBaseline='alphabetic';ctx.lineJoin='round';if(stroke){ctx.strokeStyle=stroke;ctx.lineWidth=width;ctx.strokeText(String(str),x,y);}ctx.fillStyle=fill;ctx.fillText(String(str),x,y);}
function cp(y){round(210,y,130,28,14,'#0008','#fffff0',2);text(state.cp,300,y+22,21,'#ffe826','#fffff0',5,'right');text(state.cp,300,y+22,21,'#ffe826','#222',2.5,'right');text('cp',305,y+23,16,'#ffe826','#222',2.5,'left');}
function nav(){['厨房','农场','商店','其他'].forEach((name,i)=>{const y=i===page?-20:-26;frame(i*80,y,80,52,10,i===page);text(name,i*80+40,y+44,20,i===page?'#fff':'#e5e5e5',i===page?'#000':'#4c4c4c',3);});}
function paint(){
  ctx.setTransform(2,0,0,2,0,0);ctx.clearRect(0,0,320,568);
  if(page<0){image(sourcePath('MainMenu/main_bg.jpg'),0,0,320,568);image(sourcePath('MainMenu/main_logo_cn.png'),0,0,320,568);text(loaded?'点击画面开始':'正在载入…',160,475,19,'#fffff0','#664930',3);return;}
  if(page===0)paintKitchen();else if(page===1)paintFarm();else if(page===2)image(sourcePath('MainGame/store_bg.jpg'),0,0,320,568);else{ctx.fillStyle='#f4e9cf';ctx.fillRect(0,0,320,568);image(sourcePath('MainGame/option_bg.png'),0,0,320,568,false,.1);}
  if(page===2)cp(460);
  nav();
  // The original reserves the final 50 points for the banner ad.
  ctx.fillStyle='#15120f';ctx.fillRect(0,518,320,50);
}
function paintKitchen(){
  image(sourcePath(`Tool/Tool0/tool_0_0_${state.kitchenLevel}_0.jpg`),0,0,320,568);
  if(state.dirty)image(sourcePath(`Tool/Tool0/tool_0_0_${state.kitchenLevel}_dirty.png`),0,0,320,568);
  image(sourcePath(E.canUpgradeKitchen(state)?'MainGame/level_up_0.png':'MainGame/level_star_on.png'),284,34,36,36);text(state.kitchenLevel+1,302,60.5,20,'#fff','#25201c',2.5);
  if(state.dirty)image(sourcePath('MainGame/kitchen_fix_0_0.png'),282,74,40,40);
  round(9,98,24,24,12,'#fff0cf','#c6a35b',2);image(sourcePath('MainGame/change_icon.png'),14,103,14,14);
  state.selected.forEach((id,i)=>image(imgPath(2,id),34+i*20,82,48,48));
  if(state.duck){round(5,145,24,72,12,'#0006');image(sourcePath(`Egg/egg_0_0_${state.egg===0?3:0}.png`),-2,136,40,40);image(sourcePath(`Egg/egg_1_0_${state.egg===1?3:0}.png`),-2,172,40,40);}
  for(const e of state.batch?.eggs??[]){if(e.collected)continue;const x=e.x-30,y=e.y-30;const pulse=[0,1,2,3,4,4,3,2,1,0][tick%10];
    if(e.status==='egg'||e.status==='cracking')image(sourcePath(`Egg/egg_${e.egg}_0_${e.status==='egg'?0:1}.png`),x,y-pulse,60,60+pulse,e.flipped);
    else {image(charPath(e.egg,e.id),x,y-pulse,60,60+pulse,e.flipped);if(e.status==='hatching'){const a=Math.max(0,1-(now()-e.animationAt)/900);image(sourcePath(`Egg/egg_${e.egg}_0_2.png`),x,y,60,60,e.flipped,a);}}
  }
  for(const f of flights)image(charPath(f.e.egg,f.e.id),f.x+f.dx,f.y+f.dy,60,f.h,f.e.flipped,f.alpha);
  if(state.kitchenLevel===0)image(sourcePath('Tool/Tool0/tool_0_0_0_front.png'),0,0,320,568);
  const batch=state.batch;if(batch&&batch.eggs.some(e=>!e.collected)){
    const progress=Math.max(0,Math.min(1,(batch.ends-now())/(batch.ends-batch.started)));
    round(90,317,140,24,8,null,'#31261aba',8);round(90,317,140,24,8,null,'#ffe826',2);
    round(96,323,128*progress,12,2,'#ff8602');image(imgPath(1,batch.tool,batch.level),145,309,30,30);
    image(sourcePath(`MainGame/alarm_${state.alarm?'on':'off'}.png`),54,309,38,38);
    batch.ingredients.forEach((id,i)=>image(imgPath(2,id),130+i*15,287,30,30));
  }
  cp(355);ctx.fillStyle='#00000026';ctx.fillRect(0,460,320,58);
  state.toolLevels.forEach((lv,id)=>{if(lv<0)return;const x=id*64-toolScroll,active=batch?.tool===id&&batch.eggs.some(e=>!e.collected),y=active?436:442;
    frame(x,y,64,100,20,active);image(imgPath(1,id,lv),x+5,y+10,54,54);image(sourcePath('MainGame/level_star_on.png'),x+5,y+3,25,25);text(lv+1,x+17,y+22,14,'#fff','#3a3026',2.5);text(E.cookInfo(state,id).cost+'cp',x+43,y+23,14,'#eee','#4c4031',2.5);
    const min=E.cookInfo(state,id).minutes;const duration=min>=60?Math.floor(min/60)+'hr'+(min%60?' '+min%60+'m':''):min+'m';text(duration,x+32,y+73,14,'#fff','#4a4033',2.5);
  });
  if(state.toolLevels.filter(l=>l>=0).length>5){image(sourcePath('Button/go_right.png'),302,478,18,18);image(sourcePath('Button/go_right.png'),0,478,18,18,true);}
}
let farmZone=-1;
function makeWalkers(){walkers.splice(0,walkers.length,...farmDisplay(state,now()));farmZone=timeZone(now());}
function paintFarm(){const zone=timeZone(now());if(zone!==farmZone)makeWalkers();
  image(sourcePath(`Tool/Tool0/tool_0_1_${zone}_0.jpg`),-farmScroll,0,830,568);
  image(sourcePath(`Farm/farm_house_${zone}_0.png`),2-farmScroll,136,90,90);
  image(sourcePath(`Farm/monster_village_${zone}_0.png`),235-farmScroll,146,65,65);
  image(sourcePath(`Farm/jinja_house_${zone}_0.png`),355-farmScroll,148,65,65);
  for(const w of walkers){const cycle=(tick+w.phase)%36,p=(cycle<18?cycle:36-cycle)*48/180;if(w.shadow!==null)image(sourcePath('Character/character_shadow.png'),w.x-farmScroll,w.y+w.shadow,48,48);image(charPath(w.egg,w.id),w.x-farmScroll,w.y-p,48,48+p,w.dir<0);}
  image(sourcePath(`Tool/Tool0/tool_0_1_0_front_${zone===30?1:0}.png`),-farmScroll,465,830,55);
  round(-20,486,110,28,14,'#0008','#fffff0',2);text(E.farmHP(state,now())+'%',63,508,20,'#ffe826','#222',2.5);image(sourcePath('MainGame/farm_fix_0_0.png'),-6,480,40,40);cp(486);
}
function hotspot(name,x,y,w,h,handler){const b=document.createElement('button');b.className='hotspot';b.setAttribute('aria-label',name);Object.assign(b.style,{left:x+'px',top:y+'px',width:w+'px',height:h+'px'});b.onclick=handler;controls.append(b);return b;}
function renderControls(){controls.replaceChildren();if(page<0){if(loaded)hotspot('开始游戏',0,0,320,568,()=>{sound(0);changePage(0);});return;}
  ['厨房','农场','商店','其他'].forEach((s,i)=>hotspot(s,i*80,0,80,33,()=>changePage(i)));
  if(page===0){hotspot('选择调味料',2,85,103,47,openIngredients);hotspot('厨房等级 '+(state.kitchenLevel+1),281,33,39,38,()=>{if(E.canUpgradeKitchen(state))confirmBox(`升级至厨房Lv.${state.kitchenLevel+2}需要花费${(state.kitchenLevel+1)*10000}cp。\n你确定要升级吗？`,()=>act(()=>{E.upgradeKitchen(state);sound(7);}));else alertBox('请先将前六种调理用具升至当前厨房等级。');});
    if(state.dirty)hotspot('打扫厨房',277,74,43,40,()=>confirmBox('打扫厨房需要花费40cp。\n你确定要打扫吗？',()=>act(()=>{E.clean(state,now());sound(9);})));
    if(state.duck)[0,1].forEach(i=>hotspot(i?'选择鸭蛋':'选择鸡蛋',0,136+i*36,34,36,()=>act(()=>{state.egg=i;sound(3);})));
    if(state.batch?.eggs.some(e=>!e.collected))hotspot('孵化闹钟 '+(state.alarm?'开启':'关闭'),54,309,38,38,()=>act(()=>{state.alarm=!state.alarm;sound(3);announce(state.alarm?'已开启孵化提示音':'已关闭孵化提示音');}));
    state.toolLevels.forEach((lv,id)=>{if(lv>=0&&id*64-toolScroll>-64&&id*64-toolScroll<320)hotspot('使用'+E.label(E.tool(id)),id*64-toolScroll,436,64,82,()=>requestCook(id));});
    if(state.toolLevels.filter(l=>l>=0).length>5){hotspot('向右滚动厨具',302,478,18,24,()=>{toolScroll=Math.min(192,toolScroll+64);renderControls();});hotspot('向左滚动厨具',0,478,18,24,()=>{toolScroll=Math.max(0,toolScroll-64);renderControls();});}
    state.batch?.eggs.forEach((e,i)=>{if(!e.collected)hotspot(e.status==='egg'?'孵化中的蛋 '+(i+1):'收取'+E.label(E.char(e.egg,e.id))+' '+(i+1),e.x-19,e.y-13,38,42,()=>collectEgg(i));});
  } else if(page===1){hotspot('打开收成表',2-farmScroll,136,90,90,()=>openAlbum());hotspot('整修农场',0,480,86,35,()=>confirmBox(`整修农场需要花费${E.repairCost(state,now())}cp。\n你确定要整修吗？`,()=>act(()=>{E.repair(state,now());sound(10);})));
    hotspot('妖怪村',235-farmScroll,146,65,65,()=>alertBox('妖怪村的活动玩法正在按原版规则还原。'));
    hotspot('神社',355-farmScroll,148,65,65,()=>alertBox('神社的奉纳玩法正在按原版规则还原。'));
    if(farmScroll>0)hotspot('回到农舍',0,420,40,45,()=>{farmScroll=0;renderControls();});
  }
  reportStatus();
}
function reportStatus(){
  if(review)parent.postMessage({type:'chick-status',cp:state.cp,ready:state.batch?.eggs.filter(e=>!e.collected&&e.status==='ready').length??0,eggs:state.batch?.eggs.filter(e=>!e.collected).length??0,clock:new Date(now()).toLocaleTimeString(),page},location.origin);
}
function changePage(index){if(index===page)return;closePanel();closeDialog();page=index;sound(3);if(page===1){const lost=E.checkFarmLoss(state,now());makeWalkers();if(lost){save();alertBox(`脱逃事件：逃走了${lost}只鸡！`);}}if(page===2)openShop();if(page===3)openSettings();music();renderControls();paint();}
function requestCook(id){const info=E.cookInfo(state,id);const replace=state.batch?.eggs.some(e=>!e.collected);const message=replace?`你确定要中断目前的调理，\n然后使用${E.label(E.tool(id))}开始新的调理吗？\n之前花费的CP不会退还。\n本次需要${info.cost}cp。`:`使用${E.label(E.tool(id))}调理，需要花费${info.cost}cp。\n\n你确定要使用吗？`;
  confirmBox(message,()=>act(()=>{const candidate=structuredClone(state);candidate.batch=null;E.startBatch(candidate,id,now());state=candidate;sound(1);}));
}
function collectEgg(index){if(dialogs.children.length||panel)return;const e=state.batch?.eggs[index];if(!e)return;if(E.collect(state,index)){flights.push({e:{...e},x:e.x-30,y:e.y-30,dx:0,dy:0,h:60,alpha:1,phase:0,wait:0});sound(1);save();announce('收取'+E.label(E.char(e.egg,e.id))+'，获得1cp');renderControls();}}
function animateFlights(){for(const f of flights){if(f.phase===0){f.h+=3;f.dy=60-f.h;if(f.h>=72)f.phase=1;}else if(f.phase===1){f.dy-=15;f.h=Math.max(60,f.h-7);if(f.dy<-45){f.phase=2;f.wait=2;}}else if(f.phase===2){if(f.wait-->0)continue;f.dx+=25;f.dy=Math.min(-35,f.dy+3.5);if(f.x+f.dx>=255){f.phase=3;f.wait=2;}}else if(f.phase===3){if(f.wait-->0)continue;f.dy+=30;if(f.y+f.dy>=274)f.phase=4;}else{f.alpha-=.3;f.h=Math.max(30,f.h-3);}}for(let i=flights.length-1;i>=0;i--)if(flights[i].alpha<=0)flights.splice(i,1);}
function closePanel(){panel='';panels.replaceChildren();renderControls();}
function closeDialog(){dialogs.replaceChildren();dialogAction=null;lastFocused?.focus();}
function confirmBox(message,fn,onlyOK=false){lastFocused=document.activeElement;dialogs.innerHTML=`<div class="modal-shade"></div><section class="confirm paper" role="dialog" aria-modal="true" aria-label="确认"><p></p><footer>${onlyOK?'':'<button class="cream" data-no>不要</button>'}<button class="cream" data-yes>${onlyOK?'好':'要'}</button></footer></section>`;dialogs.querySelector('p').textContent=message;dialogAction=fn;dialogs.querySelector('[data-no]')?.addEventListener('click',()=>{sound(2);closeDialog();});dialogs.querySelector('[data-yes]').onclick=()=>{const action=dialogAction;closeDialog();sound(1);action?.();};dialogs.querySelector('button').focus();}
function alertBox(message){confirmBox(message,null,true);}
function showPanel(title,body,classes=''){panel=title;sound(4);panels.innerHTML=`<section class="panel paper ${classes}" role="dialog" aria-label="${title}"><button class="close" aria-label="关闭">×</button><h2>${title}</h2>${body}</section>`;panels.querySelector('.close').onclick=closePanel;panels.querySelector('.close').focus();}
function openIngredients(){let draft=[...state.selected];const max=Math.min(3,state.kitchenLevel+1);const draw=()=>{showPanel('调味料',`<p class="counter">${draft.length} / ${max}</p><div class="scroll ingredients-grid">${Object.entries(state.ingredients).filter(([,n])=>n>0).map(([id,n])=>`<button class="ingredient ${draft.includes(+id)?'selected':''}" data-id="${id}" aria-pressed="${draft.includes(+id)}" aria-label="${E.label(E.ingredient(+id))}"><img src="${imgPath(2,id)}" alt=""><small>${n}</small><div class="ingredient-title">${E.label(E.ingredient(+id))}</div></button>`).join('')||'<p class="empty">没有调味料，可以去商店购买。</p>'}</div><footer><span></span><button class="orange" data-ok>OK</button><span></span></footer>`);panels.querySelectorAll('[data-id]').forEach(b=>b.onclick=()=>{const id=+b.dataset.id;if(draft.includes(id))draft=draft.filter(i=>i!==id);else if(draft.length<max)draft.push(id);else{sound(13);return;}sound(3);draw();});panels.querySelector('[data-ok]').onclick=()=>{state.selected=draft;save();closePanel();};};draw();}
function openShop(){panel='商店';const rows=shopTab===0?DATA.tools[1].map(t=>{const lv=state.toolLevels[t.id],allowed=E.canBuyTool(state,t.id),cost=t[`lv_${lv+1}_buy_cp`];return `<div class="item"><img src="${imgPath(1,t.id,Math.min(2,Math.max(0,lv+1)))}" alt=""><div class="info"><strong>${E.label(t)} Lv.${Math.min(3,lv+2)}</strong><br>${lv===2?'已达到最高等级':allowed?`${cost}cp`:'尚未解锁'}<br><small>${lv>=0?'持有 Lv.'+(lv+1):'未购买'}</small></div><button class="orange" data-buy-tool="${t.id}" ${!allowed?'disabled':''}>${lv<0?'购买':'升级'}</button></div>`;}).join(''):shopTab===1?E.availableIngredients(state).map(id=>{const t=E.ingredient(id);return `<div class="item"><img src="${imgPath(2,id)}" alt=""><div class="info"><strong>${E.label(t)}</strong><br>${t.buy_cp}cp<small>　持有：${state.ingredients[id]??0}</small></div><button class="orange" data-buy-ingredient="${id}">购买</button></div>`;}).join(''):`<div class="item"><img src="/assets/png/Egg/egg_0_0_0.png" alt=""><div class="info">鸡蛋<br>已拥有</div></div><div class="item"><img src="/assets/png/Egg/egg_1_0_0.png" alt=""><div class="info">鸭蛋<br>${state.duck?'已拥有':'2500cp · 原版需登录解锁'}</div><button class="orange" data-duck ${state.duck?'disabled':''}>${state.duck?'已拥有':'详情'}</button></div>`;
  showPanel('商店',`<div class="subtabs">${['调理用具','调味料','其他'].map((n,i)=>`<button data-shop-tab="${i}" class="${i===shopTab?'active':''}">${n}</button>`).join('')}</div><div class="scroll rows">${rows}</div>`,'store-panel');panels.querySelector('.close').style.display='none';
  panels.querySelectorAll('[data-shop-tab]').forEach(b=>b.onclick=()=>{shopTab=+b.dataset.shopTab;openShop();});
  panels.querySelectorAll('[data-buy-tool]').forEach(b=>b.onclick=()=>{const id=+b.dataset.buyTool,cost=E.tool(id)[`lv_${state.toolLevels[id]+1}_buy_cp`];confirmBox(`${E.label(E.tool(id))}\n需要花费${cost}cp。\n你确定要购买吗？`,()=>act(()=>{E.buyTool(state,id);sound(7);openShop();}));});
  panels.querySelectorAll('[data-buy-ingredient]').forEach(b=>b.onclick=()=>{const id=+b.dataset.buyIngredient;confirmBox(`${E.label(E.ingredient(id))} ×1\n需要花费${E.ingredient(id).buy_cp}cp。\n你确定要购买吗？`,()=>act(()=>{E.buyIngredient(state,id);sound(7);openShop();}));});
  panels.querySelector('[data-duck]')?.addEventListener('click',()=>alertBox('原版鸭蛋需要登录 iDT 并上传纪录后解锁。\n本地复刻尚未接入该服务。'));
}
function openAlbum(){selection={};renderAlbum();}
function renderAlbum(){const list=DATA.characters[albumEgg],sum=Object.entries(selection).reduce((v,[k,n])=>v+E.char(...k.split(':').map(Number)).cp_1*n,0);const scrollTop=panels.querySelector('.rows')?.scrollTop??0;
  showPanel('收成表',`<div class="subtabs"><button data-egg="0" class="${albumEgg===0?'active':''}">鸡</button><button data-egg="1" class="${albumEgg===1?'active':''}">鸭</button></div><div class="scroll rows">${list.map(c=>{const k=E.key(albumEgg,c.id),known=state.total[k]>0;return `<div class="item ${known?'':'unknown'}"><button class="portrait" data-char="${c.id}" aria-label="${known?E.label(c):'未发现品种 '+(c.id+1)}"><img src="${charPath(albumEgg,c.id)}" alt="">${known?'':'<span class="question">?</span>'}</button><div class="info"><strong>${albumEgg?'D':'C'}${String(c.id+1).padStart(2,'0')} ${known?E.label(c):'???'}</strong><br>价格：${known?c.cp_1:'-'}<br>数量：${known?state.farm[k]??0:'-'}</div>${known?`<div class="count"><button class="orange" data-minus="${k}" aria-label="减少${E.label(c)}卖出数量">−</button><output>${selection[k]??0}</output><button class="orange" data-plus="${k}" aria-label="增加${E.label(c)}卖出数量">+</button></div>`:''}</div>`;}).join('')}</div><footer><button class="orange" data-sell>卖出</button><span>合计：${sum} cp</span></footer>`,'album');
  panels.querySelector('.rows').scrollTop=scrollTop;panels.querySelectorAll('[data-egg]').forEach(b=>b.onclick=()=>{albumEgg=+b.dataset.egg;selection={};panels.querySelector('.rows').scrollTop=0;renderAlbum();});
  panels.querySelectorAll('[data-plus]').forEach(b=>b.onclick=()=>{const k=b.dataset.plus;selection[k]=Math.min(state.farm[k]??0,(selection[k]??0)+1);renderAlbum();});
  panels.querySelectorAll('[data-minus]').forEach(b=>b.onclick=()=>{const k=b.dataset.minus;selection[k]=Math.max(0,(selection[k]??0)-1);renderAlbum();});
  panels.querySelectorAll('[data-char]').forEach(b=>b.onclick=()=>{const id=+b.dataset.char;if(state.total[E.key(albumEgg,id)])showCharacter(albumEgg,id);else sound(13);});
  panels.querySelector('[data-sell]').onclick=()=>{if(sum<=0){alertBox('请先选择要卖出的数量。');return;}confirmBox(`合计：${sum}cp\n你确定要卖出吗？`,()=>act(()=>{E.sell(state,selection);selection={};sound(8);makeWalkers();renderAlbum();}));};
}
function showCharacter(egg,id){const c=E.char(egg,id),k=E.key(egg,id);showPanel(`${egg?'D':'C'}${String(id+1).padStart(2,'0')} ${E.label(c)}`,`<img class="portrait-large" src="${charPath(egg,id)}" alt="${E.label(c)}"><p>价格：${c.cp_1} cp</p><p>孵化数：${state.total[k]??0}</p><p>数量：${state.farm[k]??0}</p><footer><span></span><button class="orange" data-back>返回</button><span></span></footer>`,'detail');panels.querySelector('[data-back]').onclick=renderAlbum;}
function openSettings(){panel='其他';panels.innerHTML=`<div class="settings"><button class="paper" data-manual>游戏说明</button><div class="row"><span>背景音乐</span><button class="orange" data-music>${state.music?'ON':'OFF'}</button></div><div class="row"><span>音效</span><button class="orange" data-sound>${state.sound?'ON':'OFF'}</button></div><button class="paper" data-save>保存游戏</button><button class="paper" data-title>回到标题画面</button></div>`;panels.querySelector('[data-manual]').onclick=()=>openManual(0);panels.querySelector('[data-music]').onclick=()=>{state.music=!state.music;save();music();openSettings();};panels.querySelector('[data-sound]').onclick=()=>{state.sound=!state.sound;save();sound(3);openSettings();};panels.querySelector('[data-save]').onclick=()=>{save();alertBox('游戏进度已保存在此浏览器。');};panels.querySelector('[data-title]').onclick=()=>{save();page=-1;closePanel();music();renderControls();};}
function openManual(i){const files=i===0?['manual000_cn0.jpg','manual000_cn1.jpg']:i===1?['manual001_cn.jpg']:['manual002_cn.jpg'];showPanel('游戏说明',`<div class="subtabs">${['厨房','农场','商店'].map((n,j)=>`<button data-manual-tab="${j}" class="${j===i?'active':''}">${n}</button>`).join('')}</div><div class="scroll">${files.map(f=>`<img src="/assets/png/Manual/${f}" alt="原版${['厨房','农场','商店'][i]}操作说明">`).join('')}</div>`,'manual');panels.querySelector('.close').onclick=openSettings;panels.querySelectorAll('[data-manual-tab]').forEach(b=>b.onclick=()=>openManual(+b.dataset.manualTab));}
let pointer=null;
controls.addEventListener('pointerdown',ev=>{const r=game.getBoundingClientRect(),x=(ev.clientX-r.left)/r.width*320,y=(ev.clientY-r.top)/r.height*568;pointer={x,y,last:x,drag:false};if(page===0&&y>168&&y<309)hitEggs(x,y);});
controls.addEventListener('pointermove',ev=>{if(!pointer||panel||dialogs.children.length)return;const r=game.getBoundingClientRect(),x=(ev.clientX-r.left)/r.width*320,y=(ev.clientY-r.top)/r.height*568;if(Math.abs(x-pointer.x)>4)pointer.drag=true;if(page===0&&y>168&&y<309)hitEggs(x,y);if(page===1&&pointer.y>220&&pointer.y<470){farmScroll=Math.max(0,Math.min(510,farmScroll-(x-pointer.last)));}pointer.last=x;});
window.addEventListener('pointerup',()=>{if(page===1&&pointer?.drag)renderControls();pointer=null;});
function hitEggs(x,y){const list=state.batch?.eggs??[];for(let i=list.length-1;i>=0;i--){const e=list[i];if(!e.collected&&Math.abs(e.x-x)<19&&Math.abs(e.y+6-y)<21){collectEgg(i);break;}}}
document.addEventListener('keydown',e=>{if(e.key==='Escape'){if(dialogs.children.length)closeDialog();else if(panel&&page<2)closePanel();else if(page===3)openSettings();}if(e.key==='Tab'&&dialogs.children.length){const b=[...dialogs.querySelectorAll('button')],first=b[0],last=b.at(-1);if(e.shiftKey&&document.activeElement===first){e.preventDefault();last.focus();}else if(!e.shiftKey&&document.activeElement===last){e.preventDefault();first.focus();}}});
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
    save();renderControls();
  }
});
const primary=[...DATA.images.filter(p=>/MainMenu|MainGame|Tool0|Tool1|Egg/.test(p)),...Array.from({length:20},(_,i)=>charPath(0,i).slice(1))];
const loading=document.createElement('p');loading.className='loading';loading.textContent='正在载入…';controls.append(loading);
await Promise.allSettled([document.fonts.load('20px Chick'),...primary.map(p=>new Promise(resolve=>{const im=loadImage('/'+p);if(im.complete)resolve();else{im.onload=resolve;im.onerror=resolve;}}))]);
loaded=true;renderControls();paint();
function advanceClock(){const real=Date.now();virtualNow+=(real-previousReal)*speed;previousReal=real;}
setInterval(()=>{advanceClock();tick++;const events=E.updateBatch(state,now());if(state.alarm&&state.batch&&!state.batch.alarmed&&now()>=state.batch.ends){state.batch.alarmed=true;sound(5);announce('这一批鸡宝已经孵化，请及时收取。');save();}if(events.length){if(page===0)events.forEach(s=>sound(s==='break'?11:s==='duck'?14:12));save();renderControls();}if(tick%60===0){for(const e of state.batch?.eggs??[])if(e.status==='ready'&&Math.random()<.5)e.flipped=!e.flipped;}
  animateFlights();for(const w of walkers){w.turn+=Math.floor(Math.random()*2);if(w.turn>=30){w.turn=0;w.dir=Math.random()<.5?-1:1;}}if(tick%10===0)reportStatus();paint();
},100);
