// Work J: four projects, staged checks, bounded deliveries, fixed payments,
// ALT-R, menu presets and the PJ-4 portrait display.
import test from 'node:test';
import assert from 'node:assert/strict';
import * as E from '../web/engine.js';
import {REGIONAL,SPECIES_TRADE,resolveSpecies} from '../web/content-registry.js';
import {LEGACY193} from '../web/legacy-content.js';
import {syncProgress} from '../web/progression.js';
import {execute} from '../web/game-commands.js';
import {reduceFacts} from '../web/facts.js';
import {openBusiness} from '../web/business.js';
import {projectInfo,deliverProject,completeProjectStage,setProjectPortraits,saveMenuPreset,deleteMenuPreset,projectComplete,validateProjectsState,PROJECT_RULES} from '../web/projects.js';
import {projectsModel} from '../web/project-model.js';
import {regionalAlternativeInfo} from '../web/regional-methods.js';
import {REGIONAL_RELEASE,alternativeUnlocked} from '../web/region-model.js';
import {RUNTIME_REQUIREMENTS} from '../web/runtime-requirements.generated.js';

const NOW=1800000000000;
let seq=0;
function base(){
  const s=E.freshState(NOW,21);s.kitchenLevel=2;s.duck=true;s.toolLevels=[0,0,0,0,0,0,0,0,0];s.cp=50000;
  s.total=Object.fromEntries(LEGACY193.characters.flatMap((list,egg)=>list.map(c=>[`${egg}:${c.id}`,c.id===0&&egg===0?3000:2])));
  s.farm={'0:0':30,'0:3':20,'0:4':20,'0:8':20,'1:0':20,'1:3':20};
  s.progress.orders['first-sale']={accepted:true,choice:'0:0',delivered:12,completed:true};syncProgress(s);return E.normalizeSave(s,NOW);
}
const cards=(s,...ids)=>{for(const id of ids)s.expansion.discovery.cards[id]=++s.meta.factSeq;};
const identify=(s,...ids)=>{for(const id of ids)s.expansion.discovery.identified[String(id)]=++s.meta.factSeq;};
const collect=(s,...keys)=>{for(const k of keys){s.total[k]=(s.total[k]??0)+1;}};
const service=(s,...menus)=>{for(const menuId of menus)reduceFacts(s,[{kind:'businessWitness',sessionId:`business-${++seq}`,menuId,soldByKey:{'0:0':6},roleSales:{},fullSoldByKey:{},fullRoleSales:{},valid:true,complete:false}]);};
const check=s=>{validateProjectsState(s,m=>{throw Error(m);});E.normalizeSave(s,NOW);};
const stage=(s,p,id)=>projectInfo(s,p).stages.find(x=>x.id===id);

test('every project gate, stage check and the portrait rule is compiled; J leaves only the UI rule for K',()=>{
  for(const p of REGIONAL.projects){assert.equal(RUNTIME_REQUIREMENTS[`${p.id}:gate`].kind,'compiled');for(const st of p.stages){assert.equal(RUNTIME_REQUIREMENTS[`${st.id}:check`].kind,'compiled');assert.ok(PROJECT_RULES[st.id]);}}
  assert.equal(RUNTIME_REQUIREMENTS['PJ-4:optionalDisplay.rule'].kind,'compiled');
  assert.ok(REGIONAL_RELEASE.alternatives.includes('ALT-R'));
  assert.deepEqual(REGIONAL.projects.map(p=>p.stages.map(x=>x.costCP)),[[0,0,200],[100,0,400],[200,0,600],[400,600,1000]]);
});

test('PJ-1: six recorded dishes, two menus (retroactive), then 200 CP; afterwards three menu presets',()=>{
  const s=base();assert.equal(projectInfo(s,'PJ-1').gateMet,true);
  assert.throws(()=>saveMenuPreset(s,0,{menuId:'MN1',stock:{'0:0':6}}),/招牌册/);
  assert.equal(stage(s,'PJ-1','PJ-1-A').ready,true);completeProjectStage(s,'PJ-1','PJ-1-A');
  assert.equal(stage(s,'PJ-1','PJ-1-B').checksMet,false);service(s,'MN1','MN3');assert.equal(stage(s,'PJ-1','PJ-1-B').ready,true);
  const cp=s.cp;completeProjectStage(s,'PJ-1','PJ-1-B');assert.equal(s.cp,cp,'check stages cost nothing');
  completeProjectStage(s,'PJ-1','PJ-1-C');assert.equal(s.cp,cp-200);assert.equal(projectComplete(s,'PJ-1'),true);
  assert.throws(()=>completeProjectStage(s,'PJ-1','PJ-1-C'),/已经完成/);assert.equal(s.cp,cp-200,'a repeated confirm never charges twice');
  for(let i=0;i<3;i++)saveMenuPreset(s,i,{menuId:'MN1',stock:{'0:0':6,'0:3':6}});
  assert.throws(()=>saveMenuPreset(s,3,{menuId:'MN1',stock:{'0:0':6}}));assert.equal(s.expansion.menus.presets.length,3);
  deleteMenuPreset(s,1);assert.equal(s.expansion.menus.presets.length,2);assert.deepEqual(s.farm['0:0'],30,'presets never reserve stock');check(s);
});

test('PJ-2: 100 CP after both river specimens and four river species; two kinds × 6 locked on first delivery; 400 CP teaches ALT-R',()=>{
  const s=base();assert.equal(projectInfo(s,'PJ-2').gateMet,false);identify(s,77);assert.equal(projectInfo(s,'PJ-2').gateMet,true);
  cards(s,'R-S1','R-S2');identify(s,78);collect(s,'0:134','0:135','0:136');
  assert.equal(stage(s,'PJ-2','PJ-2-A').checksMet,false,'chicken and duck each at least one');collect(s,'1:76');
  assert.equal(stage(s,'PJ-2','PJ-2-A').checksMet,true,'an ornamental duck still counts as collected');
  assert.throws(()=>deliverProject(s,'PJ-2','PJ-2-B',{'0:3':6},{choice:['0:3','0:4']}),/不能交付/);
  const cp=s.cp;completeProjectStage(s,'PJ-2','PJ-2-A');assert.equal(s.cp,cp-100);
  assert.throws(()=>deliverProject(s,'PJ-2','PJ-2-B',{'0:3':6}),/先选定2种/);
  deliverProject(s,'PJ-2','PJ-2-B',{'0:3':4},{choice:['0:3','0:4']});assert.equal(s.farm['0:3'],16);assert.equal(s.cp,cp-100,'deliveries pay nothing');
  assert.deepEqual(s.expansion.projects['PJ-2'].pinnedChoices['PJ-2-B'],['0:3','0:4']);
  assert.throws(()=>deliverProject(s,'PJ-2','PJ-2-B',{'0:8':1}),/首次选定/);assert.throws(()=>deliverProject(s,'PJ-2','PJ-2-B',{'0:3':3}),/每种交6只/);
  assert.throws(()=>completeProjectStage(s,'PJ-2','PJ-2-B'),/还需交付/);check(s);
  deliverProject(s,'PJ-2','PJ-2-B',{'0:3':2,'0:4':6});completeProjectStage(s,'PJ-2','PJ-2-B');
  assert.equal(alternativeUnlocked(s,REGIONAL.alternatives.find(a=>a.id==='ALT-R')),false);
  completeProjectStage(s,'PJ-2','PJ-2-C');assert.equal(s.cp,cp-500);
  assert.ok(s.expansion.methods.full.includes('ALT-R'));assert.equal(alternativeUnlocked(s,REGIONAL.alternatives.find(a=>a.id==='ALT-R')),true);
  assert.ok(!regionalAlternativeInfo(s,'ALT-R').missing.some(x=>/项目/.test(x)));
  assert.deepEqual(s.expansion.facts.projectDeliveries['PJ-2-B'],{'0:3':6,'0:4':6});assert.equal(s.expansion.facts.payments['PJ-2-C'],400);check(s);
});

test('deliveries use only free stock, keep one by default and never touch business S',()=>{
  const s=base();identify(s,77,78);cards(s,'R-S1','R-S2');collect(s,'0:134','1:71','0:135','0:136');completeProjectStage(s,'PJ-2','PJ-2-A');
  openBusiness(s,{menuId:'MN1',stock:{'0:3':15}},NOW);
  assert.throws(()=>deliverProject(s,'PJ-2','PJ-2-B',{'0:3':6},{choice:['0:3','0:4']}),/自由库存不足/);
  s.farm['0:4']=6;assert.throws(()=>deliverProject(s,'PJ-2','PJ-2-B',{'0:4':6},{choice:['0:4','0:8']}),/留1只/);
  deliverProject(s,'PJ-2','PJ-2-B',{'0:4':6},{choice:['0:4','0:8'],overrideKeepOne:true});assert.equal(s.farm['0:4'],0);check(s);
});

test('PJ-3: the bake-leaf event, 36 tea/snack sold or delivered across three kinds plus one RG3 stage, then 600 CP',()=>{
  const s=base();s.total['0:0']=6000;syncProgress(s);assert.equal(projectInfo(s,'PJ-3').gateMet,true);
  assert.equal(stage(s,'PJ-3','PJ-3-A').checksMet,false);cards(s,'T-E1');completeProjectStage(s,'PJ-3','PJ-3-A');
  const tea=REGIONAL.selectors.tea,snack=REGIONAL.selectors.snack.filter(k=>!tea.includes(k));
  s.expansion.facts.businessCounts={[tea[0]]:10,[snack[0]]:10};s.expansion.facts.orderCounts={[tea[1]]:15};
  assert.equal(stage(s,'PJ-3','PJ-3-B').checksMet,false,'35 is not 36; RG3 not yet');
  s.expansion.facts.orderCounts[tea[1]]=16;assert.equal(PROJECT_RULES['PJ-3-B'].check(s)[0].met,true,'business and purchases add up here');
  assert.equal(stage(s,'PJ-3','PJ-3-B').checksMet,false);
  s.expansion.regulars.RG3={readStages:[],pendingStage:{id:'RG3-1',seq:1,branch:0},activatedSeq:null,baselines:{},lastVisit:null};
  assert.equal(stage(s,'PJ-3','PJ-3-B').ready,true);completeProjectStage(s,'PJ-3','PJ-3-B');
  const cp=s.cp;completeProjectStage(s,'PJ-3','PJ-3-C');assert.equal(s.cp,cp-600);
});

test('PJ-4: every region six species and two specimen/lore cards (400), four menus plus 48 birds in four signature groups (600), then 1000',()=>{
  const s=base();s.total['0:0']=9000;syncProgress(s);assert.equal(projectInfo(s,'PJ-4').gateMet,false,'bay closed');
  s.expansion.regions.guideFlags=['GUIDE-B'];assert.equal(projectInfo(s,'PJ-4').gateMet,true);
  for(const r of ['V','R','T','B']){collect(s,...REGIONAL.species.filter(c=>c.region===r).slice(0,6).map(c=>c.key));}
  cards(s,'V-S1','V-N1','R-S1','R-N1','T-S1','T-E1');assert.equal(stage(s,'PJ-4','PJ-4-A').checksMet,false,'events do not replace specimen/lore');
  cards(s,'T-N1','B-S1','B-N1');completeProjectStage(s,'PJ-4','PJ-4-A');
  const allowed=REGIONAL.projects[3].stages[1].consume.allowed,byCategory={};for(const k of allowed){const c=SPECIES_TRADE[k]?.category;if(c&&resolveSpecies(k).edible&&!byCategory[c])byCategory[c]=k;}
  const picks=Object.values(byCategory).slice(0,4);assert.equal(picks.length,4);for(const k of picks){s.farm[k]=60;s.total[k]=(s.total[k]??0)+60;}
  assert.throws(()=>deliverProject(s,'PJ-4','PJ-4-B',{[picks[0]]:12}),/有效接待/);service(s,'MN1','MN2','MN3','MN4');
  assert.throws(()=>deliverProject(s,'PJ-4','PJ-4-B',{[picks[0]]:46}),/至少要有4类/);
  deliverProject(s,'PJ-4','PJ-4-B',{[picks[0]]:30,[picks[1]]:6});deliverProject(s,'PJ-4','PJ-4-B',{[picks[2]]:6,[picks[3]]:6});
  assert.equal(stage(s,'PJ-4','PJ-4-B').delivery.full,true);const cp=s.cp;completeProjectStage(s,'PJ-4','PJ-4-B');assert.equal(s.cp,cp-600);
  completeProjectStage(s,'PJ-4','PJ-4-C');assert.equal(s.cp,cp-1600);assert.equal(projectComplete(s,'PJ-4'),true);check(s);
  assert.equal(REGIONAL.mementos.length,12,'no 13th memento');
});

test('PJ-4 portraits accept any recorded species (old ornamentals too) and consume nothing',()=>{
  const s=base();s.total['0:0']=9000;s.expansion.regions.guideFlags=['GUIDE-B'];syncProgress(s);
  const ornamental=LEGACY193.characters[0].find(c=>!resolveSpecies(`0:${c.id}`).edible);assert.ok(ornamental);
  const farm=structuredClone(s.farm),cp=s.cp;setProjectPortraits(s,['0:0',`0:${ornamental.id}`]);
  assert.deepEqual(s.farm,farm);assert.equal(s.cp,cp);assert.deepEqual(s.expansion.projects['PJ-4'].pinnedChoices.portraits,['0:0',`0:${ornamental.id}`]);
  assert.throws(()=>setProjectPortraits(s,['0:150']),/已经收录/);assert.throws(()=>setProjectPortraits(s,Array.from({length:13},(_,i)=>`0:${i}`)));
  setProjectPortraits(s,[]);assert.equal(s.expansion.projects['PJ-4'].pinnedChoices.portraits,undefined);check(s);
  assert.equal(stage(s,'PJ-4','PJ-4-B').delivery.total,0,'portraits are not food deliveries');
});

test('insufficient CP changes nothing; a lost acknowledgement replays without paying twice',()=>{
  let s=base();s.cp=150;const before=structuredClone(s);
  assert.throws(()=>completeProjectStage(s,'PJ-1','PJ-1-C'),/先完成前一阶段/);
  completeProjectStage(s,'PJ-1','PJ-1-A');service(s,'MN1','MN3');completeProjectStage(s,'PJ-1','PJ-1-B');
  const snapshot=structuredClone(s);assert.throws(()=>completeProjectStage(s,'PJ-1','PJ-1-C'),/CP不足/);assert.deepEqual(s,snapshot);void before;
  s.cp=500;s=E.normalizeSave(s,NOW);
  const paid=execute({state:s,now:NOW,command:{type:'project:complete',stageId:'PJ-1-C'},reduce:d=>completeProjectStage(d,'PJ-1','PJ-1-C')});
  const replay=execute({state:paid.state,commandId:paid.state.meta.lastCommit.commandId,now:NOW,command:{type:'project:complete',stageId:'PJ-1-C'},reduce:()=>assert.fail('replayed')});
  assert.equal(replay.replayed,true);assert.equal(replay.state.cp,300);
  assert.throws(()=>execute({state:paid.state,now:NOW,command:{type:'project:complete',stageId:'PJ-1-C'},reduce:d=>completeProjectStage(d,'PJ-1','PJ-1-C')}),/已经完成/);
});

test('migration leaves projects empty; checks are retroactive but nothing is ever assumed delivered or paid',()=>{
  const s=base();assert.deepEqual(s.expansion.projects,{});service(s,'MN1','MN3');
  const m=projectsModel(s);assert.ok(m.rows[0].stages[1].checks.filter(c=>c.text!=='前一阶段已完成').every(c=>c.met),'the menu check is retroactive');assert.equal(m.rows[0].progress,0);
  assert.ok(m.visible.some(r=>r.recommended),'one locked project is recommended, not every empty board');
});

test('the projects validator rejects skipped stages, wrong payments, over-delivery, unlocked kinds and early presets',()=>{
  const s=base();identify(s,77,78);cards(s,'R-S1','R-S2');collect(s,'0:134','1:71','0:135','0:136');completeProjectStage(s,'PJ-2','PJ-2-A');deliverProject(s,'PJ-2','PJ-2-B',{'0:3':6},{choice:['0:3','0:4']});check(s);
  const cases=[p=>p.stages['PJ-2-C']={complete:true,seq:1},p=>p.payments['PJ-2-A']=99,p=>delete p.payments['PJ-2-A'],p=>p.deliveries['PJ-2-B']['0:3']=7,
    p=>p.deliveries['PJ-2-B']['0:8']=1,p=>p.pinnedChoices['PJ-2-B']=['0:3'],p=>p.stages['PJ-2-B']={complete:true,seq:1},p=>p.extra=1,p=>p.pinnedChoices.portraits=['0:0']];
  for(const mutate of cases){const bad=structuredClone(s);mutate(bad.expansion.projects['PJ-2']);assert.throws(()=>check(bad));}
  const early=structuredClone(s);early.expansion.menus.presets=[{menuId:'MN1',stock:{'0:0':6}}];assert.throws(()=>check(early),/预设/);
});
