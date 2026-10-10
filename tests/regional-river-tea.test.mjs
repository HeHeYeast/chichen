import {sampleLegacyCompanions} from '../web/legacy-recipe-adapter.js';
// Work F: river (R) and tea slope (T) on the same data-driven regional chain,
// plus the orders F opens (front-region O06, tea O07-O10, O12).
import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {earnedSources} from '../web/progression.js';
import {departRegional,regionalTripInfo} from '../web/regional-exploration.js';
import {claimTrip} from '../web/exploration.js';
import {identifyMaterial,regionalRecipeInfo,regionalAlternativeInfo,prepareLocalAlternative} from '../web/regional-methods.js';
import {regionalCardChance,regionalCard,regionInfo,regionalCompanion} from '../web/region-model.js';
import {buildBatchPlan,sampleBatchPlan} from '../web/batch-plan.js';
import {originalRecipes} from '../web/recipes.js';
import {recipeStateAt} from '../web/holiday-calendar.js';
import {materialCapacity} from '../web/material-capacity.js';
import {REGIONAL,resolveSpecies} from '../web/content-registry.js';
import {orderOptions,orderMilestone,acceptProposal,skipProposal,deliverOrderGroups} from '../web/orders.js';

const NOW=1800000000000;
const OLD=['0:0','0:3','0:4','0:6','0:8','0:10','0:17','0:18','0:43','0:114','0:115','0:116','0:117','0:120','0:121','0:124','1:0','1:3','1:5','1:6','1:7','1:9','1:22','1:57','1:59'];
// Mid-game legacy fixture past the river and tea gates (kitchen Lv.2, 500+/24+).
function mid(){
  const s=E.freshState(NOW,77);s.kitchenLevel=1;s.duck=true;s.cp=50000;s.toolLevels=[0,1,1,0,0,0,-1,-1,0];
  s.total=Object.fromEntries(OLD.map((k,i)=>[k,i?20:600]));s.farm=Object.fromEntries(OLD.map(k=>[k,8]));
  s.progress.sources=earnedSources(s);s.ingredients={};return E.normalizeSave(s,NOW);
}
function trip(s,regionId,options){
  departRegional(s,{regionId,members:['0:0'],...options},s.progress.trip?.endAt??NOW);
  const t=s.progress.trip;E.advanceWorld(s,t.endAt);claimTrip(s,t.id,{discard:true},t.endAt,()=>.99);return t.regional.result;
}
const give=(s,id)=>{s.expansion.discovery.cards[id]=++s.meta.factSeq;};

test('river and tea regions open by their own gates; the bay stays closed',()=>{
  const s=mid();assert.equal(regionInfo(s,'R').met,true);assert.equal(regionInfo(s,'T').met,true);assert.equal(regionInfo(s,'B').met,false);
  const low=structuredClone(s);low.kitchenLevel=0;assert.equal(regionInfo(low,'R').met,false,'river specimen chain needs kitchen Lv.2');
});

test('first specimens: honey and white-rice supply are witnessed through the old supply rules',()=>{
  const s=mid();
  assert.equal(regionalTripInfo(s,{regionId:'R',placeId:'R:0',focus:'specimen',members:['0:0']},NOW).introCardId,'R-S1','pan Lv.2 + honey supply admits the mountain-pomelo entry');
  const noHoney=structuredClone(s);noHoney.toolLevels[1]=0;
  const info=regionalRecipeInfo(noHoney,'REC-R-C1',{entry:true});assert.ok(info.missing.some(x=>/平底锅 Lv\.2/.test(x)));
  assert.ok(regionalRecipeInfo(s,'REC-R-C2',{entry:true}).met,'boil pot Lv.2 and white rice supply admit the cress entry');
  const noRice=structuredClone(s);noRice.toolLevels[2]=0;assert.ok(regionalRecipeInfo(noRice,'REC-R-C2',{entry:true}).missing.length);
  assert.equal(trip(s,'R',{placeId:'R:1',focus:'materials'}).cardId,'R-S1');
  assert.equal(trip(s,'T',{placeId:'T:1',focus:'lore'}).cardId,'T-S1');
});

test('kettle and milk dishes stay locked until kitchen Lv.4 and their old supply, and never appear as makeable early',()=>{
  const s=mid();trip(s,'R',{placeId:'R:0',focus:'specimen'});identifyMaterial(s,77);trip(s,'T',{placeId:'T:0',focus:'specimen'});identifyMaterial(s,79);
  for(const id of ['REC-R-D1','REC-T-D1','REC-T-C4']){s.expansion.methods.full.push(id);const info=regionalRecipeInfo(s,id);assert.equal(info.met,false,id);assert.ok(info.missing.includes('厨房 Lv.4'),id);}
  assert.ok(regionalRecipeInfo(s,'REC-T-D1').missing.some(x=>/烧水壶/.test(x)),'kettle is required');
});

test('old-material species open with each region\'s first specimen; ornamentals with their lore cards',()=>{
  const s=mid();
  for(const id of ['REC-R-C5','REC-R-D5','REC-T-C5','REC-T-D5'])assert.ok(!s.expansion.methods.directions.includes(id));
  trip(s,'R',{placeId:'R:0',focus:'specimen'});
  assert.ok(s.expansion.methods.directions.includes('REC-R-C5')&&s.expansion.methods.directions.includes('REC-R-D5'));
  assert.ok(!s.expansion.methods.directions.includes('REC-T-C5'),'the other region is independent');
  identifyMaterial(s,77);
  assert.equal(regionalTripInfo(s,{regionId:'R',placeId:'R:0',focus:'lore',members:['0:0']},NOW).candidates.some(c=>c.cardId==='R-N1'),true,'any identified river material admits R-N1');
  give(s,'R-N1');trip(s,'R',{placeId:'R:1',focus:'materials'});
  assert.ok(s.expansion.methods.directions.includes('REC-R-C6'));assert.ok(!s.expansion.methods.directions.includes('REC-R-D6'),'R-D6 also needs cress identified');
  give(s,'R-S2');identifyMaterial(s,78);assert.ok(s.expansion.methods.directions.includes('REC-R-D6'));
  trip(s,'T',{placeId:'T:0',focus:'specimen'});identifyMaterial(s,79);
  assert.ok(!regionalTripInfo(s,{regionId:'T',placeId:'T:1',focus:'lore',members:['0:0']},NOW).candidates.some(c=>c.cardId==='T-N2'),'T-N2 needs osmanthus');
  give(s,'T-N1');trip(s,'T',{placeId:'T:0',focus:'materials'});assert.ok(s.expansion.methods.directions.includes('REC-T-C6'));assert.ok(!s.expansion.methods.directions.includes('REC-T-D6'));
});

test('R-S1 water and T-N2 floral chances add their bonus once, never per member',()=>{
  const water=[regionalCompanion('1:0'),regionalCompanion('1:5'),regionalCompanion('1:7')];
  const F=water.reduce((n,c)=>n+c.F,0);
  assert.equal(regionalCardChance(regionalCard('R-S1'),water),Math.min(60,25+2*F/3+5)/100);
  assert.equal(regionalCardChance(regionalCard('R-S1'),[regionalCompanion('0:0')]),(25+2*regionalCompanion('0:0').F/3)/100);
  const floral=REGIONAL.species.map(s=>s.key).concat(Object.keys(E.freshState(NOW).farm)).find(k=>resolveSpecies(k)?.traits?.includes('floral'))??'0:48';
  const team=[regionalCompanion(floral)];if(team[0].traits.includes('floral'))assert.equal(regionalCardChance(regionalCard('T-N2'),[...team,...team]),(25+2*(2*team[0].F)/3+5)/100);
});

test('ALT-T maps roasted leaf to oolong (3), keeps butter (27) and draws exactly the old tea-egg pool',()=>{
  const s=mid();trip(s,'T',{placeId:'T:0',focus:'specimen'});identifyMaterial(s,79);give(s,'T-E1');trip(s,'T',{placeId:'T:1',focus:'materials'});
  const info=regionalAlternativeInfo(s,'ALT-T');assert.deepEqual(info.legacyMaterials,[3,27]);assert.equal(info.recipe.target,'0:10','target is species 0:10, not material 10');
  s.ingredients={79:1,27:1};prepareLocalAlternative(s,'ALT-T');const plan=buildBatchPlan(s,2,NOW);
  let i=0;const seq=()=>[.2,.8,.4,.6][i++%4];const drawn=sampleBatchPlan(s,plan,NOW,seq).result;i=0;
  assert.deepEqual(drawn,sampleLegacyCompanions(s,{mode:'legacy',egg:0,toolId:2,legacyMaterials:[3,27]},NOW,seq));
});

test('material storage is unlimited while identification still controls supply',()=>{
  const s=mid();trip(s,'R',{placeId:'R:0',focus:'specimen'});identifyMaterial(s,77);assert.equal(materialCapacity(s),Infinity);
  give(s,'R-S2');identifyMaterial(s,78);trip(s,'T',{placeId:'T:0',focus:'specimen'});identifyMaterial(s,79);assert.equal(materialCapacity(s),Infinity,'three identified');
  give(s,'T-S2');identifyMaterial(s,80);assert.equal(materialCapacity(s),Infinity);s.expansion.discovery.identified={77:1,78:2,79:3,80:4};
  const other=structuredClone(s);delete other.expansion.discovery.identified[80];assert.throws(()=>E.buyIngredient(other,80,1),'osmanthus supply stays closed until identified');
});

test('O06 freezes one released region at acceptance; later releases never rewrite it',()=>{
  const s=mid();trip(s,'R',{placeId:'R:0',focus:'specimen'});identifyMaterial(s,77);s.expansion.methods.full.push('REC-R-C1');s.total['0:134']=1;s.farm['0:134']=7;
  const options=orderOptions(s,'O06',NOW);assert.deepEqual(options.map(o=>o.region),['R'],'only a region with a makeable collected dish');
  for(let n=0;n<30&&!s.expansion.orders.proposals.some(p=>p.templateId==='O06');n++){if(s.expansion.orders.proposals.length>=2)skipProposal(s,s.expansion.orders.proposals[0].id);orderMilestone(s,NOW,'trip');}
  const p=s.expansion.orders.proposals.find(x=>x.templateId==='O06');const o=acceptProposal(s,p.id,{variantId:'O06-A',region:'R'},NOW);
  assert.equal(o.region,'R');assert.ok(o.groups[0].allowed.every(k=>resolveSpecies(k).region==='R'));
  const frozen=JSON.stringify(o.groups);s.total['0:140']=1;assert.equal(JSON.stringify(s.expansion.orders.active.find(x=>x.id===o.id).groups),frozen);
  assert.ok(!orderOptions(s,'O06',NOW).some(x=>x.region==='B'),'the bay variant waits for Work G');
});

test('O10 freezes two chapters, six each; deliveries cannot cross chapters',()=>{
  // Seasonal dishes need late cookware; use a late legacy save for the chapter order.
  const s=mid();s.kitchenLevel=3;s.toolLevels=[0,2,2,2,2,2,2,2,2];for(const k of REGIONAL.selectors.season){s.total[k]=2;s.farm[k]=8;}
  const options=orderOptions(s,'O10',NOW);assert.ok(options.length);const pick=options[0];
  for(let n=0;n<30&&!s.expansion.orders.proposals.some(p=>p.templateId==='O10');n++){if(s.expansion.orders.proposals.length>=2)skipProposal(s,s.expansion.orders.proposals[0].id);orderMilestone(s,NOW,'trip');}
  const p=s.expansion.orders.proposals.find(x=>x.templateId==='O10');const o=acceptProposal(s,p.id,{variantId:pick.variantId,chapters:pick.chapters},NOW);
  assert.deepEqual(o.groups.map(g=>g.quantity),[6,6]);assert.deepEqual(o.chapters,pick.chapters);
  const [a,b]=o.groups,keyA=a.allowed.find(k=>s.farm[k]>0);
  assert.throws(()=>deliverOrderGroups(s,o.id,[{groupId:b.id,key:keyA,quantity:1}],NOW),/允许名单/);
});

test('all 24 river and tea recipes are reachable from legal prerequisites',()=>{
  const s=mid();
  trip(s,'R',{placeId:'R:0',focus:'specimen'});identifyMaterial(s,77);give(s,'R-S2');identifyMaterial(s,78);give(s,'R-N1');
  trip(s,'T',{placeId:'T:0',focus:'specimen'});identifyMaterial(s,79);give(s,'T-S2');identifyMaterial(s,80);give(s,'T-N1');give(s,'T-N2');
  trip(s,'R',{placeId:'R:1',focus:'materials'});trip(s,'T',{placeId:'T:1',focus:'materials'});
  s.kitchenLevel=3;s.toolLevels=[0,2,2,2,2,2,2,2,2];s.progress.sources=earnedSources(s);
  const ids=REGIONAL.recipes.filter(r=>/^REC-[RT]-/.test(r.id)).map(r=>r.id);assert.equal(ids.length,24);
  for(const id of ids){assert.ok(s.expansion.methods.directions.includes(id),id+' direction');s.expansion.methods.full.push(id);const info=regionalRecipeInfo(s,id);assert.equal(info.met,true,`${id}: ${info.missing.join(',')}`);}
});
