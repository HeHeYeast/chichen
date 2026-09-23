import test from 'node:test';
import assert from 'node:assert/strict';
import {readFileSync,existsSync} from 'node:fs';
import {selectUIChecks,uiCheckArguments,uiVerificationReport} from '../tools/verify-ui.mjs';
const root=new URL('../',import.meta.url);

test('daily UI keeps the original seven while release always includes A-L and all final integration suites',()=>{
  assert.deepEqual(selectUIChecks(),['skill-art','integration','tool-strip','recipe-book','save-ui','resume','kitchen-care']);
  const release=selectUIChecks({release:true,only:'save-ui'});
  assert.equal(release.length,22);assert.equal(new Set(release).size,22);
  for(const letter of 'abcdefghijkl')assert.ok(release.includes('work-'+letter));
  assert.ok(release.includes('book-navigation')&&release.includes('takeover-ui')&&release.includes('compatible-rollback'));
  assert.deepEqual(selectUIChecks({release:true,only:'misspelled-name'}),release,'release cannot be weakened by an inherited environment filter');
});

test('named UI runs support both legacy and expansion suites and reject a typo instead of a zero-test success',()=>{
  for(const name of ['save-ui','work-a','work-l','book-navigation','takeover-ui','compatible-rollback'])assert.deepEqual(selectUIChecks({only:name}),[name]);
  assert.throws(()=>selectUIChecks({only:'work-z'}),/Unknown CHICK_QA_ONLY/);
  assert.throws(()=>uiCheckArguments('work-z',{}),/Unknown UI check/);
});

test('every release gate resolves to an existing real script and preserves each argument contract',()=>{
  const options={packagePath:'playwright-package',base:'http://127.0.0.1:4173',chrome:'chrome.exe',output:'qa-output'};
  for(const name of selectUIChecks({release:true})){
    const args=uiCheckArguments(name,options);assert.ok(existsSync(new URL(args[0],root)),name);assert.equal(args[1],options.packagePath);
    if(name.startsWith('work-')||['book-navigation','takeover-ui','compatible-rollback'].includes(name))assert.equal(args.length,2,'self-hosted suites own their isolated server');
    else assert.equal(args[2],options.base);
  }
  assert.equal(uiCheckArguments('skill-art',options)[3],options.chrome);
  assert.ok(uiCheckArguments('recipe-book',options)[3].endsWith('recipe-book'),'recipe-book positional output is not a browser path');
});

test('release reports cannot pass on only the old seven, a duplicated gate, an interrupted suite or a failed child',()=>{
  const required=selectUIChecks({release:true}),checks=required.map(name=>({name,passed:true,exitCode:0}));
  assert.equal(uiVerificationReport({required,checks}).passed,true);
  assert.equal(uiVerificationReport({required,checks:checks.slice(0,7)}).passed,false);
  assert.equal(uiVerificationReport({required,checks:checks.map((c,i)=>i===required.length-1?checks[0]:c)}).passed,false);
  assert.equal(uiVerificationReport({required,checks:checks.map((c,i)=>i===required.length-1?{...c,passed:false,exitCode:1}:c)}).passed,false);
  assert.equal(uiVerificationReport({required,checks,error:'server failed'}).passed,false);
  assert.equal(uiVerificationReport({required:[],checks:[]}).passed,false);
  assert.equal(uiVerificationReport({required,checks}).expectedCount,22);
});

test('release preflight requires read-only runtime/native generation checks, current asset declarations and the full UI release set',()=>{
  const source=readFileSync(new URL('tools/verify-release.ps1',root),'utf8');
  assert.match(source,/'tools\/build-runtime-content\.mjs','--check'/);
  assert.match(source,/'android\/tests\/build-native-content\.mjs','--check'/);
  assert.match(source,/'tests\/runtime-packaging\.test\.mjs'/);
  assert.match(source,/'tools\/verify-ui\.mjs','--release'/);
  const count=[...source.matchAll(/^    Invoke-ReleaseCheck /gm)].length;
  assert.equal(count,9);assert.match(source,new RegExp('\\$verificationRequiredChecks = '+count));
  assert.match(source,/expectedCount=\$verificationRequiredChecks/);
});
