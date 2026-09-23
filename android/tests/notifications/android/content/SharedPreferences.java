package android.content;
import java.util.HashMap;
import java.util.Map;
public class SharedPreferences {
    public final Map<String,Object> values=new HashMap<>();
    public boolean commitsSucceed=true;
    public String getString(String k,String d) { return (String)values.getOrDefault(k,d); }
    public long getLong(String k,long d) { return (long)values.getOrDefault(k,d); }
    public boolean contains(String k) { return values.containsKey(k); }
    public Editor edit() { return new Editor(); }
    public final class Editor {
        private final Map<String,Object> changes=new HashMap<>();
        public Editor putString(String k,String v) { changes.put(k,v); return this; }
        public Editor putLong(String k,long v) { changes.put(k,v); return this; }
        public Editor remove(String k) { changes.put(k,null); return this; }
        public boolean commit() { if(!commitsSucceed)return false; changes.forEach((k,v)->{if(v==null)values.remove(k);else values.put(k,v);}); return true; }
    }
}
