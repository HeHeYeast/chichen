import {createRequire} from 'node:module';
import {resolve} from 'node:path';
import {mkdir,writeFile} from 'node:fs/promises';
const {chromium}=createRequire(import.meta.url)(resolve(process.argv[2]));
const output=resolve('artifacts/qa/mobile-viewport-debug');await mkdir(output,{recursive:true});
const browser=await chromium.launch({headless:true});
const reports=[];
try{
  for(const size of [{width:358,height:713},{width:360,height:717},{width:375,height:747}]){
    const context=await browser.newContext({viewport:size,deviceScaleFactor:3.5,isMobile:true,hasTouch:true,userAgent:'Mozilla/5.0 (Linux; Android 15; Magic8 Build/HONOR; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/138.0.7204.179 Mobile Safari/537.36'});
    const page=await context.newPage();await page.goto('http://127.0.0.1:4173/web/index.html?review=1');await page.getByRole('button',{name:'开始游戏',exact:true}).waitFor();await page.getByRole('button',{name:'开始游戏',exact:true}).click();
    const metrics=await page.evaluate(()=>{
      const box=id=>{const el=document.querySelector(id),r=el.getBoundingClientRect(),s=getComputedStyle(el);return{id,x:r.x,y:r.y,width:r.width,height:r.height,bottom:r.bottom,cssHeight:s.height,overflow:s.overflow,transform:s.transform,buffer:el instanceof HTMLCanvasElement?[el.width,el.height]:null};};
      return{inner:[innerWidth,innerHeight],visual:{width:visualViewport.width,height:visualViewport.height,scale:visualViewport.scale,offsetTop:visualViewport.offsetTop},dpr:devicePixelRatio,boxes:['body','#viewport','#game','#scene','#controls','#panels'].map(box)};
    });
    await page.screenshot({path:resolve(output,`${size.width}-${size.height}.png`),animations:'disabled'});reports.push({size,metrics});await context.close();
  }
  await writeFile(resolve(output,'report.json'),JSON.stringify(reports,null,2));console.log(JSON.stringify(reports,null,2));
}finally{await browser.close();}
