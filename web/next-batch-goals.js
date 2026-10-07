// 下一锅「推荐」: the few batches most worth making right now, each with the one reason that makes it worth it.
// Every card can be started now — conditions met, seasonings in stock or for sale, CP enough — the same hard rules as the
// other recommendations, and the second seasoning is never picked for the player (an investigation opens 自己选 with what the
// clues say). The tracked partner (线索册「追踪」) always comes first. The rest are ranked by how far a batch moves the game on:
// it finishes something in one go, brings a partner not met yet, nearly fills an order, or tries a nearly worked-out recipe;
// money comes last. The value stays inside: a card only says why.
// Wants (batch 3): orders and 询问 that take partners (交付), 「只看不交」 orders still short of a kind, the 厨房往事 chapter,
// project deliveries, and today's menu (its core partners still short at home). One card per set: a set that also fills
// other wants says so in a short 「也能补…」 note instead of becoming a second card. Opened from 生意 with a goal (a menu
// slot or an order), the card for that goal comes first.
// A guess becomes a card only once the clues leave a few seasonings to try (the second one's group, clue-book GUESS_MAX): a
// partner known only by its cookware or its first seasoning is left to 寻访 (its next clue is one trip away) and to the
// 线索册, where trying it is still allowed.
// Regional partners (loop batch 4) whose complete method is held get the same 「能出新伙伴」 card, made in the region's trial
// mode (exact set, 25% a batch, sure by the fourth); the tracked one leads as any tracked partner does.
// A cookware that has not hatched anyone of this egg yet (just bought, user 2026-10-07) gets one 「先空着做一锅」 card: every
// cookware has a dish that needs no seasoning (竹蒸笼: 小笼包鸡), so its first empty batch brings a partner not met yet. The
// card shows no odds and names nobody; it is offered only when that empty pot really holds an unmet partner.
// Read-only: nothing here changes the save.
import * as E from './engine.js';
import {speciesCode} from './knowledge.js';
import {seasoningAdvice,rankAdvice,guessDirections,openDemands,expectedOutcome,NEW_MIN} from './seasoning-advisor.js';
import {RECIPE_CATALOG} from './recipe-book.js';
import {trackedRow,clueRow,narrowed,TRIP_LAYER_NAMES} from './clue-book.js';
import {regionalRows,regionalHeld} from './regional-clues.js';
import {speciesDiscovered} from './species-state.js';
import {trackedFound} from './knowledge.js';
import {menuDemands,storyDemands,displayDemands} from './business-home.js';

export const GOAL_CARDS=4;
// No more than this many cards of one kind, so four orders never push out a new partner; money gets one at most.
const PER_KIND={track:1,new:2,order:2,menu:1,guess:2,income:1,tool:1,night:1};
const setKey=ids=>[...ids].sort((a,b)=>a-b).join('+');
export const rateOf=r=>r.profit/Math.max(1/60,r.minutes/60);
export const eggsOf=s=>s.duck?[0,1]:[0];

// Per save snapshot: every owned cookware's advice for one egg, and the 推测方向. A committed save is a new object, but a
// draft can be changed in place, so the cache also checks what the advice depends on (and the minute, for dated recipes).
const adviceCache=new WeakMap(),guessCache=new WeakMap();
const signature=(s,now,menuId=null)=>JSON.stringify([s.cp,s.kitchenLevel,s.toolLevels,s.ingredients,s.duck,Object.keys(s.total??{}).length,s.progress?.knowledge?.facts?.length,s.progress?.knowledge?.recipes?.length,s.expansion?.orders?.active?.length,s.expansion?.orders?.proposals?.length,!!s.expansion?.business?.active,menuId,Math.floor(now/60000)]);
const cached=(cache,s,now,menuId=null)=>{const sig=signature(s,now,menuId),hit=cache.get(s);if(hit?.sig===sig)return hit.byEgg;const byEgg={};cache.set(s,{sig,byEgg});return byEgg;};
// Everything a batch could be made for (seasoning-advisor's orders, 询问 and projects, plus 生意's own wants).
export function wants(s,now,menuId=null){return [...openDemands(s,now),...displayDemands(s,now),...storyDemands(s),...menuDemands(s,menuId,now)];}
export function allAdvice(s,egg,now,{menuId=null}={}){
  const byEgg=cached(adviceCache,s,now,menuId);if(byEgg[egg])return byEgg[egg];
  const view=s.egg===egg?s:{...s,egg},out=[],demands=wants(view,now,menuId);
  for(const [id,level]of s.toolLevels.entries()){if(!Number.isInteger(level)||level<0)continue;const a=seasoningAdvice(view,id,now,{demands});if(a.owned&&!a.eggBlocked)out.push(a);}
  return byEgg[egg]=out;
}
export function directions(s,egg,now){const byEgg=cached(guessCache,s,now);return byEgg[egg]??=guessDirections(s,egg,now);}
export {narrowed};

// The cooking fee for a cookware with nothing in it: the least a guess will cost.
function baseFee(s,egg,toolId,now){try{return E.cookInfo({...s,selected:[],egg},toolId,now).cost;}catch{return E.tool(toolId)[`lv_${s.toolLevels[toolId]}_cook_cp`]??0;}}
const findRow=(s,egg,toolId,ingredients,now,menuId=null)=>allAdvice(s,egg,now,{menuId}).find(a=>a.toolId===toolId)?.rows.find(r=>r.key===setKey(ingredients))??null;
const hint=(g)=>g.group?`第二味：${g.group}类`:g.first!=null?`第一味：${E.label(E.ingredient(g.first))}`:'照谜语配一锅';
// How close a 推测方向 is to done (out of 4 before the recipe is held), and how few seasonings are left to try.
const guessValue=g=>20+8*Math.min(4,g.depth)+(g.group?10:0)+(g.candidates&&g.candidates.length<=2?10:0);
// A party already out brings the tracked partner's next clue (or its find) home: say so instead of sending the player there.
export const onTheWay=(s,t)=>{const trip=s.progress?.trip;if(!trip||!['running','returned'].includes(trip.status))return false;
  if(t.scoutFind?.kind==='intro')return !!trip.regional?.intro&&trip.regional.regionId===t.region?.id;
  return t.scoutFind?.trip?.cardId?trip.regional?.sure===t.scoutFind.trip.cardId:!!trip.clueHit&&trip.clueOrder?.[0]?.key===t.key;};
const wayNote=t=>`${t.region?.name??''}寻访中，${t.scoutFind?`这趟带回${t.scoutFind.kind==='card'?'它的见闻':t.scoutFind.kind==='intro'?'入门标本':'它的标本'}`:`回来就读到${TRIP_LAYER_NAMES[t.scoutLevel]??'下一条线索'}`}`;
const regionNote=t=>t.scoutFind?`去${t.region?.name??''}寻访，${t.scoutFind.kind==='intro'?'第一次去':'找它的'+(t.scoutFind.kind==='specimen'?'标本':'见闻')}`:`去${t.region?.name??''}寻访，读${TRIP_LAYER_NAMES[t.scoutLevel]??'下一条线索'}`;
// A regional trial as an advice row: its exact set, what it costs with the seasonings still to buy, its odds.
function trialRow(s,r,now){
  const missing=r.ingredients.filter(id=>!(s.ingredients[id]>0)),buy=missing.reduce((v,id)=>v+E.ingredient(id).buy_cp,0);
  let cook=0,minutes=0;try{const c=E.cookInfo({...s,selected:[...r.ingredients],egg:r.egg},r.toolId,now);cook=c.cost;minutes=c.minutes;}catch{}
  return {key:`regional:${r.recipeId}`,ingredients:[...r.ingredients],missing,cash:buy+cook,profit:-(buy+cook),minutes,affordable:s.cp>=buy+cook,tasks:[],birds:[],targets:[{key:r.key,chance:r.chance}],regional:r.recipeId};
}
const trialCard=(kind,value,reason,r,row)=>({...cookCard(kind,value,reason,r.egg,r.toolId,row,{type:'target',key:r.key,chance:r.chance,silhouette:r.silhouette}),regional:row.regional});

function cookCard(kind,value,reason,egg,toolId,row,outcome,demand=null){
  return {kind,value,reason,also:[],action:'cook',id:`${egg}|${toolId}|${row.key}`,egg,toolId,ingredients:[...row.ingredients],missing:[...row.missing],cash:row.cash,profit:row.profit,minutes:row.minutes,outcome,
    want:demand?wantOf(demand):null,fills:(row.tasks??[]).filter(t=>t.n>=.5).map(t=>wantOf(t.demand))};
}
const wantOf=d=>({key:wantKey(d),kind:d.kind,id:d.id,itemKey:d.key??null,text:alsoFor(d),reason:wantReason(d)});
const wantKey=d=>`${d.kind}|${d.id}|${d.what}|${[...d.allowed].join(',')}`;
// The short note a set gets for a want it fills besides its own reason.
const alsoFor=d=>d.kind==='menu'?'也能补今日菜单':d.kind==='story'?'也能补厨房往事':`也能补「${d.name}」`;
// A goal from 生意 ({kind:'menu'|'order'|'proposal'|'story', id, key?}) and the want it means.
const goalMatch=(goal,f)=>!!goal&&f.kind===goal.kind&&f.id===goal.id&&(!goal.key||f.itemKey===goal.key);
function guessCard(kind,value,reason,g,fee){
  return {kind,value,reason,also:[],action:'guess',id:`guess|${g.key}`,egg:g.egg,toolId:g.toolId,ingredients:g.first!=null?[g.first]:[],unknown:true,cash:fee,guess:g,outcome:{type:'guess',key:g.key,silhouette:g.silhouette}};
}
const ALSO={new:'也能出新伙伴',income:'也是最赚的',night:'也能放到明早'};
// 睡前这锅 (batch 5 playtest): from 21:00 to 5:00 a pan's two hours of 保鲜 do not last the night, so one card names the
// set that earns most among those still fresh at 8:00 the next morning (烤箱 8 小时, 炖锅 12 小时…). Local time, as the
// phone shows it. Minutes until that morning, or null in the day.
export function nightGap(now){
  const d=new Date(now),h=d.getHours();if(h>=5&&h<21)return null;
  const m=new Date(d);if(h>=21)m.setDate(m.getDate()+1);m.setHours(8,0,0,0);return (m-d)/60000;
}

// tracked: the tracked partner's 线索册 card, whether it got a card here, and if not why (note).
// menuId: today's menu on the 生意 page; goal: what 生意 sent the player here for (its card leads, see goalMatch).
export function nextBatchGoals(s,now=Date.now(),{menuId=null,goal=null}={}){
  const found=trackedFound(s),t=trackedRow(s,now),cards=[];let note='',trackCard=null;
  if(t){
    if(t.status==='ready'&&t.regional&&t.trial?.inPot)note='这锅正在试做它，收取后揭晓';
    else if(t.status==='ready'&&t.regional){
      const row=trialRow(s,t,now);
      if(row.affordable)trackCard=trialCard('track',Infinity,`追踪 ${t.code} · 做法齐了`,t,row);else note=`CP 不够，这一锅要 ${row.cash}`;
    }else if(t.status==='ready'){
      const row=findRow(s,t.egg,t.toolId,t.ingredients,now,menuId);
      if(row&&row.affordable)trackCard=cookCard('track',Infinity,`追踪 ${t.code} · 配方齐了`,t.egg,t.toolId,row,{type:'target',key:t.key,chance:t.chance,silhouette:t.silhouette});
      else note=row?`CP 不够，这一锅要 ${row.cash}`:'现在做不出';
    }else if(t.status==='guess'&&narrowed(t.guess)){
      const fee=baseFee(s,t.egg,t.guess.toolId,now);
      if(s.cp>=fee)trackCard=guessCard('track',Infinity,`追踪 ${t.code} · ${hint(t.guess)}`,t.guess,fee);else note=`CP 不够，开火要 ${fee} 起`;
    }else if(t.status==='scout')note=onTheWay(s,t)?wayNote(t):regionNote(t);
    else note=t.need[0]??(t.status==='guess'?'线索还没缩小范围，可以在线索册照谜语试':t.status==='clue'?'线索还不够，可以研读':'现在还试不了');
  }
  const trackKey=t?.key??null,add=c=>cards.push(c);
  let shortOfCp=false,richest=null,sleeper=null;const gap=nightGap(now);
  for(const egg of eggsOf(s)){
    const advice=allAdvice(s,egg,now,{menuId});
    for(const a of advice){
      for(const r of a.rows){
        if(r.affordable===false){if(r.targets.length||r.taskScore>=.05||(r.kind==='known'&&r.newKinds>=NEW_MIN))shortOfCp=true;continue;}
        // new partners: a 线索册 recipe first (the target and its chance), then known sets whose pool still holds unmet ones
        for(const x of r.targets){if(x.key===trackKey)continue;const [,id]=x.key.split(':').map(Number);
          add(cookCard('new',60+30*Math.min(1,x.chance),`能出新伙伴 ${speciesCode(x.key)}`,egg,a.toolId,r,{type:'target',key:x.key,chance:x.chance,silhouette:true}));}
        if(!r.targets.length&&r.kind==='known'&&r.newKinds>=NEW_MIN)add(cookCard('new',40+25*Math.min(1,r.newKinds),r.newKinds>=.95?'能出新伙伴':'有机会出新伙伴',egg,a.toolId,r,{type:'new',chance:r.newKinds}));
      }
      const best=rankAdvice(a,'income')[0];
      if(best&&(!richest||rateOf(best)>rateOf(richest.row)))richest={row:best,egg,toolId:a.toolId};
      // night: this cookware's best set that is done before morning and still fresh at 8:00
      if(gap)for(const r of rankAdvice(a,'income')){
        if(r.affordable===false)continue;let info;try{info=E.cookInfo({...s,egg,selected:[...r.ingredients]},a.toolId,now);}catch{break;}
        if(info.minutes>gap||info.minutes/12+info.freshMinutes<gap)break;
        if(!sleeper||rateOf(r)>rateOf(sleeper.row))sleeper={row:r,egg,toolId:a.toolId};break;
      }
    }
  }
  // regional partners whose complete method is held: their trial, with the region's odds
  for(const egg of eggsOf(s))for(const x of regionalRows(egg)){
    if(x.key===trackKey||speciesDiscovered(s,x.egg,x.id)||!regionalHeld(s,x.key))continue;
    const r=clueRow(s,x.key,now);if(!r?.ready||r.trial?.inPot)continue;
    const row=trialRow(s,r,now);if(!row.affordable){shortOfCp=true;continue;}
    add(trialCard('new',60+30*Math.min(1,r.chance),`能出新伙伴 ${r.code}`,r,row));
  }
  // a cookware nobody of this egg has come from yet: try it empty
  for(const egg of eggsOf(s))for(const [toolId,level] of s.toolLevels.entries()){
    if(!Number.isInteger(level)||level<0||toolId===8&&egg===1)continue;
    if(RECIPE_CATALOG.some(r=>r.egg===egg&&r.toolId===toolId&&speciesDiscovered(s,r.egg,r.id)))continue;
    // a recipe of it is already held (线索册): that card says more
    if(cards.some(c=>c.egg===egg&&c.toolId===toolId&&c.outcome?.type==='target')||trackCard?.egg===egg&&trackCard?.toolId===toolId)continue;
    const view=s.egg===egg?s:{...s,egg};let fee=0,minutes=0;
    try{const c=E.cookInfo({...view,selected:[]},toolId,now);fee=c.cost;minutes=c.minutes;}catch{continue;}
    let out;try{out=expectedOutcome({...view,selected:[]},toolId,[],now);}catch{continue;}
    if(![...out.kinds.entries()].some(([id,p])=>p>0&&!speciesDiscovered(s,egg,id)))continue;
    if(s.cp<fee){shortOfCp=true;continue;}
    add(cookCard('tool',78,`${E.label(E.tool(toolId))}还没做过${egg?'鸭蛋':'鸡蛋'} · 先空着做一锅`,egg,toolId,{key:'',ingredients:[],missing:[],cash:fee,profit:0,minutes,tasks:[]},{type:'guess',key:null,silhouette:false}));
  }
  // money: the one set that earns most per hour of kitchen time, ranked last
  if(richest){const r=richest.row;add(cookCard('income',10,`现在最赚 · 每小时 +${Math.round(rateOf(r))}`,richest.egg,richest.toolId,r,{type:'cp',profit:r.profit,minutes:r.minutes}));}

  // orders, proposals, the 厨房往事 chapter, project stages and today's menu: the set that brings the most of what is
  // still missing
  const demands=new Map();
  for(const egg of eggsOf(s))for(const a of allAdvice(s,egg,now,{menuId}))for(const r of a.rows){
    if(r.affordable===false)continue;
    for(const task of r.tasks){const d=task.demand,k=`${d.kind}|${d.id}|${d.what}|${[...d.allowed].join(',')}`,cur=demands.get(k);
      if(!cur||task.n>cur.n+1e-9||(Math.abs(task.n-cur.n)<1e-9&&r.profit>cur.row.profit))demands.set(k,{d,n:task.n,row:r,egg,toolId:a.toolId});}
  }
  for(const {d,n,row,egg,toolId} of demands.values()){
    if(n<.5)continue;
    const coverage=Math.min(1,n/d.short),done=Math.max(0,(d.quantity-d.short)/d.quantity);
    const top=row.birds.filter(b=>d.allowed.has(b.key)).sort((a,b)=>b.n-a.n)[0];
    const value=50+20*coverage+15*done+(coverage>=.95?10:0)-(d.kind==='proposal'?8:0)-(d.kind==='project'?4:0)-(d.kind==='story'?6:0);
    add(cookCard(d.kind==='menu'?'menu':'order',value,wantReason(d),egg,toolId,row,{type:'birds',key:top.key,n:Math.min(Math.round(top.n),d.short)||1},d));
  }
  // investigations that can be tried now (the tracked one already has its card)
  for(const egg of eggsOf(s))for(const g of directions(s,egg,now)){
    if(g.key===trackKey||!narrowed(g))continue;const fee=baseFee(s,egg,g.toolId,now);if(s.cp<fee)continue;
    add(guessCard('guess',guessValue(g),`调查 ${g.code} · ${hint(g)}`,g,fee));
  }
  // one card per set: the strongest reason leads, the others become a short 「也」 note
  const byId=new Map();
  for(const c of cards.sort((a,b)=>b.value-a.value)){
    const cur=byId.get(c.id);
    if(!cur){byId.set(c.id,c);continue;}
    if(c.kind!==cur.kind&&ALSO[c.kind]&&!cur.also.includes(ALSO[c.kind]))cur.also.push(ALSO[c.kind]);
  }
  if(trackCard){const same=byId.get(trackCard.id);if(same){for(const k of [same.kind,...same.also.map(a=>Object.keys(ALSO).find(x=>ALSO[x]===a))])if(ALSO[k]&&!trackCard.also.includes(ALSO[k]))trackCard.also.push(ALSO[k]);byId.delete(trackCard.id);}}
  // the goal 生意 sent the player for: its best card leads (even before the tracked one, which follows)
  let goalCard=null;
  if(goal){
    const own=c=>!!c.want&&goalMatch(goal,c.want);
    goalCard=[...byId.values()].filter(c=>own(c)||c.fills?.some(f=>goalMatch(goal,f))).sort((a,b)=>own(b)-own(a)||b.value-a.value)[0]??null;
    if(goalCard)byId.delete(goalCard.id);
    // a set that serves the goal among other wants speaks for the goal first; its own reason becomes a 「也」 note
    if(goalCard&&!own(goalCard)){const f=goalCard.fills.find(x=>goalMatch(goal,x)),was=goalCard;goalCard={...was,reason:f.reason,want:f,also:[...(was.want?[]:[...was.also]),...(was.kind==='new'?[ALSO.new]:[])]};}
  }
  const picked=[...(goalCard?[{...goalCard,goal:true}]:[]),...(trackCard?[trackCard]:[])],count={};
  // one card per order: an order short in two groups shows its best one (the other follows once that is cooked, or
  // rides along as 「也能补」), so a second order or another reason gets the other place (batch 5 playtest)
  const source=c=>c.want&&c.kind==='order'?`${c.want.kind}|${c.want.id}`:null,sources=new Set(picked.map(source).filter(Boolean));
  for(const c of [...byId.values()].sort((a,b)=>b.value-a.value)){
    if(picked.length>=GOAL_CARDS)break;if((count[c.kind]??0)>=PER_KIND[c.kind])continue;
    const from=source(c);if(from&&sources.has(from))continue;if(from)sources.add(from);
    count[c.kind]=(count[c.kind]??0)+1;picked.push(c);
  }
  // night: a picked set that lasts until 8:00 says so; when none does, the last place goes to 睡前这锅
  if(gap){
    const lasts=c=>{if(c.action!=='cook')return false;try{const i=E.cookInfo({...s,egg:c.egg,selected:[...c.ingredients]},c.toolId,now);return i.minutes<=gap&&i.minutes/12+i.freshMinutes>=gap;}catch{return false;}};
    const fit=picked.filter(lasts);for(const c of fit)if(!c.also.includes(ALSO.night))c.also.unshift(ALSO.night);
    if(!fit.length&&sleeper){const r=sleeper.row,card=cookCard('night',75,'睡前这锅 · 放到明早也不会焦',sleeper.egg,sleeper.toolId,r,{type:'cp',profit:r.profit,minutes:r.minutes});
      const same=picked.findIndex(c=>c.id===card.id);if(same>=0)picked.splice(same,1);
      if(picked.length>=GOAL_CARDS){const at=picked.findLastIndex(c=>!c.goal&&c.kind!=='track');if(at>=0)picked.splice(at,1);}
      // it leads right after the goal and the tracked partner: at night it is the card that matters
      if(picked.length<GOAL_CARDS)picked.splice(picked.filter(c=>c.goal||c.kind==='track').length,0,card);}
  }
  // 「也能补…」: the other wants a picked set fills (its own reason already names one of them)
  for(const c of picked)for(const f of c.fills??[])if(f.key!==c.want?.key&&!c.also.includes(f.text)&&!(f.kind==='menu'&&c.kind==='menu'))c.also.push(f.text);
  return {cards:picked,tracked:t?{row:t,carded:!!trackCard,note,scout:t.status==='scout'&&!onTheWay(s,t)?t.region:null}:null,goal:goal?{...goal,carded:!!goalCard}:null,found,shortOfCp:!picked.length&&shortOfCp};
}
// One line for a want: what it is and how many are still short.
function wantReason(d){
  const what=d.display?'种':d.what?`只${d.what}`:'只';
  if(d.kind==='menu')return `今日菜单还差 ${d.short} ${what}`;
  if(d.kind==='story')return `厨房往事还差 ${d.short} ${what}`;
  if(d.display)return `${d.label}还差 ${d.short} ${what}`;
  return d.kind==='proposal'?`新订单「${d.name}」要 ${d.short} ${what}`:`${d.label}差 ${d.short} ${what}`;
}
