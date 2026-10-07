// A single golden-sample theme page. Existing character pixels and collection
// facts remain authoritative; paper/state assets are individually cut RGBA files.
import {uiPreference,setUiPreference} from './ui-preferences.js';
import {characterImage} from './catalog.js';
import {GOLDEN_PORTRAITS} from './golden-portrait-metrics.js';

const esc=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const art=name=>`/web/art/golden-collection/${name}.png`;
const picture=(name,cls='')=>`<img class="gs-asset ${cls}" src="${art(name)}" alt="" aria-hidden="true" draggable="false">`;

export function goldenThemeView(card){
  const target=card.stages[1]?.target??6;
  return {id:card.id,name:card.name,count:card.known.length,target,
    // Six is a display capacity, not six specific required identities.
    visible:card.known.slice(0,6),empty:Math.max(0,6-card.known.length),
    collectionGranted:!!card.stages[0]?.granted,practiceGranted:!!card.practice.granted,
    representative:card.representative,all:card.known};
}

function sticker(k,characterPortrait,extra=''){
  const path=characterImage(k.egg,k.id),m=GOLDEN_PORTRAITS[path];
  const portrait=m?`<svg class="sprite-art" viewBox="${m.bounds.join(' ')}" aria-hidden="true"><image href="${path}" width="${m.size[0]}" height="${m.size[1]}"/></svg>`:characterPortrait(k.egg,k.id);
  const size=m?`style="width:${m.display[0]}px;height:${m.display[1]}px"`:'';
  return `<button class="gs-character ${extra}" data-gs-species="${k.key}" aria-label="查看${esc(k.name)}档案"><span class="gs-sticker"><span class="gs-sticker-ink" ${size}>${portrait}</span></span><span class="gs-character-name">${esc(k.name)}</span></button>`;
}
function stamp(label,done,kind,portrait){
  return `<button class="gs-stamp gs-stamp-${kind} ${done?'is-done':'is-pending'}" data-gs-stamp="${kind}" aria-label="${label}：${done?'已完成':'未完成'}，查看条件">${picture(`v2-${kind}-${done?'active':'inactive'}`)}${kind==='collection'?`<b class="gs-stamp-emblem" aria-hidden="true">${portrait(0,0)}</b>`:''}<span>${label}</span></button>`;
}
const stickerFilter=`<svg class="gs-filter-defs" aria-hidden="true" width="0" height="0"><defs><filter id="gs-sticker-filter" x="-18%" y="-18%" width="136%" height="140%" color-interpolation-filters="sRGB"><feMorphology in="SourceAlpha" operator="dilate" radius="4" result="outline"/><feFlood flood-color="#fffef5" result="paper"/><feComposite in="paper" in2="outline" operator="in" result="white"/><feGaussianBlur in="outline" stdDeviation="2.1" result="soft"/><feOffset in="soft" dy="2.5" result="offset"/><feFlood flood-color="#684626" flood-opacity=".20" result="shade"/><feComposite in="shade" in2="offset" operator="in" result="shadow"/><feMerge><feMergeNode in="shadow"/><feMergeNode in="white"/><feMergeNode in="SourceGraphic"/></feMerge></filter><filter id="gs-ink-active" color-interpolation-filters="sRGB"><feColorMatrix in="SourceGraphic" type="matrix" values="0 0 0 0 0  0 0 0 0 0  0 0 0 0 0  -.2126 -.7152 -.0722 0 1" result="luma"/><feComponentTransfer in="luma" result="dark"><feFuncA type="linear" slope="2.5" intercept="-.5"/></feComponentTransfer><feComposite in="dark" in2="SourceAlpha" operator="in" result="ink"/><feFlood flood-color="#709b60"/><feComposite in2="ink" operator="in"/></filter><filter id="gs-ink-inactive" color-interpolation-filters="sRGB"><feColorMatrix in="SourceGraphic" type="matrix" values="0 0 0 0 0  0 0 0 0 0  0 0 0 0 0  -.2126 -.7152 -.0722 0 1" result="luma"/><feComponentTransfer in="luma" result="dark"><feFuncA type="linear" slope="2.5" intercept="-.5"/></feComponentTransfer><feComposite in="dark" in2="SourceAlpha" operator="in" result="ink"/><feFlood flood-color="#c5b89a"/><feComposite in2="ink" operator="in"/></filter></defs></svg>`;

export function renderGoldenCollection({card,cards,panels,showPanel,characterPortrait,openBookTab,openCategory,openTheme,showSpecies,openJournal,openShrine}){
  const model=goldenThemeView(card),index=cards.findIndex(c=>c.id===card.id),pin=uiPreference('pinnedTarget'),pinned=pin?.kind==='collection'&&pin.id===card.id;
  const labels=cards.map(c=>`<button type="button" class="gd-btn2 mini" data-books-open="${c.id}" aria-pressed="${c.id===card.id}">${esc(c.name)}</button>`).join('');
  const known=model.visible.map((k,i)=>sticker(k,characterPortrait,i===0?'with-tape-left':i===3?'with-tape-right':'')).join('');
  const unknown=Array.from({length:model.empty},()=>`<button class="gs-unknown" data-gs-unknown aria-label="尚未收录，查看公开收藏线索">${picture('v2-unknown')}<span aria-hidden="true">?</span><strong>未知</strong></button>`).join('');
  showPanel('收藏册',`${stickerFilter}<header class="gs-header"><span class="gs-title-chick" aria-hidden="true">${characterPortrait(0,0)}</span><h1>图鉴</h1><button class="gs-help gs-directory-toggle" data-gs-directory aria-label="打开图鉴目录与搜索"><img class="gs-search-art" src="/web/art/golden-journey/magnifier.png" alt="" aria-hidden="true"></button><button class="gs-help game-help" data-gs-help aria-label="收藏帮助">?</button></header>
    <nav class="gs-bookmarks" aria-label="图鉴分页"><button data-book-tab="species" aria-pressed="false">品种</button><button data-book-tab="recipes" aria-pressed="false">配方</button><button data-book-tab="collections" aria-pressed="true">收藏</button><button data-book-tab="calendar" aria-pressed="false">日历</button></nav>
    <div class="gs-paper-scroll scroll"><article class="gs-paper" aria-label="${esc(card.name)}">${picture('v2-paper','gs-paper-material')}
      <header class="gs-page-heading"><h3>${esc(card.name)}</h3><button class="gs-count" data-gs-gallery aria-label="查看全部已收录候选，${model.count}种"><strong>${Math.min(model.count,model.target)}</strong><span>/ ${model.target} 种</span>${model.count>model.target?`<small class="gs-count-all">共 ${model.count} ›</small>`:''}</button><button class="gs-pin ${pinned?'is-pinned':''}" data-books-pin aria-pressed="${pinned}" aria-label="${pinned?'取消置顶':'置顶'}${esc(card.name)}">${picture('v2-pin')}<span>${pinned?'已置顶':'置顶'}</span></button></header>
      <div class="gs-grid">${known}${unknown}</div>
      <div class="gs-page-bottom"><div class="gs-stamps">${stamp('收录',model.collectionGranted,'collection',characterPortrait)}${stamp('实践',model.practiceGranted,'practice',characterPortrait)}</div><button class="gs-memento" data-gs-mementos aria-label="查看纪念物">${picture('v2-memento')}<span class="gs-memento-chick" aria-hidden="true">${characterPortrait(0,0)}</span><span>纪念物</span></button>
      <nav class="gs-pager" aria-label="主题翻页"><button data-gs-prev aria-label="上一主题" ${index===0?'disabled':''}>‹</button><span>${index+1} / ${cards.length}</span><button data-gs-next aria-label="下一主题" ${index===cards.length-1?'disabled':''}>›</button></nav></div>
    </article></div>
    <dialog class="gs-dialog" data-gs-dialog aria-labelledby="gs-dialog-title"><header><h3 id="gs-dialog-title" data-overhang></h3><button data-gs-close data-overhang aria-label="关闭详情">×</button></header><div class="gs-dialog-body scroll"></div></dialog>
    <template data-gs-directory-template><div class="gs-directory"><div class="gd-label">翻阅分类</div><div class="gd-row gd-wrap" role="group" aria-label="翻阅分类">${[['theme','主题'],['region','地区'],['special','特殊'],['mementos','纪念物'],['papers','纸页']].map(([v,l])=>`<button type="button" class="gd-btn2 mini" data-books-category="${v}">${l}</button>`).join('')}</div><div class="gd-label">主题</div><div class="gs-theme-list gd-row gd-wrap">${labels}</div><div class="gd-label">更多</div><div class="gd-row gd-wrap"><button type="button" class="gd-btn2 mini" data-gs-search>品种</button><button type="button" class="gd-btn2 mini" data-gs-lore>食材见闻</button><button type="button" class="gd-btn2 mini" data-books-seasonal>四时回礼</button><button type="button" class="gd-btn2 mini" data-books-shrine>签鸡回礼</button></div></div></template>`, 'screen-panel books-screen golden-book');
  const find=q=>panels.querySelector(q),all=q=>panels.querySelectorAll(q);
  const dialog=find('[data-gs-dialog]');let returnFocus=null;
  function close(){dialog.close?.();returnFocus?.focus?.({preventScroll:true});}
  function bindDirectory(){
    all('[data-book-tab]').forEach(b=>b.onclick=()=>{dialog.close?.();openBookTab?.(b.dataset.bookTab);});
    find('[data-gs-search]')?.addEventListener('click',()=>{dialog.close?.();openBookTab?.('species');});
    find('[data-gs-lore]')?.addEventListener('click',()=>{dialog.close?.();openBookTab?.('lore');});
    all('[data-books-open]').forEach(b=>b.onclick=()=>{dialog.close?.();openTheme(b.dataset.booksOpen);});
    all('[data-books-category]').forEach(b=>b.onclick=()=>{dialog.close?.();openCategory(b.dataset.booksCategory);});
    find('[data-books-seasonal]')?.addEventListener('click',()=>openJournal?.('collections'));
    find('[data-books-shrine]')?.addEventListener('click',()=>openShrine?.());
  }
  function sheet(title,html,source){
    returnFocus=source;find('#gs-dialog-title').textContent=title;find('.gs-dialog-body').innerHTML=html;
    dialog.showModal?.();find('[data-gs-close]').focus?.({preventScroll:true});bindDirectory();bindSpecies();
  }
  function bindSpecies(){all('[data-gs-species]').forEach(b=>b.onclick=()=>{dialog.close?.();showSpecies?.(...b.dataset.gsSpecies.split(':').map(Number),()=>openTheme(card.id));});}
  const progress=()=>`<p>${esc(card.text)}</p><ul>${card.stages.map((s,i)=>`<li>${i?'进阶收录':'开页收录'}：${s.current} / ${s.target} 种${s.granted?' · 已入册':s.met?' · 条件已达成，等待入册':' · 未完成'}</li>`).join('')}</ul><p>代表候选${card.representative?'已收录':'尚未收录'}${card.chapters!==null&&card.chapters!==undefined?`；已覆盖 ${card.chapters} 个章节`:''}。</p><p>收录按永久发现计算，卖空仍保留记录。这六个位置展示候选，不是六个固定任务。</p><p>${esc(card.next)}</p>`;
  find('[data-gs-close]').onclick=close;
  dialog.addEventListener?.('cancel',e=>{e.preventDefault();close();});
  find('[data-gs-directory]').onclick=e=>sheet('图鉴目录',find('[data-gs-directory-template]').innerHTML,e.currentTarget);
  find('[data-gs-help]').onclick=e=>sheet('收藏帮助',`<p>翻页查看不同主题；点伙伴看档案，点空纸片看公开线索。点页边书签，可把这本册子设为唯一置顶目标。</p><p>绿色印章表示已入册，浅色印章表示未完成，点印章查看具体条件。收录不会消耗伙伴，阅读不会重复发放奖励。</p><p>“收录”记录发现；“实践”需要真实营业、交付或寻访。备货和阅读不算实践。</p><p>点右上目录可进入见闻、地区、特殊、纪念物与四时、签鸡回礼；点收录数量查看全部已知候选。</p>`,e.currentTarget);
  all('[data-gs-unknown]').forEach(b=>b.onclick=()=>sheet('这一页还可以认识谁',progress(),b));
  all('[data-gs-stamp]').forEach(b=>b.onclick=()=>sheet(b.dataset.gsStamp==='collection'?'收录条件':'实践记录',b.dataset.gsStamp==='collection'?progress():`<p>${esc(card.practice.text||'完成本主题对应的真实营业、交付或寻访记录。')}</p><p>${card.practice.granted?'实践印已入册。':'实践印尚未入册。'}</p>${card.fullMenu?`<p>完整菜单印：${card.fullMenu.granted?'已入册':'未完成'}。同一次营业以「完整菜单」卖齐必要角色、共至少6只；这是单独的印记。</p>`:''}`,b));
  find('[data-gs-gallery]').onclick=e=>sheet(`已收录候选 · ${model.count} 种`,`<div class="gs-gallery">${model.all.map(k=>sticker(k,characterPortrait)).join('')||'<p>还没有收录这一主题的伙伴。</p>'}</div>`,e.currentTarget);
  find('[data-books-pin]').onclick=()=>{setUiPreference('pinnedTarget',pinned?null:{kind:'collection',id:card.id});openTheme(card.id);find('[data-books-pin]')?.focus?.({preventScroll:true});};
  find('[data-gs-mementos]').onclick=()=>openCategory('mementos');
  find('[data-gs-prev]').onclick=()=>{if(index>0)openTheme(cards[index-1].id);};
  find('[data-gs-next]').onclick=()=>{if(index<cards.length-1)openTheme(cards[index+1].id);};
  bindDirectory();bindSpecies();
  find('[data-gs-help]').focus?.({preventScroll:true});
}
