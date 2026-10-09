import {assertNewOperation} from './rollback-policy.js';
import {EXTRA_ROUTES} from './extra-regions.js';
import {materialCapacity,materialRoom} from './material-capacity.js';
import {economicRandom} from './rng.js';
import {RULES} from './integration-data.js';
import {SPECIES_ABILITIES as ABILITIES,ABILITY_SCALE} from './content-registry.js';
import {rank,collectedTotal,discoveryCount,randomUnit,checkedIncome} from './progression.js';
import {availableCount} from './inventory.js';
import {advanceWorld} from './world-clock.js';
import {availableIngredientIds} from './ingredient-unlocks.js';
import {clueCandidates,tripClue,trackedKey} from './knowledge.js';
import {regionalRuntimeId} from './regional-clues.js';
import {learnRegionalMethod} from './regional-methods.js';
export const LEGACY_ROUTES=RULES.exploration.routes;
// The bay (Work G) is a new 12h route with its own ordinary pool (salt, laver,
// lemon); it exists only after GUIDE-B and uses the water adaptation as alias.
export const BAY_ROUTE=Object.freeze({id:'bay',name:'风湾盐田',hours:12,baseUnits:3,pool:Object.freeze([27,74,1]),requiredCollected:240,requiredDiscoveries:40,environment:'water',requiresGuide:'GUIDE-B'});
export const ROUTES=Object.freeze([...LEGACY_ROUTES,BAY_ROUTE,...EXTRA_ROUTES]);
const routeEnvironment=route=>route.environment??route.id;
export function explorationInfo(s,routeId,members=[],now=Date.now(),{light=false}={}){
  const route=ROUTES.find(r=>r.id===routeId);if(!route)throw Error('路线无效');
  let unlocked=s.kitchenLevel>=(route.kitchenLevel??0)&&collectedTotal(s)>=route.requiredCollected&&discoveryCount(s)>=route.requiredDiscoveries&&(!route.requiresGuide||!!s.expansion?.regions?.guideFlags?.includes(route.requiresGuide));
  const pool=availableIngredientIds(s),team=members.map(key=>({key,...ABILITIES[key]}));
  if(EXTRA_ROUTES.some(r=>r.id===routeId)&&!route.pool.some(id=>pool.includes(id)))unlocked=false;
  const G=team.reduce((n,a)=>n+a.gather,0),F=team.reduce((n,a)=>n+a.discover,0),A=team.filter(a=>a.environment===routeEnvironment(route)).length;
  light=!!(light&&route.id!=='yard'&&rank(s,'TRIP-5'));
  const baseUnits=route.baseUnits-(light?1:0),hours=Number((route.hours*(light?.8:1)).toFixed(2));
  const materialChance=Math.min(.6,.05+.025*G/ABILITY_SCALE+.04*A+(rank(s,'TRIP-3')?.03*A:0)+(rank(s,'TRIP-S')?.06:0));
  const clueChance=Math.min(.55,.10+.02*F/ABILITY_SCALE+.03*A+(rank(s,'TRIP-3')?.02*A:0));
  const clues=clueCandidates(s,route,now);
  // The tracked partner's clue lies on this route: every trip reads its next layer (clueSure). clueChance stays the team's
  // own odds, which the trip snapshot keeps (saves check it is at most 55%).
  const trackedClue=clues[0]?.key===trackedKey(s)?clues[0]:null;
  return {route,unlocked,team,G,F,A,materialChance,clueChance:clues.length?clueChance:0,clueSure:!!trackedClue,clues,trackedClue,light,baseUnits,hours,cpReward:rank(s,'TRIP-1')?baseUnits*6:0,directedUnits:Math.min(baseUnits,rank(s,'TRIP-S')?2:rank(s,'TRIP-2')?1:0),
    pool:route.pool.map(id=>({id,unlocked:pool.includes(id),actual:pool.includes(id)?id:0})),hardAttempt:rank(s,'TRIP-4')?6:8,
    minUnits:baseUnits,maxUnits:baseUnits+1};
}
export function depart(s,{routeId,members,directed=[],priority=null,light=false},now=Date.now(),random=economicRandom(s,'depart')){
  advanceWorld(s,now,random);
  if(['orchard','mushroom'].includes(routeId))assertNewOperation('region');
  if(['running','returned'].includes(s.progress.trip?.status))throw Error('请先等待归队并收完上一篮');
  const need={};if(Array.isArray(members))for(const k of members)need[k]=(need[k]??0)+1;
  if(!Array.isArray(members)||members.length<1||members.length>3||members.some(k=>!ABILITIES[k])||Object.entries(need).some(([k,n])=>availableCount(s,k)<n))throw Error('请选择1至3位在家伙伴；同一种可以多带，但不能超过在家的只数');
  const info=explorationInfo(s,routeId,members,now,{light});if(!info.unlocked)throw Error('路线尚未开放');
  if(!Array.isArray(directed)||directed.length>info.directedUnits||directed.some(id=>!info.pool.some(p=>p.id===id&&p.unlocked)))throw Error('定向材料不符合当前手艺或供应资格');
  if(priority!==null&&(!rank(s,'OBS-S')||!info.clues.slice(0,3).some(c=>c.key===priority)))throw Error('线索目标已变化，请重新选择');
  const remaining=[],bonus=randomUnit(random)<info.materialChance;
  const gatherPool=['orchard','mushroom'].includes(routeId)?info.pool.filter(p=>p.unlocked):info.pool;
  if(!gatherPool.length)throw Error('这里的食材尚未开放供货');
  for(let i=0;i<info.baseUnits+(bonus?1:0);i++)remaining.push(i<directed.length?directed[i]:gatherPool[Math.floor(randomUnit(random)*gatherPool.length)].actual);
  // One ticket per partner, in clueCandidates order (the tracked partner, then those closest to done); a chosen priority
  // (寻味专家) goes first unless the tracked partner's clue is on this route, which then always comes home.
  const order=info.clues.slice(0,12);
  if(priority&&!info.trackedClue){const i=order.findIndex(c=>c.key===priority);if(i>=0)order.unshift(...order.splice(i,1));}
  const roll=order.length?randomUnit(random):null,hard=(s.progress.routeFailures[routeId]??0)>=info.hardAttempt-1;
  const hit=order.length>0&&(!!info.trackedClue||hard||roll<info.clueChance),at=Math.max(now,s.progress.logicalAt);
  s.progress.logicalAt=at;
  s.progress.tripSequence++;
  s.progress.trip={id:'trip-'+s.progress.tripSequence,version:1,status:'running',routeId,members:[...members],startedAt:at,endAt:at+info.hours*3600000,
    remaining,cpReward:info.cpReward&&randomUnit(random)<.2?info.cpReward:0,cpProcessed:false,clueOrder:order,clueHit:hit,clueProcessed:false,clueResult:null,special:hit&&roll<.05,
    snapshot:{light:info.light,baseUnits:info.baseUnits,G:info.G,F:info.F,A:info.A,materialChance:info.materialChance,clueChance:info.clueChance,hardAttempt:info.hardAttempt,bonus,directed:[...directed],priority,skills:{...s.progress.skills}}};
  s.progress.lastTeam=[...members];return {id:s.progress.trip.id,endAt:s.progress.trip.endAt};
}
function tripFor(s,id){const t=s.progress.trip;if(!t||t.id!==id)throw Error('这趟寻访已变化，请重新打开');return t;}
export function recall(s,id,now=Date.now(),random=economicRandom(s,'recall')){
  advanceWorld(s,now,random);const t=tripFor(s,id);
  if(t.status!=='running')return {returned:t.status==='returned'};
  // Recall releases carried cargo untouched: nothing is exchanged or sold.
  if(t.cargo&&!t.cargo.processed){t.cargo.processed=true;t.cargo.outcome='released';}
  t.status='recalled';t.remaining=[];t.cpReward=0;t.cpProcessed=true;t.clueProcessed=true;t.recalledAt=Math.max(now,s.progress.logicalAt);return {recalled:true};
}
export function claimTrip(s,id,{materials=true,discard=false}={},now=Date.now(),random=economicRandom(s,'claimTrip')){
  advanceWorld(s,now,random);const t=tripFor(s,id);
  if(t.status==='settled')return {materials:[],clue:null};
  if(t.status!=='returned')throw Error('伙伴尚未归队');
  let clue=null,cp=0;
  if(!t.cpProcessed){cp=t.cpReward??0;checkedIncome(s,cp);s.cp+=cp;t.cpProcessed=true;}
  // At most one clue per trip: the next layer of one partner (tripClue).
  if(!t.clueProcessed){
    const pick=tripClue(s,t,now);
    if(pick){
      if(t.clueHit){clue={key:pick.key,recipeId:pick.recipeId,level:pick.level,fact:pick.fact};s.progress.knowledge.facts.push(clue.fact);s.progress.routeFailures[t.routeId]=0;t.clueResult=clue;
        // a regional partner's fifth layer is its complete method: written down as 研读 would (loop batch 4)
        const regional=regionalRuntimeId(pick.key);if(regional&&pick.level===5)learnRegionalMethod(s,regional);}
      else s.progress.routeFailures[t.routeId]=Math.min(7,(s.progress.routeFailures[t.routeId]??0)+1);
    }
    t.clueProcessed=true;
  }
  const received=[];
  if(materials){
    let room=materialRoom(s);
    while(room>0&&t.remaining.length){const id=t.remaining.shift();s.ingredients[id]=(s.ingredients[id]??0)+1;received.push(id);room--;}
  }
  if(discard)t.remaining=[];
  if(!t.remaining.length&&t.clueProcessed)t.status='settled';
  return {materials:received,cp,clue,remaining:t.remaining.length,special:t.special};
}
