import test from 'node:test';
import {freshOrders} from '../web/orders.js';
import assert from 'node:assert/strict';
import {freshState} from '../web/engine.js';
import {freshFacts} from '../web/facts.js';
import {freshBusiness,openBusiness,BUSINESS_WINDOW_MS as W} from '../web/business.js';
import {advanceTimeline,closeBusinessTimeline} from '../web/timeline.js';
import {inventoryView} from '../web/inventory.js';
import {departRegional} from '../web/regional-exploration.js';

const T=1800000000000;
function fixture(){
  const s=freshState(T,41);s.kitchenLevel=3;s.toolLevels[1]=0;s.total={'0:0':240,'0:3':3,'0:8':3,'0:4':3,'0:6':3};s.farm={'0:0':80,'0:3':4,'0:8':4};
  s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};
  s.expansion.business=freshBusiness();s.expansion.facts=freshFacts();s.expansion.orders=freshOrders();s.expansion.inventoryPolicy={keepOne:true,collectionLocks:[]};return s;
}
test('timeline online windows and one offline advance have identical sale, facts, RNG, stock and report',()=>{
  const a=fixture();openBusiness(a,{stock:{'0:0':72}},T);const b=structuredClone(a);
  for(let i=1;i<=24;i++)advanceTimeline(a,T+i*W/2);
  advanceTimeline(b,T+24*3600000);assert.deepEqual(a,b);
  const snapshot=structuredClone(b);advanceTimeline(b,T+45*86400000);assert.deepEqual(b,snapshot);
});
test('sale windows do not cause extra farm-loss checks; manual release protects S until after one old check',()=>{
  const s=fixture();s.farmFixed=T-20*86400000;s.farmChecked=T-86400001;
  openBusiness(s,{stock:{'0:0':24}},T);advanceTimeline(s,T+W);
  assert.equal(s.farmChecked,T-86400001);assert.equal(s.farm['0:0'],74);assert.equal(inventoryView(s,'0:0').S,18);
  const cp=s.cp,report=closeBusinessTimeline(s,T+W+1);assert.equal(report.totalSold,6);assert.equal(s.farm['0:0'],40);assert.equal(inventoryView(s,'0:0').S,0);assert.equal(s.cp,cp);
  const closed=structuredClone(s);closeBusinessTimeline(s,T+W+1);assert.deepEqual(s,closed);
});
test('simultaneous new trip and business release checks farm once and releases team after the check',()=>{
  const s=fixture();s.farmFixed=T-20*86400000;s.farmChecked=T-86400001;
  departRegional(s,{regionId:'V',placeId:'V:0',focus:'specimen',members:['0:3']},T);
  const end=s.progress.trip.endAt;assert.equal(end,T+W);
  openBusiness(s,{stock:{'0:0':6}},T);const single=structuredClone(s);
  advanceTimeline(s,T+W/2);advanceTimeline(s,end);advanceTimeline(single,end);
  assert.deepEqual(s,single);assert.equal(s.progress.trip.status,'returned');assert.equal(s.expansion.business.active,null);assert.equal(s.farmChecked,end);assert.ok(s.farm['0:3']>=1);
});
test('partial current windows do not sell and long offline time never automatically opens another session',()=>{
  const s=fixture();openBusiness(s,{stock:{'0:0':24}},T);advanceTimeline(s,T+W-1);assert.equal(s.cp,600);
  advanceTimeline(s,T+60*86400000);assert.equal(s.expansion.business.sequence,1);assert.equal(s.expansion.business.lastReport.totalSold,24);assert.equal(s.expansion.business.lastReport.closedAt,T+4*W);
});
