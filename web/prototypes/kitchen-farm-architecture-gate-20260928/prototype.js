import {resolveSprite,spriteSVG} from '../../art/manifest.js';
import {interfaceIcon} from '../../ui-icons.js';

const asset='/web/prototypes/kitchen-farm-architecture-gate-20260928/evidence/';
const states={empty:'空锅 · 准备下一锅',incubating:'孵化进行中',ready:'部分可收',done:'全收完 · 安排收成'};
const regions={left:'农舍段',middle:'集市段',right:'神社段'};
const query=new URLSearchParams(location.search);
const shot=query.get('shot');
const view={tab:query.get('tab')==='farm'?'farm':'kitchen',state:states[query.get('state')]?query.get('state'):'empty',region:regions[query.get('region')]?query.get('region'):'left',health:query.get('health')==='low'?'low':'full',size:query.get('size')==='320'?320:390};
const icon=path=>{const sprite=resolveSprite(path);return spriteSVG({...sprite,size:sprite.size??[1312,1199]});};
const tool=icon('/web/art/cookware-a-v15.png#0');
const egg=icon('/web/art/egg-v4.png');
const nest=icon('/web/art/stage-bed-0-v6.png');
const nav=[['厨房','kitchen'],['农场','farm'],['生意','shop'],['寻访','explore'],['图鉴','book']];
const esc=s=>String(s).replaceAll('&','&amp;').replaceAll('<','&lt;').replaceAll('>','&gt;');

function hud(){return `<header class="hud"><button class="level" data-action="厨房升级"><span class="level-chick">${icon('/web/art/chick-v4-0.png')}</span><small>Lv.1</small></button><div class="wallet"><img src="/assets/png/MainGame/coin.png" alt=""><strong>1,240</strong></div><button class="gear" aria-label="设置" data-action="设置">${interfaceIcon('settings')}<small>设置</small></button></header>`;}
function bottomNav(active){return `<nav class="bottom-nav" aria-label="正式主导航（原型示意）">${nav.map(([label,key])=>`<button class="${active===key?'active':''}" data-action="切换到${label}">${interfaceIcon(key)}<span>${label}</span></button>`).join('')}</nav>`;}
function more(){return `<button class="more" data-action="厨房二级目录" aria-label="打开厨房二级目录">⋯<span>更多</span></button>`;}
function eggButtons(state){if(state==='empty'||state==='done')return '';return `<div class="eggs" aria-label="自然错落的蛋窝，24 枚蛋">${Array.from({length:24},(_,i)=>{
  const row=Math.floor(i/6),col=i%6,ready=state==='ready'&&[2,9,14,21].includes(i);
  const dx=[0,2,-1,2,-2,0][(i+row)%6],dy=[0,-1,1,0,2,-1][i%6];
  const eggMarkup=`<${ready?'button':'span'} class="nest-egg ${ready?'is-ready':''}" style="--col:${col};--row:${row};--dx:${dx}px;--dy:${dy}px" ${ready?`aria-label="可收取的蛋 ${i+1}" data-action="收取这只鸡宝"`:''}>${egg}${ready?'<span>可收</span>':''}</${ready?'button':'span'}>`;
  return eggMarkup;
}).join('')}</div>`;}
function kitchenContext(state,variant){
  if(state==='empty')return {headline:'蛋窝还空着',detail:'选好厨具和调味，开始这一锅',cta:'开始调理 · 24 枚蛋',mark:'准备中'};
  if(state==='incubating')return {headline:'这一锅正在孵化',detail:'剩余 62:38 · 24 枚蛋在窝里',cta:variant==='k3'?'开启孵化提醒':'查看孵化时间',mark:'孵化中'};
  if(state==='ready')return {headline:'有 4 只可以收取',detail:'轻点蛋窝里发亮的蛋 · 其余继续等',cta:'去蛋窝收取 · 4 只',mark:'可收 4 / 24'};
  return {headline:'这一锅收好啦',detail:'24 只已回家 · 收成可安排去向',cta:'安排本锅收成',mark:'已收完 24 / 24'};
}
function kitchenScene(variant,state){
  const d=kitchenContext(state,variant),isK3=variant==='k3';
  return `<div class="scene kitchen-scene ${variant} state-${state}">
    <div class="kitchen-status"><span class="batch-tag">这一锅 · ${d.mark}</span><button class="cleanliness" data-action="打扫厨房，当前清洁度 100%" aria-label="厨房清洁度 100%，打开打扫">${interfaceIcon('clean')}<span>清洁 100%</span></button></div>
    <div class="kitchen-props"><button class="seasoning-object" data-action="选择调味料" aria-label="选择调味料"><img src="/assets/png/Tool/Tool2/tool_2_0_0_0.png" alt=""><span>调味</span></button><button class="basket-object ${state==='done'?'emphasis':''}" data-action="${state==='done'?'安排本锅收成':'查看农场收成表'}" aria-label="${state==='done'?'安排本锅收成':'查看农场收成表'}"><img src="/web/art/basket-v4.png" alt=""><span>${state==='done'?'安排收成':'收成篮'}</span></button></div>
    <div class="nest-area"><div class="nest-art">${nest}</div>${eggButtons(state)}${state==='empty'?'<div class="nest-message">空蛋窝<br><small>准备下一锅</small></div>':''}${state==='done'?'<div class="nest-message done-message">本锅已收完<br><small>去篮子安排收成</small></div>':''}</div>
    ${!isK3?`<div class="nest-status"><b>${esc(d.headline)}</b><span>${esc(d.detail)}</span></div><div class="k1-worktable"><div class="worktable-head"><strong>下一锅工作台</strong>${more()}</div><div class="worktable-slots"><button class="slot" data-action="选择厨具"><span class="slot-icon">${tool}</span><span><small>当前厨具</small><b>保温灯 <i>›</i></b></span></button><button class="slot" data-action="选择调味料"><span class="slot-icon">✦</span><span><small>已选材料</small><b>未放调味 <i>›</i></b></span></button></div><button class="primary kitchen-primary" data-action="${esc(d.cta)}">${esc(d.cta)}</button></div>`:
    `<div class="k3-stage"><div class="recipe-rail"><button data-action="选择厨具">${tool}<span>保温灯 <i>›</i></span></button><button data-action="选择调味料"><span class="grain">✦</span><span>未放调味 <i>›</i></span></button>${more()}</div><div class="stage-panel"><div class="stage-copy"><span class="stage-kicker">${state==='empty'?'下一步':state==='done'?'这一锅':'当前阶段'}</span><strong>${esc(d.headline)}</strong><small>${esc(d.detail)}</small></div><button class="primary stage-cta" data-action="${esc(d.cta)}">${esc(d.cta)}</button></div></div>`}
  </div>`;
}
function farmSigns(variant){
  const f3=variant==='f3';
  return `<button class="farm-sign house-sign" data-action="打开农场收成表">${f3?'农舍':'收成表'}</button><button class="farm-sign market-sign" data-action="打开鸡宝小卖部">${f3?'集市':'商店'}</button><button class="farm-sign display-sign" data-action="打开陈列架（次级）">陈列架</button><button class="farm-sign shrine-sign" data-action="打开神社">神社</button>`;
}
function farmContext(variant,region,health){
  const section={left:{title:'农舍',cta:'查看收成表',desc:'在家 3 只 · 1 种'},middle:{title:'集市',cta:'进入小卖部',desc:'购买厨具与调味料'},right:{title:'神社',cta:'查看委托簿',desc:'来信、礼物与活动'}}[region];
  const low=health==='low';
  if(variant==='f1')return `<div class="f1-wayfinding"><span class="way-label">${region==='left'?'右侧还有集市和神社 →':region==='middle'?'← 农舍 · 右侧还有神社 →':'← 农舍与集市'}</span><div class="way-dots"><i class="${region==='left'?'on':''}"></i><i class="${region==='middle'?'on':''}"></i><i class="${region==='right'?'on':''}"></i></div></div>${region==='right'?'<div class="shrine-choices"><span>神社里</span><button data-action="神社委托簿">看委托簿</button><button data-action="每日御神签">求御神签</button></div>':''}${low?'<button class="repair-nudge" data-action="整修农场">农场需整修 · 完好 24% <b>去整修 ›</b></button>':'<div class="farm-rest-note">夜里在家，草地上暂时看不见它们</div>'}`;
  return `${region==='right'&&low?'<button class="f3-repair-strip" data-action="整修农场">农场需整修 · 完好 24% <b>去整修 ›</b></button>':''}<div class="f3-focus"><div class="focus-top"><span class="focus-place">${section.title} <small>${region==='left'?'1':region==='middle'?'2':'3'} / 3</small></span><span class="focus-rule">${section.desc}</span></div><div class="focus-actions"><button class="primary" data-action="${section.cta}">${section.cta}</button>${region==='right'?'<button class="secondary" data-action="每日御神签">求御神签</button>':low?'<button class="secondary repair-small" data-action="整修农场">整修 24%</button>':'<span class="quiet-health">完好 100%</span>'}</div></div>`;
}
function farmScene(variant,region,health){
  return `<div class="scene farm-scene ${variant} region-${region} health-${health}">
    <div class="farm-world">${farmSigns(variant)}</div>
    <div class="farm-status"><div class="stock"><b>在家 3 只 · 1 种</b><small>夜间休息，草地暂未露面</small></div><div class="health ${health==='low'?'warn':''}">${health==='low'?'完好 24%':'完好 100%'}</div></div>
    ${variant==='f3'?`<div class="chapter-cue"><span>农舍</span><i></i><span>集市</span><i></i><span>神社</span></div>`:''}
    <button class="scene-arrow left-arrow" data-shift="-1" aria-label="向左查看农场">‹</button><button class="scene-arrow right-arrow" data-shift="1" aria-label="向右查看农场">›</button>
    ${farmContext(variant,region,health)}
  </div>`;
}
function phone(variant,{state=view.state,region=view.region,health=view.health,size=view.size}={}){
  const kitchen=variant.startsWith('k');
  return `<div class="phone ${size===320?'small':''}" data-variant="${variant}" data-state="${state}" data-region="${region}" data-health="${health}" style="--phone-w:${size}px;--phone-h:${size===320?568:844}px"><div class="screen">${hud()}${kitchen?kitchenScene(variant,state):farmScene(variant,region,health)}${bottomNav(kitchen?'kitchen':'farm')}<div class="prototype-toast" role="status" aria-live="polite"></div></div></div>`;
}
function galleryCard(title,path,note=''){return `<figure class="shot-card"><img src="${asset+path}" alt="${esc(title)}" loading="lazy"><figcaption><strong>${title}</strong>${note?`<span>${note}</span>`:''}</figcaption></figure>`;}
function renderBoard(){
  const k=view.tab==='kitchen',labels=k?['K1 · 蛋窝中心 + 底部工作台','K3 · 阶段焦点切换']:['F1 · 建筑即入口','F3 · 横向章节 + 随段焦点'];
  const before=k?'before-kitchen-390x844.png':`before-farm-${view.region}-390x844.png`;
  const first=k?'k1':'f1',second=k?'k3':'f3';
  document.querySelector('#app').innerHTML=`<header class="board-head"><div><span class="eyebrow">EXPERIMENT / UI ARCHITECTURE VISUAL GATE</span><h1>鸡宝厨房 <em>Kitchen / Farm</em></h1><p>正式素材的结构重排试验 · 不接入 Runtime · 不决定最终美术</p></div><div class="board-mark">4 套结构<br><strong>2 种手机尺寸</strong></div></header>
    <main class="board-main"><section class="control-panel"><div class="tab-control"><button data-set="tab:kitchen" class="${k?'selected':''}">Kitchen</button><button data-set="tab:farm" class="${!k?'selected':''}">Farm</button></div><div class="control-row"><span>${k?'循环状态':'横向区域'}</span><div class="segmented">${Object.entries(k?states:regions).map(([id,label])=>`<button data-set="${k?'state':'region'}:${id}" class="${(k?view.state:view.region)===id?'selected':''}">${label.split(' · ')[0]}</button>`).join('')}</div></div><div class="control-row"><span>视口</span><div class="segmented"><button data-set="size:390" class="${view.size===390?'selected':''}">390×844</button><button data-set="size:320" class="${view.size===320?'selected':''}">320×568</button></div>${!k?`<span class="health-label">完好度</span><div class="segmented"><button data-set="health:full" class="${view.health==='full'?'selected':''}">100%</button><button data-set="health:low" class="${view.health==='low'?'selected':''}">需整修</button></div>`:''}</div><p class="control-note">切换控件观察同一骨架的状态；手机内按钮展示入口用途，不写入玩家存档。</p></section>
    <section class="compare-section"><div class="section-title"><span>01 / 同屏比较</span><h2>${k?'Kitchen':'Farm'} · Before / ${first.toUpperCase()} / ${second.toUpperCase()}</h2><p>${k?'主操作如何跟随“准备—孵化—收取—分配”移动':'建筑、区域提示与当前操作如何绑定'}</p></div><div class="compare-grid"><article class="compare-cell"><div class="cell-heading"><b>BEFORE</b><span>${view.size===320?'正式 390 参考缩放':'正式 Runtime 截图'}</span></div><div class="before-wrap ${view.size===320?'small':''}"><img src="${asset+before}" alt="当前正式 Runtime 主界面"></div></article><article class="compare-cell"><div class="cell-heading"><b>${first.toUpperCase()}</b><span>${labels[0]}</span></div>${phone(first)}</article><article class="compare-cell"><div class="cell-heading"><b>${second.toUpperCase()}</b><span>${labels[1]}</span></div>${phone(second)}</article></div></section>
    ${k?`<section class="gallery-section"><div class="section-title"><span>02 / 完整循环</span><h2>K1 与 K3 · 四种阶段</h2><p>每张都是完整 390×844 主屏；下方另列 320×568 压力测试。</p></div><div class="state-gallery">${['k1','k3'].map(v=>`<div class="gallery-row"><h3>${v.toUpperCase()}</h3><div>${Object.entries(states).map(([s,label])=>galleryCard(label,`${v}-${s}-390.png`)).join('')}</div></div>`).join('')}</div></section><section class="gallery-section"><div class="section-title"><span>03 / 小屏</span><h2>320×568 验证</h2></div><div class="small-gallery">${['k1','k3'].flatMap(v=>['empty','ready'].map(s=>galleryCard(`${v.toUpperCase()} · ${states[s]}`,`${v}-${s}-320.png`))).join('')}</div></section>`:
    `<section class="gallery-section"><div class="section-title"><span>02 / 横向完整关系</span><h2>F1 与 F3 · 左 / 中 / 右</h2><p>同一 Farm 正式夜景素材的连续裁切；无新增收集内容。</p></div><div class="state-gallery">${['f1','f3'].map(v=>`<div class="gallery-row"><h3>${v.toUpperCase()}</h3><div>${Object.entries(regions).map(([s,label])=>galleryCard(label,`${v}-${s}-390.png`)).join('')}</div></div>`).join('')}</div></section><section class="gallery-section"><div class="section-title"><span>03 / 小屏与维护状态</span><h2>320×568 · 完好 100% / 需整修</h2></div><div class="small-gallery">${['f1','f3'].flatMap(v=>['left','middle','right'].map(s=>galleryCard(`${v.toUpperCase()} · ${regions[s]}`,`${v}-${s}-320.png`))).join('')}${['f1','f3'].map(v=>galleryCard(`${v.toUpperCase()} · 需整修`,`${v}-repair-390.png`)).join('')}${galleryCard('F3 · 神社段需整修','f3-right-repair-390.png')}</div></section>`}
    <section class="decision-section"><div class="section-title"><span>04 / 决策记录</span><h2>取舍、风险与留待 Creative Direction 的问题</h2></div><div class="decision-grid">${k?`<article><h3>K1 · 固定工作台</h3><p>厨具、材料与主按钮位置稳定；蛋窝与计时保持直连。小屏下工作台可能挤压蛋窝，要保住蛋触点和正文长度。</p></article><article><h3>K3 · 阶段焦点</h3><p>每阶段只突出当前行动；厨具与调味始终留在固定细轨。阶段切换时必须维持操作记忆，避免用户找不到下一锅配置。</p></article>`:`<article><h3>F1 · 建筑即入口</h3><p>入口与空间最一致；右侧可发现性依赖方向提示，神社在该段才展开委托与御神签。低完好度提示独立出现。</p></article><article><h3>F3 · 横向章节</h3><p>当前区域与行动对应最明确；需要稳定判定当前段，也要避免底部焦点区重新堆成管理面板。</p></article>`}<article><h3>技术实现风险</h3><p>${k?'24 枚蛋的热区在 320 宽下接近 40px；透明触点、篮子与工作台不可互相遮盖。模拟状态须映射真实 batch/clean/lock 条件。':'横向拖动与建筑点击要区分；固定层不能遮住场景目标。昼夜可见规则、库存和完好度要用真实状态驱动。'}</p></article><article><h3>下一 Gate 再决定</h3><p>字体细节、按钮质感、材料纹理、环境光、建筑/蛋窝的正式绘制、动效节奏和全等级视觉延展。</p></article></div></section></main><footer class="board-footer">PROTOTYPE / EXPERIMENT · 仅用于结构选择 · 2026-09-28</footer>`;
}
function showToast(button){const phone=button.closest('.phone');if(!phone)return;const toast=phone.querySelector('.prototype-toast');toast.textContent=`结构示意：${button.dataset.action}`;toast.classList.add('show');clearTimeout(toast.timer);toast.timer=setTimeout(()=>toast.classList.remove('show'),2200);}
function bind(){document.addEventListener('click',event=>{
  const set=event.target.closest('[data-set]');if(set){const [k,v]=set.dataset.set.split(':');view[k]=k==='size'?Number(v):v;renderBoard();return;}
  const shift=event.target.closest('[data-shift]');if(shift){const order=['left','middle','right'],next=Math.max(0,Math.min(2,order.indexOf(view.region)+Number(shift.dataset.shift)));view.region=order[next];renderBoard();return;}
  const action=event.target.closest('[data-action]');if(action)showToast(action);
});
let pointer=null;document.addEventListener('pointerdown',e=>{if(!e.target.closest('.farm-scene')||e.target.closest('button'))return;pointer={x:e.clientX,y:e.clientY};});document.addEventListener('pointerup',e=>{if(!pointer)return;const dx=e.clientX-pointer.x;if(Math.abs(dx)>35){const order=['left','middle','right'];view.region=order[Math.max(0,Math.min(2,order.indexOf(view.region)+(dx<0?1:-1)))];renderBoard();}pointer=null;});}
if(shot){document.body.classList.add('shot-mode');document.querySelector('#app').innerHTML=phone(shot,view);bind();}
else {renderBoard();bind();}
