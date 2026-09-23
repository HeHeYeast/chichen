// Work K: the fixed five places and the last compiled production rule.
import test from 'node:test';
import assert from 'node:assert/strict';
import {readFileSync} from 'node:fs';
import {NAV,PAGES,navEntries,navRect} from '../web/theme.js';
import {RUNTIME_REQUIREMENTS} from '../web/runtime-requirements.generated.js';

const coverage=JSON.parse(readFileSync(new URL('../web/runtime-condition-coverage.generated.json',import.meta.url),'utf8'));

test('the bottom bar is exactly 厨房/农场/生意/寻访/图鉴; the shop is a kitchen page, not a tab',()=>{
  assert.deepEqual(navEntries().map(n=>n.title),['厨房','农场','生意','寻访','图鉴']);
  assert.deepEqual(NAV.map(n=>n.id),[PAGES.kitchen,PAGES.farm,PAGES.trade,PAGES.explore,PAGES.book]);
  assert.ok(!NAV.some(n=>n.id===PAGES.shop||n.title==='商店'));
  assert.ok(NAV.every(n=>n.title.length===2),'two-character labels, no sideways scrolling');
  for(let i=0;i<5;i++){const r=navRect(i);assert.ok(r.w>=62&&r.x>=0&&r.x+r.w<=320);}
});

test('every authored condition is compiled; the UI rule binds to the five-place contract',()=>{
  assert.equal(coverage.total,438);assert.equal(coverage.unavailable,0);
  assert.equal(RUNTIME_REQUIREMENTS['productionRules:ui'].kind,'compiled');
  assert.deepEqual(RUNTIME_REQUIREMENTS['productionRules:ui'].runtime,{module:'web/theme.js',export:'navEntries',args:[]});
});

test('the generated font subset covers the current UI text',()=>{
  const report=JSON.parse(readFileSync(new URL('../web/fonts/coverage.json',import.meta.url),'utf8'));
  assert.deepEqual(report.missing_codepoints,[]);assert.equal(report.covered_codepoints,report.requested_codepoints);
  // `python tools/build-font.py --check` rescans the text; this pins the committed report.
});
