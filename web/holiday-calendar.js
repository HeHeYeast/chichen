// Offline annual calendar. Original campaign dates were supplied by a server;
// these explicit local-date windows replace that unavailable service.
export const HOLIDAYS=Object.freeze([
  {id:'valentine',title:'情人节',rule:'每年 2月7日—14日',dates:[2,7,2,14],keys:['0:32']},
  {id:'sakura',title:'樱花季',rule:'每年 3月20日—4月10日',dates:[3,20,4,10],keys:['0:48']},
  {id:'april-fools',title:'愚人节',rule:'每年 4月1日',dates:[4,1,4,1],keys:['0:49']},
  {id:'mothers-day',title:'母亲节',rule:'每年 5月第二个星期日',weekday:[5,0,2],keys:['0:60']},
  {id:'rain',title:'梅雨季',rule:'每年 6月1日—30日',dates:[6,1,6,30],keys:['0:61','0:62']},
  {id:'summer',title:'夏日祭',rule:'每年 7月1日—8月31日',dates:[7,1,8,31],keys:['0:63','0:65','1:35']},
  {id:'fireworks',title:'烟火大会',rule:'每年 8月1日—31日',dates:[8,1,8,31],keys:['0:64']},
  {id:'qixi',title:'七夕',rule:'每年 农历七月初七',lunar:[7,7,1],keys:['0:66']},
  {id:'mid-autumn',title:'中秋节',rule:'每年 农历八月十五',lunar:[8,15,1],keys:['0:78','1:47']},
  {id:'chestnut',title:'栗子季',rule:'每年 9月1日—11月30日',dates:[9,1,11,30],keys:['0:79']},
  {id:'maple',title:'红叶季',rule:'每年 10月1日—11月30日',dates:[10,1,11,30],keys:['1:48']},
  {id:'halloween',title:'万圣节',rule:'每年 10月25日—31日',dates:[10,25,10,31],keys:['0:80','0:67']},
  {id:'thanksgiving',title:'感恩节',rule:'每年 11月第四个星期四',weekday:[11,4,4],keys:['0:82']},
  {id:'christmas',title:'圣诞节',rule:'每年 12月18日—25日',dates:[12,18,12,25],keys:['0:26','0:27','0:83','1:49','1:50']},
  {id:'new-year',title:'元旦',rule:'每年 1月1日—7日',dates:[1,1,1,7],keys:['0:84','0:85']},
  {id:'plum',title:'赏梅季',rule:'每年 1月1日—2月底',dates:[1,1,3,0],keys:['0:86']},
  {id:'spring-festival',title:'春节',rule:'每年 农历正月初一至初七',lunar:[1,1,7],keys:['0:87','1:51']},
].map(h=>Object.freeze({...h,keys:Object.freeze(h.keys)})));
const byKey=new Map(HOLIDAYS.flatMap(h=>h.keys.map(key=>[key,h]))),cache=new Map();
let lunarFormatter,lunarZone;
const local=(year,month,day)=>new Date(year,month-1,day);
function annualWindow(h,year){
  const zone=new Intl.DateTimeFormat().resolvedOptions().timeZone;
  const key=`${h.id}:${year}:${zone}`;if(cache.has(key))return cache.get(key);
  let start,end;
  if(h.dates){const [m,d,em,ed]=h.dates;start=local(year,m,d);end=local(year,em,ed+1);}
  else if(h.weekday){const [month,weekday,n]=h.weekday,first=local(year,month,1);start=local(year,month,1+(weekday-first.getDay()+7)%7+7*(n-1));end=new Date(start);end.setDate(end.getDate()+1);}
  else{
    if(!lunarFormatter||lunarZone!==zone){lunarFormatter=new Intl.DateTimeFormat('en-u-ca-chinese',{month:'numeric',day:'numeric',timeZone:zone});lunarZone=zone;}
    if(lunarFormatter.resolvedOptions().calendar==='chinese'){
      for(const d=new Date(year,0,1,12);d.getFullYear()===year;d.setDate(d.getDate()+1)){
        const parts=lunarFormatter.formatToParts(d),month=parts.find(p=>p.type==='month')?.value,day=parts.find(p=>p.type==='day')?.value;
        // Exact month text excludes a leap-month duplicate (e.g. "6bis").
        if(month===String(h.lunar[0])&&Number(day)===h.lunar[1]){start=local(year,d.getMonth()+1,d.getDate());end=new Date(start);end.setDate(end.getDate()+h.lunar[2]);break;}
      }
    }
  }
  const result=start?{start:start.getTime(),end:end.getTime()}:null;cache.set(key,result);return result;
}
const dateText=value=>{const d=new Date(value);return `${d.getFullYear()}年${d.getMonth()+1}月${d.getDate()}日`;};
const dayNumber=value=>{const d=new Date(value);return Date.UTC(d.getFullYear(),d.getMonth(),d.getDate())/86400000;};
export function holidayWindow(holiday,now=Date.now()){
  const h=typeof holiday==='string'?HOLIDAYS.find(h=>h.id===holiday):holiday;if(!h)return null;
  const year=new Date(now).getFullYear();let window=annualWindow(h,year);
  if(window&&now>=window.end)window=annualWindow(h,year+1);
  if(!window)return {...h,active:false,days:Infinity,preview:false,dateRange:h.rule,status:'日期暂不可用'};
  const active=now>=window.start&&now<window.end,days=Math.max(0,dayNumber(window.start)-dayNumber(now));
  const last=new Date(window.end);last.setDate(last.getDate()-1);
  const dateRange=dateText(window.start)+(last.getTime()===window.start?'':`—${last.getMonth()+1}月${last.getDate()}日`);
  return {...h,...window,active,days,preview:!active&&days<=7,dateRange,status:active?'开放中':days<=7?`${days} 天后开放`:'等待节日'};
}
export const holidayForCharacter=(egg,id,now=Date.now())=>holidayWindow(byKey.get(`${egg}:${id}`),now);
export const holidayCalendar=(now=Date.now())=>HOLIDAYS.map(h=>holidayWindow(h,now)).sort((a,b)=>Number(b.active)-Number(a.active)||(a.start??Infinity)-(b.start??Infinity));
const flag=key=>'campaign_char_'+key.replace(':','_');
// Filter only the recipe input; preserve permanent qualifications, discoveries,
// owned creatures, and results that were already rolled for an existing batch.
export function recipeStateAt(state,now=Date.now()){
  const events={...state.events};
  for(const h of HOLIDAYS)if(!holidayWindow(h,now).active)for(const key of h.keys)events[flag(key)]=false;
  return {...state,events};
}
export function holidayNotice(state,now=Date.now()){
  const h=holidayCalendar(now).find(h=>(h.active||h.preview)&&h.keys.some(key=>state.events?.[flag(key)]===true&&!(state.total?.[key]>0||state.farm?.[key]>0)));
  return h?{key:`holiday:${h.id}:${h.start}`,message:`${h.title}${h.active?'已开放':`将在 ${h.days} 天后开放`}（${h.dateRange}）。活动伙伴只在开放日期内开火才有机会遇见，详见寻宝日历。`}:null;
}
