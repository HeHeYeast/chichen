import test from 'node:test';
import assert from 'node:assert/strict';
import {fitViewport,setViewportHeight,LAYOUT,RECT,navRect,toolRect,farmY,farmRect,contains} from '../web/theme.js';
import {freshState,startBatch} from '../web/engine.js';
import {FARM_ACTIONS,FARM_RECT} from '../web/farm-theme.js';

const close=(actual,expected,message)=>assert.ok(Math.abs(actual-expected)<1e-8,`${message}: ${actual} ≠ ${expected}`);
const atHeight=(height,check)=>{try{setViewportHeight(height);check();}finally{setViewportHeight(568);}};

test('portrait sizing fills Magic8 and common tall viewports without changing horizontal proportions',()=>{
  for(const [width,height] of [[1256,2503],[375,747],[430,932],[320,568]])for(const density of [1,2,3]){
    const fit=fitViewport(width,height,density);
    close(320*fit.scale,width,'visual width');close(fit.logicalHeight*fit.scale,height,'visual height');
    assert.equal(fit.width,Math.round(width*density));assert.equal(fit.height,Math.round(height*density));
    assert.ok(fit.logicalHeight>=568&&fit.logicalHeight<=800);
  }
});

test('landscape sizing keeps the entire portrait game centered without cropping its top or navigation',()=>{
  const fit=fitViewport(932,430,2);
  assert.equal(fit.logicalHeight,568);close(fit.scale*568,430,'landscape game height');
  assert.ok(fit.scale*320<932);assert.equal(fit.height,860);
});

test('resizing anchors navigation and cookware to the lower edge with stable hit-target sizes',()=>{
  for(const height of [568,320*2503/1256,320*747/375,320*932/430,800])atHeight(height,()=>{
    // Five fixed places (Work K): each at least 62px wide inside the 320 logical width.
    for(let index=0;index<5;index++){const nav=navRect(index);close(nav.y+nav.h,height-8,'navigation lower inset');assert.equal(nav.w,62);assert.equal(nav.h,54);assert.ok(nav.x>=0&&nav.x+nav.w<=320);if(index)assert.ok(nav.x>=navRect(index-1).x+navRect(index-1).w,'no overlap');assert.ok(contains(nav,nav.x+nav.w/2,nav.y+nav.h/2));}
    for(let index=0;index<4;index++){
      const nav=navRect(index),tool=toolRect(index);
      close(tool.y+tool.h,height-102,'cookware lower inset');assert.equal(tool.w,69);assert.equal(tool.h,82);
      assert.ok(tool.y>LAYOUT.timerY+26);assert.ok(tool.y+tool.h<RECT.next.y);
      assert.ok(contains(nav,nav.x+nav.w/2,nav.y+nav.h/2));
    }
    assert.ok(RECT.next.y+RECT.next.h<LAYOUT.navY);
  });
});

test('egg hit region translates with the nest while saved 6 by 4 positions and spacing stay intact',()=>{
  const state=freshState(1800000000000);startBatch(state,0,1800000000000,()=>.5,()=>.5);
  const before=structuredClone(state);
  for(const height of [568,638,694,800])atHeight(height,()=>{
    for(const egg of state.batch.eggs){
      assert.ok(contains(RECT.eggArea,egg.x,egg.y+LAYOUT.eggOffset+8),'every visible egg center is inside harvest region');
    }
    assert.equal(RECT.eggArea.w,224);assert.equal(RECT.eggArea.h,145);
    assert.deepEqual(state,before,'viewport updates cannot rewrite saved egg coordinates');
    close(state.batch.eggs[1].x-state.batch.eggs[0].x,31,'horizontal spacing');
    close(state.batch.eggs[6].y-state.batch.eggs[0].y,26,'vertical spacing');
  });
});

test('farm stretch shares its world mapping with building hit rectangles and leaves bottom actions above navigation',()=>{
  for(const height of [568,638,694,800])atHeight(height,()=>{
    close(farmY(58),58,'world top');close(farmY(502),height-66,'world bottom');
    for(const action of FARM_ACTIONS){
      const hit=farmRect(action.rect);
      close(hit.y,farmY(action.rect.y),'building upper edge');close(hit.y+hit.h,farmY(action.rect.y+action.rect.h),'building lower edge');
      const sign=action.sign;assert.ok(contains(hit,sign.x+sign.w/2,farmY(sign.y)+sign.h/2),action.id+' sign remains inside building hit area');
    }
    for(const action of Object.values(FARM_RECT))assert.ok(action.y+LAYOUT.extra+action.h<LAYOUT.navY);
  });
});

test('repeated portrait and landscape rotations do not accumulate offsets or move fixed HUD targets',()=>{
  const hud={level:structuredClone(RECT.level),settings:structuredClone(RECT.settings),ingredient:structuredClone(RECT.ingredient)};
  try{
    for(const height of [694,568,638,800,568,694,568])setViewportHeight(height);
    assert.equal(LAYOUT.extra,0);assert.equal(LAYOUT.eggOffset,0);assert.equal(LAYOUT.navY,506);assert.equal(RECT.eggArea.y,168);assert.equal(RECT.next.y,473);
    assert.deepEqual({level:RECT.level,settings:RECT.settings,ingredient:RECT.ingredient},hud);
  }finally{setViewportHeight(568);}
});
