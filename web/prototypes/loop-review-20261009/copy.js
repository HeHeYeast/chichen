// Preview copy is separated by speaker and purpose. It does not grant progress.
export const VISITORS={
  RG1:{name:'旧厨房老顾客',place:'小店柜台',background:'old-customer-courtyard-v1.png',scene:'visitor-scene-rg1-v2.png',composition:'柜台近景 · 横向构图',
    portrait:'regular-old-customer-v1.png',
    first:'刚才在谷地看见荠菜了，嫩得很。回来一路都想着荠菜饼……你这儿做不做？',
    repeat:'我本来只想买一份的。算了，再装两份吧，回去肯定有人跟我抢。',
    cue:'寻访地点：谷地早市',action:'去谷地',request:'加订荠菜煎饼鸡 ×2',
    note:'老街坊说话直接，记得住自己买过什么。不替玩家讲解菜单、收益或解锁条件。'},
  RG2:{name:'溪边采购人',place:'溪边木桥',background:'river-picnic-v1.png',scene:'visitor-scene-rg2-v2.png',composition:'木桥斜穿 · 水岸远景',portrait:null,
    first:'帮我装点一手能拿的吧。上回一手端碗、一手拎篮子，过桥时连帽子都顾不上扶。',
    repeat:'这回带饭团就省事多了。照这个大小再包六个，篮子刚好装得下。',
    cue:'溪岸野餐 · 有新的采购需求',action:'看需求',request:'野餐饭团，六份',
    note:'关注怎么拿、怎么装、够不够分，语速利落。避免每句话都提“清爽的新味”。'},
  RG3:{name:'茶坡访客',place:'茶棚圆桌',background:'tea-shed-v1.png',scene:'visitor-scene-rg3-v2.png',composition:'围桌而坐 · 圆窗框景',portrait:null,
    first:'甜的先来一小份。我泡了壶浓茶，正想找点东西配着吃。',
    repeat:'刚才只顾着吃，茶都放凉了。这份记着，下回还要。',
    cue:'茶香便当 · 有新的搭配需求',action:'看需求',request:'配茶的小点，一小盘',
    note:'会比较味道，有自己的偏好；话不多，不写成茶文化讲解或诗句。'},
  RG4:{name:'沿湾货客',place:'沿湾码头',background:'bay-loading-v1.png',scene:'visitor-scene-rg4-v2.png',composition:'货箱前景 · 码头斜向纵深',portrait:null,
    first:'脆的单包，放箱子上头。我这一路可不平，到了再拆开，别只剩一包碎的。',
    repeat:'上回一点没压坏。箱子一开就分光了，连我都没轮上。这回得多带点。',
    cue:'沿湾装箱 · 有新的采购需求',action:'看需求',request:'随船点心，多装一层',
    note:'关心运输、数量和交货，说法干脆；不凭空添加私人身世。'}
};

export const HELP={
  journey:{title:'怎么寻访？',sections:[
    ['选地区和同行伙伴','每个地区有两种当地素材和多条配方线索。没发现的内容显示问号；带回来后，才能查看名称和用途。选好地区与同行伙伴，就可以出发。'],
    ['出门以后','同行伙伴在寻访结束前不能摆摊或交付。厨房可以继续开火，其他伙伴也能照常售卖。'],
    ['回来收下什么？','素材收入材料包，配方线索记入线索册。首次发现素材后开放商店补货，不需要再点一次鉴定。']
  ]},
  next:{title:'下一锅怎么选？',sections:[
    ['推荐和新伙伴','推荐综合考虑你追踪的伙伴、订单缺口和收益。新伙伴按线索册顺序列出最近的目标，并说明现在可以做什么。'],
    ['线索还不完整','已知的厨具和调味会先填好。还缺一味时，按线索中的类别挑选调味；如果连范围都不清楚，可以先去寻访。'],
    ['准备后再开火','“准备这一锅”只带入选择。正式开火前会显示费用；可以买到的缺少调味会计入总价。上一锅未收完时，会先提醒你收取。']
  ]},
  combination:{title:'组合怎么卖？',sections:[
    ['先看能搭哪些伙伴','新发现的伙伴可以和已有伙伴组成售卖组合。页面会列出可用库存；示例只是其中一种搭配，符合菜单条件的其他伙伴也能替换。'],
    ['什么时候加价？','家常小铺需要两种不同的家常出品，各摆至少 6 只，才能凑齐完整组合。满足条件后，符合菜单的出品售价增加 25%，额外放上的其他出品不享受这项加价。'],
    ['可摆数量为什么更少？','可摆数量已经扣除了留在家里的伙伴，以及被其他用途占用的数量。仓库里有货时可以直接补到摊上；不足时，页面会列出缺口，再去下一锅补货。']
  ]},
  visitors:{title:'常客来访',sections:[
    ['看看客人带来什么','客人可能提出采购需求，或告诉你一处值得去的地方。对话下方会显示对应入口。'],
    ['可以晚点处理','暂时不处理也没关系，之后可以从生意里的常客入口继续。看对话不会扣除伙伴；只有确认交付或实际卖出时才会扣货。']
  ]}
};
export function helpForStep(step){return step===1||step===2?HELP.journey:step===3||step===7?HELP.next:step===4||step===5?HELP.combination:HELP.visitors;}

// Each reply advances the conversation only. The last turn exposes the next action.
export const VISITOR_DIALOGUES={
  RG1:{first:[
    {text:'刚从谷地回来，篮子都拎沉了。你猜今天碰上什么了？',reply:'买到什么好东西了？'},
    {text:'荠菜！叶子嫩得很。我本来只想买一点，摊主又给添了一把。',reply:'拿来做什么好？'},
    {text:'煎饼呀。切碎了放进去，煎得边上脆一点……说得我又饿了。',reply:'去谷地看看'}
  ],repeat:[
    {text:'我本来只想买一份的。刚尝了一口，又有点舍不得拿回去了。',reply:'要不再带两份？'},
    {text:'那就再装两份。回去肯定有人跟我抢，先说好，这一份是我的。',reply:'看需求'}
  ]},
  RG2:{first:[
    {text:'帮我装点一手能拿的吧。上回过桥，可把我忙坏了。',reply:'怎么了？'},
    {text:'一手端碗，一手拎篮子，风还偏吹帽子。最后只能夹着碗走。',reply:'这回带饭团？'},
    {text:'这个好！分小份包，水壶还能放在旁边。',reply:'记下需求'}
  ],repeat:[
    {text:'饭团带着真省事。这回过桥，我总算腾出手扶帽子了。',reply:'还照上回的装？'},
    {text:'嗯，再包六个。篮子刚好装得下，不用换大的。',reply:'记下需求'}
  ]},
  RG3:{first:[
    {text:'我泡了壶浓茶。光喝茶，总觉得桌上少了点什么。',reply:'配点甜的？'},
    {text:'先来一小份吧。我怕一边聊一边吃，回过神来盘子就空了。',reply:'记下需求'}
  ],repeat:[
    {text:'你看，还是让我说中了。盘子空了，茶一口没动。',reply:'我再给你添点？'},
    {text:'先不添了，等我把这杯喝完。这个搭配记着，下回还要。',reply:'记住这个搭配'}
  ]},
  RG4:{first:[
    {text:'这箱得走一段水路。脆的先别放底下，我还要往上码货。',reply:'单包，放上面？'},
    {text:'对，再垫块布。到了拆开要还是整的，光剩碎屑可没法分。',reply:'记下装箱要求'}
  ],repeat:[
    {text:'上回装得好，一点没压坏。就是有件事没料到。',reply:'什么事？'},
    {text:'箱子一开就分光了，连我都没轮上。',reply:'这回多带点？'},
    {text:'得多带。再单放一小包，我先收着，免得又白跑。',reply:'记下需求'}
  ]}
};
