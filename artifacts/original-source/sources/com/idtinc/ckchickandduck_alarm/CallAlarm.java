package com.idtinc.ckchickandduck_alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.AppMainActivity;
import com.idtinc.ckchickandduck.R;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class CallAlarm extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent preIntent) throws Resources.NotFoundException {
        Bundle preBundle = preIntent.getExtras();
        Log.i("CallAlarm", "CallAlarm:" + preBundle.getString("body_string"));
        AppDelegate appDelegate = (AppDelegate) context.getApplicationContext();
        if (appDelegate != null) {
            NotificationManager noMgr = (NotificationManager) appDelegate.getSystemService("notification");
            Intent call = new Intent(appDelegate, (Class<?>) AppMainActivity.class);
            call.putExtra("notiId", 1);
            PendingIntent pIntent = PendingIntent.getActivity(appDelegate, 0, call, 0);
            String ticket = preBundle.getString("body_string");
            long when = System.currentTimeMillis();
            Notification notification = new Notification(R.drawable.icon, ticket, when);
            String title = appDelegate.getResources().getString(R.string.app_name);
            String desc = preBundle.getString("body_string");
            notification.setLatestEventInfo(appDelegate, title, desc, pIntent);
            notification.defaults |= 32;
            String egg_id = preBundle.getString("egg_id");
            if (egg_id != null && egg_id.equals("1")) {
                notification.sound = Uri.parse("android.resource://com.idtinc.ckchickandduck/2131034126");
            } else {
                notification.sound = Uri.parse("android.resource://com.idtinc.ckchickandduck/2131034124");
            }
            notification.number = 1;
            noMgr.notify(0, notification);
            Toast.makeText(appDelegate, preBundle.getString("body_string"), 0).show();
        }
    }
}
