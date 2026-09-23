package com.jirbo.adcolony;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class q {
    public static final int a = 30;
    public static String b = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx  x          xxxxxxx                          xxxx x                          xxxxx";

    /* renamed from: c, reason: collision with root package name */
    public static String f258c = "0123456789ABCDEF";
    public static String d = "0123456789abcdef";

    q() {
    }

    static boolean a() {
        if (a.E) {
            return true;
        }
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) AdColony.activity().getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return false;
        }
        return activeNetworkInfo.getType() == 1;
    }

    static boolean b() {
        NetworkInfo activeNetworkInfo;
        if (a.E || (activeNetworkInfo = ((ConnectivityManager) AdColony.activity().getSystemService("connectivity")).getActiveNetworkInfo()) == null) {
            return false;
        }
        int type = activeNetworkInfo.getType();
        return type == 0 || type >= 2;
    }

    static boolean c() {
        return a() || b();
    }

    public static String d() {
        return a() ? "wifi" : b() ? "cell" : "offline";
    }

    public static String a(String str) {
        StringBuilder sb = new StringBuilder();
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt < 128 && b.charAt(cCharAt) == ' ') {
                sb.append(cCharAt);
            } else {
                sb.append('%');
                int i2 = (cCharAt >> 4) & 15;
                int i3 = cCharAt & 15;
                if (i2 < 10) {
                    sb.append((char) (i2 + 48));
                } else {
                    sb.append((char) ((i2 + 65) - 10));
                }
                if (i3 < 10) {
                    sb.append((char) (i3 + 48));
                } else {
                    sb.append((char) ((i3 + 65) - 10));
                }
            }
        }
        return sb.toString();
    }

    public static int a(char c2) {
        int iIndexOf = f258c.indexOf(c2);
        if (iIndexOf < 0) {
            int iIndexOf2 = d.indexOf(c2);
            if (iIndexOf2 < 0) {
                return 0;
            }
            return iIndexOf2;
        }
        return iIndexOf;
    }

    public static String b(String str) {
        StringBuilder sb = new StringBuilder();
        int length = str.length();
        int i = 0;
        while (i < length) {
            char cCharAt = str.charAt(i);
            if (cCharAt == '%') {
                char cCharAt2 = i + 1 < length ? str.charAt(i + 1) : '0';
                char cCharAt3 = i + 2 < length ? str.charAt(i + 2) : '0';
                i += 2;
                sb.append((char) (a(cCharAt3) | (a(cCharAt2) << 8)));
            } else {
                sb.append(cCharAt);
            }
            i++;
        }
        return sb.toString();
    }
}
