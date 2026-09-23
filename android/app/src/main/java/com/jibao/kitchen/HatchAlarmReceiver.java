package com.jibao.kitchen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/** Explicit, non-exported receiver for the app's single hatch alarm. */
public final class HatchAlarmReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        HatchScheduler.onAlarm(context, intent);
    }
}
