// Seasoning advice for one cookware, built only from what the player already knows:
// collected or studied recipes, plus clue directions. Expected outcomes use the same
// pools as a real batch: original pools draw 24 without replacement (hypergeometric
// means), the steamer keeps one per matched recipe and draws the rest by rate.
// Read-only: nothing here changes the save.
import * as E from './engine.js';
import {originalRecipePlan} from './recipes.js';
import {recipeStateAt} from './holiday-calendar.js';
import {expansionMatches} from './legacy-content.js';
import {RECIPE_CATALOG,recipeId,recipePathInfo,recipePaths} from './recipe-book.js';
import {holidayForCharacter} from './holiday-calendar.js';
import {observationInfo,hasFact,speciesCode,knownRecipe,clueReach} from './knowledge.js';
import {RULES} from './integration-data.js';
import {seasonalCandidates} from './seasonal-pack.js';
import {speciesDiscovered} from './species-state.js';
import {ingredientUnlockInfo} from './ingredient-unlocks.js';
import {freeCount,homeCount,lockedCount} from './inventory.js';
import {PROJECT_IDS,projectInfo} from './projects.js';
import {proposalGroups} from './orders.js';
import {REGIONAL} from './content-registry.js';
import {selectorLabel} from './order-model.js';
import {CONTENT_TEXT,SPECIES_CLUES} from './content-registry.js';
import {rank} from './progression.js';

// Any real chance of a new kind is shown (as a percentage when it is not a sure thing); only float noise is dropped.
export const NEW_MIN=.01;
export const ADVICE_LENSES=Object.freeze([['income','收益'],['tasks','任务'],['new','新伙伴']]);
// Recipes cooked as an ordinary batch: original pools, the steamer, and the 四时 handmade ones (an exact match surprises).
const KINDS=new Set(['pool','dim-sum','seasonal']);
const sum=o=>Object.values(o??{}).reduce((a,b)=>a+b,0);
const setKey=ids=>[...ids].sort((a,b)=>a-b).join('+');

// A species can only count as something new to find now if one of its recipes has every condition met (kitchen and cookware
// level, the shrine letter, the holiday window, the time of day, ...); the seasonings it needs may still be bought.
export function reachableNow(s,egg,id,now=Date.now()){
  const paths=recipePaths(`${egg}:${id}`);if(!paths.length)return true;
  return paths.some(p=>recipePathInfo({...s,egg},p,now).conditions.every(c=>c.met));
}

// Holiday-only partners that can be cooked right now (window open, shrine letter done) and have not been met yet.
// Only the holiday and a count: the partners themselves stay unnamed until found.
export function holidayLimited(s,egg,now=Date.now()){
  const byHoliday=new Map();
  for(const r of RECIPE_CATALOG){
    if(r.egg!==egg||speciesDiscovered(s,r.egg,r.id))continue;
    const h=holidayForCharacter(r.egg,r.id,now);if(!h?.active)continue;
    if(!recipePathInfo({...s,egg},r,now).conditions.every(c=>c.met))continue;
    const cur=byHoliday.get(h.title)??{title:h.title,range:h.dateRange,keys:new Set()};cur.keys.add(r.key);byHoliday.set(h.title,cur);
  }
  return [...byHoliday.values()].map(h=>({title:h.title,range:h.range,count:h.keys.size}));
}

// Expected birds per species for one seasoning set (assumes a clean kitchen and timely collection).
// kinds: the chance each species shows up at least once in the batch (what 「新伙伴」 counts, not birds).
// hideUnknown: leave out species the player has not discovered, as if the pool did not hold them — used for sets the
// player has no clue for, so the numbers cannot give a recipe away.
export function expectedOutcome(s,toolId,ingredients,now=Date.now(),{hideUnknown=false}={}){
  const egg=s.egg,out=new Map(),kinds=new Map(),add=(id,n)=>out.set(id,(out.get(id)??0)+n),seen=id=>speciesDiscovered(s,egg,id);
  if(toolId===8){
    let matches=expansionMatches(s,egg,toolId,ingredients)??[];if(hideUnknown)matches=matches.filter(c=>seen(c.id));
    const kept=matches.filter(c=>c.ingredients.length),rate=matches.reduce((n,c)=>n+c.rate,0);
    for(const c of kept)add(c.id,1);
    if(rate)for(const c of matches)add(c.id,(24-kept.length)*c.rate/rate);
    for(const c of matches)kinds.set(c.id,c.ingredients.length?1:rate?1-(1-c.rate/rate)**(24-kept.length):0);
    return {counts:out,kinds,possible:[]};
  }
  const plan=originalRecipePlan(recipeStateAt(s,now),egg,toolId,ingredients,now),pool=hideUnknown?plan.pool.filter(seen):plan.pool,n=pool.length,draws=Math.min(24,n);
  const weight=new Map();for(const id of pool)weight.set(id,(weight.get(id)??0)+1);
  for(const id of pool)add(id,draws/n);
  for(const [id,w] of weight){let none=1;if(n-w<draws)none=0;else for(let i=0;i<draws;i++)none*=(n-w-i)/(n-i);kinds.set(id,1-none);}
  // A 四时 partner not met yet: an exact two-seasoning match puts it on egg 0 in a quarter of batches (seasonalSurprise),
  // shared evenly when several match; that egg is taken from the ordinary draw.
  const surprise=hideUnknown?[]:seasonalCandidates(s,toolId,ingredients);
  if(surprise.length){for(const [id,v] of out)out.set(id,v*(1-.25/24));for(const c of surprise){add(c.id,.25/surprise.length);kinds.set(c.id,.25/surprise.length);}}
  return {counts:out,kinds,possible:[...new Set(plan.gifts.map(g=>g.id))]};
}

// Birds still wanted by active orders, order proposals (as they would be accepted today) and the current project stages,
// after free stock. kind: order | proposal | project; what: the group's label (小点, 家常, …) when it has one; quantity: the
// group's full size, so "nearly done" can be told from "just started".
// What an order can still take from home counts only the partners the player can spare: free, and above the number the
// warehouse keeps at home (the same rule one-step delivery uses, order-delivery.js spareFor).
export function openDemands(s,now=Date.now()){
  const rows=[],free=keys=>keys.reduce((n,k)=>n+Math.max(0,Math.min(freeCount(s,k),homeCount(s,k)-lockedCount(s,k))),0);
  const groupWhat=(templateId,g)=>{const def=REGIONAL.orders.find(o=>o.id===templateId),src=def?.groups.find(x=>x.id===(g.sourceGroupId??g.id));return src?.selector?selectorLabel(src.selector):'';};
  for(const a of s.expansion?.orders?.active??[]){
    if(a.kind==='display')continue;
    for(const g of a.groups){
      const held=g.allowed.reduce((n,k)=>n+(a.reserved?.[k]??0),0),short=g.quantity-sum(g.delivered)-held-free(g.allowed);
      if(short>0)rows.push({kind:'order',id:a.id,name:CONTENT_TEXT[a.templateId]?.name??a.templateId,label:`订单「${CONTENT_TEXT[a.templateId]?.name??a.templateId}」`,what:groupWhat(a.templateId,g),quantity:g.quantity,allowed:new Set(g.allowed),short});
    }
  }
  for(const p of s.expansion?.orders?.proposals??[]){
    const def=REGIONAL.orders.find(o=>o.id===p.templateId);if(!def||def.kind==='display')continue;
    let groups=null;try{groups=proposalGroups(s,p.id,now);}catch{groups=null;}
    for(const g of groups??[]){
      const short=g.quantity-free(g.allowed);
      if(short>0)rows.push({kind:'proposal',id:p.id,name:CONTENT_TEXT[p.templateId]?.name??p.templateId,label:`新订单「${CONTENT_TEXT[p.templateId]?.name??p.templateId}」`,what:g.selector?selectorLabel(g.selector):'',quantity:g.quantity,allowed:new Set(g.allowed),short});
    }
  }
  if(s.expansion?.projects)for(const id of PROJECT_IDS){
    const info=projectInfo(s,id),d=info.gateMet?info.current?.delivery:null;if(!d||d.full)continue;
    const allowed=d.locked??d.allowed,short=d.target-d.total-free(allowed);
    if(short>0)rows.push({kind:'project',id,name:info.name,label:`项目「${info.name}」`,what:'',quantity:d.target,allowed:new Set(allowed),short});
  }
  return rows;
}

function knownSets(s,toolId,now){
  const egg=s.egg,level=s.toolLevels[toolId],slots=Math.min(3,s.kitchenLevel+1),sets=new Map([['',{ingredients:[],via:[]}]]),sel=s.selected,nearSkill=!!rank(s,'OBS-2');
  const nearby=[],keep=(ids,via)=>{const k=setKey(ids);if(!sets.has(k))sets.set(k,{ingredients:[...ids].sort((a,b)=>a-b),via:[]});if(via)sets.get(k).via.push(via);};
  for(const r of RECIPE_CATALOG){
    if(r.toolId!==toolId||r.egg!==egg||!KINDS.has(r.kind)||r.minLevel>level||r.ingredients.length>slots)continue;
    const known=speciesDiscovered(s,r.egg,r.id),studied=s.progress.knowledge.recipes.includes(recipeId(r));
    // Clues that leave nothing open (the 线索册 「已解锁」 rule, knownRecipe): the cookware and the first seasoning of a
    // recipe that uses at most one, read from a clue or shown by the current observation skills.
    let clueComplete=false;
    if(!known&&!studied&&r.ingredients.length<=1){const k=knownRecipe(s,r.key,now);clueComplete=!!k&&k.toolId===r.toolId&&setKey(k.ingredients)===setKey(r.ingredients);}
    // "One seasoning away" directions the cook confirmation already shows (observation skill).
    const near=nearSkill&&!known&&!studied&&r.ingredients.length>0&&Math.max(r.ingredients.filter(id=>!sel.includes(id)).length,sel.filter(id=>!r.ingredients.includes(id)).length)===1;
    if(!known&&!studied&&!clueComplete&&!near)continue;
    if(!recipePathInfo(s,r,now).conditions.every(c=>c.met))continue;
    // Observation 2 shows at most three such directions in the cook confirmation (lowest species number first): the same three here.
    if(near&&!clueComplete){nearby.push(r);continue;}
    keep(r.ingredients,known?null:r.key);
  }
  [...new Map(nearby.map(r=>[r.key,r])).values()].sort((a,b)=>Number(a.key.split(':')[1])-Number(b.key.split(':')[1])).slice(0,3).forEach(r=>keep(r.ingredients,r.key));
  if(s.selected.length&&s.selected.length<=slots)keep(s.selected,null);
  return [...sets.values()];
}

// Clue directions for this cookware that are not complete yet: shown as "first seasoning + ?".
function clueDirections(s,toolId,now){
  const rows=[];
  for(const r of RECIPE_CATALOG){
    if(r.toolId!==toolId||r.egg!==s.egg||!KINDS.has(r.kind)||speciesDiscovered(s,r.egg,r.id)||r.ingredients.length<2)continue;
    const info=observationInfo(s,r.key,now);if(!info||info.full||info.path.toolId!==toolId)continue;
    const can=l=>info.levels[l]||hasFact(s,info.path,l);if(!can(2)||!can(3))continue;
    rows.push({key:r.key,code:speciesCode(r.key),first:E.label(E.ingredient(info.path.ingredients[0])),detail:info.details.find(d=>d.startsWith('第二味类别'))??'',count:info.path.ingredients.length});
  }
  return rows.filter((x,i,a)=>a.findIndex(y=>y.key===x.key)===i);
}

// Every union of up to `slots` seasonings taken from the known recipes of this cookware: income and orders can use them,
// but the birds in them stay hidden unless a set is a known recipe itself.
function comboSets(known,slots){
  const count=new Map();for(const k of known)for(const id of k.ingredients)count.set(id,(count.get(id)??0)+1);
  const pieces=[...count].sort((a,b)=>b[1]-a[1]||a[0]-b[0]).slice(0,10).map(([id])=>id),have=new Set(known.map(k=>setKey(k.ingredients))),out=[];
  const walk=(start,cur)=>{if(cur.length>=2&&!have.has(setKey(cur)))out.push([...cur]);if(cur.length>=slots)return;for(let i=start;i<pieces.length;i++){cur.push(pieces[i]);walk(i+1,cur);cur.pop();}};
  walk(0,[]);return out;
}

// demands: openDemands(s,now) when the caller already has it (it is the same for every cookware).
export function seasoningAdvice(s,toolId,now=Date.now(),{demands:given=null}={}){
  const level=s.toolLevels[toolId];
  if(!Number.isInteger(level)||level<0)return {toolId,owned:false,rows:[],clues:[]};
  if(toolId===8&&s.egg!==0)return {toolId,owned:true,eggBlocked:true,rows:[],clues:[]};
  const cook=E.tool(toolId)[`lv_${level}_cook_cp`],minutes=E.tool(toolId)[`lv_${level}_min`],demands=given??openDemands(s,now),egg=s.egg;
  const rows=[],slots=Math.min(3,s.kitchenLevel+1),known=knownSets(s,toolId,now);
  // known recipes first, then plain combinations; a set is only judged once, under the first reason.
  // A set whose exact recipe the player holds for a partner not met yet names that partner as a target (线索册「已解锁」).
  const held=new Map(),holds=key=>{if(!held.has(key))held.set(key,knownRecipe(s,key,now));return held.get(key);};
  const sets=[...known.map(k=>({...k,kind:'known'})),...comboSets(known.filter(k=>k.ingredients.length),slots).map(ingredients=>({ingredients:[...ingredients].sort((a,b)=>a-b),via:[],kind:'combo'}))],judged=new Set();
  for(const set of sets){
    if(judged.has(setKey(set.ingredients)))continue;judged.add(setKey(set.ingredients));
    const missing=set.ingredients.filter(id=>!(s.ingredients[id]>0));
    if(missing.some(id=>{const u=ingredientUnlockInfo(s,id);return !u.available||u.special;}))continue;
    const {counts,kinds,possible}=expectedOutcome(s,toolId,set.ingredients,now,{hideUnknown:set.kind!=='known'});
    const birds=[...counts].map(([id,n])=>({key:`${egg}:${id}`,id,n,known:speciesDiscovered(s,egg,id)})).sort((a,b)=>b.n-a.n);
    const sale=birds.reduce((v,b)=>v+b.n*E.char(egg,b.id).cp_1,0),seasoning=set.ingredients.reduce((v,id)=>v+E.ingredient(id).buy_cp,0);
    const tasks=demands.map(d=>({label:d.label,demand:d,n:Math.min(d.short,birds.filter(b=>d.allowed.has(b.key)).reduce((v,b)=>v+b.n,0))})).filter(t=>t.n>=.05);
    const buyCost=missing.reduce((v,id)=>v+E.ingredient(id).buy_cp,0);
    // The real cooking fee (skills can lower it); the list price is only the fallback.
    let fee=cook;try{fee=E.cookInfo({...s,selected:set.ingredients,egg},toolId,now).cost;}catch{/* keep the list price */}
    const newKinds=set.kind==='combo'?0:birds.filter(b=>!b.known&&reachableNow(s,egg,b.id,now)).reduce((v,b)=>v+(kinds.get(b.id)??0),0);
    const targets=set.via.filter(v=>{const k=holds(v);return !!k&&k.toolId===toolId&&setKey(k.ingredients)===setKey(set.ingredients)&&(kinds.get(Number(v.split(':')[1]))??0)>0;}).map(v=>({key:v,chance:kinds.get(Number(v.split(':')[1]))}));
    rows.push({key:setKey(set.ingredients),kind:set.kind,newKinds,targets,ingredients:set.ingredients,missing,clue:set.via.length>0,birds,possible,
      profit:Math.round(24+sale-cook-seasoning),buyCost,minutes,cash:fee+buyCost,affordable:s.cp>=fee+buyCost,
      taskScore:tasks.reduce((v,t)=>v+t.n,0),tasks,unknown:birds.filter(b=>!b.known).reduce((v,b)=>v+b.n,0)});
  }
  return {toolId,owned:true,level,cook,minutes,slots:Math.min(3,s.kitchenLevel+1),demands:demands.length,rows,clues:clueDirections(s,toolId,now)};
}

// Rows for one lens, best first. Only what can be started right now is advice: a set the player cannot pay for (cooking fee
// plus the seasonings to buy) is left out, like sets whose conditions are not met (knownSets) or whose seasonings cannot
// be bought (seasoningAdvice).
// 新伙伴 counts kinds, not birds: the real chance that a species you have not met shows up at least once, for sets whose
// recipe the player holds — so every row can really bring one. Low chances are listed too, they just rank lower; sets that
// name a target from the 线索册 come first. Sets built only by combining seasonings never appear there.
export function rankAdvice(advice,lens){
  const rows=advice.rows.filter(r=>r.affordable!==false);
  if(lens==='new')return rows.filter(r=>r.kind==='known'&&r.newKinds>=NEW_MIN).sort((a,b)=>(b.targets.length>0)-(a.targets.length>0)||b.newKinds-a.newKinds||b.profit-a.profit);
  if(lens==='tasks')return rows.filter(r=>r.taskScore>=.05).sort((a,b)=>b.taskScore-a.taskScore||b.profit-a.profit);
  return rows.sort((a,b)=>b.profit-a.profit);
}

// The known recipe that yields the most of one species per batch, across every owned
// cookware, for that species' egg. Used by "去做" links (menus, orders, projects).
const recipeCache=new WeakMap();
export function recipeForSpecies(s,key,now=Date.now()){
  const byKey=recipeCache.get(s)??new Map();recipeCache.set(s,byKey);if(byKey.has(key))return byKey.get(key);
  const egg=Number(key.split(':')[0]),view=s.egg===egg?s:{...s,egg};let best=null;
  for(const [id,level]of s.toolLevels.entries()){
    if(!Number.isInteger(level)||level<0)continue;
    const advice=seasoningAdvice(view,id,now);
    for(const row of advice.rows){const b=row.birds.find(x=>x.key===key);if(b&&b.n>=.5&&(!best||b.n>best.n))best={toolId:id,egg,ingredients:row.ingredients,missing:row.missing,n:Math.round(b.n)};}
  }
  byKey.set(key,best);return best;
}

// How many partners of this cookware are still undiscovered but reachable with what the player has (level, kitchen
// slots, seasonings that can be bought). Only a count: the recipes themselves stay secret until clues or 研读 reveal them.
export function undiscoveredReach(s,toolId,now=Date.now()){
  const level=s.toolLevels[toolId];if(!Number.isInteger(level)||level<0||(toolId===8&&s.egg!==0))return 0;
  const slots=Math.min(3,s.kitchenLevel+1),found=new Set();
  for(const r of RECIPE_CATALOG){
    if(r.toolId!==toolId||r.egg!==s.egg||!KINDS.has(r.kind)||r.minLevel>level||r.ingredients.length>slots||speciesDiscovered(s,r.egg,r.id))continue;
    if(r.ingredients.some(id=>{const u=ingredientUnlockInfo(s,id);return !u.available||u.special;}))continue;
    if(!recipePathInfo(s,r,now).conditions.every(c=>c.met))continue;
    found.add(r.key);
  }
  return found.size;
}

// 推测方向 (下一锅「新伙伴」 and 线索册「待推断」): partners not met yet whose recipe the player does not hold but whose cookware
// a clue or skill has named, and that could be cooked right now — every condition met (cookware level, kitchen level,
// holiday, shrine letter, time of day) and every seasoning of the real recipe usable now (in stock or for sale), so a
// direction is never one that cannot work yet; that check tells nothing about which seasonings they are.
// Only what the player has read comes back: the cookware, the first seasoning if known, the second one's flavour group with
// its members usable now if known. The player puts the set together; the hidden seasonings are never looked at, and no
// chance is given. Deepest clues first.
export function guessDirections(s,egg,now=Date.now()){
  const view=s.egg===egg?s:{...s,egg},seen=new Set(),out=[];
  const usable=id=>{if(view.ingredients?.[id]>0)return true;const u=ingredientUnlockInfo(view,id);return u.available&&!u.special;};
  for(const r of RECIPE_CATALOG){
    if(r.egg!==egg||seen.has(r.key)||!KINDS.has(r.kind)||speciesDiscovered(s,r.egg,r.id))continue;
    seen.add(r.key);
    if(knownRecipe(view,r.key,now))continue;
    const c=clueReach(view,r.key,now);if(!c||!c.tool||!KINDS.has(c.path.kind))continue;
    const p=c.path;if(!p.conditions.every(x=>x.met)||(p.toolId===8&&egg!==0)||!p.ingredients.every(usable))continue;
    const first=c.first&&p.ingredients.length?p.ingredients[0]:null;
    let candidates=null;
    if(c.group){candidates=(RULES.ingredientFlavorGroups[c.group]??[]).filter(id=>id!==first&&usable(id));if(!candidates.length)continue;}
    out.push({key:r.key,egg,id:r.id,code:speciesCode(r.key),silhouette:c.levels[0],riddle:SPECIES_CLUES[r.key]??'',toolId:p.toolId,level:p.minLevel,
      first,group:c.group,candidates,depth:c.levels.filter(Boolean).length});
  }
  return out.sort((a,b)=>(b.candidates!=null)-(a.candidates!=null)||(b.first!=null)-(a.first!=null)||b.depth-a.depth||a.id-b.id);
}
