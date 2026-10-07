import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {freshState} from '../web/engine.js';
import {learnSkill,syncProgress,rank} from '../web/progression.js';
import {clueBookModel,CLUE_LAYERS} from '../web/clue-book.js';
import {studyRecipe,knownRecipe} from '../web/knowledge.js';
import {speciesDiscovered} from '../web/species-state.js';
const NOW=1800000000000;
function player(skills){
  const s=freshState(NOW);s.kitchenLevel=3;s.toolLevels.fill(2);s.cp=100000;
  for(let i=0;i<89;i++){s.total['0:'+i]=100;s.farm['0:'+i]=3;}
  syncProgress(s);
  for(const id of skills)learnSkill(s,id);
  // forget most species so there is a lot left to read about
  for(let i=12;i<89;i++){delete s.total['0:'+i];delete s.farm['0:'+i];}
  return s;
}

test('the 线索册 splits every undiscovered partner into 已解锁 and 待推断, never a discovered one',()=>{
  const s=player([]);
  const m=clueBookModel(s,0,NOW),keys=[...m.unlocked,...m.clues].map(r=>r.key);
  assert.ok(m.total>0);assert.equal(keys.length,m.total);assert.equal(new Set(keys).size,keys.length,'each partner once');
  for(let i=0;i<12;i++)assert.equal(keys.includes('0:'+i),false,'0:'+i+' is discovered');
  assert.equal(m.unlocked.length,0,'without clues or 研读 nothing is unlocked');
  // a regional partner's riddle names its place and material: before its 方向 the card shows its region's next step instead
  assert.ok(m.clues.every(r=>typeof r.riddle==='string'&&(r.riddle.length>0||r.regional&&!!r.gate)),'every clue row carries its riddle');
  assert.ok(m.clues.every(r=>r.levels.length===5&&r.depth===r.levels.filter(Boolean).length));
  assert.ok(m.clues.every(r=>r.first==null&&r.toolId==null),'without skills or clue facts no cookware or seasoning is named');
  assert.ok(clueBookModel(s,1,NOW).clues.every(r=>r.egg===1),'ducks are their own list');
  assert.equal(CLUE_LAYERS.length,5);
});

test('研读 keeps its skill gate (100 CP, 50 with the specialist) and needs no clue; a studied partner moves to 已解锁',()=>{
  const plain=player([]);assert.equal(clueBookModel(plain,0,NOW).canStudy,false);
  assert.throws(()=>studyRecipe(plain,clueBookModel(plain,0,NOW).clues[0].key,NOW),/配方研读/);
  const s=player(['OBS-1','OBS-3','OBS-4']);assert.ok(rank(s,'OBS-4'));
  const m=clueBookModel(s,0,NOW);assert.equal(m.canStudy,true);assert.equal(m.studyCost,100);
  assert.ok(m.clues.filter(r=>!r.regional).every(r=>r.canStudy&&r.studyCost===100));
  // a regional partner's 研读 stays the region's own: it waits for the 方向, and the card hides it until then
  assert.ok(m.clues.filter(r=>r.regional&&r.gate).every(r=>!r.canStudy&&r.studyHidden));
  // a partner nothing is known about can still be studied
  const plainRows=m.clues.filter(r=>!r.regional),target=plainRows.find(r=>r.depth===Math.min(...plainRows.map(x=>x.depth)))??plainRows[0];
  const before=s.cp;studyRecipe(s,target.key,NOW);assert.equal(s.cp,before-100);
  const after=clueBookModel(s,0,NOW);
  assert.equal(after.clues.some(r=>r.key===target.key),false);
  const row=after.unlocked.find(r=>r.key===target.key);assert.ok(row,'it is listed as a recipe now');
  assert.ok(Number.isInteger(row.toolId)&&Array.isArray(row.ingredients));
});

test('clues that leave nothing open unlock a recipe; two-seasoning recipes stay to be worked out',()=>{
  const s=player(['OBS-1','OBS-3']);
  const m=clueBookModel(s,0,NOW);
  assert.ok(m.unlocked.length>0,'with the first-seasoning skill some one-seasoning recipes are complete');
  assert.ok(m.unlocked.every(r=>r.ingredients.length<=1&&!r.studied));
  assert.ok(m.unlocked.every(r=>knownRecipe(s,r.key,NOW)),'the same rule 下一锅 uses');
  const two=m.clues.filter(r=>r.first!=null&&r.second);assert.ok(two.length>0,'some name a first seasoning and the second one\'s group');
  assert.ok(two.every(r=>r.secondCount>1));
});

test('every 已解锁 recipe marked ready can really hatch its partner (real batches, not just the forecast)',()=>{
  const s=player(['OBS-1','OBS-3','OBS-4']);
  const m=clueBookModel(s,0,NOW);for(const r of m.clues.slice(0,8))studyRecipe(s,r.key,NOW);
  const ready=clueBookModel(s,0,NOW).unlocked.filter(r=>r.ready);
  assert.ok(ready.length>=3);
  for(const r of ready.slice(0,6)){
    assert.ok(r.chance>0&&r.chance<=1);assert.deepEqual(r.blockers,[]);
    let hits=0;const n=Math.max(60,Math.ceil(12/r.chance));
    for(let i=0;i<n;i++){
      const t=structuredClone(s);t.egg=r.egg;t.batch=null;t.dirty=false;for(const id of r.ingredients)t.ingredients[id]=1;t.selected=[...r.ingredients];
      const b=E.startBatch(t,r.toolId,NOW,Math.random,Math.random);if(b.eggs.some(e=>e.id===r.id))hits++;
    }
    assert.ok(hits>0,`${r.code} never hatched in ${n} batches`);
    assert.ok(Math.abs(hits/n-r.chance)<.2,`${r.code}: forecast ${r.chance.toFixed(2)}, batches ${(hits/n).toFixed(2)}`);
  }
});

test('a held recipe that cannot be cooked yet says why and is not ready',()=>{
  const s=player(['OBS-1','OBS-3','OBS-4']);s.kitchenLevel=0;s.toolLevels=s.toolLevels.map((l,i)=>i===0?0:-1);
  for(const r of clueBookModel(s,0,NOW).clues.filter(r=>!r.regional).slice(0,30))studyRecipe(s,r.key,NOW);
  const blocked=clueBookModel(s,0,NOW).unlocked.filter(r=>!r.ready);
  assert.ok(blocked.length>0);assert.ok(blocked.every(r=>r.blockers.length>0&&r.chance===0));
  assert.ok(!speciesDiscovered(s,0,blocked[0].id));
});
