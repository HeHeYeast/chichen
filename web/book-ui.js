import {REGIONAL,CONTENT_TEXT,resolveSpecies,speciesByKey} from './content-registry.js';
import {projectSpecies} from './visibility-model.js';
import {regionalMethodInfo} from './regional-methods.js';
import {materialView,regionView,releasedRegions} from './region-view.js';
import {recipePaths,recipeId,recipePathInfo} from './recipe-book.js';
import {uiPreference,setUiPreference} from './ui-preferences.js';

export const BOOK_TABS=[['overview','总览'],['species','品种'],['collections','收藏'],['lore','见闻']];
export const bookEscape=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
export const bookNavigation=active=>`<nav class="book-navigation" aria-label="图鉴分页">${BOOK_TABS.map(([id,label])=>`<button type="button" data-book-tab="${id}" aria-pressed="${id===active}">${label}</button>`).join('')}</nav>`;
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
  for(const order of REGIONAL.orders)if(order.groups.some(g=>g.allowed.includes(key)))links.push({kind:'order',id:order.id,label:`采购 · ${CONTENT_TEXT[order.id]?.name??order.id}`});
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
      if(method)directions.push({kind:'recipe',id:method.recipeId,regionId:id,title:`继续 ${method.code} 的地方做法`,text:method.full?'方法已记好，备齐材料就能去厨房试做。':'带着这份方向完成寻访，免费补全方法。'});
      else if(view.counts.cards<6)directions.push({kind:'region',id,regionId:id,title:`再访${view.name}`,text:'选择地点与关注，看看还有哪些记录。'});
    }
    if(directions.length===2)break;
  }
  const pinLabel=pin?(pin.kind==='recipe'?REGIONAL.recipes.find(r=>r.id===pin.id):null):null;
  const pinned=pin?{...pin,label:pinLabel?`${projectSpecies(s,pinLabel.key).code} 的做法`:CONTENT_TEXT[pin.id]?.name??CONTENT_TEXT[pin.id]?.title??'已置顶的目标'}:null;
  return {known:known.length,total:Object.keys(speciesByKey).length,pin:pinned,directions,recent:[...new Set(recent)].filter(k=>known.includes(k)).slice(0,6).map(k=>projectSpecies(s,k))};
}

export function createBookUI({getState,getNow=()=>Date.now(),panels,showPanel,openSpecies,openCollections,openRegion,openRecipeBook,openJournal,openActivities,openTarget,showSpecies,characterPortrait}){
  let tab=uiPreference('bookTab','overview'),regionId='V',type='materials';
  let previous=new Set(Object.keys(speciesByKey).filter(k=>projectSpecies(getState(),k).known));
  const find=q=>panels.querySelector(q),all=q=>panels.querySelectorAll(q),esc=bookEscape;
  function rememberDiscoveries(){
    const current=Object.keys(speciesByKey).filter(k=>projectSpecies(getState(),k).known),added=current.filter(k=>!previous.has(k));
    if(added.length)setUiPreference('bookRecent',[...added,...uiPreference('bookRecent',[])].slice(0,6));
    previous=new Set(current);
  }
  function open(options={}){
    tab=options.tab??tab;if(!BOOK_TABS.some(([id])=>id===tab))tab='overview';setUiPreference('bookTab',tab);rememberDiscoveries();
    if(tab==='species'){openSpecies?.();return;}if(tab==='collections'){
      const pin=uiPreference('pinnedTarget'),c=pin?.kind==='collection'?(REGIONAL.collections.find(c=>c.id===pin.id)??REGIONAL.specials.find(c=>c.id===pin.id)):null;
      openCollections?.(c?{tab:c.kind??'special',id:c.id}:{tab:'theme'});return;
    }
    if(tab==='lore')lore();else overview();
  }
  function shell(body){showPanel('鸡宝图鉴',`${bookNavigation(tab)}<div class="book-reader scroll">${body}</div>`,'screen-panel book-screen');bindBookNavigation(panels,id=>open({tab:id}));}
  function route(target){if(target.kind==='region'||target.kind==='material'||target.kind==='recipe')openRegion?.({regionId:target.regionId??target.id,tab:'record',recipeId:target.kind==='recipe'?target.id:undefined});else openTarget?.(target);}
  function overview(){
    const m=bookOverview(getState(),{pin:uiPreference('pinnedTarget'),recent:uiPreference('bookRecent',[])});
    shell(`<header class="book-intro"><small>厨房里的发现，慢慢记在这里</small><h3>今天翻到哪一页？</h3><p>已收录 ${m.known} / ${m.total} 种</p></header><section class="book-section"><h4>正在惦记</h4>${m.pin?`<button class="book-path" data-book-pin><strong>${esc(m.pin.label)}</strong><span>继续看看 ›</span></button>`:'<p class="book-muted">还没有置顶目标。遇见喜欢的收藏或故事，再留下书签。</p>'}</section><section class="book-section"><h4>可以继续的一点小事</h4>${m.directions.map((d,i)=>`<button class="book-path" data-book-direction="${i}"><strong>${esc(d.title)}</strong><span>${esc(d.text)}</span></button>`).join('')||'<p class="book-muted">暂时没有新的地区方向。照常做饭、收取伙伴就好。</p>'}</section>${m.recent.length?`<section class="book-section"><h4>最近收录</h4><div class="book-recent">${m.recent.map(c=>`<button data-book-species="${c.key}">${characterPortrait?.(c.egg,c.id)??''}<span>${esc(c.name)}</span><small>${c.code}</small></button>`).join('')}</div></section>`:''}<button class="book-path" data-book-browse>翻阅品种与已知做法 ›</button>`);
    find('[data-book-pin]')?.addEventListener('click',()=>route(m.pin));all('[data-book-direction]').forEach(b=>b.onclick=()=>route(m.directions[+b.dataset.bookDirection]));
    all('[data-book-species]').forEach(b=>b.onclick=()=>showSpecies?.(...b.dataset.bookSpecies.split(':').map(Number)));
    find('[data-book-browse]').onclick=()=>open({tab:'species'});
  }
  function lore(){
    const state=getState(),materials=REGIONAL.materials.filter(m=>m.region===regionId).map(m=>materialView(state,m));
    const view=regionView(state,regionId);
    shell(`<div class="book-filters"><label>地区<select data-book-region>${releasedRegions().map(id=>`<option value="${id}" ${id===regionId?'selected':''}>${esc(CONTENT_TEXT[id]?.name??id)}</option>`).join('')}</select></label><label>内容<select data-book-lore-type><option value="materials" ${type==='materials'?'selected':''}>食材见闻</option><option value="region" ${type==='region'?'selected':''}>地区与发现</option><option value="special" ${type==='special'?'selected':''}>特殊发现</option><option value="calendar" ${type==='calendar'?'selected':''}>寻宝日历</option></select></label></div>${type==='materials'?materials.map(m=>`<article class="book-material"><small>${esc(CONTENT_TEXT[m.specimenCard]?.title??m.specimenCard)}</small><h3>${esc(m.name)}</h3><p>${m.found?'已找到标本':'尚未找到标本'} · ${m.identified?'已辨认':'未辨认'}<br>${m.supplyOpen?'供货已开放':'供货未开放'} · ${m.used?'已用于料理':'尚未用于料理'}</p>${m.recognition?`<p>${esc(m.recognition)}</p>`:''}${m.lore?`<p>${esc(m.lore)}</p>`:''}<button data-book-material="${m.id}">${m.found&&!m.identified?'去免费辨认':'查看关联记录与方向'}</button></article>`).join(''):type==='region'?`<section class="book-section"><h3>${esc(view.name)}</h3><p>发现记录 ${view.counts.cards}/6 · 当地伙伴 ${view.counts.collected}/12</p><button data-book-region-record>翻阅发现与试做方向</button><button data-book-region-collection>打开地区收藏册</button></section>`:type==='special'?'<section class="book-section"><h3>把有趣的发现放在一起</h3><p>同行脚步、叶形风味、鸡鸭同桌与特别造型，各有一页记录。</p><button data-book-special>翻阅特殊发现</button></section>':'<section class="book-section"><h3>日历里的来信</h3><p>查看时令线索和已开放的旧活动；回礼仍在原处领取。</p><button data-book-calendar>查看寻宝日历</button></section>'}`);
    find('[data-book-region]').onchange=e=>{regionId=e.target.value;lore();};find('[data-book-lore-type]').onchange=e=>{type=e.target.value;lore();};
    all('[data-book-material]').forEach(b=>b.onclick=()=>openRegion?.({regionId,tab:'record',materialId:+b.dataset.bookMaterial}));
    find('[data-book-region-record]')?.addEventListener('click',()=>openRegion?.({regionId,tab:'record'}));find('[data-book-region-collection]')?.addEventListener('click',()=>openCollections?.({tab:'region',id:`COL-${regionId}`}));
    find('[data-book-special]')?.addEventListener('click',()=>openCollections?.({tab:'special'}));find('[data-book-calendar]')?.addEventListener('click',()=>openActivities?.());
  }
  function refresh(){rememberDiscoveries();if(find('.book-screen')){const scroll=find('.book-reader')?.scrollTop??0;open({tab});if(find('.book-reader'))find('.book-reader').scrollTop=scroll;}}
  return {open,refresh};
}
