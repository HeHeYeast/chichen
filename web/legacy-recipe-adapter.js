import {expansionRecipes,expansionMatches} from './legacy-content.js';
import {originalRecipes,originalRecipePlan} from './recipes.js';
import {recipeStateAt} from './holiday-calendar.js';
import {randomUnit} from './progression.js';

// The actual original interpreter retains random admission and sampling without
// replacement. A preview pool is never used as the production sampler.
export function sampleLegacyCompanions(s,plan,now,random){
  if(plan.mode.startsWith('regional')&&plan.toolId===8){
    const pool=expansionMatches(s,plan.egg,plan.toolId,plan.legacyMaterials).flatMap(c=>Array(c.rate).fill(c.id));
    return Array.from({length:24},()=>pool[Math.floor(randomUnit(random)*pool.length)]);
  }
  return expansionRecipes(s,plan.egg,plan.toolId,plan.legacyMaterials,random)??originalRecipes(recipeStateAt(s,now),plan.egg,plan.toolId,plan.legacyMaterials,now,random);
}
export function previewLegacyCompanions(s,plan,now){
  return plan.toolId===8?expansionMatches(s,plan.egg,plan.toolId,plan.legacyMaterials).map(c=>c.id):originalRecipePlan(recipeStateAt(s,now),plan.egg,plan.toolId,plan.legacyMaterials,now).pool;
}
