import {lockedCount} from './inventory.js';
// 生意 → 项目: four long projects with three stages each. Delivering and paying
// are separate, explicit confirmations; nothing here pays CP back.
import {projectsModel,deliveryCandidates,portraitCandidates} from './project-model.js';
import {deliverProject,completeProjectStage,setProjectPortraits} from './projects.js';
import {uiPreference,setUiPreference} from './ui-preferences.js';
import {tradeNav} from './order-ui.js';
import {shopSubpageHeader,shopSubpagePaper,shopSubpageDialog,bindShopSubpage} from './business-subpages.js';
import {familyArt,familyBasket} from './business-family-art.js';
import {kitButton,kitButton2,kitArt,kitLabel} from './ui-kit.js';

const esc=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const number=v=>Number(v??0).toLocaleString('zh-CN');
const resultText=r=>({'PJ-1':'可保存3套菜单预设，开张时一键摆货。','PJ-2':'配齐一篮溪岸风味，学会当地做法。','PJ-3':'为小店添一本茶坡风味册。','PJ-4':'把四地的好味道收进展册。'}[r.id]??r.result);
export const pinnedProject=()=>{const p=uiPreference('pinnedTarget');return p?.kind==='project'?p.id:null;};

export function createProjectUI({getState,commitProgress,showPanel,panels,alertBox,confirmBox,characterPortrait,openBusiness,openOrders,openRegulars}){
  let view={kind:'list'},draft={},choice=[],keepOne=true,portraits=null;
  const find=q=>panels.querySelector(q),all=q=>panels.querySelectorAll(q);
  const portrait=row=>characterPortrait?characterPortrait(row.egg,row.id):'';
  let lastView='';
  function shell(body,footer){
    const signature=JSON.stringify(view),keep=signature===lastView?find('.projects-scroll')?.scrollTop??0:0;lastView=signature;
    const helpOpen=find('.bs-sub-dialog')?.open;find('.bs-sub-dialog')?.close();
    showPanel('生意簿 · 项目',`${shopSubpageHeader('projects')}${tradeNav('projects')}<div class="projects-scroll scroll" tabindex="0" data-list>${shopSubpagePaper('projects',body)}</div><footer class="projects-footer">${footer??''}</footer>${shopSubpageDialog('projects')}`,'screen-panel projects-screen shop-subpage shop-projects');
    bindShopSubpage(panels,openBusiness);if(helpOpen)find('.bs-sub-dialog').showModal();
    all('[data-trade-view]').forEach(b=>b.onclick=()=>{const v=b.dataset.tradeView;if(v==='business')openBusiness?.();else if(v==='orders')openOrders?.();else if(v==='regulars')openRegulars?.();});
    const scroller=find('.projects-scroll');if(scroller&&keep)scroller.scrollTop=keep;
  }
  function open(id=null){view=id?{kind:'detail',id}:{kind:'list'};draft={};choice=[];portraits=null;render();}
  function refresh(){if(find('.projects-screen'))render();}
  function render(){const m=projectsModel(getState(),pinnedProject());
    if(view.kind==='list')list(m);else{const r=m.rows.find(x=>x.id===view.id);if(view.kind==='detail')detail(r);else if(view.kind==='deliver')deliver(r);else portraitsSheet(r);}}
  // A project is a blueprint with three stage stamps (stamped when done, inked for the one in hand, faint for later).
  const STAMP_ART={done:'/web/art/golden-collection/stamp-done.png',now:'/web/art/golden-business/family-stamp-active.png',later:'/web/art/golden-collection/stamp-pending.png'};
  const stageState=(r,st)=>st.complete?'done':st.current&&r.gateMet?'now':'later';
  const stamp=(state,label,cls='')=>`<span class="pj-stamp is-${state} ${cls}" style="background-image:url(${STAMP_ART[state]})"><b>${esc(label)}</b></span>`;
  const stamps=(r,small=false)=>`<div class="pj-stamps${small?' small':''}" role="img" aria-label="已完成 ${r.progress}/3 阶段">${r.stages.map((st,i)=>stamp(stageState(r,st),small?'一二三'[i]:st.label)).join('')}</div>`;
  const tick=met=>met?'<img src="/web/art/golden-business/family-check.png" alt="已达成">':'<img src="/web/art/golden-journey/lock.png" alt="未达成">';
  const cond=c=>`<span class="sh-cond${c.met?' met':''}"><i>${tick(c.met)}</i>${esc(c.text)}</span>`;
  const leader=(label,value,cls='')=>`<div class="pj-line ${cls}"><b>${esc(label)}</b><i class="rb-dots" aria-hidden="true"></i><span>${esc(value)}</span></div>`;
  function list(m){
    const focus=m.visible.find(r=>r.pinned)??m.visible.find(r=>r.gateMet&&!r.complete)??m.visible[0];
    const cards=m.visible.filter(r=>r!==focus).map(r=>`<button type="button" class="pj-card${r.complete?' is-done':''}${r.recommended?' is-locked':''}" data-project-open="${r.id}" aria-label="${esc(r.name)}，${r.recommended?'尚待筹备':r.complete?'已完成':`已完成 ${r.progress}/3 阶段`}">${kitArt('blueprint','pj-card-art')}<strong class="pj-card-name">${esc(r.name)}</strong>${stamps(r,true)}<b class="pj-card-go" aria-hidden="true">›</b></button>`).join('');
    const next=focus?(focus.gateMet?(focus.current?.checks.find(c=>!c.met)?.text??(focus.current?.delivery&&!focus.current.delivery.full?`还需交付 ${focus.current.delivery.target-focus.current.delivery.total} 只`:'可以登记当前阶段')):(focus.gate.find(c=>!c.met)?.text??'')):'';
    shell(focus?`<section class="pj-hero"><span class="pj-eyebrow">${focus.pinned?'置顶筹备':focus.complete?'留在小店的成果':focus.gateMet?'正在筹备':'下一份小店计划'}</span><div class="pj-hero-top">${kitArt('blueprint','pj-blueprint')}<div class="pj-hero-name"><h3>${esc(focus.name)}</h3>${stamps(focus,true)}</div></div><p class="pj-result">${esc(resultText(focus))}</p>${next&&!focus.complete?`<p class="pj-next">${tick(next==='可以登记当前阶段')}${esc(next)}</p>`:''}<div class="gd-actions" data-row>${kitButton(focus.complete?'回看成果':focus.gateMet?'继续筹备':'看看条件',`data-project-open="${focus.id}"`)}</div></section>${cards?`${kitLabel('其他项目')}<div class="pj-cards">${cards}</div>`:''}`:'<div class="family-empty">还没有可以筹备的项目。</div>');
    all('[data-project-open]').forEach(b=>b.onclick=()=>{view={kind:'detail',id:b.dataset.projectOpen};render();});
  }
  function stageMarkup(r,st){
    const d=st.delivery,state=stageState(r,st);
    const deliveryLine=d?leader(d.kind==='choose'?(d.locked?`交付 · 已选定 ${d.locked.length} 种`:`交付 · 任选 ${d.distinct} 种各 ${d.quantityEach} 只`):`交付 · 至少 ${d.minimumCategories} 类菜式`,`${d.total}/${d.target}`,d.full?'is-met':''):'';
    const choices=d?deliveryCandidates(getState(),st).slice(0,d.kind==='choose'?d.distinct:3):[];
    const slots=d?`<div class="pj-slots">${choices.map(c=>`<div class="pj-slot">${familyBasket(c)}<strong>${esc(c.name)}</strong><small>${d.locked||c.delivered?`已交 ${c.delivered}${d.kind==='choose'?'/'+d.quantityEach:''}`:'可选出品'}</small></div>`).join('')||`<div class="pj-slot">${familyArt('family-unknown')}<small>还没有可交付的伙伴</small></div>`}</div>`:'';
    const checks=st.checks.filter(c=>c.text!=='前一阶段已完成');
    const funds=state==='now'&&st.costCP?`<div class="pj-line pj-funds">${familyArt('coin')}<b>花费</b><i class="rb-dots" aria-hidden="true"></i><span>${number(st.costCP)} CP</span></div><small class="pj-purse${st.ready&&!st.canPay?' short':''}">持有 ${number(getState().cp)}${st.ready&&!st.canPay?' · CP不足':''}</small>`:'';
    const actions=state==='now'?`<div class="gd-actions" data-row>${d&&!d.full?kitButton2('挑选交付',`data-project-deliver="${st.id}"${st.checksMet?'':' disabled'}`):''}${kitButton(st.costCP?'付款完成':'登记完成',`data-project-complete="${st.id}"${st.canPay?'':' disabled'}`)}</div>`:'';
    const body=state==='done'?`<p class="pj-done-text">${esc(st.text)}</p>`:`${state==='now'?slots:''}${checks.length?`<div class="sh-conds pj-conds">${checks.map(cond).join('')}</div>`:''}${deliveryLine}${funds}${actions}`;
    return `<li class="pj-stage is-${state}"><header class="pj-stage-head">${stamp(state,st.label)}<span class="pj-stage-state">${state==='done'?'已完成':state==='now'?'进行中':'待筹备'}</span></header>${body}</li>`;
  }
  function detail(r){
    const portraitsRow=r.id==='PJ-4'&&r.gateMet?`<section class="pj-portraits">${kitLabel(`展册画像 ${r.portraits.length}`)}<div class="pj-portrait-row">${r.portraits.map(k=>{const [egg,id]=k.split(':').map(Number);return `<span>${portrait({egg,id})}</span>`;}).join('')||'<small class="pj-note">还没有挂上画像</small>'}</div><div class="gd-actions" data-row>${kitButton2('布置展册画像','data-project-portraits')}</div></section>`:'';
    shell(`<header class="pj-head"><span class="pj-eyebrow">${r.complete?'小店的成果':'小店筹备'}</span><h3>${esc(r.name)}</h3>${stamps(r)}<p class="pj-result">${esc(resultText(r))}</p>${r.complete?`<p class="pj-note">${esc(r.next)}</p>`:''}</header>
      ${r.gateMet?'':`<section class="pj-gate">${kitLabel('开始条件')}<div class="sh-conds pj-conds">${r.gate.map(cond).join('')}</div></section>`}
      <ol class="pj-stages">${r.stages.map(st=>stageMarkup(r,st)).join('')}</ol>${portraitsRow}`,
      `${kitButton2('返回项目','data-project-list')}${r.complete?'':kitButton2(r.pinned?'取消置顶':'置顶','data-project-pin')}`);
    find('[data-project-list]').onclick=()=>{view={kind:'list'};render();};
    find('[data-project-pin]')?.addEventListener('click',()=>{setUiPreference('pinnedTarget',r.pinned?null:{kind:'project',id:r.id});render();});
    find('[data-project-portraits]')?.addEventListener('click',()=>{portraits=[...r.portraits];view={kind:'portraits',id:r.id};render();});
    all('[data-project-deliver]').forEach(b=>b.onclick=()=>{draft={};choice=[];view={kind:'deliver',id:r.id,stageId:b.dataset.projectDeliver};render();});
    all('[data-project-complete]').forEach(b=>b.onclick=()=>{const st=r.stages.find(x=>x.id===b.dataset.projectComplete);
      confirmBox(`${r.name} · ${st.label}\n${st.costCP?`支付 ${number(st.costCP)} CP 完成这一阶段。\n当前持有 ${number(getState().cp)} CP。`:'登记这一阶段，不花费CP。'}\n完成后不退款、不可撤销。`,()=>{
        const result=commitProgress(s=>completeProjectStage(s,r.id,st.id));if(result!==null&&result!==false)render();else render();
      },false,{yes:st.costCP?'支付并完成':'登记',no:'再想想'});});
  }
  // Handing partners over: each partner stands on the shelf; tap it to choose it (choose stages), then a quick coin
  // sets how many go (不交 / 1 / 一半 / 全部). The keep-one rule is a painted switch. No checkboxes, no one-by-one steps.
  function deliver(r){
    const st=r.stages.find(x=>x.id===view.stageId),d=st.delivery;
    if(!d||d.full||!st.current){view={kind:'detail',id:r.id};render();return;}
    const candidates=deliveryCandidates(getState(),st),choosing=d.kind==='choose'&&!d.locked;
    const maxFor=row=>{const room=d.kind==='choose'?d.quantityEach-row.delivered:d.target-d.total-Object.entries(draft).filter(([k])=>k!==row.key).reduce((a,[,n])=>a+n,0);return Math.max(0,Math.min(room,row.free,row.home-lockedCount(getState(),row.key)));};
    const cards=candidates.map(row=>{const picked=choice.includes(row.key),n=draft[row.key]??0,max=maxFor(row),open=!choosing||picked;
      const coins=[...new Map([[0,'不交'],[1,'1 只'],[Math.max(1,Math.round(max/2)),'一半'],[max,'全部']].filter(([v])=>v<=max&&(v>0||n>0)).map(([v,l])=>[v,l])).entries()];
      return `<li class="pj-pick${n?' on':''}${open?'':' is-waiting'}">${choosing?`<button type="button" class="pj-pick-who" role="checkbox" aria-checked="${picked}" data-project-choice="${row.key}" aria-label="选定${esc(row.name)}">`:'<span class="pj-pick-who">'}<span class="pj-pick-art" aria-hidden="true">${portrait(row)}</span>${choosing&&picked?kitArt('ic-check','pj-pick-tick'):''}${choosing?'</button>':'</span>'}
        <span class="pj-pick-name"><b>${esc(row.name)}</b><small>可用 ${row.free} 只${row.delivered?` · 已交 ${row.delivered}`:''}</small></span>
        <span class="pj-pick-n"><b>${n}</b><small>只</small></span>
        <div class="gd-coins pj-coins" data-row>${max?coins.map(([v,l])=>`<button type="button" class="gd-coinwrap${v===n?' on':''}" data-project-set="${row.key}" data-n="${v}" aria-pressed="${v===n}" ${open?'':'disabled'}><span class="gd-coin">${v}</span><b>${l}</b></button>`).join(''):'<small class="pj-note">这一种交满了</small>'}</div></li>`;}).join('');
    const total=Object.values(draft).reduce((a,b)=>a+b,0);
    shell(`<header class="pj-head"><span class="pj-eyebrow">${esc(r.name)} · ${st.label}</span><h3>交付伙伴</h3><p class="pj-note">交出的伙伴不付货款，也不会回来。营业、寻访和订单留着的不会被动用。</p></header>
      ${choosing?`<p class="pj-choose">先点选 ${d.distinct} 种 · 已选 <b>${choice.length}/${d.distinct}</b><small>第一次交付后就定下了</small></p>`:''}<ul class="pj-picks projects-picks">${cards||'<li class="pj-note">农场里还没有可交付的伙伴。</li>'}</ul>`,
      `${kitButton2('返回','data-project-back')}${kitButton(`交付 ${total} 只`,`data-project-confirm${total>0&&(!choosing||choice.length===d.distinct)?'':' disabled'}`)}`);
    find('[data-project-back]').onclick=()=>{view={kind:'detail',id:r.id};render();};
    all('[data-project-choice]').forEach(b=>b.onclick=()=>{const k=b.dataset.projectChoice;if(!choice.includes(k)){if(choice.length>=d.distinct){alertBox(`只能选定${d.distinct}种。`);return;}choice.push(k);}else{choice=choice.filter(x=>x!==k);delete draft[k];}render();});
    all('[data-project-set]').forEach(b=>b.onclick=()=>{const k=b.dataset.projectSet,n=Number(b.dataset.n);if(n)draft[k]=n;else delete draft[k];render();});
    find('[data-project-confirm]').onclick=()=>{
      const selection=Object.fromEntries(Object.entries(draft).filter(([,n])=>n>0)),n=Object.values(selection).reduce((a,b)=>a+b,0);
      confirmBox(`交付 ${n} 只给「${r.name}」。\n交付后不退回，也不付货款。${choosing?`\n选定的${d.distinct}种之后不能更换。`:''}`,()=>{
        const result=commitProgress(s=>deliverProject(s,r.id,st.id,selection,{choice:choosing?[...choice]:null,overrideKeepOne:!keepOne}));
        if(result!==null&&result!==false){draft={};choice=[];view={kind:'detail',id:r.id};}render();
      },false,{yes:'交付',no:'再看看'});
    };
  }
  // The portrait wall: tap a partner to hang (or take down) its portrait; hung ones glow and get a tick.
  function portraitsSheet(r){
    const c=portraitCandidates(getState());
    shell(`<header class="pj-head"><span class="pj-eyebrow">四地风味展</span><h3>选择展册画像</h3><p class="pj-note">只用永久画像，不消耗库存，不算食用交付。</p><p class="pj-choose">已挂 <b>${portraits.length}/${c.max}</b></p></header>
      <div class="pj-portrait-grid">${c.rows.map(row=>{const on=portraits.includes(row.key);return `<button type="button" class="pj-portrait${on?' on':''}" role="checkbox" aria-checked="${on}" data-project-portrait="${row.key}"><span class="pj-portrait-art" aria-hidden="true">${portrait(row)}</span>${on?kitArt('ic-check','pj-pick-tick'):''}<small>${esc(row.name)}</small></button>`;}).join('')}</div>`,
      `${kitButton2('返回','data-project-back')}${kitButton('保存展册','data-project-portraits-save')}`);
    find('[data-project-back]').onclick=()=>{view={kind:'detail',id:r.id};render();};
    all('[data-project-portrait]').forEach(b=>b.onclick=()=>{const k=b.dataset.projectPortrait;if(!portraits.includes(k)){if(portraits.length>=c.max){alertBox(`展册最多放${c.max}幅画像。`);return;}portraits.push(k);}else portraits=portraits.filter(x=>x!==k);render();});
    find('[data-project-portraits-save]').onclick=()=>{const result=commitProgress(s=>setProjectPortraits(s,[...portraits]));if(result!==null&&result!==false)view={kind:'detail',id:r.id};render();};
  }
  return {open,refresh,resume:()=>render()};
}
