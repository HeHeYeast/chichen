import {harvestStock,planHarvestAllocation,allocateHarvest} from './harvest-allocation.js';
import {inventoryView,lockedCount} from './inventory.js';
import {resolveSpecies,CONTENT_TEXT} from './content-registry.js';
import {helpCardsMarkup} from './game-frame.js';
const esc=v=>String(v).replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
export function createHarvestAllocationUI({getState,getNow,showPanel,panels,commitProgress,confirmBox,closePanel,openBusinessDraft,characterPortrait,openNextBatch}){
  let rows={},orderId=null,keepOne=true,useRewards=false;
  const options=()=>({rows,orderId,keepOne,useRewards});
  // The result card shown when a batch is fully collected: who came, how many, what is new.
  // Selling the extras keeps one at home when the save's policy says so; finer splits stay one tap away.
  function extras(s){
    const keep=s.expansion?.inventoryPolicy?.keepOne!==false,sale={};
    for(const [key,n] of Object.entries(harvestStock(s))){const v=inventoryView(s,key),room=Math.max(0,keep?Math.min(v.free,v.home-lockedCount(s,key)):v.free),k=Math.min(n,room);if(k>0)sale[key]=k;}
    return sale;
  }
  // Non-blocking: the card floats over the nest, and any tap elsewhere puts it away, so the
  // kitchen controls (shop, warehouse, skills, cookware) stay usable underneath.
  let card=null,outside=null;
  function closeCard(){if(outside)document.removeEventListener('pointerdown',outside,true);outside=null;card?.remove();card=null;}
  function openCard(){
    closeCard();
    const s=getState(),stock=Object.entries(harvestStock(s)).sort((a,b)=>b[1]-a[1]),total=stock.reduce((a,[,n])=>a+n,0);
    if(!total)return;
    const isNew=([key,n])=>(s.total?.[key]??0)<=n,fresh=stock.filter(isNew).length;
    const sale=extras(s);let quote=null;try{quote=Object.keys(sale).length?planHarvestAllocation(s,{rows:Object.fromEntries(Object.entries(sale).map(([k,n])=>[k,{sale:n}])),keepOne:s.expansion?.inventoryPolicy?.keepOne!==false,useRewards:true}).quote:null;}catch{quote=null;}
    const bonus=s.progress?.lastHarvest?.bonus??0;
    const birds=stock.slice(0,4).map(([key,n])=>{const [egg,id]=key.split(':').map(Number);return `<span class="hc-bird">${characterPortrait(egg,id)}<b>×${n}</b>${isNew([key,n])?'<em>新</em>':''}</span>`;}).join('');
    card=document.createElement('section');card.className='harvest-card';card.setAttribute('role','dialog');card.setAttribute('aria-label','这锅收成');
    card.innerHTML=`<h3>这锅收成</h3><div class="hc-birds">${birds}</div>
      <p class="hc-total"><b>${total}</b> 只${stock.length>4?` · ${stock.length} 种`:''}${fresh?` · 新伙伴 ${fresh}`:''}${bonus?` · +${bonus} CP`:''}</p>
      <button class="orange hc-done" data-harvest-done data-next>收好</button>
      <div class="hc-more">${quote?`<button data-harvest-sell>卖掉多余 +${quote.income} CP</button>`:''}<button data-harvest-next>下一锅</button><button data-harvest-split>细分安排</button></div>`;
    panels.append(card);
    const find=q=>card.querySelector(q);
    find('[data-harvest-done]').onclick=closeCard;
    find('[data-harvest-next]').onclick=()=>{closeCard();openNextBatch?.();};
    find('[data-harvest-split]').onclick=()=>{closeCard();open();};
    find('[data-harvest-sell]')?.addEventListener('click',()=>{const n=Object.values(sale).reduce((a,b)=>a+b,0);closeCard();
      confirmBox(`卖掉 ${n} 只
+${quote.income} CP`,()=>{commitProgress(d=>allocateHarvest(d,{rows:Object.fromEntries(Object.entries(sale).map(([k,v])=>[k,{sale:v}])),keepOne:d.expansion?.inventoryPolicy?.keepOne!==false,useRewards:true},getNow()));},false,{yes:'卖掉',no:'先留着'});});
    outside=e=>{if(card&&!card.contains(e.target))closeCard();};
    setTimeout(()=>{if(card)document.addEventListener('pointerdown',outside,true);},0);
  }
  function open(){rows={};orderId=getState().expansion.orders.active.find(o=>o.kind==='purchase')?.id??null;render();}
  function render(){
    const s=getState(),orders=s.expansion.orders.active.filter(o=>o.kind==='purchase');
    showPanel('本锅收成',`<div class="scroll allocation-list"><p>收成已经在家。默认全部留着，下面可一次安排三种去向。</p><label><input type="checkbox" data-allocation-keep ${keepOne?'checked':''}> 按仓库里的锁定数量留着</label><label><input type="checkbox" data-allocation-rewards ${useRewards?'checked':''}> 立即出售时使用经营奖励</label><label>预留给订单 <select data-allocation-order><option value="">暂不预留</option>${orders.map(o=>`<option value="${o.id}" ${o.id===orderId?'selected':''}>${esc(CONTENT_TEXT[o.templateId]?.name??o.templateId)}</option>`).join('')}</select></label>${Object.entries(harvestStock(s)).map(([key,n])=>{const c=resolveSpecies(key),v=inventoryView(s,key);return `<section class="allocation-row"><h3>${esc(c.title_zh_CN)} · 本锅${n}只</h3><p>可用${v.free} · 外出${v.R} · 营业${v.S} · 预留${v.Q}</p><div>${[['sale','立即出售'],['business','营业草稿'],['order','订单预留']].map(([dest,label])=>`<label>${label}<input type="number" min="0" max="${Math.min(n,v.free)}" inputmode="numeric" data-allocation="${key}|${dest}" value="${rows[key]?.[dest]??0}" ${dest==='business'&&!c.edible||dest==='order'&&!orderId?'disabled':''}></label>`).join('')}</div></section>`;}).join('')}<p data-allocation-result role="status"></p></div><footer><button data-allocation-back>先留着</button><button class="orange" data-allocation-confirm>核对安排</button></footer>`,'screen-panel allocation-screen');
    const find=q=>panels.querySelector(q);
    const update=()=>{try{const p=planHarvestAllocation(getState(),options()),n=Object.values(p.sale).reduce((a,b)=>a+b,0);find('[data-allocation-result]').textContent=`立即出售${n}只，预计${p.quote.income} CP。营业仅保存草稿，订单仅预留。`;find('[data-allocation-confirm]').disabled=false;}catch(e){find('[data-allocation-result]').textContent=e.message;find('[data-allocation-confirm]').disabled=true;}};
    panels.querySelectorAll('[data-allocation]').forEach(input=>input.oninput=()=>{const [key,dest]=input.dataset.allocation.split('|');(rows[key]??={})[dest]=Number(input.value);update();});
    find('[data-allocation-keep]').onchange=e=>{keepOne=e.target.checked;update();};find('[data-allocation-rewards]').onchange=e=>{useRewards=e.target.checked;update();};
    find('[data-allocation-order]').onchange=e=>{orderId=e.target.value||null;render();};find('[data-allocation-back]').onclick=closePanel;
    find('[data-allocation-confirm]').onclick=()=>{const p=planHarvestAllocation(getState(),options()),n=Object.values(p.sale).reduce((a,b)=>a+b,0),hasDraft=Object.keys(p.business).length>0;
      confirmBox(`立即出售${n}只，获得${p.quote.income} CP。\n${hasDraft?'营业选择只保存为下次草稿，开张前还要核对。\n':''}订单只预留，不算交付。${keepOne?'':'\n本次允许分配最后一只，图鉴收录仍保留。'}`,()=>{const r=commitProgress(s=>allocateHarvest(s,options(),getNow()));if(!r){update();return;}rows={};if(hasDraft)openBusinessDraft(r.business);else closePanel();},false,{yes:n?`出售${n}只并确认`:'确认安排',no:'再看看'});
    };update();
  }
  return {open,openCard,closeCard};
}
