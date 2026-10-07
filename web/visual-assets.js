// Presentation-only paths. Final paintings can replace individual overrides
// without changing species IDs, saves or the authoritative content registry.
import {productionAsset} from './production-art.js';
const base='/web/art/polish';
export const unknownArt=`${base}/ui/unknown.svg`;
export const materialArt=id=>productionAsset('materials',Number(id),'ingredient-icon')??`${base}/materials/material-${Number(id)}.svg`;
export const regionalSpeciesArt=id=>productionAsset('species',id,'full')??`${base}/species/species-${id}.svg`;
export const discoveryArt=id=>productionAsset('cards',id,'discovery-vignette')??`${base}/discoveries/discovery-${id}.svg`;
export const mementoArt=id=>productionAsset('mementos',id,'display')??`${base}/mementos/memento-${id}.svg`;
export const visualImage=(path,kind='item')=>`<img class="polish-art polish-${kind}" src="${path}" alt="" aria-hidden="true" decoding="async">`;
export function progressTrack(value,max,label){
 const n=Math.max(0,Math.min(max,Number(value)||0)),total=Math.max(1,max);
 return `<span class="polish-track" role="progressbar" aria-label="${label}" aria-valuemin="0" aria-valuemax="${total}" aria-valuenow="${n}"><span style="width:${n/total*100}%"></span></span>`;
}
// Labels describe the original catalog rate only; never fabricate a rarity
// from selling price or a regional recipe's guaranteed target slot.
export function rarityBadge(c){
 if(c.pack==='regional')return '<span class="rarity-badge is-regional" title="地区限定做法，不代表本批出现概率">地方</span>';
 if(!Number.isFinite(c.rate)||c.rate<=0)return '<span class="rarity-badge is-special" title="特殊获取条件，以详情为准">特别</span>';
 const [kind,label]=c.rate<=10?['rare','珍稀']:c.rate<=30?['uncommon','少见']:['common','常见'];
 return `<span class="rarity-badge is-${kind}" title="原始图鉴权重分档，不代表本批实际概率">${label}</span>`;
}
