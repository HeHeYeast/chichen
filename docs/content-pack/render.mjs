// Documentation renderer, never part of game execution.
import fs from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
const dir=path.dirname(fileURLToPath(import.meta.url));
const c=JSON.parse(fs.readFileSync(path.join(dir,'content.json'))),b=JSON.parse(fs.readFileSync(path.join(dir,'baseline.json'))),a=JSON.parse(fs.readFileSync(path.join(dir,'art-manifest.json')));
const all=[...b.species.map(s=>({...s,id:s.key})),...c.species];
const label=id=>{const x=all.find(s=>s.id===id);return x?`${x.name}（${id}）`:id;};
const labels=ids=>ids?.length?ids.map(label).join('、'):'无';
const material=id=>[...b.ingredients,...c.materials].find(m=>m.id===id)?.name??id;
const tool=id=>b.tools.find(t=>t.id===id)?.name??id;
const env={yard:'菜园',water:'水边（溪岸/海湾共用）',wood:'林间'};
const list=ids=>ids.length?ids.join('、'):'无';
const mdTable=(heads,rs)=>'| '+heads.join(' | ')+' |\n| '+heads.map(()=>'---').join(' | ')+' |\n'+rs.map(r=>'| '+r.map(x=>String(x??'').replaceAll('|','／').replaceAll('\n','<br>')).join(' | ')+' |').join('\n')+'\n';
const write=(name,title,text)=>fs.writeFileSync(path.join(dir,name),`# ${title}\n\n2026-09-23 内容定义；**尚未接入游戏**。返回[内容总册](../design/content-expansion-assets.md)。由作者源导出，与[结构化清单](content.json)共用稳定身份。\n\n${text}\n`);
const cond=q=>q.kind==='tool'?`${tool(q.id)}Lv.${q.level+1}`:q.kind==='kitchen'?`厨房Lv.${q.level+1}`:q.kind==='discovery'?`实收${label(q.egg+':'+q.id)}`:q.kind==='collected'?`累计收取${q.count}只`:JSON.stringify(q);
let out='所有等级均为玩家显示等级，ID仍采用原内部编号。配方每味消耗1份，作用于一批24枚，不是每只消耗1份。鸡蛋/鸭蛋之外不暗加原料；料理形态允许沿用世界的调理抽象。\n\n';
out+=mdTable(['地区','鸡','鸭','食用/观赏'],c.regions.map(r=>[r.name,labels(c.species.filter(s=>s.region===r.id&&!s.egg).map(s=>s.id)),labels(c.species.filter(s=>s.region===r.id&&s.egg).map(s=>s.id)),'10食用＋2观赏']));
for(const r of c.regions){out+=`\n## ${r.name}\n\n${r.gate}\n\n视觉：${r.visual}\n`;
 for(const s of c.species.filter(s=>s.region===r.id)){
 out+=`\n### ${s.id} · ${s.name}\n\n${s.namingDecision}\n\n`;
 out+=mdTable(['字段','正式定义'],[
 ['稳定身份',`${s.key}／${s.catalogLabel}；${s.egg?'鸭':'鸡'}；${r.name}`],['精确配方',`${s.recipe.id}：${s.egg?'鸭蛋':'鸡蛋'}＋${tool(s.recipe.toolId)}Lv.${s.recipe.toolLevel}＋${s.recipe.ingredients.map(i=>material(i.id)+'×1').join('＋')}；精确集合，不接受额外配料。`],['最低厨房',`Lv.${s.recipe.kitchenLevel}；厨具与材料供货条件仍须逐项满足。`],['解锁事实',`地区开放；${s.unlock.identifiedMaterials.length?'辨认'+s.unlock.identifiedMaterials.map(material).join('、'):'本区首标本登记'}；${s.unlock.card?'记录'+s.unlock.card+'；':''}${s.egg?'鸭蛋资格；':''}完整地方做法已学。`],['旧料供货',s.recipe.oldSupply.length?s.recipe.oldSupply.map(i=>`${i.name}：${i.anyOf.map(g=>g.length?g.map(cond).join('且'):'起始可购').join('；或')}`).join('<br>'):'无旧料供货前置'],['设备说明',s.unlock.technologyNote],['出现与复刻','地区试做；首次25%批次机会，失败3批后第4批安排1只；已实收复刻安排1只，其余23枚走兼容旧池。病变/过熟未收目标则不重置保护。'],['食用／观赏',s.edible?'可食用、可观赏；进入明确列出的菜单/采购候选。':'仅观赏与同行；禁止食用菜单和采购，原即时交易可保留。'],['招牌',s.signature??'无'],['菜单标签',s.tags.map(t=>c.tags[t]).join('、')],['建议基础售价',`${s.priceProposal.baseSaleCP} CP/只；收取1 CP/只。为接入初值，整批与长期经济尚待验证。`],['寻访',`G${s.exploration.G}／F${s.exploration.F}，${env[s.exploration.environment]}；特征：${s.exploration.traits.map(t=>c.traits[t]).join('、')||'无'}`],['关联菜单',s.edible?list(s.menus):`不参与食用评分；可在${list(s.displayMenus)}主题旁陈列`],['关联收藏',list([...s.collections,...s.specials])],['地区与发现',`${r.id}；${list(s.relatedCards)}`],['采购／常客／项目',`${list(s.orders)}；${list(s.regulars)}；${list(s.projects)}`],['常客使用范围',s.regularRelation],]);
 out+=`\n**图鉴正文**：${s.description}\n\n**获取线索**：${s.clue}\n\n**实际用途**：\n\n${s.uses.map(u=>`- ${u.target}：${u.action}`).join('\n')}\n\n`;
 out+=mdTable(['美术字段','制作定义'],[['身体轮廓',s.art.body],['料理与材料',s.art.foodAndMaterial],['鸡鸭识别',s.art.identity],['与相似品区别',s.art.contrast],['头像构图',s.art.portrait],['未知剪影',s.art.silhouette]]);
 }}
write('species.md','48种新增鸡鸭 · 全量内容定义',out);
out='## 8种地区材料与食材见闻\n\n每条材料见闻为同一材料的知识子页；“标本已找到→已辨认→供货开放→已用于料理”四态分开。购买材料不点亮任何品种；实际开火且整批收取后记录用过的材料，不要求本批一定收出目标。\n\n';
for(const m of c.materials){out+=`### ${m.name} · ${m.stableId}\n\n`+mdTable(['字段','定义'],[['建议追加材料ID',m.id],['地区／标本',`${m.region}／${m.specimen} ${m.specimenName}`],['辨认',m.recognition],['食材见闻',m.lore],['商店供货',m.shop],['建议价格',`${m.priceCP} CP/份；常驻、无限次补货，无日限量；适用普通返利与返料规则`],['首个可试新品',label(m.entrySpecies)],['入池条件',m.entryGate],['全部关联新品',labels(m.uses)],['旧品／地方做法',labels(m.oldKeys)+'；'+m.oldUse],['涉及厨具',m.tools.map(tool).join('、')],['事件',m.event],['视觉素材',m.art]])+`\n${m.identification}\n\n`;}
out+='## 4条地方替代做法\n\n每区恰好1条，沿用旧身份，不计新增48。使用独立“地方替代”准备模式，原配方继续有效；它不获得新品的复刻保证。\n\n';
for(const x of c.alternatives)out+=`### ${x.id} · ${x.name}\n\n${label(x.target)}；${tool(x.toolId)}Lv.${x.toolLevel}；厨房Lv.${x.kitchenLevel}；${x.ingredients.map(material).join('＋')}，每味1份。由${x.unlock}解锁完整方法，旧材料仍检查供货。\n\n${x.tradeoff} ${x.guarantee}\n\n`;
out+='## 24张发现卡\n\n每地区2标本、2地点见闻、2条件事件。普通队伍示例是可替代解，不是指定名单；同一位可满足特征和环境两条件。事件不要求携带食物，唯一带货分支是B-E1，且完全可选。\n\n合格卡的新发现概率为min(60%,25%＋2%×全队F＋单次5%条件加成)。8张条件事件的特征达成给这次5%；R-S1的水边适应、T-N2的花香、B-N1的盐晶分别是可选加成，其余卡无额外加成；不会按多个同特征队员重复加。所有硬门槛在入池前检查，无合格卡不滚失败数。首标本与第4趟保护优先于概率。\n\n';
for(const card of c.cards){out+=`### ${card.id} · ${card.title}\n\n`+mdTable(['字段','定义'],[['地区／地点',`${card.region}／${card.place}`],['类型／关注',`${{specimen:'标本',lore:'地点见闻',event:'条件事件'}[card.type]}／${card.focus}`],['玩家提前看到的提示',card.hint],['队伍条件',card.team.trait?`${c.traits[card.team.trait]}＋${env[card.team.environment]}；最多2条件，可同一位满足`:'任意1–3不同品种；无硬特征门槛'],['额外条件',card.gate],['普通旧解',labels(card.team.oldExamples)],['关联特征',card.team.trait?c.traits[card.team.trait]:'不要求；标本靠可执行厨房条件入池'],['结果正文',card.result],['下一步',card.next],['实际影响',card.effects.map(e=>`${e.kind} → ${e.target??material(e.material)}`).join('；')],['保护',card.protection],['美术',card.art]])+'\n';if(card.exchange)out+='带货分支：另带家常自由库存6只（不含队员），完整归队扣货并将原基础材料中的1份换成盐花；无基础货款、无营业或采购计数。出发显示放弃的销售价值与材料格。未带货同样获得B-E1与替代方法；带货只追加交流记录。\n\n';}
out+=`## 海湾路线引导 · ${c.guide.id}\n\n${c.guide.gate}\n\n预告：${c.guide.hint}\n\n${c.guide.trigger}\n\n结果：${c.guide.result}\n\n${c.guide.effect} 不依赖海湾收藏、盐花或货客毕业；有无营业都可走。\n`;
write('materials-discoveries.md','8种材料与四地区24张发现卡',out);
out='## 菜单的共同执行口径\n\n同品种在一单只分配一个角色，同一只不填两组；每角色最多2种，整单最多6种。所有allowed名单都是作者配置，不从名称推断。例子数量是开店初备，窗口售出后重新评估，不能保证整单始终完整。合适加5%、完整加8%，只乘基础价、每只上限2 CP；原库存、2h/6只窗口与有效接待规则不变。\n\n';
for(const m of c.menus){out+=`### ${m.id} · ${m.name}\n\n${m.text}\n\n开放：${m.unlock}。完整：${m.complete}\n\n${m.validService}\n\n`+mdTable(['角色','必要','允许候选'],m.roles.map(r=>[r.name,r.required?'是':'否',labels(r.allowed)]))+`\n两套明确解：\n\n${m.examples.map(e=>'- '+e.map(x=>`${label(x.id)}×${x.quantity}`).join('＋')).join('\n')}\n\n采购${list(m.orders)}；常客${list(m.regulars)}；项目${list(m.projects)}。\n\n`;}
out+='## 12个采购模板\n\n候选最多2、进行中最多2，无期限，可按组混交；接取冻结需求与酬谢。普通单按已知可执行解生成，缺鸭蛋时用鸡蛋变体或不生成，绝不泄露未知名称。展示模板O04一次性；变体共享成果身份。首单便签由永久记录自动入册，重做只有本单货款和酬谢。\n\n';
for(const o of c.orders){out+=`### ${o.id} · ${o.name}\n\n**采购原话**：${o.request}\n\n`+mdTable(['需求组','数量','可替代出品'],o.groups.map(g=>[g.id,o.kind==='display'?'任选3种各在家1只供看':g.quantity,labels(g.allowed)]))+`\n至少${o.minimumDistinct}个不同品种。${o.seasonRule??''}\n\n${o.payment}\n\n${o.qualification}\n\n`+mdTable(['变体','规则'],o.variants.map(v=>[`${v.id} ${v.label}`,v.text+(v.allowed?' 允许：'+labels(v.allowed):'')]))+`\n**交付回应**：${o.finish}\n\n首份成果：${o.firstResult.id} ${o.firstResult.name}；${o.card}提供方向，${o.menu??'无食用菜单'}、${o.regular}、${o.project}可引用记录。\n\n`;}
out+='## 四位常客 · 16段\n\n各段两个分支满足任一即可。发现或实际经营事实默认追认，只有明确写“再完成”的交易才需新记录；同一笔成交可被不同检查引用，但不能扣同一只两次。常客页能主动继续已经达成的手动采购分支，不依赖额外随机来访才能阅读。\n\n';
for(const r of c.regulars){out+=`### ${r.id} · ${r.name}\n\n${r.delivery} ${r.completion}\n\n`;for(const st of r.stages)out+=`#### ${st.id} · ${st.title}\n\n门槛：${st.gate}。\n\n- 路径A：${st.alternatives[0]}。\n- 路径B：${st.alternatives[1]}。\n\n**正文**：${st.text}\n\n成果：${st.reward}。下一步：${st.next}\n\n`;}
out+='## 四个长期项目\n\n检查记录可追认；交付消耗独立的实际库存。支付与实物各次操作明确确认，费用分段后不重复收取。项目不设时限，不是地区供货或新品试做前置。\n\n';
for(const p of c.projects){out+=`### ${p.id} · ${p.name}\n\n开放：${p.gate}。\n\n`+mdTable(['阶段','检查事实','实际交付','支付CP'],p.stages.map(s=>[s.id,s.check,s.consume?s.consume.distinct?`任选${s.consume.distinct}种各${s.consume.quantityEach}只；允许：${labels(s.consume.allowed)}`:`共${s.consume.total}只，至少${s.consume.minimumSignatureCategories}种唯一招牌分类、每类至少1只；允许：${labels(s.consume.allowed)}`:'不扣实物',s.costCP]))+`\n旧品参与：${labels(p.oldAllowed)}。\n\n新品参与：${labels(p.newAllowed)}。\n\n成果：${p.result}\n\n**成果正文**：${p.text}\n\n下一步：${p.next}\n\n美术组件：${p.art}\n\n`;if(p.optionalDisplay)out+=`附加观赏栏：${p.optionalDisplay.rule} 无奖励或毕业前置。\n\n`;}
write('business.md','菜单、采购、常客与长期经营项目',out);
out='## 收藏统一口径\n\n固定项为明确的代表候选组，并非指定单一稀有种。任选数包括代表，不重复计同种；卖空仍保留收录事实。收录阶段奖励和实践印记分开；一条实收/成交事实可供多页引用，只发各自稳定身份的一份成果，不重复扣货。主题8页、地区4册、特殊发现4页、食材见闻8页都在前两阶段确定的二级结构内，不增加导航。\n\n';
for(const col of c.collections){out+=`### ${col.id} · ${col.name}\n\n${col.text}\n\n`+mdTable(['字段','正式内容'],[['类别',col.kind==='theme'?'经营主题页':'地区收藏'],['旧品参与',labels(col.oldKeys)],['新品参与',labels(col.newIds)],['固定项',col.fixed.kind==='any-one-of'?'以下任选1种：'+labels(col.fixed.allowed):list(col.fixed.allowed)],['任选项',labels(col.optional.allowed)],['阶段目标',col.stageRules??col.stages.map(s=>s.id+'：'+s.requires+' → '+s.reward).join('<br>')],['实践目标',col.practice.any.join('；或')],['实践补充',col.practice.fullMenuStamp??'地区使用印与完整地区收录独立；不要求12种都卖出'],['奖励',typeof col.rewards==='string'?col.rewards:col.rewards.map(r=>`${r.stage}阶段：${r.id} ${r.text}`).join('；')],['客座展示',col.displayGuests?.length?labels(col.displayGuests)+'；不计食用角色或核心收录数':'无额外客座'],['下一步',col.next]])+'\n';}
out+='## 特殊发现\n\n特殊发现复用既有记录，不是另加4张地区发现卡。\n\n';
for(const s of c.specials)out+=`### ${s.id} · ${s.name}\n\n${s.theme}\n\n旧候选：${labels(s.oldKeys)}。\n\n新候选：${labels(s.newIds)}。\n\n固定项：${list(s.fixed)}。阶段：${s.optional}\n\n实践：${s.practice}\n\n奖励：${s.reward}\n\n正文：${s.text}\n\n下一步：${s.next}\n\n`;
out+='## 食材见闻\n\n八页分别为荠菜、麦芽、山柚、水芹、焙香叶、桂花、盐花、海蓬菜，固定对应标本；辨认/供货/用过是各自阶段，不要求额外指定稀有品种。展示正文、旧新配方关联、实际用过条件和下一步见[材料与发现](materials-discoveries.md)。辨认奖励是供货资格与方法方向，用过只盖用料印；不再另发材料或CP。\n\n## 12件独立纪念物\n\n';
out+=mdTable(['ID／名称','来源','展示文案','美术'],c.mementos.map(m=>[`${m.id} ${m.name}`,m.source,m.text,m.art]));
out+='\n8主题阶段2给M01–M08，4常客终段给M09–M12。项目奖励仅使用配套底座、册页、章和布置组件；不会扩为16件独立纪念物。全部可在3个位置自选陈列，无加成、无维护、无新货币。主题阶段1插页、采购小票、常客便签只是纸册记录，使用共用纸形与文字层；一次性O04明信片同样不占“独立纪念物12件”预算。\n\n## 193旧品用途检查表\n\n每一旧品都保留原身份、原配方、原寻访能力；下表是新增用途配置，不是把非食用品变为可食。\n\n';
out+=mdTable(['旧身份','名称','食用','菜单','采购','主题/地区','特殊发现/项目'],b.species.map(s=>[s.key,s.name,s.edible?'是':'否',list(s.menus),list(s.orders),list(s.collections),list(s.specials)+'；PJ-4画像展出']));
write('collections.md','主题收藏、地区册、特殊发现与纪念物',out);
out=`## 制作总量与交付规格\n\n${a.style}\n\n`+mdTable(['资产','数量'],Object.entries(a.totals))+`\n`+mdTable(['规格','要求'],Object.entries(a.sizes))+`\n## 四地区视觉方向\n\n`+mdTable(['地区','形状与色彩','地点'],c.regions.map(r=>[r.name,r.visual,list(r.places)]));
out+='\n## 48个角色制作条目\n\n每项的正文与配方同[全量品种定义](species.md)，此处为绘制/生成素材用的独立brief。先认轮廓再认颜色。\n\n';
for(const s of a.species)out+=`### ${s.id} · ${label(s.content)}\n\n- 身体：${s.body}。\n- 料理与材料：${s.foodAndMaterial}。\n- 鸡鸭识别：${s.identity}。\n- 与相似品区分：${s.contrast}。\n- 头像：${s.portrait}。\n- 未知剪影：${s.silhouette}。\n\n`;
for(const [key,title] of [['materials','8套材料素材'],['cards','24张发现卡局部'],['mementos','12件纪念物'],['projects','项目组件'],['menus','菜单物件裁切'],['specials','特殊页覆层']])out+=`## ${title}\n\n`+mdTable(['资产ID','brief','交付'],a[key].map(x=>[x.id,x.brief,x.deliverables.join('、')]))+'\n';
out+='## 复用与验收\n\n'+a.qa.map((s,i)=>`${i+1}. ${s}`).join('\n\n')+'\n\n本轮没有批量生成最终美术，也没有把示意轮廓声称为已通过审美验证。已对照原193项描述、点心图集和四时图集；具体旧近似品列在每条brief，正式出图仍须与原角色并排审稿。\n';
write('art.md','美术制作清单与角色视觉brief',out);
console.log('Rendered species, materials/discoveries, business, collections, and art documents.');
