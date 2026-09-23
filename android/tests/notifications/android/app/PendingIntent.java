package android.app;
import android.content.Context;
import android.content.Intent;
import java.util.HashMap;
import java.util.Map;
public class PendingIntent {
    public static final int FLAG_NO_CREATE=1, FLAG_UPDATE_CURRENT=2, FLAG_IMMUTABLE=4;
    private static final Map<String,PendingIntent> entries=new HashMap<>();
    public Intent intent; public String key; public int flags;
    private static PendingIntent get(Context c,int request,Intent i,int f,String kind) {
        String key=System.identityHashCode(c)+":"+kind+":"+request+":"+i.component+":"+i.action;
        PendingIntent p=entries.get(key);
        if(p==null&&(f&FLAG_NO_CREATE)!=0)return null;
        if(p==null){p=new PendingIntent();p.key=key;entries.put(key,p);}
        if((f&FLAG_UPDATE_CURRENT)!=0||p.intent==null)p.intent=i;
        p.flags=f;return p;
    }
    public static PendingIntent getBroadcast(Context c,int r,Intent i,int f) { return get(c,r,i,f,"broadcast"); }
    public static PendingIntent getActivity(Context c,int r,Intent i,int f) { return get(c,r,i,f,"activity"); }
    public void cancel() { entries.remove(key); }
}
