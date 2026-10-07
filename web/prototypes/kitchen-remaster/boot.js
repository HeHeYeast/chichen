// Preview-owned save only; no production save reads, writes or migration.
import * as E from '/web/engine.js';
const key='chick-kitchen-remaster-lv2-v1';
if(!localStorage.getItem(key)){
 const now=Date.now(),s=E.freshState(now,2718);
 s.kitchenLevel=1;s.cp=1280;s.toolLevels[1]=s.toolLevels[2]=s.toolLevels[3]=0;
 s.progress.tutorialSeen=true;s.sound=false;s.music=false;
 s.lastClean=now-36*3600000*.18;s.cleanCycle={hours:36,dirtyAt:s.lastClean+36*3600000};
 let v=123;const visualRandom=()=>((v=(v*1664525+1013904223)>>>0)/4294967296);
 E.startBatch(s,0,now,undefined,visualRandom);
 localStorage.setItem(key,JSON.stringify(E.normalizeSave(s,now)));
}
await import('./app.js');
