import test from 'node:test';
import {freshOrders} from '../web/orders.js';
import assert from 'node:assert/strict';
import {freshState} from '../web/engine.js';
import {freshBusiness,openBusiness,advanceBusiness,closeBusiness,BUSINESS_WINDOW_MS as WINDOW} from '../web/business.js';
import {freshFacts,reduceFacts} from '../web/facts.js';
import {validateBusinessState} from '../web/business-save.js';
import {syncProgress} from '../web/progression.js';
const NOW=1800000000000;
function fixture(){const s=freshState(NOW);s.farm={'0:0':25,'0:3':25,'0:8':25};s.total={'0:0':240,'0:3':1,'0:8':1};s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};s.expansion.business=freshBusiness();s.expansion.facts=freshFacts();s.expansion.orders=freshOrders();s.expansion.inventoryPolicy={keepOne:true,collectionLocks:[],optionalOrderReservations:{}};syncProgress(s);return s;}
const check=s=>validateBusinessState(s,m=>{throw Error(m);});
test('strict business validator accepts fresh, each window, manual and sold-out reports without mutation',()=>{
 const s=fixture();assert.equal(check(s),true);openBusiness(s,{stock:{'0:0':12,'0:3':12}},NOW);for(let i=0;i<4;i++){advanceBusiness(s,NOW+i*WINDOW);const before=JSON.stringify(s);assert.equal(check(s),true);assert.equal(JSON.stringify(s),before);}closeBusiness(s,NOW+3*WINDOW+1);assert.equal(check(s),true);openBusiness(s,{stock:{'0:8':24}},NOW+5*WINDOW);advanceBusiness(s,NOW+20*WINDOW);assert.equal(check(s),true);
});
test('strict validator accepts deferred sold-out before timeline release',()=>{const s=fixture();openBusiness(s,{stock:{'0:0':1}},NOW);advanceBusiness(s,NOW+WINDOW,{deferClose:true});assert.equal(check(s),true);});
test('strict validator accepts genuine structured regional trip facts',()=>{const s=fixture();reduceFacts(s,[{kind:'tripComplete',tripId:'trip-1',region:'V',placeId:'V-1',focus:'discover',cardId:'V-E1',members:[{key:'0:0',gather:3,discover:3,environment:'yard',traits:['grain']}]}]);assert.equal(check(s),true);});
for(const [name,mutate]of Object.entries({
 'unknown top field':s=>s.expansion.business.extra=true,
 'future business version':s=>s.expansion.business.active.rulesVersion=3,
 'unknown session id':s=>s.expansion.business.active.id='bad',
 'capacity':s=>s.expansion.business.active.capacity=72,
 'stock conservation':s=>s.expansion.business.active.stock['0:0']++,
 'T below S':s=>s.farm['0:0']=1,
 'duplicated role assignment':s=>s.expansion.business.active.roles[0].keys.push('0:0'),
 'future menu snapshot':s=>s.expansion.business.active.snapshot.menuId='MN99',
 'wrong role count':s=>s.expansion.business.active.roleSales.unknown=0,
 'window replay':s=>s.expansion.business.active.windowReports[0].entries[0].key='0:3',
 'window cash':s=>s.expansion.business.active.windowReports[0].baseCP++,
 'wrong visitor remainder':s=>s.expansion.business.visitorProgress=12,
 'unknown fact species':s=>s.expansion.facts.businessCounts['0:999']=0,
 'future fact sequence':s=>s.expansion.facts.predicateWitnesses['MN1:validService']={firstSeq:s.meta.factSeq+1,lastSeq:s.meta.factSeq+1,sourceId:'business-1'},
 'future order active':s=>s.expansion.orders.active=[{id:'order-1'}],
 'invalid inventory flag':s=>s.expansion.inventoryPolicy.keepOne=1,
 'unknown locked species':s=>s.expansion.inventoryPolicy.collectionLocks=['0:999'],
 'unknown inventory metadata':s=>s.expansion.inventoryPolicy.anything=true,
})){test(`strict validator rejects ${name}`,()=>{const s=fixture();openBusiness(s,{stock:{'0:0':24}},NOW);advanceBusiness(s,NOW+WINDOW);mutate(s);assert.throws(()=>check(s),/营业|事实/);});}
test('report cannot pay a second bonus or invent stock and cash',()=>{const s=fixture();openBusiness(s,{stock:{'0:0':24}},NOW);closeBusiness(s,NOW+3*WINDOW);for(const edit of [r=>r.income++,r=>r.remainingStock['0:0']++,r=>r.windowReports[0].entries[0].roleId='unknown',r=>r.bonusSettled=false]){const copy=structuredClone(s);edit(copy.expansion.business.lastReport);assert.throws(()=>check(copy));}});
