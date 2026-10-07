import {installPopupSwipe} from './popup-swipe.js';
import {createClueBookUI} from './clue-book-ui.js';
import {KITCHEN_ART,goldenRect,goldenEggHit} from './kitchen-golden.js';
import {resolveSpecies} from './content-registry.js';
import {interfaceIcon} from './ui-icons.js';
import {kitTitle,kitChip,kitChipHtml,kitButton,kitButton2,kitBar,kitBig,kitIcon,kitArrow,kitEgg,kitLabel,placeKitDialog,kitPageHead,kitBookHead} from './ui-kit.js';
import {execute,acquireWriter} from './game-commands.js';
import {createWorkshopUI} from './workshop-ui.js';
import {createRegionalUI} from './regional-ui.js';
import {createBusinessUI} from './business-ui.js';
import {createOrderUI} from './order-ui.js';
import {createCollectionsUI} from './collections-ui.js';
import {createRegularUI} from './regular-ui.js';
import {createProjectUI} from './project-ui.js';
import {renderDesk} from './desk-ui.js';
import {createHarvestAllocationUI} from './harvest-allocation-ui.js';
import {createBookUI} from './book-ui.js';
import {newOperationsEnabled} from './rollback-policy.js';
import {uiPreference,setUiPreference} from './ui-preferences.js';
import {reconcileProgress} from './regulars.js';
import {renderSkillFeedback} from './skill-feedback.js';
import {cookingCandidates} from './candidate-query.js';
import {seasoningAdvice,rankAdvice,ADVICE_LENSES} from './seasoning-advisor.js';
import {batchModeView} from './batch-mode-view.js';
import {observationInfo} from './knowledge.js';
import {collectedTotal,rank,effects} from './progression.js';
import { GAME_DATA as DATA, TOOL_SCROLL_MAX } from './content-pack.js';
import {beginToolDrag,moveToolDrag,settleToolDrag,toolScrollFor} from './tool-strip.js';
import {cookingIngredients} from './cooking-query.js';
import {createSaveStore,makeBackup,parseBackup} from './save-store.js';
import {createPlatform} from './native-platform.js';
import * as E from './engine.js';
import {farmDisplay,timeZone} from './farm.js';
import {createRenderer} from './scene.js';
import {NAV,LAYOUT as L,RECT,navRect,toolRect,duckRect,contains,MOTION,fitViewport,setViewportHeight} from './theme.js';
import {characterImage,toolImage} from './catalog.js';
import {originalPortraitFrame} from './portrait-frames.js';
import {productionCharacter} from './production-art.js';
import {resolveSprite,spriteSVG,ASSET_FILES} from './art/manifest.js';
import {KITCHEN_STAGES,kitchenStage,kitchenBackgroundParts} from './kitchen-stages.js';
import {createCollectionUI} from './collection-ui.js';
import {createRecipeBookUI} from './recipe-book-ui.js';
import {prepareDiscoveredRecipe} from './recipe-book.js';
import {createShopUI,ingredientPortrait} from './shop-ui.js';
import {createNextBatchUI} from './next-batch-ui.js';
import {createWarehouseUI} from './warehouse-ui.js';
import {bookNavigation,bindBookNavigation} from './book-ui.js';
import {installGameFrame} from './game-frame.js';
import {createSettingsUI} from './settings-ui.js';
import {createActivitiesUI} from './activities-ui.js';
import {claimActivity} from './legacy-activities.js';
import {createShrineUI} from './shrine-ui.js';
import {shrineBook,claimShrineGoal,giftRecipe,prepareGiftRecipe} from './shrine.js';
import {createJournalUI} from './journal-ui.js';
import {plannedSeasonalRecipe,claimSeasonalChapter,SEASONAL_CHARACTERS} from './seasonal-pack.js';
import {calendarNotice} from './discovery-calendar.js';
import {FARM_ART_FILES,FARM_WORLD} from './farm-theme.js';
import {createFarmMapUI} from './farm-map-ui.js';
import {farmStock,FARM_MAP} from './farm-world.js';
import {compactNavItem} from './bottom-nav-compact-v1.js';
import {createSfx} from './sfx.js';
const game=document.querySelector('#game'),canvas=document.querySelector('#scene'),ctx=canvas.getContext('2d');
const sceneStage=document.querySelector('#scene-stage'),mainNav=document.querySelector('#main-nav');
const quickActions=document.createElement('nav');quickActions.id='quick-actions';quickActions.setAttribute('aria-label','厨房快捷入口');sceneStage.append(quickActions);
const controls=document.querySelector('#controls'),panels=document.querySelector('#panels'),dialogs=document.querySelector('#dialog-layer');
const review=new URLSearchParams(location.search).has('review');
const storageKey=review?'chick-kitchen-review-v1':'chick-kitchen-v1';
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
// Chicks swiped this gesture: already flying on screen, committed together by flushHarvest().
const pendingHarvest=new Set();let harvestTimer=0;
let lastPaint=0,activeUntil=0;
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
const sounds=createSfx(soundNames.map((s,i)=>`/res/raw/se${String(i).padStart(3,'0')}_${s}.mp3`));
const bgm=new Audio('/assets/music/bgm/bgm_000.mp3');bgm.loop=true;bgm.volume=.35;
function sound(id){if(state.sound)sounds.play(id);}
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
    if(firstFailure&&loaded&&!dialogs.children.length)alertBox('进度暂未保存，请先别关闭游戏，到设置导出备份。\n'+detail);
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
  if(panels.querySelector('.clue-screen'))clueBookUI.refresh();
  else if(panels.querySelector('.book-screen'))bookUI.refresh();
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
  else if(panels.querySelector('.next-batch-screen'))nextBatchUI.refresh();
  else if(panels.querySelector('.warehouse-screen'))warehouseUI.refresh();
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
function paint(){if(page===1&&farmZone!==timeZone(now()))makeWalkers();renderer.paint(state,{page,loaded,navBadges:navBadges(),externalNavigation:true,externalFarm:true,recovery:Boolean(recoveryError),version:platform.info.version,now:now(),tick,toolScroll,toolPosition:pointer?.kind==='tools'?pointer.position:toolScroll,farmScroll,flights,feedbacks,walkers,pressedId,reducedMotion,shrineReady,pending:pendingHarvest});}
function hotspot(name,rect,handler,id=name){
  if(page===0){const mapped=id.startsWith('tool:')?goldenRect('slot:'+(Number(id.slice(5))-toolScroll),state.kitchenLevel):goldenRect(id,state.kitchenLevel);if(mapped)rect=mapped;}
  const b=document.createElement('button');b.className='hotspot';b.setAttribute('aria-label',name);b.dataset.controlId=id;
  Object.assign(b.style,{left:rect.x+'px',top:rect.y+'px',width:rect.w+'px',height:rect.h+'px'});
  b.hitRect=rect;b.activate=handler;
  // Pointer gestures are resolved on release below. A detail-zero click remains
  // available to keyboard / assistive technology without collecting twice.
  b.onclick=event=>{if(event.detail!==0||controlBlocked(b))return;pressedId='';handler();};
  controls.append(b);return b;
}
function findControl(id){return [...controls.querySelectorAll('.hotspot')].find(b=>b.dataset.controlId===id);}
function controlBlocked(button){return entryInputBlocked()||dialogs.children.length>0||Boolean(panel&&button.hitRect.y>=58&&button.hitRect.y<L.navY-4);}
function updateControlFocus(){farmMap.setBlocked(!!panel||dialogs.children.length>0||entryInputBlocked());panels.inert=dialogs.children.length>0;mainNav.inert=dialogs.children.length>0;quickActions.hidden=page!==0||!!panel;quickActions.inert=dialogs.children.length>0||entryInputBlocked();for(const b of mainNav.querySelectorAll('button'))b.tabIndex=dialogs.children.length||entryInputBlocked()?-1:0;document.querySelector('#desk').inert=dialogs.children.length>0;for(const b of controls.querySelectorAll('.hotspot'))b.tabIndex=controlBlocked(b)?-1:0;}
function startGame(){
  // The cover overlaps the bottom navigation. Consume the remainder of a
  // rapid tap sequence before those newly visible buttons can act on it.
  entryInputBlockedUntil=performance.now()+500;
  sound(0);enterKitchen();
  // The old shop tab moved into the kitchen: tell a returning player once.
  // This introductory hint must not replace a reward or interrupt a dialog.
  // If the kitchen is busy, leave it unread so a later visit can show it.
  setTimeout(()=>{if(page===0&&!panel&&!dialogs.children.length&&!game.querySelector('.game-toast:not([hidden])')&&!uiPreference('seenSupplyMove')&&Object.keys(state.total??{}).length>1){setUiPreference('seenSupplyMove',true);toast('商店搬进厨房了，缺料时点「商店」就能补货。',4500);}},900);
  setTimeout(updateControlFocus,500);
}
function renderControls(){
  wake();updateFarmMap();game.dataset.page=String(page);
  const focusedId=document.activeElement?.dataset?.controlId;
  controls.replaceChildren();quickActions.replaceChildren();quickActions.hidden=page!==0||!!panel;
  if(page<0){mainNav.replaceChildren();if(loaded)hotspot(recoveryError?'恢复存档':'开始游戏',RECT.start,()=>{if(recoveryError){openRecovery();return;}startGame();},'start');return;}
  renderNavigation();
  if(page!==1)hotspot('设置',RECT.settings,()=>changePage(3),'settings');
  if(page!==0&&page!==1)hotspot('查看厨房等级',RECT.level,()=>{changePage(0);openKitchenUpgrade();},'level');
  if(page===0){
    hotspot('选择调味料',RECT.ingredient,openIngredients,'ingredient');
    hotspot('厨房等级 '+(state.kitchenLevel+1),RECT.level,openKitchenUpgrade,'level');
    hotspot('打扫厨房',RECT.clean,openCleaning,'clean');
    // The painted status strip and the empty-nest card look pressable, so they act on the current pot.
    hotspot(batchStatusLabel(),RECT.clean,batchStatusAction,'batch');
    if(!state.batch)hotspot('选厨具开火',RECT.clean,()=>requestCook(0),'nest-prompt');
    if(state.duck)[0,1].forEach(i=>hotspot(i?'选择鸭蛋':'选择鸡蛋',{...duckRect(i),y:duckRect(i).y+L.eggOffset},()=>act(()=>{state.egg=i;delete state.events.seasonalRecipe;sound(3);}), 'duck:'+i));
    if(state.batch?.eggs.some(e=>!e.collected))hotspot('孵化闹钟 '+(state.alarm?'开启':'关闭'),RECT.alarm,()=>toggleHatchAlarm(), 'alarm');
    for(let slot=0;slot<4;slot++){const id=toolScroll+slot;hotspot((state.toolLevels[id]>=0?'使用':'购买')+E.label(E.tool(id)),toolRect(slot),()=>{if(state.toolLevels[id]>=0)requestCook(id);else{shopUI.setTab(0);changePage(2);}},'tool:'+id);}
    for(const [id,label,icon,action] of [
      ['supply','商店','shop',()=>{shopUI.setTab(0);changePage(2);}],
      ['inventory','仓库','inventory',()=>warehouseUI.open()],
      ['workshop','手艺','workshop',()=>workshopUI.open()],
      ['help','帮助','help',()=>settingsUI.openManual()],
    ]){const b=document.createElement('button');b.dataset.controlId=id;b.className=id+'-launch';b.setAttribute('aria-label',id==='supply'?'补给 · 小卖部':label);b.innerHTML=interfaceIcon(icon)+`<span>${label}</span>`;b.onclick=()=>{if(!entryInputBlocked()&&!dialogs.children.length)action();};const r=goldenRect(id,state.kitchenLevel);Object.assign(b.style,{left:r.x+'px',top:r.y+'px',width:r.w+'px',height:r.h+'px'});quickActions.append(b);}
    hotspot('向右滚动厨具',RECT.next,()=>{toolScroll=Math.min(TOOL_SCROLL_MAX,toolScroll+1);renderControls();},'next').disabled=toolScroll===TOOL_SCROLL_MAX;
    hotspot('向左滚动厨具',RECT.prev,()=>{toolScroll=Math.max(0,toolScroll-1);renderControls();},'prev').disabled=toolScroll===0;
    state.batch?.eggs.forEach((e,i)=>{if(!e.collected&&!pendingHarvest.has(i))hotspot(e.status==='egg'?'孵化中的蛋 '+(i+1):'收取'+E.label(E.char(e.egg,e.id))+' '+(i+1),eggHitRect(e),()=>collectEgg(i),'egg:'+i);});
    // Harvest card paused at the user's request (2026-10-02); the nest stays clear once everything is collected.
    // if(state.batch?.eggs.every(e=>e.collected)){const b=hotspot('这锅收成',{x:64,y:250+L.eggOffset,w:192,h:44},()=>harvestAllocationUI.openCard(),'harvest-allocation');b.textContent='这锅收成';b.classList.add('harvest-allocation-launch');}
  }else if(page===1){
    // The map owns building hit areas and its camera, separately from the HUD.
  }
  if(pressedId&&!findControl(pressedId))pressedId='';
  if(focusedId)(findControl(focusedId)??[...quickActions.querySelectorAll('button')].find(b=>b.dataset.controlId===focusedId))?.focus({preventScroll:true});
  updateCleaningStatus();updateControlFocus();reportStatus();
}
function renderNavigation(){
  const focused=document.activeElement?.dataset?.controlId,badges=navBadges();
  mainNav.replaceChildren();
  for(const n of NAV){const b=document.createElement('button');b.dataset.controlId='nav:'+n.id;b.tabIndex=entryInputBlocked()?-1:0;b.setAttribute('aria-label',n.title);const active=page===n.id||n.id===0&&(page===2||page===3);b.setAttribute('aria-current',active?'page':'false');b.innerHTML=!wide?compactNavItem(n):interfaceIcon(({0:'kitchen',1:'farm',5:'shop',6:'explore',4:'book'})[n.id])+`<span>${n.title}</span>`;
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
function showTrade(){const v=tradeView;if(v.kind==='orders')businessUI.openOrders();else if(v.kind==='regulars')v.id?regularUI.open(v.id):regularUI.resume();else if(v.kind==='projects')v.id?projectUI.open(v.id):projectUI.resume();else businessUI.open();}
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
  // The kitchen's calendar note belongs to the kitchen; it never follows the player to another page.
  if(game.querySelector('.game-toast.calendar-toast:not([hidden])'))dismissToast();
  dismissToast();if(page===2&&index!==2)shopReturnAction=null;
  if(index===page){
    if(!panel)return;
    closeDialog();closePanel();
    if(index===2)openShop();else if(index===3)openSettings();else if(index===4)bookUI.open({tab:'species'});else if(index===5){tradeView={kind:'business'};showTrade();}else if(index===6)regionalUI.open();
    return;
  }
  if(index===3)settingsReturnPage=page<0?0:page;
  if(index===2){const origin=page===3?settingsReturnPage:page;if(origin!==2)shopReturnPage=origin<0?0:origin;}
  resetInput();closePanel();closeDialog();panelReturn=null;bookTab=null;page=index;sound(3);resize();
  // Opening the farm only needs a save when the escape check would change something.
  if(page===1){const probe=structuredClone(state),changed=E.checkFarmLoss(probe,now())>0||JSON.stringify(probe)!==JSON.stringify(state);const result=changed?commitProgress(s=>({lost:E.checkFarmLoss(s,now())})):{lost:0};makeWalkers();if(result?.lost)alertBox(`脱逃事件：逃走了${result.lost}只伙伴！`);}
  if(page===2)openShop();if(page===3)openSettings();if(page===4)bookUI.open({tab:'species'});if(page===5)showTrade();if(page===6)regionalUI.open();
  music();renderControls();paint();updateDesk();if(page===0)setTimeout(maybeDiscoveryNotice,180);
}
function startCookNow(id,prepare=null){
  const replicate=state.progress.replicate;let info=null;
  if(act(()=>{const candidate=structuredClone(state);prepare?.(candidate);info=E.cookInfo(candidate,id,now());candidate.batch=null;E.startBatch(candidate,id,now());state=candidate;sound(1);})){
    const events=[];
    if(info.hot)events.push({id:'CUL-4',text:'本批总减时 '+Math.round(info.reduction*100)+'%'});
    else if(info.reduction)events.push({id:rank(state,'CUL-S')?'CUL-S':'CUL-2',text:'本批减时 '+Math.round(info.reduction*100)+'%'});
    if(info.calm)events.push({id:'HOME-5',text:'安心等候 · 破壳后保鲜至少8小时'});
    if(replicate)events.push({id:'CUL-5',text:'已安排1只 · 记得及时照料'});
    if(events.length)skillFeedback(events);
    if(panels.querySelector('.next-batch-screen'))closePanel();
    return true;
  }
  return false;
}
// Cook confirm: egg, cookware and seasoning as pictures; time and cost as pills; who may appear as portrait slots.
// Rarely used care options (calm, replicate, protection) stay one tap away in a fold.
function requestCook(id){
  try{
    const info=E.cookInfo(state,id,now()),preview=cookingCandidates(state,id,now()),remaining=state.batch?.eggs.filter(e=>!e.collected)??[];
    const signature=()=>JSON.stringify({info:E.cookInfo(state,id,now()),preview:cookingCandidates(state,id,now()),protection:state.progress.protection,egg:state.egg});
    const quote=signature(),escape=value=>String(value).replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
    const level=state.toolLevels[id],capacity=Math.min(3,state.kitchenLevel+1),picked=preview.ingredients;
    const seconds=Math.round(info.minutes%1*60),time=`${Math.floor(info.minutes)}分${seconds?seconds+'秒':''}`;
    const badge={possible:'',gate:'有机会',guaranteed:'已安排1只',encounter:'首次偶遇'};
    const mode=batchModeView(state,preview.plan),fresh=effects(state).freshMinutes;
    const seasoning=picked.length?`<span class="gd-season">${picked.map(i=>`<span class="gd-season-item" title="${escape(E.label(E.ingredient(i)))}">${ingredientPortrait(i)}</span>`).join('')}</span>`:`<span class="gd-dashes">${'<i class="gd-dash"></i>'.repeat(Math.max(1,capacity))}</span>`;
    const candidates=preview.candidates.map(c=>{
      const o=observationInfo(state,c.key,now()),portrait=c.known?characterPortrait(c.egg,c.id):o?.silhouette?'<span class="unknown-silhouette" aria-hidden="true">'+characterPortrait(c.egg,c.id)+'</span>':'<span class="gd-q" aria-hidden="true">?</span>';
      const name=c.known?escape(E.label(E.char(c.egg,c.id))):c.code;
      return `<button type="button" class="gd-slot${c.known?'':' need'}" data-preview-species="${c.key}" aria-label="${c.code} ${badge[c.status]||'可能出现'}">${portrait}<b>${name}</b>${badge[c.status]?`<small class="gd-tag">${badge[c.status]}</small>`:'<small class="sr-only">可能出现</small>'}</button>`;
    }).join('');
    // 照料与保护: only what this player can actually switch — painted ticks, partner pictures for 拿手复刻.
    const tick='<img src="/web/art/golden-business/family-check.png" alt="">';
    const toggle=(key,label,on,off=false)=>`<button type="button" class="gd-toggle" data-cook-protect="${key}" aria-pressed="${on}"${off?' disabled':''}><i>${on?tick:''}</i>${label}</button>`;
    const options=[rank(state,'HOME-5')?toggle('calm','安心等候',!!info.calm):'',fresh?toggle('freshness',`保鲜 +${fresh}分`,!!state.progress.protection.freshness,info.calm):'',rank(state,'HOME-S')?toggle('sickness','病变保护',!!state.progress.protection.sickness):''].join('');
    const replicas=rank(state,'CUL-5')?E.replicateOptions(state,id,now()):[];
    const coin=(key,art,label,on)=>`<button type="button" class="gd-coinwrap${on?' on':''}" data-replicate="${key}" aria-pressed="${on}"><span class="gd-coin">${art}</span><b>${escape(label)}</b></button>`;
    const replicate=replicas.length?`<div class="gd-label">拿手复刻 · +10 CP</div><div class="gd-coins gd-replicate" data-row>${coin('','<b>不</b>','不指定',!state.progress.replicate)}${replicas.map(c=>coin(c.key,characterPortrait(c.egg,c.id),E.label(E.char(c.egg,c.id)),state.progress.replicate===c.key)).join('')}</div>`:'';
    // What could go wrong with this pot, as short chips worked out from this batch (not the general rules).
    const immune=picked.includes(18),dirtyBy=E.kitchenCleanInfo(state,now()).dirty||(state.cleanCycle?.dirtyAt??Infinity)<=now()+(info.minutes??0)*60000;
    const notes=[immune?kitChip('','防腐剂 · 不会病变','mini soft'):dirtyBy?kitChip('',rank(state,'HOME-S')&&state.progress.protection.sickness!==false?'孵化时变脏 · 病变机会减半':'孵化时变脏 · 可能病变','mini hot'):'',id>0&&!picked.includes(36)?kitChip('','收晚了会焦','mini'):'',preview.candidates.some(c=>c.key==='0:9')?kitChip('','温泉蛋鸡要 10 秒内收','mini'):''].join('');
    const more=options||replicate||notes?`<details class="gd-more protection-options"><summary>照料与保护</summary>${notes?`<div class="gd-row gd-wrap">${notes}</div>`:''}${options?`<div class="gd-row gd-wrap">${options}</div>`:''}${replicate}</details>`:'';
    const body=`${remaining.length?`<span class="gd-alert cook-abandon" role="alert">还有 ${remaining.length} 只没收，重开会放弃它们</span>`:''}`
      +`<div class="gd-tiles" data-row><div class="gd-tile"><span data-visual>${kitEgg(state.egg)}</span><b>${state.egg?'鸭蛋':'鸡蛋'}</b></div><div class="gd-tile wide"><span data-visual>${toolPortrait(id,level)}</span><b>${escape(E.label(E.tool(id)))}</b></div><div class="gd-tile"><span data-visual>${seasoning}</span><b>调味 ${picked.length}/${capacity}</b></div></div>`
      +`<div class="gd-row">${kitChip(kitIcon.clock,time)}${kitChipHtml(`${kitIcon.coin}${info.cost}`)}${info.hot?kitChip('',`趁热 −${Math.round(info.reduction*100)}%`,'soft'):''}</div>`
      +(mode?`<section class="gd-note cook-mode" data-cook-mode="${mode.mode}"><b>${escape(mode.title)}</b>${mode.lines.map(t=>`<span>${escape(t)}</span>`).join('')}</section>`:'')
      +kitLabel(`可能出现 · ${preview.candidates.length}`)
      +`<div class="gd-scroll-row candidate-list">${candidates}</div>`
      +(preview.nearby.length?`<div class="gd-row gd-wrap gd-nearby">${preview.nearby.map(c=>`<button type="button" class="gd-btn2 mini" data-preview-species="${c.key}">${c.code} · ${escape(c.action)}</button>`).join('')}</div>`:'')
      +(rank(state,'CUL-3')&&state.progress.leftovers.length>=5?'<span class="gd-alert shortage">余料篮满了，这一锅不会返料</span>':'')
      +more;
    const modal=kitDialog({title:remaining.length?'重开':'开火',label:'开火确认',className:'cooking-dialog',body,no:'看推荐',noAttrs:'data-cook-advice',onNo:()=>openIngredients({tool:id}),yes:remaining.length?'重开':'开火',onYes:()=>{
      if(signature()!==quote){requestCook(id);announce('搭配或条件已变化，请核对新的摘要并再次确认。');return;}
      startCookNow(id);
    }});
    modal.querySelectorAll('[data-preview-species]').forEach(b=>b.onclick=()=>{const key=b.dataset.previewSpecies;closeDialog();(E.char(...key.split(':').map(Number))?.pack==='regional'?regionalUI.open({tab:'record'}):workshopUI.observe(key,()=>{closePanel();requestCook(id);dialogs.querySelector(`.candidate-list [data-preview-species="${key}"]`)?.scrollIntoView({block:'nearest',inline:'center'});}));});
    // Opening 照料与保护 brings its chips into view inside the popup.
    modal.querySelector('.protection-options')?.addEventListener('toggle',e=>{if(e.target.open)e.target.scrollIntoView({block:'nearest'});});
    modal.querySelectorAll('[data-cook-protect]').forEach(b=>b.onclick=()=>{const selected=b.getAttribute('aria-pressed')!=='true',key=b.dataset.cookProtect;closeDialog();if(commitProgress(s=>{s.progress.protection[key]=selected;if(key==='calm'&&selected)s.progress.protection.freshness=true;return true;})){requestCook(id);{const more=dialogs.querySelector('.protection-options');if(more){more.setAttribute('open','');more.scrollIntoView({block:'nearest'});}}}});
    modal.querySelectorAll('[data-replicate]').forEach(b=>b.onclick=()=>{const key=b.dataset.replicate;closeDialog();if(commitProgress(s=>{s.progress.replicate=key||null;return true;})){requestCook(id);{const more=dialogs.querySelector('.protection-options');if(more){more.setAttribute('open','');more.scrollIntoView({block:'nearest'});}}}});
  }catch(error){alertBox(error.message);}
}
// A harvest tap or swipe shows every chick leaving at once and commits them as
// one transaction when the last one lands (or any other action needs the save).
// Rewards still come only from the committed reducer, once per chick.
function batchStatusLabel(){
  const eggs=state.batch?.eggs.filter(e=>!e.collected)??[];
  return !eggs.length?'开火 · 本锅待开始':eggs.some(e=>['ready','hatching'].includes(e.status))?'收取全部孵好的伙伴':'本锅孵化中';
}
function batchStatusAction(){
  const eggs=state.batch?.eggs??[],open=eggs.filter(e=>!e.collected);
  if(!open.length){requestCook(state.batch?.tool??0);return;}
  const ready=eggs.map((e,i)=>i).filter(i=>!eggs[i].collected&&!pendingHarvest.has(i)&&['ready','hatching'].includes(eggs[i].status));
  if(ready.length){ready.forEach((i,k)=>queueHarvest(i,k>0));return;}
  const left=Math.max(1,Math.round(((E.batchReadyAt(state.batch)??now())-now())/60000));
  toast(`还要约 ${left} 分钟孵化完成`+(state.alarm?'，好了会提醒你':''),2600);
}
// A touch that only met unhatched eggs says how long the first one still needs.
function eggWaitHint(touched){
  const eggs=state.batch?.eggs??[],list=[...touched].map(i=>eggs[i]);
  if(!list.length||list.some(e=>!e||e.collected||['ready','hatching'].includes(e.status)))return;
  const left=Math.round((list[0].openAt-now())/60000);
  toast(left>=1?`这颗蛋还要约 ${left} 分钟`:'快孵好了，再等一下',2200);
}
function queueHarvest(index,quiet=false){
  const e=state.batch?.eggs[index];
  if(dialogs.children.length||panel||recoveryError||!e||e.collected||pendingHarvest.has(index)||!['ready','hatching'].includes(e.status))return false;
  const born=performance.now(),bonus=state.batch.rules?.pickGold&&e.gold?1:0;
  pendingHarvest.add(index);
  flights.push({born,index,e:{...e},x:e.x-30,y:e.y-30});feedbacks.push({born,index,x:e.x,y:e.y,amount:1+bonus});
  if(!quiet)sound(1);clearTimeout(harvestTimer);harvestTimer=setTimeout(flushHarvest,MOTION.flightMs);
  return true;
}
function flushHarvest(){
  clearTimeout(harvestTimer);
  if(!pendingHarvest.size)return true;
  const indices=[...pendingHarvest],seen=new Set(),firsts=[];pendingHarvest.clear();
  for(const i of indices){const e=state.batch.eggs[i],k=`${e.egg}:${e.id}`;if(!seen.has(k)&&!(state.total[k]>0||state.farm[k]>0))firsts.push(i);seen.add(k);}
  const done=commitProgress((s,{now:at})=>{
    // Each collect clears the previous clue; keep the newest one found in this swipe.
    const collected=[];let clue=null;
    for(const i of indices)if(E.collect(s,i,at)){collected.push(i);clue=s.progress?.discoveryClue??clue;}
    if(s.progress&&clue)s.progress.discoveryClue=clue;
    return collected.length?collected:false;
  });
  const kept=new Set(done||[]);
  for(const list of [flights,feedbacks])for(let k=list.length-1;k>=0;k--)if(indices.includes(list[k].index)&&!kept.has(list[k].index))list.splice(k,1);
  if(!done){renderControls();return false;}
  const eggs=done.map(i=>state.batch.eggs[i]),bonus=state.batch.rules?.pickGold?eggs.filter(e=>e.gold).length:0;
  const events=[];let summary='';
  if(bonus)events.push({id:'CUL-1',text:'额外 +'+bonus+' CP'});
  if(state.progress.discoveryClue)events.push({id:'OBS-5',text:'已记下1条新线索 · 去图鉴查看'});
  if(state.batch.eggs.every(e=>e.collected)&&state.progress.lastHarvest){
    const h=state.progress.lastHarvest;summary='本批收取24只 · 基础24 CP'+(h.bonus?' · 手艺额外'+h.bonus+' CP':'');
    if(h.returned!==null)events.push({id:'CUL-3',text:'返还 '+E.label(E.ingredient(h.returned))+' ×1'});
  }
  const finished=state.batch.eggs.every(e=>e.collected);
  // Harvest card paused: if(finished)setTimeout(()=>{if(page===0&&!panel&&!dialogs.children.length&&!pendingHarvest.size&&state.batch?.eggs.every(e=>e.collected))harvestAllocationUI.openCard();},650);
  if(events.length)skillFeedback(events,summary,summary?5500:2600);else if(summary)toast(summary,5500);
  const discovered=firsts.filter(i=>kept.has(i)).map(i=>state.batch.eggs[i]);
  if(discovered.length){
    if(!events.length)toast('',5200);
    const notice=game.querySelector('.game-toast'),e=discovered[0];
    const intro=characterPortrait(e.egg,e.id)+`<span><strong>${discovered.length>1?discovered.length+' 位新伙伴':'新伙伴'} · 已收入图鉴</strong><span data-discovery-name></span><small>去图鉴查看画像与做法</small></span>`;
    if(events.length)notice.insertAdjacentHTML('afterbegin','<div class="discovery-toast-title">'+intro+'</div>');
    else{notice.classList.add('discovery-toast');notice.innerHTML=intro;}
    notice.querySelector('[data-discovery-name]').textContent=discovered.map(e=>E.label(E.char(e.egg,e.id))).join('、');
  }
  announce(eggs.length===1?'收取'+E.label(E.char(eggs[0].egg,eggs[0].id))+'，获得'+(1+bonus)+' CP':'收取'+eggs.length+'只，获得'+(eggs.length+bonus)+' CP');
  return true;
}
function collectEgg(index){return queueHarvest(index)&&flushHarvest();}
function animateFlights(){
  const at=performance.now();
  for(let i=flights.length-1;i>=0;i--)if(at-flights[i].born>=MOTION.flightMs)flights.splice(i,1);
  for(let i=feedbacks.length-1;i>=0;i--)if(at-feedbacks[i].born>=MOTION.feedbackMs)feedbacks.splice(i,1);
}
function closePanel(){dismissToast();resetInput();panel='';panels.replaceChildren();renderControls();}
function closeDialog(){resetInput();dialogs.replaceChildren();dialogAction=null;cleaningDialog=null;updateControlFocus();lastFocused?.focus({preventScroll:true});}
// Every confirm and alert is a kit popup: plank title, the message on paper, wooden buttons.
// The message stays one paragraph (line breaks kept) so callers and tests read it as a whole.
function confirmBox(message,fn,onlyOK=false,labels={}){
  kitDialog({title:labels.title??(onlyOK?'提醒':'确认'),label:onlyOK?'鸡宝提醒':'确认一下',className:'msg-dialog',body:'<p class="gd-msg"></p>',no:onlyOK?null:(labels.no||'取消'),yes:onlyOK?'好':(labels.yes||'确认'),onYes:fn,onNo:labels.onNo??null});
  dialogs.querySelector('.gd-msg').textContent=message;placeKit();
}
function alertBox(message){confirmBox(message,null,true);}
// Kit popup (ui-kit.js): same lifecycle as confirmBox. [data-yes] runs the action; [data-no] and the close button cancel.
function kitSafeTop(){
  // Popups always start below the scene's top bars, never half over them; very short screens scroll the popup body instead.
  if(page===0){const r=findControl('clean')?.getBoundingClientRect();return r?.height?Math.round(r.bottom+4):154;}
  if(page===1){const r=document.querySelector('.farm-hud')?.getBoundingClientRect();return r?.height?Math.round(r.bottom+4):112;}
  return 70;
}
function placeKit(){const d=dialogs.querySelector('.gd');if(d)placeKitDialog(d,{safeTop:kitSafeTop(),navTop:wide?innerHeight:mainNav.getBoundingClientRect().top||innerHeight});}
// Pictures can finish laying out after the popup opens: re-place it whenever its size changes.
const kitResize=typeof ResizeObserver==='function'?new ResizeObserver(()=>placeKit()):null;
// Popups can be dragged up or off to the side to close them.
installPopupSwipe(document);
function kitDialog({title,label=title,className='',body,yes,no='取消',noAttrs='data-no',onYes,onNo=null,yesDisabled=false,yesClass='',yesAttrs=''}){
  resetInput();lastFocused=document.activeElement;
  dialogs.innerHTML=`<div class="modal-shade"></div><section class="confirm gd ${className}" role="dialog" aria-modal="true" aria-label="${label}">${kitTitle(title,noAttrs==='data-no'?'data-close':'data-close data-no')}<div class="gd-body">${body}</div><footer class="gd-actions" data-row>${no?kitButton2(no,noAttrs):''}${yes?kitButton(yes,'data-yes '+yesAttrs+(yesDisabled?' disabled':''),yesClass):''}</footer></section>`;
  dialogAction=onYes;
  const cancel=()=>{sound(2);closeDialog();};
  const second=dialogs.querySelector('.gd-actions .gd-btn2');if(second)second.onclick=onNo?()=>{closeDialog();sound(3);onNo();}:cancel;dialogs.querySelector('.gd-close').onclick=cancel;
  const yesButton=dialogs.querySelector('[data-yes]');if(yesButton)yesButton.onclick=()=>{const action=dialogAction;closeDialog();sound(1);action?.();};
  updateControlFocus();placeKit();
  const box=dialogs.querySelector('.gd');kitResize?.disconnect();kitResize?.observe(box);box.tabIndex=-1;box.focus({preventScroll:true});
  return dialogs.querySelector('.gd');
}
const STAGE_BEDS=['stage-bed-0-v6.png','stage-bed-1-v6.png','stage-bed-2-v6.png','stage-facility-3-v6.png'];
function openCleaning(){
  const view={quote:null};
  kitDialog({title:'打扫厨房',className:'cleaning-dialog',yes:'打扫',no:'先不扫',onYes:()=>{
    if(act(()=>E.clean(state,now(),view.quote))){sound(9);announce('厨房打扫好了，清洁度回到 100%。');}
  // The counter in three states (painted: clean, dusty, dirty); the kitchen's current one is lifted and ringed.
  },body:`<div class="cl-states" data-row aria-hidden="true">${[['clean','干净'],['dusty','有灰'],['dirty','变脏']].map(([k,l])=>`<span class="cl-state" data-clean-step="${k}"><img src="/web/art/golden-ui/counter-${k}.png" alt=""><b>${l}</b></span>`).join('')}</div><div class="cl-now" data-row><span class="gd-big" data-clean-percent></span><div class="cl-now-side"><div class="gd-meter" data-clean-bar></div><span class="gd-chip" data-clean-time></span></div></div><span class="sr-only" data-clean-state></span><div class="gd-label gd-optional" data-clean-label>打扫要花</div><div class="gd-row" data-row data-clean-price></div>`});
  cleaningDialog=view;
  updateCleaningStatus();placeKit();
}
function updateCleaningStatus(){
  if(page!==0&&!cleaningDialog)return;
  const info=E.kitchenCleanInfo(state,now()),control=findControl('clean');
  control?.setAttribute('aria-description',`清洁度 ${100-info.percent}%，${info.canClean?'打扫需要 '+info.cost+' CP':'暂时无需打扫'}`);
  if(!cleaningDialog)return;
  cleaningDialog.quote={cost:info.cost,lastClean:state.lastClean,kitchenLevel:state.kitchenLevel};
  const minutes=Math.ceil(info.remaining/60000),hours=Math.floor(minutes/60),rest=minutes%60,clean=100-info.percent;
  dialogs.querySelector('[data-clean-percent]').innerHTML=`${clean}<small>%</small>`;
  dialogs.querySelector('[data-clean-state]').textContent=info.dirty?'变脏了':clean>=100?'很干净':'干净';
  const step=info.dirty?'dirty':clean>=60?'clean':'dusty';
  dialogs.querySelectorAll('[data-clean-step]').forEach(el=>el.classList.toggle('on',el.dataset.cleanStep===step));
  dialogs.querySelector('[data-clean-bar]').innerHTML=kitBar(clean,'厨房清洁度');
  dialogs.querySelector('[data-clean-time]').innerHTML=kitIcon.hourglass+(info.dirty?'已经变脏':hours?`${hours} 小时后变脏`:`${rest} 分钟后变脏`);
  dialogs.querySelector('[data-clean-label]').hidden=!info.canClean;
  dialogs.querySelector('[data-clean-price]').innerHTML=!info.canClean?kitChip('','现在很干净','soft'):!info.affordable?kitChipHtml(`要 ${kitIcon.coin}${info.cost}`,'soft')+kitChip('',`还差 ${info.cost-state.cp} CP`,'soft hot'):info.dirty?kitChipHtml(`${kitIcon.coin}${info.cost}`):kitChipHtml(`现在 ${kitIcon.coin}${info.cost}`,'mini')+kitChipHtml(`全脏 ${kitIcon.coin}${info.fullCost}`,'mini hot');
  dialogs.querySelector('[data-yes]').disabled=!info.canClean||!info.affordable;
}
function panelSymbol(classes){const kind=classes.includes('regional')?'explore':classes.includes('business')||classes.includes('orders')?'shop':classes.includes('projects')?'workshop':classes.includes('regulars')?'farm':null;return kind?'<span class="panel-symbol">'+interfaceIcon(kind)+'</span>':'';}
let bookTab=null;
// kit: {skin, icon, help, short} turns the panel into a full-screen kit page (plank title, back and help buttons; see ui-kit-page.css).
function showPanel(title,body,classes='',kit=null){dismissToast();resetInput();panel=title;sound(4);
  const head=kit?.skin==='book'?kitBookHead({icon:kit.icon,help:kit.help,search:kit.search,back:kit.back,title:kit.title}):kit?kitPageHead({title:kit.short??title,icon:kit.icon,help:kit.help}):`<button class="close" aria-label="关闭">×</button><h2>${panelSymbol(classes)}${title}</h2>`;
  panels.innerHTML=`<section class="panel paper ${classes}${kit?` kp gd kp-${kit.skin}`:''}" role="dialog" aria-label="${title}"${kit?' tabindex="-1"':''}>${head}${body}</section>`;if(bookTab&&/cookbook-screen|journal-screen/.test(classes)){(panels.querySelector('.panel>.kp-head')??panels.querySelector('.panel>h2')).insertAdjacentHTML('afterend',bookNavigation(bookTab));bindBookNavigation(panels,id=>bookUI.open({tab:id}));}panels.querySelector('.close').onclick=()=>{if(panelReturn){const back=panelReturn;panelReturn=null;back();return;}if(page===3)changePage(settingsReturnPage);else if(page>=2)changePage(0);else closePanel();};updateControlFocus();if(!dialogs.children.length)(kit?panels.querySelector('.panel'):panels.querySelector('.close')).focus({preventScroll:true});}
// Kitchen upgrade: before → after, what it unlocks, the six cookware requirements and the price, in one kit popup.
const UPGRADE_GAINS={2:['调味槽 1→2','厨具 Lv.2'],3:['调味槽 2→3','厨具 Lv.3','防晒乳液'],4:['烧水壶','面包机']};
function openKitchenUpgrade(){
  const info=E.kitchenUpgradeInfo(state),number=value=>value.toLocaleString('zh-CN'),level=state.kitchenLevel;
  const thumb=l=>`<span class="gd-thumb small"><img src="/web/art/${STAGE_BEDS[l]??STAGE_BEDS[0]}" alt=""><em>Lv.${l+1}</em></span>`;
  if(info.maxed){
    kitDialog({title:'厨房',label:'厨房等级',className:'upgrade-dialog',no:null,yes:'好',body:`<div class="gd-row">${thumb(level)}</div><div class="gd-row">${kitChip('','已是最高等级')}</div><div class="gd-row">${kitChip('','烧水壶','mini')}${kitChip('','面包机','mini')}</div>`});
    return;
  }
  const met=info.requirements.filter(item=>item.met).length;
  const tools=info.requirements.map(item=>`<span class="gd-tool${item.met?' done':''}" aria-label="${E.label(E.tool(item.id))} ${item.met?'已满足':`需要 Lv.${item.requiredLevel}`}">${toolPortrait(item.id,Math.max(0,(item.currentLevel||1)-1))}<b>${item.met?'<img src="/web/art/golden-business/family-check.png" alt="">':item.currentLevel?`Lv.${item.requiredLevel}`:'未拥有'}</b></span>`).join('');
  const body=`<div class="gd-row gd-optional" data-row style="gap:8px">${thumb(level)}${kitArrow}${thumb(info.targetLevel-1)}</div><div class="gd-row gd-gains">${kitChip('',`Lv.${level+1} › Lv.${info.targetLevel}`,'mini gd-short')}${(UPGRADE_GAINS[info.targetLevel]??[]).map(g=>kitChip('',g,'mini')).join('')}</div>${kitLabel(info.toolsReady?'厨具都准备好了':`先升级厨具 ${met}/${info.requirements.length}`)}<div class="gd-tools">${tools}</div><div class="gd-row">${kitChipHtml(`${kitIcon.coin}${number(info.cost)}`)}${info.affordable?'':kitChip('',`还差 ${number(info.cost-state.cp)}`,'hot')}</div>`;
  kitDialog({title:'升级厨房',className:'upgrade-dialog',body,no:'去商店',onNo:()=>{shopUI.setTab(0);changePage(2);},yes:'升级',yesAttrs:'data-upgrade-kitchen',yesDisabled:!info.canUpgrade,onYes:()=>{
    // A stale popup must never buy the following level as well.
    const expectedLevel=level;
    if(!E.kitchenUpgradeInfo(state).canUpgrade||state.kitchenLevel!==expectedLevel)return;
    if(!act(()=>{E.upgradeKitchen(state,now());}))return;
    sound(7);paint();const stage=kitchenStage(state.kitchenLevel);
    announce(`厨房已升级至 Lv.${state.kitchenLevel+1}，${stage.title}准备好了。`);
    kitDialog({title:'升级成功',className:'upgrade-done',no:null,yes:'好',body:`<div class="gd-row">${thumb(state.kitchenLevel)}</div><div class="gd-row">${kitChip('',`Lv.${state.kitchenLevel+1} · ${stage.title}`)}</div><span class="gd-note">厨房打扫干净了，这一锅照常孵化</span>`});
  }});
}
// The seasoning page leads with advice for one cookware: known recipes ranked by
// expected income, open orders/projects, or new partners. Picking a set only fills
// the draft; missing seasonings can be bought in the same step.
function openIngredients({tool=null}={}){nextBatchUI.open({tool});}
const collectionUI=createCollectionUI({skillFeedback,toolPortrait,ingredientPortrait,getState:()=>state,getPage:()=>page,getNow:now,panels,showPanel,confirmBox,alertBox,act,sound,characterPortrait,makeWalkers,changePage,openActivities,openJournal,openRecipeBook,openDuckShop:()=>{shopUI.setTab(2);changePage(2);},openObservation:key=>workshopUI.observe(key,()=>collectionUI.renderCollection()),openClues:()=>clueBookUI.open({back:()=>collectionUI.renderCollection()}),openWorkshop:()=>workshopUI.open(),openBooks:()=>bookUI.open({tab:'collections'}),openBookTab:tab=>bookUI.open({tab}),openInventory:key=>{changePage(1);warehouseUI.open({bird:key});},prepareRecipe:key=>prepareBookRecipe(key),openUse:use=>{if(use.kind==='collection')collectionsUI.open({id:use.id});else if(use.kind==='region'){const fromBook=page===4;if(!fromBook)changePage(6);if(fromBook)panelReturn=()=>bookUI.open({tab:'species'});regionalUI.open({regionId:use.id,tab:'record',recipeId:use.recipeId});}else openTrade(use.kind==='menu'?'business':'orders');}});
const shopUI=createShopUI({characterPortrait,skillFeedback,notify:message=>toast(message),openKitchenUpgrade:()=>openKitchenUpgrade(),getState:()=>state,panels,showPanel,confirmBox,alertBox,act,sound,toolPortrait,changePage,openActivities,openJournal,openRecipeBook,openCookware:id=>{toolScroll=toolScrollFor(id,TOOL_SCROLL_MAX);changePage(0);},openMaterialLore:id=>{const regionId=E.ingredient(id)?.region??({75:'V',76:'V',77:'R',78:'R',79:'T',80:'T',81:'B',82:'B'})[id];panelReturn=()=>shopUI.openIngredientDetails(id);regionalUI.open({regionId,tab:'record',materialId:id});},returnFromShop:()=>{if(shopReturnAction){const back=shopReturnAction;shopReturnAction=null;back();}else changePage(shopReturnPage);}});
const recipeBookUI=createRecipeBookUI({getState:()=>state,getNow:now,panels,showPanel,characterPortrait,toolPortrait,ingredientPortrait,
  onClose:()=>{if(page===4)collectionUI.renderCollection();else{changePage(4);}},
  onPrepare:key=>{const prepare=()=>{const r=commitProgress(s=>prepareDiscoveredRecipe(s,key,now()));if(!r){recipeBookUI.refresh();return;}toolScroll=toolScrollFor(r.toolId,TOOL_SCROLL_MAX);changePage(0);toast(`${r.name}配方已选好 · 使用${r.toolName}\n当前一批保留，开火前会再次确认。`);};if(state.selected.length)confirmBox('替换下一批的蛋种与材料。\n当前孵化继续，不会立即消耗材料或 CP。',prepare,false,{yes:'换成这份配方'});else prepare();},
  openIngredients:ids=>supplyFromRecipe(1,()=>shopUI.openIngredientDetails(ids[0])),
  openTools:id=>supplyFromRecipe(0,()=>shopUI.openToolDetails(Math.max(0,id))),
  openKitchen:()=>{changePage(0);openKitchenUpgrade();},
  openActivities,openDuckShop:()=>supplyFromRecipe(2),openCalendar:()=>openJournal('calendar')});
const workshopUI=createWorkshopUI({skillFeedback,getState:()=>state,getNow:now,panels,showPanel,confirmBox,alertBox,commit:commitProgress,characterPortrait,openRegional:()=>openExplore(),openBusiness:()=>openTrade('business'),businessNote:()=>state.expansion.business?.active?'营业中':businessUI.unreadReport()?'有新账单':'',goKitchen:()=>{if(page===0)closePanel();else changePage(0);},prepareTool:id=>{toolScroll=toolScrollFor(id,TOOL_SCROLL_MAX);if(page===0)closePanel();else changePage(0);}});
const nextBatchUI=createNextBatchUI({getState:()=>state,getNow:now,showPanel,panels,closePanel,confirmBox,alertBox,act,sound,characterPortrait,ingredientPortrait,toolPortrait,startCook:startCookNow,openClueBook:(tab='focus')=>clueBookUI.open({tab,back:()=>nextBatchUI.open({keep:true})}),openJourney:regionId=>openJourneyAt(regionId),
  getMenuId:()=>businessUI.todayMenu(),openBusiness:()=>openTrade('business'),
  openRestock:after=>{shopUI.setTab(1);changePage(2);shopReturnAction=()=>{changePage(0);after();};}});
const warehouseUI=createWarehouseUI({getState:()=>state,getNow:now,showPanel,panels,act,confirmBox,alertBox,sound,characterPortrait,ingredientPortrait,skillFeedback,
  showCharacter:(egg,id)=>collectionUI.showCharacter(egg,id,()=>warehouseUI.open()),afterSale:()=>{if(page===1)makeWalkers();},openLedger:()=>{panelReturn=()=>warehouseUI.open();collectionUI.renderAlbum();},
  openShopFor:id=>{const origin=page;shopUI.setTab(1);changePage(2);if(id!==null)shopUI.openIngredientDetails(id);shopReturnAction=()=>{changePage(origin);warehouseUI.open({tab:'mats'});};}});
function cookFor(key,{menu}={}){if(page!==0)changePage(0);else closePanel();nextBatchUI.open({preset:{key,menu}});}
// 生意主页 (batch 3): a menu slot or an order's 「去做」 opens 下一锅 with that goal; 订单情报 leads to the 线索册 or the map.
function openNextBatchGoal(goal){if(page!==0)changePage(0);else closePanel();nextBatchUI.open({goal});}
const businessUI=createBusinessUI({getState:()=>state,getNow:now,commitProgress,showPanel,panels,alertBox,confirmBox,kitDialog,characterPortrait,openSettings:()=>changePage(3),cookFor,openClue:key=>{const [egg,id]=key.split(':').map(Number);showCharacter(egg,id);},openOrders:()=>openTrade('orders'),openRegulars:id=>openTrade('regulars',id),openProjects:id=>openTrade('projects',id),goKitchen:()=>{if(page===0)closePanel();else changePage(0);},
  openGoal:openNextBatchGoal,openClueBook:key=>clueBookUI.open({focusKey:key,back:()=>openTrade('business')}),openJourney:regionId=>openJourneyAt(regionId),
  openStory:()=>{panelReturn=()=>openTrade('business');workshopUI.open('story',{back:()=>openTrade('business')});}});
const orderUI=createOrderUI({getState:()=>state,getNow:now,commitProgress,showPanel,panels,alertBox,confirmBox,characterPortrait,openBusiness:()=>openTrade('business'),openRegulars:()=>openTrade('regulars'),openProjects:()=>openTrade('projects'),openStory:()=>{panelReturn=()=>openTrade('orders');workshopUI.open('story',{back:()=>openTrade('orders')});},openRecipe:key=>openRecipeFromOrder(key),goKitchen:()=>{if(page===0)closePanel();else changePage(0);}});
const regularUI=createRegularUI({getState:()=>state,commitProgress,showPanel,panels,openBusiness:()=>openTrade('business'),openOrders:()=>openTrade('orders'),openProjects:()=>openTrade('projects')});
const projectUI=createProjectUI({getState:()=>state,commitProgress,showPanel,panels,alertBox,confirmBox,characterPortrait,openBusiness:()=>openTrade('business'),openOrders:()=>openTrade('orders'),openRegulars:()=>openTrade('regulars')});
const collectionsUI=createCollectionsUI({showSpecies:(egg,id,back)=>collectionUI.showCharacter(egg,id,back),getState:()=>state,commitProgress,showPanel,panels,alertBox,characterPortrait,openBookTab:tab=>bookUI.open({tab}),openJournal,openShrine,goBack:()=>{if(page===4)bookUI.open();else closePanel();}});
const clueBookUI=createClueBookUI({getState:()=>state,getNow:now,panels,showPanel,characterPortrait,toolPortrait,ingredientPortrait,commit:commitProgress,confirmBox,alertBox,openObservation:(key,back)=>workshopUI.observe(key,back),openSpecies:()=>collectionUI.renderCollection(),openBookTab:tab=>bookUI.open({tab}),
  openNextBatch:()=>{if(page!==0)changePage(0);else closePanel();nextBatchUI.open();},
  openRecipe:recipe=>{if(page!==0)changePage(0);else closePanel();nextBatchUI.open({recipe});},
  openGuess:guess=>{if(page!==0)changePage(0);else closePanel();nextBatchUI.open({guess});},
  openSkill:id=>workshopUI.open('skills',{skill:id,back:()=>clueBookUI.open({keep:true})}),
  openJourney:(regionId,key)=>openJourneyAt(regionId,key)});
// 寻访 map with one region selected (from 下一锅's track bar or a 线索册 「去寻访」), optionally for one partner.
function openJourneyAt(regionId,forKey=null){if(page!==6)changePage(6);regionalUI.open({regionId,forKey,map:true});}
const bookUI=createBookUI({getState:()=>state,getNow:now,panels,showPanel,openSpecies:()=>collectionUI.renderCollection(),openCollections:options=>collectionsUI.open(options),openRegion:options=>{panelReturn=()=>bookUI.open({tab:'lore'});regionalUI.open(options);},openRecipeBook,openJournal,openActivities,characterPortrait,showSpecies:(egg,id)=>collectionUI.showCharacter(egg,id),openTarget:pin=>{if(pin.kind==='collection')collectionsUI.open({id:pin.id});else if(pin.kind==='recipe')regionalUI.open({tab:'record'});else openTrade(pin.kind==='regular'?'regulars':'projects',pin.id);}});
function supplyFromRecipe(tab,detail=()=>{}){const origin=page;shopUI.setTab(tab);changePage(2);shopReturnAction=()=>{changePage(origin);recipeBookUI.resume();};detail();}
function supplyFromJournal(tab,detail=()=>{}){const origin=page;shopUI.setTab(tab);changePage(2);shopReturnAction=()=>{changePage(origin);journalUI.resume();};detail();}
function prepareBookRecipe(key){const r=commitProgress(s=>prepareDiscoveredRecipe(s,key,now()));if(!r)return;toolScroll=toolScrollFor(r.toolId,TOOL_SCROLL_MAX);changePage(0);toast('下一锅配方已准备，当前锅仍保留。确认开火才扣材料和 CP。');}
const harvestAllocationUI=createHarvestAllocationUI({getState:()=>state,getNow:now,showPanel,panels,commitProgress,confirmBox,closePanel,characterPortrait,openNextBatch:()=>nextBatchUI.open(),openBusinessDraft:stock=>{businessUI.prepareDraft(stock);tradeView={kind:'business'};if(page!==5)changePage(5);businessUI.open('prepare');}});
const regionalUI=createRegionalUI({getState:()=>state,getNow:now,commitProgress,showPanel,panels,alertBox,confirmBox,kitDialog,characterPortrait,skillFeedback,openClueBook:(key,back)=>clueBookUI.open({focusKey:key,back:back??(()=>regionalUI.open({map:true}))}),goKitchen:id=>{if(Number.isInteger(id))toolScroll=toolScrollFor(id,TOOL_SCROLL_MAX);if(page===0)closePanel();else changePage(0);}});
function openRecipeBook(options={}){if(!options.back&&page!==4)changePage(4);bookTab=options.fromBook?'recipes':null;recipeBookUI.open(options);}
const activitiesUI=createActivitiesUI({getState:()=>state,getNow:now,panels,showPanel,confirmBox,alertBox,commitClaim:commitActivity,sound,characterPortrait,ingredientPortrait,openShrine,prepareGift:prepareGiftFromUI,onClose:()=>{if(page===2)openShop();else if(page===4)collectionUI.renderCollection();else closePanel();},openKitchen:()=>{if(page===0)closePanel();else changePage(0);}});
let shrineFromFarm=false;
const shrineUI=createShrineUI({getState:()=>state,getNow:now,panels,showPanel,sound,characterPortrait,ingredientPortrait,openJournal,
  commitDraw:()=>commitActivity('shrine-gift'),commitReward:id=>commitProgress(candidate=>claimShrineGoal(candidate,id)),prepareGift:prepareGiftFromUI,
  getBackLabel:()=>shrineFromFarm?'‹ 返回营地':'‹ 返回',onClose:()=>closePanel(),openActivityTab:tab=>activitiesUI.open({tab}),openLetters:()=>activitiesUI.detail('shrine'),openGifts:()=>activitiesUI.detail('yokai'),openTravel:()=>activitiesUI.detail('time-travel'),
  openRecipes:()=>openRecipeBook({tool:8}),openDuckShop:()=>{shopUI.setTab(2);changePage(2);},openObservation:key=>workshopUI.observe(key,()=>collectionUI.renderCollection()),openWorkshop:()=>workshopUI.open(),openBooks:()=>bookUI.open({tab:'collections'}),openBookTab:tab=>bookUI.open({tab}),openInventory:key=>{changePage(1);warehouseUI.open({bird:key});},prepareRecipe:key=>prepareBookRecipe(key),openUse:use=>{if(use.kind==='collection')collectionsUI.open({id:use.id});else if(use.kind==='region'){const fromBook=page===4;if(!fromBook)changePage(6);if(fromBook)panelReturn=()=>bookUI.open({tab:'species'});regionalUI.open({regionId:use.id,tab:'record',recipeId:use.recipeId});}else openTrade(use.kind==='menu'?'business':'orders');}});
const settingsUI=createSettingsUI({getState:()=>state,panels,showPanel,alertBox,save,music,sound,characterPortrait,toolPortrait,changePage,platform,toggleHatchAlarm,exportProgress,importProgress,openJournal,openWorkshop:()=>workshopUI.open(),getSaveError:()=>lastSaveError,returnFromSettings:()=>changePage(settingsReturnPage),returnToTitle:()=>{page=-1;closePanel();resize();music();renderControls();paint();}});
function openActivities(target){activitiesUI.open(target);}
let journalReturn=()=>closePanel();
const journalNotices=new Set();
const journalUI=createJournalUI({getState:()=>state,getNow:now,panels,showPanel,characterPortrait,ingredientPortrait,toolPortrait,fromBook:()=>bookTab==='calendar',openRecipeBook,
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
function openJournal(tab='calendar',key,fromBook=false){
  bookTab=fromBook?'calendar':null;
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
  // A calendar note: icon, one line, tap to open 图鉴 · 日历.
  const element=game.querySelector('.game-toast');element.classList.add('calendar-toast');element.setAttribute('role','button');element.tabIndex=0;
  element.innerHTML=`${interfaceIcon('calendar')}<span>${element.textContent}</span><b aria-hidden="true">›</b>`;
  element.onclick=()=>{dismissToast();changePage(4);bookUI.open({tab:'calendar'});};
}
function openShrine(tab='draw'){shrineFromFarm=page===1;shrineUI.open(tab);}

function repairFarm(){
  const hp=E.farmHP(state,now());if(hp>=100){alertBox('围栏完好，暂时不需要整修。');return;}
  const cost=E.repairCost(state,now()),short=Math.max(0,cost-state.cp);
  kitDialog({title:'修复围栏',className:'repair-dialog',yes:'修好',no:'先不修',yesDisabled:short>0,onYes:()=>act(()=>{E.repair(state,now());sound(10);announce('农场已经整修好了。');}),
    body:`<div class="rp-fences" data-row><span class="rp-fence"><img src="/web/art/golden-ui/fence-broken.png" alt="">${kitBig(hp,'%')}</span>${kitArrow}<span class="rp-fence is-fixed"><img src="/web/art/golden-ui/fence-fixed.png" alt="">${kitBig(100,'%')}</span></div>${kitBar(hp,'农场完好度')}<div class="gd-row">${kitChipHtml(`${kitIcon.coin}${cost}`)}${short?kitChip('',`还差 ${short} CP`,'hot'):''}</div>`});
}
const farmMap=createFarmMapUI({stage:game,onPlace:id=>{consumeReleaseClick=true;openFarmPlace(id);},onRepair:repairFarm,onSettings:()=>changePage(3),onKitchen:()=>changePage(0),onLevel:()=>{changePage(0);openKitchenUpgrade();},avatar:()=>characterPortrait(0,0)});
function updateFarmMap(){
  if(page===1){const shrine=shrineBook(state,now());shrineReady=shrine.gift.available||shrine.goals.some(g=>g.available);}
  farmMap.update(state,{visible:page===1,hp:E.farmHP(state,now()),shrineReady});
}
function openFarmPlace(id){
  if(id==='house')warehouseUI.open();
  else if(id==='market')openTrade('business');
  else if(id==='shrine')openShrine();
  else if(id==='display')collectionsUI.open({tab:'mementos',id:null});
  else if(id==='dock')openExplore();
}
function commitActivity(id){return commitProgress(candidate=>claimActivity(candidate,id,now()));}
function commitProgress(mutate){
  if(recoveryError)return null;
  if(pendingHarvest.size)flushHarvest();
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
  clearTimeout(toastTimer);element.className='game-toast';element.removeAttribute('tabindex');element.setAttribute('role','status');element.onclick=null;element.textContent=message;element.hidden=false;toastTimer=setTimeout(()=>{element.hidden=true;},duration);
}
function skillFeedback(events,summary='',duration=4200){
  // A receipt belongs to its existing dialog; never place a second overlay over it.
  const modal=dialogs.querySelector('.confirm');
  if(modal){
    const receipt=modal.querySelector('.skill-receipt')??document.createElement('div');receipt.className='skill-receipt';
    renderSkillFeedback(receipt,events,summary);const body=modal.querySelector('.gd-body');if(body)body.append(receipt);else modal.querySelector('footer').before(receipt);placeKit();return;
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
    // Vendor battery savers drop alarms of optimised apps, so offer the exemption once per switch-on.
    if(state.alarm&&platform.info.android&&platform.notificationStatus().backgroundAllowed===false)
      confirmBox('手机省电可能让孵化提醒晚到或收不到。允许鸡宝厨房在后台运行，孵化完成时才能准时提醒你。',()=>platform.requestBackgroundRun(),false,{title:'让提醒更准时',yes:'去允许',no:'以后再说'});
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
  if(result){renderControls();paint();if(result.lost){collectionUI.refresh();alertBox(`脱逃事件：逃走了${result.lost}只伙伴！`);}}
  music();
}
function entryInputBlocked(){return performance.now()<entryInputBlockedUntil;}
function gamePoint(ev){const r=canvas.getBoundingClientRect();return {x:(ev.clientX-r.left)/r.width*L.width,y:(ev.clientY-r.top)/r.height*L.height};}
// The browser's own hit test is the ground truth for what the finger is on. If a
// WebView ever disagrees with gamePoint's geometry again, buttons still work and
// the settings device check can report the offset.
function hotspotUnder(ev){return document.elementFromPoint(ev.clientX,ev.clientY)?.closest('.hotspot')?.dataset.controlId;}
const touchDiag=window.__chickDiag?.touch??{aligned:0,offset:0,rescued:0};
function eggHitRect(e){return goldenEggHit(state.kitchenLevel,state.batch.eggs.indexOf(e));}
// Nest eggs overlap: test each egg's oval rather than its box, so the visible
// part of an egg behind another is still reachable.
function insideEgg(r,x,y){return ((x-r.x-r.w/2)/(r.w/2))**2+((y-r.y-r.h/2)/(r.h/2))**2<=1;}
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
  resetInput();flushHarvest();
  if(movedFarm)renderControls();
}
function hitEggs(x,y,visited){
  if(!contains(goldenRect('eggArea',state.kitchenLevel),x,y))return;
  const list=state.batch?.eggs??[];
  for(let i=list.length-1;i>=0;i--){
    const e=list[i];
    if(e.collected||pendingHarvest.has(i)||visited.has(i)||!insideEgg(eggHitRect(e),x,y))continue;
    // Keep the same front-to-back hit order as the visible sprites. A held
    // pointer must move before it can reach a chick behind this one.
    visited.add(i);queueHarvest(i);break;
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
  if(button&&!id.startsWith('egg:'))touchDiag[contains(button.hitRect,p.x,p.y)?'aligned':'offset']++;
  if(page===0&&!panel&&p.y>=goldenRect('slot:0').y&&p.y<=goldenRect('slot:0').y+goldenRect('slot:0').h&&(!button||id.startsWith('tool:'))){
    pointer={id:ev.pointerId,kind:'tools',controlId:id,rect:button?.hitRect,page,...beginToolDrag(p,toolScroll)};
    pressedId=id??'';button?.focus({preventScroll:true});
  }else if(button&&!id.startsWith('egg:')){
    pointer={id:ev.pointerId,kind:'button',controlId:id,rect:button.hitRect,page};
    pressedId=id;button.focus({preventScroll:true});
  }else if(page===0&&!panel&&contains(goldenRect('eggArea',state.kitchenLevel),p.x,p.y)){
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
    moveToolDrag(pointer,p,88*320/390,TOOL_SCROLL_MAX);
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
  if(gesture.kind==='tools')moveToolDrag(gesture,p,88*320/390,TOOL_SCROLL_MAX);
  // A released harvest commits when its last chick lands (queueHarvest's timer),
  // so the synchronous save never freezes the flight on its first frame.
  resetInput();
  if(gesture.kind==='harvest')eggWaitHint(gesture.visited);
  const released=(gesture.kind==='button'||(gesture.kind==='tools'&&!gesture.drag&&!gesture.cancelled))&&gesture.rect&&gesture.page===page;
  const onTarget=released&&(contains(gesture.rect,p.x,p.y)||(hotspotUnder(ev)===gesture.controlId&&++touchDiag.rescued));
  if(onTarget){
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
// Kitchen art is authored at 3x; a 3.5x phone screen would only add pixels to repaint.
const deviceRatio=()=>Math.min(3,window.devicePixelRatio||1);
let pixelRatio=deviceRatio();
// Information pages use physical CSS pixels. Only the unchanged scene scales.
let wide=false;
function resize(){
  cancelInput();pixelRatio=deviceRatio();
  const baseFont=parseFloat(getComputedStyle(document.documentElement).fontSize)||16;
  wide=newOperationsEnabled('ui')&&innerWidth>=Math.max(1024,64*baseFont)&&innerHeight>=650&&page>=0&&page!==1;
  // One bottom bar on every page (Farm UI V1's reusable five-item bar); the wide
  // desktop layout keeps its side column, and the cover has no bar at all.
  game.classList.toggle('is-farm',page===1);mainNav.dataset.variant=page>=0&&!wide?'bottom-nav-compact-v1':'';
  game.classList.toggle('is-wide',wide);game.classList.toggle('is-cover',page<0);game.classList.toggle('is-golden-kitchen',page===0);
  const areaWidth=wide?Math.min(560,innerWidth-420):Math.min(560,innerWidth);
  const areaHeight=game.clientHeight-(page<0?0:wide?24:mainNav.getBoundingClientRect().height);
  // The scene no longer contains a bottom bar. Add back its 66 logical pixels
  // when choosing height, so the visible artwork fills the measured grid row.
  const logicalHeight=page<0?fitViewport(areaWidth,areaHeight,pixelRatio).logicalHeight:Math.max(568,Math.min(866,320*areaHeight/areaWidth+66));
  setViewportHeight(logicalHeight);
  game.style.setProperty('--game-height',logicalHeight+'px');game.style.setProperty('--extra-height',L.extra+'px');
  const visibleHeight=page<0?logicalHeight:L.navY-4,scale=Math.min(areaWidth/320,areaHeight/visibleHeight);
  const width=Math.max(1,Math.round(320*scale*pixelRatio)),height=Math.max(1,Math.round(logicalHeight*scale*pixelRatio));
  // Pre-128 WebViews omit CSS zoom from getBoundingClientRect(), while pointer
  // coordinates are visual pixels. A transform keeps paint and hit testing in
  // the same coordinate space on both old and current Android kernels.
  sceneStage.style.transform=`scale(${scale})`;sceneStage.style.height=visibleHeight+'px';
  const sideSpace=Math.max(0,(areaWidth-320*scale)/2);
  quickActions.style.setProperty('--scene-top',Math.max(0,(areaHeight-visibleHeight*scale)/2)+'px');
  quickActions.style.setProperty('--quick-offset',62*scale+'px');
  quickActions.style.setProperty('--quick-inset',(sideSpace>60?sideSpace-58:sideSpace+5)+'px');
  if(canvas.width!==width||canvas.height!==height){canvas.width=width;canvas.height=height;}
  if(loaded)renderControls();updateDesk();placeKit();
}
installGameFrame(game);
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
const primary=[...KITCHEN_ART.map(p=>p.slice(1)),FARM_MAP.slice(1),...FARM_ART_FILES.map(p=>p.slice(1)),...ASSET_FILES.map(p=>p.slice(1)),
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
function eggAnimationOnly(a,b){
  const facts=s=>JSON.stringify({...s,batch:s.batch&&{...s.batch,eggs:s.batch.eggs.map(({status,animationAt,...e})=>({...e,opened:status!=='egg'}))}});
  return facts(a)===facts(b);
}
function advanceClock(){const real=Date.now();virtualNow+=(real-previousReal)*speed;previousReal=real;}
setInterval(()=>{advanceClock();tick++;if(recoveryError)return;
  const previous=state,candidate=structuredClone(state),before=JSON.stringify(candidate),wasDirty=candidate.dirty;
  const world=E.advanceWorld(candidate,now()),events=E.updateBatch(candidate,now()),becameDirty=!wasDirty&&candidate.dirty;
  let alarmed=false;
  if(candidate.alarm&&E.batchReadyAt(candidate.batch)!==null&&!candidate.batch.alarmed&&now()>=E.batchReadyAt(candidate.batch)){candidate.batch.alarmed=true;alarmed=true;}
  // Logical clock checkpoints once per minute; state transitions save immediately,
  // except an egg's crack→hatch→ready animation: its species is fixed when it cracks
  // and the rest follows from time, so it rides along with the next save instead of
  // freezing the phone for a synchronous write twice more per egg.
  const logicalAt=candidate.progress.logicalAt;candidate.progress.logicalAt=previous.progress.logicalAt;
  const checkpoint=tick%600===0&&logicalAt!==previous.progress.logicalAt;
  if(JSON.stringify(candidate)!==before||world.returned||checkpoint){
    const quiet=!world.returned&&!checkpoint&&eggAnimationOnly(previous,candidate);
    candidate.progress.logicalAt=logicalAt;state=candidate;
    if(!quiet&&!save())state=previous;
    else{if(page===0)[...new Set(events)].forEach(s=>sound(s==='break'?11:s==='duck'?14:12));if(alarmed){sound(5);announce('这一批鸡宝已经孵化，请及时收取。');}if(becameDirty)announce('厨房有些脏了，可以打扫。');if(world.returned){makeWalkers();refreshPanel();toast('寻访伙伴已归队，奖励在篮中等你。');}renderControls();}
  }
  if(!state.progress.tutorialSeen&&collectedTotal(state)>=24&&!panel&&!dialogs.children.length){if(commitProgress(s=>{s.progress.tutorialSeen=true;return true;}))toast('手艺开放了！点击厨房的「手艺」查看，完成第一批已获得2点。',8000);}
  if(tick%600===0){maybeDiscoveryNotice();journalUI.updateTime();}animateFlights();for(const w of walkers){w.turn+=Math.floor(Math.random()*2);if(w.turn>=30){w.turn=0;w.dir=Math.random()<.5?-1:1;}}if(tick%10===0){if(page===1)updateFarmMap();updateDesk();updateCleaningStatus();workshopUI.updateTime();regionalUI.refresh();businessUI.refresh();reportStatus();}
},100);

// Repaint every frame only while something moves (cover, farm walkers, flights,
// a held pointer, or just after input/state changes); an idle scene repaints four
// times a second, which keeps countdowns current without burning the main thread.
function wake(){activeUntil=performance.now()+1500;}
for(const type of ['pointerdown','keydown','wheel'])window.addEventListener(type,wake,{capture:true,passive:true});
function renderFrame(at=performance.now()){
  if(pixelRatio!==deviceRatio())resize();
  if(page<0||page===1||pointer||pressedId||flights.length||feedbacks.length||at<activeUntil||at-lastPaint>=250){paint();lastPaint=at;}
  requestAnimationFrame(renderFrame);
}
