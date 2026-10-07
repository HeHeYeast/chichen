import {layoutRemaster,decorateControl,updateCare,registerRemasterArt} from './remaster.js';
registerRemasterArt();
import {resolveSpecies} from '/web/content-registry.js';
import {interfaceIcon} from '/web/ui-icons.js';
import {execute,acquireWriter} from '/web/game-commands.js';
import {createWorkshopUI} from '/web/workshop-ui.js';
import {createRegionalUI} from '/web/regional-ui.js';
import {createBusinessUI} from '/web/business-ui.js';
import {createOrderUI} from '/web/order-ui.js';
import {createCollectionsUI} from '/web/collections-ui.js';
import {createRegularUI} from '/web/regular-ui.js';
import {createProjectUI} from '/web/project-ui.js';
import {renderDesk} from '/web/desk-ui.js';
import {createHarvestAllocationUI} from '/web/harvest-allocation-ui.js';
import {createBookUI} from '/web/book-ui.js';
import {newOperationsEnabled} from '/web/rollback-policy.js';
import {uiPreference,setUiPreference} from '/web/ui-preferences.js';
import {reconcileProgress} from '/web/regulars.js';
import {renderSkillFeedback} from '/web/skill-feedback.js';
import {cookingCandidates} from '/web/candidate-query.js';
import {batchModeView} from '/web/batch-mode-view.js';
import {observationInfo} from '/web/knowledge.js';
import {collectedTotal,rank,effects} from '/web/progression.js';
import { GAME_DATA as DATA, TOOL_SCROLL_MAX } from '/web/content-pack.js';
import {beginToolDrag,moveToolDrag,settleToolDrag,toolScrollFor} from '/web/tool-strip.js';
import {cookingIngredients} from '/web/cooking-query.js';
import {createSaveStore,makeBackup,parseBackup} from '/web/save-store.js';
import {createPlatform} from '/web/native-platform.js';
import * as E from '/web/engine.js';
import {farmDisplay,timeZone} from '/web/farm.js';
import {createRenderer} from './scene.js';
import {NAV,LAYOUT as L,RECT,navRect,toolRect,duckRect,contains,MOTION,fitViewport,setViewportHeight,farmRect} from '/web/theme.js';
import {characterImage,toolImage} from '/web/catalog.js';
import {originalPortraitFrame} from '/web/portrait-frames.js';
import {productionCharacter} from '/web/production-art.js';
import {resolveSprite,spriteSVG,ASSET_FILES} from '/web/art/manifest.js';
import {KITCHEN_STAGES,kitchenStage,kitchenBackgroundParts} from '/web/kitchen-stages.js';
import {createCollectionUI} from '/web/collection-ui.js';
import {createRecipeBookUI} from '/web/recipe-book-ui.js';
import {prepareDiscoveredRecipe} from '/web/recipe-book.js';
import {createShopUI,ingredientPortrait} from '/web/shop-ui.js';
import {createSettingsUI} from '/web/settings-ui.js';
import {createActivitiesUI} from '/web/activities-ui.js';
import {claimActivity} from '/web/legacy-activities.js';
import {createShrineUI} from '/web/shrine-ui.js';
import {shrineBook,claimShrineGoal,giftRecipe,prepareGiftRecipe} from '/web/shrine.js';
import {createJournalUI} from '/web/journal-ui.js';
import {plannedSeasonalRecipe,claimSeasonalChapter,SEASONAL_CHARACTERS} from '/web/seasonal-pack.js';
import {calendarNotice} from '/web/discovery-calendar.js';
import {FARM_ART_FILES,FARM_ACTIONS,FARM_RECT,FARM_WORLD} from '/web/farm-theme.js';
const game=document.querySelector('#game'),canvas=document.querySelector('#scene'),ctx=canvas.getContext('2d');
const sceneStage=document.querySelector('#scene-stage'),mainNav=document.querySelector('#main-nav');
const quickActions=document.createElement('nav');quickActions.id='quick-actions';quickActions.setAttribute('aria-label','厨房快捷入口');sceneStage.append(quickActions);
const controls=document.querySelector('#controls'),panels=document.querySelector('#panels'),dialogs=document.querySelector('#dialog-layer');
const review=true; // This preview never opens the production save.
const storageKey=review?'chick-kitchen-remaster-lv2-v1':'chick-kitchen-remaster-lv2-v1';
const platform=createPlatform({disabled:review});
const writer=await acquireWriter({key:storageKey,native:!!platform.bridge,isolated:review});
const saveStore=createSaveStore({writer,storage:{getItem:key=>localStorage.getItem(key),setItem:(key,value)=>localStorage.setItem(key,value)},key:storageKey,native:platform.bridge});
const initialSave=review?{state:E.readSave(localStorage,storageKey)}:saveStore.load();
let recoveryError=initialSave.error??(!writer.writable?writer.reason:''),lastSaveError='',nativeKitchenPending=false;

let settingsReturnPage=0,shopReturnPage=0,shopReturnAction=null;
let state=initialSave.state??E.freshState(),page=-1,loaded=false,panel='',toolScroll=0,farmScroll=0,clockOffset=0,speed=1,virtualNow=Date.now(),previousReal=Date.now(),tick=0,dialogAction=null,lastFocused=null;
let committedState=structuredClone(state);
if(initialSave.state&&!recoveryError)E.resume(state,Math.max(Date.now(),state.clock.logicalAt));
const images=new Map(),flights=[],feedbacks=[],walkers=[];
let pointer=null,keyboardPress=null,pressedId='',entryInputBlockedUntil=0;
const motionPreference=window.matchMedia('(prefers-reduced-motion: reduce)');
let reducedMotion=motionPreference.matches;
motionPreference.addEventListener('change',event=>{reducedMotion=event.matches;});
const now=()=>Math.max(Math.floor(virtualNow+clockOffset),state.clock?.logicalAt??0);
let cleaningDialog=null;
const imgPath=toolImage;
const charPath=characterImage;
const sourcePath=p=>'/assets/png/'+p;
const soundNames=['start','yes','no','click','open','alert0','alert1','buy','sell','clean','fix','break','chick0','error','duck0'];
const sounds=soundNames.map((s,i)=>new Audio(`/res/raw/se${String(i).padStart(3,'0')}_${s}.mp3`));
const bgm=new Audio('/assets/music/bgm/bgm_000.mp3');bgm.loop=true;bgm.volume=.35;
function sound(id){if(!state.sound)return;const a=sounds[id].cloneNode();a.volume=.65;a.play().catch(()=>{});}
function music(){if(!state.music||page<0){bgm.pause();return;}const id=page===1?1:page===2?2:0;const src=`/assets/music/bgm/bgm_00${id}.mp3`;if(!bgm.src.endsWith(src))bgm.src=src;bgm.play().catch(()=>{});}
function save(){
  if(recoveryError)return false;
  try{
    const candidate=structuredClone(state);
    const committed=execute({state:committedState,store:review?null:saveStore,command:{type:'gameplay',candidate},now:Math.floor(virtualNow+clockOffset),reduce:draft=>{Object.assign(draft,candidate);draft.meta={...structuredClone(committedState.meta),factSeq:candidate.meta.factSeq};}});
    state=committed.state;committedState=structuredClone(state);
    if(review)localStorage.setItem(storageKey,JSON.stringify(state));
    lastSaveError='';return true;
  }catch(error){
    state=structuredClone(committedState);
    const firstFailure=!lastSaveError;lastSaveError=error.message;
    if(['ACK_UNKNOWN','REVISION_CONFLICT','SAVE_LOCKED'].includes(error.code))recoveryError=error.message;
    const detail=error.code==='SAVE_FAILED'?error.cause?.message??error.message:error.message;
    announce('进度暂未保存。请到设置导出备份：'+detail);
    if(firstFailure&&loaded&&!dialogs.children.length)alertBox('进度暂未保存，请保留游戏并到设置导出备份。\n'+detail);
    return false;
  }
}
function announce(message){document.querySelector('#announcement').textContent=message;}
function act(fn){
  const result=commitProgress(draft=>{
    const published=state;state=draft;
    try{fn();if(state!==draft)Object.assign(draft,state);return {ok:true};}
    finally{state=published;}
  });
  if(!result){refreshPanel();renderControls();return false;}return true;
}
function refreshPanel(){
  if(panels.querySelector('.book-screen'))bookUI.refresh();
  else if(panels.querySelector('.business-screen'))businessUI.refresh();
  else if(panels.querySelector('.orders-screen'))orderUI.refresh();
  else if(panels.querySelector('.books-screen'))collectionsUI.refresh();
  else if(panels.querySelector('.regulars-screen'))regularUI.refresh();
  else if(panels.querySelector('.projects-screen'))projectUI.refresh();
  else if(panels.querySelector('.regional-screen'))regionalUI.refresh();
  else if(panels.querySelector('.workshop-screen'))workshopUI.refresh();
  else if(panels.querySelector('.cookbook-screen'))recipeBookUI.refresh();
  else if(panels.querySelector('.journal-screen'))journalUI.refresh();
  else if(panels.querySelector('.shrine-screen'))shrineUI.refresh();
  else if(panels.querySelector('.activities-screen'))activitiesUI.refresh();
  else if(panels.querySelector('.shop-screen'))shopUI.openShop();
  else if(panels.querySelector('.settings-screen'))settingsUI.openSettings();
  else collectionUI.refresh();
}
function loadImage(path){if(images.has(path))return images.get(path);const image=new Image();image.src=path;images.set(path,image);return image;}
function image(path,x,y,w,h,flip=false,alpha=1){
  const sprite=resolveSprite(path),im=loadImage(sprite.file);if(!im.complete||!im.naturalWidth)return;
  ctx.save();ctx.globalAlpha=alpha;ctx.translate(flip?x+w:x,y);if(flip)ctx.scale(-1,1);
  let dx=0,dy=0,dw=w,dh=h;
  if(sprite.placement){const [px,py,pw,ph]=sprite.placement;dx=w*px/120;dy=h*py/120;dw=w*pw/120;dh=h*ph/120;}
  if(sprite.frame){const [sx,sy,sw,sh]=sprite.frame;if(sprite.fit){const scale=Math.min(w/sw,h/sh);dw=sw*scale;dh=sh*scale;dx=(w-dw)/2;dy=(h-dh)/2;}
    if(sprite.clip){ctx.beginPath();sprite.clip.forEach(([px,py],i)=>{const point=[dx+(px-sx)*dw/sw,dy+(py-sy)*dh/sh];if(i)ctx.lineTo(...point);else ctx.moveTo(...point);});ctx.closePath();ctx.clip();}
    ctx.drawImage(im,sx,sy,sw,sh,dx,dy,dw,dh);
  }else ctx.drawImage(im,0,0,w,h);
  ctx.restore();
}
function toolPortrait(id,lv){
  const path=imgPath(1,id,lv),sprite=resolveSprite(path);
  return sprite.frame?spriteSVG(sprite):`<img src="${path}" alt="">`;
}
function characterPortrait(egg,id){
  const production=productionCharacter(egg,id,'portrait');
  if(production)return `<img class="sprite-art" src="${production}" alt="">`;
  const path=charPath(egg,id),resolved=resolveSprite(path),sprite=resolved.frame?resolved:originalPortraitFrame(egg,id)??resolved;
  if(!sprite.frame)return `<img src="${path}" alt="">`;
  return `<svg class="sprite-art" viewBox="${sprite.frame.join(' ')}" aria-hidden="true"><image href="${sprite.file}" width="${sprite.size[0]}" height="${sprite.size[1]}"/></svg>`;
}
function round(x,y,w,h,r,fill,stroke=null,line=1){ctx.beginPath();ctx.roundRect(x,y,w,h,r);if(fill){ctx.fillStyle=fill;ctx.fill();}if(stroke){ctx.strokeStyle=stroke;ctx.lineWidth=line;ctx.stroke();}}
const renderer=createRenderer(ctx,image);
let farmZone=-1,shrineReady=false;
function makeWalkers(){walkers.splice(0,walkers.length,...farmDisplay(state,now()));farmZone=timeZone(now());}
// A place shows one dot only for a real waiting result (never for locks or goals).
function navBadges(){
  if(!loaded||!state?.expansion)return {};
  const trip=state.progress?.trip,regulars=Object.values(state.expansion.regulars??{});
  return {5:!!businessUI.unreadReport()||regulars.some(r=>r.pendingStage),6:!!trip&&now()>=trip.endAt};
}
function paint(){updateCare(state,now());if(page===1&&farmZone!==timeZone(now()))makeWalkers();renderer.paint(state,{page,loaded,navBadges:navBadges(),externalNavigation:true,recovery:Boolean(recoveryError),version:platform.info.version,now:now(),tick,toolScroll,toolPosition:pointer?.kind==='tools'?pointer.position:toolScroll,farmScroll,flights,feedbacks,walkers,pressedId,reducedMotion,shrineReady});}
function hotspot(name,rect,handler,id=name){
  const b=document.createElement('button');b.className='hotspot';b.setAttribute('aria-label',name);b.dataset.controlId=id;
  Object.assign(b.style,{left:rect.x+'px',top:rect.y+'px',width:rect.w+'px',height:rect.h+'px'});
  b.hitRect=rect;b.activate=handler;
  // Pointer gestures are resolved on release below. A detail-zero click remains
  // available to keyboard / assistive technology without collecting twice.
  b.onclick=event=>{if(event.detail!==0||controlBlocked(b))return;pressedId='';handler();};
  decorateControl(b,id,state);controls.append(b);return b;
}
function findControl(id){return [...controls.querySelectorAll('.hotspot')].find(b=>b.dataset.controlId===id);}
function controlBlocked(button){return entryInputBlocked()||dialogs.children.length>0||Boolean(panel&&button.hitRect.y>=58&&button.hitRect.y<L.navY-4);}
function updateControlFocus(){panels.inert=dialogs.children.length>0;mainNav.inert=dialogs.children.length>0;quickActions.hidden=page!==0||!!panel;quickActions.inert=dialogs.children.length>0||entryInputBlocked();for(const b of mainNav.querySelectorAll('button'))b.tabIndex=dialogs.children.length||entryInputBlocked()?-1:0;document.querySelector('#desk').inert=dialogs.children.length>0;for(const b of controls.querySelectorAll('.hotspot'))b.tabIndex=controlBlocked(b)?-1:0;}
function startGame(){
  // The cover overlaps the bottom navigation. Consume the remainder of a
  // rapid tap sequence before those newly visible buttons can act on it.
  entryInputBlockedUntil=performance.now()+500;
  sound(0);enterKitchen();
  // The old shop tab moved into the kitchen: tell a returning player once.
  setTimeout(()=>{if(page===0&&!uiPreference('seenSupplyMove')&&Object.keys(state.total??{}).length>1){setUiPreference('seenSupplyMove',true);toast('补给搬到厨房了，缺料时也能直接打开。',4500);}},900);
  setTimeout(updateControlFocus,500);
}
function renderControls(){
  layoutRemaster();
  const focusedId=document.activeElement?.dataset?.controlId;
  controls.replaceChildren();quickActions.replaceChildren();quickActions.hidden=page!==0||!!panel;
  if(page<0){mainNav.replaceChildren();if(loaded)hotspot(recoveryError?'恢复存档':'开始游戏',RECT.start,()=>{if(recoveryError){openRecovery();return;}startGame();},'start');return;}
  renderNavigation();
  hotspot('设置',RECT.settings,()=>changePage(3),'settings');
  if(page!==0)hotspot('查看厨房等级',RECT.level,()=>{changePage(0);openKitchenUpgrade();},'level');
  if(page===0){
    hotspot('选择调味料',RECT.ingredient,openIngredients,'ingredient');
    hotspot('厨房等级 '+(state.kitchenLevel+1),RECT.level,openKitchenUpgrade,'level');
    hotspot('打扫厨房',RECT.clean,openCleaning,'clean');
    if(state.duck)[0,1].forEach(i=>hotspot(i?'选择鸭蛋':'选择鸡蛋',{...duckRect(i),y:duckRect(i).y+L.eggOffset},()=>act(()=>{state.egg=i;delete state.events.seasonalRecipe;sound(3);}), 'duck:'+i));
    if(state.batch?.eggs.some(e=>!e.collected))hotspot('孵化闹钟 '+(state.alarm?'开启':'关闭'),RECT.alarm,()=>toggleHatchAlarm(), 'alarm');
    for(let slot=0;slot<4;slot++){const id=toolScroll+slot;hotspot((state.toolLevels[id]>=0?'使用':'购买')+E.label(E.tool(id)),toolRect(slot),()=>{if(state.toolLevels[id]>=0)requestCook(id);else{shopUI.setTab(0);changePage(2);}},'tool:'+id);}
    for(const [id,label,icon,action] of [
      ['supply','商店','shop',()=>{shopUI.setTab(0);changePage(2);}],
      ['inventory','仓库','inventory',openAlbum],
      ['workshop','手艺','workshop',()=>workshopUI.open()],
      ['help','帮助','help',()=>settingsUI.openManual()],
    ]){const b=document.createElement('button');b.dataset.controlId=id;b.className=id+'-launch';b.setAttribute('aria-label',id==='supply'?'补给 · 小卖部':label);b.innerHTML=interfaceIcon(icon)+`<span>${label}</span>`;b.onclick=()=>{if(!entryInputBlocked()&&!dialogs.children.length)action();};quickActions.append(b);}
    hotspot('向右滚动厨具',RECT.next,()=>{toolScroll=Math.min(TOOL_SCROLL_MAX,toolScroll+1);renderControls();},'next').disabled=toolScroll===TOOL_SCROLL_MAX;
    hotspot('向左滚动厨具',RECT.prev,()=>{toolScroll=Math.max(0,toolScroll-1);renderControls();},'prev').disabled=toolScroll===0;
    state.batch?.eggs.forEach((e,i)=>{if(!e.collected)hotspot(e.status==='egg'?'孵化中的蛋 '+(i+1):'收取'+E.label(E.char(e.egg,e.id))+' '+(i+1),eggHitRect(e),()=>collectEgg(i),'egg:'+i);});
    if(state.batch?.eggs.every(e=>e.collected)){const b=hotspot('安排本锅收成',{x:64,y:250+L.eggOffset,w:192,h:44},()=>harvestAllocationUI.open(),'harvest-allocation');b.textContent='本锅已收完 · 安排收成';b.classList.add('harvest-allocation-launch');}
  }else if(page===1){
    const shrine=shrineBook(state,now());shrineReady=shrine.gift.available||shrine.goals.some(goal=>goal.available);
    for(const entrance of FARM_ACTIONS){
      const rect={...farmRect(entrance.rect),x:entrance.rect.x-farmScroll};
      if(rect.x+rect.w<=0||rect.x>=L.width)continue;
      hotspot(entrance.label,rect,()=>{if(entrance.action==='harvest')openAlbum();else if(entrance.action==='shop'){shopUI.setTab(0);changePage(2);}else if(entrance.action==='activities')openActivities();else if(entrance.action==='display')collectionsUI.open({tab:'mementos'});},entrance.id);
    }
    hotspot('打开收成账本',{...FARM_RECT.harvest,y:FARM_RECT.harvest.y+L.extra},openAlbum,'farm:harvest');
    hotspot('前往神社求签',{...FARM_RECT.shrine,y:FARM_RECT.shrine.y+L.extra},()=>openShrine(),'farm:fortune');
    hotspot('整修农场',{...FARM_RECT.repair,y:FARM_RECT.repair.y+L.extra},()=>confirmBox(`农场完好度 ${E.farmHP(state,now())}%\n整修需要 ${E.repairCost(state,now())} CP。\n确认后恢复农场完好度。`,()=>act(()=>{E.repair(state,now());sound(10);announce('农场已经整修好了。');})), 'farm:repair');
  }
  if(pressedId&&!findControl(pressedId))pressedId='';
  if(focusedId)(findControl(focusedId)??[...quickActions.querySelectorAll('button')].find(b=>b.dataset.controlId===focusedId))?.focus({preventScroll:true});
  updateCleaningStatus();updateControlFocus();reportStatus();
}
function renderNavigation(){
  const focused=document.activeElement?.dataset?.controlId,badges=navBadges();
  mainNav.replaceChildren();
  for(const n of NAV){const b=document.createElement('button');b.dataset.controlId='nav:'+n.id;b.tabIndex=entryInputBlocked()?-1:0;b.setAttribute('aria-label',n.title);const active=page===n.id||n.id===0&&(page===2||page===3);b.setAttribute('aria-current',active?'page':'false');b.innerHTML=interfaceIcon(({0:'kitchen',1:'farm',5:'shop',6:'explore',4:'book'})[n.id])+`<span>${n.title}</span>`;
    if(badges[n.id]){const dot=document.createElement('span');dot.className='nav-result';dot.setAttribute('aria-label','有新结果');b.append(dot);}
    b.onclick=()=>{if(entryInputBlocked()||dialogs.children.length)return;if(n.id===5&&page!==5&&businessUI.unreadReport())tradeView={kind:'business'};changePage(n.id);};mainNav.append(b);
  }
  if(focused?.startsWith('nav:'))mainNav.querySelector(`[data-control-id="${focused}"]`)?.focus({preventScroll:true});
}
function reportStatus(){
  if(review)parent.postMessage({type:'chick-status',loaded,cp:state.cp,kitchenLevel:state.kitchenLevel+1,dirty:state.dirty,ready:state.batch?.eggs.filter(e=>!e.collected&&['ready','hatching'].includes(e.status)).length??0,eggs:state.batch?.eggs.filter(e=>!e.collected).length??0,clock:new Date(now()).toLocaleTimeString(),page},location.origin);
}
// 生意 remembers its last sub-page; 寻访 always opens on its trip/region page.
let tradeView={kind:'business'},panelReturn=null;
function showTrade(){const v=tradeView;if(v.kind==='orders')orderUI.resume();else if(v.kind==='regulars')v.id?regularUI.open(v.id):regularUI.resume();else if(v.kind==='projects')v.id?projectUI.open(v.id):projectUI.resume();else businessUI.open();}
// Order → method detour: legacy species open the recipe book, regional ones their
// region record; both come back to the same order sheet.
function openRecipeFromOrder(key){
  const c=resolveSpecies(key),back=()=>openTrade('orders');
  if(c?.region){panelReturn=back;regionalUI.open({regionId:c.region,tab:'record'});return;}
  recipeBookUI.open({key,back,label:'返回订单'});
}
function openTrade(kind,id=null){tradeView={kind,id};if(page!==5)changePage(5);else showTrade();}
function openExplore(){if(page!==6)changePage(6);else regionalUI.open();}
function changePage(index){
  dismissToast();if(page===2&&index!==2)shopReturnAction=null;
  if(index===page){
    if(!panel)return;
    closeDialog();closePanel();
    if(index===2)openShop();else if(index===3)openSettings();else if(index===4)bookUI.open();else if(index===5){tradeView={kind:'business'};showTrade();}else if(index===6)regionalUI.open();
    return;
  }
  if(index===3)settingsReturnPage=page<0?0:page;
  if(index===2){const origin=page===3?settingsReturnPage:page;if(origin!==2)shopReturnPage=origin<0?0:origin;}
  const fromCover=page<0;resetInput();closePanel();closeDialog();panelReturn=null;page=index;sound(3);if(fromCover)resize();
  if(page===1){const result=commitProgress(s=>({lost:E.checkFarmLoss(s,now())}));makeWalkers();if(result?.lost)alertBox(`脱逃事件：逃走了${result.lost}只鸡！`);}
  if(page===2)openShop();if(page===3)openSettings();if(page===4)bookUI.open();if(page===5)showTrade();if(page===6)regionalUI.open();
  music();renderControls();paint();updateDesk();if(page===0)setTimeout(maybeDiscoveryNotice,180);
}
function requestCook(id){
  try{
    const info=E.cookInfo(state,id,now()),preview=cookingCandidates(state,id,now()),remaining=state.batch?.eggs.filter(e=>!e.collected)??[];
    const signature=()=>JSON.stringify({info:E.cookInfo(state,id,now()),preview:cookingCandidates(state,id,now()),protection:state.progress.protection,egg:state.egg});
    const quote=signature(),ingredients=preview.ingredients.map(i=>E.label(E.ingredient(i))).join('、')||'不放调味料';
    const message=`${remaining.length?'还有 '+remaining.length+' 只未收取，重开会放弃它们。\n':''}${E.label(E.tool(id))} · ${state.egg?'鸭蛋':'鸡蛋'}\n${ingredients}\n调理 ${Math.floor(info.minutes)}分${Math.round(info.minutes%1*60)?Math.round(info.minutes%1*60)+'秒':''} · 花费 ${info.cost} CP\n每枚破壳后保鲜 ${info.freshMinutes} 分钟${id===0?'（保温灯不焦化）':''}`;
    confirmBox(message,()=>{
      if(signature()!==quote){requestCook(id);announce('搭配或条件已变化，请核对新的摘要并再次确认。');return;}
      const replicate=state.progress.replicate;
      if(act(()=>{const candidate=structuredClone(state);candidate.batch=null;E.startBatch(candidate,id,now());state=candidate;sound(1);})){
        const events=[];
        if(info.hot)events.push({id:'CUL-4',text:'本批总减时 '+Math.round(info.reduction*100)+'%'});
        else if(info.reduction)events.push({id:rank(state,'CUL-S')?'CUL-S':'CUL-2',text:'本批减时 '+Math.round(info.reduction*100)+'%'});
        if(info.calm)events.push({id:'HOME-5',text:'安心等候 · 破壳后保鲜至少8小时'});
        if(replicate)events.push({id:'CUL-5',text:'已安排1只 · 记得及时照料'});
        if(events.length)skillFeedback(events);
      }
    },false,{yes:remaining.length?'放弃并重开':'开始调理',no:remaining.length?'继续照顾':'取消'});
    const modal=dialogs.querySelector('.confirm');modal.classList.add('cooking-dialog');
    const badge={possible:'可能出现',gate:'本批有机会出现',guaranteed:'已安排1只',encounter:'首次偶遇'};
    const escape=value=>String(value).replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
    const mode=batchModeView(state,preview.plan);
    modal.querySelector('footer').insertAdjacentHTML('beforebegin',`<div class="cook-preview">${mode?`<section class="cook-mode" data-cook-mode="${mode.mode}"><h4>${escape(mode.title)}</h4>${mode.lines.map(t=>`<p>${escape(t)}</p>`).join('')}</section>`:''}<details class="candidate-list"><summary>查看本批候选 · ${preview.candidates.length}种（未知${preview.unknown}）</summary><p>这批可能出现这些伙伴，数量不固定。本批有机会出现：开火时先判断本批能否出现它，再分配24枚蛋；满足条件不代表每批都有。</p><div class="cook-candidates">${preview.candidates.map(c=>{
      const o=observationInfo(state,c.key,now()),portrait=c.known?characterPortrait(c.egg,c.id):o?.silhouette?'<span class="unknown-silhouette" aria-hidden="true">'+characterPortrait(c.egg,c.id)+'</span>':'<span class="cook-card-unknown" aria-hidden="true">?</span>';
      return `<button data-preview-species="${c.key}" aria-label="${c.code} ${badge[c.status]}">${portrait}<span>${c.known?escape(E.label(E.char(c.egg,c.id))):c.code}<small>${badge[c.status]}</small>${!c.known&&o?.full?'<small>方法已知，尚未收录</small>':''}</span></button>`;
    }).join('')}</div></details>${preview.nearby.length?'<h4>只差一味的未知方向</h4>'+preview.nearby.map(c=>`<button data-preview-species="${c.key}">${c.code} · ${c.action}</button>`).join(''):''}${preview.blocked.length?'<details><summary>已有线索，但本次不会出现</summary>'+preview.blocked.map(c=>`<p>${c.code} · 尚缺${c.reasons.join('、')||'当前搭配条件'}</p>`).join('')+'</details>':''}<details><summary>等待与照料可能改变结果</summary>${preview.changes.map(t=>'<p>'+t+'</p>').join('')}</details><div class="cook-options"><label><input type="checkbox" data-cook-protect="calm" ${info.calm?'checked':''} ${rank(state,'HOME-5')?'':'disabled'}>安心等候 ${rank(state,'HOME-5')?'':'· 需学会夜间慢做'}</label><p>用时增加25%，破壳后至少保鲜8小时；不防止脏污病变。</p></div>${rank(state,'CUL-5')?`<details><summary>拿手复刻 · 可多付10 CP</summary><p>指定1只已收录普通候选，其余23枚照常。仍可能因脏污或超时发生变化，请及时照料。</p><select data-replicate aria-label="指定复刻伙伴"><option value="">不指定 · 不加费</option>${E.replicateOptions(state,id,now()).map(c=>`<option value="${c.key}" ${state.progress.replicate===c.key?'selected':''}>${escape(E.label(E.char(c.egg,c.id)))}</option>`).join('')}</select></details>`:''}<details class="protection-options"><summary>保鲜与病变保护</summary><label><input type="checkbox" data-cook-protect="freshness" ${state.progress.protection.freshness?'checked':''} ${info.calm?'disabled':''}>延长保鲜 · ${effects(state).freshMinutes}分钟</label><p>开启：破壳后${Math.max(info.originalMinutes*2,120)+effects(state).freshMinutes}分钟；关闭：破壳后${Math.max(info.originalMinutes*2,120)}分钟。选择只影响下一批。安心等候时至少480分钟。</p><label><input type="checkbox" data-cook-protect="sickness" ${state.progress.protection.sickness?'checked':''} ${rank(state,'HOME-S')?'':'disabled'}>持家病变保护${rank(state,'HOME-S')?'':' · 需学会安心管家'}</label><p>普通病变概率40%→20%。想尝试脏污特殊变化时，可关闭。材料固有效果仍有效。</p></details>${info.hot?'<p class="cook-summary">✓ 趁热接锅 · 本批总减时'+Math.round(info.reduction*100)+'%</p>':''}${rank(state,'CUL-3')&&state.progress.leftovers.length>=5?'<p class="shortage">余料篮已满，本批不会返料。请先收下待收余料。</p>':''}<p>已安排的伙伴仍可能因脏污或超时发生变化，请及时照料。</p></div>`);
    modal.querySelectorAll('[data-preview-species]').forEach(b=>b.onclick=()=>{const scroll=modal.querySelector('.cook-preview').scrollTop;closeDialog();(E.char(...b.dataset.previewSpecies.split(':').map(Number))?.pack==='regional'?regionalUI.open({tab:'record'}):workshopUI.observe(b.dataset.previewSpecies,()=>{closePanel();requestCook(id);const list=dialogs.querySelector('.candidate-list');if(list)list.open=true;dialogs.querySelector('.cook-preview').scrollTop=scroll;}));});
    modal.querySelectorAll('[data-cook-protect]').forEach(b=>b.onchange=()=>{const selected=b.checked,key=b.dataset.cookProtect;closeDialog();if(commitProgress(s=>{s.progress.protection[key]=selected;if(key==='calm'&&selected)s.progress.protection.freshness=true;return true;})){requestCook(id);if(key!=='calm')dialogs.querySelector('.protection-options')?.setAttribute('open','');}});
    modal.querySelector('[data-replicate]')?.addEventListener('change',e=>{const key=e.target.value;closeDialog();if(commitProgress(s=>{s.progress.replicate=key||null;return true;}))requestCook(id);});
  }catch(error){alertBox(error.message);}
}
function collectEgg(index){
  if(dialogs.children.length||panel)return false;
  const e=state.batch?.eggs[index],first=e&&!(state.total[`${e.egg}:${e.id}`]>0||state.farm[`${e.egg}:${e.id}`]>0);if(!e||!commitProgress(s=>E.collect(s,index,now())))return false;
  // Rewards are committed once here; visual completion never changes the save.
  const born=performance.now();
  flights.push({born,e:{...e},x:e.x-30,y:e.y-30});
  const bonus=state.batch.rules?.pickGold&&e.gold?1:0;feedbacks.push({born,x:e.x,y:e.y,amount:1+bonus});
  const events=[];let summary='';
  if(bonus)events.push({id:'CUL-1',text:'额外 +1 CP'});
  if(state.progress.discoveryClue)events.push({id:'OBS-5',text:'已记下1条新线索 · 去图鉴查看'});
  if(state.batch.eggs.every(e=>e.collected)&&state.progress.lastHarvest){
    const h=state.progress.lastHarvest;summary='本批收取24只 · 基础24 CP · 手艺额外'+h.bonus+' CP';
    if(h.returned!==null)events.push({id:'CUL-3',text:'返还 '+E.label(E.ingredient(h.returned))+' ×1'});
  }
  if(events.length)skillFeedback(events,summary,summary?5500:2600);else if(summary)toast(summary,5500);
  if(first){
    if(!events.length)toast('',5200);
    const notice=game.querySelector('.game-toast');
    const intro=characterPortrait(e.egg,e.id)+'<span><strong>新伙伴 · 已收入图鉴</strong><span data-discovery-name></span><small>去图鉴查看画像与做法</small></span>';
    if(events.length)notice.insertAdjacentHTML('afterbegin','<div class="discovery-toast-title">'+intro+'</div>');
    else{notice.classList.add('discovery-toast');notice.innerHTML=intro;}
    notice.querySelector('[data-discovery-name]').textContent=E.label(E.char(e.egg,e.id));
  }
  sound(1);announce('收取'+E.label(E.char(e.egg,e.id))+'，获得'+(1+bonus)+' CP');renderControls();return true;
}
function animateFlights(){
  const at=performance.now();
  for(let i=flights.length-1;i>=0;i--)if(at-flights[i].born>=MOTION.flightMs)flights.splice(i,1);
  for(let i=feedbacks.length-1;i>=0;i--)if(at-feedbacks[i].born>=MOTION.feedbackMs)feedbacks.splice(i,1);
}
function closePanel(){dismissToast();resetInput();panel='';panels.replaceChildren();renderControls();}
function closeDialog(){resetInput();dialogs.replaceChildren();dialogAction=null;cleaningDialog=null;updateControlFocus();lastFocused?.focus({preventScroll:true});}
function confirmBox(message,fn,onlyOK=false,labels={}){resetInput();lastFocused=document.activeElement;dialogs.innerHTML=`<div class="modal-shade"></div><section class="confirm paper" role="dialog" aria-modal="true" aria-label="确认"><div class="confirm-title"><span>!</span>${onlyOK?'鸡宝提醒':'确认一下'}</div><p></p><footer>${onlyOK?'':'<button class="cream" data-no></button>'}<button class="cream" data-yes></button></footer></section>`;dialogs.querySelector('p').textContent=message;dialogs.querySelector('[data-yes]').textContent=onlyOK?'好':labels.yes||'确认';const cancel=dialogs.querySelector('[data-no]');if(cancel)cancel.textContent=labels.no||'取消';dialogAction=fn;cancel?.addEventListener('click',()=>{sound(2);closeDialog();});dialogs.querySelector('[data-yes]').onclick=()=>{const action=dialogAction;closeDialog();sound(1);action?.();};updateControlFocus();dialogs.querySelector('button').focus({preventScroll:true});}
function alertBox(message){confirmBox(message,null,true);}
function openCleaning(){
  const view={quote:null};
  confirmBox('',()=>{
    if(act(()=>E.clean(state,now(),view.quote))){sound(9);announce('厨房打扫好了，脏污进度已清零。');}
  },false,{yes:'打扫',no:'返回'});
  cleaningDialog=view;
  const dialog=dialogs.querySelector('.confirm');
  dialog.classList.add('cleaning-dialog');dialog.setAttribute('aria-label','打扫厨房');
  dialog.querySelector('.confirm-title').textContent='照顾小厨房';
  dialog.querySelector('p').textContent='脏污随时间累积，本周期 '+state.cleanCycle.hours+' 小时变脏；手艺延长在下次打扫后生效。提前打扫按当前脏污比例收费，打扫后从 0% 重新计时。';
  dialog.querySelector('p').insertAdjacentHTML('beforebegin',`<div class="cleaning-status"><div class="cleaning-heading"><span>厨房脏污</span><strong data-clean-percent></strong></div><div class="cleaning-meter" role="progressbar" aria-label="厨房脏污" aria-valuemin="0" aria-valuemax="100"><span></span></div><div class="cleaning-time" data-clean-time></div></div>`);
  dialog.querySelector('footer').insertAdjacentHTML('beforebegin','<div class="cleaning-price" data-clean-price></div>');
  updateCleaningStatus();
}
function updateCleaningStatus(){
  if(page!==0&&!cleaningDialog)return;
  const info=E.kitchenCleanInfo(state,now()),control=findControl('clean');
  control?.setAttribute('aria-description',`厨房脏污 ${info.percent}%，${info.canClean?'打扫需要 '+info.cost+' CP':'暂时无需打扫'}`);
  if(!cleaningDialog)return;
  cleaningDialog.quote={cost:info.cost,lastClean:state.lastClean,kitchenLevel:state.kitchenLevel};
  const minutes=Math.ceil(info.remaining/60000),hours=Math.floor(minutes/60),rest=minutes%60;
  dialogs.querySelector('[data-clean-percent]').textContent=info.percent+'%';
  const meter=dialogs.querySelector('.cleaning-meter');
  meter.setAttribute('aria-valuenow',String(info.percent));meter.dataset.dirty=String(info.dirty);
  meter.firstElementChild.style.width=info.percent+'%';
  dialogs.querySelector('[data-clean-time]').textContent=info.dirty?'厨房已经变脏了':`距离变脏还有 ${hours?hours+' 小时 ':''}${rest} 分钟`;
  dialogs.querySelector('[data-clean-price]').textContent=!info.canClean?'现在很干净，暂时不用花钱':!info.affordable?`需要 ${info.cost} CP，还差 ${info.cost-state.cp} CP`:`本次 ${info.cost} CP · 完全变脏时 ${info.fullCost} CP`;
  const yes=dialogs.querySelector('[data-yes]');
  yes.textContent=!info.canClean?'无需打扫':info.dirty?`打扫 ${info.cost} CP`:`提前打扫 ${info.cost} CP`;
  yes.disabled=!info.canClean||!info.affordable;
}
function panelSymbol(classes){const kind=classes.includes('regional')?'explore':classes.includes('business')||classes.includes('orders')?'shop':classes.includes('projects')?'workshop':classes.includes('regulars')?'farm':null;return kind?'<span class="panel-symbol">'+interfaceIcon(kind)+'</span>':'';}
function showPanel(title,body,classes=''){dismissToast();resetInput();panel=title;sound(4);panels.innerHTML=`<section class="panel paper ${classes}" role="dialog" aria-label="${title}"><button class="close" aria-label="关闭">×</button><h2>${panelSymbol(classes)}${title}</h2>${body}</section>`;panels.querySelector('.close').onclick=()=>{if(panelReturn){const back=panelReturn;panelReturn=null;back();return;}if(page===3)changePage(settingsReturnPage);else if(page>=2)changePage(0);else closePanel();};updateControlFocus();if(!dialogs.children.length)panels.querySelector('.close').focus({preventScroll:true});}
function openKitchenUpgrade(){
  const info=E.kitchenUpgradeInfo(state),current=kitchenStage(state.kitchenLevel),target=info.maxed?current:kitchenStage(info.targetLevel-1);
  const number=value=>value.toLocaleString('zh-CN');
  const preview=(stage,label)=>{
    const part=(path,[x,y,w,h])=>{const sprite=resolveSprite(path);return sprite.frame?`<svg x="${x}" y="${y}" width="${w}" height="${h}" viewBox="${sprite.frame.join(' ')}" preserveAspectRatio="${sprite.fit?'xMidYMid meet':'none'}"><image href="${sprite.file}" width="${sprite.size[0]}" height="${sprite.size[1]}"/></svg>`:`<image href="${path}" x="${x}" y="${y}" width="${w}" height="${h}" preserveAspectRatio="none"/>`;};
    const bed=stage.bedParts?stage.bedParts.map(([suffix,...rect])=>part(stage.bed+suffix,rect)).join(''):part(stage.bed,stage.bedRect??(stage.level?[39,169,243,159]:[33,169,254,158]));
    const room=kitchenBackgroundParts(stage).map(([suffix,...rect])=>part(stage.background+suffix,rect)).join('');
    return `<figure><svg class="stage-thumbnail" viewBox="0 58 320 290" role="img" aria-label="${stage.title}">${room}${bed}${part(stage.vessel,[268,294,47,47])}</svg><figcaption><span>${label} Lv.${stage.level+1}</span><strong>${stage.title}</strong></figcaption></figure>`;
  };
  const summary=info.maxed?'厨房已满级，继续探索配方与新品种。':!info.toolsReady?'先将下方六件厨具升至要求等级。':!info.affordable?'厨具条件已齐，攒够 CP 即可升级。':'条件已齐，可以升级厨房。';
  const requirements=info.maxed?'':`<ul class="upgrade-requirements" aria-label="厨具升级条件">${info.requirements.map(item=>`<li class="${item.met?'met':'missing'}"><span class="requirement-mark" aria-label="${item.met?'已满足':'未满足'}">${item.met?'✓':'·'}</span><span class="requirement-name">${E.label(E.tool(item.id))}</span><span class="requirement-level">${item.currentLevel?`Lv.${item.currentLevel}`:'未拥有'}<span class="requirement-divider"> / </span>Lv.${item.requiredLevel}</span></li>`).join('')}</ul>`;
  const benefits={
    2:['调味料槽 1 → 2 个','厨具可升 Lv.2，调理更快（另付 CP）'],
    3:['调味料槽 2 → 3 个 · 防晒乳液可购买','厨具可升 Lv.3，调理更快（另付 CP）'],
    4:['烧水壶可购买（另付 CP）','烧水壶达 Lv.3 后可购买面包机'],
  };
  const benefit=(info.maxed?['3 个调味料槽 · 烧水壶购买已开放','面包机需烧水壶达到 Lv.3']:benefits[info.targetLevel]).join('<br>');
  const price=info.maxed?'':`<div class="upgrade-price"><span>升级 <strong>${number(info.cost)} CP</strong></span><span class="${info.affordable?'':'short'}">${info.affordable?'持有':'还差'} ${number(info.affordable?state.cp:info.cost-state.cp)} CP</span></div>`;
  const label=info.maxed?'已达到最高等级':!info.toolsReady?'厨具条件未齐':!info.affordable?'CP 不足':`升级至 Lv.${info.targetLevel}`;
  showPanel('厨房升级',`<div class="scroll upgrade-body"><div class="upgrade-preview ${info.maxed?'maximum':''}">${preview(current,'当前')}${info.maxed?'':'<span class="upgrade-arrow" aria-hidden="true">›</span>'+preview(target,'升级后')}</div><p class="upgrade-benefit">${benefit}</p><p class="upgrade-summary ${info.canUpgrade?'ready':''}">${summary}</p>${requirements}</div><footer class="upgrade-footer">${price}<div class="upgrade-actions"><button class="orange" data-upgrade-kitchen ${info.canUpgrade?'':'disabled'}>${label}</button>${!info.maxed&&!info.toolsReady?'<button class="upgrade-shop" data-upgrade-shop>去商店</button>':''}</div></footer>`,'kitchen-upgrade');
  panels.querySelector('[data-upgrade-shop]')?.addEventListener('click',()=>{shopUI.setTab(0);changePage(2);});
  panels.querySelector('[data-upgrade-kitchen]').onclick=()=>{
    if(!E.kitchenUpgradeInfo(state).canUpgrade)return;
    const expectedLevel=state.kitchenLevel;
    confirmBox(`升级至厨房 Lv.${info.targetLevel}，花费 ${number(info.cost)} CP。\n\n升级后厨房会恢复干净，当前这批鸡宝会保留。`,()=>act(()=>{
      // A stale confirmation must never buy the following level as well.
      if(state.kitchenLevel!==expectedLevel)return;
      E.upgradeKitchen(state,now());sound(7);closePanel();paint();
      announce(`厨房已升级至 Lv.${state.kitchenLevel+1}，${target.title}准备好了。`);
      alertBox(`升级成功！\n${target.title} · Lv.${state.kitchenLevel+1}\n\n厨房已打扫干净。当前这批鸡宝已保留，可以继续收取。`);
    }));
  };
}
function openIngredients(){
  let draft=[...state.selected];const max=Math.min(3,state.kitchenLevel+1);
  const draw=()=>{
    showPanel('调味料',`<p class="counter">已选 ${draft.length} / ${max} · 每种消耗一份</p><div class="scroll ingredients-grid">${Object.entries(state.ingredients).filter(([,n])=>n>0).map(([id,n])=>`<button class="ingredient ${draft.includes(+id)?'selected':''}" data-id="${id}" aria-pressed="${draft.includes(+id)}" aria-label="${E.label(E.ingredient(+id))}">${ingredientPortrait(+id)}<small>${n}</small><div class="ingredient-title">${E.label(E.ingredient(+id))}</div></button>`).join('')||'<p class="empty">调味料用完啦，去商店补一些吧。</p>'}</div><footer><button class="ingredient-restock" data-restock>去商店补货</button><button class="orange" data-ok>选好了</button></footer>`,'screen-panel ingredient-screen');
    panels.querySelectorAll('[data-id]').forEach(b=>b.onclick=()=>{const id=+b.dataset.id;if(draft.includes(id))draft=draft.filter(i=>i!==id);else if(draft.length<max)draft.push(id);else{sound(13);panels.querySelector('.counter').textContent=`已选满 ${max} 种，先取消一项再换新的调味料。`;announce('调味料槽已满，先取消一项。');return;}sound(3);b.classList.toggle('selected',draft.includes(id));b.setAttribute('aria-pressed',String(draft.includes(id)));panels.querySelector('.counter').textContent=`已选 ${draft.length} / ${max} · 每种消耗一份`;});
    panels.querySelector('[data-ok]').onclick=()=>{if(act(()=>{state.selected=draft;delete state.events.seasonalRecipe;delete state.expansion.prepareMode;}))closePanel();};
    panels.querySelector('[data-restock]').onclick=()=>{shopUI.setTab(1);changePage(2);shopReturnAction=()=>{changePage(0);draw();};};
  };draw();
}
const collectionUI=createCollectionUI({skillFeedback,getState:()=>state,getPage:()=>page,getNow:now,panels,showPanel,confirmBox,alertBox,act,sound,characterPortrait,makeWalkers,changePage,openActivities,openJournal,openRecipeBook,openDuckShop:()=>{shopUI.setTab(2);changePage(2);},openObservation:key=>workshopUI.observe(key,()=>collectionUI.renderCollection()),openWorkshop:()=>workshopUI.open(),openBooks:()=>bookUI.open({tab:'collections'}),openBookTab:tab=>bookUI.open({tab}),openInventory:key=>{changePage(1);collectionUI.openInventory(key);},prepareRecipe:key=>prepareBookRecipe(key),openUse:use=>{if(use.kind==='collection')collectionsUI.open({id:use.id});else if(use.kind==='region'){const fromBook=page===4;if(!fromBook)changePage(6);if(fromBook)panelReturn=()=>bookUI.open({tab:'species'});regionalUI.open({regionId:use.id,tab:'record',recipeId:use.recipeId});}else openTrade(use.kind==='menu'?'business':'orders');}});
const shopUI=createShopUI({skillFeedback,getState:()=>state,panels,showPanel,confirmBox,alertBox,act,sound,toolPortrait,changePage,openActivities,openJournal,openRecipeBook,openCookware:id=>{toolScroll=toolScrollFor(id,TOOL_SCROLL_MAX);changePage(0);},openMaterialLore:id=>{const regionId=E.ingredient(id)?.region??({75:'V',76:'V',77:'R',78:'R',79:'T',80:'T',81:'B',82:'B'})[id];panelReturn=()=>shopUI.openIngredientDetails(id);regionalUI.open({regionId,tab:'record',materialId:id});},returnFromShop:()=>{if(shopReturnAction){const back=shopReturnAction;shopReturnAction=null;back();}else changePage(shopReturnPage);}});
const recipeBookUI=createRecipeBookUI({getState:()=>state,getNow:now,panels,showPanel,characterPortrait,toolPortrait,ingredientPortrait,
  onClose:()=>{if(page===4)collectionUI.renderCollection();else{changePage(4);}},
  onPrepare:key=>{const prepare=()=>{const r=commitProgress(s=>prepareDiscoveredRecipe(s,key,now()));if(!r){recipeBookUI.refresh();return;}toolScroll=toolScrollFor(r.toolId,TOOL_SCROLL_MAX);changePage(0);toast(`${r.name}配方已选好 · 使用${r.toolName}\n当前一批保留，开火前会再次确认。`);};if(state.selected.length)confirmBox('替换下一批的蛋种与材料。\n当前孵化继续，不会立即消耗材料或 CP。',prepare,false,{yes:'换成这份配方'});else prepare();},
  openIngredients:ids=>supplyFromRecipe(1,()=>shopUI.openIngredientDetails(ids[0])),
  openTools:id=>supplyFromRecipe(0,()=>shopUI.openToolDetails(Math.max(0,id))),
  openKitchen:()=>{changePage(0);openKitchenUpgrade();},
  openActivities,openDuckShop:()=>supplyFromRecipe(2),openCalendar:()=>openJournal('calendar')});
const workshopUI=createWorkshopUI({skillFeedback,getState:()=>state,getNow:now,panels,showPanel,confirmBox,alertBox,commit:commitProgress,characterPortrait,openRegional:()=>openExplore(),openBusiness:()=>openTrade('business'),businessNote:()=>state.expansion.business?.active?'营业中':businessUI.unreadReport()?'有新账单':'',goKitchen:()=>{if(page===0)closePanel();else changePage(0);},prepareTool:id=>{toolScroll=toolScrollFor(id,TOOL_SCROLL_MAX);if(page===0)closePanel();else changePage(0);}});
const businessUI=createBusinessUI({getState:()=>state,getNow:now,commitProgress,showPanel,panels,alertBox,confirmBox,characterPortrait,openOrders:()=>openTrade('orders'),openRegulars:id=>openTrade('regulars',id),openProjects:()=>openTrade('projects'),goKitchen:()=>{if(page===0)closePanel();else changePage(0);}});
const orderUI=createOrderUI({getState:()=>state,getNow:now,commitProgress,showPanel,panels,alertBox,confirmBox,characterPortrait,openBusiness:()=>openTrade('business'),openRegulars:()=>openTrade('regulars'),openProjects:()=>openTrade('projects'),openStory:()=>{panelReturn=()=>openTrade('orders');workshopUI.open('story');},openRecipe:key=>openRecipeFromOrder(key),goKitchen:()=>{if(page===0)closePanel();else changePage(0);}});
const regularUI=createRegularUI({getState:()=>state,commitProgress,showPanel,panels,openBusiness:()=>openTrade('business'),openOrders:()=>openTrade('orders'),openProjects:()=>openTrade('projects')});
const projectUI=createProjectUI({getState:()=>state,commitProgress,showPanel,panels,alertBox,confirmBox,characterPortrait,openBusiness:()=>openTrade('business'),openOrders:()=>openTrade('orders'),openRegulars:()=>openTrade('regulars')});
const collectionsUI=createCollectionsUI({showSpecies:(egg,id,back)=>collectionUI.showCharacter(egg,id,back),getState:()=>state,commitProgress,showPanel,panels,alertBox,characterPortrait,openBookTab:tab=>bookUI.open({tab}),openJournal,openShrine,goBack:()=>{if(page===4)bookUI.open({tab:'overview'});else closePanel();}});
const bookUI=createBookUI({getState:()=>state,getNow:now,panels,showPanel,openSpecies:()=>collectionUI.renderCollection(),openCollections:options=>collectionsUI.open(options),openRegion:options=>{panelReturn=()=>bookUI.open({tab:'lore'});regionalUI.open(options);},openRecipeBook,openJournal,openActivities,characterPortrait,showSpecies:(egg,id)=>collectionUI.showCharacter(egg,id),openTarget:pin=>{if(pin.kind==='collection')collectionsUI.open({id:pin.id});else if(pin.kind==='recipe')regionalUI.open({tab:'record'});else openTrade(pin.kind==='regular'?'regulars':'projects',pin.id);}});
function supplyFromRecipe(tab,detail=()=>{}){const origin=page;shopUI.setTab(tab);changePage(2);shopReturnAction=()=>{changePage(origin);recipeBookUI.resume();};detail();}
function supplyFromJournal(tab,detail=()=>{}){const origin=page;shopUI.setTab(tab);changePage(2);shopReturnAction=()=>{changePage(origin);journalUI.resume();};detail();}
function prepareBookRecipe(key){const r=commitProgress(s=>prepareDiscoveredRecipe(s,key,now()));if(!r)return;toolScroll=toolScrollFor(r.toolId,TOOL_SCROLL_MAX);changePage(0);toast('下一锅配方已准备，当前锅仍保留。确认开火才扣材料和 CP。');}
const harvestAllocationUI=createHarvestAllocationUI({getState:()=>state,getNow:now,showPanel,panels,commitProgress,confirmBox,closePanel,openBusinessDraft:stock=>{businessUI.prepareDraft(stock);tradeView={kind:'business'};if(page!==5)changePage(5);businessUI.open('prepare');}});
const regionalUI=createRegionalUI({getState:()=>state,getNow:now,commitProgress,showPanel,panels,alertBox,confirmBox,characterPortrait,openLegacyTrip:()=>{panelReturn=()=>regionalUI.open();workshopUI.open('trip');},goKitchen:id=>{if(Number.isInteger(id))toolScroll=toolScrollFor(id,TOOL_SCROLL_MAX);if(page===0)closePanel();else changePage(0);}});
function openRecipeBook(options={}){if(!options.back&&page!==4)changePage(4);recipeBookUI.open(options);}
const activitiesUI=createActivitiesUI({getState:()=>state,getNow:now,panels,showPanel,confirmBox,alertBox,commitClaim:commitActivity,sound,characterPortrait,ingredientPortrait,openShrine,prepareGift:prepareGiftFromUI,onClose:()=>{if(page===2)openShop();else if(page===4)collectionUI.renderCollection();else closePanel();},openKitchen:()=>{if(page===0)closePanel();else changePage(0);}});
const shrineUI=createShrineUI({getState:()=>state,getNow:now,panels,showPanel,sound,characterPortrait,openJournal,
  commitDraw:()=>commitActivity('shrine-gift'),commitReward:id=>commitProgress(candidate=>claimShrineGoal(candidate,id)),prepareGift:prepareGiftFromUI,
  onClose:()=>activitiesUI.open(),openLetters:()=>activitiesUI.detail('shrine'),openGifts:()=>activitiesUI.detail('yokai'),openTravel:()=>activitiesUI.detail('time-travel'),
  openRecipes:()=>openRecipeBook({tool:8}),openDuckShop:()=>{shopUI.setTab(2);changePage(2);},openObservation:key=>workshopUI.observe(key,()=>collectionUI.renderCollection()),openWorkshop:()=>workshopUI.open(),openBooks:()=>bookUI.open({tab:'collections'}),openBookTab:tab=>bookUI.open({tab}),openInventory:key=>{changePage(1);collectionUI.openInventory(key);},prepareRecipe:key=>prepareBookRecipe(key),openUse:use=>{if(use.kind==='collection')collectionsUI.open({id:use.id});else if(use.kind==='region'){const fromBook=page===4;if(!fromBook)changePage(6);if(fromBook)panelReturn=()=>bookUI.open({tab:'species'});regionalUI.open({regionId:use.id,tab:'record',recipeId:use.recipeId});}else openTrade(use.kind==='menu'?'business':'orders');}});
const settingsUI=createSettingsUI({getState:()=>state,panels,showPanel,alertBox,save,music,sound,characterPortrait,toolPortrait,changePage,platform,toggleHatchAlarm,exportProgress,importProgress,openJournal,openWorkshop:()=>workshopUI.open(),getSaveError:()=>lastSaveError,returnFromSettings:()=>changePage(settingsReturnPage),returnToTitle:()=>{page=-1;closePanel();resize();music();renderControls();paint();}});
function openActivities(target){activitiesUI.open(target);}
let journalReturn=()=>closePanel();
const journalNotices=new Set();
const journalUI=createJournalUI({getState:()=>state,getNow:now,panels,showPanel,characterPortrait,ingredientPortrait,openRecipeBook,
  preparePhoenix:()=>{const prepare=()=>{if(!act(()=>{state.egg=0;state.selected=[];delete state.events.seasonalRecipe;}))return;
    toolScroll=0;if(page===0)closePanel();else changePage(0);toast('已选鸡蛋 · 使用 Lv.3 保温灯\n以开火时间判断凤凰时段，稀有伙伴不保证每批出现。',6500);};
    if(state.selected.length)confirmBox('下一批改用鸡蛋和保温灯，不放调味料。\n只调整选择，当前批次保留。',prepare,false,{yes:'选好保温灯'});else prepare();},
  claimChapter:id=>commitProgress(s=>claimSeasonalChapter(s,id)),
  setNotices:on=>act(()=>{state.events.discoveryNotices=on;}),
  openActivities,openShrine,openDimSum:()=>openRecipeBook({tool:8}),
  openToolShop:id=>supplyFromJournal(0,()=>shopUI.openToolDetails(id)),
  openIngredients:ids=>supplyFromJournal(1,()=>shopUI.openIngredientDetails(ids[0])),
  openDuckShop:()=>supplyFromJournal(2),onClose:()=>journalReturn(),
});
function openJournal(tab='calendar',key){
  if(panels.querySelector('.shrine-screen'))journalReturn=()=>openShrine();
  else if(page===4)journalReturn=()=>collectionUI.renderCollection();
  else if(page===2)journalReturn=()=>openShop();
  else if(page===3)journalReturn=()=>openSettings();
  else journalReturn=()=>closePanel();
  journalUI.open(tab,key);
}
function maybeDiscoveryNotice(){
  if(page!==0||panel||dialogs.children.length||game.querySelector('.game-toast:not([hidden])'))return;
  const notice=calendarNotice(state,now());if(!notice||journalNotices.has(notice.key))return;
  journalNotices.add(notice.key);toast(notice.message,9000);
}
function openShrine(tab='draw'){shrineUI.open(tab);}
function commitActivity(id){return commitProgress(candidate=>claimActivity(candidate,id,now()));}
function commitProgress(mutate){
  if(recoveryError)return null;
  const previous=state;
  try{
    const outcome=execute({state,store:review?null:saveStore,command:{type:'gameplay',selected:state.selected,egg:state.egg},now:Math.floor(now()),advance:E.advanceWorld,reduce:mutate});
    state=outcome.state;committedState=structuredClone(state);
    if(review)localStorage.setItem(storageKey,JSON.stringify(state));
    lastSaveError='';makeWalkers();renderControls();return outcome.result;
  }catch(error){
    state=previous;lastSaveError=error.message;
    if(['ACK_UNKNOWN','REVISION_CONFLICT','SAVE_LOCKED'].includes(error.code))recoveryError=error.message;
    sound(13);alertBox(error.message);return null;
  }
}
let toastTimer;
function dismissToast(){clearTimeout(toastTimer);const element=game.querySelector('.game-toast');if(element)element.hidden=true;}
function toast(message,duration=4200){
  let element=game.querySelector('.game-toast');if(!element){element=document.createElement('div');element.className='game-toast';element.setAttribute('role','status');game.append(element);}
  clearTimeout(toastTimer);element.className='game-toast';element.textContent=message;element.hidden=false;toastTimer=setTimeout(()=>{element.hidden=true;},duration);
}
function skillFeedback(events,summary='',duration=4200){
  // A receipt belongs to its existing dialog; never place a second overlay over it.
  const modal=dialogs.querySelector('.confirm');
  if(modal){
    const receipt=modal.querySelector('.skill-receipt')??document.createElement('div');receipt.className='skill-receipt';
    renderSkillFeedback(receipt,events,summary);modal.querySelector('footer').before(receipt);return;
  }
  toast('',duration);const element=game.querySelector('.game-toast');element.classList.add('skill-toast');if(panel)element.classList.add('is-panel-toast');renderSkillFeedback(element,events,summary);
}
function prepareGiftFromUI(id){
  try{
    const recipe=giftRecipe(state,id);
    const prepare=()=>{
      const result=commitProgress(candidate=>prepareGiftRecipe(candidate,id));if(!result)return;
      toolScroll=toolScrollFor(result.toolId,TOOL_SCROLL_MAX);
      if(page===0)closePanel();else changePage(0);
      toast(`${result.name}已选好 · 使用${result.toolName}\n${result.activeBatch?'当前一批继续孵化，收完后再开火。':'点击厨具并确认后，才会开始调理。'}`);
    };
    if(state.selected.length&&(state.selected.length!==1||state.selected[0]!==id))confirmBox(`下一批改用鸡蛋、${recipe.toolName}和${recipe.name}。\n只替换已选材料，不消耗库存，当前批次保留。`,prepare,false,{yes:'换成这份配方'});
    else prepare();
  }catch(error){sound(13);alertBox(error.message);}
}
function openShop(){shopUI.openShop();}
function openAlbum(){collectionUI.openAlbum();}
function showCharacter(egg,id){collectionUI.showCharacter(egg,id);}
function openSettings(){settingsUI.openSettings();}
function openManual(index=0){settingsUI.openManual(index);}
async function toggleHatchAlarm(){
  try{
    if(!state.alarm&&platform.info.android){
      const status=await platform.requestNotifications();
      if(!status.permissionGranted||!status.notificationsEnabled){alertBox('通知尚未获准。请在设置中的“系统通知设置”允许鸡宝厨房发送通知，再开启提醒。');return;}
    }
    const previous=state.alarm;state.alarm=!previous;
    if(!save()){state.alarm=previous;renderControls();if(page===3)openSettings();alertBox('提醒设置没有保存成功，已保留原来的开关状态。\n'+lastSaveError);return;}
    sound(3);renderControls();
    if(page===3)openSettings();
    else announce(state.alarm?'已开启孵化提醒。':'已关闭孵化提醒。');
  }catch(error){alertBox('提醒设置失败：'+error.message);}
}
async function exportProgress(){
  try{const result=await platform.exportBackup(makeBackup(state,Date.now(),platform.info.version));if(result.message!=='已取消')alertBox(result.message);}
  catch(error){alertBox('备份未导出：'+error.message);}
}
async function importProgress(){
  try{
    const result=await platform.selectBackup();if(!result.ok){if(result.message!=='已取消')alertBox(result.message);return;}
    const candidate=parseBackup(result.raw);
    const species=Object.values(candidate.total).filter(n=>n>0).length;
    confirmBox(`将导入以下进度：\n厨房 Lv.${candidate.kitchenLevel+1} · ${candidate.cp.toLocaleString('zh-CN')} CP\n已发现 ${species} 种 · 保存于 ${new Date(candidate.lastSeen).toLocaleString('zh-CN')}\n\n导入会替换当前进度。导入前建议先导出当前备份，确认继续吗？`,()=>{
      try{
        E.resume(candidate);saveStore.write(candidate,{importing:true});
        state=candidate;committedState=structuredClone(candidate);recoveryError='';lastSaveError='';clockOffset=0;speed=1;virtualNow=Date.now();previousReal=Date.now();
        flights.length=0;feedbacks.length=0;toolScroll=0;farmScroll=0;collectionUI.reset?.();recipeBookUI.reset();makeWalkers();
        page=-1;changePage(0);alertBox('进度已恢复。孵化时间、农场和图鉴都已载入。');
      }catch(error){alertBox('未导入，当前进度已保留：'+error.message);}
    });
  }catch(error){alertBox('未导入，当前进度已保留：'+error.message);}
}
function openRecovery(){
  showPanel('存档需要恢复','<div class="scroll recovery-copy"><p data-recovery-message></p><p>自动保存已暂停，原存档文件仍然保留。你可以导入以前导出的备份；若来自较新版本，请先更新游戏。</p><button class="orange" data-recover>导入备份</button></div>','screen-panel');
  panels.querySelector('[data-recovery-message]').textContent=recoveryError;
  panels.querySelector('[data-recover]').onclick=importProgress;panels.querySelector('.close').onclick=openRecovery;
}
window.addEventListener('chick:native',event=>{
  if(!platform.info.android)return;
  const type=event.detail?.type;
  if(type==='pause'){cancelInput();save();bgm.pause();}
  if(type==='resume'&&loaded){
    // A live WebView keeps its current screen and pending operation. New
    // instances already start at the title; notifications navigate separately.
    resumeGameplay();if(page===3&&!dialogs.children.length)openSettings();
  }
  if(type==='title')enterTitle();
  if(type==='kitchen')enterKitchen();
  if(type==='back'){
    if(recoveryError){platform.closeApp();return;}
    if(dialogs.children.length)closeDialog();else if(panel)panels.querySelector('.close')?.click();
    else if(page>0)changePage(0);else confirmBox('进度会自动保存。离开小厨房吗？',()=>{if(save())platform.closeApp();else alertBox('保存尚未成功，请先导出备份后再离开。');});
  }
});
function enterTitle(){
  if(recoveryError)return;
  nativeKitchenPending=false;
  if(!loaded)return;
  resetInput();closePanel();closeDialog();dismissToast();page=-1;resize();
  resumeGameplay();renderControls();paint();
}
function enterKitchen(){
  if(recoveryError)return;
  if(!loaded){nativeKitchenPending=true;return;}
  nativeKitchenPending=false;
  resetInput();closePanel();closeDialog();
  if(page!==0)changePage(0);else{dismissToast();renderControls();paint();}
  resumeGameplay();
}
function resumeGameplay(){
  advanceClock();if(recoveryError)return;
  const result=commitProgress(s=>{E.resume(s,now());return {lost:page===1?E.checkFarmLoss(s,now()):0};});
  if(result){renderControls();paint();if(result.lost){collectionUI.refresh();alertBox(`脱逃事件：逃走了${result.lost}只鸡！`);}}
  music();
}
function entryInputBlocked(){return performance.now()<entryInputBlockedUntil;}
function gamePoint(ev){const r=canvas.getBoundingClientRect();return {x:(ev.clientX-r.left)/r.width*L.width,y:(ev.clientY-r.top)/r.height*L.height};}
function eggHitRect(e){return {x:e.x-19,y:e.y-13+(L.eggOffset||0),w:38,h:42};}
function resetInput(){
  const pointerId=pointer?.id,movedTools=pointer?.kind==='tools'&&pointer.drag;
  if(movedTools)toolScroll=settleToolDrag(pointer);
  pointer=null;keyboardPress=null;pressedId='';
  if(pointerId!==undefined&&controls.hasPointerCapture(pointerId))controls.releasePointerCapture(pointerId);
  if(movedTools)renderControls();
}
function cancelInput(){
  // The farm keeps its last scrolled position when a gesture is interrupted.
  // Clear capture before rebuilding so lostpointercapture cannot cancel twice.
  const movedFarm=pointer?.kind==='farm'&&pointer.drag;
  resetInput();
  if(movedFarm)renderControls();
}
function hitEggs(x,y,visited){
  if(!contains(RECT.eggArea,x,y))return;
  const list=state.batch?.eggs??[];
  for(let i=list.length-1;i>=0;i--){
    const e=list[i];
    if(e.collected||visited.has(i)||!contains(eggHitRect(e),x,y))continue;
    // Keep the same front-to-back hit order as the visible sprites. A held
    // pointer must move before it can reach a chick behind this one.
    visited.add(i);collectEgg(i);break;
  }
}
// A touch released on a kitchen control may synthesize a click after a new
// panel opens. Consume that one release click before it can hit a new button.
let consumeReleaseClick=false;
game.addEventListener('pointerdown',()=>{consumeReleaseClick=false;},true);
game.addEventListener('click',ev=>{if(consumeReleaseClick&&ev.detail>0){consumeReleaseClick=false;ev.preventDefault();ev.stopImmediatePropagation();}},true);
controls.addEventListener('pointerdown',ev=>{
  if(entryInputBlocked()){ev.preventDefault();return;}
  if(pointer||!ev.isPrimary||ev.button!==0||dialogs.children.length)return;
  const p=gamePoint(ev),button=ev.target.closest('.hotspot');
  if(button&&(button.disabled||controlBlocked(button)))return;
  const id=button?.dataset.controlId;
  if(page===0&&!panel&&p.y>=L.toolY&&p.y<=L.toolY+L.toolHeight&&(!button||id.startsWith('tool:'))){
    pointer={id:ev.pointerId,kind:'tools',controlId:id,rect:button?.hitRect,page,...beginToolDrag(p,toolScroll)};
    pressedId=id??'';button?.focus({preventScroll:true});
  }else if(button&&!id.startsWith('egg:')){
    pointer={id:ev.pointerId,kind:'button',controlId:id,rect:button.hitRect,page};
    pressedId=id;button.focus({preventScroll:true});
  }else if(page===0&&!panel&&contains(RECT.eggArea,p.x,p.y)){
    pointer={id:ev.pointerId,kind:'harvest',last:p,visited:new Set(),page};
  }else if(page===1&&!panel&&p.y>220&&p.y<L.navY-36){
    pointer={id:ev.pointerId,kind:'farm',last:p,start:p,drag:false,page};
  }else return;
  ev.preventDefault();controls.setPointerCapture(ev.pointerId);
  if(pointer.kind==='harvest')hitEggs(p.x,p.y,pointer.visited);
});
window.addEventListener('pointermove',ev=>{
  if(!pointer||pointer.id!==ev.pointerId)return;
  const p=gamePoint(ev);ev.preventDefault();
  if(pointer.kind==='tools'){
    moveToolDrag(pointer,p,L.toolWidth+L.toolGap,TOOL_SCROLL_MAX);
    pressedId=!pointer.drag&&!pointer.cancelled&&pointer.rect&&contains(pointer.rect,p.x,p.y)?pointer.controlId:'';
  }else if(pointer.kind==='button')pressedId=contains(pointer.rect,p.x,p.y)?pointer.controlId:'';
  else if(pointer.kind==='harvest'){
    const from=pointer.last,distance=Math.hypot(p.x-from.x,p.y-from.y);
    if(distance<6)return;
    const steps=Math.ceil(distance/6);
    for(let i=1;i<=steps;i++)hitEggs(from.x+(p.x-from.x)*i/steps,from.y+(p.y-from.y)*i/steps,pointer.visited);
    pointer.last=p;
  }else if(pointer.kind==='farm'){
    if(Math.hypot(p.x-pointer.start.x,p.y-pointer.start.y)>4)pointer.drag=true;
    if(pointer.drag)farmScroll=Math.max(0,Math.min(FARM_WORLD.maxScroll,farmScroll-(p.x-pointer.last.x)));
    pointer.last=p;
  }
},{passive:false});
window.addEventListener('pointerup',ev=>{
  if(!pointer||pointer.id!==ev.pointerId)return;
  const gesture=pointer,p=gamePoint(ev);
  if(gesture.kind==='tools')moveToolDrag(gesture,p,L.toolWidth+L.toolGap,TOOL_SCROLL_MAX);
  resetInput();
  if((gesture.kind==='button'||(gesture.kind==='tools'&&!gesture.drag&&!gesture.cancelled))&&gesture.rect&&gesture.page===page&&contains(gesture.rect,p.x,p.y)){
    const button=findControl(gesture.controlId);
    if(button&&!button.disabled&&!controlBlocked(button)){consumeReleaseClick=true;button.activate();}
  }else if(gesture.kind==='farm'&&gesture.drag)renderControls();
});
window.addEventListener('pointercancel',ev=>{if(pointer?.id===ev.pointerId)cancelInput();});
controls.addEventListener('lostpointercapture',ev=>{if(pointer?.id===ev.pointerId)cancelInput();});
window.addEventListener('blur',cancelInput);
controls.addEventListener('click',ev=>{
  if(ev.detail>0||entryInputBlocked()){ev.preventDefault();ev.stopImmediatePropagation();}
},true);
controls.addEventListener('keydown',ev=>{
  if(![' ','Enter'].includes(ev.key))return;
  const button=ev.target.closest('.hotspot');if(!button||button.disabled||controlBlocked(button))return;
  ev.preventDefault();if(ev.repeat)return;
  keyboardPress={id:button.dataset.controlId,key:ev.key};pressedId=keyboardPress.id;
});
window.addEventListener('keyup',ev=>{
  if(!keyboardPress||keyboardPress.key!==ev.key)return;
  ev.preventDefault();const button=findControl(keyboardPress.id);keyboardPress=null;pressedId='';
  if(button&&!button.disabled&&!controlBlocked(button)&&document.activeElement===button)button.activate();
});
controls.addEventListener('focusout',()=>{if(keyboardPress){keyboardPress=null;pressedId='';}});
document.addEventListener('keydown',e=>{if(e.key==='Escape'){resetInput();if(dialogs.children.length)closeDialog();else if(panel)panels.querySelector('.close')?.click();}if(e.key==='Tab'&&dialogs.children.length){const b=[...dialogs.querySelectorAll('button')],first=b[0],last=b.at(-1);if(e.shiftKey&&document.activeElement===first){e.preventDefault();last.focus();}else if(!e.shiftKey&&document.activeElement===last){e.preventDefault();first.focus();}}});
let pixelRatio=window.devicePixelRatio||1;
// Information pages use physical CSS pixels. Only the unchanged scene scales.
let wide=false;
function resize(){
  cancelInput();pixelRatio=window.devicePixelRatio||1;
  const baseFont=parseFloat(getComputedStyle(document.documentElement).fontSize)||16;
  wide=newOperationsEnabled('ui')&&innerWidth>=Math.max(1024,64*baseFont)&&innerHeight>=650&&page>=0;
  game.classList.toggle('is-wide',wide);game.classList.toggle('is-cover',page<0);
  const areaWidth=wide?Math.min(560,innerWidth-420):Math.min(560,innerWidth);
  const areaHeight=game.clientHeight-(page<0?0:wide?24:mainNav.getBoundingClientRect().height);
  // The scene no longer contains a bottom bar. Add back its 66 logical pixels
  // when choosing height, so the visible artwork fills the measured grid row.
  const logicalHeight=page<0?fitViewport(areaWidth,areaHeight,pixelRatio).logicalHeight:Math.max(568,Math.min(866,320*areaHeight/areaWidth+66));
  setViewportHeight(logicalHeight);
  game.style.setProperty('--game-height',logicalHeight+'px');game.style.setProperty('--extra-height',L.extra+'px');
  const visibleHeight=page<0?logicalHeight:L.navY-4,scale=Math.min(areaWidth/320,areaHeight/visibleHeight);
  const width=Math.max(1,Math.round(320*scale*pixelRatio)),height=Math.max(1,Math.round(logicalHeight*scale*pixelRatio));
  sceneStage.style.zoom=scale;sceneStage.style.height=visibleHeight+'px';
  const sideSpace=Math.max(0,(areaWidth-320*scale)/2);
  quickActions.style.setProperty('--scene-top',Math.max(0,(areaHeight-visibleHeight*scale)/2)+'px');
  quickActions.style.setProperty('--quick-offset',62*scale+'px');
  quickActions.style.setProperty('--quick-inset',(sideSpace>60?sideSpace-58:sideSpace+5)+'px');
  if(canvas.width!==width||canvas.height!==height){canvas.width=width;canvas.height=height;}
  if(loaded)renderControls();updateDesk();
}
const desk=document.querySelector('#desk');
function updateDesk(){if(!wide||!loaded||page<0||!state?.expansion){desk.replaceChildren();return;}renderDesk(desk,state,now(),{page,summary:panels.querySelector('.business-prep-summary,.business-next-window,.business-receipt-lines,.orders-summary,.regional-preview')?.textContent??'',openTrade,openExplore,openBooks:id=>{if(page!==4)changePage(4);collectionsUI.open({id});},goKitchen:()=>{if(page===0)closePanel();else changePage(0);}});}
window.addEventListener('resize',resize);resize();
window.addEventListener('pagehide',()=>{cancelInput();save();});document.addEventListener('visibilitychange',()=>{if(document.hidden){cancelInput();save();bgm.pause();}else resumeGameplay();});
// Only the separate review iframe accepts these controls. Real saves never do.
let demoInterval=null;
function stopReviewDemo(){clearInterval(demoInterval);demoInterval=null;}
window.addEventListener('message',event=>{if(!review||event.origin!==location.origin||event.source!==parent||event.data?.type!=='chick-review')return;const command=event.data.command;
  if(command==='dirty'){
    if(!loaded||typeof event.data.value!=='boolean')return;
    resetInput();closeDialog();closePanel();state.dirty=event.data.value;
    state.lastClean=now()-(state.dirty?172800000:0);state.lastSeen=now();save();renderControls();paint();return;
  }
  if(command==='capture'){
    if(loaded){paint();parent.postMessage({type:'chick-capture',dataUrl:canvas.toDataURL('image/png')},location.origin);}
    return;
  }
  if(command==='demo'){
    stopReviewDemo();
    if(!loaded||page!==0||panel||dialogs.children.length||!state.batch?.eggs.some(e=>!e.collected&&e.status==='ready'))return;
    const batch=state.batch;
    demoInterval=setInterval(()=>{
      if(page!==0||panel||dialogs.children.length||document.hidden||state.batch!==batch){stopReviewDemo();return;}
      const index=batch.eggs.findIndex(e=>!e.collected&&e.status==='ready');
      if(index<0){stopReviewDemo();return;}
      collectEgg(index);
      if(!batch.eggs.some(e=>!e.collected&&e.status==='ready'))stopReviewDemo();
    },130);
    return;
  }
  if(command==='speed'){virtualNow=now();clockOffset=0;speed=event.data.value;}
  if(command==='skip')clockOffset+=event.data.value;
  if(command==='scene'){
    stopReviewDemo();
    const fixture=event.data.value;
    resetInput();closeDialog();closePanel();announce('');toolScroll=0;farmScroll=0;collectionUI.reset?.();recipeBookUI.reset();shopUI.setTab(0);
    speed=1;virtualNow=Date.now();previousReal=Date.now();clockOffset=0;state=E.freshState(now());state.toolLevels=[0,0,0,0,0,0,-1,-1,-1];state.cp=600;flights.length=0;feedbacks.length=0;
    if(fixture.startsWith('v14-')){
      state.kitchenLevel=3;state.toolLevels=Array(9).fill(2);state.cp=25000;state.duck=true;
      for(const [egg,ids]of [[0,[0,3,4,6,7,9,34,36,43,55,89,114,115,116,117,118,119,120]],[1,[0,5,21,30,59,64]]])for(const id of ids){state.total[`${egg}:${id}`]=3;state.farm[`${egg}:${id}`]=1;}
      state.ingredients={1:2,16:2,24:2,37:2,50:2};
      if(fixture==='v14-busy'){state.egg=1;E.startBatch(state,0,now(),()=>.5);}
      if(fixture==='v14-new'||fixture==='v14-unknown')state=E.freshState(now());
      page=-1;changePage(4);
      if(fixture==='v14-unknown')collectionUI.showUnknown(0,117);
      else if(fixture==='v14-species')collectionUI.showCharacter(1,30);
      else if(fixture==='v14-calendar')openJournal('recipes');
      else if(fixture==='v14-harvest'){state.farm['0:0']=6;state.farm['0:117']=3;state.farm['1:0']=4;changePage(1);collectionUI.renderAlbum();}
      else openRecipeBook({tool:fixture==='v14-steamer'?8:1,key:fixture==='v14-detail'?'1:30':fixture==='v14-busy'?'0:117':undefined});
    }
    else if(fixture.startsWith('v12-')){
      state.kitchenLevel=3;state.toolLevels=Array(9).fill(2);state.cp=25000;state.duck=true;state.ingredients={16:2,50:2,9:2,72:2};
      for(let id=0;id<18;id++){state.total['0:'+id]=120;state.farm['0:'+id]=2;}
      const date=new Date(now());date.setHours(fixture==='v12-preview'?9:fixture==='v12-ending'?12:11,30,0,0);virtualNow=date.getTime();clockOffset=0;
      state.farmFixed=now();state.lastClean=now();journalNotices.clear();
      if(fixture==='v12-new')state=E.freshState(now());
      if(['v12-collection','v12-farm','v12-rewards'].includes(fixture))for(const c of SEASONAL_CHARACTERS){state.total[c.key]=3;state.farm[c.key]=2;}
      if(['v12-detail','v12-busy'].includes(fixture))state.total['0:120']=1;
      if(fixture==='v12-busy'){state.egg=1;E.startBatch(state,1,now(),()=>.5);}
      page=-1;
      if(fixture==='v12-notice'){virtualNow=new Date(date.getFullYear(),date.getMonth(),date.getDate(),9,30).getTime();changePage(0);}
      else if(fixture==='v12-collection'){changePage(4);collectionUI.showCharacter(1,64);}
      else if(fixture==='v12-farm')changePage(1);
      else {changePage(1);openJournal(['v12-recipes','v12-rewards','v12-detail','v12-busy'].includes(fixture)?'recipes':'calendar',['v12-detail','v12-busy'].includes(fixture)?'0:120':undefined);}
    }
    else if(fixture.startsWith('v11-')){
      state.kitchenLevel=3;state.toolLevels=Array(9).fill(2);state.cp=25000;state.ingredients={0:2,9:2,15:2};
      for(let id=0;id<18;id++){state.total['0:'+id]=120;state.farm['0:'+id]=3;}
      claimActivity(state,'shrine',now());claimActivity(state,'yokai',now());
      if(['v11-held','v11-busy','v11-book','v11-goals'].includes(fixture))claimActivity(state,'shrine-gift',now());
      if(fixture==='v11-busy')E.startBatch(state,1,now(),()=>.5);
      if(fixture==='v11-book')for(const id of [89,90,93,99]){state.total['0:'+id]=1;state.farm['0:'+id]=1;}
      if(fixture==='v11-goals'){
        for(const id of [...Array.from({length:10},(_,i)=>89+i),67,105,106,107,104,114,115,116,117,118,119])state.total['0:'+id]=1;
        for(let id=0;id<5;id++)state.total['1:'+id]=1;
      }
      if(fixture==='v11-ingredients')state.ingredients=Object.fromEntries(Array.from({length:30},(_,id)=>[id,1]));
      if(fixture==='v11-new')state=E.freshState(now());
      page=-1;
      if(fixture==='v11-ingredients'){changePage(0);openIngredients();}
      else if(fixture==='v11-settings')changePage(3);
      else{changePage(1);if(fixture!=='v11-farm')openShrine(fixture==='v11-book'?'book':fixture==='v11-goals'?'goals':'draw');}
    }
    else if(fixture.startsWith('v10-')){
      state.kitchenLevel=3;state.toolLevels=Array(9).fill(2);state.cp=25000;state.ingredients={0:2,9:2,15:2};
      for(let id=0;id<18;id++){state.total['0:'+id]=120;state.farm['0:'+id]=3;}
      const daytime=new Date(now());daytime.setHours(11,30,0,0);virtualNow=daytime.getTime();clockOffset=0;state.farmFixed=now();state.lastClean=now();
      if(fixture==='v10-catalog-locked'){state=E.freshState(now());state.cp=3000;}
      if(fixture==='v10-activities-locked'){state=E.freshState(now());}
      if(fixture==='v10-gifts'||fixture==='v10-gifts-full'){claimActivity(state,'shrine',now());claimActivity(state,'yokai',now());}
      if(fixture==='v10-gifts-full')state.ingredients={0:30};
      page=-1;
      if(fixture.includes('catalog')){shopUI.setTab(1);changePage(2);}
      else if(fixture==='v10-duck'){shopUI.setTab(2);changePage(2);}
      else if(fixture==='v10-collection'){changePage(4);collectionUI.showUnknown(0,89);}
      else {farmScroll=510;changePage(1);if(fixture!=='v10-farm-shrine')openActivities(fixture.includes('gifts')?68:fixture==='v10-travel'?'time-travel':undefined);}
    }
    else if(fixture.startsWith('v9-dim-sum')){
      state.kitchenLevel=3;state.toolLevels=Array(9).fill(2);state.cp=25000;state.ingredients={9:3,16:3,24:3,25:3,37:3,71:3};
      for(let id=0;id<12;id++)state.total['0:'+id]=2;
      for(let id=114;id<120;id++){state.farm['0:'+id]=2;state.total['0:'+id]=4;}
      state.selected=[24,37];const at=now();E.startBatch(state,8,at-1805000,()=>.4);
      state.batch.eggs.forEach((egg,index)=>{egg.id=114+index%6;egg.status='ready';egg.openAt=at-15000;egg.blackAt=at+3600000;egg.animationAt=at-3000;});
      state.cp=25000;toolScroll=TOOL_SCROLL_MAX;page=-1;
      if(fixture==='v9-dim-sum-recipes'){changePage(2);shopUI.openRecipes(117);}
      else if(fixture==='v9-dim-sum-shop'){changePage(2);}
      else if(fixture==='v9-dim-sum-farm'){changePage(1);}
      else if(fixture==='v9-dim-sum-detail'){changePage(4);showCharacter(0,117);}
      else changePage(0);
    }
    else if(fixture.startsWith('v8-')){
      const hours={'v8-farm-dawn':6,'v8-farm-evening':18,'v8-farm-night':22};
      const daytime=new Date(now());daytime.setHours(hours[fixture]??11,30,0,0);virtualNow=daytime.getTime();clockOffset=0;
      state=E.freshState(now());state.cp=12480;state.kitchenLevel=2;state.toolLevels=[1,1,0,0,0,0,-1,-1,-1];state.duck=true;state.ingredients={0:3,1:2,2:1,4:2};
      const known=[0,3,4,5,6,8,9,10,12,14,16,18,20,23,24,31,33,34,37,38,44,46,60,65,66,70];
      known.forEach((id,i)=>{state.farm['0:'+id]=i%6+1;state.total['0:'+id]=i%6+9;});
      [0,3,4,8,12,18].forEach((id,i)=>{state.farm['1:'+id]=i%3+1;state.total['1:'+id]=i%3+5;});
      if(fixture.includes('empty')){state.farm={};state.total={};}
      if(fixture==='v8-shop-poor')state.cp=2;
      if(fixture==='v8-full-inventory')state.ingredients={0:30};
      if(fixture==='v8-farm-damaged')state.farmFixed=now()-172800000;
      page=-1;
      if(fixture.includes('farm')||fixture==='v8-harvest'){changePage(1);if(fixture==='v8-harvest')openAlbum();}
      else if(fixture.includes('collection')||fixture==='v8-detail'){changePage(4);if(fixture==='v8-detail')showCharacter(0,18);}
      else if(fixture.includes('shop')||fixture==='v8-full-inventory'){shopUI.setTab(fixture==='v8-shop-ingredients'||fixture==='v8-full-inventory'?1:0);changePage(2);}
      else{changePage(3);if(fixture==='v8-manual')openManual(0);}
    }
    else if(/^stage[1-4]$/.test(fixture)||['upgrade-ready','upgrade-short','upgrade-poor','upgrade-max'].includes(fixture)){
      const level=fixture==='upgrade-max'?3:fixture.startsWith('stage')?Number(fixture.at(-1))-1:0;
      state.kitchenLevel=level;state.toolLevels=Array(6).fill(Math.min(2,level)).concat([-1,-1,-1]);
      state.cp=50000;E.startBatch(state,1,now(),()=>.5);
      state.batch.eggs.forEach((egg,i)=>{egg.status='ready';egg.openAt=now()-10000;egg.blackAt=null;egg.id=[0,3,4][i%3];});
      state.batch.started=now()-900000;state.batch.ends=now()-1;
      state.cp=fixture==='upgrade-poor'?500:fixture.startsWith('upgrade-')?15000:(level+1)*20000;
      if(fixture==='upgrade-short')state.toolLevels=[0,0,0,-1,-1,-1,-1,-1,-1];
      state.dirty=fixture==='upgrade-ready';
      if(state.dirty){state.lastClean=now()-172800000;state.farmFixed=now()-172800000;}
      page=-1;changePage(0);
      if(fixture.startsWith('upgrade-'))openKitchenUpgrade();
    }
    else if(fixture==='title'){page=-1;music();}
    else if(fixture==='new'){state=E.freshState(now());page=-1;changePage(0);}
    else if(fixture==='empty'||fixture==='longtext'){
      if(fixture==='longtext'){state.cp=12345678;state.farm['0:18']=24;state.total['0:18']=24;}
      page=-1;changePage(0);
    }
    else if(fixture==='farm'){for(let i=0;i<12;i++){state.farm['0:'+i]=3;state.total['0:'+i]=3;}page=-1;changePage(1);}
    else {
      if(fixture==='duck'){state.duck=true;state.egg=1;}
      if(fixture==='busy'){state.duck=true;state.dirty=true;}
      if(fixture==='upgrade')state.toolLevels[1]=1;
      const id=fixture==='dirty'?0:1;
      E.startBatch(state,id,now(),()=>.5);
      if(['ready','dirty','duck','sample','busy','upgrade'].includes(fixture)){
        state.batch.eggs.forEach((e,i)=>{
          e.status='ready';e.openAt=now()-10000;e.blackAt=null;
          if(['sample','busy','upgrade'].includes(fixture))e.id=[0,3,4][i%3];
        });
        state.batch.started=now()-900000;state.batch.ends=now()-1;
      }
      if(fixture==='hatching'){
        state.batch.eggs.forEach((e,i)=>{e.openAt=now()+i*600-1000;});
        state.batch.ends=now()+13800;
      }
      if(fixture==='dirty')state.dirty=true;
      page=-1;changePage(0);
    }
    if(fixture==='longtext')confirmBox('文字显示检查：丸子鸡三兄弟、焦糖玛琪朵鸡。\n这里展示较长的中文说明与大额 CP。请选择“不要”或“要”关闭此示例，然后可在图鉴查看丸子鸡三兄弟的完整名称。\n此示例不会扣除 CP。',null);
    save();renderControls();
  }
});
const primary=[...FARM_ART_FILES.map(p=>p.slice(1)),...ASSET_FILES.map(p=>p.slice(1)),
  ...KITCHEN_STAGES.flatMap(stage=>[stage.background,stage.bed,stage.vessel,stage.dirty]).filter(path=>typeof path==='string').map(path=>resolveSprite(path).file.slice(1)),
  ...DATA.images.filter(p=>/\/Egg\//.test(p)||/\/Tool1\/tool_1_\d+_0_0\.png$/.test(p)||/alarm_|kitchen_fix_0_0|farm_fix_0_0/.test(p)),
  ...Array.from({length:20},(_,i)=>charPath(0,i).slice(1))];
requestAnimationFrame(renderFrame);
await Promise.allSettled([document.fonts.load('500 13px "Chicken UI"','鸡宝厨房'),document.fonts.load('700 14px "Chicken UI"','鸡宝厨房'),document.fonts.ready,...[...new Set(primary)].map(p=>new Promise(resolve=>{const im=loadImage('/'+p);if(im.complete)resolve();else{im.addEventListener('load',resolve,{once:true});im.addEventListener('error',resolve,{once:true});}}))]);
// An upgraded save registers already-earned collection pages in its own
// idempotent transaction right after loading; migration itself never grants.
if(state&&!recoveryError&&state.expansion?.collections){const probe=structuredClone(state);if(reconcileProgress(probe).length)commitProgress(s=>{reconcileProgress(s);return true;});}
loaded=true;renderControls();paint();
if(recoveryError)openRecovery();else{save();if(nativeKitchenPending)changePage(0);if(initialSave.notice)alertBox(initialSave.notice);}
platform.bridge?.ready();
function advanceClock(){const real=Date.now();virtualNow+=(real-previousReal)*speed;previousReal=real;}
setInterval(()=>{advanceClock();tick++;if(recoveryError)return;
  const previous=state,candidate=structuredClone(state),before=JSON.stringify(candidate),wasDirty=candidate.dirty;
  const world=E.advanceWorld(candidate,now()),events=E.updateBatch(candidate,now()),becameDirty=!wasDirty&&candidate.dirty;
  let alarmed=false;
  if(candidate.alarm&&E.batchReadyAt(candidate.batch)!==null&&!candidate.batch.alarmed&&now()>=E.batchReadyAt(candidate.batch)){candidate.batch.alarmed=true;alarmed=true;}
  // Logical clock checkpoints once per minute; state transitions save immediately.
  const logicalAt=candidate.progress.logicalAt;candidate.progress.logicalAt=previous.progress.logicalAt;
  if(JSON.stringify(candidate)!==before||world.returned||(tick%600===0&&logicalAt!==previous.progress.logicalAt)){
    candidate.progress.logicalAt=logicalAt;state=candidate;
    if(!save())state=previous;
    else{if(page===0)[...new Set(events)].forEach(s=>sound(s==='break'?11:s==='duck'?14:12));if(alarmed){sound(5);announce('这一批鸡宝已经孵化，请及时收取。');}if(becameDirty)announce('厨房有些脏了，可以打扫。');if(world.returned){makeWalkers();refreshPanel();toast('寻访伙伴已归队，奖励在篮中等你。');}renderControls();}
  }
  if(!state.progress.tutorialSeen&&collectedTotal(state)>=24&&!panel&&!dialogs.children.length){if(commitProgress(s=>{s.progress.tutorialSeen=true;return true;}))toast('手艺开放了！点击厨房的「手艺」查看，完成第一批已获得2点。',8000);}
  if(tick%600===0){maybeDiscoveryNotice();journalUI.updateTime();}animateFlights();for(const w of walkers){w.turn+=Math.floor(Math.random()*2);if(w.turn>=30){w.turn=0;w.dir=Math.random()<.5?-1:1;}}if(tick%10===0){updateDesk();updateCleaningStatus();workshopUI.updateTime();regionalUI.refresh();businessUI.refresh();reportStatus();}
},100);

function renderFrame(){if(pixelRatio!==(window.devicePixelRatio||1))resize();paint();requestAnimationFrame(renderFrame);}
