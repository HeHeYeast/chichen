// 鸡宝小卖部: a kit page with three bookmarks (厨具 / 调味料 / 其他). Goods sit on painted tiles
// (picture, name, price); a tap opens the item's drawer, where it is compared, bought or upgraded.
// Seasonings filter by the player's own cookware: what it opens for sale (配套调味料, public) and what recipes already
// recorded use (never spoilers); the cookware's drawer lists the 配套调味料 of each level,
// and amounts come from quick picks, never from typing.
import {materialCapacity,materialRoom} from './material-capacity.js';
import { GAME_DATA as DATA, EXPANSION, expansionUnlockInfo } from './content-pack.js';
import {effects} from './progression.js';
import * as E from './engine.js';
import { toolImage } from './catalog.js';
import { resolveSprite, spriteSVG, uiIcon } from './art/manifest.js';
import { ingredientUnlockInfo, cookwareSeasonings } from './ingredient-unlocks.js';
import {projectMaterial} from './visibility-model.js';
import {unknownArt,visualImage} from './visual-assets.js';
import {RECIPE_CATALOG,recipeDiscovered} from './recipe-book.js';
import {kitTabs,kitSheet,kitCell,kitCoin,kitButton,kitButton2,kitChip,kitChipHtml,kitIcon,kitEgg} from './ui-kit.js';
import {ingredientFlavor,INGREDIENT_FLAVOR_GROUPS} from './ingredient-flavors.js';

const escapeHTML = value => String(value).replace(/[&<>"']/g, character => ({ '&':'&amp;', '<':'&lt;', '>':'&gt;', '"':'&quot;', "'":'&#39;' }[character]));
const formatCP = value => Number(value).toLocaleString('zh-CN');
const inventoryCount = state => Object.values(state.ingredients).reduce((sum, count) => sum + count, 0);
const COIN = kitIcon.coin;
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

// Seasonings each cookware uses in recipes the player has already recorded (no spoilers).
export function knownToolSeasonings(state) {
  const map=new Map();
  for(const r of RECIPE_CATALOG){if(r.toolId<0||!r.ingredients.length||!recipeDiscovered(state,r.egg,r.id))continue;const set=map.get(r.toolId)??new Set();r.ingredients.forEach(id=>set.add(id));map.set(r.toolId,set);}
  return map;
}

function toolLockReason(state, id) {
  if(id===EXPANSION.toolId)return expansionUnlockInfo(state).reason;
  const level = state.toolLevels[id];
  if (level >= 2) return '已经是最高等级';
  if (E.canBuyTool(state, id)) return '';
  if (level >= 0) return `厨房 Lv.${level + 2} 后可升级`;
  if (id < 6 && id > 0) return `先买${E.label(E.tool(id - 1))}`;
  if (state.kitchenLevel < 3) return '厨房 Lv.4 后开放';
  if (id === 7) return '烧水壶 Lv.3 后开放';
  return '还没满足条件';
}
const toolRequirement=(id,level)=>level===0?id===8?'厨房 Lv.2 · 发现 12 种':id===0?'初始拥有':id<6?`先有${E.label(E.tool(id-1))}`:id===6?'厨房 Lv.4':'烧水壶 Lv.3':id===8?`厨房 Lv.${level+2}`:`厨房 Lv.${level+1}`;

export function createShopUI({ characterPortrait=()=>'', skillFeedback=()=>{}, notify=()=>{}, getState, panels, showPanel, confirmBox, alertBox, act, sound, toolPortrait, changePage, returnFromShop=()=>changePage(0), openCookware=()=>changePage(0), openJournal, openMaterialLore, openRecipeBook=()=>changePage(4), openKitchenUpgrade=()=>{}, openActivities=()=>alertBox('请从厨房打开常驻委托。') }) {
  let shopTab = 0;
  // Seasoning shelf: 'all' | 'owned' | a cookware id; locked ones show only on request.
  let seasonFilter = 'all', seasonFlavor = '', showLocked = false;
  // The open drawer: {kind:'tool'|'ingredient'|'egg'|'duck', id} or null; amount for seasonings.
  let drawer = null, amount = 1;
  const scrollPositions = [0, 0, 0];
  const tabs = ['厨具', '调味料', '其他'];
  const find = q => panels.querySelector(q);

  function rememberScroll() {
    const scroller = find('.shop-shelf-scroll');
    if (scroller) scrollPositions[Number(scroller.dataset.shopScrollTab)] = scroller.scrollTop;
  }
  function setTab(index) {
    rememberScroll();
    shopTab = Math.max(0, Math.min(2, Math.trunc(Number(index) || 0)));
    drawer = null;
  }

  // ---- shelves --------------------------------------------------------------
  // Goods stand on wooden shelves (no tile frames); the price hangs below on a tag, the name under it.
  const shelfItem=({pic,name,price,badge='',attrs='',label=name,locked=false,flavor=''})=>`<button type="button" class="sh-item${locked?' is-locked':''}" ${attrs} aria-label="${escapeHTML(label)}"><span class="sh-item-art" data-visual>${pic}${badge?`<b class="sh-item-badge">${escapeHTML(badge)}</b>`:''}</span><span class="sh-tag">${price}</span><span class="sh-item-name">${escapeHTML(name)}</span>${flavor?`<small class="sh-item-flavor">${escapeHTML(flavor)}</small>`:''}</button>`;
  // The counter: 鸡宝 minds the shop and says one useful thing for this shelf; the wallet lies on the counter.
  function counter(state) {
    let line = '今天想买点什么？';
    if (shopTab === 0) { const up = DATA.tools[1].find(t => state.toolLevels[t.id] >= 0 && state.toolLevels[t.id] < 2 && E.canBuyTool(state, t.id) && state.cp >= t[`lv_${state.toolLevels[t.id] + 1}_buy_cp`]); line = up ? `${E.label(up)}能升 Lv.${state.toolLevels[up.id] + 2} 了` : '好锅开火更快'; }
    else if (shopTab === 1) line = /^\d+$/.test(seasonFilter) ? `${E.label(E.tool(Number(seasonFilter)))}用得上这些` : seasonFilter === 'owned' ? '这些你手里都有' : '按厨具挑最快';
    else line = !state.duck && state.cp >= 2500 ? '鸭蛋到货了！' : '蛋和厨房都在这儿';
    // On the seasoning shelf the material bag lies on the counter beside the purse (how full it is matters when buying).
    const count = inventoryCount(state), cap = materialCapacity(state);
    const bag = shopTab === 1 ? kitChipHtml(`${kitIcon.pouch}<b>${count}</b>/${cap}`, 'sh-bagchip' + (count >= cap ? ' hot' : '')) : '';
    return `<div class="sh-counter"><span class="sh-keeper">${characterPortrait(0, 0)}</span><p class="sh-say">${escapeHTML(line)}</p><span class="sh-purse" data-row>${bag}${kitChipHtml(`${COIN}<b>${formatCP(state.cp)}</b>`, 'sh-wallet')}</span></div>`;
  }
  function toolTiles(state) {
    return DATA.tools[1].map(tool => {
      const level = state.toolLevels[tool.id], target = Math.min(2, Math.max(0, level + 1)), maxed = level >= 2, allowed = E.canBuyTool(state, tool.id);
      const price = tool[`lv_${target}_buy_cp`], name = E.label(tool);
      const sub = maxed ? '<span class="sh-price done">满级</span>' : allowed ? `<span class="sh-price${state.cp < price ? ' short' : ''}">${COIN}${formatCP(price)}</span>` : `<span class="sh-price lock"><img src="/web/art/golden-journey/lock.png" alt="">未开放</span>`;
      return shelfItem({pic:toolPortrait(tool.id, maxed ? 2 : target), name, price:sub, badge:level < 0 ? '' : `Lv.${level + 1}`, attrs:`data-shop-tool-details="${tool.id}" data-shop-focus="tool-details-${tool.id}"`, label:`${name}，${level < 0 ? '未拥有' : `持有 Lv.${level + 1}`}`, locked:!allowed && !maxed && level < 0});
    });
  }
  function seasoningEntries(state) {
    const known = knownToolSeasonings(state);
    const supplied = /^\d+$/.test(seasonFilter) ? new Set(cookwareSeasonings(Number(seasonFilter)).filter(x => !x.duck || state.duck).map(x => x.id)) : null;
    return shopIngredientCatalog(state).filter(({id}) => seasonFilter === 'all' ? true : seasonFilter === 'owned' ? (state.ingredients[id] ?? 0) > 0 : known.get(Number(seasonFilter))?.has(id) || supplied.has(id));
  }
  function seasoningTile(state, {id, item, available, special}) {
    const known = projectMaterial(state, id).known, n = state.ingredients[id] ?? 0, name = E.label(item);
    const sub = special ? '<span class="sh-price">委托</span>' : available ? `<span class="sh-price">${COIN}${formatCP(item.buy_cp)}</span>` : `<span class="sh-price lock"><img src="/web/art/golden-journey/lock.png" alt="">待解锁</span>`;
    return shelfItem({pic:known ? ingredientPortrait(id) : visualImage(unknownArt, 'unknown'), name, price:sub, badge:n ? `×${n}` : '', flavor:known?ingredientFlavor(id):'', attrs:`data-shop-ingredient-details="${id}" data-shop-focus="ingredient-details-${id}"${available || special ? '' : ' data-locked'}`, label:`${name}${n ? `，持有 ${n}` : ''}`, locked:!(available || special)});
  }
  function seasoningShelf(state) {
    const all = seasoningEntries(state).filter(e=>!seasonFlavor||projectMaterial(state,e.id).known&&ingredientFlavor(e.id)===seasonFlavor), open = all.filter(e => e.available || e.special || (state.ingredients[e.id] ?? 0) > 0), locked = all.filter(e => !open.includes(e));
    const owned = DATA.tools[1].filter(t => state.toolLevels[t.id] >= 0);
    const coins = `<div class="gd-scroll-row sh-filters" role="group" aria-label="按厨具筛选调味料" data-hscroll>${kitCoin('全部', 'data-shop-season="all"', seasonFilter === 'all', '全部调味料')}${kitCoin('持有', 'data-shop-season="owned"', seasonFilter === 'owned', '我持有的')}${owned.map(t => kitCoin(toolPortrait(t.id, Math.max(0, state.toolLevels[t.id])), `data-shop-season="${t.id}"`, seasonFilter === String(t.id), `${E.label(t)}用到的调味料`)).join('')}</div>`;
    const tiles = [...open.map(e => seasoningTile(state, e)), ...(showLocked ? locked.map(e => seasoningTile(state, e)) : [])];
    const more = locked.length ? shelfItem({pic:visualImage(unknownArt, 'unknown'), name:showLocked ? '收起' : '待解锁', price:`<span class="sh-price lock"><img src="/web/art/golden-journey/lock.png" alt="">${showLocked ? '收起' : `${locked.length} 种`}</span>`, attrs:'data-shop-locked-toggle', label:showLocked ? '收起待解锁的调味料' : `显示 ${locked.length} 种待解锁的调味料`}) : '';
    const tool = /^\d+$/.test(seasonFilter) ? E.tool(Number(seasonFilter)) : null;
    const empty = !tiles.length && !more ? `<p class="kp-empty">${tool ? `${escapeHTML(E.label(tool))}还没记下要放调味料的配方` : '还没有调味料'}</p>` : '';
    const flavors=`<label class="sh-flavor-filter">线索类别 <select data-shop-flavor aria-label="按线索类别筛选"><option value="">全部类别</option>${Object.keys(INGREDIENT_FLAVOR_GROUPS).map(name=>`<option${seasonFlavor===name?' selected':''}>${escapeHTML(name)}</option>`).join('')}</select></label>`;
    return `${coins}${flavors}<div class="sh-shelf">${tiles.join('')}${more}</div>${empty}`;
  }
  // 其他: four big cards (eggs, the kitchen itself, neighbour requests).
  const BEDS=['stage-bed-0-v6.png','stage-bed-1-v6.png','stage-bed-2-v6.png','stage-facility-3-v6.png'];
  const card=({art,name,status,attrs})=>`<button type="button" class="sh-card" ${attrs}><span class="sh-card-art" data-visual>${art}</span><b class="sh-card-name">${escapeHTML(name)}</b><span class="sh-card-sub">${status}</span></button>`;
  // 其他 stands on the same shelves as the rest of the shop: eggs, the kitchen plan, the neighbours' special goods.
  // One big glossy egg sits in its own basket (chicken) or tray (duck): back of the vessel, the egg, then the front rim.
  const eggVessel = duck => { const v = duck ? 'tray' : 'basket'; return `<span class="sh-vessel ${v}"><img class="v-base" src="/web/art/golden-business/${v}-base.png" alt=""><span class="v-eggs"><img class="v-egg" src="/web/art/golden-ui/${duck ? 'ic-duck-egg' : 'ic-egg'}.png" alt=""></span><img class="v-front" src="/web/art/golden-business/${v}-front.png" alt=""></span>`; };
  // 其他 is a little yard behind the counter instead of another shelf: the egg baskets on one plank, the kitchen plan as
  // a wide sign with its four levels, and the neighbours' goods. Taps and focus keys are the same as before.
  function otherPage(state) {
    const k = E.kitchenUpgradeInfo(state), level = state.kitchenLevel + 1, duckShort = !state.duck && state.cp < 2500;
    const basket = (duck, status, attrs, label) => `<button type="button" class="sh-basket${duck && duckShort ? ' is-locked' : ''}" ${attrs} aria-label="${escapeHTML(label)}"><span class="sh-basket-art" data-visual>${eggVessel(duck)}<span class="sh-tag corner">${status}</span></span><span class="sh-plate"><b data-safe>${duck ? '鸭蛋' : '鸡蛋'}</b></span></button>`;
    const pips = Array.from({length:4}, (_, i) => `<i class="${i < level ? 'on' : ''}${i === level - 1 ? ' now' : ''}"></i>`).join('');
    return `<div class="sh-yard">
      <div class="sh-bunting" aria-hidden="true"><img src="/web/art/golden-ui/bunting.png" alt=""></div>
      <div class="sh-stall">${basket(false, '<span class="sh-price done">已拥有</span>', 'data-shop-egg="0" data-shop-focus="chicken-kitchen"', '鸡蛋，已拥有')}${basket(true, state.duck ? '<span class="sh-price done">已拥有</span>' : `<span class="sh-price${duckShort ? ' short' : ''}">${COIN}2,500</span>`, 'data-shop-egg="1" data-shop-focus="duck-details"', state.duck ? '鸭蛋，已拥有' : '鸭蛋，2,500')}</div>
      <button type="button" class="sh-wide" data-shop-kitchen-upgrade data-shop-focus="kitchen-upgrade" aria-label="${k.maxed ? '厨房已是最好' : `厨房升级到 Lv.${k.targetLevel}`}">
        <span class="sh-wide-art" data-visual><img src="/web/art/golden-ui/blueprint.png" alt=""></span>
        <span class="sh-wide-body"><b>${k.maxed ? '厨房 满级' : '厨房升级'}</b><span class="sh-levels" aria-hidden="true">${pips}</span><span class="sh-tag">${k.maxed ? '<span class="sh-price done">已是最好</span>' : `<span class="sh-price${state.cp < k.cost ? ' short' : ''}">${COIN}${formatCP(k.cost)}</span>`}</span></span>
        ${k.maxed ? '' : `<em class="sh-wide-next">Lv.${k.targetLevel}</em>`}
      </button>
      <button type="button" class="sh-wide" data-shop-activity="" data-shop-focus="activities" aria-label="邻里委托，特别原料">
        <span class="sh-wide-art" data-visual><span class="sh-gift">${[68,69,70].map(id => ingredientPortrait(id)).join('')}</span></span>
        <span class="sh-wide-body"><b>邻里委托</b><span class="sh-tag"><span class="sh-price">特别原料</span></span></span>
      </button>
    </div>`;
  }

  // ---- drawers --------------------------------------------------------------
  function toolDrawer(state, id) {
    const item = E.tool(id), owned = state.toolLevels[id], allowed = E.canBuyTool(state, id), reason = toolLockReason(state, id), name = E.label(item);
    // 配套调味料: what each level opens in the 调味料 shelf (duck-only ones once duck eggs are open), one more table row
    const supply = cookwareSeasonings(id).filter(x => !x.duck || state.duck);
    const opens = level => {const list = supply.filter(x => x.level === level);return `<span class="sh-col-v sh-col-ing${owned >= level ? '' : ' locked'}" aria-label="Lv.${level + 1} 开放${escapeHTML(list.map(x => E.label(E.ingredient(x.id))).join('、') || '无')}">${list.map((x, i) => `<span class="sh-supply-ing${i ? ' extra' : ''}">${ingredientPortrait(x.id)}</span>`).slice(0, 2).join('')}${list.length > 2 ? `<small class="more">+${list.length - 2}</small>` : ''}${list.length > 1 ? `<small class="more-narrow">+${list.length - 1}</small>` : ''}${list.length ? '' : '<small>—</small>'}</span>`;};
    const col = level => `<div class="sh-col${owned === level ? ' now' : owned > level ? ' done' : ''}" role="cell"><span class="sh-col-art">${toolPortrait(id, level)}</span><b class="sh-col-lv">Lv.${level + 1}</b><span class="sh-col-tag">${owned === level ? '持有' : owned > level ? '已过' : ' '}</span><span class="sh-col-v"><img src="/web/art/golden-journey/precision-clock.png" alt="">${item[`lv_${level}_min`]}分</span><span class="sh-col-v">${COIN}${formatCP(item[`lv_${level}_cook_cp`])}</span><span class="sh-col-v buy"><img src="/web/art/golden-journey/pouch.png" alt="">${formatCP(item[`lv_${level}_buy_cp`])}</span>${supply.length ? opens(level) : ''}</div>`;
    const next = owned + 1, price = owned < 2 ? item[`lv_${next}_buy_cp`] : 0;
    const labels = `<div class="sh-rowlabels" aria-hidden="true"><span></span><span></span><span></span><span>调理</span><span>每锅</span><span>${owned < 0 ? '价格' : '升级'}</span>${supply.length ? '<span>配套</span>' : ''}</div>`;
    const note = owned >= 2 ? '三级都有了' : reason || (state.cp < price ? `还差 ${formatCP(price - state.cp)}` : `买后下一锅起生效`);
    const eggNote = id === EXPANSION.toolId ? `<p class="sh-egg-note">${kitEgg(false)}只蒸鸡蛋</p>` : '';
    return {title:name, label:`${name}成长册`, body:`<div class="sh-compare${supply.length ? ' has-supply' : ''}" role="table" aria-label="${escapeHTML(name)}三级对比">${labels}${[0,1,2].map(col).join('')}</div>${eggNote}
      <div class="gd-row" data-row>${owned < 2 ? kitChipHtml(`${COIN}${formatCP(price)}`, 'mini') : ''}${kitChip('', note, 'mini' + (reason || state.cp < price ? ' hot' : ''))}</div>
      <div class="gd-actions" data-row>${kitButton2('关闭', 'data-shop-drawer-close')}${kitButton(owned >= 2 ? '满级' : owned < 0 ? '买下' : `升 Lv.${next + 1}`, `data-shop-buy-tool="${id}" data-shop-detail-tool="${id}"${allowed ? '' : ' disabled'}`)}</div>`};
  }
  function ingredientDrawer(state, id) {
    const info = ingredientUnlockInfo(state, id), view = projectMaterial(state, id), item = E.ingredient(id);
    if (!view.known) return {title:'未辨认', body:`<div class="gd-head" data-row><span class="gd-face">${visualImage(unknownArt, 'unknown')}</span><span class="gd-note">先从寻访带回地区标本，在发现册免费辨认后就能买</span></div><div class="gd-actions" data-row>${kitButton('好', 'data-shop-drawer-close')}</div>`};
    const n = state.ingredients[id] ?? 0, name = E.label(item), uses = [...knownToolSeasonings(state)].filter(([, set]) => set.has(id)).map(([tool]) => tool);
    // The seasoning stands on a plank with its price tag (as on the shelf); what you hold sits in the pouch beside it.
    const shut = !info.special && !info.available;
    const head = `<div class="sh-show${shut ? ' locked' : ''}"><span class="sh-show-item"><span class="sh-show-art" data-visual>${ingredientPortrait(id)}</span><span class="sh-tag">${info.special ? '<span class="sh-price">委托赠品</span>' : shut ? '<span class="sh-price lock"><img src="/web/art/golden-journey/lock.png" alt="">待解锁</span>' : `<span class="sh-price">${COIN}${formatCP(item.buy_cp)}/份</span>`}</span></span><span class="sh-show-bag">${kitIcon.pouch}<b>${n}</b><small>包里有</small></span></div>`;
    const use = id === 18 ? '这一锅不会因为厨房变脏而生病' : id === 36 ? '这一锅成熟后不会烧焦' : '';
    const useRow = `${kitChip('',ingredientFlavor(id),'mini')}`+(uses.length ? `<div class="sh-uses" data-row><span>用在</span>${uses.map(t => `<span class="sh-use" title="${escapeHTML(E.label(E.tool(t)))}">${toolPortrait(t, Math.max(0, state.toolLevels[t]))}</span>`).join('')}</div>` : use ? `<span class="gd-note">${use}</span>` : '');
    const lore = id >= 75 && openMaterialLore ? kitButton2('见闻', 'data-shop-material-lore') : kitButton2('关闭', 'data-shop-drawer-close');
    if (info.special) return {title:name, body:`${head}${useRow}<div class="gd-actions" data-row>${kitButton2('关闭', 'data-shop-drawer-close')}${kitButton('去看委托', `data-shop-detail-ingredient="${id}" data-shop-activity="${id}"`)}</div>`};
    if (!info.available) {
      const groups = info.requirementGroups?.length ? info.requirementGroups : [{description:info.description, met:false}];
      // Each condition is drawn: the cookware at its level, the partner to meet, the count to reach, the kitchen level.
      const pic = c => c.kind === 'tool' ? toolPortrait(c.id, Math.max(0, c.level)) : c.kind === 'kitchen' ? kitIcon.house : c.kind === 'collected' ? kitIcon.chick : c.kind === 'regional' ? kitIcon.glass : c.kind === 'discovery' && (state.total?.[`${c.egg}:${c.id}`] ?? 0) > 0 && characterPortrait ? characterPortrait(c.egg, c.id) : visualImage(unknownArt, 'unknown');
      const tool = groups.flatMap(g => g.conditions ?? []).find(c => c.kind === 'tool' && !c.met);
      const conds = `<div class="sh-unlock">${groups.length > 1 ? '<span class="sh-conds-head">满足任意一条就开放</span>' : ''}${groups.map(g => `<div class="sh-unlock-row${g.met ? ' met' : ''}">${(g.conditions?.length ? g.conditions : [{description:g.description, met:g.met}]).map(c => `<span class="sh-unlock-need${c.met ? ' met' : ''}"><span class="sh-unlock-art" data-visual>${c.kind ? pic(c) : kitIcon.glass}</span><b>${escapeHTML(c.description)}</b>${c.met ? kitArt('ic-check', 'sh-unlock-mark') : '<img class="sh-unlock-mark" src="/web/art/golden-journey/lock.png" alt="未达成">'}</span>`).join('')}</div>`).join('')}</div>`;
      return {title:name, body:`${head}${conds}${useRow}<div class="gd-actions" data-row>${lore}${tool ? kitButton(`去看${E.label(E.tool(tool.id))}`, `data-shop-goto-tool="${tool.id}"`) : kitButton('待解锁', `data-shop-detail-ingredient="${id}" disabled`)}</div>`};
    }
    const room = materialRoom(state), afford = Math.floor(state.cp / Math.max(1, item.buy_cp)), max = Math.max(0, Math.min(room, afford));
    amount = Math.max(max ? 1 : 0, Math.min(amount, max));
    const quick = [...new Map([[1, '1 份'], [5, '5 份'], [10, '10 份'], [max, '装满']].filter(([v]) => v > 0 && v <= max)).entries()];
    const picker = max ? `<div class="gd-qty" data-row><button type="button" class="gd-round minus" data-shop-step="-1" aria-label="少买" ${amount > 1 ? '' : 'disabled'}></button><output class="gd-big" aria-live="polite">${amount}<small>份</small></output><button type="button" class="gd-round plus" data-shop-step="1" aria-label="多买" ${amount < max ? '' : 'disabled'}></button></div>
      <div class="gd-coins" data-row>${quick.map(([v, l]) => `<button type="button" class="gd-coinwrap${v === amount ? ' on' : ''}" data-shop-set="${v}" aria-pressed="${v === amount}"><span class="gd-coin">${v}</span><b>${l}</b></button>`).join('')}</div>
      <div class="gd-row" data-row>${kitChipHtml(`共${COIN}${formatCP(amount * item.buy_cp)}`, 'mini')}</div>`
      : `<span class="gd-note">${room ? `还差 ${formatCP(item.buy_cp - state.cp)} CP` : `材料包满了（${materialCapacity(state)} 份）`}</span>`;
    return {title:name, body:`${head}${useRow}${picker}<div class="gd-actions" data-row>${lore}${kitButton('买下', `data-shop-buy-ingredient="${id}" data-shop-detail-ingredient="${id}"${max ? '' : ' disabled'}`)}</div>`};
  }
  function eggDrawer(state, egg) {
    if (egg === 0 || state.duck) return {title:egg ? '鸭蛋' : '鸡蛋', body:`<div class="gd-head" data-row><span class="gd-face">${kitEgg(egg === 1)}</span><div class="gd-row">${kitChip('', '已拥有', 'soft')}</div></div><span class="gd-note">在厨房的蛋篮里换蛋种</span><div class="gd-actions" data-row>${kitButton2('关闭', 'data-shop-drawer-close')}${kitButton('去厨房', 'data-shop-kitchen')}</div>`};
    const unlock = E.duckUnlockInfo(state);
    return {title:'鸭蛋', body:`<div class="gd-head" data-row><span class="gd-face">${kitEgg(true)}</span><div class="gd-row">${kitChipHtml(`${COIN}2,500`, 'soft')}${kitChip('', `${DATA.characters[1].length} 种鸭宝`, 'soft')}</div></div><span class="gd-note">${unlock.available ? '永久开放，下一锅起可以换成鸭蛋' : escapeHTML(unlock.reason)}</span><div class="gd-actions" data-row>${kitButton2('关闭', 'data-shop-drawer-close')}${kitButton('开放', `data-shop-duck${unlock.available ? '' : ' disabled'}`)}</div>`};
  }
  function drawerMarkup(state) {
    if (!drawer) return '';
    const d = drawer.kind === 'tool' ? toolDrawer(state, drawer.id) : drawer.kind === 'ingredient' ? ingredientDrawer(state, drawer.id) : eggDrawer(state, drawer.id);
    return `<div class="kp-scrim" data-shop-drawer-close></div><section class="kp-drawer gd sh-drawer" role="dialog" aria-modal="true" aria-label="${escapeHTML(d.label ?? d.title)}"><div class="kp-drawer-title" data-art="plank"><span data-safe><b>${escapeHTML(d.title)}</b></span></div><div class="kp-drawer-body">${d.body}</div></section>`;
  }

  // ---- buying ---------------------------------------------------------------
  function requestTool(id) {
    const state = getState();
    if (!E.canBuyTool(state, id)) { alertBox(toolLockReason(state, id)); return; }
    const tool = E.tool(id), level = state.toolLevels[id], targetLevel = level + 1;
    const price = tool[`lv_${targetLevel}_buy_cp`], minutes = tool[`lv_${targetLevel}_min`];
    const duration = level < 0 ? `${minutes} 分钟` : `${tool[`lv_${level}_min`]} → ${minutes} 分钟`;
    confirmBox(`${E.label(tool)} ${level < 0 ? 'Lv.' : '升到 Lv.'}${targetLevel + 1}\n调理 ${duration}\n花 ${formatCP(price)} CP${level < 0 ? '' : '，下一锅起生效'}`, () => {let done=false;if(act(() => {
      const current = getState();
      if (current.toolLevels[id] !== level) throw Error('厨具等级已变化，请重新确认。');
      E.buyTool(current, id);
      sound(7);done=true;
    })&&done){openShop();notify(`${level < 0 ? '买到了' : '升级了'} ${E.label(tool)} Lv.${targetLevel + 1} · 调理 ${minutes} 分钟`);}},false,{title:level < 0 ? '购买' : '升级',yes:level < 0 ? '买下' : '升级',no:'再想想'});
  }
  function requestIngredient(id) {
    const unlock = ingredientUnlockInfo(getState(),id);
    if (!unlock.available) { openIngredientDetails(id); return; }
    const item = E.ingredient(id), quantity = Math.max(1, amount);
    let purchased = false;
    const state=getState(),price=item.buy_cp*quantity,rebate=price*effects(state).rebate+state.progress.trade.rebateRemainder,refund=Math.floor(rebate/100);
    confirmBox(`${E.label(item)} ×${quantity}\n花 ${formatCP(price)} CP${refund?`，返还 ${refund} CP`:''}`, () => {let actualRefund=0;if(act(() => {
      if (purchased) return;
      const current = getState();
      const before=current.cp;
      E.buyIngredient(current, id, quantity);
      actualRefund=current.cp-before+price;
      purchased = true;
      sound(7);
    })&&purchased){amount=1;openShop();notify(`买到了 ${E.label(item)} ×${quantity} · 现有 ${getState().ingredients[id]} 份`);if(actualRefund>0)skillFeedback([{id:'TRADE-1',text:'材料返利 +'+actualRefund+' CP'}]);}},false,{title:'购买',yes:'买下',no:'再想想'});
  }
  function requestDuck() {
    const unlock=E.duckUnlockInfo(getState());
    if(unlock.owned){changePage(0);return;}
    if(!unlock.available){alertBox(unlock.reason);return;}
    let purchased=false;
    confirmBox('永久开放鸭蛋\n花 2,500 CP，下一锅起可以选鸭蛋',()=>{if(act(()=>{
      if(purchased)return;
      E.buyDuck(getState());purchased=true;sound(7);
    })&&purchased){openShop();notify('鸭蛋开放了！收完这一锅后，在厨房换成鸭蛋。');}},false,{title:'鸭蛋',yes:'开放',no:'再想想'});
  }

  // ---- page -----------------------------------------------------------------
  function openShop() {
    const oldScreen = find('.shop-screen');
    const active = panels.ownerDocument?.activeElement;
    const focusKey = oldScreen?.contains(active) ? active?.dataset?.shopFocus : null;
    rememberScroll();
    const state = getState(), count = inventoryCount(state), cap = materialCapacity(state);
    const wallet = kitChipHtml(`${COIN}<b>${formatCP(state.cp)}</b>`, 'sh-wallet');
    let body;
    if (shopTab === 0) body = `${counter(state)}<div class="sh-shelf big">${toolTiles(state).join('')}</div>${openJournal ? `<div class="gd-row">${kitButton2('寻宝日历', 'data-shop-journal')}</div>` : ''}`;
    else if (shopTab === 1) body = `${counter(state)}${seasoningShelf(state)}`;
    else body = `${counter(state)}${otherPage(state)}`;
    const page = `${kitTabs(tabs.map((label, i) => ({label, attrs:`data-shop-tab="${i}" data-shop-focus="tab-${i}"`, on:i === shopTab})), '商品分类')}${kitSheet(body, '', '', '', {cls:'shop-shelf-scroll', attrs:`data-shop-scroll-tab="${shopTab}"${shopTab === 1 ? ' data-list' : ''}`})}${drawerMarkup(state)}`;
    showPanel('鸡宝小卖部', page, 'screen-panel shop-screen', {skin:'kitchen', icon:spriteMarkup(uiIcon(7))});
    const screen = find('.shop-screen');
    const close = screen.querySelector('.close');
    if (close) { close.setAttribute('aria-label', '离开商店'); close.onclick = () => { drawer = null; returnFromShop(); }; }
    const scroller = screen.querySelector('.shop-shelf-scroll');
    scroller.scrollTop = scrollPositions[shopTab];
    scroller.addEventListener('scroll', () => { scrollPositions[Number(scroller.dataset.shopScrollTab)] = scroller.scrollTop; }, { passive: true });
    const all = (q, f) => screen.querySelectorAll(q).forEach(f);
    all('[data-shop-tab]', button => { button.onclick = () => { setTab(Number(button.dataset.shopTab)); sound(3); openShop(); }; });
    all('[data-shop-tool-details]', button => { button.onclick = () => { sound(4); drawer = {kind:'tool', id:Number(button.dataset.shopToolDetails)}; openShop(); }; });
    all('[data-shop-goto-tool]', button => { button.onclick = () => { sound(4); setTab(0); drawer = {kind:'tool', id:Number(button.dataset.shopGotoTool)}; openShop(); }; });
    all('[data-shop-ingredient-details]', button => { button.onclick = () => { sound(4); amount = 1; drawer = {kind:'ingredient', id:Number(button.dataset.shopIngredientDetails)}; openShop(); }; });
    all('[data-shop-egg]', button => { button.onclick = () => { sound(4); drawer = {kind:'egg', id:Number(button.dataset.shopEgg)}; openShop(); }; });
    all('[data-shop-season]', button => { button.onclick = () => { seasonFilter = button.dataset.shopSeason; scrollPositions[1] = 0; sound(3); openShop(); }; });
    find('[data-shop-flavor]')?.addEventListener('change',event=>{seasonFlavor=event.target.value;scrollPositions[1]=0;openShop();});
    screen.querySelector('[data-shop-locked-toggle]')?.addEventListener('click', () => { showLocked = !showLocked; sound(3); openShop(); });
    all('[data-shop-drawer-close]', button => { button.onclick = () => { drawer = null; sound(2); openShop(); }; });
    all('[data-shop-buy-tool]', button => { button.onclick = () => requestTool(Number(button.dataset.shopBuyTool)); });
    all('[data-shop-buy-ingredient]', button => { button.onclick = () => requestIngredient(Number(button.dataset.shopBuyIngredient)); });
    all('[data-shop-step]', button => { button.onclick = () => { amount += Number(button.dataset.shopStep); openShop(); }; });
    all('[data-shop-set]', button => { button.onclick = () => { amount = Number(button.dataset.shopSet); openShop(); }; });
    all('[data-shop-activity]', button => { button.onclick = () => { rememberScroll(); drawer = null; openActivities(button.dataset.shopActivity === '' ? undefined : Number(button.dataset.shopActivity)); }; });
    screen.querySelector('[data-shop-material-lore]')?.addEventListener('click', () => openMaterialLore(drawer.id));
    screen.querySelector('[data-shop-journal]')?.addEventListener('click', () => { rememberScroll(); openJournal('recipes'); });
    all('[data-shop-kitchen]', button => { button.onclick = () => { drawer = null; changePage(0); }; });
    screen.querySelector('[data-shop-duck]')?.addEventListener('click', requestDuck);
    screen.querySelector('[data-shop-kitchen-upgrade]')?.addEventListener('click', () => { sound(4); openKitchenUpgrade(); });
    if (focusKey) [...screen.querySelectorAll('[data-shop-focus]')].find(button => button.dataset.shopFocus === focusKey && !button.disabled)?.focus({ preventScroll: true });
  }
  // Details open as drawers over the right shelf.
  function openToolDetails(id) { if (!E.tool(id)) return; rememberScroll(); shopTab = 0; drawer = {kind:'tool', id}; openShop(); }
  function openIngredientDetails(id) { if (!E.ingredient(id)) return; rememberScroll(); shopTab = 1; amount = 1; drawer = {kind:'ingredient', id}; openShop(); }

  return { openShop, setTab, openToolDetails, openIngredientDetails, openRecipes:id=>{rememberScroll();openRecipeBook({tool:8,key:id===undefined?null:`0:${id}`});} };
}
