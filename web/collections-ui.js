// 收藏册: theme / region / special pages, mementos with three display slots and
// paper records. Read-only except the display command; unknown species are
// counted, never named.
import {REGIONAL,CONTENT_TEXT,resolveSpecies} from './content-registry.js';
import {speciesDiscovered} from './species-state.js';
import {collectionProgress,setDisplay,ownedMementos,DISPLAY_SLOTS} from './collection-progress.js';
import {uiPreference,setUiPreference} from './ui-preferences.js';
import {bookNavigation,bindBookNavigation} from './book-ui.js';

const esc=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const TABS=[['theme','主题'],['region','地区'],['special','特殊'],['mementos','纪念物'],['papers','纸页']];
const known=(s,key)=>{const c=resolveSpecies(key);return !!c&&speciesDiscovered(s,c.egg,c.id);};
const name=id=>CONTENT_TEXT[id]?.name??CONTENT_TEXT[id]?.title??rewardName(id);
// Result identities get a readable label; the ledger itself only stores IDs.
function rewardName(id){
  let m;
  if((m=/^PAGE-(COL-\d)$/.exec(id)))return `${CONTENT_TEXT[m[1]]?.name??m[1]} · 插页`;
  if((m=/^PAGE-([VRTB])-(draft|full)$/.exec(id)))return `${CONTENT_TEXT[m[1]]?.name??m[1]}插页（${m[2]==='draft'?'草稿':'完成'}）`;
  if((m=/^BORDER-([VRTB])$/.exec(id)))return `${CONTENT_TEXT[m[1]]?.name??m[1]} · 全收边饰`;
  if((m=/^PAGE-(SP-\w+)$/.exec(id)))return `${CONTENT_TEXT[m[1]]?.name??m[1]} · 观察插页`;
  if((m=/^BORDER-(SP-\w+)$/.exec(id)))return `${CONTENT_TEXT[m[1]]?.name??m[1]} · 观察页边饰`;
  if(/^FULLSTAMP-/.test(id))return '完整菜单印';
  if(/^STAMP-/.test(id))return '实践印记';
  return id;
}

export function collectionsModel(s){
  const got=s.expansion.collections?.entitlements??{};
  const card=id=>{
    const p=collectionProgress(s,id),def=REGIONAL.collections.find(c=>c.id===id)??REGIONAL.specials.find(c=>c.id===id);
    const pool=def.optional?.allowed??[...(def.oldKeys??[]),...(def.newKeys??[])];
    const knownKeys=[...new Set(pool)].filter(k=>known(s,k));
    return {...p,name:name(id),text:CONTENT_TEXT[id]?.text??CONTENT_TEXT[id]?.theme??'',next:CONTENT_TEXT[id]?.next??'',
      stages:p.stages.map(st=>({...st,granted:Object.hasOwn(got,st.reward),rewardName:name(st.reward),requires:CONTENT_TEXT[st.id]?.requires??null})),
      practice:{...p.practice,granted:Object.hasOwn(got,p.practice.reward),text:CONTENT_TEXT[id]?.practice?.any?.join('；')??(typeof CONTENT_TEXT[id]?.practice==='string'?CONTENT_TEXT[id].practice:'')},
      fullMenu:p.fullMenu?{...p.fullMenu,granted:Object.hasOwn(got,p.fullMenu.reward)}:null,
      known:knownKeys.map(k=>({key:k,name:resolveSpecies(k).title_zh_CN,egg:Number(k[0]),id:Number(k.split(':')[1])})),hidden:new Set(pool).size-knownKeys.length};
  };
  const owned=ownedMementos(s);
  return {
    theme:REGIONAL.collections.filter(c=>c.kind==='theme').map(c=>card(c.id)),
    region:REGIONAL.collections.filter(c=>c.kind==='region').map(c=>card(c.id)),
    special:REGIONAL.specials.map(c=>card(c.id)),
    mementos:REGIONAL.mementos.map(m=>({id:m.id,owned:owned.includes(m.id),name:owned.includes(m.id)?name(m.id):'尚未得到',text:owned.includes(m.id)?CONTENT_TEXT[m.id]?.text:null,source:m.source,sourceName:name(m.source)})),
    display:[...(s.expansion.collections?.display??[null,null,null])],
    // PJ-4 exhibition: permanent portraits only (nothing consumed).
    exhibit:(s.expansion.projects?.['PJ-4']?.pinnedChoices?.portraits??[]).map(key=>{const c=resolveSpecies(key);return {key,egg:c.egg,id:c.id,name:c.title_zh_CN};}),
    papers:Object.entries(got).filter(([id])=>!id.startsWith('M')).sort((a,b)=>a[1].seq-b[1].seq).map(([id,v])=>({id,source:v.source,name:name(id),text:CONTENT_TEXT[id]?.text??''})),
  };
}

export function createCollectionsUI({getState,commitProgress,showPanel,panels,alertBox,characterPortrait,goBack,openBookTab,openJournal,openShrine}){
  let tab='theme',detail=null;
  const find=q=>panels.querySelector(q),all=q=>panels.querySelectorAll(q);
  const badge=(ok,label)=>`<span class="books-badge ${ok?'is-done':''}">${ok?'✓ ':''}${esc(label)}</span>`;
  function shell(body,footer){
    showPanel('收藏册',`${openBookTab?bookNavigation('collections'):''}<label class="books-category">翻阅分类<select data-books-category>${TABS.map(([id,label])=>`<option value="${id}" ${tab===id?'selected':''}>${label}</option>`).join('')}</select></label><div class="books-scroll scroll" tabindex="0">${body}${tab==='theme'?`<section class="book-section"><h4>旧册里的回礼</h4>${openJournal?'<button data-books-seasonal>四时收藏与回礼</button>':''}${openShrine?'<button data-books-shrine>签鸡收藏与回礼</button>':''}</section>`:''}</div><footer class="books-footer">${footer??'<button data-books-back>返回图鉴</button>'}</footer>`,'screen-panel books-screen');
    if(openBookTab)bindBookNavigation(panels,openBookTab);
    find('[data-books-category]').onchange=e=>{tab=e.target.value;detail=null;render();};
    find('[data-books-seasonal]')?.addEventListener('click',()=>openJournal('collections'));find('[data-books-shrine]')?.addEventListener('click',()=>openShrine());
    find('[data-books-back]')?.addEventListener('click',()=>goBack?.());
  }
  function open(options={}){tab=options.tab??tab;detail=options.id??null;render();}
  function refresh(){if(find('.books-screen'))render();}
  function render(){const m=collectionsModel(getState());if(detail){page(m,detail);return;}
    if(tab==='mementos')mementos(m);else if(tab==='papers')papers(m);else list(m[tab]);}
  function list(cards){
    shell(cards.map(c=>`<button class="books-card" data-books-open="${c.id}"><strong>${esc(c.name)}</strong><span class="books-row">${c.stages.map(st=>badge(st.granted,st.granted?st.rewardName:`${st.current??'—'}/${st.target??'—'}`)).join('')}${badge(c.practice.granted,'实践印')}${c.fullMenu?badge(c.fullMenu.granted,'完整菜单印'):''}</span><small>${esc(c.next)}</small></button>`).join(''));
    all('[data-books-open]').forEach(b=>b.onclick=()=>{detail=b.dataset.booksOpen;render();});
  }
  function page(m,id){
    const c=[...m.theme,...m.region,...m.special].find(x=>x.id===id),pin=uiPreference('pinnedTarget'),pinned=pin?.kind==='collection'&&pin.id===id;
    const species=c.known.map(k=>`<span class="books-species">${characterPortrait?characterPortrait(k.egg,k.id):''}<small>${esc(k.name)}</small></span>`).join('');
    const stages=c.stages.map((st,i)=>`<li>${badge(st.granted,st.rewardName)} ${st.requires?esc(st.requires):c.kind==='special'?(i?'完成观察页':'开页'):`已收录 ${st.current}/${st.target}${c.chapters!==null&&c.chapters!==undefined?` · 章节 ${c.chapters}`:''}`}${st.met&&!st.granted?' · 下次操作时自动入册':''}</li>`).join('');
    shell(`<header class="books-head"><span class="books-eyebrow">${c.kind==='theme'?'主题收藏':c.kind==='region'?'地区册':'特殊发现'}</span><h3>${esc(c.name)}</h3><p>${esc(c.text)}</p></header>
      <section><h4>阶段成果</h4><ul class="books-stages">${stages}<li>${badge(c.practice.granted,'实践印')} ${esc(c.practice.text||'需要一次真实营业、交付或寻访记录')}</li>${c.fullMenu?`<li>${badge(c.fullMenu.granted,'完整菜单印')} 同一单完整档成交覆盖必要角色、总数≥6</li>`:''}</ul>
      <p class="books-note">收录只看永久发现，卖空仍算；实践只认真实营业、交付或寻访，备货或阅读不计。已入册的成果不发CP，也不会重复。</p></section>
      ${c.footprints!==null&&c.footprints!==undefined?`<p class="books-note">已点亮同行脚步格 ${c.footprints} 种</p>`:''}
      <section><h4>已收录的候选（${c.known.length}）</h4><div class="books-grid">${species||'<p class="books-note">还没有收录这一页的候选。</p>'}</div>${c.hidden?`<p class="books-note">另有 ${c.hidden} 种候选尚未收录。</p>`:''}${c.guests?`<p class="books-note">地区客座 ${c.guests} 种（只展示，不计入章节与数量）。</p>`:''}</section>
      <p class="books-next">${esc(c.next)}</p>`,`<button data-books-list>返回列表</button><button data-books-pin>${pinned?'取消置顶':'置顶'}</button><button data-books-back>返回图鉴</button>`);
    find('[data-books-list]').onclick=()=>{detail=null;render();};find('[data-books-back]').onclick=()=>goBack?.();
    // One global pin shared with regulars and projects; changing it keeps all progress.
    find('[data-books-pin]').onclick=()=>{setUiPreference('pinnedTarget',pinned?null:{kind:'collection',id});render();};
  }
  function mementos(m){
    const owned=m.mementos.filter(x=>x.owned);
    const slots=Array.from({length:DISPLAY_SLOTS},(_,i)=>`<label class="books-slot">陈列位 ${i+1}<select data-books-slot="${i}"><option value="">空着</option>${owned.map(x=>`<option value="${x.id}" ${m.display[i]===x.id?'selected':''}>${esc(x.name)}</option>`).join('')}</select></label>`).join('');
    shell(`<section><h4>农场陈列架</h4><p class="books-note">三个位置只作陈列，没有数值、耐久或出售；替换时旧物仍留在册里。</p>${owned.length?slots:'<p class="books-note">完成主题收藏第二阶段或常客最后一段后，会收到纪念物。</p>'}</section>
      <section><h4>纪念物 ${owned.length}/${m.mementos.length}</h4><div class="books-mementos">${m.mementos.map(x=>`<article class="books-memento ${x.owned?'':'is-missing'}"><strong>${esc(x.name)}</strong><small>${x.owned?esc(x.text):`来自「${esc(x.sourceName)}」`}</small><span class="books-memento-art" aria-hidden="true">概念占位</span></article>`).join('')}</div></section>`);
    all('[data-books-slot]').forEach(select=>select.onchange=()=>{const value=select.value||null;if(commitProgress(s=>setDisplay(s,Number(select.dataset.booksSlot),value))===null)render();else render();});
  }
  function papers(m){
    const exhibit=m.exhibit.length?`<section><h4>四地风味展 · 展册画像（${m.exhibit.length}）</h4><div class="books-grid">${m.exhibit.map(k=>`<span class="books-species">${characterPortrait?characterPortrait(k.egg,k.id):''}<small>${esc(k.name)}</small></span>`).join('')}</div></section>`:'';
    shell(`${exhibit}<section><h4>已入册的纸页与印记（${m.papers.length}）</h4><ul class="books-papers">${m.papers.map(p=>`<li><strong>${esc(p.name)}</strong>${p.text?`<small>${esc(p.text)}</small>`:''}</li>`).join('')||'<li class="books-note">还没有纸页。完成一笔新采购或收录几种伙伴后会出现。</li>'}</ul></section>`);
  }
  return {open,refresh};
}
