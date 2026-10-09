import {expansionRecipes,expansionMatches} from './legacy-content.js';
import {originalRecipePlan} from './recipes.js';
import {recipeStateAt} from './holiday-calendar.js';
import {randomUnit} from './progression.js';

// Keep the original condition-derived weights, then draw every egg independently.
// Gift candidates join the weighted pool instead of occupying fixed slots.
export function sampleLegacyCompanions(s,plan,now,random){
  if(plan.mode.startsWith('regional')&&plan.toolId===8){
    const pool=expansionMatches(s,plan.egg,plan.toolId,plan.legacyMaterials).flatMap(c=>Array(c.rate).fill(c.id));
    return Array.from({length:24},()=>pool[Math.floor(randomUnit(random)*pool.length)]);
  }
  if(plan.toolId===8)return expansionRecipes(s,plan.egg,plan.toolId,plan.legacyMaterials,random);
  const p=originalRecipePlan(recipeStateAt(s,now),plan.egg,plan.toolId,plan.legacyMaterials,now);
  const pool=[...p.pool];
  for(const gift of p.gifts)pool.push(...Array(Math.max(1,Math.round(p.pool.length*.25/p.gifts.length))).fill(gift.id));
  return Array.from({length:24},()=>pool[Math.floor(randomUnit(random)*pool.length)]);
}
export function previewLegacyCompanions(s,plan,now){
  return plan.toolId===8?expansionMatches(s,plan.egg,plan.toolId,plan.legacyMaterials).map(c=>c.id):originalRecipePlan(recipeStateAt(s,now),plan.egg,plan.toolId,plan.legacyMaterials,now).pool;
}
