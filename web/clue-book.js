// 线索册 = 调查中心: every partner not met yet, as an investigation.
// A row carries what the player has read (cookware, first seasoning, the second one's flavour group, other conditions),
// how far it has got (调查 x/5 — 5 only once the exact recipe is held: studied, or clues that leave nothing open), what can be
// done with it right now, and whether it is the one partner being tracked (追踪), which 下一锅「推荐」 puts first.
// status: ready — the recipe is held and can be cooked now (去制作); guess — the partner could be cooked right now and the clues
// leave only a few seasonings to try (GUESS_MAX), so the player puts the set together (去试, guessDirections);
// scout — a trip to its main clue region can read the next layer (去寻访 · 地区); wait — something has to change first (a
// holiday, a cookware level, a seasoning that cannot be had yet, a region not open); clue — nothing left to read and too
// little known to try (研读). A partner the clues have not narrowed yet is a scout first: trying it is allowed (canTry), not
// urged.
// A projection of what is already known (skills, clue facts, studied recipes), never of a hidden recipe: a wait row only
// names conditions the player can already read, and says nothing more when the hidden seasonings are what stops it.
import * as E from './engine.js';
import {RECIPE_CATALOG} from './recipe-book.js';
import {speciesDiscovered} from './species-state.js';
import {clueReach,observationInfo,speciesCode,knownRecipe,trackedKey,trackedFound,nextClueLayer} from './knowledge.js';
import {clueRegionOf,regionAccess,REGION_SHORT} from './clue-regions.js';
import {isRegionalKey,regionalRows,regionalGate,regionalHeld,regionalTrial,regionalBlockers,regionalStudy,regionOfKey} from './regional-clues.js';
import {expectedOutcome,guessDirections} from './seasoning-advisor.js';
import {ingredientUnlockInfo} from './ingredient-unlocks.js';
import {RULES} from './integration-data.js';
import {SPECIES_CLUES} from './content-registry.js';
import {rank} from './progression.js';

export const CLUE_LAYERS=Object.freeze(['剪影','厨具与日期','第一味','第二味类别','其余条件']);
export const CLUE_STEPS=5;
// A guess worth suggesting leaves at most this many seasonings to try. On the 10/06 save a known flavour group leaves 1–6
// (median 5); a known first seasoning alone leaves 35, about one batch in 35 — that is a trip's job (read the group), not a
// batch's. Trying is still allowed from the 线索册.
export const GUESS_MAX=6;
export const narrowed=g=>!!g&&!!g.group&&Array.isArray(g.candidates)&&g.candidates.length<=GUESS_MAX;
export const TRIP_LAYER_NAMES=Object.freeze({1:'剪影',2:'要用的厨具',3:'第一味',4:'第二味的类别',5:'完整做法'});
// The 调查 page shows the tracked partner, then a few of each: the ones being worked out, the ones ready to cook.
export const FOCUS_ROWS=3;
const NO_RECIPE=new Set(['change','sign','gift']);
const usable=(s,id)=>{if(s.ingredients?.[id]>0)return true;const u=ingredientUnlockInfo(s,id);return u.available&&!u.special;};

// What stops a held recipe from being cooked right now, in the order the player would fix it.
function blockers(s,r){
  // the conditions already include the kitchen level that opens enough seasoning slots
  const out=r.conditions.filter(c=>!c.met).map(c=>c.label);
  for(const id of r.ingredients){
    if(s.ingredients[id]>0)continue;const u=ingredientUnlockInfo(s,id);
    if(!u.available||u.special)out.push(`${E.label(E.ingredient(id))}：${u.special?'只能从神社小礼得到':u.reason??'还买不到'}`);
  }
  return [...new Set(out)];
}
// What keeps a partner whose recipe is not held from being tried now, limited to what the player can read: the cookware
// level and the dates or hours (cookware layer), the first seasoning when it is known, every condition once the last layer
// is read. Empty when only the hidden part stops it.
function visibleNeeds(s,c){
  const p=c.path,out=[];
  for(const x of p.conditions){
    if(x.met)continue;
    const dated=x.kind==='calendar'||x.label.startsWith('开火时段'),toolLevel=p.toolId>=0&&x.label.endsWith(`Lv.${p.minLevel+1} 起`);
    if(c.levels[4]||(c.tool&&(dated||toolLevel)))out.push(x.label);
  }
  if(c.first&&p.ingredients.length&&!usable(s,p.ingredients[0])){const id=p.ingredients[0],u=ingredientUnlockInfo(s,id);out.push(`${E.label(E.ingredient(id))}：${u.special?'只能从神社小礼得到':u.reason??'还买不到'}`);}
  if(c.group&&!(RULES.ingredientFlavorGroups[c.group]??[]).some(id=>id!==p.ingredients[0]&&usable(s,id)))out.push(`${c.group}类现在都买不到`);
  return [...new Set(out)];
}

// One partner as an investigation card. guess: its 推测方向 when one is already at hand (clueBookModel passes them in).
export function clueRow(s,key,now=Date.now(),guess){
  const [egg,id]=key.split(':').map(Number),view=s.egg===egg?s:{...s,egg};
  if(speciesDiscovered(s,egg,id))return null;
  if(isRegionalKey(key))return regionalClueRow(view,key,now);
  const c=clueReach(view,key,now);if(!c)return null;
  const info=observationInfo(view,key,now),held=knownRecipe(view,key,now);
  const studyCost=rank(s,'OBS-S')?50:100,canStudy=!!rank(s,'OBS-4');
  const base={key,egg,id,code:speciesCode(key),silhouette:!!info?.silhouette,riddle:SPECIES_CLUES[key]??'',levels:c.levels,
    tracked:trackedKey(s)===key,canStudy,studyCost,
    // the 「其余条件」 layer: whether it is read, and how many conditions are still unmet
    rest:c.levels[4]?{known:true,unmet:c.path.conditions.filter(x=>!x.met).length}:{known:false,unmet:0}};
  if(held){
    const stop=blockers(view,held);
    // The real chance this species shows up at least once in one batch made exactly this way, with what the player owns.
    const chance=stop.length?0:expectedOutcome(view,held.toolId,held.ingredients,now).kinds.get(id)??0;
    const ready=!stop.length&&chance>0;
    return {...base,held:true,region:null,scout:false,canTry:false,narrow:false,progress:CLUE_STEPS,status:ready?'ready':'wait',toolId:held.toolId,minLevel:held.minLevel,ingredients:[...held.ingredients],kind:held.kind,
      first:held.ingredients[0]??null,none:!held.ingredients.length,second:null,secondId:held.ingredients[1]??null,secondCount:0,
      studied:c.full,blockers:ready?[]:stop.length?stop:['现在做不出'],need:ready?[]:stop.length?stop:['现在做不出'],chance,ready,guess:null};
  }
  if(guess===undefined)guess=guessDirections(s,egg,now).find(g=>g.key===key)??null;
  const p=c.path,group=c.group,narrow=narrowed(guess);
  const place=clueRegionOf(key),access=place?regionAccess(s,place.region):null,trip=nextClueLayer(s,key,now);
  const scout=!!trip&&!!access?.open,status=narrow?'guess':scout?'scout':guess?'guess':c.tool?'wait':'clue';
  const need=status==='wait'||status==='clue'?visibleNeeds(view,c):[];
  // the next clue waits in a region that is not open yet
  if(trip&&place&&!access?.open)need.push(`下一条线索在${REGION_SHORT[place.region]}（还没开放）`);
  return {...base,held:false,region:place?{id:place.region,name:REGION_SHORT[place.region]}:null,scout,scoutLevel:scout?trip.level:null,canTry:!!guess,narrow,
    // never 5 before the recipe is held: the last step is always trying (or 研读)
    progress:Math.min(CLUE_STEPS-1,c.levels.filter(Boolean).length),depth:c.levels.filter(Boolean).length,
    status,
    toolId:c.tool?p.toolId:null,minLevel:c.tool?p.minLevel:null,
    first:c.first&&p.ingredients.length?p.ingredients[0]:null,none:c.first&&!p.ingredients.length,
    second:group,secondCount:group?(RULES.ingredientFlavorGroups[group]??[]).length:0,
    when:(info?.details??[]).filter(d=>/^(已满足|尚缺)：/.test(d)).map(d=>d.replace(/^(已满足|尚缺)：/,'')).slice(0,2),
    need,guess,ready:false,chance:0};
}

// A regional partner's card (loop batch 4, regional-clues.js): the same fields, read from its region's own steps.
// Before its 方向 its next step is its region's (find its specimen or story card on a trip — scout —, 辨认 the specimen it
// brought home — identify —, open the region or duck eggs — wait); after it, trips in its region read its layers up to the
// complete method; held, it is tried with the region's odds (trial: 25% a batch, sure by the fourth).
function regionalClueRow(s,key,now){
  const [egg,id]=key.split(':').map(Number),c=clueReach(s,key,now),p=c.path,gate=regionalGate(s,key),study=regionalStudy(s,key);
  const regionId=regionOfKey(key),region={id:regionId,name:REGION_SHORT[regionId]},access=regionAccess(s,regionId);
  const base={key,egg,id,code:speciesCode(key),silhouette:c.levels[0],riddle:gate.met?SPECIES_CLUES[key]??'':'',levels:c.levels,
    tracked:trackedKey(s)===key,canStudy:study.canStudy,studyCost:study.studyCost,rest:{known:false,unmet:0},regional:true,region,gate:gate.met?null:gate.step,
    // 研读 is the region's own: with the skill it still waits for the 方向 and a kitchen that could make it
    studyHidden:!study.canStudy&&!!rank(s,'OBS-4')};
  if(regionalHeld(s,key)){
    // the region's own conditions say it all (kitchen, cookware, duck eggs, the old seasonings' supply)
    const stop=[...regionalBlockers(s,key)];
    for(const sid of p.ingredients){if(s.ingredients[sid]>0)continue;const u=ingredientUnlockInfo(s,sid);if(!u.available)stop.push(`${E.label(E.ingredient(sid))}：${u.reason??'还买不到'}`);}
    const trial=regionalTrial(s,key),ready=!stop.length;
    return {...base,held:true,scout:false,canTry:false,narrow:false,progress:CLUE_STEPS,status:ready?'ready':'wait',toolId:p.toolId,minLevel:p.minLevel,ingredients:[...p.ingredients],kind:'regional',
      first:p.ingredients[0]??null,none:false,second:null,secondId:p.ingredients[1]??null,secondCount:0,studied:true,blockers:ready?[]:[...new Set(stop)],need:ready?[]:[...new Set(stop)],
      chance:ready?trial.chance:0,trial,ready,guess:null,recipeId:trial.recipeId};
  }
  const step=gate.met?null:gate.step,trip=gate.met?nextClueLayer(s,key,now):null;
  // a find the trip can make now (its region open, nothing else in the way), or the next layer
  const findTrip=!!step?.trip&&access.met&&!step.blocked.length,scout=findTrip||!!trip&&access.open;
  const identify=step?.kind==='identify'?step.materialId:null;
  const status=scout?'scout':identify!=null?'wait':c.tool?'wait':gate.met?'clue':'wait';
  const need=status==='scout'?[]:identify!=null?[step.text]:step?(step.blocked.length?step.blocked:[step.text]):trip&&!access.open?[`下一条线索在${region.name}（还没开放）`]:[];
  return {...base,held:false,scout,scoutLevel:scout?(trip?.level??0):null,scoutFind:findTrip?step:null,identify,canTry:false,narrow:false,
    progress:Math.min(CLUE_STEPS-1,c.levels.filter(Boolean).length),depth:c.levels.filter(Boolean).length,status,
    toolId:c.tool?p.toolId:null,minLevel:c.tool?p.minLevel:null,first:c.first?p.ingredients[0]:null,none:false,
    second:c.group,secondCount:c.group?(RULES.ingredientFlavorGroups[c.group]??[]).length:0,when:[],need,guess:null,ready:false,chance:0};
}

// The tracked partner's card, or null when nothing is tracked (or the tracked one has been met).
export function trackedRow(s,now=Date.now()){const key=trackedKey(s);return key?clueRow(s,key,now):null;}

const STATUS_ORDER={ready:0,guess:1,scout:2,wait:3,clue:4};
export const CLUE_FILTERS=Object.freeze([['all','全部'],['ready','能做'],['guess','能试'],['scout','去寻访'],['wait','等条件'],['clue','缺线索']]);

export function clueBookModel(s,egg,now=Date.now()){
  const seen=new Set(),rows=[];
  const studyCost=rank(s,'OBS-S')?50:100,canStudy=!!rank(s,'OBS-4');
  const guesses=new Map(guessDirections(s,egg,now).map(g=>[g.key,g]));
  for(const r of RECIPE_CATALOG){
    if(r.egg!==egg||seen.has(r.key)||NO_RECIPE.has(r.kind)||speciesDiscovered(s,r.egg,r.id))continue;
    seen.add(r.key);
    const row=clueRow(s,r.key,now,guesses.get(r.key)??null);if(row)rows.push(row);
  }
  // the regional partners, in the same list (loop batch 4)
  for(const r of regionalRows(egg)){if(speciesDiscovered(s,r.egg,r.id))continue;const row=clueRow(s,r.key,now,null);if(row)rows.push(row);}
  // the whole list: tracked first, then what can be done now, then (inside each status) what this kitchen can finish (see
  // 调查 below), the best chance or the most read first
  const kit=r=>(r.toolId==null||(s.toolLevels[r.toolId]??-1)>=r.minLevel)&&(r.first==null||ingredientUnlockInfo(s,r.first).available);
  rows.sort((a,b)=>b.tracked-a.tracked||STATUS_ORDER[a.status]-STATUS_ORDER[b.status]||kit(b)-kit(a)||b.held-a.held||b.chance-a.chance||b.progress-a.progress||(b.first!=null)-(a.first!=null)||a.id-b.id);
  const unlocked=rows.filter(r=>r.held),clues=rows.filter(r=>!r.held);
  // 调查: the investigations closest to done (those that can be tried now first), then recipes ready to cook. Among them,
  // the ones this kitchen can finish come first: the cookware it needs is owned at its level and the first seasoning can be
  // had (batch 5 playtest: the top three were often waiting on a 竹蒸笼 or a 烧水壶 the player did not have).
  const near=clues.filter(r=>!r.tracked).sort((a,b)=>(b.status==='guess')-(a.status==='guess')||(b.status==='scout')-(a.status==='scout')||kit(b)-kit(a)||b.progress-a.progress||(b.second!=null)-(a.second!=null)||(b.first!=null)-(a.first!=null)||a.id-b.id).slice(0,FOCUS_ROWS);
  const doable=unlocked.filter(r=>r.ready&&!r.tracked).sort((a,b)=>b.chance-a.chance||a.id-b.id).slice(0,FOCUS_ROWS);
  const counts=Object.fromEntries(CLUE_FILTERS.map(([id])=>[id,id==='all'?rows.length:rows.filter(r=>r.status===id).length]));
  return {egg,rows,unlocked,clues,near,doable,counts,total:rows.length,ready:counts.ready,canStudy,studyCost,
    tracked:trackedRow(s,now),trackedFound:trackedFound(s)};
}
