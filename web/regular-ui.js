import {productionAsset} from './production-art.js';
import {VISITOR_DIALOGUES} from './visitor-dialogue.js';
// 生意 → 常客: four folded relationships, one unread stage each, two ways to
// continue. Reading is the only command; it never pays or consumes stock.
import {regularsModel} from './regular-model.js';
import {readRegularStage} from './regulars.js';
import {uiPreference,setUiPreference} from './ui-preferences.js';
import {tradeNav} from './order-ui.js';
import {shopSubpageHeader,shopSubpagePaper,shopSubpageDialog,bindShopSubpage} from './business-subpages.js';
import {guestAvatar,guestIdentity,familyStamp,familyArt} from './business-family-art.js';
import {kitButton,kitButton2,kitChip,kitLabel} from './ui-kit.js';

const esc=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const STATUS={read:'已读',pending:'可继续',current:'还需一件事',later:'之后'};
// The single global pin (shared later with themes/regions/projects in Work K).
export const pinnedRegular=()=>{const p=uiPreference('pinnedTarget');return p?.kind==='regular'?p.id:null;};

const stamps=(stages,big=false)=>`<span class="rg-stamps${big?' big':''}" aria-hidden="true">${stages.map(st=>`<i class="is-${st.status}"><img src="/web/art/golden-collection/${st.status==='read'?'stamp-done':'stamp-pending'}.png" alt="">${big?`<b>${st.index+1}</b>`:''}</i>`).join('')}</span>`;

export function createRegularUI({getState,commitProgress,showPanel,panels,openBusiness,openOrders,openProjects,openJourney}){
  let introState=null,detail=null,reading=false,dialogueTurn=0,dialogueStage=null;
  const find=q=>panels.querySelector(q),all=q=>panels.querySelectorAll(q);
  function shell(body,footer){
    const scroll=find('.regulars-scroll')?.scrollTop??0,helpOpen=find('.bs-sub-dialog')?.open;
    find('.bs-sub-dialog')?.close();
    showPanel('生意簿 · 常客',`${shopSubpageHeader('regulars')}${tradeNav('regulars')}<div class="regulars-scroll scroll" tabindex="0" data-list>${shopSubpagePaper('regulars',body)}</div><footer class="regulars-footer">${footer??''}</footer>${shopSubpageDialog('regulars')}`,'screen-panel regulars-screen shop-subpage shop-regulars');
    find('.regulars-scroll').scrollTop=scroll;bindShopSubpage(panels,openBusiness);if(helpOpen)find('.bs-sub-dialog').showModal();
    all('[data-trade-view]').forEach(b=>b.onclick=()=>{if(b.dataset.tradeView==='business')openBusiness?.();else if(b.dataset.tradeView==='orders')openOrders?.();else if(b.dataset.tradeView==='projects')openProjects?.();});
  }
  function open(id=null){introState=null;if(id?.startsWith('intro:')){intro(id.slice(6),0);return;}detail=id;reading=false;render();}
  function intro(id,turn){
    introState={id,turn};
    const lines=VISITOR_DIALOGUES[id]?.first;if(!lines)return;
    const last=turn===lines.length-1,scene=productionAsset('regulars',id,'scene');
    shell(`<div class="rg-conversation"><img class="rg-scene" src="${scene}" alt="来访的老街坊"><div class="rg-conversation-paper"><b class="rg-speaker">老街坊</b><p class="regulars-story">${esc(lines[turn].text)}</p></div></div>`,`${kitButton2('先忙去了','data-intro-close')}${kitButton(lines[turn].reply,'data-intro-next')}`);
    find('[data-intro-close]').onclick=()=>openBusiness?.();
    find('[data-intro-next]').onclick=()=>{if(last){commitProgress(s=>{s.events.loopVisitorMet=true;});openJourney?.('V');}else intro(id,turn+1);};
  }
  function refresh(){if(find('.regulars-screen'))render();}
  function render(){if(introState){intro(introState.id,introState.turn);return;}const m=regularsModel(getState(),pinnedRegular());if(detail)page(m.rows.find(r=>r.id===detail));else list(m);}
  function list(m){
    // A 2×2 guest book: big portrait, name, four story stamps, and a red ribbon for a new story.
    const cards=m.rows.map(r=>r.opened||r.readCount||r.pending
      ?`<button type="button" class="regulars-card rg-card ${r.pending?'has-unread':''}" data-regular-open="${r.id}">${r.pending?'<span class="rg-new">有新故事</span>':''}${guestAvatar(r.id)}<span class="regulars-card-text"><strong>${esc(r.name)}</strong><small>${guestIdentity(r.id)}</small></span>${stamps(r.stages)}${r.pinned?'<span class="rg-pin">置顶</span>':''}<span class="sr-only">${r.complete?'故事已读完':`故事 ${r.readCount}/4`}</span></button>`
      :`<div class="regulars-card rg-card is-locked">${guestAvatar(r.id,false)}<span class="regulars-card-text"><strong>还没来访</strong><small>继续经营</small></span></div>`).join('');
    shell(`${kitLabel(m.unread?`${m.unread} 段新故事`:'小店里的熟面孔')}<div class="rg-grid">${cards}</div>`);
    find('.bs-sub-dialog').insertAdjacentHTML('beforeend',`<h4>来访线索</h4>${m.rows.filter(r=>!r.opened&&!r.readCount&&!r.pending).map(r=>`<p>${esc(r.current?.gate.filter(g=>!g.met).map(g=>g.text).join('；')||'继续经营，慢慢认识')}</p>`).join('')||'<p>熟面孔都已记在册中。</p>'}`);
    all('[data-regular-open]').forEach(b=>b.onclick=()=>{detail=b.dataset.regularOpen;if(find('.regulars-scroll'))find('.regulars-scroll').scrollTop=0;render();});
  }
  function page(r){
    if(dialogueStage!==r.pending?.id){dialogueStage=r.pending?.id;dialogueTurn=0;reading=false;}
    const approved=null; // Story dialogue follows the completed order or service; arrival dialogue belongs to intro().
    const lines=approved?.map(x=>x.text)??(r.pending?.text??'').split(/\n\n+/).filter(Boolean),lastLine=dialogueTurn>=lines.length-1;
    const tick=met=>met?'<img src="/web/art/golden-business/family-check.png" alt="已达成">':'';
    let now='';
    if(r.pending)now=`<section class="regulars-now rg-now${reading?' is-dialogue':''}">${familyArt('family-envelope','rg-envelope')}<span class="rg-eyebrow">可继续 · ${esc(r.pending.branchText)}</span><h3>${esc(r.pending.title)}</h3>${reading?`<p class="regulars-story">${esc(lines[dialogueTurn])}</p>${lastLine?`<p class="regulars-next">${esc(r.pending.next)}</p>`:''}<span class="rg-dialogue-progress">${dialogueTurn+1} / ${lines.length}</span>`:'<span class="gd-note">有新对话，点下方查看</span>'}</section>`;
    else if(r.current)now=`<section class="regulars-now rg-now"><span class="rg-eyebrow">第 ${r.current.index+1} 段 · ${esc(r.current.title)}</span><h3>${r.current.gateMet?'任选一条继续':'还需一件事'}</h3>
      <div class="sh-conds regulars-gate">${r.current.gate.map((g,i)=>`<span class="sh-cond${g.met?' met':''}"><i>${tick(g.met)||i+1}</i>${esc(g.text)}</span>`).join('')}</div>
      ${r.current.gateMet?`<div class="sh-conds regulars-branches">${r.current.branches.map((b,i)=>`<span class="sh-cond${b.met?' met':''}"><i>${tick(b.met)||'或'}</i>${esc(b.text)}</span>`).join('')}</div>`:''}</section>`;
    else now=`<section class="regulars-now rg-now">${familyStamp('done','已读完')}<h3>这几次来往都记下了</h3><span class="gd-note">「${esc(r.mementoName)}」收进了纪念物</span></section>`;
    const scene=productionAsset('regulars',r.id,'scene');
    if(scene)now=`<div class="rg-conversation"><img class="rg-scene" src="${scene}" alt="${esc(r.name)}"><div class="rg-conversation-paper"><b class="rg-speaker">${esc(r.name)}</b>${now}</div></div>`;
    const read=r.stages.filter(st=>st.status==='read');
    const later=r.stages.filter(st=>st.status==='later');
    const ahead=later.length?`${kitLabel('之后')}<div class="rg-reads">${later.map(st=>`<div class="rg-later"><b>第 ${st.index+1} 段</b><span>还没开始</span><img src="/web/art/golden-journey/lock.png" alt=""></div>`).join('')}${r.mementoName?`<div class="gd-row">${kitChip('',`完成来往 · 得「${r.mementoName}」`,'mini')}</div>`:''}</div>`:'';
    const folds=read.length?`${kitLabel('读过的')}<div class="rg-reads">${read.map(st=>`<details class="rg-read"><summary><b>第 ${st.index+1} 段</b><span>${esc(st.title)}</span><i>回看</i></summary><p>${esc(st.text)}</p><p class="regulars-next">${esc(st.next)}</p></details>`).join('')}</div>`:'';
    shell(`<header class="regulars-head family-guest-head rg-head${reading?' is-reading':''}">${guestAvatar(r.id,r.opened||r.readCount||!!r.pending)}<div class="rg-head-text"><span class="rg-eyebrow">${guestIdentity(r.id)}</span><h3>${esc(r.name)}</h3>${stamps(r.stages,true)}</div></header>${now}${reading?'':folds+ahead}`,
      `${kitButton2('返回','data-regular-list')}${!r.complete&&!reading?kitButton2(r.pinned?'取消置顶':'置顶','data-regular-pin'):''}${r.pending?kitButton(reading?(lastLine?'知道了':approved?.[dialogueTurn]?.reply??'接着听'):'听听看','data-regular-read'):''}${reading&&lastLine?kitButton2('看订单','data-regular-next'):''}`);
    find('[data-regular-next]')?.addEventListener('click',()=>{const result=commitProgress(s=>readRegularStage(s,r.id));if(result!==null&&result!==false)openOrders?.();});
    find('[data-regular-list]').onclick=()=>{detail=null;render();};
    find('[data-regular-pin]')?.addEventListener('click',()=>{setUiPreference('pinnedTarget',r.pinned?null:{kind:'regular',id:r.id});render();});
    find('[data-regular-read]')?.addEventListener('click',()=>{
      if(!reading){reading=true;dialogueTurn=0;render();return;}
      if(!lastLine){dialogueTurn++;render();return;}
      const result=commitProgress(s=>readRegularStage(s,r.id));
      if(result!==null&&result!==false)reading=false;
      render();
    });
  }
  return {open,refresh,resume:()=>render()};
}
