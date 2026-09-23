package android.support.v4.app;

import android.app.Activity;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ActivityCompat extends ContextCompat {
    public static boolean invalidateOptionsMenu(Activity activity) {
        if (Build.VERSION.SDK_INT < 11) {
            return false;
        }
        ActivityCompatHoneycomb.invalidateOptionsMenu(activity);
        return true;
    }
}
