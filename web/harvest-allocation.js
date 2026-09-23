import {inventoryView} from './inventory.js';
import {resolveSpecies} from './content-registry.js';
import {reserveForOrder} from './orders.js';
import {basketQuote} from './progression.js';
import {sell} from './engine.js';

export function harvestStock(s){
  const rows={};
  for(const e of s.batch?.eggs??[])if(e.collected){const k=`${e.egg}:${e.id}`;rows[k]=(rows[k]??0)+1;}
  return rows;
}
// A draft is not S. Validate all three destinations together so a bird cannot
// be sold/reserved and also offered as stock in the next business preparation.
export function planHarvestAllocation(s,{rows={},orderId=null,keepOne=true,useRewards=false}={}){
  const harvested=harvestStock(s),sale={},business={},reserved={};
  if(!s.batch?.eggs.every(e=>e.collected))throw Error('先收完这一锅，再安排收成。');
  for(const [key,row] of Object.entries(rows)){
    if(!harvested[key])throw Error('这位伙伴不在本锅收成中。');
    const amounts=['sale','business','order'].map(k=>row[k]??0);
    if(amounts.some(n=>!Number.isSafeInteger(n)||n<0))throw Error('请填写整只数量。');
    const n=amounts.reduce((a,b)=>a+b,0),v=inventoryView(s,key);
    if(n>harvested[key]||n>v.free||keepOne&&n>Math.max(0,v.home-v.Q-1))throw Error('分配超过本锅自由库存，或没有在家留种。');
    const lock=s.expansion.inventoryPolicy?.collectionLocks;
    if(n&&(Array.isArray(lock)?lock.includes(key):lock?.[key]))throw Error('先在库存中解除收藏保护，再分配这位伙伴。');
    if(row.business&&!resolveSpecies(key).edible)throw Error('观赏伙伴不放入食用菜单。');
    if(row.sale)sale[key]=row.sale;if(row.business)business[key]=row.business;if(row.order)reserved[key]=row.order;
  }
  if(Object.keys(business).length>6)throw Error('一份营业草稿最多选择6种。');
  // Exercise the real reservation validator on a detached preview, without
  // changing inventory or emitting delivery facts.
  const preview=structuredClone(s);
  for(const [key,n] of Object.entries(reserved))reserveForOrder(preview,orderId,key,n);
  return {sale,business,reserved,orderId,keepOne,useRewards,quote:basketQuote(s,sale,{useRewards})};
}
export function allocateHarvest(s,options,now){
  const p=planHarvestAllocation(s,options);
  for(const [key,n] of Object.entries(p.reserved))reserveForOrder(s,p.orderId,key,n);
  if(Object.keys(p.sale).length)sell(s,p.sale,{useRewards:p.useRewards},now);
  return p;
}
