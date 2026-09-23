package vpadn;

import android.util.Log;

/* renamed from: vpadn.r, reason: case insensitive filesystem */
/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class C0104r {
    private static int a = 6;

    public static void a(String str) {
        if ("VERBOSE".equals(str)) {
            a = 2;
        } else if ("DEBUG".equals(str)) {
            a = 3;
        } else if ("INFO".equals(str)) {
            a = 4;
        } else if ("WARN".equals(str)) {
            a = 5;
        } else if ("ERROR".equals(str)) {
            a = 6;
        }
        Log.i("CordovaLog", "Changing log level to " + str + "(" + a + ")");
    }

    public static boolean a(int i) {
        return 3 >= a;
    }

    public static void a(String str, String str2) {
        if (2 >= a) {
            Log.v(str, str2);
        }
    }

    public static void b(String str, String str2) {
        if (3 >= a) {
            Log.d(str, str2);
        }
    }

    public static void c(String str, String str2) {
        if (4 >= a) {
            Log.i(str, str2);
        }
    }

    public static void d(String str, String str2) {
        if (5 >= a) {
            Log.w(str, str2);
        }
    }

    public static void e(String str, String str2) {
        if (6 >= a) {
            Log.e(str, str2);
        }
    }

    public static void a(String str, String str2, Throwable th) {
        if (5 >= a) {
            Log.w(str, str2, th);
        }
    }

    public static void b(String str, String str2, Throwable th) {
        if (6 >= a) {
            Log.e(str, str2, th);
        }
    }

    public static void a(String str, String str2, Object... objArr) {
        if (2 >= a) {
            Log.v(str, String.format(str2, objArr));
        }
    }

    public static void b(String str, String str2, Object... objArr) {
        if (3 >= a) {
            Log.d(str, String.format(str2, objArr));
        }
    }
}
