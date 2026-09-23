import {getActivities,ingredientActivityId} from './legacy-activities.js';
import {speciesLabel} from './catalog.js';

const escapeHTML=value=>String(value??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const count=value=>Number(value??0).toLocaleString('zh-CN');
const egg='<span class="activity-mystery" aria-hidden="true">?</span>';
const letterIcon='<svg viewBox="0 0 32 26" aria-hidden="true"><path d="M3 4h26v18H3zM3 4l13 11L29 4M3 22l9-10m17 10-9-10" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linejoin="round"/></svg>';
const category=entry=>entry.kind==='campaign'?'letters':entry.kind==='gift'?'gifts':'travel';
const actionLabel=entry=>entry.kind==='campaign'?'完成委托':entry.kind==='gift'?'领取礼物':'开始旅行';

// Opening this board is read-only. The host commits a confirmed reward and its
// save together, then returns a result only when persistence succeeds.
export function createActivitiesUI({getState,getNow=Date.now,panels,showPanel,confirmBox,alertBox,commitClaim,sound,characterPortrait,ingredientPortrait,onClose,openKitchen,openShrine,prepareGift}){
  let tab='letters',currentId=null;
  const find=selector=>panels.querySelector(selector);
  const list=()=>getActivities(getState(),getNow());
  const known=c=>(getState().total?.[`${c.egg}:${c.id}`]??0)>0||(getState().farm?.[`${c.egg}:${c.id}`]??0)>0;
  const stamp=entry=>entry.completed?'已完成':entry.available?entry.kind==='campaign'?'可完成':entry.kind==='travel'?'可出发':'可领取':entry.claimedToday?entry.kind==='travel'?'今日已回':'今日已领':'进行中';
  const art=entry=>entry.kind==='gift'?ingredientPortrait(entry.ingredientId):entry.characters.slice(0,3).map(c=>`<span class="activity-mini">${known(c)?characterPortrait(c.egg,c.id):egg}</span>`).join('');
  const boardScroll={letters:0,gifts:0,travel:0};
  function bindClose(back=onClose){find('.close').onclick=back;}
  function open(target){
    const id=typeof target==='number'?ingredientActivityId(target):target;
    if(id==='shrine-gift'&&openShrine){openShrine();return;}
    if(id&&list().some(entry=>entry.id===id)){detail(id);return;}
    tab='letters';board();
  }
  function board(){
    currentId=null;
    const all=list(),entries=all.filter(entry=>category(entry)===tab),complete=all.filter(entry=>entry.kind==='campaign'&&entry.completed).length;
    const cards=entries.map(entry=>`<button class="activity-letter ${entry.completed?'is-complete':''}" data-activity-id="${entry.id}"><span class="activity-letter-art ${entry.kind==='gift'?'is-gift':''}">${art(entry)}</span><span class="activity-letter-copy"><strong>${escapeHTML(entry.title)}</strong><small>${entry.kind==='campaign'?`${entry.characters.length} 种伙伴的配方来信`:entry.kind==='gift'?'每天一份 · 调理用赠品':'带一只普通鸡宝去旅行'}</small><em>${escapeHTML(entry.reason||'目标已达成，来收下回信吧')}</em></span><span class="activity-letter-status ${entry.available?'is-ready':''}">${stamp(entry)}</span></button>`).join('');
    showPanel('神社委托簿',`<div class="activity-board-lead"><span class="activity-envelope" aria-hidden="true">${letterIcon}</span><div><strong>给小厨房的来信</strong><small>常驻委托 · 已完成 ${complete} / ${all.filter(e=>e.kind==='campaign').length}</small></div></div><div class="subtabs activity-tabs">${[['letters','伙伴来信'],['gifts','每日小礼'],['travel','时空旅行']].map(([id,label])=>`<button data-activity-tab="${id}" aria-pressed="${id===tab}" class="${id===tab?'active':''}">${label}</button>`).join('')}</div><div class="scroll activity-letters">${cards}</div><footer class="activity-board-footer"><span>委托长期保留，按自己的节奏来。</span><button data-activity-close>返回</button></footer>`,'screen-panel activities-screen');
    bindClose();find('[data-activity-close]').onclick=onClose;
    if(openShrine){const entry=document.createElement('button');entry.className='activity-shrine-entry';entry.dataset.activityShrine='';entry.innerHTML=`<span>${ingredientPortrait(68)}</span><span><strong>神社御神签</strong><small>每天求签 · 签册集章 · 八份收藏回礼</small></span><b>›</b>`;find('.activity-board-lead').after(entry);entry.onclick=()=>openShrine();}
    const scroll=find('.activity-letters');scroll.scrollTop=boardScroll[tab];scroll.onscroll=()=>{boardScroll[tab]=scroll.scrollTop;};
    panels.querySelectorAll('[data-activity-tab]').forEach(button=>button.onclick=()=>{tab=button.dataset.activityTab;sound(3);board();});
    panels.querySelectorAll('[data-activity-id]').forEach(button=>button.onclick=()=>button.dataset.activityId==='shrine-gift'&&openShrine?openShrine():detail(button.dataset.activityId));
  }
  function detail(id){
    const entry=list().find(item=>item.id===id);if(!entry){board();return;}
    currentId=id;tab=category(entry);
    const conditions=entry.conditions.map(condition=>`<li class="${condition.met?'is-met':''}"><span aria-hidden="true">${condition.met?'✓':'·'}</span><div>${escapeHTML(condition.label)}<b>${count(Math.min(condition.current,condition.target))} / ${count(condition.target)}</b></div></li>`).join('');
    const preview=entry.characters.map(c=>`<span class="activity-partner" aria-label="${escapeHTML(known(c)?c.name:'未发现品种 '+speciesLabel(c.egg,c.id))}"><span>${known(c)?characterPortrait(c.egg,c.id):egg}</span><small>${speciesLabel(c.egg,c.id)}</small></span>`).join('');
    const prerequisite=entry.requirements.find(requirement=>requirement.kind==='activity');
    const prerequisiteLink=prerequisite&&!entry.conditions.find(condition=>condition.kind==='activity')?.met?`<button class="activity-prerequisite" data-activity-prerequisite="${prerequisite.id}">先去完成来信 ›</button>`:'';
    const recipe=entry.kind==='gift'?entry.ingredientId===68?'鸡蛋 · 保温灯 · 御神签':entry.ingredientId===69?'鸡蛋 · 平底锅 · 火苗':'鸡蛋 · 水煮锅 · 木绵':null;
    showPanel(entry.title,`<div class="scroll activity-detail"><div class="activity-detail-heading"><span class="activity-envelope" aria-hidden="true">${letterIcon}</span><span>${entry.kind==='campaign'?'伙伴来信':entry.kind==='gift'?'每日小礼':'时空旅行'}</span><em class="${entry.available?'is-ready':''}">${stamp(entry)}</em></div><p class="activity-description">${escapeHTML(entry.description)}</p><div class="activity-reward"><strong>${escapeHTML(entry.rewardText)}</strong>${entry.kind==='gift'?`<span class="activity-reward-gift">${ingredientPortrait(entry.ingredientId)}</span>`:`<div class="activity-partners">${preview}</div>`}${recipe?`<small>${recipe}</small>`:''}</div><h3>这封来信的约定</h3><ul class="activity-conditions">${conditions}</ul>${prerequisiteLink}<p class="activity-detail-reason" role="status">${escapeHTML(entry.reason||'已经准备好了，确认后即可收下。')}</p>${entry.kind==='campaign'?`<p class="activity-recipe-note">${entry.holidays.length?'完成后永久获得资格。节日伙伴仍需在以下日期开火，搭配对应厨具与调味料。':'完成后开放配方，在厨房搭配厨具与调味料孵化。'}</p>${entry.holidays.map(c=>`<p class="activity-recipe-note">${speciesLabel(c.egg,c.id)} · ${c.window.title}<br>${c.window.dateRange} · ${c.window.status}</p>`).join('')}`:''}</div><footer class="activity-detail-footer"><button data-activity-back>‹ 返回委托簿</button>${entry.completed||entry.claimedToday?'<button class="orange" data-activity-kitchen>去厨房</button>':`<button class="orange" data-activity-claim="${entry.id}" ${entry.available?'':'disabled'}>${actionLabel(entry)}</button>`}</footer>`,'screen-panel activities-screen activity-detail-screen');
    bindClose(board);find('[data-activity-back]').onclick=board;find('[data-activity-kitchen]')?.addEventListener('click',openKitchen);
    if(entry.kind==='gift'&&(getState().ingredients[entry.ingredientId]??0)>0&&prepareGift){
      const action=find('[data-activity-claim]')??find('[data-activity-kitchen]');
      const prepare=document.createElement('button');prepare.className='orange';prepare.dataset.activityPrepare=String(entry.ingredientId);prepare.textContent='配好下一批';prepare.onclick=()=>prepareGift(entry.ingredientId);action?.replaceWith(prepare);
    }
    find('[data-activity-prerequisite]')?.addEventListener('click',()=>detail(prerequisite.id));
    find('[data-activity-claim]')?.addEventListener('click',()=>{
      const fresh=list().find(item=>item.id===id);
      if(!fresh?.available){detail(id);return;}
      confirmBox(`${actionLabel(fresh)}：${fresh.title}\n\n${fresh.rewardText}${fresh.kind==='travel'?'\n将消耗农场中的普通鸡宝 × 1。':''}\n\n${fresh.kind==='campaign'?'取得资格后，节日伙伴仍需在对应日期调理；具体日期见来信与寻宝日历。':'领取记录会保存在当前进度中。'}`,()=>{
        const result=commitClaim(id);detail(id);
        if(result){sound(7);alertBox(`${result.rewardText}\n\n${result.kind==='campaign'?'来信已收下。到图鉴点击未发现的编号，可以查看孵化线索。':result.kind==='gift'?'礼物已放进调味料包。到厨房选择后，再开始调理。':'旅途结束，新伙伴已经来到农场并收入图鉴。'}`);}
      });
    });
  }
  function refresh(){if(!find('.activities-screen'))return;const scroll=find('.scroll')?.scrollTop??0;currentId?detail(currentId):board();const scroller=find('.scroll');if(scroller)scroller.scrollTop=scroll;}
  return {open,detail,refresh};
}
