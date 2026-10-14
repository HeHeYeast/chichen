import test from 'node:test';
import assert from 'node:assert/strict';
import {existsSync} from 'node:fs';
import {characterIndex,characterImage} from '../web/catalog.js';
import {resolveSprite} from '../web/art/manifest.js';
import {characterPortraitSprite,characterPortraitMarkup} from '../web/character-art.js';
import {journeyCharacter} from '../web/journey-art.js';
import {familyCharacter} from '../web/business-family-art.js';

test('every registered portrait resolves to a real standalone source or a bounded crop',()=>{
  for(const key of characterIndex.keys()){
    const [egg,id]=key.split(':').map(Number),sprite=characterPortraitSprite(egg,id);
    assert.doesNotMatch(sprite.file,/#/);assert.ok(existsSync(new URL('..'+sprite.file,import.meta.url)),key);
    if(sprite.frame){const [x,y,w,h]=sprite.frame;assert.ok(x>=0&&y>=0&&w>0&&h>0&&x+w<=sprite.size[0]&&y+h<=sprite.size[1],key);}
    for(const markup of [characterPortraitMarkup(egg,id),journeyCharacter(key),familyCharacter(egg,id)])
      assert.doesNotMatch(markup,/<(?:img|image)[^>]*(?:src|href)="[^"]+#/,key);
  }
});

test('dim-sum and seasonal atlases keep each identity and clip inside wide or tall slots',()=>{
  let atlases=0;
  for(const key of characterIndex.keys()){
    const [egg,id]=key.split(':').map(Number),path=characterImage(egg,id);
    if(!path.includes('#'))continue;
    atlases++;const expected=resolveSprite(path),sprite=characterPortraitSprite(egg,id);
    assert.deepEqual(sprite.frame,expected.frame,key);assert.equal(sprite.file,expected.file,key);
    for(const markup of [journeyCharacter(key),familyCharacter(egg,id),characterPortraitMarkup(egg,id)]){
      assert.ok(markup.includes(`viewBox="${expected.frame.join(' ')}"`),key);
      assert.match(markup,/overflow="hidden"/);assert.doesNotMatch(markup,/undefined/);
    }
  }
  assert.ok(atlases>=22,'six dim-sum and sixteen seasonal characters covered');
});
