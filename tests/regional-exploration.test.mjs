import {materialCapacity} from '../web/material-capacity.js';
import {inventoryView} from '../web/inventory.js';
import test from 'node:test';
import assert from 'node:assert/strict';
import {freshState} from '../web/engine.js';
import {earnedSources} from '../web/progression.js';
import {depart,claimTrip,recall} from '../web/exploration.js';
import {economicRandom} from '../web/rng.js';
import {regionInfo,regionalCard,regionalCardChance,REGIONAL_RELEASE} from '../web/region-model.js';
import {regionalRecipeInfo,regionalMethodInfo,identifyMaterial,pinRegionalMethod,studyRegionalMethod,prepareRegionalRecipe,regionalMethodPlan,settleRegionalMethod,legacyRegionalMethodPlan,convertFreeProgress} from '../web/regional-methods.js';
import {trackPartner} from '../web/knowledge.js';
import {regionalLevels,regionalTripLayers} from '../web/regional-clues.js';
import {clueRow} from '../web/clue-book.js';
import {regionalTripInfo,departRegional,settleRegionalTrip,regionalCardOutcome,validateRegionalTrip} from '../web/regional-exploration.js';
import {compileRegionalCondition,regionalDerivedRules,COMPILED_REGIONS} from '../tools/content-regional-rules.mjs';
import {REGIONAL} from '../web/content-registry.js';
import {RUNTIME_REQUIREMENTS} from '../web/runtime-requirements.generated.js';

const NOW=1800000000000;
function eligible(){
  const s=freshState(NOW,123456);
  s.total={'0:0':116,'0:3':1,'0:4':1,'0:8':1,'0:18':1};
  s.farm={'0:0':3,'0:3':2,'0:4':2,'0:8':1,'0:18':1};s.toolLevels[1]=0;
  s.progress.sources=earnedSources(s);
  return s;
}
const opts={regionId:'V',placeId:'V:0',focus:'specimen',members:['0:0']};
function returnTrip(s){const t=s.progress.trip;t.status='returned';t.returnedAt=t.endAt;return settleRegionalTrip(s,t);}
function finishTrip(s){returnTrip(s);claimTrip(s,s.progress.trip.id,{discard:true},s.progress.trip.endAt,()=>.99);}
function identified(){const s=eligible();departRegional(s,opts,NOW);finishTrip(s);identifyMaterial(s,75);return s;}
// Kitchen Lv.2, duck eggs and salt supply keep REC-V-D1 (荠菜＋盐) an executable unknown
// direction; the one-ingredient entry dish REC-V-C1 is complete as soon as it is identified.
function identifiedDuck(){const s=eligible();s.kitchenLevel=1;s.duck=true;s.total['1:0']=1;s.progress.sources=earnedSources(s);departRegional(s,opts,NOW);finishTrip(s);identifyMaterial(s,75);return s;}

test('regional release follows the Work schedule (C valley, F river/tea, G bay; ALT-R released with J, learned only from PJ-2), while region query stays pure',()=>{
  const ids=(region,kind)=>['1','2'].map(n=>`${region}-${kind}${n}`),all=['V','R','T','B'];
  assert.deepEqual(JSON.parse(JSON.stringify(REGIONAL_RELEASE)),{regions:all,cards:all.flatMap(r=>[...ids(r,'S'),...ids(r,'N'),...ids(r,'E')]),materials:[75,76,77,78,79,80,81,82],
    methods:all.flatMap(r=>['C1','C2','C3','C4','C5','C6','D1','D2','D3','D4','D5','D6'].map(x=>`REC-${r}-${x}`)),alternatives:['ALT-V','ALT-R','ALT-T','ALT-B']});
  const s=eligible(),before=JSON.stringify(s);
  assert.equal(regionInfo(s,'V').met,true);
  assert.equal(regionInfo(s,'R').met,false);
  assert.deepEqual(regionInfo(s,'V').places.map(p=>p.id),['V:0','V:1']);
  assert.equal(JSON.stringify(s),before);
});

test('V gate is cumulative120/discovery5 and first specimen additionally requires pan1',()=>{
  const s=eligible();s.total['0:0']=115;assert.equal(regionInfo(s,'V').met,false);
  s.total['0:0']=120;delete s.total['0:18'];delete s.farm['0:18'];assert.equal(regionInfo(s,'V').met,false);
  s.total['0:18']=1;assert.equal(regionInfo(s,'V').met,true);
  s.toolLevels[1]=-1;
  const p=regionalTripInfo(s,opts,NOW);assert.equal(p.canDepart,true,'old material trip is still available');assert.equal(p.firstSpecimen,false);assert.deepEqual(p.candidates,[]);
  s.toolLevels[1]=0;assert.equal(regionalTripInfo(s,opts,NOW).firstSpecimen,true);
});

test('entry specimen never depends on identification or full method; recipe production does',()=>{
  const s=eligible();assert.equal(regionalRecipeInfo(s,'REC-V-C1',{entry:true}).met,true);
  const recipe=regionalRecipeInfo(s,'REC-V-C1');assert.equal(recipe.met,false);assert.equal(recipe.missing.length,2);
  assert.equal(regionalTripInfo(s,opts,NOW).firstSpecimen,true);
  assert.deepEqual(s.expansion.methods.full,[]);
});

test('empty/over-booked/reserved teams and cross-region locations cannot depart',()=>{
  const s=eligible();assert.equal(regionalTripInfo(s,{...opts,members:[]},NOW).canDepart,false);
  s.farm['0:0']=1;assert.throws(()=>departRegional(s,{...opts,members:['0:0','0:0']},NOW));
  assert.throws(()=>departRegional(s,{...opts,placeId:'R:0'},NOW));
  assert.throws(()=>departRegional(s,{...opts,focus:'anything'},NOW));
  departRegional(s,opts,NOW);assert.equal(regionalTripInfo(s,opts,NOW).canDepart,false);
});

test('intro promise covers the other place and material focus without adding a material slot',()=>{
  const s=eligible(),options={...opts,placeId:'V:1',focus:'materials'};
  const p=regionalTripInfo(s,options,NOW);assert.equal(p.firstSpecimen,true);assert.equal(p.introOverridesPlace,true);assert.equal(p.introMaterial,75);
  departRegional(s,options,NOW);const t=s.progress.trip;
  assert.equal(t.version,2);assert.equal(t.remaining[0],75);
  assert.equal(t.remaining.length,t.snapshot.baseUnits+(t.snapshot.bonus?1:0));
  assert.deepEqual(t.regional.intro,{cardId:'V-S1',materialId:75,baseSlot:0});
  assert.deepEqual(returnTrip(s),{cardId:'V-S1',methodId:null});
});

test('preview does not draw randomness and failure retry preserves all regional and old tickets',()=>{
  const a=eligible(),b=structuredClone(a),before=JSON.stringify(a),random=Math.random;
  try{Math.random=()=>{throw Error('preview drew randomness');};for(let i=0;i<7;i++)regionalTripInfo(a,opts,NOW);}finally{Math.random=random;}
  assert.equal(JSON.stringify(a),before);
  departRegional(a,opts,NOW);departRegional(b,opts,NOW);
  assert.deepEqual(a.progress.trip,b.progress.trip);
  assert.equal(a.meta.commandSeq,0,'only execute increments the transaction sequence');
});

test('regional ticket preserves the old trip material bonus, CP and clue result independently',()=>{
  const a=eligible(),b=structuredClone(a);
  departRegional(a,opts,NOW);depart(b,{routeId:'yard',members:['0:0']},NOW,economicRandom(b,'regional-legacy-trip'));
  const x=structuredClone(a.progress.trip),y=b.progress.trip;delete x.regional;x.version=1;x.remaining[0]=y.remaining[0];
  assert.deepEqual(x,y);
});

test('full bag does not drop the card or duplicate intro material; basket blocks next trip',()=>{
  const s=eligible();s.ingredients={0:materialCapacity(s)};const cp=s.cp;
  departRegional(s,opts,NOW);returnTrip(s);s.ingredients={0:materialCapacity(s)};const t=s.progress.trip,remaining=[...t.remaining];
  assert.ok(Object.hasOwn(s.expansion.discovery.cards,'V-S1'));
  claimTrip(s,t.id,{},t.endAt,()=>.99);
  assert.deepEqual(t.remaining,remaining);assert.equal(t.status,'returned');assert.equal(s.ingredients[75],undefined);assert.equal(s.cp,cp);
  assert.throws(()=>departRegional(s,opts,t.endAt));
  const before=structuredClone(s);settleRegionalTrip(s,t);assert.deepEqual(s,before);
});

test('identification is free, idempotent and grants every released direction using that material',()=>{
  const s=eligible();assert.throws(()=>identifyMaterial(s,75));assert.throws(()=>identifyMaterial(s,76));
  departRegional(s,opts,NOW);returnTrip(s);
  const cp=s.cp,ingredients=structuredClone(s.ingredients),basket=[...s.progress.trip.remaining];
  assert.deepEqual(identifyMaterial(s,75),{id:75,identified:false,directions:[]});
  assert.equal(s.cp,cp);assert.deepEqual(s.ingredients,ingredients);assert.deepEqual(s.progress.trip.remaining,basket);
  assert.deepEqual(s.expansion.methods.freeProgress.V,{count:0,targetId:null});
  assert.equal(regionalRecipeInfo(s,'REC-V-C1').met,true,'the entry dish can be prepared right after identification');
  assert.deepEqual(identifyMaterial(s,75),{id:75,identified:false,directions:[]});
});

// 线索册 (loop batch 4): the old free method became the investigation. Tracked, every trip in the partner's region reads
// its next layer — the silhouette, the second seasoning's group, then the complete method —, for free.
test('three tracked trips in its region read a regional partner up to its complete method, for free',()=>{
  const s=identifiedDuck(),cp=s.cp;trackPartner(s,'1:65');
  assert.deepEqual(regionalLevels(s,'1:65'),[false,true,true,false,false],'the 方向 is the cookware and the first seasoning');
  const progress=[];
  for(let i=0;i<3;i++){
    departRegional(s,{...opts,placeId:i%2?'V:1':'V:0',focus:i===1?'materials':'lore'},s.progress.trip.endAt+1);
    assert.equal(s.progress.trip.regional.method.targetId,null,'no free-method ticket any more');
    finishTrip(s);progress.push(clueRow(s,'1:65',s.progress.trip.endAt).progress);
    assert.equal(s.expansion.methods.full.includes('REC-V-D1'),i===2);
  }
  assert.deepEqual(progress,[3,4,5]);
  assert.equal(s.cp,cp);assert.equal(regionalRecipeInfo(s,'REC-V-D1').met,true);
});

test('a trip that left with an old free-method ticket settles it; the trips walked become clue layers once it is back',()=>{
  const s=identifiedDuck();pinRegionalMethod(s,'REC-V-D1');
  departRegional(s,opts,s.progress.trip.endAt+1);
  // the ticket an older version froze at departure
  s.progress.trip.regional.method=legacyRegionalMethodPlan(s,'V');assert.deepEqual(s.progress.trip.regional.method,{eligibleIds:['REC-V-D1'],targetId:'REC-V-D1',countBefore:0});
  assert.deepEqual(convertFreeProgress(s,regionalTripLayers),[],'waits while that trip is out');
  s.toolLevels[1]=-1;returnTrip(s);assert.equal(s.expansion.methods.freeProgress.V.count,1,'later equipment changes do not erase an eligible completed trip');
  const before=structuredClone(s);settleRegionalTrip(s,s.progress.trip);assert.deepEqual(s,before);
  claimTrip(s,s.progress.trip.id,{discard:true},s.progress.trip.endAt,()=>.99);s.toolLevels[1]=0;
  const was=regionalLevels(s,'1:65').filter(Boolean).length;
  const added=convertFreeProgress(s,regionalTripLayers);
  assert.equal(added.length,1,'one walked trip, one layer');assert.equal(regionalLevels(s,'1:65').filter(Boolean).length,was+1);
  assert.equal(s.expansion.methods.freeProgress.V.count,0);assert.equal(s.expansion.methods.full.includes('REC-V-D1'),false,'never the method itself');
  assert.deepEqual(convertFreeProgress(s,regionalTripLayers),[],'once');
});

test('two walked trips leave the complete method to the next trip, as the old third one',()=>{
  const s=identifiedDuck();s.expansion.methods.freeProgress.V={count:2,targetId:'REC-V-D1'};
  assert.equal(convertFreeProgress(s,regionalTripLayers).length,2);
  assert.deepEqual(regionalLevels(s,'1:65'),[true,true,true,true,false]);
  trackPartner(s,'1:65');departRegional(s,opts,s.progress.trip.endAt+1);finishTrip(s);
  assert.equal(s.expansion.methods.full.includes('REC-V-D1'),true);
});

test('no eligible method freezes progress and never turns completion into research tokens or CP',()=>{
  const s=identified();s.expansion.methods.freeProgress.V.count=2;s.expansion.methods.full.push('REC-V-C1');
  const plan=regionalMethodPlan(s,'V'),cp=s.cp;assert.deepEqual(plan,{eligibleIds:[],targetId:null,countBefore:2});
  assert.equal(settleRegionalMethod(s,'V',plan),null);assert.equal(s.expansion.methods.freeProgress.V.count,2);assert.equal(s.cp,cp);
});

test('a method learned while away consumes no free progress and grants no replacement value',()=>{
  const s=identified();s.expansion.methods.freeProgress.V.count=2;departRegional(s,opts,s.progress.trip.endAt+1);
  s.expansion.methods.full.push('REC-V-C1');const cp=s.cp;
  assert.equal(returnTrip(s).methodId,null);assert.equal(s.expansion.methods.freeProgress.V.count,2);assert.equal(s.cp,cp);
});

test('recall neither registers new discoveries nor advances free method and trip history',()=>{
  const s=identified();const before=structuredClone(s.expansion);departRegional(s,opts,s.progress.trip.endAt+1);
  recall(s,s.progress.trip.id,s.progress.trip.startedAt+1,()=>.99);assert.equal(settleRegionalTrip(s,s.progress.trip),null);
  assert.deepEqual(s.expansion,before);assert.equal(s.progress.trip.regional.processed,false);
});

test('card chance is 25% + 2% team F, capped at60%; exact probability boundary fails',()=>{
  const card=regionalCard('V-S1');assert.equal(regionalCardChance(card,[{F:3}]),.27);assert.equal(regionalCardChance(card,[{F:12},{F:12},{F:12}]),.49);assert.equal(regionalCardChance(card,[{F:60}]),.6);
  const s=eligible(),ticket={intro:null,failedBefore:0,candidates:[{cardId:'V-S1',chance:.27,roll:0}]};
  assert.equal(regionalCardOutcome(s,ticket).cardId,'V-S1');
  ticket.candidates[0].roll=.27-Number.EPSILON;assert.equal(regionalCardOutcome(s,ticket).cardId,'V-S1');
  ticket.candidates[0].roll=.27;assert.equal(regionalCardOutcome(s,ticket).cardId,null);
  ticket.candidates[0].roll=1-Number.EPSILON;assert.equal(regionalCardOutcome(s,ticket).cardId,null);
  ticket.failedBefore=3;assert.equal(regionalCardOutcome(s,ticket).cardId,'V-S1');
});

test('card protection increments complete eligible misses, survives place changes, freezes empty pool',()=>{
  // Math boundary fixture isolates the non-intro branch, which is shared by
  // later cards. It does not claim a legitimate B save can lose its first card.
  const s=eligible();s.expansion.regions.introSpecimenDone=['V'];
  for(let i=0;i<4;i++){
    departRegional(s,opts,NOW+i*3*3600000);s.progress.trip.regional.candidates[0].roll=.999;
    returnTrip(s);assert.equal(s.expansion.cardProtection.V.specimen,i===3?0:i+1);s.progress.trip.status='settled';s.progress.trip.remaining=[];s.progress.trip.clueProcessed=true;
    if(i<3){const before=s.expansion.cardProtection.V.specimen;departRegional(s,{...opts,placeId:'V:1'},NOW+i*3*3600000+1);returnTrip(s);assert.equal(s.expansion.cardProtection.V.specimen,before);s.progress.trip.status='settled';s.progress.trip.remaining=[];s.progress.trip.clueProcessed=true;}
  }
  assert.ok(Object.hasOwn(s.expansion.discovery.cards,'V-S1'));
});

test('already registered card leaves the saved failure count unchanged without CP compensation',()=>{
  const s=eligible();departRegional(s,opts,NOW);s.expansion.discovery.cards['V-S1']=17;s.expansion.cardProtection.V={specimen:2,lore:1};const cp=s.cp;
  assert.equal(returnTrip(s).cardId,null);assert.equal(s.expansion.cardProtection.V.specimen,2);assert.equal(s.cp,cp);
});

test('bounded history records first complete companion trips and no fabricated old facts',()=>{
  const s=eligible();assert.equal(s.expansion.regions.history,undefined);
  departRegional(s,{...opts,members:['0:0','0:3']},NOW);assert.equal(s.expansion.regions.history,undefined);finishTrip(s);
  const first=s.expansion.regions.history.companionFirst['0:0'];assert.ok(first>0);assert.deepEqual(s.expansion.regions.history.trips.V,{count:1,lastMembers:['0:0','0:3'],regionalWithLegacy:false,twoSeasonChapters:false});
  assert.deepEqual(s.expansion.regions.history.companionFacts['0:0'],{seq:first,tripId:s.progress.trip.id,region:'V',gather:10,discover:2,environment:'yard',traits:['portable']});
  assert.deepEqual(s.expansion.regions.history.cardFacts['V-S1'],{seq:s.expansion.discovery.cards['V-S1'],tripId:s.progress.trip.id,members:s.progress.trip.regional.companions});
  departRegional(s,opts,s.progress.trip.endAt+1);finishTrip(s);assert.equal(s.expansion.regions.history.companionFirst['0:0'],first);assert.equal(s.expansion.regions.history.trips.V.count,2);
  assert.equal(s.expansion.regions.history.companionFacts['0:0'].tripId,'trip-1');
  assert.equal(s.expansion.regions.history.cardFacts['V-S1'].tripId,'trip-1');
});

test('new bounded snapshots do not invent evidence for an older first-trip marker',()=>{
  const s=eligible();s.meta.factSeq=10;s.expansion.regions.history={companionFirst:{'0:0':1},trips:{V:{count:1,lastMembers:['0:0'],regionalWithLegacy:false,twoSeasonChapters:false}}};
  departRegional(s,{...opts,members:['0:0','0:3']},NOW);finishTrip(s);
  assert.equal(s.expansion.regions.history.companionFacts['0:0'],undefined);
  assert.equal(s.expansion.regions.history.companionFirst['0:0'],1);
  assert.ok(s.expansion.regions.history.companionFacts['0:3']);
  assert.equal(s.expansion.regions.history.cardFacts['V-S1'].members.length,2);
});

test('regional study follows existing OBS-4 and100/50CP gates, and is idempotent',()=>{
  const s=identifiedDuck();assert.equal(regionalMethodInfo(s,'REC-V-D1').canStudy,false);assert.throws(()=>studyRegionalMethod(s,'REC-V-D1',NOW));
  s.progress.skills['OBS-4']=1;assert.equal(regionalMethodInfo(s,'REC-V-D1').studyCost,100);s.progress.skills['OBS-S']=1;assert.equal(regionalMethodInfo(s,'REC-V-D1').studyCost,50);
  s.cp=49;assert.throws(()=>studyRegionalMethod(s,'REC-V-D1',NOW));assert.equal(s.cp,49);s.cp=50;assert.equal(studyRegionalMethod(s,'REC-V-D1',NOW).cost,50);assert.equal(s.cp,0);assert.equal(studyRegionalMethod(s,'REC-V-D1',NOW).cost,0);
  assert.equal(regionalMethodInfo(s,'REC-V-C1').studyCost,0,'nothing to study for the entry dish');
  assert.notEqual(s.expansion.methods.freeProgress.V.targetId,'REC-V-D1','studying the pinned method moves the free-completion pin on');
});

test('preparation checks known method and actual ingredients then sets the single explicit mode',()=>{
  const s=identifiedDuck();s.ingredients[75]=1;s.ingredients[27]=1;assert.throws(()=>prepareRegionalRecipe(s,'REC-V-D1'),/补全/);
  s.expansion.methods.full.push('REC-V-D1');s.ingredients[75]=0;assert.throws(()=>prepareRegionalRecipe(s,'REC-V-D1'));
  s.ingredients[75]=1;s.events.seasonalRecipe='0:120';s.progress.replicate='0:0';prepareRegionalRecipe(s,'REC-V-D1');
  assert.deepEqual(s.expansion.prepareMode,{kind:'regional',recipeId:'REC-V-D1'});assert.deepEqual([...s.selected].sort((a,b)=>a-b),[27,75]);assert.equal(s.egg,1);assert.equal(s.ingredients[75],1);assert.equal(s.events.seasonalRecipe,undefined);assert.equal(s.progress.replicate,undefined);
});

test('trip2 validator rejects corrupt/random/future/ticket-order/early-result data',()=>{
  const s=eligible();departRegional(s,opts,NOW);const good=s.progress.trip;
  const validate=t=>validateRegionalTrip(t,label=>{throw Error(label);});validate(good);
  for(const mutate of [
    t=>t.regional.rulesVersion=99,t=>t.regional.rngAlgorithm='random',t=>t.regional.candidates[0].roll=NaN,t=>t.regional.candidates[0].roll=1,
    t=>t.regional.candidates[0].chance=.61,t=>t.regional.failedBefore=4,t=>t.regional.placeId='R:0',t=>t.regional.intro.materialId=76,
    t=>t.regional.method.targetId='REC-V-C1',t=>t.regional.companions[0].F=4,t=>t.remaining[0]=0,t=>t.regional.result={cardId:'V-S1',methodId:null},
    t=>{t.regional.processed=true;t.regional.result={cardId:'V-S1',methodId:null};},
  ]){const candidate=structuredClone(good);mutate(candidate);assert.throws(()=>validate(candidate));}
  returnTrip(s);validate(s.progress.trip);
});

test('recalled trip2 remains valid with no regional processing; returned trip requires atomic settlement',()=>{
  const s=eligible();departRegional(s,opts,NOW);
  recall(s,s.progress.trip.id,NOW+1,()=>.99);
  const validate=t=>validateRegionalTrip(t,label=>{throw Error(label);});
  validate(s.progress.trip);assert.equal(s.progress.trip.regional.processed,false);assert.equal(s.progress.trip.regional.result,null);
  const invalid=structuredClone(s.progress.trip);invalid.status='returned';invalid.returnedAt=invalid.endAt;
  assert.throws(()=>validate(invalid),/归队地区未处理/);
});

test('saved ticket identity validation does not depend on currently enabled release IDs',()=>{
  const s=eligible();departRegional(s,opts,NOW);
  const later=structuredClone(s.progress.trip);
  later.regional.placeId='V:1';later.regional.intro={cardId:'V-S2',materialId:76,baseSlot:0};later.regional.candidates[0].cardId='V-S2';later.remaining[0]=76;
  later.regional.method={eligibleIds:['REC-V-C2'],targetId:'REC-V-C2',countBefore:0};
  validateRegionalTrip(later,label=>{throw Error(label);});
  assert.ok(!validateRegionalTrip.toString().includes('REGIONAL_RELEASE'),'rollback may close a new operation without corrupting its saved identity');
});

test('regional compiler binds every valley condition to an implemented domain export or an evaluable requirement tree',async()=>{
  assert.deepEqual(COMPILED_REGIONS,['V','R','T','B']);
  const regional=r=>!/^(productionRules|O\d\d)[:]/.test(r.id);
  const valley=Object.values(RUNTIME_REQUIREMENTS).filter(r=>r.work==='C'&&r.kind!=='unavailable'&&regional(r));
  assert.equal(valley.length,44+14,'44 authored V conditions plus 12 recipe gates and 2 supply rules');
  const riverTea=Object.values(RUNTIME_REQUIREMENTS).filter(r=>r.work==='F'&&r.kind!=='unavailable'&&regional(r));
  assert.equal(riverTea.length,2*(1+24+2+4+12)+2+2*14,'R/T authored conditions (incl. ALT-R/ALT-T guarantees) plus recipe gates and supply');
  for(const rule of valley){
    if(rule.kind==='compiled'){const module=await import('../'+rule.runtime.module);assert.equal(typeof module[rule.runtime.export],'function',rule.id+': implemented domain binding');}
    else{assert.equal(rule.compiled,true);assert.ok(['all','any'].includes(rule.kind),rule.id);}
  }
  for(const card of REGIONAL.cards.filter(c=>c.region==='V'))assert.notEqual(RUNTIME_REQUIREMENTS[card.gateRequirement].kind,'unavailable',card.id);
  const bay=Object.values(RUNTIME_REQUIREMENTS).filter(r=>r.work==='G'&&r.kind!=='unavailable'&&regional(r));
  assert.equal(bay.length,(1+24+2+4+12)+1+4+14,'bay authored conditions, ALT-B, the four GUIDE-B conditions and derived rules');
  assert.equal(RUNTIME_REQUIREMENTS['GUIDE-B:gate'].kind,'compiled');
  assert.equal(compileRegionalCondition({id:'V-C1:regularRelation'},REGIONAL),null,'do not claim future regular behavior is implemented');
  assert.equal(compileRegionalCondition({id:'productionRules:timing'},REGIONAL),null,'whole-package business timing remains a later Work');
  assert.equal(Object.keys(regionalDerivedRules(REGIONAL)).length,56);
  assert.equal(regionInfo(eligible(),'V').requirementId,'V:gate');
  assert.equal(regionalRecipeInfo(eligible(),'REC-V-C1').requirementId,'REC-V-C1:gate');
});

test('one kind may be brought two or three times when enough are at home; each place leaves the farm',()=>{
  const s=eligible();s.farm['0:0']=3;
  const info=regionalTripInfo(s,{...opts,members:['0:0','0:0','0:0']},NOW);assert.equal(info.canDepart,true);
  departRegional(s,{...opts,members:['0:0','0:0','0:0']},NOW);
  assert.deepEqual(s.progress.trip.members,['0:0','0:0','0:0']);
  assert.equal(inventoryView(s,'0:0').R,3);assert.equal(inventoryView(s,'0:0').free,0);
});
