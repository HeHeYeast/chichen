// Pure preparation query shared by confirmation and real cooking. No RNG,
// resource consumption, discovery, or policy about what the player may reveal.
export function cookingIngredients(state){
  return [...new Set(state.selected)].filter(id=>state.ingredients[id]>0)
    .slice(0,Math.min(3,state.kitchenLevel+1));
}
