// Explains which guarantee strategy owns the next batch. It reads the same
// BatchPlan as execution, so the confirmation never promises a different mode.
import {resolveSpecies,CONTENT_TEXT} from './content-registry.js';
import {speciesDiscovered} from './species-state.js';

const code=key=>{const [egg,id]=key.split(':').map(Number);return `${egg?'D':'C'}${String(id+1).padStart(3,'0')}`;};
export function batchModeView(s,plan){
  if(!plan||plan.mode==='legacy')return null;
  const [egg,id]=plan.key.split(':').map(Number),known=speciesDiscovered(s,egg,id),label=known?resolveSpecies(plan.key).title_zh_CN:code(plan.key);
  if(plan.mode==='local-alternative')return {mode:plan.mode,title:`地方替代做法 · ${CONTENT_TEXT[plan.recipeId]?.name??plan.recipeId}`,
    lines:[`沿用${label}原配方的候选与概率，不额外安排目标。`,'地区材料只替换原配料，候选不变；本批不叠加地区试做、四时、礼物或点心保证。']};
  const trial=s.expansion.trial[plan.recipeId]??{failedFullBatches:0,owed:false};
  const guarantee=plan.mode==='regional-repeat'?'已收录：本批安排1只，其余23只来自原配方。':plan.guaranteed?(trial.owed?'上一只安排后未能收取：本批继续安排1只。':'连续3批未收录：本批安排1只。'):`本批有25%机会安排1只；已连续${trial.failedFullBatches}/3批未收录，第4批必定安排。`;
  return {mode:plan.mode,title:`地区试做 · ${label}`,lines:[guarantee,'安排不等于收录：保持清洁并及时收取，病变或过熟不会清除保护。','其余伙伴来自原配方候选；本批不叠加四时、礼物或点心保证。']};
}
