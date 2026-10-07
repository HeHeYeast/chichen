import {visualImage,progressTrack} from './visual-assets.js';
import {kitChip,kitChipHtml,kitIcon,kitButton,kitButton2,kitLabel,kitBar} from './ui-kit.js';
import {journeyPath,journeyArt,journeyMaterial as materialArt,journeyDiscovery as discoveryArt,journeyUnknown as unknownArt,journeyCharacter,journeySeat,journeyPositions,journeyPlace,journeyRoutePath,journeyEnvironment} from './journey-art.js';
import {regionalTripInfo,departRegional} from './regional-exploration.js';
import {identifyMaterial,studyRegionalMethod,prepareRegionalRecipe,pinRegionalMethod,prepareLocalAlternative} from './regional-methods.js';
import {recall,claimTrip,depart,explorationInfo} from './exploration.js';
import {regionCard,trackedTrail,tripClueAdvance,regionOfTrip} from './journey-model.js';
import {REGION_ROUTE,clueRegionOf} from './clue-regions.js';
import {speciesCode,trackPartner,trackedKey} from './knowledge.js';
import {clueRow,CLUE_STEPS,TRIP_LAYER_NAMES} from './clue-book.js';
import {isRegionalKey,regionOfKey,regionalGate,regionalRows,regionShort} from './regional-clues.js';
import {LEGACY193} from './legacy-content.js';
const cookwareName=id=>LEGACY193.tools[1].find(t=>t.id===id)?.title_zh_CN??'厨具';
import {regionHints} from './order-intel.js';
const INTEL_ART='/web/art/golden-journey/envelope.png';
import {availableCount} from './inventory.js';
import {toolImage} from './catalog.js';
import {materialCapacity} from './material-capacity.js';
import {resolveSpecies,SPECIES_ABILITIES,CONTENT_TEXT,REGIONAL} from './content-registry.js';
import {projectMaterial} from './visibility-model.js';
import {regionView,releasedRegions,focusLabel,traitLabel,environmentLabel,materialLabel} from './region-view.js';

const esc=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const countMaterials=s=>Object.values(s.ingredients).reduce((sum,n)=>sum+n,0);
const percent=n=>`${Math.round(n*100)}%`;
const minutes=n=>`${Math.floor(n/60)}小时${n%60?`${n%60}分`:''}`;
// The region's partners speak the 线索册's words (loop batch 4): 调查 x/5, 做法齐了, 已收录.
const stageText=(m,r)=>m.collected?'已收录':r?.held?'做法齐了':`调查 ${r?.progress??0}/${CLUE_STEPS}`;
const CARD_TYPE={specimen:'标本',lore:'见闻',event:'事件'};

/** Read-only visual navigation around the original regional commands.
 * 寻访 home is the map with one card under it: the selected region (what is left to find there, where the tracked partner's
 * next clue is, the party and 立即寻访), the trip on the road, or the trip back home (what came home and what the clue
 * adds to the 线索册). 「地点与方向」 is the second layer for choosing a place, a direction and the extras by hand.
 * The old 近郊 routes have no page of their own: a region not open yet still sends a party along its old route. */
export function createRegionalUI({getState,getNow=Date.now,commitProgress,showPanel,panels,alertBox,confirmBox,kitDialog,characterPortrait,goKitchen,openRecipe,openClueBook,skillFeedback}) {
  // manualPlan: the player chose place and direction in 「地点与方向」 (otherwise 立即寻访 picks them); forKey: the partner the
  // 线索册 sent the player here for; pickReturn: the screen the partner picker goes back to.
  let manualPlan=false,forKey=null,pickReturn='region';
  // skill options that used to live on the 近郊 page: 轻装折返 (TRIP-5), the first base materials (TRIP-2 / TRIP-S), the
  // clue to look for first (寻味专家 OBS-S)
  let light=false,directed=[],priority=null;
  let tab='trip',screen='map',regionId=releasedRegions()[0],placeId=`${regionId}:0`,focus='specimen',members=[],keepOne=true,sampling=false,guide=false,cargo={},cargoKeep=true,picking=false,lastSignature='',selectedMethod=null,pickSlot=0,autoTeam=null,again=null;
  const find=q=>panels.querySelector(q),all=q=>panels.querySelectorAll(q);
  const name=key=>resolveSpecies(key)?.title_zh_CN??'已收录伙伴';
  const portrait=journeyCharacter;
  const transact=(fn,after=render)=>{const result=commitProgress(fn);if(result!==null&&result!==false){after();return result;}return null;};
  const regionName=id=>CONTENT_TEXT[id]?.name??id;
  const placeName=id=>REGIONAL.regions.flatMap(r=>r.places).find(p=>p.id===id)?.name??id;
  const tripActive=()=>['running','returned'].includes(getState().progress.trip?.status);
  // A team may bring one kind several times, but never more than are at home now.
  const fitTeam=(st,list)=>{const used={};return list.filter(k=>(used[k]=(used[k]??0)+1)<=availableCount(st,k)).slice(0,3);};
  function positionTravellers(t){
    const path=find('.journey-routes path');if(!path)return;
    const length=path.getTotalLength(),progress=t.status==='running'?Math.max(0,Math.min(1,(getNow()-t.startedAt)/(t.endAt-t.startedAt))):1;
    const leader=Math.max(.20,Math.min(.94,.12+progress*.70));
    // The sprites are square cells (about 60 units wide on the 390-wide map): walk each follower back along the road until
    // it stands clear of the one ahead, so the cell of one never hides another.
    const els=[...all('.journey-traveller')];let u=leader,ahead=null;
    for(let i=els.length-1;i>=0;i--){
      let p=path.getPointAtLength(u*length);
      if(ahead)while(u>.01&&Math.hypot(p.x-ahead.x,p.y-ahead.y)<62){u-=.004;p=path.getPointAtLength(u*length);}
      els[i].style.left=(p.x/390*100)+'%';els[i].style.top=(p.y/684*100)+'%';ahead=p;
    }
  }
  function selectRegion(id){if(regionId!==id){regionId=id;placeId=`${id}:0`;selectedMethod=null;sampling=false;guide=false;cargo={};manualPlan=false;light=false;directed=[];priority=null;}}
  function helpBody(){
    const s=getState();let detail='';
    if(!tripActive())try{const info=regionalTripInfo(s,options(),getNow());detail=outlook(s,info,regionView(s,regionId,{members}));}catch{}
    return `<h3>怎么寻访</h3><p>① 在地图上选地区；② 带 1～3 位伙伴，点「立即寻访」，免费出发（想自己挑地点和方向，点「地点与方向」）；③ 时间到了伙伴会自己回来，再到「寻访」收下收获。</p>`+
      `<h3>伙伴线索</h3><p>每只还没认识的伙伴，线索都在一个地区。寻访一趟最多读到一只伙伴的下一条线索（剪影、厨具、第一味、第二味的类别），已经知道的不会重复。在线索册追踪一只，它的线索在哪个地区，地图上就标出来；去那里寻访，每趟一定带回它的下一条线索。</p>`+
      `<h3>伙伴的两项能力</h3><p>采集越高，越可能多带回 1 份材料；发现越高，越容易记下新发现。有些发现要求队伍里有指定环境、特征的伙伴，一位伙伴可以同时满足两项。</p>`+
      `<h3>第一趟</h3><p>每个地区第一次出发，会先带回入门标本，不论选哪个地点和方向；还会有 1 份材料换成能拿来试做的地方材料。标本不占材料包。若还缺试做它的厨具或材料，第一趟就带不回，缺什么看「找标本」的目标提示。</p>`+
      `<h3>地区伙伴</h3><p>地区伙伴和其他伙伴一样在线索册里调查。先在本地区找到它要的标本（或见闻），免费辨认后就知道厨具和第一味；之后在本地区寻访，还能读到剪影、第二味的类别，最后读到完整做法——这是地区伙伴才有的，其他伙伴要自己试。追踪它时，它要的标本、见闻和下一条线索，每趟都一定带回。</p>`+
      `<h3>地区试做</h3><p>做法齐了就在下一锅试做：每锅 25% 机会出现，连续 3 锅没出，第 4 锅一定出（要保持厨房清洁、及时收取）。已收录的地区伙伴再做时，每锅安排 1 只。</p>`+
      `<h3>不会一直落空</h3><p>找标本、寻见闻每趟最多记下 1 张新发现；同一地区、同一方向连续 3 趟都没有时，第 4 趟只要还有能找的发现，就一定记下。换地点、换队伍不会清零。</p>`+
      `<h3>其他</h3><p>提前召回：伙伴立刻回家，但这趟的材料、CP 和发现都拿不到。材料包满了，收获会留在归来篮里，之后再领，不会重复结算。地区采样（辨认后可选）：1 份基础材料换成本地区的地方材料。沿湾路标：在溪岸小集的岸边摊勾选，平安走完这一趟就能开放风湾盐田。顺路带货：另带几只伙伴去换盐花，平安回来才扣除，会占用 1 份基础材料。地区还没开放时，也可以沿它背后的老路走一趟，只带回材料和伙伴线索。</p>`+
      `${detail}`;
  }
  function shell(title,body,footer='') {
    showPanel(title,`<header class="journey-header"><button data-journey-back aria-label="${screen==='map'?'回厨房':'返回上一页'}">‹</button><h1>${screen==='map'?journeyArt('precision-ref-map-icon'):''}${esc(title)}</h1><button class="game-help" data-journey-help aria-label="寻访帮助">?</button></header><div class="regional-body journey-body scroll">${body}</div>${footer?`<footer class="regional-footer journey-footer">${footer}</footer>`:''}<dialog class="journey-help"><header><h2>寻访帮助</h2><button data-journey-help-close aria-label="关闭帮助">×</button></header><div class="journey-help-copy"></div></dialog>`,'screen-panel regional-screen journey-screen journey-'+screen);
    find('[data-journey-back]').onclick=()=>{if(screen==='map')goKitchen();else if(screen==='pick'){screen=pickReturn;render();}else if(screen==='confirm'){screen='region';render();}else{screen='map';tab='trip';render();}};
    find('[data-journey-help]').onclick=()=>{find('.journey-help-copy').innerHTML=helpBody();find('.journey-help').showModal();};
    find('[data-journey-help-close]').onclick=()=>{find('.journey-help').close();find('[data-journey-help]').focus();};
    all('[data-regional-tab]').forEach(b=>b.onclick=()=>open({tab:b.dataset.regionalTab}));
    all('[data-regional-region]').forEach(b=>b.onclick=()=>{selectRegion(b.dataset.regionalRegion);if(!tripActive())screen='map';render();});
    all('[data-regional-home]').forEach(b=>b.onclick=()=>goKitchen());
  }
  // map: stay on the map with that region selected (下一锅 / 线索册 「去寻访」); forKey: the partner it was opened for.
  function open(options={}){
    if(typeof options==='string')options={tab:options};
    forKey=options.forKey??null;
    if(options.regionId&&releasedRegions().includes(options.regionId))selectRegion(options.regionId);
    else if(!options.tab&&tripActive()){const id=regionOfTrip(getState().progress.trip);if(id&&releasedRegions().includes(id))selectRegion(id);}
    else if(!options.tab){const trail=trackedTrail(getState(),getNow());if(trail?.scout&&trail.open&&releasedRegions().includes(trail.region))selectRegion(trail.region);}
    if(options.recipeId)selectedMethod=options.recipeId;
    tab=options.tab??'trip';screen=tab==='record'?'record':options.map?'map':options.tab==='trip'||options.regionId?'region':'map';picking=false;render();
    if(Number.isInteger(options.materialId))find(`[data-regional-material="${options.materialId}"]`)?.scrollIntoView({block:'start'});
  }
  function render(){picking=false;if(tab==='record'){screen='record';record();}else if(screen==='map'||tripActive()&&screen!=='pick'){screen='map';world();}else if(screen==='confirm')confirmation();else trip();lastSignature=signature();}
  function signature(){const s=getState();return JSON.stringify([s.progress.trip,s.progress.knowledge,s.expansion.regions,s.expansion.discovery,s.expansion.methods,s.expansion.trial,s.expansion.prepareMode,s.ingredients,s.farm,s.total,s.cp,s.kitchenLevel,s.toolLevels]);}
  function refresh(){if(!find('.regional-screen')||picking||find('.journey-help[open]'))return;if(signature()!==lastSignature)render();else updateCountdown();}
  function updateCountdown(){const t=getState().progress.trip;all('[data-regional-countdown]').forEach(node=>{if(t?.status==='running')node.textContent=minutes(Math.max(0,Math.ceil((t.endAt-getNow())/60000)));});if(tripActive())positionTravellers(t);}
  function mapMarkup(s,t=null){
    const active=['running','returned'].includes(t?.status),destination=active?regionOfTrip(t):regionId,positions=journeyPositions,trail=active?null:trackedTrail(s,getNow());
    const path=journeyRoutePath;
    // One uniformly scaled stage: land, route, landmarks and party share the 390 x 684 reference space.
    return `<div class="journey-map-art"><div class="journey-map-stage" data-bleed><div class="journey-map-canvas"><div class="journey-map-land">${journeyEnvironment()}</div><svg class="journey-routes" data-allow-svg viewBox="0 0 390 684" preserveAspectRatio="none" aria-hidden="true"><path class="is-route-selected" d="${path(destination)}"/></svg>${releasedRegions().map(id=>{const v=regionView(s,id),[x,y]=positions[id];const here=trail?.scout&&trail.region===id,hinted=v.met&&regionHints(s,id).length>0;return `<button class="journey-node ${v.met?'':'is-locked'}" style="--x:${x}%;--y:${y}%" data-regional-region="${id}" aria-pressed="${regionId===id}" aria-label="${esc(v.name)}${v.met?'':'，尚未开放'}${here?`，追踪的 ${trail.code} 线索在这里`:''}${hinted?'，有订单情报':''}">${journeyArt('precision-ref-node-'+id)}${id===destination?journeyArt('flag','journey-flag'):''}${here?'<img class="journey-track-mark" src="/web/art/golden-ui/ic-star.png" alt="">':''}${hinted?`<img class="journey-hint-mark" src="${INTEL_ART}" alt="">`:''}<strong>${esc(v.name)}</strong>${!v.met?`<small>${journeyArt('lock')}未开放</small>`:''}</button>`;}).join('')}${active?`<div class="journey-travellers" data-trip-status="${t.status}">${t.members.map((k,i)=>`<span class="journey-traveller traveller-${i}">${portrait(k)}</span>`).join('')}</div>`:journeyArt('footprints','journey-map-start')}</div></div></div>`;
  }
  // 寻访 home: the map, and under it one card for the selected region — or, while a party is out, its trip.
  function world(){
    const s=getState(),t=s.progress.trip,active=tripActive(),away=active?regionOfTrip(t):null,here=!active||!releasedRegions().includes(away)||regionId===away;
    if(!active){members=fitTeam(s,members);if(!members.length&&autoTeam!==regionId){autoTeam=regionId;try{members=recommendedTeam(s);}catch{members=[];}}}
    const card=!here?regionCardMarkup(s,away):active?(t.status==='running'?travelCard(s,t):returnCard(s,t)):regionCardMarkup(s);
    shell('寻访',`${mapMarkup(s,t)}${card}<nav class="journey-small-links" aria-label="更多寻访入口"><button data-regional-tab="record">发现册</button>${active?'':'<button data-journey-enter>地点与方向</button>'}</nav>`);
    find('[data-journey-enter]')?.addEventListener('click',()=>{screen='region';tab='trip';render();});
    all('[data-regional-slot]').forEach(b=>b.onclick=()=>{pickReturn='map';pick(+b.dataset.regionalSlot);});
    find('[data-regional-go]')?.addEventListener('click',()=>{if(!members.length){pickReturn='map';pick(0);return;}if(!manualPlan)autoPlan();confirmation();});
    find('[data-regional-go-route]')?.addEventListener('click',()=>{if(!members.length){pickReturn='map';pick(0);return;}routeConfirm();});
    find('[data-regional-hint-more]')?.addEventListener('click',e=>{const b=e.currentTarget;b.setAttribute('aria-expanded',String(b.getAttribute('aria-expanded')!=='true'));});
    // 订单情报 「按提示」: go where the hint says, with companions that fit the find
    find('[data-regional-hint]')?.addEventListener('click',()=>{const h=regionHints(getState(),regionId)[0];if(!h)return;placeId=h.placeId;focus=h.focus;sampling=false;cargo={};guide=false;manualPlan=true;members=teamForHint(getState(),h);if(!members.length){pickReturn='map';pick(0);return;}confirmation();});
    if(active&&here)bindTrip(t);
    updateCountdown();
  }
  const meter=(art,label,n,of,tone)=>`<div class="jr-meter">${journeyArt(art)}<span>${label}</span><span class="jr-bar" role="img" aria-label="${label} ${n}/${of}" data-overhang-ok><i class="${tone}" style="width:calc(${of?Math.max(0,Math.min(1,n/of))*100:0}% - 4px)"${n?'':' hidden'}></i></span><b>${n}/${of}</b></div>`;
  const silhouette=(egg,id,on=true)=>`<span class="jr-sil">${on?`<span class="unknown-silhouette" aria-hidden="true">${characterPortrait(egg,id)}</span>`:'<span class="bk-q" aria-hidden="true">?</span>'}</span>`;
  // The tracked partner (or the one the 线索册 sent the player here for): where its next clue is, said on the card.
  function trailLine(s,c){
    const trail=trackedTrail(s,getNow());
    const line=(text,art,extra='')=>`<div class="jr-track"><img class="jr-star" src="/web/art/golden-ui/ic-star.png" alt=""><span>${esc(text)}</span>${art}${extra}</div>`;
    if(trail){
      const art=silhouette(trail.egg,trail.id);
      // a regional partner before its 方向: the specimen or story card it waits on is found here
      if(trail.find){const title=trail.find.trip?.cardId?CONTENT_TEXT[trail.find.trip.cardId]?.title:null;
        if(trail.region===regionId)return line(trail.find.kind==='intro'?`追踪 ${trail.code} · 第一次来这里就有进展`:`追踪 ${trail.code} · 在这里找「${title}」`,art);
        return line(`追踪 ${trail.code} · 要去${trail.regionName}`,'',`<button type="button" class="gd-btn2 mini" data-regional-region="${trail.region}">去${esc(trail.regionName)}</button>`);}
      // nothing left for trips to read: said on its own region's card only (on the others it was two lines of noise)
      if(!trail.scout)return trail.region===regionId?line(`追踪 ${trail.code} · 寻访能读的线索都读到了`,art):'';
      if(trail.region===regionId)return line(c.met||c.routeOnly?`追踪 ${trail.code} · 这里还能找到它的线索`:`追踪 ${trail.code} · 线索在这里，地区还没开放`,art);
      return line(`追踪 ${trail.code} · 线索在${trail.regionName}`,'',`<button type="button" class="gd-btn2 mini" data-regional-region="${trail.region}">去${esc(trail.regionName)}</button>`);
    }
    if(forKey&&clueRegionOf(forKey)?.region===regionId){const [egg,id]=forKey.split(':').map(Number);return line(`${speciesCode(forKey)} · 这里还能找到它的线索`,silhouette(egg,id));}
    return '';
  }
  // An order's 情报 about a find here: the spot and the find's own hint, and 「按提示」 to set the trip up for it. One line on
  // the card (device walkthrough: with the tracked line and the team it filled the card and squeezed the map); a tap on
  // the line shows the whole hint.
  function hintLine(s,c,go=true){
    if(!c.met)return '';const h=regionHints(s,regionId)[0];if(!h)return '';
    return `<div class="jr-track jr-hint"><img class="jr-star" src="${INTEL_ART}" alt=""><button type="button" class="jr-hint-text" data-regional-hint-more aria-expanded="false"><b>订单情报 · ${esc(h.placeName)}</b><i class="jr-hint-caret" aria-hidden="true"></i><span class="jr-hint-full">${esc(h.text)}</span></button>${go?'<button type="button" class="gd-btn2 mini" data-regional-hint>按提示</button>':''}</div>`;
  }
  // Companions for a hinted find: who meets its rule (trait and environment, one member may do both), best finders first,
  // then whoever already sits in the seats.
  function teamForHint(s,h){
    const traits=k=>{const c=resolveSpecies(k);return c?.exploration?.traits??c?.traits??[];};
    const pool=Object.keys(s.farm).filter(k=>(s.total[k]??0)>0&&SPECIES_ABILITIES[k]&&availableCount(s,k)>0).sort((a,b)=>SPECIES_ABILITIES[b].discover-SPECIES_ABILITIES[a].discover);
    const t=k=>!h.team.trait||traits(k).includes(h.team.trait),e=k=>!h.team.environment||SPECIES_ABILITIES[k].environment===h.team.environment;
    const out=[],take=k=>{if(k&&!out.includes(k)&&out.length<3)out.push(k);};
    take(pool.find(k=>t(k)&&e(k)));if(!out.length){take(pool.find(t));take(pool.find(e));}
    for(const k of [...members,...pool])take(k);
    return out;
  }
  // away: the region a party is out in, when this card is another region's (look only; one trip at a time)
  function regionCardMarkup(s,away=null){
    const c=regionCard(s,regionId,getNow());
    const mats=c.pool.slice(0,4).map(id=>`<span><img src="${materialArt(id)}" alt=""></span>`).join('');
    const head=`<div class="jr-head"><span class="jr-place">${journeyArt('precision-ref-node-'+regionId)}</span><div class="jr-title"><h2>${esc(c.name)}</h2><small>${minutes(Math.round(c.hours*60))} · 至少 ${c.minUnits} 份材料</small></div>${mats?`<span class="jr-mats" aria-label="常带回">${mats}</span>`:''}</div>`;
    const meters=`${meter('note','伙伴线索',c.clues.done,c.clues.total,'green')}${c.met?`${meter('pouch','新食材',c.materials.done,c.materials.total,'gold')}${meter('flag','特殊发现',c.finds.done,c.finds.total,'gold')}`:''}`;
    const recent=c.recent?`<p class="jr-recent">${journeyArt('magnifier')}<span>最近：${esc(c.recent)}</span></p>`:'';
    const seats=`<div class="jr-seats">${[0,1,2].map(i=>`<button type="button" class="jr-seat" data-regional-slot="${i}" aria-label="${members[i]?`更换${esc(name(members[i]))}`:`选择第${i+1}位同行伙伴`}"><span class="jr-seat-art">${members[i]?characterPortrait(...members[i].split(':').map(Number)):'<b aria-hidden="true">+</b>'}</span></button>`).join('')}</div>`;
    const home=s.progress.trip?.status==='returned';
    const go=away?`<p class="jr-note">${journeyArt('precision-timer-clock')}${esc(regionName(away))}那一队${home?'回来了，先收下再出发':'还在路上，回来后再出发'}</p><div class="jr-go-row jr-away-row"><button type="button" class="gd-btn2" data-regional-region="${away}">${home?`去收下${esc(regionShort(away))}的收获`:`看${esc(regionShort(away))}的行程`}</button></div>`
      :c.met?`<div class="jr-go-row">${seats}<button type="button" class="jr-go" data-regional-go>立即寻访</button></div>`
      :c.routeOnly?`<p class="jr-note">地区还没开放（还需 ${esc(c.missing.join('、'))}），这趟沿老路走：带回材料和伙伴线索</p><div class="jr-go-row">${seats}<button type="button" class="jr-go" data-regional-go-route>立即寻访</button></div>`
      :`<p class="regional-shortage jr-note">${journeyArt('lock')}开放还需：${c.missing.map(esc).join(' · ')}</p>`;
    // the tracked partner's clue is where the party already is: the away note says it, not a second 「去X」
    const trail=away&&trackedTrail(s,getNow())?.region===away?'':trailLine(s,c);
    return `<section class="jr-card" aria-label="${esc(c.name)}">${head}${meters}${recent}${trail}${hintLine(s,c,!away)}${go}</section>`;
  }
  function travelCard(s,t){
    const id=regionOfTrip(t),trail=trackedTrail(s,getNow()),lead=t.clueHit&&trail&&t.clueOrder?.[0]?.key===trail.key;
    return `<section class="jr-card jr-travel" aria-label="寻访中"><div class="jr-head"><span class="jr-place">${journeyArt('precision-ref-node-'+id)}</span><div class="jr-title"><h2>${esc(regionName(id))} · 寻访中</h2><small>还有 <span data-regional-countdown></span></small></div></div><div class="jr-party">${t.members.map(k=>`<span class="jr-face">${characterPortrait(...k.split(':').map(Number))}</span>`).join('')}</div>${lead?`<div class="jr-track"><img class="jr-star" src="/web/art/golden-ui/ic-star.png" alt=""><span>追踪 ${esc(trail.code)} · 这趟一定带回它的下一条线索</span>${silhouette(trail.egg,trail.id)}</div>`:''}<details class="jr-recall"><summary>行程管理</summary><p>提前召回拿不到这趟的材料、CP、线索和发现。</p><button data-regional-recall>提前召回</button></details></section>`;
  }
  // 寻访归来: what came home, the clue (whose, what, 调查 x/5 → y/5) and any regional find, then one button to take it all.
  function returnCard(s,t){
    const id=regionOfTrip(t),room=materialCapacity(s)-countMaterials(s),ticket=t.regional,card=ticket?.result?.cardId,definition=REGIONAL.cards.find(c=>c.id===card);
    const groups=Object.entries(t.remaining.reduce((a,m)=>(a[m]=(a[m]??0)+1,a),{}));
    const loot=`<div class="jr-loot">${groups.map(([m,n])=>{const v=projectMaterial(s,Number(m));return `<span class="jr-slot" role="img" aria-label="${esc(v.name)} ×${n}"><span class="jr-slot-art">${visualImage(v.known?materialArt(Number(m)):unknownArt)}</span><small>${esc(v.known?v.name:'未辨认')}${n>1?` ×${n}`:''}</small></span>`;}).join('')||'<p class="jr-note">材料已收好</p>'}</div>`;
    const adv=tripClueAdvance(s,t,getNow());
    const clue=adv?`<div class="jr-clue" aria-label="新线索 ${esc(adv.code)}：${esc(adv.text)}${adv.tracked?'，追踪中':''}">${silhouette(adv.egg,adv.id,adv.silhouette)}<span class="jr-clue-copy"><b>${esc(adv.code)}：${esc(adv.text)}</b><small>新线索 · 调查 ${adv.before}/5 → ${adv.complete?'配方完整':`${adv.after}/5`}</small></span><button type="button" class="gd-btn2 mini" data-regional-clue="${adv.key}">看线索</button></div>`:'';
    const protection=ticket&&!card&&['specimen','lore'].includes(ticket.focus)&&ticket.candidates.length?(s.expansion.cardProtection[id]?.[ticket.focus]??0):null;
    // a specimen not 辨认 yet is identified right here (free): its supply opens and its partners get their 方向
    const identify=definition?.type==='specimen'&&definition.material!=null&&!s.expansion.discovery.identified?.[definition.material];
    const find=definition?`<div class="jr-find"><span class="jr-find-art">${visualImage(definition.material!=null?materialArt(definition.material):discoveryArt(card),'stamp')}</span><span class="jr-clue-copy"><b>新发现 · ${esc(CONTENT_TEXT[card]?.title??card)}</b><small>${definition.type==='specimen'?identify?'标本已记进发现册 · 辨认后就能用':'标本已记进发现册':`${definition.type==='event'?'事件':'见闻'}已记进发现册`}</small></span>${identify?`<button type="button" class="gd-btn2 mini" data-regional-identify-find="${definition.material}">辨认</button>`:'<button type="button" class="gd-btn2 mini" data-regional-record>发现册</button>'}</div>`
      :protection!==null?`<p class="jr-note">${journeyArt('flag')}特殊发现 · ${protection>=3?'下一趟一定有':`最多再走 ${4-protection} 趟一定有`}</p>`:'';
    const notes=[ticket?.result?.guide?'找到沿湾路标 · 风湾盐田开放了':'',t.cargo?.outcome==='exchanged'?'带货换回盐花 ×1':'',ticket?.result?.methodId?'记下了一份地方做法':'',!t.cpProcessed&&t.cpReward?`还有 ${t.cpReward} CP`:''].filter(Boolean);
    const full=!room&&t.remaining.length?'<p class="regional-shortage jr-note">材料包满了，放不下的会留在这里</p>':'';
    const manage=`<details class="jr-manage"><summary>整理</summary><div class="jr-manage-row"><button type="button" class="gd-btn2 mini" data-regional-claim-notes>先领CP与线索</button>${t.remaining.length?`<button type="button" class="gd-btn2 mini" data-regional-discard>放弃剩余${t.remaining.length}份</button>`:''}</div></details>`;
    const names=t.members.map(name).join('、');
    return `<section class="jr-card jr-return regional-basket" aria-label="寻访归来"><div class="jr-head"><span class="jr-place">${journeyArt('pouch')}</span><div class="jr-title"><h2>${esc(regionName(id))} · 归来</h2><small>${esc(names)}回来了</small></div></div>${loot}${clue}${find}${notes.map(n=>`<p class="jr-note">${esc(n)}</p>`).join('')}${full}${manage}<div class="jr-go-row jr-return-go">${room>0||!t.remaining.length?kitButton2('再去一次','data-regional-again'):''}<button type="button" class="jr-go" data-regional-claim>收下</button></div></section>`;
  }
  function bindTrip(t){
    find('[data-regional-recall]')?.addEventListener('click',()=>confirmBox('现在召回，伙伴立即回家。这趟不会得到材料、CP、配方线索或地区新发现。',()=>transact(draft=>recall(draft,t.id,getNow()),()=>{screen='map';render();}),false,{yes:'召回伙伴'}));
    all('[data-regional-record]').forEach(b=>b.onclick=()=>open({regionId:regionOfTrip(t),tab:'record'}));
    find('[data-regional-identify-find]')?.addEventListener('click',e=>identifyNow(Number(e.currentTarget.dataset.regionalIdentifyFind)));
    // TRIP-1 (顺路拾财) shows its receipt with the CP it brought, as the old route page did.
    // note: what to tell after claiming (the CP line is added when there was any); one dialog, so the receipt stays on it
    const claim=(options,note='')=>{const r=transact(draft=>claimTrip(draft,t.id,options,getNow()),()=>{screen='map';render();});
      if(r&&(r.cp||note)){alertBox([r.cp?`这趟额外带回 ${r.cp} CP。`:'',note].filter(Boolean).join(''));if(r.cp)skillFeedback?.([{id:'TRIP-1',text:'这趟额外 +'+r.cp+' CP'}]);}return r;};
    find('[data-regional-claim]')?.addEventListener('click',()=>{const r=claim({});if(r?.remaining)alertBox(`材料包满了，还有 ${r.remaining} 份留在归来篮`);});
    find('[data-regional-clue]')?.addEventListener('click',e=>{const key=e.currentTarget.dataset.regionalClue;const r=claim({});if(!r)return;if(r.remaining)alertBox(`材料包满了，还有 ${r.remaining} 份留在归来篮`);openClueBook?.(key);});
    find('[data-regional-again]')?.addEventListener('click',()=>{const prev={regionId:regionOfTrip(t),placeId:t.regional?.placeId,focus:t.regional?.focus??focus,members:[...t.members]};const r=claim({});if(!r)return;if(r.remaining){alertBox(`材料包满了，还有 ${r.remaining} 份留在归来篮`);return;}selectRegion(prev.regionId);if(prev.placeId){placeId=prev.placeId;focus=prev.focus;manualPlan=true;}members=prev.members.filter(k=>availableCount(getState(),k)>0);autoTeam=prev.regionId;screen='map';render();});
    find('[data-regional-claim-notes]')?.addEventListener('click',()=>claim({materials:false},'CP与线索已领取，剩余材料继续留在篮里。'));
    find('[data-regional-discard]')?.addEventListener('click',()=>confirmBox(`放弃剩余${t.remaining.length}份材料，不会换成CP。已经获得的发现与方法记录保留。`,()=>claim({materials:false,discard:true}),false,{yes:'放弃这些材料'}));
  }
  // 辨认 a specimen and say what it opened: the material's supply and the partners that now have a 方向 (调查 x/5 → y/5),
  // the tracked one first; 看线索 opens the 线索册 on it.
  function identifyNow(id,after=render){
    const before=getState(),keys=regionalRows().filter(r=>resolveSpecies(r.key)?.unlock?.identifiedMaterials?.includes(id)&&!before.total?.[r.key]).map(r=>r.key);
    const was=new Map(keys.map(k=>[k,clueRow(before,k,getNow())?.progress??0]));
    if(transact(draft=>identifyMaterial(draft,id),after)===null)return;
    const s=getState(),tracked=trackedKey(s),rows=keys.map(k=>clueRow(s,k,getNow())).filter(r=>r&&r.progress>was.get(r.key)).sort((a,b)=>(b.key===tracked)-(a.key===tracked)||b.progress-a.progress);
    const material=REGIONAL.materials.find(m=>m.id===id),label=CONTENT_TEXT[material?.stableId]?.name??'';
    const lines=rows.slice(0,3).map(r=>`<div class="jr-clue"><span class="jr-sil">${r.silhouette?`<span class="unknown-silhouette" aria-hidden="true">${characterPortrait(r.egg,r.id)}</span>`:'<span class="bk-q" aria-hidden="true">?</span>'}</span><span class="jr-clue-copy"><b>${esc(r.code)} 有了做法方向</b><small>调查 ${was.get(r.key)}/5 → ${r.held?'做法齐了':`${r.progress}/5`}${r.key===tracked?' · 追踪中':''}</small></span></div>`).join('');
    const more=rows.length>3?`<small class="gd-note">还有 ${rows.length-3} 只也有了方向</small>`:'';
    kitDialog({title:'辨认好了',className:'regional-identified',body:`<div class="jr-find"><span class="jr-find-art"><img src="${materialArt(id)}" alt=""></span><span class="jr-clue-copy"><b>「${esc(label)}」</b><small>开放供货，商店能买了</small></span></div>${lines}${more}`,
      yes:rows.length&&openClueBook?'看线索':'好',no:rows.length&&openClueBook?'好':'',onYes:()=>{if(rows.length&&openClueBook)openClueBook(rows[0].key,()=>open({regionId,tab:screen==='record'?'record':undefined}));}});
  }
  // 立即寻访 picks where to go in the region: the bay guide when it can be followed, else a place and direction that still
  // has a find for this party (specimens first, then stories and events), else plain material gathering.
  function autoPlan(){
    const s=getState(),now=getNow(),view=regionView(s,regionId,{members});guide=false;
    // the tracked partner (or the one the 线索册 sent the player for) waits on a find here: go where it is, with a party
    // that meets its rule
    const findKey=[trackedKey(s),forKey].find(k=>k&&isRegionalKey(k)&&regionOfKey(k)===regionId);
    const step=findKey?regionalGate(s,findKey):null;
    if(step&&!step.met&&step.step.trip?.cardId&&!step.step.blocked.length){const card=REGIONAL.cards.find(c=>c.id===step.step.trip.cardId);placeId=card.placeId;focus=card.focus;sampling=false;cargo={};
      if(card.team.trait||card.team.environment){const team=teamForHint(s,{team:card.team});if(team.length)members=team;}return;}
    const hint=regionHints(s,regionId)[0];if(hint&&view.places.some(p=>p.id===hint.placeId)){placeId=hint.placeId;focus=hint.focus;sampling=false;cargo={};return;}
    if(regionId==='R'){try{if(regionalTripInfo(s,{regionId,placeId:'R:1',focus:'specimen',members},now).guide.available){placeId='R:1';guide=true;}}catch{}}
    let best=null;
    for(const f of ['specimen','lore'])for(const p of view.places){
      if(guide&&p.id!=='R:1')continue;
      let info;try{info=regionalTripInfo(s,{regionId,placeId:p.id,focus:f,members,guide:guide&&p.id==='R:1'},now);}catch{continue;}
      if(info.firstSpecimen){best={p:p.id,f};break;}
      if(info.candidates.length&&!best)best={p:p.id,f};
    }
    if(best){placeId=best.p;focus=best.f;}else{focus='materials';if(guide)placeId='R:1';}
    sampling=false;cargo={};
  }
  // A region not open yet: the old route behind it still brings materials and partner clues.
  function routeConfirm(){
    const s=getState(),route=REGION_ROUTE[regionId],info=explorationInfo(s,route,members,getNow(),{light});
    const body=`<div class="gd-plate" data-art="plate"><span data-safe>${esc(info.route.name)}</span></div><div class="gd-party" data-row>${members.map(k=>`<span class="gd-member" title="${esc(name(k))}">${journeyCharacter(k)}</span>`).join('')}</div><div class="gd-row gd-wrap">${kitChip(kitIcon.clock,minutes(Math.round(info.hours*60)),'mini')}</div><span class="gd-note">地区还没开放，这趟只带回材料和伙伴线索</span>`;
    kitDialog({title:'出发',label:'准备出发',className:'journey-confirm',body,no:'再看看',yes:'出发',yesAttrs:'data-journey-confirm',yesClass:'green',onYes:()=>transact(draft=>depart(draft,{routeId:route,members:[...members],light,directed:directed.filter(id=>id<75),priority:info.clues.slice(0,3).some(c=>c.key===priority)?priority:null},getNow()),()=>{screen='map';render();})});
  }
  function options(){const n=Object.values(cargo).reduce((a,b)=>a+b,0);return {regionId,placeId,focus,members:[...members],sampling,guide,cargo:n?{...cargo}:null,cargoOverrideKeepOne:!cargoKeep,light,directed:[...directed],priority};}
  const toggle=(on,attr,label)=>`<button type="button" class="jr-toggle" role="checkbox" aria-checked="${on}" ${attr}><i aria-hidden="true">${on?'✓':''}</i>${esc(label)}</button>`;
  // The trip skills the player has: shown only when learned, so the page stays as it was for everyone else.
  function skillOptions(s,info){
    const skills=s.progress.skills??{},rows=[],route=REGION_ROUTE[regionId];
    if(skills['TRIP-5']&&route!=='yard')rows.push(toggle(light,'data-regional-light',`轻装折返 · ${minutes(Math.round(info.route.hours*48))}，少带 1 份`));
    const pool=info.pool.filter(p=>p.unlocked);
    for(let x=0;x<info.directedUnits;x++)rows.push(`<div class="jr-opt-row" role="group" aria-label="指定第${x+1}份材料"><b>第${x+1}份</b>${[['','随机'],...pool.map(p=>[String(p.id),materialLabel(s,p.id)])].map(([v,l])=>`<button type="button" class="gd-btn2 mini" data-regional-directed="${x}" data-value="${v}" aria-pressed="${String(directed[x]??'')===v}">${esc(l)}</button>`).join('')}</div>`);
    if(skills['OBS-S']&&info.clues.length&&!info.trackedClue)rows.push(`<div class="jr-opt-row" role="group" aria-label="优先寻找的线索"><b>优先找</b><button type="button" class="gd-btn2 mini" data-regional-priority="" aria-pressed="${!priority}">顺路</button>${info.clues.slice(0,3).map(c=>`<button type="button" class="gd-btn2 mini" data-regional-priority="${c.key}" aria-pressed="${priority===c.key}">${speciesCode(c.key)}</button>`).join('')}</div>`);
    return rows.join('');
  }
  function infoNow(){return regionalTripInfo(getState(),options(),getNow());}
  function teamMarkup(){return `<div class="regional-team journey-team">${[0,1,2].map(i=>`<button data-regional-slot="${i}" aria-label="${members[i]?`更换${esc(name(members[i]))}`:`选择第${i+1}位同行伙伴`}">${journeySeat(members[i])}<strong>${members[i]?esc(name(members[i])):'选同行'}</strong><small>${members[i]?`采${SPECIES_ABILITIES[members[i]].gather} · 发${SPECIES_ABILITIES[members[i]].discover}`:'可留空'}</small></button>`).join('')}</div>`;}
  // The card named as this trip's target; the partner picker and preview reuse it.
  function currentTarget(info,view){
    const here=info.firstSpecimen?view.cards.filter(c=>c.id===info.introCardId):view.cards.filter(c=>c.placeId===placeId&&c.focus===focus),unfound=here.filter(c=>!c.found);
    return unfound.find(c=>info.candidates.some(x=>x.cardId===c.id))??unfound[0]??null;
  }
  // What this trip brings home, in plain words, before the player commits.
  function tripPreview(info,view){
    const intro=info.firstSpecimen?CONTENT_TEXT[REGIONAL.materials.find(m=>m.id===info.introMaterial)?.stableId]?.name:null;
    const here=view.cards.filter(c=>c.placeId===placeId&&c.focus===focus),chance=info.candidates.length?(view.protection[focus]??0)>=3?1:info.candidates.reduce((n,c)=>n+c.chance,0)/info.candidates.length:0;
    const sure=info.sureCardId?CONTENT_TEXT[info.sureCardId]?.title:null;
    const discovery=intro?`第一趟先带回「${intro}」标本，地点方向不限`:sure?`追踪 ${speciesCode(info.trackedFind.key)} · 这趟一定找到「${sure}」`:focus==='materials'?'这趟专心补材料，不找新发现':chance>=1?'这趟必定记下 1 张新发现（保底）':chance?`约 ${percent(chance)} 机会记下 1 张新发现`:here.length&&here.every(c=>c.found)?'这个方向都找到了，换个地点或方向看看':'这个方向暂时没有能找的发现';
    const clue=info.trackedClue?`追踪 ${speciesCode(info.trackedClue.key)} · 一定带回它的下一条线索`:info.clues.length?`${percent(info.clueChance)} 机会带回 1 条伙伴线索`:'这里暂时没有伙伴线索';
    return `<section class="journey-preview" aria-label="这一趟能带回什么"><p><strong>保证 ${info.minUnits} 份材料</strong>${members.length?` · ${percent(info.materialChance)} 机会再多 1 份`:' · 带上伙伴后还可能多带回'}</p><p class="jp-clue${info.trackedClue?' is-lead':''}">${esc(clue)}</p><p class="jp-find">${esc(discovery)}</p></section>`;
  }
  function targetMarkup(info,view){
    const target=currentTarget(info,view);
    const status=target?(target.found?'已发现':target.teamMet===false?'同行缺少：'+target.team.join('＋'):target.gateMissing.length?'还需：'+target.gateMissing.join(' / '):'待发现'):'';
    return `<div class="journey-target"><span class="journey-target-prefix">本次寻访目标：</span>${journeyArt(focus==='materials'&&!info.firstSpecimen?'pouch':target?.type==='event'?'flag':focus==='lore'?'note':'magnifier')}<span class="journey-target-copy"><strong>${esc(target?.title??(focus==='materials'?'沿路收集材料':'本方向已发现'))}</strong><small>${status?' · '+esc(status):''}</small></span><button data-regional-tab="record" aria-label="查看地区发现"></button></div>`;
  }
  function trip(){
    const s=getState();
    if(tripActive()){screen='map';world();return;}
    screen='region';keepOne=s.expansion?.inventoryPolicy?.keepOne!==false;members=fitTeam(s,members);
    if(!members.length&&autoTeam!==regionId){autoTeam=regionId;members=recommendedTeam(s);}
    const view=regionView(s,regionId,{members});if(!view.materials.some(m=>m.identified))sampling=false;
    const probe=regionalTripInfo(s,{regionId,placeId,focus,members,sampling},getNow());if(!probe.guide.available)guide=false;if(!probe.cargo.available)cargo={};
    const info=infoNow(),cargoTotal=Object.values(cargo).reduce((a,b)=>a+b,0);
    const places=view.places.map(p=>`<button data-regional-place="${p.id}" aria-pressed="${placeId===p.id}">${journeyArt('precision-hill','journey-place-hill')}${journeyArt('precision-ground','journey-place-ground')}${journeyArt((p.id.startsWith('V:')?'precision-ref-':'')+journeyPlace(p.id),'journey-place-object')}${placeId===p.id?journeyArt('flag','journey-place-flag'):''}<strong>${esc(p.name)}</strong></button>`).join('');
    const focusButtons=[['materials','precision-ref-action-pouch','补材料','收集沿路材料'],['specimen','precision-ref-action-magnifier','找标本','寻找地方食材'],['lore','precision-ref-action-note','寻见闻','听听当地故事']].map(([id,art,label,hint])=>`<button data-regional-focus="${id}" aria-pressed="${focus===id}">${journeyArt(art)}<strong>${label}</strong><small>${hint}</small></button>`).join('');
    const samplingField=view.materials.some(m=>m.identified)?toggle(sampling,'data-regional-sampling','地区采样'):'';
    const guideField=regionId==='R'&&placeId==='R:1'?(info.guide.done?'<p class="regional-success">风湾盐田已开放</p>':info.guide.available?toggle(guide,'data-regional-guide','追寻沿湾路标'):`<p class="regional-muted">沿湾路标 · 还需：${esc(info.guide.reason)}</p>`):'';
    const cargoRows=info.cargo.available?info.cargo.allowed.filter(k=>(s.total[k]??0)>0&&availableCount(s,k)>0).map(k=>`<div class="regional-cargo-row"><span>${esc(name(k))}<small> 可用 ${availableCount(s,k)}</small></span><span><button data-cargo-minus="${k}" aria-label="少带1只${esc(name(k))}" ${cargo[k]?'':'disabled'}>−</button><output>${cargo[k]??0}</output><button data-cargo-plus="${k}" aria-label="多带1只${esc(name(k))}" ${cargoTotal<info.cargo.quantity?'':'disabled'}>＋</button></span></div>`).join(''):'';
    const cargoField=info.cargo.available?`<details class="regional-cargo" ${cargoTotal?'open':''}><summary>顺路带货 · ${cargoTotal}/${info.cargo.quantity}只换盐花</summary>${toggle(cargoKeep,'data-cargo-keep','每种在家留1只')}${cargoRows}</details>`:'';
    shell(view.name,`<h2 class="journey-section-title">去哪里走走</h2><div class="journey-places region-${regionId}">${regionId==='V'?journeyArt('precision-ref-place-environment','journey-place-environment'):''}${places}</div>${!view.met?`<p class="regional-shortage journey-locked-message">${journeyArt('lock')}开放还需：${view.missing.map(esc).join(' · ')}</p>`:''}<section class="journey-actions"><h2 class="journey-section-title journey-action-title">可以做这些事</h2><div class="journey-focus" aria-label="追寻方向">${focusButtons}</div></section>${targetMarkup(info,view)}<div class="journey-team-label"><span>同行 ${members.length}/3${members.length?'':' · 至少带 1 位'}</span><button data-regional-last>上次队伍</button></div>${teamMarkup()}${tripPreview(info,view)}<div class="journey-options">${samplingField}${guideField}${cargoField}${skillOptions(s,info)}</div>${info.missing.filter(x=>!view.missing.includes(x)&&!x.includes('至少一位')).length?`<p class="regional-shortage">${info.missing.filter(x=>!view.missing.includes(x)&&!x.includes('至少一位')).map(esc).join('；')}</p>`:''}`,`<span class="regional-footer-caption">${journeyArt('precision-timer-clock')}${minutes(Math.round(info.hours*60))} · 免费</span><button class="journey-primary" data-regional-depart ${info.canDepart?'':'disabled'}>${members.length?'准备出发':'先选同行'}</button>`);
    all('[data-regional-place]').forEach(b=>b.onclick=()=>{placeId=b.dataset.regionalPlace;manualPlan=true;trip();});
    all('[data-regional-focus]').forEach(b=>b.onclick=()=>{focus=b.dataset.regionalFocus;manualPlan=true;trip();});
    all('[data-regional-slot]').forEach(b=>b.onclick=()=>{pickReturn='region';pick(+b.dataset.regionalSlot);});
    find('[data-regional-sampling]')?.addEventListener('click',()=>{sampling=!sampling;trip();});
    find('[data-regional-guide]')?.addEventListener('click',()=>{guide=!guide;manualPlan=true;trip();});
    find('[data-cargo-keep]')?.addEventListener('click',()=>{cargoKeep=!cargoKeep;trip();});
    find('[data-regional-light]')?.addEventListener('click',()=>{light=!light;trip();});
    all('[data-regional-directed]').forEach(b=>b.onclick=()=>{const x=Number(b.dataset.regionalDirected),v=b.dataset.value,next=[...directed];if(v==='')next.length=Math.min(next.length,x);else next[x]=Number(v);directed=next.filter(n=>Number.isInteger(n));trip();});
    all('[data-regional-priority]').forEach(b=>b.onclick=()=>{priority=b.dataset.regionalPriority||null;trip();});
    all('[data-cargo-plus]').forEach(b=>b.onclick=()=>{cargo[b.dataset.cargoPlus]=(cargo[b.dataset.cargoPlus]??0)+1;trip();});
    all('[data-cargo-minus]').forEach(b=>b.onclick=()=>{const k=b.dataset.cargoMinus;cargo[k]=Math.max(0,(cargo[k]??0)-1);if(!cargo[k])delete cargo[k];trip();});
    find('[data-regional-last]').onclick=()=>{members=fitTeam(s,s.progress.lastTeam);trip();};
    find('[data-regional-depart]').onclick=()=>confirmation();
  }
  function recommendedTeam(s){
    const room=k=>availableCount(s,k)>0,last=fitTeam(s,s.progress.lastTeam??[]);if(last.length)return last;
    const view=regionView(s,regionId,{members:[]}),target=currentTarget(infoNow(),view),need=target?.team.length?REGIONAL.cards.find(c=>c.id===target.id)?.team:null;
    const fits=k=>!!need&&((!!need.environment&&SPECIES_ABILITIES[k].environment===need.environment)||(!!need.trait&&(resolveSpecies(k)?.traits??[]).includes(need.trait)));
    const score=k=>(fits(k)?100:0)+(focus==='materials'?SPECIES_ABILITIES[k].gather:SPECIES_ABILITIES[k].discover)*10+Math.min(5,availableCount(s,k));
    return Object.keys(s.farm).filter(k=>(s.total[k]??0)>0&&SPECIES_ABILITIES[k]&&room(k)).sort((a,b)=>score(b)-score(a)).slice(0,3);
  }
  function pick(slot){
    screen='pick';picking=true;pickSlot=slot;const s=getState(),view=regionView(s,regionId,{members}),target=currentTarget(infoNow(),view);
    const need=target?.team.length?REGIONAL.cards.find(c=>c.id===target.id)?.team:null;
    const fits=k=>!!need&&((!!need.environment&&SPECIES_ABILITIES[k].environment===need.environment)||(!!need.trait&&(resolveSpecies(k)?.traits??[]).includes(need.trait)));
    const usedElsewhere=k=>members.filter((m,i)=>m===k&&i!==slot).length,available=Object.keys(s.farm).filter(k=>(s.total[k]??0)>0&&availableCount(s,k)>usedElsewhere(k)).sort((a,b)=>fits(b)-fits(a));
    const pips=n=>`<span class="jp-pips" aria-hidden="true"><span class="jp-bar"><span style="--w:${Math.round(Math.min(20,n)/20*100)}%"></span></span><span class="jp-n">${n}</span></span>`;
    shell('选择同行',`<div class="gd-row gd-wrap journey-picker-intro">${kitChip('','采集多 → 材料多','mini')}${kitChip('','发现高 → 新发现','mini')}${need?kitChip('',`目标要：${target.team.join('＋')}`,'mini hot'):''}</div><div class="journey-member-grid">${available.map(k=>`<button data-regional-member="${k}" aria-pressed="${members[slot]===k}">${fits(k)?'<em class="journey-fit">符合目标</em>':''}${journeySeat(k)}<strong>${esc(name(k))}</strong><span class="jp-stat" aria-label="采集 ${SPECIES_ABILITIES[k].gather}，发现 ${SPECIES_ABILITIES[k].discover}"><i>采集</i>${pips(SPECIES_ABILITIES[k].gather)}</span><span class="jp-stat" aria-hidden="true"><i>发现</i>${pips(SPECIES_ABILITIES[k].discover)}</span><small class="journey-env">${journeyArt('environment-'+SPECIES_ABILITIES[k].environment)}${esc(environmentLabel(SPECIES_ABILITIES[k].environment))} · 在家${availableCount(s,k)-usedElsewhere(k)}</small><small>${(resolveSpecies(k)?.traits??[]).map(traitLabel).map(esc).join(' / ')}</small></button>`).join('')||`<div class="journey-empty-state">${journeyArt('backpack')}<p>暂时没有可同行的伙伴</p><small>回厨房收取一些，再来看看。</small></div>`}</div>`,`${kitButton2('返回','data-regional-pick-back')}${members[slot]?kitButton2('空出这位','data-regional-remove'):''}`);
    all('[data-regional-member]').forEach(b=>b.onclick=()=>{members[slot]=b.dataset.regionalMember;members=members.filter(Boolean);screen=pickReturn;render();});
    find('[data-regional-pick-back]').onclick=()=>{screen=pickReturn;render();};
    find('[data-regional-remove]')?.addEventListener('click',()=>{members.splice(slot,1);screen=pickReturn;render();});
  }
  // Departure confirm: place, the party on their mats, time / cost / goal pills, and only the notes that matter.
  function confirmation(){
    const s=getState(),info=infoNow(),lastCopies=[...new Set(members)].filter(k=>availableCount(s,k)===members.filter(m=>m===k).length),carried=Object.values(cargo).reduce((a,b)=>a+b,0);
    const notes=[info.firstSpecimen?'第一趟一定带回入门标本':'',info.clueSure&&info.trackedClue?`追踪的 ${speciesCode(info.trackedClue.key)}：这趟一定带回它的下一条线索`:'',info.sureCardId?`追踪的 ${speciesCode(info.trackedFind.key)}：这趟一定找到它要的发现`:'',sampling?'顺手做地区采样':'',info.guideRequested?'追寻沿湾路标':'',info.cargoUsed?`另带 ${carried} 只，归来换盐花`:''].filter(Boolean);
    const alerts=[lastCopies.length?`${lastCopies.map(name).join('、')}会全部出门，回来前农场里看不到`:'',!info.canDepart?info.missing.join('；'):''].filter(Boolean);
    const body=`<div class="gd-place">${journeyArt(journeyPlace(placeId))}</div><div class="gd-plate" data-art="plate"><span data-safe>${esc(regionName(regionId))} · ${esc(placeName(placeId))}</span></div><div class="gd-party" data-row>${members.map(k=>`<span class="gd-member" title="${esc(name(k))}">${journeyCharacter(k)}</span>`).join('')}</div><div class="gd-row gd-wrap">${kitChip(kitIcon.clock,minutes(Math.round(info.hours*60)),'mini')}${kitChip(kitIcon.glass,focusLabel(focus),'mini')}</div>${notes.map(t=>`<span class="gd-note">${esc(t)}</span>`).join('')}${alerts.map(t=>`<span class="gd-alert regional-shortage">${esc(t)}</span>`).join('')}`;
    kitDialog({title:'出发',label:'准备出发',className:'journey-confirm',body,no:'再看看',yes:'出发',yesAttrs:'data-journey-confirm',yesClass:'green',yesDisabled:!info.canDepart,onYes:()=>transact(draft=>departRegional(draft,options(),getNow()),()=>{cargo={};guide=false;screen='map';render();})});
  }
  function outlook(s,info,view) {
    const free=materialCapacity(s)-countMaterials(s),protection=view.protection[focus]??0;
    const intro=info.firstSpecimen?CONTENT_TEXT[REGIONAL.materials.find(m=>m.id===info.introMaterial).stableId]?.name:null;
    const here=view.cards.filter(c=>c.placeId===placeId&&c.focus===focus);
    const chances=new Map(info.candidates.map(c=>[c.cardId,c.chance]));
    const rows=here.map(c=>`<li data-regional-card-preview="${c.id}">${visualImage(c.found?discoveryArt(c.id):unknownArt,"stamp")}<strong>${esc(CARD_TYPE[c.type])} · ${esc(c.title)}</strong> ${c.found?'<span class="regional-success">已记录</span>':chances.has(c.id)?`<span>${percent(chances.get(c.id))}机会</span>`:'<span class="regional-shortage">暂不符合</span>'}${!c.found?`<br><small>${esc(c.hint)}</small>${c.team.length?`<br><small>队伍条件：${c.team.map(esc).join('＋')}（同一位可同时满足）${c.teamMet===false?' · 当前队伍尚未满足':''}</small>`:''}${c.gateMissing.length?`<br><small class="regional-shortage">${c.gateMissing.map(esc).join('；')}</small>`:''}`:''}</li>`).join('');
    const discovery=intro?`这趟带回${intro}标本`:focus==='materials'?'本次专心补材料；新发现卡只在找标本或寻见闻时出现':info.candidates.length?`约 ${percent(info.candidates.reduce((n,c)=>n+c.chance,0)/info.candidates.length)} 机会记下1张新发现（每趟最多1张）`:here.length&&here.every(c=>c.found)?'本方向已完成，可继续补材料':'这个方向暂时没有能找的发现';
    return `<section class="regional-outlook"><h3>这一趟能带回什么</h3><p><strong>${info.minUnits}份材料保证</strong> · ${percent(info.materialChance)}机会再添1份</p><p>${info.clueSure?`伙伴线索：一定带回追踪的 ${speciesCode(info.trackedClue.key)} 的下一条`:info.clues?.length?`伙伴线索 ${percent(info.clueChance)} · 保底 ${s.progress.routeFailures[info.route.id]??0}/${info.hardAttempt-1}`:'伙伴线索：这里暂时没有可读的新线索'}</p><p class="regional-discovery-preview">${discovery}</p>${rows&&focus!=='materials'?`<details class="discovery-outlook"><summary>可寻找的发现 · ${here.length} 张</summary><ul class="regional-card-preview">${rows}</ul></details>`:''}${!info.firstSpecimen&&focus!=='materials'&&info.candidates.length?`<p>新发现保底 ${protection}/3 · 连续3趟没有新发现，第4趟只要还有能找的就必得；换地点、换队伍不清零</p>`:''}${info.firstSpecimen?`<small>试做材料替换1份基础材料；标本自动入册。${info.introOverridesPlace?'首次行程会先带你找到入门标本。':''}</small>`:''}${info.entryMissing?.length?`<p class="regional-shortage">要带回入门标本，还需：${info.entryMissing.map(esc).join('；')}</p>`:''}${free<info.minUnits?`<p class="regional-shortage">材料包还可放${free}份。放不下的材料留在归来篮中，标本照常记录。</p>`:''}${info.sureCardId?`<p class="regional-success">追踪的 ${speciesCode(info.trackedFind.key)}：这趟一定找到「${esc(CONTENT_TEXT[info.sureCardId]?.title??'')}」</p>`:''}</section>`;
  }
  // The detailed method defaults to something actionable, so returning to the
  // page always lands on the next real step instead of the first grid cell.
  function chooseMethod(view){
    const by=id=>view.methods.find(m=>m.recipeId===id),tracked=trackedKey(getState());
    return by(selectedMethod)??view.methods.find(m=>m.prepared)??view.methods.find(m=>m.key===tracked)??view.methods.find(m=>m.met&&!m.collected)??view.methods.find(m=>m.met)??view.methods.find(m=>m.direction&&!m.full)??view.methods[0];
  }
  // One partner of the region as a 线索册 card (loop batch 4): 调查 x/5, what is known, the next step, ☆ 追踪 and the
  // 线索册; held, its odds; met, its recipe and story.
  function methodDetail(m,s){
    const r=m.collected?null:clueRow(s,m.key,getNow());
    const targetName=m.collected?m.name:`图鉴 ${m.code} · 尚未收录`;
    const pips=r?`<span class="cb-pips" role="img" aria-label="调查 ${r.progress}/${CLUE_STEPS}">${Array.from({length:CLUE_STEPS},(_,i)=>`<i class="${i<r.progress?'on':''}"></i>`).join('')}</span>`:'';
    const art=m.collected?portrait(m.key):r?.silhouette?`<span class="unknown-silhouette" aria-hidden="true">${characterPortrait(m.egg,Number(m.key.split(':')[1]))}</span>`:visualImage(unknownArt,'unknown');
    const facts=m.collected||r?.held?`${m.egg?'鸭蛋':'鸡蛋'} · ${m.tool} · ${m.ingredients.join(' ＋ ')}`
      :r?[r.toolId!=null?`${m.egg?'鸭蛋':'鸡蛋'} · ${cookwareName(r.toolId)} Lv.${r.minLevel+1}`:'',r.first!=null?`第一味 ${materialLabel(s,r.first)}`:'',r.second?`第二味 ${r.second}类`:''].filter(Boolean).join(' · '):'';
    const trial=r?.held?r.trial?.inPot?'这锅正在试做，收取后揭晓':r.trial?.sure?'这锅一定出':`每锅 ${Math.round((r.trial?.chance??.25)*100)}% · 最多 ${r.trial?.left??4} 锅一定出`:'';
    const next=!r?'':r.held?(r.ready?trial:`还差：${r.need.slice(0,2).join('、')}`):r.status==='scout'?(r.scoutFind?`下一步：${r.scoutFind.text}`:`下一步：在${r.region.name}寻访，读${TRIP_LAYER_NAMES[r.scoutLevel]??'下一条线索'}`):r.need.length?`下一步：${r.need[0]}`:'';
    const star=r?`<button type="button" class="gd-btn2 mini" data-regional-track aria-pressed="${r.tracked}">${r.tracked?'取消追踪':'☆ 追踪'}</button>`:'';
    const identify=r?.identify!=null?`<button type="button" class="gd-btn2 mini" data-regional-identify-row="${r.identify}">辨认</button>`:'';
    // what the region's own conditions still ask for once the 方向 is known (kitchen, cookware, supply), as before
    const missing=m.direction||m.full?(m.missing??[]).filter(x=>!/补全这份地方做法/.test(x)):[];
    const clues=r&&openClueBook?`<button type="button" class="gd-btn2 mini" data-regional-cluebook>线索册</button>`:'';
    return `<section class="regional-method" data-regional-method-detail="${m.recipeId}"><div class="regional-target"><span class="rm-art">${art}</span><span><small>${esc(stageText(m,r))}${m.ornamental?' · 观赏，不上营业菜单':''}</small>${pips}<h3>${esc(targetName)}</h3></span></div>${(r&&!r.riddle)||!m.clue?'':`<p class="regional-muted">${esc(m.clue)}</p>`}${m.description?`<p>${esc(m.description)}</p>`:''}${facts?`<p>${esc(facts)}</p>`:''}${next?`<p class="regional-free-progress">${esc(next)}</p>`:''}${missing.length&&!r?.held?`<ul class="regional-requirements">${missing.map(x=>`<li>${esc(x)}</li>`).join('')}</ul>`:''}${star||identify||clues?`<div class="gd-row gd-wrap">${identify}${star}${clues}</div>`:''}</section>`;
  }
  function record() {
    const s=getState(),view=regionView(s,regionId),m=chooseMethod(view);selectedMethod=m.recipeId;
    const materials=view.materials.map(x=>`<section class="regional-specimen rc-card" data-regional-material="${x.id}"><span class="rc-state ${x.identified?'is-done':x.found?'is-ready':''}">${x.identified?'已辨认':x.found?'待辨认':'待发现'}</span><div class="material-portrait rc-art">${visualImage(x.found||x.identified?materialArt(x.id):unknownArt)}</div><h3>${x.found?esc(x.specimenName):esc(view.cards.find(c=>c.id===x.specimenCard)?.title)}</h3>${x.found&&!x.identified?`${kitChip('',x.name,'mini')}${kitButton('辨认',`data-regional-identify="${x.id}"`)}`:x.identified?`<div class="gd-row gd-wrap">${kitChip('',x.name,'mini good')}${kitChipHtml(`${kitIcon.coin}${x.price}/份`,'mini')}${x.used?kitChip('','用过了','mini good'):''}</div>`:kitButton2('去找','data-regional-to-trip')}<details class="field-notes gd-more"><summary>${x.found?"标本笔记":"去哪里找"}</summary><p>${x.found?esc(x.recognition):esc(view.cards.find(c=>c.id===x.specimenCard)?.hint)}</p>${x.identified?`<p>${esc(x.lore)}</p>`:""}</details></section>`).join('');
    const notes=view.cards.filter(c=>c.type!=='specimen').map(c=>`<li class="regional-note-card rc-note ${c.found?"is-found":"is-unfound"}" data-regional-card="${c.id}">${visualImage(c.found?discoveryArt(c.id):unknownArt,"stamp")}<details><summary><strong>${esc(c.title)}</strong><small>${esc(CARD_TYPE[c.type])} · ${esc(placeName(c.placeId))} · ${c.found?"已记录":"待发现"}</small></summary>${c.found?`<span class="regional-success"> 已记录</span><br><small>${esc(c.result)}</small>`:`<br><small>${esc(c.hint)}</small>${c.team.length?`<br><small>队伍：${c.team.map(esc).join('＋')}${c.team.length>1?'（一位伙伴可同时满足）':''}</small>`:''}${c.gateMissing.length?`<br><small class="regional-shortage">${c.gateMissing.map(esc).join('；')}</small>`:''}`}</details></li>`).join('');
    // the same picture as the 线索册: the portrait once met, the silhouette once read, else the unknown bag
    const grid=view.methods.map(x=>{const r=x.collected?null:clueRow(s,x.key,getNow()),label=stageText(x,r);return `<button class="regional-method-cell" data-regional-method="${x.recipeId}" aria-pressed="${x.recipeId===m.recipeId}" aria-label="${x.code} ${label}">${x.collected?portrait(x.key):r?.silhouette?`<span class="unknown-silhouette rm-cell-sil" aria-hidden="true">${characterPortrait(x.egg,Number(x.key.split(':')[1]))}</span>`:visualImage(unknownArt,"unknown")}<span>${x.code}</span><small>${x.collected?esc(x.name):label}</small></button>`;}).join('');
    const alternatives=view.alternatives.map(a=>`<section class="regional-alternative" data-regional-alternative="${a.id}"><h3>${a.known?esc(a.name):'地方替代做法 · 尚未记下'}</h3><p>${a.known?`效果同${a.targetName?esc(a.targetName):a.targetCode}的原配方 · ${esc(a.tool)} · ${a.ingredients.map(esc).join('＋')}`:`${a.unlockCard.startsWith('PJ-')?`完成项目「${esc(a.unlockName)}」`:`记下发现「${esc(CONTENT_TEXT[a.unlockCard]?.title??view.cards.find(c=>c.id===a.unlockCard)?.title??a.unlockCard)}」`}后，可以用地方材料替换原配料。`}</p>${a.known&&a.missing.length?`<ul class="regional-requirements">${a.missing.map(x=>`<li>${esc(x)}</li>`).join('')}</ul>`:''}${a.known?`<button data-regional-alt-prepare="${a.id}" ${a.met?'':'disabled'}>${a.prepared?'已准备 · 去厨房开火':'按替代做法准备下一锅'}</button><small>不额外安排目标；不能与地区试做、四时、礼物或点心保证叠加。</small>`:''}</section>`).join('');
    const prepareLabel=m.prepared?'去开火':m.collected?'再做一次':'去制作';
    const step=(art,label,on)=>`<span class="rc-step${on?' on':''}"><img src="${art}" alt=""><b>${label}</b></span>`;
    const stage=view.materials.some(x=>x.identified)?2:view.materials.some(x=>x.found)?1:0;
    const flow=`<div class="rc-flow" aria-label="标本 → 辨认 → 厨房试做">${step('/web/art/golden-journey/pouch.png','标本',stage===0)}<i>›</i>${step('/web/art/golden-journey/magnifier.png','辨认',stage===1)}<i>›</i>${step('/web/art/golden-kitchen/tool-1.png','试做',stage===2)}</div>`;
    shell(`${view.name}的发现册`,`<header class="regional-record-heading rc-head"><div class="gd-row">${kitChip('',`发现卡 ${view.counts.cards}/${view.cards.length}`,'mini')}${kitChip('',`新品 ${view.counts.collected}/${view.counts.total}`,'mini')}</div>${flow}</header>${kitLabel('标本')}<div class="rc-cards">${materials}</div><section class="regional-notes">${kitLabel('见闻与事件')}<ul class="rc-notes">${notes}</ul></section><section class="regional-methods">${kitLabel('地方做法')}<div class="regional-method-grid">${grid}</div></section>${methodDetail(m,s)}${alternatives}`,`${kitButton2('回到寻访','data-regional-to-trip')}${kitButton(prepareLabel,`data-regional-prepare${(m.full||m.collected)&&m.met?'':' disabled'}`)}`);
    all('[data-regional-to-trip]').forEach(b=>b.onclick=()=>open({tab:'trip'}));
    // on this page the card itself shows what 辨认 opened; the trip's return card says it in a note (identifyNow)
    all('[data-regional-identify]').forEach(b=>b.onclick=()=>transact(draft=>identifyMaterial(draft,Number(b.dataset.regionalIdentify)),record));
    find('[data-regional-identify-row]')?.addEventListener('click',e=>transact(draft=>identifyMaterial(draft,Number(e.currentTarget.dataset.regionalIdentifyRow)),record));
    all('[data-regional-method]').forEach(b=>b.onclick=()=>{selectedMethod=b.dataset.regionalMethod;record();find(`[data-regional-method-detail]`)?.scrollIntoView?.({block:'nearest'});});
    // ☆ 追踪 and the 线索册: the same investigation as everywhere else (the old 「设为寻访目标」 and 研读 live there now)
    find('[data-regional-track]')?.addEventListener('click',()=>{const on=trackedKey(getState())===m.key;transact(draft=>trackPartner(draft,on?null:m.key),record);});
    find('[data-regional-cluebook]')?.addEventListener('click',()=>openClueBook?.(m.key,()=>open({regionId,tab:'record',recipeId:m.recipeId})));
    const toKitchen=recipe=>{if(openRecipe)openRecipe(recipe);else goKitchen(REGIONAL.recipes.find(r=>r.id===recipe)?.toolId??REGIONAL.alternatives.find(a=>a.id===recipe)?.toolId);};
    find('[data-regional-prepare]').onclick=()=>transact(draft=>prepareRegionalRecipe(draft,m.recipeId),()=>toKitchen(m.recipeId));
    all('[data-regional-alt-prepare]').forEach(b=>b.onclick=()=>transact(draft=>prepareLocalAlternative(draft,b.dataset.regionalAltPrepare),()=>toKitchen(b.dataset.regionalAltPrepare)));
  }
  return {open,refresh};
}
