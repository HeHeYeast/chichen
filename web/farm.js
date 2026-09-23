// FarmBackViewUnit.refreshCharacterDisplayViewsArray, original tall viewport.
import {availableCount} from './inventory.js';
import {GAME_DATA,EXPANSION} from './content-pack.js';
import {seasonalCharacter} from './seasonal-pack.js';
export function timeZone(now){const h=new Date(now).getHours();return h>=5&&h<8?0:h>=8&&h<17?10:h>=17&&h<19?20:30;}
const dawn=[0,20,25,70,83,84,85,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,108];
const night=[20,25,26,27,30,64,67,68,78,80,83,84,105,106,107];
const hiddenDay=[26,27,30,64,67,68,78,80,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,105,106,107];
export function visibleSpecies(egg,id,zone){
  if(seasonalCharacter(egg,id))return true;
  if(egg===0&&EXPANSION.characters.some(character=>character.id===id))return true;
  if(egg===0){
    if(id===104)return false; // Original requires a completed send-chick event.
    if(zone===0)return dawn.includes(id);
    if(zone===20&&[0,20,25,68,83,84,85].includes(id))return true;
    if(zone===30&&night.includes(id))return true;
    return !hiddenDay.includes(id);
  }
  if(zone===0)return [0,38].includes(id);
  if(zone===20&&[0,36,48,49].includes(id))return true;
  if(zone===30&&[15,28,36].includes(id))return true;
  return ![15,28,36,49].includes(id);
}
const special={'0:32':[18,115,17.4],'0:52':[267,74,null],'0:78':[267,74,null],'0:64':[660,70,null],'0:80':[450,90,null],'0:70':[520,120,null],'0:68':[500,150,12.4],'1:36':[540,150,12.4],'1:38':[550,120,null]};
export function farmDisplay(state,now,random=Math.random){
  const zone=timeZone(now),slots=Array.from({length:215},(_,i)=>i+2),display=[];
  for(const [egg,characters] of GAME_DATA.characters.entries())for(const {id} of characters){
    const key=`${egg}:${id}`;
    if(!(availableCount(state,key)>0)||!visibleSpecies(egg,id,zone))continue;
    let x,y,shadow=2.4;
    if(special[key]){[x,y,shadow]=special[key];}
    else{
      let slot;
      if(egg===0&&[37,38,39].includes(id))slot=217+id-37;
      else{if(!slots.length)continue;slot=slots.splice(Math.floor(random()*slots.length),1)[0];}
      x=slot%22*37+Math.floor(random()*9);y=187+Math.floor(slot/22)*26+Math.floor(random()*10);
      if(egg===0&&[67,106].includes(id))shadow=null;
    }
    // 52 and 78 share the original sky slot; the later record wins.
    if(key==='0:78'){const at=display.findIndex(w=>w.egg===0&&w.id===52);if(at>=0)display.splice(at,1);}
    display.push({egg,id,x,y,shadow,dir:random()<.5?-1:1,phase:0,turn:0});
  }
  return display.sort((a,b)=>a.y-b.y);
}
