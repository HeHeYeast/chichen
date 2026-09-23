import {CONTENT_TEXT} from './content-registry.js';
import {ordersModel} from './order-model.js';
import {acceptProposal,skipProposal,reserveForOrder,releaseReservation,deliverOrderGroups,displayOrder,cancelOrderInstance} from './orders.js';

const esc=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const number=v=>Number(v??0).toLocaleString('zh-CN');
export const tradeNav=current=>`<nav class="trade-nav" aria-label="生意簿"><button data-trade-view="business" aria-pressed="${current==='business'}">营业</button><button data-trade-view="orders" aria-pressed="${current==='orders'}">订单</button><button data-trade-view="regulars" aria-pressed="${current==='regulars'}">常客</button><button data-trade-view="projects" aria-pressed="${current==='projects'}">项目</button></nav>`;

/** Purchases page in the business book: proposals, active orders, delivery sheets. */
export function createOrderUI({getState,getNow,commitProgress,showPanel,panels,alertBox,confirmBox,characterPortrait,openBusiness,openRegulars,openProjects,openStory,openRecipe,goKitchen}){
  let view={kind:'list'},draft={},keepOne=true,picked=[];
  const find=q=>panels.querySelector(q),all=q=>panels.querySelectorAll(q);
  const transact=(fn,after)=>{const result=commitProgress(fn);if(result!==null&&result!==false){after?.(result);return result;}render();return null;};
  const portrait=row=>characterPortrait?characterPortrait(row.egg,row.id):'';
  function shell(body,footer=''){
    showPanel('生意簿 · 订单',`${tradeNav('orders')}<div class="orders-scroll scroll" tabindex="0">${body}</div>${footer?`<footer class="orders-footer">${footer}</footer>`:''}`,'screen-panel orders-screen');
    all('[data-trade-view]').forEach(b=>b.onclick=()=>{if(b.dataset.tradeView==='business')openBusiness?.();else if(b.dataset.tradeView==='regulars')openRegulars?.();else if(b.dataset.tradeView==='projects')openProjects?.();});
  }
  function open(next={kind:'list'}){view=next;draft={};picked=[];render();}
  function refresh(){if(find('.orders-screen'))render();}
  // Coming back from a recipe/supply detour keeps the open sheet and its draft.
  function resume(){render();}
  function render(){const m=ordersModel(getState(),getNow());if(view.kind==='list')list(m);else if(view.kind==='deliver'||view.kind==='reserve')sheet(m);else if(view.kind==='display')display(m);}
  function storyCard(m){
    const open=m.story.find(x=>x.unlocked&&!x.completed),done=m.story.filter(x=>x.completed).length;
    return `<section class="orders-story"><span class="orders-eyebrow">厨房往事 · 旧三章</span><h3>${done===m.story.length?'三章采购都已完成':open?`进行中：第${m.story.indexOf(open)+1}章`:'下一章还在准备'}</h3><p>旧采购保留原来的规则与酬谢，不占下面的新意向名额。</p><button data-orders-story>查看旧采购</button></section>`;
  }
  function list(m){
    if(!m.unlock.met){shell(`${storyCard(m)}<section class="orders-empty"><h3>情境采购还没开始</h3><p>生意再多做一些，就会有人来问能不能帮忙备货。</p><ul>${m.unlock.missing.map(x=>`<li>${esc(x)}</li>`).join('')}</ul></section>`,'<button data-orders-kitchen>回厨房</button>');bind(m);return;}
    const active=m.active.map(a=>`<article class="orders-card" data-order="${a.id}"><header><span class="orders-eyebrow">进行中${a.variantLabel?` · ${esc(a.variantLabel)}`:''}${a.regionName?` · ${esc(a.regionName)}`:''}</span><h3>${esc(a.name)}</h3></header><p>${esc(a.request)}</p>
      ${a.kind==='display'?`<p class="orders-note">供查看，不消耗库存：任选${a.minimumDistinct}种，在家各1只。</p><button class="orange" data-order-display="${a.id}">选3种伙伴给来客看</button>`:
      `<ul class="orders-groups">${a.groups.map(g=>`<li><strong>${esc(g.label)}</strong> 已交 ${g.delivered}/${g.quantity}</li>`).join('')}</ul>
      <p class="orders-note">至少${a.minimumDistinct}种不同出品 · 已付货款 ${number(a.paidCP)} CP · 完成酬谢 ${a.bonusCP} CP${a.reservedTotal?` · 已预留 ${a.reservedTotal}只`:''}${a.needsRestock?' · 预留已因农场减少，需要补货':''}</p>
      <div class="orders-actions"><button class="orange" data-order-deliver="${a.id}">交付</button><button data-order-reserve="${a.id}">预留</button><button data-order-cancel="${a.id}">取消剩余</button></div>`}</article>`).join('');
    const proposals=m.proposals.map(p=>`<article class="orders-card is-proposal" data-proposal="${p.id}"><header><span class="orders-eyebrow">采购意向</span><h3>${esc(p.name)}</h3></header><p>${esc(p.request)}</p>
      <ul class="orders-groups">${p.kind==='display'?`<li>任选${p.minimumDistinct}种有趣模样，在家各1只供看</li>`:p.groups.map(g=>`<li><strong>${esc(g.label)}</strong> ${g.quantity}只</li>`).join('')}</ul>
      <p class="orders-note">${p.kind==='display'?'不扣货、不付款':`按基础售价分批付款，完成再付 ${p.bonusCP} CP`}${p.firstResult?` · 首次完成记下「${esc(p.firstResult)}」`:''}</p>
      ${p.options.length>1?`<label class="orders-variant">选择做法<select data-proposal-variant="${p.id}">${p.options.map((x,i)=>`<option value="${i}">${esc(x.label)}${x.regionName?` · ${esc(x.regionName)}`:''}${x.chapters?` · ${x.chapters.map(esc).join('＋')}`:''}</option>`).join('')}</select></label>`:''}
      <div class="orders-actions"><button class="orange" data-proposal-accept="${p.id}" ${m.canAccept&&p.options.length?'':'disabled'}>接取</button><button data-proposal-skip="${p.id}">略过</button></div>${!m.canAccept?`<small>最多同时进行${m.limits.active}单。</small>`:!p.options.length?'<small>条件已经变化，暂时不能接取。</small>':''}</article>`).join('');
    shell(`${storyCard(m)}<section><h3 class="orders-heading">进行中 ${m.active.length}/${m.limits.active}</h3>${active||'<p class="orders-empty">还没有接下的采购。</p>'}</section><section><h3 class="orders-heading">新的询问</h3>${proposals||'<p class="orders-empty">暂时没有新询问。营业、寻访归来或收完一锅后，可能会有人来问。</p>'}</section>`,'<button data-orders-kitchen>去厨房</button>');
    bind(m);
  }
  function bind(m){
    find('[data-orders-kitchen]')?.addEventListener('click',()=>goKitchen?.());
    find('[data-orders-story]')?.addEventListener('click',()=>openStory?.());
    all('[data-proposal-accept]').forEach(b=>b.onclick=()=>{const p=m.proposals.find(x=>x.id===b.dataset.proposalAccept),index=Number(find(`[data-proposal-variant="${p.id}"]`)?.value??0),option=p.options[index];
      confirmBox(`接下「${p.name}」？\n接取后需求、可替代出品与酬谢冻结，可分批交付，不设期限。`,()=>transact(s=>acceptProposal(s,p.id,{variantId:option.variantId,region:option.region,chapters:option.chapters},getNow()),()=>render()),false,{yes:'接取'});});
    all('[data-proposal-skip]').forEach(b=>b.onclick=()=>transact(s=>skipProposal(s,b.dataset.proposalSkip),()=>{render();alertBox('已略过。继续营业或寻访后，可能会有新的询问。');}));
    all('[data-order-deliver]').forEach(b=>b.onclick=()=>open({kind:'deliver',id:b.dataset.orderDeliver}));
    all('[data-order-reserve]').forEach(b=>b.onclick=()=>open({kind:'reserve',id:b.dataset.orderReserve}));
    all('[data-order-display]').forEach(b=>b.onclick=()=>open({kind:'display',id:b.dataset.orderDisplay}));
    all('[data-order-cancel]').forEach(b=>b.onclick=()=>{const a=m.active.find(x=>x.id===b.dataset.orderCancel);confirmBox(`取消「${a.name}」剩余需求？\n已交的伙伴不退，已付的 ${number(a.paidCP)} CP 保留，完成酬谢不发；预留的伙伴回到自由库存。`,()=>transact(s=>cancelOrderInstance(s,a.id),()=>open()),false,{yes:'取消剩余'});});
  }
  // One sheet for delivery and reservation: explicit group, species and quantity.
  function sheet(m){
    const a=m.active.find(x=>x.id===view.id);if(!a){open();return;}
    const deliver=view.kind==='deliver',total=Object.values(draft).reduce((n,v)=>n+v,0);
    const base=Object.entries(draft).reduce((n,[slot,q])=>{const [gid,key]=slot.split('|');return n+a.groups.find(g=>g.id===gid).rows.find(r=>r.key===key).price*q;},0);
    const completes=deliver&&a.groups.every(g=>g.remaining===Object.entries(draft).filter(([slot])=>slot.startsWith(g.id+'|')).reduce((n,[,q])=>n+q,0));
    const rows=a.groups.map(g=>`<section class="orders-group"><h3>${esc(g.label)} · 还需 ${g.remaining}只</h3>${g.rows.map(r=>{const slot=`${g.id}|${r.key}`,q=draft[slot]??0,limit=deliver?r.usable:r.free,held=a.reserved[r.key]??0;
      return `<div class="orders-row"><span class="orders-art" aria-hidden="true">${portrait(r)}</span><span class="orders-row-info"><strong>${esc(r.name)}</strong><small>${r.code} · ${deliver?`可交 ${r.usable}`:`自由 ${r.free}`}${held?` · 本单预留 ${held}`:''} · ${r.price} CP</small>${(deliver?r.usable:r.free)===0&&openRecipe?`<button class="orders-recipe" data-order-recipe="${r.key}">去看做法 ›</button>`:''}</span><span class="orders-qty"><button data-slot-minus="${slot}" aria-label="少选1只${esc(r.name)}" ${q?'':'disabled'}>−</button><output>${q}</output><button data-slot-plus="${slot}" aria-label="多选1只${esc(r.name)}" ${q<limit&&total<g.remaining+total?'':'disabled'}>＋</button></span></div>`;}).join('')}${g.hidden?`<p class="orders-note">另有${g.hidden}种符合的伙伴尚未收录。</p>`:''}${!deliver&&g.rows.length?'':''}</section>`).join('');
    const held=Object.entries(a.reserved).map(([key,q])=>`<li>${esc(a.groups.flatMap(g=>g.rows).find(r=>r.key===key)?.name??key)} ×${q} <button data-release="${key}">释放</button></li>`).join('');
    shell(`<header class="orders-sheet-head"><span class="orders-eyebrow">${deliver?'交付':'预留'} · ${esc(a.name)}</span><p>${deliver?'每只伙伴只计入一个需求组；先用本单预留，再用自由库存。':'预留只是为本单留着，不算交付，也不会被营业或寻访占用；随时可以释放。'}</p></header>${deliver?`<label class="orders-keep"><input type="checkbox" data-orders-keep ${keepOne?'checked':''}>每种在家留1只</label>`:''}${rows}${!deliver&&held?`<section class="orders-group"><h3>已预留</h3><ul>${held}</ul></section>`:''}`,
      `<button data-orders-back>返回订单</button><button class="orange" data-orders-confirm ${total?'':'disabled'}>${deliver?`交付${total}只 · ${number(base+(completes?a.bonusCP:0))} CP`:`预留${total}只`}</button>`);
    find('[data-orders-back]').onclick=()=>open();
    all('[data-order-recipe]').forEach(b=>b.onclick=()=>openRecipe?.(b.dataset.orderRecipe));
    find('[data-orders-keep]')?.addEventListener('change',e=>{keepOne=e.target.checked;sheet(ordersModel(getState(),getNow()));});
    all('[data-slot-plus]').forEach(b=>b.onclick=()=>{const slot=b.dataset.slotPlus,gid=slot.split('|')[0],g=a.groups.find(x=>x.id===gid),inGroup=Object.entries(draft).filter(([k])=>k.startsWith(gid+'|')).reduce((n,[,q])=>n+q,0);if(inGroup>=g.remaining)return;draft[slot]=(draft[slot]??0)+1;sheet(ordersModel(getState(),getNow()));});
    all('[data-slot-minus]').forEach(b=>b.onclick=()=>{const slot=b.dataset.slotMinus;draft[slot]=Math.max(0,(draft[slot]??0)-1);if(!draft[slot])delete draft[slot];sheet(ordersModel(getState(),getNow()));});
    all('[data-release]').forEach(b=>b.onclick=()=>transact(s=>releaseReservation(s,a.id,b.dataset.release),()=>sheet(ordersModel(getState(),getNow()))));
    find('[data-orders-confirm]').onclick=()=>{
      const allocations=Object.entries(draft).map(([slot,quantity])=>{const [groupId,key]=slot.split('|');return {groupId,key,quantity};});
      if(!deliver){transact(s=>{for(const x of allocations)reserveForOrder(s,a.id,x.key,x.quantity);return true;},()=>{draft={};sheet(ordersModel(getState(),getNow()));});return;}
      confirmBox(`交付${total}只给「${a.name}」？\n本次货款 ${number(base)} CP${completes?`，完成酬谢 ${a.bonusCP} CP`:''}。已交的伙伴不能取回。`,()=>transact(s=>deliverOrderGroups(s,a.id,allocations,getNow(),{overrideKeepOne:!keepOne}),r=>{open();alertBox(`已交付，收到 ${number(r.income)} CP。${r.complete?`\n${CONTENT_TEXT[r.templateId]?.finish??''}${r.firstResult?`\n记下「${CONTENT_TEXT[r.firstResult]?.name??r.firstResult}」。`:''}`:''}`);}),false,{yes:`交付${total}只`});
    };
  }
  function display(m){
    const a=m.active.find(x=>x.id===view.id);if(!a){open();return;}
    const g=a.groups[0],rows=g.rows.filter(r=>r.free>0);
    shell(`<header class="orders-sheet-head"><span class="orders-eyebrow">供查看 · ${esc(a.name)}</span><p>任选${a.minimumDistinct}种，在家各有1只自由的伙伴即可；看完仍是你的，不扣货、不付款。</p></header><div class="orders-display">${rows.map(r=>`<label class="orders-row"><input type="checkbox" data-display-key="${r.key}" ${picked.includes(r.key)?'checked':''} ${!picked.includes(r.key)&&picked.length>=a.minimumDistinct?'disabled':''}><span class="orders-art" aria-hidden="true">${portrait(r)}</span><span class="orders-row-info"><strong>${esc(r.name)}</strong><small>${r.code} · 在家自由 ${r.free}</small></span></label>`).join('')||'<p class="orders-empty">在家暂时没有符合的伙伴。</p>'}${g.hidden?`<p class="orders-note">另有${g.hidden}种符合的伙伴尚未收录。</p>`:''}</div>`,
      `<button data-orders-back>返回订单</button><button class="orange" data-display-confirm ${picked.length===a.minimumDistinct?'':'disabled'}>请来客看看</button>`);
    find('[data-orders-back]').onclick=()=>open();
    all('[data-display-key]').forEach(input=>input.onchange=()=>{const key=input.dataset.displayKey;picked=input.checked?[...picked,key]:picked.filter(k=>k!==key);display(ordersModel(getState(),getNow()));});
    find('[data-display-confirm]').onclick=()=>transact(s=>displayOrder(s,a.id,[...picked]),r=>{open();alertBox(`${CONTENT_TEXT[r.templateId]?.finish??''}${r.firstResult?`\n记下「${CONTENT_TEXT[r.firstResult]?.name??r.firstResult}」。`:''}\n没有扣除伙伴，也没有货款。`);});
  }
  return {open,resume,refresh};
}
