// Work L: the Android packer walks the runtime graph; pending art must be declared
// unavailable (and absent), while every declared-available variant must exist.
import test from 'node:test';
import assert from 'node:assert/strict';
import {existsSync,readFileSync} from 'node:fs';
import {RUNTIME_ASSETS} from '../web/runtime-assets.generated.js';

const root=new URL('../',import.meta.url);
test('declared asset availability matches the files the packer will copy',()=>{
  const variants=Object.values(RUNTIME_ASSETS).flatMap(a=>Object.values(a.variants??{}));
  assert.ok(variants.length>=144,'48 species × full/portrait/silhouette at least');
  for(const v of variants){const onDisk=existsSync(new URL(v.path.replace(/^\//,''),root));assert.equal(onDisk,v.available,`${v.path} available=${v.available}`);}
  const packer=readFileSync(new URL('android/package-runtime.mjs',root),'utf8');
  assert.match(packer,/pendingArt\.has\(clean\)/,'the packer skips only declared-unavailable pending art');
});

test('composed kit/settings/skill paths are included from the approved UI manifest',()=>{
  const manifest=JSON.parse(readFileSync(new URL('web/art/golden-ui/manifest.json',root),'utf8'));
  const paths=new Set(manifest.assets.map(a=>a.path));
  for(const file of ['arrow','ic-broom','ic-hourglass','ic-flame','ic-calendar','skill-CUL-1','branch-home','set-music','set-notify','skill-point']){
    const path=`/web/art/golden-ui/${file}.png`;
    assert.ok(paths.has(path),path);assert.ok(existsSync(new URL(path.slice(1),root)),path);
  }
  const packer=readFileSync(new URL('android/package-runtime.mjs',root),'utf8');
  assert.match(packer,/sourcePath\('web\/art\/golden-ui\/manifest\.json'\)/);
  assert.match(packer,/for\(const asset of uiKit\.assets\)add\(asset\.path\)/);
});
