package com.google.android.gms.internal;

import android.content.Context;
import android.webkit.WebSettings;

/* loaded from: D:\gxy_code\game\chicken_duck_test\classes.dex */
public final class ck {
    public static void a(Context context, WebSettings webSettings) {
        cj.a(context, webSettings);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
    }

    public static String getDefaultUserAgent(Context context) {
        return WebSettings.getDefaultUserAgent(context);
    }
}
