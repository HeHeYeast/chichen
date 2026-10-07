// 生意主页 markup (2026-10-07, loop design batch 3). One page you can play from: 今日营业 on top (the wood sign, the
// menu, its core partners in their baskets on the counter, the forecast and 开张), the order board (2–3 slips: what
// each still needs, its reward and the one next step), then two small cards for 常客 and 项目. Words stay on the
// second level (the order's request, stories, rules); the page shows faces, counts, coins and one button each.
// Data: business-home.js. Order commands: order-delivery.js (one 交付 / 摆出来). Shared with the 「全部订单」 sheet.
import {characterImage} from './catalog.js';
import {GOLDEN_PORTRAITS} from './golden-portrait-metrics.js';
import {interfaceIcon} from './ui-icons.js';
import {kitBar,kitIcon} from './ui-kit.js';
import {CONTENT_TEXT,materialById} from './content-registry.js';
import {journeyMaterial} from './journey-art.js';

const esc=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const number=v=>Number(v??0).toLocaleString('zh-CN');
const art=(name,cls='')=>`<img class="bs-art ${cls}" src="/web/art/golden-business/${name}.png" alt="" draggable="false">`;
const COIN='<img class="bh-coin" src="/web/art/golden-business/coin.png" alt="">';
export const INTEL_ART='/web/art/golden-journey/envelope.png';
const INTEL=`<img class="bh-intel-art" src="${INTEL_ART}" alt="">`;
export const refKey=ref=>`${ref.kind}:${ref.id}`;
export const parseRef=text=>{const i=text.indexOf(':');return {kind:text.slice(0,i),id:text.slice(i+1)};};

function face(key,characterPortrait,cls=''){
  if(!key)return `<span class="bh-face ${cls}"><span class="bk-q" aria-hidden="true">?</span></span>`;
  const [egg,id]=key.split(':').map(Number),path=characterImage(egg,id),m=GOLDEN_PORTRAITS[path];
  const pic=m?`<svg viewBox="${m.bounds.join(' ')}" aria-hidden="true"><image href="${path}" width="${m.size[0]}" height="${m.size[1]}"/></svg>`:characterPortrait(egg,id);
  return `<span class="bh-face ${cls}">${pic}</span>`;
}

// One basket on the counter: a core menu partner. ✓ when enough can be put out, else have/need, dimmed; 「做」 when the
// kitchen can make it now; 「?」 for a partner not met yet.
function basket(slot,characterPortrait,{active=false,count=null}={}){
  const state=slot.ok?'ok':slot.known?'need':'unknown',tag=active?`×${count}`:slot.ok?'✓':`${slot.have}/${slot.need}`;
  const label=active?`${slot.name}，还剩 ${count} 只`:slot.ok?`${slot.name}，够了`:slot.known?`${slot.name}，还差 ${slot.need-slot.have} 只${slot.makeable?'，去做':''}`:'还不认识的伙伴，看线索';
  return `<button type="button" class="bh-basket is-${state}" data-business-slot="${slot.key}" aria-label="${esc(label)}">${art('basket-base','bh-basket-base')}
    <span class="bh-basket-face">${slot.known?face(slot.key,characterPortrait):'<span class="bh-face"><span class="bk-q" aria-hidden="true">?</span></span>'}</span>${art('basket-front','bh-basket-front')}
    <b class="bh-basket-tag">${esc(tag)}</b>${slot.makeable&&!active?'<i class="bh-make" aria-hidden="true">做</i>':''}</button>`;
}

export function todayMarkup({core,model,live,stars,characterPortrait,canOpen}){
  // legacy: a shop opened before the 25% rule (1.5.17 or older) finishes on its old, smaller menu markup; no percent on its sign
  const active=!!model.active,legacy=model.active?.rulesVersion===1;
  const slots=active?core.slots.filter(x=>(live.stock[x.key]??0)>0||live.initialStock[x.key]):core.slots;
  const baskets=slots.length?slots.map(x=>basket(x,characterPortrait,{active,count:active?live.stock[x.key]??0:null})).join(''):`<p class="bh-empty">${active?'这一单没摆菜单要的伙伴':'点「换」挑一张菜单'}</p>`;
  let foot;
  if(active){
    const sold=model.active.sold,left=Object.values(live.stock).reduce((a,b)=>a+b,0);
    // open: how much has sold, the next round, the money so far; 收摊 lives in the 账单 page
    foot=`<div class="bh-live" data-row><span class="bh-live-n"><b>${sold}</b>/${sold+left}</span>${kitBar(sold+left?sold/(sold+left)*100:0,'已售')}<span class="bh-live-cp">${COIN}+${number(model.active.income)}</span></div>
      <div class="bh-actions is-live" data-row><span class="gd-chip mini">${kitIcon.clock}<span data-business-next>…</span></span><button type="button" class="gd-btn" data-business-sheet="ledger"><span data-safe>账单</span></button></div>`;
  }else{
    const gap=core.missing&&core.makeable?`<button type="button" class="gd-btn2 bh-gap" data-business-gap>还差 ${core.missing} 只 · 去做</button>`:'';
    foot=`<div class="bh-chips" data-row>${core.count?`<span class="gd-chip mini">${COIN}+${number(core.income)}</span>`:''}${core.extra?`<span class="gd-chip mini">另 ${core.extra} 只一起卖</span>`:''}</div>
      <div class="bh-actions" data-row>${gap}<button type="button" class="gd-btn" ${canOpen?'data-business-open data-next':'data-business-sheet="stock"'}><span data-safe>${core.count?'开张':'摆货'}</span></button></div>`;
  }
  // idle: the last receipt stays one tap away, under the sign
  const last=!active&&model.report?`<button type="button" class="bh-last" data-business-tab="report" aria-label="看上一单账单">上单 +${number(model.report.income)}</button>`:'';
  return `<section class="bh-today${active?' is-open':''}" aria-label="今日营业">
    <div class="bh-today-head"><div class="bh-sign-col"><button type="button" class="bh-sign" data-business-sheet="${active?'ledger':'stock'}" aria-label="${active?'营业中，看账单':'待开张，看全部出品'}">${art(active?'sign-open':'sign-prepare')}<span>${active?'营业中':'待开张'}</span></button>${last}</div>
      <div class="bh-menu-name"><h2>${esc(core.name)}</h2><span class="bh-bonus${core.complete?' is-on':''}">${active?'':stars}<b>${core.complete?'已凑齐':'凑齐'}${legacy?'':' +25%'}</b></span></div>
      <button type="button" class="bs-menu bh-easel" data-business-sheet="menu" aria-label="${active?'看本单菜单':'换菜单'}">${art('menu-easel')}<span>${active?'菜单':'换'}</span></button></div>
    <div class="bh-counter" data-count="${slots.length}">${baskets}</div><div class="bh-ledge" aria-hidden="true">${art('v2-counter')}</div>
    <div class="bh-today-foot">${foot}</div></section>`;
}

// One order slip. The name opens the order's own sheet (request text, who fits, 略过); the button is the next step.
export function orderCardMarkup(card,characterPortrait){
  const key=refKey(card.ref),tag=card.type==='story'?`<span class="bh-tag is-story">第${card.chapter}章</span>`:card.regionName?`<span class="bh-tag">${esc(card.regionName)}</span>`:'';
  const needs=card.needs.map(n=>n.kinds
    ?`<span class="bh-need is-kinds${n.have>=n.quantity?' ok':''}"><span class="bh-faces">${n.keys.slice(0,3).map(k=>face(k,characterPortrait,'mini')).join('')}</span><i>${n.have}/${n.quantity} 种</i></span>`
    :`<span class="bh-need${n.have>=n.quantity?' ok':''}">${face(n.key,characterPortrait)}<span class="bh-need-copy"><b>${esc(n.label)}</b><i>${n.have}/${n.quantity}</i></span></span>`).join('');
  const rewards=`${card.cp?`<span class="bh-reward">${COIN}+${number(card.cp)}</span>`:''}${card.intel?`<span class="bh-reward is-intel">${INTEL}${esc(card.regionName)}情报</span>`:''}`;
  const button=card.state==='ready'?`<button type="button" class="gd-btn bh-act" data-order-act="${key}" data-next><span data-safe>${card.type==='display'?'摆出来':'交付'}</span></button>`
    :card.makeable?`<button type="button" class="gd-btn2 bh-act" data-order-make="${key}">${esc(card.gap)} · 去做</button>`
    :`<button type="button" class="gd-btn2 bh-act is-quiet" data-order-detail="${key}">${esc(card.gap||'看看')}</button>`;
  return `<article class="bh-order is-${card.state}" aria-label="${esc(card.name)}"><header class="bh-order-head"><button type="button" class="bh-order-name" data-order-detail="${key}">${esc(card.name)}</button>${tag}</header>
    <div class="bh-needs">${needs}</div><div class="bh-order-foot" data-row><span class="bh-rewards">${rewards}</span>${button}</div></article>`;
}

function smallCard(kind,summary){
  if(!summary)return '';
  const label=kind==='regulars'?'常客':'项目';
  return `<button type="button" class="bh-small" data-trade-view="${kind}">${art(kind==='regulars'?'regulars-original':'projects','bh-small-art')}
    <span class="bh-small-copy"><b>${label}<i>${summary.done}/${summary.total}</i></b><small>${esc(summary.line)}</small></span>${summary.dot?'<i class="bh-dot" aria-label="有新进展"></i>':''}</button>`;
}

export function businessHomeMarkup({state,core,model,live,board,regulars,projects,stars,characterPortrait,canOpen,access}){
  const header=`<header class="bs-header"><span class="bs-wallet" aria-label="${number(state.cp)} CP">${art('v2-wallet','bs-wallet-base')}${art('coin','bs-coin')}<strong>${number(state.cp)}</strong></span><h1>${interfaceIcon('shop')}<span>生意</span></h1><button class="game-help" data-business-sheet="help" aria-label="营业帮助">?</button><button class="game-settings" data-open-settings aria-label="设置">${interfaceIcon('settings')}</button></header>`;
  const today=access.met?todayMarkup({core,model,live,stars,characterPortrait,canOpen})
    :`<section class="bh-today is-closed" aria-label="今日营业"><div class="bh-today-head"><button type="button" class="bh-sign" data-business-sheet="stock" aria-label="筹备中，看还差什么">${art('sign-prepare')}<span>筹备中</span></button><div class="bh-menu-name"><h2>先做第一笔生意</h2></div></div><div class="bh-today-foot"><div class="bh-chips" data-row>${access.missing.map(x=>`<span class="gd-chip mini">${esc(x)}</span>`).join('')}</div></div></section>`;
  const orders=board.cards.length?board.cards.map(c=>orderCardMarkup(c,characterPortrait)).join(''):'<p class="bh-empty">营业、寻访或开火后，会有人来问</p>';
  return `${header}<div class="bs-main-scroll scroll"><div class="bh-main">${today}
    <div class="bh-label"><span>订单${board.ready?`<i class="bh-ready">${board.ready} 张能交</i>`:''}</span>${board.total>board.cards.length?`<button type="button" data-business-sheet="orders">全部 ${board.total} ›</button>`:''}</div>
    <div class="bh-orders">${orders}</div>
    <div class="bh-smalls" data-row>${smallCard('regulars',regulars)}${smallCard('projects',projects)}</div></div></div>`;
}

// The order's own sheet: its request in full, who could fill each need, the reward, and the same next step (+ 略过).
export function orderSheetMarkup({card,request,candidates,characterPortrait}){
  const key=refKey(card.ref);
  const rows=card.needs.map((n,i)=>`<div class="bh-sheet-need"><b>${esc(n.kinds?'不同模样':n.label)}</b><i>${n.have}/${n.quantity}${n.kinds?' 种':''}</i><span class="bh-faces">${(candidates[i]??[]).slice(0,4).map(k=>face(k,characterPortrait,'mini')).join('')}</span></div>`).join('');
  const reward=`${card.cp?`<span class="gd-chip">${COIN}+${number(card.cp)}</span>`:''}${card.intel?`<span class="gd-chip">${INTEL}${esc(card.regionName)}情报</span>`:''}${card.type==='display'?'<span class="gd-chip">看完都回家</span>':''}`;
  const main=card.state==='ready'?`<button type="button" class="gd-btn" data-order-act="${key}"><span data-safe>${card.type==='display'?'摆出来':'交付'}</span></button>`:card.makeable?`<button type="button" class="gd-btn" data-order-make="${key}"><span data-safe>去做</span></button>`:'';
  const second=card.ref.kind==='proposal'?`<button type="button" class="gd-btn2" data-order-skip="${key}">略过</button>`:card.ref.kind==='order'?`<button type="button" class="gd-btn2" data-order-cancel="${key}">不做了</button>`:card.type==='story'?`<button type="button" class="gd-btn2" data-order-story>读往事</button>`:'';
  return `<div class="bh-sheet"><div class="bh-sheet-head"><h3>${esc(card.name)}</h3>${card.regionName?`<span class="bh-tag">${esc(card.regionName)}</span>`:''}</div>${request?`<p class="bh-request">${esc(request)}</p>`:''}${rows}<div class="bh-chips" data-row>${reward}</div><div class="gd-actions" data-row>${second}${main}</div></div>`;
}

// What finishing an order brought: the money, then its 地区情报 — a partner's new clue (who, what, x/5 → y/5) or word
// of a special find in a region (where, what to bring); a region with nothing new to tell sends materials instead, and an
// order whose completion brings no 情报 (order-intel.js ORDER_INTEL) shows the money only.
export function orderDoneMarkup(result,characterPortrait){
  const intel=result.intel,money=result.income?`<div class="bh-done-cp" data-row>${COIN}<b>+${number(result.income)}</b><small>${result.bonusCP?`货款 ${number(result.income-result.bonusCP)} · 酬谢 ${number(result.bonusCP)}`:'货款'}</small></div>`:`<div class="bh-done-cp" data-row><small>${result.kind==='display'?'看完都回家了':''}</small></div>`;
  let line='';
  if(intel?.kind==='clue'&&intel.advance){const a=intel.advance;
    line=`<div class="jr-clue bh-done-intel"><span class="jr-sil">${a.silhouette?`<span class="unknown-silhouette" aria-hidden="true">${characterPortrait(a.egg,a.id)}</span>`:'<span class="bk-q" aria-hidden="true">?</span>'}</span><span class="jr-clue-copy"><b>${esc(a.code)}：${esc(a.text)}</b><small>调查 ${a.before}/5 → ${a.complete?'配方完整':`${a.after}/5`}${a.tracked?' · 追踪中':''}</small></span></div>`;}
  else if(intel?.kind==='place'&&intel.hint)line=`<div class="jr-find bh-done-intel"><span class="jr-find-art"><img src="${INTEL_ART}" alt=""></span><span class="jr-clue-copy"><b>${esc(intel.hint.regionName)}出现新的调查提示</b><small>${esc(intel.hint.text)}</small></span></div>`;
  else if(intel?.kind==='material')line=`<div class="jr-find bh-done-intel"><span class="jr-find-art"><img src="${journeyMaterial(intel.materialId)}" alt=""></span><span class="jr-clue-copy"><b>${esc(materialById[intel.materialId]?.title_zh_CN??'')} ×${intel.quantity}</b><small>${esc(intel.regionName)}这次没有新消息，送来了材料</small></span></div>`;
  else if(intel)line=`<p class="bh-done-none">${esc(intel.regionName)}这次没有新消息</p>`;
  const note=result.firstResult?`<small class="bh-done-note">记下「${esc(CONTENT_TEXT[result.firstResult]?.name??'')}」</small>`:'';
  const label=intel?.kind==='material'?`获得：${esc(intel.regionName)}的材料`:intel&&intel.kind!=='none'?`获得：${esc(intel.regionName)}情报`:'';
  return `<div class="bh-done"><b class="bh-done-name">完成「${esc(result.name)}」</b>${money}${note}${label?`<div class="gd-label">${label}</div>`:''}${line}</div>`;
}
