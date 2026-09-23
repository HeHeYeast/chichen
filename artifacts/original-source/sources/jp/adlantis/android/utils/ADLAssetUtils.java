package jp.adlantis.android.utils;

import android.content.Context;
import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public class ADLAssetUtils {
    static String ANDROID_ASSET_PREFIX = "/android_asset/";

    public static InputStream inputStreamFromAssetUri(Context context, Uri uri) throws IOException {
        if (context == null) {
            return null;
        }
        return context.getAssets().open(pathFromAssetUri(uri));
    }

    public static boolean isAssetUri(Uri uri) {
        return uri.getScheme().equals("file") && uri.getPath().startsWith(ANDROID_ASSET_PREFIX);
    }

    public static boolean isAssetUrl(String str) {
        return str != null && isAssetUri(Uri.parse(str));
    }

    static String pathFromAssetUri(Uri uri) {
        if (isAssetUri(uri)) {
            return uri.getPath().substring(ANDROID_ASSET_PREFIX.length());
        }
        return null;
    }

    public static String uriForAsset(String str) {
        return "file://" + ANDROID_ASSET_PREFIX + str;
    }
}
