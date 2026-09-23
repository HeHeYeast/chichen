package jp.co.voyagegroup.android.fluct.jar.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.net.URLEncoder;
import jp.co.voyagegroup.android.fluct.jar.sdk.FluctPreferences;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class FluctUtils {
    private static final String TAG = "FluctUtils";

    public static String getAdid(Context context) {
        Log.d(TAG, "getAdid : ");
        FluctPreferences perferences = FluctPreferences.getInstance();
        String adid = perferences.getAdid();
        Log.v(TAG, "getAdid : adid is " + adid);
        return adid;
    }

    public static String getNotAdTrackingParam(Context context) {
        Log.d(TAG, "getNotAdTrackingParam : ");
        FluctPreferences perferences = FluctPreferences.getInstance();
        String getNotAdTrackingParam = perferences.getNotAdTrackingParam();
        Log.v(TAG, "getNotAdTrackingParam : Not Ad tracking param is " + getNotAdTrackingParam);
        return getNotAdTrackingParam;
    }

    public static boolean checkPermission(Context context) {
        Log.d(TAG, "checkPermission : ");
        PackageManager packagemanager = context.getPackageManager();
        String packageName = context.getPackageName();
        if (packagemanager.checkPermission("android.permission.INTERNET", packageName) != -1 && packagemanager.checkPermission("android.permission.ACCESS_NETWORK_STATE", packageName) != -1) {
            return true;
        }
        return false;
    }

    public static boolean isNetWorkAvailable(Context context) {
        Log.d(TAG, "isNetWorkAvailable : ");
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }

    public static String getConfigURL(String mediaId) {
        Log.d(TAG, "getConfigURL : mediaId is " + mediaId);
        String url = new StringBuilder(String.valueOf(getConfigUrl()) + FluctConstants.CONFIG_URL_PARAM).toString();
        String url2 = url.replaceAll(FluctConstants.REPLACE_APPLICATION_ID, URLEncoder.encode(mediaId));
        String version = String.valueOf(FluctConstants.SDK_VERSION_PREFIX) + Build.VERSION.RELEASE.split("\\.")[0];
        return url2.replaceAll(FluctConstants.REPLACE_SDK_VERSION, URLEncoder.encode(version));
    }

    private static String getConfigUrl() {
        Log.d(TAG, "getConfigUrl : ");
        return FluctConstants.CONFIG_PROC_URL;
    }

    public static byte[] objectToBytes(Object object) throws IOException {
        Log.d(TAG, "objectToBytes : ");
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
            objOut.writeObject(object);
            objOut.flush();
            return byteOut.toByteArray();
        } catch (IOException e) {
            Log.e(TAG, "objectToBytes : IOException is " + e.getLocalizedMessage());
            return null;
        }
    }

    public static Object bytesToObject(byte[] bytes) {
        Log.d(TAG, "bytesToObject : ");
        if (bytes != null) {
            try {
                ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
                ObjectInputStream objIn = new ObjectInputStream(byteIn);
                return objIn.readObject();
            } catch (OptionalDataException e) {
                Log.e(TAG, "bytesToObject : OptionalDataException is " + e.getLocalizedMessage());
            } catch (StreamCorruptedException e2) {
                Log.e(TAG, "bytesToObject : StreamCorruptedException is " + e2.getLocalizedMessage());
            } catch (IOException e3) {
                Log.e(TAG, "bytesToObject : IOException is " + e3.getLocalizedMessage());
            } catch (ClassNotFoundException e4) {
                Log.e(TAG, "bytesToObject : ClassNotFoundException is " + e4.getLocalizedMessage());
            }
        }
        return null;
    }

    public static String getDefaultMediaId(Context context) throws PackageManager.NameNotFoundException {
        Log.d(TAG, "getDefaultMediaId : ");
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            Bundle metaDateBundle = applicationInfo.metaData;
            if (metaDateBundle == null || metaDateBundle.get(FluctConstants.META_DATA_MEDIA_ID) == null) {
                return "";
            }
            Object defaultMediaId = metaDateBundle.get(FluctConstants.META_DATA_MEDIA_ID);
            String mediaId = defaultMediaId.toString();
            return mediaId;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getDefaultMediaId : NameNotFoundException is " + e.getLocalizedMessage());
            return "";
        }
    }

    public static int getCurrentTimeSec() {
        long currentTimeMsec = System.currentTimeMillis() / 1000;
        int result = (int) currentTimeMsec;
        return result;
    }

    public static String replaceParams(Context context, String str) {
        Log.d(TAG, "replaceUrlParams : String is " + str);
        if (str.indexOf(FluctConstants.REPLACE_AD_ID_NOT_TRACKING_URL_PARAM) > -1) {
            String notAdTrackingParam = getNotAdTrackingParam(context);
            str = str.replaceAll(FluctConstants.REPLACE_AD_ID_NOT_TRACKING_URL_PARAM, notAdTrackingParam);
        }
        if (str.indexOf(FluctConstants.REPLACE_AD_ID_URL_PARAM) > -1) {
            String adid = getAdid(context);
            str = str.replaceAll(FluctConstants.REPLACE_AD_ID_URL_PARAM, adid);
        }
        Log.v(TAG, "replaceUrlParams : Replaced String is " + str);
        return str;
    }
}
