import {REGIONAL,CONTENT_TEXT,resolveSpecies,speciesByKey} from './content-registry.js';
import {projectSpecies} from './visibility-model.js';
import {regionalMethodInfo} from './regional-methods.js';
import {materialView,regionView,releasedRegions} from './region-view.js';
import {recipePaths,recipeId,recipePathInfo} from './recipe-book.js';
import {uiPreference,setUiPreference} from './ui-preferences.js';
import {materialArt,unknownArt,visualImage,progressTrack} from './visual-assets.js';
import {interfaceIcon} from './ui-icons.js';
import {bookIndexDoors} from './remaster-view.js';
import {kitTabs,kitSheet,kitButton,kitButton2,kitChip,kitLabel} from './ui-kit.js';

export const BOOK_TABS=[['species','品种'],['recipes','配方'],['collections','收藏'],['calendar','日历']];
// Older routes (overview, lore) stay reachable from links; they are not bookmarks any more.
const BOOK_ROUTES=new Set([...BOOK_TABS.map(([id])=>id),'overview','lore']);
export const bookEscape=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
export const bookNavigation=active=>kitTabs(BOOK_TABS.map(([id,label])=>({label,attrs:`data-book-tab="${id}"`,on:id===active})),'图鉴分页');
export function bindBookNavigation(panels,open){panels.querySelectorAll('[data-book-tab]').forEach(b=>b.onclick=()=>open(b.dataset.bookTab));}

// A preparation filter only evaluates methods the player has permission to see.
export function bookPreparation(s,key,now=Date.now()){
  const c=resolveSpecies(key),view=projectSpecies(s,key);if(!c)return {ready:false,knownMethod:false};
  if(c.pack==='regional'){
    const info=regionalMethodInfo(s,c.recipeId),full=info.full;
    return {knownMethod:full,ready:full&&info.met&&info.recipe.ingredients.every(i=>(s.ingredients[i.id]??0)>=i.quantity)};
  }
  const permitted=recipePaths(key).filter(r=>view.known||s.progress.knowledge.recipes.includes(recipeId(r))).map(r=>recipePathInfo(s,r,now));
  return {knownMethod:permitted.length>0,ready:permitted.some(r=>r.ready)};
}

// The registry owns every relation; this projection only labels destinations.
export function speciesUses(s,key){
  const c=resolveSpecies(key);if(!projectSpecies(s,key)?.known)return [];
  const links=[];
  for(const menu of REGIONAL.menus)if(menu.roles.some(r=>r.allowed.includes(key)))links.push({kind:'menu',id:menu.id,label:`营业 · ${CONTENT_TEXT[menu.id]?.name??menu.id}`});
  for(const order of REGIONAL.orders)if(order.groups.some(g=>g.allowed.includes(key)))links.push({kind:'order',id:order.id,label:`订单 · ${CONTENT_TEXT[order.id]?.name??order.id}`});
  for(const collection of REGIONAL.collections)if((collection.optional?.allowed??collection.oldKeys??[]).includes(key))links.push({kind:'collection',id:collection.id,label:`收藏 · ${CONTENT_TEXT[collection.id]?.name??collection.id}`});
  links.push({kind:'region',id:c.region??({yard:'V',water:'R',wood:'T'})[c.exploration?.environment??c.environment]??'V',label:'寻访 · 同行伙伴'});
  return links;
}

export function bookOverview(s,{pin=null,recent=[]}={}){
  const known=Object.keys(speciesByKey).filter(key=>projectSpecies(s,key).known),directions=[];
  for(const id of releasedRegions()){
    const view=regionView(s,id);if(!view.met)continue;
    const specimen=view.materials.find(m=>m.found&&!m.identified);
    if(specimen)directions.push({kind:'material',id:specimen.id,regionId:id,title:`辨认${specimen.name}`,text:'标本已带回，辨认后开放供货与试做方向。'});
    else{const method=view.methods.find(m=>m.full&&!m.collected&&m.met)??view.methods.find(m=>m.direction&&!m.full);
      if(method)directions.push({kind:'recipe',id:method.recipeId,regionId:id,title:`继续 ${method.code} 的地方做法`,text:method.full?'做法齐了，备齐材料就能去厨房试做。':'在线索册追踪它，到这里寻访会读到下一条线索。'});
      else if(view.counts.cards<6)directions.push({kind:'region',id,regionId:id,title:`再访${view.name}`,text:'选择地点与关注，看看还有哪些记录。'});
    }
    if(directions.length===2)break;
  }
  const pinLabel=pin?(pin.kind==='recipe'?REGIONAL.recipes.find(r=>r.id===pin.id):null):null;
  const pinned=pin?{...pin,label:pinLabel?`${projectSpecies(s,pinLabel.key).code} 的做法`:CONTENT_TEXT[pin.id]?.name??CONTENT_TEXT[pin.id]?.title??'已置顶的目标'}:null;
  return {known:known.length,total:Object.keys(speciesByKey).length,pin:pinned,directions,recent:[...new Set(recent)].filter(k=>known.includes(k)).slice(0,6).map(k=>projectSpecies(s,k))};
}

export function createBookUI({getState,getNow=()=>Date.now(),panels,showPanel,openSpecies,openCollections,openRegion,openRecipeBook,openJournal,openActivities,openTarget,showSpecies,characterPortrait}){
  let tab=uiPreference('bookTab','species'),regionId='V',type='materials';
  let previous=new Set(Object.keys(speciesByKey).filter(k=>projectSpecies(getState(),k).known));
  let note=null;
  const find=q=>panels.querySelector(q),all=q=>panels.querySelectorAll(q),esc=bookEscape;
  function rememberDiscoveries(){
    const current=Object.keys(speciesByKey).filter(k=>projectSpecies(getState(),k).known),added=current.filter(k=>!previous.has(k));
    if(added.length)setUiPreference('bookRecent',[...added,...uiPreference('bookRecent',[])].slice(0,6));
    previous=new Set(current);
  }
  function open(options={}){
    tab=options.tab??tab;if(!BOOK_ROUTES.has(tab))tab='species';if(BOOK_TABS.some(([id])=>id===tab))setUiPreference('bookTab',tab);rememberDiscoveries();
    if(tab==='species'){openSpecies?.();return;}
    if(tab==='recipes'){openRecipeBook?.({fromBook:true});return;}
    if(tab==='calendar'){openJournal?.('calendar',undefined,true);return;}if(tab==='collections'){
      const pin=uiPreference('pinnedTarget'),c=pin?.kind==='collection'?(REGIONAL.collections.find(c=>c.id===pin.id)??REGIONAL.specials.find(c=>c.id===pin.id)):null;
      openCollections?.(c?{tab:c.kind??'special',id:c.id}:{tab:'theme'});return;
    }
    if(tab==='lore')lore();else overview();
  }
  function shell(body){showPanel('鸡宝图鉴',`${bookNavigation(tab)}<div class="book-reader scroll ${tab==='overview'?'remaster-index':''}">${body}</div>`,'screen-panel book-screen');bindBookNavigation(panels,id=>open({tab:id}));}
  function route(target){if(target.kind==='region'||target.kind==='material'||target.kind==='recipe')openRegion?.({regionId:target.regionId??target.id,tab:'record',recipeId:target.kind==='recipe'?target.id:undefined});else openTarget?.(target);}
  function overview(){
    const m=bookOverview(getState(),{pin:uiPreference('pinnedTarget'),recent:uiPreference('bookRecent',[])});
    shell(`<header class="book-intro"><div class="book-cover-art" aria-hidden="true">${interfaceIcon("book")}${characterPortrait?.(0,0)??""}</div><small>厨房里的发现，慢慢记在这里</small><h3>今天翻到哪一页？</h3><p>已收录 ${m.known} / ${m.total} 种</p>${progressTrack(m.known,m.total,"图鉴收录")}</header>${bookIndexDoors()}<section class="book-section"><h4>正在惦记</h4>${m.pin?`<button class="book-path" data-book-pin><strong>${esc(m.pin.label)}</strong><span>继续看看 ›</span></button>`:'<p class="book-muted">还没有置顶目标。遇见喜欢的收藏或故事，再留下书签。</p>'}</section><section class="book-section"><h4>可以继续的一点小事</h4>${m.directions.map((d,i)=>`<button class="book-path" data-book-direction="${i}"><strong>${esc(d.title)}</strong><span>${esc(d.text)}</span></button>`).join('')||'<p class="book-muted">暂时没有新的地区方向。照常做饭、收取伙伴就好。</p>'}</section>${m.recent.length?`<section class="book-section"><h4>最近收录</h4><div class="book-recent">${m.recent.map(c=>`<button data-book-species="${c.key}">${characterPortrait?.(c.egg,c.id)??''}<span>${esc(c.name)}</span><small>${c.code}</small></button>`).join('')}</div></section>`:''}<button class="book-path" data-book-browse>翻阅品种与已知做法 ›</button>`);
    all('[data-index-tab]').forEach(b=>b.onclick=()=>open({tab:b.dataset.indexTab}));
    find('[data-book-pin]')?.addEventListener('click',()=>route(m.pin));all('[data-book-direction]').forEach(b=>b.onclick=()=>route(m.directions[+b.dataset.bookDirection]));
    all('[data-book-species]').forEach(b=>b.onclick=()=>showSpecies?.(...b.dataset.bookSpecies.split(':').map(Number)));
    find('[data-book-browse]').onclick=()=>open({tab:'species'});
  }
  // 食材见闻: pick a region by its painted stall, then its two materials as tiles (tap one for its notes),
  // the region's two counts, and the related books. No dropdowns.
  function lore(){
    const state=getState(),materials=REGIONAL.materials.filter(m=>m.region===regionId).map(m=>materialView(state,m)),view=regionView(state,regionId);
    const CHECK='<img src="/web/art/golden-business/family-check.png" alt="">';
    const stage=m=>m.used?['已入锅','']:m.identified?['可买到','']:m.found?['待辨认','hot']:['未找到','soft'];
    const status=m=>`<span class="sr-only">${m.found?'已找到标本':'尚未找到标本'} · ${m.identified?'已辨认':'未辨认'} · ${m.supplyOpen?'供货已开放':'供货未开放'} · ${m.used?'已用于料理':'尚未用于料理'}</span>`;
    const art=m=>m.found||m.identified?`<img src="${materialArt(m.id)}" alt="">`:'<span class="bk-q" aria-hidden="true">?</span>';
    const regions=`<div class="lr-regions" role="group" aria-label="地区">${releasedRegions().map(id=>`<button type="button" class="lr-region" data-book-region="${id}" aria-pressed="${id===regionId}"><img src="/web/art/golden-journey/node-${id}.png" alt=""><b>${esc(CONTENT_TEXT[id]?.name??id)}</b></button>`).join('')}</div>`;
    const tiles=`<div class="lr-materials">${materials.map(m=>{const [label,cls]=stage(m);return `<button type="button" class="lr-material${m.found||m.identified?'':' is-unknown'}" data-book-note="${m.id}"><span class="lr-material-art">${art(m)}</span><b>${esc(m.name)}</b>${kitChip('',label,`mini ${cls}`)}${status(m)}</button>`;}).join('')}</div>`;
    const counts=`<div class="lr-card"><div class="lr-counts"><span><b>${view.counts.cards}</b><small>/6</small><em>发现记录</em></span><span><b>${view.counts.collected}</b><small>/12</small><em>当地伙伴</em></span></div><div class="gd-row lr-card-actions">${kitButton2('翻记录','data-book-region-record')}${kitButton2('收藏册','data-book-region-collection')}</div></div>`;
    const links=`${kitLabel('也看看')}<div class="jn-links"><button type="button" class="jn-link" data-book-special><span class="jn-link-art">${interfaceIcon('book')}</span><b>特殊发现</b></button><button type="button" class="jn-link" data-book-calendar><img src="/web/art/golden-journey/envelope.png" alt=""><b>伙伴来信</b></button></div>`;
    const m=materials.find(m=>m.id===note);
    const drawer=m?`<div class="kp-scrim" data-book-note-close></div><section class="kp-drawer gd lr-drawer" role="dialog" aria-modal="true" aria-label="${esc(m.name)}"><div class="kp-drawer-title" data-art="plank"><span data-safe><b>${esc(m.name)}</b></span></div><div class="kp-drawer-body">
      <div class="lr-note-head"><span class="lr-material-art big${m.found||m.identified?'':' is-unknown'}">${art(m)}</span><div class="lr-steps" aria-label="材料认识进度">${[[m.found,'标本'],[m.identified,'辨认'],[m.supplyOpen,'供货'],[m.used,'入锅']].map(([done,label])=>`<span class="lr-step${done?' done':''}"><i>${done?CHECK:''}</i><small>${label}</small></span>`).join('')}</div></div>
      ${m.recognition||m.lore?`<div class="lr-notes">${m.recognition?`<p>${esc(m.recognition)}</p>`:''}${m.lore?`<p>${esc(m.lore)}</p>`:''}</div>`:`<span class="gd-note">${m.found?'辨认后能读到更多':'寻访时找到标本，就能记下它'}</span>`}
      <div class="gd-actions" data-row>${kitButton2('关闭','data-book-note-close')}${kitButton(m.found&&!m.identified?'去免费辨认':'看记录',`data-book-material="${m.id}"`)}</div></div></section>`:'';
    showPanel('食材见闻',`${bookNavigation(tab)}${kitSheet(`${regions}${kitLabel(CONTENT_TEXT[regionId]?.name??regionId)}${tiles}${counts}${links}`,'','bk-sheet lr-sheet')}${drawer}`,'screen-panel book-screen lore-screen',{skin:'book',icon:characterPortrait?.(0,0)??'',back:true});
    bindBookNavigation(panels,id=>open({tab:id}));find('.close').onclick=()=>open({tab:'collections'});
    all('[data-book-region]').forEach(b=>b.onclick=()=>{regionId=b.dataset.bookRegion;note=null;lore();});
    all('[data-book-note]').forEach(b=>b.onclick=()=>{note=+b.dataset.bookNote;lore();});
    all('[data-book-note-close]').forEach(b=>b.onclick=()=>{note=null;lore();});
    all('[data-book-material]').forEach(b=>b.onclick=()=>{note=null;openRegion?.({regionId,tab:'record',materialId:+b.dataset.bookMaterial});});
    find('[data-book-region-record]')?.addEventListener('click',()=>openRegion?.({regionId,tab:'record'}));find('[data-book-region-collection]')?.addEventListener('click',()=>openCollections?.({tab:'region',id:`COL-${regionId}`}));
    find('[data-book-special]')?.addEventListener('click',()=>openCollections?.({tab:'special'}));find('[data-book-calendar]')?.addEventListener('click',()=>openActivities?.());
  }
  function refresh(){rememberDiscoveries();if(find('.book-screen')){const scroll=find('.book-reader')?.scrollTop??0;open({tab});if(find('.book-reader'))find('.book-reader').scrollTop=scroll;}}
  return {open,refresh};
}
