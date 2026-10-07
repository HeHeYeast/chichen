package com.jibao.kitchen;

import android.content.Context;
import android.util.AtomicFile;
import org.json.JSONObject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/** App-private saves are independent of the WebView cache and release assets. */
public final class SaveRepository {
    public static final int MAX_BYTES = 2 * 1024 * 1024;
    // JSON quotes/escapes expand the raw save inside its checksum envelope.
    private static final int MAX_ENVELOPE_BYTES = MAX_BYTES * 6 + 4096;
    private static File file(Context context, String name) {
        File folder = new File(context.getFilesDir(), "saves");
        if (!folder.exists() && !folder.mkdirs()) throw new IllegalStateException("无法创建存档目录");
        return new File(folder, name + ".json");
    }
    private static String read(File file) throws Exception {
        try (InputStream in = new AtomicFile(file).openRead()) { return readBounded(in, MAX_ENVELOPE_BYTES); }
    }
    public static String readBounded(InputStream in) throws IOException {
        return readBounded(in, MAX_BYTES);
    }
    private static String readBounded(InputStream in, int limit) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] bytes = new byte[8192]; int count;
        while ((count = in.read(bytes)) != -1) {
            if (out.size() + count > limit) throw new IOException("文件太大，不是有效的鸡宝存档");
            out.write(bytes, 0, count);
        }
        return out.toString(StandardCharsets.UTF_8.name());
    }
    private static String hash(String raw) throws Exception {
        byte[] bytes = MessageDigest.getInstance("SHA-256").digest(raw.getBytes(StandardCharsets.UTF_8));
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) result.append(String.format("%02x", b & 255));
        return result.toString();
    }
    private static JSONObject validate(String raw) throws Exception {
        if (raw.getBytes(StandardCharsets.UTF_8).length > MAX_BYTES) throw new IOException("存档过大");
        JSONObject state = new JSONObject(raw);
        Object rawVersion = state.get("version");
        if (!(rawVersion instanceof Number)) throw new IOException("存档版本无效");
        double numberVersion = ((Number) rawVersion).doubleValue();
        if (!Double.isFinite(numberVersion) || numberVersion != Math.rint(numberVersion)) throw new IOException("存档版本无效");
        if (numberVersion > CURRENT_VERSION) throw new FutureSaveException();
        int version = (int) numberVersion;
        Object rawCp = state.opt("cp");
        if (version < 1 || !(rawCp instanceof Number) || !Double.isFinite(((Number) rawCp).doubleValue()) || state.getDouble("cp") < 0 ||
                !state.has("lastSeen") || !state.has("kitchenLevel") ||
                state.getJSONArray("toolLevels").length() != (version == 1 ? 8 : 9))
            throw new IOException("存档结构不完整");
        integer(state,"cp",0,SAFE_INTEGER);integer(state,"kitchenLevel",0,3);number(state,"lastSeen",0,SAFE_INTEGER);
        org.json.JSONArray tools=state.getJSONArray("toolLevels");
        for(int i=0;i<tools.length();i++) {
            Object value=tools.get(i);
            if(!(value instanceof Number))throw new IOException("厨具等级无效");
            double level=((Number)value).doubleValue();
            if(!Double.isFinite(level)||level!=Math.rint(level)||level< -1||level>2)throw new IOException("厨具等级无效");
        }
        if(version>=4) {
            for(String field:new String[]{"selected","egg","duck","lastSeen","lastClean","farmFixed","farmChecked","batch","dirty","alarm","music","sound","events","progress","cleanCycle","meta","clock","expansion","contentRevision"})
                if(!state.has(field))throw new IOException("存档缺失字段: "+field);
            for(String field:new String[]{"lastClean","farmFixed","farmChecked"})number(state,field,0,SAFE_INTEGER);
            for(String field:new String[]{"duck","dirty","alarm","music","sound"})if(!(state.get(field) instanceof Boolean))throw new IOException("存档开关无效: "+field);
            integer(state,"egg",0,1);if(state.getInt("egg")==1&&!state.getBoolean("duck"))throw new IOException("鸭蛋未开放");
            state.getJSONObject("events");
            org.json.JSONArray selected=state.getJSONArray("selected");if(selected.length()>3)throw new IOException("选中材料过多");
            for(int i=0;i<selected.length();i++) {
                Object v=selected.get(i);
                if(!(v instanceof Number)||((Number)v).doubleValue()!=Math.rint(((Number)v).doubleValue())||((Number)v).doubleValue()<0||((Number)v).doubleValue()>82)throw new IOException("选中材料无效");
            }
        }
        state.getJSONObject("farm"); state.getJSONObject("total"); state.getJSONObject("ingredients");
        validateInventory(state, version);
        if (version >= 3) validateProgress(state);
        if (version >= 4) validateExpansion(state);
        if (version >= 5) validateBusiness(state);
        if (version >= 6) validateCollections(state);
        return state;
    }
    // The WebView validates gameplay contracts; native persistence additionally
    // rejects broken schema-3 ledgers and inventory reservations before rotating files.
    private static void integer(JSONObject o, String key, double min, double max) throws Exception {
        Object v=o.get(key);
        if (!(v instanceof Number)) throw new IOException("存档数值无效: "+key);
        double n=((Number)v).doubleValue();
        if (!Double.isFinite(n)||n!=Math.rint(n)||n<min||n>max) throw new IOException("存档数值无效: "+key);
    }
    private static void number(JSONObject o,String key,double min,double max) throws Exception {
        Object value=o.get(key);
        if(!(value instanceof Number))throw new IOException("存档数值无效: "+key);
        double n=((Number)value).doubleValue();
        if(!Double.isFinite(n)||n<min||n>max)throw new IOException("存档数值无效: "+key);
    }
    private static final double SAFE_INTEGER = 9007199254740991d;
    /** Highest save schema this build understands; newer files are never overwritten. */
    static final int CURRENT_VERSION = 6;
    private static void species(String key, int version) throws IOException {
        if (!key.matches("[01]:(0|[1-9][0-9]*)")) throw new IOException("品种身份无效");
        String[] parts=key.split(":");
        int max=parts[0].equals("0")?(version==1?113:version<4?127:151):(version==1?56:version<4?64:88);
        try { if(Long.parseLong(parts[1])>max) throw new IOException("品种超出存档版本"); }
        catch(NumberFormatException error) { throw new IOException("品种身份无效"); }
    }
    private static void validateInventory(JSONObject s, int version) throws Exception {
        for(String name:new String[]{"farm","total"}) {
            JSONObject stock=s.getJSONObject(name);
            for(java.util.Iterator<String> keys=stock.keys();keys.hasNext();) {
                String key=keys.next();species(key,version);integer(stock,key,0,99999);
            }
        }
        JSONObject materials=s.getJSONObject("ingredients");int sum=0,capacity=30;
        if(version>=4) {
            int identified=s.getJSONObject("expansion").getJSONObject("discovery").getJSONObject("identified").length();
            capacity=identified>=4?42:identified>=1?36:30;
        }
        for(java.util.Iterator<String> keys=materials.keys();keys.hasNext();) {
            String key=keys.next();
            if(!key.matches("0|[1-9][0-9]*")||Long.parseLong(key)>(version<4?74:82))throw new IOException("材料身份无效");
            integer(materials,key,0,capacity);sum+=materials.getInt(key);
        }
        if(sum>capacity)throw new IOException("材料容量无效");
        if(!s.isNull("batch")) {
            JSONObject batch=s.getJSONObject("batch");org.json.JSONArray eggs=batch.getJSONArray("eggs");
            for(int i=0;i<eggs.length();i++) {
                JSONObject egg=eggs.getJSONObject(i);integer(egg,"egg",0,1);integer(egg,"id",0,151);
                species(egg.getInt("egg")+":"+egg.getInt("id"),version);
            }
            if(batch.has("rules")) {
                integer(batch.getJSONObject("rules"),"version",1,version>=4?3:2);
                if(batch.getJSONObject("rules").getInt("version")==3)validateBatchPlan(batch,version);
            }
        }
    }
    private static void stringList(JSONObject o,String key,String pattern,int max) throws Exception {
        org.json.JSONArray a=o.getJSONArray(key);java.util.HashSet<String> seen=new java.util.HashSet<>();
        if(a.length()>max)throw new IOException("列表过长: "+key);
        for(int i=0;i<a.length();i++) {
            Object v=a.get(i);
            if(!(v instanceof String)||!((String)v).matches(pattern)||!seen.add((String)v))throw new IOException("列表无效: "+key);
        }
    }
    private static void sequenceMap(JSONObject o,String pattern) throws Exception {
        for(java.util.Iterator<String> keys=o.keys();keys.hasNext();) {
            String k=keys.next();if(!k.matches(pattern))throw new IOException("扩展身份无效: "+k);integer(o,k,0,SAFE_INTEGER);
        }
    }
    private static void validateExpansion(JSONObject s) throws Exception {
        if(!"regional-1".equals(s.get("contentRevision")))throw new IOException("内容版本无效");
        JSONObject m=s.getJSONObject("meta"),rng=m.getJSONObject("rng");
        for(String k:new String[]{"revision","commandSeq","factSeq"})integer(m,k,0,SAFE_INTEGER);
        if(m.getLong("revision")!=m.getLong("commandSeq"))throw new IOException("事务修订序号无效");
        if(!"fnv1a-mulberry32-v1".equals(rng.get("algorithm")))throw new IOException("随机算法无效");
        integer(rng,"seed",0,4294967295d);
        org.json.JSONArray history=m.getJSONArray("migrationHistory");
        if(history.length()!=s.getInt("version")-3)throw new IOException("迁移记录无效");
        for(int i=0;i<history.length();i++){integer(history.getJSONObject(i),"from",i+3,i+3);integer(history.getJSONObject(i),"to",i+4,i+4);}
        if(!m.has("lastCommit"))throw new IOException("缺失提交回执");
        if(m.isNull("lastCommit")) { if(m.getLong("commandSeq")!=0)throw new IOException("缺失提交回执"); }
        else {
            JSONObject c=m.getJSONObject("lastCommit");Object summary=c.get("summary"),hash=c.get("payloadHash");
            if(!("cmd-"+m.getLong("commandSeq")).equals(c.get("commandId"))||!(hash instanceof String)||!((String)hash).matches("[a-f0-9]{8}")||!(summary instanceof String)||((String)summary).length()>512)throw new IOException("提交回执无效");
        }
        JSONObject clock=s.getJSONObject("clock");integer(clock,"logicalAt",0,SAFE_INTEGER);integer(clock,"lastWallAt",0,SAFE_INTEGER);
        if(clock.getLong("logicalAt")<clock.getLong("lastWallAt"))throw new IOException("时钟顺序无效");
        JSONObject e=s.getJSONObject("expansion"),regions=e.getJSONObject("regions"),discovery=e.getJSONObject("discovery"),methods=e.getJSONObject("methods");
        stringList(regions,"opened","[VRTB]",4);stringList(regions,"introSpecimenDone","[VRTB]",4);stringList(regions,"guideFlags","GUIDE-B",1);
        sequenceMap(discovery.getJSONObject("cards"),"[VRTB]-[SNE][12]");sequenceMap(discovery.getJSONObject("identified"),"7[5-9]|8[0-2]");
        String recipe="(REC-[VRTB]-[CD][1-6]|ALT-[VRTB])";
        stringList(methods,"directions",recipe,52);stringList(methods,"full",recipe,52);
        JSONObject free=methods.getJSONObject("freeProgress");
        for(java.util.Iterator<String> keys=free.keys();keys.hasNext();) {
            String k=keys.next();if(!k.matches("[VRTB]"))throw new IOException("免费方法地区无效");
            JSONObject progress=free.getJSONObject(k);integer(progress,"count",0,2);
            if(!progress.has("targetId")||!progress.isNull("targetId")&&(!(progress.get("targetId") instanceof String)||!progress.getString("targetId").matches(recipe)))throw new IOException("免费方法目标无效");
        }
        JSONObject trial=e.getJSONObject("trial");
        for(java.util.Iterator<String> keys=trial.keys();keys.hasNext();) {
            String k=keys.next();if(!k.matches(recipe))throw new IOException("试做目标无效");
            JSONObject t=trial.getJSONObject(k);integer(t,"failedFullBatches",0,3);integer(t,"attemptSeq",0,SAFE_INTEGER);
            if(!(t.get("owed") instanceof Boolean))throw new IOException("试做保护无效");
        }
        JSONObject protection=e.getJSONObject("cardProtection");
        for(java.util.Iterator<String> keys=protection.keys();keys.hasNext();) {
            String k=keys.next();if(!k.matches("[VRTB]"))throw new IOException("卡保护地区无效");
            JSONObject p=protection.getJSONObject(k);integer(p,"specimen",0,3);integer(p,"lore",0,3);
        }
        if(e.has("prepareMode")) {
            JSONObject prepare=e.getJSONObject("prepareMode");
            Object kind=prepare.opt("kind"),id=prepare.opt("recipeId");
            if(prepare.length()!=2||!(id instanceof String)||!("regional".equals(kind)&&((String)id).matches("REC-[VRTB]-[CD][1-6]")||"local-alternative".equals(kind)&&((String)id).matches("ALT-[VRTB]")))throw new IOException("试做准备无效");
        }
        if(regions.has("materialUse")) {
            JSONObject used=regions.getJSONObject("materialUse");
            for(java.util.Iterator<String> keys=used.keys();keys.hasNext();) {String k=keys.next();if(!k.matches("7[5-9]|8[0-2]")||!Boolean.TRUE.equals(used.get(k)))throw new IOException("材料使用记录无效");}
        }
        if(regions.has("history")) {
            JSONObject regionHistory=regions.getJSONObject("history"),companions=regionHistory.getJSONObject("companionFirst"),trips=regionHistory.getJSONObject("trips");
            for(java.util.Iterator<String> keys=companions.keys();keys.hasNext();) {String k=keys.next();species(k,4);integer(companions,k,0,SAFE_INTEGER);}
            for(java.util.Iterator<String> keys=trips.keys();keys.hasNext();) {
                String k=keys.next();if(!k.matches("[VRTB]"))throw new IOException("同行地区无效");
                JSONObject t=trips.getJSONObject(k);integer(t,"count",0,SAFE_INTEGER);
                stringList(t,"lastMembers","[01]:(0|[1-9][0-9]*)",3);
                for(int i=0;i<t.getJSONArray("lastMembers").length();i++)species(t.getJSONArray("lastMembers").getString(i),4);
                for(String flag:new String[]{"regionalWithLegacy","twoSeasonChapters"})if(!(t.get(flag) instanceof Boolean))throw new IOException("同行见证无效");
            }
            if(regionHistory.has("companionFacts")) {
                JSONObject facts=regionHistory.getJSONObject("companionFacts");
                for(java.util.Iterator<String> keys=facts.keys();keys.hasNext();) {
                    String k=keys.next();species(k,4);JSONObject fact=facts.getJSONObject(k);
                    integer(fact,"seq",0,m.getLong("factSeq"));
                    if(!(fact.get("tripId") instanceof String)||!(fact.get("region") instanceof String)||!fact.getString("region").matches("[VRTB]"))throw new IOException("首次同行来源无效");
                    validateCompanionFact(fact,false);
                }
            }
            if(regionHistory.has("cardFacts")) {
                JSONObject facts=regionHistory.getJSONObject("cardFacts");
                for(java.util.Iterator<String> keys=facts.keys();keys.hasNext();) {
                    String k=keys.next();if(!k.matches("[VRTB]-[SNE][12]"))throw new IOException("卡片同行身份无效");
                    JSONObject fact=facts.getJSONObject(k);integer(fact,"seq",0,m.getLong("factSeq"));
                    if(!(fact.get("tripId") instanceof String))throw new IOException("卡片同行来源无效");
                    org.json.JSONArray members=fact.getJSONArray("members");if(members.length()<1||members.length()>3)throw new IOException("卡片同行队伍无效");
                    for(int i=0;i<members.length();i++){JSONObject member=members.getJSONObject(i);String key=member.getString("key");species(key,4);validateCompanionFact(member,true);}
                }
            }
        }
    }
    private static void validateCompanionFact(JSONObject fact,boolean snapshot) throws Exception {
        String gather=snapshot?"G":"gather",discover=snapshot?"F":"discover";
        integer(fact,gather,0,20);integer(fact,discover,0,20);
        if(!java.util.Arrays.asList("yard","water","wood").contains(fact.get("environment")))throw new IOException("同行能力事实无效");
        stringList(fact,"traits","portable|fruit|tea|leaf|grain|salt|floral",7);
    }
    private static void validateProgress(JSONObject s) throws Exception {
        integer(s,"cp",0,9007199254740991d);
        JSONObject p=s.getJSONObject("progress"),trade=p.getJSONObject("trade"),fortune=p.getJSONObject("fortune");
        p.getJSONObject("sources");p.getJSONObject("skills");p.getJSONObject("orders");p.getJSONObject("routeFailures");
        JSONObject protection=p.getJSONObject("protection");
        for(String k:new String[]{"freshness","sickness"}) if(!(protection.get(k) instanceof Boolean))throw new IOException("保护开关无效");
        integer(trade,"credits",0,6);integer(trade,"harvestProgress",0,23);integer(trade,"rebateRemainder",0,99);
        if(!(trade.get("initialGranted") instanceof Boolean))throw new IOException("额度标记无效");
        if(p.has("skillVersion")) {
            integer(p,"skillVersion",2,2);integer(trade,"markupRemainder",0,99);
            if(p.getJSONArray("leftovers").length()>5)throw new IOException("余料篮过大");
            if(!(protection.get("calm") instanceof Boolean))throw new IOException("安心模式无效");
        }
        integer(fortune,"drought",0,9007199254740991d);
        if(!fortune.isNull("lastTarget"))integer(fortune,"lastTarget",89,103);
        integer(p,"tripSequence",0,9007199254740991d);integer(p,"logicalAt",0,9007199254740991d);
        p.getJSONArray("lastTeam");p.getJSONObject("knowledge").getJSONArray("facts");p.getJSONObject("knowledge").getJSONArray("recipes");
        JSONObject cycle=s.getJSONObject("cleanCycle");integer(cycle,"hours",36,72);integer(cycle,"dirtyAt",0,9007199254740991d);
        if(!p.isNull("trip")) {
            JSONObject t=p.getJSONObject("trip");String status=t.getString("status");
            if(!java.util.Arrays.asList("running","returned","settled","recalled").contains(status))throw new IOException("探索状态无效");
            integer(t,"version",1,s.getInt("version")>=4?2:1);integer(t,"startedAt",0,9007199254740991d);integer(t,"endAt",0,9007199254740991d);
            if(!t.getString("id").equals("trip-"+p.getLong("tripSequence")))throw new IOException("探索事务无效");
            String route=t.getString("routeId");int hours=route.equals("yard")?2:route.equals("water")?6:route.equals("wood")?10:route.equals("bay")?12:0;
            if(route.equals("bay")&&t.getInt("version")!=2)throw new IOException("海湾只接受地区寻访票据");
            if(hours==0||t.getLong("endAt")-t.getLong("startedAt")!=hours*3600000L*(t.getJSONObject("snapshot").optBoolean("light",false)?0.8:1))throw new IOException("探索时间无效");
            org.json.JSONArray members=t.getJSONArray("members"),remaining=t.getJSONArray("remaining");
            if(members.length()<1||members.length()>3||remaining.length()>4)throw new IOException("探索队伍无效");
            java.util.HashMap<String,Integer> seen=new java.util.HashMap<>();
            for(int i=0;i<members.length();i++) {
                String k=members.getString(i);
                species(k,s.getInt("version"));
                int places=seen.merge(k,1,Integer::sum);
                if(status.equals("running")&&s.getJSONObject("farm").optInt(k,0)<places)throw new IOException("探索占用无效");
            }
            t.getJSONObject("snapshot");t.getJSONArray("clueOrder");
            if(!(t.get("clueHit") instanceof Boolean)||!(t.get("clueProcessed") instanceof Boolean))throw new IOException("探索奖励标记无效");
            if(status.equals("running")&&t.getBoolean("clueProcessed"))throw new IOException("探索奖励提前结算");
            if((status.equals("settled")||status.equals("recalled"))&&(remaining.length()!=0||!t.getBoolean("clueProcessed")))throw new IOException("探索奖励未结清");
            if(t.getInt("version")==2)validateRegionalTrip(s,t);
        }
    }
    private static JSONObject findById(String table,String id) throws Exception {
        org.json.JSONArray rows=RuntimeContent.DATA.getJSONArray(table);
        for(int i=0;i<rows.length();i++){JSONObject row=rows.getJSONObject(i);if(String.valueOf(row.get("id")).equals(id))return row;}
        return null;
    }
    private static boolean contains(org.json.JSONArray list,Object value) throws Exception {
        for(int i=0;i<list.length();i++)if(String.valueOf(list.get(i)).equals(String.valueOf(value)))return true;
        return false;
    }
    private static int intValue(Object id,int max,String label) throws IOException {
        if(!(id instanceof Number)||((Number)id).doubleValue()!=Math.rint(((Number)id).doubleValue())||((Number)id).doubleValue()<0||((Number)id).doubleValue()>max)throw new IOException(label);
        return ((Number)id).intValue();
    }
    // Registry-driven: saved tickets are validated against frozen identities, not
    // against the currently released regions, so a closed entry stays readable.
    private static void validateRegionalTrip(JSONObject s,JSONObject t) throws Exception {
        JSONObject r=t.getJSONObject("regional");integer(r,"rulesVersion",1,1);
        JSONObject region=r.get("regionId") instanceof String?findById("regions",r.getString("regionId")):null;
        if(!"fnv1a-mulberry32-v1".equals(r.get("rngAlgorithm"))||region==null||!region.getString("route").equals(t.get("routeId"))
                ||!contains(region.getJSONArray("places"),r.get("placeId"))||!java.util.Arrays.asList("materials","specimen","lore").contains(r.get("focus")))throw new IOException("地区寻访票据无效");
        String regionId=region.getString("id");
        integer(r,"failedBefore",0,3);
        if(!(r.get("processed") instanceof Boolean)||!r.has("intro")||!r.has("result"))throw new IOException("地区寻访结算标记无效");
        int introMaterial=-1;
        if(!r.isNull("intro")) {
            JSONObject intro=r.getJSONObject("intro");
            JSONObject card=intro.get("cardId") instanceof String?findById("cards",intro.getString("cardId")):null;
            if(card==null||!regionId.equals(card.get("region"))||!"specimen".equals(card.get("type")))throw new IOException("首标本票据无效");
            integer(intro,"materialId",75,82);integer(intro,"baseSlot",0,0);
            if(intro.getInt("materialId")!=card.getInt("material"))throw new IOException("首标本材料无效");
            introMaterial=intro.getInt("materialId");
        }
        org.json.JSONArray candidates=r.getJSONArray("candidates");if(candidates.length()>6)throw new IOException("地区候选过多");
        java.util.HashSet<String> cardIds=new java.util.HashSet<>();
        for(int i=0;i<candidates.length();i++) {
            JSONObject c=candidates.getJSONObject(i);
            JSONObject card=c.get("cardId") instanceof String?findById("cards",c.getString("cardId")):null;
            if(card==null||!regionId.equals(card.get("region"))||!cardIds.add(c.getString("cardId")))throw new IOException("地区发现候选无效");
            number(c,"chance",0,.6);number(c,"roll",0,1);if(c.getDouble("roll")==1)throw new IOException("发现票据越界");
        }
        if(!r.isNull("intro")&&(candidates.length()<1||!r.getJSONObject("intro").get("cardId").equals(candidates.getJSONObject(0).get("cardId"))))throw new IOException("首标本缺少候选票据");
        JSONObject method=r.getJSONObject("method");stringList(method,"eligibleIds","REC-"+regionId+"-[CD][1-6]",12);integer(method,"countBefore",0,2);
        org.json.JSONArray eligible=method.getJSONArray("eligibleIds");
        if(!method.has("targetId")||!method.isNull("targetId")&&(eligible.length()==0||!eligible.get(0).equals(method.get("targetId"))))throw new IOException("免费方法票据无效");
        if(method.isNull("targetId")&&eligible.length()!=0)throw new IOException("免费方法未指定目标");
        org.json.JSONArray members=t.getJSONArray("members"),companions=r.getJSONArray("companions"),remaining=t.getJSONArray("remaining");
        if(companions.length()!=members.length())throw new IOException("同行能力快照不完整");
        int totalG=0,totalF=0;
        for(int i=0;i<companions.length();i++) {
            JSONObject c=companions.getJSONObject(i);if(!members.getString(i).equals(c.get("key")))throw new IOException("同行能力身份不符");
            integer(c,"G",0,20);integer(c,"F",0,20);
            if(!java.util.Arrays.asList("yard","water","wood").contains(c.get("environment")))throw new IOException("同行环境无效");stringList(c,"traits",".{1,39}",20);
            totalG+=c.getInt("G");totalF+=c.getInt("F");
        }
        if(totalG!=t.getJSONObject("snapshot").getInt("G")||totalF!=t.getJSONObject("snapshot").getInt("F"))throw new IOException("地区能力与寻访快照不符");
        int samplingMaterial=-1,samplingSlot=-1;
        if(r.has("sampling")&&!r.isNull("sampling")) {
            JSONObject sampling=r.getJSONObject("sampling");org.json.JSONArray ids=sampling.getJSONArray("eligibleIds");
            if(ids.length()<1||ids.length()>2)throw new IOException("地区采样候选无效");
            for(int i=0;i<ids.length();i++)if(!contains(region.getJSONArray("materials"),ids.get(i)))throw new IOException("地区采样候选无效");
            samplingMaterial=intValue(sampling.get("materialId"),82,"地区采样材料无效");samplingSlot=intValue(sampling.get("baseSlot"),2,"地区采样槽位无效");
            if(!contains(ids,samplingMaterial)||!(sampling.get("directed") instanceof Boolean)||introMaterial>=0&&samplingSlot==0)throw new IOException("地区采样材料无效");
        }
        int cargoMaterial=-1;
        if(t.has("cargo")) {
            JSONObject cargo=t.getJSONObject("cargo");JSONObject card=cargo.get("cardId") instanceof String?findById("cards",cargo.getString("cardId")):null;
            if(card==null||!card.has("exchange")||!regionId.equals(card.get("region"))||!card.get("placeId").equals(r.get("placeId")))throw new IOException("带货约定无效");
            JSONObject exchange=card.getJSONObject("exchange"),selection=cargo.getJSONObject("selection");long total=0;
            for(java.util.Iterator<String> keys=selection.keys();keys.hasNext();){String key=keys.next();if(!contains(exchange.getJSONArray("allowed"),key))throw new IOException("带货品种无效");integer(selection,key,1,exchange.getInt("quantity"));total+=selection.getLong(key);}
            if(total!=exchange.getInt("quantity")||cargo.optInt("quantity",-1)!=exchange.getInt("quantity")||cargo.optInt("rewardMaterial",-1)!=exchange.getInt("rewardMaterial")||cargo.optInt("baseSlot",-1)!=(introMaterial>=0?1:0)||!(cargo.get("processed") instanceof Boolean))throw new IOException("带货票据无效");
            String st=t.getString("status");boolean done=cargo.getBoolean("processed");Object outcome=cargo.opt("outcome");
            if(st.equals("running")&&(done||!JSONObject.NULL.equals(outcome))||st.equals("recalled")&&!"released".equals(outcome)||(st.equals("returned")||st.equals("settled"))&&!"exchanged".equals(outcome))throw new IOException("带货结算状态无效");
            if(samplingMaterial>=0&&samplingSlot!=cargo.getInt("baseSlot")+1)throw new IOException("带货与采样槽位冲突");
            cargoMaterial=cargo.getInt("rewardMaterial");
            if(st.equals("running")&&(remaining.length()<=cargo.getInt("baseSlot")||remaining.getInt(cargo.getInt("baseSlot"))!=cargoMaterial))throw new IOException("带货交换槽位无效");
        }
        if(r.has("guide")&&(!Boolean.TRUE.equals(r.get("guide"))||!"R".equals(regionId)||!"R:1".equals(r.get("placeId"))))throw new IOException("沿湾路标票据无效");
        int regionalUnits=0;
        for(int i=0;i<remaining.length();i++) {
            int id=intValue(remaining.get(i),82,"地区材料票据无效");
            if(id>=75){regionalUnits++;if(id!=introMaterial&&id!=samplingMaterial&&id!=cargoMaterial||regionalUnits>(introMaterial>=0?1:0)+(samplingMaterial>=0?1:0)+(cargoMaterial>=0?1:0))throw new IOException("地区材料没有冻结来源");}
        }
        String status=t.getString("status");boolean processed=r.getBoolean("processed");
        if((status.equals("running")||status.equals("recalled"))&&(processed||!r.isNull("result")))throw new IOException("地区发现提前结算");
        if(status.equals("running")&&introMaterial>=0&&(remaining.length()==0||remaining.getInt(0)!=introMaterial))throw new IOException("首标本材料没有占用首格");
        if(status.equals("running")&&samplingMaterial>=0&&(remaining.length()<=samplingSlot||remaining.getInt(samplingSlot)!=samplingMaterial))throw new IOException("地区采样材料槽位无效");
        if((status.equals("returned")||status.equals("settled"))&&(!processed||r.isNull("result")))throw new IOException("地区发现尚未登记");
        if(!r.isNull("result")) {
            JSONObject result=r.getJSONObject("result");
            if(!result.has("cardId")||!result.isNull("cardId")&&!cardIds.contains(String.valueOf(result.get("cardId")))||!result.has("methodId")||!result.isNull("methodId")&&!contains(eligible,result.get("methodId")))throw new IOException("地区发现结果无效");
        }
    }
    private static java.util.List<Integer> sortedIds(org.json.JSONArray list,boolean objects) throws Exception {
        java.util.ArrayList<Integer> ids=new java.util.ArrayList<>();
        for(int i=0;i<list.length();i++)ids.add(objects?list.getJSONObject(i).getInt("id"):intValue(list.get(i),82,"材料身份无效"));
        java.util.Collections.sort(ids);return ids;
    }
    private static void validateBatchPlan(JSONObject batch,int version) throws Exception {
        JSONObject plan=batch.getJSONObject("plan");integer(plan,"version",1,1);
        String mode=plan.getString("mode");
        if(!java.util.Arrays.asList("legacy","regional-trial","regional-repeat","local-alternative").contains(mode))throw new IOException("批次模式无效");
        if(!(plan.get("targetScheduled") instanceof Boolean)||!(plan.get("finished") instanceof Boolean)||!plan.has("roll")||!plan.has("recipeId")||!plan.has("targetKey"))throw new IOException("批次计划字段无效");
        boolean regional=mode.startsWith("regional");int targetId=-1;
        if(mode.equals("legacy")) {
            if(!plan.isNull("recipeId")||!plan.isNull("targetKey")||plan.getBoolean("targetScheduled")||!plan.isNull("roll"))throw new IOException("旧池批次混入地区目标");
        } else if(mode.equals("local-alternative")) {
            JSONObject alt=plan.get("recipeId") instanceof String?findById("alternatives",plan.getString("recipeId")):null;
            if(alt==null||!alt.getString("target").equals(plan.get("targetKey"))||batch.getInt("egg")!=Integer.parseInt(alt.getString("target").split(":")[0])||batch.getInt("tool")!=alt.getInt("toolId")||plan.getBoolean("targetScheduled")||!plan.isNull("roll"))throw new IOException("替代做法票据无效");
            integer(batch,"level",alt.getInt("toolLevel"),2);integer(batch.getJSONObject("rules"),"kitchenLevel",alt.getInt("kitchenLevel"),3);
            if(!sortedIds(batch.getJSONArray("ingredients"),false).equals(sortedIds(alt.getJSONArray("ingredients"),false)))throw new IOException("替代做法材料不精确");
        } else {
            JSONObject recipe=plan.get("recipeId") instanceof String?findById("recipes",plan.getString("recipeId")):null;
            if(recipe==null||!recipe.getString("key").equals(plan.get("targetKey"))||batch.getInt("egg")!=recipe.getInt("egg")||batch.getInt("tool")!=recipe.getInt("toolId"))throw new IOException("地区目标与方法不符");
            targetId=Integer.parseInt(recipe.getString("key").split(":")[1]);
            if(!plan.isNull("roll")){number(plan,"roll",0,1);if(plan.getDouble("roll")==1)throw new IOException("试做概率票据越界");if(plan.getBoolean("targetScheduled")!=(plan.getDouble("roll")<.25))throw new IOException("试做结果与概率票据不符");}
            else if(!plan.getBoolean("targetScheduled"))throw new IOException("保证批次未安排目标");
            if(mode.equals("regional-repeat")&&(!plan.getBoolean("targetScheduled")||!plan.isNull("roll")))throw new IOException("复刻必须保证目标");
            integer(batch,"level",recipe.getInt("toolLevel"),2);integer(batch.getJSONObject("rules"),"kitchenLevel",recipe.getInt("kitchenLevel"),3);
            if(!sortedIds(batch.getJSONArray("ingredients"),false).equals(sortedIds(recipe.getJSONArray("ingredients"),true)))throw new IOException("地区配方材料不精确");
        }
        org.json.JSONArray initial=plan.getJSONArray("initialIds"),eggs=batch.getJSONArray("eggs");
        if(initial.length()!=24||eggs.length()!=24)throw new IOException("批次计划数量无效");
        int scheduled=0;boolean allCollected=true;int legacyLimit=batch.getInt("egg")==0?128:65;
        for(int i=0;i<24;i++) {
            int id=intValue(initial.get(i),151,"初始品种票据无效");
            species(batch.getInt("egg")+":"+id,version);if(id==targetId)scheduled++;
            if(id>=legacyLimit&&id!=targetId)throw new IOException("普通伴随池混入新地区身份");
            org.json.JSONArray tickets=eggs.getJSONObject(i).getJSONArray("tickets");if(tickets.length()!=6)throw new IOException("变化票据数量无效");
            if(!eggs.getJSONObject(i).getBoolean("collected"))allCollected=false;
            for(int j=0;j<6;j++){Object v=tickets.get(j);if(!(v instanceof Number))throw new IOException("变化票据无效");double n=((Number)v).doubleValue();if(!Double.isFinite(n)||n<0||n>=1)throw new IOException("变化票据越界");}
        }
        if(regional&&scheduled!=(plan.getBoolean("targetScheduled")?1:0))throw new IOException("地区安排目标数量无效");
        if(!mode.equals("legacy")&&allCollected!=plan.getBoolean("finished"))throw new IOException("地区收锅结算状态无效");
    }
    private static void exactKeys(JSONObject o,String label,String... keys) throws Exception {
        if(o.length()!=keys.length)throw new IOException("营业字段无效: "+label);
        for(String key:keys)if(!o.has(key))throw new IOException("营业字段缺失: "+label+"."+key);
    }
    // Schema 5 core invariants shared with the WebView validator: one owner per
    // reservation, S never exceeds the owned stock, and no revenue before a window.
    private static long awayCount(JSONObject trip,String key) throws Exception {
        if(trip==null||!"running".equals(trip.optString("status")))return 0;
        long away=0;org.json.JSONArray places=trip.getJSONArray("members");for(int i=0;i<places.length();i++)if(key.equals(places.optString(i)))away++;
        JSONObject cargo=trip.optJSONObject("cargo");
        if(cargo!=null&&!cargo.optBoolean("processed",true))away+=cargo.getJSONObject("selection").optLong(key,0);
        return away;
    }
    // Schema 6: entitlement ledger (once per identity, increasing fact sequence),
    // three display slots holding owned mementos, sparse regular/project records.
    private static void validateCollections(JSONObject s) throws Exception {
        JSONObject e=s.getJSONObject("expansion"),c=e.getJSONObject("collections");
        exactKeys(c,"collections","version","entitlements","display");integer(c,"version",1,1);
        long factSeq=s.getJSONObject("meta").getLong("factSeq");
        JSONObject granted=c.getJSONObject("entitlements");java.util.HashSet<Long> seqs=new java.util.HashSet<>();
        for(java.util.Iterator<String> keys=granted.keys();keys.hasNext();){
            String id=keys.next();if(!contains(RuntimeContent.DATA.getJSONArray("entitlementIds"),id))throw new IOException("收藏成果身份无效");
            JSONObject v=granted.getJSONObject(id);exactKeys(v,"entitlement","seq","source");integer(v,"seq",1,factSeq);
            if(!seqs.add(v.getLong("seq"))||!(v.get("source") instanceof String)||v.getString("source").isEmpty()||v.getString("source").length()>40)throw new IOException("收藏成果记录无效");
        }
        org.json.JSONArray display=c.getJSONArray("display");if(display.length()!=3)throw new IOException("陈列位无效");
        java.util.HashSet<String> shown=new java.util.HashSet<>();
        for(int i=0;i<3;i++){if(display.isNull(i))continue;String id=String.valueOf(display.get(i));if(!contains(RuntimeContent.DATA.getJSONArray("mementoIds"),id)||!granted.has(id)||!shown.add(id))throw new IOException("陈列纪念物无效");}
        validateRegulars(s,granted,factSeq);
        boolean pj1=validateProjects(s,factSeq);
        JSONObject menus=e.getJSONObject("menus");exactKeys(menus,"menus","presets");org.json.JSONArray presets=menus.getJSONArray("presets");
        if(presets.length()>3||(presets.length()>0&&!pj1))throw new IOException("菜单预设无效");
        for(int i=0;i<presets.length();i++){
            JSONObject preset=presets.getJSONObject(i);exactKeys(preset,"preset","menuId","stock");if(findById("menus",preset.optString("menuId",""))==null)throw new IOException("菜单预设无效");
            JSONObject stock=preset.getJSONObject("stock");if(stock.length()<1||stock.length()>6)throw new IOException("菜单预设无效");
            for(java.util.Iterator<String> keys=stock.keys();keys.hasNext();){String key=keys.next();if(!RuntimeContent.DATA.getJSONObject("species").has(key)||!RuntimeContent.DATA.getJSONObject("species").getJSONObject(key).getBoolean("edible"))throw new IOException("菜单预设无效");integer(stock,key,1,72);}
        }
    }
    // Work I: each regular reads its four stages in order, holds at most one unread
    // stage, and every read/unread stage already has its note or memento.
    private static void validateRegulars(JSONObject s,JSONObject granted,long factSeq) throws Exception {
        JSONObject all=s.getJSONObject("expansion").getJSONObject("regulars");
        for(java.util.Iterator<String> keys=all.keys();keys.hasNext();){
            String id=keys.next();JSONObject def=findById("regulars",id);if(def==null)throw new IOException("常客身份无效");
            org.json.JSONArray stages=def.getJSONArray("stages");JSONObject r=all.getJSONObject(id);
            exactKeys(r,"regular","readStages","pendingStage","activatedSeq","baselines","lastVisit");
            org.json.JSONArray read=r.getJSONArray("readStages");if(read.length()>stages.length())throw new IOException("常客已读顺序无效");
            for(int i=0;i<read.length();i++){JSONObject st=stages.getJSONObject(i);if(!st.getString("id").equals(read.optString(i,null))||!granted.has(st.getString("reward")))throw new IOException("常客已读顺序无效");}
            if(!r.isNull("pendingStage")){
                JSONObject p=r.getJSONObject("pendingStage");exactKeys(p,"pendingStage","id","seq","branch");integer(p,"seq",1,factSeq);integer(p,"branch",0,1);
                if(read.length()>=stages.length()||!stages.getJSONObject(read.length()).getString("id").equals(p.optString("id",null))||!granted.has(stages.getJSONObject(read.length()).getString("reward")))throw new IOException("常客未读段无效");
            } else if(read.length()==0) throw new IOException("空常客记录");
            if(read.length()==0){if(!r.isNull("activatedSeq"))throw new IOException("常客激活序号无效");} else integer(r,"activatedSeq",1,factSeq);
            JSONObject base=r.getJSONObject("baselines");boolean expectsO05=read.length()<stages.length()&&"RG2-4".equals(stages.getJSONObject(read.length()).getString("id"));
            if(base.length()!=(expectsO05?1:0))throw new IOException("常客基线无效");
            if(expectsO05)integer(base,"O05",0,SAFE_INTEGER);
            if(!r.isNull("lastVisit")){
                JSONObject v=r.getJSONObject("lastVisit");exactKeys(v,"lastVisit","stageId","sessionId");int index=-1;
                for(int i=0;i<stages.length();i++)if(stages.getJSONObject(i).getString("id").equals(v.optString("stageId",null)))index=i;
                if(index<0||index>read.length()||!v.optString("sessionId","").matches("business-[1-9][0-9]*"))throw new IOException("常客来访记录无效");
            }
        }
    }
    // Work J: stages complete in order with their exact fixed payment; bounded
    // deliveries (locked kinds or a total with signature categories); PJ-4 portraits.
    private static boolean validateProjects(JSONObject s,long factSeq) throws Exception {
        JSONObject all=s.getJSONObject("expansion").getJSONObject("projects");boolean pj1=false;
        for(java.util.Iterator<String> ids=all.keys();ids.hasNext();){
            String id=ids.next();JSONObject def=findById("projects",id);if(def==null)throw new IOException("项目身份无效");
            JSONObject p=all.getJSONObject(id);exactKeys(p,"project","stages","pinnedChoices","deliveries","payments");
            JSONObject stages=p.getJSONObject("stages"),payments=p.getJSONObject("payments"),deliveries=p.getJSONObject("deliveries"),choices=p.getJSONObject("pinnedChoices");
            org.json.JSONArray defs=def.getJSONArray("stages");java.util.HashSet<String> stageIds=new java.util.HashSet<>();boolean open=false;
            for(int i=0;i<defs.length();i++){
                JSONObject st=defs.getJSONObject(i);String sid=st.getString("id");stageIds.add(sid);int cost=st.getInt("costCP");boolean done=stages.has(sid);
                if(done){if(open)throw new IOException("项目阶段顺序无效");JSONObject d=stages.getJSONObject(sid);exactKeys(d,"stage","complete","seq");if(!Boolean.TRUE.equals(d.get("complete")))throw new IOException("项目阶段无效");integer(d,"seq",1,factSeq);} else open=true;
                if(payments.has(sid))integer(payments,sid,cost,cost);
                if(payments.has(sid)&&(!done||cost==0||payments.getInt(sid)!=cost))throw new IOException("项目付款无效");
                if(done&&cost>0&&(!payments.has(sid)||payments.getInt(sid)!=cost))throw new IOException("项目付款无效");
                if(deliveries.has(sid)){
                    if(st.isNull("consume"))throw new IOException("项目交付无效");
                    if(i>0&&!stages.has(defs.getJSONObject(i-1).getString("id")))throw new IOException("项目交付顺序无效");
                    JSONObject c=st.getJSONObject("consume"),got=deliveries.getJSONObject(sid);org.json.JSONArray allowed=c.getJSONArray("allowed");long total=0;java.util.HashSet<String> cats=new java.util.HashSet<>();
                    for(java.util.Iterator<String> keys=got.keys();keys.hasNext();){String key=keys.next();if(!contains(allowed,key))throw new IOException("项目交付无效");integer(got,key,1,1000);total+=got.getInt(key);
                        JSONObject trade=RuntimeContent.DATA.getJSONObject("tradeCategories");if(trade.has(key)&&!trade.isNull(key))cats.add(trade.getString(key));}
                    if(c.has("distinct")){
                        org.json.JSONArray locked=choices.optJSONArray(sid);if(locked==null||locked.length()!=c.getInt("distinct"))throw new IOException("项目选定品种无效");
                        for(java.util.Iterator<String> keys=got.keys();keys.hasNext();){String key=keys.next();if(!contains(locked,key)||got.getInt(key)>c.getInt("quantityEach"))throw new IOException("项目交付无效");}
                        if(done)for(int k=0;k<locked.length();k++)if(got.optInt(locked.getString(k),0)!=c.getInt("quantityEach"))throw new IOException("项目交付未满");
                    } else {
                        if(total>c.getInt("total"))throw new IOException("项目交付无效");
                        if(done&&(total!=c.getInt("total")||cats.size()<c.getInt("minimumSignatureCategories")))throw new IOException("项目交付未满");
                    }
                } else if(done&&!st.isNull("consume")) throw new IOException("项目交付未满");
            }
            for(java.util.Iterator<String> keys=stages.keys();keys.hasNext();)if(!stageIds.contains(keys.next()))throw new IOException("项目阶段无效");
            for(java.util.Iterator<String> keys=payments.keys();keys.hasNext();)if(!stageIds.contains(keys.next()))throw new IOException("项目付款无效");
            for(java.util.Iterator<String> keys=deliveries.keys();keys.hasNext();)if(!stageIds.contains(keys.next()))throw new IOException("项目交付无效");
            for(java.util.Iterator<String> keys=choices.keys();keys.hasNext();){
                String key=keys.next();org.json.JSONArray v=choices.getJSONArray(key);
                if(key.equals("portraits")){if(!id.equals("PJ-4")||v.length()<1||v.length()>12)throw new IOException("展册画像无效");java.util.HashSet<String> seen=new java.util.HashSet<>();
                    for(int k=0;k<v.length();k++)if(!contains(def.getJSONObject("optionalDisplay").getJSONArray("allowed"),v.getString(k))||!seen.add(v.getString(k)))throw new IOException("展册画像无效");}
                else {
                    JSONObject stage=null;for(int i=0;i<defs.length();i++)if(key.equals(defs.getJSONObject(i).getString("id")))stage=defs.getJSONObject(i);
                    JSONObject consume=stage==null?null:stage.optJSONObject("consume");
                    if(consume==null||!consume.has("distinct")||v.length()!=consume.getInt("distinct"))throw new IOException("项目选定品种无效");
                    java.util.HashSet<String> seen=new java.util.HashSet<>();
                    for(int i=0;i<v.length();i++)if(!(v.get(i) instanceof String)||!contains(consume.getJSONArray("allowed"),v.getString(i))||!seen.add(v.getString(i)))throw new IOException("项目选定品种无效");
                }
            }
            if(id.equals("PJ-1")&&stages.has(defs.getJSONObject(defs.length()-1).getString("id")))pj1=true;
        }
        return pj1;
    }
    private static void validateBusiness(JSONObject s) throws Exception {
        BusinessSaveValidator.validate(s);
        // T remains in farm. Derive R/S/Q only from their owning frozen records.
        JSONObject e=s.getJSONObject("expansion"),farm=s.getJSONObject("farm"),trip=s.getJSONObject("progress").optJSONObject("trip");
        JSONObject active=e.getJSONObject("business").optJSONObject("active");
        JSONObject stall=active==null?new JSONObject():active.getJSONObject("stock");
        java.util.HashMap<String,Long> reserved=new java.util.HashMap<>();
        org.json.JSONArray orders=e.getJSONObject("orders").getJSONArray("active");
        java.util.HashSet<String> identities=new java.util.HashSet<>();
        for(java.util.Iterator<String> keys=stall.keys();keys.hasNext();)identities.add(keys.next());
        for(int i=0;i<orders.length();i++){
            JSONObject q=orders.getJSONObject(i).getJSONObject("reserved");
            for(java.util.Iterator<String> keys=q.keys();keys.hasNext();){String key=keys.next();identities.add(key);reserved.merge(key,q.getLong(key),Long::sum);}
        }
        if(trip!=null&&"running".equals(trip.optString("status"))){
            org.json.JSONArray members=trip.getJSONArray("members");for(int i=0;i<members.length();i++)identities.add(members.getString(i));
            JSONObject cargo=trip.optJSONObject("cargo");if(cargo!=null&&!cargo.getBoolean("processed"))for(java.util.Iterator<String> keys=cargo.getJSONObject("selection").keys();keys.hasNext();)identities.add(keys.next());
        }
        for(String key:identities)if(farm.optLong(key,0)<awayCount(trip,key)+stall.optLong(key,0)+reserved.getOrDefault(key,0L))throw new IOException("伙伴库存占用无效");
    }
    private static String unwrap(File file) throws Exception {
        JSONObject envelope = new JSONObject(read(file));
        if (envelope.optInt("formatVersion") > 1) throw new FutureSaveException();
        if (!"chick-kitchen-native".equals(envelope.getString("format"))) throw new IOException("存档格式错误");
        String raw = envelope.getString("raw");
        if (!hash(raw).equals(envelope.getString("sha256"))) throw new IOException("存档校验失败");
        validate(raw); return raw;
    }
    /** For bytes this commit already validated (the candidate, or the previous generation load() unwrapped). */
    private static void writeValidated(File target, String raw) throws Exception {
        JSONObject envelope = new JSONObject().put("format", "chick-kitchen-native")
                .put("formatVersion", 1).put("sha256", hash(raw)).put("raw", raw);
        AtomicFile atomic = new AtomicFile(target); FileOutputStream out = null;
        try {
            out = atomic.startWrite(); out.write(envelope.toString().getBytes(StandardCharsets.UTF_8));
            atomic.finishWrite(out);
        } catch (Exception error) { if (out != null) atomic.failWrite(out); throw error; }
    }
    private static boolean exists(File target) {
        return target.exists() || new File(target + ".bak").exists();
    }
    private static void preserveBytes(File source, File destination) throws Exception {
        AtomicFile rescue = new AtomicFile(destination);
        FileOutputStream out = null;
        try (InputStream in = new AtomicFile(source).openRead()) {
            out = rescue.startWrite();
            byte[] bytes = new byte[8192]; int count;
            while ((count = in.read(bytes)) != -1) out.write(bytes, 0, count);
            rescue.finishWrite(out);
        } catch (Exception error) {
            if (out != null) rescue.failWrite(out);
            throw error;
        }
    }
    public static synchronized JSONObject load(Context context) {
        JSONObject result = new JSONObject();
        try {
            File current = file(context, "current"), previous = file(context, "previous");
            if (!exists(current) && !exists(previous)) return result.put("status", "empty");
            try { return result.put("status", "ok").put("raw", unwrap(current)); }
            catch (FutureSaveException future) { throw future; }
            catch (Exception damaged) {
                if (exists(previous)) return result.put("status", "recovered").put("raw", unwrap(previous))
                        .put("message", "主存档损坏，已恢复上一份自动备份。请导出备份留存。");
                throw damaged;
            }
        } catch (Exception error) {
            try { result.put("status", "error").put("message", error instanceof FutureSaveException
                    ? "此存档来自较新版本，请更新应用后继续。原文件已保留。"
                    : "无法读取存档，已停止自动保存。请导入有效备份，原文件已保留。"); }
            catch (Exception ignored) { }
            return result;
        }
    }
    public static synchronized JSONObject readState(Context context) {
        try { JSONObject result = load(context); return result.has("raw") ? validate(result.getString("raw")) : null; }
        catch (Exception error) { return null; }
    }
    public static synchronized String updateBackup(Context context, String versionName, int versionCode, long now) throws Exception {
        JSONObject loaded = load(context);
        // A recovery generation is not proof that the latest progress is safe.
        // Block upgrades until the player has dealt with an empty/damaged save.
        if (!"ok".equals(loaded.optString("status"))) throw new IOException("请先进入游戏确认并保存有效进度，再更新");
        String raw = loaded.getString("raw");
        java.text.SimpleDateFormat timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.ROOT);
        timestamp.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        String backup = new JSONObject().put("format", "chick-kitchen").put("formatVersion", 1)
                .put("exportedAt", timestamp.format(new java.util.Date(now))).put("gameVersion", versionName)
                .put("save", validate(raw)).put("updateBackup", new JSONObject().put("protocol", 1)
                        .put("applicationId", "com.jibao.kitchen").put("versionCode", versionCode)
                        .put("sourceSha256", hash(raw))).toString();
        if (backup.getBytes(StandardCharsets.UTF_8).length > MAX_BYTES) throw new IOException("备份文件过大，更新已停止");
        return backup;
    }
    public static synchronized void save(Context context, String raw, boolean importing) throws Exception {
        persist(context,raw,importing,null);
    }
    /** Explicit compare-and-swap entry point; the class monitor is the native single writer lock. */
    public static synchronized JSONObject commit(Context context,String raw,long expectedRevision) throws Exception {
        if(expectedRevision<0||expectedRevision>SAFE_INTEGER)throw new IOException("事务预期修订无效");
        return persist(context,raw,false,expectedRevision);
    }
    private static JSONObject receipt(JSONObject candidate,boolean duplicate) throws Exception {
        JSONObject result=new JSONObject().put("ok",true).put("committed",true).put("duplicate",duplicate);
        if(candidate.getInt("version")>=4)result.put("revision",candidate.getJSONObject("meta").getLong("revision"))
                .put("lastCommit",candidate.getJSONObject("meta").get("lastCommit"));
        return result;
    }
    private static boolean initialRevision(JSONObject candidate,Long expectedRevision) throws Exception {
        long revision=candidate.getJSONObject("meta").getLong("revision");
        // execute() migrates/new-creates revision zero in memory and writes its
        // first command as revision one. Both paths compare against durable zero.
        return (revision==0&&(expectedRevision==null||expectedRevision==0))
                ||(revision==1&&expectedRevision!=null&&expectedRevision==0);
    }
    private static JSONObject persist(Context context,String raw,boolean importing,Long expectedRevision) throws Exception {
        JSONObject candidate=validate(raw);
        JSONObject existing = load(context);
        if (!importing && "error".equals(existing.optString("status"))) throw new IOException(existing.optString("message"));
        if(expectedRevision!=null&&candidate.getInt("version")<4)throw new IOException("旧存档不支持事务提交");
        if (existing.has("raw")) {
            String previous = existing.getString("raw");
            JSONObject prior=new JSONObject(previous); // load() already validated it in unwrap()
            if(!importing) {
                // ACK loss is safe to retry only with the exact already-persisted candidate.
                if(previous.equals(raw))return receipt(candidate,true);
                if(candidate.getInt("version")<prior.getInt("version"))throw new RevisionConflictException();
                if(prior.getInt("version")>=4) {
                    if(candidate.getInt("version")<4)throw new RevisionConflictException();
                    JSONObject before=prior.getJSONObject("meta"),after=candidate.getJSONObject("meta");
                    long revision=before.getLong("revision");
                    if(expectedRevision!=null&&expectedRevision!=revision||after.getLong("revision")!=revision+1||after.getLong("commandSeq")!=before.getLong("commandSeq")+1)throw new RevisionConflictException();
                } else if(candidate.getInt("version")>=4) {
                    if(!initialRevision(candidate,expectedRevision))throw new RevisionConflictException();
                }
            }
            if(candidate.getInt("version")>prior.getInt("version")) {
                // A dedicated immutable rescue generation survives later rolling saves.
                File rescue=file(context,"before-upgrade-v"+prior.getInt("version"));
                File source=file(context,"recovered".equals(existing.optString("status"))?"previous":"current");
                if(!exists(rescue))preserveBytes(source,rescue);
            }
            if (importing) writeValidated(file(context, "before-import"), previous);
            if (!previous.equals(raw)) writeValidated(file(context, "previous"), previous);
        } else if (importing && exists(file(context, "current"))) {
            // Preserve unreadable/future data byte-for-byte for manual recovery.
            preserveBytes(file(context, "current"), file(context, "before-import-unreadable"));
        } else if(!importing&&candidate.getInt("version")>=4&&!initialRevision(candidate,expectedRevision)) {
            throw new RevisionConflictException();
        }
        writeValidated(file(context, "current"), raw);
        return receipt(candidate,false);
    }
    public static final class RevisionConflictException extends IOException {
        RevisionConflictException() { super("存档已在别处更新，请重新打开游戏后再试"); }
    }
    private static final class FutureSaveException extends IOException { }
}
