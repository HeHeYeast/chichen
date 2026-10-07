import test from 'node:test';
import assert from 'node:assert/strict';
import {freshState} from '../web/engine.js';
import {suggestBusinessStock,businessForecast} from '../web/business-advisor.js';
import {prepareBusiness} from '../web/business.js';
import {assignMenuRoles,menuSnapshot,menuFit} from '../web/menu-model.js';
import {RULES} from '../web/integration-data.js';

const NOW=1800000000000;
function farm(entries){
  const s=freshState(NOW,3);s.kitchenLevel=0; // capacity 24
  for(const [key,n] of Object.entries(entries)){s.farm[key]=n;s.total[key]=n;}
  return s;
}
const kinds=stock=>Object.keys(stock).length,total=stock=>Object.values(stock).reduce((a,b)=>a+b,0);

test('「帮我摆」completes 家常小铺 when two home-style kinds can reach six each',()=>{
  const s=farm({'0:0':30,'0:21':10,'0:113':20}); // 鸡宝, a second 家常, a valuable non-menu bird
  const r=suggestBusinessStock(s,'MN1');
  assert.equal(r.tier,'complete');
  assert.ok(r.stock['0:0']>=6&&r.stock['0:21']>=6);
  assert.ok(total(r.stock)<=24&&kinds(r.stock)<=6);
  const roles=assignMenuRoles('MN1',r.stock);
  assert.equal(menuFit('MN1',r.stock,roles,menuSnapshot(s,'MN1',r.stock,roles)).tier,'complete');
});

test('keep-one is honoured unless the player unticks it, and the plan is always a legal opening',()=>{
  const s=farm({'0:0':10,'0:113':1,'0:59':1});
  // Business itself is open: 72 collected and the first story order done.
  s.total['0:3']=72;s.progress.orders[RULES.storyOrders[0].id]={...(s.progress.orders[RULES.storyOrders[0].id]??{}),completed:true};
  const kept=suggestBusinessStock(s,'MN1');
  assert.deepEqual(Object.keys(kept.stock),['0:0']);assert.equal(kept.stock['0:0'],9);
  const all=suggestBusinessStock(s,'MN1',{keepOne:false});
  assert.equal(all.stock['0:113'],1);assert.equal(all.stock['0:59'],1);
  for(const [plan,overrideKeepOne] of [[kept,false],[all,true]])assert.doesNotThrow(()=>prepareBusiness(s,{menuId:'MN1',stock:plan.stock,overrideKeepOne}));
});

test('nothing to stock gives no suggestion instead of an empty plan',()=>{
  const s=farm({'0:113':1});
  assert.equal(suggestBusinessStock(s,'MN1'),null);
});

test('forecast says when it sells out and roughly what it earns',()=>{
  const s=farm({'0:0':30,'0:21':10});
  const r=suggestBusinessStock(s,'MN1'),roles=assignMenuRoles('MN1',r.stock),fit=menuFit('MN1',r.stock,roles,menuSnapshot(s,'MN1',r.stock,roles));
  const f=businessForecast(s,'MN1',r.stock,roles,fit);
  assert.equal(f.total,24);assert.equal(f.hours,8);assert.equal(f.visitors,2);assert.ok(f.income>=24*3);
});
