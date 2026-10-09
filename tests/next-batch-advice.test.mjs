import test from 'node:test';
import assert from 'node:assert/strict';
import {freshState} from '../web/engine.js';
import {seasoningAdvice,rankAdvice,undiscoveredReach,NEW_MIN} from '../web/seasoning-advisor.js';
import {syncProgress} from '../web/progression.js';
const NOW=1800000000000;

test('「新伙伴」 counts kinds; every real chance is listed, 线索册 targets first, plain combinations never',()=>{
  const row=(key,kind,newKinds,profit=1,targets=[])=>({key,kind,newKinds,profit,targets});
  const advice={rows:[row('a','known',.2,90),row('b','known',NEW_MIN,10),row('c','known',3.4,5),row('d','known',0,500),row('e','combo',9,999),row('f','known',.25,2,[{key:'0:30',chance:.25}])]};
  // a set made for a 线索册 partner leads; a 20% chance is listed (lower down); nothing at all is not; combinations never
  assert.deepEqual(rankAdvice(advice,'new').map(r=>r.key),['f','c','a','b']);
  assert.deepEqual(rankAdvice(advice,'income').map(r=>r.key),['e','d','a','b','c','f']);
});

test('every advised set carries its kitchen time and whether it can be paid for now',()=>{
  const s=freshState(NOW);s.cp=0;s.kitchenLevel=3;s.toolLevels.fill(1);s.ingredients={...s.ingredients,8:3};syncProgress(s);
  let seen=0;
  for(const [id,level] of s.toolLevels.entries()){
    if(level<0)continue;const a=seasoningAdvice(s,id,NOW);
    assert.ok(a.minutes>0);
    for(const r of a.rows){seen++;assert.equal(r.minutes,a.minutes);assert.equal(typeof r.affordable,'boolean');assert.ok(r.cash>=0);}
  }
  assert.ok(seen>0);
  // with no CP at all, a set that costs anything cannot be afforded
  assert.ok(Object.values(s.toolLevels).length&&[...s.toolLevels.keys()].some(id=>s.toolLevels[id]>=0&&seasoningAdvice(s,id,NOW).rows.some(r=>!r.affordable&&r.cash>0)));
});

test('the count of undiscovered partners reachable with a cookware is a plain number, and 0 for a cookware you do not own',()=>{
  const s=freshState(NOW);s.kitchenLevel=3;s.toolLevels.fill(1);s.cp=100000;syncProgress(s);
  const before=[...s.toolLevels.keys()].map(id=>undiscoveredReach(s,id,NOW));
  assert.ok(before.every(n=>Number.isInteger(n)&&n>=0));assert.ok(before.some(n=>n>0));
  const id=before.findIndex(n=>n>0);
  assert.equal(undiscoveredReach({...s,toolLevels:s.toolLevels.map(()=>-1)},id,NOW),0,'a cookware you do not own reaches nothing');
});

import {learnSkill,rank} from '../web/progression.js';
import {RULES} from '../web/integration-data.js';
import {speciesDiscovered} from '../web/species-state.js';
import {RECIPE_CATALOG} from '../web/recipe-book.js';
import {clueReach,studyRecipe} from '../web/knowledge.js';
import {startBatch} from '../web/engine.js';

function observer(){
  const s=freshState(NOW);s.kitchenLevel=3;s.toolLevels.fill(2);s.toolLevels[8]=-1;s.cp=100000;s.egg=0;
  for(let i=0;i<89;i++){s.total['0:'+i]=100;s.farm['0:'+i]=3;}
  syncProgress(s);s.progress.skillPoints??=0;
  for(const id of ['OBS-1','OBS-2','OBS-3']){try{learnSkill(s,id);}catch{/* prerequisites already met or points short: the test below checks what it needs */}}
  return s;
}
test('every 新伙伴 row can really bring a partner not met yet: real batches agree with the forecast',()=>{
  const s=observer();for(let i=12;i<89;i++){delete s.total['0:'+i];delete s.farm['0:'+i];}
  let checked=0;
  for(const [id,l] of s.toolLevels.entries()){if(l<0)continue;
    for(const r of rankAdvice(seasoningAdvice(s,id,NOW),'new').slice(0,2)){
      checked++;const n=Math.max(60,Math.ceil(10/Math.min(1,r.newKinds)));let hits=0;
      for(let i=0;i<n;i++){const t=structuredClone(s);t.batch=null;t.dirty=false;for(const x of r.ingredients)t.ingredients[x]=1;t.selected=[...r.ingredients];
        if(startBatch(t,id,NOW,Math.random,Math.random).eggs.some(e=>!speciesDiscovered(s,0,e.id)))hits++;}
      assert.ok(hits>0,`${id} ${r.key}: forecast ${r.newKinds.toFixed(2)} but never in ${n} batches`);
      // newKinds adds per-kind chances, so it is at least the chance of any one new kind
      assert.ok(hits/n<=Math.min(1,r.newKinds)+.2,`${id} ${r.key}: forecast ${r.newKinds.toFixed(2)}, batches ${(hits/n).toFixed(2)}`);
    }}
  assert.ok(checked>=4);
});
test('a held 四时 recipe gets its independent per-egg chance (it used to count as nothing)',()=>{
  const s=observer();learnSkill(s,'OBS-4');const key='0:121';assert.equal(speciesDiscovered(s,0,121),false);
  studyRecipe(s,key,NOW);
  const r=RECIPE_CATALOG.find(x=>x.key===key&&x.kind==='seasonal'),row=seasoningAdvice(s,r.toolId,NOW).rows.find(x=>x.key===[...r.ingredients].sort((a,b)=>a-b).join('+'));
  assert.ok(row,'the studied set is advised');const t=row.targets.find(x=>x.key===key);assert.ok(t,'and names its partner');
  assert.ok(Math.abs(t.chance-(1-.8**24))<1e-9);assert.ok(rankAdvice({rows:[row]},'new').length===1);
});
test('combinations built from known recipes are offered for income but never for 新伙伴, and show only known birds',()=>{
  const s=observer();let combos=0;
  for(const [id,l] of s.toolLevels.entries())if(l>=0){const a=seasoningAdvice(s,id,NOW);
    for(const r of a.rows)if(r.kind==='combo'){combos++;assert.ok(r.birds.every(b=>b.known));assert.equal(r.newKinds,0);}
    assert.ok(rankAdvice(a,'new').every(r=>r.kind!=='combo'));}
  assert.ok(combos>0,'several known seasonings should combine');
});

import {holidayLimited,reachableNow} from '../web/seasoning-advisor.js';
test('holiday-only partners are announced only while the window is open, the shrine letter is done, and they are still unmet',()=>{
  const when=Date.parse('2027-03-25T03:00:00Z'),s=freshState(when);s.kitchenLevel=3;s.toolLevels.fill(2);
  assert.deepEqual(holidayLimited(s,0,when),[],'the shrine letter is not done: nothing to announce');
  assert.equal(reachableNow(s,0,48,when),false,'and the partner does not count as something new to find');
  s.events.campaign_char_0_48=true;
  const now=holidayLimited(s,0,when);assert.equal(now.length,1);assert.equal(now[0].title,'樱花季');assert.equal(now[0].count,1);
  assert.equal(reachableNow(s,0,48,when),true);
  assert.deepEqual(holidayLimited(s,0,Date.parse('2027-07-01T03:00:00Z')).map(h=>h.title).includes('樱花季'),false,'out of season');
  s.total['0:48']=1;assert.deepEqual(holidayLimited(s,0,when),[],'already met: nothing left to find');
});

import {guessDirections} from '../web/seasoning-advisor.js';
import {knownRecipe} from '../web/knowledge.js';
import {recipePathInfo} from '../web/recipe-book.js';
import {ingredientUnlockInfo} from '../web/ingredient-unlocks.js';
test('推测方向 only offer what the clues say about partners that could be cooked right now, and never the hidden seasonings',()=>{
  const s=observer();for(let i=12;i<89;i++){delete s.total['0:'+i];delete s.farm['0:'+i];}
  const list=guessDirections(s,0,NOW);assert.ok(list.length>0,'with the notes skills some partners can be guessed');
  const usable=id=>(s.ingredients[id]??0)>0||(()=>{const u=ingredientUnlockInfo(s,id);return u.available&&!u.special;})();
  for(const g of list){
    assert.equal(speciesDiscovered(s,0,g.id),false);assert.equal(knownRecipe(s,g.key,NOW),null,'a held recipe is a recommendation, not a guess');
    const c=clueReach(s,g.key,NOW);assert.ok(c.tool);assert.equal(g.toolId,c.path.toolId);
    // can be cooked now: every condition met and every real seasoning usable
    assert.ok(recipePathInfo(s,c.path,NOW).conditions.every(x=>x.met));assert.ok(c.path.ingredients.every(usable));
    // only what was read: the first seasoning when known, the group when known
    assert.equal(g.first,c.first&&c.path.ingredients.length?c.path.ingredients[0]:null);
    assert.equal(g.group,c.group);
    if(g.candidates){
      const group=RULES.ingredientFlavorGroups[g.group].filter(id=>id!==g.first&&usable(id));
      assert.deepEqual(g.candidates,group,'every usable member of the group, the right one not singled out');
      assert.ok(g.candidates.includes(c.path.ingredients[1]));
    }
  }
});
test('a set the player cannot pay for right now is not advised',()=>{
  const rows=[{key:'a',kind:'known',newKinds:.5,profit:9,targets:[],affordable:false,taskScore:1},{key:'b',kind:'known',newKinds:.3,profit:1,targets:[],affordable:true,taskScore:1}];
  for(const lens of ['new','income','tasks'])assert.deepEqual(rankAdvice({rows},lens).map(r=>r.key),['b']);
});
