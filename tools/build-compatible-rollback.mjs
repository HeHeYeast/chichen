// Prepare a schema-compatible source overlay. Never installs, deploys, rewrites
// the active policy, modifies player saves, or removes runtime identities.
import {readFile,writeFile,mkdir,readdir} from 'node:fs/promises';
import {resolve,join} from 'node:path';
import {fileURLToPath} from 'node:url';
import {createHash} from 'node:crypto';
const root=fileURLToPath(new URL('../',import.meta.url));
export const PAUSED_POLICY=Object.freeze({region:false,business:false,orders:false,collections:false,regulars:false,projects:false,ui:false,regions:Object.freeze({V:false,R:false,T:false,B:false})});
export function policyOverlay(source,policy=PAUSED_POLICY){
  const keys=['region','business','orders','collections','regulars','projects','ui'];
  if(keys.some(k=>typeof policy[k]!=='boolean')||Object.keys(policy).sort().join()!==[...keys,'regions'].sort().join()||!policy.regions||Object.keys(policy.regions).sort().join()!=='B,R,T,V'||Object.values(policy.regions).some(v=>typeof v!=='boolean'))throw Error('Invalid compatible rollback policy');
  const marker=/^export const ROLLBACK_POLICY=.*$/m;if(!marker.test(source))throw Error('Policy declaration changed; review the overlay builder');
  return source.replace(marker,`export const ROLLBACK_POLICY=Object.freeze({...${JSON.stringify(policy)},regions:Object.freeze(${JSON.stringify(policy.regions)})});`);
}
const hash=raw=>createHash('sha256').update(raw).digest('hex');
export async function buildRollbackOverlay(){
  const output=resolve(root,'artifacts/release/rollback-compatible'),policySource=await readFile(resolve(root,'web/rollback-policy.js'),'utf8'),overlay=policyOverlay(policySource);
  await mkdir(join(output,'web'),{recursive:true});await writeFile(join(output,'web/rollback-policy.js'),overlay);
  const sourceHashes={};
  for(const file of (await readdir(resolve(root,'web'))).filter(p=>/\.(js|json)$/.test(p)).sort())sourceHashes['web/'+file]=hash(await readFile(resolve(root,'web',file)));
  const manifest={createdAt:new Date().toISOString(),kind:'schema-compatible-source-overlay',installed:false,saveSchema:6,policy:PAUSED_POLICY,sourceHashes,overlay:{'web/rollback-policy.js':hash(overlay)},preserves:['runtime241 identities','83 material identities','migration 1→6','batch/trip/business frozen interpreters','order settlement and cancellation','existing collections, regular notes and project contributions'],requires:['Match all source hashes or regenerate on the final candidate.','Copy only into an isolated release checkout; never overwrite live player saves.','Run rollback-policy tests, golden saves, browser settlement and Native validation before packaging.','Use normal Web/Android release packaging and backup procedures; no automatic downgrade.']};
  await writeFile(join(output,'manifest.json'),JSON.stringify(manifest,null,2)+'\n');
  await writeFile(join(output,'README.txt'),'Compatible rollback overlay — prepared, NOT INSTALLED\n\nThis is one build-time policy module and a manifest bound to the current source hashes. It is not a signed APK or a deployable full Web release. Apply the module only to an isolated checkout of this exact candidate, keep every other schema/runtime/interpreter file, then use normal packaging and release checks.\n\nDo not restore an old save, downgrade schema, remove expansion fields, erase IDs, lower material capacity, reset protection, return already-sold stock, or replay rewards. Existing trip/batch/business/order settlement remains available.\n\nSee docs/quality/compatible-rollback.md for feature behavior and recovery.\n');
  return {output,files:['web/rollback-policy.js','manifest.json','README.txt'],sourceFiles:Object.keys(sourceHashes).length,installed:false};
}
if(process.argv[1]&&resolve(process.argv[1])===fileURLToPath(import.meta.url))console.log(JSON.stringify(await buildRollbackOverlay(),null,2));
