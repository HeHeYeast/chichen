package jp.co.imobile.sdkads.android;

import android.util.Log;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class x {
    x() {
    }

    static void a(String str, String str2) {
        Log.d("i-mobile SDK ADS ERROR", "[ErrorLog]" + str + " Message:" + str2);
    }

    static void a(Throwable th) {
        if (!ImobileSdkAd.c().booleanValue() || th == null) {
            return;
        }
        th.printStackTrace();
    }

    static void b(String str, String str2) {
        if (ImobileSdkAd.c().booleanValue()) {
            Log.d("i-mobile SDK ADS DEBUG", "[ForceLog]" + str + " Message:" + str2);
        }
    }
}
