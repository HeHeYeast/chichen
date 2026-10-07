// 下一锅: a kit page with four bookmarks — 推荐 (default), 新伙伴, 订单, 多赚 — and 自己选 from the foot.
// 「推荐」 (next-batch-goals.js) is a short list of cards, each a batch worth making now with the one reason why; the tracked
// partner (线索册「追踪」) sits on top. Each card starts its batch (or, for an investigation, opens 自己选 to put it together).
// The other bookmarks show one recommendation card (cookware, seasoning slots, why, likely partners) with other picks
// as small tiles below; 自己选 gets the whole sheet (cookware coins, slots, seasoning tiles).
// The cost sits above the foot; 开火 buys any missing seasoning; 选好了 only saves the choice.
// 新伙伴 only offers sets whose recipe the player holds; a row that is the recipe of a 线索册「已解锁」 partner shows that
// partner's silhouette and its chance per batch. 线索册「去做」 opens this page with that exact set (open({recipe})).
// 「自己推测」 lists 推测方向 (guessDirections): what the clues say about a partner that could be cooked right now; picking
// one opens 自己选 with the cookware and any known first seasoning filled in and the possible second seasonings laid out,
// and the player puts the set together. Every suggestion on this page can be started now (conditions met, affordable).
import * as E from './engine.js';
import {speciesCode} from './knowledge.js';
import {rankAdvice,recipeForSpecies,undiscoveredReach,holidayLimited} from './seasoning-advisor.js';
import {nextBatchGoals,allAdvice,directions,narrowed,rateOf} from './next-batch-goals.js';
import {CLUE_STEPS} from './clue-book.js';
import {regionalTrial} from './regional-clues.js';
import {prepareRegionalRecipe} from './regional-methods.js';
import {availableIngredientIds,ingredientUnlockInfo} from './ingredient-unlocks.js';
import {helpCardsMarkup,firstVisitGuide} from './game-frame.js';
import {kitTabs,kitSheet,kitCell,kitCoin,kitButton,kitButton2,kitChip,kitChipHtml,kitLabel,kitIcon,kitEgg} from './ui-kit.js';

const esc=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const LENSES=[['goals','推荐'],['new','新伙伴'],['tasks','订单'],['income','多赚']];
const UI_ART='/web/art/golden-ui/',BIZ_ART='/web/art/golden-business/';
const GOAL_ICON={track:`${UI_ART}ic-star.png`,order:`${BIZ_ART}orders.png`,menu:`${BIZ_ART}menu-easel.png`,new:`${UI_ART}ic-egg.png`,tool:`${UI_ART}ic-egg.png`,guess:`${UI_ART}ic-book.png`,income:`${BIZ_ART}coin.png`,night:`${UI_ART}ic-hourglass.png`};
// 「多赚」 compares money per hour of kitchen time: net profit of one batch divided by how long the cookware takes.
// 「新伙伴」 counts kinds: a sure ≥1 reads as a number of kinds, anything less as a chance.
const kindsText=x=>x>=.95?`≈${Math.max(1,Math.round(x))} 种新的`:`${Math.max(1,Math.round(x*100))}% 机会出新的`;
const pct=x=>`${Math.max(1,Math.round(x*100))}%`;
const spanText=min=>min<60?`${Math.round(min)} 分钟`:`${Math.floor(min/60)} 小时${Math.round(min%60)?` ${Math.round(min%60)} 分`:''}`;
const goalsCache=new WeakMap();
// 「自己推测」 tiles: only directions the clues have narrowed down (a partner known only by its cookware waits for 寻访).
const narrowDirections=(s,egg,now)=>directions(s,egg,now).filter(narrowed);
// One 「推荐」 list per save snapshot, today's menu and goal (生意 can send the player here for a menu slot or an order).
function goals(s,now,menuId=null,goal=null){const sig=JSON.stringify([menuId,goal]),hit=goalsCache.get(s);if(hit?.sig===sig)return hit.value;const value=nextBatchGoals(s,now,{menuId,goal});goalsCache.set(s,{sig,value});return value;}
function ranked(s,egg,lens,now,menuId=null){
  const score=r=>lens==='tasks'?r.taskScore*1000+r.profit:lens==='new'?(r.targets.length?1e6:0)+r.newKinds*1000+r.profit:rateOf(r);
  // Sets that cannot be started now (unaffordable, conditions unmet) never get here (rankAdvice, knownSets).
  // One recommendation per cookware (its best set), cookwares ordered by how good that set is — except that 新伙伴 lists
  // every set made for a 线索册 partner, so each unlocked recipe can be picked here, not only the best one per cookware.
  const per=a=>{const rows=rankAdvice(a,lens);if(lens!=='new')return rows.slice(0,1);const aimed=rows.filter(r=>r.targets.length);return aimed.length?aimed:rows.slice(0,1);};
  return allAdvice(s,egg,now,{menuId}).flatMap(a=>per(a).map(r=>({...r,toolId:a.toolId}))).sort((a,b)=>(b.affordable-a.affordable)||score(b)-score(a));
}

// getMenuId: today's menu on the 生意 page (its core partners still short at home are wants here too); openBusiness: back to 生意.
export function createNextBatchUI({getState,getNow,showPanel,panels,closePanel,confirmBox,alertBox,act,sound,characterPortrait,ingredientPortrait,toolPortrait,startCook,openRestock,openClueBook,openJourney,getMenuId=()=>null,openBusiness=null}){
  // recipe: a 线索册「已解锁」 set to cook ({key,egg,toolId,ingredients}); preset: a known partner wanted by a menu or order;
  // guess: a 推测方向 being put together by hand in 自己选.
  // goal: what 生意 sent the player for ({kind:'menu'|'order'|'proposal'|'story', id, key?, name}); its card leads 「推荐」.
  let lens='goals',pick=null,preset=null,recipe=null,guess=null,manual=false,egg=null,toolId=null,draft=[],alt=0,help=false,goal=null;
  const menuNow=()=>{try{return getMenuId?.()??null;}catch{return null;}};
  function startGuess(g){manual=true;guess=g;egg=g.egg;toolId=g.toolId;draft=g.first!=null?[g.first]:[];preset=null;recipe=null;alt=0;}
  const find=sel=>panels.querySelector(sel);
  const max=()=>Math.min(3,getState().kitchenLevel+1);
  function fromRow(r){return {toolId:r.toolId,egg,ingredients:[...r.ingredients],birds:r.birds,profit:r.profit,unknown:r.unknown,newKinds:r.newKinds??0,targets:r.targets??[],kind:r.kind,taskScore:r.taskScore,minutes:r.minutes};}
  const sameSet=(a,b)=>[...a].sort((x,y)=>x-y).join('+')===[...b].sort((x,y)=>x-y).join('+');
  function choose(){
    const s=getState(),now=getNow();
    // a regional partner's trial (线索册 去制作): its exact set, made in the region's trial mode (fireSet)
    if(recipe?.regional){egg=recipe.egg;pick={toolId:recipe.toolId,egg,ingredients:[...recipe.ingredients],birds:[],targets:[{key:recipe.key,chance:recipe.chance??.25}],profit:0,regional:recipe.regional};return;}
    if(recipe){egg=recipe.egg;const row=allAdvice(s,egg,now).find(a=>a.toolId===recipe.toolId)?.rows.find(x=>sameSet(x.ingredients,recipe.ingredients));
      pick=row?fromRow({...row,toolId:recipe.toolId}):{toolId:recipe.toolId,egg,ingredients:[...recipe.ingredients],birds:[],targets:[],profit:0};return;}
    if(preset){const r=recipeForSpecies(s,preset.key,now);if(r){egg=r.egg;const rows=allAdvice(s,egg,now).find(a=>a.toolId===r.toolId)?.rows??[];const row=rows.find(x=>x.ingredients.join('+')===r.ingredients.join('+'));pick=row?fromRow({...row,toolId:r.toolId}):{toolId:r.toolId,egg,ingredients:r.ingredients,birds:[],profit:0};return;}preset=null;}
    if(manual){pick={toolId,egg,ingredients:[...draft],birds:null};return;}
    if(lens==='goals'){pick=null;return;}
    const rows=ranked(s,egg,lens,now,menuNow());pick=rows[alt]?fromRow(rows[alt]):rows[0]?fromRow(rows[0]):null;
  }
  // Likely partners for a hand-picked set come from the same advice tables when the set is known.
  function birdsFor(p){
    if(p.birds)return p.birds;const key=[...p.ingredients].sort((a,b)=>a-b).join('+');
    return allAdvice(getState(),p.egg,getNow()).find(a=>a.toolId===p.toolId)?.rows.find(r=>r.key===key)?.birds??[];
  }
  function reason(p){
    if(recipe?.regional){const t=regionalTrial(getState(),recipe.key);return t?.sure?`为 ${speciesCode(recipe.key)} 准备 · 这锅一定出`:`为 ${speciesCode(recipe.key)} 准备 · 每锅 ${pct(t?.chance??.25)}`;}
    if(recipe){const t=p.targets.find(x=>x.key===recipe.key);return t?`为 ${speciesCode(recipe.key)} 准备 · 每锅约 ${pct(t.chance)} 出现`:`为 ${speciesCode(recipe.key)} 准备`;}
    if(preset)return `一锅约 ${Math.round(birdsFor(p).find(b=>b.key===preset.key)?.n??0)} 只${esc(E.label(E.char(...preset.key.split(':').map(Number))))}`;
    if(manual||!p.birds)return '';
    return lens==='tasks'?`能交订单 · 约 ${Math.round(p.taskScore)} 只`:lens==='new'?(p.targets.length?`线索册的 ${p.targets.map(t=>speciesCode(t.key)).join('、')} · 每锅约 ${pct(Math.min(1,p.targets.reduce((v,t)=>v+t.chance,0)))}`:kindsText(p.newKinds)):`每小时约 ${Math.round(rateOf(p))} · 每锅 ${p.profit>=0?'+':''}${p.profit}，${spanText(p.minutes)}`;
  }
  // a regional trial keeps the region's promise: three batches without it, the fourth brings it
  function trialNote(p){
    if(!p.regional)return '';const t=regionalTrial(getState(),p.targets?.[0]?.key??recipe?.key);if(!t||t.sure)return '';
    return `<p class="nb-trial-note">地区试做：${t.failed?`已经 ${t.failed} 锅没出，`:''}最多 ${t.left} 锅一定出</p>`;
  }
  function costs(s,p){
    const missing=p.ingredients.filter(id=>!(s.ingredients[id]>0)),buy=missing.reduce((v,id)=>v+E.ingredient(id).buy_cp,0);
    let cook=0;try{cook=E.cookInfo({...s,selected:p.ingredients,egg:p.egg},p.toolId,getNow()).cost;}catch{cook=0;}
    return {missing,buy,cook,total:buy+cook};
  }
  const nbHelp=()=>`<div class="kp-scrim" data-nb-help-close></div><section class="kp-drawer gd nb-help" role="dialog" aria-modal="true" aria-label="这页怎么玩"><div class="kp-drawer-title" data-art="plank"><span data-safe><b>这页怎么玩</b></span></div><div class="kp-drawer-body">${helpCardsMarkup('nextBatch')}<div class="gd-actions" data-row>${kitButton('知道了','data-nb-help-close')}</div></div></section>`;
  function render(){
    const s=getState(),COIN=kitIcon.coin;
    if(egg===null)egg=s.egg;if(toolId===null||!(s.toolLevels[toolId]>=0))toolId=s.batch?.tool??s.toolLevels.findIndex(l=>l>=0);
    choose();
    const p=pick,remaining=s.batch?.eggs.filter(e=>!e.collected).length??0;
    const metric=r=>lens==='tasks'?`≈${Math.round(r.taskScore)}只`:lens==='new'?(r.newKinds>=.95?`≈${Math.round(r.newKinds)}种新`:pct(r.newKinds)):`${Math.round(rateOf(r))}/时`;
    const eggCoins=s.duck?`<div class="kp-coins nb-eggs" role="group" aria-label="蛋种">${kitCoin(kitEgg(false),'data-nb-egg="0"',egg===0,'鸡蛋')}${kitCoin(kitEgg(true),'data-nb-egg="1"',egg===1,'鸭蛋')}</div>`:'';
    // Likely partners: stickers with how many of each, unknown ones as 「?」.
    const out=list=>`<div class="nb-out" aria-label="可能出">${list.length?list.map(b=>{const [e,id]=b.key.split(':').map(Number);if(b.target)return `<span class="nb-bird is-target"><span class="nb-bird-art"><span class="unknown-silhouette" aria-hidden="true">${characterPortrait(e,id)}</span></span><b>${pct(b.chance)}</b><small>${speciesCode(b.key)}</small></span>`;return `<span class="nb-bird${b.known?'':' is-unknown'}"><span class="nb-bird-art">${b.known?characterPortrait(e,id):'<span class="bk-q" aria-hidden="true">?</span>'}</span><b>${b.n==null?'？':b.n>=.95?`≈${Math.round(b.n)}`:'偶尔'}</b><small>${b.known?esc(E.label(E.char(e,id))):b.n==null?'有机会':'没见过'}</small></span>`;}).join(''):'<span class="nb-bird is-unknown"><span class="nb-bird-art"><span class="bk-q" aria-hidden="true">?</span></span><small>开火后才知道</small></span>'}</div>`;
    const slots=list=>`<div class="nb-slots" aria-label="调味料">${Array.from({length:max()},(_,i)=>{const id=list[i];return id==null?'<span class="nb-slot is-empty"><span class="nb-slot-art"></span><small>不加</small></span>':`<span class="nb-slot"><span class="nb-slot-art">${ingredientPortrait(id)}${s.ingredients[id]>0?'':'<b class="nb-buy">买</b>'}</span><small>${esc(E.label(E.ingredient(id)))}</small></span>`;}).join('')}</div>`;
    const top=(id,extra='')=>`<div class="nb-hero-top"><span class="nb-tool">${toolPortrait(id,s.toolLevels[id])}</span><div class="nb-tool-name"><strong>${esc(E.label(E.tool(id)))}</strong>${kitChip('',`Lv.${s.toolLevels[id]+1}`,'mini soft')}</div>${extra}</div>`;
    // The partners a set is for (线索册) lead, then the most likely others; a target is never shown twice.
    const outFor=p=>{const t=p.targets??[],keys=new Set(t.map(x=>x.key));return [...t.map(x=>({...x,target:true})),...birdsFor(p).filter(b=>!keys.has(b.key))].slice(0,4);};
    // 「自己推测」: what the clues say, as cookware + known seasoning + 「?」; tapping one puts it into 自己选.
    const qSlot='<span class="nb-alt-ing nb-q" aria-hidden="true">?</span>';
    const guessTiles=list=>`<div class="nb-alts nb-guesses" role="list" aria-label="自己推测">${list.map((g,i)=>`<button type="button" role="listitem" class="nb-alt nb-guess-tile" data-nb-guess="${i}" aria-label="推测 ${esc(g.code)}：${esc(E.label(E.tool(g.toolId)))}${g.first!=null?' + '+esc(E.label(E.ingredient(g.first))):''}${g.group?' + '+esc(g.group)+'类':''}"><span class="nb-alt-tool">${toolPortrait(g.toolId,s.toolLevels[g.toolId])}</span><span class="nb-alt-ings">${g.first!=null?`<span class="nb-alt-ing">${ingredientPortrait(g.first)}</span>`:qSlot}${qSlot}</span><b>${esc(g.code)}</b></button>`).join('')}</div>`;
    const guesses=lens==='new'&&!manual&&!preset&&!recipe?narrowDirections(s,egg,getNow()):[];
    const guessBlock=guesses.length?`${kitLabel('自己推测')}${guessTiles(guesses.slice(0,4))}${guesses.length>4&&openClueBook?`<div class="gd-row">${kitButton2(`全部 ${guesses.length} 个`,'data-nb-clues-guess')}</div>`:''}`:'';
    const tabs=preset||recipe?'':kitTabs(LENSES.map(([id,label])=>({label,attrs:`data-nb-lens="${id}"`,on:!manual&&lens===id})),'推荐方向');
    // Holiday-only partners that can be cooked right now: tell the player, name nothing.
    const holidays=(preset||recipe||manual)?[]:holidayLimited(s,egg,getNow());
    const holidayNote=holidays.length?`<div class="nb-holiday" role="note">${holidays.map(h=>`<span class="nb-holiday-item"><b>${esc(h.title)} · 限定鸡宝开放中</b><small>${esc(h.range)}</small><small>还有 ${h.count} 种没见过，开火就有机会</small></span>`).join('')}</div>`:'';
    const context=holidayNote+(preset?`<div class="kp-bar nb-context" data-row>${kitChip('',`为${preset.menu??'订单'}做${E.label(E.char(...preset.key.split(':').map(Number)))}`,'')}${kitButton2('看推荐','data-nb-unpreset')}</div>`:recipe?`<div class="kp-bar nb-context" data-row>${kitChip('',`线索册 · ${speciesCode(recipe.key)}`,'')}${kitButton2('看推荐','data-nb-unpreset')}</div>`:'');
    let body;
    const home=lens==='goals'&&!manual&&!preset&&!recipe;
    if(home)body=holidayNote+goalsBody(s);
    else if(manual){
      const owned=s.toolLevels.map((l,id)=>({id,l})).filter(t=>t.l>=0);
      const cell=id=>{const n=s.ingredients[id]??0;return kitCell({pic:ingredientPortrait(id),name:E.label(E.ingredient(id)),count:n>0?`×${n}`:`买${E.ingredient(id).buy_cp}`,attrs:`data-id="${id}" aria-pressed="${draft.includes(id)}"`,on:draft.includes(id)});};
      // Guessing: the second seasoning's group when the clues name it, otherwise everything that can be had now
      // (in stock first); seasonings not in stock are bought when the batch starts.
      const usable=id=>(s.ingredients[id]??0)>0||(()=>{const u=ingredientUnlockInfo(s,id);return u.available&&!u.special;})();
      const pool=guess?(guess.candidates?[...guess.candidates]:availableIngredientIds(s).filter(usable).sort((a,b)=>((s.ingredients[b]??0)>0)-((s.ingredients[a]??0)>0)||a-b)):Object.entries(s.ingredients).filter(([,n])=>n>0).map(([id])=>+id);
      if(guess)for(const id of draft)if(!pool.includes(id))pool.unshift(id);
      const tiles=pool.map(cell).join('');
      const guessBar=guess?`<div class="nb-guess" role="note"><span class="bk-sticker-art nb-guess-art is-unknown">${guess.silhouette?`<span class="unknown-silhouette" aria-hidden="true">${characterPortrait(guess.egg,guess.id)}</span>`:'<span class="bk-q" aria-hidden="true">?</span>'}</span><span class="nb-guess-copy"><b>推测 ${esc(guess.code)}</b><small>${esc(guess.riddle)}</small></span><button type="button" class="gd-btn2 mini" data-nb-unguess>不推测</button></div>`:'';
      const pickLabel=guess?kitLabel(guess.candidates?`第二味是「${guess.group}」类，挑一种`:guess.first!=null?'第二味要靠谜语猜，自己挑':'看谜语，自己挑调味料'):'';
      body=`${guessBar}<div class="kp-coins nb-tools" role="group" aria-label="厨具">${owned.map(t=>kitCoin(toolPortrait(t.id,t.l),`data-nb-tool="${t.id}"`,toolId===t.id,E.label(E.tool(t.id)))).join('')}</div>
        <section class="nb-pick nb-hero nb-mix" aria-label="自己选的一锅"><div class="nb-mix-row"><span class="nb-tool">${toolPortrait(toolId,s.toolLevels[toolId])}</span><span class="nb-plus" aria-hidden="true">+</span>${slots(draft)}${eggCoins}</div>${out(birdsFor(p).slice(0,4))}</section>
        <div class="kp-bar" data-row>${kitChip('',`已选 ${draft.length}/${max()}`,'mini counter')}${kitButton2('去商店','data-restock')}</div>
        ${pickLabel}<div class="kp-grid ingredients-grid nb-grid">${tiles||'<p class="kp-empty">调味料用完了</p>'}</div>`;
    }else if(!p){
      // 新伙伴 with nothing known to recommend: say how many are still out there per cookware (a count only) and how to find them.
      const reach=lens==='new'?s.toolLevels.map((l,id)=>({id,l,n:l>=0?undiscoveredReach({...s,egg},id,getNow()):0})).filter(t=>t.n>0):[];
      const more=reach.length?`<div class="nb-reach" role="list" aria-label="还没发现的伙伴">${reach.map(t=>`<span class="nb-reach-item" role="listitem"><span class="nb-tool">${toolPortrait(t.id,t.l)}</span><b>${E.label(E.tool(t.id))}</b><small>还有 ${t.n} 种没发现</small></span>`).join('')}</div><p class="nb-reach-tip">线索册里推断出或研读到配方，这里就会推荐。</p>`:'';
      // Something would be advised if the player had the CP for it: say so instead of "nothing".
      const short=allAdvice(s,egg,getNow()).some(a=>rankAdvice({rows:a.rows.map(r=>({...r,affordable:true}))},lens).length);
      const title=short?'CP 不够开推荐的那一锅':lens==='tasks'?'现在没有订单在等伙伴':lens==='new'?'还没有能出新伙伴的配方':'这个方向暂时没有推荐';
      body=guessBlock&&!short?`${context}<div class="kp-empty nb-empty nb-empty-short"><span>${title}</span><span class="nb-reach-tip">照线索推测一锅试试</span></div>${guessBlock}<div class="gd-row">${openClueBook?kitButton2('去线索册','data-nb-clues'):''}${kitButton2('自己选','data-nb-lens="manual"')}</div>`
        :`${context}<div class="kp-empty nb-empty"><span class="bk-q">?</span><span>${title}</span>${short?'':more}<div class="gd-row">${lens==='new'&&openClueBook?kitButton2('去线索册','data-nb-clues'):''}${kitButton2('自己选','data-nb-lens="manual"')}${lens==='new'?'':kitButton2('看「多赚」','data-nb-lens="income"')}</div></div>`;}
    else{
      // from the 线索册 the other 新伙伴 picks stay one tap away
      const alts=!preset?ranked(s,egg,lens,getNow(),menuNow()):[],r=reason(p);
      const altGrid=alts.length>1?`${kitLabel('换一个')}<div class="nb-alts" role="list" aria-label="换一个推荐">${alts.map((a,i)=>`<button type="button" role="listitem" class="nb-alt" data-nb-alt="${i}" aria-pressed="${p.toolId===a.toolId&&p.ingredients.join('+')===a.ingredients.join('+')}"><span class="nb-alt-tool">${toolPortrait(a.toolId,s.toolLevels[a.toolId])}</span><span class="nb-alt-ings">${a.ingredients.map(id=>`<span class="nb-alt-ing">${ingredientPortrait(id)}</span>`).join('')||'<small>不加</small>'}</span><b>${metric(a)}</b></button>`).join('')}</div>`:'';
      body=`${context}<section class="nb-pick nb-hero" aria-label="这一锅">${top(p.toolId,eggCoins)}${slots(p.ingredients)}${r?`<div class="gd-row">${kitChipHtml(`<span class="nb-star" aria-hidden="true">★</span>${r}`,'mini nb-reason')}</div>`:''}${out(outFor(p))}</section>${trialNote(p)}${altGrid}${guessBlock}`;
    }
    const c=p?costs(s,p):null,afford=c&&s.cp>=c.total;
    const above=remaining?kitChip('',`还有 ${remaining} 只没收，开火会放弃`,'mini hot'):'';
    const fire=`<button type="button" class="gd-btn nb-fire" data-nb-fire${p?' data-next':' disabled'} aria-label="开火${c?`，共 ${c.total} CP`:''}"><span data-safe><b>开火</b>${c?`<small class="${afford?'':'short'}">${COIN}${c.total}</small>`:''}</span></button>`;
    const foot=home?`${kitButton2('自己选','data-nb-lens="manual"')}${openClueBook?kitButton2('线索册','data-nb-book'):''}`:`${manual?draft.every(id=>s.ingredients[id]>0)?kitButton2('选好了','data-ok'):'':kitButton2('自己选','data-nb-lens="manual"')}${fire}`;
    showPanel('下一锅',`${tabs}${kitSheet(body,foot,'nb-sheet'+(manual?' nb-is-manual':'')+(home?' nb-is-home':''),above,{cls:'nb-scroll',attrs:manual||home?'data-list':''})}${help?nbHelp():''}`,'screen-panel ingredient-screen next-batch-screen',{skin:'kitchen',icon:toolPortrait(1,0),help:'data-nb-help',short:'下一锅'});
    bind();
    if(home)firstVisitGuide('nextBatchGoals',[{selector:'#panels .nb-goal',text:'每张卡写着为什么推荐这一锅，点按钮就开火'},{selector:'#panels .nb-track',text:'在线索册追踪一只伙伴，它会排在最前面'}]);
    else firstVisitGuide('nextBatch',[{selector:'#panels .nb-pick',text:'这是推荐的一组，理由写在这里'},{selector:'#panels .nb-fire',text:'直接开火就行；也可以点下面的其他推荐'}]);
  }
  // 「推荐」: the tracked partner on a bar, then the cards. Only pictures, a number and one reason per card.
  function goalsBody(s){
    const g=goals(s,getNow(),menuNow(),goal),t=g.tracked;
    // the goal from 生意 sits above the tracked partner: what it is for, and whether a card here can help now
    const goalBar=goal?`<div class="nb-track nb-goalbar"><img class="nb-track-ic" src="${goal.kind==='menu'?`${BIZ_ART}menu-easel.png`:`${BIZ_ART}orders.png`}" alt=""><span class="nb-track-copy"><b>${esc(goal.kind==='menu'?`今日菜单「${goal.name}」`:goal.kind==='story'?'厨房往事':`订单「${goal.name}」`)}</b><small>${g.goal?.carded?'第一张就是为它做的':'现在开不出它要的伙伴'}</small></span>${openBusiness?'<button type="button" class="gd-btn2 mini" data-nb-business>回生意</button>':''}</div>`:'';
    const sil=(e,id)=>`<span class="unknown-silhouette" aria-hidden="true">${characterPortrait(e,id)}</span>`;
    const pips=n=>`<span class="cb-pips" role="img" aria-label="调查 ${n}/${CLUE_STEPS}">${Array.from({length:CLUE_STEPS},(_,i)=>`<i class="${i<n?'on':''}"></i>`).join('')}</span>`;
    const track=t?`<div class="nb-track"><span class="nb-track-art">${t.row.silhouette?sil(t.row.egg,t.row.id):'<span class="bk-q" aria-hidden="true">?</span>'}</span><span class="nb-track-copy"><b>追踪 ${esc(t.row.code)}</b>${t.carded?pips(t.row.progress):`<small>${esc(String(t.note).split(' · ')[0])}</small>`}</span>${t.scout&&openJourney?`<button type="button" class="gd-btn2 mini" data-nb-journey="${t.scout.id}">去${esc(t.scout.name)}</button>`:''}</div>`
      :g.found?`<div class="nb-track is-found"><img class="nb-track-ic" src="${UI_ART}ic-check.png" alt=""><span class="nb-track-copy"><b>${esc(speciesCode(g.found))} 已经认识了</b><small>去线索册追踪下一只</small></span></div>`
      :`<div class="nb-track is-empty"><img class="nb-track-ic" src="${UI_ART}ic-star.png" alt=""><span class="nb-track-copy"><b>还没追踪伙伴</b><small>在线索册点星，它会排在这里</small></span></div>`;
    if(!g.cards.length)return `${goalBar}${track}<div class="kp-empty nb-empty"><span class="bk-q">?</span><span>${g.shortOfCp?'CP 不够开推荐的那一锅':'现在没有要推荐的锅'}</span><div class="gd-row">${kitButton2('看「多赚」','data-nb-lens="income"')}</div></div>`;
    const slot=(id,q=false)=>q?'<span class="nb-goal-ing is-q" aria-hidden="true">?</span>':`<span class="nb-goal-ing" title="${esc(E.label(E.ingredient(id)))}">${ingredientPortrait(id)}${s.ingredients[id]>0?'':'<b class="nb-buy">买</b>'}</span>`;
    const set=c=>`<span class="nb-goal-set"><span class="nb-goal-tool">${toolPortrait(c.toolId,s.toolLevels[c.toolId])}</span><i aria-hidden="true">+</i>${c.ingredients.map(id=>slot(id)).join('')}${c.unknown?slot(null,true):c.ingredients.length?'':'<small>不放调味</small>'}</span>`;
    const outcome=o=>{
      if(o.type==='target')return `<span class="nb-goal-out">${o.silhouette?`<span class="nb-goal-bird">${sil(...o.key.split(':').map(Number))}</span>`:''}<b class="hot">${pct(o.chance)}</b></span>`;
      if(o.type==='guess')return `<span class="nb-goal-out"><span class="nb-goal-bird">${o.silhouette?sil(...o.key.split(':').map(Number)):'<span class="bk-q" aria-hidden="true">?</span>'}</span><b>?</b></span>`;
      if(o.type==='new')return `<span class="nb-goal-out"><span class="nb-goal-bird is-unknown"><span class="bk-q" aria-hidden="true">?</span></span><b class="hot">${o.chance>=.95?`≈${Math.round(o.chance)}种`:pct(o.chance)}</b></span>`;
      if(o.type==='birds'){const [e,id]=o.key.split(':').map(Number);return `<span class="nb-goal-out"><span class="nb-goal-bird">${characterPortrait(e,id)}</span><b>≈${o.n}</b></span>`;}
      return `<span class="nb-goal-out"><img class="nb-goal-coin" src="${BIZ_ART}coin.png" alt=""><b>${o.profit>=0?'+':''}${o.profit}</b></span>`;
    };
    const cards=g.cards.map((c,i)=>{
      const go=c.action==='guess'?'继续推测':c.kind==='income'?'开这一锅':'做这一锅';
      const cost=c.action==='guess'?'':`<span class="nb-goal-cost">${kitIcon.coin}${c.cash}</span>`;
      // one secondary reason on the card face (device walkthrough: two 「也能补…」 ran to three lines); the rest is a small +N
      const more=c.also.length-1,also=c.also.length?`<small class="nb-goal-also"${more?` title="${esc(c.also.join('，'))}"`:''}>${esc(c.also[0])}${more?`<i class="nb-also-more" aria-label="还有 ${more} 个">+${more}</i>`:''}</small>`:'';
      const button=`<button type="button" class="nb-goal-go" data-nb-goal="${i}">${go}</button>`;
      // an investigation has no outcome to show yet: the set and the button share a row
      const rows=c.action==='guess'?`<div class="nb-goal-row">${set(c)}${button}</div>`:`<div class="nb-goal-row">${set(c)}${outcome(c.outcome)}</div><div class="nb-goal-row nb-goal-foot">${cost}${also}${button}</div>`;
      return `<article class="nb-goal is-${c.kind}${c.goal?' is-goal':''}" role="listitem" aria-label="${esc(c.reason)}"><div class="nb-goal-why"><img src="${GOAL_ICON[c.kind]??GOAL_ICON.order}" alt=""><span>${esc(c.reason)}</span></div>${rows}</article>`;
    }).join('');
    return `${goalBar}${track}<div class="nb-goals" role="list" aria-label="推荐">${cards}</div>`;
  }
  // Start a batch for one set: buys what is missing, asks first when eggs would be given up.
  function fireSet(p){
    const s=getState(),c=costs(s,p);
    if(s.cp<c.total){alertBox(`CP 不够：这一锅要 ${c.total} CP`);return;}
    // a regional trial is cooked in the region's mode (exact set, its own odds), anything else as an ordinary batch
    const go=()=>startCook(p.toolId,draftState=>{for(const id of c.missing)E.buyIngredient(draftState,id,1,{forBatch:true});if(p.regional){prepareRegionalRecipe(draftState,p.regional);return;}draftState.selected=[...p.ingredients];draftState.egg=p.egg;delete draftState.events.seasonalRecipe;delete draftState.expansion.prepareMode;});
    const remaining=s.batch?.eggs.filter(e=>!e.collected).length??0;
    // 「先去收」 goes back to the pot (it used to only close the question and leave 下一锅 open)
    if(remaining)confirmBox(`还有 ${remaining} 只没收\n开火会放弃它们`,go,false,{yes:'放弃并开火',no:'先去收',onNo:closePanel});else go();
  }
  function bind(){
    panels.querySelectorAll('[data-nb-lens]').forEach(b=>b.onclick=()=>{const next=b.dataset.nbLens;if(next==='manual'){manual=true;}else{lens=next;manual=false;guess=null;}alt=0;preset=null;recipe=null;sound(3);render();});
    find('[data-nb-clues]')?.addEventListener('click',()=>openClueBook?.());
    find('[data-nb-business]')?.addEventListener('click',()=>openBusiness?.());
    panels.querySelectorAll('[data-nb-book]').forEach(b=>b.onclick=()=>openClueBook?.('focus'));
    panels.querySelectorAll('[data-nb-journey]').forEach(b=>b.onclick=()=>openJourney?.(b.dataset.nbJourney));
    panels.querySelectorAll('[data-nb-goal]').forEach(b=>b.onclick=()=>{
      const c=goals(getState(),getNow(),menuNow(),goal).cards[Number(b.dataset.nbGoal)];if(!c)return;
      if(c.action==='guess'){startGuess(c.guess);sound(3);render();return;}
      fireSet({toolId:c.toolId,egg:c.egg,ingredients:c.ingredients,regional:c.regional??null});
    });
    find('[data-nb-clues-guess]')?.addEventListener('click',()=>openClueBook?.('clues'));
    panels.querySelectorAll('[data-nb-guess]').forEach(b=>b.onclick=()=>{const g=narrowDirections(getState(),egg,getNow())[Number(b.dataset.nbGuess)];if(!g)return;startGuess(g);sound(3);render();});
    find('[data-nb-unguess]')?.addEventListener('click',()=>{guess=null;sound(3);render();});
    panels.querySelectorAll('[data-nb-alt]').forEach(b=>b.onclick=()=>{alt=Number(b.dataset.nbAlt);manual=false;preset=null;recipe=null;sound(3);render();});
    panels.querySelectorAll('[data-nb-egg]').forEach(b=>b.onclick=()=>{egg=Number(b.dataset.nbEgg);if(guess&&guess.egg!==egg)guess=null;alt=0;preset=null;recipe=null;sound(3);render();});
    find('[data-nb-unpreset]')?.addEventListener('click',()=>{lens='goals';preset=null;recipe=null;render();});
    find('[data-nb-help]')?.addEventListener('click',()=>{help=true;render();});
    panels.querySelectorAll('[data-nb-help-close]').forEach(b=>b.onclick=()=>{help=false;render();});
    panels.querySelectorAll('[data-nb-tool]').forEach(b=>b.onclick=()=>{toolId=Number(b.dataset.nbTool);if(guess&&guess.toolId!==toolId)guess=null;manual=true;preset=null;recipe=null;sound(3);render();});
    panels.querySelectorAll('[data-id]').forEach(b=>b.onclick=()=>{const id=+b.dataset.id;if(draft.includes(id))draft=draft.filter(i=>i!==id);else if(draft.length<max())draft.push(id);else{sound(13);const counter=find('.counter');if(counter)counter.textContent=`最多 ${max()} 种，先取消一种`;return;}manual=true;preset=null;recipe=null;sound(3);render();});
    find('[data-ok]')?.addEventListener('click',()=>{if(act(()=>{const s=getState();s.selected=draft;delete s.events.seasonalRecipe;delete s.expansion.prepareMode;}))closePanel();});
    find('[data-restock]')?.addEventListener('click',()=>openRestock(()=>open({keep:true})));
    find('[data-nb-fire]')?.addEventListener('click',()=>{if(pick)fireSet(pick);});
  }
  // keep: re-open with the same pick (after visiting the shop); tool: focus one cookware; preset: {key,menu};
  // recipe: {key,egg,toolId,ingredients} from the 线索册; guess: a 推测方向 from the 线索册 (「去试」).
  // goal: what 生意 sent the player for (a menu slot or an order); 「推荐」 puts its card first.
  function open({tool=null,keep=false,preset:next=null,recipe:wantedRecipe=null,guess:wantedGuess=null,lens:wanted=null,goal:wantedGoal=null}={}){
    const s=getState();
    if(!keep){manual=false;guess=null;alt=0;help=false;draft=[...s.selected];egg=s.egg;preset=next;recipe=wantedRecipe;lens='goals';goal=wantedGoal;if(recipe)lens='new';if(tool!==null){toolId=tool;}if(wanted&&LENSES.some(([id])=>id===wanted))lens=wanted;if(wantedGuess){lens='new';startGuess(wantedGuess);}}
    render();
  }
  return {open,refresh:()=>{if(find('.next-batch-screen'))render();}};
}
