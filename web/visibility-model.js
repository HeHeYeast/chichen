import {resolveSpecies,resolveMaterial} from './content-registry.js';
import {speciesDiscovered} from './species-state.js';
export function projectSpecies(s,key){
  const c=resolveSpecies(key);if(!c)return null;
  const known=speciesDiscovered(s,c.egg,c.id),code=(c.egg?'D':'C')+String(c.id+1).padStart(3,'0');
  return {key,egg:c.egg,id:c.id,code,known,name:known?c.title_zh_CN:'未发现',...(known?{description:c.description??c.comment,artwork:c.artwork??null}:{}),concept:c.pack==='regional',price:known?c.cp_1:null};
}
export function projectMaterial(s,id){
  const m=resolveMaterial(id);if(!m)return null;const known=id<75||Object.hasOwn(s.expansion?.discovery?.identified??{},String(id));
  return {id,known,name:known?m.title_zh_CN:'未辨认材料',price:known?m.buy_cp:null};
}
