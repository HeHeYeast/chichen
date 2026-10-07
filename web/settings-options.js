// Settings content for the kit page: switch rows under painted section labels, then a grid of
// painted shortcut tiles (no line icons), then one short save note.
const ART='/web/art/';
const TILE_ART={
  workshop:'chef',journal:ART+'golden-ui/ic-calendar.png',guides:ART+'golden-journey/flag.png',manual:'book',
  save:ART+'golden-journey/note.png',export:ART+'golden-journey/envelope.png',import:ART+'golden-journey/backpack.png',
  device:ART+'golden-ui/ic-check.png',
};
// Each switch row starts with its painted picture (GPT work set b4-3).
const ROW_ART={keepOne:'set-keep',music:'set-music',sound:'set-sound',alarm:'set-hatch',permission:'set-notify','background-help':'set-background'};
export function settingsOptions({state:s,platform,sprite}){
  const status=platform.notificationStatus(),android=platform.info.android;
  const toggle=(id,label,on)=>`<div class="settings-switch-row" data-row><i class="settings-row-art" aria-hidden="true"><img src="${ART}golden-ui/${ROW_ART[id]}.png" alt=""></i><strong>${label}</strong><button class="game-switch ${on?'on':''}" role="switch" aria-checked="${on}" aria-label="${label}" data-toggle="${id}"><span>${on?'开':'关'}</span><i></i></button></div>`;
  const art=key=>{const src=TILE_ART[key];return src==='chef'||src==='book'?sprite(src):`<img src="${src}" alt="">`;};
  const tile=(attr,key,label)=>`<button type="button" class="settings-tile" data-${attr}><span class="settings-tile-art" data-visual>${art(key)}</span><b>${label}</b></button>`;
  return `<div class="gd-label">声音与提醒</div>${toggle('music','背景音乐',s.music)}${toggle('sound','游戏音效',s.sound)}${toggle('alarm','孵化完成提醒',s.alarm)}
    <p class="notification-status" data-notification-status></p>${android?`<div class="notification-actions">${[['permission','系统通知'],...(!status.exactAllowed?[['exact','准时提醒']]:[]),...(status.backgroundAllowed===false?[['background','允许后台']]:[]),['test-notice','测试通知',!status.notificationsEnabled],['test-delayed','1 分钟测试',!status.notificationsEnabled],['background-help','后台帮助']].map(([id,label,off])=>`<button type="button" class="gd-btn2${ROW_ART[id]?' has-art':''}" data-${id}${off?' disabled':''}>${ROW_ART[id]?`<img src="${ART}golden-ui/${ROW_ART[id]}.png" alt="">`:''}${label}</button>`).join('')}</div>`:''}
    <div class="gd-label">更多</div><div class="settings-tiles">${tile('settings-workshop','workshop','手艺')}${tile('journal','journal','日历')}${tile('replay-guides','guides','重看指引')}${tile('manual','manual','游戏说明')}${tile('save','save','保存')}${tile('export','export','导出备份')}${tile('import','import','导入备份')}${tile('device-check','device','设备检测')}</div>
    <p class="save-note">${android?'覆盖更新会保留进度；卸载前请先导出备份':'进度保存在这个浏览器；换浏览器或清数据前请先导出备份'}</p><p class="settings-version">鸡宝厨房 · ${android?'Android':'浏览器'} ${platform.info.version}</p>`;
}
