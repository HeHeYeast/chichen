import test from 'node:test';
import assert from 'node:assert/strict';
import {readFileSync} from 'node:fs';
import {normalizeSave,parseSave} from '../web/engine.js';
import {makeBackup,parseBackup} from '../web/save-store.js';
import {CURRENT_SAVE_VERSION} from '../web/save-migrations.js';
import {speciesDiscovered} from '../web/species-state.js';
import {shrineGoals,claimShrineGoal} from '../web/shrine.js';
import {SEASONS,seasonalChapterInfo,claimSeasonalChapter} from '../web/seasonal-pack.js';

const root=new URL('./fixtures/golden/',import.meta.url);
const read=name=>JSON.parse(readFileSync(new URL(name+'.json',root),'utf8'));
const expected=read('expected');
const NOW=expected.now;

// expected.json is a separate, static pre-migration asset snapshot. The fixture
// author explicitly records the v1 farm-to-discovery minimum and ninth tool;
// no normalizer/migration output was used to calculate these expected values.
for(const [name,want]of Object.entries(expected.cases)){
  test(`golden ${name}: migration preserves independent asset and ticket expectations`,()=>{
    const source=read(name),bytes=JSON.stringify(source);
    assert.equal(source.version,want.sourceVersion);
    const migrated=normalizeSave(source,NOW+72*3600000);
    assert.equal(migrated.version,CURRENT_SAVE_VERSION);
    assert.equal(JSON.stringify(source),bytes,'migration does not mutate its source');
    for(const [key,value]of Object.entries(want.assets))assert.deepEqual(migrated[key],value,`${name}.${key}`);
    assert.deepEqual(migrated.toolLevels,want.toolLevels);
    if(want.progress)assert.deepEqual(migrated.progress,want.progress,'legacy skills, trip, knowledge and rewards survive byte-for-value');
    if(want.cleanCycle)assert.deepEqual(migrated.cleanCycle,want.cleanCycle);
    assert.equal(migrated.meta.revision,0);
    assert.equal(migrated.meta.commandSeq,0);
    assert.equal(migrated.meta.factSeq,0);
    assert.equal(migrated.meta.lastCommit,null);
    assert.deepEqual(migrated.expansion.discovery,{cards:{},identified:{}});
    assert.deepEqual(migrated.expansion.methods,{directions:[],full:[],freeProgress:{}});
    assert.deepEqual(migrated.expansion.regions,{opened:[],introSpecimenDone:[],guideFlags:[]});
    assert.deepEqual(migrated.expansion.trial,{});
    assert.deepEqual(migrated.expansion.cardProtection,{});
    assert.equal(migrated.clock.logicalAt,Math.max(source.progress?.logicalAt??0,source.lastSeen));
    assert.equal(migrated.clock.lastWallAt,source.lastSeen);
    assert.deepEqual(normalizeSave(source,NOW+14*86400000),migrated,'migration never settles offline time');
  });

  test(`golden ${name}: current JSON and portable backup round trips are lossless`,()=>{
    const migrated=normalizeSave(read(name),NOW);
    assert.deepEqual(parseSave(JSON.stringify(migrated),NOW+86400000),migrated);
    assert.deepEqual(parseBackup(makeBackup(migrated,NOW),NOW+86400000),migrated);
    assert.deepEqual(parseBackup(makeBackup(read(name),NOW),NOW+86400000),migrated);
  });
}

test('golden legacy migration requires neither ambient time nor random sampling',()=>{
  const random=Math.random,dateNow=Date.now;
  try{
    Math.random=()=>{throw Error('migration sampled randomness');};
    Date.now=()=>{throw Error('migration read ambient clock');};
    for(const name of Object.keys(expected.cases))normalizeSave(read(name),NOW);
  }finally{Math.random=random;Date.now=dateNow;}
});

test('golden v1 gives only old minimum discovery and an unowned ninth tool',()=>{
  const source=read('early-v1'),migrated=normalizeSave(source,NOW);
  assert.equal(source.total['0:18'],undefined);
  assert.equal(source.farm['0:18'],1);
  assert.equal(migrated.total['0:18'],1);
  assert.equal(migrated.toolLevels[8],-1);
  assert.equal(migrated.batch.rules,undefined,'old unsnapshotted batch retains its original interpreter');
  assert.equal(migrated.progress.trip,null);
  assert.equal(migrated.cleanCycle.dirtyAt,source.lastClean+36*3600000);
  assert.deepEqual(migrated.progress.orders,{});
});

test('golden mid-game keeps old batch and running trip without premature income',()=>{
  const source=read('mid-v3'),migrated=normalizeSave(source,NOW+72*3600000);
  assert.equal(migrated.batch.rules.version,2);
  assert.equal(migrated.progress.trip.version,1);
  assert.equal(migrated.progress.trip.status,'running');
  assert.equal(migrated.progress.trip.cpProcessed,false);
  assert.equal(migrated.progress.trip.clueProcessed,false);
  assert.equal(migrated.batch.eggs.filter(e=>e.collected).length,0);
  assert.equal(migrated.cp,source.cp);
  assert.deepEqual(migrated.progress.trip,source.progress.trip);
});

test('golden complete193 retains discovery after all stock was sold and mixed old claims',()=>{
  const source=read('complete193-v3'),migrated=normalizeSave(source,NOW);
  assert.deepEqual(migrated.farm,{});
  assert.equal(Object.keys(migrated.total).length,193);
  for(let egg=0;egg<2;egg++)for(let id=0;id<(egg===0?128:65);id++)assert.equal(speciesDiscovered(migrated,egg,id),true,`${egg}:${id}`);
  assert.equal(speciesDiscovered(migrated,0,128),false);
  assert.equal(speciesDiscovered(migrated,1,65),false);
  const before=JSON.stringify(migrated),goals=shrineGoals(migrated);
  assert.deepEqual(goals.filter(g=>g.claimed).map(g=>g.id),['signs-3','duck-5','dim-sum-6']);
  assert.deepEqual(goals.filter(g=>g.available).map(g=>g.id),['signs-6','signs-10','signs-15','yokai-4','time-1']);
  assert.deepEqual(SEASONS.filter(c=>seasonalChapterInfo(migrated,c.id).claimed).map(c=>c.id),['spring','autumn']);
  assert.deepEqual(SEASONS.filter(c=>seasonalChapterInfo(migrated,c.id).available).map(c=>c.id),['summer','winter']);
  assert.equal(JSON.stringify(migrated),before,'reading legacy rewards remains pure');
  assert.throws(()=>claimShrineGoal(migrated,'signs-3'));
  assert.throws(()=>claimSeasonalChapter(migrated,'spring'));
  claimShrineGoal(migrated,'signs-6');claimSeasonalChapter(migrated,'summer');
  assert.equal(migrated.cp,source.cp+800+600);
  const restored=parseBackup(makeBackup(migrated,NOW),NOW+1);
  assert.throws(()=>claimShrineGoal(restored,'signs-6'));
  assert.throws(()=>claimSeasonalChapter(restored,'summer'));
  assert.equal(restored.cp,source.cp+1400);
});

test('golden rich stock keeps near-limit CP, full pack, leftovers, residues and grandfathered credits',()=>{
  const migrated=normalizeSave(read('rich-stock-v3'),NOW);
  assert.equal(migrated.cp,Number.MAX_SAFE_INTEGER-100000);
  assert.equal(Object.values(migrated.ingredients).reduce((a,b)=>a+b,0),30);
  assert.equal(Object.keys(migrated.farm).length,8);
  assert.ok(Object.values(migrated.farm).every(n=>n===99999));
  assert.deepEqual(migrated.progress.leftovers,[0,3,9,27,74]);
  assert.equal(migrated.progress.trade.credits,6,'legacy credits above unskilled cap must not be truncated');
  assert.equal(migrated.progress.trade.rebateRemainder,99);
  assert.equal(migrated.progress.trade.markupRemainder,99);
});
