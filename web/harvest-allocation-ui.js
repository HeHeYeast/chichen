import {harvestStock,planHarvestAllocation,allocateHarvest} from './harvest-allocation.js';
import {inventoryView} from './inventory.js';
import {resolveSpecies,CONTENT_TEXT} from './content-registry.js';
const esc=v=>String(v).replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
export function createHarvestAllocationUI({getState,getNow,showPanel,panels,commitProgress,confirmBox,closePanel,openBusinessDraft}){
  let rows={},orderId=null,keepOne=true,useRewards=false;
  const options=()=>({rows,orderId,keepOne,useRewards});
  function open(){rows={};orderId=getState().expansion.orders.active.find(o=>o.kind==='purchase')?.id??null;render();}
  function render(){
    const s=getState(),orders=s.expansion.orders.active.filter(o=>o.kind==='purchase');
    showPanel('本锅收成',`<div class="scroll allocation-list"><p>收成已经在家。默认全部留着，下面可一次安排三种去向。</p><label><input type="checkbox" data-allocation-keep ${keepOne?'checked':''}> 每种在家留1只</label><label><input type="checkbox" data-allocation-rewards ${useRewards?'checked':''}> 即时出售使用经营奖励</label><label>预留给采购 <select data-allocation-order><option value="">暂不预留</option>${orders.map(o=>`<option value="${o.id}" ${o.id===orderId?'selected':''}>${esc(CONTENT_TEXT[o.templateId]?.name??o.templateId)}</option>`).join('')}</select></label>${Object.entries(harvestStock(s)).map(([key,n])=>{const c=resolveSpecies(key),v=inventoryView(s,key);return `<section class="allocation-row"><h3>${esc(c.title_zh_CN)} · 本锅${n}只</h3><p>自由${v.free} · 在途${v.R} · 营业${v.S} · 采购预留${v.Q}</p><div>${[['sale','立即出售'],['business','营业草稿'],['order','订单预留']].map(([dest,label])=>`<label>${label}<input type="number" min="0" max="${Math.min(n,v.free)}" inputmode="numeric" data-allocation="${key}|${dest}" value="${rows[key]?.[dest]??0}" ${dest==='business'&&!c.edible||dest==='order'&&!orderId?'disabled':''}></label>`).join('')}</div></section>`;}).join('')}<p data-allocation-result role="status"></p></div><footer><button data-allocation-back>先留着</button><button class="orange" data-allocation-confirm>核对安排</button></footer>`,'screen-panel allocation-screen');
    const find=q=>panels.querySelector(q);
    const update=()=>{try{const p=planHarvestAllocation(getState(),options()),n=Object.values(p.sale).reduce((a,b)=>a+b,0);find('[data-allocation-result]').textContent=`立即出售${n}只，预计${p.quote.income} CP。营业仅保存草稿，订单仅预留。`;find('[data-allocation-confirm]').disabled=false;}catch(e){find('[data-allocation-result]').textContent=e.message;find('[data-allocation-confirm]').disabled=true;}};
    panels.querySelectorAll('[data-allocation]').forEach(input=>input.oninput=()=>{const [key,dest]=input.dataset.allocation.split('|');(rows[key]??={})[dest]=Number(input.value);update();});
    find('[data-allocation-keep]').onchange=e=>{keepOne=e.target.checked;update();};find('[data-allocation-rewards]').onchange=e=>{useRewards=e.target.checked;update();};
    find('[data-allocation-order]').onchange=e=>{orderId=e.target.value||null;render();};find('[data-allocation-back]').onclick=closePanel;
    find('[data-allocation-confirm]').onclick=()=>{const p=planHarvestAllocation(getState(),options()),n=Object.values(p.sale).reduce((a,b)=>a+b,0),hasDraft=Object.keys(p.business).length>0;
      confirmBox(`立即出售${n}只，获得${p.quote.income} CP。\n${hasDraft?'营业选择只保存为下次草稿，开张前还要核对。\n':''}采购仅预留，不是交付。${keepOne?'':'\n本次允许分配最后一只，图鉴收录仍保留。'}`,()=>{const r=commitProgress(s=>allocateHarvest(s,options(),getNow()));if(!r){update();return;}rows={};if(hasDraft)openBusinessDraft(r.business);else closePanel();},false,{yes:n?`出售${n}只并保留安排`:'保留安排',no:'再看看'});
    };update();
  }
  return {open};
}
