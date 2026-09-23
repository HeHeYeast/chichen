package vpadn;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Debug;
import java.text.DecimalFormat;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class ae {
    private static int a = 0;

    public static boolean a(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return packageManager.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", packageManager.getPackageInfo(context.getPackageName(), 0).packageName) != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean b(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return packageManager.checkPermission("android.permission.ACCESS_FINE_LOCATION", packageManager.getPackageInfo(context.getPackageName(), 0).packageName) != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean c(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return packageManager.checkPermission("android.permission.ACCESS_COARSE_LOCATION", packageManager.getPackageInfo(context.getPackageName(), 0).packageName) != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean d(Context context) {
        if (a == 0) {
            try {
                a = 1;
                String[] strArr = {"android.permission.INTERNET", "android.permission.READ_PHONE_STATE", "android.permission.ACCESS_NETWORK_STATE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_WIFI_STATE"};
                new String[1][0] = "android.permission.INTERNET";
                PackageManager packageManager = context.getPackageManager();
                String str = packageManager.getPackageInfo(context.getPackageName(), 0).packageName;
                int i = 0;
                while (true) {
                    if (i >= strArr.length) {
                        break;
                    }
                    if (packageManager.checkPermission(strArr[i], str) != -1) {
                        i++;
                    } else {
                        ab.b("VponUtil", "The permission:" + strArr[i] + " is mandatory for VPON AD, please add this permission to AndroidManifest.xml");
                        a = 2;
                        break;
                    }
                }
                boolean z = packageManager.checkPermission("android.permission.ACCESS_COARSE_LOCATION", str) != -1;
                boolean z2 = packageManager.checkPermission("android.permission.ACCESS_FINE_LOCATION", str) != -1;
                if (!z && !z2) {
                    a = 2;
                    ab.b("VponUtil", "Lost location permission");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                a = 0;
            }
        }
        return a == 1;
    }

    public static int a(Double d, Double d2, double d3, double d4) {
        if (d == null || d2 == null || (d3 == 0.0d && d4 == 0.0d)) {
            return -1;
        }
        double dDoubleValue = 0.017453292519943295d * d.doubleValue();
        double dDoubleValue2 = 0.017453292519943295d * d2.doubleValue();
        double d5 = 0.017453292519943295d * d3;
        double dAcos = Math.acos((Math.cos(dDoubleValue) * Math.cos(d5) * Math.cos((0.017453292519943295d * d4) - dDoubleValue2)) + (Math.sin(dDoubleValue) * Math.sin(d5))) * 6371.0d;
        if (Double.isNaN(dAcos)) {
            return -1;
        }
        return (int) (dAcos * 1000.0d);
    }

    public static void a() {
        Double dValueOf = Double.valueOf(Double.valueOf(Debug.getNativeHeapAllocatedSize()).doubleValue() / new Double(1048576.0d).doubleValue());
        Double dValueOf2 = Double.valueOf(Double.valueOf(Debug.getNativeHeapSize()).doubleValue() / 1048576.0d);
        Double dValueOf3 = Double.valueOf(Double.valueOf(Debug.getNativeHeapFreeSize()).doubleValue() / 1048576.0d);
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        ab.a("VponUtil", "debug. =================================");
        ab.a("VponUtil", "debug.heap native: allocated " + decimalFormat.format(dValueOf) + "MB of " + decimalFormat.format(dValueOf2) + "MB (" + decimalFormat.format(dValueOf3) + "MB free)");
        ab.a("VponUtil", "debug.memory: allocated: " + decimalFormat.format(Double.valueOf(Runtime.getRuntime().totalMemory() / 1048576)) + "MB of " + decimalFormat.format(Double.valueOf(Runtime.getRuntime().maxMemory() / 1048576)) + "MB (" + decimalFormat.format(Double.valueOf(Runtime.getRuntime().freeMemory() / 1048576)) + "MB free)");
    }
}
