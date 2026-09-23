import {materialCapacity,materialRoom} from './material-capacity.js';
import {economicRandom} from './rng.js';
import {RULES} from './integration-data.js';
import {SPECIES_ABILITIES as ABILITIES} from './content-registry.js';
import {rank,collectedTotal,discoveryCount,randomUnit,checkedIncome} from './progression.js';
import {availableCount} from './inventory.js';
import {advanceWorld} from './world-clock.js';
import {availableIngredientIds} from './ingredient-unlocks.js';
import {clueCandidates,clueStillNew} from './knowledge.js';
export const LEGACY_ROUTES=RULES.exploration.routes;
// The bay (Work G) is a new 12h route with its own ordinary pool (salt, laver,
// lemon); it exists only after GUIDE-B and uses the water adaptation as alias.
export const BAY_ROUTE=Object.freeze({id:'bay',name:'风湾盐田',hours:12,baseUnits:3,pool:Object.freeze([27,74,1]),requiredCollected:240,requiredDiscoveries:40,environment:'water',requiresGuide:'GUIDE-B'});
export const ROUTES=Object.freeze([...LEGACY_ROUTES,BAY_ROUTE]);
const routeEnvironment=route=>route.environment??route.id;
export function explorationInfo(s,routeId,members=[],now=Date.now(),{light=false}={}){
  const route=ROUTES.find(r=>r.id===routeId);if(!route)throw Error('路线无效');
  const unlocked=collectedTotal(s)>=route.requiredCollected&&discoveryCount(s)>=route.requiredDiscoveries&&(!route.requiresGuide||!!s.expansion?.regions?.guideFlags?.includes(route.requiresGuide));
  const pool=availableIngredientIds(s),team=members.map(key=>({key,...ABILITIES[key]}));
  const G=team.reduce((n,a)=>n+a.gather,0),F=team.reduce((n,a)=>n+a.discover,0),A=team.filter(a=>a.environment===routeEnvironment(route)).length;
  light=!!(light&&route.id!=='yard'&&rank(s,'TRIP-5'));
  const baseUnits=route.baseUnits-(light?1:0),hours=Number((route.hours*(light?.8:1)).toFixed(2));
  const materialChance=Math.min(.6,.05+.025*G+.04*A+(rank(s,'TRIP-3')?.03*A:0)+(rank(s,'TRIP-S')?.06:0));
  const clueChance=Math.min(.55,.10+.02*F+.03*A+(rank(s,'TRIP-3')?.02*A:0));
  const clues=clueCandidates(s,route,now);
  return {route,unlocked,team,G,F,A,materialChance,clueChance:clues.length?clueChance:0,clues,light,baseUnits,hours,cpReward:rank(s,'TRIP-1')?baseUnits*6:0,directedUnits:Math.min(baseUnits,rank(s,'TRIP-S')?2:rank(s,'TRIP-2')?1:0),
    pool:route.pool.map(id=>({id,unlocked:pool.includes(id),actual:pool.includes(id)?id:0})),hardAttempt:rank(s,'TRIP-4')?6:8,
    minUnits:baseUnits,maxUnits:baseUnits+1};
}
export function depart(s,{routeId,members,directed=[],priority=null,light=false},now=Date.now(),random=economicRandom(s,'depart')){
  advanceWorld(s,now,random);
  if(['running','returned'].includes(s.progress.trip?.status))throw Error('请先等待归队并收完上一篮');
  if(!Array.isArray(members)||members.length<1||members.length>3||new Set(members).size!==members.length||members.some(k=>!ABILITIES[k]||availableCount(s,k)<1))throw Error('请选择1至3种在家伙伴，同队不能重复');
  const info=explorationInfo(s,routeId,members,now,{light});if(!info.unlocked)throw Error('路线尚未开放');
  if(!Array.isArray(directed)||directed.length>info.directedUnits||directed.some(id=>!info.pool.some(p=>p.id===id&&p.unlocked)))throw Error('定向材料不符合当前手艺或供应资格');
  if(priority!==null&&(!rank(s,'OBS-S')||!info.clues.slice(0,3).some(c=>c.key===priority)))throw Error('线索目标已变化，请重新选择');
  const remaining=[],bonus=randomUnit(random)<info.materialChance;
  for(let i=0;i<info.baseUnits+(bonus?1:0);i++)remaining.push(i<directed.length?directed[i]:info.pool[Math.floor(randomUnit(random)*info.pool.length)].actual);
  // Shallower facts always lead; shuffle only within a knowledge layer.
  const order=[];
  for(const level of [1,2]){
    const layer=info.clues.filter(c=>c.level===level);
    for(let i=layer.length-1;i>0;i--){const j=Math.floor(randomUnit(random)*(i+1));[layer[i],layer[j]]=[layer[j],layer[i]];}
    order.push(...layer);
  }
  if(priority){const i=order.findIndex(c=>c.key===priority);order.unshift(...order.splice(i,1));}
  const roll=order.length?randomUnit(random):null,hard=(s.progress.routeFailures[routeId]??0)>=info.hardAttempt-1;
  const hit=order.length>0&&(hard||roll<info.clueChance),at=Math.max(now,s.progress.logicalAt);
  s.progress.logicalAt=at;
  s.progress.tripSequence++;
  s.progress.trip={id:'trip-'+s.progress.tripSequence,version:1,status:'running',routeId,members:[...members],startedAt:at,endAt:at+info.hours*3600000,
    remaining,cpReward:info.cpReward&&randomUnit(random)<.2?info.cpReward:0,cpProcessed:false,clueOrder:order,clueHit:hit,clueProcessed:false,clueResult:null,special:hit&&roll<.05,
    snapshot:{light:info.light,baseUnits:info.baseUnits,G:info.G,F:info.F,A:info.A,materialChance:info.materialChance,clueChance:info.clueChance,hardAttempt:info.hardAttempt,bonus,directed:[...directed],priority,skills:{...s.progress.skills}}};
  s.progress.lastTeam=[...members];return {id:s.progress.trip.id,endAt:s.progress.trip.endAt};
}
function tripFor(s,id){const t=s.progress.trip;if(!t||t.id!==id)throw Error('这趟探索已变化，请重新打开');return t;}
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
  if(!t.clueProcessed){
    const eligible=t.clueOrder.filter(c=>clueStillNew(s,c));
    if(eligible.length){
      if(t.clueHit){clue=eligible[0];s.progress.knowledge.facts.push(clue.fact);s.progress.routeFailures[t.routeId]=0;t.clueResult=clue;}
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
