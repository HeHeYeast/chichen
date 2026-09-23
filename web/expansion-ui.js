import {EXPANSION,expansionRecipeHints,expansionUnlockInfo} from './content-pack.js';
import {resolveSprite} from './art/manifest.js';
import {speciesLabel} from './catalog.js';

const escapeHTML=value=>String(value).replace(/[&<>"']/g,char=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[char]));
function portrait(path){
  const sprite=resolveSprite(path);
  if(!sprite.frame||!sprite.size)return `<img src="${escapeHTML(sprite.file)}" alt="">`;
  return `<svg class="sprite-art" viewBox="${sprite.frame.join(' ')}" aria-hidden="true"><image href="${escapeHTML(sprite.file)}" width="${sprite.size[0]}" height="${sprite.size[1]}"/></svg>`;
}

export function recipePreparation(state,id){
  const recipe=expansionRecipeHints(state).find(entry=>entry.id===id);
  if(!recipe)return null;
  const missing=recipe.ingredients.filter(ingredient=>(state.ingredients[ingredient]??0)<1);
  return {...recipe,missing,ready:recipe.unlocked&&!missing.length};
}

// Setting up a recipe costs nothing and never replaces an existing batch. The
// normal cooking confirmation remains responsible for starting and spending.
export function prepareExpansionRecipe(state,id){
  const recipe=recipePreparation(state,id);
  if(!recipe)throw Error('没有找到这份配方。');
  if(!recipe.unlocked)throw Error(`这份配方需要竹蒸笼 Lv.${recipe.requiredLevel}。`);
  if(recipe.missing.length)throw Error('调味料还没备齐，请先到商店补一些。');
  state.egg=0;state.selected=[...recipe.ingredients];delete state.events.seasonalRecipe;
  return recipe;
}

export function createExpansionUI({getState,panels,showPanel,act,sound,toolPortrait,ingredientPortrait,closeBook,openToolShop=closeBook,openIngredientShop,openCookware}){
  let currentId=114;
  const find=selector=>panels.querySelector(selector);
  function openRecipeBook(id=currentId){
    const recipes=expansionRecipeHints(getState());
    currentId=recipes.some(recipe=>recipe.id===id)?id:114;
    const currentIndex=recipes.findIndex(recipe=>recipe.id===currentId),recipe=recipePreparation(getState(),currentId);
    const owned=getState().toolLevels[8]??-1,unlock=expansionUnlockInfo(getState());
    const ingredients=recipe.ingredients.length?recipe.ingredients.map((id,index)=>`${index?'<span class="recipe-plus" aria-hidden="true">＋</span>':''}<span class="recipe-ingredient">${ingredientPortrait(id)}<small>${escapeHTML(recipe.ingredientNames[index])}</small><em>${getState().ingredients[id]??0} / 1</em></span>`).join(''):'<span class="recipe-plain-egg" aria-hidden="true"></span><span class="recipe-plain-label">只用鸡蛋<br><small>不放调味料</small></span>';
    const note=!recipe.unlocked?`竹蒸笼 Lv.${recipe.requiredLevel} 开放${owned<0?` · ${unlock.reason}`:''}`:recipe.missing.length?'先备齐下方原料，每种只需一份。':'配方已备齐，下一批就来试试这一味。';
    const action=!recipe.unlocked?'去看竹蒸笼':recipe.missing.length?'去补齐原料':'选好配方';
    const body=`<div class="recipe-book-progress"><span>点心小册 · 鸡蛋专用</span><span>已认识 <strong>${recipes.filter(entry=>entry.discovered).length} / 6</strong></span></div>
      <div class="recipe-bookmarks" aria-label="六味点心">${recipes.map((entry,index)=>`<button type="button" data-recipe-id="${entry.id}" aria-label="第${index+1}味 ${escapeHTML(entry.name)}" aria-pressed="${entry.id===currentId}" class="${entry.id===currentId?'is-current':''} ${entry.unlocked?'':'is-locked'}"><span>${portrait(entry.artwork)}</span><small>${index+1}</small></button>`).join('')}</div>
      <div class="recipe-page scroll" aria-label="${escapeHTML(recipe.name)}配方"><div class="recipe-page-top"><span>${speciesLabel(0,currentId)}</span><span class="recipe-stamp ${recipe.discovered?'is-found':''}">${recipe.discovered?'已收录':recipe.unlocked?'等你发现':'待解锁'}</span></div>
        <div class="recipe-illustration"><div class="recipe-hero"><span class="recipe-tool">${toolPortrait(8,Math.max(0,owned))}</span><span class="recipe-hero-chick ${recipe.unlocked?'':'is-locked'}">${portrait(recipe.artwork)}</span><span class="recipe-steam" aria-hidden="true"><svg viewBox="0 0 25 24"><path d="M5 22C0 16 11 13 6 7M13 18C8 12 18 8 13 2M21 22C16 16 27 13 22 7" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"/></svg></span></div>
        <div class="recipe-caption"><h3>${escapeHTML(recipe.name)}</h3><p class="recipe-character-story">${escapeHTML(recipe.description)}</p></div></div>
        <div class="recipe-mixture">${ingredients}</div><p class="recipe-status ${recipe.ready?'is-ready':''}">${escapeHTML(note)}</p>
        <p class="recipe-guarantee">配方搭配成功，每批至少出 1 只。<br>保持厨房清洁，成熟后记得及时收取。</p>
      </div><div class="recipe-page-turn"><button type="button" data-recipe-prev aria-label="上一份配方" ${currentIndex===0?'disabled':''}>‹</button><span>第 ${currentIndex+1} 味 / 6</span><button type="button" data-recipe-next aria-label="下一份配方" ${currentIndex===5?'disabled':''}>›</button></div>
      <footer class="recipe-book-footer"><button type="button" data-recipe-back>‹ 返回货架</button><button type="button" class="orange" data-recipe-prepare>${action}</button></footer>`;
    showPanel('竹笼点心坊',body,'screen-panel expansion-screen');
    find('.close').onclick=closeBook;find('.close').setAttribute('aria-label','合上点心配方册');
    find('[data-recipe-back]').onclick=closeBook;
    const move=(id,focus)=>{sound(3);openRecipeBook(id);find(focus)?.focus({preventScroll:true});};
    panels.querySelectorAll('[data-recipe-id]').forEach(button=>{
      button.onclick=()=>move(Number(button.dataset.recipeId),`[data-recipe-id="${button.dataset.recipeId}"]`);
      button.onkeydown=event=>{
        if(!['ArrowLeft','ArrowRight','Home','End'].includes(event.key))return;
        event.preventDefault();
        const next=event.key==='Home'?0:event.key==='End'?5:Math.max(0,Math.min(5,currentIndex+(event.key==='ArrowRight'?1:-1)));
        move(recipes[next].id,`[data-recipe-id="${recipes[next].id}"]`);
      };
    });
    find('[data-recipe-prev]').onclick=()=>move(recipes[Math.max(0,currentIndex-1)].id,'[data-recipe-prev]');
    find('[data-recipe-next]').onclick=()=>move(recipes[Math.min(5,currentIndex+1)].id,'[data-recipe-next]');
    find('[data-recipe-prepare]').onclick=()=>{
      const latest=recipePreparation(getState(),currentId);
      if(!latest.unlocked){openToolShop();return;}
      if(latest.missing.length){openIngredientShop(latest.missing);return;}
      act(()=>{prepareExpansionRecipe(getState(),currentId);sound(3);openCookware(EXPANSION.toolId);});
    };
  }
  return {openRecipeBook};
}
