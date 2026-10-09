import test from 'node:test';
import assert from 'node:assert/strict';
import {START,H,DAY,createRunner,makeProfile,safeForGap,simulate,assetValue,PROFILES,SKILL_POLICIES,CADENCES} from '../tools/simulate-economy.mjs';
import {openBusiness} from '../web/business.js';
import * as E from '../web/engine.js';
import {REGIONAL} from '../web/content-registry.js';
import {sameStockChannels,failureCosts,timingPairs} from '../tools/simulate-economy-pairs.mjs';

test('absence planning protects the earliest egg, not just batch completion',()=>{
 const info={minutes:40,freshMinutes:120};
 assert.equal(safeForGap(info,2,150),false); // Old last-egg formula accepted 150.
 assert.equal(safeForGap(info,2,120),true);
 assert.equal(safeForGap(info,2,30),false);
 assert.equal(safeForGap(info,0,1440),true);
});
test('runner rejects atomically, records exact failure and does not swallow it',()=>{
 const r=createRunner(makeProfile('starter')),before=JSON.stringify(r.s);
 assert.throws(()=>r.run('invalid-purchase',d=>{d.cp=0;E.buyTool(d,1);}),/CP/);
 assert.equal(JSON.stringify(r.s),before);assert.equal(r.commands,0);assert.equal(r.failures.length,1);assert.equal(r.failures[0].type,'invalid-purchase');
});
test('timeline income is independent of the next voluntary transaction',()=>{
 const s=makeProfile('hoarder'),r=createRunner(s);r.run('business-open',d=>openBusiness(d,{menuId:'MN1',stock:{'0:0':12}},START));
 r.advance(START+2*H);const afterWindow=r.s.cp;r.run('noop',()=>{});
 assert.ok(r.ledger.timeline.income>0);assert.equal(r.ledger.noop.income,0);assert.equal(r.s.cp,afterWindow);
 assert.equal(r.s.cp,r.initialCP+Object.values(r.ledger).reduce((n,x)=>n+x.income-x.spend,0));
});
test('initial stock is explicitly valued rather than mistaken for newly earned wealth',()=>{
 const low=makeProfile('mature-low-stock'),large=makeProfile('hoarder');assert.equal(low.cp,large.cp);assert.ok(assetValue(large).farm>assetValue(low).farm*10);
 assert.equal(Object.keys(low.total).length,193);assert.equal(Object.keys(large.total).length,193);assert.equal(Object.keys(low.expansion.discovery.cards).length,0);
});
test('declared matrix is 4 starting states × 4 cadences × 4 real skill policies',()=>{
 assert.equal(PROFILES.length*Object.keys(CADENCES).length*SKILL_POLICIES.length,64);assert.deepEqual(Object.values(CADENCES).map(x=>x.length),[1,2,3,8]);assert.equal(Object.keys(makeProfile('collector').total).length,60);
 for(const id of PROFILES){const s=makeProfile(id);assert.ok(Object.entries(s.farm).every(([key,n])=>s.total[key]>=n),'initial owned stock must have actually been collected');}
});
for(const branch of SKILL_POLICIES)test(`short mature ${branch} trajectory conserves CP, uses monotonic validated commands, and learns only its declared branch`,()=>{
 const r=simulate('mature-low-stock','daily2',branch,{days:2});assert.ok(Object.values(r.checks).every(Boolean));assert.equal(r.failures.length,0);assert.equal(r.offline.hours,72);assert.equal(r.offline.from,START+2*DAY);
 assert.ok(r.trace.every((row,i)=>i===0||row.at>=r.trace[i-1].at&&row.seq===r.trace[i-1].seq+1));
 assert.equal(r.end.cp,r.initial.cp+r.cash.income-r.cash.spend);
 if(branch==='none')assert.equal(r.end.skills.length,0);else assert.ok(r.end.skills.length>=5&&r.end.skills.every(id=>id.startsWith(branch+'-')));
 assert.ok(r.end.cards>0);
});
test('planner delivers goods into staged projects instead of checking only already-ready stages',()=>{
 const initialState=makeProfile('hoarder');for(const id of ['R-S1','R-S2'])initialState.expansion.discovery.cards[id]=++initialState.meta.factSeq;for(const id of [77,78])initialState.expansion.discovery.identified[id]=++initialState.meta.factSeq;initialState.expansion.regions.introSpecimenDone.push('R');
 for(const c of [...REGIONAL.species.filter(c=>c.region==='R'&&c.egg===0).slice(0,3),REGIONAL.species.find(c=>c.region==='R'&&c.egg===1)])initialState.total[c.key]=1;
 const r=simulate('hoarder','daily3','none',{days:1,initialState});assert.ok(r.ledger['project-delivery']?.count>0);assert.ok(r.ledger['project-payment']?.spend>0);assert.ok(r.end.projectStages>=2);
});
test('all 48 authored recipes charge full first-failure inputs and do not hide a fourth-batch guarantee',()=>{
 const rows=failureCosts();assert.equal(rows.length,48);for(const row of rows){assert.equal(row.attempts.length,4);assert.deepEqual(row.attempts.map(x=>x.target),[0,0,0,0]);assert.equal(row.attempts[0].roll,null);assert.equal(row.attempts[3].roll,null);assert.equal(row.attempts[0].netCP,row.attempts[0].harvestCP+row.attempts[0].saleCP-row.attempts[0].fireCP-row.attempts[0].purchaseCP);}
});
test('same actual batch is exhausted once in every revenue channel and credits are recorded',()=>{
 const rows=sameStockChannels();assert.equal(rows.length,4);for(const row of rows){assert.deepEqual(row.produced,{'0:0':24});assert.equal(row.business.report.totalSold,24);assert.equal(row.order.completed,1);assert.ok(row.business.cp>=row.instant.cp-18);assert.ok(row.order.cp>0);}
});
test('HOME paired timing actually learns and enables its available slow-cooking skill',()=>{
 const rows=timingPairs();assert.ok(rows.every(row=>row.HOME.calm));assert.ok(rows.some(row=>row.gaps.some(g=>g.HOME&&!g.none)));assert.ok(rows.every(row=>row.HOME.minutes>row.none.minutes));
});
