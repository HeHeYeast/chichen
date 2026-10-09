import {build} from '../../cloud/node_modules/esbuild/lib/main.js';
import {copyFileSync,mkdirSync,writeFileSync,statSync,readFileSync} from 'node:fs';
import {fileURLToPath} from 'node:url';
const root=fileURLToPath(new URL('../../',import.meta.url)),dir=fileURLToPath(new URL('dist/',import.meta.url));mkdirSync(dir,{recursive:true});
const built=await build({entryPoints:[fileURLToPath(new URL('game.js',import.meta.url))],outfile:dir+'/game.js',bundle:true,format:'iife',platform:'neutral',target:'es2020',metafile:true,minify:false});
copyFileSync(new URL('game.json',import.meta.url),dir+'/game.json');
const files=Object.keys(built.metafile.inputs),report={bundleBytes:statSync(dir+'/game.js').size,moduleCount:files.length,modules:files,browserCoupling:files.filter(p=>/\b(document|window|localStorage|HTMLCanvasElement)\b/.test(readFileSync(p,'utf8'))),runtime:'build only; run verify.mjs for evidence'};
mkdirSync(root+'/artifacts/wechat-probe',{recursive:true});writeFileSync(root+'/artifacts/wechat-probe/build.json',JSON.stringify(report,null,2));console.log(JSON.stringify({bundleBytes:report.bundleBytes,moduleCount:report.moduleCount,browserCoupling:report.browserCoupling},null,2));
