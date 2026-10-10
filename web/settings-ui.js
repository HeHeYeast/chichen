import {resetFirstVisitGuides} from './game-frame.js';
import {GAME_DATA} from './content-pack.js';
import {settingsOptions} from './settings-options.js';
import {discoveryCount} from './species-state.js';
import {kitchenStage} from './kitchen-stages.js';

import {resolveSprite,spriteSVG,uiIcon} from './art/manifest.js';
import {kitSheet,kitTabs,kitButton,kitButton2,kitChip} from './ui-kit.js';
import {deviceCheck,deviceCheckText,clearDeviceErrors} from './device-check.js';
const sprite=name=>spriteSVG(resolveSprite(uiIcon(name==='chef'?4:6)));
const GEAR='<img src="/web/art/ui-kit/gear.png" alt="">';
export function createSettingsUI({getState,panels,showPanel,alertBox,save,music,sound,characterPortrait,toolPortrait,changePage,returnToTitle,returnFromSettings,platform,toggleHatchAlarm,exportProgress,importProgress,getSaveError,openJournal,openWorkshop,openCloud,openFeedback}){
  const chapters=[
    {title:'厨房',lead:'从一枚蛋，认识新的鸡宝',art:()=>toolPortrait(1,0),steps:[['下一锅','点「调整」打开下一锅：已经按推荐选好厨具和调味料，直接开火就行；缺的调味料开火时一起买。'],['点厨具也能开火','用当前选好的调味料，确认一下就开始。'],['收取','破壳后轻划蛋窝收进农场，每只 1 CP；放太久会焦。收完一锅会弹出这锅收成。'],['照顾厨房','清洁度低了会生病，点清洁度可以打扫，越早越便宜。']]},
    {title:'农场',lead:'伙伴住的地方',art:()=>characterPortrait(0,0),steps:[['仓库','伙伴和材料都在仓库。「卖掉多余」只卖超过锁定数量的，每种默认锁 1 只，点一种伙伴就能改。'],['神社','每天求一签、领小礼、看来信。'],['整修','完好度低了要整修，不然会有伙伴跑掉。']]},
    {title:'商店',lead:'添置厨具和调味料',art:()=>toolPortrait(2,0),steps:[['厨具','每种厨具有三级，升级只影响新开的一锅。'],['调味料','缺的调味料也可以在下一锅里直接买。'],['鸭蛋','商店「其他」花 2500 CP 开放鸭蛋。']]},
    {title:'生意',lead:'把伙伴卖出去',art:()=>characterPortrait(0,0),steps:[['开张','进生意页时已按菜单摆好，点「开张」，每 2 小时卖 6 只。'],['菜单','星越多越完整；空格里点「做」去厨房。'],['订单','生意簿里的订单可以分批交付；厨房往事是置顶的故事订单。']]},
    {title:'寻访',lead:'派伙伴出门',art:()=>characterPortrait(0,0),steps:[['出门','在地图上选地区或近郊，同行已帮你选好，点出发。'],['回来','伙伴回来后领取收获；调味料没有持有数量上限。']]},
    {title:'图鉴',lead:'认识每一位伙伴',art:()=>characterPortrait(0,0),steps:[['线索','没收录的伙伴点开能看线索；学会配方研读后可以花 CP 读完整方法。'],['去做','档案里点「去做」会打开下一锅并填好配方。']]},
  ];
  function openSettings(){
    const previousScroll=panels.querySelector('.settings-content')?.scrollTop??0;
    const s=getState(),discovered=discoveryCount(s);
    const hero=`<div class="gd-head settings-hero" data-row><span class="gd-face">${characterPortrait(0,0)}</span><div class="gd-row">${kitChip('',`${kitchenStage(s.kitchenLevel).title} Lv.${s.kitchenLevel+1}`,'soft')}${kitChip('',`认识 ${discovered} 种`,'soft')}</div></div>`;
    showPanel('小厨房设置',kitSheet(hero+settingsOptions({state:s,platform,sprite})+(openCloud?`<div class="gd-row">${kitButton2('账号与云备份','data-cloud-open')}${kitButton2('反馈与建议','data-feedback-open')}</div>`:''),`${kitButton2('回标题','data-title')}${kitButton('继续','data-return')}`,'','',{cls:'settings-content'}),'screen-panel settings-screen',{skin:'kitchen',icon:GEAR,short:'设置'});
    panels.querySelector('[data-cloud-open]')?.addEventListener('click',openCloud);
    panels.querySelector('[data-feedback-open]')?.addEventListener('click',openFeedback);
    if(openWorkshop){panels.querySelector('[data-settings-workshop]').onclick=()=>openWorkshop();
      panels.querySelector('[data-replay-guides]').onclick=()=>{resetFirstVisitGuides();sound(3);alertBox('下次进入各页时会再指引一次');};}
    const notice=platform.notificationStatus();
    panels.querySelector('[data-notification-status]').textContent=[notice.message,notice.testMessage].filter(Boolean).join('\n');
    panels.querySelector('.close').onclick=returnFromSettings;
    panels.querySelector('.settings-content').scrollTop=previousScroll;
    for(const button of panels.querySelectorAll('[data-toggle]'))button.onclick=async()=>{
      const state=getState(),key=button.dataset.toggle;
      if(key==='alarm'){await toggleHatchAlarm();return;}
      if(key==='keepOne'){const policy=state.expansion.inventoryPolicy,previous=policy.keepOne!==false;policy.keepOne=!previous;if(!save())policy.keepOne=previous;sound(3);openSettings();panels.querySelector('[data-toggle="keepOne"]')?.focus({preventScroll:true});return;}
      const previous=state[key];state[key]=!previous;if(!save())state[key]=previous;if(key==='music')music();sound(3);openSettings();panels.querySelector(`[data-toggle="${key}"]`).focus({preventScroll:true});
    };
    panels.querySelector('[data-journal]')?.addEventListener('click',()=>openJournal?.('calendar'));
    panels.querySelector('[data-manual]').onclick=()=>openManual(0);
    panels.querySelector('[data-save]').onclick=()=>{if(save())alertBox('保存好了！鸡宝和小厨房的进度已保存在'+(platform.info.android?'此手机。':'此浏览器。'));else alertBox('保存尚未成功，请先导出备份，不要卸载游戏。'+getSaveError());};
    panels.querySelector('[data-export]').onclick=exportProgress;
    panels.querySelector('[data-import]').onclick=importProgress;
    panels.querySelector('[data-permission]')?.addEventListener('click',()=>platform.openNotificationSettings());
    panels.querySelector('[data-exact]')?.addEventListener('click',()=>platform.openExactAlarmSettings());
    panels.querySelector('[data-background]')?.addEventListener('click',()=>platform.requestBackgroundRun());
    panels.querySelector('[data-test-notice]')?.addEventListener('click',()=>alertBox(platform.testNotification().message));
    panels.querySelector('[data-test-delayed]')?.addEventListener('click',()=>{const result=platform.testDelayedNotification();openSettings();alertBox(result.message);});
    panels.querySelector('[data-background-help]')?.addEventListener('click',()=>{
      showPanel('后台提醒帮助',kitSheet(`<ol class="settings-steps"><li>先点「1 分钟测试」，回到桌面或锁屏等一下</li><li>只有打开游戏才收到：点「启动管理」，把鸡宝厨房改成手动管理，打开自启动和后台活动</li><li>提醒晚到：点「允许后台」，选「允许」</li><li>没有横幅或声音：在「系统通知」里打开横幅、声音和锁屏通知</li></ol><div class="gd-row">${kitButton2('启动管理','data-startup')}${kitButton2('允许后台','data-background-run')}</div>`,kitButton('返回','data-help-back'),'','',{cls:'settings-content'}),'screen-panel settings-screen',{skin:'kitchen',icon:GEAR});
      panels.querySelector('[data-startup]').onclick=()=>platform.openStartupManager();
      panels.querySelector('[data-background-run]').onclick=()=>platform.requestBackgroundRun();panels.querySelector('.close').onclick=openSettings;panels.querySelector('[data-help-back]').onclick=openSettings;
    });
    panels.querySelector('[data-device-check]').onclick=openDeviceCheck;
    panels.querySelector('[data-title]').onclick=()=>{save();returnToTitle();};
    panels.querySelector('[data-return]').onclick=returnFromSettings;
  }
  function openDeviceCheck(){
    const result=deviceCheck(platform),esc=t=>String(t).replace(/[&<>"]/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;'}[c]));
    const rows=result.rows.map(r=>`<li class="${r.ok?'ok':'bad'}"><b>${esc(r.label)}</b><span>${esc(r.value)}</span></li>`).join('');
    const errors=result.errors.map(e=>`<li>${esc(e.message)}${e.count>1?` ×${e.count}`:''}</li>`).join('');
    const lead=result.ok?'这台手机可以正常运行游戏。':'有项目需要留意。遇到按钮没反应时，请点「复制结果」发给我们。';
    showPanel('设备检测',kitSheet(`<p class="device-check-lead">${lead}</p><ul class="device-check">${rows}</ul>${errors?`<div class="gd-label">最近出错</div><ul class="device-check-errors">${errors}</ul>`:''}<div class="gd-row">${kitButton2('复制结果','data-copy-check')}${errors?kitButton2('清除记录','data-clear-check'):''}</div>`,kitButton('返回','data-help-back'),'','',{cls:'settings-content'}),'screen-panel settings-screen',{skin:'kitchen',icon:GEAR});
    panels.querySelector('[data-copy-check]').onclick=async()=>{alertBox(await platform.copyText(deviceCheckText(result))?'检测结果已复制，可以粘贴发送。':'这台手机不支持直接复制，请截图发送。');};
    panels.querySelector('[data-clear-check]')?.addEventListener('click',()=>{clearDeviceErrors();openDeviceCheck();});
    panels.querySelector('.close').onclick=openSettings;panels.querySelector('[data-help-back]').onclick=openSettings;
  }
  // Each step of the manual starts with the painted thing it is about (GPT work icons), not a number.
  const STEP_ART={'下一锅':'ic-pot','点厨具也能开火':'ic-flame','收取':'ic-basket','照顾厨房':'ic-broom','仓库':'crate-empty','神社':'ic-gift','整修':'ic-hammer','厨具':'ic-upgrade','调味料':'ic-jar','鸭蛋':'ic-duck-egg','开张':'ic-bell','菜单':'ic-book','订单':'ic-bill','出门':'ic-map','回来':'ic-chest','线索':'skill-OBS-1','去做':'ic-flame'};
  function openManual(index=0){
    const c=chapters[index];
    const body=`<div class="manual-lead"><span class="manual-art">${c.art()}</span><strong>${c.lead}</strong></div><ol class="manual-steps">${c.steps.map(([title,body],i)=>`<li><span class="manual-step-art" aria-hidden="true">${STEP_ART[title]?`<img src="/web/art/golden-ui/${STEP_ART[title]}.png" alt="">`:`<b class="gd-coin">${i+1}</b>`}</span><div><strong>${title}</strong><p>${body}</p></div></li>`).join('')}</ol>`;
    showPanel('鸡宝照顾手册',`${kitTabs(chapters.map((chapter,i)=>({label:chapter.title,attrs:`data-chapter="${i}"`,on:i===index})),'手册章节')}${kitSheet(body,kitButton('知道啦','data-done'),'','',{cls:'manual-pages'})}`,'screen-panel guide-screen',{skin:'kitchen',icon:sprite('book'),short:'照顾手册'});
    panels.querySelectorAll('[data-chapter]').forEach(button=>button.onclick=()=>openManual(+button.dataset.chapter));
    panels.querySelector('.close').onclick=openSettings;panels.querySelector('[data-done]').onclick=openSettings;
  }
  return {openSettings,openManual};
}
