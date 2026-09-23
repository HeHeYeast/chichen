package android.support.v4.app;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class TaskStackBuilderHoneycomb {
    public static PendingIntent getActivitiesPendingIntent(Context context, int requestCode, Intent[] intents, int flags) {
        return PendingIntent.getActivities(context, requestCode, intents, flags);
    }
}
