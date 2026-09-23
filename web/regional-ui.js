import {routeIllustration} from './ui-icons.js';
import {regionalTripInfo,departRegional} from './regional-exploration.js';
import {identifyMaterial,studyRegionalMethod,prepareRegionalRecipe,pinRegionalMethod,prepareLocalAlternative} from './regional-methods.js';
import {recall,claimTrip} from './exploration.js';
import {availableCount} from './inventory.js';
import {materialCapacity} from './material-capacity.js';
import {resolveSpecies,SPECIES_ABILITIES,CONTENT_TEXT,REGIONAL} from './content-registry.js';
import {projectMaterial} from './visibility-model.js';
import {regionView,releasedRegions,focusLabel,traitLabel,environmentLabel} from './region-view.js';

const esc=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const countMaterials=s=>Object.values(s.ingredients).reduce((sum,n)=>sum+n,0);
const percent=n=>`${Math.round(n*100)}%`;
const minutes=n=>`${Math.floor(n/60)}小时${n%60?`${n%60}分`:''}`;
const STAGE={unknown:'尚无方向',direction:'已有试做方向',full:'完整方法已知',collected:'已收录'};
const CARD_TYPE={specimen:'标本',lore:'见闻',event:'事件'};
const concept='/web/art/regional-concept.svg';

/** A data-driven regional notebook; app owns state, routing and atomic commits. */
export function createRegionalUI({getState,getNow=Date.now,commitProgress,showPanel,panels,alertBox,confirmBox,goKitchen,openRecipe,openLegacyTrip,characterPortrait}) {
  let tab='trip',regionId=releasedRegions()[0],placeId=`${regionId}:0`,focus='specimen',members=[],keepOne=true,sampling=false,guide=false,cargo={},cargoKeep=true,picking=false,lastSignature='',selectedMethod=null;
  const find=q=>panels.querySelector(q),all=q=>panels.querySelectorAll(q);
  const name=key=>resolveSpecies(key)?.title_zh_CN??'已收录伙伴';
  const portrait=key=>characterPortrait?characterPortrait(...key.split(':').map(Number)):'<span class="regional-partner-dot" aria-hidden="true">●</span>';
  const transact=(fn,after=render)=>{const result=commitProgress(fn);if(result!==null&&result!==false){after();return result;}return null;};
  const regionName=id=>CONTENT_TEXT[id]?.name??id;
  const placeName=id=>REGIONAL.regions.flatMap(r=>r.places).find(p=>p.id===id)?.name??id;
  function shell(title,body,footer='') {
    const regions=releasedRegions();
    showPanel(title,`<nav class="regional-tabs" aria-label="${esc(regionName(regionId))}"><button data-regional-tab="trip" aria-pressed="${tab==='trip'}">出发与归来</button><button data-regional-tab="record" aria-pressed="${tab==='record'}">地区与发现</button>${openLegacyTrip?'<button data-regional-legacy-routes aria-label="原来的三条寻访路线">原路线</button>':''}</nav>${regions.length>1?`<div class="regional-region-list" role="group" aria-label="选择地区">${regions.map(id=>`<button data-regional-region="${id}" aria-pressed="${id===regionId}">${esc(regionName(id))}</button>`).join('')}</div>`:''}<div class="regional-body scroll">${routeIllustration()}${body}</div>${footer?`<footer class="regional-footer">${footer}</footer>`:''}`,'screen-panel regional-screen');
    all('[data-regional-tab]').forEach(b=>b.onclick=()=>open({tab:b.dataset.regionalTab}));
    all('[data-regional-region]').forEach(b=>b.onclick=()=>open({regionId:b.dataset.regionalRegion}));
    find('[data-regional-home]')?.addEventListener('click',()=>goKitchen());
    find('[data-regional-legacy]')?.addEventListener('click',()=>openLegacyTrip?.());
    // The three original routes (yard/water/wood) keep their own page; it returns here.
    find('[data-regional-legacy-routes]')?.addEventListener('click',()=>openLegacyTrip?.());
  }
  function open(options={}){
    if(typeof options==='string')options={tab:options};
    if(options.regionId&&options.regionId!==regionId&&releasedRegions().includes(options.regionId)){regionId=options.regionId;placeId=`${regionId}:0`;selectedMethod=null;sampling=false;guide=false;cargo={};}
    if(options.recipeId)selectedMethod=options.recipeId;
    tab=options.tab??tab;picking=false;render();
    if(Number.isInteger(options.materialId))find(`[data-regional-material="${options.materialId}"]`)?.scrollIntoView({block:'start'});
  }
  function render(){picking=false;if(tab==='record')record();else trip();lastSignature=signature();}
  function signature(){const s=getState();return JSON.stringify([s.progress.trip,s.expansion.regions,s.expansion.discovery,s.expansion.methods,s.expansion.trial,s.expansion.prepareMode,s.ingredients,s.farm,s.total]);}
  function refresh(){if(!find('.regional-screen')||picking)return;if(signature()!==lastSignature)render();else updateCountdown();}
  function updateCountdown(){const t=getState().progress.trip,node=find('[data-regional-countdown]');if(node&&t?.status==='running')node.textContent=minutes(Math.max(0,Math.ceil((t.endAt-getNow())/60000)));}
  function trip() {
    const s=getState(),t=s.progress.trip;
    if(t&&!['settled','recalled'].includes(t.status)) {
      if(t.version!==2||!t.regional){shell(`${regionName(regionId)}寻味`,`<div class="regional-note"><h3>伙伴已有行程</h3><p>同一支队伍完成或结清当前寻访后，就能带着新方向出发。</p></div>`,`<button data-regional-legacy>查看当前寻访</button><button data-regional-home>回厨房</button>`);return;}
      if(t.regional.regionId!==regionId){regionId=t.regional.regionId;placeId=t.regional.placeId;}
      activeTrip(s,t);return;
    }
    members=members.filter(k=>availableCount(s,k)>0);
    const view=regionView(s,regionId,{members});
    if(!view.materials.some(m=>m.identified))sampling=false;
    const probe=regionalTripInfo(s,{regionId,placeId,focus,members,sampling},getNow());
    if(!probe.guide.available)guide=false;if(!probe.cargo.available)cargo={};
    const cargoTotal=Object.values(cargo).reduce((n,v)=>n+v,0);
    const info=regionalTripInfo(s,{regionId,placeId,focus,members,sampling,guide,cargo:cargoTotal?cargo:null,cargoOverrideKeepOne:!cargoKeep},getNow());
    const places=view.places.map(p=>`<button data-regional-place="${p.id}" aria-pressed="${placeId===p.id}">${esc(p.name)}</button>`).join('');
    const focuses=[['materials','补材料'],['specimen','找标本'],['lore','寻见闻']].map(([id,label])=>`<button data-regional-focus="${id}" aria-pressed="${focus===id}">${label}</button>`).join('');
    const team=[0,1,2].map(i=>`<button data-regional-slot="${i}" aria-label="${members[i]?`更换${esc(name(members[i]))}`:`选择第${i+1}位同行伙伴`}">${members[i]?portrait(members[i]):'<span class="regional-empty-slot" aria-hidden="true">＋</span>'}<strong>${members[i]?esc(name(members[i])):'选伙伴'}</strong><small>${members[i]?`采集${SPECIES_ABILITIES[members[i]].gather} · 发现${SPECIES_ABILITIES[members[i]].discover}`:'可留空'}</small></button>`).join('');
    const samplingField=view.materials.some(m=>m.identified)?`<label class="regional-keep"><input type="checkbox" data-regional-sampling ${sampling?'checked':''}>地区采样：第一份基础材料从已辨认的本地材料中取得</label>`:'';
    const guideField=info.guide.done?'<p class="regional-success">海湾路线已开放，可在地区列表选择风湾盐田。</p>':regionId==='R'&&placeId==='R:1'?(info.guide.available?`<label class="regional-keep"><input type="checkbox" data-regional-guide ${guide?'checked':''}>追寻沿湾路标：完整归队后开放海湾路线（不占发现卡）</label>`:`<p class="regional-muted">沿湾路标：${esc(info.guide.reason)}</p>`):'';
    const cargoRows=info.cargo.available?info.cargo.allowed.filter(k=>(s.total[k]??0)>0&&availableCount(s,k)>0).map(k=>`<div class="regional-cargo-row"><span>${esc(name(k))}<small> 自由 ${availableCount(s,k)}</small></span><span><button data-cargo-minus="${k}" aria-label="少带1只${esc(name(k))}" ${cargo[k]?'':'disabled'}>−</button><output>${cargo[k]??0}</output><button data-cargo-plus="${k}" aria-label="多带1只${esc(name(k))}" ${cargoTotal<info.cargo.quantity?'':'disabled'}>＋</button></span></div>`).join(''):'';
    const cargoField=info.cargo.available?`<details class="regional-cargo" ${cargoTotal?'open':''}><summary>可选带货：另带家常${info.cargo.quantity}只，换盐花1份</summary><p class="regional-muted">带货不是同行队员；只有完整归队才扣货并换来盐花，占用本趟1份基础材料，不付售价、不计营业或采购。召回原样放回。</p><label class="regional-keep"><input type="checkbox" data-cargo-keep ${cargoKeep?'checked':''}>每种在家留1只</label>${cargoRows}<p>${cargoTotal}/${info.cargo.quantity} 只${cargoTotal&&cargoTotal!==info.cargo.quantity?' · 需要正好'+info.cargo.quantity+'只':''}</p></details>`:'';
    shell(`${view.name}寻味`,`<header class="regional-landscape"><span class="regional-region-mark">${esc(view.name)} · ${esc(placeName(placeId))}</span><h3>这一趟，去${esc(placeName(placeId))}看看</h3><p>免费出发 · 1～3种伙伴，每种1只</p></header>${!view.met?`<section class="regional-note"><h3>再准备一点，就能出发</h3><ul>${view.missing.map(x=>`<li>${esc(x)}</li>`).join('')}</ul></section>`:''}<div class="regional-field"><h3>这趟去哪里</h3><div class="regional-choices">${places}</div></div><div class="regional-field"><h3>多留意什么</h3><div class="regional-choices">${focuses}</div></div><div class="regional-team-heading"><h3>同行的小伙伴</h3><button data-regional-last>上次队伍</button></div><div class="regional-team">${team}</div>${samplingField}${guideField}${cargoField}${outlook(s,info,view)}${info.missing?.length?`<p class="regional-shortage">${info.missing.map(esc).join('；')}</p>`:''}`,`<span class="regional-footer-caption">${minutes(Math.round(info.hours*60))} · 不花CP</span><button class="orange" data-regional-depart ${info.canDepart?'':'disabled'}>${members.length?'确认这趟行程':'先选同行伙伴'}</button>`);
    all('[data-regional-place]').forEach(b=>b.onclick=()=>{placeId=b.dataset.regionalPlace;trip();});
    all('[data-regional-focus]').forEach(b=>b.onclick=()=>{focus=b.dataset.regionalFocus;trip();});
    all('[data-regional-slot]').forEach(b=>b.onclick=()=>pick(+b.dataset.regionalSlot));
    find('[data-regional-sampling]')?.addEventListener('change',e=>{sampling=e.target.checked;trip();});
    find('[data-regional-guide]')?.addEventListener('change',e=>{guide=e.target.checked;trip();});
    find('[data-cargo-keep]')?.addEventListener('change',e=>{cargoKeep=e.target.checked;trip();});
    all('[data-cargo-plus]').forEach(b=>b.onclick=()=>{cargo[b.dataset.cargoPlus]=(cargo[b.dataset.cargoPlus]??0)+1;trip();});
    all('[data-cargo-minus]').forEach(b=>b.onclick=()=>{const k=b.dataset.cargoMinus;cargo[k]=Math.max(0,(cargo[k]??0)-1);if(!cargo[k])delete cargo[k];trip();});
    find('[data-regional-last]').onclick=()=>{members=s.progress.lastTeam.filter(k=>availableCount(s,k)>(keepOne?1:0)).slice(0,3);trip();};
    find('[data-regional-depart]').onclick=()=>{
      const options={regionId,placeId,focus,members:[...members],sampling,guide,cargo:cargoTotal?{...cargo}:null,cargoOverrideKeepOne:!cargoKeep},lastCopies=members.filter(k=>availableCount(getState(),k)===1);
      const intro=info.firstSpecimen?CONTENT_TEXT[REGIONAL.materials.find(m=>m.id===info.introMaterial).stableId]?.name:null;
      confirmBox(`${view.name} · ${placeName(placeId)} · ${minutes(Math.round(info.hours*60))}\n${members.map(name).join('、')}\n保证${info.minUnits}份材料${intro?`，其中1份换成${intro}试做材料；${intro}标本另记入册，不占包。`:sampling?'，其中1份来自地区采样。':'。'}${info.guideRequested?'\n追寻沿湾路标：完整归队后开放海湾路线。':''}${info.cargoUsed?`\n另带${cargoTotal}只家常同行交换，完整归队才扣货并换盐花1份。`:''}${lastCopies.length?`\n${lastCopies.map(name).join('、')}仅剩1只，外出期间家中暂时没有。`:''}`,()=>transact(draft=>departRegional(draft,options,getNow()),()=>{cargo={};guide=false;render();}),false,{yes:'免费出发'});
    };
  }
  function outlook(s,info,view) {
    const free=materialCapacity(s)-countMaterials(s),protection=view.protection[focus]??0;
    const intro=info.firstSpecimen?CONTENT_TEXT[REGIONAL.materials.find(m=>m.id===info.introMaterial).stableId]?.name:null;
    const here=view.cards.filter(c=>c.placeId===placeId&&c.focus===focus);
    const chances=new Map(info.candidates.map(c=>[c.cardId,c.chance]));
    const rows=here.map(c=>`<li data-regional-card-preview="${c.id}"><strong>${esc(CARD_TYPE[c.type])} · ${esc(c.title)}</strong> ${c.found?'<span class="regional-success">已记录</span>':chances.has(c.id)?`<span>${percent(chances.get(c.id))}机会</span>`:'<span class="regional-shortage">暂不符合</span>'}${!c.found?`<br><small>${esc(c.hint)}</small>${c.team.length?`<br><small>队伍条件：${c.team.map(esc).join('＋')}（同一位可同时满足）${c.teamMet===false?' · 当前队伍尚未满足':''}</small>`:''}${c.gateMissing.length?`<br><small class="regional-shortage">${c.gateMissing.map(esc).join('；')}</small>`:''}`:''}</li>`).join('');
    const discovery=intro?`这趟带回${intro}标本`:focus==='materials'?'本次专心补材料；新发现卡只在找标本或寻见闻时出现':info.candidates.length?`${percent(Math.max(...info.candidates.map(c=>c.chance)))}以内机会记下1张新发现（每趟最多1张）`:here.length&&here.every(c=>c.found)?'本方向已完成，可继续补材料':'本次关注暂时没有合格的新发现';
    return `<section class="regional-outlook"><h3>这一趟能带回什么</h3><p><strong>${info.minUnits}份材料保证</strong> · ${percent(info.materialChance)}机会再添1份</p><p>${info.clues?.length?`旧配方线索 ${percent(info.clueChance)} · 独立保护 ${s.progress.routeFailures[info.route.id]}/${info.hardAttempt-1}`:'旧配方线索：当前没有可寻的新线索'}</p><p class="regional-discovery-preview">${discovery}</p>${rows&&focus!=='materials'?`<ul class="regional-card-preview">${rows}</ul>`:''}${!info.firstSpecimen&&focus!=='materials'&&info.candidates.length?`<p>新发现独立保护 ${protection}/3 · 连续3趟未得新卡，第4趟有合格候选时保证发现；换地点、换队伍不清零</p>`:''}${info.firstSpecimen?`<small>试做材料替换1份基础材料；标本自动入册。${info.introOverridesPlace?'首次行程会先带你找到入门标本。':''}</small>`:''}${info.entryMissing?.length?`<p class="regional-shortage">首标本准备：${info.entryMissing.map(esc).join('；')}</p>`:''}${free<info.minUnits?`<p class="regional-shortage">材料包还可放${free}份。放不下的材料留在归队篮中，标本照常记录。</p>`:''}<p class="regional-muted">${info.method.targetId?`免费方法进度 ${info.method.countBefore}/3 · 完整完成符合条件的行程后推进`:'免费方法：辨认标本后，选择当前可做的未知方向'}</p></section>`;
  }
  function pick(slot) {
    picking=true;const s=getState(),available=Object.keys(s.farm).filter(k=>(s.total[k]??0)>0&&availableCount(s,k)>(keepOne?1:0)&&(!members.includes(k)||members[slot]===k));
    const traits=k=>(resolveSpecies(k)?.traits??[]).map(traitLabel).join('、');
    shell('选一位同行伙伴',`<label class="regional-keep"><input type="checkbox" data-regional-keep ${keepOne?'checked':''}>每种在家留1只</label><p class="regional-muted">派出每种1只，最多3种。取消留一限制后，也可派出唯一的一只。</p><div class="regional-member-list">${available.map(k=>`<button data-regional-member="${k}">${portrait(k)}<span><strong>${esc(name(k))}</strong><small>在家 ${availableCount(s,k)}只 · 采集${SPECIES_ABILITIES[k].gather} · 发现${SPECIES_ABILITIES[k].discover} · 适应${environmentLabel(SPECIES_ABILITIES[k].environment)}${traits(k)?` · ${esc(traits(k))}`:''}</small></span></button>`).join('')||'<p class="regional-note">当前没有符合筛选的伙伴。可以取消留一限制，或回厨房收取。</p>'}</div>`,`<button data-regional-pick-back>返回行程</button>${members[slot]?'<button data-regional-remove>空出这一位</button>':''}`);
    find('[data-regional-keep]').onchange=e=>{keepOne=e.target.checked;pick(slot);};
    all('[data-regional-member]').forEach(b=>b.onclick=()=>{members[slot]=b.dataset.regionalMember;members=members.filter(Boolean);render();});
    find('[data-regional-pick-back]').onclick=render;
    find('[data-regional-remove]')?.addEventListener('click',()=>{members.splice(slot,1);render();});
  }
  function activeTrip(s,t) {
    const running=t.status==='running',room=materialCapacity(s)-countMaterials(s),card=t.regional.result?.cardId,label=regionName(t.regional.regionId);
    shell(running?`去${label}的路上`:`带着${label}的收获回家`,`<header class="regional-landscape"><span class="regional-region-mark">${esc(label)} · ${esc(placeName(t.regional.placeId))} · ${esc(focusLabel(t.regional.focus))}</span><h3>${running?'慢慢走，到点就回':'伙伴已经回家了'}</h3>${running?'<p>还需 <strong data-regional-countdown></strong></p>':'<p>新发现先入册，实体材料留在小篮子里。</p>'}<div class="regional-return-team">${t.members.map(k=>`<span>${portrait(k)}<small>${esc(name(k))}</small></span>`).join('')}</div></header>${running?'<p class="regional-note">到时伙伴自动归队。标本、见闻、方法与完整同行记录在归队时记下；没有领完的材料不会丢失。</p><details class="regional-recall"><summary>行程管理</summary><p>提前召回不会获得这趟材料与新发现，也不计入完整行程保护。</p><button data-regional-recall>提前召回</button></details>':`${card?`<section class="regional-found"><small>本次新发现 · 已入册，不占包</small><h3>${esc(CONTENT_TEXT[card]?.title??card)}</h3><p>${esc(CONTENT_TEXT[card]?.result??'')}</p><button data-regional-record>辨认与查看下一步</button></section>`:`<p class="regional-note">这一趟没有新的发现卡。完整行程进度已经记入${esc(label)}册页。</p>`}${t.regional.result?.methodId?'<p class="regional-success">免费补全了一份地方做法。</p>':''}<section class="regional-basket"><h3>实际材料篮</h3><p>材料包 ${countMaterials(s)}/${materialCapacity(s)} · 篮中 ${t.remaining.length}份</p><div class="regional-materials">${t.remaining.map(id=>`<span>${esc(projectMaterial(s,id).name)} ×1</span>`).join('')||'<span>材料已经领完</span>'}</div>${!room&&t.remaining.length?'<p class="regional-shortage">材料包已满。可以先辨认标本，或去厨房使用材料。</p>':''}<p>${t.cpProcessed?'寻访CP已结算':`待领CP：${t.cpReward??0} CP`} · ${t.clueProcessed?'旧线索已处理':'旧线索等待翻阅'}</p><button data-regional-claim-notes>先领CP与旧线索</button><details><summary>整理篮子</summary><button data-regional-discard>放弃剩余${t.remaining.length}份材料</button></details></section>`}`,running?'<button data-regional-home>回厨房等候</button>':`${room&&t.remaining.length?`<button class="orange" data-regional-claim>领入${Math.min(room,t.remaining.length)}份材料</button>`:'<button data-regional-home>去使用材料</button>'}<button data-regional-record>查看地区记录</button>`);
    updateCountdown();
    find('[data-regional-recall]')?.addEventListener('click',()=>confirmBox('现在召回，伙伴立即回家。这趟不会得到材料、CP、旧线索或地区新发现。',()=>transact(draft=>recall(draft,t.id,getNow())),false,{yes:'召回伙伴'}));
    all('[data-regional-record]').forEach(b=>b.onclick=()=>open({tab:'record'}));
    const claim=options=>transact(draft=>claimTrip(draft,t.id,options,getNow()));
    find('[data-regional-claim]')?.addEventListener('click',()=>{const r=claim({});if(r)alertBox(`已收下${r.materials.length}份材料。${r.remaining?`篮中还有${r.remaining}份。`:'这趟行程已结清。'}`);});
    find('[data-regional-claim-notes]')?.addEventListener('click',()=>{if(claim({materials:false}))alertBox('CP与旧线索已按本趟票据处理。剩余材料继续留在篮中。');});
    find('[data-regional-discard]')?.addEventListener('click',()=>confirmBox(`放弃篮中剩余${t.remaining.length}份材料，不转换CP。已经获得的发现与方法记录保留。`,()=>claim({materials:false,discard:true}),false,{yes:'放弃这些材料'}));
  }
  // The detailed method defaults to something actionable, so returning to the
  // page always lands on the next real step instead of the first grid cell.
  function chooseMethod(view){
    const by=id=>view.methods.find(m=>m.recipeId===id);
    return by(selectedMethod)??view.methods.find(m=>m.prepared)??view.methods.find(m=>m.pinned&&!m.full)??view.methods.find(m=>m.met&&!m.collected)??view.methods.find(m=>m.met)??view.methods.find(m=>m.direction&&!m.full)??view.methods[0];
  }
  function methodDetail(m,s){
    const targetName=m.collected?m.name:`${m.code} · 尚未收录`,trial=m.trial;
    const recipe=m.full||m.collected?`<p>${m.egg?'鸭蛋':'鸡蛋'} · ${esc(m.tool)} · ${esc(m.kitchen)} · ${m.ingredients.map(x=>`${esc(x)}1份`).join('＋')}</p><p>使用精确材料组合；额外加入其他材料不会成为这份试做。</p>`:m.direction?`<p>试做方向：${m.egg?'鸭蛋':'鸡蛋'} · ${esc(m.tool)} · 第一味 ${esc(m.firstIngredient)}</p><p>完整方法来自符合条件的完整行程；手艺允许时，也可支付CP研读。</p>`:'<p>还没有这份做法的方向。辨认相关标本或记下对应见闻后出现。</p>';
    const protection=m.full?`<p class="regional-note">${m.collected?'再次准备时安排1只，其他23枚照常。':trial.owed?'本目标的保护仍在：清洁并及时收取，完成安排目标的这批。':`首次每批有25%机会安排目标；连续失败${trial.failed}/3批后，第4批保护。请保持清洁并及时收取。`}</p>`:'';
    return `<section class="regional-method" data-regional-method-detail="${m.recipeId}"><div class="regional-target"><img src="${concept}" alt="${m.code}${m.collected?'概念剪影，正式美术待完成':'未知概念剪影'}"><span><small>${m.full&&!m.collected?'完整方法已记下':STAGE[m.stage]}${m.ornamental?' · 观赏，不上营业菜单':''}</small><h3>${esc(targetName)}</h3></span></div>${m.clue?`<p class="regional-muted">${esc(m.clue)}</p>`:''}${m.description?`<p>${esc(m.description)}</p>`:''}${recipe}${!m.full&&!m.collected?`<p class="regional-free-progress">免费方法进度 <strong>${s.expansion.methods.freeProgress[regionId]?.count??0}/3</strong>${m.pinned?' · 已作为寻访目标':''}</p>`:'<p class="regional-free-progress">完整方法已记入册页</p>'}${!m.full&&m.direction?`${m.pinned?'':'<button data-regional-pin>把这份方法作为寻访目标</button>'}${m.canStudy?`<button data-regional-study>研读完整方法 · ${m.studyCost} CP</button>`:''}`:''}${m.missing?.length?`<ul class="regional-requirements">${m.missing.map(x=>`<li>${esc(x)}</li>`).join('')}</ul>`:''}${protection}</section>`;
  }
  function record() {
    const s=getState(),view=regionView(s,regionId),m=chooseMethod(view);selectedMethod=m.recipeId;
    const materials=view.materials.map(x=>`<section class="regional-specimen" data-regional-material="${x.id}"><span class="regional-stamp ${x.identified?'is-identified':''}">${x.identified?'已辨认':x.found?'已记录':'待发现'}</span><h3>${x.found?esc(x.specimenName):esc(view.cards.find(c=>c.id===x.specimenCard)?.title)}</h3><p>${x.found?esc(x.recognition):esc(view.cards.find(c=>c.id===x.specimenCard)?.hint)}</p>${x.found&&!x.identified?`<button class="orange" data-regional-identify="${x.id}">免费辨认${esc(x.name)}</button>`:x.identified?`<p class="regional-success">${esc(x.name)}供货已开放 · ${x.price} CP/份${x.used?' · 已用于料理':''}</p><p class="regional-muted">${esc(x.lore)}</p>`:'<button data-regional-to-trip>去找标本</button>'}<small>标本不占材料格。辨认不会再赠材料；首次试做料来自归队篮。</small></section>`).join('');
    const notes=view.cards.filter(c=>c.type!=='specimen').map(c=>`<li class="regional-note-card" data-regional-card="${c.id}"><strong>${esc(CARD_TYPE[c.type])} · ${esc(c.title)}</strong> · ${esc(placeName(c.placeId))}${c.found?`<span class="regional-success"> 已记录</span><br><small>${esc(c.result)}</small>`:`<br><small>${esc(c.hint)}</small>${c.team.length?`<br><small>队伍：${c.team.map(esc).join('＋')}</small>`:''}${c.gateMissing.length?`<br><small class="regional-shortage">${c.gateMissing.map(esc).join('；')}</small>`:''}`}</li>`).join('');
    const grid=view.methods.map(x=>`<button class="regional-method-cell" data-regional-method="${x.recipeId}" aria-pressed="${x.recipeId===m.recipeId}" aria-label="${x.code} ${STAGE[x.stage]}"><img src="${concept}" alt=""><span>${x.code}</span><small>${x.collected?esc(x.name):STAGE[x.stage]}</small></button>`).join('');
    const alternatives=view.alternatives.map(a=>`<section class="regional-alternative" data-regional-alternative="${a.id}"><h3>${a.known?esc(a.name):'地方替代做法 · 尚未记下'}</h3><p>${a.known?`沿用${a.targetName?esc(a.targetName):a.targetCode}的原身份与原概率 · ${esc(a.tool)} · ${a.ingredients.map(esc).join('＋')}`:`${a.unlockCard.startsWith('PJ-')?`完成项目「${esc(a.unlockName)}」`:`记下发现 ${esc(a.unlockCard)}`}后，可以用地方材料替换原配料。`}</p>${a.known&&a.missing.length?`<ul class="regional-requirements">${a.missing.map(x=>`<li>${esc(x)}</li>`).join('')}</ul>`:''}${a.known?`<button data-regional-alt-prepare="${a.id}" ${a.met?'':'disabled'}>${a.prepared?'已准备 · 去厨房开火':'按替代做法准备下一锅'}</button><small>不额外安排目标；不能与地区试做、四时、礼物或点心保证叠加。</small>`:''}</section>`).join('');
    const prepareLabel=m.prepared?'已准备 · 去厨房开火':m.collected?'再次准备（安排1只）':'准备这道新风味';
    shell(`${view.name}的发现册`,`<header class="regional-record-heading"><span>发现卡 ${view.counts.cards}/${view.cards.length} · 新品 ${view.counts.collected}/${view.counts.total}</span><h3>标本 → 辨认 → 厨房试做</h3></header>${materials}<section class="regional-notes"><h3>见闻与事件</h3><ul>${notes}</ul></section><section class="regional-methods"><h3>地方做法</h3><div class="regional-method-grid">${grid}</div></section>${methodDetail(m,s)}${alternatives}`,`<button data-regional-to-trip>回到寻访</button><button class="orange" data-regional-prepare ${(m.full||m.collected)&&m.met?'':'disabled'}>${prepareLabel}</button>`);
    all('[data-regional-to-trip]').forEach(b=>b.onclick=()=>open({tab:'trip'}));
    all('[data-regional-identify]').forEach(b=>b.onclick=()=>transact(draft=>identifyMaterial(draft,Number(b.dataset.regionalIdentify))));
    all('[data-regional-method]').forEach(b=>b.onclick=()=>{selectedMethod=b.dataset.regionalMethod;record();find(`[data-regional-method-detail]`)?.scrollIntoView?.({block:'nearest'});});
    find('[data-regional-pin]')?.addEventListener('click',()=>transact(draft=>pinRegionalMethod(draft,m.recipeId)));
    find('[data-regional-study]')?.addEventListener('click',()=>confirmBox(`支付${m.studyCost} CP，永久记下${m.code}的完整方法。研读不代表已经收录，也不会赠送实体材料。`,()=>transact(draft=>studyRegionalMethod(draft,m.recipeId,getNow())),false,{yes:'研读方法'}));
    const toKitchen=recipe=>{if(openRecipe)openRecipe(recipe);else goKitchen(REGIONAL.recipes.find(r=>r.id===recipe)?.toolId??REGIONAL.alternatives.find(a=>a.id===recipe)?.toolId);};
    find('[data-regional-prepare]').onclick=()=>transact(draft=>prepareRegionalRecipe(draft,m.recipeId),()=>toKitchen(m.recipeId));
    all('[data-regional-alt-prepare]').forEach(b=>b.onclick=()=>transact(draft=>prepareLocalAlternative(draft,b.dataset.regionalAltPrepare),()=>toKitchen(b.dataset.regionalAltPrepare)));
  }
  return {open,refresh};
}
