// Daily checks retain the seven legacy suites. A release always adds A–L and
// the book/takeover/compatible-rollback interactions, regardless of CHICK_QA_ONLY.
import {spawn} from 'node:child_process';
import {createRequire} from 'node:module';
import {resolve,join} from 'node:path';
import {fileURLToPath} from 'node:url';
import {mkdir,writeFile} from 'node:fs/promises';
const root=fileURLToPath(new URL('../',import.meta.url));
const require=createRequire(import.meta.url);
export const DEFAULT_UI_CHECKS=Object.freeze(['skill-art','integration','tool-strip','recipe-book','save-ui','resume','kitchen-care']);
// popup-swipe: popups close when dragged up or off to the side (runs with the release set, not the daily seven)
export const SWIPE_UI_CHECKS=Object.freeze(['popup-swipe']);
export const EXPANSION_UI_CHECKS=Object.freeze([...Array.from({length:12},(_,i)=>'work-'+String.fromCharCode(97+i)),'book-navigation','takeover-ui','compatible-rollback']);
export const FINAL_UI_CHECKS=Object.freeze(['kitchen-golden','farm-ui-v1','release-final','ui-fit','packaged-mobile','legacy-zoom','loop-review']);
export function selectUIChecks({release=false,only=null}={}){
  const all=[...DEFAULT_UI_CHECKS,...EXPANSION_UI_CHECKS,...SWIPE_UI_CHECKS,...FINAL_UI_CHECKS];
  if(release)return all;
  if(only){if(!all.includes(only))throw Error(`Unknown CHICK_QA_ONLY: ${only}; choose ${all.join(', ')}`);return [only];}
  return [...DEFAULT_UI_CHECKS];
}
export function uiCheckArguments(name,{packagePath,base,chrome,output}){
  if(!DEFAULT_UI_CHECKS.includes(name)&&!EXPANSION_UI_CHECKS.includes(name)&&!SWIPE_UI_CHECKS.includes(name)&&!FINAL_UI_CHECKS.includes(name))throw Error('Unknown UI check '+name);
  const legacy={
    'skill-art':['tools/qa-skill-art.mjs',packagePath,base,chrome,join(output,'skill-art')],
    integration:['tools/qa-round2.mjs',packagePath,base,chrome,join(output,'integration')],
    'tool-strip':['tools/qa-tool-strip.mjs',packagePath,base,chrome],
    'recipe-book':['tools/qa-recipe-book-v14.mjs',packagePath,base,join(output,'recipe-book')],
    'save-ui':['tools/qa-save-ui.mjs',packagePath,base,join(output,'save-ui')],
    resume:['tools/qa-resume-v146.mjs',packagePath,base,chrome,join(output,'resume')],
    'kitchen-care':['tools/qa-kitchen-care-v145.mjs',packagePath,base,chrome,join(output,'kitchen-care')],
    'popup-swipe':['tools/qa-popup-swipe.mjs',packagePath,base,chrome],
  };
  if(legacy[name])return legacy[name];
  if(EXPANSION_UI_CHECKS.includes(name)||FINAL_UI_CHECKS.includes(name))return [`tools/qa-${name}.mjs`,packagePath];
  throw Error('Unknown UI check '+name);
}
export function uiVerificationReport({required,checks,release=false,error=null}){
  return {checkedAt:new Date().toISOString(),release,passed:!error&&required.length>0&&checks.length===required.length&&checks.every((c,i)=>c.name===required[i]&&c.passed),expectedCount:required.length,completedCount:checks.length,requiredChecks:required,checks,error,nativeDeviceTest:false};
}

export async function verifyUI({argv=process.argv.slice(2),env=process.env}={}){
  const release=argv.includes('--release'),required=selectUIChecks({release,only:env.CHICK_QA_ONLY});
  if(argv.includes('--list')){console.log(JSON.stringify({release,expectedCount:required.length,checks:required},null,2));return;}
  const output=resolve(root,'artifacts/qa/release-ui');await mkdir(output,{recursive:true});
  const checks=[];let server,error=null;
  try{
    const requestedPackage=argv.find(a=>!a.startsWith('--'));
    const candidates=[requestedPackage,env.CHICK_PLAYWRIGHT_PACKAGE,'playwright','playwright-core',env.APPDATA&&join(env.APPDATA,'npm/node_modules/gsd-pi/node_modules/playwright-core')].filter(Boolean);
    let packagePath;for(const candidate of candidates){try{packagePath=require.resolve(candidate);break;}catch{}}
    if(!packagePath)throw Error('Install Playwright or set CHICK_PLAYWRIGHT_PACKAGE to an existing package. UI release checks cannot be skipped.');
    const {chromium}=require(packagePath),chrome=env.CHICK_QA_CHROME??chromium.executablePath();
    if(required.includes('packaged-mobile')){
      const result=await new Promise((done,fail)=>{
        const child=spawn(process.execPath,['android/package-runtime.mjs'],{cwd:root,stdio:['ignore','pipe','pipe'],windowsHide:true,env});
        let log='';child.stdout.on('data',b=>log+=b);child.stderr.on('data',b=>log+=b);
        child.once('error',fail);child.once('exit',code=>done({code,log}));
      });
      await writeFile(join(output,'packaging.log'),result.log);
      if(result.code!==0)throw Error('Offline runtime packaging failed: '+result.log);
    }
    // Legacy suites share this isolated server; A–L own independent servers and
    // profiles. Explicit environment forwarding also covers self-hosted suites.
    server=spawn(process.execPath,['server.mjs','0'],{cwd:root,stdio:['ignore','pipe','pipe'],windowsHide:true,env});
    const base=await new Promise((done,fail)=>{
      const timer=setTimeout(()=>fail(Error('QA server did not start')),10000);
      server.once('error',e=>{clearTimeout(timer);fail(e);});
      server.once('exit',code=>{clearTimeout(timer);fail(Error(`QA server stopped: ${code}`));});
      server.stdout.on('data',chunk=>{const match=String(chunk).match(/http:\/\/127\.0\.0\.1:\d+/);if(match){clearTimeout(timer);done(match[0]);}});
    });
    for(const name of required){
      const args=uiCheckArguments(name,{packagePath,base,chrome,output});
      const result=await new Promise((done,fail)=>{
        const child=spawn(process.execPath,args,{cwd:root,stdio:['ignore','pipe','pipe'],windowsHide:true,env:{...env,CHICK_PLAYWRIGHT_PACKAGE:packagePath,CHICK_QA_CHROME:chrome}});
        let log='';child.stdout.on('data',b=>log+=b);child.stderr.on('data',b=>log+=b);
        child.once('error',fail);child.once('exit',code=>done({code,log}));
      });
      await writeFile(join(output,name+'.log'),result.log);checks.push({name,passed:result.code===0,exitCode:result.code,log:name+'.log'});
      if(result.code!==0)throw Error(`UI check failed: ${name}; see ${join(output,name+'.log')}`);
      console.log(`UI ${name}: passed (${checks.length}/${required.length})`);
    }
  }catch(e){error=e.message;throw e;}
  finally{server?.kill();await writeFile(join(output,'report.json'),JSON.stringify(uiVerificationReport({required,checks,release,error}),null,2)+'\n');}
}
if(process.argv[1]&&resolve(process.argv[1])===fileURLToPath(import.meta.url))verifyUI().catch(e=>{console.error(e.message);process.exitCode=1;});
