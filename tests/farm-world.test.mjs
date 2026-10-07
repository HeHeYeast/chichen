import test from 'node:test';
import assert from 'node:assert/strict';
import {freshState} from '../web/engine.js';
import {residentsFor,clampCamera,zoomCamera,hitPlace,PLACES,HOME_CAMERA} from '../web/farm-world.js';
test('camp residents represent actual home stock, include order reservations, exclude trips and business',()=>{
  const s=freshState();s.farm={'0:0':4,'0:3':2};
  s.progress.trip={status:'running',members:['0:0']};
  s.expansion.business.active={stock:{'0:0':2}};
  s.expansion.orders.active=[{reserved:{'0:3':1}}];
  const residents=residentsFor(s);assert.equal(residents.length,3);
  assert.equal(residents.filter(r=>r.key==='0:0').length,1);assert.equal(residents.filter(r=>r.key==='0:3').length,2);
  assert.ok(residents.every(r=>r.height===19));assert.deepEqual(residentsFor(freshState()),[]);
});
test('the camp samples at most eight residents without fabricating unowned varieties',()=>{
  const s=freshState();s.farm={'0:0':999,'0:4':1};const r=residentsFor(s);assert.equal(r.length,8);assert.equal(r.filter(r=>r.key==='0:4').length,1);
});
test('camera is bounded at near and far scales; default is a partial world view',()=>{
  assert.ok(320/HOME_CAMERA.zoom<1536/2);
  assert.deepEqual(clampCamera({x:-100,y:9999,zoom:.1},320,550),{x:768,y:512,zoom:320/1536});
  const c=clampCamera({x:-100,y:9999,zoom:2},320,550);assert.equal(c.zoom,1.6);assert.equal(c.x,100);assert.equal(c.y,1024-550/3.2);
});
test('zoom preserves the world point under the cursor away from edges',()=>{
  const c={x:768,y:512,zoom:.8},p={x:220,y:200},z=zoomCamera(c,1.2,p,320,550);
  assert.ok(Math.abs(c.x+(p.x-160)/c.zoom-z.x-(p.x-160)/z.zoom)<1e-9);
  assert.ok(Math.abs(c.y+(p.y-275)/c.zoom-z.y-(p.y-275)/z.zoom)<1e-9);
});
test('building artwork hit areas match their semantic destination; river has no action',()=>{
  for(const p of PLACES)assert.equal(hitPlace(p.rect[0]+p.rect[2]/2,p.rect[1]+p.rect[3]/2).id,p.id);
  assert.equal(hitPlace(80,300),undefined);
});
