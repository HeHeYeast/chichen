// Frozen author-format contract. Runtime never imports the author JSON or this file.
import {compileRegionalCondition,regionalDerivedRules} from './content-regional-rules.mjs';
import {compileBusinessCondition} from './content-business-rules.mjs';
import {compileCollectionCondition} from './content-collection-rules.mjs';
import {compileRegularCondition} from './content-regular-rules.mjs';
import {compileProjectCondition} from './content-project-rules.mjs';
// Conditions are addressed by stable identity plus field, not interpreted Chinese text.
export const RUNTIME_FORMAT = 1;
export const RULES_VERSION = 1;
export const CONTENT_REVISION = 'regional-1';
export const SIGNATURES = Object.freeze({'家常':'home','煎炸':'fry','炖煮':'stew','蒸点':'steam','烘焙':'bake','茶饮':'tea'});
export const CONDITION_FIELDS = Object.freeze({
  regions:['gate'], species:['unlock.oldSupplyRules','unlock.technologyNote','businessExclusion','regularRelation'],
  materials:['identification','entryGate'], alternatives:['guarantee'],
  cards:['gate','protection'], guide:['gate','team','trigger','effect'],
  menus:['complete','unlock','validService'], orders:['qualification','payment','seasonRule'],
  regulars:['delivery','completion'], 'regulars.stages':['gate','alternatives','recordPolicy'],
  projects:['gate','optionalDisplay.rule'], 'projects.stages':['check'],
  collections:['stageRules','practice.any','practice.fullMenuStamp'], 'collections.stages':['requires'],
  specials:['fixed','optional','practice','reward'], mementos:['effect','unlock'],
  productionRules:['knowledge','method','trial','companions','prices','timing','unlock','ui'],
});

export function conditionInputs(content) {
  const result=[];
  for(const [table,fields] of Object.entries(CONDITION_FIELDS)) {
    const [root,child]=table.split('.');
    const raw=content[root];
    const parents=Array.isArray(raw)?raw:[raw];
    for(const [i,parent] of parents.entries()) {
      const rows=child?(parent[child]??[]):[parent];
      for(const [j,row] of rows.entries()) for(const field of fields) {
        const value=field.split('.').reduce((v,k)=>v?.[k],row);
        if(value==null)continue;
        const pointer=`/${root}${Array.isArray(raw)?`/${i}`:''}${child?`/${child}/${j}`:''}/${field.replaceAll('.','/')}`;
        result.push({id:`${row.id??root}:${field}`,sourcePointer:pointer,source:value,work:({regions:'B',species:'B',materials:'B',alternatives:'C',cards:'B',guide:'G',menus:'E',orders:'E',regulars:'I',projects:'J',collections:'H',specials:'H',mementos:'H',productionRules:'B'})[root]});
      }
    }
  }
  return result;
}

// Actual activation belongs to each Work's domain code. A never treats a pending
// natural-language condition as true. Source hashes require explicit review on edit.
export function compileConditions(content,hash,runtime) {
  const inputs=conditionInputs(content),requirements={},coverage=[];
  for(const input of inputs) {
    if(CONDITION_SOURCE_HASHES[input.id]!==hash(JSON.stringify(input.source)))throw Error(`Unreviewed condition ${input.id} at ${input.sourcePointer}`);
    requirements[input.id]=compileRegionalCondition(input,runtime)??compileBusinessCondition(input)??compileCollectionCondition(input)??compileRegularCondition(input)??compileProjectCondition(input,content)??{id:input.id,kind:'unavailable',reason:'implementation-pending',work:input.work,sourcePointer:input.sourcePointer};
    coverage.push({id:input.id,sourcePointer:input.sourcePointer,sourceHash:CONDITION_SOURCE_HASHES[input.id],work:requirements[input.id].work??input.work,status:requirements[input.id].kind==='unavailable'?'unavailable':'compiled'});
  }
  if(inputs.length!==Object.keys(CONDITION_SOURCE_HASHES).length)throw Error('Condition coverage changed; explicitly review added/removed conditions');
  Object.assign(requirements,regionalDerivedRules(runtime));
  return {requirements,coverage};
}

// Exhaustive frozen schema: path-specific types, accepted keys and required keys.
export const AUTHOR_SCHEMAS = {
  "content": {
    "": {
      "types": [
        "object"
      ],
      "allowed": [
        "version",
        "status",
        "date",
        "counts",
        "tags",
        "traits",
        "regions",
        "species",
        "materials",
        "alternatives",
        "cards",
        "guide",
        "menus",
        "selectors",
        "orders",
        "regulars",
        "projects",
        "collections",
        "specials",
        "mementos",
        "decisions",
        "quail",
        "productionRules",
        "paperRecords"
      ],
      "required": [
        "version",
        "status",
        "date",
        "counts",
        "tags",
        "traits",
        "regions",
        "species",
        "materials",
        "alternatives",
        "cards",
        "guide",
        "menus",
        "selectors",
        "orders",
        "regulars",
        "projects",
        "collections",
        "specials",
        "mementos",
        "decisions",
        "quail",
        "productionRules",
        "paperRecords"
      ]
    },
    "/version": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/status": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/date": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/counts": {
      "types": [
        "object"
      ],
      "allowed": [
        "oldSpecies",
        "newSpecies",
        "chicken",
        "duck",
        "total",
        "materials",
        "cards",
        "themeCollections",
        "regionCollections",
        "menus",
        "orders",
        "regulars",
        "regularStages",
        "projects",
        "mementos"
      ],
      "required": [
        "oldSpecies",
        "newSpecies",
        "chicken",
        "duck",
        "total",
        "materials",
        "cards",
        "themeCollections",
        "regionCollections",
        "menus",
        "orders",
        "regulars",
        "regularStages",
        "projects",
        "mementos"
      ]
    },
    "/counts/oldSpecies": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/counts/newSpecies": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/counts/chicken": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/counts/duck": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/counts/total": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/counts/materials": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/counts/cards": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/counts/themeCollections": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/counts/regionCollections": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/counts/menus": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/counts/orders": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/counts/regulars": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/counts/regularStages": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/counts/projects": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/counts/mementos": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/tags": {
      "types": [
        "object"
      ],
      "allowed": [
        "home",
        "meal",
        "portable",
        "fresh",
        "tea",
        "steam",
        "bake",
        "sweet",
        "savory",
        "ginger",
        "mushroom",
        "floral",
        "roast",
        "bay",
        "grain",
        "leaf",
        "fruit"
      ],
      "required": [
        "home",
        "meal",
        "portable",
        "fresh",
        "tea",
        "steam",
        "bake",
        "sweet",
        "savory",
        "ginger",
        "mushroom",
        "floral",
        "roast",
        "bay",
        "grain",
        "leaf",
        "fruit"
      ]
    },
    "/tags/home": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/tags/meal": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/tags/portable": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/tags/fresh": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/tags/tea": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/tags/steam": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/tags/bake": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/tags/sweet": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/tags/savory": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/tags/ginger": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/tags/mushroom": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/tags/floral": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/tags/roast": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/tags/bay": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/tags/grain": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/tags/leaf": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/tags/fruit": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/traits": {
      "types": [
        "object"
      ],
      "allowed": [
        "leaf",
        "grain",
        "portable",
        "tea",
        "floral",
        "fruit",
        "salt"
      ],
      "required": [
        "leaf",
        "grain",
        "portable",
        "tea",
        "floral",
        "fruit",
        "salt"
      ]
    },
    "/traits/leaf": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/traits/grain": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/traits/portable": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/traits/tea": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/traits/floral": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/traits/fruit": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/traits/salt": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regions": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "name",
        "route",
        "hours",
        "environment",
        "places",
        "materials",
        "gate",
        "kitchen",
        "discoveries",
        "collected",
        "visual",
        "oldKeys",
        "entrySpecies"
      ],
      "required": [
        "id",
        "name",
        "route",
        "hours",
        "environment",
        "places",
        "materials",
        "gate",
        "kitchen",
        "discoveries",
        "collected",
        "visual",
        "oldKeys",
        "entrySpecies"
      ]
    },
    "/regions/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/name": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/route": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/hours": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/environment": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/places": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/places/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/materials": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/materials/*": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/gate": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/kitchen": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/discoveries": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/collected": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/visual": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/oldKeys": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/oldKeys/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/entrySpecies": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "key",
        "catalogLabel",
        "workName",
        "name",
        "egg",
        "region",
        "recipe",
        "unlock",
        "edible",
        "viewable",
        "signature",
        "tags",
        "exploration",
        "art",
        "description",
        "clue",
        "menus",
        "collections",
        "orders",
        "regulars",
        "projects",
        "uses",
        "relatedCards",
        "specials",
        "displayMenus",
        "businessExclusion",
        "regularRelation",
        "namingDecision",
        "priceProposal"
      ],
      "required": [
        "id",
        "key",
        "catalogLabel",
        "workName",
        "name",
        "egg",
        "region",
        "recipe",
        "unlock",
        "edible",
        "viewable",
        "signature",
        "tags",
        "exploration",
        "art",
        "description",
        "clue",
        "menus",
        "collections",
        "orders",
        "regulars",
        "projects",
        "uses",
        "relatedCards",
        "specials",
        "displayMenus",
        "businessExclusion",
        "regularRelation",
        "namingDecision",
        "priceProposal"
      ]
    },
    "/species/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/key": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/catalogLabel": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/workName": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/name": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/egg": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/region": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "mode",
        "toolId",
        "toolLevel",
        "kitchenLevel",
        "ingredients",
        "exact",
        "extraIngredientsAllowed",
        "candidateKind",
        "firstChance",
        "hardAttempt",
        "repeatGuaranteed",
        "companionPolicy",
        "materialSupplyRequired",
        "oldSupply"
      ],
      "required": [
        "id",
        "mode",
        "toolId",
        "toolLevel",
        "kitchenLevel",
        "ingredients",
        "exact",
        "extraIngredientsAllowed",
        "candidateKind",
        "firstChance",
        "hardAttempt",
        "repeatGuaranteed",
        "companionPolicy",
        "materialSupplyRequired",
        "oldSupply"
      ]
    },
    "/species/*/recipe/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/mode": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/toolId": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/toolLevel": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/kitchenLevel": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/ingredients": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/ingredients/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "quantity"
      ],
      "required": [
        "id",
        "quantity"
      ]
    },
    "/species/*/recipe/ingredients/*/id": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/ingredients/*/quantity": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/exact": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/extraIngredientsAllowed": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/candidateKind": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/firstChance": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/hardAttempt": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/repeatGuaranteed": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/companionPolicy": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/materialSupplyRequired": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/materialSupplyRequired/*": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/oldSupply": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/unlock": {
      "types": [
        "object"
      ],
      "allowed": [
        "region",
        "identifiedMaterials",
        "firstSpecimenForOldOnly",
        "card",
        "duckLicense",
        "fullMethodRequired",
        "oldSupplyRules",
        "minimumKitchen",
        "technologyNote"
      ],
      "required": [
        "region",
        "identifiedMaterials",
        "firstSpecimenForOldOnly",
        "card",
        "duckLicense",
        "fullMethodRequired",
        "oldSupplyRules",
        "minimumKitchen",
        "technologyNote"
      ]
    },
    "/species/*/unlock/region": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/unlock/identifiedMaterials": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/unlock/identifiedMaterials/*": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/unlock/firstSpecimenForOldOnly": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/unlock/card": {
      "types": [
        "null",
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/unlock/duckLicense": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/unlock/fullMethodRequired": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/unlock/oldSupplyRules": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/unlock/minimumKitchen": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/unlock/technologyNote": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/edible": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/viewable": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/signature": {
      "types": [
        "string",
        "null"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/tags": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/tags/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/exploration": {
      "types": [
        "object"
      ],
      "allowed": [
        "G",
        "F",
        "environment",
        "traits"
      ],
      "required": [
        "G",
        "F",
        "environment",
        "traits"
      ]
    },
    "/species/*/exploration/G": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/exploration/F": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/exploration/environment": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/exploration/traits": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/exploration/traits/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/art": {
      "types": [
        "object"
      ],
      "allowed": [
        "body",
        "foodAndMaterial",
        "identity",
        "contrast",
        "portrait",
        "silhouette",
        "palette"
      ],
      "required": [
        "body",
        "foodAndMaterial",
        "identity",
        "contrast",
        "portrait",
        "silhouette",
        "palette"
      ]
    },
    "/species/*/art/body": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/art/foodAndMaterial": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/art/identity": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/art/contrast": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/art/portrait": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/art/silhouette": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/art/palette": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/description": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/clue": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/menus": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/menus/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/collections": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/collections/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/orders": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/orders/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/regulars": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/regulars/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/projects": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/projects/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/uses": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/uses/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "system",
        "target",
        "action"
      ],
      "required": [
        "system",
        "target",
        "action"
      ]
    },
    "/species/*/uses/*/system": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/uses/*/target": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/uses/*/action": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/relatedCards": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/relatedCards/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/specials": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/specials/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/displayMenus": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/businessExclusion": {
      "types": [
        "null",
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/regularRelation": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/namingDecision": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/priceProposal": {
      "types": [
        "object"
      ],
      "allowed": [
        "baseSaleCP",
        "collectCP",
        "status",
        "ornamentalCostAllowed"
      ],
      "required": [
        "baseSaleCP",
        "collectCP",
        "status",
        "ornamentalCostAllowed"
      ]
    },
    "/species/*/priceProposal/baseSaleCP": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/priceProposal/collectCP": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/priceProposal/status": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/priceProposal/ornamentalCostAllowed": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/oldSupply/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "name",
        "anyOf"
      ],
      "required": [
        "id",
        "name",
        "anyOf"
      ]
    },
    "/species/*/recipe/oldSupply/*/id": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/oldSupply/*/name": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/oldSupply/*/anyOf": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/oldSupply/*/anyOf/*": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/oldSupply/*/anyOf/*/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "kind",
        "id",
        "level",
        "egg"
      ],
      "required": [
        "kind",
        "id"
      ]
    },
    "/species/*/recipe/oldSupply/*/anyOf/*/*/kind": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/oldSupply/*/anyOf/*/*/id": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/oldSupply/*/anyOf/*/*/level": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipe/oldSupply/*/anyOf/*/*/egg": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/displayMenus/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/materials": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "stableId",
        "name",
        "region",
        "priceCP",
        "specimen",
        "specimenName",
        "recognition",
        "lore",
        "shop",
        "entrySpecies",
        "oldKeys",
        "oldUse",
        "event",
        "art",
        "uses",
        "tools",
        "states",
        "identification",
        "entryGate"
      ],
      "required": [
        "id",
        "stableId",
        "name",
        "region",
        "priceCP",
        "specimen",
        "specimenName",
        "recognition",
        "lore",
        "shop",
        "entrySpecies",
        "oldKeys",
        "oldUse",
        "event",
        "art",
        "uses",
        "tools",
        "states",
        "identification",
        "entryGate"
      ]
    },
    "/materials/*/id": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/stableId": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/name": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/region": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/priceCP": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/specimen": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/specimenName": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/recognition": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/lore": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/shop": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/entrySpecies": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/oldKeys": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/oldKeys/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/oldUse": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/event": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/art": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/uses": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/uses/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/tools": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/tools/*": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/states": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/states/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/identification": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/entryGate": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/alternatives": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/alternatives/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "region",
        "target",
        "name",
        "toolId",
        "toolLevel",
        "ingredients",
        "replaces",
        "unlock",
        "tradeoff",
        "mode",
        "quantityPerIngredient",
        "guarantee",
        "kitchenLevel",
        "fullMethod"
      ],
      "required": [
        "id",
        "region",
        "target",
        "name",
        "toolId",
        "toolLevel",
        "ingredients",
        "replaces",
        "unlock",
        "tradeoff",
        "mode",
        "quantityPerIngredient",
        "guarantee",
        "kitchenLevel",
        "fullMethod"
      ]
    },
    "/alternatives/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/alternatives/*/region": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/alternatives/*/target": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/alternatives/*/name": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/alternatives/*/toolId": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/alternatives/*/toolLevel": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/alternatives/*/ingredients": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/alternatives/*/ingredients/*": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/alternatives/*/replaces": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/alternatives/*/replaces/*": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/alternatives/*/unlock": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/alternatives/*/tradeoff": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/alternatives/*/mode": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/alternatives/*/quantityPerIngredient": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/alternatives/*/guarantee": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/alternatives/*/kitchenLevel": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/alternatives/*/fullMethod": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/cards": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "region",
        "place",
        "placeIndex",
        "type",
        "title",
        "hint",
        "focus",
        "team",
        "gate",
        "result",
        "next",
        "material",
        "art",
        "protection",
        "effects",
        "discoveryChance",
        "exchange"
      ],
      "required": [
        "id",
        "region",
        "place",
        "placeIndex",
        "type",
        "title",
        "hint",
        "focus",
        "team",
        "gate",
        "result",
        "next",
        "material",
        "art",
        "protection",
        "effects",
        "discoveryChance"
      ]
    },
    "/cards/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/region": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/place": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/placeIndex": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/type": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/title": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/hint": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/focus": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/team": {
      "types": [
        "object"
      ],
      "allowed": [
        "trait",
        "environment",
        "sameMemberMaySatisfyBoth",
        "oldExamples",
        "maxPredicates",
        "bonusEnvironment",
        "bonusTrait"
      ],
      "required": [
        "trait",
        "environment",
        "sameMemberMaySatisfyBoth",
        "oldExamples",
        "maxPredicates"
      ]
    },
    "/cards/*/team/trait": {
      "types": [
        "null",
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/team/environment": {
      "types": [
        "null",
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/team/sameMemberMaySatisfyBoth": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/team/oldExamples": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/team/oldExamples/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/team/maxPredicates": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/gate": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/result": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/next": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/material": {
      "types": [
        "number",
        "null"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/art": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/protection": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/effects": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/effects/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "kind",
        "material",
        "target"
      ],
      "required": [
        "kind"
      ]
    },
    "/cards/*/effects/*/kind": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/effects/*/material": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/effects/*/target": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/discoveryChance": {
      "types": [
        "object"
      ],
      "allowed": [
        "base",
        "perTeamF",
        "cap",
        "bonus",
        "bonusCondition",
        "bonusMaximumApplications"
      ],
      "required": [
        "base",
        "perTeamF",
        "cap",
        "bonus",
        "bonusCondition",
        "bonusMaximumApplications"
      ]
    },
    "/cards/*/discoveryChance/base": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/discoveryChance/perTeamF": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/discoveryChance/cap": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/discoveryChance/bonus": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/discoveryChance/bonusCondition": {
      "types": [
        "null",
        "object"
      ],
      "allowed": [
        "trait",
        "environment"
      ],
      "required": []
    },
    "/cards/*/discoveryChance/bonusMaximumApplications": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/discoveryChance/bonusCondition/trait": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/team/bonusEnvironment": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/discoveryChance/bonusCondition/environment": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/team/bonusTrait": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/exchange": {
      "types": [
        "object"
      ],
      "allowed": [
        "optional",
        "quantity",
        "pool",
        "rewardMaterial",
        "replacesBaseSlot",
        "noBaseCP",
        "ordinaryResultSameCard",
        "extraTeamStock"
      ],
      "required": [
        "optional",
        "quantity",
        "pool",
        "rewardMaterial",
        "replacesBaseSlot",
        "noBaseCP",
        "ordinaryResultSameCard",
        "extraTeamStock"
      ]
    },
    "/cards/*/exchange/optional": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/exchange/quantity": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/exchange/pool": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/exchange/rewardMaterial": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/exchange/replacesBaseSlot": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/exchange/noBaseCP": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/exchange/ordinaryResultSameCard": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/exchange/extraTeamStock": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/guide": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "countAsDiscoveryCard",
        "sourceRegion",
        "sourcePlace",
        "hint",
        "gate",
        "team",
        "trigger",
        "result",
        "effect"
      ],
      "required": [
        "id",
        "countAsDiscoveryCard",
        "sourceRegion",
        "sourcePlace",
        "hint",
        "gate",
        "team",
        "trigger",
        "result",
        "effect"
      ]
    },
    "/guide/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/guide/countAsDiscoveryCard": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/guide/sourceRegion": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/guide/sourcePlace": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/guide/hint": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/guide/gate": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/guide/team": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/guide/trigger": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/guide/result": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/guide/effect": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/menus": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "name",
        "roles",
        "complete",
        "unlock",
        "text",
        "orders",
        "regulars",
        "projects",
        "oneSpeciesOneRole",
        "maxSpecies",
        "validService",
        "tiers",
        "perBirdBonusCapCP",
        "examples"
      ],
      "required": [
        "id",
        "name",
        "roles",
        "complete",
        "unlock",
        "text",
        "orders",
        "regulars",
        "projects",
        "oneSpeciesOneRole",
        "maxSpecies",
        "validService",
        "tiers",
        "perBirdBonusCapCP",
        "examples"
      ]
    },
    "/menus/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/name": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/roles": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/roles/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "name",
        "required",
        "allowed",
        "maxSpecies"
      ],
      "required": [
        "id",
        "name",
        "required",
        "allowed",
        "maxSpecies"
      ]
    },
    "/menus/*/roles/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/roles/*/name": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/roles/*/required": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/roles/*/allowed": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/roles/*/allowed/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/roles/*/maxSpecies": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/complete": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/unlock": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/text": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/orders": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/orders/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/regulars": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/regulars/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/projects": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/projects/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/oneSpeciesOneRole": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/maxSpecies": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/validService": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/tiers": {
      "types": [
        "object"
      ],
      "allowed": [
        "ordinary",
        "suitable",
        "complete"
      ],
      "required": [
        "ordinary",
        "suitable",
        "complete"
      ]
    },
    "/menus/*/tiers/ordinary": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/tiers/suitable": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/tiers/complete": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/perBirdBonusCapCP": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/examples": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/examples/*": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/examples/*/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "quantity"
      ],
      "required": [
        "id",
        "quantity"
      ]
    },
    "/menus/*/examples/*/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/examples/*/*/quantity": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors": {
      "types": [
        "object"
      ],
      "allowed": [
        "home",
        "tea",
        "meal",
        "portableMeal",
        "fresh",
        "snack",
        "savory",
        "sweet",
        "stew",
        "floral",
        "season",
        "display",
        "food",
        "steam",
        "savoryMeal"
      ],
      "required": [
        "home",
        "tea",
        "meal",
        "portableMeal",
        "fresh",
        "snack",
        "savory",
        "sweet",
        "stew",
        "floral",
        "season",
        "display",
        "food",
        "steam",
        "savoryMeal"
      ]
    },
    "/selectors/home": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/home/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/tea": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/tea/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/meal": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/meal/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/portableMeal": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/portableMeal/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/fresh": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/fresh/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/snack": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/snack/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/savory": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/savory/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/sweet": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/sweet/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/stew": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/stew/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/floral": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/floral/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/season": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/season/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/display": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/display/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/food": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/food/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/steam": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/steam/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/savoryMeal": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/selectors/savoryMeal/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "name",
        "kind",
        "repeatable",
        "groups",
        "minimumDistinct",
        "bonusCP",
        "menu",
        "card",
        "regular",
        "project",
        "request",
        "finish",
        "firstResult",
        "qualification",
        "payment",
        "variants",
        "displayDistinct",
        "seasonRule"
      ],
      "required": [
        "id",
        "name",
        "kind",
        "repeatable",
        "groups",
        "minimumDistinct",
        "bonusCP",
        "menu",
        "card",
        "regular",
        "project",
        "request",
        "finish",
        "firstResult",
        "qualification",
        "payment",
        "variants"
      ]
    },
    "/orders/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/name": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/kind": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/repeatable": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/groups": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/groups/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "selector",
        "quantity",
        "allowed"
      ],
      "required": [
        "id",
        "selector",
        "quantity",
        "allowed"
      ]
    },
    "/orders/*/groups/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/groups/*/selector": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/groups/*/quantity": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/groups/*/allowed": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/groups/*/allowed/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/minimumDistinct": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/bonusCP": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/menu": {
      "types": [
        "string",
        "null"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/card": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/regular": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/project": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/request": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/finish": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/firstResult": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "name",
        "repeatReward"
      ],
      "required": [
        "id",
        "name",
        "repeatReward"
      ]
    },
    "/orders/*/firstResult/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/firstResult/name": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/firstResult/repeatReward": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/qualification": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/payment": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/variants": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/variants/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "label",
        "text",
        "egg",
        "allowed",
        "regions"
      ],
      "required": [
        "id",
        "label",
        "text"
      ]
    },
    "/orders/*/variants/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/variants/*/label": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/variants/*/text": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/variants/*/egg": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/variants/*/allowed": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/variants/*/allowed/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/displayDistinct": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/variants/*/regions": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/variants/*/regions/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/orders/*/seasonRule": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regulars": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/regulars/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "name",
        "region",
        "memento",
        "stages",
        "delivery",
        "completion"
      ],
      "required": [
        "id",
        "name",
        "region",
        "memento",
        "stages",
        "delivery",
        "completion"
      ]
    },
    "/regulars/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regulars/*/name": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regulars/*/region": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regulars/*/memento": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regulars/*/stages": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/regulars/*/stages/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "title",
        "gate",
        "alternatives",
        "text",
        "next",
        "reward",
        "recordPolicy"
      ],
      "required": [
        "id",
        "title",
        "gate",
        "alternatives",
        "text",
        "next",
        "reward",
        "recordPolicy"
      ]
    },
    "/regulars/*/stages/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regulars/*/stages/*/title": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regulars/*/stages/*/gate": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regulars/*/stages/*/alternatives": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/regulars/*/stages/*/alternatives/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regulars/*/stages/*/text": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regulars/*/stages/*/next": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regulars/*/stages/*/reward": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regulars/*/stages/*/recordPolicy": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regulars/*/delivery": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regulars/*/completion": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/projects": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "name",
        "gate",
        "stages",
        "oldAllowed",
        "newAllowed",
        "result",
        "text",
        "next",
        "art",
        "optionalDisplay"
      ],
      "required": [
        "id",
        "name",
        "gate",
        "stages",
        "oldAllowed",
        "newAllowed",
        "result",
        "text",
        "next",
        "art"
      ]
    },
    "/projects/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/name": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/gate": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/stages": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/stages/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "check",
        "consume",
        "costCP"
      ],
      "required": [
        "id",
        "check",
        "consume",
        "costCP"
      ]
    },
    "/projects/*/stages/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/stages/*/check": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/stages/*/consume": {
      "types": [
        "null",
        "object"
      ],
      "allowed": [
        "distinct",
        "quantityEach",
        "allowed",
        "total",
        "minimumSignatureCategories",
        "minimumPerCategory"
      ],
      "required": [
        "allowed"
      ]
    },
    "/projects/*/stages/*/costCP": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/oldAllowed": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/oldAllowed/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/newAllowed": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/newAllowed/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/result": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/text": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/next": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/art": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/stages/*/consume/distinct": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/stages/*/consume/quantityEach": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/stages/*/consume/allowed": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/stages/*/consume/allowed/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/stages/*/consume/total": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/stages/*/consume/minimumSignatureCategories": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/stages/*/consume/minimumPerCategory": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/optionalDisplay": {
      "types": [
        "object"
      ],
      "allowed": [
        "allowed",
        "minimum",
        "consume",
        "rule"
      ],
      "required": [
        "allowed",
        "minimum",
        "consume",
        "rule"
      ]
    },
    "/projects/*/optionalDisplay/allowed": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/optionalDisplay/allowed/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/optionalDisplay/minimum": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/optionalDisplay/consume": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/optionalDisplay/rule": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "kind",
        "name",
        "menu",
        "region",
        "oldKeys",
        "newIds",
        "fixed",
        "optional",
        "stageRules",
        "displayGuests",
        "practice",
        "rewards",
        "text",
        "next",
        "stages"
      ],
      "required": [
        "id",
        "kind",
        "name",
        "region",
        "oldKeys",
        "newIds",
        "fixed",
        "optional",
        "practice",
        "rewards",
        "text",
        "next"
      ]
    },
    "/collections/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/kind": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/name": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/menu": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/region": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/oldKeys": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/oldKeys/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/newIds": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/newIds/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/fixed": {
      "types": [
        "object"
      ],
      "allowed": [
        "kind",
        "allowed",
        "note"
      ],
      "required": [
        "kind",
        "allowed"
      ]
    },
    "/collections/*/fixed/kind": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/fixed/allowed": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/fixed/allowed/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/fixed/note": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/optional": {
      "types": [
        "object"
      ],
      "allowed": [
        "allowed",
        "countStage1",
        "countStage2",
        "representativeIncludedInCount",
        "countComplete"
      ],
      "required": [
        "allowed",
        "countStage1",
        "countStage2"
      ]
    },
    "/collections/*/optional/allowed": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/optional/allowed/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/optional/countStage1": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/optional/countStage2": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/optional/representativeIncludedInCount": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/stageRules": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/displayGuests": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/displayGuests/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/practice": {
      "types": [
        "object"
      ],
      "allowed": [
        "any",
        "fullMenuStamp",
        "reward"
      ],
      "required": [
        "any"
      ]
    },
    "/collections/*/practice/any": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/practice/any/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/practice/fullMenuStamp": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/rewards": {
      "types": [
        "array",
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/rewards/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "stage",
        "id",
        "kind",
        "text"
      ],
      "required": [
        "stage",
        "id",
        "kind",
        "text"
      ]
    },
    "/collections/*/rewards/*/stage": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/rewards/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/rewards/*/kind": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/rewards/*/text": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/text": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/next": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/optional/countComplete": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/stages": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/stages/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "requires",
        "reward"
      ],
      "required": [
        "id",
        "requires",
        "reward"
      ]
    },
    "/collections/*/stages/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/stages/*/requires": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/stages/*/reward": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/collections/*/practice/reward": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/specials": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/specials/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "name",
        "theme",
        "oldKeys",
        "newIds",
        "fixed",
        "optional",
        "practice",
        "reward",
        "text",
        "next"
      ],
      "required": [
        "id",
        "name",
        "theme",
        "oldKeys",
        "newIds",
        "fixed",
        "optional",
        "practice",
        "reward",
        "text",
        "next"
      ]
    },
    "/specials/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/specials/*/name": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/specials/*/theme": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/specials/*/oldKeys": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/specials/*/oldKeys/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/specials/*/newIds": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/specials/*/newIds/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/specials/*/fixed": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/specials/*/optional": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/specials/*/practice": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/specials/*/reward": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/specials/*/text": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/specials/*/next": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/specials/*/fixed/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/mementos": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/mementos/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "name",
        "source",
        "art",
        "text",
        "once",
        "displaySlots",
        "effect",
        "unlock",
        "next"
      ],
      "required": [
        "id",
        "name",
        "source",
        "art",
        "text",
        "once",
        "displaySlots",
        "effect",
        "unlock",
        "next"
      ]
    },
    "/mementos/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/mementos/*/name": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/mementos/*/source": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/mementos/*/art": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/mementos/*/text": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/mementos/*/once": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/mementos/*/displaySlots": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/mementos/*/effect": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/mementos/*/unlock": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/mementos/*/next": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/decisions": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/decisions/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "issue",
        "resolution"
      ],
      "required": [
        "issue",
        "resolution"
      ]
    },
    "/decisions/*/issue": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/decisions/*/resolution": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/quail": {
      "types": [
        "object"
      ],
      "allowed": [
        "status",
        "inputs"
      ],
      "required": [
        "status",
        "inputs"
      ]
    },
    "/quail/status": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/quail/inputs": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/quail/inputs/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/productionRules": {
      "types": [
        "object"
      ],
      "allowed": [
        "sourceOfTruth",
        "knowledge",
        "method",
        "trial",
        "companions",
        "prices",
        "timing",
        "unlock",
        "ui"
      ],
      "required": [
        "sourceOfTruth",
        "knowledge",
        "method",
        "trial",
        "companions",
        "prices",
        "timing",
        "unlock",
        "ui"
      ]
    },
    "/productionRules/sourceOfTruth": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/productionRules/knowledge": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/productionRules/method": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/productionRules/trial": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/productionRules/companions": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/productionRules/prices": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/productionRules/timing": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/productionRules/unlock": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/productionRules/ui": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/paperRecords": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/paperRecords/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "name",
        "source",
        "text",
        "art",
        "once"
      ],
      "required": [
        "id",
        "name",
        "source",
        "text",
        "art",
        "once"
      ]
    },
    "/paperRecords/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/paperRecords/*/name": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/paperRecords/*/source": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/paperRecords/*/text": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/paperRecords/*/art": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/paperRecords/*/once": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    }
  },
  "baseline": {
    "": {
      "types": [
        "object"
      ],
      "allowed": [
        "species",
        "ingredients",
        "tools",
        "notes"
      ],
      "required": [
        "species",
        "ingredients",
        "tools",
        "notes"
      ]
    },
    "/species": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "key",
        "name",
        "egg",
        "description",
        "signature",
        "edible",
        "tags",
        "exploration",
        "recipes",
        "basePrice",
        "season",
        "participation",
        "artReference",
        "menus",
        "orders",
        "collections",
        "specials"
      ],
      "required": [
        "key",
        "name",
        "egg",
        "description",
        "signature",
        "edible",
        "tags",
        "exploration",
        "recipes",
        "basePrice",
        "season",
        "participation",
        "artReference",
        "menus",
        "orders",
        "collections",
        "specials"
      ]
    },
    "/species/*/key": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/name": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/egg": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/description": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/signature": {
      "types": [
        "string",
        "null"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/edible": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/tags": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/tags/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/exploration": {
      "types": [
        "object"
      ],
      "allowed": [
        "G",
        "F",
        "environment",
        "traits"
      ],
      "required": [
        "G",
        "F",
        "environment",
        "traits"
      ]
    },
    "/species/*/exploration/G": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/exploration/F": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/exploration/environment": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/exploration/traits": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/exploration/traits/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipes": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipes/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "egg",
        "id",
        "toolId",
        "minLevel",
        "minKitchen",
        "ingredients",
        "kind",
        "sourceLine",
        "note",
        "campaign",
        "collectionTotal",
        "time",
        "chapter"
      ],
      "required": [
        "egg",
        "id",
        "toolId",
        "minLevel",
        "ingredients",
        "kind"
      ]
    },
    "/species/*/recipes/*/egg": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipes/*/id": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipes/*/toolId": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipes/*/minLevel": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipes/*/minKitchen": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipes/*/ingredients": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipes/*/kind": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipes/*/sourceLine": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/basePrice": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/season": {
      "types": [
        "null",
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/participation": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/participation/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/artReference": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/menus": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/menus/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/orders": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/orders/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/collections": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/collections/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/specials": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/specials/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipes/*/note": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipes/*/ingredients/*": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipes/*/campaign": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipes/*/collectionTotal": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipes/*/collectionTotal/*": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipes/*/time": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/recipes/*/chapter": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/ingredients": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/ingredients/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "name",
        "price",
        "unlockAlternatives",
        "special"
      ],
      "required": [
        "id",
        "name",
        "price",
        "unlockAlternatives",
        "special"
      ]
    },
    "/ingredients/*/id": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/ingredients/*/name": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/ingredients/*/price": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/ingredients/*/unlockAlternatives": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/ingredients/*/unlockAlternatives/*": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/ingredients/*/special": {
      "types": [
        "boolean"
      ],
      "allowed": [],
      "required": null
    },
    "/ingredients/*/unlockAlternatives/*/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "kind",
        "id",
        "level",
        "egg",
        "count"
      ],
      "required": [
        "kind"
      ]
    },
    "/ingredients/*/unlockAlternatives/*/*/kind": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/ingredients/*/unlockAlternatives/*/*/id": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/ingredients/*/unlockAlternatives/*/*/level": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/ingredients/*/unlockAlternatives/*/*/egg": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/ingredients/*/unlockAlternatives/*/*/count": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/tools": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/tools/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "name",
        "cookCP"
      ],
      "required": [
        "id",
        "name",
        "cookCP"
      ]
    },
    "/tools/*/id": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/tools/*/name": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/tools/*/cookCP": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/notes": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/notes/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    }
  },
  "art-manifest": {
    "": {
      "types": [
        "object"
      ],
      "allowed": [
        "style",
        "sizes",
        "species",
        "materials",
        "cards",
        "mementos",
        "regions",
        "projects",
        "menus",
        "specials",
        "qa",
        "totals"
      ],
      "required": [
        "style",
        "sizes",
        "species",
        "materials",
        "cards",
        "mementos",
        "regions",
        "projects",
        "menus",
        "specials",
        "qa",
        "totals"
      ]
    },
    "/style": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/sizes": {
      "types": [
        "object"
      ],
      "allowed": [
        "speciesMaster",
        "portrait",
        "silhouette",
        "material",
        "card",
        "memento",
        "region"
      ],
      "required": [
        "speciesMaster",
        "portrait",
        "silhouette",
        "material",
        "card",
        "memento",
        "region"
      ]
    },
    "/sizes/speciesMaster": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/sizes/portrait": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/sizes/silhouette": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/sizes/material": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/sizes/card": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/sizes/memento": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/sizes/region": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "content",
        "deliverables",
        "body",
        "foodAndMaterial",
        "identity",
        "contrast",
        "portrait",
        "silhouette",
        "palette"
      ],
      "required": [
        "id",
        "content",
        "deliverables",
        "body",
        "foodAndMaterial",
        "identity",
        "contrast",
        "portrait",
        "silhouette",
        "palette"
      ]
    },
    "/species/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/content": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/deliverables": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/deliverables/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/body": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/foodAndMaterial": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/identity": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/contrast": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/portrait": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/silhouette": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/species/*/palette": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/materials": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "content",
        "brief",
        "deliverables"
      ],
      "required": [
        "id",
        "content",
        "brief",
        "deliverables"
      ]
    },
    "/materials/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/content": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/brief": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/deliverables": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/materials/*/deliverables/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "content",
        "brief",
        "reuse",
        "deliverables"
      ],
      "required": [
        "id",
        "content",
        "brief",
        "reuse",
        "deliverables"
      ]
    },
    "/cards/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/content": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/brief": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/reuse": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/deliverables": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/cards/*/deliverables/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/mementos": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/mementos/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "content",
        "brief",
        "deliverables"
      ],
      "required": [
        "id",
        "content",
        "brief",
        "deliverables"
      ]
    },
    "/mementos/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/mementos/*/content": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/mementos/*/brief": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/mementos/*/deliverables": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/mementos/*/deliverables/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regions": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "content",
        "brief",
        "places",
        "deliverables"
      ],
      "required": [
        "id",
        "content",
        "brief",
        "places",
        "deliverables"
      ]
    },
    "/regions/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/content": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/brief": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/places": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/places/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/deliverables": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/regions/*/deliverables/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/projects": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "content",
        "brief",
        "deliverables"
      ],
      "required": [
        "id",
        "content",
        "brief",
        "deliverables"
      ]
    },
    "/projects/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/content": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/brief": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/deliverables": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/projects/*/deliverables/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/menus": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "content",
        "brief",
        "deliverables"
      ],
      "required": [
        "id",
        "content",
        "brief",
        "deliverables"
      ]
    },
    "/menus/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/content": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/brief": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/deliverables": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/menus/*/deliverables/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/specials": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/specials/*": {
      "types": [
        "object"
      ],
      "allowed": [
        "id",
        "content",
        "brief",
        "deliverables"
      ],
      "required": [
        "id",
        "content",
        "brief",
        "deliverables"
      ]
    },
    "/specials/*/id": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/specials/*/content": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/specials/*/brief": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/specials/*/deliverables": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/specials/*/deliverables/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/qa": {
      "types": [
        "array"
      ],
      "allowed": [],
      "required": null
    },
    "/qa/*": {
      "types": [
        "string"
      ],
      "allowed": [],
      "required": null
    },
    "/totals": {
      "types": [
        "object"
      ],
      "allowed": [
        "newSpeciesMaster",
        "derivedPortrait",
        "derivedSilhouette",
        "materialSets",
        "discoveryVignettes",
        "standaloneMementos",
        "regionInserts",
        "placeCrops",
        "projectComponentSets",
        "menuCrops",
        "specialOverlays",
        "fullNewKitchenScenes"
      ],
      "required": [
        "newSpeciesMaster",
        "derivedPortrait",
        "derivedSilhouette",
        "materialSets",
        "discoveryVignettes",
        "standaloneMementos",
        "regionInserts",
        "placeCrops",
        "projectComponentSets",
        "menuCrops",
        "specialOverlays",
        "fullNewKitchenScenes"
      ]
    },
    "/totals/newSpeciesMaster": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/totals/derivedPortrait": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/totals/derivedSilhouette": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/totals/materialSets": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/totals/discoveryVignettes": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/totals/standaloneMementos": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/totals/regionInserts": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/totals/placeCrops": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/totals/projectComponentSets": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/totals/menuCrops": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/totals/specialOverlays": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    },
    "/totals/fullNewKitchenScenes": {
      "types": [
        "number"
      ],
      "allowed": [],
      "required": null
    }
  }
};

export const CONDITION_SOURCE_HASHES = {
  "V:gate": "6c1a8d31d71a7ad78b1397ecfa71b2019dd4a50fb5c9ccbba2f35f8f7712acf8",
  "R:gate": "fc255253f14d314365580c0a0d154d84029132450f5603b27110a9c7174a81d3",
  "T:gate": "a7355e028b315d0f5da009b81077a7670d785de1ca4314992a8226e58d42229d",
  "B:gate": "41491068b0ebc1d5ef002ed6ba2754ba828cc8d652c2b1920b4084d93250e0a3",
  "V-C1:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "V-C1:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "V-C1:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "V-C2:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "V-C2:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "V-C2:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "V-C3:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "V-C3:unlock.technologyNote": "15d83c188cab879186c052bdc620d6ac9d8f6b44ec15d132d172b388924dec93",
  "V-C3:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "V-C4:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "V-C4:unlock.technologyNote": "7faf3e7c9ce6ec1eb6be8177a46e1db03e224da912657ead5dfe83195bc83901",
  "V-C4:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "V-C5:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "V-C5:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "V-C5:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "V-C6:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "V-C6:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "V-C6:businessExclusion": "fea063f24affe12fc3d5c3e3e3dfc506cdc63430c4291e55d00275affb0c8af1",
  "V-C6:regularRelation": "36b744f326d57ec5beee53b939b72182844fe48fe1a8cb09186448c12fddcafe",
  "V-D1:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "V-D1:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "V-D1:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "V-D2:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "V-D2:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "V-D2:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "V-D3:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "V-D3:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "V-D3:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "V-D4:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "V-D4:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "V-D4:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "V-D5:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "V-D5:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "V-D5:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "V-D6:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "V-D6:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "V-D6:businessExclusion": "fea063f24affe12fc3d5c3e3e3dfc506cdc63430c4291e55d00275affb0c8af1",
  "V-D6:regularRelation": "36b744f326d57ec5beee53b939b72182844fe48fe1a8cb09186448c12fddcafe",
  "R-C1:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "R-C1:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "R-C1:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "R-C2:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "R-C2:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "R-C2:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "R-C3:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "R-C3:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "R-C3:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "R-C4:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "R-C4:unlock.technologyNote": "15d83c188cab879186c052bdc620d6ac9d8f6b44ec15d132d172b388924dec93",
  "R-C4:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "R-C5:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "R-C5:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "R-C5:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "R-C6:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "R-C6:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "R-C6:businessExclusion": "fea063f24affe12fc3d5c3e3e3dfc506cdc63430c4291e55d00275affb0c8af1",
  "R-C6:regularRelation": "36b744f326d57ec5beee53b939b72182844fe48fe1a8cb09186448c12fddcafe",
  "R-D1:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "R-D1:unlock.technologyNote": "bfbf9388451f864ee37dc5de905cbbc4a7d51deb9cec948eef717c4089f4445c",
  "R-D1:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "R-D2:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "R-D2:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "R-D2:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "R-D3:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "R-D3:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "R-D3:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "R-D4:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "R-D4:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "R-D4:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "R-D5:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "R-D5:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "R-D5:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "R-D6:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "R-D6:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "R-D6:businessExclusion": "fea063f24affe12fc3d5c3e3e3dfc506cdc63430c4291e55d00275affb0c8af1",
  "R-D6:regularRelation": "36b744f326d57ec5beee53b939b72182844fe48fe1a8cb09186448c12fddcafe",
  "T-C1:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "T-C1:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "T-C1:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "T-C2:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "T-C2:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "T-C2:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "T-C3:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "T-C3:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "T-C3:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "T-C4:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "T-C4:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "T-C4:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "T-C5:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "T-C5:unlock.technologyNote": "15d83c188cab879186c052bdc620d6ac9d8f6b44ec15d132d172b388924dec93",
  "T-C5:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "T-C6:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "T-C6:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "T-C6:businessExclusion": "fea063f24affe12fc3d5c3e3e3dfc506cdc63430c4291e55d00275affb0c8af1",
  "T-C6:regularRelation": "36b744f326d57ec5beee53b939b72182844fe48fe1a8cb09186448c12fddcafe",
  "T-D1:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "T-D1:unlock.technologyNote": "bfbf9388451f864ee37dc5de905cbbc4a7d51deb9cec948eef717c4089f4445c",
  "T-D1:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "T-D2:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "T-D2:unlock.technologyNote": "bfbf9388451f864ee37dc5de905cbbc4a7d51deb9cec948eef717c4089f4445c",
  "T-D2:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "T-D3:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "T-D3:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "T-D3:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "T-D4:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "T-D4:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "T-D4:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "T-D5:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "T-D5:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "T-D5:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "T-D6:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "T-D6:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "T-D6:businessExclusion": "fea063f24affe12fc3d5c3e3e3dfc506cdc63430c4291e55d00275affb0c8af1",
  "T-D6:regularRelation": "36b744f326d57ec5beee53b939b72182844fe48fe1a8cb09186448c12fddcafe",
  "B-C1:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "B-C1:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "B-C1:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "B-C2:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "B-C2:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "B-C2:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "B-C3:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "B-C3:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "B-C3:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "B-C4:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "B-C4:unlock.technologyNote": "15d83c188cab879186c052bdc620d6ac9d8f6b44ec15d132d172b388924dec93",
  "B-C4:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "B-C5:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "B-C5:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "B-C5:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "B-C6:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "B-C6:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "B-C6:businessExclusion": "fea063f24affe12fc3d5c3e3e3dfc506cdc63430c4291e55d00275affb0c8af1",
  "B-C6:regularRelation": "36b744f326d57ec5beee53b939b72182844fe48fe1a8cb09186448c12fddcafe",
  "B-D1:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "B-D1:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "B-D1:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "B-D2:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "B-D2:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "B-D2:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "B-D3:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "B-D3:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "B-D3:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "B-D4:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "B-D4:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "B-D4:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "B-D5:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "B-D5:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "B-D5:regularRelation": "ec688329cfb069a565f08eb1bc099adcbc9b7fb2b9b0beb3c580876e5a3db7f6",
  "B-D6:unlock.oldSupplyRules": "ffaa1dda5026b119d4138331af66ccd1a525ee65d399664bc556937088be611d",
  "B-D6:unlock.technologyNote": "4475ffdf7c4bf405a3e922ece6f25d6d67e03e0760a8efc17a3b2bb9a69adb54",
  "B-D6:businessExclusion": "fea063f24affe12fc3d5c3e3e3dfc506cdc63430c4291e55d00275affb0c8af1",
  "B-D6:regularRelation": "36b744f326d57ec5beee53b939b72182844fe48fe1a8cb09186448c12fddcafe",
  "75:identification": "544605f9d44dd3691fea4a04d5b37a8e2f7659b82162422889fd51a2df1b67af",
  "75:entryGate": "3cdb69fd1dce779bc23cafa38ed6b111740215f81cbc725795467affe543230c",
  "76:identification": "544605f9d44dd3691fea4a04d5b37a8e2f7659b82162422889fd51a2df1b67af",
  "76:entryGate": "3cdb69fd1dce779bc23cafa38ed6b111740215f81cbc725795467affe543230c",
  "77:identification": "544605f9d44dd3691fea4a04d5b37a8e2f7659b82162422889fd51a2df1b67af",
  "77:entryGate": "3cdb69fd1dce779bc23cafa38ed6b111740215f81cbc725795467affe543230c",
  "78:identification": "544605f9d44dd3691fea4a04d5b37a8e2f7659b82162422889fd51a2df1b67af",
  "78:entryGate": "3cdb69fd1dce779bc23cafa38ed6b111740215f81cbc725795467affe543230c",
  "79:identification": "544605f9d44dd3691fea4a04d5b37a8e2f7659b82162422889fd51a2df1b67af",
  "79:entryGate": "3cdb69fd1dce779bc23cafa38ed6b111740215f81cbc725795467affe543230c",
  "80:identification": "544605f9d44dd3691fea4a04d5b37a8e2f7659b82162422889fd51a2df1b67af",
  "80:entryGate": "3cdb69fd1dce779bc23cafa38ed6b111740215f81cbc725795467affe543230c",
  "81:identification": "544605f9d44dd3691fea4a04d5b37a8e2f7659b82162422889fd51a2df1b67af",
  "81:entryGate": "3cdb69fd1dce779bc23cafa38ed6b111740215f81cbc725795467affe543230c",
  "82:identification": "544605f9d44dd3691fea4a04d5b37a8e2f7659b82162422889fd51a2df1b67af",
  "82:entryGate": "3cdb69fd1dce779bc23cafa38ed6b111740215f81cbc725795467affe543230c",
  "ALT-V:guarantee": "3be836f1f16a9da0978d5648b07a2f940a3100cefbf23ca0ed4c462632d5fd0f",
  "ALT-R:guarantee": "3be836f1f16a9da0978d5648b07a2f940a3100cefbf23ca0ed4c462632d5fd0f",
  "ALT-T:guarantee": "3be836f1f16a9da0978d5648b07a2f940a3100cefbf23ca0ed4c462632d5fd0f",
  "ALT-B:guarantee": "3be836f1f16a9da0978d5648b07a2f940a3100cefbf23ca0ed4c462632d5fd0f",
  "V-S1:gate": "7b631709cff64301fcbe4c764553176f17e5f7b03bb077a0512ce5f1139ea463",
  "V-S1:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "V-S2:gate": "58111a68db5b21361f4a6dbe47689e9152af44132dea809a739da6204de1b1a3",
  "V-S2:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "V-N1:gate": "c86f161476a3be5c3396c0aea6e99caaad12888fb4f3397e2146072fb7fabd8d",
  "V-N1:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "V-N2:gate": "b7a201043cd5ec69e1a1fcda1030ba5940a73576bede85683a0e71bd6dab03d6",
  "V-N2:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "V-E1:gate": "c86f161476a3be5c3396c0aea6e99caaad12888fb4f3397e2146072fb7fabd8d",
  "V-E1:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "V-E2:gate": "b7a201043cd5ec69e1a1fcda1030ba5940a73576bede85683a0e71bd6dab03d6",
  "V-E2:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "R-S1:gate": "0b05afba4de9ac4b1ba23fa54dddf0790804cf6b7810ab4c80b298cacb2bd107",
  "R-S1:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "R-S2:gate": "41992d713edf71eda542665fe4aebd7a1145ab277be0a9f3a2034e9142c889a5",
  "R-S2:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "R-N1:gate": "ba248a60c54d51ead4da73792466d3e4d34a8bc1fd7885f5e1b84b7f7a9b0ce2",
  "R-N1:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "R-N2:gate": "c86f161476a3be5c3396c0aea6e99caaad12888fb4f3397e2146072fb7fabd8d",
  "R-N2:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "R-E1:gate": "66ec524f1a498ed8c92850d9e3b5e0b1b483e3003ef14f496816ce4a86d642dc",
  "R-E1:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "R-E2:gate": "c86f161476a3be5c3396c0aea6e99caaad12888fb4f3397e2146072fb7fabd8d",
  "R-E2:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "T-S1:gate": "943524e59db306fd4b1e9ea4928061a376e61e78a2653112770510036c2c3413",
  "T-S1:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "T-S2:gate": "3f8433fe509f00ce0b5e673ccadab5a1a345252814817255bddb0fc452ac8ae5",
  "T-S2:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "T-N1:gate": "ff27bbe7224d6a6f521b9041ff4314f226f684361930ab2410cbd60ed89eec87",
  "T-N1:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "T-N2:gate": "cbacc8d65d89b800bae79a812bf3f6dfea9adb80ff383f1f6d47c97f310bbabe",
  "T-N2:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "T-E1:gate": "ff27bbe7224d6a6f521b9041ff4314f226f684361930ab2410cbd60ed89eec87",
  "T-E1:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "T-E2:gate": "cbacc8d65d89b800bae79a812bf3f6dfea9adb80ff383f1f6d47c97f310bbabe",
  "T-E2:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "B-S1:gate": "373c87038dac59d7d0f5d4484ed8a6bc5d2c1aabc024ddc747cb3513e8349e32",
  "B-S1:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "B-S2:gate": "a02f9ce5197c4dd21512063339b9c4b7d4a23f0e2e5ba2b3049f4ff2e5594456",
  "B-S2:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "B-N1:gate": "620be356250f3cd61a09f3089af8c0910348b26526b6e983a0ed98b2eb6e28c3",
  "B-N1:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "B-N2:gate": "8f00ff51fc5c1c7463a76890ddb1fefc254b276b1653ba7b458d4b3715f97b07",
  "B-N2:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "B-E1:gate": "620be356250f3cd61a09f3089af8c0910348b26526b6e983a0ed98b2eb6e28c3",
  "B-E1:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "B-E2:gate": "66e360099428a7bcebbc23c249df6fb7bcc46cd39b71dd1081df6134504e640d",
  "B-E2:protection": "be82f1639e062c23dca8766a84ef10c7f2523e2f71997179fd7143e33eb930c8",
  "GUIDE-B:gate": "41491068b0ebc1d5ef002ed6ba2754ba828cc8d652c2b1920b4084d93250e0a3",
  "GUIDE-B:team": "97ff9ed70bdf3d48bff36183f38ac1a8fc06a95e34e6e8c85cad9dd5ccc16dec",
  "GUIDE-B:trigger": "949b479076552151d1c7fc056233574c7838b02190a9cb67566685205456a587",
  "GUIDE-B:effect": "849d68bae7228ab07354714b140d6ce00cc4595f0ea76f5c42e88d2179d362a6",
  "MN1:complete": "02ce6a9b2f99cd70e934863f36686ba70ca743419786ad6cdbb9c81ec4969719",
  "MN1:unlock": "3974cf18d7002254836577dfd9cf6cc9394d78b3e941fe43a4285341009875b7",
  "MN1:validService": "5d7df8433b3324ac83aa1b3a59fb04fd5af2b4fbb423e967b9e7d603aab12a60",
  "MN2:complete": "3b652059a5ca7978ccfd96117ba3c041b344311b871e536755756244b6b35577",
  "MN2:unlock": "c26d284005a210d48f5e4d6f4f62cd1b33f79346319c5ac2804b3501d1e4d31b",
  "MN2:validService": "5d7df8433b3324ac83aa1b3a59fb04fd5af2b4fbb423e967b9e7d603aab12a60",
  "MN3:complete": "1aea75f56f08e636b1c35484ca48edd22e2618df66b8650cb30fa2bd237f44d0",
  "MN3:unlock": "14c497022456bed6f8e00c8877b719bbab82afd124916acfcd02b982e455e2b4",
  "MN3:validService": "5d7df8433b3324ac83aa1b3a59fb04fd5af2b4fbb423e967b9e7d603aab12a60",
  "MN4:complete": "6b4fa9ca1e371896b425ccc97b125815e1590a6ad6e8138f8cc0e5b43dc647ee",
  "MN4:unlock": "755cf82a270a40b492f48cd4163099c4db9b5c26d1040284b46e908ea59fbf27",
  "MN4:validService": "5d7df8433b3324ac83aa1b3a59fb04fd5af2b4fbb423e967b9e7d603aab12a60",
  "MN5:complete": "b6afd6a7fe787a97d82c8596cba6bb8d28a7b937783dde0bfc51f098385ae772",
  "MN5:unlock": "d3f83268fd3ccc079c885c79de1b193b2a8246ad980a2749ba3fdd6b3cb5f43f",
  "MN5:validService": "5d7df8433b3324ac83aa1b3a59fb04fd5af2b4fbb423e967b9e7d603aab12a60",
  "MN6:complete": "37754ce4c6a2c21a61f8d3e3f6d2f902b84fa9976a595b259c6edcbaee6ac4e3",
  "MN6:unlock": "bb9605026728c4bec8e3f96101144c6fc854b7c075d2289f9679caff600773bf",
  "MN6:validService": "5d7df8433b3324ac83aa1b3a59fb04fd5af2b4fbb423e967b9e7d603aab12a60",
  "MN7:complete": "53d19e4cc79d44651208db1c513d8100b19718970efd6b47a39d4b749c40228c",
  "MN7:unlock": "3a6721c6237a0921a9fa58c89fa36b9045c2bd48ff742c5902a079a4f7c9fa37",
  "MN7:validService": "5d7df8433b3324ac83aa1b3a59fb04fd5af2b4fbb423e967b9e7d603aab12a60",
  "MN8:complete": "e49a646d7367002b3406c0b2fdb3710df3cdf7d489bcadddea7a76b3b31c42d4",
  "MN8:unlock": "11cbf555ff29a899f9f2841a75af3c42d94f6f7a2ebe27020a06efd48f1924e1",
  "MN8:validService": "5d7df8433b3324ac83aa1b3a59fb04fd5af2b4fbb423e967b9e7d603aab12a60",
  "O01:qualification": "b6631dedf580fe957c03596cd58f52ba4dc7c22206040c981c5755567ee78b2a",
  "O01:payment": "1c8776ba8fba4900fa95b26eb2579fd4a71f8e484b63c551f28cce208bf70925",
  "O02:qualification": "b6631dedf580fe957c03596cd58f52ba4dc7c22206040c981c5755567ee78b2a",
  "O02:payment": "1c8776ba8fba4900fa95b26eb2579fd4a71f8e484b63c551f28cce208bf70925",
  "O03:qualification": "b6631dedf580fe957c03596cd58f52ba4dc7c22206040c981c5755567ee78b2a",
  "O03:payment": "1c8776ba8fba4900fa95b26eb2579fd4a71f8e484b63c551f28cce208bf70925",
  "O04:qualification": "b6631dedf580fe957c03596cd58f52ba4dc7c22206040c981c5755567ee78b2a",
  "O04:payment": "75566f562b45ce95e11ff572f24d4aee545136e9e2e9646024a86dd18f6e0a32",
  "O05:qualification": "b6631dedf580fe957c03596cd58f52ba4dc7c22206040c981c5755567ee78b2a",
  "O05:payment": "1c8776ba8fba4900fa95b26eb2579fd4a71f8e484b63c551f28cce208bf70925",
  "O06:qualification": "b6631dedf580fe957c03596cd58f52ba4dc7c22206040c981c5755567ee78b2a",
  "O06:payment": "1c8776ba8fba4900fa95b26eb2579fd4a71f8e484b63c551f28cce208bf70925",
  "O07:qualification": "b6631dedf580fe957c03596cd58f52ba4dc7c22206040c981c5755567ee78b2a",
  "O07:payment": "1c8776ba8fba4900fa95b26eb2579fd4a71f8e484b63c551f28cce208bf70925",
  "O08:qualification": "b6631dedf580fe957c03596cd58f52ba4dc7c22206040c981c5755567ee78b2a",
  "O08:payment": "1c8776ba8fba4900fa95b26eb2579fd4a71f8e484b63c551f28cce208bf70925",
  "O09:qualification": "b6631dedf580fe957c03596cd58f52ba4dc7c22206040c981c5755567ee78b2a",
  "O09:payment": "1c8776ba8fba4900fa95b26eb2579fd4a71f8e484b63c551f28cce208bf70925",
  "O10:qualification": "b6631dedf580fe957c03596cd58f52ba4dc7c22206040c981c5755567ee78b2a",
  "O10:payment": "1c8776ba8fba4900fa95b26eb2579fd4a71f8e484b63c551f28cce208bf70925",
  "O10:seasonRule": "cb801ef874ada306d0daaa230c92a4df2d13449f6797efa31440e71d7d6e4a3a",
  "O11:qualification": "b6631dedf580fe957c03596cd58f52ba4dc7c22206040c981c5755567ee78b2a",
  "O11:payment": "1c8776ba8fba4900fa95b26eb2579fd4a71f8e484b63c551f28cce208bf70925",
  "O12:qualification": "b6631dedf580fe957c03596cd58f52ba4dc7c22206040c981c5755567ee78b2a",
  "O12:payment": "1c8776ba8fba4900fa95b26eb2579fd4a71f8e484b63c551f28cce208bf70925",
  "RG1:delivery": "c4f180cd4bdddddf3e0bedb6e32a6448dd87206a0dad3f6a9d13cb82da2321ab",
  "RG1:completion": "5f18f2383fb55635837832b5f16748c06ca40b55bff8727664830285fc446eff",
  "RG2:delivery": "c4f180cd4bdddddf3e0bedb6e32a6448dd87206a0dad3f6a9d13cb82da2321ab",
  "RG2:completion": "5f18f2383fb55635837832b5f16748c06ca40b55bff8727664830285fc446eff",
  "RG3:delivery": "c4f180cd4bdddddf3e0bedb6e32a6448dd87206a0dad3f6a9d13cb82da2321ab",
  "RG3:completion": "5f18f2383fb55635837832b5f16748c06ca40b55bff8727664830285fc446eff",
  "RG4:delivery": "c4f180cd4bdddddf3e0bedb6e32a6448dd87206a0dad3f6a9d13cb82da2321ab",
  "RG4:completion": "5f18f2383fb55635837832b5f16748c06ca40b55bff8727664830285fc446eff",
  "RG1-1:gate": "9e5098d50b1e65181197e3666d20b9ee291b247009060ebc123d2b0b3f6ec69f",
  "RG1-1:alternatives": "e37c7cffdf3421d8499d2d6b2ecd84858026a59d31cade5501863fe338975c4c",
  "RG1-1:recordPolicy": "517304d39cb0cf10e985417eb79dc52a469ead31399787ff387bdb356e7ad005",
  "RG1-2:gate": "c79890a3637b3cfdf9501f74d8574fcece44edb6ec5951f4e7ead8705842393f",
  "RG1-2:alternatives": "a4fb5df8618eab428fdfb5e19ee1b8b495987ab9b91c4dce6dff3f0c9f7a08eb",
  "RG1-2:recordPolicy": "b851ad37e7cdbeca66f7e32314b0bb65ece501ad313757a5eb10a12ebeb53dd4",
  "RG1-3:gate": "c79890a3637b3cfdf9501f74d8574fcece44edb6ec5951f4e7ead8705842393f",
  "RG1-3:alternatives": "3fe858ac2bab9269db6d0701c57bda777562951f24a08356a5f8ebf12c808f12",
  "RG1-3:recordPolicy": "b851ad37e7cdbeca66f7e32314b0bb65ece501ad313757a5eb10a12ebeb53dd4",
  "RG1-4:gate": "411078ed5c4d7a6aaeb0a4461ad8e386502b4cf1cf5a7f172dd3aa21269c5e12",
  "RG1-4:alternatives": "65c551a673e970cc9d6d59051f9f26a0faa1d9d0f0205f1dcd76e69ae48f9ed9",
  "RG1-4:recordPolicy": "b851ad37e7cdbeca66f7e32314b0bb65ece501ad313757a5eb10a12ebeb53dd4",
  "RG2-1:gate": "52aafc342dc236d937163ccc605b1f9187a61a7ad3bc5e4bb3c1b26238cba2c4",
  "RG2-1:alternatives": "af44a33bdc26c230cbd93d98015357ea54d5283327babe87dac3a7d522ea98f6",
  "RG2-1:recordPolicy": "517304d39cb0cf10e985417eb79dc52a469ead31399787ff387bdb356e7ad005",
  "RG2-2:gate": "84ad860359577dac36b3e903640d3672c9f836f1afd6c745e48bc2574ea8dc4b",
  "RG2-2:alternatives": "814ce2a6414477f207dd0f2658120f60191f28b17307cc1d17057d3ebd38412b",
  "RG2-2:recordPolicy": "b851ad37e7cdbeca66f7e32314b0bb65ece501ad313757a5eb10a12ebeb53dd4",
  "RG2-3:gate": "873f80c496ef5c0021d99fee20ba98099c39ece06bd0c10bde1a0f5036097948",
  "RG2-3:alternatives": "77908f168eeb40a8577fdd3417bcc28303550f585b2263109373123835ee01fc",
  "RG2-3:recordPolicy": "b851ad37e7cdbeca66f7e32314b0bb65ece501ad313757a5eb10a12ebeb53dd4",
  "RG2-4:gate": "c79890a3637b3cfdf9501f74d8574fcece44edb6ec5951f4e7ead8705842393f",
  "RG2-4:alternatives": "356e286d67e7532a09da7de17324ef8b3bbe578214a771ac8a249f758e301f4f",
  "RG2-4:recordPolicy": "b851ad37e7cdbeca66f7e32314b0bb65ece501ad313757a5eb10a12ebeb53dd4",
  "RG3-1:gate": "d3f83268fd3ccc079c885c79de1b193b2a8246ad980a2749ba3fdd6b3cb5f43f",
  "RG3-1:alternatives": "d9bdd754dfbaa246c0038bbceea72b228dc8960c143bac724e9bfe3f2743a366",
  "RG3-1:recordPolicy": "517304d39cb0cf10e985417eb79dc52a469ead31399787ff387bdb356e7ad005",
  "RG3-2:gate": "1b5c48483274586c5a1c7787234327c0acae61715906f6155123b706b1d2f63d",
  "RG3-2:alternatives": "2b18885d6af7b59054cb387cb02138412a39ddcce69027cdf4b27da95271125b",
  "RG3-2:recordPolicy": "b851ad37e7cdbeca66f7e32314b0bb65ece501ad313757a5eb10a12ebeb53dd4",
  "RG3-3:gate": "c79890a3637b3cfdf9501f74d8574fcece44edb6ec5951f4e7ead8705842393f",
  "RG3-3:alternatives": "2a643a8612d0ac6a53950643754d09997aa9d8f13fc0ed89321ebf325e8fc07f",
  "RG3-3:recordPolicy": "b851ad37e7cdbeca66f7e32314b0bb65ece501ad313757a5eb10a12ebeb53dd4",
  "RG3-4:gate": "c79890a3637b3cfdf9501f74d8574fcece44edb6ec5951f4e7ead8705842393f",
  "RG3-4:alternatives": "4810464862e62919a8550dd8d5abc1b7d1b2fc93db3fad68e5b69beeb6833665",
  "RG3-4:recordPolicy": "b851ad37e7cdbeca66f7e32314b0bb65ece501ad313757a5eb10a12ebeb53dd4",
  "RG4-1:gate": "2e45ac1f1a60bd40f98d77aea8d9783e6cf3ca55e6d8c797fc1b6633e426c038",
  "RG4-1:alternatives": "e0218208d7b025cf4ad4d1a514e7e7b91f05fbb9cc5633cc25e8fc13d25d86cd",
  "RG4-1:recordPolicy": "517304d39cb0cf10e985417eb79dc52a469ead31399787ff387bdb356e7ad005",
  "RG4-2:gate": "34416e43b1ef2545e2315fb3344f62d9f614c56e2be3f8d0c679fa77cd5e24da",
  "RG4-2:alternatives": "828c51f5c247a5b137bc086fdb71bacf56456acad5f30e858c439d0d8cae0538",
  "RG4-2:recordPolicy": "b851ad37e7cdbeca66f7e32314b0bb65ece501ad313757a5eb10a12ebeb53dd4",
  "RG4-3:gate": "c79890a3637b3cfdf9501f74d8574fcece44edb6ec5951f4e7ead8705842393f",
  "RG4-3:alternatives": "728a8238a239d1a91298c1e51b86a2adee1047848ff345d042ad8cc054dc87b6",
  "RG4-3:recordPolicy": "b851ad37e7cdbeca66f7e32314b0bb65ece501ad313757a5eb10a12ebeb53dd4",
  "RG4-4:gate": "c79890a3637b3cfdf9501f74d8574fcece44edb6ec5951f4e7ead8705842393f",
  "RG4-4:alternatives": "e45c5af489dfc61d83fe0dde1210e65332bf6f75293142d0727d3e89c1679f08",
  "RG4-4:recordPolicy": "b851ad37e7cdbeca66f7e32314b0bb65ece501ad313757a5eb10a12ebeb53dd4",
  "PJ-1:gate": "5a58c50d42edcc1d421b7b9bfe5d0267c181f5286d75e8874091a3ce0b25c772",
  "PJ-2:gate": "7ac484d214b2ef2075ab69d96b403407ed0022643ff1e412aca1ce562b8b21bb",
  "PJ-3:gate": "d3f83268fd3ccc079c885c79de1b193b2a8246ad980a2749ba3fdd6b3cb5f43f",
  "PJ-4:gate": "e48fbb6e6bac24335bf2b6b23bf86e9bc86d694bc80d17ff3ee85f589c059a84",
  "PJ-4:optionalDisplay.rule": "7df3bb489348c81ea82c3f3b058df468218a18035bebe9e09805d4cffab28a94",
  "PJ-1-A:check": "55e112abc80f7fd7b877f2bb1014544f5126cf389c6a3fa2cb0d86ca6d9b98b4",
  "PJ-1-B:check": "7b516b07fa4e0330b4cd09fe29e1e4445adbd421a628a5cb013b4c81b599e88e",
  "PJ-1-C:check": "1b7e2b31261263a1de4e2a0401bec60ff514465ba786ed449a70c677069f2107",
  "PJ-2-A:check": "0319aeb57a1c360b0accc6986f485bb71fcbfc382db9d4fe9e213bba59136729",
  "PJ-2-B:check": "7f645794eb9522aa99a2760f8d4f1840614c9fcbef3ca6a88cac18f23811319d",
  "PJ-2-C:check": "fc222b45dc6d8dfad399596154d8c73141480724d3276ce2ce8e1cb3eec73c55",
  "PJ-3-A:check": "30e425f9c0cff63ac9c86ce102e9a8abdf5954f7a0b1cb9bd853e1949ec4f852",
  "PJ-3-B:check": "6906f4e18865073813418b1ef2a624332a9e72e1b007c2a1f9b2dc3307b17082",
  "PJ-3-C:check": "1d0d56a4118fd36085a9e82f195b9cb21fd09c13e1dc43f3283ec25fa3ea43e7",
  "PJ-4-A:check": "42f00e1b7d303b0e7d270d7a793a229d401287de47335c68b97afa46ee499560",
  "PJ-4-B:check": "72e0ea9982bc53e2aa8be6e757fb82f2ba544597634bb4fa1e8f5ec75687b848",
  "PJ-4-C:check": "92d56f66dd944ee9866f5f5d59ffc5b8c26e3eef460336406deb1d404ab1ce78",
  "COL-1:stageRules": "45af3aea1cf1933c7105fd074d1f749b4f9cee6305e396f32d9b164fc4c100aa",
  "COL-1:practice.any": "fc05ee4ed761849d9f3ee1cb1f218c556f569d8da0b9275d1cb0ad260e242d32",
  "COL-1:practice.fullMenuStamp": "1ac91d7ca9e712c1029de415366439bbed785020de194639d670f74cbb714f97",
  "COL-2:stageRules": "45af3aea1cf1933c7105fd074d1f749b4f9cee6305e396f32d9b164fc4c100aa",
  "COL-2:practice.any": "5e314e50f5d8b200224b5a0466e88a63566d0047e52f8a667fd76616acb9339f",
  "COL-2:practice.fullMenuStamp": "995219606ac7fbd2b74a97f92dc2ea251ec57551107c0add33d9bfe6ff6f201a",
  "COL-3:stageRules": "45af3aea1cf1933c7105fd074d1f749b4f9cee6305e396f32d9b164fc4c100aa",
  "COL-3:practice.any": "9c5c27af2b05983948c657c5e71e049f42108e93af0196c0344cd032417283a5",
  "COL-3:practice.fullMenuStamp": "040c79d44b5531fe799c4a5083f599fa36b8c6b58d3df20c36c32ca6f8869a4b",
  "COL-4:stageRules": "45af3aea1cf1933c7105fd074d1f749b4f9cee6305e396f32d9b164fc4c100aa",
  "COL-4:practice.any": "0132907191d9d1a876f1289495a1e85662c19d182c980dd70ca294b6bdb8fb26",
  "COL-4:practice.fullMenuStamp": "d776d07dbeb757b6ed21d2ae74a0472d133a8c0dfbd6d13825ec37ff63b96f4b",
  "COL-5:stageRules": "45af3aea1cf1933c7105fd074d1f749b4f9cee6305e396f32d9b164fc4c100aa",
  "COL-5:practice.any": "335cf955930373818601a126a9a4b04f02db74a95a0a77a4553680ffeb46bf4e",
  "COL-5:practice.fullMenuStamp": "a5726aab12fd4009dab26842b04c0b9748b86bbac58abe5a488d3c8d8b657e60",
  "COL-6:stageRules": "45af3aea1cf1933c7105fd074d1f749b4f9cee6305e396f32d9b164fc4c100aa",
  "COL-6:practice.any": "8e77c2e7be066b37069b7590dbb6555089e812a90773168c4220b02f1b7a3a7b",
  "COL-6:practice.fullMenuStamp": "566c4143c79b167706dc4341eb708f9ef6a21fa9ee41229050af903d7f1b2a4d",
  "COL-7:stageRules": "45af3aea1cf1933c7105fd074d1f749b4f9cee6305e396f32d9b164fc4c100aa",
  "COL-7:practice.any": "0e8ae04a4a1e179502d6e35d1fdaea51268b516568092db7f4400748629294d8",
  "COL-7:practice.fullMenuStamp": "8ad57a6459f12374abf1f4a2a44af74dec92a07942712230403611e941b6601b",
  "COL-8:stageRules": "4a925a226dbc85662b59f9d137debf874bafb2cfa956b9f93d59fcb6bb666558",
  "COL-8:practice.any": "905e74f5774aa054ec6d2c0b8a010b653a299fdbfe6adc21d0bb8b92feb6f4d6",
  "COL-8:practice.fullMenuStamp": "88e5958539755c7238288700cd882e02fc3b32cc532d8cef2b3b36e34d3323e2",
  "COL-V:practice.any": "d431d0ecaede5836724668b4f3aa6ff0f399a7bb6296e9d5f63c6c4e8ca5d73d",
  "COL-R:practice.any": "d431d0ecaede5836724668b4f3aa6ff0f399a7bb6296e9d5f63c6c4e8ca5d73d",
  "COL-T:practice.any": "d431d0ecaede5836724668b4f3aa6ff0f399a7bb6296e9d5f63c6c4e8ca5d73d",
  "COL-B:practice.any": "d431d0ecaede5836724668b4f3aa6ff0f399a7bb6296e9d5f63c6c4e8ca5d73d",
  "COL-V-A:requires": "261cef4e61f2dccc032b2ae6ccac79a873df3e18b81e5133946059dfb4e2e24e",
  "COL-V-B:requires": "c0f50d4eded9c568a97ff5f976ca69a04a0113206e9dc75ac513123d52e98a83",
  "COL-V-C:requires": "a4cd67c45369567b5d6918af7e91004339ac11410ebc4d3b6d22d6b177a17f25",
  "COL-R-A:requires": "261cef4e61f2dccc032b2ae6ccac79a873df3e18b81e5133946059dfb4e2e24e",
  "COL-R-B:requires": "c0f50d4eded9c568a97ff5f976ca69a04a0113206e9dc75ac513123d52e98a83",
  "COL-R-C:requires": "a4cd67c45369567b5d6918af7e91004339ac11410ebc4d3b6d22d6b177a17f25",
  "COL-T-A:requires": "261cef4e61f2dccc032b2ae6ccac79a873df3e18b81e5133946059dfb4e2e24e",
  "COL-T-B:requires": "c0f50d4eded9c568a97ff5f976ca69a04a0113206e9dc75ac513123d52e98a83",
  "COL-T-C:requires": "a4cd67c45369567b5d6918af7e91004339ac11410ebc4d3b6d22d6b177a17f25",
  "COL-B-A:requires": "261cef4e61f2dccc032b2ae6ccac79a873df3e18b81e5133946059dfb4e2e24e",
  "COL-B-B:requires": "c0f50d4eded9c568a97ff5f976ca69a04a0113206e9dc75ac513123d52e98a83",
  "COL-B-C:requires": "a4cd67c45369567b5d6918af7e91004339ac11410ebc4d3b6d22d6b177a17f25",
  "SP-ALL:fixed": "4f53cda18c2baa0c0354bb5f9a3ecbe5ed12ab4d8e11ba873c2f11161202b945",
  "SP-ALL:optional": "916787f3799c47a79fd3ffd5babc9f0637f70a3987e807cfbb857328187cf8ff",
  "SP-ALL:practice": "1b8778dd3f7d39f24853691374737dd42e37b7ced1674783140cb433d5ebe9c9",
  "SP-ALL:reward": "27ea8ebdcc1534b9bca6b1171d38bd30ea79b0cbb8872b9583c9040f53739f9b",
  "SP-LEAF:fixed": "fbe6c6680578018687b73bc76bfdfc992b3e094467e7efe654a6e249c8650841",
  "SP-LEAF:optional": "98c0670a92d7864344b8c1ec3f5e311ab3f6f6aa31b37aea1b69d2ebf6ad6307",
  "SP-LEAF:practice": "6e531b7f938307dd688214f0234384014e614d6d9be77900343f6c97d4092367",
  "SP-LEAF:reward": "6137881bb45b034529ea8017424c5cc5d547ba1411654ef63f7c6b2eb0253041",
  "SP-TABLE:fixed": "68eced609174bac0fceba4c69d703e453814f737238a7f367592b8b3b5bfdaff",
  "SP-TABLE:optional": "9052c07ac3e2449a5a49b928534a1fbe72b8de03d81a0ce2d8087ccf4788eb4a",
  "SP-TABLE:practice": "f3d33ad59a3741433566366db7741529cbf4afa65f794f2f8dc03c1de0da2d15",
  "SP-TABLE:reward": "fec47460f299d57023d76f7e59a4a54c3dc8934477fe424296d5ad63618241e2",
  "SP-SHAPE:fixed": "4f53cda18c2baa0c0354bb5f9a3ecbe5ed12ab4d8e11ba873c2f11161202b945",
  "SP-SHAPE:optional": "05c5751702835bdce78a6ef8c28b374ce0db9c34fe18a5b69a366856ce312f83",
  "SP-SHAPE:practice": "d8493a96fa11d5fd7236ef86b2f27f3cac1e4107822ca82f7b7510f0f6f465d3",
  "SP-SHAPE:reward": "1cf814729ba938b195a09af8703cd0fdc6b4db604bf5ba1753b672395cc328cd",
  "M01:effect": "8fd2f88ea475f13b1807ab9105396301f6ea58243d64c3cee07f02db35061fa0",
  "M01:unlock": "d4868020b6cfefd87e85e9155596aca2e56859f76acd67bcf73f6c94333a674e",
  "M02:effect": "8fd2f88ea475f13b1807ab9105396301f6ea58243d64c3cee07f02db35061fa0",
  "M02:unlock": "d4868020b6cfefd87e85e9155596aca2e56859f76acd67bcf73f6c94333a674e",
  "M03:effect": "8fd2f88ea475f13b1807ab9105396301f6ea58243d64c3cee07f02db35061fa0",
  "M03:unlock": "d4868020b6cfefd87e85e9155596aca2e56859f76acd67bcf73f6c94333a674e",
  "M04:effect": "8fd2f88ea475f13b1807ab9105396301f6ea58243d64c3cee07f02db35061fa0",
  "M04:unlock": "d4868020b6cfefd87e85e9155596aca2e56859f76acd67bcf73f6c94333a674e",
  "M05:effect": "8fd2f88ea475f13b1807ab9105396301f6ea58243d64c3cee07f02db35061fa0",
  "M05:unlock": "d4868020b6cfefd87e85e9155596aca2e56859f76acd67bcf73f6c94333a674e",
  "M06:effect": "8fd2f88ea475f13b1807ab9105396301f6ea58243d64c3cee07f02db35061fa0",
  "M06:unlock": "d4868020b6cfefd87e85e9155596aca2e56859f76acd67bcf73f6c94333a674e",
  "M07:effect": "8fd2f88ea475f13b1807ab9105396301f6ea58243d64c3cee07f02db35061fa0",
  "M07:unlock": "d4868020b6cfefd87e85e9155596aca2e56859f76acd67bcf73f6c94333a674e",
  "M08:effect": "8fd2f88ea475f13b1807ab9105396301f6ea58243d64c3cee07f02db35061fa0",
  "M08:unlock": "d4868020b6cfefd87e85e9155596aca2e56859f76acd67bcf73f6c94333a674e",
  "M09:effect": "8fd2f88ea475f13b1807ab9105396301f6ea58243d64c3cee07f02db35061fa0",
  "M09:unlock": "a5cb1c172c5d6b3650e1d6036c08bd3adffab401f2aabae23bb5d085b5fab626",
  "M10:effect": "8fd2f88ea475f13b1807ab9105396301f6ea58243d64c3cee07f02db35061fa0",
  "M10:unlock": "a5cb1c172c5d6b3650e1d6036c08bd3adffab401f2aabae23bb5d085b5fab626",
  "M11:effect": "8fd2f88ea475f13b1807ab9105396301f6ea58243d64c3cee07f02db35061fa0",
  "M11:unlock": "a5cb1c172c5d6b3650e1d6036c08bd3adffab401f2aabae23bb5d085b5fab626",
  "M12:effect": "8fd2f88ea475f13b1807ab9105396301f6ea58243d64c3cee07f02db35061fa0",
  "M12:unlock": "a5cb1c172c5d6b3650e1d6036c08bd3adffab401f2aabae23bb5d085b5fab626",
  "productionRules:knowledge": "82567da3630b760804d55e8f971b365e4097d436bcdc7c9a1b0a1c4ae2ad812d",
  "productionRules:method": "bf3419d9057bf54eae6a9c9f4ad96f84d9ccae91a5e7378e373aa586c7323f5c",
  "productionRules:trial": "b5afd70ced28e0d8bf6d29f8f71d435dc8319488df00da06895184799a3f594f",
  "productionRules:companions": "f04298ee0897bd09fb886e5a1c93fd77bbe448a291862af4eccd998611259999",
  "productionRules:prices": "1a5f6551f3e3dd83383577bc7d2b4d6e4cf8f2deb7b46a336d94389ead984327",
  "productionRules:timing": "3e42249661f6ad8982bf1061a636858865e4b44b3b05259ac7019d12510618ca",
  "productionRules:unlock": "9357e5dbf44f40701d6c8b180a0d8da4743a4caad6b4b24f5b703d2412d66c4b",
  "productionRules:ui": "7c23b6536dffa56956262b65626cfad7676ac37bbd03d65a8b80cd8c71bed653"
};
