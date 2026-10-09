// New batches sample every egg independently. Existing tickets are immutable.
export const HATCH_TIERS=Object.freeze({common:.30,uncommon:.20,rare:.10});
export function recipeTier(recipe){
  const level=recipe.toolLevel??recipe.minLevel??0;
  return level>=2?'rare':level>=1||recipe.ingredients.length>1?'uncommon':'common';
}
export const recipeChance=recipe=>HATCH_TIERS[recipeTier(recipe)];
export const chanceInBatch=(chance,count=24)=>1-(1-chance)**count;
