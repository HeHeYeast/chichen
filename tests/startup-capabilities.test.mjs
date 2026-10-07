import test from 'node:test';
import assert from 'node:assert/strict';
import {readFileSync} from 'node:fs';
import {runInNewContext} from 'node:vm';

const source=readFileSync(new URL('../web/boot.js',import.meta.url),'utf8');
for(const feature of ['clone','canvas','dialog','layout','inert','pointer','capture']){
  test(`unsupported ${feature} shows recovery guidance before touching progress`,()=>{
    const nodes=[],writes=[];
    const node=()=>({style:{},children:[],setAttribute(){},appendChild(child){this.children.push(child);}});
    const context={
      structuredClone:feature==='clone'?undefined:structuredClone,
      CanvasRenderingContext2D:{prototype:{roundRect:feature==='canvas'?undefined:()=>{}}},
      HTMLDialogElement:{prototype:{showModal:feature==='dialog'?undefined:()=>{}}},
      HTMLElement:{prototype:feature==='inert'?{}:{inert:false}},
      Element:{prototype:{setPointerCapture:feature==='capture'?undefined:()=>{}}},
      PointerEvent:feature==='pointer'?undefined:class{},
      CSS:{supports:()=>feature!=='layout'},
      document:{createElement:node,body:{appendChild:panel=>nodes.push(panel)}},
      // Only the separate script-error log may be read before the capability check.
      localStorage:{getItem(key){if(key!=='chick-kitchen-diag-v1')throw Error('must not read saves');return null;},setItem(...args){writes.push(args);}},
      window:{addEventListener(){}},navigator:{userAgent:'Mozilla/5.0 (Linux; Android 12) Chrome/104.0.0.0 Mobile'},
      location:{reload(){}},console,
    };
    runInNewContext(source,context);
    assert.equal(nodes.length,1);assert.equal(nodes[0].children[0].textContent,'请更新系统网页组件');
    assert.match(nodes[0].children[1].textContent,/目前是 104 版，需要 105 版或更新/);
    assert.match(nodes[0].children[2].textContent,/请勿卸载/);assert.deepEqual(writes,[]);
  });
}
