package com.immersion.hapticmediasdk.utils;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class Log {
    private static final boolean a = false;

    /* renamed from: b044A044Aъъъ044A, reason: contains not printable characters */
    public static int f96b044A044A044A = 48;

    /* renamed from: b044Aъ044Aъъ044A, reason: contains not printable characters */
    public static int f97b044A044A044A = 1;

    /* renamed from: bъъ044Aъъ044A, reason: contains not printable characters */
    public static int f98b044A044A = 0;

    /* renamed from: bъъъ044Aъ044A, reason: contains not printable characters */
    public static int f99b044A044A = 2;

    public Log() {
        if (((f96b044A044A044A + f97b044A044A044A) * f96b044A044A044A) % m123b044A044A044A() != f98b044A044A) {
            f96b044A044A044A = 0;
            f98b044A044A = m121b044A044A044A044A();
        }
    }

    /* renamed from: b044A044A044Aъъ044A, reason: contains not printable characters */
    public static int m121b044A044A044A044A() {
        return 1;
    }

    /* renamed from: b044Aъъ044Aъ044A, reason: contains not printable characters */
    public static int m122b044A044A044A() {
        return 1;
    }

    /* renamed from: bъ044A044Aъъ044A, reason: contains not printable characters */
    public static int m123b044A044A044A() {
        return 2;
    }

    public static void d(String str, String str2) {
    }

    public static void e(String str, String str2) {
        android.util.Log.e(str, str2);
        while (true) {
            switch (1) {
                case 0:
                case 1:
                    break;
                default:
                    while (true) {
                        boolean z = false;
                        switch (z) {
                        }
                    }
                    break;
            }
        }
        int iM121b044A044A044A044A = m121b044A044A044A044A();
        switch ((iM121b044A044A044A044A * (f97b044A044A044A + iM121b044A044A044A044A)) % f99b044A044A) {
            case 0:
                break;
            default:
                f96b044A044A044A = m121b044A044A044A044A();
                f98b044A044A = 56;
                break;
        }
    }

    public static void i(String str, String str2) {
        int i = f96b044A044A044A;
        switch ((i * (m122b044A044A044A() + i)) % f99b044A044A) {
            case 0:
                break;
            default:
                f96b044A044A044A = 75;
                f98b044A044A = 9;
                break;
        }
        android.util.Log.i(str, str2);
    }

    public static void v(String str, String str2) {
        while (true) {
            switch (1) {
                case 0:
                    break;
                case 1:
                    return;
                default:
                    while (true) {
                        switch (1) {
                            case 1:
                                return;
                        }
                    }
                    break;
            }
        }
    }

    public static void w(String str, String str2) {
        android.util.Log.w(str, str2);
    }
}
