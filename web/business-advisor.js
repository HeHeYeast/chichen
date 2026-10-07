// "帮我摆": a stock suggestion for one menu, plus the plain-language forecast shown
// under the stock sheet. Read-only; the player still confirms opening.
// Menu-role species are picked first (completing the menu when the farm allows it),
// then the remaining capacity goes to the most valuable free birds, up to 6 kinds.
import {REGIONAL,resolveSpecies,SPECIES_TRADE} from './content-registry.js';
import {effects} from './progression.js';
import {inventoryView,lockedCount} from './inventory.js';
import {businessCapacity,BUSINESS_WINDOW_MS,BUSINESS_RULES_VERSION,themeUnitsOf} from './business.js';
import {assignMenuRoles,menuSnapshot,menuFit,menuUnlockInfo} from './menu-model.js';
import {orderHolds} from './order-delivery.js';

const TIER_RANK={ordinary:0,suitable:1,complete:2};
const MAX_KINDS=6,PER_WINDOW=6;

function lockedKeys(s){
  const locks=s.expansion?.inventoryPolicy?.collectionLocks;
  return new Set(Array.isArray(locks)?locks:Object.keys(locks??{}).filter(k=>locks[k]));
}
export function birdPrice(s,key){
  const base=resolveSpecies(key)?.cp_1??0,markup=SPECIES_TRADE[key]?.category===s.progress.trade.category?effects(s).markup:0;
  return {base,markup};
}

// Expected CP if everything sells (it does within 24 h: 6 per 2 h, at most 72 birds), under the rules a new opening uses.
export function estimateIncome(s,menuId,stock,roles,fit){
  const inRole=new Set(roles.filter(r=>r.roleId!=='ordinary').flatMap(r=>r.keys));
  let cp=0;
  for(const [key,n] of Object.entries(stock)){const {base,markup}=birdPrice(s,key);cp+=n*(base+base*markup/100+themeUnitsOf(BUSINESS_RULES_VERSION,base,fit,inRole.has(key))/100);}
  return Math.floor(cp);
}
// What the full menu adds over selling the same birds without it (the 「凑齐 +25%」 tag).
export function menuBonusIncome(s,stock,roles){
  const inRole=new Set(roles.filter(r=>r.roleId!=='ordinary').flatMap(r=>r.keys));
  let cp=0;for(const [key,n] of Object.entries(stock))if(inRole.has(key))cp+=n*themeUnitsOf(BUSINESS_RULES_VERSION,birdPrice(s,key).base,{complete:true},true)/100;
  return Math.floor(cp);
}
export function businessForecast(s,menuId,stock,roles,fit){
  const total=Object.values(stock).reduce((a,b)=>a+b,0),windows=Math.min(12,Math.ceil(total/PER_WINDOW));
  return {total,hours:windows*BUSINESS_WINDOW_MS/3600000,income:estimateIncome(s,menuId,stock,roles,fit),visitors:Math.floor(Math.min(total,72)/12)};
}

function choose(list,max){const out=[[]];for(const item of list)for(const set of [...out])if(set.length<max)out.push([...set,item]);return out;}

// Birds the order board is counting on stay at home (order-delivery.js orderHolds), cached per save snapshot.
const holdsCache=new WeakMap();
export const holdsOf=(s,now)=>{if(!holdsCache.has(s))holdsCache.set(s,orderHolds(s,now));return holdsCache.get(s);};
export function suggestBusinessStock(s,menuId,{keepOne=true,now=Date.now()}={}){
  const menu=REGIONAL.menus.find(m=>m.id===menuId);if(!menu)throw Error('菜单不存在');
  const capacity=businessCapacity(s),locked=lockedKeys(s),held=holdsOf(s,now);
  const pool=Object.keys(s.farm).filter(k=>s.farm[k]>0&&resolveSpecies(k)?.edible&&!locked.has(k)).map(key=>{
    const v=inventoryView(s,key),n=Math.max(0,Math.min(v.free,keepOne?v.home-lockedCount(s,key):v.free)-(held[key]??0)),{base,markup}=birdPrice(s,key);
    return {key,n,value:base*(1+markup/100)};
  }).filter(x=>x.n>0).sort((a,b)=>b.value-a.value||b.n-a.n);
  if(!pool.length)return null;
  // Each menu-role species needs a minimum count to qualify (家常小铺 asks for 6 each).
  const need=menuId==='MN1'?6:3,score=x=>x.value*Math.min(x.n,capacity);
  const options=menu.roles.map(role=>{const allowed=pool.filter(x=>role.allowed.includes(x.key)).sort((a,b)=>score(b)-score(a)).slice(0,4);
    // An empty pick stays allowed: if a required role cannot be filled the plan is ordinary, not impossible.
    return choose(allowed,role.maxSpecies);});
  let best=null;
  const visit=(index,picked)=>{
    if(index<options.length){for(const set of options[index]){if(set.some(x=>picked.some(y=>y.key===x.key))||picked.length+set.length>MAX_KINDS)continue;visit(index+1,[...picked,...set]);}return;}
    const stock={};let room=capacity;
    for(const x of picked){const n=Math.min(x.n,need,room);if(n>0){stock[x.key]=n;room-=n;}}
    // Then fill by value, menu picks and the rest alike, keeping within six kinds.
    for(const x of pool){if(room<=0)break;if(!stock[x.key]&&Object.keys(stock).length>=MAX_KINDS)continue;const add=Math.min(x.n-(stock[x.key]??0),room);if(add>0){stock[x.key]=(stock[x.key]??0)+add;room-=add;}}
    if(!Object.keys(stock).length)return;
    const roles=assignMenuRoles(menuId,stock),fit=menuFit(menuId,stock,roles,menuSnapshot(s,menuId,stock,roles)),income=estimateIncome(s,menuId,stock,roles,fit);
    const rank=[TIER_RANK[fit.tier],income];
    if(!best||rank[0]>best.rank[0]||rank[0]===best.rank[0]&&rank[1]>best.rank[1])best={stock,roles,fit,income,rank};
  };
  visit(0,[]);
  return best&&{stock:best.stock,tier:best.fit.tier,income:best.income};
}

// Menu chooser data: each menu's suggested plan plus what a complete menu still lacks.
// The shortfall uses the authored example closest to the current stock (fewest extra birds),
// keeping one at home when the player's policy says so. Cached per save snapshot.
const overviewCache=new WeakMap();
export function menuOverview(s,{keepOne=true}={}){
  const cached=overviewCache.get(s)?.[keepOne?1:0];if(cached)return cached;
  const out=REGIONAL.menus.map(menu=>{
    const plan=menuUnlockInfo(s,menu.id).met?suggestBusinessStock(s,menu.id,{keepOne}):null;
    let needs=[];
    if(plan&&plan.tier!=='complete')for(const example of menu.examples??[]){
      const lack=example.map(({key,quantity})=>{const v=inventoryView(s,key),usable=Math.max(0,keepOne?Math.min(v.free,v.home-lockedCount(s,key)):v.free);return {key,n:Math.max(0,quantity-usable)};}).filter(x=>x.n>0);
      if(!needs.length||lack.reduce((a,b)=>a+b.n,0)<needs.reduce((a,b)=>a+b.n,0))needs=lack;
    }
    return {id:menu.id,roleKeys:[...new Set(menu.roles.flatMap(r=>r.allowed))],plan,needs};
  });
  const entry=overviewCache.get(s)??[];entry[keepOne?1:0]=out;overviewCache.set(s,entry);return out;
}
