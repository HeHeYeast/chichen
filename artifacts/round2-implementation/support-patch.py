from pathlib import Path
p=Path('web/collection-ui.js');s=p.read_text(encoding='utf-8').replace("import {DESCRIPTIONS}","import {DESCRIPTIONS,ABILITIES}")
s=s.replace("import {basketQuote}","import {storyOrders} from './story-orders.js';\nimport {basketQuote}")
s=s.replace('saleSelectionSummary(state,selection={})','saleSelectionSummary(state,selection={},options={})').replace('basketQuote(state,normalized)', 'basketQuote(state,normalized,options)').replace('income:income+quote.bonus', 'income:quote.income')
s=s.replace('keepOneSelection=false;', 'keepOneSelection=false,useRewards=true;',1)
s=s.replace('saleSelectionSummary(getState(),selection)', 'saleSelectionSummary(getState(),selection,{useRewards})')
s=s.replace('<span>鸡宝和鸭宝一起选</span>', '<span>外出伙伴不可售 · 鸡鸭一起选</span>').replace('<button type="button" data-harvest-keep-one>每种留一只</button>', '<button type="button" data-harvest-keep-one>每种留一只</button><button type="button" data-harvest-orders>为采购留货</button>')
s=s.replace('<div class="harvest-checkout">', '<p class="sale-breakdown" data-sale-breakdown></p><label class="sale-rewards"><input type="checkbox" data-use-rewards ${useRewards?\'checked\':\'\'}>使用经营奖励（整筐优先，再拼盘）</label><div class="harvest-checkout">')
s=s.replace("if(income)income.textContent=number(summary.income);", "if(income)income.textContent=number(summary.income);const breakdown=find('[data-sale-breakdown]');if(breakdown)breakdown.textContent=`货款 ${summary.baseIncome} CP · 招牌 +${summary.markup} · 经营 +${summary.bonus}`;")
s=s.replace("find('[data-harvest-sell]').onclick=()=>{", "find('[data-use-rewards]').onchange=e=>{useRewards=e.target.checked;updateHarvestSelection();};\n    find('[data-harvest-orders]').onclick=()=>{selection=bulkSaleSelection(getState());for(const o of storyOrders(getState()).filter(o=>o.accepted&&!o.completed)){const c=o.choices.find(c=>c.species===o.choice);selection[c.species]=Math.max(0,(selection[c.species]??0)-(c.count-o.delivered));}keepOneSelection=false;updateHarvestSelection();};\n    find('[data-harvest-sell]').onclick=()=>{")
s=s.replace('其中整筐额外 ${summary.bonus} CP；外出伙伴不参与。', '基础货款 ${summary.baseIncome} CP · 招牌加价 ${summary.markup} CP · 经营奖励 ${summary.bonus} CP。\\n整筐${summary.baskets}次，拼盘${summary.platters}次；招牌小数余额 ${(summary.markupRemainder/100).toFixed(2)} CP。${summary.basketItems.length?\'\\n整筐：\'+summary.basketItems.map(i=>E.label(E.char(...i.key.split(\':\').map(Number)))+\'×\'+i.count).join(\'、\'):\'\'}${summary.platterItems.length?\'\\n拼盘：\'+summary.platterItems.map(ks=>ks.map(k=>E.label(E.char(...k.split(\':\').map(Number)))+\'×3\').join(\'、\')).join(\'；\'):\'\'}\\n外出伙伴不参与。')
s=s.replace('snapshot,{keepOne:reserveOne}', 'snapshot,{keepOne:reserveOne,useRewards}')
s=s.replace('qty*entry.price+basketQuote(getState(),{[key]:qty}).bonus', 'basketQuote(getState(),{[key]:qty}).income').replace('quantity*current.price+basketQuote(getState(),{[key]:quantity}).bonus', 'basketQuote(getState(),{[key]:quantity}).income')
s=s.replace('<dt>农场库存</dt>', '<dt>在家可用</dt>')
s=s.replace('<p class="species-note">${entry.stock?', '<p class="species-note">总持有 ${entry.held}只 · 外出 ${entry.away}只<br>寻访：采集${ABILITIES[key].gather} · 发现${ABILITIES[key].discover} · 适应${({yard:\'菜园\',water:\'溪岸\',wood:\'林间\'})[ABILITIES[key].environment]}</p><p class="species-note">${entry.stock?')
p.write_text(s,encoding='utf-8')
p=Path('web/shop-ui.js');s=p.read_text(encoding='utf-8').replace("import * as E", "import {effects} from './progression.js';\nimport * as E")
s=s.replace("confirmBox(`${E.label(item)} ×${quantity}\\n需要花费 ${formatCP(item.buy_cp * quantity)} CP。\\n确定购买吗？`", "const price=item.buy_cp*quantity,rebate=price*effects(state).rebate+state.progress.trade.rebateRemainder,refund=Math.floor(rebate/100);\n    confirmBox(`${E.label(item)} ×${quantity}\\n材料原价${price} CP · 本次返还${refund} CP · 实付${price-refund} CP。\\n尚有${((rebate%100)/100).toFixed(2)} CP返利未到账，攒满1 CP自动返还。需先付得起原价。`")
p.write_text(s,encoding='utf-8')
p=Path('web/settings-ui.js');s=p.read_text(encoding='utf-8');idx=s.index('  ];')
extra=[('手艺','把喜欢的本领，搭配在一起','发现新品种、累计收取和升级厨房都能获得手艺点。点开手艺看具体效果，加入方案后点击「应用这套手艺」才会生效。普通手艺可以跨方向学习，专精只能选一个。正在进行的批次和旅程使用开始时的手艺。'),('寻访','带一点新味道回家','选一条路线，再派出1～3种在家伙伴，每种1只。每种伙伴有采集、发现和适应环境；选人时可以看到带回材料和线索的机会。到期后伙伴自动回家，材料、CP和线索等你领取。材料包满了也不会丢失已带回的材料。'),('观察','认识味道，保留发现的惊喜','线索告诉你可以尝试什么；研读可以学会完整获取方法；只有实际收取，才会正式收入图鉴。知道方法不代表已经满足条件，也不保证普通随机配方每批都有目标。'),('采购','一笔生意，可以慢慢完成','采购没有截止日期，可以分批交付。每次交付都按数量支付普通货款，全部交齐后再给一次酬谢。已交付的伙伴不能取回；尚未交第一只时，可以更换订单允许的出品。'),('经营','每一份收成，都算得明白','学会成筐交售或多味拼盘后，每新收取24只获得1次经营奖励。卖出时满足条件才会使用，确认前能看到额外收入。招牌加价只按基础售价计算，订单货款和酬谢不加价。')]
s=s[:idx]+''.join("    {title:'%s',lead:'%s',art:()=>characterPortrait(0,0),steps:[['%s','%s']]},\n"%(a,b,b,c) for a,b,c in extra)+s[idx:];p.write_text(s,encoding='utf-8')
p=Path('web/scene.js');s=p.read_text(encoding='utf-8').replace("text('选一件厨具，孵出新鸡宝',160,235,14,'#8b6a3e',700);text('一批 24 枚 · 轻划就能收取',160,257,11,'#a0804c',500);", "ctx.fillStyle='#fff7de';ctx.strokeStyle='#b18a54';ctx.lineWidth=1.5;ctx.beginPath();ctx.roundRect(65,217,200,55,10);ctx.fill();ctx.stroke();text('点下方保温灯，开始第一批',165,237,12,'#68482d',700);text('免费调理24枚蛋',165,258,12,'#795b3c',500);")
p.write_text(s,encoding='utf-8')
p=Path('android/app/src/main/java/com/jibao/kitchen/SaveRepository.java');s=p.read_text(encoding='utf-8').replace('!=hours*3600000L', '!=hours*3600000L*(t.getJSONObject("snapshot").optBoolean("light",false)?0.8:1)')
s=s.replace('integer(fortune,"drought"', '''if(p.has("skillVersion")) {
            integer(p,"skillVersion",2,2);integer(trade,"markupRemainder",0,99);
            if(p.getJSONArray("leftovers").length()>5)throw new IOException("余料篮过大");
            if(!(protection.get("calm") instanceof Boolean))throw new IOException("安心模式无效");
        }
        integer(fortune,"drought"''')
p.write_text(s,encoding='utf-8')
