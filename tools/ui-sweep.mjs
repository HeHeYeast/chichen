// Fast pass over the browser UI checks for daily work (the release gate in verify-release.ps1 is unchanged and still
// runs everything, one after the other, and stops at the first failure).
//   node tools/ui-sweep.mjs                run every check, keep going after a failure, print one table
//   node tools/ui-sweep.mjs --jobs 3       run up to 3 checks at once (each check starts its own server and profile)
//   node tools/ui-sweep.mjs --only work-a,resume
//   node tools/ui-sweep.mjs --timeout 240  seconds before one check is declared stuck and killed (default 300)
// A check that fails in a parallel run is re-run once alone, so a busy machine does not report a false failure.
// `packaged-mobile` rewrites the packaged runtime, so it always runs alone at the end. Exit code 1 if anything failed.
import {spawn} from 'node:child_process';
import {mkdir,writeFile,readFile} from 'node:fs/promises';
import {resolve} from 'node:path';
import {fileURLToPath} from 'node:url';
import {selectUIChecks} from './verify-ui.mjs';

const root=fileURLToPath(new URL('../',import.meta.url));
const arg=(name,fallback)=>{const i=process.argv.indexOf('--'+name);return i>=0?process.argv[i+1]:fallback;};
const jobs=Math.max(1,Number(arg('jobs',2))),timeoutMs=Number(arg('timeout',300))*1000;
const only=arg('only',null)?.split(',');
const all=selectUIChecks({release:true});
const names=only??all;
for(const n of names)if(!all.includes(n))throw Error('Unknown check '+n+'; choose '+all.join(', '));
const out=resolve(root,'artifacts/qa/sweep');await mkdir(out,{recursive:true});

function runOne(name){
  return new Promise(done=>{
    const started=Date.now();let log='',killed=false;
    const child=spawn(process.execPath,['tools/verify-ui.mjs'],{cwd:root,windowsHide:true,stdio:['ignore','pipe','pipe'],env:{...process.env,CHICK_QA_ONLY:name}});
    child.stdout.on('data',b=>log+=b);child.stderr.on('data',b=>log+=b);
    const timer=setTimeout(()=>{killed=true;child.kill();},timeoutMs);
    child.once('exit',code=>{clearTimeout(timer);done({name,passed:code===0&&!killed,seconds:Math.round((Date.now()-started)/1000),stuck:killed,log});});
  });
}
async function pool(list,size){
  const results=[];let next=0;
  await Promise.all(Array.from({length:Math.min(size,list.length)},async()=>{
    while(next<list.length){const name=list[next++];const r=await runOne(name);results.push(r);process.stdout.write(`${r.passed?'PASS':r.stuck?'STUCK':'FAIL'} ${name} ${r.seconds}s\n`);}
  }));
  return results;
}

const alone=names.filter(n=>n==='packaged-mobile'),parallel=names.filter(n=>n!=='packaged-mobile');
const t0=Date.now();
let results=await pool(parallel,jobs);
// A failure while others were running may only be load; give it one clean second try.
if(jobs>1){
  const retry=results.filter(r=>!r.passed).map(r=>r.name);
  if(retry.length){console.log('re-running alone: '+retry.join(', '));const again=await pool(retry,1);results=results.filter(r=>r.passed).concat(again);}
}
results=results.concat(await pool(alone,1));
results.sort((a,b)=>names.indexOf(a.name)-names.indexOf(b.name));
const failed=results.filter(r=>!r.passed);
// verify-ui keeps each check's own output (where the assertion text is) in artifacts/qa/release-ui/<name>.log
for(const r of failed){const own=await readFile(resolve(root,'artifacts/qa/release-ui',r.name+'.log'),'utf8').catch(()=>'');r.log=own+String.fromCharCode(10)+r.log;await writeFile(resolve(out,r.name+'.log'),r.log);}
console.log(`\n${results.length-failed.length}/${results.length} passed in ${Math.round((Date.now()-t0)/1000)}s`);
for(const r of failed){
  const reason=(r.log.match(/(AssertionError[^\n]*\n[^\n]*\n[^\n]*|TimeoutError[^\n]*|UI check failed[^\n]*|Error: [^\n]*)/)||[''])[0].slice(0,300);
  console.log(`  ✖ ${r.name}${r.stuck?' (stuck, killed)':''}: ${reason.replace(/\s+/g,' ')}\n    log: artifacts/qa/sweep/${r.name}.log`);
}
process.exit(failed.length?1:0);
