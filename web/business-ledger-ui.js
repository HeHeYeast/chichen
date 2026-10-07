// Read-only receipt presentation: totals always come from the saved session/report.
import {resolveSpecies} from './content-registry.js';
import {reportVisitors} from './regular-model.js';
import {familyBasket,familyArt,familyStamp} from './business-family-art.js';
import {kitChip,kitChipHtml,kitIcon} from './ui-kit.js';
const esc=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const number=v=>Number(v??0).toLocaleString('zh-CN');
export function businessLedgerMarkup(state,model,settled){
  const r=settled?model.report:state.expansion.business.active;
  if(!r)return `<div class="family-empty">${familyArt('orders')}<h3>账页还是空的</h3><p>完成一次营业，这里就会留下记录。</p></div>`;
  const name=settled?r.name:model.active.name,total=settled?r.income:r.baseCP+r.markupCP+r.themeCP;
  const entries=Object.entries(r.soldByKey).filter(([,n])=>n>0).map(([key,quantity])=>{const c=resolveSpecies(key);return {egg:c.egg,id:c.id,name:c.title_zh_CN,quantity};});
  const visitors=reportVisitors(state,r);
  const lines=[['基础',r.baseCP],['招牌',r.markupCP],['菜单',r.themeCP],...(settled?[['奖励',r.bonusCP]]:[])].filter(([,n],i)=>i===0||n>0);
  const unsold=settled?Object.values(r.remainingStock).reduce((a,b)=>a+b,0):0;
  // One receipt that fits on one screen: who sold, the money as pills and one big total, then any visitors.
  const tags=[r.completeMenu||r.recordedComplete?kitChip('','完整菜单印','mini good'):r.validMenu||r.recordedValid?kitChip('','有效接待','mini good'):'',
    settled?kitChip('',r.reason==='manual'?'提前收摊':r.reason==='sold-out'?'出品售罄':'营业满 24 小时','mini'):kitChipHtml(`${kitIcon.clock}<span data-business-countdown></span>`,'mini'),
    settled&&unsold?kitChip('',`未售 ${unsold} 只回农场`,'mini'):'',!settled&&model.active.projectedBonus?kitChip('',`收摊另结 ${number(model.active.projectedBonus)}`,'mini'):''].filter(Boolean).join('');
  return `<header class="business-receipt-head family-receipt-head rc-head"><h3>${esc(name)}</h3><span class="rc-sold">已售 <b>${r.totalSold}</b> 只</span>${familyStamp(settled?'done':'active',settled?'已入账':'营业中')}</header>
    <div class="receipt-sold rc-sold-row gd-scroll-row" data-hscroll aria-label="本单实际售出的伙伴">${entries.map(row=>`<div class="rc-item">${familyBasket(row)}<b>×${row.quantity}</b></div>`).join('')||'<p class="rc-empty">还没有成交</p>'}</div>
    ${settled?'':`<div class="rc-left"><span class="rc-vlabel">还在卖</span><div class="rc-sold-row gd-scroll-row" data-hscroll>${model.active.stock.filter(x=>x.quantity>0).map(row=>`<div class="rc-item">${familyBasket(row)}<b>×${row.quantity}</b></div>`).join('')}</div></div>`}
    <div class="gd-row rc-lines business-receipt-lines">${lines.map(([label,n],i)=>kitChip('',`${label} ${i?'+':''}${number(n)}`,'mini')).join('')}</div>
    <div class="business-receipt-total business-totals rc-total"><span class="rc-total-label">${settled?'总收入':'已入账'}</span>${familyArt('coin')}<b>${number(total)}</b><small>CP</small></div>
    ${tags?`<div class="gd-row rc-tags">${tags}</div>`:''}
    ${visitors.length?`<section class="receipt-visitors rc-visitors" aria-label="来访的常客"><span class="rc-vlabel">来访的常客</span>${visitors.map(v=>`<button type="button" class="rc-visitor" data-business-regulars="${v.id}">${familyArt('family-envelope')}<span>${esc(v.name)}带来「${esc(v.title)}」</span><b>${v.unread?'读故事':'回看'} ›</b></button>`).join('')}</section>`:r.visitorEvents?'<p class="rc-note">来客随口聊了几句</p>':''}
`;
}
