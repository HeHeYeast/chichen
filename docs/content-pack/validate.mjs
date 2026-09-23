// Static content audit and narrowly scoped batch arithmetic. No state, web, or Android writes.
import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import {fileURLToPath} from 'node:url';
import {GAME_DATA as D,EXPANSION} from '../../web/content-pack.js';
import {originalRecipePlan} from '../../web/recipes.js';
import {availableIngredientIds} from '../../web/ingredient-unlocks.js';
const dir=path.dirname(fileURLToPath(import.meta.url));
const c=JSON.parse(fs.readFileSync(path.join(dir,'content.json'))),b=JSON.parse(fs.readFileSync(path.join(dir,'baseline.json')));
const failures=[],checks=[];
function check(name,condition,detail=''){checks.push({name,pass:!!condition,detail});if(!condition)failures.push(name+': '+detail);}
const everyone=[...b.species.map(s=>({...s,id:s.key})),...c.species],get=id=>everyone.find(s=>s.id===id);
const names=new Set(),keys=new Set();
check('现行193及新总量241',D.characters.flat().length===193&&c.species.length===48&&everyone.length===241);
check('24鸡24鸭',c.species.filter(s=>s.egg===0).length===24&&c.species.filter(s=>s.egg===1).length===24);
for(const r of c.regions)check(`${r.id}每区6鸡6鸭`,[0,1].every(e=>c.species.filter(s=>s.region===r.id&&s.egg===e).length===6));
for(const s of everyone){check('唯一身份 '+s.id,!keys.has(s.key));keys.add(s.key);check('唯一名称 '+s.name,!names.has(s.name));names.add(s.name);check('G/F与特征 '+s.id,s.exploration.G+s.exploration.F===6&&[2,3,4].includes(s.exploration.G)&&s.exploration.traits.length<=2);}
check('旧鸡鸭ID保持不变',b.species.every(s=>D.characters[s.egg].some(x=>s.key===`${s.egg}:${x.id}`&&x.title_zh_CN===s.name)));
check('新ID连续追加',c.species.every(s=>{const [e,n]=s.key.split(':').map(Number);return e===s.egg&&(e===0?n>=128&&n<=151:n>=65&&n<=88);}));
const exact=r=>`${r.egg??''}|${r.toolId}|${[...(r.ingredients??[])].map(i=>typeof i==='number'?i:i.id).sort((a,b)=>a-b).join(',')}`;
const oldRecipes=b.species.flatMap(s=>s.recipes),collisions=[],subsets=[];
for(const s of c.species){
 check('完整字段 '+s.id,['name','workName','description','clue','art','recipe','unlock','signature','tags','exploration','menus','collections','relatedCards','orders','regulars','projects','uses'].every(k=>Object.hasOwn(s,k))&&s.description.length>=25&&Object.values(s.art).every(Boolean));
 check('至少两项跨系统用途 '+s.id,new Set(s.uses.map(u=>u.system)).size>=2);
 check('非图鉴孤岛 '+s.id,s.edible?s.menus.length>0&&s.orders.length>0:s.uses.some(u=>u.system==='exploration')&&s.orders.includes('O04'));
 check('可追寻的完整入口 '+s.id,!!s.recipe.id&&s.unlock.fullMethodRequired&&s.recipe.hardAttempt===4&&s.recipe.repeatGuaranteed===1&&s.unlock.minimumKitchen===s.recipe.kitchenLevel);
 check('合法材料和厨具 '+s.id,s.recipe.ingredients.every(i=>i.quantity===1&&[...b.ingredients,...c.materials].some(m=>m.id===i.id))&&s.recipe.ingredients.length<=3&&s.recipe.toolLevel>=1&&s.recipe.toolLevel<=3);
 check('不碰神社媒介/鸭蒸笼 '+s.id,!s.recipe.ingredients.some(i=>[68,69,70].includes(i.id))&&!(s.egg&&s.recipe.toolId===8));
 check('合法唯一招牌与食用 '+s.id,s.edible?['家常','煎炸','炖煮','烘焙','蒸点','茶饮'].includes(s.signature):s.signature===null&&s.menus.length===0);
 const key=exact({...s.recipe,egg:s.egg});const same=oldRecipes.filter(r=>exact(r)===key);
 if(same.length)collisions.push({id:s.id,old:same.map(r=>`${r.egg}:${r.id}`)});
 const subset=oldRecipes.filter(r=>r.kind==='pool'||r.kind==='steamer').filter(r=>r.egg===s.egg&&r.toolId===s.recipe.toolId&&r.ingredients.length>0&&r.ingredients.every(x=>s.recipe.ingredients.some(i=>i.id===x)));
 if(subset.length)subsets.push({id:s.id,old:subset.map(r=>`${r.egg}:${r.id}`),resolution:'显式地区模式；旧材料继续参与普通伴随池，但旧蒸笼/四时/礼物的保证插入互斥。'});
}
check('与193旧配方精确组合零重复',collisions.length===0,JSON.stringify(collisions));
const newRecipeKeys=c.species.map(s=>exact({...s.recipe,egg:s.egg}));check('48新品精确配方唯一',new Set(newRecipeKeys).size===48);
const swapped=[];for(const s of c.species.filter(s=>!s.egg))for(const d of c.species.filter(s=>s.egg))if(exact({...s.recipe,egg:0})===exact({...d.recipe,egg:0}))swapped.push([s.id,d.id]);
check('无仅换蛋种的同工具同料鸡鸭配方',swapped.length===0,JSON.stringify(swapped));
for(const m of c.materials){const uses=c.species.filter(s=>s.recipe.ingredients.some(i=>i.id===m.id));check('材料多用 '+m.name,uses.length>=4&&new Set(uses.map(s=>s.egg)).size===2&&new Set(uses.map(s=>s.recipe.toolId)).size>=2);check('材料入口实物一致 '+m.name,get(m.entrySpecies).recipe.ingredients.some(i=>i.id===m.id)&&c.cards.find(x=>x.id===m.specimen)?.material===m.id);}
check('数量：8材料24卡',c.materials.length===8&&c.cards.length===24);
for(const r of c.regions)check('卡片分配 '+r.id,['specimen','lore','event'].every(t=>c.cards.filter(x=>x.region===r.id&&x.type===t).length===2));
for(const x of c.cards){const team=x.team.oldExamples.map(get);check('旧队伍可解 '+x.id,team.every(Boolean)&&team.length>=1&&team.length<=3&&(!x.team.trait||team.some(s=>s.exploration.traits.includes(x.team.trait)))&&(!x.team.environment||team.some(s=>s.exploration.environment===x.team.environment)));check('发现卡文本和影响 '+x.id,!!x.hint&&!!x.result&&!!x.next&&x.effects.length>0);}
const refs={menus:c.menus,collections:c.collections,orders:c.orders,regulars:c.regulars,projects:c.projects,relatedCards:c.cards,specials:c.specials};
for(const s of c.species)for(const [field,objects] of Object.entries(refs))check('引用 '+s.id+'/'+field,s[field].every(id=>objects.some(o=>o.id===id)),s[field].join(','));
for(const m of c.menus)for(const r of m.roles)check('菜单仅食用品 '+r.id,r.allowed.length>=2&&r.allowed.every(id=>get(id)?.edible));
for(const col of c.collections)check('收藏新旧身份 '+col.id,col.oldKeys.every(id=>b.species.some(s=>s.key===id))&&col.newIds.every(id=>c.species.some(s=>s.id===id)));
for(const o of c.orders){check('采购定义 '+o.id,o.variants.length<=3&&o.variants.length>=1&&o.groups.every(g=>g.allowed.length>=2&&g.allowed.every(id=>get(id)))&&(o.kind==='display'||o.groups.every(g=>g.allowed.every(id=>get(id).edible))));}
function assignments(menu,items){
 const result=[];
 const step=(i,picks)=>{if(i===items.length){if(menu.roles.filter(r=>r.required).every(r=>picks.some(p=>p.role===r.id)))result.push(picks);return;}
  for(const r of menu.roles)if(r.allowed.includes(items[i].id)&&picks.filter(p=>p.role===r.id).length<r.maxSpecies)step(i+1,[...picks,{...items[i],role:r.id}]);};step(0,[]);return result;
}
function complete(menu,items){const ss=items.map(x=>get(x.id)),has=t=>ss.some(s=>s.tags.includes(t)),atLeast=n=>items.every(x=>x.quantity>=n);
 if(!assignments(menu,items).length)return false;
 if(menu.id==='MN1')return items.length>=2&&atLeast(6);
 if(menu.id==='MN2')return items.length>=3&&atLeast(3)&&ss.some(s=>s.tags.includes('portable')&&s.tags.includes('sweet'));
 if(menu.id==='MN3')return has('savory')&&has('sweet')&&atLeast(3);
 if(menu.id==='MN4')return ss.length>=3&&atLeast(3)&&new Set(ss.map(s=>s.egg)).size===2;
 if(menu.id==='MN5')return ss.length>=3&&atLeast(3)&&(has('floral')||has('roast'));
 if(menu.id==='MN6')return ss.length>=3&&atLeast(3)&&(has('ginger')||has('mushroom'));
 if(menu.id==='MN7')return has('bay')&&new Set(ss.map(s=>s.egg)).size===2&&atLeast(3);
 if(menu.id==='MN8')return ss.length>=3&&new Set(ss.map(s=>s.season)).size>=3&&atLeast(3);
 return false;
}
for(const m of c.menus)check('菜单两套完整解 '+m.id,m.examples.length>=2&&m.examples.every(ex=>complete(m,ex)),JSON.stringify(m.examples));
check('四时严格原16手作',c.menus.find(m=>m.id==='MN8').roles.every(r=>r.allowed.length===16&&r.allowed.every(id=>b.species.find(s=>s.key===id)?.season)));
check('全部旧193有真实寻访及展示用途',b.species.every(s=>s.specials.includes('SP-ALL')&&s.participation.includes('PJ-4:optional-display')));
for(const r of c.regions)check('每区至少6旧品参与 '+r.id,r.oldKeys.length>=6&&r.oldKeys.every(id=>b.species.some(s=>s.key===id)));
check('8主题4地区4特殊发现',c.collections.filter(x=>x.kind==='theme').length===8&&c.collections.filter(x=>x.kind==='region').length===4&&c.specials.length===4);
check('经营数量全量',c.menus.length===8&&c.orders.length===12&&c.regulars.length===4&&c.regulars.every(r=>r.stages.length===4&&r.stages.every(s=>s.alternatives.length===2&&s.text.length>=30))&&c.projects.length===4);
check('独立纪念物12件',c.mementos.length===12&&c.mementos.filter(m=>m.source.startsWith('COL')).length===8);
check('24条纸册成果身份与正文',c.paperRecords.length===24&&new Set(c.paperRecords.map(n=>n.id)).size===24&&c.paperRecords.every(n=>n.text&&n.source));
const relationGraph=JSON.parse(fs.readFileSync(path.join(dir,'relation-graph.json'))),nodeIds=new Set(relationGraph.nodes.map(n=>n.id));
check('完整关系图节点唯一',nodeIds.size===relationGraph.nodes.length);
check('完整关系图没有悬空边',relationGraph.edges.every(e=>nodeIds.has(e.from)&&nodeIds.has(e.to)),relationGraph.edges.filter(e=>!nodeIds.has(e.from)||!nodeIds.has(e.to)).map(e=>`${e.from}->${e.to}`).join(','));
check('4区替代方不缩水48',c.alternatives.length===4&&new Set(c.alternatives.map(a=>a.region)).size===4&&c.alternatives.every(a=>b.species.some(s=>s.key===a.target)));
// Conservative technology fixture: every tool legal at the listed kitchen stage is purchased.
// This proves a finite no-new-content-old-gate witness, not that an average player owns all tools.
const supply=[];
for(const s of c.species){const k=s.recipe.kitchenLevel;
 const state={kitchenLevel:k-1,toolLevels:Array.from({length:9},(_,i)=>i===8?k>=2?k-2:-1:i>=6?k===4?2:-1:Math.min(2,k-1)),duck:!!s.egg,total:{'0:0':3000,'0:18':1,...(s.egg?{'1:0':1}:{})},events:{}};
 const available=availableIngredientIds(state),missing=s.recipe.ingredients.filter(i=>i.id<75&&!available.includes(i.id)).map(i=>i.id);
 supply.push({id:s.id,kitchen:k,missing,requiredToolsForOldSupply:s.recipe.oldSupply});
 check('标记阶段有合法供料见证 '+s.id,missing.length===0&&state.toolLevels[s.recipe.toolId]>=s.recipe.toolLevel-1,JSON.stringify(missing));
}
// Dependency graph covers primary acquisition and the sole project-gated alternative.
const graph=[];const add=(id,all=[],any=[])=>graph.push({id,all,any});
add('BASE');for(let k=1;k<=4;k++)add('K'+k,['BASE']);add('DUCK',['BASE']);add('OLD-SUPPLY',['BASE']);add('V',['BASE']);add('R',['BASE','K2']);add('T',['BASE','K2']);add('GUIDE-B',['K3'],[['V-C1','R-C1','T-C1']]);add('B',['GUIDE-B']);
for(const card of c.cards){const deps=[card.region];if(card.type==='specimen'){const m=c.materials.find(m=>m.id===card.material),entry=get(m.entrySpecies);deps.push('K'+entry.recipe.kitchenLevel,'OLD-SUPPLY');}else if(card.id==='V-N2')deps.push('MAT-76');else if(['T-N1','T-E1'].includes(card.id))deps.push('MAT-79');else if(['T-N2','T-E2'].includes(card.id))deps.push('MAT-80');else if(['B-N1','B-E1'].includes(card.id))deps.push('MAT-81');else if(card.id==='B-E2')deps.push('MAT-82');else if(card.id==='R-E1')deps.push('MAT-77');
 add(card.id,deps,card.id==='R-N1'?[['MAT-77','MAT-78']]:card.id==='B-N2'?[['MAT-81','MAT-82']]:[]);}
for(const m of c.materials)add('MAT-'+m.id,[m.specimen]);
for(const s of c.species)add(s.id,[s.region,'K'+s.recipe.kitchenLevel,'OLD-SUPPLY',...(s.egg?['DUCK']:[]),...s.unlock.identifiedMaterials.map(id=>'MAT-'+id),...(s.unlock.card?[s.unlock.card]:[]),...(s.unlock.firstSpecimenForOldOnly?[s.region+'-S1']:[])]);
add('PJ-2',['R-S1','R-S2','R-C1','R-C2','R-C3','R-D2','DUCK']);for(const a of c.alternatives)add(a.id,[a.unlock,'OLD-SUPPLY',...a.ingredients.filter(id=>id>=75).map(id=>'MAT-'+id)]);
let reachable=new Set(),change=true;while(change){change=false;for(const n of graph)if(!reachable.has(n.id)&&n.all.every(x=>reachable.has(x))&&n.any.every(group=>group.some(x=>reachable.has(x)))){reachable.add(n.id);change=true;}}
check('主获取图所有节点可达',graph.every(n=>reachable.has(n.id)),graph.filter(n=>!reachable.has(n.id)).map(n=>n.id).join(','));
const cycles=[];const visit=(id,stack)=>{if(stack.includes(id)){cycles.push([...stack,id]);return;}const n=graph.find(n=>n.id===id);if(!n)return;for(const d of n.all)visit(d,[...stack,id]);};for(const n of graph)visit(n.id,[]);check('强依赖无环',!cycles.length);
// Batch arithmetic for the protected/replicated 1-new + 23-old case. Does not model cleanliness or skills.
const economy=[];
for(const s of c.species){const r=s.recipe,state={kitchenLevel:r.kitchenLevel-1,toolLevels:Array(9).fill(r.toolLevel-1),total:{},events:{}};
 let pool;if(r.toolId===8)pool=EXPANSION.characters.filter(x=>x.minLevel<=r.toolLevel-1&&x.ingredients.every(id=>r.ingredients.some(i=>i.id===id))).flatMap(x=>Array(x.rate).fill(x.id));else pool=originalRecipePlan(state,s.egg,r.toolId,r.ingredients.map(i=>i.id),Date.UTC(2026,8,23,1)).pool;
 const avg=pool.reduce((n,id)=>n+D.characters[s.egg].find(x=>x.id===id).cp_1,0)/pool.length;
 const ingredientCP=r.ingredients.reduce((n,i)=>n+([...b.ingredients,...c.materials].find(m=>m.id===i.id).price??c.materials.find(m=>m.id===i.id).priceCP),0),cookCP=b.tools.find(t=>t.id===r.toolId).cookCP,price=s.priceProposal.baseSaleCP;
 economy.push({id:s.id,baseSaleCP:price,ingredientCP,cookCP,oldMeanSaleCP:+avg.toFixed(4),protectedBatchGrossMargin:+(price+23*avg+24-ingredientCP-cookCP).toFixed(2),failedDiscoveryBatchGrossMargin:+(24*avg+24-ingredientCP-cookCP).toFixed(2),scope:'无技能/无经营加价/原料全购/无维护分摊/无变化；初次失败批24旧，复刻批1新+23旧；不能宣称净收益或14天成长通过'});
}
const priceWarnings=economy.filter(e=>get(e.id).edible&&e.protectedBatchGrossMargin<=0);
check('复刻批基础毛差为正（非净利润）',priceWarnings.length===0,JSON.stringify(priceWarnings));
const materialCoverage=c.materials.map(m=>({material:m.name,count:m.uses.length,chicken:m.uses.filter(id=>!get(id).egg).length,duck:m.uses.filter(id=>get(id).egg).length,tools:m.tools}));
const stageDistribution=[1,2,3,4].map(k=>({kitchen:k,count:c.species.filter(s=>s.recipe.kitchenLevel===k).length,ids:c.species.filter(s=>s.recipe.kitchenLevel===k).map(s=>s.id)}));
const sourceFiles=['web/data.js','web/content-pack.js','web/integration-data.js','web/recipes.js','web/recipe-catalog-data.js','web/ingredient-unlocks.js','web/seasonal-pack.js','web/trade-data.js','web/engine.js','web/shrine.js','docs/gameplay-expansion-design.md','docs/ui-information-architecture.md'];
const hashes=Object.fromEntries(sourceFiles.map(f=>[f,crypto.createHash('sha256').update(fs.readFileSync(path.resolve(dir,'../..',f))).digest('hex')]));
const report={status:failures.length?'FAIL':'PASS',checks:checks.length,failed:failures,scope:'作者内容静态审计；非运行时实现、迁移、真人、最终美术或完整经济验证。',materialCoverage,stageDistribution,exactOldRecipeCollisions:collisions,legacySubsetMatches:subsets,swappedEggDuplicates:swapped,supply,acquisitionGraph:graph,economy,sourceHashes:hashes};
fs.writeFileSync(path.join(dir,'audit.json'),JSON.stringify(report,null,2)+'\n');
const name=id=>get(id)?.name??id;
const table=(h,rs)=>'| '+h.join(' | ')+' |\n| '+h.map(()=>'---').join(' | ')+' |\n'+rs.map(r=>'| '+r.map(x=>String(x).replaceAll('|','／')).join(' | ')+' |').join('\n')+'\n';
const auditMd=`# 内容关系审计\n\n2026-09-23；状态：**${report.status}，${checks.length}项静态检查，${failures.length}失败**。返回[内容总册](../content-expansion-assets.md)。机器细节见[audit.json](audit.json)，全关系见[relation-graph.json](relation-graph.json)。这不是游戏实现测试或最终平衡通过。\n\n## 1. 继承依据与查阅范围\n\n完整读取玩法扩展和UI架构；检查当前193名称、193正文、193能力、75材料、原配方目录、点心6种、四时16种、厨具与材料开放、神社媒介和特殊变化规则。既有点心和四时图集已作视觉对照，全部旧近似品通过名称、描述和规则逐项核查；未把未逐张看过的193张原图声称为已完成逐图美术验收。每个新品的art.contrast列出后续必须并排比对的旧近邻。基线文件摘要保存在机器报告。\n\n## 2. 48个工作名逐项审查\n\n表中保留工作名不表示原样接受外形或配方。完整差异由同一品种档案明确；已针对奶凝、茶蛋、米团、薄饼、芝麻脆片、观赏叶形等邻近组分开大轮廓和料理方式。\n\n${table(['ID','原工作名','正式名','主要区别/处置'],c.species.map(s=>[s.id,s.workName,s.name,s.art.contrast]))}\n## 3. 已发现并直接调整的问题\n\n${table(['问题','本包已采用的处理'],c.decisions.map(d=>[d.issue,d.resolution]))}\n## 4. 九项关系审计\n\n${table(['检查','结果与依据'],[
 ['1 新品只剩图鉴价值','48/48至少两种不同系统用途。40食用品有具体菜单/采购候选和寻访；8观赏品有G/F实用、事件/同行、O04展示及地区展册，不假装可食。'],
 ['2 材料只服务单方','8/8各服务4–6种新品，均覆盖鸡鸭，均跨至少3种厨具；完整统计如下。'],
 ['3 旧品无法参与','193/193进入SP-ALL同行观察与PJ-4画像展出，每种保留G/F与适应的实际贡献。食用旧品另有明确菜单、采购与收藏；四地各有至少6个列名旧品。'],
 ['4 鸡鸭明显换皮','0组同厨具同材料仅换蛋种配方；轮廓对照已人工审查。饭团的三角/双瓣/长枕/长舟、奶砖/奶环、饼片/网饼/折扇不只换头。最终绘制需灰度剪影复核。'],
 ['5 菜单唯一解','8/8给两套可满足完整条件的不同例子，角色候选不止1种；旧品能满足全部菜单合适档，海湾完整档才需一款可保底地方味。MN8严格原16四时。'],
 ['6 稀有随机硬锁','事件普通旧队伍全部通过条件检查。标本4趟、免费知识3趟、新品4批构成有限上界；去掉河水病变供货前置。旧节令/签鸡只为可选替代，8新观赏即可完成造型页。'],
 ['7 循环/不可达','主获取AND/OR依赖图全部可达，强依赖无环。海湾由前区入门实收+溪岸确定路标开放，先于海湾收藏/PJ-4；PJ-2只锁旧方ALT-R，不锁新品。旧料供货逐方存在合法阶段见证。'],
 ['8 前中后期','厨房最低阶段1/2/3/4分别为'+stageDistribution.map(x=>x.count).join('/')+'种；Lv.2仍分谷地、溪岸、500收取/24发现茶坡门槛；Lv.4回访9种，不把高阶奶料和芝麻当首标本条件。'],
 ['9 地区特色','谷地：麦香纸包/谷粒与折片；溪岸：厚皮果香/芹梗便携；茶坡：焙叶两面/花点与咸甜；海湾：方晶/分叉梗与装箱。各有独立2材料、2地点、6卡和12种，并与旧品跨区组合。'],
 ])}\n## 5. 材料复用与阶段分布\n\n${table(['材料','新品数','鸡','鸭','厨具ID'],materialCoverage.map(x=>[x.material,x.count,x.chicken,x.duck,x.tools.join('、')]))}\n${table(['最低厨房','数量','新品'],stageDistribution.map(x=>[x.kitchen,x.count,x.ids.map(id=>name(id)+'（'+id+'）').join('、')]))}\n阶段数字是最低可执行资格，不是默认已拥有所有设备。供货见证假设购买本阶段合法设备，累计收取满足普通材料门槛并实收普通丸子鸡/鸭宝；不要求病变、神社签礼或新稀有。若玩家未买相应设备，页面直接显示缺项。\n\n## 6. 精确配方和兼容候选\n\n与旧193种目录的同蛋种、同厨具、同精确材料组合碰撞：${collisions.length}；新品相互精确重复：${48-new Set(newRecipeKeys).size}；忽略蛋种后的鸡鸭同方：${swapped.length}。旧无料基础候选天然仍存在，不当成内容冲突。旧有料子集命中如下，必须作为伴随保留，但不能同时插入第二套保底：\n\n${table(['新品','会命中的旧子配方','接入处置'],subsets.map(x=>[x.id,x.old.map(name).join('、'),x.resolution]))}\n## 7. 经济算术和未证明的部分\n\n采用实际旧权重池，复刻批按1新品+23旧伴随，首发失败批按24旧；原料全买，包含24 CP基础收取，排除所有手艺、招牌和营业加价。下表是未分摊清洁/修缮的毛差，不能写成净利润。已据此修正四款负毛差配方；不是把新品售价一律拉高。\n\n${table(['ID','售价初值','整批料费','开火费','旧单只均价','复刻批毛差','首次失败批毛差'],economy.map(e=>[e.id,e.baseSaleCP,e.ingredientCP,e.cookCP,e.oldMeanSaleCP,e.protectedBatchGrossMargin,e.failedDiscoveryBatchGrossMargin]))}\n首次试做可能有采购成本，负毛差失败批在此如实列出；不是永远亏损生产。4批保护限制发现尝试长尾，复刻以后才是重复供货场景。所有普通新品复刻批毛差为正不代表任何回访频率下都盈利，未计维护、变化损失、设备摊销、技能返料、历史库存与14天成长。发布前仍须按玩法文档做完整平衡，不能把静态毛差换算毕业天数。观赏批允许为展示付出成本，不用食用菜单补贴。\n\n## 8. 冷读后的执行样例\n\n1. 新档：先按旧系统至收120/发现5并买平锅→谷地首趟荠菜标本与原基础格内试做料→免费辨认→知识兜底/研读→V-C1四批保护→MN1可与旧鸡宝搭配；全程不需鸭、奶、蒸笼或常客。\n2. 中档：厨房Lv.2、溪岸开放、蜂蜜供货→R-S1山柚→R-C1；野餐采购可先全用旧墨西哥卷鸡/盐水鸭。PJ-2要鸡鸭和4款新品，但它不是水芹供货前置。\n3. 后档：厨房Lv.3/发现40且前区入门实收→溪岸确定引路→盐花首标本→B-C1；不用先交海湾货或完成四地展。Lv.4再回谷地/茶坡做奶凝与面包，供货条件仍可直接查看。\n4. 193全收老档：主题纯收录与旧知识追认；营业印记不虚构。旧神社/四时回礼不重发；观赏和病变旧品可同行或放展册，全部24发现卡与48新品仍按新事实取得。\n5. 只收集玩家：供货、地方做法与48实收都能走免费寻访；主题前两段和地区12种全收可完成。营业故事、营业印记、四地展是自选成果，不成为基础制作门槛。\n\n纠正的冷读歧义：最低厨房不等于全设备就绪；每味1份是整批不是每只；displayMenus不等于可食；O04不扣货；项目交付没有基础货款；常客手动路径无需再抽来访；四时客座不计章节；河水不能成为新观赏的病变硬锁。\n\n## 9. 验证边界与复查\n\n本次${checks.length}项检查涵盖结构、数量、引用、白名单、示例队伍/菜单、供料见证及主获取图，不是通用目标引擎实现。完整关系图有${relationGraph.nodes.length}个节点、${relationGraph.edges.length}条边；关系边并非全部AND前置。机器文件列出具体图与来源哈希。没有运行游戏回归、发布构建、迁移玩家存档或批量生成美术。后续应验证实际候选池与保底互斥、库存守恒、未知遮罩、24h离线、免费知识补全、逐步支付和最终剪影辨识。\n`;
fs.writeFileSync(path.join(dir,'audit.md'),auditMd);
console.log(`${report.status}: ${checks.length} checks; ${failures.length} failures`);for(const f of failures)console.log(f);
console.log(JSON.stringify({materialCoverage,stageDistribution,priceWarnings},null,2));
if(failures.length)process.exitCode=1;
