package android.support.v4.view;

import android.view.MenuItem;
import android.view.View;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class MenuItemCompatHoneycomb {
    MenuItemCompatHoneycomb() {
    }

    public static void setShowAsAction(MenuItem item, int actionEnum) {
        item.setShowAsAction(actionEnum);
    }

    public static MenuItem setActionView(MenuItem item, View view) {
        return item.setActionView(view);
    }
}
