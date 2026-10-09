import test from 'node:test';
import assert from 'node:assert/strict';
import {createProfileVault} from '../web/cloud-profiles.js';
import {freshState} from '../web/engine.js';
const memory=()=>{const m=new Map();return {getItem:k=>m.get(k)??null,setItem:(k,v)=>m.set(k,v)};};
test('binding an old guest copies all progress and logout preserves the account branch',()=>{
  const storage=memory(),key='chick-kitchen-v1',state=freshState(1791388800000,42);state.cp=12345;storage.setItem(key,JSON.stringify(state));
  const vault=createProfileVault({storage,key});vault.activate({uid:'account-a',username:'alice'},{copyGuest:true});
  assert.equal(storage.getItem(key),JSON.stringify(state));assert.deepEqual(JSON.parse(storage.getItem(vault.saveKey)),state);
  const accountKey=vault.saveKey;vault.logout();assert.equal(vault.saveKey,key);assert.equal(storage.getItem(accountKey),JSON.stringify(state));
  vault.activate({uid:'account-b',username:'bob'});assert.equal(storage.getItem(vault.saveKey),null);
  vault.activate({uid:'account-a',username:'alice'});assert.equal(vault.saveKey,accountKey);assert.equal(JSON.parse(storage.getItem(accountKey)).cp,12345);
});
test('a failed account copy never publishes a new active pointer or erases the guest',()=>{
  const storage=memory(),key='chick-kitchen-v1',raw=JSON.stringify(freshState(1791388800000,42));storage.setItem(key,raw);
  const failing={getItem:storage.getItem,setItem:(k,v)=>{if(k.includes('.account.'))throw Error('quota');storage.setItem(k,v);}};
  const vault=createProfileVault({storage:failing,key});assert.throws(()=>vault.activate({uid:'alice',username:'alice'},{copyGuest:true}),/quota/);
  assert.equal(vault.active,null);assert.equal(storage.getItem(key),raw);
  storage.setItem(key+'.active-profile','broken');assert.throws(()=>vault.active);assert.equal(storage.getItem(key),raw);
});
