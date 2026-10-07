// 收藏册: theme / region / special pages, mementos with three display slots and
// paper records. Read-only except the display command; unknown species are
// counted, never named.
import {REGIONAL,CONTENT_TEXT,resolveSpecies} from './content-registry.js';
import {speciesDiscovered} from './species-state.js';
import {collectionProgress,setDisplay,ownedMementos,DISPLAY_SLOTS} from './collection-progress.js';
import {readableRequirement} from './regular-model.js';
import {uiPreference,setUiPreference} from './ui-preferences.js';
import {bookNavigation,bindBookNavigation} from './book-ui.js';
import {visualImage,mementoArt,unknownArt} from './visual-assets.js';
import {interfaceIcon} from './ui-icons.js';
import {collectionCover,collectionDirectory,collectionAchievements} from './remaster-view.js';
import {renderGoldenCollection} from './golden-collection-ui.js';
import {kitSheet,kitButton,kitButton2,kitChip,kitLabel} from './ui-kit.js';

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
      stages:p.stages.map(st=>({...st,granted:Object.hasOwn(got,st.reward),rewardName:name(st.reward),requires:CONTENT_TEXT[st.id]?.requires?readableRequirement(s,CONTENT_TEXT[st.id].requires):null})),
      practice:{...p.practice,granted:Object.hasOwn(got,p.practice.reward),text:readableRequirement(s,CONTENT_TEXT[id]?.practice?.any?.join('；或者')??(typeof CONTENT_TEXT[id]?.practice==='string'?CONTENT_TEXT[id].practice:''))},
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

export function createCollectionsUI({getState,commitProgress,showPanel,panels,alertBox,characterPortrait,goBack,openBookTab,openJournal,openShrine,showSpecies}){
  let tab='theme',detail=null;
  const find=q=>panels.querySelector(q),all=q=>panels.querySelectorAll(q);
  const badge=(ok,label)=>`<span class="books-badge ${ok?'is-done':''}">${ok?'✓ ':''}${esc(label)}</span>`;
  // Every 收藏 sub-page: the 图鉴 shell, category pills, one sheet; actions at its foot.
  function shell(body,footer){
    const cats=`<div class="gd-scroll-row bk-cats" role="group" aria-label="翻阅分类" data-hscroll>${TABS.map(([id,label])=>`<button type="button" class="gd-btn2 mini" data-books-category="${id}" aria-pressed="${tab===id&&!detail}">${label}</button>`).join('')}</div>`;
    const more=tab==='theme'&&(openJournal||openShrine)?`<div class="gd-row gd-wrap">${openJournal?kitButton2('四时回礼','data-books-seasonal'):''}${openShrine?kitButton2('签鸡回礼','data-books-shrine'):''}</div>`:'';
    showPanel('收藏册',`${openBookTab?bookNavigation('collections'):''}${kitSheet(`${cats}${body}${more}`,footer??kitButton('返回图鉴','data-books-back'),'bk-sheet books-sheet','',{cls:'books-scroll',attrs:'data-list'})}`,'screen-panel books-screen',{skin:'book',icon:characterPortrait?characterPortrait(0,0):'',back:true});
    if(openBookTab)bindBookNavigation(panels,openBookTab);
    all('[data-books-category]').forEach(b=>b.onclick=()=>{tab=b.dataset.booksCategory;detail=null;render();});
    find('[data-books-seasonal]')?.addEventListener('click',()=>openJournal('collections'));find('[data-books-shrine]')?.addEventListener('click',()=>openShrine());
    find('[data-books-back]')?.addEventListener('click',()=>goBack?.());
    const close=find('.close');if(close)close.onclick=()=>{if(detail){detail=null;render();}else goBack?.();};
  }
  const stamp=(done,label)=>`<span class="bk-stamp ${done?'is-done':''}"><img src="/web/art/golden-collection/${done?'stamp-done':'stamp-pending'}.png" alt=""><b>${esc(label)}</b></span>`;
  const marks=c=>[...c.stages.map((s,j)=>stamp(s.granted,'阶段'+(j+1))),stamp(c.practice.granted,'实践'),...(c.fullMenu?[stamp(c.fullMenu.granted,'菜单')]:[])].join('');
  function open(options={}){tab=options.tab??tab;detail=options.id??null;render();}
  function refresh(){if(find('.books-screen'))render();}
  function render(){const m=collectionsModel(getState());
    const theme=m.theme.find(c=>c.id===detail)??(!detail&&tab==='theme'?m.theme[0]:null);
    if(theme){detail=theme.id;tab='theme';renderGoldenCollection({card:theme,cards:m.theme,panels,showPanel,characterPortrait,openBookTab,openJournal,openShrine,showSpecies,
      openCategory:next=>open({tab:next,id:null}),openTheme:id=>open({tab:'theme',id})});return;}
    if(detail){page(m,detail);return;}
    if(tab==='mementos')mementos(m);else if(tab==='papers')papers(m);else list(m[tab]);}
  function list(cards){
    shell(`<div class="bk-albums">${cards.map((c,i)=>`<button type="button" class="books-card bk-album" data-books-open="${c.id}">${collectionCover(c,i)}<strong>${esc(c.name)}</strong><span class="bk-stamps">${marks(c)}</span><small>收录 ${c.known.length} 种</small></button>`).join('')}</div>`);
    all('[data-books-open]').forEach(b=>b.onclick=()=>{detail=b.dataset.booksOpen;render();});
  }
  function page(m,id){
    const c=[...m.theme,...m.region,...m.special].find(x=>x.id===id),pin=uiPreference('pinnedTarget'),pinned=pin?.kind==='collection'&&pin.id===id;
    const species=c.known.map(k=>`<span class="books-species">${characterPortrait?characterPortrait(k.egg,k.id):''}<small>${esc(k.name)}</small></span>`).join('');
    const stages=c.stages.map((st,i)=>`<li>${badge(st.granted,st.rewardName)} ${st.requires?esc(st.requires):c.kind==='special'?(i?'完成观察页':'开页'):`已收录 ${st.current}/${st.target}${c.chapters!==null&&c.chapters!==undefined?` · 章节 ${c.chapters}`:''}`}${st.met&&!st.granted?' · 下次操作时自动入册':''}</li>`).join('');
    const species2=c.known.map(k=>`<span class="books-species bk-sticker is-known"><span class="bk-sticker-art">${characterPortrait?characterPortrait(k.egg,k.id):''}</span><strong class="collection-name">${esc(k.name)}</strong></span>`).join('');
    shell(`<div class="remaster-collection-page bk-album-page"><header class="books-head bk-album-head">${collectionCover(c,Math.max(0,m.special.findIndex(x=>x.id===id)))}<div><span class="bk-code">${c.kind==='theme'?'主题收藏':c.kind==='region'?'地区册':'特殊发现'}</span><h3>${esc(c.name)}</h3></div></header>
      <div class="bk-stamps big">${marks(c)}</div>
      <details class="gd-more remaster-conditions"><summary>阶段目标与入册说明</summary><p class="books-note">${esc(c.text)}</p><ul class="books-stages">${stages}<li>${badge(c.practice.granted,'实践印')} ${esc(c.practice.text||'需要一次真实营业、交付或寻访记录')}</li>${c.fullMenu?`<li>${badge(c.fullMenu.granted,'完整菜单印')} 同一次营业以「完整菜单」卖齐必要角色、共≥6只</li>`:''}</ul></details>
      ${kitLabel(`已收录的候选 ${c.known.length}`)}<div class="books-grid bk-grid">${species2||'<p class="kp-empty">还没有收录这一页的候选</p>'}</div>
      <div class="gd-row gd-wrap">${c.hidden?kitChip('',`还有 ${c.hidden} 种未收录`,'mini'):''}${c.guests?kitChip('',`客座 ${c.guests} 种`,'mini'):''}${c.footprints!==null&&c.footprints!==undefined?kitChip('',`同行过 ${c.footprints} 种`,'mini'):''}</div>
      ${c.next?`<span class="gd-note books-next">${esc(c.next)}</span>`:''}</div>`,`${kitButton2('列表','data-books-list')}${kitButton2(pinned?'取消置顶':'置顶','data-books-pin')}${kitButton('返回图鉴','data-books-back')}`);
    find('[data-books-list]').onclick=()=>{detail=null;render();};find('[data-books-back]').onclick=()=>goBack?.();
    // One global pin shared with regulars and projects; changing it keeps all progress.
    find('[data-books-pin]').onclick=()=>{setUiPreference('pinnedTarget',pinned?null:{kind:'collection',id});render();};
  }
  let slotOpen=null;
  function mementos(m){
    const owned=m.mementos.filter(x=>x.owned);
    const slots=Array.from({length:DISPLAY_SLOTS},(_,i)=>{const id=m.display[i]&&owned.some(x=>x.id===m.display[i])?m.display[i]:null;return `<button type="button" class="books-slot bk-shelf-slot" data-books-slot="${i}" aria-label="陈列位 ${i+1}：${id?esc(name(id)):'空着'}，点一下更换"><span class="memento-slot-art">${visualImage(id?mementoArt(id):unknownArt,'memento')}</span><b>${id?esc(name(id)):'空着'}</b></button>`;}).join('');
    const chooser=slotOpen===null?'':`<div class="kp-scrim" data-books-chooser-close></div><section class="kp-drawer gd bk-chooser" role="dialog" aria-modal="true" aria-label="陈列位 ${slotOpen+1}"><div class="kp-drawer-title" data-art="plank"><span data-safe><b>陈列位 ${slotOpen+1}</b></span></div><div class="kp-drawer-body"><div class="kp-grid">${owned.map(x=>`<button type="button" class="kp-cell${m.display[slotOpen]===x.id?' on':''}" data-books-pick="${x.id}"><span class="kp-cell-art" data-visual>${visualImage(mementoArt(x.id),'memento')}</span><span class="kp-cell-name">${esc(x.name)}</span></button>`).join('')}</div><div class="gd-actions" data-row>${kitButton2('空着','data-books-pick=""')}${kitButton('好','data-books-chooser-close')}</div></div></section>`;
    shell(`${kitLabel('农场陈列架')}${owned.length?`<div class="memento-display-slots bk-shelf">${slots}</div>`:'<span class="gd-note">完成主题收藏第二阶段或常客最后一段，会收到纪念物</span>'}
      ${kitLabel(`纪念物 ${owned.length}/${m.mementos.length}`)}<div class="books-mementos bk-mementos">${m.mementos.map(x=>`<article class="books-memento bk-memento ${x.owned?'':'is-missing'}"><span class="bk-memento-art">${visualImage(x.owned?mementoArt(x.id):unknownArt,'memento')}</span><strong>${esc(x.name)}</strong><small>${x.owned?esc(x.text):`来自「${esc(x.sourceName)}」`}</small></article>`).join('')}</div>`);
    if(chooser)find('.books-screen').insertAdjacentHTML('beforeend',chooser);
    all('[data-books-slot]').forEach(b=>b.onclick=()=>{slotOpen=Number(b.dataset.booksSlot);render();});
    all('[data-books-chooser-close]').forEach(b=>b.onclick=()=>{slotOpen=null;render();});
    all('[data-books-pick]').forEach(b=>b.onclick=()=>{const value=b.dataset.booksPick||null,slot=slotOpen;slotOpen=null;commitProgress(s=>setDisplay(s,slot,value));render();});
  }
  function papers(m){
    const exhibit=m.exhibit.length?`<section><h4>四地风味展 · 展册画像（${m.exhibit.length}）</h4><div class="books-grid">${m.exhibit.map(k=>`<span class="books-species">${characterPortrait?characterPortrait(k.egg,k.id):''}<small>${esc(k.name)}</small></span>`).join('')}</div></section>`:'';
    shell(`${exhibit}${kitLabel(`纸页与印记 ${m.papers.length}`)}<div class="bk-papers">${m.papers.map(p=>`<div class="bk-paper-row"><img src="/web/art/golden-journey/note.png" alt=""><span><strong>${esc(p.name)}</strong>${p.text?`<small>${esc(p.text)}</small>`:''}</span></div>`).join('')||'<span class="gd-note">完成一笔订单或收录几种伙伴后会出现</span>'}</div>`);
  }
  return {open,refresh};
}
