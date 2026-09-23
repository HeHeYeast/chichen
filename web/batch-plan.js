import {REGIONAL,resolveRecipeId,resolveSpecies} from './content-registry.js';
import {regionalRecipeInfo,regionalAlternativeInfo} from './regional-methods.js';
import {cookingIngredients} from './cooking-query.js';
import {speciesDiscovered} from './species-state.js';
import {sampleLegacyCompanions,previewLegacyCompanions} from './legacy-recipe-adapter.js';
import {plannedSeasonalRecipe,seasonalSurprise} from './seasonal-pack.js';
import {randomUnit} from './progression.js';
import {reduceFacts} from './facts.js';
import {assertNewOperation} from './rollback-policy.js';

// One pure plan decides which guarantee strategy owns the batch.
export function buildBatchPlan(s,toolId,now){
  const materials=cookingIngredients(s),intent=s.expansion?.prepareMode;
  if(intent?.kind==='local-alternative'){
    const info=regionalAlternativeInfo(s,intent.recipeId),r=info.recipe;
    if(!info.met)throw Error(info.missing.join('；'));
    if(s.egg!==info.egg||toolId!==r.toolId||JSON.stringify([...s.selected].sort((a,b)=>a-b))!==JSON.stringify([...info.materials].sort((a,b)=>a-b))||materials.length!==info.materials.length)throw Error('地方替代做法需要精确的蛋种、厨具与材料，不能额外放料。');
    if(s.events.seasonalRecipe||s.progress.replicate||materials.some(id=>[68,69,70].includes(id)))throw Error('请只选择一种调理模式。');
    return {version:1,mode:'local-alternative',recipeId:r.id,key:r.target,egg:info.egg,toolId,materials,legacyMaterials:info.legacyMaterials,guaranteed:false};
  }
  if(intent?.kind==='regional'){
    const info=regionalRecipeInfo(s,intent.recipeId),r=info.recipe;
    assertNewOperation('region',r?.region??resolveSpecies(r?.key)?.region??null);
    if(!info.met)throw Error(info.missing.join('；'));
    const expected=r.ingredients.map(x=>x.id).sort((a,b)=>a-b),actual=[...s.selected].sort((a,b)=>a-b);
    if(s.egg!==r.egg||toolId!==r.toolId||JSON.stringify(expected)!==JSON.stringify(actual)||materials.length!==expected.length)throw Error('本批蛋种、厨具或材料与地区做法不一致，请按完整做法准备，不能额外放料。');
    if(s.events.seasonalRecipe||s.progress.replicate||materials.some(id=>[68,69,70].includes(id)))throw Error('请只选择一种调理模式，再确认开火。');
    const known=speciesDiscovered(s,r.egg,Number(r.key.split(':')[1])),trial=s.expansion.trial[r.id];
    return {version:1,mode:known?'regional-repeat':'regional-trial',recipeId:r.id,key:r.key,egg:r.egg,toolId,materials,legacyMaterials:materials.filter(id=>id<75),guaranteed:known||!!trial?.owed||(trial?.failedFullBatches??0)>=3,chance:.25};
  }
  if(intent)throw Error('这条地方做法尚未开放。');
  return {version:1,mode:'legacy',egg:s.egg,toolId,materials,legacyMaterials:materials.filter(id=>id<75),guaranteed:false};
}
export function sampleBatchPlan(s,plan,now,random){
  const regional=plan.mode.startsWith('regional'),result=sampleLegacyCompanions(s,plan,now,random);
  let targetScheduled=false,roll=null,seasonal=null;
  if(regional){
    if(!plan.guaranteed)roll=randomUnit(random);
    targetScheduled=plan.guaranteed||roll<plan.chance;
    if(targetScheduled)result[0]=Number(plan.key.split(':')[1]);
  }else if(plan.mode==='legacy'){
    seasonal=plannedSeasonalRecipe(s,plan.toolId,plan.materials);
    if(seasonal)result[0]=seasonal.id;
    else{const surprise=seasonalSurprise(s,plan.toolId,plan.materials,random);if(surprise)result[0]=surprise.id;}
    if(s.progress.replicate)result[0]=Number(s.progress.replicate.split(':')[1]);
  }
  return {result,seasonal,ticket:{version:1,mode:plan.mode,recipeId:plan.recipeId??null,targetKey:plan.key??null,targetScheduled,roll,initialIds:[...result]}};
}
export function recordBatchStarted(s,ticket){
  if(!ticket.mode.startsWith('regional'))return;
  const t=s.expansion.trial[ticket.recipeId]??={failedFullBatches:0,owed:false,attemptSeq:0};t.attemptSeq++;
  if(ticket.targetScheduled)t.owed=true;
}
export function recordRegionalHarvest(s,batch,actualKey){
  const ticket=batch.plan;if(!ticket)return;
  if(batch.eggs.every(e=>e.collected)&&!ticket.finished){const used=s.expansion.regions.materialUse??={};for(const id of batch.ingredients)if(id>=75)used[id]=true;}
  if(!ticket.mode.startsWith('regional')){if(batch.eggs.every(e=>e.collected))ticket.finished=true;return;}
  const trial=s.expansion.trial[ticket.recipeId];
  if(actualKey===ticket.targetKey){trial.failedFullBatches=0;trial.owed=false;}
  if(batch.eggs.every(e=>e.collected)&&!ticket.finished){
    ticket.finished=true;
    if(batch.ingredients.includes(79))reduceFacts(s,[{kind:'regionalMaterialBatch',batchId:String(batch.started),materialId:79}]);
    if(!speciesDiscovered(s,...ticket.targetKey.split(':').map(Number)))trial.failedFullBatches=Math.min(3,trial.failedFullBatches+1);
    const used=s.expansion.regions.materialUse??={};for(const id of batch.ingredients)if(id>=75)used[id]=true;
  }
}
export function regionalPreview(s,toolId,now){
  const plan=buildBatchPlan(s,toolId,now),pool=previewLegacyCompanions(s,plan,now);
  return {plan,candidates:[...new Set(pool)].map(id=>({key:`${s.egg}:${id}`,guaranteed:0,status:'possible'})).concat(plan.mode.startsWith('regional')?[{key:plan.key,guaranteed:plan.guaranteed?1:0,status:plan.guaranteed?'guaranteed':'encounter'}]:[])};
}
export function validateBatchPlan(s,fail){
  const b=s.batch,p=b.plan;
  if(!p||p.version!==1||!['legacy','regional-trial','regional-repeat','local-alternative'].includes(p.mode)||typeof p.targetScheduled!=='boolean'||typeof p.finished!=='boolean')fail('调理计划');
  if(!Array.isArray(p.initialIds)||p.initialIds.length!==24||p.initialIds.some(id=>!resolveSpecies(`${b.egg}:${id}`)))fail('原始调理票据');
  for(const e of b.eggs)if(!Array.isArray(e.tickets)||e.tickets.length!==6||e.tickets.some(v=>!Number.isFinite(v)||v<0||v>=1))fail('变化概率票据');
  if(p.mode==='legacy'){
    if(p.recipeId!==null||p.targetKey!==null||p.targetScheduled||p.roll!==null)fail('普通调理保证冲突');
    if(p.initialIds.some(id=>id>=(b.egg?65:128)))fail('普通调理旧身份边界');
  }else if(p.mode==='local-alternative'){
    const r=resolveRecipeId(p.recipeId);
    if(!r||r.mode!=='local-alternative'||r.target!==p.targetKey||Number(r.target.split(':')[0])!==b.egg||r.toolId!==b.tool||p.targetScheduled||p.roll!==null||p.initialIds.some(id=>id>=(b.egg?65:128)))fail('替代做法票据');
    if(b.level<r.toolLevel||b.rules.kitchenLevel<r.kitchenLevel||JSON.stringify([...b.ingredients].sort((a,b)=>a-b))!==JSON.stringify([...r.ingredients].sort((a,b)=>a-b)))fail('替代做法精确材料');
  }else{
    const r=resolveRecipeId(p.recipeId);
    if(!r||r.mode!=='regional-trial'||r.key!==p.targetKey||r.egg!==b.egg||r.toolId!==b.tool||!s.expansion.trial[r.id])fail('地区调理身份');
    if(p.roll!==null&&(!Number.isFinite(p.roll)||p.roll<0||p.roll>=1))fail('试做票据');
    if(p.roll===null&&!p.targetScheduled||p.roll!==null&&p.targetScheduled!==(p.roll<.25)||p.mode==='regional-repeat'&&(!p.targetScheduled||p.roll!==null))fail('地区命中票据');
    if(b.level<r.toolLevel||b.rules.kitchenLevel<r.kitchenLevel||JSON.stringify([...b.ingredients].sort((a,b)=>a-b))!==JSON.stringify(r.ingredients.map(x=>x.id).sort((a,b)=>a-b)))fail('地区精确配方');
    if(p.initialIds.some(id=>id!==Number(p.targetKey.split(':')[1])&&id>=(b.egg?65:128)))fail('地区旧伴随边界');
    if(p.targetScheduled&&p.initialIds.filter(id=>id===Number(p.targetKey.split(':')[1])).length!==1)fail('地区唯一目标');
    if(!p.targetScheduled&&p.initialIds.some(id=>id===Number(p.targetKey.split(':')[1])))fail('地区未命中目标');
    if(p.finished!==b.eggs.every(e=>e.collected))fail('地区收锅状态');
  }
}
