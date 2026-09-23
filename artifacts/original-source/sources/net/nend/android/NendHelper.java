package net.nend.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.text.TextUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
final class NendHelper {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static boolean mDebuggable;
    private static boolean mDev;

    static {
        $assertionsDisabled = !NendHelper.class.desiredAssertionStatus();
        mDebuggable = false;
        mDev = false;
    }

    private NendHelper() {
    }

    static void setDebuggable(Context context) {
        mDebuggable = MetaDataHelper.getBooleanValue(context, "NendDebuggable", false);
        mDev = MetaDataHelper.getBooleanValue(context, "NendDev", false);
    }

    static void disableDebug() {
        mDebuggable = false;
        mDev = false;
    }

    static boolean isDebuggable() {
        return mDebuggable;
    }

    static boolean isDev() {
        return mDev;
    }

    static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    static String makeUid(Context context) throws NoSuchAlgorithmException {
        if (!$assertionsDisabled && context == null) {
            throw new AssertionError();
        }
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String prefNendUid = pref.getString("NENDUUID", "");
        if (TextUtils.isEmpty(prefNendUid)) {
            String uid = md5String(UUID.randomUUID().toString());
            SharedPreferences.Editor prefEditor = pref.edit();
            prefEditor.putString("NENDUUID", uid);
            prefEditor.commit();
            return uid;
        }
        return prefNendUid;
    }

    static String md5String(String str) throws NoSuchAlgorithmException {
        byte[] mdbytes = new byte[16];
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());
            mdbytes = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            NendLog.e("nend_SDK", e.getMessage(), e);
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : mdbytes) {
            sb.append(Integer.toString((b & 255) + 256, 16).substring(1));
        }
        return sb.toString();
    }

    static void startBrowser(Context context, String url) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        if (hasImplicitIntent(context, intent)) {
            intent.setFlags(268435456);
            context.startActivity(intent);
        }
    }

    private static boolean hasImplicitIntent(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> apps = packageManager.queryIntentActivities(intent, AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
        return apps.size() > 0;
    }

    public static int setReloadIntervalInSeconds(int reloadIntervalInSeconds) {
        if (reloadIntervalInSeconds > 99999) {
            return 99999;
        }
        if (!isDev() && reloadIntervalInSeconds <= 30) {
            return 30;
        }
        return reloadIntervalInSeconds;
    }

    public static class MetaDataHelper {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !NendHelper.class.desiredAssertionStatus();
        }

        public static boolean getBooleanValue(Context context, String key, boolean defaultValue) throws PackageManager.NameNotFoundException {
            Bundle metaData = getMetaData(context);
            if (metaData != null) {
                return metaData.getBoolean(key, defaultValue);
            }
            return defaultValue;
        }

        public static String getStringValue(Context context, String key, String defaultValue) throws PackageManager.NameNotFoundException {
            Bundle metaData = getMetaData(context);
            if (metaData != null && metaData.getString(key) != null) {
                return metaData.getString(key);
            }
            return defaultValue;
        }

        private static Bundle getMetaData(Context context) throws PackageManager.NameNotFoundException {
            try {
                ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
                return applicationInfo.metaData;
            } catch (PackageManager.NameNotFoundException e) {
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                NendLog.d(NendStatus.ERR_UNEXPECTED, e);
                return null;
            }
        }
    }

    @SuppressLint({"NewApi"})
    public static class AsyncTaskHelper {
        public static <T> void execute(AsyncTask<T, ?, ?> task, T... tArr) {
            if (Build.VERSION.SDK_INT >= 11) {
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, tArr);
            } else {
                task.execute(tArr);
            }
        }
    }
}
