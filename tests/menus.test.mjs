// Work E: all eight menus. Compiled unlocks equal the domain gate, and every
// authored example that can open in E runs as a real session whose tier is
// re-judged from the remaining stock at each window.
import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {REGIONAL,resolveSpecies} from '../web/content-registry.js';
import {menuUnlockInfo,menuFit,assignMenuRoles} from '../web/menu-model.js';
import {evaluate} from '../web/requirements.js';
import {openBusiness,advanceBusiness,BUSINESS_WINDOW_MS as WINDOW,ACTIVE_BUSINESS_MENUS} from '../web/business.js';
import {syncProgress} from '../web/progression.js';
import {regionInfo} from '../web/region-model.js';
import {RUNTIME_REQUIREMENTS} from '../web/runtime-requirements.generated.js';

const NOW=1800000000000;
function rich(keys){
  const s=E.freshState(NOW,9);s.kitchenLevel=2;s.duck=true;s.toolLevels=[0,0,0,0,0,0,0,0,0];
  s.total={'0:0':400,'0:3':1,'0:8':1,'0:12':1,'0:16':1,'0:21':1,'1:0':1,'1:3':1};s.farm={};
  for(const key of keys){s.total[key]=(s.total[key]??0)+5;s.farm[key]=20;}
  s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};
  syncProgress(s);return s;
}

test('E opens all eight menu definitions and compiles their three conditions',()=>{
  assert.deepEqual([...ACTIVE_BUSINESS_MENUS],['MN1','MN2','MN3','MN4','MN5','MN6','MN7','MN8']);
  for(const m of REGIONAL.menus)for(const field of ['unlock','complete','validService'])assert.equal(RUNTIME_REQUIREMENTS[`${m.id}:${field}`].compiled,true,`${m.id}:${field}`);
});

test('compiled unlock requirements equal the domain gate for every menu across boundary states',()=>{
  const all=[...new Set(REGIONAL.menus.flatMap(m=>m.examples.flat().map(x=>x.key)))];
  const states=[E.freshState(NOW,1),rich([]),rich(all.filter(k=>resolveSpecies(k).pack!=='regional')),rich(all)];
  const late=rich(all);late.expansion.regions.guideFlags=['GUIDE-B'];states.push(late);
  const lowRoute=rich(all);lowRoute.total={'0:0':100};states.push(lowRoute);
  for(const s of states)for(const m of REGIONAL.menus)assert.equal(evaluate(`${m.id}:unlock`,s).met,menuUnlockInfo(s,m.id).met,`${m.id}`);
});

test('regional menus stay closed with a stated reason until their region or bay route opens',()=>{
  const s=rich(REGIONAL.menus.flatMap(m=>m.examples.flat().map(x=>x.key)));
  assert.equal(menuUnlockInfo(s,'MN5').met,regionInfo(s,'T').met,'MN5 follows the tea-slope region gate');
  const early=structuredClone(s);early.kitchenLevel=0;assert.equal(menuUnlockInfo(early,'MN5').met,false);assert.ok(menuUnlockInfo(early,'MN5').missing[0]);
  assert.equal(menuUnlockInfo(s,'MN7').met,false,'no GUIDE-B yet');
  assert.throws(()=>openBusiness(s,{menuId:'MN7',stock:{'0:19':3,'1:7':3}},NOW));
  s.expansion.regions.guideFlags=['GUIDE-B'];assert.equal(menuUnlockInfo(s,'MN7').met,true,'legacy dishes may open the bay menu at suitable tier');
});

test('every openable authored example is complete at opening and re-judged from remaining stock each window',()=>{
  let ran=0;
  for(const m of REGIONAL.menus)for(const example of m.examples){
    if(example.some(x=>resolveSpecies(x.key).pack==='regional'))continue;
    const s=rich(example.map(x=>x.key));if(m.id==='MN7')s.expansion.regions.guideFlags=['GUIDE-B'];
    if(!menuUnlockInfo(s,m.id).met)continue;
    const stock=Object.fromEntries(example.map(x=>[x.key,x.quantity]));
    const opened=openBusiness(s,{menuId:m.id,stock},NOW);assert.equal(opened.fit.tier,'complete',`${m.id} example opens complete`);
    advanceBusiness(s,NOW+12*WINDOW);
    const report=s.expansion.business.lastReport;assert.equal(report.totalSold,example.reduce((n,x)=>n+x.quantity,0));
    assert.equal(report.validMenu,true);assert.equal(report.completeMenu,true,`${m.id}: six sales from the complete first window`);
    const tiers=report.windowReports.map(w=>w.tier);assert.equal(tiers[0],'complete');
    if(tiers.length>1)assert.notEqual(tiers.at(-1),'complete',`${m.id}: the depleted last window is judged on what remains`);
    assert.ok(s.expansion.facts.predicateWitnesses[`${m.id}:completeService`]);ran++;
  }
  assert.ok(ran>=8,`ran ${ran} real example sessions`);
});

test('regional-species examples satisfy each menu rule on the frozen snapshot (opening waits for their Works)',()=>{
  for(const m of REGIONAL.menus)for(const example of m.examples.filter(ex=>ex.some(x=>resolveSpecies(x.key).pack==='regional'))){
    const stock=Object.fromEntries(example.map(x=>[x.key,x.quantity])),roles=assignMenuRoles(m.id,stock);
    const species=Object.fromEntries(example.map(x=>{const c=resolveSpecies(x.key);return [x.key,{egg:c.egg,region:c.region??null,tags:[...(c.tags??[])],season:c.season??null,regionalMaterials:[...(c.unlock?.identifiedMaterials??[])]}];}));
    const fit=menuFit(m.id,stock,roles,{menuId:m.id,roles,identified:['75','76','77','78','79','80','81','82'],species});
    assert.equal(fit.tier,'complete',`${m.id} ${example.map(x=>x.key)}`);
  }
});
