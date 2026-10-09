import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {claimActivity} from '../web/legacy-activities.js';
import {makeBackup,parseBackup} from '../web/save-store.js';
import {FORTUNE_IDS,SHRINE_GOALS,shrineBook,shrineGoals,claimShrineGoal,giftRecipe,prepareGiftRecipe} from '../web/shrine.js';

const NOW=new Date(2026,8,14,11).getTime();
function progressed(){
  const s=E.freshState(NOW);s.kitchenLevel=3;s.toolLevels.fill(2);s.cp=10000;
  for(let id=0;id<18;id++){s.total['0:'+id]=100;s.farm['0:'+id]=1;}
  claimActivity(s,'shrine',NOW);claimActivity(s,'yokai',NOW);return s;
}
function noChange(s,fn,pattern){const before=structuredClone(s);assert.throws(fn,pattern);assert.deepEqual(s,before);}

test('the shrine is read-only, shows all fifteen original signs, and no new-player rewards',()=>{
  const s=E.freshState(NOW),before=structuredClone(s),book=shrineBook(s,NOW);
  assert.deepEqual(book.signs.map(s=>s.id),FORTUNE_IDS);assert.equal(book.gift.available,false);
  assert.equal(book.discovered,0);assert.equal(book.goals.length,8);assert.ok(book.goals.every(g=>!g.available));
  assert.deepEqual(s,before);
});
test('drawing a sign is separate from discovering it and preparing it never consumes anything',()=>{
  const s=progressed();claimActivity(s,'shrine-gift',NOW,()=>0);const before=structuredClone(s);
  const book=shrineBook(s,NOW);assert.equal(book.heldId,89);assert.equal(book.latest,89);assert.equal(book.discovered,0);
  assert.equal(book.gift.available,false);assert.equal(book.signs[0].held,true);
  assert.equal(prepareGiftRecipe(s,68).toolId,0);assert.equal(s.egg,0);assert.deepEqual(s.selected,[68]);
  for(const key of ['cp','events','batch','farm','total','ingredients'])assert.deepEqual(s[key],before[key],key);
});
test('special gift preparation preserves an active duck batch and only replaces next-batch choices',()=>{
  const s=progressed();s.duck=true;s.egg=1;E.startBatch(s,1,NOW,()=>.5);s.ingredients={68:1,69:1,70:1,0:1};s.selected=[0];
  const batch=structuredClone(s.batch),cp=s.cp,inventory=structuredClone(s.ingredients);
  for(const [id,tool] of [[68,0],[69,1],[70,2]]){
    assert.equal(giftRecipe(s,id).activeBatch,true);assert.equal(prepareGiftRecipe(s,id).toolId,tool);
    assert.deepEqual(s.batch,batch);assert.deepEqual(s.ingredients,inventory);assert.equal(s.cp,cp);assert.deepEqual(s.selected,[id]);
  }
});
test('missing gift and missing cookware cannot replace next-batch choices',()=>{
  const s=E.freshState(NOW);s.selected=[0];
  noChange(s,()=>prepareGiftRecipe(s,68),/先领取/);
  s.ingredients[69]=1;noChange(s,()=>prepareGiftRecipe(s,69),/对应厨具/);
  noChange(s,()=>prepareGiftRecipe(s,0),/特别配方/);
});
test('the prepared sign uses the original recipe and only a collected chick stamps the book',()=>{
  const s=progressed();claimActivity(s,'shrine-gift',NOW,()=>0);prepareGiftRecipe(s,68);E.startBatch(s,0,NOW,()=>.99);
  const index=s.batch.eggs.findIndex(e=>e.id===89);assert.ok(index>=0);assert.equal(s.batch.eggs.filter(e=>e.id===89).length,24);
  let book=shrineBook(s,NOW);assert.equal(book.held,false);assert.equal(book.signs[0].cooking,true);assert.equal(book.discovered,0);
  s.batch.eggs[index].status='ready';E.collect(s,index);book=shrineBook(s,NOW);assert.equal(book.discovered,1);assert.equal(book.signs[0].found,true);
});
test('all eight collections count real lifetime discoveries, including old sold companions',()=>{
  const s=progressed();s.farm={};
  for(const id of [...FORTUNE_IDS,67,105,106,107,114,115,116,117,118,119,104])s.total['0:'+id]=1;
  for(let id=0;id<5;id++)s.total['1:'+id]=1;
  const original=structuredClone(s),rewards=shrineGoals(s);assert.ok(rewards.every(g=>g.available));
  for(const g of rewards){assert.equal(claimShrineGoal(s,g.id).cp,g.cp);noChange(s,()=>claimShrineGoal(s,g.id),/已收下/);}
  assert.equal(s.cp,original.cp+SHRINE_GOALS.reduce((sum,g)=>sum+g.cp,0));
  for(const key of ['ingredients','farm','total','selected','batch','toolLevels'])assert.deepEqual(s[key],original[key]);
  assert.equal(s.events.campaign_char_0_88,true);assert.ok(shrineGoals(s).every(g=>g.claimed&&!g.available));
});
test('partial progress, unknown goals and CP overflow cannot award or consume progress',()=>{
  const s=progressed();s.total['0:89']=1000;s.total['0:90']=1000;
  assert.equal(shrineGoals(s)[0].current,2);noChange(s,()=>claimShrineGoal(s,'signs-3'),/再收录/);
  noChange(s,()=>claimShrineGoal(s,'unknown'),/没有找到/);
  s.total['0:91']=1;s.cp=Number.MAX_SAFE_INTEGER-299;
  noChange(s,()=>claimShrineGoal(s,'signs-3'),/上限/);
});
test('milestones and sign sequence survive current backup and do not disturb unrelated events or live eggs',()=>{
  const s=progressed();s.events.unrelated={keep:true};for(const id of [89,90,91])s.total['0:'+id]=1;
  claimShrineGoal(s,'signs-3');claimActivity(s,'shrine-gift',NOW,()=>0);prepareGiftRecipe(s,68);E.startBatch(s,0,NOW,()=>.99);
  const restored=parseBackup(makeBackup(s,NOW),NOW);
  assert.deepEqual(restored.batch,s.batch);assert.deepEqual(restored.events,s.events);assert.equal(restored.cp,s.cp);
  assert.equal(shrineGoals(restored)[0].claimed,true);assert.equal(shrineBook(restored,NOW).latest,89);
  noChange(restored,()=>claimShrineGoal(restored,'signs-3'),/已收下/);
});
