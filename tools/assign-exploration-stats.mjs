// Assigns every partner's 采集 (gather) and 发现 (discover) for 寻访, each 1–20 (2026-10-05 rework: no fixed total of 6,
// extreme specialists allowed). Deterministic: rarity (base price) sets how strong a partner is overall; the old
// 4/2, 3/3, 2/4 lean plus the partner's name/setting/traits decide where the strength goes; a stable hash per key
// spreads the rest so that no two neighbours look alike. Writes:
//   docs/b-group-balance/exploration-species.json  (the 193 original partners, with reasons)
//   docs/b-group-balance/exploration-regional.json (the 48 regional partners: key -> {G,F})
// Run, then `node tools/build-integration-content.mjs`, rewrite the regional rows in docs/content-pack/authoring.mjs
// (tools/apply-regional-stats.py does it), `node docs/content-pack/authoring.mjs`, `node tools/build-runtime-content.mjs`.
import fs from 'node:fs';
import {LEGACY193} from '../web/legacy-content.js';

const root=new URL('../',import.meta.url);
// The priors are the first (total = 6) design, frozen in the *-v1.json files so the script gives the same answer every run.
const OLD=Object.fromEntries(JSON.parse(fs.readFileSync(new URL('docs/b-group-balance/exploration-species-v1.json',root),'utf8')).map(a=>[a.key,a]));
const OLD_REGIONAL=JSON.parse(fs.readFileSync(new URL('docs/b-group-balance/exploration-regional-v1.json',root),'utf8'));

// A small stable hash -> [0,1)
function hash(text,salt){let h=2166136261;for(const c of text+'|'+salt){h^=c.codePointAt(0);h=Math.imul(h,16777619)>>>0;}return h/4294967296;}
const clamp=(v,lo,hi)=>Math.max(lo,Math.min(hi,v));

const strength=price=>clamp(Math.round(5+5.5*Math.log(price+.5)),6,38);
const rarityWord=price=>price<=3?'常见':price<=12?'家常':price<=30?'少见':price<=70?'稀有':'珍稀';

// Setting-based leans: >0 pulls toward gathering, <0 toward discovering. Each hit is named for the reason text.
const NAME_RULES=[
  [/茶|花|菊|梅|樱|牵牛|康乃馨|草|叶|枫|向日葵|文旦|柚/,-.12,'茶花草木，偏辨味'],
  [/凤凰|时空|幽灵|鬼|髑髅|晕眩|骗人|乌鸦|蜘蛛|蚊|苍蝇|蟑螂|灰尘|丧尸|巫女|玉兔|晴天娃娃/,-.16,'异象与灵感，偏发现'],
  [/斗鸡|火鸡|公鸡|北京|烤鸭|炸|炭|红酒炖|白酒炖|醉鸡|龙田/,.1,'体壮手勤，偏采集'],
  [/河童|鸭嘴兽|白鹅|天鹅|绿头|鸳|鸯|番鸭/,.05,'水边出身'],
  [/吐司|面包|饺|包鸡|烧麦|糯米|饭团|月饼|铜锣|曲奇|大福|松饼|布丁/,.06,'点心与面食，偏采集'],
  [/咖啡|奶茶|红茶|绿茶|乌龙|红豆冰|汽水|冰沙|可可/,-.05,'饮品香气，略偏发现'],
];
const OMIKUJI=name=>/签鸡$/.test(name);
const omikujiLean=name=>/大凶|小凶|半凶|末凶|凶/.test(name)?.66:/超大吉|大吉|中吉|小吉|末小吉|半吉|末吉|^吉/.test(name)?.34:.5;
const TRAIT_LEAN={tea:-.08,floral:-.08,leaf:-.06,portable:.06,grain:.05,salt:.06,fruit:0};

function assign({key,price,priorG,priorF,name='',traits=[]}){
  let g0=priorG/(priorG+priorF);const why=[];
  for(const [re,delta,label] of NAME_RULES)if(re.test(name)){g0+=delta;why.push(label);}
  for(const t of traits)if(TRAIT_LEAN[t]){g0+=TRAIT_LEAN[t];}
  if(OMIKUJI(name)){g0=omikujiLean(name);why.push(g0<.45?'吉签，运气好，偏发现':g0>.55?'凶签，肯出力，偏采集':'平签，均衡');}
  if(Math.abs(priorG-priorF)<1&&!OMIKUJI(name)&&!why.length){const h=hash(key,'lean');g0=h<.3?.5:h<.65?.64:.36;}
  const amp=1.5+.95*hash(key,'amp');
  const g1=clamp(.5+(g0-.5)*amp,.1,.9);
  const total=clamp(strength(price)+Math.round((hash(key,'total')-.5)*2),6,38);
  let G=clamp(Math.round(total*g1),1,20),F=clamp(Math.round(total*(1-g1)),1,20);
  // The weaker side never drops to nothing for a strong partner (the strongest still read 20 / 6, not 20 / 3).
  const floor=Math.round(total*.2);if(G<F)G=Math.max(G,Math.min(floor,F-2));else if(F<G)F=Math.max(F,Math.min(floor,G-2));
  return {G,F,why,lean:g1};
}

// ---- the 193 original partners
const out=[];
for(const egg of [0,1])for(const c of LEGACY193.characters[egg]){
  const key=`${egg}:${c.id}`,old=OLD[key],name=c.title_zh_CN;
  const r=assign({key,price:c.cp_1,priorG:old.gather,priorF:old.discover,name});
  const lean=r.G>=r.F*1.6?'偏采集':r.F>=r.G*1.6?'偏发现':'采集与发现均衡';
  const reason=`${rarityWord(c.cp_1)}品种（售价 ${c.cp_1}）；${[...new Set(r.why)].join('；')||lean}${r.why.length?'':''}`;
  out.push({key,name,gather:r.G,discover:r.F,environment:old.environment,reason});
}
fs.writeFileSync(new URL('docs/b-group-balance/exploration-species.json',root),JSON.stringify(out,null,2)+'\n');

// ---- the 48 regional partners
const regional={};
for(const [key,s] of Object.entries(OLD_REGIONAL)){
  const r=assign({key,price:s.price??12,priorG:s.G,priorF:s.F,name:'',traits:s.traits});
  regional[key]={G:r.G,F:r.F};
}
fs.writeFileSync(new URL('docs/b-group-balance/exploration-regional.json',root),JSON.stringify(regional,null,2)+'\n');

// ---- a look at the result
const all=[...out.map(a=>[a.gather,a.discover]),...Object.values(regional).map(v=>[v.G,v.F])];
const sums=all.map(([g,f])=>g+f);
console.log('partners',all.length,'G range',Math.min(...all.map(a=>a[0])),Math.max(...all.map(a=>a[0])),'F range',Math.min(...all.map(a=>a[1])),Math.max(...all.map(a=>a[1])),'sum range',Math.min(...sums),Math.max(...sums));
const bucket={};for(const [g,f] of all){const k=g>=f*1.6?'采集型':f>=g*1.6?'发现型':'均衡';bucket[k]=(bucket[k]??0)+1;}console.log(bucket);
const distinct=new Set(all.map(a=>a.join('/')));console.log('distinct G/F pairs',distinct.size);
