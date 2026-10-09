import {kitSheet,kitButton,kitButton2} from './ui-kit.js';
import {createCloudAPI,createManualCloud} from './cloud-client.js';
import {cloudEndpoint} from './cloud-config.js';
import {parseSave} from './engine.js';
const esc=s=>String(s).replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const summary=raw=>{const s=parseSave(raw);return `厨房 Lv.${s.kitchenLevel+1} · ${s.cp} CP · 认识 ${Object.values(s.total).filter(n=>n>0).length} 种伙伴 · ${s.progress.trip?'有寻访记录':'尚未寻访'}`;};
export function createCloudUI({vault,storage,sessionStorage,saveKey,getRaw,replaceRaw,flush,panels,showPanel,alertBox,confirmBox,back,writable}){
  const endpoint=cloudEndpoint(location),sessionKey='chick-cloud-session-v1';
  let busy=false;
  const session=()=>{try{const s=JSON.parse(sessionStorage.getItem(sessionKey));return s?.endpoint===endpoint&&s?.uid===vault.active?.uid?s:null;}catch{return null;}};
  const api=endpoint?createCloudAPI({endpoint,token:()=>session()?.token}):null;
  const manager=()=>{const s=session();if(!s)throw Error('请先登录此账号。');return createManualCloud({api,storage,key:saveKey,getRaw,replaceRaw,epoch:s.epoch});};
  function shell(title,body){showPanel(title,kitSheet(body,kitButton('返回','data-cloud-back'),'','',{cls:'settings-content'}),'screen-panel settings-screen cloud-screen',{skin:'kitchen'});panels.querySelector('[data-cloud-back]').onclick=back;panels.querySelector('.close').onclick=back;}
  async function work(fn){if(busy)return;if(!writable())return alertBox('当前窗口不能写入，请在可保存的窗口操作。');busy=true;try{await fn();}catch(e){alertBox(e.message);}finally{busy=false;}}
  function open(){
    const active=vault.active,s=session();
    shell('账号与云备份',`<p>${active?`本机账号：${esc(active.username)}`:'当前为游客进度。注册后会保留并复制这份进度；登录已有账号时，游客进度仍留在本机，退出账号可返回。'}</p><p>本机自动保存；云端由你手动备份和恢复。</p>${endpoint?'': '<p>云服务尚未配置。你仍可正常游玩，并在设置导出备份。</p>'}${!s?`<label>用户名 <input data-cloud-name autocomplete="username" maxlength="24"></label><label>密码 <input data-cloud-password type="password" autocomplete="current-password" maxlength="128"></label><div class="gd-row">${kitButton('登录','data-cloud-login')}${!active?kitButton2('注册并保留进度','data-cloud-register'):''}</div><p>不收集邮箱或手机号，目前没有自助找回。请妥善保管密码，定期导出本机备份。</p>`:`<p>登录仅用于备份，不会自动覆盖本机。</p><div class="gd-row">${kitButton('备份到云端','data-cloud-upload')}${kitButton2('查看云端并恢复','data-cloud-preview')}${kitButton2('历史备份','data-cloud-history')}</div>`}${active?kitButton2('退出账号，保留本机副本','data-cloud-logout'):''}`);
    for(const [attr,path]of [['login','sessions'],['register','accounts']])panels.querySelector('[data-cloud-'+attr+']')?.addEventListener('click',()=>work(async()=>{
      if(!api)throw Error('尚未配置云服务，本机进度仍保留。');if(!flush())throw Error('请先解决本机保存问题。');
      const username=panels.querySelector('[data-cloud-name]').value,password=panels.querySelector('[data-cloud-password]').value;
      const auth=await api(path,{method:'POST',body:{username,password}});panels.querySelector('[data-cloud-password]').value='';
      // Prepare profile before switching pointer. Failure leaves the old active profile untouched.
      vault.activate(auth,{copyGuest:path==='accounts'});sessionStorage.setItem(sessionKey,JSON.stringify({...auth,endpoint}));location.reload();
    }));
    panels.querySelector('[data-cloud-upload]')?.addEventListener('click',()=>work(async()=>{if(!flush())throw Error('本机进度尚未保存。');const result=await manager().backup();alertBox(result.phase==='backed-up'?'云端已收到这份备份。':result.phase==='conflict'?'备份已收到，但云端又有其他进度，请查看云端。':'刚才的备份已收到；本机又有新进度，可再次备份。');}));
    panels.querySelector('[data-cloud-preview]')?.addEventListener('click',()=>work(()=>preview()));
    panels.querySelector('[data-cloud-history]')?.addEventListener('click',()=>work(()=>history()));
    panels.querySelector('[data-cloud-logout]')?.addEventListener('click',()=>work(async()=>{if(!flush())throw Error('本机尚未保存，暂不退出。');try{if(s)await api('sessions/current',{method:'DELETE'});}catch{}vault.logout();sessionStorage.removeItem(sessionKey);location.reload();}));
  }
  async function preview(revision){
    if(!flush())throw Error('本机尚未保存。');const sync=manager(),p=await sync.preview(revision);
    if(revision){const head=await api('saves/current');if(head.epoch!==p.epoch||!head.save)throw Error('云端记录已变化。');p.headRevision=head.save.revision;}
    shell('选择备份进度',`<p>这台设备：${esc(summary(p.localRaw))}</p><p>云端：${p.remote?esc(summary(p.remote.raw))+'<br>'+esc(new Date(p.remote.createdAt).toLocaleString()):'还没有备份'}</p><p>恢复前会保存本机副本；上传替换前云端会保留历史。不会合并 CP、库存或奖励。</p><div class="gd-row">${p.remote?kitButton('恢复这份云端进度','data-cloud-restore'):''}${!revision?kitButton2('使用本机进度备份','data-cloud-choose-local'):''}</div>`);
    panels.querySelector('[data-cloud-restore]')?.addEventListener('click',()=>confirmBox('确认恢复这份云端进度？当前本机进度会保留一份恢复前备份。',()=>work(async()=>{await sync.restore(p);location.reload();}),false,{title:'恢复进度',yes:'恢复',no:'取消'}));
    panels.querySelector('[data-cloud-choose-local]')?.addEventListener('click',()=>confirmBox('确认用这台设备的进度建立新的云端备份？云端原进度会进入历史记录。',()=>work(async()=>{await sync.chooseLocal(p);await sync.backup();alertBox('云端已收到备份。');}),false,{title:'备份进度',yes:'备份',no:'取消'}));
  }
  async function history(before){
    const result=await manager().history({before});shell('云端历史备份',result.items.length?result.items.map(s=>`<p>${esc(new Date(s.created_at).toLocaleString())} ${kitButton2('查看','data-cloud-revision="'+s.revision+'"')}</p>`).join('')+(result.nextCursor?kitButton2('更早的备份','data-cloud-more'):''):'<p>暂无历史备份。</p>');
    panels.querySelectorAll('[data-cloud-revision]').forEach(button=>button.onclick=()=>work(()=>preview(Number(button.dataset.cloudRevision))));panels.querySelector('[data-cloud-more]')?.addEventListener('click',()=>work(()=>history(result.nextCursor)));
  }
  function feedback(){
    const key=saveKey+'.feedback-draft';let draft;
    try{draft=JSON.parse(storage.getItem(key))??{id:crypto.randomUUID(),text:''};}catch{return alertBox('反馈草稿无法读取，请先导出诊断。');}
    shell('反馈与建议',`<p>描述遇到的问题或建议。请勿填写密码等敏感信息；不会自动附上存档。</p><label>反馈内容<textarea data-feedback-text maxlength="2000" rows="6">${esc(draft.text)}</textarea></label><p>断网时草稿留在本机；联网后点提交重试。</p>${kitButton('提交反馈','data-feedback-send')}`);
    panels.querySelector('[data-feedback-text]').oninput=e=>{draft={id:crypto.randomUUID(),text:e.target.value};try{storage.setItem(key,JSON.stringify(draft));}catch{alertBox('草稿暂未保存，请复制保留。');}};
    panels.querySelector('[data-feedback-send]').onclick=()=>work(async()=>{storage.setItem(key,JSON.stringify(draft));if(!api)throw Error('草稿已保存；云服务配置后可提交。');const result=await api('feedback',{method:'POST',body:draft});storage.removeItem(key);alertBox('反馈已收到，编号：'+result.id);back();});
  }
  return {open,feedback};
}
