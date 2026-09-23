// Verify the delivered design artifacts, not the future runtime implementation.
import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import assert from 'node:assert/strict';
import {GAME_DATA as DATA} from '../web/content-pack.js';
const dir='docs/b-group-balance';
const read=p=>JSON.parse(fs.readFileSync(p,'utf8'));
const cfg=read(`${dir}/design-config.json`),report=read(`${dir}/simulation.json`),baseline=read(`${dir}/baseline.json`),species=read(`${dir}/exploration-species.json`);
let checks=0;function check(label,fn){fn();checks++;console.log(`PASS ${label}`);}
check('193 species, unique stable keys, full live catalog coverage',()=>{
 const actual=new Set(DATA.characters.flatMap((a,e)=>a.map(c=>`${e}:${c.id}`)));
 assert.equal(species.length,193);assert.equal(new Set(species.map(c=>c.key)).size,193);assert.deepEqual(new Set(species.map(c=>c.key)),actual);
});
check('Equal ability budget; all fortune species mechanically equal',()=>{
 assert(species.every(c=>c.gather+c.discover===cfg.exploration.attributeTotalPerSpecies));
 const profiles=species.filter(c=>c.key.startsWith('0:')&&Number(c.key.slice(2))>=89&&Number(c.key.slice(2))<=103).map(({gather,discover,environment})=>JSON.stringify({gather,discover,environment}));assert.equal(new Set(profiles).size,1);
});
check('75 ingredient flavor groups cover each real ingredient exactly once',()=>{
 const ids=Object.values(cfg.ingredientFlavorGroups).flat().sort((a,b)=>a-b);assert.deepEqual(ids,DATA.tools[2].map(c=>c.id).sort((a,b)=>a-b));
});
check('Point economy and every modeled cohort match machine configuration',()=>{
 for(const s of report.stages){const p=(s.h>=24?cfg.skillPoints.onCollected24:0)+Math.floor(s.d/cfg.skillPoints.discoveriesPerPoint)+cfg.skillPoints.perKitchenUpgrade*(s.k-1)+cfg.skillPoints.perMilestone*cfg.skillPoints.collectionMilestones.filter(t=>s.h>=t).length;assert.equal(s.points,p);}
 assert.equal(report.stages.at(-1).points,54);assert(54<5*cfg.skillCosts.baseBranchTotal);assert.equal(['A','B','C','D'].flatMap(k=>cfg.skillCosts[k]).reduce((s,x)=>s+x,0),12);assert.equal(cfg.skillCosts.specializationSlots,1);
});
check('27 cookware levels, speed cap, preservation independent of speed',()=>{
 assert.equal(report.time.length,27);for(const t of report.time){assert.equal(t.specialist,Math.max(6,t.base*.75));assert.equal(t.baseFresh,Math.max(120,t.base*2));assert.equal(t.maxFresh,t.baseFresh+90);}assert.equal(Math.min(...report.time.map(t=>t.specialist)),6);
});
check('Fortune Monte Carlo agrees with exact final-target expectation and 7-draw bound',()=>{
 assert.equal(report.trials,100000);const s=report.signs.find(s=>s.mode==='pity'&&s.collected===14);assert(Math.abs(s.next.mean-report.finalSignExact.expected)<.02);assert.equal(s.next.observedMax,7);assert.equal(report.finalSignExact.attempts.at(-1).cumulative,1);assert.equal(report.signs.length,12);
});
check('All route rewards use final 1/2/3 bases and specialist two-unit direction',()=>{
 for(const r of cfg.exploration.routes){const rows=report.expeditions.filter(e=>e.route===r.id);assert(rows.every(e=>e.base===r.baseUnits));const mean=r.pool.reduce((s,id)=>s+DATA.tools[2][id].buy_cp,0)/r.pool.length;const best=Math.max(...r.pool.map(id=>DATA.tools[2][id].buy_cp));for(const e of rows){const expected=e.build.includes('最贵')?Math.min(2,r.baseUnits)*best+(r.baseUnits-Math.min(2,r.baseUnits))*mean+e.p*mean:(r.baseUnits+e.p)*mean;assert(Math.abs(e.replacementCP-expected)<.00011);}}
});
check('Extreme teams are attainable with three different real species',()=>{
 for(const a of report.attainable)for(const mode of ['bestMaterial','bestClue']){const t=a[mode];assert.equal(new Set(t.team).size,3);const team=t.team.map(k=>species.find(s=>s.key===k));assert(team.every(Boolean));assert.equal(team.reduce((s,x)=>s+x.gather,0),t.G);assert.equal(team.reduce((s,x)=>s+x.discover,0),t.F);assert.equal(team.filter(s=>s.environment===a.route).length,t.A);}assert(report.attainable.every(a=>a.bestMaterial.p===.59&&a.bestClue.q===.49));
});
check('Economics sum correctly, story bonuses bounded, inventory conservation',()=>{
 assert.equal(report.econ.length,10);for(const e of report.econ){assert(Math.abs(e.net-(e.sale+24-e.cook-e.material))<.001);assert(Math.abs(e.purchaseRebateMax-e.material*.1)<.001);}assert.equal(cfg.storyOrders.reduce((s,o)=>s+o.extraCP,0),2120);for(const c of report.inventoryCases){assert.equal(c.home+c.reserved,c.total);assert.equal(c.keepOne,Math.max(0,c.home-1));}
});
const integrated=process.argv.includes('--integrated'),drift=[];
check(integrated?'Historical design hashes agree; runtime changes recorded':'All claimed gameplay source hashes still match this workspace',()=>{
 for(const [p,h] of Object.entries(report.hashes)){const current=crypto.createHash('sha256').update(fs.readFileSync(p)).digest('hex');if(integrated){if(current!==h)drift.push(p);}else assert.equal(current,h,`source drift: ${p}`);assert.equal(baseline.hashes[p],h);}
});
check('All local Markdown links in the design resolve; required sections and node IDs exist',()=>{
 const filename='docs/b-group-gameplay-design.md',text=fs.readFileSync(filename,'utf8');for(const m of text.matchAll(/\[[^\]]*\]\(([^)]+)\)/g)){const href=m[1].split('#')[0];if(!href||href.includes('://'))continue;assert(fs.existsSync(path.resolve(path.dirname(filename),href)),href);}for(const k of ['CUL','HOME','TRADE','OBS','TRIP'])for(const n of ['A','B','C','D','S'])assert(text.includes(`${k}-${n}`));for(let n=1;n<=12;n++)assert(text.includes(`## ${n}. `));
});
const result={status:'PASS',checks,verifiedAt:new Date().toISOString(),scope:'design-model-and-artifacts-only',integrated,changedRuntimeFiles:drift,modelSeed:report.seed,fortuneRuns:report.trials*report.signs.length,kitchenBatches:20000*report.econ.length,sourceHashCount:Object.keys(report.hashes).length};
fs.writeFileSync(integrated?'artifacts/qa/b-group-integrated-verification.json':`${dir}/verification.json`,JSON.stringify(result,null,2)+'\n');console.log(JSON.stringify(result,null,2));
