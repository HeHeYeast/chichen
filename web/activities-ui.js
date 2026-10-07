import {getActivities,ingredientActivityId} from './legacy-activities.js';
import {speciesLabel} from './catalog.js';
import {shrineTabsMarkup,shrineConditions,shrineHelp,SHRINE_ICON} from './shrine-ui.js';
import {kitSheet,kitButton,kitButton2,kitChip,kitLabel,kitCell} from './ui-kit.js';

const escapeHTML=value=>String(value??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const ENVELOPE='<img src="/web/art/golden-journey/envelope.png" alt="">';
// 时空旅行 is a daily exchange, shown with the daily gifts.
const category=entry=>entry.kind==='campaign'?'letters':'gifts';
const actionLabel=entry=>entry.kind==='campaign'?'完成委托':entry.kind==='gift'?'领取礼物':'开始旅行';

// Opening this board is read-only. The host commits a confirmed reward and its
// save together, then returns a result only when persistence succeeds.
export function createActivitiesUI({getState,getNow=Date.now,panels,showPanel,confirmBox,alertBox,commitClaim,sound,characterPortrait,ingredientPortrait,onClose,openKitchen,openShrine,prepareGift}){
  let tab='letters',currentId=null,help=false;
  const find=selector=>panels.querySelector(selector);
  const list=()=>getActivities(getState(),getNow());
  const known=c=>(getState().total?.[`${c.egg}:${c.id}`]??0)>0||(getState().farm?.[`${c.egg}:${c.id}`]??0)>0;
  const stamp=entry=>entry.completed?'已完成':entry.available?entry.kind==='campaign'?'可完成':entry.kind==='travel'?'可出发':'可领取':entry.claimedToday?entry.kind==='travel'?'今日已回':'今日已领':'';
  const face=c=>known(c)?characterPortrait(c.egg,c.id):'<span class="bk-q" aria-hidden="true">?</span>';
  const tileArt=entry=>entry.kind==='gift'?ingredientPortrait(entry.ingredientId):entry.kind==='travel'?characterPortrait(0,0):`<span class="sr-env">${ENVELOPE}<span class="sr-env-face${known(entry.characters[0])?'':' is-unknown'}">${face(entry.characters[0])}</span></span>`;
  const boardScroll={letters:0,gifts:0};
  function bindClose(back=onClose){find('.close').onclick=back;}
  function bindHelp(again){find('[data-activity-help]')?.addEventListener('click',()=>{help=true;again();});panels.querySelectorAll('[data-activity-help-close]').forEach(b=>b.onclick=()=>{help=false;again();});}
  function open(target){
    const id=typeof target==='number'?ingredientActivityId(target):target;help=false;
    if(id==='shrine-gift'&&openShrine){openShrine();return;}
    if(id&&list().some(entry=>entry.id===id)){detail(id);return;}
    tab=target?.tab==='gifts'?'gifts':'letters';board();
  }
  // The board: painted tiles (envelope, gift or the travelling chick) with their state on the tile.
  function board(){
    currentId=null;
    const all=list(),entries=all.filter(entry=>category(entry)===tab),done=entries.filter(e=>e.completed).length;
    // Letters are envelopes: sealed with wax until the promise is kept, opened afterwards; the first partner is the postage stamp.
    const envelope=entry=>`<button type="button" class="sr-letter-env${entry.completed?' is-open':''}${entry.available?' is-ready':''}" data-activity-id="${entry.id}" aria-label="${escapeHTML(entry.title)} · ${stamp(entry)||'进行中'}"><span class="sr-env-art"><img src="${entry.completed?'/web/art/golden-journey/precision-envelope-back.png':'/web/art/golden-business/family-envelope.png'}" alt=""><span class="sr-env-stamp${known(entry.characters[0])?'':' is-unknown'}">${face(entry.characters[0])}</span>${entry.available?'<b class="sr-env-flag">可完成</b>':''}</span><b class="sr-env-title">${escapeHTML(entry.title)}</b><small class="sr-env-sub">${entry.characters.length} 位伙伴</small></button>`;
    const tiles=tab==='letters'?entries.map(envelope).join(''):entries.map(entry=>kitCell({pic:tileArt(entry),name:entry.title,attrs:`data-activity-id="${entry.id}"`,tag:stamp(entry),on:entry.available,label:`${entry.title} · ${stamp(entry)||'进行中'}`})).join('');
    const head=tab==='letters'?`<div class="kp-bar" data-row><span class="bk-count"><b>${done}</b>/ ${entries.length} 封已完成</span></div>`:kitLabel('每天一份');
    showPanel('神社',`${shrineTabsMarkup(tab,{gifts:all.some(e=>e.kind!=='campaign'&&e.available),letters:all.some(e=>e.kind==='campaign'&&e.available)})}${kitSheet(`${head}<div class="${tab==='letters'?'sr-envs':'kp-grid sr-board sr-board-gifts'}">${tiles}</div>`,kitButton2('返回','data-activity-close'),'sr-sheet','',{cls:'activity-letters',attrs:'data-list'})}${help?shrineHelp('data-activity-help-close'):''}`,'screen-panel activities-screen',{skin:'farm',icon:SHRINE_ICON,help:'data-activity-help',short:'神社'});
    bindClose();find('[data-activity-close]').onclick=onClose;bindHelp(board);
    const scroll=find('.activity-letters');scroll.scrollTop=boardScroll[tab];scroll.onscroll=()=>{boardScroll[tab]=scroll.scrollTop;};
    panels.querySelectorAll('[data-shrine-tab]').forEach(button=>button.onclick=()=>{const next=button.dataset.shrineTab;sound(3);if(next==='draw'||next==='book'){openShrine?.(next);return;}tab=next;board();});
    panels.querySelectorAll('[data-activity-id]').forEach(button=>button.onclick=()=>button.dataset.activityId==='shrine-gift'&&openShrine?openShrine():detail(button.dataset.activityId));
  }
  // One letter: what it gives (pictures), the promise as numbered rows, the festival dates, one action.
  function detail(id){
    const entry=list().find(item=>item.id===id);if(!entry){board();return;}
    currentId=id;tab=category(entry);
    const state=stamp(entry);
    const gives=entry.kind==='gift'?`<span class="sr-gift">${ingredientPortrait(entry.ingredientId)}</span>`:`<div class="sr-partners">${entry.characters.map(c=>`<span class="sr-partner" aria-label="${escapeHTML(known(c)?c.name:'未发现品种 '+speciesLabel(c.egg,c.id))}"><span class="sr-partner-art${known(c)?'':' is-unknown'}">${face(c)}</span><small>${speciesLabel(c.egg,c.id)}</small></span>`).join('')}</div>`;
    const recipe=entry.kind==='gift'?entry.ingredientId===68?'鸡蛋 · 保温灯 · 御神签':entry.ingredientId===69?'鸡蛋 · 平底锅 · 火苗':'鸡蛋 · 水煮锅 · 木绵':null;
    const prerequisite=entry.requirements.find(requirement=>requirement.kind==='activity');
    const prerequisiteLink=prerequisite&&!entry.conditions.find(condition=>condition.kind==='activity')?.met?`<div class="gd-row">${kitButton2('先去完成来信',`data-activity-prerequisite="${prerequisite.id}"`)}</div>`:'';
    const dates=entry.kind==='campaign'&&entry.holidays.length?`${kitLabel('节日里开火')}<div class="sr-dates">${entry.holidays.map(c=>`<div class="sr-date${c.window.active?' is-open':''}"><span class="bk-code">${speciesLabel(c.egg,c.id)}</span><strong>${escapeHTML(c.window.title)}</strong><span class="sr-date-range">${escapeHTML(c.window.dateRange)}</span>${c.window.active||c.window.preview?kitChip('',c.window.status,'mini'+(c.window.active?'':' hot')):''}</div>`).join('')}</div>`:'';
    // The letter itself: greeting, the request as its body, the partners as photos taped on, a signature and a postmark.
    // The promise follows as the P.S.
    const photos=entry.kind==='gift'?`<span class="sr-photo"><img class="sr-photo-tape" src="/web/art/golden-collection/v2-tape.png" alt="" data-overhang><span class="sr-photo-art">${ingredientPortrait(entry.ingredientId)}</span></span>`
      :entry.characters.map((c,n)=>`<span class="sr-photo" style="rotate:${[-4,3,-2,4,-3][n%5]}deg" aria-label="${escapeHTML(known(c)?c.name:'未发现品种 '+speciesLabel(c.egg,c.id))}"><img class="sr-photo-tape" src="/web/art/golden-collection/v2-tape.png" alt="" data-overhang><span class="sr-photo-art${known(c)?'':' is-unknown'}">${face(c)}</span><small>${speciesLabel(c.egg,c.id)}</small></span>`).join('');
    const mark=entry.completed?['stamp-done','已完成']:entry.available?['family-stamp-active','可完成']:['stamp-pending',entry.claimedToday?stamp(entry):'进行中'];
    const sign=entry.kind==='campaign'?`${entry.title}的伙伴们`:entry.kind==='gift'?'小神社':'时空旅行社';
    const body=`<article class="sr-stationery"><span class="sr-postmark${entry.completed?' done':''}" data-overhang style="background-image:url(/web/art/${mark[0].startsWith('family')?'golden-business':'golden-collection'}/${mark[0]}.png)"><b>${escapeHTML(mark[1])}</b></span>
      <p class="sr-dear">致 鸡宝厨房：</p><p class="activity-description sr-body">${escapeHTML(entry.description)}</p>
      <div class="activity-reward sr-photos">${photos}</div>${entry.kind==='campaign'?'':`<p class="sr-gift-line">${escapeHTML(entry.rewardText)}</p>`}${recipe?`<p class="sr-gift-line">${escapeHTML(recipe)}</p>`:''}
      <p class="sr-sign">—— ${escapeHTML(sign)}</p></article>
      <section class="sr-ps">${kitLabel('附言 · 这封信的约定')}${shrineConditions(entry.conditions)}${prerequisiteLink}</section>${dates}`;
    const action=entry.completed||entry.claimedToday?kitButton('去厨房','data-activity-kitchen'):kitButton(actionLabel(entry),`data-activity-claim="${entry.id}"${entry.available?'':' disabled'}`);
    showPanel(entry.title,`${kitSheet(body,`${kitButton2('委托簿','data-activity-back')}${action}`,'sr-sheet sr-letter','',{cls:'activity-detail'})}${help?shrineHelp('data-activity-help-close'):''}`,'screen-panel activities-screen activity-detail-screen',{skin:'farm',icon:ENVELOPE,help:'data-activity-help'});
    bindClose(board);find('[data-activity-back]').onclick=board;find('[data-activity-kitchen]')?.addEventListener('click',openKitchen);bindHelp(()=>detail(id));
    if(entry.kind==='gift'&&(getState().ingredients[entry.ingredientId]??0)>0&&prepareGift){
      const old=find('[data-activity-claim]')??find('[data-activity-kitchen]');
      if(old){const holder=document.createElement('template');holder.innerHTML=kitButton('配好下一批',`data-activity-prepare="${entry.ingredientId}"`);const prepare=holder.content.firstElementChild;prepare.onclick=()=>prepareGift(entry.ingredientId);old.replaceWith(prepare);}
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
  function refresh(){if(!find('.activities-screen'))return;const scroll=find('.kp-scroll')?.scrollTop??0;currentId?detail(currentId):board();const scroller=find('.kp-scroll');if(scroller)scroller.scrollTop=scroll;}
  return {open,detail,refresh};
}
