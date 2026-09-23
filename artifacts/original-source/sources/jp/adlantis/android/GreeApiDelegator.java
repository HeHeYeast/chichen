package jp.adlantis.android;

import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class GreeApiDelegator {
    static final String greeApiClassSpecifier = "net.gree.asdk.api.GreePlatform";
    static final String digestClassSpecifier = "net.gree.asdk.core.codec.Digest";
    static final String[] greeClasses = {greeApiClassSpecifier, digestClassSpecifier};

    protected static Class<?> getGreePlatformClass() throws ClassNotFoundException {
        return Class.forName(greeApiClassSpecifier);
    }

    public static String getSha1DigestInString(String str) throws IllegalAccessException, InstantiationException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
        try {
            Class<?> cls = Class.forName(digestClassSpecifier);
            return (String) cls.getMethod("getDigestInString", String.class).invoke(cls.getConstructor(String.class).newInstance("SHA-1"), str);
        } catch (Exception e) {
            Log.e("GreeApiDelegator", "getSha1DigestInString exception=" + e);
            logProGuardError();
            return null;
        }
    }

    public static String getUserCountry() {
        return getUserFieldValue("region");
    }

    private static String getUserFieldValue(String str) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException, InvocationTargetException {
        try {
            Class<?> greePlatformClass = getGreePlatformClass();
            Object objInvoke = greePlatformClass.getMethod("getLocalUser", new Class[0]).invoke(greePlatformClass, new Object[0]);
            if (objInvoke == null) {
                return null;
            }
            Field declaredField = objInvoke.getClass().getDeclaredField(str);
            declaredField.setAccessible(true);
            return (String) declaredField.get(objInvoke);
        } catch (Exception e) {
            Log.e("GreeApiDelegator", "getUserField('" + str + "') exception=" + e);
            logProGuardError();
            return null;
        }
    }

    public static String getUserId() {
        return getUserFieldValue("id");
    }

    public static boolean greePlatformAvailable() {
        try {
            return getGreePlatformClass() != null;
        } catch (Exception e) {
            return false;
        }
    }

    private static void logProGuardError() {
        Log.e("GreeApiDelegator", "If using ProGuard, include the following lines in your proguard.cfg file:");
        for (String str : greeClasses) {
            Log.e("GreeApiDelegator", " -keep public class " + str + " { public static *; }");
        }
    }
}
