// Work G: the bay — deterministic river guide, a new 12h route, optional cargo
// exchange — and the full 48-species / 24-card reachability on legal states.
import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {earnedSources} from '../web/progression.js';
import {departRegional,regionalTripInfo,guideEligibility} from '../web/regional-exploration.js';
import {claimTrip,recall,explorationInfo} from '../web/exploration.js';
import {identifyMaterial,regionalRecipeInfo,regionalAlternativeInfo,prepareRegionalRecipe} from '../web/regional-methods.js';
import {trackPartner} from '../web/knowledge.js';
import {LEGACY193} from '../web/legacy-content.js';
import {execute} from '../web/game-commands.js';
import {regionInfo,REGIONAL_RELEASE} from '../web/region-model.js';
import {inventoryView} from '../web/inventory.js';
import {REGIONAL,resolveSpecies} from '../web/content-registry.js';
import {orderOptions} from '../web/orders.js';
import {menuUnlockInfo} from '../web/menu-model.js';

const NOW=1800000000000,H=3600000;
const OLD=['0:0','0:3','0:4','0:6','0:8','0:10','0:12','0:16','0:17','0:18','0:21','0:43','0:114','0:115','0:116','0:117','1:0','1:3','1:5','1:6','1:7','1:8','1:9','1:10','1:12','1:22'];
function late(){
  const s=E.freshState(NOW,99);s.kitchenLevel=2;s.duck=true;s.cp=90000;s.toolLevels=[0,1,1,1,1,1,1,-1,1];
  s.total=Object.fromEntries(OLD.map((k,i)=>[k,i?20:900]));for(let i=22;i<40;i++)s.total['0:'+i]??=1;
  s.farm=Object.fromEntries(OLD.map(k=>[k,9]));s.progress.sources=earnedSources(s);s.ingredients={};return E.normalizeSave(s,NOW);
}
function trip(s,regionId,options,{doRecall=false}={}){
  departRegional(s,{regionId,members:['0:0'],...options},s.progress.trip?.endAt??NOW);const t=s.progress.trip;
  if(doRecall){recall(s,t.id,t.startedAt+1,()=>.99);return t;}
  E.advanceWorld(s,t.endAt);claimTrip(s,t.id,{discard:true},t.endAt,()=>.99);return t;
}
function earlierRegion(s){trip(s,'V',{placeId:'V:0',focus:'specimen'});identifyMaterial(s,75);s.total['0:128']=1;s.farm['0:128']=1;}
function guided(){const s=late();earlierRegion(s);trip(s,'R',{placeId:'R:1',focus:'materials',guide:true});return s;}

test('GUIDE-B needs kitchen Lv.3, 40 discoveries and an identified-and-collected earlier region',()=>{
  const s=late();assert.equal(guideEligibility(s).met,false,'no earlier region yet');
  assert.equal(regionalTripInfo(s,{regionId:'R',placeId:'R:1',focus:'materials',members:['0:0'],guide:true},NOW).canDepart,false);
  earlierRegion(s);assert.equal(guideEligibility(s).met,true);
  const low=structuredClone(s);low.kitchenLevel=1;assert.ok(guideEligibility(low).missing.includes('厨房 Lv.3'));
  assert.equal(regionalTripInfo(s,{regionId:'R',placeId:'R:0',focus:'materials',members:['0:0']},NOW).guide.available,false,'only from the river stall');
  assert.equal(regionInfo(s,'B').met,false);assert.match(regionInfo(s,'B').missing.join(),/沿湾路标/);
});

test('GUIDE-B identifies the actual first specimen, including an executable second-material intro',()=>{
  for(const secondFirst of [false,true]){
    let now=NOW,s=E.freshState(now,99);
    s.kitchenLevel=3;s.duck=true;s.cp=2000000;s.toolLevels=s.toolLevels.map(()=>2);
    s.total=Object.fromEntries(LEGACY193.characters.flatMap((list,egg)=>list.map(c=>[`${egg}:${c.id}`,3])));s.total['0:0']=9000;
    s.farm=Object.fromEntries(Object.keys(s.total).map(k=>[k,6]));s.progress.sources=earnedSources(s);
    if(secondFirst)s.toolLevels[1]=-1;
    s=E.normalizeSave(s,now);
    function command(type,fn){s=execute({state:s,now,command:{type,at:now},reduce:draft=>{s=draft;fn();}}).state;}
    function voyage(placeId,focus){
      command('depart',()=>departRegional(s,{regionId:'V',placeId,focus,members:['0:0']},now));now=s.progress.trip.endAt;
      command('claim',()=>{E.advanceWorld(s,now);claimTrip(s,s.progress.trip.id,{discard:true},now);});
    }
    voyage('V:0','specimen');
    assert.equal(s.progress.trip.regional.intro.materialId,secondFirst?76:75);
    for(let i=0;!s.expansion.discovery.cards['V-S2']&&i<4;i++)voyage('V:1','specimen');
    command('identify',()=>identifyMaterial(s,76));
    // 线索册 (batch 4): track it, and each valley trip reads its next layer up to the complete method
    command('track',()=>trackPartner(s,'0:129'));
    for(let i=0;!s.expansion.methods.full.includes('REC-V-C2')&&i<3;i++)voyage('V:0','materials');
    assert.ok(s.expansion.methods.full.includes('REC-V-C2'),'three tracked trips write the method down');
    for(let i=0;!s.total['0:129']&&i<4;i++){
      command('cook',()=>{E.buyIngredient(s,76,1);E.buyIngredient(s,9,1);prepareRegionalRecipe(s,'REC-V-C2');if(E.kitchenCleanInfo(s,now).cost)E.clean(s,now);E.startBatch(s,4,now);});
      now=E.batchReadyAt(s.batch);
      for(const dt of [0,2100,3100])command('tick',()=>{E.resume(s,now+dt);E.updateBatch(s,now+dt);});
      now+=3200;command('collect',()=>{E.updateBatch(s,now);for(let j=0;j<24;j++)E.collect(s,j,now);});
    }
    assert.ok(s.total['0:129']>0,'new dish is obtained through actual paid cooking and collection');
    assert.equal(!!s.expansion.discovery.identified[75],!secondFirst);
    assert.equal(guideEligibility(s).met,true,'second material qualifies only when it was the actual first specimen');
    if(!secondFirst){command('identify',()=>identifyMaterial(s,75));assert.equal(guideEligibility(s).met,true);}
  }
});

test('a legacy region with missing first-specimen history needs both identifications, without guessed facts',()=>{
  const s=late();earlierRegion(s);delete s.expansion.regions.history.cardFacts;
  const before=structuredClone(s);assert.equal(guideEligibility(s).met,false);assert.deepEqual(s,before,'qualification never reconstructs or grants facts');
  // A later real specimen snapshot does not prove it was the first one.
  for(let i=0;!s.expansion.discovery.cards['V-S2']&&i<4;i++)trip(s,'V',{placeId:'V:1',focus:'specimen'});
  assert.ok(s.expansion.regions.history.cardFacts['V-S2']);assert.equal(guideEligibility(s).met,true);
  identifyMaterial(s,76);assert.equal(guideEligibility(s).met,true,'both identified is safe regardless of missing historical order');
});

test('a complete river trip grants the guide deterministically; recall grants nothing; it is not a card',()=>{
  const s=late();earlierRegion(s);const cards=Object.keys(s.expansion.discovery.cards).length;
  const recalled=trip(s,'R',{placeId:'R:1',focus:'materials',guide:true},{doRecall:true});assert.equal(recalled.regional.guide,true);
  assert.deepEqual(s.expansion.regions.guideFlags,[],'recall never grants the guide');
  const t=trip(s,'R',{placeId:'R:1',focus:'materials',guide:true});
  assert.deepEqual(s.expansion.regions.guideFlags,['GUIDE-B']);assert.equal(t.regional.result.guide,true);
  assert.equal(Object.keys(s.expansion.discovery.cards).length-cards,t.regional.result.cardId?1:0,'the guide occupies no card slot');
  assert.ok(!Object.keys(s.expansion.discovery.cards).includes('GUIDE-B'));
  assert.equal(regionInfo(s,'B').met,true);assert.equal(menuUnlockInfo(s,'MN7').met,true);
});

test('the bay route is 12h with three base units (light 9h36 / two); TRIP-1 CP follows the real base units',()=>{
  const s=guided();const normal=explorationInfo(s,'bay',['1:0'],NOW);
  assert.deepEqual([normal.hours,normal.baseUnits,normal.unlocked],[12,3,true]);assert.equal(normal.A,1,'water adaptation is the bay alias');
  s.progress.skills={'TRIP-1':1,'TRIP-5':1};const light=explorationInfo(s,'bay',['1:0'],NOW,{light:true});
  assert.deepEqual([light.hours,light.baseUnits,light.cpReward],[9.6,2,12]);assert.equal(explorationInfo(s,'bay',['1:0'],NOW).cpReward,18);
  const t=trip(s,'B',{placeId:'B:0',focus:'specimen'});assert.equal(t.endAt-t.startedAt,12*H);assert.equal(t.regional.intro?.cardId,'B-S1');
});

test('cargo reserves six extra birds (never a companion), replaces one base slot, and is consumed once on return',()=>{
  const s=guided();trip(s,'B',{placeId:'B:0',focus:'specimen'});identifyMaterial(s,81);
  s.farm['0:0']=7;s.farm['1:0']=7;const members=['1:0'];
  assert.equal(regionalTripInfo(s,{regionId:'B',placeId:'B:0',focus:'lore',members,cargo:{'1:0':6}},NOW).canDepart,false,'eligible companion plus 6 leaves nobody home by default');
  assert.equal(regionalTripInfo(s,{regionId:'B',placeId:'B:0',focus:'lore',members,cargo:{'1:0':7}},NOW).canDepart,false,'an eligible companion cannot also be cargo');
  const plain=structuredClone(s);departRegional(plain,{regionId:'B',placeId:'B:0',focus:'lore',members:['1:0']},s.progress.trip.endAt);
  const cp=s.cp,orders=structuredClone(s.expansion.facts.orderCounts),sales=structuredClone(s.expansion.facts.businessCounts);
  departRegional(s,{regionId:'B',placeId:'B:0',focus:'lore',members:['1:0'],cargo:{'0:0':3,'0:3':3}},s.progress.trip.endAt);const t=s.progress.trip;
  assert.equal(t.remaining.length,plain.progress.trip.remaining.length,'the exchange replaces a base slot, never adds one');assert.equal(t.remaining[t.cargo.baseSlot],81);
  assert.deepEqual(inventoryView(s,'0:0'),{T:7,R:3,S:0,Q:0,free:4,home:4});
  assert.deepEqual(E.normalizeSave(structuredClone(s),NOW).progress.trip.cargo,t.cargo,'saving mid-trip keeps one contract');
  E.advanceWorld(s,t.endAt);assert.equal(s.farm['0:0'],4);assert.equal(s.farm['0:3'],6);assert.equal(t.cargo.outcome,'exchanged');
  assert.ok(s.expansion.facts.predicateWitnesses['B-E1:cargoExchange']);
  assert.deepEqual(s.expansion.facts.orderCounts,orders);assert.deepEqual(s.expansion.facts.businessCounts,sales);
  claimTrip(s,t.id,{},t.endAt,()=>.99);assert.ok(s.cp-cp<=18,'no sale CP for the cargo, only the ordinary trip CP');
  E.advanceWorld(s,t.endAt+H);assert.equal(s.farm['0:0'],4,'a returned trip never consumes twice');
});

test('B-E1 cargo requires its portable and water team even without a new card roll',()=>{
  const s=guided();trip(s,'B',{placeId:'B:0',focus:'specimen'});identifyMaterial(s,81);
  for(const owned of [false,true]){
    if(owned)s.expansion.discovery.cards['B-E1']=++s.meta.factSeq;
    for(const focus of ['lore','materials']){
      const options={regionId:'B',placeId:'B:0',focus,members:['0:0'],cargo:{'0:3':6}};
      const before=structuredClone(s),info=regionalTripInfo(s,options,s.progress.trip.endAt);
      assert.equal(info.canDepart,false,'portable yard chicken has no water companion');
      assert.equal(info.cargo.available,false);
      assert.match(info.missing.join('；'),/便携.*水边/);
      assert.throws(()=>departRegional(s,options,s.progress.trip.endAt),/便携.*水边/);
      assert.deepEqual(s,before,'refused preview and departure allocate no RNG, reserve no stock and grant no fact');
      const withoutCargo=regionalTripInfo(s,{...options,cargo:null},s.progress.trip.endAt);
      assert.equal(withoutCargo.canDepart,true,'the event team gate does not block an ordinary bay trip');
    }
  }
});

test('one portable water companion can exchange before and after B-E1; complete returns consume once',()=>{
  const s=guided();trip(s,'B',{placeId:'B:0',focus:'specimen'});identifyMaterial(s,81);
  for(const owned of [false,true]){
    if(owned)s.expansion.discovery.cards['B-E1']??=++s.meta.factSeq;
    const options={regionId:'B',placeId:'B:0',focus:'materials',members:['1:0'],cargo:{'0:3':6},cargoOverrideKeepOne:true};
    s.farm['0:3']=12;
    assert.equal(regionalTripInfo(s,options,s.progress.trip.endAt).canDepart,true,'one duck fulfills both frozen team conditions');
    const t=trip(s,'B',options);
    assert.equal(t.regional.candidates.some(c=>c.cardId==='B-E1'),false,'the exchange does not need to draw the event again');
    assert.equal(t.cargo.outcome,'exchanged');assert.equal(s.farm['0:3'],6);
    const fact=structuredClone(s.expansion.facts.predicateWitnesses['B-E1:cargoExchange']);
    assert.equal(fact.sourceId,t.id);
    E.advanceWorld(s,t.endAt+1);
    assert.equal(s.farm['0:3'],6);assert.deepEqual(s.expansion.facts.predicateWitnesses['B-E1:cargoExchange'],fact);
    assert.deepEqual(E.normalizeSave(structuredClone(s),t.endAt+1).progress.trip.cargo,t.cargo);
  }
});

test('recalling a cargo trip releases every carried bird; the exchange stays available after B-E1',()=>{
  const s=guided();trip(s,'B',{placeId:'B:0',focus:'specimen'});identifyMaterial(s,81);
  departRegional(s,{regionId:'B',placeId:'B:0',focus:'lore',members:['1:0'],cargo:{'0:3':6},cargoOverrideKeepOne:true},s.progress.trip.endAt);
  recall(s,s.progress.trip.id,s.progress.trip.startedAt+1,()=>.99);assert.equal(s.farm['0:3'],9);assert.equal(s.progress.trip.cargo.outcome,'released');
  s.expansion.discovery.cards['B-E1']=++s.meta.factSeq;
  assert.equal(regionalTripInfo(s,{regionId:'B',placeId:'B:0',focus:'materials',members:['1:0'],cargo:{'0:3':6},cargoOverrideKeepOne:true},s.progress.trip.recalledAt).canDepart,true,'exchange does not need the card to be drawn again');
});

test('the cargo ticket validator rejects a wrong slot, count, reward or premature consumption',()=>{
  const s=guided();trip(s,'B',{placeId:'B:0',focus:'specimen'});identifyMaterial(s,81);
  departRegional(s,{regionId:'B',placeId:'B:0',focus:'lore',members:['1:0'],cargo:{'0:3':6}},s.progress.trip.endAt);
  for(const mutate of [c=>c.baseSlot=2,c=>c.selection['0:3']=7,c=>c.rewardMaterial=82,c=>{c.processed=true;c.outcome='exchanged';},c=>c.selection={'0:5':6}]){
    const bad=structuredClone(s);mutate(bad.progress.trip.cargo);assert.throws(()=>E.normalizeSave(bad,NOW));
  }
});

test('ALT-B swaps salt flower for salt against the old salted-duck pool; O11, the bay O06 variant and MN7 open',()=>{
  const s=guided();trip(s,'B',{placeId:'B:0',focus:'specimen'});identifyMaterial(s,81);s.expansion.discovery.cards['B-E1']=++s.meta.factSeq;
  trip(s,'B',{placeId:'B:1',focus:'materials'});
  const alt=regionalAlternativeInfo(s,'ALT-B');assert.deepEqual(alt.legacyMaterials,[27]);assert.equal(alt.recipe.target,'1:7');
  s.expansion.methods.full.push('REC-B-C1');s.total['0:146']=1;s.farm['0:146']=6;
  assert.ok(orderOptions(s,'O06',NOW).some(x=>x.region==='B'),'bay variant appears once a bay dish is makeable');
  assert.ok(orderOptions(s,'O11',NOW).length);assert.ok(REGIONAL_RELEASE.regions.includes('B'));
});

test('every one of the 48 new species has a legal recipe path once its region prerequisites are met',()=>{
  const s=guided();
  for(const region of ['V','R','T','B'])for(const m of REGIONAL.materials.filter(m=>m.region===region)){s.expansion.discovery.cards[m.specimen]??=++s.meta.factSeq;if(!s.expansion.discovery.identified[m.id])identifyMaterial(s,m.id);}
  for(const c of REGIONAL.cards.filter(c=>c.type!=='specimen'))s.expansion.discovery.cards[c.id]??=++s.meta.factSeq;
  for(const region of ['V','R','T','B']){if(!s.expansion.regions.introSpecimenDone.includes(region))s.expansion.regions.introSpecimenDone.push(region);}
  trip(s,'V',{placeId:'V:1',focus:'materials'});trip(s,'R',{placeId:'R:0',focus:'materials'});trip(s,'T',{placeId:'T:0',focus:'materials'});trip(s,'B',{placeId:'B:1',focus:'materials'});
  s.kitchenLevel=3;s.toolLevels=[0,2,2,2,2,2,2,2,2];s.progress.sources=earnedSources(s);
  assert.equal(REGIONAL.recipes.length,48);
  for(const r of REGIONAL.recipes){assert.ok(s.expansion.methods.directions.includes(r.id),r.id+' direction');s.expansion.methods.full.push(r.id);const info=regionalRecipeInfo(s,r.id);assert.equal(info.met,true,`${r.id}: ${info.missing.join(',')}`);}
  assert.equal(REGIONAL.species.filter(x=>!x.edible).length,8);
});

test('every one of the 24 discovery cards becomes an eligible candidate under some legal team and state',()=>{
  const s=guided();
  for(const m of REGIONAL.materials){s.expansion.discovery.cards[m.specimen]??=++s.meta.factSeq;if(!s.expansion.discovery.identified[m.id])identifyMaterial(s,m.id);delete s.expansion.discovery.cards[m.specimen];}
  const teams=[['0:0'],['1:0'],['0:17','0:0'],['0:18'],['0:6','1:0'],['1:0','0:0'],['0:10'],['0:0','0:10'],['0:17','1:0']];
  const found=new Set();
  for(const card of REGIONAL.cards){
    const x=structuredClone(s);for(const r of ['V','R','T','B'])if(!x.expansion.regions.introSpecimenDone.includes(r))x.expansion.regions.introSpecimenDone.push(r);
    for(const members of teams){const info=regionalTripInfo(x,{regionId:card.region,placeId:card.placeId,focus:card.focus,members:members.filter(k=>x.farm[k]>0)},NOW);if(info.candidates.some(c=>c.cardId===card.id)){found.add(card.id);break;}}
  }
  assert.deepEqual([...found].sort(),REGIONAL.cards.map(c=>c.id).sort());assert.equal(found.size,24);
});
