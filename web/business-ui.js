import {interfaceIcon} from './ui-icons.js';
import {REGIONAL,resolveSpecies,CONTENT_TEXT} from './content-registry.js';
import {speciesView} from './collection-ui.js';
import {freeCount,homeCount,inventoryView,lockedCount} from './inventory.js';
import {suggestBusinessStock,businessForecast,menuOverview} from './business-advisor.js';
import {recipeForSpecies} from './seasoning-advisor.js';
import {starsMarkup,slotMarkup,helpCardsMarkup,firstVisitGuide} from './game-frame.js';
import {kitChip,kitChipHtml,kitBig,kitButton,kitButton2,kitLabel,kitCell,kitIcon,kitArt} from './ui-kit.js';
import {openBusiness,prepareBusiness} from './business.js';
import {closeBusinessTimeline} from './timeline.js';
import {businessModel} from './business-model.js';
import {uiPreference,setUiPreference} from './ui-preferences.js';
import {assignMenuRoles} from './menu-model.js';
import {pinnedRegular} from './regular-ui.js';
import {saveMenuPreset,deleteMenuPreset,projectComplete,MAX_PRESETS} from './projects.js';
import {businessGoldenMarkup} from './business-golden-ui.js';
import {businessLedgerMarkup} from './business-ledger-ui.js';
import {shopSubpageHeader,shopSubpagePaper,shopSubpageDialog,bindShopSubpage} from './business-subpages.js';
import {todayMenuId,menuCore,orderBoard,regularsSummary,projectsSummary} from './business-home.js';
import {businessHomeMarkup,orderCardMarkup,orderSheetMarkup,orderDoneMarkup,parseRef,refKey} from './business-home-ui.js';
import {completeOrderNow,completeStoryNow,orderStatus,spareFor} from './order-delivery.js';
import {skipProposal,cancelOrderInstance} from './orders.js';
import {speciesDiscovered} from './species-state.js';

const esc=value=>String(value??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const number=value=>Number(value??0).toLocaleString('zh-CN');
const remaining=ms=>{const mins=Math.max(0,Math.ceil(ms/60000));return mins>=60?`${Math.floor(mins/60)}小时${mins%60}分`:`${mins}分钟`;};
const tierLabel={ordinary:'普通营业',suitable:'搭配合适',complete:'完整菜单'};

export function createBusinessUI({getState,getNow,commitProgress,showPanel,panels,confirmBox,alertBox,kitDialog,characterPortrait,goKitchen,openOrders,openRegulars,openProjects,openSettings,cookFor,openClue,openGoal,openClueBook,openJourney,openStory}){
  // menuId: today's menu (the player's last pick, kept as a UI preference; else the one that sells best now).
  // manualStock: the player changed the baskets by hand; until then the stock follows the auto plan as the farm changes.
  let mode='prepare',menuId=null,manualStock=false,orderRef=null,stock={},placements={},keepOne=true,useRewards=false,tendency='regulars',filter='theme',busy=false,scrollTop=0,lastRenderedState=null,manualPlacement=false,autofillNote='',moreOpen=false,basketFrom=null;
  let sheet=null,mainScrollTop=0,returnSheetButton=null,basketKey=null,autoFilledFor=null;
  // The keep-one rule is the save's own inventory policy (设置), not a per-sheet checkbox.
  // The player's per-kind locks (warehouse) decide how many stay at home; the old global switch is gone.
  const policyKeepOne=()=>true;
  const usable=row=>Math.max(0,Math.min(row.free,row.home-lockedCount(getState(),row.key)));
  const find=selector=>panels.querySelector(selector);
  const title=()=>CONTENT_TEXT[menuId]?.name??'营业';
  function pickMenu(id){if(menuId!==id){menuId=id;stock={};manualStock=false;autoFilledFor=null;}setUiPreference('businessMenu',id);}
  const selectedKeys=()=>Object.keys(stock).filter(key=>stock[key]>0);
  function rows(){const s=getState(),menu=REGIONAL.menus.find(m=>m.id===menuId);return Object.keys(s.farm).filter(key=>s.farm[key]>0&&resolveSpecies(key)?.edible).map(key=>{const [egg,id]=key.split(':').map(Number),row=speciesView(s,egg,id);const v=inventoryView(s,key);return {...row,free:freeCount(s,key),home:homeCount(s,key),reserved:v.Q,away:v.R,fits:menu.roles.some(r=>r.allowed.includes(key)),locked:Array.isArray(s.expansion.inventoryPolicy?.collectionLocks)?s.expansion.inventoryPolicy.collectionLocks.includes(key):!!s.expansion.inventoryPolicy?.collectionLocks?.[key]};}).filter(row=>row.known).sort((a,b)=>Number(b.fits)-Number(a.fits)||a.egg-b.egg||a.id-b.id);}
  function options(){
    const selected=Object.fromEntries(Object.entries(stock).filter(([,n])=>n>0)),menu=REGIONAL.menus.find(m=>m.id===menuId);
    const roles=menu.roles.map(role=>({roleId:role.id,keys:selectedKeys().filter(key=>placements[key]?.roleId===role.id).sort((a,b)=>placements[a].position-placements[b].position)}));
    return {menuId,stock:selected,roles,useRewards,overrideKeepOne:!keepOne,tendency,pinnedRegular:pinnedRegular()};
  }
  function resetAssignments(){const assigned=assignMenuRoles(menuId,Object.fromEntries(Object.entries(stock).filter(([,n])=>n>0)));placements={};for(const role of assigned)role.keys.forEach((key,position)=>placements[key]={roleId:role.roleId,position});}
  function changeQuantity(key,value){
    const old=stock[key]??0,cap=businessModel(getState(),getNow()).capacity,current=selectedKeys().reduce((sum,k)=>sum+stock[k],0),r=rows().find(r=>r.key===key);
    if(!r)return;const allowed=Math.max(0,Math.min(usable(r),cap-current+old));
    const quantity=Math.max(0,Math.min(allowed,Math.floor(Number(value)||0)));
    if(quantity>0&&old===0&&selectedKeys().length>=6){notify('这一单最多摆6种，先取下一种再添新出品。');return;}
    stock[key]=quantity;manualStock=true;autofillNote='';if(!old||!quantity)resetAssignments();render(true);
  }
  function preview(){try{return {ready:true,plan:prepareBusiness(getState(),options())};}catch(error){return {ready:false,message:error.message};}}
  // A settled receipt the player has not opened yet is the first thing shown.
  const unreadReport=()=>{const r=getState().expansion.business.lastReport;return r&&uiPreference('seenBusinessReport')!==r.id?r:null;};
  function dismissSheet(){find('.bs-dialog')?.close();sheet=null;if(returnSheetButton)find(returnSheetButton)?.focus({preventScroll:true});}
  function notify(message){dismissSheet();alertBox(message);}
  function open(nextMode){dismissSheet();const b=getState().expansion.business;mode=typeof nextMode==='string'?nextMode:b.active?'active':unreadReport()?'report':'prepare';render();}
  function render(preserve=false){
    const focused=preserve&&panels.contains(document.activeElement)?document.activeElement:null;
    const focusAttribute=focused?[...focused.attributes].find(a=>a.name.startsWith('data-business-')):null;
    if(preserve)scrollTop=find('.business-scroll')?.scrollTop??scrollTop;else scrollTop=0;
    mainScrollTop=preserve?(find('.bs-main-scroll')?.scrollTop??mainScrollTop):0;
    find('.bs-dialog')?.close();
    const s=getState(),now=getNow(),model=businessModel(s,now);keepOne=policyKeepOne();
    menuId=todayMenuId(s,menuId??uiPreference('businessMenu'));
    // The stock follows the auto plan (menu partners first, then the other spare partners) until the player changes it.
    if(mode==='prepare'&&!model.active&&model.access.met&&!manualStock){
      const r=suggestBusinessStock(s,menuId,{keepOne}),next=r?{...r.stock}:{};
      if(JSON.stringify(next)!==JSON.stringify(Object.fromEntries(Object.entries(stock).filter(([,n])=>n>0)))){stock=next;resetAssignments();}
    }
    if(mode==='active'&&!model.active)mode=model.report?'report':'prepare';
    if(mode==='prepare'&&model.active)mode='active';
    if(mode==='report'&&model.report)setUiPreference('seenBusinessReport',model.report.id);
    if(mode==='report'||sheet==='ledger'){
      const settled=mode==='report'||!model.active;
      const helpOpen=find('.bs-sub-dialog')?.open;find('.bs-sub-dialog')?.close();
      showPanel('小店账单',`${shopSubpageHeader('ledger')}<div class="business-scroll scroll" tabindex="0">${shopSubpagePaper('ledger',businessLedgerMarkup(s,model,settled))}</div><footer class="business-footer rc-foot" data-row>${!settled?kitButton2('收摊','data-business-close'):s.expansion.orders.proposals.length?kitButton2(`询问 ${s.expansion.orders.proposals.length}`,'data-trade-view="orders"'):''}${kitButton(settled&&!model.active?'下一单':'回营业','data-business-ledger-back')}</footer>${shopSubpageDialog('ledger')}`,'screen-panel business-screen shop-subpage shop-ledger');
      const back=()=>{sheet=null;mode=model.active?'active':'prepare';render();};
      bindShopSubpage(panels,back);if(helpOpen)find('.bs-sub-dialog').showModal();
      find('[data-shop-back]').setAttribute('data-business-sheet-close','');
      find('[data-shop-back]').setAttribute('data-trade-view','business');
      find('[data-business-ledger-back]').setAttribute('data-business-again','');
      find('[data-business-ledger-back]').onclick=back;
      find('[data-trade-view="business"]').onclick=back;
      updateTime();
    }else{
      const stockRows=rows().filter(r=>stock[r.key]>0).map(r=>({...r,quantity:stock[r.key]}));
      const p=preview(),fit=p.ready?p.plan.fit.tier:'ordinary',forecast=p.ready?businessForecast(s,menuId,p.plan.stock,p.plan.roles,p.plan.fit):null;
      const singles=rows().some(r=>!r.locked&&r.free>0&&usable(r)===0&&!stock[r.key]);
      const live=model.active?s.expansion.business.active:null,board=orderBoard(s,now);
      const core=menuCore(s,live?live.menuId:menuId,live?live.initialStock:Object.fromEntries(Object.entries(stock).filter(([,n])=>n>0)),now);
      const main=businessHomeMarkup({state:s,core,model,live,board,regulars:regularsSummary(s),projects:projectsSummary(s),stars:starsMarkup(fit),characterPortrait,canOpen:model.access.met&&p.ready,access:model.access});
      if(sheet==='order'&&!board.all.some(c=>refKey(c.ref)===orderRef))sheet=null;
      const sheetTitles={stock:'全部出品',menu:model.active?'本单菜单':'换菜单',help:'这页怎么玩',ledger:model.active?'本单账单':'上一单账单',basket:'这一篮',why:'留着没摆',orders:'全部订单',order:'订单'};
      const sheetBody=sheet==='stock'?prepareMarkup(model):sheet==='menu'?(model.active?menuMarkup(model):menuGridMarkup(model)):sheet==='help'?helpMarkup(model):sheet==='basket'?basketMarkup(model):sheet==='why'?whyMarkup():sheet==='orders'?`<div class="bh-orders is-all">${board.all.map(c=>orderCardMarkup(c,characterPortrait)).join('')}</div>`:sheet==='order'?orderSheet(s,board,now):'';
      showPanel('小店营业',`${main}<dialog class="bs-dialog" aria-labelledby="bs-dialog-title"><header><h3 id="bs-dialog-title" class="bs-plank" data-art="plank" data-overhang><span data-safe><b>${sheetTitles[sheet]??''}</b></span></h3><button data-business-sheet-close data-overhang aria-label="关闭详情">×</button></header><div class="business-scroll scroll">${sheetBody}</div></dialog>`,`screen-panel business-screen golden-business business-home${model.active?' is-active':''}`);
      find('.bs-main-scroll').scrollTop=mainScrollTop;updateTime();
      panels.querySelectorAll('[data-business-sheet]').forEach(button=>button.onclick=()=>{basketFrom=button.dataset.businessFrom??null;if(!button.closest('.bs-dialog'))returnSheetButton=button.dataset.businessStockKey?`[data-business-stock-key="${button.dataset.businessStockKey}"]`:`[data-business-sheet="${button.dataset.businessSheet}"]`;sheet=button.dataset.businessSheet;if(sheet==='basket')basketKey=button.dataset.businessStockKey??null;scrollTop=0;render(true);});
      find('[data-business-sheet-close]').onclick=dismissSheet;
      find('.bs-dialog').addEventListener('cancel',()=>{sheet=null;});
      if(sheet)find('.bs-dialog').showModal();
      if(!sheet&&mode==='prepare'&&selectedKeys().length)firstVisitGuide('business',[{selector:'#panels .golden-business .bh-counter',text:'菜单要的伙伴摆在篮子里，缺的点一下去做'},{selector:'#panels .golden-business .bh-orders',text:'订单够了就能一下交付'}]);
    }
    if(find('.business-scroll'))find('.business-scroll').scrollTop=scrollTop;
    find('[data-business-first-order]')?.addEventListener('click',()=>{dismissSheet();openOrders?.();});
    panels.querySelectorAll('[data-business-tab]').forEach(button=>button.onclick=()=>{mode=button.dataset.businessTab;render();});
    // 全部订单 is a sheet of this page now (the old 订单 page and its 接取 / 预留 steps have no entry).
    panels.querySelectorAll('[data-trade-view="orders"]').forEach(button=>button.onclick=()=>{find('.bs-dialog')?.close();sheet='orders';mode=getState().expansion.business.active?'active':'prepare';render();});
    panels.querySelectorAll('[data-trade-view="regulars"]').forEach(button=>button.onclick=()=>{dismissSheet();openRegulars?.(button.dataset.tradeFocus||null);});
    panels.querySelectorAll('[data-trade-view="projects"]').forEach(button=>button.onclick=()=>{dismissSheet();openProjects?.(button.dataset.tradeFocus||null);});
    bind(model);
    lastRenderedState=s;
    if(focusAttribute){const replacement=[...panels.querySelectorAll(`[${focusAttribute.name}]`)].find(el=>el.getAttribute(focusAttribute.name)===focusAttribute.value);replacement?.focus({preventScroll:true});}
  }

  // The order sheet: the request in full, who could fill each need (spare partners first), reward, next step, 略过.
  function orderSheet(s,board,now){
    const card=board.all.find(c=>refKey(c.ref)===orderRef);if(!card)return '';
    const met=k=>{const c=resolveSpecies(k);return !!c&&speciesDiscovered(s,c.egg,c.id);};
    let request='',candidates=[];
    if(card.ref.kind==='story'){request=card.name;candidates=card.needs.map(n=>[n.key]);}
    else{
      const st=orderStatus(s,card.ref,now),active=card.ref.kind==='order'?s.expansion.orders.active.find(a=>a.id===card.ref.id):null;
      request=(active?CONTENT_TEXT[active.variantId]?.text:CONTENT_TEXT[st.option?.variantId]?.text)??CONTENT_TEXT[card.templateId]?.request??'';
      candidates=st.kind==='display'?[st.allowed.filter(k=>met(k)&&freeCount(s,k)>0)]:st.groups.map(g=>g.allowed.filter(met).sort((a,b)=>spareFor(s,b)-spareFor(s,a)));
    }
    return orderSheetMarkup({card,request,candidates,characterPortrait});
  }
  // One 交付 / 摆出来: the whole order in one step, then what it brought (money, then its 地区情报).
  function deliver(ref){
    if(busy)return;busy=true;let result=null;
    try{result=commitProgress(draft=>ref.kind==='story'?completeStoryNow(draft,ref.id,Math.floor(getNow())):completeOrderNow(draft,ref,Math.floor(getNow())));}finally{busy=false;}
    if(result===null||result===false||result===undefined)return;
    sheet=null;orderRef=null;render();
    const done=ref.kind==='story'?{name:result.name,income:result.income,bonusCP:result.extra,kind:'story',intel:null}:result,intel=done.intel;
    const go=intel?.kind==='clue'?'看线索':intel?.kind==='place'?`去${intel.hint.regionName}`:null;
    kitDialog({title:'订单完成',className:'order-done',body:orderDoneMarkup(done,characterPortrait),yes:go??'收下',no:go?'收下':'',yesAttrs:'data-order-done-go',
      onYes:()=>{if(intel?.kind==='clue')openClueBook?.(intel.advance?.key??intel.clue.key);else if(intel?.kind==='place')openJourney?.(intel.hint.region);}});
  }
  function bindHome(model){
    panels.querySelectorAll('[data-business-slot]').forEach(b=>b.onclick=()=>{
      const s=getState(),key=b.dataset.businessSlot,[egg,id]=key.split(':').map(Number);
      if(model.active){sheet='ledger';render(true);return;}
      if(!speciesDiscovered(s,egg,id)){openClueBook?.(key);return;}
      if(b.classList.contains('is-ok')){basketKey=key;basketFrom=null;returnSheetButton=`[data-business-slot="${key}"]`;sheet='basket';render(true);return;}
      openGoal?.({kind:'menu',id:menuId,key,name:title()});
    });
    find('[data-business-gap]')?.addEventListener('click',()=>openGoal?.({kind:'menu',id:menuId,name:title()}));
    panels.querySelectorAll('[data-order-detail]').forEach(b=>b.onclick=()=>{orderRef=b.dataset.orderDetail;returnSheetButton=`.bh-main [data-order-detail="${orderRef}"]`;sheet='order';render(true);});
    panels.querySelectorAll('[data-order-make]').forEach(b=>b.onclick=()=>{const ref=parseRef(b.dataset.orderMake),card=orderBoard(getState(),getNow()).all.find(c=>refKey(c.ref)===b.dataset.orderMake);dismissSheet();openGoal?.({...ref,name:card?.name??''});});
    panels.querySelectorAll('[data-order-act]').forEach(b=>b.onclick=()=>deliver(parseRef(b.dataset.orderAct)));
    panels.querySelectorAll('[data-order-skip]').forEach(b=>b.onclick=()=>{const {id}=parseRef(b.dataset.orderSkip);const r=commitProgress(draft=>skipProposal(draft,id));if(r!==null&&r!==false){sheet=null;render();}});
    panels.querySelectorAll('[data-order-cancel]').forEach(b=>b.onclick=()=>{const {id}=parseRef(b.dataset.orderCancel);confirmBox('这单不做了？\n已经交出的伙伴不退回，留着的伙伴都放回家。',()=>{const r=commitProgress(draft=>cancelOrderInstance(draft,id));if(r!==null&&r!==false){sheet=null;render();}},false,{yes:'不做了',no:'再想想'});});
    find('[data-order-story]')?.addEventListener('click',()=>{dismissSheet();openStory?.();});
  }
  // Menu card / menu chooser slots: role birds already stocked, then what a complete menu lacks.
  function menuSlots(s,id,interactive){
    const o=menuOverview(s,{keepOne}).find(x=>x.id===id),role=new Set(o?.roleKeys??[]),out=[];
    const current=id===menuId?Object.entries(stock).filter(([,n])=>n>0):Object.entries(o?.plan?.stock??{});
    for(const [key,n] of current)if(role.has(key)){const [egg,sid]=key.split(':').map(Number);out.push(slotMarkup({kind:'ok',count:n,portrait:characterPortrait(egg,sid),label:`${resolveSpecies(key)?.title_zh_CN??''} ×${n}`}));}
    for(const need of o?.needs??[]){
      const [egg,sid]=need.key.split(':').map(Number),view=speciesView(s,egg,sid),known=!!view?.known;
      if(!known){out.push(slotMarkup({kind:'unknown',count:need.n,attrs:interactive?`data-business-clue="${need.key}"`:'',label:interactive?'还不认识的伙伴，看线索':''}));continue;}
      const recipe=interactive?recipeForSpecies(s,need.key,getNow()):null;
      out.push(slotMarkup({kind:'need',count:need.n,portrait:characterPortrait(egg,sid),make:!!recipe,attrs:interactive?(recipe?`data-business-make="${need.key}"`:`data-business-clue="${need.key}"`):'',label:interactive?`还差${view.name} ${need.n}只，${recipe?'去厨房做':'看线索'}`:''}));
    }
    return out.slice(0,3).join('');
  }
  function menuGridMarkup(model){
    const s=getState(),overview=menuOverview(s,{keepOne}),rank={ordinary:0,suitable:1,complete:2};
    const best=overview.filter(o=>o.plan).sort((a,b)=>rank[b.plan.tier]-rank[a.plan.tier]||b.plan.income-a.plan.income)[0]?.id;
    const lock='<img class="bs-menu-lock-art" src="/web/art/golden-journey/lock.png" alt="">';
    return `<div class="bs-menu-grid" role="group" aria-label="选择菜单">${model.menus.map(m=>{const o=overview.find(x=>x.id===m.id);
      if(!m.unlock.met||!o?.plan)return `<button data-business-pick-menu="${m.id}" aria-disabled="true" aria-label="${esc(m.name)}，还没开放"><strong>${esc(m.name)}</strong><span class="bs-menu-lock">${lock}</span></button>`;
      return `<button data-business-pick-menu="${m.id}" aria-pressed="${m.id===menuId}"><strong>${m.id===best?'<span class="bs-menu-tag">荐</span>':''}${esc(m.name)}</strong>${starsMarkup(o.plan.tier)}<span class="bs-menu-mini">${menuSlots(s,m.id,false)}</span><span class="bs-menu-income"><img src="/web/art/golden-business/coin.png" alt="">+${number(o.plan.income)}</span></button>`;}).join('')}</div>`;
  }
  function basketRows(){const menu=REGIONAL.menus.find(m=>m.id===menuId);return rows().filter(r=>!r.locked).sort((a,b)=>Number(b.fits)-Number(a.fits)||usable(b)-usable(a));}
  // One basket: who it is, how many (big number, wooden slider, quick picks; plus and minus for fine tuning), and who else could go in it.
  function basketMarkup(model){
    const all=basketRows();let key=basketKey&&(stock[basketKey]>0||usable(all.find(r=>r.key===basketKey)??{free:0,home:0})>0)?basketKey:null;
    if(!key){const pick=all.find(r=>usable(r)>0&&!stock[r.key]);if(!pick)return '<div class="bs-why-sheet"><p>没有可以再摆的伙伴了</p></div>';key=pick.key;basketKey=key;}
    const row=all.find(r=>r.key===key),n=stock[key]??0,cap=model.capacity,room=cap-selectedKeys().reduce((a,k)=>a+stock[k],0)+n,max=Math.min(usable(row),room);
    const picks=[...new Map([[Math.min(6,max),'少量'],[Math.max(1,Math.round(max/2)),'一半'],[max,'全部']].filter(([v])=>v>0).map(([v,l])=>[v,l])).entries()];
    const swap=all.filter(r=>r.key===key||usable(r)>0).slice(0,12).map(r=>{const taken=r.key!==key&&stock[r.key]>0,u=usable(r);return slotMarkup({kind:'ok',count:u,portrait:characterPortrait(r.egg,r.id),attrs:`data-business-basket-swap="${r.key}" aria-pressed="${r.key===key}" ${u&&!taken?'':'disabled'}`,label:`换成${r.name}，可摆${u}只`});}).join('');
    const percent=max?n/max*100:0;
    return `<div class="gd gd-basket bs-basket-sheet"><div class="gd-head" data-row><span class="gd-face">${characterPortrait(row.egg,row.id)}</span><div><h3>${esc(row.name)}</h3><div class="gd-row">${kitChip('',`在家 ${row.home}`,'soft')}${kitChip('',`锁 ${lockedCount(getState(),row.key)}`,'soft')}</div></div></div>
      <div class="gd-qty" data-row><button type="button" class="gd-round minus" data-business-basket-step="-1" aria-label="少摆" ${n?'':'disabled'}></button><output class="gd-big" aria-live="polite" data-basket-count>${n}<small>只</small></output><button type="button" class="gd-round plus" data-business-basket-step="1" aria-label="多摆" ${n<max?'':'disabled'}></button></div>
      <label class="gd-slider"><span class="sr-only">拖动选择数量</span><input type="range" min="0" max="${max}" step="1" value="${n}" data-business-basket-range style="--fill:${percent}%"></label>
      <div class="gd-coins" data-row>${picks.map(([v,l])=>`<button type="button" class="gd-coinwrap${v===n?' on':''}" data-business-basket-set="${v}" aria-pressed="${v===n}"><span class="gd-coin">${v}</span><b>${l}</b></button>`).join('')}</div>
      ${manualPlacement&&n?placementMarkup(key):''}${basketFrom==='stock'?'':`${kitLabel('也可以换成')}<div class="bs-swap-grid gd-scroll-row" role="group" aria-label="换成别的伙伴">${swap}</div>`}
      <div class="gd-actions" data-row>${basketFrom==='stock'?'':kitButton2('全部出品','data-business-sheet="stock"')}${kitButton('放好了','data-business-basket-done')}</div></div>`;
  }
  // Manual placement (更多设置): the menu roles as pills.
  function placementMarkup(key){
    const menu=REGIONAL.menus.find(m=>m.id===menuId),placement=placements[key],selected=placement&&placement.roleId!=='ordinary'?`${placement.roleId}:${placement.position}`:'ordinary';
    const choices=[...menu.roles.filter(role=>role.allowed.includes(key)).flatMap(role=>Array.from({length:role.maxSpecies},(_,index)=>({value:`${role.id}:${index}`,label:`${role.required?'菜单':'搭配'}${menu.roles.length>1?(menu.roles.indexOf(role)+1):''}${index?'替补':''}`}))),{value:'ordinary',label:'普通'}];
    return `<div class="gd-row gd-wrap bs-roles" role="group" aria-label="摆放位置">${choices.map(c=>`<button type="button" class="gd-btn2 mini" data-business-placement="${key}" data-value="${c.value}" aria-pressed="${selected===c.value}">${c.label}</button>`).join('')}</div>`;
  }

  // Why some partners stayed home: the keep-one nest, one line, then 好 / 全部都摆 as game buttons.
  function whyMarkup(){return `<div class="bs-why-sheet">${kitArt('set-keep','bs-why-art')}<p>其余伙伴按锁定的数量留在家里，没摆。要摆就到仓库把锁定改小。</p><div class="gd-actions" data-row>${kitButton('好','data-business-sheet-close-why')}</div></div>`;}
  function menuMarkup(model){
    const id=model.active?.menuId??menuId;
    return `${!model.active?`<div class="business-menu-list" role="group" aria-label="选择今天的菜单">${model.menus.map(m=>`<button data-business-menu="${m.id}" aria-pressed="${m.id===menuId}" ${m.unlock.met?'':'aria-disabled="true"'}>${esc(m.name)}${m.unlock.met?'':'<small>未开放</small>'}</button>`).join('')}</div>`:''}<header class="business-menu-sheet"><h3>${esc(CONTENT_TEXT[id].name)}</h3><p>${esc(CONTENT_TEXT[id].text)}</p><p>${esc(CONTENT_TEXT[id].complete)}</p></header>${model.active?'<p>这单按开张时的菜单招待，收摊后可以换菜单。</p>':'<button data-business-sheet="stock">去摆货</button>'}<button data-business-sheet="help">查看营业规则</button>`;
  }
  function helpMarkup(model){return `${helpCardsMarkup('business')}<details class="business-rules"><summary>详细规则</summary><p>点货篮选择出品和数量，点菜单牌换菜单。确认开张前不会占用库存。</p><ul><li>每单摆1至6种食用出品，当前最多${model.capacity}只；默认每种在家留1只，收藏锁定的伙伴不参与备货。</li><li>每满2小时招待一轮，最多卖6只；最长营业24小时，售罄自动收摊。</li><li>主选卖完才卖替补，与普通出品轮流成交。卖满6只且每个菜单位都卖出过，算一次有效接待。</li><li>开张时凑齐菜单，这一单里菜单要的伙伴每只多卖 25%，卖到收摊都算；其余多余的伙伴按原价一起卖。订单正等着的伙伴留在家里，不会摆出来。</li><li>订单够了点「交付」一次完成，卡上有「情报」的这次还带回本地区的消息；不够点「去做」。</li><li>货款随成交入账；收摊结算整筐、拼盘等奖励。未售出品与未用奖励次数自动解除占用。</li><li>“使用已有经营奖励”和来客倾向在货篮备货详情中设置。已开张的这单不再改货。</li><li>账单可以反复查看，不会重复发放货款。</li></ul>${!model.access.met?`<h4>开张前还需</h4><ul>${model.access.missing.map(x=>`<li>${esc(x)}</li>`).join('')}</ul><button data-business-first-order>去完成第一笔生意</button>`:''}<button data-business-kitchen>回厨房做一锅</button></details>`;}
  function prepareMarkup(model){
    if(!model.access.met)return `<div class="business-empty"><div class="business-welcome-art" aria-hidden="true">${interfaceIcon('shop')}${characterPortrait(0,0)}</div><span class="business-shop-sign" aria-hidden="true">歇一歇</span><h3>先把第一笔生意做好</h3><p>认得几样熟悉的出品，就能把小店摆起来。</p><ul>${model.access.missing.map(x=>`<li>${esc(x)}</li>`).join('')}</ul><button class="cream" data-business-first-order>去完成第一笔生意</button><p class="business-note">收成可以在仓库即时出售，也可以准备好后摆到小店。</p></div>`;
    const p=preview(),count=selectedKeys().reduce((sum,key)=>sum+stock[key],0),all=rows(),visible=all.filter(row=>filter==='all'||row.fits||stock[row.key]>0);
    const chooser=model.menus.length>1?`<div class="business-menu-list" role="group" aria-label="选择今天的菜单">${model.menus.map(m=>`<button data-business-menu="${m.id}" aria-pressed="${m.id===menuId}" ${m.unlock.met?'':'aria-disabled="true"'}>${esc(m.name)}${m.unlock.met?'':'<small>未开放</small>'}</button>`).join('')}</div>`:'';
    const forecast=p.ready?businessForecast(getState(),menuId,p.plan.stock,p.plan.roles,p.plan.fit):null;
    const tiles=visible.map(row=>{const n=stock[row.key]??0,can=!row.locked&&usable(row)>0;
      return kitCell({pic:characterPortrait(row.egg,row.id),name:row.name,count:n?`摆 ${n}`:`×${usable(row)}`,on:n>0,attrs:`data-business-sheet="basket" data-business-stock-key="${row.key}" data-business-from="stock"${can||n?'':' disabled'}`,label:`${row.name}，${n?`摆了 ${n} 只，`:''}可用 ${row.free} 只`});}).join('');
    const toggle=(attr,on,label)=>`<button type="button" class="gd-toggle" role="checkbox" aria-checked="${on}" aria-pressed="${on}" ${attr}><i>${on?'✓':''}</i>${label}</button>`;
    const more=`<details class="gd-more business-more"><summary>更多设置</summary><div class="gd-row gd-wrap">${toggle('data-business-rewards',useRewards,`用经营奖励${useRewards&&p.ready?` · 留 ${p.plan.creditReserve}`:''}`)}${toggle('data-business-manual',manualPlacement,'手动摆位')}</div><div class="gd-row bs-tendency" role="group" aria-label="来客倾向">${[['regulars','熟客故事'],['discovery','外地常客']].map(([v,l])=>`<button type="button" class="gd-btn2 mini" data-business-tendency="${v}" aria-pressed="${tendency===v}">${l}</button>`).join('')}</div></details>`;
    return `${presetsMarkup()}<div class="bs-prep-head"><b class="bs-prep-name">${esc(title())}</b>${p.ready?starsMarkup(p.plan.fit.tier):''}</div>
      <div class="gd-row gd-wrap bs-prep-chips business-prep-summary" aria-live="polite">${kitChipHtml(`<b>${count}</b>/${model.capacity} 只`,'mini')}${forecast?`${kitChipHtml(`${kitIcon.clock}约 ${forecast.hours} 小时`,'mini')}${kitChipHtml(`${kitIcon.coin}+${number(forecast.income)}`,'mini')}`:''}</div>
      <div class="gd-actions business-autofill" data-row>${kitButton2('清空','data-business-clear')}${kitButton('帮我摆','data-business-autofill')}</div>${autofillNote?`<span class="gd-note business-autofill-note" role="status">${esc(autofillNote)}</span>`:''}${!p.ready&&selectedKeys().length?`<span class="gd-alert business-prep-hint" role="status">${esc(p.message)}</span>`:''}
      <div class="gd-row bs-filter business-filter" role="group" aria-label="显示哪些伙伴">${[['theme','适合菜单'],['all','全部伙伴']].map(([v,l])=>`<button type="button" class="gd-btn2 mini" data-business-filter="${v}" aria-pressed="${filter===v}">${l}</button>`).join('')}</div>
      <div class="kp-grid bs-stock-grid business-stock-list">${tiles||'<p class="kp-empty">这一页还没有可用伙伴</p>'}</div>${more}`;
  }
  // PJ-1 result: three saved setups. Loading only fills this draft (no reservation).
  function presetsMarkup(){
    const s=getState();if(!projectComplete(s,'PJ-1'))return '';
    const presets=s.expansion.menus.presets,count=selectedKeys().length;
    const slots=Array.from({length:MAX_PRESETS},(_,i)=>{const p=presets[i];if(!p)return i===presets.length?`<button data-business-preset-save="${i}" ${count?'':'disabled'}>存为预设${i+1}</button>`:'';
      const n=Object.values(p.stock).reduce((a,b)=>a+b,0);return `<span class="business-preset"><button data-business-preset-load="${i}">${esc(CONTENT_TEXT[p.menuId]?.name??p.menuId)} · ${n}只</button><button data-business-preset-save="${i}" aria-label="用当前备货覆盖预设${i+1}" ${count?'':'disabled'}>覆盖</button><button data-business-preset-delete="${i}" aria-label="删除预设${i+1}">删除</button></span>`;}).join('');
    return `<div class="business-presets" role="group" aria-label="菜单预设"><span class="business-eyebrow">菜单预设</span>${slots}</div>`;
  }
  function bind(model){
    bindHome(model);
    panels.querySelectorAll('[data-business-pick-menu]').forEach(button=>button.onclick=()=>{const m=model.menus.find(x=>x.id===button.dataset.businessPickMenu);if(!m.unlock.met){notify(`${m.name}：${m.unlock.missing.join('；')}`);return;}pickMenu(m.id);sheet=null;render();});
    panels.querySelectorAll('[data-business-basket-step]').forEach(button=>button.onclick=()=>{const row=basketRows().find(r=>r.key===basketKey);if(!row)return;const n=stock[basketKey]??0,dir=Number(button.dataset.businessBasketStep),step=dir>0?(usable(row)-n>=6?6:1):(n>6?6:1);changeQuantity(basketKey,n+dir*step);});
    panels.querySelectorAll('[data-business-basket-set]').forEach(button=>button.onclick=()=>{if(basketKey)changeQuantity(basketKey,Number(button.dataset.businessBasketSet));});
    {const range=find('[data-business-basket-range]');if(range){const out=find('[data-basket-count]');range.oninput=()=>{range.style.setProperty('--fill',(range.max>0?range.value/range.max*100:0)+'%');if(out)out.firstChild.textContent=range.value;};range.onchange=()=>{if(basketKey)changeQuantity(basketKey,Number(range.value));};}}
    panels.querySelectorAll('[data-business-basket-swap]').forEach(button=>button.onclick=()=>{const to=button.dataset.businessBasketSwap;if(to===basketKey)return;const old=basketKey,n=stock[old]??0,row=basketRows().find(r=>r.key===to);if(!row)return;if(old)delete stock[old];stock[to]=Math.max(1,Math.min(n||6,usable(row)));manualStock=true;basketKey=to;resetAssignments();render(true);});
    find('[data-business-basket-done]')?.addEventListener('click',()=>{for(const k of Object.keys(stock))if(!stock[k])delete stock[k];if(basketFrom==='stock'){basketFrom=null;sheet='stock';render(true);return;}dismissSheet();render(true);});
    find('[data-business-sheet-close-why]')?.addEventListener('click',dismissSheet);
    panels.querySelectorAll('[data-business-make]').forEach(button=>button.onclick=()=>{dismissSheet();cookFor?.(button.dataset.businessMake,{menu:title()});});
    panels.querySelectorAll('[data-business-clue]').forEach(button=>button.onclick=()=>{dismissSheet();openClue?.(button.dataset.businessClue);});
    find('[data-open-settings]')?.addEventListener('click',()=>{dismissSheet();openSettings?.();});

    panels.querySelectorAll('[data-business-regulars]').forEach(button=>button.onclick=()=>{dismissSheet();openRegulars?.(button.dataset.businessRegulars);});
    panels.querySelectorAll('[data-business-menu]').forEach(button=>button.onclick=()=>{const m=model.menus.find(x=>x.id===button.dataset.businessMenu);if(!m.unlock.met){notify(`${m.name}尚未开放：${m.unlock.missing.join('；')}`);return;}if(menuId!==m.id){pickMenu(m.id);render(true);}});
    find('[data-business-kitchen]')?.addEventListener('click',()=>{dismissSheet();goKitchen?.();});
    find('[data-business-again]:not([data-business-ledger-back])')?.addEventListener('click',()=>{mode=model.active?'active':'prepare';render();});
    find('[data-business-manual]')?.addEventListener('click',()=>{manualPlacement=!manualPlacement;render(true);});
    find('[data-business-clear]')?.addEventListener('click',()=>{stock={};placements={};manualStock=true;autofillNote='';autoFilledFor=`${menuId}:${keepOne}`;render(true);});
    find('[data-business-autofill]')?.addEventListener('click',()=>{
      const s=getState(),r=suggestBusinessStock(s,menuId,{keepOne}),singles=rows().filter(x=>!x.locked&&x.free>0&&usable(x)===0).length;
      if(!r){autofillNote=keepOne&&singles?`现在没有能摆出来的伙伴：${singles}种都按锁定数量留在家里了。到仓库把锁定改小就能摆上它们。`:'现在没有能摆出来的伙伴，可能都被订单预留或在寻访中。';render(true);return;}
      stock=r.stock;manualStock=false;resetAssignments();
      autofillNote=`已按「${title()}」摆好：${tierLabel[r.tier]}。${keepOne&&singles?`另有${singles}种按锁定数量留在家里，没有摆上。`:''}`;
      render(true);
    });
    find('[data-business-rewards]')?.addEventListener('click',()=>{useRewards=!useRewards;render(true);});
    panels.querySelectorAll('[data-business-tendency]').forEach(button=>button.onclick=()=>{tendency=button.dataset.businessTendency;render(true);});
    // Keep the "更多设置" fold open across re-renders once the player opened it.
    find('.business-more')?.addEventListener('toggle',event=>{moreOpen=event.target.open;});
    if(moreOpen)find('.business-more')?.setAttribute('open','');
    panels.querySelectorAll('[data-business-preset-load]').forEach(button=>button.onclick=()=>{
      const p=getState().expansion.menus.presets[Number(button.dataset.businessPresetLoad)];if(!p)return;
      const m=model.menus.find(x=>x.id===p.menuId);if(!m?.unlock.met){notify('这张菜单现在还不能营业。');return;}
      pickMenu(p.menuId);manualStock=true;stock={};const available=rows(),skipped=[];
      for(const [key,n]of Object.entries(p.stock)){const r=available.find(x=>x.key===key);const max=r?usable(r):0;if(max>0)stock[key]=Math.min(n,max);if(!r||max<n)skipped.push(key);}
      resetAssignments();render(true);if(skipped.length)notify(`有${skipped.length}种出品库存不足，已按现有可用数量载入。`);
    });
    panels.querySelectorAll('[data-business-preset-save]').forEach(button=>button.onclick=()=>{
      const slot=Number(button.dataset.businessPresetSave),selected=Object.fromEntries(Object.entries(stock).filter(([,n])=>n>0));
      commitProgress(draft=>saveMenuPreset(draft,slot,{menuId,stock:selected}));render(true);
    });
    panels.querySelectorAll('[data-business-preset-delete]').forEach(button=>button.onclick=()=>{commitProgress(draft=>deleteMenuPreset(draft,Number(button.dataset.businessPresetDelete)));render(true);});
    panels.querySelectorAll('[data-business-regulars]').forEach(button=>button.onclick=()=>openRegulars?.(button.dataset.businessRegulars||null));
    panels.querySelectorAll('[data-business-filter]').forEach(button=>button.onclick=()=>{filter=button.dataset.businessFilter;render(true);});
    panels.querySelectorAll('[data-business-placement]').forEach(button=>button.onclick=()=>{
      const key=button.dataset.businessPlacement,old=placements[key],parts=button.dataset.value.split(':'),next={roleId:parts[0],position:Number(parts[1]??0)};
      const occupied=selectedKeys().find(k=>k!==key&&placements[k]?.roleId===next.roleId&&placements[k]?.position===next.position);
      if(occupied&&next.roleId!=='ordinary')placements[occupied]=old??{roleId:'ordinary',position:0};placements[key]=next;render(true);
    });
    find('[data-business-open]')?.addEventListener('click',()=>{
      if(busy)return;const p=preview();if(!p.ready){notify(p.message);return;}const request=structuredClone(options()),amount=Object.values(request.stock).reduce((a,b)=>a+b,0);
      const hours=Math.min(12,Math.ceil(amount/6))*2;
      confirmBox(`${title()} · ${amount} 只\n约 ${hours} 小时卖完`,()=>{
        if(busy)return;busy=true;try{const result=commitProgress(draft=>openBusiness(draft,request,Math.floor(getNow())));if(result){stock={};placements={};manualStock=false;autoFilledFor=null;setUiPreference('businessMenu',menuId);mode='active';render();}}finally{busy=false;}
      },false,{yes:'开张',no:'再看看'});
    });
    find('[data-business-close]')?.addEventListener('click',()=>confirmBox('收摊会先结清已完成的接待窗口。\n未满2小时的部分不成交，未售出品与未用奖励次数解除占用。',()=>{
      if(busy)return;busy=true;try{const result=commitProgress(draft=>closeBusinessTimeline(draft,Math.floor(getNow())));if(result){mode='report';render();}}finally{busy=false;}
    },false,{yes:'结算并收摊',no:'继续营业'}));
  }
  function refresh(){
    const screen=find('.business-screen');if(!screen)return;
    const focused=document.activeElement;
    // The clock must not replace an uncommitted quantity or an open select.
    // A real stock change is validated on blur/confirmation against current state.
    const editing=mode==='prepare'&&screen.contains(focused)&&focused.matches('input,select,textarea');
    if(lastRenderedState!==getState()&&!editing)render(true);else updateTime();
  }
  function updateTime(){if(!find('.business-screen'))return;const active=businessModel(getState(),getNow()).active;if(mode==='active'&&!active){render(true);return;}const field=find('[data-business-countdown]');if(field&&active)field.textContent=`下一窗还需 ${remaining(active.nextAt-getNow())}`;const next=find('[data-business-next]');if(next&&active)next.textContent=`下一轮 ${remaining(active.nextAt-getNow())}`;}
  // todayMenu: what 下一锅 counts as today's menu. openOrders: this page with the 全部订单 sheet open.
  return {open,refresh,updateTime,unreadReport,todayMenu:()=>todayMenuId(getState(),menuId??uiPreference('businessMenu')),
    openOrders(){dismissSheet();const b=getState().expansion.business;mode=b.active?'active':'prepare';sheet='orders';render();},
    prepareDraft(next){stock={...next};manualStock=true;resetAssignments();mode='prepare';},reset(){stock={};placements={};manualStock=false;mode='prepare';scrollTop=0;}};
}
