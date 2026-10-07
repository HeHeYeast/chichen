import {SEASONS,SEASONAL_CHARACTERS,seasonalRecipeInfo,seasonalChapterInfo} from './seasonal-pack.js';
import {phoenixWindows,seasonCalendar} from './discovery-calendar.js';
import {getActivities} from './legacy-activities.js';
import {HOLIDAYS,holidayWindow} from './holiday-calendar.js';
import {speciesLabel} from './catalog.js';
import {kitSheet,kitButton,kitButton2,kitChip,kitChipHtml,kitLabel,kitIcon,kitCell,kitCoin} from './ui-kit.js';
import {discoveryCount} from './species-state.js';
import {helpCardsMarkup} from './game-frame.js';
const esc=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const CHECK='<img class="jn-check" src="/web/art/golden-business/family-check.png" alt="">';
const dayText=at=>{const d=new Date(at);return {date:`${d.getMonth()+1}月${d.getDate()}日`,week:d.toLocaleDateString('zh-CN',{weekday:'long'})};};
// 寻宝日历 and 四时手记 are 图鉴 pages: pictures and dates first; the rules live behind 「?」.
export function createJournalUI({getState,getNow,panels,showPanel,characterPortrait,toolPortrait=()=>'',fromBook=()=>false,preparePhoenix,claimChapter,setNotices,openActivities,openShrine,openDimSum,openToolShop,openRecipeBook,onClose}){
  let tab='calendar',chapter='spring',calendarDay='',help=false,calMonth=null,calPick=null;
  const find=s=>panels.querySelector(s);
  function open(next='calendar',key){tab=next==='recipes'?'recipes':'calendar';help=false;chapter=SEASONAL_CHARACTERS.find(c=>c.key===key)?.chapter??seasonCalendar(getNow()).current.id;if(key&&seasonalRecipeInfo(getState(),key)?.found){openRecipeBook({key});return;}render();}
  const known=(s,key)=>(s.total?.[key]??0)>0||(s.farm?.[key]??0)>0;
  // A partner coin: its sticker once collected, otherwise the 「?」 card; a tick when the letter is done.
  const who=(s,egg,id,attrs,label,done)=>`<button type="button" class="jn-who${known(s,`${egg}:${id}`)?'':' is-unknown'}" ${attrs} aria-label="${esc(label)}">${known(s,`${egg}:${id}`)?characterPortrait(egg,id):'<span class="bk-q" aria-hidden="true">?</span>'}${done?CHECK:''}</button>`;
  function calendar(){
    const s=getState(),at=getNow(),season=seasonCalendar(at),windows=phoenixWindows(s,at),activities=getActivities(s,at).filter(a=>a.kind==='campaign'),today=dayText(at);
    calendarDay=new Date(at).toDateString();
    const notices=s.events?.discoveryNotices!==false;
    const head=`<div class="kp-bar jn-today" data-row><div class="journal-date jn-date"><img src="/web/art/golden-journey/precision-clock.png" alt=""><span><b>${today.date}</b><small>${today.week}</small></span></div>
      <div class="jn-notice"><span>预告</span><button type="button" class="game-switch ${notices?'on':''}" role="switch" aria-checked="${notices}" aria-label="发现预告" data-discovery-notices><span>${notices?'开':'关'}</span><i></i></button></div></div>`;
    // Both phoenixes need the same Lv.3 lamp, so the lamp and its one action sit above the two rows.
    const ready=windows[0].ready;
    const lamp=`<div class="kp-bar jn-lamp" data-row><span class="jn-need${ready?' met':''}">${toolPortrait(0,2)}<b>Lv.3 保温灯</b>${ready?CHECK:''}</span>${ready?kitButton('去准备',`data-window-go="${windows[0].id}"`):kitButton2('看厨具',`data-window-go="${windows[0].id}"`)}</div>`;
    const phoenix=`${kitLabel('凤凰时段')}${lamp}<div class="jn-windows">${windows.map(w=>`<article data-window-id="${w.id}" class="jn-window${w.active?' is-open':''}"><span class="jn-window-art">${characterPortrait(0,w.id)}${w.found?'<img class="jn-seal" src="/web/art/golden-collection/stamp-done.png" alt="已收录">':''}</span>
      <div class="jn-window-text"><strong>${esc(w.name)}</strong><span class="jn-time">${esc(w.window.replace('每天 ','').replace('次日 ','次日'))}</span></div><b class="jn-status" data-window-status>${esc(w.status)}</b></article>`).join('')}</div>`;
    // A wall calendar: one month of days, each festival a coloured band across its days with its partner
    // on the first day; tap a festival day to see who comes and their letter. ‹ › turn the month.
    const now=new Date(at);if(!calMonth)calMonth={y:now.getFullYear(),m:now.getMonth()};
    const first=new Date(calMonth.y,calMonth.m,1),next=new Date(calMonth.y,calMonth.m+1,1),days=new Date(calMonth.y,calMonth.m+1,0).getDate();
    const wins=HOLIDAYS.map(h=>holidayWindow(h,first.getTime())).filter(w=>w.start!=null&&w.start<next.getTime()&&w.end>first.getTime());
    const tint=w=>['#f6c9cf','#f6c9cf','#f8d3de','#f8d3de','#d5e9bf','#c6e3ee','#c6e3ee','#c6e3ee','#f3d6a8','#f3d6a8','#f3d6a8','#ddd2ef'][new Date(w.start).getMonth()];
    const pick=wins.find(w=>w.id===calPick)??wins.find(w=>w.active)??wins.find(w=>w.start>=now.getTime())??wins[0];
    const firstKey=w=>{const [egg,id]=w.keys[0].split(':').map(Number);return {egg,id};};
    const cells=Array.from({length:(first.getDay()+6)%7},()=>'<span class="jn-day is-blank"></span>').join('')+Array.from({length:days},(_,n)=>{
      const d=n+1,t=new Date(calMonth.y,calMonth.m,d).getTime(),hs=wins.filter(w=>t+864e5>w.start&&t<w.end).sort((a,b)=>(a.end-a.start)-(b.end-b.start)),w=hs[0],isToday=new Date(t).toDateString()===now.toDateString();
      const head=w&&(new Date(w.start).toDateString()===new Date(t).toDateString()||d===1);
      const k=w?firstKey(w):null;
      const cls=`jn-day${isToday?' is-today':''}${w?' has-h':''}${hs.some(x=>pick&&x.id===pick.id)?' is-picked':''}`;
      return w?`<button type="button" class="${cls}" style="--h:${tint(w)}" data-cal-holiday="${w.id}" aria-label="${calMonth.m+1}月${d}日 ${esc(w.title)}"><b>${d}</b>${head?`<span class="jn-day-who${known(s,`${k.egg}:${k.id}`)?'':' is-unknown'}">${known(s,`${k.egg}:${k.id}`)?characterPortrait(k.egg,k.id):'<span class="bk-q" aria-hidden="true">?</span>'}</span>`:''}</button>`
        :`<span class="${cls}"><b>${d}</b></span>`;}).join('');
    const detail=pick?(()=>{const entries=pick.keys.map(key=>{const [egg,id]=key.split(':').map(Number);return {egg,id,activity:activities.find(a=>a.characters.some(c=>c.egg===egg&&c.id===id))};});
      return `<article class="jn-holiday${pick.active?' is-open':pick.preview?' is-soon':''}" data-holiday="${pick.id}" style="--h:${tint(pick)}"><div class="jn-holiday-text"><div class="jn-holiday-title"><strong>${esc(pick.title)}</strong>${pick.active||pick.preview?kitChip('',pick.status,'mini'+(pick.active?'':' hot')):''}</div><span class="jn-holiday-date">${esc(pick.dateRange)}</span></div>
        <div class="jn-holiday-who">${entries.map(({egg,id,activity})=>who(s,egg,id,`data-journal-activity="${activity.id}"`,`${speciesLabel(egg,id)} · ${activity.completed?'资格已备好':'查看委托'}`,activity.completed)).join('')}</div></article>`;})()
      :'<p class="jn-cal-none">这个月没有节日伙伴</p>';
    const holidays=`${kitLabel('节日挂历')}<section class="jn-wallcal" aria-label="${calMonth.y}年${calMonth.m+1}月"><img class="jn-wallcal-clip" src="/web/art/golden-business/family-clip.png" alt="" data-overhang>
      <div class="jn-cal-head" data-row><button type="button" class="jn-cal-turn" data-cal-turn="-1" aria-label="上个月">‹</button><b>${calMonth.y}年 ${calMonth.m+1}月</b><button type="button" class="jn-cal-turn" data-cal-turn="1" aria-label="下个月">›</button></div>
      <div class="jn-cal-week" aria-hidden="true">${'一二三四五六日'.split('').map(x=>`<span>${x}</span>`).join('')}</div><div class="jn-cal-grid">${cells}</div></section>${detail}`;
    const letters=`${kitLabel('伙伴来信')}<div class="sr-envs jn-letters" data-list>${activities.map(a=>{const c=a.characters[0],k=`${c.egg}:${c.id}`;return `<button type="button" class="sr-letter-env${a.completed?' is-open':''}${a.available?' is-ready':''}" data-journal-activity="${a.id}" aria-label="${esc(a.title)} · ${a.completed?'资格已获得':a.available?'可完成':'查看条件'}"><span class="sr-env-art"><img src="${a.completed?'/web/art/golden-journey/precision-envelope-back.png':'/web/art/golden-business/family-envelope.png'}" alt=""><span class="sr-env-stamp${known(s,k)?'':' is-unknown'}">${known(s,k)?characterPortrait(c.egg,c.id):'<span class="bk-q" aria-hidden="true">?</span>'}</span>${a.available?'<b class="sr-env-flag">可完成</b>':''}</span><b class="sr-env-title">${esc(a.title)}</b></button>`;}).join('')}</div>`;
    const ch=seasonalChapterInfo(s,season.current.id),switchDay=new Date(season.start);
    const seasonCard=`${kitLabel(`当季 · ${season.current.title}`)}<div class="jn-season"><div class="jn-season-who">${ch.characters.map(c=>`<span class="jn-who${known(s,c.key)?'':' is-unknown'}">${known(s,c.key)?characterPortrait(c.egg,c.id):'<span class="bk-q" aria-hidden="true">?</span>'}</span>`).join('')}</div>
      <div class="gd-row jn-season-foot">${kitChip('',season.preview?`${season.days} 天后换「${season.next.title}」`:`${switchDay.getMonth()+1}月1日换「${season.next.title}」`,'mini')}${kitButton2('四时手记','data-journal-recipes')}</div></div>`;
    const links=`${kitLabel('也看看')}<div class="jn-links"><button type="button" class="jn-link" data-journal-shrine><img src="/web/art/ui-kit/shrine-snapshot.png" alt=""><b>御神签</b></button><button type="button" class="jn-link" data-journal-dim><span class="jn-link-art">${toolPortrait(8,Math.max(0,s.toolLevels?.[8]??0))}</span><b>竹蒸笼配方</b></button></div>`;
    return `${head}${phoenix}${holidays}${letters}${seasonCard}${links}`;
  }
  function recipes(){
    const s=getState(),ch=seasonalChapterInfo(s,chapter),found=discoveryCount(s);
    const rules=[[s.kitchenLevel>=1,'厨房 Lv.2'],[found>=12,found>=12?'认识 12 种':`认识 ${found}/12 种`]];
    const pills=`<div class="kp-coins jn-chapters" role="group" aria-label="四季">${SEASONS.map(c=>kitCoin(c.title[0],`data-journal-chapter="${c.id}"`,c.id===chapter,c.title)).join('')}</div>`;
    const conds=`<div class="jn-rules">${rules.map(([met,label])=>kitChipHtml(`${met?CHECK:''}${esc(label)}`,'mini'+(met?'':' hot'))).join('')}</div>`;
    const grid=`<div class="bk-grid jn-recipes">${ch.characters.map(c=>{const r=seasonalRecipeInfo(s,c.key);return `<button type="button" ${r.found?`data-seasonal-recipe="${c.key}"`:'disabled'} class="jn-recipe bk-sticker ${r.found?'is-known':'is-unknown'}"><span class="bk-sticker-art">${r.found?characterPortrait(c.egg,c.id):'<span class="bk-q" aria-hidden="true">?</span>'}</span><strong class="collection-name">${r.found?esc(c.title_zh_CN):'尚未收录'}</strong><span class="collection-code">${r.found?'看配方 ›':speciesLabel(c.egg,c.id)}</span></button>`;}).join('')}</div>`;
    // The label counts the four; the row under the stickers is the 600 CP gift and its button.
    const reward=`<div class="kp-bar jn-reward" data-row>${kitChipHtml(`${kitIcon.coin}${ch.cp}`,'mini')}${ch.claimed?kitChip('','回礼已领','mini soft'):kitButton('领取',`data-seasonal-reward="${chapter}"${ch.available?'':' disabled'}`)}</div>`;
    return `${pills}${kitLabel(`${ch.title} · ${ch.found}/4`)}${conds}${grid}${reward}`;
  }
  function helpSheet(){
    if(!help)return '';
    return `<div class="kp-scrim" data-journal-help-close></div><section class="kp-drawer gd jn-help" role="dialog" aria-modal="true" aria-label="这页怎么看"><div class="kp-drawer-title" data-art="plank"><span data-safe><b>这页怎么看</b></span></div><div class="kp-drawer-body">${helpCardsMarkup(tab==='recipes'?'seasonal':'calendar')}<div class="gd-actions" data-row>${kitButton('知道了','data-journal-help-close')}</div></div></section>`;
  }
  function render(){
    const old=find('.journal-scroll'),scroll=old?.scrollTop??0,book=fromBook();
    const tabs=`<div class="gd-scroll-row bk-cats journal-tabs" role="group" aria-label="日历分页">${[['calendar','寻宝日历'],['recipes','四时手记']].map(([id,label])=>`<button type="button" class="gd-btn2 mini" data-journal-tab="${id}" aria-pressed="${tab===id}">${label}</button>`).join('')}</div>`;
    showPanel('寻宝日历与四时手记',`${kitSheet(`${tabs}${tab==='calendar'?calendar():recipes()}`,book?'':kitButton('返回','data-journal-back'),'bk-sheet jn-sheet'+(tab==='recipes'?' jn-seasonal':''),'',{cls:'journal-scroll',attrs:tab==='calendar'?'data-list':''})}${helpSheet()}`,'screen-panel journal-screen',{skin:'book',icon:characterPortrait(0,0),help:'data-journal-help',back:!book});
    find('.close').onclick=onClose;find('[data-journal-back]')?.addEventListener('click',onClose);
    const top=()=>{const el=find('.journal-scroll');if(el)el.scrollTop=0;};
    panels.querySelectorAll('[data-journal-tab]').forEach(b=>b.onclick=()=>{tab=b.dataset.journalTab;render();top();});
    panels.querySelectorAll('[data-journal-chapter]').forEach(b=>b.onclick=()=>{chapter=b.dataset.journalChapter;render();top();});
    panels.querySelectorAll('[data-seasonal-recipe]').forEach(b=>b.onclick=()=>openRecipeBook({key:b.dataset.seasonalRecipe}));
    panels.querySelectorAll('[data-journal-activity]').forEach(b=>b.onclick=()=>openActivities(b.dataset.journalActivity));
    panels.querySelectorAll('[data-window-go]').forEach(b=>b.onclick=()=>{const w=phoenixWindows(getState(),getNow()).find(w=>w.id===+b.dataset.windowGo);if(w.ready)preparePhoenix();else openToolShop(0);});
    find('[data-journal-recipes]')?.addEventListener('click',()=>{tab='recipes';chapter=seasonCalendar(getNow()).current.id;render();top();});
    find('[data-journal-shrine]')?.addEventListener('click',openShrine);find('[data-journal-dim]')?.addEventListener('click',openDimSum);
    find('[data-discovery-notices]')?.addEventListener('click',()=>{if(setNotices(getState().events?.discoveryNotices===false))render();});
    find('[data-seasonal-reward]')?.addEventListener('click',()=>{if(claimChapter(chapter))render();});
    find('[data-journal-help]')?.addEventListener('click',()=>{help=true;render();});
    panels.querySelectorAll('[data-cal-turn]').forEach(b=>b.onclick=()=>{const d=new Date(calMonth.y,calMonth.m+Number(b.dataset.calTurn),1);calMonth={y:d.getFullYear(),m:d.getMonth()};calPick=null;render();});
    panels.querySelectorAll('[data-cal-holiday]').forEach(b=>b.onclick=()=>{calPick=b.dataset.calHoliday;render();});
    panels.querySelectorAll('[data-journal-help-close]').forEach(b=>b.onclick=()=>{help=false;render();});
    if(old)find('.journal-scroll').scrollTop=scroll;
  }
  return {open,resume:render,refresh(){if(find('.journal-screen'))render();},updateTime(){
    if(!find('.journal-date'))return;
    if(new Date(getNow()).toDateString()!==calendarDay){render();return;}
    for(const w of phoenixWindows(getState(),getNow())){const card=find(`[data-window-id="${w.id}"]`);if(!card)continue;card.classList.toggle('is-open',w.active);card.querySelector('[data-window-status]').textContent=w.status;}
  }};
}
