import {shrineBook} from './shrine.js';
import {speciesLabel} from './catalog.js';

const escape=value=>String(value??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const count=value=>Number(value).toLocaleString('zh-CN');
const cylinder='<svg viewBox="0 0 90 112" aria-hidden="true"><path d="M26 39L18 6l8-2 15 37M42 38L39 2h9l4 38M59 39L68 5l9 3-9 35" fill="#f8da90" stroke="#77502f" stroke-width="3" stroke-linejoin="round"/><path d="M18 37Q45 29 72 37l-4 60q-23 14-46 0z" fill="#c67748" stroke="#77502f" stroke-width="3"/><ellipse cx="45" cy="38" rx="27" ry="8" fill="#804d31" stroke="#77502f" stroke-width="3"/><path d="M25 49l3 42M63 49l-3 42" stroke="#e9a464" stroke-width="4" stroke-linecap="round"/><path d="M33 51h24v35H33z" fill="#fff2c8" stroke="#77502f" stroke-width="2"/><path d="M38 62h14m-7-6v20m-8-7h16" fill="none" stroke="#b9573e" stroke-width="3"/></svg>';

export function createShrineUI({getState,getNow,panels,showPanel,commitDraw,commitReward,prepareGift,sound,characterPortrait,onClose,openLetters,openGifts,openTravel,openRecipes,openDuckShop,openJournal}){
  let tab='draw',selectedId=null,revealing=false,revealTimer=null;
  const find=selector=>panels.querySelector(selector);
  const model=()=>shrineBook(getState(),getNow());
  const art=sign=>sign.found?characterPortrait(0,sign.id):`<span class="shrine-unknown" aria-hidden="true">?</span>`;
  function open(next='draw'){
    tab=['draw','book','goals'].includes(next)?next:'draw';selectedId=null;render();
  }
  function stopReveal(){clearTimeout(revealTimer);revealTimer=null;revealing=false;}
  function close(){stopReveal();onClose();}
  function render(){
    const old=find('.shrine-body'),scroll=old?.scrollTop??0,m=model();
    const selected=m.signs.find(s=>s.id===selectedId);
    const content=selected?signDetail(selected,m):tab==='draw'?drawPage(m):tab==='book'?bookPage(m):goalsPage(m);
    showPanel('神社御神签',`<div class="shrine-ledger"><span>常驻小神社 · 每天一签</span><strong>${m.discovered} / 15 种签鸡</strong></div><div class="subtabs shrine-tabs" aria-label="神社页面">${[['draw','求一签'],['book','签册'],['goals','收藏回礼']].map(([id,label])=>`<button data-shrine-tab="${id}" aria-pressed="${tab===id}" class="${tab===id?'active':''}">${label}${id==='goals'&&m.goals.some(g=>g.available)?'<i aria-label="有可领取回礼"></i>':''}</button>`).join('')}</div><div class="scroll shrine-body">${content}</div><footer class="shrine-footer"><button data-shrine-back>${selected?'‹ 返回签册':'‹ 返回委托簿'}</button>${openJournal?'<button class="journal-entry" data-shrine-journal>寻宝日历 ›</button>':'<span>常驻收藏，随时回来。</span>'}</footer>`,'screen-panel shrine-screen');
    find('.close').onclick=close;
    find('[data-shrine-back]').onclick=()=>{if(selected){selectedId=null;render();}else close();};
    panels.querySelectorAll('[data-shrine-tab]').forEach(button=>button.onclick=()=>{stopReveal();tab=button.dataset.shrineTab;selectedId=null;sound(3);render();find('.shrine-body').scrollTop=0;});
    panels.querySelectorAll('[data-shrine-sign]').forEach(button=>button.onclick=()=>{selectedId=Number(button.dataset.shrineSign);sound(3);render();find('.shrine-body').scrollTop=0;});
    find('[data-shrine-draw]')?.addEventListener('click',draw);
    find('[data-shrine-prepare]')?.addEventListener('click',()=>prepareGift(68));
    find('[data-shrine-journal]')?.addEventListener('click',()=>openJournal('calendar'));
    find('[data-shrine-letters]')?.addEventListener('click',openLetters);
    panels.querySelectorAll('[data-shrine-reward]').forEach(button=>button.onclick=()=>{
      const result=commitReward(button.dataset.shrineReward);if(!result)return;
      sound(7);render();const stamp=find(`[data-shrine-claimed="${result.id}"]`);stamp.textContent=`已收下 +${count(result.cp)} CP`;stamp.focus({preventScroll:true});
    });
    panels.querySelectorAll('[data-shrine-route]').forEach(button=>button.onclick=()=>{
      const route=button.dataset.shrineRoute;
      if(route==='signs'){tab='draw';selectedId=null;render();}
      else if(route==='ducks')openDuckShop();else if(route==='yokai')openGifts();else if(route==='dim-sum')openRecipes();else openTravel();
    });
    if(old)find('.shrine-body').scrollTop=scroll;
  }
  function drawPage(m){
    const current=m.signs.find(s=>s.id===m.heldId||(!m.heldId&&s.id===m.latest));
    const held=m.held;
    const headline=held?'签已收好，去厨房赴约吧':m.gift.available?'轻摇签筒，收下今天的相遇':'小神社一直在这里等你';
    const panel=current?`<div class="shrine-slip ${revealing?'is-revealing':''}"><small>第 ${current.number} 签 · ${held?'调味料包内':'最近一次签缘'}</small><strong>${escape(current.fortune)}</strong><span>${current.found?escape(current.name):speciesLabel(0,current.id)}</span><p>${current.found?escape(current.note):'先在厨房赴约，收取后认识这位伙伴。'}</p><em>${current.found?'已收录签鸡':current.cooking?'正在厨房孵化':held?'待调理':'等待收录'}</em></div>`:`<div class="shrine-draw-scene"><div class="shrine-rope" aria-hidden="true"></div><div class="shrine-cylinder">${cylinder}</div><span>御神签</span><small>一签一位新朋友</small></div>`;
    const unmet=m.gift.conditions.filter(c=>!c.met);
    const reason=m.gift.available?'每日免费一份 · 随机签意，未收录优先':m.gift.reason;
    const progress=unmet.map(c=>`<li><span>${escape(c.label)}</span><b>${Math.min(c.current,c.target)} / ${c.target}</b></li>`).join('');
    return `<h3 class="shrine-heading">${headline}</h3>${panel}<p class="shrine-status" role="status">${escape(reason)}</p>${held?`<button class="orange shrine-main-action" data-shrine-prepare>配好下一批</button><p class="shrine-fine">鸡蛋 · 保温灯 · 御神签<br>${m.activeBatch?'当前一批继续孵化，收完后再开火。':'只选好材料；点击厨具并确认后才开火。'}</p>`:`<button class="orange shrine-main-action" data-shrine-draw ${m.gift.available?'':'disabled'}>${m.gift.claimedToday?'今天已求签':'求一签'}</button>${progress?`<ul class="shrine-requirements">${progress}</ul>`:''}${unmet.some(c=>c.kind==='activity')?'<button class="shrine-link" data-shrine-letters>去完成「神社来信」 ›</button>':''}`}<p class="shrine-fine">再次求签需隔天、新收取 24 只伙伴，并用完上一份。收录签鸡后，可以在「收藏回礼」领取 CP。</p>`;
  }
  function bookPage(m){
    return `<p class="shrine-book-intro">每一枚印记，都来自亲手孵化并收取的伙伴。</p><div class="shrine-stamps">${m.signs.map(s=>`<button data-shrine-sign="${s.id}" class="shrine-stamp ${s.found?'is-found':''}" aria-label="第${s.number}签 ${s.found?s.name:'尚未收录'}"><small>第 ${s.number} 签</small><span class="shrine-stamp-art">${art(s)}</span><strong>${s.found?escape(s.fortune):'未收录'}</strong><em>${s.found?'已集章':s.held?'签在包内':s.cooking?'孵化中':speciesLabel(0,s.id)}</em></button>`).join('')}</div>`;
  }
  function signDetail(sign,m){
    const mayReveal=sign.found;
    return `<div class="shrine-sign-detail"><small>第 ${sign.number} 签 · ${speciesLabel(0,sign.id)}</small><span class="shrine-sign-portrait">${art(sign)}</span><h3>${mayReveal?escape(sign.name):'尚未收录的签鸡'}</h3><p>${mayReveal?escape(sign.note):'每天求签，随机与15种签鸡相遇。连续未新增，第7次安排未收录目标签；仍需安全收取。'}</p><p class="shrine-status">${sign.found?'印记会永久保留，出售伙伴也不会丢失。':sign.cooking?'这一位正在厨房里，记得孵化后收取。':sign.held?'对应御神签已经放在调味料包里。':'领到对应御神签后，用鸡蛋和保温灯调理。'}</p>${sign.held?'<button class="orange shrine-main-action" data-shrine-prepare>配好下一批</button>':''}<p class="shrine-fine">保持厨房干净，并及时收取。</p></div>`;
  }
  function goalsPage(m){
    return `<p class="shrine-book-intro">把遇见的伙伴记进册子。八份常驻回礼，旧收藏也算数。</p><div class="shrine-goals">${m.goals.map(g=>`<article class="shrine-goal ${g.claimed?'is-claimed':''}"><div class="shrine-goal-title"><strong>${g.title}</strong><b>${count(g.cp)} CP</b></div><p>${g.note}</p><div class="shrine-goal-progress"><progress value="${Math.min(g.current,g.target)}" max="${g.target}" aria-label="${g.title}收藏进度"></progress><span>${Math.min(g.current,g.target)} / ${g.target}</span></div><div class="shrine-goal-action"><small>${g.reason}</small>${g.claimed?`<span class="shrine-claimed-stamp" data-shrine-claimed="${g.id}" role="status" tabindex="-1">已收下</span>`:g.available?`<button class="orange" data-shrine-reward="${g.id}">收下回礼</button>`:`<button data-shrine-route="${g.kind}">去看看 ›</button>`}</div></article>`).join('')}</div>`;
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
