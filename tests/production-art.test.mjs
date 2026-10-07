import test from 'node:test';
import assert from 'node:assert/strict';
import fs from 'node:fs';
import path from 'node:path';
import os from 'node:os';
import crypto from 'node:crypto';
import {mergeProductionArt} from '../tools/production-art-manifest.mjs';

test('production manifest validates PNG bytes, identity, paths and final QA independently of batch size',()=>{
 const root=fs.mkdtempSync(path.join(os.tmpdir(),'chick-art-'));
 try {
  const bytes=Buffer.from('iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQIHWP4z8DwHwAFgAI/ScLttAAAAABJRU5ErkJggg==','base64');
  fs.mkdirSync(path.join(root,'web/art/production/ART-X'),{recursive:true});fs.writeFileSync(path.join(root,'web/art/production/ART-X/full.png'),bytes);
  const v={path:'/web/art/production/ART-X/full.png',width:1,height:1,available:true,sha256:crypto.createHash('sha256').update(bytes).digest('hex')};
  const asset={id:'ART-X',contentId:'custom',kind:'species',identityKey:'0:888',productionStatus:'art-approved',runtimeQA:'pending',variants:{full:v,portrait:v,silhouette:v}};
  const content={species:[{id:'custom',key:'0:888'}]},manifest={version:1,assets:{'ART-X':asset}};
  assert.equal(Object.keys(mergeProductionArt(root,content,{},manifest)).length,1);
  const bad=()=>structuredClone(manifest);
  let m=bad();m.assets['ART-X'].identityKey='0:0';assert.throws(()=>mergeProductionArt(root,content,{},m),/identity/);
  m=bad();m.assets['ART-X'].productionStatus='FINAL';assert.throws(()=>mergeProductionArt(root,content,{},m),/Runtime QA/);
  m=bad();m.assets['ART-X'].variants.full.path='/web/art/production/../../../outside.png';assert.throws(()=>mergeProductionArt(root,content,{},m),/escape/);
  m=bad();m.assets['ART-X'].variants.full.sha256='changed';assert.throws(()=>mergeProductionArt(root,content,{},m),/hash/);
  m=bad();delete m.assets['ART-X'].variants.silhouette;assert.throws(()=>mergeProductionArt(root,content,{},m),/variant/);
 }finally{
  assert.ok(path.resolve(root).startsWith(path.resolve(os.tmpdir())+path.sep+'chick-art-'));
  fs.rmSync(root,{recursive:true,force:true});
 }
});
