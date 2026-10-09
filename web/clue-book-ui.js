// 线索册 = 调查中心: a page of its own (not one of the 图鉴 bookmarks), opened from 图鉴 → 品种 and from 下一锅; ‹ goes back
// to where it was opened from.
// 「调查」 (default) leads with the tracked partner (a big card: 调查 x/5, what is read so far, the riddle, the next step), then a
// few partners being worked out and a few recipes ready to cook. 「全部」 lists every partner not met yet, with filters.
// One button per card, chosen by what can be done now: 去制作 (recipe held, ready), 去试 (put the set together in 下一锅, once
// the clues narrow it down), 去…寻访 (its next clue is a trip away, in the region named), 研读 (buy the whole recipe;
// skill-gated). ☆ tracks one partner at a time: 下一锅「推荐」 puts it first and the 寻访 map marks its region.
// The regional partners (loop batch 4) are cards like the others: before their 方向 the next step is their region's (去…寻访
// finds the specimen or story card, 辨认 is one tap), then trips read their layers up to the complete method; held, 去制作
// tries them with the region's odds (每锅 25%, the fourth batch sure).
import * as E from './engine.js';
import {bookEscape as esc} from './book-ui.js';
import {clueBookModel,CLUE_FILTERS,CLUE_STEPS} from './clue-book.js';
import {studyRecipe,trackPartner,speciesCode} from './knowledge.js';
import {identifyMaterial} from './regional-methods.js';
import {kitSheet,kitCoin,kitButton,kitButton2,kitIcon,kitEgg,kitLabel,kitChip} from './ui-kit.js';
import {onTheWay} from './next-batch-goals.js';

const ICON='/web/art/golden-ui/';
// Condition labels are written for the detail page; a card only has room for the first clause.
const short=label=>String(label).split(' · ')[0].replace(/（([^）]*)）/g,(m,x)=>/\d+ \/ \d+/.test(x)?`（${x.replace(/ /g,'')}）`:'').replace(/^累计/,'');
const pct=x=>`${Math.max(1,Math.round(x*100))}%`;

export function createClueBookUI({getState,getNow=()=>Date.now(),panels,showPanel,characterPortrait,toolPortrait,ingredientPortrait,commit,confirmBox,alertBox,openObservation,openSpecies,openNextBatch,openRecipe,openGuess,openSkill,openJourney}){
  // back: where ‹ returns (the 品种 book page when nothing else was given)
  let egg=0,view='focus',filter='all',flash=null,back=null;
  const goBack=()=>(back??openSpecies)();
  const find=q=>panels.querySelector(q),all=q=>panels.querySelectorAll(q);
  const art=(r,size='')=>`<span class="bk-sticker-art cb-art${size} is-unknown">${r.silhouette?`<span class="unknown-silhouette" aria-hidden="true">${characterPortrait(r.egg,r.id)}</span>`:'<span class="bk-q" aria-hidden="true">?</span>'}</span>`;
  const tool=(id,level)=>`<span class="cb-tool"><span class="cb-tool-art">${toolPortrait(id,Math.max(0,level))}</span><b>Lv.${level+1}</b></span>`;
  const seasoning=id=>`<span class="cb-ing" title="${esc(E.label(E.ingredient(id)))}">${ingredientPortrait(id)}</span>`;
  const unknownSlot='<span class="cb-ing is-unknown" aria-hidden="true">?</span>';
  const pips=r=>`<span class="cb-pips" role="img" aria-label="调查 ${r.progress}/${CLUE_STEPS}">${Array.from({length:CLUE_STEPS},(_,i)=>`<i class="${i<r.progress?'on':''}"></i>`).join('')}</span>`;
  const star=r=>`<button type="button" class="cb-star${r.tracked?' is-on':''}" data-cb-track="${r.key}" aria-pressed="${r.tracked}" aria-label="${r.tracked?'取消追踪':'追踪'} ${esc(r.code)}"><img src="${ICON}ic-star.png" alt=""></button>`;
  // what is known, as pictures: cookware, then the seasonings (「?」 for the unknown ones, the flavour group when read)
  function facts(r){
    if(r.held)return `${tool(r.toolId,r.minLevel)}<i aria-hidden="true">+</i>${r.ingredients.length?r.ingredients.map(seasoning).join(''):'<small class="cb-none">不放调味</small>'}`;
    const head=r.toolId!=null?tool(r.toolId,r.minLevel):'<span class="cb-tool is-unknown" aria-hidden="true">?</span>';
    if(r.none)return `${head}<i aria-hidden="true">+</i><small class="cb-none">不放调味</small>`;
    return `${head}<i aria-hidden="true">+</i>${r.first!=null?seasoning(r.first):unknownSlot}${r.second?`<i aria-hidden="true">+</i>${kitChip('',`${r.second}类`,'mini soft cb-group')}`:''}`;
  }
  const needText=r=>r.identify!=null?r.need[0]:r.need?.length?`还差：${r.need.map(short).slice(0,2).join('、')}`:'还有条件没满足';
  // a regional trial: its odds, and how many batches at most until it is sure
  const trialText=r=>r.trial?.inPot?'这锅正在试做 · 收取后揭晓':`每枚 ${pct(r.trial.perEgg)} · 平均 ${r.trial.expected.toFixed(1)} 只`;
  const study=(r,mini=true)=>r.held||r.studyHidden?'':r.canStudy?`<button type="button" class="gd-btn2${mini?' mini':''} cb-study" data-cb-study="${r.key}" aria-label="研读 ${esc(r.code)}，${r.studyCost} CP">研读<span class="cb-cost">${kitIcon.coin}${r.studyCost}</span></button>`
    :`<button type="button" class="gd-btn2${mini?' mini':''} cb-study is-locked" data-cb-study-locked aria-label="研读：先学会手艺「配方研读」"><img class="cb-lock" src="/web/art/golden-journey/lock.png" alt="">研读</button>`;
  // the one thing to do with this partner now
  function action(r,mini=true){
    if(r.status==='ready'&&r.trial?.inPot)return '';
    if(r.status==='ready')return `<button type="button" class="cb-go" data-cb-cook="${r.key}">去制作</button>`;
    if(r.identify!=null)return `<button type="button" class="cb-go" data-cb-identify="${r.identify}" aria-label="辨认带回的标本，${esc(r.code)} 就有了做法方向">辨认</button>`;
    if(r.status==='guess')return `<button type="button" class="cb-go" data-cb-try="${r.key}" aria-label="自己配一锅试 ${esc(r.code)}">${mini?'去试':'去试一锅'}</button>`;
    // the party already out there brings it home: the button shows that trip instead of asking for another
    if(r.status==='scout'&&openJourney&&onTheWay(getState(),r))return `<button type="button" class="cb-go" data-cb-journey="${r.key}" aria-label="${esc(r.region.name)}寻访中，会带回 ${esc(r.code)} 的下一条线索">${mini?`${esc(r.region.name)}寻访中`:`寻访中 · ${esc(r.region.name)}`}</button>`;
    if(r.status==='scout'&&openJourney)return `<button type="button" class="cb-go" data-cb-journey="${r.key}" aria-label="去${esc(r.region.name)}寻访 ${esc(r.code)} 的下一条线索">${mini?`去${esc(r.region.name)}寻访`:`去寻访 · ${esc(r.region.name)}`}</button>`;
    return mini&&!r.canStudy?'':study(r,mini);
  }
  // The tracked card's second button: trying when the next clue is still a trip away, or the trip when it can be tried.
  function secondAction(r){
    const cls='gd-btn2';
    if(r.status==='scout'&&r.canTry)return `<button type="button" class="${cls}" data-cb-try="${r.key}">去试一锅</button>`;
    if(r.status==='guess'&&r.scout&&openJourney)return `<button type="button" class="${cls}" data-cb-journey="${r.key}">去寻访 · ${esc(r.region.name)}</button>`;
    return r.status==='guess'||r.status==='scout'?study(r):'';
  }
  // a compact card: the riddle for partners still being worked out, the recipe for held ones
  function card(r){
    // a regional partner before its 方向 shows the find its region still holds instead of its riddle
    const riddle=!r.held&&r.riddle?`<span class="cb-riddle">${esc(r.riddle)}</span>`:!r.held&&r.scoutFind?`<span class="cb-riddle cb-find">${esc(r.scoutFind.text)}</span>`:'';
    const need=r.status==='wait'?`<span class="cb-need">${esc(needText(r))}</span>`:'';
    const chance=r.ready?`<b class="cb-chance">${r.trial?esc(trialText(r)):`每锅 ${pct(r.chance)}`}</b>`:'';
    const act=action(r),note=riddle+need+chance;
    return `<div class="cb-card${r.tracked?' is-tracked':''}${flash===r.key?' is-new':''}" role="listitem"><div class="cb-card-top"><button type="button" class="cb-tap" data-cb-row="${r.key}" aria-label="${esc(r.code)}，调查 ${r.progress}/${CLUE_STEPS}${r.riddle?'：'+esc(r.riddle):''}">${art(r)}<span class="cb-main"><span class="cb-head"><b class="bk-code">${esc(r.code)}</b>${pips(r)}</span><span class="cb-recipe-line">${facts(r)}</span></span></button>${star(r)}</div>${note||act?`<div class="cb-card-foot"><span class="cb-card-note">${note}</span>${act}</div>`:''}</div>`;
  }
  // the tracked partner: every layer as a check, the riddle, and the next step
  function bigCard(r){
    const check=(ok,label,value,half=false)=>`<span class="cb-check${ok?'':' no'}${half?' half':''}">${ok?`<img src="${ICON}ic-check.png" alt="">`:'<i aria-hidden="true">?</i>'}<em>${label}</em>${value?`<b>${esc(value)}</b>`:''}</span>`;
    const toolName=r.toolId!=null?`${E.label(E.tool(r.toolId))} Lv.${r.minLevel+1}`:'';
    const first=r.none?'不放调味':r.first!=null?E.label(E.ingredient(r.first)):'';
    const second=r.held?(r.secondId!=null?E.label(E.ingredient(r.secondId)):'没有'):r.second?`${r.second}类`:'';
    const unmet=r.held?r.blockers.length:r.rest.unmet,restKnown=r.held||r.rest.known;
    // a regional partner's fifth layer is its complete method (read on a trip in its region, or 研读)
    const last=r.regional?check(false,'完整做法',''):check(restKnown&&!unmet,'其余条件',restKnown?unmet?`差 ${unmet} 项`:'都满足':'');
    const checks=`<div class="cb-checks">${check(r.toolId!=null,'厨具',toolName)}${check(r.first!=null||r.none,'第一味',first)}${check(r.held||!!r.second,'第二味',second,!r.held&&!!r.second)}${last}</div>`;
    const where=r.held?`<span class="cb-recipe-line">${facts(r)}${r.ready?`<b class="cb-chance">${r.trial?esc(trialText(r)):`每锅 ${pct(r.chance)}`}</b>`:''}</span>`:'';
    const step=!r.held&&r.scoutFind?`<span class="cb-need cb-find">${esc(r.scoutFind.text)}</span>`:'';
    // the cookware it needs and the kitchen does not have yet (said before the 第二味 makes it a 等条件 row)
    const level=r.toolId!=null?getState().toolLevels[r.toolId]??-1:null,tool=r.toolId!=null?E.label(E.tool(r.toolId)):'';
    const lack=r.toolId==null||r.status==='wait'?'':level<0?`还没有${tool}`:level<r.minLevel?`${tool}要到 Lv.${r.minLevel+1}`:'';
    const need=r.status==='wait'||r.status==='clue'?`<span class="cb-need">${esc(r.status==='clue'?'线索还不够，可以研读':needText(r))}</span>`:step||(lack?`<span class="cb-need">${esc(lack)}</span>`:'');
    const acts=[secondAction(r),action(r,false)].filter(Boolean).join('');
    return `<article class="cb-card cb-big is-tracked${flash===r.key?' is-new':''}" aria-label="追踪中的伙伴 ${esc(r.code)}">
      <div class="cb-big-top"><button type="button" class="cb-tap cb-big-tap" data-cb-row="${r.key}" aria-label="${esc(r.code)} 的风味观察">${art(r,' cb-art-big')}<span class="cb-main"><span class="cb-head"><b class="bk-code">${esc(r.code)}</b></span><span class="cb-progress">${pips(r)}<small>${r.held?'配方完整':`调查 ${r.progress}/${CLUE_STEPS}`}</small></span></span></button>${star(r)}</div>
      ${r.held?where:checks}${r.riddle&&!r.held?`<div class="cb-note">${esc(r.riddle)}</div>`:''}${need}${acts?`<div class="cb-acts">${acts}</div>`:''}</article>`;
  }
  const list=(rows,label)=>`<div class="cb-list" role="list" aria-label="${label}">${rows.map(card).join('')}</div>`;
  function focusBody(m){
    const s=getState(),found=m.trackedFound?`<div class="kp-bar cb-found" data-row>${kitChip(`<img class="cb-found-ic" src="${ICON}ic-check.png" alt="">`,`${speciesCode(m.trackedFound)} 已经认识了`,'mini')}<span class="cb-hint">选下一只追踪</span></div>`:'';
    const top=m.tracked?bigCard(m.tracked):`<div class="cb-card cb-empty-track"><img src="${ICON}ic-star.png" alt=""><span>点星追踪一只，下一锅先推荐它</span></div>`;
    const near=m.near.length?`${kitLabel('正在调查')}${list(m.near,'正在调查')}`:'';
    const doable=m.doable.length?`${kitLabel('现在能做')}${list(m.doable,'现在能做')}`:'';
    const rest=m.total>(m.near.length+m.doable.length)?`<div class="gd-row cb-more">${kitButton2(`全部 ${m.total} 只`,'data-cb-view="all"')}</div>`:'';
    return `${found}${top}${near}${doable}${!near&&!doable&&!m.total?'<div class="kp-empty"><span>这一册的伙伴都认识了。</span></div>':''}${rest}`;
  }
  function allBody(m){
    const rows=m.rows.filter(r=>filter==='all'||r.status===filter);
    const chips=CLUE_FILTERS.filter(([id])=>id==='all'||m.counts[id]>0).map(([id,label])=>`<button type="button" class="gd-btn2 mini" data-cb-filter="${id}" aria-pressed="${filter===id}">${label}<b>${m.counts[id]}</b></button>`).join('');
    return `<div class="gd-row cb-filters" role="group" aria-label="筛选">${chips}</div>${rows.length?list(rows,'全部伙伴'):'<div class="kp-empty"><span>没有这一类的伙伴。</span></div>'}`;
  }
  function render(){
    const s=getState(),m=clueBookModel(s,egg,getNow());
    if(view==='all'&&filter!=='all'&&!m.counts[filter])filter='all';
    const tabs=`<div class="gd-row cb-tabs" role="tablist" aria-label="线索册">${[['focus','调查'],['all','全部伙伴']].map(([v,l])=>`<button type="button" role="tab" class="gd-btn2 mini" data-cb-view="${v}" aria-selected="${view===v}" aria-pressed="${view===v}">${l}</button>`).join('')}</div>`;
    const body=`<div class="kp-bar bk-bar" data-row><div class="kp-coins" role="group" aria-label="蛋种">${kitCoin(kitEgg(false),'data-cb-egg="0"',egg===0,'鸡宝')}${kitCoin(kitEgg(true),'data-cb-egg="1"',egg===1,'鸭宝')}</div><span class="bk-count" aria-label="还没认识 ${m.total} 种"><b>${m.total}</b>只在调查</span></div>
      ${tabs}${view==='all'?allBody(m):focusBody(m)}`;
    const foot=kitButton('去下一锅','data-cb-next');
    showPanel('线索册',kitSheet(body,foot,'bk-sheet cb-sheet'),'screen-panel book-screen collection-screen clue-screen',{skin:'book',title:'线索册',icon:'<img src="/web/art/golden-journey/magnifier.png" alt="">',back:true});
    const rowOf=key=>m.rows.find(x=>x.key===key)??(m.tracked?.key===key?m.tracked:null);
    const close=find('.close');if(close)close.onclick=goBack;
    find('[data-cb-next]')?.addEventListener('click',()=>openNextBatch?.());
    all('[data-cb-egg]').forEach(b=>b.onclick=()=>{egg=+b.dataset.cbEgg;flash=null;render();});
    all('[data-cb-view]').forEach(b=>b.onclick=()=>{view=b.dataset.cbView;flash=null;render();find('.kp-scroll')?.scrollTo?.(0,0);});
    all('[data-cb-filter]').forEach(b=>b.onclick=()=>{filter=b.dataset.cbFilter;flash=null;render();});
    all('[data-cb-row]').forEach(b=>b.onclick=()=>{const scroll=find('.kp-scroll')?.scrollTop??0;openObservation(b.dataset.cbRow,()=>{render();const list=find('.kp-scroll');if(list)list.scrollTop=scroll;});});
    all('[data-cb-track]').forEach(b=>b.onclick=()=>{const r=rowOf(b.dataset.cbTrack);if(!r)return;const scroll=find('.kp-scroll')?.scrollTop??0;
      if(commit(draft=>trackPartner(draft,r.tracked?null:r.key))){flash=r.tracked?null:r.key;render();const list=find('.kp-scroll');if(list)list.scrollTop=view==='focus'&&!r.tracked?0:scroll;}});
    all('[data-cb-cook]').forEach(b=>b.onclick=()=>{const r=rowOf(b.dataset.cbCook);if(r?.ready)openRecipe?.({key:r.key,egg:r.egg,toolId:r.toolId,ingredients:[...r.ingredients],...(r.recipeId?{regional:r.recipeId,chance:r.chance}:{})});});
    // 辨认 the specimen a trip brought home: free, opens its supply and the 方向 of the partners it belongs to
    all('[data-cb-identify]').forEach(b=>b.onclick=()=>{const id=Number(b.dataset.cbIdentify),r=m.rows.find(x=>x.identify===id)??m.tracked;const scroll=find('.kp-scroll')?.scrollTop??0;
      if(commit(draft=>identifyMaterial(draft,id))){flash=r?.key??null;render();const list=find('.kp-scroll');if(list)list.scrollTop=scroll;}});
    all('[data-cb-try]').forEach(b=>b.onclick=()=>{const r=rowOf(b.dataset.cbTry);if(r?.guess)openGuess?.(r.guess);});
    all('[data-cb-journey]').forEach(b=>b.onclick=()=>{const r=rowOf(b.dataset.cbJourney);if(r?.region)openJourney?.(r.region.id,r.key);});
    all('[data-cb-study-locked]').forEach(b=>b.onclick=()=>confirmBox('研读要先在「手艺」学会「配方研读」',()=>openSkill?.('OBS-4'),false,{yes:'去看手艺',no:'好'}));
    all('[data-cb-study]').forEach(b=>b.onclick=()=>{
      const r=rowOf(b.dataset.cbStudy);if(!r)return;
      if(s.cp<r.studyCost){alertBox(`研读要 ${r.studyCost} CP，还差 ${r.studyCost-s.cp}`);return;}
      confirmBox(`花 ${r.studyCost} CP 研读 ${r.code}\n研读后配方就完整了，照着做就有机会孵出`,()=>{if(commit(draft=>studyRecipe(draft,r.key,getNow()))){flash=r.key;render();find('.cb-card.is-new')?.scrollIntoView?.({block:'center'});}},false,{yes:'研读',no:'再想想'});
    });
  }
  // tab: 'focus' (调查) or 'all'; the old 'unlocked' / 'clues' tabs open the matching filter of 全部.
  // keep: come back to the same view (after the observation or skill page); back: where ‹ returns.
  // focusKey: open on that partner (its egg, highlighted; under 全部 when 调查 does not show it).
  return {open:(options={})=>{
    back=options.back??(options.keep?back:null);egg=options.egg??egg;flash=null;
    if(options.keep){render();return;}
    const tab=options.tab??'focus';
    if(tab==='unlocked'){view='all';filter='ready';}else if(tab==='clues'){view='all';filter='guess';}else if(tab==='all'){view='all';filter='all';}else view='focus';
    if(options.focusKey){
      const key=options.focusKey;egg=Number(key.split(':')[0]);flash=key;
      const m=clueBookModel(getState(),egg,getNow()),shown=m.tracked?.key===key||m.near.some(r=>r.key===key)||m.doable.some(r=>r.key===key);
      if(!shown){view='all';filter='all';}
      render();find('.cb-card.is-new')?.scrollIntoView?.({block:'center'});return;
    }
    render();
  },refresh:()=>{if(find('.clue-screen'))render();}};
}
