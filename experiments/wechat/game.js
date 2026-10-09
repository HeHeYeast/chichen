// Isolated technical probe, not a replacement for any production game screen.
import {freshState,startBatch,updateBatch,collect} from '../../web/engine.js';
import {execute} from '../../web/game-commands.js';
import {createSaveStore} from '../../web/save-store.js';
if(!globalThis.structuredClone)globalThis.structuredClone=value=>JSON.parse(JSON.stringify(value));
if(!globalThis.TextEncoder)globalThis.TextEncoder=class {encode(value){const out=[];for(const ch of String(value)){let c=ch.codePointAt(0);if(c>=0xd800&&c<=0xdfff)c=0xfffd;if(c<128)out.push(c);else if(c<2048)out.push(192|(c>>6),128|(c&63));else if(c<65536)out.push(224|(c>>12),128|((c>>6)&63),128|(c&63));else out.push(240|(c>>18),128|((c>>12)&63),128|((c>>6)&63),128|(c&63));}return new Uint8Array(out);}};
const key='jibao-wechat-compatibility-probe-v1';
const storage={getItem:key=>{const value=wx.getStorageSync(key);return value===''||value===undefined?null:value;},setItem:(key,value)=>wx.setStorageSync(key,value)};
const store=createSaveStore({storage,key,writer:{writable:true},now:()=>Date.now()});
const loaded=store.load();let state=loaded.state??freshState(Date.now(),42),error=loaded.error??'',message='复用现有规则与 schema 6；仅技术原型';
const canvas=wx.createCanvas(),info=wx.getSystemInfoSync(),width=info.windowWidth,height=info.windowHeight,dpr=info.pixelRatio||1;
canvas.width=width*dpr;canvas.height=height*dpr;const ctx=canvas.getContext('2d');ctx.scale(dpr,dpr);
function draw(){
  ctx.fillStyle='#fff5dc';ctx.fillRect(0,0,width,height);ctx.fillStyle='#493827';ctx.font='bold 22px sans-serif';ctx.fillText('鸡宝厨房 · 兼容性实验',18,48);
  ctx.font='16px sans-serif';ctx.fillText('CP '+state.cp+'   schema '+state.version,18,88);ctx.fillText('认识 '+Object.keys(state.total).length+' 种伙伴',18,118);
  for(let i=0;i<24;i++){const egg=state.batch?.eggs[i];ctx.fillStyle=egg?.collected?'#bbcab0':'#e8b85f';ctx.beginPath();ctx.arc(40+(i%6)*50,170+Math.floor(i/6)*40,13,0,Math.PI*2);ctx.fill();}
  for(const [y,label]of [[360,'开一锅（共用真实规则）'],[420,'推进并收取（原型测试）'],[480,'读回本地存档']]){ctx.fillStyle='#6b8454';ctx.fillRect(18,y,width-36,44);ctx.fillStyle='white';ctx.fillText(label,28,y+28);}
  ctx.fillStyle=error?'#a32020':'#493827';ctx.font='12px sans-serif';ctx.fillText(error||message,18,555,width-36);
}
function command(type,reduce,at=Date.now()){
  if(error)throw Error(error);
  const result=execute({state,store,command:{type},now:at,reduce});state=result.state;draw();return state;
}
function start(){return command('probe-start',(s,c)=>{if(s.batch?.eggs.some(e=>!e.collected))throw Error('请先收取当前锅。');startBatch(s,0,c.now,c.random);});}
function harvest(){
  if(!state.batch)return state;
  const at=Math.max(...state.batch.eggs.map(e=>e.openAt));
  return command('probe-harvest',(s,c)=>{updateBatch(s,at+1,c.random);updateBatch(s,at+2001,c.random);updateBatch(s,at+2901,c.random);for(let i=0;i<s.batch.eggs.length;i++)collect(s,i,at+2901);},at+2901);
}
function reload(){const next=store.load();if(next.error){error=next.error;throw Error(error);}state=next.state;message='已从 wx 存储接口重新读回';draw();return state;}
wx.onTouchStart(event=>{const y=event.touches[0]?.clientY;try{if(y>=360&&y<404)start();else if(y>=420&&y<464)harvest();else if(y>=480&&y<524)reload();}catch(e){message=e.message;draw();}});
wx.onHide(()=>{try{if(!error)store.write(state);}catch(e){error=e.message;}});
// Deliberately exposed only inside this isolated probe for the compatibility runner.
globalThis.JibaoProbe={start,harvest,reload,state:()=>JSON.parse(JSON.stringify(state)),draw};draw();
