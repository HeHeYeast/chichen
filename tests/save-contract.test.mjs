import test from 'node:test';
import assert from 'node:assert/strict';
import {readFileSync} from 'node:fs';
import {normalizeSave} from '../web/engine.js';
import {makeBackup,parseBackup} from '../web/save-store.js';
import {checkBackup} from '../tools/check-update-backup.mjs';
import {speciesKey,speciesDiscovered} from '../web/species-state.js';
import {characterIndex} from '../web/catalog.js';
import {GAME_DATA} from '../web/content-pack.js';
const fixture=JSON.parse(readFileSync(new URL('./fixtures/save-contract.json',import.meta.url),'utf8'));
for(const entry of fixture.cases)test(`shared save contract: ${entry.name}`,()=>{
  const original=structuredClone(entry.state);
  const legacyProjection=value=>{const copy=structuredClone(value);for(const key of ['meta','clock','expansion','contentRevision'])delete copy[key];copy.version=3;return copy;};
  assert.deepEqual(legacyProjection(normalizeSave(original,fixture.now)),entry.normalized);
  const backup=makeBackup(original,fixture.now);
  assert.deepEqual(legacyProjection(parseBackup(backup,fixture.now)),entry.normalized);
  assert.equal(checkBackup(backup).summary.cp,entry.normalized.cp);
  assert.deepEqual(original,entry.state,'migration and export must be pure');
  const migrated=normalizeSave(entry.normalized,fixture.now);assert.deepEqual(normalizeSave(migrated,fixture.now),migrated,'migration is idempotent');
});
test('all 241 stable identities round trip through data and permanent discovery',()=>{
  assert.equal(characterIndex.size,241);
  GAME_DATA.characters.forEach((rows,egg)=>rows.forEach(c=>{
    const key=speciesKey(egg,c.id);assert.equal(characterIndex.get(key),c);
    assert.ok(speciesDiscovered({total:{[key]:1},farm:{}},egg,c.id));
    assert.ok(speciesDiscovered({total:{},farm:{[key]:1}},egg,c.id));
    assert.equal(speciesDiscovered({total:{},farm:{}},egg,c.id),false);
  }));
});
