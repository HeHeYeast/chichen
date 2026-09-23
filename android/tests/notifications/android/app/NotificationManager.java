package android.app;
import java.util.HashMap;
import java.util.Map;
public class NotificationManager {
    public static final int IMPORTANCE_DEFAULT=3,IMPORTANCE_NONE=0;
    public final Map<String,NotificationChannel> channels=new HashMap<>();
    public final Map<Integer,Notification> active=new HashMap<>();
    public boolean enabled=true,failNotify=false; public int notifyCalls=0;
    public boolean areNotificationsEnabled() { return enabled; }
    public void createNotificationChannel(NotificationChannel c) { channels.putIfAbsent(c.id,c); }
    public NotificationChannel getNotificationChannel(String id) { return channels.get(id); }
    public void notify(int id,Notification n) { if(failNotify)throw new SecurityException("denied");notifyCalls++;active.put(id,n); }
    public void cancel(int id) { active.remove(id); }
}
