// Shop-only presentation. Existing order / regular commands remain authoritative.
const HELP={
  projects:{title:'项目筹备帮助',body:'<p>项目按三个阶段推进，可同时筹备，也可以置顶一个。先达成当前阶段条件，再交付或支付。不用花 CP 的阶段，条件满足就自动完成。</p><p>交付伙伴不付货款，交出的伙伴不会退回。只使用可用伙伴，默认每种在家留1只；营业、订单预留和寻访中的伙伴不会被取走。</p><p>任选品种的阶段首次交付后锁定选择。阶段费用只在确认完成时扣除一次，已支付费用不退回。</p><p>展册画像使用已收录伙伴的永久画像，不消耗库存，也不算食用交付。</p>'},
  ledger:{title:'账单帮助',body:'<p>基础收入来自本单实际售出的伙伴；招牌加价与菜单加成按成交时条件计算。货款随成交保存。</p><p>每满2小时招待一轮，最多售出6只；最长营业24小时，售罄自动收摊。下一轮之前不会提前记收入。</p><p>整筐、拼盘等经营奖励在收摊时结算。营业中的预计奖励不计入已入账总额；未售出品与未用奖励次数在收摊后解除占用。</p><p>只列本单实际产生的常客故事和菜单成果。账单可反复打开，不会重复发钱。</p>'},
  orders:{title:'订单帮助',body:'<h4>先看需求，再交付</h4><p>点开订单可查看品种、数量和报酬。可用伙伴够数后，点「交付」完成采购，领取货款与酬谢。数量不足时，点「去做」打开下一锅，查看这单需要补什么。</p><h4>哪些伙伴会交出去？</h4><p>交付会扣除伙伴；留在家里或被其他用途占用的数量不算可用库存。陈列订单只需点「摆出来」，不消耗伙伴。</p><h4>订单情报有什么用？</h4><p>卡上标有「情报」时，本次完成还会获得对应地区的寻访提示。普通采购通常只在首次完成时提供，是否有情报以卡片为准。</p><h4>厨房往事</h4><p>旧版三章采购保留原来的流程，不占这里的订单名额。</p>'},
  regulars:{title:'常客帮助',body:'<h4>怎样继续来往？</h4><p>点开常客，查看当前条件。营业、订单和寻访都可能推进来往；列出的两条做法，完成其中一条即可。</p><h4>对话可以稍后看吗？</h4><p>可以。新对话会保存在这里，不会过期，也不会挡住后续进展。看过的内容可以再次打开；阅读本身不扣伙伴或 CP。</p><h4>置顶和纪念物</h4><p>置顶后，符合来访条件的这位常客会在营业时优先出现。完成最后一段的条件可获得纪念物，到收藏中查看。</p>'},
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
