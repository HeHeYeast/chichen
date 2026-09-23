import {homeCount,shrinkOrderReservations} from './inventory.js';
import {effects} from './progression.js';
export function farmHP(s,now=Date.now()){return Math.max(0,100-Math.floor(Math.max(0,now-s.farmFixed)/7200000));}
// Historical checks happen at visits and reservation-release boundaries only.
// Business sale windows are not additional farm-loss checks.
export function farmLossAt(s,now,random){
  const elapsed=now-(s.farmChecked??s.farmFixed),hp=farmHP(s,now);
  if(elapsed<=86400000)return 0;
  let rate=hp<30?60-hp:hp<60?Math.ceil((60-hp)/2):0;
  if(!rate)return 0;
  if(rate>=60){if(elapsed>4*86400000)rate=100;else if(elapsed>3*86400000)rate=80;else if(elapsed>2*86400000)rate=68;}
  s.farmChecked=now;let lost=0;
  for(const k of Object.keys(s.farm)){
    const n=homeCount(s,k);
    let keep=n>=10?Math.floor((100-rate)*n/100):Array.from({length:n},()=>random()*100<100-rate?1:0).reduce((a,b)=>a+b,0);
    if(effects(s).reserve&&n>0)keep=Math.max(1,keep);
    lost+=n-keep;s.farm[k]-=n-keep;
  }
  shrinkOrderReservations(s);return lost;
}
