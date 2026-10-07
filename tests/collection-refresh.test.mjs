import test from 'node:test';
import assert from 'node:assert/strict';
import {freshState} from '../web/engine.js';
import {createCollectionUI, speciesView} from '../web/collection-ui.js';

// This adapter exercises UI state and rendered values, not browser layout.
// Replacing the panel discards its nodes just as showPanel does in the app.
function createHarness(stock=10) {
  let state=freshState(1800000000000),page=1,markup='',screenClasses='';
  let nodes=new Map();
  state.farm['0:0']=stock;
  state.total['0:0']=20;

  function node(selector) {
    if(!nodes.has(selector))nodes.set(selector,{
      textContent:'',scrollTop:0,disabled:false,dataset:{},
      classList:{toggle(){}},
      querySelector:child=>node(selector+' '+child),
      addEventListener(event,fn){this['on'+event]=fn;},
    });
    return nodes.get(selector);
  }
  const panels={
    querySelector(selector) {
      if(selector.startsWith('.')&&!markup.includes(selector.slice(1))&&!screenClasses.split(' ').includes(selector.slice(1)))return null;
      if(selector.startsWith('[')&&!markup.includes(selector.slice(1,-1)))return null;
      return node(selector);
    },
    querySelectorAll(selector) {
      if(selector==='[data-harvest-max]')return [...markup.matchAll(/data-harvest-max="([^"]+)"/g)].map(match=>
        Object.assign(node('max:'+match[1]),{dataset:{harvestMax:match[1]}}));
      return [];
    },
  };
  const ui=createCollectionUI({
    getState:()=>state,getPage:()=>page,panels,
    showPanel(_title,body,classes){
      markup='<button class="close"></button>'+body;
      screenClasses=classes;
      nodes=new Map();
    },
    confirmBox(){throw Error('Refreshing a panel must not open a purchase or sale confirmation.');},
    alertBox(){throw Error('A discovered species must remain available after its stock reaches zero.');},
    act:action=>action(),sound(){},characterPortrait:()=>'<img alt="">',makeWalkers(){},
    changePage:value=>{page=value;},
  });
  return {
    ui,panels,
    get state(){return state;},set state(value){state=value;},
    get page(){return page;},set page(value){page=value;},
    get markup(){return markup;},get screenClasses(){return screenClasses;},
  };
}

test('refresh after an inventory loss clamps the harvest selection and income while retaining scroll',()=>{
  const h=createHarness(10),initialCP=h.state.cp;h.state.expansion.inventoryPolicy.keepOne=false;
  h.ui.openAlbum();
  h.panels.querySelectorAll('[data-harvest-max]')[0].onclick();
  assert.equal(h.panels.querySelector('[data-harvest-total]').textContent,'10');
  assert.equal(h.panels.querySelector('[data-harvest-income]').textContent,'30');
  h.panels.querySelector('.harvest-list').scrollTop=75;

  // The visibility handler has already applied farm loss before calling refresh.
  h.state.farm['0:0']=3;
  h.ui.refresh();

  assert.equal(h.screenClasses,'screen-panel harvest-screen');
  assert.equal(h.panels.querySelector('[data-harvest-total]').textContent,'3');
  assert.equal(h.panels.querySelector('[data-harvest-income]').textContent,'9');
  assert.equal(h.panels.querySelector('.harvest-list').scrollTop,75);
  assert.equal(h.state.cp,initialCP,'refresh must not commit a sale');
  assert.equal(h.state.farm['0:0'],3,'refresh must not alter inventory');
});

test('a detail refresh reads a replaced state object and keeps the active view and scroll',()=>{
  const h=createHarness(10);h.state.expansion.inventoryPolicy.keepOne=false;
  h.ui.showCharacter(0,0);
  h.panels.querySelector('[data-species-max]').onclick();
  h.panels.querySelector('.species-sheet').scrollTop=61;
  const oldState=h.state;
  h.state=structuredClone(oldState);
  h.state.farm['0:0']=2;

  h.ui.refresh();

  assert.equal(h.screenClasses,'screen-panel species-screen');
  assert.equal(h.panels.querySelector('[data-species-quantity]').textContent,'2');
  assert.equal(h.panels.querySelector('[data-species-value]').textContent,'6');
  assert.equal(h.panels.querySelector('.species-sheet').scrollTop,61);
  assert.equal(h.panels.querySelector('[data-species-sell]').disabled,false);
  assert.equal(oldState.farm['0:0'],10,'callbacks must use getState rather than the old object');
});

test('zero-stock details disable selling while retaining the discovered species and lifetime count',()=>{
  const h=createHarness(3);
  h.ui.showCharacter(0,0);
  h.panels.querySelector('[data-species-max]').onclick();
  h.state.farm['0:0']=0;

  h.ui.refresh();

  assert.equal(h.screenClasses,'screen-panel species-screen');
  assert.equal(h.panels.querySelector('[data-species-quantity]').textContent,'0');
  assert.equal(h.panels.querySelector('[data-species-value]').textContent,'0');
  assert.equal(h.panels.querySelector('[data-species-sell]').disabled,true);
  assert.equal(h.panels.querySelector('[data-species-plus]').disabled,true);
  assert.equal(h.panels.querySelector('[data-species-max]').disabled,true);
  assert.match(h.markup,/已经收入图鉴/);
  assert.equal(speciesView(h.state,0,0).known,true);
  assert.equal(speciesView(h.state,0,0).total,20);
});

test('refresh retains the collection page after inventory changes',()=>{
  const h=createHarness(3);
  h.page=4;
  h.ui.openAlbum();
  h.panels.querySelector('[data-collection-next]').onclick();
  assert.match(h.markup,/图鉴，第 2 页/);

  h.state.farm['0:0']=0;
  h.ui.refresh();

  assert.equal(h.screenClasses,'screen-panel collection-screen');
  assert.match(h.markup,/图鉴，第 2 页/);
});

test('bulk shortcuts include the other egg tab and only choose quantities until confirmation',()=>{
  const h=createHarness(4);h.state.farm['1:0']=3;h.state.total['1:0']=3;h.state.farm['0:4']=1;h.state.total['0:4']=1;
  const before=structuredClone(h.state);h.ui.openAlbum();
  h.panels.querySelector('[data-harvest-all]').onclick();assert.equal(h.panels.querySelector('[data-harvest-total]').textContent,'5');
  assert.equal(h.panels.querySelector('[data-harvest-keep-one]'),null,'no sell-down-to-one button any more');
  assert.deepEqual(h.state,before);
  h.state.farm['0:0']=3;h.ui.refresh();assert.equal(h.panels.querySelector('[data-harvest-total]').textContent,'4');
  h.panels.querySelector('[data-harvest-clear]').onclick();assert.equal(h.panels.querySelector('[data-harvest-sell]').disabled,true);
});
