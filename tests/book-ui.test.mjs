import test from 'node:test';
import assert from 'node:assert/strict';
import {freshState} from '../web/engine.js';
import {collectionPageModel,createCollectionUI} from '../web/collection-ui.js';
import {createBookUI,bookOverview,bookPreparation,BOOK_TABS,speciesUses} from '../web/book-ui.js';
import {createCollectionsUI} from '../web/collections-ui.js';
import {recipePaths,recipeId} from '../web/recipe-book.js';
import {resolveSpecies} from '../web/content-registry.js';
const NOW=1800000000000;

function harness(state=freshState(NOW)){
  let markup='',classes='',nodes=new Map();
  const node=(selector,dataset={})=>{
    if(!nodes.has(selector))nodes.set(selector,{dataset,scrollTop:0,classList:{toggle(){}},querySelector:q=>node(selector+' '+q),addEventListener(event,fn){this['on'+event]=fn;}});
    return nodes.get(selector);
  };
  const panels={querySelector(q){if(q.startsWith('[')&&!markup.includes(q.slice(1,-1)))return null;if(q.startsWith('.')&&!markup.includes(q.slice(1))&&!classes.includes(q.slice(1)))return null;return node(q);},querySelectorAll(q){
    const attribute=q.match(/^\[(data-[\w-]+)\]$/)?.[1];if(!attribute)return [];
    const key=attribute.slice(5).replace(/-([a-z])/g,(_,c)=>c.toUpperCase());
    return [...markup.matchAll(new RegExp(attribute+'="([^"]*)"','g'))].map(m=>node(q+':'+m[1],{[key]:m[1]}));
  }};
  return {state,panels,get markup(){return markup;},get classes(){return classes;},getState:()=>state,getNow:()=>NOW,showPanel(title,body,screen){markup='<button class="close"></button>'+body;classes=screen;nodes=new Map();},click:q=>panels.querySelector(q).onclick(),characterPortrait:()=>'<img alt="">'};
}

test('book search accepts stable codes for unknown species but never their hidden names',()=>{
  const s=freshState(NOW),hidden=resolveSpecies('0:128').title_zh_CN;
  assert.deepEqual(collectionPageModel(s,{search:hidden}).entries,[]);
  const code=collectionPageModel(s,{search:'c129'}).entries;assert.equal(code.length,1);assert.equal(code[0].key,'0:128');assert.equal(code[0].name,'未发现');
  s.total['0:128']=1;
  assert.equal(collectionPageModel(s,{search:hidden}).entries[0].key,'0:128');
  assert.equal(collectionPageModel(s,{search:hidden,knownOnly:true}).entries[0].stock,0,'sold-out history remains searchable');
});

test('can-prepare filter requires a known executable method and current ingredients, without revealing the name',()=>{
  const s=freshState(NOW),key='0:0';
  assert.equal(bookPreparation(s,key,NOW).ready,false);
  for(const r of recipePaths(key))s.progress.knowledge.recipes.push(recipeId(r));
  assert.equal(bookPreparation(s,key,NOW).ready,true);
  const result=collectionPageModel(s,{search:'C001',readyOnly:true,now:NOW});
  assert.equal(result.entries.length,1);assert.equal(result.entries[0].name,'未发现');
  assert.deepEqual(collectionPageModel(s,{search:'C129',readyOnly:true,now:NOW}).entries,[]);
});

test('overview shows one pin and at most two actionable directions, and does not fabricate recent history',()=>{
  const s=freshState(NOW);s.total={'0:0':130,'0:3':1,'0:4':1,'0:8':1,'0:18':1};s.toolLevels[1]=0;
  const model=bookOverview(s,{pin:{kind:'recipe',id:'REC-V-C1'},recent:['0:128','0:0']});
  assert.match(model.pin.label,/C129/);assert.ok(model.directions.length<=2);assert.deepEqual(model.recent.map(c=>c.key),['0:0']);
  assert.ok(!JSON.stringify(model).includes(resolveSpecies('0:128').title_zh_CN));
  assert.equal(bookOverview(s).recent.length,0);assert.deepEqual(BOOK_TABS.map(x=>x[1]),['总览','品种','收藏','见闻']);
});

test('the four book sections route through one navigation; material notes retain all four states',()=>{
  const h=harness(),calls=[];const ui=createBookUI({...h,openSpecies:()=>calls.push('species'),openCollections:o=>calls.push(o.tab),openRegion:o=>calls.push(o),openActivities:()=>calls.push('calendar')});
  ui.open({tab:'overview'});assert.equal(h.panels.querySelectorAll('[data-book-tab]').length,4);
  h.panels.querySelectorAll('[data-book-tab]').find(b=>b.dataset.bookTab==='species').onclick();assert.equal(calls.pop(),'species');
  ui.open({tab:'collections'});assert.equal(calls.pop(),'theme');ui.open({tab:'lore'});
  for(const text of ['尚未找到标本','未辨认','供货未开放','尚未用于料理'])assert.ok(h.markup.includes(text));
  assert.ok(!h.markup.includes('荠菜'));
  h.state.expansion.discovery.cards['V-S1']=1;h.state.expansion.discovery.identified[75]=2;h.state.expansion.regions.materialUse={75:true};ui.refresh();
  for(const text of ['荠菜','已找到标本','已辨认','供货已开放','已用于料理'])assert.ok(h.markup.includes(text));
  h.panels.querySelectorAll('[data-book-material]')[0].onclick();assert.deepEqual(calls.pop(),{regionId:'V',tab:'record',materialId:75});
});

test('encyclopedia profiles prepare and manage inventory, while farm profiles retain selling',()=>{
  const h=harness(),calls=[];h.state.total['0:0']=20;h.state.farm['0:0']=3;let page=4;
  const ui=createCollectionUI({...h,getPage:()=>page,act:fn=>fn(),sound(){},makeWalkers(){},changePage:p=>{page=p;},openBookTab:tab=>calls.push(tab),prepareRecipe:key=>calls.push('prepare:'+key),openInventory:key=>calls.push('inventory:'+key),openRecipeBook:()=>{},alertBox:()=>{},confirmBox:()=>{}});
  ui.showCharacter(0,0);assert.equal(h.panels.querySelector('[data-species-sell]'),null);assert.equal(h.panels.querySelector('[data-species-quantity]'),null);
  assert.ok(h.markup.includes('自由可用 3'));assert.ok(h.markup.includes('准备下一锅'));
  const beforeDetails=h.markup.split('<details>')[0];assert.ok((beforeDetails.match(/data-species-use/g)??[]).length<=2);
  h.click('[data-species-prepare]');assert.equal(calls.pop(),'prepare:0:0');h.click('[data-species-inventory]');assert.equal(calls.pop(),'inventory:0:0');
  page=1;ui.showCharacter(0,0);assert.ok(h.panels.querySelector('[data-species-sell]'));assert.ok(h.panels.querySelector('[data-species-quantity]'));
});

test('collection viewpoints use one category select and retain independent old reward entrances',()=>{
  const h=harness(),calls=[];const ui=createCollectionsUI({...h,commitProgress:fn=>fn(h.state),openBookTab:tab=>calls.push(tab),openJournal:tab=>calls.push(tab),openShrine:()=>calls.push('shrine')});
  ui.open();assert.equal(h.panels.querySelectorAll('[data-book-tab]').length,4);assert.equal(h.panels.querySelectorAll('[data-books-tab]').length,0);assert.ok(h.panels.querySelector('[data-books-category]'));
  h.click('[data-books-seasonal]');assert.equal(calls.pop(),'collections');h.click('[data-books-shrine]');assert.equal(calls.pop(),'shrine');
  h.panels.querySelector('[data-books-category]').onchange({target:{value:'region'}});assert.ok(h.markup.includes('data-books-open="COL-V"'));assert.ok(!h.markup.includes('data-books-seasonal'));
});

test('uncollected species expose no usage associations and known species use only authored allowed sets',()=>{
  const s=freshState(NOW);assert.deepEqual(speciesUses(s,'0:128'),[]);s.total['0:0']=1;
  assert.ok(speciesUses(s,'0:0').some(x=>x.kind==='menu'&&x.id==='MN1'));
});

test('profile inventory link filters precisely to a sold-out species and can return to the whole ledger',()=>{
  const h=harness();h.state.total={'0:0':3,'0:3':2};h.state.farm={'0:0':3,'0:3':0};
  const ui=createCollectionUI({...h,getPage:()=>1,act:fn=>fn(),sound(){},makeWalkers(){},changePage(){},alertBox(){},confirmBox(){}});
  ui.openInventory('0:3');assert.ok(h.markup.includes('data-harvest-row="0:3"'));assert.ok(!h.markup.includes('data-harvest-row="0:0"'));
  h.click('[data-harvest-unfilter]');assert.ok(h.markup.includes('data-harvest-row="0:0"'));assert.ok(h.markup.includes('data-harvest-row="0:3"'));
});
