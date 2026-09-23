import test from 'node:test';
import assert from 'node:assert/strict';
import {existsSync,readFileSync} from 'node:fs';
import * as E from '../web/engine.js';
import {toolImage} from '../web/catalog.js';
import {resolveSprite,ASSET_FILES} from '../web/art/manifest.js';
const NOW=1800000000000,DAY=E.CLEAN_INTERVAL;

test('cleanliness and early prices advance together through all four kitchens',()=>{
  for(const [level,fullCost] of [100,150,200,250].entries()){
    const s=E.freshState(NOW);s.kitchenLevel=level;
    for(let percent=0;percent<=100;percent++){
      const at=NOW+DAY*percent/100,info=E.kitchenCleanInfo(s,at);
      assert.equal(info.percent,percent);assert.equal(info.cost,Math.ceil(fullCost*percent/100));
      assert.equal(info.dirty,percent===100);assert.equal(info.canClean,percent>0);
      assert.equal(E.cleanCost(s,at),info.cost);
    }
    assert.equal(E.kitchenCleanInfo(s,NOW+10*DAY).cost,fullCost);
    assert.equal(s.dirty,false,'read-only inspection must not mutate the save');
  }
});
test('early cleaning resets the clock, preserves the batch and cannot charge twice',()=>{
  const s=E.freshState(NOW);E.startBatch(s,0,NOW,()=>.5);
  const batch=structuredClone(s.batch),at=NOW+DAY/4,balance=s.cp;
  assert.equal(E.clean(s,at),true);assert.equal(s.cp,balance-25);
  assert.equal(s.lastClean,at);assert.equal(s.dirty,false);assert.deepEqual(s.batch.eggs.map(e=>[e.openAt,e.blackAt,e.collected]),batch.eggs.map(e=>[e.openAt,e.blackAt,e.collected]));
  const clean=structuredClone(s);assert.equal(E.clean(s,at+100),false);assert.deepEqual(s,clean);
  E.updateKitchen(s,at+DAY-1);assert.equal(s.dirty,false);
  E.updateKitchen(s,at+DAY);assert.equal(s.dirty,true);
});
test('insufficient CP leaves early and fully dirty states untouched',()=>{
  for(const elapsed of [DAY/10,DAY,DAY*2]){
    const s=E.freshState(NOW);s.cp=E.cleanCost(s,NOW+elapsed)-1;
    const before=structuredClone(s);assert.throws(()=>E.clean(s,NOW+elapsed),/CP不足/);
    assert.deepEqual(s,before);
  }
});
test('old dirty flags stay dirty even with a recently reset or backwards clock',()=>{
  const s=E.freshState(NOW);s.dirty=true;
  for(const at of [NOW-DAY,NOW,NOW+DAY]){
    const info=E.kitchenCleanInfo(s,at);assert.equal(info.percent,100);assert.equal(info.cost,100);
  }
  const clean=E.freshState(NOW);assert.equal(E.kitchenCleanInfo(clean,NOW-DAY).percent,0);
  assert.equal(E.clean(clean,NOW-DAY),false);assert.equal(clean.cp,600);assert.equal(clean.lastClean,NOW);
});
test('saved partial dirt restores the same percentage, fee and due time',()=>{
  const s=E.freshState(NOW),at=NOW+DAY*.6;s.kitchenLevel=3;
  E.resume(s,at);const expected=E.kitchenCleanInfo(s,at);
  const loaded=E.readSave({getItem:()=>JSON.stringify(s)},'save',at);
  assert.deepEqual(E.kitchenCleanInfo(loaded,at),expected);
  E.resume(loaded,at+100);assert.equal(loaded.lastClean,NOW);
  E.clean(loaded,at);assert.equal(loaded.cp,450);assert.equal(E.kitchenCleanInfo(loaded,at).percent,0);
});
test('delayed confirmations reject a changed fee or a newly cleaned kitchen',()=>{
  const s=E.freshState(NOW),at=NOW+DAY/4;
  const quote={cost:E.cleanCost(s,at),lastClean:s.lastClean,kitchenLevel:s.kitchenLevel};
  const before=structuredClone(s);
  assert.throws(()=>E.clean(s,at+DAY/100,quote),/已变化/);assert.deepEqual(s,before);
  E.clean(s,at);const cleaned=structuredClone(s);
  assert.throws(()=>E.clean(s,at+DAY/4,quote),/已变化/);assert.deepEqual(s,cleaned);
});
test('all 27 cookware tiers resolve to distinct transparent, proportionate redrawn sprites',()=>{
  const paths=new Set();
  for(let id=0;id<9;id++)for(let level=0;level<3;level++){
    const path=toolImage(1,id,level),sprite=resolveSprite(path);paths.add(path);
    assert.match(path,/^\/web\/art\//);assert.equal(sprite.fit,true);
    const [x,y,w,h]=sprite.frame,[width,height]=sprite.size;
    assert.ok(x>=0&&y>=0&&w>0&&h>0&&x+w<=width&&y+h<=height,path);
    assert.ok(existsSync(new URL('..'+sprite.file,import.meta.url)),path);
    assert.ok(ASSET_FILES.includes(sprite.file));
    if(id<8){const png=readFileSync(new URL('..'+sprite.file,import.meta.url));assert.equal(png[25],6,'RGBA PNG');}
  }
  assert.equal(paths.size,27);
});
