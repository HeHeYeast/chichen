// A short, read-only projection of the clue book. Keep its order and secrecy;
// profit and the number of other possible discoveries never reorder targets.
import * as E from './engine.js';
import {clueBookModel,narrowed} from './clue-book.js';

export const NEW_PARTNER_LIMIT=5;
export function newPartnerAdvice(s,egg,now=Date.now(),limit=NEW_PARTNER_LIMIT){
  const book=clueBookModel(s,egg,now);
  const rows=book.rows.filter(r=>r.held||r.canTry||r.scout||r.progress>0||r.tracked).map(r=>{
    let action='clues',label='看线索',note=r.need?.[0]??'先找更多线索',cash=null;
    if(r.trial?.inPot){action='collect';label='回厨房';note='这一锅还没收完';}
    else if(r.ready){
      try{
        cash=E.cookInfo({...s,egg,selected:r.ingredients},r.toolId,now).cost+
          r.ingredients.filter(id=>!(s.ingredients[id]>0)).reduce((n,id)=>n+E.ingredient(id).buy_cp,0);
        action=s.cp>=cash?'recipe':'funds';label=action==='recipe'?'准备这一锅':'看线索';
        note=action==='recipe'?`开火共 ${cash} CP`:`还差 ${cash-s.cp} CP`;
      }catch{note='查看配方所需条件';}
    }else if(r.canTry&&narrowed(r.guess)){action='guess';label='去试做';note=`第二味是${r.guess.group}类，有 ${r.guess.candidates.length} 种可选`;}
    else if(r.scout&&r.region){action='journey';label='去寻访';note=`下一条线索在${r.region.name}`;}
    return {...r,action,label,note,cash};
  });
  return {rows:rows.slice(0,limit),total:book.total,more:Math.max(0,rows.length-limit)};
}
