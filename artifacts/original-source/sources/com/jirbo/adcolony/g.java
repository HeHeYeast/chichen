package com.jirbo.adcolony;

import android.app.ActivityManager;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import c.NetworkManager;
import java.util.Locale;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class g {
    static String a;
    static boolean b;

    g() {
    }

    static String a() {
        return Settings.Secure.getString(AdColony.activity().getContentResolver(), "android_id");
    }

    static String b() {
        String networkOperatorName = ((TelephonyManager) AdColony.activity().getSystemService("phone")).getNetworkOperatorName();
        return networkOperatorName.length() == 0 ? NetworkManager.TYPE_UNKNOWN : networkOperatorName;
    }

    static int c() {
        Context applicationContext = a.b().getApplicationContext();
        a.b();
        return ((ActivityManager) applicationContext.getSystemService("activity")).getMemoryClass();
    }

    static long d() {
        Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) / 1048576;
    }

    static String e() {
        return aj.a(a.b());
    }

    static int f() {
        return a.b().getWindowManager().getDefaultDisplay().getWidth();
    }

    static int g() {
        return a.b().getWindowManager().getDefaultDisplay().getHeight();
    }

    static String h() {
        return "";
    }

    static boolean i() {
        if (a.X != null) {
            return a.X.equals("tablet");
        }
        DisplayMetrics displayMetrics = AdColony.activity().getResources().getDisplayMetrics();
        float f = displayMetrics.widthPixels / displayMetrics.xdpi;
        float f2 = displayMetrics.heightPixels / displayMetrics.ydpi;
        return Math.sqrt((double) ((f2 * f2) + (f * f))) >= 6.0d;
    }

    static String j() {
        return Locale.getDefault().getLanguage();
    }

    static String k() {
        try {
            return ((WifiManager) AdColony.activity().getSystemService("wifi")).getConnectionInfo().getMacAddress();
        } catch (RuntimeException e) {
            return null;
        }
    }

    static String l() {
        return Build.MANUFACTURER;
    }

    static String m() {
        return Build.MODEL;
    }

    static String n() {
        return "";
    }

    static String o() {
        return Build.VERSION.RELEASE;
    }
}
