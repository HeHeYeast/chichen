from pathlib import Path
p=Path('web/progress-save.js');s=p.read_text(encoding='utf-8').replace("import {earnedSources,skillPoints,skillGate}","import {earnedSources,skillPoints,skillGate,SKILL_BY_ID,TRADE_CATEGORIES}")
s=s.replace('return {sources:earnedSources(s),skills:{},','return {skillVersion:2,migrationRespec:false,migrationNotice:false,leftovers:[],hotStove:null,lastHarvest:null,sources:earnedSources(s),skills:{},')
s=s.replace('rebateRemainder:0}', 'rebateRemainder:0,markupRemainder:0,category:null}')
s=s.replace('sickness:true}', 'sickness:true,calm:false}')
s=s.replace("for(const [id,lv]of Object.entries(p.skills)){int(lv,'技能等级',1,3);if(skillGate(s,id,lv))fail('技能前置 '+id);}","for(const [id,lv]of Object.entries(p.skills)){int(lv,'技能等级',1,1);if(skillGate(s,id,lv))fail('技能前置 '+id);}\n  int(p.skillVersion,'手艺版本',2,2);bool(p.migrationRespec,'迁移重配');bool(p.migrationNotice,'迁移提示');\n  if(!Array.isArray(p.leftovers)||p.leftovers.length>5||p.leftovers.some(id=>!Number.isInteger(id)||id<0||id>74||[68,69,70].includes(id)))fail('待收余料');\n  if(p.hotStove!==null){object(p.hotStove,'接锅记录');num(p.hotStove.at,'接锅时间');if(typeof p.hotStove.signature!=='string')fail('接锅配方');}\n  if(p.lastHarvest!==null){object(p.lastHarvest,'收成摘要');int(p.lastHarvest.base,'基础收成',24,24);int(p.lastHarvest.bonus,'收成奖励',0,24);}")
s=s.replace("bool(p.protection.sickness,'病变开关');", "bool(p.protection.sickness,'病变开关');bool(p.protection.calm,'安心模式');")
s=s.replace("if(p.skills['TRADE-B']&&!p.trade.initialGranted)","int(p.trade.markupRemainder,'招牌小数',0,99);if(p.trade.category!==null&&!TRADE_CATEGORIES.includes(p.trade.category))fail('招牌分类');\n  if((p.skills['TRADE-3']||p.skills['TRADE-4'])&&!p.trade.initialGranted)")
s=s.replace("if(t.endAt-t.startedAt!==r.hours*3600000)","if(t.endAt-t.startedAt!==r.hours*3600000*(t.snapshot?.light?.8:1))")
s=s.replace("if(t.status==='running'&&t.remaining.length!==r.baseUnits+(x.bonus?1:0))", "if(x.light!==undefined){bool(x.light,'轻装快照');if(x.light&&r.id==='yard')fail('菜园轻装');}\n    if(t.cpReward!==undefined){int(t.cpReward,'寻访CP',0,r.baseUnits*6);bool(t.cpProcessed,'寻访CP结算');if(t.status==='running'&&t.cpProcessed)fail('提前发放寻访CP');}\n    if(t.status==='running'&&t.remaining.length!==r.baseUnits-(x.light?1:0)+(x.bonus?1:0))")
s=s.replace("if(!Object.keys(RULES.skillCosts).includes(id.split('-')[1])||!['CUL','HOME','TRADE','OBS','TRIP'].includes(id.split('-')[0])||!Number.isInteger(lv)||lv<1||lv>RULES.skillCosts[id.split('-')[1]].length)","if(!(SKILL_BY_ID[id]&&lv===1)&&(!Array.isArray(RULES.skillCosts[id.split('-')[1]])||!['CUL','HOME','TRADE','OBS','TRIP'].includes(id.split('-')[0])||!Number.isInteger(lv)||lv<1||lv>RULES.skillCosts[id.split('-')[1]].length))")
s=s.replace("int(b.version,'调理规则版本',1,1)","int(b.version,'调理规则版本',1,2)")
s=s.replace("bool(b.protectSickness,'防病选项');}","bool(b.protectSickness,'防病选项');if(b.version===2){bool(b.calm,'安心批次');bool(b.pickGold,'拾金快照');bool(b.returnEligible,'返料资格');bool(b.finished,'批次结算');int(b.harvestBonus,'拾金总计',0,24);if(b.returnTicket!==null)int(b.returnTicket,'返料票据',0,74);for(const egg of s.batch.eggs)bool(egg.gold,'拾金票据');}}")
s+='''
// Old skill allocations are refunded once; clocks, knowledge, rewards and active snapshots survive unchanged.
export function migrateSkills(s,fail){
 const p=s.progress;if(p.skillVersion===2)return;
 if(p.skillVersion!==undefined)fail('未知手艺版本');
 let spent=0;
 for(const [id,lv]of Object.entries(p.skills??{})){
  const [b,n]=id.split('-'),costs=RULES.skillCosts[n];
  if(!['CUL','HOME','TRADE','OBS','TRIP'].includes(b)||!Array.isArray(costs)||!Number.isInteger(lv)||lv<1||lv>costs.length)fail('旧手艺节点');
  spent+=costs.slice(0,lv).reduce((a,b)=>a+b,0);
 }
 if(spent>Object.values(earnedSources(s)).reduce((a,b)=>a+b,0))fail('旧手艺透支');
 p.skillVersion=2;p.skills={};p.migrationRespec=true;p.migrationNotice=true;
 p.leftovers=[];p.hotStove=null;p.lastHarvest=null;p.protection.calm=false;p.trade.markupRemainder=0;p.trade.category=null;
}
'''
p.write_text(s,encoding='utf-8')
p=Path('web/engine.js');s=p.read_text(encoding='utf-8').replace('freshProgress,validateProgress','freshProgress,validateProgress,migrateSkills').replace('basketQuote,checkedIncome','basketQuote,checkedIncome,rank,randomUnit,batchSignature,cookingTiming')
s=s.replace("import {availableCount,validateConsumption}","import {cookingCandidates} from './candidate-query.js';\nimport {TRADE_SPECIES} from './trade-data.js';\nimport {clueCandidates} from './knowledge.js';\nimport {RULES} from './integration-data.js';\nimport {availableCount,validateConsumption}")
a=s.index('export function cookInfo(');b=s.index('export function startBatch',a)
s=s[:a]+'''export function cookInfo(s,id,now=Date.now()) {
 const t=tool(id),lv=s.toolLevels[id];
 if(!t||!Number.isInteger(lv)||lv<0||lv>2)throw Error('请先在商店购买调理用具。');
 const signature=batchSignature(s.egg,id,cookingIngredients(s));
 return {cost:t[`lv_${lv}_cook_cp`]+(s.progress?.replicate?10:0),...cookingTiming(s,t[`lv_${lv}_min`],{signature,now})};
}
export function replicateOptions(s,id,now=Date.now()){
 const preview=cookingCandidates(s,id,now);
 if(preview.candidates.some(c=>c.guaranteed)||plannedSeasonalRecipe(s,id,preview.ingredients))return [];
 return preview.candidates.filter(c=>c.known&&c.status==='possible'&&TRADE_SPECIES[c.key]?.replicable);
}
'''+s[b:]
s=s.replace('const info=cookInfo(s,id),{cost,minutes}=info;', 'const info=cookInfo(s,id,now),{cost,minutes}=info;\n  const replicate=s.progress.replicate;if(replicate&&(!rank(s,\'CUL-5\')||!replicateOptions(s,id,now).some(c=>c.key===replicate)))throw Error(\'指定伙伴不符合本批普通候选，请重新选择\');')
s=s.replace('const duration=Math.ceil(minutes*60000);',"if(replicate)result[0]=Number(replicate.split(':')[1]);\n  const returnPool=selected.filter(i=>![68,69,70].includes(i)),returnEligible=!!rank(s,'CUL-3')&&s.progress.leftovers.length<5;\n  const returnTicket=returnEligible&&returnPool.length&&randomUnit(random)<.2?returnPool[Math.floor(randomUnit(random)*returnPool.length)]:null;\n  const pickGold=!!rank(s,'CUL-1');\n  const duration=Math.ceil(minutes*60000);")
s=s.replace("collected:false,status:'egg'", "gold:pickGold&&randomUnit(random)<.15,collected:false,status:'egg'")
s=s.replace('rules:{version:1,originalMinutes', 'rules:{version:2,calm:info.calm,pickGold,returnEligible,returnTicket,finished:false,harvestBonus:0,originalMinutes')
s=s.replace('freshness:s.progress.protection.freshness','freshness:info.freshness')
s=s.replace('delete s.events.seasonalRecipe;\n  return s.batch;', 'delete s.events.seasonalRecipe;delete s.progress.replicate;s.progress.hotStove=null;\n  return s.batch;')
s=s.replace('export function collect(s,index) {', 'export function collect(s,index,now=Date.now()) {')
s=s.replace('wasKnown=speciesDiscovered(s,e.egg,e.id);checkedIncome(s,1);', 'wasKnown=speciesDiscovered(s,e.egg,e.id),bonus=s.batch.rules?.pickGold&&e.gold?1:0;checkedIncome(s,1+bonus);')
s=s.replace('s.cp+=1;', 's.cp+=1+bonus;\n  if(s.batch.rules?.version===2)s.batch.rules.harvestBonus+=bonus;')
s=s.replace('syncProgress(s);}\n  return true;', '''syncProgress(s);
    if(!wasKnown&&rank(s,'OBS-5')){
      const clues=RULES.exploration.routes.flatMap(r=>clueCandidates(s,r,now)).sort((a,b)=>a.level-b.level);
      if(clues.length){s.progress.knowledge.facts.push(clues[0].fact);s.progress.discoveryClue=clues[0].key;}
    }
    const b=s.batch,r=b.rules;
    if(r?.version===2&&!r.finished&&b.eggs.every(e=>e.collected)){
      r.finished=true;s.progress.hotStove={signature:batchSignature(b.egg,b.tool,b.ingredients),at:Math.max(now,s.progress.logicalAt)};
      if(r.returnTicket!==null){const room=30-Object.values(s.ingredients).reduce((a,b)=>a+b,0);if(room>0)s.ingredients[r.returnTicket]=(s.ingredients[r.returnTicket]??0)+1;else s.progress.leftovers.push(r.returnTicket);}
      s.progress.lastHarvest={base:24,bonus:r.harvestBonus,returned:r.returnTicket};
    }
  }
  return true;''')
s=s.replace('const quote=basketQuote(s,selection),income=baseIncome+quote.bonus;', 'const quote=basketQuote(s,selection,options),income=quote.income;')
s=s.replace('if(s.progress)s.progress.trade.credits-=quote.baskets;', 'if(s.progress){s.progress.trade.credits-=quote.creditsUsed;s.progress.trade.markupRemainder=quote.markupRemainder;}')
s=s.replace('validateProgress(normalized,fail);','migrateSkills(normalized,fail);\n  validateProgress(normalized,fail);')
p.write_text(s,encoding='utf-8')
