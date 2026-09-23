package vpadn;

import android.util.Log;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class ab {
    private static boolean a = true;
    private static boolean b = true;

    public static void a() {
        Log.w("VPADN", "call setOnlyShowWarningAndErrorLog");
        a = false;
        b = false;
    }

    public static void b() {
        a = true;
        b = true;
    }

    public static int a(String str, String str2) {
        if (a) {
            return Log.d("VPADN", "[::" + str + "::]  " + str2);
        }
        return 0;
    }

    public static int b(String str, String str2) {
        return Log.e("VPADN", "[::" + str + "::]  " + str2);
    }

    public static int a(String str, String str2, Throwable th) {
        return Log.e("VPADN", "[::" + str + "::]  " + str2, th);
    }

    public static int c(String str, String str2) {
        if (b) {
            return Log.i("VPADN", "[::" + str + "::]  " + str2);
        }
        return 0;
    }

    public static int d(String str, String str2) {
        return Log.w("VPADN", "[::" + str + "::]  " + str2);
    }

    public static int b(String str, String str2, Throwable th) {
        return Log.w("VPADN", "[::" + str + "::]  " + str2, th);
    }
}
