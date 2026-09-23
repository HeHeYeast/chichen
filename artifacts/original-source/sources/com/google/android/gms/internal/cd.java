package com.google.android.gms.internal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import java.util.Locale;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class cd {
    public final int hh;
    public final boolean hi;
    public final boolean hj;
    public final String hk;
    public final String hl;
    public final boolean hm;
    public final boolean hn;
    public final boolean ho;
    public final String hp;
    public final String hq;
    public final int hr;
    public final int hs;
    public final int ht;
    public final int hu;
    public final int hv;
    public final int hw;
    public final float hx;
    public final int hy;
    public final int hz;

    public cd(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Locale locale = Locale.getDefault();
        PackageManager packageManager = context.getPackageManager();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        this.hh = audioManager.getMode();
        this.hi = a(packageManager, "geo:0,0?q=donuts") != null;
        this.hj = a(packageManager, "http://www.google.com") != null;
        this.hk = telephonyManager.getNetworkOperator();
        this.hl = locale.getCountry();
        this.hm = cm.aq();
        this.hn = audioManager.isMusicActive();
        this.ho = audioManager.isSpeakerphoneOn();
        this.hp = locale.getLanguage();
        this.hq = a(packageManager);
        this.hr = audioManager.getStreamVolume(3);
        this.hs = a(context, connectivityManager, packageManager);
        this.ht = telephonyManager.getNetworkType();
        this.hu = telephonyManager.getPhoneType();
        this.hv = audioManager.getRingerMode();
        this.hw = audioManager.getStreamVolume(2);
        this.hx = displayMetrics.density;
        this.hy = displayMetrics.widthPixels;
        this.hz = displayMetrics.heightPixels;
    }

    private static int a(Context context, ConnectivityManager connectivityManager, PackageManager packageManager) {
        if (!ci.a(packageManager, context.getPackageName(), "android.permission.ACCESS_NETWORK_STATE")) {
            return -2;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.getType();
        }
        return -1;
    }

    private static ResolveInfo a(PackageManager packageManager, String str) {
        return packageManager.resolveActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)), AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
    }

    private static String a(PackageManager packageManager) throws PackageManager.NameNotFoundException {
        ActivityInfo activityInfo;
        ResolveInfo resolveInfoA = a(packageManager, "market://details?id=com.google.android.gms.ads");
        if (resolveInfoA == null || (activityInfo = resolveInfoA.activityInfo) == null) {
            return null;
        }
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(activityInfo.packageName, 0);
            if (packageInfo != null) {
                return packageInfo.versionCode + "." + activityInfo.packageName;
            }
            return null;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }
}
