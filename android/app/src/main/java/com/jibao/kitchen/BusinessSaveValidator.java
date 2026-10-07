package com.jibao.kitchen;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.*;

/** Read-only mirror of business-save.js and orders.js. Frozen content is generated
 * from the runtime registry; validation never repairs, advances time or pays out. */
final class BusinessSaveValidator {
    private static final long MAX=9007199254740991L, WINDOW=7200000L, DAY=86400000L;
    private static final JSONObject DATA=RuntimeContent.DATA;
    private static final String[] MONEY={"baseCP","markupCP","themeCP"};
    private static void require(boolean condition,String label)throws IOException {if(!condition)throw new IOException("经营存档无效: "+label);}
    private static Set<String> keys(JSONObject o){Set<String> out=new LinkedHashSet<>();for(Iterator<String> i=o.keys();i.hasNext();)out.add(i.next());return out;}
    private static JSONObject object(Object value,String... fields)throws Exception {
        require(value instanceof JSONObject,"对象");JSONObject o=(JSONObject)value;
        if(fields.length>0)require(keys(o).equals(new HashSet<>(Arrays.asList(fields))),"字段集合 "+Arrays.toString(fields));return o;
    }
    private static JSONArray array(Object value,int max)throws Exception {require(value instanceof JSONArray,"列表");JSONArray a=(JSONArray)value;require(a.length()<=max,"列表上限");return a;}
    private static String string(Object value)throws Exception {require(value instanceof String,"字符串");return (String)value;}
    private static long integer(Object value,long min,long max)throws Exception {
        require(value instanceof Number,"整数类型");double n=((Number)value).doubleValue();
        require(Double.isFinite(n)&&n==Math.rint(n)&&n>=min&&n<=max&&n<=MAX,"整数范围");return ((Number)value).longValue();
    }
    private static long n(JSONObject o,String k)throws Exception{return integer(o.get(k),0,MAX);}
    private static long n(JSONObject o,String k,long min,long max)throws Exception{return integer(o.get(k),min,max);}
    private static boolean bool(Object v)throws Exception {require(v instanceof Boolean,"布尔类型");return (Boolean)v;}
    private static boolean has(JSONArray a,Object value)throws Exception {for(int i=0;i<a.length();i++)if(Objects.equals(a.get(i),value))return true;return false;}
    private static Set<String> strings(JSONArray a)throws Exception {Set<String> out=new LinkedHashSet<>();for(int i=0;i<a.length();i++)require(out.add(string(a.get(i))),"重复身份");return out;}
    private static void choice(Object v,String... values)throws Exception {require(Arrays.asList(values).contains(v),"枚举");}
    private static JSONObject find(JSONArray a,String id)throws Exception {for(int i=0;i<a.length();i++){JSONObject o=a.getJSONObject(i);if(id.equals(o.getString("id")))return o;}return null;}
    private static JSONObject definition(String kind,Object id)throws Exception {JSONObject o=find(DATA.getJSONArray(kind),string(id));require(o!=null,kind+"身份");return o;}
    private static JSONObject species(String key)throws Exception {JSONObject c=DATA.getJSONObject("species").optJSONObject(key);require(c!=null,"品种身份");return c;}
    private static void equal(Object a,Object b,String label)throws Exception {
        if(a instanceof JSONObject&&b instanceof JSONObject){JSONObject x=(JSONObject)a,y=(JSONObject)b;require(keys(x).equals(keys(y)),label);for(String k:keys(x))equal(x.get(k),y.get(k),label);}
        else if(a instanceof JSONArray&&b instanceof JSONArray){JSONArray x=(JSONArray)a,y=(JSONArray)b;require(x.length()==y.length(),label);for(int i=0;i<x.length();i++)equal(x.get(i),y.get(i),label);}
        else if(a instanceof Number&&b instanceof Number)require(((Number)a).doubleValue()==((Number)b).doubleValue(),label);
        else require(Objects.equals(a,b),label);
    }
    private static long sum(JSONObject map)throws Exception {long out=0;for(String k:keys(map))out+=n(map,k);return out;}
    private static long value(JSONObject o,String k)throws Exception{return o.has(k)?n(o,k):0;}
    private static void add(JSONObject o,String k,long v)throws Exception{o.put(k,value(o,k)+v);}
    private static void mapEqual(JSONObject a,JSONObject b)throws Exception {Set<String> all=keys(a);all.addAll(keys(b));for(String k:all)require(value(a,k)==value(b,k),"数量守恒 "+k);}
    private static JSONObject stock(Object v,Set<String> allowed,boolean positive)throws Exception {
        JSONObject o=object(v);require(o.length()<=6,"备货品种数");
        for(String k:keys(o)){require(species(k).getBoolean("edible")&&(allowed==null||allowed.contains(k)),"备货身份");n(o,k,positive?1:0,72);}return o;
    }
    private static long identity(Object v,String prefix,long max)throws Exception {
        String id=string(v);require(id.matches(prefix+"-[1-9][0-9]*"),"实例身份");
        long seq;try{seq=Long.parseLong(id.substring(prefix.length()+1));}catch(NumberFormatException e){throw new IOException("实例序号溢出");}
        require(seq<=max,"实例序号");return seq;
    }
    private static void sourceId(Object v,String prefix)throws Exception {require(string(v).matches(prefix+"-[1-9][0-9]*"),"来源身份");}

    static void validate(JSONObject s)throws Exception {
        JSONObject e=s.getJSONObject("expansion"),b=object(e.get("business"),"sequence","active","lastReport","visitorProgress","visitorSequence","themeRemainder");
        n(b,"sequence");n(b,"visitorSequence");n(b,"visitorProgress",0,11);n(b,"themeRemainder",0,99);
        orders(s);
        JSONObject policy=(JSONObject)e.get("inventoryPolicy");boolean hasLocks=policy.has("locks");
        JSONObject p=hasLocks?object(policy,"keepOne","collectionLocks","optionalOrderReservations","locks"):object(policy,"keepOne","collectionLocks","optionalOrderReservations");bool(p.get("keepOne"));
        if(hasLocks){JSONObject locks=object(p.get("locks"));for(String k:keys(locks)){species(k);n(locks,k,0,99999);}}
        for(String k:strings(array(p.get("collectionLocks"),241)))species(k);
        require(object(p.get("optionalOrderReservations")).length()==0,"自选预留容器");
        if(!b.isNull("active"))active(s,b);
        if(!b.isNull("lastReport"))report(b);
        facts(s);
    }
    private static JSONObject core(JSONObject r,JSONObject b,boolean active)throws Exception {
        JSONObject menu=definition("menus",r.get("menuId"));n(r,"rulesVersion",1,2);identity(r.get("id"),"business",n(b,"sequence"));
        n(r,"startAt");n(r,"totalSold",0,72);n(r,"visitorEvents",0,6);
        for(String k:new String[]{"baseCP","markupCP","themeCP","bonusCP"})n(r,k,0,10000000);
        JSONObject initial=stock(r.get("initialStock"),null,true);require(initial.length()>0&&sum(initial)<=72,"初备容量");
        JSONObject sold=stock(r.get("soldByKey"),keys(initial),false);require(sum(sold)==n(r,"totalSold"),"成交总数");
        require(bool(r.get("bonusSettled"))!=active,"奖励结清状态");return menu;
    }
    private static void active(JSONObject s,JSONObject b)throws Exception {
        JSONObject a=object(b.get("active"),"menuId","roles","stock","initialStock","snapshot","prices","rewards","creditReserve","useRewards","tendency","capacity","id","rulesVersion","startAt","hardEndAt","processedWindow","roleCursor","soldByKey","roleSales","fullSoldByKey","fullRoleSales","baseCP","markupCP","themeCP","bonusCP","totalSold","windowReports","recordedValid","recordedComplete","bonusSettled","pendingCloseAt","visitorCandidates","visitorEvents");
        JSONObject menu=core(a,b,true);require(identity(a.get("id"),"business",MAX)==n(b,"sequence"),"当前营业序号");
        long start=n(a,"startAt"),end=n(a,"hardEndAt"),processed=n(a,"processedWindow",0,12);
        require(end==start+DAY,"24小时边界");JSONArray rows=array(a.get("windowReports"),12);require(rows.length()==processed,"窗口游标");
        int kitchen=s.getInt("kitchenLevel");long capacity=n(a,"capacity");require((capacity==24||capacity==48||capacity==72)&&capacity<=(kitchen==0?24:kitchen==1?48:72),"厨房容量");
        JSONObject initial=a.getJSONObject("initialStock"),stock=stock(a.get("stock"),keys(initial),false),prices=object(a.get("prices"));Set<String> initialKeys=keys(initial);
        require(sum(initial)<=capacity&&keys(stock).equals(initialKeys)&&keys(prices).equals(initialKeys),"备货与价格集合");
        for(String k:keys(prices)){JSONObject p=object(prices.get(k),"baseCP","markupPercent");n(p,"baseCP",1,100000);long rate=n(p,"markupPercent");require(rate==0||rate==12||rate==18,"招牌百分比");}
        JSONArray roleDefs=menu.getJSONArray("roles"),roles=array(a.get("roles"),roleDefs.length()+1);Set<String> roleIds=new HashSet<>(),assigned=new HashSet<>();
        for(int i=0;i<roles.length();i++){
            JSONObject r=object(roles.get(i),"roleId","keys");String id=string(r.get("roleId"));require(roleIds.add(id),"重复角色");JSONObject def=find(roleDefs,id);
            require(def!=null||id.equals("ordinary"),"角色身份");JSONArray ks=array(r.get("keys"),def==null?6:def.getInt("maxSpecies"));
            for(int j=0;j<ks.length();j++){String key=string(ks.get(j));require(initialKeys.contains(key)&&assigned.add(key)&&(def==null||has(def.getJSONArray("allowed"),key)),"角色分配");}
        }
        for(int i=0;i<roleDefs.length();i++)require(roleIds.contains(roleDefs.getJSONObject(i).getString("id")),"角色覆盖");
        require(assigned.equals(initialKeys),"出品覆盖");n(a,"roleCursor",0,roles.length()-1);
        JSONObject snap=object(a.get("snapshot"),"menuId","roles","identified","species");equal(snap.get("menuId"),a.get("menuId"),"快照菜单");equal(snap.get("roles"),roles,"快照角色");
        for(String id:strings(array(snap.get("identified"),8)))require(id.matches("7[5-9]|8[0-2]"),"辨认快照");
        JSONObject semantic=object(snap.get("species"));require(keys(semantic).equals(initialKeys),"快照品种覆盖");
        for(String k:initialKeys){JSONObject c=object(semantic.get(k),"egg","region","tags","season","regionalMaterials"),source=species(k);for(String field:keys(c))equal(c.get(field),source.get(field),"品种语义快照");}
        JSONObject reward=object(a.get("rewards"),"basket","platter","basketBonus","platterBonus","basketEligible","platterEligible");bool(reward.get("basket"));bool(reward.get("platter"));
        long basket=n(reward,"basketBonus"),platter=n(reward,"platterBonus");require(basket==12&&platter==8||basket==18&&platter==12,"奖励专精快照");
        equal(reward.get("basketEligible"),DATA.get("basketEligible"),"整筐白名单");Set<String> platterKeys=strings(array(reward.get("platterEligible"),6)),expected=new HashSet<>();
        for(String k:initialKeys)if(species(k).getBoolean("platter"))expected.add(k);require(platterKeys.equals(expected),"拼盘白名单");
        long reserve=n(a,"creditReserve",0,6);boolean use=bool(a.get("useRewards"));require(reserve<=s.getJSONObject("progress").getJSONObject("trade").getLong("credits")&&(use||reserve==0),"经营额度");choice(a.get("tendency"),"regulars","discovery");
        require(n(a,"bonusCP")==0,"提前奖励");bool(a.get("recordedValid"));bool(a.get("recordedComplete"));
        for(String id:strings(array(a.get("visitorCandidates"),4)))definition("regulars",id);
        WindowTotals calc=windows(rows,initial,menu,roles,snap,prices,n(a,"rulesVersion"));
        require(calc.cursor==n(a,"roleCursor"),"角色游标");
        for(String field:new String[]{"soldByKey","roleSales","fullSoldByKey","fullRoleSales"}){
            JSONObject counts=object(a.get(field));Set<String> allowed=field.equals("roleSales")||field.equals("fullRoleSales")?roleIds:initialKeys;
            for(String k:keys(counts)){require(allowed.contains(k),"成交聚合身份");n(counts,k,0,72);}mapEqual(counts,calc.maps.get(field));
        }
        for(String field:MONEY)require(n(a,field)==n(calc.money,field),"营业金额");mapEqual(stock,calc.remaining);
        witness(a,"recordedValid","recordedComplete",menu,calc);
        for(int i=0;i<rows.length();i++)require(n(rows.getJSONObject(i),"at")==start+(i+1)*WINDOW,"窗口时间边界");
        if(!a.isNull("pendingCloseAt")){long close=n(a,"pendingCloseAt",start,end);require(sum(stock)==0?close==start+processed*WINDOW:processed==12&&close==end,"待收摊边界");}
        else require(sum(stock)>0&&processed<12,"缺少待收摊标记");
        require(bonusCredits(reward,initial)>=reserve,"多余额度预留");
    }
    private static int bonusCredits(JSONObject rewards,JSONObject initial)throws Exception {
        JSONObject left=new JSONObject(initial.toString());int credits=6;
        if(rewards.getBoolean("basket"))for(String k:keys(left))if(has(rewards.getJSONArray("basketEligible"),k)){long count=Math.min(credits,n(left,k)/24);credits-=count;left.put(k,n(left,k)-count*24);}
        if(rewards.getBoolean("platter"))while(credits>0){List<String> selected=new ArrayList<>();for(String k:keys(left))if(has(rewards.getJSONArray("platterEligible"),k)&&n(left,k)>=3)selected.add(k);
            selected.sort((x,y)->{int diff=Long.compare(left.optLong(y),left.optLong(x));return diff==0?x.compareTo(y):diff;});if(selected.size()<4)break;
            for(int i=0;i<4;i++){String k=selected.get(i);left.put(k,n(left,k)-3);}credits--;}
        return 6-credits;
    }
    private static final class WindowTotals {
        final JSONObject remaining,money=new JSONObject();final Map<String,JSONObject> maps=new HashMap<>();int cursor=0;
        WindowTotals(JSONObject initial)throws Exception {remaining=new JSONObject(initial.toString());for(String k:MONEY)money.put(k,0);for(String k:new String[]{"soldByKey","roleSales","fullSoldByKey","fullRoleSales"})maps.put(k,new JSONObject());}
    }
    // Rules 1: a menu bird carries at most 2 CP and nothing when ordinary. Rules 2 (complete menu +25%): at most a quarter
    // of its price rounded up, and only a shop that opened complete pays — the first window's tier — in every window
    // (web/business.js themeCapCP and advanceBusiness).
    private static WindowTotals windows(JSONArray rows,JSONObject initial,JSONObject menu,JSONArray roles,JSONObject snapshot,JSONObject prices,long version)throws Exception {
        WindowTotals out=new WindowTotals(initial);array(rows,12);
        String opening=rows.length()>0?object(rows.get(0),"index","at","tier","entries","baseCP","markupCP","themeCP").getString("tier"):"ordinary";
        for(int i=0;i<rows.length();i++){
            JSONObject w=object(rows.get(i),"index","at","tier","entries","baseCP","markupCP","themeCP");require(n(w,"index",1,12)==i+1,"窗口顺序");n(w,"at");choice(w.get("tier"),"ordinary","suitable","complete");
            if(snapshot!=null)require(fit(menu,out.remaining,roles,snapshot).equals(w.getString("tier")),"窗口当时档位");
            JSONArray entries=array(w.get("entries"),6);require(entries.length()>0,"空成交窗口");JSONObject money=new JSONObject();for(String k:MONEY)money.put(k,0);
            for(int j=0;j<entries.length();j++){
                JSONObject entry=object(entries.get(j),"key","roleId","quantity","baseCP","markupCP","themeCP");String key=string(entry.get("key")),roleId=string(entry.get("roleId"));
                require(initial.has(key)&&value(out.remaining,key)>0,"成交来源");n(entry,"quantity",1,1);
                if(roles!=null){String expectedKey=null,expectedRole=null;
                    for(int k=0;k<roles.length();k++){JSONObject r=roles.getJSONObject(out.cursor);out.cursor=(out.cursor+1)%roles.length();JSONArray ks=r.getJSONArray("keys");
                        for(int m=0;m<ks.length();m++)if(value(out.remaining,ks.getString(m))>0){expectedKey=ks.getString(m);expectedRole=r.getString("roleId");break;}if(expectedKey!=null)break;}
                    require(key.equals(expectedKey)&&roleId.equals(expectedRole),"角色轮转");
                }else if(!roleId.equals("ordinary")){JSONObject def=find(menu.getJSONArray("roles"),roleId);require(def!=null&&has(def.getJSONArray("allowed"),key),"成交角色身份");}
                for(String k:MONEY)add(money,k,n(entry,k,0,100000));
                if(prices!=null){JSONObject p=prices.getJSONObject(key);require(n(entry,"baseCP")==n(p,"baseCP")&&n(entry,"markupCP")<=(n(p,"baseCP")*n(p,"markupPercent")+99)/100,"冻结价格");}
                long theme=n(entry,"themeCP"),cap=version==1?2:(n(entry,"baseCP")*25+99)/100;boolean pays=version==1?!w.getString("tier").equals("ordinary"):opening.equals("complete");
                require(theme<=cap&&(pays||theme==0),"主题单只上限");
                out.remaining.put(key,n(out.remaining,key)-1);add(out.maps.get("soldByKey"),key,1);add(out.maps.get("roleSales"),roleId,1);
                if(w.getString("tier").equals("complete")){add(out.maps.get("fullSoldByKey"),key,1);add(out.maps.get("fullRoleSales"),roleId,1);}
            }
            for(String k:MONEY){require(n(w,k)==n(money,k),"窗口金额守恒");add(out.money,k,n(w,k));}
        }return out;
    }
    private static List<String> roleAvailable(JSONArray roles,String id,JSONObject stock,long minimum)throws Exception {
        List<String> out=new ArrayList<>();for(int i=0;i<roles.length();i++){JSONObject r=roles.getJSONObject(i);if(!id.equals(r.getString("roleId")))continue;JSONArray ks=r.getJSONArray("keys");for(int j=0;j<ks.length();j++)if(value(stock,ks.getString(j))>=minimum)out.add(ks.getString(j));}return out;
    }
    private static boolean tag(JSONObject snapshot,String key,String tag)throws Exception{return has(snapshot.getJSONObject("species").getJSONObject(key).getJSONArray("tags"),tag);}
    private static boolean anyTag(JSONObject snapshot,List<String> keys,String tag)throws Exception {for(String k:keys)if(tag(snapshot,k,tag))return true;return false;}
    private static String fit(JSONObject menu,JSONObject stock,JSONArray roles,JSONObject snap)throws Exception {
        JSONArray defs=menu.getJSONArray("roles");for(int i=0;i<defs.length();i++){JSONObject d=defs.getJSONObject(i);if(d.getBoolean("required")&&roleAvailable(roles,d.getString("id"),stock,1).isEmpty())return "ordinary";}
        List<String> assigned=new ArrayList<>(),q=new ArrayList<>();for(int i=0;i<roles.length();i++){String id=roles.getJSONObject(i).getString("roleId");if(!id.equals("ordinary")){assigned.addAll(roleAvailable(roles,id,stock,1));q.addAll(roleAvailable(roles,id,stock,3));}}
        JSONObject sem=snap.getJSONObject("species");boolean complete=false;String id=menu.getString("id");
        switch(id){
            case "MN1":complete=roleAvailable(roles,"MN1-R1",stock,6).size()>=2;break;
            case "MN2":complete=q.size()>=3;for(int i=0;i<defs.length();i++)complete&=!roleAvailable(roles,defs.getJSONObject(i).getString("id"),stock,3).isEmpty();break;
            case "MN3":for(String a:q)for(String b:q)if(!a.equals(b)&&tag(snap,a,"savory")&&tag(snap,b,"sweet"))complete=true;break;
            case "MN4":{Set<Integer> eggs=new HashSet<>();for(String k:q)eggs.add(sem.getJSONObject(k).getInt("egg"));complete=q.size()>=3&&eggs.size()==2;break;}
            case "MN5":complete=q.size()>=3&&(anyTag(snap,q,"floral")||anyTag(snap,q,"roast"));break;
            case "MN6":complete=q.size()>=3&&(anyTag(snap,q,"ginger")||anyTag(snap,q,"mushroom"));break;
            case "MN7":{Set<Integer> eggs=new HashSet<>();boolean bay=false;complete=assigned.size()==q.size();for(String k:assigned){JSONObject c=sem.getJSONObject(k);eggs.add(c.getInt("egg"));if(tag(snap,k,"bay")){JSONArray mats=c.getJSONArray("regionalMaterials");for(int i=0;i<mats.length();i++)if(has(snap.getJSONArray("identified"),String.valueOf(mats.getInt(i))))bay=true;}}complete&=eggs.size()==2&&bay;break;}
            case "MN8":{Set<String> seasons=new HashSet<>();for(String k:q)if(!sem.getJSONObject(k).isNull("season"))seasons.add(sem.getJSONObject(k).getString("season"));complete=seasons.size()>=3;break;}
        }return complete?"complete":"suitable";
    }
    private static boolean covered(JSONObject menu,JSONObject map)throws Exception {JSONArray defs=menu.getJSONArray("roles");for(int i=0;i<defs.length();i++){JSONObject d=defs.getJSONObject(i);if(d.getBoolean("required")&&value(map,d.getString("id"))<1)return false;}return true;}
    private static void witness(JSONObject row,String validKey,String completeKey,JSONObject menu,WindowTotals t)throws Exception {
        require(bool(row.get(validKey))==(sum(t.maps.get("soldByKey"))>=6&&covered(menu,t.maps.get("roleSales"))),"有效菜单见证");
        require(bool(row.get(completeKey))==(sum(t.maps.get("fullSoldByKey"))>=6&&covered(menu,t.maps.get("fullRoleSales"))),"完整菜单见证");
    }
    private static void report(JSONObject b)throws Exception {
        JSONObject r=object(b.get("lastReport"),"id","rulesVersion","menuId","startAt","closedAt","reason","totalSold","initialStock","soldByKey","remainingStock","baseCP","markupCP","themeCP","bonusCP","income","creditsUsed","creditsReleased","baskets","platters","validMenu","completeMenu","visitorEvents","windowReports","bonusSettled");
        JSONObject menu=core(r,b,false);if(!b.isNull("active"))require(identity(r.get("id"),"business",MAX)<n(b,"sequence"),"账单顺序");
        long start=n(r,"startAt"),close=n(r,"closedAt",start,start+DAY);choice(r.get("reason"),"manual","deadline","sold-out");
        long used=n(r,"creditsUsed",0,6),released=n(r,"creditsReleased",0,6),baskets=n(r,"baskets",0,6),platters=n(r,"platters",0,6),bonus=n(r,"bonusCP");
        require(used==baskets+platters&&used+released<=6&&baskets*24+platters*12<=n(r,"totalSold")&&(bonus==baskets*12+platters*8||bonus==baskets*18+platters*12),"账单奖励守恒");
        require(n(r,"income")==n(r,"baseCP")+n(r,"markupCP")+n(r,"themeCP")+bonus,"账单收入守恒");
        JSONObject initial=r.getJSONObject("initialStock"),remaining=stock(r.get("remainingStock"),keys(initial),false);JSONArray rows=array(r.get("windowReports"),12);
        WindowTotals t=windows(rows,initial,menu,null,null,null,n(r,"rulesVersion"));mapEqual(r.getJSONObject("soldByKey"),t.maps.get("soldByKey"));mapEqual(remaining,t.remaining);
        for(String field:MONEY)require(n(r,field)==n(t.money,field),"账单金额");
        for(int i=0;i<rows.length();i++){long at=n(rows.getJSONObject(i),"at");require(at==start+(i+1)*WINDOW&&at<=close,"账单窗口时间");}
        witness(r,"validMenu","completeMenu",menu,t);
        require(!r.getString("reason").equals("sold-out")||sum(remaining)==0,"售罄边界");require(!r.getString("reason").equals("deadline")||close==start+DAY,"到期边界");
    }
    private static JSONArray expandedGroups(JSONObject def,JSONObject variant,JSONObject order)throws Exception {
        JSONArray out=new JSONArray(),groups=def.getJSONArray("groups"),chapters=order.optJSONArray("chapters");boolean seasonal=def.getString("id").equals("O10")&&chapters!=null;
        int count=seasonal?chapters.length():groups.length();
        for(int i=0;i<count;i++){
            JSONObject g=groups.getJSONObject(seasonal?0:i);JSONArray base=variant.has("allowed")&&def.getString("kind").equals("display")?variant.getJSONArray("allowed"):g.getJSONArray("allowed"),allowed=new JSONArray();
            for(int j=0;j<base.length();j++){String key=base.getString(j);JSONObject c=species(key);if(variant.has("egg")&&variant.getInt("egg")==0&&!key.startsWith("0:"))continue;
                if(seasonal&&!Objects.equals(c.get("season"),chapters.get(i)))continue;
                if(!order.isNull("region")&&def.getString("id").equals("O06")&&g.optString("selector").equals("regionFood")&&!Objects.equals(c.get("region"),order.get("region")))continue;allowed.put(key);}
            String source=g.getString("id");out.put(new JSONObject().put("id",seasonal?source+":"+chapters.getString(i):source).put("sourceGroupId",source).put("quantity",g.getInt("quantity")/(seasonal?chapters.length():1)).put("allowed",allowed));
        }return out;
    }
    private static void orders(JSONObject s)throws Exception {
        JSONObject o=object(s.getJSONObject("expansion").get("orders"),"sequence","proposalSequence","proposals","active","templateProgress","refillCredits","lastProposedTemplate");
        n(o,"sequence");n(o,"proposalSequence");n(o,"refillCredits",0,2);if(!o.isNull("lastProposedTemplate"))definition("orders",o.get("lastProposedTemplate"));
        JSONArray proposals=array(o.get("proposals"),2);Set<String> ids=new HashSet<>();
        for(int i=0;i<proposals.length();i++){JSONObject p=object(proposals.get(i),"id","templateId","reason");identity(p.get("id"),"proposal",n(o,"proposalSequence"));require(ids.add(p.getString("id")),"重复提案");definition("orders",p.get("templateId"));choice(p.get("reason"),"visitor","trip","batch");}
        JSONObject progress=object(o.get("templateProgress"));for(String id:keys(progress)){definition("orders",id);JSONObject p=object(progress.get(id),"accepted","completed","cancelled","skipped");for(String k:keys(p))n(p,k);require(n(p,"completed")+n(p,"cancelled")<=n(p,"accepted"),"模板计数守恒");}
        JSONArray active=array(o.get("active"),2);ids.clear();
        for(int i=0;i<active.length();i++){
            JSONObject a=object(active.get(i),"id","templateId","variantId","kind","rulesVersion","acceptedAt","region","chapters","minimumDistinct","bonusCP","groups","reserved","paidCP","deliveries","needsRestock");
            identity(a.get("id"),"order",n(o,"sequence"));require(ids.add(a.getString("id")),"重复采购");JSONObject def=definition("orders",a.get("templateId")),variant=find(def.getJSONArray("variants"),string(a.get("variantId")));require(variant!=null,"采购变体");
            equal(a.get("kind"),def.get("kind"),"采购类型");n(a,"rulesVersion",1,1);equal(a.get("bonusCP"),def.get("bonusCP"),"采购奖励");
            equal(a.get("minimumDistinct"),def.getString("kind").equals("display")?def.opt("displayDistinct")!=null?def.get("displayDistinct"):def.get("minimumDistinct"):def.get("minimumDistinct"),"采购种数");
            n(a,"acceptedAt");n(a,"paidCP");n(a,"deliveries");bool(a.get("needsRestock"));
            if(!a.isNull("region"))require(def.getString("id").equals("O06")&&variant.has("regions")&&has(variant.getJSONArray("regions"),a.get("region")),"采购冻结地区");
            if(!a.isNull("chapters")){
                require(def.getString("id").equals("O10"),"采购章节类型");JSONArray chapters=array(a.get("chapters"),2);require(chapters.length()==2,"章节数量");Set<String> chosen=strings(chapters),seasons=new HashSet<>();JSONArray allowed=def.getJSONArray("groups").getJSONObject(0).getJSONArray("allowed");
                for(int j=0;j<allowed.length();j++){JSONObject c=species(allowed.getString(j));if(!c.isNull("season"))seasons.add(c.getString("season"));}require(seasons.containsAll(chosen),"冻结章节");
            }else require(!def.getString("id").equals("O10"),"缺少冻结章节");
            JSONArray expected=expandedGroups(def,variant,a),groups=array(a.get("groups"),expected.length());require(groups.length()==expected.length(),"需求组数量");long paid=0,remaining=0;
            for(int j=0;j<groups.length();j++){
                JSONObject g=object(groups.get(j),"id","sourceGroupId","quantity","allowed","delivered"),ex=expected.getJSONObject(j);for(String k:keys(ex))equal(g.get(k),ex.get(k),"需求组冻结");
                JSONObject delivered=object(g.get("delivered"));long got=0;for(String key:keys(delivered)){require(has(g.getJSONArray("allowed"),key),"交付身份");long q=n(delivered,key,1,MAX);got+=q;paid+=species(key).getLong("baseSaleCP")*q;}
                require(got<=n(g,"quantity"),"交付超量");remaining+=n(g,"quantity")-got;
            }
            JSONObject reserved=object(a.get("reserved"));if(def.getString("kind").equals("display"))require(n(a,"deliveries")==0&&n(a,"paidCP")==0&&reserved.length()==0,"展示委托不交付");
            require(paid==n(a,"paidCP")&&remaining>0,"采购货款或完成状态");
            for(String key:keys(reserved)){long quantity=n(reserved,key,1,MAX),need=0;for(int j=0;j<groups.length();j++){JSONObject g=groups.getJSONObject(j);if(has(g.getJSONArray("allowed"),key))need+=n(g,"quantity")-sum(g.getJSONObject("delivered"));}require(quantity<=need,"预留超过需求");}
        }
    }
    private interface Allowed {boolean test(String key)throws Exception;}
    private static void counts(Object value,Allowed allowed)throws Exception {JSONObject o=object(value);for(String key:keys(o)){require(allowed.test(key),"事实计数身份");n(o,key);}}
    private static boolean known(String kind,String id)throws Exception{return find(DATA.getJSONArray(kind),id)!=null;}
    private static boolean nestedId(String kind,String field,String id)throws Exception {JSONArray all=DATA.getJSONArray(kind);for(int i=0;i<all.length();i++)if(find(all.getJSONObject(i).getJSONArray(field),id)!=null)return true;return false;}
    private static void sequences(JSONObject row,long max)throws Exception {long first=n(row,"firstSeq",1,max);n(row,"lastSeq",first,max);}
    private static void facts(JSONObject s)throws Exception {
        JSONObject f=object(s.getJSONObject("expansion").get("facts"),"version","businessCounts","businessMenuCounts","orderCounts","orderTemplateCounts","orderGroupCounts","menuWitnesses","tripWitnesses","companionFirst","eventWitnesses","predicateWitnesses","materialBatches","payments","projectDeliveries");
        n(f,"version",1,1);long seq=s.getJSONObject("meta").getLong("factSeq");Allowed speciesId=k->DATA.getJSONObject("species").has(k);
        counts(f.get("businessCounts"),speciesId);counts(f.get("orderCounts"),speciesId);counts(f.get("orderTemplateCounts"),k->known("orders",k));counts(f.get("materialBatches"),k->k.matches("0|[1-9][0-9]?")&&Integer.parseInt(k)<=82);
        for(String field:new String[]{"businessMenuCounts","orderGroupCounts","projectDeliveries"}){JSONObject map=object(f.get(field));for(String id:keys(map)){require(field.equals("businessMenuCounts")?known("menus",id):field.equals("orderGroupCounts")?nestedId("orders","groups",id):nestedId("projects","stages",id),"分组事实身份");counts(map.get(id),speciesId);}}
        counts(f.get("payments"),id->nestedId("projects","stages",id));
        JSONObject map=object(f.get("menuWitnesses"));for(String id:keys(map)){definition("menus",id);JSONObject v=object(map.get(id),"count","completeCount","firstSeq","lastSeq","lastSessionId","completeLastSessionId");sequences(v,seq);long count=n(v,"count",1,MAX);n(v,"completeCount",0,count);sourceId(v.get("lastSessionId"),"business");if(!v.isNull("completeLastSessionId"))sourceId(v.get("completeLastSessionId"),"business");}
        map=object(f.get("tripWitnesses"));for(String id:keys(map)){choice(id,"V","R","T","B");JSONObject v=object(map.get(id),"count","firstSeq","lastSeq");sequences(v,seq);n(v,"count",1,MAX);}
        map=object(f.get("eventWitnesses"));for(String id:keys(map)){definition("cards",id);JSONObject v=object(map.get(id),"count","firstSeq","lastSeq","tripId");sequences(v,seq);n(v,"count",1,MAX);sourceId(v.get("tripId"),"trip");}
        map=object(f.get("companionFirst"));for(String key:keys(map)){species(key);JSONObject v=object(map.get(key),"seq","tripId","region","gather","discover","environment","traits");n(v,"seq",1,seq);sourceId(v.get("tripId"),"trip");choice(v.get("region"),"V","R","T","B");choice(v.get("environment"),"yard","water","wood");n(v,"gather",0,20);n(v,"discover",0,20);for(String trait:strings(array(v.get("traits"),7)))choice(trait,"leaf","grain","portable","tea","floral","fruit","salt");}
        map=object(f.get("predicateWitnesses"));for(String id:keys(map)){require(has(DATA.getJSONArray("predicates"),id),"复合事实身份");JSONObject v=object(map.get(id),"firstSeq","lastSeq","sourceId");sequences(v,seq);String source=string(v.get("sourceId"));require(source.length()>0&&source.length()<=80,"事实来源");}
    }
    private BusinessSaveValidator(){}
}
