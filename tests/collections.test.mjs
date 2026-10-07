// Work H: collections, entitlements (paper/stamp/memento) and display.
import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {earnedSources} from '../web/progression.js';
import {execute} from '../web/game-commands.js';
import {collectionProgress,reconcileEntitlements,setDisplay,ownedMementos,ENTITLEMENTS,validateCollectionsState,grantEntitlementOnce} from '../web/collection-progress.js';
import {migrate5to6,CURRENT_SAVE_VERSION} from '../web/save-migrations.js';
import {openBusiness,advanceBusiness,BUSINESS_WINDOW_MS as WINDOW} from '../web/business.js';
import {reduceFacts} from '../web/facts.js';
import {REGIONAL} from '../web/content-registry.js';
import {LEGACY193} from '../web/legacy-content.js';

const NOW=1800000000000;
const all193=()=>LEGACY193.characters.flatMap((list,egg)=>list.map(c=>`${egg}:${c.id}`));
function complete193(){
  const s=E.freshState(NOW,1);s.kitchenLevel=3;s.duck=true;s.total=Object.fromEntries(all193().map(k=>[k,k==='0:0'?6000:2]));s.farm={'0:0':30,'1:0':30,'0:3':10};
  s.events.seasonalCollections={spring:true};s.progress.sources=earnedSources(s);return s;
}
const asV5=s=>{const v=structuredClone(s);v.version=5;v.meta.migrationHistory.pop();delete v.expansion.collections;delete v.expansion.regulars;delete v.expansion.projects;delete v.expansion.menus;return v;};
const run=(s,reduce=()=>{})=>execute({state:s,now:NOW,command:{type:'test'},reduce}).state;

test('schema 6 adds empty containers; migrating never grants, the next committed command reconciles once',()=>{
  assert.equal(CURRENT_SAVE_VERSION,6);
  const old=asV5(E.normalizeSave(complete193(),NOW)),migrated=E.normalizeSave(old,NOW);
  assert.equal(migrated.version,6);assert.deepEqual(migrated.expansion.collections,{version:1,entitlements:{},display:[null,null,null]});
  assert.deepEqual([migrated.expansion.regulars,migrated.expansion.projects,migrated.expansion.menus],[{},{},{presets:[]}]);
  assert.deepEqual(migrated.meta.migrationHistory.at(-1),{from:5,to:6});
  const once=run(migrated),twice=run(once);
  assert.equal(once.cp,migrated.cp,'no CP for historical pages');assert.deepEqual(once.events,migrated.events,'old seasonal/shrine claims untouched');
  assert.deepEqual(Object.keys(twice.expansion.collections.entitlements),Object.keys(once.expansion.collections.entitlements),'reconciliation is idempotent');
});

test('a 193-complete legacy save gets every theme page and M01–M08 but no practice stamp or region page',()=>{
  const s=run(E.normalizeSave(complete193(),NOW)),got=s.expansion.collections.entitlements;
  for(let i=1;i<=8;i++){assert.ok(got[`PAGE-COL-${i}`],`page ${i}`);assert.ok(got[`M0${i}`],`memento ${i}`);assert.ok(!got[`STAMP-COL-${i}`],'no invented practice');assert.ok(!got[`FULLSTAMP-COL-${i}`]);}
  for(const r of ['V','R','T','B'])assert.ok(!got[`PAGE-${r}-draft`]&&!got[`STAMP-${r}`],'no region page without identification or use');
  assert.ok(got['PAGE-SP-ALL']&&got['BORDER-SP-ALL']&&got['PAGE-SP-TABLE']&&got['PAGE-SP-SHAPE']&&got['BORDER-SP-SHAPE']);
  assert.ok(!got['PAGE-SP-LEAF'],'SP-LEAF needs a leaf specimen card');assert.ok(!got['STAMP-SP-ALL']);
  assert.deepEqual(ownedMementos(s),['M01','M02','M03','M04','M05','M06','M07','M08']);
});

test('COL-8 counts seasonal chapters only; regional guests are shown but never counted',()=>{
  const s=E.freshState(NOW,2);s.total={'0:120':1,'0:121':1,'0:122':1,'0:123':1};for(const k of REGIONAL.species.map(x=>x.key))s.total[k]=1;
  const p=collectionProgress(s,'COL-8');assert.equal(p.stages[0].met,true,'4 from two chapters');assert.equal(p.guests,48);assert.equal(p.stages[1].met,false);
  s.total={'0:120':1,'0:121':1,'0:122':1,'0:123':1,'1:57':1,'1:58':1,'1:59':1,'1:60':1};assert.equal(collectionProgress(s,'COL-8').stages[1].met,false,'8 but only spring+summer');
  s.total['0:124']=1;assert.equal(collectionProgress(s,'COL-8').stages[1].met,true);
});

test('one regional dish sold on an ordinary stall earns the region use stamp without faking a menu service',()=>{
  const s=E.freshState(NOW,3);s.total={'0:0':500,'0:128':1,'0:3':1,'0:8':1};s.farm={'0:128':3,'0:0':10};s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};s.progress.sources=earnedSources(s);
  openBusiness(s,{stock:{'0:128':1,'0:0':1},overrideKeepOne:true},NOW);advanceBusiness(s,NOW+2*WINDOW);
  reconcileEntitlements(s);const got=s.expansion.collections.entitlements;
  assert.ok(got['STAMP-V'],'a single regional sale is a region use');assert.ok(!got['STAMP-COL-1'],'two sales are not a valid MN1 service');
  assert.equal(collectionProgress(s,'COL-V').practice.sources.business,true);
});

test('SP-ALL footprints are the first complete companion trip per species; the stamp follows any complete trip',()=>{
  const s=E.freshState(NOW,4);s.total={'0:0':5,'0:3':5,'1:0':5};
  reduceFacts(s,[{kind:'tripComplete',tripId:'trip-1',region:'V',members:[{key:'0:0',gather:4,discover:2,environment:'yard',traits:['portable']},{key:'1:0',gather:3,discover:3,environment:'water',traits:['portable']}]}]);
  reduceFacts(s,[{kind:'tripComplete',tripId:'trip-2',region:'V',members:[{key:'0:0',gather:4,discover:2,environment:'yard',traits:['portable']}]}]);
  const p=collectionProgress(s,'SP-ALL');assert.equal(p.footprints,2);assert.equal(p.practice.met,true);
  assert.equal(s.expansion.facts.companionFirst['0:0'].tripId,'trip-1');
});

test('card-linked practices stay reachable on a later regional trip once the card is recorded',()=>{
  const member=(key,traits)=>({key,gather:3,discover:3,environment:'yard',traits});
  const s=E.freshState(NOW,4);s.total={'0:0':5,'0:10':5,'1:0':5};s.expansion.discovery.cards['V-E1']=1;
  reduceFacts(s,[{kind:'tripComplete',tripId:'trip-1',region:'V',placeId:'V-P1',cardId:'V-E1',members:[member('1:0',['portable'])]}]);
  assert.equal(collectionProgress(s,'SP-LEAF').practice.met,false,'the first event trip had no leaf or tea companion');
  reduceFacts(s,[{kind:'tripComplete',tripId:'trip-2',region:'V',placeId:'V-P1',cardId:null,members:[member('0:10',['tea']),member('0:0',['portable'])]}]);
  assert.equal(collectionProgress(s,'SP-LEAF').practice.met,true);assert.equal(collectionProgress(s,'COL-1').practice.sources.trip,true);
  const legacy=E.freshState(NOW,4);legacy.total={'0:10':5};legacy.expansion.discovery.cards['V-E1']=1;
  reduceFacts(legacy,[{kind:'tripComplete',tripId:'trip-1',region:'V',placeId:null,cardId:null,members:[member('0:10',['tea'])]}]);
  assert.equal(collectionProgress(legacy,'SP-LEAF').practice.met,false,'old garden routes are not the event place');
});

test('display holds up to three owned mementos; moving or clearing has no economic effect',()=>{
  const s=run(E.normalizeSave(complete193(),NOW));const cp=s.cp,farm=structuredClone(s.farm);
  setDisplay(s,0,'M01');setDisplay(s,1,'M02');setDisplay(s,2,'M03');assert.deepEqual(s.expansion.collections.display,['M01','M02','M03']);
  setDisplay(s,0,'M03');assert.deepEqual(s.expansion.collections.display,['M03','M02',null],'one memento occupies one slot');
  assert.throws(()=>setDisplay(s,1,'M09'),/还没有/);assert.throws(()=>setDisplay(s,3,'M01'));
  setDisplay(s,1,null);assert.equal(s.cp,cp);assert.deepEqual(s.farm,farm);
  assert.equal(ENTITLEMENTS.filter(e=>e.kind==='memento').length,8,'collections grant M01–M08; M09–M12 come from regulars');
  assert.equal(REGIONAL.mementos.length,12);validateCollectionsState(s,m=>{throw Error(m);});
});

test('the first purchase note is registered from the permanent completion fact, once',()=>{
  const s=E.freshState(NOW,5);reduceFacts(s,[{kind:'orderComplete',instanceId:'order-1',templateId:'O01',variantId:'O01-A',region:null,chapters:null,groupDeliveries:[]}]);
  assert.deepEqual(reconcileEntitlements(s).filter(x=>x.startsWith('NOTE')),['NOTE-O01']);assert.deepEqual(reconcileEntitlements(s),[]);
  assert.equal(grantEntitlementOnce(s,'NOTE-O01','O01'),false);
});

test('the collections validator rejects unknown results, reused sequence numbers and unowned displays',()=>{
  const s=run(E.normalizeSave(complete193(),NOW));const check=x=>validateCollectionsState(x,m=>{throw Error(m);});check(s);
  for(const mutate of [c=>c.entitlements['M13']={seq:1,source:'x'},c=>{const [a,b]=Object.keys(c.entitlements);c.entitlements[b].seq=c.entitlements[a].seq;},c=>c.display=['M11',null,null],c=>c.display=[null,null,null,null],c=>c.version=2,c=>c.extra=1]){
    const bad=structuredClone(s);mutate(bad.expansion.collections);assert.throws(()=>check(bad));
  }
});
