// Run the offline touch regression and the kitchen tap/swipe check on real old
// Chromium engines, standing in for phones whose WebView is older than the
// developer's browser (WebView 105 is the minimum boot.js accepts; Huawei
// WebView 114 was the 1.5.9/1.5.10 failure). Desktop flags cannot reproduce
// every old-engine behaviour, so these are actual old builds.
//   node tools/qa-old-webviews.mjs
//   env: CHICK_LEGACY_CHROMIUM_ROOT  folder holding <name>/chrome-win/chrome.exe builds
//        CHICK_LEGACY_PLAYWRIGHT     playwright-core old enough to drive them (1.34.x)
//        CHICK_OLD_WEBVIEW_SWEEP=1   also run the per-button sweep and compare engines
// Playwright's CDN keeps the builds: chromium/1019 = 105, 1033 = 108, 1064 = 114.
import {spawn} from 'node:child_process';
import {existsSync,readdirSync} from 'node:fs';
import {mkdir,writeFile} from 'node:fs/promises';
import {join,resolve} from 'node:path';
import {fileURLToPath} from 'node:url';

const root=fileURLToPath(new URL('../',import.meta.url));
const legacyRoot=resolve(process.env.CHICK_LEGACY_CHROMIUM_ROOT??'D:/gxy_code/_toolchain/legacy-chromium');
const playwright=process.env.CHICK_LEGACY_PLAYWRIGHT??join(legacyRoot,'node_modules/playwright-core');
const out=resolve(root,process.env.CHICK_OLD_WEBVIEW_OUTPUT??'artifacts/qa/old-webviews');
const engines=existsSync(legacyRoot)?readdirSync(legacyRoot,{withFileTypes:true}).filter(d=>d.isDirectory()&&existsSync(join(legacyRoot,d.name,'chrome-win/chrome.exe'))).map(d=>({name:d.name,chrome:join(legacyRoot,d.name,'chrome-win/chrome.exe')})):[];
if(!engines.length||!existsSync(playwright)){
  console.error(`No old Chromium builds under ${legacyRoot} (or no playwright-core at ${playwright}). See the header of this file.`);
  process.exit(2);
}
await mkdir(out,{recursive:true});
const run=(name,script,env,pkg=playwright)=>new Promise(done=>{
  const child=spawn(process.execPath,[script,...(pkg?[pkg]:[])],{cwd:root,stdio:['ignore','pipe','pipe'],windowsHide:true,env:{...process.env,...env}});
  let log='';child.stdout.on('data',b=>log+=b);child.stderr.on('data',b=>log+=b);
  child.once('exit',code=>done({name,code,log}));
});
const results=[];
// The offline payload is shared; package it once before the engines run in parallel.
const pack=await new Promise(done=>{const c=spawn(process.execPath,['android/package-runtime.mjs'],{cwd:root,stdio:'inherit',windowsHide:true});c.once('exit',done);});
if(pack!==0)process.exit(1);
await Promise.all(engines.map(async engine=>{
  const dir=join(out,engine.name);
  const env={CHICK_QA_CHROME:engine.chrome};
  const checks=[
    await run('packaged-mobile','tools/qa-packaged-mobile.mjs',{...env,CHICK_QA_MOBILE_OUTPUT:join(dir,'packaged-mobile')}),
    await run('legacy-zoom','tools/qa-legacy-zoom.mjs',{...env,CHICK_ZOOM_MODES:'legacy',CHICK_ZOOM_OUTPUT:join(dir,'legacy-zoom')}),
  ];
  if(process.env.CHICK_OLD_WEBVIEW_SWEEP==='1')checks.push(await run('button-sweep','tools/qa-button-sweep.mjs',{...env,CHICK_SWEEP_OUTPUT:join(dir,'button-sweep')}));
  for(const c of checks){await writeFile(join(dir,c.name+'.log'),c.log);results.push({engine:engine.name,check:c.name,passed:c.code===0});console.log(`${engine.name} ${c.name}: ${c.code===0?'passed':'FAILED'}`);}
}));
if(process.env.CHICK_OLD_WEBVIEW_SWEEP==='1'){
  const reference=join(out,'current','button-sweep');
  // The reference is the developer's current Chromium, driven by the default Playwright.
  const sweep=await run('button-sweep','tools/qa-button-sweep.mjs',{CHICK_QA_CHROME:'',CHICK_SWEEP_OUTPUT:reference},null);
  const compare=await new Promise(done=>{const c=spawn(process.execPath,['tools/compare-button-sweeps.mjs',join(reference,'report.json'),...engines.map(e=>join(out,e.name,'button-sweep','report.json'))],{cwd:root,stdio:'inherit',windowsHide:true});c.once('exit',done);});
  results.push({engine:'all',check:'button-sweep-compare',passed:sweep.code===0&&compare===0});
}
const report={checkedAt:new Date().toISOString(),engines:engines.map(e=>e.name),results,passed:results.every(r=>r.passed)};
await writeFile(join(out,'report.json'),JSON.stringify(report,null,2)+'\n');
console.log(report.passed?`Old WebView checks passed on ${engines.length} engines.`:'Old WebView checks FAILED.');
process.exitCode=report.passed?0:1;
