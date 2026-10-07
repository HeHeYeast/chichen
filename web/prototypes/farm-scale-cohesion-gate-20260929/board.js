import {interfaceIcon} from '../../ui-icons.js';
import {resolveSprite,spriteSVG} from '../../art/manifest.js';
import {characterImage} from '../../catalog.js';
import {SCALE,px} from './scale.js';
const kit=await (await fetch('./asset-manifest.json')).json();
const buildings=[['house',107,232],['shop',290,254],['shrine',296,586],['display',103,586]].map(([id,x,y])=>[id,x,y,px(SCALE.buildingUnits[id])]);
const boundary=[
 ['tree-round',-15,63,76],['tree-pine',36,87,74],['tree-round',87,69,76],['tree-pine',141,53,68],['tree-round',199,84,76],['tree-pine',254,65,75],['tree-round',313,77,76],['tree-pine',370,93,74],
 ['tree-pine',5,140,72],['tree-round',75,123,68],['tree-round',168,102,69],['tree-pine',251,137,70],['tree-round',343,144,73],['tree-round',405,191,74],
 ['bush-low',194,148,36],['bush-low',326,155,40],['tree-pine',-10,241,67],['bush-tall',17,277,35],['tree-pine',386,300,71],['bush-low',356,338,34],
 ['bush-low',9,363,39],['rock-wide',25,401,26],['tree-round',-14,473,73],['bush-tall',15,495,32],['tree-round',399,439,74],['bush-low',363,468,33],['rock-tall',379,530,27],
 ['tree-pine',-2,575,70],['tree-round',372,626,74],['bush-tall',346,634,36],['tree-round',21,673,72],['bush-low',60,690,41],['tree-pine',114,724,74],['tree-round',278,723,76],['tree-pine',350,727,70],['bush-low',318,688,43]
];
const props=[['fence-front',44,193,47],['fence-front',202,194,47],['fence-return',353,533,45],['fence-front',61,631,52],['fence-return',339,665,45],['bench',44,454,37],['basin',338,444,25],['bush-low',58,515,30],['bush-low',200,576,29],['rock-wide',353,581,22],['rock-wide',75,677,23],['edge-stone',106,240,43],['edge-stone',293,263,32],['edge-stone',294,593,36],['edge-stone',100,593,40]];
const chickens=[[0,141,334],[3,238,380],[4,296,433],[0,154,469],[3,282,312]].map((c,i)=>[...c,px(SCALE.chickUnits[i])]);
const asset=id=>{const a=kit[id];return `<svg class="asset-svg" viewBox="${a.frame.join(' ')}" aria-hidden="true"><image href="${a.file}" width="${a.width}" height="${a.height}"/></svg>`;};
function object([id,x,y,w],kind='environment'){
 const a=kit[id],h=w*a.frame[3]/a.frame[2],sh=kind==='building'?w*.045:Math.max(2.2,w*.054);
 return `<div class="object ${kind}" data-asset="${id}" data-foot-y="${y}" style="left:${x-w/2}px;top:${y-h}px;width:${w}px;height:${h}px;z-index:${Math.round(y)}"><i class="contact" style="width:${w*(kind==='building'?.7:.61)}px;height:${sh}px"></i>${asset(id)}</div>`;
}
function chicken([id,x,y,h]){
 const s=resolveSprite(characterImage(0,id)),w=h*s.frame[2]/s.frame[3];
 return `<div class="object character" data-species="0:${id}" data-foot-y="${y}" style="left:${x-w/2}px;top:${y-h}px;width:${w}px;height:${h}px;z-index:${y}"><i class="contact" style="width:${w*.65}px;height:3px"></i>${spriteSVG(s)}</div>`;
}
const mainRoad='M184 712C183 672 199 648 213 622L225 556';
const road='M225 556C173 550 139 526 115 490C80 441 63 402 78 353C91 310 111 273 107 235M225 556C271 542 308 496 318 445C330 381 317 308 290 254';
const branches='M206 622Q157 620 104 587M213 622Q261 615 296 587';
const roads=[['main',mainRoad],['link',road],['entry',branches]];
// Paint every edge first, then every surface so connected lanes have no dark seams.
const roadLayer=roads.map(([kind,d])=>`<path d="${d}" stroke="#ad9659" stroke-width="${px(SCALE.roadUnits[kind])+3}"/>`).join('')+roads.map(([kind,d])=>`<path data-road="${kind}" d="${d}" stroke="#d9b779" stroke-width="${px(SCALE.roadUnits[kind])}"/>`).join('');
const grass=[ [31,311],[347,366],[125,175],[215,220],[251,648],[48,562],[125,489],[280,425],[182,289],[132,397],[326,213],[315,644],[213,517] ];
const turf=grass.map(([x,y])=>`<path d="m${x} ${y} -3-5m3 5 1-6m-1 6 5-3" fill="none" stroke="#8cac50" stroke-width="1.5" stroke-linecap="round" opacity=".58"/>`).join('');
const names={house:'农舍',shop:'补给摊',shrine:'神社',display:'陈列亭'};
const nav=[['kitchen','厨房'],['farm','农场'],['shop','生意'],['explore','寻访'],['book','图鉴']];
document.querySelector('#camp').innerHTML=`<header class="hud"><div><span class="coin">C</span>1,240<strong>农场</strong><span class="settings">⚙</span></div><p>在家 5 只<span>完好 100%</span></p></header><div class="world" aria-label="四座独立功能建筑围绕中央伙伴活动区的营地">
<svg class="terrain" viewBox="0 0 390 696" aria-label="程序化草地与连接四个入口的支路">
<rect width="390" height="696" fill="#a9c462"/>
<path d="M0 0H390V161Q348 155 335 141L299 155Q265 147 244 158L211 139Q181 152 163 138L132 151Q95 133 66 148L0 173Z" fill="#598044"/>
<path d="M0 155 36 171 32 290 19 340 28 416 17 462 33 527 42 583 84 635 126 659 137 696H0ZM390 150 357 166 358 284 345 323 364 374 347 447 352 520 328 593 303 642 262 671 263 696H390Z" fill="#7fa34e"/>
<path d="M48 240Q135 207 220 245 316 246 338 343L329 493Q309 572 233 609L96 593Q42 525 43 441Z" fill="#b6cc72"/>
<g fill="none" stroke-linecap="round" stroke-linejoin="round">${roadLayer}</g>
<path d="M136 284Q190 260 242 293L273 335 286 390Q298 441 258 477L204 509Q168 502 150 476L126 422Q106 374 119 334Z" fill="#bdd17b"/>
<path d="M270 519 280 509M81 405 84 416M183 659 188 645" fill="none" stroke="#e6c894" stroke-width="2" stroke-linecap="round"/>
${turf}</svg>
${boundary.map(n=>object(n)).join('')}${props.map(n=>object(n)).join('')}${buildings.map(n=>object(n,'building')).join('')}${chickens.map(chicken).join('')}
${buildings.map(n=>`<span class="building-label" style="left:${n[1]}px;top:${n[2]+5}px">${names[n[0]]}<small>›</small></span>`).join('')}
<svg class="guides" viewBox="0 0 390 696"><rect x="86" y="277" width="220" height="210"/><text x="99" y="294">活动范围 ≈ 8.5U × 8.1U</text><rect x="29" y="102" width="156" height="130"/><text x="34" y="96">农舍宽 6U / 高约 5U</text><rect x="124" y="308" width="29" height="27"/><text x="157" y="330">鸡宝 ≈ 1U</text><text x="95" y="656">主路 1.4U / 支路 1–1.15U</text><text x="96" y="679">1U = 26px · 屏幕比例尺</text></svg>
</div><nav>${nav.map(([icon,label])=>`<span class="${icon==='farm'?'active':''}">${interfaceIcon(icon)}<b>${label}</b></span>`).join('')}</nav>`;
const ruler=[...buildings.map(([id,,,w])=>[id,w,names[id]]),['tree-round',66,'树'],['bush-low',36,'灌木'],['rock-wide',24,'石'],['fence-front',48,'围栏'],['bench',37,'木凳'],['basin',25,'水盆'],['campfire',24,'营火']];
document.querySelector('#ruler').innerHTML=ruler.map(([id,w,label])=>`<figure><div class="sample" style="width:${w}px;height:${w*kit[id].frame[3]/kit[id].frame[2]}px">${asset(id)}</div><figcaption>${label}<br>${w}px 宽</figcaption></figure>`).join('')+`<figure><div class="sample" style="width:27px;height:26px">${spriteSVG(resolveSprite(characterImage(0,0)))}</div><figcaption>鸡宝<br>约 26px 高</figcaption></figure>`;
const capture=new URLSearchParams(location.search).has('capture');
const compare=[['塔塔营地 / 组织参考','../farm-modular-asset-gate-20260928/reference/tata-02-camp.jpg','取尺度与边界组织，不取 UI 密度。'],['上一轮 / Test A','../farm-modular-asset-gate-20260928/evidence/test-a.png','2 建筑 · 鸡宝 50–52 px · 凳 60 px'],['上一轮 / Test B','../farm-modular-asset-gate-20260928/evidence/test-b.png','2 建筑 · 鸡宝 50–51 px · 盆 42 px'],['本轮 / 正式测试视口','evidence/camp-390x844.png','4 建筑 · 鸡宝 24–27 px · 凳 37 px']];
document.querySelector('#comparison').innerHTML=compare.map(([title,src,note],i)=>`<figure><figcaption>${title}</figcaption>${capture&&i===3?`<div class="pending" data-src="${src}"></div>`:`<img src="${src}" alt="${title}">`}<p>${note}</p></figure>`).join('');
for(const [id,cls,on,off] of [['guide','show-guides','隐藏尺度辅助线','显示尺度辅助线'],['hide','hide-buildings','显示建筑','隐藏建筑检查地面']])document.getElementById(id).addEventListener('click',e=>{const active=document.body.classList.toggle(cls);e.target.setAttribute('aria-pressed',String(active));e.target.textContent=active?on:off;});
document.querySelector('#ratio-readout').innerHTML=`<b>共同标尺：鸡宝高 ≈ 1U（${SCALE.unit}px）</b><span>农舍宽 6U · 门高约 1.7U</span><span>主路 1.4U · 支路 1–1.15U</span><span>活动区约 8.5U × 8.1U · 场景宽 15U</span>`;
window.scaleGate={ready:true,buildings,chickens,props,central:SCALE.central,scale:SCALE,assetCount:Object.keys(kit).length};

