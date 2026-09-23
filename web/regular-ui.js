// 生意 → 常客: four folded relationships, one unread stage each, two ways to
// continue. Reading is the only command; it never pays or consumes stock.
import {regularsModel} from './regular-model.js';
import {readRegularStage} from './regulars.js';
import {uiPreference,setUiPreference} from './ui-preferences.js';
import {tradeNav} from './order-ui.js';

const esc=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const STATUS={read:'已读',pending:'可继续',current:'还需一件事',later:'之后'};
// The single global pin (shared later with themes/regions/projects in Work K).
export const pinnedRegular=()=>{const p=uiPreference('pinnedTarget');return p?.kind==='regular'?p.id:null;};

export function createRegularUI({getState,commitProgress,showPanel,panels,openBusiness,openOrders,openProjects}){
  let detail=null,reading=false;
  const find=q=>panels.querySelector(q),all=q=>panels.querySelectorAll(q);
  function shell(body,footer){
    showPanel('生意簿 · 常客',`${tradeNav('regulars')}<div class="regulars-scroll scroll" tabindex="0">${body}</div><footer class="regulars-footer">${footer??''}</footer>`,'screen-panel regulars-screen');
    all('[data-trade-view]').forEach(b=>b.onclick=()=>{if(b.dataset.tradeView==='business')openBusiness?.();else if(b.dataset.tradeView==='orders')openOrders?.();else if(b.dataset.tradeView==='projects')openProjects?.();});
  }
  function open(id=null){detail=id;reading=false;render();}
  function refresh(){if(find('.regulars-screen'))render();}
  function render(){const m=regularsModel(getState(),pinnedRegular());if(detail)page(m.rows.find(r=>r.id===detail));else list(m);}
  function list(m){
    const cards=m.rows.map(r=>r.opened||r.readCount||r.pending
      ?`<button class="regulars-card ${r.pending?'has-unread':''}" data-regular-open="${r.id}"><span class="regulars-token" aria-hidden="true">${esc(r.name.slice(0,1))}</span><span class="regulars-card-text"><strong>${esc(r.name)}${r.pinned?' · 置顶':''}</strong><small>${esc(r.regionName)} · ${esc(r.topic)}</small></span><span class="regulars-folds" aria-label="已读${r.readCount}/4">${r.stages.map(st=>`<i class="is-${st.status}"></i>`).join('')}</span></button>`
      :`<div class="regulars-card is-locked"><span class="regulars-token" aria-hidden="true">?</span><span class="regulars-card-text"><strong>还没来访</strong><small>${esc(r.current?.gate.find(g=>!g.met)?.text??'')}</small></span></div>`).join('');
    shell(`<p class="regulars-intro">常客不排名、不计好感，也没有期限。每位最多留一段未读；读完才会有下一件事。</p>${cards}`);
    all('[data-regular-open]').forEach(b=>b.onclick=()=>{detail=b.dataset.regularOpen;render();});
  }
  function page(r){
    const folds=r.stages.map(st=>`<li class="regulars-fold is-${st.status}"><span class="regulars-fold-index">${st.index+1}</span><div><strong>${st.status==='later'?'之后的一段':esc(st.title)}</strong><small>${STATUS[st.status]}${st.status==='read'?` · ${esc(st.rewardName)}`:''}</small>${st.status==='read'?`<details><summary>回读</summary><p>${esc(st.text)}</p><p class="regulars-next">${esc(st.next)}</p></details>`:''}</div></li>`).join('');
    let now='';
    if(r.pending)now=`<section class="regulars-now"><span class="regulars-eyebrow">可继续 · ${esc(r.pending.branchText)}</span><h3>${esc(r.pending.title)}</h3>${reading?`<p class="regulars-story">${esc(r.pending.text)}</p><p class="regulars-next">${esc(r.pending.next)}</p>`:'<p>这一段已经留在常客页，读完才会开始下一件事。读故事不扣库存、不付款。</p>'}</section>`;
    else if(r.current)now=`<section class="regulars-now"><span class="regulars-eyebrow">第${r.current.index+1}段 · ${esc(r.current.title)}</span><h3>${r.current.gateMet?'任选一条路继续':'还需一件事'}</h3>
      <ul class="regulars-gate">${r.current.gate.map(g=>`<li class="${g.met?'is-met':''}">${g.met?'✓ ':''}${esc(g.text)}</li>`).join('')}</ul>
      ${r.current.gateMet?`<ol class="regulars-branches">${r.current.branches.map(b=>`<li class="${b.met?'is-met':''}">${esc(b.text)}</li>`).join('')}</ol><p class="regulars-note">${r.current.index===0&&r.current.branches[0]?.met&&!r.current.branches[1]?.met?'营业记录已达成；累计每12只实际销量会招待一轮来客，等这位常客带来故事。也可完成另一条手动采购，直接在这里阅读。':'营业记录与手动采购/寻访任一条达成都算；后续故事可追认已有记录。'}</p>`:''}</section>`;
    else now=`<section class="regulars-now"><h3>四段都读完了</h3><p>「${esc(r.mementoName)}」已收进纪念物，可去农场陈列。</p></section>`;
    const pin=!r.complete?`<button data-regular-pin>${r.pinned?'取消置顶':'置顶这位常客'}</button>`:'';
    shell(`<header class="regulars-head"><span class="regulars-eyebrow">${esc(r.regionName)}${r.acquainted?' · 旧三章已认识':''}</span><h3>${esc(r.name)}</h3></header>${now}<ol class="regulars-folds-list">${folds}</ol>`,
      `<button data-regular-list>返回常客</button>${pin}${r.pending?`<button class="orange" data-regular-read>${reading?'读完了，收好这段':'读这一段'}</button>`:''}`);
    find('[data-regular-list]').onclick=()=>{detail=null;render();};
    find('[data-regular-pin]')?.addEventListener('click',()=>{setUiPreference('pinnedTarget',r.pinned?null:{kind:'regular',id:r.id});render();});
    find('[data-regular-read]')?.addEventListener('click',()=>{
      if(!reading){reading=true;render();return;}
      const result=commitProgress(s=>readRegularStage(s,r.id));
      if(result!==null&&result!==false)reading=false;
      render();
    });
  }
  return {open,refresh,resume:()=>render()};
}
