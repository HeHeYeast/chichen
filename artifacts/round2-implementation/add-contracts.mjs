import * as E from '../../web/engine.js';import * as P from '../../web/progression.js';import * as T from '../../web/exploration.js';import {readFileSync,writeFileSync} from 'node:fs';
const file='tests/fixtures/save-contract.json',data=JSON.parse(readFileSync(file)),now=data.now;
const s=E.freshState(now);s.cp=100000;s.kitchenLevel=3;s.toolLevels.fill(2);for(let i=0;i<80;i++){s.total['0:'+i]=100;s.farm['0:'+i]=3;}P.syncProgress(s);for(const id of ['TRIP-1','TRIP-2','TRIP-3','TRIP-5'])P.learnSkill(s,id);T.depart(s,{routeId:'water',members:['0:0'],light:true},now,()=>0);
for(const name of ['v3-round2-light-trip','v3-round2-batch-snapshot']){data.cases=data.cases.filter(c=>c.name!==name);}
data.cases.push({name:'v3-round2-light-trip',state:structuredClone(s),normalized:structuredClone(s)});
const b=E.freshState(now);b.cp=100000;b.kitchenLevel=3;b.toolLevels.fill(2);for(let i=0;i<80;i++)b.total['0:'+i]=100;for(const id of ['CUL-1','CUL-3'])P.learnSkill(b,id);b.selected=[0];E.startBatch(b,1,now,()=>0);data.cases.push({name:'v3-round2-batch-snapshot',state:b,normalized:structuredClone(b)});
writeFileSync(file,JSON.stringify(data,null,2)+'\n');
