package com.jirbo.adcolony;

import android.content.pm.PackageManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
class ab {
    static byte[] a = new byte[1024];
    static StringBuilder b = new StringBuilder();

    ab() {
    }

    static boolean a(String str) throws PackageManager.NameNotFoundException {
        try {
            AdColony.activity().getApplication().getPackageManager().getApplicationInfo(str, 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static String a() {
        try {
            return AdColony.activity().getPackageManager().getPackageInfo(AdColony.activity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            com.jirbo.adcolony.a.e("Failed to retrieve package info.");
            return "1.0";
        }
    }

    static String b(String str) {
        try {
            return ai.a(str);
        } catch (Exception e) {
            return null;
        }
    }

    static String b() {
        return UUID.randomUUID().toString();
    }

    static double c() {
        return System.currentTimeMillis() / 1000.0d;
    }

    static String a(double d, int i) {
        StringBuilder sb = new StringBuilder();
        a(d, i, sb);
        return sb.toString();
    }

    static void a(double d, int i, StringBuilder sb) {
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            sb.append(d);
            return;
        }
        if (d < 0.0d) {
            d = -d;
            sb.append('-');
        }
        if (i == 0) {
            sb.append(Math.round(d));
            return;
        }
        long jPow = (long) Math.pow(10.0d, i);
        long jRound = Math.round(jPow * d);
        sb.append(jRound / jPow);
        sb.append('.');
        long j = jRound % jPow;
        if (j == 0) {
            for (int i2 = 0; i2 < i; i2++) {
                sb.append('0');
            }
            return;
        }
        for (long j2 = j * 10; j2 < jPow; j2 *= 10) {
            sb.append('0');
        }
        sb.append(j);
    }

    static boolean d() {
        return new File(new StringBuilder().append(com.jirbo.adcolony.a.l.f.c()).append("/../lib/libImmEndpointWarpJ.so").toString()).exists();
    }

    static String c(String str) {
        return a(str, "");
    }

    static String a(String str, String str2) {
        String string;
        if (str != null) {
            try {
                l.a.a("Loading ").b((Object) str);
                FileInputStream fileInputStream = new FileInputStream(str);
                synchronized (a) {
                    b.setLength(0);
                    b.append(str2);
                    for (int i = fileInputStream.read(a, 0, a.length); i != -1; i = fileInputStream.read(a, 0, a.length)) {
                        for (int i2 = 0; i2 < i; i2++) {
                            b.append((char) a[i2]);
                        }
                    }
                    fileInputStream.close();
                    string = b.toString();
                }
                return string;
            } catch (IOException e) {
                l.d.a("Unable to load ").b((Object) str);
                return "";
            }
        }
        return "";
    }

    static boolean e() {
        return com.jirbo.adcolony.a.b().checkCallingOrSelfPermission("android.permission.VIBRATE") == 0;
    }

    static String f() {
        return com.jirbo.adcolony.a.b().getPackageName();
    }

    static class a {
        long a = System.currentTimeMillis();

        a() {
        }

        void a() {
            this.a = System.currentTimeMillis();
        }

        double b() {
            return (System.currentTimeMillis() - this.a) / 1000.0d;
        }

        public String toString() {
            return ab.a(b(), 2);
        }
    }

    static class b {
        double a = System.currentTimeMillis();

        b(double d) {
            a(d);
        }

        void a(double d) {
            this.a = (System.currentTimeMillis() / 1000.0d) + d;
        }

        boolean a() {
            return b() == 0.0d;
        }

        double b() {
            double dCurrentTimeMillis = this.a - (System.currentTimeMillis() / 1000.0d);
            if (dCurrentTimeMillis <= 0.0d) {
                return 0.0d;
            }
            return dCurrentTimeMillis;
        }

        public String toString() {
            return ab.a(b(), 2);
        }
    }
}
