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
  return {mode:plan.mode,title:`地区配方 · ${label}`,lines:[`每枚 ${Math.round(plan.chance*100)}% · 一锅平均 ${(24*plan.chance).toFixed(1)} 只`,'每枚独立抽取，同锅可出多只；首次与再次制作概率相同。','保持清洁并及时收取，其他伙伴来自普通配方。']};
}
