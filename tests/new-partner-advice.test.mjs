import test from 'node:test';
import assert from 'node:assert/strict';
import {freshState} from '../web/engine.js';
import {syncProgress,learnSkill} from '../web/progression.js';
import {studyRecipe,trackPartner} from '../web/knowledge.js';
import {clueBookModel} from '../web/clue-book.js';
import {newPartnerAdvice} from '../web/new-partner-advice.js';
import {INGREDIENT_FLAVOR_GROUPS,ingredientFlavor} from '../web/ingredient-flavors.js';

const NOW=1800000000000;
function player(){
  const s=freshState(NOW);s.kitchenLevel=3;s.cp=100000;s.toolLevels.fill(2);
  for(let id=0;id<89;id++){s.total[`0:${id}`]=100;s.farm[`0:${id}`]=3;}
  syncProgress(s);for(const id of ['OBS-1','OBS-3','OBS-4'])learnSkill(s,id);
  for(let id=12;id<89;id++){delete s.total[`0:${id}`];delete s.farm[`0:${id}`];}
  for(const r of clueBookModel(s,0,NOW).clues.filter(r=>!r.regional).slice(0,8))studyRecipe(s,r.key,NOW);
  return s;
}
test('new partners follow the clue book, are unique, bounded and do not mutate the save',()=>{
  const s=player(),before=structuredClone(s),book=clueBookModel(s,0,NOW),m=newPartnerAdvice(s,0,NOW);
  assert.equal(m.rows.length,5);assert.ok(m.more>0);assert.equal(new Set(m.rows.map(r=>r.key)).size,5);
  const indexes=m.rows.map(r=>book.rows.findIndex(x=>x.key===r.key));assert.deepEqual(indexes,[...indexes].sort((a,b)=>a-b));
  assert.deepEqual(s,before);assert.ok(m.rows.some(r=>r.action==='recipe'));
});
test('a discovered target disappears; a tracked undiscovered target keeps clue-book priority',()=>{
  const s=player(),last=newPartnerAdvice(s,0,NOW,100).rows.at(-1);trackPartner(s,last.key);
  assert.equal(newPartnerAdvice(s,0,NOW).rows[0].key,last.key);
  s.total[last.key]=1;assert.ok(!newPartnerAdvice(s,0,NOW,100).rows.some(r=>r.key===last.key));
});
test('unaffordable targets explain the exact shortfall instead of pretending nothing is known',()=>{
  const s=player(),ready=newPartnerAdvice(s,0,NOW,100).rows.filter(r=>r.action==='recipe'&&r.cash>0);assert.ok(ready.length);
  s.cp=0;const poor=newPartnerAdvice(s,0,NOW,100);
  for(const r of ready){const row=poor.rows.find(x=>x.key===r.key);assert.equal(row.action,'funds');assert.match(row.note,/还差 \d+ CP/);assert.ok(row.cash>0);}
});
test('regional recipes join the same list and an uncollected pot does not reveal its draw',()=>{
  const s=player();s.expansion.regions.opened=['V'];s.expansion.regions.introSpecimenDone=['V'];
  s.expansion.discovery.identified['75']=true;s.expansion.discovery.cards['V-S1']=true;
  s.expansion.methods.directions.push('REC-V-C1');s.expansion.methods.full.push('REC-V-C1');s.ingredients[75]=2;
  trackPartner(s,'0:128');let row=newPartnerAdvice(s,0,NOW).rows[0];assert.equal(row.key,'0:128');assert.equal(row.action,'recipe');
  s.batch={plan:{mode:'regional-trial',recipeId:'REC-V-C1',finished:false,targetScheduled:false},eggs:[{collected:false}]};
  row=newPartnerAdvice(s,0,NOW).rows[0];assert.equal(row.action,'collect');assert.equal(row.note,'这一锅还没收完');
  s.batch.plan.targetScheduled=true;assert.deepEqual(newPartnerAdvice(s,0,NOW).rows[0],row);
});
test('flavor names classify every seasoning once, including the eight regional materials',()=>{
  const ids=Object.values(INGREDIENT_FLAVOR_GROUPS).flat();assert.equal(ids.length,83);assert.equal(new Set(ids).size,83);
  for(let id=0;id<83;id++)assert.ok(ingredientFlavor(id),String(id));
  assert.equal(ingredientFlavor(33),'甜味');assert.equal(ingredientFlavor(75),'蔬菜');assert.equal(ingredientFlavor(76),'谷豆与面食');assert.equal(ingredientFlavor(77),'果物');
});
