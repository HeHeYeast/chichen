import {recipeBookModel,discoveredRecipe} from './recipe-book.js';
import {speciesLabel} from './catalog.js';

const esc=value=>String(value??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
export function createRecipeBookUI({getState,getNow,panels,showPanel,characterPortrait,toolPortrait,ingredientPortrait,onClose,onPrepare,openIngredients,openTools,openKitchen,openActivities,openDuckShop,openCalendar}){
  let toolId=0,egg=-1,selected=null,detailReturn=null;
  const scrolls=new Map();
  const find=s=>panels.querySelector(s);
  const remember=()=>{if(!selected&&find('.cookbook-list'))scrolls.set(`${toolId}:${egg}`,find('.cookbook-list').scrollTop);};
  let backLabel=null;
  function open({tool=toolId,key=null,back=null,label=null}={}){
    remember();toolId=tool;selected=null;detailReturn=null;backLabel=label;
    const r=key?discoveredRecipe(getState(),key,getNow()):null;
    if(r){toolId=r.toolId;selected=key;detailReturn=back;}
    render();
  }
  function render(){
    const s=getState(),model=recipeBookModel(s,{toolId,egg},getNow());
    let r=selected?discoveredRecipe(s,selected,getNow()):null;
    if(selected&&!r){selected=null;detailReturn=null;}
    const tabs=`<div class="cookbook-tools" aria-label="按厨具查配方">${model.tools.map(t=>`<button data-cookbook-tool="${t.id}" aria-pressed="${toolId===t.id}" class="${toolId===t.id?'is-current':''}">${t.id>=0?toolPortrait(t.id,Math.max(0,s.toolLevels[t.id]??0)):'<span class="cookbook-change-icon">?</span>'}<span>${t.name}</span><small>${t.count} 份</small></button>`).join('')}</div>`;
    const filters=`<div class="cookbook-filter"><span>已记下 ${model.total} 份</span><div>${[[-1,'全部'],[0,'鸡宝'],[1,'鸭宝']].map(([value,label])=>`<button data-cookbook-egg="${value}" aria-pressed="${egg===value}">${label}</button>`).join('')}</div></div>`;
    const body=r?detail(r):`${tabs}${filters}<div class="cookbook-list scroll">${model.entries.length?model.entries.map(entry=>`<button class="cookbook-card" data-cookbook-recipe="${entry.key}"><span class="cookbook-code">${speciesLabel(entry.egg,entry.id)} · ${entry.egg?'鸭宝':'鸡宝'}</span><span class="cookbook-card-art">${characterPortrait(entry.egg,entry.id)}</span><strong>${esc(entry.name)}</strong><small>${entry.special?'查看出现方式':`${entry.toolName} Lv.${entry.minLevel+1}`}</small></button>`).join(''):'<div class="cookbook-empty"><span class="collection-unknown-egg">?</span><strong>这一页还没有配方</strong><p>用这件厨具发现新伙伴，<br>首次收取后，配方会记在这里。</p><small>未收录的伙伴不会提前显示配方。</small></div>'}</div>`;
    const action=r?nextAction(r):null;
    showPanel('厨房配方册',`${body}<footer class="cookbook-footer"><button data-cookbook-back>‹ ${selected?detailReturn?(backLabel??'返回档案'):'返回配方册':'返回图鉴'}</button>${r?`<button class="orange" data-cookbook-prepare ${action.disabled?'disabled':''}>${action.label}</button>`:'<span>收录一次，配方一直保留</span>'}</footer>`,'screen-panel cookbook-screen');
    find('.close').onclick=back;find('[data-cookbook-back]').onclick=back;
    panels.querySelectorAll('[data-cookbook-tool]').forEach(b=>b.onclick=()=>{remember();toolId=+b.dataset.cookbookTool;selected=null;detailReturn=null;render();});
    panels.querySelectorAll('[data-cookbook-egg]').forEach(b=>b.onclick=()=>{remember();egg=+b.dataset.cookbookEgg;render();});
    panels.querySelectorAll('[data-cookbook-recipe]').forEach(b=>b.onclick=()=>{remember();selected=b.dataset.cookbookRecipe;detailReturn=null;render();});
    find('[data-cookbook-prepare]')?.addEventListener('click',()=>{
      const latest=discoveredRecipe(getState(),selected,getNow());if(!latest){selected=null;render();return;}
      const a=nextAction(latest);if(a.disabled)return;
      if(a.kind==='ingredients')openIngredients(latest.missing);
      else if(a.kind==='duck')openDuckShop();else if(a.kind==='activity')openActivities(latest.activityId??'shrine-gift');
      else if(a.kind==='time')openCalendar();else if(a.kind==='tools')openTools(latest.toolId);else if(a.kind==='kitchen')openKitchen();
      else onPrepare(latest.key);
    });
    if(!selected){find('.cookbook-list').scrollTop=scrolls.get(`${toolId}:${egg}`)??0;find(`[data-cookbook-tool="${toolId}"]`)?.scrollIntoView({block:'nearest',inline:'center'});}
  }
  function nextAction(r){
    if(r.special)return {label:'特殊出现方式',disabled:true};
    if(r.egg&&!getState().duck)return {label:'去开放鸭蛋',kind:'duck'};
    if(r.conditions.some(c=>!c.met&&c.kind==='calendar'))return {label:'查看活动日期',kind:'time'};
    if(r.conditions.some(c=>!c.met&&c.label.includes('开火时段')))return {label:'查看时段',kind:'time'};
    if(r.conditions.some(c=>!c.met&&(c.label.includes('完成「')||c.label.includes('对应的签礼'))))return {label:'去神社看看',kind:'activity'};
    if(r.conditions.some(c=>!c.met&&c.label.startsWith('厨房')))return {label:'查看厨房升级',kind:'kitchen'};
    if(r.conditions.some(c=>!c.met&&/^(累计收取|认识)/.test(c.label)))return {label:'继续收集伙伴',disabled:true};
    if(r.conditions.some(c=>!c.met))return {label:'查看所需厨具',kind:'tools'};
    if(r.missing.length)return {label:'补齐材料',kind:'ingredients'};
    return {label:'配好下一批',kind:'prepare'};
  }
  function detail(r){
    return `<div class="cookbook-detail scroll"><div class="cookbook-detail-heading"><span>${speciesLabel(r.egg,r.id)} · ${r.egg?'鸭宝':'鸡宝'}</span><b>已解锁配方</b></div><div class="cookbook-hero"><span>${characterPortrait(r.egg,r.id)}</span>${r.toolId>=0?`<span class="cookbook-detail-tool">${toolPortrait(r.toolId,r.minLevel)}</span>`:''}</div><h3>${esc(r.name)}</h3>${r.description?`<p class="cookbook-story">${esc(r.description)}</p>`:''}<div class="cookbook-method"><strong>${r.special?'出现方式':`${r.egg?'鸭蛋':'鸡蛋'} · ${esc(r.toolName)} Lv.${r.minLevel+1} 起`}</strong>${r.special?'':`<p>${getState().toolLevels[r.toolId]>=r.minLevel?'按现有厨具':'按所需等级'}：本次${Number(r.minutes.toFixed(2))}分钟（原${r.originalMinutes}分钟）· ${r.cost} CP / 批</p>`}</div>${r.special?'':`<div class="cookbook-mixture">${r.ingredients.length?r.ingredients.map((id,i)=>`<span>${ingredientPortrait(id)}<b>${esc(r.ingredientNames[i])}</b><small>持有 ${getState().ingredients[id]??0} / 1</small></span>`).join('<i>＋</i>'):'<p>不放调味料</p>'}</div>`}<ul class="cookbook-conditions">${r.conditions.map(c=>`<li class="${c.met?'is-met':''}">${c.met?'✓':'○'} ${esc(c.label)}</li>`).join('')}</ul><p class="cookbook-note">${esc(r.note)}</p>${r.special?'':'<p class="cookbook-save-note">准备只替换下一批的蛋种与材料，当前一批继续孵化。开火确认后才消耗材料和 CP。</p>'}</div>`;
  }
  function back(){if(selected){if(detailReturn){const previous=detailReturn;selected=null;detailReturn=null;previous();}else{selected=null;render();}}else onClose();}
  return {open,resume:render,refresh(){if(find('.cookbook-screen'))render();},reset(){selected=null;toolId=0;egg=-1;detailReturn=null;scrolls.clear();}};
}
