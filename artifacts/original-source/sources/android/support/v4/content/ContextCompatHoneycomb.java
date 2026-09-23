package android.support.v4.content;

import android.content.Context;
import android.content.Intent;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class ContextCompatHoneycomb {
    ContextCompatHoneycomb() {
    }

    static void startActivities(Context context, Intent[] intents) {
        context.startActivities(intents);
    }
}
