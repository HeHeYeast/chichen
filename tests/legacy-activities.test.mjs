import {CURRENT_SAVE_VERSION} from '../web/save-migrations.js';
import test from 'node:test';
import assert from 'node:assert/strict';
import {readFileSync} from 'node:fs';
import * as E from '../web/engine.js';
import {originalRecipes} from '../web/recipes.js';
import {makeBackup,parseBackup} from '../web/save-store.js';
import {fortunePool} from '../web/progression.js';
import {activityCatalog,getActivities,claimActivity,characterAccessInfo,ingredientActivityId} from '../web/legacy-activities.js';

const NOW=new Date(2026,8,14,14).getTime(),DAY=86400000;
const info=(state,id,now=NOW)=>getActivities(state,now).find(entry=>entry.id===id);
function progressed(){
  const state=E.freshState(NOW);state.cp=100000;state.kitchenLevel=3;state.toolLevels.fill(2);
  state.total=Object.fromEntries(Array.from({length:20},(_,id)=>[`0:${id}`,100]));
  state.farm={'0:0':100};state.events={unrelated:{kept:true}};return state;
}
function giftReady(){const state=progressed();claimActivity(state,'shrine',NOW);claimActivity(state,'yokai',NOW);return state;}
function unchangedOnFailure(state,action,message){const before=structuredClone(state);assert.throws(action,message);assert.deepEqual(state,before);}

test('permanent commissions cover every campaign flag in original recipes exactly once',()=>{
  const source=readFileSync(new URL('../web/recipes.js',import.meta.url),'utf8');
  const original=[...new Set(source.match(/campaign_char_[01]_\d+/g))].sort();
  const restored=activityCatalog.flatMap(entry=>entry.flags).sort();
  assert.deepEqual(restored,original);assert.equal(new Set(restored).size,restored.length);
  assert.equal(activityCatalog.filter(entry=>entry.kind==='campaign').length,10);
  assert.deepEqual(activityCatalog.filter(entry=>entry.kind==='gift').map(entry=>entry.ingredientId),[68,69,70]);
  for(const entry of activityCatalog)for(const condition of entry.requirements)assert.ok(condition.target>0);
});

test('reading commission details never grants rewards and unmet claims are atomic',()=>{
  const state=E.freshState(NOW),before=structuredClone(state);
  const entries=getActivities(state,NOW);assert.equal(entries.length,14);assert.ok(entries.every(entry=>!entry.available));
  assert.deepEqual(state,before);
  for(const entry of entries)unchangedOnFailure(state,()=>claimActivity(state,entry.id,NOW));
  unchangedOnFailure(state,()=>claimActivity(state,'unknown',NOW),/没有找到/);
  unchangedOnFailure(state,()=>claimActivity(state,'spring',NaN),/时间无效/);
});

test('commissions use lifetime discovery progress and preserve CP, live batch, inventory and unrelated events',()=>{
  const state=progressed();state.farm={};E.startBatch(state,0,NOW,()=>.5);
  const before=structuredClone(state);claimActivity(state,'spring',NOW);
  assert.equal(info(state,'spring').completed,true);
  for(const key of ['cp','farm','total','batch','ingredients','egg','duck','selected','toolLevels'])assert.deepEqual(state[key],before[key],key);
  assert.deepEqual(state.events.unrelated,before.events.unrelated);
  for(const flag of ['campaign_char_0_48','campaign_char_0_49','campaign_char_0_60'])assert.equal(state.events[flag],true);
  unchangedOnFailure(state,()=>claimActivity(state,'spring',NOW),/已完成/);
});

test('old campaign flags count as completed and partially unlocked groups preserve original flags',()=>{
  const state=progressed();state.events.campaign_char_0_48=true;
  assert.equal(info(state,'spring').completed,false);claimActivity(state,'spring',NOW);
  delete state.events.legacyActivityClaims;
  assert.equal(info(state,'spring').completed,true);assert.equal(info(state,'spring').available,false);
  const hint=characterAccessInfo(0,48,state,NOW);assert.equal(hint.qualified,true);assert.equal(hint.activityId,'spring');
});

test('each campaign unlock reaches its original recipe while required ingredients and cookware remain necessary',()=>{
  for(const entry of activityCatalog.filter(entry=>entry.kind==='campaign'))for(const {egg,id} of entry.characters){
    const state=progressed();claimActivity(state,entry.id,NOW);
    state.kitchenLevel=0;state.toolLevels.fill(0);state.total={'0:0':2000};
    state.events={[`campaign_char_${egg}_${id}`]:true};
    let tool=0,ingredients=[];
    if(egg===0&&[26,27].includes(id))ingredients=[15];
    if(egg===0&&id===32){tool=2;ingredients=[17];}
    if(egg===0&&id===82)tool=4;
    if(egg===1&&id===50){tool=4;state.toolLevels[4]=1;ingredients=[9,29];}
    assert.ok(originalRecipes(state,egg,tool,ingredients,NOW,()=>.99999).includes(id),`${egg}:${id} reachable`);
    state.events={};
    assert.ok(!originalRecipes(state,egg,tool,ingredients,NOW,()=>.99999).includes(id),`${egg}:${id} requires unlock`);
  }
  const state=progressed();claimActivity(state,'winter',NOW);
  assert.ok(!originalRecipes(state,0,0,[59],NOW,()=>.99999).includes(26),'ice is not snow crystal');
  state.toolLevels[4]=0;
  assert.ok(!originalRecipes(state,1,4,[9,29],NOW,()=>.99999).includes(50),'duck oven requires Lv.2');
  assert.match(characterAccessInfo(0,26,state).recipeText,/雪晶/);
  assert.match(characterAccessInfo(0,32,state).recipeText,/巧克力砖/);
  assert.match(characterAccessInfo(1,50,state).recipeText,/生姜/);
});

test('three special materials remain unavailable for normal purchase and point to working gift activities',()=>{
  const state=giftReady();
  for(const [ingredient,id] of [[68,'shrine-gift'],[69,'flame-gift'],[70,'cotton-gift']]){
    assert.equal(ingredientActivityId(ingredient),id);assert.ok(info(state,id).available);
    unchangedOnFailure(state,()=>E.buyIngredient(state,ingredient),/尚未解锁/);
  }
  assert.equal(ingredientActivityId(0),null);
});

test('full inventory and an unused matching gift never consume a gift entitlement',()=>{
  for(const [id,ingredient] of [['shrine-gift',68],['flame-gift',69],['cotton-gift',70]]){
    const state=giftReady();state.ingredients={0:30};
    unchangedOnFailure(state,()=>claimActivity(state,id,NOW),/已满/);
    state.ingredients={0:29};claimActivity(state,id,NOW);
    assert.equal(state.ingredients[ingredient],1);assert.equal(Object.values(state.ingredients).reduce((a,b)=>a+b),30);
    state.total['0:0']+=24;
    unchangedOnFailure(state,()=>claimActivity(state,id,NOW+DAY),/先用完/);
    state.ingredients[ingredient]=0;claimActivity(state,id,NOW+DAY);
    assert.equal(state.ingredients[ingredient],1);
  }
});

test('daily gifts require both a later local day and 24 further collections; clock rollback cannot repeat',()=>{
  const state=giftReady();claimActivity(state,'flame-gift',NOW);state.ingredients[69]=0;
  unchangedOnFailure(state,()=>claimActivity(state,'flame-gift',NOW),/今天已领取/);
  unchangedOnFailure(state,()=>claimActivity(state,'flame-gift',NOW+DAY),/24/);
  state.total['0:0']+=23;unchangedOnFailure(state,()=>claimActivity(state,'flame-gift',NOW+DAY),/24/);
  state.total['0:0']++;
  unchangedOnFailure(state,()=>claimActivity(state,'flame-gift',NOW-DAY),/今天已领取/);
  claimActivity(state,'flame-gift',NOW+DAY);assert.equal(state.ingredients[69],1);
});

test('all 15 shrine targets including 103 can be earned and each gift inserts its promised character into a batch',()=>{
  const state=giftReady();state.ingredients={};const cp=state.cp;
  for(let visit=0;visit<16;visit++){
    const now=NOW+visit*DAY,target=89+visit%15;
    assert.equal(info(state,'shrine-gift',now).targetCharacter,null);
    const pool=fortunePool(state).pool,index=pool.findIndex(p=>p.id===target),weight=pool.reduce((n,p)=>n+p.weight,0);
    const draw=(pool.slice(0,index).reduce((n,p)=>n+p.weight,0)+.1)/weight;
    const result=claimActivity(state,'shrine-gift',now,()=>draw);assert.equal(result.targetCharacter.id,target);
    const ids=originalRecipes(state,0,0,[68],now,()=>.5);
    assert.equal(ids.length,24);assert.equal(ids.filter(id=>id===target).length,1);
    state.ingredients[68]=0;state.total['0:0']+=24;
  }
  assert.equal(state.cp,cp);assert.equal(state.events.legacyOmikujiCount,16);
});

test('fire and cotton gifts use original guaranteed per-batch insertion without directly granting species',()=>{
  const state=giftReady();const before=structuredClone(state.farm);
  for(const [activity,ingredient,tool,target]of [['flame-gift',69,1,106],['cotton-gift',70,2,107]]){
    claimActivity(state,activity,NOW);assert.deepEqual(state.farm,before);
    const batch=originalRecipes(state,0,tool,[ingredient],NOW,()=>.5);
    assert.equal(batch.length,24);assert.equal(batch.filter(id=>id===target).length,1);
  }
});

test('time travel exchanges exactly one stocked chick, keeps its lifetime discovery and never disturbs the current batch',()=>{
  const state=progressed();E.startBatch(state,0,NOW,()=>.5);const before=structuredClone(state);
  assert.equal(info(state,'time-travel').available,true);const result=claimActivity(state,'time-travel',NOW);
  assert.equal(result.kind,'travel');assert.equal(state.farm['0:0'],before.farm['0:0']-1);
  assert.equal(state.farm['0:104'],1);assert.equal(state.total['0:104'],1);assert.equal(state.total['0:0'],before.total['0:0']);
  for(const key of ['cp','batch','ingredients','egg','duck','selected','toolLevels'])assert.deepEqual(state[key],before[key],key);
  assert.equal(characterAccessInfo(0,104,state).unlocked,true);
  unchangedOnFailure(state,()=>claimActivity(state,'time-travel',NOW),/今天已旅行/);
  unchangedOnFailure(state,()=>claimActivity(state,'time-travel',NOW+DAY),/24/);
  state.total['0:0']+=24;claimActivity(state,'time-travel',NOW+DAY);assert.equal(state.farm['0:104'],2);
});

test('time travel visits are exchanges and never count toward the next gift 24-collection progress',()=>{
  const state=giftReady();claimActivity(state,'shrine-gift',NOW);state.ingredients[68]=0;
  claimActivity(state,'time-travel',NOW);
  const progress=()=>info(state,'shrine-gift',NOW+DAY).conditions.find(c=>c.kind==='new-collections').current;
  assert.equal(progress(),0);
  state.total['0:0']+=24;assert.equal(progress(),24);assert.equal(info(state,'shrine-gift',NOW+DAY).available,true);
  // Claims saved before the snapshot existed keep their earlier count.
  delete state.events.legacyActivityClaims['shrine-gift'].travels;assert.equal(progress(),25);
});

test('time travel validates stock and target count limits before consuming a chick',()=>{
  for(const mutate of [state=>state.farm['0:0']=0,state=>state.farm['0:104']=99999,state=>state.total['0:104']=99999]){
    const state=progressed();mutate(state);unchangedOnFailure(state,()=>claimActivity(state,'time-travel',NOW));
  }
});

test('activity history, shrine target and travel progress survive current backup export/import and legacy migration',()=>{
  const state=giftReady();claimActivity(state,'shrine-gift',NOW);claimActivity(state,'time-travel',NOW);
  const imported=parseBackup(makeBackup(state,NOW,'1.1.0'),NOW+DAY);
  assert.deepEqual(imported,E.normalizeSave(state,NOW));assert.equal(imported.version,CURRENT_SAVE_VERSION);
  assert.equal(info(imported,'shrine-gift',NOW).claimedToday,true);
  assert.equal(info(imported,'time-travel',NOW).claimedToday,true);
  const old=structuredClone(state);old.version=1;old.toolLevels.pop();
  const migrated=E.normalizeSave(old,NOW+DAY);assert.deepEqual(migrated.events,state.events);assert.deepEqual(migrated.farm,state.farm);
});

test('phoenix access hints and recipes retain the original local hour window',()=>{
  const state=progressed();state.events={};state.kitchenLevel=0;state.total={};
  for(const hour of [0,9,10,11,12,13,23]){
    const now=new Date(2026,8,14,hour).getTime(),target=hour>=10&&hour<=12?52:51;
    let calls=0;const random=()=>calls++===0?0:.99999;
    const batch=originalRecipes(state,0,0,[],now,random);
    assert.ok(batch.includes(target));assert.ok(!batch.includes(target===52?51:52));
  }
  assert.match(characterAccessInfo(0,52,state).text,/10:00–12:59/);
  assert.match(characterAccessInfo(0,51,state).text,/13:00–23:59/);
});
