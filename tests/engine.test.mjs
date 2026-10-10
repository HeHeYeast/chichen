import test from 'node:test';
import assert from 'node:assert/strict';
import { existsSync } from 'node:fs';
import { DATA } from '../web/data.js';
import * as E from '../web/engine.js';
import { originalRecipes } from '../web/recipes.js';
import { farmDisplay,visibleSpecies,timeZone } from '../web/farm.js';
const NOW=1800000000000, HOUR=3600000, DAY=24*HOUR, CLEAN=E.CLEAN_INTERVAL;
function rng(seed=42){return()=>{seed=(Math.imul(1664525,seed)+1013904223)>>>0;return seed/4294967296;};}
function prepared(){const s=E.freshState(NOW);s.toolLevels=Array(8).fill(2);s.kitchenLevel=3;s.cp=100000;return s;}

test('original data has 114 chickens, 57 ducks and every referenced sprite exists',()=>{
  assert.deepEqual(DATA.characters.map(a=>a.length),[114,57]);
  for(const path of DATA.images)assert.ok(existsSync(new URL('../'+path,import.meta.url)),path);
  for(const [egg,chars] of DATA.characters.entries())for(const c of chars)assert.ok(existsSync(new URL(`../assets/png/Character/character_${egg}/character_${egg}_${c.id}_0_0.png`,import.meta.url)));
});
test('new game has original 600 CP, lamp and one salt',()=>{
  const s=E.freshState(NOW);assert.equal(s.cp,600);assert.deepEqual(s.toolLevels,[0,-1,-1,-1,-1,-1,-1,-1,-1]);assert.deepEqual(s.ingredients,{0:1});assert.deepEqual(E.availableIngredients(s),[0]);
});
test('the requested compact 6 by 4 egg formation stays inside original nest footprint',()=>{
  const s=E.freshState(NOW);const eggs=E.startBatch(s,0,NOW,rng()).eggs;
  assert.equal(eggs.length,24);
  for(let row=0;row<4;row++){
    const members=eggs.filter(e=>Math.abs(e.y-(199+row*26))<=3);assert.equal(members.length,6);
    const xs=members.map(e=>e.x).sort((a,b)=>a-b);for(let col=0;col<6;col++)assert.ok(Math.abs(xs[col]-(80.5+col*31))<=3.5);
  }
  assert.ok(Math.max(...eggs.map(e=>e.x))-Math.min(...eggs.map(e=>e.x))<=161);
});
test('buy pan, use it and prevent premature collection',()=>{
  const s=E.freshState(NOW);E.buyTool(s,1);assert.equal(s.cp,100);
  const b=E.startBatch(s,1,NOW,rng());assert.equal(s.cp,10);assert.equal(b.eggs.length,24);assert.equal(b.ends,NOW+15*60000);
  assert.equal(E.collect(s,0),false);assert.equal(s.cp,10);
  for(const e of b.eggs){assert.ok(e.openAt>=NOW+75000&&e.openAt<=b.ends);assert.equal(e.blackAt-e.openAt,120*60000);}
});
test('all cookware and egg recipes produce 24 valid results across all levels and ingredients',()=>{
  const random=rng();let cases=0;
  for(let egg=0;egg<2;egg++)for(let level=0;level<3;level++)for(let id=0;id<8;id++)for(let ingredient=-1;ingredient<75;ingredient++){
    const s=prepared();s.toolLevels.fill(level);s.egg=egg;for(const [species,chars]of DATA.characters.entries())for(const c of chars)s.total[`${species}:${c.id}`]=100;
    const ids=originalRecipes(s,egg,id,ingredient<0?[]:[ingredient],NOW,random);
    assert.equal(ids.length,24,`${egg}/${level}/${id}/${ingredient}`);assert.ok(ids.every(n=>E.char(egg,n)),`${egg}/${level}/${id}/${ingredient}: ${ids}`);cases++;
  }
  assert.equal(cases,3648);
});
test('crack, hatch, collect and sell retain lifetime encyclopedia count',()=>{
  const s=E.freshState(NOW);const b=E.startBatch(s,0,NOW,rng());const e=b.eggs[0];
  E.updateBatch(s,e.openAt+1,rng());assert.equal(e.status,'cracking');
  E.updateBatch(s,e.openAt+2001,rng());assert.equal(e.status,'hatching');
  E.updateBatch(s,e.openAt+2901,rng());assert.equal(e.status,'ready');
  assert.ok(E.collect(s,0));assert.equal(E.collect(s,0),false);assert.equal(s.cp,601);
  assert.equal(E.sell(s,{'0:0':1}),3);assert.equal(s.cp,604);assert.equal(s.farm['0:0'],0);assert.equal(s.total['0:0'],1);
});
test('invalid transactions leave state unchanged',()=>{
  const s=E.freshState(NOW),before=structuredClone(s);
  assert.throws(()=>E.buyTool(s,2));assert.throws(()=>E.sell(s,{'0:0':1}));assert.throws(()=>E.buyIngredient(s,1));assert.deepEqual(s,before);
  s.toolLevels[1]=0;s.cp=0;const poor=structuredClone(s);assert.throws(()=>E.startBatch(s,1,NOW));assert.deepEqual(s,poor);
});
test('ingredient stock grows beyond the former shared cap and invalid purchases are atomic',()=>{
  const s=prepared();s.ingredients={0:29};E.buyIngredient(s,1);assert.equal(s.ingredients[1],1);
  E.buyIngredient(s,2);assert.equal(s.ingredients[2],1);
  const before=structuredClone(s);assert.throws(()=>E.buyIngredient(s,2,-1));assert.deepEqual(s,before);
});
test('ingredient unlocks require the specific cookware or discovered duck',()=>{
  const s=E.freshState(NOW);s.kitchenLevel=2;assert.deepEqual(E.availableIngredients(s),[0,36]);
  s.toolLevels[1]=1;assert.ok(E.availableIngredients(s).includes(19));assert.ok(!E.availableIngredients(s).includes(21));assert.ok(!E.availableIngredients(s).includes(44));
  s.duck=true;assert.ok(!E.availableIngredients(s).includes(44));s.total['1:0']=1;assert.ok(E.availableIngredients(s).includes(44));
  s.total['0:0']=1999;assert.ok(E.availableIngredients(s).includes(35));
});
test('selected ingredients are consumed once, slots respect kitchen level',()=>{
  const s=E.freshState(NOW);s.selected=[0,0,1];s.ingredients={0:2,1:2};E.startBatch(s,0,NOW,rng());assert.deepEqual(s.batch.ingredients,[0]);assert.deepEqual(s.ingredients,{0:1,1:2});assert.deepEqual(s.selected,[]);
});
test('dirty kitchen sickness and prevention follow batch ingredients',()=>{
  for(const immune of [false,true]){const s=prepared();s.dirty=true;E.startBatch(s,1,NOW,rng());const e=s.batch.eggs[0];e.id=4;e.immune=immune;e.openAt=NOW;e.tickets=Array(6).fill(.2);E.updateBatch(s,NOW+1,()=>.2);assert.equal(e.id,immune?4:1);}
});
test('overcooking burns a normal pan chick but lamp and protected recipes survive',()=>{
  for(const id of [0,1]){const s=prepared();E.startBatch(s,id,NOW,rng());const e=s.batch.eggs[0];e.id=4;e.status='ready';e.blackAt=NOW;E.updateBatch(s,NOW+1,()=>.9);assert.equal(e.id,id===0?4:2);}
  const s=prepared();s.ingredients={36:1};s.selected=[36];E.startBatch(s,1,NOW,rng());assert.ok(s.batch.eggs.every(e=>e.blackAt===null));
});
test('upgrade requirements expose each missing cookware level and distinguish insufficient CP',()=>{
  const s=E.freshState(NOW),before=structuredClone(s),info=E.kitchenUpgradeInfo(s);
  assert.equal(info.currentLevel,1);assert.equal(info.targetLevel,2);assert.equal(info.cost,10000);assert.equal(info.slots,1);
  assert.equal(info.maxed,false);assert.equal(info.toolsReady,false);assert.equal(info.affordable,false);assert.equal(info.canUpgrade,false);
  assert.deepEqual(info.requirements,[0,1,2,3,4,5].map(id=>({id,currentLevel:id===0?1:0,requiredLevel:1,met:id===0})));
  assert.deepEqual(s,before);
  s.toolLevels.fill(0,0,6);s.cp=9999;
  assert.equal(E.canUpgradeKitchen(s),true);assert.equal(E.kitchenUpgradeInfo(s).canUpgrade,false);
  s.cp=10000;assert.equal(E.kitchenUpgradeInfo(s).canUpgrade,true);
});
test('three kitchen upgrades follow all six cookware levels and preserve the live batch',()=>{
  const s=E.freshState(NOW);s.cp=1000000;
  for(let id=1;id<6;id++)E.buyTool(s,id);
  const batch=E.startBatch(s,0,NOW,rng()),savedBatch=structuredClone(batch);
  for(let current=0;current<3;current++){
    assert.equal(s.kitchenLevel,current);assert.equal(E.canUpgradeKitchen(s),true);
    assert.equal(E.canBuyTool(s,6),false);
    if(current<2)assert.equal(E.canBuyTool(s,0),false);
    s.dirty=true;const beforeCP=s.cp,at=NOW+(current+1)*HOUR;
    E.upgradeKitchen(s,at);
    assert.equal(s.cp,beforeCP-[10000,20000,30000][current]);assert.equal(s.kitchenLevel,current+1);
    assert.equal(s.dirty,false);assert.equal(s.lastClean,at);assert.equal(s.lastSeen,at);
    assert.equal(s.batch,batch);assert.deepEqual(s.batch.eggs.map(e=>[e.id,e.openAt,e.blackAt,e.collected]),savedBatch.eggs.map(e=>[e.id,e.openAt,e.blackAt,e.collected]));
    assert.equal(E.kitchenUpgradeInfo(s).slots,[2,3,3][current]);
    if(current<2){
      assert.equal(E.canUpgradeKitchen(s),false);
      for(let id=0;id<6;id++){assert.equal(E.canBuyTool(s,id),true);E.buyTool(s,id);}
    }
  }
  const info=E.kitchenUpgradeInfo(s);
  assert.equal(info.maxed,true);assert.equal(info.currentLevel,4);assert.equal(info.targetLevel,null);assert.equal(info.cost,0);
  assert.equal(info.toolsReady,true);assert.equal(info.canUpgrade,false);assert.equal(E.canUpgradeKitchen(s),false);
  assert.ok(info.requirements.every(r=>r.currentLevel===3&&r.requiredLevel===3&&r.met));
  assert.equal(E.canBuyTool(s,6),true);assert.equal(E.canBuyTool(s,7),false);
  for(let level=0;level<3;level++)E.buyTool(s,6);
  assert.equal(E.canBuyTool(s,7),true);
});
test('upgrade rejection leaves balance, batch and maintenance state unchanged',()=>{
  for(let level=0;level<3;level++){
    const s=prepared();s.kitchenLevel=level;s.toolLevels.fill(level,0,6);s.dirty=true;
    E.startBatch(s,0,NOW,rng());s.cp=100000;
    s.toolLevels[5]=level-1;const missing=structuredClone(s);
    assert.throws(()=>E.upgradeKitchen(s,NOW+DAY),/前六种/);assert.deepEqual(s,missing);
    s.toolLevels[5]=level;s.cp=(level+1)*10000-1;const poor=structuredClone(s);
    assert.throws(()=>E.upgradeKitchen(s,NOW+DAY),/CP不足/);assert.deepEqual(s,poor);
  }
  const maxed=prepared();maxed.cp=0;maxed.dirty=true;const before=structuredClone(maxed);
  assert.throws(()=>E.upgradeKitchen(maxed,NOW+DAY),/最高等级/);assert.deepEqual(maxed,before);
});
test('upgrade restarts the cleaning interval without changing farm maintenance',()=>{
  const s=prepared();s.kitchenLevel=0;s.lastSeen=NOW-2*DAY;s.lastClean=NOW-3*DAY;s.dirty=true;
  const farmFixed=s.farmFixed,farmChecked=s.farmChecked;
  E.upgradeKitchen(s,NOW);E.resume(s,NOW+DAY-1);
  assert.equal(s.dirty,false);assert.equal(s.lastClean,NOW);assert.equal(s.farmFixed,farmFixed);assert.equal(s.farmChecked,farmChecked);
  E.resume(s,NOW+2*DAY+1);assert.equal(s.dirty,true);
});
test('36 hours since cleaning dirties kitchen and cleaning uses each stage confirmation price',()=>{
  for(const [level,cost] of [100,150,200,250].entries()){
    const s=E.freshState(NOW);s.kitchenLevel=level;E.resume(s,NOW+CLEAN+1);assert.equal(s.dirty,true);
    assert.equal(E.cleanCost(s),cost);E.clean(s,NOW+CLEAN+1);
    assert.equal(s.cp,600-cost);assert.equal(s.dirty,false);assert.equal(s.lastClean,NOW+CLEAN+1);
    const cleaned=structuredClone(s);E.clean(s,NOW+CLEAN+2);assert.deepEqual(s,cleaned);
    s.dirty=true;s.cp=cost-1;const poor=structuredClone(s);
    assert.throws(()=>E.clean(s,NOW+2*CLEAN),/CP不足/);assert.deepEqual(s,poor);
  }
});

test('frequent play and save reloads do not postpone the next cleaning',()=>{
  let s=E.freshState(NOW);
  for(let hour=1;hour<36;hour++){
    E.resume(s,NOW+hour*HOUR);
    s=E.readSave({getItem:()=>JSON.stringify(s)},'save',NOW+hour*HOUR);
    assert.equal(s.dirty,false);assert.equal(s.lastClean,NOW);
  }
  E.resume(s,NOW+CLEAN+1);
  assert.equal(s.dirty,true);assert.equal(s.lastSeen,NOW+CLEAN+1);
  assert.equal(s.lastClean,NOW);
});

test('an active empty kitchen becomes dirty, and cleaning starts a full new interval',()=>{
  const s=E.freshState(NOW);const farmTimes=[s.farmFixed,s.farmChecked];
  E.updateBatch(s,NOW+CLEAN-1);assert.equal(s.dirty,false);
  E.updateBatch(s,NOW+CLEAN+1);assert.equal(s.dirty,true);assert.equal(s.batch,null);
  E.clean(s,NOW+CLEAN+2);assert.equal(s.dirty,false);
  E.updateBatch(s,NOW+2*CLEAN+1);assert.equal(s.dirty,false);
  E.updateBatch(s,NOW+2*CLEAN+3);assert.equal(s.dirty,true);
  assert.deepEqual([s.farmFixed,s.farmChecked],farmTimes);assert.equal(s.cp,500);
});

test('naturally dirty kitchens can hatch and collect every level of sickness-related chicken and duck',()=>{
  for(const egg of [0,1])for(let level=0;level<4;level++){
    const s=E.freshState(NOW);s.kitchenLevel=level;s.duck=true;s.egg=egg;
    const batch=E.startBatch(s,0,NOW+CLEAN,()=>.5),e=batch.eggs[0],at=e.openAt+1;
    assert.equal(s.dirty,true);
    e.tickets=Array(6).fill(0);E.updateBatch(s,at,()=>0);
    const target=(egg?[1,19,29,36]:[1,34,53,68])[level];
    assert.equal(s.dirty,true);assert.equal(e.id,target);
    E.updateBatch(s,at+2000,()=>0);assert.ok(E.collect(s,0));
    assert.equal(s.total[`${egg}:${target}`],1);assert.equal(s.farm[`${egg}:${target}`],1);
    assert.equal(E.collect(s,0),false);
  }
});

test('natural dirt keeps preventive ingredients and sickness-exempt species effective',()=>{
  for(const egg of [0,1])for(const protectedBy of ['ingredient','species']){
    const s=E.freshState(NOW);s.duck=true;s.egg=egg;
    if(protectedBy==='ingredient'){s.ingredients={18:1};s.selected=[18];}
    const e=E.startBatch(s,0,NOW+CLEAN,()=>.5).eggs[0];
    if(protectedBy==='species')e.id=2;
    const before=e.id;E.updateBatch(s,e.openAt+1,()=>0);
    assert.equal(s.dirty,true);assert.equal(e.id,before);
  }
});
test('farm HP and tiered repair costs match original thresholds',()=>{
  const s=E.freshState(NOW);
  for(const [hp,cost] of [[100,0],[60,40],[50,60],[20,120],[0,200]]){const at=NOW+(100-hp)*2*HOUR;assert.equal(E.farmHP(s,at),hp);assert.equal(E.repairCost(s,at),cost);}
  E.repair(s,NOW+200*HOUR);assert.equal(s.cp,400);assert.equal(E.farmHP(s,NOW+200*HOUR),100);
  const fixed=s.farmFixed;assert.equal(E.repair(s,NOW+201*HOUR),false);assert.equal(s.farmFixed,fixed);assert.equal(s.cp,400);
});
test('farm escape reduces current stock only and cannot repeat in same day',()=>{
  const s=E.freshState(NOW);s.farm={'0:0':100,'0:4':3};s.total={'0:0':100,'0:4':3};s.farmFixed=NOW-160*HOUR;s.farmChecked=NOW-DAY-1;
  assert.equal(E.checkFarmLoss(s,NOW,()=>.9),43);assert.equal(s.farm['0:0'],60);assert.equal(s.farm['0:4'],0);assert.equal(s.total['0:0'],100);assert.equal(E.checkFarmLoss(s,NOW+HOUR),0);
});
test('abandoned destroyed farm loses all stock after four days',()=>{
  const s=E.freshState(NOW);s.farm={'0:0':100};assert.equal(E.checkFarmLoss(s,NOW+10*DAY),100);
});
test('save round-trip keeps batch deadlines; corrupt data starts safely',()=>{
  const s=E.freshState(NOW);E.startBatch(s,0,NOW,rng());const raw=JSON.stringify(s),restored=E.readSave({getItem:()=>raw},'save',NOW+1000);assert.equal(restored.batch.eggs[0].openAt,s.batch.eggs[0].openAt);
  assert.equal(E.readSave({getItem:()=>'{broken'},'save',NOW).cp,600);
});
test('farm displays one representative per species and preserves special positions',()=>{
  const s=E.freshState(NOW);s.farm={'0:0':999,'0:32':12,'0:64':5};
  const date=new Date(NOW);date.setHours(12);const day=farmDisplay(s,date.getTime(),rng());
  assert.equal(day.filter(w=>w.id===0).length,1);assert.equal(day.some(w=>w.id===64),false);
  const roof=day.find(w=>w.id===32);assert.deepEqual([roof.x,roof.y],[18,115]);
  date.setHours(22);const night=farmDisplay(s,date.getTime(),rng());assert.ok(night.some(w=>w.id===64&&w.x===660));
});
test('farm time periods and dawn population match original',()=>{
  const date=new Date(NOW);for(const [hour,zone] of [[4,30],[5,0],[8,10],[17,20],[19,30]]){date.setHours(hour);assert.equal(timeZone(date.getTime()),zone);}
  assert.equal(visibleSpecies(0,3,0),false);assert.equal(visibleSpecies(0,0,0),true);assert.equal(visibleSpecies(1,36,10),false);assert.equal(visibleSpecies(1,36,20),true);
});
