import {interfaceIcon} from '../../ui-icons.js';
import {resolveSprite,spriteSVG} from '../../art/manifest.js';
import {characterImage} from '../../catalog.js';
const kit=await (await fetch('./asset-manifest.json')).json();
const labels={house:'农舍 / 大入口',shop:'补给摊 / 开放棚架',shrine:'神社 / 祈愿铃',display:'陈列亭 / 三个空台座','tree-pine':'树 A · 分层松','tree-round':'树 B · 团冠树','bush-low':'灌木 A · 低矮','bush-tall':'灌木 B · 立簇','rock-wide':'石 A · 宽切面','rock-tall':'石 B · 竖切面','fence-front':'围栏 A · 正向段','fence-return':'围栏 B · 转向段',bench:'生活 · 木凳',basin:'生活 · 水盆',campfire:'生活 · 营火','edge-path':'边缘 · 土路','edge-grass':'边缘 · 草地','edge-stone':'边缘 · 排石'};
function asset(id){const a=kit[id];return `<svg class="asset-svg" viewBox="${a.frame.join(' ')}" aria-hidden="true"><image href="${a.file}" width="${a.width}" height="${a.height}"/></svg>`;}
for(const id of ['house','shop','shrine','display'])document.querySelector('#buildings').insertAdjacentHTML('beforeend',`<figure class="asset-card"><div class="checker big">${asset(id)}</div><figcaption><b>${labels[id]}</b><span>ImageGen · PNG / 1 个采用稿</span></figcaption></figure>`);
for(const id of Object.keys(kit).filter(k=>kit[k].kind==='environment'))document.querySelector('#environment').insertAdjacentHTML('beforeend',`<figure class="asset-card"><div class="checker small-asset">${asset(id)}</div><figcaption><b>${labels[id]}</b><span>独立 SVG · 无像素背景</span></figcaption></figure>`);
const common=[['tree-pine',16,116,90],['tree-round',72,80,88],['tree-pine',152,60,83],['tree-round',220,70,90],['tree-pine',310,106,91],['tree-round',380,147,98],['bush-low',45,137,52],['bush-tall',358,189,52],['rock-wide',30,245,42],['bush-low',6,307,65],['tree-pine',378,500,90],['bush-low',348,530,48],['tree-round',6,607,96],['bush-tall',46,627,50],['tree-pine',330,683,86],['bush-low',269,690,55],['rock-tall',379,613,43]];
const scenes=[
 {id:'a',title:'Test A · 主屋与补给',desc:'3 只现有鸡宝；一条路径连接两个入口，中央活动地由平面色块留出。',buildings:[['house',124,249,154],['shop',298,366,137]],
  props:[['bench',82,440,60],['basin',272,523,45],['rock-wide',300,607,38],['bush-low',233,273,48],['edge-stone',160,278,62]],characters:[[0,144,388,52],[3,220,448,52],[4,144,517,50]],
  ground:'<path d="M50 224Q165 213 242 299L310 390Q337 548 214 602L95 555Q24 390 50 224Z" fill="#c3d681"/><path d="M155 696Q132 628 98 577T80 468Q74 380 154 320T303 388" stroke="#bca165" stroke-width="50" fill="none" stroke-linecap="round"/><path d="M155 696Q132 628 98 577T80 468Q74 380 154 320T303 388M154 320L109 254" stroke="#e4c385" stroke-width="43" fill="none" stroke-linecap="round"/><path d="M140 360Q244 345 282 431T238 540Q151 584 111 514T140 360Z" fill="#cadb89"/>',
  destination:['农舍 · 收成','补给摊']},
 {id:'b',title:'Test B · 神社与陈列',desc:'2 只现有鸡宝；围栏与树簇建立路径边界，陈列位保持为空。',buildings:[['shrine',273,244,134],['display',129,403,170]],
  props:[['fence-front',65,252,96],['fence-return',340,441,65],['fence-return',337,509,65],['bush-low',47,446,55],['bench',218,510,58],['campfire',250,576,39],['rock-wide',74,568,48],['basin',147,616,42],['edge-stone',258,274,75]],characters:[[0,252,422,50],[3,161,528,51]],
  ground:'<path d="M112 166Q235 166 332 284L324 492 252 596 126 551 39 419Z" fill="#c4d483"/><path d="M208 696Q243 611 305 563T262 451Q220 432 225 351T257 247M225 421L116 418" stroke="#bca165" stroke-width="51" fill="none" stroke-linecap="round"/><path d="M208 696Q243 611 305 563T262 451Q220 432 225 351T257 247M225 421L116 418" stroke="#e4c385" stroke-width="44" fill="none" stroke-linecap="round"/>',
  destination:['神社','陈列亭']}
];
function instance([id,x,y,w],kind='environment'){
 const a=kit[id],h=w*a.frame[3]/a.frame[2],left=x-w/2,top=y-h;
 // Contact shadow is separate and uniform; the sprite itself is unmodified.
 return `<div class="object ${kind}" data-asset="${id}" data-foot-y="${y}" style="left:${left}px;top:${top}px;width:${w}px;height:${h}px;z-index:${Math.round(y)}"><i class="contact" style="width:${w*.66}px;height:${Math.max(6,w*.09)}px"></i>${asset(id)}<i class="anchor"></i></div>`;
}
function character([id,x,y,w]){return `<div class="object character" data-species="0:${id}" data-foot-y="${y}" style="left:${x-w/2}px;top:${y-w}px;width:${w}px;height:${w}px;z-index:${y}"><i class="contact" style="width:${w*.6}px;height:7px"></i>${spriteSVG(resolveSprite(characterImage(0,id)))}<i class="anchor"></i></div>`;}
const nav=[['kitchen','厨房'],['farm','农场'],['shop','生意'],['explore','寻访'],['book','图鉴']];
for(const d of scenes){
 let objects=[...common,...d.props].map(n=>instance(n)).join('')+d.buildings.map(n=>instance(n,'building')).join('')+d.characters.map(character).join('');
 const marks=[[55,175],[190,202],[354,288],[111,302],[180,568],[215,656],[308,469]].map(([x,y])=>`<path d="m${x} ${y} -3-6m3 6 3-8m-3 8 7-3" stroke="#88a44b" stroke-width="2.5" stroke-linecap="round" opacity=".42"/>`).join('');
 document.querySelector('#tests').insertAdjacentHTML('beforeend',`<article class="test" id="test-${d.id}"><header><h3>${d.title}</h3><p>${d.desc}</p></header><div class="screen"><div class="hud"><div><span class="coin">C</span> 1,240<strong>农场</strong><span class="settings">⚙</span></div><p>在家 ${d.characters.length} 只 <span>完好 100%</span></p></div><div class="world"><svg class="terrain" viewBox="0 0 390 696" aria-label="程序化地面"><rect width="390" height="696" fill="#b1c96a"/><path d="M0 0H390V135Q368 145 345 125Q320 150 289 125Q260 105 236 112Q211 124 183 103Q151 85 124 106Q94 126 67 113Q34 112 0 147Z" fill="#70974d"/><path d="M0 540 36 561 36 625 95 662 101 696H0ZM390 402 365 456 371 505 334 555 361 590 321 635 304 696H390Z" fill="#91b059"/>${d.ground}${marks}</svg>${objects}${d.buildings.map((n,i)=>`<span class="building-label" data-label="${n[0]}" style="left:${n[1]}px;top:${n[2]+10}px;z-index:800">${d.destination[i]} ›</span>`).join('')}<span class="test-badge">${d.id.toUpperCase()} · 模块拼装测试</span></div><nav>${nav.map(([icon,text])=>`<span class="${icon==='farm'?'active':''}">${interfaceIcon(icon)}<b>${text}</b></span>`).join('')}</nav></div></article>`);
}
const comparisons=[['塔塔 · 营地参考','reference/tata-02-camp.jpg'],['旧 B1 · 整图生成','../farm-b-scene-gate-20260928/deliverables/b1-390x844.png'],['旧 B2 · 整图生成','../farm-b-scene-gate-20260928/deliverables/b2-390x844.png'],['旧 B3 · 整图生成','../farm-b-scene-gate-20260928/deliverables/b3-390x844.png'],['Test A · 模块组装','evidence/test-a.png'],['Test B · 模块组装','evidence/test-b.png']];
const isCapture=new URLSearchParams(location.search).has('capture');
document.querySelector('#comparison').innerHTML=comparisons.map(([title,src],i)=>`<figure><figcaption>${title}</figcaption>${isCapture&&i>=4?`<div class="pending" data-src="${src}"></div>`:`<img src="${src}" alt="${title}">`}</figure>`).join('');
document.querySelectorAll('[data-toggle]').forEach(b=>b.addEventListener('click',()=>{const key=b.dataset.toggle,on=document.body.classList.toggle('toggle-'+key);b.setAttribute('aria-pressed',String(on));b.textContent=key==='anchors'?(on?'隐藏脚点':'显示脚点'):(on?'显示':'隐藏')+(key==='buildings'?'建筑层':'环境层');}));
window.assetGate={ready:true,assets:Object.keys(kit).length,buildingCount:4,environmentCount:14};
