import {shrineBook,goalMembers} from './shrine.js';
import {speciesLabel} from './catalog.js';
import {kitTabs,kitSheet,kitButton,kitButton2,kitChip,kitChipHtml,kitLabel,kitIcon,kitBar,kitEgg} from './ui-kit.js';
import {helpCardsMarkup} from './game-frame.js';

const escape=value=>String(value??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const count=value=>Number(value).toLocaleString('zh-CN');
export const SHRINE_ICON='<img src="/web/art/ui-kit/shrine-snapshot.png" alt="">';
const CHECK='<img src="/web/art/golden-business/family-check.png" alt="已达成">';

// The four 神社 bookmarks, shared with the letters/gifts board (activities-ui). A dot marks something to collect.
export const SHRINE_TABS=[['draw','求签'],['gifts','小礼'],['letters','来信'],['book','签册']];
export const shrineTabsMarkup=(active,dot={})=>kitTabs(SHRINE_TABS.map(([id,label])=>({label,on:active===id,attrs:`data-shrine-tab="${id}"${dot[id]?' data-dot aria-description="有可领取"':''}`})),'神社');
// Conditions as numbered rows with their count (a tick once met).
export const shrineConditions=list=>`<div class="sh-conds sr-conds">${list.map((c,n)=>`<span class="sh-cond${c.met?' met':''}"><i>${c.met?CHECK:n+1}</i><span>${escape(c.label)}</span><b>${count(Math.min(c.current,c.target))}/${count(c.target)}</b></span>`).join('')}</div>`;
// The shared 「?」 drawer for every 神社 page.
export const shrineHelp=closeAttr=>`<div class="kp-scrim" ${closeAttr}></div><section class="kp-drawer gd sr-help" role="dialog" aria-modal="true" aria-label="这页怎么玩"><div class="kp-drawer-title" data-art="plank"><span data-safe><b>这页怎么玩</b></span></div><div class="kp-drawer-body">${helpCardsMarkup('shrine')}<div class="gd-actions" data-row>${kitButton('知道了',closeAttr)}</div></div></section>`;

export function createShrineUI({getState,getNow,panels,showPanel,commitDraw,commitReward,prepareGift,sound,characterPortrait,ingredientPortrait=()=>'',onClose,openLetters,openGifts,openTravel,openRecipes,openDuckShop,openJournal,openActivityTab,getBackLabel=()=> '‹ 返回'}){
  let tab='draw',selectedId=null,revealing=false,revealTimer=null,help=false,claimed=null;
  const find=selector=>panels.querySelector(selector);
  const model=()=>shrineBook(getState(),getNow());
  const art=sign=>sign.found?characterPortrait(0,sign.id):'<span class="bk-q" aria-hidden="true">?</span>';
  function open(next='draw'){
    tab=['draw','book','goals'].includes(next)?next:'draw';selectedId=null;help=false;render();
  }
  function stopReveal(){clearTimeout(revealTimer);revealTimer=null;revealing=false;}
  function close(){stopReveal();onClose();}
  function render(){
    const old=find('.shrine-body'),scroll=old?.scrollTop??0,m=model();
    const selected=m.signs.find(s=>s.id===selectedId);
    const back=getBackLabel().replace(/^‹\s*/,'');
    let body,foot;
    if(tab==='draw'){[body,foot]=drawPage(m,back);}
    else if(tab==='book'){body=bookPage(m);foot=`${kitButton2(back,'data-shrine-back')}${openJournal?kitButton2('寻宝日历','data-shrine-journal'):''}`;}
    else{body=goalsPage(m);foot=`${kitButton2('回签册','data-shrine-tab="book"')}${kitButton2(back,'data-shrine-back')}`;}
    showPanel('神社',`${shrineTabsMarkup(tab==='goals'?'book':tab,{book:m.goals.some(g=>g.available)})}${kitSheet(body,foot,'sr-sheet sr-'+tab,'',{cls:'shrine-body',attrs:tab==='goals'?'data-list':''})}${selected?signDetail(selected):''}${help?shrineHelp('data-shrine-help-close'):''}`,'screen-panel shrine-screen',{skin:'farm',icon:SHRINE_ICON,help:'data-shrine-help',short:'神社'});
    find('.close').onclick=close;
    panels.querySelectorAll('[data-shrine-back]').forEach(b=>b.onclick=close);
    panels.querySelectorAll('[data-shrine-tab]').forEach(button=>button.onclick=()=>{const next=button.dataset.shrineTab;if(next==='gifts'||next==='letters'){stopReveal();openActivityTab?.(next);return;}stopReveal();tab=next;selectedId=null;sound(3);render();find('.shrine-body').scrollTop=0;});
    panels.querySelectorAll('[data-shrine-sign]').forEach(button=>button.onclick=()=>{selectedId=Number(button.dataset.shrineSign);sound(3);render();});
    panels.querySelectorAll('[data-shrine-sign-close]').forEach(b=>b.onclick=()=>{selectedId=null;render();});
    find('[data-shrine-draw]')?.addEventListener('click',draw);
    panels.querySelectorAll('[data-shrine-prepare]').forEach(b=>b.onclick=()=>prepareGift(68));
    find('[data-shrine-journal]')?.addEventListener('click',()=>openJournal('calendar'));
    find('[data-shrine-letters]')?.addEventListener('click',openLetters);
    find('[data-shrine-help]')?.addEventListener('click',()=>{help=true;render();});
    panels.querySelectorAll('[data-shrine-help-close]').forEach(b=>b.onclick=()=>{help=false;render();});
    panels.querySelectorAll('[data-shrine-reward]').forEach(button=>button.onclick=()=>{
      const result=commitReward(button.dataset.shrineReward);if(!result)return;
      sound(7);claimed=result;render();claimed=null;find(`[data-shrine-claimed="${result.id}"]`)?.focus({preventScroll:true});
    });
    panels.querySelectorAll('[data-shrine-route]').forEach(button=>button.onclick=()=>{
      const route=button.dataset.shrineRoute;
      if(route==='signs'){tab='draw';selectedId=null;render();}
      else if(route==='ducks')openDuckShop();else if(route==='yokai')openGifts();else if(route==='dim-sum')openRecipes();else openTravel();
    });
    if(old)find('.shrine-body').scrollTop=scroll;
  }
  // 求签: the slip (or the shrine before the first draw), what is missing, and the one action in the foot.
  function drawPage(m,back){
    const current=m.signs.find(s=>s.id===m.heldId||(!m.heldId&&s.id===m.latest)),held=m.held;
    const state=current?(current.found?'已收录':current.cooking?'正在孵化':held?'在调味料包里':'等待收录'):'';
    const slip=current?`<div class="shrine-slip sr-slip${revealing?' is-revealing':''}"><span class="sr-slip-no">第 ${current.number} 签</span><strong class="sr-fortune">${escape(current.fortune)}</strong><span class="sr-slip-art${current.found?'':' is-unknown'}">${art(current)}</span><b class="sr-slip-name">${current.found?escape(current.name):speciesLabel(0,current.id)}</b>${current.found?`<p>${escape(current.note)}</p>`:''}${kitChip('',state,'mini'+(current.found?'':' soft'))}</div>`
      :`<div class="sr-slip is-empty"><span class="sr-scene">${SHRINE_ICON}</span><strong class="sr-fortune">御神签</strong>${kitChip('','一签一位新朋友','mini')}</div>`;
    const unmet=m.gift.conditions.filter(c=>!c.met);
    const status=held?kitChipHtml(`${ingredientPortrait(68)}御神签 × 1`,'sr-held'):m.gift.available?kitChip('','未收录的签更容易抽到','mini'):'';
    const needs=!held&&unmet.length?`${shrineConditions(unmet)}${unmet.some(c=>c.kind==='activity')?`<div class="gd-row">${kitButton2('去看「神社来信」','data-shrine-letters')}</div>`:''}`:'';
    const action=held?kitButton('配好下一批','data-shrine-prepare'):kitButton(m.gift.claimedToday?'明天再来':'求一签',`data-shrine-draw${m.gift.available?'':' disabled'}`);
    return [`${slip}<div class="sr-status" role="status">${status}</div>${needs}`,`${kitButton2(back,'data-shrine-back')}${action}`];
  }
  // 签册: fifteen stickers; tapping one opens its card. The 收藏回礼 board sits under them.
  function bookPage(m){
    const ready=m.goals.some(g=>g.available);
    return `<div class="kp-bar sr-book-head" data-row><span class="bk-count"><b>${m.discovered}</b>/ 15 种签鸡</span><button type="button" class="gd-btn2 shrine-goals-link sr-goals-link" data-shrine-tab="goals"${ready?' data-dot':''}><img src="/web/art/golden-business/coin.png" alt=""><b>收藏回礼</b></button></div>
      <div class="sr-stamps">${m.signs.map(s=>`<button type="button" data-shrine-sign="${s.id}" class="shrine-stamp bk-sticker ${s.found?'is-known':'is-unknown'}" aria-label="第${s.number}签 ${s.found?s.name:'尚未收录'}"><span class="bk-sticker-art">${art(s)}</span><strong class="collection-name">${s.found?escape(s.fortune):`第 ${s.number} 签`}</strong>${s.held||s.cooking?`<span class="collection-code">${s.held?'在包里':'孵化中'}</span>`:''}</button>`).join('')}</div>`;
  }
  function signDetail(sign){
    const line=sign.found?'印记永久保留，卖掉伙伴也不会丢':sign.cooking?'正在厨房里，孵化后记得收取':sign.held?'这一签已经在调味料包里':'求到这一签，用鸡蛋和保温灯调理';
    return `<div class="kp-scrim" data-shrine-sign-close></div><section class="kp-drawer gd sr-sign" role="dialog" aria-modal="true" aria-label="第 ${sign.number} 签"><div class="kp-drawer-title" data-art="plank"><span data-safe><b>第 ${sign.number} 签</b></span></div><div class="kp-drawer-body shrine-sign-detail">
      <div class="sr-sign-head"><span class="bk-sticker-art big${sign.found?'':' is-unknown'}">${art(sign)}</span><div class="sr-sign-name"><span class="bk-code">${speciesLabel(0,sign.id)}</span><strong class="sr-fortune">${sign.found?escape(sign.fortune):'未收录'}</strong>${sign.found?`<b>${escape(sign.name)}</b>`:''}</div></div>
      ${sign.found?`<p class="sr-note">${escape(sign.note)}</p>`:''}<span class="gd-note">${line}</span>
      <div class="gd-actions" data-row>${sign.held?`${kitButton2('关闭','data-shrine-sign-close')}${kitButton('配好下一批','data-shrine-prepare')}`:kitButton('好','data-shrine-sign-close')}</div></div></section>`;
  }
  // 收藏回礼: a stamp card per gift (like a mileage card). Its own tint and emblem, one slot per partner it asks for,
  // each collected partner stamped into a slot; the reward is a coin stamp; a claimed card gets the big 「已收下」 stamp.
  const EMBLEM={signs:()=>ingredientPortrait(68),ducks:()=>kitEgg(true),yokai:()=>ingredientPortrait(69),'dim-sum':()=>ingredientPortrait(70),time:()=>'<img src="/web/art/golden-journey/precision-clock.png" alt="">'};
  function goalsPage(m){
    const state=getState();
    return `<div class="sr-goals">${m.goals.map(g=>{const done=Math.min(g.current,g.target),got=goalMembers(state,g.kind).slice(0,g.target);
      const slots=Array.from({length:g.target},(_,n)=>got[n]?`<span class="sr-slot is-stamped" style="rotate:${[-5,4,-3,5,-4,3][n%6]}deg">${characterPortrait(...got[n])}</span>`:'<span class="sr-slot"></span>').join('');
      return `<article class="shrine-goal sr-goal kind-${g.kind}${g.claimed?' is-claimed':''}"><div class="sr-goal-top"><span class="sr-goal-emblem">${EMBLEM[g.kind]?.()??''}</span><div class="sr-goal-name"><strong>${escape(g.title)}</strong><small>${escape(g.note)}</small></div><span class="sr-goal-coin"><img src="/web/art/golden-business/coin.png" alt=""><b>+${count(g.cp)}</b></span></div>
      <div class="sr-slots${g.target>8?' many':''}" aria-label="${g.title} ${done}/${g.target}">${slots}</div>
      <div class="sr-goal-foot"><b class="sr-goal-n">${done}/${g.target}</b>${g.claimed?`<span class="sr-claimed" data-shrine-claimed="${g.id}" role="status" tabindex="-1"><img src="/web/art/golden-collection/stamp-done.png" alt=""><b>${claimed?.id===g.id?`+${count(g.cp)}`:'已收下'}</b></span>`:g.available?kitButton('收下',`data-shrine-reward="${g.id}"`):kitButton2('去看看',`data-shrine-route="${g.kind}"`)}</div></article>`;}).join('')}</div>`;
  }
  function draw(){
    if(revealing)return;
    const result=commitDraw();if(!result)return;
    sound(7);revealing=true;tab='draw';selectedId=null;render();
    revealTimer=setTimeout(()=>{revealing=false;find('.shrine-slip')?.classList.remove('is-revealing');},700);
  }
  function refresh(){if(find('.shrine-screen'))render();}
  return {open,refresh};
}
