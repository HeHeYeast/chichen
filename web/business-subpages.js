// Shop-only presentation. Existing order / regular commands remain authoritative.
const HELP={
  projects:{title:'项目筹备帮助',body:'<p>项目按三个阶段推进，可同时筹备，也可以置顶一个。先达成当前阶段条件，再交付或支付。不用花 CP 的阶段，条件满足就自动完成。</p><p>交付伙伴不付货款，交出的伙伴不会退回。只使用可用伙伴，默认每种在家留1只；营业、订单预留和寻访中的伙伴不会被取走。</p><p>任选品种的阶段首次交付后锁定选择。阶段费用只在确认完成时扣除一次，已支付费用不退回。</p><p>展册画像使用已收录伙伴的永久画像，不消耗库存，也不算食用交付。</p>'},
  ledger:{title:'账单帮助',body:'<p>基础收入来自本单实际售出的伙伴；招牌加价与菜单加成按成交时条件计算。货款随成交保存。</p><p>每满2小时招待一轮，最多售出6只；最长营业24小时，售罄自动收摊。下一轮之前不会提前记收入。</p><p>整筐、拼盘等经营奖励在收摊时结算。营业中的预计奖励不计入已入账总额；未售出品与未用奖励次数在收摊后解除占用。</p><p>只列本单实际产生的常客故事和菜单成果。账单可反复打开，不会重复发钱。</p>'},
  orders:{title:'订单帮助',body:'<p>订单在生意页上：要的伙伴够了，点「交付」一次完成，付货款和酬谢；卡上有「情报」的这一次还带回本地区的消息（重要订单常有，普通订单只有第一次）。不够就点「去做」，下一锅会把这单放在最前面。</p><p>每种在家留着的伙伴不会交出去。只看不交的订单点「摆出来」，看完都回家。</p><p>「厨房往事」三章照原样进行，不占新询问的名额。</p>'},
  regulars:{title:'常客帮助',body:'<p>常客不排名、不计好感，也没有期限。新的一段到了就记下；没读的故事不会挡住下一段，随时可以回读。</p><p>营业、订单或寻访记录按各段条件推进；读故事不扣库存、不付款。</p><p>已读故事可以回读。未到访者不提前公开名字和故事。置顶的常客营业时会优先来访。</p>'},
};
export function shopSubpageHeader(kind){
  const title={orders:'订单',regulars:'常客',projects:'项目',ledger:'账单'}[kind],art={orders:'orders',regulars:'regulars-original',projects:'projects',ledger:'orders'}[kind];
  return `<header class="bs-sub-header"><button data-shop-back aria-label="回到营业">‹</button><h1><img src="/web/art/golden-business/${art}.png" alt="">${title}</h1><button class="game-help" data-shop-help aria-label="${title}帮助">?</button></header>`;
}
export function shopSubpagePaper(kind,body){
  const art={orders:'v2-order-pad',regulars:'v2-guest-page',projects:'family-project-board',ledger:'family-receipt'}[kind];
  return `<div class="bs-sub-paper"><img class="bs-sub-material" src="/web/art/golden-business/${art}.png" alt="" aria-hidden="true">${kind==='projects'||kind==='ledger'?`<img class="family-fastener" data-overhang src="/web/art/golden-business/family-${kind==='projects'?'pin':'clip'}.png" alt="">`:''}${body}</div>`;
}
export function shopSubpageDialog(kind){const help=HELP[kind];return `<dialog class="bs-sub-dialog" aria-labelledby="bs-help-title"><header><h3 id="bs-help-title">${help.title}</h3><button data-shop-help-close aria-label="关闭帮助">×</button></header>${help.body}</dialog>`;}
export function bindShopSubpage(panels,openBusiness){
  const dialog=panels.querySelector('.bs-sub-dialog'),button=panels.querySelector('[data-shop-help]');
  panels.querySelector('[data-shop-back]').onclick=()=>openBusiness?.();
  button.onclick=()=>dialog.showModal();
  panels.querySelector('[data-shop-help-close]').onclick=()=>{dialog.close();button.focus({preventScroll:true});};
}
