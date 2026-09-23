package android.content;
import android.app.AlarmManager;
import android.app.NotificationManager;
public class Context {
    public static final int MODE_PRIVATE=0;
    public final SharedPreferences prefs=new SharedPreferences();
    public final AlarmManager alarms=new AlarmManager();
    public final NotificationManager notifications=new NotificationManager();
    public int permission=0;
    public Context getApplicationContext() { return this; }
    public SharedPreferences getSharedPreferences(String name,int mode) { return prefs; }
    public <T> T getSystemService(Class<T> type) { if(type==AlarmManager.class)return type.cast(alarms); if(type==NotificationManager.class)return type.cast(notifications); return null; }
    public int checkSelfPermission(String name) { return permission; }
}
