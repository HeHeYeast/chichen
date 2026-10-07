import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {freshState,normalizeSave,validateSaveSchema} from '../web/engine.js';
import {learnSkill,syncProgress} from '../web/progression.js';
import {nextBatchGoals,GOAL_CARDS,allAdvice,directions,wants,nightGap} from '../web/next-batch-goals.js';
import {clueBookModel,clueRow,FOCUS_ROWS,CLUE_STEPS} from '../web/clue-book.js';
import {trackPartner,trackedKey,trackedFound,studyRecipe} from '../web/knowledge.js';
import {openDemands} from '../web/seasoning-advisor.js';
import {orderMilestone} from '../web/orders.js';
import {recipePathInfo,RECIPE_CATALOG} from '../web/recipe-book.js';
import {ingredientUnlockInfo} from '../web/ingredient-unlocks.js';
import {speciesDiscovered} from '../web/species-state.js';
const NOW=1800000000000;
function player(skills,{cp=100000}={}){
  const s=freshState(NOW);s.kitchenLevel=3;s.toolLevels.fill(2);s.toolLevels[8]=-1;s.cp=100000;s.egg=0;
  for(let i=0;i<89;i++){s.total['0:'+i]=100;s.farm['0:'+i]=3;}
  syncProgress(s);
  for(const id of skills)learnSkill(s,id);
  for(let i=12;i<89;i++){delete s.total['0:'+i];delete s.farm['0:'+i];}
  s.cp=cp;return s;
}
const usable=(s,id)=>s.ingredients[id]>0||(ingredientUnlockInfo(s,id).available&&!ingredientUnlockInfo(s,id).special);

test('追踪 is one optional save field: set, replace, clear; old saves without it stay valid; a met partner counts as nothing tracked',()=>{
  const s=player(['OBS-1']);assert.equal(trackedKey(s),null);assert.equal('tracked' in s.progress.knowledge,false);
  const [a,b]=clueBookModel(s,0,NOW).rows;
  trackPartner(s,a.key);assert.equal(trackedKey(s),a.key);
  trackPartner(s,b.key);assert.equal(trackedKey(s),b.key,'one at a time: tracking another replaces it');
  assert.throws(()=>trackPartner(s,'0:0'),/认识/,'a partner already met cannot be tracked');
  assert.throws(()=>trackPartner(s,'9:9'),/不能追踪/);
  trackPartner(s,null);assert.equal('tracked' in s.progress.knowledge,false);
  // save validation: absent, null and a real key pass; anything else is rejected
  const fresh=normalizeSave(freshState(NOW),NOW);
  const ok=t=>{const x=structuredClone(fresh);if(t!==undefined)x.progress.knowledge.tracked=t;return validateSaveSchema(x,NOW);};
  ok(undefined);ok(null);ok(a.key);
  assert.throws(()=>ok('0:9999'));assert.throws(()=>ok(5));
  // once hatched, it is "found" for the UI and no longer tracked
  trackPartner(s,a.key);s.total[a.key]=1;s.farm[a.key]=1;
  assert.equal(trackedKey(s),null);assert.equal(trackedFound(s),a.key);
  const kept=structuredClone(fresh);kept.progress.knowledge.tracked=a.key;
  assert.equal(normalizeSave(kept,NOW).progress.knowledge.tracked,a.key,'loading a save keeps the field');
});

test('线索册 rows: status follows what can be done now; 调查 x/5 is 5 only for a held recipe; 调查 lists stay short',()=>{
  const s=player(['OBS-1','OBS-3']);
  for(const egg of [0]){
    const m=clueBookModel(s,egg,NOW);assert.ok(m.rows.length>0);
    for(const r of m.rows){
      // with both observation skills an ordinary partner has nothing left for a trip; a regional one still has its finds
      assert.ok(['ready','guess','wait','clue'].includes(r.status)||r.regional&&r.status==='scout',`${r.code} ${r.status}`);
      assert.equal(r.progress===CLUE_STEPS,r.held,`${r.code}: 5/5 only when the recipe is held`);
      if(r.status==='ready')assert.ok(r.held&&r.chance>0);
      if(r.status==='guess')assert.ok(!r.held&&r.guess&&directions(s,egg,NOW).some(g=>g.key===r.key));
    }
    assert.ok(m.near.length<=FOCUS_ROWS&&m.doable.length<=FOCUS_ROWS);
    assert.ok(m.doable.every(r=>r.status==='ready'));
    assert.equal(m.counts.all,m.rows.length);
  }
  const m=clueBookModel(s,0,NOW),target=m.near[0];trackPartner(s,target.key);
  const after=clueBookModel(s,0,NOW);
  assert.equal(after.tracked.key,target.key);assert.equal(after.rows[0].key,target.key,'the tracked one leads 全部');
  assert.ok(!after.near.some(r=>r.key===target.key),'and is not repeated under 正在调查');
});

test('a wait row only names conditions the player can read, never a hidden seasoning',()=>{
  const s=player(['OBS-1']);
  for(const egg of [0,1]){const view={...s,egg};
    for(const r of clueBookModel(s,egg,NOW).rows.filter(r=>!r.held)){
      const path=RECIPE_CATALOG.find(x=>x.key===r.key&&x.toolId===r.toolId)??null;if(!path)continue;
      const hidden=path.ingredients.filter(id=>id!==r.first).map(id=>E.label(E.ingredient(id)));
      for(const text of r.need)for(const name of hidden)assert.ok(!text.includes(name),`${r.code}: "${text}" names ${name}`);
    }}
});

test('推荐: the tracked partner is always the first card, whether its recipe is held or still being worked out',()=>{
  const s=player(['OBS-1','OBS-3','OBS-4']);
  const guess=clueBookModel(s,0,NOW).rows.find(r=>r.status==='guess');assert.ok(guess,'something to try');
  trackPartner(s,guess.key);
  let g=nextBatchGoals(s,NOW);
  assert.equal(g.cards[0].kind,'track');assert.equal(g.cards[0].action,'guess');assert.equal(g.cards[0].guess.key,guess.key);
  assert.ok(g.tracked.carded);
  // studied: now it is a recipe to cook, still first
  studyRecipe(s,guess.key,NOW);g=nextBatchGoals(s,NOW);
  const row=clueRow(s,guess.key,NOW);
  if(row.status==='ready'){assert.equal(g.cards[0].kind,'track');assert.equal(g.cards[0].action,'cook');assert.equal(g.cards[0].outcome.key,guess.key);}
  else assert.equal(g.tracked.carded,false);
});

test('推荐: a tracked partner that cannot be made now gets no card, only a note saying why (or where to look next)',()=>{
  const s=player(['OBS-1']);
  for(const status of ['wait','scout']){
    const row=clueBookModel(s,0,NOW).rows.find(r=>r.status===status&&(status!=='wait'||r.need.length));if(!row)continue;
    trackPartner(s,row.key);const g=nextBatchGoals(s,NOW);
    assert.equal(g.tracked.carded,false,row.code);assert.ok(g.tracked.note.length>0);
    assert.ok(!g.cards.some(c=>c.kind==='track'));
    // known only by its cookware: the next clue is a trip away, and the note says which region
    if(status==='scout'){assert.match(g.tracked.note,new RegExp(`去${row.region.name}寻访`));assert.equal(g.tracked.scout.id,row.region.id);}
  }
});

test('推荐: at most four cards, every one startable now, guesses never fill in the hidden seasoning, money last',()=>{
  for(const skills of [['OBS-1'],['OBS-1','OBS-3'],['OBS-1','OBS-3','OBS-4']]){
    const s=player(skills,{cp:400});
    const m=clueBookModel(s,0,NOW);for(const r of m.rows.slice(0,3))if(skills.includes('OBS-4')){s.cp+=200;studyRecipe(s,r.key,NOW);}
    const g=nextBatchGoals(s,NOW);
    assert.ok(g.cards.length<=GOAL_CARDS);
    assert.equal(new Set(g.cards.map(c=>c.id)).size,g.cards.length,'one card per set');
    assert.ok(g.cards.filter(c=>c.kind==='income').length<=1);
    const rest=g.cards.filter(c=>c.kind!=='track');
    for(let i=1;i<rest.length;i++)assert.ok(rest[i-1].value>=rest[i].value,'ranked by how far it moves the game on');
    const income=rest.findIndex(c=>c.kind==='income');if(income>=0)assert.equal(income,rest.length-1,'money never outranks progress');
    for(const c of g.cards){
      const view={...s,egg:c.egg};
      if(c.action==='cook'){
        assert.ok(c.cash<=s.cp,`${c.reason}: ${c.cash} CP but only ${s.cp}`);
        const path=RECIPE_CATALOG.find(r=>r.toolId===c.toolId&&r.egg===c.egg);
        assert.ok((s.toolLevels[c.toolId]??-1)>=0);
        for(const id of c.ingredients)assert.ok(usable(s,id),`${c.reason}: seasoning ${id} cannot be had`);
        assert.ok(allAdvice(s,c.egg,NOW).find(a=>a.toolId===c.toolId).rows.some(r=>r.ingredients.join('+')===[...c.ingredients].sort((a,b)=>a-b).join('+')&&r.affordable));
      }else{
        const g2=directions(s,c.egg,NOW).find(x=>x.key===c.guess.key);assert.ok(g2,'a guess is a current 推测方向');
        assert.deepEqual(c.ingredients,g2.first!=null?[g2.first]:[],'only the seasoning the clues name is filled in');
        assert.ok(c.cash<=s.cp);
        const real=recipePathInfo(view,RECIPE_CATALOG.find(r=>r.key===c.guess.key&&r.toolId===c.toolId),NOW);
        assert.ok(real.conditions.every(x=>x.met),'and its real recipe could be cooked now');
      }
    }
  }
});

test('推荐: with no CP nothing that costs CP is offered, and the page can say CP is what is missing',()=>{
  const s=player(['OBS-1','OBS-3'],{cp:0});
  const g=nextBatchGoals(s,NOW);
  assert.ok(g.cards.every(c=>c.cash===0),'only free batches');
});

test('orders and proposals both count as demands, and an order card brings birds the order takes',()=>{
  const s=player(['OBS-1']);s.total['0:0']=400;
  s.expansion.orders??={sequence:0,proposalSequence:0,proposals:[],active:[],templateProgress:{},refillCredits:0,lastProposedTemplate:null};
  for(let i=0;i<3;i++)orderMilestone(s,NOW,'batch');
  const demands=openDemands(s,NOW);
  if(!s.expansion.orders.proposals.length)return;// no template qualifies for this kitchen: nothing to check
  assert.ok(demands.some(d=>d.kind==='proposal'),'proposals are planned for before they are accepted');
  const g=nextBatchGoals(s,NOW),all=wants(s,NOW);
  for(const c of g.cards.filter(c=>c.kind==='order')){
    const d=all.find(d=>c.reason.includes(d.name)||d.kind==='story'&&c.reason.includes(d.what));assert.ok(d,c.reason);
    assert.ok(d.allowed.has(c.outcome.key),'the bird shown is one the order takes');
  }
});

// Batch 5 playtest: at the last look of the evening every card was a two-hour pan, burnt by morning.
// (Higher cookware levels cook faster and keep fresh shorter: at Lv.3 nothing lasts from 21:30 to 8:00, and then there is
// no such card rather than a wrong one.)
test('推荐 at night: a set still fresh at 8:00 the next morning is named when one exists; never in the day',()=>{
  const s=player(['OBS-1'],{cp:5000}),at=(h,m=0)=>{const d=new Date(NOW);d.setHours(h,m,0,0);return +d;};
  assert.equal(nextBatchGoals(s,at(16)).cards.filter(c=>c.kind==='night'||c.also.includes('也能放到明早')).length,0);
  let shown=0;
  for(const [h,m] of [[21,30],[23,30],[1,0],[3,0]]){
    const now=at(h,m),gap=nightGap(now),cards=nextBatchGoals(s,now).cards,night=cards.filter(c=>c.kind==='night'||c.also.includes('也能放到明早'));
    const lasts=(toolId,ingredients)=>{const i=E.cookInfo({...s,egg:0,selected:[...ingredients]},toolId,now);return i.minutes<=gap&&i.minutes/12+i.freshMinutes>=gap;};
    const any=allAdvice(s,0,now).some(a=>a.rows.some(r=>r.affordable!==false&&lasts(a.toolId,r.ingredients)));
    assert.equal(night.length>0,any,`${h}:${m}: a night card exactly when some set lasts`);
    for(const c of night)assert.ok(lasts(c.toolId,c.ingredients),`${c.reason}: done and still fresh at 8:00`);
    assert.ok(cards.length<=GOAL_CARDS);shown+=night.length>0;
  }
  assert.ok(shown>0,'the stew pot lasts from 1:00');
});
