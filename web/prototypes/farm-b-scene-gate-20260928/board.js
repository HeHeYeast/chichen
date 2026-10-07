import {interfaceIcon} from '../../ui-icons.js';
const candidates=[
 {id:'b1',name:'温和营地型',tag:'均衡环绕 · 日常归属',concept:'一圈暖土路围住完整草地，四处建筑各占一个边缘；先看中央伙伴，再顺着环路办事。',
  camera:'中景偏远。主屋露正面与侧墙，棚架露顶面和立柱；前景陈列与神社略大，构成前后关系。',style:'清楚的深棕轮廓、短接地影和叶团块面；暖红、奶油与草绿集中组织，整体最温和。',organization:'左上农舍＋整修台，右上补给摊，左下陈列亭，右下神社；中央为椭圆伙伴草地。',risk:'环路过于规整，容易像展示庭院；近景还需要减少背景草花细节。',
  far:'补全西侧工具台外缘、南入口和林带；看清整圈步道及四条门前短路。',near:'聚焦中央草地、木凳与农舍门前；外围神社、陈列可通过平移查看。',
  labels:[['农舍 · 收成',29,25.2],['补给摊',78,29.3],['神社',79,77.8],['陈列亭',22,77.6]],pins:[[29,19],[78,24],[79,69],[22,68],[53,45],[10,22]]},
 {id:'b2',name:'功能聚落型',tag:'服务庭院 · 分组办事',concept:'左侧农舍与补给共享服务庭院，右下神社与陈列组成静区；S 形主路把两组串起来。',
  camera:'中景。斜向道路拉开纵深，主屋同时显示屋顶、两面墙和门阶；右侧建筑用前后错位组织。',style:'借鉴设施成组与地面分区，屋瓦、石坪和木架保持同一光向；比 B1 更有经营聚落的秩序。',organization:'左上农舍旁附整修台、其下补给；右中神社、右下陈列；中央草地夹在两组之间。',risk:'主屋与石坪容易抢走中央伙伴的注意；草地偏纵长，缩小后角色可能过小。',
  far:'多看到左侧服务庭院完整边界、右侧静区支路、上方林间路与南入口。',near:'优先中央草地与服务庭院交界；一侧处理收成，一侧看伙伴停留，平移进入静区。',
  labels:[['农舍 · 收成',39,25.2],['补给摊',13,37],['神社',85,61.6],['陈列亭',81,81.8]],pins:[[33,17],[13,29],[85,49],[81,72],[51,51],[28,22]]},
 {id:'b3',name:'活力活动型',tag:'伙伴主角 · 开放活动地',concept:'中央改为较开阔的沙地与草地组合；追叶、停凳、探水三处日常小动作形成生活重心。',
  camera:'中景偏近。主屋在后、神社在右中、陈列在右前，建筑侧面和门阶可读；角色尺度更大。',style:'更鲜明的黄绿地面、暖色活动场与集中角色轮廓；用稀疏脚印和短影表达活动，不加奖励气泡。',organization:'后侧主屋＋整修，左中补给，右中神社，右前陈列；分叉路径沿中央活动地边缘通过。',risk:'伙伴大动作容易让玩家期待可点击小游戏；沙地与商品篮也要避免暗示新增玩法。',
  far:'完整看到左侧补给摊、右侧神社屋檐、前景陈列外缘与三处分叉路径。',near:'以追叶的两只伙伴为焦点，保留木凳与水盆的方向线索；放大不新增可操作系统。',
  labels:[['农舍 · 收成',39,23.3],['补给摊',12,51.1],['神社',85,36.6],['陈列亭',80,79.8]],pins:[[37,14],[13,40],[84,27],[80,70],[55,44],[12,18]]}
];
const names=['农舍 / 收成表','补给 / 小卖部','神社 / 委托与御神签','陈列 / 三个展示位','中央伙伴活动地','整修 / 附属工具台'];
const nav=[['kitchen','厨房'],['farm','农场'],['shop','生意'],['explore','寻访'],['book','图鉴']];
const settings='<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7"><path d="m9 3 1-2h4l1 2 3 2 2 .2 2 3-1 2v3l1 2-2 3-2 .2-3 2-1 2h-4l-1-2-3-2-2-.2-2-3 1-2v-3l-1-2 2-3 2-.2z" transform="translate(1 1) scale(.9)"/><circle cx="12" cy="12" r="3.4"/></svg>';
for(const d of candidates){
 const article=document.createElement('article');article.className='candidate';article.id=d.id;
 article.innerHTML=`<header class="candidate-head"><span>${d.id.toUpperCase()}</span><div><h2>${d.name}</h2><p>${d.tag}</p></div></header>
 <div class="game-screen" role="img" aria-label="${d.name}，390乘844完整Farm候选界面">
  <header class="game-hud"><div class="hud-main"><div class="money"><img src="/web/art/golden-business/coin.png" alt="CP">1,240</div><strong>农场</strong><span class="settings">${settings}</span></div><div class="hud-status"><span><i class="dot"></i>在家 5 只</span><span class="condition">完好 100% <i>⌁</i></span></div></header>
  <div class="scene"><img class="scene-art" src="art/${d.id}.png" alt="${d.organization}">
   ${d.labels.map(([t,x,y])=>`<span class="building-label" style="left:${x}%;top:${y}%">${t}<b>›</b></span>`).join('')}
   <span class="gesture">拖动浏览 · 双指缩放</span><div class="camera-affordance" aria-hidden="true"><span>−</span><span class="focus-icon">⌖</span><span>＋</span></div>
   <div class="annotations">${d.pins.map(([x,y],i)=>`<span class="pin" style="left:${x}%;top:${y}%">${i+1}</span>`).join('')}</div>
  </div><nav class="game-nav">${nav.map(([icon,t])=>`<span class="${icon==='farm'?'active':''}">${interfaceIcon(icon)}<b>${t}</b></span>`).join('')}</nav>
 </div>
 <div class="annotation-key"><h3>位置编号</h3><ol>${names.map(n=>`<li>${n}</li>`).join('')}</ol></div>
 <section class="candidate-notes"><p class="core">${d.concept}</p><dl><dt>视角</dt><dd>${d.camera}</dd><dt>画风</dt><dd>${d.style}</dd><dt>功能</dt><dd>${d.organization}</dd><dt>风险</dt><dd>${d.risk}</dd></dl><div class="camera-note"><b>远景</b><p>${d.far}</p><b>近景</b><p>${d.near}</p></div><a href="deliverables/${d.id}-390x844.png">主界面 PNG ↗</a> <a href="deliverables/${d.id}-annotated-390x844.png">位置说明图 ↗</a></section>`;
 document.querySelector('#candidates').append(article);
}
document.querySelector('#annotate').addEventListener('click',e=>{const on=document.body.classList.toggle('annotated');e.currentTarget.setAttribute('aria-pressed',String(on));e.currentTarget.textContent=on?'隐藏建筑位置说明':'显示建筑位置说明';});
window.sceneGate={ids:candidates.map(d=>d.id),ready:true};
