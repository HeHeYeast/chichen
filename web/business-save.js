import {REGIONAL,resolveSpecies,SPECIES_TRADE} from './content-registry.js';
import {BUSINESS_WINDOW_MS,BUSINESS_DURATION_MS,BUSINESS_RULES_VERSION,businessCapacity,businessBonusQuote} from './business.js';
import {menuFit,menuSalesWitness} from './menu-model.js';
import {inventoryView} from './inventory.js';
import {RULES} from './integration-data.js';
import {validateOrdersState} from './orders.js';

// Strict schema validation, never a repair/migration. No mutation, time or RNG.
export function validateBusinessState(s,fail){
  const error=label=>{fail(`营业 ${label}`);throw Error(`营业 ${label}`);};
  const obj=(v,label,keys)=>{if(!v||typeof v!=='object'||Array.isArray(v)||![Object.prototype,null].includes(Object.getPrototypeOf(v)))error(label);if(keys){for(const key of keys)if(!Object.hasOwn(v,key))error(`${label}.${key}`);for(const key of Object.keys(v))if(!keys.includes(key))error(`${label}.${key}`);}return v;};
  const int=(v,label,min=0,max=Number.MAX_SAFE_INTEGER)=>{if(!Number.isSafeInteger(v)||v<min||v>max)error(label);return v;};
  const bool=(v,label)=>{if(typeof v!=='boolean')error(label);};
  const list=(v,label,max)=>{if(!Array.isArray(v)||v.length>max)error(label);return v;};
  const equal=(a,b,label)=>{if(JSON.stringify(a)!==JSON.stringify(b))error(label);};
  const mapEqual=(a,b,label)=>{const keys=new Set([...Object.keys(a),...Object.keys(b)]);for(const key of keys)if((a[key]??0)!==(b[key]??0))error(label);};
  const sum=map=>Object.values(map).reduce((a,b)=>a+b,0);
  const stockMap=(map,label,{keys,max=72,positive=false}={})=>{obj(map,label);if(Object.keys(map).length>6)error(label);for(const [key,n]of Object.entries(map)){if(!resolveSpecies(key)?.edible||keys&&!keys.includes(key))error(label);int(n,label,positive?1:0,max);}return map;};
  const countMap=(map,label,allowed,max=Number.MAX_SAFE_INTEGER)=>{obj(map,label);for(const [key,n]of Object.entries(map)){if(!allowed(key))error(`${label}.${key}`);int(n,`${label}.${key}`,0,max);}return map;};
  const b=obj(s.expansion.business,'状态',['sequence','active','lastReport','visitorProgress','visitorSequence','themeRemainder']);
  int(b.sequence,'序号');int(b.visitorProgress,'来客余量',0,11);int(b.visitorSequence,'来客序号');int(b.themeRemainder,'主题余数',0,99);
  validateOrdersState(s,error);
  for(const order of s.expansion.orders.active)for(const key of Object.keys(order.reserved)){try{inventoryView(s,key);}catch{error('T/R/S/Q守恒');}}
  const policy=obj(s.expansion.inventoryPolicy,'库存偏好',['keepOne','collectionLocks','optionalOrderReservations']);bool(policy.keepOne,'每种留一');list(policy.collectionLocks,'收藏锁',241);if(new Set(policy.collectionLocks).size!==policy.collectionLocks.length||policy.collectionLocks.some(k=>!resolveSpecies(k)))error('收藏锁身份');if(Object.keys(obj(policy.optionalOrderReservations,'自选预留')).length)error('采购预留尚未开放');

  function windows(rows,initial,{menuId,snapshot,roles,prices,roleCursor}={}){
    list(rows,'窗口',12);const totals={soldByKey:{},roleSales:{},fullSoldByKey:{},fullRoleSales:{},baseCP:0,markupCP:0,themeCP:0},remaining={...initial};let cursor=0;
    for(const [i,w]of rows.entries()){
      obj(w,'窗口',['index','at','tier','entries','baseCP','markupCP','themeCP']);int(w.index,'窗口序号',1,12);if(w.index!==i+1)error('窗口顺序');int(w.at,'窗口时间');
      if(!['ordinary','suitable','complete'].includes(w.tier))error('窗口档位');list(w.entries,'成交记录',6);if(!w.entries.length)error('空成交窗口');
      if(snapshot&&menuFit(menuId,remaining,roles,snapshot).tier!==w.tier)error('窗口档位与当时备货不符');
      const money={baseCP:0,markupCP:0,themeCP:0};
      for(const entry of w.entries){
        obj(entry,'成交',['key','roleId','quantity','baseCP','markupCP','themeCP']);if(!Object.hasOwn(initial,entry.key)||remaining[entry.key]<1)error('成交来源');
        if(entry.quantity!==1)error('逐只成交数量');
        if(roles){let expected=null;for(let j=0;j<roles.length;j++){const role=roles[cursor];cursor=(cursor+1)%roles.length;const key=role.keys.find(k=>remaining[k]>0);if(key){expected={key,roleId:role.roleId};break;}}
          if(!expected||expected.key!==entry.key||expected.roleId!==entry.roleId)error('角色轮转或替代顺序');
        }else if(entry.roleId!=='ordinary'&&!REGIONAL.menus.find(m=>m.id===menuId)?.roles.some(r=>r.id===entry.roleId&&r.allowed.includes(entry.key)))error('角色身份');
        for(const field of ['baseCP','markupCP','themeCP']){int(entry[field],'成交金额',0,100000);money[field]+=entry[field];}
        if(prices&&(entry.baseCP!==prices[entry.key].baseCP||entry.markupCP>Math.ceil(prices[entry.key].baseCP*prices[entry.key].markupPercent/100)))error('成交冻结价格');
        if(entry.themeCP>2||w.tier==='ordinary'&&entry.themeCP!==0)error('主题单只上限');
        remaining[entry.key]--;totals.soldByKey[entry.key]=(totals.soldByKey[entry.key]??0)+1;totals.roleSales[entry.roleId]=(totals.roleSales[entry.roleId]??0)+1;
        if(w.tier==='complete'){totals.fullSoldByKey[entry.key]=(totals.fullSoldByKey[entry.key]??0)+1;totals.fullRoleSales[entry.roleId]=(totals.fullRoleSales[entry.roleId]??0)+1;}
      }
      for(const field of ['baseCP','markupCP','themeCP']){int(w[field],'窗口合计');if(w[field]!==money[field])error('窗口金额守恒');totals[field]+=w[field];}
    }
    if(roleCursor!==undefined&&cursor!==roleCursor)error('角色游标');return {...totals,remaining};
  }
  function core(row,isActive){
    const m=REGIONAL.menus.find(m=>m.id===row.menuId);if(!m)error('菜单身份');if(row.rulesVersion!==BUSINESS_RULES_VERSION)error('规则版本');
    const match=/^business-([1-9]\d*)$/.exec(row.id);if(!match||!Number.isSafeInteger(Number(match[1]))||Number(match[1])>b.sequence)error('营业身份');
    int(row.startAt,'开始时间');int(row.totalSold,'成交总数',0,72);int(row.visitorEvents,'来客次数',0,6);
    for(const field of ['baseCP','markupCP','themeCP','bonusCP'])int(row[field],field,0,10000000);
    stockMap(row.initialStock,'初备',{positive:true});if(!Object.keys(row.initialStock).length||sum(row.initialStock)>72)error('初备容量');
    stockMap(row.soldByKey,'已售',{keys:Object.keys(row.initialStock)});if(sum(row.soldByKey)!==row.totalSold)error('已售数量');
    bool(row.bonusSettled,'奖励结清');if(isActive===row.bonusSettled)error('奖励结清状态');return {menu:m,sequence:Number(match[1])};
  }
  if(b.active!==null){
    const a=obj(b.active,'当前单',['menuId','roles','stock','initialStock','snapshot','prices','rewards','creditReserve','useRewards','tendency','capacity','id','rulesVersion','startAt','hardEndAt','processedWindow','roleCursor','soldByKey','roleSales','fullSoldByKey','fullRoleSales','baseCP','markupCP','themeCP','bonusCP','totalSold','windowReports','recordedValid','recordedComplete','bonusSettled','pendingCloseAt','visitorCandidates','visitorEvents']);
    const {menu,sequence}=core(a,true);if(sequence!==b.sequence)error('当前营业序号');
    int(a.hardEndAt,'截止时间');if(a.hardEndAt!==a.startAt+BUSINESS_DURATION_MS)error('24小时边界');int(a.processedWindow,'窗口游标',0,12);if(a.windowReports.length!==a.processedWindow)error('窗口记录游标');
    if(![24,48,72].includes(a.capacity)||a.capacity>businessCapacity(s)||sum(a.initialStock)>a.capacity)error('厨房备货容量');
    const keys=Object.keys(a.initialStock);stockMap(a.stock,'未售',{keys});equal(Object.keys(a.stock).sort(),[...keys].sort(),'未售键集合');
    obj(a.prices,'冻结价格');equal(Object.keys(a.prices).sort(),[...keys].sort(),'价格键集合');
    for(const p of Object.values(a.prices)){obj(p,'价格',['baseCP','markupPercent']);int(p.baseCP,'基础价',1,100000);if(![0,12,18].includes(p.markupPercent))error('招牌百分比');}
    list(a.roles,'角色',menu.roles.length+1);const assigned=new Set(),roleIds=new Set();
    for(const r of a.roles){obj(r,'角色',['roleId','keys']);if(roleIds.has(r.roleId))error('重复角色');roleIds.add(r.roleId);const def=menu.roles.find(m=>m.id===r.roleId);if(!def&&r.roleId!=='ordinary')error('角色身份');list(r.keys,'角色出品',def?.maxSpecies??6);for(const key of r.keys){if(!keys.includes(key)||assigned.has(key)||def&&!def.allowed.includes(key))error('角色品种分配');assigned.add(key);}}
    if(menu.roles.some(r=>!roleIds.has(r.id))||assigned.size!==keys.length)error('角色覆盖');int(a.roleCursor,'角色游标',0,a.roles.length-1);
    obj(a.snapshot,'菜单快照',['menuId','roles','identified','species']);if(a.snapshot.menuId!==a.menuId)error('快照菜单');equal(a.snapshot.roles,a.roles,'快照角色');list(a.snapshot.identified,'辨认快照',8);if(new Set(a.snapshot.identified).size!==a.snapshot.identified.length||a.snapshot.identified.some(id=>!/^(7[5-9]|8[0-2])$/.test(id)))error('辨认快照');
    obj(a.snapshot.species,'品种快照');equal(Object.keys(a.snapshot.species).sort(),[...keys].sort(),'快照品种覆盖');
    for(const [key,c]of Object.entries(a.snapshot.species)){obj(c,'品种语义',['egg','region','tags','season','regionalMaterials']);const source=resolveSpecies(key);if(c.egg!==source.egg||c.region!==(source.region??null)||c.season!==(source.season??null))error('品种语义');equal(c.tags,source.tags??[],'标签快照');equal(c.regionalMaterials,source.unlock?.identifiedMaterials??[],'地区材料快照');}
    obj(a.rewards,'奖励快照',['basket','platter','basketBonus','platterBonus','basketEligible','platterEligible']);bool(a.rewards.basket,'整筐快照');bool(a.rewards.platter,'拼盘快照');if(![12,18].includes(a.rewards.basketBonus)||![8,12].includes(a.rewards.platterBonus))error('奖励金额');equal(a.rewards.basketEligible,RULES.trade.eligibleSpecies,'整筐白名单');list(a.rewards.platterEligible,'拼盘白名单',6);if(new Set(a.rewards.platterEligible).size!==a.rewards.platterEligible.length||a.rewards.platterEligible.some(k=>!keys.includes(k)||!resolveSpecies(k)?.edible))error('拼盘名单');
    int(a.creditReserve,'额度预留',0,6);if(a.creditReserve>s.progress.trade.credits||!a.useRewards&&a.creditReserve)error('经营额度守恒');bool(a.useRewards,'奖励选择');if(!['regulars','discovery'].includes(a.tendency))error('经营倾向');
    if(![[12,8],[18,12]].some(pair=>pair[0]===a.rewards.basketBonus&&pair[1]===a.rewards.platterBonus))error('奖励专精快照');
    equal([...a.rewards.platterEligible].sort(),keys.filter(k=>SPECIES_TRADE[k]?.platter).sort(),'拼盘白名单快照');
    if(a.bonusCP!==0)error('未收摊不能预付奖励');bool(a.recordedValid,'有效菜单');bool(a.recordedComplete,'完整菜单');list(a.visitorCandidates,'来客候选',4);if(new Set(a.visitorCandidates).size!==a.visitorCandidates.length||a.visitorCandidates.some(id=>!REGIONAL.regulars.some(r=>r.id===id)))error('来客身份');
    const calculated=windows(a.windowReports,a.initialStock,{menuId:a.menuId,snapshot:a.snapshot,roles:a.roles,prices:a.prices,roleCursor:a.roleCursor});
    for(const [i,w]of a.windowReports.entries())if(w.at!==a.startAt+(i+1)*BUSINESS_WINDOW_MS)error('窗口时间边界');
    for(const field of ['soldByKey','roleSales','fullSoldByKey','fullRoleSales']){countMap(a[field],field,k=>field.includes('Role')||field==='roleSales'?roleIds.has(k):keys.includes(k),72);mapEqual(a[field],calculated[field],`${field}守恒`);}
    for(const field of ['baseCP','markupCP','themeCP'])if(a[field]!==calculated[field])error('营业金额守恒');
    mapEqual(a.stock,calculated.remaining,'T/S成交守恒');
    for(const key of keys){try{inventoryView(s,key);}catch{error('T/R/S/Q守恒');}}
    const witness=menuSalesWitness(a.menuId,a.soldByKey,a.roleSales,a.fullSoldByKey,a.fullRoleSales);if(a.recordedValid!==witness.valid||a.recordedComplete!==witness.complete)error('菜单成交见证');
    if(a.pendingCloseAt!==null){int(a.pendingCloseAt,'待收摊时间',a.startAt,a.hardEndAt);if(sum(a.stock)===0){if(a.pendingCloseAt!==a.startAt+a.processedWindow*BUSINESS_WINDOW_MS)error('售罄时间');}else if(a.processedWindow!==12||a.pendingCloseAt!==a.hardEndAt)error('到期收摊');}
    else if(!sum(a.stock)||a.processedWindow===12)error('缺少待收摊标记');
    if(businessBonusQuote(a.rewards,a.initialStock,6).creditsUsed<a.creditReserve)error('多余经营额度预留');
  }
  if(b.lastReport!==null){
    const r=obj(b.lastReport,'账单',['id','rulesVersion','menuId','startAt','closedAt','reason','totalSold','initialStock','soldByKey','remainingStock','baseCP','markupCP','themeCP','bonusCP','income','creditsUsed','creditsReleased','baskets','platters','validMenu','completeMenu','visitorEvents','windowReports','bonusSettled']);
    const {sequence}=core(r,false);if(b.active&&sequence>=b.sequence)error('账单与当前单顺序');int(r.closedAt,'收摊时间',r.startAt,r.startAt+BUSINESS_DURATION_MS);if(!['manual','deadline','sold-out'].includes(r.reason))error('收摊原因');
    for(const key of ['creditsUsed','creditsReleased','baskets','platters'])int(r[key],key,0,6);if(r.creditsUsed!==r.baskets+r.platters)error('奖励次数守恒');
    if(r.creditsUsed+r.creditsReleased>6||r.baskets*24+r.platters*12>r.totalSold||![r.baskets*12+r.platters*8,r.baskets*18+r.platters*12].includes(r.bonusCP))error('奖励出品或金额');
    int(r.income,'账单收入');if(r.income!==r.baseCP+r.markupCP+r.themeCP+r.bonusCP)error('账单收入守恒');bool(r.validMenu,'账单菜单');bool(r.completeMenu,'账单完整菜单');
    stockMap(r.remainingStock,'账单余货',{keys:Object.keys(r.initialStock)});const calculated=windows(r.windowReports,r.initialStock,{menuId:r.menuId});
    mapEqual(r.soldByKey,calculated.soldByKey,'账单销量');mapEqual(r.remainingStock,calculated.remaining,'账单余货守恒');
    for(const field of ['baseCP','markupCP','themeCP'])if(r[field]!==calculated[field])error('账单金额');
    for(const [i,w]of r.windowReports.entries())if(w.at!==r.startAt+(i+1)*BUSINESS_WINDOW_MS||w.at>r.closedAt)error('账单窗口时间');
    const w=menuSalesWitness(r.menuId,calculated.soldByKey,calculated.roleSales,calculated.fullSoldByKey,calculated.fullRoleSales);if(r.validMenu!==w.valid||r.completeMenu!==w.complete)error('账单菜单见证');
    if(r.reason==='sold-out'&&sum(r.remainingStock)!==0||r.reason==='deadline'&&r.closedAt!==r.startAt+BUSINESS_DURATION_MS)error('收摊边界');
  }
  validateFactsState(s,fail);
  return true;
}

export function validateFactsState(s,fail){
  const error=label=>{fail(`事实 ${label}`);throw Error(`事实 ${label}`);};
  const obj=(v,label,keys)=>{if(!v||typeof v!=='object'||Array.isArray(v))error(label);if(keys&&(keys.some(k=>!Object.hasOwn(v,k))||Object.keys(v).some(k=>!keys.includes(k))))error(label);return v;};
  const int=(n,label,min=0,max=Number.MAX_SAFE_INTEGER)=>{if(!Number.isSafeInteger(n)||n<min||n>max)error(label);};
  const seq=(n,label)=>int(n,label,1,s.meta.factSeq);
  const species=key=>!!resolveSpecies(key),regions=['V','R','T','B'];
  const f=obj(s.expansion.facts,'容器',['version','businessCounts','businessMenuCounts','orderCounts','orderTemplateCounts','orderGroupCounts','menuWitnesses','tripWitnesses','companionFirst','eventWitnesses','predicateWitnesses','materialBatches','payments','projectDeliveries']);if(f.version!==1)error('版本');
  const counts=(map,label,allowed)=>{obj(map,label);for(const [key,n]of Object.entries(map)){if(!allowed(key))error(label);int(n,label);}};
  counts(f.businessCounts,'营业数量',species);counts(f.orderCounts,'采购数量',species);counts(f.orderTemplateCounts,'模板数量',id=>REGIONAL.orders.some(o=>o.id===id));counts(f.materialBatches,'材料批数',id=>/^(0|[1-9]\d*)$/.test(id)&&Number(id)<=82);
  for(const [name,allowed]of [['businessMenuCounts',id=>REGIONAL.menus.some(m=>m.id===id)],['orderGroupCounts',id=>REGIONAL.orders.some(o=>o.groups.some(g=>g.id===id))],['projectDeliveries',id=>REGIONAL.projects.some(p=>p.stages.some(t=>t.id===id))]]){obj(f[name],name);for(const [id,map]of Object.entries(f[name])){if(!allowed(id))error(name);counts(map,name,species);}}
  counts(f.payments,'项目付款',id=>REGIONAL.projects.some(p=>p.stages.some(t=>t.id===id)));
  const witness=(v,label,keys)=>{obj(v,label,keys);seq(v.firstSeq,label);seq(v.lastSeq,label);if(v.firstSeq>v.lastSeq)error(label);};
  obj(f.menuWitnesses,'菜单见证');for(const [id,v]of Object.entries(f.menuWitnesses)){if(!REGIONAL.menus.some(m=>m.id===id))error('菜单身份');witness(v,'菜单见证',['count','completeCount','firstSeq','lastSeq','lastSessionId','completeLastSessionId']);int(v.count,'接待次数',1);int(v.completeCount,'完整次数',0,v.count);if(!/^business-[1-9]\d*$/.test(v.lastSessionId)||v.completeLastSessionId!==null&&!/^business-[1-9]\d*$/.test(v.completeLastSessionId))error('菜单来源');}
  obj(f.tripWitnesses,'寻访见证');for(const [id,v]of Object.entries(f.tripWitnesses)){if(!regions.includes(id))error('寻访地区');witness(v,'寻访见证',['count','firstSeq','lastSeq']);int(v.count,'完整寻访次数',1);}
  obj(f.eventWitnesses,'卡见证');for(const [id,v]of Object.entries(f.eventWitnesses)){if(!REGIONAL.cards.some(c=>c.id===id))error('卡身份');witness(v,'卡见证',['firstSeq','lastSeq','count','tripId']);int(v.count,'卡次数',1);if(!/^trip-[1-9]\d*$/.test(v.tripId))error('卡来源');}
  obj(f.companionFirst,'首次同行');for(const [key,v]of Object.entries(f.companionFirst)){if(!species(key))error('同行身份');obj(v,'同行快照',['seq','tripId','region','gather','discover','environment','traits']);seq(v.seq,'同行序号');if(!/^trip-[1-9]\d*$/.test(v.tripId)||!regions.includes(v.region)||!['yard','water','wood'].includes(v.environment))error('同行来源');int(v.gather,'G',0,6);int(v.discover,'F',0,6);if(v.gather+v.discover!==6||!Array.isArray(v.traits)||new Set(v.traits).size!==v.traits.length||v.traits.some(x=>!['leaf','grain','portable','tea','floral','fruit','salt'].includes(x)))error('同行特征');}
  const predicates=new Set(['RG2-3:business','RG3-1:order','RG3-2:regionalBatch','RG3-3:business','SP-ALL:practice','SP-LEAF:practice','SP-SHAPE:practice','SP-TABLE:practice','COL-8:practice.trip','B-E1:cargoExchange','O04:display',
    ...REGIONAL.menus.flatMap(m=>[`${m.id}:validService`,`${m.id}:completeService`]),...REGIONAL.orders.map(o=>`${o.id}:complete`),...regions.map(r=>`O06:${r}:complete`),
    ...REGIONAL.collections.filter(c=>c.kind==='theme').map(c=>`${c.id}:practice.trip`),...regions.flatMap(r=>[`COL-${r}:practice.trip`,`COL-${r}:practice.business`,`COL-${r}:practice.order`])]);
  obj(f.predicateWitnesses,'复合见证');for(const [id,v]of Object.entries(f.predicateWitnesses)){if(!predicates.has(id))error('复合条件身份');witness(v,'复合见证',['firstSeq','lastSeq','sourceId']);if(typeof v.sourceId!=='string'||v.sourceId.length>80||!v.sourceId.length)error('复合见证来源');}
  return true;
}
