// Content-authoring source only. Never import this file from the game.
import fs from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';
import {LEGACY193 as D,EXPANSION} from '../../web/legacy-content.js';
import {SEASONAL_CHARACTERS} from '../../web/seasonal-pack.js';
import {ORIGINAL_RECIPE_CATALOG} from '../../web/recipe-catalog-data.js';
import {ABILITIES,DESCRIPTIONS} from '../../web/integration-data.js';
import {TRADE_SPECIES} from '../../web/trade-data.js';
import {INGREDIENT_UNLOCK_RULES} from '../../web/ingredient-unlocks.js';
const root=path.dirname(fileURLToPath(import.meta.url));
const save=(name,data)=>fs.writeFileSync(path.join(root,name),JSON.stringify(data,null,2)+'\n');
const words=s=>s?s.split(' '):[];
const rows=s=>s.trim().split('\n').map(l=>l.split('|'));
const tags={home:'家常',meal:'饱腹',portable:'便携',fresh:'清爽',tea:'茶味',steam:'蒸点',bake:'烘焙',sweet:'甜味',savory:'咸味',ginger:'姜香',mushroom:'菌菇',floral:'花香',roast:'焙香',bay:'海湾风味',grain:'谷物',leaf:'叶形',fruit:'果香'};
const traits={leaf:'叶形',grain:'谷物',portable:'便携',tea:'茶香',floral:'花香',fruit:'果香',salt:'盐晶'};
const regions=[
 {id:'V',name:'谷地早市',route:'yard',hours:2,environment:'yard',places:['菜畦','谷物棚边'],materials:[75,76],gate:'累计收取120只、发现5种，原菜园开放；荠菜首标本另需平底锅Lv.1。',kitchen:1,discoveries:5,collected:120,visual:'麦秆黄、荠菜绿、琥珀；扁圆、折片、谷粒；干燥纸包与矮棚，不画秋收节庆。',oldKeys:words('0:0 0:3 0:4 0:8 0:18 0:114 0:115 0:116 1:0 1:3 1:6'),entrySpecies:'V-C1'},
 {id:'R',name:'溪岸小集',route:'water',hours:6,environment:'water',places:['浅滩','岸边摊'],materials:[77,78],gate:'累计收取240只、发现8种，原溪岸开放；厨房Lv.2；平底锅Lv.2与蜂蜜供货就绪后启用首标本。',kitchen:2,discoveries:8,collected:240,visual:'柚皮黄、水芹青、溪水灰蓝；半月、折角、长梗；浅滩石阶与折叠布，不画远游船。',oldKeys:words('0:0 0:6 0:10 0:19 0:43 0:116 1:0 1:5 1:7 1:9 1:22'),entrySpecies:'R-C1'},
 {id:'T',name:'林间茶坡',route:'wood',hours:10,environment:'wood',places:['焙叶棚','花树径'],materials:[79,80],gate:'累计收取500只、发现24种，原茶坡开放；厨房Lv.2、水煮锅Lv.1。',kitchen:2,discoveries:24,collected:500,visual:'焙叶褐、桂花金、奶纸白；压印方饼、窄卷、细花簇；低棚与树影，不画整套茶楼。',oldKeys:words('0:10 0:18 0:28 0:114 0:117 0:121 1:11 1:39 1:40 1:41 1:57'),entrySpecies:'T-C1'},
 {id:'B',name:'风湾盐田',route:'bay',hours:12,environment:'water',places:['盐田小路','潮线草地'],materials:[81,82],gate:'厨房Lv.3、发现40种；前三区任一区辨认首标本并实收一款新品；从符合条件的溪岸寻访追寻沿湾路标，或沿湾货客引路。',kitchen:3,discoveries:40,collected:240,visual:'盐白、海蓬灰绿、浅烤金；方晶、叉枝、梯形、贝壳扇；近郊盐畦和潮痕，不画港口远航。',oldKeys:words('0:0 0:6 0:16 0:17 0:19 0:21 0:43 1:0 1:7 1:10 1:12 1:24'),entrySpecies:'B-C1'},
];
// work name | final name | tool | displayed tier | ingredients | category | tags | G | environment | traits | gate card | silhouette | food/material detail | contrast | description | clue
const authored={
 V:rows(`
荠菜煎饼鸡|荠菜煎饼鸡|1|1|75|家常|home meal portable savory leaf|15/4|yard|leaf portable||扁圆饼身，边缘两处小缺口|浅金煎面嵌不规则荠叶碎，尖嘴从薄边探出|0:4 荷包蛋鸡的白蛋边；本品没有中央蛋黄大圆|薄薄的饼边兜着碎叶香，走起路来总贴着地面晃。停下时喜欢把缺口朝外，像给旁边留了一个小座位。|菜畦边的碎叶香，放进平锅轻轻煎。 
麦芽脆片鸡|麦芽叠脆鸡|4|1|76 9|烘焙|bake portable sweet grain roast|20/4|yard|grain portable||三片错开的菱形脆片，后高前低|琥珀麦芽薄膜只连片角，碎麦点在切面|0:124 芝麻月饼鸡的圆花边；改成不对称薄片叠层|三片小脆片总想排整齐，最上面那片却偏要歪一点。晒到暖光时，片角那点麦芽色就亮了。|谷棚里的琥珀香，配面粉烤成薄片。
荠菜蒸团鸡|荠叶窝窝鸡|8|1|75 9|蒸点|steam meal savory leaf portable|11/11|yard|leaf grain||矮圆锥窝窝身，底缘外翻|粗面细孔、荠叶绿点，头顶凹窝而非包子收口|0:114 小笼包鸡；不画褶口或汤汁肚|头顶的小窝总接住一片落叶，抖两下才肯掉。它沿着笼边慢慢走，把朴素的面香带了一路。|面粉裹住荠菜，竹笼里留一个小窝。
麦芽小卷鸡|麦芽辫卷鸡|7|1|76 24|烘焙|bake meal portable sweet grain|20/5|yard|grain portable||矮胖三股辫结，短尾从结后翘起|麦芽亮边沿编纹走，奶油色只留辫缝|0:111 菠萝面包鸡；不用格纹圆顶，保留编结孔|一圈麦芽亮光绕着辫结走，总差一点才绕完。它低头找那一段，找着找着就靠着同伴睡着了。|麦芽和奶油交错，面包机能留住松软的辫纹。
芝麻薄脆鸡|芝麻折扇鸡|4|2|9 72 27|烘焙|bake portable savory grain|20/5|yard|grain portable||半展开的折扇饼，五道宽折|黑芝麻沿折谷排，淡盐烤色，脸在扇轴上方|0:124 芝麻月饼鸡；无圆边、月饼压花，咸味折片|把薄饼折成一把小扇，走一步便轻轻张开一点。真有风来时，它反倒赶紧合拢，护住折缝里的芝麻。|谷地摊主把面粉、黑芝麻和盐烤出折痕。
麦穗冠鸡|麦穗冠鸡|0|1|76 0|-|grain|4/16|yard|grain leaf|V-N2|水滴谷粒身，一束向右弯的长麦芒|哑光麦壳纹、窄叶翼，穗冠不发光|0:33 孔雀；非尾部开屏，0:85 门松鸡无竹节绳饰|头顶的麦芒总比自己先点头。站在谷棚旁边时，它会把小翅膀收紧，装作一粒还没决定落下的谷子。|认过谷棚的芒影，再用麦芽和食盐土在暖灯下留住它。
荠菜蛋卷鸭|荠菜蛋卷鸭|1|1|75 27|煎炸|meal portable savory leaf home|5/16|yard|leaf portable||斜切短圆筒，宽嘴从一端露出|蛋卷横截面三圈绿线，短鸭尾在另一端|V-C1 扁饼鸡；加盐、卷芯与横向鸭身，非圆饼换嘴|卷芯里藏着细细的荠菜纹，转身时总先把宽嘴探过去。等身子跟上，香气已经在前面等它了。|荠菜添一点盐，平锅里卷成短短一截。
麦芽米糕鸭|麦芽切糕鸭|2|2|76 16|蒸点|steam sweet grain portable meal|18/5|water|grain portable||低矮长方切糕，切面斜出一角|米粒纵向压实，琥珀麦芽夹层靠底，无顶帽|T-C2 桂花糕；切块米粒实心与斜角，非透明花冠|方方的米糕身总想对齐桌边，可宽嘴偏向一旁。它挪来挪去，最后决定斜着站也挺好。|麦芽与糯米在水煮锅里慢慢合成紧实切面。
荠菜汤包鸭|荠叶粉结鸭|2|2|75 48|炖煮|meal savory leaf fresh|4/19|water|leaf portable||扁椭圆粉结窝，鸭颈从结心探出|冬粉是粗细明确的绕结，荠叶只夹两处|1:22 冬粉鸭的垂发；改成低位闭合结窝，避开蒸笼鸭蛋限制|把冬粉绕成小窝，荠叶香便停在窝里。它每次起步都先看一眼结心，确认宽嘴没有钻错方向。|冬粉配一把谷地荠菜，在水煮锅里结成温热的一窝。
麦芽奶冻鸭|麦芽奶砖鸭|2|2|76 57 25|蒸点|sweet grain fresh|13/13|water|grain||横卧奶白砖形，圆角只在背部|薯粉凝成乳白方身，麦芽透明纹在侧壁，不挂焦糖帽|0:123 焦糖布丁鸡；无布丁倒扣形，非T-C4花奶环|它总把平整的一面靠在同伴旁边，像怕奶香漏出缝隙。麦芽细纹沿着侧身缓缓绕，怎么也绕不到宽嘴上。|牛乳与薯粉收成方砖，麦芽只沿侧面留一道甜纹。
椒香饭团鸭|椒香饭团鸭|2|2|31 41|炖煮|meal portable savory grain|7/16|water|grain portable||三角饭团顶压低，宽嘴横跨短边|米粒白身、胡椒小点集中脚边，不戴海苔围裙|1:62 烤菇饭团鸭；没有菇帽，无烤壳，辛香水煮饭团|小胡椒点都落在脚边，它却总抬头找香气。尖尖的饭团角碰到同伴，便不好意思地往后缩一点。|白米饭添黑胡椒，用水煮锅收出一个小三角。
谷粒斑鸭|谷粒斑鸭|0|1|76 31|-|grain|18/4|yard|grain portable|V-N2|矮宽鸭身，背部三排突出谷粒羽|稻白、麦棕点斑不对称；谷粒是羽纹不是食物|1:18 鸯的小浅斑；改成凸起谷粒背，脸不加眼线|背上的谷粒斑一排长、一排短，怎么都对不齐。它低头看不见，只好把背转给同伴看一看。|记下谷棚的谷粒排列，用麦芽和白米饭在暖灯下试一试。`),
 R:rows(`
山柚蜜煎鸡|山柚蜜煎鸡|1|2|77 33|煎炸|fresh fruit savory meal|18/4|water|fruit portable||半月煎身，厚柚皮弧包一侧|蜂蜜只亮在煎纹上，柚皮油点大且疏|0:6 柠檬香煎鸡；无头顶柠檬片，半月厚皮包身|半月形的柚皮边总先碰到同伴，蜜香才慢慢跟上。它停下便把弧边朝着溪风，闻得眼睛都眯了起来。|浅滩的厚柚皮遇上蜂蜜，在平锅里留下半月煎纹。
水芹饭团鸡|水芹饭团鸡|2|2|78 31|炖煮|meal portable fresh savory leaf|20/5|water|leaf portable||扁椭圆双饭瓣，短芹梗横系中腰|米粒明显，芹叶仅在结扣，不包成整片荷叶|0:116 荷叶糯米鸡；白米裸露、横束芹梗，没有叶被|两瓣饭团中间横着一小段芹梗，像把腰带系得太认真。它弯腰时先护住结扣，生怕清香松开。|白米饭在水煮锅里合成两瓣，水芹横着系住。
山柚酥饼鸡|柚皮口袋酥鸡|4|2|77 9|烘焙|bake portable sweet fruit|20/4|water|fruit portable||矩形鼓口袋，一角翻开|柚皮小丁露在袋口，酥层侧边三道，尖嘴在翻口下|0:127 雪帽曲奇鸡；无圆花边糖帽，酥袋空口可见|小口袋装满柚皮香，走几步便偷偷朝里面看。它一直没找到掉出去的那一点香气，只好再往前闻闻。|山柚藏进面粉口袋，烤箱把开口烘得酥酥的。
水芹蒸饺鸡|芹梗方饺鸡|8|2|78 25|蒸点|steam meal fresh savory leaf|5/20|water|leaf portable||四角捏起的方枕饺，前角低于后角|微透皮只透出芹梗直线，四角无连续月牙褶|0:118 水晶饺鸡；从月牙改方枕，竹蒸笼Lv.2才稳定供应薯粉|四个角都捏得整齐，偏有一角老想翘起来。它便把那一角朝外摆，让溪风替自己轻轻压一压。|薯粉裹住水芹梗，双层竹笼里捏出四只角。
姜香小饼鸡|姜丝锅贴鸡|1|2|9 29|煎炸|meal portable savory ginger|20/5|yard|portable||窄长舟形锅贴，背脊封口单线|姜丝只从端口伸出两根；金底白背无糖霜|1:50 姜饼鸭；由烤甜饼改咸锅贴、舟形剪影|窄窄的锅贴身像一只小船，姜丝在船尾轻轻翘着。它每次转弯都绕得大一点，免得把尾巴碰弯。|面粉与生姜不必等节日，在平锅里煎出一条金底。
柚灯鸡|柚灯鸡|0|1|77 0|-|fruit|4/17|water|fruit|R-N1|中空柚皮环，顶部短柄，脸在环下缘|纸白柚瓤内壁与黄皮穿孔；不自发光、不挂灯笼穗|1:47 文旦鸭整果身、0:80 南瓜鸡实心橙身；本品环形负空间|身子中间留着一小圈空，光从里面穿过去，像点亮了柚皮。它转身时总慢一点，怕把这点好看的亮处转丢。|看过柚皮在浅滩透光，再用山柚与食盐土守一盏暖灯。
山柚清茶鸭|山柚清茶鸭|6|1|77 3|茶饮|tea fresh fruit|5/20|wood|tea fruit||矮梨形清茶身，长柚皮卷从肩绕到尾|淡黄茶身中留细叶影，不顶圆果片|1:42 柠檬红茶鸭；无红茶暖红色、果片冠，长卷颈圈|一条长柚皮绕过脖子，总有一头落在背后。它闻完前面的茶香，又回头找后面那一截。|山柚和乌龙茶叶在水壶里相遇，香气绕成长长一卷。
水芹清煨鸭|水芹清煨鸭|5|1|78 27|炖煮|meal fresh savory leaf|17/5|water|leaf||浅碗状矮身，背后两根弯芹梗|清煨乳灰汤纹、浅绿茎节，不画温泉桶或油封罐|1:7 盐水鸭小碟饰；本品芹梗拱桥与敞口浅碗身|两根水芹梗在背后搭起小桥，它却总想从桥底看过去。慢煨的香气绕了一圈，最后又落回宽嘴边。|水芹添盐进炖锅，慢慢等清香回来。
山柚米团鸭|柚瓤双团鸭|2|2|77 16|蒸点|sweet fresh portable fruit grain|12/12|water|fruit grain||一大一小相贴双团，小团偏后|厚柚瓤白绵层，黄皮细屑沿两团接缝，不包绿叶|1:57 抹茶团子鸭；双团裸露白身、无叶被|大团先走一步，小团便慢慢跟上来。宽嘴总朝着两团中间，像在确认柚香有没有从缝里溜走。|糯米裹住山柚瓤，水煮锅里挨成一大一小。
水芹薄饼鸭|芹叶摊饼鸭|1|2|78 9|煎炸|meal portable fresh savory leaf|11/11|water|leaf portable||长椭圆薄饼，前缘卷起成低檐|芹叶压出完整三小叶影，油点细，宽嘴从卷檐下露出|V-C1 荠叶碎圆饼；长卷檐与整叶压痕，不是绿圆饼换鸭嘴|长长的饼边卷起一点，刚好给宽嘴遮住风。它抬头看路时，那片芹叶影也像跟着伸了个懒腰。|水芹与面粉摊成长饼，平锅只卷起一条边。
芝麻酥卷鸭|芝麻脆筒鸭|3|2|9 72|煎炸|portable sweet grain|20/5|yard|grain portable||斜立空心脆筒，筒口椭圆|黑芝麻在外壁疏密两带，内壁淡金；嘴从筒口横伸|V-C5 芝麻折扇鸡；油炸空筒与烘烤扇面分开|脆筒里总留着一点回声，它一动嘴，自己也要愣一下。外面的芝麻排列得很认真，里面却空空松松。|面粉卷成空筒，沾黑芝麻下油锅。
水纹叶鸭|水纹叶鸭|0|1|78 11|-|leaf|4/17|water|leaf|R-N1|横向舟叶身，前后尖端反翘|灰青叶脉和留白水线交叉，表面哑光不呈汤冻|1:48 枫叶鸭尖角放射；本品两端反翘长舟叶|叶脉和水纹在背上交错，走几步就像换了一条小溪。它停住时总把两端翘高，仿佛还在等下一道轻波。|浅滩水线记在叶背上，水芹和河水在暖灯下留住纹路。`),
 T:rows(`
焙茶锅煮鸡|焙叶茶衣鸡|2|1|79|炖煮|tea roast savory home|4/18|wood|tea leaf||短梨形蛋身，两瓣焙叶衣在肩侧翻开|茶褐面细密压叶纹；无蛋壳帽，浅胸露尖嘴|0:10 茶叶蛋鸡；去掉同款圆壳帽，以翻叶肩衣区分|两瓣焙叶衣总往肩外翻，露出一点浅色肚皮。它低头闻过一次，便不舍得再把衣边合紧了。|焙过的叶香先到水煮锅，不必等烧水壶。
桂花米糕鸡|桂花层糕鸡|2|2|80 16|蒸点|steam sweet floral grain portable|18/5|wood|floral grain||三层矮梯台，顶层偏小|半透米糕层间细金花点，花不聚成皇冠|V-D2 实心切糕；透亮梯层、细花夹层非谷粒方砖|每层糕边都藏着细小桂花，抬头时便一层层露出来。它想站得高一点，却又舍不得把最软的那层压扁。|桂花落进糯米，水煮锅里叠出三层香气。
焙茶小饼鸡|焙叶压纹鸡|4|1|79 9|烘焙|bake portable tea roast savory|18/4|wood|tea portable||长六角薄饼，中央一条叶脉压槽|深焙色边、灰褐哑面，无奶油或糖霜|0:127 曲奇圆花边；六角长饼与一条清晰叶脉|叶脉纹被压得很清楚，它便总爱正面朝着别人。走到窄处只好侧身，那条认真纹路也跟着藏起来了。|焙香叶与面粉压成薄饼，烤箱留住一条叶脉。
桂花奶冻鸡|桂花奶环鸡|2|2|80 57 25|蒸点|sweet floral fresh|19/6|wood|floral||乳白圆环身，脸在环上缘|薯粉凝奶环，桂花只在内圈，中央小孔透背景|V-D4 麦芽奶砖与0:123布丁；环形负空间，无焦糖帽|小圆环里落了几粒桂花，它低下头便看得清清楚楚。只要同伴靠近，就会把环心转过去让一让。|牛乳与薯粉围成软环，桂花留在环心边。
蜜香蒸糕鸡|蜜孔发糕鸡|8|1|9 33|蒸点|steam sweet portable grain|20/5|yard|grain portable||开裂四瓣发糕，圆底低顶|粗孔断面、蜂蜜只挂裂缝，无松饼堆叠|0:121 蜂蜜松饼鸡；蒸裂糕而非黄油三层煎饼|头顶四道裂口把蜜香分成了四份，它走向哪里，哪里先闻到一份。停下来时还要看看，裂缝有没有笑得太开。|面粉和蜂蜜到竹笼里，蒸开几道松软小口。
茶芽冠鸡|茶芽冠鸡|0|1|79 0|-|tea leaf|4/19|wood|tea leaf|T-N1|细长芽滴身，一芽两叶竖起|叶背铜褐，叶面灰绿，无饮品透明感|0:48 樱花鸡/0:85 门松鸡；纵向单芽不铺花、不捆竹节|头上的两片叶子一高一低，听见声响便一起轻晃。它喜欢站在焙棚阴影边，让叶背和叶面各分到一点光。|分清焙叶的两面，再把焙香叶与食盐土放在暖灯下。
焙香奶茶鸭|焙叶双层茶鸭|6|2|79 57|茶饮|tea roast sweet|5/20|wood|tea||矮圆柱双层茶身，顶部平斜面|上层奶白下层深焙，叶形小泡在层界，无奶瓶饰物|1:43 奶茶鸭单色圆身奶瓶；本品分层短柱|奶色和焙色在腰间碰头，谁也不肯多占一点。它一转身，那道整齐的分界就像系好了一圈腰带。|焙香叶与牛乳分成两层，需要能供应牛乳的水壶。
桂花茶鸭|桂花浮茶鸭|6|2|80 63|茶饮|tea floral fresh|5/20|wood|tea floral||浅敞口茶盏身，两侧低短翼|红茶淡琥珀，细桂花漂在边缘，不戴完整大花|1:44 菊花茶鸭/1:58 花香茶冻鸭；无大花冠、非冻体|桂花总漂到离宽嘴最远的那一边。它转过去闻，花又轻轻散开，像一场慢吞吞的捉迷藏。|红茶叶接住细桂花，水壶里不必添菊花皇冠。
焙香米团鸭|焙叶米枕鸭|2|2|79 31|炖煮|tea meal portable savory grain roast|17/6|wood|tea grain||横卧长米枕，两端压出浅折|焙叶碎夹在白米中线，枕面米粒裸露无裹叶|1:57 抹茶团子鸭；长白米枕与短绿糯团区分|长长的米枕一头高、一头低，它总把脸靠在低处。闻到中间那道焙香时，又忍不住往高处蹭一点。|焙香叶夹进白米饭，水煮锅里收成长枕。
桂花酥鸭|桂花风车酥鸭|4|2|80 9|烘焙|bake sweet floral portable|17/5|wood|floral portable||四角向心翻折的风车酥|桂花碎只在中心酥窝，鸭嘴从下角横出|0:124 月饼/0:127曲奇；四翼折角不做花瓣圆饼|四只酥角都朝中心靠，桂花香便藏在中间。它一动，像只小风车，却从来不肯真的转得太快。|桂花和面粉烤成四只折角，把花香拢在正中。
姜蜜软糕鸭|姜蜜炖梨形鸭|5|2|29 33|炖煮|sweet ginger fresh|6/16|wood|fruit portable||上窄下宽的梨形软身，侧边短卷梗|姜蜜半透琥珀纤维；只是梨形，不宣称配方含梨|1:50 姜饼鸭烤饼、1:63姜糖茶鸭饮品；改慢炖凝润实体|软软的身子下宽上窄，像把暖香稳稳放在底下。它低头时总显得格外认真，仿佛要把每一缕姜香都数清楚。|蜂蜜和生姜在炖锅里相伴，收出一颗梨形暖香。
花露羽鸭|桂露穗羽鸭|0|1|80 11|-|floral|4/18|wood|floral leaf|T-N2|细颈宽尾，三束下垂花穗羽|桂花小四瓣与哑光露点，不画可饮用透明身体|1:35 向日葵鸭/1:58花冻；垂穗尾羽不围面花盘|三束小花穗在尾后轻轻垂着，它走过窄处便先把尾巴收拢。露点没有掉下来，只换了一个亮亮的角度。|花树径留下细露的形状，桂花与河水在灯下慢慢成羽。`),
 B:rows(`
盐花烤鸡|盐花裂皮鸡|4|2|81 8|烘焙|meal savory bay roast|20/5|yard|salt portable||宽肩梯形烤身，侧面裂皮向外翘|盐晶只落裂缝，香草细叶在肩，烤皮大块拼面|0:17 香草烤鸡帽叶珠串；无帽饰，以裂皮梯形区分|烤皮裂出几道小缝，盐花就安稳地坐在里面。它挺起胸口时总慢一点，像怕把这些小客人晃醒。|盐花与香草叶到烤箱里，等烤皮自己裂开。
海蓬薄饼鸡|海蓬网饼鸡|1|2|82 9|煎炸|meal portable savory leaf bay|12/12|water|leaf portable||椭圆网孔薄饼，三处大孔贯穿|面粉金线交织，海蓬分叉梗在网结，非整绿饼|V-C1荠菜饼/R-D4芹叶饼；镂空网结而非实心饼|海蓬梗躲在网结旁，风一过便闻得分外清楚。它低头数孔，数到第三个就忘了第一个在哪里。|海蓬菜和面粉在平锅里牵成细网。
盐花奶酥鸡|盐花奶酥鸡|4|2|81 9 24|烘焙|bake sweet savory portable bay|20/5|yard|salt grain||两片厚短酥条交叠成十字|奶油酥层、疏大盐片落顶角，粉白切面不糖霜覆盖|0:127 雪帽曲奇鸡；十字短条、无雪帽和彩点|两条奶酥交叠得很稳，它却总想把上面那条推正。盐花落在角上，倒像已经替它选好了位置。|奶油、面粉和盐花一同烘成短短的交叉酥条。
海蓬蒸卷鸡|海蓬开口卷鸡|8|1|82 9|蒸点|steam meal savory portable leaf bay|18/4|water|leaf portable||立式开口卷，顶部V形缺口|海蓬分叉梗从卷心上探，哑白面皮不透明|0:114 小笼包收口；竖直开口卷、无收褶圆包|卷口从来合不拢，海蓬梗便从中间探出头。它走得很小心，怕把那一撮精神的绿意碰歪。|海蓬菜裹进面粉卷，竹笼里留一个开口。
胡椒米饼鸡|胡椒锅巴鸡|1|3|31 41 14|煎炸|meal portable savory grain|20/5|yard|grain portable||凹边不规则米片，背后两块翘角|酱油煎成琥珀底，胡椒集中边缘，无饭团立体心|V-D5椒香饭团鸭；平锅焦脆米片与水煮三角米团分开|米片边缘翘起两只角，看着像刚听见一个有趣消息。它每走一步都很认真，偏偏影子总比自己先拐弯。|白米饭、黑胡椒和酱油在平锅里结成脆底。
盐晶冠鸡|盐晶冠鸡|0|1|81 0|-|bay|5/14|water|salt|B-N1|矮方晶身，头上两片错高方晶冠|乳白晶面边薄透，冠为直角空心片，不雪晶尖瓣|0:25 雪人鸡/0:51凤凰；方晶与矮方身，不围巾、不火羽|两片方晶冠一高一低，光照过来便留下一道窄影。它总爱挪到光边站着，让那道影子慢慢追上自己。|认过盐田的方晶印，盐花与食盐土在暖灯下排好棱角。
盐花慢煨鸭|盐花葱结鸭|5|2|81 46|炖煮|meal savory fresh bay|18/4|water|salt leaf||低圆炖身，上搭单个宽葱结|盐白汤膜与青葱结，侧边浅凹无汤桶或罐盖|1:7盐水鸭/1:13油封鸭；明确葱结低冠，炖锅并用青葱|葱结搭得有些宽，每次点头都要跟着晃半拍。它索性慢慢走，让清清的盐香先到前面打个招呼。|盐花和青葱结到炖锅里，慢火把边角煨软。
海蓬饭团鸭|海蓬长饭鸭|2|2|82 31|炖煮|meal portable savory leaf bay|19/5|water|leaf grain||长舟形饭团，头低尾高|海蓬枝沿侧面叉开，白米细粒露顶，无海苔裹身|1:62烤菇饭团鸭/V-D5三角团；长舟侧叉枝、无菇帽|长饭团的尾端总比头高一点，海蓬梗沿两旁伸开。它转弯要多挪两步，却从来不肯把队伍甩在后面。|白米饭托住海蓬菜，水煮锅里排成一条长舟。
盐花焦糖鸭|盐花糖壳鸭|2|2|81 58|蒸点|sweet savory bay|6/18|water|salt||扁水滴糖壳，后缘有短短缺口|浅琥珀硬壳厚边，盐晶在内部亮点，无奶布丁芯|0:123焦糖布丁鸡/1:61焦糖吐司鸭；无乳白布丁或方吐司，糖壳实体|薄糖壳里藏着几粒盐花，笑起来时亮点也像在动。它总把缺口朝后，认真护住最完整的那一面。|焦糖浆遇到盐花，水煮锅里收成薄薄糖壳。
海蓬汤包鸭|海蓬豆窝鸭|5|2|82 49|炖煮|meal savory leaf bay|19/4|water|leaf grain||白豆簇成低窝，鸭颈从偏侧露出|海蓬叉枝托住豆粒，豆粒大于米粒，无汤包皮|V-D3粉结鸭/旧小笼包；豆簇开放窝，无蒸笼鸭蛋要求|白豆围成小窝，海蓬枝从底下稳稳托着。它刚把头探出来，便闻到一股不慌不忙的豆香。|海蓬菜陪白豆慢炖，不用裹面皮也能留住热乎香气。
柚香脆卷鸭|橙皮风帆鸭|4|2|44 10|烘焙|bake portable fresh fruit sweet|20/5|water|fruit portable||单片弧帆薄饼直立，侧身低矮|柳橙皮短丝沿帆边，金色薄饼不是螺旋卷|R-C3柚口袋酥/R-D5芝麻筒；旧柳橙与墨西哥薄饼，弧帆非闭卷|薄饼边立起一面小帆，橙香沿帆沿慢慢散开。它只在桌边挪几步，就觉得已经吹到一阵很好的风。|沿湾旧摊用柳橙配墨西哥薄饼，烤出一面弯弯小帆。
潮纹贝鸭|潮纹贝鸭|0|1|81 11|-|bay|6/14|water|salt leaf|B-N2|双壳扇翼半开，中间留窄长脸缝|灰白壳面两条潮线、内侧淡青，无珍珠发光|1:27白天鹅翼羽/1:14鸭嘴兽；两片壳铰接，不羽毛开屏|两片贝壳翼只肯打开一点，刚好让宽嘴探出去。潮纹留在外面，里面藏着一副安安静静的神情。|记住潮线在空壳上的弧度，盐花和河水在灯下留下两道线。`),
};
const species=[];
for(const [ri,region] of regions.entries())for(const [i,r] of authored[region.id].entries()){
 const [workName,name,tool,level,ings,category,ts,g,environment,tr,card,shape,detail,contrast,description,clue]=r;
 const egg=i<6?0:1,n=i%6+1,id=(egg?65:128)+ri*6+n-1,ingredientIds=words(ings).map(Number);
 const toolId=+tool,toolLevel=+level;
 const minimumKitchen=Math.max(region.kitchen,ingredientIds.length,toolLevel,toolId>=6&&toolId<=7?4:1,ingredientIds.some(x=>[57,58,72].includes(x))?4:1,toolId===8?toolLevel+1:1);
 species.push({id:`${region.id}-${egg?'D':'C'}${n}`,key:`${egg}:${id}`,catalogLabel:`${egg?'D':'C'}${String(id+1).padStart(3,'0')}`,workName,name,egg,region:region.id,
  recipe:{id:`REC-${region.id}-${egg?'D':'C'}${n}`,mode:'regional-trial',toolId,toolLevel,kitchenLevel:minimumKitchen,ingredients:ingredientIds.map(id=>({id,quantity:1})),exact:true,extraIngredientsAllowed:false,candidateKind:category==='-'?'regional-ornamental':'regional-food',firstChance:toolLevel>=3?.10:toolLevel>=2||ingredientIds.length>1?.20:.30,hardAttempt:0,repeatGuaranteed:0,companionPolicy:toolId===8?'legacy-steamer-without-guaranteed-inserts':'legacy-compatible-pool',materialSupplyRequired:ingredientIds},
  unlock:{region:region.id,identifiedMaterials:ingredientIds.filter(id=>id>=75),firstSpecimenForOldOnly:ingredientIds.every(id=>id<75),card:card||null,duckLicense:!!egg,fullMethodRequired:true,oldSupplyRules:'逐材料沿用baseline.ingredients.unlockAlternatives；不以地区身份绕过旧供货'},
  edible:category!=='-',viewable:true,signature:category==='-'?null:category,tags:words(ts),exploration:{G:+String(g).split('/')[0],F:+String(g).split('/')[1],environment,traits:words(tr)},
  art:{body:shape,foodAndMaterial:detail,identity:egg?'扁宽橙嘴、低位眼线、两只短蹼足；不画鸡冠':'短尖橙嘴、圆眼、小鸡爪；可用短冠羽但不能盖住主体形状',contrast,portrait:`四分之三视角，完整容纳${shape.split('，')[0]}；主体占框约78%，面朝内侧，保留脚下8%空白`,silhouette:`保留${shape}；不依赖颜色、小颗粒或文字识别`,palette:region.visual},
  description,clue:clue.trim(),menus:[],collections:[],orders:[],regulars:[],projects:[],uses:[],relatedCards:card?[card]:[]});
}
const S=id=>species.find(s=>s.id===id);
// Recipe corrections from actual old-pool and ingredient-supply arithmetic.
S('V-D5').recipe.kitchenLevel=3; // Black pepper needs a level-3 ordinary cookware route.
for(const id of ['V-D4','T-C4']){
 S(id).recipe.toolId=5;S(id).clue=S(id).clue.replace('牛乳与薯粉','牛乳与薯粉在炖锅里').replace('牛乳和薯粉','牛乳和薯粉在炖锅里');
}
S('T-D1').recipe.ingredients=[79,57,63].map(id=>({id,quantity:1}));
S('T-D1').recipe.materialSupplyRequired=[79,57,63];
S('T-D1').clue='红茶叶托住焙香叶的烘香，再添牛乳，在水壶里留两层颜色。';
S('B-C5').recipe.ingredients=[31,41].map(id=>({id,quantity:1}));
S('B-C5').recipe.materialSupplyRequired=[31,41];
S('B-C5').art.foodAndMaterial='米饭煎成浅金脆底，胡椒集中边缘，无饭团立体心';
S('B-C5').clue='白米饭与黑胡椒在平锅里结成脆底，不用再添一味酱汁。';
S('T-D5').name='姜蜜软糖鸭';
S('T-D5').tags=['sweet','ginger','portable'];
S('T-D5').exploration.traits=['portable'];
S('T-D5').art.body='上窄下宽的水滴糖身，侧边短卷梗';
S('T-D5').art.foodAndMaterial='姜蜜半透琥珀纤维，圆润糖块边；无水果切面、果核或果皮';
S('T-D5').art.portrait='四分之三视角，完整容纳水滴糖身；主体占框约78%，保留宽嘴与脚下空白';
S('T-D5').art.silhouette='保留窄顶宽底的凝润糖块和短卷梗，不依赖糖色或姜丝细纹';
S('T-D5').clue='蜂蜜和生姜在炖锅里相伴，收成一小块暖香软糖。';
S('B-D6').exploration.traits=['salt'];
// River-water shop qualification currently requires a sickness discovery. Regional ornamental
// recipes must not require players to deliberately dirty a kitchen or wait for that random branch.
for(const [id,ids,clue] of [
 ['R-D6',[78,27],'浅滩水线记在叶背上，水芹与盐在暖灯下留住纹路。'],
 ['T-D6',[80,0],'花树径留下细露的形状，桂花与食盐土在灯下慢慢成羽。'],
 ['B-D6',[81,27],'记住潮线在空壳上的弧度，盐花与盐在灯下留下两道线。'],
]){S(id).recipe.ingredients=ids.map(id=>({id,quantity:1}));S(id).recipe.materialSupplyRequired=ids;S(id).clue=clue;}
const allRecipes=[...ORIGINAL_RECIPE_CATALOG,...EXPANSION.characters.map(c=>({egg:0,id:c.id,toolId:8,minLevel:c.minLevel,ingredients:c.ingredients,kind:'steamer'})),...SEASONAL_CHARACTERS.map(c=>({egg:c.egg,id:c.id,toolId:c.toolId,minLevel:c.minLevel,ingredients:c.ingredients,kind:'seasonal',chapter:c.chapter}))];
// Explicit food whitelist, separate from signature. Entries excluded here remain tradeable by old rules.
const foodKeys=words('0:0 0:3 0:4 0:6 0:7 0:8 0:9 0:10 0:11 0:12 0:13 0:14 0:15 0:16 0:17 0:18 0:19 0:21 0:22 0:23 0:24 0:28 0:29 0:36 0:37 0:38 0:39 0:40 0:41 0:42 0:43 0:44 0:45 0:46 0:55 0:56 0:57 0:58 0:59 0:69 0:71 0:72 0:73 0:74 0:75 0:76 0:77 0:109 0:111 0:112 0:113 0:114 0:115 0:116 0:117 0:118 0:119 0:120 0:121 0:122 0:123 0:124 0:125 0:126 0:127 1:0 1:3 1:5 1:6 1:7 1:8 1:9 1:10 1:11 1:12 1:13 1:21 1:22 1:23 1:24 1:25 1:30 1:31 1:32 1:33 1:34 1:37 1:39 1:40 1:41 1:42 1:43 1:44 1:45 1:46 1:52 1:54 1:55 1:56 1:57 1:58 1:59 1:60 1:61 1:62 1:63 1:64');
const oldTagGroups={
 home:'0:0 0:3 0:8 0:12 0:16 0:21 1:0 1:3 1:6 1:8 1:10 1:12',
 meal:'0:3 0:4 0:7 0:8 0:11 0:12 0:13 0:14 0:15 0:16 0:17 0:18 0:19 0:21 0:22 0:23 0:24 0:28 0:36 0:37 0:38 0:39 0:40 0:41 0:42 0:43 0:44 0:45 0:46 0:55 0:56 0:57 0:58 0:59 0:109 0:111 0:112 0:113 0:114 0:115 0:116 0:118 1:3 1:6 1:7 1:8 1:9 1:10 1:11 1:12 1:13 1:21 1:22 1:23 1:24 1:25 1:31 1:32 1:33 1:34 1:52 1:54 1:55 1:56 1:61 1:62',
 portable:'0:4 0:12 0:18 0:19 0:28 0:40 0:43 0:44 0:57 0:109 0:111 0:112 0:113 0:114 0:115 0:116 0:117 0:118 0:119 0:120 0:121 0:124 0:125 0:127 1:8 1:9 1:23 1:33 1:52 1:54 1:55 1:56 1:57 1:60 1:61 1:62 1:64',
 fresh:'0:6 0:8 0:10 0:56 0:69 0:120 0:122 1:5 1:6 1:7 1:37 1:40 1:41 1:42 1:44 1:58 1:59',
 tea:'0:10 1:39 1:40 1:41 1:42 1:43 1:44 1:45 1:46 1:57 1:58 1:63',
 steam:'0:114 0:115 0:116 0:117 0:118 0:119 0:120 0:123 1:57 1:64',
 bake:'0:17 0:18 0:19 0:28 0:41 0:45 0:58 0:109 0:111 0:112 0:113 0:121 0:124 0:125 0:127 1:11 1:24 1:33 1:52 1:54 1:55 1:56 1:61 1:62',
 sweet:'0:28 0:29 0:45 0:73 0:74 0:75 0:76 0:77 0:111 0:112 0:117 0:119 0:120 0:121 0:122 0:123 0:124 0:125 0:126 0:127 1:11 1:30 1:43 1:44 1:45 1:46 1:54 1:57 1:58 1:59 1:60 1:61 1:63 1:64',
 savory:'0:0 0:3 0:4 0:6 0:7 0:8 0:9 0:10 0:11 0:12 0:13 0:14 0:15 0:16 0:17 0:18 0:19 0:21 0:22 0:23 0:24 0:36 0:37 0:38 0:39 0:40 0:41 0:42 0:43 0:44 0:46 0:55 0:56 0:57 0:58 0:59 0:109 0:113 0:114 0:115 0:116 0:118 1:0 1:3 1:5 1:6 1:7 1:8 1:9 1:10 1:12 1:13 1:21 1:22 1:23 1:24 1:25 1:31 1:32 1:33 1:34 1:52 1:55 1:56 1:62',
 ginger:'0:42 1:32 1:34 1:46 1:63',mushroom:'1:62',floral:'1:44 1:58',roast:'0:10 0:71 0:72 1:41',
 grain:'0:18 0:19 0:28 0:43 0:109 0:111 0:112 0:113 0:114 0:115 0:116 0:117 0:118 0:119 0:120 0:121 0:124 0:125 0:127 1:9 1:31 1:33 1:52 1:54 1:55 1:56 1:57 1:61 1:62 1:64',
 leaf:'0:10 0:17 0:20 0:36 0:38 0:48 0:60 0:63 0:83 0:85 0:94 0:98 0:116 1:21 1:35 1:39 1:40 1:41 1:48 1:57',fruit:'0:6 0:120 0:122 1:5 1:30 1:42 1:47 1:59',
};
const oldTraits={tea:'0:10 1:39 1:40 1:41 1:42 1:43 1:46 1:57 1:63',leaf:'0:17 0:20 0:36 0:38 0:48 0:60 0:63 0:83 0:85 0:94 0:98 0:116 1:21 1:35 1:48 1:57',grain:'0:18 0:28 0:43 0:109 0:111 0:112 0:114 0:115 1:52 1:54 1:61 1:62 1:64',portable:'0:0 0:3 0:4 0:8 0:12 0:19 0:40 0:43 0:114 0:115 0:116 0:118 0:121 0:125 1:0 1:3 1:6 1:8 1:9 1:52 1:54',floral:'0:48 0:60 0:63 0:86 0:92 1:35 1:44 1:58',fruit:'0:6 0:120 0:122 1:5 1:30 1:42 1:47 1:59',salt:'1:4 1:7 1:13 1:24 0:41'};
const baseline={species:D.characters.flatMap((list,egg)=>list.map(c=>{const key=`${egg}:${c.id}`;return {key,name:c.title_zh_CN,egg,description:DESCRIPTIONS[key],signature:TRADE_SPECIES[key].category,edible:foodKeys.includes(key),tags:Object.entries(oldTagGroups).filter(([,v])=>words(v).includes(key)).map(([k])=>k),exploration:{G:ABILITIES[key].gather,F:ABILITIES[key].discover,environment:ABILITIES[key].environment,traits:Object.entries(oldTraits).filter(([,v])=>words(v).includes(key)).map(([k])=>k)},recipes:allRecipes.filter(r=>r.egg===egg&&r.id===c.id),basePrice:c.cp_1,season:SEASONAL_CHARACTERS.find(s=>s.key===key)?.chapter??null,participation:['SP-ALL','PJ-4:optional-display'],artReference:`../../assets/png/Character/character_${egg}/character_${egg}_${c.id}_0_0.png`};})),ingredients:D.tools[2].map(i=>({id:i.id,name:i.title_zh_CN,price:i.buy_cp,unlockAlternatives:INGREDIENT_UNLOCK_RULES[i.id].alternatives,special:[68,69,70].includes(i.id)})),tools:D.tools[1].map(t=>({id:t.id,name:t.title_zh_CN,cookCP:t.lv_0_cook_cp})),notes:['名称、描述、旧配方、G/F、环境、招牌与价格为读取现行数据的快照。edible、tags、traits、participation是本期明确的作者配置，尚未接入。','非白名单的皮蛋鸡、咸蛋鸭、姜饼鸭、烤火鸡等只保留原交易与展示、寻访；不凭料理名称自动纳入营业。','旧图像引用：114–119使用点心图集；120–127和鸭57–64使用四时图集；其余按原图路径，原0/3/4可使用现行重绘。']};
const materials=rows(`
75|荠菜|V|15|V-S1|一片缺齿荠叶|叶缘深浅不齐，近叶柄处收成窄腰。|切碎后香气散得快，留整叶更容易看清纹路。|谷地送来的荠菜已经拣好，一小束也肯卖。|V-C1|0:116 0:115|与荷叶糯米鸡、烧麦鸡组成咸蒸点搭配；不改其原配方。|V-E1|一束整叶、碎叶小碟、标本单叶；缺齿大形在32像素仍可读
76|麦芽|V|20|V-S2|带芽的麦粒|短芽贴着谷粒侧面，芽尖浅白，和普通干谷粒一比就能分开。|不是所有甜味都要亮晶晶；麦芽能留住薄脆边，也能托起软面香。|谷棚留出的麦芽，按份装进小纸包，全年都能补。|V-C2|0:18 0:28|ALT-V以麦芽替代丸子鸡三兄弟的面粉；年糕鸡仍按糯米原方，只作菜单对照。|V-E2|带短芽麦粒、琥珀薄膜、开口纸包三态
77|山柚|R|20|R-S1|浅滩柚皮|外皮有大而疏的油点，白瓤比柠檬、柳橙厚得多。|香气多停在皮与白瓤之间，做成小口袋也不会只剩酸味。|岸边摊挑好的山柚，皮肉连着装，想试一锅就拿一份。|R-C1|0:6 1:5 1:42|ALT-R替代柠檬香煎鸡的柠檬；与橙汁香煎鸭比较果香，旧茶鸭不自动换叶。|R-E1|完整略扁果、厚皮切面、细皮卷；不画新角色脸
78|水芹|R|15|R-S2|三叶水芹梗|细梗带节，一梗托三片小叶，叶缘整齐，不像荠菜那样缺齿。|留梗能撑出形状，切叶能散出清香；饭团和慢煨各有用法。|溪岸的水芹沥过水才装包，梗叶都留着。|R-C2|0:116 1:22 1:7|与荷叶糯米鸡并排看包裹和绑束，冬粉鸭与盐水鸭作清淡搭配；无第二条溪岸替代配方。|R-E2|三叶带节梗、沥水小束、压在米饭上的叶印
79|焙香叶|T|25|T-S1|翻面的焙叶|叶面灰绿、叶背铜褐，卷边可见；不是茶树新叶，是当地焙过的叶子。|乌龙茶叶的香气更舒展，焙香叶在热锅里先给一股干爽烘香。|焙棚分好的小纸袋，开袋有香，补货不用等茶季。|T-C1|0:10 1:41 1:43|ALT-T给旧茶叶蛋鸡焙叶与盐做法；乌龙茶鸭和奶茶鸭仍保留原方用于对照。|T-E1|一叶正反两面、卷边叶堆、纸袋；不画焦黑烧毁叶
80|桂花|T|25|T-S2|一簇四瓣桂花|一簇细小四瓣，短梗，不像菊花那样长瓣散开。|花点不用铺满；夹在米糕层间，靠近才闻得到。|花树径收好的干桂花，一小撮装一份，香气慢慢用。|T-C2|1:44 1:58 0:117|菊花茶鸭、花香茶冻鸭对照细花与大花，奶黄流沙鸡作一咸一甜候选；不改菊花旧方。|T-E2|干花小撮、单簇四瓣、夹层点花；不做樱花粉色
81|盐花|B|20|B-S1|方晶盐片|薄方片叠在一起，边白中心浅透；不是雪晶六角，也不是食盐土。|一片放在裂皮边，一点落进清煨汤，咸香显出来的次序不同。|盐田晒好的盐花，薄片装小盒，不看潮汐也能买。|B-C1|1:7 0:41 1:24|ALT-B替代盐水鸭的盐；炭烤鸡与烟熏鸭可搭配展示，木炭原料不强制用于地区推进。|B-E1|方晶近景、白纸小盒、疏落烤皮盐粒
82|海蓬菜|B|20|B-S2|潮线分叉梗|肉质小节连续分叉，没有宽阔叶片；名字里有“菜”，却不长叶子。|小分叉适合留在卷口，切段则能散进米饭和豆窝。|潮线草地带回的海蓬菜，拣去老梗后按小束供应。|B-C2|1:25 1:62 0:116|白豆炖鸭、烤菇饭团鸭和荷叶糯米鸡提供豆、米、蒸点对照；不把海蓬直接覆盖海苔旧方。|B-E2|分叉整梗、切段小碟、束装；灰绿与节状剪影优先
`).map(([id,name,region,price,specimen,specimenName,recognition,lore,shop,entry,old,oldUse,event,art])=>({id:+id,stableId:`MAT-${region}-${id}`,name,region,priceCP:+price,specimen,specimenName,recognition,lore,shop,entrySpecies:entry,oldKeys:words(old),oldUse,event,art,uses:species.filter(s=>s.recipe.ingredients.some(i=>i.id===+id)).map(s=>s.id),tools:[...new Set(species.filter(s=>s.recipe.ingredients.some(i=>i.id===+id)).map(s=>s.recipe.toolId))],states:['specimen-found','identified','supply-open','used-in-cooking'],identification:'找到标本后免费辨认，供货同时永久开放；首趟试做料占原基础份数一格，辨认不重复赠料。',entryGate:'标本入池前，entrySpecies的厨具、厨房与所有旧材料供货须就绪；鸭材料入口优先取可执行鸡方。'}));
const alternatives=[
 {id:'ALT-V',region:'V',target:'0:18',name:'麦芽丸子做法',toolId:4,toolLevel:1,ingredients:[76],replaces:[9],unlock:'V-E2',tradeoff:'用20 CP麦芽替代20 CP面粉；可用地区采样补料。风味变成微甜麦香，沿用原身份、画像、售价和招牌。'},
 {id:'ALT-R',region:'R',target:'0:6',name:'山柚香煎做法',toolId:1,toolLevel:1,ingredients:[77],replaces:[1],unlock:'PJ-2',tradeoff:'20 CP山柚替代5 CP柠檬，花费更高但能消化溪岸余料；非收益强化。'},
 {id:'ALT-T',region:'T',target:'0:10',name:'焙叶盐香做法',toolId:2,toolLevel:1,ingredients:[79,27],replaces:[3],unlock:'T-E1',tradeoff:'从一槽10 CP乌龙叶变成两槽40 CP焙叶与盐；用地区余料做旧茶叶蛋，避免与T-C1单焙叶方重合。'},
 {id:'ALT-B',region:'B',target:'1:7',name:'盐花清煮做法',toolId:2,toolLevel:1,ingredients:[81],replaces:[27],unlock:'B-E1',tradeoff:'20 CP盐花替代15 CP盐；有固定地区补料来源，旧盐方仍更便宜。'},
].map(a=>({...a,mode:'local-alternative',quantityPerIngredient:1,guarantee:'沿用目标旧方的候选与概率，不加定向保证；替换匹配用料后再执行旧池，禁止同时注入新品、四时或蒸笼保底。',kitchenLevel:2,fullMethod:true}));
const cardRows=rows(`
V-S1|V|0|specimen|藏在菜畦边的香气|菜叶有一道不整齐的小齿边，带谁去都能看见。||||平锅与荠菜方向可执行|篮边夹着一片缺齿叶，轻轻一揉，香气便出来了。掌柜把它夹进册页，说这就是谷地常用的荠菜。|辨认荠菜，准备V-C1|75|缺齿单叶压在奶油纸角，背后一点菜畦土色
V-S2|V|1|specimen|麦粒边的一点白|谷棚里有刚露短芽的麦粒，烤箱和面粉备好再去。||||厨房Lv.2、烤箱Lv.1、面粉供货|几粒麦子挤在麻袋折边，短芽白白的。拾起来时，一股温和麦香留在了纸包上。|辨认麦芽，准备V-C2|76|麻布折边、三粒带芽麦、小张编号签
V-N1|V|0|lore|菜畦旁的空竹筛|竹筛有大小不同的孔，普通伙伴也能帮忙看清。||||无额外门槛|空竹筛靠在菜畦边，筛上的叶影一大一小。同行伙伴换个位置，影子也跟着换了一格。|把普通叶形队员加入V-E1；开启O01谷地补早饭方向||竹筛圆框局部与透孔叶影
V-N2|V|1|lore|谷棚的芒影|棚檐有细长的穗影，先看看，不必带来新品种。||||已辨认麦芽|麦芒的影子比谷粒长得多，落在棚边像一排小梳子。队伍停了一会儿，才发现每一排都不太齐。|开放V-C6/V-D6观赏试做方向，推进SP-SHAPE||长麦芒影与不整齐谷粒，保留一大一小对照
V-E1|V|0|event|把叶子放平|带一位有叶形特征的伙伴，再有一位适应菜园的同行者。|leaf|yard|0:17 0:0|无额外门槛|叶子压平后，缺齿和细茎一下都清楚了。同行伙伴没催着走，给这一页留下了整整齐齐的绿影。|记录叶形实践；O04花叶陈列可接受此事件作为替代实践||压平叶与两枚脚印，不画指定稀有角色
V-E2|V|1|event|一袋麦香两种做法|带一位谷物伙伴和一位适应菜园的伙伴，到谷棚闻闻。|grain|yard|0:18|已辨认麦芽|同一袋麦芽，一边香得脆，一边香得软。棚边的小纸条提醒你，熟悉的丸子也能换一种麦香。|学会ALT-V；开启O02谷棚点心箱||一袋麦芽旁各画薄片与丸子线稿
R-S1|R|0|specimen|漂在浅滩的柚皮|厚白瓤贴着浅黄皮；水边队员能多留意一会儿。|||1:0|R入门方就绪；水边适应仅机会加成，不作门槛|一弯柚皮停在石边，水退后还留着清香。白瓤比柠檬厚，揉一揉外皮，香气又亮了起来。|辨认山柚，准备R-C1|77|浅滩灰石与半月厚柚皮，不出现成品鸡
R-S2|R|1|specimen|摊边沥水的细梗|小束菜梗带着节，白米饭与水煮锅就绪后便能试。||||水煮锅Lv.2、白米饭供货|摊边一束水芹正在沥水，三片小叶贴着细梗。把纸包折好带回去，梗上的清香还在。|辨认水芹，准备R-C2|78|竹夹上的三叶芹束和两滴水
R-N1|R|0|lore|石上的两道水线|浅滩石头留着弧线，任意队伍都能慢慢看。||||已辨认本区任一材料|旧水线绕过一片叶子，新水线只碰到它的尖。队伍把这两条弧线记下来，像收好了溪水走过的脚步。|开放R-C6/R-D6观赏方向；两材料仍各自辨认||石面双水线、空心柚皮影、舟叶轮廓
R-N2|R|1|lore|布角压住了纸|摊边的布角压着一张配货纸，普通队员也能读懂图样。||||无额外门槛|纸上没写非带什么不可，只画了方便拿的小份和一份清爽。采购人说，坐在溪边吃的那一顿，留点选择才舒服。|开放O05雨后野餐和MN4方向；海湾门槛已满足时可另选追寻沿湾路标||折布一角与两格无字配货图
R-E1|R|0|event|柚香绕过石头|果香伙伴与适应水边的伙伴同行，就能比较哪边先闻到香。|fruit|water|0:6 1:0|已辨认山柚|石头挡住了水，却没挡住柚香。队伍沿着香气绕回来，把厚皮和果肉的区别写进了纸页。|山柚见闻实践；O06外地尝鲜箱溪岸变体||石头两侧的薄香线和厚柚皮，非魔法光效
R-E2|R|1|event|把野餐布折小|便携伙伴和水边伙伴同行，看看摊主怎样把布折进小篮。|portable|water|0:0 1:0|无额外门槛|布沿折了两次，篮边便空出一小条位置。摊主笑着说，带熟悉的出品也好，留一角给新味道就行。|PJ-2筹备提示；RG2-4可用此记录；O05布角变体||四折布、小篮空角，两件占位轮廓
T-S1|T|0|specimen|翻过来才闻见|焙叶棚里的叶子叶背铜褐，带谁去都能翻面看看；水煮锅即可试。||||水煮锅Lv.1|叶面还带一点灰绿，翻过来却闻到干爽烘香。焙棚主人拿两张纸分开装，让你回去慢慢比。|辨认焙香叶，准备T-C1|79|正反焙叶两片与浅炭褐纸角
T-S2|T|1|specimen|落进袖口的小花|花树径的细小四瓣花会落在纸边，糯米与水煮锅准备好再试。||||水煮锅Lv.2、糯米供货|几粒桂花落在纸边，花粒小小的，香气却没有躲起来。收拢时不必装满，留一撮就够记住。|辨认桂花，准备T-C2|80|四瓣小花簇与纸边，不画手或规定NPC外貌
T-N1|T|0|lore|棚下的两面叶|叶片翻开有两种颜色，普通伙伴也能记住。||||已辨认焙香叶|阴影里的叶面偏绿，亮处的叶背偏褐。队伍挪了两步，才把两种颜色都画在同一页上。|开放T-C6；为T-E1显示普通旧队员候选||一芽两叶正反面，焙棚低檐局部
T-N2|T|1|lore|花树下的慢露水|不用等真实清晨，花树径的这处露影一直可以追寻。||||已辨认桂花|露点停在花穗边，风过来也没急着落。大家看了一会儿，把垂下来的三束小影子记得清清楚楚。|开放T-D6；补充桂花见闻，不给额外实体料||三垂花穗、两处细露点、浅树影
T-E1|T|0|event|两种很像的叶子|茶香伙伴与林间适应伙伴同行，同一位可以兼任。|tea|wood|0:10|已辨认焙香叶|乌龙叶香先散开，焙叶的干香却靠近了才明显。队伍把旧做法留在左页，在右页补了一行焙叶与盐。|学会ALT-T；PJ-3焙叶事件记录；O07茶会添盘||左右叶样对照、锅的线稿，不复制两张发现卡
T-E2|T|1|event|留一点花香给咸味|便携伙伴与林间伙伴同行，把一咸一甜的搭配记下来。|portable|wood|0:0 0:10|已辨认桂花|纸上先摆了熟悉的咸味，又在旁边点了几粒桂花。访客说，不用每一样都甜，一桌也能很香。|RG3-3搭配实践候选；O08一咸一甜||两小纸碟，一侧细花、一侧叶影，无必需成品
B-S1|B|0|specimen|方晶落在纸上|盐田小路上有带直角的白色薄片，烤箱与香草叶就绪后可试。||||烤箱Lv.2、香草叶供货|纸上落着几片盐花，边缘很白，中间却透着一点光。它们没有雪晶的尖角，倒像几扇小窗。|辨认盐花，准备B-C1|81|白纸、疏落方盐晶和浅金盐畦
B-S2|B|1|specimen|潮线边的小分叉|没有阔叶，细梗一节一节分开；平底锅与面粉就绪后可试。||||平底锅Lv.2、面粉供货|潮线草地的小枝分成许多短节，握在纸包里刚好一小束。模样记进册里，回去能做成网饼和开口卷。|辨认海蓬菜，准备B-C2|82|灰绿分叉梗、纸包、潮地浅灰底
B-N1|B|0|lore|盐畦留方印|盐畦上的小方印，任意队伍都能看。||||已辨认盐花|盐花移开后，纸上还留着淡淡方印。队伍把边角描得稍大些，说这样回家也认得出。|开放B-C6观赏方向；SP-SHAPE||大小方印，边薄中心空，避免雪花六角
B-N2|B|1|lore|空贝壳的潮纹|草地边有空壳留下的弧纹，不必带来一位贝壳伙伴。||||已辨认盐花或海蓬菜|空壳的两边各留一道潮纹，合起来像一张没写完的小地图。队伍只记下弧度，把壳轻轻留在原处。|开放B-D6；四地风味展海湾见闻||双壳内外弧线与一点潮湿草影
B-E1|B|0|event|货筐上的盐晶|带便携伙伴与水边伙伴；可选另带家常6只换盐花，不带也能记录。|portable|water|0:0 1:0|已辨认盐花|货筐边粘着细盐晶，货客把纸签翻过来，画出一锅清煮的样子。新味不一定要换整套手艺，熟悉的盐水鸭也能试。|学会ALT-B；可选交换记录；RG4-4/PJ-4地方交流||货筐角、盐晶、空白货签；交换只加小图标
B-E2|B|1|event|草梗替篮角留空|叶形伙伴与水边伙伴同行；旧叶形鸡加普通鸭即可。|leaf|water|0:17 1:0|已辨认海蓬菜|细分叉把篮角撑出一点空，放豆窝也不挤，放饭团也不晃。队伍把这个小办法写给了厨房。|O11海湾试味箱；海风轻食收藏探索实践||分叉梗支住纸篮角、豆粒与米粒示意
`);
const cards=cardRows.map(([id,region,place,type,title,hint,trait,environment,oldTeam,gate,result,next,material,art])=>({id,region,place:regions.find(r=>r.id===region).places[+place],placeIndex:+place,type,title,hint,focus:type==='specimen'?'找标本':'寻见闻',team:{trait:trait||null,environment:environment||null,sameMemberMaySatisfyBoth:true,oldExamples:words(oldTeam||'0:0'),maxPredicates:trait?2:0},gate,result,next,material:material?+material:null,art,protection:'有合格未得卡才计失败；地区+关注方向连续3趟未得，第4趟必得；首趟保证当前可做入门标本。',effects:[]}));
for(const c of cards){
 if(c.material)c.effects.push({kind:'specimen',material:c.material},{kind:'supply-after-identification',material:c.material});
 for(const s of species.filter(s=>s.unlock.card===c.id))c.effects.push({kind:'species-direction',target:s.id});
 for(const a of alternatives.filter(a=>a.unlock===c.id))c.effects.push({kind:'alternative-method',target:a.id});
 c.effects.push({kind:'regional-collection',target:`COL-${c.region}`});
 if(c.type==='event')c.effects.push({kind:'special-practice',target:'SP-LEAF'});
}
cards.find(c=>c.id==='R-S1').team.bonusEnvironment='water';
cards.find(c=>c.id==='B-N1').team.bonusTrait='salt';
cards.find(c=>c.id==='B-N1').hint+='带盐晶特征的伙伴更容易发现。';
cards.find(c=>c.id==='T-N2').team.bonusTrait='floral';
cards.find(c=>c.id==='T-N2').hint+='带花香特征的伙伴更容易发现。';
for(const card of cards)card.discoveryChance={base:.25,perTeamF:.02,cap:.60,bonus:.05,bonusCondition:card.team.bonusTrait?{trait:card.team.bonusTrait}:card.team.bonusEnvironment?{environment:card.team.bonusEnvironment}:card.team.trait?{trait:card.team.trait}:null,bonusMaximumApplications:1};
cards.find(c=>c.id==='B-E1').exchange={optional:true,quantity:6,pool:'home',rewardMaterial:81,replacesBaseSlot:true,noBaseCP:true,ordinaryResultSameCard:true,extraTeamStock:true};
const guide={id:'GUIDE-B',countAsDiscoveryCard:false,sourceRegion:'R',sourcePlace:'岸边摊',hint:'顺着沿湾路标走一趟，就知道盐田在哪里。',gate:regions[3].gate,team:'任意1–3种；不要求新品或额外特征',trigger:'满足海湾基础门槛后，在溪岸选择追寻沿湾路标；完整归队确定获得引路资格，或RG4引路分支直接获得。',result:'路标拐过水边小坡，盐畦就在能望见归路的地方。把这条路记好，下次可以直接来了。',effect:'开放海湾路线；属于路线状态记录，不增加第25张发现卡，不占本趟发现卡名额。'};
const everyone=[...baseline.species.map(s=>({...s,id:s.key})),...species];
const get=id=>everyone.find(s=>s.id===id);
const pool=(...ts)=>everyone.filter(s=>s.edible&&ts.every(t=>s.tags.includes(t))).map(s=>s.id);
const union=(...sets)=>[...new Set(sets.flat())];
const menuDefs=[
 ['MN1','家常小铺',[['家常','home']], '两种不同家常，每种初备至少6只。','厨房收72只、发现3种、完成「第一笔生意」','搭配不同的家常出品。','O01','RG1','PJ-1'],
 ['MN2','茶香便当',[['茶味','tea'],['饱腹','meal'],['便携小点','portable','sweet']], '茶味、饱腹必备；再加一种便携甜小点，三种各初备至少3只，就是完整菜单。','已收录茶味和饱腹出品各一种','茶味搭配饱腹出品，可加便携小点。','O07','RG1','PJ-1'],
 ['MN3','蒸笼早市',[['蒸点','steam'],['家常','home']], '蒸点、家常都摆上，并有咸、甜各一种各备至少3只；甜的可放第三位。','已有竹蒸笼，或收录过用水煮锅做的蒸点','蒸点搭配家常出品。','O03','RG1','PJ-1'],
 ['MN4','溪岸野餐',[['便携主食','portable','meal'],['清爽','fresh']], '三种不同出品各备至少3只，鸡宝鸭宝都要有；第三种选便携或清爽的。','溪岸路线开放，并已收录便携主食和清爽出品各一种','便携主食搭配清爽出品。','O05','RG2','PJ-2'],
 ['MN5','茶坡小点',[['茶味','tea'],['烘焙或蒸点','bake|steam']], '三种不同出品，各初备至少3只，其中至少一种花香或焙香；第三种选茶味或小点。','茶坡地区开放','茶味搭配烘焙或蒸点。','O08','RG3','PJ-3'],
 ['MN6','暖锅小聚',[['炖煮','@stew'],['家常','home']], '三种不同出品各备至少3只，至少一种姜香或菌菇；第三种也从炖煮、家常、姜香、菌菇里选。','已收录炖煮和家常出品各一种','炖煮搭配家常出品。','O09','RG1','PJ-3'],
 ['MN7','海风轻食',[['咸香主食','savory','meal'],['清爽','fresh']], '菜单与搭配位的出品各初备至少3只，鸡鸭都要有，并含一种已辨认材料的海湾风味。','海湾路线开放','咸香主食搭配清爽出品。','O11','RG4','PJ-4'],
 ['MN8','四时相逢',[['四时手作甲','@season'],['四时手作乙','@season']], '三种出品来自三个不同季节，各备至少3只。','收录任意两种四时手作（不限当前季节）','搭配两个季节的四时手作。','O10','RG3','PJ-4'],
];
const resolveRole=spec=>spec[1]==='@stew'?everyone.filter(s=>s.edible&&(s.signature==='炖煮'||s.tags.includes('home')&&((s.recipes??[]).some(r=>r.toolId===5)))).map(s=>s.id):spec[1]==='@season'?baseline.species.filter(s=>s.season).map(s=>s.key):everyone.filter(s=>s.edible&&spec.slice(1).every(t=>t.split('|').some(x=>s.tags.includes(x)))).map(s=>s.id);
const menus=menuDefs.map(([id,name,rs,complete,unlock,text,order,regular,project])=>({id,name,roles:rs.map((s,i)=>({id:`${id}-R${i+1}`,name:s[0],required:!(id==='MN2'&&i===2),allowed:resolveRole(s),maxSpecies:2})),complete,unlock,text,orders:[order],regulars:[regular],projects:[project],oneSpeciesOneRole:true,maxSpecies:6,validService:'同单实际成交覆盖每个必要角色，每角色至少1只且总数至少6只；完整印记需成交所在窗口达到完整档，不把备货当成交。',tiers:{ordinary:0,suitable:.05,complete:.08},perBirdBonusCapCP:2}));
// Explicit examples, quantities are initial stock; do not imply a complete menu lasts every window.
const examples={MN1:[['0:0','0:3'],['0:8','1:0']],MN2:[['0:10','0:19','0:121'],['1:41','0:43','T-C5']],MN3:[['0:114','0:0','0:117'],['V-C3','1:3','T-C2']],MN4:[['0:19','1:7','0:8'],['0:43','1:5','R-C2']],MN5:[['0:10','0:18','0:117'],['1:41','T-C3','T-D4']],MN6:[['0:21','0:0','0:42'],['1:12','1:3','1:34']],MN7:[['0:19','1:7','B-C1'],['1:33','0:8','B-D2']],MN8:[['0:121','0:123','0:124'],['1:57','1:59','1:64']]};
for(const m of menus){m.examples=examples[m.id].map(a=>a.map(id=>({id,quantity:m.id==='MN1'?6:3})));if(m.roles.length===2&&m.id!=='MN1')m.roles.push({id:m.id+'-R3',name:'补充搭配',required:false,allowed:m.id==='MN8'?resolveRole(['','@season']):m.id==='MN4'?union(pool('portable'),pool('fresh')):m.id==='MN5'?union(pool('tea'),pool('bake'),pool('steam')):m.id==='MN7'?pool('bay'):everyone.filter(s=>s.edible).map(s=>s.id),maxSpecies:2});}
menus.find(m=>m.id==='MN3').roles[2].allowed=union(pool('steam'),pool('home'));
menus.find(m=>m.id==='MN6').roles[2].allowed=union(resolveRole(['','@stew']),pool('home'),pool('ginger'),pool('mushroom'));
// MN1 has one role allowing two different species; all food tags are author-defined, never inferred from names.
for(const s of species)s.menus=menus.filter(m=>m.roles.some(r=>r.allowed.includes(s.id))).map(m=>m.id);
const selectors={home:pool('home'),tea:pool('tea'),meal:pool('meal'),portableMeal:pool('portable','meal'),fresh:pool('fresh'),snack:union(pool('steam'),pool('bake'),pool('portable','sweet')),savory:pool('savory'),sweet:pool('sweet'),stew:resolveRole(['','@stew']),floral:union(pool('floral'),pool('roast')),season:resolveRole(['','@season']),display:everyone.filter(s=>s.tags.includes('leaf')||s.tags.includes('floral')||!s.edible).map(s=>s.id),food:everyone.filter(s=>s.edible).map(s=>s.id)};
const orderDefs=[
 ['O01','街坊备早饭','home:12',12,'MN1','V-N1','RG1','PJ-1','明早家里人多，帮我备一篮家常的。挑你做得顺手的就行。','这些够了，明早少忙一阵。谢谢！','街坊便签'],
 ['O02','谷棚点心箱','snack:6 home:6',18,'MN3','V-E2','RG1','PJ-1','我要带去谷棚。小点单独包，别让家常那包压着。','两包都齐了，我这就带过去。','谷棚纸包图'],
 ['O03','蒸笼留一格','steam:6 home:6',20,'MN3','V-N1','RG1','PJ-1','蒸点和家常各来一些，大家口味不一样。咸甜你看着配。','这下都能挑到想吃的了。','笼格便签'],
 ['O04','花叶陈列','display:3',0,'','V-E1','RG2','PJ-4','想看看你家的伙伴，挑三种模样有趣的就好。我画下来，不带走。','画好了，谢谢你让它们出来给我看看。','花叶明信片'],
 ['O05','雨后野餐','portableMeal:6 fresh:6',20,'MN4','R-N2','RG2','PJ-2','雨停了，正好去溪边坐坐。帮我备点好拿的，再搭一样清爽的。','包得挺牢，我提着走了。','布角笔记'],
 ['O06','外地尝鲜箱','regionFood:6 home:6',24,'MN4','R-E1','RG2','PJ-4','当地的新做法我想尝尝，再配些家常的。分开装，免得认错。','这包是当地做法，对吧？我先尝这一包。','地方货签'],
 ['O07','茶会添一盘','tea:6 snack:6',20,'MN2','T-E1','RG3','PJ-3','茶味的和小点各备一些。茶叶蛋也行，这次来的人都爱吃。','摆上这些，茶会就可以开始了。','添盘小票'],
 ['O08','一咸一甜','savory:6 sweet:6',20,'MN5','T-E2','RG3','PJ-3','咸的、甜的分两包吧。我都想尝，先别混在一起。','两包我都收好了，谢谢。','两味评语'],
 ['O09','暖锅多双筷','stew:6 home:6',20,'MN6','T-N1','RG1','PJ-3','今天多了几个人吃饭。炖煮的搭些家常，帮我一起备好。','够大家吃了，我回去摆碗筷。','锅边便签'],
 ['O10','四时装一盒','season:12',24,'MN8','T-N2','RG3','PJ-4','四时手记里的出品，挑两个季节各装一些。我想比着尝尝。','原来这两样是不同季节的做法，我记住了。','四时夹页'],
 ['O11','沿湾轻食箱','savoryMeal:6 fresh:6',24,'MN7','B-E2','RG4','PJ-4','我要往沿湾走，带些咸香的主食，再配一点清爽的。分开包就行。','装得稳当，路上不用重新整理了。','沿湾装箱图'],
 ['O12','街角分享篮','food:12',12,'MN1','B-N2','RG4','PJ-4','街角几个人一起分，挑两种家常出品吧。每样都装些，大家能换着尝。','我拿去给大家分，每人都能尝两样。','分享便签'],
];
selectors.steam=pool('steam');selectors.savoryMeal=pool('savory','meal');
const orders=orderDefs.map(([id,name,groups,bonus,menu,card,regular,project,request,finish,note])=>({id,name,kind:id==='O04'?'display':'purchase',repeatable:id!=='O04',groups:groups.split(' ').map((x,i)=>{const [selector,n]=x.split(':');return {id:`${id}-G${i+1}`,selector,quantity:+n,allowed:selector==='regionFood'?species.filter(s=>s.edible).map(s=>s.id):selectors[selector]};}),minimumDistinct:id==='O04'?3:id==='O01'?1:2,bonusCP:bonus,menu:menu||null,card,regular,project,request,finish,firstResult:{id:`NOTE-${id}`,name:note,repeatReward:false},qualification:'生成前检查所有需求组至少有一条已收录且已知完整配方、蛋种开放、厨具与供货可执行的解；不足时不生成该单。数量可分批补，绝不要求库存现已有12只新品。',payment:id==='O04'?'只检查在家可用各1只，不占用、不扣货、无CP；首次留明信片。':'每次交付按现有基础售价付款；完成时只发一次约定酬谢12–24 CP，不叠招牌/主题/整筐。',variants:[]}));
orders.find(o=>o.id==='O04').groups[0].quantity=1;
orders.find(o=>o.id==='O04').displayDistinct=3;
orders.find(o=>o.id==='O10').seasonRule='任意两章各6只，共12只；组内可混交；接取冻结已选两章。';
for(const o of orders){o.variants.push({id:o.id+'-A',label:'常备',text:o.request});if(o.id==='O06'){o.variants=[{id:'O06-A',label:'谷地或溪岸',regions:['V','R'],text:'选择已经会做的一处地方味，混装6只；另备6只家常。'},{id:'O06-B',label:'茶坡',regions:['T'],text:'焙香或花香的地方新品混装6只，再搭6只熟悉家常。'},{id:'O06-C',label:'海湾',regions:['B'],text:'海湾新品任选混装6只；不要求同一新品凑满，家常另备6只。'}];}else if(o.id==='O04'){o.variants=[{id:'O04-A',label:'花叶一角',allowed:everyone.filter(s=>s.tags.includes('leaf')||s.tags.includes('floral')).map(s=>s.id),text:'花或叶造型任选3种，在家各1只供看。'},{id:'O04-B',label:'有趣的轮廓',allowed:selectors.display,text:'观赏或花叶造型任选3种，在家各1只供看。'}];}else{o.variants.push({id:o.id+'-B',label:'鸡蛋厨房',egg:0,text:'还没有鸭蛋也不要紧，这单只收鸡宝。'});}}
const regularDefs=[
 ['RG1','旧厨房老顾客','V','M09',[
  ['早饭趁热','收240只、发现8种且完成旧第一笔生意；旧三章完成只追认已认识，不发旧酬谢','MN1有效接待一次','O01完成一次','今天这份火候正好。\n\n你忙你的，我坐门口吃，篮子一会儿再拿。','接下来可经营茶香便当，或完成茶会采购。','NOTE-RG1-1'],
  ['分开包','上一段已读','MN2有效接待一次','O07交付茶味6只和小点6只','茶香这包归我，小点带回去。\n\n袋口折一下就行，我怕拎到家弄混了。','接下来可经营蒸笼早市或暖锅小聚，也可完成对应订单。','NOTE-RG1-2'],
  ['今天不赶路','上一段已读','MN3或MN6有效接待一次','O03或O09完成一次','今天不用打包，我坐下慢慢吃。\n\n刚才只顾着赶路，连早饭都没好好尝。','收取过六种食用伙伴，再完成两种菜单接待或两种订单。以前的记录也算。','NOTE-RG1-3'],
  ['照这张单子','上一段已读；不同食用品种实收至少6种','任意两种菜单各有过一次有效接待（以前的也算）','完成过两种不同的订单（以前的也算）','你现在做的，我也认得几样了。\n\n这张是我常买的，贴柜台里吧，省得我每回来都念一遍。','已获得老顾客的纪念物，可到收藏中查看。','M09'],
 ]],
 ['RG2','溪边采购人','R','M10',[
  ['拎着也方便','原溪岸开放、情境采购开放','MN4有效接待一次','O05完成一次','这几包大小正好，篮子里还放得下水壶。\n\n刚才过桥腾不出手，可把我忙坏了。','去溪岸寻访，带回并辨认一种当地素材。','NOTE-RG2-1'],
  ['先尝一样','上一段已读；辨认77或78，任一即可','带果香或叶形旧伙伴完成R-E1或R-E2','免费寻访取得另一张溪岸标本或地点见闻','你带回来的这个，我在溪边见过。\n\n原来还能下锅？做出来让我尝尝，光看样子我可猜不出味道。','收取一种溪岸食用新品，再用它营业或交付溪岸尝鲜订单。','NOTE-RG2-2'],
  ['给新口味做个记号','上一段已读；溪岸任意一款食用新品实收','在一次MN4或MN2有效接待里卖出至少1只溪岸新品','O06选溪岸味完成一次（6只地方味可混装）','新做的那包我尝过了。\n\n以后在袋口打个结吧，不拆开也能认出来。','可继续溪岸野餐，或查看常客页列出的订单条件。','NOTE-RG2-3'],
  ['一块野餐布','上一段已读','R-E2已记录且MN4有效接待一次','O05再完成一单；阶段激活后新交付，旧同单不重复扣货','这块布给你，洗过了，干净的。\n\n哪天你也去溪边坐坐，别总是我来拿东西。','已获得折叠野餐布，可到收藏中查看。','M10'],
 ]],
 ['RG3','茶坡访客','T','M11',[
  ['先尝两样','茶坡地区开放','茶味任选两种各售至少1只，累计共6只','给O07的茶味组交齐两种共6只（不必整单完成）','这两样先别混着装，我想各尝一口。\n\n嗯……这一份茶味轻些，空口吃也合适。','到林间茶坡寻访，带回并辨认当地素材；茶叶蛋鸡可以同行。','NOTE-RG3-1'],
  ['焙过的叶子','上一段已读；已辨认焙香叶','记下T-E1（只带茶叶蛋鸡也能满足队伍条件）','记下T-N1，再用79做一锅地区试做并全部收完（出什么都算）','这回的香气重些。\n\n叶子焙过了？难怪，我还以为是自己今天把茶泡淡了。','接下来可搭配咸味与甜味出品营业，或完成一咸一甜订单。','NOTE-RG3-2'],
  ['甜的放旁边','上一段已读','在一次MN5有效接待里，咸、甜出品各卖出至少1只','O08完成一次','甜的这份，得配浓一点的茶。\n\n刚才一口接一口，差点忘了桌上还有咸的。','茶味与小点任选三种，营业售出或订单交付累计十八只，每种至少一只。','NOTE-RG3-3'],
  ['带来的茶罐','上一段已读','茶味/小点任选3种，各至少1只、营业累计18只','茶味/小点任选3种，各至少1只、订单累计交付18只','这只茶罐送你，盖子要拧紧。\n\n下次有了新点心，我带茶来，我们坐下试。','已获得小茶罐，可到收藏中查看。','M11'],
 ]],
 ['RG4','沿湾货客','B','M12',[
  ['盐田怎么走','厨房Lv.3、发现40种且前三区任一入门标本辨认+新品实收；不要求已开海湾','MN1有效接待一次','O12完成一次','货装好了。\n\n我回去要经过盐田，你想去的话，沿水边走，过了小坡就到了。路我画在这张纸上了。','风湾盐田已开放，可从寻访页出发。','NOTE-RG4-1'],
  ['盐花少放些','上一段已读；辨认盐花','B-N1已记录','B-E1已记录，不用带货','盐田的盐花，你也找着了？先放一点，尝过再添。\n\n看着薄，咸味可不轻。','可按线索试做海湾新品；下一次轻食采购也接受符合条件的旧品种。','NOTE-RG4-2'],
  ['垫好再装','上一段已读','MN7有效接待一次','O11完成一次，全用旧品种也行','这次两包分得清楚，拿起来省事。\n\n箱底我垫了布，路上晃也不怕撞散。','再去盐田可带货交换，或完成海湾尝鲜订单。具体需求见常客页。','NOTE-RG4-3'],
  ['认箱子的记号','上一段已读','在B-E1带货交换一次（多带6只家常换1份盐花）','O06选海湾味完成一次（6只地方味＋6只家常）','这枚贝壳签系你箱子上吧。\n\n码头的箱子一个样，有了它，我老远就认得出。','已获得贝壳货签，可到收藏中查看。','M12'],
 ]],
];
const regulars=regularDefs.map(([id,name,region,memento,stages])=>({id,name,region,memento,stages:stages.map(([title,gate,a,b,text,next,reward],i)=>({id:`${id}-${i+1}`,title,gate,alternatives:[a,b],text,next,reward,recordPolicy:i===0?'门槛事实可追认；故事在合格营业来客或手动完成对应采购后呈现':'永久发现和经营记录默认追认，文本注明“再完成/激活后”才要求新交易；读一段才开放下一段，读取本身不扣货。'})),delivery:'任一分支达成即将下一段保存在常客页，最多1段未读；有营业的下一次合格来客判定优先呈现。无营业的手动采购/发现分支达成后同样可在常客页阅读，不为看故事强制再开店。',completion:'仅一次性文字/纪念物，无好感数值，无时间期限，无每日问候。'}));
regulars.find(r=>r.id==='RG2').stages[1].alternatives[0]='记下R-E1或R-E2（旧伙伴也能满足队伍条件）';
regulars.find(r=>r.id==='RG2').stages[3].alternatives[1]='读完上一段后再完成一单O05，或完成过一次O04（以前的也算）';
const projects=[
 {id:'PJ-1',name:'我的小店招牌册',gate:'厨房Lv.2、发现12种',stages:[{id:'PJ-1-A',check:'可营业品种任意6种已收录',consume:null,costCP:0},{id:'PJ-1-B',check:'两种不同菜单各有过一次有效接待',consume:null,costCP:0},{id:'PJ-1-C',check:'前两项已满足',consume:null,costCP:200}],oldAllowed:foodKeys,newAllowed:species.filter(s=>s.edible).map(s=>s.id),result:'招牌册册页组件＋3套菜单预设保存资格；配在M01下陈列，不另算第13件纪念物。',text:'纸上写下的不只是名字，还有这家厨房招待过的几顿饭。空白页留着，以后想换一味也放得下。',next:'可以把已经接待过的菜单存成预设，再去看溪岸的小篮。',art:'M01账页夹的打开态：6格小图位置、两枚菜单印、空白题签；不烘焙文字'},
 {id:'PJ-2',name:'溪岸风味篮',gate:'厨房Lv.2，溪岸任一标本辨认',stages:[{id:'PJ-2-A',check:'R-S1、R-S2已登记并辨认；溪岸新品任意4种实收，鸡鸭各至少1种，观赏也可算实收',consume:null,costCP:100},{id:'PJ-2-B',check:'前项满足；选任意两种食用料理',consume:{distinct:2,quantityEach:6,allowed:union(pool('meal'),pool('steam'),pool('bake'))},costCP:0},{id:'PJ-2-C',check:'筹备交付已完成',consume:null,costCP:400}],oldAllowed:union(pool('meal'),pool('steam'),pool('bake')).filter(id=>id.includes(':')),newAllowed:species.filter(s=>s.region==='R'||s.edible).map(s=>s.id),result:'溪岸插页完成态、野餐篮底座组件；学会ALT-R。篮底座与M04或M10搭配，不新增独立陈列件。',text:'两张标本夹在布角，篮里装的是这次认真挑好的两样。它不必每次都装满，空处还能留给下一阵溪风。',next:'山柚可以替下柠檬，试试柠檬香煎鸡的地方做法。',art:'M04纸篮外套的竹篮底座、M10折布覆篮态、溪岸双标本夹页'},
 {id:'PJ-3',name:'茶坡一桌',gate:'茶坡地区开放',stages:[{id:'PJ-3-A',check:'记下T-E1',consume:null,costCP:200},{id:'PJ-3-B',check:'茶味或点心任选3种，各至少1只；营业或订单累计36只；茶坡访客任一段完成',consume:null,costCP:0},{id:'PJ-3-C',check:'前两项满足',consume:null,costCP:600}],oldAllowed:union(selectors.tea,selectors.snack).filter(id=>id.includes(':')),newAllowed:union(selectors.tea,selectors.snack).filter(id=>!id.includes(':')),result:'茶坡风味册、茶香小聚成果页、桌布组件；搭配M05/M11，无售价倍率和新计时活动。',text:'一桌已经摆好，咸味和甜味各有一个位置。来的人不必赶着到，记在册里的香气也不会散。',next:'选一张喜欢的菜单保存；也可以翻去海湾，把咸香的一页接下去。',art:'M05茶点托盘加窄桌布、M11茶罐旁薄册；三份小点用已收录角色头像占位'},
 {id:'PJ-4',name:'四地风味展',gate:'海湾开放后显示，可提前追认前三区记录',stages:[{id:'PJ-4-A',check:'每区收录6种新品，并有至少2张标本或地点见闻（事件不算）',consume:null,costCP:400},{id:'PJ-4-B',check:'任选4种不同菜单，各有过一次有效接待',consume:{total:48,minimumSignatureCategories:4,minimumPerCategory:1,allowed:selectors.food},costCP:600},{id:'PJ-4-C',check:'前两项完成',consume:null,costCP:1000}],oldAllowed:foodKeys,newAllowed:species.map(s=>s.id),optionalDisplay:{allowed:everyone.map(s=>s.id),minimum:1,consume:false,rule:'任意已收录品种可用永久画像加入展册；若选择在家实物陈列则需自由库存1只且不消耗。病变、签鸡、节令、旧观赏同样可作为观察页；不计食用交付。'},result:'四地展折页插画、纪念章、三个既有陈列位的自选布局；组合M01–M12及地区插页，不新增独立纪念物或大型农场建筑。',text:'从菜畦到潮线，每一页都留了一点厨房认识过的味道。旧伙伴也在展册里占着位置，旁边写着：这一路，我们带着熟悉的东西去看新的。',next:'展册已齐，可以换个陈列样子，也可以安静做下一锅熟悉的。',art:'四张地区局部拼成折页；中心纪念章是覆盖层；12件纪念物重排，无全新大型场景'},
];
const collectionDefs=[
 ['COL-1','街坊早饭册','MN1','M01','0:0 0:3 0:8 1:0 1:3 1:6','home|meal','把最熟悉的几样写在一页，隔天再看，仍然知道从哪里开火。','先认一种基础家常，再从手头能做的料理里添两样。','V'],
 ['COL-2','纸包里的茶香','MN2','M02','0:10 1:39 1:40 1:41','tea|portable','茶香挨着饭香，纸包打开的时候，路也像近了一点。','已有茶叶蛋鸡也能代表茶味，再添一款方便拿的小点。','R'],
 ['COL-3','掀盖早市','MN3','M03','0:114 0:115 0:116','steam|home','揭开这一页，好像又听见笼盖轻轻碰了一声。','先从小笼包或烧麦认起，再选一份甜蒸点；没有甜的也能先开页。','V'],
 ['COL-4','溪边铺开的布','MN4','M04','0:19 0:43 1:9 1:7','portable|fresh','布角压住了纸，熟悉的味道和溪风一起坐了下来。','便携与清爽都可用旧出品，鸭蛋开放后再补鸡鸭同桌印记。','R'],
 ['COL-5','花叶间的小点','MN5','M05','0:10 0:18 1:41 1:57','tea|bake|steam','一片叶，一点花香，旁边留出咸味坐的位置。','先用茶叶蛋鸡和旧小点开页，桂花与焙香叶只是更多选择。','T'],
 ['COL-6','锅边多双筷','MN6','M06','0:21 1:12 0:42 1:34','home|ginger|mushroom|@stew','锅里响得慢，来过的人也愿意多坐一会儿。','普通炖鸡或炖鸭即可代表暖锅，姜香可以以后慢慢补。','T'],
 ['COL-7','沿湾轻食记','MN7','M07','0:19 1:7 0:16 1:10','savory|fresh|bay','盐花没有盖住原来的味道，只把海风留在了边角。','旧咸香和清爽能先开页，海湾标本辨认后再添地方印记。','B'],
 ['COL-8','四时同桌册','MN8','M08','0:120 0:121 1:57 1:58 0:122 0:123 1:59 1:60 0:124 0:125 1:61 1:62 0:126 0:127 1:63 1:64','@season','春夏秋冬不必等着来，一桌熟悉手作就能慢慢相逢。','任意两章的旧四时手作即可开始；地区新味可放客座栏，但不算四时章节。','B'],
];
const collections=collectionDefs.map(([id,name,menu,memento,fixed,selector,text,next,region])=>{
 const eligible=selector==='@season'?selectors.season:everyone.filter(s=>s.edible&&selector.split('|').some(t=>t==='@stew'?selectors.stew.includes(s.id):s.tags.includes(t))).map(s=>s.id);
 const displayGuests=species.filter(s=>!s.edible&&s.region===region).map(s=>s.id);
 return {id,kind:'theme',name,menu,region,oldKeys:eligible.filter(k=>k.includes(':')),newIds:eligible.filter(k=>!k.includes(':')),fixed:{kind:'any-one-of',allowed:words(fixed),note:'代表项任选1种，不要求全部；只检查永久发现，不锁库存。'},optional:{allowed:eligible,countStage1:id==='COL-8'?4:3,countStage2:id==='COL-8'?8:6,representativeIncludedInCount:true},stageRules:id==='COL-8'?'阶段1至少2个四时章节；阶段2至少3个章节。地区客座独立显示，不计4/8和菜单章节。':'阶段1含代表项且累计3种；阶段2含代表项且累计6种。',displayGuests:id==='COL-8'?species.map(s=>s.id):displayGuests,practice:{any:[`「${menus.find(m=>m.id===menu).name}」有效接待一次`,id==='COL-8'?'带两个不同四时章节的伙伴完成一次地区寻访':`记下${regions.find(r=>r.id===region).name}的任一事件后，带本页已收录的伙伴去那里寻访一次`],fullMenuStamp:`${menu}完整档成交覆盖必要角色、总成交≥6，另盖完整菜单印，无第二份物品。`},rewards:[{stage:1,id:`PAGE-${id}`,kind:'insert',text:'主题插页'},{stage:2,id:memento,kind:'memento',text:'小纪念物'},{stage:3,id:`STAMP-${id}`,kind:'stamp',text:'实践印记'}],text,next};
});
for(const r of regions)collections.push({id:`COL-${r.id}`,kind:'region',name:r.name+'地区册',region:r.id,oldKeys:r.oldKeys,newIds:species.filter(s=>s.region===r.id).map(s=>s.id),fixed:{kind:'all',allowed:[`${r.id}-S1`,`${r.id}-S2`]},optional:{allowed:species.filter(s=>s.region===r.id).map(s=>s.id),countStage1:3,countStage2:6,countComplete:12},stages:[{id:`COL-${r.id}-A`,requires:'辨认本区任一材料，收录本区任意3种，并收录过1种本地旧伙伴（卖空也算）',reward:`PAGE-${r.id}-draft`},{id:`COL-${r.id}-B`,requires:'辨认本区两种材料，收录本区任意6种，并记下本区任一张见闻',reward:`PAGE-${r.id}-full`},{id:`COL-${r.id}-C`,requires:'收齐本区12种，6张发现卡全部记下，两种材料都已辨认',reward:`BORDER-${r.id}`}],practice:{any:[`营业卖出或采购交出1只本区食用新品`,`带本区新品（观赏的也行）和旧伙伴同行，完成一次本区寻访`],reward:`STAMP-${r.id}`},text:['谷棚的纸包打开了，第一把新香气回到了厨房。','溪水走过石边，新味也在纸上留下了一道线。','叶背与花点各有一页，热锅和茶壶都能慢慢翻到。','盐畦离归路不远，几片方晶就够记住一阵海风。'][regions.indexOf(r)],next:regions.indexOf(r)<3?`可以继续补齐本地12种，也可查看${regions[regions.indexOf(r)+1].name}的条件，不用先全收齐。`:'四地区都各自成页；想筹风味展可以去生意簿，不想营业也能补齐品种。',rewards:'地区插页从草稿到完成态＋12种全收边饰；均为同一页升级，不另发CP或新纪念物。'});
const specials=[
 {id:'SP-ALL',name:'每一种都有自己的脚步',theme:'已有全部193种和新增48种的同行观察页；也是特殊、病变、签鸡与观赏伙伴的实际用途入口。',oldKeys:baseline.species.map(s=>s.key),newIds:species.map(s=>s.id),fixed:[],optional:'任意3种实收开页，任意6种实收得观察页边饰；不要求凑193/241。',practice:'带任意伙伴完成一次地区寻访；每种伙伴第一次同行，会点亮它的脚步格。',reward:'PAGE-SP-ALL观察插页与每种脚步格，无CP、无稀有奖励',text:'有的走得快，有的总慢半步。把它们带出去，才知道每一种都能给路上添一点不同。',next:'从在家多于1只的伙伴里挑一个出发；不必为填格故意制造病变。'},
 {id:'SP-LEAF',name:'一片叶子的四种模样',theme:'叶形、茶香与材料标本对照',oldKeys:union(words(oldTagGroups.leaf),words(oldTraits.tea)),newIds:species.filter(s=>s.exploration.traits.some(t=>['leaf','tea'].includes(t))).map(s=>s.id),fixed:['任一叶类标本：75/78/79/82'],optional:'候选品种任意2种开页，4种得完整观察插页；旧新不限。',practice:'记下「把叶子放平」「柚香绕过石头」「两种很像的叶子」或「草梗替篮角留空」，并带叶形或茶香伙伴同行。',reward:'PAGE-SP-LEAF叶脉透明覆页＋实践印，无额外纪念物',text:'切碎、卷起、压平，叶子的样子变了，熟悉的香气仍能找到路。',next:'茶叶蛋鸡可以去焙叶棚；普通香草烤鸡加鸭宝也能去看海蓬菜。'},
 {id:'SP-TABLE',name:'鸡鸭同桌',theme:'两类出品的独立风味',oldKeys:foodKeys,newIds:species.filter(s=>s.edible).map(s=>s.id),fixed:['食用鸡3种','食用鸭3种'],optional:'鸡鸭各1种开页，各3种完成；不限旧新，不指定稀有。',practice:'任意完整菜单实际成交同时含鸡鸭，每类至少1只、总数≥6。',reward:'PAGE-SP-TABLE双餐垫纸页与同桌印，无第13件纪念物',text:'宽嘴与尖嘴都到齐，纸包里才有两种不同的热闹。',next:'家常小铺摆上鸡宝和鸭宝各6只，就是一份完整菜单。'},
 {id:'SP-SHAPE',name:'看起来不像鸡鸭',theme:'观赏形态与旧特殊内容对照',oldKeys:baseline.species.filter(s=>!s.edible).map(s=>s.key),newIds:species.filter(s=>!s.edible).map(s=>s.id),fixed:[],optional:'候选任意3种开页，任意8种完成；八个新观赏保证有全年无稀有随机解，旧河童、雪人鸡、鸭嘴兽等可替代。',practice:'完成一次「花叶陈列」，或带本页任一伙伴记下一张见闻；都不用交出伙伴。',reward:'PAGE-SP-SHAPE剪影翻页与观察印；旧签鸡回礼仍在原处单独领取',text:'一眼像叶子，再看像贝壳，最后才看见小脚在底下忙。熟悉的伙伴总能换个样子打招呼。',next:'从谷棚芒影、浅滩水线等见闻追寻观赏方向，不用等节日或神社抽中某张签。'},
];
const mementos=rows(`
M01|家常账页夹|COL-1|木夹夹住折起的账页，缺一角的旧纸作轮廓|家常的几样，夹在最容易翻到的那一页。
M02|茶香纸包签|COL-2|双折纸包配一片细茶叶压纹，签绳短短|纸包拆过了，茶香的位置还记得。
M03|小竹笼垫|COL-3|扁圆竹编垫，右上缺一小格，留蒸气印而非厨具|不必总装满一笼，留一格也很热闹。
M04|溪风纸篮套|COL-4|折纸篮套、斜压布角、开口留白|空出来的那一角，刚好装一阵溪风。
M05|双味点心托|COL-5|长椭圆托盘两小凹位，叶印与细花印分列|咸甜各坐一边，中间留给茶香。
M06|锅边筷枕|COL-6|短胖木筷枕两道凹槽，配一张暖锅纸签|多留一个位置，总会有熟悉的香气过来。
M07|盐田折页夹|COL-7|方盐晶形纸夹压住灰绿折页，无水晶宝石高光|这一点咸香，从离家不远的水边来。
M08|四时纸转页|COL-8|四片错开的纸页围一个小铆钉，四种季节纹样|翻到哪一页，今天都可以做。
M09|常坐的那一桌|RG1|两次折痕的便签靠在小木片上，桌角图案无人物|这张纸认得现在厨房的味道。
M10|折叠野餐布|RG2|青灰方格布折四层，一角微翘，细线不糊成噪点|折小一点，下一次还装得进篮子。
M11|小茶罐|RG3|矮圆陶罐与铜褐纸封、细花点；不画昂贵礼盒|盖子合好，留一点香气给下次见面。
M12|贝壳货签|RG4|哑白贝壳形木签，两条潮纹，粗短绳结|绳结不用系死，下回还能慢慢续上。
`).map(([id,name,source,art,text])=>({id,name,source,art,text,once:true,displaySlots:3,effect:'纯陈列，无数值、耐久、出售、重复兑换。',unlock:source.startsWith('COL')?'对应主题收录阶段2自动登记；实践不是领取前置。':'对应常客第4段达成自动登记。',next:'去农场选一个陈列位；替换时旧物仍留在册里。'}));
// Complete bidirectional links are derived only from explicit authored eligibility lists.
for(const s of species){
 s.collections=collections.filter(c=>c.newIds.includes(s.id)||(c.displayGuests??[]).includes(s.id)).map(c=>c.id);
 s.specials=specials.filter(c=>c.newIds.includes(s.id)).map(c=>c.id);
 s.orders=orders.filter(o=>o.groups.some(g=>g.allowed.includes(s.id))).map(o=>o.id);
 s.regulars=s.edible?[...new Set(s.orders.map(id=>orders.find(o=>o.id===id).regular))]:s.region==='B'?['RG2','RG4']:s.region==='T'?['RG2','RG3']:['RG2'];
 s.projects=projects.filter(p=>p.newAllowed.includes(s.id)).map(p=>p.id);
 s.displayMenus=s.edible?[]:[{V:'MN3',R:'MN4',T:'MN5',B:'MN7'}[s.region]];
 s.relatedCards=union(s.relatedCards,materials.filter(m=>s.recipe.ingredients.some(i=>i.id===m.id)).map(m=>m.specimen),cards.filter(c=>c.region===s.region&&c.type==='event'&&(!c.team.trait||s.exploration.traits.includes(c.team.trait))).map(c=>c.id));
 s.uses=s.edible?[{system:'business',target:s.orders.includes('O12')?'O12':s.orders[0],action:'作为允许的替代实际交付，扣库存并获得基础货款；组内混交，不必凑满本品。'},{system:'exploration',target:'SP-ALL',action:`作为G${s.exploration.G}/F${s.exploration.F}、${s.exploration.environment}适应队员参与整趟；特征可帮助相应事件。`},{system:'project',target:s.projects.includes('PJ-4')?'PJ-4':s.projects[0],action:'实收事实用于地区展出门槛；实际交付另消耗自由库存。'}]:[{system:'exploration',target:'SP-ALL',action:`提供G${s.exploration.G}/F${s.exploration.F}和${s.exploration.environment}适应，${s.exploration.traits.map(t=>traits[t]).join('、')}可解已提示事件。`},{system:'display',target:'O04',action:'作为有趣轮廓展示候选，在家留1只供看，不消耗；可推进一次性明信片。'},{system:'project',target:'PJ-4',action:'计入本区6种实收门槛，可进入展册观赏栏；不计食用筹备48只。'}];
 s.businessExclusion=s.edible?null:'菜单角色与食用采购均不合格；displayMenus仅指相邻陈列主题，不参与菜单评分、货款或加价。';
 s.regularRelation=s.edible?'以实际合格采购/菜单参与所列常客分支，具体条件见段落；不是每次出现就自动推进。':'RG2-4可用O04展示明信片作布样参考；茶坡观赏可同行T-N1参与RG3-2，海湾观赏可同行B-N1参与RG4-2。均不交付本品食用，也不是必需品种。';
 s.unlock.minimumKitchen=s.recipe.kitchenLevel;
 s.unlock.technologyNote=s.recipe.toolId===8?'蒸笼Lv.1须厨房Lv.2且发现12种；Lv.2须厨房Lv.3，Lv.3须厨房Lv.4。本包不新增鸭蛋蒸笼。':s.recipe.toolId===7?'面包机须厨房Lv.4且烧水壶Lv.3；奶油供货另核。':s.recipe.toolId===6?'烧水壶须厨房Lv.4；牛乳须壶Lv.2，焦糖须壶Lv.3；不能仅按配方所需壶级判断全部条件。':'按既有厨具购买顺序及升级条件；材料须逐项满足现行供货资格。';
 s.namingDecision=s.workName===s.name?'保留工作名；按新轮廓与精确配方区分旧品。':`工作名“${s.workName}”调整为“${s.name}”，把形状和做法写清，区别见美术对照。`;
}
for(const s of species){
 s.relatedCards=union(s.relatedCards,cards.filter(d=>d.team.bonusTrait&&s.exploration.traits.includes(d.team.bonusTrait)).map(d=>d.id));
 if(s.edible){const preferences={V:['O01','O03','O02','O08','O06'],R:['O05','O07','O08','O06'],T:['O07','O08','O09','O06'],B:['O11','O06','O08']};
  const order=preferences[s.region].find(id=>s.orders.includes(id));
  s.uses[0]={system:'business',target:order,action:`作为${orders.find(o=>o.id===order).name}的允许替代实际交付，组内混交并获得基础货款；仍须完成其他需求组才得酬谢。`};
 }
}
for(const b of baseline.species){b.menus=menus.filter(m=>m.roles.some(r=>r.allowed.includes(b.key))).map(m=>m.id);b.orders=orders.filter(o=>o.groups.some(g=>g.allowed.includes(b.key))).map(o=>o.id);b.collections=collections.filter(c=>c.oldKeys.includes(b.key)).map(c=>c.id);b.specials=specials.filter(c=>c.oldKeys.includes(b.key)).map(c=>c.id);}
for(const c of cards.filter(c=>c.type==='event')){c.effects=c.effects.filter(e=>e.target!=='SP-LEAF');c.effects.push({kind:'practice',target:`COL-${{V:3,R:4,T:5,B:7}[c.region]}`});if(['V-E1','R-E1','T-E1','B-E2'].includes(c.id))c.effects.push({kind:'practice',target:'SP-LEAF'});}
const supplemental={
 decisions:[
  {issue:'鸡鸭蒸笼支持冲突',resolution:'荠菜汤包鸭改荠叶粉结鸭（水煮锅），海蓬汤包鸭改海蓬豆窝鸭（炖锅）；48种不减，竹蒸笼维持鸡蛋专用。'},
  {issue:'水芹蒸饺鸡与水晶饺鸡轮廓重复',resolution:'改为芹梗方饺鸡，四角方枕；厨具改Lv.2匹配薯粉常驻供货，厨房实际最低Lv.3。'},
  {issue:'焙茶锅煮鸡与茶叶蛋鸡同为褐蛋壳帽',resolution:'新款改翻叶肩衣、梨形蛋身；旧茶叶蛋地方替代增加盐，避免与新品单焙叶方完全相同。'},
  {issue:'四组米糕、奶冻、脆饼互相换味',resolution:'切糕/梯层糕、奶砖/奶环、叠脆/折扇/口袋/六角/十字分别冻结大形；对应厨具、味型、配料与用途差异见逐种表。'},
  {issue:'牛乳、焦糖、黑芝麻过早承诺',resolution:'V-D4/T-C4/T-D1/B-D3与芝麻两款为厨房Lv.4回访内容；不提前开启旧供货，不作为标本、路线、普通菜单唯一解。'},
  {issue:'海湾“柚香脆卷”使地方旧料位又依赖溪岸',resolution:'改橙皮风帆鸭，使用已有柳橙＋墨西哥薄饼；不隐增第9材料，也不让海湾必须先辨认山柚。'},
  {issue:'四时主题不能拿地区新品冒充四时',resolution:'MN8只用原16种；COL-8新增客座栏引用新品但不计章节和收录4/8目标。'},
  {issue:'全193旧品用途不能等同全193可食',resolution:'显式白名单；全部旧品参与SP-ALL同行记录及PJ-4永久画像展出；非食用品仍能用G/F和环境寻访，旧出售规则保留。'},
  {issue:'姜蜜软糕鸭与姜饼、姜糖茶撞味',resolution:'改姜蜜软糖鸭：慢炖凝润水滴糖块，区别烤饼和茶饮；不含梨，不给果香寻访特征。'},
 ],
 quail:{status:'仅保留实验输入，不属于本包',inputs:['48鸡鸭完成后才可评估；不可抢占ID、数量或正式图鉴格。','另案提供一批两味的小量决策、单批24位置约束与鸡鸭小份模式对照。','先证明独立玩法和轮廓价值，再决定试做4种与存档扩展；本包不定义鹌鹑配方、角色、美术排期。']},
 productionRules:{sourceOfTruth:'authoring.mjs是本内容包作者源；JSON与Markdown由其输出，修改后先重导出再运行validate.mjs；这些文件不被游戏导入。',knowledge:'作者可见完整姓名与配方。玩家未实收前仅显示编号、蛋种、剪影、已知条件；完整方法解锁也不显示真名或正彩图。',method:'首次带回后自动识别并展示相关配方线索；旧料新品在首标本登记后出现。观察/研读可补方法，追踪目标后，每次本地区完整寻访补全下一层线索，无券、不溢出换CP。观赏还需对应见闻。',trial:'严格按蛋种、精确材料集合（每味1份/24枚整批）和最低厨具/厨房检查；不得多放第三味仍算精确配方。新开锅逐枚独立抽取，按配方档位30%/20%/10%；首见和复做一致，不设累计锅数保底，不固定插入。未命中新品的蛋走旧兼容池，必须按整锅期望分布估算。旧版本已开锅的冻结结果不改写。',companions:'新地区材料对旧规则不作为新的匹配条件；旧材料仍能命中旧普通候选。竹蒸笼地区模式使用旧权重候选但禁用旧多目标保证插入；没有新区目标时仍抽24枚旧池。四时、神社礼物、地区试做、地方替代准备模式互斥。68/69/70禁止作为本包配料。',prices:'材料价格15/20/25 CP为建议；新品基础售价不在本轮伪定为已平衡。后续平衡必须验证整批伴随分布、开火、买料、收取、销售、维护与技能；内容实现可先用非发布测试价格，发布不能跳过平衡门禁。',timing:'一店一队、营业24h上限、2h/6只窗口、12只来客进度、1–3不同品种、首标本+4趟发现保护继承玩法文档。发现卡保底与旧线索6/8趟独立。',unlock:'本包额外条件均以事实检查，花费与检查分开。路线GUIDE-B确定取得、不计发现卡；旧材料资格保留，标本入池先核入门配方的非新材料条件。',ui:'固定厨房/农场/生意/寻访/图鉴五导航，生意4标签，图鉴4标签。食材见闻是见闻子页，特殊发现不加主标签。纪念物/新插页自动入册；旧回礼保留原领取。'},
};
supplemental.decisions.push(
 {issue:'河水旧供货依赖病变发现',resolution:'水纹叶鸭改水芹＋盐，桂露穗羽鸭改桂花＋食盐土，潮纹贝鸭改盐花＋盐；形态仍由相应地点见闻解释，不要求故意弄脏厨房或随机得到病变。旧河水配方不变。'},
 {issue:'黑胡椒供货晚于水煮锅Lv.2',resolution:'椒香饭团鸭最低厨房改Lv.3，另需黑胡椒真实供货（如油炸锅/烤箱Lv.3）；不把厨具配方等级误当全量开放条件。'},
 {issue:'1新品+23伴随导致部分料理亏损',resolution:'麦芽奶砖、桂花奶环由水煮锅改炖锅Lv.2；焙叶双层茶加红茶叶，保留旧奶茶兼容候选；胡椒锅巴去酱油。新增基础价逐种给接入初值；复刻批毛差审查通过不等于长期净利润通过。'},
);
// Indicative prices for implementation fixtures. These are not a release balance verdict.
const proposedPrices={V:[14,23,22,28,26,12,18,23,23,30,24,12],R:[25,24,26,25,24,14,26,22,25,23,42,14],T:[18,25,24,30,25,15,30,27,25,27,25,15],B:[28,25,29,26,27,16,25,26,31,25,26,16]};
for(const r of regions)species.filter(s=>s.region===r.id).forEach((s,i)=>{s.priceProposal={baseSaleCP:proposedPrices[r.id][i],collectCP:1,status:'待整批经济和维护成本验证的接入初值',ornamentalCostAllowed:!s.edible};});
supplemental.productionRules.prices='材料15/20/25 CP、新品逐种baseSaleCP均为可接入的建议初值，非已通过发布平衡。后续须检验23枚伴随产物、首次失败批、开火、买料、收取、交易、维护和技能；不能使用24×新品价。';
// All expensive old ingredients keep their exact existing supply gates; the effective stage is explicit.
for(const s of species)s.recipe.oldSupply=baseline.ingredients.filter(i=>s.recipe.ingredients.some(x=>x.id===i.id)).map(i=>({id:i.id,name:i.name,anyOf:i.unlockAlternatives}));
const artManifest={style:'原作小厨房风味生物；棕色粗轮廓、少量暖面高光，食物结构先于饰品；不采用现代餐厅或宝石养成图标。',sizes:{speciesMaster:'512×512透明PNG，另交分层源稿；脚底与视觉中心单独记裁切元数据',portrait:'256×256透明PNG，安全边12%，48/64/96像素复核',silhouette:'同尺寸纯色实心外轮廓保留必要负空间，未知状态不露细纹',material:'128×128透明PNG＋256×192标本局部',card:'480×270无字插画局部，关键物件置中60%；320宽仍可读',memento:'256×256透明PNG＋48像素简化图；项目变体复用底图',region:'960×540无字地区局部，拆两地点；不生成全屏新厨房'},species:species.map(s=>({id:`ART-${s.id}`,content:s.id,deliverables:['full','portrait','silhouette'],...s.art})),materials:materials.map(m=>({id:`ART-MAT-${m.id}`,content:m.id,brief:m.art,deliverables:['ingredient-icon','specimen-cutout','shop-bundle']})),cards:cards.map(c=>({id:`ART-${c.id}`,content:c.id,brief:c.art,reuse:c.type==='specimen'?`ART-MAT-${c.material}标本近景`:`ART-REG-${c.region}地点底图`,deliverables:['discovery-vignette']})),mementos:mementos.map(m=>({id:`ART-${m.id}`,content:m.id,brief:m.art,deliverables:['display','small-icon']})),regions:regions.map(r=>({id:`ART-REG-${r.id}`,content:r.id,brief:r.visual,places:r.places,deliverables:['regional-insert','two-place-crops']})),projects:projects.map(p=>({id:`ART-${p.id}`,content:p.id,brief:p.art,deliverables:['overlay-components','result-page']})),menus:menus.map(m=>({id:`ART-${m.id}`,content:m.id,brief:`${m.name}纸菜单题签，复用对应M${m.id.slice(2).padStart(2,'0')}主题物件，无文字烘焙，无新增角色绘制`,deliverables:['menu-object-crop']})),specials:specials.map(s=>({id:`ART-${s.id}`,content:s.id,brief:'复用已收录角色头像、材料标本、脚印/叶脉/餐垫/剪影覆页；不为每格画新插画',deliverables:['page-overlay']})),qa:['先做每区鸡鸭各1张线稿，共8张，连同各区2个材料和一个地点局部一起评轮廓；这只是后续制作顺序，本轮未生成。','48个剪影去色并排；相似组必须仍能分清：所有饼、米团、奶凝、茶饮、叶形观赏。','同菜系不得只换鸡嘴/鸭嘴；逐项对照art.contrast中的旧品与近邻。','精确配方没用的材料不作为主视觉：盐花不是雪晶，桂花不是菊花，橙皮风帆不是山柚，姜蜜软糖没有水果原料。','主图与头像保留鸡/鸭嘴型；头饰、饼边、长梗、晶角不可裁掉；无姓名、编号、UI标签烘入素材。','没有最终角色图时用明确标注概念剪影，不能把旧图当新品；概念图不作为风格已批准证据。'],totals:{newSpeciesMaster:48,derivedPortrait:48,derivedSilhouette:48,materialSets:8,discoveryVignettes:24,standaloneMementos:12,regionInserts:4,placeCrops:8,projectComponentSets:4,menuCrops:8,specialOverlays:4,fullNewKitchenScenes:0}};
const manifest={version:1,status:'authored-not-implemented',date:'2026-09-23',counts:{oldSpecies:193,newSpecies:48,chicken:24,duck:24,total:241,materials:8,cards:24,themeCollections:8,regionCollections:4,menus:8,orders:12,regulars:4,regularStages:16,projects:4,mementos:12},tags,traits,regions,species,materials,alternatives,cards,guide,menus,selectors,orders,regulars,projects,collections,specials,mementos,...supplemental};
manifest.paperRecords=[...orders.map(o=>({id:o.firstResult.id,name:o.firstResult.name,source:o.id,text:o.finish,art:'共用纸包便签/采购小票，文字独立层，O04用无字明信片底。',once:true})),...regulars.flatMap(r=>r.stages.filter(s=>s.reward.startsWith('NOTE-')).map(s=>({id:s.reward,name:s.title+'便签',source:s.id,text:s.text,art:'共用常客折页纸形，配该常客纪念物简化图，不新作立绘。',once:true})))];
save('content.json',manifest);save('baseline.json',baseline);save('art-manifest.json',artManifest);
const nodes=[],edges=[];
const node=(id,kind,name)=>nodes.push({id,kind,name});const edge=(from,to,kind)=>edges.push({from,to,kind});
for(const r of regions)node(r.id,'region',r.name);
for(const s of baseline.species)node(s.key,'old-species',s.name);
for(const m of [...baseline.ingredients,...materials])node('MAT-'+m.id,'material',m.name);
for(const x of [...cards,...menus,...orders,...regulars,...regulars.flatMap(r=>r.stages),...projects,...collections,...specials,...mementos,...manifest.paperRecords,...alternatives])node(x.id,'content',x.name??x.title);
for(const s of species){node(s.id,'new-species',s.name);node(s.recipe.id,'recipe',s.name+'配方');edge(s.id,s.recipe.id,'made-by');edge(s.id,s.region,'belongs-to');for(const i of s.recipe.ingredients)edge(s.recipe.id,'MAT-'+i.id,'consumes');for(const k of ['menus','orders','regulars','projects','collections','specials','relatedCards'])for(const id of s[k])edge(s.id,id,k);}
for(const m of materials){edge('MAT-'+m.id,m.region,'source');edge('MAT-'+m.id,m.specimen,'identified-by');for(const id of m.oldKeys)edge('MAT-'+m.id,id,'old-comparison');}
for(const d of cards){edge(d.id,d.region,'found-at');for(const id of d.team.oldExamples)edge(id,d.id,'ordinary-team-witness');for(const e of d.effects)if(e.target)edge(d.id,e.target,e.kind);}
for(const m of menus){for(const id of m.orders)edge(m.id,id,'request');for(const id of m.regulars)edge(m.id,id,'regular');for(const id of m.projects)edge(m.id,id,'practice');}
for(const o of orders){edge(o.id,o.regular,'story');edge(o.id,o.project,'project-record');edge(o.id,o.firstResult.id,'first-result');}
for(const r of regulars){for(const st of r.stages){edge(r.id,st.id,'stage');edge(st.id,st.reward,'result');}edge(r.id,r.memento,'memento');}
for(const m of mementos)edge(m.source,m.id,'reward');
for(const s of baseline.species){for(const k of ['menus','orders','collections','specials'])for(const id of s[k])edge(s.key,id,k);edge(s.key,'PJ-4','optional-display');}
save('relation-graph.json',{nodes,edges,semantics:'关系图用于反查和审计；edges不是全部AND解锁条件。精确门槛以对应对象字段为准，主获取AND/OR图在audit.json。'});
// A reviewable relation table, including the two non-food paths of ornamental species.
const header=['工作ID','稳定身份','名称','旧工作名','地区','鸡鸭','配方','厨具','厨房','材料','菜单','主题/地区收藏','发现卡','采购','常客','项目','食用','G','F','环境','特征'];
const csvEscape=s=>'"'+String(s??'').replaceAll('"','""')+'"';
const lines=species.map(s=>[s.id,s.key,s.name,s.workName,s.region,s.egg?'鸭':'鸡',s.recipe.id,`${s.recipe.toolId}:Lv.${s.recipe.toolLevel}`,s.recipe.kitchenLevel,s.recipe.ingredients.map(i=>i.id).join(';'),s.menus.join(';'),s.collections.join(';'),s.relatedCards.join(';'),s.orders.join(';'),s.regulars.join(';'),s.projects.join(';'),s.edible?'是':'否',s.exploration.G,s.exploration.F,s.exploration.environment,s.exploration.traits.join(';')]);
fs.writeFileSync(path.join(root,'relations.csv'),'\uFEFF'+[header,...lines].map(a=>a.map(csvEscape).join(',')).join('\n')+'\n');
console.log(`Authored ${species.length} species; ${cards.length} cards; ${regulars.reduce((n,r)=>n+r.stages.length,0)} story stages.`);
