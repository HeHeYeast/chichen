import {settingsOptions} from './settings-options.js';
import {kitchenStage} from './kitchen-stages.js';

const glyphs={
  bell:'<path d="M6 20h16l-2-4v-5a6 6 0 0 0-12 0v5zM11 23q3 4 6 0M14 3v2"/>',
  music:'<path d="M9 17V6l12-3v11M9 9l12-3"/><ellipse cx="6" cy="18" rx="3" ry="2.5"/><ellipse cx="18" cy="15" rx="3" ry="2.5"/>',
  sound:'<path d="M4 10h4l6-5v18l-6-5H4zM18 9q6 5 0 10M21 5q10 9 0 18"/>',
  save:'<path d="M5 4h15l4 4v16H5zM9 4v7h10V4M9 24v-8h11v8"/><path d="M16 6v3"/>',
  book:'<path d="M3 5q7-2 11 2 4-4 11-2v18q-7-2-11 2-4-4-11-2zM14 7v18M6 10h5M6 14h5M17 10h5M17 14h5"/>',
  home:'<path d="M3 13L14 3l11 10M6 11v14h16V11M11 25v-9h6v9"/>',
};
const icon=name=>`<svg viewBox="0 0 28 28" aria-hidden="true">${glyphs[name]}</svg>`;
export function createSettingsUI({getState,panels,showPanel,alertBox,save,music,sound,characterPortrait,toolPortrait,changePage,returnToTitle,returnFromSettings,platform,toggleHatchAlarm,exportProgress,importProgress,getSaveError,openJournal,openWorkshop}){
  const chapters=[
    {title:'厨房',lead:'从一枚蛋，认识新的鸡宝',art:()=>toolPortrait(1,0),steps:[['先选调味料','开火前选好调味料，一批消耗各一份。厨房升级后可增加调味料槽。'],['选厨具，再开火','在厨房下方选择一件已拥有的厨具，确认 CP 花费后开始调理。'],['等破壳，再收取','倒计时结束后轻划鸡宝，收进农场。每收取一只获得 1 CP。'],['照顾小厨房','厨房右上方显示脏污进度，基础36小时变脏，持家手艺可延长至72小时。可以提前打扫，费用按脏污百分比计算，越早越便宜；打扫后进度归零。厨房升级也会清洁。脏污时破壳可能遇见特殊伙伴。升级前先将前六种厨具升到要求等级。']]},
    {title:'农场',lead:'让鸡宝住下来，也收获一点回报',art:()=>characterPortrait(0,0),steps:[['看看鸡宝','厨房收取的鸡宝会来到农场。拖动画面可以看见农场的其他角落。'],['打开收成账本','点击农舍或收成入口，选择鸡宝和卖出数量。确认后获得对应 CP。'],['收藏会留下','卖出会减少当前拥有数量，累计孵化记录和已发现的图鉴仍然保留。'],['记得整修','留意农场完好度，及时整修。长时间荒废会影响鸡宝居住。']]},
    {title:'商店',lead:'添置厨具，试试更多搭配',art:()=>toolPortrait(2,0),steps:[['看看成长册','每种厨具都能预览三级外观、调理时间和费用；升级只影响新开的一批。'],['翻翻完整目录','75 种调味料均可查看，尚未开放的条目写明条件。包内最多持有 30 份。'],['开放鸭蛋','商店「其他」页花费 2500 CP 开放鸭蛋。回到厨房切换蛋种，原来的一批继续孵化。'],['认识第一只鸭宝','收取普通鸭宝后，会陆续开放鸭宝相关调味料；仅购买鸭蛋还不算发现。']]},
    {title:'委托',lead:'收到来信，认识更多老朋友',art:()=>characterPortrait(0,0),steps:[['找到神社','点击农场底部的神社按钮即可求签，或向右拖动点击委托牌。商店「其他」也有入口。'],['先达成，再收下','来信的收取数量、品种和厨具条件都会记下来。完成后取得配方资格。节日伙伴仍需在对应日期开火，寻宝日历提前 7 天预告。'],['每天一份小礼','完成前置来信后领取御神签、火苗、木绵。每天每种一次，再领前需新增收取 24 只并用完上一份。礼物可直接配好下一批。'],['看看伙伴线索','签册记录 15 种签鸡；收藏回礼还包含鸭宝、妖怪、点心和时空鸡目标。旧收藏也计入，奖励只领一次。']]},
    {title:'手艺',lead:'把喜欢的本领，搭配在一起',art:()=>characterPortrait(0,0),steps:[['把喜欢的本领，搭配在一起','发现新品种、累计收取和升级厨房都能获得手艺点。点开手艺看具体效果，加入方案后点击「应用这套手艺」才会生效。普通手艺可以跨方向学习，专精只能选一个。正在进行的批次和旅程使用开始时的手艺。']]},
    {title:'寻访',lead:'带一点新味道回家',art:()=>characterPortrait(0,0),steps:[['带一点新味道回家','选一条路线，再派出1～3种在家伙伴，每种1只。每种伙伴有采集、发现和适应环境；选人时可以看到带回材料和线索的机会。到期后伙伴自动回家，材料、CP和线索等你领取。材料包满了也不会丢失已带回的材料。']]},
    {title:'观察',lead:'认识味道，保留发现的惊喜',art:()=>characterPortrait(0,0),steps:[['认识味道，保留发现的惊喜','线索告诉你可以尝试什么；研读可以学会完整获取方法；只有实际收取，才会正式收入图鉴。知道方法不代表已经满足条件，也不保证普通随机配方每批都有目标。']]},
    {title:'采购',lead:'一笔生意，可以慢慢完成',art:()=>characterPortrait(0,0),steps:[['一笔生意，可以慢慢完成','采购没有截止日期，可以分批交付。每次交付都按数量支付普通货款，全部交齐后再给一次酬谢。已交付的伙伴不能取回；尚未交第一只时，可以更换订单允许的出品。']]},
    {title:'经营',lead:'每一份收成，都算得明白',art:()=>characterPortrait(0,0),steps:[['每一份收成，都算得明白','学会成筐交售或多味拼盘后，每新收取24只获得1次经营奖励。卖出时满足条件才会使用，确认前能看到额外收入。招牌加价只按基础售价计算，订单货款和酬谢不加价。']]},
  ];
  function openSettings(){
    const previousScroll=panels.querySelector('.settings-content')?.scrollTop??0;
    const s=getState(),discovered=Object.values(s.total).filter(n=>n>0).length;
    showPanel('小厨房设置',`<div class="settings-hero"><div class="settings-mascot">${characterPortrait(0,0)}</div><div><strong>今天也照顾好鸡宝</strong><p>${kitchenStage(s.kitchenLevel).title} · Lv.${s.kitchenLevel+1}</p><small>已经认识 ${discovered} 种鸡宝与鸭宝</small></div></div>${settingsOptions({state:s,icon,platform})}<footer><button class="settings-home" data-title>${icon('home')}回到标题画面</button><button class="orange" data-return>继续游戏</button></footer>`,'screen-panel settings-screen');
    if(openWorkshop){panels.querySelector('.settings-content').insertAdjacentHTML('afterbegin','<button class="workshop-entry" data-settings-workshop>手艺 · 旧采购 · 旧路线</button>');panels.querySelector('[data-settings-workshop]').onclick=()=>openWorkshop();}
    const notice=platform.notificationStatus();
    panels.querySelector('[data-notification-status]').textContent=[notice.message,notice.testMessage].filter(Boolean).join('\n');
    panels.querySelector('.close').onclick=returnFromSettings;
    panels.querySelector('.settings-content').scrollTop=previousScroll;
    for(const button of panels.querySelectorAll('[data-toggle]'))button.onclick=async()=>{
      const state=getState(),key=button.dataset.toggle;
      if(key==='alarm'){await toggleHatchAlarm();return;}
      const previous=state[key];state[key]=!previous;if(!save())state[key]=previous;if(key==='music')music();sound(3);openSettings();panels.querySelector(`[data-toggle="${key}"]`).focus({preventScroll:true});
    };
    panels.querySelector('[data-journal]')?.addEventListener('click',()=>openJournal?.('calendar'));
    panels.querySelector('[data-manual]').onclick=()=>openManual(0);
    panels.querySelector('[data-save]').onclick=()=>{if(save())alertBox('保存好了！鸡宝和小厨房的进度已保存在'+(platform.info.android?'此手机。':'此浏览器。'));else alertBox('保存尚未成功，请导出备份并保留游戏。'+getSaveError());};
    panels.querySelector('[data-export]').onclick=exportProgress;
    panels.querySelector('[data-import]').onclick=importProgress;
    panels.querySelector('[data-permission]')?.addEventListener('click',()=>platform.openNotificationSettings());
    panels.querySelector('[data-exact]')?.addEventListener('click',()=>platform.openExactAlarmSettings());
    panels.querySelector('[data-test-notice]')?.addEventListener('click',()=>alertBox(platform.testNotification().message));
    panels.querySelector('[data-test-delayed]')?.addEventListener('click',()=>{const result=platform.testDelayedNotification();openSettings();alertBox(result.message);});
    panels.querySelector('[data-background-help]')?.addEventListener('click',()=>{
      showPanel('后台提醒帮助',`<div class="scroll settings-content"><p class="save-note">先用“1 分钟后台测试”，返回桌面或锁屏等待。普通“测试通知”只检查当下能否发通知，不能验证后台等待。</p><p class="save-note">如果只有重新打开游戏后才收到：在荣耀手机的“应用启动管理”找到鸡宝厨房，关闭自动管理，并允许自启动、关联启动和后台活动。</p><p class="save-note">仍有延迟时，可在系统设置搜索“电池优化”，选择鸡宝厨房并设为“不允许”。具体名称可能随系统版本变化。</p><p class="save-note">在“系统通知设置”中检查孵化提醒、声音、横幅和锁屏通知。关闭游戏内提醒或收完这一批，会取消对应提醒。</p><button class="orange" data-app-settings>打开应用设置</button></div><footer><button data-help-back>返回设置</button></footer>`,'screen-panel settings-screen');
      panels.querySelector('[data-app-settings]').onclick=()=>platform.openAppSettings();panels.querySelector('.close').onclick=openSettings;panels.querySelector('[data-help-back]').onclick=openSettings;
    });
    panels.querySelector('[data-title]').onclick=()=>{save();returnToTitle();};
    panels.querySelector('[data-return]').onclick=returnFromSettings;
  }
  function openManual(index=0){
    const c=chapters[index];
    showPanel('鸡宝照顾手册',`<div class="subtabs manual-tabs">${chapters.map((chapter,i)=>`<button data-chapter="${i}" aria-pressed="${i===index}" class="${i===index?'active':''}">${chapter.title}</button>`).join('')}</div><div class="scroll manual-pages"><div class="manual-lead"><span>${c.art()}</span><strong>${c.lead}</strong></div><ol>${c.steps.map(([title,body],i)=>`<li><b>${i+1}</b><div><strong>${title}</strong><p>${body}</p></div></li>`).join('')}</ol></div><footer><span class="manual-page-no">照顾手册 · ${index+1} / ${chapters.length}</span><button class="orange" data-done>知道啦</button></footer>`,'screen-panel guide-screen');
    panels.querySelectorAll('[data-chapter]').forEach(button=>button.onclick=()=>openManual(+button.dataset.chapter));
    panels.querySelector('.close').onclick=openSettings;panels.querySelector('[data-done]').onclick=openSettings;
  }
  return {openSettings,openManual};
}
