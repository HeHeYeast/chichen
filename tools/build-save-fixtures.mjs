import {readFileSync,writeFileSync} from 'node:fs';
import {freshState,normalizeSave} from '../web/engine.js';
import {depart,claimTrip} from '../web/exploration.js';
import {advanceWorld} from '../web/world-clock.js';
const path=new URL('../tests/fixtures/save-contract.json',import.meta.url),previous=JSON.parse(readFileSync(path)),now=previous.now;
// Preserve historical schema-1/2 payloads verbatim as compatibility evidence.
const cases=previous.cases.filter(c=>c.state.version<3).map(({name,state})=>({name,state}));
const s=freshState(now);s.total={'0:0':200,'0:3':1,'0:4':1,'0:5':1,'0:6':1};s.farm={'0:0':3};s.ingredients={0:30};
depart(s,{routeId:'yard',members:['0:0']},now,()=>0);
cases.push({name:'v3-running-reservation',state:structuredClone(s)});
advanceWorld(s,s.progress.trip.endAt);claimTrip(s,s.progress.trip.id,{},s.progress.trip.endAt,()=>0);
cases.push({name:'v3-returned-full-bag',state:structuredClone(s)});
s.ingredients[0]=29;claimTrip(s,s.progress.trip.id,{},s.progress.trip.endAt,()=>0);
cases.push({name:'v3-partially-claimed-basket',state:structuredClone(s)});
for(const c of cases)c.normalized=normalizeSave(c.state,now);
writeFileSync(path,JSON.stringify({now,cases},null,2)+'\n');console.log('Historical and schema-3 transaction fixtures written');
