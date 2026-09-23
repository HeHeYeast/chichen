// Desktop side column (wide landscape only): the current pot, business and trip
// at a glance plus the single pinned target. Read-only; every button routes to
// the place that owns the object. Phones never render it.
import {uiPreference} from './ui-preferences.js';
import {regularInfo} from './regulars.js';
import {projectInfo} from './projects.js';
import {collectionProgress} from './collection-progress.js';
import {CONTENT_TEXT} from './content-registry.js';

const esc=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const remaining=ms=>{const mins=Math.max(0,Math.ceil(ms/60000));return mins>=60?`${Math.floor(mins/60)}小时${mins%60}分`:`${mins}分钟`;};

export function deskModel(s,now){
  const batch=s.batch,waiting=batch?.eggs?.filter(e=>!e.collected).length??0,trip=s.progress?.trip,active=s.expansion?.business?.active;
  const pot=!batch?'灶台空着，可以备下一锅':waiting&&now<batch.ends?`孵化中 · 还需${remaining(batch.ends-now)}`:waiting?`${waiting}只等你收取`:'这一锅已收完';
  const business=active?`营业中 · 已卖${active.totalSold}只`:s.expansion?.business?.lastReport?'最近一单已结清':'还没有开张';
  const explore=trip?now>=trip.endAt?'队伍已归来，等你查看':`在途 · 还需${remaining(trip.endAt-now)}`:'没有队伍在外';
  const pin=uiPreference('pinnedTarget');let target=null;
  try{
    if(pin?.kind==='regular'){const r=regularInfo(s,pin.id);target={kind:'regular',id:pin.id,label:'常客',name:r.name,detail:r.complete?'已毕业':r.pending?'有一段可读':r.stage?`第${r.stage.index+1}段 · ${r.stage.title}`:''};}
    else if(pin?.kind==='collection'){const c=collectionProgress(s,pin.id),done=c.stages.filter(x=>x.met).length;target={kind:'collection',id:pin.id,label:'收藏',name:CONTENT_TEXT[pin.id]?.name??pin.id,detail:`阶段 ${done}/${c.stages.length}${c.practice?.met?' · 已实践':''}`};}
    else if(pin?.kind==='project'){const p=projectInfo(s,pin.id);target={kind:'project',id:pin.id,label:'项目',name:p.name,detail:`${p.stages.filter(x=>x.complete).length}/3 阶段`};}
  }catch{target=null;}
  return {pot,business,explore,target,cp:s.cp};
}

export function renderDesk(root,s,now,{openTrade,openExplore,openBooks,goKitchen,page=0,summary=''}){
  const m=deskModel(s,now);
  const focus=document.activeElement?.dataset?.desk;
  root.innerHTML=`<section class="desk paper" aria-label="今日小厨房"><h2>今日小厨房</h2>
    ${page<=1?'':'<p class="desk-note">'+esc(summary||({5:m.business,6:m.explore,4:'永久收录与库存分开，卖光仍然收录。'})[page]||'核对当前页面的条件与安排。')+'</p>'}
    ${page<=1?`<button data-desk="kitchen"><span>厨房</span><strong>${esc(m.pot)}</strong></button>
    <button data-desk="trade"><span>生意</span><strong>${esc(m.business)}</strong></button>
    <button data-desk="explore"><span>寻访</span><strong>${esc(m.explore)}</strong></button>`:''}
    <div class="desk-target"><span>置顶目标</span>${m.target?`<button data-desk="target"><strong>${esc(m.target.label)} · ${esc(m.target.name)}</strong><small>${esc(m.target.detail)}</small></button>`:'<p>在收藏、常客或项目页可以置顶一个想继续的目标（只占一个位置）。</p>'}</div>
    <p class="desk-note">确认操作在左侧正文中进行；这张便签只汇总当前安排。</p></section>`;
  root.querySelector('[data-desk="kitchen"]')?.addEventListener('click',goKitchen);
  root.querySelector('[data-desk="trade"]')?.addEventListener('click',()=>openTrade('business'));
  root.querySelector('[data-desk="explore"]')?.addEventListener('click',openExplore);
  root.querySelector('[data-desk="target"]')?.addEventListener('click',()=>m.target.kind==='collection'?openBooks(m.target.id):openTrade(m.target.kind==='regular'?'regulars':'projects',m.target.id));
  if(focus)root.querySelector(`[data-desk="${focus}"]`)?.focus({preventScroll:true});
}
