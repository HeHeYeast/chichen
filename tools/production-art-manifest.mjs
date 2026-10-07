import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';

const hash=bytes=>crypto.createHash('sha256').update(bytes).digest('hex');
const assert=(value,message)=>{if(!value)throw Error(message);};
export function mergeProductionArt(root,content,assets,manifest) {
  assert(manifest.version===1 && manifest.assets && typeof manifest.assets==='object','Invalid production manifest');
  const result=structuredClone(assets),identities=new Set();
  for(const [id,entry] of Object.entries(manifest.assets)) {
    assert(id===entry.id && /^[A-Za-z0-9][A-Za-z0-9_-]{0,95}$/.test(id),'Invalid production asset ID');
    assert(['species','materials','cards','mementos','regulars'].includes(entry.kind),'Unsupported production asset kind');
    const definition=content[entry.kind]?.find(row=>row.id===entry.contentId);
    assert(definition,'Unknown production content');
    const identity=`${entry.kind}:${entry.contentId}`;
    assert(!identities.has(identity),'Duplicate production content');identities.add(identity);
    if(entry.kind==='species')assert(entry.identityKey===definition.key,'Production identity drift');
    if(result[id])assert(result[id].kind===entry.kind&&result[id].contentId===entry.contentId,'Production mapping drift');
    assert(['art-approved','FINAL'].includes(entry.productionStatus),'Unapproved production status');
    if(entry.productionStatus==='FINAL')assert(entry.runtimeQA==='passed','Final production art lacks Runtime QA');
    const required={species:['full','portrait','silhouette'],materials:['ingredient-icon','specimen-cutout','shop-bundle'],cards:['discovery-vignette'],mementos:['display','small-icon'],regulars:['portrait']}[entry.kind];
    assert(required.every(v=>entry.variants?.[v]?.available),'Missing production variant');
    for(const v of Object.values(entry.variants)) {
      assert(v.path?.startsWith('/web/art/production/')&&!v.path.includes('\\'),'Invalid production path');
      const file=path.resolve(root,'.'+v.path),base=path.resolve(root,'web/art/production');
      assert(file.startsWith(base+path.sep),'Production path escape');
      const bytes=fs.readFileSync(file);
      assert(bytes.subarray(0,8).equals(Buffer.from([137,80,78,71,13,10,26,10])),'Production asset is not PNG');
      assert(bytes.readUInt32BE(16)===v.width&&bytes.readUInt32BE(20)===v.height,'Production dimensions drift');
      assert(hash(bytes)===v.sha256,'Production asset hash drift');
    }
    result[id]={...result[id],...entry};
  }
  return result;
}
