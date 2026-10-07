// Business presentation only. Quantities, prices and reservations stay in business.js.
import {characterImage} from './catalog.js';
import {GOLDEN_PORTRAITS} from './golden-portrait-metrics.js';
import {interfaceIcon} from './ui-icons.js';
import {kitBar,kitChipHtml,kitIcon} from './ui-kit.js';

const esc=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const image=(name,cls='')=>`<img class="bs-art ${cls}" src="/web/art/golden-business/${name}.png" alt="" draggable="false">`;

function portrait(row,characterPortrait){
  const path=characterImage(row.egg,row.id),m=GOLDEN_PORTRAITS[path];
  // Reuse only source alpha bounds, never collection white edges or optical sizes.
  return m?`<svg class="bs-source-character" viewBox="${m.bounds.join(' ')}" aria-hidden="true"><image href="${path}" width="${m.size[0]}" height="${m.size[1]}"/></svg>`:characterPortrait(row.egg,row.id);
}
function vessel(row,characterPortrait,wide,active){
  const type=row?.egg===1?'tray':'basket';
  return `<button class="bs-vessel ${type} ${wide?'is-wide':''} ${row?'':'is-empty'}" data-business-sheet="${active?'ledger':'basket'}" ${row?`data-business-stock-key="${row.key}"`:''} aria-label="${row?`${esc(row.name)}，${active?'剩余':'备货'}${row.quantity}只，${active?'查看账单':'调整备货'}`:'摆上出品'}">
    ${image(type+'-base','bs-vessel-base')}${row?`<span class="bs-character">${portrait(row,characterPortrait)}</span>`:'<span class="bs-empty-mark" aria-hidden="true">＋</span>'}${image(type+'-front','bs-vessel-front')}
    <span class="bs-nameplate ${row&&row.name.length>=4?'long-name':''}">${row?`<span class="bs-label-name">${esc(row.name)}</span><span class="bs-quantity">×${row.quantity}</span>`:'摆上出品'}</span></button>`;
}

export function businessGoldenMarkup({model,state,menuName,stockRows,ready,characterPortrait,menuCard=null,forecast=null,whyNote=false}){
  const active=!!model.active,rows=active?model.active.stock.filter(r=>r.quantity>0):stockRows;
  const count=rows.reduce((n,r)=>n+r.quantity,0),few=rows.length<=3;
  const vessels=rows.length?rows.map((r,i)=>vessel(r,characterPortrait,rows.length%2===1&&i===rows.length-1,active)).join(''):vessel(null,characterPortrait,true,false);
  // While open, the menu card shows the shop's live progress instead of the stock slots.
  const live=active?state.expansion.business.active:null,sold=active?model.active.sold:0,income=live?live.baseCP+live.markupCP+live.themeCP:0;
  const liveBlock=active?`<div class="bs-live" data-row><span class="bs-live-n"><b>${sold}</b>/${sold+count}<small>已售</small></span>${kitBar(sold+count?sold/(sold+count)*100:0,'已售')}</div><div class="gd-row bs-live-chips">${kitChipHtml(`${kitIcon.clock}<span data-business-next>…</span>`,'mini')}${kitChipHtml(`${kitIcon.coin}+${Number(income).toLocaleString('zh-CN')}`,'mini')}</div>`:'';
  const props=[['orders','订单'],['regulars','常客'],['projects','项目']].map(([id,label])=>`<button class="bs-prop" data-trade-view="${id}">${image(id==='regulars'?'regulars-original':id)}<span>${label}</span></button>`).join('');
  return `<header class="bs-header"><span class="bs-wallet" aria-label="${Number(state.cp).toLocaleString('zh-CN')} CP">${image('v2-wallet','bs-wallet-base')}${image('coin','bs-coin')}<strong>${Number(state.cp).toLocaleString('zh-CN')}</strong></span><h1>${interfaceIcon('shop')}<span>生意</span></h1><button class="game-help" data-business-sheet="help" aria-label="营业帮助">?</button><button class="game-settings" data-open-settings aria-label="设置">${interfaceIcon('settings')}</button></header>
    <div class="bs-main-scroll scroll"><div class="bs-main">
      <header class="bs-shop-header is-simple"><button class="bs-status" data-business-sheet="${active?'ledger':'stock'}" aria-label="${active?'营业中，查看营业情况':'待开张，调整备货'}">${image(active?'sign-open':'sign-prepare')}<span>${active?'营业中':model.access.met?'待开张':'筹备中'}</span></button>
      <div class="bs-menu-card"><div class="bs-menu-card-head"><h3>${esc(active?model.active.name:menuName)}</h3>${menuCard?.stars??''}</div>${menuCard?.slots?`<div class="bs-menu-slots">${menuCard.slots}</div>`:''}${liveBlock}
      <button class="bs-menu" data-business-sheet="menu" aria-label="${active?'查看本单菜单':'换菜单'}">${image('menu-easel')}<span>${active?'菜单':'换'}</span></button></div></header>
      <div class="bs-counter ${few?'few':'many'} ${rows.length===0?'empty-counter':''}" aria-label="${active?'实际在售出品':'开张前备货'}">${vessels}</div>
      <div class="bs-ledge" aria-hidden="true" data-bleed>${image('v2-counter')}</div>
      ${whyNote&&!active?'<button class="bs-why" data-business-sheet="why">其余伙伴在家只有 1 只</button>':''}
      ${active?'':`<div class="bs-summary" role="status">${image('v2-summary')}<span>${count?`<b>${count}</b>只 · 约${forecast?.hours??Math.ceil(count/6)*2}小时${forecast?` · <img class="bs-coin-inline" src="/web/art/golden-business/coin.png" alt="">+${forecast.income}`:''}`:'点货篮摆上出品'}</span></div>`}
      <nav class="bs-props" aria-label="其他生意事项">${props}</nav>
      <div class="bs-actions"><button class="bs-main-action" ${active?'data-business-sheet="ledger"':ready?'data-business-open data-next':'data-business-sheet="stock"'}>${image('v2-action')}<span>${active?'查看账单':count?'开张':'摆上出品'}</span></button>${active?'<button type="button" class="bs-secondary gd-btn2" data-business-close>收摊</button>':model.report?'<button class="bs-secondary" data-business-tab="report">上单账单</button>':''}</div>
    </div></div>`;
}
