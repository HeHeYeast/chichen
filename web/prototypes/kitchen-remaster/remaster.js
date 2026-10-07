import * as E from '/web/engine.js';
import {LAYOUT as L,RECT} from '/web/theme.js';
import {KITCHEN_STAGES} from '/web/kitchen-stages.js';
import {SPRITES} from '/web/art/manifest.js';
const root='/web/prototypes/kitchen-remaster/assets/';
export const remasterStage={...KITCHEN_STAGES[1],background:root+'wood-room.png'};
export function registerRemasterArt(){
 // Source dimensions and section anchors are measured from the generated plate.
 for(const [part,frame] of Object.entries({wall:[0,0,948,542],table:[0,542,948,535],base:[0,1077,948,582]})){
  SPRITES[remasterStage.background+'#'+part]={file:remasterStage.background,frame,size:[948,1659]};
 }
}
export function layoutRemaster(){
 const compact=L.extra<70;
 const stage=document.querySelector('#scene-stage');
 stage.classList.toggle('remaster-compact',compact);
 stage.style.setProperty('--shelf-actions-top',(57+L.eggOffset*.53)+'px');
 L.timerY=compact?343+L.extra:345+L.eggOffset;
 RECT.alarm.y=L.timerY-3;RECT.alarm.h=36;
 Object.assign(RECT.ingredient,{x:20,y:compact?124+L.eggOffset:L.toolY-50,w:134,h:compact?36:44});
 Object.assign(RECT.clean,{x:177,y:compact?124+L.eggOffset:L.toolY-50,w:127,h:compact?36:44});
}
export function decorateControl(b,id,state){
 if(id==='ingredient'){
  b.classList.add('remaster-care','remaster-seasoning');
  b.innerHTML=`<img src="${root}seasoning.png" alt=""><span><strong>调味</strong><small data-seasoning-status></small></span>`;
 }
 if(id==='clean'){
  b.classList.add('remaster-care','remaster-cleaning');
  b.innerHTML=`<img src="${root}cleaning.png" alt=""><span><strong>打扫</strong><small data-grime-status></small><i class="grime-track"><i></i></i></span>`;
 }
}
export function updateCare(state,now){
 const selected=document.querySelector('[data-seasoning-status]');
 if(selected){const value=state.selected.length?'已选 '+state.selected.length+' 种':'选择调味料';if(selected.textContent!==value)selected.textContent=value;}
 const dirty=document.querySelector('[data-grime-status]');
 if(dirty){const info=E.kitchenCleanInfo(state,now),value='脏污 '+info.percent+'%';if(dirty.textContent!==value){dirty.textContent=value;const button=dirty.closest('button');button.classList.toggle('is-dirty',info.dirty);button.querySelector('.grime-track>i').style.width=info.percent+'%';}}
}
