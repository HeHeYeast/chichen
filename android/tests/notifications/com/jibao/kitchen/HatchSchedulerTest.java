package com.jibao.kitchen;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import org.json.JSONArray;
import org.json.JSONObject;

/** Executes the production scheduler against deterministic platform fakes; not device validation. */
public final class HatchSchedulerTest {
    private static int assertions=0;
    private static void check(boolean result,String description) { assertions++;if(!result)throw new AssertionError(description); }
    private static JSONObject state(long ends) throws Exception {
        return new JSONObject().put("alarm",true).put("batch",new JSONObject()
                .put("started",ends-60000).put("ends",ends).put("tool",0).put("level",0).put("egg",0)
                .put("eggs",new JSONArray().put(new JSONObject().put("openAt",ends-10000).put("collected",false))));
    }
    private static void save(Context c,JSONObject s) { SaveRepository.state=s;HatchScheduler.syncFromSave(c,s); }
    private static JSONObject status(Context c) { return HatchScheduler.status(c); }
    public static void main(String[] args) throws Exception {
        long future=System.currentTimeMillis()+120000;
        Context c=new Context();JSONObject s=state(future);save(c,s);
        check(c.alarms.exactCalls==1,"one exact alarm");
        check(c.alarms.due==future-7000,"actual last egg plus animation, before nominal cook time");
        PendingIntent initial=c.alarms.pending;
        check((initial.flags&PendingIntent.FLAG_IMMUTABLE)!=0,"alarm PendingIntent is immutable");
        save(c,s);check(c.alarms.exactCalls==1,"repeated save does not reschedule");
        HatchScheduler.restore(c);check(c.alarms.exactCalls==2,"restore rebuilds scheduling");
        check(status(c).optBoolean("scheduled",false),"status reports scheduled");
        s.optJSONObject("batch").optJSONArray("eggs").optJSONObject(0).put("openAt",future+20000);
        save(c,s);check(c.alarms.due==future+23000,"latest remaining egg is included");
        s.optJSONObject("batch").optJSONArray("eggs").optJSONObject(0).put("collected",true);
        save(c,s);check(c.alarms.pending==null,"fully collected batch cancels alarm");
        check(!status(c).optBoolean("scheduled",true),"fully collected status is not scheduled");

        c=new Context();s=state(future);c.alarms.exactAllowed=false;save(c,s);
        check(c.alarms.exactCalls==0&&c.alarms.inexactCalls==1,"inexact fallback without permission");
        check(status(c).optString("scheduleMode","").equals("inexact"),"status explains inexact scheduling");
        check(status(c).optString("message","").contains("延迟"),"delay disclosed");
        c.alarms.exactAllowed=true;HatchScheduler.restore(c);
        check(c.alarms.exactCalls==1,"permission grant upgrades schedule");
        c=new Context();c.alarms.denyExact=true;s=state(future);save(c,s);
        check(c.alarms.inexactCalls==1,"permission race falls back safely");
        check(status(c).optString("scheduleMode","").equals("inexact"),"fallback mode persisted");

        c=new Context();c.permission=-1;s=state(future);save(c,s);
        check(c.alarms.pending==null,"no alarm while notification permission denied");
        check(!status(c).optBoolean("permissionGranted",true),"runtime permission status");
        c.permission=0;HatchScheduler.restore(c);check(c.alarms.pending!=null,"permission grant restores reminder");
        c.notifications.channels.get(HatchScheduler.CHANNEL_ID).importance=NotificationManager.IMPORTANCE_NONE;
        save(c,s);check(c.alarms.pending==null,"disabled channel cancels alarm");
        check(!status(c).optBoolean("channelEnabled",true),"channel status distinguishes blocked channel");
        c.notifications.channels.get(HatchScheduler.CHANNEL_ID).importance=NotificationManager.IMPORTANCE_DEFAULT;
        c.notifications.enabled=false;save(c,s);
        check(!status(c).optBoolean("notificationsEnabled",true),"application notifications off");
        c.notifications.enabled=true;s.put("alarm",false);save(c,s);
        check(c.alarms.pending==null,"game setting off cancels alarm");

        c=new Context();s=state(System.currentTimeMillis()-10000);
        s.optJSONObject("batch").put("alarmed",true);save(c,s);
        check(c.notifications.notifyCalls==1,"past due batch posts even with front-end alarmed flag");
        check(status(c).optBoolean("delivered",false),"delivery history is independent of save");
        Notification n=c.notifications.active.values().iterator().next();
        check(Boolean.TRUE.equals(n.content.intent.extras.get("goKitchen")),"notification click opens kitchen");
        check(n.title.contains("孵化完成"),"notification title is useful");
        check(n.visibility==Notification.VISIBILITY_PUBLIC,"harmless reminder text is readable on the lock screen");
        check(c.notifications.channels.get(HatchScheduler.CHANNEL_ID).importance==NotificationManager.IMPORTANCE_HIGH,"reminder channel shows a heads-up banner");
        check(c.notifications.channels.get(HatchScheduler.CHANNEL_ID).vibration,"reminder vibrates");
        save(c,s);HatchScheduler.restore(c);
        check(c.notifications.notifyCalls==1,"repeated save/restore cannot duplicate delivery");
        HatchScheduler.cancel(c);save(c,s);
        check(c.notifications.notifyCalls==1,"disable/re-enable preserves delivered token");
        JSONObject newer=state(System.currentTimeMillis()-5000);save(c,newer);
        check(c.notifications.notifyCalls==2,"different batch may notify");
        save(c,s);check(c.notifications.notifyCalls==2,"restoring prior notified save does not duplicate");


        c=new Context();c.notifications.channels.put(HatchScheduler.LEGACY_CHANNEL_ID,new android.app.NotificationChannel(HatchScheduler.LEGACY_CHANNEL_ID,"old",NotificationManager.IMPORTANCE_DEFAULT));
        s=state(future);save(c,s);
        check(!c.notifications.channels.containsKey(HatchScheduler.LEGACY_CHANNEL_ID),"quiet v1 channel is removed after upgrade");
        check(!status(c).optBoolean("backgroundAllowed",true)&&status(c).optString("message","").contains("允许后台"),"battery-optimised status points to the background switch");
        c.power.ignoring=true;
        check(status(c).optBoolean("backgroundAllowed",false)&&status(c).optString("message","").equals("已安排孵化完成提醒。预计 "+new java.text.SimpleDateFormat("M月d日 HH:mm",java.util.Locale.CHINA).format(new java.util.Date(future-7000))+" 提醒（按本批实际孵化时间）。"),"exempt app gets the plain scheduled message");
        c=new Context();s=state(future);save(c,s);initial=c.alarms.pending;
        HatchScheduler.testNotification(c);
        check(c.notifications.notifyCalls==1,"test notification posts");
        check(c.alarms.pending==initial&&c.alarms.exactCalls==1,"test leaves real schedule untouched");
        check(!status(c).optBoolean("delivered",true),"test does not mark actual batch delivered");
        c.permission=-1;boolean blocked=false;try{HatchScheduler.testNotification(c);}catch(IllegalStateException expected){blocked=true;}
        check(blocked,"test explains permission denial");

        c=new Context();s=state(future);save(c,s);Intent old=c.alarms.pending.intent;
        JSONObject replacement=state(future+60000);save(c,replacement);
        new HatchAlarmReceiver().onReceive(c,old);
        check(c.notifications.notifyCalls==0&&c.alarms.due==future+53000,"stale old receiver cannot notify early or replace new batch");
        int before=c.alarms.exactCalls;
        new RestoreAlarmsReceiver().onReceive(c,new Intent().setAction("untrusted.action"));
        check(c.alarms.exactCalls==before,"restore rejects unrelated actions");
        for(String action:new String[]{Intent.ACTION_BOOT_COMPLETED,Intent.ACTION_MY_PACKAGE_REPLACED,Intent.ACTION_TIME_CHANGED,Intent.ACTION_TIMEZONE_CHANGED,AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED}) {
            new RestoreAlarmsReceiver().onReceive(c,new Intent().setAction(action));
        }
        check(c.alarms.exactCalls==before+5,"boot/update/time/exact-access restore paths");
        HatchScheduler.syncFromSave(c,null);check(c.alarms.pending==null,"missing save cancels stale schedule");

        c=new Context();s=state(System.currentTimeMillis()-10000);c.notifications.failNotify=true;save(c,s);
        check(!status(c).optBoolean("delivered",true),"failed notification does not consume token");
        c.notifications.failNotify=false;HatchScheduler.restore(c);
        check(c.notifications.notifyCalls==1,"notification failure can recover");
        c=new Context();s=state(future);c.alarms.denyAll=true;save(c,s);
        check(!status(c).optBoolean("scheduled",true),"scheduler failure not shown as success");
        check(status(c).optString("message","").contains("未安排成功"),"scheduler failure is visible");
        c.alarms.denyAll=false;HatchScheduler.restore(c);
        check(status(c).optBoolean("scheduled",false),"scheduler failure restores later");

        c=new Context();s=state(System.currentTimeMillis()-10000);c.prefs.commitsSucceed=false;save(c,s);
        check(c.notifications.notifyCalls==0,"delivery does not bypass failed durable token write");
        c.prefs.commitsSucceed=true;HatchScheduler.restore(c);
        check(c.notifications.notifyCalls==1,"delivery retries after durable storage recovers");
        c=new Context();s=state(Long.MAX_VALUE);save(c,s);
        check(c.alarms.pending==null&&c.notifications.notifyCalls==0,"overflowed timestamp is rejected");
        c=new Context();s=state(future);Build.VERSION.SDK_INT=26;c.permission=-1;save(c,s);
        check(c.alarms.exactCalls==1,"Android 8 does not require Android 13 notification permission");
        Build.VERSION.SDK_INT=35;
        c=new Context();s=state(future);save(c,s);initial=c.alarms.pending;long actual=c.alarms.due;
        HatchScheduler.testDelayedNotification(c);PendingIntent probe=c.alarms.pending;
        check(c.alarms.scheduled.size()==2&&c.alarms.scheduled.get(initial)==actual,"delayed probe preserves actual hatch alarm");
        check(status(c).optLong("testScheduledAt",0)>System.currentTimeMillis(),"probe has visible future deadline");
        check(status(c).optLong("scheduledAt",0)==actual,"game status still describes real batch");
        new HatchAlarmReceiver().onReceive(c,probe.intent);check(c.notifications.notifyCalls==0,"early probe cannot post early");
        Intent stale=probe.intent;HatchScheduler.testDelayedNotification(c);probe=c.alarms.pending;
        c.prefs.edit().putLong("testAt",System.currentTimeMillis()-1).commit();
        new HatchAlarmReceiver().onReceive(c,stale);check(c.notifications.notifyCalls==0,"replaced probe rejects stale receiver");
        new HatchAlarmReceiver().onReceive(c,probe.intent);
        check(c.notifications.notifyCalls==1&&status(c).optLong("testPostedAt",0)>0,"due background probe records actual system post");
        check(c.alarms.scheduled.size()==1&&c.alarms.scheduled.get(initial)==actual,"probe delivery preserves actual hatch schedule");
        check(!status(c).optBoolean("delivered",true)&&SaveRepository.state==s,"probe does not consume batch delivery or change save");
        new HatchAlarmReceiver().onReceive(c,probe.intent);check(c.notifications.notifyCalls==1,"probe receiver replay cannot duplicate");
        c.alarms.exactAllowed=false;HatchScheduler.testDelayedNotification(c);
        check(c.alarms.inexactCalls==1,"background probe uses same inexact fallback");
        HatchScheduler.cancel(c);check(c.alarms.scheduled.size()==1,"cancelling real batch does not cancel requested probe");
        c.alarms.denyAll=true;HatchScheduler.restore(c);
        check(status(c).optLong("testScheduledAt",0)==0,"restore failure clears probe without crashing");
        c=new Context();c.alarms.denyAll=true;blocked=false;
        try{HatchScheduler.testDelayedNotification(c);}catch(IllegalStateException expected){blocked=true;}
        check(blocked&&status(c).optLong("testScheduledAt",0)==0,"failed probe reports no false success");
        System.out.println("HatchScheduler: "+assertions+" behavioral assertions passed (JVM platform fakes; not device validation).");
    }
}
