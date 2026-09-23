import {DATA} from './data.js';
import {SEASONS} from './seasonal-pack.js';
import {holidayNotice} from './holiday-calendar.js';
// All times are local, matching originalRecipes(new Date(now).getHours()).
export function phoenixWindows(state,now=Date.now()){
  const today=new Date(now),at=(day,hour)=>{const d=new Date(today);d.setDate(d.getDate()+day);d.setHours(hour,0,0,0);return d.getTime();};
  const hour=today.getHours(),hot=hour>=10&&hour<13;
  return [52,51].map(id=>{
    const active=id===52?hot:!hot;
    const start=id===52?(hour<13?at(0,10):at(1,10)):(hour<10?at(-1,13):at(0,13));
    const nextStart=active?start:id===52?(hour<10?at(0,10):at(1,10)):at(0,13);
    const end=id===52?(hour<13?at(0,13):at(1,13)):(hour<10?at(0,10):at(1,10));
    const minutes=Math.max(0,Math.ceil((nextStart-now)/60000));
    return {id,name:DATA.characters[0][id].title_zh_CN,active,start:nextStart,end,minutes,
      ready:(state.toolLevels?.[0]??-1)>=2,found:(state.total?.['0:'+id]??0)>0||(state.farm?.['0:'+id]??0)>0,
      window:id===52?'每天 10:00–12:59':'每天 13:00–次日 09:59',
      status:active?'窗口开放中':minutes<=60?`${minutes} 分钟后开放`:`下次 ${new Date(nextStart).getDate()===today.getDate()?'今天':'明天'} ${id===52?'10:00':'13:00'}`};
  });
}
export function calendarNotice(state,now=Date.now()){
  if(state.events?.discoveryNotices===false)return null;
  const next=phoenixWindows(state,now).find(w=>w.ready&&!w.found&&!w.active&&w.minutes>0&&w.minutes<=60);
  return next?{key:`${next.id}:${next.start}`,message:`${next.name}将在 ${next.minutes} 分钟后进入可遇见时段。用 Lv.3 保温灯开火，详见寻宝日历。`}:holidayNotice(state,now);
}
export function seasonCalendar(now=Date.now()){
  const date=new Date(now),month=date.getMonth()+1,current=SEASONS.find(s=>s.months.includes(month));
  const next=SEASONS[(SEASONS.indexOf(current)+1)%4],nextMonth=next.months[0];
  const start=new Date(date.getFullYear()+(nextMonth<=month?1:0),nextMonth-1,1);
  // Calendar-day difference, independent of daylight-saving clock changes.
  const days=Math.round((Date.UTC(start.getFullYear(),start.getMonth(),1)-Date.UTC(date.getFullYear(),date.getMonth(),date.getDate()))/86400000);
  return {current,next,start:start.getTime(),days,preview:days<=7};
}
