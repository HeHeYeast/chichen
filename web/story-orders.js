import {RULES,STORY_CHAPTERS} from './integration-data.js';

import {collectedTotal,discoveryCount,checkedIncome} from './progression.js';
import {availableCount,validateConsumption} from './inventory.js';
import {advanceWorld} from './world-clock.js';
import {GAME_DATA} from './content-pack.js';
import {recipePaths,recipeId} from './recipe-book.js';
import {clueCandidates} from './knowledge.js';
export function storyOrders(s){
  return RULES.storyOrders.map((order,index)=>{
    const saved=s.progress.orders[order.id]??{accepted:false,choice:null,delivered:0,completed:false};
    const requirements=[{met:collectedTotal(s)>=order.requiredCollected,text:'累计收取 '+order.requiredCollected+' 只'},
      ...(order.previous?[{met:s.progress.orders[order.previous]?.completed===true,text:'完成上一笔生意'}]:[]),
      ...(order.requiredDiscoveries?[{met:discoveryCount(s)>=order.requiredDiscoveries,text:'发现 '+order.requiredDiscoveries+' 种'}]:[]),
      ...(order.requiredTool!==undefined?[{met:s.toolLevels[order.requiredTool]>=0,text:'持有水煮锅'}]:[]),
      ...(order.requiredKitchenDisplayLevel?[{met:s.kitchenLevel+1>=order.requiredKitchenDisplayLevel,text:'厨房 Lv.'+order.requiredKitchenDisplayLevel}]:[])];
    const choices=(order.items??order.chooseOne).map(item=>{const [egg,id]=item.species.split(':').map(Number),c=GAME_DATA.characters[egg].find(c=>c.id===id);return {...item,name:c.title_zh_CN,price:c.cp_1,available:item.requiredTool===undefined||s.toolLevels[item.requiredTool]>=0,atHome:availableCount(s,item.species)};});
    return {...order,...saved,index,chapter:STORY_CHAPTERS[index],requirements,unlocked:requirements.every(r=>r.met),choices};
  });
}
export function acceptOrder(s,id,choice=null){
  const o=storyOrders(s).find(o=>o.id===id);if(!o?.unlocked||o.completed)throw Error('这笔生意还不能接');
  const selected=o.choices.find(c=>c.species===(choice??o.choice??o.choices[0].species));if(!selected?.available)throw Error('该选项需要先购买对应厨具');
  if(o.delivered>0&&selected.species!==o.choice)throw Error('已经部分交付，不能再更换出品');
  s.progress.orders[id]={accepted:true,choice:selected.species,delivered:o.delivered,completed:false};
  if(id==='tea-party')for(const r of recipePaths('0:10'))if(!s.progress.knowledge.recipes.includes(recipeId(r)))s.progress.knowledge.recipes.push(recipeId(r));
  return true;
}
export function deliverOrder(s,id,count,now=Date.now(),random=Math.random){
  advanceWorld(s,now,random);
  const o=storyOrders(s).find(o=>o.id===id),item=o?.choices.find(c=>c.species===o.choice);
  if(!o?.unlocked||!o.accepted||o.completed||!item)throw Error('采购状态已变化');
  if(!Number.isSafeInteger(count)||count<1||count>item.count-o.delivered)throw Error('交付数量无效');
  validateConsumption(s,{[item.species]:count});
  const complete=o.delivered+count===item.count,extra=complete?o.extraCP:0,income=count*item.price+extra;
  const balance=checkedIncome(s,income);
  s.farm[item.species]-=count;s.cp=balance;
  Object.assign(s.progress.orders[id],{delivered:o.delivered+count,completed:complete});
  let clue=null;
  if(complete&&id==='signature-table'){
    clue=RULES.exploration.routes.flatMap(r=>clueCandidates(s,r,now)).find(c=>c.level===1)??null;
    if(clue)s.progress.knowledge.facts.push(clue.fact);
  }
  return {income,extra,complete,clue};
}
