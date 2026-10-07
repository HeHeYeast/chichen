// 仓库: one home for partners and materials (kitchen and farm both open it).
// A kit page: square painted tiles (picture, count, name), bookmarks for 伙伴 / 材料, and one plank
// button. Selling never asks the player to count one by one: 「卖掉多余」 sells every kind down to the number the
// player locked at home (one by default; set on each kind's sheet), 「挑着卖」 picks whole kinds with a tap, and one kind's sheet sets an
// amount with quick picks or the wooden slider.
import * as E from './engine.js';
import {inventoryView,lockedCount} from './inventory.js';
import {resolveSpecies} from './content-registry.js';
import {speciesView,saleSelectionSummary} from './collection-ui.js';
import {basketQuote,effects} from './progression.js';
import {materialCapacity,materialCount} from './material-capacity.js';
import {helpCardsMarkup,firstVisitGuide} from './game-frame.js';
import {interfaceIcon} from './ui-icons.js';
import {kitTabs,kitSheet,kitCell,kitCoin,kitButton,kitButton2,kitChip,kitChipHtml,kitBar,kitIcon,kitEgg} from './ui-kit.js';

const esc=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const number=n=>Number(n??0).toLocaleString('zh-CN');
const COIN=kitIcon.coin;

export function createWarehouseUI({getState,getNow,showPanel,panels,act,confirmBox,alertBox,sound,characterPortrait,ingredientPortrait,showCharacter,openShopFor,afterSale,skillFeedback}){
  const saleEffects=quote=>{const e=effects(getState()),events=[];if(quote.markup)events.push({id:'TRADE-2',text:'招牌加价 +'+quote.markup+' CP'});if(quote.baskets)events.push({id:'TRADE-3',text:quote.baskets+'次整筐 · +'+quote.baskets*e.basketBonus+' CP'});if(quote.platters)events.push({id:'TRADE-4',text:quote.platters+'次拼盘 · +'+quote.platters*e.platterBonus+' CP'});if(events.length)skillFeedback?.(events);};
  // sheet: null | 'extra' | 'bird' | 'mat' | 'help'; picking: the 「挑着卖」 mode with its chosen amounts.
  let tab='birds',egg='all',sheet=null,selected=null,amount=1,picking=false,picks={};
  const find=q=>panels.querySelector(q);
  const lockOf=key=>lockedCount(getState(),key);
  function row(key){const s=getState(),[e,id]=key.split(':').map(Number),v=inventoryView(s,key),view=speciesView(s,e,id);return {key,egg:e,id,name:view?.name??'',price:view?.price??0,...v};}
  function birds(){
    const s=getState();
    return Object.keys(s.farm??{}).filter(key=>s.farm[key]>0&&resolveSpecies(key)).map(row)
      .filter(r=>egg==='all'||String(r.egg)===egg).sort((a,b)=>b.T-a.T||a.key.localeCompare(b.key,undefined,{numeric:true}));
  }
  const extraOf=r=>Math.max(0,Math.min(r.free,r.home-lockOf(r.key)));
  function allExtras(){const s=getState(),sel={};for(const key of Object.keys(s.farm??{})){if(!(s.farm[key]>0)||!resolveSpecies(key))continue;const n=extraOf(row(key));if(n>0)sel[key]=n;}return sel;}
  function sell(selection,done){
    const summary=saleSelectionSummary(getState(),selection,{useRewards:true});if(!summary.quantity){alertBox('没有可以卖的伙伴');return;}
    const snapshot=Object.freeze({...summary.selection});
    // Re-render first, then show the skill feedback (a re-render dismisses toasts).
    confirmBox(`卖掉 ${number(summary.quantity)} 只\n+${number(summary.income)} CP`,()=>{let settled=null;if(act(()=>{settled=basketQuote(getState(),snapshot,{useRewards:true});E.sell(getState(),snapshot,{keepOne:true,useRewards:true},getNow());sound(8);})){afterSale?.();done?.();saleEffects?.(settled);}},false,{title:'出售',yes:'卖掉',no:'先留着'});
  }
  const picked=()=>Object.entries(picks).filter(([,n])=>n>0);
  // Partners stand on wooden shelves (like the shop): the count hangs on a tag at the shelf edge, a status
  // (订单 / 在外 / 营业, or 留 1 while picking) sits on the corner; picked ones glow and their tag says how many go.
  const shelfBird=(r,{count,badge='',attrs,label,on=false,off=false})=>`<button type="button" class="sh-item wh-item${on?' on':''}${off?' is-locked':''}" ${attrs} aria-label="${esc(label)}"><span class="sh-item-art" data-visual>${characterPortrait(r.egg,r.id)}${badge?`<b class="sh-item-badge${off?' soft':''}">${esc(badge)}</b>`:''}</span><span class="sh-tag wh-count${on?' hot':''}">${esc(count)}</span><span class="sh-item-name">${esc(r.name)}</span></button>`;
  function cellBird(r){
    const tag=r.Q?`订单 ${r.Q}`:r.R?'在外':r.S?'营业':'';
    if(picking){const n=picks[r.key]??0,can=extraOf(r)>0;
      return shelfBird(r,{count:n?`卖 ${n}`:`×${r.T}`,on:n>0,off:!can,badge:can?'':(lockOf(r.key)?`锁 ${lockOf(r.key)}`:''),attrs:`data-wh-pick="${r.key}" ${can?'':'disabled'}`,label:`${r.name}，${n?`卖 ${n} 只`:`在家 ${r.home} 只`}`});}
    return shelfBird(r,{count:`×${r.T}`,badge:tag,attrs:`data-wh-bird="${r.key}"`,label:`${r.name}，在家 ${r.home} 只`});
  }
  const eggCoins=()=>`<div class="kp-coins" role="group" aria-label="蛋种">${kitCoin('全','data-wh-egg="all"',egg==='all','全部')}${kitCoin(kitEgg(false),'data-wh-egg="0"',egg==='0','鸡宝')}${kitCoin(kitEgg(true),'data-wh-egg="1"',egg==='1','鸭宝')}</div>`;
  function render(){
    const s=getState(),list=birds(),sel=allExtras(),extraN=Object.values(sel).reduce((a,b)=>a+b,0);
    const cap=materialCapacity(s),used=materialCount(s),mats=Object.entries(s.ingredients).filter(([,n])=>n>0);
    const home=Object.keys(s.farm??{}).filter(k=>s.farm[k]>0&&resolveSpecies(k)).reduce((a,k)=>a+inventoryView(s,k).home,0);
    const tabs=kitTabs([{label:'伙伴',attrs:'data-wh-tab="birds"',on:tab==='birds'},{label:'材料',attrs:'data-wh-tab="mats"',on:tab==='mats'}],'仓库分类');
    let body,foot,above='';
    if(tab==='birds'){
      const total=picked().reduce((a,[,n])=>a+n,0),income=total?saleSelectionSummary(s,Object.fromEntries(picked()),{useRewards:true}).income:0;
      const head=picking?`<div class="kp-bar wh-pickbar" data-row>${kitButton2('全选','data-wh-preset="extra"')}${kitButton2('清空','data-wh-preset="none"')}</div>`
        :`<div class="kp-bar" data-row>${kitChipHtml(`${kitIcon.chick}<b class="wh-home">${number(home)}</b>只<span class="wide-only">在家</span>`,'wh-total')}${eggCoins()}</div>`;
      body=`${head}<div class="sh-shelf wh-grid">${list.length?list.map(cellBird).join(''):`<p class="kp-empty">还没有伙伴，去厨房做一锅吧</p>`}</div>`;
      above=picking?`${kitChip('',`${number(total)} 只`,'mini')}${kitChipHtml(`${COIN}+${number(income)}`,'mini')}`:'';
      foot=picking?`${kitButton2('取消','data-wh-pick-cancel')}${kitButton('卖掉','data-wh-pick-sell'+(total?'':' disabled'))}`
        :`${kitButton2('挑着卖','data-wh-pick-start')}${kitButton('卖掉多余',`data-wh-sell-extra ${extraN?'data-next':'disabled'}`)}`;
    }else{
      body=`<div class="kp-bar wh-bag" data-row aria-label="材料包 ${used} / ${cap} 格">${kitChipHtml(`${kitIcon.pouch}材料包`,'mini')}${kitBar(cap?used/cap*100:0,'材料包')}<b class="wh-bag-n">${used}/${cap}</b></div>
        <div class="wh-slots wh-grid" role="list">${mats.flatMap(([id,n])=>{const name=E.label(E.ingredient(Number(id)));return Array.from({length:n},(_,i)=>`<button type="button" class="wh-slot${i?'':' first'}" role="listitem" data-wh-mat="${id}" aria-label="${esc(name)}${i?'':`，${n} 份`}"><span data-visual>${ingredientPortrait(Number(id))}</span></button>`);}).join('')}${Array.from({length:Math.max(0,cap-used)},()=>'<span class="wh-slot is-empty" aria-hidden="true"></span>').join('')}</div>${mats.length?`<div class="gd-label">包里有</div><div class="wh-kinds">${mats.map(([id,n])=>`<button type="button" class="gd-chip mini wh-kind" data-wh-mat="${id}"><span class="wh-kind-art" data-visual>${ingredientPortrait(Number(id))}</span>${esc(E.label(E.ingredient(Number(id))))}<b>${n}</b></button>`).join('')}</div>`:''}${mats.length?'':'<p class="kp-empty">材料包是空的，去商店或寻访带一些回来</p>'}`;
      foot=kitButton('去商店','data-wh-shop');
    }
    showPanel('仓库',`${tabs}${kitSheet(body,foot,picking?'wh-picking':'',above,{attrs:'data-list'})}${sheetMarkup(sel)}`,'screen-panel warehouse-screen',{skin:'kitchen',icon:interfaceIcon('farm'),help:'data-wh-help'});
    bind();
    if(!sheet&&!picking)firstVisitGuide('warehouse',[{selector:'#panels .wh-grid',text:'每格是一种伙伴和它的数量'},{selector:'#panels [data-wh-sell-extra]',text:'多出来的可以一键卖掉；点一种伙伴，可以改它留几只在家'}]);
  }
  // The amount sheet for one kind: quick picks, the wooden slider, plus/minus for fine tuning.
  function amountMarkup(r,max,n,{confirm,confirmLabel,extra='',sum=''}){
    const quick=[...new Map([[1,'1 只'],[Math.max(1,Math.round(max/2)),'一半'],[max,'到锁定数']].filter(([v])=>v>0&&v<=max).map(([v,l])=>[v,l])).entries()];
    return `<div class="gd-qty" data-row><button type="button" class="gd-round minus" data-wh-step="-1" aria-label="少卖" ${n>1?'':'disabled'}></button><output class="gd-big" aria-live="polite" data-wh-amount>${n}<small>只</small></output><button type="button" class="gd-round plus" data-wh-step="1" aria-label="多卖" ${n<max?'':'disabled'}></button></div>
      <label class="gd-slider gd-optional"><span class="sr-only">拖动选择数量</span><input type="range" min="1" max="${max}" step="1" value="${n}" data-wh-range style="--fill:${max>1?(n-1)/(max-1)*100:100}%"></label>
      <div class="gd-coins" data-row>${quick.map(([v,l])=>`<button type="button" class="gd-coinwrap${v===n?' on':''}" data-wh-set="${v}" aria-pressed="${v===n}"><span class="gd-coin">${v}</span><b>${l}</b></button>`).join('')}</div>
      ${sum}<div class="gd-actions" data-row>${extra}${kitButton(confirmLabel,confirm)}</div>`;
  }
  function sheetMarkup(sel){
    if(!sheet)return '';
    let inner='',title='';
    if(sheet==='extra'){const summary=saleSelectionSummary(getState(),sel,{useRewards:true});title='卖掉多余';
      inner=`<div class="wh-preview gd-scroll-row">${Object.entries(summary.selection).sort((a,b)=>b[1]-a[1]).map(([key,n])=>{const [e,id]=key.split(':').map(Number);return kitCell({pic:characterPortrait(e,id),name:row(key).name,count:`×${n}`});}).join('')}</div>
        <div class="gd-row" data-row>${kitChip('',`${number(summary.quantity)} 只`,'mini')}${kitChipHtml(`${COIN}+${number(summary.income)}`,'mini')}${kitChip('','留够锁定数','mini')}</div>
        <div class="gd-actions" data-row>${kitButton2('先不卖','data-wh-close')}${kitButton('卖掉','data-wh-confirm-extra')}</div>`;}
    if(sheet==='bird'||sheet==='pick'){const r=row(selected),max=extraOf(r),lock=lockOf(r.key);title=r.name;
      amount=Math.max(max?1:0,Math.min(amount,max));const last=r.home-amount<1;
      const head=`<div class="gd-head" data-row><span class="gd-face">${characterPortrait(r.egg,r.id)}</span><div class="gd-row">${kitChip('',`在家 ${r.home}`,'soft')}${kitChipHtml(`${COIN}${r.price}/只`,'soft')}</div></div>`;
      // 「留在家」: how many of this kind are kept at home (not sold, not stocked, not delivered). Quick picks only.
      const keepRow=sheet==='bird'?`<div class="wh-lock"><div class="gd-label"><img class="wh-lock-ic" src="/web/art/golden-journey/lock.png" alt="">留在家</div><div class="gd-coins" data-row>${[...new Set([0,1,2,3,r.T].filter(v=>v<=r.T))].sort((x,y)=>x-y).map(v=>`<button type="button" class="gd-coinwrap${v===lock?' on':''}" data-wh-lock="${v}" aria-pressed="${v===lock}" aria-label="${v===0?'不留':v===r.T&&r.T>3?'全部留着':'留'+v+'只'}"><span class="gd-coin">${v}</span><b>${v===0?'不留':v===r.T&&r.T>3?'全留':'留'}</b></button>`).join('')}</div></div>`:'';
      if(sheet==='pick')inner=`${head}${amountMarkup(r,max,amount,{confirm:'data-wh-pick-set',confirmLabel:'就这些',extra:kitButton2('不卖它','data-wh-pick-drop')})}`;
      else inner=`${head}${keepRow}${max?amountMarkup(r,max,amount,{confirm:'data-wh-sell-one',confirmLabel:'卖出',extra:kitButton2('看档案','data-wh-profile'),sum:`<div class="gd-row" data-row>${kitChipHtml(`${COIN}+${number(amount*r.price)}`,'mini')}${last?kitChip('','最后 1 只','mini hot'):''}</div>`})
        :`<span class="gd-note">${r.free>0&&r.home>0?`已按「留在家 ${lock}」留着，要卖就把上面的数改小`:r.home?'都在外面或被订单留着':'已经全部卖出，图鉴记录还在'}</span><div class="gd-actions" data-row>${kitButton2('看档案','data-wh-profile')}${kitButton('好','data-wh-close')}</div>`}`;}
    if(sheet==='mat'){const id=Number(selected),n=getState().ingredients[id]??0,m=E.ingredient(id);title=E.label(m);
      inner=`<div class="gd-head" data-row><span class="gd-face">${ingredientPortrait(id)}</span><div class="gd-row">${kitChip('',`有 ${n} 份`,'soft')}${kitChipHtml(`${COIN}${m?.buy_cp??0}`,'soft')}</div></div>
        <div class="gd-actions" data-row>${kitButton2('关闭','data-wh-close')}${kitButton('去商店买',`data-wh-buy="${id}"`)}</div>`;}
    if(sheet==='help'){title='这页怎么玩';inner=`${helpCardsMarkup('warehouse')}<div class="gd-actions" data-row>${kitButton('知道了','data-wh-close')}</div>`;}
    return `<div class="wh-scrim kp-scrim" data-wh-close></div><section class="wh-sheet kp-drawer gd" role="dialog" aria-modal="true" aria-label="${esc(title)}"><div class="kp-drawer-title" data-art="plank"><span data-safe><b>${esc(title)}</b></span></div><div class="kp-drawer-body">${inner}</div></section>`;
  }
  function bind(){
    const all=(q,f)=>panels.querySelectorAll(q).forEach(f);
    all('[data-wh-tab]',b=>b.onclick=()=>{tab=b.dataset.whTab;sheet=null;picking=false;sound(3);render();});
    all('[data-wh-egg]',b=>b.onclick=()=>{egg=b.dataset.whEgg;sound(3);render();});
    all('[data-wh-bird]',b=>b.onclick=()=>{selected=b.dataset.whBird;amount=Math.max(1,extraOf(row(selected))||1);sheet='bird';sound(4);render();});
    all('[data-wh-mat]',b=>b.onclick=()=>{selected=b.dataset.whMat;sheet='mat';sound(4);render();});
    all('[data-wh-close]',b=>b.onclick=()=>{sheet=null;render();});
    find('[data-wh-help]')?.addEventListener('click',()=>{sheet='help';render();});
    find('[data-wh-sell-extra]')?.addEventListener('click',()=>{sheet='extra';render();});
    find('[data-wh-confirm-extra]')?.addEventListener('click',()=>sell(allExtras(),()=>{sheet=null;render();}));
    // Picking: a tap takes a kind's extras; tapping it again opens its amount sheet.
    find('[data-wh-pick-start]')?.addEventListener('click',()=>{picking=true;picks={};sound(3);render();});
    find('[data-wh-pick-cancel]')?.addEventListener('click',()=>{picking=false;picks={};sound(2);render();});
    all('[data-wh-preset]',b=>b.onclick=()=>{picks=b.dataset.whPreset==='extra'?allExtras():{};sound(3);render();});
    all('[data-wh-pick]',b=>b.onclick=()=>{const key=b.dataset.whPick;if(picks[key]){selected=key;amount=picks[key];sheet='pick';sound(4);}else{picks[key]=extraOf(row(key));sound(3);}render();});
    find('[data-wh-pick-set]')?.addEventListener('click',()=>{picks[selected]=amount;sheet=null;render();});
    find('[data-wh-pick-drop]')?.addEventListener('click',()=>{delete picks[selected];sheet=null;render();});
    find('[data-wh-pick-sell]')?.addEventListener('click',()=>sell(Object.fromEntries(picked()),()=>{picking=false;picks={};render();}));
    // Amount: plus/minus and quick picks re-render; the slider updates live and commits on release.
    all('[data-wh-lock]',b=>b.onclick=()=>{const n=Number(b.dataset.whLock),key=selected;if(act(()=>{const p=getState().expansion.inventoryPolicy;p.locks={...(p.locks??{}),[key]:n};})){sound(3);amount=Math.max(1,extraOf(row(key))||1);render();}});
    all('[data-wh-step]',b=>b.onclick=()=>{amount+=Number(b.dataset.whStep);render();});
    all('[data-wh-set]',b=>b.onclick=()=>{amount=Number(b.dataset.whSet);render();});
    const range=find('[data-wh-range]');if(range){const out=find('[data-wh-amount]');range.oninput=()=>{const max=Number(range.max);range.style.setProperty('--fill',(max>1?(range.value-1)/(max-1)*100:100)+'%');if(out)out.firstChild.textContent=range.value;};range.onchange=()=>{amount=Number(range.value);render();};}
    find('[data-wh-sell-one]')?.addEventListener('click',()=>sell({[selected]:amount},()=>{amount=1;render();}));
    find('[data-wh-profile]')?.addEventListener('click',()=>{const [e,id]=selected.split(':').map(Number);showCharacter(e,id);});
    find('[data-wh-buy]')?.addEventListener('click',()=>openShopFor(Number(find('[data-wh-buy]').dataset.whBuy)));
    find('[data-wh-shop]')?.addEventListener('click',()=>openShopFor(null));
  }
  // bird: open straight onto one partner's sheet (from its book page).
  function open({tab:next='birds',bird=null}={}){tab=bird?'birds':next;sheet=null;picking=false;picks={};if(bird){selected=bird;amount=Math.max(1,extraOf(row(bird))||1);sheet='bird';}render();}
  return {open,refresh:()=>{if(find('.warehouse-screen'))render();}};
}
