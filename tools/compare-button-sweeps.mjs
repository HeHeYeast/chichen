// Compare qa-button-sweep reports from different engines. The first report is the
// reference (normally current Chromium); every button that responds there but is
// dead, covered, erroring or missing on another engine is listed, and the exit
// code is 1 if any exists.
//   node tools/compare-button-sweeps.mjs reference/report.json other/report.json [...]
import {readFile} from 'node:fs/promises';

const [refPath,...others]=process.argv.slice(2);
if(!refPath||!others.length){console.error('usage: compare-button-sweeps.mjs reference.json other.json [...]');process.exit(2);}
const load=async p=>JSON.parse(await readFile(p,'utf8'));
const key=r=>`${r.seed}|${r.page}|${r.n}|${r.label}`;
const ref=await load(refPath),refMap=new Map(ref.results.map(r=>[key(r),r]));
let regressions=0;
console.log(`reference ${ref.browser} ${ref.viewport.join('x')}: ${JSON.stringify(ref.summary)}`);
for(const p of others){
  const other=await load(p),map=new Map(other.results.map(r=>[key(r),r])),problems=[];
  for(const f of other.failures)problems.push(`cannot open ${f.seed}/${f.page}: ${f.error}`);
  for(const [k,r] of refMap){
    const o=map.get(k);
    const worked=r.changed&&!r.covered&&!r.errors?.length&&!r.error;
    if(!o){if(worked)problems.push(`missing ${k}`);continue;}
    if(o.errors?.length&&!r.errors?.length)problems.push(`script error ${k}: ${o.errors[0]}`);
    if(!worked)continue;
    if(o.covered)problems.push(`covered ${k} by ${o.cover}`);
    else if(o.changed===false)problems.push(`no response ${k}`);
    else if(o.error)problems.push(`tap failed ${k}: ${o.error}`);
  }
  regressions+=problems.length;
  console.log(`\n${other.browser} ${other.viewport.join('x')}: ${JSON.stringify(other.summary)} → ${problems.length?problems.length+' differences':'matches reference'}`);
  for(const line of problems)console.log('  '+line);
}
process.exitCode=regressions?1:0;
