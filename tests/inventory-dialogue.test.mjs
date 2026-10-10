import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {makeBackup,parseBackup} from '../web/save-store.js';
import {createRegularUI} from '../web/regular-ui.js';
import {loopGuide} from '../web/loop-guide.js';
import {createWorkshopUI} from '../web/workshop-ui.js';
import {depart} from '../web/exploration.js';

const NOW=1800000000000;

test('ingredient purchases above every former cap survive save and backup round trips',()=>{
  const s=E.freshState(NOW);s.cp=10000000;
  E.buyIngredient(s,0,100000);
  assert.equal(s.ingredients[0],100001);
  assert.deepEqual(E.normalizeSave(s,NOW).ingredients,s.ingredients);
  assert.deepEqual(parseBackup(makeBackup(s,NOW),NOW).ingredients,s.ingredients);
  for(const n of [-1,1.5,Infinity,NaN,Number.MAX_SAFE_INTEGER+1]){
    const invalid=structuredClone(s);invalid.ingredients[0]=n;
    assert.throws(()=>E.normalizeSave(invalid,NOW));
  }
});

test('the guide recognizes an existing trip before asking for the introductory conversation',()=>{
  const s=E.freshState(NOW);
  s.total={'0:0':100,'0:3':1,'0:4':1};
  s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};
  for(const status of ['running','returned']){
    s.progress.trip={status};
    assert.equal(loopGuide(s).target,'journey');
    assert.match(loopGuide(s).text,status==='running'?/正在寻访/:/已经回来了/);
  }
});

// Exercise actual dialogue button handlers. Persistence uses the same save
// validator as gameplay; navigation is recorded so failed saves cannot advance.
function dialogueHarness({fail=false,state=E.freshState(NOW)}={}){
  let controls=new Map(),html='',business=0,journeys=0;
  const panels={querySelector:q=>controls.get(q)??null,querySelectorAll:()=>[]};
  const ui=createRegularUI({getState:()=>state,panels,
    showPanel(_title,body){html=body;controls=new Map(['.regulars-scroll','.bs-sub-dialog','[data-shop-back]','[data-shop-help]','[data-shop-help-close]','[data-intro-close]','[data-intro-next]'].map(q=>[q,{scrollTop:0,close(){},showModal(){},insertAdjacentHTML(){}}]));},
    commitProgress:fn=>{if(fail)return null;const draft=structuredClone(state),result=fn(draft);state=E.normalizeSave(draft,NOW);return result;},
    openBusiness:()=>{business++;},openJourney:()=>{journeys++;}
  });
  return {ui,next:()=>controls.get('[data-intro-next]').onclick(),get state(){return state;},get html(){return html;},get business(){return business;},get journeys(){return journeys;}};
}

test('completed introduction cannot replay through a remembered subpage or a restored save',()=>{
  const h=dialogueHarness();h.ui.open('intro:RG1');h.next();h.next();
  assert.equal(h.state.events.loopVisitorMet,undefined);
  h.next();assert.equal(h.state.events.loopVisitorMet,true);assert.equal(h.journeys,1);
  h.ui.open('intro:RG1');assert.equal(h.business,1);assert.equal(h.journeys,1);
  h.ui.resume();assert.doesNotMatch(h.html,/data-intro-next/);
  const restored=dialogueHarness({state:parseBackup(makeBackup(h.state,NOW),NOW)});
  restored.ui.open('intro:RG1');assert.equal(restored.business,1);assert.equal(restored.journeys,0);
});

test('failed acknowledgement save keeps the final dialogue available to retry',()=>{
  const h=dialogueHarness({fail:true});h.ui.open('intro:RG1');h.next();h.next();h.next();
  assert.equal(h.state.events.loopVisitorMet,undefined);assert.equal(h.journeys,0);
  assert.match(h.html,/data-intro-next/);
});

test('legacy trip receipt renders all materials with a large inventory',()=>{
  const s=E.freshState(NOW);s.total=Object.fromEntries(Array.from({length:24},(_,id)=>['0:'+id,100]));s.farm={'0:0':3};s.ingredients={0:100000};
  depart(s,{routeId:'yard',members:['0:0']},NOW,()=>.99);
  E.advanceWorld(s,s.progress.trip.endAt,()=>.99);
  let html='';
  const ui=createWorkshopUI({getState:()=>s,getNow:()=>s.progress.trip.endAt,
    panels:{querySelector:()=>null,querySelectorAll:()=>[]},
    showPanel:(_title,body)=>{html=body;},characterPortrait:()=>''});
  ui.open('trip');assert.match(html,/data-claim-trip/);assert.match(html,/材料包 100000 份/);assert.doesNotMatch(html,/Infinity|材料包满/);
});
