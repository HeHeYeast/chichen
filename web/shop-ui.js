import {materialCapacity,materialRoom} from './material-capacity.js';
import { GAME_DATA as DATA, TOOL_COUNT, EXPANSION, expansionUnlockInfo } from './content-pack.js';
import {effects} from './progression.js';
import * as E from './engine.js';
import { toolImage } from './catalog.js';
import { resolveSprite, uiIcon } from './art/manifest.js';
import { ingredientUnlockInfo } from './ingredient-unlocks.js';
import {projectMaterial} from './visibility-model.js';

const escapeHTML = value => String(value).replace(/[&<>"']/g, character => ({ '&':'&amp;', '<':'&lt;', '>':'&gt;', '"':'&quot;', "'":'&#39;' }[character]));
const formatCP = value => Number(value).toLocaleString('zh-CN');
const inventoryCount = state => Object.values(state.ingredients).reduce((sum, count) => sum + count, 0);
// Read-only alpha bounds of the 75 original 120px ingredient sprites. Crop at
// presentation time, keeping the original files and their stable IDs intact.
const ingredientFrames = [[6,44,109,73],[10,31,103,86],[12,15,97,102],[10,18,100,99],[12,28,97,89],[27,15,66,102],[27,7,66,110],[27,13,66,104],[7,22,106,97],[9,19,102,97],[6,49,110,68],[12,10,97,107],[20,6,81,111],[28,21,75,96],[27,3,67,114],[19,28,82,89],[9,48,104,69],[4,28,114,89],[27,17,67,100],[11,31,96,86],[3,40,110,77],[16,28,87,89],[18,21,85,96],[19,35,82,82],[10,49,103,68],[9,18,101,99],[8,29,105,88],[26,21,67,96],[24,8,76,109],[14,32,96,85],[9,8,86,109],[17,33,86,84],[27,11,66,106],[8,24,109,93],[7,30,106,87],[21,12,82,105],[28,34,64,83],[19,29,82,88],[25,16,67,101],[27,4,66,113],[16,37,89,80],[12,13,96,104],[19,18,92,99],[9,30,102,87],[10,19,103,98],[19,31,82,86],[13,18,94,99],[16,41,85,76],[10,37,100,80],[12,51,96,66],[19,31,82,86],[19,16,82,101],[9,53,102,64],[25,46,70,71],[9,48,102,69],[15,70,90,47],[10,26,102,91],[23,23,74,93],[28,17,64,100],[9,41,102,76],[21,34,78,83],[17,15,86,102],[28,20,64,97],[9,15,102,102],[9,19,104,98],[9,29,102,88],[5,58,108,59],[9,31,102,86],[3,57,114,60],[19,34,82,83],[14,37,93,80],[13,71,94,46],[10,64,97,53],[12,49,98,68],[4,60,112,56]];

function spriteMarkup(path, frame = null) {
  const sprite = frame ? { file:path, frame, size:[120,120] } : resolveSprite(path);
  if (!sprite.frame || !sprite.size) return `<img src="${escapeHTML(sprite.file)}" alt="">`;
  return `<svg class="sprite-art" viewBox="${sprite.frame.join(' ')}" aria-hidden="true"><image href="${escapeHTML(sprite.file)}" width="${sprite.size[0]}" height="${sprite.size[1]}"/></svg>`;
}

export const ingredientPortrait=id=>spriteMarkup(toolImage(2,id),ingredientFrames[id]);

export function shopIngredientCatalog(state, filter='all', query='') {
  const terms=String(query).trim().toLocaleLowerCase('zh-CN').split(/\s+/).filter(Boolean);
  return DATA.tools[2].map(raw=>{const projected=projectMaterial(state,raw.id);return {...ingredientUnlockInfo(state,raw.id),item:projected.known?raw:{id:raw.id,title_zh_CN:projected.name,buy_cp:null}};})
    .filter(entry=>filter==='available'?entry.available:filter==='locked'?!entry.available:true)
    .filter(({item})=>terms.every(term=>[item.title_zh_CN,item.title_zh_TW].filter(Boolean).join(' ').toLocaleLowerCase('zh-CN').includes(term)));
}

function toolLockReason(state, id) {
  if(id===EXPANSION.toolId)return expansionUnlockInfo(state).reason;
  const level = state.toolLevels[id];
  if (level >= 2) return '这一件已升至最高等级';
  if (E.canBuyTool(state, id)) return '';
  if (level >= 0) return `厨房 Lv.${level + 2} 后可升级`;
  if (id < 6 && id > 0) return `需先购买${E.label(E.tool(id - 1))}`;
  if (state.kitchenLevel < 3) return '厨房 Lv.4 后开放';
  if (id === 7) return '烧水壶 Lv.3 后开放';
  return '尚未满足购买条件';
}

function shelfRows(products) {
  const rows = [];
  for (let index = 0; index < products.length; index += 2) rows.push(`<div class="shop-shelf">${products.slice(index, index + 2).join('')}</div>`);
  return rows.join('');
}

export function createShopUI({ skillFeedback=()=>{}, getState, panels, showPanel, confirmBox, alertBox, act, sound, toolPortrait, changePage, returnFromShop=()=>changePage(0), openCookware=()=>changePage(0), openJournal, openMaterialLore, openRecipeBook=()=>changePage(4), openActivities=()=>alertBox('请从厨房打开常驻委托。') }) {
  let shopTab = 0;
  let ingredientFilter = 'all';
  let ingredientQuery = '';
  let message = '';
  const scrollPositions = [0, 0, 0];
  const quantities = new Map();
  const tabs = ['厨具', '调味料', '其他'];


  function rememberScroll() {
    const scroller = panels.querySelector('.shop-shelf-scroll');
    if (scroller) scrollPositions[Number(scroller.dataset.shopScrollTab)] = scroller.scrollTop;
  }

  function setTab(index) {
    rememberScroll();
    shopTab = Math.max(0, Math.min(2, Math.trunc(Number(index) || 0)));
    message = '';
  }

  function cookwareProducts(state) {
    return DATA.tools[1].map(tool => {
      const level = state.toolLevels[tool.id];
      const targetLevel = Math.min(2, Math.max(0, level + 1));
      const maxed = level >= 2;
      const allowed = E.canBuyTool(state, tool.id);
      const price = tool[`lv_${targetLevel}_buy_cp`];
      const targetMinutes = tool[`lv_${targetLevel}_min`];
      const currentMinutes = level >= 0 ? tool[`lv_${level}_min`] : null;
      const name = escapeHTML(E.label(tool));
      const reason = toolLockReason(state, tool.id);
      const hint = reason || (state.cp < price ? `还差 ${formatCP(price - state.cp)} CP` : tool.id===8 ? `点心配方 ${targetLevel===0?'3':targetLevel===1?'5':'6'} 味 · 鸡蛋专用` : level >= 0 ? '下次调理使用升级时长' : '购买后可在厨房使用');
      const artwork = level >= 0 && !maxed ? `<span class="shop-art-current">${toolPortrait(tool.id, level)}</span><span class="shop-art-arrow" aria-hidden="true">›</span><span class="shop-art-target">${toolPortrait(tool.id, targetLevel)}</span>` : `<span class="shop-art-single">${toolPortrait(tool.id, targetLevel)}</span>`;
      const duration = level >= 0 && !maxed ? `${currentMinutes} → ${targetMinutes} 分钟` : `${targetMinutes} 分钟`;
      return `<article class="shop-product ${allowed ? '' : 'shop-product-unavailable'}" aria-label="${name}">
        <button type="button" class="shop-product-art shop-inspect" data-shop-tool-details="${tool.id}" data-shop-focus="tool-details-${tool.id}" aria-label="查看${name}的三级外观与成长">${artwork}<span class="shop-inspect-mark" aria-hidden="true">三级外观 ›</span></button>
        <h3 class="shop-product-name">${name} <span>Lv.${targetLevel + 1}</span></h3>
        <p class="shop-item-meta">${level < 0 ? '尚未拥有' : `持有 Lv.${level + 1}`}</p>
        <p class="shop-time"><small>调理</small> ${duration}</p>
        <p class="shop-lock-reason ${allowed && state.cp < price ? 'shop-short' : ''}">${escapeHTML(hint)}</p>
        <button class="shop-buy" data-shop-buy-tool="${tool.id}" data-shop-focus="tool-${tool.id}" ${allowed ? '' : 'disabled'} aria-label="${maxed ? name + '已满级' : (level < 0 ? '购买' : '升级') + name + ' Lv.' + (targetLevel + 1) + '，' + price + ' CP' + (!allowed ? '，' + escapeHTML(reason) : '')}">${maxed ? '已满级' : `<span>${allowed ? level < 0 ? '购买' : '升级' : '未解锁'}</span><strong>${formatCP(price)} CP</strong>`}</button>
      </article>`;
    });
  }

  function ingredientProducts(state) {
    const room = Math.max(1, materialRoom(state));
    return shopIngredientCatalog(state,ingredientFilter,ingredientQuery).map(({id,item,available,special}) => {
      const quantity = Math.min(room, Math.max(1, quantities.get(id) || 1));
      quantities.set(id, quantity);
      const name = escapeHTML(E.label(item));
      const price = item.buy_cp * quantity;
      return `<article class="shop-product shop-ingredient ${available?'':'shop-ingredient-locked'}" aria-label="${name}">
        <button type="button" class="shop-product-art shop-inspect" data-shop-ingredient-details="${id}" data-shop-focus="ingredient-details-${id}" aria-label="查看${name}的用途与解锁条件"><span class="shop-art-single">${ingredientPortrait(id)}</span><span class="shop-owned">持有 ${state.ingredients[id] || 0}</span></button>
        <h3 class="shop-product-name">${name}</h3>
        <p class="shop-item-meta">${special?'委托赠品':item.buy_cp===null?'辨认后开放供应':`单价 ${formatCP(item.buy_cp)} CP`}</p>
        ${available?`<div class="shop-quantity" aria-label="${name}购买数量"><button data-shop-quantity="${id}" data-step="-1" data-shop-focus="minus-${id}" aria-label="减少${name}购买数量" ${quantity <= 1 ? 'disabled' : ''}>−</button><output aria-label="购买数量">${quantity}</output><button data-shop-quantity="${id}" data-step="1" data-shop-focus="plus-${id}" aria-label="增加${name}购买数量" ${quantity >= room ? 'disabled' : ''}>＋</button></div>
        <button class="shop-buy" data-shop-buy-ingredient="${id}" data-shop-focus="ingredient-${id}" aria-label="购买${name} ${quantity} 个，${price} CP"><span>购买</span><strong>${formatCP(price)} CP</strong></button>`:`<p class="shop-unlock-label">${special?'完成委托可领取':'未解锁 · 目标可查看'}</p><button type="button" class="shop-buy shop-details" ${special?`data-shop-activity="${id}"`:`data-shop-ingredient-details="${id}"`} data-shop-focus="ingredient-${id}">${special?'去看委托':'查看解锁条件'}</button>`}
      </article>`;
    });
  }

  function otherProducts(state) {
    return [
      `<article class="shop-product shop-service"><div class="shop-product-art"><span class="shop-art-single"><img src="/assets/png/Egg/egg_0_0_0.png" alt=""></span></div><h3 class="shop-product-name">鸡蛋</h3><p class="shop-item-meta shop-owned-label">已拥有</p><p class="shop-service-note">选择厨具开始调理，<br>孵出不同的鸡宝。</p><button class="shop-buy" data-shop-kitchen data-shop-focus="chicken-kitchen">去厨房</button></article>`,
      `<article class="shop-product shop-service"><div class="shop-product-art"><span class="shop-art-single"><img src="/assets/png/Egg/egg_1_0_0.png" alt=""></span></div><h3 class="shop-product-name">鸭蛋</h3><p class="shop-item-meta">${state.duck ? '已拥有' : '永久解锁 · 2,500 CP'}</p><p class="shop-service-note">${state.duck ? '可在厨房切换蛋种，<br>继续收集鸭宝。' : '开启 '+DATA.characters[1].length+' 种鸭宝的收集之旅。<br>从下一批调理开始切换。'}</p><button class="shop-buy" ${state.duck ? 'data-shop-kitchen' : 'data-shop-duck'} data-shop-focus="duck-details">${state.duck ? '去厨房' : '解锁鸭蛋'}</button></article>`,
      `<article class="shop-product shop-service shop-activity-wide"><div class="shop-gift-art">${[68,69,70].map(id=>`<span>${ingredientPortrait(id)}</span>`).join('')}</div><h3 class="shop-product-name">鸡宝邻里委托</h3><p class="shop-item-meta">御神签 · 火苗 · 木绵</p><p class="shop-activity-note">完成常驻委托，领取特别原料。</p><button class="shop-buy shop-details" data-shop-activity="" data-shop-focus="activities">去看委托与礼物</button></article>`,
    ];
  }

  function requestTool(id) {
    const state = getState();
    if (!E.canBuyTool(state, id)) { alertBox(toolLockReason(state, id)); return; }
    const tool = E.tool(id), level = state.toolLevels[id], targetLevel = level + 1;
    const price = tool[`lv_${targetLevel}_buy_cp`], minutes = tool[`lv_${targetLevel}_min`];
    const duration = level < 0 ? `${minutes} 分钟` : `${tool[`lv_${level}_min`]} → ${minutes} 分钟`;
    confirmBox(`${E.label(tool)} ${level < 0 ? 'Lv.' : '升至 Lv.'}${targetLevel + 1}\n调理时长：${duration}\n需要花费 ${formatCP(price)} CP。\n${level < 0 ? '确定购买吗？' : '升级后对新调理生效。确定升级吗？'}`, () => act(() => {
      const current = getState();
      if (current.toolLevels[id] !== level) throw Error('厨具等级已变化，请重新确认。');
      E.buyTool(current, id);
      sound(7);
      message = `${level < 0 ? '已购买' : '已升级'} ${E.label(tool)} Lv.${targetLevel + 1} · 调理 ${minutes} 分钟`;
      openShop();
    }));
  }

  function requestIngredient(id) {
    const unlock = ingredientUnlockInfo(getState(),id);
    if (!unlock.available) { openIngredientDetails(id); return; }
    const item = E.ingredient(id), quantity = quantities.get(id) || 1;
    let purchased = false;
    const state=getState(),price=item.buy_cp*quantity,rebate=price*effects(state).rebate+state.progress.trade.rebateRemainder,refund=Math.floor(rebate/100);
    confirmBox(`${E.label(item)} ×${quantity}\n材料原价${price} CP · 本次返还${refund} CP · 实付${price-refund} CP。\n尚有${((rebate%100)/100).toFixed(2)} CP返利未到账，攒满1 CP自动返还。需先付得起原价。`, () => {let actualRefund=0;if(act(() => {
      if (purchased) return;
      const current = getState();
      const before=current.cp;
      E.buyIngredient(current, id, quantity);
      actualRefund=current.cp-before+price;
      purchased = true;
      sound(7);
      quantities.set(id, 1);
      message = `买到了 ${E.label(item)} ×${quantity} · 现有 ${current.ingredients[id]} 个`;
      openShop();
    })&&actualRefund>0)skillFeedback([{id:'TRADE-1',text:'材料返利 +'+actualRefund+' CP'}]);});
  }

  function requestDuck() {
    const unlock=E.duckUnlockInfo(getState());
    if(unlock.owned){changePage(0);return;}
    if(!unlock.available){alertBox(unlock.reason);return;}
    let purchased=false;
    confirmBox('永久解锁鸭蛋\n需要花费 2,500 CP。\n当前这一批继续孵化，收取后可切换鸭蛋。\n确定解锁吗？',()=>act(()=>{
      if(purchased)return;
      E.buyDuck(getState());purchased=true;sound(7);
      message='鸭蛋已解锁！收完当前这批后，在厨房切换蛋种，开始收集鸭宝。';
      openShop();
    }));
  }

  function bindDetailBack() {
    const back=()=>{sound(3);openShop();};
    const close=panels.querySelector('.close');
    if(close){close.onclick=back;close.setAttribute('aria-label','返回商店货架');}
    panels.querySelector('[data-shop-detail-back]').onclick=back;
  }

  function openToolDetails(id) {
    rememberScroll();
    const state=getState(),item=E.tool(id),owned=state.toolLevels[id];
    if(!item)return;
    const stages=[0,1,2].map(level=>{
      const art=toolPortrait(id,level);
      const requirement=level===0?id===8?'厨房 Lv.2，发现 12 种伙伴':id===0?'初始拥有':id<6?`先拥有${E.label(E.tool(id-1))}`:id===6?'厨房 Lv.4':'厨房 Lv.4，烧水壶 Lv.3':id===8?`先升至 Lv.${level}，厨房 Lv.${level+2}`:`先升至 Lv.${level}，厨房 Lv.${level+1}`;
      return `<section class="shop-growth-stage ${owned===level?'is-owned':''}" aria-label="${escapeHTML(E.label(item))} Lv.${level+1}"><h3>Lv.${level+1}</h3><div class="shop-growth-art">${art}</div><span class="shop-growth-owned">${owned===level?'当前持有':owned>level?'已达成':'待升级'}</span><p><b>${item[`lv_${level}_min`]} 分钟</b><small>调理时长</small></p><p><b>${formatCP(item[`lv_${level}_cook_cp`])} CP</b><small>每批消耗</small></p><p><b>${formatCP(item[`lv_${level}_buy_cp`])} CP</b><small>${level?'升级费用':'购买费用'}</small></p><p class="shop-growth-requirement">${escapeHTML(requirement)}</p></section>`;
    }).join('');
    const allowed=E.canBuyTool(state,id),reason=toolLockReason(state,id);
    const body=`<p class="shop-detail-eyebrow">${id<8?'厨具三级外观':'竹笼点心坊'} · 已拥有 ${owned<0?'0':owned+1} / 3 级</p><div class="scroll shop-detail-scroll"><div class="shop-growth-stages">${stages}</div><p class="shop-detail-note">${id===8?'竹笼升级会缩短时长，并开放更多点心配方。':'三级造型各有变化。升级缩短调理时长，让下一批更快出炉。'}<br>升级对新调理生效，当前这一批继续孵化。</p>${reason?`<p class="shop-detail-status">${escapeHTML(reason)}</p>`:''}</div><footer class="shop-detail-footer"><button type="button" data-shop-detail-back>‹ 返回货架</button><button type="button" class="shop-buy" data-shop-detail-tool="${id}" ${allowed?'':'disabled'}>${owned>=2?'已满级':`${owned<0?'购买':'升级'} Lv.${owned+2}`}</button></footer>`;
    showPanel(`${E.label(item)}成长册`,body,'screen-panel shop-screen shop-detail-screen');
    bindDetailBack();
    panels.querySelector('[data-shop-detail-tool]').onclick=()=>requestTool(id);
  }

  function openIngredientDetails(id) {
    rememberScroll();
    const state=getState(),info=ingredientUnlockInfo(state,id),view=projectMaterial(state,id),item=E.ingredient(id);
    if(!item)return;
    if(!view.known){showPanel('未辨认材料','<div class="scroll shop-detail-scroll"><h3>先带回地区标本</h3><p>在地区与发现中免费辨认后，这份材料的名称、价格和供应会记入册中。</p></div><footer class="shop-detail-footer"><button data-shop-detail-back>返回货架</button></footer>','screen-panel shop-screen shop-detail-screen');bindDetailBack();return;}
    const use=id===18?'加入这一批后，鸡宝不会因为厨房变脏而生病。':id===36?'加入这一批后，鸡宝成熟后不会烧焦。':'调理前加入，参与厨具与蛋种的配方搭配。每批消耗一份。';
    const requirements=info.requirementGroups?.length?info.requirementGroups.map((group,index)=>`<li class="${group.met?'is-met':''}"><span class="shop-condition-check" aria-hidden="true">${group.met?'✓':index+1}</span><div>${escapeHTML(group.description)}<small>${group.met?'已达成':'尚未达成'}</small></div></li>`).join(''):`<li><div>${escapeHTML(info.description)}</div></li>`;
    const body=`<div class="shop-ingredient-hero"><span>${ingredientPortrait(id)}</span><div><b>${info.special?'委托赠品':`${formatCP(item.buy_cp)} CP / 份`}</b><small>持有 ${state.ingredients[id]??0} 份</small><em>${info.available?'已开放购买':info.special?'活动获得':'待解锁'}</em></div></div><div class="scroll shop-detail-scroll"><p class="shop-detail-note">${use}</p><h3 class="shop-condition-heading">${info.special?'获取方式':info.requirementGroups?.length>1?'满足以下任一条件即可购买':'购买条件'}</h3><ol class="shop-condition-list">${requirements}</ol>${info.special?'<p class="shop-detail-note">常驻委托可重复领取。先查看目标与奖励，再去完成。</p>':''}${id>=75&&openMaterialLore?'<button data-shop-material-lore>食材见闻与地方做法</button>':''}</div><footer class="shop-detail-footer"><button type="button" data-shop-detail-back>‹ 返回货架</button><button type="button" class="shop-buy" data-shop-detail-ingredient="${id}" ${info.available||info.special?'':'disabled'}>${info.special?'去看委托':info.available?'购买一份':'待解锁'}</button></footer>`;
    showPanel(E.label(item),body,'screen-panel shop-screen shop-detail-screen');
    bindDetailBack();
    panels.querySelector('[data-shop-material-lore]')?.addEventListener('click',()=>openMaterialLore(id));
    panels.querySelector('[data-shop-detail-ingredient]').onclick=()=>{
      if(info.special){openActivities(id);return;}
      quantities.set(id,1);requestIngredient(id);
    };
  }

  function bindIngredientControls(screen) {
    screen.querySelectorAll('[data-shop-ingredient-details]').forEach(button=>{button.onclick=()=>{sound(3);openIngredientDetails(Number(button.dataset.shopIngredientDetails));};});
    screen.querySelectorAll('[data-shop-activity]').forEach(button=>{button.onclick=()=>{rememberScroll();openActivities(button.dataset.shopActivity===''?undefined:Number(button.dataset.shopActivity));};});
    screen.querySelectorAll('[data-shop-buy-ingredient]').forEach(button=>{button.onclick=()=>requestIngredient(Number(button.dataset.shopBuyIngredient));});
    screen.querySelectorAll('[data-shop-quantity]').forEach(button=>{button.onclick=()=>{
      const id=Number(button.dataset.shopQuantity),room=Math.max(1,materialRoom(getState()));
      quantities.set(id,Math.min(room,Math.max(1,(quantities.get(id)||1)+Number(button.dataset.step))));
      sound(3);openShop();
    };});
  }

  const shelfContents=products=>products.length?shelfRows(products):'<p class="shop-empty">没有找到这份调味料。<br>换个名字，或试试「全部」。</p>';

  function openShop() {
    const oldScreen = panels.querySelector('.shop-screen');
    if (!oldScreen) message = '';
    const active = panels.ownerDocument?.activeElement;
    const focusKey = oldScreen?.contains(active) ? active?.dataset?.shopFocus : null;
    rememberScroll();
    const state = getState(), count = inventoryCount(state);
    const products = shopTab === 0 ? cookwareProducts(state) : shopTab === 1 ? ingredientProducts(state) : otherProducts(state);
    const allIngredients=shopTab===1?shopIngredientCatalog(state):[];
    const availableCount=allIngredients.filter(entry=>entry.available).length;
    const shelfInfo = shopTab === 0 ? `${TOOL_COUNT} 种厨具 · ${TOOL_COUNT*3} 套外观` : shopTab === 1 ? `可购买 ${availableCount} / ${allIngredients.length} 种` : '蛋种与服务';
    const feedback = message || (shopTab === 0 ? '看看升级后的样子，下一批调理会更快。' : shopTab === 1 ? count >= materialCapacity(state) ? `调味料已满 ${materialCapacity(state)} 个，使用一些后再来。` : `调味料最多可持有 ${materialCapacity(state)} 个，可选择购买数量。` : '切换蛋种，认识不同的伙伴。');
    const body = `<div class="shop-tabbar" role="tablist" aria-label="商品分类">${tabs.map((name, index) => `<button role="tab" aria-selected="${index === shopTab}" aria-controls="shop-shelves-panel" tabindex="${index === shopTab ? 0 : -1}" data-shop-tab="${index}" data-shop-focus="tab-${index}" class="${index === shopTab ? 'shop-tab-active' : ''}">${name}</button>`).join('')}</div>
      <div class="shop-ledger"><span>${shelfInfo}</span><span>${shopTab === 1 ? `包内 <b>${count} / ${materialCapacity(state)}</b>` : `持有 <b>${formatCP(state.cp)} CP</b>`}</span></div>
      ${shopTab===1?`<div class="shop-ingredient-filters" role="group" aria-label="调味料开放状态">${[['all',`全部 ${allIngredients.length}`],['available',`可购买 ${availableCount}`],['locked',`待解锁 ${allIngredients.length-availableCount}`]].map(([key,label])=>`<button type="button" data-shop-filter="${key}" data-shop-focus="filter-${key}" aria-pressed="${ingredientFilter===key}">${label}</button>`).join('')}</div>`:''}
      ${shopTab===1?`<div class="shop-search"><input type="search" aria-label="搜索调味料名称" placeholder="找一份调味料…" value="${escapeHTML(ingredientQuery)}" data-shop-search data-shop-focus="search"><span data-shop-search-count role="status">${products.length} 种</span><button type="button" data-shop-search-clear aria-label="清除调味料搜索" ${ingredientQuery?'':'disabled'}>×</button></div>`:''}
      <div class="scroll shop-shelf-scroll" id="shop-shelves-panel" data-shop-scroll-tab="${shopTab}" role="tabpanel" aria-label="${tabs[shopTab]}商品货架" tabindex="0">${openJournal&&shopTab!==1?'<button class="journal-entry" data-shop-journal>寻宝日历 · 16 位四时新伙伴 ›</button>':''}<div class="shop-shelf-case">${shelfContents(products)}</div></div>
      <footer class="shop-counter"><span class="shop-sign-icon" aria-hidden="true">${spriteMarkup(uiIcon(7))}</span><p class="shop-message" role="status">${escapeHTML(feedback)}</p></footer>`;
    showPanel('鸡宝小卖部', body, 'screen-panel shop-screen');
    const screen = panels.querySelector('.shop-screen');
    const close = screen.querySelector('.close');
    if (close) { close.style.display = ''; close.setAttribute('aria-label', '离开商店'); close.onclick = returnFromShop; }
    const scroller = screen.querySelector('.shop-shelf-scroll');
    scroller.scrollTop = scrollPositions[shopTab];
    scroller.addEventListener('scroll', () => { scrollPositions[Number(scroller.dataset.shopScrollTab)] = scroller.scrollTop; }, { passive: true });
    screen.querySelectorAll('[data-shop-tab]').forEach(button => {
      button.onclick = () => { setTab(Number(button.dataset.shopTab)); sound(3); openShop(); };
      button.onkeydown = event => {
        if (!['ArrowLeft', 'ArrowRight', 'Home', 'End'].includes(event.key)) return;
        event.preventDefault();
        const target = event.key === 'Home' ? 0 : event.key === 'End' ? 2 : (shopTab + (event.key === 'ArrowRight' ? 1 : 2)) % 3;
        setTab(target); sound(3); openShop();
        [...panels.querySelector('.shop-screen').querySelectorAll('[data-shop-tab]')].find(item => Number(item.dataset.shopTab) === target)?.focus({ preventScroll:true });
      };
    });
    screen.querySelectorAll('[data-shop-buy-tool]').forEach(button => { button.onclick = () => requestTool(Number(button.dataset.shopBuyTool)); });
    screen.querySelectorAll('[data-shop-tool-details]').forEach(button=>{button.onclick=()=>{sound(3);openToolDetails(Number(button.dataset.shopToolDetails));};});
    bindIngredientControls(screen);
    screen.querySelectorAll('[data-shop-filter]').forEach(button=>{button.onclick=()=>{ingredientFilter=button.dataset.shopFilter;scrollPositions[1]=0;sound(3);const current=screen.querySelector('.shop-shelf-scroll');if(current)current.scrollTop=0;openShop();};});
    screen.querySelector('[data-shop-journal]')?.addEventListener('click',()=>{rememberScroll();openJournal('recipes');});
    const search=screen.querySelector('[data-shop-search]');
    if(search){
      let composing=false;
      const updateSearch=()=>{
        ingredientQuery=search.value;
        const matches=ingredientProducts(getState());
        screen.querySelector('.shop-shelf-case').innerHTML=shelfContents(matches);
        screen.querySelector('[data-shop-search-count]').textContent=`${matches.length} 种`;
        screen.querySelector('[data-shop-search-clear]').disabled=!ingredientQuery;
        scroller.scrollTop=0;scrollPositions[1]=0;bindIngredientControls(screen);
      };
      search.addEventListener('compositionstart',()=>{composing=true;});
      search.addEventListener('compositionend',()=>{composing=false;updateSearch();});
      search.oninput=event=>{if(!composing&&!event.isComposing)updateSearch();};
      screen.querySelector('[data-shop-search-clear]').onclick=()=>{search.value='';updateSearch();search.focus({preventScroll:true});};
    }
    screen.querySelectorAll('[data-shop-kitchen]').forEach(button => { button.onclick = () => changePage(0); });
    screen.querySelector('[data-shop-duck]')?.addEventListener('click', requestDuck);
    if (focusKey) [...screen.querySelectorAll('[data-shop-focus]')].find(button => button.dataset.shopFocus === focusKey && !button.disabled)?.focus({ preventScroll: true });
  }

  return { openShop, setTab, openToolDetails, openIngredientDetails, openRecipes:id=>{rememberScroll();openRecipeBook({tool:8,key:id===undefined?null:`0:${id}`});} };
}
