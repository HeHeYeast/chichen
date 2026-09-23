from pathlib import Path
p=Path('web/workshop-ui.js');s=p.read_text(encoding='utf-8')
s=s.replace('BRANCHES,NODE_NAMES,rank,skillPoints,skillGate,learnSkill,respecSkills,effects,collectedTotal','BRANCHES,SKILLS,SKILL_BY_ID,TRADE_CATEGORIES,rank,skillPoints,skillGate,skillRequirements,learnSkill,applySkillPlan,respecReason,effects,collectedTotal,discoveryCount,claimLeftovers')
s=s.replace("import {RULES", "import {TRADE_SPECIES} from './trade-data.js';\nimport {RULES")
a=s.index('const nodeDetails=');b=s.index('export function createWorkshopUI',a)
s=s[:a]+'''const branchArt={CUL:'0:3',HOME:'0:0',TRADE:'0:12',OBS:'0:21',TRIP:'0:8'};
const shortEffects={
 'CUL-1':'收取时，15%概率额外 +1 CP','CUL-2':'新批次减时10% · 最短6分钟','CUL-3':'收完一批，20%概率返1份普通材料','CUL-4':'30分钟内同配方接锅，额外减时5个百分点','CUL-5':'多付10 CP，安排1只已收录普通候选','CUL-S':'基础减时20% · 接锅时25%',
 'HOME-1':'每只破壳后，保鲜延长30分钟','HOME-2':'脱逃时，每个在家品种至少留1只','HOME-3':'下次打扫后，保持干净54小时','HOME-4':'额外保鲜提高至90分钟','HOME-5':'安心等候：用时+25%，保鲜至少8小时','HOME-S':'干净72小时 · 普通病变40%→20%',
 'TRADE-1':'普通材料货款返还6%，小数自动积攒','TRADE-2':'选一个招牌类别，出售加价12%','TRADE-3':'同种家常伙伴24只，额外获得12 CP','TRADE-4':'4种普通料理各3只，额外获得8 CP','TRADE-5':'经营奖励最多存6次','TRADE-S':'招牌加价18% · 整筐18 CP／拼盘12 CP',
 'OBS-1':'看剪影、蛋种、厨具与日期条件','OBS-2':'预览最多3种只差一味的未知方向','OBS-3':'看第一味、第二味类别与非材料条件','OBS-4':'100 CP，永久研读全部获取方法','OBS-5':'首次收取新品种，再记下1条可寻线索','OBS-S':'研读50 CP · 寻访可指定优先线索',
 'TRIP-1':'每趟20%概率带回6／12／18 CP','TRIP-2':'指定第1份基础材料','TRIP-3':'每位适应伙伴：材料+3、线索+2个百分点','TRIP-4':'连续5趟无线索，下次有合格线索时必得','TRIP-5':'溪岸／林间轻装：减时20%，基础材料少1份','TRIP-S':'可指定前2份材料 · 额外材料概率+6个百分点'};
const helpCopy={skills:'发现新品种、累计收取和升级厨房都能获得手艺点。点开手艺看具体效果，加入方案后点击「应用这套手艺」才会生效。普通手艺可以跨方向学习，专精只能选一个。正在进行的批次和旅程使用开始时的手艺。',trip:'选一条路线，再派出1～3种在家伙伴，每种1只。每种伙伴有采集、发现和适应环境；选人时可以看到带回材料和线索的机会。到期后伙伴自动回家，材料、CP和线索等你领取。材料包满了也不会丢失已带回的材料。',story:'采购没有截止日期，可以分批交付。每次交付都按数量支付普通货款，全部交齐后再给一次酬谢。已交付的伙伴不能取回；尚未交第一只时，可以更换订单允许的出品。',observe:'线索告诉你可以尝试什么；研读可以学会完整获取方法；只有实际收取，才会正式收入图鉴。知道方法不代表已经满足条件，也不保证普通随机配方每批都有目标。'};
const pct=v=>Number((v*100).toFixed(1))+'%';
const duration=h=>Math.floor(h)+'小时'+(Math.round(h%1*60)?Math.round(h%1*60)+'分':'');
'''+s[b:]
s=s.replace('draftBase=null;', 'draftBase=null,light=false,category=null,skillDetail=null;')
a=s.index('  function shell(');b=s.index('  function story()',a)
s=s[:a]+'''  function shell(title,body,footer='',standalone=false){
    showPanel(title,`${standalone?'':`<nav class="workshop-tabs" aria-label="厨房成长">${[['skills','手艺'],['story','生意'],['trip','寻访']].map(([id,label])=>`<button data-workshop-tab="${id}" aria-pressed="${tab===id}">${label}</button>`).join('')}<button data-workshop-help aria-label="查看${title}帮助">?</button></nav>`}<div class="workshop-body scroll">${body}</div>${footer?`<footer class="workshop-footer">${footer}</footer>`:''}`,'screen-panel workshop-screen'+(standalone?' workshop-detail':''));
    each('[data-workshop-tab]',b=>b.onclick=()=>open(b.dataset.workshopTab));
    find('[data-workshop-help]')?.addEventListener('click',()=>alertBox(helpCopy[tab]));
  }
  function open(next=tab){tab=next;detailKey=null;skillDetail=null;if(tab==='skills')skills();else if(tab==='story')story();else trip();}
  function initDraft(){if(draft)return;draft=structuredClone(getState());draftSteps=[];draftReset=false;draftBase=JSON.stringify(getState().progress.skills);category=getState().progress.trade.category;}
  function skills(){
    initDraft();skillDetail=null;
    const s=draft,p=skillPoints(s),actual=getState(),e=effects(actual),H=collectedTotal(s),D=discoveryCount(s),changed=draftReset||draftSteps.length;
    const defs=SKILLS.filter(n=>n.branch===branch),ready=defs.filter(n=>rank(s,n.id)||!skillGate(s,n.id)),locked=defs.filter(n=>!rank(s,n.id)&&skillGate(s,n.id));
    const card=n=>{
      const learned=!!rank(actual,n.id)&&!draftReset,chosen=!!rank(s,n.id),reason=skillGate(s,n.id),short=p.available<n.cost,tag=chosen?(learned?'✓ 已学会':'✓ 已加入'):reason?'尚未解锁':short?'还差'+(n.cost-p.available)+'点':'可学习';
      return `<article class="skill-node ${chosen?'is-learned':''} ${chosen&&!learned?'is-selected':''} ${reason&&!chosen?'is-locked':''}"><button class="skill-detail-link" data-skill-detail="${n.id}" aria-label="查看${n.name}效果与解锁条件"><span class="skill-emblem" aria-hidden="true">${portrait(branchArt[n.branch])}</span><span><span class="skill-state">${n.node==='S'?'专精 · ':''}${tag}</span><strong>${n.name}</strong></span><span class="skill-cost">${n.cost}<small>点</small></span></button><p>${shortEffects[n.id]}</p><div class="skill-card-bottom"><small>${n.when}</small>${chosen?`<span class="skill-check">${learned?'已生效':'待应用'}</span>`:`<button data-learn="${n.id}" ${reason||short?'disabled':''}>加入方案</button>`}</div></article>`;
    };
    shell('厨房手艺',`<div class="skill-ledger"><div><strong>${p.available}</strong> 可用点<span>已学 ${Object.keys(actual.progress.skills).length} 项</span></div><button data-point-help aria-label="点数从哪里来">?</button></div>${H<24?`<div class="workshop-empty">${portrait('0:0')}<h3>第一批，也是第一份本领</h3><p>再收取 ${24-H} 只，开启手艺。<br>完成第一批就能获得2点。</p><button data-workshop-home>回厨房收取</button></div>`:`<p class="skill-next">${D<190?'再发现 '+(5-D%5)+' 种伙伴，获得 1 点':'收取与厨房里程碑也能获得点数'} <button data-point-help>查看来源</button></p>`}${actual.progress.migrationNotice?'<details class="migration-note"><summary>手艺更新：旧点数已全部退还</summary><p>现在共有30个单级手艺。旧配方知识、经营奖励、订单与库存保留；正在进行的批次和清洁周期不变。采购返利现为6%，料理基础减时现为10%／20%。赠送一次不受冷却限制的重配。</p></details>':''}<nav class="branch-tabs" aria-label="手艺方向">${Object.entries(BRANCHES).map(([id,n])=>`<button data-branch="${id}" aria-pressed="${branch===id}">${n}</button>`).join('')}</nav>${ready.map(card).join('')}${locked.length?`<details class="future-skills" ${!ready.length?'open':''}><summary>后续手艺 · ${locked.length} 项 <span>查看解锁条件</span></summary>${locked.map(card).join('')}</details>`:''}${rank(s,'TRADE-2')&&branch==='TRADE'?`<div class="category-choice"><h4>小店的招牌</h4><p>选择后仅在重新分配时可更换。采购不加价。</p><div class="category-grid">${TRADE_CATEGORIES.map(c=>`<button data-category="${c}" aria-pressed="${category===c}" ${rank(actual,'TRADE-2')&&!draftReset?'disabled':''}>${category===c?'✓ ':''}${c}</button>`).join('')}</div>${category?`<details><summary>${category} · 查看合格伙伴</summary><div class="category-portraits">${Object.keys(TRADE_SPECIES).filter(k=>TRADE_SPECIES[k].category===category&&((s.total[k]??0)>0||(s.farm[k]??0)>0)).map(k=>`<span>${portrait(k)}${escape(name(k))}</span>`).join('')}</div><p>另有 ${Object.keys(TRADE_SPECIES).filter(k=>TRADE_SPECIES[k].category===category&&!((s.total[k]??0)>0||(s.farm[k]??0)>0)).length} 种尚未收录</p></details>`:''}</div>`:''}<details class="workshop-note"><summary>当前生效与重配</summary><p>基础减时 ${e.reduction*100}% · 保鲜加时 ${e.freshMinutes} 分钟<br>本次清洁 ${actual.cleanCycle.hours} 小时，下次打扫 ${e.cleanHours} 小时。</p><p>经营奖励 ${actual.progress.trade.credits}/${e.capacity} 次 · 再收取 ${24-actual.progress.trade.harvestProgress} 只增加1次。需学会成筐交售或多味拼盘。</p><p>尚有 ${(actual.progress.trade.rebateRemainder/100).toFixed(2)} CP材料返利未到账，攒满1 CP自动返还。</p><button data-respec>免费重配</button><p>${respecReason(actual,getNow())||'收完当前批次、队伍归队后，可重新分配。首次免费，此后间隔72小时。'}</p></details>${actual.progress.leftovers.length?`<div class="workshop-note">待收余料 ${actual.progress.leftovers.length}/5 份<button data-leftovers>收下余料</button><small>材料包满时继续为你保留</small></div>`:''}`,`<p class="draft-status" role="status">${changed?`试配中，效果尚未生效<br>本次${draftReset?'重新分配':'花费'} ${draftSteps.reduce((v,id)=>v+SKILL_BY_ID[id].cost,0)} 点 · 应用后剩余 ${p.available} 点`:'先把喜欢的手艺加入方案'}</p><div class="draft-actions"><button data-plan-cancel ${changed?'':'disabled'}>放弃改动</button><button class="orange" data-plan-apply ${changed?'':'disabled'}>应用这套手艺</button></div>`);
    each('[data-branch]',b=>b.onclick=()=>{branch=b.dataset.branch;skills();});
    each('[data-skill-detail]',b=>b.onclick=()=>showSkill(b.dataset.skillDetail));
    each('[data-learn]',b=>b.onclick=()=>addSkill(b.dataset.learn));
    each('[data-point-help]',b=>b.onclick=()=>alertBox('首次收取24只：2点。每发现5种：1点。每次厨房升级：2点。累计收取120、500、2,000、5,000只：各2点。当前内容最多54点。'));
    each('[data-category]',b=>b.onclick=()=>{category=b.dataset.category;skills();});
    find('[data-plan-cancel]').onclick=()=>{draft=null;skills();};
    find('[data-plan-apply]').onclick=()=>{const learned=draftSteps.map(id=>SKILL_BY_ID[id].name);if(transact(s=>applySkillPlan(s,{steps:draftSteps,reset:draftReset,category,base:draftBase},getNow()))){draft=null;skills();alertBox('已应用这套手艺'+(learned.length?'：'+learned.join('、'):'')+'。新批次与寻访从下次开始生效，清洁从下次打扫生效；查看与出售类立即生效。');}};
    find('[data-respec]')?.addEventListener('click',()=>{draft=structuredClone(getState());draft.progress.skills={};draftSteps=[];draftReset=true;category=null;skills();});
    find('[data-leftovers]')?.addEventListener('click',()=>{const r=transact(s=>claimLeftovers(s));draft=null;skills();alertBox(r?.length?'已收下 '+r.length+' 份余料。':'材料包已满。先去厨房使用材料，余料会继续保留。');});
    find('[data-workshop-home]')?.addEventListener('click',goKitchen);
  }
  function addSkill(id){try{learnSkill(draft,id);draftSteps.push(id);skills();}catch(e){alertBox(e.message);}}
  function showSkill(id){
    initDraft();skillDetail=id;const n=SKILL_BY_ID[id],p=skillPoints(draft),reason=skillGate(draft,id),chosen=rank(draft,id),extra=id==='TRADE-3'?`<details><summary>12种家常伙伴</summary><div class="category-portraits">${RULES.trade.eligibleSpecies.map(k=>`<span>${(getState().total[k]??0)>0?portrait(k):'<b>?</b>'}${(getState().total[k]??0)>0?escape(name(k)):speciesCode(k)}</span>`).join('')}</div></details>`:'';
    shell(n.name,`<div class="skill-detail-hero">${portrait(branchArt[n.branch])}<span>${BRANCHES[n.branch]} · ${n.node==='S'?'全局唯一专精':'厨房手艺'}<strong>${n.cost} 点</strong></span></div><h3>学习后</h3><p class="skill-full-effect">${escape(n.description)}</p>${extra}${['TRADE-3','TRADE-4'].includes(id)?'<p class="workshop-note">每新收取24只获得1次经营奖励，最多3次；首次学习共赠1次。整筐优先，再用剩余数量组拼盘，不能重复计数。</p>':''}<h3>何时生效</h3><p>${n.when}。试配不消耗CP，应用后才生效。</p><h3>解锁条件</h3><ul class="skill-requirements">${skillRequirements(draft,id).map(r=>`<li class="${r.met?'met':''}">${r.met?'✓':'○'} ${r.text}</li>`).join('')}</ul>${p.available<n.cost&&!chosen?`<p class="shortage">还差 ${n.cost-p.available} 点。发现新品种、收取与升级厨房可以获得点数。</p>`:''}`,`<button data-skill-back>‹ 返回手艺</button><button class="orange" data-detail-learn ${chosen||reason||p.available<n.cost?'disabled':''}>${chosen?'已加入方案':'加入方案 · '+n.cost+'点'}</button>`,true);
    find('[data-skill-back]').onclick=skills;find('.close').onclick=skills;find('[data-detail-learn]').onclick=()=>addSkill(id);
  }
'''+s[b:]
p.write_text(s,encoding='utf-8')
