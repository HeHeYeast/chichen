import {interfaceIcon} from './ui-icons.js';
import {REGIONAL,resolveSpecies,CONTENT_TEXT} from './content-registry.js';
import {speciesView} from './collection-ui.js';
import {freeCount,homeCount} from './inventory.js';
import {openBusiness,prepareBusiness} from './business.js';
import {closeBusinessTimeline} from './timeline.js';
import {businessModel} from './business-model.js';
import {uiPreference,setUiPreference} from './ui-preferences.js';
import {tradeNav} from './order-ui.js';
import {assignMenuRoles} from './menu-model.js';
import {reportVisitors} from './regular-model.js';
import {pinnedRegular} from './regular-ui.js';
import {saveMenuPreset,deleteMenuPreset,projectComplete,MAX_PRESETS} from './projects.js';

const esc=value=>String(value??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const number=value=>Number(value??0).toLocaleString('zh-CN');
const time=value=>new Date(value).toLocaleTimeString('zh-CN',{hour:'2-digit',minute:'2-digit'});
const remaining=ms=>{const mins=Math.max(0,Math.ceil(ms/60000));return mins>=60?`${Math.floor(mins/60)}小时${mins%60}分`:`${mins}分钟`;};
const tierLabel={ordinary:'普通营业',suitable:'搭配合适',complete:'完整菜单'};

export function createBusinessUI({getState,getNow,commitProgress,showPanel,panels,confirmBox,alertBox,characterPortrait,goKitchen,openOrders,openRegulars,openProjects}){
  let mode='prepare',menuId='MN1',stock={},placements={},keepOne=true,useRewards=false,tendency='regulars',filter='theme',busy=false,scrollTop=0,lastRenderedState=null;
  const find=selector=>panels.querySelector(selector);
  const title=()=>CONTENT_TEXT[menuId]?.name??'营业';
  const selectedKeys=()=>Object.keys(stock).filter(key=>stock[key]>0);
  function rows(){const s=getState(),menu=REGIONAL.menus.find(m=>m.id===menuId);return Object.keys(s.farm).filter(key=>s.farm[key]>0&&resolveSpecies(key)?.edible).map(key=>{const [egg,id]=key.split(':').map(Number),row=speciesView(s,egg,id);return {...row,free:freeCount(s,key),home:homeCount(s,key),fits:menu.roles.some(r=>r.allowed.includes(key)),locked:Array.isArray(s.expansion.inventoryPolicy?.collectionLocks)?s.expansion.inventoryPolicy.collectionLocks.includes(key):!!s.expansion.inventoryPolicy?.collectionLocks?.[key]};}).filter(row=>row.known).sort((a,b)=>Number(b.fits)-Number(a.fits)||a.egg-b.egg||a.id-b.id);}
  function options(){
    const selected=Object.fromEntries(Object.entries(stock).filter(([,n])=>n>0)),menu=REGIONAL.menus.find(m=>m.id===menuId);
    const roles=menu.roles.map(role=>({roleId:role.id,keys:selectedKeys().filter(key=>placements[key]?.roleId===role.id).sort((a,b)=>placements[a].position-placements[b].position)}));
    return {menuId,stock:selected,roles,useRewards,overrideKeepOne:!keepOne,tendency,pinnedRegular:pinnedRegular()};
  }
  function resetAssignments(){const assigned=assignMenuRoles(menuId,Object.fromEntries(Object.entries(stock).filter(([,n])=>n>0)));placements={};for(const role of assigned)role.keys.forEach((key,position)=>placements[key]={roleId:role.roleId,position});}
  function changeQuantity(key,value){
    const old=stock[key]??0,cap=businessModel(getState(),getNow()).capacity,current=selectedKeys().reduce((sum,k)=>sum+stock[k],0),r=rows().find(r=>r.key===key);
    if(!r)return;const allowed=Math.max(0,Math.min(r.free,keepOne?r.home-1:r.free,cap-current+old));
    const quantity=Math.max(0,Math.min(allowed,Math.floor(Number(value)||0)));
    if(quantity>0&&old===0&&selectedKeys().length>=6){alertBox('这一单最多摆6种，先取下一种再添新出品。');return;}
    stock[key]=quantity;if(!old||!quantity)resetAssignments();render(true);
  }
  function preview(){try{return {ready:true,plan:prepareBusiness(getState(),options())};}catch(error){return {ready:false,message:error.message};}}
  // A settled receipt the player has not opened yet is the first thing shown.
  const unreadReport=()=>{const r=getState().expansion.business.lastReport;return r&&uiPreference('seenBusinessReport')!==r.id?r:null;};
  function open(nextMode){const b=getState().expansion.business;mode=typeof nextMode==='string'?nextMode:b.active?'active':unreadReport()?'report':'prepare';render();}
  function render(preserve=false){
    const focused=preserve&&panels.contains(document.activeElement)?document.activeElement:null;
    const focusAttribute=focused?[...focused.attributes].find(a=>a.name.startsWith('data-business-')):null;
    if(preserve)scrollTop=find('.business-scroll')?.scrollTop??scrollTop;else scrollTop=0;
    const s=getState(),model=businessModel(s,getNow());
    if(mode==='active'&&!model.active)mode=model.report?'report':'prepare';
    if(mode==='prepare'&&model.active)mode='active';
    const mainLabel=model.active?'营业中':'准备开张';
    const tabs=`<nav class="business-tabs" aria-label="营业页面"><button data-business-tab="${model.active?'active':'prepare'}" aria-pressed="${mode!=='report'}">${mainLabel}</button><button data-business-tab="report" aria-pressed="${mode==='report'}" ${!model.report?'disabled':''}>上一单账单</button></nav>`;
    const body=mode==='active'?activeMarkup(model):mode==='report'?reportMarkup(model):prepareMarkup(model);
    if(mode==='report'&&model.report)setUiPreference('seenBusinessReport',model.report.id);
    showPanel('小店营业簿',`${tradeNav('business')}${tabs}<div class="business-scroll scroll" tabindex="0">${body}</div>${footerMarkup(model)}`,'screen-panel business-screen');
    find('.business-scroll').scrollTop=scrollTop;
    find('[data-business-first-order]')?.addEventListener('click',()=>openOrders?.());
    panels.querySelectorAll('[data-business-tab]').forEach(button=>button.onclick=()=>{mode=button.dataset.businessTab;render();});
    panels.querySelectorAll('[data-trade-view="orders"]').forEach(button=>button.onclick=()=>openOrders?.());
    panels.querySelectorAll('[data-trade-view="regulars"]').forEach(button=>button.onclick=()=>openRegulars?.());
    panels.querySelectorAll('[data-trade-view="projects"]').forEach(button=>button.onclick=()=>openProjects?.());
    bind(model);
    lastRenderedState=s;
    if(focusAttribute){const replacement=[...panels.querySelectorAll(`[${focusAttribute.name}]`)].find(el=>el.getAttribute(focusAttribute.name)===focusAttribute.value);replacement?.focus({preventScroll:true});}
  }
  function prepareMarkup(model){
    if(!model.access.met)return `<div class="business-empty"><div class="business-welcome-art" aria-hidden="true">${interfaceIcon('shop')}${characterPortrait(0,0)}</div><span class="business-shop-sign" aria-hidden="true">歇一歇</span><h3>先把第一笔生意做好</h3><p>认得几样熟悉的出品，就能把小店摆起来。</p><ul>${model.access.missing.map(x=>`<li>${esc(x)}</li>`).join('')}</ul><button class="cream" data-business-first-order>去完成第一笔采购</button><p class="business-note">收成可以在仓库即时出售，也可以准备好后摆到小店。</p></div>`;
    const p=preview(),count=selectedKeys().reduce((sum,key)=>sum+stock[key],0),all=rows(),visible=all.filter(row=>filter==='all'||row.fits||stock[row.key]>0);
    const chooser=model.menus.length>1?`<div class="business-menu-list" role="group" aria-label="选择今天的菜单">${model.menus.map(m=>`<button data-business-menu="${m.id}" aria-pressed="${m.id===menuId}" ${m.unlock.met?'':'aria-disabled="true"'}>${esc(m.name)}${m.unlock.met?'':'<small>未开放</small>'}</button>`).join('')}</div>`:'';
    return `${chooser}${presetsMarkup()}<header class="business-menu-sheet"><span class="business-eyebrow">今天的纸菜单</span><h3>${esc(title())}</h3><p>${esc(CONTENT_TEXT[menuId].text)}</p><small>${esc(CONTENT_TEXT[menuId].complete)}</small></header>
      <div class="business-prep-summary" aria-live="polite"><strong>备好 ${count} / ${model.capacity} 只</strong><span class="business-tier ${p.ready?p.plan.fit.tier:''}">${p.ready?tierLabel[p.plan.fit.tier]:'先摆上出品'}</span></div>
      <div class="business-preferences"><label><input type="checkbox" data-business-keep ${keepOne?'checked':''}>每种在家留1只</label><label><input type="checkbox" data-business-rewards ${useRewards?'checked':''}>使用已有经营奖励</label><label>来客倾向<select data-business-tendency><option value="regulars" ${tendency==='regulars'?'selected':''}>熟客故事优先</option><option value="discovery" ${tendency==='discovery'?'selected':''}>地区线索优先</option></select></label><small>${useRewards?`持有 ${number(getState().progress.trade.credits)} 次，${p.ready?`本单预留 ${p.plan.creditReserve} 次`:'选好货后计算预留'}`:'默认不占用经营奖励次数'}</small></div>
      <div class="business-filter"><button data-business-filter="theme" aria-pressed="${filter==='theme'}">适合菜单</button><button data-business-filter="all" aria-pressed="${filter==='all'}">全部食用出品</button></div>
      <div class="business-stock-list">${visible.length?visible.map(row=>stockRow(row)).join(''):'<div class="business-empty compact"><p>这一页还没有自由库存。</p><small>去厨房做一锅，收好后再摆出来。</small></div>'}</div>
      ${selectedKeys().length?`<p class="business-note">主选先卖完，再用替补。普通出品照常成交；每满2小时卖出最多6只。</p>`:''}
      <p class="business-prep-hint" role="status">${p.ready?'备货由小店照看，最长营业24小时；售罄即收摊。':esc(selectedKeys().length?p.message:'选择数量就能查看搭配与预留，确认开张前不会占用库存。')}</p>`;
  }
  function stockRow(row){
    const quantity=stock[row.key]??0,placement=placements[row.key],menu=REGIONAL.menus.find(m=>m.id===menuId),canAdd=!row.locked&&row.free>quantity&&(!keepOne||row.home-quantity>1);
    const choices=menu.roles.filter(role=>role.allowed.includes(row.key)).flatMap(role=>Array.from({length:role.maxSpecies},(_,index)=>({value:`${role.id}:${index}`,label:`${role.required?'菜单':'搭配'}${menu.roles.length>1?' '+(menu.roles.indexOf(role)+1):''} · ${index?'替补':'主选'}`})));
    const selected=placement&&placement.roleId!=='ordinary'?`${placement.roleId}:${placement.position}`:'ordinary';
    return `<article class="business-stock-row ${quantity?'is-selected':''}"><div class="business-stock-art" aria-hidden="true">${characterPortrait(row.egg,row.id)}</div><div class="business-stock-info"><strong>${esc(row.name)}</strong><small>${esc(row.code)} · 自由 ${row.free}只${row.locked?' · 收藏已锁定':''}</small><span>${row.fits?'可作菜单搭配':'普通营业出品'}</span></div>
      <div class="business-quantity"><button data-business-minus="${row.key}" aria-label="少备1只${esc(row.name)}" ${quantity?'':'disabled'}>−</button><input data-business-quantity="${row.key}" aria-label="${esc(row.name)}备货数量" type="number" inputmode="numeric" min="0" max="${row.free}" value="${quantity}" ${row.locked?'disabled':''}><button data-business-plus="${row.key}" aria-label="多备1只${esc(row.name)}" ${canAdd?'':'disabled'}>＋</button></div>
      ${quantity?`<label class="business-role-picker"><span>摆放位置</span><select data-business-placement="${row.key}" aria-label="${esc(row.name)}的菜单位置">${choices.map(choice=>`<option value="${choice.value}" ${selected===choice.value?'selected':''}>${choice.label}</option>`).join('')}<option value="ordinary" ${selected==='ordinary'?'selected':''}>普通出品</option></select></label>`:''}</article>`;
  }
  function activeMarkup(model){const a=model.active,session=getState().expansion.business.active;
    return `<header class="business-menu-sheet is-open"><span class="business-shop-sign">开张中</span><h3>${esc(a.name)}</h3><p>留给小店慢慢招待，厨房还可以继续忙。</p></header><dl class="business-totals"><div><dt>已经卖出</dt><dd>${a.sold}<small>只</small></dd></div><div><dt>货款已入账</dt><dd>${number(a.income)}<small>CP</small></dd></div></dl>
      <div class="business-next-window"><strong data-business-countdown>下一窗还需 ${remaining(a.nextAt-getNow())}</strong><span>最多6只 · ${tierLabel[a.fit.tier]}</span><small>最晚 ${time(a.hardEndAt)} 收摊，剩余 ${remaining(a.remainingMs)}</small></div>
      <h4 class="business-section-label">柜台上还剩</h4><div class="business-counter-list">${a.stock.map(row=>`<div><span class="business-mini-art" aria-hidden="true">${characterPortrait(row.egg,row.id)}</span><span>${esc(row.name)}</span><strong>${row.quantity}只</strong></div>`).join('')}</div>
      <p class="business-note">经营奖励预留 ${a.creditReserve} 次。按当前已售数量，收摊另结 ${a.projectedBonus} CP；未用次数自动释放。</p>
      <details class="business-windows"><summary>已完成 ${session.windowReports.length} 个接待窗口</summary>${windowRows(session.windowReports)}</details>`;
  }
  function windowRows(windows){return windows.length?`<ol>${windows.map(window=>`<li><span>${time(window.at)} · ${tierLabel[window.tier]}</span><strong>${window.entries.length}只 / ${number(window.baseCP+window.markupCP+window.themeCP)} CP</strong></li>`).join('')}</ol>`:'<p>第一窗满2小时后开始成交。</p>';}
  function reportMarkup(model){const r=model.report;if(!r)return '<div class="business-empty"><h3>账页还是空的</h3><p>完成一单营业后，这里会记下货款与来客记录。</p></div>';
    return `<header class="business-receipt-head"><span class="business-eyebrow">最近一单 · ${time(r.closedAt)}收摊</span><h3>${esc(r.name)}</h3><span class="business-receipt-stamp">已入账</span><p>卖出${r.totalSold}只，${r.reason==='manual'?'提前收摊':r.reason==='sold-out'?'备货售罄':'本单营业结束'}。</p></header>
      <dl class="business-receipt-lines"><div><dt>基础货款</dt><dd>${number(r.baseCP)} CP</dd></div><div><dt>招牌加价</dt><dd>＋${number(r.markupCP)} CP</dd></div><div><dt>主题加价</dt><dd>＋${number(r.themeCP)} CP</dd></div><div><dt>整筐 / 拼盘</dt><dd>＋${number(r.bonusCP)} CP</dd></div><div class="business-receipt-total"><dt>本单共计</dt><dd>${number(r.income)} CP</dd></div></dl>
      <p class="business-report-note">${r.completeMenu?'这一单记下了完整菜单印。':r.validMenu?'这一单完成了有效主题接待。':'这一单按普通营业记入小店账。'}${r.visitorEvents?`共接待${r.visitorEvents}轮来客。`:''}</p>${visitorMarkup(r)}
      <h4 class="business-section-label">实际售出的出品</h4><div class="business-counter-list">${r.entries.map(row=>`<div><span class="business-mini-art" aria-hidden="true">${characterPortrait(row.egg,row.id)}</span><span>${esc(row.name)}</span><strong>${row.quantity}只</strong></div>`).join('')||'<p>本单还没有成交。</p>'}</div>
      <p class="business-note">未售 ${Object.values(r.remainingStock).reduce((a,b)=>a+b,0)} 只已解除营业占用。用了 ${r.creditsUsed} 次经营奖励，释放 ${r.creditsReleased} 次。</p>
      <details class="business-windows"><summary>逐窗账目</summary>${windowRows(r.windowReports)}</details><p class="business-readonly">账单可以反复看，货款已随成交保存。</p>`;
  }
  // Regular stories this receipt's visitors brought; otherwise plain small talk (no CP).
  function visitorMarkup(r){const v=reportVisitors(getState(),r);if(!v.length)return r.visitorEvents?'<p class="business-note">来客随口聊了几句，没有新的故事。</p>':'';
    return `<h4 class="business-section-label">来访的常客</h4><ul class="business-visitors">${v.map(x=>`<li><span>${esc(x.name)}带来「${esc(x.title)}」</span><button data-business-regulars="${x.id}">${x.unread?'去读':'回看'}</button></li>`).join('')}</ul>`;}
  // PJ-1 result: three saved setups. Loading only fills this draft (no reservation).
  function presetsMarkup(){
    const s=getState();if(!projectComplete(s,'PJ-1'))return '';
    const presets=s.expansion.menus.presets,count=selectedKeys().length;
    const slots=Array.from({length:MAX_PRESETS},(_,i)=>{const p=presets[i];if(!p)return i===presets.length?`<button data-business-preset-save="${i}" ${count?'':'disabled'}>存为预设${i+1}</button>`:'';
      const n=Object.values(p.stock).reduce((a,b)=>a+b,0);return `<span class="business-preset"><button data-business-preset-load="${i}">${esc(CONTENT_TEXT[p.menuId]?.name??p.menuId)} · ${n}只</button><button data-business-preset-save="${i}" aria-label="用当前备货覆盖预设${i+1}" ${count?'':'disabled'}>覆盖</button><button data-business-preset-delete="${i}" aria-label="删除预设${i+1}">删除</button></span>`;}).join('');
    return `<div class="business-presets" role="group" aria-label="菜单预设"><span class="business-eyebrow">菜单预设</span>${slots}</div>`;
  }
  function footerMarkup(model){
    const p=mode==='prepare'&&model.access.met?preview():null;
    return `<footer class="business-footer"><button data-business-kitchen>去厨房</button>${mode==='active'?'<button class="orange" data-business-close>提前收摊</button>':mode==='report'?`<button class="orange" data-business-again>${model.active?'回到营业':'准备下一单'}</button>`:`<button class="orange" data-business-open ${!p?.ready||busy?'disabled':''}>确认开张</button>`}</footer>`;
  }
  function bind(model){
    panels.querySelectorAll('[data-business-menu]').forEach(button=>button.onclick=()=>{const m=model.menus.find(x=>x.id===button.dataset.businessMenu);if(!m.unlock.met){alertBox(`${m.name}尚未开放：${m.unlock.missing.join('；')}`);return;}if(menuId!==m.id){menuId=m.id;resetAssignments();render(true);}});
    find('[data-business-kitchen]').onclick=()=>goKitchen?.();
    find('[data-business-again]')?.addEventListener('click',()=>{mode=model.active?'active':'prepare';render();});
    find('[data-business-keep]')?.addEventListener('change',event=>{keepOne=event.target.checked;render(true);});
    find('[data-business-rewards]')?.addEventListener('change',event=>{useRewards=event.target.checked;render(true);});
    find('[data-business-tendency]')?.addEventListener('change',event=>{tendency=event.target.value;render(true);});
    panels.querySelectorAll('[data-business-preset-load]').forEach(button=>button.onclick=()=>{
      const p=getState().expansion.menus.presets[Number(button.dataset.businessPresetLoad)];if(!p)return;
      const m=model.menus.find(x=>x.id===p.menuId);if(!m?.unlock.met){alertBox('这张菜单现在还不能营业。');return;}
      menuId=p.menuId;stock={};const available=rows(),skipped=[];
      for(const [key,n]of Object.entries(p.stock)){const r=available.find(x=>x.key===key);const max=r?Math.max(0,Math.min(r.free,keepOne?r.home-1:r.free)):0;if(max>0)stock[key]=Math.min(n,max);if(!r||max<n)skipped.push(key);}
      resetAssignments();render(true);if(skipped.length)alertBox(`有${skipped.length}种出品库存不足，已按现有自由库存载入。`);
    });
    panels.querySelectorAll('[data-business-preset-save]').forEach(button=>button.onclick=()=>{
      const slot=Number(button.dataset.businessPresetSave),selected=Object.fromEntries(Object.entries(stock).filter(([,n])=>n>0));
      commitProgress(draft=>saveMenuPreset(draft,slot,{menuId,stock:selected}));render(true);
    });
    panels.querySelectorAll('[data-business-preset-delete]').forEach(button=>button.onclick=()=>{commitProgress(draft=>deleteMenuPreset(draft,Number(button.dataset.businessPresetDelete)));render(true);});
    panels.querySelectorAll('[data-business-regulars]').forEach(button=>button.onclick=()=>openRegulars?.(button.dataset.businessRegulars||null));
    panels.querySelectorAll('[data-business-filter]').forEach(button=>button.onclick=()=>{filter=button.dataset.businessFilter;render(true);});
    panels.querySelectorAll('[data-business-plus]').forEach(button=>button.onclick=()=>changeQuantity(button.dataset.businessPlus,(stock[button.dataset.businessPlus]??0)+1));
    panels.querySelectorAll('[data-business-minus]').forEach(button=>button.onclick=()=>changeQuantity(button.dataset.businessMinus,(stock[button.dataset.businessMinus]??0)-1));
    panels.querySelectorAll('[data-business-quantity]').forEach(input=>input.onchange=()=>changeQuantity(input.dataset.businessQuantity,input.value));
    panels.querySelectorAll('[data-business-placement]').forEach(select=>select.onchange=()=>{
      const key=select.dataset.businessPlacement,old=placements[key],parts=select.value.split(':'),next={roleId:parts[0],position:Number(parts[1]??0)};
      const occupied=selectedKeys().find(k=>k!==key&&placements[k]?.roleId===next.roleId&&placements[k]?.position===next.position);
      if(occupied&&next.roleId!=='ordinary')placements[occupied]=old??{roleId:'ordinary',position:0};placements[key]=next;render(true);
    });
    find('[data-business-open]')?.addEventListener('click',()=>{
      if(busy)return;const p=preview();if(!p.ready){alertBox(p.message);return;}const request=structuredClone(options()),amount=Object.values(request.stock).reduce((a,b)=>a+b,0);
      confirmBox(`${title()} · 备货${amount}只\n${tierLabel[p.plan.fit.tier]}，每2小时最多卖6只。\n${keepOne?'每种在家留1只。':'本单允许全部备货。'}经营奖励预留${p.plan.creditReserve}次。\n确认后占用这些出品，最长营业24小时。`,()=>{
        if(busy)return;busy=true;try{const result=commitProgress(draft=>openBusiness(draft,request,Math.floor(getNow())));if(result){stock={};placements={};mode='active';render();}}finally{busy=false;}
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
  function updateTime(){if(!find('.business-screen'))return;const active=businessModel(getState(),getNow()).active;if(mode==='active'&&!active){render(true);return;}const field=find('[data-business-countdown]');if(field&&active)field.textContent=`下一窗还需 ${remaining(active.nextAt-getNow())}`;}
  return {open,refresh,updateTime,unreadReport,prepareDraft(next){stock={...next};resetAssignments();mode='prepare';},reset(){stock={};placements={};mode='prepare';scrollTop=0;}};
}
