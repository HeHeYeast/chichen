// 生意 → 项目: four long projects with three stages each. Delivering and paying
// are separate, explicit confirmations; nothing here pays CP back.
import {projectsModel,deliveryCandidates,portraitCandidates} from './project-model.js';
import {deliverProject,completeProjectStage,setProjectPortraits} from './projects.js';
import {uiPreference,setUiPreference} from './ui-preferences.js';
import {tradeNav} from './order-ui.js';

const esc=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const number=v=>Number(v??0).toLocaleString('zh-CN');
export const pinnedProject=()=>{const p=uiPreference('pinnedTarget');return p?.kind==='project'?p.id:null;};

export function createProjectUI({getState,commitProgress,showPanel,panels,alertBox,confirmBox,characterPortrait,openBusiness,openOrders,openRegulars}){
  let view={kind:'list'},draft={},choice=[],keepOne=true,portraits=null;
  const find=q=>panels.querySelector(q),all=q=>panels.querySelectorAll(q);
  const portrait=row=>characterPortrait?characterPortrait(row.egg,row.id):'';
  let lastView='';
  function shell(body,footer){
    const signature=JSON.stringify(view),keep=signature===lastView?find('.projects-scroll')?.scrollTop??0:0;lastView=signature;
    showPanel('生意簿 · 项目',`${tradeNav('projects')}<div class="projects-scroll scroll" tabindex="0">${body}</div><footer class="projects-footer">${footer??''}</footer>`,'screen-panel projects-screen');
    all('[data-trade-view]').forEach(b=>b.onclick=()=>{const v=b.dataset.tradeView;if(v==='business')openBusiness?.();else if(v==='orders')openOrders?.();else if(v==='regulars')openRegulars?.();});
    const scroller=find('.projects-scroll');if(scroller&&keep)scroller.scrollTop=keep;
  }
  function open(id=null){view=id?{kind:'detail',id}:{kind:'list'};draft={};choice=[];portraits=null;render();}
  function refresh(){if(find('.projects-screen'))render();}
  function render(){const m=projectsModel(getState(),pinnedProject());
    if(view.kind==='list')list(m);else{const r=m.rows.find(x=>x.id===view.id);if(view.kind==='detail')detail(r);else if(view.kind==='deliver')deliver(r);else portraitsSheet(r);}}
  function list(m){
    const cards=m.visible.map(r=>`<button class="projects-card ${r.complete?'is-done':''} ${r.recommended?'is-locked':''}" data-project-open="${r.id}"><strong>${esc(r.name)}${r.pinned?' · 置顶':''}</strong>
      <small>${r.recommended?`下一个可能的项目 · ${esc(r.gate.find(g=>!g.met)?.text??'')}`:r.complete?'已完成，可回看':`${r.progress}/3 阶段${r.current?` · 现在：${esc(r.current.id.slice(-1))}`:''}`}</small>
      <span class="projects-steps">${r.stages.map(st=>`<i class="${st.complete?'is-done':st.ready?'is-ready':''}"></i>`).join('')}</span></button>`).join('');
    shell(`<p class="projects-intro">项目只整理已有的生产、经营和发现记录；交付的伙伴不付货款，阶段费用在完成时一次支付。可同时推进，也可置顶一个。</p>${cards||'<p class="projects-intro">还没有可以开始的项目。</p>'}`);
    all('[data-project-open]').forEach(b=>b.onclick=()=>{view={kind:'detail',id:b.dataset.projectOpen};render();});
  }
  function stageMarkup(r,st){
    const d=st.delivery;
    const deliveryLine=d?(d.kind==='choose'?`<p class="projects-delivery">交付：${d.locked?`已选定 ${d.locked.length} 种`:`任选${d.distinct}种食用料理（首次交付后锁定）`}，每种${d.quantityEach}只 · 已交 ${d.total}/${d.target}</p>`
      :`<p class="projects-delivery">交付：共${d.target}只，至少${d.minimumCategories}类招牌风味 · 已交 ${d.total}/${d.target}，已有 ${d.categories.length} 类</p>`):'';
    const actions=st.complete?'':st.current&&r.gateMet?`<div class="projects-actions">${d&&!d.full?`<button data-project-deliver="${st.id}" ${st.checksMet?'':'disabled'}>交付</button>`:''}<button class="orange" data-project-complete="${st.id}" ${st.ready?'':'disabled'}>${st.costCP?`支付 ${number(st.costCP)} CP 完成`:'登记完成'}</button></div>`:'';
    return `<li class="projects-stage ${st.complete?'is-done':st.current?'is-current':''}"><header><span>${st.label}</span><strong>${st.complete?'已完成':st.costCP?`费用 ${number(st.costCP)} CP`:'无费用'}</strong></header>
      <p>${esc(st.text)}</p>${st.complete?'':`<ul class="projects-checks">${st.checks.map(c=>`<li class="${c.met?'is-met':''}">${c.met?'✓ ':''}${esc(c.text)}</li>`).join('')}</ul>`}${deliveryLine}${actions}</li>`;
  }
  function detail(r){
    const portraitsRow=r.id==='PJ-4'&&r.gateMet?`<section class="projects-portraits"><h4>展册画像 ${r.portraits.length}</h4><p class="projects-note">任何已收录的伙伴都能以永久画像加入展册，旧观赏、节令也可以；不消耗库存，也不算食用交付。</p><div class="projects-portrait-row">${r.portraits.map(k=>{const [egg,id]=k.split(':').map(Number);return `<span>${portrait({egg,id})}</span>`;}).join('')}</div><button data-project-portraits>布置展册画像</button></section>`:'';
    shell(`<header class="projects-head"><span class="projects-eyebrow">长期项目</span><h3>${esc(r.name)}</h3>${r.complete?`<p>${esc(r.text)}</p><p class="projects-note">成果：${esc(r.result)}</p><p class="projects-note">${esc(r.next)}</p>`:`<p class="projects-note">完成后：${esc(r.result)}</p>`}</header>
      ${r.gateMet?'':`<section class="projects-locked"><h4>开始条件</h4><ul class="projects-checks">${r.gate.map(c=>`<li class="${c.met?'is-met':''}">${c.met?'✓ ':''}${esc(c.text)}</li>`).join('')}</ul></section>`}
      <ol class="projects-stages">${r.stages.map(st=>stageMarkup(r,st)).join('')}</ol>${portraitsRow}`,
      `<button data-project-list>返回项目</button>${r.complete?'':`<button data-project-pin>${r.pinned?'取消置顶':'置顶'}</button>`}`);
    find('[data-project-list]').onclick=()=>{view={kind:'list'};render();};
    find('[data-project-pin]')?.addEventListener('click',()=>{setUiPreference('pinnedTarget',r.pinned?null:{kind:'project',id:r.id});render();});
    find('[data-project-portraits]')?.addEventListener('click',()=>{portraits=[...r.portraits];view={kind:'portraits',id:r.id};render();});
    all('[data-project-deliver]').forEach(b=>b.onclick=()=>{draft={};choice=[];view={kind:'deliver',id:r.id,stageId:b.dataset.projectDeliver};render();});
    all('[data-project-complete]').forEach(b=>b.onclick=()=>{const st=r.stages.find(x=>x.id===b.dataset.projectComplete);
      confirmBox(`${r.name} · ${st.label}\n${st.costCP?`支付 ${number(st.costCP)} CP 完成这一阶段。\n当前持有 ${number(getState().cp)} CP。`:'登记这一阶段，不花费CP。'}\n完成后不退款、不可撤销。`,()=>{
        const result=commitProgress(s=>completeProjectStage(s,r.id,st.id));if(result!==null&&result!==false)render();else render();
      },false,{yes:st.costCP?'支付并完成':'登记',no:'再想想'});});
  }
  function deliver(r){
    const st=r.stages.find(x=>x.id===view.stageId),d=st.delivery;
    if(!d||d.full||!st.current){view={kind:'detail',id:r.id};render();return;}
    const candidates=deliveryCandidates(getState(),st),choosing=d.kind==='choose'&&!d.locked;
    const maxFor=row=>{const room=d.kind==='choose'?d.quantityEach-row.delivered:d.target-d.total-Object.entries(draft).filter(([k])=>k!==row.key).reduce((a,[,n])=>a+n,0);return Math.max(0,Math.min(room,row.free,keepOne?row.home-1:row.free));};
    const rows=candidates.map(row=>{const picked=choice.includes(row.key),n=draft[row.key]??0,max=maxFor(row);
      return `<li class="projects-pick">${choosing?`<input type="checkbox" data-project-choice="${row.key}" ${picked?'checked':''} aria-label="选定${esc(row.name)}">`:''}<span class="projects-art" aria-hidden="true">${portrait(row)}</span><span class="projects-pick-name">${esc(row.name)}<small>自由 ${row.free}只${row.delivered?` · 已交${row.delivered}`:''}</small></span>
        <span class="projects-stepper"><button data-project-minus="${row.key}" aria-label="少交一只" ${n>0?'':'disabled'}>−</button><strong>${n}</strong><button data-project-plus="${row.key}" aria-label="多交一只" ${n<max&&(!choosing||picked)?'':'disabled'}>＋</button></span></li>`;}).join('');
    const total=Object.values(draft).reduce((a,b)=>a+b,0);
    shell(`<header class="projects-head"><span class="projects-eyebrow">${esc(r.name)} · ${st.label}</span><h3>交付伙伴</h3><p class="projects-note">交付的伙伴离开农场、不付货款；只用自由库存，营业、寻访和采购预留中的不会被动用。${choosing?`先勾选${d.distinct}种，首次交付后锁定。`:''}</p></header>
      <label class="projects-keep"><input type="checkbox" data-project-keep ${keepOne?'checked':''}>每种在家留1只</label><ul class="projects-picks">${rows||'<li class="projects-note">农场里还没有可交付的伙伴。</li>'}</ul>`,
      `<button data-project-back>返回</button><button class="orange" data-project-confirm ${total>0&&(!choosing||choice.length===d.distinct)?'':'disabled'}>交付 ${total} 只</button>`);
    find('[data-project-back]').onclick=()=>{view={kind:'detail',id:r.id};render();};
    find('[data-project-keep]').onchange=e=>{keepOne=e.target.checked;draft={};render();};
    all('[data-project-choice]').forEach(b=>b.onchange=()=>{const k=b.dataset.projectChoice;if(b.checked){if(choice.length>=d.distinct){b.checked=false;alertBox(`只能选定${d.distinct}种。`);return;}choice.push(k);}else{choice=choice.filter(x=>x!==k);delete draft[k];}render();});
    all('[data-project-plus]').forEach(b=>b.onclick=()=>{const k=b.dataset.projectPlus;draft[k]=(draft[k]??0)+1;render();});
    all('[data-project-minus]').forEach(b=>b.onclick=()=>{const k=b.dataset.projectMinus;draft[k]=Math.max(0,(draft[k]??0)-1);if(!draft[k])delete draft[k];render();});
    find('[data-project-confirm]').onclick=()=>{
      const selection=Object.fromEntries(Object.entries(draft).filter(([,n])=>n>0)),n=Object.values(selection).reduce((a,b)=>a+b,0);
      confirmBox(`交付 ${n} 只给「${r.name}」。\n交付后不退回，也不付货款。${choosing?`\n选定的${d.distinct}种之后不能更换。`:''}`,()=>{
        const result=commitProgress(s=>deliverProject(s,r.id,st.id,selection,{choice:choosing?[...choice]:null,overrideKeepOne:!keepOne}));
        if(result!==null&&result!==false){draft={};choice=[];view={kind:'detail',id:r.id};}render();
      },false,{yes:'交付',no:'再看看'});
    };
  }
  function portraitsSheet(r){
    const c=portraitCandidates(getState());
    shell(`<header class="projects-head"><span class="projects-eyebrow">四地风味展</span><h3>选择展册画像</h3><p class="projects-note">最多${c.max}幅；只用永久画像，不消耗库存，不算食用交付。已选 ${portraits.length}/${c.max}</p></header>
      <div class="projects-portrait-grid">${c.rows.map(row=>`<label class="projects-portrait ${portraits.includes(row.key)?'is-on':''}"><input type="checkbox" data-project-portrait="${row.key}" ${portraits.includes(row.key)?'checked':''}><span aria-hidden="true">${portrait(row)}</span><small>${esc(row.name)}</small></label>`).join('')}</div>`,
      `<button data-project-back>返回</button><button class="orange" data-project-portraits-save>保存展册</button>`);
    find('[data-project-back]').onclick=()=>{view={kind:'detail',id:r.id};render();};
    all('[data-project-portrait]').forEach(b=>b.onchange=()=>{const k=b.dataset.projectPortrait;if(b.checked){if(portraits.length>=c.max){b.checked=false;alertBox(`展册最多放${c.max}幅画像。`);return;}portraits.push(k);}else portraits=portraits.filter(x=>x!==k);render();});
    find('[data-project-portraits-save]').onclick=()=>{const result=commitProgress(s=>setProjectPortraits(s,[...portraits]));if(result!==null&&result!==false)view={kind:'detail',id:r.id};render();};
  }
  return {open,refresh,resume:()=>render()};
}
