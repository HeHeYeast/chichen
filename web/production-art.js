import {RUNTIME_ASSETS} from './runtime-assets.generated.js';

const byContent = new Map(Object.values(RUNTIME_ASSETS).map(a=>[`${a.kind}:${a.contentId}`,a]));
const byIdentity = new Map(Object.values(RUNTIME_ASSETS).filter(a=>a.identityKey).map(a=>[a.identityKey,a]));
const pathFor=(asset,variant)=>asset?.variants?.[variant]?.available?asset.variants[variant].path:null;
export const productionAsset=(kind,id,variant)=>pathFor(byContent.get(`${kind}:${id}`),variant);
export const productionCharacter=(egg,id,variant='full')=>pathFor(byIdentity.get(`${egg}:${id}`),variant);
