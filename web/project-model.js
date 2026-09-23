// Read-only projection of the four projects for 生意 → 项目.
import {REGIONAL,CONTENT_TEXT,resolveSpecies} from './content-registry.js';
import {projectInfo,PROJECT_IDS,MAX_PORTRAITS} from './projects.js';
import {speciesDiscovered} from './species-state.js';
import {freeCount,homeCount} from './inventory.js';
import {readableRequirement} from './regular-model.js';

const STAGE_LABEL=['第一阶段','第二阶段','第三阶段'];
const known=(s,key)=>{const c=resolveSpecies(key);return !!c&&speciesDiscovered(s,c.egg,c.id);};
export function projectsModel(s,pinned=null){
  const rows=PROJECT_IDS.map(id=>{
    const info=projectInfo(s,id),text=CONTENT_TEXT[id]??{},started=!!s.expansion?.projects?.[id];
    return {...info,text:text.text,next:readableRequirement(s,text.next),result:readableRequirement(s,text.result),pinned:pinned===id,started,
      stages:info.stages.map(st=>({...st,label:STAGE_LABEL[st.index],text:readableRequirement(s,CONTENT_TEXT[st.id]?.check??'')})),
      progress:info.stages.filter(x=>x.complete).length,portraits:s.expansion?.projects?.[id]?.pinnedChoices?.portraits??[]};
  });
  // Early on, show one recommended locked project rather than every empty board.
  const open=rows.filter(r=>r.gateMet||r.started),next=rows.find(r=>!r.gateMet&&!r.started);
  return {rows,visible:next?[...open,{...next,recommended:true}]:open,cp:s.cp};
}
// Candidates for a delivery sheet: allowed, edible, with free stock at home.
export function deliveryCandidates(s,stage){
  const d=stage.delivery;if(!d)return [];
  const keys=d.locked??d.allowed;
  return keys.filter(k=>resolveSpecies(k)?.edible&&(freeCount(s,k)>0||(d.delivered[k]??0)>0)).map(key=>{const c=resolveSpecies(key);
    return {key,egg:c.egg,id:c.id,name:c.title_zh_CN,free:freeCount(s,key),home:homeCount(s,key),delivered:d.delivered[key]??0};});
}
export function portraitCandidates(s){
  const allowed=REGIONAL.projects.find(p=>p.id==='PJ-4').optionalDisplay.allowed;
  return {max:MAX_PORTRAITS,rows:allowed.filter(k=>known(s,k)).map(key=>{const c=resolveSpecies(key);return {key,egg:c.egg,id:c.id,name:c.title_zh_CN,edible:!!c.edible};})};
}
