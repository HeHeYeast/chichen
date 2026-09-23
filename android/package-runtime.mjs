import path from 'node:path';
import {fileURLToPath, pathToFileURL} from 'node:url';
import {createHash} from 'node:crypto';
import {readFile, writeFile, readdir, mkdir, rm, copyFile, lstat} from 'node:fs/promises';

// Start at the real game entry point. Review pages, old baselines, the recovered
// original APK and unused image candidates must never enter the Android bundle.
const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const destination=path.join(root,'android','generated-assets');
const allowed=new Set(), inspected=new Set();
const normalize=relative=>relative.split(path.sep).join('/');
function sourcePath(relative){
  const result=path.resolve(root,relative);
  if(!result.startsWith(root+path.sep))throw new Error(`Asset outside project: ${relative}`);
  return result;
}
// Final art for the 48 new species is still pending: the runtime manifest declares
// those variants `available:false` and draws labelled concept silhouettes instead.
// Only exactly those declared-unavailable paths are skipped; any other missing file still fails.
const {RUNTIME_ASSETS}=await import(pathToFileURL(path.join(root,'web','runtime-assets.generated.js')).href);
const pendingArt=new Set(Object.values(RUNTIME_ASSETS).flatMap(a=>Object.values(a.variants??{})).filter(v=>v.available===false).map(v=>v.path.replace(/^\//,'')));
function add(relative){
  const clean=normalize(relative).replace(/^\//,'').split(/[?#]/)[0];
  if(!clean||clean.includes('${'))return;
  if(pendingArt.has(clean))return;
  if(!/^(web|assets\/png|assets\/music|res\/raw)\//.test(clean))throw new Error(`Not a runtime asset: ${clean}`);
  if(/(?:^|\/)(?:baseline[^/]*|classic[^/]*|[^/]*review[^/]*|compare\.html|asset-plan\.html|art-board\.html)(?:\/|$)/.test(clean))throw new Error(`Non-runtime asset dependency: ${clean}`);
  sourcePath(clean);allowed.add(clean);return clean;
}
function resolveWeb(current,reference){
  if(/^(?:data:|https?:|#)/.test(reference))return null;
  return reference.startsWith('/')?reference.slice(1):normalize(path.join(path.dirname(current),reference));
}
async function walkModule(relative){
  relative=add(relative);
  if(!relative||inspected.has(relative))return;
  inspected.add(relative);
  const source=await readFile(sourcePath(relative),'utf8');
  for(const match of source.matchAll(/(?:\b(?:import|export)\s+(?:[^;]*?\s+from\s*)?|\bimport\s*\()(['"])([^'"\r\n]+)\1/g)){
    const reference=match[2];
    if(!reference.startsWith('.')&&!reference.startsWith('/'))throw new Error(`External dependency cannot be bundled offline: ${reference}`);
    const target=resolveWeb(relative,reference);
    if(target?.endsWith('.js'))await walkModule(target);
  }
  for(const match of source.matchAll(/['"`]((?:\/web\/art\/)[^'"`\r\n]+\.(?:png|jpg|webp|svg))(?:#[^'"`\r\n]*)?['"`]/g))add(match[1]);
}
async function walkCss(relative){
  relative=add(relative);
  if(!relative||inspected.has(relative))return;
  inspected.add(relative);
  const source=await readFile(sourcePath(relative),'utf8');
  for(const match of source.matchAll(/url\(\s*['"]?([^'"\s)]+)['"]?\s*\)/g)){
    const target=resolveWeb(relative,match[1]);
    if(target?.endsWith('.css'))await walkCss(target);else if(target)add(target);
  }
}
async function addTree(relative,extensions){
  for(const item of await readdir(sourcePath(relative),{withFileTypes:true})){
    const next=normalize(path.join(relative,item.name));
    if(item.isSymbolicLink())throw new Error(`Symlink is not permitted in the game bundle: ${next}`);
    if(item.isDirectory())await addTree(next,extensions);
    else if(extensions.has(path.extname(next).toLowerCase()))add(next);
  }
}

const html=await readFile(sourcePath('web/index.html'),'utf8');add('web/index.html');
for(const match of html.matchAll(/(?:href|src)=["']([^"']+)["']/g)){
  const target=resolveWeb('web/index.html',match[1]);
  if(target?.endsWith('.js'))await walkModule(target);
  else if(target?.endsWith('.css'))await walkCss(target);
  else if(target)add(target);
}
const {ASSET_FILES,SPRITES}=await import(pathToFileURL(sourcePath('web/art/manifest.js')));
const {FARM_ART_FILES}=await import(pathToFileURL(sourcePath('web/farm-theme.js')));
const {artworkOverrides}=await import(pathToFileURL(sourcePath('web/catalog.js')));
for(const file of [...ASSET_FILES,...FARM_ART_FILES,...Object.values(SPRITES).map(sprite=>sprite.file),...Object.values(artworkOverrides.characters),...Object.values(artworkOverrides.tools)])add(file);
await addTree('assets/png',new Set(['.png','.jpg','.jpeg','.webp']));
await addTree('assets/music',new Set(['.mp3','.ogg','.wav']));
await addTree('res/raw',new Set(['.mp3','.ogg','.wav']));
add('web/fonts/OFL-NotoSansSC.txt');
const files=[];
for(const relative of [...allowed].sort()){
  const info=await lstat(sourcePath(relative));
  if(!info.isFile()||info.isSymbolicLink())throw new Error(`Invalid runtime file: ${relative}`);
  const data=await readFile(sourcePath(relative));
  files.push({path:relative,size:data.length,sha256:createHash('sha256').update(data).digest('hex')});
}
// The one recursive removal is restricted to this exact generated directory.
if(destination!==path.join(root,'android','generated-assets'))throw new Error('Unexpected packaging destination');
await rm(destination,{recursive:true,force:true});await mkdir(destination,{recursive:true});
for(const file of files){
  const target=path.join(destination,file.path);
  await mkdir(path.dirname(target),{recursive:true});await copyFile(sourcePath(file.path),target);
}
const release=JSON.parse(await readFile(sourcePath('android/release.json'),'utf8'));
await writeFile(path.join(destination,'web','app-version.json'),JSON.stringify({applicationId:release.applicationId,versionName:release.versionName,versionCode:release.versionCode})+'\n');
const manifest={schema:1,entry:'web/index.html',applicationId:release.applicationId,versionName:release.versionName,versionCode:release.versionCode,files:files.length,bytes:files.reduce((sum,file)=>sum+file.size,0),contentHash:createHash('sha256').update(JSON.stringify(files)).digest('hex'),assets:files};
await writeFile(path.join(destination,'runtime-manifest.json'),JSON.stringify(manifest,null,2)+'\n');
console.log(`Offline game packaged: ${manifest.files} source files, ${(manifest.bytes/1024/1024).toFixed(1)} MiB. Runtime SHA-256: ${manifest.contentHash}`);
