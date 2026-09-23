package android.support.v4.net;

import android.net.ConnectivityManager;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class ConnectivityManagerCompatJellyBean {
    ConnectivityManagerCompatJellyBean() {
    }

    public static boolean isActiveNetworkMetered(ConnectivityManager cm) {
        return cm.isActiveNetworkMetered();
    }
}
