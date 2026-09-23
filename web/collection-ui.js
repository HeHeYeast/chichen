import {SPECIES_DESCRIPTIONS as DESCRIPTIONS,SPECIES_ABILITIES as ABILITIES,resolveSpecies} from './content-registry.js';
import {availableCount,reservedCount,inventoryView} from './inventory.js';
import {bookNavigation,bindBookNavigation,bookPreparation,speciesUses} from './book-ui.js';
import {storyOrders} from './story-orders.js';
import {basketQuote,effects} from './progression.js';
import {GAME_DATA as DATA,expansionRecipeHints} from './content-pack.js';
import * as E from './engine.js';
import {speciesLabel} from './catalog.js';
import {seasonalCharacter,seasonalRecipeInfo} from './seasonal-pack.js';
import {characterAccessInfo} from './legacy-activities.js';
import {discoveredRecipe} from './recipe-book.js';
import {discoveryClue} from './discovery-clues.js';
import {projectSpecies} from './visibility-model.js';

const PAGE_SIZE=9;
const escapeText=value=>String(value??'').replace(/[&<>"']/g,char=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[char]));
const count=value=>Number.isFinite(Number(value))?Math.max(0,Math.trunc(Number(value))):0;
const number=value=>count(value).toLocaleString('zh-CN');

// Unknown entries intentionally contain neither the species name nor its price.
export function speciesView(state,egg,id){
  const character=E.char(egg,id);
  if(!character)return null;
  const key=E.key(egg,id),stock=availableCount(state,key),held=count(state.farm[key]),away=reservedCount(state,key),total=count(state.total[key]),known=total>0||held>0;
  return {...projectSpecies(state,key),held,away,stock:known?stock:0,total:known?total:0};
}

export function collectionPageModel(state,{egg=0,page=0,knownOnly=false,search='',readyOnly=false,now=Date.now()}={}){
  egg=egg===1?1:0;
  const all=DATA.characters[egg].map(character=>speciesView(state,egg,character.id));
  const needle=String(search).trim().toLocaleLowerCase();
  const discovered=all.filter(entry=>entry.known).length,list=all.filter(entry=>(!knownOnly||entry.known)&&(!needle||entry.code.toLowerCase().includes(needle)||(entry.known&&entry.name.toLocaleLowerCase().includes(needle)))&&(!readyOnly||bookPreparation(state,entry.key,now).ready));
  const pages=Math.max(1,Math.ceil(list.length/PAGE_SIZE)),currentPage=Math.min(pages-1,count(page));
  return {egg,page:currentPage,pages,discovered,total:all.length,entries:list.slice(currentPage*PAGE_SIZE,(currentPage+1)*PAGE_SIZE)};
}

export function saleSelectionSummary(state,selection={},options={}){
  const normalized={};let quantity=0,income=0;
  for(const [key,value]of Object.entries(selection)){
    if(!/^[01]:\d+$/.test(key))continue;
    const [egg,id]=key.split(':').map(Number),entry=speciesView(state,egg,id);
    if(!entry?.known)continue;
    const chosen=Math.min(entry.stock,count(value));
    if(!chosen)continue;
    normalized[key]=chosen;quantity+=chosen;income+=chosen*entry.price;
  }
  const quote=basketQuote(state,normalized,options);
  return {selection:normalized,quantity,income:quote.income,...quote};
}

export function harvestEntries(state,egg=0,stockOnly=true){
  return DATA.characters[egg===1?1:0].map(character=>speciesView(state,egg===1?1:0,character.id)).filter(entry=>entry.known&&(!stockOnly||entry.stock>0));
}

// A batch shortcut always covers both ledgers, including rows outside the viewport.
export function bulkSaleSelection(state,keepOne=false){
  const selection={};
  for(const egg of [0,1])for(const entry of harvestEntries(state,egg)){
    const quantity=Math.max(0,entry.stock-(keepOne?1:0));
    if(quantity)selection[entry.key]=quantity;
  }
  return selection;
}

export function createCollectionUI({getState,getPage,getNow=()=>Date.now(),panels,showPanel,confirmBox,alertBox,act,sound,characterPortrait,makeWalkers,changePage,openActivities,openDuckShop,openJournal,openRecipeBook,openObservation,openWorkshop,openBooks,openBookTab,openInventory,prepareRecipe,openUse,skillFeedback=()=>{}}){
  let bookEgg=0,harvestEgg=0,knownOnly=false,stockOnly=true,selection={},keepOneSelection=false,useRewards=true;
  let refreshDetail=null;
  let search='',readyOnly=false,inventoryKey=null;
  const bookPages=[0,0],harvestScroll=[0,0],detailQuantities=new Map();
  const find=selector=>panels.querySelector(selector);
  const each=(selector,handler)=>panels.querySelectorAll(selector).forEach(handler);
  const eggPlaceholder=()=>'<span class="collection-unknown-egg" aria-hidden="true"><span>?</span></span>';
  const portrait=entry=>entry.known?characterPortrait(entry.egg,entry.id):eggPlaceholder();
  function saleEffects(quote){
    const e=effects(getState()),events=[];
    if(quote.markup)events.push({id:'TRADE-2',text:'招牌加价 +'+quote.markup+' CP'});
    if(quote.baskets)events.push({id:'TRADE-3',text:quote.baskets+'次整筐 · 额外 +'+quote.baskets*e.basketBonus+' CP'});
    if(quote.platters)events.push({id:'TRADE-4',text:quote.platters+'次拼盘 · 额外 +'+quote.platters*e.platterBonus+' CP'});
    if(events.length)skillFeedback(events);
  }
  const tabs=(scope,egg)=>`<div class="${scope}-tabs" aria-label="品种类别">${[0,1].map(value=>`<button type="button" data-${scope}-egg="${value}" aria-pressed="${value===egg}" class="${value===egg?'is-active':''}"><span class="${scope}-tab-egg ${value?'is-duck':''}" aria-hidden="true"></span>${value?'鸭宝':'鸡宝'}</button>`).join('')}</div>`;
  function rememberHarvestScroll(){const rows=find('.harvest-list');if(rows)harvestScroll[harvestEgg]=rows.scrollTop;}
  function openAlbum(){if(getPage()===4)renderCollection();else renderAlbum();}

  function renderCollection(){
    refreshDetail=null;
    const model=collectionPageModel(getState(),{egg:bookEgg,page:bookPages[bookEgg],knownOnly,search,readyOnly,now:getNow()});
    bookPages[bookEgg]=model.page;
    const cards=model.entries.map(entry=>`<button type="button" class="collection-stamp ${entry.known?'is-known':'is-unknown'}" data-collection-card="${entry.id}" aria-label="${escapeText(entry.known?entry.code+' '+entry.name:'未发现品种 '+entry.code)}"><span class="collection-code">${entry.code}</span><span class="collection-picture">${portrait(entry)}</span><strong class="collection-name">${escapeText(entry.name)}</strong><span class="collection-stock">${entry.known?'在家可用 '+number(entry.stock):bookPreparation(getState(),entry.key,getNow()).knownMethod?'已知方法 · 未收录':'等待发现'}</span></button>`).join('');
    const empty='<div class="collection-empty">'+eggPlaceholder()+'<strong>这一册还没有新伙伴</strong><p>去厨房孵化并收取，<br>就能留下第一枚收藏印记。</p><button type="button" data-collection-kitchen>去厨房</button></div>';
    showPanel('鸡宝图鉴',`${openBookTab?bookNavigation('species'):''}${tabs('collection',bookEgg)}<form class="book-search" data-book-search-form><label>名称或编号<input type="search" data-book-search value="${escapeText(search)}" placeholder="已知名称 / C001 / D001" autocomplete="off"></label><button type="submit">查找</button></form><div class="collection-progress"><span>已收录 <strong>${model.discovered}</strong> / ${model.total}</span>${openRecipeBook?'<button class="journal-entry" data-collection-recipes>配方册 ›</button>':''}<button type="button" data-collection-filter aria-pressed="${knownOnly}">${knownOnly?'查看全部':'只看已收录'}</button><button type="button" data-collection-ready aria-pressed="${readyOnly}">${readyOnly?'取消可准备':'可准备'}</button></div><div class="collection-book scroll ${model.entries.length?'':'is-empty'}" aria-label="${bookEgg?'鸭宝':'鸡宝'}图鉴，第 ${model.page+1} 页">${model.entries.length?cards+Array.from({length:PAGE_SIZE-model.entries.length},()=>'<span class="collection-blank" aria-hidden="true"></span>').join(''):search||readyOnly?'<div class="collection-empty"><strong>没有符合条件的记录</strong><p>可以换个编号，或放宽筛选再找找。</p><button data-collection-clear>清除搜索与筛选</button></div>':empty}</div><footer class="collection-pager"><button type="button" class="cream" data-collection-prev aria-label="图鉴上一页" ${model.page===0?'disabled':''}>‹</button><span>第 <strong>${model.page+1}</strong> / ${model.pages} 页</span><button type="button" class="cream" data-collection-next aria-label="图鉴下一页" ${model.page>=model.pages-1?'disabled':''}>›</button></footer>`,'screen-panel collection-screen');
    if(openBookTab)bindBookNavigation(panels,openBookTab);
    find('[data-book-search]').oninput=e=>{search=e.target.value;};
    find('[data-book-search-form]').onsubmit=e=>{e.preventDefault();search=find('[data-book-search]').value;bookPages.fill(0);renderCollection();};
    find('[data-collection-ready]').onclick=()=>{readyOnly=!readyOnly;bookPages.fill(0);renderCollection();};
    const clear=find('[data-collection-clear]');if(clear)clear.onclick=()=>{search='';readyOnly=false;knownOnly=false;bookPages.fill(0);renderCollection();};
    find('[data-collection-workshop]')?.addEventListener('click',()=>openWorkshop());
    find('[data-collection-recipes]')?.addEventListener('click',()=>openRecipeBook());
    find('[data-collection-books]')?.addEventListener('click',()=>openBooks());
    each('[data-collection-egg]',button=>button.onclick=()=>{bookEgg=+button.dataset.collectionEgg;sound(3);renderCollection();});
    find('[data-collection-filter]').onclick=()=>{knownOnly=!knownOnly;bookPages.fill(0);renderCollection();};
    find('[data-collection-prev]').onclick=()=>{bookPages[bookEgg]=Math.max(0,bookPages[bookEgg]-1);renderCollection();};
    find('[data-collection-next]').onclick=()=>{bookPages[bookEgg]=Math.min(model.pages-1,bookPages[bookEgg]+1);renderCollection();};
    each('[data-collection-card]',button=>button.onclick=()=>{
      const entry=speciesView(getState(),bookEgg,+button.dataset.collectionCard);
      if(entry?.known)showCharacter(bookEgg,entry.id);
      else if(entry)showUnknown(bookEgg,entry.id);
    });
    const kitchen=find('[data-collection-kitchen]');if(kitchen)kitchen.onclick=()=>changePage(0);
  }

  function accessMarkup(egg,id,isKnown){
    const state=getState(),access=characterAccessInfo(egg,id,state,getNow());
    const duckLocked=egg===1&&!state.duck;
    if(!isKnown)return `<div class="species-access"><strong>配方尚未解锁</strong><p>首次孵化并收取这位伙伴后，完整配方会记入图鉴。</p>${access?.activityId?'<p>也许神社的来信里，还有一些消息。</p>':''}${access?.holiday?`<p class="species-clue-calendar">${escapeText(access.holiday.title)} · ${escapeText(access.holiday.dateRange)}<br>${escapeText(access.holiday.status)}，以开火日期为准。</p>`:''}${duckLocked?'<p>需要先在商店开放鸭蛋。</p>':''}${duckLocked&&openDuckShop?'<button data-species-duck>去商店开放鸭蛋</button>':access?.activityId&&openActivities?`<button data-species-activity="${access.activityId}">去看看相关来信</button>`:''}</div>`;
    const seasonal=seasonalCharacter(egg,id);
    if(seasonal){const r=seasonalRecipeInfo(state,seasonal.key);return `<div class="species-access"><strong>四时食谱 · 常驻手作</strong>${escapeText(r.toolName)} Lv.${r.minLevel+1} · ${escapeText(r.ingredientNames.join(' ＋ '))}<p>先在四时食谱选好配方，再开火；全年都能获得。${duckLocked?'需先开放鸭蛋。':''}</p><button data-species-seasonal="${seasonal.key}">查看这份配方</button></div>`;}
    const hint=access?`${access.kind==='campaign'?(access.unlocked?'配方已开放。':`完成「${access.title}」后开放配方。`):access.kind==='gift'?`在「${access.title}」领取调理赠品。`:''}${access.recipeText}`:'在厨房尝试不同的厨具和调味料，孵化后记得收取。';
    const text=isKnown&&access?access.text:hint;
    const recipe=egg===0?expansionRecipeHints(state).find(recipe=>recipe.id===id):null;
    return `<div class="species-access"><strong>${access?'寻找伙伴的线索':recipe?'竹笼点心坊':'发现小提示'}</strong>${escapeText(recipe?`竹蒸笼 Lv.${recipe.requiredLevel} · ${recipe.hint}`:text)}${duckLocked?'<p>先到商店开放鸭蛋，之后在厨房切换蛋种。</p>':''}${duckLocked&&openDuckShop?'<button data-species-duck>去商店开放鸭蛋</button>':access?.activityId&&openActivities?`<button data-species-activity="${access.activityId}">查看${escapeText(access.title)}</button>`:''}</div>`;
  }
  function bindAccess(){
    find('[data-species-seasonal]')?.addEventListener('click',e=>openJournal?.('recipes',e.currentTarget.dataset.speciesSeasonal));
    find('[data-species-activity]')?.addEventListener('click',event=>openActivities?.(event.currentTarget.dataset.speciesActivity));
    find('[data-species-duck]')?.addEventListener('click',()=>openDuckShop?.());
  }
  function showUnknown(egg,id){
    const c=resolveSpecies(`${egg}:${id}`);
    if(c?.pack==='regional'&&openUse){openUse({kind:'region',id:c.region,recipeId:c.recipeId});return;}
    if(openObservation){openObservation(egg+':'+id);return;}
    const entry=speciesView(getState(),egg,id);if(!entry)return;
    if(entry.known){showCharacter(egg,id);return;}
    refreshDetail=()=>showUnknown(egg,id);
    const clue=discoveryClue(getState(),egg,id);
    const clueCard=clue?`<section class="species-discovery-clue" aria-labelledby="discovery-clue-title"><h4 id="discovery-clue-title">发现小线索</h4><p>${escapeText(clue.text)}</p><small>循着一点线索，试着认识它。</small></section>`:'';
    showPanel('未发现的伙伴',`<div class="scroll species-sheet"><div class="species-topline"><span>${entry.code}</span><span>等待收录</span></div><div class="species-portrait">${eggPlaceholder()}</div><h3 class="species-name">这里会住着谁呢？</h3>${clueCard}${accessMarkup(egg,id,false)}</div><footer class="species-footer"><button class="species-back" data-unknown-back>‹ 返回图鉴</button><button class="orange" data-unknown-kitchen>去厨房</button></footer>`,'screen-panel species-screen species-unknown-screen');
    find('.close').onclick=renderCollection;find('[data-unknown-back]').onclick=renderCollection;find('[data-unknown-kitchen]').onclick=()=>changePage(0);bindAccess();
  }

  function harvestRow(entry){
    const qty=selection[entry.key]??0;
    return `<div class="harvest-row" data-harvest-row="${entry.key}"><button type="button" class="harvest-portrait" data-harvest-character="${entry.id}" aria-label="查看${escapeText(entry.name)}详情">${characterPortrait(entry.egg,entry.id)}</button><div class="harvest-entry"><span class="harvest-code">${entry.code}</span><strong class="harvest-name">${escapeText(entry.name)}</strong><div class="harvest-meta"><span>售价 <b>${number(entry.price)}</b> CP</span><span>库存 <b>${number(entry.stock)}</b></span></div><div class="harvest-quantity"><button type="button" data-harvest-minus="${entry.key}" aria-label="减少${escapeText(entry.name)}卖出数量" ${qty?'':'disabled'}>−</button><output data-harvest-quantity="${entry.key}" aria-label="${escapeText(entry.name)}已选数量">${number(qty)}</output><button type="button" data-harvest-plus="${entry.key}" aria-label="增加${escapeText(entry.name)}卖出数量" ${qty<entry.stock?'':'disabled'}>+</button><button type="button" class="harvest-max" data-harvest-max="${entry.key}" aria-label="选择全部${escapeText(entry.name)}" ${entry.stock&&qty<entry.stock?'':'disabled'}>最大</button></div></div></div>`;
  }
  function updateHarvestSelection(){
    if(keepOneSelection)for(const key of Object.keys(selection))selection[key]=Math.min(selection[key],Math.max(0,availableCount(getState(),key)-1));
    const summary=saleSelectionSummary(getState(),selection,{useRewards});selection=summary.selection;
    each('[data-harvest-row]',row=>{
      const key=row.dataset.harvestRow,stock=availableCount(getState(),key),qty=selection[key]??0;
      row.querySelector('[data-harvest-quantity]').textContent=number(qty);
      row.querySelector('[data-harvest-minus]').disabled=qty===0;
      row.querySelector('[data-harvest-plus]').disabled=qty>=stock;
      row.querySelector('[data-harvest-max]').disabled=stock===0||qty>=stock;
      row.classList.toggle('is-selected',qty>0);
    });
    const income=find('[data-harvest-income]'),quantity=find('[data-harvest-total]'),sell=find('[data-harvest-sell]'),clear=find('[data-harvest-clear]');
    if(income)income.textContent=number(summary.income);const breakdown=find('[data-sale-breakdown]');if(breakdown)breakdown.textContent=`货款 ${summary.baseIncome} CP · 招牌 +${summary.markup} · 经营 +${summary.bonus}`;
    if(quantity)quantity.textContent=number(summary.quantity);
    if(sell)sell.disabled=summary.quantity===0;
    if(clear)clear.disabled=summary.quantity===0;
    const all=find('[data-harvest-all]'),keep=find('[data-harvest-keep-one]');
    if(all)all.disabled=Object.keys(bulkSaleSelection(getState())).length===0;
    if(keep)keep.disabled=Object.keys(bulkSaleSelection(getState(),true)).length===0;
  }
  function choose(key,value){keepOneSelection=false;selection[key]=Math.min(availableCount(getState(),key),count(value));updateHarvestSelection();}

  function renderAlbum(){
    refreshDetail=null;
    selection=saleSelectionSummary(getState(),selection,{useRewards}).selection;
    const entries=harvestEntries(getState(),harvestEgg,stockOnly).filter(e=>!inventoryKey||e.key===inventoryKey),known=harvestEntries(getState(),harvestEgg,false).length;
    const empty=`<div class="harvest-empty">${eggPlaceholder()}<strong>${known?'暂时没有可出售的伙伴':'还没有收获记录'}</strong><p>${known?'已收录的品种仍保存在图鉴中。':'在厨房孵化并收取后，<br>伙伴就会来到农场。'}</p><button type="button" data-harvest-kitchen>去厨房</button></div>`;
    showPanel('农场收成表',`${tabs('harvest',harvestEgg)}${inventoryKey?`<div class="harvest-filter"><span>正在管理 ${escapeText(speciesView(getState(),...inventoryKey.split(':').map(Number)).name)}</span><button data-harvest-unfilter>查看全部库存</button></div>`:''}<div class="harvest-filter"><span>点头像查看档案</span><button type="button" data-harvest-filter aria-pressed="${stockOnly}">${stockOnly?'仅看有库存':'全部已发现'}</button></div><div class="harvest-bulk"><span>外出伙伴不可售 · 鸡鸭一起选</span><button type="button" data-harvest-all>全选</button><button type="button" data-harvest-keep-one>每种留一只</button><button type="button" data-harvest-orders>为采购留货</button></div><div class="harvest-list scroll" aria-label="${harvestEgg?'鸭宝':'鸡宝'}收成">${entries.length?entries.map(harvestRow).join(''):empty}</div><footer class="harvest-footer"><div class="harvest-total"><small>合计 <strong data-harvest-total>0</strong> 只（含鸡鸭）</small><span><strong data-harvest-income>0</strong> CP</span></div><p class="sale-breakdown" data-sale-breakdown></p><label class="sale-rewards"><input type="checkbox" data-use-rewards ${useRewards?'checked':''}>使用经营奖励（整筐优先，再拼盘）</label><div class="harvest-checkout"><button type="button" class="orange" data-harvest-sell>确认出售</button><button type="button" data-harvest-clear>清空选择</button></div></footer>`,'screen-panel harvest-screen');
    const unfilter=find('[data-harvest-unfilter]');if(unfilter)unfilter.onclick=()=>{inventoryKey=null;renderAlbum();};
    const list=find('.harvest-list');list.scrollTop=harvestScroll[harvestEgg];list.onscroll=()=>{harvestScroll[harvestEgg]=list.scrollTop;};
    each('[data-harvest-egg]',button=>button.onclick=()=>{rememberHarvestScroll();inventoryKey=null;harvestEgg=+button.dataset.harvestEgg;renderAlbum();});
    find('[data-harvest-filter]').onclick=()=>{stockOnly=!stockOnly;harvestScroll.fill(0);renderAlbum();};
    each('[data-harvest-minus]',button=>button.onclick=()=>{const key=button.dataset.harvestMinus;choose(key,(selection[key]??0)-1);});
    each('[data-harvest-plus]',button=>button.onclick=()=>{const key=button.dataset.harvestPlus;choose(key,(selection[key]??0)+1);});
    each('[data-harvest-max]',button=>button.onclick=()=>choose(button.dataset.harvestMax,getState().farm[button.dataset.harvestMax]));
    each('[data-harvest-character]',button=>button.onclick=()=>{rememberHarvestScroll();showCharacter(harvestEgg,+button.dataset.harvestCharacter);});
    find('[data-harvest-clear]').onclick=()=>{selection={};keepOneSelection=false;updateHarvestSelection();};
    find('[data-harvest-all]').onclick=()=>{selection=bulkSaleSelection(getState());keepOneSelection=false;updateHarvestSelection();};
    find('[data-harvest-keep-one]').onclick=()=>{selection=bulkSaleSelection(getState(),true);keepOneSelection=true;updateHarvestSelection();};
    find('[data-use-rewards]').onchange=e=>{useRewards=e.target.checked;updateHarvestSelection();};
    find('[data-harvest-orders]').onclick=()=>{selection=bulkSaleSelection(getState());for(const o of storyOrders(getState()).filter(o=>o.accepted&&!o.completed)){const c=o.choices.find(c=>c.species===o.choice);selection[c.species]=Math.max(0,(selection[c.species]??0)-(c.count-o.delivered));}keepOneSelection=false;updateHarvestSelection();};
    find('[data-harvest-sell]').onclick=()=>{
      const summary=saleSelectionSummary(getState(),selection,{useRewards});
      if(!summary.quantity){alertBox('请先选择要出售的数量。');return;}
      const snapshot=Object.freeze({...summary.selection}),reserveOne=keepOneSelection;let settled;
      const remaining=[0,1].flatMap(egg=>harvestEntries(getState(),egg)).filter(entry=>entry.stock-(snapshot[entry.key]??0)>0).length;
      confirmBox(`出售 ${number(summary.quantity)} 只伙伴，获得 ${number(summary.income)} CP。\n\n基础货款 ${summary.baseIncome} CP · 招牌加价 ${summary.markup} CP · 经营奖励 ${summary.bonus} CP。\n整筐${summary.baskets}次，拼盘${summary.platters}次；招牌小数余额 ${(summary.markupRemainder/100).toFixed(2)} CP。${summary.basketItems.length?'\n整筐：'+summary.basketItems.map(i=>E.label(E.char(...i.key.split(':').map(Number)))+'×'+i.count).join('、'):''}${summary.platterItems.length?'\n拼盘：'+summary.platterItems.map(ks=>ks.map(k=>E.label(E.char(...k.split(':').map(Number)))+'×3').join('、')).join('；'):''}\n外出伙伴不参与。\n出售后农场保留 ${number(remaining)} 个品种。已发现的图鉴与累计收取记录会保留。`,()=>{if(act(()=>{
        if(reserveOne&&Object.entries(snapshot).some(([key,qty])=>availableCount(getState(),key)-qty<1))throw Error('农场库存发生变化，请重新选择数量，让每种伙伴留下一只。');
        settled=basketQuote(getState(),snapshot,{useRewards});
        E.sell(getState(),snapshot,{keepOne:reserveOne,useRewards},getNow());
        for(const [key,qty]of Object.entries(snapshot))selection[key]=Math.max(0,(selection[key]??0)-qty);
        sound(8);makeWalkers();renderAlbum();
      }))saleEffects(settled);});
    };
    const kitchen=find('[data-harvest-kitchen]');if(kitchen)kitchen.onclick=()=>changePage(0);
    updateHarvestSelection();
  }

  function showCharacter(egg,id){
    const initial=speciesView(getState(),egg,id);
    if(!initial?.known){sound(13);alertBox('这个品种还没有收录。');return;}
    const origin=getPage()===4?'collection':'harvest',key=initial.key;
    if(!detailQuantities.has(key))detailQuantities.set(key,Math.min(initial.stock,selection[key]??1));
    const back=()=>origin==='collection'?renderCollection():renderAlbum();
    function updateQuantity(value){
      const entry=speciesView(getState(),egg,id),qty=Math.min(entry?.stock??0,count(value));detailQuantities.set(key,qty);
      const output=find('[data-species-quantity]');if(!output)return;
      output.textContent=number(qty);find('[data-species-minus]').disabled=qty===0;find('[data-species-plus]').disabled=qty>=entry.stock;
      find('[data-species-max]').disabled=entry.stock===0||qty>=entry.stock;
      find('[data-species-sell]').disabled=qty===0;find('[data-species-value]').textContent=number(basketQuote(getState(),{[key]:qty}).income);
    }
    function draw(){
      const entry=speciesView(getState(),egg,id);
      if(!entry?.known){back();return;}
      const recipe=egg===0?expansionRecipeHints(getState()).find(recipe=>recipe.id===id):null;
      const access=characterAccessInfo(egg,id,getState());
      const seasonal=seasonalCharacter(egg,id);
      const recorded=discoveredRecipe(getState(),key);
      const description=DESCRIPTIONS[key];
      const story=(description?`<p class="species-story">${escapeText(description)}</p>`:'')+(recorded?`<button class="species-recipe-link" data-species-recipe="${key}"><span>${recorded.special?'出现方式':`${escapeText(recorded.toolName)} Lv.${recorded.minLevel+1}`}<small>${recorded.special?'在配方册中查看条件':escapeText(recorded.ingredientNames.join(' ＋ ')||'不放调味料')}</small></span><b>${recorded.special?'查看 ›':'配方 ›'}</b></button>`:'');
      if(origin==='collection'){
        const inv=inventoryView(getState(),key),uses=speciesUses(getState(),key),ready=bookPreparation(getState(),key,getNow()).ready;
        const useButton=(use,i)=>`<button class="book-path" data-species-use="${i}">${escapeText(use.label)} ›</button>`;
        showPanel('伙伴档案',`${openBookTab?bookNavigation('species'):''}<div class="species-sheet scroll"><div class="species-topline"><span>${entry.code}</span><span class="species-seal">已收录${inv.home?'':' · 在家0'}</span></div><div class="species-portrait">${characterPortrait(egg,id)}</div><h3 class="species-name">${escapeText(entry.name)}</h3>${story}<section class="book-section"><h4>伙伴现在在哪里</h4><p>自由可用 ${inv.free} · 寻访 ${inv.R} · 营业备货 ${inv.S} · 采购预留 ${inv.Q}<br>总持有 ${inv.T} · 累计收取 ${entry.total}</p><button data-species-inventory>管理这类库存</button></section><section class="book-section"><h4>这位伙伴还能做什么</h4>${uses.slice(0,2).map(useButton).join('')}${uses.length>2?`<details><summary>全部关联（${uses.length}）</summary>${uses.slice(2).map((u,i)=>useButton(u,i+2)).join('')}</details>`:''}</section></div><footer class="species-footer"><button class="species-back cream" data-species-back>‹ 返回品种</button><button class="orange" data-species-prepare>${ready?'准备下一锅':'查看缺少条件'}</button></footer>`,'screen-panel species-screen species-book-screen');
        if(openBookTab)bindBookNavigation(panels,openBookTab);
        find('[data-species-back]').onclick=back;const close=find('.close');if(close)close.onclick=back;
        const recipeLink=find('[data-species-recipe]');if(recipeLink)recipeLink.onclick=()=>openRecipeBook?.({key,back:draw});
        find('[data-species-prepare]').onclick=()=>{if(ready&&prepareRecipe)prepareRecipe(key);else openRecipeBook?.({key,back:draw});};
        find('[data-species-inventory]').onclick=()=>{if(openInventory)openInventory(key);else{changePage(1);harvestEgg=egg;renderAlbum();}};
        each('[data-species-use]',b=>b.onclick=()=>{const use=uses[+b.dataset.speciesUse];if(openUse)openUse(use);else if(use.kind==='collection')openBooks?.({id:use.id});});
        return;
      }
      const body=`<div class="species-sheet scroll"><div class="species-topline"><span>${entry.code}</span><span class="species-seal">已收录</span></div><div class="species-portrait">${characterPortrait(egg,id)}</div><h3 class="species-name">${escapeText(entry.name)}</h3>${story}<dl class="species-stats"><div><dt>每只售价</dt><dd>${number(entry.price)}<small> CP</small></dd></div><div><dt>在家可用</dt><dd>${number(entry.stock)}<small> 只（外出 ${entry.away}）</small></dd></div><div><dt>累计收取</dt><dd>${number(entry.total)}<small> 只</small></dd></div></dl><p class="species-note">总持有 ${entry.held}只 · 外出 ${entry.away}只<br>寻访：采集${ABILITIES[key].gather} · 发现${ABILITIES[key].discover} · 适应${({yard:'菜园',water:'溪岸',wood:'林间'})[ABILITIES[key].environment]}</p><p class="species-note">${entry.stock?'选择数量，即可在农场出售。':'已经收入图鉴，农场暂时没有库存。'}</p></div><div class="species-quantity"><span>出售数量</span><button type="button" data-species-minus aria-label="减少卖出数量">−</button><output data-species-quantity aria-label="卖出数量">0</output><button type="button" data-species-plus aria-label="增加卖出数量">+</button><button type="button" class="species-max" data-species-max>最大</button></div><footer class="species-footer"><button type="button" class="species-back cream" data-species-back>‹ 返回${origin==='collection'?'图鉴':'收成表'}</button><button type="button" class="species-sell orange" data-species-sell>出售 <strong data-species-value>0</strong> CP</button></footer>`;
      showPanel('伙伴档案',body,`screen-panel species-screen${recipe?' species-expanded':''}`);
      find('[data-species-back]').onclick=back;
      bindAccess();
      const recipeLink=find('[data-species-recipe]');if(recipeLink)recipeLink.onclick=()=>openRecipeBook?.({key,back:draw});
      const close=find('.close');if(close)close.onclick=back;
      find('[data-species-minus]').onclick=()=>updateQuantity((detailQuantities.get(key)??0)-1);
      find('[data-species-plus]').onclick=()=>updateQuantity((detailQuantities.get(key)??0)+1);
      find('[data-species-max]').onclick=()=>updateQuantity(availableCount(getState(),key));
      find('[data-species-sell]').onclick=()=>{
        const current=speciesView(getState(),egg,id),quantity=Math.min(current?.stock??0,detailQuantities.get(key)??0);
        if(!quantity)return;
        const snapshot=Object.freeze({[key]:quantity}),income=basketQuote(getState(),{[key]:quantity}).income;let settled;
        confirmBox(`出售 ${number(quantity)} 只${current.name}，获得 ${number(income)} CP。\n\n图鉴与累计收取记录会保留。`,()=>{if(act(()=>{
          settled=basketQuote(getState(),snapshot);
          E.sell(getState(),snapshot,{},getNow());selection=saleSelectionSummary(getState(),selection,{useRewards}).selection;
          detailQuantities.set(key,Math.min(quantity,availableCount(getState(),key)));sound(8);makeWalkers();draw();
        }))saleEffects(settled);});
      };
      updateQuantity(detailQuantities.get(key)??0);
    }
    refreshDetail=draw;
    draw();
  }
  function refresh(){
    selection=saleSelectionSummary(getState(),selection,{useRewards}).selection;
    if(find('.species-screen')&&refreshDetail){
      const scrollTop=find('.species-sheet')?.scrollTop??0;
      refreshDetail();
      const sheet=find('.species-sheet');if(sheet)sheet.scrollTop=scrollTop;
    }else if(find('.harvest-screen')){
      rememberHarvestScroll();renderAlbum();
    }else if(find('.collection-screen')){const scrollTop=find('.collection-book')?.scrollTop??0;renderCollection();const book=find('.collection-book');if(book)book.scrollTop=scrollTop;}
  }
  function focusInventory(key){const c=resolveSpecies(key);if(!c)return;inventoryKey=key;harvestEgg=c.egg;stockOnly=false;selection={};harvestScroll.fill(0);renderAlbum();}
  function reset(){bookEgg=0;harvestEgg=0;knownOnly=false;search='';readyOnly=false;inventoryKey=null;stockOnly=true;selection={};keepOneSelection=false;refreshDetail=null;bookPages.fill(0);harvestScroll.fill(0);detailQuantities.clear();}
  return {openAlbum,showCharacter,showUnknown,renderCollection,renderAlbum,openInventory:focusInventory,refresh,reset};
}
