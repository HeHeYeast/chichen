// Work C: the region notebook projection never leaks unknown names or recipes.
import test from 'node:test';
import assert from 'node:assert/strict';
import {freshState} from '../web/engine.js';
import {earnedSources} from '../web/progression.js';
import {regionView} from '../web/region-view.js';
import {departRegional} from '../web/regional-exploration.js';
import {identifyMaterial} from '../web/regional-methods.js';
import {advanceWorld} from '../web/world-clock.js';
import {REGIONAL,CONTENT_TEXT,resolveSpecies} from '../web/content-registry.js';

const NOW=1800000000000;
const names=REGIONAL.species.filter(s=>s.region==='V').map(s=>resolveSpecies(s.key).title_zh_CN);
function eligible(){const s=freshState(NOW,7);s.total={'0:0':130,'0:3':1,'0:4':1,'0:8':1,'0:18':1};s.farm={'0:0':4};s.toolLevels[1]=0;s.progress.sources=earnedSources(s);return s;}

test('an untouched valley shows card riddles and codes, never species names, recipes or unfound material names',()=>{
  const view=regionView(eligible(),'V'),json=JSON.stringify(view);
  for(const n of names)assert.ok(!json.includes(n),n);
  for(const id of ['MAT-V-75','MAT-V-76'])assert.ok(!json.includes(`"${CONTENT_TEXT[id].name}"`),id);
  assert.deepEqual(view.methods.map(m=>m.code),['C129','C130','C131','C132','C133','C134','D066','D067','D068','D069','D070','D071']);
  assert.ok(view.methods.every(m=>m.stage==='unknown'&&m.ingredients===null&&m.tool===null&&m.missing.length===0));
  assert.equal(view.cards.length,6);assert.ok(view.cards.every(c=>c.title&&c.hint&&c.result===null));
  assert.ok(view.cards.find(c=>c.id==='V-N2').gateMissing[0].includes('麦粒边的一点白'),'unfound malt is named by its card riddle');
});

test('specimen, direction, full method and collection reveal progressively',()=>{
  const s=eligible();departRegional(s,{regionId:'V',placeId:'V:0',focus:'specimen',members:['0:0']},NOW);advanceWorld(s,s.progress.trip.endAt);
  let view=regionView(s,'V');const m75=view.materials.find(m=>m.id===75);
  assert.equal(m75.found,true);assert.equal(m75.name,'荠菜');assert.equal(m75.identified,false);
  identifyMaterial(s,75);view=regionView(s,'V');
  const c1=view.methods.find(m=>m.recipeId==='REC-V-C1');
  assert.equal(c1.stage,'direction');assert.equal(c1.firstIngredient,'荠菜');assert.equal(c1.ingredients,null);assert.equal(c1.name,null);assert.ok(c1.pinned);
  s.expansion.methods.full.push('REC-V-C1');view=regionView(s,'V');
  const full=view.methods.find(m=>m.recipeId==='REC-V-C1');assert.equal(full.stage,'full');assert.deepEqual(full.ingredients,['荠菜']);assert.equal(full.name,null);
  assert.ok(!JSON.stringify(view).includes('荠菜煎饼鸡'),'full method still hides the real name');
  s.total['0:128']=1;view=regionView(s,'V');assert.equal(view.methods.find(m=>m.recipeId==='REC-V-C1').name,'荠菜煎饼鸡');
  assert.equal(view.counts.collected,1);
});
