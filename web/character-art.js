// One portrait path for every UI. Atlas fragments are identities, never image URLs.
import {characterImage} from './catalog.js';
import {productionCharacter} from './production-art.js';
import {resolveSprite,spriteSVG} from './art/manifest.js';
import {originalPortraitFrame} from './portrait-frames.js';
import {GOLDEN_PORTRAITS} from './golden-portrait-metrics.js';

export function characterPortraitSprite(egg,id){
  const portrait=productionCharacter(egg,id,'portrait');
  if(portrait)return {file:portrait};
  const path=characterImage(egg,id),resolved=resolveSprite(path);
  if(resolved.frame)return resolved;
  const metrics=GOLDEN_PORTRAITS[path];
  if(metrics)return {file:path,frame:metrics.bounds,size:metrics.size};
  const original=originalPortraitFrame(egg,id);
  return original?.file===path?original:resolved;
}

export function characterPortraitMarkup(egg,id,className='sprite-art'){
  const sprite=characterPortraitSprite(egg,id);
  return sprite.frame?spriteSVG(sprite).replace('class="sprite-art"',`class="${className}"`)
    :`<img class="${className}" src="${sprite.file}" alt="" draggable="false">`;
}
