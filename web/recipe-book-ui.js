import {recipeBookModel,discoveredRecipe} from './recipe-book.js';
import {speciesLabel} from './catalog.js';
import {kitSheet,kitCoin,kitEgg,kitButton,kitButton2,kitChip,kitChipHtml,kitLabel,kitIcon,kitCell} from './ui-kit.js';

const esc=value=>String(value??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
export function createRecipeBookUI({getState,getNow,panels,showPanel,characterPortrait,toolPortrait,ingredientPortrait,onClose,onPrepare,openIngredients,openTools,openKitchen,openActivities,openDuckShop,openCalendar}){
  let toolId=0,egg=-1,selected=null,detailReturn=null,fromBook=false;
  const scrolls=new Map();
  const find=s=>panels.querySelector(s);
  const remember=()=>{if(!selected&&find('.cookbook-screen .kp-scroll'))scrolls.set(`${toolId}:${egg}`,find('.cookbook-screen .kp-scroll').scrollTop);};
  let backLabel=null;
  function open({tool=toolId,key=null,back=null,label=null,fromBook:book=false}={}){
    remember();fromBook=book&&!key;toolId=tool;selected=null;detailReturn=null;backLabel=label;
    const r=key?discoveredRecipe(getState(),key,getNow()):null;
    if(r){toolId=r.toolId;selected=key;detailReturn=back;}
    render();
  }
  function render(){
    const s=getState(),model=recipeBookModel(s,{toolId,egg},getNow());
    let r=selected?discoveredRecipe(s,selected,getNow()):null;
    if(selected&&!r){selected=null;detailReturn=null;}
    // List: cookware coins, egg coins, then the recorded recipes as stickers. Detail: pictures first.
    const tools=`<div class="gd-scroll-row cookbook-tools bk-tools" role="group" aria-label="按厨具查配方" data-hscroll>${model.tools.map(t=>`<button type="button" class="bk-tool" data-cookbook-tool="${t.id}" aria-pressed="${toolId===t.id}"><span class="bk-tool-art">${t.id>=0?toolPortrait(t.id,Math.max(0,s.toolLevels[t.id]??0)):'<b>?</b>'}</span><small>${t.count}</small></button>`).join('')}</div>`;
    const eggs=`<div class="kp-bar bk-bar" data-row><div class="kp-coins" role="group" aria-label="蛋种">${kitCoin('全','data-cookbook-egg="-1"',egg===-1,'全部')}${kitCoin(kitEgg(false),'data-cookbook-egg="0"',egg===0,'鸡宝')}${kitCoin(kitEgg(true),'data-cookbook-egg="1"',egg===1,'鸭宝')}</div><span class="bk-count"><b>${model.total}</b>份配方</span></div>`;
    const list=model.entries.length?`<div class="cookbook-list bk-grid">${model.entries.map(entry=>`<button type="button" class="cookbook-card bk-sticker is-known" data-cookbook-recipe="${entry.key}"><span class="bk-sticker-art">${characterPortrait(entry.egg,entry.id)}</span><strong class="collection-name">${esc(entry.name)}</strong><span class="collection-code">${entry.special?'特别出现':`${esc(entry.toolName)} Lv.${entry.minLevel+1}`}</span></button>`).join('')}</div>`:`<div class="kp-empty cookbook-empty"><span class="bk-q">?</span><span>这件厨具还没记下配方</span></div>`;
    const action=r?nextAction(r):null;
    const body=r?detail(r):`${eggs}${tools}${kitLabel(model.tools.find(t=>t.id===toolId)?.name??'')}${list}`;
    // The back button names where it goes (an order, a profile, the list or the 图鉴).
    const foot=r?`${kitButton2(detailReturn?(backLabel??'返回档案'):'返回配方册','data-cookbook-back')}${kitButton(action.label,'data-cookbook-prepare'+(action.disabled?' disabled':''))}`:(fromBook?'':kitButton('返回图鉴','data-cookbook-back'));
    showPanel('厨房配方册',kitSheet(body,foot,'bk-sheet',r?'':'',{attrs:r?'':'data-list',cls:r?'':''}),'screen-panel cookbook-screen',{skin:'book',icon:characterPortrait(0,0),back:!!r||!fromBook});
    find('.close').onclick=back;find('[data-cookbook-back]')?.addEventListener('click',back);
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
    if(!selected){const list=find('.kp-scroll');if(list)list.scrollTop=scrolls.get(`${toolId}:${egg}`)??0;find(`[data-cookbook-tool="${toolId}"]`)?.scrollIntoView({block:'nearest',inline:'center'});}
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
  // The recipe card: the partner on a little stage in the light, its name on a ribbon; then the method as
  // dotted-leader rows (the cookware, each seasoning) ending in what you have, and the facts as pictures + numbers.
  function detail(r){
    const tick=met=>met?'<img src="/web/art/golden-business/family-check.png" alt="已达成">':'';
    const st=getState(),ART='/web/art/golden-ui/';
    const line=(pic,name,have,ok,cls='')=>`<span class="rb-line${cls}"><i class="rb-pic" data-visual>${pic}</i><b class="rb-what">${esc(name)}</b><i class="rb-dots" aria-hidden="true"></i><span class="rb-have${ok?' ok':' short'}">${esc(have)}</span>${ok?'<img class="rb-ok" src="/web/art/golden-business/family-check.png" alt="有">':''}</span>`;
    const lv=r.toolId>=0?(st.toolLevels?.[r.toolId]??-1):-1;
    const tool=r.toolId>=0?line(toolPortrait(r.toolId,r.minLevel),`${r.toolName}`,lv<0?'还没有':lv>=r.minLevel?`Lv.${r.minLevel+1} 起`:`要 Lv.${r.minLevel+1}`,lv>=r.minLevel,' is-tool'):'';
    const mix=r.ingredients.length?r.ingredients.map((id,n)=>{const have=st.ingredients[id]??0;return line(ingredientPortrait(id),r.ingredientNames[n],have?`有 ${have}`:'缺',have>0);}).join('')
      :`<p class="rb-line is-none"><i class="rb-pic" data-visual><img src="${ART}ic-jar.png" alt=""></i><b class="rb-what">调味料</b><i class="rb-dots" aria-hidden="true"></i><span class="rb-have ok">不放</span></p>`;
    const fact=(icon,v,l)=>`<span class="rb-fact"><img src="${icon}" alt=""><b>${esc(v)}</b><small>${esc(l)}</small></span>`;
    return `<div class="cookbook-detail bk-recipe-detail rb-detail"><div class="rb-stage"><span class="rb-hero">${characterPortrait(r.egg,r.id)}</span></div>
      <div class="rb-ribbon"><span data-safe><h3 class="species-name">${esc(r.name)}</h3></span></div><small class="rb-code">${speciesLabel(r.egg,r.id)} · ${r.egg?'鸭宝':'鸡宝'}</small>
      ${r.special?kitChip('','特别出现方式','soft'):`<section class="cookbook-method rb-card" aria-label="做法">${tool}<div class="cookbook-mixture rb-mix">${mix}</div></section>
      <div class="rb-facts">${fact(ART+'ic-hourglass.png',`${Number(r.minutes.toFixed(2))} 分`,'用时')}${fact('/web/art/golden-business/coin.png',`${r.cost}`,'每锅 CP')}${fact(ART+(r.egg?'ic-duck-egg.png':'ic-egg.png'),r.egg?'鸭蛋':'鸡蛋','用蛋')}</div>`}
      ${r.description?`<p class="cookbook-story species-story">${esc(r.description)}</p>`:''}
      ${r.conditions.length?`<div class="sh-conds cookbook-conditions">${r.conditions.map((c,n)=>`<span class="sh-cond${c.met?' met':''}"><i>${tick(c.met)||n+1}</i>${esc(c.label)}</span>`).join('')}</div>`:''}
      <p class="cookbook-note rb-note">${esc(r.note)}</p></div>`;
  }
  function back(){if(selected){if(detailReturn){const previous=detailReturn;selected=null;detailReturn=null;previous();}else{selected=null;render();}}else onClose();}
  return {open,resume:render,refresh(){if(find('.cookbook-screen'))render();},reset(){selected=null;toolId=0;egg=-1;detailReturn=null;scrolls.clear();}};
}
