// Presentation only: all progress and visibility are provided by existing models.
import {interfaceIcon} from './ui-icons.js';
const esc=v=>String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
export function collectionCover(c,index=0){
 const region=/^COL-([VRTB])$/.exec(c.id)?.[1];
 return `<span class="remaster-cover-object" aria-hidden="true">${region?`<img src="/web/art/golden-journey/node-${region}.png" alt="">`:interfaceIcon(['explore','workshop','farm','book'][index%4])}</span>`;
}
export function collectionDirectory(cards){
 return `<div class="remaster-directory">${cards.map((c,i)=>{
 const marks=[...c.stages.map((s,j)=>({done:s.granted,label:'阶段'+(j+1)})),{done:c.practice.granted,label:'实践印'},...(c.fullMenu?[{done:c.fullMenu.granted,label:'菜单印'}]:[])];
 return `<button class="books-card" data-books-open="${c.id}">${collectionCover(c,i)}<strong>${esc(c.name)}</strong><span class="remaster-cover-stamps">${marks.map((s,j)=>`<span class="${s.done?'is-done':''}" aria-label="${s.label}：${s.done?'已入册':'待完成'}">${s.done?'✓':j+1}</span>`).join('')}</span><small>已收录 ${c.known.length} 种<br>翻开查看目标 ›</small></button>`;
 }).join('')}</div>`;
}
export function collectionAchievements(c){
 const marks=[...c.stages.map((s,i)=>({done:s.granted,label:'阶段 '+(i+1)})),{done:c.practice.granted,label:'实践印'},...(c.fullMenu?[{done:c.fullMenu.granted,label:'菜单印'}]:[])];
 return `<div class="remaster-achievements" aria-label="入册成果">${marks.map(s=>`<span class="${s.done?'is-done':''}"><b aria-hidden="true">${s.done?'✓':'○'}</b>${s.label}<span class="sr-only">${s.done?'已入册':'待完成'}</span></span>`).join('')}</div>`;
}
export const bookIndexDoors=()=>`<div class="remaster-index-doors">${[['species','book','品种','发现的伙伴'],['collections','inventory','收藏','留下的印记'],['lore','explore','见闻','路上的记录']].map(([tab,icon,title,note])=>`<button data-index-tab="${tab}">${interfaceIcon(icon)}<strong>${title}</strong><small>${note}</small></button>`).join('')}</div>`;
