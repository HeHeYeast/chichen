package com.jibao.kitchen;

import android.content.Context;
import android.util.AtomicFile;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HexFormat;

/** Production SaveRepository + AOSP JSON + actual files; AtomicFile is a JVM adapter. */
public final class SaveRepositoryTest {
    private static int assertions=0;
    private static Path root;
    private static void check(boolean result,String description){assertions++;if(!result)throw new AssertionError(description);}
    private static Context context() throws Exception{return new Context(Files.createTempDirectory(root,"case-").toFile());}
    private static Path file(Context c,String name){return c.getFilesDir().toPath().resolve("saves/"+name+".json");}
    private static String state(int version,int cp) throws Exception{
        JSONArray tools=new JSONArray();for(int i=0;i<(version==1?8:9);i++)tools.put(i==0?0:-1);
        return new JSONObject().put("version",version).put("cp",cp).put("kitchenLevel",0).put("toolLevels",tools)
                .put("lastSeen",System.currentTimeMillis()).put("ingredients",new JSONObject()).put("farm",new JSONObject()).put("total",new JSONObject()).toString();
    }
    private static String envelope(String raw,int version) throws Exception{
        return new JSONObject().put("format","chick-kitchen-native").put("formatVersion",version).put("raw",raw)
                .put("sha256",HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(raw.getBytes(StandardCharsets.UTF_8)))).toString();
    }
    private static boolean failsSave(Context c,String raw,boolean importing){try{SaveRepository.save(c,raw,importing);return false;}catch(Exception expected){return true;}}
    private static boolean failsBackup(Context c){try{SaveRepository.updateBackup(c,"1.4.7",14,1234L);return false;}catch(Exception expected){return true;}}
    private static JSONObject v4(JSONObject source) throws Exception {
        JSONObject s=new JSONObject(source.toString());s.put("version",4).put("contentRevision","regional-1");
        s.put("meta",new JSONObject().put("revision",0).put("commandSeq",0).put("factSeq",0).put("lastCommit",JSONObject.NULL)
                .put("rng",new JSONObject().put("algorithm","fnv1a-mulberry32-v1").put("seed",4294967295L))
                .put("migrationHistory",new JSONArray().put(new JSONObject().put("from",3).put("to",4))));
        s.put("clock",new JSONObject().put("logicalAt",Math.max(s.getLong("lastSeen"),s.getJSONObject("progress").getLong("logicalAt"))).put("lastWallAt",s.getLong("lastSeen")));
        s.put("expansion",new JSONObject().put("regions",new JSONObject().put("opened",new JSONArray()).put("introSpecimenDone",new JSONArray()).put("guideFlags",new JSONArray()))
                .put("discovery",new JSONObject().put("cards",new JSONObject()).put("identified",new JSONObject()))
                .put("methods",new JSONObject().put("directions",new JSONArray()).put("full",new JSONArray()).put("freeProgress",new JSONObject()))
                .put("trial",new JSONObject()).put("cardProtection",new JSONObject()));
        return s;
    }
    private static JSONObject next(JSONObject source,String hash) throws Exception {
        JSONObject s=new JSONObject(source.toString()),m=s.getJSONObject("meta");long seq=m.getLong("revision")+1;
        m.put("revision",seq).put("commandSeq",seq).put("lastCommit",new JSONObject().put("commandId","cmd-"+seq).put("payloadHash",hash).put("summary","出售完成"));
        return s;
    }
    private static boolean failsCommit(Context c,String raw,long revision){try{SaveRepository.commit(c,raw,revision);return false;}catch(Exception expected){return true;}}
    public static void main(String[] args) throws Exception{
        root=Path.of(args[0]);Files.createDirectories(root);
        String first=state(2,600),second=state(2,700),third=state(2,800);
        Context c=context();check(SaveRepository.load(c).getString("status").equals("empty"),"empty storage explicit");
        SaveRepository.save(c,first,false);check(SaveRepository.load(c).getString("raw").equals(first),"first save round trip");
        SaveRepository.save(c,second,false);check(SaveRepository.readState(c).getInt("cp")==700,"latest state loaded");
        check(new JSONObject(Files.readString(file(c,"previous"))).getString("raw").equals(first),"previous save retained");
        SaveRepository.save(c,second,false);check(new JSONObject(Files.readString(file(c,"previous"))).getString("raw").equals(first),"duplicate save does not replace previous");
        Files.writeString(file(c,"current"),"{truncated");
        JSONObject recovered=SaveRepository.load(c);check(recovered.getString("status").equals("recovered"),"broken current falls back");
        check(recovered.getString("raw").equals(first),"fallback uses verified previous");
        SaveRepository.save(c,third,false);check(SaveRepository.readState(c).getInt("cp")==800,"recovered save may advance");
        check(new JSONObject(Files.readString(file(c,"previous"))).getString("raw").equals(first),"recovery does not copy broken current over previous");

        c=context();SaveRepository.save(c,first,false);SaveRepository.save(c,second,false);
        String future=envelope(state(7,999),1);Files.writeString(file(c,"current"),future);
        check(SaveRepository.load(c).getString("status").equals("error"),"future save blocks legacy fallback");
        check(SaveRepository.load(c).getString("message").contains("较新版本"),"future save reason visible");
        check(failsSave(c,first,false),"automatic writes cannot overwrite future state");
        check(Files.readString(file(c,"current")).equals(future),"future file unchanged after rejected write");
        SaveRepository.save(c,first,true);
        check(Files.readString(file(c,"before-import-unreadable")).equals(future),"explicit import preserves future bytes");
        check(SaveRepository.readState(c).getInt("cp")==600,"explicit import usable afterwards");

        c=context();SaveRepository.save(c,first,false);
        Files.writeString(file(c,"current"),envelope(first,2));
        check(SaveRepository.load(c).getString("status").equals("error"),"future native envelope blocks loading");
        check(failsSave(c,second,false),"future native envelope blocks automatic writing");

        c=context();SaveRepository.save(c,first,false);SaveRepository.save(c,second,false);
        JSONObject tampered=new JSONObject(Files.readString(file(c,"current")));tampered.put("raw",third);
        Files.writeString(file(c,"current"),tampered.toString());
        check(SaveRepository.load(c).getString("status").equals("recovered"),"checksum mismatch triggers recovery");
        check(SaveRepository.load(c).getString("raw").equals(first),"tampered payload is never loaded");

        c=context();byte[] malformed={(byte)0xc3,0x28,(byte)0xff,0,0x7b};
        Files.createDirectories(file(c,"current").getParent());Files.write(file(c,"current"),malformed);
        check(SaveRepository.load(c).getString("status").equals("error"),"invalid UTF-8 damaged save blocked");
        check(failsSave(c,first,false),"damaged save with no fallback is protected");
        check(Arrays.equals(Files.readAllBytes(file(c,"current")),malformed),"rejected automatic write leaves bytes untouched");
        SaveRepository.save(c,first,true);
        check(Arrays.equals(Files.readAllBytes(file(c,"before-import-unreadable")),malformed),"import preserves invalid UTF-8 byte-for-byte");
        check(SaveRepository.load(c).getString("raw").equals(first),"valid import recovers damaged-only installation");

        c=context();String large=new JSONObject(first).put("padding","\"".repeat(600000)).toString();
        check(large.getBytes(StandardCharsets.UTF_8).length<SaveRepository.MAX_BYTES,"large raw fixture within external limit");
        SaveRepository.save(c,large,false);
        check(Files.size(file(c,"current"))>SaveRepository.MAX_BYTES,"envelope expands beyond raw size limit");
        check(SaveRepository.load(c).getString("raw").equals(large),"large escaped envelope remains readable");
        byte[] oversized=new byte[SaveRepository.MAX_BYTES+1];boolean tooLarge=false;
        try{SaveRepository.readBounded(new ByteArrayInputStream(oversized));}catch(Exception expected){tooLarge=true;}
        check(tooLarge,"SAF input size remains bounded");
        byte[] chinese="鸡".repeat(800000).getBytes(StandardCharsets.UTF_8);tooLarge=false;
        try{SaveRepository.readBounded(new ByteArrayInputStream(chinese));}catch(Exception expected){tooLarge=true;}
        check(tooLarge,"external limit counts UTF-8 bytes");

        c=context();SaveRepository.save(c,first,false);
        for(Object bad:new Object[]{"2",2.5,-1}) {
            check(failsSave(c,new JSONObject(first).put("version",bad).toString(),false),"invalid version rejected: "+bad);
        }
        check(failsSave(c,new JSONObject(first).put("cp","NaN").toString(),false),"NaN-string CP rejected");
        check(failsSave(c,new JSONObject(first).put("cp",-1).toString(),false),"negative CP rejected");
        check(SaveRepository.load(c).getString("raw").equals(first),"validation errors leave existing save unchanged");
        check(failsSave(c,state(7,600),true),"cannot import unsupported future raw state");
        SaveRepository.save(c,state(1,650),true);check(SaveRepository.readState(c).getInt("version")==1,"legacy eight-tool state still accepted for JS migration");

        c=context();SaveRepository.save(c,first,false);AtomicFile.failFinish=true;
        check(failsSave(c,second,false),"atomic write failure surfaced");AtomicFile.failFinish=false;
        check(SaveRepository.load(c).getString("raw").equals(first),"failed atomic write retains prior save");
        SaveRepository.save(c,second,true);
        check(new JSONObject(Files.readString(file(c,"before-import"))).getString("raw").equals(first),"valid pre-import save preserved separately");

        c=context();check(failsBackup(c),"update cannot back up empty storage as a new game");
        SaveRepository.save(c,first,false);SaveRepository.save(c,second,false);
        byte[] currentBefore=Files.readAllBytes(file(c,"current")),previousBefore=Files.readAllBytes(file(c,"previous"));
        JSONObject backup=new JSONObject(SaveRepository.updateBackup(c,"1.4.7",14,1234L));
        check(backup.getString("format").equals("chick-kitchen")&&backup.getInt("formatVersion")==1,"update backup uses portable import format");
        check(backup.getString("exportedAt").equals("1970-01-01T00:00:01.234Z"),"backup date is UTC");
        check(backup.getJSONObject("save").getInt("cp")==700,"latest persisted save exported");
        JSONObject protocol=backup.getJSONObject("updateBackup");
        check(protocol.getInt("protocol")==1&&protocol.getInt("versionCode")==14,"protocol identifies source version");
        check(protocol.getString("sourceSha256").equals(new JSONObject(new String(currentBefore,StandardCharsets.UTF_8)).getString("sha256")),"snapshot carries verified source digest");
        check(Arrays.equals(Files.readAllBytes(file(c,"current")),currentBefore)&&Arrays.equals(Files.readAllBytes(file(c,"previous")),previousBefore),"export leaves both save generations untouched");
        Files.writeString(file(c,"current"),"{broken");
        check(failsBackup(c),"update refuses recovered-only snapshot instead of silently using older progress");
        check(Files.readString(file(c,"current")).equals("{broken"),"failed backup leaves damaged file untouched");
        Files.writeString(file(c,"current"),future);
        check(failsBackup(c),"update refuses future save");
        JSONObject fixtures=new JSONObject(Files.readString(Path.of(args[1])));
        JSONArray cases=fixtures.getJSONArray("cases");
        for(int i=0;i<cases.length();i++){
            JSONObject entry=cases.getJSONObject(i);
            c=context();String original=entry.getJSONObject("state").toString();
            SaveRepository.save(c,original,false);
            check(SaveRepository.load(c).getString("raw").equals(original),"shared fixture native byte round trip: "+entry.getString("name"));
            String migrated=entry.getJSONObject("normalized").toString();
            SaveRepository.save(c,migrated,false);
            check(SaveRepository.load(c).getString("raw").equals(migrated),"shared fixture migrated batch and events preserved");
            JSONObject exported=new JSONObject(SaveRepository.updateBackup(c,"1.4.7",14,fixtures.getLong("now")));
            check(exported.getJSONObject("save").toString().equals(migrated),"shared fixture update export preserves all fields");
        }
        JSONObject running=cases.getJSONObject(2).getJSONObject("normalized");
        c=context();String safe=running.toString();SaveRepository.save(c,safe,false);
        JSONObject bad=new JSONObject(safe);bad.getJSONObject("farm").put("0:0",0);
        check(failsSave(c,bad.toString(),false),"schema-3 reservation cannot exceed inventory");
        bad=new JSONObject(safe);bad.getJSONObject("progress").getJSONObject("trade").put("credits",7);
        check(failsSave(c,bad.toString(),false),"schema-3 excessive basket credits rejected");
        bad=new JSONObject(safe);bad.getJSONObject("farm").put("0:0",1);bad.getJSONObject("progress").getJSONObject("trip").getJSONArray("members").put("0:0");
        check(failsSave(c,bad.toString(),false),"schema-3 team places beyond the partners at home rejected");
        bad=new JSONObject(safe);bad.getJSONObject("progress").getJSONObject("trip").put("clueProcessed",true);
        check(failsSave(c,bad.toString(),false),"schema-3 premature reward rejected");
        check(SaveRepository.load(c).getString("raw").equals(safe),"failed schema-3 writes preserve current bytes");
        JSONObject expanded=v4(running);
        c=context();SaveRepository.save(c,safe,false);byte[] legacyBytes=Files.readAllBytes(file(c,"current"));
        SaveRepository.save(c,expanded.toString(),false);
        check(Arrays.equals(Files.readAllBytes(file(c,"before-upgrade-v3")),legacyBytes),"schema upgrade retains byte-exact immutable original envelope");
        check(SaveRepository.load(c).getString("raw").equals(expanded.toString()),"schema-4 legacy active trip round trip");
        JSONObject committed=next(expanded,"1234abcd");committed.put("cp",expanded.getLong("cp")+3);
        check(SaveRepository.commit(c,committed.toString(),0).getBoolean("committed"),"first command commits revision one");
        check(SaveRepository.commit(c,committed.toString(),0).getBoolean("duplicate"),"ACK-loss retry returns committed receipt");
        check(Arrays.equals(Files.readAllBytes(file(c,"before-upgrade-v3")),legacyBytes),"rolling transaction leaves upgrade copy untouched");
        bad=new JSONObject(committed.toString());bad.put("cp",bad.getLong("cp")+5);
        check(failsCommit(c,bad.toString(),0),"same command identity cannot overwrite different assets");
        bad=new JSONObject(committed.toString());bad.getJSONObject("meta").getJSONObject("lastCommit").put("payloadHash","aaaaaaaa");
        check(failsCommit(c,bad.toString(),0),"same revision with different payload hash conflicts");
        check(failsCommit(c,next(committed,"87654321").toString(),0),"stale expected revision conflicts");
        check(failsSave(c,expanded.toString(),false),"legacy bridge cannot overwrite newer revision");
        check(failsSave(c,safe,false),"legacy bridge cannot downgrade expanded schema");
        JSONObject newest=next(committed,"87654321");
        AtomicFile.failFinish=true;check(failsCommit(c,newest.toString(),1),"atomic failure rejects candidate command");AtomicFile.failFinish=false;
        check(SaveRepository.load(c).getString("raw").equals(committed.toString()),"failed command keeps durable revision and CP");
        SaveRepository.commit(c,newest.toString(),1);
        check(SaveRepository.readState(c).getJSONObject("meta").getLong("revision")==2,"same candidate retry succeeds after definite failure");
        for(String field:new String[]{"meta","clock","expansion","contentRevision"}) {
            bad=new JSONObject(expanded.toString());bad.remove(field);
            check(failsSave(context(),bad.toString(),false),"schema-4 requires "+field);
        }
        bad=new JSONObject(expanded.toString());bad.getJSONObject("meta").getJSONObject("rng").put("seed",4294967296L);
        check(failsSave(context(),bad.toString(),false),"RNG seed limited to unsigned 32 bit");
        bad=new JSONObject(expanded.toString());bad.getJSONObject("clock").put("logicalAt",0);
        check(failsSave(context(),bad.toString(),false),"logical clock cannot precede wall clock");
        bad=new JSONObject(expanded.toString());bad.getJSONObject("expansion").getJSONObject("discovery").getJSONObject("cards").put("GUIDE-B",1);
        check(failsSave(context(),bad.toString(),false),"guide cannot masquerade as discovery card");
        bad=new JSONObject(expanded.toString());bad.getJSONObject("expansion").getJSONObject("trial").put("REC-V-C1",new JSONObject().put("failedFullBatches",4).put("owed",true).put("attemptSeq",4));
        check(failsSave(context(),bad.toString(),false),"trial failure counter respects fourth-attempt protection");
        bad=new JSONObject(expanded.toString());bad.getJSONObject("expansion").getJSONObject("regions").getJSONArray("opened").put("V").put("V");
        check(failsSave(context(),bad.toString(),false),"duplicate region IDs rejected");
        JSONObject allIdentities=new JSONObject(expanded.toString());
        for(int egg=0;egg<2;egg++)for(int id=0;id<(egg==0?152:89);id++)allIdentities.getJSONObject("total").put(egg+":"+id,1);
        for(int id=0;id<83;id++)allIdentities.getJSONObject("ingredients").put(""+id,0);
        c=context();SaveRepository.save(c,allIdentities.toString(),false);
        check(SaveRepository.readState(c).getJSONObject("total").length()==241,"all 241 identities and 83 material IDs accepted");
        for(String key:new String[]{"0:152","1:89","0:01","2:0"}) {
            bad=new JSONObject(expanded.toString());bad.getJSONObject("farm").put(key,1);
            check(failsSave(context(),bad.toString(),false),"invalid expanded identity rejected: "+key);
        }
        bad=new JSONObject(safe);bad.getJSONObject("farm").put("0:128",1);
        check(failsSave(context(),bad.toString(),false),"legacy193 identity boundary frozen");
        bad=new JSONObject(first);bad.getJSONObject("ingredients").put("75",1);
        check(failsSave(context(),bad.toString(),false),"legacy75 material boundary frozen");
        bad=new JSONObject(state(1,600));bad.getJSONObject("farm").put("0:114",1);
        check(failsSave(context(),bad.toString(),false),"schema1 original171 identity boundary frozen");
        bad=new JSONObject(expanded.toString());bad.put("padding","鸡".repeat(800000));
        check(failsSave(context(),bad.toString(),false),"UTF-8 save limit enforced before writing schema4");
        c=context();SaveRepository.save(c,expanded.toString(),false);
        JSONObject bridge=SaveBridge.write(c,committed.toString(),false,0L,state->{throw new java.io.IOException("notification denied");});
        check(bridge.getBoolean("ok")&&bridge.getBoolean("committed")&&bridge.has("notificationWarning"),"notification failure retains successful durable ACK");
        check(SaveRepository.load(c).getString("raw").equals(committed.toString()),"notification failure cannot roll back CP transaction");
        bridge=SaveBridge.write(c,committed.toString(),false,0L,state->{});
        check(bridge.getBoolean("duplicate")&&SaveRepository.readState(c).getLong("cp")==committed.getLong("cp"),"lost bridge ACK retry neither duplicates nor rolls back CP");
        final boolean[] notified={false};
        bridge=SaveBridge.write(c,newest.toString(),false,0L,state->{notified[0]=true;});
        check(!bridge.getBoolean("ok")&&bridge.getString("code").equals("REVISION_CONFLICT")&&!notified[0],"revision failure never schedules notifications");
        c=context();SaveRepository.save(c,safe,false);legacyBytes=Files.readAllBytes(file(c,"current"));
        AtomicFile.failFileName="before-upgrade-v3.json";
        check(failsSave(c,expanded.toString(),false),"upgrade blocks when byte-exact rescue copy cannot commit");AtomicFile.failFileName=null;
        check(Arrays.equals(Files.readAllBytes(file(c,"current")),legacyBytes),"failed rescue copy leaves original main bytes untouched");
        SaveRepository.save(c,expanded.toString(),false);
        AtomicFile.failFileName="current.json";
        check(failsCommit(c,committed.toString(),0),"failure after previous generation write is not a commit");AtomicFile.failFileName=null;
        check(SaveRepository.load(c).getString("raw").equals(expanded.toString()),"current-write failure retains old revision");
        check(new JSONObject(Files.readString(file(c,"previous"))).getString("raw").equals(expanded.toString()),"previous generation remains readable after current-write failure");
        SaveRepository.commit(c,committed.toString(),0);
        check(SaveRepository.readState(c).getJSONObject("meta").getLong("revision")==1,"retry after current-write failure commits exactly once");
        c=context();SaveRepository.save(c,committed.toString(),true);
        check(SaveRepository.load(c).getString("raw").equals(committed.toString()),"portable backup with nonzero revision imports into empty install");
        JSONArray commandCases=new JSONObject(Files.readString(Path.of(args[2]))).getJSONArray("cases");
        for(int i=0;i<commandCases.length();i++) {
            JSONObject scenario=commandCases.getJSONObject(i);c=context();byte[] sourceBytes=null;
            if(!scenario.isNull("source")) {
                SaveRepository.save(c,scenario.getJSONObject("source").toString(),false);
                sourceBytes=Files.readAllBytes(file(c,"current"));
            }
            JSONArray writes=scenario.getJSONArray("writes");
            for(int j=0;j<writes.length();j++) {
                JSONObject write=writes.getJSONObject(j);String raw=write.getJSONObject("state").toString();
                long expected=write.getLong("expectedRevision");
                JSONObject ack=SaveBridge.write(c,raw,false,expected,state->{});
                check(ack.getBoolean("ok")&&ack.getLong("revision")==j+1,"real Web command persisted: "+scenario.getString("name")+" / "+j);
                check(SaveRepository.load(c).getString("raw").equals(raw),"real Web command exact CP and inventory round trip");
                check(SaveRepository.commit(c,raw,expected).getBoolean("duplicate"),"real Web command retry is idempotent");
            }
            if(sourceBytes!=null)check(Arrays.equals(Files.readAllBytes(file(c,"before-upgrade-v"+scenario.getJSONObject("source").getInt("version"))),sourceBytes),"real first Web command preserves pre-upgrade source bytes");
        }
        c=context();check(failsCommit(c,committed.toString(),1),"first revision one requires expected zero");
        check(failsCommit(c,newest.toString(),0),"fresh install cannot skip first revision");
        for(Object value:new Object[]{0.5,9007199254740992d,"600"}) {
            bad=new JSONObject(expanded.toString()).put("cp",value);
            check(failsSave(context(),bad.toString(),true),"CP must be a safe integer: "+value);
        }
        for(Object value:new Object[]{-1,4,1.5,"2"}) {
            bad=new JSONObject(expanded.toString()).put("kitchenLevel",value);
            check(failsSave(context(),bad.toString(),true),"kitchen level boundary: "+value);
        }
        for(Object value:new Object[]{-2,3,0.5,"1"}) {
            bad=new JSONObject(expanded.toString());bad.getJSONArray("toolLevels").put(0,value);
            check(failsSave(context(),bad.toString(),true),"tool level boundary: "+value);
        }
        for(String field:new String[]{"lastSeen","lastClean","farmFixed","farmChecked"})for(Object value:new Object[]{-1,9007199254740992d,"123"}) {
            bad=new JSONObject(expanded.toString()).put(field,value);
            check(failsSave(context(),bad.toString(),true),"root time number boundary: "+field+" / "+value);
        }
        for(String field:new String[]{"selected","egg","duck","lastClean","farmFixed","farmChecked","batch","dirty","alarm","music","sound","events"}) {
            bad=new JSONObject(expanded.toString());bad.remove(field);
            check(failsSave(context(),bad.toString(),true),"schema4 missing required root field: "+field);
        }
        JSONArray regionalCases=new JSONObject(Files.readString(Path.of(args[2]))).getJSONArray("regional");
        for(int i=0;i<regionalCases.length();i++) {
            JSONObject entry=regionalCases.getJSONObject(i);String regionalRaw=entry.getJSONObject("state").toString();c=context();
            SaveRepository.save(c,regionalRaw,true);
            check(SaveRepository.load(c).getString("raw").equals(regionalRaw),"real regional Web command byte round trip: "+entry.getString("name"));
        }
        JSONObject regionalRunning=regionalCases.getJSONObject(0).getJSONObject("state");
        bad=new JSONObject(regionalRunning.toString());bad.getJSONObject("progress").getJSONObject("trip").getJSONObject("regional").getJSONArray("candidates").getJSONObject(0).put("roll",1);
        check(failsSave(context(),bad.toString(),true),"regional card roll must be below one");
        bad=new JSONObject(regionalRunning.toString());bad.getJSONObject("progress").getJSONObject("trip").getJSONObject("regional").put("processed",true);
        check(failsSave(context(),bad.toString(),true),"regional card cannot settle while running");
        JSONObject regionalBatch=regionalCases.getJSONObject(regionalCases.length()-1).getJSONObject("state");
        bad=new JSONObject(regionalBatch.toString());bad.getJSONObject("batch").getJSONArray("eggs").getJSONObject(0).getJSONArray("tickets").put(0,1);
        check(failsSave(context(),bad.toString(),true),"batch3 mutation tickets reject upper endpoint");
        bad=new JSONObject(regionalBatch.toString());bad.getJSONObject("batch").getJSONObject("plan").put("targetKey","0:129");
        check(failsSave(context(),bad.toString(),true),"batch3 recipe and target identity must agree");
        bad=new JSONObject(regionalBatch.toString());bad.getJSONObject("batch").getJSONObject("plan").put("mode","legacy");
        check(failsSave(context(),bad.toString(),true),"batch3 legacy and regional guarantees cannot overlap");
        bad=new JSONObject(regionalBatch.toString());bad.getJSONObject("ingredients").put("0",37);
        check(failsSave(context(),bad.toString(),true),"single identification capacity does not exceed36");
        bad=new JSONObject(regionalBatch.toString());bad.getJSONObject("batch").getJSONObject("plan").getJSONArray("initialIds").put(0,4294967296L);
        check(failsSave(context(),bad.toString(),true),"batch initial IDs cannot wrap Java integer range");
        bad=new JSONObject(regionalRunning.toString());bad.getJSONObject("progress").getJSONObject("trip").getJSONArray("remaining").put(1,4294967296L);
        check(failsSave(context(),bad.toString(),true),"trip material IDs cannot wrap Java integer range");
        JSONArray valleyCases=new JSONObject(Files.readString(Path.of(args[2]))).getJSONArray("valley");
        check(valleyCases.length()>=6,"Work C valley trajectory generated through real Web commands");
        java.util.HashMap<String,JSONObject> valley=new java.util.HashMap<>();
        for(int i=0;i<valleyCases.length();i++) {
            JSONObject entry=valleyCases.getJSONObject(i);String raw=entry.getJSONObject("state").toString();c=context();
            SaveRepository.save(c,raw,true);check(SaveRepository.load(c).getString("raw").equals(raw),"valley Web command byte round trip: "+entry.getString("name"));
            valley.put(entry.getString("name"),entry.getJSONObject("state"));
        }
        JSONObject river=valley.get("river-depart");
        check(river.getJSONObject("progress").getJSONObject("trip").getJSONObject("regional").getString("regionId").equals("R"),"river fixture uses the water route");
        bad=new JSONObject(river.toString());bad.getJSONObject("progress").getJSONObject("trip").put("routeId","yard");
        check(failsSave(context(),bad.toString(),true),"a river ticket cannot claim the yard route");
        bad=new JSONObject(river.toString());bad.getJSONObject("progress").getJSONObject("trip").getJSONObject("regional").getJSONObject("intro").put("cardId","V-S1");
        check(failsSave(context(),bad.toString(),true),"an intro card must belong to the ticket's region");
        JSONObject sampled=valley.get("sampling-depart");
        bad=new JSONObject(sampled.toString());bad.getJSONObject("progress").getJSONObject("trip").getJSONObject("regional").getJSONObject("sampling").put("materialId",76);
        check(failsSave(context(),bad.toString(),true),"sampling material must be one of its frozen eligible IDs");
        bad=new JSONObject(sampled.toString());bad.getJSONObject("progress").getJSONObject("trip").getJSONArray("remaining").put(0,76);
        check(failsSave(context(),bad.toString(),true),"a regional material in the basket needs a frozen source slot");
        JSONObject alt=valley.get("batch3-ALT-V");
        bad=new JSONObject(alt.toString());bad.getJSONObject("batch").getJSONObject("plan").put("targetScheduled",true);
        check(failsSave(context(),bad.toString(),true),"a local alternative never schedules a guaranteed target");
        bad=new JSONObject(alt.toString());bad.getJSONObject("batch").getJSONArray("ingredients").put(0,9);
        check(failsSave(context(),bad.toString(),true),"a local alternative requires its exact regional ingredients");
        bad=new JSONObject(alt.toString());bad.getJSONObject("batch").getJSONObject("plan").put("recipeId","ALT-T");
        check(failsSave(context(),bad.toString(),true),"alternative identity must match target and tool");
        JSONObject steamer=valley.get("abandon-ALT-batch-start-steamer-C3");
        bad=new JSONObject(steamer.toString());bad.getJSONObject("batch").getJSONObject("plan").put("recipeId","REC-V-C1");
        check(failsSave(context(),bad.toString(),true),"regional steamer plan cannot claim another recipe");
        bad=new JSONObject(steamer.toString());bad.getJSONObject("batch").getJSONObject("plan").getJSONArray("initialIds").put(1,131);
        check(failsSave(context(),bad.toString(),true),"a second new identity cannot hide in the companions");
        bad=new JSONObject(steamer.toString());bad.getJSONObject("expansion").put("prepareMode",new JSONObject().put("kind","regional").put("recipeId","ALT-V"));
        check(failsSave(context(),bad.toString(),true),"prepare mode kind and identity must agree");
        JSONArray businessCases=new JSONObject(Files.readString(Path.of(args[2]))).getJSONArray("business");
        for(int i=0;i<businessCases.length();i++){JSONObject entry=businessCases.getJSONObject(i);String raw=entry.getJSONObject("state").toString();c=context();SaveRepository.save(c,raw,true);check(SaveRepository.load(c).getString("raw").equals(raw),"business Web command byte round trip: "+entry.getString("name"));}
        JSONObject openSession=businessCases.getJSONObject(1).getJSONObject("state");
        check(openSession.getJSONObject("expansion").getJSONObject("business").getJSONObject("active").getInt("totalSold")==12,"fixture sold two windows");
        bad=new JSONObject(openSession.toString());bad.getJSONObject("farm").put("0:0",11);
        check(failsSave(context(),bad.toString(),true),"S cannot exceed the stock actually at home (T>=R+S)");
        bad=new JSONObject(openSession.toString());bad.getJSONObject("expansion").getJSONObject("business").getJSONObject("active").getJSONObject("stock").put("0:0",13);
        check(failsSave(context(),bad.toString(),true),"unsold S plus sold must equal the frozen initial stock");
        bad=new JSONObject(openSession.toString());bad.getJSONObject("expansion").getJSONObject("business").getJSONObject("active").put("bonusSettled",true);
        check(failsSave(context(),bad.toString(),true),"an open session cannot have paid its basket bonus");
        bad=new JSONObject(openSession.toString());bad.getJSONObject("expansion").getJSONObject("business").getJSONObject("active").put("creditReserve",5);
        check(failsSave(context(),bad.toString(),true),"credit reservation cannot exceed held credits");
        bad=new JSONObject(openSession.toString());bad.getJSONObject("expansion").getJSONObject("business").getJSONObject("active").put("hardEndAt",openSession.getJSONObject("expansion").getJSONObject("business").getJSONObject("active").getLong("startAt")+90000000L);
        check(failsSave(context(),bad.toString(),true),"a session lasts exactly 24 hours");
        bad=new JSONObject(openSession.toString());bad.getJSONObject("expansion").getJSONObject("business").getJSONObject("active").getJSONObject("initialStock").put("0:1",1);bad.getJSONObject("expansion").getJSONObject("business").getJSONObject("active").getJSONObject("stock").put("0:1",1);
        check(failsSave(context(),bad.toString(),true),"inedible species can never be business stock");
        JSONObject closed=businessCases.getJSONObject(2).getJSONObject("state");
        check(closed.getJSONObject("expansion").getJSONObject("business").isNull("active")&&closed.getJSONObject("expansion").getJSONObject("business").getJSONObject("lastReport").getInt("income")==92,"fixture sold out for 92 CP (rules 2: 鸡宝 alone is no complete menu, so no menu bonus)");
        JSONObject legacy=businessCases.getJSONObject(4).getJSONObject("state");
        check(legacy.getJSONObject("expansion").getJSONObject("business").getJSONObject("lastReport").getInt("rulesVersion")==1&&legacy.getJSONObject("expansion").getJSONObject("business").getJSONObject("lastReport").getInt("income")==95,"a rules 1 session still settles the audited 95 CP");
        bad=new JSONObject(closed.toString());bad.getJSONObject("expansion").getJSONObject("business").getJSONObject("lastReport").put("income",96);
        check(failsSave(context(),bad.toString(),true),"receipt income equals its itemized parts");
        bad=new JSONObject(closed.toString());bad.getJSONObject("expansion").put("business",JSONObject.NULL);
        check(failsSave(context(),bad.toString(),true),"schema 5 requires the business container");
        JSONArray orderCases=new JSONObject(Files.readString(Path.of(args[2]))).getJSONArray("orders");
        for(int i=0;i<orderCases.length();i++){JSONObject entry=orderCases.getJSONObject(i);String raw=entry.getJSONObject("state").toString();c=context();SaveRepository.save(c,raw,true);check(SaveRepository.load(c).getString("raw").equals(raw),"order Web command byte round trip: "+entry.getString("name"));}
        JSONObject held=null,lostQ=null;
        for(int i=0;i<orderCases.length();i++){
            JSONObject entry=orderCases.getJSONObject(i);
            if(entry.getString("name").equals("deliver-first-six"))held=entry.getJSONObject("state");
            if(entry.getString("name").equals("complete-Q-loss"))lostQ=entry.getJSONObject("state");
        }
        check(held!=null&&lostQ!=null,"named partial and complete-loss order fixtures exist");
        check(lostQ.getJSONObject("expansion").getJSONObject("orders").getJSONArray("active").getJSONObject(0).getJSONObject("reserved").length()==0&&lostQ.getJSONObject("expansion").getJSONObject("orders").getJSONArray("active").getJSONObject(0).getBoolean("needsRestock"),"complete Q loss removes zero reservation and marks restock");
        bad=new JSONObject(held.toString());bad.getJSONObject("farm").put("0:3",5);
        check(failsSave(context(),bad.toString(),true),"order reservation Q cannot exceed stock at home (T>=R+S+Q)");
        bad=new JSONObject(held.toString());bad.getJSONObject("expansion").getJSONObject("orders").getJSONArray("active").getJSONObject(0).put("bonusCP",99);
        check(failsSave(context(),bad.toString(),true),"an accepted order keeps its frozen bonus");
        bad=new JSONObject(held.toString());bad.getJSONObject("expansion").getJSONObject("orders").getJSONArray("active").getJSONObject(0).getJSONArray("groups").getJSONObject(0).getJSONObject("delivered").put("0:0",12);
        check(failsSave(context(),bad.toString(),true),"a fully delivered order cannot remain active");
        bad=new JSONObject(held.toString());bad.getJSONObject("expansion").getJSONObject("orders").getJSONArray("active").getJSONObject(0).getJSONArray("groups").getJSONObject(0).getJSONObject("delivered").put("1:0",1);
        check(failsSave(context(),bad.toString(),true),"deliveries only use the frozen allowed species");
        JSONArray bayCases=new JSONObject(Files.readString(Path.of(args[2]))).getJSONArray("bay");
        java.util.HashMap<String,JSONObject> bayStates=new java.util.HashMap<>();
        for(int i=0;i<bayCases.length();i++){JSONObject entry=bayCases.getJSONObject(i);String raw=entry.getJSONObject("state").toString();c=context();SaveRepository.save(c,raw,true);check(SaveRepository.load(c).getString("raw").equals(raw),"bay Web command byte round trip: "+entry.getString("name"));bayStates.put(entry.getString("name"),entry.getJSONObject("state"));}
        check(bayStates.get("guide-return").getJSONObject("expansion").getJSONObject("regions").getJSONArray("guideFlags").getString(0).equals("GUIDE-B"),"a complete river trip earned the guide");
        JSONObject carrying=bayStates.get("cargo-depart");
        check(carrying.getJSONObject("progress").getJSONObject("trip").getString("routeId").equals("bay"),"cargo fixture runs on the 12h bay route");
        bad=new JSONObject(carrying.toString());bad.getJSONObject("farm").put("0:3",2);
        check(failsSave(context(),bad.toString(),true),"carried cargo counts in R and cannot exceed T");
        bad=new JSONObject(carrying.toString());bad.getJSONObject("progress").getJSONObject("trip").getJSONObject("cargo").getJSONObject("selection").put("0:3",4);
        check(failsSave(context(),bad.toString(),true),"cargo contract carries exactly six birds");
        bad=new JSONObject(carrying.toString());bad.getJSONObject("progress").getJSONObject("trip").getJSONObject("cargo").put("processed",true).put("outcome","exchanged");
        check(failsSave(context(),bad.toString(),true),"cargo cannot be consumed before a complete return");
        bad=new JSONObject(carrying.toString());bad.getJSONObject("progress").getJSONObject("trip").put("endAt",bad.getJSONObject("progress").getJSONObject("trip").getLong("startedAt")+6*3600000L);
        check(failsSave(context(),bad.toString(),true),"the bay route lasts 12 hours");
        bad=new JSONObject(bayStates.get("guide-depart").toString());bad.getJSONObject("progress").getJSONObject("trip").getJSONObject("regional").put("placeId","R:0");
        check(failsSave(context(),bad.toString(),true),"the guide is only followed from the river stall");
        JSONObject v6=bayStates.get("cargo-return");
        check(v6.getInt("version")==6&&v6.getJSONObject("expansion").has("collections"),"current Web saves are schema 6");
        bad=new JSONObject(v6.toString());bad.getJSONObject("expansion").getJSONObject("collections").getJSONObject("entitlements").put("M13",new JSONObject().put("seq",1).put("source","x"));
        check(failsSave(context(),bad.toString(),true),"unknown collection results are rejected");
        bad=new JSONObject(v6.toString());bad.getJSONObject("expansion").getJSONObject("collections").put("display",new JSONArray().put("M01").put(JSONObject.NULL).put(JSONObject.NULL));
        check(v6.getJSONObject("expansion").getJSONObject("collections").getJSONObject("entitlements").has("M01")||failsSave(context(),bad.toString(),true),"only owned mementos can be displayed");
        bad=new JSONObject(v6.toString());bad.getJSONObject("expansion").getJSONObject("collections").put("display",new JSONArray().put(JSONObject.NULL).put(JSONObject.NULL));
        check(failsSave(context(),bad.toString(),true),"there are exactly three display slots");
        bad=new JSONObject(v6.toString());bad.getJSONObject("expansion").remove("collections");
        check(failsSave(context(),bad.toString(),true),"schema 6 requires the collections container");
        JSONArray regularCases=new JSONObject(Files.readString(Path.of(args[2]))).getJSONArray("regulars");
        java.util.HashMap<String,JSONObject> regularStates=new java.util.HashMap<>();
        for(int i=0;i<regularCases.length();i++){JSONObject entry=regularCases.getJSONObject(i);String raw=entry.getJSONObject("state").toString();c=context();SaveRepository.save(c,raw,true);check(SaveRepository.load(c).getString("raw").equals(raw),"regular Web command byte round trip: "+entry.getString("name"));regularStates.put(entry.getString("name"),entry.getJSONObject("state"));}
        JSONObject visited=regularStates.get("mn1-visitor"),readOne=regularStates.get("read-RG1-1");
        check(visited.getJSONObject("expansion").getJSONObject("regulars").getJSONObject("RG1").getJSONObject("pendingStage").getString("id").equals("RG1-1"),"a real MN1 visitor queued RG1-1");
        bad=new JSONObject(visited.toString());bad.getJSONObject("expansion").getJSONObject("collections").getJSONObject("entitlements").remove("NOTE-RG1-1");
        check(failsSave(context(),bad.toString(),true),"an unread regular stage must already hold its note");
        bad=new JSONObject(readOne.toString());bad.getJSONObject("expansion").getJSONObject("regulars").getJSONObject("RG1").put("readStages",new JSONArray().put("RG1-2"));
        check(failsSave(context(),bad.toString(),true),"regular stages are read in order");
        bad=new JSONObject(readOne.toString());bad.getJSONObject("expansion").getJSONObject("regulars").put("RG9",new JSONObject(readOne.getJSONObject("expansion").getJSONObject("regulars").getJSONObject("RG1").toString()));
        check(failsSave(context(),bad.toString(),true),"unknown regulars are rejected");
        bad=new JSONObject(readOne.toString());bad.getJSONObject("expansion").getJSONObject("regulars").getJSONObject("RG1").put("baselines",new JSONObject().put("O05",1));
        check(failsSave(context(),bad.toString(),true),"only RG2-4 keeps an activation baseline");
        bad=new JSONObject(readOne.toString());bad.getJSONObject("expansion").getJSONObject("regulars").getJSONObject("RG1").put("pendingStage",new JSONObject().put("id","RG1-3").put("seq",1).put("branch",0));
        check(failsSave(context(),bad.toString(),true),"only the next stage can be unread");
        bad=new JSONObject(regularStates.get("mn1-open").toString());bad.getJSONObject("expansion").getJSONObject("business").getJSONObject("active").put("visitorCandidates",new JSONArray().put("RG9"));
        check(failsSave(context(),bad.toString(),true),"visitor candidates are frozen regular identities");
        JSONArray projectCases=new JSONObject(Files.readString(Path.of(args[2]))).getJSONArray("projects");
        java.util.HashMap<String,JSONObject> projectStates=new java.util.HashMap<>();
        for(int i=0;i<projectCases.length();i++){JSONObject entry=projectCases.getJSONObject(i);String raw=entry.getJSONObject("state").toString();c=context();SaveRepository.save(c,raw,true);check(SaveRepository.load(c).getString("raw").equals(raw),"project Web command byte round trip: "+entry.getString("name"));projectStates.put(entry.getString("name"),entry.getJSONObject("state"));}
        JSONObject partial=projectStates.get("pj2-b-deliver-4");
        check(partial.getJSONObject("expansion").getJSONObject("projects").getJSONObject("PJ-1").getJSONObject("payments").getInt("PJ-1-C")==200,"PJ-1 paid its 200 CP stage once");
        bad=new JSONObject(partial.toString());bad.getJSONObject("expansion").getJSONObject("projects").getJSONObject("PJ-2").getJSONObject("payments").put("PJ-2-A",99);
        check(failsSave(context(),bad.toString(),true),"project payments equal the fixed stage cost");
        bad=new JSONObject(partial.toString());bad.getJSONObject("expansion").getJSONObject("projects").getJSONObject("PJ-2").getJSONObject("deliveries").getJSONObject("PJ-2-B").put("0:8",1);
        check(failsSave(context(),bad.toString(),true),"deliveries only use the locked kinds");
        bad=new JSONObject(partial.toString());bad.getJSONObject("expansion").getJSONObject("projects").getJSONObject("PJ-2").getJSONObject("stages").put("PJ-2-B",new JSONObject().put("complete",true).put("seq",1));
        check(failsSave(context(),bad.toString(),true),"a delivery stage cannot complete before it is full");
        bad=new JSONObject(partial.toString());bad.getJSONObject("expansion").getJSONObject("projects").getJSONObject("PJ-2").getJSONObject("stages").put("PJ-2-C",new JSONObject().put("complete",true).put("seq",1));
        check(failsSave(context(),bad.toString(),true),"project stages complete in order");
        bad=new JSONObject(projectStates.get("pj1-b").toString());bad.getJSONObject("expansion").getJSONObject("menus").getJSONArray("presets").put(new JSONObject().put("menuId","MN1").put("stock",new JSONObject().put("0:0",6)));
        check(failsSave(context(),bad.toString(),true),"menu presets need the finished PJ-1");
        bad=new JSONObject(partial.toString());bad.getJSONObject("expansion").getJSONObject("projects").getJSONObject("PJ-2").getJSONObject("pinnedChoices").put("portraits",new JSONArray().put("0:0"));
        check(failsSave(context(),bad.toString(),true),"portraits belong only to PJ-4");
        JSONObject returnedFact=regionalCases.getJSONObject(1).getJSONObject("state");
        JSONObject actualHistory=returnedFact.getJSONObject("expansion").getJSONObject("regions").getJSONObject("history");
        check(actualHistory.getJSONObject("companionFacts").getJSONObject("0:0").getString("tripId").equals("trip-1"),"first companion fact comes from real Web completed trip");
        check(actualHistory.getJSONObject("cardFacts").getJSONObject("V-S1").getJSONArray("members").getJSONObject(0).getString("key").equals("0:0"),"card fact preserves actual companion snapshot");
        bad=new JSONObject(returnedFact.toString());bad.getJSONObject("expansion").getJSONObject("regions").getJSONObject("history").getJSONObject("companionFacts").getJSONObject("0:0").put("seq",bad.getJSONObject("meta").getLong("factSeq")+1);
        check(failsSave(context(),bad.toString(),true),"companion fact cannot refer to future sequence");
        bad=new JSONObject(returnedFact.toString());bad.getJSONObject("expansion").getJSONObject("regions").getJSONObject("history").getJSONObject("cardFacts").getJSONObject("V-S1").getJSONArray("members").getJSONObject(0).put("traits",new JSONArray().put("invented-trait"));
        check(failsSave(context(),bad.toString(),true),"card fact traits use frozen semantic vocabulary");
        bad=new JSONObject(regionalBatch.toString());bad.getJSONObject("batch").getJSONObject("plan").put("targetScheduled",false).put("roll",JSONObject.NULL);
        check(failsSave(context(),bad.toString(),true),"regional guarantee null roll must schedule target");
        bad=new JSONObject(regionalBatch.toString());bad.getJSONObject("batch").getJSONObject("plan").put("targetScheduled",true).put("roll",.25);
        check(failsSave(context(),bad.toString(),true),"regional probability at25 percent does not hit");
        bad=new JSONObject(regionalBatch.toString());bad.getJSONObject("batch").getJSONObject("plan").put("targetScheduled",true).put("roll",.1).put("mode","regional-repeat");
        check(failsSave(context(),bad.toString(),true),"repeat mode never uses a probability roll");
        bad=new JSONObject(regionalBatch.toString());bad.getJSONObject("batch").getJSONArray("ingredients").put(0);
        check(failsSave(context(),bad.toString(),true),"regional batch rejects extra recipe ingredients");
        bad=new JSONObject(regionalBatch.toString());bad.getJSONObject("batch").getJSONObject("plan").getJSONArray("initialIds").put(1,129);
        check(failsSave(context(),bad.toString(),true),"regional companion pool cannot grant another new identity");
        bad=new JSONObject(regionalBatch.toString());JSONObject legacyPlan=bad.getJSONObject("batch").getJSONObject("plan");legacyPlan.put("mode","legacy").put("recipeId",JSONObject.NULL).put("targetKey",JSONObject.NULL).put("targetScheduled",false).put("roll",JSONObject.NULL);legacyPlan.getJSONArray("initialIds").put(0,128);
        check(failsSave(context(),bad.toString(),true),"legacy initial pool rejects all regional identities");
        JSONArray invalidCases=new JSONObject(Files.readString(Path.of(args[2]))).getJSONArray("invalid");
        for(int i=0;i<invalidCases.length();i++){
            JSONObject entry=invalidCases.getJSONObject(i);c=context();String safeRaw=projectStates.get("preset-0").toString();SaveRepository.save(c,safeRaw,true);
            check(failsSave(c,entry.getJSONObject("state").toString(),true),"shared Web/Native rejection: "+entry.getString("name"));
            check(SaveRepository.load(c).getString("raw").equals(safeRaw),"invalid candidate preserves durable bytes: "+entry.getString("name"));
        }
        JSONArray matrixCases=new JSONObject(Files.readString(Path.of(args[2]))).getJSONArray("matrix");
        for(int i=0;i<matrixCases.length();i++){
            JSONObject entry=matrixCases.getJSONObject(i);c=context();String raw=entry.getJSONObject("state").toString();
            SaveRepository.save(c,raw,true);check(SaveRepository.load(c).getString("raw").equals(raw),"all-menu/order-variant contract matrix: "+entry.getString("name"));
        }
        JSONArray goldenCases=new JSONObject(Files.readString(Path.of(args[2]))).getJSONArray("golden");
        for(int i=0;i<goldenCases.length();i++){
            JSONObject entry=goldenCases.getJSONObject(i);c=context();String oldRaw=entry.getJSONObject("source").toString(),raw=entry.getJSONObject("state").toString();
            SaveRepository.save(c,oldRaw,true);check(SaveRepository.load(c).getString("raw").equals(oldRaw),"golden source accepted: "+entry.getString("name"));
            byte[] beforeUpgrade=Files.readAllBytes(file(c,"current"));SaveRepository.save(c,raw,true);
            check(SaveRepository.load(c).getString("raw").equals(raw),"golden schema 6 round trip: "+entry.getString("name"));
            check(Arrays.equals(beforeUpgrade,Files.readAllBytes(file(c,"before-upgrade-v"+entry.getJSONObject("source").getInt("version")))),"golden byte-exact upgrade backup: "+entry.getString("name"));
        }
        JSONArray liveUpgrades=new JSONObject(Files.readString(Path.of(args[2]))).getJSONArray("upgrades");
        for(int i=0;i<liveUpgrades.length();i++){
            JSONObject entry=liveUpgrades.getJSONObject(i);c=context();String oldRaw=entry.getJSONObject("source").toString(),raw=entry.getJSONObject("state").toString();
            SaveRepository.save(c,oldRaw,true);byte[] beforeUpgrade=Files.readAllBytes(file(c,"current"));
            check(SaveRepository.commit(c,raw,entry.getLong("expectedRevision")).getBoolean("committed"),"live schema5 upgrade commit: "+entry.getString("name"));
            check(SaveRepository.commit(c,raw,entry.getLong("expectedRevision")).getBoolean("duplicate"),"live schema5 upgrade ACK retry: "+entry.getString("name"));
            check(SaveRepository.load(c).getString("raw").equals(raw),"live schema5 frozen ticket round trip: "+entry.getString("name"));
            check(Arrays.equals(beforeUpgrade,Files.readAllBytes(file(c,"before-upgrade-v5"))),"live schema5 byte-exact backup: "+entry.getString("name"));
            JSONObject downgrade=new JSONObject(entry.getJSONObject("source").toString());
            downgrade.put("meta",next(entry.getJSONObject("state"),"1234abcd").getJSONObject("meta"));
            downgrade.getJSONObject("meta").getJSONArray("migrationHistory").remove(2);
            check(failsCommit(c,downgrade.toString(),entry.getJSONObject("state").getJSONObject("meta").getLong("revision")),"automatic commit cannot downgrade schema and drop progress: "+entry.getString("name"));
            check(SaveRepository.load(c).getString("raw").equals(raw),"rejected downgrade preserves current schema 6 bytes");
        }
        JSONArray loopCases=new JSONObject(Files.readString(Path.of(args[2]))).getJSONArray("loop");
        for(int i=0;i<loopCases.length();i++){
            JSONObject entry=loopCases.getJSONObject(i);String raw=entry.getJSONObject("state").toString();c=context();SaveRepository.save(c,raw,true);
            check(SaveRepository.load(c).getString("raw").equals(raw),"loop frozen ticket byte round trip: "+entry.getString("name"));
        }
        bad=new JSONObject(regionalBatch.toString());bad.getJSONObject("batch").getJSONObject("plan").put("chance",.99);
        check(failsSave(context(),bad.toString(),true),"per-egg chance outside released tiers rejected");
        System.out.println("SaveRepository: "+assertions+" behavioral assertions passed (AOSP JSON + real files; JVM AtomicFile adapter).");
    }
}
