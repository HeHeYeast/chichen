import {SEASONS,SEASONAL_CHARACTERS,seasonalRecipeInfo,seasonalChapterInfo} from './seasonal-pack.js';
import {phoenixWindows,seasonCalendar} from './discovery-calendar.js';
import {getActivities} from './legacy-activities.js';
import {holidayCalendar} from './holiday-calendar.js';
import {speciesLabel} from './catalog.js';
export function createJournalUI({getState,getNow,panels,showPanel,characterPortrait,preparePhoenix,claimChapter,setNotices,openActivities,openShrine,openDimSum,openToolShop,openRecipeBook,onClose}){
  let tab='calendar',chapter='spring',calendarDay='';
  const find=s=>panels.querySelector(s);
  function open(next='calendar',key){tab=next==='recipes'?'recipes':'calendar';chapter=SEASONAL_CHARACTERS.find(c=>c.key===key)?.chapter??seasonCalendar(getNow()).current.id;if(key&&seasonalRecipeInfo(getState(),key)?.found){openRecipeBook({key});return;}render();}
  function calendar(){
    const s=getState(),at=getNow(),season=seasonCalendar(at),windows=phoenixWindows(s,at),activities=getActivities(s,at).filter(a=>a.kind==='campaign');
    calendarDay=new Date(at).toDateString();
    return `<div class="journal-date"><b>${new Date(at).toLocaleDateString('zh-CN',{month:'long',day:'numeric',weekday:'long'})}</b><span>按手机本地时间</span></div>
      <h3>每天的凤凰时段</h3><p class="journal-note">看的是<strong>开始调理</strong>的时间，跨过时段后才孵化也可以。先备好 Lv.3 保温灯。</p>
      ${windows.map(w=>`<article data-window-id="${w.id}" class="journal-window ${w.active?'is-open':''}"><div class="journal-window-art">${characterPortrait(0,w.id)}</div><div><strong>${w.name}</strong><small>${w.window}</small><b>${w.status}</b><small>${w.found?'图鉴已收录':w.ready?'厨具已备好':'还需要保温灯 Lv.3'}</small></div><button data-window-go="${w.id}">${w.ready?'去准备':'看厨具'} ›</button></article>`).join('')}
      <p class="journal-note">每批开火有10%机会开启本时段凤凰的出现机会；开启后仍需逐枚抽取，不保证这一批能收到。</p>
      <button class="journal-reminder" role="switch" aria-checked="${s.events?.discoveryNotices!==false}" data-discovery-notices>发现预告 <b>${s.events?.discoveryNotices===false?'关':'开'}</b><small>节日前 7 天、凤凰时段前 60 分钟，回到厨房提示已具备资格但未收录的伙伴。仅游戏内提示。</small></button>
      <h3>节日伙伴 · 按日期相遇</h3><p class="journal-note">先完成委托取得资格，再在对应日期<strong>开火</strong>。已有伙伴和配方一直保留，节日结束后暂时不能再次孵出。以下为本作离线年度日历。</p>
      <div class="journal-holidays">${holidayCalendar(at).map(h=>{const entries=h.keys.map(key=>{const [egg,id]=key.split(':').map(Number);return {egg,id,activity:activities.find(a=>a.characters.some(c=>c.egg===egg&&c.id===id))};});return `<article class="journal-holiday ${h.active?'is-open':h.preview?'is-soon':''}" data-holiday="${h.id}"><header><strong>${h.title}</strong><b>${h.status}</b></header><p>${h.dateRange}</p><small>${h.rule} · 当地时间 00:00 至末日 23:59</small><div>${entries.map(({egg,id,activity})=>`<button data-journal-activity="${activity.id}">${speciesLabel(egg,id)} · ${activity.completed?'资格已备好':'查看委托'} ›</button>`).join('')}</div></article>`;}).join('')}</div>
      <h3>伙伴来信 · 可提前准备</h3><p class="journal-note">委托进度与资格永久保留；神社签礼、灶火与木绵小礼照常开放。</p>
      <div class="journal-letters">${activities.map(a=>`<button data-journal-activity="${a.id}"><span>${a.title}</span><small>${a.completed?'资格已获得':a.available?'可完成':'查看条件'} ›</small></button>`).join('')}</div>
      <h3>${season.current.title} · 当季推荐</h3><p class="journal-note">四时手作的 16 位伙伴全年都可发现，收录后解锁配方。${season.preview?`还有 ${season.days} 天换季，提前看看「${season.next.title}」。`:`下次推荐 ${new Date(season.start).getMonth()+1} 月 1 日切换为「${season.next.title}」，提前 7 天在此预告。`}</p>
      <button class="orange journal-wide" data-journal-recipes>四时收集手记 · 8 鸡 + 8 鸭</button>
      <div class="journal-shortcuts"><button data-journal-shrine>御神签 · 每天一份</button><button data-journal-dim>图鉴配方册 · 竹蒸笼</button></div>`;
  }
  function recipes(){
    const s=getState(),ch=seasonalChapterInfo(s,chapter);
    return `<p class="journal-note">厨房 Lv.2、认识 12 种伙伴后，合适的甜品搭配有机会遇见手作伙伴。首次收取后，图鉴配方册开放定向制作。</p>
      <div class="journal-chapters">${SEASONS.map(c=>`<button data-journal-chapter="${c.id}" aria-pressed="${c.id===chapter}">${c.title}</button>`).join('')}</div>
      <div class="journal-recipe-grid">${ch.characters.map(c=>{const r=seasonalRecipeInfo(s,c.key);return `<button ${r.found?`data-seasonal-recipe="${c.key}"`:'disabled'} class="journal-recipe-card"><span class="journal-recipe-art">${r.found?characterPortrait(c.egg,c.id):'<span class="collection-unknown-egg"><span>?</span></span>'}</span><strong>${r.found?c.title_zh_CN:'尚未收录'}</strong><small>${r.found?'查看已解锁配方':`${c.egg?'D':'C'}${c.id+1} · 发现后解锁`}</small></button>`;}).join('')}</div>
      <div class="journal-chapter-reward"><span>章节集章 ${ch.found} / 4<small>收齐这四位，领取 600 CP · 旧收藏计入</small></span>${ch.claimed?'<b>回礼已领</b>':`<button class="orange" data-seasonal-reward="${chapter}" ${ch.available?'':'disabled'}>领取回礼</button>`}</div>
      <p class="journal-note">全年可发现，不需要等待季节。已解锁配方统一保存在图鉴，按厨具分类。</p>`;
  }
  function render(){
    const old=find('.journal-scroll'),scroll=old?.scrollTop??0;
    showPanel('寻宝日历与四时手记',`<div class="subtabs journal-tabs"><button data-journal-tab="calendar" aria-pressed="${tab==='calendar'}" class="${tab==='calendar'?'active':''}">寻宝日历</button><button data-journal-tab="recipes" aria-pressed="${tab==='recipes'}" class="${tab==='recipes'?'active':''}">四时手记 · 16 位</button></div><div class="scroll journal-scroll">${tab==='calendar'?calendar():recipes()}</div><footer class="journal-footer"><button data-journal-back>‹ 返回游戏</button><span>配方首次收录后解锁</span></footer>`,'screen-panel journal-screen');
    find('.close').onclick=onClose;find('[data-journal-back]').onclick=onClose;
    panels.querySelectorAll('[data-journal-tab]').forEach(b=>b.onclick=()=>{tab=b.dataset.journalTab;render();find('.journal-scroll').scrollTop=0;});
    panels.querySelectorAll('[data-journal-chapter]').forEach(b=>b.onclick=()=>{chapter=b.dataset.journalChapter;render();find('.journal-scroll').scrollTop=0;});
    panels.querySelectorAll('[data-seasonal-recipe]').forEach(b=>b.onclick=()=>openRecipeBook({key:b.dataset.seasonalRecipe}));
    panels.querySelectorAll('[data-journal-activity]').forEach(b=>b.onclick=()=>openActivities(b.dataset.journalActivity));
    panels.querySelectorAll('[data-window-go]').forEach(b=>b.onclick=()=>{const w=phoenixWindows(getState(),getNow()).find(w=>w.id===+b.dataset.windowGo);if(w.ready)preparePhoenix();else openToolShop(0);});
    find('[data-journal-recipes]')?.addEventListener('click',()=>{tab='recipes';render();find('.journal-scroll').scrollTop=0;});
    find('[data-journal-shrine]')?.addEventListener('click',openShrine);find('[data-journal-dim]')?.addEventListener('click',openDimSum);
    find('[data-discovery-notices]')?.addEventListener('click',()=>{if(setNotices(getState().events?.discoveryNotices===false))render();});
    find('[data-seasonal-reward]')?.addEventListener('click',()=>{if(claimChapter(chapter))render();});
    if(old)find('.journal-scroll').scrollTop=scroll;
  }
  return {open,resume:render,refresh(){if(find('.journal-screen'))render();},updateTime(){
    if(!find('.journal-date'))return;
    if(new Date(getNow()).toDateString()!==calendarDay){render();return;}
    find('.journal-date b').textContent=new Date(getNow()).toLocaleDateString('zh-CN',{month:'long',day:'numeric',weekday:'long'});
    for(const w of phoenixWindows(getState(),getNow())){const card=find(`[data-window-id="${w.id}"]`);if(!card)continue;card.classList.toggle('is-open',w.active);card.querySelector('b').textContent=w.status;}
  }};
}
