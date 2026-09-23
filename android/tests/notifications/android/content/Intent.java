package android.content;
import java.util.HashMap;
import java.util.Map;
public class Intent {
    public static final String ACTION_BOOT_COMPLETED="android.intent.action.BOOT_COMPLETED", ACTION_MY_PACKAGE_REPLACED="android.intent.action.MY_PACKAGE_REPLACED", ACTION_TIME_CHANGED="android.intent.action.TIME_SET", ACTION_TIMEZONE_CHANGED="android.intent.action.TIMEZONE_CHANGED";
    public static final int FLAG_ACTIVITY_CLEAR_TOP=1, FLAG_ACTIVITY_SINGLE_TOP=2;
    public String action; public Class<?> component; public int flags;
    public final Map<String,Object> extras=new HashMap<>();
    public Intent() {} public Intent(Context c, Class<?> target) { component=target; }
    public Intent setAction(String value) { action=value; return this; }
    public String getAction() { return action; }
    public Intent addFlags(int value) { flags|=value; return this; }
    public Intent putExtra(String key, Object value) { extras.put(key,value); return this; }
    public String getStringExtra(String key) { return (String)extras.get(key); }
}
