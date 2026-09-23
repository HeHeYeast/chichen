export function createPlatform({window:host=globalThis.window,disabled=false}={}){
  const bridge=disabled?null:host?.ChickNative;
  const pending=new Map();let sequence=0;
  host?.addEventListener('chick:native',event=>{
    const detail=event.detail;
    if(detail?.type==='response'&&pending.has(detail.id)){
      pending.get(detail.id)(detail.result);pending.delete(detail.id);
    }
  });
  const request=(method,...args)=>new Promise((resolve,reject)=>{
    const id='request-'+(++sequence);pending.set(id,resolve);
    try{bridge[method](id,...args);}catch(error){pending.delete(id);reject(error);}
  });
  function notificationStatus(){
    if(!bridge)return {supported:false,message:'浏览器仅在游戏打开时提醒。安装 Android 版后可在后台通知。'};
    try{return JSON.parse(bridge.notificationStatus());}catch{return {supported:true,permissionGranted:false,message:'暂时无法读取通知设置。'};}
  }
  const info=bridge?JSON.parse(bridge.platformInfo()):{android:false,version:'1.5.0 · 241候选'};
  return {
    bridge,info,notificationStatus,
    requestNotifications:()=>bridge?request('requestNotifications'):Promise.resolve(notificationStatus()),
    openNotificationSettings:()=>bridge?.openNotificationSettings(),
    openExactAlarmSettings:()=>bridge?.openExactAlarmSettings(),
    openAppSettings:()=>bridge?.openAppSettings(),
    testDelayedNotification:()=>{try{return bridge?JSON.parse(bridge.testDelayedNotification()):{ok:false,message:'请在 Android 版中测试后台通知。'};}catch{return {ok:false,message:'请先更新 Android 安装包，再测试后台提醒。'};}},
    testNotification:()=>bridge?JSON.parse(bridge.testNotification()):{ok:false,message:'请在 Android 版中测试后台通知。'},
    closeApp:()=>bridge?.closeApp(),
    async exportBackup(text){
      if(bridge)return request('exportSave',text);
      const url=URL.createObjectURL(new Blob([text],{type:'application/json;charset=utf-8'}));
      const link=host.document.createElement('a');link.href=url;link.download='鸡宝厨房备份-'+new Date().toISOString().slice(0,10)+'.json';
      host.document.body.append(link);link.click();link.remove();setTimeout(()=>URL.revokeObjectURL(url),60000);
      return {ok:true,message:'已发起下载，请在下载列表中确认备份文件。'};
    },
    async selectBackup(){
      if(bridge)return request('importSave');
      return new Promise(resolve=>{
        const input=host.document.createElement('input');input.type='file';input.accept='.json,application/json';input.hidden=true;
        input.addEventListener('cancel',()=>{input.remove();resolve({ok:false,message:'已取消'});},{once:true});
        input.addEventListener('change',async()=>{
          try{const file=input.files?.[0];if(!file){resolve({ok:false,message:'已取消'});return;}
            if(file.size>2*1024*1024)throw Error('文件太大，请选择鸡宝厨房的 JSON 备份。');
            resolve({ok:true,raw:await file.text()});
          }catch(error){resolve({ok:false,message:error.message});}finally{input.remove();}
        },{once:true});
        host.document.body.append(input);input.click();
      });
    },
  };
}
