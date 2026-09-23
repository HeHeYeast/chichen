package com.idtinc.ckchickandduck_alarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import com.idtinc.ckchickandduck.AppDelegate;
import com.idtinc.ckchickandduck.AppMainActivity;
import com.idtinc.ckchickandduck.R;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class AlarmAlert extends Activity {
    AppDelegate appDelegate = null;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) throws Resources.NotFoundException {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(3);
        Log.i("AlarmAlert", "AlarmAlert:");
        Bundle bundle = getIntent().getExtras();
        if (this.appDelegate == null) {
            this.appDelegate = (AppDelegate) getApplicationContext();
        }
        NotificationManager notificationManager = (NotificationManager) this.appDelegate.getSystemService("notification");
        Intent call = new Intent(this.appDelegate, (Class<?>) AppMainActivity.class);
        call.putExtra("notiId", 1);
        PendingIntent pIntent = PendingIntent.getActivity(this.appDelegate, 0, call, 0);
        String ticket = bundle.getString("body_string");
        long when = System.currentTimeMillis();
        Notification notification = new Notification(R.drawable.icon, ticket, when);
        String title = this.appDelegate.getResources().getString(R.string.app_name);
        String desc = bundle.getString("body_string");
        notification.setLatestEventInfo(this.appDelegate, title, desc, pIntent);
        notification.defaults |= 32;
        notification.sound = Uri.parse("android.resource://com.idtinc.ckchickandduck/2131034124");
        notification.number = 1;
        notificationManager.notify(0, notification);
        new AlertDialog.Builder(this).setIcon(R.drawable.icon).setTitle(R.string.app_name).setMessage(bundle.getString("body_string")).setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.idtinc.ckchickandduck_alarm.AlarmAlert.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
                AlarmAlert.this.finishThis();
            }
        }).show();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || event.getRepeatCount() != 0) {
            return super.onKeyDown(keyCode, event);
        }
        finishThis();
        return true;
    }

    public void finishThis() {
        Intent intent = new Intent();
        intent.setClass(this.appDelegate, AppMainActivity.class);
        startActivity(intent);
        this.appDelegate = null;
        finish();
    }
}
