import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import { createShopUI, shopIngredientCatalog } from '../web/shop-ui.js';

const NOW = 1800000000000;

// A deliberately small, non-layout DOM adapter. It exposes the controls actually
// rendered by the shop and dispatches their real handlers; it does not simulate
// purchasing, inventory rules, or browser layout. No storage or network is used.
function controlDOM(html, ownerDocument) {
  const nodes = [...html.matchAll(/<([a-z][\w-]*)\b([^>]*)>/gi)].map(([, tag, attributes]) => {
    const attrs = Object.fromEntries([...attributes.matchAll(/([\w:-]+)(?:="([^"]*)")?/g)].map(([, name, value]) => [name, value ?? '']));
    const node = {
      tagName: tag.toUpperCase(), attrs, style: {}, scrollTop: 0,
      disabled: Object.hasOwn(attrs, 'disabled'),
      dataset: Object.fromEntries(Object.entries(attrs).filter(([key]) => key.startsWith('data-')).map(([key, value]) => [key.slice(5).replace(/-([a-z])/g, (_, letter) => letter.toUpperCase()), value])),
      setAttribute(name, value) { this.attrs[name] = value; },
      addEventListener(name, handler) { this[`on${name}`] = handler; },
      focus() { ownerDocument.activeElement = this; },
      click() { if (!this.disabled) this.onclick?.(); },
    };
    return node;
  });
  const matches = (node, selector) => {
    if (selector.startsWith('.')) return (node.attrs.class ?? '').split(/\s+/).includes(selector.slice(1));
    const match = selector.match(/^\[([\w-]+)(?:="([^"]*)")?\]$/);
    assert.ok(match, `DOM adapter does not support selector ${selector}`);
    return Object.hasOwn(node.attrs, match[1]) && (match[2] === undefined || node.attrs[match[1]] === match[2]);
  };
  return {
    contains: node => nodes.includes(node),
    querySelector: selector => nodes.find(node => matches(node, selector)) ?? null,
    querySelectorAll: selector => nodes.filter(node => matches(node, selector)),
  };
}

function shopHarness(initial = E.freshState(NOW)) {
  let state = initial, screen = null, pending = null, screenClass='',markup='',openedTool=null,activity=null,recipeRequest=null;
  const alerts = [];
  const ownerDocument = { activeElement: null };
  const panels = {
    ownerDocument,
    querySelector: selector => ['.shop-screen','.expansion-screen'].includes(selector) ? screenClass.split(' ').includes(selector.slice(1))?screen:null : screen?.querySelector(selector) ?? null,
    querySelectorAll: selector => screen?.querySelectorAll(selector) ?? [],
  };
  const ui = createShopUI({
    getState: () => state,
    panels,
    showPanel(_title, html,classes) { screenClass=classes;markup=html;screen = controlDOM(`<button class="close"></button>${html}`, ownerDocument); },
    confirmBox(message, callback) { pending = { message, callback }; },
    alertBox(message) { alerts.push(message); },
    act(callback) { try { callback(); } catch (error) { alerts.push(error.message); } },
    sound() {},
    toolPortrait() { return ''; },
    changePage() {},
    openCookware(id) { openedTool=id; },
    openActivities(id) { activity={ingredientId:id}; },
    openRecipeBook(options) { recipeRequest=options; },
  });
  function click(selector) {
    const button = screen?.querySelector(selector);
    assert.ok(button, `Shop did not render ${selector}`);
    assert.equal(button.disabled, false, `${selector} is disabled`);
    button.click();
  }
  return {
    ui,
    get recipeRequest(){return recipeRequest;},
    get state() { return state; },
    get markup() { return markup; },
    get openedTool() { return openedTool; },
    get activity() { return activity; },
    get screenClass() { return screenClass; },
    replaceState(next) { state = next; },
    alerts,
    open(tab = 0) { ui.setTab(tab); ui.openShop(); },
    click,
    control: selector=>screen?.querySelector(selector) ?? null,
    quantity(id, step) {
      const button = screen.querySelectorAll('[data-shop-quantity]').find(node => Number(node.dataset.shopQuantity) === id && Number(node.dataset.step) === step);
      assert.ok(button, `No quantity control for ingredient ${id}, step ${step}`);
      assert.equal(button.disabled, false, 'Quantity control is disabled');
      button.click();
    },
    confirmation() { assert.ok(pending, 'Expected a purchase confirmation'); return pending; },
    confirm() { const { callback } = this.confirmation(); pending = null; callback(); },
    cancel() { this.confirmation(); pending = null; },
  };
}

function upgradeState() {
  const state = E.freshState(NOW);
  state.kitchenLevel = 2;
  state.cp = 10000;
  return state;
}

test('cancelling either shop purchase leaves all game state unchanged', () => {
  for (const [tab, selector] of [[0, '[data-shop-buy-tool="1"]'], [1, '[data-shop-buy-ingredient="0"]']]) {
    const shop = shopHarness(), before = structuredClone(shop.state);
    shop.open(tab);
    shop.click(selector);
    assert.deepEqual(shop.state, before, 'Opening confirmation must not buy anything');
    shop.cancel();
    assert.deepEqual(shop.state, before);
    assert.deepEqual(shop.alerts, []);
  }
});

test('ingredient quantity controls buy the confirmed quantity at its actual price', () => {
  const shop = shopHarness();
  shop.open(1);
  shop.quantity(0, 1);
  shop.quantity(0, 1);
  shop.quantity(0, -1);
  shop.click('[data-shop-buy-ingredient="0"]');
  assert.match(shop.confirmation().message, /×2/);
  assert.equal(shop.state.cp, 600);
  assert.equal(shop.state.ingredients[0], 1);
  shop.confirm();
  assert.equal(shop.state.cp, 590, 'Two salt cost 10 CP');
  assert.equal(shop.state.ingredients[0], 3);
  assert.deepEqual(shop.alerts, []);
});

test('replaying a successful ingredient confirmation cannot charge twice', () => {
  const shop = shopHarness();
  shop.open(1);
  shop.click('[data-shop-buy-ingredient="0"]');
  const callback = shop.confirmation().callback;
  shop.confirm();
  const purchased = structuredClone(shop.state);
  assert.equal(purchased.cp, 595);
  assert.equal(purchased.ingredients[0], 2);
  callback();
  assert.deepEqual(shop.state, purchased);
  assert.deepEqual(shop.alerts, []);
});

test('tool upgrade charges once and preserves the batch already cooking', () => {
  const state = upgradeState();
  const batch = E.startBatch(state, 0, NOW, () => 0.5);
  const shop = shopHarness(state), before = structuredClone(state);
  shop.open();
  shop.click('[data-shop-buy-tool="0"]');
  const callback = shop.confirmation().callback;
  assert.deepEqual(state, before);
  shop.confirm();
  assert.equal(state.cp, before.cp - 1000);
  assert.equal(state.toolLevels[0], 1);
  assert.equal(state.batch, batch);
  assert.deepEqual(state.batch, before.batch);
  const purchased = structuredClone(state);
  callback();
  assert.deepEqual(state, purchased, 'Replaying must not buy the next tool tier');
  assert.match(shop.alerts.at(-1), /等级已变化/);
});

test('tool confirmation rejects a level changed by another purchase', () => {
  const shop = shopHarness(upgradeState());
  shop.open();
  shop.click('[data-shop-buy-tool="0"]');
  E.buyTool(shop.state, 0);
  const changed = structuredClone(shop.state);
  shop.confirm();
  assert.deepEqual(shop.state, changed);
  assert.match(shop.alerts.at(-1), /等级已变化/);
});

test('tool confirmation rechecks the latest balance before spending', () => {
  const shop = shopHarness(upgradeState());
  shop.open();
  shop.click('[data-shop-buy-tool="0"]');
  shop.state.cp = 999;
  const changed = structuredClone(shop.state);
  shop.confirm();
  assert.deepEqual(shop.state, changed);
  assert.match(shop.alerts.at(-1), /CP不足/);
});

test('tool confirmation rechecks kitchen unlock conditions from a replaced state', () => {
  const oldState = upgradeState(), shop = shopHarness(oldState);
  shop.open();
  shop.click('[data-shop-buy-tool="0"]');
  const latest = structuredClone(oldState);
  latest.kitchenLevel = 0;
  const unchangedOld = structuredClone(oldState), unchangedLatest = structuredClone(latest);
  shop.replaceState(latest);
  shop.confirm();
  assert.deepEqual(latest, unchangedLatest);
  assert.deepEqual(oldState, unchangedOld);
  assert.match(shop.alerts.at(-1), /尚未满足购买条件/);
});

test('ingredient confirmation updates the latest state without mutating the previous one', () => {
  const oldState = E.freshState(NOW), shop = shopHarness(oldState);
  shop.open(1);
  shop.quantity(0, 1);
  shop.click('[data-shop-buy-ingredient="0"]');
  const latest = structuredClone(oldState), unchangedOld = structuredClone(oldState);
  latest.cp = 100;
  latest.ingredients[0] = 7;
  shop.replaceState(latest);
  shop.confirm();
  assert.equal(latest.cp, 90);
  assert.equal(latest.ingredients[0], 9);
  assert.deepEqual(oldState, unchangedOld);
  assert.deepEqual(shop.alerts, []);
});

test('ingredient confirmation fails atomically when its quantity no longer fits', () => {
  const shop = shopHarness();
  shop.open(1);
  shop.quantity(0, 1);
  shop.click('[data-shop-buy-ingredient="0"]');
  E.buyIngredient(shop.state, 0, 28);
  const changed = structuredClone(shop.state);
  shop.confirm();
  assert.deepEqual(shop.state, changed);
  assert.match(shop.alerts.at(-1), /最多可持有30个/);
});

test('ingredient confirmation fails atomically if its balance becomes insufficient', () => {
  const shop = shopHarness();
  shop.open(1);
  shop.quantity(0, 1);
  shop.click('[data-shop-buy-ingredient="0"]');
  shop.state.cp = 9;
  const changed = structuredClone(shop.state);
  shop.confirm();
  assert.deepEqual(shop.state, changed);
  assert.match(shop.alerts.at(-1), /CP不足/);
});

test('ingredient confirmation rechecks availability after its prerequisite changes', () => {
  const state = E.freshState(NOW);
  E.buyTool(state, 1);
  const shop = shopHarness(state);
  shop.open(1);
  shop.click('[data-shop-buy-ingredient="1"]');
  const latest = E.freshState(NOW), unchanged = structuredClone(latest);
  shop.replaceState(latest);
  shop.confirm();
  assert.deepEqual(latest, unchanged);
  assert.match(shop.alerts.at(-1), /尚未解锁/);
});

test('shop exposes all nine tools and the steamer names its real discovery requirement',()=>{
  const state=E.freshState(NOW);state.kitchenLevel=1;
  const shop=shopHarness(state);shop.open();
  assert.match(shop.markup,/9 种厨具/);
  assert.match(shop.markup,/已发现 0 \/ 12 种伙伴/);
  assert.equal(shop.control('[data-shop-buy-tool="8"]').disabled,true);
  assert.equal(shop.control('[data-shop-recipes]'),null);
  for(let id=0;id<12;id++)state.total[`0:${id}`]=1;
  state.cp=3000;shop.open();shop.click('[data-shop-buy-tool="8"]');
  assert.match(shop.confirmation().message,/45 分钟/);shop.confirm();
  assert.equal(state.toolLevels[8],0);assert.equal(state.cp,0);
});

test('legacy steamer recipe navigation now forwards to the collection and never opens a separate shop book',()=>{
  const shop=shopHarness();shop.open();shop.ui.openRecipes(117);
  assert.equal(shop.control('[data-shop-recipes]'),null);
  assert.deepEqual(shop.recipeRequest,{tool:8,key:'0:117'});
});

test('the complete ingredient cabinet can filter all 83 records without hiding locked ingredients',()=>{
  const s=E.freshState(NOW),all=shopIngredientCatalog(s),available=shopIngredientCatalog(s,'available'),locked=shopIngredientCatalog(s,'locked');
  assert.equal(all.length,83);assert.equal(available.length+locked.length,83);assert.ok(locked.length>0);
  assert.deepEqual(available.map(r=>r.item.id).sort((a,b)=>a-b),E.availableIngredients(s).sort((a,b)=>a-b));
  const shop=shopHarness(s);shop.open(1);shop.click('[data-shop-filter="locked"]');assert.ok(shop.control('[data-shop-ingredient-details="68"]'));
});

test('ingredient search composes with unlock filters and returns a useful empty set',()=>{
  const s=E.freshState(NOW);for(const filter of ['all','available','locked']){
    const matches=shopIngredientCatalog(s,filter,'牛');assert.ok(matches.every(r=>[r.item.title_zh_CN,r.item.title_zh_TW].join(' ').includes('牛')));
    assert.deepEqual(shopIngredientCatalog(s,filter,'这是一份不存在的调味料'),[]);
  }
});

test('locked ingredient details explain the current gate and return to the chosen filter',()=>{
  const shop=shopHarness();shop.open(1);shop.click('[data-shop-filter="locked"]');shop.ui.openIngredientDetails(50);
  assert.match(shop.markup,/待开放|条件|开放/);assert.equal(shop.control('[data-shop-detail-ingredient]').disabled,true);
  shop.click('[data-shop-detail-back]');assert.equal(shop.control('[data-shop-filter="locked"]').attrs['aria-pressed'],'true');
});

test('special ingredients lead to their activity, never to a free purchase confirmation',()=>{
  const shop=shopHarness();shop.open(1);shop.ui.openIngredientDetails(68);
  const action=shop.control('[data-shop-detail-ingredient]');assert.ok(action);action.click();assert.deepEqual(shop.activity,{ingredientId:68});
  assert.equal(shop.control('[data-shop-buy-ingredient]'),null);
});

test('all original cookware can preview three levels before purchase without changing progress',()=>{
  const shop=shopHarness(),before=structuredClone(shop.state);
  for(let id=0;id<8;id++){shop.ui.openToolDetails(id);for(let level=1;level<=3;level++)assert.ok(shop.markup.includes('Lv.'+level));assert.deepEqual(shop.state,before);}
});

test('duck confirmation cancel, purchase and replay preserve selection and active batch',()=>{
  const s=E.freshState(NOW);s.cp=5000;E.startBatch(s,0,NOW,()=>.5);s.selected=[0];const shop=shopHarness(s);shop.open(2);const before=structuredClone(s);
  shop.click('[data-shop-duck]');shop.cancel();assert.deepEqual(s,before);
  shop.click('[data-shop-duck]');const replay=shop.confirmation().callback;shop.confirm();assert.equal(s.duck,true);assert.equal(s.cp,before.cp-2500);assert.deepEqual(s.batch,before.batch);assert.deepEqual(s.selected,before.selected);
  replay();assert.equal(s.cp,before.cp-2500);
});

test('duck confirmation rechecks current balance and ownership without charging a replacement save',()=>{
  for(const change of [s=>s.cp=0,s=>s.duck=true]){
    const initial=E.freshState(NOW);initial.cp=5000;const shop=shopHarness(initial);shop.open(2);shop.click('[data-shop-duck]');
    const replacement=structuredClone(initial);change(replacement);const before=structuredClone(replacement);shop.replaceState(replacement);shop.confirm();assert.deepEqual(shop.state,before);assert.equal(initial.duck,false);
  }
});
