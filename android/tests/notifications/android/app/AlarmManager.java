package android.app;
public class AlarmManager {
    public final java.util.Map<PendingIntent,Long> scheduled=new java.util.HashMap<>();
    public static final int RTC_WAKEUP=0;
    public static final String ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED="android.app.action.SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED";
    public boolean exactAllowed=true,denyExact=false,denyAll=false;
    public int exactCalls=0,inexactCalls=0,cancelCalls=0; public long due=0; public PendingIntent pending;
    public boolean canScheduleExactAlarms() { return exactAllowed; }
    public void setExactAndAllowWhileIdle(int type,long time,PendingIntent p) { exactCalls++;if(denyExact||denyAll)throw new SecurityException("denied");due=time;pending=p;scheduled.put(p,time); }
    public void setAndAllowWhileIdle(int type,long time,PendingIntent p) { inexactCalls++;if(denyAll)throw new IllegalStateException("blocked");due=time;pending=p;scheduled.put(p,time); }
    public void cancel(PendingIntent p) { cancelCalls++;if(pending==p)pending=null;scheduled.remove(p); }
}
