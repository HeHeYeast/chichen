package jp.adlantis.android.utils;

import android.net.Uri;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ADLStringUtils {
    public static boolean isHttpUrl(String str) {
        if (str == null) {
            return false;
        }
        String scheme = Uri.parse(str).getScheme();
        return "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
    }
}
