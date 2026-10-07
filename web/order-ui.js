import {CONTENT_TEXT} from './content-registry.js';
import {ordersModel,SEASON_NAME} from './order-model.js';
import {acceptProposal,skipProposal,reserveForOrder,releaseReservation,deliverOrderGroups,displayOrder,cancelOrderInstance} from './orders.js';
import {shopSubpageHeader,shopSubpagePaper,shopSubpageDialog,bindShopSubpage} from './business-subpages.js';
import {familyBasket,familyArt} from './business-family-art.js';
import {kitBar,kitButton,kitButton2,kitChip,kitChipHtml,kitCell,kitIcon,kitLabel} from './ui-kit.js';

const esc=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const number=v=>Number(v??0).toLocaleString('zh-CN');
// The chicken-only variant's authoring note is not player-facing copy.
const requestText=a=>a.templateId==='O01'&&a.variantLabel===CONTENT_TEXT['O01-B'].label?'家里早上人多，这单备一篮熟悉的鸡宝就好，不必追稀罕的。':a.request;
// 2026-10-07 (batch 3): 生意 is one home page; 常客 and 项目 are its second level (‹ goes back), so the four peer
// bookmarks are gone. The old markup is kept below for reference only.
export const tradeNav=()=>'';
export const tradeNavLegacy=current=>`<nav class="trade-nav" aria-label="生意簿"><button data-trade-view="business" aria-pressed="${current==='business'}">营业</button><button data-trade-view="orders" aria-pressed="${current==='orders'}">订单</button><button data-trade-view="regulars" aria-pressed="${current==='regulars'}">常客</button><button data-trade-view="projects" aria-pressed="${current==='projects'}">项目</button></nav>`;

/** Purchases page in the business book: proposals, active orders, delivery sheets. */
export function createOrderUI({getState,getNow,commitProgress,showPanel,panels,alertBox,confirmBox,characterPortrait,openBusiness,openRegulars,openProjects,openStory,openRecipe,goKitchen}){
  // slotOpen: the delivery tile whose amount drawer is open; variants: the 做法 picked on each proposal.
  let view={kind:'list'},draft={},keepOne=true,picked=[],slotOpen=null,variants={};
  const find=q=>panels.querySelector(q),all=q=>panels.querySelectorAll(q);
  const transact=(fn,after)=>{const result=commitProgress(fn);if(result!==null&&result!==false){after?.(result);return result;}render();return null;};
  const portrait=row=>characterPortrait?characterPortrait(row.egg,row.id):'';
  const examples=rows=>{const unique=[...new Map(rows.map(r=>[r.key,r])).values()].sort((a,b)=>b.free-a.free).slice(0,2);return `<div class="order-examples" aria-label="符合需求的已收录出品示例">${unique.length?unique.map(r=>`<span title="${esc(r.name)}">${familyBasket(r)}<small>${esc(r.name)}</small></span>`).join(''):familyArt('orders')}</div>`;};
  function shell(body,footer=''){
    const scroll=find('.orders-scroll')?.scrollTop??0,helpOpen=find('.bs-sub-dialog')?.open;
    find('.bs-sub-dialog')?.close();
    const listFooter=view.kind==='list'&&footer?`<footer class="orders-footer">${footer}</footer>`:'';
    showPanel('生意簿 · 订单',`${shopSubpageHeader('orders')}${tradeNav('orders')}<div class="orders-scroll scroll" tabindex="0" data-list>${shopSubpagePaper('orders',body+listFooter)}</div>${footer&&!listFooter?`<footer class="orders-footer">${footer}</footer>`:''}${shopSubpageDialog('orders')}`,'screen-panel orders-screen shop-subpage shop-orders');
    find('.orders-scroll').scrollTop=scroll;bindShopSubpage(panels,openBusiness);if(helpOpen)find('.bs-sub-dialog').showModal();
    all('[data-trade-view]').forEach(b=>b.onclick=()=>{if(b.dataset.tradeView==='business')openBusiness?.();else if(b.dataset.tradeView==='regulars')openRegulars?.();else if(b.dataset.tradeView==='projects')openProjects?.();});
  }
  function open(next={kind:'list'}){view=next;draft={};picked=[];slotOpen=null;keepOne=getState().expansion?.inventoryPolicy?.keepOne!==false;if(find('.orders-scroll'))find('.orders-scroll').scrollTop=0;render();}
  function refresh(){if(find('.orders-screen'))render();}
  // Coming back from a recipe/supply detour keeps the open sheet and its draft.
  function resume(){render();}
  function render(){const m=ordersModel(getState(),getNow());if(view.kind==='list')list(m);else if(view.kind==='deliver'||view.kind==='reserve')sheet(m);else if(view.kind==='display')display(m);}
  // 厨房往事 is taped to the top of the pad: the chapter on a stamp, its partner as a dotted line to the count.
  function storyCard(m){
    const open=m.story.find(x=>x.unlocked&&!x.completed),done=m.story.filter(x=>x.completed).length,all=done===m.story.length;
    const c=open?(open.choices.find(c=>c.species===(open.choice??open.choices[0].species))):null;
    const face=c?`<span class="od-face">${characterPortrait?characterPortrait(...c.species.split(':').map(Number)):''}</span>`:'';
    return `<section class="orders-story od-slip is-story"><img class="od-fastener tape" src="/web/art/golden-collection/v2-tape.png" alt="" data-overhang><span class="orders-eyebrow">厨房往事</span><header class="od-slip-head"><h3>${c?esc(open.chapter.title.split('：').at(-1)):'厨房往事'}</h3><span class="od-stamp is-story"><b>${all?'读完':open?`第${m.story.indexOf(open)+1}章`:'往事'}</b></span></header>${c?`<div class="od-lines"><div class="od-line">${face}<b class="od-what">${esc(c.name)}</b><i class="od-dots" aria-hidden="true"></i><b class="od-count">${open.delivered}/${c.count}</b></div></div>`:''}<div class="gd-actions" data-row><button type="button" class="gd-btn2" data-orders-story ${c?'data-next':''}>${c?(open.accepted?'去交付':'去看看'):all?'重读':'去看看'}</button></div></section>`;
  }
  function list(m){
    if(!m.unlock.met){shell(`${storyCard(m)}<section class="orders-empty"><h3>还没有人来下订单</h3><p>再多收一些伙伴、多认识几种，就会有人来问能不能帮忙备货。</p><ul>${m.unlock.missing.map(x=>`<li>${esc(x)}</li>`).join('')}</ul></section>`,'<button data-orders-kitchen>回厨房</button>');bind(m);return;}
    // Each order is a waiter's slip on the pad: a pin (new) or a clip (taken), the customer's name, the reward
    // as a coin stamp, and every wanted group as a dotted line to its count (like a materials list).
    const stamp=a=>a.kind==='display'?'<span class="od-stamp is-look"><b>只看</b></span>':`<span class="od-stamp"><img src="/web/art/golden-business/coin.png" alt=""><b>+${number(a.bonusCP)}</b></span>`;
    const sticker=row=>row?`<span class="od-face">${portrait(row)}</span>`:'<span class="od-face is-any"><span class="bk-q" aria-hidden="true">?</span></span>';
    const line=(g,row,count)=>`<div class="od-line${count&&g.delivered>=g.quantity?' done':''}">${sticker(row)}<b class="od-what">${esc(g.label)}</b><i class="od-dots" aria-hidden="true"></i><b class="od-count">${count?`${g.delivered}/${g.quantity}`:`×${g.quantity}`}</b></div>`;
    const active=m.active.map(a=>`<article class="orders-card od-slip is-taken" data-order="${a.id}"><img class="od-fastener clip" src="/web/art/golden-business/family-clip.png" alt="" data-overhang><header class="od-slip-head"><h3>${esc(a.name)}</h3>${stamp(a)}</header>
      ${a.kind==='display'?`<div class="od-lines"><div class="od-line"><span class="od-face is-any"><span class="bk-q" aria-hidden="true">?</span></span><b class="od-what">任选 ${a.minimumDistinct??3} 种</b><i class="od-dots" aria-hidden="true"></i><b class="od-count">各 1 只</b></div></div><div class="gd-actions" data-row>${kitButton('选 3 种','data-order-display="'+a.id+'"')}</div>`:
      `<div class="od-lines">${a.groups.map(g=>line(g,[...g.rows].sort((x,y)=>y.free-x.free)[0],true)).join('')}</div>
      <div class="gd-row od-tags">${a.reservedTotal?kitChip('',`留着 ${a.reservedTotal}`,'mini soft'):''}${a.needsRestock?kitChip('','预留不够了','mini hot'):''}</div>
      <div class="gd-actions" data-row>${kitButton2('预留',`data-order-reserve="${a.id}"`)}${kitButton('交付',`data-order-deliver="${a.id}"`)}</div>`}</article>`).join('');
    const proposals=m.proposals.map(p=>{const v=variants[p.id]??0,rows=p.options[v]?.artRows??[];return `<article class="orders-card od-slip is-proposal" data-proposal="${p.id}"><img class="od-fastener pin" src="/web/art/golden-business/family-pin.png" alt="" data-overhang><header class="od-slip-head"><h3>${esc(p.name)}</h3>${stamp(p)}</header>
      <div class="od-lines" data-proposal-art="${p.id}">${p.kind==='display'?`<div class="od-line"><span class="od-face is-any"><span class="bk-q" aria-hidden="true">?</span></span><b class="od-what">任选 ${p.minimumDistinct} 种</b><i class="od-dots" aria-hidden="true"></i><b class="od-count">各 1 只</b></div>`:p.groups.map((g,n)=>line(g,rows[n]??rows[0],false)).join('')}</div>
      ${p.options.length>1?`<div class="gd-row gd-wrap od-variants" role="group" aria-label="选择做法">${p.options.map((x,i)=>`<button type="button" class="gd-btn2 mini" data-proposal-variant="${p.id}" data-index="${i}" aria-pressed="${i===v}">${esc(x.label)}${x.regionName?` · ${esc(x.regionName)}`:''}</button>`).join('')}</div>`:''}
      <div class="gd-actions" data-row>${kitButton2('略过',`data-proposal-skip="${p.id}"`)}${kitButton('接取',`data-proposal-accept="${p.id}"${m.canAccept&&p.options.length?'':' disabled'}`)}</div>${!m.canAccept?`<span class="gd-note">最多同时 ${m.limits.active} 单</span>`:!p.options.length?'<span class="gd-note">条件变了，暂时不能接</span>':''}</article>`;}).join('');
    shell(`${m.story.some(x=>x.unlocked&&!x.completed)?storyCard(m):''}<section class="od-section">${kitLabel(`进行中 ${m.active.length}/${m.limits.active}`)}${active||`<div class="orders-empty od-empty"><img src="/web/art/golden-business/family-clip.png" alt=""><span>${m.completed?`已办好 ${m.completed} 单 · `:''}接下的订单夹在这里</span></div>`}</section><section class="od-section">${kitLabel('新的询问')}${proposals||`<div class="orders-empty od-empty is-asks"><img src="/web/art/golden-ui/ic-bell.png" alt=""><span>暂时没有新询问</span>${kitButton2('去厨房','data-orders-kitchen')}</div>`}</section>${m.story.some(x=>x.unlocked&&!x.completed)?'':storyCard(m)}`,proposals?kitButton2('去厨房','data-orders-kitchen'):'');
    bind(m);
  }
  function bind(m){
    find('.bs-sub-dialog').insertAdjacentHTML('beforeend',`<h4>来客的叮嘱</h4>${m.active.map(a=>`<h4>${esc(a.name)}</h4><p>${esc(requestText(a))}</p><p>至少${a.minimumDistinct}种出品；已付货款 ${number(a.paidCP)} CP。</p>`).join('')}${m.proposals.map(p=>`<h4>${esc(p.name)}</h4><p>${esc(p.request)}</p>${p.firstResult?`<p>首次完成记下「${esc(p.firstResult)}」。</p>`:''}`).join('')}`);

    all('[data-proposal-variant]').forEach(button=>button.onclick=()=>{variants[button.dataset.proposalVariant]=Number(button.dataset.index);render();});
    find('[data-orders-kitchen]')?.addEventListener('click',()=>goKitchen?.());
    find('[data-orders-story]')?.addEventListener('click',()=>openStory?.());
    all('[data-proposal-accept]').forEach(b=>b.onclick=()=>{const p=m.proposals.find(x=>x.id===b.dataset.proposalAccept),index=variants[p.id]??0,option=p.options[index];
      confirmBox(`接下「${p.name}」？\n可以分批交，不设期限`,()=>transact(s=>acceptProposal(s,p.id,{variantId:option.variantId,region:option.region,chapters:option.chapters},getNow()),()=>render()),false,{yes:'接取'});});
    all('[data-proposal-skip]').forEach(b=>b.onclick=()=>transact(s=>skipProposal(s,b.dataset.proposalSkip),()=>{render();alertBox('已略过。继续营业或寻访后，可能会有新的询问。');}));
    all('[data-order-deliver]').forEach(b=>b.onclick=()=>open({kind:'deliver',id:b.dataset.orderDeliver}));
    all('[data-order-reserve]').forEach(b=>b.onclick=()=>open({kind:'reserve',id:b.dataset.orderReserve}));
    all('[data-order-display]').forEach(b=>b.onclick=()=>open({kind:'display',id:b.dataset.orderDisplay}));
    all('[data-order-cancel]').forEach(b=>b.onclick=()=>{const a=m.active.find(x=>x.id===b.dataset.orderCancel);confirmBox(`取消「${a.name}」剩余需求？\n已交的伙伴不退，已付的 ${number(a.paidCP)} CP 保留，完成酬谢不发；预留的伙伴恢复可用。`,()=>transact(s=>cancelOrderInstance(s,a.id),()=>open()),false,{yes:'取消剩余'});});
  }
  // One sheet for delivery and reservation: a tile per partner in each group. 「一键配齐」 fills the
  // groups; a tile opens its amount drawer (slider, quick picks, plus/minus for fine tuning).
  function limits(a,deliver){
    const used=gid=>Object.entries(draft).filter(([slot])=>slot.startsWith(gid+'|')).reduce((n,[,q])=>n+q,0);
    const max=(g,r)=>{const slot=`${g.id}|${r.key}`;return Math.max(0,Math.min(deliver?r.usable:r.free,g.remaining-used(g.id)+(draft[slot]??0)));};
    return {used,max};
  }
  function sheet(m){
    const a=m.active.find(x=>x.id===view.id);if(!a){open();return;}
    const deliver=view.kind==='deliver',total=Object.values(draft).reduce((n,v)=>n+v,0),{used,max}=limits(a,deliver);
    const base=Object.entries(draft).reduce((n,[slot,q])=>{const [gid,key]=slot.split('|');return n+a.groups.find(g=>g.id===gid).rows.find(r=>r.key===key).price*q;},0);
    const completes=deliver&&a.groups.every(g=>g.remaining===used(g.id));
    const groups=a.groups.map(g=>{const missing=g.rows.filter(r=>(deliver?r.usable:r.free)===0);
      return `<section class="orders-group od-sheet-group">${kitLabel(`${g.label} · 还需 ${g.remaining}`)}<div class="kp-grid od-grid">${g.rows.map(r=>{const slot=`${g.id}|${r.key}`,q=draft[slot]??0,limit=deliver?r.usable:r.free,held=a.reserved[r.key]??0;
        return kitCell({pic:portrait(r),name:r.name,count:q?`${deliver?'交':'留'} ${q}`:`×${limit}`,on:q>0,tag:held?`留 ${held}`:'',attrs:`data-slot-pick="${slot}"${limit||q?'':' disabled'}`,label:`${r.name}，${deliver?'可交':'可用'} ${limit} 只${q?`，已选 ${q}`:''}`});}).join('')}</div>
        ${missing.length&&openRecipe?`<div class="gd-row gd-wrap">${missing.slice(0,3).map(r=>kitButton2(`做${r.name}`,`data-order-recipe="${r.key}"`)).join('')}</div>`:''}${g.hidden?`<div class="gd-row">${kitChip('',`另有 ${g.hidden} 种没收录`,'mini')}</div>`:''}</section>`;}).join('');
    const held=Object.entries(a.reserved).map(([key,q])=>`<span class="od-held">${kitChip('',`${esc(a.groups.flatMap(g=>g.rows).find(r=>r.key===key)?.name??key)} ×${q}`,'mini')}${kitButton2('释放',`data-release="${key}"`)}</span>`).join('');
    const head=`${kitLabel(`${deliver?'交付':'预留'} · ${a.name}`)}<div class="gd-row od-sheet-head">${kitButton2('一键配齐','data-orders-fill')}</div>`;
    let drawer='';
    if(slotOpen){const [gid,key]=slotOpen.split('|'),g=a.groups.find(x=>x.id===gid),r=g?.rows.find(x=>x.key===key);
      if(r){const top=max(g,r),q=Math.min(draft[slotOpen]??0,top);
        const quick=[...new Map([[1,'1 只'],[Math.max(1,Math.round(top/2)),'一半'],[top,'全部']].filter(([v])=>v>0&&v<=top)).entries()];
        drawer=`<div class="kp-scrim" data-slot-done></div><section class="kp-drawer gd od-drawer" role="dialog" aria-modal="true" aria-label="${esc(r.name)}"><div class="kp-drawer-title" data-art="plank"><span data-safe><b>${esc(r.name)}</b></span></div><div class="kp-drawer-body">
          <div class="gd-head" data-row><span class="gd-face">${portrait(r)}</span><div class="gd-row">${kitChip('',`${deliver?'可交':'可用'} ${deliver?r.usable:r.free}`,'soft')}${kitChipHtml(`${kitIcon.coin}${r.price}/只`,'soft')}</div></div>
          <div class="gd-qty" data-row><button type="button" class="gd-round minus" data-slot-minus="${slotOpen}" aria-label="少选1只" ${q?'':'disabled'}></button><output class="gd-big" aria-live="polite">${q}<small>只</small></output><button type="button" class="gd-round plus" data-slot-plus="${slotOpen}" aria-label="多选1只" ${q<top?'':'disabled'}></button></div>
          <label class="gd-slider"><span class="sr-only">拖动选择数量</span><input type="range" min="0" max="${top}" step="1" value="${q}" data-slot-range style="--fill:${top?q/top*100:0}%"></label>
          <div class="gd-coins" data-row>${quick.map(([v,l])=>`<button type="button" class="gd-coinwrap${v===q?' on':''}" data-slot-set="${v}" aria-pressed="${v===q}"><span class="gd-coin">${v}</span><b>${l}</b></button>`).join('')}</div>
          <div class="gd-actions" data-row>${kitButton2('不要','data-slot-clear')}${kitButton('好','data-slot-done')}</div></div></section>`;}}
    shell(`${head}${groups}${!deliver&&held?`<section class="orders-group od-sheet-group">${kitLabel('已预留')}<div class="gd-row gd-wrap od-helds">${held}</div></section>`:''}${deliver?`<div class="gd-row od-cancel">${kitButton2('取消剩余',`data-order-cancel="${a.id}"`)}</div>`:''}`,
      `${kitButton2('返回','data-orders-back')}${kitButton(total?`${deliver?'交付':'预留'} ${total} 只`:deliver?'交付':'预留',`data-orders-confirm${total?'':' disabled'}`)}`);
    if(deliver&&total)find('.orders-footer')?.insertAdjacentHTML('afterbegin',`<div class="od-sum">${kitChipHtml(`${kitIcon.coin}+${number(base+(completes?a.bonusCP:0))}`,'mini')}</div>`);
    if(drawer)find('.orders-screen').insertAdjacentHTML('beforeend',drawer);
    const again=()=>sheet(ordersModel(getState(),getNow()));
    find('[data-orders-back]').onclick=()=>open();
    all('[data-order-recipe]').forEach(b=>b.onclick=()=>openRecipe?.(b.dataset.orderRecipe));
    all('[data-order-cancel]').forEach(b=>b.onclick=()=>confirmBox(`取消「${a.name}」剩下的？\n已交的不退，已付的 ${number(a.paidCP)} CP 留着，酬谢不发`,()=>transact(s=>cancelOrderInstance(s,a.id),()=>open()),false,{title:'取消订单',yes:'取消剩余',no:'再想想'}));
    find('[data-orders-fill]').onclick=()=>{draft={};for(const g of a.groups){let need=g.remaining;for(const r of [...g.rows].sort((x,y)=>(a.reserved[y.key]??0)-(a.reserved[x.key]??0)||(deliver?y.usable-x.usable:y.free-x.free))){if(need<=0)break;const q=Math.min(need,deliver?r.usable:r.free);if(q>0){draft[`${g.id}|${r.key}`]=q;need-=q;}}}again();};
    const setSlot=(slot,q)=>{const [gid,key]=slot.split('|'),g=a.groups.find(x=>x.id===gid),r=g.rows.find(x=>x.key===key),top=max(g,r);const n=Math.max(0,Math.min(top,q));if(n)draft[slot]=n;else delete draft[slot];};
    all('[data-slot-pick]').forEach(b=>b.onclick=()=>{const slot=b.dataset.slotPick;if(!draft[slot]){const [gid,key]=slot.split('|'),g=a.groups.find(x=>x.id===gid);setSlot(slot,max(g,g.rows.find(x=>x.key===key)));}slotOpen=slot;again();});
    all('[data-slot-plus]').forEach(b=>b.onclick=()=>{setSlot(b.dataset.slotPlus,(draft[b.dataset.slotPlus]??0)+1);again();});
    all('[data-slot-minus]').forEach(b=>b.onclick=()=>{setSlot(b.dataset.slotMinus,(draft[b.dataset.slotMinus]??0)-1);again();});
    all('[data-slot-set]').forEach(b=>b.onclick=()=>{setSlot(slotOpen,Number(b.dataset.slotSet));again();});
    {const range=find('[data-slot-range]');if(range){const out=range.closest('.kp-drawer-body').querySelector('.gd-big');range.oninput=()=>{range.style.setProperty('--fill',(range.max>0?range.value/range.max*100:0)+'%');out.firstChild.textContent=range.value;};range.onchange=()=>{setSlot(slotOpen,Number(range.value));again();};}}
    find('[data-slot-clear]')?.addEventListener('click',()=>{delete draft[slotOpen];slotOpen=null;again();});
    all('[data-slot-done]').forEach(b=>b.onclick=()=>{slotOpen=null;again();});
    all('[data-release]').forEach(b=>b.onclick=()=>transact(s=>releaseReservation(s,a.id,b.dataset.release),again));
    find('[data-orders-confirm]').onclick=()=>{
      const allocations=Object.entries(draft).map(([slot,quantity])=>{const [groupId,key]=slot.split('|');return {groupId,key,quantity};});
      if(!deliver){transact(s=>{for(const x of allocations)reserveForOrder(s,a.id,x.key,x.quantity);return true;},()=>{draft={};again();});return;}
      confirmBox(`交付 ${total} 只给「${a.name}」？\n货款 ${number(base)} CP${completes?`，酬谢 ${a.bonusCP} CP`:''}；交了不能取回`,()=>transact(s=>deliverOrderGroups(s,a.id,allocations,getNow(),{overrideKeepOne:!keepOne}),r=>{open();alertBox(`收到 ${number(r.income)} CP${r.complete?`\n${CONTENT_TEXT[r.templateId]?.finish??''}${r.firstResult?`\n记下「${CONTENT_TEXT[r.firstResult]?.name??r.firstResult}」`:''}`:''}`);}),false,{title:'交付',yes:`交付 ${total} 只`});
    };
  }
  function display(m){
    const a=m.active.find(x=>x.id===view.id);if(!a){open();return;}
    const g=a.groups[0],rows=g.rows.filter(r=>r.free>0);
    shell(`<div class="kp-bar od-sheet-head" data-row>${kitChip('',`供查看 · ${a.name}`,'soft')}${kitChip('',`选 ${picked.length}/${a.minimumDistinct}`,'mini')}</div><div class="kp-grid od-grid orders-display">${rows.map(r=>{const on=picked.includes(r.key);return kitCell({pic:portrait(r),name:r.name,count:on?'选中':`×${r.free}`,on,attrs:`role="checkbox" aria-checked="${on}" data-display-key="${r.key}"${!on&&picked.length>=a.minimumDistinct?' disabled':''}`});}).join('')||'<p class="orders-empty">在家暂时没有符合的伙伴</p>'}</div>${g.hidden?`<span class="gd-note">另有 ${g.hidden} 种还没收录</span>`:''}`,
      `${kitButton2('返回','data-orders-back')}${kitButton('请来看','data-display-confirm'+(picked.length===a.minimumDistinct?'':' disabled'))}`);
    find('[data-orders-back]').onclick=()=>open();
    all('[data-display-key]').forEach(button=>button.onclick=()=>{const key=button.dataset.displayKey;picked=picked.includes(key)?picked.filter(k=>k!==key):[...picked,key];display(ordersModel(getState(),getNow()));});
    find('[data-display-confirm]').onclick=()=>transact(s=>displayOrder(s,a.id,[...picked]),r=>{open();alertBox(`${CONTENT_TEXT[r.templateId]?.finish??''}${r.firstResult?`\n记下「${CONTENT_TEXT[r.firstResult]?.name??r.firstResult}」。`:''}\n没有扣除伙伴，也没有货款。`);});
  }
  return {open,resume,refresh};
}
