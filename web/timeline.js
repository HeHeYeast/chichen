import {REGIONAL} from './content-registry.js';
import {identifyMaterial} from './regional-methods.js';
import {advanceBusiness,nextBusinessBoundary,settleBusinessClose,closeBusiness} from './business.js';
import {settleRegionalTrip} from './regional-exploration.js';
import {farmLossAt} from './farm-clock.js';
import {channelRandom} from './rng.js';
import {orderMilestone} from './orders.js';
import {recordLegacyTripFact} from './regional-exploration.js';

export function advanceTimeline(s,now=Date.now(),random=null,{checkFarm=false}={}){
  if(!Number.isSafeInteger(now)||now<0)throw Error('时间无效');
  // Old records gain the same automatic recognition, without replaying rewards.
  for(const m of REGIONAL.materials)if(Object.hasOwn(s.expansion?.discovery?.cards??{},m.specimen)&&!Object.hasOwn(s.expansion.discovery.identified,m.id))identifyMaterial(s,m.id,{settlement:true});
  const at=Math.max(now,s.progress.logicalAt??0),trip=s.progress.trip;
  let lost=0,returned=false,sold=0,closed=false,lastRelease=null;
  const loss=when=>farmLossAt(s,when,random??channelRandom(s,`farm-${when}`,'farm-loss'));
  for(let guard=0;guard<16;guard++){
    const tripAt=trip?.status==='running'?trip.endAt:Infinity,businessAt=nextBusinessBoundary(s)??Infinity;
    const boundary=Math.min(tripAt,businessAt);if(boundary>at)break;
    if(s.expansion.business?.active){const outcome=advanceBusiness(s,boundary,{deferClose:true});sold+=outcome.sold;}
    const releaseTrip=tripAt===boundary,session=s.expansion.business?.active,releaseBusiness=session?.pendingCloseAt===boundary;
    if(releaseTrip||releaseBusiness){lost+=loss(boundary);lastRelease=boundary;}
    if(releaseTrip){trip.status='returned';trip.returnedAt=boundary;settleRegionalTrip(s,trip);recordLegacyTripFact(s,trip);orderMilestone(s,boundary,'trip');returned=true;}
    if(releaseBusiness){settleBusinessClose(s,boundary,Object.values(session.stock).some(n=>n>0)?'deadline':'sold-out');closed=true;}
    if(guard===15)throw Error('时间线事件数量异常');
  }
  if((checkFarm||(returned&&trip.version===1))&&lastRelease!==at)lost+=loss(at);
  if(s.progress.trip?.status==='running'||s.expansion.business?.active||returned||closed)s.progress.logicalAt=at;
  return {lost,returned,sold,closed};
}
export function closeBusinessTimeline(s,now){advanceTimeline(s,now,null,{checkFarm:true});return closeBusiness(s,now);}
