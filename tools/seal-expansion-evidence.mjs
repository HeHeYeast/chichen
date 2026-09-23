// Seal this milestone's evidence only after the actual gates and real clock finish.
// Does not run gameplay, change saves, sign, install, or treat pending art as passed.
import {readFile,writeFile,stat,mkdir} from 'node:fs/promises';
import {createReadStream} from 'node:fs';
import {createHash} from 'node:crypto';
import {resolve,relative,sep,isAbsolute} from 'node:path';
import {fileURLToPath} from 'node:url';
import assert from 'node:assert/strict';
import {selectUIChecks} from './verify-ui.mjs';
import {RUNTIME_ASSETS} from '../web/runtime-assets.generated.js';
const root=fileURLToPath(new URL('../',import.meta.url));
const inside=path=>{const full=resolve(root,path),rel=relative(root,full);assert.ok(rel&&!rel.startsWith('..'+sep)&&rel!=='..'&&!isAbsolute(rel),'path outside workspace');return full;};
const json=async path=>JSON.parse((await readFile(inside(path),'utf8')).replace(/^\uFEFF/,''));
async function sha(path){const h=createHash('sha256');for await(const chunk of createReadStream(inside(path)))h.update(chunk);return h.digest('hex');}
const fresh=report=>assert.ok(Date.parse(report.checkedAt)>Date.parse('2026-09-23T13:35:00Z'),'historical report cannot seal this takeover');
const preflight=await json('android/build/release-verification.json');fresh(preflight);
assert.equal(preflight.passed,true);assert.equal(preflight.expectedCount,9);assert.equal(preflight.completedCount,9);assert.ok(preflight.checks.every(c=>c.passed&&c.exitCode===0));
const ui=await json('artifacts/qa/release-ui/report.json');fresh(ui);
assert.equal(ui.release,true);assert.equal(ui.passed,true);assert.deepEqual(ui.requiredChecks,selectUIChecks({release:true}));assert.equal(ui.completedCount,22);assert.ok(ui.checks.every(c=>c.passed));
const real=await json('artifacts/qa/real-window/report.json');fresh(real);
assert.equal(real.passed,true);assert.equal(real.acceleratedClock,false);assert.equal(real.realDeviceTest,false);
assert.ok(Date.parse(real.timeline.at(-1).at)-Date.parse(real.timeline[0].at)>=2*3600000,'two real hours must elapse');
assert.ok(real.timeline.some(x=>x.text.startsWith('1h return:')));assert.ok(real.timeline.some(x=>x.text.startsWith('2h03m return:')));
const native=await json('artifacts/takeover/native-final-evidence.json');fresh(native);
assert.equal(native.apk.signatureV2,true);assert.equal(native.runtime.allPackagedBytesMatchCurrentSources,true);assert.equal(await sha(native.apk.path),native.apk.sha256);
const packaged=await json('artifacts/takeover/native-runtime-manifest-final.json');assert.equal(packaged.files,662);assert.equal(packaged.contentHash,native.runtime.contentHash);
for(const item of packaged.assets){assert.equal(await sha(item.path),item.sha256,'runtime drift: '+item.path);assert.equal((await stat(inside(item.path))).size,item.size);}
for(const [path,hash]of Object.entries(native.nativeSources))assert.equal(await sha(path),hash,'Native drift: '+path);
const runtime=await json('web/runtime-content.manifest.json');
for(const [path,hash]of Object.entries(runtime.sources))assert.equal(await sha(path),hash,'author/build source drift: '+path);
const rollback=await json('artifacts/release/rollback-compatible/manifest.json');
for(const [path,hash]of Object.entries(rollback.sourceHashes))assert.equal(await sha(path),hash,'rollback drift: '+path);
const reach=await json('artifacts/sim/reachability-summary.json');fresh(reach);assert.equal(reach.passed,true);assert.equal(reach.seedCount,8);
for(const run of reach.runs){const path=`artifacts/sim/reachability-${run.seed}.json`;assert.equal(await sha(path),run.sha256);const result=await json(path);assert.equal(result.passed,true);assert.equal(result.finalValid,true);}
for(const path of ['artifacts/sim/economy-14d.json','artifacts/sim/economy-pairs.json','artifacts/sim/economy-investments.json','artifacts/sim/kitchen-lv4/result.json']){const report=await json(path);fresh(report);assert.equal(report.passed,true);}
const snapshot=process.argv[2];assert.ok(snapshot,'provide the final verified source snapshot');assert.ok(snapshot.endsWith('.zip'));
assert.ok((await stat(inside(snapshot))).mtimeMs>=Date.parse(real.checkedAt),'source snapshot must follow final real-clock result');
const log=await readFile(inside('artifacts/takeover/node-final.txt'),'utf8'),count=Number(log.match(/(?:ℹ|#) tests (\d+)/)?.[1]);
assert.ok(count>=582);assert.equal(Number(log.match(/(?:ℹ|#) pass (\d+)/)?.[1]),count);for(const field of ['fail','skipped','cancelled','todo'])assert.match(log,new RegExp('(?:ℹ|#) '+field+' 0\\b'));
const paths=['android/build/release-verification.json','android/build/release-verification.log','artifacts/qa/release-ui/report.json','artifacts/qa/real-window/report.json','artifacts/qa/book-navigation/report.json','artifacts/qa/takeover-ui/report.json','artifacts/qa/compatible-rollback/report.json','artifacts/takeover/node-final.txt','artifacts/takeover/native-final-evidence.json','artifacts/takeover/native-runtime-manifest-final.json','artifacts/takeover/native-repair.md','artifacts/takeover/economy-audit.md','artifacts/takeover/economy-investment-appendix.md','artifacts/sim/reachability-summary.json','artifacts/sim/economy-14d.json','artifacts/sim/economy-pairs.json','artifacts/sim/economy-investments.json','artifacts/release/rollback-compatible/manifest.json','artifacts/release/rollback-compatible/web/rollback-policy.js','docs/implementation-status.md','docs/release-acceptance-report.md','docs/verification-coverage.md','docs/device-release-checklist.md','docs/plan.md','docs/compatible-rollback.md'];
for(const name of selectUIChecks({release:true}))paths.push(`artifacts/qa/release-ui/${name}.log`);
paths.push('artifacts/takeover/formal-content-unchanged.json','artifacts/takeover/kitchen-lv4-investment.md','artifacts/takeover/kitchen-lv4-verification.log','artifacts/sim/kitchen-lv4/result.json','tools/verify-kitchen-lv4-investment.mjs');
const evidence={};for(const path of paths)evidence[path]={sha256:await sha(path),bytes:(await stat(inside(path))).size};
const report={checkedAt:new Date().toISOString(),engineeringAutomaticGatesPassed:true,formalReleaseAccepted:false,works:Object.fromEntries('ABCDEFGHIJK'.split('').map(k=>[k,'DONE (engineering)']).concat([['L','PARTIAL (automatic gates complete; external release acceptance pending)']])),schemaVersion:6,contentRevision:runtime.contentRevision,rulesVersion:runtime.rulesVersion,authorSourceHash:runtime.sourceHash,nodeTests:count,browserSuites:22,releaseGates:9,nativeChecks:native.checks,apk:native.apk,runtime:native.runtime,realClock:real,sourceSnapshot:{path:relative(root,inside(snapshot)).split(sep).join('/'),sha256:await sha(snapshot)},finalArtPendingSets:Object.values(RUNTIME_ASSETS).filter(a=>a.productionStatus==='final-art-pending').length,externalAcceptance:['final art','Android physical-device lifecycle/process-death/file-picker restore','3–5 new-player comprehension','formal signing and release process'],evidence};
await mkdir(inside('artifacts/takeover'),{recursive:true});await writeFile(inside('artifacts/takeover/final-provenance.json'),JSON.stringify(report,null,2)+'\n');
console.log(JSON.stringify({engineeringAutomaticGatesPassed:true,formalReleaseAccepted:false,nodeTests:count,browserSuites:22,releaseGates:9,sourceSnapshot:report.sourceSnapshot,apk:report.apk.path,finalArtPendingSets:report.finalArtPendingSets},null,2));
