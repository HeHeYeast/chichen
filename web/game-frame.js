// Shared, text-light guidance for every page (no persistent hint bars):
//  * stars + species slots instead of requirement sentences;
//  * the element marked [data-next] breathes (CSS) and, after a few idle seconds, gets a small arrow;
//  * a one-time first-visit pointer per page;
//  * 「?」 picture cards (icon + one line) instead of long help text.
import {uiPreference,setUiPreference} from './ui-preferences.js';
import {resolveSprite,spriteSVG,uiIcon} from './art/manifest.js';

const esc=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const TIER_STARS={ordinary:1,suitable:2,complete:3};

export function starsMarkup(tier,label=true){
  const n=TIER_STARS[tier]??1,name={1:'普通',2:'合适',3:'完整'}[n];
  return `<span class="gf-stars" ${label?`aria-label="${name}"`:'aria-hidden="true"'}>${'★'.repeat(n)}<i>${'★'.repeat(3-n)}</i></span>`;
}

// kind: 'ok' (already there), 'need' (known, still needed), 'unknown' (never collected).
// The count sits under the picture; a needed slot that can be cooked shows a small pan next to it.
export function slotMarkup({kind,count,portrait='',attrs='',label='',make=false}){
  const tag=attrs?'button':'span';
  return `<${tag} class="gf-slot is-${kind}" ${attrs} ${label?`aria-label="${esc(label)}"`:''}>${kind==='unknown'?'<span class="gf-q" aria-hidden="true">?</span>':portrait}${count!=null?`<b>×${count}${make?icon('pan','gf-pan'):''}</b>`:''}</${tag}>`;
}

// Picture cards for 「?」: [icon, line]. Icons are the game's painted pieces (no line drawings, no emoji:
// the bundled font subset is checked against every character in web/*.js).
const A='/web/art/';
const ART={basket:A+'basket-v4.png',star:A+'golden-business/projects.png',clock:A+'golden-journey/precision-clock.png',list:A+'golden-business/orders.png',
  check:A+'golden-business/family-check.png',pan:A+'golden-kitchen/tool-1.png',map:A+'golden-journey/map-icon.png',chick:'chick',bag:A+'golden-journey/pouch.png',
  box:'barn',coin:A+'golden-business/coin.png',book:'book',question:A+'golden-journey/unknown.png',gift:A+'golden-journey/pouch.png',
  letter:A+'golden-journey/envelope.png',sign:A+'golden-business/sign-prepare.png'};
const icon=(name,cls='')=>{const src=ART[name]??ART.question;
  if(src==='barn'||src==='book'||src==='chick'){const sprite=src==='chick'?resolveSprite('/web/art/chick-v4-0.png'):resolveSprite(uiIcon(src==='barn'?5:6));return `<span class="gf-art ${cls}" aria-hidden="true">${spriteSVG(sprite)}</span>`;}
  return `<img class="gf-art ${cls}" src="${src}" alt="" aria-hidden="true" draggable="false">`;};
export const HELP_CARDS={
  business:[['basket','点货篮选出品，点「换菜单」改组合'],['star','凑齐菜单，对应出品售价 +25%'],['list','订单够数可交付；不足时点「去做」']],
  orders:[['list','点订单查看品种、数量和报酬'],['check','可用库存够数后，点「交付」'],['pan','缺货时点「去做」，准备补货']],
  journey:[['map','选地区和方向'],['chick','同行已帮你选好'],['bag','回来点「全部收下」']],
  warehouse:[['box','伙伴和材料都在这里'],['coin','「卖掉多余」只卖超过锁定数量的'],['basket','「挑着卖」只卖点中的']],
  nextBatch:[['star','推荐综合追踪目标、订单和收益'],['pan','新伙伴按线索进度列出可尝试的目标'],['coin','开火前确认费用和要补买的调味']],
  book:[['book','品种、配方、收藏、日历'],['question','没收录的点开看线索'],['pan','「去做」打开下一锅']],
  shrine:[['sign','每天求一签'],['gift','小礼每天领一次'],['letter','来信开放节日伙伴']],
  harvest:[['chick','这一锅收好了'],['coin','多的可以直接卖'],['pan','接着做下一锅']],
  calendar:[['clock','凤凰看开火时间，要 Lv.3 保温灯'],['question','开火有机会遇见，不保证每锅都有'],['letter','节日伙伴：先完成来信，节日里开火']],
  seasonal:[['star','四时手记 16 位全年都能遇见'],['pan','收录后点开看配方，直接做下一锅'],['coin','一季四位收齐，领 600 CP 回礼']],
};
export function helpCardsMarkup(key){
  const cards=HELP_CARDS[key]??[];
  const detail=key==='nextBatch'?`<section class="gf-help-detail"><h3>四个方向怎么选？</h3><p>推荐会综合追踪目标、订单缺口和收入，挑出现在值得做的几锅。新伙伴按线索册顺序列出最近的目标；订单看当前需求，多赚比较每种厨具的预计每小时净收益。</p><h3>配方还不完整怎么办？</h3><p>新伙伴卡片会标出下一步。已知配方可以直接准备；线索缩小到某一类调味时，可以去试做，自己选择剩余调味。还缺线索就去对应地区寻访。调味标签与线索中的类别一致。</p><h3>准备和开火有什么区别？</h3><p>准备只打开配方，不扣钱、不消耗材料。开火前会显示本锅费用，缺少且可以买到的调味会一起购买。如果上一锅还没收完，会先提醒你回去收取。</p></section>`:'';
  return `<div class="gf-help-cards" role="list">${cards.map(([name,line])=>`<div class="gf-help-card" role="listitem">${icon(name)}<span>${esc(line)}</span></div>`).join('')}</div>${detail}`;
}
export const frameIcon=icon;

// ---- idle nudge: after 4 s without input, point at the visible [data-next] element.
let game=null,nudge=null,timer=0,observer=null;
const IDLE_MS=4000;
function hideNudge(){if(nudge)nudge.hidden=true;}
function visibleNext(){
  const dialogs=game.querySelector('#dialog-layer');if(dialogs?.children.length)return null;
  const open=[...game.querySelectorAll('dialog[open]')].at(-1),scope=open??game;
  const target=[...scope.querySelectorAll('[data-next]')].find(el=>{const r=el.getBoundingClientRect();return r.width>0&&r.height>0&&!el.disabled&&getComputedStyle(el).visibility!=='hidden';});
  return target??null;
}
function showNudge(){
  if(!game||document.hidden)return;const target=visibleNext();if(!target){hideNudge();return;}
  const g=game.getBoundingClientRect(),r=target.getBoundingClientRect(),k=g.width/game.offsetWidth||1;
  if(!nudge){nudge=document.createElement('div');nudge.className='gf-nudge';nudge.setAttribute('aria-hidden','true');game.append(nudge);}
  const right=(r.right-g.left)/k,mid=(r.top-g.top+r.height/2)/k,roomRight=game.offsetWidth-right>40;
  nudge.classList.toggle('to-left',!roomRight);
  Object.assign(nudge.style,roomRight?{left:right+6+'px',top:mid-16+'px'}:{left:(r.left-g.left)/k-40+'px',top:mid-16+'px'});
  nudge.hidden=false;
}
function rearm(){hideNudge();clearTimeout(timer);timer=setTimeout(showNudge,IDLE_MS);}
export function installGameFrame(root){
  game=root;['pointerdown','keydown','wheel'].forEach(type=>root.addEventListener(type,rearm,{capture:true,passive:true}));
  observer=new MutationObserver(rearm);observer.observe(root.querySelector('#panels')??root,{childList:true});
  rearm();
}

// ---- first-visit pointer: steps [{selector,text}], shown once per key.
export function firstVisitGuide(key,steps){
  // Automated runs (navigator.webdriver) skip the one-time pointer; players always get it once.
  if(!game||uiPreference('guide:'+key)||navigator.webdriver)return;
  // A page that renders again while its pointer is up (生意 re-renders on every change) keeps the one already shown.
  if([...game.querySelectorAll('.gf-guide')].some(g=>g.dataset.guide===key))return;
  const usable=steps.filter(s=>game.querySelector(s.selector));if(!usable.length)return;
  let i=0;const layer=document.createElement('div');layer.className='gf-guide';layer.dataset.guide=key;layer.setAttribute('role','dialog');layer.setAttribute('aria-label','第一次来');
  // The pointer never blocks play: tapping the highlighted control (or anything) acts and closes it.
  const outside=e=>{if(!layer.contains(e.target))done();};
  const done=()=>{setUiPreference('guide:'+key,true);layer.remove();game.removeEventListener('pointerdown',outside,true);rearm();};
  game.addEventListener('pointerdown',outside,true);
  const draw=()=>{
    const step=usable[i],el=game.querySelector(step.selector);if(!el){done();return;}
    const g=game.getBoundingClientRect(),r=el.getBoundingClientRect(),k=g.width/game.offsetWidth||1;
    const x=(r.left-g.left)/k,y=(r.top-g.top)/k,w=r.width/k,h=r.height/k,last=i===usable.length-1;
    layer.innerHTML=`<div class="gf-ring" style="left:${x-6}px;top:${y-6}px;width:${w+12}px;height:${h+12}px"></div><div class="gf-tip" style="top:${y>game.offsetHeight/2?Math.max(8,y-112):y+h+16}px"><p>${esc(step.text)}</p><div><button type="button" data-guide-skip>跳过</button><button type="button" class="is-primary" data-guide-next>${last?'知道了':'下一步'}</button></div></div>`;
    layer.querySelector('[data-guide-skip]').onclick=done;
    layer.querySelector('[data-guide-next]').onclick=()=>{if(last)done();else{i++;draw();}};
    layer.querySelector('[data-guide-next]').focus({preventScroll:true});
  };
  game.append(layer);requestAnimationFrame(draw);
}
export function resetFirstVisitGuides(){for(const k of Object.keys(HELP_CARDS))setUiPreference('guide:'+k,false);}
