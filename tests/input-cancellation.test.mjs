import test from 'node:test';
import assert from 'node:assert/strict';
import {readFileSync} from 'node:fs';
import vm from 'node:vm';
import {LAYOUT as L,RECT,contains} from '../web/theme.js';
import {FARM_WORLD,FARM_ACTIONS} from '../web/farm-theme.js';
import {beginToolDrag,moveToolDrag,settleToolDrag} from '../web/tool-strip.js';

// Execute the actual app handlers against a small event/capture adapter. This
// checks interruption ordering and current hit targets, not browser geometry.
const app=readFileSync(new URL('../web/app.js',import.meta.url),'utf8');
const inputSource=app.slice(app.indexOf('function entryInputBlocked('),app.indexOf('let pixelRatio='));
const lifecycleSource=app.split(/\r?\n/).find(line=>line.startsWith("window.addEventListener('pagehide'"));

function harness(page=1){
  function eventTarget(){
    const listeners=new Map();
    return {
      addEventListener(type,callback){if(!listeners.has(type))listeners.set(type,[]);listeners.get(type).push(callback);},
      emit(type,values={}){
        const event={pointerId:1,isPrimary:true,button:0,clientX:240,clientY:300,preventDefault(){},stopImmediatePropagation(){},...values};
        for(const listener of listeners.get(type)??[])listener(event);
      },
    };
  }
  const window=eventTarget(),controls=eventTarget(),document=eventTarget(),captures=new Set();
  let rendered=0,activated=0,released=0,hotspot;
  const context={
    window,controls,document,L,RECT,contains,FARM_WORLD,beginToolDrag,moveToolDrag,settleToolDrag,TOOL_SCROLL_MAX:5,toolScroll:0,
    game:{...eventTarget(),getBoundingClientRect:()=>({left:0,top:0,width:320,height:568})},
    canvas:{getBoundingClientRect:()=>({left:0,top:0,width:320,height:568})},
    dialogs:{children:[]},panels:{querySelector:()=>null},
    state:{batch:null},page,panel:'',farmScroll:0,pointer:null,keyboardPress:null,pressedId:'',entryInputBlockedUntil:0,performance:{now:()=>1000},
    controlBlocked:()=>false,findControl:id=>hotspot?.dataset.controlId===id?hotspot:null,
    collectEgg(){throw Error('A farm or button cancellation must not collect a kitchen egg.');},
    renderControls(){
      rendered++;
      const rect=page===1?{...FARM_ACTIONS[0].rect,x:FARM_ACTIONS[0].rect.x-context.farmScroll}:{x:20,y:400,w:60,h:40};
      hotspot={dataset:{controlId:page===1?'farm:house':'tool:0'},hitRect:rect,disabled:false,
        focus(){document.activeElement=this;},activate(){activated++;}};
    },
    save(){},bgm:{pause(){}},music(){},advanceClock(){},E:{resume(){},checkFarmLoss:()=>0},
    now:()=>0,makeWalkers(){},collectionUI:{refresh(){}},alertBox(){},
  };
  controls.hasPointerCapture=id=>captures.has(id);
  controls.setPointerCapture=id=>captures.add(id);
  controls.releasePointerCapture=id=>{
    assert.ok(captures.delete(id),'capture must be released at most once');released++;
    // Exercise the strict synchronous case to detect recursive cancellation.
    controls.emit('lostpointercapture',{pointerId:id});
  };
  vm.createContext(context);
  vm.runInContext(inputSource+'\n'+lifecycleSource,context);
  context.renderControls();rendered=0;
  function dispatch(target,type,values={}){
    const point={x:values.clientX??240,y:values.clientY??300};
    const hit=contains(hotspot.hitRect,point.x,point.y)?hotspot:null;
    target.emit(type,{target:{closest:()=>hit},...values});
  }
  return {
    context,window,controls,document,
    down:(x=240,y=300)=>{context.game.emit('pointerdown');dispatch(controls,'pointerdown',{clientX:x,clientY:y});},
    move:(x=140,y=300)=>dispatch(window,'pointermove',{clientX:x,clientY:y}),
    up:(x=140,y=300)=>dispatch(window,'pointerup',{clientX:x,clientY:y}),
    interrupt(type){
      if(type==='lostpointercapture'){captures.delete(1);controls.emit(type);}
      else if(type==='visibilitychange'){document.hidden=true;document.emit(type);}
      else window.emit(type);
    },
    get rendered(){return rendered;},get activated(){return activated;},get released(){return released;},
    get rect(){return hotspot.hitRect;},
  };
}

for(const interruption of ['pointercancel','lostpointercapture','blur','pagehide','visibilitychange']){
  test(`farm ${interruption} synchronizes moved hit targets once and never activates a button`,()=>{
    const h=harness();
    h.down();h.move();
    assert.equal(h.context.farmScroll,100);
    assert.equal(h.rect.x,18,'targets are rebuilt at the gesture boundary');
    h.interrupt(interruption);
    assert.equal(h.context.pointer,null);
    assert.equal(h.context.pressedId,'');
    assert.equal(h.rect.x,-82,'building target follows the retained farm position');
    assert.equal(h.rendered,1);
    assert.equal(h.activated,0);
    h.up();
    assert.equal(h.activated,0,'a late release cannot revive the cancelled gesture');
    assert.equal(h.rendered,1,'release and lost capture cannot repeat the refresh');
    h.down(5,100);h.up(5,100);
    assert.equal(h.activated,1,'the visibly shifted building is clickable at its new position');
  });
}

test('normal farm release refreshes once despite synchronous lost capture',()=>{
  const h=harness();h.down();h.move();h.up();
  assert.equal(h.rect.x,-82);assert.equal(h.rendered,1);assert.equal(h.released,1);assert.equal(h.activated,0);
  h.up();assert.equal(h.rendered,1);assert.equal(h.activated,0);
});

test('an unrelated pointer cancellation does not interrupt the active farm gesture',()=>{
  const h=harness();h.down();h.move();
  h.window.emit('pointercancel',{pointerId:2});
  assert.equal(h.context.pointer.id,1);assert.equal(h.rendered,0);
  h.move(120);h.up(120);assert.equal(h.context.farmScroll,120);assert.equal(h.rect.x,-102);assert.equal(h.rendered,1);
});

test('stationary farm cancellation does not rebuild unchanged targets',()=>{
  const h=harness();h.down();h.interrupt('pointercancel');
  assert.equal(h.context.pointer,null);assert.equal(h.rect.x,18);assert.equal(h.rendered,0);assert.equal(h.activated,0);
});

test('kitchen button cancellations never purchase or rebuild farm targets; normal release activates once',()=>{
  for(const interruption of ['pointercancel','lostpointercapture','blur','pagehide','visibilitychange']){
    const h=harness(0);h.down(40,420);h.interrupt(interruption);h.up(40,420);
    assert.equal(h.context.pressedId,'');assert.equal(h.activated,0);assert.equal(h.rendered,0);
  }
  const h=harness(0);h.down(40,420);h.up(40,420);h.up(40,420);
  assert.equal(h.activated,1);assert.equal(h.released,1);assert.equal(h.rendered,0);
});

test('the cover entry guard consumes rapid pointer input until the kitchen is ready for a new gesture',()=>{
  const h=harness(0);h.context.entryInputBlockedUntil=1500;
  h.down(40,420);h.up(40,420);
  assert.equal(h.context.pointer,null);assert.equal(h.activated,0);assert.equal(h.released,0);
  h.context.performance.now=()=>1500;
  h.down(40,420);h.up(40,420);assert.equal(h.activated,1);
});

test('the entry guard also consumes a detail-zero compatibility click, preserving later accessible clicks',()=>{
  const h=harness(0);h.context.entryInputBlockedUntil=1500;
  let prevented=0,stopped=0;
  const click=()=>h.controls.emit('click',{detail:0,preventDefault(){prevented++;},stopImmediatePropagation(){stopped++;}});
  click();assert.equal(prevented,1);assert.equal(stopped,1);
  h.context.performance.now=()=>1500;click();assert.equal(prevented,1);assert.equal(stopped,1);
});

for(const interruption of ['pointercancel','lostpointercapture','blur','pagehide','visibilitychange']){
  test(`tool strip ${interruption} settles and ignores late release`,()=>{
    const h=harness(0);h.down(70,420);h.move(-100,420);
    h.interrupt(interruption);h.up(40,420);
    assert.equal(h.context.toolScroll,2);assert.equal(h.activated,0);
    assert.equal(h.context.pointer,null);assert.equal(h.rendered,1);
  });
}
test('tool drag crossing back to the initial button never cooks; boundaries clamp',()=>{
  const h=harness(0);h.down(70,420);h.move(-500,420);h.move(40,420);h.up(40,420);
  assert.equal(h.activated,0);assert.equal(h.context.toolScroll,0);
  h.down(70,420);h.move(-900,420);h.up(-900,420);assert.equal(h.context.toolScroll,5);
  h.down(70,420);h.move(900,420);h.up(900,420);assert.equal(h.context.toolScroll,0);
});
test('vertical motion cancels tool clicks permanently while minor jitter allows a tap',()=>{
  const h=harness(0);h.down(40,420);h.move(41,470);h.move(40,420);h.up(40,420);
  assert.equal(h.activated,0);assert.equal(h.context.toolScroll,0);
  h.down(40,420);h.move(43,421);h.up(43,421);assert.equal(h.activated,1);
});
test('tool release uses final coordinate even when move was not delivered',()=>{
  const h=harness(0);h.down(70,420);h.up(-100,420);
  assert.equal(h.activated,0);assert.equal(h.context.toolScroll,2);
});
test('tool gaps drag, unrelated and secondary pointers cannot steal capture',()=>{
  const h=harness(0);h.down(120,420);h.move(20,420);
  h.controls.emit('pointerdown',{pointerId:2,isPrimary:false});
  h.window.emit('pointercancel',{pointerId:2});h.up(20,420);
  assert.equal(h.activated,0);assert.equal(h.context.toolScroll,1);
});

test('a control release consumes its synthetic click before it can activate newly opened UI',()=>{
  const h=harness(0);h.down(40,420);h.up(40,420);let stopped=0;
  h.context.game.emit('click',{detail:1,stopImmediatePropagation(){stopped++;}});assert.equal(stopped,1);
  h.context.game.emit('pointerdown');h.context.game.emit('click',{detail:1,stopImmediatePropagation(){stopped++;}});assert.equal(stopped,1,'a new deliberate tap remains usable');
});
