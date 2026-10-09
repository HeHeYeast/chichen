import {closeBusinessTimeline} from './timeline.js';
import {BRANCHES,SKILLS,SKILL_BY_ID,TRADE_CATEGORIES,rank,skillPoints,skillGate,skillRequirements,learnSkill,applySkillPlan,respecReason,effects,collectedTotal,discoveryCount,claimLeftovers,SPECIES_SOURCE_LIMIT,MAX_SKILL_POINTS} from './progression.js';
import {SPECIES_TRADE as TRADE_SPECIES} from './content-registry.js';
import {RULES,STORY_CHAPTERS} from './integration-data.js';
import {SPECIES_ABILITIES as ABILITIES} from './content-registry.js';
import {GAME_DATA} from './content-pack.js';
import {availableCount,reservedCount} from './inventory.js';
import {ROUTES,LEGACY_ROUTES,explorationInfo,depart,recall,claimTrip} from './exploration.js';
import {storyOrders,acceptOrder,deliverOrder} from './story-orders.js';
import {observationInfo,readObservation,studyRecipe,prepareKnownPath,ingredientName,speciesCode} from './knowledge.js';
import {recipeId} from './recipe-book.js';
const escape=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
import {skillIcon,branchEmblem} from './skill-icons.js';
import {progressTrack} from './visual-assets.js';
import {materialCapacity} from './material-capacity.js';
import {resolveSprite,spriteSVG,uiIcon} from './art/manifest.js';
import {kitTabs,kitSheet,kitButton,kitButton2,kitChip,kitChipHtml,kitBar,kitIcon,kitCell,kitLabel,kitArt} from './ui-kit.js';
const shortEffects={
 'CUL-1':'收取时，15%概率额外 +1 CP','CUL-2':'新批次减时10% · 最短6分钟','CUL-3':'收完一批，20%概率返1份普通材料','CUL-4':'30分钟内同配方接锅，额外减时5个百分点','CUL-5':'多付10 CP，每枚普通蛋20%机会转为指定伙伴','CUL-S':'基础减时20% · 接锅时25%',
 'HOME-1':'每只破壳后，保鲜延长30分钟','HOME-2':'脱逃时，每个在家品种至少留1只','HOME-3':'下次打扫后，保持干净54小时','HOME-4':'额外保鲜提高至90分钟','HOME-5':'安心等候：用时+25%，保鲜至少8小时','HOME-S':'干净72小时 · 普通病变40%→20%',
 'TRADE-1':'普通材料货款返还6%，小数自动积攒','TRADE-2':'选一个招牌类别，出售加价12%','TRADE-3':'同种家常伙伴24只，额外获得12 CP','TRADE-4':'4种普通料理各3只，额外获得8 CP','TRADE-5':'经营奖励最多存6次','TRADE-S':'招牌加价18% · 整筐18 CP／拼盘12 CP',
 'OBS-1':'看剪影、蛋种、厨具与日期条件','OBS-2':'预览最多3种只差一味的未知方向','OBS-3':'看第一味、第二味类别与非材料条件','OBS-4':'100 CP，永久研读全部获取方法','OBS-5':'首次收取新品种，再记下1条可寻线索','OBS-S':'研读50 CP · 寻访可指定优先线索',
 'TRIP-1':'每趟20%概率带回6／12／18 CP','TRIP-2':'指定第1份基础材料','TRIP-3':'每位适应伙伴：材料+3、线索+2个百分点','TRIP-4':'连续5趟无线索，下次有合格线索时必得','TRIP-5':'溪岸／林间轻装：减时20%，基础材料少1份','TRIP-S':'可指定前2份材料 · 额外材料概率+6个百分点'};
const helpCopy={skills:'发现新品种、累计收取和升级厨房都能获得手艺点。点开手艺看具体效果，加入方案后点击「应用这套手艺」才会生效。普通手艺可以跨方向学习，专精只能选一个。正在进行的批次和寻访，沿用开始时的手艺。',trip:'选一条路线，再派出1～3种在家伙伴，每种1只。每种伙伴有采集、发现和适应环境；选人时可以看到带回材料和线索的机会。到期后伙伴自动回家，材料、CP和线索等你领取。材料包满了也不会丢失已带回的材料。',story:'采购没有截止日期，可以分批交付。每次交付都按数量支付普通货款，全部交齐后再给一次酬谢。已交付的伙伴不能取回；尚未交第一只时，可以更换订单允许的出品。',observe:'线索告诉你可以尝试什么；研读可以学会完整获取方法；只有实际收取，才会正式收入图鉴。知道方法不代表已经满足条件，也不保证普通随机配方每批都有目标。'};
const pct=v=>Number((v*100).toFixed(1))+'%';
const duration=h=>Math.floor(h)+'小时'+(Math.round(h%1*60)?Math.round(h%1*60)+'分':'');
export function createWorkshopUI({getState,getNow,panels,showPanel,confirmBox,alertBox,commit,characterPortrait,goKitchen,prepareTool,openRegional=()=>{},openBusiness=()=>{},businessNote=()=>'',skillFeedback=()=>{}}){
  let tab='skills',branch='CUL',routeId='yard',members=null,directed=[],priority=null,detailKey=null,draft=null,draftSteps=[],draftReset=false,draftBase=null,light=false,category=null,skillDetail=null,tripStatus=null;
  const find=q=>panels.querySelector(q),each=(q,f)=>panels.querySelectorAll(q).forEach(f);
  const transact=fn=>commit(fn);
  const portrait=key=>characterPortrait(...key.split(':').map(Number));
  const name=key=>{const [e,id]=key.split(':').map(Number);return GAME_DATA.characters[e].find(c=>c.id===id).title_zh_CN;};
  // 厨房往事 lives under 生意簿·订单 and 日常寻访 under the 寻访 map; each page returns to where it was opened.
  let back=null;
  // A kit page per tab: 手艺 sits in the kitchen, 往事 with the business pages, 日常寻访 on the journey side.
  const SKIN={skills:'kitchen',story:'business',trip:'journey',observe:'book'};
  const ICON={skills:()=>spriteSVG(resolveSprite(uiIcon(4))),story:()=>'<img src="/web/art/golden-business/orders.png" alt="">',trip:()=>'<img src="/web/art/golden-journey/map-icon.png" alt="">',observe:()=>portrait('0:0')};
  function shell(title,body,footer='',standalone=false,{tabs='',above='',kind=tab,short,list=false,help=null,back=false}={}){
    showPanel(title,`${tabs}${kitSheet(body,footer,'workshop-sheet',above,{cls:'workshop-body',attrs:list?'data-list':''})}`,'screen-panel workshop-screen'+(standalone?' workshop-detail':''),{skin:SKIN[kind]??'kitchen',icon:ICON[kind]?.()??'',help:help??(standalone?'':`data-workshop-help`),short,back});
    if(back&&tab!=='skills'&&!standalone){const close=find('.close');if(close)close.onclick=()=>back();}
    // 生意 and 寻访 are main places (Work K); the old tabs keep their legacy content only.
    find('[data-workshop-help]')?.addEventListener('click',()=>alertBox(helpCopy[tab]));
  }
  // options.skill opens that skill's detail on its own branch (e.g. 线索册 → 配方研读).
  function open(next='skills',options={}){if(options.back!==undefined)back=options.back;else if(next==='skills')back=null;tab=next;detailKey=null;skillDetail=null;if(tab==='skills'&&SKILL_BY_ID[options.skill]){branch=SKILL_BY_ID[options.skill].branch;showSkill(options.skill);}else if(tab==='skills')skills();else if(tab==='story')story();else trip();}
  function initDraft(){if(draft&&(draftReset||draftSteps.length))return;draft=structuredClone(getState());draftSteps=[];draftReset=false;draftBase=JSON.stringify(getState().progress.skills);category=getState().progress.trade.category;}
  function skills(){
    initDraft();skillDetail=null;
    const s=draft,p=skillPoints(s),actual=getState(),e=effects(actual),H=collectedTotal(s),D=discoveryCount(s),changed=draftReset||draftSteps.length;
    const defs=SKILLS.filter(n=>n.branch===branch);
    // A skill tree: the branch's six skills hang in four tiers (1·2, 3·4, 5, then the 专精 crown), joined by
    // painted arrows. Each skill is its painted picture on a medal; a ＋ coin puts it in the plan, a tick or a lock
    // tells the rest. A locked tier shows its first missing condition on the arrow above it.
    const node=n=>{
      const learned=!!rank(actual,n.id)&&!draftReset,chosen=!!rank(s,n.id),reason=skillGate(s,n.id),short=p.available<n.cost;
      const state=chosen?(learned?'is-learned':'is-selected'):reason?'is-locked':short?'is-short':'is-open';
      const label=`${n.name}，${chosen?(learned?'已学会':'已加入方案'):reason?'尚未解锁':short?'还差'+(n.cost-p.available)+'点':'可学习'}，${n.cost} 点`;
      const badge=chosen?kitArt(learned?'ic-check':'sparkles','ws-badge'+(learned?'':' new')):reason?'<img class="ws-badge lock" src="/web/art/golden-journey/lock.png" alt="">':'';
      const plus=!chosen&&!reason?`<button type="button" class="ws-learn" data-learn="${n.id}" aria-label="把${escape(n.name)}加入方案"${short?' disabled':''}>${kitArt('btn-plus')}</button>`:'';
      return `<div class="ws-node ${state}${n.node==='S'?' is-crown':''}"><button type="button" class="skill-detail-link ws-medal" data-skill-detail="${n.id}" aria-label="${escape(label)}"><span class="skill-emblem" aria-hidden="true">${skillIcon(n.id)}</span>${badge}</button>${plus}<b class="ws-name">${escape(n.name)}<span class="ws-cost">${kitIcon.point}${n.cost}</span></b><small class="ws-effect">${chosen&&!learned?'待应用 · ':''}${escape(shortEffects[n.id])}</small></div>`;
    };
    const tiers=[['1','2'],['3','4'],['5'],['S']].map(t=>defs.filter(n=>t.includes(n.node)));
    const gate=t=>{if(t.some(n=>rank(s,n.id)||!skillGate(s,n.id)))return '';const miss=skillRequirements(s,t[0].id).find(r=>!r.met);return miss?`<span class="ws-gate"><img src="/web/art/golden-journey/lock.png" alt="">${escape(miss.text)}</span>`:'';};
    const tree=`<div class="ws-tree">${tiers.map((t,i)=>`${i?`<div class="ws-step" data-row>${kitArt('arrow','ws-arrow')}${gate(t)}</div>`:''}<div class="ws-tier n${t.length}">${t.map(node).join('')}</div>`).join('')}</div>`;
    const spent=draftSteps.reduce((v,id)=>v+SKILL_BY_ID[id].cost,0);
    const branches=`<nav class="ws-branches" role="tablist" aria-label="手艺方向">${Object.entries(BRANCHES).map(([id,label])=>{const all=SKILLS.filter(n=>n.branch===id),got=all.filter(n=>rank(s,n.id)).length;return `<button type="button" role="tab" class="ws-branch" data-branch="${id}" aria-selected="${branch===id}" aria-label="${label}，已学 ${got}/${all.length}"><span class="ws-branch-art">${branchEmblem(id)}<b>${got}/${all.length}</b></span><span class="ws-branch-name">${label}</span></button>`;}).join('')}</nav>`;
    const purse=`<div class="ws-ledger" data-row><span class="ws-purse">${kitIcon.point}<b>${p.available}</b><span class="ws-purse-text"><small>点可用</small><small class="ws-next">${D<SPECIES_SOURCE_LIMIT?'再发现 '+(5-D%5)+' 种 +1 点':'收取、升级厨房得点'}</small></span></span>${kitButton2('来源','data-point-help')}</div>`;
    const sticky=actual.progress.migrationNotice?`<button type="button" class="ws-sticky" data-migration-note><span data-safe><b>手艺更新</b><span>旧点数已全部退还</span><span>点一下看详情</span></span></button>`:'';
    const why=respecReason(actual,getNow());
    const now=`${kitLabel('现在生效')}<div class="ws-effects">${[[kitIcon.hourglass,`${Math.round(e.reduction*100)}%`,'减时'],[kitIcon.fresh,`+${e.freshMinutes}`,'保鲜分钟'],[kitIcon.broom,`${actual.cleanCycle.hours}`,e.cleanHours!==actual.cleanCycle.hours?`干净小时 · 下次 ${e.cleanHours}`:'干净小时'],[kitIcon.gift,`${actual.progress.trade.credits}/${e.capacity}`,'经营奖励'],[kitIcon.coin,`${e.rebate}%`,actual.progress.trade.rebateRemainder?`返利 · 攒 ${(actual.progress.trade.rebateRemainder/100).toFixed(2)}`:'采购返利']].map(([icon,v,l])=>`<span class="ws-effect-cell"><i>${icon}</i><b>${v}</b><small>${escape(l)}</small></span>`).join('')}</div>
      <div class="ws-respec" data-row>${kitButton2('免费重配','data-respec'+(why?' disabled':''))}${actual.expansion?.business?.active?kitButton2('收摊后重配','data-respec-close-business'):''}</div><p class="ws-respec-note">${escape(why||'收完这批、队伍归队后可免费重配；之后每 72 小时一次。')}</p>`;
    const leftovers=actual.progress.leftovers.length?`<div class="ws-leftovers" data-row>${kitArt('crate-empty')}<span><b>待收余料 ${actual.progress.leftovers.length}/5 份</b><small>材料包满时继续为你保留</small></span>${kitButton2('收下','data-leftovers')}</div>`:'';
    const trade=rank(s,'TRADE-2')&&branch==='TRADE'?(()=>{const keys=Object.keys(TRADE_SPECIES).filter(k=>TRADE_SPECIES[k].category===category),have=keys.filter(k=>(s.total[k]??0)>0||(s.farm[k]??0)>0);
      return `${kitLabel('小店的招牌')}<div class="ws-cats" role="radiogroup" aria-label="招牌类别">${TRADE_CATEGORIES.map(c=>`<button type="button" class="ws-cat" role="radio" data-category="${c}" aria-checked="${category===c}" aria-pressed="${category===c}" ${rank(actual,'TRADE-2')&&!draftReset?'disabled':''}>${c}</button>`).join('')}</div>${category?`<div class="ws-cat-who" aria-label="${category}的合格伙伴">${have.slice(0,12).map(k=>`<span title="${escape(name(k))}">${portrait(k)}</span>`).join('')}${have.length>12?`<b>+${have.length-12}</b>`:''}</div><p class="ws-respec-note">还有 ${keys.length-have.length} 种没收录 · 重配时才能换招牌</p>`:'<p class="ws-respec-note">选一类，出售时加价</p>'}`;})():'';
    shell('厨房手艺',`${branches}${purse}${H<24?`<div class="workshop-empty">${portrait('0:0')}<h3>第一批，也是第一份本领</h3><p>再收取 ${24-H} 只，开启手艺。<br>完成第一批就能获得2点。</p><button data-workshop-home>回厨房收取</button></div>`:''}${sticky}${tree}${trade}${leftovers}${now}`,`${kitButton2('放弃','data-plan-cancel'+(changed?'':' disabled'))}${kitButton('应用','data-plan-apply'+(changed?'':' disabled'))}`,false,{above:changed?`<span class="draft-status sr-only" role="status">试配中，效果尚未生效</span>${kitChip('',draftReset?`重新分配 ${spent} 点`:`花 ${spent} 点`,'mini')}${kitChip('',`剩 ${p.available} 点`,'mini')}`:''});
    find('[data-migration-note]')?.addEventListener('click',()=>{transact(s=>{s.progress.migrationNotice=false;});alertBox('现在共有30个单级手艺，旧点数已全部退还。旧配方知识、经营奖励、订单与库存保留；正在进行的批次和清洁周期不变。采购返利现为6%，料理基础减时现为10%／20%。赠送一次不受冷却限制的重配。');skills();});
    each('[data-branch]',b=>b.onclick=()=>{branch=b.dataset.branch;skills();});
    each('[data-skill-detail]',b=>b.onclick=()=>showSkill(b.dataset.skillDetail));
    each('[data-learn]',b=>b.onclick=()=>addSkill(b.dataset.learn));
    each('[data-point-help]',b=>b.onclick=()=>alertBox((discoveryCount(getState())<SPECIES_SOURCE_LIMIT?'再发现'+(5-discoveryCount(getState())%5)+'种伙伴，获得1点。\n':'')+'首次收取24只：2点。每发现5种：1点。每次厨房升级：2点。累计收取120、500、2,000、5,000只：各2点。当前内容最多'+MAX_SKILL_POINTS+'点。'));
    each('[data-category]',b=>b.onclick=()=>{category=b.dataset.category;skills();});
    find('[data-plan-cancel]').onclick=()=>{draft=null;skills();};
    find('[data-plan-apply]').onclick=()=>{const learned=draftSteps.map(id=>SKILL_BY_ID[id].name);if(transact(s=>applySkillPlan(s,{steps:draftSteps,reset:draftReset,category,base:draftBase},getNow()))){draft=null;skills();alertBox('已应用这套手艺'+(learned.length?'：'+learned.join('、'):'')+'。新批次与寻访从下次开始生效，清洁从下次打扫生效；查看与出售类立即生效。');}};
    find('[data-respec]')?.addEventListener('click',()=>{draft=structuredClone(getState());draft.progress.skills={};draftSteps=[];draftReset=true;category=null;skills();});
    // Respec never rewrites a running business snapshot: close first (settling past windows), then draft.
    find('[data-respec-close-business]')?.addEventListener('click',()=>confirmBox('先按已经过去的接待窗口结算，并收摊释放未售备货与未用经营奖励；已成交的货款不变。随后进入手艺重新分配。',()=>{if(transact(s=>closeBusinessTimeline(s,getNow()))){draft=structuredClone(getState());draft.progress.skills={};draftSteps=[];draftReset=true;category=null;skills();}},false,{yes:'收摊并继续'}));
    find('[data-leftovers]')?.addEventListener('click',()=>{const r=transact(s=>claimLeftovers(s));draft=null;skills();alertBox(r?.length?'已收下 '+r.length+' 份余料。':'材料包已满。先去厨房使用材料，余料会继续保留。');});
    find('[data-workshop-home]')?.addEventListener('click',goKitchen);
  }
  function addSkill(id){try{learnSkill(draft,id);draftSteps.push(id);skills();const card=find(`[data-skill-detail="${id}"]`)?.closest(".ws-node");card?.classList.add("skill-just-selected");card?.scrollIntoView({block:"nearest"});}catch(e){alertBox(e.message);}}
  function showSkill(id){
    initDraft();skillDetail=id;const n=SKILL_BY_ID[id],p=skillPoints(draft),reason=skillGate(draft,id),chosen=rank(draft,id),extra=id==='TRADE-3'?`<details><summary>12种家常伙伴</summary><div class="category-portraits">${RULES.trade.eligibleSpecies.map(k=>`<span>${(getState().total[k]??0)>0?portrait(k):'<b>?</b>'}${(getState().total[k]??0)>0?escape(name(k)):speciesCode(k)}</span>`).join('')}</div></details>`:'';
    // The detail is a drawer over the skills list (the list stays underneath, nothing else moves).
    skills();skillDetail=id;
    const body=`<div class="skill-detail-hero gd-head" data-row><span class="gd-face">${skillIcon(n.id)}</span><div class="gd-row">${kitChip('',BRANCHES[n.branch]+(n.node==='S'?' · 专精':''),'soft')}${kitChip('',`${n.cost} 点`,'soft')}</div></div><div class="gd-label">学习后</div><p class="skill-full-effect">${escape(n.description)}</p>${extra}${['TRADE-3','TRADE-4'].includes(id)?'<p class="workshop-note">每新收取24只获得1次经营奖励，最多3次；首次学习共赠1次。整筐优先，再用剩余数量组拼盘，不能重复计数。</p>':''}<div class="gd-label">何时生效</div><p class="skill-when">${n.when}，应用后生效</p><div class="gd-label">解锁条件</div><div class="sh-conds skill-requirements">${skillRequirements(draft,id).map((r,i)=>`<span class="sh-cond${r.met?' met':''}"><i>${r.met?'<img src="/web/art/golden-business/family-check.png" alt="已达成">':i+1}</i>${escape(r.text)}</span>`).join('')}</div>${p.available<n.cost&&!chosen?`<span class="gd-alert shortage">还差 ${n.cost-p.available} 点</span>`:''}<div class="gd-actions" data-row>${kitButton2('返回','data-skill-back')}${kitButton(chosen?'已加入':'加入','data-detail-learn'+(chosen||reason||p.available<n.cost?' disabled':''))}</div>`;
    find('.workshop-screen').insertAdjacentHTML('beforeend',`<div class="kp-scrim" data-skill-back></div><section class="kp-drawer gd ws-drawer workshop-detail" role="dialog" aria-modal="true" aria-label="${escape(n.name)}"><div class="kp-drawer-title" data-art="plank"><span data-safe><b>${escape(n.name)}</b></span></div><div class="kp-drawer-body">${body}</div></section>`);
    each('[data-skill-back]',b=>b.onclick=()=>skills());find('[data-detail-learn]').onclick=()=>addSkill(id);find('.ws-drawer [data-detail-learn]:not(:disabled),.ws-drawer [data-skill-back].gd-btn2')?.focus({preventScroll:true});
  }
  // 厨房往事: the open chapter as one card (partner, progress, money, a plank button); earlier chapters as rows.
  // The amount to hand over comes from quick picks or the slider, never typing.
  let storyAmount={},storyChoice={};
  const chosen=o=>o.accepted?(o.choice??o.choices[0].species):(storyChoice[o.id]??o.choice??o.choices[0].species);
  function story(){
    const orders=storyOrders(getState());
    const current=orders.find(o=>!o.completed);
    const card=o=>{
      const c=o.choices.find(c=>c.species===chosen(o)),left=c?c.count-o.delivered:0,top=c?Math.max(0,Math.min(c.atHome,left)):0;
      if(!o.unlocked)return `<article class="od-card story-card"><div class="od-title"><span class="story-eyebrow">第 ${o.index+1} 章</span><h3>${escape(o.chapter.title.split('：').at(-1))}</h3></div><span class="gd-note">${o.requirements.filter(r=>!r.met).map(r=>escape(r.text)).join(' · ')}</span></article>`;
      const n=Math.max(top?1:0,Math.min(storyAmount[o.id]??top,top));
      const quick=[...new Map([[1,'1 只'],[Math.max(1,Math.round(top/2)),'一半'],[top,'全部']].filter(([v])=>v>0&&v<=top)).entries()];
      const choices=o.choices.length>1?`<div class="kp-grid story-choices" role="radiogroup" aria-label="交哪种">${o.choices.map(x=>{const on=chosen(o)===x.species;return kitCell({pic:portrait(x.species),name:x.name,count:`×${x.count}`,on,attrs:`role="radio" aria-checked="${on}" data-order-choice="${o.id}" data-value="${x.species}"${!x.available||o.delivered>0?' disabled':''}`,label:`${x.name} ×${x.count}，${x.available?'在家 '+x.atHome:'需先持有竹蒸笼'}`});}).join('')}</div>`:'';
      const deliver=o.accepted?`<div class="gd-qty" data-row><button type="button" class="gd-round minus" data-story-step="-1" data-order="${o.id}" aria-label="少交1只" ${n>1?'':'disabled'}></button><output class="gd-big" aria-live="polite" data-story-count="${o.id}">${n}<small>只</small></output><button type="button" class="gd-round plus" data-story-step="1" data-order="${o.id}" aria-label="多交1只" ${n<top?'':'disabled'}></button></div>
        ${top>1?`<label class="gd-slider"><span class="sr-only">拖动选择数量</span><input type="range" min="1" max="${top}" step="1" value="${n}" data-story-range="${o.id}" style="--fill:${top>1?(n-1)/(top-1)*100:100}%"></label>`:''}
        ${quick.length?`<div class="gd-coins" data-row>${quick.map(([v,l])=>`<button type="button" class="gd-coinwrap${v===n?' on':''}" data-story-set="${v}" data-order="${o.id}" aria-pressed="${v===n}"><span class="gd-coin">${v}</span><b>${l}</b></button>`).join('')}</div>`:''}
        <div class="gd-actions" data-row>${kitButton(c.atHome?'交付':'不够了',`data-deliver="${o.id}"${c.atHome?'':' disabled'}`)}</div>`
        :`<div class="gd-actions" data-row>${kitButton('接下','data-accept="'+o.id+'"')}</div>`;
      return `<article class="od-card story-card current-order"><div class="od-title"><span class="story-eyebrow">第 ${o.index+1} 章${o.accepted?' · 已接下':''}</span><h3>${escape(o.chapter.title.split('：').at(-1))}</h3></div>
        <div class="gd-head" data-row><span class="gd-face">${portrait(c.species)}</span><div class="gd-row">${kitChip('',`${c.name} ×${c.count}`,'soft')}${kitChip('',`在家 ${c.atHome}`,'soft')}</div></div>
        <div class="od-group" data-row><b class="od-glabel">已交</b>${kitBar(c.count?o.delivered/c.count*100:0,'交付进度')}<b class="od-gn">${o.delivered}/${c.count}</b></div>
        <div class="gd-row gd-wrap">${kitChipHtml(`${kitIcon.coin}${c.price}/只`,'mini')}${kitChipHtml(`交齐 +${o.extraCP.toLocaleString()}`,'mini')}</div>
        ${choices}${o.id==='tea-party'?'<span class="gd-note">鸡蛋＋水煮锅＋乌龙茶叶，可能出茶叶蛋鸡</span>':''}${deliver}</article>`;
    };
    const done=orders.filter(o=>o.completed);
    const history=done.length?`${kitLabel('读过的往事')}<div class="story-history">${done.map(o=>`<button type="button" class="story-row" data-story-read="${o.index}"><b>第 ${o.index+1} 章</b><span>${escape(o.chapter.title.split('：').at(-1))}</span><i>重读 ›</i></button>`).join('')}</div>`:'';
    const later=orders.filter(o=>!o.completed&&o!==current);
    shell('厨房往事',`${current?card(current):`<span class="gd-note">三章都读完了</span>`}${history}${later.length?`${kitLabel('之后')}${later.map(card).join('')}`:''}`,'',false,{kind:'story'});
    each('[data-accept]',b=>b.onclick=()=>{const id=b.dataset.accept,o=storyOrders(getState()).find(x=>x.id===id);if(transact(s=>acceptOrder(s,id,chosen(o))))story();});
    each('[data-order-choice]',b=>b.onclick=()=>{const id=b.dataset.orderChoice,o=storyOrders(getState()).find(x=>x.id===id);if(o.accepted){if(transact(s=>acceptOrder(s,id,b.dataset.value)))story();}else{storyChoice[id]=b.dataset.value;story();}});
    each('[data-story-step]',b=>b.onclick=()=>{const id=b.dataset.order;storyAmount[id]=(Number(find(`[data-story-count="${id}"]`).firstChild.textContent)||1)+Number(b.dataset.storyStep);story();});
    each('[data-story-set]',b=>b.onclick=()=>{storyAmount[b.dataset.order]=Number(b.dataset.storySet);story();});
    each('[data-story-range]',r=>{r.oninput=()=>{const max=Number(r.max);r.style.setProperty('--fill',(max>1?(r.value-1)/(max-1)*100:100)+'%');find(`[data-story-count="${r.dataset.storyRange}"]`).firstChild.textContent=r.value;};r.onchange=()=>{storyAmount[r.dataset.storyRange]=Number(r.value);story();};});
    each('[data-deliver]',b=>b.onclick=()=>{
      const id=b.dataset.deliver,o=storyOrders(getState()).find(o=>o.id===id),c=o.choices.find(c=>c.species===o.choice),n=Number(find(`[data-story-count="${id}"]`).firstChild.textContent),empty=n===c.atHome;
      if(!Number.isInteger(n)||n<1||n>c.atHome||n>c.count-o.delivered){alertBox('先选好交多少');return;}
      confirmBox(`交 ${n} 只${c.name}\n货款 ${n*c.price} CP，进度 ${n+o.delivered}/${c.count}${n+o.delivered===c.count?`，交齐另得 ${o.extraCP} CP`:''}${empty?'\n家里不会再有这种伙伴，图鉴还在':''}`,()=>{const r=transact(s=>deliverOrder(s,id,n,getNow()));if(r){storyAmount={};if(r.complete)readChapter(o.index);else story();}},false,{title:'交付',yes:'交付'});
    });
    each('[data-story-read]',b=>b.onclick=()=>readChapter(+b.dataset.storyRead));
  }
  function readChapter(index){const c=STORY_CHAPTERS[index];shell(c.title,`<h3 class="story-title">${escape(c.title)}</h3><article class="story-prose">${c.paragraphs.map(p=>`<p>${escape(p)}</p>`).join('')}</article>`,kitButton('读完了','data-story-back'),false,{kind:'story',short:'往事'});find('[data-story-back]').onclick=story;}
  // A saved team may bring one kind several times, but never more than are at home now.
  const fitTeam=(st,list)=>{const used={};return list.filter(k=>(used[k]=(used[k]??0)+1)<=availableCount(st,k)).slice(0,3);};
  function trip(){
    // 近郊 starts with a team: the last one if they are home, else the best gatherers suited to this route.
    if(members===null){const st=getState(),room=k=>availableCount(st,k)>0;members=fitTeam(st,st.progress.lastTeam??[]);
      if(!members.length){const score=k=>(ABILITIES[k]?.environment===routeId?100:0)+(ABILITIES[k]?.gather??0)*10;members=Object.keys(st.farm).filter(k=>ABILITIES[k]&&(st.total[k]??0)>0&&room(k)).sort((a,b)=>score(b)-score(a)).slice(0,3);}}
    detailKey=null;skillDetail=null;tripStatus=getState().progress.trip?.status;
    const s=getState(),t=s.progress.trip,room=materialCapacity(s)-Object.values(s.ingredients).reduce((a,b)=>a+b,0);
    if(t&&['running','returned'].includes(t.status)){
      const route=ROUTES.find(r=>r.id===t.routeId),running=t.status==='running',minutes=Math.max(0,Math.ceil((t.endAt-getNow())/60000));
      const groups=Object.entries(t.remaining.reduce((a,id)=>(a[id]=(a[id]??0)+1,a),{}));
      shell('日常寻访',`<div class="trip-landscape ${route.id}"><span>${running?'沿着香气，慢慢走':'带着收获，回家了'}</span><h3>${route.name} · ${running?'正在寻访':'伙伴已回家'}</h3><div class="trip-team">${t.members.map(k=>`<div>${portrait(k)}<strong>${escape(name(k))}</strong></div>`).join('')}</div></div>
        ${running?`<div class="gd-row">${kitChipHtml(`${kitIcon.clock}<span data-trip-countdown>还需 ${Math.floor(minutes/60)}小时${minutes%60}分</span>`)}</div><details class="recall-menu gd-more"><summary>行程管理</summary><p>提前召回会空手归来</p>${kitButton2('提前召回','data-recall')}</details>`
        :`<div class="trip-return">${kitLabel('这一趟的收获')}<div class="gd-row gd-wrap material-chips">${groups.map(([id,n])=>kitChip('',`${ingredientName(Number(id))} ×${n}`,'mini')).join('')||kitChip('','材料已收好','mini')}</div>
          <div class="gd-row gd-wrap">${t.cpProcessed?'':kitChipHtml(`${kitIcon.coin}+${t.cpReward??0}`,'mini')}${kitChip('',`材料包 ${materialCapacity(s)-room}/${materialCapacity(s)}`,'mini'+(room?'':' hot'))}${t.clueResult?kitButton2('读线索',`data-trip-clue="${t.clueResult.key}"`):''}</div>
          ${!room&&t.remaining.length?'<span class="gd-alert shortage">材料包满了，先领 CP 与线索</span>':''}<details class="gd-more"><summary>整理篮子</summary>${kitButton2('放弃余料','data-discard-trip')}</details></div>`}`,
        running?kitButton('回厨房','data-trip-home'):`${kitButton2('先领CP','data-claim-clue')}${room?kitButton(`领取 ${Math.min(room,t.remaining.length)} 份`,'data-claim-trip'):kitButton('用材料','data-trip-home')}`);
      find('[data-trip-home]')?.addEventListener('click',goKitchen);
      find('[data-recall]')?.addEventListener('click',()=>confirmBox('现在召回会立即归队，但这趟没有材料、CP或线索奖励，也不计入线索保底。',()=>{if(transact(s=>recall(s,t.id,getNow())))trip();},false,{yes:'召回'}));
      find('[data-claim-trip]')?.addEventListener('click',()=>{const r=transact(s=>claimTrip(s,t.id,{},getNow()));if(r){trip();alertBox(`已收下 ${r.materials.length} 份材料${r.cp?'、'+r.cp+' CP':''}。${r.clue?'另记下1条新线索。':''}${r.remaining?'篮中还剩 '+r.remaining+' 份，腾出空位后可继续领取。':''}`);if(r.cp)skillFeedback([{id:'TRIP-1',text:'这趟额外 +'+r.cp+' CP'}]);}});
      find('[data-claim-clue]')?.addEventListener('click',()=>{const r=transact(s=>claimTrip(s,t.id,{materials:false},getNow()));if(r){trip();alertBox(`${r.cp?'获得 '+r.cp+' CP。':'CP已结算。'}${r.clue?'已记下 '+speciesCode(r.clue.key)+' 的新线索。':'本次没有新的线索。'}材料继续保留在篮中。`);if(r.cp)skillFeedback([{id:'TRIP-1',text:'这趟额外 +'+r.cp+' CP'}]);}});
      find('[data-discard-trip]')?.addEventListener('click',()=>confirmBox('放弃篮中全部剩余材料，不会换成CP。未领的CP和线索仍会结算。',()=>{if(transact(s=>claimTrip(s,t.id,{materials:false,discard:true},getNow())))trip();},false,{yes:'放弃余料'}));
      find('[data-trip-clue]')?.addEventListener('click',e=>observe(e.currentTarget.dataset.tripClue,trip));return;
    }
    members=members.filter(k=>availableCount(s,k)>0);
    const info=explorationInfo(s,routeId,members,getNow(),{light});light=info.light;directed=directed.slice(0,info.directedUnits);
    const pips=n=>`<span class="jp-pips" aria-hidden="true"><span class="jp-bar"><span style="--w:${Math.round(Math.min(20,n)/20*100)}%"></span></span><span class="jp-n">${n}</span></span>`;
    const routes=`<div class="route-tabs near-routes" role="group" aria-label="路线">${LEGACY_ROUTES.map(r=>`<button type="button" class="near-route" data-route="${r.id}" aria-pressed="${routeId===r.id}"><img class="legacy-route-object" src="/web/art/golden-journey/environment-${r.id}.png" alt=""><b>${r.name}</b><small>${duration(r.hours)}</small><small>${r.baseUnits}份起</small></button>`).join('')}</div>`;
    const slots=`<div class="trip-slots near-slots">${[0,1,2].map(x=>{const k=members[x];return `<button type="button" class="near-slot" data-member-slot="${x}" aria-label="${k?'更换'+name(k):'选择同行伙伴'+(x+1)}"><span class="near-seat">${k?portrait(k):'<span class="empty-slot">＋</span>'}</span><strong>${k?escape(name(k)):'选伙伴'}</strong>${k?`<span class="jp-stat"><i>采</i>${pips(ABILITIES[k].gather)}</span><span class="jp-stat"><i>发</i>${pips(ABILITIES[k].discover)}</span>${ABILITIES[k].environment===routeId?'<em class="near-fit">适应</em>':''}`:'<small>空位</small>'}</button>`;}).join('')}</div>`;
    const outlook=`<div class="trip-outlook near-outlook"><div class="near-yield"><b class="gd-big">${info.minUnits}<small>份保证</small></b><div class="gd-row gd-wrap">${kitChip('',`${pct(info.materialChance)} 多 1 份`,'mini')}${kitChip('',info.clues.length?`${pct(info.clueChance)} 新线索`:'暂无线索','mini')}${info.cpReward?kitChip('',`20% +${info.cpReward} CP`,'mini'):''}</div></div><details class="gd-more"><summary>采集 ${info.G} · 发现 ${info.F} · 适应 ${info.A} 位</summary><p>基础材料5%＋采集每点2.5%＋适应每位4%${rank(s,'TRIP-3')?'＋因地制宜每位3%':''}${rank(s,'TRIP-S')?'＋熟途采集6%':''}，最高60%。<br>线索10%＋发现每点2%＋适应每位3%${rank(s,'TRIP-3')?'＋因地制宜每位2%':''}，最高55%。</p></details></div>`;
    const pool=info.pool.filter(p=>p.unlocked);
    const prefs=`<details class="trip-preferences gd-more"><summary>路线材料与偏好</summary>${rank(s,'TRIP-5')&&routeId!=='yard'?`<button type="button" class="gd-toggle" role="checkbox" aria-checked="${light}" aria-pressed="${light}" data-light><i>${light?'✓':''}</i>轻装折返 · ${duration(info.route.hours*.8)}／${info.route.baseUnits-1}份</button>`:''}<div class="gd-row gd-wrap">${info.pool.map(p=>kitChip('',p.unlocked?ingredientName(p.id):'未解锁','mini soft')).join('')}</div>${Array.from({length:info.directedUnits},(_,x)=>`<div class="near-directed" role="group" aria-label="指定第${x+1}份"><b>第${x+1}份</b>${[['','随机'],...pool.map(p=>[String(p.id),ingredientName(p.id)])].map(([v,l])=>`<button type="button" class="gd-btn2 mini" data-directed="${x}" data-value="${v}" aria-pressed="${String(directed[x]??'')===v}">${escape(l)}</button>`).join('')}</div>`).join('')}<p>${info.clues.length?`线索保底 ${s.progress.routeFailures[routeId]}/${info.hardAttempt-1} · 连续${info.hardAttempt-1}趟未获新线索，下次必得`:'没有合格线索时，不累计保底。'}</p>${rank(s,'OBS-S')&&info.clues.length?`<div class="near-directed" role="group" aria-label="优先寻找（不提高概率）"><b>优先寻找（不提高概率）</b><button type="button" class="gd-btn2 mini" data-priority="" aria-pressed="${!priority}">顺路发现</button>${info.clues.slice(0,3).map(c=>`<button type="button" class="gd-btn2 mini" data-priority="${c.key}" aria-pressed="${priority===c.key}">${speciesCode(c.key)}</button>`).join('')}</div>`:''}</details>`;
    shell('去哪里寻味？',`${routes}${!info.unlocked?`<span class="gd-alert">还没开放：收取 ${Math.min(collectedTotal(s),info.route.requiredCollected)}/${info.route.requiredCollected} · 发现 ${Math.min(discoveryCount(s),info.route.requiredDiscoveries)}/${info.route.requiredDiscoveries}</span>`:''}<div class="kp-bar near-team-head" data-row>${kitLabel('同行')}${kitButton2('上次队伍','data-last-team')}</div>${slots}${outlook}${prefs}`,`${kitChipHtml(`${kitIcon.clock}${duration(info.hours)}`,'mini')}${kitButton(!info.unlocked?'未开放':!members.length?'先选伙伴':'出发','data-depart'+(!members.length||!info.unlocked?' disabled':''))}`,false,{kind:'trip',short:'近郊'});
    each('[data-route]',b=>b.onclick=()=>{routeId=b.dataset.route;directed=[];priority=null;trip();});
    each('[data-member-slot]',b=>b.onclick=()=>pickMember(+b.dataset.memberSlot));
    find('[data-last-team]').onclick=()=>{members=fitTeam(s,s.progress.lastTeam);trip();};
    find('[data-light]')?.addEventListener('click',()=>{light=!light;trip();});
    each('[data-directed]',b=>b.onclick=()=>{const x=Number(b.dataset.directed),v=b.dataset.value;const next=[...directed];if(v==='')next.length=Math.min(next.length,x);else next[x]=Number(v);directed=next.filter((d,n)=>d!=null&&next.slice(0,n).every(z=>z!=null));trip();});
    each('[data-priority]',b=>b.onclick=()=>{priority=b.dataset.priority||null;trip();});
    find('[data-depart]').onclick=()=>{
      const selected=[...members],options={routeId,members:selected,directed:[...directed],priority,light},unique=[...new Set(selected)].filter(k=>availableCount(getState(),k)===selected.filter(m=>m===k).length);
      confirmBox(`${info.route.name} · ${duration(info.hours)}\n${selected.map(name).join('、')}\n保证 ${info.minUnits} 份，免费出发${unique.length?'\n'+unique.map(name).join('、')+'会全部出门，农场里暂时看不到，回来就回到农场':''}`,()=>{if(transact(s=>depart(s,options,getNow())))trip();},false,{title:'出发',yes:'出发',no:'再看看'});
    };
  }
  function pickMember(slot){
    const s=getState(),usedElsewhere=k=>members.filter((m,i)=>m===k&&i!==slot).length,available=Object.keys(ABILITIES).filter(k=>availableCount(s,k)>usedElsewhere(k));
    const before=explorationInfo(s,routeId,members,getNow(),{light});
    const pips=n=>`<span class="jp-pips" aria-hidden="true"><span class="jp-bar"><span style="--w:${Math.round(Math.min(20,n)/20*100)}%"></span></span><span class="jp-n">${n}</span></span>`;
    shell('选择同行伙伴',`<div class="gd-row gd-wrap">${kitChip('',`材料 ${pct(before.materialChance)}`,'mini')}${before.clues.length?kitChip('',`线索 ${pct(before.clueChance)}`,'mini'):''}</div><div class="near-pick-grid member-list">${available.map(k=>{
      const next=[...members];next[slot]=k;const after=explorationInfo(s,routeId,next.filter(Boolean),getNow(),{light}),a=ABILITIES[k],gain=Math.round((after.materialChance-before.materialChance)*1000)/10;
      return `<button type="button" class="near-pick" data-pick-member="${k}" aria-pressed="${members[slot]===k}" aria-label="${escape(name(k))}，在家${availableCount(s,k)}，采集${a.gather}，发现${a.discover}">${a.environment===routeId?'<em class="near-fit">适应</em>':''}<span class="near-seat">${portrait(k)}</span><strong>${escape(name(k))}</strong><span class="jp-stat"><i>采</i>${pips(a.gather)}</span><span class="jp-stat"><i>发</i>${pips(a.discover)}</span><small>${gain>0?`材料 +${gain}%`:gain<0?`材料 ${gain}%`:`在家 ${availableCount(s,k)-usedElsewhere(k)}`}</small></button>`;
    }).join('')}</div>${!available.length?`<div class="workshop-empty"><span class="gd-note">家里暂时没有能同行的伙伴</span><div class="gd-row">${kitButton2('回厨房','data-pick-home')}</div></div>`:''}`,`${kitButton2('返回','data-pick-back')}${members[slot]?kitButton2('空出这位','data-remove-member'):''}`,true,{kind:'trip',short:'选同行',list:true});
    each('[data-pick-member]',b=>b.onclick=()=>{members[slot]=b.dataset.pickMember;members=members.filter(Boolean);trip();});
    find('[data-pick-back]').onclick=trip;find('.close').onclick=trip;
    find('[data-remove-member]')?.addEventListener('click',()=>{members.splice(slot,1);trip();});
    find('[data-pick-home]')?.addEventListener('click',goKitchen);
  }
  function observe(key,back=()=>open()){
    const s=getState(),info=observationInfo(s,key,getNow());if(!info)return;
    // Only an explicit detail request records the facts actually opened here.
    if(!transact(s=>readObservation(s,key,getNow())))return;
    detailKey=key;
    const art=info.known?portrait(key):info.silhouette?`<span class="unknown-silhouette" aria-hidden="true">${portrait(key)}</span>`:'<span class="observation-unknown" aria-hidden="true">?</span>';
    // 风味观察 is a 图鉴 page: the silhouette sticker, the clue on a note, what is known as rows, then paths.
    const tick=met=>met?'<img src="/web/art/golden-business/family-check.png" alt="已达成">':'';
    const paths=info.full?info.paths.map(r=>`<article class="observed-path bk-path-card"><div class="gd-row">${kitChip('',`${r.toolName}${r.special?'':' Lv.'+(r.minLevel+1)}`,'mini')}${r.special?'':kitChip('',r.ingredientNames.join(' ＋ ')||'不放调味料','mini soft')}</div><div class="sh-conds">${r.conditions.map((c,n)=>`<span class="sh-cond${c.met?' met':''}"><i>${tick(c.met)||n+1}</i>${escape(c.label)}</span>`).join('')}</div>${r.kind==='seasonal'&&!info.known?'<span class="gd-note">首次仍按偶遇规则</span>':''}${!r.special?`<div class="gd-row">${kitButton2('配好下一批',`data-prepare-path="${escape(recipeId(r))}"${r.ready?'':' disabled'}`)}</div>`:''}</article>`).join(''):'';
    const study=!info.full&&info.canStudy?`<div class="gd-actions" data-row>${kitButton('研读',`data-study="${key}"`)}${kitChipHtml(`${kitIcon.coin}${info.studyCost}`,'mini'+(s.cp<info.studyCost?' hot':''))}</div>`:'';
    shell('风味观察',`<div class="observation-hero bk-profile-top"><span class="bk-sticker-art big ${info.known?'':'is-unknown'}">${art}</span><div class="bk-profile-name"><span class="bk-code">${info.code}</span><h3 class="species-name">${info.known?escape(info.name):'尚未收录'}</h3>${kitChip('',info.full?'方法已知':info.silhouette?'知道一点':'还不认识','mini')}</div></div>
      <div class="observation-clue bk-clue"><img src="/web/art/golden-journey/note.png" alt=""><p>${escape(info.clue)}</p></div>
      ${info.details.length?`<div class="bk-facts">${info.details.map(d=>`<span class="bk-fact">${escape(d)}</span>`).join('')}</div>`:''}${paths}${study}${!info.full&&!info.canStudy?'<span class="gd-note">学会「配方研读」后可以花 CP 读完整方法</span>':''}`,kitButton('返回','data-observe-back'),true,{kind:'observe',help:'data-observe-help',back:true});
    find('[data-observe-help]').onclick=()=>alertBox(helpCopy.observe);
    find('[data-observe-back]').onclick=back;find('.close').onclick=back;
    find('[data-study]')?.addEventListener('click',()=>confirmBox(`花费 ${info.studyCost} CP 永久学习 ${info.code} 的全部现有获取方法。不会自动收录或取得制作资格，未知名称与完整画像仍保密。`,()=>{if(transact(s=>studyRecipe(s,key,getNow())))observe(key,back);},false,{yes:'研读'}));
    each('[data-prepare-path]',b=>b.onclick=()=>confirmBox('替换下一批蛋种与材料。当前批次保留，开火时另行确认。',()=>{const r=transact(s=>prepareKnownPath(s,b.dataset.preparePath,getNow()));if(r)prepareTool(r.toolId);},false,{yes:'配好下一批'}));
  }
  return {open,observe,updateTime:()=>{const el=find('[data-trip-countdown]'),t=getState().progress.trip;if(el&&t?.status==='running'){const m=Math.max(0,Math.ceil((t.endAt-getNow())/60000));el.textContent='还需 '+Math.floor(m/60)+'小时'+m%60+'分';}},refresh:()=>{if(!detailKey&&!skillDetail&&tab==='trip'&&tripStatus!==getState().progress.trip?.status)trip();},isOpen:()=>!!find('.workshop-screen')};
}
