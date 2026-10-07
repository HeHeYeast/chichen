// Work D gate: closing/settling through the real command + store boundary,
// compiled MN1 conditions, respec interplay and instant sale beside S.
import test from 'node:test';
import assert from 'node:assert/strict';
import {freshState,normalizeSave,sell} from '../web/engine.js';
import {openBusiness,closeBusiness,BUSINESS_WINDOW_MS as WINDOW} from '../web/business.js';
import {businessUnlockInfo} from '../web/menu-model.js';
import {evaluate} from '../web/requirements.js';
import {syncProgress,respecReason,applySkillPlan,learnSkill} from '../web/progression.js';
import {execute} from '../web/game-commands.js';
import {createSaveStore} from '../web/save-store.js';
import {advanceTimeline,closeBusinessTimeline} from '../web/timeline.js';
import {freeCount,inventoryView} from '../web/inventory.js';

const NOW=1800000000000,stock={'0:0':24};
function fixture({rewards=true}={}){
  const s=freshState(NOW);s.farm={'0:0':25,'0:3':25};s.total=Object.fromEntries(Array.from({length:12},(_,id)=>[`0:${id}`,id===0?2000:1]));
  s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};
  syncProgress(s);if(rewards){learnSkill(s,'TRADE-2');learnSkill(s,'TRADE-3');s.progress.trade.category='家常';}
  return normalizeSave(s,NOW);
}
function nativeStore(state,{loseAck=false,failWrite=false}={}){
  let raw=JSON.stringify(state);
  const native={loadSave:()=>JSON.stringify({status:'ok',raw}),commitGame(next){if(failWrite)return JSON.stringify({ok:false,message:'storage full'});raw=next;if(loseAck)throw Error('bridge lost');return JSON.stringify({ok:true});},saveGame(next){raw=next;return JSON.stringify({ok:true});}};
  const store=createSaveStore({storage:null,key:'native',native,now:()=>NOW});store.load();
  return {store,read:()=>JSON.parse(raw)};
}
const run=(state,store,now,type,reduce)=>execute({state,store,command:{type},now,advance:advanceTimeline,reduce});

test('MN1 compiled requirements agree with the business gate at every boundary',()=>{
  const s=fixture();
  for(const mutate of [s=>s,s=>{s.total['0:0']=50;},s=>{for(let i=2;i<12;i++)delete s.total['0:'+i];},s=>{s.progress.orders['first-sale'].completed=false;}]){
    const x=structuredClone(s);mutate(x);
    assert.equal(evaluate('MN1:unlock',x).met,businessUnlockInfo(x).met);
  }
});

test('a close whose native acknowledgement is lost is read back as committed and never pays twice',()=>{
  let state=fixture();const {store,read}=nativeStore(state,{loseAck:false});
  state=run(state,store,NOW,'openBusiness',d=>openBusiness(d,{stock,useRewards:true},NOW)).state;
  const lossy=nativeStore(state,{loseAck:true});
  const closed=run(state,lossy.store,NOW+8*WINDOW,'closeBusiness',d=>closeBusinessTimeline(d,NOW+8*WINDOW));
  const persisted=lossy.read();
  assert.equal(persisted.meta.revision,closed.state.meta.revision,'the persisted revision is the committed one');
  // rules 2: 鸡宝 alone does not complete 家常小铺, so no menu bonus (72 + markup 8 + basket 12)
  assert.equal(closed.state.expansion.business.lastReport.income,92);
  const replay=execute({state:closed.state,store:lossy.store,commandId:closed.state.meta.lastCommit.commandId,command:{type:'closeBusiness'},now:NOW+8*WINDOW,reduce:d=>closeBusinessTimeline(d,NOW+8*WINDOW)});
  assert.equal(replay.replayed,true);assert.equal(replay.state.cp,closed.state.cp);
  const again=run(closed.state,lossy.store,NOW+9*WINDOW,'closeBusiness',d=>closeBusiness(d,NOW+9*WINDOW));
  assert.equal(again.state.cp,closed.state.cp,'a second close only returns the report');
  assert.equal(again.state.progress.trade.credits,closed.state.progress.trade.credits);
  void read;
});

test('an unwritten close leaves CP, farm, S and credits exactly as persisted',()=>{
  let state=fixture();const ok=nativeStore(state);
  state=run(state,ok.store,NOW,'openBusiness',d=>openBusiness(d,{stock,useRewards:true},NOW)).state;
  const failing=nativeStore(state,{failWrite:true});
  assert.throws(()=>run(state,failing.store,NOW+3*WINDOW,'closeBusiness',d=>closeBusinessTimeline(d,NOW+3*WINDOW)),error=>error.code==='SAVE_FAILED'&&/暂未保存/.test(error.message));
  assert.deepEqual(failing.read(),state);assert.equal(state.expansion.business.active.stock['0:0'],24);
});

test('instant sale beside an open business uses only free stock and unreserved credits',()=>{
  const s=fixture();openBusiness(s,{stock,useRewards:true},NOW);
  assert.equal(freeCount(s,'0:0'),1);assert.throws(()=>sell(s,{'0:0':2},{},NOW+1));
  const cp=s.cp,credits=s.progress.trade.credits;sell(s,{'0:0':1},{keepOne:false},NOW+1);
  assert.equal(s.progress.trade.credits,credits,'reserved basket credit is not spent by an instant sale');
  assert.equal(s.cp,cp+3+0);assert.deepEqual(inventoryView(s,'0:0'),{T:24,R:0,S:24,Q:0,free:0,home:0});
});

test('respec waits for the business; closing settles past windows first and releases unsold S',()=>{
  const s=fixture();openBusiness(s,{stock,useRewards:true},NOW);
  assert.match(respecReason(s,NOW+1),/营业/);assert.throws(()=>applySkillPlan(s,{steps:[],reset:true,category:null,base:JSON.stringify(s.progress.skills)},NOW+1));
  const report=closeBusinessTimeline(s,NOW+3*WINDOW+5);
  assert.equal(report.totalSold,18);assert.equal(report.creditsReleased,1);assert.equal(s.progress.trade.credits,1);
  assert.equal(freeCount(s,'0:0'),7);assert.equal(respecReason(s,NOW+3*WINDOW+5),'');
  applySkillPlan(s,{steps:[],reset:true,category:null,base:JSON.stringify(s.progress.skills)},NOW+3*WINDOW+5);
  assert.equal(s.expansion.business.lastReport.income,report.income,'respec never rewrites a closed report');
});

test('weeks offline settle one session exactly once and never reopen another',()=>{
  const s=fixture();openBusiness(s,{stock},NOW);const cp=s.cp;
  advanceTimeline(s,NOW+21*24*3600000);
  assert.equal(s.expansion.business.active,null);assert.equal(s.expansion.business.lastReport.totalSold,24);
  assert.equal(s.cp,cp+s.expansion.business.lastReport.income);
  const after=structuredClone(s);advanceTimeline(s,NOW+42*24*3600000);assert.equal(s.cp,after.cp);assert.equal(s.expansion.business.sequence,1);
});
