import test from 'node:test';
import assert from 'node:assert/strict';
import {LAYOUT,RECT,NAV,navRect,toolRect,duckRect,contains,MOTION,canvasSizing} from '../web/theme.js';
import {freshState,startBatch} from '../web/engine.js';

const intersects=(a,b)=>a.x<b.x+b.w&&a.x+a.w>b.x&&a.y<b.y+b.h&&a.y+a.h>b.y;
test('canvas preserves the full game at 1x, 2x, 3x and on a tall phone',()=>{
  for(const density of [1,2,3]){
    const exact=canvasSizing(320,568,density);
    assert.deepEqual(exact,{scale:1,width:320*density,height:568*density});
    const tall=canvasSizing(375,813,density);
    assert.equal(tall.width,375*density);
    assert.ok(Math.abs(tall.height-665.625*density)<=.5);
    assert.ok(tall.height<=813*density);
  }
});
const kitchenControls=()=>[
  ...['level','settings','ingredient','clean','alarm','prev','next'].map(id=>[id,RECT[id]]),
  ...NAV.map((item,slot)=>['nav:'+item.id,navRect(slot)]),
  ...Array.from({length:4},(_,slot)=>['tool:'+slot,toolRect(slot)]),
  ...[0,1].map(id=>['duck:'+id,duckRect(id)]),
];

test('busy kitchen controls fit the design viewport and do not share clickable area',()=>{
  const controls=kitchenControls();
  for(const [name,rect] of controls){
    assert.ok(rect.w>0&&rect.h>0,name+' has a nonempty target');
    assert.ok(rect.x>=0&&rect.y>=0,name+' starts inside the viewport');
    assert.ok(rect.x+rect.w<=LAYOUT.width&&rect.y+rect.h<=LAYOUT.height,name+' ends inside the viewport');
    assert.equal(intersects(rect,RECT.eggArea),false,name+' must not steal a harvest gesture');
  }
  for(let i=0;i<controls.length;i++)for(let j=i+1;j<controls.length;j++){
    assert.equal(intersects(controls[i][1],controls[j][1]),false,controls[i][0]+' overlaps '+controls[j][0]);
  }
});

test('pressing cookware retains separation from pagination and bottom navigation',()=>{
  const pagerTop=Math.min(RECT.prev.y,RECT.next.y);
  const pagerBottom=Math.max(RECT.prev.y+RECT.prev.h,RECT.next.y+RECT.next.h);
  for(let slot=0;slot<4;slot++){
    const tool=toolRect(slot),nav=navRect(slot);
    assert.ok(tool.y+tool.h+MOTION.pressDepth<pagerTop,'pressed cookware must stop above the pager');
    assert.ok(pagerBottom<nav.y,'pagination must stop above navigation');
    assert.ok(nav.y+nav.h+MOTION.pressDepth<=LAYOUT.height,'pressed navigation remains visible');
  }
});

test('all original randomized egg positions remain in the dedicated harvest region',()=>{
  // Exercise the original placement extremes, not just one attractive fixture.
  for(const randomValue of [0,.01,.25,.5,.75,.99,.999999]){
    const state=freshState(1800000000000);
    const batch=startBatch(state,0,1800000000000,()=>randomValue);
    assert.equal(batch.eggs.length,24);
    for(const egg of batch.eggs){
      const target={x:egg.x-19,y:egg.y-13,w:38,h:42};
      assert.ok(contains(RECT.eggArea,target.x,target.y),'top-left egg target is inside harvest area');
      assert.ok(contains(RECT.eggArea,target.x+target.w,target.y+target.h),'bottom-right egg target is inside harvest area');
      for(const [name,control] of kitchenControls())assert.equal(intersects(target,control),false,'egg overlaps '+name);
    }
  }
});

test('shared hit testing accepts the control edge and cancels immediately outside it',()=>{
  const rect=RECT.settings;
  assert.equal(contains(rect,rect.x,rect.y),true);
  assert.equal(contains(rect,rect.x+rect.w,rect.y+rect.h),true);
  assert.equal(contains(rect,rect.x-.01,rect.y+rect.h/2),false);
  assert.equal(contains(rect,rect.x+rect.w+.01,rect.y+rect.h/2),false);
  assert.equal(contains(rect,rect.x+rect.w/2,rect.y-.01),false);
  assert.equal(contains(rect,rect.x+rect.w/2,rect.y+rect.h+.01),false);
});
