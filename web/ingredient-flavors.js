import {RULES} from './integration-data.js';

// The clue book, shop and mixing tray share the same vocabulary.
const regional={蔬菜:[75,78,82],谷豆与面食:[76],果物:[77],叶花与草木:[79,80],调味:[81]};
export const INGREDIENT_FLAVOR_GROUPS=Object.freeze(Object.fromEntries(
  Object.entries(RULES.ingredientFlavorGroups).map(([name,ids])=>[name,Object.freeze([...new Set([...ids,...(regional[name]??[])])])])
));
export const ingredientFlavor=id=>Object.entries(INGREDIENT_FLAVOR_GROUPS).find(([,ids])=>ids.includes(Number(id)))?.[0]??'';
