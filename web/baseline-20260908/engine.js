import { DATA } from './data.js';
import { originalRecipes } from './recipes.js';
import {characterIndex} from './catalog.js';

export const char = (egg,id) => characterIndex.get(`${egg}:${id}`);
export const tool = id => DATA.tools[1].find(t=>t.id===id);
export const ingredient = id => DATA.tools[2].find(t=>t.id===id);
export const label = o => o?.title_zh_CN ?? '???';
export const key = (egg,id) => `${egg}:${id}`;
export function freshState(now=Date.now()) {
  return {version:1,cp:600,kitchenLevel:0,toolLevels:[0,-1,-1,-1,-1,-1,-1,-1],ingredients:{0:1},selected:[],egg:0,duck:false,batch:null,farm:{},total:{},lastSeen:now,lastClean:now,farmFixed:now,farmChecked:now,dirty:false,alarm:false,music:true,sound:true,events:{}};
}
export function cookInfo(s,id) {
  const t=tool(id),lv=s.toolLevels[id];
  return {cost:t[`lv_${lv}_cook_cp`],minutes:t[`lv_${lv}_min`]};
}
export function startBatch(s,id,now=Date.now(),random=Math.random) {
  if(s.batch?.eggs.some(e=>!e.collected)) throw Error('请先收取这一批鸡宝。');
  if(s.toolLevels[id]<0) throw Error('请先在商店购买调理用具。');
  const {cost,minutes}=cookInfo(s,id);
  if(s.cp<cost) throw Error('CP不足。');
  const selected=[...new Set(s.selected)].filter(i=>s.ingredients[i]>0).slice(0,Math.min(3,s.kitchenLevel+1));
  const result=originalRecipes(s,s.egg,id,selected,now,random);
  if(!result || result.length!==24 || result.some(i=>!char(s.egg,i))) throw Error('配方数据异常，未扣除CP。');
  const duration=minutes*60000;
  const positions=Array.from({length:24},(_,i)=>({x:Math.trunc(80.5+31*(i%6)+Math.trunc(3-random()*6)),y:184+15+26*Math.floor(i/6)+Math.trunc(3-random()*6)})).sort((a,b)=>a.y-b.y);
  const eggs=positions.map((p,i)=>{
    // Original chooses a time from the 24-entry list independently for each egg.
    const reduction=minutes*55000/23*Math.floor(random()*24);
    return {...p,id:result[i],egg:s.egg,openAt:now+duration-reduction,blackAt:selected.includes(36)?null:now+3*duration-reduction,collected:false,status:'egg',flipped:random()<0.5,immune:selected.includes(18)};
  });
  s.cp-=cost;
  selected.forEach(i=>s.ingredients[i]--);
  s.selected=[];
  s.batch={tool:id,level:s.toolLevels[id],egg:s.egg,ingredients:selected,started:now,ends:now+duration,eggs};
  return s.batch;
}
const sickExempt=[[1,2,20,30,34,35,51,52,53,54,68],[1,2,14,15,19,20,27,28,29,36]];
const burntExempt=[[0,2,5,20,25,26,27,30,31,33,34,35,53,54,68,70,110],[0,2,4,14,15,16,17,18,19,20,27,28,29,36,38,53]];
export function updateBatch(s,now=Date.now(),random=Math.random) {
  const events=[];
  for(const e of s.batch?.eggs??[]) {
    if(e.collected) continue;
    if(e.status==='egg'&&now>e.openAt) {
      if(e.egg===0&&e.id===9&&now>e.openAt+10000)e.id=8;
      if(s.dirty&&!e.immune&&!sickExempt[e.egg].includes(e.id)&&random()<0.4) {
        e.id=1;
        if(s.kitchenLevel>=1&&random()<0.1){e.id=e.egg?19:34;if(s.kitchenLevel>=2&&random()<1/3){e.id=e.egg?29:53;if(s.kitchenLevel>=3&&random()<0.5)e.id=e.egg?36:68;}}
      }
      e.status='cracking';e.animationAt=now;events.push('break');
    }
    if(e.status==='cracking'&&now-e.animationAt>=2000){e.status='hatching';e.animationAt=now;events.push(e.egg?'duck':'chick');}
    if(e.status==='hatching'&&now-e.animationAt>=900)e.status='ready';
    if(e.status==='ready'&&e.blackAt&&now>e.blackAt&&s.batch.tool>0&&!burntExempt[e.egg].includes(e.id)) {
      if((e.egg===0&&[109,111,112,113].includes(e.id))||(e.egg===1&&[52,54,55,56].includes(e.id)))e.id=e.egg?53:110;
      else if((e.egg===0&&[69,71,72,73,74,75,76,77].includes(e.id))||(e.egg===1&&[37,39,40,41,42,43,44,45,46].includes(e.id)))e.id=e.egg?38:70;
      else {e.id=2;if(s.kitchenLevel>=1&&random()<1/50){e.id=e.egg?20:35;if(s.kitchenLevel>=2&&random()<(e.egg?1/20:1/3))e.id=e.egg?28:54;}}
    }
  }
  return events;
}
export function collect(s,index) {
  const e=s.batch?.eggs[index];
  if(!e||e.collected||!['ready','hatching'].includes(e.status))return false;
  e.collected=true;const k=key(e.egg,e.id);
  s.farm[k]=Math.min(99999,(s.farm[k]??0)+1);s.total[k]=Math.min(99999,(s.total[k]??0)+1);s.cp+=1;
  return true;
}
export function sell(s,selection) {
  const entries=Object.entries(selection);
  for(const [k,n] of entries)if(!Number.isInteger(n)||n<0||n>(s.farm[k]??0))throw Error('卖出数量有误。');
  const income=entries.reduce((v,[k,n])=>v+char(...k.split(':').map(Number)).cp_1*n,0);
  if(!income)throw Error('请先选择要卖出的数量。');
  entries.forEach(([k,n])=>s.farm[k]-=n);s.cp+=income;return income;
}
export function canBuyTool(s,id) {
  const level=s.toolLevels[id],next=level+1;
  if(next>2)return false;
  if(level>=0)return next<=s.kitchenLevel;
  if(id===0)return true;
  if(id<6)return s.toolLevels[id-1]>=0;
  if(id===6)return s.kitchenLevel>=3;
  return s.kitchenLevel>=3&&s.toolLevels[6]>=2;
}
export function buyTool(s,id) {
  if(!canBuyTool(s,id))throw Error('尚未满足购买条件。');
  const next=s.toolLevels[id]+1,cost=tool(id)[`lv_${next}_buy_cp`];
  if(s.cp<cost)throw Error('CP不足。');
  s.cp-=cost;s.toolLevels[id]=next;return cost;
}
export function availableIngredients(s) {
  const ids=new Set([0]);
  // StoreUnit.setTool2CanBuy: ownership levels are zero-based.
  const [lamp,pan,boil,fryer,oven,stew,kettle,box]=s.toolLevels;
  const duck=(s.total['1:0']??0)>0;
  const add=(condition,...values)=>{if(condition)values.forEach(id=>ids.add(id));};
  const total=Object.values(s.total).reduce((a,b)=>a+b,0);
  add(pan>=0,1,2);add(boil>=0||(duck&&kettle>=0),3);add(boil>=0,4);
  add(fryer>=0,5,6,7);add(oven>=0,8,9,10);add(boil>=1,9);
  add(s.total['0:1']>0||s.total['1:1']>0,11);add(stew>=0,12,13);
  add(stew>=0||fryer>=1,14);add(total>=100,15);add(s.total['0:18']>0,16);
  add(total>=300,17);add(total>=500,18);add(pan>=1,19,30,31);
  add(pan>=1||boil>=1,20);add(boil>=1,21,22,24);
  add(boil>=1||oven>=1||stew>=1||(duck&&pan>=1),23);
  add(fryer>=1,25,32);add(s.total['0:35']>0,26);
  add(oven>=1||(duck&&(lamp>=0||boil>=0||stew>=0)),27);
  add(stew>=1,28);add(stew>=1||(duck&&(fryer>=2||oven>=1||stew>=2||kettle>=2)),29);
  add(oven>=1||(duck&&oven>=0),33);add(stew>=1||(duck&&pan>=2),34);
  add(total>=2000,35);add(s.kitchenLevel>=2,36);add(pan>=2,37,38);
  add(boil>=2,39,40);add(fryer>=2||oven>=2||(duck&&pan>=2),41);
  add(oven>=2,42);add(stew>=2,43);add(duck&&pan>=0,44);
  add(duck&&(fryer>=0||stew>=1),45);
  add(duck&&(pan>=1||boil>=2||fryer>=2||oven>=2),46);
  add(duck&&boil>=1,47,48);add(duck&&stew>=1,49);add(duck&&pan>=2,50);
  add(duck&&boil>=2,51,52);add(duck&&fryer>=2,53);add(duck&&oven>=2,54,55);
  add(kettle>=0,56);add(kettle>=1,57);add(kettle>=2,58,59,60,61,62);
  add(duck&&kettle>=0,63,64);add(duck&&kettle>=1,65,66);add(duck&&kettle>=2,67);
  add(box>=1,71,72);add(duck&&box>=1,73,74);
  return [...ids].sort((a,b)=>a-b);
}
export function buyIngredient(s,id,count=1) {
  if(!availableIngredients(s).includes(id))throw Error('尚未解锁。');
  if(!Number.isInteger(count)||count<1)throw Error('数量有误。');
  if(Object.values(s.ingredients).reduce((a,b)=>a+b,0)+count>30)throw Error('调味料最多可持有30个。');
  const cost=ingredient(id).buy_cp*count;if(s.cp<cost)throw Error('CP不足。');
  s.cp-=cost;s.ingredients[id]=(s.ingredients[id]??0)+count;
}
export function canUpgradeKitchen(s){return s.kitchenLevel<3&&s.toolLevels.slice(0,6).every(l=>l>=s.kitchenLevel);}
export function upgradeKitchen(s){if(!canUpgradeKitchen(s))throw Error('请先将前六种调理用具升至当前厨房等级。');const cost=(s.kitchenLevel+1)*10000;if(s.cp<cost)throw Error('CP不足。');s.cp-=cost;s.kitchenLevel++;s.dirty=false;}
export function resume(s,now=Date.now()) {if(now-s.lastSeen>86400000)s.dirty=true;s.lastSeen=now;}
export function farmHP(s,now=Date.now()){return Math.max(0,100-Math.floor((now-s.farmFixed)/7200000));}
export function repairCost(s,now=Date.now()){const hp=farmHP(s,now);return Math.trunc(Math.min(200,(100-hp)*(hp<=0?2:hp<30?1.5:hp<60?1.2:1)));}
export function checkFarmLoss(s,now=Date.now(),random=Math.random){
  const elapsed=now-(s.farmChecked??s.farmFixed),hp=farmHP(s,now);
  if(elapsed<=86400000)return 0;
  let rate=hp<30?60-hp:hp<60?Math.ceil((60-hp)/2):0;
  if(rate===0)return 0;
  if(rate>=60){if(elapsed>4*86400000)rate=100;else if(elapsed>3*86400000)rate=80;else if(elapsed>2*86400000)rate=68;}
  s.farmChecked=now;let lost=0;
  for(const [k,n] of Object.entries(s.farm)){
    let keep=n;if(rate>0){keep=n>=10?Math.floor((100-rate)*n/100):Array.from({length:n},()=>random()*100<100-rate?1:0).reduce((a,b)=>a+b,0);}
    lost+=n-keep;s.farm[k]=keep;
  }
  return lost;
}
export function clean(s,now=Date.now()){if(!s.dirty)return; if(s.cp<40)throw Error('CP不足。');s.cp-=40;s.dirty=false;s.lastClean=now;}
export function repair(s,now=Date.now()){const cost=repairCost(s,now);if(s.cp<cost)throw Error('CP不足。');s.cp-=cost;s.farmFixed=now;s.farmChecked=now;}
export function readSave(storage,k,now=Date.now()) {
  try {
    const raw=storage.getItem(k);if(!raw)return freshState(now);
    const s=JSON.parse(raw);
    if(s.version!==1||!Number.isFinite(s.cp)||s.cp<0||!Array.isArray(s.toolLevels)||s.toolLevels.length!==8||!s.total||!s.farm||!s.ingredients)throw Error();
    resume(s,now);return s;
  } catch {return freshState(now);}
}
