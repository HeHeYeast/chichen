// Presentation only. Existing character pixels retain their identity and palette.
import {characterImage,toolImage} from './catalog.js';
import {GOLDEN_PORTRAITS} from './golden-portrait-metrics.js';
import {JOURNEY_METRICS} from './journey-art-metrics.js';
import {productionAsset} from './production-art.js';
export const journeyPath=name=>`/web/art/golden-journey/${name}.png`;
export const journeyArt=(name,cls='')=>{
 const m=JOURNEY_METRICS[name];
 // SVG is solely a raster crop viewport, never a replacement drawing.
 return m&&name!=='world-terrain'?`<svg class="journey-art ${cls}" viewBox="${m.visualBounds.join(' ')}" preserveAspectRatio="${['precision-river','mat','precision-envelope-back','precision-envelope-front'].includes(name)?'none':`xMidY${m.anchorKind==='ground-center'?'Max':'Mid'} meet`}" aria-hidden="true" data-asset="${name}" data-anchor="${m.anchor.join(',')}"><image href="${journeyPath(name)}" width="${m.size[0]}" height="${m.size[1]}"/></svg>`:`<img class="journey-art ${cls}" src="${journeyPath(name)}" alt="" draggable="false">`;
};
export const journeyMaterial=id=>productionAsset('materials',Number(id),'ingredient-icon')??(id>=75&&id<=82?journeyPath(`material-${id}`):toolImage(2,id));
export const journeyUnknown=journeyPath('unknown');
export function journeyCharacter(key){
 const reference='precision-ref-character-'+key.replace(':','-'),ref=JOURNEY_METRICS[reference];
 if(ref)return `<svg class="journey-character" data-character="${key}" data-asset="${reference}" viewBox="${ref.visualBounds.join(' ')}" aria-hidden="true"><image href="${journeyPath(reference)}" width="${ref.size[0]}" height="${ref.size[1]}"/></svg>`;
 const [egg,id]=key.split(':').map(Number),path=characterImage(egg,id),m=GOLDEN_PORTRAITS[path];
 return m?`<svg class="journey-character" viewBox="${m.bounds.join(' ')}" aria-hidden="true"><image href="${path}" width="${m.size[0]}" height="${m.size[1]}"/></svg>`:`<img class="journey-character" src="${path}" alt="">`;
}
export const journeySeat=key=>`<span class="journey-seat">${journeyArt('mat')}${key?journeyCharacter(key):journeyArt('backpack','journey-empty-pack')}</span>`;
export const journeyPositions={"V": [22.83, 10.938], "T": [75.147, 13.454], "R": [46.152, 36.635], "B": [71.47, 59.935]};
// Reference-space cubic paths, measured in the 390 x 684 map viewport.
// The DOM path is also the source of arc-length positions for the live party.
const trunk='M 20 508 C 34 472 72 479 130 428 C 165 398 213 366 200 347 C 187 328 134 350 126 300';
export const journeyRoutePath=id=>({
 V:trunk+' C 116 266 151 237 168 215 C 197 181 166 176 160 152 C 149 129 184 107 170 98 C 164 91 157 88 149 85',
 R:trunk+' C 123 275 155 263 177 263',
 T:trunk+' C 117 266 157 226 204 207 C 238 193 250 171 264 160',
 B:'M 20 508 C 37 476 70 478 127 437 C 169 402 192 431 217 450 C 239 466 262 460 276 441'
})[id];
export const journeyEnvironment=()=>[["ref-map-hills", 1.4, -0.678, 97.49, 24.439], ["ref-map-ground", 1.4, 28.313, 97.49, 46.601], ["ref-map-river", 1.4, 24.599, 97.49, 31.267], ["ref-tree-left", 0.9, 8.1, 9.6, 9.6], ["ref-tree-round", 7.5, 41.5, 16.4, 13.3], ["ref-tree-tall", 85.9, 23.3, 11.6, 9.6], ["ref-grass-rock", 45.5, 59.5, 19.9, 9.4], ["ref-rock",86,49.5,7.8,4.0]].map(([name,x,y,w,h])=>{
 // Trees and rocks shrink about their centre with the smaller landmarks; the land layers keep their full span.
 const k=name.startsWith('ref-map-')?1:.85;[x,y,w,h]=[x+w*(1-k)/2,y+h*(1-k)/2,w*k,h*k];
 return `<span class="journey-environment" style="left:${+x.toFixed(3)}%;top:${+y.toFixed(3)}%;width:${+w.toFixed(3)}%;height:${+h.toFixed(3)}%">${journeyArt('precision-'+name)}</span>`;
}).join('');
export const journeyPlace=id=>'place-'+id.replace(':','-');
export const journeyDiscovery=id=>productionAsset('cards',id,'discovery-vignette')??journeyPath(id.includes('-S')?'magnifier':id.includes('-E')?'flag':'note');
