import {SKILL_BY_ID} from './skill-data.js';
import {skillIcon} from './skill-icons.js';

// Visuals accept settled outcomes only. They have no access to game state or saves.
export function renderSkillFeedback(element,events,summary=''){
 const unique=new Map();
 for(const event of events)if(SKILL_BY_ID[event.id]&&event.text)unique.set(event.id,event);
 const rows=[...unique.values()].slice(0,3),doc=element.ownerDocument;
 element.replaceChildren();
 for(const event of rows){
  const row=doc.createElement('div');row.className='skill-feedback-row';row.dataset.skillTrigger=event.id;
  const emblem=doc.createElement('span');emblem.className='skill-feedback-emblem';emblem.innerHTML=skillIcon(event.id);
  const copy=doc.createElement('span'),title=doc.createElement('strong'),detail=doc.createElement('span');
  title.textContent=SKILL_BY_ID[event.id].name;detail.textContent=event.text;copy.append(title,detail);row.append(emblem,copy);element.append(row);
 }
 if(summary){const note=doc.createElement('p');note.className='skill-feedback-summary';note.textContent=summary;element.append(note);}
 return rows.length;
}
