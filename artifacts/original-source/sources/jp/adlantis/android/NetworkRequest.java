package jp.adlantis.android;

import android.util.Log;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class NetworkRequest {
    protected static String DEBUG_TASK = "NetworkRequest";

    static AbstractHttpClient httpClientFactory() {
        return new DefaultHttpClient();
    }

    AdManager adManager() {
        return AdManager.getInstance();
    }

    AdNetworkConnection getAdNetworkConnection() {
        return adManager().getAdNetworkConnection();
    }

    protected void log_d(String str) {
        Log.d(DEBUG_TASK, str);
    }

    protected void log_e(String str) {
        Log.e(DEBUG_TASK, str);
    }
}
