import fs from 'node:fs';
import {LEGACY193 as GAME_DATA} from '../web/legacy-content.js';
const read=p=>fs.readFileSync(new URL('../'+p,import.meta.url),'utf8');
const write=(p,s)=>fs.writeFileSync(new URL('../'+p,import.meta.url),s);
const descriptions=Object.fromEntries([...read('docs/species-descriptions.md').matchAll(/^\| ([01]:\d+) \| (.*?) \| (.*?) \|$/gm)].map(([,key,name,text])=>{
  const [egg,id]=key.split(':').map(Number);
  if(GAME_DATA.characters[egg].find(c=>c.id===id)?.title_zh_CN!==name)throw Error('身份或名称不一致 '+key);
  return [key,text];
}));
const clues=Object.fromEntries([...read('docs/species-discovery-audit.md').matchAll(/^\| ([01]:\d+) \| .*? \| (?:保留|修订) \| (.*?) \| .*? \|$/gm)].map(([,key,text])=>[key,text]));
const abilities=JSON.parse(read('docs/b-group-balance/exploration-species.json'));
if(Object.keys(descriptions).length!==193||Object.keys(clues).length!==193||abilities.length!==193)throw Error('内容覆盖不完整');
for(const a of abilities)if(![a.gather,a.discover].every(v=>Number.isInteger(v)&&v>=1&&v<=20)||!descriptions[a.key])throw Error('能力无效 '+a.key);
const config=JSON.parse(read('docs/b-group-balance/design-config.json'));
delete config.status;
const story=read('docs/worldbuilding-kitchen-story.md');
const chapters=[...story.matchAll(/### (CH-0[123]) (.*?)\r?\n([\s\S]*?)(?=\r?\n### |\r?\n## )/g)].map(([,id,title,body])=>({id,title,paragraphs:body.split(/\r?\n\r?\n/).map(x=>x.trim()).filter(x=>x&&!x.startsWith('**'))}));
if(chapters.length!==3)throw Error('章节不完整');
write('web/integration-data.js','// Generated from approved A/B handoffs by tools/build-integration-content.mjs.\n'+
  Object.entries({DESCRIPTIONS:descriptions,AUTHORED_CLUES:clues,ABILITIES:Object.fromEntries(abilities.map(a=>[a.key,{gather:a.gather,discover:a.discover,environment:a.environment}])),RULES:config,STORY_CHAPTERS:chapters})
  .map(([key,value])=>'export const '+key+' = '+JSON.stringify(value,null,2)+';').join('\n')+'\n');
console.log('Generated: 193 descriptions, 193 clues, 193 abilities, 3 chapters, B rules');
