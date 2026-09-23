package com.idtinc.ckchickandduck;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gcm.GCMBaseIntentService;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class GCMIntentService extends GCMBaseIntentService {
    private static final String SENDER_ID = "235863556449";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    @Override // com.google.android.gcm.GCMBaseIntentService
    public void onRegistered(Context context, String registrationId) {
        AppDelegate appDelegate;
        Log.w("registration id:", registrationId);
        if (registrationId != null && registrationId.length() > 0 && (appDelegate = (AppDelegate) getApplicationContext()) != null) {
            appDelegate.set_device_token(registrationId);
            appDelegate.post_device_token();
        }
    }

    @Override // com.google.android.gcm.GCMBaseIntentService
    protected void onUnregistered(Context context, String registrationId) {
    }

    @Override // com.google.android.gcm.GCMBaseIntentService
    public void onError(Context context, String errorId) {
    }

    @Override // com.google.android.gcm.GCMBaseIntentService
    protected void onMessage(Context context, Intent intent) {
        Log.i(GCMBaseIntentService.TAG, "Received message");
        Bundle gcmData = intent.getExtras();
        generateNotification(context, gcmData);
    }

    private static void generateNotification(Context context, Bundle gcmData) {
        Intent gcmIntent;
        if (gcmData != null) {
            String gcmAlertString = gcmData.getString("alert");
            if (gcmAlertString == null) {
                gcmAlertString = "";
            }
            String gcmMessageString = gcmData.getString("message");
            if (gcmMessageString == null) {
                gcmMessageString = "";
            }
            String gcmUrlString = gcmData.getString("url");
            if (gcmUrlString == null) {
                gcmUrlString = "";
            }
            if (gcmAlertString.length() > 0) {
                long when = System.currentTimeMillis();
                NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
                if (gcmUrlString.length() <= 0) {
                    gcmIntent = new Intent(context, (Class<?>) AppMainActivity.class);
                    gcmIntent.putExtras(gcmData);
                    gcmIntent.setFlags(603979776);
                } else {
                    Uri uri = Uri.parse(gcmUrlString);
                    gcmIntent = new Intent("android.intent.action.VIEW", uri);
                }
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, gcmIntent, 0);
                Notification gcmNotification = new NotificationCompat.Builder(context).setContentTitle(gcmAlertString).setContentText(gcmMessageString).setContentIntent(pendingIntent).setDefaults(-1).setSmallIcon(R.drawable.icon).setWhen(when).build();
                notificationManager.notify(0, gcmNotification);
                AppDelegate appDelegate = (AppDelegate) context.getApplicationContext();
                if (appDelegate != null) {
                    if (appDelegate.defaultSharedPreferences == null) {
                        appDelegate.defaultSharedPreferences = appDelegate.getSharedPreferences("default", 0);
                    }
                    if (appDelegate.defaultSharedPreferences != null) {
                        SharedPreferences.Editor editor = appDelegate.defaultSharedPreferences.edit();
                        editor.putString("gcm_alert", gcmAlertString);
                        editor.putString("gcm_message", gcmMessageString);
                        editor.putString("gcm_url", gcmUrlString);
                        editor.commit();
                    }
                    appDelegate.gcmCheckF = true;
                }
            }
        }
    }
}
